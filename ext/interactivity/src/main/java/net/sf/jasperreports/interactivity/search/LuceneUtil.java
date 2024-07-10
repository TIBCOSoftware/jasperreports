/*
 * JasperReports - Free Java Reporting Library.
 * Copyright (C) 2001 - 2023 Cloud Software Group, Inc. All rights reserved.
 * http://www.jaspersoft.com
 *
 * Unless you have purchased a commercial license agreement from Jaspersoft,
 * the following license terms apply:
 *
 * This program is part of JasperReports.
 *
 * JasperReports is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * JasperReports is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with JasperReports. If not, see <http://www.gnu.org/licenses/>.
 */
package net.sf.jasperreports.interactivity.search;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.analysis.tokenattributes.OffsetAttribute;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.FieldType;
import org.apache.lucene.document.StoredField;
import org.apache.lucene.document.StringField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexOptions;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.IndexWriterConfig.OpenMode;
import org.apache.lucene.index.PostingsEnum;
import org.apache.lucene.index.Term;
import org.apache.lucene.index.Terms;
import org.apache.lucene.index.TermsEnum;
import org.apache.lucene.search.FuzzyQuery;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.MultiTermQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.WildcardQuery;
import org.apache.lucene.search.spans.SpanMultiTermQueryWrapper;
import org.apache.lucene.search.spans.SpanNearQuery;
import org.apache.lucene.search.spans.SpanOrQuery;
import org.apache.lucene.search.spans.SpanQuery;
import org.apache.lucene.search.spans.SpanTermQuery;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.RAMDirectory;
import org.apache.lucene.util.BytesRef;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRPrintElement;
import net.sf.jasperreports.engine.JRPrintFrame;
import net.sf.jasperreports.engine.JRPrintPage;
import net.sf.jasperreports.engine.JRPrintText;
import net.sf.jasperreports.engine.JRStyledTextAttributeSelector;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReportsContext;
import net.sf.jasperreports.engine.PrintElementId;
import net.sf.jasperreports.engine.util.JRStyledText;
import net.sf.jasperreports.engine.util.JRStyledTextUtil;
import net.sf.jasperreports.search.HitSpanInfo;
import net.sf.jasperreports.search.HitTermInfo;
import net.sf.jasperreports.search.SpansInfo;

/**
 * @author Narcis Marcu (narcism@users.sourceforge.net)
 */
public class LuceneUtil {

	private static final Log log = LogFactory.getLog(LuceneUtil.class);
	private static final String CONTENT_FIELD = "content";

	private JRStyledTextAttributeSelector noneSelector;
	private JRStyledTextUtil styledTextUtil;
	private Analyzer analyzer;
	private IndexWriter writer;
	private FieldType fieldType;

	private boolean isCaseSensitive;
	private boolean isWholeWordsOnly;
	private boolean removeAccents;


	public LuceneUtil(JasperReportsContext jasperReportsContext, boolean isCaseSensitive, boolean isWholeWordsOnly, boolean removeAccents) {
		this.isCaseSensitive = isCaseSensitive;
		this.isWholeWordsOnly = isWholeWordsOnly;
		this.removeAccents = removeAccents;

		this.noneSelector = JRStyledTextAttributeSelector.getNoneSelector(jasperReportsContext);
		this.styledTextUtil = JRStyledTextUtil.getInstance(jasperReportsContext);
		
		fieldType = new FieldType();
		fieldType.setIndexOptions(IndexOptions.DOCS_AND_FREQS_AND_POSITIONS);
		fieldType.setTokenized(true);
		fieldType.setStored(true);
		fieldType.setStoreTermVectors(true);
		fieldType.setStoreTermVectorPositions(true);
		fieldType.setStoreTermVectorOffsets(true);
		fieldType.freeze();
	}


