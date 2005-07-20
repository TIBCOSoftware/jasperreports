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
 * 185, Berry Street, Suite 6200
 * San Francisco CA 94107
 * http://www.jaspersoft.com
 */
package net.sf.jasperreports.engine.base;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

import net.sf.jasperreports.engine.JRPrintElement;
import net.sf.jasperreports.engine.JRPrintPage;
import net.sf.jasperreports.engine.base.JRVirtualPrintPage.IdentityDataProvider;
import net.sf.jasperreports.engine.base.JRVirtualPrintPage.ObjectIDPair;


/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id$
 */
public class JRBasePrintPage implements JRPrintPage, Serializable
{


	/**
	 *
	 */
	private static final long serialVersionUID = 10000;

	/**
	 *
	 */
	protected List elements = new ArrayList();
	
	
	private transient IdentityDataProvider identityDataProvider = null;


	/**
	 *
	 */
	public List getElements()
	{
		return this.elements;
	}
		
	/**
	 *
	 */
	public void setElements(List elements)
	{
		this.elements = elements;
	}
		
	/**
	 *
	 */
	public void addElement(JRPrintElement element)
	{
		this.elements.add(element);
	}
	

	private final class PageIdentityDataProvider implements IdentityDataProvider
	{
		public ObjectIDPair[] getIdentityData(JRVirtualPrintPage page)
		{
			return null;
		}

		public void setIdentityData(JRVirtualPrintPage page, ObjectIDPair[] identityData)
		{
			if (identityData != null && identityData.length > 0)
			{
				Map idMap = new HashMap();
				for (int i = 0; i < identityData.length; i++)
				{
					idMap.put(new Integer(identityData[i].getIdentity()), identityData[i].getObject()); 
				}
				
				for (ListIterator i = elements.listIterator(); i.hasNext();)
				{
					Object element = i.next();
					Integer id = new Integer(System.identityHashCode(element));
					
					Object idObject = idMap.get(id);
					if (idObject != null)
					{
						i.set(idObject);
					}
				}
			}
		}
	}
	
	
	public IdentityDataProvider createIdentityDataProvider ()
	{
		if (identityDataProvider == null)
		{
			identityDataProvider = new PageIdentityDataProvider(); 
		}

		return identityDataProvider; 
	}
}
