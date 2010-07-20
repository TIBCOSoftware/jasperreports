/*
 * JasperReports - Free Java Reporting Library.
 * Copyright (C) 2001 - 2009 Jaspersoft Corporation. All rights reserved.
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
package net.sf.jasperreports.engine;

import net.sf.jasperreports.engine.type.EvaluationTimeEnum;


/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id$
 */
public interface JRExpression extends JRCloneable
{


	/**
	 * A constant specifying that an expression should be evaluated at the exact moment in the filling process
	 * when it is encountered.
	 * @deprecated Replaced by {@link EvaluationTimeEnum#NOW}.
	 */
	public static final byte EVALUATION_TIME_NOW = 1;


	/**
	 * A constant specifying that an expression should be evaluated at the end of the filling process.
	 * @deprecated Replaced by {@link EvaluationTimeEnum#REPORT}.
	 */
	public static final byte EVALUATION_TIME_REPORT = 2;


	/**
	 * A constant specifying that an expression should be evaluated after each page is filled.
	 * @deprecated Replaced by {@link EvaluationTimeEnum#PAGE}.
	 */
	public static final byte EVALUATION_TIME_PAGE = 3;


	/**
	 * A constant specifying that an expression should be evaluated after each column is filled.
	 * @deprecated Replaced by {@link EvaluationTimeEnum#COLUMN}.
	 */
	public static final byte EVALUATION_TIME_COLUMN = 4;


	/**
	 * A constant specifying that an expression should be evaluated after each group break.
	 * @deprecated Replaced by {@link EvaluationTimeEnum#GROUP}.
	 */
	public static final byte EVALUATION_TIME_GROUP = 5;


	/**
	 * The element will be evaluated at band end.
	 * @deprecated Replaced by {@link EvaluationTimeEnum#BAND}.
	 */
	public static final byte EVALUATION_TIME_BAND = 6;

	
	/**
	 * Evaluation time indicating that each variable participating in the expression
	 * should be evaluated at a time decided by the engine.
	 * <p/>
	 * Variables will be evaluated at a time corresponding to their reset type.
	 * Fields are evaluated "now", i.e. at the time the band the element lies on gets filled.
	 * <p/>
	 * This evaluation type should be used when report elements having expressions that combine 
	 * values evaluated at different times are required (e.g. percentage out of a total).
	 * <p/>
	 * NB: avoid using this evaluation type when other types suffice as it can lead
	 * to performance loss.
	 * @deprecated Replaced by {@link EvaluationTimeEnum#AUTO}.
	 */
	public static final byte EVALUATION_TIME_AUTO = 7;

	/**
	 *
	 */
	public static final byte EVALUATION_OLD = 1;
	public static final byte EVALUATION_ESTIMATED = 2;
	public static final byte EVALUATION_DEFAULT = 3;

	/**
	 * Dummy ID that is assigned to expression that are not used (and not collected).
	 */
	public static final Integer NOT_USED_ID = Integer.valueOf(-1);

	/**
	 * Returns the expression return value class.
	 */
	public Class getValueClass();
	
	/**
	 * Returns the expression return value class.
	 */
	public String getValueClassName();
	
	/**
	 *
	 */
	public int getId();
			
	/**
	 *
	 */
	public JRExpressionChunk[] getChunks();

	/**
	 *
	 */
	public String getText();


}
