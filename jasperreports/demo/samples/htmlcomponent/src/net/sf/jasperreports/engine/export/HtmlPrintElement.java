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
package net.sf.jasperreports.engine.export;

import javax.swing.JEditorPane;

import net.sf.jasperreports.engine.JRGenericPrintElement;
import net.sf.jasperreports.engine.JRPrintImage;
import net.sf.jasperreports.engine.base.JRBasePrintImage;
import net.sf.jasperreports.engine.type.HorizontalAlignEnum;
import net.sf.jasperreports.engine.type.ModeEnum;
import net.sf.jasperreports.engine.type.ScaleImageEnum;
import net.sf.jasperreports.engine.type.VerticalAlignEnum;
import net.sf.jasperreports.engine.util.JRColorUtil;
import net.sf.jasperreports.renderers.AwtComponentRenderer;


/**
 * @author Narcis Marcu (narcism@users.sourceforge.net)
 * @version $Id$
 */
public class HtmlPrintElement {
	
	public static final String PARAMETER_HTML_CONTENT = "htmlContent";

	public static final String PARAMETER_SCALE_TYPE = "scaleType";
	
	public static final String PARAMETER_HORIZONTAL_ALIGN = "horizontalAlign";
	
	public static final String PARAMETER_VERTICAL_ALIGN = "verticalAlign";
	
	public static JRPrintImage createImageFromElement(JRGenericPrintElement element) {
		String htmlContent = (String) element.getParameterValue(HtmlPrintElement.PARAMETER_HTML_CONTENT);
		String scaleType = (String) element.getParameterValue(HtmlPrintElement.PARAMETER_SCALE_TYPE);
		String horizontalAlignment = (String) element.getParameterValue(HtmlPrintElement.PARAMETER_HORIZONTAL_ALIGN);
		String verticalAlignment = (String) element.getParameterValue(HtmlPrintElement.PARAMETER_VERTICAL_ALIGN);
		
		JEditorPane editorPane = new JEditorPane();

		editorPane.setContentType("text/html");
		
		StringBuffer html = new StringBuffer();

		html.append("<table style='width:" + (element.getWidth() - 0) + "px; height:" + (element.getHeight() - 0) + "px; overflow:hidden; ");
		
		if (element.getModeValue() == ModeEnum.OPAQUE)
		{
			html.append("background-color: #");
			html.append(JRColorUtil.getColorHexa(element.getBackcolor()));
			html.append("; ");
		}
		html.append("'><tr><td");
		if (horizontalAlignment != null) {
			html.append(" align='" + horizontalAlignment.toLowerCase() + "'");
		}
		if (verticalAlignment != null) {
			html.append(" valign='" + verticalAlignment.toLowerCase() + "'");
		}
		html.append(" style='padding: 0px'>" + htmlContent + "</td></tr></table>");
		
		
		editorPane.setText(html.toString());
		editorPane.setBorder(null);
		editorPane.setSize(editorPane.getPreferredSize());
		
        JRBasePrintImage printImage = new JRBasePrintImage(element.getDefaultStyleProvider());

        printImage.setStyle(element.getStyle());
        printImage.setMode(element.getModeValue());
        printImage.setBackcolor(element.getBackcolor());
        printImage.setForecolor(element.getForecolor());
        printImage.setX(element.getX());
        printImage.setY(element.getY());
        printImage.setWidth(element.getWidth());
        printImage.setHeight(element.getHeight());
        printImage.setScaleImage(ScaleImageEnum.getByName(scaleType));
        printImage.setHorizontalAlignment(HorizontalAlignEnum.getByName(horizontalAlignment));
        printImage.setVerticalAlignment(VerticalAlignEnum.getByName(verticalAlignment));
        printImage.setRenderer(new AwtComponentRenderer(editorPane));
        
        return printImage;
	}

}