	public SpansInfo getSpansInfo(JasperPrint jasperPrint, String queryString) throws IOException, JRException {
		Long start = System.currentTimeMillis();

		Directory dir = createLuceneDirectory(jasperPrint);

		if (log.isDebugEnabled()) {
			log.debug("original query: [" + queryString + "]");
		}

		IndexReader reader = DirectoryReader.open(dir);
		IndexSearcher searcher = new IndexSearcher(reader);
		List<String> queryTerms = getQueryTerms(queryString);
		int queryTermsSize = queryTerms.size();
		SpanQuery query = buildQuery(queryTerms);

		if (log.isDebugEnabled()) {
			log.debug("lucene query: [" + query.toString() + "]");
		}

		TopDocs results = searcher.search(query, Integer.MAX_VALUE);
		ScoreDoc[] hits = results.scoreDocs;

		if (log.isDebugEnabled()) {
			log.debug("The query produced hits in two documents");
		}

		Map<Integer, Set<Term>> hitTermsMap = new LinkedHashMap<>();
		for (ScoreDoc hit: hits) {
			Set<Term> hitTerms = new TreeSet<>();
			getHitTerms(query, searcher, hit.doc, hitTerms);
			hitTermsMap.put(hit.doc, hitTerms);
		}

		SpansInfo spansInfo = new LuceneSpansInfo(queryTerms.size(), queryTerms);

		// get the info for each matched term from the document's termVector
		for (Map.Entry<Integer, Set<Term>> entry: hitTermsMap.entrySet()) {
			int docId = entry.getKey();
			Terms termVector = reader.getTermVector(docId, CONTENT_FIELD);
			Document doc = searcher.doc(docId);
			String uid = doc.get("uid");
			String pageNo = doc.get("pageNo");

			// construct the hit terms info list
			List<HitTermInfo> hitTermInfoList = new ArrayList<>();
			Set<Term> terms = entry.getValue();
			for (Term term: terms) {
				TermsEnum iterator = termVector.iterator();
				BytesRef termBytesRef = new BytesRef(term.text());

				if (iterator.seekExact(termBytesRef)) {
					PostingsEnum docsAndPositions = iterator.postings(null, PostingsEnum.ALL);
					docsAndPositions.nextDoc();

					for (int i = 0, freq = docsAndPositions.freq(); i < freq; ++i) {
						HitTermInfo termInfo = new HitTermInfo(docsAndPositions.nextPosition(), docsAndPositions.startOffset(), docsAndPositions.endOffset(), term.text());
						hitTermInfoList.add(termInfo);

						// when the query contains only one term, construct the spansInfo directly
						if (queryTermsSize == 1) {
							HitSpanInfo tsi = new HitSpanInfo(termInfo);
							tsi.setPageNo(pageNo);
							spansInfo.addSpanInfo(uid, tsi);
						}
					}
				}
			}

			// when the query has more than one term, manually check which hit terms actually validate the query
			if (queryTermsSize > 1) {
				validateHitTerms(hitTermInfoList, queryTerms, uid, pageNo, spansInfo);
			}
		}

		reader.close();
		if (log.isDebugEnabled()) {
			log.debug("search took: " + (System.currentTimeMillis() - start) + " ms");
		}

		return spansInfo;
	}

