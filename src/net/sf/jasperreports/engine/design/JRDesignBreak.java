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

import java.io.IOException;

import net.sf.jasperreports.engine.JRAbstractObjectFactory;
import net.sf.jasperreports.engine.JRBreak;
import net.sf.jasperreports.engine.JRChild;
import net.sf.jasperreports.engine.JRConstants;
import net.sf.jasperreports.engine.JRDefaultStyleProvider;
import net.sf.jasperreports.engine.JRExpressionCollector;
import net.sf.jasperreports.engine.xml.JRXmlWriter;


/**
 * The actual implementation of a break element, used at design time.
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id$
 */
public class JRDesignBreak extends JRDesignElement implements JRBreak
{


	/**
	 *
	 */
	private static final long serialVersionUID = JRConstants.SERIAL_VERSION_UID;

	/**
	 *
	 */
	protected byte type = TYPE_PAGE;


	/**
	 *
	 */
	public JRDesignBreak()
	{
		super(null);
	}
		
	/**
	 *
	 */
	public JRDesignBreak(JRDefaultStyleProvider defaultStyleProvider)
	{
		super(defaultStyleProvider);
	}
		

	/**
	 *
	 */
	public int getX()
	{
		return 0;
	}

	/**
	 *
	 */
	public int getHeight()
	{
		return 1;
	}

	/**
	 *
	 */
	public byte getType()
	{
		return type;
	}

	/**
	 *
	 */
	public void setType(byte type)
	{
		this.type = type;
	}

	/**
	 *
	 */
	public JRChild getCopy(JRAbstractObjectFactory factory)
	{
		return factory.getBreak(this);
	}

	/**
	 *
	 */
	public void collectExpressions(JRExpressionCollector collector)
	{
		collector.collect(this);
	}

	/**
	 *
	 */
	public void writeXml(JRXmlWriter xmlWriter) throws IOException
	{
		xmlWriter.writeBreak(this);
	}


}
