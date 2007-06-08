/*
 * ============================================================================
 * GNU Lesser General Public License
 * ============================================================================
 *
 * JasperReports - Free Java report-generating library.
 * Copyright (C) 2001-2006 JasperSoft Corporation http://www.jaspersoft.com
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
 * 303 Second Street, Suite 450 North
 * San Francisco, CA 94107
 * http://www.jaspersoft.com
 */

/*
 * Contributors:
 * Ryan Johnson - delscovich@users.sourceforge.net
 * Carlton Moore - cmoore79@users.sourceforge.net
 */
package net.sf.jasperreports.view;

import java.awt.Color;
import java.awt.Image;
import java.awt.font.TextAttribute;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import net.sf.jasperreports.crosstabs.JRCellContents;
import net.sf.jasperreports.crosstabs.JRCrosstab;
import net.sf.jasperreports.crosstabs.JRCrosstabCell;
import net.sf.jasperreports.crosstabs.JRCrosstabColumnGroup;
import net.sf.jasperreports.crosstabs.JRCrosstabRowGroup;
import net.sf.jasperreports.crosstabs.fill.calculation.BucketDefinition;
import net.sf.jasperreports.engine.JRBand;
import net.sf.jasperreports.engine.JRBox;
import net.sf.jasperreports.engine.JRChart;
import net.sf.jasperreports.engine.JRChild;
import net.sf.jasperreports.engine.JRDefaultStyleProvider;
import net.sf.jasperreports.engine.JRElement;
import net.sf.jasperreports.engine.JRElementGroup;
import net.sf.jasperreports.engine.JREllipse;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExpression;
import net.sf.jasperreports.engine.JRExpressionChunk;
import net.sf.jasperreports.engine.JRFrame;
import net.sf.jasperreports.engine.JRGraphicElement;
import net.sf.jasperreports.engine.JRGroup;
import net.sf.jasperreports.engine.JRImage;
import net.sf.jasperreports.engine.JRImageRenderer;
import net.sf.jasperreports.engine.JRLine;
import net.sf.jasperreports.engine.JRPrintLine;
import net.sf.jasperreports.engine.JRPrintPage;
import net.sf.jasperreports.engine.JRRectangle;
import net.sf.jasperreports.engine.JRRenderable;
import net.sf.jasperreports.engine.JRReport;
import net.sf.jasperreports.engine.JRStaticText;
import net.sf.jasperreports.engine.JRStyle;
import net.sf.jasperreports.engine.JRTextElement;
import net.sf.jasperreports.engine.JRTextField;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.base.JRBasePrintElement;
import net.sf.jasperreports.engine.base.JRBasePrintEllipse;
import net.sf.jasperreports.engine.base.JRBasePrintFrame;
import net.sf.jasperreports.engine.base.JRBasePrintGraphicElement;
import net.sf.jasperreports.engine.base.JRBasePrintImage;
import net.sf.jasperreports.engine.base.JRBasePrintLine;
import net.sf.jasperreports.engine.base.JRBasePrintPage;
import net.sf.jasperreports.engine.base.JRBasePrintRectangle;
import net.sf.jasperreports.engine.base.JRBasePrintText;
import net.sf.jasperreports.engine.design.JRDesignFrame;
import net.sf.jasperreports.engine.fill.TextMeasurer;
import net.sf.jasperreports.engine.util.JRFontUtil;
import net.sf.jasperreports.engine.util.JRImageLoader;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.engine.util.JRStyledText;
import net.sf.jasperreports.engine.util.JRStyledTextParser;

import org.xml.sax.SAXException;

/**
 * @author sanda zaharia 
 * @version $Id$
 */
public class JRPreviewBuilder 
{

	private JasperPrint jasperPrint;
	private JRPrintPage page;
	int pageWidth;
	private int offsetX;
	private int offsetY;
	private int upColumns;
	private int downColumns;
	
	/**
	 * List containing ContourElement objects, containers for contour line coordinates and dimensions 
	 */
	private List contourElementsList = new ArrayList();
	
	/**
	 * List containing int[4] arrays, representing band separator line coordinates and dimensions 
	 */
	private List bandSeparatorList = new ArrayList();
	
	/**
	 * List containing page elements in a given order 
	 */
	private List pageElements = new ArrayList();
	
	private JRDefaultStyleProvider defaultStyleProvider;
	private JRStyledTextParser styledTextParser = new JRStyledTextParser();

	private boolean isFirstBand = true;
	
	private boolean hasContour;

	/**
	 * Constructors
	 */
	
	public JRPreviewBuilder()
	{
	}
	 
	public JRPreviewBuilder(JRReport report)  throws JRException
	{
		createJasperPrint(report);
	}
	
