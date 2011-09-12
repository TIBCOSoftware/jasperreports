<?xml version="1.0" encoding="UTF-8"?>

<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
	xmlns:xsd="http://www.w3.org/2001/XMLSchema"
	xmlns:jr="http://jasperreports.sourceforge.net/jasperreports">

<xsl:output method="html"/>
<xsl:param name="sf.net"/>
<xsl:param name="version"/>

<xsl:variable name="api.url">
 <xsl:choose>
  <xsl:when test="$sf.net = 'true'">api/</xsl:when>
  <xsl:otherwise>http://jasperreports.sourceforge.net/api/</xsl:otherwise>
 </xsl:choose>
</xsl:variable>

<xsl:template match="/">
<html>
<head>
<title>JasperReports <xsl:value-of select="$version"/> - Sample Reference</title>
<style type="text/css">
.title {
	font-family: Arial, Verdana, Helvetica, sans-serif;
	font-size: 28px;
	font-weight: normal;
}

.toc {
	font-family: Courier New, Courier, serif;
	font-size: 12px;
	font-weight: normal;
}

.name {
	font-family: Courier New, Courier, serif;
	font-size: 16px;
	font-weight: bold;
}

.label {
	font-family: Arial, Verdana, Helvetica, sans-serif;
	font-size: 12px;
	font-weight: bold;
	font-style: italic;
}

.description {
	font-family: Arial, Verdana, Helvetica, sans-serif;
	font-size: 12px;
	font-weight: normal;
}

.value {
	font-family: Courier New, Courier, serif;
	font-size: 12px;
	font-weight: normal;
}

.element {
	font-family: Courier New, Courier, serif;
	font-size: 12px;
	font-weight: normal;
}

.attribute {
	font-family: Courier New, Courier, serif;
	font-size: 12px;
	font-weight: bold;
}

.complete {
	font-family: Courier New, Courier, serif;
	font-size: 12px;
	font-weight: normal;
	color: #000000;
}

.incomplete {
	font-family: Courier New, Courier, serif;
	font-size: 12px;
	font-weight: normal;
	color: #999999;
}

.copy {
	font-decoration: none;
	font-family: Verdana, Arial, Helvetica, sans-serif;
	font-size: 8pt;
	font-style: normal;
	color: #000000;
}

</style>
</head>
<body bgcolor="#FFFFFF">
<xsl:if test="$sf.net = 'true'">
<!-- Piwik -->
<script type="text/javascript">
var pkBaseURL = (("https:" == document.location.protocol) ? "https://sourceforge.net/apps/piwik/jasperreports/" : "http://sourceforge.net/apps/piwik/jasperreports/");
document.write(unescape("%3Cscript src='" + pkBaseURL + "piwik.js' type='text/javascript'%3E%3C/script%3E"));
</script><script type="text/javascript">
piwik_action_name = '';
piwik_idsite = 1;
piwik_url = pkBaseURL + "piwik.php";
piwik_log(piwik_action_name, piwik_idsite, piwik_url);
</script>
<object><noscript><p><img src="http://sourceforge.net/apps/piwik/jasperreports/piwik.php?idsite=1" alt="piwik"/></p></noscript></object>
<!-- End Piwik Tag -->
</xsl:if>

<a name="top"/>
<table cellspacing="0" cellpadding="0" border="0" width="100%">
  <tr>
    <td colspan="2" align="right">
<span class="element"><xsl:element name="a">
<xsl:attribute name="href">sample.reference.html</xsl:attribute>Sample Reference</xsl:element> - <xsl:element name="a">
<xsl:attribute name="href">schema.reference.html</xsl:attribute>Schema Reference</xsl:element> - <xsl:element name="a">
<xsl:attribute name="href">config.reference.html</xsl:attribute>Configuration Reference</xsl:element> - <xsl:element name="a">
<xsl:attribute name="href"><xsl:value-of select="$api.url"/>index.html</xsl:attribute>API (Javadoc)</xsl:element></span>
<br/>
    </td>
  </tr>
  <tr>
    <td colspan="2">
      <hr size="1"/>
    </td>
  </tr>
  <tr valign="middle">
    <td nowrap="true">
<span class="title">JasperReports - Sample Reference (version <xsl:value-of select="$version"/>)</span>
    </td>
    <td align="right">
