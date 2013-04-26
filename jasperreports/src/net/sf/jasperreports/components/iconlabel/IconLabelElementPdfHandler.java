/*
 * JasperReports - Free Java Reporting Library.
 * Copyright (C) 2001 - 2013 Jaspersoft Corporation. All rights reserved.
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
package net.sf.jasperreports.components.iconlabel;

import net.sf.jasperreports.engine.JRGenericPrintElement;
import net.sf.jasperreports.engine.JRPrintText;
import net.sf.jasperreports.engine.JRRuntimeException;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.export.GenericElementPdfHandler;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.engine.export.JRPdfExporterContext;

import com.lowagie.text.pdf.PdfWriter;

/**
 * @author sanda zaharia (shertage@users.sourceforge.net)
 * @version $Id: TextInputElementPdfHandler.java 5922 2013-02-19 11:03:27Z teodord $
 */
public class IconLabelElementPdfHandler implements GenericElementPdfHandler
{

	private String fieldNameParameter;
	private String textParameter;
	private String defaultTextParameter;
	
	public void exportElement(JRPdfExporterContext exporterContext, JRGenericPrintElement element)
	{
		PdfWriter writer = exporterContext.getPdfWriter();
		JasperPrint jasperPrint = exporterContext.getExportedReport();
		
		JRPrintText labelPrintText = (JRPrintText)element.getParameterValue(IconLabelElement.PARAMETER_PRINT_TEXT_ELEMENT);
		if (labelPrintText == null) //FIXMEINPUT deal with xml serialization
		{
			return;
		}
		
		JRPdfExporter exporter = (JRPdfExporter)exporterContext.getExporter();
		labelPrintText.setX(element.getX());//FIXMESORT
		labelPrintText.setY(element.getY());
        try
        {
        	exporter.exportText(labelPrintText);
        }
        catch(Exception e)
        {
        	throw new JRRuntimeException(e);
        }

		JRPrintText iconPrintText = (JRPrintText)element.getParameterValue(IconLabelElement.PARAMETER_PRINT_TEXT_ELEMENT + ".icon");
		if (iconPrintText != null) //FIXMEINPUT deal with xml serialization
		{
			iconPrintText.setX(element.getX() + iconPrintText.getX());//FIXMESORT
			iconPrintText.setY(element.getY() + iconPrintText.getY());
	        try
	        {
	        	exporter.exportText(iconPrintText);
	        }
	        catch(Exception e)
	        {
	        	throw new JRRuntimeException(e);
	        }
		}
        
//        printText.setText("A");//\u25B2");
//        printText.setBackcolor(Color.yellow);
//        printText.setForecolor(Color.red);
//		printText.setX(element.getX() + element.getWidth() - element.getHeight());//FIXMESORT
//		printText.setFontName("DejaVu Sans");
//        try
//        {
//        	exporter.exportText(printText);
//        }
//        catch(Exception e)
//        {
//        	throw new JRRuntimeException(e);
//        }
        
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
