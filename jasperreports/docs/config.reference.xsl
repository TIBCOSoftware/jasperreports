<?xml version="1.0" encoding="UTF-8"?>

<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

<xsl:output method = "html" />
<xsl:param name="version" />

<xsl:template match="/">
<html>
<head>
<title>JasperReports <xsl:value-of select="$version"/> - Configuration Reference</title>
<style type="text/css">
body {
	margin: 0;
	padding: 0;
	font-family: arial, helvetica, sans-serif;
}
#wrap {
	background-image: url(images/landing-bg.gif);
	margin: auto;
	width: 900px;
	padding: 0;
}
form {
	padding:0;
	margin:0;
}
.logo-bg { 
	background-image:url(images/logo-bg.gif);
	background-repeat:repeat-x;
}
.padten {
	padding:10px;
}
.pad5 {
	padding:5px;
}
.pad3 {
	padding:3px;
}
.fnt11 {
	font-size:11px;
}
.fnt12 {
	font-size:12px;
}
.fnt13 {
	font-size:13px;
}
.blueline {
	background-image:url(images/blueline.gif);
	background-position:left top;
	background-repeat:repeat-x;
}
.greybg {
	background-color:#eeedee;
}
.mn1 {
	padding:4px 2px 4px 4px;
}
.mn2 {
	padding:2px 2px 2px 4px;
}
.horline {
	width:70%;
	color:#666666;
	margin-left: auto;
    margin-right: auto;	
}
.vertline {
	background-image:url(images/vertline.gif);
	background-repeat:repeat-y;
}
.blclr {
	color:#3da1f0;
}
.blhead {
	font-size:20px;
	font-weight:bold;
	color:#3da1f0;
}
.blnormal {
	font-size:13px;
	font-weight:normal;
	color:#FFF;
	padding:0px 10px 5px 10px;
}
.blclrbg {
	background-color:#3da1f0;
}
.botbg {
	background-image:url(images/bottom.gif);
	background-repeat:repeat-x;
	color:#FFF;
}
.lm {
	font-size:11px;
	text-decoration:none;
	color:#0f72ab;
}
.lm:hover {
	font-size:11px;
	text-decoration:underline;
	color:#0f72ab;
}
a.ft {
	text-decoration:none;
	color:#FFF;
}
a.ft:hover {
	text-decoration:underline;
	color:#FFF;
}
a.bc {
	text-decoration:none;
	color:#000;
}
a.bc:hover {
	text-decoration:underline;
	color:#000;
}
.blck1 { 
	color:#464646; 
}
.gr1 { 
	color:#666666; 
}
.quote {
	color: #959595;
	padding:10px;
	font-size:13px;
	font-weight:bold;
	font-family: serif, Geneva, Arial, Helvetica;
	text-align: left;
}
.style1 {
	color: #0f72ab;
}
.fnt11bullet {
	font-size:11px;
	list-style-position: outside;
	list-style-type: disc;
	text-align: left;
}
.prop {
	font-size:12px;
	font-family: Courier New, Courier, serif;
}
.bld{
	font-weight:bold;
}
</style>
</head>
<body>

<a name="top"/>
<table width="70%" border="0" cellpadding="0" cellspacing="0" align="center" style='border-collapse:collapse;border:none'>
  <tr>
    <td height="20px"></td>
  </tr>
  <tr>
  	<td valign="top" >
  	  <span class="blhead">JasperReports <xsl:value-of select="$version"/> - Configuration Reference</span>
  	</td>
  </tr>
  <tr>
	<td height="20px"></td>
  </tr>
  <tr>
	<td height="20px"></td>
  </tr>
  <tr>
  	<td valign="top">
  	  <span class="fnt12 gr1">The table below list all the configuration properties available for the JasperReports library:</span>
  	</td>
  </tr>
</table>

<br/>

<table width="70%" border="1" cellpadding="0" cellspacing="0" align="center" style='border-collapse:collapse;border:none'>
  <xsl:for-each select="configReference/category">
  <xsl:for-each select="content/property">
  	<xsl:sort select="@ref"/>
  </xsl:for-each>
  <tr>
  	<td valign="top" style='background:#EEEDEE;border:solid windowtext 1.0pt;padding:8.9pt 5.75pt 2.9pt 15.75pt'>
  	  <xsl:value-of select="name"/>
  	</td>
  	<td valign="middle" style='border:solid windowtext 1.0pt;padding:2.9pt 5.75pt 2.9pt 5.75pt'>
  	  <xsl:apply-templates select="content"/>
  	</td>
  </tr>
  </xsl:for-each>
</table>

<br/>
<hr class="horline"/>
<br/>
<br/>

