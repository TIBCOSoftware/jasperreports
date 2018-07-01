/*
 * JasperReports - Free Java Reporting Library.
 * Copyright (C) 2001 - 2018 TIBCO Software Inc. All rights reserved.
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

import java.awt.Graphics2D;
import java.awt.geom.Dimension2D;
import java.awt.geom.Rectangle2D;
import java.util.List;

import net.sf.jasperreports.engine.DefaultJasperReportsContext;
import net.sf.jasperreports.engine.JRConstants;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRPrintImageAreaHyperlink;
import net.sf.jasperreports.engine.JasperReportsContext;
import net.sf.jasperreports.engine.type.RenderableTypeEnum;
import net.sf.jasperreports.renderers.util.RendererUtil;


/**
 * SVG renderer implementation based on <a href="http://xmlgraphics.apache.org/batik/">Batik</a>.
 *
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @deprecated To be removed.
 */
public class WrappingDataToGraphics2DRenderer extends AbstractRenderToImageAwareRenderer implements DataRenderable, Graphics2DRenderable, DimensionRenderable, AreaHyperlinksRenderable
{
	private static final long serialVersionUID = JRConstants.SERIAL_VERSION_UID;

	private final DataRenderable dataRenderer;
	private final AreaHyperlinksRenderable areaHyperlinksRenderer;

	private JasperReportsContext jasperReportsContext;
	private byte[] data;
	private RenderableTypeEnum renderableType;
	private Graphics2DRenderable grxRenderer; 
	private DimensionRenderable dimensionRenderer; 

	/**
	 *
	 */
	public WrappingDataToGraphics2DRenderer(DataRenderable dataRenderer)
	{
		this.dataRenderer = dataRenderer;
		this.areaHyperlinksRenderer = dataRenderer instanceof AreaHyperlinksRenderable ? (AreaHyperlinksRenderable)dataRenderer : null;
	}

	@Override
	public List<JRPrintImageAreaHyperlink> getImageAreaHyperlinks(Rectangle2D renderingArea) throws JRException
	{
		return areaHyperlinksRenderer == null ? null : areaHyperlinksRenderer.getImageAreaHyperlinks(renderingArea);
	}

	@Override
	public boolean hasImageAreaHyperlinks()
	{
		return areaHyperlinksRenderer == null ? false : areaHyperlinksRenderer.hasImageAreaHyperlinks();
	}

	@Override
	public byte[] getData(JasperReportsContext jasperReportsContext) throws JRException 
	{
		updateCache(jasperReportsContext);

		return data;
	}

	@Override
	public Dimension2D getDimension(JasperReportsContext jasperReportsContext) throws JRException 
	{
		updateCache(jasperReportsContext);
		
		return dimensionRenderer.getDimension(jasperReportsContext);
	}

	@Override
	public void render(
		JasperReportsContext jasperReportsContext, 
		Graphics2D grx, 
		Rectangle2D rectangle
		) throws JRException 
	{
		updateCache(jasperReportsContext);
		
		grxRenderer.render(jasperReportsContext, grx, rectangle);
	}

	public RenderableTypeEnum getRenderableType()
	{
		try
		{
			updateCache(DefaultJasperReportsContext.getInstance());
		}
		catch (JRException e)
		{
			//ignore
			return RenderableTypeEnum.IMAGE; // arbitrary default; should not get here in normal cases
		}
		
		return renderableType;
	}
	
	protected void updateCache(JasperReportsContext jasperReportsContext) throws JRException
	{
		if (
			this.jasperReportsContext != jasperReportsContext
			|| data == null
			)
		{
			this.jasperReportsContext = jasperReportsContext;
			this.data = dataRenderer.getData(jasperReportsContext);
			
			boolean isSvgData = RendererUtil.getInstance(jasperReportsContext).isSvgData(data);
			if (isSvgData)
			{
				grxRenderer = new WrappingSvgDataToGraphics2DRenderer(dataRenderer);
				renderableType = RenderableTypeEnum.SVG;
			}
			else
			{
				grxRenderer = new WrappingImageDataToGraphics2DRenderer(dataRenderer);
				renderableType = RenderableTypeEnum.IMAGE;
			}
			this.dimensionRenderer = (DimensionRenderable)grxRenderer;
		}
	}
}
