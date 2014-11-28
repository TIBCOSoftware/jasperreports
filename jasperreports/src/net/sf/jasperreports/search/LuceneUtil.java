/*
 * JasperReports - Free Java Reporting Library.
 * Copyright (C) 2001 - 2014 TIBCO Software Inc. All rights reserved.
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
package net.sf.jasperreports.search;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.analysis.tokenattributes.OffsetAttribute;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.FieldType;
import org.apache.lucene.document.IntField;
import org.apache.lucene.document.StringField;
import org.apache.lucene.index.AtomicReaderContext;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.DocsAndPositionsEnum;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.IndexWriterConfig.OpenMode;
import org.apache.lucene.index.Term;
import org.apache.lucene.index.TermContext;
import org.apache.lucene.index.Terms;
import org.apache.lucene.index.TermsEnum;
import org.apache.lucene.queryparser.classic.ParseException;
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
import org.apache.lucene.search.spans.Spans;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.RAMDirectory;
import org.apache.lucene.util.Bits;
import org.apache.lucene.util.BytesRef;
import org.apache.lucene.util.Version;

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
		fieldType.setIndexed(true);
		fieldType.setTokenized(true);
		fieldType.setStored(true);
		fieldType.setStoreTermVectors(true);
		fieldType.setStoreTermVectorPositions(true);
		fieldType.setStoreTermVectorOffsets(true);
		fieldType.freeze();
	}


	public SpansInfo getSpansInfo(JasperPrint jasperPrint, String queryString) throws IOException, ParseException, JRException {
		Long start = System.currentTimeMillis();

		Directory dir = createLuceneDirectory(jasperPrint);

		if (log.isDebugEnabled()) {
			log.debug("original query: [" + queryString + "]");
		}

		IndexReader reader = DirectoryReader.open(dir);
		IndexSearcher searcher = new IndexSearcher(reader);
		List<String> queryTerms = getQueryTerms(queryString);
		SpanNearQuery query = buildQuery(queryTerms);

		if (log.isDebugEnabled()) {
			log.debug("lucene query: [" + query.toString() + "]");
		}

		TopDocs results = searcher.search(query, Integer.MAX_VALUE);
		ScoreDoc[] hits = results.scoreDocs;

		Map<Integer, List<Term>> hitTermsMap = new LinkedHashMap<Integer, List<Term>>();

		for (int i = 0; i < hits.length; i++) {
			getHitTerms(query, searcher, hits[i].doc, hitTermsMap);
		}

		Map<Term,TermContext> termContexts = new HashMap<Term,TermContext>();

		// get the info for each matched term from the document's termVector
		Map<Integer, List<HitTermInfo>> hitTermsInfoMap = new HashMap<Integer, List<HitTermInfo>>();
		for (Entry<Integer, List<Term>> entry: hitTermsMap.entrySet()) {
			List<Term> terms = entry.getValue();
			Terms termVector = reader.getTermVector(entry.getKey(), CONTENT_FIELD);
			DocsAndPositionsEnum docsAndPositions;

			for (Term term: terms) {
				termContexts.put(term, TermContext.build(reader.getContext(), term));
				TermsEnum iterator = termVector.iterator(TermsEnum.EMPTY);

				BytesRef termBytesRef = new BytesRef(term.text());

				if (iterator.seekExact(termBytesRef)) {
					docsAndPositions = iterator.docsAndPositions(null, null);
					docsAndPositions.nextDoc();

					for (int i = 0, freq = docsAndPositions.freq(); i < freq; ++i) {
						if (hitTermsInfoMap.get(entry.getKey()) == null) {
							hitTermsInfoMap.put(entry.getKey(), new ArrayList<HitTermInfo>());
						}
						hitTermsInfoMap.get(entry.getKey()).add(new HitTermInfo(docsAndPositions.nextPosition(), docsAndPositions.startOffset(), docsAndPositions.endOffset(), termBytesRef.utf8ToString()));
					}
				}
			}
		}

		// get the spans for the matched terms
		AtomicReaderContext context = reader.leaves().get(0);
		Bits acceptDocs = context.reader().getLiveDocs();
		SpanQuery rewrittenQuery = (SpanQuery)query.rewrite(reader);
		Spans spans = rewrittenQuery.getSpans(context, acceptDocs, termContexts);
		LuceneSpansInfo spansInfo = new LuceneSpansInfo(queryTerms.size());

		while (spans.next()) {
			Document doc = searcher.doc(spans.doc());
			String uid = doc.get("uid");
			List<HitTermInfo> hitTermsInfo = hitTermsInfoMap.get(spans.doc());

			for (int i = spans.start(); i < spans.end(); i++) {
				for (HitTermInfo ti: hitTermsInfo) {
					if (ti.getPosition() == i) {
						if (log.isDebugEnabled()) {
							log.debug(String.format("term: %s@%d [%d, %d] - uid: %s, pageNo: %s", ti.getValue(), ti.getPosition(), ti.getStart(), ti.getEnd(), uid, doc.get("pageNo")));
						}
						ti.setPageNo(doc.get("pageNo"));
						spansInfo.addTermInfo(uid, ti);
					}
				}
			}
		}

		reader.close();
		if (log.isDebugEnabled()) {
			log.debug("search took: " + (System.currentTimeMillis() - start) + " ms");
		}

		return spansInfo;
	}


	protected Directory createLuceneDirectory(JasperPrint jasperPrint) throws IOException, JRException {
		Long start = System.currentTimeMillis();
		Directory dir = new RAMDirectory();
		Analyzer analyzer = getConfiguredAnalyzer();
		IndexWriterConfig iwc = new IndexWriterConfig(Version.LUCENE_45, analyzer);

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
			doc.add(new IntField("pageNo", pageNo, Field.Store.YES));

			PrintElementId peid = PrintElementId.forElement(element);
			doc.add(new StringField("uid", peid.toString(), Field.Store.YES));

			displayTokens(allText, peid.toString());

			doc.add(tf);
			writer.addDocument(doc);
		}

	}


	protected Analyzer getConfiguredAnalyzer() {
		if (analyzer == null) {
			analyzer = new LuceneSimpleAnalyzer(Version.LUCENE_45, isCaseSensitive, removeAccents);
		}
		return analyzer;
	}


	protected JRStyledText getStyledText(JRPrintText textElement)
	{
		return styledTextUtil.getStyledText(textElement, noneSelector);
	}


	protected SpanNearQuery buildQuery(List<String> queryTerms) {
		SpanNearQuery query = null;
		List<SpanQuery> clauses = new ArrayList<SpanQuery>();
		for (int i = 0, ln = queryTerms.size(); i < ln; i++) {
			String term = queryTerms.get(i);
			if (isWholeWordsOnly) {
				clauses.add(new SpanTermQuery(new Term(CONTENT_FIELD, term)));
			} else {
				if (i == 0) {
					term = "*" + term;
				}
				if (i == ln-1) {
					term = term + "*";
				}
				clauses.add(new SpanMultiTermQueryWrapper<WildcardQuery>(new WildcardQuery(new Term(CONTENT_FIELD, term))));
			}
		}

		if (clauses.size() > 0) {
			// create a spanQuery with no distance between terms; the terms' order matters
			query = new SpanNearQuery(clauses.toArray(new SpanQuery[]{}), 0, true);
		}

		return query;
	}


	protected List<String> getQueryTerms(String queryString) throws IOException {
		List<String> queryTerms = new ArrayList<String>();
		Analyzer analyzer = getConfiguredAnalyzer();
		TokenStream tokenStream = analyzer.tokenStream(null, queryString);
		CharTermAttribute charTermAttribute = tokenStream.addAttribute(CharTermAttribute.class);

		tokenStream.reset();
		while (tokenStream.incrementToken()) {
			queryTerms.add(charTermAttribute.toString());
		}

		return queryTerms;
	}


	protected void displayTokens(String text, String elementId) throws IOException {
		if (log.isDebugEnabled()) {
			Analyzer analyzer = getConfiguredAnalyzer();
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

			log.debug(sb);
		}
	}
	

	@SuppressWarnings("rawtypes")
	protected void getHitTerms(Query query, IndexSearcher searcher, int docId, Map<Integer, List<Term>> hitTerms) throws IOException {
		if (query instanceof SpanTermQuery) {
			if (searcher.explain(query, docId).isMatch() == true) {
				if (!hitTerms.containsKey(docId)) {
					hitTerms.put(docId, new ArrayList<Term>());
				}
				hitTerms.get(docId).add(((SpanTermQuery) query).getTerm());
			}
			return; 
		}

		if (query instanceof MultiTermQuery) {
			if (!(query instanceof FuzzyQuery)) { // FuzzQuery doesn't support SetRewriteMethod
				((MultiTermQuery) query).setRewriteMethod(MultiTermQuery.SCORING_BOOLEAN_QUERY_REWRITE);
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
			return;
		}
	}
	
}
