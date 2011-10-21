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
package net.sf.jasperreports.engine.base;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.jasperreports.engine.JRConstants;
import net.sf.jasperreports.engine.JRPrintElement;
import net.sf.jasperreports.engine.fill.JREvaluationTime;
import net.sf.jasperreports.engine.util.Pair;

/**
 * Virtual data type used by report pages to virtualize elements.
 * 
 * @author Lucian Chirita (lucianc@users.sourceforge.net)
 * @version $Id$
 */
public class VirtualElementsData implements Serializable
{
	private static final long serialVersionUID = JRConstants.SERIAL_VERSION_UID;
	
	private Map<Pair<Integer, JREvaluationTime>, Map<JRPrintElement, Integer>> elementEvaluations;
	private List<JRPrintElement> elements;
	
	public VirtualElementsData(List<JRPrintElement> elements)
	{
		this.elements = elements;
	}
	
	public void setElementEvaluations(int fillerId, JREvaluationTime evaluationTime, Map<JRPrintElement, Integer> evaluations)
	{
		if (elementEvaluations == null)
		{
			elementEvaluations = new HashMap<Pair<Integer, JREvaluationTime>, Map<JRPrintElement,Integer>>();
		}
		
		elementEvaluations.put(new Pair<Integer, JREvaluationTime>(fillerId, evaluationTime), evaluations);
	}

	public Map<JRPrintElement, Integer> getElementEvaluations(int fillerId, JREvaluationTime evaluationTime)
	{
		return elementEvaluations == null ? null : elementEvaluations.get(
				new Pair<Integer, JREvaluationTime>(fillerId, evaluationTime));
	}
	
	public List<JRPrintElement> getElements()
	{
		return elements;
	}
}
