/*
 * ============================================================================
 * GNU Lesser General Public License
 * ============================================================================
 *
 * JasperReports - Free Java report-generating library.
 * Copyright (C) 2001-2005 JasperSoft Corporation http://www.jaspersoft.com
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
package net.sf.jasperreports.engine.fill;

import java.awt.Color;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import net.sf.jasperreports.crosstabs.JRCellContents;
import net.sf.jasperreports.engine.JRBox;
import net.sf.jasperreports.engine.JRDefaultStyleProvider;
import net.sf.jasperreports.engine.JRElement;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRFrame;
import net.sf.jasperreports.engine.JRGraphicElement;
import net.sf.jasperreports.engine.JRPrintElement;
import net.sf.jasperreports.engine.JRPrintFrame;
import net.sf.jasperreports.engine.JRStyle;
import net.sf.jasperreports.engine.base.JRBaseBox;

import org.apache.commons.collections.ReferenceMap;

/**
 * Crosstab cell contents filler.
 * 
 * @author Lucian Chirita (lucianc@users.sourceforge.net)
 * @version $Id$
 */
public class JRFillCellContents extends JRFillElementContainer implements JRCellContents, JRCloneable
{
	private final Map transformedContentsCache;
	private final Map boxContentsCache;
	private final JRClonePool clonePool;
	
	private JRFillCellContents original;
	
	private final JRCellContents parentCell;
	
	private JRBox box;
	
	private int height;
	private int width;
	
	private int x;
	private int y;
	private int verticalSpan;
	private byte verticalPositionType = JRCellContents.POSITION_Y_TOP;
	
	private Map templateFrames;
	
	private final JRStyle style;

	public JRFillCellContents(JRBaseFiller filler, JRCellContents cell, JRFillObjectFactory factory)
	{
		super(filler, cell, factory);
		
		parentCell = cell;
		
		box = cell.getBox();
		
		width = cell.getWidth();
		height = cell.getHeight();
		
		style = factory.getStyle(parentCell.getStyle());
		
		initElements();
		
		initConditionalStyles();
		
		initTemplatesMap();
		
		transformedContentsCache = new ReferenceMap();
		boxContentsCache = new HashMap();
		clonePool = new JRClonePool(this, true, true);
	}

	private void initTemplatesMap()
	{
		templateFrames = new HashMap();
	}

	protected JRFillCellContents(JRFillCellContents cellContents, JRFillCloneFactory factory)
	{
		super(cellContents, factory);
		
		parentCell = cellContents.parentCell;
		
		box = cellContents.box;
		
		width = cellContents.width;
		height = cellContents.height;
		
		style = cellContents.style;
		
		initElements();
		
		initConditionalStyles();
		
		this.templateFrames = cellContents.templateFrames;
		
		transformedContentsCache = new ReferenceMap();
		boxContentsCache = new HashMap();
		clonePool = new JRClonePool(this, true, true);
		
		verticalPositionType = cellContents.verticalPositionType;
	}

	public Color getBackcolor()
	{
		return parentCell.getBackcolor();
	}

	public JRBox getBox()
	{
		return box;
	}

	protected void setBox(JRBox box)
	{
		this.box = box;
		
		initTemplatesMap();
	}
	
	public int getHeight()
	{
		return height;
	}
	
	
	public int getWidth()
	{
		return width;
	}
	
	
	protected void setHeight(int height)
	{
		this.height = height;
	}
	
	
	protected void setWidth(int width)
	{
		this.width = width;
	}
	
	
	public JRFillCellContents getBoxContents(boolean left, boolean top)
	{
		if (box == null)
		{
			return this;
		}
		
		boolean copyLeft = left && box.getLeftBorder() == JRGraphicElement.PEN_NONE && box.getRightBorder() != JRGraphicElement.PEN_NONE;
		boolean copyTop = top && box.getTopBorder() == JRGraphicElement.PEN_NONE && box.getBottomBorder() != JRGraphicElement.PEN_NONE;
		
		if (!(copyLeft || copyTop))
		{
			return this;
		}
		
		Object key = new BoxContents(copyLeft, copyTop);
		JRFillCellContents boxContents = (JRFillCellContents) boxContentsCache.get(key);
		if (boxContents == null)
		{
			boxContents = (JRFillCellContents) createClone();
			
			JRBaseBox newBox = new JRBaseBox(box);
			
			if (copyLeft)
			{
				newBox.setLeftBorder(box.getRightBorder());
				newBox.setLeftBorderColor(box.getRightBorderColor());
			}
			
			if (copyTop)
			{
				newBox.setTopBorder(box.getBottomBorder());
				newBox.setTopBorderColor(box.getBottomBorderColor());
			}
			
			boxContents.setBox(newBox);
			
			boxContentsCache.put(key, boxContents);
		}
		
		return boxContents;
	}
	
	
	public JRFillCellContents getTransformedContents(
			int newWidth, int newHeight,
			byte xPosition, byte yPosition) throws JRException
	{
		if ((getHeight() == newHeight) && 
				(getWidth() == newWidth))
		{
			return this;
		}
		
		if (newHeight < getHeight() || newWidth < getWidth())
		{
			throw new JRException("Cannot shrink cell contents.");
		}
		
		Object key = new StretchedContents(newWidth, newHeight, xPosition, yPosition);
		
		JRFillCellContents transformedCell = (JRFillCellContents) transformedContentsCache.get(key);
		if (transformedCell == null)
		{
			transformedCell = (JRFillCellContents) createClone();
			transformedCell.transform(newWidth, newHeight, xPosition, yPosition);
			transformedCell.setElementsBandBottomY();
			
			transformedContentsCache.put(key, transformedCell);
		}
		
		return transformedCell;
	}
	
	
	private void transform(int newWidth, int newHeight, byte xPosition, byte yPosition)
	{
		transformElements(newWidth, newHeight, xPosition, yPosition);
		
		width = newWidth;
		height = newHeight;
	}

