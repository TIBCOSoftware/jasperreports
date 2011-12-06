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
package net.sf.jasperreports.engine.base;

import java.io.Serializable;

import net.sf.jasperreports.engine.JRBand;
import net.sf.jasperreports.engine.JRConstants;
import net.sf.jasperreports.engine.JRRuntimeException;
import net.sf.jasperreports.engine.JRSection;
import net.sf.jasperreports.engine.design.events.JRChangeEventsSupport;
import net.sf.jasperreports.engine.design.events.JRPropertyChangeSupport;
import net.sf.jasperreports.engine.util.JRCloneUtils;


/**
 * Used for implementing section functionality. A report can contain the following sections: detail.
 * For each group defined in the report, there is a corresponding group header section and group footer section.
 * Report sections consist of one or more bands.
 * @see JRBaseBand
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id$
 */
public class JRBaseSection implements JRSection, JRChangeEventsSupport, Serializable
{
	

	/**
	 *
	 */
	private static final long serialVersionUID = JRConstants.SERIAL_VERSION_UID;
	
	protected JRBand[] bands;


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

		clone.bands = JRCloneUtils.cloneArray(bands);
		clone.eventSupport = null;

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
