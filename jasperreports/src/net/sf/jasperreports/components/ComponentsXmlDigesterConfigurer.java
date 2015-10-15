/*
 * JasperReports - Free Java Reporting Library.
 * Copyright (C) 2001 - 2014 TIBCO Software Inc. All rights reserved.
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
package net.sf.jasperreports.components;

import net.sf.jasperreports.components.barbecue.StandardBarbecueComponent;
import net.sf.jasperreports.components.barcode4j.CodabarComponent;
import net.sf.jasperreports.components.barcode4j.Code128Component;
import net.sf.jasperreports.components.barcode4j.Code39Component;
import net.sf.jasperreports.components.barcode4j.DataMatrixComponent;
import net.sf.jasperreports.components.barcode4j.EAN128Component;
import net.sf.jasperreports.components.barcode4j.EAN13Component;
import net.sf.jasperreports.components.barcode4j.EAN8Component;
import net.sf.jasperreports.components.barcode4j.ErrorCorrectionLevelEnum;
import net.sf.jasperreports.components.barcode4j.Interleaved2Of5Component;
import net.sf.jasperreports.components.barcode4j.OrientationEnum;
import net.sf.jasperreports.components.barcode4j.PDF417Component;
import net.sf.jasperreports.components.barcode4j.POSTNETComponent;
import net.sf.jasperreports.components.barcode4j.QRCodeComponent;
import net.sf.jasperreports.components.barcode4j.RoyalMailCustomerComponent;
import net.sf.jasperreports.components.barcode4j.TextPositionEnum;
import net.sf.jasperreports.components.barcode4j.UPCAComponent;
import net.sf.jasperreports.components.barcode4j.UPCEComponent;
import net.sf.jasperreports.components.barcode4j.USPSIntelligentMailComponent;
import net.sf.jasperreports.components.iconlabel.IconLabelComponentDigester;
import net.sf.jasperreports.components.items.Item;
import net.sf.jasperreports.components.items.ItemData;
import net.sf.jasperreports.components.items.ItemDataXmlFactory;
import net.sf.jasperreports.components.items.ItemDatasetFactory;
import net.sf.jasperreports.components.items.ItemProperty;
import net.sf.jasperreports.components.items.ItemPropertyXmlFactory;
import net.sf.jasperreports.components.items.ItemXmlFactory;
import net.sf.jasperreports.components.list.DesignListContents;
import net.sf.jasperreports.components.list.StandardListComponent;
import net.sf.jasperreports.components.map.MapXmlFactory;
import net.sf.jasperreports.components.sort.SortComponentDigester;
import net.sf.jasperreports.components.spiderchart.SpiderChartDigester;
import net.sf.jasperreports.components.table.DesignCell;
import net.sf.jasperreports.components.table.StandardColumn;
import net.sf.jasperreports.components.table.StandardColumnGroup;
import net.sf.jasperreports.components.table.StandardGroupCell;
import net.sf.jasperreports.components.table.StandardTableFactory;
import net.sf.jasperreports.components.table.TableReportContextXmlRule;
import net.sf.jasperreports.engine.JRElementDataset;
import net.sf.jasperreports.engine.JRExpression;
import net.sf.jasperreports.engine.component.XmlDigesterConfigurer;
import net.sf.jasperreports.engine.type.EvaluationTimeEnum;
import net.sf.jasperreports.engine.type.PrintOrderEnum;
import net.sf.jasperreports.engine.type.RotationEnum;
import net.sf.jasperreports.engine.xml.JRExpressionFactory;
import net.sf.jasperreports.engine.xml.JRXmlConstants;
import net.sf.jasperreports.engine.xml.JRXmlWriter;
import net.sf.jasperreports.engine.xml.StyleContainerRule;
import net.sf.jasperreports.engine.xml.UuidPropertyRule;
import net.sf.jasperreports.engine.xml.XmlConstantPropertyRule;

import org.apache.commons.digester.Digester;

/**
 * XML digester for built-in component implementations.
 * 
 * @author Lucian Chirita (lucianc@users.sourceforge.net)
 * @see ComponentsExtensionsRegistryFactory
 */
