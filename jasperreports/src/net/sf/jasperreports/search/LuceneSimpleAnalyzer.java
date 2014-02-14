package net.sf.jasperreports.search;

import java.io.Reader;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.Tokenizer;
import org.apache.lucene.analysis.core.LowerCaseFilter;
import org.apache.lucene.analysis.core.WhitespaceTokenizer;
import org.apache.lucene.analysis.miscellaneous.ASCIIFoldingFilter;
import org.apache.lucene.util.Version;

/**
 * @author Narcis Marcu (narcism@users.sourceforge.net)
 * @version $Id$
 */
public class LuceneSimpleAnalyzer extends Analyzer {

	private Version matchVersion;
    private boolean isCaseSensitive;
    private boolean removeAccents;

	public LuceneSimpleAnalyzer(Version matchVersion, boolean isCaseSensitive, boolean removeAccents) {
		this.matchVersion = matchVersion;
        this.isCaseSensitive = isCaseSensitive;
//        this.removeAccents = removeAccents;
        this.removeAccents = true;
	}

	@Override
	protected TokenStreamComponents createComponents(String fieldName, Reader reader) {
		Tokenizer source = new WhitespaceTokenizer(matchVersion, reader);
        TokenStream result = source;

        if (!isCaseSensitive) {
            // lowercase tokens
            result = new LowerCaseFilter(matchVersion, source);
        }

        if (removeAccents) {
            // normalize characters
            result = new ASCIIFoldingFilter(result);
        }

        // exclude words with length < 3
//		result = new LengthFilter(matchVersion, result, 3, Integer.MAX_VALUE);


		return new TokenStreamComponents(source, result);
	}

}
