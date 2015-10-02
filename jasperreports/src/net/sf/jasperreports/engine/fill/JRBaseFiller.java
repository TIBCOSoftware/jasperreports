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

/*
 * Contributors:
 * John Bindel - jbindel@users.sourceforge.net
 */

package net.sf.jasperreports.engine.fill;

import java.text.Format;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.TimeZone;
import java.util.concurrent.ConcurrentHashMap;

import net.sf.jasperreports.engine.JRBand;
import net.sf.jasperreports.engine.JRDefaultStyleProvider;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExpression;
import net.sf.jasperreports.engine.JRGroup;
import net.sf.jasperreports.engine.JROrigin;
import net.sf.jasperreports.engine.JRParameter;
import net.sf.jasperreports.engine.JRPrintElement;
import net.sf.jasperreports.engine.JRPrintPage;
import net.sf.jasperreports.engine.JRReportTemplate;
import net.sf.jasperreports.engine.JRRuntimeException;
import net.sf.jasperreports.engine.JRStyle;
import net.sf.jasperreports.engine.JRStyleSetter;
import net.sf.jasperreports.engine.JRTemplate;
import net.sf.jasperreports.engine.JRTemplateReference;
import net.sf.jasperreports.engine.JRVariable;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.JasperReportsContext;
import net.sf.jasperreports.engine.base.JRBasePrintPage;
import net.sf.jasperreports.engine.base.JRVirtualPrintPage;
import net.sf.jasperreports.engine.type.BandTypeEnum;
import net.sf.jasperreports.engine.type.EvaluationTimeEnum;
import net.sf.jasperreports.engine.type.FooterPositionEnum;
import net.sf.jasperreports.engine.type.OrientationEnum;
import net.sf.jasperreports.engine.type.PrintOrderEnum;
import net.sf.jasperreports.engine.type.RunDirectionEnum;
import net.sf.jasperreports.engine.type.SectionTypeEnum;
import net.sf.jasperreports.engine.type.WhenNoDataTypeEnum;
import net.sf.jasperreports.engine.type.WhenResourceMissingTypeEnum;
import net.sf.jasperreports.engine.util.JRDataUtils;
import net.sf.jasperreports.engine.util.JRStyledTextParser;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 */
public abstract class JRBaseFiller extends BaseReportFiller implements JRDefaultStyleProvider
{

	private static final Log log = LogFactory.getLog(JRBaseFiller.class);
	
	public static final String EXCEPTION_MESSAGE_KEY_INFINITE_LOOP_CREATING_NEW_PAGE = "fill.common.filler.infinite.loop.creating.new.page";
	public static final String EXCEPTION_MESSAGE_KEY_COLUMN_HEADER_OVERFLOW_INFINITE_LOOP = "fill.common.filler.column.header.overflow.infinite.loop";
	public static final String EXCEPTION_MESSAGE_KEY_CIRCULAR_DEPENDENCY_FOUND = "fill.base.filler.circular.dependency.found";
	public static final String EXCEPTION_MESSAGE_KEY_EXTERNAL_STYLE_NAME_NOT_SET = "fill.base.filler.external.style.name.not.set";
	public static final String EXCEPTION_MESSAGE_KEY_NO_SUCH_GROUP = "fill.base.filler.no.such.group";
	public static final String EXCEPTION_MESSAGE_KEY_PAGE_HEADER_OVERFLOW_INFINITE_LOOP = "fill.common.filler.page.header.overflow.infinite.loop";
	public static final String EXCEPTION_MESSAGE_KEY_UNSUPPORTED_REPORT_SECTION_TYPE = "fill.base.filler.unsupported.report.section.type";
	
	private static final int PAGE_HEIGHT_PAGINATION_IGNORED = 0x7d000000;//less than Integer.MAX_VALUE to avoid 

	protected BandReportFillerParent bandReportParent;

	private JRStyledTextParser styledTextParser = JRStyledTextParser.getInstance();

	/**
	 *
	 */
	protected String name;

	protected int columnCount;

	protected PrintOrderEnum printOrder;

	protected RunDirectionEnum columnDirection;

	protected int pageWidth;

	protected int pageHeight;

	protected OrientationEnum orientation;

