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
package com.jaspersoft.jasperreports.customvisualization.xml;

import org.xml.sax.Attributes;

import net.sf.jasperreports.components.items.StandardItemProperty;
import net.sf.jasperreports.engine.xml.JRBaseFactory;
import net.sf.jasperreports.engine.xml.JRXmlConstants;

/**
 * @author Giulio Toffoli (gtoffoli@tibco.com)
 */
public class CVItemPropertyXmlFactory extends JRBaseFactory
{

	@Override
	public Object createObject(Attributes atts)
	{
		StandardItemProperty itemProperty = new StandardItemProperty();

		String name = atts.getValue(JRXmlConstants.ATTRIBUTE_name);
		if (name != null)
		{
			itemProperty.setName(name);
		}
		String value = atts.getValue(JRXmlConstants.ATTRIBUTE_value);
		if (value != null)
		{
			itemProperty.setValue(value);
		}
		return itemProperty;
	}
}
