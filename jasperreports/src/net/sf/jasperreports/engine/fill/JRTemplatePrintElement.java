/*
 * ============================================================================
 * GNU Lesser General Public License
 * ============================================================================
 *
 * JasperReports - Free Java report-generating library.
 * Copyright (C) 2001-2006 JasperSoft Corporation http://www.jaspersoft.com
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
 * 303 Second Street, Suite 450 North
 * San Francisco, CA 94107
 * http://www.jaspersoft.com
 */
package net.sf.jasperreports.engine.fill;

import java.awt.Color;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import net.sf.jasperreports.engine.JRConstants;
import net.sf.jasperreports.engine.JRDefaultStyleProvider;
import net.sf.jasperreports.engine.JROrigin;
import net.sf.jasperreports.engine.JRPrintElement;
import net.sf.jasperreports.engine.JRPropertiesHolder;
import net.sf.jasperreports.engine.JRPropertiesMap;
import net.sf.jasperreports.engine.JRStyle;


/**
 * Base implementation of {@link net.sf.jasperreports.engine.JRPrintElement} that uses
 * a {@link net.sf.jasperreports.engine.fill.JRTemplateElement} instance to
 * store common attributes. 
 * 
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id$
 */
public class JRTemplatePrintElement implements JRPrintElement, Serializable
{


	/**
	 *
	 */
	private static final long serialVersionUID = JRConstants.SERIAL_VERSION_UID;

	/**
	 *
	 */
	protected JRTemplateElement template = null;

	private int x = 0;
	private int y = 0;
	private int height = 0;
	private int width = 0;

	private JRPropertiesMap properties;
	
	/**
	 *
	 */
	protected JRTemplatePrintElement(JRTemplateElement element)
	{
		template = element;
	}

	/**
	 *
	 */
	public JRDefaultStyleProvider getDefaultStyleProvider()
	{
		return template.getDefaultStyleProvider();
	}
	
	/**
	 *
	 */
	public JROrigin getOrigin()
	{
		return template.getOrigin();
	}
	
	/**
	 *
	 */
	public JRStyle getStyle()
	{
		return template.getStyle();
	}
	
	/**
	 *
	 */
	public void setStyle(JRStyle style)
	{
	}
	
	/**
	 *
	 */
	public byte getMode()
	{
		return this.template.getMode();
	}
	
	/**
	 *
	 */
	public Byte getOwnMode()
	{
		return this.template.getOwnMode();
	}
	
	/**
	 *
	 */
	public void setMode(byte mode)
	{
	}
	
	/**
	 *
	 */
	public void setMode(Byte mode)
	{
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
		return this.template.getForecolor();
	}
	
	/**
	 *
	 */
	public Color getOwnForecolor()
	{
		return this.template.getOwnForecolor();
	}
	
	/**
	 *
	 */
	public void setForecolor(Color color)
	{
	}
	
	/**
	 *
	 */
	public Color getBackcolor()
	{
		return this.template.getBackcolor();
	}

	/**
	 *
	 */
	public Color getOwnBackcolor()
	{
		return this.template.getOwnBackcolor();
	}

	/**
	 *
	 */
	public void setBackcolor(Color color)
	{
	}

	
	public JRTemplateElement getTemplate()
	{
		return template;
	}

	public void setTemplate(JRTemplateElement template)
	{
		this.template = template;
		
		if (properties != null)
		{
			if (this.template != null && this.template.hasProperties())
			{
				properties.setBaseProperties(this.template.getPropertiesMap());
			}
			else
			{
				properties.setBaseProperties(null);
			}
		}
	}

	public String getKey()
	{
		return template.getKey();
	}

	/**
	 * Returns null as external style references are not allowed for print objects.
	 */
	public String getStyleNameReference()
	{
		return null;
	}

	/**
	 * 
	 */
	public Color getDefaultLineColor() 
	{
		return getForecolor();
	}

	public synchronized boolean hasProperties()
	{
		return properties != null && properties.hasProperties()
				|| template.hasProperties();
	}

	public synchronized JRPropertiesMap getPropertiesMap()
	{
		if (properties == null)
		{
			//FIXME avoid this on read only calls
			properties = new JRPropertiesMap();
			
			if (template.hasProperties())
			{
				properties.setBaseProperties(template.getPropertiesMap());
			}
		}
		
		return properties;
	}

	public JRPropertiesHolder getParentProperties()
	{
		return null;
	}

	private synchronized void writeObject(ObjectOutputStream out) throws IOException
	{
		if (properties != null && !properties.hasOwnProperties())
		{
			properties = null;
		}
		
		out.defaultWriteObject();
	}
}
