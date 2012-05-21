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
package net.sf.jasperreports.engine.xml;

import java.io.IOException;

import net.sf.jasperreports.engine.JRConditionalStyle;
import net.sf.jasperreports.engine.JRLineBox;
import net.sf.jasperreports.engine.JRParagraph;
import net.sf.jasperreports.engine.JRPen;
import net.sf.jasperreports.engine.JRStyle;
import net.sf.jasperreports.engine.JRStyleContainer;
import net.sf.jasperreports.engine.TabStop;
import net.sf.jasperreports.engine.util.JRXmlWriteHelper;
import net.sf.jasperreports.engine.util.XmlNamespace;


/**
 * Base XML writer.
 * 
 * @author Lucian Chirita (lucianc@users.sourceforge.net)
 * @version $Id$
 */
public abstract class JRXmlBaseWriter
{
	
	protected JRXmlWriteHelper writer;
	
	/**
	 * Sets the XML write helper.
	 * 
	 * @param aWriter the XML write helper
	 */
	protected void useWriter(JRXmlWriteHelper aWriter)
	{
		this.writer = aWriter;
	}
	
	/**
	 * Writes a style.
	 * 
	 * @param style the style to write.
	 * @throws IOException
	 */
	protected void writeStyle(JRStyle style) throws IOException
	{
		writer.startElement(JRXmlConstants.ELEMENT_style);
		writer.addEncodedAttribute(JRXmlConstants.ATTRIBUTE_name, style.getName());
		writer.addAttribute(JRXmlConstants.ATTRIBUTE_isDefault, style.isDefault(), false);
		writeStyleReferenceAttr(style);
		writer.addAttribute(JRXmlConstants.ATTRIBUTE_mode, style.getOwnModeValue());
		writer.addAttribute(JRXmlConstants.ATTRIBUTE_forecolor, style.getOwnForecolor());
		writer.addAttribute(JRXmlConstants.ATTRIBUTE_backcolor, style.getOwnBackcolor());
		writer.addAttribute(JRXmlConstants.ATTRIBUTE_fill, style.getOwnFillValue());
		writer.addAttribute(JRXmlConstants.ATTRIBUTE_radius, style.getOwnRadius());
		writer.addAttribute(JRXmlConstants.ATTRIBUTE_scaleImage, style.getOwnScaleImageValue());
		writer.addAttribute(JRXmlConstants.ATTRIBUTE_hAlign, style.getOwnHorizontalAlignmentValue());
		writer.addAttribute(JRXmlConstants.ATTRIBUTE_vAlign, style.getOwnVerticalAlignmentValue());
		writer.addAttribute(JRXmlConstants.ATTRIBUTE_rotation, style.getOwnRotationValue());
		writer.addAttribute(JRXmlConstants.ATTRIBUTE_markup, style.getOwnMarkup());
		writer.addEncodedAttribute(JRXmlConstants.ATTRIBUTE_pattern, style.getOwnPattern());
		writer.addAttribute(JRXmlConstants.ATTRIBUTE_isBlankWhenNull, style.isOwnBlankWhenNull());
		
		writer.addEncodedAttribute(JRXmlConstants.ATTRIBUTE_fontName, style.getOwnFontName());
		writer.addAttribute(JRXmlConstants.ATTRIBUTE_fontSize, style.getOwnFontSize());
		writer.addAttribute(JRXmlConstants.ATTRIBUTE_isBold, style.isOwnBold());
		writer.addAttribute(JRXmlConstants.ATTRIBUTE_isItalic, style.isOwnItalic());
		writer.addAttribute(JRXmlConstants.ATTRIBUTE_isUnderline, style.isOwnUnderline());
		writer.addAttribute(JRXmlConstants.ATTRIBUTE_isStrikeThrough, style.isOwnStrikeThrough());
		writer.addEncodedAttribute(JRXmlConstants.ATTRIBUTE_pdfFontName, style.getOwnPdfFontName());
		writer.addEncodedAttribute(JRXmlConstants.ATTRIBUTE_pdfEncoding, style.getOwnPdfEncoding());
		writer.addAttribute(JRXmlConstants.ATTRIBUTE_isPdfEmbedded, style.isOwnPdfEmbedded());

		writePen(style.getLinePen());
		writeBox(style.getLineBox());
		writeParagraph(style.getParagraph());
		
		if (toWriteConditionalStyles())
		{
			JRConditionalStyle[] conditionalStyles = style.getConditionalStyles();
			if (!(style instanceof JRConditionalStyle) && conditionalStyles != null)
			{
				for (int i = 0; i < conditionalStyles.length; i++)
				{
					writeConditionalStyle(conditionalStyles[i]);
				}
			}
		}

		writer.closeElement();
	}

	public void writeStyleReferenceAttr(JRStyleContainer styleContainer)
	{
		if (!(styleContainer instanceof JRConditionalStyle))
		{
			if (styleContainer.getStyle() != null)
			{
				writer.addEncodedAttribute(JRXmlConstants.ATTRIBUTE_style, styleContainer.getStyle().getName());
			}
			else if (styleContainer.getStyleNameReference() != null)
			{
				writer.addEncodedAttribute(JRXmlConstants.ATTRIBUTE_style, styleContainer.getStyleNameReference());
			}
		}
	}

