/*
 * ============================================================================
 * GNU Lesser General Public License
 * ============================================================================
 *
 * JasperReports - Free Java report-generating library.
 * Copyright (C) 2001-2009 JasperSoft Corporation http://www.jaspersoft.com
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
 * 539 Bryant Street, Suite 100
 * San Francisco, CA 94107
 * http://www.jaspersoft.com
 */

/*
 * Special thanks to Google 'Summer of Code 2005' program for supporting this development
 * 
 * Contributors:
 * Majid Ali Khan - majidkk@users.sourceforge.net
 * Frank Schönheit - Frank.Schoenheit@Sun.COM
 */
package net.sf.jasperreports.engine.export.ooxml;

import java.io.IOException;
import java.io.Writer;
import java.util.List;

import net.sf.jasperreports.engine.JRReport;
import net.sf.jasperreports.engine.JasperPrint;


/**
 * @author sanda zaharia (shertage@users.sourceforge.net)
 * @version $Id$
 */
public class StyleBuilder
{
	/**
	 * 
	 */
//	private List jasperPrintList;
	private Writer writer;
	private String xmlProlog;	
	/**
	 * 
	 */
	public StyleBuilder(Writer writer, String xmlProlog)
	{
//		this.jasperPrintList = jasperPrintList;
		this.writer = writer;
		this.xmlProlog = xmlProlog;
	}

	/**
	 * 
	 */
	public void build() throws IOException
	{
		buildDocDefaults();
		//buildLatentStyles();
		
//		for(int reportIndex = 0; reportIndex < jasperPrintList.size(); reportIndex++)
//		{
//			JasperPrint jasperPrint = (JasperPrint)jasperPrintList.get(reportIndex);
//			
//			buildPageLayout(reportIndex, jasperPrint);
//		}
//
//
//		for(int reportIndex = 0; reportIndex < jasperPrintList.size(); reportIndex++)
//		{
//			buildMasterPage(reportIndex);
//		}

//		buildStyles();
		
		writer.flush();
//		writer.close();
	}
	
	/**
	 * 
	 */
	private void buildDocDefaults() throws IOException
	{
		writer.write(xmlProlog);

		writer.write("<w:styles \r\n");
		writer.write(" xmlns:r=\"http://schemas.openxmlformats.org/officeDocument/2006/relationships\" \r\n");
		writer.write(" xmlns:w=\"http://schemas.openxmlformats.org/wordprocessingml/2006/main\"> \r\n");
		writer.write(" <w:docDefaults> \r\n");
		writer.write("  <w:rPrDefault> \r\n");
		writer.write("   <w:rPr> \r\n");
		writer.write("    <w:rFonts w:ascii=\"Times New Roman\" w:eastAsia=\"Times New Roman\" w:hAnsi=\"Times New Roman\" w:cs=\"Times New Roman\"/> \r\n");
		writer.write("   </w:rPr> \r\n");
		writer.write("  </w:rPrDefault> \r\n");
		writer.write("  <w:pPrDefault> \r\n");
		writer.write("  </w:pPrDefault> \r\n");
		writer.write(" </w:docDefaults> \r\n");
		writer.flush();
	}
	
