/*
 * ============================================================================
 *                   GNU Lesser General Public License
 * ============================================================================
 *
 * JasperReports - Free Java report-generating library.
 * Copyright (C) 2001-2005 Teodor Danciu teodord@users.sourceforge.net
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
 * Teodor Danciu
 * 173, Calea Calarasilor, Bl. 42, Sc. 1, Ap. 18
 * Postal code 030615, Sector 3
 * Bucharest, ROMANIA
 * Email: teodord@users.sourceforge.net
 */
package net.sf.jasperreports.engine;


/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id$
 */
public interface JRVariable
{


	/**
	 *
	 */
	public static final String REPORT_COUNT = "REPORT_COUNT";
	public static final String PAGE_COUNT = "PAGE_COUNT";
	public static final String COLUMN_COUNT = "COLUMN_COUNT";
	public static final String PAGE_NUMBER = "PAGE_NUMBER";
	public static final String COLUMN_NUMBER = "COLUMN_NUMBER";

	/**
	 *
	 */
	public static final byte RESET_TYPE_REPORT = 1;
	public static final byte RESET_TYPE_PAGE = 2;
	public static final byte RESET_TYPE_COLUMN = 3;
	public static final byte RESET_TYPE_GROUP = 4;
	public static final byte RESET_TYPE_NONE = 5;

	/**
	 *
	 */
	public static final byte CALCULATION_NOTHING = 0;
	public static final byte CALCULATION_COUNT = 1;
	public static final byte CALCULATION_SUM = 2;
	public static final byte CALCULATION_AVERAGE = 3;
	public static final byte CALCULATION_LOWEST = 4;
	public static final byte CALCULATION_HIGHEST = 5;
	public static final byte CALCULATION_STANDARD_DEVIATION = 6;
	public static final byte CALCULATION_VARIANCE = 7;
	public static final byte CALCULATION_SYSTEM = 8;


	/**
	 *
	 */
	public String getName();
		
	/**
	 *
	 */
	public Class getValueClass();
		
	/**
	 *
	 */
	public String getValueClassName();
		
	/**
	 *
	 */
	public Class getIncrementerFactoryClass();
		
	/**
	 *
	 */
	public String getIncrementerFactoryClassName();
		
	/**
	 *
	 */
	public byte getResetType();
		
	/**
	 *
	 */
	public byte getCalculation();

	/**
	 *
	 */
	public boolean isSystemDefined();

	/**
	 *
	 */
	public JRExpression getExpression();
		
	/**
	 *
	 */
	public JRExpression getInitialValueExpression();
		
	/**
	 *
	 */
	public JRGroup getResetGroup();
		
	/**
	 *
	 */
	public JRVariable getCountVariable();

	/**
	 *
	 */
	public JRVariable getSumVariable();

	/**
	 *
	 */
	public JRVariable getVarianceVariable();

		
}
