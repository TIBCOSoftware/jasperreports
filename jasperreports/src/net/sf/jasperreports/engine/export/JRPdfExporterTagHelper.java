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

/*
 * Contributors:
 * Adrian Jackson - iapetus@users.sourceforge.net
 * David Taylor - exodussystems@users.sourceforge.net
 * Lars Kristensen - llk@users.sourceforge.net
 * Ling Li - lonecatz@users.sourceforge.net
 * Martin Clough - mtclough@users.sourceforge.net
 */
package net.sf.jasperreports.engine.export;

import java.util.Stack;

import net.sf.jasperreports.crosstabs.JRCellContents;
import net.sf.jasperreports.engine.JRPrintElement;
import net.sf.jasperreports.engine.JRPrintFrame;
import net.sf.jasperreports.engine.JRPrintImage;

import com.lowagie.text.pdf.PdfArray;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfDictionary;
import com.lowagie.text.pdf.PdfName;
import com.lowagie.text.pdf.PdfNumber;
import com.lowagie.text.pdf.PdfString;
import com.lowagie.text.pdf.PdfStructureElement;
import com.lowagie.text.pdf.PdfStructureTreeRoot;
import com.lowagie.text.pdf.PdfWriter;


/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id$
 */
public class JRPdfExporterTagHelper
{
	public static final String PROPERTY_TAG_TABLE = JRPdfExporter.PDF_EXPORTER_PROPERTIES_PREFIX + "tag.table";
	public static final String PROPERTY_TAG_TR = JRPdfExporter.PDF_EXPORTER_PROPERTIES_PREFIX + "tag.tr";
	public static final String PROPERTY_TAG_TH = JRPdfExporter.PDF_EXPORTER_PROPERTIES_PREFIX + "tag.th";
	public static final String PROPERTY_TAG_TD = JRPdfExporter.PDF_EXPORTER_PROPERTIES_PREFIX + "tag.td";
	public static final String PROPERTY_TAG_H1 = JRPdfExporter.PDF_EXPORTER_PROPERTIES_PREFIX + "tag.h1";
	public static final String PROPERTY_TAG_H2 = JRPdfExporter.PDF_EXPORTER_PROPERTIES_PREFIX + "tag.h2";
	public static final String PROPERTY_TAG_H3 = JRPdfExporter.PDF_EXPORTER_PROPERTIES_PREFIX + "tag.h3";
	public static final String PROPERTY_TAG_COLSPAN = JRPdfExporter.PDF_EXPORTER_PROPERTIES_PREFIX + "tag.colspan";
	public static final String PROPERTY_TAG_ROWSPAN = JRPdfExporter.PDF_EXPORTER_PROPERTIES_PREFIX + "tag.rowspan";
	public static final String TAG_START = "start";
	public static final String TAG_END = "end";
	public static final String TAG_FULL = "full";
	
	protected JRPdfExporter exporter;

	protected PdfContentByte pdfContentByte;
	protected PdfWriter pdfWriter;

	protected PdfStructureElement allTag;
	protected Stack<PdfStructureElement> tagStack;
	protected boolean isTagEmpty = true;
	protected int crtCrosstabRowY = -1;
	protected int crosstabFrameDepth;
	protected boolean isDataCellPrinted;

	protected boolean isTagged;

	/**
	 *
	 */
	protected JRPdfExporterTagHelper(JRPdfExporter exporter)
	{
		this.exporter = exporter;
	}
	
	/**
	 *
	 */
	protected void setTagged(boolean isTagged)
	{
		this.isTagged = isTagged;
	}
	
	/**
	 *
	 */
	protected void setPdfWriter(PdfWriter pdfWriter)
	{
		this.pdfWriter = pdfWriter;
		
		if (isTagged)
		{
			pdfWriter.setTagged();
		}
	}
	
