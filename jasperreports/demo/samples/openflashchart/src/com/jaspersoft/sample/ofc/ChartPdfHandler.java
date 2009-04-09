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
package com.jaspersoft.sample.ofc;

import java.util.HashMap;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRGenericPrintElement;
import net.sf.jasperreports.engine.export.GenericElementPdfHandler;
import net.sf.jasperreports.engine.export.JRPdfExporterContext;
import net.sf.jasperreports.engine.util.JRLoader;

import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.PdfAnnotation;
import com.lowagie.text.pdf.PdfArray;
import com.lowagie.text.pdf.PdfDictionary;
import com.lowagie.text.pdf.PdfFileSpecification;
import com.lowagie.text.pdf.PdfIndirectObject;
import com.lowagie.text.pdf.PdfName;
import com.lowagie.text.pdf.PdfNameTree;
import com.lowagie.text.pdf.PdfString;
import com.lowagie.text.pdf.PdfWriter;

/**
 * @author Lucian Chirita (lucianc@users.sourceforge.net)
 * @version $Id$
 */
public class ChartPdfHandler implements GenericElementPdfHandler
{

	public static final String PARAMETER_CHART_DATA = "ChartData";

	public boolean toExport(JRGenericPrintElement element)
	{
		return true;
	}
	
	public void exportElement(JRPdfExporterContext exporterContext,
			JRGenericPrintElement element)
	{
		//TODO add catalog extensions dictionary
		
		try
		{
			String chartData = (String) element.getParameterValue(PARAMETER_CHART_DATA);
			
			PdfWriter writer = exporterContext.getPdfWriter();
			
			Rectangle rect = new Rectangle(element.getX() + exporterContext.getOffsetX(), 
					exporterContext.getExportedReport().getPageHeight() - element.getY() - exporterContext.getOffsetY(), 
					element.getX() + exporterContext.getOffsetX() + element.getWidth(),
					exporterContext.getExportedReport().getPageHeight() - element.getY() - exporterContext.getOffsetY() - element.getHeight());
			PdfAnnotation ann = new PdfAnnotation(writer, rect);
			ann.put(PdfName.SUBTYPE, new PdfName("RichMedia"));
			
			PdfDictionary settings = new PdfDictionary();
			PdfDictionary activation = new PdfDictionary();
			activation.put(new PdfName("Condition"), new PdfName("PV"));
			settings.put(new PdfName("Activation"), activation);
			ann.put(new PdfName("RichMediaSettings"), settings);
			
			PdfDictionary content = new PdfDictionary();
			
			//TODO reuse the swf
			HashMap assets = new HashMap();
			byte[] swfData = readSwf();
			PdfFileSpecification swfFile = PdfFileSpecification.fileEmbedded(writer, 
					null, "Open Flash Chart", swfData);
			PdfIndirectObject swfRef = writer.addToBody(swfFile);
			assets.put("map.swf", swfRef.getIndirectReference());
			PdfDictionary assetsDictionary = PdfNameTree.writeTree(assets, writer);
			content.put(new PdfName("Assets"), assetsDictionary);
			
			PdfArray configurations = new PdfArray();
			
			PdfDictionary configuration = new PdfDictionary();
			
			PdfArray instances = new PdfArray();
			PdfDictionary instance = new PdfDictionary();
			instance.put(new PdfName("Subtype"), new PdfName("Flash"));
			PdfDictionary params = new PdfDictionary();
			String vars = "inline_data=" + chartData;
			params.put(new PdfName("FlashVars"), new PdfString(vars));
			instance.put(new PdfName("Params"), params);
			instance.put(new PdfName("Asset"), swfRef.getIndirectReference());
			PdfIndirectObject instanceRef = writer.addToBody(instance);
			instances.add(instanceRef.getIndirectReference());
			configuration.put(new PdfName("Instances"), instances);
			
			PdfIndirectObject configurationRef = writer.addToBody(configuration);
			configurations.add(configurationRef.getIndirectReference());
			content.put(new PdfName("Configurations"), configurations);
			
			ann.put(new PdfName("RichMediaContent"), content);
			
			writer.addAnnotation(ann);
		}
		catch (Exception e)
		{
			throw new RuntimeException(e);
		}
	}

	protected byte[] readSwf() throws JRException
	{
		return JRLoader.loadBytesFromLocation("openflashchart/open-flash-chart.swf");
	}
}