	private void transformElements(int newWidth, int newHeight, byte xPosition, byte yPosition)
	{
		if ((height == newHeight || yPosition == JRCellContents.POSITION_Y_TOP) && 
				(width == newWidth || xPosition == JRCellContents.POSITION_X_LEFT))
		{
			return;
		}

		double scaleX =  -1d;
		int offsetX = 0;
		switch (xPosition)
		{
			case JRCellContents.POSITION_X_CENTER:
				offsetX = (newWidth - width) / 2;
				break;
			case JRCellContents.POSITION_X_RIGHT:
				offsetX = newWidth - width;
				break;
			case JRCellContents.POSITION_X_STRETCH:
				scaleX = ((double) newWidth) / width;
				break;
		}
		
		double scaleY =  -1d;
		int offsetY = 0;
		switch (yPosition)
		{
			case JRCellContents.POSITION_Y_MIDDLE:
				offsetY = (newHeight - height) / 2;
				break;
			case JRCellContents.POSITION_Y_BOTTOM:
				offsetY = newHeight - height;
				break;
			case JRCellContents.POSITION_X_STRETCH:
				scaleY = ((double) newHeight) / height;
				break;
		}
		
		transformElements(getElements(), scaleX, offsetX, scaleY, offsetY);
	}

	private static void transformElements(JRElement[] elements, double scaleX, int offsetX, double scaleY, int offsetY)
	{
		if (elements != null)
		{
			for (int i = 0; i < elements.length; i++)
			{
				JRFillElement element = (JRFillElement) elements[i];
				
				if (scaleX != -1d)
				{
					element.setX((int) (element.getX() * scaleX));
					element.setWidth((int) (element.getWidth() * scaleX));
				}
				
				if (offsetX != 0)
				{
					element.setX(element.getX() + offsetX);
				}				
				
				if (scaleY != -1d)
				{
					element.setY((int) (element.getY() * scaleY));
					element.setHeight((int) (element.getHeight() * scaleY));
				}
				
				if (offsetY != 0)
				{
					element.setY(element.getY() + offsetY);
				}
				
				if (element instanceof JRFrame)
				{
					JRElement[] frameElements = ((JRFrame) element).getElements();
					transformElements(frameElements, scaleX, offsetX, scaleY, offsetY);
				}
			}
		}
	}
	
	
	protected void prepare(int availableStretchHeight) throws JRException
	{
		initFill();
		resetElements();
		prepareElements(availableStretchHeight, true);
	}

	
	protected JRPrintFrame fill() throws JRException
	{
		stretchElements();
		moveBandBottomElements();
		removeBlankElements();

		JRTemplatePrintFrame printCell = new JRTemplatePrintFrame(getTemplateFrame());
		printCell.setX(x);
		printCell.setY(y);
		printCell.setWidth(width);
		
		fillElements(printCell);
		
		verticallyPositionElements(printCell);
		
		printCell.setHeight(getPrintHeight());
		
		return printCell;
	}

	
	private JRTemplateFrame getTemplateFrame()
	{
		JRStyle currStyle = getEvaluatedConditionalStyle(style);
		if (currStyle == null)
		{
			currStyle = style;
		}
		JRTemplateFrame template = (JRTemplateFrame) templateFrames.get(currStyle);
		if (template == null)
		{
			template = new JRTemplateFrame(filler.getJasperPrint().getDefaultStyleProvider(), this, currStyle);
			templateFrames.put(currStyle, template);
		}
		return template;
	}

	
	protected void verticallyPositionElements(JRTemplatePrintFrame printCell)
	{
		int positionOffset;
		
		switch (verticalPositionType)
		{
			case JRCellContents.POSITION_Y_MIDDLE:
				positionOffset = (getStretchHeight() - getContainerHeight()) / 2;
				break;
			case JRCellContents.POSITION_Y_BOTTOM:
				positionOffset = getStretchHeight() - getContainerHeight();
				break;
			default:
				positionOffset = 0;
				break;
		}
		
		if (positionOffset != 0)
		{
			List printElements = printCell.getElements();
			
			int positionY = getStretchHeight() - positionOffset;
			boolean outside = false;
			for (Iterator it = printElements.iterator(); !outside && it.hasNext();)
			{
				JRPrintElement element = (JRPrintElement) it.next();
				outside = element.getY() > positionY;
			}
			
			if (!outside)
			{
				for (Iterator it = printElements.iterator(); it.hasNext();)
				{
					JRPrintElement element = (JRPrintElement) it.next();
					element.setY(element.getY() + positionOffset);
				}
			}
		}
	}