	/**
	 * This method reads a JRReport object and creates a simmilar JasperPrint object
	 * containing the design representation information
	 * @param report the JRReport object
	 * @throws JRException
	 */
	private void createJasperPrint(JRReport report) throws JRException
	{
		jasperPrint = new JasperPrint();
		defaultStyleProvider = jasperPrint.getDefaultStyleProvider();
		page = new JRBasePrintPage();
		pageWidth = report.getPageWidth();
		
		jasperPrint.setDefaultFont(report.getDefaultFont());
		jasperPrint.setDefaultStyle(report.getDefaultStyle());
		jasperPrint.setFormatFactoryClass(report.getFormatFactoryClass());
		//TODO: locale and timezone settings jasperprint object
//		jasperPrint.setLocaleCode(JRDataUtils.getLocaleCode(Locale.getDefault()));
//		jasperPrint.setTimeZoneId(JRDataUtils.getTimeZoneId(TimeZone.getDefault()));
		jasperPrint.setName(report.getName());
		jasperPrint.setOrientation(report.getOrientation());
		jasperPrint.setPageWidth(pageWidth);
		offsetY = report.getTopMargin();
		offsetX = report.getLeftMargin();
		
		JRStyle[] styles = report.getStyles();
		if (styles != null)
			for (int i = 0; i < styles.length; i++)
			{
				jasperPrint.addStyle(styles[i]);
			}
			

		JRBand band = report.getBackground();
		if(band != null)
			setElements(band.getChildren(), report.getPageHeight(), 0, 0, 0, 0, false);

		Color color = new Color(170, 170, 255);
		
		addBandElements(report.getTitle(), color);
		addBandElements(report.getPageHeader(), color);
		upColumns = offsetY;
		addBandElements(report.getColumnHeader(), color);
		
		JRGroup[] groups = report.getGroups();
		if (groups != null)
		{
			for (int i = 0; i < groups.length ; i++)
			{
				addBandElements(groups[i].getGroupHeader(), color);
			}
		}
		
		addBandElements(report.getDetail(), color);

		if (groups != null)
		{
			for (int i = 0; i < groups.length ; i++)
			{
				addBandElements(groups[i].getGroupFooter(), color);
			}
		}
		
		addBandElements(report.getColumnFooter(), color);
		downColumns = offsetY;
		addBandElements(report.getPageFooter(), color);
		addBandElements(report.getLastPageFooter(), color);
		addBandElements(report.getSummary(), color);
		jasperPrint.setPageHeight(offsetY + report.getBottomMargin());
		
		// contour lines
		for(int i = 0; i < contourElementsList.size(); i++)
		{
			pageElements.add(getContourShape((ContourElement)contourElementsList.get(i)));
		}
		
		//band dotted delimitation
		for(int i = 0; i < bandSeparatorList.size(); i++)
		{
			pageElements.add(0,getContourLine((ContourElement)bandSeparatorList.get(i)));
		}
		
		// column dotted delimitation 
		int leftColumnPos = report.getLeftMargin();
		for(int i = 0; i< report.getColumnCount(); i++)
		{
			pageElements.add(0,getContourLine(new ContourElement(leftColumnPos, upColumns, 0, downColumns - upColumns, color, JRGraphicElement.PEN_DOTTED)));
			leftColumnPos += report.getColumnWidth();
			pageElements.add(0,getContourLine(new ContourElement(leftColumnPos, upColumns, 0, downColumns - upColumns, color, JRGraphicElement.PEN_DOTTED)));
			leftColumnPos += report.getColumnSpacing();
		}
		// page dotted contour line
		pageElements.add(0,getContourLine(new ContourElement(0, report.getTopMargin(), pageWidth, 0, color, JRGraphicElement.PEN_DOTTED)));
		pageElements.add(0,getContourLine(new ContourElement(0, offsetY, pageWidth, 0, color, JRGraphicElement.PEN_DOTTED)));
		pageElements.add(0,getContourLine(new ContourElement(offsetX, 0, 0, jasperPrint.getPageHeight(), color, JRGraphicElement.PEN_DOTTED)));
		pageElements.add(0,getContourLine(new ContourElement(pageWidth - report.getRightMargin(), 0, 0, jasperPrint.getPageHeight(), color, JRGraphicElement.PEN_DOTTED)));

		page.setElements(pageElements);
		jasperPrint.addPage(page);
	}

	/**
	 * This method reads elements or element groups from a JRDesignElements list (usually a band elements
	 * list), and transfers all their relevant properties to the correspondents JRPrintElements
	 * @param list list of JRDesignElement
	 * @param bandHeight the band height 
	 */
	private void setElements(List list, int bandHeight, int parentX, int parentY, int parentWidth, int parentHeight, boolean isFrameChild)
	{
		Iterator it = list.iterator();
		while (it.hasNext())
		{
			Object element = it.next();
			JRBasePrintElement baseElement = null;
			
			//element could be either a JRFrame, JRDesignElementGroup or a JRDesignElement instance
			if(element instanceof JRFrame)
			{
				//it extends both JRElement and JRElementGroup
				JRFrame frameElement = (JRFrame)element;
				baseElement = getFrameElement(frameElement);
				setBaseElement(baseElement,(JRElement)element, bandHeight, parentX, parentY, parentWidth, parentHeight, isFrameChild);
				if(baseElement.getHeight() > 0)
					pageElements.add(baseElement);
				
				JRBasePrintFrame basePrintFrameElement =(JRBasePrintFrame)baseElement;
				
				// one add contour lines only if the element has no its own border line
				// if at least one border line is defined, the element gets no contour line
				if(basePrintFrameElement.getBorder() == JRGraphicElement.PEN_NONE &&
						basePrintFrameElement.getTopBorder() == JRGraphicElement.PEN_NONE &&
						basePrintFrameElement.getLeftBorder() == JRGraphicElement.PEN_NONE &&
						basePrintFrameElement.getRightBorder() == JRGraphicElement.PEN_NONE &&
						basePrintFrameElement.getBottomBorder() == JRGraphicElement.PEN_NONE)
					addContourElement(baseElement);
				
				// setting frame children elements
				List frameChildren = Arrays.asList(frameElement.getElements());
				List availableElements = new ArrayList();
				Iterator frameIt = frameChildren.iterator();
				while(frameIt.hasNext())
				{
					JRElement elem = (JRElement)frameIt.next();
					// consider only elements
					if(elem.getY() < frameElement.getHeight() && elem.getX() < frameElement.getWidth())
					{
						availableElements.add(elem);
					}
				}
				setElements(availableElements,bandHeight,basePrintFrameElement.getX(),basePrintFrameElement.getY(),basePrintFrameElement.getWidth(),basePrintFrameElement.getHeight(), true);
			}
			else if(element instanceof JRElementGroup)
			{
				List children = ((JRElementGroup)element).getChildren();
				setElements(children, bandHeight, parentX, parentY, parentWidth, parentHeight, isFrameChild);
			}
			else
			{
				hasContour = false;
				
				if(element instanceof JRImage)
				{
					baseElement = getImageElement((JRImage)element);
				}
				else if (element instanceof JRGraphicElement)
				{
					baseElement = getGraphicElement((JRGraphicElement)element);
				}
				else if(element instanceof JRTextElement)
				{
					baseElement = getTextElement((JRTextElement)element);
				}
				else if(element instanceof JRChart)
				{
					baseElement = getChartElement((JRChart)element);
				}
				else if(element instanceof JRCrosstab)
				{
					setElements(getCrosstabList((JRCrosstab)element), bandHeight, parentX, parentY, parentWidth, parentHeight, isFrameChild);
					continue;
				}
				else
				{
					baseElement = new JRBasePrintElement(defaultStyleProvider);
				}
				
				setBaseElement(baseElement,(JRElement)element, bandHeight, parentX, parentY, parentWidth, parentHeight, isFrameChild);
				
				if(element instanceof JRTextElement)
				{
					JRBasePrintText basePrintTextElement = (JRBasePrintText) baseElement;
					measureTextElement(basePrintTextElement, (JRTextElement)element);
				}
				
				// one add elements in this list before setting the print page elements,
				// because dotted design lines should be painted first
				pageElements.add(baseElement);

				if(!hasContour)
				{
					// preparing for drawing additional contour lines
					addContourElement(baseElement);
				}
				hasContour = false;
			}
		}
	}

