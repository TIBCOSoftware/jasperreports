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

/*
 * Contributors:
 * Artur Biesiadowski - abies@users.sourceforge.net 
 */
package dori.jasper.engine.xml;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.digester.Digester;
import org.xml.sax.SAXException;

import dori.jasper.engine.JRException;
import dori.jasper.engine.JRGroup;
import dori.jasper.engine.JRVariable;
import dori.jasper.engine.design.JRDesignElement;
import dori.jasper.engine.design.JRDesignImage;
import dori.jasper.engine.design.JRDesignTextField;
import dori.jasper.engine.design.JRDesignVariable;
import dori.jasper.engine.design.JasperDesign;


/**
 *
 */
public class JRXmlLoader
{

	/**
	 *
	 */
	private JasperDesign jasperDesign = null;
	private Collection groupReprintedElements = new ArrayList();
	private Collection groupEvaluatedImages = new ArrayList();
	private Collection groupEvaluatedTextFields = new ArrayList();
	private List errors = new ArrayList();
	private int printWhenExpressionsCount = 0;
	private int anchorNameExpressionsCount = 0;
	private int hyperlinkReferenceExpressionsCount = 0;
	private int hyperlinkAnchorExpressionsCount = 0;
	private int hyperlinkPageExpressionsCount = 0;
	private int imagesCount = 0;
	private int textFieldsCount = 0;
	private int subreportsCount = 0;
	private int subreportParametersCount = 0;

	private Digester digester;
	
	public JRXmlLoader(Digester digester)
	{
		this.digester = digester;
	}

	/**
	 *
	 */
	public void setJasperDesign(JasperDesign jasperDesign)
	{
		this.jasperDesign = jasperDesign;
	}

	/**
	 *
	 */
	public Collection getGroupReprintedElements()
	{
		return this.groupReprintedElements;
	}

	/**
	 *
	 */
	public Collection getGroupEvaluatedImages()
	{
		return this.groupEvaluatedImages;
	}

	/**
	 *
	 */
	public Collection getGroupEvaluatedTextFields()
	{
		return this.groupEvaluatedTextFields;
	}

	/**
	 *
	 */
	public int getPrintWhenExpressionsCount()
	{
		return this.printWhenExpressionsCount;
	}

	/**
	 *
	 */
	public void setPrintWhenExpressionsCount(int count)
	{
		this.printWhenExpressionsCount = count;
	}

	/**
	 *
	 */
	public int getAnchorNameExpressionsCount()
	{
		return this.anchorNameExpressionsCount;
	}

	/**
	 *
	 */
	public void setAnchorNameExpressionsCount(int count)
	{
		this.anchorNameExpressionsCount = count;
	}

	/**
	 *
	 */
	public int getHyperlinkReferenceExpressionsCount()
	{
		return this.hyperlinkReferenceExpressionsCount;
	}

	/**
	 *
	 */
	public void setHyperlinkReferenceExpressionsCount(int count)
	{
		this.hyperlinkReferenceExpressionsCount = count;
	}

	/**
	 *
	 */
	public int getHyperlinkAnchorExpressionsCount()
	{
		return this.hyperlinkAnchorExpressionsCount;
	}

	/**
	 *
	 */
	public void setHyperlinkAnchorExpressionsCount(int count)
	{
		this.hyperlinkAnchorExpressionsCount = count;
	}

	/**
	 *
	 */
	public int getHyperlinkPageExpressionsCount()
	{
		return this.hyperlinkPageExpressionsCount;
	}

	/**
	 *
	 */
	public void setHyperlinkPageExpressionsCount(int count)
	{
		this.hyperlinkPageExpressionsCount = count;
	}

	/**
	 *
	 */
	public int getImagesCount()
	{
		return this.imagesCount;
	}

	/**
	 *
	 */
	public void setImagesCount(int count)
	{
		this.imagesCount = count;
	}

	/**
	 *
	 */
	public int getTextFieldsCount()
	{
		return this.textFieldsCount;
	}

	/**
	 *
	 */
	public void setTextFieldsCount(int count)
	{
		this.textFieldsCount = count;
	}

	/**
	 *
	 */
	public int getSubreportsCount()
	{
		return this.subreportsCount;
	}

	/**
	 *
	 */
	public void setSubreportsCount(int count)
	{
		this.subreportsCount = count;
	}

	/**
	 *
	 */
	public int getSubreportParametersCount()
	{
		return this.subreportParametersCount;
	}

	/**
	 *
	 */
	public void setSubreportParametersCount(int count)
	{
		this.subreportParametersCount = count;
	}


	/**
	 *
	 */
	public static JasperDesign load(String sourceFileName) throws JRException
	{
		return load(new File(sourceFileName));
	}


