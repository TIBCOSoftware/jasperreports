<%--
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
 * 185, Berry Street, Suite 6200
 * San Francisco CA 94107
 * http://www.jaspersoft.com
 */
--%>

<%@ page errorPage="error.jsp" %>
<%@ page import="net.sf.jasperreports.engine.*,net.sf.jasperreports.engine.util.JRProperties" %>

<%
	JRProperties.setProperty(
		JRProperties.COMPILER_CLASSPATH, 
		application.getRealPath("/WEB-INF/lib/jasperreports-1.0.2.jar") +
		System.getProperty("path.separator") + 
		application.getRealPath("/WEB-INF/classes/")
		);

	JRProperties.setProperty(
		JRProperties.COMPILER_TEMP_DIR, 
		application.getRealPath("/reports/")
		);

	JasperCompileManager.compileReportToFile(application.getRealPath("/reports/WebappReport.jrxml"));
%>

<html>
<head>
<title>JasperReports - Web Application Sample</title>
<link rel="stylesheet" type="text/css" href="../stylesheet.css" title="Style">
</head>

<body bgcolor="white">

<span class="bold">The XML report design was successfully compiled.</span>

</body>
</html>
