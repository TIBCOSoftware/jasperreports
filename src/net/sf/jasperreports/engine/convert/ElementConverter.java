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
package net.sf.jasperreports.engine.convert;

import net.sf.jasperreports.engine.JRBox;
import net.sf.jasperreports.engine.JRElement;
import net.sf.jasperreports.engine.JRGraphicElement;
import net.sf.jasperreports.engine.JRPrintElement;
import net.sf.jasperreports.engine.JRPrintGraphicElement;


/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id: JRGraphics2DExporter.java 1811 2007-08-13 19:52:07Z teodord $
 */
public abstract class ElementConverter
{

	/**
	 *
	 */
	public abstract JRPrintElement convert(ReportConverter reportConverter, JRElement element);


	/**
	 * 
	 */
	protected void copyElement(ReportConverter reportConverter, JRElement element, JRPrintElement printElement)
	{
		printElement.setX(element.getX());
		printElement.setY(element.getY());
		printElement.setWidth(element.getWidth());			
		printElement.setHeight(element.getHeight());
		printElement.setBackcolor(element.getOwnBackcolor());
		printElement.setForecolor(element.getOwnForecolor());
		//printElement.setKey(element.getKey());
		printElement.setMode(element.getOwnMode());
		printElement.setStyle(reportConverter.resolveStyle(element));
	}

	
	/**
	 * 
	 */
	protected void copyBox(JRBox source, JRBox dest)
	{
		dest.setBorder(source.getOwnBorder());
		dest.setBorderColor(source.getOwnBorderColor());
		dest.setPadding(source.getOwnPadding());
		
		dest.setBottomBorder(source.getOwnBottomBorder());
		dest.setBottomBorderColor(source.getOwnBottomBorderColor());
		dest.setBottomPadding(source.getOwnBottomPadding());
		
		dest.setLeftBorder(source.getOwnLeftBorder());
		dest.setLeftBorderColor(source.getOwnLeftBorderColor());
		dest.setLeftPadding(source.getOwnLeftPadding());
		
		dest.setRightBorder(source.getOwnRightBorder());
		dest.setRightBorderColor(source.getOwnRightBorderColor());
		dest.setRightPadding(source.getOwnRightPadding());
		
		dest.setTopBorder(source.getOwnTopBorder());
		dest.setTopBorderColor(source.getOwnTopBorderColor());
		dest.setTopPadding(source.getOwnTopPadding());
	}
	

	/**
	 *
	 */
	protected void copyGraphicElement(ReportConverter reportConverter, JRGraphicElement element, JRPrintGraphicElement printElement)
	{
		copyElement(reportConverter, element, printElement);
		
		printElement.setFill(element.getOwnFill());
		printElement.setPen(element.getOwnPen());
	}


}
