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

import net.sf.jasperreports.engine.JRPrintHyperlink;


/**
 * An abstract factory of {@link JRHyperlinkTargetProducer hyperlink target producers}.
 * <p>
 * The factory is responsible for returning a hyperlink target producer for a
 * custom hyperlink type.
 * </p>
 * 
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id: JRHyperlinkProducerFactory.java 1364 2006-08-31 15:13:20Z lucianc $
 */
public abstract class JRHyperlinkTargetProducerFactory
{

	/**
	 * Returns the hyperlink target producer associated with a specific hyperlink target.
	 * 
	 * @param linkTarget the hyperlink target
	 * @return an associated hyperlink target producer, or <code>null</code> when none associated
	 */
	public abstract JRHyperlinkTargetProducer getHyperlinkTargetProducer(String linkTarget);
	
	
	/**
	 * Generates the String hyperlink target for a hyperlink instance based on its
	 * type and on the associated hyperlink target producer.
	 * 
	 * @param hyperlink the hyperlink instance
	 * @return the genereated String hyperlink target
	 * @see JRHyperlinkTargetProducer#getHyperlinkTarget(JRPrintHyperlink)
	 */
	public String produceHyperlinkTarget(JRPrintHyperlink hyperlink)
	{
		String linkTarget = hyperlink.getLinkTarget();
		String target = null;
		if (linkTarget != null)
		{
			JRHyperlinkTargetProducer producer = getHyperlinkTargetProducer(linkTarget);
			if (producer != null)
			{
				target = producer.getHyperlinkTarget(hyperlink);
			}
		}
		return target;
	}

}
