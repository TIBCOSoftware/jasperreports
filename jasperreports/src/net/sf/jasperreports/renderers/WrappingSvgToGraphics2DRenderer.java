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

import java.io.ByteArrayInputStream;
import java.io.IOException;
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
public class WrappingSvgToGraphics2DRenderer extends AbstractSvgRenderer
{
	private static final long serialVersionUID = JRConstants.SERIAL_VERSION_UID;

	private SvgRenderable svgRenderer;

	/**
	 *
	 */
	public WrappingSvgToGraphics2DRenderer(
		SvgRenderable svgRenderer, 
		List<JRPrintImageAreaHyperlink> areaHyperlinks
		)
	{
		super(areaHyperlinks);
		
		this.svgRenderer = svgRenderer;
	}

	@Override
	protected SVGDocument getSvgDocument(
		JasperReportsContext jasperReportsContext,
		SVGDocumentFactory documentFactory
		) throws JRException
	{
		try
		{
			return 
				documentFactory.createSVGDocument(
					null, 
					new ByteArrayInputStream(
						svgRenderer.getSvgData(jasperReportsContext)
						)
					);
		}
		catch (IOException e)
		{
			throw new JRRuntimeException(e);
		}
	}

	@Override
	public byte[] getSvgData(JasperReportsContext jasperReportsContext) throws JRException 
	{
		return svgRenderer.getSvgData(jasperReportsContext);
	}
}
