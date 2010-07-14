/*
 * JasperReports - Free Java Reporting Library.
 * Copyright (C) 2001 - 2009 Jaspersoft Corporation. All rights reserved.
 * http://www.jaspersoft.com
 *
 * Unless you have purchased a commercial license agreement from Jaspersoft,
 * the following license terms apply:
 *
 * This program is part of JasperReports.
 *
 * JasperReports is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * JasperReports is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with JasperReports. If not, see <http://www.gnu.org/licenses/>.
 */
package net.sf.jasperreports.components.spiderchart;

import java.awt.Color;

import net.sf.jasperreports.charts.type.EdgeEnum;
import net.sf.jasperreports.engine.JRAnchor;
import net.sf.jasperreports.engine.JRConstants;
import net.sf.jasperreports.engine.JRExpression;
import net.sf.jasperreports.engine.JRExpressionCollector;
import net.sf.jasperreports.engine.JRFont;
import net.sf.jasperreports.engine.JRHyperlinkHelper;
import net.sf.jasperreports.engine.JRHyperlinkParameter;
import net.sf.jasperreports.engine.JRRuntimeException;
import net.sf.jasperreports.engine.base.JRBaseHyperlink;
import net.sf.jasperreports.engine.base.JRBaseObjectFactory;
import net.sf.jasperreports.engine.type.HyperlinkTypeEnum;


/**
 * @author sanda zaharia (shertage@users.sourceforge.net)
 * @version $Id$
 */
public class StandardChartSettings implements ChartSettings
{


	/**
	 *
	 */
	private static final long serialVersionUID = JRConstants.SERIAL_VERSION_UID;
	
	/*
	 * Chart properties
	 */
	
	public static final byte CHART_TYPE_SPIDER = 22;
	
	public static final String PROPERTY_LEGEND_BACKGROUND_COLOR = "legendBackgroundColor";
	
	public static final String PROPERTY_LEGEND_COLOR = "legendColor";
	
	public static final String PROPERTY_LEGEND_POSITION = "legendPosition";
	
	public static final String PROPERTY_SHOW_LEGEND = "showLegend";
	
	public static final String PROPERTY_SUBTITLE_COLOR = "subtitleColor";
	
	public static final String PROPERTY_TITLE_COLOR = "titleColor";
	
	public static final String PROPERTY_TITLE_POSITION = "titlePosition";
	
	public static final String PROPERTY_RENDER_TYPE = "renderType";
	
	public static final String PROPERTY_THEME = "theme";
	
	/**
	 *
	 */
	protected byte chartType = CHART_TYPE_SPIDER;

	/**
	 *
	 */
	protected Boolean showLegend = null;
	protected String linkType;
	protected String linkTarget;
	private JRHyperlinkParameter[] hyperlinkParameters;
	
	protected Color titleColor = null;
	protected Color subtitleColor = null;
	protected Color legendColor = null;
	protected Color legendBackgroundColor = null;
	protected EdgeEnum legendPositionValue = null;
	protected EdgeEnum titlePositionValue = null;

	protected String renderType;

	/**
	 *
	 */
	protected JRFont titleFont = null;
	protected JRFont subtitleFont = null;
	protected JRFont legendFont = null;

	/**
	 *
	 */
	protected JRExpression titleExpression = null;
	protected JRExpression subtitleExpression = null;
	protected JRExpression anchorNameExpression = null;
	protected JRExpression hyperlinkReferenceExpression = null;
	protected JRExpression hyperlinkAnchorExpression = null;
	protected JRExpression hyperlinkPageExpression = null;
	private JRExpression hyperlinkTooltipExpression;

	/**
	 * The bookmark level for the anchor associated with this chart.
	 * @see JRAnchor#getBookmarkLevel()
	 */
	protected int bookmarkLevel = JRAnchor.NO_BOOKMARK;

	/**
	 *
	 */
	public StandardChartSettings()
	{

	}
	
	
	public StandardChartSettings(ChartSettings chart, JRBaseObjectFactory factory)
	{
		showLegend = chart.getShowLegend();
		linkType = chart.getLinkType();
		linkTarget = chart.getLinkTarget();
		titlePositionValue = chart.getTitlePositionValue();
		titleColor = chart.getTitleColor();
		subtitleColor = chart.getSubtitleColor();
		legendColor = chart.getLegendColor();
		legendBackgroundColor = chart.getLegendBackgroundColor();
		legendPositionValue = chart.getLegendPositionValue();
		renderType = chart.getRenderType();
		
		titleFont = chart.getTitleFont();
		subtitleFont = chart.getSubtitleFont();
		legendFont = chart.getLegendFont();
		titleExpression = factory.getExpression(chart.getTitleExpression());

		subtitleExpression = factory.getExpression(chart.getSubtitleExpression());
		anchorNameExpression = factory.getExpression(chart.getAnchorNameExpression());
		hyperlinkReferenceExpression = factory.getExpression(chart.getHyperlinkReferenceExpression());
		hyperlinkAnchorExpression = factory.getExpression(chart.getHyperlinkAnchorExpression());
		hyperlinkPageExpression = factory.getExpression(chart.getHyperlinkPageExpression());
		hyperlinkTooltipExpression = factory.getExpression(chart.getHyperlinkTooltipExpression());
		bookmarkLevel = chart.getBookmarkLevel();
		hyperlinkParameters = JRBaseHyperlink.copyHyperlinkParameters(chart, factory);

	}
	/**
	 * 
	 */
	public Boolean getShowLegend()
	{
		return this.showLegend;
	}

