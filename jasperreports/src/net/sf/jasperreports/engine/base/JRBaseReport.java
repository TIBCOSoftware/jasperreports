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
package net.sf.jasperreports.engine.base;

import java.io.Serializable;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import net.sf.jasperreports.engine.JRBand;
import net.sf.jasperreports.engine.JRConstants;
import net.sf.jasperreports.engine.JRDataset;
import net.sf.jasperreports.engine.JRExpressionCollector;
import net.sf.jasperreports.engine.JRField;
import net.sf.jasperreports.engine.JRGroup;
import net.sf.jasperreports.engine.JRParameter;
import net.sf.jasperreports.engine.JRPropertiesMap;
import net.sf.jasperreports.engine.JRQuery;
import net.sf.jasperreports.engine.JRReport;
import net.sf.jasperreports.engine.JRReportFont;
import net.sf.jasperreports.engine.JRReportTemplate;
import net.sf.jasperreports.engine.JRSortField;
import net.sf.jasperreports.engine.JRStyle;
import net.sf.jasperreports.engine.JRVariable;


/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id$
 */
public class JRBaseReport implements JRReport, Serializable
{


	/**
	 *
	 */
	private static final long serialVersionUID = JRConstants.SERIAL_VERSION_UID;

	/**
	 *
	 */
	protected String name = null;
	protected String language = LANGUAGE_JAVA;
	protected int columnCount = 1;
	protected byte printOrder = PRINT_ORDER_VERTICAL;
	protected int pageWidth = 595;
	protected int pageHeight = 842;
	protected byte orientation = ORIENTATION_PORTRAIT;
	protected byte whenNoDataType = WHEN_NO_DATA_TYPE_NO_PAGES;
	protected int columnWidth = 555;
	protected int columnSpacing = 0;
	protected int leftMargin = 20;
	protected int rightMargin = 20;
	protected int topMargin = 30;
	protected int bottomMargin = 30;
	protected boolean isTitleNewPage = false;
	protected boolean isSummaryNewPage = false;
	protected boolean isFloatColumnFooter = false;
	protected boolean ignorePagination = false;

	/**
	 *
	 */
	protected String formatFactoryClass = null;

	/**
	 *
	 */
	protected Set importsSet = null;

	/**
	 * Report templates.
	 */
	protected JRReportTemplate[] templates;

	protected JRReportFont defaultFont = null;
	protected JRReportFont[] fonts = null;
	protected JRStyle defaultStyle = null;
	protected JRStyle[] styles = null;

	/**
	 * The main dataset of the report.
	 */
	protected JRDataset mainDataset;

	/**
	 * Sub datasets of the report.
	 */
	protected JRDataset[] datasets;

	protected JRBand background = null;
	protected JRBand title = null;
	protected JRBand pageHeader = null;
	protected JRBand columnHeader = null;
	protected JRBand detail = null;
	protected JRBand columnFooter = null;
	protected JRBand pageFooter = null;
	protected JRBand lastPageFooter = null;
	protected JRBand summary = null;
	protected JRBand noData = null;


	/**
	 *
	 */
	public JRBaseReport()
	{
	}

