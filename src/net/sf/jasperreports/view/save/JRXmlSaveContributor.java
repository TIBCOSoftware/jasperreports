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
package net.sf.jasperreports.view.save;

import java.io.File;
import java.text.MessageFormat;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.swing.JOptionPane;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.export.JRXmlExporter;
import net.sf.jasperreports.engine.export.JRXmlExporterParameter;
import net.sf.jasperreports.view.JRSaveContributor;

/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id$
 */
public class JRXmlSaveContributor extends JRSaveContributor
{

	/**
	 * 
	 */
	private static final String EXTENSION_XML = ".xml"; 
	private static final String EXTENSION_JRPXML = ".jrpxml"; 

	/**
	 * 
	 */
	public JRXmlSaveContributor(Locale locale, ResourceBundle resBundle)
	{
		super(locale, resBundle);
	}
	
	/**
	 * 
	 */
	public boolean accept(File file)
	{
		if (file.isDirectory())
		{
			return true;
		}
		String name = file.getName().toLowerCase();
		return (name.endsWith(EXTENSION_XML) || name.endsWith(EXTENSION_JRPXML));
	}

	/**
	 * 
	 */
	public String getDescription()
	{
		return getBundleString("file.desc.xml");
	}

	/**
	 * 
	 */
	public void save(JasperPrint jasperPrint, File file) throws JRException
	{
		if (
			!file.getName().toLowerCase().endsWith(EXTENSION_XML)
			&& !file.getName().toLowerCase().endsWith(EXTENSION_JRPXML)
			)
		{
			file = new File(file.getAbsolutePath() + EXTENSION_JRPXML);
		}
			
		if (
			!file.exists() ||
			JOptionPane.OK_OPTION == 
				JOptionPane.showConfirmDialog(
					null, 
					MessageFormat.format(
						getBundleString("file.exists"),
						new Object[]{file.getName()}
						), 
					getBundleString("save"), 
					JOptionPane.OK_CANCEL_OPTION
					)
			)
		{
			JRXmlExporter exporter = new JRXmlExporter();
			exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint); 
			exporter.setParameter(JRExporterParameter.OUTPUT_FILE, file);
			exporter.setParameter(JRXmlExporterParameter.IS_EMBEDDING_IMAGES, Boolean.FALSE);
			exporter.exportReport(); 
		}
	}

}
