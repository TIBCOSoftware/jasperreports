/*
 * JasperReports - Free Java Reporting Library.
 * Copyright (C) 2001 - 2013 Jaspersoft Corporation. All rights reserved.
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
package net.sf.jasperreports.functions.standard;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.sf.jasperreports.engine.JRRuntimeException;
import net.sf.jasperreports.functions.annotations.Function;
import net.sf.jasperreports.functions.annotations.FunctionCategories;
import net.sf.jasperreports.functions.annotations.FunctionParameter;
import net.sf.jasperreports.functions.annotations.FunctionParameters;

/**
 * This class should maintain all function methods that belongs to the category {@link #TEXT}.
 * 
 * @author Massimo Rabbi (mrabbi@users.sourceforge.net)
 * @version $Id: CastorUtil.java 5880 2013-01-07 20:40:06Z teodord $
 */
public final class TextFunctions 
{
	/**
	 * Category for text/string manipulation functions 
	 */
	public static final String TEXT = "TEXT";
	
	private static final int BASE_MIN_RADIX=2;
	private static final int BASE_MAX_RADIX=36;

	// ===================== BASE function ===================== //
	@Function(name="BASE",description="Returns a text representation of a number, in a specified base radix.")
	@FunctionCategories({TEXT})
	@FunctionParameters({
		@FunctionParameter(name="Number",description="The positive integer number to convert"),
		@FunctionParameter(name="Radix",description="The base radix, an integer between 2 and 36"),
		@FunctionParameter(name="Minimum length",description="Specifies the minimum number of characters returned; zeroes are added on the left if necessary.")})
	public static String BASE(Integer number, Integer radix){
		// java.lang.Character.MIN_RADIX and java.lang.Character.MAX_RADIX are already 2 and 36 respectively
		// However we should check the parameter specified because the method we rely on uses 10 radix
		// as fallback when a smaller/greater radix is specified.
		if(number==null || radix==null || radix>BASE_MAX_RADIX || radix<BASE_MIN_RADIX){
			// The radix parameter must be an integer number between 2 and 36
			return null;
		}
		return Integer.toString(number, radix);
	}

	public static String BASE(Integer number, Integer radix, Integer minlength){
		if (number==null || radix==null || minlength==null) return null;
		String spacePaddedStr = String.format("%"+minlength+"s",BASE(number, radix));
		return spacePaddedStr.replace(' ', '0');
	}

	// ===================== CHAR function ===================== //
	@Function(name="CHAR",description="Returns a single text character, given a character code.")
	@FunctionCategories({TEXT})
	@FunctionParameters({
			@FunctionParameter(name="Char code",description="The character code, in the range 1-255.")})
	public static String CHAR(Integer number){
		if(number==null || (number <1 || number>255)){
			// The number must be an integer number between 1 and 255
			return null;
		}
		return Character.toString((char)number.intValue());
	}
	
	// ===================== CLEAN function ===================== //
	@Function(name="CLEAN",description="Returns a new text string without non-printable characters.")
	@FunctionCategories({TEXT})
	@FunctionParameters({
			@FunctionParameter(name="Text",description="The text to be cleaned.")})
	public static String CLEAN(String text){
		if(text==null){
			return null;
		}
		String cleanedString = text.replaceAll("\\p{Cntrl}", "");
		return cleanedString;
	}

	// ===================== CODE function ===================== //
	@Function(name="CODE",description="Returns the numeric code (0-255) for the first character in a string.")
	@FunctionCategories({TEXT})
	@FunctionParameters({
			@FunctionParameter(name="Text",description="The string containing the character to convert.")})
	public static Integer CODE(String textString){
		if(textString==null) return null;
		int firstCharAsNum = textString.charAt(0);
		if(firstCharAsNum<0 || firstCharAsNum>255){
			throw new JRRuntimeException("The first character of the text can not be converted to a valid numeric ASCII code.");
		}
		return firstCharAsNum;
	}
	
	// ===================== CONCATENATE function ===================== //
	@Function(name="CONCATENATE",description="Combines a list of strings into a single one.")
	@FunctionCategories({TEXT})
	@FunctionParameters({
			@FunctionParameter(name="Text",description="The string containing the character to convert.")})
	public static String CONCATENATE(String ...strings){
		if(strings.length==0) return null;
		StringBuffer sb=new StringBuffer();
		for (int i=0;i<strings.length;i++){
			sb.append(strings[i]);
		}
		return sb.toString();
	}
	
