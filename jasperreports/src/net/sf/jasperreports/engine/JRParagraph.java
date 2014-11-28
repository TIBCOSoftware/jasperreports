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
package net.sf.jasperreports.engine;

import net.sf.jasperreports.engine.type.LineSpacingEnum;



/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 */
public interface JRParagraph extends JRStyleContainer
{
	/**
	 * Specifies the default value for the text line spacing size, if no explicit information is provided in a 
	 * JR paragraph. This value is used in combination with the size-dependent line spacings in {@link LineSpacingEnum}: 
	 * <ul>
	 * <li>AT_LEAST</li>
	 * <li>FIXED</li>
	 * <li>PROPORTIONAL</li>
	 * </ul>
	 * 
	 * @see net.sf.jasperreports.engine.export.AbstractTextRenderer#getLineHeight(boolean, JRParagraph, float, float)
	 */
	public static final String DEFAULT_LINE_SPACING_SIZE = JRPropertiesUtil.PROPERTY_PREFIX + "default.line.spacing.size";
	
	/**
	 * Specifies the default value (in pixels) for the first line indent, if no explicit information is provided in a JR paragraph. 
	 * <br/>
	 * By default the first line in JR paragraphs has no indentation. 
	 */
	public static final String DEFAULT_FIRST_LINE_INDENT = JRPropertiesUtil.PROPERTY_PREFIX + "default.first.line.indent";
	
	/**
	 * Specifies the default value (in pixels) for the left indent, if no explicit information is provided in a JR paragraph. 
	 * <br/>
	 * By default JR paragraphs provide no left indentation. 
	 */
	public static final String DEFAULT_LEFT_INDENT = JRPropertiesUtil.PROPERTY_PREFIX + "default.left.indent";
	
	/**
	 * Specifies the default value (in pixels) for the right indent, if no explicit information is provided in a JR paragraph. 
	 * <br/>
	 * By default JR paragraphs provide no right indentation. 
	 */
	public static final String DEFAULT_RIGHT_INDENT = JRPropertiesUtil.PROPERTY_PREFIX + "default.right.indent";
	
	/**
	 * Specifies the default value (in pixels) for the spacing before lines, if no explicit information is provided in a JR paragraph. 
	 * <br/>
	 * By default JR paragraphs provide no spacing before lines. 
	 */
	public static final String DEFAULT_SPACING_BEFORE = JRPropertiesUtil.PROPERTY_PREFIX + "default.spacing.before";
	
	/**
	 * Specifies the default value (in pixels) for the spacing after lines, if no explicit information is provided in a JR paragraph. 
	 * <br/>
	 * By default JR paragraphs provide no spacing after lines. 
	 */
	public static final String DEFAULT_SPACING_AFTER = JRPropertiesUtil.PROPERTY_PREFIX + "default.spacing.after";
	
	/**
	 * Specifies the default value (in pixels) for the tab stop width, if no explicit information is provided in a paragraph. 
	 * <br/>
	 * By default JR paragraphs provide 40 pixels wide tab stops. 
	 */
	public static final String DEFAULT_TAB_STOP_WIDTH = JRPropertiesUtil.PROPERTY_PREFIX + "default.tab.stop.width";

	/**
	 * 
	 */
	public JRParagraph clone(JRParagraphContainer paragraphContainer);

	/**
	 * Gets the text line spacing.
	 * @return a value representing one of the line spacing constants in {@link LineSpacingEnum}
	 */
	public LineSpacingEnum getLineSpacing();
	
	/**
	 * Gets the text own line spacing.
	 * @return a value representing one of the line spacing constants in {@link LineSpacingEnum}
	 */
	public LineSpacingEnum getOwnLineSpacing();
	
	/**
	 * Sets the text line spacing.
	 * @param lineSpacing a value representing one of the line spacing constants in {@link LineSpacingEnum}
	 */
	public void setLineSpacing(LineSpacingEnum lineSpacing);
	
	/**
	 * Gets the text line spacing size to be used in combination with the line spacing type.
	 */
	public Float getLineSpacingSize();
	
	/**
	 * Gets the text own line spacing size to be used in combination with the line spacing type.
	 */
	public Float getOwnLineSpacingSize();
	
	/**
	 * Sets the text line spacing size to be used in combination with the line spacing type.
	 */
	public void setLineSpacingSize(Float lineSpacingSize);
	
	/**
	 * Gets the text left indent.
	 */
	public Integer getLeftIndent();
	
	/**
	 * Gets the text own left indent.
	 */
	public Integer getOwnLeftIndent();
	
	/**
	 * Sets the text own left indent.
	 */
	public void setLeftIndent(Integer leftIndent);
	
	/**
	 * Gets the text first line indent.
	 */
	public Integer getFirstLineIndent();
	
	/**
	 * Gets the text own first line indent.
	 */
	public Integer getOwnFirstLineIndent();
	
	/**
	 * Sets the text own first line indent.
	 */
	public void setFirstLineIndent(Integer firstLineIndent);
	
	/**
	 * Gets the text right indent.
	 */
	public Integer getRightIndent();
	
	/**
	 * Gets the text own right indent.
	 */
	public Integer getOwnRightIndent();
	
	/**
	 * Sets the text own right indent.
	 */
	public void setRightIndent(Integer rightIndent);

	/**
	 * Gets the text spacing before.
	 */
	public Integer getSpacingBefore();
	
	/**
	 * Gets the text own spacing before.
	 */
	public Integer getOwnSpacingBefore();
	
	/**
	 * Sets the text own spacing before.
	 */
	public void setSpacingBefore(Integer spacingBefore);
	
	/**
	 * Gets the text spacing after.
	 */
	public Integer getSpacingAfter();
	
	/**
	 * Gets the text own spacing after.
	 */
	public Integer getOwnSpacingAfter();
	
	/**
	 * Sets the text own spacing after.
	 */
	public void setSpacingAfter(Integer spacingAfter);
	
	/**
	 * Gets the text tab stop width.
	 */
	public Integer getTabStopWidth();
	
	/**
	 * Gets the text own tab stop width.
	 */
	public Integer getOwnTabStopWidth();
	
	/**
	 * Sets the text own tab stop width.
	 */
	public void setTabStopWidth(Integer tabStopWidth);

	/**
	 * Gets the custom tab stops.
	 */
	public TabStop[] getTabStops();

	/**
	 * Gets the custom tab stops.
	 */
	public TabStop[] getOwnTabStops();
	
	/**
	 *
	 */
	public void addTabStop(TabStop tabStop);
	
	/**
	 *
	 */
	public void addTabStop(int index, TabStop tabStop);

	/**
	 *
	 */
	public void removeTabStop(int index);

	/**
	 *
	 */
	public void removeTabStop(TabStop tabStop);

}
