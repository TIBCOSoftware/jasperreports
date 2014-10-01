package com.jaspersoft.jasperreports.customvisualization.xml;

import net.sf.jasperreports.engine.type.EvaluationTimeEnum;
import net.sf.jasperreports.engine.type.OnErrorTypeEnum;
import net.sf.jasperreports.engine.xml.JRBaseFactory;
import net.sf.jasperreports.engine.xml.JRXmlConstants;

import org.xml.sax.Attributes;

import com.jaspersoft.jasperreports.customvisualization.design.CVDesignComponent;


public class CVXmlFactory extends JRBaseFactory
{
        public static final String ELEMENT_itemProperty = "itemProperty";
        public static final String ATTRIBUTE_processingClass = "processingClass";
        public static final String ELEMENT_cvData = "cvData";
        public static final String ELEMENT_item = "item";
        public static final String ATTRIBUTE_onErrorType = "onErrorType";
        
	public Object createObject(Attributes atts)
	{

            CVDesignComponent component = new CVDesignComponent();
            
            
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
