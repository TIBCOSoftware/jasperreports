/*
 * JasperReports - Free Java Reporting Library.
 * Copyright (C) 2001 - 2019 TIBCO Software Inc. All rights reserved.
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
package net.sf.jasperreports.customvisualization.fill;

import net.sf.jasperreports.components.items.fill.FillItemData;
import net.sf.jasperreports.engine.fill.JRFillObjectFactory;

/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 */
public class CVFillItemDataset extends net.sf.jasperreports.components.items.fill.FillItemDataset
{

	protected final FillItemData itemData;
	// protected List<FillMarker> markerList;
	// protected List<Map<String,Object>> evaluatedMarkers;
	// protected JRFillExpressionEvaluator evaluator;
	// protected byte evaluation = JRExpression.EVALUATION_DEFAULT;

	public CVFillItemDataset(FillItemData itemData, JRFillObjectFactory factory)
	{
		super(itemData, factory);

		this.itemData = itemData;

		// factory.registerElementDataset(this);
	}

	// protected void customInitialize()
	// {
	// itemData.reset();
	// }
	//
	//
	// protected void customEvaluate(JRCalculator calculator) throws
	// JRExpressionEvalException
	// {
	// try
	// {
	// System.out.println("Calculator dataset for this item dataset: " +
	// calculator.getFillDataset().getName());
	// itemData.evaluateItems(calculator, evaluation);
	// }
	// catch (JRExpressionEvalException e)
	// {
	// throw e;
	// }
	// catch (JRException e)
	// {
	// throw new JRRuntimeException(e);
	// }
	// }
	//
	// protected void customIncrement()
	// {
	// itemData.addEvaluateItems();
	// }
	//
	// /**
	// * We need to reset our data here....
	// */
	// protected void customInitialize()
	// {
	// itemData.reset();
	// }
	//
	// public void collectExpressions(JRExpressionCollector collector)
	// {
	// //CVCompiler.collectExpressions(markerDataset, collector);
	// }
	//
	// /**
	// * @return the evaluation
	// */
	// public byte getEvaluation() {
	// return evaluation;
	// }
	//
	// /**
	// * @param evaluation the evaluation to set
	// */
	// public void setEvaluation(byte evaluation) {
	// this.evaluation = evaluation;
	// }
}
