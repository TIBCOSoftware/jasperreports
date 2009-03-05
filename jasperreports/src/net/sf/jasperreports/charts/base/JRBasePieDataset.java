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

import java.io.IOException;
import java.io.ObjectInputStream;

import net.sf.jasperreports.charts.JRPieDataset;
import net.sf.jasperreports.charts.JRPieSeries;
import net.sf.jasperreports.charts.design.JRDesignPieSeries;
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
	
	protected JRPieSeries[] pieSeries = null;

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
		
		/*   */
		JRPieSeries[] srcPieSeries = dataset.getSeries();
		if (srcPieSeries != null && srcPieSeries.length > 0)
		{
			pieSeries = new JRPieSeries[srcPieSeries.length];
			for(int i = 0; i < pieSeries.length; i++)
			{
				pieSeries[i] = factory.getPieSeries(srcPieSeries[i]);
			}
		}

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
	public JRPieSeries[] getSeries()
	{
		return pieSeries;
	}

	/**
	 * @deprecated Replaced by {@link #getSeries()}.
	 */
	public JRExpression getKeyExpression()
	{
		return pieSeries != null && pieSeries.length > 0 ? pieSeries[0].getKeyExpression() : null;
	}
		
	/**
	 * @deprecated Replaced by {@link #getSeries()}.
	 */
	public JRExpression getValueExpression()
	{
		return pieSeries != null && pieSeries.length > 0 ? pieSeries[0].getValueExpression() : null;
	}
		
	/**
	 * @deprecated Replaced by {@link #getSeries()}.
	 */
	public JRExpression getLabelExpression()
	{
		return pieSeries != null && pieSeries.length > 0 ? pieSeries[0].getLabelExpression() : null;
	}

	/**
	 * @deprecated Replaced by {@link #getSeries()}.
	 */
	public JRHyperlink getSectionHyperlink()
	{
		return pieSeries != null && pieSeries.length > 0 ? pieSeries[0].getSectionHyperlink() : null;
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
	public JRHyperlink getOtherSectionHyperlink()
	{
		return otherSectionHyperlink;
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


	/**
	 *
	 */
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
		
		if (pieSeries != null)
		{
			clone.pieSeries = new JRPieSeries[pieSeries.length];
			for(int i = 0; i < pieSeries.length; i++)
			{
				pieSeries[i] = (JRPieSeries)pieSeries[i].clone();
			}
		}
		
		if (otherKeyExpression != null)
		{
			clone.otherKeyExpression = (JRExpression)otherKeyExpression.clone();
		}
		if (otherLabelExpression != null)
		{
			clone.otherLabelExpression = (JRExpression)otherLabelExpression.clone();
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

	/**
	 * These fields are only for serialization backward compatibility.
	 */
	private JRExpression keyExpression = null;
	private JRExpression valueExpression = null;
	private JRExpression labelExpression = null;
	private JRHyperlink sectionHyperlink = null;
	
	private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException
	{
		in.defaultReadObject();
		
		if (pieSeries == null)
		{
			pieSeries = new JRPieSeries[1];

			JRDesignPieSeries ps = new JRDesignPieSeries();
			ps.setKeyExpression(keyExpression);
			ps.setValueExpression(valueExpression);
			ps.setLabelExpression(labelExpression);
			ps.setSectionHyperlink(sectionHyperlink);
			pieSeries[0] = ps;

			keyExpression = null;
			valueExpression = null;
			labelExpression = null;
			sectionHyperlink = null;
		}
	}

}
