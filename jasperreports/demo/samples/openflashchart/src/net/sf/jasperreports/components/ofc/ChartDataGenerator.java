/*
 * JasperReports - Free Java Reporting Library.
 * Copyright (C) 2001 - 2009 Jaspersoft Corporation. All rights reserved.
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
package net.sf.jasperreports.components.ofc;

import java.util.Iterator;
import java.util.Set;

import net.sf.jasperreports.engine.JRRuntimeException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * @author Lucian Chirita (lucianc@users.sourceforge.net)
 * @version $Id: ChartDataGenerator.java 3031 2009-08-27 11:14:57Z teodord $
 */
public class ChartDataGenerator
{

	private static final String BGCOLOR = "ffffff";
	private static final String[] COLORS = {"fadf12", "ed262a", "006f3c", "fa6112", "409dcf", "e5018c", "eaab35"};
	
	protected static final ChartDataGenerator INSTANCE = new ChartDataGenerator();
	
	public static ChartDataGenerator instance()
	{
		return INSTANCE;
	}
	
	public String generatePieChart(String title, FillPieDataset dataset)
	{
		try
		{
			JSONObject chart = new JSONObject();
			chart.put("bg_colour", BGCOLOR);
			chart.put("title", new JSONObject().put("text", title));
			
			JSONObject pie = new JSONObject();
			pie.put("type", "pie");
			pie.put("animate", true);
			pie.put("tip", "#val# of #total#<br>#percent#");
			pie.put("start-angle", 35);
			pie.put("border", 2);
			pie.put("alpha", .6);
			
			JSONArray colors = new JSONArray();
			for (int i = 0; i < COLORS.length; i++)
			{
				colors.put(COLORS[i]);
			}
			pie.put("colours", colors);
			
			JSONArray pieValues = new JSONArray();
			Iterator labelIt = dataset.getKeys().iterator();
			Iterator valueIt = dataset.getValues().iterator();
			while (labelIt.hasNext() && valueIt.hasNext())
			{
				String label = (String) labelIt.next();
				Number value = (Number) valueIt.next();
				JSONObject pieValue = new JSONObject();
				pieValue.put("value", value);
				pieValue.put("label", label);
				pieValues.put(pieValue);
			}
			pie.put("values", pieValues);
			
			JSONArray elements = new JSONArray();
			elements.put(pie);
			chart.put("elements", elements);
			
			return chart.toString();
		}
		catch (JSONException e)
		{
			throw new JRRuntimeException(e);
		}
	}
	
	public String generateBarChart(String title, FillBarDataset dataset)
	{
		try
		{
			Set seriesKeys = dataset.getSeriesKeys();
			Set categories = dataset.getCategories();
			
			JSONObject chart = new JSONObject();
			chart.put("bg_colour", BGCOLOR);
			chart.put("title", new JSONObject().put("text", title));

			JSONObject xAxis = new JSONObject();
			JSONArray labels = new JSONArray(categories);
			xAxis.put("labels", new JSONObject().put("labels", labels));
			chart.put("x_axis", xAxis);
			
			JSONArray elements = new JSONArray();
			int seriesIdx = 0;
			double max = 0d;
			for (Iterator seriesIt = seriesKeys.iterator(); seriesIt.hasNext();)
			{
				String seriesKey = (String) seriesIt.next();
				
				JSONObject bar = new JSONObject();
				bar.put("type", "bar_glass");
				bar.put("colour", COLORS[seriesIdx % COLORS.length]);
				bar.put("alpha", .9);
				
				JSONArray values = new JSONArray();
				for (Iterator catIt = categories.iterator(); catIt.hasNext();)
				{
					String category = (String) catIt.next();
					Number value = dataset.getValue(seriesKey, category);
					if (value == null)
					{
						value = new Integer(0);
					}
					
					JSONObject valueObject = new JSONObject();
					valueObject.put("top", value);
					valueObject.put("tip", seriesKey + " #top#");
					values.put(valueObject);
					
					if (value.doubleValue() > max)
					{
						max = value.doubleValue();
					}
				}
				bar.put("values", values);
				
				elements.put(bar);
				++seriesIdx;
			}
			
			int yMax = (int) Math.ceil(max);
			int m = 1;
			while (yMax > 100)
			{
				yMax = (int) Math.ceil(yMax / 10d);
				m *= 10;
			}
			yMax = 5 * (int) Math.ceil(yMax / 5d);
			yMax *= m;
			int steps = yMax / 5;
			
			JSONObject yAxis = new JSONObject();
			yAxis.put("max", yMax);
			yAxis.put("steps", steps);
			chart.put("y_axis", yAxis);
			
			
			chart.put("elements", elements);
			
			return chart.toString();
		}
		catch (JSONException e)
		{
			throw new JRRuntimeException(e);
		}
	}
	
}
