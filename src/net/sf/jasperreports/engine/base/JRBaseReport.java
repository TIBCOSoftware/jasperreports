/*
 * ============================================================================
 *                   The JasperReports License, Version 1.0
 * ============================================================================
 * 
 * Copyright (C) 2001-2004 Teodor Danciu (teodord@users.sourceforge.net). All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without modification,
 * are permitted provided that the following conditions are met:
 * 
 * 1. Redistributions of source code must retain the above copyright notice,
 *    this list of conditions and the following disclaimer.
 * 
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 * 
 * 3. The end-user documentation included with the redistribution, if any, must
 *    include the following acknowledgment: "This product includes software
 *    developed by Teodor Danciu (http://jasperreports.sourceforge.net)."
 *    Alternately, this acknowledgment may appear in the software itself, if
 *    and wherever such third-party acknowledgments normally appear.
 * 
 * 4. The name "JasperReports" must not be used to endorse or promote products 
 *    derived from this software without prior written permission. For written 
 *    permission, please contact teodord@users.sourceforge.net.
 * 
 * 5. Products derived from this software may not be called "JasperReports", nor 
 *    may "JasperReports" appear in their name, without prior written permission
 *    of Teodor Danciu.
 * 
 * THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED WARRANTIES,
 * INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
 * FITNESS  FOR A PARTICULAR  PURPOSE ARE  DISCLAIMED.  IN NO  EVENT SHALL  THE
 * APACHE SOFTWARE  FOUNDATION  OR ITS CONTRIBUTORS  BE LIABLE FOR  ANY DIRECT,
 * INDIRECT, INCIDENTAL, SPECIAL,  EXEMPLARY, OR CONSEQUENTIAL  DAMAGES (INCLU-
 * DING, BUT NOT LIMITED TO, PROCUREMENT  OF SUBSTITUTE GOODS OR SERVICES; LOSS
 * OF USE, DATA, OR  PROFITS; OR BUSINESS  INTERRUPTION)  HOWEVER CAUSED AND ON
 * ANY  THEORY OF LIABILITY,  WHETHER  IN CONTRACT,  STRICT LIABILITY,  OR TORT
 * (INCLUDING  NEGLIGENCE OR  OTHERWISE) ARISING IN  ANY WAY OUT OF THE  USE OF
 * THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

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
package net.sf.jasperreports.engine.base;

import java.io.Serializable;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import net.sf.jasperreports.engine.JRBand;
import net.sf.jasperreports.engine.JRField;
import net.sf.jasperreports.engine.JRGroup;
import net.sf.jasperreports.engine.JRParameter;
import net.sf.jasperreports.engine.JRQuery;
import net.sf.jasperreports.engine.JRReport;
import net.sf.jasperreports.engine.JRReportFont;
import net.sf.jasperreports.engine.JRVariable;


/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id$
 */
public class JRBaseReport implements JRReport, Serializable
{

	
	/**
	 *
	 */
	private static final long serialVersionUID = 604;

	/**
	 *
	 */
	protected String name = null;
	protected int columnCount = 1;
	protected byte printOrder = PRINT_ORDER_VERTICAL;
	protected int pageWidth = 595;
	protected int pageHeight = 842;
	protected byte orientation = ORIENTATION_PORTRAIT;
	protected byte whenNoDataType = WHEN_NO_DATA_TYPE_NO_PAGES;
	protected int columnWidth = 555;
	protected int columnSpacing = 0;
	protected int leftMargin = 20;
	protected int rightMargin = 20;
	protected int topMargin = 30;
	protected int bottomMargin = 30;
	protected boolean isTitleNewPage = false;
	protected boolean isSummaryNewPage = false;
	protected boolean isFloatColumnFooter = false;
	protected String scriptletClass = null;
	protected String resourceBundle = null;

	/**
	 *
	 */
	protected Map propertiesMap = null;
	protected Set importsSet = null;
	protected JRReportFont defaultFont = null;
	protected JRReportFont[] fonts = null;
	protected JRParameter[] parameters = null;
	protected JRQuery query = null;
	protected JRField[] fields = null;
	protected JRVariable[] variables = null;
	protected JRGroup[] groups = null;
	protected JRBand background = null;
	protected JRBand title = null;
	protected JRBand pageHeader = null;
	protected JRBand columnHeader = null;
	protected JRBand detail = null;
	protected JRBand columnFooter = null;
	protected JRBand pageFooter = null;
	protected JRBand lastPageFooter = null;
	protected JRBand summary = null;

	
	/**
	 *
	 */
	public JRBaseReport()
	{
	}
	
