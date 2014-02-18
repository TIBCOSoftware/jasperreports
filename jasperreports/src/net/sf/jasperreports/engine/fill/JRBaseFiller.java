/*
 * JasperReports - Free Java Reporting Library.
 * Copyright (C) 2001 - 2013 Jaspersoft Corporation. All rights reserved.
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

/*
 * Contributors:
 * John Bindel - jbindel@users.sourceforge.net
 */

package net.sf.jasperreports.engine.fill;

import java.sql.Connection;
import java.text.Format;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.TimeZone;
import java.util.concurrent.ConcurrentHashMap;

import net.sf.jasperreports.engine.BookmarkHelper;
import net.sf.jasperreports.engine.JRAbstractScriptlet;
import net.sf.jasperreports.engine.JRBand;
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
import net.sf.jasperreports.engine.JRPropertiesUtil;
import net.sf.jasperreports.engine.JRReportTemplate;
import net.sf.jasperreports.engine.JRRuntimeException;
import net.sf.jasperreports.engine.JRStyle;
import net.sf.jasperreports.engine.JRStyleSetter;
import net.sf.jasperreports.engine.JRTemplate;
import net.sf.jasperreports.engine.JRTemplateReference;
import net.sf.jasperreports.engine.JRVirtualizable;
import net.sf.jasperreports.engine.JRVirtualizer;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.JasperReportsContext;
import net.sf.jasperreports.engine.PrintElementVisitor;
import net.sf.jasperreports.engine.ReportContext;
import net.sf.jasperreports.engine.base.JRBasePrintPage;
import net.sf.jasperreports.engine.base.JRVirtualPrintPage;
import net.sf.jasperreports.engine.base.VirtualElementsData;
import net.sf.jasperreports.engine.base.VirtualizablePageElements;
import net.sf.jasperreports.engine.type.BandTypeEnum;
import net.sf.jasperreports.engine.type.CalculationEnum;
import net.sf.jasperreports.engine.type.EvaluationTimeEnum;
import net.sf.jasperreports.engine.type.FooterPositionEnum;
import net.sf.jasperreports.engine.type.OrientationEnum;
import net.sf.jasperreports.engine.type.PrintOrderEnum;
import net.sf.jasperreports.engine.type.RunDirectionEnum;
import net.sf.jasperreports.engine.type.WhenNoDataTypeEnum;
import net.sf.jasperreports.engine.type.WhenResourceMissingTypeEnum;
import net.sf.jasperreports.engine.util.DefaultFormatFactory;
import net.sf.jasperreports.engine.util.FormatFactory;
import net.sf.jasperreports.engine.util.JRDataUtils;
import net.sf.jasperreports.engine.util.JRGraphEnvInitializer;
import net.sf.jasperreports.engine.util.JRStyledTextParser;
import net.sf.jasperreports.engine.util.LinkedMap;
import net.sf.jasperreports.engine.util.LocalJasperReportsContext;
import net.sf.jasperreports.engine.util.UniformPrintElementVisitor;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id$
 */
public abstract class JRBaseFiller implements JRDefaultStyleProvider
{

	private static final Log log = LogFactory.getLog(JRBaseFiller.class);
	
	private static final int PAGE_HEIGHT_PAGINATION_IGNORED = 0x7d000000;//less than Integer.MAX_VALUE to avoid 

	protected final Map<Integer, JRFillElement> fillElements = new HashMap<Integer, JRFillElement>();
	protected final int fillerId;

	/**
	 *
	 */
	protected JRBaseFiller parentFiller;
	protected JRFillSubreport parentElement;

	private final JRFillObjectFactory factory;

	private JRStyledTextParser styledTextParser = JRStyledTextParser.getInstance();

	private FillListener fillListener;
	
	/**
	 *
	 */
	private boolean isInterrupted;

	/**
	 *
	 */
	protected String name;

	protected int columnCount = 1;

	protected PrintOrderEnum printOrder = PrintOrderEnum.VERTICAL;

	protected RunDirectionEnum columnDirection = RunDirectionEnum.LTR;

	protected int pageWidth;

	protected int pageHeight;

	protected OrientationEnum orientation = OrientationEnum.PORTRAIT;

	protected WhenNoDataTypeEnum whenNoDataType = WhenNoDataTypeEnum.NO_PAGES;

	protected int columnWidth;

	protected int columnSpacing;

	protected int leftMargin;

	protected int rightMargin;

	protected int topMargin;

	protected int bottomMargin;

	protected boolean isTitleNewPage;

	protected boolean isSummaryNewPage;

	protected boolean isSummaryWithPageHeaderAndFooter;

	protected boolean isFloatColumnFooter;

	/**
	 * the resource missing handling type
	 */
	protected WhenResourceMissingTypeEnum whenResourceMissingType = WhenResourceMissingTypeEnum.NULL;

	protected JRFillReportTemplate[] reportTemplates;
	
	protected List<JRTemplate> templates;

	protected JRStyle defaultStyle;

	protected JRStyle[] styles;

	/**
	 * Main report dataset.
	 */
	protected JRFillDataset mainDataset;

	protected JRFillGroup[] groups;

	protected JRFillSection missingFillSection;
	protected JRFillBand missingFillBand;

	protected JRFillBand background;

	protected JRFillBand title;

	protected JRFillBand pageHeader;

	protected JRFillBand columnHeader;

	protected JRFillSection detailSection;

	protected JRFillBand columnFooter;

	protected JRFillBand pageFooter;

	protected JRFillBand lastPageFooter;

	protected JRFillBand summary;

	protected JRFillBand noData;

	protected JRVirtualizationContext virtualizationContext;
	protected ElementEvaluationVirtualizationListener virtualizationListener;

	protected FormatFactory formatFactory;

	protected JRFillContext fillContext;

	/**
	 * Bound element maps indexed by {@link JREvaluationTime JREvaluationTime} objects.
	 */
	// we can use HashMap because the map is initialized in the beginning and doesn't change afterwards
	protected HashMap<JREvaluationTime, LinkedHashMap<PageKey, LinkedMap<Object, EvaluationBoundAction>>> boundElements;

	/**
	 *
	 */
	protected JasperPrint jasperPrint;

	protected JRPrintPage printPage;
	
	protected BookmarkHelper bookmarkHelper;

	protected int printPageStretchHeight;

	/**
	 * List of {@link JRFillBand JRFillBand} objects containing all bands of the
	 * report.
	 */
	protected List<JRBand> bands;

	/**
	 * Collection of subfillers
	 */
	protected Map<Integer, JRBaseFiller> subfillers;

	private Thread fillingThread;

	protected JRCalculator calculator;

	protected JRAbstractScriptlet scriptlet;

	/**
	 * Map of datasets ({@link JRFillDataset JRFillDataset} objects} indexed by name.
	 */
	protected Map<String,JRFillDataset> datasetMap;

	/**
	 *
	 */
	private JasperReportsContext jasperReportsContext;
	private JRPropertiesUtil propertiesUtil;
	private List<String> printTransferPropertyPrefixes;

