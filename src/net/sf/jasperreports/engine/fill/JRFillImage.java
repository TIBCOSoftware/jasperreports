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

import java.awt.Image;
import java.io.File;
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
	private boolean isValueRepeating = false;
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


	/**
	 *
	 */
	public byte getScaleImage()
	{
		return ((JRImage)this.parent).getScaleImage();
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
	public byte getHorizontalAlignment()
	{
		return ((JRImage)this.parent).getHorizontalAlignment();
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
	public byte getVerticalAlignment()
	{
		return ((JRImage)this.parent).getVerticalAlignment();
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
	public boolean isUsingCache()
	{
		return ((JRImage)this.parent).isUsingCache();
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
	 *
	 */
	public JRBox getBox()
	{
		return ((JRImage)parent).getBox();
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
			template = new JRTemplateImage((JRImage)this.parent);
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
		
		Object source = this.filler.calculator.evaluate(expression, evaluation);
		if (source != null)
		{
			Class expressionClass = expression.getValueClass();
			
			if (Image.class.getName().equals(expressionClass.getName()))
			{
				Image img = (Image)source;
				newRenderer = JRImageRenderer.getInstance(img, getOnErrorType());
			}
			else if (InputStream.class.getName().equals(expressionClass.getName()))
			{
				InputStream is = (InputStream)source;
				newRenderer = JRImageRenderer.getInstance(is, getOnErrorType());
			}
			else if (URL.class.getName().equals(expressionClass.getName()))
			{
				URL url = (URL)source;
				newRenderer = JRImageRenderer.getInstance(url, getOnErrorType());
			}
			else if (File.class.getName().equals(expressionClass.getName()))
			{
				File file = (File)source;
				newRenderer = JRImageRenderer.getInstance(file, getOnErrorType());
			}
			else if (String.class.getName().equals(expressionClass.getName()))
			{
				String location = (String)source;
				if (this.isUsingCache())
				{
					if ( this.filler.loadedImages.containsKey(location) )
					{
						newRenderer = ((JRPrintImage)this.filler.loadedImages.get(location)).getRenderer();
					}
					else
					{
						newRenderer = JRImageRenderer.getInstance(location, getOnErrorType(), isLazy());
						JRPrintImage img = new JRTemplatePrintImage(this.getJRTemplateImage());
						img.setRenderer(newRenderer);
						this.filler.loadedImages.put(location, img);
					}
				}
				else
				{
					newRenderer = JRImageRenderer.getInstance(location, getOnErrorType(), isLazy());
				}
			}
			else if (JRRenderable.class.getName().equals(expressionClass.getName()))
			{
				newRenderer = (JRRenderable)source;
			}
		}

		this.isValueRepeating = (this.renderer == newRenderer);
	
		this.renderer = newRenderer;
		
		this.anchorName = (String)this.filler.calculator.evaluate(this.getAnchorNameExpression(), evaluation);
		this.hyperlinkReference = (String)this.filler.calculator.evaluate(this.getHyperlinkReferenceExpression(), evaluation);
		this.hyperlinkAnchor = (String)this.filler.calculator.evaluate(this.getHyperlinkAnchorExpression(), evaluation);
		this.hyperlinkPage = (Integer)this.filler.calculator.evaluate(this.getHyperlinkPageExpression(), evaluation);
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
				this.isValueRepeating
				)
			{
				if (
					( !this.isPrintInFirstWholeBand() || !this.getBand().isNewPageColumn() ) &&
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
	public void writeXml(JRXmlWriter xmlWriter)
	{
		xmlWriter.writeImage(this);
	}


	protected void resolveElement(JRPrintElement element, byte evaluation) throws JRException
	{
		evaluateImage(evaluation);

		copy((JRPrintImage) element);
	}

}