	/**
	 *
	 */
	protected void init(PdfContentByte pdfContentByte)
	{
		this.pdfContentByte = pdfContentByte;
		
		if (isTagged)
		{
			PdfStructureTreeRoot root = pdfWriter.getStructureTreeRoot();
			allTag = new PdfStructureElement(root, new PdfName("All"));
			root.mapRole(new PdfName("All"), new PdfName("Sect"));
			root.mapRole(new PdfName("Anchor"), PdfName.TEXT);
			String language = 
				exporter.getStringParameter(
						JRPdfExporterParameter.TAG_LANGUAGE,
						JRPdfExporterParameter.PROPERTY_TAG_LANGUAGE
						);
			if (language != null)
			{
				allTag.put(PdfName.LANG, new PdfString(language));
			}
			tagStack = new Stack<PdfStructureElement>();
			tagStack.push(allTag);
		}
	}
	
	protected void startPageAnchor()
	{
		if (isTagged)
		{
			PdfStructureElement textTag = new PdfStructureElement(allTag, new PdfName("Anchor"));
			pdfContentByte.beginMarkedContentSequence(textTag);
		}
	}
	
	protected void endPageAnchor()
	{
		if (isTagged)
		{
			pdfContentByte.endMarkedContentSequence();
		}
	}

	protected void startPage()
	{
		crtCrosstabRowY = -1;
		crosstabFrameDepth = 0;
		isDataCellPrinted = false;
		
	}
	
	protected void endPage()
	{
		if (isTagged)
		{
			if (crtCrosstabRowY >= 0) //crosstab still open
			{
				//end the current row
				pdfContentByte.endMarkedContentSequence();
				tagStack.pop();
				
				//end the table
				pdfContentByte.endMarkedContentSequence();
				tagStack.pop();
			}
		}
	}

	protected void startElement(JRPrintElement element)
	{
		if (isTagged)
		{
			if (element instanceof JRPrintFrame)
			{
				JRPrintFrame frame = (JRPrintFrame) element;
				
				boolean isCellContentsFrame =
					frame.getPropertiesMap().hasProperties()
					&& frame.getPropertiesMap().getProperty(JRCellContents.PROPERTY_TYPE) != null;
				
				if (crtCrosstabRowY >= 0) //crosstab already started
				{
					//frame depth must be incremented for all frame, when inside a crosstab
					crosstabFrameDepth++;

					if (isCellContentsFrame)
					{
						if (JRCellContents.TYPE_DATA.equals(frame.getPropertiesMap().getProperty(JRCellContents.PROPERTY_TYPE)))
						{
							isDataCellPrinted = true;
						}

						if (crtCrosstabRowY != frame.getY())
						{
							//this is the first cell on a new row

							//end the current row
							pdfContentByte.endMarkedContentSequence();
							tagStack.pop();
							
							if (
								isDataCellPrinted
								&& (JRCellContents.TYPE_CROSSTAB_HEADER.equals(frame.getPropertiesMap().getProperty(JRCellContents.PROPERTY_TYPE))
									|| JRCellContents.TYPE_COLUMN_HEADER.equals(frame.getPropertiesMap().getProperty(JRCellContents.PROPERTY_TYPE)))
								)
							{
								//end the table
								pdfContentByte.endMarkedContentSequence();
								tagStack.pop();

								//start table
								createTableStartTag();
								//start crosstab
								isDataCellPrinted = false;
							}

							//start the new row
							createTrStartTag();

							//keep crosstab open, but mark new row position and frame depth
							crtCrosstabRowY = frame.getY();
						}
					}
					else
					{
						if (crosstabFrameDepth == 1)
						{
							//normal frame outside crosstab
							
							//end the current row
							pdfContentByte.endMarkedContentSequence();
							tagStack.pop();
							
							//end the table
							pdfContentByte.endMarkedContentSequence();
							tagStack.pop();
							
							//end crosstab
							crtCrosstabRowY = -1;
							//make depth zero because it will not be decremented after frame export 
							// due to crosstab being ended here
							crosstabFrameDepth = 0;
						}
					}
				}
				else
				{
					if (isCellContentsFrame)
					{
						//start table and firts row
						createTableStartTag();
						createTrStartTag();
						//start crosstab
						crtCrosstabRowY = frame.getY();
						crosstabFrameDepth++;
						isDataCellPrinted = false;
					}
//					else
//					{
//						//normal frame outside crosstab
//					}
				}
			}
			else
			{
				if (crtCrosstabRowY >= 0) //crosstab already started
				{
					if (crosstabFrameDepth == 0)
					{
						//normal element outside crosstab
						
						//end the current row
						pdfContentByte.endMarkedContentSequence();
						tagStack.pop();
						
						//end the table
						pdfContentByte.endMarkedContentSequence();
						tagStack.pop();
						
						//end crosstab
						crtCrosstabRowY = -1;
						crosstabFrameDepth = 0;
					}
				}
			}
			
			createStartTags(element);
		}
	}
	
