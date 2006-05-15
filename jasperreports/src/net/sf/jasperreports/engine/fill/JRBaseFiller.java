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

/*
 * Contributors:
 * John Bindel - jbindel@users.sourceforge.net 
 */

package net.sf.jasperreports.engine.fill;

import java.net.URLStreamHandlerFactory;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.Collection;

import net.sf.jasperreports.engine.JRAbstractScriptlet;
import net.sf.jasperreports.engine.JRBand;
import net.sf.jasperreports.engine.JRConstants;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRDataset;
import net.sf.jasperreports.engine.JRDefaultStyleProvider;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExpression;
import net.sf.jasperreports.engine.JRGroup;
import net.sf.jasperreports.engine.JRParameter;
import net.sf.jasperreports.engine.JRPrintElement;
import net.sf.jasperreports.engine.JRPrintPage;
import net.sf.jasperreports.engine.JRReport;
import net.sf.jasperreports.engine.JRReportFont;
import net.sf.jasperreports.engine.JRRuntimeException;
import net.sf.jasperreports.engine.JRStyle;
import net.sf.jasperreports.engine.JRVirtualizer;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.base.JRBasePrintPage;
import net.sf.jasperreports.engine.base.JRVirtualPrintPage;
import net.sf.jasperreports.engine.util.JRGraphEnvInitializer;
import net.sf.jasperreports.engine.util.JRResourcesUtil;
import net.sf.jasperreports.engine.util.JRStyledTextParser;


/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id$
 */
public abstract class JRBaseFiller implements JRDefaultStyleProvider, JRVirtualPrintPage.IdentityDataProvider//, JRDefaultFontProvider
{

	/**
	 * Map class to be used for bound elements.
	 * <p/>
	 * Keeps print elements to fill elements maps.
	 * If per page element maps are used, such maps are kept per page.
	 *
	 * @author John Bindel
	 */
	public class BoundElementMap
	{
		private static final long serialVersionUID = JRConstants.SERIAL_VERSION_UID;

		private final Map map;

		BoundElementMap()
		{
			map = new HashMap();
		}

		/**
		 * Keep track of the objects per page for our virtualizer.
		 */
		public Object put(Object key, Object value, JRPrintPage keyPage)
		{
			Map pageMap = (Map) map.get(keyPage);
			if (pageMap == null)
			{
				pageMap = new HashMap();
				map.put(keyPage, pageMap);
			}

			return pageMap.put(key, value);
		}

		/**
		 * If per page map is required, the entry will also be added for the
		 * current print page.
		 */
		public Object put(Object key, Object value)
		{
			if (isPerPageBoundElements)
			{
				return put(key, value, fillContext.getPrintPage());
			}

			return map.put(key, value);
		}

		public void clear()
		{
			map.clear();
		}

		public Map getMap()
		{
			return map;
		}

		public Map getMap(JRPrintPage page)
		{
			return (Map) map.get(page);
		}

		public Map putMap(JRPrintPage page, Map valueMap)
		{
			return (Map) map.put(page, valueMap);
		}
	}

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

	protected URLStreamHandlerFactory urlHandlerFactory;

	protected List formattedTextFields = new ArrayList();

	protected Map loadedSubreports = null;

	protected JRFillContext fillContext;

	/**
	 * Bound element maps indexed by {@link JREvaluationTime JREvaluationTime} objects.
	 */
	protected Map boundElements;

	/**
	 *
	 */
	protected JasperPrint jasperPrint = null;

	protected JRPrintPage printPage = null;

	protected int printPageStretchHeight = 0;

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

	protected boolean isPerPageBoundElements;

	/**
	 * TODO
	 */
	protected Map consolidatedStyles = new HashMap();
	
	private JRSubreportRunner subreportRunner;


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
		if (isEmpty(jasperReport.getSummary()))
		{
			summary = missingFillBand;
		}
		else
		{
			summary = factory.getBand(jasperReport.getSummary());
		}

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


	/**
	 * Returns a report field.
	 *
	 * @param fieldName the field name
	 * @return the field
	 */
	protected JRFillField getField(String fieldName)
	{
		return (JRFillField) mainDataset.fieldsMap.get(fieldName);
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

		initBandsNowEvaluationTimes();
	}


