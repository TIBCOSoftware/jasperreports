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
package net.sf.jasperreports.engine.type;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import net.sf.jasperreports.engine.JRConstants;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id: JasperCompileManager.java 3033 2009-08-27 11:46:22Z teodord $
 */
public abstract class AbstractEnum implements Serializable
{
	/**
	 *
	 */
	private static final long serialVersionUID = JRConstants.SERIAL_VERSION_UID;

	private static final Log log = LogFactory.getLog(AbstractEnum.class);

	private static final Map globalValueMap = new HashMap();
	private static final Map globalNameMap = new HashMap();

	private final byte value;
	private final transient String name;
	
	/**
	 *
	 */
	protected AbstractEnum(byte value, String name)
	{
		this.value = value;
		this.name = name;
		
		Map valueMap = (Map)globalValueMap.get(getClass().getName());
		if (valueMap == null)
		{
			valueMap = new HashMap();
			globalValueMap.put(getClass().getName(), valueMap);
		}
		valueMap.put(new Byte(value), this);
		
		
		Map nameMap = (Map)globalNameMap.get(getClass().getName());
		if (nameMap == null)
		{
			nameMap = new HashMap();
			globalNameMap.put(getClass().getName(), nameMap);
		}
		nameMap.put(name, this);
	}
	
	/**
	 *
	 */
	public Byte getValueByte()
	{
		return new Byte(value);
	}
	
	/**
	 *
	 */
	public byte getValue()
	{
		return value;
	}
	
	/**
	 *
	 */
	public String getName()
	{
		return name;
	}
	
	/**
	 *
	 */
	public static AbstractEnum get(Class clazz, Byte value)
	{
		if (value != null)
		{
			Map valueMap = (Map)globalValueMap.get(clazz.getName());
			if (valueMap != null)
			{
				AbstractEnum instance  = (AbstractEnum)valueMap.get(value);
				if (instance == null)
				{
					if (log.isWarnEnabled())
						log.warn("Type enum not found for class '" + clazz.getName() + "' and value " + value + ".");
				}
				return instance;
			}
		}
		return null;
	}
	
	/**
	 *
	 */
	public static AbstractEnum get(Class clazz, byte value)
	{
		return get(clazz, new Byte(value));
	}
	
	/**
	 *
	 */
	public static AbstractEnum get(Class clazz, String name)
	{
		if (name != null)
		{
			Map nameMap = (Map)globalNameMap.get(clazz.getName());
			if (nameMap != null)
			{
				AbstractEnum instance  = (AbstractEnum)nameMap.get(name);
				if (instance == null)
				{
					if (log.isWarnEnabled())
						log.warn("Type enum not found for class " + clazz.getName() + " and name " + name + ".");
				}
				return instance;
			}
		}
		return null;
	}
}
