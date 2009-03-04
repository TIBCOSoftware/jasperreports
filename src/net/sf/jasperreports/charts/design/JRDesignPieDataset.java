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
package net.sf.jasperreports.charts.design;

import net.sf.jasperreports.charts.JRPieDataset;
import net.sf.jasperreports.engine.JRChartDataset;
import net.sf.jasperreports.engine.JRConstants;
import net.sf.jasperreports.engine.JRExpression;
import net.sf.jasperreports.engine.JRExpressionCollector;
import net.sf.jasperreports.engine.JRHyperlink;
import net.sf.jasperreports.engine.design.JRDesignChartDataset;
import net.sf.jasperreports.engine.design.JRVerifier;



/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id$
 */
public class JRDesignPieDataset extends JRDesignChartDataset implements JRPieDataset
{


	/**
	 *
	 */
	private static final long serialVersionUID = JRConstants.SERIAL_VERSION_UID;
	
	public static final String PROPERTY_MIN_PERCENTAGE = "minPercentage";
	public static final String PROPERTY_MAX_COUNT = "maxCount";
	public static final String PROPERTY_KEY_EXPRESSION = "keyExpression";
	public static final String PROPERTY_VALUE_EXPRESSION = "valueExpression";
	public static final String PROPERTY_LABEL_EXPRESSION = "labelExpression";
	public static final String PROPERTY_SECTION_HYPERLINK = "sectionHyperlink";
	public static final String PROPERTY_OTHER_KEY_EXPRESSION = "otherKeyExpression";
	public static final String PROPERTY_OTHER_LABEL_EXPRESSION = "otherLabelExpression";
	public static final String PROPERTY_OTHER_SECTION_HYPERLINK = "otherSectionHyperlink";

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
	public JRDesignPieDataset(JRChartDataset dataset)
	{
		super(dataset);
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
	public void setKeyExpression(JRExpression keyExpression)
	{
		Object old = this.keyExpression;
		this.keyExpression = keyExpression;
		getEventSupport().firePropertyChange(PROPERTY_KEY_EXPRESSION, old, this.keyExpression);
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
	public void setValueExpression(JRExpression valueExpression)
	{
		Object old = this.valueExpression;
		this.valueExpression = valueExpression;
		getEventSupport().firePropertyChange(PROPERTY_VALUE_EXPRESSION, old, this.valueExpression);
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
	public void setLabelExpression(JRExpression labelExpression)
	{
		Object old = this.labelExpression;
		this.labelExpression = labelExpression;
		getEventSupport().firePropertyChange(PROPERTY_LABEL_EXPRESSION, old, this.labelExpression);
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
	public void setOtherKeyExpression(JRExpression otherKeyExpression)
	{
		Object old = this.otherKeyExpression;
		this.otherKeyExpression = otherKeyExpression;
		getEventSupport().firePropertyChange(PROPERTY_OTHER_KEY_EXPRESSION, old, this.otherKeyExpression);
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
	public void setOtherLabelExpression(JRExpression otherLabelExpression)
	{
		Object old = this.otherLabelExpression;
		this.otherLabelExpression = otherLabelExpression;
		getEventSupport().firePropertyChange(PROPERTY_OTHER_LABEL_EXPRESSION, old, this.otherLabelExpression);
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


	/**
	 * Sets the hyperlink specification for chart sections.
	 * 
	 * @param sectionHyperlink the hyperlink specification
	 * @see #getSectionHyperlink()
	 */
	public void setSectionHyperlink(JRHyperlink sectionHyperlink)
	{
		Object old = this.sectionHyperlink;
		this.sectionHyperlink = sectionHyperlink;
		getEventSupport().firePropertyChange(PROPERTY_SECTION_HYPERLINK, old, this.sectionHyperlink);
	}
	
	
	public JRHyperlink getOtherSectionHyperlink()
	{
		return otherSectionHyperlink;
	}


	/**
	 * 
	 */
	public void setOtherSectionHyperlink(JRHyperlink otherSectionHyperlink)
	{
		Object old = this.otherSectionHyperlink;
		this.otherSectionHyperlink = otherSectionHyperlink;
		getEventSupport().firePropertyChange(PROPERTY_OTHER_SECTION_HYPERLINK, old, this.otherSectionHyperlink);
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
		JRDesignPieDataset clone = (JRDesignPieDataset)super.clone();
		
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
}