	/**
	 * Constructs a copy of a report.
	 *
	 * @param report the original report
	 * @param expressionCollector expression collector used to provide new expression IDs
	 */
	public JRBaseReport(JRReport report, JRExpressionCollector expressionCollector)
	{
		/*   */
		name = report.getName();
		language = report.getLanguage();
		columnCount = report.getColumnCount();
		printOrder = report.getPrintOrder();
		pageWidth = report.getPageWidth();
		pageHeight = report.getPageHeight();
		orientation = report.getOrientation();
		whenNoDataType = report.getWhenNoDataType();
		columnWidth = report.getColumnWidth();
		columnSpacing = report.getColumnSpacing();
		leftMargin = report.getLeftMargin();
		rightMargin = report.getRightMargin();
		topMargin = report.getTopMargin();
		bottomMargin = report.getBottomMargin();
		isTitleNewPage = report.isTitleNewPage();
		isSummaryNewPage = report.isSummaryNewPage();
		isFloatColumnFooter = report.isFloatColumnFooter();
		ignorePagination = report.isIgnorePagination();

		formatFactoryClass = report.getFormatFactoryClass();

		/*   */
		String[] imports = report.getImports();
		if (imports != null && imports.length > 0)
		{
			importsSet = new HashSet(imports.length);
			importsSet.addAll(Arrays.asList(imports));
		}

		/*   */
		JRBaseObjectFactory factory = new JRBaseObjectFactory(this, expressionCollector);

		copyTemplates(report, factory);

		/*   */
		defaultFont = factory.getReportFont(report.getDefaultFont());

		/*   */
		JRReportFont[] jrFonts = report.getFonts();
		if (jrFonts != null && jrFonts.length > 0)
		{
			fonts = new JRReportFont[jrFonts.length];
			for(int i = 0; i < fonts.length; i++)
			{
				fonts[i] = factory.getReportFont(jrFonts[i]);
			}
		}

		/*   */
		defaultStyle = factory.getStyle(report.getDefaultStyle());

		/*   */
		JRStyle[] jrStyles = report.getStyles();
		if (jrStyles != null && jrStyles.length > 0)
		{
			styles = new JRStyle[jrStyles.length];
			for(int i = 0; i < styles.length; i++)
			{
				styles[i] = factory.getStyle(jrStyles[i]);
			}
		}

		mainDataset = factory.getDataset(report.getMainDataset());

		JRDataset[] datasetArray = report.getDatasets();
		if (datasetArray != null && datasetArray.length > 0)
		{
			datasets = new JRDataset[datasetArray.length];
			for (int i = 0; i < datasets.length; i++)
			{
				datasets[i] = factory.getDataset(datasetArray[i]);
			}
		}

		background = factory.getBand(report.getBackground());
		title = factory.getBand(report.getTitle());
		pageHeader = factory.getBand(report.getPageHeader());
		columnHeader = factory.getBand(report.getColumnHeader());
		detail = factory.getBand(report.getDetail());
		columnFooter = factory.getBand(report.getColumnFooter());
		pageFooter = factory.getBand(report.getPageFooter());
		lastPageFooter = factory.getBand(report.getLastPageFooter());
		summary = factory.getBand(report.getSummary());
		noData = factory.getBand(report.getNoData());
	}


	protected void copyTemplates(JRReport report, JRBaseObjectFactory factory)
	{
		JRReportTemplate[] reportTemplates = report.getTemplates();
		if (reportTemplates == null || reportTemplates.length == 0)
		{
			templates = null;
		}
		else
		{
			templates = new JRReportTemplate[reportTemplates.length];
			for (int i = 0; i < reportTemplates.length; i++)
			{
				templates[i] = factory.getReportTemplate(reportTemplates[i]);
			}
		}
	}

	public JRBaseReport(JRReport report)
	{
		this(report, null);
	}


	/**
	 *
	 */
	public String getName()
	{
		return name;
	}

	/**
	 *
	 */
	public String getLanguage()
	{
		return language;
	}

	/**
	 *
	 */
	public int getColumnCount()
	{
		return columnCount;
	}

	/**
	 *
	 */
	public byte getPrintOrder()
	{
		return printOrder;
	}

	/**
	 *
	 */
	public int getPageWidth()
	{
		return pageWidth;
	}

	/**
	 *
	 */
	public int getPageHeight()
	{
		return pageHeight;
	}

	/**
	 *
	 */
	public byte getOrientation()
	{
		return orientation;
	}

	/**
	 *
	 */
	public byte getWhenNoDataType()
	{
		return whenNoDataType;
	}

	/**
	 *
	 */
	public void setWhenNoDataType(byte whenNoDataType)
	{
		this.whenNoDataType = whenNoDataType;
	}

	/**
	 *
	 */
	public int getColumnWidth()
	{
		return columnWidth;
	}

	/**
	 *
	 */
	public int getColumnSpacing()
	{
		return columnSpacing;
	}

	/**
	 *
	 */
	public int getLeftMargin()
	{
		return leftMargin;
	}

	/**
	 *
	 */
	public int getRightMargin()
	{
		return rightMargin;
	}

	/**
	 *
	 */
	public int getTopMargin()
	{
		return topMargin;
	}

	/**
	 *
	 */
	public int getBottomMargin()
	{
		return bottomMargin;
	}

	/**
	 *
	 */
	public boolean isTitleNewPage()
	{
		return isTitleNewPage;
	}

	/**
	 *
	 */
	public boolean isSummaryNewPage()
	{
		return isSummaryNewPage;
	}

