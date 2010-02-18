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
package net.sf.jasperreports.engine;

import net.sf.jasperreports.engine.type.BreakTypeEnum;
import net.sf.jasperreports.engine.type.HorizontalAlignEnum;
import net.sf.jasperreports.engine.type.RotationEnum;
import net.sf.jasperreports.engine.type.VerticalAlignEnum;


/**
 * An abstract representation of a break element.
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id$
 */
public interface JRBreak extends JRElement
{


	/**
	 * @deprecated Replaced by {@link BreakTypeEnum#PAGE}.
	 */
	public static final byte TYPE_PAGE = 1;

	/**
	 * @deprecated Replaced by {@link BreakTypeEnum#COLUMN}.
	 */
	public static final byte TYPE_COLUMN = 2;


	/**
	 * @deprecated Replaced by {@link getTypeValue()}.
	 */
	public byte getType();

	/**
	 * @deprecated Replaced by {@link setType(BreakTypeEnum)}.
	 */
	public void setType(byte type);
	
	//TODO: uncomment these below

//	/**
//	 * Gets the break type.
//	 * @return a value representing one of the break type constants in {@link BreakTypeEnum}
//	 */
//	public BreakTypeEnum getTypeValue();
//	
//	/**
//	 * Sets the break type.
//	 * @param breakTypeEnum a value representing one of the break type constants in {@link BreakTypeEnum}
//	 */
//	public void setType(BreakTypeEnum breakTypeEnum);
	

}
