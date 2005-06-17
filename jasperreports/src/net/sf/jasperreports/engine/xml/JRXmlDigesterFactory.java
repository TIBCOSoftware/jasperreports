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
package net.sf.jasperreports.engine.xml;

import javax.xml.parsers.ParserConfigurationException;

import net.sf.jasperreports.engine.JRBand;
import net.sf.jasperreports.engine.JRBox;
import net.sf.jasperreports.engine.JRExpression;
import net.sf.jasperreports.engine.JRField;
import net.sf.jasperreports.engine.JRFont;
import net.sf.jasperreports.engine.JRParameter;
import net.sf.jasperreports.engine.JRQuery;
import net.sf.jasperreports.engine.JRReportFont;
import net.sf.jasperreports.engine.JRSubreportParameter;
import net.sf.jasperreports.engine.design.JRDesignElement;
import net.sf.jasperreports.engine.design.JRDesignElementGroup;
import net.sf.jasperreports.engine.design.JRDesignExpression;
import net.sf.jasperreports.engine.design.JRDesignGroup;
import net.sf.jasperreports.engine.design.JRDesignVariable;
import net.sf.jasperreports.engine.design.JasperDesign;

import org.apache.commons.digester.Digester;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;


/**
 * JRXmlDigesterFactory encapsulates the code necessary to construct and configure
 * a digester in order to prepare it for parsing JasperReports xml definition files.
 * 
 * @author Peter Severin (peter_p_s@users.sourceforge.net)
 * @version $Id$
 */
public class JRXmlDigesterFactory 
{


	/**
	 * 
	 */
	private JRXmlDigesterFactory() 
	{
		super();
	}


