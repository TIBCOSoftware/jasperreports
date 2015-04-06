/*
 * JasperReports - Free Java Reporting Library.
 * Copyright (C) 2001 - 2014 TIBCO Software Inc. All rights reserved.
 * http://www.jaspersoft.com
 *
 * Unless you have purchased a commercial license agreement from Jaspersoft,
 * the following license terms apply:
 *
 * This program is part of JasperReports.
 *
 * JasperReports is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * JasperReports is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with JasperReports. If not, see <http://www.gnu.org/licenses/>.
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
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.xml.parsers.ParserConfigurationException;

import net.sf.jasperreports.engine.DefaultJasperReportsContext;
import net.sf.jasperreports.engine.JRDataset;
import net.sf.jasperreports.engine.JRDatasetRun;
import net.sf.jasperreports.engine.JRElementDataset;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRGroup;
import net.sf.jasperreports.engine.JRRuntimeException;
import net.sf.jasperreports.engine.JRVariable;
import net.sf.jasperreports.engine.JasperReportsContext;
import net.sf.jasperreports.engine.design.JRDesignChart;
import net.sf.jasperreports.engine.design.JRDesignDataset;
import net.sf.jasperreports.engine.design.JRDesignElement;
import net.sf.jasperreports.engine.design.JRDesignElementDataset;
import net.sf.jasperreports.engine.design.JRDesignImage;
import net.sf.jasperreports.engine.design.JRDesignTextField;
import net.sf.jasperreports.engine.design.JRDesignVariable;
import net.sf.jasperreports.engine.design.JRValidationException;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.type.IncrementTypeEnum;
import net.sf.jasperreports.engine.type.ResetTypeEnum;

import org.apache.commons.digester.Digester;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;


/**
 * Utility class that helps parsing a JRXML file into a 
 * {@link net.sf.jasperreports.engine.design.JasperDesign} object.
 * <p>
 * This can be done using one of the <code>load(...)</code> or <code>loadXml</code> 
 * methods published by this class. Applications might need to do this in cases where report
 * templates kept in their source form (JRXML) must be modified at runtime based on
 * some user input and then compiled on the fly for filling with data.
 * </p>
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 */
public class JRXmlLoader
{
	public static final String EXCEPTION_MESSAGE_KEY_UNKNOWN_SUBDATASET = "xml.loader.unknown.subdataset";
	public static final String EXCEPTION_MESSAGE_KEY_SUBDATASET_NOT_FOUND = "xml.loader.subdataset.not.found";

	/**
	 *
	 */
	private final JasperReportsContext jasperReportsContext;
	private JasperDesign jasperDesign;
	private LinkedList<XmlLoaderReportContext> contextStack = 
		new LinkedList<XmlLoaderReportContext>();
	
	private Map<XmlGroupReference, XmlLoaderReportContext> groupReferences = 
		new HashMap<XmlGroupReference, XmlLoaderReportContext>();
	
	//TODO use XmlGroupReference for datasets
	private Set<JRElementDataset> groupBoundDatasets = new HashSet<JRElementDataset>();
	
	private List<Exception> errors = new ArrayList<Exception>();

	private Digester digester;

	private boolean ignoreConsistencyProblems;
		
	/**
	 * @deprecated Replaced by {@link #JRXmlLoader(JasperReportsContext, Digester)}.
	 */
	public JRXmlLoader(Digester digester)
	{
		this(DefaultJasperReportsContext.getInstance(), digester);
	}

	/**
	 *
	 */
	public JRXmlLoader(JasperReportsContext jasperReportsContext, Digester digester)
	{
		this.jasperReportsContext = jasperReportsContext;
		this.digester = digester;
	}

	/**
	 *
	 */
	public JasperReportsContext getJasperReportsContext()
	{
		return jasperReportsContext;
	}

	/**
	 *
	 */
	public void setJasperDesign(JasperDesign jasperDesign)
	{
		this.jasperDesign = jasperDesign;
	}
	
	public void addGroupReference(XmlGroupReference reference)
	{
		XmlLoaderReportContext reportContext = getReportContext();
		groupReferences.put(reference, reportContext);
	}
	
