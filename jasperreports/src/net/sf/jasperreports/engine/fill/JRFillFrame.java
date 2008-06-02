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
import java.util.List;
import java.util.Map;

import net.sf.jasperreports.engine.JRElement;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExpressionCollector;
import net.sf.jasperreports.engine.JRFrame;
import net.sf.jasperreports.engine.JRLineBox;
import net.sf.jasperreports.engine.JRPrintElement;
import net.sf.jasperreports.engine.JRStyle;
import net.sf.jasperreports.engine.JRVisitor;
import net.sf.jasperreports.engine.base.JRBaseElementGroup;
import net.sf.jasperreports.engine.util.JRBoxUtil;
import net.sf.jasperreports.engine.util.JRPenUtil;
import net.sf.jasperreports.engine.util.JRStyleResolver;

/**
 * Fill time implementation of a frame element.
 * 
 * @author Lucian Chirita (lucianc@users.sourceforge.net)
 * @version $Id$
 */
public class JRFillFrame extends JRFillElement implements JRFrame
{
	protected final JRFrame parentFrame;
	
	protected final JRLineBox lineBox;
	
	/**
	 * Element container used for filling.
	 */
	private JRFillFrameElements frameContainer;
	
	/**
	 * Template frame without the bottom border.
	 */
	private Map bottomTemplateFrames;
	
	/**
	 * Template frame without the top border
	 */
	private Map topTemplateFrames;
	
	/**
	 * Template frame without the top and bottom borders
	 */
	private Map topBottomTemplateFrames;
	
	/**
	 * Whether the current frame chunk is the first one.
	 */
	private boolean first;
	
	private boolean fillBottomBorder;
	
	/**
	 * Whether the frame has started filling and not ended.
	 */
	private boolean filling;

	public JRFillFrame(JRBaseFiller filler, JRFrame frame, JRFillObjectFactory factory)
	{
		super(filler, frame, factory);
		
		parentFrame = frame;
		
		lineBox = frame.getLineBox().clone(this);
		
		frameContainer = new JRFillFrameElements(factory);
		
		bottomTemplateFrames = new HashMap();
		topTemplateFrames = new HashMap();
		topBottomTemplateFrames = new HashMap();
		
		setShrinkable(true);
	}

	protected JRFillFrame(JRFillFrame frame, JRFillCloneFactory factory)
	{
		super(frame, factory);
		
		parentFrame = frame.parentFrame;
		
		lineBox = frame.getLineBox().clone(this);
		
		frameContainer = new JRFillFrameElements(frame.frameContainer, factory);
		
		bottomTemplateFrames = frame.bottomTemplateFrames;
		topTemplateFrames = frame.topTemplateFrames;
		topBottomTemplateFrames = frame.topBottomTemplateFrames;
	}

	/**
	 *
	 */
	public byte getMode()
	{
		return JRStyleResolver.getMode(this, MODE_TRANSPARENT);
	}

	/**
	 * 
	 */
	public Color getDefaultLineColor() 
	{
		return getForecolor();
	}

	
	protected void evaluate(byte evaluation) throws JRException
	{
		reset();

		evaluatePrintWhenExpression(evaluation);
		evaluateProperties(evaluation);

		if (isPrintWhenExpressionNull() || isPrintWhenTrue())
		{
			frameContainer.evaluate(evaluation);
			
			boolean repeating = true;
			JRFillElement[] elements = (JRFillElement[]) getElements();
			for (int i = 0; repeating && i < elements.length; i++)
			{
				repeating &= elements[i].isValueRepeating();
			}
			setValueRepeating(repeating);
		}
		
		filling = false;
	}

	protected void rewind() throws JRException
	{
		frameContainer.rewind();
		
		filling = false;
	}

