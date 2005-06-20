/*
 * ============================================================================
 *                   GNU Lesser General Public License
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

import java.util.ArrayList;
import java.util.List;

import net.sf.jasperreports.charts.JRBarChart;
import net.sf.jasperreports.charts.JRBarPlot;
import net.sf.jasperreports.charts.JRCategoryDataset;
import net.sf.jasperreports.charts.JRPie3DChart;
import net.sf.jasperreports.charts.JRPie3DPlot;
import net.sf.jasperreports.charts.JRPieChart;
import net.sf.jasperreports.charts.JRPieDataset;
import net.sf.jasperreports.charts.JRPiePlot;
import net.sf.jasperreports.charts.JRStackedBarChart;
import net.sf.jasperreports.charts.fill.JRFillBarChart;
import net.sf.jasperreports.charts.fill.JRFillBarPlot;
import net.sf.jasperreports.charts.fill.JRFillCategoryDataset;
import net.sf.jasperreports.charts.fill.JRFillPie3DChart;
import net.sf.jasperreports.charts.fill.JRFillPie3DPlot;
import net.sf.jasperreports.charts.fill.JRFillPieChart;
import net.sf.jasperreports.charts.fill.JRFillPieDataset;
import net.sf.jasperreports.charts.fill.JRFillPiePlot;
import net.sf.jasperreports.charts.fill.JRFillStackedBarChart;
import net.sf.jasperreports.engine.JRAbstractObjectFactory;
import net.sf.jasperreports.engine.JRBand;
import net.sf.jasperreports.engine.JRElementGroup;
import net.sf.jasperreports.engine.JREllipse;
import net.sf.jasperreports.engine.JRField;
import net.sf.jasperreports.engine.JRFont;
import net.sf.jasperreports.engine.JRGroup;
import net.sf.jasperreports.engine.JRImage;
import net.sf.jasperreports.engine.JRLine;
import net.sf.jasperreports.engine.JRParameter;
import net.sf.jasperreports.engine.JRRectangle;
import net.sf.jasperreports.engine.JRReportFont;
import net.sf.jasperreports.engine.JRStaticText;
import net.sf.jasperreports.engine.JRSubreport;
import net.sf.jasperreports.engine.JRTextField;
import net.sf.jasperreports.engine.JRVariable;
import net.sf.jasperreports.engine.base.JRBaseFont;
import net.sf.jasperreports.engine.base.JRBaseReportFont;


/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id$
 */
public class JRFillObjectFactory extends JRAbstractObjectFactory
{


	/**
	 *
	 */
	private JRBaseFiller filler = null;

	private JRFont defaultFont = null;

	private List datasets = new ArrayList();


	/**
	 *
	 */
	protected JRFillObjectFactory(JRBaseFiller filler)
	{
		this.filler = filler;
	}


	/**
	 *
	 */
	protected JRFillChartDataset[] getDatasets()
	{
		return (JRFillChartDataset[]) datasets.toArray(new JRFillChartDataset[datasets.size()]);
	}
	

	/**
	 *
	 */
	public JRReportFont getReportFont(JRReportFont font)
	{
		JRBaseReportFont fillFont = null;
		
		if (font != null)
		{
			fillFont = (JRBaseReportFont)get(font);
			if (fillFont == null)
			{
				fillFont = new JRBaseReportFont(font);
				fillFont.setCachingAttributes(true);
				put(font, fillFont);
			}
		}
		
		return fillFont;
	}


	/**
	 *
	 */
	protected JRBaseFont getFont(JRFont font)
	{
		JRBaseFont fillFont = null;
		
		if (font != null)
		{
			fillFont = (JRBaseFont)get(font);
			if (fillFont == null)
			{
				fillFont = 
					new JRBaseFont(
						filler.getJasperPrint(), 
						getReportFont(font.getReportFont()), 
						font
						);
				fillFont.setCachingAttributes(true);
				put(font, fillFont);
			}
		}
		else 
		{
			if (defaultFont == null)
			{
				defaultFont = new JRBaseFont();
			}
			fillFont = getFont(defaultFont);
		}
		
		return fillFont;
	}


	/**
	 *
	 */
	protected JRFillParameter getParameter(JRParameter parameter)
	{
		JRFillParameter fillParameter = null;
		
		if (parameter != null)
		{
			fillParameter = (JRFillParameter)get(parameter);
			if (fillParameter == null)
			{
				fillParameter = new JRFillParameter(parameter, this);
			}
		}
		
		return fillParameter;
	}


