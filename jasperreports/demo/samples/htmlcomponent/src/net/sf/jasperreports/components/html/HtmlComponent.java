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
package net.sf.jasperreports.components.html;

import java.io.Serializable;

import net.sf.jasperreports.engine.JRCloneable;
import net.sf.jasperreports.engine.JRExpression;
import net.sf.jasperreports.engine.JRRuntimeException;
import net.sf.jasperreports.engine.base.JRBaseObjectFactory;
import net.sf.jasperreports.engine.component.BaseComponentContext;
import net.sf.jasperreports.engine.component.ComponentContext;
import net.sf.jasperreports.engine.component.ContextAwareComponent;
import net.sf.jasperreports.engine.design.events.JRChangeEventsSupport;
import net.sf.jasperreports.engine.design.events.JRPropertyChangeSupport;
import net.sf.jasperreports.engine.type.EvaluationTimeEnum;
import net.sf.jasperreports.engine.type.HorizontalAlignEnum;
import net.sf.jasperreports.engine.type.ScaleImageEnum;
import net.sf.jasperreports.engine.type.VerticalAlignEnum;
import net.sf.jasperreports.engine.util.JRCloneUtils;

/**
 * @author Narcis Marcu (narcism@users.sourceforge.net)
 * @version $Id$
 */
public class HtmlComponent implements ContextAwareComponent, Serializable, JRChangeEventsSupport, JRCloneable
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public static final String PROPERTY_EVALUATION_TIME = "evaluationTime";
	public static final String PROPERTY_EVALUATION_GROUP = "evaluationGroup";
	public static final String PROPERTY_SCALE_TYPE = "scaleType";
	public static final String PROPERTY_HORIZONTAL_ALIGN = "horizontalAlign";
	public static final String PROPERTY_VERTICAL_ALIGN = "verticalAlign";
	public static final String PROPERTY_HTMLCONTENT_EXPRESSION = "htmlContentExpression";
	public static final String PROPERTY_CLIP_ON_OVERFLOW = "clipOnOverflow";
	
	private JRExpression htmlContentExpression;
	private ScaleImageEnum scaleType = ScaleImageEnum.RETAIN_SHAPE;
	private HorizontalAlignEnum horizontalAlign = HorizontalAlignEnum.LEFT;
	private VerticalAlignEnum verticalAlign = VerticalAlignEnum.MIDDLE;
	private EvaluationTimeEnum evaluationTime = EvaluationTimeEnum.NOW;
	private String evaluationGroup;
	private Boolean clipOnOverflow = Boolean.TRUE;
	private ComponentContext context;

	private transient JRPropertyChangeSupport eventSupport;
	
	public HtmlComponent() {
	}
	
	public HtmlComponent(HtmlComponent component, JRBaseObjectFactory objectFactory) {
		this.scaleType = component.getScaleType();
		this.horizontalAlign = component.getHorizontalAlign();
		this.verticalAlign = component.getVerticalAlign();
		this.htmlContentExpression = objectFactory.getExpression(component.getHtmlContentExpression());
		this.context = new BaseComponentContext(component.getContext(), objectFactory);
		this.evaluationTime= component.getEvaluationTime();
		this.evaluationGroup = component.getEvaluationGroup();
		this.clipOnOverflow = component.getClipOnOverflow();
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
	 * @return the htmlContent
	 */
	public JRExpression getHtmlContentExpression() {
		return htmlContentExpression;
	}
	
	/**
	 * @param htmlContent the htmlContent to set
	 */
	public void setHtmlContentExpression(JRExpression htmlContentExpression) {
		Object old = this.htmlContentExpression;
		this.htmlContentExpression = htmlContentExpression;
		getEventSupport().firePropertyChange(PROPERTY_HTMLCONTENT_EXPRESSION, 
				old, this.htmlContentExpression);
	}
	
	/**
	 * @return the scaleType
	 */
	public ScaleImageEnum getScaleType() {
		return scaleType;
	}

	/**
	 * @param scaleType the scaleType to set
	 */
	public void setScaleType(ScaleImageEnum scaleType) {
		Object old = this.scaleType;
		this.scaleType = scaleType;
		getEventSupport().firePropertyChange(PROPERTY_SCALE_TYPE, 
				old, this.scaleType);
	}

	/**
	 * @return the horizontalAlign
	 */
	public HorizontalAlignEnum getHorizontalAlign() {
		return horizontalAlign;
	}
	
	/**
	 * @param horizontalAlign the horizontalAlign to set
	 */
	public void setHorizontalAlign(HorizontalAlignEnum horizontalAlign) {
		Object old = this.horizontalAlign;
		this.horizontalAlign = horizontalAlign;
		getEventSupport().firePropertyChange(PROPERTY_HORIZONTAL_ALIGN, 
				old, this.horizontalAlign);
	}
	
	/**
	 * @return the verticalAlign
	 */
	public VerticalAlignEnum getVerticalAlign() {
		return verticalAlign;
	}
	
	/**
	 * @param verticalAlign the verticalAlign to set
	 */
	public void setVerticalAlign(VerticalAlignEnum verticalAlign) {
		Object old = this.verticalAlign;
		this.verticalAlign = verticalAlign;
		getEventSupport().firePropertyChange(PROPERTY_VERTICAL_ALIGN, 
				old, this.verticalAlign);
	}

	/**
	 * @return the evaluationTime
	 */
	public EvaluationTimeEnum getEvaluationTime() {
		return evaluationTime;
	}

	/**
	 * @param evaluationTime the evaluationTime to set
	 */
	public void setEvaluationTime(EvaluationTimeEnum evaluationTime) {
		Object old = this.evaluationTime;
		this.evaluationTime = evaluationTime;
		getEventSupport().firePropertyChange(PROPERTY_EVALUATION_TIME, 
				old, this.evaluationTime);
	}

	public String getEvaluationGroup()
	{
		return evaluationGroup;
	}

	public void setEvaluationGroup(String evaluationGroup)
	{
		Object old = this.evaluationGroup;
		this.evaluationGroup = evaluationGroup;
		getEventSupport().firePropertyChange(PROPERTY_EVALUATION_GROUP, 
				old, this.evaluationGroup);
	}
	
	public Boolean getClipOnOverflow() 
	{
		return clipOnOverflow;
	}
	
	public void setClipOnOverflow(Boolean clipOnOverflow) 
	{
		Object old = this.clipOnOverflow;
		this.clipOnOverflow = clipOnOverflow;
		getEventSupport().firePropertyChange(PROPERTY_CLIP_ON_OVERFLOW, 
				old, this.clipOnOverflow);
	}

	public Object clone()
	{
		HtmlComponent clone = null;
		try
		{
			clone = (HtmlComponent) super.clone();
		}
		catch (CloneNotSupportedException e)
		{
			// never
			throw new JRRuntimeException(e);
		}
		clone.htmlContentExpression = JRCloneUtils.nullSafeClone(htmlContentExpression);
		clone.eventSupport = null;
		return clone;
	}

	public JRPropertyChangeSupport getEventSupport() {
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