	protected void endElement(JRPrintElement element)
	{
		if (isTagged)
		{
			if (element instanceof JRPrintFrame)
			{
				if (crtCrosstabRowY >= 0) // crosstab started
				{
					crosstabFrameDepth--;
				}
			}
			
			createEndTags(element);
		}
	}

	protected void startImage(JRPrintImage printImage)
	{
		if (isTagged)
		{
			PdfStructureElement imageTag = new PdfStructureElement(allTag, PdfName.IMAGE);
			pdfContentByte.beginMarkedContentSequence(imageTag);
			if (printImage.getHyperlinkTooltip() != null)
			{
				imageTag.put(PdfName.ALT, new PdfString(printImage.getHyperlinkTooltip()));
			}
		}
	}

	protected void endImage()
	{
		if (isTagged)
		{
			pdfContentByte.endMarkedContentSequence();
		}
	}

	protected void startText()
	{
		if (isTagged)
		{
//			PdfStructureElement parentTag = tableCellTag == null ? (tableHeaderTag == null ? allTag : tableHeaderTag): tableCellTag;
//			PdfStructureElement textTag = new PdfStructureElement(parentTag, PdfName.TEXT);
			PdfStructureElement textTag = new PdfStructureElement(tagStack.peek(), PdfName.TEXT);
			pdfContentByte.beginMarkedContentSequence(textTag);
		}
	}

	protected void endText()
	{
		if (isTagged)
		{
			pdfContentByte.endMarkedContentSequence();
			isTagEmpty = false;
		}
	}

	protected void createStartTags(JRPrintElement element)
	{
		if (element.hasProperties())
		{
			String prop = element.getPropertiesMap().getProperty(PROPERTY_TAG_TABLE);
			if (prop != null && (TAG_START.equals(prop) || TAG_FULL.equals(prop)))
			{
				createTableStartTag();
			}
			
			prop = element.getPropertiesMap().getProperty(PROPERTY_TAG_TR);
			if (prop != null && (TAG_START.equals(prop) || TAG_FULL.equals(prop)))
			{
				createTrStartTag();
			}
			
			prop = element.getPropertiesMap().getProperty(PROPERTY_TAG_TH);
			if (prop != null && (TAG_START.equals(prop) || TAG_FULL.equals(prop)))
			{
				createThStartTag(element);
			}

			prop = element.getPropertiesMap().getProperty(PROPERTY_TAG_TD);
			if (prop != null && (TAG_START.equals(prop) || TAG_FULL.equals(prop)))
			{
				createTdStartTag(element);
			}
			
			prop = element.getPropertiesMap().getProperty(JRCellContents.PROPERTY_TYPE);
			if (
				prop != null 
				&& (JRCellContents.TYPE_CROSSTAB_HEADER.equals(prop) 
					|| JRCellContents.TYPE_COLUMN_HEADER.equals(prop)
					|| JRCellContents.TYPE_ROW_HEADER.equals(prop)))
			{
				createThStartTag(element);
			}
			if (prop != null && (JRCellContents.TYPE_DATA.equals(prop)))
			{
				createTdStartTag(element);
			}
			
			prop = element.getPropertiesMap().getProperty(PROPERTY_TAG_H1);
			if (prop != null && (TAG_START.equals(prop) || TAG_FULL.equals(prop)))
			{
				PdfStructureElement headingTag = new PdfStructureElement(tagStack.peek(), new PdfName("H1"));
				pdfContentByte.beginMarkedContentSequence(headingTag);
				headingTag.put(PdfName.K, new PdfArray());
				tagStack.push(headingTag);
				isTagEmpty = true;
			}
			
			prop = element.getPropertiesMap().getProperty(PROPERTY_TAG_H2);
			if (prop != null && (TAG_START.equals(prop) || TAG_FULL.equals(prop)))
			{
				PdfStructureElement headingTag = new PdfStructureElement(tagStack.peek(), new PdfName("H2"));
				pdfContentByte.beginMarkedContentSequence(headingTag);
				headingTag.put(PdfName.K, new PdfArray());
				tagStack.push(headingTag);
				isTagEmpty = true;
			}
			
			prop = element.getPropertiesMap().getProperty(PROPERTY_TAG_H3);
			if (prop != null && (TAG_START.equals(prop) || TAG_FULL.equals(prop)))
			{
				PdfStructureElement headingTag = new PdfStructureElement(tagStack.peek(), new PdfName("H3"));
				pdfContentByte.beginMarkedContentSequence(headingTag);
				headingTag.put(PdfName.K, new PdfArray());
				tagStack.push(headingTag);
				isTagEmpty = true;
			}
		}
	}


