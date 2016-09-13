/*
 * JasperReports - Free Java Reporting Library.
 * Copyright (C) 2001 - 2016 TIBCO Software Inc. All rights reserved.
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
package net.sf.jasperreports.chartcustomizers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import org.jfree.chart.JFreeChart;

import net.sf.jasperreports.engine.JRAbstractChartCustomizer;
import net.sf.jasperreports.engine.JRChart;
import net.sf.jasperreports.engine.JRChartCustomizer;
import net.sf.jasperreports.engine.JRPropertiesUtil;
import net.sf.jasperreports.engine.JRRuntimeException;
import net.sf.jasperreports.engine.fill.JRBaseFiller;
import net.sf.jasperreports.engine.fill.JRFillChart;
import net.sf.jasperreports.engine.util.JRClassLoader;

/**
 *
 * This class allows to chain one or more chart customizers. Customizers are
 * loaded from the classpath, are instantiated and executed. Customizers are
 * read from the element properties in the format of:
 * 
 * chartcustomizer.full-class-name
 * 
 * i.e.
 * 
 * chartcustomizer.customizer-instance-identifier.class=com.jaspersoft.
 * jasperreports.chartcustomizers.SplineCustomizer
 * 
 * Options for customizers will be available as:
 * 
 * chartcustomizer.full-class-name.*
 * 
 * @author gtoffoli
 */
public class ProxyChartCustomizer extends JRAbstractChartCustomizer {
	
	/**
	 * The prefix of a customizer property attribute
	 */
	public static final String CUSTOMIZER_ATTRIBUTE_PREFIX = "chartcustomizer";
	
	/**
	 * The attribute name of the class in a chart customizer property
	 */
	public static final String CUSTOMIZER_CLASS_ATTRIUBUTE = "class";
	
	/**
	 * The separator used in a chart customizer attribute name
	 */
	public static final String CUSTOMIZER_ATTRIBUTE_SEPARATOR = ".";

	private JRBaseFiller filler;
	
	private JRFillChart chart;

	/**
	 * We need to make a public constructor, since the JRAbstractChartCustomizer
	 * provides only a protected one.
	 */
	public ProxyChartCustomizer() {
		super();
	}

	@Override
	public void customize(JFreeChart jfc, JRChart jrc) {

		List<JRPropertiesUtil.PropertySuffix> properties = JRPropertiesUtil.getProperties(jrc.getPropertiesMap(), CUSTOMIZER_ATTRIBUTE_PREFIX + CUSTOMIZER_ATTRIBUTE_SEPARATOR);

		// We look for all the class properties...
		for (JRPropertiesUtil.PropertySuffix prop : properties) {
			//System.out.println("Property found: " + prop.getSuffix());
 
			//Escape the separator char
			Pattern escaper = Pattern.compile("([^a-zA-z0-9])");
			String splitPattern =  escaper.matcher(CUSTOMIZER_ATTRIBUTE_SEPARATOR).replaceAll("\\\\$1");
			String[] parts = prop.getSuffix().split(splitPattern);

			if (parts.length == 2 && parts[1].equals(CUSTOMIZER_CLASS_ATTRIUBUTE)) {

				// We have found a chart customizer
				String key = parts[0];
				String customizerClass = prop.getValue();

				//System.out.println("Customizer found: " + customizerClass);

				JRChartCustomizer chartCustomizer = null;

				if (customizerClass != null && customizerClass.length() > 0) {
					try {
						Class<?> myClass = JRClassLoader.loadClassForName(customizerClass);
						chartCustomizer = (JRChartCustomizer) myClass.newInstance();

						//System.out.println("Customizer instanced: " + chartCustomizer);
					} catch (Exception e) {
						throw new JRRuntimeException(
								net.sf.jasperreports.engine.fill.JRFillChart.EXCEPTION_MESSAGE_KEY_CUSTOMIZER_INSTANCE_ERROR,
								(Object[]) null, e);
					}

					if (chartCustomizer instanceof JRAbstractChartCustomizer) {
						((JRAbstractChartCustomizer) chartCustomizer).init(this.filler, this.chart);
					}

					// It this chartCustomizer is configurable, we pass in the
					// properties
					if (chartCustomizer instanceof ConfigurableChartCustomizer) {
						List<JRPropertiesUtil.PropertySuffix> chartProperties = JRPropertiesUtil.getProperties(jrc.getPropertiesMap(), getCustomizerPropertiesPrefix(key));

						Map<String, String> chartConfiguration = new HashMap<String, String>();

						for (JRPropertiesUtil.PropertySuffix chartProp : chartProperties) {
							chartConfiguration.put(chartProp.getSuffix(), chartProp.getValue());
						}

						((ConfigurableChartCustomizer) chartCustomizer).setConfiguration(chartConfiguration);
					}

					//System.out.println("Invoking customize... " + chartCustomizer.getClass().getName());

					chartCustomizer.customize(jfc, jrc);
				}
			}

		}

	}
	
	/**
	 * Return the prefix of a chart customizer property
	 */
	protected String getCustomizerPropertiesPrefix(String cutstomizerKey){
		return CUSTOMIZER_ATTRIBUTE_PREFIX + CUSTOMIZER_ATTRIBUTE_SEPARATOR + cutstomizerKey + CUSTOMIZER_ATTRIBUTE_SEPARATOR;
	}

	/**
	 * Initializes the chart customizer.
	 * 
	 * We store the filler and the chart parameters to propagate them to chart
	 * customizers used by the proxy if required, which is when they implements
	 * JRAbstractChartCustomizer
	 * 
	 * @param chartFiller
	 *            the filler instance
	 * @param chart
	 *            the fill chart object
	 */
	public void init(JRBaseFiller chartFiller, JRFillChart chart) {
		this.filler = chartFiller;
		this.chart = chart;
		super.init(chartFiller, chart);
	}

}
