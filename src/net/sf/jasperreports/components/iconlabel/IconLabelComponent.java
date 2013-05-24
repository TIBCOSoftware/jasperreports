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
package net.sf.jasperreports.components.iconlabel;

import java.io.Serializable;

import net.sf.jasperreports.engine.JRDefaultStyleProvider;
import net.sf.jasperreports.engine.JRTextField;
import net.sf.jasperreports.engine.base.JRBaseObjectFactory;
import net.sf.jasperreports.engine.component.BaseComponentContext;
import net.sf.jasperreports.engine.component.ComponentContext;
import net.sf.jasperreports.engine.component.ContextAwareComponent;
import net.sf.jasperreports.engine.design.JRDesignTextField;
import net.sf.jasperreports.engine.design.events.JRChangeEventsSupport;
import net.sf.jasperreports.engine.design.events.JRPropertyChangeSupport;
import net.sf.jasperreports.engine.type.HorizontalAlignEnum;
import net.sf.jasperreports.engine.type.VerticalAlignEnum;

/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id: TextInputComponent.java 5922 2013-02-19 11:03:27Z teodord $
 */
public class IconLabelComponent implements ContextAwareComponent, Serializable, JRChangeEventsSupport {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public static final String PROPERTY_ICON_POSITION = "iconPosition";
	public static final String PROPERTY_LABEL_FILL = "labelFill";
	public static final String PROPERTY_HORIZONTAL_ALIGN = "horizontalAlign";
	public static final String PROPERTY_VERTICAL_ALIGN = "verticalAlign";

	private JRTextField labelTextField;
	private JRTextField iconTextField;
	private IconPositionEnum iconPosition;
	private ContainerFillEnum labelFill;
	private HorizontalAlignEnum horizontalAlign;
	private VerticalAlignEnum verticalAlign;

	private ComponentContext context;

	private transient JRPropertyChangeSupport eventSupport;
	
	public IconLabelComponent(JRDefaultStyleProvider defaultStyleProvider) 
	{
		labelTextField = new JRDesignTextField(defaultStyleProvider);
		iconTextField = new JRDesignTextField(defaultStyleProvider);
	}
	
	public IconLabelComponent(IconLabelComponent component, JRBaseObjectFactory objectFactory) 
	{
		this.labelTextField = (JRTextField)objectFactory.getVisitResult(component.getLabelTextField());
		this.iconTextField = (JRTextField)objectFactory.getVisitResult(component.getIconTextField());
		
		this.iconPosition = component.getIconPosition();
		this.labelFill = component.getLabelFill();
		this.horizontalAlign = component.getHorizontalAlign();
		this.verticalAlign = component.getVerticalAlign();

		this.context = new BaseComponentContext(component.getContext(), objectFactory);
	}

	public void setContext(ComponentContext context)
	{
		this.context = context;
	}
	
	public ComponentContext getContext()
	{
		return context;
	}
	
	/**
	 *
	 */
	public JRTextField getLabelTextField() 
	{
		return labelTextField;
	}

	/**
	 *
	 */
	public void setLabelTextField(JRTextField labelTextField) 
	{
		this.labelTextField = labelTextField;
	}
	
	/**
	 *
	 */
	public JRTextField getIconTextField() 
	{
		return iconTextField;
	}

	/**
	 *
	 */
	public void setIconTextField(JRTextField iconTextField) 
	{
		this.iconTextField = iconTextField;
	}
	
	/**
	 *
	 */
	public IconPositionEnum getIconPosition() 
	{
		return iconPosition;
	}

	/**
	 *
	 */
	public void setIconPosition(IconPositionEnum iconPosition) 
	{
		IconPositionEnum old = this.iconPosition;
		this.iconPosition = iconPosition;
		getEventSupport().firePropertyChange(PROPERTY_ICON_POSITION, old, this.iconPosition);
	}

	/**
	 *
	 */
	public HorizontalAlignEnum getHorizontalAlign() 
	{
		return horizontalAlign;
	}

	/**
	 *
	 */
	public void setHorizontalAlign(HorizontalAlignEnum horizontalAlign) 
	{
		HorizontalAlignEnum old = this.horizontalAlign;
		this.horizontalAlign = horizontalAlign;
		getEventSupport().firePropertyChange(PROPERTY_HORIZONTAL_ALIGN, old, this.horizontalAlign);
	}

	/**
	 *
	 */
	public VerticalAlignEnum getVerticalAlign() 
	{
		return verticalAlign;
	}

	/**
	 *
	 */
	public void setVerticalAlign(VerticalAlignEnum verticalAlign) 
	{
		VerticalAlignEnum old = this.verticalAlign;
		this.verticalAlign = verticalAlign;
		getEventSupport().firePropertyChange(PROPERTY_VERTICAL_ALIGN, old, this.verticalAlign);
	}

	/**
	 *
	 */
	public ContainerFillEnum getLabelFill() 
	{
		return labelFill;
	}

	/**
	 *
	 */
	public void setTextFill(ContainerFillEnum labelFill) 
	{
		ContainerFillEnum old = this.labelFill;
		this.labelFill = labelFill;
		getEventSupport().firePropertyChange(PROPERTY_LABEL_FILL, old, this.labelFill);
	}

	/**
	 *
	 */
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
}
