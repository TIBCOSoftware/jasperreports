package com.jaspersoft.jasperreports.bridge.xml;

import com.jaspersoft.jasperreports.bridge.design.BridgeDesignComponent;
import net.sf.jasperreports.engine.type.EvaluationTimeEnum;
import net.sf.jasperreports.engine.type.OnErrorTypeEnum;
import net.sf.jasperreports.engine.xml.JRBaseFactory;
import net.sf.jasperreports.engine.xml.JRXmlConstants;
import org.xml.sax.Attributes;


public class BridgeXmlFactory extends JRBaseFactory
{
        public static final String ELEMENT_itemProperty = "itemProperty";
        public static final String ATTRIBUTE_processingClass = "processingClass";
        public static final String ELEMENT_bridgeData = "bridgeData";
        public static final String ELEMENT_item = "item";
        public static final String ATTRIBUTE_onErrorType = "onErrorType";
        
	public Object createObject(Attributes atts)
	{

            BridgeDesignComponent component = new BridgeDesignComponent();
            
            
            EvaluationTimeEnum evaluationTime = EvaluationTimeEnum.getByName(atts.getValue(JRXmlConstants.ATTRIBUTE_evaluationTime));
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
                component.setProcessingClass( processingClass );
            }
            
            
            OnErrorTypeEnum onErrorType = OnErrorTypeEnum.getByName(atts.getValue(ATTRIBUTE_onErrorType));
            if(onErrorType != null)
            {
                    component.setOnErrorType(onErrorType);
            }
            
                
            return component;
        }

}
