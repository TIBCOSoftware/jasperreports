/*******************************************************************************
 * Copyright (C) 2005 - 2014 TIBCO Software Inc. All rights reserved.
 * http://www.jaspersoft.com.
 * 
 * Unless you have purchased  a commercial license agreement from Jaspersoft,
 * the following license terms  apply:
 * 
 * The Custom Visualization Component program and the accompanying materials
 * has been dual licensed under the the following licenses:
 * 
 * Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * The Custom Visualization Component is distributed in the hope that it will be
 * useful, but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License.
 * If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/
package com.jaspersoft.jasperreports.customvisualization.export;

import com.jaspersoft.jasperreports.customvisualization.CVConstants;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import com.jaspersoft.jasperreports.customvisualization.CVPrintElement;
import java.io.UnsupportedEncodingException;
import java.util.logging.Level;
import java.util.logging.Logger;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRGenericPrintElement;
import net.sf.jasperreports.engine.JRPrintImage;
import net.sf.jasperreports.engine.JRPropertiesUtil;
import net.sf.jasperreports.engine.JasperReportsContext;
import net.sf.jasperreports.engine.base.JRBasePrintImage;
import net.sf.jasperreports.engine.type.HorizontalImageAlignEnum;
import net.sf.jasperreports.engine.type.OnErrorTypeEnum;
import net.sf.jasperreports.engine.type.ScaleImageEnum;
import net.sf.jasperreports.engine.type.VerticalImageAlignEnum;
import net.sf.jasperreports.renderers.Renderable;
import net.sf.jasperreports.renderers.SimpleRenderToImageAwareDataRenderer;
import net.sf.jasperreports.renderers.util.RendererUtil;

/**
 * @author Giulio Toffoli (gtoffoli@tibco.com)
 */
public abstract class CVElementImageProvider
{
	public static final String TEMP_RESOURCE_PREFIX = "cv_component_";

	public static final String TEMPORARY_SVG = "com/jaspersoft/jasperreports/customvisualization/export/temporary.svg";

	private static final CVElementImageProvider defaultProvider = new CVElementPhantomJSImageProvider();

	public static CVElementImageProvider getDefaultProvider()
	{
		return defaultProvider;
	}

	/**
	 * Produce an image for the element.
	 * 
	 * This method is invoked by JasperReports all the times the report needs to
	 * be rendered. It is called even if the element itself is not yet ready to
	 * be painted (in example when the viewer is displaying the first page
	 * containing this element, but this element has evaluation time report).
	 * 
	 * The method creates, based on the availability to the configuration
	 * object, two cached images: a temporary image and the final image, cached
	 * as CVPrintElement.PARAMETER_TEMPORARY_CACHE_RENDERER and
	 * CVPrintElement.PARAMETER_CACHE_RENDERER.
	 * 
	 * 
	 * @param jasperReportsContext
	 * @param element
	 * @param createSvg
	 * @return
	 * @throws JRException
	 */
	public JRPrintImage getImage(
		JasperReportsContext jasperReportsContext, 
		JRGenericPrintElement element
		) throws JRException
	{
		JRBasePrintImage printImage = new JRBasePrintImage(element.getDefaultStyleProvider());

		printImage.setUUID(element.getUUID());
		printImage.setX(element.getX());
		printImage.setY(element.getY());
		printImage.setWidth(element.getWidth());
		printImage.setHeight(element.getHeight());
		printImage.setStyle(element.getStyle());
		printImage.setMode(element.getModeValue());
		printImage.setBackcolor(element.getBackcolor());
		printImage.setForecolor(element.getForecolor());

		printImage.setScaleImage(ScaleImageEnum.RETAIN_SHAPE);
		printImage.setHorizontalImageAlign(HorizontalImageAlignEnum.LEFT);
		printImage.setVerticalImageAlign(VerticalImageAlignEnum.TOP);

		OnErrorTypeEnum onErrorType = OnErrorTypeEnum.BLANK; // CVPrintElement.DEFAULT_ON_ERROR_TYPE;
		// OnErrorTypeEnum onErrorType =
		// element.getParameterValue(CVPrintElement.PARAMETER_ON_ERROR_TYPE) ==
		// null
		// ? CVPrintElement.DEFAULT_ON_ERROR_TYPE
		// :
		// OnErrorTypeEnum.getByName((String)element.getParameterValue(CVPrintElement.PARAMETER_ON_ERROR_TYPE));
		printImage.setOnErrorType(onErrorType);

		Renderable cacheRenderer = null;

		if (element.getParameterValue(CVPrintElement.CONFIGURATION) == null)
		{
			// We did not finished to export the chart yet, we wcan just return
			// an empty image....
			cacheRenderer = (Renderable) element.getParameterValue(CVPrintElement.PARAMETER_TEMPORARY_CACHE_RENDERER);
			if (cacheRenderer == null)
			{
				cacheRenderer = RendererUtil.getInstance(jasperReportsContext).getNonLazyRenderable(TEMPORARY_SVG,
						onErrorType);
				element.setParameterValue(CVPrintElement.PARAMETER_TEMPORARY_CACHE_RENDERER, cacheRenderer);
			}
		}
		else
		{
			cacheRenderer = (Renderable) element.getParameterValue(CVPrintElement.PARAMETER_SVG_CACHE_RENDERER);
			if (cacheRenderer == null)
			{
				String svgString = createSvgImage(jasperReportsContext, element);

				SimpleRenderToImageAwareDataRenderer renderer;
                                try {
                                    renderer = new SimpleRenderToImageAwareDataRenderer(svgString.getBytes("UTF-8"), null);

                                    // Configure min DPI
                                    int minDPI = JRPropertiesUtil.getInstance(jasperReportsContext)
                                                .getIntegerProperty(CVConstants.CV_PNG_MIN_DPI, CVConstants.CV_PNG_MIN_DPI_DEFAULT_VALUE);

                                    renderer.setMinDPI(minDPI);

                                    // Configure Antialiasing
                                    boolean useAntiAlias = JRPropertiesUtil.getInstance(jasperReportsContext)
                                                .getBooleanProperty(CVConstants.CV_PNG_ANTIALIAS, CVConstants.CV_PNG_ANTIALIAS_DEFAULT_VALUE);

                                    renderer.setAntiAlias(useAntiAlias);

                                } catch (UnsupportedEncodingException ex) {
                                    throw new JRException(ex); // Very unlikely UTF-8 is not supported.
                                }

				cacheRenderer = renderer;

				element.setParameterValue(CVPrintElement.PARAMETER_SVG_CACHE_RENDERER, cacheRenderer);
			}
		}

		printImage.setRenderer(cacheRenderer);

		return printImage;
	}

	public abstract String createSvgImage(JasperReportsContext jasperReportsContext, JRGenericPrintElement element);

	/**
	 * Save the given input stream inside the specified file
	 * 
	 * @param file
	 * @param is
	 * @return
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public static File createFile(File file, InputStream is) throws FileNotFoundException, IOException
	{
		FileOutputStream os = new FileOutputStream(file);

		copyStream(is, os);

		os.close();
		is.close();

		return file;
	}

	/**
	 * Save the give input stream inside the specified file
	 * 
	 * @param file
	 * @param is
	 * @return
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public static void copyStream(InputStream is, OutputStream os) throws FileNotFoundException, IOException
	{
		final byte[] tmpBuffer = new byte[4096];

		int len;

		for (;;)
		{
			len = is.read(tmpBuffer);
			if (len == -1)
				break;
			os.write(tmpBuffer, 0, len);
		}
	}
}
