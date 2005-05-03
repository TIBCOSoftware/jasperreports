/*
 * ============================================================================
 *                   GNU Lesser General Public License
 * ============================================================================
 *
 * JasperReports - Free Java report-generating library.
 * Copyright (C) 2001-2005 JasperSoft Corporation http://www.jaspersoft.com
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
 * 185, Berry Street, Suite 6200
 * San Francisco CA 94107
 * http://www.jaspersoft.com
 */
package net.sf.jasperreports.engine.base;

import java.util.HashMap;
import java.util.Map;

import net.sf.jasperreports.engine.JRBand;
import net.sf.jasperreports.engine.JRElement;
import net.sf.jasperreports.engine.JRElementGroup;
import net.sf.jasperreports.engine.JREllipse;
import net.sf.jasperreports.engine.JRExpression;
import net.sf.jasperreports.engine.JRExpressionChunk;
import net.sf.jasperreports.engine.JRField;
import net.sf.jasperreports.engine.JRFont;
import net.sf.jasperreports.engine.JRGroup;
import net.sf.jasperreports.engine.JRImage;
import net.sf.jasperreports.engine.JRLine;
import net.sf.jasperreports.engine.JRParameter;
import net.sf.jasperreports.engine.JRQuery;
import net.sf.jasperreports.engine.JRQueryChunk;
import net.sf.jasperreports.engine.JRRectangle;
import net.sf.jasperreports.engine.JRReportFont;
import net.sf.jasperreports.engine.JRStaticText;
import net.sf.jasperreports.engine.JRSubreport;
import net.sf.jasperreports.engine.JRSubreportParameter;
import net.sf.jasperreports.engine.JRTextField;
import net.sf.jasperreports.engine.JRVariable;


/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id$
 */
public class JRBaseObjectFactory
{

	
	/**
	 *
	 */
	private JRBaseReport report = null;
	private Map baseObjectsMap = new HashMap();


	/**
	 *
	 */
	protected JRBaseObjectFactory(JRBaseReport baseReport)
	{
		this.report = baseReport;
	}


	/**
	 *
	 */
	protected void put(Object object, Object baseObject)
	{
		baseObjectsMap.put(object, baseObject);
	}


	/**
	 *
	 */
	protected JRBaseReportFont getReportFont(JRReportFont font)
	{
		JRBaseReportFont baseFont = null;
		
		if (font != null)
		{
			baseFont = (JRBaseReportFont)baseObjectsMap.get(font);
			if (baseFont == null)
			{
				baseFont = new JRBaseReportFont(font);
				baseObjectsMap.put(font, baseFont);
			}
		}
		
		return baseFont;
	}


	/**
	 *
	 */
	protected JRBaseFont getFont(JRFont font)
	{
		JRBaseFont baseFont = null;
		
		if (font != null)
		{
			baseFont = (JRBaseFont)baseObjectsMap.get(font);
			if (baseFont == null)
			{
				baseFont = 
					new JRBaseFont(
						report, 
						getReportFont(font.getReportFont()), 
						font
						);
				baseObjectsMap.put(font, baseFont);
			}
		}
		
		return baseFont;
	}


	/**
	 *
	 */
	protected JRBaseParameter getParameter(JRParameter parameter)
	{
		JRBaseParameter baseParameter = null;
		
		if (parameter != null)
		{
			baseParameter = (JRBaseParameter)baseObjectsMap.get(parameter);
			if (baseParameter == null)
			{
				baseParameter = new JRBaseParameter(parameter, this);
			}
		}
		
		return baseParameter;
	}


	/**
	 *
	 */
	protected JRBaseQuery getQuery(JRQuery query)
	{
		JRBaseQuery baseQuery = null;
		
		if (query != null)
		{
			baseQuery = (JRBaseQuery)baseObjectsMap.get(query);
			if (baseQuery == null)
			{
				baseQuery = new JRBaseQuery(query, this);
			}
		}
		
		return baseQuery;
	}


