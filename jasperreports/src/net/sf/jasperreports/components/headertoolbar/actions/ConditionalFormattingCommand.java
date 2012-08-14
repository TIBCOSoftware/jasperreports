/*
 * JasperReports - Free Java Reporting Library.
 * Copyright (C) 2001 - 2011 Jaspersoft Corporation. All rights reserved.
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
package net.sf.jasperreports.components.headertoolbar.actions;

import java.util.List;

import net.sf.jasperreports.engine.JRPropertiesMap;
import net.sf.jasperreports.engine.JasperReportsContext;
import net.sf.jasperreports.engine.design.JRDesignDataset;
import net.sf.jasperreports.web.commands.Command;
import net.sf.jasperreports.web.util.JacksonUtil;

/**
 * @author Narcis Marcu (narcism@users.sourceforge.net)
 * @version $Id$
 */
public class ConditionalFormattingCommand implements Command 
{
	
	public static final String DATASET_CONDITIONAL_FORMATTING_PROPERTY = "net.sf.jasperreports.conditional.formatting";
	
	private JasperReportsContext jasperReportsContext;
	protected JRDesignDataset dataset;
	protected ConditionalFormattingData conditionalFormattingData;
	private String oldSerializedConditionsData;
	private String newSerializedConditionsData;
	
	public ConditionalFormattingCommand(JasperReportsContext jasperReportsContext, JRDesignDataset dataset, ConditionalFormattingData conditionalFormattingData) 
	{
		this.jasperReportsContext = jasperReportsContext;
		this.dataset = dataset;
		this.conditionalFormattingData = conditionalFormattingData;
	}

	public void execute() 
	{
		// get existing conditions data as JSON string
		String serializedConditionsData = "[]";
		JRPropertiesMap propertiesMap = dataset.getPropertiesMap();
		if (propertiesMap.getProperty(DATASET_CONDITIONAL_FORMATTING_PROPERTY) != null) {
			serializedConditionsData = propertiesMap.getProperty(DATASET_CONDITIONAL_FORMATTING_PROPERTY);
		}
		
		oldSerializedConditionsData = serializedConditionsData;
		
		JacksonUtil jacksonUtil = JacksonUtil.getInstance(jasperReportsContext);
		List<ConditionalFormattingData> existingConditionsData = jacksonUtil.loadList(serializedConditionsData, ConditionalFormattingData.class);
		boolean foundExistingConditionData = false;
		
		for (ConditionalFormattingData cfData: existingConditionsData) {
			if (cfData.getFieldName().equals(conditionalFormattingData.getFieldName())) {
				cfData.setConditions(conditionalFormattingData.getConditions());
				foundExistingConditionData = true;
				break;
			}
		}
			
		if (!foundExistingConditionData && conditionalFormattingData.getConditions().size() > 0) {
			existingConditionsData.add(conditionalFormattingData);
		}
		
		newSerializedConditionsData = jacksonUtil.getJsonString(existingConditionsData);
		propertiesMap.setProperty(DATASET_CONDITIONAL_FORMATTING_PROPERTY, newSerializedConditionsData);
	}
	
	public void undo() 
	{
		dataset.getPropertiesMap().setProperty(DATASET_CONDITIONAL_FORMATTING_PROPERTY, oldSerializedConditionsData);
	}

	public void redo() 
	{
		dataset.getPropertiesMap().setProperty(DATASET_CONDITIONAL_FORMATTING_PROPERTY, newSerializedConditionsData);
	}
}
