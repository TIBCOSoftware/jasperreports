/*
 * ============================================================================
 * GNU Lesser General Public License
 * ============================================================================
 *
 * JasperReports - Free Java report-generating library.
 * Copyright (C) 2001-2005 JasperSoft Corporation http://www.jaspersoft.com
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

import java.util.HashMap;
import java.util.Map;


/**
 * Factory class used for fill-time working clones creation.
 * <p>
 * A instance of this class is used for each created clone.
 * 
 * @author Lucian Chirita (lucianc@users.sourceforge.net)
 * @version $Id$
 */
public class JRFillCloneFactory
{
	private Map cloneMap;
	
	public JRFillCloneFactory()
	{
		cloneMap = new HashMap();
	}

	protected JRCloneable getCached(JRCloneable original)
	{
		return (JRCloneable) cloneMap.get(original);
	}

	public void put(JRCloneable original, JRCloneable clone)
	{
		cloneMap.put(original, clone);
	}

	public JRCloneable getClone(JRCloneable original)
	{
		JRCloneable clone;
		
		if (original == null)
		{
			clone = null;
		}
		else
		{
			clone = getCached(original);
			
			if (clone == null)
			{
				clone = original.createClone(this);
			}
		}

		return clone;
	}
}