public class ComponentsXmlDigesterConfigurer implements XmlDigesterConfigurer
{
	public void configureDigester(Digester digester)
	{
		addListRules(digester);
		addBarbecueRules(digester);
		addBarcode4jRules(digester);
		addTableRules(digester);
		SpiderChartDigester.addSpiderChartRules(digester);
		addMapRules(digester);
		SortComponentDigester.addSortComponentRules(digester);
		IconLabelComponentDigester.addIconLabelComponentRules(digester);
	}

	protected void addListRules(Digester digester)
	{
		String listPattern = "*/componentElement/list";
		digester.addObjectCreate(listPattern, StandardListComponent.class);
		digester.addSetProperties(listPattern,
				//properties to be ignored by this rule
				new String[]{"printOrder"}, 
				new String[0]);
		digester.addRule(listPattern, new XmlConstantPropertyRule(
				"printOrder", "printOrderValue", PrintOrderEnum.values()));
		
		String listContentsPattern = listPattern + "/listContents";
		digester.addObjectCreate(listContentsPattern, DesignListContents.class);
		digester.addSetProperties(listContentsPattern);
		digester.addSetNext(listContentsPattern, "setContents");
	}

	@SuppressWarnings("deprecation")
	protected void addBarbecueRules(Digester digester)
	{
		String barcodePattern = "*/componentElement/barbecue";
		digester.addObjectCreate(barcodePattern, StandardBarbecueComponent.class);
		digester.addSetProperties(barcodePattern,
				//properties to be ignored by this rule
				new String[]{JRXmlConstants.ATTRIBUTE_evaluationTime, StandardBarbecueComponent.PROPERTY_ROTATION}, 
				new String[0]);
		digester.addRule(barcodePattern, 
				new XmlConstantPropertyRule(
						JRXmlConstants.ATTRIBUTE_evaluationTime, "evaluationTimeValue",
						EvaluationTimeEnum.values()));
		digester.addRule(barcodePattern, 
				new XmlConstantPropertyRule(
						StandardBarbecueComponent.PROPERTY_ROTATION,
						RotationEnum.values()));

		String barcodeExpressionPattern = barcodePattern + "/codeExpression";
		digester.addFactoryCreate(barcodeExpressionPattern, 
				JRExpressionFactory.StringExpressionFactory.class.getName());
		digester.addCallMethod(barcodeExpressionPattern, "setText", 0);
		digester.addSetNext(barcodeExpressionPattern, "setCodeExpression", 
				JRExpression.class.getName());

		String applicationIdentifierExpressionPattern = barcodePattern 
				+ "/applicationIdentifierExpression";
		digester.addFactoryCreate(applicationIdentifierExpressionPattern, 
				JRExpressionFactory.StringExpressionFactory.class.getName());
		digester.addCallMethod(applicationIdentifierExpressionPattern, "setText", 0);
		digester.addSetNext(applicationIdentifierExpressionPattern, 
				"setApplicationIdentifierExpression", 
				JRExpression.class.getName());
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
		addBarcodeRules(digester, barcodePattern, barcodeComponentClass);
		addPatternExpressionRules(digester, barcodePattern);
		
		digester.addRule(barcodePattern, 
				new XmlConstantPropertyRule(
						"orientation", "orientationValue",
						OrientationEnum.values()));
		digester.addRule(barcodePattern, 
				new XmlConstantPropertyRule(
						"textPosition", "textPositionValue",
						TextPositionEnum.values()));
	}
	
	@SuppressWarnings("deprecation")
	protected <T> void addPatternExpressionRules(Digester digester, String barcodePattern)
	{
		String patternExpressionPattern = barcodePattern + "/patternExpression";
		digester.addFactoryCreate(patternExpressionPattern, 
				JRExpressionFactory.StringExpressionFactory.class.getName());
		digester.addCallMethod(patternExpressionPattern, "setText", 0);
		digester.addSetNext(patternExpressionPattern, "setPatternExpression", 
				JRExpression.class.getName());
	}
	
