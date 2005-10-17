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
 * 185, Berry Street, Suite 6200
 * San Francisco CA 94107
 * http://www.jaspersoft.com
 */
package net.sf.jasperreports.engine.fill;

import java.io.IOException;
import java.util.List;

import net.sf.jasperreports.engine.JRAbstractObjectFactory;
import net.sf.jasperreports.engine.JRBox;
import net.sf.jasperreports.engine.JRChild;
import net.sf.jasperreports.engine.JRElement;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExpressionCollector;
import net.sf.jasperreports.engine.JRFrame;
import net.sf.jasperreports.engine.JRPrintElement;
import net.sf.jasperreports.engine.base.JRBaseBox;
import net.sf.jasperreports.engine.base.JRBaseElementGroup;
import net.sf.jasperreports.engine.xml.JRXmlWriter;

/**
 * Fill time implementation of a frame element.
 * 
 * @author Lucian Chirita (lucianc@users.sourceforge.net)
 * @version $Id$
 */
public class JRFillFrame extends JRFillElement implements JRFrame
{
	protected final JRFrame parentFrame;
	protected final JRBox box;
	
	/**
	 * Element container used for filling.
	 */
	private JRFillFrameElements frameContainer;
	
	/**
	 * Template frame.
	 */
	private JRTemplateFrame templateFrame;
	
	/**
	 * Template frame without the bottom border.
	 */
	private JRTemplateFrame bottomTemplateFrame;
	
	/**
	 * Template frame without the top border
	 */
	private JRTemplateFrame topTemplateFrame;
	
	/**
	 * Template frame without the top and bottom borders
	 */
	private JRTemplateFrame topBottomTemplateFrame;
	
	/**
	 * Whether the current frame chunk is the first one.
	 */
	private boolean first;
	
	/**
	 * Whether the frame has started filling and not ended.
	 */
	private boolean filling;

	public JRFillFrame(JRBaseFiller filler, JRFrame frame, JRFillObjectFactory factory)
	{
		super(filler, frame, factory);
		
		parentFrame = frame;
		box = frame.getBox();
		
		frameContainer = new JRFillFrameElements(factory);
		
		templateFrame = new JRTemplateFrame(this);
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
		
		if (availableStretchHeight < getRelativeY() - getY() - getBandBottomY())
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

		// TODO reprinted when isAlreadyPrinted() || !isPrintRepeatedValues()?
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
		
		first = !isOverflow || !filling;
				
		int topPadding = 0;
		int bottomPadding = 0;
		if (box != null)
		{
			if (first)
			{
				topPadding = box.getTopPadding();
			}
			
			bottomPadding = box.getBottomPadding();
		}
		
		int stretchHeight = availableStretchHeight - getRelativeY() + getY() + getBandBottomY();
		
		frameContainer.initFill();
		frameContainer.resetElements();
		frameContainer.prepareElements(stretchHeight - topPadding - bottomPadding, true);
		
		boolean willOverflow = frameContainer.willOverflow();
		if (willOverflow)
		{
			setStretchHeight(getHeight() + stretchHeight);
		}
		else
		{
			setStretchHeight(frameContainer.getStretchHeight() - frameContainer.getFirstY());
		}

		filling = willOverflow;

		return willOverflow;
	}

	protected JRPrintElement fill() throws JRException
	{
		frameContainer.stretchElements();
		frameContainer.moveBandBottomElements();
		frameContainer.removeBlankElements();
		
		JRTemplatePrintFrame printFrame = new JRTemplatePrintFrame(getTemplate());
		printFrame.setX(getX());
		printFrame.setY(getRelativeY());
		printFrame.setWidth(getWidth());
		printFrame.setHeight(frameContainer.getStretchHeight());
		
		frameContainer.fillElements(printFrame);
		
		return printFrame;
	}

	protected JRTemplateFrame getTemplate()
	{
		JRTemplateFrame boxTemplate;
		
		if (first)
		{
			if (filling) //remove the bottom border
			{				
				if (bottomTemplateFrame == null)
				{
					JRBox bottomBox = new JRBaseBox(box, true, true, true, false, null);
					
					bottomTemplateFrame = new JRTemplateFrame(this);
					bottomTemplateFrame.setBox(bottomBox);
				}
				
				boxTemplate = bottomTemplateFrame;
			}
			else
			{
				boxTemplate = templateFrame;
			}
		}
		else
		{
			if (filling) //remove the top and bottom borders
			{
				if (topBottomTemplateFrame == null)
				{
					JRBox topBottomBox = new JRBaseBox(box, true, true, false, false, null);
					
					topBottomTemplateFrame = new JRTemplateFrame(this);
					topBottomTemplateFrame.setBox(topBottomBox);
				}
				
				boxTemplate = topBottomTemplateFrame;
			}
			else //remove the top border
			{
				if (topTemplateFrame == null)
				{
					JRBox topBox = new JRBaseBox(box, true, true, false, true, null);
					
					topTemplateFrame = new JRTemplateFrame(this);
					topTemplateFrame.setBox(topBox);
				}
				
				boxTemplate = topTemplateFrame;
			}
		}
		
		return boxTemplate;
	}

	protected void resolveElement(JRPrintElement element, byte evaluation) throws JRException
	{
		// nothing
	}

	public JRBox getBox()
	{
		return box;
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

	public JRChild getCopy(JRAbstractObjectFactory factory)
	{
		return factory.getFrame(this);
	}

	public void writeXml(JRXmlWriter writer) throws IOException
	{
		writer.writeFrame(this);
	}

	
	public JRElement getElementByKey(String key)
	{
		return JRBaseElementGroup.getElementByKey(getElements(), key);
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

		protected int getHeight()
		{
			return JRFillFrame.this.getHeight();
		}
	}
}
