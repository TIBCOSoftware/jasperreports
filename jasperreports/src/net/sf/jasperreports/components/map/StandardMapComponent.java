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
package net.sf.jasperreports.components.map;

import java.io.Serializable;

import net.sf.jasperreports.engine.JRConstants;
import net.sf.jasperreports.engine.JRExpression;
import net.sf.jasperreports.engine.JRRuntimeException;
import net.sf.jasperreports.engine.base.JRBaseObjectFactory;
import net.sf.jasperreports.engine.design.events.JRChangeEventsSupport;
import net.sf.jasperreports.engine.design.events.JRPropertyChangeSupport;
import net.sf.jasperreports.engine.type.EvaluationTimeEnum;
import net.sf.jasperreports.engine.util.JRCloneUtils;

/**
 * 
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id$
 */
public class StandardMapComponent implements MapComponent, Serializable, JRChangeEventsSupport
{

	private static final long serialVersionUID = JRConstants.SERIAL_VERSION_UID;
	
	public static final String PROPERTY_LATITUDE_EXPRESSION = "latitudeExpression";
	public static final String PROPERTY_LONGITUDE_EXPRESSION = "longitudeExpression";
	public static final String PROPERTY_ZOOM_EXPRESSION = "zoomExpression";
	public static final String PROPERTY_EVALUATION_TIME = "evaluationTime";
	public static final String PROPERTY_EVALUATION_GROUP = "evaluationGroup";

	private JRExpression latitudeExpression;
	private JRExpression longitudeExpression;
	private JRExpression zoomExpression;
	private EvaluationTimeEnum evaluationTime = EvaluationTimeEnum.NOW;
	private String evaluationGroup;
	
	private transient JRPropertyChangeSupport eventSupport;

	public StandardMapComponent()
	{
	}

	public StandardMapComponent(MapComponent map, JRBaseObjectFactory objectFactory)
	{
		this.latitudeExpression = objectFactory.getExpression(map.getLatitudeExpression());
		this.longitudeExpression = objectFactory.getExpression(map.getLongitudeExpression());
		this.zoomExpression = objectFactory.getExpression(map.getZoomExpression());
		this.evaluationTime = map.getEvaluationTime();
		this.evaluationGroup = map.getEvaluationGroup();
	}
	
	public JRExpression getLatitudeExpression()
	{
		return latitudeExpression;
	}

	public void setLatitudeExpression(JRExpression latitudeExpression)
	{
		Object old = this.latitudeExpression;
		this.latitudeExpression = latitudeExpression;
		getEventSupport().firePropertyChange(PROPERTY_LATITUDE_EXPRESSION, old, this.latitudeExpression);
	}

	public JRExpression getLongitudeExpression()
	{
		return longitudeExpression;
	}

	public void setLongitudeExpression(JRExpression longitudeExpression)
	{
		Object old = this.longitudeExpression;
		this.longitudeExpression = longitudeExpression;
		getEventSupport().firePropertyChange(PROPERTY_LONGITUDE_EXPRESSION, old, this.longitudeExpression);
	}

	public JRExpression getZoomExpression()
	{
		return zoomExpression;
	}

	public void setZoomExpression(JRExpression zoomExpression)
	{
		Object old = this.zoomExpression;
		this.zoomExpression = zoomExpression;
		getEventSupport().firePropertyChange(PROPERTY_ZOOM_EXPRESSION, old, this.zoomExpression);
	}

	public EvaluationTimeEnum getEvaluationTime()
	{
		return evaluationTime;
	}

	public void setEvaluationTime(EvaluationTimeEnum evaluationTimeValue)
	{
		Object old = this.evaluationTime;
		this.evaluationTime = evaluationTimeValue;
		getEventSupport().firePropertyChange(PROPERTY_EVALUATION_TIME, old, this.evaluationTime);
	}

	public String getEvaluationGroup()
	{
		return evaluationGroup;
	}

	public void setEvaluationGroup(String evaluationGroup)
	{
		Object old = this.evaluationGroup;
		this.evaluationGroup = evaluationGroup;
		getEventSupport().firePropertyChange(PROPERTY_EVALUATION_GROUP, 
				old, this.evaluationGroup);
	}
	
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
	
	public Object clone()
	{
		StandardMapComponent clone = null;
		try
		{
			clone = (StandardMapComponent) super.clone();
		}
		catch (CloneNotSupportedException e)
		{
			// never
			throw new JRRuntimeException(e);
		}
		clone.latitudeExpression = JRCloneUtils.nullSafeClone(latitudeExpression);
		clone.longitudeExpression = JRCloneUtils.nullSafeClone(longitudeExpression);
		clone.zoomExpression = JRCloneUtils.nullSafeClone(zoomExpression);
		clone.eventSupport = null;
		return clone;
	}

}
