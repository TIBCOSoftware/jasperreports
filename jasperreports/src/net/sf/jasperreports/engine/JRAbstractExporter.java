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
package net.sf.jasperreports.engine;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.URLStreamHandlerFactory;
import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

import net.sf.jasperreports.engine.export.ExporterFilter;
import net.sf.jasperreports.engine.export.ExporterFilterFactory;
import net.sf.jasperreports.engine.export.ExporterFilterFactoryUtil;
import net.sf.jasperreports.engine.export.JRExporterContext;
import net.sf.jasperreports.engine.export.JRHyperlinkProducer;
import net.sf.jasperreports.engine.export.JRHyperlinkProducerFactory;
import net.sf.jasperreports.engine.export.data.BooleanTextValue;
import net.sf.jasperreports.engine.export.data.DateTextValue;
import net.sf.jasperreports.engine.export.data.NumberTextValue;
import net.sf.jasperreports.engine.export.data.StringTextValue;
import net.sf.jasperreports.engine.export.data.TextValue;
import net.sf.jasperreports.engine.fonts.FontUtil;
import net.sf.jasperreports.engine.util.DefaultFormatFactory;
import net.sf.jasperreports.engine.util.FileResolver;
import net.sf.jasperreports.engine.util.FormatFactory;
import net.sf.jasperreports.engine.util.JRClassLoader;
import net.sf.jasperreports.engine.util.JRDataUtils;
import net.sf.jasperreports.engine.util.JRStyledText;
import net.sf.jasperreports.engine.util.JRStyledTextParser;
import net.sf.jasperreports.engine.util.JRStyledTextUtil;
import net.sf.jasperreports.engine.util.LocalJasperReportsContext;
import net.sf.jasperreports.engine.util.Pair;
import net.sf.jasperreports.export.CompositeExporterConfigurationFactory;
import net.sf.jasperreports.export.Exporter;
import net.sf.jasperreports.export.ExporterConfiguration;
import net.sf.jasperreports.export.ExporterInput;
import net.sf.jasperreports.export.ExporterInputItem;
import net.sf.jasperreports.export.ExporterOutput;
import net.sf.jasperreports.export.PropertiesExporterConfigurationFactory;
import net.sf.jasperreports.export.SimpleExporterInputItem;


/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id$
 */
public abstract class JRAbstractExporter<C extends ExporterConfiguration, O extends ExporterOutput, E extends JRExporterContext> implements JRExporter<ExporterInput, C, O>
{
	/**
	 * A property that gives the generic default filter factory class name.
	 * 
	 * @see #PROPERTY_SUFFIX_DEFAULT_FILTER_FACTORY
	 */
	public static final String PROPERTY_DEFAULT_FILTER_FACTORY = 
		JRPropertiesUtil.PROPERTY_PREFIX + "export.default.filter.factory";

	/**
	 * The suffix applied to properties that give the default filter factory for
	 * a specific exporter.
	 * 
	 * For instance, the default filter factory for XLS exporters is
	 * <code>net.sf.jasperreports.export.xls.default.filter.factory</code>.
	 * 
	 * If this property is not defined for a specific exporter, the generic
	 * exporter factory given by {@link #PROPERTY_DEFAULT_FILTER_FACTORY} is used.
	 */
	public static final String PROPERTY_SUFFIX_DEFAULT_FILTER_FACTORY = "default.filter.factory";
	
	public abstract class BaseExporterContext implements JRExporterContext
	{
		/**
		 * @deprecated Replaced by {@link #getExporterRef()}.
		 */
		public JRExporter getExporter()
		{
			return JRAbstractExporter.this;
		}

		public Exporter getExporterRef()
		{
			return JRAbstractExporter.this;
		}

		public JasperReportsContext getJasperReportsContext()
		{
			return jasperReportsContext;
		}
		
		public JasperPrint getExportedReport()
		{
			return jasperPrint;
		}

		@SuppressWarnings("deprecation")
		public Map<JRExporterParameter,Object> getExportParameters()
		{
			return parameters;
		}