	// ===================== CONCATENATE function ===================== //
	@Function(name="EXACT",description="Returns TRUE if the two text specified are exactly the same (case sensitive compare).")
	@FunctionCategories({TEXT})
	@FunctionParameters({
			@FunctionParameter(name="Text 1",description="The first text to compare."),
			@FunctionParameter(name="Text 2",description="The second text to compare.")})
	public static Boolean EXACT(String text1, String text2){
		if(text1!=null){
			return text1.equals(text2);
		}
		else if(text2!=null){
			return text2.equals(text1);
		}
		else {
			return null;
		}
	}

	// ===================== DOUBLE_VALUE function ===================== //
	@Function(name="DOUBLE_VALUE", description="Returns a Double number representing the given text string.")
	@FunctionCategories({TEXT})
	@FunctionParameters({
			@FunctionParameter(name="Number (as text)",description="The input text string representing a number.")})
	public static Double DOUBLE_VALUE(String textNumber){
		if(textNumber==null) return null;
		return Double.parseDouble(textNumber);
	}	
	
	// ===================== FIND function ===================== //
	@Function(name="FIND",description="Returns the character position of a string inside another text. If the text is not found then -1 is returned.")
	@FunctionCategories({TEXT})
	@FunctionParameters({
			@FunctionParameter(name="Find text",description="The text to look into."),
			@FunctionParameter(name="Text to search",description="The text string to search."),
			@FunctionParameter(name="Start position",description="The position from which the search should start.")})
	public static Integer FIND(String findText, String searchText){
		return FIND(findText, searchText, 0);
	}
	
	public static Integer FIND(String findText, String searchText, Integer startPosition){
		if(findText==null || searchText==null) return null;
		int position = findText.indexOf(searchText, startPosition);
		return position;
	}
	
	// ===================== FIXED function ===================== //
	@Function(name="FIXED", description="Returns the text representing number with the specified decimal places.")
	@FunctionCategories({TEXT})
	@FunctionParameters({
			@FunctionParameter(name="Number",description="The number to print out."),
			@FunctionParameter(name="Decimals",description="The number of decimal places."),
			@FunctionParameter(name="Omit separators",description="The flag to specify if the thousands separators shoud be included or not.")})
	public static String FIXED(Number number, Integer decimals){
		return FIXED(number, decimals,false);
	}

	public static String FIXED(Number number, Integer decimals, Boolean omitSeparators){
		if(number==null || decimals==null || omitSeparators==null) return null;
		// Pattern samples:
		// 123456.789	###,###.###	123,456.789
		// 123456.789	###.##	123456.79
		// 123.78	000000.000	000123.780
		StringBuffer patternBuf=new StringBuffer("###");
		if(!omitSeparators){
			patternBuf.append(",###");
		}
		patternBuf.append(".");
		for(int i=0;i<decimals;i++){
			patternBuf.append("0");
		}
		DecimalFormat myFormatter = new DecimalFormat(patternBuf.toString());
	    return myFormatter.format(number);
	}
	
	// ===================== FLOAT_VALUE function ===================== //
	@Function(name="FLOAT_VALUE", description="Returns a Float number representing the given text string.")
	@FunctionCategories({TEXT})
	@FunctionParameters({
			@FunctionParameter(name="Number (as text)",description="The input text string representing a number.")})
	public static Float FLOAT_VALUE(String textNumber){
		if(textNumber==null) return null;
		return Float.parseFloat(textNumber);
	}
	
	// ===================== INTEGER_VALUE function ===================== //
	@Function(name="INTEGER_VALUE", description="Returns an Integer number representing the given text string.")
	@FunctionCategories({TEXT})
	@FunctionParameters({
			@FunctionParameter(name="Number (as text)",description="The input text string representing a number.")})
	public static Integer INTEGER_VALUE(String textNumber){
		if(textNumber==null) return null;
		return Integer.parseInt(textNumber);
	}
	
	// ===================== LEFT function ===================== //
	@Function(name="LEFT", description="Returns the specified number of characters (1 by default) from the left side of the input text.")
	@FunctionCategories({TEXT})
	@FunctionParameters({
			@FunctionParameter(name="Text",description="The input text."),
			@FunctionParameter(name="Characters num",description="The number of characters. Default (not specified) is 1.")})
	public static String LEFT(String text){
		return LEFT(text,1);
	}
	
	public static String LEFT(String text, Integer charactersNum){
		if(text==null || charactersNum==null) return null;
		return text.substring(0,charactersNum);
	}
	
	// ===================== LEN function ===================== //
	@Function(name="LEN", description="Returns the length of the specified text string.")
	@FunctionCategories({TEXT})
	@FunctionParameters({
			@FunctionParameter(name="Text",description="The input text string.")})
	public static Integer LEN(String text){
		if(text==null) return null;
		return text.length();
	}

