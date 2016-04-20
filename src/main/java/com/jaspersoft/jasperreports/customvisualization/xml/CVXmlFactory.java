/*******************************************************************************
 * Copyright (C) 2005 - 2014 TIBCO Software Inc. All rights reserved.
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
package com.jaspersoft.jasperreports.customvisualization.xml;

import org.xml.sax.Attributes;

import com.jaspersoft.jasperreports.customvisualization.design.CVDesignComponent;

import net.sf.jasperreports.engine.type.EvaluationTimeEnum;
import net.sf.jasperreports.engine.type.OnErrorTypeEnum;
import net.sf.jasperreports.engine.xml.JRBaseFactory;
import net.sf.jasperreports.engine.xml.JRXmlConstants;

public class CVXmlFactory extends JRBaseFactory
{
	public static final String ELEMENT_itemProperty = "itemProperty";
	public static final String ATTRIBUTE_processingClass = "processingClass";
	public static final String ELEMENT_cvData = "cvData";
	public static final String ELEMENT_item = "item";
	public static final String ATTRIBUTE_onErrorType = "onErrorType";

	@Override
	public Object createObject(Attributes atts)
	{

		CVDesignComponent component = new CVDesignComponent();

		EvaluationTimeEnum evaluationTime = 
			EvaluationTimeEnum.getByName(atts.getValue(JRXmlConstants.ATTRIBUTE_evaluationTime));
		if (evaluationTime != null)
		{
			component.setEvaluationTime(evaluationTime);
		}

		if (component.getEvaluationTime() == EvaluationTimeEnum.GROUP)
		{
			String groupName = atts.getValue(JRXmlConstants.ATTRIBUTE_evaluationGroup);
			component.setEvaluationGroup(groupName);
		}

		String processingClass = atts.getValue(ATTRIBUTE_processingClass);

		if (processingClass != null)
		{
			component.setProcessingClass(processingClass);
		}

		OnErrorTypeEnum onErrorType = OnErrorTypeEnum.getByName(atts.getValue(ATTRIBUTE_onErrorType));
		if (onErrorType != null)
		{
			component.setOnErrorType(onErrorType);
		}

		return component;
	}

}
