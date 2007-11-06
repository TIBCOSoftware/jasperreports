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

import net.sf.jasperreports.engine.JRBox;
import net.sf.jasperreports.engine.JRElement;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExpressionCollector;
import net.sf.jasperreports.engine.JRFrame;
import net.sf.jasperreports.engine.JRPrintElement;
import net.sf.jasperreports.engine.JRStyle;
import net.sf.jasperreports.engine.JRVisitor;
import net.sf.jasperreports.engine.base.JRBaseBox;
import net.sf.jasperreports.engine.base.JRBaseElementGroup;
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

	protected void evaluate(byte evaluation) throws JRException
	{
		reset();

		evaluatePrintWhenExpression(evaluation);

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
		int topPadding = first ? getTopPadding() : 0;
		int bottomPadding = getBottomPadding();		
		
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
		int frameElemsAvailableHeight = stretchHeight + bottomPadding + getTopPadding() - topPadding;
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
		
		int topPadding = first ? getTopPadding() : 0;
		int bottomPadding = fillBottomBorder ? getBottomPadding() : 0;		
		frameContainer.setStretchHeight(stretchHeight + frameContainer.getFirstY() - topPadding - bottomPadding);
	}
	
	
	protected void stretchHeightFinal()
	{
		frameContainer.stretchElements();
		frameContainer.moveBandBottomElements();
		frameContainer.removeBlankElements();

		int topPadding = first ? getTopPadding() : 0;
		int bottomPadding = fillBottomBorder ? getBottomPadding() : 0;
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
			if (first)
			{
				if (!fillBottomBorder) //remove the bottom border
				{				
					JRBox bottomBox = new JRBaseBox(this, false, false, false, true);
					boxTemplate.setBox(bottomBox);
				}
			}
			else
			{
				if (fillBottomBorder) //remove the top border
				{
					JRBox topBox = new JRBaseBox(this, false, false, true, false);
					boxTemplate.setBox(topBox);
				}
				else //remove the top and bottom borders
				{
					JRBox topBottomBox = new JRBaseBox(this, false, false, true, true);					
					boxTemplate.setBox(topBottomBox);
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
			return JRFillFrame.this.getHeight() - getTopPadding() - getBottomPadding();
		}
	}
	
	//box

	public byte getBorder()
	{
		return JRStyleResolver.getBorder(this);
	}

	public Byte getOwnBorder()
	{
		return parentFrame.getOwnBorder();
	}

	public void setBorder(byte border)
	{
	}

	public Color getBorderColor()
	{
		return JRStyleResolver.getBorderColor(this, getForecolor());
	}

	public Color getOwnBorderColor()
	{
		return parentFrame.getOwnBorderColor();
	}

	public void setBorderColor(Color borderColor)
	{
	}

	public int getPadding()
	{
		return JRStyleResolver.getPadding(this);
	}

	public Integer getOwnPadding()
	{
		return parentFrame.getOwnPadding();
	}

	public void setPadding(int padding)
	{
	}

	public byte getTopBorder()
	{
		return JRStyleResolver.getTopBorder(this);
	}

	public Byte getOwnTopBorder()
	{
		return parentFrame.getOwnTopBorder();
	}

	public void setTopBorder(byte topBorder)
	{
	}

	public Color getTopBorderColor()
	{
		return JRStyleResolver.getTopBorderColor(this, getForecolor());
	}

	public Color getOwnTopBorderColor()
	{
		return parentFrame.getOwnTopBorderColor();
	}

	public void setTopBorderColor(Color topBorderColor)
	{
	}

	public int getTopPadding()
	{
		return JRStyleResolver.getTopPadding(this);
	}

	public Integer getOwnTopPadding()
	{
		return parentFrame.getOwnTopPadding();
	}

	public void setTopPadding(int topPadding)
	{
	}

	public byte getLeftBorder()
	{
		return JRStyleResolver.getLeftBorder(this);
	}

	public Byte getOwnLeftBorder()
	{
		return parentFrame.getOwnLeftBorder();
	}

	public void setLeftBorder(byte leftBorder)
	{
	}

	public Color getLeftBorderColor()
	{
		return JRStyleResolver.getLeftBorderColor(this, getForecolor());
	}

	public Color getOwnLeftBorderColor()
	{
		return parentFrame.getOwnLeftBorderColor();
	}

	public void setLeftBorderColor(Color leftBorderColor)
	{
	}

	public int getLeftPadding()
	{
		return JRStyleResolver.getLeftPadding(this);
	}

	public Integer getOwnLeftPadding()
	{
		return parentFrame.getOwnLeftPadding();
	}

	public void setLeftPadding(int leftPadding)
	{
	}

	public byte getBottomBorder()
	{
		return JRStyleResolver.getBottomBorder(this);
	}

	public Byte getOwnBottomBorder()
	{
		return parentFrame.getOwnBottomBorder();
	}

	public void setBottomBorder(byte bottomBorder)
	{
	}

	public Color getBottomBorderColor()
	{
		return JRStyleResolver.getBottomBorderColor(this, getForecolor());
	}

	public Color getOwnBottomBorderColor()
	{
		return parentFrame.getOwnBottomBorderColor();
	}

	public void setBottomBorderColor(Color bottomBorderColor)
	{
	}

	public int getBottomPadding()
	{
		return JRStyleResolver.getBottomPadding(this);
	}

	public Integer getOwnBottomPadding()
	{
		return parentFrame.getOwnBottomPadding();
	}

	public void setBottomPadding(int bottomPadding)
	{
	}

	public byte getRightBorder()
	{
		return JRStyleResolver.getRightBorder(this);
	}

	public Byte getOwnRightBorder()
	{
		return parentFrame.getOwnRightBorder();
	}

	public void setRightBorder(byte rightBorder)
	{
	}

	public Color getRightBorderColor()
	{
		return JRStyleResolver.getRightBorderColor(this, getForecolor());
	}

	public Color getOwnRightBorderColor()
	{
		return parentFrame.getOwnRightBorderColor();
	}

	public void setRightBorderColor(Color rightBorderColor)
	{
	}

	public int getRightPadding()
	{
		return JRStyleResolver.getRightPadding(this);
	}

	public Integer getOwnRightPadding()
	{
		return parentFrame.getOwnRightPadding();
	}

	public void setRightPadding(int rightPadding)
	{
	}

	public void setBorder(Byte border)
	{
	}

	public void setPadding(Integer padding)
	{
	}

	public void setTopBorder(Byte topBorder)
	{
	}

	public void setTopPadding(Integer topPadding)
	{
	}

	public void setLeftBorder(Byte leftBorder)
	{
	}

	public void setLeftPadding(Integer leftPadding)
	{
	}

	public void setBottomBorder(Byte bottomBorder)
	{
	}

	public void setBottomPadding(Integer bottomPadding)
	{
	}

	public void setRightBorder(Byte rightBorder)
	{
	}

	public void setRightPadding(Integer rightPadding)
	{
	}
}
