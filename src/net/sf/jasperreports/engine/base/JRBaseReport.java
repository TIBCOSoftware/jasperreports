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
package dori.jasper.engine.base;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import dori.jasper.engine.JRBand;
import dori.jasper.engine.JRField;
import dori.jasper.engine.JRGroup;
import dori.jasper.engine.JRParameter;
import dori.jasper.engine.JRQuery;
import dori.jasper.engine.JRReport;
import dori.jasper.engine.JRReportFont;
import dori.jasper.engine.JRVariable;


/**
 *
 */
public class JRBaseReport implements JRReport, Serializable
{

	
	/**
	 *
	 */
	private static final long serialVersionUID = 501;

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
	protected String scriptletClass = null;

	/**
	 *
	 */
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
		scriptletClass = report.getScriptletClass();

		/*   */
		Map baseObjectsMap = new HashMap();
		
		/*   */
		defaultFont = JRBaseObjectFactory.getReportFont(report.getDefaultFont(), baseObjectsMap);

		/*   */
		JRReportFont[] jrFonts = report.getFonts();
		if (jrFonts != null && jrFonts.length > 0)
		{
			fonts = new JRReportFont[jrFonts.length];
			for(int i = 0; i < fonts.length; i++)
			{
				fonts[i] = JRBaseObjectFactory.getReportFont(jrFonts[i], baseObjectsMap);
			}
		}

		/*   */
		JRParameter[] jrParameters = report.getParameters();
		if (jrParameters != null && jrParameters.length > 0)
		{
			parameters = new JRParameter[jrParameters.length];
			for(int i = 0; i < parameters.length; i++)
			{
				parameters[i] = JRBaseObjectFactory.getParameter(jrParameters[i], baseObjectsMap);
			}
		}

		/*   */
		query = JRBaseObjectFactory.getQuery(report.getQuery(), baseObjectsMap);
		
		/*   */
		JRField[] jrFields = report.getFields();
		if (jrFields != null && jrFields.length > 0)
		{
			fields = new JRField[jrFields.length];
			for(int i = 0; i < fields.length; i++)
			{
				fields[i] = JRBaseObjectFactory.getField(jrFields[i], baseObjectsMap);
			}
		}

		/*   */
		JRVariable[] jrVariables = report.getVariables();
		if (jrVariables != null && jrVariables.length > 0)
		{
			variables = new JRVariable[jrVariables.length];
			for(int i = 0; i < variables.length; i++)
			{
				variables[i] = JRBaseObjectFactory.getVariable(jrVariables[i], baseObjectsMap);
			}
		}

		/*   */
		JRGroup[] jrGroups = report.getGroups();
		if (jrGroups != null && jrGroups.length > 0)
		{
			groups = new JRGroup[jrGroups.length];
			for(int i = 0; i < groups.length; i++)
			{
				groups[i] = JRBaseObjectFactory.getGroup(jrGroups[i], baseObjectsMap);
			}
		}

		/*   */
		background = JRBaseObjectFactory.getBand(report.getBackground(), baseObjectsMap);
		title = JRBaseObjectFactory.getBand(report.getTitle(), baseObjectsMap);
		pageHeader = JRBaseObjectFactory.getBand(report.getPageHeader(), baseObjectsMap);
		columnHeader = JRBaseObjectFactory.getBand(report.getColumnHeader(), baseObjectsMap);
		detail = JRBaseObjectFactory.getBand(report.getDetail(), baseObjectsMap);
		columnFooter = JRBaseObjectFactory.getBand(report.getColumnFooter(), baseObjectsMap);
		pageFooter = JRBaseObjectFactory.getBand(report.getPageFooter(), baseObjectsMap);
		summary = JRBaseObjectFactory.getBand(report.getSummary(), baseObjectsMap);
	}


	/**
	 *
	 */
	public String getName()
	{
		return this.name;
	}

	/**
	 *
	 */
	public int getColumnCount()
	{
		return this.columnCount;
	}
		
	/**
	 *
	 */
	public byte getPrintOrder()
	{
		return this.printOrder;
	}
		
	/**
	 *
	 */
	public int getPageWidth()
	{
		return this.pageWidth;
	}
		
	/**
	 *
	 */
	public int getPageHeight()
	{
		return this.pageHeight;
	}
		
	/**
	 *
	 */
	public byte getOrientation()
	{
		return this.orientation;
	}
		
	/**
	 *
	 */
	public byte getWhenNoDataType()
	{
		return this.whenNoDataType;
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
		return this.columnWidth;
	}
		
	/**
	 *
	 */
	public int getColumnSpacing()
	{
		return this.columnSpacing;
	}
		
	/**
	 *
	 */
	public int getLeftMargin()
	{
		return this.leftMargin;
	}
		
	/**
	 *
	 */
	public int getRightMargin()
	{
		return this.rightMargin;
	}
		
	/**
	 *
	 */
	public int getTopMargin()
	{
		return this.topMargin;
	}
		
	/**
	 *
	 */
	public int getBottomMargin()
	{
		return this.bottomMargin;
	}
		
	/**
	 *
	 */
	public boolean isTitleNewPage()
	{
		return this.isTitleNewPage;
	}
		
	/**
	 *
	 */
	public boolean isSummaryNewPage()
	{
		return this.isSummaryNewPage;
	}
		
	/**
	 *
	 */
	public String getScriptletClass()
	{
		return this.scriptletClass;
	}

	/**
	 *
	 */
	public JRReportFont getDefaultFont()
	{
		return this.defaultFont;
	}

	/**
	 *
	 */
	public JRReportFont[] getFonts()
	{
		return this.fonts;
	}

	/**
	 *
	 */
	public JRParameter[] getParameters()
	{
		return this.parameters;
	}

	/**
	 *
	 */
	public JRQuery getQuery()
	{
		return this.query;
	}

	/**
	 *
	 */
	public JRField[] getFields()
	{
		return this.fields;
	}

	/**
	 *
	 */
	public JRVariable[] getVariables()
	{
		return this.variables;
	}

	/**
	 *
	 */
	public JRGroup[] getGroups()
	{
		return this.groups;
	}

	/**
	 *
	 */
	public JRBand getBackground()
	{
		return this.background;
	}

	/**
	 *
	 */
	public JRBand getTitle()
	{
		return this.title;
	}

	/**
	 *
	 */
	public JRBand getPageHeader()
	{
		return this.pageHeader;
	}

	/**
	 *
	 */
	public JRBand getColumnHeader()
	{
		return this.columnHeader;
	}

	/**
	 *
	 */
	public JRBand getDetail()
	{
		return this.detail;
	}

	/**
	 *
	 */
	public JRBand getColumnFooter()
	{
		return this.columnFooter;
	}

	/**
	 *
	 */
	public JRBand getPageFooter()
	{
		return this.pageFooter;
	}

	/**
	 *
	 */
	public JRBand getSummary()
	{
		return this.summary;
	}


}
