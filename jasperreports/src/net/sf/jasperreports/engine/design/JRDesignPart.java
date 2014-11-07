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
package net.sf.jasperreports.engine.design;

import java.util.UUID;

import net.sf.jasperreports.engine.JRConstants;
import net.sf.jasperreports.engine.JRExpression;
import net.sf.jasperreports.engine.base.JRBasePart;
import net.sf.jasperreports.engine.component.ComponentKey;
import net.sf.jasperreports.engine.part.PartComponent;
import net.sf.jasperreports.engine.part.PartEvaluationTime;


/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id: JRDesignBand.java 5878 2013-01-07 20:23:13Z teodord $
 */
public class JRDesignPart extends JRBasePart
{
	

	/**
	 *
	 */
	private static final long serialVersionUID = JRConstants.SERIAL_VERSION_UID;

	public static final String PROPERTY_PRINT_WHEN_EXPRESSION = "printWhenExpression";
	public static final String PROPERTY_PART_NAME_EXPRESSION = "partNameExpression";
	public static final String PROPERTY_COMPONENT = "component";
	public static final String PROPERTY_COMPONENT_KEY = "componentKey";
	public static final String PROPERTY_EVALUATION_TIME = "evaluationTime";
	
	/**
	 * Creates an empty report part.
	 */
	public JRDesignPart()
	{
	}

	public void setUUID(UUID uuid)
	{
		this.uuid = uuid;
	}

	/**
	 *
	 */
	public void setPrintWhenExpression(JRExpression expression)
	{
		Object old = this.printWhenExpression;
		this.printWhenExpression = expression;
		getEventSupport().firePropertyChange(PROPERTY_PRINT_WHEN_EXPRESSION, old, this.printWhenExpression);
	}
	
	public void setPartNameExpression(JRExpression expression)
	{
		Object old = this.partNameExpression;
		this.partNameExpression = expression;
		getEventSupport().firePropertyChange(PROPERTY_PART_NAME_EXPRESSION, old, this.partNameExpression);
	}
	

	/**
	 * Sets the component type key that corresponds to the component instance.
	 * 
	 * @param componentKey the component type key
	 * @see #getComponentKey()
	 */
	public void setComponentKey(ComponentKey componentKey)
	{
		Object old = this.componentKey;
		this.componentKey = componentKey;
		getEventSupport().firePropertyChange(PROPERTY_COMPONENT_KEY, old, this.componentKey);
	}


	/**
	 * Sets the component instance wrapped by this part.
	 * 
	 * @param component the component instance
	 * @see #getComponent()
	 */
	public void setComponent(PartComponent component)
	{
		Object old = this.component;
		this.component = component;
		getEventSupport().firePropertyChange(PROPERTY_COMPONENT, old, this.component);
	}
	
	public void setEvaluationTime(PartEvaluationTime evaluationTime)
	{
		Object old = this.evaluationTime;
		this.evaluationTime = evaluationTime;
		getEventSupport().firePropertyChange(PROPERTY_EVALUATION_TIME, old, this.evaluationTime);
	}

}
