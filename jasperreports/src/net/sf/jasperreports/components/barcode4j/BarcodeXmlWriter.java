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
package net.sf.jasperreports.components.barcode4j;

import java.io.IOException;

import net.sf.jasperreports.components.ComponentsExtensionsRegistryFactory;
import net.sf.jasperreports.engine.JRComponentElement;
import net.sf.jasperreports.engine.JRConstants;
import net.sf.jasperreports.engine.JRExpression;
import net.sf.jasperreports.engine.JRRuntimeException;
import net.sf.jasperreports.engine.component.ComponentKey;
import net.sf.jasperreports.engine.type.EvaluationTimeEnum;
import net.sf.jasperreports.engine.util.JRXmlWriteHelper;
import net.sf.jasperreports.engine.util.VersionComparator;
import net.sf.jasperreports.engine.util.XmlNamespace;
import net.sf.jasperreports.engine.xml.JRXmlConstants;
import net.sf.jasperreports.engine.xml.JRXmlWriter;

/**
 * 
 * @author Lucian Chirita (lucianc@users.sourceforge.net)
 */
public class BarcodeXmlWriter implements BarcodeVisitor
{

	private final JRXmlWriteHelper xmlWriteHelper;
	private final BarcodeComponent barcodeComponent;
	private final ComponentKey componentKey;
	private final String version;
	private final VersionComparator versionComparator;
	
	public BarcodeXmlWriter(JRXmlWriter reportWriter, JRComponentElement componentElement)
	{
		this(reportWriter, componentElement, null, new VersionComparator());
	}
	
	public BarcodeXmlWriter(JRXmlWriter reportWriter, JRComponentElement componentElement, String version, VersionComparator versionComparator)
	{
		this.xmlWriteHelper = reportWriter.getXmlWriteHelper();
		this.barcodeComponent = (BarcodeComponent) componentElement.getComponent();
		this.componentKey = componentElement.getComponentKey();
		this.version = version;
		this.versionComparator = versionComparator;
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
	}
	
	protected void endBarcode() throws IOException
	{
		xmlWriteHelper.closeElement();
	}
	
	protected void writeBaseAttributes(BarcodeComponent barcode)
	{
		xmlWriteHelper.addAttribute(JRXmlConstants.ATTRIBUTE_evaluationTime, 
				barcode.getEvaluationTimeValue(), EvaluationTimeEnum.NOW);
		xmlWriteHelper.addAttribute(JRXmlConstants.ATTRIBUTE_evaluationGroup, 
				barcode.getEvaluationGroup());
	}
	
	protected void writeBaseAttributes(Barcode4jComponent barcode)
	{
		writeBaseAttributes((BarcodeComponent)barcode);
		
		xmlWriteHelper.addAttribute("moduleWidth", barcode.getModuleWidth());
		
		if (isNewerVersionOrEqual(version, JRConstants.VERSION_6_0_2))
		{
			xmlWriteHelper.addAttribute("orientation", barcode.getOrientationValue(), OrientationEnum.UP);
		}
		else
		{
			xmlWriteHelper.addAttribute("orientation", barcode.getOrientationValue().getValue(), (Integer)0);
		}
		
		xmlWriteHelper.addAttribute("textPosition", barcode.getTextPositionValue());
		xmlWriteHelper.addAttribute("quietZone", barcode.getQuietZone());
		xmlWriteHelper.addAttribute("verticalQuietZone", barcode.getVerticalQuietZone());
	}

	protected void writeBaseContents(BarcodeComponent barcode) throws IOException
	{
		writeExpression("codeExpression", barcode.getCodeExpression(), false);
	}
	
	protected void writeBaseContents(Barcode4jComponent barcode) throws IOException
	{
		writeBaseContents((BarcodeComponent)barcode);
		
		writeExpression("patternExpression", barcode.getPatternExpression(), false);
	}
	
