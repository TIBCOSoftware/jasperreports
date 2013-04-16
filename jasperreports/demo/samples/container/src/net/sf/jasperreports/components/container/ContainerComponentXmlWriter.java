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
package net.sf.jasperreports.components.container;

import java.io.IOException;

import net.sf.jasperreports.components.ComponentsXmlWriter;
import net.sf.jasperreports.engine.JRComponentElement;
import net.sf.jasperreports.engine.JRConstants;
import net.sf.jasperreports.engine.JasperReportsContext;
import net.sf.jasperreports.engine.component.Component;
import net.sf.jasperreports.engine.component.ComponentKey;
import net.sf.jasperreports.engine.component.ComponentXmlWriter;
import net.sf.jasperreports.engine.component.ComponentsEnvironment;
import net.sf.jasperreports.engine.util.JRXmlWriteHelper;
import net.sf.jasperreports.engine.util.VersionComparator;
import net.sf.jasperreports.engine.util.XmlNamespace;
import net.sf.jasperreports.engine.xml.JRXmlWriter;

/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id: TextInputComponentXmlWriter.java 5922 2013-02-19 11:03:27Z teodord $
 */
public class ContainerComponentXmlWriter implements ComponentXmlWriter 
{
	private final JasperReportsContext jasperReportsContext;
	private final VersionComparator versionComparator;
	
	/**
	 * 
	 */
	public ContainerComponentXmlWriter(JasperReportsContext jasperReportsContext)
	{
		this.jasperReportsContext = jasperReportsContext;
		this.versionComparator = new VersionComparator();
	}


	public boolean isToWrite(JRComponentElement componentElement, JRXmlWriter reportWriter) 
	{
		String version = ComponentsXmlWriter.getVersion(jasperReportsContext, componentElement, reportWriter);
		return isNewerVersionOrEqual(version, JRConstants.VERSION_4_1_1);
	}
	
	
	public void writeToXml(JRComponentElement componentElement, JRXmlWriter reportWriter) throws IOException 
	{
		Component component = componentElement.getComponent();
		if (component instanceof ContainerComponent) {
			ContainerComponent containerComponent = (ContainerComponent) component;
			ComponentKey componentKey = componentElement.getComponentKey();
			writeContainerComponent(containerComponent, componentKey, reportWriter);
		}
	}
	
	protected void writeContainerComponent(ContainerComponent containerComponent, ComponentKey componentKey,
			JRXmlWriter reportWriter) throws IOException {
		JRXmlWriteHelper writer = reportWriter.getXmlWriteHelper();
		
		String namespaceURI = componentKey.getNamespace();
		String schemaLocation = 
			ComponentsEnvironment.getInstance(jasperReportsContext).getBundle(namespaceURI).getXmlParser().getPublicSchemaLocation();
		XmlNamespace componentNamespace = new XmlNamespace(namespaceURI, componentKey.getNamespacePrefix(),
				schemaLocation);
		
		writer.startElement("container", componentNamespace);
		
		writer.addAttribute("multiLine", containerComponent.isMultiLine());

		reportWriter.writeTextField(containerComponent.getTextField());

		writer.closeElement();
	}

	protected boolean isNewerVersionOrEqual(String currentVersion, String oldVersion) 
	{
		return versionComparator.compare(currentVersion, oldVersion) >= 0;
	}
	
}
