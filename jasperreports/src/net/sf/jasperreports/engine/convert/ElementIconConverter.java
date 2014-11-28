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
import net.sf.jasperreports.engine.JRPrintElement;
import net.sf.jasperreports.engine.JasperReportsContext;
import net.sf.jasperreports.engine.Renderable;
import net.sf.jasperreports.engine.RenderableUtil;
import net.sf.jasperreports.engine.base.JRBasePrintImage;
import net.sf.jasperreports.engine.type.OnErrorTypeEnum;
import net.sf.jasperreports.engine.type.ScaleImageEnum;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


/**
 * Base converter that generates a static preview icon for the element.
 * 
 * @author Lucian Chirita (lucianc@users.sourceforge.net)
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
		printImage.setScaleImage(ScaleImageEnum.CLIP);
		
		printImage.setRenderable(getRenderer(reportConverter.getJasperReportsContext()));
		return printImage;
	}

	protected Renderable getRenderer(JasperReportsContext jasperReportsContext)
	{
		try
		{
			return RenderableUtil.getInstance(jasperReportsContext).getRenderable(
					iconLocation, 
					OnErrorTypeEnum.ERROR);
		}
		catch (JRException e)
		{
			log.warn("Error creating component design preview icon", e);
			return null;
		}
	}

}
