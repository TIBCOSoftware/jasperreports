/*
 * ============================================================================
 * GNU Lesser General Public License
 * ============================================================================
 *
 * JasperReports - Free Java report-generating library.
 * Copyright (C) 2001-2005 JasperSoft Corporation http://www.jaspersoft.com
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
 * 185, Berry Street, Suite 6200
 * San Francisco CA 94107
 * http://www.jaspersoft.com
 */

/*
 * Contributors:
 * John Bindel - jbindel@users.sourceforge.net 
 */

package net.sf.jasperreports.engine.fill;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import java.util.Set;

import net.sf.jasperreports.engine.JRAbstractScriptlet;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRDefaultFontProvider;
import net.sf.jasperreports.engine.JRDefaultScriptlet;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExpression;
import net.sf.jasperreports.engine.JRField;
import net.sf.jasperreports.engine.JRGroup;
import net.sf.jasperreports.engine.JRParameter;
import net.sf.jasperreports.engine.JRPrintImage;
import net.sf.jasperreports.engine.JRPrintPage;
import net.sf.jasperreports.engine.JRPrintText;
import net.sf.jasperreports.engine.JRQuery;
import net.sf.jasperreports.engine.JRReport;
import net.sf.jasperreports.engine.JRReportFont;
import net.sf.jasperreports.engine.JRResultSetDataSource;
import net.sf.jasperreports.engine.JRVariable;
import net.sf.jasperreports.engine.JRVirtualizer;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.base.JRBasePrintPage;
import net.sf.jasperreports.engine.base.JRVirtualPrintPage;
import net.sf.jasperreports.engine.design.JRDefaultCompiler;
import net.sf.jasperreports.engine.util.JRClassLoader;
import net.sf.jasperreports.engine.util.JRGraphEnvInitializer;
import net.sf.jasperreports.engine.util.JRQueryExecuter;
import net.sf.jasperreports.engine.util.JRStyledTextParser;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id$
 */
public abstract class JRBaseFiller implements JRDefaultFontProvider
{

	/**
	 * Map class to be used for bound elements.
	 * <p>
	 * If per page element maps are used, the map will update the page map 
	 * when adding or clearing the bound element map. 
	 * 
	 * @author John Bindel
	 */
	public class BoundElementMap extends HashMap
	{
        private static final long serialVersionUID = 608;
        
        private final Map perPageElements;

        /**
         * Used when per page maps are not required.  The map will behave just like
         * <code>java.util.HashMap</code>.
         */
        BoundElementMap()
        {
        	super();
        	this.perPageElements = null;
        }
        
        /**
         * Used when per page map is required.
         * 
         * @param perPageElements	the page map 
         */
		BoundElementMap(Map perPageElements)
		{
			super();
			this.perPageElements = perPageElements;
		}

		/**
		 * Keep track of the objects per page for our virtualizer.
		 */
		public Object put(Object key, Object value, JRPrintPage keyPage)
		{
			if (perPageElements != null)
			{
				// Track the key element by its page.
				Map map = (Map) this.perPageElements.get(keyPage);
				if (map == null)
				{
					map = new HashMap();
					this.perPageElements.put(keyPage, map);
				}
				map.put(new Integer(System.identityHashCode(key)), key);
			}

		    return super.put(key, value);
		}
		
		/**
		 * If per page map is required, the entry will also be added for the current print page.
		 */
		public Object put(Object key, Object value)
		{
			if (perPageElements != null)
			{
				return put(key, value, printPage);
			}
			return super.put(key, value);
		}

		public void clear()
		{
			super.clear();
			
			if (perPageElements != null)
			{
				perPageElements.clear();
			}
		}
	}

	/**
	 * Keeps per page maps of bound elements.
	 * 
	 * @author John Bindel
	 */
	private class BoundElements implements JRVirtualPrintPage.IdentityDataProvider
	{
		// Each of these contains a java.util.Map of identity hash codes (Integer) to image elements.
		HashMap pageToReportImage;
		HashMap pageToPageImage;
		HashMap pageToColumnImage;
		// Contains a java.util.Map per group of identity hash codes (Integer) to image elements.
		HashMap pageToGroupImage;

		// Each of these contains a java.util.Map of identity hash codes (Integer) to text elements.
		HashMap pageToReportText;
		HashMap pageToPageText;
		HashMap pageToColumnText;
		// Contains a java.util.Map per group of identity hash codes (Integer) to text elements.
		HashMap pageToGroupText;
		
		// Each of these contains a java.util.Map of identity hash codes (Integer) to chart elements.
		HashMap pageToReportChart;
		HashMap pageToPageChart;
		HashMap pageToColumnChart;
		// Contains a java.util.Map per group of identity hash codes (Integer) to chart elements.
		HashMap pageToGroupChart;

		BoundElements()
		{
			this.pageToReportImage = new HashMap();
			this.pageToPageImage = new HashMap();
			this.pageToColumnImage = new HashMap();
			this.pageToGroupImage = new HashMap();

			this.pageToReportText = new HashMap();
			this.pageToPageText = new HashMap();
			this.pageToColumnText = new HashMap();
			this.pageToGroupText = new HashMap();
			
			this.pageToReportChart = new HashMap();
			this.pageToPageChart = new HashMap();
			this.pageToColumnChart = new HashMap();
			this.pageToGroupChart = new HashMap();
		}

