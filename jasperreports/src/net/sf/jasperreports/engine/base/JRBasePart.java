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
package net.sf.jasperreports.engine.base;

import java.io.Serializable;
import java.util.UUID;

import net.sf.jasperreports.engine.DefaultJasperReportsContext;
import net.sf.jasperreports.engine.JRConstants;
import net.sf.jasperreports.engine.JRExpression;
import net.sf.jasperreports.engine.JRPart;
import net.sf.jasperreports.engine.JRPropertiesHolder;
import net.sf.jasperreports.engine.JRPropertiesMap;
import net.sf.jasperreports.engine.JRRuntimeException;
import net.sf.jasperreports.engine.JRVisitable;
import net.sf.jasperreports.engine.component.ComponentKey;
import net.sf.jasperreports.engine.design.events.JRChangeEventsSupport;
import net.sf.jasperreports.engine.design.events.JRPropertyChangeSupport;
import net.sf.jasperreports.engine.part.PartComponent;
import net.sf.jasperreports.engine.part.PartComponentManager;
import net.sf.jasperreports.engine.part.PartComponentsEnvironment;
import net.sf.jasperreports.engine.part.PartEvaluationTime;
import net.sf.jasperreports.engine.util.JRCloneUtils;

/**
 * A read-only {@link JRPart} implementation which is included
 * in compiled reports.
 * 
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id: JRBaseComponentElement.java 5878 2013-01-07 20:23:13Z teodord $
 */
public class JRBasePart implements JRPart, Serializable, JRChangeEventsSupport
{

	private static final long serialVersionUID = JRConstants.SERIAL_VERSION_UID;

	protected UUID uuid;
	private JRPropertiesMap propertiesMap;

	protected JRExpression printWhenExpression;
	protected JRExpression partNameExpression;

	protected ComponentKey componentKey;
	protected PartComponent component;
	
	protected PartEvaluationTime evaluationTime;
	
	protected JRBasePart()
	{
	}
	
	public JRBasePart(JRPart part, JRBaseObjectFactory factory)
	{
		factory.put(part, this);
		
		this.uuid = part.getUUID();
		this.propertiesMap = JRPropertiesMap.getPropertiesClone(part);
		this.printWhenExpression = factory.getExpression(part.getPrintWhenExpression());
		this.partNameExpression = factory.getExpression(part.getPartNameExpression());
		this.evaluationTime = part.getEvaluationTime();

		componentKey = part.getComponentKey();
		
		PartComponentManager manager = PartComponentsEnvironment.getInstance(DefaultJasperReportsContext.getInstance()).getManager(componentKey);
		component = manager.getComponentCompiler(DefaultJasperReportsContext.getInstance()).toCompiledComponent(
				part.getComponent(), factory);

		if (component instanceof JRVisitable)
		{
			((JRVisitable) component).visit(factory);
		}
	}

	@Override
	public UUID getUUID()
	{
		if (uuid == null)
		{
			uuid = UUID.randomUUID();
		}
		return uuid;
	}

	public PartComponent getComponent()
	{
		return component;
	}

	public ComponentKey getComponentKey()
	{
		return componentKey;
	}

	@Override
	public PartEvaluationTime getEvaluationTime()
	{
		return evaluationTime;
	}

	/**
	 *
	 */
	public JRExpression getPrintWhenExpression()
	{
		return this.printWhenExpression;
	}

	@Override
	public JRExpression getPartNameExpression()
	{
		return partNameExpression;
	}

	public boolean hasProperties()
	{
		return propertiesMap != null && propertiesMap.hasProperties();
	}

	public JRPropertiesMap getPropertiesMap()
	{
		if (propertiesMap == null)
		{
			propertiesMap = new JRPropertiesMap();
		}
		return propertiesMap;
	}

	public JRPropertiesHolder getParentProperties()
	{
		return null;
	}

	private transient JRPropertyChangeSupport eventSupport;
	
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

	/**
	 * 
	 */
	public Object clone() 
	{
		JRBasePart clone = null;
		
		try
		{
			clone = (JRBasePart)super.clone();
		}
		catch (CloneNotSupportedException e)
		{
			throw new JRRuntimeException(e);
		}

		clone.printWhenExpression = JRCloneUtils.nullSafeClone(printWhenExpression);
		clone.partNameExpression = JRCloneUtils.nullSafeClone(partNameExpression);
		clone.propertiesMap = JRPropertiesMap.getPropertiesClone(this);
		clone.uuid = null;
		clone.eventSupport = null;
		
		return clone;
	}
}
