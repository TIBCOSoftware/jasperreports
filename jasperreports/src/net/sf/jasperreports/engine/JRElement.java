/*
 * ============================================================================
 *                   GNU Lesser General Public License
 * ============================================================================
 *
 * JasperReports - Free Java report-generating library.
 * Copyright (C) 2001-2005 Teodor Danciu teodord@users.sourceforge.net
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
package net.sf.jasperreports.engine;

import java.awt.Color;


/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id$
 */
public interface JRElement
{


	/**
	 *
	 */
	public static final byte POSITION_TYPE_FLOAT = 1;
	public static final byte POSITION_TYPE_FIX_RELATIVE_TO_TOP = 2;
	public static final byte POSITION_TYPE_FIX_RELATIVE_TO_BOTTOM = 3;

	/**
	 *
	 */
	public static final byte MODE_OPAQUE = 1;
	public static final byte MODE_TRANSPARENT = 2;

	/**
	 *
	 */
	public static final byte STRETCH_TYPE_NO_STRETCH = 0;
	public static final byte STRETCH_TYPE_RELATIVE_TO_TALLEST_OBJECT = 1;
	public static final byte STRETCH_TYPE_RELATIVE_TO_BAND_HEIGHT = 2;


	/**
	 *
	 */
	public String getKey();

	/**
	 *
	 */
	public byte getPositionType();

	/**
	 *
	 */
	public void setPositionType(byte positionType);

	/**
	 *
	 */
	public byte getStretchType();
		
	/**
	 *
	 */
	public void setStretchType(byte stretchType);
		
	/**
	 *
	 */
	public boolean isPrintRepeatedValues();
	
	/**
	 *
	 */
	public void setPrintRepeatedValues(boolean isPrintRepeatedValues);
	
	/**
	 *
	 */
	public byte getMode();
	
	/**
	 *
	 */
	public void setMode(byte mode);
	
	/**
	 *
	 */
	public int getX();
	
	/**
	 *
	 */
	public void setX(int x);
	
	/**
	 *
	 */
	public int getY();
	
	/**
	 *
	 */
	public int getWidth();
	
	/**
	 *
	 */
	public void setWidth(int width);
	
	/**
	 *
	 */
	public int getHeight();
	
	/**
	 *
	 */
	public boolean isRemoveLineWhenBlank();
	
	/**
	 *
	 */
	public void setRemoveLineWhenBlank(boolean isRemoveLineWhenBlank);
	
	/**
	 *
	 */
	public boolean isPrintInFirstWholeBand();
	
	/**
	 *
	 */
	public void setPrintInFirstWholeBand(boolean isPrintInFirstWholeBand);
	
	/**
	 *
	 */
	public boolean isPrintWhenDetailOverflows();
	
	/**
	 *
	 */
	public void setPrintWhenDetailOverflows(boolean isPrintWhenDetailOverflows);
	
	/**
	 *
	 */
	public Color getForecolor();
	
	/**
	 *
	 */
	public void setForecolor(Color forecolor);
	
	/**
	 *
	 */
	public Color getBackcolor();
	
	/**
	 *
	 */
	public void setBackcolor(Color backcolor);
	
	/**
	 *
	 */
	public JRExpression getPrintWhenExpression();
	
	/**
	 *
	 */
	public JRGroup getPrintWhenGroupChanges();
	
	/**
	 *
	 */
	public JRElementGroup getElementGroup();


}
