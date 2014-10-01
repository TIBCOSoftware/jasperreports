package com.jaspersoft.jasperreports.customvisualization.design;

import java.util.ArrayList;
import java.util.List;

import net.sf.jasperreports.engine.JRRuntimeException;
import net.sf.jasperreports.engine.base.JRBaseObjectFactory;
import net.sf.jasperreports.engine.component.BaseComponentContext;
import net.sf.jasperreports.engine.component.ComponentContext;
import net.sf.jasperreports.engine.design.events.JRPropertyChangeSupport;
import net.sf.jasperreports.engine.type.EvaluationTimeEnum;
import net.sf.jasperreports.engine.type.OnErrorTypeEnum;
import net.sf.jasperreports.engine.util.JRCloneUtils;

import com.jaspersoft.jasperreports.customvisualization.CVComponent;
import com.jaspersoft.jasperreports.customvisualization.CVConstants;
import com.jaspersoft.jasperreports.customvisualization.CVItemData;
import com.jaspersoft.jasperreports.customvisualization.CVItemProperty;



public class CVDesignComponent implements CVComponent,
            net.sf.jasperreports.engine.design.events.JRChangeEventsSupport
{

	public static final long serialVersionUID = CVConstants.SERIAL_VERSION_UID;;

	public static final String PROPERTY_ITEM_PROPERTIES = "itemProperties";
        public static final String PROPERTY_ITEM_DATA = "itemData";
        public static final String PROPERTY_EVALUATION_TIME = "evaluationTime";
	public static final String PROPERTY_EVALUATION_GROUP = "evaluationGroup";
        public static final String PROPERTY_PROCESSING_CLASS = "processingClass";
        public static final String PROPERTY_ON_ERROR_TYPE = CVConstants.PROPERTY_ON_ERROR_TYPE;
        
        
        private ComponentContext context;
        
	//public static final String PROPERTY_COLOR_RANGES = "colorRanges";
	//public static final String PROPERTY_DATASET = "dataset";
        
        
        private transient JRPropertyChangeSupport eventSupport;
        
        // Component attributes
        private EvaluationTimeEnum evaluationTime = EvaluationTimeEnum.NOW;
	private String evaluationGroup;
        private List<CVItemProperty> itemProperties = new ArrayList<CVItemProperty>();
        private List<CVItemData> itemDataList = new ArrayList<CVItemData>();
        private String processingClass;
        private OnErrorTypeEnum onErrorType = OnErrorTypeEnum.ERROR;
        
        
        public CVDesignComponent()
	{
            
        }
	
	public CVDesignComponent(CVComponent component, JRBaseObjectFactory baseFactory)
	{
                
                this.evaluationTime = component.getEvaluationTime();
		this.evaluationGroup = component.getEvaluationGroup();
		this.processingClass = component.getProcessingClass();
                this.onErrorType = component.getOnErrorType();
                
                for (CVItemProperty prop : component.getItemProperties())
                {
                    addItemProperty( new CVDesignItemProperty(prop.getName(), prop.getValue(), baseFactory.getExpression(prop.getValueExpression()))  );
                }
                
                for (CVItemData itemData : component.getItemData())
                {
                    addItemData( new CVDesignItemData( itemData, baseFactory)  );
                }
                
                this.context = new BaseComponentContext(component.getContext(), baseFactory);
                
                

        }

     

	public JRPropertyChangeSupport getEventSupport()
	{
		synchronized (this)
		{
			if (eventSupport == null)
			{
				eventSupport = new JRPropertyChangeSupport(this);
			}
		}

		return eventSupport;
	}

        @Override
	public Object clone()
	{
		CVDesignComponent clone = null;
		try
		{ 
			clone = (CVDesignComponent) super.clone();
                        clone.itemProperties = JRCloneUtils.cloneList(itemProperties);

                        return clone;
		}
		catch (CloneNotSupportedException e)
		{
			// never
			throw new JRRuntimeException(e);
		}
	}

        public EvaluationTimeEnum getEvaluationTime()
        {
                return evaluationTime;
        }

        public void setEvaluationTime(EvaluationTimeEnum evaluationTimeValue)
        {
                EvaluationTimeEnum old = this.evaluationTime;
                this.evaluationTime = evaluationTimeValue;
                getEventSupport().firePropertyChange(PROPERTY_EVALUATION_TIME, 
                                old, this.evaluationTime);
        }

        public String getEvaluationGroup()
        {
                return evaluationGroup;
        }

        public void setEvaluationGroup(String evaluationGroup)
        {
                Object old = this.evaluationGroup;
                this.evaluationGroup = evaluationGroup;
                getEventSupport().firePropertyChange(PROPERTY_EVALUATION_GROUP, 
                                old, this.evaluationGroup);
        }
        
        /**
	 * 
	 */
	public OnErrorTypeEnum getOnErrorType()
	{
		return this.onErrorType;
	}

	/**
	 * 
	 */
	public void setOnErrorType(OnErrorTypeEnum onErrorType)
        {
		OnErrorTypeEnum old = this.onErrorType;
		this.onErrorType = onErrorType;
		getEventSupport().firePropertyChange(PROPERTY_ON_ERROR_TYPE, old, this.onErrorType);
	}

        public List<CVItemProperty> getItemProperties() 
	{
		return itemProperties;
	}
	
	public void addItemProperty(CVItemProperty property)
	{
		itemProperties.add(property);
                getEventSupport().fireCollectionElementAddedEvent(PROPERTY_ITEM_PROPERTIES, property, itemProperties.size() - 1);
	}
	
	public void removeItemProperty(CVItemProperty property)
	{
		int idx = itemProperties.indexOf(property);
		if (idx >= 0)
		{
			itemProperties.remove(idx);
			
			getEventSupport().fireCollectionElementRemovedEvent(PROPERTY_ITEM_PROPERTIES, 
					property, idx);
		}
	}
        
        
        
         public List<CVItemData> getItemData() 
	{
		return itemDataList;
	}
	
	public void addItemData(CVItemData data)
	{
		itemDataList.add(data);
                getEventSupport().fireCollectionElementAddedEvent(PROPERTY_ITEM_DATA, data, itemDataList.size() - 1);
	}
	
	public void removeItemData(CVItemData dataset)
	{
		int idx = itemDataList.indexOf(dataset);
		if (idx >= 0)
		{
			itemDataList.remove(idx);
			
			getEventSupport().fireCollectionElementRemovedEvent(PROPERTY_ITEM_DATA, 
					dataset, idx);
		}
	}
        
        
        

    /**
     * @return the processingClass
     */
    public String getProcessingClass() {
        return processingClass;
    }

    /**
     * @param processingClass the processingClass to set
     */
    public void setProcessingClass(String processingClass) {
        
        String old = this.processingClass;
        this.processingClass = processingClass;
        getEventSupport().firePropertyChange(PROPERTY_PROCESSING_CLASS, 
                        old, this.processingClass);
    }

    public void setContext(ComponentContext context)
    {
            this.context = context;
    }

    public ComponentContext getContext()
    {
            return context;
    }

        
}
