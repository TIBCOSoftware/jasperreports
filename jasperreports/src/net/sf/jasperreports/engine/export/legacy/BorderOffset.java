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

/*
 * Contributors:
 * Eugene D - eugenedruy@users.sourceforge.net 
 * Adrian Jackson - iapetus@users.sourceforge.net
 * David Taylor - exodussystems@users.sourceforge.net
 * Lars Kristensen - llk@users.sourceforge.net
 */
package net.sf.jasperreports.engine.export.legacy;

import net.sf.jasperreports.engine.JRPen;
import net.sf.jasperreports.engine.util.JRProperties;

/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id: JRGraphics2DExporter.java 1811 2007-08-13 19:52:07Z teodord $
 */
public abstract class BorderOffset
{
	/**
	 * 
	 */
	public static final String PROPERTY_LEGACY_BORDER_OFFSET = JRProperties.PROPERTY_PREFIX + "export.legacy.border.offset";
	
	/**
	 * 
	 */
	public static final BorderOffset DEFAULT = 
		new BorderOffset()
		{
			public float getValue(JRPen pen)
			{
				return 0f;
			}
		};
	
	/**
	 * 
	 */
	public static final BorderOffset LEGACY = 
		new BorderOffset()
		{
			public float getValue(JRPen pen)
			{
				float lineWidth = pen.getLineWidth().floatValue();
				byte lineStyle = pen.getLineStyle().byteValue();
				
				if (
					lineWidth == 0.5f 
					&& lineStyle == JRPen.LINE_STYLE_SOLID
					)
				{
					return 0.25f;
				}
				else if (
						lineWidth == 1.0f 
					&& (lineStyle == JRPen.LINE_STYLE_SOLID 
						|| lineStyle == JRPen.LINE_STYLE_DASHED)
					)
				{
					return 0.5f;
				}
				
				return 0f;
			}
		};
		
		
	/**
	 * 
	 */
	private static final ThreadLocal threadInstance = new ThreadLocal();
	

	/**
	 * 
	 */
	public static void setLegacy(boolean isLegacy)
	{
		threadInstance.set(isLegacy ? LEGACY : DEFAULT);
	}

	/**
	 * 
	 */
	public static float getOffset(JRPen pen)
	{
		return ((BorderOffset)threadInstance.get()).getValue(pen);
	}

	/**
	 * 
	 */
	public abstract float getValue(JRPen pen);

}