	/**
	 *
	 */
	public void setShowLegend(Boolean isShowLegend)
	{
		this.showLegend = isShowLegend;
	}

	/**
	 *
	 */
	public JRFont getTitleFont()
	{
		return titleFont;
	}

	/**
	 *
	 */
	public EdgeEnum getTitlePositionValue()
	{
		return titlePositionValue;
	}

	/**
	 *
	 */
	public void setTitlePosition(EdgeEnum titlePositionValue)
	{
		this.titlePositionValue = titlePositionValue;
	}

	/**
	 *
	 */
	public Color getTitleColor()
	{
		return titleColor;
	}

	/**
	 *
	 */
	public void setTitleColor(Color titleColor)
	{
		this.titleColor = titleColor;
	}

	/**
	 *
	 */
	public JRFont getSubtitleFont()
	{
		return subtitleFont;
	}

	/**
	 *
	 */
	public Color getSubtitleColor()
	{
		return subtitleColor;
	}

	/**
	 *
	 */
	public void setSubtitleColor(Color subtitleColor)
	{
		this.subtitleColor = subtitleColor;
	}

	/**
	 *
	 */
	public Color getLegendBackgroundColor() {
		return legendBackgroundColor;
	}

	/**
	 *
	 */
	public Color getLegendColor() {
		return legendColor;
	}

	/**
	 *
	 */
	public JRFont getLegendFont() {
		return legendFont;
	}

	/**
	 *
	 */
	public void setLegendBackgroundColor(Color legendBackgroundColor) {
		this.legendBackgroundColor = legendBackgroundColor;
	}

	/**
	 *
	 */
	public void setLegendColor(Color legendColor) {
		this.legendColor = legendColor;
	}

	/**
	 *
	 */
	public EdgeEnum getLegendPositionValue()
	{
		return legendPositionValue;
	}

	/**
	 *
	 */
	public void setLegendPosition(EdgeEnum legendPositionValue)
	{
		this.legendPositionValue = legendPositionValue;
	}

	/**
	 * @deprecated Replaced by {@link #getHyperlinkTypeValue()}.
	 */
	public byte getHyperlinkType()
	{
		return getHyperlinkTypeValue().getValue();
	}
		
	/**
	 *
	 */
	public HyperlinkTypeEnum getHyperlinkTypeValue()
	{
		return JRHyperlinkHelper.getHyperlinkTypeValue(this);
	}
		
	/**
	 *
	 */
	public byte getHyperlinkTarget()
	{
		return JRHyperlinkHelper.getHyperlinkTarget(this);
	}
		
	/**
	 *
	 */
	public JRExpression getTitleExpression()
	{
		return titleExpression;
	}

	/**
	 *
	 */
	public JRExpression getSubtitleExpression()
	{
		return subtitleExpression;
	}

	/**
	 *
	 */
	public JRExpression getAnchorNameExpression()
	{
		return anchorNameExpression;
	}

	/**
	 *
	 */
	public JRExpression getHyperlinkReferenceExpression()
	{
		return hyperlinkReferenceExpression;
	}

	/**
	 *
	 */
	public JRExpression getHyperlinkAnchorExpression()
	{
		return hyperlinkAnchorExpression;
	}

	/**
	 *
	 */
	public JRExpression getHyperlinkPageExpression()
	{
		return hyperlinkPageExpression;
	}
	
	public byte getChartType()
	{
		return chartType;
	}

	/**
	 *
	 */
	public String getRenderType()
	{
		return renderType;
	}

	/**
	 *
	 */
	public void setRenderType(String renderType)
	{
		this.renderType = renderType;
	}

	public int getBookmarkLevel()
	{
		return bookmarkLevel;
	}

	/**
	 *
	 */
	public void setBookmarkLevel(int bookmarkLevel)
	{
		this.bookmarkLevel = bookmarkLevel;
	}


	public String getLinkType()
	{
		return linkType;
	}
	
	public String getLinkTarget()
	{
		return linkTarget;
	}


	public JRHyperlinkParameter[] getHyperlinkParameters()
	{
		return hyperlinkParameters;
	}
	
	
	public JRExpression getHyperlinkTooltipExpression()
	{
		return hyperlinkTooltipExpression;
	}


