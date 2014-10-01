package com.jaspersoft.jasperreports.bridge;

import java.io.Serializable;
import java.util.List;
import net.sf.jasperreports.engine.type.EvaluationTimeEnum;
import net.sf.jasperreports.engine.type.OnErrorTypeEnum;


public interface BridgeComponent 
    extends net.sf.jasperreports.engine.component.Component, 
            net.sf.jasperreports.engine.component.ContextAwareComponent,
            net.sf.jasperreports.engine.JRCloneable,
            Serializable
{

       public EvaluationTimeEnum getEvaluationTime();
       public String getEvaluationGroup();
       
       public String getProcessingClass();
       
       public List<BridgeItemProperty> getItemProperties();
       
       public List<BridgeItemData> getItemData();
       
       /**
	 * Indicates how the engine will treat a situation where there is an error.
	 * @return a value representing one of the missing image handling constants in {@link OnErrorTypeEnum}
	 */
       public OnErrorTypeEnum getOnErrorType();
       
}
