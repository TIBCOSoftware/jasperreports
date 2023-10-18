/*
 * JasperReports - Free Java Reporting Library.
 * Copyright (C) 2001 - 2023 Cloud Software Group, Inc. All rights reserved.
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
package net.sf.jasperreports.barcode4j;

import org.apache.commons.digester.Digester;

import net.sf.jasperreports.engine.JRExpression;
import net.sf.jasperreports.engine.component.XmlDigesterConfigurer;
import net.sf.jasperreports.engine.type.EvaluationTimeEnum;
import net.sf.jasperreports.engine.xml.JRExpressionFactory;
import net.sf.jasperreports.engine.xml.JRXmlConstants;
import net.sf.jasperreports.engine.xml.XmlConstantPropertyRule;

/**
 * XML digester for built-in component implementations.
 * 
 * @author Lucian Chirita (lucianc@users.sourceforge.net)
 * @see Barcode4JExtensionsRegistryFactory
 */
public class Barcode4JXmlDigesterConfigurer implements XmlDigesterConfigurer
{
	
	private static final String[] BARCODE4J_IGNORED_PROPERTIES = {
			JRXmlConstants.ATTRIBUTE_evaluationTime,
			"orientation",
			"textPosition"};

	private static final String[] QRCODE_IGNORED_PROPERTIES = {
			JRXmlConstants.ATTRIBUTE_evaluationTime,
			"errorCorrectionLevel"};
	
	@Override
	public void configureDigester(Digester digester)
	{
		addBarcode4jRules(digester);
	}

	protected void addBarcode4jRules(Digester digester)
	{
		addBaseBarcode4jRules(digester, 
				"*/componentElement/Codabar", 
				CodabarComponent.class);
		addBaseBarcode4jRules(digester, 
				"*/componentElement/Code128", 
				Code128Component.class);
		addBaseBarcode4jRules(digester, 
				"*/componentElement/EAN128", 
				EAN128Component.class);
		addTemplateRules(digester, 
				"*/componentElement/EAN128");
		addBaseBarcode4jRules(digester, 
				"*/componentElement/DataMatrix", 
				DataMatrixComponent.class);
		addBaseBarcode4jRules(digester, 
				"*/componentElement/RoyalMailCustomer", 
				RoyalMailCustomerComponent.class);
		addBaseBarcode4jRules(digester, 
				"*/componentElement/USPSIntelligentMail", 
				USPSIntelligentMailComponent.class);
		addBaseBarcode4jRules(digester, 
				"*/componentElement/Code39", Code39Component.class);
		addBaseBarcode4jRules(digester, 
				"*/componentElement/Interleaved2Of5", Interleaved2Of5Component.class);
		addBaseBarcode4jRules(digester, 
				"*/componentElement/UPCA", UPCAComponent.class);
		addBaseBarcode4jRules(digester, 
				"*/componentElement/UPCE", UPCEComponent.class);
		addBaseBarcode4jRules(digester, 
				"*/componentElement/EAN13", EAN13Component.class);
		addBaseBarcode4jRules(digester, 
				"*/componentElement/EAN8", EAN8Component.class);
		addBaseBarcode4jRules(digester, 
				"*/componentElement/POSTNET", POSTNETComponent.class);
		addBaseBarcode4jRules(digester, 
				"*/componentElement/PDF417", PDF417Component.class);
		addQRCodeRules(digester, 
				"*/componentElement/QRCode", QRCodeComponent.class);
	}
	
	protected <T> void addBaseBarcode4jRules(Digester digester, 
			String barcodePattern, Class<T> barcodeComponentClass)
	{
		addBarcodeRules(digester, barcodePattern, barcodeComponentClass, BARCODE4J_IGNORED_PROPERTIES);
		addPatternExpressionRules(digester, barcodePattern);
		
		digester.addRule(barcodePattern, 
				new OrientationRule("orientation", "orientation"));
		digester.addRule(barcodePattern, 
				new XmlConstantPropertyRule(
						"textPosition", "textPosition",
						TextPositionEnum.values()));
	}
	
	protected <T> void addPatternExpressionRules(Digester digester, String barcodePattern)
	{
		String patternExpressionPattern = barcodePattern + "/patternExpression";
		digester.addFactoryCreate(patternExpressionPattern, 
				JRExpressionFactory.class.getName());
		digester.addCallMethod(patternExpressionPattern, "setText", 0);
		digester.addSetNext(patternExpressionPattern, "setPatternExpression", 
				JRExpression.class.getName());
	}
	
	protected <T> void addBarcodeRules(Digester digester, 
			String barcodePattern, Class<T> barcodeComponentClass,
			String[] ignoredProperties)
	{
		digester.addObjectCreate(barcodePattern, barcodeComponentClass);
		digester.addSetProperties(barcodePattern,
				//properties to be ignored by this rule
				ignoredProperties, 
				new String[0]);
		//rule to set evaluation time
		digester.addRule(barcodePattern, 
				new XmlConstantPropertyRule(
						JRXmlConstants.ATTRIBUTE_evaluationTime, "evaluationTimeValue",
						EvaluationTimeEnum.values()));
		
		String codeExpressionPattern = barcodePattern + "/codeExpression";
		digester.addFactoryCreate(codeExpressionPattern, 
				JRExpressionFactory.class.getName());
		digester.addCallMethod(codeExpressionPattern, "setText", 0);
		digester.addSetNext(codeExpressionPattern, "setCodeExpression", 
				JRExpression.class.getName());
	}

	protected <T> void addQRCodeRules(Digester digester, 
			String barcodePattern, Class<T> barcodeComponentClass)
	{
		addBarcodeRules(digester, barcodePattern, barcodeComponentClass, QRCODE_IGNORED_PROPERTIES);

		digester.addRule(barcodePattern, 
				new XmlConstantPropertyRule(
						"errorCorrectionLevel", "errorCorrectionLevel",
						ErrorCorrectionLevelEnum.values()));
	}
	
	protected void addTemplateRules(Digester digester, String barcodePattern)
	{
		String templateExpressionPattern = barcodePattern + "/templateExpression";
		digester.addFactoryCreate(templateExpressionPattern, 
				JRExpressionFactory.class.getName());
		digester.addCallMethod(templateExpressionPattern, "setText", 0);
		digester.addSetNext(templateExpressionPattern, "setTemplateExpression", 
				JRExpression.class.getName());
	}

}