	protected void validateHitTerms(List<HitTermInfo> hitTermInfoList, List<String> queryTerms, String uid,
							String pageNo, SpansInfo spansInfo) {
		// sort hit terms info by position
		Collections.sort(hitTermInfoList);

		int queryTermsSize = queryTerms.size();

		if (log.isDebugEnabled()) {
			log.debug("Validating hitTermInfoList: " + hitTermInfoList + " against: " + queryTerms);
		}

		for (int i = 0, sz = hitTermInfoList.size(); i < (sz - queryTermsSize + 1); i++) {
			boolean isValidSpan = true;
			List<HitTermInfo> validSpan = new ArrayList<>();
			for (int j = 0; j < queryTermsSize; j++) {
				HitTermInfo htiIJ = hitTermInfoList.get(i + j);
				String queryTermJ = queryTerms.get(j);
				if (log.isDebugEnabled()) {
					log.debug("Comparing: " + htiIJ + " with " + queryTermJ);
				}
				if (j == 0) {
					if (!htiIJ.getValue().endsWith(queryTermJ)) {
						if (log.isDebugEnabled()) {
							log.debug(htiIJ + " does not end with " + queryTermJ);
						}
						isValidSpan = false;
						break;
					}
				} else if (j < queryTermsSize - 1) {
					if (!htiIJ.getValue().equals(queryTermJ)) {
						if (log.isDebugEnabled()) {
							log.debug(htiIJ + " does not equal " + queryTermJ);
						}
						isValidSpan = false;
						break;
					}
				} else {
					if (!htiIJ.getValue().startsWith(queryTermJ)) {
						if (log.isDebugEnabled()) {
							log.debug(htiIJ + " does not start with " + queryTermJ);
						}
						isValidSpan = false;
						break;
					}
				}
				validSpan.add(htiIJ);
			}

			if (isValidSpan) {
				if (log.isDebugEnabled()) {
					log.debug("Found valid span: " + validSpan);
				}

				HitSpanInfo tsi = new HitSpanInfo(validSpan);
				tsi.setPageNo(pageNo);
				spansInfo.addSpanInfo(uid, tsi);
			}
		}
	}


	protected Directory createLuceneDirectory(JasperPrint jasperPrint) throws IOException, JRException {
		Long start = System.currentTimeMillis();
		Directory dir = new RAMDirectory();
		Analyzer analyzer = getConfiguredAnalyzer();
		IndexWriterConfig iwc = new IndexWriterConfig(analyzer);

		iwc.setOpenMode(OpenMode.CREATE);
		writer = new IndexWriter(dir, iwc);

		List<JRPrintPage> pages = jasperPrint.getPages();
		if (pages != null && pages.size() > 0) {
			if (log.isDebugEnabled()) {
				log.debug("there are " + pages.size() + " pages to be indexed");
			}
			for (int i = 0, ps = pages.size(); i < ps; i++) {
				if (log.isDebugEnabled()) {
					log.debug("indexing page: " + i);
				}
				indexPage(pages.get(i), i);
			}
		}

		writer.close();

		if (log.isDebugEnabled()) {
			log.debug("index creation took: " + (System.currentTimeMillis() - start) + " ms");
		}

		return dir;
	}


	protected void indexPage(JRPrintPage page, int pageNo) throws IOException {
		List<JRPrintElement> elements = page.getElements();
		if (page.getElements().size() > 0) {
			indexElements(pageNo, elements);
		}
	}


	protected void indexElements(int pageNo, List<JRPrintElement> elements) throws IOException {
		for (int i = 0; i < elements.size(); i++) {
			Object element = elements.get(i);
			if (element instanceof JRPrintText) {
				addContentField(pageNo, (JRPrintText) element);
			} else if (element instanceof JRPrintFrame) {
				JRPrintFrame frame = (JRPrintFrame) element;
				indexElements(pageNo, frame.getElements());
			}
		}
	}


	protected void addContentField(int pageNo, JRPrintText element) throws IOException {
		String allText;
		JRStyledText styledText = getStyledText(element);
		if (styledText == null) {
			allText = "";
		} else {
			allText = styledText.getText();
		}

		if (allText != null && allText.length() > 0) {
			Field tf = new Field(CONTENT_FIELD, allText, fieldType);
			Document doc = new Document();
			doc.add(new StoredField("pageNo", pageNo));

			PrintElementId peid = PrintElementId.forElement(element);
			doc.add(new StringField("uid", peid.toString(), Field.Store.YES));

			if (log.isDebugEnabled()) {
				log.debug(displayTokens(allText, peid.toString()));
			}

			doc.add(tf);
			writer.addDocument(doc);
		}

	}


	protected Analyzer getConfiguredAnalyzer() {
		if (analyzer == null) {
			analyzer = new LuceneSimpleAnalyzer(isCaseSensitive, removeAccents);
		}
		return analyzer;
	}


	protected JRStyledText getStyledText(JRPrintText textElement)
	{
		return styledTextUtil.getStyledText(textElement, noneSelector);
	}


