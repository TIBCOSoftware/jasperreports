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
import java.util.ListIterator;
import java.util.Locale;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import java.util.Set;

import net.sf.jasperreports.engine.JRAbstractScriptlet;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRDefaultScriptlet;
import net.sf.jasperreports.engine.JRDefaultStyleProvider;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExpression;
import net.sf.jasperreports.engine.JRField;
import net.sf.jasperreports.engine.JRGroup;
import net.sf.jasperreports.engine.JRParameter;
import net.sf.jasperreports.engine.JRPrintElement;
import net.sf.jasperreports.engine.JRPrintPage;
import net.sf.jasperreports.engine.JRQuery;
import net.sf.jasperreports.engine.JRReport;
import net.sf.jasperreports.engine.JRReportFont;
import net.sf.jasperreports.engine.JRResultSetDataSource;
import net.sf.jasperreports.engine.JRStyle;
import net.sf.jasperreports.engine.JRVariable;
import net.sf.jasperreports.engine.JRVirtualizer;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.base.JRBasePrintPage;
import net.sf.jasperreports.engine.base.JRVirtualPrintPage;
import net.sf.jasperreports.engine.design.JRDefaultCompiler;
import net.sf.jasperreports.engine.design.JRDesignVariable;
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
public abstract class JRBaseFiller implements JRDefaultStyleProvider//, JRDefaultFontProvider
{

	/**
	 * Map class to be used for bound elements.
	 * <p>
	 * If per page element maps are used, the map will update the page map when
	 * adding or clearing the bound element map.
	 * 
	 * @author John Bindel
	 */
	public class BoundElementMap extends HashMap
	{
		private static final long serialVersionUID = 10003;

		private final Map perPageElements;

		/**
		 * Used when per page maps are not required. The map will behave just
		 * like <code>java.util.HashMap</code>.
		 */
		BoundElementMap()
		{
			super();
			this.perPageElements = null;
		}

