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
package net.sf.jasperreports.engine.fill;

import java.awt.Color;
import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Map;

import net.sf.jasperreports.engine.JRAbstractObjectFactory;
import net.sf.jasperreports.engine.JRBox;
import net.sf.jasperreports.engine.JRChild;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExpression;
import net.sf.jasperreports.engine.JRExpressionCollector;
import net.sf.jasperreports.engine.JRGroup;
import net.sf.jasperreports.engine.JRImage;
import net.sf.jasperreports.engine.JRImageRenderer;
import net.sf.jasperreports.engine.JRPrintElement;
import net.sf.jasperreports.engine.JRPrintImage;
import net.sf.jasperreports.engine.JRRenderable;
import net.sf.jasperreports.engine.xml.JRXmlWriter;
//import java.awt.image.*;


/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id$
 */
public class JRFillImage extends JRFillGraphicElement implements JRImage
{


	/**
	 *
	 */
	private JRGroup evaluationGroup = null;

	/**
	 *
	 */
	private JRRenderable renderer = null;
	private String anchorName = null;
	private String hyperlinkReference = null;
	private String hyperlinkAnchor = null;
	private Integer hyperlinkPage = null;


	/**
	 *
	 */
	protected JRFillImage(
		JRBaseFiller filler,
		JRImage image, 
		JRFillObjectFactory factory
		)
	{
		super(filler, image, factory);

		evaluationGroup = factory.getGroup(image.getEvaluationGroup());
	}


	protected JRFillImage(JRFillImage image, JRFillCloneFactory factory)
	{
		super(image, factory);

		evaluationGroup = image.evaluationGroup;
	}


	/**
	 * 
	 */
	public byte getScaleImage()
	{
		return ((JRImage)this.parent).getScaleImage();
	}
		
	public Byte getOwnScaleImage()
	{
		return ((JRImage)this.parent).getOwnScaleImage();
	}

	/**
	 *
	 */
	public void setScaleImage(byte scaleImage)
	{
	}

	/**
	 *
	 */
	public void setScaleImage(Byte scaleImage)
	{
	}

	/**
	 *
	 */
	public byte getHorizontalAlignment()
	{
		return ((JRImage)this.parent).getHorizontalAlignment();
	}
		
	public Byte getOwnHorizontalAlignment()
	{
		return ((JRImage)this.parent).getOwnHorizontalAlignment();
	}

	/**
	 *
	 */
	public void setHorizontalAlignment(byte horizontalAlignment)
	{
	}
		
	/**
	 *
	 */
	public void setHorizontalAlignment(Byte horizontalAlignment)
	{
	}
		
	/**
	 *
	 */
	public byte getVerticalAlignment()
	{
		return ((JRImage)this.parent).getVerticalAlignment();
	}
		
	public Byte getOwnVerticalAlignment()
	{
		return ((JRImage)this.parent).getOwnVerticalAlignment();
	}

	/**
	 *
	 */
	public void setVerticalAlignment(byte verticalAlignment)
	{
	}
		
	/**
	 *
	 */
	public void setVerticalAlignment(Byte verticalAlignment)
	{
	}
		
	/**
	 *
	 */
	public boolean isUsingCache()
	{
		return ((JRImage)this.parent).isUsingCache();
	}
		
	/**
	 *
	 */
	public Boolean isOwnUsingCache()
	{
		return ((JRImage)this.parent).isOwnUsingCache();
	}
		
	/**
	 *
	 */
	public void setUsingCache(boolean isUsingCache)
	{
	}
		
	/**
	 *
	 */
	public void setUsingCache(Boolean isUsingCache)
	{
	}
		
	/**
	 *
	 */
	public boolean isLazy()
	{
		return ((JRImage)this.parent).isLazy();
	}
		
	/**
	 *
	 */
	public void setLazy(boolean isLazy)
	{
	}

	/**
	 *
	 */
	public byte getOnErrorType()
	{
		return ((JRImage)this.parent).getOnErrorType();
	}
		
	/**
	 *
	 */
	public void setOnErrorType(byte onErrorType)
	{
	}

	/**
	 *
	 */
	public byte getEvaluationTime()
	{
		return ((JRImage)this.parent).getEvaluationTime();
	}
		
	/**
	 *
	 */
	public JRGroup getEvaluationGroup()
	{
		return this.evaluationGroup;
	}
		
