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

import java.awt.Color;
import java.text.AttributedCharacterIterator.Attribute;
import java.util.Locale;
import java.util.Map;

import com.lowagie.text.Font;
import com.lowagie.text.pdf.BaseField;
import com.lowagie.text.pdf.TextField;

import net.sf.jasperreports.engine.export.type.PdfFieldBorderStyleEnum;
import net.sf.jasperreports.export.pdf.PdfField;
import net.sf.jasperreports.export.pdf.PdfTextAlignment;

/**
 * 
 * @author Lucian Chirita (lucianc@users.sourceforge.net)
 */
public abstract class ClassicPdfField implements PdfField
{

	protected ClassicPdfProducer pdfProducer;
	private BaseField field;
	
	public ClassicPdfField(ClassicPdfProducer pdfProducer, BaseField field)
	{
		this.pdfProducer = pdfProducer;
		this.field = field;
	}

	@Override
	public void setBorderWidth(float borderWidth)
	{
		float width = borderWidth > BaseField.BORDER_WIDTH_THICK ? BaseField.BORDER_WIDTH_THICK : borderWidth;
		field.setBorderWidth(width);
	}

	@Override
	public void setBackgroundColor(Color backcolor)
	{
		field.setBackgroundColor(backcolor);
	}

	@Override
	public void setTextColor(Color forecolor)
	{
		field.setTextColor(forecolor);
	}

	@Override
	public void setAlignment(PdfTextAlignment alignment)
	{
		field.setAlignment(ClassicPdfUtils.toITextAlignment(alignment));
	}

	@Override
	public void setBorderColor(Color lineColor)
	{
		field.setBorderColor(lineColor);
	}

	@Override
	public void setBorderStyle(PdfFieldBorderStyleEnum borderStyle)
	{
		field.setBorderStyle(borderStyle.getValue());
	}

	@Override
	public void setReadOnly()
	{
		field.setOptions(field.getOptions() | TextField.READ_ONLY);
	}

	@Override
	public void setText(String value)
	{
		field.setText(value);
	}

	@Override
	public void setFont(Map<Attribute, Object> attributes, Locale locale)
	{
		Font font = pdfProducer.getFont(attributes, locale);
		field.setFont(font.getBaseFont());
	}
	
	@Override
	public void setFontSize(float fontsize)
	{
		field.setFontSize(fontsize);
	}

	@Override
	public void setRotation(int rotation)
	{
		field.setRotation(rotation);
	}

	@Override
	public void setVisible()
	{
		field.setVisibility(TextField.VISIBLE);
	}

}