		/**
		 * Used when per page map is required.
		 * 
		 * @param perPageElements
		 *            the page map
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
		 * If per page map is required, the entry will also be added for the
		 * current print page.
		 */
		public Object put(Object key, Object value)
		{
			if (perPageElements != null)
			{
				return put(key, value, fillContext.getPrintPage());
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
		// Each of these contains a java.util.Map of identity hash codes
		// (Integer) to text elements.
		HashMap pageToReportElements;

		HashMap pageToPageElements;

		HashMap pageToColumnElements;

		HashMap pageToBandElements;

		// Contains a java.util.Map per group of identity hash codes (Integer)
		// to text elements.
		HashMap pageToGroupElements;

		BoundElements()
		{
			this.pageToReportElements = new HashMap();
			this.pageToPageElements = new HashMap();
			this.pageToColumnElements = new HashMap();
			this.pageToBandElements = new HashMap();
			this.pageToGroupElements = new HashMap();
		}

		/**
		 * Collect all of the identity data the the JRBaseFiller needs to know.
		 * <p>
		 * All the bound elements on the page are collected and transformed into
		 * identity objects.
		 * 
		 * @param page
		 *            the page to get the identity data for
		 */
		public JRVirtualPrintPage.ObjectIDPair[] getIdentityData(JRVirtualPrintPage page)
		{
			Set allElements = new HashSet();

			addElements(allElements, pageToReportElements, page);
			addElements(allElements, pageToPageElements, page);
			addElements(allElements, pageToColumnElements, page);
			addElements(allElements, pageToBandElements, page);
			addGroupElements(allElements, pageToGroupElements, page);

			return createIdentityData(allElements);
		}

		/**
		 * Sets the identity date for a virtualized page.
		 * <p>
		 * The identity data consists of bound elements located on the page.
		 * Pairs of identity hash code and objects are stored when the page is
		 * virtualized. When the page gets devirtualized, the original objects
		 * are substituted in the bound maps based on their identity hash code.
		 * 
		 * @param page
		 *            the virtualized page
		 * @param identityData
		 *            the identity data
		 */
		public void setIdentityData(JRVirtualPrintPage page, JRVirtualPrintPage.ObjectIDPair[] identityData)
		{
			// Update the perPageEntries, AND the normal bound maps.
			updateIdentityData(pageToReportElements, page, reportBoundElements, identityData);
			updateIdentityData(pageToPageElements, page, pageBoundElements, identityData);
			updateIdentityData(pageToColumnElements, page, columnBoundElements, identityData);
			updateGroupIdentityData(pageToGroupElements, page, groupBoundElements, identityData);
		}
	}

	/**
	 * 
	 */
	private static final Log log = LogFactory.getLog(JRBaseFiller.class);

	/**
	 * 
	 */
	protected JRBaseFiller parentFiller = null;

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

	protected JRStyle defaultStyle = null;

	protected JRStyle[] styles = null;

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

	protected ClassLoader reportClassLoader = null;

	protected Integer reportMaxCount = null;

	protected int reportCount = 0;

	protected List formattedTextFields = new ArrayList();

	protected Map loadedSubreports = null;

	protected JRFillContext fillContext;
	
	/**
	 * All bound elements per page.
	 */
	protected BoundElements perPageBoundElements = null;

	/**
	 * Map of elements to be resolved at report level.
	 */
	protected BoundElementMap reportBoundElements = null;

	/**
	 * Map of elements to be resolved at page level.
	 */
	protected BoundElementMap pageBoundElements = null;

	/**
	 * Map of elements to be resolved at column level.
	 */
	protected BoundElementMap columnBoundElements = null;

	/**
	 * Maps of elements to be resolved at group levels.
	 */
	protected Map groupBoundElements = null;

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
	 * List of {@link JRFillBand JRFillBand} objects containing all bands of the
	 * report.
	 */
	protected List bands;

	/**
	 * Collection of subfillers
	 */
	protected Set subfillers;
	
	private List identityPages;

	private Thread fillingThread;

	private PreparedStatement dataSourceStatement;

	private static class VariableCalculationReq
	{
		String variableName;

		byte calculation;

		VariableCalculationReq(String variableName, byte calculation)
		{
			this.variableName = variableName;
			this.calculation = calculation;
		}

		public boolean equals(Object o)
		{
			if (o == null || !(o instanceof VariableCalculationReq))
			{
				return false;
			}

			VariableCalculationReq r = (VariableCalculationReq) o;

			return variableName.equals(r.variableName) && calculation == r.calculation;
		}

		public int hashCode()
		{
			return 31 * calculation + variableName.hashCode();
		}
	}

	protected Set variableCalculationReqs;

	/**
	 * 
	 */
	protected JRBaseFiller(JasperReport jasperReport, JRCalculator initCalculator, JRBaseFiller parentFiller) throws JRException
	{
		JRGraphEnvInitializer.initializeGraphEnv();

		/*   */
		this.parentFiller = parentFiller;
		
		if (parentFiller == null)
		{
			fillContext = new JRFillContext();
		}
		else
		{
			fillContext = parentFiller.fillContext;
		}

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
			for (int i = 0; i < fonts.length; i++)
			{
				fonts[i] = factory.getReportFont(jrFonts[i]);
			}
		}

		/*   */
		defaultStyle = factory.getStyle(jasperReport.getDefaultStyle());

		/*   */
		JRStyle[] jrStyles = jasperReport.getStyles();
		if (jrStyles != null && jrStyles.length > 0)
		{
			styles = new JRStyle[jrStyles.length];
			for (int i = 0; i < styles.length; i++)
			{
				styles[i] = factory.getStyle(jrStyles[i]);
			}
		}

		/*   */
		JRParameter[] jrParameters = jasperReport.getParameters();
		if (jrParameters != null && jrParameters.length > 0)
		{
			parameters = new JRFillParameter[jrParameters.length];
			parametersMap = new HashMap();
			for (int i = 0; i < parameters.length; i++)
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
			for (int i = 0; i < fields.length; i++)
			{
				fields[i] = factory.getField(jrFields[i]);
				fieldsMap.put(fields[i].getName(), fields[i]);
			}
		}

		/*   */
		JRVariable[] jrVariables = jasperReport.getVariables();
		if (jrVariables != null && jrVariables.length > 0)
		{
			List variableList = new ArrayList(jrVariables.length * 3);

			variablesMap = new HashMap();
			for (int i = 0; i < jrVariables.length; i++)
			{
				addVariable(jrVariables[i], variableList, factory);
			}

			setVariables(variableList);
		}

		/*   */
		JRGroup[] jrGroups = jasperReport.getGroups();
		if (jrGroups != null && jrGroups.length > 0)
		{
			groups = new JRFillGroup[jrGroups.length];
			for (int i = 0; i < groups.length; i++)
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

		checkVariableCalculationReqs(factory);

		/*   */
		scriptlet = createScriptlet();

		/*   */
		scriptlet.setData(parametersMap, fieldsMap, variablesMap, groups);

		/*   */
		if (initCalculator == null)
		{
			calculator = JRDefaultCompiler.getInstance().loadCalculator(jasperReport);
		}
		else
		{
			calculator = initCalculator;
		}

		calculator.init(this);

		initBands();
	}

	private void initBands()
	{
		bands = new ArrayList(8 + (groups == null ? 0 : (2 * groups.length)));
		bands.add(title);
		bands.add(summary);
		bands.add(pageHeader);
		bands.add(pageFooter);
		bands.add(lastPageFooter);
		bands.add(columnHeader);
		bands.add(columnFooter);
		bands.add(detail);

		if (groups != null && groups.length > 0)
		{
			for (int i = 0; i < groups.length; i++)
			{
				JRFillGroup group = groups[i];
				bands.add(group.getGroupHeader());
				bands.add(group.getGroupFooter());
			}
		}
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
	public JRStyle getDefaultStyle()
	{
		return defaultStyle;
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
	protected JRStyle[] getStyles()
	{
		return styles;
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
	public JasperPrint fill(Map parameterValues, Connection conn) throws JRException
	{
		fillingThread = Thread.currentThread();

		if (parameterValues == null)
		{
			parameterValues = new HashMap();
		}

		/*   */
		setParameters(parameterValues);

		if (parentFiller != null)
		{
			parentFiller.registerSubfiller(this);
		}

		/*   */
		if (conn == null)
		{
			conn = (Connection) parameterValues.get(JRParameter.REPORT_CONNECTION);
		}
		if (conn == null)
		{
			parameterValues.remove(JRParameter.REPORT_CONNECTION);
		}
		else
		{
			parameterValues.put(JRParameter.REPORT_CONNECTION, conn);
		}
		JRFillParameter parameter = (JRFillParameter) parametersMap.get(JRParameter.REPORT_CONNECTION);
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
		ResultSet rs = null;

		try
		{
			JRDataSource ds = null;

			pstmt = JRQueryExecuter.getStatement(query, parametersMap, parameterValues, conn);

			if (pstmt != null)
			{
				if (reportMaxCount != null)
				{
					pstmt.setMaxRows(reportMaxCount.intValue());
				}

				dataSourceStatement = pstmt;

				rs = pstmt.executeQuery();

				dataSourceStatement = null;

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
			fillingThread = null;
			dataSourceStatement = null;

			if (rs != null)
			{
				try
				{
					rs.close();
				}
				catch (SQLException e)
				{
				}
			}

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
	public JasperPrint fill(Map parameterValues, JRDataSource ds) throws JRException
	{
		fillingThread = Thread.currentThread();
		try
		{
			dataSource = ds;

			if (parameterValues == null)
			{
				parameterValues = new HashMap();
			}

			/*   */
			setParameters(parameterValues);

			if (parentFiller != null)
			{
				parentFiller.registerSubfiller(this);
			}

			/*   */
			if (dataSource == null)
			{
				dataSource = (JRDataSource) parameterValues.get(JRParameter.REPORT_DATA_SOURCE);
			}
			if (dataSource == null)
			{
				parameterValues.remove(JRParameter.REPORT_DATA_SOURCE);
			}
			else
			{
				parameterValues.put(JRParameter.REPORT_DATA_SOURCE, dataSource);
			}
			JRFillParameter parameter = (JRFillParameter) parametersMap.get(JRParameter.REPORT_DATA_SOURCE);
			if (parameter != null)
			{
				setParameter(parameter, dataSource);
			}

			/*   */
			parameterValues.put(JRParameter.REPORT_SCRIPTLET, scriptlet);
			parameter = (JRFillParameter) parametersMap.get(JRParameter.REPORT_SCRIPTLET);
			if (parameter != null)
			{
				setParameter(parameter, scriptlet);
			}

			/*   */
			parameterValues.put(JRParameter.REPORT_PARAMETERS_MAP, parameterValues);
			parameter = (JRFillParameter) parametersMap.get(JRParameter.REPORT_PARAMETERS_MAP);
			if (parameter != null)
			{
				setParameter(parameter, parameterValues);
			}

			jasperPrint.setName(name);
			jasperPrint.setPageWidth(pageWidth);
			jasperPrint.setPageHeight(pageHeight);
			jasperPrint.setOrientation(orientation);

			jasperPrint.setDefaultFont(defaultFont);//FIXME STYLE investigate this

			/*   */
			if (fonts != null && fonts.length > 0)
			{
				for (int i = 0; i < fonts.length; i++)
				{
					try
					{
						jasperPrint.addFont(fonts[i]);
					}
					catch (JRException e)
					{
						// ignore font duplication exception
					}
				}
			}

			jasperPrint.setDefaultStyle(defaultStyle);//FIXME STYLE investigate this

			/*   */
			if (styles != null && styles.length > 0)
			{
				for (int i = 0; i < styles.length; i++)
				{
					try
					{
						jasperPrint.addStyle(styles[i]);
					}
					catch (JRException e)
					{
						// ignore font duplication exception
					}
				}
			}

			for (int i = 0; i < formattedTextFields.size(); i++)
			{
				((JRFillTextField) formattedTextFields.get(i)).setFormat();
			}

			loadedSubreports = new HashMap();

			if (!fillContext.isPerPageBoundElements())
			{
				// per page maps are not used
				reportBoundElements = new BoundElementMap();
				pageBoundElements = new BoundElementMap();
				columnBoundElements = new BoundElementMap();
				groupBoundElements = new HashMap();

				if (groups != null && groups.length > 0)
				{
					for (int i = 0; i < groups.length; i++)
					{
						groupBoundElements.put(groups[i].getName(), new BoundElementMap());
					}
				}
			}
			else
			{
				// per page maps are used
				reportBoundElements = new BoundElementMap(perPageBoundElements.pageToReportElements);
				pageBoundElements = new BoundElementMap(perPageBoundElements.pageToPageElements);
				columnBoundElements = new BoundElementMap(perPageBoundElements.pageToColumnElements);
				groupBoundElements = new HashMap();

				if (groups != null && groups.length > 0)
				{
					for (int i = 0; i < groups.length; i++)
					{
						String groupName = groups[i].getName();

						HashMap map = new HashMap();
						perPageBoundElements.pageToGroupElements.put(groupName, map);
						groupBoundElements.put(groupName, new BoundElementMap(map));
					}
				}
			}

			for (Iterator i = bands.iterator(); i.hasNext();)
			{
				JRFillBand band = (JRFillBand) i.next();
				band.initBoundElementMap(perPageBoundElements != null);
			}

			/*   */
			reportCount = 0;

			/*   */
			fillReport();

			if (parentFiller != null)
			{
				parentFiller.unregisterSubfiller(this);
			}

			return jasperPrint;
		}
		finally
		{
			fillingThread = null;
		}
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
					tmpResourceBundle = ResourceBundle.getBundle(resourceBundleBaseName, locale, classLoader);
				}
				catch (MissingResourceException e)
				{
					// if (log.isWarnEnabled())
					// log.warn("Failure using
					// Thread.currentThread().getContextClassLoader() in
					// JRClassLoader class. Using
					// JRClassLoader.class.getClassLoader() instead.");
				}
			}

			if (tmpResourceBundle == null)
			{
				classLoader = JRClassLoader.class.getClassLoader();

				if (classLoader == null)
				{
					tmpResourceBundle = ResourceBundle.getBundle(resourceBundleBaseName, locale);
				}
				else
				{
					tmpResourceBundle = ResourceBundle.getBundle(resourceBundleBaseName, locale, classLoader);
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
				tmpScriptlet = (JRAbstractScriptlet) clazz.newInstance();
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
	 */
	protected void setParameters(Map parameterValues) throws JRException
	{
		if (isParametersAlreadySet)
		{
			return;
		}

		/*   */
		reportMaxCount = (Integer) parameterValues.get(JRParameter.REPORT_MAX_COUNT);//FIXME NOW why not setParameter()

		/*   */
		locale = (Locale) parameterValues.get(JRParameter.REPORT_LOCALE);
		if (locale == null)
		{
			locale = Locale.getDefault();
		}
		if (locale == null)
		{
			parameterValues.remove(JRParameter.REPORT_LOCALE);//FIXME NOW why remove? check all
		}
		else
		{
			parameterValues.put(JRParameter.REPORT_LOCALE, locale);
		}
		JRFillParameter parameter = (JRFillParameter) parametersMap.get(JRParameter.REPORT_LOCALE);
		if (parameter != null)
		{
			setParameter(parameter, locale);
		}

		/*   */
		resourceBundle = (ResourceBundle) parameterValues.get(JRParameter.REPORT_RESOURCE_BUNDLE);
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
		parameter = (JRFillParameter) parametersMap.get(JRParameter.REPORT_RESOURCE_BUNDLE);
		if (parameter != null)
		{
			setParameter(parameter, resourceBundle);
		}

		/* Virtualizer */
		virtualizer = (JRVirtualizer) parameterValues.get(JRParameter.REPORT_VIRTUALIZER);
		parameter = (JRFillParameter) parametersMap.get(JRParameter.REPORT_VIRTUALIZER);
		if (parameter != null)
		{
			setParameter(parameter, virtualizer);
		}
		
		if (virtualizer != null)
		{
			fillContext.setUsingVirtualizer(true);
			fillContext.setPerPageBoundElements(true);
		}
		
		if (virtualizer != null || fillContext.isPerPageBoundElements())
		{
			// keep per page element maps
			perPageBoundElements = new BoundElements();
		}
		
		/*   */
		reportClassLoader = (ClassLoader) parameterValues.get(JRParameter.REPORT_CLASS_LOADER);
		parameter = (JRFillParameter) parametersMap.get(JRParameter.REPORT_CLASS_LOADER);
		if (parameter != null)
		{
			setParameter(parameter, reportClassLoader);
		}

		Boolean isIgnorePaginationParam = (Boolean) parameterValues.get(JRParameter.IS_IGNORE_PAGINATION);
		if (isIgnorePaginationParam != null)
		{
			fillContext.setIgnorePagination(isIgnorePaginationParam.booleanValue());
		}
		else
		{
			parameterValues.put(JRParameter.IS_IGNORE_PAGINATION, fillContext.isIgnorePagination() ? Boolean.TRUE : Boolean.FALSE);
		}
		
		if (fillContext.isIgnorePagination())
		{
			isTitleNewPage = false;
			isSummaryNewPage = false;
			if (groups != null)
			{
				for (int i = 0; i < groups.length; i++)
				{
					groups[i].setStartNewPage(false);
					groups[i].setResetPageNumber(false);
					groups[i].setStartNewColumn(false);
				}
			}
			setPageHeight(Integer.MAX_VALUE);
		}

		/*   */
		if (parameters != null && parameters.length > 0)
		{
			Object value = null;
			for (int i = 0; i < parameters.length; i++)
			{
				if (parameterValues.containsKey(parameters[i].getName()))
				{
					setParameter(parameters[i], parameterValues.get(parameters[i].getName()));
				}
				else if (!parameters[i].isSystemDefined())
				{
					value = calculator.evaluate(parameters[i].getDefaultValueExpression(), JRExpression.EVALUATION_DEFAULT);
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
				throw 
					new JRException(
						"Incompatible " 
						+ value.getClass().getName() 
						+ " value assigned to parameter " 
						+ parameter.getName() 
						+ " in the " + name + " report."
						);
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
			hasNext = (reportMaxCount == null || reportMaxCount.intValue() > reportCount++) && dataSource.next();

			if (hasNext)
			{
				/*   */
				if (fields != null && fields.length > 0)
				{
					JRFillField field = null;
					for (int i = 0; i < fields.length; i++)
					{
						field = fields[i];
						field.setOldValue(field.getValue());
						field.setValue(dataSource.getFieldValue(field));
					}
				}

				/*   */
				if (variables != null && variables.length > 0)
				{
					JRFillVariable variable = null;
					for (int i = 0; i < variables.length; i++)
					{
						variable = variables[i];
						variable.setOldValue(variable.getValue());
					}
				}
			}
		}

		return hasNext;
	}

	private void resolveBoundElements(Collection elements, Map boundElements, byte evaluation) throws JRException
	{
		if (elements != null && elements.size() > 0)
		{
			for (Iterator it = elements.iterator(); it.hasNext();)
			{
				JRPrintElement element = (JRPrintElement) it.next();
				JRFillElement fillElement = (JRFillElement) boundElements.get(element);

				fillElement.resolveElement(element, evaluation);
			}
		}
	}

	private void resolvePerPageBoundElements(Map perPageElements, Map boundElements, byte evaluation) throws JRException
	{
		if (perPageElements != null)
		{
			for (Iterator it = perPageElements.entrySet().iterator(); it.hasNext();)
			{
				Map.Entry entry = (Map.Entry) it.next();
				// Calling getElements() will page in the data for the page.
				JRPrintPage page = (JRPrintPage) entry.getKey();
				page.getElements();
				resolveBoundElements(((Map) entry.getValue()).values(), boundElements, evaluation);
			}
		}

		boundElements.clear();
	}

	private void resolveBoundElements(Map boundElements, byte evaluation) throws JRException
	{
		resolveBoundElements(boundElements.keySet(), boundElements, evaluation);

		boundElements.clear();
	}

	/**
	 * Resolves elements which are to be evaluated at report level.
	 */
	protected void resolveReportBoundElements() throws JRException
	{
		if (fillContext.isPerPageBoundElements())
		{
			resolvePerPageBoundElements(perPageBoundElements.pageToReportElements, reportBoundElements, JRExpression.EVALUATION_DEFAULT);
		}
		else
		{
			resolveBoundElements(reportBoundElements, JRExpression.EVALUATION_DEFAULT);
		}
	}

	/**
	 * Resolves elements which are to be evaluated at page level.
	 * 
	 * @param evaluation
	 *            the evaluation type
	 */
	protected void resolvePageBoundElements(byte evaluation) throws JRException
	{
		if (fillContext.isPerPageBoundElements())
		{
			resolvePerPageBoundElements(perPageBoundElements.pageToPageElements, pageBoundElements, evaluation);
		}
		else
		{
			resolveBoundElements(pageBoundElements, evaluation);
		}
	}

	/**
	 * Resolves elements which are to be evaluated at column level.
	 * 
	 * @param evaluation
	 *            the evaluation type
	 */
	protected void resolveColumnBoundElements(byte evaluation) throws JRException
	{
		if (fillContext.isPerPageBoundElements())
		{
			resolvePerPageBoundElements(perPageBoundElements.pageToColumnElements, columnBoundElements, evaluation);
		}
		else
		{
			resolveBoundElements(columnBoundElements, evaluation);
		}
	}

	/**
	 * Resolves elements which are to be evaluated at group level.
	 * 
	 * @param evaluation
	 *            the evaluation type
	 * @param isFinal
	 */
	protected void resolveGroupBoundElements(byte evaluation, boolean isFinal) throws JRException
	{
		if (groups != null && groups.length > 0)
		{
			for (int i = 0; i < groups.length; i++)
			{
				JRFillGroup group = groups[i];

				if ((group.hasChanged() && group.isFooterPrinted()) || isFinal)
				{
					String groupName = group.getName();
					Map specificGroupBoundTexts = (Map) groupBoundElements.get(groupName);

					if (fillContext.isPerPageBoundElements())
					{
						resolvePerPageBoundElements((Map) perPageBoundElements.pageToGroupElements.get(groupName), specificGroupBoundTexts, evaluation);
					}
					else
					{
						resolveBoundElements(specificGroupBoundTexts, evaluation);
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

			addIdentityDataProviders(virtualPage, this);

			page = virtualPage;
		}
		else
		{
			page = new JRBasePrintPage();
		}

		return page;
	}

	/**
	 * Returns the value of a variable.
	 * 
	 * @param variableName
	 *            the variable name
	 * @return the variable value
	 * @throws JRException
	 */
	protected Object getVariableValue(String variableName) throws JRException
	{
		JRFillVariable var = (JRFillVariable) variablesMap.get(variableName);
		if (var == null)
		{
			throw new JRException("No such variable " + variableName);
		}
		return var.getValue();
	}

	/**
	 * Resloves elements which are to be evaluated at band level.
	 * 
	 * @param band
	 *            the band
	 * @param evaluation
	 *            the evaluation type
	 * @throws JRException
	 */
	protected void resolveBandBoundElements(JRFillBand band, byte evaluation) throws JRException
	{
		if (fillContext.isPerPageBoundElements())
		{
			resolvePerPageBoundElements(band.pageToBoundElements, band.boundElements, evaluation);
		}
		else
		{
			resolveBoundElements(band.boundElements, evaluation);
		}
	}

	/**
	 * Creates identity data out of a list of elements.
	 * 
	 * @param allElements
	 *            the elements
	 * @return the identity data
	 */
	protected static JRVirtualPrintPage.ObjectIDPair[] createIdentityData(Set allElements)
	{
		JRVirtualPrintPage.ObjectIDPair[] ids;

		if (!allElements.isEmpty())
		{
			ids = new JRVirtualPrintPage.ObjectIDPair[allElements.size()];

			int i = 0;
			for (Iterator it = allElements.iterator(); it.hasNext(); ++i)
			{
				ids[i] = new JRVirtualPrintPage.ObjectIDPair(it.next());
			}
		}
		else
		{
			ids = null;
		}
		return ids;
	}

	/**
	 * Collects elements from a map.
	 * 
	 * @param allElements
	 *            the elements are collected here
	 * @param pageMap
	 *            the page map
	 * @param page
	 *            the page
	 */
	protected static void addElements(Set allElements, Map pageMap, JRVirtualPrintPage page)
	{
		Map map = (Map) pageMap.get(page);
		if (map != null && !map.isEmpty())
		{
			Collection elements = map.values();
			allElements.addAll(elements);
		}
	}

	/**
	 * Collects elements from a group map.
	 * 
	 * @param allElements
	 *            the elements are collected here
	 * @param groupMap
	 *            the group map
	 * @param page
	 *            the page
	 */
	protected static void addGroupElements(Set allElements, Map groupMap, JRVirtualPrintPage page)
	{
		for (Iterator it = groupMap.entrySet().iterator(); it.hasNext();)
		{
			Map.Entry entry = (Map.Entry) it.next();
			addElements(allElements, (Map) entry.getValue(), page);
		}
	}

	/**
	 * Updates element maps on page devirtualization.
	 * 
	 * @param pageMap
	 *            the page map
	 * @param page
	 *            the page
	 * @param boundElements
	 *            the bound element map
	 * @param identityData
	 *            the identity data
	 */
	protected static void updateIdentityData(Map pageMap, JRVirtualPrintPage page, BoundElementMap boundElements, JRVirtualPrintPage.ObjectIDPair[] identityData)
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
						// This will also add the new element into the
						// pageElements map.
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

	/**
	 * Updates group element maps on page devirtualization.
	 * 
	 * @param pageGroupMap
	 *            the page map
	 * @param page
	 *            the page
	 * @param groupMap
	 *            the group map
	 * @param identityData
	 *            the identity data
	 */
	protected static void updateGroupIdentityData(Map pageGroupMap, JRVirtualPrintPage page, Map groupMap, JRVirtualPrintPage.ObjectIDPair[] identityData)
	{
		for (Iterator it = pageGroupMap.entrySet().iterator(); it.hasNext();)
		{
			Map.Entry entry = (Map.Entry) it.next();
			updateIdentityData((Map) entry.getValue(), page, (BoundElementMap) groupMap.get(entry.getKey()), identityData);
		}
	}

	private JRFillVariable addVariable(JRVariable parentVariable, List variableList, JRFillObjectFactory factory)
	{
		JRFillVariable variable = factory.getVariable(parentVariable);

		byte calculation = variable.getCalculation();
		switch (calculation)
		{
			case JRVariable.CALCULATION_AVERAGE:
			case JRVariable.CALCULATION_VARIANCE:
			{
				JRVariable countVar = createHelperVariable(parentVariable, "_COUNT", JRVariable.CALCULATION_COUNT);
				JRFillVariable fillCountVar = addVariable(countVar, variableList, factory);
				variable.setHelperVariable(fillCountVar, JRFillVariable.HELPER_COUNT);

				JRVariable sumVar = createHelperVariable(parentVariable, "_SUM", JRVariable.CALCULATION_SUM);
				JRFillVariable fillSumVar = addVariable(sumVar, variableList, factory);
				variable.setHelperVariable(fillSumVar, JRFillVariable.HELPER_SUM);

				break;
			}
			case JRVariable.CALCULATION_STANDARD_DEVIATION:
			{
				JRVariable varianceVar = createHelperVariable(parentVariable, "_VARIANCE", JRVariable.CALCULATION_VARIANCE);
				JRFillVariable fillVarianceVar = addVariable(varianceVar, variableList, factory);
				variable.setHelperVariable(fillVarianceVar, JRFillVariable.HELPER_VARIANCE);

				break;
			}
		}

		variableList.add(variable);
		return variable;
	}

	private void setVariables(List variableList)
	{
		variables = new JRFillVariable[variableList.size()];
		variables = (JRFillVariable[]) variableList.toArray(variables);

		for (int i = 0; i < variables.length; i++)
		{
			variablesMap.put(variables[i].getName(), variables[i]);
		}
	}

	private JRVariable createHelperVariable(JRVariable variable, String nameSuffix, byte calculation)
	{
		JRDesignVariable helper = new JRDesignVariable();
		helper.setName(variable.getName() + nameSuffix);
		helper.setValueClassName(variable.getValueClassName());
		helper.setIncrementerFactoryClassName(variable.getIncrementerFactoryClassName());
		helper.setResetType(variable.getResetType());
		helper.setResetGroup(variable.getResetGroup());
		helper.setIncrementType(variable.getIncrementType());
		helper.setIncrementGroup(variable.getIncrementGroup());
		helper.setCalculation(calculation);
		helper.setSystemDefined(true);
		helper.setExpression(variable.getExpression());

		return helper;
	}

	/**
	 * Adds a variable calculation request.
	 * 
	 * @param variableName
	 *            the variable name
	 * @param calculation
	 *            the calculation type
	 */
	protected void addVariableCalculationReq(String variableName, byte calculation)
	{
		if (variableCalculationReqs == null)
		{
			variableCalculationReqs = new HashSet();
		}

		variableCalculationReqs.add(new VariableCalculationReq(variableName, calculation));
	}

	private void checkVariableCalculationReqs(JRFillObjectFactory factory)
	{
		if (variableCalculationReqs != null && !variableCalculationReqs.isEmpty())
		{
			List variableList = new ArrayList(variables.length * 2);

			for (int i = 0; i < variables.length; i++)
			{
				JRFillVariable variable = variables[i];
				checkVariableCalculationReq(variable, variableList, factory);
			}

			setVariables(variableList);
		}
	}

	private void checkVariableCalculationReq(JRFillVariable variable, List variableList, JRFillObjectFactory factory)
	{
		if (hasVariableCalculationReq(variable, JRVariable.CALCULATION_AVERAGE) || hasVariableCalculationReq(variable, JRVariable.CALCULATION_VARIANCE))
		{
			if (variable.getHelperVariable(JRFillVariable.HELPER_COUNT) == null)
			{
				JRVariable countVar = createHelperVariable(variable, "_COUNT", JRVariable.CALCULATION_COUNT);
				JRFillVariable fillCountVar = factory.getVariable(countVar);
				checkVariableCalculationReq(fillCountVar, variableList, factory);
				variable.setHelperVariable(fillCountVar, JRFillVariable.HELPER_COUNT);
			}

			if (variable.getHelperVariable(JRFillVariable.HELPER_SUM) == null)
			{
				JRVariable sumVar = createHelperVariable(variable, "_SUM", JRVariable.CALCULATION_SUM);
				JRFillVariable fillSumVar = factory.getVariable(sumVar);
				checkVariableCalculationReq(fillSumVar, variableList, factory);
				variable.setHelperVariable(fillSumVar, JRFillVariable.HELPER_SUM);
			}
		}

		if (hasVariableCalculationReq(variable, JRVariable.CALCULATION_STANDARD_DEVIATION))
		{
			if (variable.getHelperVariable(JRFillVariable.HELPER_VARIANCE) == null)
			{
				JRVariable varianceVar = createHelperVariable(variable, "_VARIANCE", JRVariable.CALCULATION_VARIANCE);
				JRFillVariable fillVarianceVar = factory.getVariable(varianceVar);
				checkVariableCalculationReq(fillVarianceVar, variableList, factory);
				variable.setHelperVariable(fillVarianceVar, JRFillVariable.HELPER_VARIANCE);
			}
		}

		variableList.add(variable);
	}

	private boolean hasVariableCalculationReq(JRVariable var, byte calculation)
	{
		return variableCalculationReqs.contains(new VariableCalculationReq(var.getName(), calculation));
	}

	/**
	 * Cancells the fill process.
	 * 
	 * @throws JRException
	 */
	public void cancelFill() throws JRException
	{
		PreparedStatement s = dataSourceStatement;
		if (s != null)
		{
			try
			{
				s.cancel();
			}
			catch (Throwable t)
			{
				throw new JRException("Error cancelling SQL statement", t);
			}
		}
		else
		{
			Thread t = fillingThread;
			if (t != null)
			{
				t.interrupt();
			}
		}
	}

	
	protected void registerSubfiller(JRBaseFiller subfiller)
	{
		if (subfillers == null)
		{
			subfillers = new HashSet();
		}

		if (subfillers.add(subfiller) && fillContext.isUsingVirtualizer())
		{
			subfiller.identityPages = new ArrayList();
			
			JRVirtualPrintPage masterPrintPage = (JRVirtualPrintPage) fillContext.getPrintPage();
			subfiller.identityPages.add(masterPrintPage);
			addIdentityDataProviders(masterPrintPage, subfiller);
		}
	}

	protected void unregisterSubfiller(JRBaseFiller subfiller)
	{
		if (subfillers.remove(subfiller) && fillContext.isUsingVirtualizer())
		{
			removeIdentityDataProviders(subfiller);
		}
	}

	private static void addIdentityDataProviders(JRVirtualPrintPage page, JRBaseFiller filler)
	{
		page.addIdentityDataProvider(filler.perPageBoundElements);
		for (Iterator i = filler.bands.iterator(); i.hasNext();)
		{
			JRFillBand band = (JRFillBand) i.next();
			page.addIdentityDataProvider(band);
		}

		if (filler.subfillers != null)
		{
			for (Iterator i = filler.subfillers.iterator(); i.hasNext();)
			{
				JRBaseFiller subfiller = (JRBaseFiller) i.next();
				
				subfiller.identityPages.add(page);
				addIdentityDataProviders(page, subfiller);
			}
		}
	}

	private void removeIdentityDataProviders(JRBaseFiller filler)
	{
		if (filler.identityPages != null)
		{
			for (Iterator it = filler.identityPages.iterator(); it.hasNext();)
			{
				JRVirtualPrintPage page = (JRVirtualPrintPage) it.next();
				
				page.removeIdentityDataProvider(filler.perPageBoundElements);
				for (Iterator i = filler.bands.iterator(); i.hasNext();)
				{
					JRFillBand band = (JRFillBand) i.next();
					page.removeIdentityDataProvider(band);
				}
			}
			
			filler.identityPages = null;
		}
	}

	
	protected void addPage(JRPrintPage page)
	{
		if (!isSubreport())
		{
			jasperPrint.addPage(page);
			fillContext.setPrintPage(page);
		}
	}
	

	protected static final class PageIdentityDataProvider implements JRVirtualPrintPage.IdentityDataProvider
	{
		private static final Map providers = new HashMap();  
		
		private final JRBasePrintPage printPage;
		
		protected PageIdentityDataProvider(JRBasePrintPage printPage)
		{
			this.printPage = printPage;
		}

		public JRVirtualPrintPage.ObjectIDPair[] getIdentityData(JRVirtualPrintPage page)
		{
			return null;
		}

		public void setIdentityData(JRVirtualPrintPage page, JRVirtualPrintPage.ObjectIDPair[] identityData)
		{
			if (identityData != null && identityData.length > 0)
			{
				Map idMap = new HashMap();
				for (int i = 0; i < identityData.length; i++)
				{
					idMap.put(new Integer(identityData[i].getIdentity()), identityData[i].getObject()); 
				}
				
				for (ListIterator i = printPage.getElements().listIterator(); i.hasNext();)
				{
					Object element = i.next();
					Integer id = new Integer(System.identityHashCode(element));
					
					Object idObject = idMap.get(id);
					if (idObject != null)
					{
						i.set(idObject);
					}
				}
			}
		}
		
		public static JRVirtualPrintPage.IdentityDataProvider getIdentityDataProvider(JRBasePrintPage printPage)
		{
			JRVirtualPrintPage.IdentityDataProvider provider = (JRVirtualPrintPage.IdentityDataProvider) providers.get(printPage);
			if (provider == null)
			{
				provider = new PageIdentityDataProvider(printPage);
				providers.put(printPage, provider);
			}
			return provider;
		}
		
		public static JRVirtualPrintPage.IdentityDataProvider removeIdentityDataProvider(JRBasePrintPage printPage)
		{
			JRVirtualPrintPage.IdentityDataProvider provider = (JRVirtualPrintPage.IdentityDataProvider) providers.remove(printPage);
			return provider;
		}
	}

	
	protected void addPageIdentityDataProvider()
	{
		JRVirtualPrintPage.IdentityDataProvider pageProvider = PageIdentityDataProvider.getIdentityDataProvider((JRBasePrintPage) printPage);
		((JRVirtualPrintPage) fillContext.getPrintPage()).addIdentityDataProvider(pageProvider);
	}

	
	protected void removePageIdentityDataProvider()
	{
		JRVirtualPrintPage.IdentityDataProvider pageProvider = PageIdentityDataProvider.removeIdentityDataProvider((JRBasePrintPage) printPage);
		if (pageProvider != null)
		{
			((JRVirtualPrintPage) fillContext.getPrintPage()).removeIdentityDataProvider(pageProvider);
		}
	}
}
