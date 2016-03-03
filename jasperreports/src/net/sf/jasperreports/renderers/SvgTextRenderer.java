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
package net.sf.jasperreports.renderers;

import java.io.IOException;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.util.List;

import org.apache.batik.dom.svg.SVGDocumentFactory;
import org.w3c.dom.svg.SVGDocument;

import net.sf.jasperreports.engine.JRConstants;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRPrintImageAreaHyperlink;
import net.sf.jasperreports.engine.JRRuntimeException;
import net.sf.jasperreports.engine.JasperReportsContext;


/**
 * SVG renderer implementation based on <a href="http://xmlgraphics.apache.org/batik/">Batik</a>.
 *
 * @author Lucian Chirita (lucianc@users.sourceforge.net)
 */
public class SvgTextRenderer extends AbstractSvgRenderer
{
	private static final long serialVersionUID = JRConstants.SERIAL_VERSION_UID;

	private String svgText;

	/**
	 * Creates a SVG renderer.
	 *
	 * @param svgText the SVG text
	 * @param areaHyperlinks a list of {@link JRPrintImageAreaHyperlink area hyperlinks}
	 */
	public SvgTextRenderer(String svgText, List<JRPrintImageAreaHyperlink> areaHyperlinks)
	{
		super(areaHyperlinks);
		
		this.svgText = svgText;
	}

	@Override
	protected SVGDocument getSvgDocument(SVGDocumentFactory documentFactory) throws IOException
	{
		return 
			documentFactory.createSVGDocument(
				null, 
				new StringReader(svgText)
				);
	}

	/**
	 * Creates a SVG renderer from SVG text.
	 *
	 * @param svgText the SVG text
	 * @return a SVG renderer
	 */
	public static SvgTextRenderer getInstance(String svgText)
	{
		return new SvgTextRenderer(svgText, null);
	}

	@Override
	public byte[] getSvgData(JasperReportsContext jasperReportsContext) throws JRException 
	{
		try
		{
			return svgText.getBytes("UTF-8");
		}
		catch (UnsupportedEncodingException e)
		{
			throw new JRRuntimeException(e);
		}
	}
}
