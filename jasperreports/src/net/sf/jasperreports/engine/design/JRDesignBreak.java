/*
 * JasperReports - Free Java Reporting Library.
 * Copyright (C) 2001 - 2009 Jaspersoft Corporation. All rights reserved.
 * http://www.jaspersoft.com
 *
 * Unless you have purchased a commercial license agreement from Jaspersoft,
 * the following license terms apply:
 *
 * This program is part of JasperReports.
 *
 * JasperReports is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * JasperReports is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with JasperReports. If not, see <http://www.gnu.org/licenses/>.
 */
package net.sf.jasperreports.engine.design;

import net.sf.jasperreports.engine.JRBreak;
import net.sf.jasperreports.engine.JRConstants;
import net.sf.jasperreports.engine.JRDefaultStyleProvider;
import net.sf.jasperreports.engine.JRExpressionCollector;
import net.sf.jasperreports.engine.JRVisitor;
import net.sf.jasperreports.engine.base.JRBaseBreak;


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
		byte old = this.type;
		this.type = type;
		getEventSupport().firePropertyChange(JRBaseBreak.PROPERTY_TYPE, old, this.type);
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
	public void visit(JRVisitor visitor)
	{
		visitor.visitBreak(this);
	}


}
