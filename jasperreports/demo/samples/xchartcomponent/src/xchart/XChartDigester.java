/*
 * JasperReports - Free Java Reporting Library.
 * Copyright (C) 2001 - 2018 TIBCO Software Inc. All rights reserved.
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
package xchart;

import net.sf.jasperreports.engine.JRDatasetParameter;
import net.sf.jasperreports.engine.JRDatasetRun;
import net.sf.jasperreports.engine.JRExpression;
import net.sf.jasperreports.engine.component.XmlDigesterConfigurer;
import net.sf.jasperreports.engine.design.DesignReturnValue;
import net.sf.jasperreports.engine.design.JRDesignExpression;
import net.sf.jasperreports.engine.type.CalculationEnum;
import net.sf.jasperreports.engine.xml.JRDatasetRunFactory;
import net.sf.jasperreports.engine.xml.JRDatasetRunParameterFactory;
import net.sf.jasperreports.engine.xml.JRElementDatasetFactory;
import net.sf.jasperreports.engine.xml.JRExpressionFactory;
import net.sf.jasperreports.engine.xml.JRXmlConstants;
import net.sf.jasperreports.engine.xml.XmlConstantPropertyRule;

import org.apache.commons.digester.Digester;

/**
 * 
 * @author Sanda Zaharia (shertage@users.sourceforge.net)
 */
public class XChartDigester implements XmlDigesterConfigurer
{

	@Override
	public void configureDigester(Digester digester)
	{
		String componentNamespace = digester.getRuleNamespaceURI();
		String jrNamespace = JRXmlConstants.JASPERREPORTS_NAMESPACE;
		
		String xyChartPattern = "*/componentElement/XYChart";
		digester.addFactoryCreate(xyChartPattern, XYChartXmlFactory.class.getName());
		
		String xyDatasetPattern = xyChartPattern + "/XYDataset";
		digester.addFactoryCreate(xyDatasetPattern, XYDatasetXmlFactory.class.getName());
		digester.addSetNext(xyDatasetPattern, "setDataset", XYDataset.class.getName());

		digester.setRuleNamespaceURI(jrNamespace);

		String datasetPattern = xyDatasetPattern + "/dataset";
		digester.addFactoryCreate(datasetPattern, JRElementDatasetFactory.class.getName());

		String datasetIncrementWhenExpressionPath = datasetPattern + JRXmlConstants.ELEMENT_incrementWhenExpression;
		digester.addFactoryCreate(datasetIncrementWhenExpressionPath, JRExpressionFactory.class.getName());
		digester.addSetNext(datasetIncrementWhenExpressionPath, "setIncrementWhenExpression", JRExpression.class.getName());
		digester.addCallMethod(datasetIncrementWhenExpressionPath, "setText", 0);
		
		String datasetRunPattern = datasetPattern +"/" + JRXmlConstants.ELEMENT_datasetRun;
		digester.addFactoryCreate(datasetRunPattern, JRDatasetRunFactory.class.getName());
		digester.addSetNext(datasetRunPattern, "setDatasetRun", JRDatasetRun.class.getName());

		String datasetParamPattern = datasetRunPattern + "/" + JRXmlConstants.ELEMENT_datasetParameter;
		digester.addFactoryCreate(datasetParamPattern, JRDatasetRunParameterFactory.class.getName());
		digester.addSetNext(datasetParamPattern, "addParameter", JRDatasetParameter.class.getName());

		String datasetParamExprPattern = datasetParamPattern + "/" + JRXmlConstants.ELEMENT_datasetParameterExpression;
		digester.addFactoryCreate(datasetParamExprPattern, JRExpressionFactory.class.getName());
		digester.addSetNext(datasetParamExprPattern, "setExpression", JRExpression.class.getName());
		digester.addCallMethod(datasetParamExprPattern, "setText", 0);

		String returnValuePattern = datasetRunPattern + "/" + JRXmlConstants.ELEMENT_returnValue;
		digester.addObjectCreate(returnValuePattern, DesignReturnValue.class.getName());
		digester.addSetProperties(returnValuePattern, 
				new String[]{JRXmlConstants.ATTRIBUTE_incrementerFactoryClass, JRXmlConstants.ATTRIBUTE_calculation}, 
				new String[]{"incrementerFactoryClassName"});
		digester.addRule(returnValuePattern, new XmlConstantPropertyRule(
				JRXmlConstants.ATTRIBUTE_calculation, CalculationEnum.values()));
		digester.addSetNext(returnValuePattern, "addReturnValue");
		
		digester.setRuleNamespaceURI(componentNamespace);
		String seriesPattern = xyDatasetPattern + "/XYSeries";
		digester.addFactoryCreate(seriesPattern, XYSeriesFactory.class.getName());
		digester.addSetNext(seriesPattern, "addXYSeries", DesignXYSeries.class.getName());

		digester.setRuleNamespaceURI(jrNamespace);
		digester.addFactoryCreate(seriesPattern + "/seriesExpression", JRExpressionFactory.class.getName());
		digester.addSetNext(seriesPattern + "/seriesExpression", "setSeriesExpression", JRDesignExpression.class.getName());
		digester.addCallMethod(seriesPattern + "/seriesExpression", "setText", 0);
		
		digester.addFactoryCreate(seriesPattern + "/xValueExpression", JRExpressionFactory.class.getName());
		digester.addSetNext(seriesPattern + "/xValueExpression", "setXValueExpression", JRDesignExpression.class.getName());
		digester.addCallMethod(seriesPattern + "/xValueExpression", "setText", 0);
		
		digester.addFactoryCreate(seriesPattern + "/yValueExpression", JRExpressionFactory.class.getName());
		digester.addSetNext(seriesPattern + "/yValueExpression", "setYValueExpression", JRDesignExpression.class.getName());
		digester.addCallMethod(seriesPattern + "/yValueExpression", "setText", 0);

		digester.setRuleNamespaceURI(componentNamespace);
		
		digester.addFactoryCreate(seriesPattern + "/colorExpression", JRExpressionFactory.class.getName());
		digester.addSetNext(seriesPattern + "/colorExpression", "setColorExpression", JRDesignExpression.class.getName());
		digester.addCallMethod(seriesPattern + "/colorExpression", "setText", 0);
		
		String chartTitleExpressionPattern = xyChartPattern + "/chartTitleExpression";
		digester.addFactoryCreate(chartTitleExpressionPattern, JRExpressionFactory.class.getName());
		digester.addCallMethod(chartTitleExpressionPattern, "setText", 0);
		digester.addSetNext(chartTitleExpressionPattern, "setChartTitleExpression", JRExpression.class.getName());
		
		String xAxisTitleExpressionPattern = xyChartPattern + "/xAxisTitleExpression";
		digester.addFactoryCreate(xAxisTitleExpressionPattern, JRExpressionFactory.class.getName());
		digester.addCallMethod(xAxisTitleExpressionPattern, "setText", 0);
		digester.addSetNext(xAxisTitleExpressionPattern, "setXAxisTitleExpression", JRExpression.class.getName());
		
		String yAxisTitleExpressionPattern = xyChartPattern + "/yAxisTitleExpression";
		digester.addFactoryCreate(yAxisTitleExpressionPattern, JRExpressionFactory.class.getName());
		digester.addCallMethod(yAxisTitleExpressionPattern, "setText", 0);
		digester.addSetNext(yAxisTitleExpressionPattern, "setYAxisTitleExpression", JRExpression.class.getName());
		
	}

}
