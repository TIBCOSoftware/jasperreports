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
package net.sf.jasperreports.engine.fill;

import java.awt.Color;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.util.Random;

import net.sf.jasperreports.engine.Deduplicable;
import net.sf.jasperreports.engine.JRCommonElement;
import net.sf.jasperreports.engine.JRConstants;
import net.sf.jasperreports.engine.JRDefaultStyleProvider;
import net.sf.jasperreports.engine.JRElement;
import net.sf.jasperreports.engine.JROrigin;
import net.sf.jasperreports.engine.JRPropertiesHolder;
import net.sf.jasperreports.engine.JRPropertiesMap;
import net.sf.jasperreports.engine.JRStyle;
import net.sf.jasperreports.engine.type.ModeEnum;
import net.sf.jasperreports.engine.util.JRStyleResolver;
import net.sf.jasperreports.engine.util.ObjectUtils;


/**
 * Base class consisting of print element information shared by multiple
 * print elements.
 * 
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id$
 */
public abstract class JRTemplateElement implements JRCommonElement, Serializable, JRPropertiesHolder, Deduplicable
{


	/**
	 *
	 */
	private static final long serialVersionUID = JRConstants.SERIAL_VERSION_UID;
	
	private static final Random randomId = new Random();

	/**
	 *
	 */
	private String key;
	private ModeEnum modeValue;
	private Color forecolor;
	private Color backcolor;

	protected JROrigin origin;

	protected JRDefaultStyleProvider defaultStyleProvider;
	protected JRStyle parentStyle;

	private final String id;
	
	private JRPropertiesMap propertiesMap;
	
	/**
	 *
	 */
	protected JRTemplateElement(JROrigin origin, JRDefaultStyleProvider defaultStyleProvider)
	{
		this.origin = origin;
		this.defaultStyleProvider = defaultStyleProvider;
		id = createId();
	}

	/**
	 *
	 */
	protected JRTemplateElement(JROrigin origin, JRElement element)
	{
		this.origin = origin;
		setElement(element);
		id = createId();
	}

	protected JRTemplateElement(String id)
	{
		this.id = id;
	}
	
	private String createId()
	{
		return System.identityHashCode(this) + "_" + System.currentTimeMillis() + "_" + randomId.nextInt();
	}


	/**
	 * Copies basic element attributes: the element style, key,
	 * mode, forecolor and backcolor.
	 * 
	 * @param element the element to copy attributes from
	 */
	public void setElement(JRElement element)
	{
		parentStyle = element.getStyle();
		
		setKey(element.getKey());
		
		modeValue = element.getOwnModeValue();
		forecolor = element.getOwnForecolor();
		backcolor = element.getOwnBackcolor();
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
	public JRDefaultStyleProvider getDefaultStyleProvider()
	{
		return defaultStyleProvider;
	}

	/**
	 *
	 */
	public JRStyle getStyle()
	{
		return parentStyle;
	}

	/**
	 *
	 */
	protected JRStyle getBaseStyle()
	{
		if (parentStyle != null)
		{
			return parentStyle;
		}
		if (defaultStyleProvider != null)
		{
			return defaultStyleProvider.getDefaultStyle();
		}
		return null;
	}

	/**
	 *
	 */
	public int getWidth()
	{
		throw new UnsupportedOperationException();
	}
		
	/**
	 *
	 */
	public int getHeight()
	{
		throw new UnsupportedOperationException();
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
	public Color getForecolor()
	{
		return JRStyleResolver.getForecolor(this);
	}
	
	/**
	 *
	 */
	public Color getOwnForecolor()
	{
		return this.forecolor;
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
		return this.backcolor;
	}
	
	/**
	 *
	 */
	public void setBackcolor(Color backcolor)
	{
		this.backcolor = backcolor;
	}
	
	/**
	 *
	 */
	public String getId()
	{
		return id;
	}

	
	public String getKey()
	{
		return key;
	}

	
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

	/**
	 * Sets the template element style.
	 * 
	 * @param style the style
	 */
	public void setStyle(JRStyle style)
	{
		this.parentStyle = style;
	}

	/*
	 * These fields are only for serialization backward compatibility.
	 */
	private int PSEUDO_SERIAL_VERSION_UID = JRConstants.PSEUDO_SERIAL_VERSION_UID; //NOPMD
	/**
	 * @deprecated
	 */
	private Byte mode;
	
	private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException
	{
		in.defaultReadObject();
		
		if (PSEUDO_SERIAL_VERSION_UID < JRConstants.PSEUDO_SERIAL_VERSION_UID_3_7_2)
		{
			modeValue = ModeEnum.getByValue(mode);
			
			mode = null;
		}
	}

	protected void addTemplateHash(ObjectUtils.HashCode hash)
	{
		hash.addIdentity(defaultStyleProvider == null ? null : defaultStyleProvider.getDefaultStyle());
		hash.addIdentity(parentStyle);
		hash.add(origin);
		hash.add(key);
		hash.add(modeValue);
		hash.add(forecolor);
		hash.add(backcolor);
		hash.add(propertiesMap);
	}
	
	protected boolean templateIdentical(JRTemplateElement template)
	{
		return (defaultStyleProvider == null 
				? template.defaultStyleProvider == null 
				: template.defaultStyleProvider != null 
					&& ObjectUtils.equalsIdentity(defaultStyleProvider.getDefaultStyle(), template.defaultStyleProvider.getDefaultStyle()))
				&& ObjectUtils.equalsIdentity(parentStyle, template.parentStyle)
				&& ObjectUtils.equals(origin, template.origin)
				&& ObjectUtils.equals(key, template.key)
				&& ObjectUtils.equals(modeValue, template.modeValue)
				&& ObjectUtils.equals(forecolor, template.forecolor)
				&& ObjectUtils.equals(backcolor, template.backcolor)
				&& ObjectUtils.equals(propertiesMap, template.propertiesMap);
	}
}