	/**
	 *
	 */
	public JRBaseReport(JRReport report)
	{
		/*   */
		name = report.getName();
		columnCount = report.getColumnCount();
		printOrder = report.getPrintOrder();
		pageWidth = report.getPageWidth();
		pageHeight = report.getPageHeight();
		orientation = report.getOrientation();
		whenNoDataType = report.getWhenNoDataType();
		columnWidth = report.getColumnWidth();
		columnSpacing = report.getColumnSpacing();
		leftMargin = report.getLeftMargin();
		rightMargin = report.getRightMargin();
		topMargin = report.getTopMargin();
		bottomMargin = report.getBottomMargin();
		isTitleNewPage = report.isTitleNewPage();
		isSummaryNewPage = report.isSummaryNewPage();
		isFloatColumnFooter = report.isFloatColumnFooter();
		scriptletClass = report.getScriptletClass();
		resourceBundle = report.getResourceBundle();

		/*   */
		String[] propertyNames = report.getPropertyNames();
		if (propertyNames != null && propertyNames.length > 0)
		{
			for(int i = 0; i < propertyNames.length; i++)
			{
				setProperty(propertyNames[i], report.getProperty(propertyNames[i]));
			}
		}

		/*   */
		String[] imports = report.getImports();
		if (imports != null && imports.length > 0)
		{
			importsSet = new HashSet(imports.length);
			importsSet.addAll(Arrays.asList(imports));
		}

		/*   */
		JRBaseObjectFactory factory = new JRBaseObjectFactory(this);
		
		/*   */
		defaultFont = factory.getReportFont(report.getDefaultFont());

		/*   */
		JRReportFont[] jrFonts = report.getFonts();
		if (jrFonts != null && jrFonts.length > 0)
		{
			fonts = new JRReportFont[jrFonts.length];
			for(int i = 0; i < fonts.length; i++)
			{
				fonts[i] = factory.getReportFont(jrFonts[i]);
			}
		}

		/*   */
		JRParameter[] jrParameters = report.getParameters();
		if (jrParameters != null && jrParameters.length > 0)
		{
			parameters = new JRParameter[jrParameters.length];
			for(int i = 0; i < parameters.length; i++)
			{
				parameters[i] = factory.getParameter(jrParameters[i]);
			}
		}

		/*   */
		query = factory.getQuery(report.getQuery());
		
		/*   */
		JRField[] jrFields = report.getFields();
		if (jrFields != null && jrFields.length > 0)
		{
			fields = new JRField[jrFields.length];
			for(int i = 0; i < fields.length; i++)
			{
				fields[i] = factory.getField(jrFields[i]);
			}
		}

		/*   */
		JRVariable[] jrVariables = report.getVariables();
		if (jrVariables != null && jrVariables.length > 0)
		{
			variables = new JRVariable[jrVariables.length];
			for(int i = 0; i < variables.length; i++)
			{
				variables[i] = factory.getVariable(jrVariables[i]);
			}
		}

		/*   */
		JRGroup[] jrGroups = report.getGroups();
		if (jrGroups != null && jrGroups.length > 0)
		{
			groups = new JRGroup[jrGroups.length];
			for(int i = 0; i < groups.length; i++)
			{
				groups[i] = factory.getGroup(jrGroups[i]);
			}
		}

		/*   */
		background = factory.getBand(report.getBackground());
		title = factory.getBand(report.getTitle());
		pageHeader = factory.getBand(report.getPageHeader());
		columnHeader = factory.getBand(report.getColumnHeader());
		detail = factory.getBand(report.getDetail());
		columnFooter = factory.getBand(report.getColumnFooter());
		pageFooter = factory.getBand(report.getPageFooter());
		lastPageFooter = factory.getBand(report.getLastPageFooter());
		summary = factory.getBand(report.getSummary());
	}


	/**
	 *
	 */
	public String getName()
	{
		return name;
	}

	/**
	 *
	 */
	public int getColumnCount()
	{
		return columnCount;
	}
		
	/**
	 *
	 */
	public byte getPrintOrder()
	{
		return printOrder;
	}
		