	/**
	 *
	 */
	protected JRBaseQueryChunk getQueryChunk(JRQueryChunk queryChunk)
	{
		JRBaseQueryChunk baseQueryChunk = null;
		
		if (queryChunk != null)
		{
			baseQueryChunk = (JRBaseQueryChunk)baseObjectsMap.get(queryChunk);
			if (baseQueryChunk == null)
			{
				baseQueryChunk = new JRBaseQueryChunk(queryChunk, this);
			}
		}
		
		return baseQueryChunk;
	}


	/**
	 *
	 */
	protected JRBaseField getField(JRField field)
	{
		JRBaseField baseField = null;
		
		if (field != null)
		{
			baseField = (JRBaseField)baseObjectsMap.get(field);
			if (baseField == null)
			{
				baseField = new JRBaseField(field, this);
			}
		}
		
		return baseField;
	}


	/**
	 *
	 */
	protected JRBaseVariable getVariable(JRVariable variable)
	{
		JRBaseVariable baseVariable = null;
		
		if (variable != null)
		{
			baseVariable = (JRBaseVariable)baseObjectsMap.get(variable);
			if (baseVariable == null)
			{
				baseVariable = new JRBaseVariable(variable, this);
			}
		}
		
		return baseVariable;
	}


	/**
	 *
	 */
	protected JRBaseExpression getExpression(JRExpression expression)
	{
		JRBaseExpression baseExpression = null;
		
		if (expression != null)
		{
			baseExpression = (JRBaseExpression)baseObjectsMap.get(expression);
			if (baseExpression == null)
			{
				baseExpression = new JRBaseExpression(expression, this);
			}
		}
		
		return baseExpression;
	}


	/**
	 *
	 */
	protected JRBaseExpressionChunk getExpressionChunk(JRExpressionChunk expressionChunk)
	{
		JRBaseExpressionChunk baseExpressionChunk = null;
		
		if (expressionChunk != null)
		{
			baseExpressionChunk = (JRBaseExpressionChunk)baseObjectsMap.get(expressionChunk);
			if (baseExpressionChunk == null)
			{
				baseExpressionChunk = new JRBaseExpressionChunk(expressionChunk, this);
			}
		}
		
		return baseExpressionChunk;
	}


	/**
	 *
	 */
	protected JRBaseGroup getGroup(JRGroup group)
	{
		JRBaseGroup baseGroup = null;
		
		if (group != null)
		{
			baseGroup = (JRBaseGroup)baseObjectsMap.get(group);
			if (baseGroup == null)
			{
				baseGroup = new JRBaseGroup(group, this);
			}
		}
		
		return baseGroup;
	}


	/**
	 *
	 */
	protected JRBaseBand getBand(JRBand band)
	{
		JRBaseBand baseBand = null;
		
		if (band != null)
		{
			baseBand = (JRBaseBand)baseObjectsMap.get(band);
			if (baseBand == null)
			{
				baseBand = new JRBaseBand(band, this);
			}
		}
		
		return baseBand;
	}


	/**
	 *
	 */
	protected JRBaseElementGroup getElementGroup(JRElementGroup elementGroup)
	{
		JRBaseElementGroup baseElementGroup = null;
		
		if (elementGroup != null)
		{
			baseElementGroup = (JRBaseElementGroup)baseObjectsMap.get(elementGroup);
			if (baseElementGroup == null)
			{
				baseElementGroup = new JRBaseElementGroup(elementGroup, this);
			}
		}
		
		return baseElementGroup;
	}


	/**
	 *
	 */
	protected JRBaseElement getElement(JRElement element)
	{
		JRBaseElement baseElement = null;
		
		if (element instanceof JRLine)
		{
			baseElement = getLine((JRLine)element);
		}
		else if (element instanceof JRRectangle)
		{
			baseElement = getRectangle((JRRectangle)element);
		}
		else if (element instanceof JREllipse)
		{
			baseElement = getEllipse((JREllipse)element);
		}
		else if (element instanceof JRImage)
		{
			baseElement = getImage((JRImage)element);
		}
		else if (element instanceof JRStaticText)
		{
			baseElement = getStaticText((JRStaticText)element);
		}
		else if (element instanceof JRTextField)
		{
			baseElement = getTextField((JRTextField)element);
		}
		else if (element instanceof JRSubreport)
		{
			baseElement = getSubreport((JRSubreport)element);
		}
		
		return baseElement;
	}