		private void addElements(Set allElements, Map pageMap, JRVirtualPrintPage page)
		{
			Map map = (Map) pageMap.get(page);
			if (map != null && !map.isEmpty())
			{
				Collection elements = map.values();
				allElements.addAll(elements);
			}
		}

		private void addGroupElements(Set allElements, Map groupMap, JRVirtualPrintPage page)
		{
			for (Iterator it = groupMap.entrySet().iterator(); it.hasNext(); )
			{
				Map.Entry entry = (Map.Entry) it.next();
				addElements(allElements, (Map) entry.getValue(), page);
			}
		}

		/**
		 * Collect all of the identity data the the JRBaseFiller needs to know.
		 * <p>
		 * All the bound elements on the page are collected and transformed into
		 * identity objects.
		 * 
		 * @param page the page to get the identity data for
		 */
		public JRVirtualPrintPage.ObjectIDPair[] getIdentityData(JRVirtualPrintPage page)
		{
			Set allElements = new HashSet();
			addElements(allElements, pageToReportImage, page);
			addElements(allElements, pageToPageImage, page);
			addElements(allElements, pageToColumnImage, page);
			addGroupElements(allElements, pageToGroupImage, page);

			addElements(allElements, pageToReportText, page);
			addElements(allElements, pageToPageText, page);
			addElements(allElements, pageToColumnText, page);
			addGroupElements(allElements, pageToGroupText, page);
			
			addElements(allElements, pageToReportChart, page);
			addElements(allElements, pageToPageChart, page);
			addElements(allElements, pageToColumnChart, page);
			addGroupElements(allElements, pageToGroupChart, page);

			JRVirtualPrintPage.ObjectIDPair[] ids;

			if (!allElements.isEmpty())
			{
				Object[] objects = allElements.toArray(new Object[allElements.size()]);
				ids = new JRVirtualPrintPage.ObjectIDPair[objects.length];
				for (int i = 0; i < objects.length; ++i)
				{
					ids[i] = new JRVirtualPrintPage.ObjectIDPair(objects[i]);
				}
			}
			else
			{
				ids = null;
			}

			return ids;
		}

		private void updateIdentityData(Map pageMap, JRVirtualPrintPage page, BoundElementMap boundElements, JRVirtualPrintPage.ObjectIDPair[] identityData)
		{
			Map pageElements = (Map) pageMap.get(page);
			if (pageElements != null && pageElements.size() > 0)
			{
				for (int i = 0; i < identityData.length; ++i)
				{
					Object oldObject = pageElements.remove(new Integer(identityData[i].getIdentity()));
					if (oldObject != null)
					{
						Object resolver = boundElements.remove(oldObject);
						if (resolver != null)
						{
							// This will also add the new element into the pageElements map.
							boundElements.put(identityData[i].getObject(), resolver, page);
						}
						else
						{
							// Strange.
						}
					}
				}
			}
		}

		private void updateGroupIdentityData(Map pageGroupMap, JRVirtualPrintPage page, Map groupMap, JRVirtualPrintPage.ObjectIDPair[] identityData)
		{
			for (Iterator it = pageGroupMap.entrySet().iterator(); it.hasNext(); )
			{
				Map.Entry entry = (Map.Entry) it.next();
				updateIdentityData((Map) entry.getValue(), page,
								   (BoundElementMap) groupMap.get(entry.getKey()),
								   identityData);
			}
		}

		/**
		 * Sets the identity date for a virtualized page.
		 * <p>
		 * The identity data consists of bound elements located on the page.
		 * Pairs of identity hash code and objects are stored when the page is virtualized.
		 * When the page gets devirtualized, the original objects are substituted in the bound maps
		 * based on their identity hash code.
		 * 
		 * @param page	the virtualized page
		 * @param identityData	the identity data
		 */
		public void setIdentityData(JRVirtualPrintPage page, JRVirtualPrintPage.ObjectIDPair[] identityData)
		{
			// Update the perPageEntries, AND the normal bound maps.
			updateIdentityData(pageToReportImage, page, reportBoundImages, identityData);
			updateIdentityData(pageToPageImage, page, pageBoundImages, identityData);
			updateIdentityData(pageToColumnImage, page, columnBoundImages, identityData);
			updateGroupIdentityData(pageToGroupImage, page, groupBoundImages, identityData);

			updateIdentityData(pageToReportText, page, reportBoundTexts, identityData);
			updateIdentityData(pageToPageText, page, pageBoundTexts, identityData);
			updateIdentityData(pageToColumnText, page, columnBoundTexts, identityData);
			updateGroupIdentityData(pageToGroupText, page, groupBoundTexts, identityData);

			updateIdentityData(pageToReportChart, page, reportBoundCharts, identityData);
			updateIdentityData(pageToPageChart, page, pageBoundCharts, identityData);
			updateIdentityData(pageToColumnChart, page, columnBoundCharts, identityData);
			updateGroupIdentityData(pageToGroupChart, page, groupBoundCharts, identityData);
		}
	}

	
	/**
	 *
	 */
	private static final Log log = LogFactory.getLog(JRBaseFiller.class);

	/**
	 *
	 */
	private JRBaseFiller parentFiller = null;
	private JRStyledTextParser styledTextParser = new JRStyledTextParser();

	/**
	 *
	 */
	private boolean isInterrupted = false;

