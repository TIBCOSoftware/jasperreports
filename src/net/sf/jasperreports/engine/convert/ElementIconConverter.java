/*
 * ============================================================================
 * GNU Lesser General Public License
 * ============================================================================
 *
 * JasperReports - Free Java report-generating library.
 * Copyright (C) 2001-2006 JasperSoft Corporation http://www.jaspersoft.com
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
 * 303 Second Street, Suite 450 North
 * San Francisco, CA 94107
 * http://www.jaspersoft.com
 */

/*
 * Contributors:
 * Eugene D - eugenedruy@users.sourceforge.net 
 * Adrian Jackson - iapetus@users.sourceforge.net
 * David Taylor - exodussystems@users.sourceforge.net
 * Lars Kristensen - llk@users.sourceforge.net
 */
package net.sf.jasperreports.engine.convert;

import net.sf.jasperreports.engine.JRElement;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRImage;
import net.sf.jasperreports.engine.JRImageRenderer;
import net.sf.jasperreports.engine.JRPrintElement;
import net.sf.jasperreports.engine.JRRenderable;
import net.sf.jasperreports.engine.base.JRBasePrintImage;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


/**
 * Base converter that generates a static preview icon for the element.
 * 
 * @author Lucian Chirita (lucianc@users.sourceforge.net)
 * @version $Id$
 */
public class ElementIconConverter extends ElementConverter
{

	private static final Log log = LogFactory.getLog(ElementIconConverter.class);
	
	private final String iconLocation;
	
	public ElementIconConverter(String iconLocation)
	{
		this.iconLocation = iconLocation;
	}
	
	public JRPrintElement convert(ReportConverter reportConverter, JRElement element)
	{
		JRBasePrintImage printImage = new JRBasePrintImage(
				reportConverter.getDefaultStyleProvider());
		copyElement(reportConverter, element, printImage);
		
		printImage.getLineBox().setPadding(3);
		printImage.setScaleImage(JRImage.SCALE_IMAGE_CLIP);
		
		printImage.setRenderer(getRenderer());
		return printImage;
	}

	protected JRRenderable getRenderer()
	{
		try
		{
			return JRImageRenderer.getInstance(
					iconLocation, 
					JRImage.ON_ERROR_TYPE_ERROR);
		}
		catch (JRException e)
		{
			log.warn("Error creating component design preview icon", e);
			return null;
		}
	}

}
