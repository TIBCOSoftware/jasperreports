/*
 * ============================================================================
 *                   The JasperReports License, Version 1.0
 * ============================================================================
 * 
 * Copyright (C) 2001-2004 Teodor Danciu (teodord@users.sourceforge.net). All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without modification,
 * are permitted provided that the following conditions are met:
 * 
 * 1. Redistributions of source code must retain the above copyright notice,
 *    this list of conditions and the following disclaimer.
 * 
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 * 
 * 3. The end-user documentation included with the redistribution, if any, must
 *    include the following acknowledgment: "This product includes software
 *    developed by Teodor Danciu (http://jasperreports.sourceforge.net)."
 *    Alternately, this acknowledgment may appear in the software itself, if
 *    and wherever such third-party acknowledgments normally appear.
 * 
 * 4. The name "JasperReports" must not be used to endorse or promote products 
 *    derived from this software without prior written permission. For written 
 *    permission, please contact teodord@users.sourceforge.net.
 * 
 * 5. Products derived from this software may not be called "JasperReports", nor 
 *    may "JasperReports" appear in their name, without prior written permission
 *    of Teodor Danciu.
 * 
 * THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED WARRANTIES,
 * INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
 * FITNESS  FOR A PARTICULAR  PURPOSE ARE  DISCLAIMED.  IN NO  EVENT SHALL  THE
 * APACHE SOFTWARE  FOUNDATION  OR ITS CONTRIBUTORS  BE LIABLE FOR  ANY DIRECT,
 * INDIRECT, INCIDENTAL, SPECIAL,  EXEMPLARY, OR CONSEQUENTIAL  DAMAGES (INCLU-
 * DING, BUT NOT LIMITED TO, PROCUREMENT  OF SUBSTITUTE GOODS OR SERVICES; LOSS
 * OF USE, DATA, OR  PROFITS; OR BUSINESS  INTERRUPTION)  HOWEVER CAUSED AND ON
 * ANY  THEORY OF LIABILITY,  WHETHER  IN CONTRACT,  STRICT LIABILITY,  OR TORT
 * (INCLUDING  NEGLIGENCE OR  OTHERWISE) ARISING IN  ANY WAY OUT OF THE  USE OF
 * THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

/*
 * ============================================================================
 *                   GNU Lesser General Public License
 * ============================================================================
 *
 * JasperReports - Free Java report-generating library.
 * Copyright (C) 2001-2004 Teodor Danciu teodord@users.sourceforge.net
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
 * Teodor Danciu
 * 173, Calea Calarasilor, Bl. 42, Sc. 1, Ap. 18
 * Postal code 030615, Sector 3
 * Bucharest, ROMANIA
 * Email: teodord@users.sourceforge.net
 */
package dori.jasper.engine.fill;

import java.awt.Image;
import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.util.Map;

import dori.jasper.engine.JRException;
import dori.jasper.engine.JRExpression;
import dori.jasper.engine.JRGroup;
import dori.jasper.engine.JRImage;
import dori.jasper.engine.JRPrintElement;
import dori.jasper.engine.JRPrintImage;
import dori.jasper.engine.util.JRImageLoader;
//import java.awt.image.*;


/**
 *
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
	private byte[] imageData = null;
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
		Map fillObjectsMap
		)
	{
		super(filler, image, fillObjectsMap);

		evaluationGroup = (JRGroup)JRFillObjectFactory.getGroup(filler, image.getEvaluationGroup(), fillObjectsMap);
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
	public byte getHyperlinkType()
	{
		return ((JRImage)this.parent).getHyperlinkType();
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
	protected byte[] getImageData()
	{
		return this.imageData;
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

		byte[] newImageData = null;
		
		Object source = this.filler.calculator.evaluate(expression, evaluation);
		if (source != null)
		{
			Class expressionClass = expression.getValueClass();
			
			if (expressionClass.equals(java.awt.Image.class))
			{
				Image img = (Image)source;
				newImageData = JRImageLoader.loadImageDataFromAWTImage(img);
			}
			else if (expressionClass.equals(java.io.InputStream.class))
			{
				InputStream is = (InputStream)source;
				newImageData = JRImageLoader.loadImageDataFromInputStream(is);
			}
			else if (expressionClass.equals(java.net.URL.class))
			{
				URL url = (URL)source;
				newImageData = JRImageLoader.loadImageDataFromURL(url);
			}
			else if (expressionClass.equals(java.io.File.class))
			{
				File file = (File)source;
				newImageData = JRImageLoader.loadImageDataFromFile(file);
			}
			else if (expressionClass.equals(java.lang.String.class))
			{
				String location = (String)source;
				if (this.isUsingCache())
				{
					if ( this.filler.loadedImages.containsKey(location) )
					{
						newImageData = ((JRPrintImage)this.filler.loadedImages.get(location)).getImageData();
					}
					else
					{
						newImageData = JRImageLoader.loadImageDataFromLocation(location);
						JRPrintImage img = new JRTemplatePrintImage(this.getJRTemplateImage());
						img.setImageData(
							newImageData
							//this.getImageData()
							);
						this.filler.loadedImages.put(location, img);
					}
				}
				else
				{
					newImageData = JRImageLoader.loadImageDataFromLocation(location);
				}
			}
		}

		this.isValueRepeating = (this.imageData == newImageData);
	
		this.imageData = newImageData;
		
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
		) throws JRException
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
				(this.getImageData() == null || this.getImageData().length == 0)
				)
			{
				isToPrint = false;
			}
		}

		this.setToPrint(isToPrint);
		this.setReprinted(isReprinted);
		
		return willOverflow;
	}


	/**
	 *
	 */
	protected JRPrintElement fill() throws JRException
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
				this.filler.reportBoundImages.put(printImage, this);
				break;
			}
			case JRExpression.EVALUATION_TIME_PAGE :
			{
				this.filler.pageBoundImages.put(printImage, this);
				break;
			}
			case JRExpression.EVALUATION_TIME_COLUMN :
			{
				this.filler.columnBoundImages.put(printImage, this);
				break;
			}
			case JRExpression.EVALUATION_TIME_GROUP :
			{
				Map specificGroupBoundImages = (Map)this.filler.groupBoundImages.get(this.getEvaluationGroup().getName());
				specificGroupBoundImages.put(printImage, this);
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
	protected void copy(JRPrintImage printImage) throws JRException
	{
		printImage.setImageData(
			this.getImageData()
			);
		printImage.setAnchorName(this.getAnchorName());
		printImage.setHyperlinkReference(this.getHyperlinkReference());
		printImage.setHyperlinkAnchor(this.getHyperlinkAnchor());
		printImage.setHyperlinkPage(this.getHyperlinkPage());
	}
		

}
