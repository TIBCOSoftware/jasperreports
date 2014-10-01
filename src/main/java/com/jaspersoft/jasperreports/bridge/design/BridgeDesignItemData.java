/*
 * JasperReports - Free Java Reporting Library.
 * Copyright (C) 2001 - 2013 Jaspersoft Corporation. All rights reserved.
 * http://www.jaspersoft.com
 *
 * Unless you have purchased a commercial license agreement from Jaspersoft,
 * the following license terms apply:
 *
 * This program is part of JasperReports.
 *
 * JasperReports is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * JasperReports is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with JasperReports. If not, see <http://www.gnu.org/licenses/>.
 */
package com.jaspersoft.jasperreports.bridge.design;

import com.jaspersoft.jasperreports.bridge.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import net.sf.jasperreports.engine.JRCloneable;

import net.sf.jasperreports.engine.JRElementDataset;
import net.sf.jasperreports.engine.JRExpressionCollector;
import net.sf.jasperreports.engine.JRRuntimeException;
import net.sf.jasperreports.engine.base.JRBaseObjectFactory;
import net.sf.jasperreports.engine.design.events.JRPropertyChangeSupport;
import net.sf.jasperreports.engine.util.JRCloneUtils;

/**
 * @author Giulio Toffoli (gtoffoli@tibco.com)
 */
public class BridgeDesignItemData implements BridgeItemData, JRCloneable, Serializable {

        private static final long serialVersionUID = BridgeConstants.SERIAL_VERSION_UID;
        
        
	public static final String PROPERTY_ITEM = "bridgeItem";
        public static final String PROPERTY_DATASET = "dataset";

	private List<BridgeItem> itemList = new ArrayList<BridgeItem>();
	private JRElementDataset dataset;
	
	private transient JRPropertyChangeSupport eventSupport;
	
	public BridgeDesignItemData()
	{
	}

	public BridgeDesignItemData(BridgeItemData bridgeDataset, JRBaseObjectFactory factory)
	{
		this.itemList = getCompiledItems(bridgeDataset.getBridgeItems(), factory);
		this.dataset = factory.getElementDataset(bridgeDataset.getDataset());
	}

	private static List<BridgeItem> getCompiledItems(List<BridgeItem> items, JRBaseObjectFactory factory)
	{
		if (items == null)
		{
			return null;
		}
		
		List<BridgeItem> compiledItems = new ArrayList<BridgeItem>(items.size());
		for (Iterator<BridgeItem> it = items.iterator(); it.hasNext();)
		{
			BridgeItem item = it.next();
			BridgeItem compiledItem = new BridgeDesignItem(getCompiledProperties(item.getItemProperties(), factory));
			compiledItems.add(compiledItem);
		}
		return compiledItems;
	}

	private static List<BridgeItemProperty> getCompiledProperties(List<BridgeItemProperty> properties, JRBaseObjectFactory factory)
	{
		if (properties == null)
		{
			return null;
		}
		
		List<BridgeItemProperty> compiledProperties = new ArrayList<BridgeItemProperty>(properties.size());
		for (Iterator<BridgeItemProperty> it = properties.iterator(); it.hasNext();)
		{
			BridgeItemProperty property = it.next();
			BridgeItemProperty compiledProperty = new BridgeDesignItemProperty(property.getName(), property.getValue(), factory.getExpression(property.getValueExpression()));
			compiledProperties.add(compiledProperty);
		}
		return compiledProperties;
	}

	public void collectExpressions(JRExpressionCollector collector) {
		BridgeCompiler.collectExpressions(this, collector);
	}

	public List<BridgeItem> getMarkers() {
		return itemList;
	}
	
	/**
	 *
	 */
	public void addItem(BridgeItem item)
	{
		itemList.add(item);
		getEventSupport().fireCollectionElementAddedEvent(PROPERTY_ITEM, item, itemList.size() - 1);
	}
	
	/**
	 *
	 */
	public void addItem(int index, BridgeItem item)
	{
		if(index >=0 && index < itemList.size())
			itemList.add(index, item);
		else{
			itemList.add(item);
			index = itemList.size() - 1;
		}
		getEventSupport().fireCollectionElementAddedEvent(PROPERTY_ITEM, itemList, index);
	}
        
        

	/**
	 *
	 */
	public BridgeItem removeItem(BridgeItem item)
	{
		if (itemList != null)
		{
			int idx = itemList.indexOf(itemList);
			if (idx >= 0)
			{
				itemList.remove(idx);
				getEventSupport().fireCollectionElementRemovedEvent(PROPERTY_ITEM, itemList, idx);
			}
		}
		return item;
	}
        
        
        public List<BridgeItem> getBridgeItems(BridgeItem item)
	{
            return itemList;
        }

	
        @Override
	public JRElementDataset getDataset()
	{
		return dataset;
	}
	
	/**
	 * Sets the dataset information that will be used to create the item list.
	 * 
	 * @param dataset the dataset information
	 * @see #getDataset()
	 */
	public void setDataset(JRElementDataset dataset)
	{
		Object old = this.dataset;
		this.dataset = dataset;
		getEventSupport().firePropertyChange(PROPERTY_DATASET, old, this.dataset);
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

	public Object clone()
	{
		BridgeDesignItemData clone = null;
		try
		{
			clone = (BridgeDesignItemData) super.clone();
		} 
		catch (CloneNotSupportedException e)
		{
			// never
			throw new JRRuntimeException(e);
		}
		clone.dataset = JRCloneUtils.nullSafeClone(dataset);
		clone.itemList = JRCloneUtils.cloneList(itemList);
		clone.eventSupport = null;
		return clone;
	}

        
        
    public List<BridgeItem> getBridgeItems() {
       return itemList;
    }


}
