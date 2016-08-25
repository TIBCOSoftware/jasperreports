/*******************************************************************************
 * Copyright (C) 2005 - 2016 TIBCO Software Inc. All rights reserved.
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

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.jaspersoft.jasperreports.customvisualization.CVConstants;
import com.jaspersoft.jasperreports.customvisualization.CVPrintElement;
import com.jaspersoft.jasperreports.customvisualization.CVUtils;

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
import net.sf.jasperreports.renderers.SimpleDataRenderer;
import net.sf.jasperreports.renderers.SimpleRenderToImageAwareDataRenderer;
import net.sf.jasperreports.renderers.util.RendererUtil;

/**
 * @author Giulio Toffoli (gtoffoli@tibco.com)
 */
public abstract class CVElementImageProvider
{
	private static final Log log = LogFactory.getLog(CVElementImageProvider.class);

	public static final String TEMP_RESOURCE_PREFIX = "cv_component_";

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
	 * The method return a JRPrintImage with a null renderer until the final image
		 * is not complete, in which case we look in the element for a cached renderer
		 * save as parameter with key CVPrintElement.PARAMETER_CACHE_RENDERER inside the element.
		 * If the renderer is not available (because this is the first time we try to draw this
		 * element after the image has been produced), the new renderer will be created.
		 * 
		 * The ability to set a null renderer works starting from 6.2.2.
	 * 
	 * @param jasperReportsContext
	 * @param element
	 * @param createSvg
	 * @return
	 * @throws JRException
	 */
	public JRPrintImage getImage(
		JasperReportsContext jasperReportsContext, 
		JRGenericPrintElement element,
		boolean createSvg
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

		//printImage.setOnErrorType(OnErrorTypeEnum.BLANK);

		Renderable cacheRenderer = null;
		
		if (element.getParameterValue(CVPrintElement.CONFIGURATION) != null)
		{
			JRPropertiesUtil propUtil = JRPropertiesUtil.getInstance(jasperReportsContext);
		
			// If the exporter requested a PNG, we have two options:
			
			// 1. Ignore the fact that a PNG will be renderer on the exported document, and let JR to produce the PNG
			//	starting from the SVG (in that case min.dpi and antialias property are used to control the final rasterized image).
			//	This is the default behavior and can be changed by setting the property CVConstants.CV_PNG_USE_JR_TO_RENDER
			//	at global or element level.
			//
			// 2. Force the use of PhantomJS to render the PNG starting from the produced SVG.
			//
			
			if (propUtil. getBooleanProperty(element, CVConstants.CV_PNG_USE_JR_TO_RENDER, CVConstants.CV_PNG_USE_JR_TO_RENDER_DEFAULT_VALUE)) 
			{
				createSvg = true;
			}
                        
                        boolean renderAsPng = CVUtils.isRenderAsPng(element);
			
			String cacheKey = (createSvg && !renderAsPng) ? CVPrintElement.PARAMETER_SVG_CACHE_RENDERER : CVPrintElement.PARAMETER_PNG_CACHE_RENDERER;
			
			cacheRenderer = (Renderable) element.getParameterValue(cacheKey);
			if (cacheRenderer == null)
			{
				try
				{
					byte[] imageData = getImageData(jasperReportsContext, element, createSvg);

                                        if (renderAsPng)
                                        {
                                            cacheRenderer = new SimpleDataRenderer(imageData, null);
                                        }
                                        else
                                        {
                                            SimpleRenderToImageAwareDataRenderer renderer =
                                                    new SimpleRenderToImageAwareDataRenderer(imageData, null);


                                            renderer.setMinDPI(propUtil.getIntegerProperty(element, CVConstants.CV_PNG_MIN_DPI, CVConstants.CV_PNG_MIN_DPI_DEFAULT_VALUE));
                                            renderer.setAntiAlias(propUtil.getBooleanProperty(element, CVConstants.CV_PNG_ANTIALIAS, CVConstants.CV_PNG_ANTIALIAS_DEFAULT_VALUE));
                                            cacheRenderer = renderer;
                                        }
    					
				}
				catch (Exception e)
				{
					if (log.isErrorEnabled())
					{
						log.error("Generating image for Custom Visualization element " + element.hashCode() + " failed.", e);
					}

					cacheRenderer =
						RendererUtil.getInstance(jasperReportsContext).handleImageError(
							e, 
							element.getParameterValue(CVPrintElement.PARAMETER_ON_ERROR_TYPE) == null
								? CVPrintElement.DEFAULT_ON_ERROR_TYPE
								: OnErrorTypeEnum.getByName((String) element.getParameterValue(CVPrintElement.PARAMETER_ON_ERROR_TYPE))
							);
				}

				element.setParameterValue(cacheKey, cacheRenderer);
			}
		}

		printImage.setRenderer(cacheRenderer);

		return printImage;
	}

	public abstract byte[] getImageData(JasperReportsContext jasperReportsContext, JRGenericPrintElement element, boolean createSvg) throws Exception;

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
