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
package net.sf.jasperreports.chartthemes.simple;


import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Paint;
import java.awt.Stroke;

import net.sf.jasperreports.engine.design.events.JRChangeEventsSupport;
import net.sf.jasperreports.engine.design.events.JRPropertyChangeSupport;

import org.jfree.chart.block.BlockFrame;
import org.jfree.chart.block.LineBorder;
import org.jfree.ui.RectangleInsets;

/**
 * 
 * @author Lucian Chirita (lucianc@users.sourceforge.net)
 */
public class LineBorderProvider implements BlockFrameProvider, JRChangeEventsSupport
{

	public static final String PROPERTY_INSETS = "insets";
	public static final String PROPERTY_LINE_STROKE = "lineStroke";
	public static final String PROPERTY_PAINT = "paint";
	
	private RectangleInsets insets;
	private Stroke lineStroke;
	private PaintProvider paint;

	public LineBorderProvider()
	{
	}

	public LineBorderProvider(RectangleInsets insets, Stroke stroke, PaintProvider paint)
	{
		this.insets = insets;
		this.lineStroke = stroke;
		this.paint = paint;
	}

	@Override
	public BlockFrame getBlockFrame()
	{
		RectangleInsets borderInsets = insets == null ? new RectangleInsets(1.0, 1.0, 1.0, 1.0) : insets;
		Stroke borderStroke = lineStroke == null ? new BasicStroke(1.0f) : lineStroke;
		Paint borderPaint = paint == null ? null : paint.getPaint();
		if (borderPaint == null) 
		{
			borderPaint = Color.BLACK;
		}
		
		return new LineBorder(borderPaint, borderStroke, borderInsets);
	}

	public RectangleInsets getInsets()
	{
		return insets;
	}


	public void setInsets(RectangleInsets insets)
	{
		Object old = this.insets;
		this.insets = insets;
		getEventSupport().firePropertyChange(PROPERTY_INSETS, old, this.insets);
	}

	public Stroke getLineStroke()
	{
		return lineStroke;
	}

	public void setLineStroke(Stroke stroke)
	{
		Object old = this.lineStroke;
		this.lineStroke = stroke;
		getEventSupport().firePropertyChange(PROPERTY_LINE_STROKE, old, this.lineStroke);
	}

	public PaintProvider getPaint()
	{
		return paint;
	}

	public void setPaint(PaintProvider paint)
	{
		Object old = this.paint;
		this.paint = paint;
		getEventSupport().firePropertyChange(PROPERTY_PAINT, old, this.paint);
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

}