	/**
	 * @deprecated
	 */
	public JRBox getBox()
	{
		return this;
	}

	/**
	 *
	 */
	public byte getHyperlinkType()
	{
		return ((JRImage)this.parent).getHyperlinkType();
	}
		
	/**
	 *
	 */
	public byte getHyperlinkTarget()
	{
		return ((JRImage)this.parent).getHyperlinkTarget();
	}
		
	/**
	 *
	 */
	public JRExpression getExpression()
	{
		return ((JRImage)this.parent).getExpression();
	}

	/**
	 *
	 */
	public JRExpression getAnchorNameExpression()
	{
		return ((JRImage)this.parent).getAnchorNameExpression();
	}

	/**
	 *
	 */
	public JRExpression getHyperlinkReferenceExpression()
	{
		return ((JRImage)this.parent).getHyperlinkReferenceExpression();
	}

	/**
	 *
	 */
	public JRExpression getHyperlinkAnchorExpression()
	{
		return ((JRImage)this.parent).getHyperlinkAnchorExpression();
	}

	/**
	 *
	 */
	public JRExpression getHyperlinkPageExpression()
	{
		return ((JRImage)this.parent).getHyperlinkPageExpression();
	}

		
	/**
	 *
	 */
	protected JRRenderable getRenderer()
	{
		return this.renderer;
	}
		
	/**
	 *
	 */
	protected String getAnchorName()
	{
		return this.anchorName;
	}

	/**
	 *
	 */
	protected String getHyperlinkReference()
	{
		return this.hyperlinkReference;
	}

	/**
	 *
	 */
	protected String getHyperlinkAnchor()
	{
		return this.hyperlinkAnchor;
	}

	/**
	 *
	 */
	protected Integer getHyperlinkPage()
	{
		return this.hyperlinkPage;
	}
		

	/**
	 *
	 */
	protected JRTemplateImage getJRTemplateImage()
	{
		if (template == null)
		{
			template = new JRTemplateImage(filler.getJasperPrint().getDefaultStyleProvider(), (JRImage)this.parent);
		}
		
		return (JRTemplateImage)template;
	}


	/**
	 *
	 */
	protected void evaluate(
		byte evaluation
		) throws JRException
	{
		this.reset();
		
		this.evaluatePrintWhenExpression(evaluation);

		if (
			(this.isPrintWhenExpressionNull() ||
			(!this.isPrintWhenExpressionNull() && 
			this.isPrintWhenTrue()))
			)
		{
			if (this.getEvaluationTime() == JRExpression.EVALUATION_TIME_NOW)
			{
				this.evaluateImage(evaluation);
			}
		}
	}
	

	/**
	 *
	 */
	protected void evaluateImage(
		byte evaluation
		) throws JRException
	{
		JRExpression expression = this.getExpression();

		JRRenderable newRenderer = null;
		
		Object source = evaluateExpression(expression, evaluation);
		if (source != null)
		{
			if (this.isUsingCache() && this.filler.fillContext.hasLoadedImage(source))
			{
				newRenderer = this.filler.fillContext.getLoadedImage(source).getRenderer();
			}
			else
			{
				Class expressionClass = expression.getValueClass();
				
				if (Image.class.getName().equals(expressionClass.getName()))
				{
					Image img = (Image) source;
					newRenderer = JRImageRenderer.getInstance(img, getOnErrorType());
				}
				else if (InputStream.class.getName().equals(expressionClass.getName()))
				{
					InputStream is = (InputStream) source;
					newRenderer = JRImageRenderer.getInstance(is, getOnErrorType());
				}
				else if (URL.class.getName().equals(expressionClass.getName()))
				{
					URL url = (URL) source;
					newRenderer = JRImageRenderer.getInstance(url, getOnErrorType());
				}
				else if (File.class.getName().equals(expressionClass.getName()))
				{
					File file = (File) source;
					newRenderer = JRImageRenderer.getInstance(file, getOnErrorType());
				}
				else if (String.class.getName().equals(expressionClass.getName()))
				{
					String location = (String) source;
					newRenderer = JRImageRenderer.getInstance(location, getOnErrorType(), isLazy(), filler.reportClassLoader);
				}
				else if (JRRenderable.class.getName().equals(expressionClass.getName()))
				{
					newRenderer = (JRRenderable) source;
				}

				if (this.isUsingCache())
				{
					JRPrintImage img = new JRTemplatePrintImage(this.getJRTemplateImage());
					img.setRenderer(newRenderer);
					this.filler.fillContext.registerLoadedImage(source, img);
				}
			}
		}

		setValueRepeating(this.renderer == newRenderer);
	
		this.renderer = newRenderer;
		
		this.anchorName = (String) evaluateExpression(this.getAnchorNameExpression(), evaluation);
		this.hyperlinkReference = (String) evaluateExpression(this.getHyperlinkReferenceExpression(), evaluation);
		this.hyperlinkAnchor = (String) evaluateExpression(this.getHyperlinkAnchorExpression(), evaluation);
		this.hyperlinkPage = (Integer) evaluateExpression(this.getHyperlinkPageExpression(), evaluation);
	}
	

