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
package net.sf.jasperreports.components.barcode4j;

import java.io.IOException;
import java.io.ObjectInputStream;

import net.sf.jasperreports.engine.JRConstants;
import net.sf.jasperreports.engine.JRExpression;
import net.sf.jasperreports.engine.util.JRCloneUtils;

import org.krysalis.barcode4j.HumanReadablePlacement;

/**
 * 
 * @author Lucian Chirita (lucianc@users.sourceforge.net)
 */
public abstract class Barcode4jComponent extends BarcodeComponent
{
	public static final String PROPERTY_ORIENTATION = "orientation";
	public static final String PROPERTY_PATTERN_EXPRESSION = "patternExpression";
	public static final String PROPERTY_MODULE_WIDTH = "moduleWidth";
	public static final String PROPERTY_TEXT_POSITION = "textPosition";
	public static final String PROPERTY_QUIET_ZONE = "quietZone";
	public static final String PROPERTY_VERTICAL_QUIET_ZONE = "verticalQuietZone";
	
	private static final long serialVersionUID = JRConstants.SERIAL_VERSION_UID;
	
	private OrientationEnum orientationValue;
	private JRExpression patternExpression;
	private Double moduleWidth;
	private TextPositionEnum textPositionValue;
	private Double quietZone;
	private Double verticalQuietZone;

	public Barcode4jComponent()
	{
	}
	
	/**
	 * @deprecated Replaced by {@link #getOrientationValue()}.
	 */
	public int getOrientation()
	{
		return getOrientationValue().getValue();
	}

	/**
	 * @deprecated Replaced by {@link #setOrientation(OrientationEnum)}.
	 */
	public void setOrientation(int orientation)
	{
		setOrientation(OrientationEnum.getByValue(orientation));
	}

	public OrientationEnum getOrientationValue()
	{
		return orientationValue == null ? OrientationEnum.UP : orientationValue;
	}

	/**
	 * Used only for the sake of digester.
	 * @deprecated Replaced by {@link #setOrientation(OrientationEnum)}.
	 */
	public void setOrientationValue(OrientationEnum orientationValue)
	{
		setOrientation(orientationValue);
	}

	public void setOrientation(OrientationEnum orientationValue)
	{
		OrientationEnum old = this.orientationValue;
		this.orientationValue = orientationValue;
		getEventSupport().firePropertyChange(PROPERTY_ORIENTATION, 
				old, this.orientationValue);
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

	/**
	 * @deprecated Replaced by {@link #getTextPositionValue()}.
	 */
	public String getTextPosition()
	{
		return textPosition == null ? null : getTextPositionValue().getName();
	}

	/**
	 * @deprecated Replaced by {@link #setTextPosition(TextPositionEnum)}.
	 */
	public void setTextPosition(String textPosition)
	{
		setTextPosition(TextPositionEnum.getByName(textPosition));
	}

	public TextPositionEnum getTextPositionValue()
	{
		return textPositionValue;
	}

	/**
	 * Used only for the sake of digester.
	 * @deprecated Replaced by {@link #setTextPosition(TextPositionEnum)}.
	 */
	public void setTextPositionValue(TextPositionEnum textPositionValue)
	{
		setTextPosition(textPositionValue);
	}

	public void setTextPosition(TextPositionEnum textPositionValue)
	{
		Object old = this.textPositionValue;
		this.textPositionValue = textPositionValue;
		getEventSupport().firePropertyChange(PROPERTY_TEXT_POSITION, 
				old, this.textPositionValue);
	}

	/**
	 * @deprecated Replaced by {@link #setTextPosition(TextPositionEnum)}.
	 */
	public void setTextPosition(HumanReadablePlacement textPosition)
	{
		setTextPosition(textPosition == null ? null : textPosition.getName());
	}

	public Object clone()
	{
		Barcode4jComponent clone = (Barcode4jComponent) super.clone();

		clone.patternExpression = JRCloneUtils.nullSafeClone(patternExpression);
		
		return clone;
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


	/*
	 * These fields are only for serialization backward compatibility.
	 */
	private int PSEUDO_SERIAL_VERSION_UID = JRConstants.PSEUDO_SERIAL_VERSION_UID; //NOPMD
	/**
	 * @deprecated
	 */
	private int orientation;
	/**
	 * @deprecated
	 */
	private String textPosition;

	
	private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException
	{
		in.defaultReadObject();
		
		if (PSEUDO_SERIAL_VERSION_UID < JRConstants.PSEUDO_SERIAL_VERSION_UID_6_0_2)
		{
			orientationValue = OrientationEnum.getByValue(orientation);
			textPositionValue = TextPositionEnum.getByName(textPosition);
			
			textPosition = null;
		}
	}
}
