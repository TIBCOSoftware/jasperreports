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
package dori.jasper.engine.xml;

import javax.xml.parsers.ParserConfigurationException;

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
		digester.addFactoryCreate("jasperReport", "dori.jasper.engine.xml.JasperDesignFactory");
		digester.addSetNext("jasperReport", "setJasperDesign", "dori.jasper.engine.design.JasperDesign");

		/*   */
		digester.addCallMethod("jasperReport/property", "setProperty", 2);
		digester.addCallParam("jasperReport/property", 0, "name");
		digester.addCallParam("jasperReport/property", 1, "value");

		/*   */
		digester.addFactoryCreate("jasperReport/reportFont", "dori.jasper.engine.xml.JRReportFontFactory");
		digester.addSetNext("jasperReport/reportFont", "addFont", "dori.jasper.engine.JRReportFont");

		/*   */
		digester.addFactoryCreate("jasperReport/parameter", "dori.jasper.engine.xml.JRParameterFactory");
		digester.addSetNext("jasperReport/parameter", "addParameter", "dori.jasper.engine.JRParameter");
		digester.addCallMethod("jasperReport/parameter/parameterDescription", "setDescription", 0);

		/*   */
		digester.addFactoryCreate("jasperReport/parameter/defaultValueExpression", "dori.jasper.engine.xml.JRDefaultValueExpressionFactory");
		digester.addSetNext("jasperReport/parameter/defaultValueExpression", "setDefaultValueExpression", "dori.jasper.engine.JRExpression");
		digester.addCallMethod("jasperReport/parameter/defaultValueExpression", "setText", 0);

		/*   */
		digester.addFactoryCreate("jasperReport/queryString", "dori.jasper.engine.xml.JRQueryFactory");
		digester.addSetNext("jasperReport/queryString", "setQuery", "dori.jasper.engine.JRQuery");
		digester.addCallMethod("jasperReport/queryString", "setText", 0);
  		
		/*   */
		digester.addFactoryCreate("jasperReport/field", "dori.jasper.engine.xml.JRFieldFactory");
		digester.addSetNext("jasperReport/field", "addField", "dori.jasper.engine.JRField");
		digester.addCallMethod("jasperReport/field/fieldDescription", "setDescription", 0);

		/*   */
		digester.addFactoryCreate("jasperReport/variable", "dori.jasper.engine.xml.JRVariableFactory");
		digester.addSetNext("jasperReport/variable", "addVariable", "dori.jasper.engine.design.JRDesignVariable");

		/*   */
		digester.addFactoryCreate("jasperReport/variable/variableExpression", "dori.jasper.engine.xml.JRVariableExpressionFactory");
		digester.addSetNext("jasperReport/variable/variableExpression", "setExpression", "dori.jasper.engine.JRExpression");
		digester.addCallMethod("jasperReport/variable/variableExpression", "setText", 0);

		/*   */
		digester.addFactoryCreate("jasperReport/variable/initialValueExpression", "dori.jasper.engine.xml.JRInitialValueExpressionFactory");
		digester.addSetNext("jasperReport/variable/initialValueExpression", "setInitialValueExpression", "dori.jasper.engine.JRExpression");
		digester.addCallMethod("jasperReport/variable/initialValueExpression", "setText", 0);

		/*   */
		digester.addFactoryCreate("jasperReport/group", "dori.jasper.engine.xml.JRGroupFactory");
		digester.addSetNext("jasperReport/group", "addGroup", "dori.jasper.engine.design.JRDesignGroup");

		/*   */
		digester.addFactoryCreate("jasperReport/group/groupExpression", "dori.jasper.engine.xml.JRGroupExpressionFactory");
		digester.addSetNext("jasperReport/group/groupExpression", "setExpression", "dori.jasper.engine.JRExpression");
		digester.addCallMethod("jasperReport/group/groupExpression", "setText", 0);

		/*   */
		digester.addFactoryCreate("jasperReport/background/band", "dori.jasper.engine.xml.JRBandFactory");
		digester.addSetNext("jasperReport/background/band", "setBackground", "dori.jasper.engine.JRBand");
		digester.addFactoryCreate("jasperReport/title/band", "dori.jasper.engine.xml.JRBandFactory");
		digester.addSetNext("jasperReport/title/band", "setTitle", "dori.jasper.engine.JRBand");
		digester.addFactoryCreate("jasperReport/pageHeader/band", "dori.jasper.engine.xml.JRBandFactory");
		digester.addSetNext("jasperReport/pageHeader/band", "setPageHeader", "dori.jasper.engine.JRBand");
		digester.addFactoryCreate("jasperReport/columnHeader/band", "dori.jasper.engine.xml.JRBandFactory");
		digester.addSetNext("jasperReport/columnHeader/band", "setColumnHeader", "dori.jasper.engine.JRBand");
		digester.addFactoryCreate("jasperReport/group/groupHeader/band", "dori.jasper.engine.xml.JRBandFactory");
		digester.addSetNext("jasperReport/group/groupHeader/band", "setGroupHeader", "dori.jasper.engine.JRBand");
		digester.addFactoryCreate("jasperReport/detail/band", "dori.jasper.engine.xml.JRBandFactory");
		digester.addSetNext("jasperReport/detail/band", "setDetail", "dori.jasper.engine.JRBand");
		digester.addFactoryCreate("jasperReport/group/groupFooter/band", "dori.jasper.engine.xml.JRBandFactory");
		digester.addSetNext("jasperReport/group/groupFooter/band", "setGroupFooter", "dori.jasper.engine.JRBand");
		digester.addFactoryCreate("jasperReport/columnFooter/band", "dori.jasper.engine.xml.JRBandFactory");
		digester.addSetNext("jasperReport/columnFooter/band", "setColumnFooter", "dori.jasper.engine.JRBand");
		digester.addFactoryCreate("jasperReport/pageFooter/band", "dori.jasper.engine.xml.JRBandFactory");
		digester.addSetNext("jasperReport/pageFooter/band", "setPageFooter", "dori.jasper.engine.JRBand");
		digester.addFactoryCreate("jasperReport/summary/band", "dori.jasper.engine.xml.JRBandFactory");
		digester.addSetNext("jasperReport/summary/band", "setSummary", "dori.jasper.engine.JRBand");

		/*   */
		digester.addFactoryCreate("*/band/printWhenExpression", "dori.jasper.engine.xml.JRPrintWhenExpressionFactory");
		digester.addSetNext("*/band/printWhenExpression", "setPrintWhenExpression", "dori.jasper.engine.JRExpression");
		digester.addCallMethod("*/band/printWhenExpression", "setText", 0);

		/*   */
		digester.addFactoryCreate("*/line", "dori.jasper.engine.xml.JRLineFactory");
		digester.addSetNext("*/line", "addElement", "dori.jasper.engine.design.JRDesignElement");

		/*   */
		digester.addFactoryCreate("*/reportElement", "dori.jasper.engine.xml.JRElementFactory");

		/*   */
		digester.addFactoryCreate("*/reportElement/printWhenExpression", "dori.jasper.engine.xml.JRPrintWhenExpressionFactory");
		digester.addSetNext("*/reportElement/printWhenExpression", "setPrintWhenExpression", "dori.jasper.engine.JRExpression");
		digester.addCallMethod("*/reportElement/printWhenExpression", "setText", 0);

		/*   */
		digester.addFactoryCreate("*/graphicElement", "dori.jasper.engine.xml.JRGraphicElementFactory");

		/*   */
		digester.addFactoryCreate("*/rectangle", "dori.jasper.engine.xml.JRRectangleFactory");
		digester.addSetNext("*/rectangle", "addElement", "dori.jasper.engine.design.JRDesignElement");

		/*   */
		digester.addFactoryCreate("*/ellipse", "dori.jasper.engine.xml.JREllipseFactory");
		digester.addSetNext("*/ellipse", "addElement", "dori.jasper.engine.design.JRDesignElement");

		/*   */
		digester.addFactoryCreate("*/image", "dori.jasper.engine.xml.JRImageFactory");
		digester.addSetNext("*/image", "addElement", "dori.jasper.engine.design.JRDesignElement");

		/*   */
		digester.addFactoryCreate("*/image/imageExpression", "dori.jasper.engine.xml.JRImageExpressionFactory");
		digester.addSetNext("*/image/imageExpression", "setExpression", "dori.jasper.engine.JRExpression");
		digester.addCallMethod("*/image/imageExpression", "setText", 0);

		/*   */
		digester.addFactoryCreate("*/staticText", "dori.jasper.engine.xml.JRStaticTextFactory");
		digester.addSetNext("*/staticText", "addElement", "dori.jasper.engine.design.JRDesignElement");
		digester.addCallMethod("*/staticText/text", "setText", 0);

		/*   */
		digester.addFactoryCreate("*/textElement", "dori.jasper.engine.xml.JRTextElementFactory");

		/*   */
		digester.addFactoryCreate("*/textElement/font", "dori.jasper.engine.xml.JRFontFactory");
		digester.addSetNext("*/textElement/font", "setFont", "dori.jasper.engine.JRFont");

		/*   */
		digester.addFactoryCreate("*/textField", "dori.jasper.engine.xml.JRTextFieldFactory");
		digester.addSetNext("*/textField", "addElement", "dori.jasper.engine.design.JRDesignElement");

		/*   */
		digester.addFactoryCreate("*/textField/textFieldExpression", "dori.jasper.engine.xml.JRTextFieldExpressionFactory");
		digester.addSetNext("*/textField/textFieldExpression", "setExpression", "dori.jasper.engine.JRExpression");
		digester.addCallMethod("*/textField/textFieldExpression", "setText", 0);

		/*   */
		digester.addFactoryCreate("*/anchorNameExpression", "dori.jasper.engine.xml.JRAnchorNameExpressionFactory");
		digester.addSetNext("*/anchorNameExpression", "setAnchorNameExpression", "dori.jasper.engine.JRExpression");
		digester.addCallMethod("*/anchorNameExpression", "setText", 0);
		digester.addFactoryCreate("*/hyperlinkReferenceExpression", "dori.jasper.engine.xml.JRHyperlinkReferenceExpressionFactory");
		digester.addSetNext("*/hyperlinkReferenceExpression", "setHyperlinkReferenceExpression", "dori.jasper.engine.JRExpression");
		digester.addCallMethod("*/hyperlinkReferenceExpression", "setText", 0);
		digester.addFactoryCreate("*/hyperlinkAnchorExpression", "dori.jasper.engine.xml.JRHyperlinkAnchorExpressionFactory");
		digester.addSetNext("*/hyperlinkAnchorExpression", "setHyperlinkAnchorExpression", "dori.jasper.engine.JRExpression");
		digester.addCallMethod("*/hyperlinkAnchorExpression", "setText", 0);
		digester.addFactoryCreate("*/hyperlinkPageExpression", "dori.jasper.engine.xml.JRHyperlinkPageExpressionFactory");
		digester.addSetNext("*/hyperlinkPageExpression", "setHyperlinkPageExpression", "dori.jasper.engine.JRExpression");
		digester.addCallMethod("*/hyperlinkPageExpression", "setText", 0);

		/*   */
		digester.addFactoryCreate("*/subreport", "dori.jasper.engine.xml.JRSubreportFactory");
		digester.addSetNext("*/subreport", "addElement", "dori.jasper.engine.design.JRDesignElement");

		/*   */
		digester.addFactoryCreate("*/subreport/subreportParameter", "dori.jasper.engine.xml.JRSubreportParameterFactory");
		digester.addSetNext("*/subreport/subreportParameter", "addParameter", "dori.jasper.engine.JRSubreportParameter");

		/*   */
		digester.addFactoryCreate("*/subreport/parametersMapExpression", "dori.jasper.engine.xml.JRParametersMapExpressionFactory");
		digester.addSetNext("*/subreport/parametersMapExpression", "setParametersMapExpression", "dori.jasper.engine.JRExpression");
		digester.addCallMethod("*/subreport/parametersMapExpression", "setText", 0);

		/*   */
		digester.addFactoryCreate("*/subreport/subreportParameter/subreportParameterExpression", "dori.jasper.engine.xml.JRSubreportParameterExpressionFactory");
		digester.addSetNext("*/subreport/subreportParameter/subreportParameterExpression", "setExpression", "dori.jasper.engine.JRExpression");
		digester.addCallMethod("*/subreport/subreportParameter/subreportParameterExpression", "setText", 0);

		/*   */
		digester.addFactoryCreate("*/subreport/connectionExpression", "dori.jasper.engine.xml.JRConnectionExpressionFactory");
		digester.addSetNext("*/subreport/connectionExpression", "setConnectionExpression", "dori.jasper.engine.JRExpression");
		digester.addCallMethod("*/subreport/connectionExpression", "setText", 0);

		/*   */
		digester.addFactoryCreate("*/subreport/dataSourceExpression", "dori.jasper.engine.xml.JRDataSourceExpressionFactory");
		digester.addSetNext("*/subreport/dataSourceExpression", "setDataSourceExpression", "dori.jasper.engine.JRExpression");
		digester.addCallMethod("*/subreport/dataSourceExpression", "setText", 0);

		/*   */
		digester.addFactoryCreate("*/subreport/subreportExpression", "dori.jasper.engine.xml.JRSubreportExpressionFactory");
		digester.addSetNext("*/subreport/subreportExpression", "setExpression", "dori.jasper.engine.JRExpression");
		digester.addCallMethod("*/subreport/subreportExpression", "setText", 0);

		/*   */
		digester.addFactoryCreate("*/elementGroup", "dori.jasper.engine.xml.JRElementGroupFactory");
		digester.addSetNext("*/elementGroup", "addElementGroup", "dori.jasper.engine.design.JRDesignElementGroup");
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