	/**
	 *
	 */
	protected boolean prepare(
		int availableStretchHeight,
		boolean isOverflow
		)
	{
		boolean willOverflow = false;

		if (
			this.isPrintWhenExpressionNull() ||
			( !this.isPrintWhenExpressionNull() && 
			this.isPrintWhenTrue() )
			)
		{
			this.setToPrint(true);
		}
		else
		{
			this.setToPrint(false);
		}

		if (!this.isToPrint())
		{
			return willOverflow;
		}
		
		boolean isToPrint = true;
		boolean isReprinted = false;

		if (this.getEvaluationTime() == JRExpression.EVALUATION_TIME_NOW)
		{
			if (isOverflow && this.isAlreadyPrinted() && !this.isPrintWhenDetailOverflows())
			{
				isToPrint = false;
			}
	
			if (
				isToPrint && 
				this.isPrintWhenExpressionNull() &&
				!this.isPrintRepeatedValues() &&
				isValueRepeating()
				)
			{
				if (
					( !this.isPrintInFirstWholeBand() || !this.getBand().isFirstWholeOnPageColumn() ) &&
					( this.getPrintWhenGroupChanges() == null || !this.getBand().isNewGroup(this.getPrintWhenGroupChanges()) ) &&
					( !isOverflow || !this.isPrintWhenDetailOverflows() )
					)
				{
					isToPrint = false;
				}
			}
	
			if (
				isToPrint && 
				availableStretchHeight < this.getRelativeY() - this.getY() - this.getBandBottomY()
				)
			{
				isToPrint = false;
				willOverflow = true;
			}
			
			if (
				isToPrint && 
				isOverflow && 
				//(this.isAlreadyPrinted() || !this.isPrintRepeatedValues())
				(this.isPrintWhenDetailOverflows() && (this.isAlreadyPrinted() || (!this.isAlreadyPrinted() && !this.isPrintRepeatedValues())))
				)
			{
				isReprinted = true;
			}

			if (
				isToPrint && 
				this.isRemoveLineWhenBlank() &&
				this.getRenderer() == null
				)
			{
				isToPrint = false;
			}
		}
		else
		{
			if (isOverflow && this.isAlreadyPrinted() && !this.isPrintWhenDetailOverflows())
			{
				isToPrint = false;
			}
	
			if (
				isToPrint && 
				availableStretchHeight < this.getRelativeY() - this.getY() - this.getBandBottomY()
				)
			{
				isToPrint = false;
				willOverflow = true;
			}
			
			if (
				isToPrint && 
				isOverflow && 
				//(this.isAlreadyPrinted() || !this.isPrintRepeatedValues())
				(this.isPrintWhenDetailOverflows() && (this.isAlreadyPrinted() || (!this.isAlreadyPrinted() && !this.isPrintRepeatedValues())))
				)
			{
				isReprinted = true;
			}
		}

		this.setToPrint(isToPrint);
		this.setReprinted(isReprinted);
		
		return willOverflow;
	}


