/*
 * JasperReports - Free Java Reporting Library.
 * Copyright (C) 2001 - 2011 Jaspersoft Corporation. All rights reserved.
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

import java.io.File;
import java.io.InputStream;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.URL;
import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

import net.sf.jasperreports.engine.export.DefaultHyperlinkProducerFactory;
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
import net.sf.jasperreports.engine.util.DefaultFormatFactory;
import net.sf.jasperreports.engine.util.FormatFactory;
import net.sf.jasperreports.engine.util.JRClassLoader;
import net.sf.jasperreports.engine.util.JRDataUtils;
import net.sf.jasperreports.engine.util.JRFontUtil;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.engine.util.JRProperties;
import net.sf.jasperreports.engine.util.JRProperties.PropertySuffix;
import net.sf.jasperreports.engine.util.JRStyledText;
import net.sf.jasperreports.engine.util.JRStyledTextParser;
import net.sf.jasperreports.repo.RepositoryUtil;
import net.sf.jasperreports.repo.SimpleRepositoryContext;


/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id$
 */
public abstract class JRAbstractExporter implements JRExporter
{
	/**
	 * A property that gives the generic default filter factory class name.
	 * 
	 * @see #PROPERTY_SUFFIX_DEFAULT_FILTER_FACTORY
	 */
	public static final String PROPERTY_DEFAULT_FILTER_FACTORY = 
		JRProperties.PROPERTY_PREFIX + "export.default.filter.factory";

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
		public JRExporter getExporter()
		{
			return JRAbstractExporter.this;
		}

		public JasperPrint getExportedReport()
		{
			return jasperPrint;
		}

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
	
	protected static interface ParameterResolver
	{
		String getStringParameter(JRExporterParameter parameter, String property);
		
		String[] getStringArrayParameter(JRExporterParameter parameter, String propertyPrefix);

		String getStringParameterOrDefault(JRExporterParameter parameter, String property);
		
		boolean getBooleanParameter(JRExporterParameter parameter, String property, boolean defaultValue);
		
		int getIntegerParameter(JRExporterParameter parameter, String property, int defaultValue);

		float getFloatParameter(JRExporterParameter parameter, String property, float defaultValue);

		Character getCharacterParameter(JRExporterParameter parameter, String property);
	}
	
	protected class ParameterOverrideResolver implements ParameterResolver
	{
		
		public String getStringParameter(JRExporterParameter parameter, String property)
		{
			if (parameters.containsKey(parameter))
			{
				return (String)parameters.get(parameter);
			}
			else
			{
				return 
					JRProperties.getProperty(
						jasperPrint.getPropertiesMap(),
						property
						);
			}
		}

		public String[] getStringArrayParameter(JRExporterParameter parameter, String propertyPrefix)
		{
			String[] values = null; 
			if (parameters.containsKey(parameter))
			{
				values = (String[])parameters.get(parameter);
			}
			else
			{
				List<PropertySuffix> properties = JRProperties.getProperties(jasperPrint.getPropertiesMap(), propertyPrefix);
				if (properties != null)
				{
					values = new String[properties.size()];
					for(int i = 0; i < values.length; i++)
					{
						values[i] = properties.get(i).getValue();
					}
				}
			}
			return values;
		}

		public String getStringParameterOrDefault(JRExporterParameter parameter, String property)
		{
			if (parameters.containsKey(parameter))
			{
				String value = (String)parameters.get(parameter);
				if (value == null)
				{
					return JRProperties.getProperty(property);
				}
				else
				{
					return value;
				}
			}
			else
			{
				return
					JRProperties.getProperty(
						jasperPrint.getPropertiesMap(),
						property
						);
			}
		}

		public boolean getBooleanParameter(JRExporterParameter parameter, String property, boolean defaultValue)
		{
			if (parameters.containsKey(parameter))
			{
				Boolean booleanValue = (Boolean)parameters.get(parameter);
				if (booleanValue == null)
				{
					return JRProperties.getBooleanProperty(property);
				}
				else
				{
					return booleanValue.booleanValue();
				}
			}
			else
			{
				return 
					JRProperties.getBooleanProperty(
						jasperPrint.getPropertiesMap(),
						property,
						defaultValue
						);
			}
		}

