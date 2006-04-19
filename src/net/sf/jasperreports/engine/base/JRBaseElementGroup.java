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
package net.sf.jasperreports.engine.base;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import net.sf.jasperreports.crosstabs.JRCrosstab;
import net.sf.jasperreports.engine.JRAbstractObjectFactory;
import net.sf.jasperreports.engine.JRChild;
import net.sf.jasperreports.engine.JRConstants;
import net.sf.jasperreports.engine.JRElement;
import net.sf.jasperreports.engine.JRElementGroup;
import net.sf.jasperreports.engine.JRFrame;
import net.sf.jasperreports.engine.xml.JRXmlWriter;


/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id$
 */
public class JRBaseElementGroup implements JRElementGroup, Serializable
{


	/**
	 *
	 */
	private static final long serialVersionUID = JRConstants.SERIAL_VERSION_UID;

	/**
	 *
	 */
	protected List children = new ArrayList();
	protected JRElementGroup elementGroup = null;


	/**
	 *
	 */
	protected JRBaseElementGroup()
	{
	}
	
	
	/**
	 *
	 */
	protected JRBaseElementGroup(JRElementGroup elementGrp, JRBaseObjectFactory factory)
	{
		factory.put(elementGrp, this);
		
		/*   */
		List list = elementGrp.getChildren();
		if (list != null && list.size() > 0)
		{
			for(int i = 0; i < list.size(); i++)
			{
				JRChild child = (JRChild)list.get(i);
				child = child.getCopy(factory);
				children.add(child);
			}
		}

		this.elementGroup = factory.getElementGroup(elementGrp.getElementGroup());
	}
		

	/**
	 *
	 */
	public List getChildren()
	{
		return this.children;
	}


	/**
	 *
	 */
	public JRElementGroup getElementGroup()
	{
		return this.elementGroup;
	}


	/**
	 *
	 */
	public static JRElement[] getElements(List children)
	{
		JRElement[] elements = null;
		
		if (children != null)
		{
			List allElements = new ArrayList();
			Object child = null;
			JRElement[] childElementArray = null;
			for(int i = 0; i < children.size(); i++)
			{
				child = children.get(i);
				if (child instanceof JRElement)
				{
					allElements.add(child);
				}
				else if (child instanceof JRElementGroup)
				{
					childElementArray = ((JRElementGroup)child).getElements();
					if (childElementArray != null)
					{
						allElements.addAll( Arrays.asList(childElementArray) );
					}
				}
			}
			
			elements = new JRElement[allElements.size()];
			allElements.toArray(elements);
		}
		
		return elements;
	}

	
	public JRElement[] getElements()
	{
		return getElements(children);
	}
	

	/**
	 *
	 */
	public static JRElement getElementByKey(JRElement[] elements, String key)
	{
		JRElement element = null;
		
		if (key != null)
		{
			if (elements != null)
			{
				int i = 0;
				while (element == null && i < elements.length)
				{
					JRElement elem = elements[i];
					if (key.equals(elem.getKey()))
					{
						element = elem;
					}
					else if (elem instanceof JRFrame)
					{
						element = ((JRFrame) elem).getElementByKey(key);
					}
					else if (elem instanceof JRCrosstab)
					{
						element = ((JRCrosstab) elem).getElementByKey(key);
					}
					i++;
				}
			}
		}
		
		return element;
	}

	
	public JRElement getElementByKey(String key)
	{
		return getElementByKey(getElements(), key);
	}

	
	/**
	 *
	 */
	public JRChild getCopy(JRAbstractObjectFactory factory)
	{
		return factory.getElementGroup(this);
	}


	/**
	 *
	 */
	public void writeXml(JRXmlWriter xmlWriter) throws IOException
	{
		xmlWriter.writeElementGroup(this);
	}


}
