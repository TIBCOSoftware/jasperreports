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
package net.sf.jasperreports.engine.design;

import net.sf.jasperreports.engine.JRFont;
import net.sf.jasperreports.engine.JRTextElement;


/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id$
 */
public abstract class JRDesignTextElement extends JRDesignElement implements JRTextElement
{


	/**
	 *
	 */
	private static final long serialVersionUID = 503;

	/**
	 *
	 */
	protected byte horizontalAlignment = HORIZONTAL_ALIGN_LEFT;
	protected byte verticalAlignment = VERTICAL_ALIGN_TOP;
	protected byte rotation = ROTATION_NONE;
	protected byte lineSpacing = LINE_SPACING_SINGLE;
	protected boolean isStyledText = false;

	/**
	 *
	 */
	protected JRFont font = null;


	/**
	 *
	 */
	protected JRDesignTextElement()
	{
		super();
		
		mode = MODE_TRANSPARENT;
	}
		

	/**
	 *
	 */
	public byte getTextAlignment()
	{
		return horizontalAlignment;
	}
		
	/**
	 *
	 */
	public byte getVerticalAlignment()
	{
		return verticalAlignment;
	}
		
	/**
	 *
	 */
	public byte getRotation()
	{
		return rotation;
	}
		
	/**
	 *
	 */
	public byte getLineSpacing()
	{
		return lineSpacing;
	}
		
	/**
	 *
	 */
	public boolean isStyledText()
	{
		return isStyledText;
	}
		
	/**
	 *
	 */
	public JRFont getFont()
	{
		return font;
	}

	/**
	 *
	 */
	public void setTextAlignment(byte horizontalAlignment)
	{
		this.horizontalAlignment = horizontalAlignment;
	}
		
	/**
	 *
	 */
	public void setVerticalAlignment(byte verticalAlignment)
	{
		this.verticalAlignment = verticalAlignment;
	}
		
	/**
	 *
	 */
	public void setRotation(byte rotation)
	{
		this.rotation = rotation;
	}
		
	/**
	 *
	 */
	public void setLineSpacing(byte lineSpacing)
	{
		this.lineSpacing = lineSpacing;
	}
		
	/**
	 *
	 */
	public void setStyledText(boolean isStyledText)
	{
		this.isStyledText = isStyledText;
	}
		
	/**
	 *
	 */
	public void setFont(JRFont font)
	{
		this.font = font;
	}
	

}