		public int getOffsetX()
		{
			return JRAbstractExporter.this.getOffsetX();
		}

		public int getOffsetY()
		{
			return JRAbstractExporter.this.getOffsetY();
		}
	}
	
	// this would make the applet require logging library
	//private final static Log log = LogFactory.getLog(JRAbstractExporter.class);

	/**
	 *
	 */
	protected JasperReportsContext jasperReportsContext;
	protected JRPropertiesUtil propertiesUtil;
	protected JRStyledTextAttributeSelector allSelector;
	protected JRStyledTextAttributeSelector noBackcolorSelector;
	protected JRStyledTextAttributeSelector noneSelector;
	protected JRStyledTextUtil styledTextUtil;
	
	/**
	 *
	 */
	@SuppressWarnings("deprecation")
	protected Map<JRExporterParameter,Object> parameters = new HashMap<JRExporterParameter,Object>();

	/**
	 *
	 */
	protected ExporterInput exporterInput;
	protected C exporterConfiguration;
	protected O exporterOutput;

	protected ExporterInputItem crtItem;
	protected C crtItemConfiguration;
	protected C crtConfiguration;
	protected JasperPrint jasperPrint;

	/**
	 *
	 */
	private LinkedList<int[]> elementOffsetStack = new LinkedList<int[]>();
	private int elementOffsetX;
	private int elementOffsetY;

	/**
	 *
	 */
	protected Map<String, DateFormat> dateFormatCache = new HashMap<String, DateFormat>();
	protected Map<String, NumberFormat> numberFormatCache = new HashMap<String, NumberFormat>();

	/*
	 * cached text locale, JRDataUtils.getLocale(String) is rather slow.
	 * helps in cases where there's a single report locale, which are most likely 9x% of all cases.
	 * note that we're assuming single threaded exporting, otherwise we would need volatile.
	 */
	private Pair<String, Locale> lastTextLocale;
	
	/*
	 * cache of text value class to avoid calling JRClassLoader.loadClassForRealName() each time.
	 * note that we're assuming single threaded exporting.
	 */
	private Map<String, Class<?>> textValueClasses = new HashMap<String, Class<?>>();
	
	/**
	 *
	 */
	private ReportContext reportContext;
	
	
	/**
	 * @deprecated Replaced by {@link #JRAbstractExporter(JasperReportsContext)}.
	 */
	protected JRAbstractExporter()
	{
		this(DefaultJasperReportsContext.getInstance());
	}
	
	
	/**
	 *
	 */
	protected JRAbstractExporter(JasperReportsContext jasperReportsContext)
	{
		setJasperReportsContext(jasperReportsContext);
	}
	
	
	/**
	 *
	 */
	public void reset()
	{
		parameters = new HashMap<JRExporterParameter,Object>();
		elementOffsetStack = new LinkedList<int[]>();
		exporterInput = null;
		exporterOutput = null;
		exporterConfiguration = null;
	}
	
	
	/**
	 * @deprecated Replaced by  {@link #setExporterInput(net.sf.jasperreports.export.ExporterInput)}, 
	 * {@link #setConfiguration(ExporterConfiguration)} and {@link #setExporterOutput(net.sf.jasperreports.export.ExporterOutput)}
	 */
	public void setParameter(JRExporterParameter parameter, Object value)
	{
		parameters.put(parameter, value);
		exporterInput = null;
		exporterOutput = null;
		exporterConfiguration = null;
	}


	/**
	 * @deprecated Replaced by {@link #getExporterInput()}, {@link #getConfiguration()} and {@link #getExporterOutput()}.
	 */
	public Object getParameter(JRExporterParameter parameter)
	{
		return parameters.get(parameter);
	}


	/**
	 * @deprecated Replaced by  {@link #setExporterInput(net.sf.jasperreports.export.ExporterInput)}, 
	 * {@link #setConfiguration(ExporterConfiguration)} and {@link #setExporterOutput(net.sf.jasperreports.export.ExporterOutput)}
	 */
	public void setParameters(Map<JRExporterParameter,Object> parameters)
	{
		this.parameters = parameters;
		exporterInput = null;
		exporterOutput = null;
		exporterConfiguration = null;
	}
	

