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
package net.sf.jasperreports.engine.fill;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.sf.jasperreports.charts.JRAreaPlot;
import net.sf.jasperreports.charts.JRBar3DPlot;
import net.sf.jasperreports.charts.JRBarPlot;
import net.sf.jasperreports.charts.JRBubblePlot;
import net.sf.jasperreports.charts.JRCandlestickPlot;
import net.sf.jasperreports.charts.JRCategoryDataset;
import net.sf.jasperreports.charts.JRCategorySeries;
import net.sf.jasperreports.charts.JRChartAxis;
import net.sf.jasperreports.charts.JRGanttDataset;
import net.sf.jasperreports.charts.JRGanttSeries;
import net.sf.jasperreports.charts.JRHighLowDataset;
import net.sf.jasperreports.charts.JRHighLowPlot;
import net.sf.jasperreports.charts.JRLinePlot;
import net.sf.jasperreports.charts.JRMeterPlot;
import net.sf.jasperreports.charts.JRMultiAxisPlot;
import net.sf.jasperreports.charts.JRPie3DPlot;
import net.sf.jasperreports.charts.JRPieDataset;
import net.sf.jasperreports.charts.JRPiePlot;
import net.sf.jasperreports.charts.JRPieSeries;
import net.sf.jasperreports.charts.JRScatterPlot;
import net.sf.jasperreports.charts.JRThermometerPlot;
import net.sf.jasperreports.charts.JRTimePeriodDataset;
import net.sf.jasperreports.charts.JRTimePeriodSeries;
import net.sf.jasperreports.charts.JRTimeSeries;
import net.sf.jasperreports.charts.JRTimeSeriesDataset;
import net.sf.jasperreports.charts.JRTimeSeriesPlot;
import net.sf.jasperreports.charts.JRValueDataset;
import net.sf.jasperreports.charts.JRXyDataset;
import net.sf.jasperreports.charts.JRXySeries;
import net.sf.jasperreports.charts.JRXyzDataset;
import net.sf.jasperreports.charts.JRXyzSeries;
import net.sf.jasperreports.charts.fill.JRFillAreaPlot;
import net.sf.jasperreports.charts.fill.JRFillBar3DPlot;
import net.sf.jasperreports.charts.fill.JRFillBarPlot;
import net.sf.jasperreports.charts.fill.JRFillBubblePlot;
import net.sf.jasperreports.charts.fill.JRFillCandlestickPlot;
import net.sf.jasperreports.charts.fill.JRFillCategoryDataset;
import net.sf.jasperreports.charts.fill.JRFillCategorySeries;
import net.sf.jasperreports.charts.fill.JRFillChartAxis;
import net.sf.jasperreports.charts.fill.JRFillGanttDataset;
import net.sf.jasperreports.charts.fill.JRFillGanttSeries;
import net.sf.jasperreports.charts.fill.JRFillHighLowDataset;
import net.sf.jasperreports.charts.fill.JRFillHighLowPlot;
import net.sf.jasperreports.charts.fill.JRFillLinePlot;
import net.sf.jasperreports.charts.fill.JRFillMeterPlot;
import net.sf.jasperreports.charts.fill.JRFillMultiAxisPlot;
import net.sf.jasperreports.charts.fill.JRFillPie3DPlot;
import net.sf.jasperreports.charts.fill.JRFillPieDataset;
import net.sf.jasperreports.charts.fill.JRFillPiePlot;
import net.sf.jasperreports.charts.fill.JRFillPieSeries;
import net.sf.jasperreports.charts.fill.JRFillScatterPlot;
import net.sf.jasperreports.charts.fill.JRFillThermometerPlot;
import net.sf.jasperreports.charts.fill.JRFillTimePeriodDataset;
import net.sf.jasperreports.charts.fill.JRFillTimePeriodSeries;
import net.sf.jasperreports.charts.fill.JRFillTimeSeries;
import net.sf.jasperreports.charts.fill.JRFillTimeSeriesDataset;
import net.sf.jasperreports.charts.fill.JRFillTimeSeriesPlot;
import net.sf.jasperreports.charts.fill.JRFillValueDataset;
import net.sf.jasperreports.charts.fill.JRFillXyDataset;
import net.sf.jasperreports.charts.fill.JRFillXySeries;
import net.sf.jasperreports.charts.fill.JRFillXyzDataset;
import net.sf.jasperreports.charts.fill.JRFillXyzSeries;
import net.sf.jasperreports.crosstabs.JRCrosstab;
import net.sf.jasperreports.crosstabs.JRCrosstabDataset;
import net.sf.jasperreports.crosstabs.JRCrosstabParameter;
import net.sf.jasperreports.crosstabs.fill.JRFillCrosstabParameter;
import net.sf.jasperreports.engine.JRAbstractObjectFactory;
import net.sf.jasperreports.engine.JRBand;
import net.sf.jasperreports.engine.JRBreak;
import net.sf.jasperreports.engine.JRChart;
import net.sf.jasperreports.engine.JRComponentElement;
import net.sf.jasperreports.engine.JRConditionalStyle;
import net.sf.jasperreports.engine.JRDataset;
import net.sf.jasperreports.engine.JRDatasetRun;
import net.sf.jasperreports.engine.JRDefaultStyleProvider;
import net.sf.jasperreports.engine.JRElementGroup;
import net.sf.jasperreports.engine.JREllipse;
import net.sf.jasperreports.engine.JRExpression;
import net.sf.jasperreports.engine.JRField;
import net.sf.jasperreports.engine.JRFrame;
import net.sf.jasperreports.engine.JRGenericElement;
import net.sf.jasperreports.engine.JRGroup;
import net.sf.jasperreports.engine.JRImage;
import net.sf.jasperreports.engine.JRLine;
import net.sf.jasperreports.engine.JRParameter;
import net.sf.jasperreports.engine.JRRectangle;
import net.sf.jasperreports.engine.JRReportTemplate;
import net.sf.jasperreports.engine.JRRuntimeException;
import net.sf.jasperreports.engine.JRSection;
import net.sf.jasperreports.engine.JRStaticText;
import net.sf.jasperreports.engine.JRStyle;
import net.sf.jasperreports.engine.JRStyleContainer;
import net.sf.jasperreports.engine.JRStyleSetter;
import net.sf.jasperreports.engine.JRSubreport;
import net.sf.jasperreports.engine.JRSubreportReturnValue;
import net.sf.jasperreports.engine.JRTextField;
import net.sf.jasperreports.engine.JRVariable;
import net.sf.jasperreports.engine.base.JRBaseConditionalStyle;
import net.sf.jasperreports.engine.base.JRBaseStyle;