	/**
	 *
	 */
	public static JasperDesign load(File file) throws JRException
	{
		JasperDesign jasperDesign = null;

		FileInputStream fis = null;

		try
		{
			fis = new FileInputStream(file);
			jasperDesign = JRXmlLoader.load(fis);
		}
		catch(IOException e)
		{
			throw new JRException(e);
		}
		finally
		{
			if (fis != null)
			{
				try
				{
					fis.close();
				}
				catch(IOException e)
				{
				}
			}
		}

		return jasperDesign;
	}


	/**
	 *
	 */
	public static JasperDesign load(InputStream is) throws JRException
	{
		JasperDesign jasperDesign = null;

		JRXmlLoader xmlLoader = null;
		try {
			xmlLoader = new JRXmlLoader(JRXmlDigesterFactory.createDigester());
		} catch (ParserConfigurationException e) {
			throw new JRException(e);
		} catch (SAXException e) {
			throw new JRException(e);
		}
		
		jasperDesign = xmlLoader.loadXML(is);

		return jasperDesign;
	}


	/**
	 *
	 */
	private JasperDesign loadXML(InputStream is) throws JRException
	{
		try
		{
			digester.push(this);	
			/*   */
			digester.parse(is);
		}
		catch(SAXException e)
		{
			throw new JRException(e);
		}
		catch(IOException e)
		{
			throw new JRException(e);
		} finally {
			digester.clear();
		}
		
		if (errors.size() > 0)
		{
			Exception e = (Exception)errors.get(0);
			if (e instanceof JRException)
			{
				throw (JRException)e;
			}
			else
			{
				throw new JRException(e);
			}
		}

		/*   */
		this.assignGroupsToVariables();
		this.assignGroupsToElements();
		this.assignGroupsToImages();
		this.assignGroupsToTextFields();
		
		return this.jasperDesign;
	}

	/**
	 *
	 */
	private void assignGroupsToVariables() throws JRException
	{
		JRVariable[] variables = jasperDesign.getVariables();
		if (variables != null && variables.length > 0)
		{
			Map groupsMap = jasperDesign.getGroupsMap();
			JRDesignVariable variable = null;
			String groupName = null;
			JRGroup group = null;
			for(int i = 0; i < variables.length; i++)
			{
				variable = (JRDesignVariable)variables[i];
				if (variable.getResetType() == JRVariable.RESET_TYPE_GROUP)
				{
					groupName = null;
					group = variable.getResetGroup();
					if (group != null)
					{
						groupName = group.getName();
						group = (JRGroup)groupsMap.get(groupName);
					}

					if (group == null)
					{
						throw new JRException("Unknown reset group '" + groupName + "' for variable : " + variable.getName());
					}
					else
					{
						variable.setResetGroup(group);
					}
				}
				else
				{
					variable.setResetGroup(null);
				}
			}
		}
	}


	/**
	 *
	 */
	private void assignGroupsToElements() throws JRException
	{
		Map groupsMap = jasperDesign.getGroupsMap();
		JRDesignElement element = null;
		String groupName = null;
		JRGroup group = null;
		for(Iterator it = groupReprintedElements.iterator(); it.hasNext();)
		{
			element = (JRDesignElement)it.next();

			groupName = null;
			group = element.getPrintWhenGroupChanges();
			if (group != null)
			{
				groupName = group.getName();
				group = (JRGroup)groupsMap.get(group.getName());
			}

			if (group == null)
			{
				throw new JRException("Unknown reprint group '" + groupName + "' for element.");
			}
			else
			{
				element.setPrintWhenGroupChanges(group);
			}
		}
	}


	/**
	 *
	 */
	private void assignGroupsToImages() throws JRException
	{
		Map groupsMap = jasperDesign.getGroupsMap();
		JRDesignImage image = null;
		String groupName = null;
		JRGroup group = null;
		for(Iterator it = groupEvaluatedImages.iterator(); it.hasNext();)
		{
			image = (JRDesignImage)it.next();

			groupName = null;
			group = image.getEvaluationGroup();
			if (group != null)
			{
				groupName = group.getName();
				group = (JRGroup)groupsMap.get(group.getName());
			}

			if (group == null)
			{
				throw new JRException("Unknown evaluation group '" + groupName + "' for image.");
			}
			else
			{
				image.setEvaluationGroup(group);
			}
		}
	}


	/**
	 *
	 */
	private void assignGroupsToTextFields() throws JRException
	{
		Map groupsMap = jasperDesign.getGroupsMap();
		JRDesignTextField textField = null;
		String groupName = null;
		JRGroup group = null;
		for(Iterator it = groupEvaluatedTextFields.iterator(); it.hasNext();)
		{
			textField = (JRDesignTextField)it.next();

			groupName = null;
			group = textField.getEvaluationGroup();
			if (group != null)
			{
				groupName = group.getName();
				group = (JRGroup)groupsMap.get(group.getName());
			}

			if (group == null)
			{
				throw new JRException("Unknown evaluation group '" + groupName + "' for text field.");
			}
			else
			{
				textField.setEvaluationGroup(group);
			}
		}
	}


	/**
	 *
	 */
	public void addError(Exception e)
	{
		this.errors.add(e);
	}

}
