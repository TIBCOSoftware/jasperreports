/*
 * JasperReports - Free Java Reporting Library.
 * Copyright (C) 2001 - 2018 TIBCO Software Inc. All rights reserved.
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

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.Tokenizer;
import org.apache.lucene.analysis.core.LowerCaseFilter;
import org.apache.lucene.analysis.core.WhitespaceTokenizer;
import org.apache.lucene.analysis.miscellaneous.ASCIIFoldingFilter;

/**
 * @author Narcis Marcu (narcism@users.sourceforge.net)
 */
public class LuceneSimpleAnalyzer extends Analyzer {

	private boolean isCaseSensitive;
	private boolean removeAccents;

	public LuceneSimpleAnalyzer(boolean isCaseSensitive, boolean removeAccents) {
		this.isCaseSensitive = isCaseSensitive;
//		this.removeAccents = removeAccents;
		this.removeAccents = true;
	}

	@Override
	protected TokenStreamComponents createComponents(String fieldName) {
		Tokenizer source = new WhitespaceTokenizer();
		TokenStream result = source;

		if (!isCaseSensitive) {
			// lowercase tokens
			result = new LowerCaseFilter(source);
		}

		if (removeAccents) {
			// normalize characters
			result = new ASCIIFoldingFilter(result);
		}

		// exclude words with length < 3
//		result = new LengthFilter(result, 3, Integer.MAX_VALUE);


		return new TokenStreamComponents(source, result);
	}

}