	/**
	 *
	 */
	public int getPageWidth()
	{
		return pageWidth;
	}
		
	/**
	 *
	 */
	public int getPageHeight()
	{
		return pageHeight;
	}
		
	/**
	 *
	 */
	public byte getOrientation()
	{
		return orientation;
	}
		
	/**
	 *
	 */
	public byte getWhenNoDataType()
	{
		return whenNoDataType;
	}
		
	/**
	 *
	 */
	public void setWhenNoDataType(byte whenNoDataType)
	{
		this.whenNoDataType = whenNoDataType;
	}

	/**
	 *
	 */
	public int getColumnWidth()
	{
		return columnWidth;
	}
		
	/**
	 *
	 */
	public int getColumnSpacing()
	{
		return columnSpacing;
	}
		
	/**
	 *
	 */
	public int getLeftMargin()
	{
		return leftMargin;
	}
		
	/**
	 *
	 */
	public int getRightMargin()
	{
		return rightMargin;
	}
		
	/**
	 *
	 */
	public int getTopMargin()
	{
		return topMargin;
	}
		
	/**
	 *
	 */
	public int getBottomMargin()
	{
		return bottomMargin;
	}
		
	/**
	 *
	 */
	public boolean isTitleNewPage()
	{
		return isTitleNewPage;
	}
		
	/**
	 *
	 */
	public boolean isSummaryNewPage()
	{
		return isSummaryNewPage;
	}
		
	/**
	 *
	 */
	public boolean isFloatColumnFooter()
	{
		return isFloatColumnFooter;
	}
		
	/**
	 *
	 */
	public String getScriptletClass()
	{
		return scriptletClass;
	}

	/**
	 *
	 */
	public String getResourceBundle()
	{
		return resourceBundle;
	}

	/**
	 *
	 */
	public String[] getPropertyNames()
	{
		if (propertiesMap != null)
		{
			Set names = propertiesMap.keySet(); 
			return (String[])names.toArray(new String[names.size()]);
		}
		else
		{
			return null;
		}
	}

	/**
	 *
	 */
	public String getProperty(String name)
	{
		if (propertiesMap != null)
		{
			return (String)propertiesMap.get(name);
		}
		else
		{
			return null;
		}
	}

	/**
	 *
	 */
	public void setProperty(String name, String value)
	{
		if (propertiesMap == null)
		{
			propertiesMap = new HashMap();
		}
		
		propertiesMap.put(name, value);
	}

	/**
	 *
	 */
	public void removeProperty(String name)
	{
		if (propertiesMap != null)
		{
			propertiesMap.remove(name);
		}
	}

	/**
	 *
	 */
	public String[] getImports()
	{
		if (importsSet != null)
		{
			return (String[])importsSet.toArray(new String[importsSet.size()]);
		}
		else
		{
			return null;
		}
	}

	/**
	 *
	 */
	public JRReportFont getDefaultFont()
	{
		return defaultFont;
	}

	/**
	 *
	 */
	public JRReportFont[] getFonts()
	{
		return fonts;
	}

	/**
	 *
	 */
	public JRParameter[] getParameters()
	{
		return parameters;
	}

	/**
	 *
	 */
	public JRQuery getQuery()
	{
		return query;
	}

	/**
	 *
	 */
	public JRField[] getFields()
	{
		return fields;
	}

	/**
	 *
	 */
	public JRVariable[] getVariables()
	{
		return variables;
	}

	/**
	 *
	 */
	public JRGroup[] getGroups()
	{
		return groups;
	}

	/**
	 *
	 */
	public JRBand getBackground()
	{
		return background;
	}

	/**
	 *
	 */
	public JRBand getTitle()
	{
		return title;
	}

	/**
	 *
	 */
	public JRBand getPageHeader()
	{
		return pageHeader;
	}

	/**
	 *
	 */
	public JRBand getColumnHeader()
	{
		return columnHeader;
	}

	/**
	 *
	 */
	public JRBand getDetail()
	{
		return detail;
	}

	/**
	 *
	 */
	public JRBand getColumnFooter()
	{
		return columnFooter;
	}

	/**
	 *
	 */
	public JRBand getPageFooter()
	{
		return pageFooter;
	}

	/**
	 *
	 */
	public JRBand getLastPageFooter()
	{
		return lastPageFooter;
	}

	/**
	 *
	 */
	public JRBand getSummary()
	{
		return summary;
	}


}
