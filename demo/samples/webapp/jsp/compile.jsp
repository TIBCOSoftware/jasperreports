<%--
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
<%@ page import="net.sf.jasperreports.engine.*" %>

<%
	System.setProperty(
		"jasper.reports.compile.class.path", 
		application.getRealPath("/WEB-INF/lib/jasperreports-0.6.4.jar") +
		System.getProperty("path.separator") + 
		application.getRealPath("/WEB-INF/classes/")
		);

	System.setProperty(
		"jasper.reports.compile.temp", 
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
