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
package net.sf.jasperreports.engine.xml;

import java.util.Collection;

import net.sf.jasperreports.engine.JRExpression;
import net.sf.jasperreports.engine.design.JRDesignGroup;
import net.sf.jasperreports.engine.design.JRDesignImage;
import net.sf.jasperreports.engine.design.JasperDesign;

import org.xml.sax.Attributes;


/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id$
 */
public class JRImageFactory extends JRBaseFactory
{

	/**
	 *
	 */
	public Object createObject(Attributes atts)
	{
		JRXmlLoader xmlLoader = (JRXmlLoader)digester.peek(digester.getCount() - 1);
		Collection groupEvaluatedImages = xmlLoader.getGroupEvaluatedImages();
		JasperDesign jasperDesign = (JasperDesign)digester.peek(digester.getCount() - 2);

		JRDesignImage image = new JRDesignImage(jasperDesign);

		Byte scaleImage = (Byte)JRXmlConstants.getScaleImageMap().get(atts.getValue(JRXmlConstants.ATTRIBUTE_scaleImage));
		if (scaleImage != null)
		{
			image.setScaleImage(scaleImage);
		}

		Byte horizontalAlignment = (Byte)JRXmlConstants.getHorizontalAlignMap().get(atts.getValue(JRXmlConstants.ATTRIBUTE_hAlign));
		if (horizontalAlignment != null)
		{
			image.setHorizontalAlignment(horizontalAlignment);
		}

		Byte verticalAlignment = (Byte)JRXmlConstants.getVerticalAlignMap().get(atts.getValue(JRXmlConstants.ATTRIBUTE_vAlign));
		if (verticalAlignment != null)
		{
			image.setVerticalAlignment(verticalAlignment);
		}

		String isUsingCache = atts.getValue(JRXmlConstants.ATTRIBUTE_isUsingCache);
		if (isUsingCache != null && isUsingCache.length() > 0)
		{
			image.setUsingCache(Boolean.valueOf(isUsingCache));
		}

		String isLazy = atts.getValue(JRXmlConstants.ATTRIBUTE_isLazy);
		if (isLazy != null && isLazy.length() > 0)
		{
			image.setLazy(Boolean.valueOf(isLazy).booleanValue());
		}

		Byte onErrorType = (Byte)JRXmlConstants.getOnErrorTypeMap().get(atts.getValue(JRXmlConstants.ATTRIBUTE_onErrorType));
		if (onErrorType != null)
		{
			image.setOnErrorType(onErrorType.byteValue());
		}

		Byte evaluationTime = (Byte)JRXmlConstants.getEvaluationTimeMap().get(atts.getValue(JRXmlConstants.ATTRIBUTE_evaluationTime));
		if (evaluationTime != null)
		{
			image.setEvaluationTime(evaluationTime.byteValue());
		}
		if (image.getEvaluationTime() == JRExpression.EVALUATION_TIME_GROUP)
		{
			groupEvaluatedImages.add(image);

			String groupName = atts.getValue(JRXmlConstants.ATTRIBUTE_evaluationGroup);
			if (groupName != null)
			{
				JRDesignGroup group = new JRDesignGroup();
				group.setName(groupName);
				image.setEvaluationGroup(group);
			}
		}

		String hyperlinkType = atts.getValue(JRXmlConstants.ATTRIBUTE_hyperlinkType);
		if (hyperlinkType != null)
		{
			image.setLinkType(hyperlinkType);
		}

//		Byte hyperlinkTarget = (Byte)JRXmlConstants.getHyperlinkTargetMap().get(atts.getValue(JRXmlConstants.ATTRIBUTE_hyperlinkTarget));
		String hyperlinkTarget = atts.getValue(JRXmlConstants.ATTRIBUTE_hyperlinkTarget);
		if (hyperlinkTarget != null)
		{
			image.setLinkTarget(hyperlinkTarget);
		}

		
		String bookmarkLevelAttr = atts.getValue(JRXmlConstants.ATTRIBUTE_bookmarkLevel);
		if (bookmarkLevelAttr != null)
		{
			image.setBookmarkLevel(Integer.parseInt(bookmarkLevelAttr));
		}		

		return image;
	}
	

}
