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
package net.sf.jasperreports.crosstabs.base;

import java.io.IOException;
import java.io.ObjectInputStream;

import net.sf.jasperreports.crosstabs.JRCellContents;
import net.sf.jasperreports.crosstabs.JRCrosstabColumnGroup;
import net.sf.jasperreports.crosstabs.type.CrosstabColumnPositionEnum;
import net.sf.jasperreports.engine.JRConstants;
import net.sf.jasperreports.engine.base.JRBaseObjectFactory;
import net.sf.jasperreports.engine.util.JRCloneUtils;

/**
 * Base read-only implementation of crosstab column groups.
 * 
 * @author Lucian Chirita (lucianc@users.sourceforge.net)
 */
public class JRBaseCrosstabColumnGroup extends JRBaseCrosstabGroup implements JRCrosstabColumnGroup
{
	private static final long serialVersionUID = JRConstants.SERIAL_VERSION_UID;

	protected int height;
	protected CrosstabColumnPositionEnum positionValue = CrosstabColumnPositionEnum.LEFT;
	protected JRCellContents crosstabHeader;

	public JRBaseCrosstabColumnGroup(JRCrosstabColumnGroup group, JRBaseObjectFactory factory)
	{
		super(group, factory);
		
		height = group.getHeight();
		positionValue = group.getPositionValue();
		crosstabHeader = factory.getCell(group.getCrosstabHeader());
	}

	public CrosstabColumnPositionEnum getPositionValue()
	{
		return positionValue;
	}

	public int getHeight()
	{
		return height;
	}

	@Override
	public JRCellContents getCrosstabHeader()
	{
		return crosstabHeader;
	}

	@Override
	public Object clone()
	{
		JRBaseCrosstabColumnGroup clone = (JRBaseCrosstabColumnGroup) super.clone();
		clone.crosstabHeader = JRCloneUtils.nullSafeClone(crosstabHeader);
		return clone;
	}

	
	/*
	 * These fields are only for serialization backward compatibility.
	 */
	private int PSEUDO_SERIAL_VERSION_UID = JRConstants.PSEUDO_SERIAL_VERSION_UID; //NOPMD
	/**
	 * @deprecated
	 */
	private byte position;
	
	@SuppressWarnings("deprecation")
	private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException
	{
		in.defaultReadObject();
		
		if (PSEUDO_SERIAL_VERSION_UID < JRConstants.PSEUDO_SERIAL_VERSION_UID_3_7_2)
		{
			positionValue = CrosstabColumnPositionEnum.getByValue(position);
		}
	}

}
