
package com.jaspersoft.jasperreports.customvisualization;

import java.util.List;

import net.sf.jasperreports.engine.JRElementDataset;
import net.sf.jasperreports.engine.JRExpressionCollector;
import net.sf.jasperreports.engine.base.JRBaseObjectFactory;
import net.sf.jasperreports.engine.component.Component;
import net.sf.jasperreports.engine.component.ComponentCompiler;
import net.sf.jasperreports.engine.design.JRVerifier;
import net.sf.jasperreports.engine.type.EvaluationTimeEnum;

import com.jaspersoft.jasperreports.customvisualization.design.CVDesignComponent;


public class CVCompiler implements ComponentCompiler
{
	
	public void collectExpressions(Component component, JRExpressionCollector collector)
	{
            	CVComponent cmp = (CVComponent) component;
		
                for (CVItemProperty p : cmp.getItemProperties())
                {
                    collector.addExpression( p.getValueExpression() );
                }
                
                List<CVItemData> cvDataList = cmp.getItemData();
                if(cvDataList != null) // cvDatasets should neber be null...
                {
                        for(CVItemData cvData : cvDataList)
                        {
                                collectExpressions(cvData, collector);
                        }
                }
                
	}
        
        
        /**
         * Collect the expressions in a cv item dataset.
         * 
         * @param cvDataset
         * @param collector 
         */
        public static void collectExpressions(CVItemData cvData, JRExpressionCollector collector)
	{
		if(cvData != null)
		{
			JRExpressionCollector datasetCollector = collector;

			JRElementDataset dataset = cvData.getDataset();
                        if (dataset != null)
                        {
                            collector.collect(dataset);
                        }

			List<CVItem> items = cvData.getCVItems();
			if (items != null && !items.isEmpty())
			{
				for(CVItem item : items)
				{
					List<CVItemProperty> itemProperties = item.getItemProperties();
					if(itemProperties != null)
					{
						for(CVItemProperty property : itemProperties)
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
                CVComponent cmp = (CVComponent) component;
		CVComponent compiledComponent =  new CVDesignComponent(cmp, baseFactory);
                return compiledComponent;
	}

	
        
        
        public void verify(Component component, JRVerifier verifier)
	{
		CVComponent cmp = (CVComponent) component;
		
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
		
                
                for (CVItemProperty p : cmp.getItemProperties())
                {
                    verifier.verifyExpression(
			p.getValueExpression(), 
			p, 
			null
			);
                }
            
	}


}