	@SuppressWarnings("deprecation")
	protected <T> void addBarcodeRules(Digester digester, 
			String barcodePattern, Class<T> barcodeComponentClass)
	{
		digester.addObjectCreate(barcodePattern, barcodeComponentClass);
		digester.addSetProperties(barcodePattern,
				//properties to be ignored by this rule
				new String[]{
					JRXmlConstants.ATTRIBUTE_evaluationTime,
					"orientation",
					"textPosition",
					"errorCorrectionLevel"
					}, 
				new String[0]);
		//rule to set evaluation time
		digester.addRule(barcodePattern, 
				new XmlConstantPropertyRule(
						JRXmlConstants.ATTRIBUTE_evaluationTime, "evaluationTimeValue",
						EvaluationTimeEnum.values()));
		
		String codeExpressionPattern = barcodePattern + "/codeExpression";
		digester.addFactoryCreate(codeExpressionPattern, 
				JRExpressionFactory.StringExpressionFactory.class.getName());
		digester.addCallMethod(codeExpressionPattern, "setText", 0);
		digester.addSetNext(codeExpressionPattern, "setCodeExpression", 
				JRExpression.class.getName());
	}

	protected <T> void addQRCodeRules(Digester digester, 
			String barcodePattern, Class<T> barcodeComponentClass)
	{
		addBarcodeRules(digester, barcodePattern, barcodeComponentClass);

		digester.addRule(barcodePattern, 
				new XmlConstantPropertyRule(
						"errorCorrectionLevel", "errorCorrectionLevel",
						ErrorCorrectionLevelEnum.values()));
	}
	
	@SuppressWarnings("deprecation")
	protected void addTemplateRules(Digester digester, String barcodePattern)
	{
		String templateExpressionPattern = barcodePattern + "/templateExpression";
		digester.addFactoryCreate(templateExpressionPattern, 
				JRExpressionFactory.StringExpressionFactory.class.getName());
		digester.addCallMethod(templateExpressionPattern, "setText", 0);
		digester.addSetNext(templateExpressionPattern, "setTemplateExpression", 
				JRExpression.class.getName());
	}

