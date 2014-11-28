/*
 * JasperReports - Free Java Reporting Library.
 * Copyright (C) 2001 - 2014 TIBCO Software Inc. All rights reserved.
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

import net.sf.jasperreports.engine.JRPropertiesMap;
import net.sf.jasperreports.engine.JRTextField;
import net.sf.jasperreports.engine.JasperReportsContext;
import net.sf.jasperreports.web.commands.Command;
import net.sf.jasperreports.web.util.JacksonUtil;

/**
 * @author Narcis Marcu (narcism@users.sourceforge.net)
 */
public class ConditionalFormattingCommand implements Command 
{
	
	public static final String COLUMN_CONDITIONAL_FORMATTING_PROPERTY = "net.sf.jasperreports.components.headertoolbar.conditional.formatting";
	
	private JasperReportsContext jasperReportsContext;
	protected ConditionalFormattingData conditionalFormattingData;
	private String oldSerializedConditionsData;
	private String newSerializedConditionsData;
	private JRTextField textElement;

	public ConditionalFormattingCommand(JasperReportsContext jasperReportsContext, JRTextField textElement, ConditionalFormattingData conditionalFormattingData)
	{
		this.jasperReportsContext = jasperReportsContext;
		this.textElement = textElement;
		this.conditionalFormattingData = conditionalFormattingData;
	}

	public void execute() 
	{
		if (textElement != null)
		{
			// get existing condition data as JSON string
			String serializedConditionData = null;
			JRPropertiesMap propertiesMap = textElement.getPropertiesMap();
			if (propertiesMap.containsProperty(COLUMN_CONDITIONAL_FORMATTING_PROPERTY)) {
				serializedConditionData = propertiesMap.getProperty(COLUMN_CONDITIONAL_FORMATTING_PROPERTY);
			}
			
			oldSerializedConditionsData = serializedConditionData;
			
			JacksonUtil jacksonUtil = JacksonUtil.getInstance(jasperReportsContext);
//			ConditionalFormattingData existingConditionData = jacksonUtil.loadObject(serializedConditionData, ConditionalFormattingData.class);
//			if (existingConditionData != null) {
//				existingConditionData.setConditions(conditionalFormattingData.getConditions());
//			} else {
//				existingConditionData = conditionalFormattingData;
//			}
//			
//			newSerializedConditionsData = jacksonUtil.getJsonString(existingConditionData);
			newSerializedConditionsData = jacksonUtil.getJsonString(conditionalFormattingData);
			propertiesMap.setProperty(COLUMN_CONDITIONAL_FORMATTING_PROPERTY, newSerializedConditionsData);
		}
	}
	
	public void undo() 
	{
		if (textElement != null) 
		{
			textElement.getPropertiesMap().setProperty(COLUMN_CONDITIONAL_FORMATTING_PROPERTY, oldSerializedConditionsData);
		}
	}

	public void redo() 
	{
		if (textElement != null) 
		{
			textElement.getPropertiesMap().setProperty(COLUMN_CONDITIONAL_FORMATTING_PROPERTY, newSerializedConditionsData);
		}			
	}
}
