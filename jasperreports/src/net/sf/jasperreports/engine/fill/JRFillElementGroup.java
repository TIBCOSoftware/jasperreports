/*
 * ============================================================================
 *                   The JasperReports License, Version 1.0
 * ============================================================================
 * 
 * Copyright (C) 2001-2004 Teodor Danciu (teodord@users.sourceforge.net). All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without modification,
 * are permitted provided that the following conditions are met:
 * 
 * 1. Redistributions of source code must retain the above copyright notice,
 *    this list of conditions and the following disclaimer.
 * 
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 * 
 * 3. The end-user documentation included with the redistribution, if any, must
 *    include the following acknowledgment: "This product includes software
 *    developed by Teodor Danciu (http://jasperreports.sourceforge.net)."
 *    Alternately, this acknowledgment may appear in the software itself, if
 *    and wherever such third-party acknowledgments normally appear.
 * 
 * 4. The name "JasperReports" must not be used to endorse or promote products 
 *    derived from this software without prior written permission. For written 
 *    permission, please contact teodord@users.sourceforge.net.
 * 
 * 5. Products derived from this software may not be called "JasperReports", nor 
 *    may "JasperReports" appear in their name, without prior written permission
 *    of Teodor Danciu.
 * 
 * THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED WARRANTIES,
 * INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
 * FITNESS  FOR A PARTICULAR  PURPOSE ARE  DISCLAIMED.  IN NO  EVENT SHALL  THE
 * APACHE SOFTWARE  FOUNDATION  OR ITS CONTRIBUTORS  BE LIABLE FOR  ANY DIRECT,
 * INDIRECT, INCIDENTAL, SPECIAL,  EXEMPLARY, OR CONSEQUENTIAL  DAMAGES (INCLU-
 * DING, BUT NOT LIMITED TO, PROCUREMENT  OF SUBSTITUTE GOODS OR SERVICES; LOSS
 * OF USE, DATA, OR  PROFITS; OR BUSINESS  INTERRUPTION)  HOWEVER CAUSED AND ON
 * ANY  THEORY OF LIABILITY,  WHETHER  IN CONTRACT,  STRICT LIABILITY,  OR TORT
 * (INCLUDING  NEGLIGENCE OR  OTHERWISE) ARISING IN  ANY WAY OUT OF THE  USE OF
 * THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

/*
 * ============================================================================
 *                   GNU Lesser General Public License
 * ============================================================================
 *
 * JasperReports - Free Java report-generating library.
 * Copyright (C) 2001-2004 Teodor Danciu teodord@users.sourceforge.net
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
 * Teodor Danciu
 * 173, Calea Calarasilor, Bl. 42, Sc. 1, Ap. 18
 * Postal code 030615, Sector 3
 * Bucharest, ROMANIA
 * Email: teodord@users.sourceforge.net
 */
package dori.jasper.engine.fill;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import dori.jasper.engine.JRElement;
import dori.jasper.engine.JRElementGroup;


/**
 *
 */
public class JRFillElementGroup implements JRElementGroup
{


	/**
	 *
	 */
	protected List children = new ArrayList();
	protected JRElementGroup elementGroup = null;

	/**
	 *
	 */
	protected JRFillElement[] elements = null;

	/**
	 *
	 */
	private JRElement topElementInGroup = null;
	private JRElement bottomElementInGroup = null;
	private int stretchHeightDiff = 0;


	/**
	 *
	 */
	protected JRFillElementGroup(
		JRElementGroup elementGroup, 
		JRFillObjectFactory factory
		)
	{
		if (elementGroup != null)
		{
			factory.put(elementGroup, this);
	
			/*   */
			List list = elementGroup.getChildren();
			if (list != null && list.size() > 0)
			{
				Object child = null;
				for(int i = 0; i < list.size(); i++)
				{
					child = list.get(i);
					if (child instanceof JRElement)
					{
						child = factory.getElement((JRElement)child);
						this.children.add(child);
					}
					else if (child instanceof JRElementGroup)
					{
						child = factory.getElementGroup((JRElementGroup)child);
						this.children.add(child);
					}
				}
			}
	
			/*   */
			this.getElements();
	
			elementGroup = factory.getElementGroup(elementGroup.getElementGroup());
		}
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
	public JRElement[] getElements()
	{
		if (this.elements == null)
		{
			if (this.children != null)
			{
				List allElements = new ArrayList();
				Object child = null;
				JRElement[] childElementArray = null;
				for(int i = 0; i < this.children.size(); i++)
				{
					child = this.children.get(i);
					if (child instanceof JRFillElement)
					{
						allElements.add(child);
					}
					else if (child instanceof JRFillElementGroup)
					{
						childElementArray = ((JRFillElementGroup)child).getElements();
						if (childElementArray != null)
						{
							allElements.addAll( Arrays.asList(childElementArray) );
						}
					}
				}
				
				this.elements = new JRFillElement[allElements.size()];
				allElements.toArray(this.elements);
			}
		}
		
		return this.elements;
	}


	/**
	 *
	 */
	public JRElement getElementByKey(String key)
	{
		return null;
	}


	/**
	 *
	 */
	protected void reset()
	{
		topElementInGroup = null;
	}


	/**
	 *
	 */
	protected int getStretchHeightDiff()
	{
		if (topElementInGroup == null)
		{
			stretchHeightDiff = 0;
			
			setTopBottomElements();

			JRElement[] elements = getElements();

			if (elements != null && elements.length > 0)
			{
				JRFillElement topElem = null;
				JRFillElement bottomElem = null;

				for(int i = 0; i < elements.length; i++)
				{
					JRFillElement element = (JRFillElement)elements[i];
					//if (element != this && element.isToPrint())
					if (element.isToPrint())
					{
						if (
							topElem == null ||
							(topElem != null &&
							element.getRelativeY() + element.getStretchHeight() <
							topElem.getRelativeY() + topElem.getStretchHeight())
							)
						{
							topElem = element;
						}

						if (
							bottomElem == null ||
							(bottomElem != null &&
							element.getRelativeY() + element.getStretchHeight() >
							bottomElem.getRelativeY() + bottomElem.getStretchHeight())
							)
						{
							bottomElem = element;
						}
					}
				}

				stretchHeightDiff = 
					bottomElem.getRelativeY() + bottomElem.getStretchHeight() - topElem.getRelativeY() -
					(bottomElementInGroup.getY() + bottomElementInGroup.getHeight() - topElementInGroup.getY());

				if (stretchHeightDiff < 0)
				{
					stretchHeightDiff = 0;
				}
			}
		}
		
		return stretchHeightDiff;
	}


	/**
	 *
	 */
	private void setTopBottomElements()
	{
		JRElement[] elements = getElements();
	
		if (elements != null && elements.length > 0)
		{
			for(int i = 0; i < elements.length; i++)
			{
				if (
					topElementInGroup == null ||
					(topElementInGroup != null &&
					elements[i].getY() + elements[i].getHeight() <
					topElementInGroup.getY() + topElementInGroup.getHeight())
					)
				{
					topElementInGroup = elements[i];
				}

				if (
					bottomElementInGroup == null ||
					(bottomElementInGroup != null &&
					elements[i].getY() + elements[i].getHeight() >
					bottomElementInGroup.getY() + bottomElementInGroup.getHeight())
					)
				{
					bottomElementInGroup = elements[i];
				}
			}
		}
	}


}