	/**
	 *
	 */
	protected String name = null;
	protected int columnCount = 1;
	protected byte printOrder = JRReport.PRINT_ORDER_VERTICAL;
	protected int pageWidth = 0;
	protected int pageHeight = 0;
	protected byte orientation = JRReport.ORIENTATION_PORTRAIT;
	protected byte whenNoDataType = JRReport.WHEN_NO_DATA_TYPE_NO_PAGES;
	protected int columnWidth = 0;
	protected int columnSpacing = 0;
	protected int leftMargin = 0;
	protected int rightMargin = 0;
	protected int topMargin = 0;
	protected int bottomMargin = 0;
	protected boolean isTitleNewPage = false;
	protected boolean isSummaryNewPage = false;
	protected boolean isFloatColumnFooter = false;
	protected String scriptletClassName = null;
	protected String resourceBundleBaseName = null;
	
	/**
	 * the resource missing handling type
	 */
	protected byte whenResourceMissingType = JRReport.WHEN_RESOURCE_MISSING_TYPE_NULL;

	protected JRReportFont defaultFont = null;
	protected JRReportFont[] fonts = null;
	protected JRFillParameter[] parameters = null;
	protected Map parametersMap = null;
	protected JRQuery query = null;
	protected JRFillField[] fields = null;
	protected Map fieldsMap = null;
	protected JRFillVariable[] variables = null;
	protected Map variablesMap = null;
	protected JRFillGroup[] groups = null;

	protected JRFillBand missingFillBand = null;
	protected JRFillBand background = null;
	protected JRFillBand title = null;
	protected JRFillBand pageHeader = null;
	protected JRFillBand columnHeader = null;
	protected JRFillBand detail = null;
	protected JRFillBand columnFooter = null;
	protected JRFillBand pageFooter = null;
	protected JRFillBand lastPageFooter = null;
	protected JRFillBand summary = null;

	protected JRCalculator calculator = null;
	protected Locale locale = null;
	protected ResourceBundle resourceBundle = null;
	protected JRAbstractScriptlet scriptlet = null;
	protected JRDataSource dataSource = null;
	protected JRVirtualizer virtualizer = null;
	protected Integer reportMaxCount = null;
	protected int reportCount = 0;

	protected List formattedTextFields = new ArrayList();
	
	protected Map loadedImages = null;
	protected Map loadedSubreports = null;
	
	/**
	 * All bound elements per page.
	 */
	private BoundElements perPageBoundElements = null;

	protected BoundElementMap reportBoundImages = null;
	protected BoundElementMap pageBoundImages = null;
	protected BoundElementMap columnBoundImages = null;
	protected Map groupBoundImages = null;

	protected BoundElementMap reportBoundCharts = null;
	protected BoundElementMap pageBoundCharts = null;
	protected BoundElementMap columnBoundCharts = null;
	protected Map groupBoundCharts = null;

	protected BoundElementMap reportBoundTexts = null;
	protected BoundElementMap pageBoundTexts = null;
	protected BoundElementMap columnBoundTexts = null;
	protected Map groupBoundTexts = null;

	/**
	 *
	 */
	protected JasperPrint jasperPrint = null;
	protected JRPrintPage printPage = null;
	protected int printPageStretchHeight = 0;

	/**
	 *
	 */
	protected boolean isParametersAlreadySet = false;

