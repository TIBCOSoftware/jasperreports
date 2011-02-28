/*
 * JasperReports - Free Java Reporting Library.
 * Copyright (C) 2001 - 2009 Jaspersoft Corporation. All rights reserved.
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

/*
 * Contributors:
 * Alex Parfenov - aparfeno@users.sourceforge.net
 * Adrian Jackson - iapetus@users.sourceforge.net
 * David Taylor - exodussystems@users.sourceforge.net
 * Lars Kristensen - llk@users.sourceforge.net
 */

package net.sf.jasperreports.engine.export;

import java.util.Iterator;
import java.util.ListIterator;
import java.util.Stack;

import net.sf.jasperreports.crosstabs.JRCellContents;
import net.sf.jasperreports.engine.JRPrintElement;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.base.JRBasePrintFrame;


/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id: JRHtmlExporter.java 3675 2010-04-02 08:58:23Z shertage $
 */
public class JRHtmlExporterHelper
{
	private final static Integer TABLE_TYPE_TABLE = 1;
	private final static Integer TABLE_TYPE_CROSSTAB = 2;
	
	JasperPrint jasperPrint = null;

	Stack<Integer> tableTypes = new Stack<Integer>();
	
	public JRHtmlExporterHelper(JasperPrint jasperPrint)
	{
		this.jasperPrint = jasperPrint;
	}
	
	/**
	 *
	 */
	public void createNestedFrames(ListIterator elemIt, JRBasePrintFrame parentFrame)//FIXME need to make this recursive for frames
	{
		while (elemIt.hasNext())
		{
			JRPrintElement element = (JRPrintElement)elemIt.next();
			
			if (element.hasProperties())
			{
				String tableTagProp = element.getPropertiesMap().getProperty(JRPdfExporterTagHelper.PROPERTY_TAG_TABLE);
				boolean tableStart = JRPdfExporterTagHelper.TAG_START.equals(tableTagProp);// || JRPdfExporterTagHelper.TAG_FULL.equals(prop)))
				
				String crosstabProp = element.getPropertiesMap().getProperty(JRCellContents.PROPERTY_TYPE);
				boolean crosstabStart = JRCellContents.TYPE_CROSSTAB_HEADER.equals(crosstabProp);
				
				if (tableStart || crosstabStart)
				{
					tableTypes.push(crosstabStart ? TABLE_TYPE_CROSSTAB : TABLE_TYPE_TABLE);
					
					JRBasePrintFrame nestedFrame = new JRBasePrintFrame(jasperPrint.getDefaultStyleProvider());
					nestedFrame.getPropertiesMap().setProperty(JRHtmlExporterParameter.PROPERTY_FRAMES_AS_NESTED_TABLES, Boolean.TRUE.toString());
					nestedFrame.addElement(element);

					createNestedFrames(elemIt, nestedFrame);

					sizeFrame(nestedFrame);
					
					parentFrame.addElement(nestedFrame);
					
					continue;
				}
				
				if (
					tableTypes.size() > 0
					&& (TABLE_TYPE_CROSSTAB.equals(tableTypes.get(tableTypes.size() - 1))
					&& crosstabProp == null)
					||
					JRPdfExporterTagHelper.TAG_END.equals(tableTagProp)// || JRPdfExporterTagHelper.TAG_FULL.equals(prop)))
					)
				{
					//crosstab or table ended;
					tableTypes.pop();
					//parentFrame.addElement(element);
					break;
				}
			}
			
			parentFrame.addElement(element);
		}
		return;
	}
	

	/**
	 *
	 */
	private void sizeFrame(JRBasePrintFrame frame)
	{
		int x = Integer.MAX_VALUE;
		int y = Integer.MAX_VALUE;
		int width = 0;
		int height = 0;
		for (Iterator it = frame.getElements().iterator(); it.hasNext();)
		{
			JRPrintElement element = (JRPrintElement)it.next();
			x = element.getX() < x ? element.getX() : x; 
			y = element.getY() < y ? element.getY() : y;
			width = element.getX() + element.getWidth() > width ? element.getX() + element.getWidth() : width;
			height = element.getY() + element.getHeight() > height ? element.getY() + element.getHeight() : height;
		}
		
		frame.setX(x);// - x);
		frame.setY(y);// - y);
		frame.setWidth(width - x);
		frame.setHeight(height - y);

		for (Iterator it = frame.getElements().iterator(); it.hasNext();)
		{
			JRPrintElement element = (JRPrintElement)it.next();
			element.setX(element.getX() - x); 
			element.setY(element.getY() - y); 
		}
	}
	

}