	protected boolean prepare(int availableStretchHeight, boolean isOverflow) throws JRException
	{
		super.prepare(availableStretchHeight, isOverflow);

		if (!isToPrint())
		{
			return false;
		}
		
		first = !isOverflow || !filling;
		int topPadding = first ? getLineBox().getTopPadding().intValue() : 0;
		int bottomPadding = getLineBox().getBottomPadding().intValue();		
		
		if (availableStretchHeight < getRelativeY() - getY() - getBandBottomY() - topPadding)
		{
			setToPrint(false);
			return true;
		}
		
		if (!filling && !isPrintRepeatedValues() && isValueRepeating() &&
				(!isPrintInFirstWholeBand() || !getBand().isFirstWholeOnPageColumn()) &&
				(getPrintWhenGroupChanges() == null || !getBand().isNewGroup(getPrintWhenGroupChanges())) &&
				(!isOverflow || !isPrintWhenDetailOverflows())
			)
		{
			setToPrint(false);
			return false;
		}

		// FIXME reprinted when isAlreadyPrinted() || !isPrintRepeatedValues()?
		if (!filling && isOverflow && isAlreadyPrinted())
		{
			if (isPrintWhenDetailOverflows())
			{
				rewind();
				setReprinted(true);
			}
			else
			{
				setToPrint(false);
				return false;
			}
		}
		
		int stretchHeight = availableStretchHeight - getRelativeY() + getY() + getBandBottomY();
		
		frameContainer.initFill();
		frameContainer.resetElements();
		int frameElemsAvailableHeight = stretchHeight + bottomPadding + getLineBox().getTopPadding().intValue() - topPadding;
		frameContainer.prepareElements(frameElemsAvailableHeight, true);
		
		boolean willOverflow = frameContainer.willOverflow();
		if (willOverflow)
		{
			fillBottomBorder = false;
			setStretchHeight(getHeight() + stretchHeight);
		}
		else
		{
			int neededStretch = frameContainer.getStretchHeight() - frameContainer.getFirstY() + topPadding + bottomPadding;
			if (neededStretch <= getHeight() + stretchHeight)
			{
				fillBottomBorder = true;
				setStretchHeight(neededStretch);
			}
			else //don't overflow because of the bottom padding
			{
				fillBottomBorder = false;
				setStretchHeight(getHeight() + stretchHeight);
			}
		}

		filling = willOverflow;

		return willOverflow;
	}

	protected void setStretchHeight(int stretchHeight)
	{
		super.setStretchHeight(stretchHeight);
		
		int topPadding = first ? getLineBox().getTopPadding().intValue() : 0;
		int bottomPadding = fillBottomBorder ? getLineBox().getBottomPadding().intValue() : 0;		
		frameContainer.setStretchHeight(stretchHeight + frameContainer.getFirstY() - topPadding - bottomPadding);
	}
	
	
	protected void stretchHeightFinal()
	{
		frameContainer.stretchElements();
		frameContainer.moveBandBottomElements();
		frameContainer.removeBlankElements();

		int topPadding = first ? getLineBox().getTopPadding().intValue() : 0;
		int bottomPadding = fillBottomBorder ? getLineBox().getBottomPadding().intValue() : 0;
		super.setStretchHeight(frameContainer.getStretchHeight() - frameContainer.getFirstY() + topPadding + bottomPadding);
	}


	protected JRPrintElement fill() throws JRException
	{		
		JRTemplatePrintFrame printFrame = new JRTemplatePrintFrame(getTemplate());
		printFrame.setX(getX());
		printFrame.setY(getRelativeY());
		printFrame.setWidth(getWidth());
		
		frameContainer.fillElements(printFrame);
		
		printFrame.setHeight(getStretchHeight());
		transferProperties(printFrame);
		
		return printFrame;
	}

	protected JRTemplateFrame getTemplate()
	{
		JRStyle style = getStyle();

		Map templatesMap;
		if (first)
		{
			if (fillBottomBorder)
			{
				templatesMap = templates;
			}
			else //remove the bottom border
			{
				templatesMap = bottomTemplateFrames;
			}
		}
		else
		{
			if (fillBottomBorder) //remove the top border
			{
				templatesMap = topTemplateFrames;
			}
			else //remove the top and bottom borders
			{
				templatesMap = topBottomTemplateFrames;
			}
		}
		
		JRTemplateFrame boxTemplate = (JRTemplateFrame) templatesMap.get(style);
		if (boxTemplate == null)
		{
			boxTemplate = 
				new JRTemplateFrame(
					band == null ? null : band.getOrigin(), 
					filler.getJasperPrint().getDefaultStyleProvider(), 
					this);
			transferProperties(boxTemplate);
			if (first)
			{
				if (!fillBottomBorder) //remove the bottom border
				{				
					boxTemplate.copyBox(getLineBox());
					JRBoxUtil.reset(boxTemplate.getLineBox(), false, false, false, true);
				}
			}
			else
			{
				if (fillBottomBorder) //remove the top border
				{
					boxTemplate.copyBox(getLineBox());
					JRBoxUtil.reset(boxTemplate.getLineBox(), false, false, true, false);
				}
				else //remove the top and bottom borders
				{
					boxTemplate.copyBox(getLineBox());
					JRBoxUtil.reset(boxTemplate.getLineBox(), false, false, true, true);					
				}
			}
			
			templatesMap.put(style, boxTemplate);
		}
		
		return boxTemplate;
	}