	/**
	 *
	 */
	protected JRBaseLine getLine(JRLine line)
	{
		JRBaseLine baseLine = null;
		
		if (line != null)
		{
			baseLine = (JRBaseLine)baseObjectsMap.get(line);
			if (baseLine == null)
			{
				baseLine = new JRBaseLine(line, this);
			}
		}
		
		return baseLine;
	}


	/**
	 *
	 */
	protected JRBaseRectangle getRectangle(JRRectangle rectangle)
	{
		JRBaseRectangle baseRectangle = null;
		
		if (rectangle != null)
		{
			baseRectangle = (JRBaseRectangle)baseObjectsMap.get(rectangle);
			if (baseRectangle == null)
			{
				baseRectangle = new JRBaseRectangle(rectangle, this);
			}
		}
		
		return baseRectangle;
	}


	/**
	 *
	 */
	protected JRBaseEllipse getEllipse(JREllipse ellipse)
	{
		JRBaseEllipse baseEllipse = null;
		
		if (ellipse != null)
		{
			baseEllipse = (JRBaseEllipse)baseObjectsMap.get(ellipse);
			if (baseEllipse == null)
			{
				baseEllipse = new JRBaseEllipse(ellipse, this);
			}
		}
		
		return baseEllipse;
	}


	/**
	 *
	 */
	protected JRBaseImage getImage(JRImage image)
	{
		JRBaseImage baseImage = null;
		
		if (image != null)
		{
			baseImage = (JRBaseImage)baseObjectsMap.get(image);
			if (baseImage == null)
			{
				baseImage = new JRBaseImage(image, this);
			}
		}
		
		return baseImage;
	}


	/**
	 *
	 */
	protected JRBaseStaticText getStaticText(JRStaticText staticText)
	{
		JRBaseStaticText baseStaticText = null;
		
		if (staticText != null)
		{
			baseStaticText = (JRBaseStaticText)baseObjectsMap.get(staticText);
			if (baseStaticText == null)
			{
				baseStaticText = new JRBaseStaticText(staticText, this);
			}
		}
		
		return baseStaticText;
	}


	/**
	 *
	 */
	protected JRBaseTextField getTextField(JRTextField textField)
	{
		JRBaseTextField baseTextField = null;
		
		if (textField != null)
		{
			baseTextField = (JRBaseTextField)baseObjectsMap.get(textField);
			if (baseTextField == null)
			{
				baseTextField = new JRBaseTextField(textField, this);
			}
		}
		
		return baseTextField;
	}


	/**
	 *
	 */
	protected JRBaseSubreport getSubreport(JRSubreport subreport)
	{
		JRBaseSubreport baseSubreport = null;
		
		if (subreport != null)
		{
			baseSubreport = (JRBaseSubreport)baseObjectsMap.get(subreport);
			if (baseSubreport == null)
			{
				baseSubreport = new JRBaseSubreport(subreport, this);
			}
		}
		
		return baseSubreport;
	}


	/**
	 *
	 */
	protected JRBaseSubreportParameter getSubreportParameter(JRSubreportParameter subreportParameter)
	{
		JRBaseSubreportParameter baseSubreportParameter = null;
		
		if (subreportParameter != null)
		{
			baseSubreportParameter = (JRBaseSubreportParameter)baseObjectsMap.get(subreportParameter);
			if (baseSubreportParameter == null)
			{
				baseSubreportParameter = new JRBaseSubreportParameter(subreportParameter, this);
				baseObjectsMap.put(subreportParameter, baseSubreportParameter);
			}
		}
		
		return baseSubreportParameter;
	}
	

}
