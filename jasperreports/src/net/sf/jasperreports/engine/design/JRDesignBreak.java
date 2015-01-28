/*
 * JasperReports - Free Java Reporting Library.
 * Copyright (C) 2001 - 2014 TIBCO Software Inc. All rights reserved.
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

import java.io.IOException;
import java.io.ObjectInputStream;

import net.sf.jasperreports.engine.JRBreak;
import net.sf.jasperreports.engine.JRConstants;
import net.sf.jasperreports.engine.JRDefaultStyleProvider;
import net.sf.jasperreports.engine.JRExpressionCollector;
import net.sf.jasperreports.engine.JRVisitor;
import net.sf.jasperreports.engine.base.JRBaseBreak;
import net.sf.jasperreports.engine.type.BreakTypeEnum;


/**
 * The actual implementation of a break element, used at design time.
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 */
public class JRDesignBreak extends JRDesignElement implements JRBreak
{


	/**
	 *
	 */
	private static final long serialVersionUID = JRConstants.SERIAL_VERSION_UID;

	protected BreakTypeEnum typeValue = BreakTypeEnum.PAGE;

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
	public BreakTypeEnum getTypeValue()
	{
		return this.typeValue;
	}

	/**
	 *
	 */
	public void setType(BreakTypeEnum typeValue)
	{
		Object old = this.typeValue;
		this.typeValue = typeValue;
		getEventSupport().firePropertyChange(JRBaseBreak.PROPERTY_TYPE, old, this.typeValue);
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

	/*
	 * These fields are only for serialization backward compatibility.
	 */
	private int PSEUDO_SERIAL_VERSION_UID = JRConstants.PSEUDO_SERIAL_VERSION_UID; //NOPMD
	/**
	 * @deprecated
	 */
	private byte type;
	
	@SuppressWarnings("deprecation")
	private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException
	{
		in.defaultReadObject();

		if (PSEUDO_SERIAL_VERSION_UID < JRConstants.PSEUDO_SERIAL_VERSION_UID_3_7_2)
		{
			typeValue = BreakTypeEnum.getByValue(type);
		}
	}

}