	protected int getPrintHeight()
	{
		return getStretchHeight() + getTopPadding() + getBottomPadding();
	}

	protected void stretchTo(int stretchHeight)
	{
		setStretchHeight(stretchHeight - getTopPadding() - getBottomPadding());
	}
	
	protected static class BoxContents
	{
		final boolean left;
		final boolean top;
		final int hashCode;
		
		public BoxContents(boolean left, boolean top)
		{
			this.left = left;
			this.top = top;
			
			int hash = left ? 1231 : 1237;
			hash = 31*hash + (top ? 1231 : 1237);
			hashCode = hash;
		}

		public boolean equals(Object obj)
		{
			if (obj == this)
			{
				return true;
			}
			
			BoxContents b = (BoxContents) obj;
			
			return  
				b.left == left && b.top == top;
		}

		public int hashCode()
		{
			return hashCode;
		}
	}
	
	protected static class StretchedContents
	{
		final int newHeight;
		final int newWidth;
		final int hashCode;
		final byte xPosition;
		final byte yPosition;
		
		StretchedContents(
				int newWidth, int newHeight, byte xPosition, byte yPosition)
		{
			this.newHeight = newHeight;
			this.newWidth = newWidth;
			this.xPosition = xPosition;
			this.yPosition = yPosition;
			
			int hash = newHeight;
			hash = 31*hash + newWidth;
			hash = 31*hash + xPosition;
			hash = 31*hash + yPosition;
			hashCode = hash;
		}
		
		public boolean equals(Object o)
		{
			if (o == this)
			{
				return true;
			}
			
			StretchedContents s = (StretchedContents) o;
			
			return  
				s.newHeight == newHeight && s.newWidth == newWidth &&
				s.xPosition == xPosition && s.yPosition == yPosition;
		}
		
		public int hashCode()
		{
			return hashCode;
		}
	}

	protected int getContainerHeight()
	{
		return getHeight() - getTopPadding() - getBottomPadding();
	}
	
	protected int getTopPadding()
	{
		return box == null ? 0 : box.getTopPadding();
	}
	
	protected int getBottomPadding()
	{
		return box == null ? 0 : box.getBottomPadding();
	}

	public JRCloneable createClone()
	{
		JRFillCloneFactory factory = new JRFillCloneFactory();
		return createClone(factory);
	}

	public JRCloneable createClone(JRFillCloneFactory factory)
	{
		return new JRFillCellContents(this, factory);
	}
	
	public JRFillCellContents getWorkingClone()
	{
		JRFillCellContents clone = (JRFillCellContents) clonePool.getClone();
		clone.original = this;
		return clone;
	}
	
	public void releaseWorkingClone()
	{
		original.clonePool.releaseClone(this);
	}

	public void setX(int x)
	{
		this.x = x;
	}

	public void setY(int y)
	{
		this.y = y;
	}

	public int getVerticalSpan()
	{
		return verticalSpan;
	}

	public void setVerticalSpan(int span)
	{
		verticalSpan = span;
	}

	public void setVerticalPositionType(byte positionType)
	{
		this.verticalPositionType = positionType;
	}

	protected void evaluate(byte evaluation) throws JRException
	{
		super.evaluate(evaluation);
		evaluateConditionalStyles(evaluation);
	}

	public JRDefaultStyleProvider getDefaultStyleProvider()
	{
		return parentCell.getDefaultStyleProvider();
	}

	public JRStyle getStyle()
	{
		return style;
	}

	protected void initConditionalStyles()
	{
		super.initConditionalStyles();
		collectConditionalStyle(style);
	}

	public Byte getMode()
	{
		return parentCell.getMode();
	}
}
