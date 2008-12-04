package net.sf.jasperreports.charts.themes;

import java.awt.Color;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Paint;
import java.util.ArrayList;
import java.util.List;

import net.sf.jasperreports.engine.JRConstants;

public interface ChartThemesConstants
{
	static final long serialVersionUID = JRConstants.SERIAL_VERSION_UID;
	
	//general properties
	public static final Paint TRANSPARENT_PAINT = new Color(0, 0, 0, 0);
	
	public static final Integer FONT_PLAIN_STYLE = new Integer(Font.PLAIN);
	public static final Integer FONT_BOLD_STYLE = new Integer(Font.BOLD);
	public static final Integer FONT_ITALIC_STYLE = new Integer(Font.ITALIC);

	public static final List EYE_CANDY_SIXTIES_COLORS =
		new ArrayList(){{
			add(new Color(250, 97, 18));
			add(new Color(237, 38, 42));
			add(new Color(0, 111, 60));
			add(new Color(250, 223, 18));
			add(new Color(47, 137, 187));
			add(new Color(231, 133, 35));
			add(new Color(229, 1, 140));
			add(new Color(234, 171, 53));
			}};
	
	public static final List EYE_CANDY_SIXTIES_GRADIENT_PAINTS = new ArrayList(){{
			add(new GradientPaint(0f, 0f, (Color)EYE_CANDY_SIXTIES_COLORS.get(0), 0f, 0f, ((Color)EYE_CANDY_SIXTIES_COLORS.get(0)).darker()));
			add(new GradientPaint(0f, 0f, (Color)EYE_CANDY_SIXTIES_COLORS.get(1), 0f, 0f, ((Color)EYE_CANDY_SIXTIES_COLORS.get(1)).darker()));
			add(new GradientPaint(0f, 0f, (Color)EYE_CANDY_SIXTIES_COLORS.get(2), 0f, 0f, ((Color)EYE_CANDY_SIXTIES_COLORS.get(2)).darker()));
			add(new GradientPaint(0f, 0f, (Color)EYE_CANDY_SIXTIES_COLORS.get(3), 0f, 0f, ((Color)EYE_CANDY_SIXTIES_COLORS.get(3)).darker()));
			add(new GradientPaint(0f, 0f, (Color)EYE_CANDY_SIXTIES_COLORS.get(4), 0f, 0f, ((Color)EYE_CANDY_SIXTIES_COLORS.get(4)).darker()));
			add(new GradientPaint(0f, 0f, (Color)EYE_CANDY_SIXTIES_COLORS.get(5), 0f, 0f, ((Color)EYE_CANDY_SIXTIES_COLORS.get(5)).darker()));
			add(new GradientPaint(0f, 0f, (Color)EYE_CANDY_SIXTIES_COLORS.get(6), 0f, 0f, ((Color)EYE_CANDY_SIXTIES_COLORS.get(6)).darker()));
    }};

    // generic chart default properties names
	public static final String DEFAULT_BACKGROUND_PAINT = "defaultBackgroundPaint";
	public static final String DEFAULT_SERIES_COLORS = "defaultSeriesColors";
	public static final String DEFAULT_SERIES_GRADIENT_PAINTS = "defaultSeriesGradientPaints";
	public static final String DEFAULT_CHART_BORDER_PAINT = "defaultChartBorderPaint";
	public static final String DEFAULT_CHART_BORDER_STROKE = "defaultChartBorderStroke";
	public static final String DEFAULT_CHART_BORDER_VISIBLE = "defaultChartBorderVisible";
	
	public static final String DEFAULT_TITLE_POSITION = "defaultTitlePosition";
	public static final String DEFAULT_TITLE_BASEFONT_BOLD_STYLE = "defaultTitleBaseFontBoldStyle";
	public static final String DEFAULT_TITLE_BASEFONT_ITALIC_STYLE = "defaultTitleBaseFontItalicStyle";
	public static final String DEFAULT_TITLE_FORECOLOR = "defaultTitleForecolor";
	public static final String DEFAULT_TITLE_BACKCOLOR = "defaultTitleBackcolor";
	public static final String DEFAULT_TITLE_BASEFONT_SIZE = "defaultTitleBaseFontSize";
	
	public static final String DEFAULT_SUBTITLE_POSITION = "defaultSubtitlePosition";
	public static final String DEFAULT_SUBTITLE_BASEFONT_BOLD_STYLE = "defaultSubtitleBaseFontBoldStyle";
	public static final String DEFAULT_SUBTITLE_BASEFONT_ITALIC_STYLE = "defaultSubtitleBaseFontItalicStyle";
	public static final String DEFAULT_SUBTITLE_FORECOLOR = "defaultSubtitleForecolor";
	public static final String DEFAULT_SUBTITLE_BACKCOLOR = "defaultSubtitleBackcolor";
	public static final String DEFAULT_SUBTITLE_BASEFONT_SIZE = "defaultSubtitleBaseFontSize";
	
	public static final String DEFAULT_LEGEND_POSITION = "defaultLegendPosition";
	public static final String DEFAULT_LEGEND_BASEFONT_BOLD_STYLE = "defaultLegendBaseFontBoldStyle";
	public static final String DEFAULT_LEGEND_BASEFONT_ITALIC_STYLE = "defaultLegendBaseFontItalicStyle";
	public static final String DEFAULT_LEGEND_FORECOLOR = "defaultLegendForecolor";
	public static final String DEFAULT_LEGEND_BACKCOLOR = "defaultLegendBackcolor";
	public static final String DEFAULT_LEGEND_BASEFONT_SIZE = "defaultLegendBaseFontSize";
	
	public static final String DEFAULT_BASEFONT_SIZE = "defaultBaseFontSize";
	
	// generic plot default properties names
	public static final String DEFAULT_PLOT_BACKGROUND_ALPHA = "defaultPlotBackgroundAlpha";
	public static final String DEFAULT_PLOT_FOREGROUND_ALPHA = "defaultPlotForegroundAlpha";
	public static final String DEFAULT_PLOT_LABEL_ROTATION = "defaultPlotLabelRotation";
	public static final String DEFAULT_PLOT_ORIENTATION = "defaultPlotOrientation";
}