	/**
	 * @deprecated Replaced by {@link #getExporterInput()}, {@link #getConfiguration()} and {@link #getExporterOutput()}.
	 */
	public Map<JRExporterParameter,Object> getParameters()
	{
		return parameters;
	}

	/**
	 *
	 */
	protected ExporterInput getExporterInput()
	{
		return exporterInput;
	}

	
	/**
	 *
	 */
	public void setExporterInput(ExporterInput exporterInput)
	{
		this.exporterInput = exporterInput;
	}

	
	/**
	 *
	 */
	protected O getExporterOutput()
	{
		return exporterOutput;
	}

	
	/**
	 *
	 */
	public void setExporterOutput(O exporterOutput)
	{
		this.exporterOutput = exporterOutput;
	}

	
	/**
	 *
	 */
	public void setConfiguration(C configuration)
	{
		this.exporterConfiguration = configuration;
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
		this.allSelector = JRStyledTextAttributeSelector.getAllSelector(jasperReportsContext);
		this.noBackcolorSelector = JRStyledTextAttributeSelector.getNoBackcolorSelector(jasperReportsContext);
		this.noneSelector = JRStyledTextAttributeSelector.getNoneSelector(jasperReportsContext);
		this.styledTextUtil = JRStyledTextUtil.getInstance(jasperReportsContext);
	}

	
	/**
	 *
	 */
	public void setReportContext(ReportContext reportContext)
	{
		this.reportContext = reportContext;
	}

	
	/**
	 *
	 */
	public ReportContext getReportContext()
	{
		return reportContext;
	}

	
	/**
	 *
	 */
	public JRPropertiesUtil getPropertiesUtil()
	{
		return propertiesUtil;
	}

	
	/**
	 *
	 */
	public abstract void exportReport() throws JRException;


	protected void setOffset()
	{
		setOffset(true);
	}
	
	/**
	 *
	 */
	protected void setOffset(boolean setElementOffsets)
	{
		if (setElementOffsets)
		{
			ExporterConfiguration configuration = getCurrentConfiguration();
			Integer offsetX = configuration.getOffsetX();
			if (offsetX != null)
			{
				elementOffsetX = offsetX.intValue();
			}
			else
			{
				elementOffsetX = 0;
			}

			Integer offsetY = configuration.getOffsetY();
			if (offsetY != null)
			{
				elementOffsetY = offsetY.intValue();
			}
			else
			{
				elementOffsetY = 0;
			}
		}
	}
	

	/**
	 *
	 */
	@SuppressWarnings("deprecation")
	protected void ensureJasperReportsContext()
	{
		if (
			parameters.containsKey(JRExporterParameter.CLASS_LOADER)
			|| parameters.containsKey(JRExporterParameter.URL_HANDLER_FACTORY)
			|| parameters.containsKey(JRExporterParameter.FILE_RESOLVER)
			)
		{
			LocalJasperReportsContext localJasperReportsContext = new LocalJasperReportsContext(jasperReportsContext);

			if (parameters.containsKey(JRExporterParameter.CLASS_LOADER))
			{
				localJasperReportsContext.setClassLoader((ClassLoader)parameters.get(JRExporterParameter.CLASS_LOADER));
			}

			if (parameters.containsKey(JRExporterParameter.URL_HANDLER_FACTORY))
			{
				localJasperReportsContext.setURLStreamHandlerFactory((URLStreamHandlerFactory)parameters.get(JRExporterParameter.URL_HANDLER_FACTORY));
			}

			if (parameters.containsKey(JRExporterParameter.FILE_RESOLVER))
			{
				localJasperReportsContext.setFileResolver((FileResolver)parameters.get(JRExporterParameter.FILE_RESOLVER));
			}
			
			setJasperReportsContext(localJasperReportsContext);
		}
		
		FontUtil.getInstance(jasperReportsContext).resetThreadMissingFontsCache();
	}
		