		public int getIntegerParameter(JRExporterParameter parameter, String property, int defaultValue)
		{
			if (parameters.containsKey(parameter))
			{
				Integer integerValue = (Integer)parameters.get(parameter);
				if (integerValue == null)
				{
					return JRProperties.getIntegerProperty(property);
				}
				else
				{
					return integerValue.intValue();
				}
			}
			else
			{
				return 
					JRProperties.getIntegerProperty(
						jasperPrint.getPropertiesMap(),
						property,
						defaultValue
						);
			}
		}
		
		public float getFloatParameter(JRExporterParameter parameter, String property, float defaultValue)
		{
			if (parameters.containsKey(parameter))
			{
				Float floatValue = (Float)parameters.get(parameter);
				if (floatValue == null)
				{
					return JRProperties.getIntegerProperty(property);
				}
				else
				{
					return floatValue.intValue();
				}
			}
			else
			{
				return 
					JRProperties.getFloatProperty(
						jasperPrint.getPropertiesMap(),
						property,
						defaultValue
						);
			}
		}
		
		public Character getCharacterParameter(JRExporterParameter parameter, 
				String property)
		{
			if (parameters.containsKey(parameter))
			{
				return (Character) parameters.get(parameter);
			}
			else
			{
				return JRProperties.getCharacterProperty(
						jasperPrint.getPropertiesMap(), property);
			}
		}
	}
	
	protected class ParameterOverriddenResolver implements ParameterResolver
	{
		
		public String getStringParameter(JRExporterParameter parameter, String property)
		{
			String value;
			JRPropertiesMap hintsMap = jasperPrint.getPropertiesMap();
			if (hintsMap != null && hintsMap.containsProperty(property))
			{
				value = hintsMap.getProperty(property);
			}
			else
			{
				value = (String) parameters.get(parameter);
				
				if (value == null)
				{
					value = JRProperties.getProperty(property);
				}
			}
			return value;
		}

		public String[] getStringArrayParameter(JRExporterParameter parameter, String propertyPrefix)
		{
			String[] values = null;
			JRPropertiesMap hintsMap = jasperPrint.getPropertiesMap();
			if (hintsMap != null)
			{
				List<PropertySuffix> properties = JRProperties.getProperties(hintsMap, propertyPrefix);
				if (properties != null)
				{
					values = new String[properties.size()];
					for(int i = 0; i < values.length; i++)
					{
						values[i] = properties.get(i).getValue();
					}
				}
			}
			else
			{
				values = (String[])parameters.get(parameter);
			}
			return values;
		}

		public String getStringParameterOrDefault(JRExporterParameter parameter, String property)
		{
			String value;
			JRPropertiesMap hintsMap = jasperPrint.getPropertiesMap();
			if (hintsMap != null && hintsMap.containsProperty(property))
			{
				value = hintsMap.getProperty(property);
			}
			else
			{
				value = (String) parameters.get(parameter);
			}
			
			if (value == null)
			{
				value = JRProperties.getProperty(property);
			}
			
			return value;
		}

		public boolean getBooleanParameter(JRExporterParameter parameter, String property, boolean defaultValue)
		{
			boolean value;
			JRPropertiesMap hintsMap = jasperPrint.getPropertiesMap();
			if (hintsMap != null && hintsMap.containsProperty(property))
			{
				String prop = hintsMap.getProperty(property);
				if (prop == null)
				{
					value = JRProperties.getBooleanProperty(property);
				}
				else
				{
					value = JRProperties.asBoolean(prop);
				}
			}
			else
			{
				Boolean param = (Boolean) parameters.get(parameter);
				if (param == null)
				{
					value = JRProperties.getBooleanProperty(property);
				}
				else
				{
					value = param.booleanValue();
				}
			}
			return value;
		}

		public int getIntegerParameter(JRExporterParameter parameter, String property, int defaultValue)
		{
			int value;
			JRPropertiesMap hintsMap = jasperPrint.getPropertiesMap();
			if (hintsMap != null && hintsMap.containsProperty(property))
			{
				String prop = hintsMap.getProperty(property);
				if (prop == null)
				{
					value = JRProperties.getIntegerProperty(property);
				}
				else
				{
					value = JRProperties.asInteger(prop);
				}
			}
			else
			{
				Integer param = (Integer) parameters.get(parameter);
				if (param == null)
				{
					value = JRProperties.getIntegerProperty(property);
				}
				else
				{
					value = param.intValue();
				}
			}
			return value;
		}
		
