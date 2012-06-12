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

import net.sf.jasperreports.crosstabs.JRCrosstabRowGroup;
import net.sf.jasperreports.crosstabs.type.CrosstabRowPositionEnum;
import net.sf.jasperreports.engine.JRConstants;

/**
 * Crosstab row group implementation to be used for report designing.
 *  
 * @author Lucian Chirita (lucianc@users.sourceforge.net)
 * @version $Id$
 */
public class JRDesignCrosstabRowGroup extends JRDesignCrosstabGroup implements JRCrosstabRowGroup
{
	private static final long serialVersionUID = JRConstants.SERIAL_VERSION_UID;

	public static final String PROPERTY_POSITION = "position";

	public static final String PROPERTY_WIDTH = "width";

	protected int width;
	protected CrosstabRowPositionEnum positionValue = CrosstabRowPositionEnum.TOP;

	public JRDesignCrosstabRowGroup()
	{
		super();
	}

	public CrosstabRowPositionEnum getPositionValue()
	{
		return positionValue;
	}
	
	
	/**
	 * Sets the header contents stretch position.
	 * 
	 * @param positionValue the header contents stretch position
	 * @see JRCrosstabRowGroup#getPositionValue()
	 */
	public void setPosition(CrosstabRowPositionEnum positionValue)
	{
		Object old = this.positionValue;
		this.positionValue = positionValue;
		getEventSupport().firePropertyChange(PROPERTY_POSITION, old, this.positionValue);
	}

	public int getWidth()
	{
		return width;
	}

	
	/**
	 * Sets the header cell width.
	 * 
	 * @param width the width
	 * @see JRCrosstabRowGroup#getWidth()
	 */
	public void setWidth(int width)
	{
		int old = this.width;
		this.width = width;
		getEventSupport().firePropertyChange(PROPERTY_WIDTH, old, this.width);
	}

	public void setHeader(JRDesignCellContents header)
	{
		super.setHeader(header);
		
		setCellOrigin(this.header, 
				new JRCrosstabOrigin(getParent(), JRCrosstabOrigin.TYPE_ROW_GROUP_HEADER,
						getName(), null));
	}

	public void setTotalHeader(JRDesignCellContents totalHeader)
	{
		super.setTotalHeader(totalHeader);
		
		setCellOrigin(this.totalHeader, 
				new JRCrosstabOrigin(getParent(), JRCrosstabOrigin.TYPE_ROW_GROUP_TOTAL_HEADER,
						getName(), null));
	}

	void setParent(JRDesignCrosstab parent)
	{
		super.setParent(parent);
		
		setCellOrigin(this.header, 
				new JRCrosstabOrigin(getParent(), JRCrosstabOrigin.TYPE_ROW_GROUP_HEADER,
						getName(), null));
		setCellOrigin(this.totalHeader, 
				new JRCrosstabOrigin(getParent(), JRCrosstabOrigin.TYPE_ROW_GROUP_TOTAL_HEADER,
						getName(), null));
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
			positionValue = CrosstabRowPositionEnum.getByValue(position);
		}
	}

}