	protected JRFillChartDataset[] datasets;

	
	/**
	 *
	 */
	protected JRBaseFiller(JasperReport jasperReport, JRBaseFiller parentFiller) throws JRException
	{
		JRGraphEnvInitializer.initializeGraphEnv();
		
		/*   */
		this.parentFiller = parentFiller;

		/*   */
		name = jasperReport.getName();
		columnCount = jasperReport.getColumnCount();
		printOrder = jasperReport.getPrintOrder();
		pageWidth = jasperReport.getPageWidth();
		pageHeight = jasperReport.getPageHeight();
		orientation = jasperReport.getOrientation();
		whenNoDataType = jasperReport.getWhenNoDataType();
		columnWidth = jasperReport.getColumnWidth();
		columnSpacing = jasperReport.getColumnSpacing();
		leftMargin = jasperReport.getLeftMargin();
		rightMargin = jasperReport.getRightMargin();
		topMargin = jasperReport.getTopMargin();
		bottomMargin = jasperReport.getBottomMargin();
		isTitleNewPage = jasperReport.isTitleNewPage();
		isSummaryNewPage = jasperReport.isSummaryNewPage();
		isFloatColumnFooter = jasperReport.isFloatColumnFooter();
		scriptletClassName = jasperReport.getScriptletClass();
		resourceBundleBaseName = jasperReport.getResourceBundle();
		whenResourceMissingType = jasperReport.getWhenResourceMissingType();

		jasperPrint = new JasperPrint();

		/*   */
		JRFillObjectFactory factory = new JRFillObjectFactory(this);

		/*   */
		defaultFont = factory.getReportFont(jasperReport.getDefaultFont());

		/*   */
		JRReportFont[] jrFonts = jasperReport.getFonts();
		if (jrFonts != null && jrFonts.length > 0)
		{
			fonts = new JRReportFont[jrFonts.length];
			for(int i = 0; i < fonts.length; i++)
			{
				fonts[i] = factory.getReportFont(jrFonts[i]);
			}
		}

		/*   */
		JRParameter[] jrParameters = jasperReport.getParameters();
		if (jrParameters != null && jrParameters.length > 0)
		{
			parameters = new JRFillParameter[jrParameters.length];
			parametersMap = new HashMap();
			for(int i = 0; i < parameters.length; i++)
			{
				parameters[i] = factory.getParameter(jrParameters[i]);
				parametersMap.put(parameters[i].getName(), parameters[i]);
			}
		}

		/*   */
		query = jasperReport.getQuery();
		
		/*   */
		JRField[] jrFields = jasperReport.getFields();
		if (jrFields != null && jrFields.length > 0)
		{
			fields = new JRFillField[jrFields.length];
			fieldsMap = new HashMap();
			for(int i = 0; i < fields.length; i++)
			{
				fields[i] = factory.getField(jrFields[i]);
				fieldsMap.put(fields[i].getName(), fields[i]);
			}
		}

		/*   */
		JRVariable[] jrVariables = jasperReport.getVariables();
		if (jrVariables != null && jrVariables.length > 0)
		{
			variables = new JRFillVariable[jrVariables.length];
			variablesMap = new HashMap();
			for(int i = 0; i < variables.length; i++)
			{
				variables[i] = factory.getVariable(jrVariables[i]);
				variablesMap.put(variables[i].getName(), variables[i]);
			}
		}

		/*   */
		JRGroup[] jrGroups = jasperReport.getGroups();
		if (jrGroups != null && jrGroups.length > 0)
		{
			groups = new JRFillGroup[jrGroups.length];
			for(int i = 0; i < groups.length; i++)
			{
				groups[i] = factory.getGroup(jrGroups[i]);
			}
		}

		/*   */
		missingFillBand = factory.getBand(null);
		background = factory.getBand(jasperReport.getBackground());
		title = factory.getBand(jasperReport.getTitle());
		pageHeader = factory.getBand(jasperReport.getPageHeader());
		columnHeader = factory.getBand(jasperReport.getColumnHeader());
		detail = factory.getBand(jasperReport.getDetail());
		columnFooter = factory.getBand(jasperReport.getColumnFooter());
		pageFooter = factory.getBand(jasperReport.getPageFooter());
		lastPageFooter = factory.getBand(jasperReport.getLastPageFooter());
		summary = factory.getBand(jasperReport.getSummary());
		
		datasets = factory.getDatasets();

		/*   *
		resourceBundle = loadResourceBundle(resourceBundleBaseName);

		/*   *
		scriptlet = 
			loadScriptlet(
				scriptletClass,
				parametersMap,
				fieldsMap,
				variablesMap,
				groups
				);

		/*   *
		calculator = 
			loadCalculator(
				jasperReport,
				parametersMap,
				fieldsMap,
				variablesMap,
				variables,
				groups
				);
		*/
		
		/*   */
		scriptlet = createScriptlet();
		
		/*   */
		scriptlet.setData(
			parametersMap,
			fieldsMap,
			variablesMap,
			groups
			);

		/*   */
		calculator = new JRDefaultCompiler().loadCalculator(jasperReport);

		calculator.init(this);
	}


	/**
	 *
	 */
	public JRStyledTextParser getStyledTextParser()
	{
		return styledTextParser;
	}


	/**
	 *
	 */
	public JasperPrint getJasperPrint()
	{
		return jasperPrint;
	}


	/**
	 *
	 */
	public JRReportFont getDefaultFont()
	{
		return defaultFont;
	}


	/**
	 *
	 */
	protected boolean isSubreport()
	{
		return (parentFiller != null);
	}

	/**
	 *
	 */
	protected boolean isInterrupted()
	{
		return (isInterrupted || (parentFiller != null && parentFiller.isInterrupted()));
	}

	/**
	 *
	 */
	protected void setInterrupted(boolean isInterrupted)
	{
		this.isInterrupted = isInterrupted;
	}

	/**
	 *
	 */
	protected JRPrintPage getCurrentPage()
	{
		return printPage;
	}

	/**
	 *
	 */
	protected JRReportFont[] getFonts()
	{
		return fonts;
	}

	/**
	 *
	 */
	protected int getCurrentPageStretchHeight()
	{
		return printPageStretchHeight;
	}

	/**
	 *
	 */
	protected abstract void setPageHeight(int pageHeight);
	

