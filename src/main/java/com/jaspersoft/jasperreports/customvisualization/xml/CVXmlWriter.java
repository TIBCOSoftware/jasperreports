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

import java.io.IOException;
import java.util.List;

import net.sf.jasperreports.components.AbstractComponentXmlWriter;
import net.sf.jasperreports.engine.JRComponentElement;
import net.sf.jasperreports.engine.JRElementDataset;
import net.sf.jasperreports.engine.JasperReportsContext;
import net.sf.jasperreports.engine.component.Component;
import net.sf.jasperreports.engine.component.ComponentKey;
import net.sf.jasperreports.engine.type.EvaluationTimeEnum;
import net.sf.jasperreports.engine.type.OnErrorTypeEnum;
import net.sf.jasperreports.engine.util.JRXmlWriteHelper;
import net.sf.jasperreports.engine.util.XmlNamespace;
import net.sf.jasperreports.engine.xml.JRXmlConstants;
import net.sf.jasperreports.engine.xml.JRXmlWriter;

import com.jaspersoft.jasperreports.customvisualization.CVComponent;
import com.jaspersoft.jasperreports.customvisualization.CVConstants;
import com.jaspersoft.jasperreports.customvisualization.CVItem;
import com.jaspersoft.jasperreports.customvisualization.CVItemData;
import com.jaspersoft.jasperreports.customvisualization.CVItemProperty;



/**
 *
 * @author Giulio Toffoli (gtoffoli@tibco.com)
 */
public class CVXmlWriter extends AbstractComponentXmlWriter {

    
    public CVXmlWriter(JasperReportsContext jasperReportsContext)
    {
            super(jasperReportsContext);
    }
    
    
    
    public void writeCV(JRComponentElement componentElement, JRXmlWriter reportWriter) throws IOException {

        
        Component component = componentElement.getComponent();
        CVComponent comp = (CVComponent) component;
        JRXmlWriteHelper writer = reportWriter.getXmlWriteHelper();
        ComponentKey componentKey = componentElement.getComponentKey();

        XmlNamespace namespace = new XmlNamespace(
                CVConstants.NAMESPACE, 
                componentKey.getNamespacePrefix(),
                CVConstants.XSD_LOCATION);
                
        writer.startElement("customvisualization", namespace);
        
        writer.addAttribute(JRXmlConstants.ATTRIBUTE_evaluationTime, 
				comp.getEvaluationTime(), EvaluationTimeEnum.NOW);
        
        if (comp.getEvaluationGroup() != null && comp.getEvaluationGroup().trim().length() > 0)
        {
            writer.addAttribute(JRXmlConstants.ATTRIBUTE_evaluationGroup, 
				comp.getEvaluationGroup());
        }
        
        writer.addAttribute(CVXmlFactory.ATTRIBUTE_processingClass, 
				comp.getProcessingClass());

        writer.addAttribute(CVXmlFactory.ATTRIBUTE_onErrorType, comp.getOnErrorType(), OnErrorTypeEnum.ERROR);
        
        List<CVItemProperty> itemProperties = comp.getItemProperties();
        for (CVItemProperty itemProperty : itemProperties)
        {
                writeCVItemProperty(itemProperty, writer, reportWriter, namespace, componentElement);
        }
        
        
        List<CVItemData> itemDataList = comp.getItemData();
        for (CVItemData itemData : itemDataList)
        {
                if (itemData == null) continue;
                writeItemDataset(itemData, writer, reportWriter, namespace, componentElement);
        }
        
        writer.closeElement();
    }
    
    
    
    private void writeCVItemProperty(CVItemProperty itemProperty, JRXmlWriteHelper writer, JRXmlWriter reportWriter, XmlNamespace namespace, JRComponentElement componentElement) throws IOException
    {
            writer.startElement(CVXmlFactory.ELEMENT_itemProperty, namespace);
            
            writer.addAttribute(JRXmlConstants.ATTRIBUTE_name, itemProperty.getName());
            if(itemProperty.getValue() != null)
            {
                    writer.addAttribute(JRXmlConstants.ATTRIBUTE_value, itemProperty.getValue());
            }
            writeExpression(JRXmlConstants.ELEMENT_valueExpression, JRXmlWriter.JASPERREPORTS_NAMESPACE, itemProperty.getValueExpression(), false, componentElement, reportWriter);
            writer.closeElement();
    }
    
    
    
    private void writeItemDataset(CVItemData itemDataset, JRXmlWriteHelper writer, JRXmlWriter reportWriter, XmlNamespace namespace, JRComponentElement componentElement) throws IOException
    {
            writer.startElement(CVXmlFactory.ELEMENT_cvData, namespace);
            	
            JRElementDataset dataset = itemDataset.getDataset();
            if (dataset != null)
            {
                    reportWriter.writeElementDataset(dataset, false);
            }

            /*   */
            List<CVItem> itemList = itemDataset.getCVItems();
            if (itemList != null && !itemList.isEmpty())
            {
                    
                    for(CVItem item : itemList)
                    {
                            writer.startElement(CVXmlFactory.ELEMENT_item, namespace);
                            if(item.getItemProperties() != null && !item.getItemProperties().isEmpty())
                            {
                                for (CVItemProperty itemProperty : item.getItemProperties())
                                {
                                    if (itemProperty == null) continue;
                                    writeCVItemProperty(itemProperty, writer, reportWriter, namespace, componentElement);
                                }
                            }
                            writer.closeElement();
                    }
                    
            }
            
            
            writer.closeElement();
    }

    
    
    public boolean isToWrite(JRComponentElement jrce, JRXmlWriter writer) {
        return true;
    }



    public void writeToXml(JRComponentElement componentElement, JRXmlWriter reportWriter) throws IOException {
        Component component = componentElement.getComponent();
        if (component instanceof CVComponent)
        {
                writeCV(componentElement, reportWriter);
        }
    }
    

}
