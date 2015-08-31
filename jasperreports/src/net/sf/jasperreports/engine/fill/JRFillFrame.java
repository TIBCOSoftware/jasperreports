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
package net.sf.jasperreports.engine.fill;

import java.awt.Color;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.jasperreports.engine.JRChild;
import net.sf.jasperreports.engine.JRElement;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExpressionCollector;
import net.sf.jasperreports.engine.JRFrame;
import net.sf.jasperreports.engine.JRLineBox;
import net.sf.jasperreports.engine.JRPrintElement;
import net.sf.jasperreports.engine.JRStyle;
import net.sf.jasperreports.engine.JRVisitor;
import net.sf.jasperreports.engine.base.JRBaseElementGroup;
import net.sf.jasperreports.engine.type.BorderSplitType;
import net.sf.jasperreports.engine.type.ModeEnum;
import net.sf.jasperreports.engine.util.ElementsVisitorUtils;
import net.sf.jasperreports.engine.util.JRBoxUtil;
import net.sf.jasperreports.engine.util.JRStyleResolver;

/**
 * Fill time implementation of a frame element.
 * 
 * @author Lucian Chirita (lucianc@users.sourceforge.net)
 */
public class JRFillFrame extends JRFillElement implements JRFrame
{
	protected final JRFrame parentFrame;
	
	protected final JRLineBox lineBox;
	
	protected final BorderSplitType borderSplitType;
	
	/**
	 * Element container used for filling.
	 */
	private JRFillFrameElements frameContainer;
	
	/**
	 * Template frame without the bottom border.
	 */
	private Map<JRStyle,JRTemplateElement> bottomTemplateFrames;
	
	/**
	 * Template frame without the top border
	 */
	private Map<JRStyle,JRTemplateElement> topTemplateFrames;
	
	/**
	 * Template frame without the top and bottom borders
	 */
	private Map<JRStyle,JRTemplateElement> topBottomTemplateFrames;
	
	private boolean fillTopBorder;
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
		borderSplitType = initBorderSplitType(filler, frame);
		
		frameContainer = new JRFillFrameElements(factory);
		
		bottomTemplateFrames = new HashMap<JRStyle,JRTemplateElement>();
		topTemplateFrames = new HashMap<JRStyle,JRTemplateElement>();
		topBottomTemplateFrames = new HashMap<JRStyle,JRTemplateElement>();
		
