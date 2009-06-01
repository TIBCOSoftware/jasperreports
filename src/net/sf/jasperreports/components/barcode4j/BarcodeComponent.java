/*
 * ============================================================================
 * GNU Lesser General Public License
 * ============================================================================
 *
 * JasperReports - Free Java report-generating library.
 * Copyright (C) 2001-2009 JasperSoft Corporation http://www.jaspersoft.com
 * 
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307, USA.
 * 
 * JasperSoft Corporation
 * 539 Bryant Street, Suite 100
 * San Francisco, CA 94107
 * http://www.jaspersoft.com
 */
package net.sf.jasperreports.components.barcode4j;

import java.io.Serializable;

import org.krysalis.barcode4j.HumanReadablePlacement;

import net.sf.jasperreports.engine.JRConstants;
import net.sf.jasperreports.engine.JRExpression;
import net.sf.jasperreports.engine.JRRuntimeException;
import net.sf.jasperreports.engine.component.Component;
import net.sf.jasperreports.engine.design.events.JRChangeEventsSupport;
import net.sf.jasperreports.engine.design.events.JRPropertyChangeSupport;
import net.sf.jasperreports.engine.util.JRProperties;

/**
 * 
 * @author Lucian Chirita (lucianc@users.sourceforge.net)
 * @version $Id$
 */
public abstract class BarcodeComponent implements Component, Serializable, Cloneable, JRChangeEventsSupport
{

	public static final String PROPERTY_PREFIX = 
		JRProperties.PROPERTY_PREFIX + "components.barcode4j.";

	public static final int ORIENTATION_UP = 0;
	public static final int ORIENTATION_LEFT = 90;
	public static final int ORIENTATION_DOWN = 180;
	public static final int ORIENTATION_RIGHT = 270;
	
	public static final String PROPERTY_EVALUATION_TIME = "evaluationTime";
	public static final String PROPERTY_EVALUATION_GROUP = "evaluationGroup";
	public static final String PROPERTY_ORIENTATION = "orientation";
	public static final String PROPERTY_CODE_EXPRESSION = "codeExpression";
	public static final String PROPERTY_PATTERN_EXPRESSION = "patternExpression";
	public static final String PROPERTY_MODULE_WIDTH = "moduleWidth";
	public static final String PROPERTY_TEXT_POSITION = "textPosition";
	public static final String PROPERTY_QUIET_ZONE = "quietZone";
	public static final String PROPERTY_VERTICAL_QUIET_ZONE = "verticalQuietZone";
	
	private static final long serialVersionUID = JRConstants.SERIAL_VERSION_UID;
	
	private transient JRPropertyChangeSupport eventSupport;
	
	private byte evaluationTime;
	private String evaluationGroup;
	
	private int orientation;
	private JRExpression codeExpression;
	private JRExpression patternExpression;
	private Double moduleWidth;
	private String textPosition;
	private Double quietZone;
	private Double verticalQuietZone;

	public byte getEvaluationTime()
	{
		return evaluationTime;
	}

	public void setEvaluationTime(byte evaluationTime)
	{
		byte old = this.evaluationTime;
		this.evaluationTime = evaluationTime;
		getEventSupport().firePropertyChange(PROPERTY_EVALUATION_TIME, 
				old, this.evaluationTime);
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

	public int getOrientation()
	{
		return orientation;
	}

	public void setOrientation(int orientation)
	{
		int old = this.orientation;
		this.orientation = orientation;
		getEventSupport().firePropertyChange(PROPERTY_ORIENTATION, 
				old, this.orientation);
	}

	public JRExpression getCodeExpression()
	{
		return codeExpression;
	}

	public void setCodeExpression(JRExpression codeExpression)
	{
		Object old = this.codeExpression;
		this.codeExpression = codeExpression;
		getEventSupport().firePropertyChange(PROPERTY_CODE_EXPRESSION, 
				old, this.codeExpression);
	}

	public JRExpression getPatternExpression()
	{
		return patternExpression;
	}

	public void setPatternExpression(JRExpression patternExpression)
	{
		Object old = this.patternExpression;
		this.patternExpression = patternExpression;
		getEventSupport().firePropertyChange(PROPERTY_PATTERN_EXPRESSION, 
				old, this.patternExpression);
	}

	public Double getModuleWidth()
	{
		return moduleWidth;
	}

	public void setModuleWidth(Double moduleWidth)
	{
		Object old = this.moduleWidth;
		this.moduleWidth = moduleWidth;
		getEventSupport().firePropertyChange(PROPERTY_MODULE_WIDTH, 
				old, this.moduleWidth);
	}

	public String getTextPosition()
	{
		return textPosition;
	}

	public void setTextPosition(String textPosition)
	{
		Object old = this.textPosition;
		this.textPosition = textPosition;
		getEventSupport().firePropertyChange(PROPERTY_TEXT_POSITION, 
				old, this.textPosition);
	}

	public void setTextPosition(HumanReadablePlacement textPosition)
	{
		setTextPosition(textPosition == null ? null : textPosition.getName());
	}

	public BarcodeComponent cloneBarcode()
	{
		try
		{
			return (BarcodeComponent) super.clone();
		}
		catch (CloneNotSupportedException e)
		{
			// never
			throw new JRRuntimeException(e);
		}
	}
	
	public abstract void receive(BarcodeVisitor visitor);

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

	public Double getQuietZone()
	{
		return quietZone;
	}

	public void setQuietZone(Double quietZone)
	{
		Object old = this.quietZone;
		this.quietZone = quietZone;
		getEventSupport().firePropertyChange(PROPERTY_QUIET_ZONE, 
				old, this.quietZone);
	}

	public Double getVerticalQuietZone()
	{
		return verticalQuietZone;
	}

	public void setVerticalQuietZone(Double verticalQuietZone)
	{
		Object old = this.verticalQuietZone;
		this.verticalQuietZone = verticalQuietZone;
		getEventSupport().firePropertyChange(PROPERTY_VERTICAL_QUIET_ZONE, 
				old, this.verticalQuietZone);
	}
	
}
