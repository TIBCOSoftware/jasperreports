/*
 * ============================================================================
 * GNU Lesser General Public License
 * ============================================================================
 *
 * JasperReports - Free Java report-generating library.
 * Copyright (C) 2001-2005 JasperSoft Corporation http://www.jaspersoft.com
 * 
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307, USA.
 * 
 * JasperSoft Corporation
 * 185, Berry Street, Suite 6200
 * San Francisco CA 94107
 * http://www.jaspersoft.com
 */
package net.sf.jasperreports.engine;

//import java.text.Format;


/**
 * An abstract representation of a report static text. It provides functionality for static texts.
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id$
 */
public interface JRTextField extends JRTextElement, JRAnchor, JRHyperlink
{

	/**
	 * Used in the date pattern to specify the default style.
	 * @see java.text.DateFormat#DEFAULT
	 */
	public static final String STANDARD_DATE_FORMAT_DEFAULT = "default";

	/**
	 * Used in the date pattern to specify the short style.
	 * @see java.text.DateFormat#SHORT
	 */
	public static final String STANDARD_DATE_FORMAT_SHORT = "short";

	/**
	 * Used in the date pattern to specify the medium style.
	 * @see java.text.DateFormat#MEDIUM
	 */
	public static final String STANDARD_DATE_FORMAT_MEDIUM = "medium";

	/**
	 * Used in the date pattern to specify the long style.
	 * @see java.text.DateFormat#LONG
	 */
	public static final String STANDARD_DATE_FORMAT_LONG = "long";

	/**
	 * Used in the date pattern to specify the full style.
	 * @see java.text.DateFormat#FULL
	 */
	public static final String STANDARD_DATE_FORMAT_FULL = "full";

	/**
	 * Used in the date pattern to specify that the date or time should not be included.
	 */
	public static final String STANDARD_DATE_FORMAT_HIDE = "hide";

	/**
	 * Used in the date format pattern to separate the date and time styles.
	 */
	public static final String STANDARD_DATE_FORMAT_SEPARATOR = ",";

	
	/**
	 * Specifies whether the text field will stretch vertically if its text does not fit in one line.
	 * @return true if the text field will stretch vertically, false otherwise
	 */
	public boolean isStretchWithOverflow();

	/**
	 * Set to true if the text field should stretch vertically if its text does not fit in one line.
	 */
	public void setStretchWithOverflow(boolean isStretchWithOverflow);
		
	/**
	 * Gets the evaluation time for this text field.
	 * @return one of the evaluation time constants in {@link JRExpression}
	 */
	public byte getEvaluationTime();
		
	/**
	 * Gets the pattern used for this text field. The pattern will be used in a <tt>SimpleDateFormat</tt> for dates
	 * and a <tt>DecimalFormat</tt> for numeric text fields. The pattern format must follow one of these two classes
	 * formatting rules, as specified in the JDK API docs.
	 * @return a string containing the pattern.
	 */
	public String getPattern();
		
	public String getOwnPattern();

	/**
	 * Sets the pattern used for this text field. The pattern will be used in a <tt>SimpleDateFormat</tt> for dates
	 * and a <tt>DecimalFormat</tt> for numeric text fields. The pattern format must follow one of these two classes
	 * formatting rules, as specified in the JDK API docs. If the pattern is incorrect, the exception thrown by formatter
	 * classes will be rethrown by the JasperReports fill engine.
	 */
	public void setPattern(String pattern);
		
	/**
	 * Indicates whether an empty string will be displayed if the field's expression evaluates to <code>null</code>.
	 * @return true if an empty string will be displayed instead of null values, false otherwise
	 */
	public boolean isBlankWhenNull();

	public Boolean isOwnBlankWhenNull();

	/**
	 * Specifies whether an empty string sholuld be displayed if the field's expression evaluates to <code>null</code>.
	 * @param isBlank true if an empty string will be displayed instead of null values, false otherwise
	 */
	public void setBlankWhenNull(boolean isBlank);

	public void setBlankWhenNull(Boolean isBlank);

	/**
	 * Gets the evaluation group for this text field. Used only when evaluation time is group.
	 * @see JRExpression#EVALUATION_TIME_GROUP
	 */
	public JRGroup getEvaluationGroup();
		
	/**
	 * Gets the expression for this field. The result obtained after evaluating this expression will be dispayed as
	 * the field text.
	 */
	public JRExpression getExpression();
		

}