	/**
	 * The method is a caller for the main setElements() method
	 * @param band the band where elements are picked up from
	 */
	private void addBandElements (JRBand band, Color color)
	{
		if(band != null)
		{
			if(isFirstBand)
			{	
				bandSeparatorList.add(new ContourElement(0, offsetY, pageWidth, 0, color, JRGraphicElement.PEN_DOTTED));
			}
			setElements(band.getChildren(), band.getHeight(), 0, 0, 0, 0, false);
			offsetY += band.getHeight();
			bandSeparatorList.add(new ContourElement(0, offsetY, pageWidth, 0, color, JRGraphicElement.PEN_DOTTED));
			isFirstBand = false;
		}
	}
	
	/**
	 * 
	 * @param exp
	 * @return
	 */
	private String getExpressionText(JRExpression exp)
	{
		return exp == null ? null : exp.getText();
	}
	
	/**
	 * Returns a styled text associated with a textElement
	 * @param textElement
	 * @return
	 */
	private JRStyledText getStyledText(JRTextElement textElement)
	{
		JRStyledText styledText = null;

		String text = null;
		if (textElement instanceof JRStaticText)
		{
			text = ((JRStaticText)textElement).getText();
		}
		else if (textElement instanceof JRTextField)
		{
			JRExpression textExpression = ((JRTextField) textElement).getExpression();
			if (textExpression != null)
			{
				text = textExpression.getText();
			}
		}
		
		if (text == null)
		{
			text = "";
		}

		Map attributes = new HashMap(); 
		JRFontUtil.setAttributes(attributes, textElement);
		attributes.put(TextAttribute.FOREGROUND, textElement.getForecolor());

		if (
			textElement instanceof JRStaticText
			&& textElement.isStyledText()
			)
		{
			try
			{
				styledText = styledTextParser.parse(attributes, text);
			}
			catch (SAXException e)
			{
				//ignore if invalid styled text and treat like normal text
			}
		}
	
		if (styledText == null)
		{
			styledText = new JRStyledText();
			styledText.append(text);
			styledText.addRun(new JRStyledText.Run(attributes, 0, text.length()));
		}
		
		return styledText;
	}
	
	 /**
	  * 
	  * @param imageElement
	  * @return
	  */
	private JRRenderable getRenderer(JRImage imageElement)
	{
		JRRenderable imageRenderer = null;
		Image awtImage = null;
		JRExpression expression = imageElement.getExpression();
		if(expression != null)
		{
			if (expression.getChunks().length == 1)
			{
				JRExpressionChunk firstChunk = expression.getChunks()[0];
				
				if (firstChunk.getType() == JRExpressionChunk.TYPE_TEXT)
				{
					String location = firstChunk.getText().trim();
					if (location.startsWith("\"") && location.endsWith("\""))
					{
						location = location.substring(1, location.length() - 1);
						try
						{
							awtImage = JRImageLoader.loadImage(
								JRLoader.loadBytesFromLocation(location)
								);
							if (awtImage == null)
							{
								awtImage = JRImageLoader.getImage(JRImageLoader.NO_IMAGE);
								imageElement.setScaleImage(JRImage.SCALE_IMAGE_CLIP);
								imageElement.setStretchType(JRElement.STRETCH_TYPE_NO_STRETCH);
							}
							imageRenderer = JRImageRenderer.getInstance(
									awtImage, 
									imageElement.getOnErrorType()
									);
							return imageRenderer;
						}
						catch (JRException e)
						{
							e.printStackTrace();
						}
					}
				}
			}
			
		}
		try
		{
			awtImage = JRImageLoader.getImage(JRImageLoader.NO_IMAGE);
			imageRenderer = JRImageRenderer.getInstance(
					awtImage, 
					imageElement.getOnErrorType()
					);
			imageElement.setScaleImage(JRImage.SCALE_IMAGE_CLIP);
			imageElement.setStretchType(JRElement.STRETCH_TYPE_NO_STRETCH);
		}
		catch (JRException e)
		{
			e.printStackTrace();
		}
		
		return imageRenderer;
	}

	 /**
	  * 
	  * @param imageElement image representing a chart element
	  * @return
	  */
	private JRRenderable getRenderer(JRChart imageElement)
	{
		JRRenderable imageRenderer = null;
		Image awtImage = null;
		
		try
		{
			awtImage = JRImageLoader.getImage(JRImageLoader.CHART_IMAGE);
			if(awtImage == null)
			{
				awtImage = JRImageLoader.getImage(JRImageLoader.NO_IMAGE);
			}
			imageRenderer = JRImageRenderer.getInstance(
					awtImage, 
					JRImage.ON_ERROR_TYPE_ICON
					);
			imageElement.setStretchType(JRElement.STRETCH_TYPE_NO_STRETCH);
		}
		catch (JRException e)
		{
			e.printStackTrace();
		}
		
		return imageRenderer;
	}

	/**
	 * Return a top-down dotted line having the specified coordinates and dimensions
	 * @param x the x coordinate of the starting point
	 * @param y the y coordinate of the starting point
	 * @param width the line width
	 * @param height the line height
	 * @param color the line color
	 * @param pen the line style
	 * @return JRPrintLine
	 */
	private JRPrintLine getContourLine(ContourElement contour)
	{
		JRPrintLine line = new JRBasePrintLine(defaultStyleProvider);
		line.setX(contour.getContourX());
		line.setY(contour.getContourY());
		line.setWidth(contour.getContourWidth());
		line.setHeight(contour.getContourHeight());
		line.setPen(contour.getContourPen());
		line.setForecolor(contour.getContourColor());
		line.setDirection(JRLine.DIRECTION_TOP_DOWN);
		return line;
	}
	