	@SuppressWarnings("deprecation")
	protected void addMapRules(Digester digester)
	{
		String mapPattern = "*/componentElement/map";
		digester.addFactoryCreate(mapPattern, MapXmlFactory.class);

		String latitudeExpressionPattern = mapPattern + "/latitudeExpression";
		digester.addFactoryCreate(latitudeExpressionPattern, 
				JRExpressionFactory.class.getName());
		digester.addCallMethod(latitudeExpressionPattern, "setText", 0);
		digester.addSetNext(latitudeExpressionPattern, "setLatitudeExpression", 
				JRExpression.class.getName());

		String longitudeExpressionPattern = mapPattern + "/longitudeExpression";
		digester.addFactoryCreate(longitudeExpressionPattern, 
				JRExpressionFactory.class.getName());
		digester.addCallMethod(longitudeExpressionPattern, "setText", 0);
		digester.addSetNext(longitudeExpressionPattern, "setLongitudeExpression", 
				JRExpression.class.getName());
		
		String addressExpressionPattern = mapPattern + "/addressExpression";
		digester.addFactoryCreate(addressExpressionPattern, 
				JRExpressionFactory.class.getName());
		digester.addCallMethod(addressExpressionPattern, "setText", 0);
		digester.addSetNext(addressExpressionPattern, "setAddressExpression", 
				JRExpression.class.getName());
		
		String zoomExpressionPattern = mapPattern + "/zoomExpression";
		digester.addFactoryCreate(zoomExpressionPattern, 
				JRExpressionFactory.class.getName());
		digester.addCallMethod(zoomExpressionPattern, "setText", 0);
		digester.addSetNext(zoomExpressionPattern, "setZoomExpression", 
				JRExpression.class.getName());
		
		String languageExpressionPattern = mapPattern + "/languageExpression";
		digester.addFactoryCreate(languageExpressionPattern, 
				JRExpressionFactory.class.getName());
		digester.addCallMethod(languageExpressionPattern, "setText", 0);
		digester.addSetNext(languageExpressionPattern, "setLanguageExpression", 
				JRExpression.class.getName());

		String componentNamespace = digester.getRuleNamespaceURI();
		String jrNamespace = JRXmlConstants.JASPERREPORTS_NAMESPACE;

		String markerDatasetPattern = mapPattern + "/markerDataset";
		digester.addFactoryCreate(markerDatasetPattern, net.sf.jasperreports.components.map.MarkerDatasetXmlFactory.class.getName());
		digester.addSetNext(markerDatasetPattern, "setMarkerDataset", net.sf.jasperreports.components.map.MarkerDataset.class.getName());

		String markerPattern = markerDatasetPattern + "/marker";
		digester.addFactoryCreate(markerPattern, net.sf.jasperreports.components.map.MarkerXmlFactory.class.getName());
		digester.addSetNext(markerPattern, "addMarker", net.sf.jasperreports.components.map.Marker.class.getName());

		String markerPropertyPattern = markerPattern + "/markerProperty";
		digester.addFactoryCreate(markerPropertyPattern, net.sf.jasperreports.components.map.MarkerPropertyXmlFactory.class.getName());
		digester.addSetNext(markerPropertyPattern, "addMarkerProperty", net.sf.jasperreports.components.map.MarkerProperty.class.getName());

		digester.setRuleNamespaceURI(jrNamespace);
		
		String markerPropertyValueExpressionPattern = markerPropertyPattern + "/" + JRXmlConstants.ELEMENT_valueExpression;
		digester.addFactoryCreate(markerPropertyValueExpressionPattern, JRExpressionFactory.class.getName());
		digester.addCallMethod(markerPropertyValueExpressionPattern, "setText", 0);
		digester.addSetNext(markerPropertyValueExpressionPattern, "setValueExpression", JRExpression.class.getName());
		
		digester.setRuleNamespaceURI(componentNamespace);
		
		String markerDataPattern = mapPattern + "/markerData";
		digester.addFactoryCreate(markerDataPattern, ItemDataXmlFactory.class.getName());
		digester.addSetNext(markerDataPattern, "addMarkerData", ItemData.class.getName());
		
		addItemRules(digester, markerDataPattern + "/item", jrNamespace);
		
		digester.setRuleNamespaceURI(jrNamespace);
		digester.addFactoryCreate(markerDataPattern + "/dataset", ItemDatasetFactory.class.getName());
		digester.addSetNext(markerDataPattern + "/dataset", "setDataset", JRElementDataset.class.getName());
		
		digester.setRuleNamespaceURI(componentNamespace);
		
		String pathStylePattern = mapPattern + "/pathStyle";
		digester.addFactoryCreate(pathStylePattern, ItemDataXmlFactory.class.getName());
		digester.addSetNext(pathStylePattern, "addPathStyle", ItemData.class.getName());
		
		addItemRules(digester, pathStylePattern + "/item", jrNamespace);
		
		digester.setRuleNamespaceURI(jrNamespace);
		digester.addFactoryCreate(pathStylePattern + "/dataset", ItemDatasetFactory.class.getName());
		digester.addSetNext(pathStylePattern + "/dataset", "setDataset", JRElementDataset.class.getName());
		
		digester.setRuleNamespaceURI(componentNamespace);
		
		String pathDataPattern = mapPattern + "/pathData";
		digester.addFactoryCreate(pathDataPattern, ItemDataXmlFactory.class.getName());
		digester.addSetNext(pathDataPattern, "addPathData", ItemData.class.getName());

		addItemRules(digester, pathDataPattern + "/item", jrNamespace);

		digester.setRuleNamespaceURI(jrNamespace);
		digester.addFactoryCreate(pathDataPattern + "/dataset", ItemDatasetFactory.class.getName());
		digester.addSetNext(pathDataPattern + "/dataset", "setDataset", JRElementDataset.class.getName());
		
		digester.setRuleNamespaceURI(componentNamespace);
		
	}

