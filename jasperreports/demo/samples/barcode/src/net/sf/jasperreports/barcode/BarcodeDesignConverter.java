/*
 * ============================================================================
 * GNU Lesser General Public License
 * ============================================================================
 *
 * JasperReports - Free Java report-generating library.
 * Copyright (C) 2001-2009 JasperSoft Corporation http://www.jaspersoft.com
 * 
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307, USA.
 * 
 * JasperSoft Corporation
 * 539 Bryant Street, Suite 100
 * San Francisco, CA 94107
 * http://www.jaspersoft.com
 */
package net.sf.jasperreports.barcode;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import net.sf.jasperreports.engine.JRComponentElement;
import net.sf.jasperreports.engine.JRImage;
import net.sf.jasperreports.engine.JRPrintElement;
import net.sf.jasperreports.engine.base.JRBasePrintImage;
import net.sf.jasperreports.engine.component.ComponentDesignConverter;
import net.sf.jasperreports.engine.convert.ReportConverter;
import net.sf.jasperreports.engine.util.JRExpressionUtil;
import net.sourceforge.barbecue.Barcode;

/**
 * 
 * @author Lucian Chirita (lucianc@users.sourceforge.net)
 * @version $Id$
 */
public class BarcodeDesignConverter implements ComponentDesignConverter
{
	
	private static final Log log = LogFactory.getLog(BarcodeDesignConverter.class);
	
	private static final String DEFAULT_PREVIEW_CODE = "0123456789";
	
	private BarcodeProviders providers;

	public JRPrintElement convert(ReportConverter reportConverter,
			JRComponentElement element)
	{
		BarcodeComponent component = (BarcodeComponent) element.getComponent();
		if (component == null || component.getType() == null)
		{
			return null;
		}
		
		try
		{
			JRBasePrintImage image = new JRBasePrintImage(reportConverter.getDefaultStyleProvider());
			reportConverter.copyBaseAttributes(element, image);
			image.setScaleImage(JRImage.SCALE_IMAGE_RETAIN_SHAPE);
			
			String code = null;
			if (component.getCodeExpression() != null)
			{
				code = JRExpressionUtil.getSimpleExpressionText(component.getCodeExpression());
			}
			
			if (code == null)
			{
				code = DEFAULT_PREVIEW_CODE;
			}
			
			Barcode barcode = providers.createBarcode(component.getType(), code);
			barcode.setDrawingText(component.isDrawText());
			
			image.setRenderer(new BarbecueRenderer(barcode));
			return image;
		}
		catch (Exception e)
		{
			if (log.isDebugEnabled())
			{
				log.debug("Error while previewing barcode", e);
			}
			
			return null;
		}
		
	}

	public BarcodeProviders getProviders()
	{
		return providers;
	}

	public void setProviders(BarcodeProviders providers)
	{
		this.providers = providers;
	}

}