	private void initBandsNowEvaluationTimes()
	{
		JREvaluationTime[] groupEvaluationTimes;
		if (groups == null)
		{
			groupEvaluationTimes = new JREvaluationTime[0];
		}
		else
		{
			groupEvaluationTimes = new JREvaluationTime[groups.length];
			for (int i = 0; i < groups.length; i++)
			{
				groupEvaluationTimes[i] = JREvaluationTime.getGroupEvaluationTime(groups[i].getName());
			}

			for (int i = 0; i < groups.length; i++)
			{
				JRGroup group = groups[i];
				JRFillBand footer = (JRFillBand) group.getGroupFooter();

				for (int j = i; j < groupEvaluationTimes.length; ++j)
				{
					footer.addNowEvaluationTime(groupEvaluationTimes[j]);
				}
			}
		}

		columnFooter.addNowEvaluationTime(JREvaluationTime.EVALUATION_TIME_COLUMN);

		pageFooter.addNowEvaluationTime(JREvaluationTime.EVALUATION_TIME_COLUMN);
		pageFooter.addNowEvaluationTime(JREvaluationTime.EVALUATION_TIME_PAGE);

		summary.addNowEvaluationTimes(groupEvaluationTimes);
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


	public JasperPrint fill(Map parameterValues, Connection conn) throws JRException
	{
		if (parameterValues == null)
		{
			parameterValues = new HashMap();
		}

		setConnectionParameterValue(parameterValues, conn);

		return fill(parameterValues);
	}


	protected void setConnectionParameterValue(Map parameterValues, Connection conn)
	{
		mainDataset.setConnectionParameterValue(parameterValues, conn);
	}


	public JasperPrint fill(Map parameterValues, JRDataSource ds) throws JRException
	{
		if (parameterValues == null)
		{
			parameterValues = new HashMap();
		}

		setDatasourceParameterValue(parameterValues, ds);

		return fill(parameterValues);
	}


	protected void setDatasourceParameterValue(Map parameterValues, JRDataSource ds)
	{
		mainDataset.setDatasourceParameterValue(parameterValues, ds);
	}


	public JasperPrint fill(Map parameterValues) throws JRException
	{
		if (parameterValues == null)
		{
			parameterValues = new HashMap();
		}

		fillingThread = Thread.currentThread();
		boolean urlHandlerFactorySet = false;
		boolean classLoaderSet = false;
		try
		{
			/*   */
			setParameters(parameterValues);

			classLoaderSet = setClassLoader(parameterValues);
			urlHandlerFactorySet = setUrlHandlerFactory(parameterValues);

			if (parentFiller != null)
			{
				parentFiller.registerSubfiller(this);
			}

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

			createBoundElemementMaps();

			/*   */
			mainDataset.start();

			/*   */
			fillReport();

			//FIXME STYLE maybe we don,t need the consolidated styles in the JasperPrint
			// add consolidates styles as normal styles in the print object
			Set initialStyles = consolidatedStyles.keySet();
			for (Iterator it = initialStyles.iterator(); it.hasNext();) {
				JRStyle initialStyle = (JRStyle) it.next();
				Map styleMap = (Map) consolidatedStyles.get(initialStyle);
				Collection newStyles = styleMap.values();
				for (Iterator it2 = newStyles.iterator(); it2.hasNext();) {
					jasperPrint.addStyle((JRStyle) it2.next());
				}
			}


			return jasperPrint;
		}
		finally
		{
			mainDataset.closeDatasource();

			if (parentFiller != null)
			{
				parentFiller.unregisterSubfiller(this);
			}

			fillingThread = null;

			//kill the subreport filler threads
			killSubfillerThreads();
			
			if (classLoaderSet)
			{
				JRResourcesUtil.resetClassLoader();
			}

			if (urlHandlerFactorySet)
			{
				JRResourcesUtil.resetThreadURLHandlerFactory();
			}
		}
	}


	private void createBoundElemementMaps()
	{
		boundElements = new HashMap();

		createBoundElementMaps(JREvaluationTime.EVALUATION_TIME_REPORT);
		createBoundElementMaps(JREvaluationTime.EVALUATION_TIME_PAGE);
		createBoundElementMaps(JREvaluationTime.EVALUATION_TIME_COLUMN);

		if (groups != null)
		{
			for (int i = 0; i < groups.length; i++)
			{
				createBoundElementMaps(JREvaluationTime.getGroupEvaluationTime(groups[i].getName()));
			}
		}

		for (Iterator i = bands.iterator(); i.hasNext();)
		{
			JRFillBand band = (JRFillBand) i.next();
			createBoundElementMaps(JREvaluationTime.getBandEvaluationTime(band));
		}
	}


	private void createBoundElementMaps(JREvaluationTime evaluationTime)
	{
		BoundElementMap boundElementsMap = new BoundElementMap();
		boundElements.put(evaluationTime, boundElementsMap);
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
		if (!isSubreport())
		{
			/* Virtualizer */
			virtualizer = (JRVirtualizer) parameterValues.get(JRParameter.REPORT_VIRTUALIZER);

			if (virtualizer != null)
			{
				fillContext.setUsingVirtualizer(true);
				fillContext.setPerPageBoundElements(true);
				JRVirtualizationContext.register(fillContext.getVirtualizationContext(), jasperPrint);
			}
		}

		isPerPageBoundElements = fillContext.isPerPageBoundElements();

		setIgnorePagination(parameterValues);

		mainDataset.setParameterValues(parameterValues);
	}


	private boolean setClassLoader(Map parameterValues)
	{
		reportClassLoader = (ClassLoader) parameterValues.get(JRParameter.REPORT_CLASS_LOADER);
		boolean setClassLoader = reportClassLoader != null;
		if (setClassLoader)
		{
			JRResourcesUtil.setThreadClassLoader(reportClassLoader);
		}
		return setClassLoader;
	}


	private boolean setUrlHandlerFactory(Map parameterValues)
	{
		urlHandlerFactory = (URLStreamHandlerFactory) parameterValues.get(JRParameter.REPORT_URL_HANDLER_FACTORY);
		boolean setUrlHandlerFactory = urlHandlerFactory != null;
		if (setUrlHandlerFactory)
		{
			JRResourcesUtil.setThreadURLHandlerFactory(urlHandlerFactory);
		}
		return setUrlHandlerFactory;
	}


	private void setIgnorePagination(Map parameterValues)
	{
		if (parentFiller == null)//pagination is driven by the master
		{
			Boolean isIgnorePaginationParam = (Boolean) parameterValues.get(JRParameter.IS_IGNORE_PAGINATION);
			if (isIgnorePaginationParam != null)
			{
				fillContext.setIgnorePagination(isIgnorePaginationParam.booleanValue());
			}
			else
			{
				boolean ignorePagination = jasperReport.isIgnorePagination();
				fillContext.setIgnorePagination(ignorePagination);
				parameterValues.put(JRParameter.IS_IGNORE_PAGINATION, ignorePagination ? Boolean.TRUE : Boolean.FALSE);
			}
		}
		else
		{
			boolean ignorePagination = fillContext.isIgnorePagination();
			parameterValues.put(JRParameter.IS_IGNORE_PAGINATION, ignorePagination ? Boolean.TRUE : Boolean.FALSE);
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

	private void resolveBoundElements(Map boundElementsMap, byte evaluation, JREvaluationTime evaluationTime) throws JRException
	{
		if (boundElementsMap != null)
		{
			for (Iterator it = boundElementsMap.entrySet().iterator(); it.hasNext();)
			{
				Map.Entry entry = (Map.Entry) it.next();
				JRPrintElement element = (JRPrintElement) entry.getKey();
				JRFillElement fillElement = (JRFillElement) entry.getValue();

				fillElement.resolveElement(element, evaluation, evaluationTime);
			}
		}
	}

	protected void resolveBoundElements(JREvaluationTime evaluationTime, byte evaluation) throws JRException
	{
		BoundElementMap boundElementsMap = (BoundElementMap) boundElements.get(evaluationTime);
		if (isPerPageBoundElements)
		{
			Map perPageElementsMap = boundElementsMap.getMap();
			for (Iterator it = perPageElementsMap.entrySet().iterator(); it.hasNext();)
			{
				Map.Entry entry = (Map.Entry) it.next();
				// Calling getElements() will page in the data for the page.
				JRPrintPage page = (JRPrintPage) entry.getKey();
				page.getElements();
				Map elementsMap = (Map) entry.getValue();
				resolveBoundElements(elementsMap, evaluation, evaluationTime);
			}

			boundElementsMap.clear();
		}
		else
		{
			resolveBoundElements(boundElementsMap.getMap(), evaluation, evaluationTime);
			boundElementsMap.clear();
		}
	}

	/**
	 * Resolves elements which are to be evaluated at report level.
	 */
	protected void resolveReportBoundElements() throws JRException
	{
		resolveBoundElements(JREvaluationTime.EVALUATION_TIME_REPORT, JRExpression.EVALUATION_DEFAULT);
	}

	/**
	 * Resolves elements which are to be evaluated at page level.
	 *
	 * @param evaluation
	 *            the evaluation type
	 */
	protected void resolvePageBoundElements(byte evaluation) throws JRException
	{
		resolveBoundElements(JREvaluationTime.EVALUATION_TIME_PAGE, evaluation);
	}

	/**
	 * Resolves elements which are to be evaluated at column level.
	 *
	 * @param evaluation
	 *            the evaluation type
	 */
	protected void resolveColumnBoundElements(byte evaluation) throws JRException
	{
		resolveBoundElements(JREvaluationTime.EVALUATION_TIME_COLUMN, evaluation);
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

					resolveBoundElements(JREvaluationTime.getGroupEvaluationTime(groupName), evaluation);
				}
			}
		}
	}