	/**
	 *
	 */
	public JasperPrint fill(
		Map parameterValues, 
		Connection conn
		) throws JRException
	{
		if (parameterValues == null)
		{
			parameterValues = new HashMap();
		}

		/*   */
		setParameters(parameterValues);

		/*   */
		if (conn == null)
		{
			conn = (Connection)parameterValues.get(JRParameter.REPORT_CONNECTION);
		}
		if (conn == null)
		{
			parameterValues.remove(JRParameter.REPORT_CONNECTION);
		}
		else
		{
			parameterValues.put(JRParameter.REPORT_CONNECTION, conn);
		}
		JRFillParameter parameter = (JRFillParameter)parametersMap.get(JRParameter.REPORT_CONNECTION);
		if (parameter != null)
		{
			setParameter(parameter, conn);
		}

		if (conn == null)
		{
			if (log.isWarnEnabled())
				log.warn("The supplied java.sql.Connection object is null.");
		}

		PreparedStatement pstmt = null; 
		
		try
		{
			JRDataSource ds = null;

			pstmt = 
				JRQueryExecuter.getStatement(
					query, 
					parametersMap, 
					parameterValues, 
					conn
					);
	
			if (pstmt != null)
			{
				if (reportMaxCount != null)
				{
					pstmt.setMaxRows(reportMaxCount.intValue());
				}
				
				ResultSet rs = pstmt.executeQuery();
		
				ds = new JRResultSetDataSource(rs);
			}
		
			fill(parameterValues, ds);
		}
		catch (SQLException e)
		{
			throw new JRException("Error executing SQL statement for report : " + name, e);
		}
		finally
		{
			if (pstmt != null)
			{
				try
				{
					pstmt.close();
				}
				catch (SQLException e)
				{
				}
			}
		}
		
		return jasperPrint;
	}

	
	/**
	 *
	 */
	protected JasperPrint fill(
		Map parameterValues,
		JRDataSource ds
		) throws JRException
	{
		dataSource = ds;
		
		if (parameterValues == null)
		{
			parameterValues = new HashMap();
		}

		/*   */
		setParameters(parameterValues);

		/*   */
		if (dataSource == null)
		{
			dataSource = (JRDataSource)parameterValues.get(JRParameter.REPORT_DATA_SOURCE);
		}
		if (dataSource == null)
		{
			parameterValues.remove(JRParameter.REPORT_DATA_SOURCE);
		}
		else
		{
			parameterValues.put(JRParameter.REPORT_DATA_SOURCE, dataSource);
		}
		JRFillParameter parameter = (JRFillParameter)parametersMap.get(JRParameter.REPORT_DATA_SOURCE);
		if (parameter != null)
		{
			setParameter(parameter, dataSource);
		}

		/*   */
		parameterValues.put(JRParameter.REPORT_SCRIPTLET, scriptlet);
		parameter = (JRFillParameter)parametersMap.get(JRParameter.REPORT_SCRIPTLET);
		if (parameter != null)
		{
			setParameter(parameter, scriptlet);
		}

		/*   */
		parameterValues.put(JRParameter.REPORT_PARAMETERS_MAP, parameterValues);
		parameter = (JRFillParameter)parametersMap.get(JRParameter.REPORT_PARAMETERS_MAP);
		if (parameter != null)
		{
			setParameter(parameter, parameterValues);
		}

		
		jasperPrint.setName(name);
		jasperPrint.setPageWidth(pageWidth);
		jasperPrint.setPageHeight(pageHeight);
		jasperPrint.setOrientation(orientation);

		jasperPrint.setDefaultFont(defaultFont);

		/*   */
		if (fonts != null && fonts.length > 0)
		{
			for(int i = 0; i < fonts.length; i++)
			{
				try
				{
					jasperPrint.addFont(fonts[i]);
				}
				catch(JRException e)
				{
					//ignore font duplication exception
				}
			}
		}

		for(int i = 0; i < formattedTextFields.size(); i++)
		{
			((JRFillTextField)formattedTextFields.get(i)).setFormat();
		}

		loadedImages = new HashMap();
		loadedSubreports = new HashMap();
		
		if (perPageBoundElements == null)
		{
			// per page maps are not used
			reportBoundImages = new BoundElementMap();
			pageBoundImages = new BoundElementMap();
			columnBoundImages = new BoundElementMap();
			groupBoundImages = new HashMap();

			reportBoundTexts = new BoundElementMap();
			pageBoundTexts = new BoundElementMap();
			columnBoundTexts = new BoundElementMap();
			groupBoundTexts = new HashMap();

			reportBoundCharts = new BoundElementMap();
			pageBoundCharts = new BoundElementMap();
			columnBoundCharts = new BoundElementMap();
			groupBoundCharts = new HashMap();

			if (groups != null && groups.length > 0)
			{
				for(int i = 0; i < groups.length; i++)
				{
					groupBoundImages.put( groups[i].getName(), new BoundElementMap() );
					groupBoundTexts.put( groups[i].getName(), new BoundElementMap() );
					groupBoundCharts.put( groups[i].getName(), new BoundElementMap() );
				}
			}		    
		}
		else
		{
			// per page maps are used
			reportBoundImages = new BoundElementMap(perPageBoundElements.pageToReportImage);
			pageBoundImages = new BoundElementMap(perPageBoundElements.pageToPageImage);
			columnBoundImages = new BoundElementMap(perPageBoundElements.pageToColumnImage);
			groupBoundImages = new HashMap();

			reportBoundTexts = new BoundElementMap(perPageBoundElements.pageToReportText);
			pageBoundTexts = new BoundElementMap(perPageBoundElements.pageToPageText);
			columnBoundTexts = new BoundElementMap(perPageBoundElements.pageToColumnText);
			groupBoundTexts = new HashMap();

			reportBoundCharts = new BoundElementMap(perPageBoundElements.pageToReportChart);
			pageBoundCharts = new BoundElementMap(perPageBoundElements.pageToPageChart);
			columnBoundCharts = new BoundElementMap(perPageBoundElements.pageToColumnChart);
			groupBoundCharts = new HashMap();

			if (groups != null && groups.length > 0)
			{
				for(int i = 0; i < groups.length; i++)
				{
					String groupName = groups[i].getName();
					
					HashMap map = new HashMap();
					perPageBoundElements.pageToGroupImage.put( groupName, map );
					groupBoundImages.put(groupName, new BoundElementMap(map) );

					map = new HashMap();
					perPageBoundElements.pageToGroupText.put( groupName, map );
					groupBoundTexts.put(groupName, new BoundElementMap(map));

					map = new HashMap();
					perPageBoundElements.pageToGroupChart.put( groupName, map );
					groupBoundCharts.put(groupName, new BoundElementMap(map));
				}
			}
		}
		
		/*   */
		reportCount = 0;

		/*   */
		fillReport();
		
		return jasperPrint;
	}
	