	/**
	 *
	 */
	protected JRFillField getField(JRField field)
	{
		JRFillField fillField = null;
		
		if (field != null)
		{
			fillField = (JRFillField)get(field);
			if (fillField == null)
			{
				fillField = new JRFillField(field, this);
			}
		}
		
		return fillField;
	}


	/**
	 *
	 */
	protected JRFillVariable getVariable(JRVariable variable)
	{
		JRFillVariable fillVariable = null;
		
		if (variable != null)
		{
			fillVariable = (JRFillVariable)get(variable);
			if (fillVariable == null)
			{
				fillVariable = new JRFillVariable(variable, this);
			}
		}
		
		return fillVariable;
	}


	/**
	 *
	 */
	protected JRFillGroup getGroup(JRGroup group)
	{
		JRFillGroup fillGroup = null;
		
		if (group != null)
		{
			fillGroup = (JRFillGroup)get(group);
			if (fillGroup == null)
			{
				fillGroup = new JRFillGroup(group, this);
			}
		}
		
		return fillGroup;
	}


	/**
	 *
	 */
	protected JRFillBand getBand(JRBand band)
	{
		JRFillBand fillBand = null;
		
		//if (band != null)
		//{
		// for null bands, the filler's missingFillBand will be returned
			fillBand = (JRFillBand)get(band);
			if (fillBand == null)
			{
				fillBand = new JRFillBand(filler, band, this);
			}
		//}
		
		return fillBand;
	}


	/**
	 *
	 */
	protected JRFillElementGroup getElementGroup(JRElementGroup elementGroup)
	{
		JRFillElementGroup fillElementGroup = null;
		
		if (elementGroup != null)
		{
			fillElementGroup = (JRFillElementGroup)get(elementGroup);
			if (fillElementGroup == null)
			{
				fillElementGroup = new JRFillElementGroup(elementGroup, this);
			}
		}
		
		return fillElementGroup;
	}


	/**
	 *
	 */
	public JRLine getLine(JRLine line)
	{
		JRFillLine fillLine = null;
		
		if (line != null)
		{
			fillLine = (JRFillLine)get(line);
			if (fillLine == null)
			{
				fillLine = new JRFillLine(filler, line, this);
			}
		}
		
		return fillLine;
	}


	/**
	 *
	 */
	public JRRectangle getRectangle(JRRectangle rectangle)
	{
		JRFillRectangle fillRectangle = null;
		
		if (rectangle != null)
		{
			fillRectangle = (JRFillRectangle)get(rectangle);
			if (fillRectangle == null)
			{
				fillRectangle = new JRFillRectangle(filler, rectangle, this);
			}
		}
		
		return fillRectangle;
	}


	/**
	 *
	 */
	public JREllipse getEllipse(JREllipse ellipse)
	{
		JRFillEllipse fillEllipse = null;
		
		if (ellipse != null)
		{
			fillEllipse = (JRFillEllipse)get(ellipse);
			if (fillEllipse == null)
			{
				fillEllipse = new JRFillEllipse(filler, ellipse, this);
			}
		}
		
		return fillEllipse;
	}


	/**
	 *
	 */
	public JRImage getImage(JRImage image)
	{
		JRFillImage fillImage = null;
		
		if (image != null)
		{
			fillImage = (JRFillImage)get(image);
			if (fillImage == null)
			{
				fillImage = new JRFillImage(filler, image, this);
			}
		}
		
		return fillImage;
	}


	/**
	 *
	 */
	public JRStaticText getStaticText(JRStaticText staticText)
	{
		JRFillStaticText fillStaticText = null;
		
		if (staticText != null)
		{
			fillStaticText = (JRFillStaticText)get(staticText);
			if (fillStaticText == null)
			{
				fillStaticText = new JRFillStaticText(filler, staticText, this);
			}
		}
		
		return fillStaticText;
	}


	/**
	 *
	 */
	public JRTextField getTextField(JRTextField textField)
	{
		JRFillTextField fillTextField = null;
		
		if (textField != null)
		{
			fillTextField = (JRFillTextField)get(textField);
			if (fillTextField == null)
			{
				fillTextField = new JRFillTextField(filler, textField, this);
			}
		}
		
		return fillTextField;
	}


	/**
	 *
	 */
	public JRSubreport getSubreport(JRSubreport subreport)
	{
		JRFillSubreport fillSubreport = null;
		
		if (subreport != null)
		{
			fillSubreport = (JRFillSubreport)get(subreport);
			if (fillSubreport == null)
			{
				fillSubreport = new JRFillSubreport(filler, subreport, this);
			}
		}
		
		return fillSubreport;
	}


