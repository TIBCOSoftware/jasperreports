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
package com.jaspersoft.jasperreports.customvisualization.design;

import java.util.ArrayList;
import java.util.List;

import com.jaspersoft.jasperreports.customvisualization.CVComponent;
import com.jaspersoft.jasperreports.customvisualization.CVConstants;

import net.sf.jasperreports.components.items.ItemData;
import net.sf.jasperreports.components.items.ItemProperty;
import net.sf.jasperreports.components.items.StandardItemData;
import net.sf.jasperreports.components.items.StandardItemProperty;
import net.sf.jasperreports.engine.JRRuntimeException;
import net.sf.jasperreports.engine.base.JRBaseObjectFactory;
import net.sf.jasperreports.engine.component.BaseComponentContext;
import net.sf.jasperreports.engine.component.ComponentContext;
import net.sf.jasperreports.engine.design.events.JRPropertyChangeSupport;
import net.sf.jasperreports.engine.type.EvaluationTimeEnum;
import net.sf.jasperreports.engine.type.OnErrorTypeEnum;
import net.sf.jasperreports.engine.util.JRCloneUtils;

public class CVDesignComponent implements CVComponent, net.sf.jasperreports.engine.design.events.JRChangeEventsSupport
{
	public static final long serialVersionUID = CVConstants.SERIAL_VERSION_UID;;

	public static final String PROPERTY_ITEM_PROPERTIES = "itemProperties";
	public static final String PROPERTY_ITEM_DATA = "itemData";
	public static final String PROPERTY_EVALUATION_TIME = "evaluationTime";
	public static final String PROPERTY_EVALUATION_GROUP = "evaluationGroup";
	public static final String PROPERTY_PROCESSING_CLASS = "processingClass";
	public static final String PROPERTY_ON_ERROR_TYPE = CVConstants.PROPERTY_ON_ERROR_TYPE;

	private ComponentContext context;

	// public static final String PROPERTY_COLOR_RANGES = "colorRanges";
	// public static final String PROPERTY_DATASET = "dataset";

	private transient JRPropertyChangeSupport eventSupport;

	// Component attributes
	private EvaluationTimeEnum evaluationTime = EvaluationTimeEnum.NOW;
	private String evaluationGroup;
	private List<ItemProperty> itemProperties = new ArrayList<ItemProperty>();
	private List<ItemData> itemDataList = new ArrayList<ItemData>();
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

		for (ItemProperty prop : component.getItemProperties())
		{
			addItemProperty(new StandardItemProperty(prop.getName(), prop.getValue(),
					baseFactory.getExpression(prop.getValueExpression())));
		}

		for (ItemData itemData : component.getItemData())
		{
			addItemData(new StandardItemData(itemData, baseFactory));
		}

		this.context = new BaseComponentContext(component.getContext(), baseFactory);
	}

	@Override
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
			clone.itemDataList = JRCloneUtils.cloneList(itemDataList);

			return clone;
		}
		catch (CloneNotSupportedException e)
		{
			// never
			throw new JRRuntimeException(e);
		}
	}

	@Override
	public EvaluationTimeEnum getEvaluationTime()
	{
		return evaluationTime;
	}

	public void setEvaluationTime(EvaluationTimeEnum evaluationTimeValue)
	{
		EvaluationTimeEnum old = this.evaluationTime;
		this.evaluationTime = evaluationTimeValue;
		getEventSupport().firePropertyChange(PROPERTY_EVALUATION_TIME, old, this.evaluationTime);
	}

	@Override
	public String getEvaluationGroup()
	{
		return evaluationGroup;
	}

	public void setEvaluationGroup(String evaluationGroup)
	{
		Object old = this.evaluationGroup;
		this.evaluationGroup = evaluationGroup;
		getEventSupport().firePropertyChange(PROPERTY_EVALUATION_GROUP, old, this.evaluationGroup);
	}

	@Override
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

	@Override
	public List<ItemProperty> getItemProperties()
	{
		return itemProperties;
	}

	public void addItemProperty(ItemProperty property)
	{
		itemProperties.add(property);
		getEventSupport().fireCollectionElementAddedEvent(PROPERTY_ITEM_PROPERTIES, property,
				itemProperties.size() - 1);
	}

	public void removeItemProperty(ItemProperty property)
	{
		int idx = itemProperties.indexOf(property);
		if (idx >= 0)
		{
			itemProperties.remove(idx);

			getEventSupport().fireCollectionElementRemovedEvent(PROPERTY_ITEM_PROPERTIES, property, idx);
		}
	}

	@Override
	public List<ItemData> getItemData()
	{
		return itemDataList;
	}

	public void addItemData(ItemData data)
	{
		itemDataList.add(data);
		getEventSupport().fireCollectionElementAddedEvent(PROPERTY_ITEM_DATA, data, itemDataList.size() - 1);
	}

	public void removeItemData(ItemData dataset)
	{
		int idx = itemDataList.indexOf(dataset);
		if (idx >= 0)
		{
			itemDataList.remove(idx);

			getEventSupport().fireCollectionElementRemovedEvent(PROPERTY_ITEM_DATA, dataset, idx);
		}
	}

	/**
	 * @return the processingClass
	 */
	@Override
	public String getProcessingClass()
	{
		return processingClass;
	}

	/**
	 * @param processingClass
	 *            the processingClass to set
	 */
	public void setProcessingClass(String processingClass)
	{

		String old = this.processingClass;
		this.processingClass = processingClass;
		getEventSupport().firePropertyChange(PROPERTY_PROCESSING_CLASS, old, this.processingClass);
	}

	@Override
	public void setContext(ComponentContext context)
	{
		this.context = context;
	}

	@Override
	public ComponentContext getContext()
	{
		return context;
	}
}
