/*
 * JasperReports - Free Java Reporting Library.
 * Copyright (C) 2001 - 2011 Jaspersoft Corporation. All rights reserved.
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
package net.sf.jasperreports.engine;

import net.sf.jasperreports.engine.type.EvaluationTimeEnum;

//import java.text.Format;


/**
 * An abstract representation of a report static text. It provides functionality for static texts.
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id$
 */
public interface JRTextField extends JRTextElement, JRAnchor, JRHyperlink
{

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
	public EvaluationTimeEnum getEvaluationTimeValue();
		
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
	 * @see EvaluationTimeEnum#GROUP
	 */
	public JRGroup getEvaluationGroup();
		
	/**
	 * Gets the expression for this field. The result obtained after evaluating this expression will be dispayed as
	 * the field text.
	 */
	public JRExpression getExpression();
		
	/**
	 * Gets the pattern expression, in case the patter needs to be dynamic.
	 * @see #getPattern()
	 */
	public JRExpression getPatternExpression();

}
