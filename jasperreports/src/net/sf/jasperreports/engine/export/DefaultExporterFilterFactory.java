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
package net.sf.jasperreports.engine.export;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.sf.jasperreports.engine.DefaultJasperReportsContext;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRPropertiesUtil;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReportsContext;

/**
 * Default exporter filter factory.
 * 
 * The factory searches for all registered filter factories and collects all
 * filters produced by these factories for a specific exporter context.
 * 
 * @author Lucian Chirita (lucianc@users.sourceforge.net)
 * @see #getFilter(JRExporterContext)
 */
public class DefaultExporterFilterFactory implements ExporterFilterFactory
{

	/**
	 * The prefix of properties that are used to register filter factories.
	 */
	public static final String PROPERTY_EXPORTER_FILTER_FACTORY_PREFIX = 
		JRPropertiesUtil.PROPERTY_PREFIX + "export.filter.factory.";
	
	/**
	 * The method searches for all filter factories registered via
	 * {@link #PROPERTY_EXPORTER_FILTER_FACTORY_PREFIX net.sf.jasperreports.export.filter.factory.*} properties,
	 * calls each factory and collects the returned filters.
	 * 
	 * The method returns:
	 * <ul>
	 * 	<li><code>null</code>, if no factory returned a not null filter.</li>
	 * 	<li>an individual filter, if a single factory returned a not null filter.</li>
	 * 	<li>a {@link ExporterFilterContainer} instance, if several factories returned not null filters.</li>
	 * </ul>
	 * 
	 * @see #PROPERTY_EXPORTER_FILTER_FACTORY_PREFIX
	 */
	public ExporterFilter getFilter(JRExporterContext exporterContext) throws JRException
	{
		List<ExporterFilterFactory> factories = getAllFilterFactories(exporterContext.getJasperReportsContext(), exporterContext.getExportedReport());
		List<ExporterFilter> filters = new ArrayList<ExporterFilter>(factories.size());
		for (Iterator<ExporterFilterFactory> it = factories.iterator(); it.hasNext();)
		{
			ExporterFilterFactory factory = it.next();
			ExporterFilter filter = factory.getFilter(exporterContext);
			if (filter != null)
			{
				filters.add(filter);
			}
		}
		
		ExporterFilter filter;
		if (filters.isEmpty())
		{
			filter = null;
		}
		else if (filters.size() == 1)
		{
			filter = filters.get(0);
		}
		else
		{
			filter = new ExporterFilterContainer(filters);
		}
		return filter;
	}

	protected List<ExporterFilterFactory> getAllFilterFactories(JasperReportsContext jasperReportsContext, JasperPrint report) throws JRException
	{
		List<JRPropertiesUtil.PropertySuffix> factoryProps = JRPropertiesUtil.getInstance(jasperReportsContext).getAllProperties(report, 
				PROPERTY_EXPORTER_FILTER_FACTORY_PREFIX);
		List<ExporterFilterFactory> factories = new ArrayList<ExporterFilterFactory>(factoryProps.size());
		for (Iterator<JRPropertiesUtil.PropertySuffix> it = factoryProps.iterator(); it.hasNext();)
		{
			JRPropertiesUtil.PropertySuffix prop = it.next();
			ExporterFilterFactory factory = getFilterFactory(prop.getValue());
			factories.add(factory);
		}
		return factories;
	}
	
	/**
	 * @deprecated Replaced by {@link #getAllFilterFactories(JasperReportsContext, JasperPrint)}.
	 */
	protected List<ExporterFilterFactory> getAllFilterFactories(JasperPrint report) throws JRException
	{
		return getAllFilterFactories(DefaultJasperReportsContext.getInstance(), report);
	}
	
	protected ExporterFilterFactory getFilterFactory(String factoryClassName) throws JRException
	{
		return ExporterFilterFactoryUtil.getFilterFactory(factoryClassName);
	}
	
}
