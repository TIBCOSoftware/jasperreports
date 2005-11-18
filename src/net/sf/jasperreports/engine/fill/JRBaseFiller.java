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
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import net.sf.jasperreports.engine.JRAbstractScriptlet;
import net.sf.jasperreports.engine.JRConstants;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRDataset;
import net.sf.jasperreports.engine.JRDefaultStyleProvider;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExpression;
import net.sf.jasperreports.engine.JRParameter;
import net.sf.jasperreports.engine.JRPrintElement;
import net.sf.jasperreports.engine.JRPrintPage;
import net.sf.jasperreports.engine.JRReport;
import net.sf.jasperreports.engine.JRReportFont;
import net.sf.jasperreports.engine.JRStyle;
import net.sf.jasperreports.engine.JRVirtualizer;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.base.JRBasePrintPage;
import net.sf.jasperreports.engine.base.JRVirtualPrintPage;
import net.sf.jasperreports.engine.util.JRGraphEnvInitializer;
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
		private static final long serialVersionUID = JRConstants.SERIAL_VERSION_UID;

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

	/**
	 * the resource missing handling type
	 */
	protected byte whenResourceMissingType = JRReport.WHEN_RESOURCE_MISSING_TYPE_NULL;

	protected JRReportFont defaultFont = null;

	protected JRReportFont[] fonts = null;

	protected JRStyle defaultStyle = null;

	protected JRStyle[] styles = null;

	/**
	 * Main report dataset.
	 */
	protected JRFillDataset mainDataset;
	
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

	protected JRVirtualizer virtualizer = null;

	protected ClassLoader reportClassLoader = null;

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

	protected JRCalculator calculator;

	protected JRAbstractScriptlet scriptlet;
	
	/**
	 * Map of datasets ({@link JRFillDataset JRFillDataset} objects} indexed by name.
	 */
	protected Map datasetMap;
	
	/**
	 * The report.
	 */
	protected JasperReport jasperReport;

	private boolean bandOverFlowAllowed;
	
	/**
	 * 
	 */
	protected JRBaseFiller(JasperReport jasperReport, JREvaluator initEvaluator, JRBaseFiller parentFiller) throws JRException
	{
		JRGraphEnvInitializer.initializeGraphEnv();
		
		this.jasperReport = jasperReport;
		
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
		whenResourceMissingType = jasperReport.getWhenResourceMissingType();

		jasperPrint = new JasperPrint();
		
		if (initEvaluator == null)
		{
			calculator = JRFillDataset.createCalculator(jasperReport, jasperReport.getMainDataset());
		}
		else
		{
			calculator = new JRCalculator(initEvaluator);
		}

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
		
		createDatasets(factory);
		mainDataset = factory.getDataset(jasperReport.getMainDataset());
		groups = mainDataset.groups;
		
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

		mainDataset.initElementDatasets(factory);		
		initDatasets(factory);

		mainDataset.checkVariableCalculationReqs(factory);

		/*   */
		scriptlet = mainDataset.initScriptlet();

		/*   */
		mainDataset.setCalculator(calculator);
		mainDataset.initCalculator();
		
		initBands();
	}


	/**
	 * Returns the report parameters indexed by name.
	 * 
	 * @return the report parameters map
	 */
	protected Map getParametersMap()
	{
		return mainDataset.parametersMap;
	}

	
	/**
	 * Returns the report fields indexed by name.
	 * 
	 * @return the report fields map
	 */
	protected Map getFieldsMap()
	{
		return mainDataset.fieldsMap;
	}

	
	/**
	 * Returns the report variables indexed by name.
	 * 
	 * @return the report variables map
	 */
	protected Map getVariablesMap()
	{
		return mainDataset.variablesMap;
	}
	
	
	/**
	 * Returns a report variable.
	 * 
	 * @param variableName the variable name
	 * @return the variable
	 */
	protected JRFillVariable getVariable(String variableName)
	{
		return (JRFillVariable) mainDataset.variablesMap.get(variableName);
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
		try
		{
			JRDataSource ds = mainDataset.createDataSource(parameterValues, conn);
			fill(parameterValues, ds);
		}
		finally
		{
			fillingThread = null;
			
			mainDataset.closeStatement();
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
			mainDataset.setDatasource(parameterValues, ds);

			jasperPrint.setName(name);
			jasperPrint.setPageWidth(pageWidth);
			jasperPrint.setPageHeight(pageHeight);
			jasperPrint.setOrientation(orientation);

			jasperPrint.setDefaultFont(defaultFont);

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

			jasperPrint.setDefaultStyle(defaultStyle);

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

			setTextFieldsFormats();

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
			mainDataset.start();

			/*   */
			fillReport();

			return jasperPrint;
		}
		finally
		{
			if (parentFiller != null)
			{
				parentFiller.unregisterSubfiller(this);
			}
			
			fillingThread = null;
			
			//kill the subreport filler threads
			killSubfillerThreads();
		}
	}


	private void killSubfillerThreads()
	{
		if (subfillers != null && !subfillers.isEmpty())
		{
			for (Iterator it = subfillers.iterator(); it.hasNext();)
			{
				JRBaseFiller subfiller = (JRBaseFiller) it.next();
				if (subfiller.fillingThread != null)
				{
					subfiller.fillingThread.interrupt();
				}
			}
		}
	}

	
	protected void setTextFieldsFormats()
	{
		for (int i = 0; i < formattedTextFields.size(); i++)
		{
			((JRFillTextField) formattedTextFields.get(i)).setFormat();
		}
		
		formattedTextFields.clear();
	}

	/**
	 * 
	 */
	protected abstract void fillReport() throws JRException;

	/**
	 * 
	 */
	protected void setParameters(Map parameterValues) throws JRException
	{
		if (isParametersAlreadySet)
		{
			return;
		}
		
		mainDataset.setParameters(parameterValues);
		
		/* Virtualizer */
		virtualizer = (JRVirtualizer) parameterValues.get(JRParameter.REPORT_VIRTUALIZER);
		setParameter(JRParameter.REPORT_VIRTUALIZER, virtualizer);
		
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
		setParameter(JRParameter.REPORT_CLASS_LOADER, reportClassLoader);

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

		mainDataset.setParameterValues(parameterValues);

		isParametersAlreadySet = true;
	}
	
	
	/**
	 * Returns the report locale.
	 * 
	 * @return the report locale
	 */
	protected Locale getLocale()
	{
		return mainDataset.locale;
	}
	
	
	/**
	 * Sets a parameter's value.
	 * 
	 * @param parameterName the parameter name
	 * @param value the value
	 * @throws JRException
	 */
	protected void setParameter(String parameterName, Object value) throws JRException
	{
		mainDataset.setParameter(parameterName, value);
	}
	

	/**
	 * Sets a parameter's value.
	 * 
	 * @param parameter the parameter
	 * @param value the value
	 * @throws JRException
	 */
	protected void setParameter(JRFillParameter parameter, Object value) throws JRException
	{
		mainDataset.setParameter(parameter, value);
	}

	/**
	 * 
	 */
	protected boolean next() throws JRException
	{
		return mainDataset.next();
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
		return mainDataset.getVariableValue(variableName);
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
		mainDataset.addVariableCalculationReq(variableName, calculation);
	}

	
	/**
	 * Cancells the fill process.
	 * 
	 * @throws JRException
	 */
	public void cancelFill() throws JRException
	{
		if (!fillContext.cancelRunningStatement())
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
	
	
	/**
	 * Evaluates an expression
	 * @param expression the expression
	 * @param evaluation the evaluation type
	 * @return the evaluation result
	 * @throws JRException
	 */
	protected Object evaluateExpression(JRExpression expression, byte evaluation) throws JRException
	{
		return mainDataset.calculator.evaluate(expression, evaluation);
	}

	
	private void createDatasets(JRFillObjectFactory factory) throws JRException
	{
		datasetMap = new HashMap();
		
		JRDataset[] datasets = jasperReport.getDatasets();
		if (datasets != null && datasets.length > 0)
		{
			for (int i = 0; i < datasets.length; i++)
			{
				JRFillDataset fillDataset = factory.getDataset(datasets[i]);	
				fillDataset.createCalculator(jasperReport);
				
				datasetMap.put(datasets[i].getName(), fillDataset);
			}
		}
	}

	
	private void initDatasets(JRFillObjectFactory factory)
	{
		for (Iterator it = datasetMap.values().iterator(); it.hasNext();)
		{
			JRFillDataset dataset = (JRFillDataset) it.next();
			dataset.inheritFromMain();
			dataset.initElementDatasets(factory);
		}
	}
	
	
	protected byte getWhenResourceMissingType()
	{
		return mainDataset.whenResourceMissingType;
	}
	
	
	/**
	 * Returns the report.
	 * 
	 * @return the report
	 */
	protected JasperReport getJasperReport()
	{
		return jasperReport;
	}


	protected boolean isBandOverFlowAllowed()
	{
		return bandOverFlowAllowed;
	}


	protected void setBandOverFlowAllowed(boolean splittableBand)
	{
		this.bandOverFlowAllowed = splittableBand;
	}
	
	protected int getMasterColumnCount()
	{
		JRBaseFiller filler = parentFiller;
		int colCount = 1;
		
		while (filler != null)
		{
			colCount *= filler.columnCount;
			filler = filler.parentFiller;
		}
		
		return colCount;
	}
	
	
	public JRFillDataset getMainDataset()
	{
		return mainDataset;
	}
}
