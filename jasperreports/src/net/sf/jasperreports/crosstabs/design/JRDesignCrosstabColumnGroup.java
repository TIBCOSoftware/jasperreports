/*
 * JasperReports - Free Java Reporting Library.
 * Copyright (C) 2001 - 2011 Jaspersoft Corporation. All rights reserved.
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
package net.sf.jasperreports.crosstabs.design;

import java.io.IOException;
import java.io.ObjectInputStream;

import net.sf.jasperreports.crosstabs.JRCrosstabColumnGroup;
import net.sf.jasperreports.crosstabs.type.CrosstabColumnPositionEnum;
import net.sf.jasperreports.engine.JRConstants;

/**
 * Crosstab column group implementation to be used for report designing. 
 * 
 * @author Lucian Chirita (lucianc@users.sourceforge.net)
 * @version $Id$
 */
public class JRDesignCrosstabColumnGroup extends JRDesignCrosstabGroup implements JRCrosstabColumnGroup
{
	private static final long serialVersionUID = JRConstants.SERIAL_VERSION_UID;

	public static final String PROPERTY_HEIGHT = "height";

	public static final String PROPERTY_POSITION = "position";

	protected int height;
	protected CrosstabColumnPositionEnum positionValue = CrosstabColumnPositionEnum.LEFT;

	
	/**
	 * Creates a column group.
	 */
	public JRDesignCrosstabColumnGroup()
	{
		super();
	}

	public CrosstabColumnPositionEnum getPositionValue()
	{
		return positionValue;
	}
	
	
	/**
	 * Sets the header contents stretch position.
	 * 
	 * @param positionValue the header contents stretch position
	 * @see JRCrosstabColumnGroup#getPositionValue()
	 */
	public void setPosition(CrosstabColumnPositionEnum positionValue)
	{
		Object old = this.positionValue;
		this.positionValue = positionValue;
		getEventSupport().firePropertyChange(PROPERTY_POSITION, old, this.positionValue);
	}

	public int getHeight()
	{
		return height;
	}

	
	/**
	 * Sets the header cell height.
	 * 
	 * @param height the height
	 * @see JRCrosstabColumnGroup#getHeight()
	 */
	public void setHeight(int height)
	{
		int old = this.height;
		this.height = height;
		getEventSupport().firePropertyChange(PROPERTY_HEIGHT, old, this.height);
	}

	public void setHeader(JRDesignCellContents header)
	{
		super.setHeader(header);
		
		setCellOrigin(this.header, 
				new JRCrosstabOrigin(getParent(), JRCrosstabOrigin.TYPE_COLUMN_GROUP_HEADER,
						null, getName()));
	}

	public void setTotalHeader(JRDesignCellContents totalHeader)
	{
		super.setTotalHeader(totalHeader);
		
		setCellOrigin(this.totalHeader, 
				new JRCrosstabOrigin(getParent(), JRCrosstabOrigin.TYPE_COLUMN_GROUP_TOTAL_HEADER,
						null, getName()));
	}

	void setParent(JRDesignCrosstab parent)
	{
		super.setParent(parent);
		
		setCellOrigin(this.header, 
				new JRCrosstabOrigin(getParent(), JRCrosstabOrigin.TYPE_COLUMN_GROUP_HEADER,
						null, getName()));
		setCellOrigin(this.totalHeader, 
				new JRCrosstabOrigin(getParent(), JRCrosstabOrigin.TYPE_COLUMN_GROUP_TOTAL_HEADER,
						null, getName()));
	}

	
	/*
	 * These fields are only for serialization backward compatibility.
	 */
	private int PSEUDO_SERIAL_VERSION_UID = JRConstants.PSEUDO_SERIAL_VERSION_UID; //NOPMD
	/**
	 * @deprecated
	 */
	private byte position;
	
	private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException
	{
		in.defaultReadObject();
		
		if (PSEUDO_SERIAL_VERSION_UID < JRConstants.PSEUDO_SERIAL_VERSION_UID_3_7_2)
		{
			positionValue = CrosstabColumnPositionEnum.getByValue(position);
		}
	}

}