	public void addGroupReprintedElement(JRDesignElement element)
	{
		addGroupReference(
				new ElementReprintGroupReference(element));
	}
	
	public void addGroupEvaluatedImage(JRDesignImage image)
	{
		addGroupReference(
				new ImageEvaluationGroupReference(image));
	}
	
	public void addGroupEvaluatedTextField(JRDesignTextField textField)
	{
		addGroupReference(
				new TextFieldEvaluationGroupReference(textField));
	}
	
	public void addGroupEvaluatedChart(JRDesignChart chart)
	{
		addGroupReference(
				new ChartEvaluationGroupReference(chart));
	}

	/**
	*
	*/
	public Set<JRElementDataset> getGroupBoundDatasets()
	{
		return groupBoundDatasets;
	}


	/**
	 * @see #load(JasperReportsContext, String)
	 */
	public static JasperDesign load(String sourceFileName) throws JRException//FIXMEREPO consider renaming
	{
		return load(DefaultJasperReportsContext.getInstance(),  sourceFileName);
	}


	/**
	 *
	 */
	public static JasperDesign load(JasperReportsContext jasperReportsContext, String sourceFileName) throws JRException//FIXMEREPO consider renaming
	{
		return load(jasperReportsContext, new File(sourceFileName));
	}


	/**
	 * @see #load(JasperReportsContext, File)
	 */
	public static JasperDesign load(File file) throws JRException
	{
		return load(DefaultJasperReportsContext.getInstance(), file);
	}


