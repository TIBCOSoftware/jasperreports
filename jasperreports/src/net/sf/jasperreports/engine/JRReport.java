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
public interface JRReport extends JRDefaultFontProvider
{

	
	/**
	 *
	 */
	public static final byte PRINT_ORDER_VERTICAL = 1;
	public static final byte PRINT_ORDER_HORIZONTAL = 2;

	/**
	 *
	 */
	public static final byte ORIENTATION_PORTRAIT = 1;
	public static final byte ORIENTATION_LANDSCAPE = 2;

	/**
	 *
	 */
	public static final byte WHEN_NO_DATA_TYPE_NO_PAGES = 1;
	public static final byte WHEN_NO_DATA_TYPE_BLANK_PAGE = 2;
	public static final byte WHEN_NO_DATA_TYPE_ALL_SECTIONS_NO_DETAIL = 3;

	
	/**
	 *
	 */
	public String getName();

	/**
	 *
	 */
	public int getColumnCount();
		
	/**
	 *
	 */
	public byte getPrintOrder();
		
	/**
	 *
	 */
	public int getPageWidth();
		
	/**
	 *
	 */
	public int getPageHeight();
		
	/**
	 *
	 */
	public byte getOrientation();
		
	/**
	 *
	 */
	public byte getWhenNoDataType();
		
	/**
	 *
	 */
	public void setWhenNoDataType(byte whenNoDataType);
		
	/**
	 *
	 */
	public int getColumnWidth();
		
	/**
	 *
	 */
	public int getColumnSpacing();
		
	/**
	 *
	 */
	public int getLeftMargin();
		
	/**
	 *
	 */
	public int getRightMargin();
		
	/**
	 *
	 */
	public int getTopMargin();
		
	/**
	 *
	 */
	public int getBottomMargin();
		
	/**
	 *
	 */
	public boolean isTitleNewPage();
		
	/**
	 *
	 */
	public boolean isSummaryNewPage();
		
	/**
	 *
	 */
	public boolean isFloatColumnFooter();
		
	/**
	 *
	 */
	public String getScriptletClass();

	/**
	 *
	 */
	public String getResourceBundle();

	/**
	 *
	 */
	public String[] getPropertyNames();

	/**
	 *
	 */
	public String getProperty(String name);

	/**
	 *
	 */
	public void setProperty(String name, String value);

	/**
	 *
	 */
	public void removeProperty(String name);

	/**
	 *
	 */
	public String[] getImports();

	/**
	 *
	 */
	public JRReportFont[] getFonts();

	/**
	 *
	 */
	public JRParameter[] getParameters();

	/**
	 *
	 */
	public JRQuery getQuery();

	/**
	 *
	 */
	public JRField[] getFields();

	/**
	 *
	 */
	public JRVariable[] getVariables();

	/**
	 *
	 */
	public JRGroup[] getGroups();

	/**
	 *
	 */
	public JRBand getBackground();

	/**
	 *
	 */
	public JRBand getTitle();

	/**
	 *
	 */
	public JRBand getPageHeader();

	/**
	 *
	 */
	public JRBand getColumnHeader();

	/**
	 *
	 */
	public JRBand getDetail();

	/**
	 *
	 */
	public JRBand getColumnFooter();

	/**
	 *
	 */
	public JRBand getPageFooter();

	/**
	 *
	 */
	public JRBand getLastPageFooter();

	/**
	 *
	 */
	public JRBand getSummary();


}
