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
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.xml.parsers.ParserConfigurationException;

import net.sf.jasperreports.engine.JRDatasetRun;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRGroup;
import net.sf.jasperreports.engine.JRVariable;
import net.sf.jasperreports.engine.design.JRDesignChart;
import net.sf.jasperreports.engine.design.JRDesignChartDataset;
import net.sf.jasperreports.engine.design.JRDesignDataset;
import net.sf.jasperreports.engine.design.JRDesignElement;
import net.sf.jasperreports.engine.design.JRDesignImage;
import net.sf.jasperreports.engine.design.JRDesignTextField;
import net.sf.jasperreports.engine.design.JRDesignVariable;
import net.sf.jasperreports.engine.design.JasperDesign;

import org.apache.commons.digester.Digester;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;


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
	private Collection groupEvaluatedCharts = new ArrayList();
	private Set groupBoundDatasets = new HashSet();
	private List errors = new ArrayList();

	private Digester digester = null;

	private boolean ignoreConsistencyProblems;
		
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
	public Collection getGroupEvaluatedCharts()
	{
		return groupEvaluatedCharts;
	}

	/**
	*
	*/
	public Set getGroupBoundDatasets()
	{
		return groupBoundDatasets;
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
		return loadXML(new InputSource(is));
	}

	/**
	 *
	 */
	public JasperDesign loadXML(InputSource is) throws JRException
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
			throw new JRException(e);
		}

		/*   */
		assignGroupsToVariables(jasperDesign.getMainDesignDataset());
		for (Iterator it = jasperDesign.getDatasetsList().iterator(); it.hasNext();)
		{
			JRDesignDataset dataset = (JRDesignDataset) it.next();
			assignGroupsToVariables(dataset);
		}
		
		this.assignGroupsToElements();
		this.assignGroupsToImages();
		this.assignGroupsToTextFields();
		this.assignGroupsToCharts();
		this.assignGroupsToDatasets();
		
		return this.jasperDesign;
	}

	/**
	 *
	 */
	private void assignGroupsToVariables(JRDesignDataset dataset) throws JRException
	{
		JRVariable[] variables = dataset.getVariables();
		if (variables != null && variables.length > 0)
		{
			Map groupsMap = dataset.getGroupsMap();
			for(int i = 0; i < variables.length; i++)
			{
				JRDesignVariable variable = (JRDesignVariable)variables[i];
				if (variable.getResetType() == JRVariable.RESET_TYPE_GROUP)
				{
					String groupName = null;
					JRGroup group = variable.getResetGroup();
					if (group != null)
					{
						groupName = group.getName();
						group = (JRGroup)groupsMap.get(groupName);
					}

					if (!ignoreConsistencyProblems && group == null)
					{
						throw 
							new JRException(
								"Unknown reset group '" + groupName 
								+ "' for variable : " + variable.getName()
								);
					}

					variable.setResetGroup(group);
				}
				else
				{
					variable.setResetGroup(null);
				}

				if (variable.getIncrementType() == JRVariable.RESET_TYPE_GROUP)
				{
					String groupName = null;
					JRGroup group = variable.getIncrementGroup();
					if (group != null)
					{
						groupName = group.getName();
						group = (JRGroup)groupsMap.get(groupName);
					}

					if (!ignoreConsistencyProblems && group == null)
					{
						throw 
							new JRException(
								"Unknown increment group '" + groupName 
								+ "' for variable : " + variable.getName()
								);
					}

					variable.setIncrementGroup(group);
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

			if (!ignoreConsistencyProblems && group == null)
			{
				throw new JRException("Unknown reprint group '" + groupName + "' for element.");
			}

			element.setPrintWhenGroupChanges(group);
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

			if (!ignoreConsistencyProblems && group == null)
			{
				throw new JRException("Unknown evaluation group '" + groupName + "' for image.");
			}

			image.setEvaluationGroup(group);
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

			if (!ignoreConsistencyProblems && group == null)
			{
				throw new JRException("Unknown evaluation group '" + groupName + "' for text field.");
			}

			textField.setEvaluationGroup(group);
		}
	}


	/**
	 *
	 */
	private void assignGroupsToCharts() throws JRException
	{
		Map groupsMap = jasperDesign.getGroupsMap();
		for(Iterator it = groupEvaluatedCharts.iterator(); it.hasNext();)
		{
			JRDesignChart chart = (JRDesignChart)it.next();

			String groupName = null;
			JRGroup group = chart.getEvaluationGroup();
			if (group != null)
			{
				groupName = group.getName();
				group = (JRGroup)groupsMap.get(group.getName());
			}

			if (!ignoreConsistencyProblems && group == null)
			{
				throw new JRException("Unknown evaluation group '" + groupName + "' for chart.");
			}

			chart.setEvaluationGroup(group);
		}
	}


	/**
	 *
	 */
	private void assignGroupsToDatasets() throws JRException
	{
		for(Iterator it = groupBoundDatasets.iterator(); it.hasNext();)
		{
			JRDesignChartDataset dataset = (JRDesignChartDataset)it.next();
			
			JRDatasetRun datasetRun = dataset.getDatasetRun();
			Map groupsMap;
			if (datasetRun == null)
			{
				groupsMap = jasperDesign.getGroupsMap();
			}
			else
			{
				Map datasetMap = jasperDesign.getDatasetMap();
				String datasetName = datasetRun.getDatasetName();
				JRDesignDataset subDataset = (JRDesignDataset) datasetMap.get(datasetName);
				if (subDataset == null)
				{
					throw new JRException("Unknown sub dataset '" + datasetName + "' for chart dataset.");
				}
				groupsMap = subDataset.getGroupsMap();
			}

			if (dataset.getIncrementType() == JRVariable.RESET_TYPE_GROUP)
			{
				String groupName = null;
				JRGroup group = dataset.getIncrementGroup();
				if (group != null)
				{
					groupName = group.getName();
					group = (JRGroup)groupsMap.get(group.getName());
				}

				if (!ignoreConsistencyProblems && group == null)
				{
					throw new JRException("Unknown increment group '" + groupName + "' for chart dataset.");
				}

				dataset.setIncrementGroup(group);
			}
			else
			{
				dataset.setIncrementGroup(null);
			}

			if (dataset.getResetType() == JRVariable.RESET_TYPE_GROUP)
			{
				String groupName = null;
				JRGroup group = dataset.getResetGroup();
				if (group != null)
				{
					groupName = group.getName();
					group = (JRGroup)groupsMap.get(group.getName());
				}

				if (!ignoreConsistencyProblems && group == null)
				{
					throw new JRException("Unknown reset group '" + groupName + "' for chart dataset.");
				}

				dataset.setResetGroup(group);
			}
			else
			{
				dataset.setResetGroup(null);
			}
		}
	}

	
	/**
	 *
	 */
	public void addError(Exception e)
	{
		if(!ignoreConsistencyProblems)
			this.errors.add(e);
	}
	
	/**
	 * Returns true if the loader is set to ignore consistency problems
	 * @return the ignoreConsistencyProblems flag.
	 */
	public boolean isIgnoreConsistencyProblems() {
		return ignoreConsistencyProblems;
	}
	
	/**
	 * Allows to enable or disable the reporting of consistency problems. Consistency 
	 * problems are problems in the logical structure of the report such as references
	 * to missing groups and fonts.
	 * 
	 * @param ignoreConsistencyProblems The ignoreConsistencyProblems value to set.
	 */
	public void setIgnoreConsistencyProblems(boolean ignoreConsistencyProblems) {
		this.ignoreConsistencyProblems = ignoreConsistencyProblems;
	}

}