	/**
	 *
	 */
	public JRPieChart getPieChart(JRPieChart pieChart)
	{
		JRFillPieChart fillPieChart = null;
		
		if (pieChart != null)
		{
			fillPieChart = (JRFillPieChart)get(pieChart);
			if (fillPieChart == null)
			{
				fillPieChart = new JRFillPieChart(filler, pieChart, this);
			}
		}
		
		return fillPieChart;
	}


	/**
	 *
	 */
	public JRPieDataset getPieDataset(JRPieDataset pieDataset)
	{
		JRFillPieDataset fillPieDataset = null;
		
		if (pieDataset != null)
		{
			fillPieDataset = (JRFillPieDataset)get(pieDataset);
			if (fillPieDataset == null)
			{
				fillPieDataset = new JRFillPieDataset(pieDataset, this);
				datasets.add(fillPieDataset);
			}
		}
		
		return fillPieDataset;
	}


	/**
	 *
	 */
	public JRPiePlot getPiePlot(JRPiePlot piePlot)
	{
		JRFillPiePlot fillPiePlot = null;
		
		if (piePlot != null)
		{
			fillPiePlot = (JRFillPiePlot)get(piePlot);
			if (fillPiePlot == null)
			{
				fillPiePlot = new JRFillPiePlot(piePlot, this);
			}
		}
		
		return fillPiePlot;
	}


	/**
	 *
	 */
	public JRPie3DChart getPie3DChart(JRPie3DChart pie3DChart)
	{
		JRFillPie3DChart fillPie3DChart = null;
		
		if (pie3DChart != null)
		{
			fillPie3DChart = (JRFillPie3DChart)get(pie3DChart);
			if (fillPie3DChart == null)
			{
				fillPie3DChart = new JRFillPie3DChart(filler, pie3DChart, this);
			}
		}
		
		return fillPie3DChart;
	}


	/**
	 *
	 */
	public JRPie3DPlot getPie3DPlot(JRPie3DPlot pie3DPlot)
	{
		JRFillPie3DPlot fillPie3DPlot = null;
		
		if (pie3DPlot != null)
		{
			fillPie3DPlot = (JRFillPie3DPlot)get(pie3DPlot);
			if (fillPie3DPlot == null)
			{
				fillPie3DPlot = new JRFillPie3DPlot(pie3DPlot, this);
			}
		}
		
		return fillPie3DPlot;
	}


	/**
	 *
	 */
	public JRBarChart getBarChart(JRBarChart barChart)
	{
		JRFillBarChart fillBarChart = null;
		
		if (barChart != null)
		{
			fillBarChart = (JRFillBarChart)get(barChart);
			if (fillBarChart == null)
			{
				fillBarChart = new JRFillBarChart(filler, barChart, this);
			}
		}
		
		return fillBarChart;
	}


	/**
	 *
	 */
	public JRStackedBarChart getStackedBarChart(JRStackedBarChart stackedBarChart)
	{
		JRFillStackedBarChart fillBarChart = null;

		if (stackedBarChart != null)
		{
			fillBarChart = (JRFillStackedBarChart)get(stackedBarChart);
			if (fillBarChart == null)
			{
				fillBarChart = new JRFillStackedBarChart(filler, stackedBarChart, this);
			}
		}

		return fillBarChart;
	}


	/**
	 *
	 */
	public JRCategoryDataset getCategoryDataset(JRCategoryDataset categoryDataset)
	{
		JRFillCategoryDataset fillCategoryDataset = null;
		
		if (categoryDataset != null)
		{
			fillCategoryDataset = (JRFillCategoryDataset)get(categoryDataset);
			if (fillCategoryDataset == null)
			{
				fillCategoryDataset = new JRFillCategoryDataset(categoryDataset, this);
				datasets.add(fillCategoryDataset);
			}
		}
		
		return fillCategoryDataset;
	}


	/**
	 *
	 */
	public JRBarPlot getBarPlot(JRBarPlot barPlot)
	{
		JRFillBarPlot fillBarPlot = null;
		
		if (barPlot != null)
		{
			fillBarPlot = (JRFillBarPlot)get(barPlot);
			if (fillBarPlot == null)
			{
				fillBarPlot = new JRFillBarPlot(barPlot, this);
			}
		}
		
		return fillBarPlot;
	}


}