	public void visitCodabar(CodabarComponent codabar)
	{
		try
		{
			startBarcode(codabar);
			writeBaseAttributes(codabar);
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
			writeBaseAttributes(code128);
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
			writeBaseAttributes(dataMatrix);
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
			writeBaseAttributes(ean128);
			xmlWriteHelper.addAttribute("checksumMode", ean128.getChecksumMode());
			writeBaseContents(ean128);
			if(isNewerVersionOrEqual(version, JRConstants.VERSION_5_1_2))
			{
				writeExpression("templateExpression", ean128.getTemplateExpression(), false);
			}
			endBarcode();
		}
		catch (IOException e)
		{
			throw new JRRuntimeException(e);
		}
	}

	public void visitCode39(Code39Component code39)
	{
		try
		{
			startBarcode(code39);
			writeBaseAttributes(code39);
			xmlWriteHelper.addAttribute("checksumMode", code39.getChecksumMode());
			xmlWriteHelper.addAttribute("displayChecksum", code39.isDisplayChecksum());
			xmlWriteHelper.addAttribute("displayStartStop", code39.isDisplayStartStop());
			xmlWriteHelper.addAttribute("extendedCharSetEnabled", code39.isExtendedCharSetEnabled());
			xmlWriteHelper.addAttribute("intercharGapWidth", code39.getIntercharGapWidth());
			xmlWriteHelper.addAttribute("wideFactor", code39.getWideFactor());
			writeBaseContents(code39);
			endBarcode();
		}
		catch (IOException e)
		{
			throw new JRRuntimeException(e);
		}
	}

	public void visitUPCA(UPCAComponent upcA)
	{
		try
		{
			startBarcode(upcA);
			writeBaseAttributes(upcA);
			xmlWriteHelper.addAttribute("checksumMode", upcA.getChecksumMode());
			writeBaseContents(upcA);
			endBarcode();
		}
		catch (IOException e)
		{
			throw new JRRuntimeException(e);
		}
	}

	public void visitUPCE(UPCEComponent upcE)
	{
		try
		{
			startBarcode(upcE);
			writeBaseAttributes(upcE);
			xmlWriteHelper.addAttribute("checksumMode", upcE.getChecksumMode());
			writeBaseContents(upcE);
			endBarcode();
		}
		catch (IOException e)
		{
			throw new JRRuntimeException(e);
		}
	}

	public void visitEAN13(EAN13Component ean13)
	{
		try
		{
			startBarcode(ean13);
			writeBaseAttributes(ean13);
			xmlWriteHelper.addAttribute("checksumMode", ean13.getChecksumMode());
			writeBaseContents(ean13);
			endBarcode();
		}
		catch (IOException e)
		{
			throw new JRRuntimeException(e);
		}
	}

	public void visitEAN8(EAN8Component ean8)
	{
		try
		{
			startBarcode(ean8);
			writeBaseAttributes(ean8);
			xmlWriteHelper.addAttribute("checksumMode", ean8.getChecksumMode());
			writeBaseContents(ean8);
			endBarcode();
		}
		catch (IOException e)
		{
			throw new JRRuntimeException(e);
		}
	}

	public void visitInterleaved2Of5(Interleaved2Of5Component interleaved2Of5)
	{
		try
		{
			startBarcode(interleaved2Of5);
			writeBaseAttributes(interleaved2Of5);
			xmlWriteHelper.addAttribute("checksumMode", interleaved2Of5.getChecksumMode());
			xmlWriteHelper.addAttribute("displayChecksum", interleaved2Of5.isDisplayChecksum());
			xmlWriteHelper.addAttribute("wideFactor", interleaved2Of5.getWideFactor());
			writeBaseContents(interleaved2Of5);
			endBarcode();
		}
		catch (IOException e)
		{
			throw new JRRuntimeException(e);
		}
	}

	protected void writeFourStateAttributes(FourStateBarcodeComponent barcode)
	{
		xmlWriteHelper.addAttribute("ascenderHeight", barcode.getAscenderHeight());
		xmlWriteHelper.addAttribute("checksumMode", barcode.getChecksumMode());
		xmlWriteHelper.addAttribute("intercharGapWidth", barcode.getIntercharGapWidth());
		xmlWriteHelper.addAttribute("trackHeight", barcode.getTrackHeight());
	}
	
