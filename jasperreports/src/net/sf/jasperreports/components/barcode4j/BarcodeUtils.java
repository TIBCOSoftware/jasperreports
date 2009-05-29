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
package net.sf.jasperreports.components.barcode4j;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRPropertiesHolder;
import net.sf.jasperreports.engine.JRRuntimeException;
import net.sf.jasperreports.engine.util.JRProperties;
import net.sf.jasperreports.engine.util.JRSingletonCache;

/**
 * 
 * @author Lucian Chirita (lucianc@users.sourceforge.net)
 * @version $Id$
 */
public class BarcodeUtils
{

	protected static JRSingletonCache imageProducerCache = 
		new JRSingletonCache(BarcodeImageProducer.class);
	
	public static BarcodeImageProducer getImageProducer(
			JRPropertiesHolder propertiesHolder)
	{
		String producerProperty = JRProperties.getProperty(propertiesHolder, 
				BarcodeImageProducer.PROPERTY_IMAGE_PRODUCER);
		
		String producerClass = JRProperties.getProperty(propertiesHolder, 
				BarcodeImageProducer.PROPERTY_PREFIX_IMAGE_PRODUCER + producerProperty);
		if (producerClass == null)
		{
			producerClass = producerProperty;
		}
		
		try
		{
			return (BarcodeImageProducer) imageProducerCache.getCachedInstance(producerClass);
		}
		catch (JRException e)
		{
			throw new JRRuntimeException(e);
		}
	}

	public static boolean isVertical(BarcodeComponent barcode)
	{
		int orientation = barcode.getOrientation();
		return orientation == 90 || orientation == 270;
	}
	
}