	/**
	 * Return a top-down dotted line having the specified coordinates and dimensions
	 * @param x the x coordinate of the starting point
	 * @param y the y coordinate of the starting point
	 * @param width the line width
	 * @param height the line height
	 * @param color the line color
	 * @param pen the line style
	 * @return JRPrintLine
	 */
	private JRBasePrintElement getContourShape(ContourElement contour)
	{
		JRBasePrintRectangle rectangle = new JRBasePrintRectangle(defaultStyleProvider);
		rectangle.setX(contour.getContourX());
		rectangle.setY(contour.getContourY());
		rectangle.setWidth(contour.getContourWidth());
		rectangle.setHeight(contour.getContourHeight());
		rectangle.setPen(JRGraphicElement.PEN_THIN);
		rectangle.setForecolor(contour.getContourColor());
		rectangle.setMode(JRElement.MODE_TRANSPARENT);
		return rectangle;
	}
	
	/**
	 * 
	 * @param pen
	 * @return
	 */
	private int getBorderWidth(byte pen)
	{
		int borderWidth = 1;
		switch (pen)
		{
			case JRGraphicElement.PEN_4_POINT :
			{
				borderWidth = 4;
				break;
			}
			case JRGraphicElement.PEN_2_POINT :
			{
				borderWidth = 2;
				break;
			}
			case JRGraphicElement.PEN_NONE :
			{
				borderWidth = 0;
				break;
			}
			default :
			{
				borderWidth = 1;
				break;
			}
		}
		return borderWidth;
	}

	/**
	 * 
	 * @param imageElement
	 * @return
	 */
	private JRBasePrintElement getImageElement (JRImage imageElement)
	{
		JRBasePrintImage basePrintImageElement = new JRBasePrintImage(defaultStyleProvider);

		basePrintImageElement.setAnchorName(getExpressionText(
				imageElement.getAnchorNameExpression())
				);
		basePrintImageElement.setBookmarkLevel(imageElement.getBookmarkLevel());
		basePrintImageElement.setBorder(imageElement.getBorder());
		basePrintImageElement.setBorderColor(imageElement.getBorderColor());
		basePrintImageElement.setBottomBorder(imageElement.getBottomBorder());
		basePrintImageElement.setBottomBorderColor(imageElement.getBottomBorderColor());
		basePrintImageElement.setBottomPadding(imageElement.getBottomPadding());
		basePrintImageElement.setFill(imageElement.getFill());
		basePrintImageElement.setHorizontalAlignment(imageElement.getHorizontalAlignment());
		basePrintImageElement.setLazy(imageElement.isLazy());
		basePrintImageElement.setLeftBorder(imageElement.getLeftBorder());
		basePrintImageElement.setLeftBorderColor(imageElement.getLeftBorderColor());
		basePrintImageElement.setLeftPadding(imageElement.getLeftPadding());
		basePrintImageElement.setLinkType(imageElement.getLinkType());
		basePrintImageElement.setOnErrorType(imageElement.getOnErrorType());
		basePrintImageElement.setPadding(imageElement.getPadding());
		basePrintImageElement.setPen(imageElement.getPen());
		basePrintImageElement.setRightBorder(imageElement.getRightBorder());
		basePrintImageElement.setRightBorderColor(imageElement.getRightBorderColor());
		basePrintImageElement.setRightPadding(imageElement.getRightPadding());
		basePrintImageElement.setTopBorder(imageElement.getTopBorder());
		basePrintImageElement.setTopBorderColor(imageElement.getTopBorderColor());
		basePrintImageElement.setTopPadding(imageElement.getTopPadding());
		basePrintImageElement.setVerticalAlignment(imageElement.getVerticalAlignment());
		basePrintImageElement.setRenderer(getRenderer(imageElement));
		basePrintImageElement.setScaleImage(imageElement.getScaleImage());
		
		hasContour = (basePrintImageElement.getBorder() != JRGraphicElement.PEN_NONE ||
				(basePrintImageElement.getTopBorder() != JRGraphicElement.PEN_NONE &&
				basePrintImageElement.getLeftBorder() != JRGraphicElement.PEN_NONE &&
				basePrintImageElement.getRightBorder() != JRGraphicElement.PEN_NONE &&
				basePrintImageElement.getBottomBorder() != JRGraphicElement.PEN_NONE)
				);
		return basePrintImageElement;
	}

	/**
	 * 
	 * @param imageElement
	 * @return
	 */
	private JRBasePrintElement getChartElement (JRChart imageElement)
	{
		JRBasePrintImage basePrintImageElement = new JRBasePrintImage(defaultStyleProvider);

		basePrintImageElement.setAnchorName(getExpressionText(
				imageElement.getAnchorNameExpression())
				);
		basePrintImageElement.setBookmarkLevel(imageElement.getBookmarkLevel());
		basePrintImageElement.setBorder(imageElement.getBorder());
		basePrintImageElement.setBorderColor(imageElement.getBorderColor());
		basePrintImageElement.setBottomBorder(imageElement.getBottomBorder());
		basePrintImageElement.setBottomBorderColor(imageElement.getBottomBorderColor());
		basePrintImageElement.setBottomPadding(imageElement.getBottomPadding());
		basePrintImageElement.setLeftBorder(imageElement.getLeftBorder());
		basePrintImageElement.setLeftBorderColor(imageElement.getLeftBorderColor());
		basePrintImageElement.setLeftPadding(imageElement.getLeftPadding());
		basePrintImageElement.setLinkType(imageElement.getLinkType());
		basePrintImageElement.setOnErrorType(JRImage.ON_ERROR_TYPE_ICON);
		basePrintImageElement.setPadding(imageElement.getPadding());
		basePrintImageElement.setPen(JRGraphicElement.PEN_THIN);
		basePrintImageElement.setRightBorder(imageElement.getRightBorder());
		basePrintImageElement.setRightBorderColor(imageElement.getRightBorderColor());
		basePrintImageElement.setRightPadding(imageElement.getRightPadding());
		basePrintImageElement.setTopBorder(imageElement.getTopBorder());
		basePrintImageElement.setTopBorderColor(imageElement.getTopBorderColor());
		basePrintImageElement.setTopPadding(imageElement.getTopPadding());
		basePrintImageElement.setRenderer(getRenderer(imageElement));
		basePrintImageElement.setScaleImage(JRImage.SCALE_IMAGE_CLIP);
		hasContour = (basePrintImageElement.getBorder() != JRGraphicElement.PEN_NONE ||
				(basePrintImageElement.getTopBorder() != JRGraphicElement.PEN_NONE &&
				basePrintImageElement.getLeftBorder() != JRGraphicElement.PEN_NONE &&
				basePrintImageElement.getRightBorder() != JRGraphicElement.PEN_NONE &&
				basePrintImageElement.getBottomBorder() != JRGraphicElement.PEN_NONE)
				);
		return basePrintImageElement;
	}