	/**
	 * Configures the given digester for parsing jasperreport xml report definition files.
	 */
	public static void configureDigester(Digester digester) throws SAXException, ParserConfigurationException 
	{
		String validation = System.getProperty("jasper.reports.compile.xml.validation");
		if (validation == null || validation.length() == 0)
		{
			validation = "true";
		}
		boolean validating = Boolean.valueOf(validation).booleanValue();
		
		digester.setErrorHandler(new ErrorHandlerImpl());
		digester.setValidating(validating);
		digester.setFeature("http://xml.org/sax/features/validation", validating);
				
		/*   */
		digester.addFactoryCreate("jasperReport", JasperDesignFactory.class.getName());
		digester.addSetNext("jasperReport", "setJasperDesign", JasperDesign.class.getName());

		/*   */
		digester.addCallMethod("jasperReport/property", "setProperty", 2);
		digester.addCallParam("jasperReport/property", 0, "name");
		digester.addCallParam("jasperReport/property", 1, "value");

		/*   */
		digester.addCallMethod("jasperReport/import", "addImport", 1);
		digester.addCallParam("jasperReport/import", 0, "value");

		/*   */
		digester.addFactoryCreate("jasperReport/reportFont", JRReportFontFactory.class.getName());
		digester.addSetNext("jasperReport/reportFont", "addFont", JRReportFont.class.getName());

		/*   */
		digester.addFactoryCreate("jasperReport/parameter", JRParameterFactory.class.getName());
		digester.addSetNext("jasperReport/parameter", "addParameter", JRParameter.class.getName());
		digester.addCallMethod("jasperReport/parameter/parameterDescription", "setDescription", 0);

		/*   */
		digester.addFactoryCreate("jasperReport/parameter/defaultValueExpression", JRDefaultValueExpressionFactory.class.getName());
		digester.addSetNext("jasperReport/parameter/defaultValueExpression", "setDefaultValueExpression", JRExpression.class.getName());
		digester.addCallMethod("jasperReport/parameter/defaultValueExpression", "setText", 0);

		/*   */
		digester.addFactoryCreate("jasperReport/queryString", JRQueryFactory.class.getName());
		digester.addSetNext("jasperReport/queryString", "setQuery", JRQuery.class.getName());
		digester.addCallMethod("jasperReport/queryString", "setText", 0);
  		
		/*   */
		digester.addFactoryCreate("jasperReport/field", JRFieldFactory.class.getName());
		digester.addSetNext("jasperReport/field", "addField", JRField.class.getName());
		digester.addCallMethod("jasperReport/field/fieldDescription", "setDescription", 0);

		/*   */
		digester.addFactoryCreate("jasperReport/variable", JRVariableFactory.class.getName());
		digester.addSetNext("jasperReport/variable", "addVariable", JRDesignVariable.class.getName());

		/*   */
		digester.addFactoryCreate("jasperReport/variable/variableExpression", JRVariableExpressionFactory.class.getName());
		digester.addSetNext("jasperReport/variable/variableExpression", "setExpression", JRExpression.class.getName());
		digester.addCallMethod("jasperReport/variable/variableExpression", "setText", 0);

		/*   */
		digester.addFactoryCreate("jasperReport/variable/initialValueExpression", JRInitialValueExpressionFactory.class.getName());
		digester.addSetNext("jasperReport/variable/initialValueExpression", "setInitialValueExpression", JRExpression.class.getName());
		digester.addCallMethod("jasperReport/variable/initialValueExpression", "setText", 0);

		/*   */
		digester.addFactoryCreate("jasperReport/group", JRGroupFactory.class.getName());
		digester.addSetNext("jasperReport/group", "addGroup", JRDesignGroup.class.getName());

		/*   */
		digester.addFactoryCreate("jasperReport/group/groupExpression", JRGroupExpressionFactory.class.getName());
		digester.addSetNext("jasperReport/group/groupExpression", "setExpression", JRExpression.class.getName());
		digester.addCallMethod("jasperReport/group/groupExpression", "setText", 0);

		/*   */
		digester.addFactoryCreate("jasperReport/background/band", JRBandFactory.class.getName());
		digester.addSetNext("jasperReport/background/band", "setBackground", JRBand.class.getName());
		digester.addFactoryCreate("jasperReport/title/band", JRBandFactory.class.getName());
		digester.addSetNext("jasperReport/title/band", "setTitle", JRBand.class.getName());
		digester.addFactoryCreate("jasperReport/pageHeader/band", JRBandFactory.class.getName());
		digester.addSetNext("jasperReport/pageHeader/band", "setPageHeader", JRBand.class.getName());
		digester.addFactoryCreate("jasperReport/columnHeader/band", JRBandFactory.class.getName());
		digester.addSetNext("jasperReport/columnHeader/band", "setColumnHeader", JRBand.class.getName());
		digester.addFactoryCreate("jasperReport/group/groupHeader/band", JRBandFactory.class.getName());
		digester.addSetNext("jasperReport/group/groupHeader/band", "setGroupHeader", JRBand.class.getName());
		digester.addFactoryCreate("jasperReport/detail/band", JRBandFactory.class.getName());
		digester.addSetNext("jasperReport/detail/band", "setDetail", JRBand.class.getName());
		digester.addFactoryCreate("jasperReport/group/groupFooter/band", JRBandFactory.class.getName());
		digester.addSetNext("jasperReport/group/groupFooter/band", "setGroupFooter", JRBand.class.getName());
		digester.addFactoryCreate("jasperReport/columnFooter/band", JRBandFactory.class.getName());
		digester.addSetNext("jasperReport/columnFooter/band", "setColumnFooter", JRBand.class.getName());
		digester.addFactoryCreate("jasperReport/pageFooter/band", JRBandFactory.class.getName());
		digester.addSetNext("jasperReport/pageFooter/band", "setPageFooter", JRBand.class.getName());
		digester.addFactoryCreate("jasperReport/lastPageFooter/band", JRBandFactory.class.getName());
		digester.addSetNext("jasperReport/lastPageFooter/band", "setLastPageFooter", JRBand.class.getName());
		digester.addFactoryCreate("jasperReport/summary/band", JRBandFactory.class.getName());
		digester.addSetNext("jasperReport/summary/band", "setSummary", JRBand.class.getName());

		/*   */
		digester.addFactoryCreate("*/band/printWhenExpression", JRPrintWhenExpressionFactory.class.getName());
		digester.addSetNext("*/band/printWhenExpression", "setPrintWhenExpression", JRExpression.class.getName());
		digester.addCallMethod("*/band/printWhenExpression", "setText", 0);

		/*   */
		digester.addFactoryCreate("*/line", JRLineFactory.class.getName());
		digester.addSetNext("*/line", "addElement", JRDesignElement.class.getName());

		/*   */
		digester.addFactoryCreate("*/reportElement", JRElementFactory.class.getName());

		/*   */
		digester.addFactoryCreate("*/reportElement/printWhenExpression", JRPrintWhenExpressionFactory.class.getName());
		digester.addSetNext("*/reportElement/printWhenExpression", "setPrintWhenExpression", JRExpression.class.getName());
		digester.addCallMethod("*/reportElement/printWhenExpression", "setText", 0);

		/*   */
		digester.addFactoryCreate("*/graphicElement", JRGraphicElementFactory.class.getName());

		/*   */
		digester.addFactoryCreate("*/rectangle", JRRectangleFactory.class.getName());
		digester.addSetNext("*/rectangle", "addElement", JRDesignElement.class.getName());

		/*   */
		digester.addFactoryCreate("*/ellipse", JREllipseFactory.class.getName());
		digester.addSetNext("*/ellipse", "addElement", JRDesignElement.class.getName());

		/*   */
		digester.addFactoryCreate("*/image", JRImageFactory.class.getName());
		digester.addSetNext("*/image", "addElement", JRDesignElement.class.getName());

		/*   */
		digester.addFactoryCreate("*/box", JRBoxFactory.class.getName());
		digester.addSetNext("*/box", "setBox", JRBox.class.getName());

		/*   */
		digester.addFactoryCreate("*/image/imageExpression", JRImageExpressionFactory.class.getName());
		digester.addSetNext("*/image/imageExpression", "setExpression", JRExpression.class.getName());
		digester.addCallMethod("*/image/imageExpression", "setText", 0);

		/*   */
		digester.addFactoryCreate("*/staticText", JRStaticTextFactory.class.getName());
		digester.addSetNext("*/staticText", "addElement", JRDesignElement.class.getName());
		digester.addCallMethod("*/staticText/text", "setText", 0);


		/*   */
		digester.addFactoryCreate("*/textElement", JRTextElementFactory.class.getName());

		/*   */
		digester.addFactoryCreate("*/textElement/font", JRFontFactory.class.getName());
		digester.addSetNext("*/textElement/font", "setFont", JRFont.class.getName());

		/*   */
		digester.addFactoryCreate("*/textField", JRTextFieldFactory.class.getName());
		digester.addSetNext("*/textField", "addElement", JRDesignElement.class.getName());

		/*   */
		digester.addFactoryCreate("*/textField/textFieldExpression", JRTextFieldExpressionFactory.class.getName());
		digester.addSetNext("*/textField/textFieldExpression", "setExpression", JRExpression.class.getName());
		digester.addCallMethod("*/textField/textFieldExpression", "setText", 0);

		/*   */
		digester.addFactoryCreate("*/anchorNameExpression", JRAnchorNameExpressionFactory.class.getName());
		digester.addSetNext("*/anchorNameExpression", "setAnchorNameExpression", JRExpression.class.getName());
		digester.addCallMethod("*/anchorNameExpression", "setText", 0);
		digester.addFactoryCreate("*/hyperlinkReferenceExpression", JRHyperlinkReferenceExpressionFactory.class.getName());
		digester.addSetNext("*/hyperlinkReferenceExpression", "setHyperlinkReferenceExpression", JRExpression.class.getName());
		digester.addCallMethod("*/hyperlinkReferenceExpression", "setText", 0);
		digester.addFactoryCreate("*/hyperlinkAnchorExpression", JRHyperlinkAnchorExpressionFactory.class.getName());
		digester.addSetNext("*/hyperlinkAnchorExpression", "setHyperlinkAnchorExpression", JRExpression.class.getName());
		digester.addCallMethod("*/hyperlinkAnchorExpression", "setText", 0);
		digester.addFactoryCreate("*/hyperlinkPageExpression", JRHyperlinkPageExpressionFactory.class.getName());
		digester.addSetNext("*/hyperlinkPageExpression", "setHyperlinkPageExpression", JRExpression.class.getName());
		digester.addCallMethod("*/hyperlinkPageExpression", "setText", 0);

		/*   */
		digester.addFactoryCreate("*/subreport", JRSubreportFactory.class.getName());
		digester.addSetNext("*/subreport", "addElement", JRDesignElement.class.getName());

		/*   */
		digester.addFactoryCreate("*/subreport/subreportParameter", JRSubreportParameterFactory.class.getName());
		digester.addSetNext("*/subreport/subreportParameter", "addParameter", JRSubreportParameter.class.getName());

		/*   */
		digester.addFactoryCreate("*/subreport/parametersMapExpression", JRParametersMapExpressionFactory.class.getName());
		digester.addSetNext("*/subreport/parametersMapExpression", "setParametersMapExpression", JRExpression.class.getName());
		digester.addCallMethod("*/subreport/parametersMapExpression", "setText", 0);

		/*   */
		digester.addFactoryCreate("*/subreport/subreportParameter/subreportParameterExpression", JRSubreportParameterExpressionFactory.class.getName());
		digester.addSetNext("*/subreport/subreportParameter/subreportParameterExpression", "setExpression", JRExpression.class.getName());
		digester.addCallMethod("*/subreport/subreportParameter/subreportParameterExpression", "setText", 0);

		/*   */
		digester.addFactoryCreate("*/subreport/connectionExpression", JRConnectionExpressionFactory.class.getName());
		digester.addSetNext("*/subreport/connectionExpression", "setConnectionExpression", JRExpression.class.getName());
		digester.addCallMethod("*/subreport/connectionExpression", "setText", 0);

		/*   */
		digester.addFactoryCreate("*/subreport/dataSourceExpression", JRDataSourceExpressionFactory.class.getName());
		digester.addSetNext("*/subreport/dataSourceExpression", "setDataSourceExpression", JRExpression.class.getName());
		digester.addCallMethod("*/subreport/dataSourceExpression", "setText", 0);

		/*   */
		digester.addFactoryCreate("*/subreport/subreportExpression", JRSubreportExpressionFactory.class.getName());
		digester.addSetNext("*/subreport/subreportExpression", "setExpression", JRExpression.class.getName());
		digester.addCallMethod("*/subreport/subreportExpression", "setText", 0);

		/*   */
		digester.addFactoryCreate("*/elementGroup", JRElementGroupFactory.class.getName());
		digester.addSetNext("*/elementGroup", "addElementGroup", JRDesignElementGroup.class.getName());

		addChartRules(digester);
	}