		public float getFloatParameter(JRExporterParameter parameter, String property, float defaultValue)
		{
			float value;
			JRPropertiesMap hintsMap = jasperPrint.getPropertiesMap();
			if (hintsMap != null && hintsMap.containsProperty(property))
			{
				String prop = hintsMap.getProperty(property);
				if (prop == null)
				{
					value = JRProperties.getFloatProperty(property);
				}
				else
				{
					value = JRProperties.asFloat(prop);
				}
			}
			else
			{
				Float param = (Float) parameters.get(parameter);
				if (param == null)
				{
					value = JRProperties.getFloatProperty(property);
				}
				else
				{
					value = param.floatValue();
				}
			}
			return value;
		}
		
		public Character getCharacterParameter(JRExporterParameter parameter, String property)
		{
			Character value;
			JRPropertiesMap hintsMap = jasperPrint.getPropertiesMap();
			if (hintsMap != null && hintsMap.containsProperty(property))
			{
				String prop = hintsMap.getProperty(property);
				value = JRProperties.asCharacter(prop);
			}
			else
			{
				value = (Character) parameters.get(parameter);
				
				if (value == null)
				{
					value = JRProperties.getCharacterProperty(property);
				}
			}
			return value;
		}
		
	}
	
	// this would make the applet require logging library
	//private final static Log log = LogFactory.getLog(JRAbstractExporter.class);

	private ParameterResolver parameterResolver;
	
	/**
	 *
	 */
	protected Map<JRExporterParameter,Object> parameters = new HashMap<JRExporterParameter,Object>();

	/**
	 *
	 */
	protected List<JasperPrint> jasperPrintList;
	protected JasperPrint jasperPrint;
	protected boolean isModeBatch = true;
	protected int startPageIndex;
	protected int endPageIndex;
	protected int globalOffsetX;
	protected int globalOffsetY;
//	protected ClassLoader classLoader;
//	protected boolean classLoaderSet;
//	protected URLStreamHandlerFactory urlHandlerFactory;
//	protected boolean urlHandlerFactorySet;
//	protected FileResolver fileResolver;
//	protected boolean fileResolverSet;
	protected ExporterFilter filter;

	/**
	 *
	 */
	private LinkedList<int[]> elementOffsetStack = new LinkedList<int[]>();
	private int elementOffsetX = globalOffsetX;
	private int elementOffsetY = globalOffsetY;

	/**
	 *
	 */
	protected Map<String, DateFormat> dateFormatCache = new HashMap<String, DateFormat>();
	protected Map<String, NumberFormat> numberFormatCache = new HashMap<String, NumberFormat>();

	/**
	 *
	 */
	protected JRHyperlinkProducerFactory hyperlinkProducerFactory;
	
	/**
	 *
	 */
	private ReportContext reportContext;
	
	
	/**
	 *
	 */
	protected JRAbstractExporter()
	{
	}
	
	
	/**
	 *
	 */
	public void reset()
	{
		parameters = new HashMap<JRExporterParameter,Object>();
		elementOffsetStack = new LinkedList<int[]>();
	}
	
	
	/**
	 *
	 */
	public void setParameter(JRExporterParameter parameter, Object value)
	{
		parameters.put(parameter, value);
	}


	/**
	 *
	 */
	public Object getParameter(JRExporterParameter parameter)
	{
		return parameters.get(parameter);
	}


	/**
	 *
	 */
	public void setParameters(Map<JRExporterParameter,Object> parameters)
	{
		this.parameters = parameters;
	}
	

	/**
	 *
	 */
	public Map<JRExporterParameter,Object> getParameters()
	{
		return parameters;
	}
	
	protected ParameterResolver getParameterResolver()
	{
		if (parameterResolver == null)
		{
			boolean parametersOverrideHints;
			Boolean param = (Boolean) parameters.get(JRExporterParameter.PARAMETERS_OVERRIDE_REPORT_HINTS);
			if (param == null)
			{
				parametersOverrideHints = JRProperties.getBooleanProperty(JRExporterParameter.PROPERTY_EXPORT_PARAMETERS_OVERRIDE_REPORT_HINTS);
			}
			else
			{
				parametersOverrideHints = param.booleanValue();
			}
			
			if (parametersOverrideHints)
			{
				parameterResolver = new ParameterOverrideResolver();
			}
			else
			{
				parameterResolver = new ParameterOverriddenResolver();
			}
		}
		
		return parameterResolver;
	}