	// ===================== LONG_VALUE function ===================== //
	@Function(name="LONG_VALUE", description="Returns a Long number representing the given text string.")
	@FunctionCategories({TEXT})
	@FunctionParameters({
			@FunctionParameter(name="Number (as text)",description="The input text string representing a number.")})
	public static Long LONG_VALUE(String textNumber){
		if(textNumber==null) return null;
		return Long.parseLong(textNumber);
	}

	// ===================== LOWER function ===================== //
	@Function(name="LOWER", description="Performs the lower case conversion of the specified text string.")
	@FunctionCategories({TEXT})
	@FunctionParameters({
			@FunctionParameter(name="Text",description="The input text string.")})
	public static String LOWER(String text){
		if(text==null) return null;
		return text.toLowerCase();
	}

	// ===================== LTRIM function ===================== //
	@Function(name="LTRIM", description="Clear a string, removing leading whitespaces.")
	@FunctionCategories({TEXT})
	@FunctionParameters({
			@FunctionParameter(name="Text",description="The text string to be trimmed.")})
	public static String LTRIM(String text){
		if(text==null) return null;
		return text.replaceAll("^\\s+", "");
	}
		
	// ===================== MID function ===================== //
	@Function(name="MID", description="Returns the text from the middle of a text string.")
	@FunctionCategories({TEXT})
	@FunctionParameters({
			@FunctionParameter(name="Text",description="The input text."),
			@FunctionParameter(name="Start",description="The initial position to extract the text."),
			@FunctionParameter(name="Characters num",description="The number of characters.")})
	public static String MID(String text, Integer startPosition, Integer charactersNum){
		if(text==null || startPosition==null || charactersNum==null) return null;
		return text.substring(startPosition-1,startPosition-1+charactersNum);
	}
	
	// ===================== PROPER function ===================== //
	@Function(name="PROPER", description="Capitalizes each words of the specified text. The remaining parts of words are in lowercase.")
	@FunctionCategories({TEXT})
	@FunctionParameters({
			@FunctionParameter(name="Text",description="The input text.")})
	public static String PROPER(String text){
		if(text==null) return null;
		String lowerCaseString=LOWER(text);
		StringBuffer result=new StringBuffer();
		boolean capitalizeNext=false;
		
		for (int i=0; i<lowerCaseString.length(); i++){
			char c = lowerCaseString.charAt(i);
			if(!isDelimiter(c)){
				if(capitalizeNext){
					result.append(Character.toTitleCase(c));
				}
				else{
					result.append(c);
				}
				capitalizeNext=false;
			}
			else{
				result.append(c);
				capitalizeNext=true;
			}
		}
		
		return result.toString();
	}
	
	// ===================== REPLACE function ===================== //
	@Function(name="REPLACE", description="Replaces parts of a text string with a different one. " +
			"Starting from a specified position, removes a certain number of characters and then insert the new text.")
	@FunctionCategories({TEXT})
	@FunctionParameters({
			@FunctionParameter(name="Original Text",description="The input text to modify."),
			@FunctionParameter(name="Start position",description="The number of characters. Default (not specified) is 1."),
			@FunctionParameter(name="Characters num",description="The number of characters to remove."),
			@FunctionParameter(name="New Text",description="The text that will replace the old one.")})
	public static String REPLACE(String originalText, Integer startPosition, Integer charsNum, String newText){
		if(originalText==null || startPosition==null || charsNum==null || newText==null) return null;		
		StringBuffer output=new StringBuffer();
		output.append(originalText.substring(0, startPosition-1));
		output.append(newText);
		output.append(originalText.substring(startPosition+charsNum-1, originalText.length()));
		return output.toString();
	}
	
	// ===================== REPT function ===================== //
	@Function(name="REPT", description="Replicates an input text string for a specified number of times.")
	@FunctionCategories({TEXT})
	@FunctionParameters({
			@FunctionParameter(name="Original Text",description="The input text to replicate."),
			@FunctionParameter(name="Number of copies",description="The desiderata number of copies.")})
	public static String REPT(String originalText, Integer numberOfTimes){
		if(originalText==null || numberOfTimes==null) return null;
		StringBuffer output=new StringBuffer();
		for(int i=0;i<numberOfTimes;i++){
			output.append(originalText);
		}
		return output.toString();
	}
	
	// ===================== RIGHT function ===================== //
	@Function(name="RIGHT", description="Returns the specified number of characters (1 by default) from the right side of the input text.")
	@FunctionCategories({TEXT})
	@FunctionParameters({
			@FunctionParameter(name="Text",description="The input text."),
			@FunctionParameter(name="Characters num",description="The number of characters. Default (not specified) is 1.")})
	public static String RIGHT(String text){
		return RIGHT(text,1);
	}
	
