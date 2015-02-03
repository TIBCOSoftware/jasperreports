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
package net.sf.jasperreports.engine.export;

import java.awt.Dimension;
import java.io.ByteArrayInputStream;

import net.sf.jasperreports.components.html.HtmlComponent;
import net.sf.jasperreports.engine.JRComponentElement;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRGenericPrintElement;
import net.sf.jasperreports.engine.JRPrintImage;
import net.sf.jasperreports.engine.base.JRBasePrintImage;
import net.sf.jasperreports.engine.type.HorizontalImageAlignEnum;
import net.sf.jasperreports.engine.type.ScaleImageEnum;
import net.sf.jasperreports.engine.type.VerticalImageAlignEnum;
import net.sf.jasperreports.engine.util.HtmlPrintElement;
import net.sf.jasperreports.engine.util.JRExpressionUtil;
import net.sf.jasperreports.renderers.FlyingSaucerXhtmlToImageRenderer;

import org.w3c.dom.Document;
import org.w3c.tidy.Tidy;


/**
 * @author Narcis Marcu (narcism@users.sourceforge.net)
 */
public class FlyingSaucerHtmlPrintElement implements HtmlPrintElement {
	
	public FlyingSaucerHtmlPrintElement(){
	}
	
	public JRPrintImage createImageFromElement(JRGenericPrintElement element) throws JRException {
		String htmlContent = (String) element.getParameterValue(HtmlPrintElement.PARAMETER_HTML_CONTENT);
		String scaleType = (String) element.getParameterValue(HtmlPrintElement.PARAMETER_SCALE_TYPE);
		String horizontalAlignment = (String) element.getParameterValue(HtmlPrintElement.PARAMETER_HORIZONTAL_ALIGN);
		String verticalAlignment = (String) element.getParameterValue(HtmlPrintElement.PARAMETER_VERTICAL_ALIGN);
		Boolean hasOverflowed = (Boolean) element.getParameterValue(HtmlPrintElement.BUILTIN_PARAMETER_HAS_OVERFLOWED);
		Boolean clipOnOverflow = (Boolean) element.getParameterValue(HtmlPrintElement.PARAMETER_CLIP_ON_OVERFLOW);
		
		JRBasePrintImage printImage = new JRBasePrintImage(element.getDefaultStyleProvider());
		printImage.setStyle(element.getStyle());
		printImage.setMode(element.getModeValue());
		printImage.setBackcolor(element.getBackcolor());
		printImage.setForecolor(element.getForecolor());
		printImage.setX(element.getX());
		printImage.setY(element.getY());
		printImage.setWidth(element.getWidth());
		printImage.setScaleImage(ScaleImageEnum.getByName(scaleType));
		printImage.setHorizontalImageAlign(HorizontalImageAlignEnum.getByName(horizontalAlignment));
		printImage.setVerticalImageAlign(VerticalImageAlignEnum.getByName(verticalAlignment));
		
		FlyingSaucerXhtmlToImageRenderer renderer = new FlyingSaucerXhtmlToImageRenderer(getHtmlDocument(htmlContent), element.getWidth(), element.getHeight());
		
		if (printImage.getScaleImageValue() == ScaleImageEnum.REAL_HEIGHT || printImage.getScaleImageValue() == ScaleImageEnum.REAL_SIZE) {
			boolean canClip = hasOverflowed != null ? hasOverflowed : false;
			if (canClip) {
				printImage.setHeight(element.getHeight());
				if (clipOnOverflow) {
					printImage.setScaleImage(ScaleImageEnum.CLIP);
				}
			} else {
				printImage.setHeight(renderer.getComputedSize().height);
			}
		} else {
			printImage.setHeight(element.getHeight());
		}

		printImage.setRenderable(renderer);
		return printImage;
	}

	public JRPrintImage createImageFromComponentElement(JRComponentElement componentElement) throws JRException {
		HtmlComponent html = (HtmlComponent) componentElement.getComponent();
		
		String htmlContent = "";
		
		if (html.getHtmlContentExpression() != null) {
			htmlContent = JRExpressionUtil.getExpressionText(html.getHtmlContentExpression());
		}
		
		JRBasePrintImage printImage = new JRBasePrintImage(componentElement.getDefaultStyleProvider());

		printImage.setStyle(componentElement.getStyle());
		printImage.setMode(componentElement.getModeValue());
		printImage.setBackcolor(componentElement.getBackcolor());
		printImage.setForecolor(componentElement.getForecolor());
		printImage.setX(componentElement.getX());
		printImage.setY(componentElement.getY());
		printImage.setWidth(componentElement.getWidth());
		printImage.setHeight(componentElement.getHeight());
		printImage.setScaleImage(html.getScaleType());
		printImage.setHorizontalImageAlign(html.getHorizontalImageAlign());
		printImage.setVerticalImageAlign(html.getVerticalImageAlign());
		
		FlyingSaucerXhtmlToImageRenderer renderer = new FlyingSaucerXhtmlToImageRenderer(getHtmlDocument(htmlContent), componentElement.getWidth(), componentElement.getHeight());
		printImage.setRenderable(renderer);
		return printImage;
	}
	
	public Dimension getComputedSize(JRGenericPrintElement element) {
		String htmlContent = (String) element.getParameterValue(HtmlPrintElement.PARAMETER_HTML_CONTENT);
		
		FlyingSaucerXhtmlToImageRenderer renderer = new FlyingSaucerXhtmlToImageRenderer(getHtmlDocument(htmlContent), element.getWidth(), element.getHeight());

		return renderer.getComputedSize();
	}
	
	private Document getHtmlDocument(String htmlContent) {
		StringBuffer buf = new StringBuffer();
		buf.append("<html>");
		buf.append("<head><style language='text/css'>");
		buf.append("@page{ margin: 0; }");
		buf.append("body{ margin:0;}");
		buf.append("</style></head>");
		buf.append("<body>");
		buf.append(htmlContent);
		buf.append("</body>");
		buf.append("</html>");
		
		Tidy tidy = new Tidy();
		tidy.setXHTML(true);
		tidy.setQuiet(true);
		tidy.setShowWarnings(false);
		
		return tidy.parseDOM(new ByteArrayInputStream(buf.toString().getBytes()), null);
	}

}