	/**
	 * 
	 */
	private static void addChartRules(Digester digester)
	{
		digester.addFactoryCreate("*/dataset", JRDatasetFactory.class.getName());
		digester.addFactoryCreate("*/plot", JRChartPlotFactory.class.getName());

		digester.addFactoryCreate("*/chart", JRChartFactory.class.getName());
		digester.addFactoryCreate("*/chart/chartTitle", JRChartFactory.JRChartTitleFactory.class.getName());
		digester.addFactoryCreate("*/chart/chartTitle/font", JRFontFactory.class.getName());
		digester.addSetNext("*/chart/chartTitle/font", "setTitleFont", JRFont.class.getName());
		digester.addFactoryCreate("*/chart/chartTitle/titleExpression", JRChartFactory.JRTitleExpressionFactory.class);
		digester.addSetNext("*/chart/chartTitle/titleExpression", "setTitleExpression", JRDesignExpression.class.getName());
		digester.addCallMethod("*/chart/chartTitle/titleExpression", "setText", 0);
		digester.addFactoryCreate("*/chart/chartSubtitle", JRChartFactory.JRChartTitleFactory.class.getName());
		digester.addFactoryCreate("*/chart/chartSubtitle/font", JRFontFactory.class.getName());
		digester.addSetNext("*/chart/chartSubtitle/font", "setSubtitleFont", JRFont.class.getName());
		digester.addFactoryCreate("*/chart/chartSubtitle/subtitleExpression", JRChartFactory.JRTitleExpressionFactory.class);
		digester.addSetNext("*/chart/chartSubtitle/subtitleExpression", "setSubtitleExpression", JRDesignExpression.class.getName());
		digester.addCallMethod("*/chart/chartSubtitle/subtitleExpression", "setText", 0);

		// pie charts
		digester.addFactoryCreate("*/pieChart", JRPieChartFactory.class.getName());
		digester.addSetNext("*/pieChart", "addElement", JRDesignElement.class.getName());
		digester.addFactoryCreate("*/pieChart/pieDataset", JRPieDatasetFactory.class.getName());
		digester.addFactoryCreate("*/pieChart/piePlot", JRPiePlotFactory.class.getName());

		digester.addFactoryCreate("*/pieChart/pieDataset/keyExpression", JRPieDatasetFactory.JRKeyExpressionFactory.class);
		digester.addSetNext("*/pieChart/pieDataset/keyExpression", "setKeyExpression", JRDesignExpression.class.getName());
		digester.addCallMethod("*/pieChart/pieDataset/keyExpression", "setText", 0);

		digester.addFactoryCreate("*/pieChart/pieDataset/labelExpression", JRPieDatasetFactory.JRLabelExpressionFactory.class);
		digester.addSetNext("*/pieChart/pieDataset/labelExpression", "setLabelExpression", JRDesignExpression.class.getName());
		digester.addCallMethod("*/pieChart/pieDataset/labelExpression", "setText", 0);

		digester.addFactoryCreate("*/pieChart/pieDataset/valueExpression", JRPieDatasetFactory.JRValueExpressionFactory.class);
		digester.addSetNext("*/pieChart/pieDataset/valueExpression", "setValueExpression", JRDesignExpression.class.getName());
		digester.addCallMethod("*/pieChart/pieDataset/valueExpression", "setText", 0);
	}
	
	
	/**
	 * Creates a new instance of digester. The created digester is ready for 
	 * parsing report definition files.
	 */	
	public static JRXmlDigester createDigester() throws ParserConfigurationException, SAXException
	{
		JRXmlDigester digester = new JRXmlDigester();
		configureDigester(digester);
		return digester;
	}
	

	/**
	 * 
	 */
	private static class ErrorHandlerImpl implements ErrorHandler 
	{
		/**
		 * @see org.xml.sax.ErrorHandler#error(org.xml.sax.SAXParseException)
		 */
		public void error(SAXParseException exception) throws SAXException 
		{
			throw exception;
		}

		/**
		 * @see org.xml.sax.ErrorHandler#fatalError(org.xml.sax.SAXParseException)
		 */
		public void fatalError(SAXParseException exception) throws SAXException 
		{
			throw exception;
		}

		/**
		 * @see org.xml.sax.ErrorHandler#warning(org.xml.sax.SAXParseException)
		 */
		public void warning(SAXParseException exception) throws SAXException 
		{
			throw exception;
		}
	}
	
}
