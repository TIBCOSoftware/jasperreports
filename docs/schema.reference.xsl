<?xml version="1.0" encoding="UTF-8"?>

<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
	xmlns:xsd="http://www.w3.org/2001/XMLSchema"
	xmlns:jr="http://jasperreports.sourceforge.net/jasperreports">

<xsl:output method = "html" />
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
<title>JasperReports <xsl:value-of select="$version"/> - Schema Reference</title>
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
	text-decoration: none;
	color: #000000;
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
<span class="title">JasperReports - Schema Reference (version <xsl:value-of select="$version"/>)</span>
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

<span class="description">This document describes the structure of the JRXML report template files for the JasperReports library.</span>

<br/>
<br/>

<table width="100%" cellspacing="0" cellpadding="5" border="0">
  <tr valign="top">
    <td>
  <xsl:for-each select="//xsd:element[@name]">
  <xsl:sort select="@name"/>
    <xsl:element name="a"><xsl:attribute name="href">#<xsl:choose>
	  <xsl:when test="../../../../@name"><xsl:value-of select="concat(../../../../@name,'_', @name)"/></xsl:when>
	  <xsl:when test="../../../@name"><xsl:value-of select="concat(../../../@name,'_', @name)"/></xsl:when>
	  <xsl:otherwise><xsl:value-of select="@name"/></xsl:otherwise>
    </xsl:choose>
    </xsl:attribute><span class="toc"><xsl:value-of select="@name"/></span>
    </xsl:element>
    <span class="toc">
	<xsl:choose>
	  <xsl:when test="../../../../@name"> (in <xsl:value-of select="../../../../@name"/>)</xsl:when>
	  <xsl:when test="../../../@name"> (in <xsl:value-of select="../../../@name"/>)</xsl:when>
    </xsl:choose>
    </span>
    <br/>
    <xsl:if test="position() mod 45 = 0">
	  <xsl:text disable-output-escaping="yes">&lt;/td&gt;&lt;td&gt;</xsl:text>
	</xsl:if>
  </xsl:for-each>
    </td>
  </tr>
</table>


<table width="100%" cellspacing="0" cellpadding="0" border="0">
  <tr>
    <td style="width: 20px;"><br/></td>
    <td style="width: 20px;"><br/></td>
    <td style="width: 20px;"><br/></td>
    <td style="width: 20px;"><br/></td>
    <td><br/></td>
  </tr>
  <xsl:for-each select="//xsd:element[@name]">
  <xsl:sort select="@name"/>
  <tr>
    <td colspan="5" align="right"><br/><xsl:element name="a"><xsl:attribute name="name">
	<xsl:choose>
	  <xsl:when test="../../../../@name"><xsl:value-of select="concat(../../../../@name,'_', @name)"/></xsl:when>
	  <xsl:when test="../../../@name"><xsl:value-of select="concat(../../../@name,'_', @name)"/></xsl:when>
	  <xsl:otherwise><xsl:value-of select="@name"/></xsl:otherwise>
    </xsl:choose>
    </xsl:attribute></xsl:element><a href="#top" class="toc">top</a></td>
  </tr>
  <tr>
    <td colspan="5"><hr size="1"/></td>
  </tr>
  <tr>
    <td colspan="5"><span class="name"><xsl:value-of select="@name"/>
    <xsl:choose>
	  <xsl:when test="../../../../@name"> (in <xsl:value-of select="../../../../@name"/>)</xsl:when>
	  <xsl:when test="../../../@name"> (in <xsl:value-of select="../../../@name"/>)</xsl:when>
    </xsl:choose>
    </span>
    </td>
  </tr>
  <!-- 
  <tr>
    <td></td>
    <td>
<xsl:apply-templates select="deprecation"/>
    </td>
  </tr>
  -->
  <tr>
    <td></td>
    <td colspan="4"><xsl:apply-templates select="xsd:annotation/xsd:documentation"/></td>
  </tr>
  <xsl:apply-templates select="xsd:complexType/xsd:sequence"/>
  <xsl:if test="xsd:complexType/xsd:attribute">
  <tr>
    <td></td>
	<td colspan="4"><span class="label"><br/>Attributes</span></td>
  </tr>
  <xsl:apply-templates select="xsd:complexType/xsd:attribute"/>
  </xsl:if>
  </xsl:for-each>
  <tr>
    <td colspan="5"><br/><br/></td>
  </tr>
  <tr>
    <td colspan="5"><hr size="1"/></td>
  </tr>
  <tr>
    <td colspan="5" align="center">
      <span class="copy">&#169; 2001-2010 Jaspersoft Corporation <a href="http://www.jaspersoft.com" target="_blank" class="copy">www.jaspersoft.com</a></span>
    </td>
  </tr>
</table>

</body>
</html>
</xsl:template>


<xsl:template match="xsd:complexType/xsd:sequence">
  <tr>
    <td></td>
	<td colspan="4"><span class="label"><br/>Contains</span></td>
  </tr>
  <xsl:apply-templates/>