	protected void resolveElement(JRPrintElement element, byte evaluation)
	{
		// nothing
	}

	public JRElement[] getElements()
	{
		return frameContainer.getElements();
	}
	
	public List getChildren()
	{
		return frameContainer.getChildren();
	}

	public void collectExpressions(JRExpressionCollector collector)
	{
		collector.collect(this);
	}

	/**
	 *
	 */
	public JRLineBox getLineBox()
	{
		return lineBox;
	}
	
	/**
	 * @deprecated Replaced by {@link #getLineBox()}
	 */
	public byte getBorder()
	{
		return JRPenUtil.getPenFromLinePen(getLineBox().getPen());
	}

	/**
	 * @deprecated Replaced by {@link #getLineBox()}
	 */
	public Byte getOwnBorder()
	{
		return JRPenUtil.getOwnPenFromLinePen(getLineBox().getPen());
	}

	/**
	 * @deprecated Replaced by {@link #getLineBox()}
	 */
	public void setBorder(byte border)
	{
		JRPenUtil.setLinePenFromPen(border, getLineBox().getPen());
	}

	/**
	 * @deprecated Replaced by {@link #getLineBox()}
	 */
	public void setBorder(Byte border)
	{
		JRPenUtil.setLinePenFromPen(border, getLineBox().getPen());
	}

	/**
	 * @deprecated Replaced by {@link #getLineBox()}
	 */
	public Color getBorderColor()
	{
		return getLineBox().getPen().getLineColor();
	}

	/**
	 * @deprecated Replaced by {@link #getLineBox()}
	 */
	public Color getOwnBorderColor()
	{
		return getLineBox().getPen().getOwnLineColor();
	}

	/**
	 * @deprecated Replaced by {@link #getLineBox()}
	 */
	public void setBorderColor(Color borderColor)
	{
		getLineBox().getPen().setLineColor(borderColor);
	}

	/**
	 * @deprecated Replaced by {@link #getLineBox()}
	 */
	public int getPadding()
	{
		return getLineBox().getPadding().intValue();
	}

	/**
	 * @deprecated Replaced by {@link #getLineBox()}
	 */
	public Integer getOwnPadding()
	{
		return getLineBox().getOwnPadding();
	}

	/**
	 * @deprecated Replaced by {@link #getLineBox()}
	 */
	public void setPadding(int padding)
	{
		getLineBox().setPadding(padding);
	}

	/**
	 * @deprecated Replaced by {@link #getLineBox()}
	 */
	public void setPadding(Integer padding)
	{
		getLineBox().setPadding(padding);
	}

	/**
	 * @deprecated Replaced by {@link #getLineBox()}
	 */
	public byte getTopBorder()
	{
		return JRPenUtil.getPenFromLinePen(getLineBox().getTopPen());
	}

	/**
	 * @deprecated Replaced by {@link #getLineBox()}
	 */
	public Byte getOwnTopBorder()
	{
		return JRPenUtil.getOwnPenFromLinePen(getLineBox().getTopPen());
	}

	/**
	 * @deprecated Replaced by {@link #getLineBox()}
	 */
	public void setTopBorder(byte topBorder)
	{
		JRPenUtil.setLinePenFromPen(topBorder, getLineBox().getTopPen());
	}

	/**
	 * @deprecated Replaced by {@link #getLineBox()}
	 */
	public void setTopBorder(Byte topBorder)
	{
		JRPenUtil.setLinePenFromPen(topBorder, getLineBox().getTopPen());
	}

	/**
	 * @deprecated Replaced by {@link #getLineBox()}
	 */
	public Color getTopBorderColor()
	{
		return getLineBox().getTopPen().getLineColor();
	}

	/**
	 * @deprecated Replaced by {@link #getLineBox()}
	 */
	public Color getOwnTopBorderColor()
	{
		return getLineBox().getTopPen().getOwnLineColor();
	}

	/**
	 * @deprecated Replaced by {@link #getLineBox()}
	 */
	public void setTopBorderColor(Color topBorderColor)
	{
		getLineBox().getTopPen().setLineColor(topBorderColor);
	}

