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
package net.sf.jasperreports.engine.base;

import net.sf.jasperreports.engine.JRBand;
import net.sf.jasperreports.engine.JRConstants;
import net.sf.jasperreports.engine.JRExpression;


/**
 * Used for implementing band functionality. A report can contain the following * bands: background, title,
 * summary, page header, page footer, last page footer, column header, column footer and detail.
 * For each group defined in the report, there is a corresponding group header and group footer.
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id$
 */
public class JRBaseBand extends JRBaseElementGroup implements JRBand
{
	

	/**
	 *
	 */
	private static final long serialVersionUID = JRConstants.SERIAL_VERSION_UID;

	/**
	 *
	 */
	protected int height = 0;
	protected boolean isSplitAllowed = true;

	/**
	 *
	 */
	protected JRExpression printWhenExpression = null;
	

	/**
	 *
	 */
	protected JRBaseBand(JRBand band, JRBaseObjectFactory factory)
	{
		super(band, factory);
		
		height = band.getHeight();
		isSplitAllowed = band.isSplitAllowed();

		printWhenExpression = factory.getExpression(band.getPrintWhenExpression());
	}
		

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
	public boolean isSplitAllowed()
	{
		return this.isSplitAllowed;
	}

	/**
	 *
	 */
	public void setSplitAllowed(boolean isSplitAllowed)
	{
		this.isSplitAllowed = isSplitAllowed;
	}

	/**
	 *
	 */
	public JRExpression getPrintWhenExpression()
	{
		return this.printWhenExpression;
	}

		
}
