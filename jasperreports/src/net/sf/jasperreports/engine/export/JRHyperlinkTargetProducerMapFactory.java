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
package net.sf.jasperreports.engine.export;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import net.sf.jasperreports.engine.JRConstants;


/**
 * Map-based hyperlink target producer factory implementation.
 * <p>
 * This implementation wraps a hyperlink target to hyperling target producer instance
 * association map.
 * </p>
 * 
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id: JRHyperlinkProducerMapFactory.java 1368 2006-09-01 12:01:52Z lucianc $
 */
public class JRHyperlinkTargetProducerMapFactory extends JRHyperlinkTargetProducerFactory implements Serializable
{
	private static final long serialVersionUID = JRConstants.SERIAL_VERSION_UID;

	private Map producers;
	
	
	/**
	 * Creates a blank factory.
	 */
	public JRHyperlinkTargetProducerMapFactory()
	{
		producers = new HashMap();
	}

	
	/**
	 * Exposes the target to producer association map.
	 * 
	 * @return the target to producer association map
	 */
	public Map getProducersMap()
	{
		return producers;
	}

	
	/**
	 * Sets the target to producer association map.
	 *
	 * @param producers bulk target to producer association map
	 * @see #getProducersMap()
	 */
	public void setProducersMap(Map producers)
	{
		this.producers = producers;
	}
	
	
	/**
	 * Adds a hyperlink target producer instance associated to a hyperlink target.
	 *  
	 * @param linkTarget the target
	 * @param producer the producer
	 */
	public void addProducer(String linkTarget, JRHyperlinkTargetProducer producer)
	{
		producers.put(linkTarget, producer);
	}
	
	
	/**
	 * Removes a target to producer association.
	 * 
	 * @param linkTarget the hyperlink target
	 * @return the producer which was associated to the target, if any
	 */
	public JRHyperlinkTargetProducer removeProducer(String linkTarget)
	{
		return (JRHyperlinkTargetProducer) producers.remove(linkTarget);
	}
	
	
	public JRHyperlinkTargetProducer getHyperlinkTargetProducer(String linkTarget)
	{
		return (JRHyperlinkTargetProducer) producers.get(linkTarget);
	}

}
