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

package net.sf.jasperreports.view;

import java.util.Locale;
import java.util.ResourceBundle;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperPrint;


/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id$
 */
public class JRDesignViewer extends JRViewer
{

	/** Creates new form JRDesignViewer */
	public JRDesignViewer(String fileName, JasperPrint jrPrint) throws JRException
	{
		this(fileName, jrPrint, null);
	}
	
	/** Creates new form JRDesignViewer */
	public JRDesignViewer(String fileName, JasperPrint jrPrint, boolean isXmlFile) throws JRException
	{
		this(fileName, jrPrint, isXmlFile, null);
	}

	/** Creates new form JRDesignViewer */
	public JRDesignViewer(String fileName, JasperPrint jrPrint, Locale locale) throws JRException
	{
		this(fileName, jrPrint, locale, null);
	}

	/** Creates new form JRDesignViewer */
	public JRDesignViewer(String fileName, JasperPrint jrPrint, boolean isXmlFile, Locale locale) throws JRException
	{
		this(fileName, jrPrint, isXmlFile, locale, null);
	}

	/** Creates new form JRDesignViewer */
	public JRDesignViewer(String fileName, JasperPrint jrPrint, boolean isXmlFile, Locale locale, ResourceBundle resBundle) throws JRException
	{
		super(jrPrint, locale, resBundle);
		loadReport(fileName, jrPrint, isXmlFile);
		hideUnusedComponents();
	}

	/** Creates new form JRDesignViewer */
	public JRDesignViewer(String fileName, JasperPrint jrPrint, Locale locale, ResourceBundle resBundle) throws JRException
	{
		this(fileName, jrPrint, false, locale, resBundle);
	}

	private void hideUnusedComponents()
	{
		btnFirst.setVisible(false);
		btnLast.setVisible(false);
		btnPrevious.setVisible(false);	
		btnNext.setVisible(false);
		txtGoTo.setVisible(false);
		pnlStatus.setVisible(false);
		btnReload.setEnabled(isXML);
	}
	
	/**
	 * 
	 */
	protected void loadReport(String fileName, JasperPrint jrPrint, boolean isXmlFile)
	{
		jasperPrint = jrPrint;
		reportFileName = fileName;
		isXML = isXmlFile;
		isPreview = true;
	}
}
