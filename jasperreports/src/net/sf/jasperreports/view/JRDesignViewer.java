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

import java.io.InputStream;
import java.util.Collection;
import java.util.Locale;
import java.util.ResourceBundle;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRReport;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.design.JRValidationException;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.engine.xml.JRXmlLoader;


/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id$
 */
public class JRDesignViewer extends JRViewer
{

	private boolean isXML;
	
	/** Creates new form JRDesignViewer */
	public JRDesignViewer(String fileName, boolean isXML) throws JRException
	{
		this(fileName, isXML, null);
	}


	/** Creates new form JRDesignViewer */
	public JRDesignViewer(InputStream is, boolean isXML) throws JRException
	{
		this(is, isXML, null);
	}


	/** Creates new form JRDesignViewer */
	public JRDesignViewer(JasperPrint jrPrint)
	{
		this(jrPrint, null);
	}
	
	/** Creates new form JRDesignViewer */
	public JRDesignViewer(JasperPrint jrPrint, boolean isXML)
	{
		this(jrPrint, isXML, null);
	}

	
	/** Creates new form JRDesignViewer */
	public JRDesignViewer(String fileName, boolean isXML, Locale locale) throws JRException
	{
		this(fileName, isXML, locale, null);
	}


	/** Creates new form JRDesignViewer */
	public JRDesignViewer(InputStream is, boolean isXML, Locale locale) throws JRException
	{
		this(is, isXML, locale, null);
	}


	/** Creates new form JRDesignViewer */
	public JRDesignViewer(JasperPrint jrPrint, Locale locale)
	{
		this(jrPrint, locale, null);
	}

	/** Creates new form JRDesignViewer */
	public JRDesignViewer(JasperPrint jrPrint, boolean isXML, Locale locale)
	{
		this(jrPrint, isXML, locale, null);
	}

	
	/** Creates new form JRDesignViewer */
	public JRDesignViewer(String fileName, boolean isXML, Locale locale, ResourceBundle resBundle) throws JRException
	{
		super(fileName, isXML, locale, resBundle);
		hideUnusedComponents();
	}


	/** Creates new form JRDesignViewer */
	public JRDesignViewer(InputStream is, boolean isXML, Locale locale, ResourceBundle resBundle) throws JRException
	{
		super(is, isXML, locale, resBundle);
		hideUnusedComponents();
	}


	/** Creates new form JRDesignViewer */
	public JRDesignViewer(JasperPrint jrPrint, boolean isXML, Locale locale, ResourceBundle resBundle)
	{
		this(jrPrint, locale, resBundle);
		loadReport(jrPrint, isXML);
	}
	

	/** Creates new form JRDesignViewer */
	public JRDesignViewer(JasperPrint jrPrint, Locale locale, ResourceBundle resBundle)
	{
		super(jrPrint, locale, resBundle);
		hideUnusedComponents();
	}

	private void hideUnusedComponents()
	{
		btnFirst.setVisible(false);
		btnLast.setVisible(false);
		btnPrevious.setVisible(false);	
		btnNext.setVisible(false);
		txtGoTo.setVisible(false);
		pnlStatus.setVisible(false);
	}
	
	/**
	 * 
	 */
	protected void loadReport(String fileName, boolean isXmlReport) throws JRException
	{
		
		if (fileName != null && fileName.endsWith(".jrxml"))
		{
			JasperDesign jasperDesign = JRXmlLoader.load(fileName);
			setReport(jasperDesign);
		}
		else
		{
			setReport((JRReport) JRLoader.loadObject(fileName));
		}
		// useful when reloading preview
		isXML = isXmlReport;
		reportFileName = fileName;
	}


	/**
	 * 
	 */
	protected void loadReport(InputStream is, boolean isXmlReport) throws JRException
	{
		if (isXmlReport)
		{
			JasperDesign jasperDesign = JRXmlLoader.load(is);
			setReport(jasperDesign);
		}
		else
		{
			setReport((JRReport) JRLoader.loadObject(is));
		}
		//useful when reloading preview
		isXML = isXmlReport;
	}


	/**
	 * 
	 */
	protected void loadReport(JasperPrint jrPrint, boolean isXmlFile)
	{
		jasperPrint = jrPrint;
		if(isXmlFile)
			reportFileName = jasperPrint.getName()+".jrxml";
	}
	
	/**
	 * 
	 */
	protected void loadReport(JasperPrint jrPrint)
	{
		jasperPrint = jrPrint;
		if(isXML)
			reportFileName = jasperPrint.getName()+".jrxml";
	}
 
	private void setReport(JRReport report) throws JRException
	{
		if (report instanceof JasperDesign)
		{
			verifyDesign((JasperDesign) report);
		}
		jasperPrint = new JRPreviewBuilder(report).getJasperPrint();
	}
	
	/**
	 * 
	 */
	private void verifyDesign(JasperDesign jasperDesign) throws JRException
	{
		/*   */
		Collection brokenRules = JasperCompileManager.verifyDesign(jasperDesign);
		if (brokenRules != null && brokenRules.size() > 0)
		{
			throw new JRValidationException(brokenRules);
		}
	}
}
