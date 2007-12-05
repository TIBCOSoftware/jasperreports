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
package net.sf.jasperreports.engine.design;

import net.sf.jasperreports.engine.JRBand;
import net.sf.jasperreports.engine.JRConstants;
import net.sf.jasperreports.engine.JRExpression;
import net.sf.jasperreports.engine.JROrigin;
import net.sf.jasperreports.engine.base.JRBaseBand;


/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id$
 */
public class JRDesignBand extends JRDesignElementGroup implements JRBand
{
	

	/**
	 *
	 */
	private static final long serialVersionUID = JRConstants.SERIAL_VERSION_UID;

	public static final String PROPERTY_HEIGHT = "height";

	public static final String PROPERTY_PRINT_WHEN_EXPRESSION = "printWhenExpression";

	/**
	 *
	 */
	protected int height = 0;
	protected boolean isSplitAllowed = true;

	/**
	 *
	 */
	protected JRExpression printWhenExpression = null;
	
	private JROrigin origin;

	/**
	 *
	 */
	public int getHeight()
	{
		return this.height;
	}

	/**
	 *
	 */
	public void setHeight(int height)
	{
		int old = this.height;
		this.height = height;
		getEventSupport().firePropertyChange(PROPERTY_HEIGHT, old, this.height);
	}

	/**
	 *
	 */
	public boolean isSplitAllowed()
	{
		return this.isSplitAllowed;
	}

	/**
	 *
	 */
	public void setSplitAllowed(boolean isSplitAllowed)
	{
		boolean old = this.isSplitAllowed;
		this.isSplitAllowed = isSplitAllowed;
		getEventSupport().firePropertyChange(JRBaseBand.PROPERTY_SPLIT_ALLOWED, old, this.isSplitAllowed);
	}

	/**
	 *
	 */
	public JRExpression getPrintWhenExpression()
	{
		return this.printWhenExpression;
	}
	
	/**
	 *
	 */
	public void setPrintWhenExpression(JRExpression expression)
	{
		Object old = this.printWhenExpression;
		this.printWhenExpression = expression;
		getEventSupport().firePropertyChange(PROPERTY_PRINT_WHEN_EXPRESSION, old, this.printWhenExpression);
	}

	/**
	 * Returns the band origin, i.e. its location/role within the report
	 * (e.g. detail/title/group header/etc).
	 * The location is automatically set when the band is inserted
	 * into the report (via one of the
	 * {@link JasperDesign#setDetail(JRBand) setDetail}/
	 * {@link JasperDesign#setTitle(JRBand) setTitle}/
	 * {@link JRDesignGroup#setGroupHeader(JRBand) setGroupHeader}
	 * methods).
	 * 
	 * @return the band origin
	 */
	public JROrigin getOrigin()
	{
		return origin;
	}

	void setOrigin(JROrigin origin)
	{
		this.origin = origin;
	}
	

	/**
	 *
	 */
	public Object clone() 
	{
		JRDesignBand clone = (JRDesignBand)super.clone();
		if (printWhenExpression != null)
		{
			clone.printWhenExpression = (JRExpression)printWhenExpression.clone();
		}
		if (origin != null)
		{
			clone.origin = (JROrigin)origin.clone();
		}
		return clone;
	}

	
}
