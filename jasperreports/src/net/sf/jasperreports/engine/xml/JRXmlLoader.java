/*
 * ============================================================================
 *                   GNU Lesser General Public License
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

/*
 * Contributors:
 * Artur Biesiadowski - abies@users.sourceforge.net 
 */
package net.sf.jasperreports.engine.xml;

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

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRGroup;
import net.sf.jasperreports.engine.JRVariable;
import net.sf.jasperreports.engine.design.JRDesignElement;
import net.sf.jasperreports.engine.design.JRDesignImage;
import net.sf.jasperreports.engine.design.JRDesignTextField;
import net.sf.jasperreports.engine.design.JRDesignVariable;
import net.sf.jasperreports.engine.design.JasperDesign;


/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id$
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

	private Digester digester = null;

	
	/**
	 *
	 */
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

		try 
		{
			xmlLoader = new JRXmlLoader(JRXmlDigesterFactory.createDigester());
		}
		catch (ParserConfigurationException e) 
		{
			throw new JRException(e);
		}
		catch (SAXException e) 
		{
			throw new JRException(e);
		}
		
		jasperDesign = xmlLoader.loadXML(is);

		return jasperDesign;
	}


	/**
	 *
	 */
	public JasperDesign loadXML(InputStream is) throws JRException
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
		}
		finally 
		{
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
						throw 
							new JRException(
								"Unknown reset group '" + groupName 
								+ "' for variable : " + variable.getName()
								);
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

				if (variable.getIncrementType() == JRVariable.RESET_TYPE_GROUP)
				{
					groupName = null;
					group = variable.getIncrementGroup();
					if (group != null)
					{
						groupName = group.getName();
						group = (JRGroup)groupsMap.get(groupName);
					}

					if (group == null)
					{
						throw 
							new JRException(
								"Unknown increment group '" + groupName 
								+ "' for variable : " + variable.getName()
								);
					}
					else
					{
						variable.setIncrementGroup(group);
					}
				}
				else
				{
					variable.setIncrementGroup(null);
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