import org.apache.commons.collections.SequencedHashMap;


/**
 * A factory used to instantiate fill objects based on compiled report objects.
 * 
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id$
 */
public class JRFillObjectFactory extends JRAbstractObjectFactory
{


	/**
	 *
	 */
	protected JRBaseFiller filler;
	private JRFillExpressionEvaluator evaluator;

	private JRFillObjectFactory parentFiller;
	
//	private JRFont defaultFont;

	private List<JRFillElementDataset> elementDatasets = new ArrayList<JRFillElementDataset>();
	private Map<String,List<JRFillElementDataset>> elementDatasetMap = new HashMap<String,List<JRFillElementDataset>>();
	
	private Map<String,List<JRStyleSetter>> delayedStyleSettersByName = new HashMap<String,List<JRStyleSetter>>();
	
	protected static class StylesList
	{
		private final List<JRStyle> styles = new ArrayList<JRStyle>();
		private final Map<String,Integer> stylesIdx = new HashMap<String,Integer>();
		
		public boolean containsStyle(String name)
		{
			return stylesIdx.containsKey(name);
		}
		
		public JRStyle getStyle(String name)
		{
			Integer idx = stylesIdx.get(name);
			return idx == null ? null : styles.get(idx.intValue());
		}
		
		public void addStyle(JRStyle style)
		{
			styles.add(style);
			stylesIdx.put(style.getName(), Integer.valueOf(styles.size() - 1));
		}
		
		public void renamed(String oldName, String newName)
		{
			Integer idx = stylesIdx.remove(oldName);
			stylesIdx.put(newName, idx);
		}
	}
	
	private Set<JRStyle> originalStyleList;
	private StylesList stylesMap = new StylesList();


	/**
	 *
	 */
	protected JRFillObjectFactory(JRBaseFiller filler)
	{
		this(filler, filler.calculator);
	}


	/**
	 *
	 */
	public JRFillObjectFactory(JRBaseFiller filler, JRFillExpressionEvaluator expressionEvaluator)
	{
		this.filler = filler;
		this.evaluator = expressionEvaluator;
	}

	
	public JRFillObjectFactory(JRFillObjectFactory parent, JRFillExpressionEvaluator expressionEvaluator)
	{
		this.parentFiller = parent;
		this.filler = parent.filler;
		this.evaluator = expressionEvaluator;
	}

	
	/**
	 * Returns the expression evaluator which is to be used by objects
	 * created by this factory.
	 * 
	 * @return the expression evaluator associated with this factory
	 */
	public JRFillExpressionEvaluator getExpressionEvaluator()
	{
		return evaluator;
	}

	/**
	 *
	 */
	protected JRFillChartDataset[] getDatasets()
	{
		return elementDatasets.toArray(new JRFillChartDataset[elementDatasets.size()]);
	}