	/**
	 *
	 */
	public boolean isFloatColumnFooter()
	{
		return isFloatColumnFooter;
	}

	/**
	 *
	 */
	public String getScriptletClass()
	{
		return mainDataset.getScriptletClass();
	}

	/**
	 *
	 */
	public String getFormatFactoryClass()
	{
		return formatFactoryClass;
	}

	/**
	 *
	 */
	public String getResourceBundle()
	{
		return mainDataset.getResourceBundle();
	}

	/**
	 *
	 */
	public String[] getPropertyNames()
	{
		return mainDataset.getPropertiesMap().getPropertyNames();
	}

	/**
	 *
	 */
	public String getProperty(String propName)
	{
		return mainDataset.getPropertiesMap().getProperty(propName);
	}

	/**
	 *
	 */
	public void setProperty(String propName, String value)
	{
		mainDataset.getPropertiesMap().setProperty(propName, value);
	}

	/**
	 *
	 */
	public void removeProperty(String propName)
	{
		mainDataset.getPropertiesMap().removeProperty(propName);
	}

	/**
	 *
	 */
	public String[] getImports()
	{
		if (importsSet != null)
		{
			return (String[])importsSet.toArray(new String[importsSet.size()]);
		}
		return null;
	}

	/**
	 * @deprecated
	 */
	public JRReportFont getDefaultFont()
	{
		return defaultFont;
	}

	/**
	 * @deprecated
	 */
	public JRReportFont[] getFonts()
	{
		return fonts;
	}

	/**
	 *
	 */
	public JRStyle getDefaultStyle()
	{
		return defaultStyle;
	}

	/**
	 *
	 */
	public JRStyle[] getStyles()
	{
		return styles;
	}

	/**
	 * Gets an array of report parameters (including built-in ones).
	 */
	public JRParameter[] getParameters()
	{
		return mainDataset.getParameters();
	}

	/**
	 *
	 */
	public JRQuery getQuery()
	{
		return mainDataset.getQuery();
	}

	/**
	 *  Gets an array of report fields.
	 */
	public JRField[] getFields()
	{
		return mainDataset.getFields();
	}

	/**
	 *  Gets an array of sort report fields.
	 */
	public JRSortField[] getSortFields()
	{
		return mainDataset.getSortFields();
	}

	/**
	 * Gets an array of report variables.
	 */
	public JRVariable[] getVariables()
	{
		return mainDataset.getVariables();
	}

	/**
	 *
	 */
	public JRGroup[] getGroups()
	{
		return mainDataset.getGroups();
	}

	/**
	 *
	 */
	public JRBand getBackground()
	{
		return background;
	}

	/**
	 *
	 */
	public JRBand getTitle()
	{
		return title;
	}

	/**
	 *
	 */
	public JRBand getPageHeader()
	{
		return pageHeader;
	}

	/**
	 *
	 */
	public JRBand getColumnHeader()
	{
		return columnHeader;
	}

	/**
	 *
	 */
	public JRBand getDetail()
	{
		return detail;
	}

	/**
	 *
	 */
	public JRBand getColumnFooter()
	{
		return columnFooter;
	}

	/**
	 *
	 */
	public JRBand getPageFooter()
	{
		return pageFooter;
	}

	/**
	 *
	 */
	public JRBand getLastPageFooter()
	{
		return lastPageFooter;
	}

	/**
	 *
	 */
	public JRBand getSummary()
	{
		return summary;
	}


	/**
	 *
	 */
	public byte getWhenResourceMissingType()
	{
		return mainDataset.getWhenResourceMissingType();
	}

	/**
	 *
	 */
	public void setWhenResourceMissingType(byte whenResourceMissingType)
	{
		mainDataset.setWhenResourceMissingType(whenResourceMissingType);
	}


	public JRDataset getMainDataset()
	{
		return mainDataset;
	}


	public JRDataset[] getDatasets()
	{
		return datasets;
	}


	public boolean isIgnorePagination()
	{
		return ignorePagination;
	}

	public JRPropertiesMap getPropertiesMap()
	{
		return mainDataset.getPropertiesMap();
	}

	public JRReportTemplate[] getTemplates()
	{
		return templates;
	}

	/**
	 * @return the noData
	 */
	public JRBand getNoData() {
		return noData;
	}
}
