/*
 * JasperReports - Free Java Reporting Library.
 * Copyright (C) 2001 - 2011 Jaspersoft Corporation. All rights reserved.
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
public final class BarcodeUtils
{

	protected static JRSingletonCache<BarcodeImageProducer> imageProducerCache = 
		new JRSingletonCache<BarcodeImageProducer>(BarcodeImageProducer.class);
	
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
			return imageProducerCache.getCachedInstance(producerClass);
		}
		catch (JRException e)
		{
			throw new JRRuntimeException(e);
		}
	}

	public static boolean isVertical(BarcodeComponent barcode)
	{
		int orientation = barcode.getOrientation();
		return orientation == BarcodeComponent.ORIENTATION_LEFT
				|| orientation == BarcodeComponent.ORIENTATION_RIGHT;
	}
	
	private BarcodeUtils()
	{
	}
}