	/**
	 *
	 */
	protected void resetExportContext()
	{
	}

	
	/**
	 * @deprecated replaced by {@link #ensureJasperReportsContext() setExportContext} 
	 */
	protected void setClassLoader()
	{
		ensureJasperReportsContext();
	}

	
	/**
	 * @deprecated replaced by {@link #resetExportContext() resetExportContext} 
	 */
	protected void resetClassLoader()
	{
		resetExportContext();
	}


	/**
	 *
	 */
	protected void setCurrentExporterInputItem(ExporterInputItem crtItem)
	{
		this.crtItem = crtItem;

		jasperPrint = crtItem.getJasperPrint();
		String localeCode = jasperPrint.getLocaleCode();
		JRStyledTextParser.setLocale(localeCode == null ? null : JRDataUtils.getLocale(localeCode));

		crtItemConfiguration = (C)crtItem.getConfiguration();
		
		if (crtItemConfiguration == null)
		{
			@SuppressWarnings("deprecation")
			C depConf = 
				new net.sf.jasperreports.export.parameters.ParametersExporterConfigurationFactory<C>(
					getJasperReportsContext(),
					getParameters(),
					getCurrentJasperPrint()
					).getConfiguration(
						getConfigurationInterface()
						);
			crtItemConfiguration = depConf; 
		}
		else
		{
			PropertiesExporterConfigurationFactory<C> factory = new PropertiesExporterConfigurationFactory<C>(jasperReportsContext);
			crtItemConfiguration = factory.getConfiguration(crtItemConfiguration, jasperPrint);
		}
		
		crtConfiguration = null;
	}


	/**
	 *
	 */
	protected C getCurrentConfiguration()
	{
		if (crtConfiguration == null)//FIXMEEXPORT if you want to give up lazy loading, solve the setOffset order problem
		{
			CompositeExporterConfigurationFactory<C> factory = new CompositeExporterConfigurationFactory<C>(jasperReportsContext);
			crtConfiguration = factory.getConfiguration(exporterConfiguration, crtItemConfiguration);
		}
		return crtConfiguration;
	}
	
	
	/**
	 * @deprecated Replaced by {@link #setCurrentExporterInputItem(ExporterInputItem)}.
	 */
	protected void setJasperPrint(JasperPrint jasperPrint)
	{
		setCurrentExporterInputItem(new SimpleExporterInputItem(jasperPrint));
	}
	

	/**
	 *
	 */
	protected abstract Class<C> getConfigurationInterface();

	
	/**
	 *
	 */
	@SuppressWarnings("deprecation")
	protected void ensureInput()
	{
		if (exporterInput == null)
		{
			exporterInput = new net.sf.jasperreports.export.parameters.ParametersExporterInput(parameters);
		}
		
		setCurrentExporterInputItem(exporterInput.getItems().get(0));//FIXMEEXPORT check order of operations for first item
	}

	
	/**
	 *
	 */
	protected abstract void ensureOutput();
	

	/**
	 *
	 */
	protected void initExport()
	{
		setOffset();
		
		createFilter();
	}
	

	/**
	 *
	 */
	protected PageRange getPageRange()
	{
		Integer startPageIndex = null;
		Integer endPageIndex = null;
		
		int lastPageIndex = -1;
		if (jasperPrint.getPages() != null)
		{
			lastPageIndex = jasperPrint.getPages().size() - 1;
		}

		ExporterConfiguration configuration = getCurrentConfiguration();
		
		Integer start = configuration.getStartPageIndex();
		if (start != null)
		{
			startPageIndex = start;
			if (startPageIndex < 0 || startPageIndex > lastPageIndex)
			{
				throw new JRRuntimeException("Start page index out of range : " + startPageIndex + " of " + lastPageIndex);
			}
		}

		Integer end = configuration.getEndPageIndex();
		if (end != null)
		{
			endPageIndex = end;
			if (endPageIndex < startPageIndex || endPageIndex > lastPageIndex)
			{
				throw new JRRuntimeException("End page index out of range : " + endPageIndex + " (" + startPageIndex + " : " + lastPageIndex + ")");
			}
		}

		Integer pageIndex = configuration.getPageIndex();
		if (pageIndex != null)
		{
			if (pageIndex < 0 || pageIndex > lastPageIndex)
			{
				throw new JRRuntimeException("Page index out of range : " + pageIndex + " of " + lastPageIndex);
			}
			startPageIndex = pageIndex;
			endPageIndex = pageIndex;
		}
		
		PageRange pageRange = null;
		
		if (startPageIndex != null || endPageIndex != null)
		{
			pageRange = new PageRange(startPageIndex, endPageIndex);
		}
		
		return pageRange;
	}
	

