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
package net.sf.jasperreports.engine.base;

import java.io.Serializable;

import net.sf.jasperreports.engine.JRBand;
import net.sf.jasperreports.engine.JRConstants;
import net.sf.jasperreports.engine.JRRuntimeException;
import net.sf.jasperreports.engine.JRSection;
import net.sf.jasperreports.engine.design.events.JRChangeEventsSupport;
import net.sf.jasperreports.engine.design.events.JRPropertyChangeSupport;


/**
 * Used for implementing section functionality. A report can contain the following sections: detail.
 * For each group defined in the report, there is a corresponding group header section and group footer section.
 * Report sections consist of one or more bands.
 * @see JRBaseBand
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id: JRBaseBand.java 2694 2009-03-24 18:11:24Z teodord $
 */
public class JRBaseSection implements JRSection, JRChangeEventsSupport, Serializable
{
	

	/**
	 *
	 */
	private static final long serialVersionUID = JRConstants.SERIAL_VERSION_UID;
	
	protected JRBand[] bands = null;


	/**
	 *
	 */
	protected JRBaseSection()
	{
	}
	
	
	/**
	 *
	 */
	protected JRBaseSection(JRBand band)
	{
		bands = new JRBand[]{band};
	}
	
	
	/**
	 *
	 */
	protected JRBaseSection(JRSection section, JRBaseObjectFactory factory)
	{
		factory.put(section, this);
		
		/*   */
		JRBand[] jrBands = section.getBands();
		if (jrBands != null && jrBands.length > 0)
		{
			bands = new JRBand[jrBands.length];
			for(int i = 0; i < jrBands.length; i++)
			{
				bands[i] = factory.getBand(jrBands[i]);
			}
		}
	}

	/**
	 *
	 */
	public JRBand[] getBands() 
	{
		return bands;
	}	

	/**
	 *
	 */
	public Object clone() 
	{
		JRBaseSection clone = null;
		
		try
		{
			clone = (JRBaseSection)super.clone();
		}
		catch (CloneNotSupportedException e)
		{
			throw new JRRuntimeException(e);
		}

		if (bands != null)
		{
			clone.bands = new JRBand[bands.length];
			for(int i = 0; i < bands.length; i++)
			{
				clone.bands[i] = (JRBand)bands[i].clone();
			}
		}

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