	/**
	 *
	 */
	public String getStringParameter(JRExporterParameter parameter, String property)
	{
		return getParameterResolver().getStringParameter(parameter, property);
	}

	
	/**
	 *
	 */
	public String[] getStringArrayParameter(JRExporterParameter parameter, String property)
	{
		return getParameterResolver().getStringArrayParameter(parameter, property);
	}

	
	/**
	 *
	 */
	public String getStringParameterOrDefault(JRExporterParameter parameter, String property)
	{
		return getParameterResolver().getStringParameterOrDefault(parameter, property);
	}

	
	/**
	 *
	 */
	public boolean getBooleanParameter(JRExporterParameter parameter, String property, boolean defaultValue)
	{
		return getParameterResolver().getBooleanParameter(parameter, property, defaultValue);
	}

	
	/**
	 *
	 */
	public int getIntegerParameter(JRExporterParameter parameter, String property, int defaultValue)
	{
		return getParameterResolver().getIntegerParameter(parameter, property, defaultValue);
	}

	
	/**
	 *
	 */
	public float getFloatParameter(JRExporterParameter parameter, String property, float defaultValue)
	{
		return getParameterResolver().getFloatParameter(parameter, property, defaultValue);
	}

	
	/**
	 *
	 */
	public Character getCharacterParameter(JRExporterParameter parameter, String property)
	{
		return getParameterResolver().getCharacterParameter(parameter, property);
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
		Integer offsetX = (Integer)parameters.get(JRExporterParameter.OFFSET_X);
		if (offsetX != null)
		{
			globalOffsetX = offsetX.intValue();
		}
		else
		{
			globalOffsetX = 0;
		}

		Integer offsetY = (Integer)parameters.get(JRExporterParameter.OFFSET_Y);
		if (offsetY != null)
		{
			globalOffsetY = offsetY.intValue();
		}
		else
		{
			globalOffsetY = 0;
		}
		
		if (setElementOffsets)
		{
			elementOffsetX = globalOffsetX;
			elementOffsetY = globalOffsetY;
		}
	}
	

	/**
	 *
	 */
	protected void setExportContext()
	{
//		classLoaderSet = false;
//		urlHandlerFactorySet = false;
//		fileResolverSet = false;
//		
//		classLoader = (ClassLoader)parameters.get(JRExporterParameter.CLASS_LOADER);
//		if (classLoader != null)
//		{
//			JRResourcesUtil.setThreadClassLoader(classLoader);
//			classLoaderSet = true;
//		}
//
//		urlHandlerFactory = (URLStreamHandlerFactory) parameters.get(JRExporterParameter.URL_HANDLER_FACTORY);
//		if (urlHandlerFactory != null)
//		{
//			JRResourcesUtil.setThreadURLHandlerFactory(urlHandlerFactory);
//			urlHandlerFactorySet = true;
//		}
//
//		fileResolver = (FileResolver) parameters.get(JRExporterParameter.FILE_RESOLVER);
//		if (fileResolver != null)
//		{
//			JRResourcesUtil.setThreadFileResolver(fileResolver);
//			fileResolverSet = true;
//		}

		Map<String, Object> contextParamValues = new HashMap<String, Object>(3);
		contextParamValues.put(JRParameter.REPORT_CLASS_LOADER, parameters.get(JRExporterParameter.CLASS_LOADER));
		contextParamValues.put(JRParameter.REPORT_URL_HANDLER_FACTORY, parameters.get(JRExporterParameter.URL_HANDLER_FACTORY));
		contextParamValues.put(JRParameter.REPORT_FILE_RESOLVER, parameters.get(JRExporterParameter.FILE_RESOLVER));

		RepositoryUtil.setRepositoryContext(new SimpleRepositoryContext(contextParamValues));
		
		JRFontUtil.resetThreadMissingFontsCache();
	}
		