	/**
	 * @deprecated Replaced by {@link #getLineBox()}
	 */
	public int getTopPadding()
	{
		return getLineBox().getTopPadding().intValue();
	}

	/**
	 * @deprecated Replaced by {@link #getLineBox()}
	 */
	public Integer getOwnTopPadding()
	{
		return getLineBox().getOwnTopPadding();
	}

	/**
	 * @deprecated Replaced by {@link #getLineBox()}
	 */
	public void setTopPadding(int topPadding)
	{
		getLineBox().setTopPadding(topPadding);
	}

	/**
	 * @deprecated Replaced by {@link #getLineBox()}
	 */
	public void setTopPadding(Integer topPadding)
	{
		getLineBox().setTopPadding(topPadding);
	}

	/**
	 * @deprecated Replaced by {@link #getLineBox()}
	 */
	public byte getLeftBorder()
	{
		return JRPenUtil.getPenFromLinePen(getLineBox().getLeftPen());
	}

	/**
	 * @deprecated Replaced by {@link #getLineBox()}
	 */
	public Byte getOwnLeftBorder()
	{
		return JRPenUtil.getOwnPenFromLinePen(getLineBox().getLeftPen());
	}

	/**
	 * @deprecated Replaced by {@link #getLineBox()}
	 */
	public void setLeftBorder(byte leftBorder)
	{
		JRPenUtil.setLinePenFromPen(leftBorder, getLineBox().getLeftPen());
	}

	/**
	 * @deprecated Replaced by {@link #getLineBox()}
	 */
	public void setLeftBorder(Byte leftBorder)
	{
		JRPenUtil.setLinePenFromPen(leftBorder, getLineBox().getLeftPen());
	}

	/**
	 * @deprecated Replaced by {@link #getLineBox()}
	 */
	public Color getLeftBorderColor()
	{
		return getLineBox().getLeftPen().getLineColor();
	}

	/**
	 * @deprecated Replaced by {@link #getLineBox()}
	 */
	public Color getOwnLeftBorderColor()
	{
		return getLineBox().getLeftPen().getOwnLineColor();
	}

	/**
	 * @deprecated Replaced by {@link #getLineBox()}
	 */
	public void setLeftBorderColor(Color leftBorderColor)
	{
		getLineBox().getLeftPen().setLineColor(leftBorderColor);
	}

	/**
	 * @deprecated Replaced by {@link #getLineBox()}
	 */
	public int getLeftPadding()
	{
		return getLineBox().getLeftPadding().intValue();
	}

	/**
	 * @deprecated Replaced by {@link #getLineBox()}
	 */
	public Integer getOwnLeftPadding()
	{
		return getLineBox().getOwnLeftPadding();
	}

	/**
	 * @deprecated Replaced by {@link #getLineBox()}
	 */
	public void setLeftPadding(int leftPadding)
	{
		getLineBox().setLeftPadding(leftPadding);
	}

	/**
	 * @deprecated Replaced by {@link #getLineBox()}
	 */
	public void setLeftPadding(Integer leftPadding)
	{
		getLineBox().setLeftPadding(leftPadding);
	}

	/**
	 * @deprecated Replaced by {@link #getLineBox()}
	 */
	public byte getBottomBorder()
	{
		return JRPenUtil.getPenFromLinePen(getLineBox().getBottomPen());
	}

	/**
	 * @deprecated Replaced by {@link #getLineBox()}
	 */
	public Byte getOwnBottomBorder()
	{
		return JRPenUtil.getOwnPenFromLinePen(getLineBox().getBottomPen());
	}

	/**
	 * @deprecated Replaced by {@link #getLineBox()}
	 */
	public void setBottomBorder(byte bottomBorder)
	{
		JRPenUtil.setLinePenFromPen(bottomBorder, getLineBox().getBottomPen());
	}

	/**
	 * @deprecated Replaced by {@link #getLineBox()}
	 */
	public void setBottomBorder(Byte bottomBorder)
	{
		JRPenUtil.setLinePenFromPen(bottomBorder, getLineBox().getBottomPen());
	}

	/**
	 * @deprecated Replaced by {@link #getLineBox()}
	 */
	public Color getBottomBorderColor()
	{
		return getLineBox().getBottomPen().getLineColor();
	}

	/**
	 * @deprecated Replaced by {@link #getLineBox()}
	 */
	public Color getOwnBottomBorderColor()
	{
		return getLineBox().getBottomPen().getOwnLineColor();
	}

