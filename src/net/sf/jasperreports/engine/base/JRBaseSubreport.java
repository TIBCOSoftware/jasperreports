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

import net.sf.jasperreports.engine.JRAbstractObjectFactory;
import net.sf.jasperreports.engine.JRChild;
import net.sf.jasperreports.engine.JRExpression;
import net.sf.jasperreports.engine.JRExpressionCollector;
import net.sf.jasperreports.engine.JRSubreport;
import net.sf.jasperreports.engine.JRSubreportReturnValue;
import net.sf.jasperreports.engine.JRSubreportParameter;
import net.sf.jasperreports.engine.xml.JRXmlWriter;


/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id$
 */
public class JRBaseSubreport extends JRBaseElement implements JRSubreport
{


	/**
	 *
	 */
	private static final long serialVersionUID = 10000;

	/**
	 *
	 */
	protected Boolean isUsingCache = null;

	/**
	 *
	 */
	protected JRExpression parametersMapExpression = null;
	protected JRSubreportParameter[] parameters = null;
	protected JRExpression connectionExpression = null;
	protected JRExpression dataSourceExpression = null;
	protected JRExpression expression = null;
	
	/**
	 * Values to be copied from the subreport into the master report.
	 */
	protected JRSubreportReturnValue[] returnValues = null;


	/**
	 *
	 */
	protected JRBaseSubreport()
	{
		super();
		
		this.mode = MODE_TRANSPARENT;
	}
		

	/**
	 *
	 */
	protected JRBaseSubreport(JRSubreport subreport, JRBaseObjectFactory factory)
	{
		super(subreport, factory);
		
		isUsingCache = subreport.isOwnUsingCache();

		parametersMapExpression = factory.getExpression(subreport.getParametersMapExpression());

		/*   */
		JRSubreportParameter[] jrSubreportParameters = subreport.getParameters();
		if (jrSubreportParameters != null && jrSubreportParameters.length > 0)
		{
			parameters = new JRSubreportParameter[jrSubreportParameters.length];
			for(int i = 0; i < parameters.length; i++)
			{
				parameters[i] = factory.getSubreportParameter(jrSubreportParameters[i]);
			}
		}

		connectionExpression = factory.getExpression(subreport.getConnectionExpression());
		dataSourceExpression = factory.getExpression(subreport.getDataSourceExpression());
		
		JRSubreportReturnValue[] subrepReturnValues = subreport.getReturnValues();
		if (subrepReturnValues != null && subrepReturnValues.length > 0)
		{
			this.returnValues = new JRSubreportReturnValue[subrepReturnValues.length];
			for (int i = 0; i < subrepReturnValues.length; i++)
			{
				this.returnValues[i] = factory.getSubreportReturnValue(subrepReturnValues[i]);
			}
		}
		
		expression = factory.getExpression(subreport.getExpression());
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
	 *
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
	public JRSubreportParameter[] getParameters()
	{
		return this.parameters;
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
	public JRExpression getDataSourceExpression()
	{
		return this.dataSourceExpression;
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
	 * Returns the list of values to be copied from the subreport into the master.
	 * 
	 * @return the list of values to be copied from the subreport into the master.
	 */
	public JRSubreportReturnValue[] getReturnValues()
	{
		return this.returnValues;
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