	/**
	 * 
	 * @param element
	 * @return
	 */
	private JRBasePrintElement getGraphicElement(JRGraphicElement element)
	{
		JRBasePrintElement baseElement = null;
		if(element instanceof JREllipse)
		{
			baseElement = new JRBasePrintEllipse(defaultStyleProvider);
		}
		else if(element instanceof JRRectangle)
		{
			baseElement = new JRBasePrintRectangle(defaultStyleProvider);
			((JRBasePrintRectangle)baseElement).setRadius(((JRRectangle)element).getRadius());
		}
		else if(element instanceof JRLine)
		{
			baseElement = new JRBasePrintLine(defaultStyleProvider);
			((JRBasePrintLine)baseElement).setDirection(((JRLine)element).getDirection());
		}
		JRBasePrintGraphicElement basePrintGraphicElement = (JRBasePrintGraphicElement)baseElement;
		JRGraphicElement graphicElement = (JRGraphicElement)element;
		basePrintGraphicElement.setFill(graphicElement.getFill());
		basePrintGraphicElement.setPen(graphicElement.getPen());
		
		hasContour = basePrintGraphicElement.getPen() != JRGraphicElement.PEN_NONE;
		return basePrintGraphicElement;
	}
	
	/**
	 * 
	 * @param frameElement
	 * @return
	 */
	private JRBasePrintElement getFrameElement (JRFrame frameElement)
	{
		JRBasePrintFrame basePrintFrameElement = new JRBasePrintFrame(defaultStyleProvider);
		basePrintFrameElement.setBorder(frameElement.getBorder());
		basePrintFrameElement.setBorderColor(frameElement.getBorderColor());
		basePrintFrameElement.setBottomBorder(frameElement.getBottomBorder());
		basePrintFrameElement.setBottomBorderColor(frameElement.getBottomBorderColor());
		basePrintFrameElement.setBottomPadding(frameElement.getBottomPadding());
		basePrintFrameElement.setLeftBorder(frameElement.getLeftBorder());
		basePrintFrameElement.setLeftBorderColor(frameElement.getLeftBorderColor());
		basePrintFrameElement.setLeftPadding(frameElement.getLeftPadding());
		basePrintFrameElement.setPadding(frameElement.getPadding());
		basePrintFrameElement.setRightBorder(frameElement.getRightBorder());
		basePrintFrameElement.setRightBorderColor(frameElement.getRightBorderColor());
		basePrintFrameElement.setRightPadding(frameElement.getRightPadding());
		basePrintFrameElement.setTopBorder(frameElement.getTopBorder());
		basePrintFrameElement.setTopBorderColor(frameElement.getTopBorderColor());
		basePrintFrameElement.setTopPadding(frameElement.getTopPadding());
		return basePrintFrameElement;
	}
	
	/**
	 * 
	 * @param textElement
	 * @return
	 */
	private JRBasePrintElement getTextElement (JRTextElement textElement)
	{
		JRBasePrintText basePrintTextElement = new JRBasePrintText(defaultStyleProvider);
		
		basePrintTextElement.setBorder(textElement.getBorder());
		basePrintTextElement.setBorderColor(textElement.getBorderColor());
		basePrintTextElement.setBottomBorder(textElement.getBottomBorder());
		basePrintTextElement.setBottomBorderColor(textElement.getBottomBorderColor());
		basePrintTextElement.setBottomPadding(textElement.getBottomPadding());
		basePrintTextElement.setLeftBorder(textElement.getLeftBorder());
		basePrintTextElement.setLeftBorderColor(textElement.getLeftBorderColor());
		basePrintTextElement.setLeftPadding(textElement.getLeftPadding());
		basePrintTextElement.setPadding(textElement.getPadding());
		basePrintTextElement.setRightBorder(textElement.getRightBorder());
		basePrintTextElement.setRightBorderColor(textElement.getRightBorderColor());
		basePrintTextElement.setRightPadding(textElement.getRightPadding());
		basePrintTextElement.setTopBorder(textElement.getTopBorder());
		basePrintTextElement.setTopBorderColor(textElement.getTopBorderColor());
		basePrintTextElement.setTopPadding(textElement.getTopPadding());
		basePrintTextElement.setBold(textElement.isBold());
		basePrintTextElement.setFontName(textElement.getFontName());
		basePrintTextElement.setFontSize(textElement.getFontSize());
		basePrintTextElement.setHorizontalAlignment(textElement.getHorizontalAlignment());
		basePrintTextElement.setItalic(textElement.isItalic());
		basePrintTextElement.setLineSpacing(textElement.getLineSpacing());
		basePrintTextElement.setPdfEmbedded(textElement.isPdfEmbedded());
		basePrintTextElement.setPdfEncoding(textElement.getPdfEncoding());
		basePrintTextElement.setPdfFontName(textElement.getPdfFontName());
		basePrintTextElement.setReportFont(textElement.getReportFont());
		basePrintTextElement.setRotation(textElement.getRotation());
		basePrintTextElement.setStrikeThrough(textElement.isStrikeThrough());
		basePrintTextElement.setStyledText(textElement.isStyledText());
		basePrintTextElement.setUnderline(textElement.isUnderline());
		basePrintTextElement.setVerticalAlignment(textElement.getVerticalAlignment());

		if(textElement instanceof JRStaticText)
		{
			basePrintTextElement.setText(((JRStaticText)textElement).getText());
		}
		if(textElement instanceof JRTextField)
		{
			JRTextField textFieldElement = (JRTextField)textElement;
			basePrintTextElement.setAnchorName(
					getExpressionText(textFieldElement.getAnchorNameExpression())
					);
			basePrintTextElement.setBookmarkLevel(textFieldElement.getBookmarkLevel());
			basePrintTextElement.setLinkType(textFieldElement.getLinkType());
			basePrintTextElement.setPattern(textFieldElement.getPattern());
			basePrintTextElement.setText(
					getExpressionText(textFieldElement.getExpression())
					);
		}
		
		hasContour = (basePrintTextElement.getBorder() != JRGraphicElement.PEN_NONE ||
				(basePrintTextElement.getTopBorder() != JRGraphicElement.PEN_NONE &&
				basePrintTextElement.getLeftBorder() != JRGraphicElement.PEN_NONE &&
				basePrintTextElement.getRightBorder() != JRGraphicElement.PEN_NONE &&
				basePrintTextElement.getBottomBorder() != JRGraphicElement.PEN_NONE)
				);
		return basePrintTextElement;
	}

