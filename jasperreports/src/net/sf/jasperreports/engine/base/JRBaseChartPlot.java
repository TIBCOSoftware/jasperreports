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
package net.sf.jasperreports.engine.base;

import java.awt.Color;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.util.Collection;
import java.util.Iterator;
import java.util.SortedSet;
import java.util.TreeSet;

import net.sf.jasperreports.charts.JRCategoryAxisFormat;
import net.sf.jasperreports.charts.type.PlotOrientationEnum;
import net.sf.jasperreports.engine.JRChart;
import net.sf.jasperreports.engine.JRChartPlot;
import net.sf.jasperreports.engine.JRConstants;
import net.sf.jasperreports.engine.JRRuntimeException;
import net.sf.jasperreports.engine.design.events.JRChangeEventsSupport;
import net.sf.jasperreports.engine.design.events.JRPropertyChangeSupport;
import net.sf.jasperreports.engine.util.JRCloneUtils;
import net.sf.jasperreports.engine.util.JRStyleResolver;

import org.jfree.chart.plot.PlotOrientation;


/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 */
public abstract class JRBaseChartPlot implements JRChartPlot, Serializable, JRChangeEventsSupport
{


	/**
	 *
	 */
	private static final long serialVersionUID = JRConstants.SERIAL_VERSION_UID;
	
	public static final String PROPERTY_BACKCOLOR = "backcolor";
	
	public static final String PROPERTY_BACKGROUND_ALPHA = "backgroundAlpha";
	
	public static final String PROPERTY_FOREGROUND_ALPHA = "foregroundAlpha";
	
	public static final String PROPERTY_LABEL_ROTATION = "labelRotation";
	
	public static final String PROPERTY_ORIENTATION = "orientation";
	
	public static final String PROPERTY_SERIES_COLORS = "seriesColors";
	
	protected JRChart chart;
	protected Color backcolor;
	protected PlotOrientationEnum orientationValue = PlotOrientationEnum.VERTICAL;
	protected Float backgroundAlphaFloat;
	protected Float foregroundAlphaFloat;
	protected Double labelRotationDouble;
	protected SortedSet<JRSeriesColor>  seriesColors;


	/**
	 *
	 */
	@SuppressWarnings("deprecation")
	protected JRBaseChartPlot(JRChartPlot plot, JRChart chart)
	{
		this.chart = chart;

		if (plot != null) 
		{
			backcolor = plot.getOwnBackcolor();
			orientationValue = plot.getOrientationValue();
			backgroundAlphaFloat = plot.getBackgroundAlphaFloat();
			foregroundAlphaFloat = plot.getForegroundAlphaFloat();
			labelRotationDouble = plot.getLabelRotationDouble();
			seriesColors = new TreeSet<JRSeriesColor>(plot.getSeriesColors());
		}
		else
		{
			seriesColors = new TreeSet<JRSeriesColor>();
		}
	}


	/**
	 *
	 */
	@SuppressWarnings("deprecation")
	protected JRBaseChartPlot(JRChartPlot plot, JRBaseObjectFactory factory)
	{
		factory.put(plot, this);

		chart = (JRChart)factory.getVisitResult(plot.getChart());

		backcolor = plot.getOwnBackcolor();
		orientationValue = plot.getOrientationValue();
		backgroundAlphaFloat = plot.getBackgroundAlphaFloat();
		foregroundAlphaFloat = plot.getForegroundAlphaFloat();
		labelRotationDouble = plot.getLabelRotationDouble();
		seriesColors = new TreeSet<JRSeriesColor>(plot.getSeriesColors());
	}

	
	/**
	 *
	 */
	public JRChart getChart()
	{
		return chart;
	}

	/**
	 *
	 */
	public Color getBackcolor()
	{
		return JRStyleResolver.getBackcolor(this);
	}

	/**
	 *
	 */
	public Color getOwnBackcolor()
	{
		return this.backcolor;
	}

