/*
 * ============================================================================
 * GNU Lesser General Public License
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
 * 303 Second Street, Suite 450 North
 * San Francisco, CA 94107
 * http://www.jaspersoft.com
 */
package net.sf.jasperreports.engine.xml;

import net.sf.jasperreports.engine.JRDatasetParameter;
import net.sf.jasperreports.engine.JRDatasetRun;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRParameter;
import net.sf.jasperreports.engine.design.JRDesignDataset;
import net.sf.jasperreports.engine.design.JRDesignExpression;
import net.sf.jasperreports.engine.design.JasperDesign;

import org.xml.sax.Attributes;

/**
 * @author Lucian Chirita (lucianc@users.sourceforge.net)
 * @version $Id$
 */
public class JRDatasetRunParameterExpressionFactory extends JRBaseFactory
{
	public static final String TAG_DATASET_PARAMETER_EXPRESSION = "datasetParameterExpression";
	
	public Object createObject(Attributes attributes)
	{
		String valueClassName = Object.class.getName();
		
		JRXmlLoader xmlLoader = (JRXmlLoader) digester.peek(digester.getCount() - 1);
		JasperDesign design = (JasperDesign) digester.peek(digester.getCount() - 2);
		JRDatasetRun datasetRun = (JRDatasetRun) digester.peek(1);
		
		JRDesignDataset dataset = (JRDesignDataset) design.getDatasetMap().get(datasetRun.getDatasetName());
		if (dataset == null)
		{
			xmlLoader.addError(new JRException("Unknown sub dataset " + datasetRun.getDatasetName()));
		}
		else
		{
			JRDatasetParameter runParameter = (JRDatasetParameter) digester.peek();
			JRParameter param = (JRParameter) dataset.getParametersMap().get(runParameter.getName());
			
			if (param == null)
			{
				xmlLoader.addError(new JRException("Unknown parameter " + runParameter.getName() + " in sub dataset " + datasetRun.getDatasetName()));
			}
			else
			{
				valueClassName = param.getValueClassName();
			}
		}

		JRDesignExpression expression = new JRDesignExpression();
		expression.setValueClassName(valueClassName);
		
		return expression;
	}
}
