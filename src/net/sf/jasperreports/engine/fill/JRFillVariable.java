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

import dori.jasper.engine.JRExpression;
import dori.jasper.engine.JRGroup;
import dori.jasper.engine.JRVariable;


/**
 *
 */
public class JRFillVariable implements JRVariable
{


	/**
	 *
	 */
	protected JRVariable parent = null;

	/**
	 *
	 */
	private JRGroup resetGroup = null;
	private JRVariable countVariable = null;
	private JRVariable sumVariable = null;
	private JRVariable varianceVariable = null;

	/**
	 *
	 */
	private Object oldValue = null;
	private Object estimatedValue = null;
	private Object value = null;
	private boolean isInitialized = false;


	/**
	 *
	 */
	protected JRFillVariable(
		JRVariable variable, 
		JRFillObjectFactory factory
		)
	{
		factory.put(variable, this);

		parent = variable;
		
		resetGroup = (JRGroup)factory.getGroup(variable.getResetGroup());
		countVariable = (JRVariable)factory.getVariable(variable.getCountVariable());
		sumVariable = (JRVariable)factory.getVariable(variable.getSumVariable());
		varianceVariable = (JRVariable)factory.getVariable(variable.getVarianceVariable());
	}


	/**
	 *
	 */
	public String getName()
	{
		return this.parent.getName();
	}
		
	/**
	 *
	 */
	public Class getValueClass()
	{
		return this.parent.getValueClass();
	}
		
	/**
	 *
	 */
	public JRExpression getExpression()
	{
		return this.parent.getExpression();
	}
		
	/**
	 *
	 */
	public JRExpression getInitialValueExpression()
	{
		return this.parent.getInitialValueExpression();
	}
		
	/**
	 *
	 */
	public byte getResetType()
	{
		return this.parent.getResetType();
	}
		
	/**
	 *
	 */
	public byte getCalculation()
	{
		return this.parent.getCalculation();
	}
		
	/**
	 *
	 */
	public boolean isSystemDefined()
	{
		return this.parent.isSystemDefined();
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
	
	/**
	 *
	 */
	public Object getOldValue()
	{
		return this.oldValue;
	}
		
	/**
	 *
	 */
	public void setOldValue(Object oldValue)
	{
		this.oldValue = oldValue;
	}

	/**
	 *
	 */
	public Object getEstimatedValue()
	{
		return this.estimatedValue;
	}
		
	/**
	 *
	 */
	public void setEstimatedValue(Object estimatedValue)
	{
		this.estimatedValue = estimatedValue;
	}

	/**
	 *
	 */
	public Object getValue()
	{
		return this.value;
	}
		
	/**
	 *
	 */
	public void setValue(Object value)
	{
		this.value = value;
	}

	/**
	 *
	 */
	public boolean isInitialized()
	{
		return this.isInitialized;
	}
		
	/**
	 *
	 */
	public void setInitialized(boolean isInitialized)
	{
		this.isInitialized = isInitialized;
	}


}