		setShrinkable(true);
	}

	protected JRFillFrame(JRFillFrame frame, JRFillCloneFactory factory)
	{
		super(frame, factory);
		
		parentFrame = frame.parentFrame;
		
		lineBox = frame.getLineBox().clone(this);
		borderSplitType = frame.borderSplitType;
		
		frameContainer = new JRFillFrameElements(frame.frameContainer, factory);
		
		bottomTemplateFrames = frame.bottomTemplateFrames;
		topTemplateFrames = frame.topTemplateFrames;
		topBottomTemplateFrames = frame.topBottomTemplateFrames;
	}
	
	private BorderSplitType initBorderSplitType(JRBaseFiller filler, JRFrame frame)
	{
		BorderSplitType splitType = frame.getBorderSplitType();
		if (splitType == null)
		{
			String splitTypeProp = filler.getPropertiesUtil().getProperty(filler.getJasperReport(), PROPERTY_BORDER_SPLIT_TYPE);
			if (splitTypeProp != null)
			{
				splitType = BorderSplitType.byName(splitTypeProp);
			}
		}
		return splitType;
	}

	/**
	 *
	 */
	public ModeEnum getModeValue()
	{
		return JRStyleResolver.getMode(this, ModeEnum.TRANSPARENT);
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
		if (isPrintWhenExpressionNull() || isPrintWhenTrue())
		{
			evaluateProperties(evaluation);
			evaluateStyle(evaluation);

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

	protected boolean drawTopBorderOnSplit()
	{
		return borderSplitType == BorderSplitType.DRAW_BORDERS;
	}

	protected boolean drawBotomBorderOnSplit()
	{
		return borderSplitType == BorderSplitType.DRAW_BORDERS;
	}
	
	protected boolean prepare(int availableHeight, boolean isOverflow) throws JRException
	{
		super.prepare(availableHeight, isOverflow);

		if (!isToPrint())
		{
			return false;
		}
		
		// whether the current frame chunk is the first one.
		boolean first = !isOverflow || !filling;
		
		int topPadding = getLineBox().getTopPadding().intValue();
		int bottomPadding = getLineBox().getBottomPadding().intValue();		
		
		if (availableHeight < getRelativeY() + getHeight() - topPadding - bottomPadding)
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
		
		frameContainer.initFill();
		frameContainer.resetElements();
		
		frameContainer.prepareElements(availableHeight - getRelativeY() - topPadding - bottomPadding, true);
		
		boolean willOverflow = frameContainer.willOverflow();
		fillTopBorder = first || drawTopBorderOnSplit();
		fillBottomBorder = !willOverflow || drawBotomBorderOnSplit();
		
		if (willOverflow)
		{
			setStretchHeight(availableHeight - getRelativeY());
		}
		else
		{
			int neededStretch = frameContainer.getStretchHeight() - frameContainer.getFirstY() + topPadding + bottomPadding;
			if (neededStretch <= availableHeight - getRelativeY()) 
			{
				setStretchHeight(neededStretch);
			}
			else
			{
				//FIXME is this case possible?
				setStretchHeight(availableHeight - getRelativeY());
			}
		}

		filling = willOverflow;

		return willOverflow;
	}

	protected void setStretchHeight(int stretchHeight)
	{
		super.setStretchHeight(stretchHeight);
		
		int topPadding = getLineBox().getTopPadding().intValue();
		int bottomPadding = getLineBox().getBottomPadding().intValue();		
		frameContainer.setStretchHeight(stretchHeight + frameContainer.getFirstY() - topPadding - bottomPadding);
	}
	
	
	protected void stretchHeightFinal()
	{
		// only do this if the frame is printing
		if (isToPrint())
		{
			frameContainer.stretchElements();
			frameContainer.moveBandBottomElements();
			frameContainer.removeBlankElements();

			int topPadding = getLineBox().getTopPadding().intValue();
			int bottomPadding = getLineBox().getBottomPadding().intValue();
			super.setStretchHeight(frameContainer.getStretchHeight() - frameContainer.getFirstY() + topPadding + bottomPadding);
		}
	}


	protected JRPrintElement fill() throws JRException
	{		
		JRTemplatePrintFrame printFrame = new JRTemplatePrintFrame(getTemplate(), printElementOriginator);
		printFrame.setUUID(getUUID());
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

		Map<JRStyle,JRTemplateElement> templatesMap;
		if (fillTopBorder)
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
			boxTemplate = createFrameTemplate();
			transferProperties(boxTemplate);
			
			//FIXME up to revision 2006 (Dec 5 2007) we were resetting both the border and the padding.
			// now we are only resetting the border and not the padding, prepare() assumes that the top and bottom paddings are always used.
			if (fillTopBorder)
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
			
			boxTemplate = filler.fillContext.deduplicate(boxTemplate);
			templatesMap.put(style, boxTemplate);
		}
		
		return boxTemplate;
	}

	protected JRTemplateFrame createFrameTemplate()
	{
		return new JRTemplateFrame(getElementOrigin(), 
				filler.getJasperPrint().getDefaultStyleProvider(), this);
	}

	protected JRTemplateElement createElementTemplate()
	{
		return createFrameTemplate();
	}

	protected void resolveElement(JRPrintElement element, byte evaluation)
	{
		// nothing
	}

	public JRElement[] getElements()
	{
		return frameContainer.getElements();
	}
	
	public List<JRChild> getChildren()
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


	@Override
	public BorderSplitType getBorderSplitType()
	{
		return borderSplitType;
	}
	
	/**
	 *
	 */
	public void visit(JRVisitor visitor)
	{
		visitor.visitFrame(this);
		
		if (ElementsVisitorUtils.visitDeepElements(visitor))
		{
			ElementsVisitorUtils.visitElements(visitor, getChildren());
		}
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

		protected int getActualContainerHeight()
		{
			int containerHeight = JRFillFrame.this.getHeight() - getLineBox().getTopPadding().intValue() - getLineBox().getBottomPadding().intValue(); 
			
			if (JRFillFrame.this.frameContainer.bottomElementInGroup != null)
			{
				if (
					getLineBox().getTopPadding().intValue() 
					+ JRFillFrame.this.frameContainer.bottomElementInGroup.getY() 
					+ JRFillFrame.this.frameContainer.bottomElementInGroup.getHeight() > JRFillFrame.this.getHeight()
					)
				{
					containerHeight = 
						JRFillFrame.this.frameContainer.bottomElementInGroup.getY() 
						+ JRFillFrame.this.frameContainer.bottomElementInGroup.getHeight();
				}
			}

			return containerHeight; 
		}
	}
	
}
