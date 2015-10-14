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

import java.io.ByteArrayOutputStream;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.adobe.xmp.XMPConst;
import com.adobe.xmp.XMPException;
import com.adobe.xmp.XMPMeta;
import com.adobe.xmp.XMPMetaFactory;
import com.adobe.xmp.options.PropertyOptions;
import com.adobe.xmp.options.SerializeOptions;
import com.lowagie.text.Document;
import com.lowagie.text.pdf.PdfDate;
import com.lowagie.text.pdf.PdfDictionary;
import com.lowagie.text.pdf.PdfName;
import com.lowagie.text.pdf.PdfString;
import com.lowagie.text.pdf.PdfWriter;
import com.lowagie.text.xml.xmp.DublinCoreSchema;

import net.sf.jasperreports.engine.JRRuntimeException;

/**
 * @author Lucian Chirita (lucianc@users.sourceforge.net)
 */
public class PdfXmpCreator
{

	private static final Log log = LogFactory.getLog(PdfXmpCreator.class);
	
	private static final boolean XMP_LIBRARY;

	static
	{
		XMP_LIBRARY = determineXmpLibrary();
	}

	private static boolean determineXmpLibrary()
	{
		try
		{
			Class.forName("com.adobe.xmp.XMPMetaFactory");
			return true;
		} catch (ClassNotFoundException e)
		{
			log.info("Adobe XMP library not found");
			return false;
		}
	}

	public static boolean supported()
	{
		return XMP_LIBRARY;
	}

	public static byte[] createXmpMetadata(PdfWriter pdfWriter)
	{
		XmpWriter writer = new XmpWriter(pdfWriter);
		return writer.createXmpMetadata();
	}

}


class XmpWriter
{
	private static final String FORMAT_PDF = "application/pdf";

	private static final String PDF_PRODUCER = "Producer";

	private static final String PDF_KEYWORDS = "Keywords";

	private static final String PDFA_PART = "part";

	private static final String PDFA_CONFORMANCE = "conformance";

	private static final String PDFA_PART_1 = "1";

	private static final String PDFA_CONFORMANCE_A = "A";

	private static final String PDFA_CONFORMANCE_B = "B";

	private static final String XMP_CREATE_DATE = "CreateDate";

	private static final String XMP_MODIFY_DATE = "ModifyDate";

	private static final String XMP_CREATOR_TOOL = "CreatorTool";
	
	private final PdfWriter pdfWriter;
	private final PdfDictionary info;
	
	XmpWriter(PdfWriter pdfWriter)
	{
		this.pdfWriter = pdfWriter;
		this.info = pdfWriter.getInfo();
	}
	
	byte[] createXmpMetadata()
	{
		try
		{
			XMPMeta xmp = XMPMetaFactory.create();
			xmp.setObjectName("");

			xmp.setProperty(XMPConst.NS_DC, DublinCoreSchema.FORMAT, FORMAT_PDF);
			xmp.setProperty(XMPConst.NS_PDF, PDF_PRODUCER, Document.getVersion());

			if (pdfWriter.getPDFXConformance() == PdfWriter.PDFA1A)
			{
				xmp.setProperty(XMPConst.NS_PDFA_ID, PDFA_PART, PDFA_PART_1);
				xmp.setProperty(XMPConst.NS_PDFA_ID, PDFA_CONFORMANCE, PDFA_CONFORMANCE_A);
			}
			else if (pdfWriter.getPDFXConformance() == PdfWriter.PDFA1B)
			{
				xmp.setProperty(XMPConst.NS_PDFA_ID, PDFA_PART, PDFA_PART_1);
				xmp.setProperty(XMPConst.NS_PDFA_ID, PDFA_CONFORMANCE, PDFA_CONFORMANCE_B);
			}

			xmp.setProperty(XMPConst.NS_XMP, XMP_CREATE_DATE, ((PdfDate) info.get(PdfName.CREATIONDATE)).getW3CDate());
			xmp.setProperty(XMPConst.NS_XMP, XMP_MODIFY_DATE, ((PdfDate) info.get(PdfName.MODDATE)).getW3CDate());

			String title = extractInfo(PdfName.TITLE);
			if (title != null)
			{
				xmp.setLocalizedText(XMPConst.NS_DC, DublinCoreSchema.TITLE, 
						//FIXME use the tag language?
						XMPConst.X_DEFAULT, XMPConst.X_DEFAULT, title);
			}

			String author = extractInfo(PdfName.AUTHOR);
			if (author != null)
			{
				//FIXME cache the options?
				PropertyOptions arrayOrdered = new PropertyOptions().setArrayOrdered(true);
				xmp.appendArrayItem(XMPConst.NS_DC, DublinCoreSchema.CREATOR, arrayOrdered, author, null);
			}

			String subject = extractInfo(PdfName.SUBJECT);
			if (subject != null)
			{
				PropertyOptions array = new PropertyOptions().setArray(true);
				xmp.appendArrayItem(XMPConst.NS_DC, DublinCoreSchema.SUBJECT, array, subject, null);
				xmp.setLocalizedText(XMPConst.NS_DC, DublinCoreSchema.DESCRIPTION, 
						XMPConst.X_DEFAULT, XMPConst.X_DEFAULT, subject);
			}

			String keywords = extractInfo(PdfName.KEYWORDS);
			if (keywords != null)
			{
				xmp.setProperty(XMPConst.NS_PDF, PDF_KEYWORDS, keywords);
			}

			String creator = extractInfo(PdfName.CREATOR);
			if (creator != null)
			{
				xmp.setProperty(XMPConst.NS_XMP, XMP_CREATOR_TOOL, creator);
			}

			SerializeOptions options = new SerializeOptions();
			options.setUseCanonicalFormat(true);

			ByteArrayOutputStream out = new ByteArrayOutputStream(4096);
			XMPMetaFactory.serialize(xmp, out, options);
			return out.toByteArray();
		}
		catch (XMPException e)
		{
			throw new JRRuntimeException(e);
		}
	}
	
	private String extractInfo(PdfName key)
	{
		PdfString value = (PdfString) info.get(key);
		return value == null ? null : value.toUnicodeString();
	}
}