	protected WhenNoDataTypeEnum whenNoDataType;

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
	protected WhenResourceMissingTypeEnum whenResourceMissingType;

	protected JRFillReportTemplate[] reportTemplates;
	
	protected List<JRTemplate> templates;

	protected JRStyle defaultStyle;

	protected JRStyle[] styles;

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

	protected JRPrintPage printPage;

	/**
	 * List of {@link JRFillBand JRFillBand} objects containing all bands of the
	 * report.
	 */
	protected List<JRBand> bands;

	/**
	 * Collection of subfillers
	 */
	protected Map<Integer, JRBaseFiller> subfillers;

	private boolean bandOverFlowAllowed;

	/**
	 *
	 */
	protected Map<String,Format> dateFormatCache = new HashMap<String,Format>();
	protected Map<String,Format> numberFormatCache = new HashMap<String,Format>();

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

	protected boolean isReorderBandElements;

	/**
	 *
	 */
	protected JRBaseFiller(
		JasperReportsContext jasperReportsContext, 
		JasperReport jasperReport, 
		BandReportFillerParent parent 
		) throws JRException
	{
		super(jasperReportsContext, jasperReport, parent);
		
		this.bandReportParent = parent;

		groups = mainDataset.groups;

		createReportTemplates(factory);

		String reportName = factory.getFiller().isSubreport() ? factory.getFiller().getJasperReport().getName() : null;
		
		background = createFillBand(jasperReport.getBackground(), reportName, BandTypeEnum.BACKGROUND);
		title = createFillBand(jasperReport.getTitle(), reportName, BandTypeEnum.TITLE);
		pageHeader = createFillBand(jasperReport.getPageHeader(), reportName, BandTypeEnum.PAGE_HEADER);
		columnHeader = createFillBand(jasperReport.getColumnHeader(), reportName, BandTypeEnum.COLUMN_HEADER);
		
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
		
		columnFooter = createFillBand(jasperReport.getColumnFooter(), reportName, BandTypeEnum.COLUMN_FOOTER);
		pageFooter = createFillBand(jasperReport.getPageFooter(), reportName, BandTypeEnum.PAGE_FOOTER);
		lastPageFooter = createFillBand(jasperReport.getLastPageFooter(), reportName, BandTypeEnum.LAST_PAGE_FOOTER);
		
		summary = createFillBand(jasperReport.getSummary(), reportName, BandTypeEnum.SUMMARY);
		if (summary != missingFillBand && summary.isEmpty())
		{
			summary = missingFillBand;
		}
		
		noData = createFillBand(jasperReport.getNoData(), reportName, BandTypeEnum.NO_DATA);

		initDatasets();
		initBands();
	}