	protected JRFillElementDataset[] getElementDatasets(JRDataset dataset)
	{
		JRFillElementDataset[] elementDatasetsArray;
		List<JRFillElementDataset> elementDatasetsList;
		if (dataset.isMainDataset())
		{
			elementDatasetsList = elementDatasets;
		}
		else
		{
			elementDatasetsList = elementDatasetMap.get(dataset.getName());
		}

		if (elementDatasetsList == null || elementDatasetsList.size() == 0)
		{
			elementDatasetsArray = new JRFillElementDataset[0];
		}
		else
		{
			elementDatasetsArray = new JRFillElementDataset[elementDatasetsList.size()];
			elementDatasetsList.toArray(elementDatasetsArray);
		}

		return elementDatasetsArray;
	}


	protected void registerDelayedStyleSetter(JRStyleSetter delayedSetter, String styleName)
	{
		if (parentFiller == null)
		{
			List<JRStyleSetter> setters = delayedStyleSettersByName.get(styleName);
			if (setters == null)
			{
				setters = new ArrayList<JRStyleSetter>();
				delayedStyleSettersByName.put(styleName, setters);
			}
			
			setters.add(delayedSetter);
		}
		else
		{
			parentFiller.registerDelayedStyleSetter(delayedSetter, styleName);
		}
	}

	public void registerDelayedStyleSetter(JRStyleSetter delayedSetter, JRStyleContainer styleContainer)
	{
		JRStyle style = styleContainer.getStyle();
		String nameReference = styleContainer.getStyleNameReference();
		if (style != null)
		{
			registerDelayedStyleSetter(delayedSetter, style.getName());
		}
		else if (nameReference != null)
		{
			registerDelayedStyleSetter(delayedSetter, nameReference);
		}
	}
	
	public JRBaseStyle getStyle(JRStyle style)
	{
		JRBaseStyle fillStyle = null;

		if (style != null)
		{
			fillStyle = (JRBaseStyle) get(style);
			if (fillStyle == null)
			{
				fillStyle = new JRBaseStyle(style, this);
				
				// deduplicate to previously created identical instances
				fillStyle = filler.fillContext.deduplicate(fillStyle);
				
				put(style, fillStyle);
				
				if (originalStyleList != null && originalStyleList.contains(style))
				{
					renameExistingStyle(style.getName());
					stylesMap.addStyle(style);
				}				
			}
		}

		return fillStyle;
	}

	protected void renameExistingStyle(String name)
	{
		JRStyle originalStyle = stylesMap.getStyle(name);
		if (originalStyle != null)
		{
			//found a previous external style with the same name
			//renaming the previous style
			JRBaseStyle style = (JRBaseStyle) get(originalStyle);
			
			String newName;
			int suf = 1;
			do
			{
				newName = name + suf;
				++suf;
			}
			while(stylesMap.containsStyle(newName));
			
			style.rename(newName);
			stylesMap.renamed(name, newName);
		}
	}


	public void setStyle(JRStyleSetter setter, JRStyleContainer styleContainer)
	{
		JRStyle style = styleContainer.getStyle();
		String nameReference = styleContainer.getStyleNameReference();
		if (style != null)
		{
			JRStyle newStyle = getStyle(style);
			setter.setStyle(newStyle);
		}
		else if (nameReference != null)
		{
			JRStyle originalStyle = stylesMap.getStyle(nameReference);
			if (originalStyle == null)
			{
				throw new JRRuntimeException("Style " + nameReference + " not found");
			}
			
			JRStyle externalStyle = (JRStyle) get(originalStyle);
			setter.setStyle(externalStyle);
		}
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
	public JRFillVariable getVariable(JRVariable variable)
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
	protected JRFillSection getSection(JRSection section)
	{
		JRFillSection fillSection = null;

		if (section == null)
		{
			fillSection = filler.missingFillSection;
		}
		else
		{
			fillSection = (JRFillSection)get(section);
			if (fillSection == null)
			{
				fillSection = new JRFillSection(filler, section, this);
			}
		}

		return fillSection;
	}


	/**
	 *
	 */
	protected JRFillBand getBand(JRBand band)
	{
		JRFillBand fillBand = null;

		if (band == null)
		{
			fillBand = filler.missingFillBand;
		}
		else
		{
			fillBand = (JRFillBand)get(band);
			if (fillBand == null)
			{
				fillBand = new JRFillBand(filler, band, this);
			}
		}

		return fillBand;
	}


	/**
	 *
	 */
	public void visitElementGroup(JRElementGroup elementGroup)
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

		setVisitResult(fillElementGroup);
	}


	/**
	 *
	 */
	public void visitBreak(JRBreak breakElement)
	{
		JRFillBreak fillBreak = null;

		if (breakElement != null)
		{
			fillBreak = (JRFillBreak)get(breakElement);
			if (fillBreak == null)
			{
				fillBreak = new JRFillBreak(filler, breakElement, this);
			}
		}

		setVisitResult(fillBreak);
	}


	/**
	 *
	 */
	public void visitLine(JRLine line)
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

