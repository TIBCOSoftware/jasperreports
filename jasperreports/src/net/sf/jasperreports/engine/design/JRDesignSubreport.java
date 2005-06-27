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

import java.util.HashMap;
import java.util.Map;

import net.sf.jasperreports.engine.JRAbstractObjectFactory;
import net.sf.jasperreports.engine.JRElement;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExpression;
import net.sf.jasperreports.engine.JRExpressionCollector;
import net.sf.jasperreports.engine.JRSubreport;
import net.sf.jasperreports.engine.JRSubreportParameter;
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
	private static final long serialVersionUID = 608;

	/**
	 *
	 */
	protected boolean isUsingCache = true;

	/**
	 *
	 */
	protected Map parametersMap = new HashMap();

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
	public JRDesignSubreport()
	{
		super();
		
		this.mode = MODE_TRANSPARENT;
	}
		

	/**
	 *
	 */
	public boolean isUsingCache()
	{
		return this.isUsingCache;
	}

	/**
	 *
	 */
	public void setUsingCache(boolean isUsingCache)
	{
		this.isUsingCache = isUsingCache;
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
	public JRElement getCopy(JRAbstractObjectFactory factory)
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


}