	/**
	 * 
	 *
	private void buildLatentStyles() throws IOException
	{
		writer.write(" <w:latentStyles w:defLockedState=\"0\" w:defUIPriority=\"99\" w:defSemiHidden=\"1\" w:defUnhideWhenUsed=\"1\" w:defQFormat=\"0\" w:count=\"267\"> \r\n");
		writer.write("  <w:lsdException w:name=\"Normal\" w:semiHidden=\"0\" w:uiPriority=\"0\" w:unhideWhenUsed=\"0\" w:qFormat=\"1\"/> \r\n");
		writer.write("  <w:lsdException w:name=\"heading 1\" w:semiHidden=\"0\" w:uiPriority=\"9\" w:unhideWhenUsed=\"0\" w:qFormat=\"1\"/> \r\n");
		writer.write("  <w:lsdException w:name=\"heading 2\" w:uiPriority=\"9\" w:qFormat=\"1\"/> \r\n");
		writer.write("  <w:lsdException w:name=\"heading 3\" w:uiPriority=\"9\" w:qFormat=\"1\"/> \r\n");
		writer.write("  <w:lsdException w:name=\"heading 4\" w:uiPriority=\"9\" w:qFormat=\"1\"/> \r\n");
		writer.write("  <w:lsdException w:name=\"heading 5\" w:uiPriority=\"9\" w:qFormat=\"1\"/> \r\n");
		writer.write("  <w:lsdException w:name=\"heading 6\" w:uiPriority=\"9\" w:qFormat=\"1\"/> \r\n");
		writer.write("  <w:lsdException w:name=\"heading 7\" w:uiPriority=\"9\" w:qFormat=\"1\"/> \r\n");
		writer.write("  <w:lsdException w:name=\"heading 8\" w:uiPriority=\"9\" w:qFormat=\"1\"/> \r\n");
		writer.write("  <w:lsdException w:name=\"heading 9\" w:uiPriority=\"9\" w:qFormat=\"1\"/> \r\n");
		writer.write("  <w:lsdException w:name=\"toc 1\" w:uiPriority=\"39\"/> \r\n");
		writer.write("  <w:lsdException w:name=\"toc 2\" w:uiPriority=\"39\"/> \r\n");
		writer.write("  <w:lsdException w:name=\"toc 3\" w:uiPriority=\"39\"/> \r\n");
		writer.write("  <w:lsdException w:name=\"toc 4\" w:uiPriority=\"39\"/> \r\n");
		writer.write("  <w:lsdException w:name=\"toc 5\" w:uiPriority=\"39\"/> \r\n");
		writer.write("  <w:lsdException w:name=\"toc 6\" w:uiPriority=\"39\"/> \r\n");
		writer.write("  <w:lsdException w:name=\"toc 7\" w:uiPriority=\"39\"/> \r\n");
		writer.write("  <w:lsdException w:name=\"toc 8\" w:uiPriority=\"39\"/> \r\n");
		writer.write("  <w:lsdException w:name=\"toc 9\" w:uiPriority=\"39\"/> \r\n");
		writer.write("  <w:lsdException w:name=\"caption\" w:uiPriority=\"35\" w:qFormat=\"1\"/> \r\n");
		writer.write("  <w:lsdException w:name=\"Title\" w:semiHidden=\"0\" w:uiPriority=\"10\" w:unhideWhenUsed=\"0\" w:qFormat=\"1\"/> \r\n");
		writer.write("  <w:lsdException w:name=\"Default Paragraph Font\" w:uiPriority=\"1\"/> \r\n");
		writer.write("  <w:lsdException w:name=\"Subtitle\" w:semiHidden=\"0\" w:uiPriority=\"11\" w:unhideWhenUsed=\"0\" w:qFormat=\"1\"/> \r\n");
		writer.write("  <w:lsdException w:name=\"Strong\" w:semiHidden=\"0\" w:uiPriority=\"22\" w:unhideWhenUsed=\"0\" w:qFormat=\"1\"/> \r\n");
		writer.write("  <w:lsdException w:name=\"Emphasis\" w:semiHidden=\"0\" w:uiPriority=\"20\" w:unhideWhenUsed=\"0\" w:qFormat=\"1\"/> \r\n");
		writer.write("  <w:lsdException w:name=\"Table Grid\" w:semiHidden=\"0\" w:uiPriority=\"59\" w:unhideWhenUsed=\"0\"/> \r\n");
		writer.write("  <w:lsdException w:name=\"Placeholder Text\" w:unhideWhenUsed=\"0\"/> \r\n");
		writer.write("  <w:lsdException w:name=\"No Spacing\" w:semiHidden=\"0\" w:uiPriority=\"1\" w:unhideWhenUsed=\"0\" w:qFormat=\"1\"/> \r\n");
		writer.write("  <w:lsdException w:name=\"Light Shading\" w:semiHidden=\"0\" w:uiPriority=\"60\" w:unhideWhenUsed=\"0\"/> \r\n");
		writer.write("  <w:lsdException w:name=\"Light List\" w:semiHidden=\"0\" w:uiPriority=\"61\" w:unhideWhenUsed=\"0\"/> \r\n");
		writer.write("  <w:lsdException w:name=\"Light Grid\" w:semiHidden=\"0\" w:uiPriority=\"62\" w:unhideWhenUsed=\"0\"/> \r\n");
		writer.write("  <w:lsdException w:name=\"Medium Shading 1\" w:semiHidden=\"0\" w:uiPriority=\"63\" w:unhideWhenUsed=\"0\"/> \r\n");
		writer.write("  <w:lsdException w:name=\"Medium Shading 2\" w:semiHidden=\"0\" w:uiPriority=\"64\" w:unhideWhenUsed=\"0\"/> \r\n");
		writer.write("  <w:lsdException w:name=\"Medium List 1\" w:semiHidden=\"0\" w:uiPriority=\"65\" w:unhideWhenUsed=\"0\"/> \r\n");
		writer.write("  <w:lsdException w:name=\"Medium List 2\" w:semiHidden=\"0\" w:uiPriority=\"66\" w:unhideWhenUsed=\"0\"/> \r\n");
		writer.write("  <w:lsdException w:name=\"Medium Grid 1\" w:semiHidden=\"0\" w:uiPriority=\"67\" w:unhideWhenUsed=\"0\"/> \r\n");
		writer.write("  <w:lsdException w:name=\"Medium Grid 2\" w:semiHidden=\"0\" w:uiPriority=\"68\" w:unhideWhenUsed=\"0\"/> \r\n");
		writer.write("  <w:lsdException w:name=\"Medium Grid 3\" w:semiHidden=\"0\" w:uiPriority=\"69\" w:unhideWhenUsed=\"0\"/> \r\n");
		writer.write("  <w:lsdException w:name=\"Dark List\" w:semiHidden=\"0\" w:uiPriority=\"70\" w:unhideWhenUsed=\"0\"/> \r\n");
		writer.write("  <w:lsdException w:name=\"Colorful Shading\" w:semiHidden=\"0\" w:uiPriority=\"71\" w:unhideWhenUsed=\"0\"/> \r\n");
		writer.write("  <w:lsdException w:name=\"Colorful List\" w:semiHidden=\"0\" w:uiPriority=\"72\" w:unhideWhenUsed=\"0\"/> \r\n");
		writer.write("  <w:lsdException w:name=\"Colorful Grid\" w:semiHidden=\"0\" w:uiPriority=\"73\" w:unhideWhenUsed=\"0\"/> \r\n");
		writer.write("  <w:lsdException w:name=\"Light Shading Accent 1\" w:semiHidden=\"0\" w:uiPriority=\"60\" w:unhideWhenUsed=\"0\"/> \r\n");
		writer.write("  <w:lsdException w:name=\"Light List Accent 1\" w:semiHidden=\"0\" w:uiPriority=\"61\" w:unhideWhenUsed=\"0\"/> \r\n");
		writer.write("  <w:lsdException w:name=\"Light Grid Accent 1\" w:semiHidden=\"0\" w:uiPriority=\"62\" w:unhideWhenUsed=\"0\"/> \r\n");
		writer.write("  <w:lsdException w:name=\"Medium Shading 1 Accent 1\" w:semiHidden=\"0\" w:uiPriority=\"63\" w:unhideWhenUsed=\"0\"/> \r\n");
		writer.write("  <w:lsdException w:name=\"Medium Shading 2 Accent 1\" w:semiHidden=\"0\" w:uiPriority=\"64\" w:unhideWhenUsed=\"0\"/> \r\n");
		writer.write("  <w:lsdException w:name=\"Medium List 1 Accent 1\" w:semiHidden=\"0\" w:uiPriority=\"65\" w:unhideWhenUsed=\"0\"/> \r\n");
		writer.write("  <w:lsdException w:name=\"Revision\" w:unhideWhenUsed=\"0\"/> \r\n");
		writer.write("  <w:lsdException w:name=\"List Paragraph\" w:semiHidden=\"0\" w:uiPriority=\"34\" w:unhideWhenUsed=\"0\" w:qFormat=\"1\"/> \r\n");
		writer.write("  <w:lsdException w:name=\"Quote\" w:semiHidden=\"0\" w:uiPriority=\"29\" w:unhideWhenUsed=\"0\" w:qFormat=\"1\"/> \r\n");
		writer.write("  <w:lsdException w:name=\"Intense Quote\" w:semiHidden=\"0\" w:uiPriority=\"30\" w:unhideWhenUsed=\"0\" w:qFormat=\"1\"/> \r\n");
		writer.write("  <w:lsdException w:name=\"Medium List 2 Accent 1\" w:semiHidden=\"0\" w:uiPriority=\"66\" w:unhideWhenUsed=\"0\"/> \r\n");
		writer.write("  <w:lsdException w:name=\"Medium Grid 1 Accent 1\" w:semiHidden=\"0\" w:uiPriority=\"67\" w:unhideWhenUsed=\"0\"/> \r\n");
		writer.write("  <w:lsdException w:name=\"Medium Grid 2 Accent 1\" w:semiHidden=\"0\" w:uiPriority=\"68\" w:unhideWhenUsed=\"0\"/> \r\n");
		writer.write("  <w:lsdException w:name=\"Medium Grid 3 Accent 1\" w:semiHidden=\"0\" w:uiPriority=\"69\" w:unhideWhenUsed=\"0\"/> \r\n");
		writer.write("  <w:lsdException w:name=\"Dark List Accent 1\" w:semiHidden=\"0\" w:uiPriority=\"70\" w:unhideWhenUsed=\"0\"/> \r\n");
		writer.write("  <w:lsdException w:name=\"Colorful Shading Accent 1\" w:semiHidden=\"0\" w:uiPriority=\"71\" w:unhideWhenUsed=\"0\"/> \r\n");
		writer.write("  <w:lsdException w:name=\"Colorful List Accent 1\" w:semiHidden=\"0\" w:uiPriority=\"72\" w:unhideWhenUsed=\"0\"/> \r\n");
		writer.write("  <w:lsdException w:name=\"Colorful Grid Accent 1\" w:semiHidden=\"0\" w:uiPriority=\"73\" w:unhideWhenUsed=\"0\"/> \r\n");
		writer.write("  <w:lsdException w:name=\"Light Shading Accent 2\" w:semiHidden=\"0\" w:uiPriority=\"60\" w:unhideWhenUsed=\"0\"/> \r\n");
		writer.write("  <w:lsdException w:name=\"Light List Accent 2\" w:semiHidden=\"0\" w:uiPriority=\"61\" w:unhideWhenUsed=\"0\"/> \r\n");
		writer.write("  <w:lsdException w:name=\"Light Grid Accent 2\" w:semiHidden=\"0\" w:uiPriority=\"62\" w:unhideWhenUsed=\"0\"/> \r\n");
		writer.write("  <w:lsdException w:name=\"Medium Shading 1 Accent 2\" w:semiHidden=\"0\" w:uiPriority=\"63\" w:unhideWhenUsed=\"0\"/> \r\n");
		writer.write("  <w:lsdException w:name=\"Medium Shading 2 Accent 2\" w:semiHidden=\"0\" w:uiPriority=\"64\" w:unhideWhenUsed=\"0\"/> \r\n");
		writer.write("  <w:lsdException w:name=\"Medium List 1 Accent 2\" w:semiHidden=\"0\" w:uiPriority=\"65\" w:unhideWhenUsed=\"0\"/> \r\n");
		writer.write("  <w:lsdException w:name=\"Medium List 2 Accent 2\" w:semiHidden=\"0\" w:uiPriority=\"66\" w:unhideWhenUsed=\"0\"/> \r\n");
		writer.write("  <w:lsdException w:name=\"Medium Grid 1 Accent 2\" w:semiHidden=\"0\" w:uiPriority=\"67\" w:unhideWhenUsed=\"0\"/> \r\n");
		writer.write("  <w:lsdException w:name=\"Medium Grid 2 Accent 2\" w:semiHidden=\"0\" w:uiPriority=\"68\" w:unhideWhenUsed=\"0\"/> \r\n");
		writer.write("  <w:lsdException w:name=\"Medium Grid 3 Accent 2\" w:semiHidden=\"0\" w:uiPriority=\"69\" w:unhideWhenUsed=\"0\"/> \r\n");
		writer.write("  <w:lsdException w:name=\"Dark List Accent 2\" w:semiHidden=\"0\" w:uiPriority=\"70\" w:unhideWhenUsed=\"0\"/> \r\n");
		writer.write("  <w:lsdException w:name=\"Colorful Shading Accent 2\" w:semiHidden=\"0\" w:uiPriority=\"71\" w:unhideWhenUsed=\"0\"/> \r\n");
		writer.write("  <w:lsdException w:name=\"Colorful List Accent 2\" w:semiHidden=\"0\" w:uiPriority=\"72\" w:unhideWhenUsed=\"0\"/> \r\n");
		writer.write("  <w:lsdException w:name=\"Colorful Grid Accent 2\" w:semiHidden=\"0\" w:uiPriority=\"73\" w:unhideWhenUsed=\"0\"/> \r\n");
		writer.write("  <w:lsdException w:name=\"Light Shading Accent 3\" w:semiHidden=\"0\" w:uiPriority=\"60\" w:unhideWhenUsed=\"0\"/> \r\n");
		writer.write("  <w:lsdException w:name=\"Light List Accent 3\" w:semiHidden=\"0\" w:uiPriority=\"61\" w:unhideWhenUsed=\"0\"/> \r\n");
		writer.write("  <w:lsdException w:name=\"Light Grid Accent 3\" w:semiHidden=\"0\" w:uiPriority=\"62\" w:unhideWhenUsed=\"0\"/> \r\n");
		writer.write("  <w:lsdException w:name=\"Medium Shading 1 Accent 3\" w:semiHidden=\"0\" w:uiPriority=\"63\" w:unhideWhenUsed=\"0\"/> \r\n");
		writer.write("  <w:lsdException w:name=\"Medium Shading 2 Accent 3\" w:semiHidden=\"0\" w:uiPriority=\"64\" w:unhideWhenUsed=\"0\"/> \r\n");
		writer.write("  <w:lsdException w:name=\"Medium List 1 Accent 3\" w:semiHidden=\"0\" w:uiPriority=\"65\" w:unhideWhenUsed=\"0\"/> \r\n");
		writer.write("  <w:lsdException w:name=\"Medium List 2 Accent 3\" w:semiHidden=\"0\" w:uiPriority=\"66\" w:unhideWhenUsed=\"0\"/> \r\n");
		writer.write("  <w:lsdException w:name=\"Medium Grid 1 Accent 3\" w:semiHidden=\"0\" w:uiPriority=\"67\" w:unhideWhenUsed=\"0\"/> \r\n");
		writer.write("  <w:lsdException w:name=\"Medium Grid 2 Accent 3\" w:semiHidden=\"0\" w:uiPriority=\"68\" w:unhideWhenUsed=\"0\"/> \r\n");
		writer.write("  <w:lsdException w:name=\"Medium Grid 3 Accent 3\" w:semiHidden=\"0\" w:uiPriority=\"69\" w:unhideWhenUsed=\"0\"/> \r\n");
		writer.write("  <w:lsdException w:name=\"Dark List Accent 3\" w:semiHidden=\"0\" w:uiPriority=\"70\" w:unhideWhenUsed=\"0\"/> \r\n");
		writer.write("  <w:lsdException w:name=\"Colorful Shading Accent 3\" w:semiHidden=\"0\" w:uiPriority=\"71\" w:unhideWhenUsed=\"0\"/> \r\n");
		writer.write("  <w:lsdException w:name=\"Colorful List Accent 3\" w:semiHidden=\"0\" w:uiPriority=\"72\" w:unhideWhenUsed=\"0\"/> \r\n");
		writer.write("  <w:lsdException w:name=\"Colorful Grid Accent 3\" w:semiHidden=\"0\" w:uiPriority=\"73\" w:unhideWhenUsed=\"0\"/> \r\n");
		writer.write("  <w:lsdException w:name=\"Light Shading Accent 4\" w:semiHidden=\"0\" w:uiPriority=\"60\" w:unhideWhenUsed=\"0\"/> \r\n");
		writer.write("  <w:lsdException w:name=\"Light List Accent 4\" w:semiHidden=\"0\" w:uiPriority=\"61\" w:unhideWhenUsed=\"0\"/> \r\n");
		writer.write("  <w:lsdException w:name=\"Light Grid Accent 4\" w:semiHidden=\"0\" w:uiPriority=\"62\" w:unhideWhenUsed=\"0\"/> \r\n");
		writer.write("  <w:lsdException w:name=\"Medium Shading 1 Accent 4\" w:semiHidden=\"0\" w:uiPriority=\"63\" w:unhideWhenUsed=\"0\"/> \r\n");
		writer.write("  <w:lsdException w:name=\"Medium Shading 2 Accent 4\" w:semiHidden=\"0\" w:uiPriority=\"64\" w:unhideWhenUsed=\"0\"/> \r\n");
		writer.write("  <w:lsdException w:name=\"Medium List 1 Accent 4\" w:semiHidden=\"0\" w:uiPriority=\"65\" w:unhideWhenUsed=\"0\"/> \r\n");
		writer.write("  <w:lsdException w:name=\"Medium List 2 Accent 4\" w:semiHidden=\"0\" w:uiPriority=\"66\" w:unhideWhenUsed=\"0\"/> \r\n");
		writer.write("  <w:lsdException w:name=\"Medium Grid 1 Accent 4\" w:semiHidden=\"0\" w:uiPriority=\"67\" w:unhideWhenUsed=\"0\"/> \r\n");
		writer.write("  <w:lsdException w:name=\"Medium Grid 2 Accent 4\" w:semiHidden=\"0\" w:uiPriority=\"68\" w:unhideWhenUsed=\"0\"/> \r\n");
		writer.write("  <w:lsdException w:name=\"Medium Grid 3 Accent 4\" w:semiHidden=\"0\" w:uiPriority=\"69\" w:unhideWhenUsed=\"0\"/> \r\n");
		writer.write("  <w:lsdException w:name=\"Dark List Accent 4\" w:semiHidden=\"0\" w:uiPriority=\"70\" w:unhideWhenUsed=\"0\"/> \r\n");
		writer.write("  <w:lsdException w:name=\"Colorful Shading Accent 4\" w:semiHidden=\"0\" w:uiPriority=\"71\" w:unhideWhenUsed=\"0\"/> \r\n");
		writer.write("  <w:lsdException w:name=\"Colorful List Accent 4\" w:semiHidden=\"0\" w:uiPriority=\"72\" w:unhideWhenUsed=\"0\"/> \r\n");
		writer.write("  <w:lsdException w:name=\"Colorful Grid Accent 4\" w:semiHidden=\"0\" w:uiPriority=\"73\" w:unhideWhenUsed=\"0\"/> \r\n");
		writer.write("  <w:lsdException w:name=\"Light Shading Accent 5\" w:semiHidden=\"0\" w:uiPriority=\"60\" w:unhideWhenUsed=\"0\"/> \r\n");
		writer.write("  <w:lsdException w:name=\"Light List Accent 5\" w:semiHidden=\"0\" w:uiPriority=\"61\" w:unhideWhenUsed=\"0\"/> \r\n");
		writer.write("  <w:lsdException w:name=\"Light Grid Accent 5\" w:semiHidden=\"0\" w:uiPriority=\"62\" w:unhideWhenUsed=\"0\"/> \r\n");
		writer.write("  <w:lsdException w:name=\"Medium Shading 1 Accent 5\" w:semiHidden=\"0\" w:uiPriority=\"63\" w:unhideWhenUsed=\"0\"/> \r\n");
		writer.write("  <w:lsdException w:name=\"Medium Shading 2 Accent 5\" w:semiHidden=\"0\" w:uiPriority=\"64\" w:unhideWhenUsed=\"0\"/> \r\n");
		writer.write("  <w:lsdException w:name=\"Medium List 1 Accent 5\" w:semiHidden=\"0\" w:uiPriority=\"65\" w:unhideWhenUsed=\"0\"/> \r\n");
		writer.write("  <w:lsdException w:name=\"Medium List 2 Accent 5\" w:semiHidden=\"0\" w:uiPriority=\"66\" w:unhideWhenUsed=\"0\"/> \r\n");
		writer.write("  <w:lsdException w:name=\"Medium Grid 1 Accent 5\" w:semiHidden=\"0\" w:uiPriority=\"67\" w:unhideWhenUsed=\"0\"/> \r\n");
		writer.write("  <w:lsdException w:name=\"Medium Grid 2 Accent 5\" w:semiHidden=\"0\" w:uiPriority=\"68\" w:unhideWhenUsed=\"0\"/> \r\n");
		writer.write("  <w:lsdException w:name=\"Medium Grid 3 Accent 5\" w:semiHidden=\"0\" w:uiPriority=\"69\" w:unhideWhenUsed=\"0\"/> \r\n");
		writer.write("  <w:lsdException w:name=\"Dark List Accent 5\" w:semiHidden=\"0\" w:uiPriority=\"70\" w:unhideWhenUsed=\"0\"/> \r\n");
		writer.write("  <w:lsdException w:name=\"Colorful Shading Accent 5\" w:semiHidden=\"0\" w:uiPriority=\"71\" w:unhideWhenUsed=\"0\"/> \r\n");
		writer.write("  <w:lsdException w:name=\"Colorful List Accent 5\" w:semiHidden=\"0\" w:uiPriority=\"72\" w:unhideWhenUsed=\"0\"/> \r\n");
		writer.write("  <w:lsdException w:name=\"Colorful Grid Accent 5\" w:semiHidden=\"0\" w:uiPriority=\"73\" w:unhideWhenUsed=\"0\"/> \r\n");
		writer.write("  <w:lsdException w:name=\"Light Shading Accent 6\" w:semiHidden=\"0\" w:uiPriority=\"60\" w:unhideWhenUsed=\"0\"/> \r\n");
		writer.write("  <w:lsdException w:name=\"Light List Accent 6\" w:semiHidden=\"0\" w:uiPriority=\"61\" w:unhideWhenUsed=\"0\"/> \r\n");
		writer.write("  <w:lsdException w:name=\"Light Grid Accent 6\" w:semiHidden=\"0\" w:uiPriority=\"62\" w:unhideWhenUsed=\"0\"/> \r\n");
		writer.write("  <w:lsdException w:name=\"Medium Shading 1 Accent 6\" w:semiHidden=\"0\" w:uiPriority=\"63\" w:unhideWhenUsed=\"0\"/> \r\n");
		writer.write("  <w:lsdException w:name=\"Medium Shading 2 Accent 6\" w:semiHidden=\"0\" w:uiPriority=\"64\" w:unhideWhenUsed=\"0\"/> \r\n");
		writer.write("  <w:lsdException w:name=\"Medium List 1 Accent 6\" w:semiHidden=\"0\" w:uiPriority=\"65\" w:unhideWhenUsed=\"0\"/> \r\n");
		writer.write("  <w:lsdException w:name=\"Medium List 2 Accent 6\" w:semiHidden=\"0\" w:uiPriority=\"66\" w:unhideWhenUsed=\"0\"/> \r\n");
		writer.write("  <w:lsdException w:name=\"Medium Grid 1 Accent 6\" w:semiHidden=\"0\" w:uiPriority=\"67\" w:unhideWhenUsed=\"0\"/> \r\n");
		writer.write("  <w:lsdException w:name=\"Medium Grid 2 Accent 6\" w:semiHidden=\"0\" w:uiPriority=\"68\" w:unhideWhenUsed=\"0\"/> \r\n");
		writer.write("  <w:lsdException w:name=\"Medium Grid 3 Accent 6\" w:semiHidden=\"0\" w:uiPriority=\"69\" w:unhideWhenUsed=\"0\"/> \r\n");
		writer.write("  <w:lsdException w:name=\"Dark List Accent 6\" w:semiHidden=\"0\" w:uiPriority=\"70\" w:unhideWhenUsed=\"0\"/> \r\n");
		writer.write("  <w:lsdException w:name=\"Colorful Shading Accent 6\" w:semiHidden=\"0\" w:uiPriority=\"71\" w:unhideWhenUsed=\"0\"/> \r\n");
		writer.write("  <w:lsdException w:name=\"Colorful List Accent 6\" w:semiHidden=\"0\" w:uiPriority=\"72\" w:unhideWhenUsed=\"0\"/> \r\n");
		writer.write("  <w:lsdException w:name=\"Colorful Grid Accent 6\" w:semiHidden=\"0\" w:uiPriority=\"73\" w:unhideWhenUsed=\"0\"/> \r\n");
		writer.write("  <w:lsdException w:name=\"Subtle Emphasis\" w:semiHidden=\"0\" w:uiPriority=\"19\" w:unhideWhenUsed=\"0\" w:qFormat=\"1\"/> \r\n");
		writer.write("  <w:lsdException w:name=\"Intense Emphasis\" w:semiHidden=\"0\" w:uiPriority=\"21\" w:unhideWhenUsed=\"0\" w:qFormat=\"1\"/> \r\n");
		writer.write("  <w:lsdException w:name=\"Subtle Reference\" w:semiHidden=\"0\" w:uiPriority=\"31\" w:unhideWhenUsed=\"0\" w:qFormat=\"1\"/> \r\n");
		writer.write("  <w:lsdException w:name=\"Intense Reference\" w:semiHidden=\"0\" w:uiPriority=\"32\" w:unhideWhenUsed=\"0\" w:qFormat=\"1\"/> \r\n");
		writer.write("  <w:lsdException w:name=\"Book Title\" w:semiHidden=\"0\" w:uiPriority=\"33\" w:unhideWhenUsed=\"0\" w:qFormat=\"1\"/> \r\n");
		writer.write("  <w:lsdException w:name=\"Bibliography\" w:uiPriority=\"37\"/><w:lsdException w:name=\"TOC Heading\" w:uiPriority=\"39\" w:qFormat=\"1\"/> \r\n");
		writer.write(" </w:latentStyles> \r\n");
		writer.flush();
	}
	
	/**
	 * 
	 *
	private void buildStyles() throws IOException
	{
		writer.write(" <w:style w:type=\"paragraph\" w:default=\"1\" w:styleId=\"Normal\"> \r\n");	
		writer.write("  <w:name w:val=\"Normal\"/> \r\n");	
		writer.write("  <w:qFormat/> \r\n");	
		writer.write(" </w:style> \r\n");	
		writer.write(" <w:style w:type=\"character\" w:default=\"1\" w:styleId=\"DefaultParagraphFont\"> \r\n");	
		writer.write("  <w:name w:val=\"Default Paragraph Font\"/> \r\n");	
		writer.write("  <w:uiPriority w:val=\"1\"/> \r\n");	
		writer.write("  <w:semiHidden/> \r\n");	
		writer.write("  <w:unhideWhenUsed/> \r\n");	
		writer.write(" </w:style> \r\n");	
		writer.write(" <w:style w:type=\"table\" w:default=\"1\" w:styleId=\"TableNormal\"> \r\n");	
		writer.write("  <w:name w:val=\"Normal Table\"/> \r\n");	
		writer.write("  <w:uiPriority w:val=\"99\"/> \r\n");	
		writer.write("  <w:semiHidden/> \r\n");	
		writer.write("  <w:unhideWhenUsed/> \r\n");	
		writer.write("  <w:qFormat/> \r\n");	
		writer.write("  <w:tblPr> \r\n");	
		writer.write("   <w:tblInd w:w=\"0\" w:type=\"dxa\"/> \r\n");	
		writer.write("   <w:tblCellMar> \r\n");	
		writer.write("    <w:top w:w=\"0\" w:type=\"dxa\"/> \r\n");	
		writer.write("    <w:left w:w=\"0\" w:type=\"dxa\"/> \r\n");	
		writer.write("    <w:bottom w:w=\"0\" w:type=\"dxa\"/> \r\n");	
		writer.write("    <w:right w:w=\"0\" w:type=\"dxa\"/> \r\n");	
		writer.write("   </w:tblCellMar> \r\n");	
		writer.write("  </w:tblPr> \r\n");	
		writer.write(" </w:style> \r\n");	
		writer.write(" <w:style w:type=\"numbering\" w:default=\"1\" w:styleId=\"NoList\"> \r\n");	
		writer.write("  <w:name w:val=\"No List\"/> \r\n");	
		writer.write("  <w:uiPriority w:val=\"99\"/> \r\n");	
		writer.write("  <w:semiHidden/> \r\n");	
		writer.write("  <w:unhideWhenUsed/> \r\n");	
		writer.write(" </w:style> \r\n");	
		writer.flush();	
	}

	/**
	 * 
	 */
	public void buildStylesFooter() throws IOException
	{
		writer.write("</w:styles> \r\n");
		writer.flush();
	}
	