	/**
	 *
	 */
	protected JRStyledText getStyledText(JRPrintText textElement, boolean setBackcolor)
	{
		return styledTextUtil.getStyledText(textElement, setBackcolor ? allSelector : noBackcolorSelector);
	}

	
	protected JRStyledText getStyledText(JRPrintText textElement)
	{
		return getStyledText(textElement, true);
	}


	/**
	 * Returns the X axis offset used for element export.
	 * <p>
	 * This method should be used instead of {@link #globalOffsetX globalOffsetX} when
	 * exporting elements.
	 * 
	 * @return the X axis offset
	 */
	protected int getOffsetX()
	{
		return elementOffsetX;
	}


	/**
	 * Returns the Y axis offset used for element export.
	 * <p>
	 * This method should be used istead of {@link #globalOffsetY globalOffsetY} when
	 * exporting elements.
	 * 
	 * @return the Y axis offset
	 */
	protected int getOffsetY()
	{
		return elementOffsetY;
	}

	
	/**
	 * Sets the offsets for exporting elements from a {@link JRPrintFrame frame}.
	 * <p>
	 * After the frame elements are exported, a call to {@link #restoreElementOffsets() popElementOffsets} is required
	 * so that the previous offsets are restored.
	 * 
	 * @param frame
	 * @param relative
	 * @see #getOffsetX()
	 * @see #getOffsetY()
	 * @see #restoreElementOffsets()
	 */
	protected void setFrameElementsOffset(JRPrintFrame frame, boolean relative)
	{	
		if (relative)
		{
			setElementOffsets(0, 0);
		}
		else
		{
			int topPadding = frame.getLineBox().getTopPadding().intValue();
			int leftPadding = frame.getLineBox().getLeftPadding().intValue();

			setElementOffsets(getOffsetX() + frame.getX() + leftPadding, getOffsetY() + frame.getY() + topPadding);
		}
	}
	
	
	private void setElementOffsets(int offsetX, int offsetY)
	{
		elementOffsetStack.addLast(new int[]{elementOffsetX, elementOffsetY});
		
		elementOffsetX = offsetX;
		elementOffsetY = offsetY;
	}

	
	/**
	 * Restores offsets after a call to 
	 * {@link #setFrameElementsOffset(JRPrintFrame, boolean) setFrameElementsOffset}.
	 */
	protected void restoreElementOffsets()
	{
		int[] offsets = elementOffsetStack.removeLast();
		elementOffsetX = offsets[0];
		elementOffsetY = offsets[1];
	}

	
	protected String getTextFormatFactoryClass(JRPrintText text)
	{
		String formatFactoryClass = text.getFormatFactoryClass();
		if (formatFactoryClass == null)
		{
			formatFactoryClass = jasperPrint.getFormatFactoryClass();
		}
		return formatFactoryClass;
	}

	protected Locale getLocale()
	{
		String localeCode = jasperPrint.getLocaleCode();
		return localeCode == null ? null : JRDataUtils.getLocale(localeCode);
	}

	protected Locale getTextLocale(JRPrintText text)
	{
		String localeCode = text.getLocaleCode();
		if (localeCode == null)
		{
			localeCode = jasperPrint.getLocaleCode();
		}
		
		if (localeCode == null)
		{
			return null;
		}
		
		Pair<String, Locale> last = lastTextLocale;
		if (last != null && last.first().equals(localeCode))
		{
			return last.second();
		}
		
		Locale locale = JRDataUtils.getLocale(localeCode);
		lastTextLocale = new Pair<String, Locale>(localeCode, locale);
		return locale;
	}

