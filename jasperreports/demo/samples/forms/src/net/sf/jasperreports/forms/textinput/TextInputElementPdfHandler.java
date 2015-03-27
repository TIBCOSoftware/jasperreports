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
package net.sf.jasperreports.forms.textinput;

import java.awt.Color;

import net.sf.jasperreports.engine.JRGenericPrintElement;
import net.sf.jasperreports.engine.JRPrintText;
import net.sf.jasperreports.engine.JRRuntimeException;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.export.GenericElementPdfHandler;
import net.sf.jasperreports.engine.export.JRPdfExporterContext;

import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.PdfFormField;
import com.lowagie.text.pdf.PdfWriter;
import com.lowagie.text.pdf.TextField;


/**
 * @author sanda zaharia (shertage@users.sourceforge.net)
 */
public class TextInputElementPdfHandler implements GenericElementPdfHandler
{

	private String fieldNameParameter;
	private String textParameter;
	private String defaultTextParameter;
	
	public void exportElement(JRPdfExporterContext exporterContext, JRGenericPrintElement element)
	{
		PdfWriter writer = exporterContext.getPdfWriter();
		JasperPrint jasperPrint = exporterContext.getExportedReport();
		
		JRPrintText printText = (JRPrintText)element.getParameterValue(TextInputElement.PARAMETER_PRINT_TEXT_ELEMENT);
		if (printText == null) //FIXMEINPUT deal with xml serialization
		{
			return;
		}
		
		Rectangle rectangle = new Rectangle(
				element.getX() + exporterContext.getOffsetX(),
				jasperPrint.getPageHeight() - element.getY() - exporterContext.getOffsetY(),
				element.getX() + exporterContext.getOffsetX() + element.getWidth(),
				jasperPrint.getPageHeight() - element.getY() - exporterContext.getOffsetY() - element.getHeight()
				);
		TextField text = new TextField(writer, rectangle, getFieldName(element));
		Color backColor = printText.getBackcolor();
		if(backColor != null){
			text.setBackgroundColor(backColor);
		}
		Color forecolor = printText.getForecolor();
		if(forecolor != null){
			text.setTextColor(forecolor);
		}
		text.setText(printText.getFullText());
		text.setDefaultText("default:" + printText.getFullText());
//		text.setBackgroundColor(element.getBackcolor());
//		text.setTextColor(element.getForecolor());
//		text.setText(getText(element));
//		text.setDefaultText(getDefaultText(element));

		//FIXME: dynamic settings below:
		
//		text.setAlignment(Element.ALIGN_LEFT);
//		text.setBorderColor(Color.BLACK);
//		text.setBorderWidth(TextField.BORDER_WIDTH_THIN);
//		text.setBorderStyle(PdfBorderDictionary.STYLE_INSET);

//		text.setChoiceExports(null);
//		text.setChoices(null);
//		text.setChoiceSelection(0);
//		text.setExtensionFont(null);
//		text.setExtraMargin(0, 0);
//		try{
//			text.setFont(BaseFont.createFont(BaseFont.COURIER, BaseFont.CP1250, true));
//		}catch(Exception e){
//			throw new JRRuntimeException(e);
//		}
		text.setFontSize(printText.getFontsize());
		if (Boolean.TRUE.equals(element.getParameterValue(TextInputElement.PARAMETER_MULTI_LINE)))
		{
			text.setOptions(TextField.MULTILINE);
		}
//		text.setRotation(90);
		text.setVisibility(TextField.VISIBLE);
		
		try{
			PdfFormField field = text.getTextField();
			writer.addAnnotation(field);
		}catch(Exception e){
			throw new JRRuntimeException(e);
		}
		
	}
	
	public String getFieldNameParameter()
	{
		return fieldNameParameter;
	}
	
	public String getFieldName(JRGenericPrintElement element)
	{
		return (String)element.getParameterValue(fieldNameParameter);
	}
	
	public void setFieldNameParameter(String fieldNameParameter)
	{
		this.fieldNameParameter = fieldNameParameter;
	}
	
	public String getDefaultTextParameter()
	{
		return defaultTextParameter;
	}
	
	public String getDefaultText(JRGenericPrintElement element)
	{
		return element.getParameterValue(defaultTextParameter) == null ? null : element.getParameterValue(defaultTextParameter).toString();
	}
	
	public void setDefaultTextParameter(String defaultTextParameter)
	{
		this.defaultTextParameter = defaultTextParameter;
	}
	
	public String getTextParameter()
	{
		return textParameter;
	}
	
	public String getText(JRGenericPrintElement element)
	{
		return element.getParameterValue(textParameter) == null ? null : element.getParameterValue(textParameter).toString();
	}

	public void setTextParameter(String textParameter)
	{
		this.textParameter = textParameter;
	}

	public boolean toExport(JRGenericPrintElement element)
	{
		return true;
	}
}