	protected void addItemRules(Digester digester, String itemPattern, String namespace)
	{
		digester.addFactoryCreate(itemPattern, ItemXmlFactory.class.getName());
		digester.addSetNext(itemPattern, "addItem", Item.class.getName());

		String locationItemPropertyPattern = itemPattern + "/itemProperty";
		digester.addFactoryCreate(locationItemPropertyPattern, ItemPropertyXmlFactory.class.getName());
		digester.addSetNext(locationItemPropertyPattern, "addItemProperty", ItemProperty.class.getName());

		digester.setRuleNamespaceURI(namespace);
		
		String locationItemPropertyValueExpressionPattern = locationItemPropertyPattern + "/" + JRXmlConstants.ELEMENT_valueExpression;
		digester.addFactoryCreate(locationItemPropertyValueExpressionPattern, JRExpressionFactory.class.getName());
		digester.addCallMethod(locationItemPropertyValueExpressionPattern, "setText", 0);
		digester.addSetNext(locationItemPropertyValueExpressionPattern, "setValueExpression", JRExpression.class.getName());
	}

	
	@SuppressWarnings("deprecation")
	protected void addTableRules(Digester digester)
	{
		String tablePattern = "*/componentElement/table";
		//digester.addObjectCreate(tablePattern, StandardTable.class);
		digester.addFactoryCreate(tablePattern, StandardTableFactory.class.getName());
		
		String columnPattern = "*/column";
		digester.addObjectCreate(columnPattern, StandardColumn.class);
		digester.addSetNext(columnPattern, "addColumn");
		digester.addSetProperties(columnPattern,
				//properties to be ignored by this rule
				new String[]{"uuid"}, 
				new String[0]);
		digester.addRule(columnPattern, new UuidPropertyRule("uuid", "UUID"));
		addExpressionRules(digester, columnPattern + "/printWhenExpression", 
				JRExpressionFactory.BooleanExpressionFactory.class, "setPrintWhenExpression",
				true);
		addTableCellRules(digester, columnPattern + "/tableHeader", "setTableHeader");
		addTableCellRules(digester, columnPattern + "/tableFooter", "setTableFooter");
		addTableGroupCellRules(digester, columnPattern + "/groupHeader", "addGroupHeader");
		addTableGroupCellRules(digester, columnPattern + "/groupFooter", "addGroupFooter");
		addTableCellRules(digester, columnPattern + "/columnHeader", "setColumnHeader");
		addTableCellRules(digester, columnPattern + "/columnFooter", "setColumnFooter");
		addTableCellRules(digester, columnPattern + "/detailCell", "setDetailCell");
		
		String columnGroupPattern = "*/columnGroup";
		digester.addObjectCreate(columnGroupPattern, StandardColumnGroup.class);
		digester.addSetNext(columnGroupPattern, "addColumn");
		digester.addSetProperties(columnGroupPattern,
				//properties to be ignored by this rule
				new String[]{"uuid"}, 
				new String[0]);
		digester.addRule(columnGroupPattern, new UuidPropertyRule("uuid", "UUID"));
		addExpressionRules(digester, columnGroupPattern + "/printWhenExpression", 
				JRExpressionFactory.BooleanExpressionFactory.class, "setPrintWhenExpression",
				true);
		addTableCellRules(digester, columnGroupPattern + "/tableHeader", "setTableHeader");
		addTableCellRules(digester, columnGroupPattern + "/tableFooter", "setTableFooter");
		addTableGroupCellRules(digester, columnGroupPattern + "/groupHeader", "addGroupHeader");
		addTableGroupCellRules(digester, columnGroupPattern + "/groupFooter", "addGroupFooter");
		addTableCellRules(digester, columnGroupPattern + "/columnHeader", "setColumnHeader");
		addTableCellRules(digester, columnGroupPattern + "/columnFooter", "setColumnFooter");
	}
	
	protected void addTableCellRules(Digester digester, String pattern, 
			String setNextMethod)
	{
		digester.addObjectCreate(pattern, DesignCell.class);
		digester.addSetNext(pattern, setNextMethod);
		// rule to set the context dataset name
		digester.addRule(pattern, new TableReportContextXmlRule());
		
		digester.addSetProperties(pattern,
				new String[]{JRXmlConstants.ATTRIBUTE_style}, 
				new String[0]);
		digester.addRule(pattern, new StyleContainerRule());
	}
	
	protected void addTableGroupCellRules(Digester digester, String pattern, 
			String setNextMethod)
	{
		digester.addObjectCreate(pattern, StandardGroupCell.class);
		digester.addSetProperties(pattern);
		addTableCellRules(digester, pattern + "/cell", "setCell");
		digester.addSetNext(pattern, setNextMethod);
	}

	protected <T> void addExpressionRules(Digester digester, String expressionPattern,
			Class<T> factoryClass, String setterMethod, boolean jrNamespace)
	{
		String originalNamespace = digester.getRuleNamespaceURI();
		if (jrNamespace)
		{
			digester.setRuleNamespaceURI(JRXmlWriter.JASPERREPORTS_NAMESPACE.getNamespaceURI());
		}
		
		digester.addFactoryCreate(expressionPattern, factoryClass);
		digester.addCallMethod(expressionPattern, "setText", 0);
		digester.addSetNext(expressionPattern, setterMethod,
				JRExpression.class.getName());
		
		if (jrNamespace)
		{
			digester.setRuleNamespaceURI(originalNamespace);
		}
	}
}
