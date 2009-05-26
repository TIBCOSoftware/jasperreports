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
package net.sf.jasperreports.components.barcode4j;

import java.io.IOException;

import net.sf.jasperreports.components.ComponentsExtensionsRegistryFactory;
import net.sf.jasperreports.engine.JRExpression;
import net.sf.jasperreports.engine.JRRuntimeException;
import net.sf.jasperreports.engine.component.ComponentKey;
import net.sf.jasperreports.engine.util.JRXmlWriteHelper;
import net.sf.jasperreports.engine.util.XmlNamespace;
import net.sf.jasperreports.engine.xml.JRXmlConstants;
import net.sf.jasperreports.engine.xml.JRXmlWriter;

/**
 * 
 * @author Lucian Chirita (lucianc@users.sourceforge.net)
 * @version $Id$
 */
public class BarcodeXmlWriter implements BarcodeVisitor
{

	private final JRXmlWriteHelper xmlWriteHelper;
	private final BarcodeComponent barcodeComponent;
	private final ComponentKey componentKey;
	
	public BarcodeXmlWriter(JRXmlWriter reportWriter, 
			BarcodeComponent barcode, ComponentKey componentKey)
	{
		this.xmlWriteHelper = reportWriter.getXmlWriteHelper();
		this.barcodeComponent = barcode;
		this.componentKey = componentKey;
	}
	
	public void writeBarcode()
	{
		barcodeComponent.receive(this);
	}
	
	protected void startBarcode(BarcodeComponent barcode)
	{
		XmlNamespace namespace = new XmlNamespace(
				ComponentsExtensionsRegistryFactory.NAMESPACE, 
				componentKey.getNamespacePrefix(),
				ComponentsExtensionsRegistryFactory.XSD_LOCATION);
		xmlWriteHelper.startElement(componentKey.getName(), namespace);
		writeBaseAttributes(barcode);
	}
	
	protected void endBarcode() throws IOException
	{
		xmlWriteHelper.closeElement();
	}
	
	protected void writeBaseAttributes(BarcodeComponent barcode)
	{
		if (barcode.getEvaluationTime() != JRExpression.EVALUATION_TIME_NOW)
		{
			xmlWriteHelper.addAttribute(JRXmlConstants.ATTRIBUTE_evaluationTime, 
					barcode.getEvaluationTime(),
					JRXmlConstants.getEvaluationTimeMap());
		}
		xmlWriteHelper.addAttribute(JRXmlConstants.ATTRIBUTE_evaluationGroup, 
				barcode.getEvaluationGroup());
		
		xmlWriteHelper.addAttribute("orientation", barcode.getOrientation(), 0);
		xmlWriteHelper.addAttribute("moduleWidth", barcode.getModuleWidth());
		xmlWriteHelper.addAttribute("textPosition", barcode.getTextPosition());
	}
	
	protected void writeBaseContents(BarcodeComponent barcode) throws IOException
	{
		xmlWriteHelper.writeExpression("codeExpression", 
				barcode.getCodeExpression(), false);
		xmlWriteHelper.writeExpression("patternExpression", 
				barcode.getPatternExpression(), false);
	}
	
	public void visitCodabar(CodabarComponent codabar)
	{
		try
		{
			startBarcode(codabar);
			xmlWriteHelper.addAttribute("checksumMode", codabar.getChecksumMode());
			xmlWriteHelper.addAttribute("wideFactor", codabar.getWideFactor());
			writeBaseContents(codabar);
			endBarcode();
		}
		catch (IOException e)
		{
			throw new JRRuntimeException(e);
		}
	}

	public void visitCode128(Code128Component code128)
	{
		try
		{
			startBarcode(code128);
			writeBaseContents(code128);
			endBarcode();
		}
		catch (IOException e)
		{
			throw new JRRuntimeException(e);
		}
	}

	public void visitDataMatrix(DataMatrixComponent dataMatrix)
	{
		try
		{
			startBarcode(dataMatrix);
			xmlWriteHelper.addAttribute("shape", dataMatrix.getShape());
			writeBaseContents(dataMatrix);
			endBarcode();
		}
		catch (IOException e)
		{
			throw new JRRuntimeException(e);
		}
	}

	public void visitEANCode128(EAN128Component ean128)
	{
		try
		{
			startBarcode(ean128);
			xmlWriteHelper.addAttribute("checksumMode", ean128.getChecksumMode());
			writeBaseContents(ean128);
			endBarcode();
		}
		catch (IOException e)
		{
			throw new JRRuntimeException(e);
		}
	}

}
