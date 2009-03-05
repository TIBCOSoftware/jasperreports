/*
 * ============================================================================
 * GNU Lesser General Public License
 * ============================================================================
 *
 * JasperReports - Free Java report-generating library.
 * Copyright (C) 2001-2006 JasperSoft Corporation http://www.jaspersoft.com
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
 * 303 Second Street, Suite 450 North
 * San Francisco, CA 94107
 * http://www.jaspersoft.com
 */
package net.sf.jasperreports.charts.base;

import java.io.Serializable;

import net.sf.jasperreports.charts.JRPieSeries;
import net.sf.jasperreports.engine.JRConstants;
import net.sf.jasperreports.engine.JRExpression;
import net.sf.jasperreports.engine.JRHyperlink;
import net.sf.jasperreports.engine.JRRuntimeException;
import net.sf.jasperreports.engine.base.JRBaseObjectFactory;


/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id: JRBaseCategorySeries.java 1994 2007-12-05 13:48:10Z teodord $
 */
public class JRBasePieSeries implements JRPieSeries, Serializable
{


	/**
	 *
	 */
	private static final long serialVersionUID = JRConstants.SERIAL_VERSION_UID;

	protected JRExpression keyExpression = null;
	protected JRExpression valueExpression = null;
	protected JRExpression labelExpression = null;
	protected JRHyperlink sectionHyperlink;

	
	/**
	 *
	 */
	protected JRBasePieSeries()
	{
	}
	
	
	/**
	 *
	 */
	public JRBasePieSeries(JRPieSeries pieSeries, JRBaseObjectFactory factory)
	{
		factory.put(pieSeries, this);

		keyExpression = factory.getExpression(pieSeries.getKeyExpression());
		valueExpression = factory.getExpression(pieSeries.getValueExpression());
		labelExpression = factory.getExpression(pieSeries.getLabelExpression());
		sectionHyperlink = factory.getHyperlink(pieSeries.getSectionHyperlink());
	}

	
	/**
	 *
	 */
	public JRExpression getKeyExpression()
	{
		return keyExpression;
	}
		
	/**
	 *
	 */
	public JRExpression getValueExpression()
	{
		return valueExpression;
	}
		
	/**
	 *
	 */
	public JRExpression getLabelExpression()
	{
		return labelExpression;
	}

	
	public JRHyperlink getSectionHyperlink()
	{
		return sectionHyperlink;
	}
		
	/**
	 * 
	 */
	public Object clone() 
	{
		JRBasePieSeries clone = null;
		
		try
		{
			clone = (JRBasePieSeries)super.clone();
		}
		catch (CloneNotSupportedException e)
		{
			throw new JRRuntimeException(e);
		}
		
		if (keyExpression != null)
		{
			clone.keyExpression = (JRExpression)keyExpression.clone();
		}
		if (valueExpression != null)
		{
			clone.valueExpression = (JRExpression)valueExpression.clone();
		}
		if (labelExpression != null)
		{
			clone.labelExpression = (JRExpression)labelExpression.clone();
		}
		if (sectionHyperlink != null)
		{
			clone.sectionHyperlink = (JRHyperlink)sectionHyperlink.clone();
		}
		
		return clone;
	}
}