<img src="resources/jasperreports.png" border="0"/>
    </td>
  </tr>
  <tr>
    <td colspan="2">
      <hr size="1"/>
    </td>
  </tr>
</table>

<br/>

<span class="description">This document lists all the major features of the JasperReports library, as shown in the samples shipped with the project's source code package.</span>

<table width="100%" border="0" cellpadding="0" cellspacing="0">
  <tr>
    <td style="width: 20px;"><br/></td>
    <td><br/></td>
  </tr>
  <xsl:for-each select="sampleReference/category">
  <!-- FIXME this is useless here; check the others
  <xsl:for-each select="content/feature">
    <xsl:sort select="@ref"/>
  </xsl:for-each>
  -->
  <tr>
    <td colspan="2">
      <span class="label"><br/><a><xsl:attribute name="name"><xsl:value-of select="translate(name,' /','')"/></xsl:attribute><xsl:value-of select="name"/></a></span>
    </td>
  </tr>
  <xsl:apply-templates select="content"/>
  </xsl:for-each>
</table>

<table width="100%" cellspacing="0" cellpadding="0" border="0">
  <tr>
    <td><br/><br/></td>
  </tr>
  <tr>
    <td><hr size="1"/></td>
  </tr>
  <tr>
    <td align="center">
      <span class="copy">&#169; 2001-2010 Jaspersoft Corporation <a href="http://www.jaspersoft.com" target="_blank" class="copy">www.jaspersoft.com</a></span>
    </td>
  </tr>
</table>

</body>
</html>
</xsl:template>


<xsl:template match="content">
  <xsl:for-each select="feature">
  <tr>
    <td></td>
    <td>
       <xsl:choose>
         <xsl:when test="@complete = 'false'">
      		<span class="incomplete"><xsl:element name="a"><xsl:attribute name="href">sample.reference/<xsl:value-of select="@sample"/>/index.html#<xsl:value-of select="@name"/></xsl:attribute><xsl:attribute name="class">incomplete</xsl:attribute><xsl:value-of select="@title"/></xsl:element></span>
         </xsl:when>
         <xsl:otherwise>
      		<span class="complete"><xsl:element name="a"><xsl:attribute name="href">sample.reference/<xsl:value-of select="@sample"/>/index.html#<xsl:value-of select="@name"/></xsl:attribute><xsl:attribute name="class">complete</xsl:attribute><xsl:value-of select="@title"/></xsl:element></span>
         </xsl:otherwise>
       </xsl:choose>
    </td>
  </tr>
  </xsl:for-each>
</xsl:template>

<xsl:template match="description">
  <xsl:apply-templates/>
</xsl:template>


<xsl:template match="sample">
  <span class="element"><xsl:element name="a"><xsl:attribute name="href">sample.reference/<xsl:value-of select="text()"/>/index.html</xsl:attribute><xsl:value-of select="concat('/demo/samples/', text())"/></xsl:element></span>
</xsl:template>


<xsl:template match="*" mode="copy">
  <span class="description"><xsl:copy-of select="."/></span>
</xsl:template>


<xsl:template match="text()">
  <span class="description"><xsl:value-of select="."/></span>
</xsl:template>


<xsl:template match="p/text()">
  <p><span class="description"><xsl:value-of select="." disable-output-escaping="yes" /></span></p>
</xsl:template>


<xsl:template match="br">
  <br/>
</xsl:template>


<xsl:template match="a">
  <span class="element"><xsl:element name="a"><xsl:attribute name="href"><xsl:value-of select="./@href"/></xsl:attribute><xsl:attribute name="target">_blank</xsl:attribute><xsl:value-of select="."/></xsl:element></span>
</xsl:template>


<xsl:template match="api">
  <span class="element"><xsl:element name="a"><xsl:attribute name="href"><xsl:value-of select="$api.url"/><xsl:value-of select="./@href"/></xsl:attribute><xsl:value-of select="."/></xsl:element></span>
</xsl:template>


<xsl:template match="ul">
  <xsl:element name="ul"><xsl:apply-templates/></xsl:element>
</xsl:template>


<xsl:template match="ol">
  <xsl:element name="ol"><xsl:apply-templates/></xsl:element>
</xsl:template>


<xsl:template match="li">
  <xsl:element name="li"><xsl:apply-templates/></xsl:element>
</xsl:template>


</xsl:stylesheet>
