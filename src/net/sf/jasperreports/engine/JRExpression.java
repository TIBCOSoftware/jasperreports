/*
 * ============================================================================
 *                   GNU Lesser General Public License
 * ============================================================================
 *
 * JasperReports - Free Java report-generating library.
 * Copyright (C) 2001-2004 Teodor Danciu teodord@users.sourceforge.net
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
public interface JRExpression
{


	/**
	 *
	 */
	public static final byte EVALUATION_TIME_NOW = 1;
	public static final byte EVALUATION_TIME_REPORT = 2;
	public static final byte EVALUATION_TIME_PAGE = 3;
	public static final byte EVALUATION_TIME_COLUMN = 4;
	public static final byte EVALUATION_TIME_GROUP = 5;


	/**
	 *
	 */
	public static final byte EVALUATION_OLD = 1;
	public static final byte EVALUATION_ESTIMATED = 2;
	public static final byte EVALUATION_DEFAULT = 3;


	/**
	 *
	 */
	public static final String PREFIX_variable = "variable_";
	public static final String PREFIX_variableInitialValue = "variableInitialValue_";
	public static final String PREFIX_connection = "connection_";
	public static final String PREFIX_anchorName = "anchorName_";
	public static final String PREFIX_dataSource = "dataSource_";
	public static final String PREFIX_image = "image_";
	public static final String PREFIX_parameterDefaultValue = "parameterDefaultValue_";
	public static final String PREFIX_group = "group_";
	public static final String PREFIX_hyperlinkAnchor = "hyperlinkAnchor_";
	public static final String PREFIX_hyperlinkPage = "hyperlinkPage_";
	public static final String PREFIX_hyperlinkReference = "hyperlinkReference_";
	public static final String PREFIX_parametersMap = "parametersMap_";
	public static final String PREFIX_printWhen = "printWhen_";
	public static final String PREFIX_subreport = "subreport_";
	public static final String PREFIX_subreportParameter = "subreportParameter_";
	public static final String PREFIX_textField = "textField_";


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
	public String getName();
	
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