	/**
	 * 
	 * @param basePrintTextElement
	 * @param textElement
	 */
	private void measureTextElement (JRBasePrintText basePrintTextElement, JRTextElement textElement)
	{
		TextMeasurer textMeasurer = new TextMeasurer(textElement);
		textMeasurer.measure(
				getStyledText(textElement), 
				getStyledText(textElement).getText(),
				0,
				0
				);
		basePrintTextElement.setTextHeight(textMeasurer.getTextHeight() < basePrintTextElement.getHeight() ? textMeasurer.getTextHeight() : basePrintTextElement.getHeight());
		basePrintTextElement.setLeadingOffset(textMeasurer.getLeadingOffset());
		basePrintTextElement.setLineSpacingFactor(textMeasurer.getLineSpacingFactor());
		
	}
	
	/**
	 * 
	 * @param baseElement
	 * @param designElement
	 * @param bandHeight
	 */
	private void setBaseElement (JRBasePrintElement baseElement, JRElement designElement, int bandHeight, int parentX, int parentY, int parentWidth, int parentHeight, boolean isFrameChild)
	{
		baseElement.setBackcolor(designElement.getBackcolor());
		baseElement.setForecolor(designElement.getForecolor());
		baseElement.setKey(designElement.getKey());
		baseElement.setMode(designElement.getMode());
		baseElement.setStyle(designElement.getStyle());
		
		if(isFrameChild)
		{
			setBaseElementGeometry(baseElement, designElement, parentX, parentY, parentWidth, parentHeight);
		}
		else
		{
			setBaseElementGeometry(baseElement, designElement, offsetX, offsetY, pageWidth, bandHeight);
		}
	}
	
	private void setBaseElementGeometry (JRBasePrintElement baseElement, JRElement element,int parentX, int parentY, int parentWidth, int parentHeight)
	{
		baseElement.setX(element.getX() + parentX);
		baseElement.setY(element.getY() + parentY);

		baseElement.setWidth(element.getWidth());			
		// truncating boundary elements, if necessary
		if(element.getY() + element.getHeight() > parentHeight)
		{
			baseElement.setHeight(parentHeight - element.getY());
		}
		else
		{
			baseElement.setHeight(element.getHeight());
		}

		if(parentWidth > 0 && element.getX() + element.getWidth() > parentWidth)
		{
			baseElement.setWidth(parentWidth - element.getX());
		}
		else
		{
			baseElement.setWidth(element.getWidth());
		}

	}
	
	/**
	 * 
	 * @param baseElement
	 */
	private void addContourElement(JRBasePrintElement baseElement)
	{
		Color contourColor = baseElement.getForecolor() == null ? Color.black : baseElement.getForecolor();
		ContourElement contourElement = 
			new ContourElement(
				baseElement.getX(),
				baseElement.getY(),
				baseElement.getWidth(),
				baseElement.getHeight(),
				contourColor,
				JRGraphicElement.PEN_THIN
				);
		contourElementsList.add(contourElement);
	}
	
	/**
	 * The method picks up elements from within a crosstab and creates a list
	 * of frames containing all valid cells info; each cell is represented as a frame
	 * @param crosstab the crosstab element
	 * @return List the list of available design elements (frames)
	 */
	protected List getCrosstabList(JRCrosstab crosstab)
	{
		List crosstabElements = new ArrayList();
		
		JRCrosstabRowGroup[] rowGroups = crosstab.getRowGroups();
		int rowHeadersXOffset = 0;
		for (int i = 0; i < rowGroups.length; i++)
		{
			rowHeadersXOffset += rowGroups[i].getWidth();
		}
		
		JRCrosstabColumnGroup[] columnGroups = crosstab.getColumnGroups();
		int colHeadersYOffset = 0;
		for (int i = 0; i < columnGroups.length; i++)
		{
			colHeadersYOffset += columnGroups[i].getHeight();
		}
		
		JRCellContents headerCell = crosstab.getHeaderCell();
		if (headerCell != null)
		{
			if (headerCell.getWidth() != 0 && headerCell.getHeight() != 0)
			{
				crosstabElements.add(getCrosstabCellFrame(
						headerCell, 
						crosstab.getX(), 
						crosstab.getY(), 
						false, 
						false, 
						false
						));

			}
			
		}
		
		addCrosstabColumnHeaders(crosstab, rowHeadersXOffset, crosstabElements);
		addCrosstabRows(crosstab, rowHeadersXOffset, colHeadersYOffset, crosstabElements);
		
		if (crosstab.getRunDirection() == JRCrosstab.RUN_DIRECTION_RTL)
		{
			mirrorElements(crosstabElements, crosstab.getX(), crosstab.getWidth());
		}
		
		return crosstabElements;
	}

