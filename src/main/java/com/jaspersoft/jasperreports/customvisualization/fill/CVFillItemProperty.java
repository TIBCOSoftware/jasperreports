/*
 * iReport - Visual Designer for JasperReports.
 * Copyright (C) 2002 - 2009 Jaspersoft Corporation. All rights reserved.
 * http://www.jaspersoft.com
 *
 * Unless you have purchased a commercial license agreement from Jaspersoft,
 * the following license terms apply:
 *
 * This program is part of iReport.
 *
 * iReport is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * iReport is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with iReport. If not, see <http://www.gnu.org/licenses/>.
 */
package com.jaspersoft.jasperreports.customvisualization.fill;

import java.io.Serializable;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExpression;
import net.sf.jasperreports.engine.fill.JRFillExpressionEvaluator;
import net.sf.jasperreports.engine.fill.JRFillObjectFactory;

import com.jaspersoft.jasperreports.customvisualization.CVItemProperty;

/**
 *
 * @author Giulio Toffoli (gtoffoli@tibco.com)
 */
public class CVFillItemProperty  implements Serializable {

    
    protected String name;
    protected JRExpression valueExpression;
    protected String value;
    
    public CVFillItemProperty(CVItemProperty itemProperty, JRFillObjectFactory factory)
    {
            this.valueExpression = factory.getExpression(itemProperty.getValueExpression());
            this.value = itemProperty.getValue();
            this.name = itemProperty.getName();
    }
    
    public String getName() {
        return name;
    }

    public String getValue() {
        return value;
    }

    
    
    /**
     *
     */
    public void evaluate(JRFillExpressionEvaluator evaluator, byte evaluation) throws JRException
    {
           this.value = getEvaluatedValue(evaluator, evaluation);
    }

    
    /**
     *
     */
    public Object clone() 
    {
           throw new UnsupportedOperationException();
    }

    public String getEvaluatedValue(JRFillExpressionEvaluator evaluator, byte evaluation) throws JRException
    {
           
           if(this.valueExpression == null || "".equals(valueExpression.getText()))
           {
                   return getValue();
           }
           else
           {
                   Object evaluatedValue = evaluator.evaluate(this.valueExpression, evaluation);
                   verifyValue(evaluatedValue);
                   return evaluatedValue == null ? null : evaluatedValue.toString();
           }
    }

    
    /**
     * Here we may add a validation handler to validate individual properties....
     * By default we 
     * 
     * @param property
     * @param value
     * @throws JRException 
     */
    public void verifyValue(Object value) throws JRException
    {
        // Empty implementation. We don't do any verification.
    }

}
