package com.jaspersoft.jasperreports.bridge.fill;

import com.jaspersoft.jasperreports.bridge.BridgeConstants;
import com.jaspersoft.jasperreports.bridge.BridgeComponent;
import com.jaspersoft.jasperreports.bridge.BridgeItemData;
import com.jaspersoft.jasperreports.bridge.BridgeItemProperty;
import com.jaspersoft.jasperreports.bridge.BridgePrintElement;
import com.jaspersoft.jasperreports.bridge.Processor;
import com.jaspersoft.jasperreports.bridge.design.BridgeDesignComponent;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.component.*;
import net.sf.jasperreports.engine.fill.*;
import net.sf.jasperreports.engine.type.*;
import net.sf.jasperreports.engine.util.JRClassLoader;


public class BridgeFillComponent extends BaseFillComponent implements Serializable, FillContextProvider
{
        private static final long serialVersionUID = BridgeConstants.SERIAL_VERSION_UID;

	private final BridgeComponent component;
        
        // Fill component elements
        private List<BridgeFillItemProperty> itemProperties;
        private List<BridgeFillItemData> itemDataList = new ArrayList<BridgeFillItemData>();
        private List<List<Map<String,Object>>> datasetsData = new ArrayList<List<Map<String,Object>>>();
        private Processor processor = null;
        
        
        
        
//      private List<SVGMapFillLayer> fillLayers;
//	private SVGMapFillDataset dataset;     
//      private List<SVGMapFillColorRange> colorRanges;
	

	
	public BridgeFillComponent(BridgeComponent component, JRFillObjectFactory factory)
	{
                this.component = component;
                this.itemProperties = new ArrayList<BridgeFillItemProperty>();
                
                if (factory != null)
                {
                    //factory.registerElementDataset(this.dataset);
                    for (BridgeItemProperty itemProperty : component.getItemProperties())
                    {
                        this.itemProperties.add(new BridgeFillItemProperty(itemProperty, factory));
                    }
                    
                    for (BridgeItemData data : component.getItemData())
                    {
                        if (data != null)
                        {
                                itemDataList.add( new BridgeFillItemData(this, data, factory));
                                datasetsData.add(null);
                        }
                    }
                }
                
                
                String processingClass = component.getProcessingClass();
		if (processingClass != null && processingClass.length() > 0) {
			try {
				Class<?> myClass = JRClassLoader.loadClassForName(processingClass);
				processor = (Processor) myClass.newInstance();
			} catch (Exception e) {
				throw new JRRuntimeException("Could not create processor instance.", e);
			}
		}
        }
        
        protected boolean isEvaluateNow()
	{
		return getComponent().getEvaluationTime() == EvaluationTimeEnum.NOW;
	}
        
	
	protected BridgeComponent getComponent()
	{
		return component;
	}

	
	public void evaluate(byte evaluation) throws JRException
	{
            if (isEvaluateNow())
            {
                evaluateComponent(evaluation);
            }
            
        }
        
        public void evaluateComponent(byte evaluation) throws JRException
	{
                if (fillContext != null)
                {
                    for (BridgeFillItemProperty itemProperty : this.itemProperties)
                    {
                        itemProperty.evaluate(fillContext, evaluation);
                    }
                    
                    for (int i=0; i<this.itemDataList.size(); ++i)
                    {
                         BridgeFillItemData itemData = this.itemDataList.get(i);
                        if (itemData != null)
                        {
                            List<Map<String,Object>> newSet = itemData.getEvaluateItems(evaluation);
                            datasetsData.set(i, newSet );
                        }
                        else
                        {
                            datasetsData.set(i, null);
                        }
                    }
                }
                
                
                
        }
        
	public FillPrepareResult prepare(int availableHeight)
	{
		return FillPrepareResult.PRINT_NO_STRETCH;
	}


	public JRPrintElement fill()
	{
                JRComponentElement element = fillContext.getComponentElement();
		JRTemplateGenericElement template = new JRTemplateGenericElement(
				fillContext.getElementOrigin(), 
				fillContext.getDefaultStyleProvider(),
				BridgePrintElement.BRIDGE_ELEMENT_TYPE);
		template = deduplicate(template);
                
                
		JRTemplateGenericPrintElement printElement = new JRTemplateGenericPrintElement(template); // printElementOriginator);
		printElement.setUUID(element.getUUID());
		printElement.setX(element.getX());
		printElement.setY(fillContext.getElementPrintY());
		printElement.setWidth(element.getWidth());
		printElement.setHeight(element.getHeight());
                
                
                if (element.hasProperties() &&
                    element.getPropertiesMap().getProperty("bridge.keepTemporaryFiles") != null &&
                    element.getPropertiesMap().getProperty("bridge.keepTemporaryFiles").equals("true"))
                {
                    printElement.getPropertiesMap().setProperty("bridge.keepTemporaryFiles", "true");
                }
                
                if (isEvaluateNow())
		{
                        evaluationPerformed(printElement);
		}
		else
		{
                        fillContext.registerDelayedEvaluation(printElement, 
					getComponent().getEvaluationTime(), 
                                        getComponent().getEvaluationGroup());
		}
                
                return printElement;
                 
                
	}
        
        
      
    @Override
    public void evaluateDelayedElement(JRPrintElement element, byte evaluation) throws JRException
    {
            evaluateComponent(evaluation);
            evaluationPerformed((JRTemplateGenericPrintElement)element);
    }

    
    /**
    * The right place to perform fill objects processing after evaluation
    * of all the expressions.
    *
    */
    protected void evaluationPerformed(JRTemplateGenericPrintElement element)
    {

          // Build the processor...
          Map<String, Object> configuration = new HashMap<String, Object>();
          
          //configuration.put(Processor.CONF_FILL_CONTEXT, this.fillContext);
          configuration.put(Processor.CONF_PRINT_ELEMENT, element);
          element.setParameterValue(BridgePrintElement.PARAMETER_ON_ERROR_TYPE, this.component.getOnErrorType().getName());
          
          
          List<List<Map<String,Object>>> savedDatasetsData = new ArrayList<List<Map<String,Object>>>();
          
          
          for (int i=0; i<datasetsData.size(); ++i)
          {
                  savedDatasetsData.add(datasetsData.get(i));
                  datasetsData.set(i,null);
          }
          
          
          configuration.put(Processor.CONF_SERIES, savedDatasetsData);
          
          for (BridgeFillItemProperty p : itemProperties)
          {
                configuration.put(p.getName(), p.getValue());
          }
          
          
          if (processor != null)
          {
              try {
                  configuration = processor.processConfiguration(configuration);
              } catch (Throwable t)
              {
                 throw new JRRuntimeException("Bridge component processing failed.", t); 
              }
          }
          
          element.setParameterValue(BridgePrintElement.CONFIGURATION, configuration);
          
    }

   
   
    public FillContext getFillContext()
    {
            return fillContext;
    }


}