<table width="70%" cellspacing="0" cellpadding="0" border="0" align="center">
  <xsl:for-each select="configReference/configProperty">
  <xsl:sort select="@name"/>
  <tr>
    <td valign="middle"><span class="prop bld"><xsl:element name="a"><xsl:attribute name="name"><xsl:value-of select="@name"/></xsl:attribute><xsl:value-of select="@name"/></xsl:element></span></td>
  </tr>
  <tr>
    <td height="20px"></td>
  </tr>
  <tr>
    <td valign="middle" colspan="5"><span class="fnt12 gr1"><xsl:apply-templates select="description"/></span></td>
  </tr>
  <tr>
    <td height="20px"></td>
  </tr>
  <tr>
    <td>
	  <table width="100%" border="0" cellpadding="0" cellspacing="0" style='border-collapse:collapse'>
	  <tr>
	    <td valign="middle" style='width:100px;border:solid windowtext 1.0pt;background:#EEEDEE;padding:2.9pt 5.75pt 2.9pt 5.75pt'>
	      <span class="fnt11 gr1"><strong>API</strong></span></td>
	    <td valign="middle" style='border:solid windowtext 1.0pt;background:#FFFFFF;padding:2.9pt 5.75pt 2.9pt 5.75pt'>
	      	<xsl:choose>
	      		<xsl:when test="api='N/A'">
	      		  <span class="fnt11 gr1"><xsl:value-of select="api"/></span>
	      		</xsl:when>
	      		<xsl:otherwise>
	      		 <span class="lm">
	      		  <xsl:element name="a"><xsl:attribute name="href"><xsl:value-of select="api"/></xsl:attribute><xsl:attribute name="target">_blank</xsl:attribute><xsl:value-of select="substring-after(./api,'#')"/></xsl:element>
	        	 </span>
	        	</xsl:otherwise>
	        </xsl:choose>
	    </td>
	  </tr>
	  <tr>
	    <td valign="middle" style='width:100px;border:solid windowtext 1.0pt;background:#EEEDEE;padding:2.9pt 5.75pt 2.9pt 5.75pt'>
	      <span class="fnt11 gr1"><strong>Default</strong></span></td>
	    <td valign="middle" style='border:solid windowtext 1.0pt;background:#FFFFFF;padding:2.9pt 5.75pt 2.9pt 5.75pt'>
	      <span class="fnt11 gr1"><xsl:apply-templates select="default"/></span></td>
	  </tr>
	  <tr>
	    <td valign="middle" style='width:100px;border:solid windowtext 1.0pt;background:#EEEDEE;padding:2.9pt 5.75pt 2.9pt 5.75pt'>
	      <span class="fnt11 gr1"><strong>Scope</strong></span></td>
	    <td valign="middle" style='border:solid windowtext 1.0pt;background:#FFFFFF;padding:2.9pt 5.75pt 2.9pt 5.75pt'>
	      <span class="fnt11 gr1"><xsl:value-of select="scope"/></span></td>
	  </tr>
	  <tr>
	    <td colspan="2" valign="top" align="right" style='border:none;background:#FFFFFF;padding:2.9pt 5.75pt 2.9pt 5.75pt'>
	      <span class="fnt11 gr1"><xsl:element name="a"><xsl:attribute name="href">#top</xsl:attribute>back to top</xsl:element></span></td>
	  </tr>
	  </table>
	</td>
  </tr>
  <tr>
    <td height="20px"></td>
  </tr>
  </xsl:for-each>
</table>

</body>
</html>
</xsl:template>


<xsl:template match="content">
  <ul>
  	<xsl:apply-templates select="property"/>
  </ul>
</xsl:template>

<xsl:template match="description">
  <xsl:apply-templates/>
</xsl:template>


<xsl:template match="default">
  <xsl:apply-templates/>
</xsl:template>


<xsl:template match="property">
  <li><span class="lm"><xsl:element name="a"><xsl:attribute name="href">#<xsl:value-of select="@ref"/></xsl:attribute><xsl:attribute name="target">_blank</xsl:attribute><xsl:value-of select="@ref"/></xsl:element></span></li>
</xsl:template>

<xsl:template match="*" mode="copy">
  <span class="fnt11 gr1"><xsl:copy-of select="."/></span>
</xsl:template>


<xsl:template match="text()">
  <span class="fnt11 gr1"><xsl:value-of select="."/></span>
</xsl:template>


<xsl:template match="p/text()">
  <p><span class="fnt11 gr1"><xsl:value-of select="." disable-output-escaping="yes" /></span></p>
</xsl:template>


<xsl:template match="br">
  <br/>
</xsl:template>


<xsl:template match="a">
  <span class="lm"><xsl:element name="a"><xsl:attribute name="href"><xsl:value-of select="./@href"/></xsl:attribute><xsl:attribute name="target">_blank</xsl:attribute><xsl:value-of select="."/></xsl:element></span>
</xsl:template>


<xsl:template match="code">
  <xsl:element name="code"><xsl:value-of select="."/></xsl:element>
</xsl:template>


<xsl:template match="ul">
  <span class="fnt11 gr1"><xsl:element name="ul"><xsl:apply-templates/></xsl:element></span>
</xsl:template>


<xsl:template match="ol">
  <span class="fnt11 gr1"><xsl:element name="ol"><xsl:apply-templates/></xsl:element></span>
</xsl:template>


<xsl:template match="li">
  <span class="fnt11 gr1"><xsl:element name="li"><xsl:apply-templates/></xsl:element></span>
</xsl:template>


</xsl:stylesheet>