	/**
	 *
	 */
	protected abstract void fillReport() throws JRException;


	/**
	 *
	 */
	protected ResourceBundle loadResourceBundle()
	{
		ResourceBundle tmpResourceBundle = null;

		if (resourceBundleBaseName != null)
		{
			ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
			if (classLoader != null)
			{
				try
				{
					tmpResourceBundle = 
						ResourceBundle.getBundle(
							resourceBundleBaseName,
							locale,
							classLoader
							); 
				}
				catch(MissingResourceException e)
				{
					//if (log.isWarnEnabled())
					//	log.warn("Failure using Thread.currentThread().getContextClassLoader() in JRClassLoader class. Using JRClassLoader.class.getClassLoader() instead.");
				}
			}
		
			if (tmpResourceBundle == null)
			{
				classLoader = JRClassLoader.class.getClassLoader();
				
				if (classLoader == null)
				{
					tmpResourceBundle = 
						ResourceBundle.getBundle(
							resourceBundleBaseName,
							locale
							);	        
				}
				else
				{
					tmpResourceBundle = 
						ResourceBundle.getBundle(
							resourceBundleBaseName,
							locale,
							classLoader
							); 
				}
			}
		}
		
		return tmpResourceBundle;
	}


	/**
	 *
	 */
	protected JRAbstractScriptlet createScriptlet() throws JRException
	{
		JRAbstractScriptlet tmpScriptlet = null;

		if (scriptletClassName != null)
		{
			Class clazz = null;

			try
			{
				clazz = JRClassLoader.loadClassForName(scriptletClassName);
			}
			catch (ClassNotFoundException e)
			{
				throw new JRException("Error loading scriptlet class : " + scriptletClassName, e);
			}

			try
			{
				tmpScriptlet = (JRAbstractScriptlet)clazz.newInstance();
			}
			catch (Exception e)
			{
				throw new JRException("Error creating scriptlet class instance : " + scriptletClassName, e);
			}
		}

		if (tmpScriptlet == null)
		{
			tmpScriptlet = new JRDefaultScriptlet();
		}

		return tmpScriptlet;
	}


	/**
	 *
	 *
	protected static JRCalculator loadCalculator(
		JasperReport jasperReport,
		Map parametersMap,
		Map fieldsMap,
		Map variablesMap,
		JRFillVariable[] variables,
		JRFillGroup[] groups
		) throws JRException
	{
		JRCalculator calculator = new JRDefaultCompiler().loadCalculator(jasperReport);
		
		calculator.init(
			parametersMap,
			fieldsMap,
			variablesMap,
			variables,
			groups
			);
		
		return calculator;
	}


	/**
	 *
	 */
	protected void setParameters(Map parameterValues) throws JRException
	{
		if (isParametersAlreadySet)
		{
			return;
		}
		
		/*   */
		reportMaxCount = (Integer)parameterValues.get(JRParameter.REPORT_MAX_COUNT);

		/*   */
		locale = (Locale)parameterValues.get(JRParameter.REPORT_LOCALE);
		if (locale == null)
		{
			locale = Locale.getDefault();
		}
		if (locale == null)
		{
			parameterValues.remove(JRParameter.REPORT_LOCALE);
		}
		else
		{
			parameterValues.put(JRParameter.REPORT_LOCALE, locale);
		}
		JRFillParameter parameter = (JRFillParameter)parametersMap.get(JRParameter.REPORT_LOCALE);
		if (parameter != null)
		{
			setParameter(parameter, locale);
		}

		/*   */
		resourceBundle = (ResourceBundle)parameterValues.get(JRParameter.REPORT_RESOURCE_BUNDLE);
		if (resourceBundle == null)
		{
			resourceBundle = loadResourceBundle();
		}
		if (resourceBundle == null)
		{
			parameterValues.remove(JRParameter.REPORT_RESOURCE_BUNDLE);
		}
		else
		{
			parameterValues.put(JRParameter.REPORT_RESOURCE_BUNDLE, resourceBundle);
		}
		parameter = (JRFillParameter)parametersMap.get(JRParameter.REPORT_RESOURCE_BUNDLE);
		if (parameter != null)
		{
			setParameter(parameter, resourceBundle);
		}
		
		
		/* Virtualizer */
		virtualizer = (JRVirtualizer)parameterValues.get(JRParameter.REPORT_VIRTUALIZER);
		parameter = (JRFillParameter)parametersMap.get(JRParameter.REPORT_VIRTUALIZER);
		if (parameter != null)
		{
			setParameter(parameter, virtualizer);
		}
		if (virtualizer != null)
		{
			// keep per page element maps
			perPageBoundElements = new BoundElements();
		}


		/*   */
		if (parameters != null && parameters.length > 0)
		{
			Object value = null;
			for(int i = 0; i < parameters.length; i++)
			{
				if (parameterValues.containsKey(parameters[i].getName()))
				{
					setParameter(
						parameters[i], 
						parameterValues.get(parameters[i].getName())
						);
				}
				else if (!parameters[i].isSystemDefined())
				{
					value = 
						calculator.evaluate(
							parameters[i].getDefaultValueExpression(), 
							JRExpression.EVALUATION_DEFAULT
							);
					if (value != null)
					{
						parameterValues.put(parameters[i].getName(), value);
					}
					setParameter(parameters[i], value);
				}
			}
		}

		isParametersAlreadySet = true;
	}


