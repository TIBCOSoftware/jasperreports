/*******************************************************************************
 * Copyright (C) 2005 - 2014 TIBCO Software Inc. All rights reserved.
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

import com.jaspersoft.jasperreports.customvisualization.CVCompiler;
import com.jaspersoft.jasperreports.customvisualization.CVConstants;
import com.jaspersoft.jasperreports.customvisualization.CVItem;
import com.jaspersoft.jasperreports.customvisualization.CVItemData;
import com.jaspersoft.jasperreports.customvisualization.CVItemProperty;

/**
 * @author Giulio Toffoli (gtoffoli@tibco.com)
 */
public class CVDesignItemData implements CVItemData, JRCloneable, Serializable {

        private static final long serialVersionUID = CVConstants.SERIAL_VERSION_UID;
        
        
	public static final String PROPERTY_ITEM = "cvItem";
    public static final String PROPERTY_DATASET = "dataset";

	private List<CVItem> itemList = new ArrayList<CVItem>();
	private JRElementDataset dataset;
	
	private transient JRPropertyChangeSupport eventSupport;
	
	public CVDesignItemData()
	{
	}

	public CVDesignItemData(CVItemData cvDataset, JRBaseObjectFactory factory)
	{
		this.itemList = getCompiledItems(cvDataset.getCVItems(), factory);
		this.dataset = factory.getElementDataset(cvDataset.getDataset());
	}

	private static List<CVItem> getCompiledItems(List<CVItem> items, JRBaseObjectFactory factory)
	{
		if (items == null)
		{
			return null;
		}
		
		List<CVItem> compiledItems = new ArrayList<CVItem>(items.size());
		for (Iterator<CVItem> it = items.iterator(); it.hasNext();)
		{
			CVItem item = it.next();
			CVItem compiledItem = new CVDesignItem(getCompiledProperties(item.getItemProperties(), factory));
			compiledItems.add(compiledItem);
		}
		return compiledItems;
	}

	private static List<CVItemProperty> getCompiledProperties(List<CVItemProperty> properties, JRBaseObjectFactory factory)
	{
		if (properties == null)
		{
			return null;
		}
		
		List<CVItemProperty> compiledProperties = new ArrayList<CVItemProperty>(properties.size());
		for (Iterator<CVItemProperty> it = properties.iterator(); it.hasNext();)
		{
			CVItemProperty property = it.next();
			CVItemProperty compiledProperty = new CVDesignItemProperty(property.getName(), property.getValue(), factory.getExpression(property.getValueExpression()));
			compiledProperties.add(compiledProperty);
		}
		return compiledProperties;
	}

	public void collectExpressions(JRExpressionCollector collector) {
		CVCompiler.collectExpressions(this, collector);
	}

	public List<CVItem> getMarkers() {
		return itemList;
	}
	
	/**
	 *
	 */
	public void addItem(CVItem item)
	{
		itemList.add(item);
		getEventSupport().fireCollectionElementAddedEvent(PROPERTY_ITEM, item, itemList.size() - 1);
	}
	
	/**
	 *
	 */
	public void addItem(int index, CVItem item)
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
	public CVItem removeItem(CVItem item)
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
        
        
        public List<CVItem> getCVItems(CVItem item)
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
		CVDesignItemData clone = null;
		try
		{
			clone = (CVDesignItemData) super.clone();
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

        
        
    public List<CVItem> getCVItems() {
       return itemList;
    }


}