	/**
	 *
	 */
	protected void resetExportContext()
	{
//		if (classLoaderSet)
//		{
//			JRResourcesUtil.resetClassLoader();
//		}
//		
//		if (urlHandlerFactorySet)
//		{
//			JRResourcesUtil.resetThreadURLHandlerFactory();
//		}
//		
//		if (fileResolverSet)
//		{
//			JRResourcesUtil.resetThreadFileResolver();
//		}

		RepositoryUtil.revertRepositoryContext();
	}

	
	/**
	 * @deprecated replaced by {@link #setExportContext() setExportContext} 
	 */
	protected void setClassLoader()
	{
		setExportContext();
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
	protected void setJasperPrint(JasperPrint jasperPrint)
	{
		this.jasperPrint = jasperPrint;

		String localeCode = jasperPrint.getLocaleCode();
		JRStyledTextParser.setLocale(localeCode == null ? null : JRDataUtils.getLocale(localeCode));
	}
	
	
	/**
	 *
	 */
	protected void setInput() throws JRException
	{
		jasperPrintList = (List<JasperPrint>)parameters.get(JRExporterParameter.JASPER_PRINT_LIST);
		if (jasperPrintList == null)
		{
			isModeBatch = false;
			
			jasperPrint = (JasperPrint)parameters.get(JRExporterParameter.JASPER_PRINT);
			if (jasperPrint == null)
			{
				InputStream is = (InputStream)parameters.get(JRExporterParameter.INPUT_STREAM);
				if (is != null)
				{
					jasperPrint = (JasperPrint)JRLoader.loadObject(is);
				}
				else
				{
					URL url = (URL)parameters.get(JRExporterParameter.INPUT_URL);
					if (url != null)
					{
						jasperPrint = (JasperPrint)JRLoader.loadObject(url);
					}
					else
					{
						File file = (File)parameters.get(JRExporterParameter.INPUT_FILE);
						if (file != null)
						{
							jasperPrint = (JasperPrint)JRLoader.loadObject(file);
						}
						else
						{
							String fileName = (String)parameters.get(JRExporterParameter.INPUT_FILE_NAME);
							if (fileName != null)
							{
								jasperPrint = (JasperPrint)JRLoader.loadObjectFromFile(fileName);
							}
							else
							{
								throw new JRException("No input source supplied to the exporter.");
							}
						}
					}
				}
			}
			
			jasperPrintList = new ArrayList<JasperPrint>();
			jasperPrintList.add(jasperPrint);
		}
		else
		{
			isModeBatch = true;

			if (jasperPrintList.size() == 0)
			{
				throw new JRException("Empty input source supplied to the exporter in batch mode.");
			}

			jasperPrint = jasperPrintList.get(0);
		}

		setJasperPrint(jasperPrint);

		filter = (ExporterFilter)parameters.get(JRExporterParameter.FILTER);
	}
	

	/**
	 *
	 */
	protected void setPageRange() throws JRException
	{
		int lastPageIndex = -1;
		if (jasperPrint.getPages() != null)
		{
			lastPageIndex = jasperPrint.getPages().size() - 1;
		}

		Integer start = (Integer)parameters.get(JRExporterParameter.START_PAGE_INDEX);
		if (start == null)
		{
			startPageIndex = 0;
		}
		else
		{
			startPageIndex = start.intValue();
			if (startPageIndex < 0 || startPageIndex > lastPageIndex)
			{
				throw new JRException("Start page index out of range : " + startPageIndex + " of " + lastPageIndex);
			}
		}

		Integer end = (Integer)parameters.get(JRExporterParameter.END_PAGE_INDEX);
		if (end == null)
		{
			endPageIndex = lastPageIndex;
		}
		else
		{
			endPageIndex = end.intValue();
			if (endPageIndex < startPageIndex || endPageIndex > lastPageIndex)
			{
				throw new JRException("End page index out of range : " + endPageIndex + " (" + startPageIndex + " : " + lastPageIndex + ")");
			}
		}

		Integer index = (Integer)parameters.get(JRExporterParameter.PAGE_INDEX);
		if (index != null)
		{
			int pageIndex = index.intValue();
			if (pageIndex < 0 || pageIndex > lastPageIndex)
			{
				throw new JRException("Page index out of range : " + pageIndex + " of " + lastPageIndex);
			}
			startPageIndex = pageIndex;
			endPageIndex = pageIndex;
		}
	}
	

	/**
	 *
	 */
	protected JRStyledText getStyledText(JRPrintText textElement, boolean setBackcolor)
	{
		return textElement.getStyledText(
				setBackcolor ? JRStyledTextAttributeSelector.ALL : JRStyledTextAttributeSelector.NO_BACKCOLOR);
	}

	
	protected JRStyledText getStyledText(JRPrintText textElement)
	{
		return getStyledText(textElement, true);
	}

	
	/**
	 *
	 */
	protected void setOutput()
	{
	}


	/**
	 * Returns the X axis offset used for element export.
	 * <p>
	 * This method should be used istead of {@link #globalOffsetX globalOffsetX} when
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
	 * so that the previous offsets are resored.
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
		return localeCode == null ? null : JRDataUtils.getLocale(localeCode);
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
		if (text.getValueClassName() == null)
		{
			textValue = getTextValueString(text, textStr);
		}
		else
		{
			try
			{
				Class<?> valueClass = JRClassLoader.loadClassForRealName(text.getValueClassName());
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
//		TextValue textValue;
//		String pattern = text.getPattern();
//		if (pattern == null || pattern.trim().length() == 0)
//		{
//			textValue = getTextValueString(text, textStr);
//		}
//		else
//		{
//			DateFormat dateFormat = getDateFormat(getTextFormatFactoryClass(text), pattern, getTextLocale(text), getTextTimeZone(text));
//			
//			Date value = null;
//			if (textStr != null && textStr.length() > 0)
//			{
//				value = dateFormat.parse(textStr);
//			}
//			textValue = new DateTextValue(textStr, value, getPattern(text));
//		}
//		return textValue;
	}

	protected TextValue getNumberCellValue(JRPrintText text, String textStr) throws ParseException, ClassNotFoundException
	{
		return new NumberTextValue(textStr, (Number)text.getValue(), text.getPattern());
//		TextValue textValue;
//		String pattern = text.getPattern();
//		if (pattern == null || pattern.trim().length() == 0)
//		{
//			if (textStr != null && textStr.length() > 0)
//			{
//				Number value = defaultParseNumber(textStr, JRClassLoader.loadClassForRealName(text.getValueClassName()));
//
//				if (value != null)
//				{
//					textValue = new NumberTextValue(textStr, value, getPattern(text));
//				}
//				else
//				{
//					textValue = getTextValueString(text, textStr);
//				}
//			}
//			else
//			{
//				textValue = new NumberTextValue(textStr, null, getPattern(text));
//			}
//		}
//		else
//		{
//			NumberFormat numberFormat = getNumberFormat(getTextFormatFactoryClass(text), pattern, getTextLocale(text));
//			
//			Number value = null;
//			if (textStr != null && textStr.length() > 0)
//			{
//				value = numberFormat.parse(textStr);
//			}
//			textValue = new NumberTextValue(textStr, value, getPattern(text));
//		}
//		return textValue;
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
	
	protected ExporterFilter createFilter(final String exportPropertyPrefix) 
			throws JRException
	{
		String exportDefaultFactoryProperty = exportPropertyPrefix 
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
			defaultFilterClassName = JRProperties.getProperty(exportDefaultFactoryProperty);
		}
		
		//and finally the global generic property
		if (defaultFilterClassName == null)
		{
			defaultFilterClassName = JRProperties.getProperty(PROPERTY_DEFAULT_FILTER_FACTORY);
		}
		
		ExporterFilterFactory defaultFactory = ExporterFilterFactoryUtil.getFilterFactory(defaultFilterClassName);
		
		JRExporterContext context = new BaseExporterContext()
		{
			public String getExportPropertiesPrefix()
			{
				return exportPropertyPrefix;
			}
		};
		return defaultFactory.getFilter(context);
	}

	protected void setHyperlinkProducerFactory()
	{
		hyperlinkProducerFactory = (JRHyperlinkProducerFactory) parameters.get(JRExporterParameter.HYPERLINK_PRODUCER_FACTORY);
		if (hyperlinkProducerFactory == null)
		{
			hyperlinkProducerFactory = new DefaultHyperlinkProducerFactory();//FIXME use singleton cache? for target producer too;
		}
	}
	
	protected JRHyperlinkProducer getHyperlinkProducer(JRPrintHyperlink link)
	{
		return hyperlinkProducerFactory == null ? null : hyperlinkProducerFactory.getHandler(link.getLinkType());
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
	protected abstract String getExporterKey() throws JRException;


}
