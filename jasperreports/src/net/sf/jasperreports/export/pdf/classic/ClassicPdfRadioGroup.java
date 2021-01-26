/*
 * JasperReports - Free Java Reporting Library.
 * Copyright (C) 2001 - 2019 TIBCO Software Inc. All rights reserved.
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
package net.sf.jasperreports.export.pdf.classic;

import java.io.IOException;

import com.lowagie.text.DocumentException;
import com.lowagie.text.pdf.PdfFormField;
import com.lowagie.text.pdf.RadioCheckField;

import net.sf.jasperreports.engine.JRRuntimeException;
import net.sf.jasperreports.export.pdf.PdfRadioCheck;
import net.sf.jasperreports.export.pdf.PdfRadioGroup;

/**
 * 
 * @author Lucian Chirita (lucianc@users.sourceforge.net)
 */
public class ClassicPdfRadioGroup implements PdfRadioGroup
{

	private ClassicPdfProducer pdfProducer;
	private PdfFormField radioGroup;

	public ClassicPdfRadioGroup(ClassicPdfProducer pdfProducer, PdfFormField radioGroup)
	{
		this.pdfProducer = pdfProducer;
		this.radioGroup = radioGroup;
	}

	@Override
	public void add()
	{
		pdfProducer.getPdfWriter().addAnnotation(radioGroup);
	}

	@Override
	public void addKid(PdfRadioCheck radioField) throws IOException
	{
		RadioCheckField radioCheckField = ((ClassicRadioCheck) radioField).getRadioCheckField();
		try
		{
			radioGroup.addKid(radioCheckField.getRadioField());
		}
		catch (DocumentException e)
		{
			throw new JRRuntimeException(e);
		}
	}

}
