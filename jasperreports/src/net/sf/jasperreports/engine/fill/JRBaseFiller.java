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
import java.text.Format;
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
import java.util.TimeZone;

import net.sf.jasperreports.engine.JRAbstractScriptlet;
import net.sf.jasperreports.engine.JRConstants;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRDataset;
import net.sf.jasperreports.engine.JRDefaultStyleProvider;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExpression;
import net.sf.jasperreports.engine.JRGroup;
import net.sf.jasperreports.engine.JROrigin;
import net.sf.jasperreports.engine.JRParameter;
import net.sf.jasperreports.engine.JRPrintElement;
import net.sf.jasperreports.engine.JRPrintPage;
import net.sf.jasperreports.engine.JRReport;
import net.sf.jasperreports.engine.JRReportFont;
import net.sf.jasperreports.engine.JRReportTemplate;
import net.sf.jasperreports.engine.JRRuntimeException;
import net.sf.jasperreports.engine.JRStyle;
import net.sf.jasperreports.engine.JRStyleSetter;
import net.sf.jasperreports.engine.JRTemplate;
import net.sf.jasperreports.engine.JRTemplateReference;
import net.sf.jasperreports.engine.JRVirtualizer;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.base.JRBasePrintPage;
import net.sf.jasperreports.engine.base.JRVirtualPrintPage;
import net.sf.jasperreports.engine.util.DefaultFormatFactory;
import net.sf.jasperreports.engine.util.FileResolver;
import net.sf.jasperreports.engine.util.FormatFactory;
import net.sf.jasperreports.engine.util.JRDataUtils;
import net.sf.jasperreports.engine.util.JRGraphEnvInitializer;
import net.sf.jasperreports.engine.util.JRProperties;
import net.sf.jasperreports.engine.util.JRResourcesUtil;
import net.sf.jasperreports.engine.util.JRStyledTextParser;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id$
 */
public abstract class JRBaseFiller implements JRDefaultStyleProvider, JRVirtualPrintPage.IdentityDataProvider//, JRDefaultFontProvider
{

	private static final Log log = LogFactory.getLog(JRBaseFiller.class);

	private static final String PROPERTIES_PRINT_TRANSFER_PREFIX = JRProperties.PROPERTY_PREFIX + "print.transfer.";
	
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

	protected final String fillerId;

	/**
	 *
	 */
	protected JRBaseFiller parentFiller = null;

	private final JRFillObjectFactory factory;

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

	protected JRFillReportTemplate[] reportTemplates;

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

	protected JRFillBand noData = null;

	protected JRVirtualizer virtualizer = null;

	protected ClassLoader reportClassLoader = null;

	protected FormatFactory formatFactory = null;

	protected URLStreamHandlerFactory urlHandlerFactory;

	protected FileResolver fileResolver;

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
	 *
	 */
	protected Map dateFormatCache = new HashMap();
	protected Map numberFormatCache = new HashMap();

	private JRSubreportRunner subreportRunner;


	/**
	 *
	 */
	protected boolean isCreatingNewPage = false;
	protected boolean isNewPage = false;
	protected boolean isNewColumn = false;
	protected boolean isNewGroup = true;
	protected boolean isFirstPageBand = false;
	protected boolean isFirstColumnBand = false;

	protected int columnIndex = 0;

	protected int offsetX = 0;
	protected int offsetY = 0;
	protected int columnHeaderOffsetY = 0;
	protected int columnFooterOffsetY = 0;
	protected int lastPageColumnFooterOffsetY = 0;

	protected boolean isLastPageFooter = false;