	protected void mirrorElements(List elements, int x, int width)
	{
		for (Iterator it = elements.iterator(); it.hasNext();)
		{
			JRElement element = (JRElement) it.next();
			int mirrorX = 2 * x + width - element.getX() - element.getWidth();
			element.setX(mirrorX);
		}
	}
	
	/**
	 * 
	 * @param cell
	 * @param x
	 * @param y
	 * @param left
	 * @param top
	 * @return
	 */
	protected JRFrame getCrosstabCellFrame(
			JRCellContents cell, 
			int x, 
			int y, 
			boolean left, 
			boolean right, 
			boolean top
			)
	{
		JRDesignFrame frame = new JRDesignFrame(cell.getDefaultStyleProvider());
		frame.setX(x);
		frame.setY(y);
		frame.setWidth(cell.getWidth());
		frame.setHeight(cell.getHeight());
		
		frame.setMode(cell.getMode());
		frame.setBackcolor(cell.getBackcolor());
		frame.setStyle(cell.getStyle());
		
		JRBox box = cell.getBox();
		if (box != null)
		{
			frame.setBox(box);
			
			boolean copyLeft = left && box.getLeftBorder() == JRGraphicElement.PEN_NONE && box.getRightBorder() != JRGraphicElement.PEN_NONE;
			boolean copyRight = right && box.getRightBorder() == JRGraphicElement.PEN_NONE && box.getLeftBorder() != JRGraphicElement.PEN_NONE;
			boolean copyTop = top && box.getTopBorder() == JRGraphicElement.PEN_NONE && box.getBottomBorder() != JRGraphicElement.PEN_NONE;
			
			if (copyLeft)
			{
				frame.setLeftBorder(box.getRightBorder());
				frame.setLeftBorderColor(box.getRightBorderColor());
			}
			
			if (copyRight)
			{
				frame.setRightBorder(box.getLeftBorder());
				frame.setRightBorderColor(box.getLeftBorderColor());
			}
			
			if (copyTop)
			{
				frame.setTopBorder(box.getBottomBorder());
				frame.setTopBorderColor(box.getBottomBorderColor());
			}
		}
		
		List children = cell.getChildren();
		if (children != null)
		{
			for (Iterator it = children.iterator(); it.hasNext();)
			{
				JRChild child = (JRChild) it.next();
				if (child instanceof JRElement)
				{
					frame.addElement((JRElement) child);
				}
				else if (child instanceof JRElementGroup)
				{
					frame.addElementGroup((JRElementGroup) child);
				}
			}
		}
		
		return frame;
	}
	
	/**
	 * 
	 * @param crosstab
	 * @param rowHeadersXOffset
	 * @param crosstabElements
	 */
	protected void addCrosstabColumnHeaders(JRCrosstab crosstab, int rowHeadersXOffset, List crosstabElements)
	{
		JRCrosstabColumnGroup[] groups = crosstab.getColumnGroups();
		int crosstabX = crosstab.getX();
		int crosstabY = crosstab.getY();
		
		for (int i = 0, x = 0, y = 0; i < groups.length; i++)
		{
			JRCrosstabColumnGroup group = groups[i];
			
			if (group.getTotalPosition() == BucketDefinition.TOTAL_POSITION_START)
			{
				JRCellContents totalHeader = group.getTotalHeader();
				if (totalHeader.getWidth() != 0 && totalHeader.getHeight() != 0)
				{
					boolean firstOnRow = x == 0 && crosstab.getHeaderCell() == null;
					crosstabElements.add(getCrosstabCellFrame(
							totalHeader, 
							crosstabX + rowHeadersXOffset + x, 
							crosstabY + y, 
							firstOnRow && crosstab.getRunDirection() == JRCrosstab.RUN_DIRECTION_LTR,
							firstOnRow && crosstab.getRunDirection() == JRCrosstab.RUN_DIRECTION_RTL,
							false
							));
	
					x += totalHeader.getWidth();
				}
			}
			
			JRCellContents header = group.getHeader();
			if (header.getWidth() != 0 && header.getHeight() != 0) {
				boolean firstOnRow = x == 0 && crosstab.getHeaderCell() == null;
				crosstabElements.add(getCrosstabCellFrame(
						header, 
						crosstabX + rowHeadersXOffset + x, 
						crosstabY + y, 
						firstOnRow && crosstab.getRunDirection() == JRCrosstab.RUN_DIRECTION_LTR,
						firstOnRow && crosstab.getRunDirection() == JRCrosstab.RUN_DIRECTION_RTL,
						false
						));
			}
			
			if (group.getTotalPosition() == BucketDefinition.TOTAL_POSITION_END)
			{
				JRCellContents totalHeader = group.getTotalHeader();
				if (totalHeader.getWidth() != 0 && totalHeader.getHeight() != 0)
					crosstabElements.add(getCrosstabCellFrame(
							totalHeader, 
							crosstabX + rowHeadersXOffset + x + header.getWidth(), 
							crosstabY + y, 
							false, 
							false, 
							false
							));
				
			}
			
			y += group.getHeight();
		}
	}

