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
import java.util.Map;

import net.sf.jasperreports.engine.JRBox;
import net.sf.jasperreports.engine.JRChart;
import net.sf.jasperreports.engine.JRChartDataset;
import net.sf.jasperreports.engine.JRChartPlot;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExpression;
import net.sf.jasperreports.engine.JRFont;
import net.sf.jasperreports.engine.JRGroup;
import net.sf.jasperreports.engine.JRPrintElement;
import net.sf.jasperreports.engine.JRPrintImage;
import net.sf.jasperreports.engine.JRRenderable;


/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id$
 */
public abstract class JRFillChart extends JRFillElement implements JRChart
{


	/**
	 *
	 */
	protected JRFont titleFont = null;
	protected JRFont subtitleFont = null;

	/**
	 *
	 */
	protected JRGroup evaluationGroup = null;

	protected JRChartDataset dataset = null;
	protected JRChartPlot plot = null;

	protected JRRenderable renderer = null;
	private String anchorName = null;
	private String hyperlinkReference = null;
	private String hyperlinkAnchor = null;
	private Integer hyperlinkPage = null;
	

	/**
	 *
	 */
	protected JRFillChart(
		JRBaseFiller filler,
		JRChart chart, 
		JRFillObjectFactory factory
		)
	{
		super(filler, chart, factory);

		/*   */
		titleFont = factory.getFont(chart.getTitleFont());
		subtitleFont = factory.getFont(chart.getSubtitleFont());

		evaluationGroup = (JRGroup)factory.getGroup(chart.getEvaluationGroup());
	}


	/**
	 *
	 */
	public boolean isShowLegend()
	{
		return ((JRChart)parent).isShowLegend();
	}
		
	/**
	 *
	 */
	public void setShowLegend(boolean isShowLegend)
	{
	}
		
	/**
	 *
	 */
	public byte getEvaluationTime()
	{
		return ((JRChart)parent).getEvaluationTime();
	}
		
	/**
	 *
	 */
	public JRGroup getEvaluationGroup()
	{
		return evaluationGroup;
	}
		
	/**
	 *
	 */
	public JRBox getBox()
	{
		return ((JRChart)parent).getBox();
	}

	/**
	 *
	 */
	public JRFont getTitleFont()
	{
		return titleFont;
	}

	/**
	 *
	 */
	public byte getTitlePosition()
	{
		return ((JRChart)parent).getTitlePosition();
	}

	/**
	 *
	 */
	public void setTitlePosition(byte titlePosition)
	{
	}

	/**
	 *
	 */
	public Color getTitleColor()
	{
		return ((JRChart)parent).getTitleColor();
	}

	/**
	 *
	 */
	public void setTitleColor(Color titleColor)
	{
	}

	/**
	 *
	 */
	public JRFont getSubtitleFont()
	{
		return subtitleFont;
	}

	/**
	 *
	 */
	public Color getSubtitleColor()
	{
		return ((JRChart)parent).getSubtitleColor();
	}

	/**
	 *
	 */
	public void setSubtitleColor(Color subtitleColor)
	{
	}

	/**
	 *
	 */
	public JRExpression getTitleExpression()
	{
		return ((JRChart)parent).getTitleExpression();
	}

	/**
	 *
	 */
	public JRExpression getSubtitleExpression()
	{
		return ((JRChart)parent).getSubtitleExpression();
	}

	/**
	 *
	 */
	public byte getHyperlinkType()
	{
		return ((JRChart)parent).getHyperlinkType();
	}
		
	/**
	 *
	 */
	public byte getHyperlinkTarget()
	{
		return ((JRChart)parent).getHyperlinkTarget();
	}
		
	/**
	 *
	 */
	public JRExpression getAnchorNameExpression()
	{
		return ((JRChart)parent).getAnchorNameExpression();
	}

	/**
	 *
	 */
	public JRExpression getHyperlinkReferenceExpression()
	{
		return ((JRChart)parent).getHyperlinkReferenceExpression();
	}

	/**
	 *
	 */
	public JRExpression getHyperlinkAnchorExpression()
	{
		return ((JRChart)parent).getHyperlinkAnchorExpression();
	}

	/**
	 *
	 */
	public JRExpression getHyperlinkPageExpression()
	{
		return ((JRChart)parent).getHyperlinkPageExpression();
	}

	
	/**
	 *
	 */
	public JRChartDataset getDataset()
	{
		return dataset;
	}

	/**
	 *
	 */
	public JRChartPlot getPlot()
	{
		return plot;
	}

	/**
	 *
	 */
	protected JRRenderable getRenderer()
	{
		return renderer;
	}
		
	/**
	 *
	 */
	protected String getAnchorName()
	{
		return anchorName;
	}

	/**
	 *
	 */
	protected String getHyperlinkReference()
	{
		return hyperlinkReference;
	}

	/**
	 *
	 */
	protected String getHyperlinkAnchor()
	{
		return hyperlinkAnchor;
	}

	/**
	 *
	 */
	protected Integer getHyperlinkPage()
	{
		return hyperlinkPage;
	}
		

