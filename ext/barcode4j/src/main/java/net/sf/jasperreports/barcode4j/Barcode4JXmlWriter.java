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

import java.io.IOException;

import net.sf.jasperreports.components.AbstractComponentXmlWriter;
import net.sf.jasperreports.engine.JRComponentElement;
import net.sf.jasperreports.engine.JasperReportsContext;
import net.sf.jasperreports.engine.component.Component;
import net.sf.jasperreports.engine.xml.JRXmlWriter;

/**
 * XML writer for built-in component implementations.
 * 
 * @author Lucian Chirita (lucianc@users.sourceforge.net)
 * @see Barcode4JExtensionsRegistryFactory
 */
public class Barcode4JXmlWriter extends AbstractComponentXmlWriter
{
	/**
	 * 
	 */
	public Barcode4JXmlWriter(JasperReportsContext jasperReportsContext)
	{
		super(jasperReportsContext);
	}

	@Override
	public void writeToXml(JRComponentElement componentElement, JRXmlWriter reportWriter) throws IOException
	{
		Component component = componentElement.getComponent();
		if (component instanceof BarcodeComponent)
		{
			BarcodeXmlWriter barcodeWriter = new BarcodeXmlWriter(
													reportWriter, 
													componentElement, 
													getVersion(jasperReportsContext, componentElement, reportWriter), 
													versionComparator);
			barcodeWriter.writeBarcode();
		}
	}

	@Override
	public boolean isToWrite(JRComponentElement componentElement, JRXmlWriter reportWriter) 
	{
//		ComponentKey componentKey = componentElement.getComponentKey();
//		if (ComponentsExtensionsRegistryFactory.NAMESPACE.equals(componentKey.getNamespace()))
//		{
//			if(ComponentsExtensionsRegistryFactory.SORT_COMPONENT_NAME.equals(componentKey.getName())
//					|| ComponentsExtensionsRegistryFactory.MAP_COMPONENT_NAME.equals(componentKey.getName()))
//			{
//				return isNewerVersionOrEqual(componentElement, reportWriter, JRConstants.VERSION_4_1_1);
//			}
//			else if(ComponentsExtensionsRegistryFactory.SPIDERCHART_COMPONENT_NAME.equals(componentKey.getName()))
//			{
//				return isNewerVersionOrEqual(componentElement, reportWriter, JRConstants.VERSION_3_7_4);
//			}
//			else if(ComponentsExtensionsRegistryFactory.TABLE_COMPONENT_NAME.equals(componentKey.getName()))
//			{
//				return isNewerVersionOrEqual(componentElement, reportWriter, JRConstants.VERSION_3_7_2);
//			}
//			else if(ComponentsExtensionsRegistryFactory.LIST_COMPONENT_NAME.equals(componentKey.getName()))
//			{
//				return isNewerVersionOrEqual(componentElement, reportWriter, JRConstants.VERSION_3_5_1);
//			}
//			else if(ComponentsExtensionsRegistryFactory.BARBECUE_COMPONENT_NAME.equals(componentKey.getName())
//					|| isBarcode4jName(componentKey.getName()))
//			{
//				return isNewerVersionOrEqual(componentElement, reportWriter, JRConstants.VERSION_3_5_2);
//			}
//		}

		return true;
	}
	
	
	protected boolean isBarcode4jName(String name)
	{
		for (String barcode4jName : Barcode4JExtensionsRegistryFactory.BARCODE4J_COMPONENT_NAMES)
		{
			if(barcode4jName.equals(name)){
				return true;
			}
		}
		return false;
	}
}
