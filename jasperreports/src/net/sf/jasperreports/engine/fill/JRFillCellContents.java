/*
 * ============================================================================
 * GNU Lesser General Public License
 * ============================================================================
 *
 * JasperReports - Free Java report-generating library.
 * Copyright (C) 2001-2006 JasperSoft Corporation http://www.jaspersoft.com
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
import net.sf.jasperreports.crosstabs.fill.JRFillCrosstabObjectFactory;
import net.sf.jasperreports.engine.JRBox;
import net.sf.jasperreports.engine.JRDefaultStyleProvider;
import net.sf.jasperreports.engine.JRElement;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRFrame;
import net.sf.jasperreports.engine.JRLineBox;
import net.sf.jasperreports.engine.JROrigin;
import net.sf.jasperreports.engine.JRPrintElement;
import net.sf.jasperreports.engine.JRPrintFrame;
import net.sf.jasperreports.engine.JRStyle;
import net.sf.jasperreports.engine.JRStyleSetter;
import net.sf.jasperreports.engine.util.LineBoxWrapper;

import org.apache.commons.collections.ReferenceMap;

/**
 * Crosstab cell contents filler.
 * 
 * @author Lucian Chirita (lucianc@users.sourceforge.net)
 * @version $Id$
 */
public class JRFillCellContents extends JRFillElementContainer implements JRCellContents, JRFillCloneable, JRStyleSetter
{
	private final Map transformedContentsCache;
	private final Map boxContentsCache;
	private final JRClonePool clonePool;
	private final JROriginProvider originProvider;
	
	private JRFillCellContents original;
	
	private final JRCellContents parentCell;
	
	private JRLineBox lineBox;
	
	private int height;
	private int width;
	
	private int x;
	private int y;
	private int verticalSpan;
	private byte verticalPositionType = JRCellContents.POSITION_Y_TOP;
	
	private Map templateFrames;
	
	private JRDefaultStyleProvider defaultStyleProvider;
	private JRStyle initStyle;

	public JRFillCellContents(JRBaseFiller filler, JRCellContents cell, JRFillCrosstabObjectFactory factory)
	{
		super(filler, cell, factory);
		
		defaultStyleProvider = factory.getDefaultStyleProvider();
		
		parentCell = cell;
		
		lineBox = cell.getLineBox().clone(this);
		
		width = cell.getWidth();
		height = cell.getHeight();
		
		factory.registerDelayedStyleSetter(this, parentCell);
		
		initElements();
		
		initConditionalStyles();
		
		initTemplatesMap();
		
		this.originProvider = factory.getParentOriginProvider();
		setElementOriginProvider(this.originProvider);
		
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
		
		defaultStyleProvider = cellContents.defaultStyleProvider;
		
		parentCell = cellContents.parentCell;
		
		lineBox = cellContents.getLineBox().clone(this);
		
		width = cellContents.width;
		height = cellContents.height;
		
		initStyle = cellContents.initStyle;
		
		initElements();
		
		initConditionalStyles();
		
		this.templateFrames = cellContents.templateFrames;
		
		this.originProvider = cellContents.originProvider;
		
		transformedContentsCache = new ReferenceMap();
		boxContentsCache = new HashMap();
		clonePool = new JRClonePool(this, true, true);
		
		verticalPositionType = cellContents.verticalPositionType;
	}

	public Color getBackcolor()
	{
		return parentCell.getBackcolor();
	}

	/**
	 * @deprecated Replaced by {@link #getLineBox()}
	 */
	public JRBox getBox()
	{
		return new LineBoxWrapper(getLineBox());
	}

	/**
	 *
	 */
	public JRLineBox getLineBox()
	{
		return lineBox;
	}