	/**
	 *
	 */
	protected JRTemplateImage getJRTemplateImage()
	{
		if (template == null)
		{
			template = new JRTemplateImage((JRChart)parent);
		}
		
		return (JRTemplateImage)template;
	}


	/**
	 *
	 */
	protected Object evaluateExpression(JRExpression expression, byte evaluation) throws JRException
	{
		return filler.calculator.evaluate(expression, evaluation);
	}


	/**
	 *
	 */
	protected void rewind() throws JRException
	{
	}


	/**
	 *
	 */
	protected void evaluate(byte evaluation) throws JRException
	{
		reset();
		
		evaluatePrintWhenExpression(evaluation);

		if (
			(isPrintWhenExpressionNull() ||
			(!isPrintWhenExpressionNull() && 
			isPrintWhenTrue()))
			)
		{
			if (getEvaluationTime() == JRExpression.EVALUATION_TIME_NOW)
			{
				evaluateImage(evaluation);
			}
		}
	}


	/**
	 *
	 */
	protected void evaluateImage(byte evaluation) throws JRException
	{
		anchorName = (String)filler.calculator.evaluate(getAnchorNameExpression(), evaluation);
		hyperlinkReference = (String)filler.calculator.evaluate(getHyperlinkReferenceExpression(), evaluation);
		hyperlinkAnchor = (String)filler.calculator.evaluate(getHyperlinkAnchorExpression(), evaluation);
		hyperlinkPage = (Integer)filler.calculator.evaluate(getHyperlinkPageExpression(), evaluation);
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
			isPrintWhenExpressionNull() ||
			( !isPrintWhenExpressionNull() && 
			isPrintWhenTrue() )
			)
		{
			setToPrint(true);
		}
		else
		{
			setToPrint(false);
		}

		if (!isToPrint())
		{
			return willOverflow;
		}
		
		boolean isToPrint = true;
		boolean isReprinted = false;

		if (getEvaluationTime() == JRExpression.EVALUATION_TIME_NOW)
		{
			if (isOverflow && isAlreadyPrinted() && !isPrintWhenDetailOverflows())
			{
				isToPrint = false;
			}
	
			if (
				isToPrint && 
				availableStretchHeight < getRelativeY() - getY() - getBandBottomY()
				)
			{
				isToPrint = false;
				willOverflow = true;
			}
			
			if (
				isToPrint && 
				isOverflow && 
				//(this.isAlreadyPrinted() || !this.isPrintRepeatedValues())
				(isPrintWhenDetailOverflows() && (isAlreadyPrinted() || (!isAlreadyPrinted() && !isPrintRepeatedValues())))
				)
			{
				isReprinted = true;
			}

			if (
				isToPrint && 
				isRemoveLineWhenBlank() &&
				getRenderer() == null
				)
			{
				isToPrint = false;
			}
		}
		else
		{
			if (isOverflow && isAlreadyPrinted() && !isPrintWhenDetailOverflows())
			{
				isToPrint = false;
			}
	
			if (
				isToPrint && 
				availableStretchHeight < getRelativeY() - getY() - getBandBottomY()
				)
			{
				isToPrint = false;
				willOverflow = true;
			}
			
			if (
				isToPrint && 
				isOverflow && 
				//(this.isAlreadyPrinted() || !this.isPrintRepeatedValues())
				(isPrintWhenDetailOverflows() && (isAlreadyPrinted() || (!isAlreadyPrinted() && !isPrintRepeatedValues())))
				)
			{
				isReprinted = true;
			}
		}

		setToPrint(isToPrint);
		setReprinted(isReprinted);
		
		return willOverflow;
	}


	/**
	 *
	 */
	protected JRPrintElement fill() throws JRException
	{
		JRPrintImage printImage = new JRTemplatePrintImage(getJRTemplateImage());
		
		printImage.setX(getX());
		printImage.setY(getRelativeY());
		printImage.setHeight(getStretchHeight());

		switch (getEvaluationTime())
		{
			case JRExpression.EVALUATION_TIME_REPORT :
			{
				filler.reportBoundCharts.put(printImage, this);
				break;
			}
			case JRExpression.EVALUATION_TIME_PAGE :
			{
				filler.pageBoundCharts.put(printImage, this);
				break;
			}
			case JRExpression.EVALUATION_TIME_COLUMN :
			{
				filler.columnBoundCharts.put(printImage, this);
				break;
			}
			case JRExpression.EVALUATION_TIME_GROUP :
			{
				Map specificGroupBoundCharts = (Map)filler.groupBoundCharts.get(getEvaluationGroup().getName());
				specificGroupBoundCharts.put(printImage, this);
				break;
			}
			case JRExpression.EVALUATION_TIME_NOW :
			default :
			{
				copy(printImage);
			}
		}
		
		return printImage;
	}


	/**
	 *
	 */
	protected void copy(JRPrintImage printImage) throws JRException//FIXME NOW do hyperlinks work for evaluationTime != now?
	{
		printImage.setRenderer(getRenderer());
		printImage.setAnchorName(getAnchorName());
		printImage.setHyperlinkReference(getHyperlinkReference());
		printImage.setHyperlinkAnchor(getHyperlinkAnchor());
		printImage.setHyperlinkPage(getHyperlinkPage());
	}


}