	/**
	 *
	 */
	protected JRBaseFiller(JasperReport jasperReport, JREvaluator initEvaluator, JRBaseFiller parentFiller) throws JRException
	{
		this.fillerId = Integer.toString(System.identityHashCode(this));

		if (log.isDebugEnabled())
		{
			log.debug("Fill " + fillerId + ": created for " + jasperReport.getName());
		}

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
		
		List transferPrefixProps = JRProperties.getProperties(PROPERTIES_PRINT_TRANSFER_PREFIX);
		for (Iterator prefixIt = transferPrefixProps.iterator(); prefixIt.hasNext();)
		{
			JRProperties.PropertySuffix transferPrefixProp = (JRProperties.PropertySuffix) prefixIt.next();
			String transferPrefix = transferPrefixProp.getValue();
			if (transferPrefix != null && transferPrefix.length() > 0)
			{
				List transferProps = JRProperties.getProperties(jasperReport.getPropertiesMap(), transferPrefix);
				for (Iterator propIt = transferProps.iterator(); propIt.hasNext();)
				{
					JRProperties.PropertySuffix property = (JRProperties.PropertySuffix) propIt.next();
					String value = property.getValue();
					jasperPrint.setProperty(property.getKey(), value);
				}
			}
		}
		
		if (initEvaluator == null)
		{
			calculator = JRFillDataset.createCalculator(jasperReport, jasperReport.getMainDataset());
		}
		else
		{
			calculator = new JRCalculator(initEvaluator);
		}

		/*   */
		factory = new JRFillObjectFactory(this);

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

		createDatasets();
		mainDataset = factory.getDataset(jasperReport.getMainDataset());
		groups = mainDataset.groups;

		createReportTemplates(factory);

		String reportName = factory.getFiller().isSubreport() ? factory.getFiller().getJasperReport().getName() : null;
		
		/*   */
		missingFillBand = factory.getBand(null);

		background = factory.getBand(jasperReport.getBackground());
		if (background != missingFillBand)
		{
			background.setOrigin(
				new JROrigin(
					reportName,
					JROrigin.BACKGROUND
					)
				);
		}
		
		title = factory.getBand(jasperReport.getTitle());
		if (title != missingFillBand)
		{
			title.setOrigin(
				new JROrigin(
					reportName,
					JROrigin.TITLE
					)
				);
		}

		pageHeader = factory.getBand(jasperReport.getPageHeader());
		if (pageHeader != missingFillBand)
		{
			pageHeader.setOrigin(
				new JROrigin(
					reportName,
					JROrigin.PAGE_HEADER
					)
				);
		}
		
		columnHeader = factory.getBand(jasperReport.getColumnHeader());
		if (columnHeader != missingFillBand)
		{
			columnHeader.setOrigin(
				new JROrigin(
					reportName,
					JROrigin.COLUMN_HEADER
					)
				);
		}
		
		detail = factory.getBand(jasperReport.getDetail());
		if (detail != missingFillBand)
		{
			detail.setOrigin(
				new JROrigin(
					reportName,
					JROrigin.DETAIL
					)
				);
		}
		
		columnFooter = factory.getBand(jasperReport.getColumnFooter());
		if (columnFooter != missingFillBand)
		{
			columnFooter.setOrigin(
				new JROrigin(
					reportName,
					JROrigin.COLUMN_FOOTER
					)
				);
		}
		
		pageFooter = factory.getBand(jasperReport.getPageFooter());
		if (pageFooter != missingFillBand)
		{
			pageFooter.setOrigin(
				new JROrigin(
					reportName,
					JROrigin.PAGE_FOOTER
					)
				);
		}
		
		lastPageFooter = factory.getBand(jasperReport.getLastPageFooter());
		if (lastPageFooter != missingFillBand)
		{
			lastPageFooter.setOrigin(
				new JROrigin(
					reportName,
					JROrigin.LAST_PAGE_FOOTER
					)
				);
		}
		
		summary = factory.getBand(jasperReport.getSummary());
		if (summary != missingFillBand && summary.isEmpty())
		{
			summary = missingFillBand;
		}
		if (summary != missingFillBand)
		{
			summary.setOrigin(
				new JROrigin(
					reportName,
					JROrigin.SUMMARY
					)
				);
		}
		
		noData = factory.getBand(jasperReport.getNoData());
		if (noData != missingFillBand)
		{
			noData.setOrigin(
				new JROrigin(
					reportName,
					JROrigin.NO_DATA
					)
				);
		}

		mainDataset.initElementDatasets(factory);
		initDatasets(factory);

		mainDataset.checkVariableCalculationReqs(factory);

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
		bands.add(noData);

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
		noData.addNowEvaluationTimes(groupEvaluationTimes);
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

		if (log.isDebugEnabled())
		{
			log.debug("Fill " + fillerId + ": filling report");
		}

		fillingThread = Thread.currentThread();
		boolean fileResolverSet = false;
		boolean urlHandlerFactorySet = false;
		boolean classLoaderSet = false;
		try
		{
			classLoaderSet = setClassLoader(parameterValues);
			urlHandlerFactorySet = setUrlHandlerFactory(parameterValues);
			fileResolverSet = setFileResolver(parameterValues);

			setParameters(parameterValues);

			loadStyles();

			if (parentFiller != null)
			{
				parentFiller.registerSubfiller(this);
			}

			jasperPrint.setName(name);
			jasperPrint.setPageWidth(pageWidth);
			jasperPrint.setPageHeight(pageHeight);
			jasperPrint.setOrientation(orientation);

			jasperPrint.setDefaultFont(defaultFont);

			jasperPrint.setFormatFactoryClass(jasperReport.getFormatFactoryClass());
			jasperPrint.setLocaleCode(JRDataUtils.getLocaleCode(getLocale()));
			jasperPrint.setTimeZoneId(JRDataUtils.getTimeZoneId(getTimeZone()));

			/*   */
			if (fonts != null && fonts.length > 0)
			{
				for (int i = 0; i < fonts.length; i++)
				{
					jasperPrint.addFont(fonts[i], true);
				}
			}

			jasperPrint.setDefaultStyle(defaultStyle);

			/*   */
			if (styles != null && styles.length > 0)
			{
				for (int i = 0; i < styles.length; i++)
				{
					addPrintStyle(styles[i]);
				}
			}

			createBoundElemementMaps();

			/*   */
			mainDataset.start();

			/*   */
			fillReport();

			// add consolidates styles as normal styles in the print object
//			for (Iterator it = consolidatedStyles.values().iterator(); it.hasNext();)
//			{
//				jasperPrint.addStyle((JRStyle) it.next());
//			}

			if (log.isDebugEnabled())
			{
				log.debug("Fill " + fillerId + ": ended");
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

			if (fileResolverSet)
			{
				JRResourcesUtil.resetThreadFileResolver();
			}
		}
	}

	protected void addPrintStyle(JRStyle style) throws JRException
	{
		jasperPrint.addStyle(style, true);
	}
	
	protected static interface DefaultStyleListener
	{
		void defaultStyleSet(JRStyle style);
	}

	private final List defaultStyleListeners = new ArrayList();

	protected void addDefaultStyleListener(DefaultStyleListener listener)
	{
		defaultStyleListeners.add(listener);
	}

	protected void setDefaultStyle(JRStyle style)
	{
		defaultStyle = style;

		for (Iterator it = defaultStyleListeners.iterator(); it.hasNext();)
		{
			DefaultStyleListener listener = (DefaultStyleListener) it.next();
			listener.defaultStyleSet(style);
		}
	}

	protected void loadStyles() throws JRException
	{
		List styleList = collectStyles();
		JRStyle reportDefaultStyle = jasperReport.getDefaultStyle();
		if (reportDefaultStyle == null)
		{
			lookupExternalDefaultStyle(styleList);
		}

		List includedStyles = factory.setStyles(styleList);

		styles = (JRStyle[]) includedStyles.toArray(new JRStyle[includedStyles.size()]);

		if (reportDefaultStyle != null)
		{
			setDefaultStyle(factory.getStyle(reportDefaultStyle));
		}
	}


	private static final JRStyleSetter DUMMY_STYLE_SETTER = new JRStyleSetter()
	{
		public void setStyle(JRStyle style)
		{
		}

		public void setStyleNameReference(String name)
		{
		}
	};

	protected List collectStyles() throws JRException
	{
		List styleList = collectTemplateStyles();

		JRStyle[] reportStyles = jasperReport.getStyles();
		if (reportStyles != null)
		{
			styles = new JRStyle[reportStyles.length];

			for (int i = 0; i < reportStyles.length; i++)
			{
				JRStyle style = reportStyles[i];
				styleList.add(style);

				//add dummy style requester so that report styles are always included
				//in the final list
				factory.registerDelayedStyleSetter(DUMMY_STYLE_SETTER, style.getName());
			}
		}

		return styleList;
	}

	protected List collectTemplateStyles() throws JRException
	{
		List externalStyles = new ArrayList();
		HashSet loadedLocations = new HashSet();

		if (reportTemplates != null)
		{
			for (int i = 0; i < reportTemplates.length; i++)
			{
				JRFillReportTemplate reportTemplate = reportTemplates[i];
				JRTemplate template = reportTemplate.evaluate();
				collectStyles(template, externalStyles, loadedLocations);
			}
		}

		Collection paramTemplates = (Collection) mainDataset.getParameterValue(JRParameter.REPORT_TEMPLATES, true);
		if (paramTemplates != null)
		{
			for (Iterator it = paramTemplates.iterator(); it.hasNext();)
			{
				JRTemplate template = (JRTemplate) it.next();
				collectStyles(template, externalStyles, loadedLocations);
			}
		}

		return externalStyles;
	}

	protected void collectStyles(JRTemplate template, List externalStyles, Set loadedLocations) throws JRException
	{
		collectIncludedTemplates(template, externalStyles, loadedLocations);

		JRStyle[] templateStyles = template.getStyles();
		if (templateStyles != null)
		{
			for (int i = 0; i < templateStyles.length; i++)
			{
				JRStyle style = templateStyles[i];
				String styleName = style.getName();
				if (styleName == null)
				{
					throw new JRRuntimeException("External style name not set.");
				}

				externalStyles.add(style);
			}
		}
	}

	protected void collectIncludedTemplates(JRTemplate template, List externalStyles, Set loadedLocations) throws JRException
	{
		JRTemplateReference[] includedTemplates = template.getIncludedTemplates();
		if (includedTemplates != null)
		{
			for (int i = 0; i < includedTemplates.length; i++)
			{
				JRTemplateReference reference = includedTemplates[i];
				String location = reference.getLocation();

				if (!loadedLocations.add(location))
				{
					throw new JRRuntimeException("Circular dependency found for template at location " + location);
				}

				JRTemplate includedTemplate = JRFillReportTemplate.loadTemplate(location, String.class, fillContext);
				collectStyles(includedTemplate, externalStyles, loadedLocations);
			}
		}
	}

	protected void lookupExternalDefaultStyle(Collection styleList)
	{
		JRStyle defStyle = null;
		for (Iterator it = styleList.iterator(); it.hasNext();)
		{
			JRStyle style = (JRStyle) it.next();
			if (style.isDefault())
			{
				defStyle = style;
			}
		}

		if (defStyle != null)
		{
			factory.registerDelayedStyleSetter(new JRStyleSetter()
			{
				public void setStyle(JRStyle style)
				{
					if (style.isDefault())
					{
						setDefaultStyle(style);
					}
				}

				public void setStyleNameReference(String name)
				{
				}
			}, defStyle.getName());
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
					if (log.isDebugEnabled())
					{
						log.debug("Fill " + fillerId + ": Interrupting subfiller thread " + subfiller.fillingThread);
					}

					subfiller.fillingThread.interrupt();
				}
			}
		}
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
				if (log.isDebugEnabled())
				{
					log.debug("Fill " + fillerId + ": using virtualizer " + virtualizer);
				}