	/**
	 * The report.
	 */
	protected JasperReport jasperReport;

	private boolean bandOverFlowAllowed;

	/**
	 *
	 */
	protected Map<String,Format> dateFormatCache = new HashMap<String,Format>();
	protected Map<String,Format> numberFormatCache = new HashMap<String,Format>();

	private JRSubreportRunner subreportRunner;

	protected SavePoint keepTogetherSavePoint;
	

	/**
	 *
	 */
	protected boolean isCreatingNewPage;
	protected boolean isNewPage;
	protected boolean isNewColumn;
	protected boolean isNewGroup = true;
	protected boolean isFirstPageBand;
	protected boolean isFirstColumnBand;

	protected int columnIndex;

	protected int offsetX;
	protected int offsetY;
	protected int columnHeaderOffsetY;
	protected int columnFooterOffsetY;
	protected int lastPageColumnFooterOffsetY;

	protected boolean isLastPageFooter;

	/**
	 *
	 */
	protected JRBaseFiller(
		JasperReportsContext jasperReportsContext, 
		JasperReport jasperReport, 
		JREvaluator initEvaluator, 
		JRFillSubreport parentElement
		) throws JRException
	{
		this(jasperReportsContext, jasperReport, (DatasetExpressionEvaluator) initEvaluator, parentElement);
	}