	public static String RIGHT(String text, Integer charactersNum){
		if(text==null || charactersNum==null) return null;
		int length = text.length();
		return text.substring(length-charactersNum,length);
	}
	
	// ===================== RTRIM function ===================== //
	@Function(name="RTRIM", description="Clear a string, removing trailing whitespaces.")
	@FunctionCategories({TEXT})
	@FunctionParameters({
			@FunctionParameter(name="Text",description="The text string to be trimmed.")})
	public static String RTRIM(String text){
		if(text==null) return null;
		return text.replaceAll("\\s+$", "");
	}	
	
	// ===================== SEARCH function ===================== //
	@Function(name="SEARCH", description="Returns the position of a string of text in another string. Search is not case-sensitive")
	@FunctionCategories({TEXT})
	@FunctionParameters({
			@FunctionParameter(name="Find Text",description="The text to find."),
			@FunctionParameter(name="Text to search",description="The text to search."),
			@FunctionParameter(name="Start position",description="The initial position.")})
	public static Integer SEARCH(String findText, String textToSearch){
		return SEARCH(findText, textToSearch,1);
	}	
	
	public static Integer SEARCH(String findText, String textToSearch, Integer startPosition){
		if(findText==null || textToSearch==null || startPosition==null) return null;
		return textToSearch.indexOf(findText, startPosition-1);
	}
	
	// ===================== SUBSTITUTE function ===================== //
	@Function(name="SUBSTITUTE", description="Substitutes new text for old text in a text string. " +
			"When no occurrence is specified all occurrences are replaced.")
	@FunctionCategories({TEXT})
	@FunctionParameters({
			@FunctionParameter(name="Original text",description="The text to be modified."),
			@FunctionParameter(name="Old text",description="The old text to be replaced."),
			@FunctionParameter(name="New text",description="The new text that will replace the old one."),
			@FunctionParameter(name="Occurrence",description="The occurrence of 'old text' to be replaced."),})
	public static String SUBSTITUTE(String originalText, String oldText, String newText){
		return SUBSTITUTE(originalText, oldText, newText, null);
	}	
	
	public static String SUBSTITUTE(String originalText, String oldText, String newText, Integer occurrenceNum){
		if(originalText==null || oldText==null || newText==null) return null;
		if(occurrenceNum==null){
			// Replace all occurrences
			return oldText.replaceAll(Pattern.quote(oldText), Matcher.quoteReplacement(newText));
		}
		else{
			int startIdx=0;
			int counter=1;

			// Find the right occurrence
			while (startIdx<originalText.length()-1){
				// Locate the next index position of the occurrence of the old text
				int foundPosition = originalText.indexOf(oldText,startIdx);
				if(counter==occurrenceNum){
					return REPLACE(originalText,foundPosition+1,oldText.length(),newText);
				}
				else{
					startIdx=foundPosition+oldText.length();
					counter++;
				}
			}
			// Fall-back
			return null;					
		}
	}
	
	// ===================== T function ===================== //
	@Function(name="T", description="Returns the text string if the value is a string, otherwise an empty string is returned.")
	@FunctionCategories({TEXT})
	@FunctionParameters({
			@FunctionParameter(name="Generic value",description="The object value to be tested.")})
	public static String T(Object value){
		if(value instanceof String) {
			return (String)value;
		}
		else{
			return "";
		}
	}	
	
	// ===================== TEXT function ===================== //
	@Function(name="TEXT", description="Converts a number into a text string according to a specified format.")
	@FunctionCategories({TEXT})
	@FunctionParameters({
			@FunctionParameter(name="Number",description="The number to be formatted."),
			@FunctionParameter(name="Format",description="The format pattern.")})
	public static String TEXT(Number number, String numberFormat){
		if(number==null || numberFormat==null) return null;
		NumberFormat nformat=new DecimalFormat(numberFormat);
		return nformat.format(number);
	}	
	
	// ===================== TRIM function ===================== //
	@Function(name="TRIM", description="Clear a string,removing leading and trailing whitespaces.")
	@FunctionCategories({TEXT})
	@FunctionParameters({
			@FunctionParameter(name="Text",description="The text string to be trimmed.")})
	public static String TRIM(String text){
		if(text==null) return null;
		return text.trim(); 
	}	
		
	// ===================== UPPER function ===================== //
	@Function(name="UPPER", description="Performs the upper case conversion of the specified text string.")
	@FunctionCategories({TEXT})
	@FunctionParameters({
			@FunctionParameter(name="Text",description="The input text string.")})
	public static String UPPER(String text){
		if(text==null) return null;
		return text.toUpperCase();
	}
	
	// Internal private methods
	private static boolean isDelimiter(char c){
		return Character.isWhitespace(c) || Character.isSpaceChar(c);
	}
}