				fillContext.setUsingVirtualizer(true);
				fillContext.setPerPageBoundElements(true);
				JRVirtualizationContext.register(fillContext.getVirtualizationContext(), jasperPrint);
			}
		}

		isPerPageBoundElements = fillContext.isPerPageBoundElements();

		setFormatFactory(parameterValues);

		setIgnorePagination(parameterValues);

		mainDataset.setParameterValues(parameterValues);
		mainDataset.initDatasource();

		this.scriptlet = mainDataset.scriptlet;

		if (!isSubreport())
		{
			fillContext.setMasterFormatFactory(getFormatFactory());
			fillContext.setMasterLocale(getLocale());
			fillContext.setMasterTimeZone(getTimeZone());
		}
	}


	private void setFormatFactory(Map parameterValues)
	{
		formatFactory = (FormatFactory)parameterValues.get(JRParameter.REPORT_FORMAT_FACTORY);
		if (formatFactory == null)
		{
			formatFactory = DefaultFormatFactory.createFormatFactory(jasperReport.getFormatFactoryClass());
			parameterValues.put(JRParameter.REPORT_FORMAT_FACTORY, formatFactory);
		}
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


	private boolean setFileResolver(Map parameterValues)
	{
		fileResolver = (FileResolver) parameterValues.get(JRParameter.REPORT_FILE_RESOLVER);
		boolean setFileResolver = fileResolver != null;
		if (setFileResolver)
		{
			JRResourcesUtil.setThreadFileResolver(fileResolver);
		}
		return setFileResolver;
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
	 * Returns the report time zone.
	 *
	 * @return the report time zone
	 */
	protected TimeZone getTimeZone()
	{
		return mainDataset.timeZone;
	}


	/**
	 * Returns the report format factory.
	 *
	 * @return the report format factory
	 */
	protected FormatFactory getFormatFactory()
	{
		return formatFactory;
	}


	/**
	 *
	 */
	protected Format getDateFormat(String pattern)
	{
		Locale lc = getLocale();
		TimeZone tz = getTimeZone();
		String key = pattern + "|" + JRDataUtils.getLocaleCode(lc) + "|" + JRDataUtils.getTimeZoneId(tz);
		Format format = (Format)dateFormatCache.get(key);
		if (format == null)
		{
			format = getFormatFactory().createDateFormat(pattern, lc, tz);
			if (format != null)
			{
				dateFormatCache.put(key, format);
			}
		}
		return format;
	}


	/**
	 *
	 */
	protected Format getNumberFormat(String pattern)
	{
		Locale lc = getLocale();
		String key = pattern + "|" + JRDataUtils.getLocaleCode(lc);
		Format format = (Format)numberFormatCache.get(key);
		if (format == null)
		{
			format = getFormatFactory().createNumberFormat(pattern, lc);
			if (format != null)
			{
				numberFormatCache.put(key, format);
			}
		}
		return format;
	}


	protected boolean hasMasterFormatFactory()
	{
		return
			!isSubreport()
			|| getFormatFactory().getClass().getName().equals(
				fillContext.getMasterFormatFactory().getClass().getName()
				);
	}


	protected boolean hasMasterLocale()
	{
		return !isSubreport() || getLocale().equals(fillContext.getMasterLocale());
	}


	protected boolean hasMasterTimeZone()
	{
		return !isSubreport() || getTimeZone().equals(fillContext.getMasterTimeZone());
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
	 *
	 * @return the variable value
	 *
	 * @throws JRRuntimeException when the variable does not exist
	 */
	public Object getVariableValue(String variableName)
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
		if (log.isDebugEnabled())
		{
			log.debug("Fill " + fillerId + ": cancelling");
		}

		if (fillContext.cancelRunningQuery())
		{
			if (log.isDebugEnabled())
			{
				log.debug("Fill " + fillerId + ": query cancelled");
			}
		}
		else
		{
			Thread t = fillingThread;
			if (t != null)
			{
				if (log.isDebugEnabled())
				{
					log.debug("Fill " + fillerId + ": Interrupting thread " + t);
				}

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
			if (log.isDebugEnabled())
			{
				log.debug("Fill " + fillerId + ": adding page " + (jasperPrint.getPages().size() + 1));
			}

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


	private void createDatasets() throws JRException
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


//	protected JRStyle getConsolidatedStyle(String consolidatedStyleName)
//	{
//		return (JRStyle) consolidatedStyles.get(consolidatedStyleName);
//	}
//
//
//	protected void putConsolidatedStyle(JRStyle consolidatedStyle)
//	{
//		consolidatedStyles.put(consolidatedStyle.getName(), consolidatedStyle);
//	}

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

		if (log.isDebugEnabled())
		{
			log.debug("Fill " + fillerId + ": suspeding subreport runner");
		}

		subreportRunner.suspend();
	}


	protected void createReportTemplates(JRFillObjectFactory factory)
	{
		JRReportTemplate[] templates = jasperReport.getTemplates();
		if (templates != null)
		{
			reportTemplates = new JRFillReportTemplate[templates.length];

			for (int i = 0; i < templates.length; i++)
			{
				JRReportTemplate template = templates[i];
				reportTemplates[i] = factory.getReportTemplate(template);
			}
		}
	}

}