	/**
	 *
	 */
	protected JRBaseFiller(
		JasperReportsContext jasperReportsContext, 
		JasperReport jasperReport, 
		DatasetExpressionEvaluator initEvaluator, 
		JRFillSubreport parentElement
		) throws JRException
	{
		JRGraphEnvInitializer.initializeGraphEnv();

		setJasperReportsContext(jasperReportsContext);
		
		this.jasperReport = jasperReport;

		/*   */
		this.parentElement = parentElement;
		if (parentElement != null)
		{
			this.parentFiller = parentElement.filler;
		}

		if (parentFiller == null)
		{
			fillContext = new JRFillContext(this);
			printTransferPropertyPrefixes = readPrintTransferPropertyPrefixes();
		}
		else
		{
			fillContext = parentFiller.fillContext;
			printTransferPropertyPrefixes = parentFiller.printTransferPropertyPrefixes;
		}
		
		this.fillerId = fillContext.generatedFillerId();
		if (log.isDebugEnabled())
		{
			log.debug("Fill " + fillerId + ": created for " + jasperReport.getName());
		}

		/*   */
		name = jasperReport.getName();
		columnCount = jasperReport.getColumnCount();
		printOrder = jasperReport.getPrintOrderValue();
		columnDirection = jasperReport.getColumnDirection();
		pageWidth = jasperReport.getPageWidth();
		pageHeight = jasperReport.getPageHeight();
		orientation = jasperReport.getOrientationValue();
		whenNoDataType = jasperReport.getWhenNoDataTypeValue();
		columnWidth = jasperReport.getColumnWidth();
		columnSpacing = jasperReport.getColumnSpacing();
		leftMargin = jasperReport.getLeftMargin();
		rightMargin = jasperReport.getRightMargin();
		topMargin = jasperReport.getTopMargin();
		bottomMargin = jasperReport.getBottomMargin();
		isTitleNewPage = jasperReport.isTitleNewPage();
		isSummaryNewPage = jasperReport.isSummaryNewPage();
		isSummaryWithPageHeaderAndFooter = jasperReport.isSummaryWithPageHeaderAndFooter();
		isFloatColumnFooter = jasperReport.isFloatColumnFooter();
		whenResourceMissingType = jasperReport.getWhenResourceMissingTypeValue();

		jasperPrint = new JasperPrint();

		boolean isCreateBookmarks = 
			propertiesUtil.getBooleanProperty(
				jasperReport, 
				JasperPrint.PROPERTY_CREATE_BOOKMARKS,
				false
				);
		if (isCreateBookmarks)
		{
			bookmarkHelper = 
				new BookmarkHelper(
					propertiesUtil.getBooleanProperty(
						jasperReport, 
						JasperPrint.PROPERTY_COLLAPSE_MISSING_BOOKMARK_LEVELS,
						false
						)
					);
		}
		
		getPropertiesUtil().transferProperties(jasperReport, jasperPrint, 
				JasperPrint.PROPERTIES_PRINT_TRANSFER_PREFIX);
		
		if (initEvaluator == null)
		{
			calculator = JRFillDataset.createCalculator(jasperReportsContext, jasperReport, jasperReport.getMainDataset());
		}
		else
		{
			calculator = new JRCalculator(initEvaluator);
		}

		/*   */
		factory = new JRFillObjectFactory(this);

		/*   */
		missingFillBand = new JRFillBand(this, null, factory);
		missingFillSection = new JRFillSection(this, null, factory);

		createDatasets();
		mainDataset = factory.getDataset(jasperReport.getMainDataset());
		
		if (parentFiller == null)
		{
			FillDatasetPosition masterFillPosition = new FillDatasetPosition(null);
			mainDataset.setFillPosition(masterFillPosition);
		}
		
		groups = mainDataset.groups;

		createReportTemplates(factory);

		String reportName = factory.getFiller().isSubreport() ? factory.getFiller().getJasperReport().getName() : null;
		
		background = factory.getBand(jasperReport.getBackground());
		if (background != missingFillBand)
		{
			background.setOrigin(
				new JROrigin(
					reportName,
					BandTypeEnum.BACKGROUND
					)
				);
		}
		
		title = factory.getBand(jasperReport.getTitle());
		if (title != missingFillBand)
		{
			title.setOrigin(
				new JROrigin(
					reportName,
					BandTypeEnum.TITLE
					)
				);
		}

		pageHeader = factory.getBand(jasperReport.getPageHeader());
		if (pageHeader != missingFillBand)
		{
			pageHeader.setOrigin(
				new JROrigin(
					reportName,
					BandTypeEnum.PAGE_HEADER
					)
				);
		}
		
		columnHeader = factory.getBand(jasperReport.getColumnHeader());
		if (columnHeader != missingFillBand)
		{
			columnHeader.setOrigin(
				new JROrigin(
					reportName,
					BandTypeEnum.COLUMN_HEADER
					)
				);
		}
		
		detailSection = factory.getSection(jasperReport.getDetailSection());
		if (detailSection != missingFillSection)
		{
			detailSection.setOrigin(
				new JROrigin(
					reportName,
					BandTypeEnum.DETAIL
					)
				);
		}
		
		columnFooter = factory.getBand(jasperReport.getColumnFooter());
		if (columnFooter != missingFillBand)
		{
			columnFooter.setOrigin(
				new JROrigin(
					reportName,
					BandTypeEnum.COLUMN_FOOTER
					)
				);
		}
		
		pageFooter = factory.getBand(jasperReport.getPageFooter());
		if (pageFooter != missingFillBand)
		{
			pageFooter.setOrigin(
				new JROrigin(
					reportName,
					BandTypeEnum.PAGE_FOOTER
					)
				);
		}
		
		lastPageFooter = factory.getBand(jasperReport.getLastPageFooter());
		if (lastPageFooter != missingFillBand)
		{
			lastPageFooter.setOrigin(
				new JROrigin(
					reportName,
					BandTypeEnum.LAST_PAGE_FOOTER
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
					BandTypeEnum.SUMMARY
					)
				);
		}
		
		noData = factory.getBand(jasperReport.getNoData());
		if (noData != missingFillBand)
		{
			noData.setOrigin(
				new JROrigin(
					reportName,
					BandTypeEnum.NO_DATA
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
	protected Map<String,JRFillParameter> getParametersMap()
	{
		return mainDataset.parametersMap;
	}

	
	/**
	 * Returns the map of parameter values.
	 * 
	 * @return the map of parameter values
	 */
	public Map<String,Object> getParameterValuesMap()
	{
		return mainDataset.getParameterValuesMap();
	}

	/**
	 * Returns the report fields indexed by name.
	 *
	 * @return the report fields map
	 */
	protected Map<String,JRFillField> getFieldsMap()
	{
		return mainDataset.fieldsMap;
	}


	/**
	 * Returns the report variables indexed by name.
	 *
	 * @return the report variables map
	 */
	protected Map<String,JRFillVariable> getVariablesMap()
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
		return mainDataset.getVariable(variableName);
	}


	/**
	 * Returns a report field.
	 *
	 * @param fieldName the field name
	 * @return the field
	 */
	protected JRFillField getField(String fieldName)
	{
		return mainDataset.getFillField(fieldName);
	}

	private void initBands()
	{
		bands = new ArrayList<JRBand>(8 + (groups == null ? 0 : (2 * groups.length)));
		bands.add(title);
		bands.add(summary);
		bands.add(pageHeader);
		bands.add(pageFooter);
		bands.add(lastPageFooter);
		bands.add(columnHeader);
		bands.add(columnFooter);
		if (detailSection.getBands() != null)
		{
			bands.addAll(Arrays.asList(detailSection.getBands()));
		}
		bands.add(noData);

		if (groups != null && groups.length > 0)
		{
			for (int i = 0; i < groups.length; i++)
			{
				JRFillGroup group = groups[i];
				if (group.getGroupHeaderSection().getBands() != null)
				{
					bands.addAll(Arrays.asList(group.getGroupHeaderSection().getBands()));
				}
				if (group.getGroupFooterSection().getBands() != null)
				{
					bands.addAll(Arrays.asList(group.getGroupFooterSection().getBands()));
				}
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
				JRFillSection footer = (JRFillSection) group.getGroupFooterSection();

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
	public JasperReportsContext getJasperReportsContext()
	{
		return jasperReportsContext;
	}

	/**
	 *
	 */
	protected void setJasperReportsContext(JasperReportsContext jasperReportsContext)
	{
		this.jasperReportsContext = jasperReportsContext;
		this.propertiesUtil = JRPropertiesUtil.getInstance(jasperReportsContext);
	}

	/**
	 *
	 */
	public JRPropertiesUtil getPropertiesUtil()
	{
		return propertiesUtil;
	}

	private List<String> readPrintTransferPropertyPrefixes()
	{
		List<JRPropertiesUtil.PropertySuffix> transferProperties = propertiesUtil.getProperties(
				JasperPrint.PROPERTIES_PRINT_TRANSFER_PREFIX);
		List<String> prefixes = new ArrayList<String>(transferProperties.size());
		for (JRPropertiesUtil.PropertySuffix property : transferProperties)
		{
			String transferPrefix = property.getValue();
			if (transferPrefix != null && transferPrefix.length() > 0)
			{
				prefixes.add(transferPrefix);
			}
		}
		return prefixes;
	}

	protected List<String> getPrintTransferPropertyPrefixes()
	{
		return printTransferPropertyPrefixes;
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
	 * Returns the number of generated master print pages.
	 * 
	 * @return the number of generated master print pages
	 */
	public int getCurrentPageCount()
	{
		return getMasterFiller().jasperPrint.getPages().size();
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

	protected boolean isMasterReport()
	{
		return parentFiller == null;
	}

	protected boolean isSubreportRunToBottom()
	{
		return parentElement != null && parentElement.isRunToBottom() != null
				&& parentElement.isRunToBottom().booleanValue();
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

	protected void checkInterrupted()
	{
		if (Thread.interrupted())
		{
			setInterrupted(true);
		}
		
		if (isInterrupted())
		{
			if (log.isDebugEnabled())
			{
				log.debug("Fill " + fillerId + ": interrupting");
			}

			throw new JRFillInterruptedException();
		}
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
	protected int getCurrentPageStretchHeight()
	{
		return printPageStretchHeight;
	}

	/**
	 *
	 */
	protected abstract void setPageHeight(int pageHeight);

	
	/**
	 * Adds a fill lister to be notified by events that occur during the fill.
	 * 
	 * @param fillListener the listener to add
	 */
	public void addFillListener(FillListener fillListener)
	{
		this.fillListener = CompositeFillListener.addListener(this.fillListener, fillListener);
	}

	public JasperPrint fill(Map<String,Object> parameterValues, Connection conn) throws JRException
	{
		if (parameterValues == null)
		{
			parameterValues = new HashMap<String,Object>();
		}

		setConnectionParameterValue(parameterValues, conn);

		return fill(parameterValues);
	}


	protected void setConnectionParameterValue(Map<String,Object> parameterValues, Connection conn)
	{
		mainDataset.setConnectionParameterValue(parameterValues, conn);
	}


	public JasperPrint fill(Map<String,Object> parameterValues, JRDataSource ds) throws JRException
	{
		if (parameterValues == null)
		{
			parameterValues = new HashMap<String,Object>();
		}

		setDatasourceParameterValue(parameterValues, ds);

		return fill(parameterValues);
	}


	protected void setDatasourceParameterValue(Map<String,Object> parameterValues, JRDataSource ds)
	{
		mainDataset.setDatasourceParameterValue(parameterValues, ds);
	}


	public JasperPrint fill(Map<String,Object> parameterValues) throws JRException
	{
		setParametersToContext(parameterValues);
		
		if (parameterValues == null)
		{
			parameterValues = new HashMap<String,Object>();
		}

		if (log.isDebugEnabled())
		{
			log.debug("Fill " + fillerId + ": filling report");
		}

		fillingThread = Thread.currentThread();
		
		JRResourcesFillUtil.ResourcesFillContext resourcesContext = 
			JRResourcesFillUtil.setResourcesFillContext(parameterValues);
		
		boolean success = false;
		try
		{
			createBoundElemementMaps();

			if (parentFiller != null)
			{
				parentFiller.registerSubfiller(this);
			}

			setParameters(parameterValues);

			loadStyles();

			jasperPrint.setName(name);
			jasperPrint.setPageWidth(pageWidth);
			jasperPrint.setPageHeight(pageHeight);
			jasperPrint.setTopMargin(topMargin);
			jasperPrint.setLeftMargin(leftMargin);
			jasperPrint.setBottomMargin(bottomMargin);
			jasperPrint.setRightMargin(rightMargin);
			jasperPrint.setOrientation(orientation);

			jasperPrint.setFormatFactoryClass(jasperReport.getFormatFactoryClass());
			jasperPrint.setLocaleCode(JRDataUtils.getLocaleCode(getLocale()));
			jasperPrint.setTimeZoneId(JRDataUtils.getTimeZoneId(getTimeZone()));

			jasperPrint.setDefaultStyle(defaultStyle);

			/*   */
			if (styles != null && styles.length > 0)
			{
				for (int i = 0; i < styles.length; i++)
				{
					addPrintStyle(styles[i]);
				}
			}

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

			success = true;
			return jasperPrint;
		}
		finally
		{
			mainDataset.closeDatasource();
			mainDataset.disposeParameterContributors();
			
			if (success && parentFiller == null)
			{
				// commit the cached data
				fillContext.cacheDone();
			}

			if (parentFiller != null)
			{
				parentFiller.unregisterSubfiller(this);
			}
			
			if (fillContext.isUsingVirtualizer())
			{
				// removing the listener
				virtualizationContext.removeListener(virtualizationListener);
			}

			fillingThread = null;

			//kill the subreport filler threads
			killSubfillerThreads();
			
			if (parentFiller == null)
			{
				fillContext.dispose();
			}

			JRResourcesFillUtil.revertResourcesFillContext(resourcesContext);
		}
	}

	private void setParametersToContext(Map<String,Object> parameterValues)
	{
		JasperReportsContext localContext = LocalJasperReportsContext.getLocalContext(jasperReportsContext, parameterValues);
		if (localContext != jasperReportsContext)
		{
			setJasperReportsContext(localContext);
		}
	}
		
	public void addPrintStyle(JRStyle style) throws JRException
	{
		jasperPrint.addStyle(style, true);
	}
	
	protected static interface DefaultStyleListener
	{
		void defaultStyleSet(JRStyle style);
	}

	private final List<DefaultStyleListener> defaultStyleListeners = new ArrayList<DefaultStyleListener>();

	protected void addDefaultStyleListener(DefaultStyleListener listener)
	{
		defaultStyleListeners.add(listener);
	}

	protected void setDefaultStyle(JRStyle style)
	{
		defaultStyle = style;

		for (Iterator<DefaultStyleListener> it = defaultStyleListeners.iterator(); it.hasNext();)
		{
			DefaultStyleListener listener = it.next();
			listener.defaultStyleSet(style);
		}
	}

	protected void loadStyles() throws JRException
	{
		List<JRStyle> styleList = collectStyles();
		JRStyle reportDefaultStyle = jasperReport.getDefaultStyle();
		if (reportDefaultStyle == null)
		{
			lookupExternalDefaultStyle(styleList);
		}

		List<JRStyle> includedStyles = factory.setStyles(styleList);

		styles = includedStyles.toArray(new JRStyle[includedStyles.size()]);

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

	protected List<JRStyle> collectStyles() throws JRException
	{
		List<JRStyle> styleList = collectTemplateStyles();

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

	protected void collectTemplates() throws JRException
	{
		templates = new ArrayList<JRTemplate>();

		if (reportTemplates != null)
		{
			for (JRFillReportTemplate reportTemplate : reportTemplates)
			{
				JRTemplate template = reportTemplate.evaluate();
				if (template != null)
				{
					templates.add(template);
				}
			}
		}

		Collection<JRTemplate> paramTemplates = (Collection<JRTemplate>) mainDataset.getParameterValue(
				JRParameter.REPORT_TEMPLATES, true);
		if (paramTemplates != null)
		{
			templates.addAll(paramTemplates);
		}
	}

	public List<JRTemplate> getTemplates()
	{
		return templates;
	}
	
	protected List<JRStyle> collectTemplateStyles() throws JRException
	{
		collectTemplates();
		
		List<JRStyle> externalStyles = new ArrayList<JRStyle>();
		HashSet<String> loadedLocations = new HashSet<String>();
		for (JRTemplate template : templates)
		{
			collectStyles(template, externalStyles, loadedLocations);
		}
		return externalStyles;
	}

	protected void collectStyles(JRTemplate template, List<JRStyle> externalStyles, Set<String> loadedLocations) throws JRException
	{
		HashSet<String> parentLocations = new HashSet<String>();
		collectStyles(template, externalStyles, loadedLocations, parentLocations);
	}
	
	protected void collectStyles(JRTemplate template, List<JRStyle> externalStyles, 
			Set<String> loadedLocations, Set<String> templateParentLocations) throws JRException
	{
		collectIncludedTemplates(template, externalStyles, 
				loadedLocations, templateParentLocations);

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

	protected void collectIncludedTemplates(JRTemplate template, List<JRStyle> externalStyles, 
			Set<String> loadedLocations, Set<String> templateParentLocations) throws JRException
	{
		JRTemplateReference[] includedTemplates = template.getIncludedTemplates();
		if (includedTemplates != null)
		{
			for (int i = 0; i < includedTemplates.length; i++)
			{
				JRTemplateReference reference = includedTemplates[i];
				String location = reference.getLocation();

				if (!templateParentLocations.add(location))
				{
					throw new JRRuntimeException("Circular dependency found for template at location " 
							+ location);
				}
				
				if (loadedLocations.add(location))
				{
					//template not yet loaded
					JRTemplate includedTemplate = JRFillReportTemplate.loadTemplate(
							location, this);
					collectStyles(includedTemplate, externalStyles, 
							loadedLocations, templateParentLocations);
					
				}
			}
		}
	}

	protected void lookupExternalDefaultStyle(Collection<JRStyle> styleList)
	{
		JRStyle defStyle = null;
		for (Iterator<JRStyle> it = styleList.iterator(); it.hasNext();)
		{
			JRStyle style = it.next();
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
		boundElements = new HashMap<JREvaluationTime, LinkedHashMap<PageKey, LinkedMap<Object, EvaluationBoundAction>>>();

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

		for (Iterator<JRBand> i = bands.iterator(); i.hasNext();)
		{
			JRFillBand band = (JRFillBand) i.next();
			createBoundElementMaps(JREvaluationTime.getBandEvaluationTime(band));
		}
	}


	private void createBoundElementMaps(JREvaluationTime evaluationTime)
	{
		LinkedHashMap<PageKey, LinkedMap<Object, EvaluationBoundAction>> boundElementsMap = 
				new LinkedHashMap<PageKey, LinkedMap<Object, EvaluationBoundAction>>();
		boundElements.put(evaluationTime, boundElementsMap);
	}


	private void killSubfillerThreads()
	{
		if (subfillers != null && !subfillers.isEmpty())
		{
			for (JRBaseFiller subfiller : subfillers.values())
			{
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
	protected void setParameters(Map<String,Object> parameterValues) throws JRException
	{
		initVirtualizationContext(parameterValues);

		setFormatFactory(parameterValues);

		setIgnorePagination(parameterValues);

		if (parentFiller == null)
		{
			ReportContext reportContext = (ReportContext) parameterValues.get(JRParameter.REPORT_CONTEXT);
			fillContext.setReportContext(reportContext);
		}

		mainDataset.setParameterValues(parameterValues);
		mainDataset.initDatasource();

		this.scriptlet = mainDataset.delegateScriptlet;

		if (!isSubreport())
		{
			fillContext.setMasterFormatFactory(getFormatFactory());
			fillContext.setMasterLocale(getLocale());
			fillContext.setMasterTimeZone(getTimeZone());
		}
	}

	protected void initVirtualizationContext(Map<String, Object> parameterValues)
	{
		if (isSubreport())
		{
			if (fillContext.isUsingVirtualizer())
			{
				// creating a subcontext for the subreport.
				// this allows setting a separate listener, and guarantees that
				// the current subreport page is not externalized.
				virtualizationContext = new JRVirtualizationContext(fillContext.getVirtualizationContext());//FIXME lucianc clear this context from the virtualizer
				
				// setting per subreport page size
				setVirtualPageSize(parameterValues);
				
				virtualizationListener = new ElementEvaluationVirtualizationListener(this);
				virtualizationContext.addListener(virtualizationListener);
			}
		}
		else
		{
			/* Virtualizer */
			JRVirtualizer virtualizer = (JRVirtualizer) parameterValues.get(JRParameter.REPORT_VIRTUALIZER);
			if (virtualizer == null)
			{
				return;
			}
			
			if (log.isDebugEnabled())
			{
				log.debug("Fill " + fillerId + ": using virtualizer " + virtualizer);
			}

			fillContext.setUsingVirtualizer(true);
			
			virtualizationContext = fillContext.getVirtualizationContext();
			virtualizationContext.setVirtualizer(virtualizer);
			
			setVirtualPageSize(parameterValues);
			
			virtualizationListener = new ElementEvaluationVirtualizationListener(this);
			virtualizationContext.addListener(virtualizationListener);
			
			JRVirtualizationContext.register(virtualizationContext, jasperPrint);
		}
	}
	
	protected void lockVirtualizationContext()
	{
		if (virtualizationContext != null)
		{
			virtualizationContext.lock();
		}
	}
	
	protected void unlockVirtualizationContext()
	{
		if (virtualizationContext != null)
		{
			virtualizationContext.unlock();
		}
	}

	protected void setVirtualPageSize(Map<String, Object> parameterValues)
	{
		// see if we have a parameter for the page size
		Integer virtualPageSize = (Integer) parameterValues.get(
				JRVirtualPrintPage.PROPERTY_VIRTUAL_PAGE_ELEMENT_SIZE);
		if (virtualPageSize == null)
		{
			// check if we have a property
			String pageSizeProp = jasperReport.getPropertiesMap().getProperty(
					JRVirtualPrintPage.PROPERTY_VIRTUAL_PAGE_ELEMENT_SIZE);
			if (pageSizeProp != null)
			{
				virtualPageSize = JRPropertiesUtil.asInteger(pageSizeProp);
			}
		}
		
		if (virtualPageSize != null)
		{
			if (log.isDebugEnabled())
			{
				log.debug("virtual page size " + virtualPageSize);
			}
			
			// override the default
			virtualizationContext.setPageElementSize(virtualPageSize);
		}
	}


	private void setFormatFactory(Map<String,Object> parameterValues)
	{
		formatFactory = (FormatFactory)parameterValues.get(JRParameter.REPORT_FORMAT_FACTORY);
		if (formatFactory == null)
		{
			formatFactory = DefaultFormatFactory.createFormatFactory(jasperReport.getFormatFactoryClass());
			parameterValues.put(JRParameter.REPORT_FORMAT_FACTORY, formatFactory);
		}
	}


	private void setIgnorePagination(Map<String,Object> parameterValues)
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
			
			if (isMasterReport())//subreport page height is already set by master
			{
				setPageHeight(PAGE_HEIGHT_PAGINATION_IGNORED);
			}
		}
	}


	/**
	 * Returns the report locale.
	 *
	 * @return the report locale
	 */
	protected Locale getLocale()
	{
		return mainDataset.getLocale();
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
	 * Returns the report resource bundle.
	 *
	 * @return the report resource bundle
	 */
	protected ResourceBundle getResourceBundle()
	{
		return mainDataset.resourceBundle;
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
	public Format getDateFormat(String pattern)
	{
		return getDateFormat(pattern, null);
	}
	
	protected Format getDateFormat(String pattern, TimeZone timeZone)
	{
		Locale lc = getLocale();
		TimeZone tz = timeZone == null ? getTimeZone() : timeZone;// default to filler timezone
		String key = pattern + "|" + JRDataUtils.getLocaleCode(lc) + "|" + JRDataUtils.getTimeZoneId(tz);
		Format format = dateFormatCache.get(key);
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
	public Format getNumberFormat(String pattern)
	{
		Locale lc = getLocale();
		String key = pattern + "|" + JRDataUtils.getLocaleCode(lc);
		Format format = numberFormatCache.get(key);
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

	protected void resolveBoundElements(JREvaluationTime evaluationTime, byte evaluation) throws JRException
	{
		LinkedHashMap<PageKey, LinkedMap<Object, EvaluationBoundAction>> pagesMap = boundElements.get(evaluationTime);
		
		boolean hasEntry;
		do
		{
			checkInterrupted();
			
			// locking once per page so that we don't hold the lock for too long
			// (that would prevent async exporters from getting page data during a long resolve)
			lockVirtualizationContext();
			try
			{
				synchronized (pagesMap)
				{
					// resolve a single page
					Iterator<Map.Entry<PageKey, LinkedMap<Object, EvaluationBoundAction>>> pagesIt = pagesMap.entrySet().iterator();
					hasEntry = pagesIt.hasNext();
					if (hasEntry)
					{
						Map.Entry<PageKey, LinkedMap<Object, EvaluationBoundAction>> pageEntry = pagesIt.next();
						int pageIdx = pageEntry.getKey().index;
						
						LinkedMap<Object, EvaluationBoundAction> boundElementsMap = pageEntry.getValue();
						// execute the actions
						while (!boundElementsMap.isEmpty())
						{
							EvaluationBoundAction action = boundElementsMap.pop();
							action.execute(evaluation, evaluationTime);
						}
						
						// remove the entry from the pages map
						pagesIt.remove();
						
						// call the listener to signal that the page has been modified
						if (fillListener != null)
						{
							fillListener.pageUpdated(jasperPrint, pageIdx);
						}
					}
				}
			}
			finally
			{
				unlockVirtualizationContext();
			}
		}
		while(hasEntry);
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

		if (fillContext.isUsingVirtualizer())
		{
			JRVirtualPrintPage virtualPage = new JRVirtualPrintPage(jasperPrint, virtualizationContext);
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
	 * Returns the value of a parameter.
	 * 
	 * @param parameterName the parameter name
	 * @return the parameter value
	 */
	public Object getParameterValue(String parameterName)
	{
		return mainDataset.getParameterValue(parameterName);
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
	protected void addVariableCalculationReq(String variableName, CalculationEnum calculation)
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

		fillContext.markCanceled();
		
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
			subfillers = new ConcurrentHashMap<Integer, JRBaseFiller>(16, 0.75f, 1);
		}

		subfillers.put(subfiller.fillerId, subfiller);
	}

	protected void unregisterSubfiller(JRBaseFiller subfiller)
	{
		if (subfillers != null)
		{
			subfillers.remove(subfiller.fillerId);
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
			
			// notify that the previous page was generated
			int pageCount = jasperPrint.getPages().size();
			if (pageCount > 0 && fillListener != null)
			{
				fillListener.pageGenerated(jasperPrint, pageCount - 1);
			}

			addLastPageBookmarks();

			jasperPrint.addPage(page);
			fillContext.setPrintPage(page);
		}
	}


	protected void addLastPageBookmarks()
	{
		if (bookmarkHelper != null)
		{
			int pageIndex = jasperPrint.getPages() == null ? -1 : (jasperPrint.getPages().size() - 1);
			if (pageIndex >= 0)
			{
				JRPrintPage page = jasperPrint.getPages().get(pageIndex);
				bookmarkHelper.addBookmarks(page, pageIndex);
			}
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
		return mainDataset.evaluateExpression(expression, evaluation);
	}

	protected JRFillExpressionEvaluator getExpressionEvaluator()
	{
		return calculator;
	}

	private void createDatasets() throws JRException
	{
		datasetMap = new HashMap<String,JRFillDataset>();

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
		for (Iterator<JRFillDataset> it = datasetMap.values().iterator(); it.hasNext();)
		{
			JRFillDataset dataset = it.next();
			dataset.inheritFromMain();
			dataset.initElementDatasets(factory);
		}
	}


	protected WhenResourceMissingTypeEnum getWhenResourceMissingType()
	{
		return mainDataset.whenResourceMissingType;
	}


	/**
	 * Returns the report.
	 *
	 * @return the report
	 */
	public JasperReport getJasperReport()
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

	/**
	 * Returns the top-level (master) filler object.
	 * 
	 * @return the master filler object
	 */
	public JRBaseFiller getMasterFiller()
	{
		JRBaseFiller filler = this;
		while (filler.parentFiller != null)
		{
			filler = filler.parentFiller;
		}
		return filler;
	}

	public JRFillDataset getMainDataset()
	{
		return mainDataset;
	}


	protected void addBoundElement(JRFillElement element, JRPrintElement printElement, 
			EvaluationTimeEnum evaluationType, String groupName, JRFillBand band)
	{
		JRFillGroup group = groupName == null ? null : getGroup(groupName);
		addBoundElement(element, printElement, evaluationType, group, band);
	}

	protected void addBoundElement(JRFillElement element, JRPrintElement printElement, EvaluationTimeEnum evaluationType, JRGroup group, JRFillBand band)
	{
		JREvaluationTime evaluationTime = JREvaluationTime.getEvaluationTime(evaluationType, group, band);
		addBoundElement(element, printElement, evaluationTime);
	}

	protected void addBoundElement(JRFillElement element, JRPrintElement printElement, JREvaluationTime evaluationTime)
	{
		if (log.isDebugEnabled())
		{
			log.debug("Adding evaluation of " + printElement + " by " + element 
					+ " for evaluation " + evaluationTime);
		}
		
		// get the pages map for the evaluation
		LinkedHashMap<PageKey, LinkedMap<Object, EvaluationBoundAction>> pagesMap = boundElements.get(evaluationTime);
		
		lockVirtualizationContext();
		try
		{
			synchronized (pagesMap)
			{
				// the key contains the page and its index; the index is only stored so that we have it in resolveBoundElements
				PageKey pageKey = new PageKey(printPage, jasperPrint.getPages().size() - 1);
				
				// get the actions map for the current page, creating if it does not yet exist
				LinkedMap<Object, EvaluationBoundAction> boundElementsMap = pagesMap.get(pageKey);
				if (boundElementsMap == null)
				{
					boundElementsMap = new LinkedMap<Object, EvaluationBoundAction>();
					pagesMap.put(pageKey, boundElementsMap);
				}
				
				// add the delayed element action to the map
				boundElementsMap.add(printElement, new ElementEvaluationAction(element, printElement));
			}
		}
		finally
		{
			unlockVirtualizationContext();
		}
	}

	protected void subreportPageFilled(JRPrintPage subreportPage)
	{
		PageKey subreportKey = new PageKey(subreportPage);
		PageKey parentKey = new PageKey(parentFiller.printPage);
		
		// move all delayed elements from the subreport page to the master page
		moveBoundActions(subreportKey, parentKey);
	}

	protected void moveBoundActions(PageKey subreportKey, PageKey parentKey)
	{
		for (LinkedHashMap<PageKey, LinkedMap<Object, EvaluationBoundAction>> map : boundElements.values())
		{
			lockVirtualizationContext();
			try
			{
				synchronized (map)
				{
					LinkedMap<Object, EvaluationBoundAction> subreportMap = map.remove(subreportKey);
					if (subreportMap != null && !subreportMap.isEmpty())
					{
						LinkedMap<Object, EvaluationBoundAction> masterMap = map.get(parentKey);
						if (masterMap == null)
						{
							masterMap = new LinkedMap<Object, EvaluationBoundAction>();
							map.put(parentKey, masterMap);
						}
						
						masterMap.addAll(subreportMap);
					}
				}
			}
			finally
			{
				unlockVirtualizationContext();
			}
		}
		
		if (subfillers != null)//recursive
		{
			for (JRBaseFiller subfiller : subfillers.values())
			{
				subfiller.moveBoundActions(subreportKey, parentKey);
			}
		}
	}

	protected boolean isPageFinal(int pageIdx)
	{
		JRPrintPage page = jasperPrint.getPages().get(pageIdx);
		return !hasBoundActions(page);
	}

	protected boolean hasBoundActions(JRPrintPage page)
	{
		for (LinkedHashMap<PageKey, LinkedMap<Object, EvaluationBoundAction>> map : boundElements.values())
		{
			lockVirtualizationContext();
			try
			{
				synchronized (map)
				{
					LinkedMap<Object, EvaluationBoundAction> boundMap = map.get(new PageKey(page));
					if (boundMap != null && !boundMap.isEmpty())
					{
						return true;
					}
				}
			}
			finally
			{
				unlockVirtualizationContext();
			}
		}
		
		if (subfillers != null)
		{
			for (JRBaseFiller subfiller : subfillers.values())
			{
				// recursive
				if (subfiller.hasBoundActions(page))
				{
					return true;
				}
			}
		}

		return false;
	}

	protected JRFillGroup getGroup(String groupName)
	{
		JRFillGroup group = null;
		if (groups != null)
		{
			for (int i = 0; i < groups.length; i++)
			{
				if (groups[i].getName().equals(groupName))
				{
					group = groups[i];
					break;
				}
			}
		}
		
		if (group == null)
		{
			throw new JRRuntimeException("No such group " + groupName);
		}
		return group;
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

	/**
	 *
	 */
	protected SavePoint advanceSavePoint(SavePoint savePoint, SavePoint newSavePoint)
	{
		if (savePoint == null)
		{
			savePoint = newSavePoint;
		}
		else if (newSavePoint != null)
		{
			// check to see if the new save point is on the same page/column as the previous one
			
			if (
				savePoint.page == newSavePoint.page
				&& savePoint.columnIndex == newSavePoint.columnIndex
				)
			{
				// if the new save point is on the same page/column, 
				// we just move the marker on the existing save point 
				savePoint.saveHeightOffset(newSavePoint.heightOffset);
			}
			else
			{
				// page/column break occurred, so the move operation 
				// must be performed on the previous save point
				savePoint.moveSavePointContent();
				savePoint = newSavePoint;
			}
		}
		
		return savePoint;
	}


	/**
	 *
	 */
	protected boolean moveKeepTogetherSavePointContent()
	{
		boolean moved = false;
		
		if (keepTogetherSavePoint != null)
		{
			if (keepTogetherSavePoint.page == getCurrentPage())
			{
				// it's a column break

				if (!keepTogetherSavePoint.isNewColumn)
				{
					keepTogetherSavePoint.addContent(
						printPage, 
						columnSpacing + columnWidth,
						offsetY - keepTogetherSavePoint.startOffsetY
						);

					offsetY = offsetY + keepTogetherSavePoint.endOffsetY - keepTogetherSavePoint.startOffsetY;
					
					moved = true;
				}
			}
			else
			{
				// it's a page break

				if (!keepTogetherSavePoint.isNewPage)
				{
					keepTogetherSavePoint.addContent(
							printPage, 
							(columnIndex - keepTogetherSavePoint.columnIndex) * (columnSpacing + columnWidth),
							offsetY - keepTogetherSavePoint.startOffsetY
							);

					offsetY = offsetY + keepTogetherSavePoint.endOffsetY - keepTogetherSavePoint.startOffsetY;

					moved = true;
				}
			}
			
			keepTogetherSavePoint = null;
		}
		
		return moved;
	}

	public JRFillContext getFillContext()
	{
		return fillContext;
	}
	
	protected int getFillerId()
	{
		return fillerId;
	}

	protected PrintElementOriginator assignElementId(JRFillElement fillElement)
	{
		int id = getFillContext().generateFillElementId();
		fillElements.put(id, fillElement);
		
		DefaultPrintElementOriginator originator = new DefaultPrintElementOriginator(id);
		return originator;
	}
}


class SavePoint
{
	protected JRPrintPage page;
	protected int columnIndex;
	protected boolean isNewPage;
	protected boolean isNewColumn;
	protected int startOffsetY;
	protected int endOffsetY;
	protected int startElementIndex;
	protected int endElementIndex;
	protected int heightOffset;
	protected int groupIndex;
	protected FooterPositionEnum footerPosition = FooterPositionEnum.NORMAL;
	protected List<JRPrintElement> elementsToMove = new ArrayList<JRPrintElement>();
	
	protected SavePoint(
		JRPrintPage page,
		int columnIndex,
		boolean isNewPage,
		boolean isNewColumn,
		int startOffsetY
		)
	{
		this.page = page;
		this.columnIndex = columnIndex;
		this.isNewPage = isNewPage;
		this.isNewColumn = isNewColumn;

		this.startElementIndex = page.getElements().size();
		this.endElementIndex = startElementIndex;
		
		this.startOffsetY = startOffsetY;
	}
	
	protected void saveHeightOffset(int heightOffset)
	{
		this.heightOffset = heightOffset;
		
		save();
	}
	
	protected void saveEndOffsetY(int endOffsetY)
	{
		this.endOffsetY = endOffsetY;
		
		save();
	}
	
	protected void save()
	{
		this.endElementIndex = page.getElements().size();
	}
	
	/**
	 *
	 */
	protected void removeContent()
	{
		for(int i = endElementIndex - 1; i >= startElementIndex; i--)
		{
			elementsToMove.add(page.getElements().remove(i));//FIXME this breaks delayed evaluations
		}
	}

	/**
	 *
	 */
	protected void addContent(JRPrintPage printPage, int xdelta, int ydelta)
	{
		for(int i = elementsToMove.size() - 1; i >= 0; i--)// elementsToMove were added in reverse order
		{
			JRPrintElement printElement = elementsToMove.get(i);

			printElement.setX(printElement.getX() + xdelta);
			printElement.setY(printElement.getY() + ydelta);

			printPage.addElement(printElement);
		}
	}

	/**
	 *
	 */
	protected void moveSavePointContent()
	{
		if (footerPosition != FooterPositionEnum.NORMAL)//FIXME is footerPosition testing required here?
		{
			//no page/column break occurred
			for(int i = startElementIndex; i < endElementIndex; i++)
			{
				JRPrintElement printElement = page.getElements().get(i);
				printElement.setY(printElement.getY() + heightOffset);
			}
		}
	}

}

class PageKey
{
	final JRPrintPage page;
	final int index;
	
	public PageKey(JRPrintPage page, int index)
	{
		this.page = page;
		this.index = index;
	}
	
	public PageKey(JRPrintPage page)
	{
		this(page, 0);
	}

	@Override
	public int hashCode()
	{
		return page.hashCode();
	}

	@Override
	public boolean equals(Object obj)
	{
		if (!(obj instanceof PageKey))
		{
			return false;
		}
		
		return page.equals(((PageKey) obj).page);
	}
}

/**
 * Generic delayed evaluation action.
 */
interface EvaluationBoundAction
{
	void execute(byte evaluation, JREvaluationTime evaluationTime) throws JRException;
}

/**
 * Delayed evaluation action that evaluates a print element.
 */
class ElementEvaluationAction implements EvaluationBoundAction
{
	private static final Log log = LogFactory.getLog(ElementEvaluationAction.class);
	
	protected final JRFillElement element;
	protected final JRPrintElement printElement;

	public ElementEvaluationAction(JRFillElement element, JRPrintElement printElement)
	{
		this.element = element;
		this.printElement = printElement;
	}
	
	public void execute(byte evaluation, JREvaluationTime evaluationTime) throws JRException
	{
		if (log.isDebugEnabled())
		{
			log.debug("resolving element " + printElement + " by " + element
					+ " on " + evaluationTime);
		}
		
		element.resolveElement(printElement, evaluation, evaluationTime);
	}

	@Override
	public String toString()
	{
		return "delayed evaluation {element: " + element
				+ ", printElement: " + printElement
				+ "}";
	}
}

/**
 * Virtualization listener that looks for elements with delayed evaluations 
 * and saves/restores the evaluations and externalization/internalization.
 */
class ElementEvaluationVirtualizationListener implements VirtualizationListener<VirtualElementsData>
{
	private static final Log log = LogFactory.getLog(ElementEvaluationAction.class);
	
	private final JRBaseFiller mainFiller;
	
	public ElementEvaluationVirtualizationListener(JRBaseFiller filler)
	{
		this.mainFiller = filler;
	}

	public void beforeExternalization(JRVirtualizable<VirtualElementsData> object)
	{
		JRVirtualizationContext virtualizationContext = object.getContext();
		virtualizationContext.lock();
		try
		{
			setElementEvaluationsToPage(mainFiller, object);
		}
		finally
		{
			virtualizationContext.unlock();
		}
	}

	protected void setElementEvaluationsToPage(final JRBaseFiller filler, final JRVirtualizable<VirtualElementsData> object)
	{
		if (log.isDebugEnabled())
		{
			log.debug("filler " + filler.fillerId + " setting element evaluation for elements in " + object.getUID());
		}
		
		JRVirtualPrintPage page = ((VirtualizablePageElements) object).getPage();// ugly but needed for now
		PageKey pageKey = new PageKey(page);
		VirtualElementsData virtualData = object.getVirtualData();
		
		for (Map.Entry<JREvaluationTime, LinkedHashMap<PageKey, LinkedMap<Object, EvaluationBoundAction>>> boundMapEntry : 
			filler.boundElements.entrySet())
		{
			final JREvaluationTime evaluationTime = boundMapEntry.getKey();
			LinkedHashMap<PageKey, LinkedMap<Object, EvaluationBoundAction>> map = boundMapEntry.getValue();
			
			synchronized (map)
			{
				final LinkedMap<Object, EvaluationBoundAction> actionsMap = map.get(pageKey);
				
				if (actionsMap != null && !actionsMap.isEmpty())
				{
					// collection delayed evaluations for elements that are about to be externalized.
					// the evaluations store the ID of the fill elements in order to serialize the data.
					final Map<JRPrintElement, Integer> elementEvaluations = new LinkedHashMap<JRPrintElement, Integer>();
					
					// FIXME optimize for pages with a single virtual block
					// create a deep element visitor
					PrintElementVisitor<Void> visitor = new UniformPrintElementVisitor<Void>(true)
					{
						@Override
						protected void visitElement(JRPrintElement element, Void arg)
						{
							// remove the action from the map because we're saving it as part of the page.
							// ugly cast but acceptable for now.
							ElementEvaluationAction action = (ElementEvaluationAction) actionsMap.remove(element);
							if (action != null)
							{
								elementEvaluations.put(element, action.element.printElementOriginator.getSourceElementId());
								
								if (log.isDebugEnabled())
								{
									log.debug("filler " + filler.fillerId + " saving evaluation " + evaluationTime + " of element " + element 
											+ " on object " + object);
								}
							}
						}
					};
					
					for (JRPrintElement element : virtualData.getElements())
					{
						element.accept(visitor, null);
					}
					
					if (!elementEvaluations.isEmpty())
					{
						// save the evaluations in the virtual data
						virtualData.setElementEvaluations(filler.fillerId, evaluationTime, elementEvaluations);
						
						// add an action for the page so that it gets devirtualized on resolveBoundElements
						actionsMap.add(null, new VirtualizedPageEvaluationAction(object));
					}
				}
			}
		}
		
		if (filler.subfillers != null)//recursive
		{
			for (JRBaseFiller subfiller : filler.subfillers.values())
			{
				setElementEvaluationsToPage(subfiller, object);
			}
		}
	}
	
	public void afterInternalization(JRVirtualizable<VirtualElementsData> object)
	{
		JRVirtualizationContext virtualizationContext = object.getContext();
		virtualizationContext.lock();
		try
		{
			getElementEvaluationsFromPage(mainFiller, object);
		}
		finally
		{
			virtualizationContext.unlock();
		}
	}

	protected void getElementEvaluationsFromPage(JRBaseFiller filler, JRVirtualizable<VirtualElementsData> object)
	{
		if (log.isDebugEnabled())
		{
			log.debug("filler " + filler.fillerId + " recreating element evaluation for elements in " + object.getUID());
		}
		
		JRVirtualPrintPage page = ((VirtualizablePageElements) object).getPage();// ugly but needed for now
		PageKey pageKey = new PageKey(page);
		VirtualElementsData elementsData = object.getVirtualData();
		
		for (Map.Entry<JREvaluationTime, LinkedHashMap<PageKey, LinkedMap<Object, EvaluationBoundAction>>> boundMapEntry : 
			filler.boundElements.entrySet())
		{
			JREvaluationTime evaluationTime = boundMapEntry.getKey();
			LinkedHashMap<PageKey, LinkedMap<Object, EvaluationBoundAction>> map = boundMapEntry.getValue();
			
			synchronized (map)
			{
				LinkedMap<Object, EvaluationBoundAction> actionsMap = map.get(pageKey);
				
				// get the delayed evaluations from the devirtualized data and add it back
				// to the filler delayed evaluation maps.
				Map<JRPrintElement, Integer> elementEvaluations = elementsData.getElementEvaluations(filler.fillerId, evaluationTime);
				if (elementEvaluations != null)
				{
					for (Map.Entry<JRPrintElement, Integer> entry : elementEvaluations.entrySet())
					{
						JRPrintElement element = entry.getKey();
						int fillElementId = entry.getValue();
						JRFillElement fillElement = filler.fillElements.get(fillElementId);
						
						if (log.isDebugEnabled())
						{
							log.debug("filler " + filler.fillerId + " got evaluation " + evaluationTime + " on " + element 
									+ " from object " + object + ", using " + fillElement);
						}
						
						if (fillElement == null)
						{
							throw new JRRuntimeException("Fill element with id " + fillElementId + " not found");
						}
						
						// add first so that it will be executed immediately
						actionsMap.addFirst(element, new ElementEvaluationAction(fillElement, element));
					}
				}
			}
		}
		
		if (filler.subfillers != null)//recursive
		{
			for (JRBaseFiller subfiller : filler.subfillers.values())
			{
				getElementEvaluationsFromPage(subfiller, object);
			}
		}
	}
}

/**
 * Delayed evaluation action that devirtualizes a set of elements in order to
 * evaluate one of several of them.
 */
class VirtualizedPageEvaluationAction implements EvaluationBoundAction
{
	private final JRVirtualizable<?> object;

	public VirtualizedPageEvaluationAction(JRVirtualizable<?> object)
	{
		this.object = object;
	}

	public void execute(byte evaluation, JREvaluationTime evaluationTime)
			throws JRException
	{
		// this forces devirtualization and queues the element evaluations via setElementEvaluationsToPage
		object.ensureVirtualData();
	}
}
