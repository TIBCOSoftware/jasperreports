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

import net.sf.jasperreports.engine.JRExpression;
import net.sf.jasperreports.engine.design.JRDesignGenericElement;
import net.sf.jasperreports.engine.design.JasperDesign;

import org.xml.sax.Attributes;

/**
 * XML factory of {@link JRDesignGenericElement} instances.
 * 
 * @author Lucian Chirita (lucianc@users.sourceforge.net)
 * @version $Id$
 */
public class JRGenericElementFactory extends JRBaseFactory
{

	public Object createObject(Attributes attrs) throws Exception
	{
		JasperDesign jasperDesign = (JasperDesign)digester.peek(digester.getCount() - 2);
		JRDesignGenericElement element = new JRDesignGenericElement(jasperDesign);
		
		String evaluationTimeAttr = attrs.getValue(
				JRXmlConstants.ATTRIBUTE_evaluationTime);
		if (evaluationTimeAttr != null)
		{
			Byte evaluationTime = (Byte) JRXmlConstants.getEvaluationTimeMap().get(
					evaluationTimeAttr);
			element.setEvaluationTime(evaluationTime.byteValue());
		}
		
		if (element.getEvaluationTime() == JRExpression.EVALUATION_TIME_GROUP)
		{
			String groupName = attrs.getValue(JRXmlConstants.ATTRIBUTE_evaluationGroup);
			if (groupName != null)
			{
				element.setEvaluationGroupName(groupName);
			}
		}
		
		return element;
	}

}