	/**
	 *
	 */
	public static JasperDesign load(JasperReportsContext jasperReportsContext, File file) throws JRException
	{
		JasperDesign jasperDesign = null;

		FileInputStream fis = null;

		try
		{
			fis = new FileInputStream(file);
			jasperDesign = JRXmlLoader.load(jasperReportsContext, fis);
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
	 * @see #load(JasperReportsContext, InputStream)
	 */
	public static JasperDesign load(InputStream is) throws JRException
	{
		return load(DefaultJasperReportsContext.getInstance(), is);
	}


	/**
	 *
	 */
	public static JasperDesign load(JasperReportsContext jasperReportsContext, InputStream is) throws JRException
	{
		JasperDesign jasperDesign = null;

		JRXmlLoader xmlLoader = null;

		try 
		{
			xmlLoader = new JRXmlLoader(jasperReportsContext, JRXmlDigesterFactory.createDigester(jasperReportsContext));
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
			Exception e = errors.get(0);
			if (e instanceof JRException)
			{
				throw (JRException)e;
			}
			throw new JRException(e);
		}

		/*   */
		assignGroupsToVariables(jasperDesign.getMainDesignDataset());
		for (Iterator<JRDataset> it = jasperDesign.getDatasetsList().iterator(); it.hasNext();)
		{
			JRDesignDataset dataset = (JRDesignDataset) it.next();
			assignGroupsToVariables(dataset);
		}
		
		assignGroupReferences();
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
			Map<String,JRGroup> groupsMap = dataset.getGroupsMap();
			for(int i = 0; i < variables.length; i++)
			{
				JRDesignVariable variable = (JRDesignVariable)variables[i];
				if (variable.getResetTypeValue() == ResetTypeEnum.GROUP)
				{
					String groupName = null;
					JRGroup group = variable.getResetGroup();
					if (group != null)
					{
						groupName = group.getName();
						group = groupsMap.get(groupName);
					}

					if (!ignoreConsistencyProblems && group == null)
					{
						throw 
							new JRValidationException(
								"Unknown reset group '" + groupName 
								+ "' for variable : " + variable.getName(),
								variable
								);
					}

					variable.setResetGroup(group);
				}
				else
				{
					variable.setResetGroup(null);
				}

				if (variable.getIncrementTypeValue() == IncrementTypeEnum.GROUP)
				{
					String groupName = null;
					JRGroup group = variable.getIncrementGroup();
					if (group != null)
					{
						groupName = group.getName();
						group = groupsMap.get(groupName);
					}

					if (!ignoreConsistencyProblems && group == null)
					{
						throw 
							new JRValidationException(
								"Unknown increment group '" + groupName 
								+ "' for variable : " + variable.getName(),
								variable
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
	private void assignGroupReferences() throws JRException
	{
		for (Map.Entry<XmlGroupReference, XmlLoaderReportContext> entry : 
			groupReferences.entrySet())
		{
			XmlGroupReference reference = entry.getKey();
			XmlLoaderReportContext context = entry.getValue();

			String groupName = null;
			JRGroup group = reference.getGroupReference();
			if (group != null)
			{
				groupName = group.getName();
				group = resolveGroup(groupName, context);
			}

			if (!ignoreConsistencyProblems && group == null)
			{
				reference.groupNotFound(groupName);
			}
			else
			{
				reference.assignGroup(group);
			}
		}
	}
	
	protected JRGroup resolveGroup(String groupName, XmlLoaderReportContext context)
	{
		JRGroup group;
		if (context == null)
		{
			// main dataset groups
			Map<String,JRGroup> groupsMap = jasperDesign.getGroupsMap();
			group = groupsMap.get(groupName);
		}
		else
		{
			String datasetName = context.getSubdatesetName();
			JRDesignDataset dataset = (JRDesignDataset) jasperDesign.getDatasetMap().get(datasetName);
			if (dataset == null)
			{
				throw 
					new JRRuntimeException(
						EXCEPTION_MESSAGE_KEY_SUBDATASET_NOT_FOUND,
						new Object[]{datasetName});
			}
			
			group = dataset.getGroupsMap().get(groupName);
		}
		return group;
	}


	/**
	 *
	 */
	private void assignGroupsToDatasets() throws JRException
	{
		for(Iterator<JRElementDataset> it = groupBoundDatasets.iterator(); it.hasNext();)
		{
			JRDesignElementDataset dataset = (JRDesignElementDataset) it.next();
			
			JRDatasetRun datasetRun = dataset.getDatasetRun();
			Map<String,JRGroup> groupsMap;
			if (datasetRun == null)
			{
				groupsMap = jasperDesign.getGroupsMap();
			}
			else
			{
				Map<String,JRDataset> datasetMap = jasperDesign.getDatasetMap();
				String datasetName = datasetRun.getDatasetName();
				JRDesignDataset subDataset = (JRDesignDataset) datasetMap.get(datasetName);
				if (subDataset == null)
				{
					throw 
						new JRException(
							EXCEPTION_MESSAGE_KEY_UNKNOWN_SUBDATASET,
							new Object[]{datasetName});
				}
				groupsMap = subDataset.getGroupsMap();
			}

			if (dataset.getIncrementTypeValue() == IncrementTypeEnum.GROUP)
			{
				String groupName = null;
				JRGroup group = dataset.getIncrementGroup();
				if (group != null)
				{
					groupName = group.getName();
					group = groupsMap.get(group.getName());
				}

				if (!ignoreConsistencyProblems && group == null)
				{
					throw new JRValidationException("Unknown increment group '" + groupName + "' for chart dataset.", dataset);
				}

				dataset.setIncrementGroup(group);
			}
			else
			{
				dataset.setIncrementGroup(null);
			}

			if (dataset.getResetTypeValue() == ResetTypeEnum.GROUP)
			{
				String groupName = null;
				JRGroup group = dataset.getResetGroup();
				if (group != null)
				{
					groupName = group.getName();
					group = groupsMap.get(group.getName());
				}

				if (!ignoreConsistencyProblems && group == null)
				{
					throw new JRValidationException("Unknown reset group '" + groupName + "' for chart dataset.", dataset);
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
		{
			this.errors.add(e);
		}
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

	public void pushReportContext(XmlLoaderReportContext context)
	{
		contextStack.addFirst(context);
	}
	
	public XmlLoaderReportContext popReportContext()
	{
		return contextStack.removeFirst();
	}
	
	public XmlLoaderReportContext getReportContext()
	{
		return contextStack.isEmpty() ? null : contextStack.getFirst();
	}
}
