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
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.List;

import org.apache.batik.dom.svg.SVGDocumentFactory;
import org.w3c.dom.svg.SVGDocument;

import net.sf.jasperreports.engine.JRConstants;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRPrintImageAreaHyperlink;
import net.sf.jasperreports.engine.JasperReportsContext;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.repo.RepositoryUtil;


/**
 * SVG renderer implementation based on <a href="http://xmlgraphics.apache.org/batik/">Batik</a>.
 *
 * @author Lucian Chirita (lucianc@users.sourceforge.net)
 */
public class SvgDataRenderer extends AbstractSvgRenderer
{
	private static final long serialVersionUID = JRConstants.SERIAL_VERSION_UID;

	private byte[] svgData;

	/**
	 * Creates a SVG renderer.
	 *
	 * @param svgData the SVG (binary) data
	 * @param areaHyperlinks a list of {@link JRPrintImageAreaHyperlink area hyperlinks}
	 */
	public SvgDataRenderer(byte[] svgData, List<JRPrintImageAreaHyperlink> areaHyperlinks)
	{
		super(areaHyperlinks);
		
		this.svgData = svgData;
	}

	@Override
	protected SVGDocument getSvgDocument(SVGDocumentFactory documentFactory) throws IOException
	{
		return 
			documentFactory.createSVGDocument(
				null, 
				new ByteArrayInputStream(svgData)
				);
	}

	/**
	 * Creates a SVG renderer from binary data.
	 *
	 * @param svgData the SVG (binary) data
	 * @return a SVG renderer
	 */
	public static SvgDataRenderer getInstance(byte[] svgData)
	{
		return new SvgDataRenderer(svgData, null);
	}

	/**
	 * Creates a SVG renderer from a data stream.
	 *
	 * <p>
	 * Note: the data stream is exhausted, but not closed.
	 * </p>
	 *
	 * @param svgDataStream the SVG binary data stream
	 * @return a SVG renderer
	 * @throws JRException
	 */
	public static SvgDataRenderer getInstance(InputStream svgDataStream) throws JRException
	{
		byte[] data = JRLoader.loadBytes(svgDataStream);
		return new SvgDataRenderer(data, null);
	}

	/**
	 * Creates a SVG renderer from a file.
	 *
	 * @param svgFile the SVG file to read
	 * @return a SVG renderer
	 * @throws JRException
	 */
	public static SvgDataRenderer getInstance(File svgFile) throws JRException
	{
		byte[] data = JRLoader.loadBytes(svgFile);
		return new SvgDataRenderer(data, null);
	}

	/**
	 * Creates a SVG renderer from a {@link URL}.
	 *
	 * @param svgURL the SVG URL
	 * @return a SVG renderer
	 * @throws JRException
	 */
	public static SvgDataRenderer getInstance(URL svgURL) throws JRException
	{
		byte[] data = JRLoader.loadBytes(svgURL);
		return new SvgDataRenderer(data, null);
	}

	/**
	 * Creates a SVG renderer by loading data from a generic location.
	 *
	 * @param location the location
	 * @return a SVG renderer
	 * @throws JRException
	 * @see RepositoryUtil#getBytesFromLocation(String)
	 */
	public static SvgDataRenderer getInstanceFromLocation(JasperReportsContext jasperReportsContext, String location) throws JRException
	{
		byte[] data = RepositoryUtil.getInstance(jasperReportsContext).getBytesFromLocation(location);
		return new SvgDataRenderer(data, null);
	}

	@Override
	public byte[] getSvgData(JasperReportsContext jasperReportsContext) throws JRException 
	{
		return svgData;
	}
}