	/**
	 *
	 */
	protected void setParameter(JRFillParameter parameter, Object value) throws JRException
	{
		if (value != null)
		{
			if (parameter.getValueClass().isInstance(value))
			{
				parameter.setValue(value);
			}
			else
			{
				throw new JRException("Incompatible value assigned to parameter " + parameter.getName() + " : " + name);
			}
		}
		else
		{
			parameter.setValue(value);
		}
	}


	/**
	 *
	 */
	protected boolean next() throws JRException
	{
		boolean hasNext = false;
		
		if (dataSource != null)
		{
			hasNext = (reportMaxCount == null || reportMaxCount.intValue() > reportCount++)	&& dataSource.next();

			if (hasNext)
			{
				/*   */
				if (fields != null && fields.length > 0)
				{
					JRFillField field = null;
					Object objValue = null;
					for(int i = 0; i < fields.length; i++)
					{
						field = fields[i];
						objValue = dataSource.getFieldValue(field);
						field.setOldValue(field.getValue());
						field.setValue(objValue);
					}
				}

				/*   */
				if (variables != null && variables.length > 0)
				{
					JRFillVariable variable = null;
					for(int i = 0; i < variables.length; i++)
					{
						variable = variables[i];
						variable.setOldValue(variable.getValue());
					}
				}
			}
		}
		
		return hasNext;
	}
	
	
	private void resolveBoundImages(Collection images, Map boundImages, byte evaluation) throws JRException
	{ 
		if (images != null && images.size() > 0)
		{
			for(Iterator it = images.iterator(); it.hasNext();)
			{
			    JRPrintImage printImage = (JRPrintImage)it.next();
			    JRFillImage image = (JRFillImage)boundImages.get(printImage);
				
				image.evaluateImage(evaluation);

				image.copy(printImage);
			}
		}
	}

	private void resolvePerPageBoundImages(Map perPageImages, Map boundImages, byte evaluation) throws JRException
	{
		if (perPageImages != null)
		{
			for (Iterator it = perPageImages.entrySet().iterator(); it.hasNext(); )
			{
				Map.Entry entry = (Map.Entry) it.next();
				// Calling getElements() will page in the data for the page.
				((JRPrintPage) entry.getKey()).getElements();
				resolveBoundImages(((Map) entry.getValue()).values(), boundImages, evaluation);
			}
		}

		boundImages.clear();
	}

	private void resolveBoundImages(Map boundImages, byte evaluation) throws JRException
	{ 
		resolveBoundImages(boundImages.keySet(), boundImages, evaluation);

		boundImages.clear();
	}
	
	/**
	 *
	 */
	protected void resolveReportBoundImages() throws JRException
	{
		if (perPageBoundElements != null)
		{
			resolvePerPageBoundImages(perPageBoundElements.pageToReportImage, reportBoundImages, JRExpression.EVALUATION_DEFAULT);
			resolvePerPageBoundCharts(perPageBoundElements.pageToReportChart, reportBoundCharts, JRExpression.EVALUATION_DEFAULT);
		}
		else
		{
			resolveBoundImages(reportBoundImages, JRExpression.EVALUATION_DEFAULT);
			resolveBoundCharts(reportBoundCharts, JRExpression.EVALUATION_DEFAULT);
		}
	}


	/**
	 *
	 */
	protected void resolvePageBoundImages(byte evaluation) throws JRException
	{
		if (perPageBoundElements != null)
		{
			resolvePerPageBoundImages(perPageBoundElements.pageToPageImage, pageBoundImages, evaluation);
			resolvePerPageBoundCharts(perPageBoundElements.pageToPageChart, pageBoundCharts, evaluation);
		}
		else
		{
			resolveBoundImages(pageBoundImages, evaluation);
			resolveBoundCharts(pageBoundCharts, evaluation);
		}
	}


	/**
	 *
	 */
	protected void resolveColumnBoundImages(byte evaluation) throws JRException
	{
		if (perPageBoundElements != null)
		{
			resolvePerPageBoundImages(perPageBoundElements.pageToColumnImage, columnBoundImages, evaluation);
			resolvePerPageBoundCharts(perPageBoundElements.pageToColumnChart, columnBoundCharts, evaluation);
		}
		else
		{
			resolveBoundImages(columnBoundImages, evaluation);
			resolveBoundCharts(columnBoundCharts, evaluation);
		}
	}
	
	private void resolveBoundCharts(Collection charts, Map boundCharts, byte evaluation) throws JRException
	{ 
		if (charts != null && charts.size() > 0)
		{
			for(Iterator it = charts.iterator(); it.hasNext();)
			{
			    JRPrintImage printImage = (JRPrintImage)it.next();
			    JRFillChart chart = (JRFillChart)boundCharts.get(printImage);
				
				chart.evaluateImage(evaluation);

				chart.copy(printImage);
			}
		}
	}

