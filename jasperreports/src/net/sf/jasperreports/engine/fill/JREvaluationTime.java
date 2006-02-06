/*
 * ============================================================================
 * GNU Lesser General Public License
 * ============================================================================
 *
 * JasperReports - Free Java report-generating library.
 * Copyright (C) 2001-2005 JasperSoft Corporation http://www.jaspersoft.com
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
package net.sf.jasperreports.engine.fill;

import java.io.Serializable;

import net.sf.jasperreports.engine.JRExpression;
import net.sf.jasperreports.engine.JRGroup;


/**
 * An evaluation time during the report fill process.
 * 
 * @author Lucian Chirita (lucianc@users.sourceforge.net)
 * @version $Id$
 */
public class JREvaluationTime implements Serializable
{
	private static final long serialVersionUID = 1L;
	
	/**
	 * Evaluation time corresponding to {@link JRExpression#EVALUATION_TIME_REPORT JRExpression.EVALUATION_TIME_REPORT}.
	 */
	public static final JREvaluationTime EVALUATION_TIME_REPORT = new JREvaluationTime(JRExpression.EVALUATION_TIME_REPORT, null, null);
	/**
	 * Evaluation time corresponding to {@link JRExpression#EVALUATION_TIME_PAGE JRExpression.EVALUATION_TIME_PAGE}.
	 */
	public static final JREvaluationTime EVALUATION_TIME_PAGE = new JREvaluationTime(JRExpression.EVALUATION_TIME_PAGE, null, null);
	/**
	 * Evaluation time corresponding to {@link JRExpression#EVALUATION_TIME_COLUMN JRExpression.EVALUATION_TIME_COLUMN}.
	 */
	public static final JREvaluationTime EVALUATION_TIME_COLUMN = new JREvaluationTime(JRExpression.EVALUATION_TIME_COLUMN, null, null);
	/**
	 * Evaluation time corresponding to {@link JRExpression#EVALUATION_TIME_NOW JRExpression.EVALUATION_TIME_NOW}.
	 */
	public static final JREvaluationTime EVALUATION_TIME_NOW = new JREvaluationTime(JRExpression.EVALUATION_TIME_NOW, null, null);
	
	
	/**
	 * Returns the evaluation time corresponding to
	 * {@link JRExpression#EVALUATION_TIME_GROUP JRExpression.EVALUATION_TIME_GROUP} for a specific group.
	 * 
	 * @param groupName the group name
	 * @return corresponding group evaluation time
	 */
	public static JREvaluationTime getGroupEvaluationTime(String groupName)
	{
		return new JREvaluationTime(JRExpression.EVALUATION_TIME_GROUP, groupName, null);
	}
	
	/**
	 * Returns the evaluation time corresponding to
	 * {@link JRExpression#EVALUATION_TIME_BAND JRExpression.EVALUATION_TIME_BAND} for a specific band.
	 * 
	 * @param band the band
	 * @return corresponding band evaluation time
	 */
	public static JREvaluationTime getBandEvaluationTime(JRFillBand band)
	{
		return new JREvaluationTime(JRExpression.EVALUATION_TIME_BAND, null, band);
	}
	
	
	/**
	 * Returns the evaluation time corresponding to an evaluation time type.
	 * 
	 * @param type the evaluation time type
	 * @param group the group used for {@link JRExpression#EVALUATION_TIME_GROUP JRExpression.EVALUATION_TIME_GROUP}
	 * 	evaluation time type
	 * @param band the band used for {@link JRExpression#EVALUATION_TIME_BAND JRExpression.EVALUATION_TIME_BAND}
	 * 	evaluation time type
	 * @return the evaluation time corresponding to an evaluation time type
	 */
	public static JREvaluationTime getEvaluationTime(byte type, JRGroup group, JRFillBand band)
	{
		JREvaluationTime evaluationTime;
		
		switch (type)
		{
			case JRExpression.EVALUATION_TIME_REPORT:
				evaluationTime = EVALUATION_TIME_REPORT;
				break;
			case JRExpression.EVALUATION_TIME_PAGE:
				evaluationTime = EVALUATION_TIME_PAGE;
				break;
			case JRExpression.EVALUATION_TIME_COLUMN:
				evaluationTime = EVALUATION_TIME_COLUMN;
				break;
			case JRExpression.EVALUATION_TIME_GROUP:
				evaluationTime = getGroupEvaluationTime(group.getName());
				break;
			case JRExpression.EVALUATION_TIME_BAND:
				evaluationTime = getBandEvaluationTime(band);
				break;
			default:
				evaluationTime = null;
				break;
		}
		
		return evaluationTime;
	}
	
	private final byte type;
	private final String groupName;
	private final int bandId;
	private final int hash;
	
	
	private JREvaluationTime(byte type, String groupName, JRFillBand band)
	{
		this.type = type;
		this.groupName = groupName;
		this.bandId = band == null ? 0 : band.getId();
		
		this.hash = computeHash();
	}


	private int computeHash()
	{
		int hashCode = type;
		hashCode = 31*hashCode + (groupName == null ? 0 : groupName.hashCode());
		hashCode = 31*hashCode + bandId;
		return hashCode;
	}


	public boolean equals(Object obj)
	{
		if (obj == this)
		{
			return true;
		}
		
		JREvaluationTime e = (JREvaluationTime) obj;
		
		boolean eq = e.type == type;
		
		if (eq)
		{
			switch (type)
			{
				case JRExpression.EVALUATION_TIME_GROUP:
					eq = groupName.equals(e.groupName);
					break;
				case JRExpression.EVALUATION_TIME_BAND:
					eq = bandId == e.bandId;
					break;
			}
		}
		
		return eq;
	}


	public int hashCode()
	{
		return hash;
	}
}
