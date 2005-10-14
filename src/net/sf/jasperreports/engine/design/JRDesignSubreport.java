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
package net.sf.jasperreports.engine.design;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.jasperreports.engine.JRAbstractObjectFactory;
import net.sf.jasperreports.engine.JRChild;
import net.sf.jasperreports.engine.JRDefaultStyleProvider;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExpression;
import net.sf.jasperreports.engine.JRExpressionCollector;
import net.sf.jasperreports.engine.JRSubreport;
import net.sf.jasperreports.engine.JRSubreportParameter;
import net.sf.jasperreports.engine.JRSubreportReturnValue;
import net.sf.jasperreports.engine.util.JRStyleResolver;
import net.sf.jasperreports.engine.xml.JRXmlWriter;


/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id$
 */
public class JRDesignSubreport extends JRDesignElement implements JRSubreport
{


	/**
	 *
	 */
	private static final long serialVersionUID = 10003;

	/**
	 *
	 */
	protected Boolean isUsingCache = null;

	/**
	 *
	 */
	protected Map parametersMap = new HashMap();
	
	/**
	 * Values to be copied from the subreport into the master report.
	 */
	protected List returnValues = new ArrayList();

	/**
	 *
	 */
	protected JRExpression parametersMapExpression = null;
	protected JRExpression connectionExpression = null;
	protected JRExpression dataSourceExpression = null;
	protected JRExpression expression = null;


	/**
	 *
	 */
	public JRDesignSubreport(JRDefaultStyleProvider defaultStyleProvider)
	{
		super(defaultStyleProvider);
	}
		

	/**
	 *
	 */
	public byte getMode()
	{
		return JRStyleResolver.getMode(this, MODE_TRANSPARENT);
	}


	/**
	 *
	 */
	public boolean isUsingCache()
	{
		if (isUsingCache == null)
		{
			JRExpression subreportExpression = getExpression();
			if (subreportExpression != null)
			{
				return String.class.getName().equals(subreportExpression.getValueClassName());
			}
			return true;
		}
		return isUsingCache.booleanValue();
	}

	/**
	 * @deprecated
	 */
	public void setUsingCache(boolean isUsingCache)
	{
		setUsingCache(isUsingCache ? Boolean.TRUE : Boolean.FALSE);
	}

	/**
	 *
	 */
	public JRExpression getParametersMapExpression()
	{
		return this.parametersMapExpression;
	}

	/**
	 *
	 */
	public void setParametersMapExpression(JRExpression parametersMapExpression)
	{
		this.parametersMapExpression = parametersMapExpression;
	}

	/**
	 *
	 */
	public JRSubreportParameter[] getParameters()
	{
		JRSubreportParameter[] parametersArray = new JRSubreportParameter[parametersMap.size()];
		
		parametersMap.values().toArray(parametersArray);

		return parametersArray;
	}
	
	/**
	 *
	 */
	public Map getParametersMap()
	{
		return this.parametersMap;
	}
	
	/**
	 *
	 */
	public void addParameter(JRSubreportParameter subreportParameter) throws JRException
	{
		if (this.parametersMap.containsKey(subreportParameter.getName()))
		{
			throw new JRException("Duplicate declaration of subreport parameter : " + subreportParameter.getName());
		}

		this.parametersMap.put(subreportParameter.getName(), subreportParameter);
	}
	
	/**
	 *
	 */
	public JRSubreportParameter removeParameter(String name)
	{
		return (JRSubreportParameter)this.parametersMap.remove(name);
	}

	/**
	 *
	 */
	public JRExpression getConnectionExpression()
	{
		return this.connectionExpression;
	}

	/**
	 *
	 */
	public void setConnectionExpression(JRExpression connectionExpression)
	{
		this.connectionExpression = connectionExpression;
		this.dataSourceExpression = null;
	}

	/**
	 *
	 */
	public JRExpression getDataSourceExpression()
	{
		return this.dataSourceExpression;
	}

	/**
	 *
	 */
	public void setDataSourceExpression(JRExpression dataSourceExpression)
	{
		this.dataSourceExpression = dataSourceExpression;
	}

	/**
	 *
	 */
	public JRExpression getExpression()
	{
		return this.expression;
	}

	/**
	 *
	 */
	public void setExpression(JRExpression expression)
	{
		this.expression = expression;
	}
	
	/**
	 *
	 */
	public JRChild getCopy(JRAbstractObjectFactory factory)
	{
		return factory.getSubreport(this);
	}

	/**
	 *
	 */
	public void collectExpressions(JRExpressionCollector collector)
	{
		collector.collect(this);
	}

	/**
	 *
	 */
	public void writeXml(JRXmlWriter xmlWriter)
	{
		xmlWriter.writeSubreport(this);
	}

	
	/**
	 * Adds a return value to the subreport.
	 * 
	 * @param returnValue the return value to be added.
	 */
	public void addReturnValue(JRSubreportReturnValue returnValue)
	{
		this.returnValues.add(returnValue);
	}

	
	/**
	 * Returns the list of values to be copied from the subreport into the master.
	 * 
	 * @return the list of values to be copied from the subreport into the master.
	 */
	public JRSubreportReturnValue[] getReturnValues()
	{
		JRSubreportReturnValue[] returnValuesArray = new JRSubreportReturnValue[returnValues.size()];

		returnValues.toArray(returnValuesArray);

		return returnValuesArray;
	}

	
	/**
	 * Returns the list of values to be copied from the subreport into the master.
	 * 
	 * @return list of {@link JRSubreportReturnValue JRSubreportReturnValue} objects
	 */
	public List getReturnValuesList()
	{
		return returnValues;
	}

	
	/**
	 * Removes a return value from the subreport.
	 * 
	 * @param returnValue the return value to be removed
	 * @return <code>true</code> if the return value was found and removed 
	 */
	public boolean removeReturnValue(JRSubreportReturnValue returnValue)
	{
		return this.returnValues.remove(returnValue);
	}


	public Boolean isOwnUsingCache()
	{
		return isUsingCache;
	}


	public void setUsingCache(Boolean isUsingCache)
	{
		this.isUsingCache = isUsingCache;
	}
}