	protected JRPrintPage newPage()
	{
		JRPrintPage page;

		if (virtualizer != null)
		{
			JRVirtualPrintPage virtualPage = new JRVirtualPrintPage(jasperPrint, virtualizer, fillContext.getVirtualizationContext());

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
	 */
	protected Object getVariableValue(String variableName)
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
		resolveBoundElements(JREvaluationTime.getBandEvaluationTime(band), evaluation);
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
		if (!fillContext.cancelRunningQuery())
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
		if (subfillers != null && subfillers.remove(subfiller) && fillContext.isUsingVirtualizer())
		{
			removeIdentityDataProviders(subfiller);
		}
	}

	private static void addIdentityDataProviders(JRVirtualPrintPage page, JRBaseFiller filler)
	{
		page.addIdentityDataProvider(filler);

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

				page.removeIdentityDataProvider(filler);
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

		private final JRPrintPage printPage;

		protected PageIdentityDataProvider(JRPrintPage printPage)
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

		public static JRVirtualPrintPage.IdentityDataProvider getIdentityDataProvider(JRPrintPage printPage)
		{
			JRVirtualPrintPage.IdentityDataProvider provider = (JRVirtualPrintPage.IdentityDataProvider) providers.get(printPage);
			if (provider == null)
			{
				provider = new PageIdentityDataProvider(printPage);
				providers.put(printPage, provider);
			}
			return provider;
		}

		public static JRVirtualPrintPage.IdentityDataProvider removeIdentityDataProvider(JRPrintPage printPage)
		{
			JRVirtualPrintPage.IdentityDataProvider provider = (JRVirtualPrintPage.IdentityDataProvider) providers.remove(printPage);
			return provider;
		}
	}


	protected void addPageIdentityDataProvider()
	{
		JRVirtualPrintPage.IdentityDataProvider pageProvider = PageIdentityDataProvider.getIdentityDataProvider(printPage);
		JRVirtualPrintPage masterPage = (JRVirtualPrintPage) fillContext.getPrintPage();
		masterPage.addIdentityDataProvider(pageProvider);
	}


	protected void removePageIdentityDataProvider()
	{
		JRVirtualPrintPage.IdentityDataProvider pageProvider = PageIdentityDataProvider.removeIdentityDataProvider(printPage);
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
				fillDataset.initScriptlet();

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


	protected void addBoundElement(JRFillElement element, JRPrintElement printElement, byte evaluationType, JRGroup group, JRFillBand band)
	{
		JREvaluationTime evaluationTime = JREvaluationTime.getEvaluationTime(evaluationType, group, band);
		addBoundElement(element, printElement, evaluationTime);
	}


	protected void addBoundElement(JRFillElement element, JRPrintElement printElement, JREvaluationTime evaluationTime)
	{
		BoundElementMap boundElementsMap = (BoundElementMap) boundElements.get(evaluationTime);
		boundElementsMap.put(printElement, element);
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
		Map allElements = new HashMap();
		List identityList = new ArrayList();

		for (Iterator it = boundElements.values().iterator(); it.hasNext();)
		{
			BoundElementMap pageBoundElementsMap = (BoundElementMap) it.next();
			Map map = pageBoundElementsMap.getMap(page);
			if (map != null && !map.isEmpty())
			{
				Map idMap = new HashMap();

				for (Iterator iter = map.entrySet().iterator(); iter.hasNext();)
				{
					Map.Entry entry = (Map.Entry) iter.next();
					Object key = entry.getKey();
					Integer id = (Integer) allElements.get(key);
					if (id == null)
					{
						JRVirtualPrintPage.ObjectIDPair idPair = new JRVirtualPrintPage.ObjectIDPair(key);
						identityList.add(idPair);

						id = new Integer(idPair.getIdentity());
						allElements.put(key, id);
					}
					idMap.put(id, entry.getValue());
				}
				pageBoundElementsMap.putMap(page, idMap);
			}
		}

		JRVirtualPrintPage.ObjectIDPair[] identityData = null;
		if (!identityList.isEmpty())
		{
			identityData = new JRVirtualPrintPage.ObjectIDPair[identityList.size()];
			identityList.toArray(identityData);
		}

		return identityData;
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
		if (identityData == null || identityData.length == 0)
		{
			return;
		}

		for (Iterator it = boundElements.values().iterator(); it.hasNext();)
		{
			BoundElementMap pageBoundElementsMap = (BoundElementMap) it.next();
			Map idMap = pageBoundElementsMap.getMap(page);
			if (idMap != null && !idMap.isEmpty())
			{
				Map map = new HashMap();

				for (int i = 0; i < identityData.length; i++)
				{
					JRVirtualPrintPage.ObjectIDPair idPair = identityData[i];
					Integer id = new Integer(idPair.getIdentity());

					Object value = idMap.get(id);
					if (value != null)
					{
						map.put(idPair.getObject(), value);
					}
				}

				pageBoundElementsMap.putMap(page, map);
			}
		}
	}
	
	
	protected JRStyle getConditionalStyle(JRStyle initialStyle, String condStyleName)
	{
		JRStyle condStyle = null;
		Map condStyles = (Map) consolidatedStyles.get(initialStyle);
		if (condStyles != null)
		{
			condStyle = (JRStyle) condStyles.get(condStyleName);
		}
		return condStyle;
	}
	
	
	protected void putConditionalStyle(JRStyle initialStyle, JRStyle condStyle)
	{
		Map condStyles = (Map) consolidatedStyles.get(initialStyle);
		if (condStyles == null)
		{
			condStyles = new HashMap();
			consolidatedStyles.put(initialStyle, condStyles);
		}
		condStyles.put(condStyle.getName(), condStyle);
	}
	
	protected final boolean isEmpty(JRBand band)
	{
		return band == null || 
				(band.getHeight() == 0
				&& (band.getElements() == null || band.getElements().length == 0)
				&& band.getPrintWhenExpression() == null);
	}
	
	protected void setSubreportRunner(JRSubreportRunner runner)
	{
		this.subreportRunner = runner;
	}
	
	protected void suspendSubreportRunner() throws JRException
	{
		if (subreportRunner == null)
		{
			throw new JRRuntimeException("No subreport runner set.");
		}

		subreportRunner.suspend();
	}
}
