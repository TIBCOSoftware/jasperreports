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

package net.sf.jasperreports.view;

import java.io.File;
import java.io.InputStream;
import java.util.Arrays;

import javax.swing.JOptionPane;

import net.sf.jasperreports.engine.DefaultJasperReportsContext;
import net.sf.jasperreports.engine.JRConstants;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRReport;
import net.sf.jasperreports.engine.JasperReportsContext;
import net.sf.jasperreports.engine.convert.ReportConverter;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.export.JRGraphics2DExporter;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.engine.util.LocalJasperReportsContext;
import net.sf.jasperreports.engine.util.SimpleFileResolver;
import net.sf.jasperreports.engine.xml.JRXmlLoader;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @deprecated Replaced by {@link net.sf.jasperreports.swing.JRDesignViewer}.
 */
public class JRDesignViewer extends JRViewer
{
	private static final Log log = LogFactory.getLog(JRDesignViewer.class);

	private static final long serialVersionUID = JRConstants.SERIAL_VERSION_UID;
	
	/**
	 * @see #JRDesignViewer(JasperReportsContext, String, boolean)
	 */
	public JRDesignViewer(String fileName, boolean isXML) throws JRException
	{
		this(DefaultJasperReportsContext.getInstance(), fileName, isXML);
	}
	
	/**
	 * @see #JRDesignViewer(JasperReportsContext, InputStream, boolean)
	 */
	public JRDesignViewer(InputStream is, boolean isXML) throws JRException
	{
		this(DefaultJasperReportsContext.getInstance(), is, isXML);
	}
	
	/**
	 * @see #JRDesignViewer(JasperReportsContext, JRReport)
	 */
	public JRDesignViewer(JRReport report) throws JRException
	{
		this(DefaultJasperReportsContext.getInstance(), report);
	}
	
	/**
	 *
	 */
	public JRDesignViewer(
		JasperReportsContext jasperReportsContext,
		String fileName, 
		boolean isXML
		) throws JRException
	{
		super(jasperReportsContext, fileName, isXML, null, null);
		hideUnusedComponents();
	}
	
	/**
	 *
	 */
	public JRDesignViewer(
		JasperReportsContext jasperReportsContext,
		InputStream is, 
		boolean isXML
		) throws JRException
	{
		super(jasperReportsContext, is, isXML, null, null);
		hideUnusedComponents();
	}
	
	/**
	 *
	 */
	public JRDesignViewer(
		JasperReportsContext jasperReportsContext,
		JRReport report
		) throws JRException
	{
		super(jasperReportsContext, new ReportConverter(jasperReportsContext, report, false).getJasperPrint(), null, null);
		//reconfigureReloadButton();
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

	void btnReloadActionPerformed(java.awt.event.ActionEvent evt)
	{
		if (this.type == TYPE_FILE_NAME)
		{
			try
			{
				loadReport(reportFileName, isXML);
				forceRefresh();
			}
			catch (JRException e)
			{
				if (log.isErrorEnabled())
				{
					log.error("Reload error.", e);
				}
				JOptionPane.showMessageDialog(this, "Error loading report design. See the log for more details.");
			}
		}
	}


	/**
	*/
	protected void loadReport(String fileName, boolean isXmlReport) throws JRException
	{
		SimpleFileResolver fileResolver = new SimpleFileResolver(Arrays.asList(new File[]{new File(fileName).getParentFile(), new File(".")}));
		fileResolver.setResolveAbsolutePath(true);
		if (localJasperReportsContext == null)
		{
			localJasperReportsContext = new LocalJasperReportsContext(jasperReportsContext);
			jasperReportsContext = localJasperReportsContext;
		}
		localJasperReportsContext.setFileResolver(fileResolver);
		
		if (isXmlReport)
		{
			JasperDesign jasperDesign = JRXmlLoader.load(fileName);
			setReport(jasperDesign);
		}
		else
		{
			setReport((JRReport) JRLoader.loadObjectFromFile(fileName));
		}
		this.type = TYPE_FILE_NAME;
		this.isXML = isXmlReport;
		this.reportFileName = fileName;
		
		//FIXME there are two more lines here in JRViewer; check other places too
	}


	/**
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
		this.type = TYPE_INPUT_STREAM;
		this.isXML = isXmlReport;
	}


	/**
	*/
	public void loadReport(JRReport rep) throws JRException
	{
		setReport(rep);
		this.type = TYPE_OBJECT;
		this.isXML = false;
	}
	
	private void setReport(JRReport report) throws JRException
	{
		this.jasperPrint = new ReportConverter(jasperReportsContext, report, false).getJasperPrint();		
	}

	/**
	*/
	protected JRGraphics2DExporter getGraphics2DExporter() throws JRException
	{
		return 
			new JRGraphics2DExporter(getJasperReportsContext())
			{
				protected void initReport()
				{
					super.initReport();
					frameDrawer.setClip(true);//FIXMENOW thick border of margin elements is clipped
				}
			};
	}
	
}
