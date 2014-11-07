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
package net.sf.jasperreports.engine.xml;

import net.sf.jasperreports.engine.design.JRDesignPart;
import net.sf.jasperreports.engine.part.PartEvaluationTime;
import net.sf.jasperreports.engine.part.StandardPartEvaluationTime;
import net.sf.jasperreports.engine.type.PartEvaluationTimeType;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.xml.sax.Attributes;


/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id: JRBandFactory.java 5878 2013-01-07 20:23:13Z teodord $
 */
public class JRPartFactory extends JRBaseFactory
{
	private static final Log log = LogFactory.getLog(JRPartFactory.class);
	
	/**
	 *
	 */
	public Object createObject(Attributes atts)
	{
		JRDesignPart part = new JRDesignPart();
		
		PartEvaluationTime evaluationTime = readEvaluationTime(atts);
		part.setEvaluationTime(evaluationTime);
		
		return part;
	}

	protected PartEvaluationTime readEvaluationTime(Attributes atts)
	{
		PartEvaluationTime evaluationTime = null;
		String evaluationTimeAttr = atts.getValue(JRXmlConstants.ATTRIBUTE_evaluationTime);
		if (evaluationTimeAttr != null)
		{
			if (evaluationTimeAttr.equals(PartEvaluationTimeType.GROUP.getName()))
			{
				String evaluationGroupAttr = atts.getValue(JRXmlConstants.ATTRIBUTE_evaluationGroup);
				evaluationTime = StandardPartEvaluationTime.forGroup(evaluationGroupAttr);
			}
			else
			{
				evaluationTime = StandardPartEvaluationTime.forType(evaluationTimeAttr);
			}
		}
		return evaluationTime;
	}
	

}
