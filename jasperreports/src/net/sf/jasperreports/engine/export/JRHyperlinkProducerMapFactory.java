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

import java.util.HashMap;
import java.util.Map;


/**
 * Map-based hyperlink producer factory implementation.
 * <p>
 * This implementation wraps a hyperlink type to hyperling producer instance
 * association map.
 * </p>
 * 
 * @author Lucian Chirita (lucianc@users.sourceforge.net)
 * @version $Id$
 */
public class JRHyperlinkProducerMapFactory extends JRHyperlinkProducerFactory
{
	
	private Map producers;
	
	
	/**
	 * Creates a blank factory.
	 */
	public JRHyperlinkProducerMapFactory()
	{
		producers = new HashMap();
	}

	
	/**
	 * Exposes the type to producer association map.
	 * 
	 * @return the type to producer association map
	 */
	public Map getProducersMap()
	{
		return producers;
	}

	
	/**
	 * Sets the type to producer association map.
	 *
	 * @param producers bulk type to producer association map
	 * @see #getHandlersMap()
	 */
	public void setProducersMap(Map producers)
	{
		this.producers = producers;
	}
	
	
	/**
	 * Adds a hyperlink producer instance associated to a hyperlink type.
	 *  
	 * @param linkType the type
	 * @param producer the producer
	 */
	public void addProducer(String linkType, JRHyperlinkProducer producer)
	{
		producers.put(linkType, producer);
	}
	
	
	/**
	 * Removes a type to producer association.
	 * 
	 * @param linkType the hyperlink type
	 * @return the producer which was associated to the type, if any
	 */
	public JRHyperlinkProducer removeProducer(String linkType)
	{
		return (JRHyperlinkProducer) producers.remove(linkType);
	}
	
	
	public JRHyperlinkProducer getHandler(String linkType)
	{
		return (JRHyperlinkProducer) producers.get(linkType);
	}

}