	protected SpanQuery buildQuery(List<String> queryTerms) {
		List<SpanQuery> clauses = new ArrayList<>();
		for (int i = 0, ln = queryTerms.size(); i < ln; i++) {
			String term = queryTerms.get(i);
			if (isWholeWordsOnly) {
				clauses.add(new SpanTermQuery(new Term(CONTENT_FIELD, term)));
			} else {
				if (i == 0) {
					term = "*" + term;
				}
				if (i == ln - 1) {
					term = term + "*";
				}
				clauses.add(new SpanMultiTermQueryWrapper<>(new WildcardQuery(new Term(CONTENT_FIELD, term))));
			}
		}

		if (clauses.size() > 1) {
			// create a spanQuery with no distance between terms; the terms' order matters
			// SpanNearQuery requires at least 2 clauses
			return new SpanNearQuery(clauses.toArray(new SpanQuery[]{}), 0, true);
		} else if (clauses.size() == 1) {
			return clauses.get(0);
		}

		return null;
	}


	protected List<String> getQueryTerms(String queryString) throws IOException {
		List<String> queryTerms = new ArrayList<>();
		Analyzer analyzer = getConfiguredAnalyzer();
		TokenStream tokenStream = analyzer.tokenStream(null, queryString);
		CharTermAttribute charTermAttribute = tokenStream.addAttribute(CharTermAttribute.class);

		tokenStream.reset();
		while (tokenStream.incrementToken()) {
			queryTerms.add(charTermAttribute.toString());
		}

		return queryTerms;
	}


	protected String displayTokens(String text, String elementId) throws IOException {
		Analyzer analyzer = new LuceneSimpleAnalyzer(isCaseSensitive, removeAccents);
		StringBuilder sb = new StringBuilder();
		sb.append(elementId).append(": ").append(text).append(": ");

		TokenStream tokenStream = analyzer.tokenStream(null, new StringReader(text));
		CharTermAttribute charTermAttribute = tokenStream.addAttribute(CharTermAttribute.class);
		OffsetAttribute offsetAttribute = tokenStream.addAttribute(OffsetAttribute.class);

		tokenStream.reset();
		while (tokenStream.incrementToken()) {
			int startOffset = offsetAttribute.startOffset();
			int endOffset = offsetAttribute.endOffset();
			String term = charTermAttribute.toString();
			sb.append("[" + term + "](" + startOffset + "," + endOffset + ") ");
		}

		return sb.toString();
	}
	

	@SuppressWarnings("rawtypes")
	protected void getHitTerms(Query query, IndexSearcher searcher, int docId, Set<Term> hitTerms) throws IOException {
		if (query instanceof SpanTermQuery) {
			if (searcher.explain(query, docId).isMatch() == true) {
				hitTerms.add(((SpanTermQuery) query).getTerm());
			}
			return; 
		}

		if (query instanceof MultiTermQuery) {
			if (!(query instanceof FuzzyQuery)) { // FuzzQuery doesn't support SetRewriteMethod
				((MultiTermQuery) query).setRewriteMethod(MultiTermQuery.SCORING_BOOLEAN_REWRITE);
			}
			getHitTerms(query.rewrite(searcher.getIndexReader()), searcher, docId, hitTerms);
			return;
		}
		
		if (query instanceof SpanNearQuery) {
			for (SpanQuery bc : ((SpanNearQuery) query).getClauses()) {
				getHitTerms(bc, searcher, docId, hitTerms);
			}
			return;
		}

		if (query instanceof SpanOrQuery) {
			for (SpanQuery bc : ((SpanOrQuery) query).getClauses()) {
				getHitTerms(bc, searcher, docId, hitTerms);
			}
			return;
		}

		if (query instanceof SpanMultiTermQueryWrapper) {
			((SpanMultiTermQueryWrapper) query).setRewriteMethod(SpanMultiTermQueryWrapper.SCORING_SPAN_QUERY_REWRITE);
			getHitTerms(query.rewrite(searcher.getIndexReader()), searcher, docId, hitTerms);
		}
	}
	
}
