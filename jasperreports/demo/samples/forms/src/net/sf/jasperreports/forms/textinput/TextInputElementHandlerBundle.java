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

import net.sf.jasperreports.engine.export.GenericElementHandler;
import net.sf.jasperreports.engine.export.GenericElementHandlerBundle;
import net.sf.jasperreports.engine.export.JRPdfExporter;

/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 */
public final class TextInputElementHandlerBundle implements GenericElementHandlerBundle
{
	public static final String NAMESPACE = "http://jasperreports.sourceforge.net/jasperreports/textinput";
	
	private static final TextInputElementHandlerBundle INSTANCE = new TextInputElementHandlerBundle();
	
	public static TextInputElementHandlerBundle getInstance()
	{
		return INSTANCE;
	}
	
	public String getNamespace()
	{
		return NAMESPACE;
	}
	
	public GenericElementHandler getHandler(String elementName,
			String exporterKey)
	{
//		if (NAME.equals(elementName) 
//				&& JRXhtmlExporter.XHTML_EXPORTER_KEY.equals(exporterKey))
//		{
//			return new HtmlElementXhtmlHandler();
//		} 
//		else if (NAME.equals(elementName)
		if (TextInputElement.TEXT_INPUT_ELEMENT_NAME.equals(elementName)
				&& JRPdfExporter.PDF_EXPORTER_KEY.equals(exporterKey))
		{
			return new TextInputElementPdfHandler();
		}
//		else if (NAME.equals(elementName)
//				&& JRHtmlExporter.HTML_EXPORTER_KEY.equals(exporterKey))
//		{
//			return new HtmlElementHtmlHandler();
//		}
//		else if (NAME.equals(elementName)
//				&& JRXlsExporter.XLS_EXPORTER_KEY.equals(exporterKey))
//		{
//			return new HtmlElementXlsHandler();
//		}
//		else if (NAME.equals(elementName)
//				&& JExcelApiExporter.JXL_EXPORTER_KEY.equals(exporterKey))
//		{
//			return new HtmlElementJExcelHandler();
//		}		
//		else if (NAME.equals(elementName)
//				&& JRGraphics2DExporter.GRAPHICS2D_EXPORTER_KEY.equals(exporterKey))
//		{
//			return new HtmlElementGraphics2DHandler();
//		}		
//		else if (NAME.equals(elementName)
//				&& JRDocxExporter.DOCX_EXPORTER_KEY.equals(exporterKey))
//		{
//			return new HtmlElementDocxHandler();
//		}		
//		else if (NAME.equals(elementName)
//				&& JRPptxExporter.PPTX_EXPORTER_KEY.equals(exporterKey))
//		{
//			return new HtmlElementPptxHandler();
//		}
//		else if (NAME.equals(elementName)
//				&& JRXlsxExporter.XLSX_EXPORTER_KEY.equals(exporterKey))
//		{
//			return new HtmlElementXlsxHandler();
//		}
//		else if (NAME.equals(elementName)
//				&& JRRtfExporter.RTF_EXPORTER_KEY.equals(exporterKey))
//		{
//			return new HtmlElementRtfHandler();
//		}
//		else if (NAME.equals(elementName)
//				&& JROdtExporter.ODT_EXPORTER_KEY.equals(exporterKey))
//		{
//			return new HtmlElementOdtHandler();
//		}
//		else if (NAME.equals(elementName)
//				&& JROdsExporter.ODS_EXPORTER_KEY.equals(exporterKey))
//		{
//			return new HtmlElementOdsHandler();
//		}		
		return null;
	}
}