	/**
	 * @deprecated Replaced by {@link #getLineBox()}
	 */
	public void setBottomBorderColor(Color bottomBorderColor)
	{
		getLineBox().getBottomPen().setLineColor(bottomBorderColor);
	}

	/**
	 * @deprecated Replaced by {@link #getLineBox()}
	 */
	public int getBottomPadding()
	{
		return getLineBox().getBottomPadding().intValue();
	}

	/**
	 * @deprecated Replaced by {@link #getLineBox()}
	 */
	public Integer getOwnBottomPadding()
	{
		return getLineBox().getOwnBottomPadding();
	}

	/**
	 * @deprecated Replaced by {@link #getLineBox()}
	 */
	public void setBottomPadding(int bottomPadding)
	{
		getLineBox().setBottomPadding(bottomPadding);
	}

	/**
	 * @deprecated Replaced by {@link #getLineBox()}
	 */
	public void setBottomPadding(Integer bottomPadding)
	{
		getLineBox().setBottomPadding(bottomPadding);
	}

	/**
	 * @deprecated Replaced by {@link #getLineBox()}
	 */
	public byte getRightBorder()
	{
		return JRPenUtil.getPenFromLinePen(getLineBox().getRightPen());
	}

	/**
	 * @deprecated Replaced by {@link #getLineBox()}
	 */
	public Byte getOwnRightBorder()
	{
		return JRPenUtil.getOwnPenFromLinePen(getLineBox().getRightPen());
	}

	/**
	 * @deprecated Replaced by {@link #getLineBox()}
	 */
	public void setRightBorder(byte rightBorder)
	{
		JRPenUtil.setLinePenFromPen(rightBorder, getLineBox().getRightPen());
	}

	/**
	 * @deprecated Replaced by {@link #getLineBox()}
	 */
	public void setRightBorder(Byte rightBorder)
	{
		JRPenUtil.setLinePenFromPen(rightBorder, getLineBox().getRightPen());
	}

	/**
	 * @deprecated Replaced by {@link #getLineBox()}
	 */
	public Color getRightBorderColor()
	{
		return getLineBox().getRightPen().getLineColor();
	}

	/**
	 * @deprecated Replaced by {@link #getLineBox()}
	 */
	public Color getOwnRightBorderColor()
	{
		return getLineBox().getRightPen().getOwnLineColor();
	}

	/**
	 * @deprecated Replaced by {@link #getLineBox()}
	 */
	public void setRightBorderColor(Color rightBorderColor)
	{
		getLineBox().getRightPen().setLineColor(rightBorderColor);
	}

	/**
	 * @deprecated Replaced by {@link #getLineBox()}
	 */
	public int getRightPadding()
	{
		return getLineBox().getRightPadding().intValue();
	}

	/**
	 * @deprecated Replaced by {@link #getLineBox()}
	 */
	public Integer getOwnRightPadding()
	{
		return getLineBox().getOwnRightPadding();
	}

	/**
	 * @deprecated Replaced by {@link #getLineBox()}
	 */
	public void setRightPadding(int rightPadding)
	{
		getLineBox().setRightPadding(rightPadding);
	}

	/**
	 * @deprecated Replaced by {@link #getLineBox()}
	 */
	public void setRightPadding(Integer rightPadding)
	{
		getLineBox().setRightPadding(rightPadding);
	}

	/**
	 *
	 */
	public void visit(JRVisitor visitor)
	{
		visitor.visitFrame(this);
	}
	
	
	public JRElement getElementByKey(String key)
	{
		return JRBaseElementGroup.getElementByKey(getElements(), key);
	}

	public JRFillCloneable createClone(JRFillCloneFactory factory)
	{
		return new JRFillFrame(this, factory);
	}
	

	/**
	 * Frame element container filler.
	 */
	protected class JRFillFrameElements extends JRFillElementContainer
	{
		JRFillFrameElements(JRFillObjectFactory factory)
		{
			super(JRFillFrame.this.filler, parentFrame, factory);
			initElements();
		}

		JRFillFrameElements(JRFillFrameElements frameElements, JRFillCloneFactory factory)
		{
			super(frameElements, factory);
			initElements();
		}

		protected int getContainerHeight()
		{
			return JRFillFrame.this.getHeight() - getLineBox().getTopPadding().intValue() - getLineBox().getBottomPadding().intValue();
		}
	}
	
}