	/**
	 *
	 */
	protected JRPrintElement fill()
	{
		JRPrintImage printImage = null;
		
		printImage = new JRTemplatePrintImage(this.getJRTemplateImage());
		
		printImage.setX(this.getX());
		printImage.setY(this.getRelativeY());
		printImage.setWidth(getWidth());
		printImage.setHeight(this.getStretchHeight());

		switch (this.getEvaluationTime())
		{
			case JRExpression.EVALUATION_TIME_REPORT :
			{
				this.filler.reportBoundElements.put(printImage, this);
				break;
			}
			case JRExpression.EVALUATION_TIME_PAGE :
			{
				this.filler.pageBoundElements.put(printImage, this);
				break;
			}
			case JRExpression.EVALUATION_TIME_COLUMN :
			{
				this.filler.columnBoundElements.put(printImage, this);
				break;
			}
			case JRExpression.EVALUATION_TIME_GROUP :
			{
				Map specificGroupBoundImages = (Map)this.filler.groupBoundElements.get(this.getEvaluationGroup().getName());
				specificGroupBoundImages.put(printImage, this);
				break;
			}
			case JRExpression.EVALUATION_TIME_BAND :
			{
				this.band.boundElements.put(printImage, this);
				break;
			}
			case JRExpression.EVALUATION_TIME_NOW :
			default :
			{
				this.copy(printImage);
			}
		}
		
		return printImage;
	}
		

	/**
	 *
	 */
	protected void copy(JRPrintImage printImage)
	{
		printImage.setRenderer(
			this.getRenderer()
			);
		printImage.setAnchorName(this.getAnchorName());
		printImage.setHyperlinkReference(this.getHyperlinkReference());
		printImage.setHyperlinkAnchor(this.getHyperlinkAnchor());
		printImage.setHyperlinkPage(this.getHyperlinkPage());
		printImage.setBookmarkLevel(this.getBookmarkLevel());
	}


	/**
	 *
	 */
	public JRChild getCopy(JRAbstractObjectFactory factory)
	{
		return factory.getImage(this);
	}

	/**
	 *
	 */
	public void collectExpressions(JRExpressionCollector collector)
	{
		collector.collect(this);
	}

	/**
	 *
	 */
	public void writeXml(JRXmlWriter xmlWriter) throws IOException
	{
		xmlWriter.writeImage(this);
	}


	protected void resolveElement(JRPrintElement element, byte evaluation) throws JRException
	{
		evaluateImage(evaluation);

		copy((JRPrintImage) element);
	}


	public int getBookmarkLevel()
	{
		return ((JRImage)this.parent).getBookmarkLevel();
	}

	/**
	 *
	 */
	public byte getBorder()
	{
		return ((JRBox)parent).getBorder();
	}

	public Byte getOwnBorder()
	{
		return ((JRBox)parent).getOwnBorder();
	}

	/**
	 *
	 */
	public void setBorder(byte border)
	{
	}

	/**
	 *
	 */
	public Color getBorderColor()
	{
		return ((JRBox)parent).getBorderColor();
	}

	public Color getOwnBorderColor()
	{
		return ((JRBox)parent).getOwnBorderColor();
	}

	/**
	 *
	 */
	public void setBorderColor(Color borderColor)
	{
	}

	/**
	 *
	 */
	public int getPadding()
	{
		return ((JRBox)parent).getPadding();
	}

	public Integer getOwnPadding()
	{
		return ((JRBox)parent).getOwnPadding();
	}

	/**
	 *
	 */
	public void setPadding(int padding)
	{
	}

	/**
	 *
	 */
	public byte getTopBorder()
	{
		return ((JRBox)parent).getTopBorder();
	}

	/**
	 *
	 */
	public Byte getOwnTopBorder()
	{
		return ((JRBox)parent).getOwnTopBorder();
	}

	/**
	 *
	 */
	public void setTopBorder(byte topBorder)
	{
	}

	/**
	 *
	 */
	public Color getTopBorderColor()
	{
		return ((JRBox)parent).getTopBorderColor();
	}

	/**
	 *
	 */
	public Color getOwnTopBorderColor()
	{
		return ((JRBox)parent).getOwnTopBorderColor();
	}

	/**
	 *
	 */
	public void setTopBorderColor(Color topBorderColor)
	{
	}

	/**
	 *
	 */
	public int getTopPadding()
	{
		return ((JRBox)parent).getTopPadding();
	}

	/**
	 *
	 */
	public Integer getOwnTopPadding()
	{
		return ((JRBox)parent).getOwnTopPadding();
	}

	/**
	 *
	 */
	public void setTopPadding(int topPadding)
	{
	}

	/**
	 *
	 */
	public byte getLeftBorder()
	{
		return ((JRBox)parent).getLeftBorder();
	}

