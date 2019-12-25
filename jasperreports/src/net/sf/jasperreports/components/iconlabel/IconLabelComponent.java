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
package net.sf.jasperreports.components.iconlabel;

import java.awt.Color;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;

import net.sf.jasperreports.engine.JRBoxContainer;
import net.sf.jasperreports.engine.JRConstants;
import net.sf.jasperreports.engine.JRDefaultStyleProvider;
import net.sf.jasperreports.engine.JRImageAlignment;
import net.sf.jasperreports.engine.JRLineBox;
import net.sf.jasperreports.engine.JRStyle;
import net.sf.jasperreports.engine.JRTextField;
import net.sf.jasperreports.engine.base.JRBaseLineBox;
import net.sf.jasperreports.engine.base.JRBaseObjectFactory;
import net.sf.jasperreports.engine.component.BaseComponentContext;
import net.sf.jasperreports.engine.component.ComponentContext;
import net.sf.jasperreports.engine.component.ContextAwareComponent;
import net.sf.jasperreports.engine.design.JRDesignTextField;
import net.sf.jasperreports.engine.design.events.JRChangeEventsSupport;
import net.sf.jasperreports.engine.design.events.JRPropertyChangeSupport;
import net.sf.jasperreports.engine.type.HorizontalImageAlignEnum;
import net.sf.jasperreports.engine.type.VerticalImageAlignEnum;
import net.sf.jasperreports.engine.util.StyleResolver;

/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 */
public class IconLabelComponent implements ContextAwareComponent, JRBoxContainer, JRImageAlignment, Serializable, JRChangeEventsSupport 
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public static final String PROPERTY_ICON_POSITION = "iconPosition";
	public static final String PROPERTY_LABEL_FILL = "labelFill";
	public static final String PROPERTY_HORIZONTAL_ALIGNMENT = "horizontalAlignment";
	public static final String PROPERTY_VERTICAL_ALIGNMENT = "verticalAlignment";

	private JRLineBox lineBox;
	private JRTextField labelTextField;
	private JRTextField iconTextField;
	private IconPositionEnum iconPosition;
	private ContainerFillEnum labelFill;
	private HorizontalImageAlignEnum horizontalImageAlign;
	private VerticalImageAlignEnum verticalImageAlign;

	private ComponentContext context;
	
	private transient JRPropertyChangeSupport eventSupport;
	
	public IconLabelComponent(JRDefaultStyleProvider defaultStyleProvider) 
	{
		lineBox = new JRBaseLineBox(this);
		labelTextField = new JRDesignTextField(defaultStyleProvider);
		iconTextField = new JRDesignTextField(defaultStyleProvider);
	}
	
	public IconLabelComponent(IconLabelComponent component, JRBaseObjectFactory objectFactory) 
	{
		this.lineBox = component.getLineBox().clone(this);
		this.labelTextField = (JRTextField)objectFactory.getVisitResult(component.getLabelTextField());
		this.iconTextField = (JRTextField)objectFactory.getVisitResult(component.getIconTextField());
		
		this.iconPosition = component.getIconPosition();
		this.labelFill = component.getLabelFill();
		this.horizontalImageAlign = component.getOwnHorizontalImageAlign();
		this.verticalImageAlign = component.getOwnVerticalImageAlign();

		this.context = new BaseComponentContext(component.getContext(), objectFactory);
	}

	@Override
	public void setContext(ComponentContext context)
	{
		this.context = context;
	}
	
	@Override
	public ComponentContext getContext()
	{
		return context;
	}
	
	@Override
	public JRLineBox getLineBox()
	{
		return lineBox;
	}
	
	/**
	 *
	 */
	public void setLineBox(JRLineBox lineBox) 
	{
		this.lineBox = lineBox;
	}
	
	@Override
	public JRDefaultStyleProvider getDefaultStyleProvider()
	{
		return context == null ? labelTextField.getDefaultStyleProvider() : context.getComponentElement().getDefaultStyleProvider();
	}
	
	/**
	 *
	 */
	protected StyleResolver getStyleResolver()
	{
		return getDefaultStyleProvider().getStyleResolver();
	}
	
	@Override
	public JRStyle getStyle()
	{
		return context == null ? null : context.getComponentElement().getStyle();
	}
	
	@Override
	public String getStyleNameReference()
	{
		return context == null ? null : context.getComponentElement().getStyleNameReference();
	}

	@Override
	public Color getDefaultLineColor() 
	{
		return Color.black;
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

	@Override
	public HorizontalImageAlignEnum getHorizontalImageAlign()
	{
		return getStyleResolver().getHorizontalImageAlign(this);
	}
		
	@Override
	public HorizontalImageAlignEnum getOwnHorizontalImageAlign()
	{
		return horizontalImageAlign;
	}
		
	@Override
	public void setHorizontalImageAlign(HorizontalImageAlignEnum horizontalImageAlign)
	{
		Object old = this.horizontalImageAlign;
		this.horizontalImageAlign = horizontalImageAlign;
		getEventSupport().firePropertyChange(PROPERTY_HORIZONTAL_ALIGNMENT, old, this.horizontalImageAlign);
	}

	@Override
	public VerticalImageAlignEnum getVerticalImageAlign()
	{
		return getStyleResolver().getVerticalImageAlign(this);
	}
		
	@Override
	public VerticalImageAlignEnum getOwnVerticalImageAlign()
	{
		return verticalImageAlign;
	}
		
	@Override
	public void setVerticalImageAlign(VerticalImageAlignEnum verticalImageAlign)
	{
		Object old = this.verticalImageAlign;
		this.verticalImageAlign = verticalImageAlign;
		getEventSupport().firePropertyChange(PROPERTY_VERTICAL_ALIGNMENT, old, this.verticalImageAlign);
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
	public void setLabelFill(ContainerFillEnum labelFill) 
	{
		ContainerFillEnum old = this.labelFill;
		this.labelFill = labelFill;
		getEventSupport().firePropertyChange(PROPERTY_LABEL_FILL, old, this.labelFill);
	}

	@Override
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


	/*
	 * These fields are only for serialization backward compatibility.
	 */
	private int PSEUDO_SERIAL_VERSION_UID = JRConstants.PSEUDO_SERIAL_VERSION_UID; //NOPMD
	/**
	 * @deprecated
	 */
	private net.sf.jasperreports.engine.type.HorizontalAlignEnum horizontalAlign;
	/**
	 * @deprecated
	 */
	private net.sf.jasperreports.engine.type.VerticalAlignEnum verticalAlign;
	
	@SuppressWarnings("deprecation")
	private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException
	{
		in.defaultReadObject();

		if (PSEUDO_SERIAL_VERSION_UID < JRConstants.PSEUDO_SERIAL_VERSION_UID_6_0_2)
		{
			horizontalImageAlign = net.sf.jasperreports.engine.type.HorizontalAlignEnum.getHorizontalImageAlignEnum(horizontalAlign);
			verticalImageAlign = net.sf.jasperreports.engine.type.VerticalAlignEnum.getVerticalImageAlignEnum(verticalAlign);

			horizontalAlign = null;
			verticalAlign = null;
		}
	}
}