	/**
	 * 
	 */
//	private void buildPageLayout(int reportIndex, JasperPrint jasperPrint) throws IOException 
//	{
//			writer.write("<style:page-layout");
//			writer.write(" style:name=\"page_" + reportIndex + "\">\n");
//			
//			writer.write("<style:page-layout-properties");
//			writer.write(" fo:page-width=\"" + Utility.translatePixelsToInchesRound(jasperPrint.getPageWidth()) +"in\"");
//			writer.write(" fo:page-height=\"" + Utility.translatePixelsToInchesRound(jasperPrint.getPageHeight()) +"in\"");//FIXMEODT we probably need some actualHeight trick
//			writer.write(" fo:margin-top=\"0in\"");//FIXMEODT useless?
//			writer.write(" fo:margin-bottom=\"0in\"");
//			writer.write(" fo:margin-left=\"0in\"");
//			writer.write(" fo:margin-right=\"0in\"");
//
//			switch (jasperPrint.getOrientation())
//			{
//				case JRReport.ORIENTATION_LANDSCAPE:
//					writer.write(" style:print-orientation=\"landscape\"");
//					break;
//				default:
//					writer.write(" style:print-orientation=\"portrait\"");
//					break;
//			}
//			
//			writer.write("/>\n");
//			writer.write("</style:page-layout>\n");
//	}

	/**
	 * 
	 */
//	private void buildMasterPage(int reportIndex) throws IOException 
//	{
//		writer.write("<style:master-page style:name=\"master_");
//		writer.write(String.valueOf(reportIndex));
//		writer.write("\" style:page-layout-name=\"page_");
//		writer.write(String.valueOf(reportIndex));
//		writer.write("\"/>\n");
//	}

}