	public void visitRoyalMailCustomer(
			RoyalMailCustomerComponent royalMailCustomer)
	{
		try
		{
			startBarcode(royalMailCustomer);
			writeBaseAttributes(royalMailCustomer);
			writeFourStateAttributes(royalMailCustomer);
			writeBaseContents(royalMailCustomer);
			endBarcode();
		}
		catch (IOException e)
		{
			throw new JRRuntimeException(e);
		}
	}

	public void visitUSPSIntelligentMail(
			USPSIntelligentMailComponent intelligentMail)
	{
		try
		{
			startBarcode(intelligentMail);
			writeBaseAttributes(intelligentMail);
			writeFourStateAttributes(intelligentMail);
			writeBaseContents(intelligentMail);
			endBarcode();
		}
		catch (IOException e)
		{
			throw new JRRuntimeException(e);
		}
	}

	public void visitPostnet(POSTNETComponent postnet)
	{
		try
		{
			startBarcode(postnet);
			writeBaseAttributes(postnet);
			xmlWriteHelper.addAttribute("shortBarHeight", postnet.getShortBarHeight());
			xmlWriteHelper.addAttribute("baselinePosition", postnet.getBaselinePosition());
			xmlWriteHelper.addAttribute("checksumMode", postnet.getChecksumMode());
			xmlWriteHelper.addAttribute("displayChecksum", postnet.getDisplayChecksum());
			xmlWriteHelper.addAttribute("intercharGapWidth", postnet.getIntercharGapWidth());
			writeBaseContents(postnet);
			endBarcode();
		}
		catch (IOException e)
		{
			throw new JRRuntimeException(e);
		}
	}

	public void visitPDF417(PDF417Component pdf417)
	{
		try
		{
			startBarcode(pdf417);
			writeBaseAttributes(pdf417);
			xmlWriteHelper.addAttribute("minColumns", pdf417.getMinColumns());
			xmlWriteHelper.addAttribute("maxColumns", pdf417.getMaxColumns());
			xmlWriteHelper.addAttribute("minRows", pdf417.getMinRows());
			xmlWriteHelper.addAttribute("maxRows", pdf417.getMaxRows());
			xmlWriteHelper.addAttribute("widthToHeightRatio", pdf417.getWidthToHeightRatio());
			xmlWriteHelper.addAttribute("errorCorrectionLevel", pdf417.getErrorCorrectionLevel());
			writeBaseContents(pdf417);
			endBarcode();
		}
		catch (IOException e)
		{
			throw new JRRuntimeException(e);
		}
	}
	
	public void visitQRCode(QRCodeComponent qrCode)
	{
		try
		{
			startBarcode(qrCode);
			writeBaseAttributes(qrCode);
			xmlWriteHelper.addAttribute("margin", qrCode.getMargin());
			xmlWriteHelper.addAttribute(QRCodeComponent.PROPERTY_ERROR_CORRECTION_LEVEL, 
					qrCode.getErrorCorrectionLevel(), 
					ErrorCorrectionLevelEnum.L);
			writeBaseContents(qrCode);
			endBarcode();
		}
		catch (IOException e)
		{
			throw new JRRuntimeException(e);
		}
	}

	@SuppressWarnings("deprecation")
	protected void writeExpression(String name, JRExpression expression, boolean writeClass)  throws IOException
	{
		if(versionComparator.compare(version, JRConstants.VERSION_4_1_1) >= 0 )
		{
			xmlWriteHelper.writeExpression(name, expression);
		}
		else
		{
			xmlWriteHelper.writeExpression(name, expression, writeClass);
		}
	}

	protected boolean isNewerVersionOrEqual(String currentVersion, String oldVersion) 
	{
		return versionComparator.compare(currentVersion, oldVersion) >= 0;
	}
}