</xsl:template>


<xsl:template match="xsd:annotation/xsd:documentation">
  <xsl:apply-templates/>
</xsl:template>


<xsl:template match="xsd:*" mode="copy">
  <span class="description"><xsl:copy-of select="."/></span>
</xsl:template>


<xsl:template match="text()">
  <span class="description"><xsl:value-of select="."/></span>
</xsl:template>


<xsl:template match="xsd:p">
  <p><xsl:apply-templates/></p>
</xsl:template>


<xsl:template match="xsd:p/text()">
  <span class="description"><xsl:value-of select="." disable-output-escaping="yes" /></span>
</xsl:template>


<xsl:template match="xsd:br">
  <br/>
</xsl:template>


<xsl:template match="xsd:a">
  <span class="element"><xsl:element name="a"><xsl:attribute name="href"><xsl:value-of select="@href"/></xsl:attribute><xsl:value-of select="."/></xsl:element></span>
</xsl:template>


<xsl:template match="xsd:elem">
  <span class="element"><xsl:element name="a"><xsl:attribute name="href">#<xsl:value-of select="."/></xsl:attribute>&lt;<xsl:value-of select="."/>&gt;</xsl:element></span>
</xsl:template>


<xsl:template match="xsd:ul">
  <xsl:element name="ul"><xsl:apply-templates/></xsl:element>
</xsl:template>


<xsl:template match="xsd:li">
  <xsl:element name="li"><xsl:apply-templates/></xsl:element>
</xsl:template>


<xsl:template match="xsd:dl">
  <table width="100%" cellpadding="0" cellspacing="0" border="0">
	<tr valign="top">
  	  <td style="width: 20px;"></td>
  	  <td><span class="element"><xsl:element name="dl"><xsl:apply-templates/></xsl:element></span></td>
	</tr>
  </table>
</xsl:template>


<xsl:template match="xsd:dd">
  <span class="description"><xsl:element name="dd"><xsl:apply-templates/></xsl:element></span>
</xsl:template>


<xsl:template match="xsd:dt">
  <span class="value"><xsl:element name="dt"><xsl:value-of select="."/></xsl:element></span>
</xsl:template>


<xsl:template match="xsd:complexType/xsd:attribute">
  <tr>
    <td colspan="2"></td>
	<td colspan="3"><br/><span class="attribute"><xsl:element name="a"><xsl:attribute name="name"><xsl:value-of select="concat(../../@name,'_', @name)"/></xsl:attribute><xsl:attribute name="href">#<xsl:value-of select="concat(../../@name,'_', @name)"/></xsl:attribute><xsl:attribute name="class">attribute</xsl:attribute><xsl:value-of select="@name"/></xsl:element></span></td>
  </tr>
  <tr>
    <td colspan="3"></td>
	<td colspan="2"><xsl:apply-templates select="xsd:annotation/xsd:documentation"/></td>
  </tr>
  <tr>
    <td colspan="3"></td>
    <td colspan="2"><span class="label">Type: </span><span class="description"><xsl:value-of select="@type"/></span></td>
  </tr>
  <tr>
    <td colspan="3"></td>
    <td colspan="2"><span class="label">Use: </span><span class="description"><xsl:value-of select="@use"/></span></td>
  </tr>
  <xsl:if test="xsd:simpleType/xsd:restriction/xsd:enumeration">
  <tr>
    <td colspan="3"></td>
    <td colspan="2"><span class="label">Values </span></td>
  </tr>
  <tr>
    <td colspan="4"></td>
    <td>
      <table width="100%" cellpadding="0" cellspacing="0" border="0">
      <xsl:apply-templates select="xsd:simpleType/xsd:restriction/xsd:enumeration"/>
      </table>
	</td>
  </tr>
  </xsl:if>
  <xsl:if test="@type='jr:basicEvaluationTime'">
  <tr>
    <td colspan="3"></td>
    <td colspan="2"><span class="label">Values </span></td>
  </tr>
  <tr>
    <td colspan="4"></td>
    <td>
      <table width="100%" cellpadding="0" cellspacing="0" border="0">
      <xsl:apply-templates select="../../../xsd:simpleType[@name='basicEvaluationTime']"/>
      </table>
	</td>
  </tr>
  </xsl:if>
  <xsl:if test="@type='jr:complexEvaluationTime'">
  <tr>
    <td colspan="3"></td>
    <td colspan="2"><span class="label">Values </span></td>
  </tr>
  <tr>
    <td colspan="4"></td>
    <td>
      <table width="100%" cellpadding="0" cellspacing="0" border="0">
      <xsl:apply-templates select="../../../xsd:simpleType[@name='complexEvaluationTime']"/>
      </table>
	</td>
  </tr>
  </xsl:if>
  <xsl:if test="@default">
  <tr>
    <td colspan="3"></td>
    <td colspan="2"><span class="label">Default: </span><span class="description"><xsl:value-of select="@default"/></span></td>
  </tr>
  </xsl:if>
  <!--
  <tr>
    <td colspan="5"/>
  </tr>
  -->
