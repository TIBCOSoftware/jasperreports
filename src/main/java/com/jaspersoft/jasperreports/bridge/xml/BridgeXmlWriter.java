package com.jaspersoft.jasperreports.bridge.xml;

import com.jaspersoft.jasperreports.bridge.BridgeItemProperty;
import com.jaspersoft.jasperreports.bridge.BridgeComponent;
import com.jaspersoft.jasperreports.bridge.BridgeConstants;
import com.jaspersoft.jasperreports.bridge.BridgeItem;
import com.jaspersoft.jasperreports.bridge.BridgeItemData;
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



/**
 *
 * @author Giulio Toffoli (gtoffoli@tibco.com)
 */
public class BridgeXmlWriter extends AbstractComponentXmlWriter {

    
    public BridgeXmlWriter(JasperReportsContext jasperReportsContext)
    {
            super(jasperReportsContext);
    }
    
    
    
    public void writeBridge(JRComponentElement componentElement, JRXmlWriter reportWriter) throws IOException {

        
        Component component = componentElement.getComponent();
        BridgeComponent comp = (BridgeComponent) component;
        JRXmlWriteHelper writer = reportWriter.getXmlWriteHelper();
        ComponentKey componentKey = componentElement.getComponentKey();

        XmlNamespace namespace = new XmlNamespace(
                BridgeConstants.NAMESPACE, 
                componentKey.getNamespacePrefix(),
                BridgeConstants.XSD_LOCATION);
                
        writer.startElement("bridge", namespace);
        
        writer.addAttribute(JRXmlConstants.ATTRIBUTE_evaluationTime, 
				comp.getEvaluationTime(), EvaluationTimeEnum.NOW);
        
        if (comp.getEvaluationGroup() != null && comp.getEvaluationGroup().trim().length() > 0)
        {
            writer.addAttribute(JRXmlConstants.ATTRIBUTE_evaluationGroup, 
				comp.getEvaluationGroup());
        }
        
        writer.addAttribute(BridgeXmlFactory.ATTRIBUTE_processingClass, 
				comp.getProcessingClass());

        writer.addAttribute(BridgeXmlFactory.ATTRIBUTE_onErrorType, comp.getOnErrorType(), OnErrorTypeEnum.ERROR);
        
        List<BridgeItemProperty> itemProperties = comp.getItemProperties();
        for (BridgeItemProperty itemProperty : itemProperties)
        {
                writeBridgeItemProperty(itemProperty, writer, reportWriter, namespace, componentElement);
        }
        
        
        List<BridgeItemData> itemDataList = comp.getItemData();
        for (BridgeItemData itemData : itemDataList)
        {
                if (itemData == null) continue;
                writeItemDataset(itemData, writer, reportWriter, namespace, componentElement);
        }
        
        writer.closeElement();
    }
    
    
    
    private void writeBridgeItemProperty(BridgeItemProperty itemProperty, JRXmlWriteHelper writer, JRXmlWriter reportWriter, XmlNamespace namespace, JRComponentElement componentElement) throws IOException
    {
            writer.startElement(BridgeXmlFactory.ELEMENT_itemProperty, namespace);
            
            writer.addAttribute(JRXmlConstants.ATTRIBUTE_name, itemProperty.getName());
            if(itemProperty.getValue() != null)
            {
                    writer.addAttribute(JRXmlConstants.ATTRIBUTE_value, itemProperty.getValue());
            }
            writeExpression(JRXmlConstants.ELEMENT_valueExpression, JRXmlWriter.JASPERREPORTS_NAMESPACE, itemProperty.getValueExpression(), false, componentElement, reportWriter);
            writer.closeElement();
    }
    
    
    
    private void writeItemDataset(BridgeItemData itemDataset, JRXmlWriteHelper writer, JRXmlWriter reportWriter, XmlNamespace namespace, JRComponentElement componentElement) throws IOException
    {
            writer.startElement(BridgeXmlFactory.ELEMENT_bridgeData, namespace);
            	
            JRElementDataset dataset = itemDataset.getDataset();
            if (dataset != null)
            {
                    reportWriter.writeElementDataset(dataset, false);
            }

            /*   */
            List<BridgeItem> itemList = itemDataset.getBridgeItems();
            if (itemList != null && !itemList.isEmpty())
            {
                    
                    for(BridgeItem item : itemList)
                    {
                            writer.startElement(BridgeXmlFactory.ELEMENT_item, namespace);
                            if(item.getItemProperties() != null && !item.getItemProperties().isEmpty())
                            {
                                for (BridgeItemProperty itemProperty : item.getItemProperties())
                                {
                                    if (itemProperty == null) continue;
                                    writeBridgeItemProperty(itemProperty, writer, reportWriter, namespace, componentElement);
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
        if (component instanceof BridgeComponent)
        {
                writeBridge(componentElement, reportWriter);
        }
    }
    

}