	/**
	 *
	 */
	public void setBackcolor(Color backcolor)
	{
		Object old = this.backcolor;
		this.backcolor = backcolor;
		getEventSupport().firePropertyChange(PROPERTY_BACKCOLOR, old, this.backcolor);
	}

	/**
	 * @deprecated Replaced by {@link #getOrientationValue()}.
	 */
	public PlotOrientation getOrientation()
	{
		return getOrientationValue().getOrientation();
	}

	/**
	 *
	 */
	public PlotOrientationEnum getOrientationValue()
	{
		return orientationValue;
	}

	/**
	 * @deprecated Replaced by {@link #setOrientation(PlotOrientationEnum)}.
	 */
	public void setOrientation(PlotOrientation orientation)
	{
		setOrientation(PlotOrientationEnum.getByValue(orientation));
	}

	/**
	 *
	 */
	public void setOrientation(PlotOrientationEnum orientationValue)
	{
		Object old = this.orientationValue;
		this.orientationValue = orientationValue;
		getEventSupport().firePropertyChange(PROPERTY_ORIENTATION, old, this.orientationValue);
	}

	/**
	 *
	 */
	public Float getBackgroundAlphaFloat()
	{
		return backgroundAlphaFloat;
	}

	/**
	 *
	 */
	public void setBackgroundAlpha(Float backgroundAlpha)
	{
		Float old = this.backgroundAlphaFloat;
		this.backgroundAlphaFloat = backgroundAlpha;
		getEventSupport().firePropertyChange(PROPERTY_BACKGROUND_ALPHA, old, this.backgroundAlphaFloat);
	}

	/**
	 *
	 */
	public Float getForegroundAlphaFloat()
	{
		return foregroundAlphaFloat;
	}

	/**
	 *
	 */
	public void setForegroundAlpha(Float foregroundAlpha)
	{
		Float old = this.foregroundAlphaFloat;
		this.foregroundAlphaFloat = foregroundAlpha;
		getEventSupport().firePropertyChange(PROPERTY_FOREGROUND_ALPHA, old, this.foregroundAlphaFloat);
	}

	/**
	 * Gets the angle in degrees to rotate the data axis labels.  The range is -360 to 360.  A positive value angles
	 * the label so it reads downwards wile a negative value angles the label so it reads upwards.  Only charts that
	 * use a category based axis (such as line or bar charts) support label rotation.
	 * @deprecated Replaced by {@link JRCategoryAxisFormat#getCategoryAxisTickLabelRotation()}.
	 */
	public Double getLabelRotationDouble()
	{
		return labelRotationDouble;
	}
	
	/**
	 * Sets the angle in degrees to rotate the data axis labels.  The range is -360 to 360.  A positive value angles
	 * the label so it reads downwards wile a negative value angles the label so it reads upwards.  Only charts that
	 * use a category based axis (such as line or bar charts) support label rotation.
	 * @deprecated Replaced by {@link JRCategoryAxisFormat#setCategoryAxisTickLabelRotation(Double)}.
	 */
	public void setLabelRotation(Double labelRotation)
	{
		Double old = this.labelRotationDouble;
		this.labelRotationDouble = labelRotation;
		getEventSupport().firePropertyChange(PROPERTY_LABEL_ROTATION, old, this.labelRotationDouble);
	}
	
	
	/**
	 * Returns a list of all the defined series colors.  Every entry in the list is of type JRChartPlot.JRSeriesColor.
	 * If there are no defined series colors this method will return an empty list, not null. 
	 */
	public SortedSet<JRSeriesColor> getSeriesColors()
	{
		return seriesColors;
	}
	
	/**
	 * Removes all defined series colors.
	 */
	public void clearSeriesColors()
	{
		setSeriesColors(null);
	}
	
	/**
	 * Adds the specified series color to the plot.
	 */
	public void addSeriesColor(JRSeriesColor seriesColor)
	{
		seriesColors.add(seriesColor);
		getEventSupport().fireCollectionElementAddedEvent(PROPERTY_SERIES_COLORS, 
				seriesColor, seriesColors.size() - 1);
	}
	
