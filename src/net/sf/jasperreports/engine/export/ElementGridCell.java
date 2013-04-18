/*
 * JasperReports - Free Java Reporting Library.
 * Copyright (C) 2001 - 2013 Jaspersoft Corporation. All rights reserved.
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

import net.sf.jasperreports.engine.JRPrintElement;
import net.sf.jasperreports.engine.JRPrintFrame;
import net.sf.jasperreports.engine.JRRuntimeException;

	
	
/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id$
 */
public class ElementGridCell extends JRExporterGridCell
{
	
	private int width;
	private int height;
	private int colSpan;
	private int rowSpan;

	// TODO lucianc do not keep a reference to the container here but require exporters to use the cell with the container
	private JRGridLayout container;

	private PrintElementIndex parentIndex;
	private int elementIndex;

	/**
	 *
	 */
	public ElementGridCell(
		JRGridLayout container,
		PrintElementIndex parentIndex,
		int elementIndex,
		int width, 
		int height,
		int colSpan, 
		int rowSpan
		)
	{
		// TODO lucianc store these in separate cached objects since they usually repeat
		this.width = width;
		this.height = height;
		this.colSpan = colSpan;
		this.rowSpan = rowSpan;
		
		this.container = container;
		this.parentIndex = parentIndex;
		this.elementIndex = elementIndex;
	}

	public int getWidth()
	{
		return width;
	}

	public void setWidth(int width)
	{
		// should not happen
		throw new UnsupportedOperationException("Cannot set width on an element cell");
	}

	public int getHeight()
	{
		return height;
	}

	public int getColSpan()
	{
		return colSpan;
	}

	public void setColSpan(int colSpan)
	{
		// should not happen
		throw new UnsupportedOperationException("Cannot set column span on an element cell");
	}

	public int getRowSpan()
	{
		return rowSpan;
	}

	@Override
	public byte getType()
	{
		return TYPE_ELEMENT_CELL;
	}

	@Override
	public boolean isEmpty()
	{
		return false;
	}

	@Override
	public boolean isOccupied()
	{
		return true;
	}
	
	@Override
	public JRPrintElement getElement()
	{
		return container.getElement(parentIndex, elementIndex);
	}
	
	public String getProperty(String propName)
	{
		JRPrintElement element = getElement();
		if (element.hasProperties()
				&& element.getPropertiesMap().containsProperty(propName))
		{
			return element.getPropertiesMap().getProperty(propName);
		}

		PrintElementIndex ancestorIndex = parentIndex;
		while (ancestorIndex != null)
		{
			JRPrintElement ancestor = container.getElement(
					ancestorIndex.getParentIndex(), ancestorIndex.getIndex());
			if (ancestor.hasProperties()
					&& ancestor.getPropertiesMap().containsProperty(propName))
			{
				return ancestor.getPropertiesMap().getProperty(propName);
			}
			
			ancestorIndex = ancestorIndex.getParentIndex();
		}
			
		return null;
	}

	public PrintElementIndex getParentIndex()
	{
		return parentIndex;
	}

	public int getElementIndex()
	{
		return elementIndex;
	}

	@Override
	public String getElementAddress()
	{
		return PrintElementIndex.asAddress(parentIndex, elementIndex);
	}

	public JRGridLayout getLayout()
	{
		JRPrintElement element = getElement();
		if (!(element instanceof JRPrintFrame))
		{
			// should not happen
			throw new JRRuntimeException("Element at address " + getElementAddress() + " is not a frame");
		}
		
		JRPrintFrame frame = (JRPrintFrame) element;
		PrintElementIndex frameIndex = new PrintElementIndex(getParentIndex(), getElementIndex());
		return new JRGridLayout(
				container, 
				frame.getElements(),
				frame.getWidth(), 
				frame.getHeight(),
				0, //offsetX
				0, //offsetY
				frameIndex
		);
	}

}