	private void resolvePerPageBoundCharts(Map perPageCharts, Map boundCharts, byte evaluation) throws JRException
	{
		if (perPageCharts != null)
		{
			for (Iterator it = perPageCharts.entrySet().iterator(); it.hasNext(); )
			{
				Map.Entry entry = (Map.Entry) it.next();
				// Calling getElements() will page in the data for the page.
				((JRPrintPage) entry.getKey()).getElements();
				resolveBoundCharts(((Map) entry.getValue()).values(), boundCharts, evaluation);
			}
		}

		boundCharts.clear();
	}

	private void resolveBoundCharts(Map boundCharts, byte evaluation) throws JRException
	{ 
		resolveBoundCharts(boundCharts.keySet(), boundCharts, evaluation);
		
		boundCharts.clear();
	}
	

	/**
	 *
	 */
	protected void resolveGroupBoundImages(byte evaluation, boolean isFinal) throws JRException
	{
		if (groups != null && groups.length > 0)
		{
			for(int i = 0; i < groups.length; i++)
			{
				JRFillGroup group = groups[i];

				if ((group.hasChanged() && group.isFooterPrinted()) || isFinal)
				{
					String groupName = group.getName();
					
					Map specificGroupBoundImages = (Map)groupBoundImages.get(groupName);
					Map specificGroupBoundCharts = (Map)groupBoundCharts.get(groupName);
					if (perPageBoundElements != null)
					{
						resolvePerPageBoundImages((Map) perPageBoundElements.pageToGroupImage.get(groupName), specificGroupBoundImages, evaluation);
						resolvePerPageBoundCharts((Map) perPageBoundElements.pageToGroupChart.get(groupName), specificGroupBoundCharts, evaluation);
					}
					else
					{
						resolveBoundImages(specificGroupBoundImages, evaluation);
						resolveBoundCharts(specificGroupBoundCharts, evaluation);
					}
				}
			}
		}
	}

	
	private void resolveBoundTexts(Collection texts, Map boundTexts, byte evaluation) throws JRException
	{
		if (texts != null && texts.size() > 0)
		{
			for(Iterator it = texts.iterator(); it.hasNext();)
			{
			    JRPrintText text = (JRPrintText)it.next();
			    JRFillTextField textField = (JRFillTextField)boundTexts.get(text);
				
				textField.evaluateText(evaluation);

				textField.chopTextElement(0);

				textField.copy(text);
			}
		}
	}

	private void resolvePerPageBoundTexts(Map perPageTexts, Map boundTexts, byte evaluation) throws JRException
	{
		if (perPageTexts != null)
		{
			for (Iterator it = perPageTexts.entrySet().iterator(); it.hasNext(); )
			{
				Map.Entry entry = (Map.Entry) it.next();
				// Calling getElements() will page in the data for the page.
				JRPrintPage page = (JRPrintPage) entry.getKey();
				page.getElements();
				resolveBoundTexts(((Map) entry.getValue()).values(), boundTexts, evaluation);
			}
		}
		
		boundTexts.clear();
	}

	private void resolveBoundTexts(Map boundTexts, byte evaluation) throws JRException
	{ 
		resolveBoundTexts(boundTexts.keySet(), boundTexts, evaluation);
		
		boundTexts.clear();
	}

	/**
	 *
	 */
	protected void resolveReportBoundTexts() throws JRException
	{
		if (perPageBoundElements != null)
		{
			resolvePerPageBoundTexts(perPageBoundElements.pageToReportText, reportBoundTexts, JRExpression.EVALUATION_DEFAULT);
		}
		else
		{
			resolveBoundTexts(reportBoundTexts, JRExpression.EVALUATION_DEFAULT);
		}
	}


	/**
	 *
	 */
	protected void resolvePageBoundTexts(byte evaluation) throws JRException
	{
		if (perPageBoundElements != null)
		{
			resolvePerPageBoundTexts(perPageBoundElements.pageToPageText, pageBoundTexts, evaluation);
		}
		else
		{
			resolveBoundTexts(pageBoundTexts, evaluation);
		}
	}


	/**
	 *
	 */
	protected void resolveColumnBoundTexts(byte evaluation) throws JRException
	{
		if (perPageBoundElements != null)
		{
			resolvePerPageBoundTexts(perPageBoundElements.pageToColumnText, columnBoundTexts, evaluation);
		}
		else
		{
			resolveBoundTexts(columnBoundTexts, evaluation);
		}
	}


	/**
	 *
	 */
	protected void resolveGroupBoundTexts(byte evaluation, boolean isFinal) throws JRException
	{
		if (groups != null && groups.length > 0)
		{
			for(int i = 0; i < groups.length; i++)
			{
			    JRFillGroup group = groups[i];

				if ((group.hasChanged() && group.isFooterPrinted()) || isFinal)
				{
					String groupName = group.getName();
					Map specificGroupBoundTexts = (Map)groupBoundTexts.get(groupName);

					if (perPageBoundElements != null)
					{
						resolvePerPageBoundTexts((Map) perPageBoundElements.pageToGroupText.get(groupName), specificGroupBoundTexts, evaluation);
					}
					else
					{
						resolveBoundTexts(specificGroupBoundTexts, evaluation);
					}
				}
			}
		}
	}


	protected JRPrintPage newPage()
	{
		JRPrintPage page;

		if (virtualizer != null)
		{
			JRVirtualPrintPage virtualPage = new JRVirtualPrintPage(jasperPrint, virtualizer);
			virtualPage.addIdentityDataProvider(perPageBoundElements);
			page = virtualPage;
		}
		else
		{
			page = new JRBasePrintPage();
		}

		return page;
	}

}