	protected TimeZone getTextTimeZone(JRPrintText text)
	{
		String tzId = text.getTimeZoneId();
		if (tzId == null)
		{
			tzId = jasperPrint.getTimeZoneId();
		}
		return tzId == null ? null : JRDataUtils.getTimeZone(tzId);
	}
	
	protected TextValue getTextValue(JRPrintText text, String textStr)
	{
		TextValue textValue;
		String valueClassName = text.getValueClassName();
		if (valueClassName == null)
		{
			textValue = getTextValueString(text, textStr);
		}
		else
		{
			try
			{
				Class<?> valueClass = textValueClasses.get(valueClassName);
				if (valueClass == null)
				{
					valueClass = JRClassLoader.loadClassForRealName(valueClassName);
					textValueClasses.put(valueClassName, valueClass);
				}
				
				if (java.lang.Number.class.isAssignableFrom(valueClass))
				{
					textValue = getNumberCellValue(text, textStr);
				}
				else if (Date.class.isAssignableFrom(valueClass))
				{
					textValue = getDateCellValue(text, textStr);
				}
				else if (Boolean.class.equals(valueClass))
				{
					textValue = getBooleanCellValue(text, textStr);
				}
				else
				{
					textValue = getTextValueString(text, textStr);
				} 
			}
			catch (ParseException e)
			{
				//log.warn("Error parsing text value", e);
				textValue = getTextValueString(text, textStr);
			}
			catch (ClassNotFoundException e)
			{
				//log.warn("Error loading text value class", e);
				textValue = getTextValueString(text, textStr);
			}			
		}
		return textValue;
	}

	protected TextValue getTextValueString(JRPrintText text, String textStr)
	{
		return new StringTextValue(textStr);
	}

	protected TextValue getDateCellValue(JRPrintText text, String textStr) throws ParseException
	{
		return new DateTextValue(textStr, (Date)text.getValue(), text.getPattern());
	}

	protected TextValue getNumberCellValue(JRPrintText text, String textStr) throws ParseException, ClassNotFoundException
	{
		return new NumberTextValue(textStr, (Number)text.getValue(), text.getPattern());
	}

	protected Number defaultParseNumber(String textStr, Class<?> valueClass)
	{
		Number value = null;
		try
		{
			if (valueClass.equals(Byte.class))
			{
				value = Byte.valueOf(textStr);
			}
			else if (valueClass.equals(Short.class))
			{
				value = Short.valueOf(textStr);
			}
			else if (valueClass.equals(Integer.class))
			{
				value = Integer.valueOf(textStr);
			}
			else if (valueClass.equals(Long.class))
			{
				value = Long.valueOf(textStr);
			}
			else if (valueClass.equals(Float.class))
			{
				value = Float.valueOf(textStr);
			}
			else if (valueClass.equals(Double.class))
			{
				value = Double.valueOf(textStr);
			}
			else if (valueClass.equals(BigInteger.class))
			{
				value = new BigInteger(textStr);
			}
			else if (valueClass.equals(BigDecimal.class))
			{
				value = new BigDecimal(textStr);
			}
		}
		catch (NumberFormatException e)
		{
			//skip
		}
		return value;
	}
	
	protected TextValue getBooleanCellValue(JRPrintText text, String textStr)
	{
		Boolean value = null;
		if (textStr != null && textStr.length() > 0)
		{
			value = Boolean.valueOf(textStr);
		}
		return new BooleanTextValue(textStr, value);
	}

	protected DateFormat getDateFormat(String formatFactoryClass, String pattern, Locale lc, TimeZone tz)
	{
		String key = formatFactoryClass 
			+ "|" + pattern 
			+ "|" + (lc == null ? "" : JRDataUtils.getLocaleCode(lc)) 
			+ "|" + (tz == null ? "" : JRDataUtils.getTimeZoneId(tz));
		DateFormat dateFormat = dateFormatCache.get(key);
		if (dateFormat == null)
		{
			FormatFactory formatFactory = DefaultFormatFactory.createFormatFactory(formatFactoryClass);//FIXMEFORMAT cache this too
			dateFormat = formatFactory.createDateFormat(pattern, lc, tz);
			dateFormatCache.put(key, dateFormat);
		}
		return dateFormat;
	}