	public void setSeriesColors(Collection<JRSeriesColor> colors)
	{
		Object old = new TreeSet<JRSeriesColor>(seriesColors);
		seriesColors.clear();
		if (colors != null)
		{
			seriesColors.addAll(colors);
		}
		getEventSupport().firePropertyChange(PROPERTY_SERIES_COLORS, old, seriesColors);
	}
	
	public static class JRBaseSeriesColor implements JRChartPlot.JRSeriesColor, Serializable, Comparable<JRBaseSeriesColor>
	{
		/**
		 *
		 */
		private static final long serialVersionUID = JRConstants.SERIAL_VERSION_UID;
		
		protected int seriesOrder = -1;
		protected Color color;
		
		public JRBaseSeriesColor(int seriesOrder, Color color)
		{
			this.seriesOrder = seriesOrder;
			this.color = color;
		}
		
		/**
		 * Returns the series number (0 based) that this color applies to.
		 */
		public int getSeriesOrder()
		{
			return seriesOrder;
		}
		
		/**
		 * Returns the color to use for this series.
		 */
		public Color getColor()
		{
			return color;
		}

		public int compareTo(JRBaseSeriesColor obj) {
			if (obj == null)
			{
				throw new IllegalArgumentException();
			}
			
			return seriesOrder - obj.getSeriesOrder();
		}
		
		public Object clone()
		{
			try
			{
				return super.clone();
			}
			catch (CloneNotSupportedException e)
			{
				throw new JRRuntimeException(e);
			}
		}
	}
	

	/**
	 *
	 */
	public Object clone() 
	{
		JRBaseChartPlot clone = null;

		try
		{
			clone = (JRBaseChartPlot)super.clone();
		}
		catch (CloneNotSupportedException e)
		{
			throw new JRRuntimeException(e);
		}
		
		if (seriesColors != null)
		{
			clone.seriesColors = new TreeSet<JRSeriesColor>();
			for(Iterator<JRSeriesColor> it = seriesColors.iterator(); it.hasNext();)
			{
				clone.seriesColors.add(JRCloneUtils.nullSafeClone(it.next()));
			}
		}
		
		return clone;
	}


	/**
	 *
	 */
	public Object clone(JRChart parentChart) 
	{
		JRBaseChartPlot clone = (JRBaseChartPlot)this.clone();
		clone.chart = parentChart;
		clone.eventSupport = null;
		return clone;
	}


	private transient JRPropertyChangeSupport eventSupport;
	
	public JRPropertyChangeSupport getEventSupport()
	{
		synchronized (this)
		{
			if (eventSupport == null)
			{
				eventSupport = new JRPropertyChangeSupport(this);
			}
		}
		
		return eventSupport;
	}
	
	/*
	 * These fields are only for serialization backward compatibility.
	 */
	private int PSEUDO_SERIAL_VERSION_UID = JRConstants.PSEUDO_SERIAL_VERSION_UID; //NOPMD
	/**
	 * @deprecated
	 */
	private float backgroundAlpha = 1;
	/**
	 * @deprecated
	 */
	private float foregroundAlpha = 1;
	/**
	 * @deprecated
	 */
	private double labelRotation = 0.0;
	/**
	 * @deprecated
	 */
	private PlotOrientation orientation;
	
	@SuppressWarnings("deprecation")
	private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException
	{
		in.defaultReadObject();
		if (PSEUDO_SERIAL_VERSION_UID < JRConstants.PSEUDO_SERIAL_VERSION_UID_3_1_3)
		{
			backgroundAlphaFloat = new Float(backgroundAlpha);
			foregroundAlphaFloat = new Float(foregroundAlpha);
			labelRotationDouble = new Double(labelRotation);
		}
		if (PSEUDO_SERIAL_VERSION_UID < JRConstants.PSEUDO_SERIAL_VERSION_UID_4_1_3)
		{
			orientationValue = PlotOrientationEnum.getByValue(orientation);
			
			orientation = null;
		}
	}
	
}
