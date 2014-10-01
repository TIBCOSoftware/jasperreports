
package com.jaspersoft.jasperreports.bridge;

import com.jaspersoft.jasperreports.bridge.design.BridgeDesignComponent;
import java.util.List;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.base.*;
import net.sf.jasperreports.engine.component.*;
import net.sf.jasperreports.engine.design.*;
import net.sf.jasperreports.engine.type.*;


public class BridgeCompiler implements ComponentCompiler
{
	
	public void collectExpressions(Component component, JRExpressionCollector collector)
	{
            	BridgeComponent cmp = (BridgeComponent) component;
		
                for (BridgeItemProperty p : cmp.getItemProperties())
                {
                    collector.addExpression( p.getValueExpression() );
                }
                
                List<BridgeItemData> bridgeDataList = cmp.getItemData();
                if(bridgeDataList != null) // bridgeDatasets should neber be null...
                {
                        for(BridgeItemData bridgeData : bridgeDataList)
                        {
                                collectExpressions(bridgeData, collector);
                        }
                }
                
	}
        
        
        /**
         * Collect the expressions in a bridge item dataset.
         * 
         * @param bridgeDataset
         * @param collector 
         */
        public static void collectExpressions(BridgeItemData bridgeData, JRExpressionCollector collector)
	{
		if(bridgeData != null)
		{
			JRExpressionCollector datasetCollector = collector;

			JRElementDataset dataset = bridgeData.getDataset();
                        if (dataset != null)
                        {
                            collector.collect(dataset);
                        }

			List<BridgeItem> items = bridgeData.getBridgeItems();
			if (items != null && !items.isEmpty())
			{
				for(BridgeItem item : items)
				{
					List<BridgeItemProperty> itemProperties = item.getItemProperties();
					if(itemProperties != null)
					{
						for(BridgeItemProperty property : itemProperties)
						{
							datasetCollector.addExpression(property.getValueExpression());
						}
					}
				}
			}
		}
	}
        

	public Component toCompiledComponent(Component component,
			JRBaseObjectFactory baseFactory)
	{
                BridgeComponent cmp = (BridgeComponent) component;
		BridgeComponent compiledComponent =  new BridgeDesignComponent(cmp, baseFactory);
                return compiledComponent;
	}

	
        
        
        public void verify(Component component, JRVerifier verifier)
	{
		BridgeComponent cmp = (BridgeComponent) component;
		
		EvaluationTimeEnum evaluationTime = cmp.getEvaluationTime();
		if (evaluationTime == EvaluationTimeEnum.AUTO)
		{
			verifier.addBrokenRule("Auto evaluation time is not supported for this component", cmp);
		}
		else if (evaluationTime == EvaluationTimeEnum.GROUP)
		{
			String evaluationGroup = cmp.getEvaluationGroup();
			if (evaluationGroup == null || evaluationGroup.length() == 0)
			{
				verifier.addBrokenRule("No evaluation group set", cmp);
			}
			else if (!verifier.getReportDesign().getGroupsMap().containsKey(evaluationGroup))
			{
				verifier.addBrokenRule("Evalution group \"" + evaluationGroup + " not found", cmp);
			}
		}
		
                
                for (BridgeItemProperty p : cmp.getItemProperties())
                {
                    verifier.verifyExpression(
			p.getValueExpression(), 
			p, 
			null
			);
                }
            
	}


}
