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
package com.jaspersoft.jasperreports.customvisualization.fill;

import net.sf.jasperreports.components.items.Item;
import net.sf.jasperreports.components.items.ItemData;
import net.sf.jasperreports.components.items.fill.FillItem;
import net.sf.jasperreports.engine.component.FillContextProvider;
import net.sf.jasperreports.engine.fill.JRFillObjectFactory;

/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id: CVFillItemData.java 6004 2013-03-20 12:49:30Z teodord $
 */
public class CVFillItemData extends net.sf.jasperreports.components.items.fill.FillItemData
{

	/**
	 *
	 */
	public CVFillItemData(FillContextProvider fillContextProvider, ItemData itemData, JRFillObjectFactory factory)// throws
																													// JRException
	{
		super(fillContextProvider, itemData, factory);
	}

	@Override
	public FillItem getFillItem(Item item, JRFillObjectFactory factory)
	{
		return new CVFillItem(item, factory);
	}

	// /**
	// *
	// */
	// protected ItemData cvDataset;
	// protected List<CVFillItem> itemsList;
	// protected CVFillItemDataset fillItemDataset;
	// protected FillContextProvider fillContextProvider;
	// private List<Map<String, Object>> evaluatedItems = null;
	//
	// /**
	// *
	// */
	// public CVFillItemData(FillContextProvider fillContextProvider,
	// ItemData cvDataset, JRFillObjectFactory factory)// throws
	// // JRException
	// {
	// factory.put(cvDataset, this);
	//
	// this.cvDataset = cvDataset;
	// this.fillContextProvider = fillContextProvider;
	//
	// if (cvDataset.getDataset() != null) {
	// fillItemDataset = new CVFillItemDataset(this, factory);
	// }
	//
	// /* */
	// List<Item> srcItemList = cvDataset.getItems();
	// if (srcItemList != null && !srcItemList.isEmpty()) {
	// itemsList = new ArrayList<CVFillItem>();
	// for (Item item : srcItemList) {
	// if (item != null) {
	// itemsList.add(new CVFillItem(item, factory));
	// }
	// }
	// }
	// }
	//
	// /**
	// *
	// */
	// public JRElementDataset getDataset() {
	// return cvDataset.getDataset();
	// }
	//
	// /**
	// *
	// */
	// public void evaluateItems(JRFillExpressionEvaluator evaluator,
	// byte evaluation) throws JRException {
	// if (itemsList != null) {
	// for (CVFillItem item : itemsList) {
	//
	// System.out.println("Evaluating item in the list of items for a data item
	// ------------");
	// System.out.println("Properties to evaluate in this item: ");
	// for (ItemProperty ip : item.getProperties())
	// {
	// System.out.println( ip.getName() + " ==> " +
	// ip.getValueExpression().getText());
	// }
	//
	// item.evaluateProperties(evaluator, evaluation);
	//
	// System.out.println("Properties after evaluation");
	//
	// for (ItemProperty ip : item.getProperties())
	// {
	// System.out.println( ip.getName() + " ==> " +
	// item.getEvaluatedProperties().get(ip.getName()));
	// }
	//
	//
	// }
	// }
	// }
	//
	// /**
	// *
	// */
	// public List<Map<String, Object>> getEvaluateItems(byte evaluation)
	// throws JRException {
	//
	// // if (fillItemDataset != null)
	// // {
	// // fillItemDataset.setEvaluation(evaluation);
	// // fillItemDataset.evaluateDatasetRun(evaluation);
	// // }
	//
	// if (itemsList != null) {
	// if (getDataset() != null) {
	// evaluateItems(fillContextProvider.getFillContext(), evaluation);
	// addEvaluateItems();
	// }
	// }
	//
	// if (evaluatedItems == null) {
	// // No records...
	// evaluatedItems = new ArrayList<Map<String, Object>>();
	// }
	//
	// return evaluatedItems;
	// }
	//
	// /**
	// *
	// */
	// public void addEvaluateItems() {
	// if (itemsList != null) {
	// if (evaluatedItems == null || getDataset() == null) {
	// evaluatedItems = new ArrayList<Map<String, Object>>();
	// }
	//
	// for (CVFillItem item : itemsList) {
	// Map<String, Object> record = item.getEvaluatedProperties();
	// if (record == null)
	// continue;
	// evaluatedItems.add(record);
	// }
	// }
	// }
	//
	// public void reset() {
	//
	// evaluatedItems = null;
	// }
}
