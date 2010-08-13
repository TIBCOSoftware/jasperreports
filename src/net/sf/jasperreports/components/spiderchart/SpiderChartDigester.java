/*
 * JasperReports - Free Java Reporting Library.
 * Copyright (C) 2001 - 2009 Jaspersoft Corporation. All rights reserved.
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
package net.sf.jasperreports.components.spiderchart;

import net.sf.jasperreports.charts.design.JRDesignCategorySeries;
import net.sf.jasperreports.charts.xml.JRCategorySeriesFactory;
import net.sf.jasperreports.components.charts.ChartSettings;
import net.sf.jasperreports.components.charts.ChartSettingsXmlFactory;
import net.sf.jasperreports.engine.JRExpression;
import net.sf.jasperreports.engine.JRFont;
import net.sf.jasperreports.engine.component.XmlDigesterConfigurer;
import net.sf.jasperreports.engine.design.JRDesignExpression;
import net.sf.jasperreports.engine.xml.JRElementDatasetFactory;
import net.sf.jasperreports.engine.xml.JRExpressionFactory;
import net.sf.jasperreports.engine.xml.JRFontFactory;
import net.sf.jasperreports.engine.xml.JRXmlConstants;

import org.apache.commons.digester.Digester;

/**
 * 
 * @author sanda zaharia (shertage@users.sourceforge.net)
 * @version $Id: SpiderChartDigester.java 3892 2010-07-16 13:43:20Z shertage $
 */
public class SpiderChartDigester implements XmlDigesterConfigurer
{

