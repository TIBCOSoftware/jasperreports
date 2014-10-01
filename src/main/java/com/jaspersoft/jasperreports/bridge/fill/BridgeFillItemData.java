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
package com.jaspersoft.jasperreports.bridge.fill;

import com.jaspersoft.jasperreports.bridge.BridgeItem;
import com.jaspersoft.jasperreports.bridge.BridgeItemData;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import net.sf.jasperreports.engine.JRElementDataset;
import net.sf.jasperreports.engine.JRException;

import net.sf.jasperreports.engine.fill.JRFillExpressionEvaluator;
import net.sf.jasperreports.engine.fill.JRFillObjectFactory;


/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id: BridgeFillItemData.java 6004 2013-03-20 12:49:30Z teodord $
 */
public class BridgeFillItemData
{

	/**
	 *
	 */
	protected BridgeItemData bridgeDataset;
	protected List<BridgeFillItem> itemsList;
	protected BridgeFillItemDataset fillItemDataset;
	protected FillContextProvider fillContextProvider;
	private List<Map<String,Object>> evaluatedItems = null;
	
	/**
	 *
	 */
	public BridgeFillItemData(
		FillContextProvider fillContextProvider,
		BridgeItemData bridgeDataset, 
		JRFillObjectFactory factory
		)// throws JRException
	{
		factory.put(bridgeDataset, this);
		
		this.bridgeDataset = bridgeDataset;
		this.fillContextProvider = fillContextProvider;

		if (bridgeDataset.getDataset() != null)
		{
			fillItemDataset = new BridgeFillItemDataset(this, factory);
		}

		/*   */
		List<BridgeItem> srcItemList = bridgeDataset.getBridgeItems();
		if (srcItemList != null && !srcItemList.isEmpty())
		{
			itemsList = new ArrayList<BridgeFillItem>();
			for(BridgeItem item : srcItemList)
			{
				if(item != null)
				{
					itemsList.add(new BridgeFillItem(item, factory));
				}
			}
		}
	}
	
	/**
	 *
	 */
	public JRElementDataset getDataset()
	{
		return bridgeDataset.getDataset();
	}
	
	/**
	 *
	 */
	public void evaluateItems(JRFillExpressionEvaluator evaluator, byte evaluation) throws JRException
	{
		if (itemsList != null)
		{
			for(BridgeFillItem item : itemsList)
			{
				item.evaluateProperties(evaluator, evaluation);
			}
		}
	}
	
	/**
	 *
	 */
	public List<Map<String,Object>> getEvaluateItems(byte evaluation) throws JRException
	{
                
                
//		if (fillItemDataset != null)
//		{
//			fillItemDataset.setEvaluation(evaluation);
//			fillItemDataset.evaluateDatasetRun(evaluation);
//		}
		
		if (itemsList != null)
		{
			if (getDataset() == null)
			{
                                evaluateItems(fillContextProvider.getFillContext(), evaluation);
                                addEvaluateItems();
			}
		}
		
                if (evaluatedItems == null)
                {
                    // No records...
                    evaluatedItems = new ArrayList<Map<String,Object>>();
                }
                
                
		return evaluatedItems;
	}
	
	/**
	 *
	 */
	public void addEvaluateItems()
	{
		if (itemsList != null)
		{
			if (evaluatedItems == null || getDataset() == null)
			{
				evaluatedItems = new ArrayList<Map<String,Object>>();
			}

                        for(BridgeFillItem item : itemsList)
			{
                            Map<String, Object> record = item.getEvaluatedProperties();
                            if (record == null) continue;
			    evaluatedItems.add(record);
			}
		}
	}
        
        
        public void reset()
        {
            evaluatedItems = null;
        }
}
