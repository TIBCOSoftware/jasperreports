/*
 * JasperReports - Free Java Reporting Library.
 * Copyright (C) 2001 - 2023 Cloud Software Group, Inc. All rights reserved.
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
package net.sf.jasperreports.customvisualization;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

import net.sf.jasperreports.components.items.ItemData;
import net.sf.jasperreports.components.items.ItemProperty;
import net.sf.jasperreports.customvisualization.design.CVDesignComponent;
import net.sf.jasperreports.engine.JRCloneable;
import net.sf.jasperreports.engine.component.Component;
import net.sf.jasperreports.engine.component.ContextAwareComponent;
import net.sf.jasperreports.engine.type.EvaluationTimeEnum;
import net.sf.jasperreports.engine.type.OnErrorTypeEnum;

@JsonTypeName(CVConstants.COMPONENT_NAME)
@JsonDeserialize(as = CVDesignComponent.class)
public interface CVComponent extends Component, ContextAwareComponent, JRCloneable, Serializable
{
	@JacksonXmlProperty(isAttribute = true)
	public EvaluationTimeEnum getEvaluationTime();

	@JacksonXmlProperty(isAttribute = true)
	public String getEvaluationGroup();

	@JacksonXmlProperty(isAttribute = true)
	public String getProcessingClass();

	@JsonGetter("properties")
	@JacksonXmlProperty(localName = "property")
	@JacksonXmlElementWrapper(useWrapping = false)
	public List<ItemProperty> getItemProperties();

	@JacksonXmlElementWrapper(useWrapping = false)
	public List<ItemData> getItemData();

	/**
	 * Indicates how the engine will treat a situation where there is an error.
	 * 
	 * @return a value representing one of the missing image handling constants
	 *         in {@link OnErrorTypeEnum}
	 */
	@JacksonXmlProperty(isAttribute = true)
	public OnErrorTypeEnum getOnErrorType();
}
