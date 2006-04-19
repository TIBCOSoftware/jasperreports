/*
 * ============================================================================
 * GNU Lesser General Public License
 * ============================================================================
 *
 * JasperReports - Free Java report-generating library.
 * Copyright (C) 2001-2006 JasperSoft Corporation http://www.jaspersoft.com
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
 * 303 Second Street, Suite 450 North
 * San Francisco, CA 94107
 * http://www.jaspersoft.com
 */
package net.sf.jasperreports.view.save;

import java.io.File;
import java.text.MessageFormat;

import javax.swing.JOptionPane;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.export.JRCsvExporter;
import net.sf.jasperreports.view.JRSaveContributor;

/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id$
 */
public class JRCsvSaveContributor extends JRSaveContributor
{

	/**
	 * 
	 */
	private static final String EXTENSION_CSV = ".csv"; 
	public static final JRCsvSaveContributor INSTANCE = new JRCsvSaveContributor(); 

	/**
	 * 
	 */
	public static JRCsvSaveContributor getInstance()
	{
		return INSTANCE;
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
		return file.getName().toLowerCase().endsWith(EXTENSION_CSV);
	}

	/**
	 * 
	 */
	public String getDescription()
	{
		return "CSV (*.csv)";
	}

	/**
	 * 
	 */
	public void save(JasperPrint jasperPrint, File file) throws JRException
	{
		if (!file.getName().endsWith(EXTENSION_CSV))
		{
			file = new File(file.getAbsolutePath() + EXTENSION_CSV);
		}
		
		if (
			!file.exists() ||
			JOptionPane.OK_OPTION == 
				JOptionPane.showConfirmDialog(
					null, 
					MessageFormat.format(
						java.util.ResourceBundle.getBundle("net/sf/jasperreports/view/viewer").getString("file.exists"),
						new Object[]{file.getName()}
						), 
					java.util.ResourceBundle.getBundle("net/sf/jasperreports/view/viewer").getString("save"), 
					JOptionPane.OK_CANCEL_OPTION
					)
			)
		{
			JRCsvExporter exporter = new JRCsvExporter();
			exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint); 
			exporter.setParameter(JRExporterParameter.OUTPUT_FILE, file);
			exporter.exportReport();
		}
	}

}
