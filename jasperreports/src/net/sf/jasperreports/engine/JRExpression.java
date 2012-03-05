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
package net.sf.jasperreports.engine;



/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id$
 */
public interface JRExpression extends JRCloneable
{


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
	 * @deprecated To be removed.
	 */
	public Class<?> getValueClass();
	
	/**
	 * Returns the expression return value class.
	 * @deprecated To be removed.
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
