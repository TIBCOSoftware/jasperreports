/*
 * ============================================================================
 * GNU Lesser General Public License
 * ============================================================================
 *
 * JasperReports - Free Java report-generating library.
 * Copyright (C) 2001-2009 JasperSoft Corporation http://www.jaspersoft.com
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
 * 539 Bryant Street, Suite 100
 * San Francisco, CA 94107
 * http://www.jaspersoft.com
 */
package net.sf.jasperreports.engine.fill;

import net.sf.jasperreports.engine.JRBand;
import net.sf.jasperreports.engine.JRGroup;
import net.sf.jasperreports.engine.JROrigin;
import net.sf.jasperreports.engine.JRSection;


/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id: JRFillBand.java 2696 2009-03-24 18:35:01Z teodord $
 */
public class JRFillSection implements JRSection, JROriginProvider
{

	/**
	 *
	 */
	protected JRBaseFiller filler;

	protected JRFillBand[] bands = null;

	protected JROrigin origin = null;

	private boolean isEmpty = true;
	private boolean areAllPrintWhenExprNull = true;

	
	/**
	 *
	 */
	protected JRFillSection(
		JRBaseFiller filler,
		JRSection section,
		JRFillObjectFactory factory
		)
	{
		if (section != null)
		{
			factory.put(section, this);

			isEmpty = true;
			areAllPrintWhenExprNull = true;
			
			JRBand[] jrBands = section.getBands();
			if (jrBands != null && jrBands.length > 0)
			{
				bands = new JRFillBand[jrBands.length];
				for (int i = 0; i < jrBands.length; i++)
				{
					bands[i] = factory.getBand(jrBands[i]);
					isEmpty = isEmpty && bands[i].isEmpty();
					areAllPrintWhenExprNull = areAllPrintWhenExprNull && bands[i].isPrintWhenExpressionNull();
				}
			}
		}

		this.filler = filler;
	}

	
	/**
	 *
	 */
	public JROrigin getOrigin()
	{
		return origin;
	}

	
	/**
	 *
	 */
	protected void setOrigin(JROrigin origin)
	{
		this.origin = origin;

		if (bands != null && bands.length > 0)
		{
			for (int i = 0; i < bands.length; i++)
			{
				bands[i].setOrigin(origin);
			}
		}
		
		this.filler.getJasperPrint().addOrigin(origin);//FIXMESECTION detail origin appears even if empty
	}


	/**
	 *
	 */
	public JRBand[] getBands() 
	{
		return bands;
	}


	/**
	 *
	 */
	public Object clone() 
	{
		throw new UnsupportedOperationException();
	}


	protected boolean isEmpty()
	{
		return isEmpty;
	}


	protected boolean areAllPrintWhenExpressionsNull()
	{
		return areAllPrintWhenExprNull;
	}


	protected void setNewPageColumn(boolean isNew)
	{
		if (bands != null)
		{
			for(int i = 0; i < bands.length; i++)
			{
				bands[i].setNewPageColumn(isNew);
			}
		}
	}


	protected void setNewGroup(JRGroup group, boolean isNew)
	{
		if (bands != null)
		{
			for(int i = 0; i < bands.length; i++)
			{
				bands[i].setNewGroup(group, isNew);
			}
		}
	}


	protected void addNowEvaluationTime(JREvaluationTime evaluationTime)
	{
		if (bands != null)
		{
			for(int i = 0; i < bands.length; i++)
			{
				bands[i].addNowEvaluationTime(evaluationTime);
			}
		}
	}

}
