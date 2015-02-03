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

import javax.swing.JEditorPane;
import javax.swing.text.Element;
import javax.swing.text.View;
import javax.swing.text.ViewFactory;
import javax.swing.text.html.HTMLEditorKit;
import javax.swing.text.html.ImageView;

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
import net.sf.jasperreports.renderers.AwtComponentRenderer;


/**
 * @author Narcis Marcu (narcism@users.sourceforge.net)
 */
public class DefaultHtmlPrintElement implements HtmlPrintElement {
	
	public DefaultHtmlPrintElement(){
	}
	
	public JRPrintImage createImageFromElement(JRGenericPrintElement element) {
		String htmlContent = (String) element.getParameterValue(HtmlPrintElement.PARAMETER_HTML_CONTENT);
		String scaleType = (String) element.getParameterValue(HtmlPrintElement.PARAMETER_SCALE_TYPE);
		String horizontalAlignment = (String) element.getParameterValue(HtmlPrintElement.PARAMETER_HORIZONTAL_ALIGN);
		String verticalAlignment = (String) element.getParameterValue(HtmlPrintElement.PARAMETER_VERTICAL_ALIGN);
		
		JEditorPane editorPane = new JEditorPane();
		editorPane.setEditorKitForContentType("text/html", new SynchronousImageLoaderKit());
		editorPane.setContentType("text/html");
		
		editorPane.setText(htmlContent);
		editorPane.setBorder(null);
		editorPane.setSize(editorPane.getPreferredSize());

		JRBasePrintImage printImage = new JRBasePrintImage(element.getDefaultStyleProvider());
		printImage.setX(element.getX());
		printImage.setY(element.getY());
		printImage.setWidth(element.getWidth());
		printImage.setHeight(element.getHeight());
		printImage.setScaleImage(ScaleImageEnum.getByName(scaleType));
		printImage.setHorizontalImageAlign(HorizontalImageAlignEnum.getByName(horizontalAlignment));
		printImage.setVerticalImageAlign(VerticalImageAlignEnum.getByName(verticalAlignment));
		printImage.setStyle(element.getStyle());
		printImage.setMode(element.getModeValue());
		printImage.setBackcolor(element.getBackcolor());
		printImage.setForecolor(element.getForecolor());
		printImage.setRenderable(new AwtComponentRenderer(editorPane));

		return printImage;
	}

	public JRPrintImage createImageFromComponentElement(JRComponentElement componentElement) throws JRException {
		HtmlComponent html = (HtmlComponent) componentElement.getComponent();
		
		JEditorPane editorPane = new JEditorPane();
		editorPane.setEditorKitForContentType("text/html", new SynchronousImageLoaderKit());
		editorPane.setContentType("text/html");
		
		String htmlContent = "";
		
		if (html.getHtmlContentExpression() != null) {
			htmlContent = JRExpressionUtil.getExpressionText(html.getHtmlContentExpression());
		}
		
		editorPane.setText(htmlContent);
		editorPane.setBorder(null);
		editorPane.setSize(editorPane.getPreferredSize());
		
		JRBasePrintImage printImage = new JRBasePrintImage(componentElement.getDefaultStyleProvider());
		printImage.setX(componentElement.getX());
		printImage.setY(componentElement.getY());
		printImage.setWidth(componentElement.getWidth());
		printImage.setHeight(componentElement.getHeight());
		printImage.setScaleImage(html.getScaleType());
		printImage.setHorizontalImageAlign(html.getHorizontalImageAlign());
		printImage.setVerticalImageAlign(html.getVerticalImageAlign());
		printImage.setStyle(componentElement.getStyle());
		printImage.setMode(componentElement.getModeValue());
		printImage.setBackcolor(componentElement.getBackcolor());
		printImage.setForecolor(componentElement.getForecolor());

		printImage.setRenderable(new AwtComponentRenderer(editorPane));
		
		return printImage;
	}

	public Dimension getComputedSize(JRGenericPrintElement element) {
		String htmlContent = (String) element.getParameterValue(HtmlPrintElement.PARAMETER_HTML_CONTENT);
		JEditorPane editorPane = new JEditorPane();
		editorPane.setEditorKitForContentType("text/html", new SynchronousImageLoaderKit());
		editorPane.setContentType("text/html");
		editorPane.setText(htmlContent);
		editorPane.setBorder(null);
		return editorPane.getPreferredSize();
	}
	
	public class SynchronousImageLoaderKit extends HTMLEditorKit {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		@Override
		public ViewFactory getViewFactory() {
			return new HTMLFactory() {

				@Override
				public View create(Element elem) {
					View view = super.create(elem);
					if (view instanceof ImageView) {
						((ImageView) view).setLoadsSynchronously(true);
					}
					return view;
				}
			};
		}
	}

}
