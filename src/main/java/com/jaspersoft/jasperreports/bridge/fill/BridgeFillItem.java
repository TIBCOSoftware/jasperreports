/*
 * JasperReports - Free Java Reporting Library.
 * Copyright (C) 2001 - 2013 Jaspersoft Corporation. All rights reserved.
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
package com.jaspersoft.jasperreports.bridge.fill;

import com.jaspersoft.jasperreports.bridge.BridgeItem;
import com.jaspersoft.jasperreports.bridge.BridgeItemProperty;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.fill.JRFillExpressionEvaluator;
import net.sf.jasperreports.engine.fill.JRFillObjectFactory;


/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id: BridgeFillItem.java 6002 2013-03-20 08:15:32Z teodord $
 */
public class BridgeFillItem implements BridgeItem
{

	/**
	 *
	 */
	protected BridgeItem item;
	protected Map<String, Object> evaluatedProperties;
	
	/**
	 *
	 */
	public BridgeFillItem(
		BridgeItem item, 
		JRFillObjectFactory factory
		)
	{
		factory.put(item, this);

		this.item = item;
	}
	
	
	/**
	 *
	 */
	public void evaluateProperties(JRFillExpressionEvaluator evaluator, byte evaluation) throws JRException
	{
                List<BridgeItemProperty> itemProperties = getItemProperties();
		Map<String, Object> result = null;
		if(itemProperties != null && !itemProperties.isEmpty())
		{
                 	result = new HashMap<String, Object>();
			for(BridgeItemProperty property : itemProperties)
			{
                                result.put(property.getName(), getEvaluatedValue(property, evaluator, evaluation));
			}
		}
                
		evaluatedProperties = result;
	}


	/**
	 *
	 */
	public Object clone() 
	{
		throw new UnsupportedOperationException();
	}

	@Override
	public List<BridgeItemProperty> getItemProperties() 
	{
		return item.getItemProperties();
	}
	
	public Map<String, Object> getEvaluatedProperties() 
	{
		return evaluatedProperties;
	}

	public Object getEvaluatedValue(BridgeItemProperty property, JRFillExpressionEvaluator evaluator, byte evaluation) throws JRException
	{
		Object result = null;
		if(property.getValueExpression() == null || "".equals(property.getValueExpression()))
		{
			result = property.getValue();
		}
		else
		{
			result = evaluator.evaluate(property.getValueExpression(), evaluation);
		}

		verifyValue(property, result);
		
		return result;
	}
        

	public void verifyValue(BridgeItemProperty property, Object value) throws JRException
        {
            
        }
}
