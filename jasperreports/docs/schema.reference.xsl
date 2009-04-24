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
<link rel="stylesheet" type="text/css" href="../docs/schema.reference.css"/>
</head>
<body>

<a name="top"/>

<h1>JasperReports <xsl:value-of select="$version"/> - Schema Reference</h1>


<table width="100%" cellspacing="0" cellpadding="0" border="0">
  <tr>
    <td colspan="2"></td>
  </tr>
  <xsl:for-each select="xsd:schema/xsd:element">
  <xsl:sort select="@name"/>
  <tr>
    <td colspan="2" class="enumeration"><xsl:element name="a"><xsl:attribute name="href">#<xsl:value-of select="@name"/></xsl:attribute><b><xsl:value-of select="@name"/></b></xsl:element></td>
  </tr>
  </xsl:for-each>
</table>


<table width="100%" cellspacing="0" cellpadding="0" border="0">
  <tr>
    <td style="width: 20px;"></td>
    <td></td>
  </tr>
  <xsl:for-each select="xsd:schema/xsd:element">
  <xsl:sort select="@name"/>
  <tr>
    <td colspan="2" align="right"><br/><a href="#top">top</a></td>
  </tr>
  <tr>
    <td colspan="2"><hr size="1"/></td>
  </tr>
  <tr>
    <td colspan="2"><xsl:element name="a"><xsl:attribute name="name"><xsl:value-of select="@name"/></xsl:attribute></xsl:element><b><xsl:value-of select="@name"/></b></td>
  </tr>
  <!-- tr>
    <td></td>
    <td>
<xsl:apply-templates select="deprecation"/>
    </td>
  </tr-->
  <tr>
  <td></td>
  <td>
  <table width="100%" cellspacing="0" cellpadding="0" border="0">
  <tr>
    <td style="height: 20px;" colspan="2"></td>
  </tr>
	<xsl:apply-templates select="xsd:annotation/xsd:documentation"/>
	<xsl:apply-templates select="xsd:complexType/xsd:sequence"/>
  <tr>
    <td style="height: 20px" colspan="2"></td>
  </tr>
  <tr>
    <td colspan="2"><i>Attributes:</i><xsl:choose><xsl:when test="xsd:complexType/xsd:attribute"></xsl:when><xsl:otherwise><span class="description bold"> This element has no attributes.</span></xsl:otherwise></xsl:choose></td>
  </tr>
  <xsl:if test="xsd:complexType/xsd:attribute">
  <tr>
    <td></td>
    <td>
		<table width="100%" cellspacing="0" cellpadding="0" border="0">
		  <tr>
		    <td style="width: 20px;"></td>
		    <td></td>
		  </tr>
		  	<xsl:apply-templates select="xsd:complexType/xsd:attribute"/>
		</table>
	</td>
  </tr>
  </xsl:if>
  </table>
  </td>
  </tr>
  </xsl:for-each>
</table>

</body>
</html>
</xsl:template>


<xsl:template match="xsd:complexType/xsd:sequence">
  <tr>
    <td style="height: 20px;" colspan="2"></td>
  </tr>
  <tr>
    <td colspan="2"><i>Contains:</i></td>
  </tr>
  <tr>
    <td style="width: 20px;"></td>
    <td><xsl:apply-templates/></td>
  </tr>
</xsl:template>


<xsl:template match="xsd:annotation/xsd:documentation">
  <tr>
    <td colspan="2"><i>Description:</i></td>
  </tr>
  <tr>
  	<td style="width: 20px"></td>
    <td><xsl:apply-templates/></td>
  </tr>
</xsl:template>

<xsl:template match="xsd:*" mode="copy">
  <span class="description bold"><xsl:copy-of select="."/></span>
</xsl:template>

<xsl:template match="text()">
  <span class="description bold"><xsl:value-of select="."/></span>
</xsl:template>


<xsl:template match="xsd:p/text()">
  <p><span class="description bold"><xsl:value-of select="." disable-output-escaping="yes" /></span></p>
</xsl:template>

<xsl:template match="xsd:br">
  <br/>
</xsl:template>


<xsl:template match="xsd:a">
  <span class="description bold">&lt;<xsl:element name="a"><xsl:attribute name="href">#<xsl:value-of select="./@href"/></xsl:attribute><xsl:value-of select="substring(.,2,string-length(.)-2)"/></xsl:element>&gt;</span>
</xsl:template>


<xsl:template match="xsd:complexType/xsd:attribute">
  <tr>
    <td style="width: 20px; height: 20px;" colspan="2"></td>
  </tr>
  <tr>
    <td colspan="2"><b><xsl:value-of select="@name"/></b></td>
  </tr>
  <tr>
	<td style="width: 20px;"></td>
    <td>
	<table width="100%" cellspacing="0" cellpadding="0" border="0">
	  <tr>
	    <td style="width: 20px; height: 20px;" ></td>
	    <td></td>
	  </tr>
<xsl:apply-templates select="xsd:annotation/xsd:documentation"/>
	</table>    
    </td>
  </tr>
  <tr>
    <td></td>
    <td><i>Type: </i><span class="description bold"><xsl:value-of select="@type"/></span></td>
  </tr>
  <tr>
    <td></td>
    <td><i>Use: </i><span class="description bold"><xsl:value-of select="@use"/></span></td>
  </tr>
  <tr>
    <td></td>
    <td><i>Values: </i><xsl:choose><xsl:when test="xsd:simpleType/xsd:restriction/xsd:enumeration"></xsl:when><xsl:otherwise><span class="description bold">There are no predefined values.</span></xsl:otherwise></xsl:choose></td>
  </tr>
  <xsl:if test="xsd:simpleType/xsd:restriction/xsd:enumeration">
  <tr>
    <td></td>
    <td>
	<table width="100%" cellspacing="0" cellpadding="0" border="0">
	  <tr>
	    <td style="width: 20px;"></td>
	    <td></td>
	  </tr>
<xsl:apply-templates select="xsd:simpleType/xsd:restriction/xsd:enumeration"/>
	</table>
    </td>
  </tr>
  </xsl:if>
  <tr>
    <td></td>
    <td><i>Default: </i><span class="description bold"><xsl:choose><xsl:when test="@default">
    <xsl:value-of select="@default"/></xsl:when><xsl:otherwise>There is no default value.</xsl:otherwise></xsl:choose></span></td>
  </tr>
  <tr>
    <td colspan="2"/>
  </tr>
</xsl:template>

<xsl:template match="xsd:element">
  <tr>
  	<td style="width: 20px;"></td>
    <td><xsl:element name="a"><xsl:attribute name="href">#<xsl:value-of select="substring(@ref,4)"/></xsl:attribute><span class="description bold"><xsl:value-of select="substring(@ref,4)"/></span></xsl:element><xsl:choose><xsl:when test="@maxOccurs='unbounded' or ../@maxOccurs='unbounded'"><span class="description bold">*</span></xsl:when><xsl:when test="@maxOccurs='1' or ../@maxOccurs='1'"><span class="description bold">?</span></xsl:when></xsl:choose></td>
  </tr>
</xsl:template>

<xsl:template match="xsd:simpleType/xsd:restriction/xsd:enumeration">
  <tr>
    <td style="width: 20px;"></td>
    <td>- <span class="description bold"><xsl:value-of select="@value"/></span> 
    	<xsl:if test="xsd:annotation/xsd:documentation and xsd:annotation/xsd:documentation[.!='']"> - <span class="description"><xsl:value-of select="xsd:annotation/xsd:documentation"/></span></xsl:if>
    </td>
  </tr>
</xsl:template>

</xsl:stylesheet>
