/*
 * ============================================================================
 *                   GNU Lesser General Public License
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
package net.sf.jasperreports.engine.base;

import net.sf.jasperreports.engine.JRStaticText;
import net.sf.jasperreports.engine.JRTextElement;


/**
 * This class is used for representing a static text element. Together with its parents, it contains the full
 * functionality needed for handling a static text.
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id$
 */
public class JRBaseStaticText extends JRBaseTextElement implements JRStaticText
{


	/**
	 *
	 */
	private static final long serialVersionUID = 608;

	/**
	 *
	 */
	protected String text = null;


	/**
	 * Initializes the element properties. 
	 */
	protected JRBaseStaticText(JRStaticText staticText, JRBaseObjectFactory factory)
	{
		super((JRTextElement)staticText, factory);
		
		text = staticText.getText();
	}
		

	/**
	 *
	 */
	public String getText()
	{
		return this.text;
	}

	/**
	 *
	 */
	public void setText(String text)
	{
		this.text = text;
	}


}