	/**
	 * @param chartType the chartType to set
	 */
	public void setChartType(byte chartType) {
		this.chartType = chartType;
	}
	/**
	 * @param linkType the linkType to set
	 */
	public void setLinkType(String linkType) {
		this.linkType = linkType;
	}
	/**
	 * @param linkTarget the linkTarget to set
	 */
	public void setLinkTarget(String linkTarget) {
		this.linkTarget = linkTarget;
	}

	/**
	 * @param titleFont the titleFont to set
	 */
	public void setTitleFont(JRFont titleFont) {
		this.titleFont = titleFont;
	}
	/**
	 * @param subtitleFont the subtitleFont to set
	 */
	public void setSubtitleFont(JRFont subtitleFont) {
		this.subtitleFont = subtitleFont;
	}
	/**
	 * @param legendFont the legendFont to set
	 */
	public void setLegendFont(JRFont legendFont) {
		this.legendFont = legendFont;
	}

	/**
	 * @param hyperlinkParameters the hyperlinkParameters to set
	 */
	public void setHyperlinkParameters(JRHyperlinkParameter[] hyperlinkParameters) {
		this.hyperlinkParameters = hyperlinkParameters;
	}


	/**
	 * @param legendPositionValue the legendPositionValue to set
	 */
	public void setLegendPositionValue(EdgeEnum legendPositionValue) {
		this.legendPositionValue = legendPositionValue;
	}


	/**
	 * @param titlePositionValue the titlePositionValue to set
	 */
	public void setTitlePositionValue(EdgeEnum titlePositionValue) {
		this.titlePositionValue = titlePositionValue;
	}


	/**
	 * @param titleExpression the titleExpression to set
	 */
	public void setTitleExpression(JRExpression titleExpression) {
		this.titleExpression = titleExpression;
	}


	/**
	 * @param subtitleExpression the subtitleExpression to set
	 */
	public void setSubtitleExpression(JRExpression subtitleExpression) {
		this.subtitleExpression = subtitleExpression;
	}


	/**
	 * @param anchorNameExpression the anchorNameExpression to set
	 */
	public void setAnchorNameExpression(JRExpression anchorNameExpression) {
		this.anchorNameExpression = anchorNameExpression;
	}


	/**
	 * @param hyperlinkReferenceExpression the hyperlinkReferenceExpression to set
	 */
	public void setHyperlinkReferenceExpression(
			JRExpression hyperlinkReferenceExpression) {
		this.hyperlinkReferenceExpression = hyperlinkReferenceExpression;
	}


	/**
	 * @param hyperlinkAnchorExpression the hyperlinkAnchorExpression to set
	 */
	public void setHyperlinkAnchorExpression(JRExpression hyperlinkAnchorExpression) {
		this.hyperlinkAnchorExpression = hyperlinkAnchorExpression;
	}


	/**
	 * @param hyperlinkPageExpression the hyperlinkPageExpression to set
	 */
	public void setHyperlinkPageExpression(JRExpression hyperlinkPageExpression) {
		this.hyperlinkPageExpression = hyperlinkPageExpression;
	}


	/**
	 * @param hyperlinkTooltipExpression the hyperlinkTooltipExpression to set
	 */
	public void setHyperlinkTooltipExpression(
			JRExpression hyperlinkTooltipExpression) {
		this.hyperlinkTooltipExpression = hyperlinkTooltipExpression;
	}

	public void collectExpressions(JRExpressionCollector collector)
	{
		SpiderChartCompiler.collectExpressions(this, collector);
	}

	public Object clone() 
	{
		try
		{
			StandardChartSettings clone = (StandardChartSettings)super.clone();
			
			if (hyperlinkParameters != null)
			{
				clone.hyperlinkParameters = new JRHyperlinkParameter[hyperlinkParameters.length];
				for(int i = 0; i < hyperlinkParameters.length; i++)
				{
					clone.hyperlinkParameters[i] = (JRHyperlinkParameter)hyperlinkParameters[i].clone();
				}
			}
	
			if (titleExpression != null)
			{
				clone.titleExpression = (JRExpression)titleExpression.clone();
			}
			if (subtitleExpression != null)
			{
				clone.subtitleExpression = (JRExpression)subtitleExpression.clone();
			}
			if (anchorNameExpression != null)
			{
				clone.anchorNameExpression = (JRExpression)anchorNameExpression.clone();
			}
			if (hyperlinkReferenceExpression != null)
			{
				clone.hyperlinkReferenceExpression = (JRExpression)hyperlinkReferenceExpression.clone();
			}
			if (hyperlinkAnchorExpression != null)
			{
				clone.hyperlinkAnchorExpression = (JRExpression)hyperlinkAnchorExpression.clone();
			}
			if (hyperlinkPageExpression != null)
			{
				clone.hyperlinkPageExpression = (JRExpression)hyperlinkPageExpression.clone();
			}
			if (hyperlinkTooltipExpression != null)
			{
				clone.hyperlinkTooltipExpression = (JRExpression)hyperlinkTooltipExpression.clone();
			}
			
			return clone;
		}
		catch (CloneNotSupportedException e)
		{
			throw new JRRuntimeException(e);
		}

		
	}

	
}