	/**
	 * 
	 * @param crosstab
	 * @param rowHeadersXOffset
	 * @param colHeadersYOffset
	 * @param crosstabElements
	 */
	protected void addCrosstabRows(JRCrosstab crosstab, int rowHeadersXOffset, int colHeadersYOffset, List crosstabElements)
	{
		JRCrosstabRowGroup[] groups = crosstab.getRowGroups();
		int crosstabX = crosstab.getX();
		int crosstabY = crosstab.getY();
		
		for (int i = 0, x = 0, y = 0; i < groups.length; i++)
		{
			JRCrosstabRowGroup group = groups[i];
			
			if (group.getTotalPosition() == BucketDefinition.TOTAL_POSITION_START)
			{
				JRCellContents totalHeader = group.getTotalHeader();
				if (totalHeader.getWidth() != 0 && totalHeader.getHeight() != 0)
				{
					crosstabElements.add(getCrosstabCellFrame(
							totalHeader, 
							crosstabX + x, 
							crosstabY + colHeadersYOffset + y, 
							false, 
							false, 
							y == 0 && crosstab.getHeaderCell() == null
							));
					
					addCrosstabDataCellsRow(crosstab, rowHeadersXOffset, colHeadersYOffset + y, i, crosstabElements);
					y += totalHeader.getHeight();
				}
			}
			
			JRCellContents header = group.getHeader();
			if (header.getWidth() != 0 && header.getHeight() != 0)
				crosstabElements.add(getCrosstabCellFrame(
						header, 
						crosstabX + x, 
						crosstabY + colHeadersYOffset + y, 
						false, 
						false, 
						y == 0 && crosstab.getHeaderCell() == null
						));
			
			if (i == groups.length - 1)
			{
				addCrosstabDataCellsRow(crosstab, rowHeadersXOffset, colHeadersYOffset + y, groups.length, crosstabElements);				
			}
			
			if (group.getTotalPosition() == BucketDefinition.TOTAL_POSITION_END)
			{
				JRCellContents totalHeader = group.getTotalHeader();
				if (totalHeader.getWidth() != 0 && totalHeader.getHeight() != 0)
				{
					crosstabElements.add(getCrosstabCellFrame(
							totalHeader, 
							crosstabX + x, 
							crosstabY + colHeadersYOffset + y + header.getHeight(), 
							false, 
							false, 
							false
							));
					
					addCrosstabDataCellsRow(crosstab, rowHeadersXOffset, colHeadersYOffset + y + header.getHeight(), i, crosstabElements);
				}
			}
			
			x += group.getWidth();
		}
	}

	/**
	 * 
	 * @param crosstab
	 * @param rowOffsetX
	 * @param rowOffsetY
	 * @param rowIndex
	 * @param crosstabElements
	 */
	protected void addCrosstabDataCellsRow(
			JRCrosstab crosstab, 
			int rowOffsetX, 
			int rowOffsetY, 
			int rowIndex,
			List crosstabElements
			)
	{
		
		JRCrosstabColumnGroup[] colGroups = crosstab.getColumnGroups();
		JRCrosstabCell[][] cells = crosstab.getCells();
		int crosstabX = crosstab.getX() + rowOffsetX;
		int crosstabY = crosstab.getY() + rowOffsetY;

		for (int i = 0, x = 0; i < colGroups.length; i++)
		{
			JRCrosstabColumnGroup group = colGroups[i];
			
			if (group.getTotalPosition() == BucketDefinition.TOTAL_POSITION_START)
			{
				JRCellContents cell = cells[rowIndex][i].getContents();
				if (cell.getWidth() != 0 && cell.getHeight() != 0)
				{
					crosstabElements.add(getCrosstabCellFrame(
							cell, 
							crosstabX + x, 
							crosstabY, 
							false, 
							false, 
							false
							));
					x += cells[rowIndex][i].getContents().getWidth();
				}
			}
			
			if (i == colGroups.length - 1)
			{
				JRCellContents cell = cells[rowIndex][colGroups.length].getContents();
				if (cell.getWidth() != 0 && cell.getHeight() != 0)
					crosstabElements.add(getCrosstabCellFrame(
							cell, 
							crosstabX + x, 
							crosstabY, 
							false, 
							false, 
							false
							));
			}
			
			if (group.getTotalPosition() == BucketDefinition.TOTAL_POSITION_END)
			{
				JRCellContents cell = cells[rowIndex][i].getContents();
				if (cell.getWidth() != 0 && cell.getHeight() != 0)
					crosstabElements.add(getCrosstabCellFrame(
							cell, 
							crosstabX + x + group.getHeader().getWidth(), 
							crosstabY, 
							false, 
							false, 
							false
							));
			}
		}
	}

	/**
	 * 
	 * @return
	 */	
	public JasperPrint getJasperPrint()
	{
		return this.jasperPrint;
	}
	
	/**
	 * 
	 * @param jasperPrint
	 */
	public void setJasperPrint (JasperPrint jasperPrint)
	{
		this.jasperPrint = jasperPrint;
	}
	
	/**
	 * 
	 * @param report
	 * @throws JRException
	 */
	public void setJasperPrint (JRReport report) throws JRException
	{
		createJasperPrint(report);
	}
	
	/**
	 * 
	 * @author sanda zaharia
	 *
	 */
	protected class ContourElement
	{
		private int contourX;
		private int contourY;
		private int contourWidth;
		private int contourHeight;
		private byte contourPen;
		private Color contourColor;
		
		public ContourElement()
		{
			
		}
		
		public ContourElement(int x, int y, int width, int height, Color color, byte pen)
		{
			this.contourX = x;
			this.contourY = y;
			this.contourWidth = width;
			this.contourHeight = height;
			this.contourColor = color;
			this.contourPen = pen;
		}
		
		/**
		 * @return the contourColor
		 */
		public Color getContourColor() {
			return contourColor;
		}
		/**
		 * @param contourColor the contourColor to set
		 */
		public void setContourColor(Color contourColor) {
			this.contourColor = contourColor;
		}
		/**
		 * @return the contourHeight
		 */
		public int getContourHeight() {
			return contourHeight;
		}
		/**
		 * @param contourHeight the contourHeight to set
		 */
		public void setContourHeight(int contourHeight) {
			this.contourHeight = contourHeight;
		}
		/**
		 * @return the contourPen
		 */
		public byte getContourPen() {
			return contourPen;
		}
		/**
		 * @param contourPen the contourPen to set
		 */
		public void setContourPen(byte contourPen) {
			this.contourPen = contourPen;
		}
		/**
		 * @return the contourWidth
		 */
		public int getContourWidth() {
			return contourWidth;
		}
		/**
		 * @param contourWidth the contourWidth to set
		 */
		public void setContourWidth(int contourWidth) {
			this.contourWidth = contourWidth;
		}
		/**
		 * @return the contourX
		 */
		public int getContourX() {
			return contourX;
		}
		/**
		 * @param contourX the contourX to set
		 */
		public void setContourX(int contourX) {
			this.contourX = contourX;
		}
		/**
		 * @return the contourY
		 */
		public int getContourY() {
			return contourY;
		}
		/**
		 * @param contourY the contourY to set
		 */
		public void setContourY(int contourY) {
			this.contourY = contourY;
		}
	}
}
