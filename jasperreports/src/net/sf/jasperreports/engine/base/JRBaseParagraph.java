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
package net.sf.jasperreports.engine.base;

import java.io.Serializable;

import net.sf.jasperreports.engine.JRConstants;
import net.sf.jasperreports.engine.JRDefaultStyleProvider;
import net.sf.jasperreports.engine.JRParagraph;
import net.sf.jasperreports.engine.JRParagraphContainer;
import net.sf.jasperreports.engine.JRRuntimeException;
import net.sf.jasperreports.engine.JRStyle;
import net.sf.jasperreports.engine.design.events.JRChangeEventsSupport;
import net.sf.jasperreports.engine.design.events.JRPropertyChangeSupport;
import net.sf.jasperreports.engine.util.JRStyleResolver;


/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id: JRBaseLineBox.java 3939 2010-08-20 09:52:00Z teodord $
 */
public class JRBaseParagraph implements JRParagraph, Serializable, Cloneable, JRChangeEventsSupport
{


	/**
	 *
	 */
	private static final long serialVersionUID = JRConstants.SERIAL_VERSION_UID;
	
	public static final String PROPERTY_TAB_STOP = "tabStop";
	

	protected JRParagraphContainer paragraphContainer;

	/**
	 *
	 */
	protected Integer tabStop;

	
	/**
	 *
	 */
	public JRBaseParagraph(JRParagraphContainer paragraphContainer)
	{
		this.paragraphContainer = paragraphContainer;
	}
	
	
	/**
	 *
	 */
	public JRDefaultStyleProvider getDefaultStyleProvider() 
	{
		if (paragraphContainer != null)
		{
			return paragraphContainer.getDefaultStyleProvider();
		}
		return null;
	}

	/**
	 *
	 */
	public JRStyle getStyle() 
	{
		if (paragraphContainer != null)
		{
			return paragraphContainer.getStyle();
		}
		return null;
	}

	/**
	 *
	 */
	public String getStyleNameReference()
	{
		if (paragraphContainer != null)
		{
			return paragraphContainer.getStyleNameReference();
		}
		return null;
	}

	/**
	 *
	 */
	public JRParagraphContainer getParagraphContainer()
	{
		return paragraphContainer;
	}

	/**
	 *
	 */
	public Integer getTabStop()
	{
		return JRStyleResolver.getTabStop(this);
	}

	/**
	 *
	 */
	public Integer getOwnTabStop()
	{
		return tabStop;
	}
	
	/**
	 *
	 */
	public void setTabStop(Integer tabStop)
	{
		Object old = this.tabStop;
		this.tabStop = tabStop;
		getEventSupport().firePropertyChange(PROPERTY_TAB_STOP, old, this.tabStop);
	}


	/**
	 * 
	 */
	public JRParagraph clone(JRParagraphContainer paragraphContainer)
	{
		JRBaseParagraph clone = null;
		
		try
		{
			clone = (JRBaseParagraph)super.clone();
		}
		catch(CloneNotSupportedException e)
		{
			throw new JRRuntimeException(e);
		}
		
		clone.paragraphContainer = paragraphContainer;
		
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

}
