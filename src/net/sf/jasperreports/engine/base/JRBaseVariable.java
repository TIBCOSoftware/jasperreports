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
package net.sf.jasperreports.engine.base;

import java.io.Serializable;

import net.sf.jasperreports.engine.JRExpression;
import net.sf.jasperreports.engine.JRGroup;
import net.sf.jasperreports.engine.JRRuntimeException;
import net.sf.jasperreports.engine.JRVariable;
import net.sf.jasperreports.engine.util.JRClassLoader;


/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id$
 */
public class JRBaseVariable implements JRVariable, Serializable
{


	/**
	 *
	 */
	private static final long serialVersionUID = 602;

	/**
	 *
	 */
	protected String name = null;
	protected String valueClassName = java.lang.String.class.getName();
	protected String incrementerFactoryClassName = null;
	protected byte resetType = RESET_TYPE_REPORT;
	protected byte calculation = CALCULATION_NOTHING;
	protected boolean isSystemDefined = false;

	protected transient Class valueClass = null;
	protected transient Class incrementerFactoryClass = null;

	/**
	 *
	 */
	protected JRExpression expression = null;
	protected JRExpression initialValueExpression = null;
	protected JRGroup resetGroup = null;
	protected JRVariable countVariable = null;
	protected JRVariable sumVariable = null;
	protected JRVariable varianceVariable = null;


	/**
	 *
	 */
	protected JRBaseVariable()
	{
	}
	
	
	/**
	 *
	 */
	protected JRBaseVariable(JRVariable variable, JRBaseObjectFactory factory)
	{
		factory.put(variable, this);
		
		name = variable.getName();
		valueClassName = variable.getValueClassName();
		incrementerFactoryClassName = variable.getIncrementerFactoryClassName();
		resetType = variable.getResetType();
		calculation = variable.getCalculation();
		isSystemDefined = variable.isSystemDefined();
		
		expression = factory.getExpression(variable.getExpression());
		initialValueExpression = factory.getExpression(variable.getInitialValueExpression());

		resetGroup = factory.getGroup(variable.getResetGroup());
		countVariable = factory.getVariable(variable.getCountVariable());
		sumVariable = factory.getVariable(variable.getSumVariable());
		varianceVariable = factory.getVariable(variable.getVarianceVariable());
	}
		

	/**
	 *
	 */
	public String getName()
	{
		return this.name;
	}
		
	/**
	 *
	 */
	public Class getValueClass()
	{
		if (valueClass == null)
		{
			if (valueClassName != null)
			{
				try
				{
					valueClass = JRClassLoader.loadClassForName(valueClassName);
				}
				catch(ClassNotFoundException e)
				{
					throw new JRRuntimeException(e);
				}
			}
		}
		
		return valueClass;
	}
		
	/**
	 *
	 */
	public String getValueClassName()
	{
		return valueClassName;
	}
		
	/**
	 *
	 */
	public Class getIncrementerFactoryClass()
	{
		if (incrementerFactoryClass == null)
		{
			if (incrementerFactoryClassName != null)
			{
				try
				{
					incrementerFactoryClass = JRClassLoader.loadClassForName(incrementerFactoryClassName);
				}
				catch(ClassNotFoundException e)
				{
					throw new JRRuntimeException(e);
				}
			}
		}
		
		return incrementerFactoryClass;
	}
		
	/**
	 *
	 */
	public String getIncrementerFactoryClassName()
	{
		return incrementerFactoryClassName;
	}
		
	/**
	 *
	 */
	public byte getResetType()
	{
		return this.resetType;
	}
		
	/**
	 *
	 */
	public byte getCalculation()
	{
		return this.calculation;
	}

	/**
	 *
	 */
	public boolean isSystemDefined()
	{
		return this.isSystemDefined;
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
	public JRExpression getInitialValueExpression()
	{
		return this.initialValueExpression;
	}
		
	/**
	 *
	 */
	public JRGroup getResetGroup()
	{
		return this.resetGroup;
	}
		
	/**
	 *
	 */
	public JRVariable getCountVariable()
	{
		return this.countVariable;
	}

	/**
	 *
	 */
	public JRVariable getSumVariable()
	{
		return this.sumVariable;
	}

	/**
	 *
	 */
	public JRVariable getVarianceVariable()
	{
		return this.varianceVariable;
	}

		
}
