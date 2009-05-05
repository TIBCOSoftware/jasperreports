/*
 * ============================================================================
 * GNU Lesser General Public License
 * ============================================================================
 *
 * JasperReports - Free Java report-generating library.
 * Copyright (C) 2001-2009 JasperSoft Corporation http://www.jaspersoft.com
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
 * 539 Bryant Street, Suite 100
 * San Francisco, CA 94107
 * http://www.jaspersoft.com
 */
package net.sf.jasperreports.components.list;

import java.util.Iterator;
import java.util.List;

import net.sf.jasperreports.engine.JRChild;
import net.sf.jasperreports.engine.JRComponentElement;
import net.sf.jasperreports.engine.JRElement;
import net.sf.jasperreports.engine.JRPrintElement;
import net.sf.jasperreports.engine.base.JRBasePrintFrame;
import net.sf.jasperreports.engine.base.JRBasePrintImage;
import net.sf.jasperreports.engine.component.ComponentDesignConverter;
import net.sf.jasperreports.engine.convert.ConvertVisitor;
import net.sf.jasperreports.engine.convert.ReportConverter;

/**
 * List preview converter.
 * 
 * @author Lucian Chirita (lucianc@users.sourceforge.net)
 * @version $Id$
 */
public class ListDesignConverter implements ComponentDesignConverter
{

	public JRPrintElement convert(ReportConverter reportConverter,
			JRComponentElement element)
	{
		ListComponent list = (ListComponent) element.getComponent();
		if (list == null || list.getContents() == null)
		{
			return null;
		}
		
		JRBasePrintFrame frame = new JRBasePrintFrame(
				reportConverter.getDefaultStyleProvider());
		reportConverter.copyBaseAttributes(element, frame);
		
		ListContents contents = list.getContents();
		if (contents.getHeight() > 0)
		{
			JRBasePrintFrame contentsFrame = new JRBasePrintFrame(
					reportConverter.getDefaultStyleProvider());
			contentsFrame.setX(0);
			contentsFrame.setY(0);
			contentsFrame.setWidth(element.getWidth());			
			contentsFrame.setHeight(contents.getHeight());
			contentsFrame.setMode(JRElement.MODE_TRANSPARENT);
			
			List children = contents.getChildren();
			if (children != null)
			{
				ConvertVisitor contentsVisitor = new ConvertVisitor(reportConverter, 
						contentsFrame);
				for (Iterator it = children.iterator(); it
						.hasNext();)
				{
					JRChild child = (JRChild) it.next();
					child.visit(contentsVisitor);
				}
			}
			
			frame.addElement(contentsFrame);

			if (contents.getHeight() < element.getHeight())
			{
				// add a grey rectangle to highlight the contents height
				JRBasePrintImage image = new JRBasePrintImage(
						reportConverter.getDefaultStyleProvider());
				image.setX(0);
				image.setWidth(element.getWidth());
				image.setY(contents.getHeight());
				image.setHeight(element.getHeight() - contents.getHeight());
				image.setMode(JRElement.MODE_TRANSPARENT);
				image.setRenderer(UnusedSpaceImageRenderer.INSTANCE);
				frame.addElement(image);
			}
		}
		
		return frame;
	}

}
