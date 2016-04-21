/*******************************************************************************
 * Copyright (C) 2005 - 2016 TIBCO Software Inc. All rights reserved.
 * http://www.jaspersoft.com.
 * 
 * Unless you have purchased  a commercial license agreement from Jaspersoft,
 * the following license terms  apply:
 * 
 * The Custom Visualization Component program and the accompanying materials
 * has been dual licensed under the the following licenses:
 * 
 * Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * The Custom Visualization Component is distributed in the hope that it will be
 * useful, but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License.
 * If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/
package com.jaspersoft.jasperreports.customvisualization.fill;

import java.io.Serializable;

import net.sf.jasperreports.components.items.ItemProperty;
import net.sf.jasperreports.engine.JRConstants;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExpression;
import net.sf.jasperreports.engine.fill.JRFillExpressionEvaluator;
import net.sf.jasperreports.engine.fill.JRFillObjectFactory;

/**
 *
 * @author Giulio Toffoli (gtoffoli@tibco.com)
 */
public class CVFillItemProperty implements Serializable
{

	private static final long serialVersionUID = JRConstants.SERIAL_VERSION_UID;
	protected String name;
	protected JRExpression valueExpression;
	protected String value;

	public CVFillItemProperty(ItemProperty itemProperty, JRFillObjectFactory factory)
	{
		this.valueExpression = factory.getExpression(itemProperty.getValueExpression());
		this.value = itemProperty.getValue();
		this.name = itemProperty.getName();
	}

	public String getName()
	{
		return name;
	}

	public String getValue()
	{
		return value;
	}

	/**
	 *
	 */
	public void evaluate(JRFillExpressionEvaluator evaluator, byte evaluation) throws JRException
	{
		this.value = getEvaluatedValue(evaluator, evaluation);
	}

	@Override
	public Object clone()
	{
		throw new UnsupportedOperationException();
	}

	public String getEvaluatedValue(JRFillExpressionEvaluator evaluator, byte evaluation) throws JRException
	{

		if (this.valueExpression == null || "".equals(valueExpression.getText()))
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
	 * Here we may add a validation handler to validate individual
	 * properties.... By default we
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