	/**
	 *
	 */
	public Byte getOwnLeftBorder()
	{
		return ((JRBox)parent).getOwnLeftBorder();
	}

	/**
	 *
	 */
	public void setLeftBorder(byte leftBorder)
	{
	}

	/**
	 *
	 */
	public Color getLeftBorderColor()
	{
		return ((JRBox)parent).getLeftBorderColor();
	}

	/**
	 *
	 */
	public Color getOwnLeftBorderColor()
	{
		return ((JRBox)parent).getOwnLeftBorderColor();
	}

	/**
	 *
	 */
	public void setLeftBorderColor(Color leftBorderColor)
	{
	}

	/**
	 *
	 */
	public int getLeftPadding()
	{
		return ((JRBox)parent).getLeftPadding();
	}

	/**
	 *
	 */
	public Integer getOwnLeftPadding()
	{
		return ((JRBox)parent).getOwnLeftPadding();
	}

	/**
	 *
	 */
	public void setLeftPadding(int leftPadding)
	{
	}

	/**
	 *
	 */
	public byte getBottomBorder()
	{
		return ((JRBox)parent).getBottomBorder();
	}

	/**
	 *
	 */
	public Byte getOwnBottomBorder()
	{
		return ((JRBox)parent).getOwnBottomBorder();
	}

	/**
	 *
	 */
	public void setBottomBorder(byte bottomBorder)
	{
	}

	/**
	 *
	 */
	public Color getBottomBorderColor()
	{
		return ((JRBox)parent).getBottomBorderColor();
	}

	/**
	 *
	 */
	public Color getOwnBottomBorderColor()
	{
		return ((JRBox)parent).getOwnBottomBorderColor();
	}

	/**
	 *
	 */
	public void setBottomBorderColor(Color bottomBorderColor)
	{
	}

	/**
	 *
	 */
	public int getBottomPadding()
	{
		return ((JRBox)parent).getBottomPadding();
	}

	/**
	 *
	 */
	public Integer getOwnBottomPadding()
	{
		return ((JRBox)parent).getOwnBottomPadding();
	}

	/**
	 *
	 */
	public void setBottomPadding(int bottomPadding)
	{
	}

	/**
	 *
	 */
	public byte getRightBorder()
	{
		return ((JRBox)parent).getRightBorder();
	}

	/**
	 *
	 */
	public Byte getOwnRightBorder()
	{
		return ((JRBox)parent).getOwnRightBorder();
	}

	/**
	 *
	 */
	public void setRightBorder(byte rightBorder)
	{
	}

	/**
	 *
	 */
	public Color getRightBorderColor()
	{
		return ((JRBox)parent).getRightBorderColor();
	}

	/**
	 *
	 */
	public Color getOwnRightBorderColor()
	{
		return ((JRBox)parent).getOwnRightBorderColor();
	}

	/**
	 *
	 */
	public void setRightBorderColor(Color rightBorderColor)
	{
	}

	/**
	 *
	 */
	public int getRightPadding()
	{
		return ((JRBox)parent).getRightPadding();
	}

	/**
	 *
	 */
	public Integer getOwnRightPadding()
	{
		return ((JRBox)parent).getOwnRightPadding();
	}

	/**
	 *
	 */
	public void setRightPadding(int rightPadding)
	{
	}
	
	/**
	 *
	 */
	public void setBorder(Byte border)
	{
	}

	/**
	 *
	 */
	public void setPadding(Integer padding)
	{
	}

	/**
	 *
	 */
	public void setTopBorder(Byte topBorder)
	{
	}

	/**
	 *
	 */
	public void setTopPadding(Integer topPadding)
	{
	}

	/**
	 *
	 */
	public void setLeftBorder(Byte leftBorder)
	{
	}

	/**
	 *
	 */
	public void setLeftPadding(Integer leftPadding)
	{
	}

	/**
	 *
	 */
	public void setBottomBorder(Byte bottomBorder)
	{
	}

	/**
	 *
	 */
	public void setBottomPadding(Integer bottomPadding)
	{
	}

	/**
	 *
	 */
	public void setRightBorder(Byte rightBorder)
	{
	}

	/**
	 *
	 */
	public void setRightPadding(Integer rightPadding)
	{
	}


	public JRCloneable createClone(JRFillCloneFactory factory)
	{
		return new JRFillImage(this, factory);
	}

}
