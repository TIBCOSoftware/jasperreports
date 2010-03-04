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
package net.sf.jasperreports.engine.fill;

import java.io.IOException;
import java.io.ObjectInputStream;

import net.sf.jasperreports.engine.JRConstants;
import net.sf.jasperreports.engine.JRDefaultStyleProvider;
import net.sf.jasperreports.engine.JRLine;
import net.sf.jasperreports.engine.JROrigin;
import net.sf.jasperreports.engine.base.JRBasePen;
import net.sf.jasperreports.engine.type.LineDirectionEnum;


/**
 * Line element information shared by multiple print line objects.
 * 
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id$
 * @see JRTemplatePrintLine
 */
public class JRTemplateLine extends JRTemplateGraphicElement
{


	/**
	 *
	 */
	private static final long serialVersionUID = JRConstants.SERIAL_VERSION_UID;

	/**
	 *
	 */
	protected LineDirectionEnum directionValue = LineDirectionEnum.TOP_DOWN;

	/**
	 *
	 */
	protected JRTemplateLine(JROrigin origin, JRDefaultStyleProvider defaultStyleProvider, JRLine line)
	{
		super(origin, defaultStyleProvider);

		setLine(line);
	}

	
	/**
	 * Creates a template line.
	 * 
	 * @param origin the origin of the elements that will use this template
	 * @param defaultStyleProvider the default style provider to use for
	 * this template
	 */
	public JRTemplateLine(JROrigin origin, JRDefaultStyleProvider defaultStyleProvider)
	{
		super(origin, defaultStyleProvider);
		
		this.linePen = new JRBasePen(this);
	}


	/**
	 *
	 */
	protected void setLine(JRLine line)
	{
		super.setGraphicElement(line);

		setDirection(line.getDirectionValue());
	}

	/**
	 * @deprecated Replaced by {@link getDirectionValue()}.
	 */
	public byte getDirection()
	{
		return getDirectionValue().getValue();
	}

	/**
	 * @deprecated Replaced by {@link setDirection(LineDirectionEnum)}.
	 */
	public void setDirection(byte direction)
	{
		setDirection(LineDirectionEnum.getByValue(direction));
	}

	/**
	 * 
	 */
	public LineDirectionEnum getDirectionValue()
	{
		return this.directionValue;
	}

	/**
	 * 
	 */
	public void setDirection(LineDirectionEnum directionValue)
	{
		this.directionValue = directionValue;
	}

	/**
	 * These fields are only for serialization backward compatibility.
	 */
	private int PSEUDO_SERIAL_VERSION_UID = JRConstants.PSEUDO_SERIAL_VERSION_UID;
	private byte direction;
	
	private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException
	{
		in.defaultReadObject();

		if (PSEUDO_SERIAL_VERSION_UID < JRConstants.PSEUDO_SERIAL_VERSION_UID_3_7_2)
		{
			directionValue = LineDirectionEnum.getByValue(direction);
		}
		
	}

}
