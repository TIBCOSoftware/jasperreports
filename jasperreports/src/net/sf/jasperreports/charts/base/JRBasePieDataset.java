/*
 * ============================================================================
 * GNU Lesser General Public License
 * ============================================================================
 *
 * JasperReports - Free Java report-generating library.
 * Copyright (C) 2001-2006 JasperSoft Corporation http://www.jaspersoft.com
 * 
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307, USA.
 * 
 * JasperSoft Corporation
 * 303 Second Street, Suite 450 North
 * San Francisco, CA 94107
 * http://www.jaspersoft.com
 */
package net.sf.jasperreports.charts.base;

import net.sf.jasperreports.charts.JRPieDataset;
import net.sf.jasperreports.engine.JRChartDataset;
import net.sf.jasperreports.engine.JRConstants;
import net.sf.jasperreports.engine.JRExpression;
import net.sf.jasperreports.engine.JRExpressionCollector;
import net.sf.jasperreports.engine.JRHyperlink;
import net.sf.jasperreports.engine.base.JRBaseChartDataset;
import net.sf.jasperreports.engine.base.JRBaseObjectFactory;
import net.sf.jasperreports.engine.design.JRVerifier;
import net.sf.jasperreports.engine.design.events.JRChangeEventsSupport;
import net.sf.jasperreports.engine.design.events.JRPropertyChangeSupport;


/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id$
 */
public class JRBasePieDataset extends JRBaseChartDataset implements JRPieDataset, JRChangeEventsSupport
{


	/**
	 *
	 */
	private static final long serialVersionUID = JRConstants.SERIAL_VERSION_UID;

	public static final String PROPERTY_MIN_PERCENTAGE = "minPercentage";
	public static final String PROPERTY_MAX_COUNT = "maxCount";

	private Float minPercentage = null;
	private Integer maxCount = null;
	
	protected JRExpression keyExpression = null;
	protected JRExpression valueExpression = null;
	protected JRExpression labelExpression = null;
	private JRHyperlink sectionHyperlink = null;
	protected JRExpression otherKeyExpression = null;
	protected JRExpression otherLabelExpression = null;
	private JRHyperlink otherSectionHyperlink = null;

	
	/**
	 *
	 */
	public JRBasePieDataset(JRChartDataset dataset)
	{
		super(dataset);
	}
	
	
	/**
	 *
	 */
	public JRBasePieDataset(JRPieDataset dataset, JRBaseObjectFactory factory)
	{
		super(dataset, factory);

		minPercentage = dataset.getMinPercentage();
		maxCount = dataset.getMaxCount();
		
		keyExpression = factory.getExpression(dataset.getKeyExpression());
		valueExpression = factory.getExpression(dataset.getValueExpression());
		labelExpression = factory.getExpression(dataset.getLabelExpression());
		sectionHyperlink = factory.getHyperlink(dataset.getSectionHyperlink());
		otherKeyExpression = factory.getExpression(dataset.getOtherKeyExpression());
		otherLabelExpression = factory.getExpression(dataset.getOtherLabelExpression());
		otherSectionHyperlink = factory.getHyperlink(dataset.getOtherSectionHyperlink());
	}

	
	/**
	 *
	 */
	public Float getMinPercentage()
	{
		return minPercentage;
	}
	
	/**
	 *
	 */
	public void setMinPercentage(Float minPercentage)
	{
		Object old = this.minPercentage;
		this.minPercentage = minPercentage;
		getEventSupport().firePropertyChange(PROPERTY_MIN_PERCENTAGE, old, this.minPercentage);
	}

	/**
	 *
	 */
	public Integer getMaxCount()
	{
		return maxCount;
	}
	
	/**
	 *
	 */
	public void setMaxCount(Integer maxCount)
	{
		Object old = this.maxCount;
		this.maxCount = maxCount;
		getEventSupport().firePropertyChange(PROPERTY_MAX_COUNT, old, this.maxCount);
	}

	/**
	 *
	 */
	public JRExpression getKeyExpression()
	{
		return keyExpression;
	}
		
	/**
	 *
	 */
	public JRExpression getValueExpression()
	{
		return valueExpression;
	}
		
	/**
	 *
	 */
	public JRExpression getLabelExpression()
	{
		return labelExpression;
	}


	/**
	 *
	 */
	public JRExpression getOtherKeyExpression()
	{
		return otherKeyExpression;
	}


	/**
	 *
	 */
	public JRExpression getOtherLabelExpression()
	{
		return otherLabelExpression;
	}


	/** 
	 * 
	 */
	public byte getDatasetType() {
		return JRChartDataset.PIE_DATASET;
	}
		

	/**
	 *
	 */
	public void collectExpressions(JRExpressionCollector collector)
	{
		collector.collect(this);
	}


	public JRHyperlink getSectionHyperlink()
	{
		return sectionHyperlink;
	}


	public JRHyperlink getOtherSectionHyperlink()
	{
		return otherSectionHyperlink;
	}


	public void validate(JRVerifier verifier)
	{
		verifier.verify(this);
	}

	/**
	 * 
	 */
	public Object clone() 
	{
		JRBasePieDataset clone = (JRBasePieDataset)super.clone();
		
		if (keyExpression != null)
		{
			clone.keyExpression = (JRExpression)keyExpression.clone();
		}
		if (valueExpression != null)
		{
			clone.valueExpression = (JRExpression)valueExpression.clone();
		}
		if (labelExpression != null)
		{
			clone.labelExpression = (JRExpression)labelExpression.clone();
		}
		if (otherKeyExpression != null)
		{
			clone.otherKeyExpression = (JRExpression)otherKeyExpression.clone();
		}
		if (otherLabelExpression != null)
		{
			clone.otherLabelExpression = (JRExpression)otherLabelExpression.clone();
		}
		if (sectionHyperlink != null)
		{
			clone.sectionHyperlink = (JRHyperlink)sectionHyperlink.clone();
		}
		if (otherSectionHyperlink != null)
		{
			clone.otherSectionHyperlink = (JRHyperlink)otherSectionHyperlink.clone();
		}
		
		return clone;
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

}
