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
package net.sf.jasperreports.engine.fill;

import net.sf.jasperreports.engine.JRBox;
import net.sf.jasperreports.engine.JRConstants;

/**
 * Frame information shared by multiple print frame objects.
 * 
 * @author Lucian Chirita (lucianc@users.sourceforge.net)
 * @version $Id$
 * @see net.sf.jasperreports.engine.fill.JRTemplatePrintFrame
 */
public class JRTemplateFrame extends JRTemplateElement
{
	private static final long serialVersionUID = JRConstants.SERIAL_VERSION_UID;

	private JRBox box;

	
	/**
	 * Creates a template frame for a crosstab cell.
	 * 
	 * @param crosstab the crosstab
	 * @param cell the cell
	 */
	public JRTemplateFrame(JRFillCrosstab crosstab, JRFillCellContents cell)
	{
		super(crosstab);
		
		if (cell.getBackcolor() != null)
		{
			setBackcolor(cell.getBackcolor());
		}
		
		setBox(cell.getBox());
	}
	
	
	/**
	 * Creates a template frame for a frame.
	 * 
	 * @param frame the frame
	 */
	public JRTemplateFrame(JRFillFrame frame)
	{
		super(frame);
		
		setBox(frame.getBox());
	}

	
	/**
	 * Returns the border of this template frame.
	 * 
	 * @return the border of this template frame
	 */
	public JRBox getBox()
	{
		return box;
	}

	
	/**
	 * Sets the border of this template frame.
	 * 
	 * @param box the border
	 */
	public void setBox(JRBox box)
	{
		this.box = box;
	}
}