	protected NumberFormat getNumberFormat(String formatFactoryClass, String pattern, Locale lc)
	{
		String key = formatFactoryClass 
			+ "|" + pattern 
			+ "|" + (lc == null ? "" : JRDataUtils.getLocaleCode(lc)); 
		NumberFormat numberFormat = numberFormatCache.get(key);
		if (numberFormat == null)
		{
			FormatFactory formatFactory = DefaultFormatFactory.createFormatFactory(formatFactoryClass);//FIXMEFORMAT cache this too
			numberFormat = formatFactory.createNumberFormat(pattern, lc);
			numberFormatCache.put(key, numberFormat);
		}
		return numberFormat;
	}
	
	/**
	 * 
	 */
	protected ExporterFilter createFilter()
	{
		E exporterContext = getExporterContext();
		
		String exportDefaultFactoryProperty = exporterContext.getExportPropertiesPrefix() 
				+ PROPERTY_SUFFIX_DEFAULT_FILTER_FACTORY;
		
		//the default filter class is determined from 4 possible sources
		String defaultFilterClassName = null;
		
		if (jasperPrint.hasProperties())
		{
			//try first the exporter specific property from the report
			defaultFilterClassName = jasperPrint.getPropertiesMap().getProperty(
					exportDefaultFactoryProperty);
			
			//then the generic property from the report
			if (defaultFilterClassName == null)
			{
				defaultFilterClassName = jasperPrint.getPropertiesMap().getProperty(
						PROPERTY_DEFAULT_FILTER_FACTORY);
			}
		}
		
		//then the global exporter specific property
		if (defaultFilterClassName == null)
		{
			defaultFilterClassName = getPropertiesUtil().getProperty(exportDefaultFactoryProperty);
		}
		
		//and finally the global generic property
		if (defaultFilterClassName == null)
		{
			defaultFilterClassName = getPropertiesUtil().getProperty(PROPERTY_DEFAULT_FILTER_FACTORY);
		}
		
		ExporterFilter filter = null;
		
		try
		{
			ExporterFilterFactory defaultFactory = ExporterFilterFactoryUtil.getFilterFactory(defaultFilterClassName);
			filter = defaultFactory.getFilter(exporterContext);
		}
		catch (JRException e)
		{
			throw new JRRuntimeException(e);
		}

		return filter;
	}

	public JRHyperlinkProducer getHyperlinkProducer(JRPrintHyperlink link)
	{
		JRHyperlinkProducerFactory factory = getCurrentConfiguration().getHyperlinkProducerFactory();
		return factory == null ? null : factory.getHandler(link.getLinkType());
	}

	/**
	 * @deprecated Replaced by {@link #getHyperlinkProducer(JRPrintHyperlink)}.
	 */
	protected JRHyperlinkProducer getCustomHandler(JRPrintHyperlink link)
	{
		return getHyperlinkProducer(link);
	}

	/**
	 * 
	 */
	public abstract String getExporterKey();

	/**
	 * 
	 */
	public abstract E getExporterContext();

	public JasperPrint getCurrentJasperPrint()
	{
		return jasperPrint;
	}

	protected class PageRange
	{
		private Integer startPageIndex;
		private Integer endPageIndex;
		
		/**
		 * 
		 */
		public PageRange(Integer startPageIndex, Integer endPageIndex)
		{
			this.startPageIndex = startPageIndex;
			this.endPageIndex = endPageIndex;
		}
		
		/**
		 * 
		 */
		public Integer getStartPageIndex()
		{
			return startPageIndex;
		}

		/**
		 * 
		 */
		public Integer getEndPageIndex()
		{
			return endPageIndex;
		}
	}
}