		setVisitResult(fillLine);
	}


	/**
	 *
	 */
	public void visitRectangle(JRRectangle rectangle)
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

		setVisitResult(fillRectangle);
	}


	/**
	 *
	 */
	public void visitEllipse(JREllipse ellipse)
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

		setVisitResult(fillEllipse);
	}


	/**
	 *
	 */
	public void visitImage(JRImage image)
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

		setVisitResult(fillImage);
	}


	/**
	 *
	 */
	public void visitStaticText(JRStaticText staticText)
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

		setVisitResult(fillStaticText);
	}


	/**
	 *
	 */
	public void visitTextField(JRTextField textField)
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

		setVisitResult(fillTextField);
	}


	/**
	 *
	 */
	public void visitSubreport(JRSubreport subreport)
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

		setVisitResult(fillSubreport);
	}


	public void visitChart(JRChart chart)
	{
		JRFillChart fillChart = null;

		if (chart != null)
		{
			fillChart = (JRFillChart)get(chart);
			if (fillChart == null)
			{
				fillChart = new JRFillChart(filler, chart, this);
			}
		}

		setVisitResult(fillChart);
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
				registerElementDataset(fillPieDataset);
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
	public JRCategoryDataset getCategoryDataset(JRCategoryDataset categoryDataset)
	{
		JRFillCategoryDataset fillCategoryDataset = null;

		if (categoryDataset != null)
		{
			fillCategoryDataset = (JRFillCategoryDataset)get(categoryDataset);
			if (fillCategoryDataset == null)
			{
				fillCategoryDataset = new JRFillCategoryDataset(categoryDataset, this);
				registerElementDataset(fillCategoryDataset);
			}
		}

		return fillCategoryDataset;
	}

	public JRXyzDataset getXyzDataset( JRXyzDataset xyzDataset ){
		JRFillXyzDataset fillXyzDataset = null;
		if( xyzDataset != null ){
			fillXyzDataset = (JRFillXyzDataset)get( xyzDataset );
			if( fillXyzDataset == null ){
				fillXyzDataset = new JRFillXyzDataset( xyzDataset, this );
				registerElementDataset(fillXyzDataset);
			}
		}

		return fillXyzDataset;

	}


	/**
	 *
	 */
	public JRXyDataset getXyDataset(JRXyDataset xyDataset)
	{
		JRFillXyDataset fillXyDataset = null;

		if (xyDataset != null)
		{
			fillXyDataset = (JRFillXyDataset)get(xyDataset);
			if (fillXyDataset == null)
			{
				fillXyDataset = new JRFillXyDataset(xyDataset, this);
				registerElementDataset(fillXyDataset);
			}
		}

		return fillXyDataset;
	}


	/**
	 *
	 */
	public JRTimeSeriesDataset getTimeSeriesDataset( JRTimeSeriesDataset timeSeriesDataset ){
		JRFillTimeSeriesDataset fillTimeSeriesDataset = null;

		if( timeSeriesDataset != null ){

			fillTimeSeriesDataset = (JRFillTimeSeriesDataset)get( timeSeriesDataset );

			if( fillTimeSeriesDataset == null ){
				fillTimeSeriesDataset = new JRFillTimeSeriesDataset( timeSeriesDataset, this );
				registerElementDataset(fillTimeSeriesDataset);
			}
		}

		return fillTimeSeriesDataset;
	}

	public JRTimePeriodDataset getTimePeriodDataset( JRTimePeriodDataset timePeriodDataset ){
		JRFillTimePeriodDataset fillTimePeriodDataset = null;
		if( timePeriodDataset != null ){
			fillTimePeriodDataset = (JRFillTimePeriodDataset)get( timePeriodDataset );
			if( fillTimePeriodDataset == null ){
				fillTimePeriodDataset = new JRFillTimePeriodDataset( timePeriodDataset, this );
				registerElementDataset(fillTimePeriodDataset);
			}
		}
		return fillTimePeriodDataset;
	}
	
	/**
	 * 
	 */
	public JRGanttDataset getGanttDataset(JRGanttDataset ganttDataset)
	{
		JRFillGanttDataset fillGanttDataset = null;
		
		if (ganttDataset != null)
		{
			fillGanttDataset = (JRFillGanttDataset)get(ganttDataset);
			if (fillGanttDataset == null)
			{
				fillGanttDataset = new JRFillGanttDataset(ganttDataset, this);
				registerElementDataset(fillGanttDataset);
			}
		}
		
		return fillGanttDataset;
	}
	
	/**
	 *
	 */
	public JRPieSeries getPieSeries(JRPieSeries pieSeries)
	{
		JRFillPieSeries fillPieSeries = null;

		if (pieSeries != null)
		{
			fillPieSeries = (JRFillPieSeries)get(pieSeries);
			if (fillPieSeries == null)
			{
				fillPieSeries = new JRFillPieSeries(pieSeries, this);
			}
		}

		return fillPieSeries;
	}


	/**
	 *
	 */
	public JRCategorySeries getCategorySeries(JRCategorySeries categorySeries)
	{
		JRFillCategorySeries fillCategorySeries = null;

		if (categorySeries != null)
		{
			fillCategorySeries = (JRFillCategorySeries)get(categorySeries);
			if (fillCategorySeries == null)
			{
				fillCategorySeries = new JRFillCategorySeries(categorySeries, this);
			}
		}

		return fillCategorySeries;
	}


	public JRXyzSeries getXyzSeries( JRXyzSeries xyzSeries ){
		JRFillXyzSeries fillXyzSeries = null;

		if( xyzSeries != null ){
			fillXyzSeries = (JRFillXyzSeries)get( xyzSeries );

			if( fillXyzSeries == null ){
				fillXyzSeries = new JRFillXyzSeries( xyzSeries, this );
			}
		}

		return fillXyzSeries;
	}


	/**
	 *
	 */
	public JRXySeries getXySeries(JRXySeries xySeries)
	{
		JRFillXySeries fillXySeries = null;

		if (xySeries != null)
		{
			fillXySeries = (JRFillXySeries)get(xySeries);
			if (fillXySeries == null)
			{
				fillXySeries = new JRFillXySeries(xySeries, this);
			}
		}

		return fillXySeries;
	}
	
	
	/**
	 * 
	 */
	public JRGanttSeries getGanttSeries(JRGanttSeries ganttSeries)
	{
		JRFillGanttSeries fillGanttSeries = null;
		
		if (ganttSeries != null)
		{
			fillGanttSeries = (JRFillGanttSeries)get(ganttSeries);
			if (fillGanttSeries == null)
			{
				fillGanttSeries = new JRFillGanttSeries(ganttSeries, this);
			}
		}
		
		return fillGanttSeries;
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


	/**
	 *
	 */
	public JRTimeSeries getTimeSeries(JRTimeSeries timeSeries)
	{
		JRFillTimeSeries fillTimeSeries = null;

		if (timeSeries != null)
		{
			fillTimeSeries = (JRFillTimeSeries)get(timeSeries);
			if (fillTimeSeries == null)
			{
				fillTimeSeries = new JRFillTimeSeries(timeSeries, this);
			}
		}

		return fillTimeSeries;
	}

	public JRTimePeriodSeries getTimePeriodSeries( JRTimePeriodSeries timePeriodSeries ){
		JRFillTimePeriodSeries fillTimePeriodSeries = null;
		if( timePeriodSeries != null ){
			fillTimePeriodSeries = (JRFillTimePeriodSeries)get( timePeriodSeries );
			if( fillTimePeriodSeries == null ){
				fillTimePeriodSeries = new JRFillTimePeriodSeries( timePeriodSeries, this );
			}
		}

		return fillTimePeriodSeries;
	}


	/**
	 *
	 */
	public JRBar3DPlot getBar3DPlot(JRBar3DPlot barPlot) {
		JRFillBar3DPlot fillBarPlot = null;

		if (barPlot != null){
			fillBarPlot = (JRFillBar3DPlot)get(barPlot);
			if (fillBarPlot == null){
				fillBarPlot = new JRFillBar3DPlot(barPlot, this);
			}
		}

		return fillBarPlot;
	}


	/**
	 *
	 */
	public JRLinePlot getLinePlot(JRLinePlot linePlot) {
		JRFillLinePlot fillLinePlot = null;

		if (linePlot != null){
			fillLinePlot = (JRFillLinePlot)get(linePlot);
			if (fillLinePlot == null){
				fillLinePlot = new JRFillLinePlot(linePlot, this);
			}
		}

		return fillLinePlot;
	}


	/**
	 *
	 */
	public JRScatterPlot getScatterPlot(JRScatterPlot scatterPlot) {
		JRFillScatterPlot fillScatterPlot = null;

		if (scatterPlot != null){
			fillScatterPlot = (JRFillScatterPlot)get(scatterPlot);
			if (fillScatterPlot == null){
				fillScatterPlot = new JRFillScatterPlot(scatterPlot, this);
			}
		}

		return fillScatterPlot;
	}


	/**
	 *
	 */
	public JRAreaPlot getAreaPlot(JRAreaPlot areaPlot) {
		JRFillAreaPlot fillAreaPlot = null;

		if (areaPlot != null)
		{
			fillAreaPlot = (JRFillAreaPlot)get(areaPlot);
			if (fillAreaPlot == null)
			{
				fillAreaPlot = new JRFillAreaPlot(areaPlot, this);
			}
		}

		return fillAreaPlot;
	}


	/* (non-Javadoc)
	 * @see net.sf.jasperreports.engine.JRAbstractObjectFactory#getBubblePlot(net.sf.jasperreports.charts.JRBubblePlot)
	 */
	public JRBubblePlot getBubblePlot(JRBubblePlot bubblePlot) {
		JRFillBubblePlot fillBubblePlot = null;

		if (bubblePlot != null)
		{
			fillBubblePlot = (JRFillBubblePlot)get(bubblePlot);
			if (fillBubblePlot == null)
			{
				fillBubblePlot = new JRFillBubblePlot(bubblePlot, this);
			}
		}

		return fillBubblePlot;
	}


	/**
	 *
	 */
	public JRHighLowDataset getHighLowDataset(JRHighLowDataset highLowDataset)
	{
		JRFillHighLowDataset fillHighLowDataset = null;

		if (highLowDataset != null)
		{
			fillHighLowDataset = (JRFillHighLowDataset)get(highLowDataset);
			if (fillHighLowDataset == null)
			{
				fillHighLowDataset = new JRFillHighLowDataset(highLowDataset, this);
				registerElementDataset(fillHighLowDataset);
			}
		}

		return fillHighLowDataset;
	}


	/**
	 *
	 */
	public JRHighLowPlot getHighLowPlot(JRHighLowPlot highLowPlot) {
		JRFillHighLowPlot fillHighLowPlot = null;

		if (highLowPlot != null){
			fillHighLowPlot = (JRFillHighLowPlot)get(highLowPlot);
			if (fillHighLowPlot == null){
				fillHighLowPlot = new JRFillHighLowPlot(highLowPlot, this);
			}
		}

		return fillHighLowPlot;
	}


	public JRCandlestickPlot getCandlestickPlot(JRCandlestickPlot candlestickPlot)
	{
		JRFillCandlestickPlot fillCandlestickPlot = null;

		if (candlestickPlot != null){
			fillCandlestickPlot = (JRFillCandlestickPlot)get(candlestickPlot);
			if (fillCandlestickPlot == null){
				fillCandlestickPlot = new JRFillCandlestickPlot(candlestickPlot, this);
			}
		}

		return fillCandlestickPlot;
	}



	public JRTimeSeriesPlot getTimeSeriesPlot( JRTimeSeriesPlot plot ){
		JRFillTimeSeriesPlot fillPlot = null;
		if( plot != null ){
			fillPlot = (JRFillTimeSeriesPlot)get( plot );
			if( fillPlot == null ){
				fillPlot = new JRFillTimeSeriesPlot( plot, this );
			}
		}

		return fillPlot;

	}

	/**
	 *
	 */
	public JRValueDataset getValueDataset(JRValueDataset valueDataset)
	{
		JRFillValueDataset fillValueDataset = null;

		if (valueDataset != null)
		{
			fillValueDataset = (JRFillValueDataset)get(valueDataset);
			if (fillValueDataset == null)
			{
				fillValueDataset = new JRFillValueDataset(valueDataset, this);
				registerElementDataset(fillValueDataset);
			}
		}

		return fillValueDataset;
	}

	/**
	 *
	 */
	public JRMeterPlot getMeterPlot(JRMeterPlot meterPlot)
	{
		JRFillMeterPlot fillMeterPlot = null;

		if (meterPlot != null)
		{
			fillMeterPlot = (JRFillMeterPlot)get(meterPlot);
			if (fillMeterPlot == null)
			{
				fillMeterPlot = new JRFillMeterPlot(meterPlot, this);
			}
		}

		return fillMeterPlot;
	}
	
	/**
	 *
	 */
	public JRThermometerPlot getThermometerPlot(JRThermometerPlot thermometerPlot)
	{
		JRFillThermometerPlot fillThermometerPlot = null;

		if (thermometerPlot != null)
		{
			fillThermometerPlot = (JRFillThermometerPlot)get(thermometerPlot);
			if (fillThermometerPlot == null)
			{
				fillThermometerPlot = new JRFillThermometerPlot(thermometerPlot, this);
			}
		}

		return fillThermometerPlot;
	}
	
	
	/**
	 *
	 */
	public JRMultiAxisPlot getMultiAxisPlot(JRMultiAxisPlot multiAxisPlot)
	{
		JRFillMultiAxisPlot fillMultiAxisPlot = null;

		if (multiAxisPlot != null)
		{
			fillMultiAxisPlot = (JRFillMultiAxisPlot)get(multiAxisPlot);
			if (fillMultiAxisPlot == null)
			{
				fillMultiAxisPlot = new JRFillMultiAxisPlot(multiAxisPlot, this);
			}
		}

		return fillMultiAxisPlot;
	}
	
	
	protected JRFillSubreportReturnValue getSubreportReturnValue(JRSubreportReturnValue returnValue)
	{
		JRFillSubreportReturnValue fillReturnValue = null;

		if (returnValue != null)
		{
			fillReturnValue = (JRFillSubreportReturnValue) get(returnValue);
			if (fillReturnValue == null)
			{
				fillReturnValue = new JRFillSubreportReturnValue(returnValue, this, filler);
			}
		}

		return fillReturnValue;
	}


	public void visitCrosstab(JRCrosstab crosstabElement)
	{
		JRFillCrosstab fillCrosstab = null;

		if (crosstabElement != null)
		{
			fillCrosstab = (JRFillCrosstab) get(crosstabElement);
			if (fillCrosstab == null)
			{
				fillCrosstab = new JRFillCrosstab(filler, crosstabElement, this);
			}
		}

		setVisitResult(fillCrosstab);
	}


	public JRFillCrosstab.JRFillCrosstabDataset getCrosstabDataset(JRCrosstabDataset dataset, JRFillCrosstab fillCrosstab)
	{
		JRFillCrosstab.JRFillCrosstabDataset fillDataset = null;

		if (dataset != null)
		{
			fillDataset = (JRFillCrosstab.JRFillCrosstabDataset)get(dataset);
			if (fillDataset == null)
			{
				fillDataset = fillCrosstab.new JRFillCrosstabDataset(dataset, this);
				registerElementDataset(fillDataset);
			}
		}

		return fillDataset;
	}


	public JRFillDataset getDataset(JRDataset dataset)
	{
		JRFillDataset fillDataset = null;

		if (dataset != null)
		{
			fillDataset = (JRFillDataset) get(dataset);
			if (fillDataset == null)
			{
				fillDataset = new JRFillDataset(filler, dataset, this);
			}
		}

		return fillDataset;
	}


	/**
	 * Register an element dataset with the report filler.
	 * 
	 * <p>
	 * Registration of element datasets is required in order for the filler
	 * to increment the datasets when iterating through the datasource.
	 * 
	 * @param elementDataset the dataset to register
	 */
	public void registerElementDataset(JRFillElementDataset elementDataset)
	{
		List<JRFillElementDataset> elementDatasetsList;
		JRDatasetRun datasetRun = elementDataset.getDatasetRun();
		if (datasetRun == null)
		{
			elementDatasetsList = elementDatasets;
		}
		else
		{
			String datasetName = datasetRun.getDatasetName();
			elementDatasetsList = elementDatasetMap.get(datasetName);
			if (elementDatasetsList == null)
			{
				elementDatasetsList = new ArrayList<JRFillElementDataset>();
				elementDatasetMap.put(datasetName, elementDatasetsList);
			}
		}
		elementDatasetsList.add(elementDataset);
	}


	public JRFillDatasetRun getDatasetRun(JRDatasetRun datasetRun)
	{
		JRFillDatasetRun fillDatasetRun = null;

		if (datasetRun != null)
		{
			fillDatasetRun = (JRFillDatasetRun) get(datasetRun);
			if (fillDatasetRun == null)
			{
				fillDatasetRun = new JRFillDatasetRun(filler, datasetRun, this);
			}
		}

		return fillDatasetRun;
	}


	public JRFillCrosstabParameter getCrosstabParameter(JRCrosstabParameter parameter)
	{
		JRFillCrosstabParameter fillParameter = null;

		if (parameter != null)
		{
			fillParameter = (JRFillCrosstabParameter) get(parameter);
			if (fillParameter == null)
			{
				fillParameter = new JRFillCrosstabParameter(parameter, this);
			}
		}

		return fillParameter;
	}


	public void visitFrame(JRFrame frame)
	{
		Object fillFrame = null;
		// This is the only place where an object gets replaced in the factory map by something else,
		// and we can no longer make a precise cast when getting it.
		// The JRFillFrame object is replaced in the map by a JRFillFrameElements object.
		//JRFillFrame fillFrame = null;

		if (frame != null)
		{
			fillFrame = get(frame);
			//fillFrame = (JRFillFrame) get(frame);
			if (fillFrame == null)
			{
				fillFrame = new JRFillFrame(filler, frame, this);
			}
		}

		setVisitResult(fillFrame);
	}


	/**
	 * Returns the current report filler.
	 * 
	 * @return the current report filler
	 */
	public JRBaseFiller getFiller()
	{
		return filler;
	}


	/**
	 *
	 */
	public JRConditionalStyle getConditionalStyle(JRConditionalStyle conditionalStyle, JRStyle style)
	{
		JRBaseConditionalStyle baseConditionalStyle = null;
		if (conditionalStyle != null)
		{
			baseConditionalStyle = (JRBaseConditionalStyle) get(conditionalStyle);
			if (baseConditionalStyle == null) {
				baseConditionalStyle = new JRBaseConditionalStyle(conditionalStyle, style, this);
				put(conditionalStyle, baseConditionalStyle);
			}
		}
		return baseConditionalStyle;
	}


	public JRExpression getExpression(JRExpression expression, boolean assignNotUsedId)
	{
		return expression;
	}


	public JRChartAxis getChartAxis(JRChartAxis axis)
	{
		JRFillChartAxis fillAxis = null;
		if (axis != null)
		{
			fillAxis = (JRFillChartAxis) get(axis);
			if (fillAxis == null)
			{
				fillAxis = new JRFillChartAxis(axis, this);
			}
		}
		return fillAxis;
	}


	public JRFillReportTemplate getReportTemplate(JRReportTemplate template)
	{
		JRFillReportTemplate fillTemplate = null;
		if (template != null)
		{
			fillTemplate = (JRFillReportTemplate) get(template);
			if (fillTemplate == null)
			{
				fillTemplate = new JRFillReportTemplate(template, filler, this);
			}
		}
		return fillTemplate;
	}
	
	public List<JRStyle> setStyles(List<JRStyle> styles)
	{
		originalStyleList = new HashSet<JRStyle>(styles);
		
		//filtering requested styles
		Set<JRStyle> requestedStyles = collectRequestedStyles(styles);
		
		//collect used styles
		Map<JRStyle,Object> usedStylesMap = new SequencedHashMap();
		Map<String,JRStyle> allStylesMap = new HashMap<String,JRStyle>();
		for (Iterator<JRStyle> it = styles.iterator(); it.hasNext();)
		{
			JRStyle style = it.next();
			if (requestedStyles.contains(style))
			{
				collectUsedStyles(style, usedStylesMap, allStylesMap);
			}
			allStylesMap.put(style.getName(), style);
		}
		
		List<JRStyle> includedStyles = new ArrayList<JRStyle>();
		for (Iterator<JRStyle> it = usedStylesMap.keySet().iterator(); it.hasNext();)
		{
			JRStyle style = it.next();
			JRStyle newStyle = getStyle(style);
			
			includedStyles.add(newStyle);
			if (requestedStyles.contains(style))
			{
				useDelayedStyle(newStyle);
			}			
		}
		
		checkUnresolvedReferences();
		
		return includedStyles;
	}

	protected Set<JRStyle> collectRequestedStyles(List<JRStyle> externalStyles)
	{
		Map<String,JRStyle> requestedStylesMap = new HashMap<String,JRStyle>();
		for (Iterator<JRStyle> it = externalStyles.iterator(); it.hasNext();)
		{
			JRStyle style = it.next();
			String name = style.getName();
			if (delayedStyleSettersByName.containsKey(name))
			{
				requestedStylesMap.put(name, style);
			}
		}
		
		return new HashSet<JRStyle>(requestedStylesMap.values());
	}

	protected void collectUsedStyles(JRStyle style, Map<JRStyle,Object> usedStylesMap, Map<String,JRStyle> allStylesMap)
	{
		if (!usedStylesMap.containsKey(style) && originalStyleList.contains(style))
		{
			JRStyle parent = style.getStyle();
			if (parent == null)
			{
				String parentName = style.getStyleNameReference();
				if (parentName != null)
				{
					parent = allStylesMap.get(parentName);
					if (parent == null)
					{
						throw new JRRuntimeException("Style " + parentName + " not found");
					}
				}
			}
			
			if (parent != null)
			{
				collectUsedStyles(parent, usedStylesMap, allStylesMap);
			}
			
			usedStylesMap.put(style, null);
		}
	}
	
	protected void useDelayedStyle(JRStyle style)
	{
		List<JRStyleSetter> delayedSetters = delayedStyleSettersByName.remove(style.getName());
		if (delayedSetters != null)
		{
			for (Iterator<JRStyleSetter> it = delayedSetters.iterator(); it.hasNext();)
			{
				JRStyleSetter setter = it.next();
				setter.setStyle(style);
			}
		}
	}

	protected void checkUnresolvedReferences()
	{
		if (!delayedStyleSettersByName.isEmpty())
		{
			StringBuffer errorMsg = new StringBuffer("Could not resolve style(s): ");
			for (Iterator<String> it = delayedStyleSettersByName.keySet().iterator(); it.hasNext();)
			{
				String name = it.next();
				errorMsg.append(name);
				errorMsg.append(", ");
			}
			
			throw new JRRuntimeException(errorMsg.substring(0, errorMsg.length() - 2));
		}
	}

	public JRDefaultStyleProvider getDefaultStyleProvider()
	{
		return filler.getJasperPrint().getDefaultStyleProvider();
	}


	public void visitComponentElement(JRComponentElement componentElement)
	{
		JRFillComponentElement fill = null;

		if (componentElement != null)
		{
			fill = (JRFillComponentElement) get(componentElement);
			if (fill == null)
			{
				fill = new JRFillComponentElement(filler, componentElement, this);
			}
		}

		setVisitResult(fill);
	}


	public void visitGenericElement(JRGenericElement element)
	{
		JRFillGenericElement fill = null;
		if (element != null)
		{
			fill = (JRFillGenericElement) get(element);
			if (fill == null)
			{
				fill = new JRFillGenericElement(filler, element, this);
			}
		}
		setVisitResult(fill);
	}

}