	/**
	 * Decides whether conditional styles are to be written.
	 * 
	 * @return whether conditional styles are to be written
	 */
	protected abstract boolean toWriteConditionalStyles();

	/**
	 * Writes a conditional style.
	 * 
	 * @param style the conditional style
	 * @throws IOException
	 */
	protected void writeConditionalStyle(JRConditionalStyle style) throws IOException
	{
		writer.startElement(JRXmlConstants.ELEMENT_conditionalStyle);
		writer.writeExpression(JRXmlConstants.ELEMENT_conditionExpression, style.getConditionExpression());
		writeStyle(style);
		writer.closeElement();
	}

	/**
	 *
	 */
	protected void writePen(JRPen pen) throws IOException
	{
		writePen(JRXmlConstants.ELEMENT_pen, pen);
	}

	/**
	 *
	 */
	private void writePen(String element, JRPen pen) throws IOException
	{
		writer.startElement(element);
		writer.addAttribute(JRXmlConstants.ATTRIBUTE_lineWidth, pen.getOwnLineWidth());
		writer.addAttribute(JRXmlConstants.ATTRIBUTE_lineStyle, pen.getOwnLineStyleValue());
		writer.addAttribute(JRXmlConstants.ATTRIBUTE_lineColor, pen.getOwnLineColor());
		writer.closeElement(true);
	}

	public void writeBox(JRLineBox box) throws IOException
	{
		writeBox(box, null);
	}
	
	/**
	 *
	 */
	public void writeBox(JRLineBox box, XmlNamespace namespace) throws IOException
	{
		if (box != null)
		{
			writer.startElement(JRXmlConstants.ELEMENT_box, namespace);
			
			writer.addAttribute(JRXmlConstants.ATTRIBUTE_padding, box.getOwnPadding());
			writer.addAttribute(JRXmlConstants.ATTRIBUTE_topPadding, box.getOwnTopPadding());
			writer.addAttribute(JRXmlConstants.ATTRIBUTE_leftPadding, box.getOwnLeftPadding());
			writer.addAttribute(JRXmlConstants.ATTRIBUTE_bottomPadding, box.getOwnBottomPadding());
			writer.addAttribute(JRXmlConstants.ATTRIBUTE_rightPadding, box.getOwnRightPadding());

			writePen(JRXmlConstants.ELEMENT_pen, box.getPen());
			writePen(JRXmlConstants.ELEMENT_topPen, box.getTopPen());
			writePen(JRXmlConstants.ELEMENT_leftPen, box.getLeftPen());
			writePen(JRXmlConstants.ELEMENT_bottomPen, box.getBottomPen());
			writePen(JRXmlConstants.ELEMENT_rightPen, box.getRightPen());

			writer.closeElement(true);
		}
	}

	public void writeParagraph(JRParagraph paragraph) throws IOException
	{
		writeParagraph(paragraph, null);
	}
	
	/**
	 *
	 */
	public void writeParagraph(JRParagraph paragraph, XmlNamespace namespace) throws IOException
	{
		if (paragraph != null)
		{
			writer.startElement(JRXmlConstants.ELEMENT_paragraph, namespace);
			
			writer.addAttribute(JRXmlConstants.ATTRIBUTE_lineSpacing, paragraph.getOwnLineSpacing());
			writer.addAttribute(JRXmlConstants.ATTRIBUTE_lineSpacingSize, paragraph.getOwnLineSpacingSize());
			writer.addAttribute(JRXmlConstants.ATTRIBUTE_firstLineIndent, paragraph.getOwnFirstLineIndent());
			writer.addAttribute(JRXmlConstants.ATTRIBUTE_leftIndent, paragraph.getOwnLeftIndent());
			writer.addAttribute(JRXmlConstants.ATTRIBUTE_rightIndent, paragraph.getOwnRightIndent());
			writer.addAttribute(JRXmlConstants.ATTRIBUTE_spacingBefore, paragraph.getOwnSpacingBefore());
			writer.addAttribute(JRXmlConstants.ATTRIBUTE_spacingAfter, paragraph.getOwnSpacingAfter());
			writer.addAttribute(JRXmlConstants.ATTRIBUTE_tabStopWidth, paragraph.getOwnTabStopWidth());

			/*   */
			TabStop[] tabStops = paragraph.getTabStops();
			if (tabStops != null && tabStops.length > 0)
			{
				for(int i = 0; i < tabStops.length; i++)
				{
					writeTabStop(tabStops[i]);
				}
			}

			writer.closeElement(true);
		}
	}

	
	/**
	 *
	 */
	public void writeTabStop(TabStop tabStop) throws IOException
	{
		if (tabStop != null)
		{
			writer.startElement(JRXmlConstants.ELEMENT_tabStop);
			
			writer.addAttribute(JRXmlConstants.ATTRIBUTE_position, tabStop.getPosition());
			writer.addAttribute(JRXmlConstants.ATTRIBUTE_alignment, tabStop.getAlignment());

			writer.closeElement(true);
		}
	}

}