	protected void createTableStartTag()
	{
		PdfStructureElement tableTag = new PdfStructureElement(allTag, new PdfName("Table"));
		pdfContentByte.beginMarkedContentSequence(tableTag);
		tableTag.put(PdfName.K, new PdfArray());
		tagStack.push(tableTag);
	}
		
	
	protected void createTrStartTag()
	{
		PdfStructureElement tableRowTag = new PdfStructureElement(tagStack.peek(), new PdfName("TR"));
		pdfContentByte.beginMarkedContentSequence(tableRowTag);
		tableRowTag.put(PdfName.K, new PdfArray());
		tagStack.push(tableRowTag);
	}
		
	
	protected void createThStartTag(JRPrintElement element)
	{
		PdfStructureElement tableHeaderTag = new PdfStructureElement(tagStack.peek(), new PdfName("TH"));
		pdfContentByte.beginMarkedContentSequence(tableHeaderTag);
		tableHeaderTag.put(PdfName.K, new PdfArray());
		tagStack.push(tableHeaderTag);
		isTagEmpty = true;
		
		createSpanTags(element, tableHeaderTag);
	}

	
	protected void createTdStartTag(JRPrintElement element)
	{
		PdfStructureElement tableCellTag = new PdfStructureElement(tagStack.peek(), new PdfName("TD"));
		pdfContentByte.beginMarkedContentSequence(tableCellTag);
		tableCellTag.put(PdfName.K, new PdfArray());
		tagStack.push(tableCellTag);
		isTagEmpty = true;
		
		createSpanTags(element, tableCellTag);
	}
		

	protected void createSpanTags(JRPrintElement element, PdfStructureElement parentTag)
	{
		int colSpan = 0;
		int rowSpan = 0;
		try	{
			colSpan = Integer.valueOf(element.getPropertiesMap().getProperty(PROPERTY_TAG_COLSPAN)).intValue();
		} catch (NumberFormatException e) {
			try	{
				colSpan = Integer.valueOf(element.getPropertiesMap().getProperty(JRCellContents.PROPERTY_COLUMN_SPAN)).intValue();
			} catch (NumberFormatException ex) {}
		}
		try {
			rowSpan = Integer.valueOf(element.getPropertiesMap().getProperty(PROPERTY_TAG_ROWSPAN)).intValue();
		} catch (NumberFormatException e) {
			try {
				rowSpan = Integer.valueOf(element.getPropertiesMap().getProperty(JRCellContents.PROPERTY_ROW_SPAN)).intValue();
			} catch (NumberFormatException ex) {}
		}
		if (colSpan > 1 || rowSpan > 1)
		{
			PdfArray a = new PdfArray();
			PdfDictionary dict = new PdfDictionary();
			if (colSpan > 1)
			{
				dict.put(new PdfName("ColSpan"), new PdfNumber(colSpan));
			}
			if (rowSpan > 1)
			{
				dict.put(new PdfName("RowSpan"), new PdfNumber(rowSpan));
			}
			dict.put(PdfName.O, new PdfName("Table"));
			a.add(dict);
			parentTag.put(PdfName.A, a);
		}
	}


