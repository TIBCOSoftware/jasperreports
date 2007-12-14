<?xml version="1.0" encoding="UTF-8"?>

<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

<xsl:param name="version" />

<xsl:template match="/">
<html>
<head>
<title>JasperReports <xsl:value-of select="$version"/> - Quick Reference</title>
</head>
<body>

<a name="top"/>

<h1>JasperReports <xsl:value-of select="$version"/> - Quick Reference</h1>


<table width="100%" cellspacing="0" cellpadding="0" border="0">
  <tr>
    <td style="width: 20px;"></td>
    <td></td>
  </tr>
  <xsl:for-each select="reference/element">
  <tr>
    <td colspan="2"><xsl:element name="a"><xsl:attribute name="href">#<xsl:value-of select="name"/></xsl:attribute><b><xsl:value-of select="name"/></b></xsl:element></td>
  </tr>
  </xsl:for-each>
</table>


<table width="100%" cellspacing="0" cellpadding="0" border="0">
  <tr>
    <td style="width: 20px;"></td>
    <td></td>
  </tr>
  <xsl:for-each select="reference/element">
  <tr>
    <td colspan="2" align="right"><br/><a href="#top">top</a></td>
  </tr>
  <tr>
    <td colspan="2"><hr size="1"/></td>
  </tr>
  <tr>
    <td colspan="2"><xsl:element name="a"><xsl:attribute name="name"><xsl:value-of select="name"/></xsl:attribute></xsl:element><b><xsl:value-of select="name"/></b></td>
  </tr>
  <tr>
    <td></td>
    <td>
<xsl:apply-templates select="deprecation"/>
    </td>
  </tr>
  <tr>
    <td></td>
    <td>
<xsl:apply-templates select="description"/>
    </td>
  </tr>
<xsl:apply-templates select="contains"/>
  <tr>
    <td colspan="2"><i>Attributes</i></td>
  </tr>
  <tr>
    <td></td>
    <td><xsl:apply-templates select="attributes"/></td>
  </tr>
  </xsl:for-each>
</table>

</body>
</html>
</xsl:template>


<xsl:template match="contains">
  <tr>
    <td colspan="2"><i>Contains</i></td>
  </tr>
  <tr>
    <td></td>
    <td><code>
<xsl:apply-templates/>
    </code></td>
  </tr>
</xsl:template>


<xsl:template match="description">
  <xsl:apply-templates/>
</xsl:template>

<xsl:template match="*" mode="copy">
  <xsl:copy-of select="."/>
</xsl:template>

<xsl:template match="text()">
  <xsl:value-of select="."/>
</xsl:template>


<xsl:template match="br">
  <br/>
</xsl:template>


<xsl:template match="p/text()">
  <p><xsl:value-of select="." disable-output-escaping="yes" /></p>
</xsl:template>


<xsl:template match="a">
  <xsl:element name="a"><xsl:attribute name="href">#<xsl:value-of select="./@href"/></xsl:attribute><xsl:value-of select="."/></xsl:element>
</xsl:template>


<xsl:template match="attributes">
<table width="100%" cellspacing="0" cellpadding="0" border="0">
  <tr>
    <td style="width: 20px;"></td>
    <td></td>
  </tr>
  <xsl:for-each select="attribute">
  <tr>
    <td colspan="2"><b><xsl:value-of select="name"/></b></td>
  </tr>
  <tr>
    <td></td>
    <td>
<xsl:apply-templates select="deprecation"/>
    </td>
  </tr>
  <tr>
    <td></td>
    <td>
<xsl:apply-templates select="description"/>
    </td>
  </tr>
<xsl:apply-templates select="values"/>
<xsl:apply-templates select="default"/>
<xsl:apply-templates select="required"/>
  </xsl:for-each>
</table>
</xsl:template>


<xsl:template match="values">
  <tr>
    <td colspan="2"><i>Values</i></td>
  </tr>
  <tr>
    <td></td>
    <td>
<table width="100%" cellspacing="0" cellpadding="0" border="0">
  <tr>
    <td style="width: 20px;"></td>
    <td></td>
  </tr>
  <xsl:for-each select="value">
  <tr>
    <td colspan="2"><code><xsl:value-of select="name"/></code></td>
  </tr>
  <tr>
    <td></td>
    <td>
<xsl:value-of select="description"/>
    </td>
  </tr>
  </xsl:for-each>
</table>
    </td>
  </tr>
</xsl:template>


<xsl:template match="deprecation">
  <i><b>Deprecated. </b><xsl:apply-templates/></i>
</xsl:template>


<xsl:template match="default">
  <tr>
    <td colspan="2"><i>Default </i><code><xsl:value-of select="."/></code></td>
  </tr>
</xsl:template>


<xsl:template match="required">
  <tr>
    <td colspan="2"><i>Required </i><code><xsl:value-of select="."/></code></td>
  </tr>
</xsl:template>

</xsl:stylesheet>