	@Override
	protected void jasperReportSet()
	{
		SectionTypeEnum sectionType = jasperReport.getSectionType();
		if (sectionType != null && sectionType != SectionTypeEnum.BAND)
		{
			throw 
				new JRRuntimeException(
					EXCEPTION_MESSAGE_KEY_UNSUPPORTED_REPORT_SECTION_TYPE,  
					new Object[]{jasperReport.getSectionType()} 
					);
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
	}

	@Override
	protected JRFillObjectFactory initFillFactory()
	{
		JRFillObjectFactory fillFactory = new JRFillObjectFactory(this);
		
		// needed when creating group bands
		defaultStyleListeners = new ArrayList<DefaultStyleListener>();
		
		missingFillBand = new JRFillBand(this, null, fillFactory);
		missingFillSection = new JRFillSection(this, null, fillFactory);

		return fillFactory;
	}
	
	private JRFillBand createFillBand(JRBand reportBand, String reportName, BandTypeEnum bandType)
	{
		JRFillBand fillBand = factory.getBand(reportBand);
		if (fillBand != missingFillBand)
		{
			JROrigin origin = new JROrigin(reportName, bandType);
			fillBand.setOrigin(origin);
		}
		return fillBand;
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

	protected boolean isSubreportRunToBottom()
	{
		return bandReportParent != null && bandReportParent.isRunToBottom();
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
	protected abstract void setPageHeight(int pageHeight);


	@Override
	public JasperPrint fill(Map<String,Object> parameterValues) throws JRException
	{
		if (parameterValues == null)
		{
			parameterValues = new HashMap<String,Object>();
		}

		if (log.isDebugEnabled())
		{
			log.debug("Fill " + fillerId + ": filling report");
		}

		setParametersToContext(parameterValues);

		fillingThread = Thread.currentThread();
		
		JRResourcesFillUtil.ResourcesFillContext resourcesContext = 
			JRResourcesFillUtil.setResourcesFillContext(parameterValues);
		
		boolean success = false;
		try
		{
			createBoundElemementMaps();

			if (parent != null)
			{
				bandReportParent.registerSubfiller(this);
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
			
			if (success && parent == null)
			{
				// commit the cached data
				fillContext.cacheDone();
			}

			if (parent != null)
			{
				bandReportParent.unregisterSubfiller(this);
			}
			
			delayedActions.dispose();

			fillingThread = null;

			//kill the subreport filler threads
			killSubfillerThreads();
			
			if (parent == null)
			{
				fillContext.dispose();
			}

			JRResourcesFillUtil.revertResourcesFillContext(resourcesContext);
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

	private List<DefaultStyleListener> defaultStyleListeners;

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
					throw 
						new JRRuntimeException(
							EXCEPTION_MESSAGE_KEY_EXTERNAL_STYLE_NAME_NOT_SET,  
							(Object[])null 
							);
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
					throw 
						new JRRuntimeException(
							EXCEPTION_MESSAGE_KEY_CIRCULAR_DEPENDENCY_FOUND,  
							new Object[]{location} 
							);
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
		createBoundElementMaps(JREvaluationTime.EVALUATION_TIME_MASTER);
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

	@Override
	protected void ignorePaginationSet()
	{
		if (ignorePagination)
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
			
			if (isMasterReport() || !bandReportParent.isParentPagination())//subreport page height is already set by band master
			{
				setPageHeight(PAGE_HEIGHT_PAGINATION_IGNORED);
			}
		}
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
	 * Cancells the fill process.
	 *
	 * @throws JRException
	 */
	@Override
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


	/**
	 *
	 */
	protected void fillBand(JRPrintBand band)
	{
		if (isReorderBandElements())
		{
			List<JRPrintElement> elements = new ArrayList<JRPrintElement>();
			for(Iterator<JRPrintElement> it = band.iterateElements(); it.hasNext();)
			{
				JRPrintElement element = it.next();
				element.setX(element.getX() + offsetX);
				element.setY(element.getY() + offsetY);
				elements.add(element);
			}
			Collections.sort(elements, new JRYXComparator());//FIXME make singleton comparator; same for older comparator
			for (JRPrintElement element : elements)
			{
				printPage.addElement(element);
			}
		}
		else
		{
			for(Iterator<JRPrintElement> it = band.iterateElements(); it.hasNext();)
			{
				JRPrintElement element = it.next();
				element.setX(element.getX() + offsetX);
				element.setY(element.getY() + offsetY);
				printPage.addElement(element);
			}
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
	
	protected void addPageToParent(final boolean ended) throws JRException
	{
		if (printPage == null)
		{
			return;
		}
		
		FillerPageAddedEvent pageAdded = new FillerPageAddedEvent()
		{
			@Override
			public JasperPrint getJasperPrint()
			{
				return jasperPrint;
			}
			
			@Override
			public JRPrintPage getPage()
			{
				return printPage;
			}

			@Override
			public boolean hasReportEnded()
			{
				return ended;
			}

			@Override
			public int getPageStretchHeight()
			{
				return offsetY + bottomMargin;
			}

			@Override
			public int getPageIndex()
			{
				Number pageNumber = (Number) calculator.getPageNumber().getValue();
				if (pageNumber == null)//this happens when whenNoDataType="BlankPage" //FIXMEBOOK maybe we should set the variable?
				{
					return 0;
				}
				return pageNumber.intValue() - 1;
			}

			@Override
			public JRBaseFiller getFiller()
			{
				return JRBaseFiller.this;
			}

			@Override
			public DelayedFillActions getDelayedActions()
			{
				return delayedActions;
			}
		};
		
		//FIXMEBOOK use a fill listener instead of this?
		bandReportParent.addPage(pageAdded);
	}

	protected void setMasterPageVariables(int currentPageIndex, int totalPages)
	{
		JRFillVariable masterCurrentPage = getVariable(JRVariable.MASTER_CURRENT_PAGE);
		if (masterCurrentPage != null)
		{
			masterCurrentPage.setValue(currentPageIndex + 1);
		}
		
		JRFillVariable masterTotalPages = getVariable(JRVariable.MASTER_TOTAL_PAGES);
		if (masterTotalPages != null)
		{
			masterTotalPages.setValue(totalPages);
		}
	}

	protected WhenResourceMissingTypeEnum getWhenResourceMissingType()
	{
		return mainDataset.whenResourceMissingType;
	}


	protected boolean isBandOverFlowAllowed()
	{
		return bandOverFlowAllowed;
	}


	protected void setBandOverFlowAllowed(boolean splittableBand)
	{
		this.bandOverFlowAllowed = splittableBand;
	}


	protected boolean isReorderBandElements()
	{
		return isReorderBandElements;
	}


	protected void setReorderBandElements(boolean isReorderBandElements)
	{
		this.isReorderBandElements = isReorderBandElements;
	}


	protected int getMasterColumnCount()
	{
		FillerParent fillerParent = parent;
		int colCount = 1;

		while (fillerParent != null)
		{
			BaseReportFiller filler = fillerParent.getFiller();
			if (filler instanceof JRBaseFiller)//FIXMEBOOK
			{
				colCount *= ((JRBaseFiller) filler).columnCount;
			}
			fillerParent = filler.parent;
		}

		return colCount;
	}

	/**
	 * Returns the top-level (master) filler object.
	 * 
	 * @return the master filler object
	 */
	public BaseReportFiller getMasterFiller()
	{
		BaseReportFiller filler = this;
		while (filler.parent != null)
		{
			filler = filler.parent.getFiller();
		}
		return filler;
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
		// the key contains the page and its index; the index is only stored so that we have it in resolveBoundElements
		int pageIndex = ((Number) calculator.getPageNumber().getValue()).intValue() - 1;
		FillPageKey pageKey = new FillPageKey(printPage, pageIndex);
		
		addBoundElement(element, printElement, evaluationTime, pageKey);
	}

	protected void subreportPageFilled(JRPrintPage subreportPage)
	{
		FillPageKey subreportKey = new FillPageKey(subreportPage);
		
		// this method is only called when the parent is a band report
		JRBaseFiller parentFiller = (JRBaseFiller) parent.getFiller();
		//FIXMEBOOK the index is only correct when the parent is the master, see fillListener.pageUpdated
		int parentPageIndex = parentFiller.getJasperPrint().getPages().size() - 1;
		FillPageKey parentKey = new FillPageKey(parentFiller.printPage, parentPageIndex);
		
		// move all delayed elements from the subreport page to the master page
		moveBoundActions(subreportKey, parentKey);
		// move all master evaluations to the parent
		parent.getFiller().delayedActions.moveMasterEvaluations(delayedActions, parentKey);
	}

	protected void moveBoundActions(FillPageKey subreportKey, FillPageKey parentKey)
	{
		delayedActions.moveActions(subreportKey, parentKey);
		
		if (subfillers != null)//recursive
		{
			for (JRBaseFiller subfiller : subfillers.values())
			{
				subfiller.moveBoundActions(subreportKey, parentKey);
			}
		}
	}

	@Override
	public boolean isPageFinal(int pageIdx)
	{
		JRPrintPage page = jasperPrint.getPages().get(pageIdx);
		return !hasBoundActions(page);
	}

	public boolean isPageFinal(JRPrintPage page)
	{
		return !hasBoundActions(page);
	}
	
	protected boolean hasBoundActions(JRPrintPage page)
	{
		boolean hasActions = delayedActions.hasDelayedActions(page);
		if (hasActions)
		{
			return true;
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
			throw 
			new JRRuntimeException(
				EXCEPTION_MESSAGE_KEY_NO_SUCH_GROUP,  
				new Object[]{groupName} 
				);
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
	
	protected int getFillerId()
	{
		return fillerId;
	}

	protected PrintElementOriginator assignElementId(JRFillElement fillElement)
	{
		int id = getFillContext().generateFillElementId();
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