	protected void createEndTags(JRPrintElement element)// throws DocumentException, IOException, JRException
	{
		if (element.hasProperties())
		{
			String prop = element.getPropertiesMap().getProperty(PROPERTY_TAG_TABLE);
			if (prop != null && (TAG_END.equals(prop) || TAG_FULL.equals(prop)))
			{
				pdfContentByte.endMarkedContentSequence();
				tagStack.pop();
			}
			
			prop = element.getPropertiesMap().getProperty(PROPERTY_TAG_TR);
			if (prop != null && (TAG_END.equals(prop) || TAG_FULL.equals(prop)))
			{
				pdfContentByte.endMarkedContentSequence();
				tagStack.pop();
			}
			
			prop = element.getPropertiesMap().getProperty(PROPERTY_TAG_TH);
			if (prop != null && (TAG_END.equals(prop) || TAG_FULL.equals(prop)))
			{
				pdfContentByte.endMarkedContentSequence();
				
				if (isTagEmpty)
				{
					pdfContentByte.beginMarkedContentSequence(new PdfStructureElement(tagStack.peek(), PdfName.SPAN));
					pdfContentByte.endMarkedContentSequence();
				}
				
				tagStack.pop();
			}
			
			prop = element.getPropertiesMap().getProperty(PROPERTY_TAG_TD);
			if (prop != null && (TAG_END.equals(prop) || TAG_FULL.equals(prop)))
			{
				pdfContentByte.endMarkedContentSequence();
				
				if (isTagEmpty)
				{
					pdfContentByte.beginMarkedContentSequence(new PdfStructureElement(tagStack.peek(), PdfName.SPAN));
					pdfContentByte.endMarkedContentSequence();
				}

				tagStack.pop();
			}

			prop = element.getPropertiesMap().getProperty(JRCellContents.PROPERTY_TYPE);
			if (
				prop != null 
				&& (JRCellContents.TYPE_CROSSTAB_HEADER.equals(prop) 
					|| JRCellContents.TYPE_COLUMN_HEADER.equals(prop)
					|| JRCellContents.TYPE_ROW_HEADER.equals(prop)
					|| JRCellContents.TYPE_DATA.equals(prop)))
			{
				pdfContentByte.endMarkedContentSequence();
				
				if (isTagEmpty)
				{
					pdfContentByte.beginMarkedContentSequence(new PdfStructureElement(tagStack.peek(), PdfName.SPAN));
					pdfContentByte.endMarkedContentSequence();
				}

				tagStack.pop();
			}
			
			prop = element.getPropertiesMap().getProperty(PROPERTY_TAG_H1);
			if (prop != null && (TAG_END.equals(prop) || TAG_FULL.equals(prop)))
			{
				pdfContentByte.endMarkedContentSequence(); 

				if (isTagEmpty)
				{
					pdfContentByte.beginMarkedContentSequence(new PdfStructureElement(tagStack.peek(), PdfName.SPAN));
					pdfContentByte.endMarkedContentSequence();
				}

				tagStack.pop();
			}

			prop = element.getPropertiesMap().getProperty(PROPERTY_TAG_H2);
			if (prop != null && (TAG_END.equals(prop) || TAG_FULL.equals(prop)))
			{
				pdfContentByte.endMarkedContentSequence(); 

				if (isTagEmpty)
				{
					pdfContentByte.beginMarkedContentSequence(new PdfStructureElement(tagStack.peek(), PdfName.SPAN));
					pdfContentByte.endMarkedContentSequence();
				}

				tagStack.pop();
			}

			prop = element.getPropertiesMap().getProperty(PROPERTY_TAG_H3);
			if (prop != null && (TAG_END.equals(prop) || TAG_FULL.equals(prop)))
			{
				pdfContentByte.endMarkedContentSequence(); 

				if (isTagEmpty)
				{
					pdfContentByte.beginMarkedContentSequence(new PdfStructureElement(tagStack.peek(), PdfName.SPAN));
					pdfContentByte.endMarkedContentSequence();
				}

				tagStack.pop();
			}
		}
	}
}
