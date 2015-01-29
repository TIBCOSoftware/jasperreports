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
import java.util.UUID;

import net.sf.jasperreports.engine.JRConstants;
import net.sf.jasperreports.engine.JRDefaultStyleProvider;
import net.sf.jasperreports.engine.JROrigin;
import net.sf.jasperreports.engine.JRPrintElement;
import net.sf.jasperreports.engine.JRPropertiesHolder;
import net.sf.jasperreports.engine.JRPropertiesMap;
import net.sf.jasperreports.engine.JRStyle;
import net.sf.jasperreports.engine.PrintElementVisitor;
import net.sf.jasperreports.engine.type.ModeEnum;
import net.sf.jasperreports.engine.util.JRStyleResolver;


/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 */
public class JRBasePrintElement implements JRPrintElement, Serializable
{


	/**
	 *
	 */
	private static final long serialVersionUID = JRConstants.SERIAL_VERSION_UID;

	protected UUID uuid;
	protected JROrigin origin;
	protected String key;
	
	/**
	 *
	 */
	protected ModeEnum modeValue;
	protected int x;
	protected int y;
	protected int width;
	protected int height;
	protected Color forecolor;
	protected Color backcolor;

	protected JRDefaultStyleProvider defaultStyleProvider;
	protected JRStyle style;
	
	private JRPropertiesMap propertiesMap;
	private int sourceElementId;
	private int printElementId;

	
	/**
	 *
	 */
	public JRBasePrintElement(JRDefaultStyleProvider defaultStyleProvider)
	{
		this.defaultStyleProvider = defaultStyleProvider;
	}
	

	/**
	 *
	 */
	public JRDefaultStyleProvider getDefaultStyleProvider()
	{
		return defaultStyleProvider;
	}
	
	/**
	 *
	 */
	public UUID getUUID()
	{
		return uuid;
	}

	/**
	 *
	 */
	public void setUUID(UUID uuid)
	{
		this.uuid = uuid;
	}

	/**
	 *
	 */
	public JROrigin getOrigin()
	{
		return origin;
	}
	
	/**
	 *
	 */
	public void setOrigin(JROrigin origin)
	{
		this.origin = origin;
	}

	/**
	 *
	 */
	public JRStyle getStyle()
	{
		return style;
	}
	
	/**
	 *
	 */
	public void setStyle(JRStyle style)
	{
		this.style = style;
	}

	/**
	 *
	 */
	public ModeEnum getModeValue()
	{
		return JRStyleResolver.getMode(this, ModeEnum.OPAQUE);
	}

	/**
	 *
	 */
	public ModeEnum getOwnModeValue()
	{
		return modeValue;
	}

	/**
	 *
	 */
	public void setMode(ModeEnum modeValue)
	{
		this.modeValue = modeValue;
	}

	/**
	 *
	 */
	public int getX()
	{
		return this.x;
	}
	
	/**
	 *
	 */
	public void setX(int x)
	{
		this.x = x;
	}
	
	/**
	 *
	 */
	public int getY()
	{
		return this.y;
	}
	
	/**
	 *
	 */
	public void setY(int y)
	{
		this.y = y;
	}
	
	/**
	 *
	 */
	public int getWidth()
	{
		return this.width;
	}
	
	/**
	 *
	 */
	public void setWidth(int width)
	{
		this.width = width;
	}
	
	/**
	 *
	 */
	public int getHeight()
	{
		return this.height;
	}
	
	/**
	 *
	 */
	public void setHeight(int height)
	{
		this.height = height;
	}
	
	/**
	 *
	 */
	public Color getForecolor()
	{
		return JRStyleResolver.getForecolor(this);
	}
	
	/**
	 *
	 */
	public Color getOwnForecolor()
	{
		return forecolor;
	}
	
	/**
	 *
	 */
	public void setForecolor(Color forecolor)
	{
		this.forecolor = forecolor;
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
		return backcolor;
	}

	/**
	 *
	 */
	public void setBackcolor(Color backcolor)
	{
		this.backcolor = backcolor;
	}


	
	public String getKey()
	{
		return key;
	}


	/**
	 * Sets the print element key.
	 * 
	 * @param key the element key
	 * @see #getKey()
	 */
	public void setKey(String key)
	{
		this.key = key;
	}


	/**
	 * Returns null as external style references are not allowed for print objects.
	 */
	public String getStyleNameReference()
	{
		return null;
	}
	

	public synchronized boolean hasProperties()
	{
		return propertiesMap != null && propertiesMap.hasProperties();
	}

	public synchronized JRPropertiesMap getPropertiesMap()
	{
		if (propertiesMap == null)
		{
			propertiesMap = new JRPropertiesMap();
		}
		return propertiesMap;
	}

	public JRPropertiesHolder getParentProperties()
	{
		return null;
	}

	
	/*
	 * These fields are only for serialization backward compatibility.
	 */
	private int PSEUDO_SERIAL_VERSION_UID = JRConstants.PSEUDO_SERIAL_VERSION_UID; //NOPMD
	/**
	 * @deprecated
	 */
	private Byte mode;
	
	@SuppressWarnings("deprecation")
	private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException
	{
		in.defaultReadObject();
		
		if (PSEUDO_SERIAL_VERSION_UID < JRConstants.PSEUDO_SERIAL_VERSION_UID_3_7_2)
		{
			modeValue = ModeEnum.getByValue(mode);
			
			mode = null;
		}
	}


	public <T> void accept(PrintElementVisitor<T> visitor, T arg)
	{
		throw new UnsupportedOperationException();
	}

	public int getSourceElementId()
	{
		return sourceElementId;
	}

	/**
	 * Sets the source/fill element Id for the print element.
	 * 
	 * @param sourceElementId
	 * @see #getSourceElementId()
	 */
	public void setSourceElementId(int sourceElementId)
	{
		this.sourceElementId = sourceElementId;
	}

	@Override
	public int getPrintElementId()
	{
		return printElementId;
	}

	/**
	 * Sets the print Id for this element.
	 * 
	 * @param printElementId the print Id
	 * @see #getPrintElementId()
	 */
	public void setPrintElementId(int printElementId)
	{
		this.printElementId = printElementId;
	}

}