	/**
	 * 
	 */
	protected void setBox(JRLineBox box)
	{
		this.lineBox = box;
		
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
	
	
	public JRFillCellContents getBoxContents(boolean left, boolean right, boolean top)
	{
		if (lineBox == null)
		{
			return this;
		}
		
		boolean copyLeft = left && lineBox.getLeftPen().getLineWidth().floatValue() <= 0f && lineBox.getRightPen().getLineWidth().floatValue() > 0f;
		boolean copyRight = right && lineBox.getRightPen().getLineWidth().floatValue() <= 0f && lineBox.getLeftPen().getLineWidth().floatValue() > 0f;
		boolean copyTop = top && lineBox.getTopPen().getLineWidth().floatValue() <= 0f && lineBox.getBottomPen().getLineWidth().floatValue() > 0f;
		
		if (!(copyLeft || copyRight || copyTop))
		{
			return this;
		}
		
		Object key = new BoxContents(copyLeft, copyRight, copyTop);
		JRFillCellContents boxContents = (JRFillCellContents) boxContentsCache.get(key);
		if (boxContents == null)
		{
			boxContents = (JRFillCellContents) createClone();
			
			JRLineBox newBox = (JRLineBox)lineBox.clone(this);
			
			if (copyLeft)
			{
				newBox.copyLeftPen(lineBox.getRightPen());
			}
			
			if (copyRight)
			{
				newBox.copyRightPen(lineBox.getLeftPen());
			}
			
			if (copyTop)
			{
				newBox.copyTopPen(lineBox.getBottomPen());
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
		//set bandBottomY again as getContainerHeight() might have changed
		setElementsBandBottomY();
		
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

	
	protected JRTemplateFrame getTemplateFrame()
	{
		JRStyle style = getStyle();
		JRTemplateFrame template = (JRTemplateFrame) templateFrames.get(style);
		if (template == null)
		{
			template = new JRTemplateFrame(getOrigin(), 
					filler.getJasperPrint().getDefaultStyleProvider(), this);
			templateFrames.put(style, template);
		}
		return template;
	}

	protected JROrigin getOrigin()
	{
		return originProvider == null ? null : originProvider.getOrigin();
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
		final boolean right;
		final boolean top;
		final int hashCode;
		
		public BoxContents(boolean left, boolean right, boolean top)
		{
			this.left = left;
			this.right = right;
			this.top = top;
			
			int hash = left ? 1231 : 1237;
			hash = 31*hash + (right ? 1231 : 1237);
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
				b.left == left && b.right == right && b.top == top;
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
		return lineBox == null ? 0 : lineBox.getTopPadding().intValue();
	}
	
	protected int getBottomPadding()
	{
		return lineBox == null ? 0 : lineBox.getBottomPadding().intValue();
	}

	public JRFillCloneable createClone()
	{
		JRFillCloneFactory factory = new JRFillCloneFactory();
		return createClone(factory);
	}

	public JRFillCloneable createClone(JRFillCloneFactory factory)
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
		evaluateConditionalStyles(evaluation);
		super.evaluate(evaluation);
	}

	public JRDefaultStyleProvider getDefaultStyleProvider()
	{
		return defaultStyleProvider;
	}

	public JRStyle getStyle()
	{
		JRStyle crtStyle = initStyle;
		
		boolean isUsingDefaultStyle = false;

		if (crtStyle == null)
		{
			crtStyle = filler.getDefaultStyle();
			isUsingDefaultStyle = true;
		}

		JRStyle evalStyle = getEvaluatedConditionalStyle(crtStyle);
		
		if (isUsingDefaultStyle && evalStyle == crtStyle)
			evalStyle = null;
		
		return evalStyle;
	}

	protected void initConditionalStyles()
	{
		super.initConditionalStyles();
		collectConditionalStyle(initStyle);
	}

	public Byte getMode()
	{
		return parentCell.getMode();
	}

	public String getStyleNameReference()
	{
		return null;
	}

	public void setStyle(JRStyle style)
	{
		this.initStyle = style;
		collectConditionalStyle(style);
	}

	public void setStyleNameReference(String name)
	{
		throw new UnsupportedOperationException("Style name references not allowed at fill time");
	}

	/**
	 * 
	 */
	public Color getDefaultLineColor() 
	{
		return parentCell.getDefaultLineColor();
	}

}