</xsl:template>


<xsl:template match="xsd:element">
  <tr>
  	<td colspan="2"></td>
    <td colspan="3"><xsl:element name="a">
     <xsl:choose>
      <xsl:when test="@name"><xsl:attribute name="href">#<xsl:choose>
	    <xsl:when test="../../../../@name"><xsl:value-of select="concat(../../../../@name,'_', @name)"/></xsl:when>
	    <xsl:when test="../../../@name"><xsl:value-of select="concat(../../../@name,'_', @name)"/></xsl:when>
	    <xsl:otherwise><xsl:value-of select="@name"/></xsl:otherwise>
       </xsl:choose>
      </xsl:attribute><span class="element"><xsl:value-of select="@name"/></span></xsl:when>
      <xsl:otherwise><xsl:attribute name="href">#<xsl:value-of select="substring(@ref,4)"/></xsl:attribute><span class="element"><xsl:value-of select="substring(@ref,4)"/></span></xsl:otherwise>
     </xsl:choose>
    </xsl:element>
    <xsl:choose><xsl:when test="@maxOccurs='unbounded' or ../@maxOccurs='unbounded'"><span class="description">*</span></xsl:when><xsl:when test="@maxOccurs='1' or ../@maxOccurs='1'"><span class="description">?</span></xsl:when></xsl:choose></td>
  </tr>
</xsl:template>


<xsl:template match="xsd:complexType/xsd:sequence/xsd:choice">
  <tr>
  	<td colspan="2"></td>
    <td colspan="3">
    <span class="description">( </span>
    <xsl:for-each select="./xsd:element">
    <xsl:element name="a">
     <xsl:choose>
      <xsl:when test="@name"><xsl:attribute name="href">#<xsl:choose>
	    <xsl:when test="../../../../@name"><xsl:value-of select="concat(../../../../@name,'_', @name)"/></xsl:when>
	    <xsl:when test="../../../@name"><xsl:value-of select="concat(../../../@name,'_', @name)"/></xsl:when>
	    <xsl:otherwise><xsl:value-of select="@name"/></xsl:otherwise>
       </xsl:choose>
      </xsl:attribute><span class="element"><xsl:value-of select="@name"/></span></xsl:when>
      <xsl:otherwise><xsl:attribute name="href">#<xsl:value-of select="substring(@ref,4)"/></xsl:attribute><span class="element"><xsl:value-of select="substring(@ref,4)"/></span></xsl:otherwise>
     </xsl:choose>
    </xsl:element>
    <xsl:choose><xsl:when test="@maxOccurs='unbounded'"><span class="description">*</span></xsl:when><xsl:when test="@maxOccurs='1' or ../@maxOccurs='1'"><span class="description">?</span></xsl:when></xsl:choose>
    <xsl:if test="@name">
    <span class="element">
	<xsl:choose>
	  <xsl:when test="../../../../@name"> (in <xsl:value-of select="../../../../@name"/>)</xsl:when>
	  <xsl:when test="../../../@name"> (in <xsl:value-of select="../../../@name"/>)</xsl:when>
    </xsl:choose>
    </span>
    </xsl:if>  
    <xsl:if test="position() &lt; count(../xsd:element)"><span class="description">
     | </span></xsl:if>
    </xsl:for-each><span class="description"> )<xsl:choose><xsl:when test="@maxOccurs='unbounded' or ../@maxOccurs='unbounded'"><span class="description">*</span></xsl:when><xsl:when test="@maxOccurs='1' or ../@maxOccurs='1'"><span class="description">?</span></xsl:when></xsl:choose>
    </span>
    </td>
  </tr>
</xsl:template>


<xsl:template match="xsd:simpleType/xsd:restriction/xsd:enumeration">
  <xsl:apply-templates select="xsd:restriction/xsd:enumeration"/>
</xsl:template>


<xsl:template match="xsd:simpleType[@name='basicEvaluationTime']">
  <xsl:apply-templates select="xsd:restriction/xsd:enumeration"/>
</xsl:template>


<xsl:template match="xsd:simpleType[@name='complexEvaluationTime']">
  <xsl:apply-templates select="xsd:restriction/xsd:enumeration"/>
</xsl:template>


<xsl:template match="xsd:restriction/xsd:enumeration">
  <tr valign="top">
    <td style="width: 10px;" nowrap="nowrap"><span class="value"><xsl:value-of select="@value"/></span></td>
    <td style="width: 10px;"></td>
    <td><xsl:if test="xsd:annotation/xsd:documentation and xsd:annotation/xsd:documentation[.!='']"><span class="description"><xsl:value-of select="xsd:annotation/xsd:documentation"/></span></xsl:if></td>
  </tr>
</xsl:template>

</xsl:stylesheet>
