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
package net.sf.jasperreports.engine.export.ooxml;

import java.io.Writer;
import java.util.List;

import net.sf.jasperreports.engine.JRStyle;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReportsContext;
import net.sf.jasperreports.engine.design.JRDesignStyle;
import net.sf.jasperreports.engine.util.JRDataUtils;
import net.sf.jasperreports.engine.util.JRStyleResolver;
import net.sf.jasperreports.export.ExporterInput;
import net.sf.jasperreports.export.ExporterInputItem;


/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 */
public class DocxStyleHelper extends BaseHelper
{
	/**
	 * 
	 */
	private DocxParagraphHelper paragraphHelper;
	private DocxRunHelper runHelper;
	
	/**
	 * 
	 */
	public DocxStyleHelper(JasperReportsContext jasperReportsContext, Writer writer, String exporterKey)
	{
		super(jasperReportsContext, writer);
		
		paragraphHelper = new DocxParagraphHelper(jasperReportsContext, writer, false);
		runHelper = new DocxRunHelper(jasperReportsContext, writer, exporterKey);
	}

	/**
	 * 
	 */
	public void export(ExporterInput exporterInput)
	{
		write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
		write("<w:styles\n");
		write(" xmlns:r=\"http://schemas.openxmlformats.org/officeDocument/2006/relationships\"\n");
		write(" xmlns:w=\"http://schemas.openxmlformats.org/wordprocessingml/2006/main\">\n");
		write(" <w:docDefaults>\n");
		write("  <w:rPrDefault>\n");
		//write("   <w:rPr>\n");
		//write("    <w:rFonts w:ascii=\"Times New Roman\" w:eastAsia=\"Times New Roman\" w:hAnsi=\"Times New Roman\" w:cs=\"Times New Roman\"/>\n");
		//write("   </w:rPr>\n");
		write("  </w:rPrDefault>\n");
		write("  <w:pPrDefault>\n");
		write("  <w:pPr>\n");
		write("  <w:spacing w:line=\"" + DocxParagraphHelper.LINE_SPACING_FACTOR + "\"/>\n");
		write("  </w:pPr>\n");
		write("  </w:pPrDefault>\n");
		write(" </w:docDefaults>\n");

		List<ExporterInputItem> items = exporterInput.getItems();

		for(int reportIndex = 0; reportIndex < items.size(); reportIndex++)
		{
			ExporterInputItem item = items.get(reportIndex);
			JasperPrint jasperPrint = item.getJasperPrint();
			
			String localeCode = jasperPrint.getLocaleCode();
			
			if (reportIndex == 0)
			{
				JRDesignStyle style = new JRDesignStyle();
				style.setName("EMPTY_CELL_STYLE");
				style.setParentStyle(jasperPrint.getDefaultStyle());
				style.setFontSize(0f);
				exportHeader(style);
				paragraphHelper.exportProps(style);
				runHelper.exportProps(style, (localeCode == null ? null : JRDataUtils.getLocale(localeCode)));//FIXMEDOCX reuse exporter
				exportFooter();
			}
			
			JRStyle[] styles = jasperPrint.getStyles();
			if (styles != null)
			{
				for(int i = 0; i < styles.length; i++)
				{
					JRStyle style = styles[i];
					exportHeader(style);
					paragraphHelper.exportProps(style);
					runHelper.exportProps(style, (localeCode == null ? null : JRDataUtils.getLocale(localeCode)));//FIXMEDOCX reuse exporter
					exportFooter();
				}
			}
		}

		write("</w:styles>\n");
	}
	
	/**
	 * 
	 */
	private void exportHeader(JRStyle style)
	{
		//write(" <w:style w:type=\"paragraph\" w:default=\"1\" w:styleId=\"" + style.getName() + "\">\n");
		write(" <w:style w:type=\"paragraph\" w:styleId=\"" + style.getName() + "\"");
		if (style.isDefault())
		{
			write(" w:default=\"1\"");
		}
		write(">\n");
		write("  <w:name w:val=\"" + style.getName() + "\" />\n");
		write("  <w:qFormat />\n");
		JRStyle baseStyle = JRStyleResolver.getBaseStyle(style);
		String styleNameReference = baseStyle == null ? null : baseStyle.getName(); //javadoc says getStyleNameReference is not supposed to work for print elements
		if (styleNameReference != null)
		{
			write("  <w:basedOn w:val=\"" + styleNameReference + "\" />\n");
		}
	}
	
	/**
	 * 
	 */
	private void exportFooter()
	{
		write(" </w:style>\n");
	}
	
}
