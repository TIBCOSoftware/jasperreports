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

import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.List;

import net.sf.jasperreports.charts.JRPieDataset;
import net.sf.jasperreports.charts.JRPieSeries;
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
	public static final String PROPERTY_PIE_SERIES = "pieSeries";
	public static final String PROPERTY_KEY_EXPRESSION = "keyExpression";
	public static final String PROPERTY_VALUE_EXPRESSION = "valueExpression";
	public static final String PROPERTY_LABEL_EXPRESSION = "labelExpression";
	public static final String PROPERTY_SECTION_HYPERLINK = "sectionHyperlink";
	public static final String PROPERTY_OTHER_KEY_EXPRESSION = "otherKeyExpression";
	public static final String PROPERTY_OTHER_LABEL_EXPRESSION = "otherLabelExpression";
	public static final String PROPERTY_OTHER_SECTION_HYPERLINK = "otherSectionHyperlink";

	private Float minPercentage = null;
	private Integer maxCount = null;
	
	private List pieSeriesList = new ArrayList();

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
	public JRPieSeries[] getSeries()
	{
		JRPieSeries[] pieSeriesArray = new JRPieSeries[pieSeriesList.size()];
		
		pieSeriesList.toArray(pieSeriesArray);

		return pieSeriesArray;
	}
	

	/**
	 * 
	 */
	public List getSeriesList()
	{
		return pieSeriesList;
	}

	
	/**
	 *
	 */
	public void addPieSeries(JRPieSeries pieSeries)
	{
		pieSeriesList.add(pieSeries);
		getEventSupport().fireCollectionElementAddedEvent(PROPERTY_PIE_SERIES, 
				pieSeries, pieSeriesList.size() - 1);
	}
	

	/**
	 *
	 */
	public JRPieSeries removePieSeries(JRPieSeries pieSeries)
	{
		if (pieSeries != null)
		{
			int idx = pieSeriesList.indexOf(pieSeries);
			if (idx >= 0)
			{
				pieSeriesList.remove(idx);
				getEventSupport().fireCollectionElementRemovedEvent(PROPERTY_PIE_SERIES, 
						pieSeries, idx);
			}
		}
		
		return pieSeries;
	}


	/**
	 * @deprecated Replaced by {@link #getSeries()}.
	 */
	public JRExpression getKeyExpression()
	{
		return pieSeriesList.size() > 0 ? ((JRPieSeries)pieSeriesList.get(0)).getKeyExpression() : null;
	}
		
	/**
	 * @deprecated Replaced by {@link #getSeries()}.
	 */
	public void setKeyExpression(JRExpression keyExpression)
	{
		if (pieSeriesList.size() == 0)
		{
			addPieSeries(new JRDesignPieSeries());
		}
		((JRDesignPieSeries)pieSeriesList.get(0)).setKeyExpression(keyExpression);
	}

	/**
	 * @deprecated Replaced by {@link #getSeries()}.
	 */
	public JRExpression getValueExpression()
	{
		return pieSeriesList.size() > 0 ? ((JRPieSeries)pieSeriesList.get(0)).getValueExpression() : null;
	}
		
	/**
	 * @deprecated Replaced by {@link #getSeries()}.
	 */
	public void setValueExpression(JRExpression valueExpression)
	{
		if (pieSeriesList.size() == 0)
		{
			addPieSeries(new JRDesignPieSeries());
		}
		((JRDesignPieSeries)pieSeriesList.get(0)).setValueExpression(valueExpression);
	}

	/**
	 * @deprecated Replaced by {@link #getSeries()}.
	 */
	public JRExpression getLabelExpression()
	{
		return pieSeriesList.size() > 0 ? ((JRPieSeries)pieSeriesList.get(0)).getLabelExpression() : null;
	}
		
	/**
	 * @deprecated Replaced by {@link #getSeries()}.
	 */
	public void setLabelExpression(JRExpression labelExpression)
	{
		if (pieSeriesList.size() == 0)
		{
			addPieSeries(new JRDesignPieSeries());
		}
		((JRDesignPieSeries)pieSeriesList.get(0)).setLabelExpression(labelExpression);
	}

	/**
	 * @deprecated Replaced by {@link #getSeries()}.
	 */
	public JRHyperlink getSectionHyperlink()
	{
		return pieSeriesList.size() > 0 ? ((JRPieSeries)pieSeriesList.get(0)).getSectionHyperlink() : null;
	}


	/**
	 * @deprecated Replaced by {@link #getSeries()}.
	 */
	public void setSectionHyperlink(JRHyperlink sectionHyperlink)
	{
		if (pieSeriesList.size() == 0)
		{
			addPieSeries(new JRDesignPieSeries());
		}
		((JRDesignPieSeries)pieSeriesList.get(0)).setSectionHyperlink(sectionHyperlink);
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
		
		if (pieSeriesList != null)
		{
			clone.pieSeriesList = new ArrayList(pieSeriesList.size());
			for(int i = 0; i < pieSeriesList.size(); i++)
			{
				clone.pieSeriesList.add(((JRPieSeries)pieSeriesList.get(i)).clone());
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
		
		if (pieSeriesList == null)
		{
			pieSeriesList = new ArrayList();
			
			JRDesignPieSeries ps = new JRDesignPieSeries();
			ps.setKeyExpression(keyExpression);
			ps.setValueExpression(valueExpression);
			ps.setLabelExpression(labelExpression);
			ps.setSectionHyperlink(sectionHyperlink);
			pieSeriesList.add(ps);

			keyExpression = null;
			valueExpression = null;
			labelExpression = null;
			sectionHyperlink = null;
		}
	}

}