	public void configureDigester(Digester digester)
	{
		addSpiderChartRules(digester);
	}
	
	
	protected void addSpiderChartRules(Digester digester)
	{
		String componentNamespace = digester.getRuleNamespaceURI();
		String jrNamespace = JRXmlConstants.JASPERREPORTS_NAMESPACE;
		
		String spiderChartPattern = "*/componentElement/spiderChart";
		digester.addFactoryCreate(spiderChartPattern, SpiderChartXmlFactory.class.getName());
		
		String chartSettingsPattern = spiderChartPattern + "/chartSettings";
		digester.addFactoryCreate(chartSettingsPattern, ChartSettingsXmlFactory.class.getName());
		digester.addSetNext(chartSettingsPattern, "setChartSettings", ChartSettings.class.getName());

		digester.setRuleNamespaceURI(jrNamespace);
		String chartTitlePattern = chartSettingsPattern + "/chartTitle";
		digester.addFactoryCreate(chartTitlePattern, ChartSettingsXmlFactory.ChartTitleFactory.class.getName());
		digester.addFactoryCreate(chartTitlePattern + "/font", JRFontFactory.ChartFontFactory.class.getName());
		digester.addSetNext(chartTitlePattern + "/font", "setTitleFont", JRFont.class.getName());
		digester.addFactoryCreate(chartTitlePattern + "/titleExpression", JRExpressionFactory.StringExpressionFactory.class);
		digester.addSetNext(chartTitlePattern + "/titleExpression", "setTitleExpression", JRDesignExpression.class.getName());
		digester.addCallMethod(chartTitlePattern + "/titleExpression", "setText", 0);
		
		String chartSubtitlePattern = chartSettingsPattern + "/chartSubtitle";
		digester.addFactoryCreate(chartSubtitlePattern, ChartSettingsXmlFactory.ChartSubtitleFactory.class.getName());
		digester.addFactoryCreate(chartSubtitlePattern + "/font", JRFontFactory.ChartFontFactory.class.getName());
		digester.addSetNext(chartSubtitlePattern + "/font", "setSubtitleFont", JRFont.class.getName());
		digester.addFactoryCreate(chartSubtitlePattern + "/subtitleExpression", JRExpressionFactory.StringExpressionFactory.class);
		digester.addSetNext(chartSubtitlePattern + "/subtitleExpression", "setSubtitleExpression", JRDesignExpression.class.getName());
		digester.addCallMethod(chartSubtitlePattern + "/subtitleExpression", "setText", 0);
		
		digester.addFactoryCreate(chartSettingsPattern + "/chartLegend", ChartSettingsXmlFactory.ChartLegendFactory.class.getName());
		digester.addFactoryCreate(chartSettingsPattern + "/chartLegend/font", JRFontFactory.ChartFontFactory.class.getName());
		digester.addSetNext(chartSettingsPattern + "/chartLegend/font", "setLegendFont", JRFont.class.getName());

		digester.setRuleNamespaceURI(componentNamespace);
		String spiderDatasetPattern = spiderChartPattern + "/spiderDataset";
		digester.addFactoryCreate(spiderDatasetPattern, SpiderDatasetXmlFactory.class.getName());
		digester.addSetNext(spiderDatasetPattern, "setDataset", SpiderDataset.class.getName());
		
		digester.setRuleNamespaceURI(jrNamespace);
		String datasetPattern = spiderDatasetPattern + "/dataset";
		digester.addFactoryCreate(datasetPattern, JRElementDatasetFactory.class.getName());

		String datasetIncrementWhenExpressionPath = datasetPattern + JRXmlConstants.ELEMENT_incrementWhenExpression;
		digester.addFactoryCreate(datasetIncrementWhenExpressionPath, JRExpressionFactory.BooleanExpressionFactory.class.getName());
		digester.addSetNext(datasetIncrementWhenExpressionPath, "setIncrementWhenExpression", JRExpression.class.getName());
		digester.addCallMethod(datasetIncrementWhenExpressionPath, "setText", 0);

		String seriesPattern = spiderDatasetPattern + "/categorySeries";
		digester.addFactoryCreate(seriesPattern, JRCategorySeriesFactory.class.getName());
		digester.addSetNext(seriesPattern, "addCategorySeries", JRDesignCategorySeries.class.getName());

		digester.addFactoryCreate(seriesPattern + "/seriesExpression", JRExpressionFactory.ComparableExpressionFactory.class);
		digester.addSetNext(seriesPattern + "/seriesExpression", "setSeriesExpression", JRDesignExpression.class.getName());
		digester.addCallMethod(seriesPattern + "/seriesExpression", "setText", 0);
		digester.addFactoryCreate(seriesPattern + "/categoryExpression", JRExpressionFactory.ComparableExpressionFactory.class);
		digester.addSetNext(seriesPattern + "/categoryExpression", "setCategoryExpression", JRDesignExpression.class.getName());
		digester.addCallMethod(seriesPattern + "/categoryExpression", "setText", 0);
		digester.addFactoryCreate(seriesPattern + "/labelExpression", JRExpressionFactory.StringExpressionFactory.class);
		digester.addSetNext(seriesPattern + "/labelExpression", "setLabelExpression", JRDesignExpression.class.getName());
		digester.addCallMethod(seriesPattern + "/labelExpression", "setText", 0);
		digester.addFactoryCreate(seriesPattern + "/valueExpression", JRExpressionFactory.NumberExpressionFactory.class);
		digester.addSetNext(seriesPattern + "/valueExpression", "setValueExpression", JRDesignExpression.class.getName());
		digester.addCallMethod(seriesPattern + "/valueExpression", "setText", 0);
		
		digester.setRuleNamespaceURI(componentNamespace);
		String plotPattern = spiderChartPattern + "/spiderPlot";
		digester.addFactoryCreate(plotPattern, SpiderPlotXmlFactory.class.getName());
		digester.addSetNext(plotPattern, "setPlot", SpiderPlot.class.getName());

		digester.setRuleNamespaceURI(jrNamespace);
		String labelFontPattern = plotPattern + "/labelFont/font";
		digester.addFactoryCreate(labelFontPattern, JRFontFactory.ChartFontFactory.class.getName());
		digester.addSetNext(labelFontPattern, "setLabelFont", JRFont.class.getName());

		digester.setRuleNamespaceURI(componentNamespace);
		String maxValueExpressionPattern = plotPattern + "/maxValueExpression";
		digester.addFactoryCreate(maxValueExpressionPattern, JRExpressionFactory.DoubleExpressionFactory.class);
		digester.addSetNext(maxValueExpressionPattern, "setMaxValueExpression", JRDesignExpression.class.getName() );
		digester.addCallMethod(maxValueExpressionPattern, "setText", 0 );
	}
}
