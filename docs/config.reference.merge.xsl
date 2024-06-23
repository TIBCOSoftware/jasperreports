<?xml version="1.0" encoding="UTF-8"?>

<xsl:stylesheet version="2.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
	xmlns:xsd="http://www.w3.org/2001/XMLSchema"
	xmlns:jr="http://jasperreports.sourceforge.net/jasperreports">

<xsl:output method="xml"/>
<xsl:param name="configRefFile" select="'../core/target/config.reference.xml'"/>
<xsl:param name="configRef" select="document($configRefFile)"/>
<xsl:param name="barcode4jConfigRefFile" select="'../ext/barcode4j/target/config.reference.xml'"/>
<xsl:param name="barcode4jCR" select="document($barcode4jConfigRefFile)"/>
<xsl:param name="chartsConfigRefFile" select="'../ext/charts/target/config.reference.xml'"/>
<xsl:param name="chartsCR" select="document($chartsConfigRefFile)"/>
<xsl:param name="chromeConfigRefFile" select="'../ext/chrome/target/config.reference.xml'"/>
<xsl:param name="chromeCR" select="document($chromeConfigRefFile)"/>
<xsl:param name="dataAdaptersConfigRefFile" select="'../ext/data-adapters/target/config.reference.xml'"/>
<xsl:param name="dataAdaptersCR" select="document($dataAdaptersConfigRefFile)"/>
<xsl:param name="dataAdaptersHttpConfigRefFile" select="'../ext/data-adapters-http/target/config.reference.xml'"/>
<xsl:param name="dataAdaptersHttpCR" select="document($dataAdaptersHttpConfigRefFile)"/>
<xsl:param name="ejbqlConfigRefFile" select="'../ext/ejbql/target/config.reference.xml'"/>
<xsl:param name="ejbqlCR" select="document($ejbqlConfigRefFile)"/>
<xsl:param name="ejbqlJ2eeConfigRefFile" select="'../ext/ejbql-j2ee/target/config.reference.xml'"/>
<xsl:param name="ejbqlJ2eeCR" select="document($ejbqlJ2eeConfigRefFile)"/>
<xsl:param name="excelPoiConfigRefFile" select="'../ext/excel-poi/target/config.reference.xml'"/>
<xsl:param name="excelPoiCR" select="document($excelPoiConfigRefFile)"/>
<xsl:param name="googleMapsConfigRefFile" select="'../ext/google-maps/target/config.reference.xml'"/>
<xsl:param name="googleMapsCR" select="document($googleMapsConfigRefFile)"/>
<xsl:param name="groovyConfigRefFile" select="'../ext/groovy/target/config.reference.xml'"/>
<xsl:param name="groovyCR" select="document($groovyConfigRefFile)"/>
<xsl:param name="interactivityConfigRefFile" select="'../ext/interactivity/target/config.reference.xml'"/>
<xsl:param name="interactivityCR" select="document($interactivityConfigRefFile)"/>
<xsl:param name="javascriptConfigRefFile" select="'../ext/javascript/target/config.reference.xml'"/>
<xsl:param name="javascriptCR" select="document($javascriptConfigRefFile)"/>
<xsl:param name="jsonConfigRefFile" select="'../ext/json/target/config.reference.xml'"/>
<xsl:param name="jsonCR" select="document($jsonConfigRefFile)"/>
<xsl:param name="olapConfigRefFile" select="'../ext/olap/target/config.reference.xml'"/>
<xsl:param name="olapCR" select="document($olapConfigRefFile)"/>
<xsl:param name="pdfConfigRefFile" select="'../ext/pdf/target/config.reference.xml'"/>
<xsl:param name="pdfCR" select="document($pdfConfigRefFile)"/>
<xsl:param name="velocityConfigRefFile" select="'../ext/velocity/target/config.reference.xml'"/>
<xsl:param name="velocityCR" select="document($velocityConfigRefFile)"/>

<xsl:variable name="configReferences" select="$configRef, $barcode4jCR, $chartsCR, $chromeCR, $dataAdaptersCR, $dataAdaptersHttpCR, $ejbqlCR, $ejbqlJ2eeCR, $excelPoiCR, $googleMapsCR, $groovyCR, $interactivityCR, $javascriptCR, $jsonCR, $olapCR, $pdfCR, $velocityCR"/>

<xsl:template match="/">
<configReference>
	<xsl:for-each-group select="$configReferences/configReference/category" group-by="name">
		<xsl:variable name="crtCategory" select="."/>
	<category>
		<name><xsl:value-of select="name"/></name>
		<content>
		<xsl:for-each select="$configReferences/configReference/category">
			<xsl:if test="$crtCategory/name = name">
				<xsl:for-each select="./content/property">
					<xsl:apply-templates select="."/>
				</xsl:for-each>
			</xsl:if>
		</xsl:for-each>
		</content>
	</category>
	</xsl:for-each-group>
	<xsl:for-each select="$configReferences/configReference/configProperty">
		<xsl:apply-templates select="."/>
	</xsl:for-each>
</configReference>
</xsl:template>

<xsl:template match="@* | node()">
	<xsl:copy>
		<xsl:apply-templates select="@* | node()" />
	</xsl:copy>
</xsl:template>

</xsl:stylesheet>
