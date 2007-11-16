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

/*
 * Contributors:
 * Eugene D - eugenedruy@users.sourceforge.net 
 * Adrian Jackson - iapetus@users.sourceforge.net
 * David Taylor - exodussystems@users.sourceforge.net
 * Lars Kristensen - llk@users.sourceforge.net
 */
package net.sf.jasperreports.engine.export.draw;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Shape;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;

import net.sf.jasperreports.engine.JRElement;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRPrintElement;
import net.sf.jasperreports.engine.JRPrintEllipse;
import net.sf.jasperreports.engine.JRPrintFrame;
import net.sf.jasperreports.engine.JRPrintImage;
import net.sf.jasperreports.engine.JRPrintLine;
import net.sf.jasperreports.engine.JRPrintRectangle;
import net.sf.jasperreports.engine.JRPrintText;
import net.sf.jasperreports.engine.export.ExporterFilter;
import net.sf.jasperreports.engine.export.TextRenderer;
import net.sf.jasperreports.engine.util.JRStyledTextParser;


/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id: JRGraphics2DExporter.java 1811 2007-08-13 19:52:07Z teodord $
 */
public class FrameDrawer extends ElementDrawer
{
	/**
	 *
	 */
	private static final int ELEMENT_RECTANGLE_PADDING = 3;

	/**
	 *
	 */
	private ExporterFilter filter = null;
	private Graphics2D grx = null;
	private LinkedList elementOffsetStack = new LinkedList();
	private int elementOffsetX = 0;
	private int elementOffsetY = 0;
	private boolean isClip = false;
	
	/**
	 *
	 */
	private LineDrawer lineDrawer = null;
	private RectangleDrawer rectangleDrawer = null;
	private EllipseDrawer ellipseDrawer = null;
	private ImageDrawer imageDrawer = null;
	private TextDrawer textDrawer = null;

	
	/**
	 *
	 */
	public FrameDrawer(
		ExporterFilter filter,
		TextRenderer textRenderer,
		JRStyledTextParser styledTextParser
		)
	{
		this.filter = filter;
		
		lineDrawer = new LineDrawer();
		rectangleDrawer = new RectangleDrawer();
		ellipseDrawer = new EllipseDrawer();
		imageDrawer = new ImageDrawer();
		textDrawer = new TextDrawer(textRenderer, styledTextParser);
	}
	
	
	/**
	 *
	 */
	public void setClip(boolean isClip)
	{
		this.isClip = isClip;
	}
	
	
	/**
	 *
	 */
	public void draw(Graphics2D grx, JRPrintElement element, int offsetX, int offsetY) throws JRException
	{
		this.grx = grx;
		
		JRPrintFrame frame = (JRPrintFrame)element;
		
		Shape oldClipShape = null;
		if (isClip)
		{
			oldClipShape = grx.getClip();
			grx.clip(
				new Rectangle(
					frame.getX() + offsetX, 
					frame.getY() + offsetY, 
					frame.getWidth(), 
					frame.getHeight()
					)
				);
		}
		
		if (frame.getMode() == JRElement.MODE_OPAQUE)
		{
			grx.setColor(frame.getBackcolor());
			grx.fillRect(
				frame.getX() + offsetX, 
				frame.getY() + offsetY, 
				frame.getWidth(),
				frame.getHeight()
				);
		}

		grx.setColor(frame.getForecolor());

		setFrameElementsOffset(frame, offsetX, offsetY);
		try
		{
			draw(frame.getElements());
		}
		finally
		{
			if (isClip)
			{
				grx.setClip(oldClipShape);
			}
			restoreElementOffsets();
		}
		
		/*   */
		drawBox(grx, frame, frame, offsetX, offsetY);
	}


	/**
	 *
	 */
	public void draw(Graphics2D grx, Collection elements, int offsetX, int offsetY) throws JRException
	{
		this.grx = grx;
		
		setElementOffsets(offsetX, offsetY);
		try
		{
			draw(elements);
		}
		finally
		{
			restoreElementOffsets();
		}
	}


	/**
	 *
	 */
	private void draw(Collection elements) throws JRException
	{
		if (elements != null && elements.size() > 0)
		{
			Shape clipArea = grx.getClip();
			for(Iterator it = elements.iterator(); it.hasNext();)
			{
				JRPrintElement element = (JRPrintElement)it.next();
				
				if (
					(filter != null && !filter.isToExport(element))
					|| !clipArea.intersects(
						element.getX() + getOffsetX() - ELEMENT_RECTANGLE_PADDING, 
						element.getY() + getOffsetY() - ELEMENT_RECTANGLE_PADDING, 
						element.getWidth() + 2 * ELEMENT_RECTANGLE_PADDING, 
						element.getHeight() + 2 * ELEMENT_RECTANGLE_PADDING)
					)
				{
					continue;
				}
				
				if (element instanceof JRPrintLine)
				{
					lineDrawer.draw(grx, element, getOffsetX(), getOffsetY());
				}
				else if (element instanceof JRPrintRectangle)
				{
					rectangleDrawer.draw(grx, element, getOffsetX(), getOffsetY());
				}
				else if (element instanceof JRPrintEllipse)
				{
					ellipseDrawer.draw(grx, element, getOffsetX(), getOffsetY());
				}
				else if (element instanceof JRPrintImage)
				{
					imageDrawer.draw(grx, element, getOffsetX(), getOffsetY());
				}
				else if (element instanceof JRPrintText)
				{
					textDrawer.draw(grx, element, getOffsetX(), getOffsetY());
				}
				else if (element instanceof JRPrintFrame)
				{
					this.draw(grx, element, getOffsetX(), getOffsetY());
				}
			}
		}
	}


	/**
	 *
	 */
	private void setFrameElementsOffset(JRPrintFrame frame, int offsetX, int offsetY)
	{	
		setElementOffsets(
			offsetX + frame.getX() + frame.getLeftPadding(), 
			offsetY + frame.getY() + frame.getTopPadding()
			);
	}
	
	
	/**
	 *
	 */
	private void setElementOffsets(int offsetX, int offsetY)
	{
		elementOffsetStack.addLast(new int[]{elementOffsetX, elementOffsetY});
		
		elementOffsetX = offsetX;
		elementOffsetY = offsetY;
	}

	
	/**
	 *
	 */
	private void restoreElementOffsets()
	{
		int[] offsets = (int[]) elementOffsetStack.removeLast();
		elementOffsetX = offsets[0];
		elementOffsetY = offsets[1];
	}

	
	/**
	 *
	 */
	private int getOffsetX()
	{
		return elementOffsetX;
	}


	/**
	 *
	 */
	private int getOffsetY()
	{
		return elementOffsetY;
	}


}
