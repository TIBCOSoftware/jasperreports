<%--
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
--%>

<%@ page errorPage="error.jsp" %>
<%@ page import="datasource.*" %>
<%@ page import="dori.jasper.engine.*" %>
<%@ page import="dori.jasper.engine.util.*" %>
<%@ page import="dori.jasper.engine.export.*" %>
<%@ page import="java.util.*" %>
<%@ page import="java.io.*" %>

<%
	JasperPrint jasperPrint = (JasperPrint)session.getAttribute("JASPER_PRINT");
	
	if (request.getParameter("reload") != null || jasperPrint == null)
	{
		File reportFile = new File(application.getRealPath("/reports/WebappReport.jasper"));
	
		JasperReport jasperReport = (JasperReport)JRLoader.loadObject(reportFile.getPath());

		Map parameters = new HashMap();
		parameters.put("ReportTitle", "Address Report");
		parameters.put("BaseDir", reportFile.getParentFile());
					
		jasperPrint = 
			JasperFillManager.fillReport(
				jasperReport, 
				parameters, 
				new WebappDataSource()
				);
				
		session.setAttribute("JASPER_PRINT", jasperPrint);
	}
	
	File htmlFile = new File(application.getRealPath("/jsp/html/reportPage.html"));

	JRHtmlExporter exporter = new JRHtmlExporter();
	
	int pageIndex = 0;
	int lastPageIndex = 0;
	if (jasperPrint.getPages() != null)
	{
		lastPageIndex = jasperPrint.getPages().size() - 1;
	}

	String pageStr = request.getParameter("page");
	try
	{
		pageIndex = Integer.parseInt(pageStr);
	}
	catch(Exception e)
	{
	}
	
	if (pageIndex < 0)
	{
		pageIndex = 0;
	}

	if (pageIndex > lastPageIndex)
	{
		pageIndex = lastPageIndex;
	}
	
	StringBuffer sbuffer = new StringBuffer();

	Map imagesMap = new HashMap();
	session.setAttribute("IMAGES_MAP", imagesMap);

	exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
	exporter.setParameter(JRExporterParameter.OUTPUT_STRING_BUFFER, sbuffer);
	exporter.setParameter(JRHtmlExporterParameter.IMAGES_MAP, imagesMap);
	exporter.setParameter(JRHtmlExporterParameter.IMAGES_URI, "image.jsp?image=");
	exporter.setParameter(JRExporterParameter.PAGE_INDEX, new Integer(pageIndex));
	exporter.setParameter(JRHtmlExporterParameter.HTML_HEADER, "");
	exporter.setParameter(JRHtmlExporterParameter.BETWEEN_PAGES_HTML, "");
	exporter.setParameter(JRHtmlExporterParameter.HTML_FOOTER, "");

	exporter.exportReport();
%>

<html>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<head>
  <style type="text/css">
    a {text-decoration: none}
  </style>
</head>
<body text="#000000" link="#000000" alink="#000000" vlink="#000000">
<table width="100%" cellpadding="0" cellspacing="0" border="0">
<tr>
  <td width="50%">&nbsp;</td>
  <td align="left">
    <hr size="1" color="#000000">
    <table width="100%" cellpadding="0" cellspacing="0" border="0">
      <tr>
        <td><a href="viewer.jsp?reload=true"><img src="../images/reload.GIF" border="0"></a></td>
        <td>&nbsp;&nbsp;&nbsp;</td>
<%
	if (pageIndex > 0)
	{
%>
        <td><a href="viewer.jsp?page=0"><img src="../images/first.GIF" border="0"></a></td>
        <td><a href="viewer.jsp?page=<%=pageIndex - 1%>"><img src="../images/previous.GIF" border="0"></a></td>
<%
	}
	else
	{
%>
        <td><img src="../images/first_grey.GIF" border="0"></td>
        <td><img src="../images/previous_grey.GIF" border="0"></td>
<%
	}

	if (pageIndex < lastPageIndex)
	{
%>
        <td><a href="viewer.jsp?page=<%=pageIndex + 1%>"><img src="../images/next.GIF" border="0"></a></td>
        <td><a href="viewer.jsp?page=<%=lastPageIndex%>"><img src="../images/last.GIF" border="0"></a></td>
<%
	}
	else
	{
%>
        <td><img src="../images/next_grey.GIF" border="0"></td>
        <td><img src="../images/last_grey.GIF" border="0"></td>
<%
	}
%>
        <td width="100%">&nbsp;</td>
      </tr>
    </table>
    <hr size="1" color="#000000">
  </td>
  <td width="50%">&nbsp;</td>
</tr>
<tr>
  <td width="50%">&nbsp;</td>
  <td align="center">

<%=sbuffer.toString()%>

  </td>
  <td width="50%">&nbsp;</td>
</tr>
</table>
</body>
</html>
