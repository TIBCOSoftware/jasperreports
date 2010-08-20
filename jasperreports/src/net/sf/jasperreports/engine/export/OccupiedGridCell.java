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
package net.sf.jasperreports.engine.export;

import net.sf.jasperreports.engine.base.JRBasePrintElement;

	
	
/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id: JRExporterGridCell.java 2695 2009-03-24 18:14:25Z teodord $
 */
public class OccupiedGridCell extends JRExporterGridCell
{


	/**
	 *
	 */
	private JRExporterGridCell occupier;


	/**
	 *
	 */
	public OccupiedGridCell(JRExporterGridCell occupier)
	{
		super(
			new ElementWrapper(null, new JRBasePrintElement(null), null), //FIXMEDOCX optimize memory with static fields
			0, 0, 1, 1
			);
		
		this.occupier = occupier;
	}


	public JRExporterGridCell getOccupier()
	{
		return occupier;
	}


	public byte getType()
	{
		return TYPE_OCCUPIED_CELL;
	}

}
