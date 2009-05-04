<?xml version="1.0" encoding="UTF-8"?>

<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
	xmlns:xsd="http://www.w3.org/2001/XMLSchema"
	xmlns:jr="http://jasperreports.sourceforge.net/jasperreports">

<xsl:output method = "html" />
<xsl:param name="version" />

<xsl:template match="/">
<html>
<head>
<title>JasperReports <xsl:value-of select="$version"/> - Schema Reference</title>
<style type="text/css">
.title {
font-family: Arial, Verdana, Helvetica, sans-serif;
font-size: 32px;
font-weight: bold;
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
</style>
</head>
<body>

<a name="top"/>
<br/>
<span class="title">JasperReports <xsl:value-of select="$version"/> - Schema Reference</span>
<br/>
<br/>


<table width="100%" cellspacing="0" cellpadding="0" border="0">
  <tr>
    <td colspan="2"></td>
  </tr>
  <xsl:for-each select="xsd:schema/xsd:element">
  <xsl:sort select="@name"/>
  <tr>
    <td colspan="2"><xsl:element name="a"><xsl:attribute name="href">#<xsl:value-of select="@name"/></xsl:attribute><span class="toc"><xsl:value-of select="@name"/></span></xsl:element></td>
  </tr>
  </xsl:for-each>
</table>


<table width="100%" cellspacing="0" cellpadding="0" border="0">
  <tr>
    <td style="width: 20px;">.</td>
    <td style="width: 20px;">.</td>
    <td style="width: 20px;">.</td>
    <td style="width: 20px;">.</td>
    <td>.</td>
  </tr>
  <xsl:for-each select="xsd:schema/xsd:element">
  <xsl:sort select="@name"/>
  <tr>
    <td colspan="5" align="right"><br/><xsl:element name="a"><xsl:attribute name="name"><xsl:value-of select="@name"/></xsl:attribute></xsl:element><a href="#top" class="toc">top</a></td>
  </tr>
  <tr>
    <td colspan="5"><hr size="1"/></td>
  </tr>
  <tr>
    <td colspan="5"><span class="name"><xsl:value-of select="@name"/></span></td>
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


<xsl:template match="xsd:p/text()">
  <p><span class="description"><xsl:value-of select="." disable-output-escaping="yes" /></span></p>
</xsl:template>


<xsl:template match="xsd:br">
  <br/>
</xsl:template>


<xsl:template match="xsd:a">
  <span class="element">&lt;<xsl:element name="a"><xsl:attribute name="href">#<xsl:value-of select="./@href"/></xsl:attribute><xsl:value-of select="substring(.,2,string-length(.)-2)"/></xsl:element>&gt;</span>
</xsl:template>


<xsl:template match="xsd:complexType/xsd:attribute">
  <tr>
    <td colspan="2"></td>
	<td colspan="3"><span class="attribute"><br/><xsl:value-of select="@name"/></span></td>
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
    <td colspan="3"><xsl:element name="a"><xsl:attribute name="href">#<xsl:value-of select="substring(@ref,4)"/></xsl:attribute><span class="element"><xsl:value-of select="substring(@ref,4)"/></span></xsl:element><xsl:choose><xsl:when test="@maxOccurs='unbounded' or ../@maxOccurs='unbounded'"><span class="description">*</span></xsl:when><xsl:when test="@maxOccurs='1' or ../@maxOccurs='1'"><span class="description">?</span></xsl:when></xsl:choose></td>
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
