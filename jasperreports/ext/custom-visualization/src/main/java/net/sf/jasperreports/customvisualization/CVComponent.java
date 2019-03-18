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
package net.sf.jasperreports.customvisualization;

import java.io.Serializable;
import java.util.List;

import net.sf.jasperreports.components.items.ItemData;
import net.sf.jasperreports.components.items.ItemProperty;
import net.sf.jasperreports.engine.JRCloneable;
import net.sf.jasperreports.engine.component.Component;
import net.sf.jasperreports.engine.component.ContextAwareComponent;
import net.sf.jasperreports.engine.type.EvaluationTimeEnum;
import net.sf.jasperreports.engine.type.OnErrorTypeEnum;

public interface CVComponent extends Component, ContextAwareComponent, JRCloneable, Serializable
{
	public EvaluationTimeEnum getEvaluationTime();

	public String getEvaluationGroup();

	public String getProcessingClass();

	public List<ItemProperty> getItemProperties();

	public List<ItemData> getItemData();

	/**
	 * Indicates how the engine will treat a situation where there is an error.
	 * 
	 * @return a value representing one of the missing image handling constants
	 *         in {@link OnErrorTypeEnum}
	 */
	public OnErrorTypeEnum getOnErrorType();
}
