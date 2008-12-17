package net.sf.jasperreports.charts.themes;

import java.awt.Color;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Paint;
import java.util.ArrayList;
import java.util.List;

import org.jfree.chart.plot.DefaultDrawingSupplier;

import net.sf.jasperreports.engine.JRConstants;

public interface ChartThemesConstants
{
	static final long serialVersionUID = JRConstants.SERIAL_VERSION_UID;
	
	//general properties
	public static final Paint TRANSPARENT_PAINT = new Color(0, 0, 0, 0);
	
	public static final Integer FONT_PLAIN_STYLE = new Integer(Font.PLAIN);
	public static final Integer FONT_BOLD_STYLE = new Integer(Font.BOLD);
	public static final Integer FONT_ITALIC_STYLE = new Integer(Font.ITALIC);

	public static final Paint AEGEAN_BORDER_PAINT = new Color(134,134,134);
	
	public static final List AEGEAN_INTERVAL_COLORS = new ArrayList(){{
		add(new Color(182, 0, 40));
		add(new Color(240, 205, 0));
		add(new Color(0,153,0));
		}};
	
	public static final List EYE_CANDY_SIXTIES_COLORS =	new ArrayList(){{
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

	public static final Paint EYE_CANDY_SIXTIES_GRIDLINE_PAINT = new Color(134,134,134);
    
    // generic chart default properties names
	public static final String DEFAULT_BACKGROUND_PAINT = "defaultBackgroundPaint";
	public static final String DEFAULT_BACKGROUND_IMAGE = "defaultBackgroundImage";
	public static final String DEFAULT_BACKGROUND_IMAGE_ALIGNMENT = "defaultBackgroundImageAlignment";
	public static final String DEFAULT_BACKGROUND_IMAGE_ALPHA = "defaultBackgroundImageAlpha";
	public static final String DEFAULT_SERIES_COLORS = "defaultSeriesColors";
	public static final String DEFAULT_BASEFONT_SIZE = "defaultBaseFontSize";
	public static final String DEFAULT_SERIES_GRADIENT_PAINTS = "defaultSeriesGradientPaints";
	public static final String DEFAULT_CHART_BORDER_PAINT = "defaultChartBorderPaint";
	public static final String DEFAULT_CHART_BORDER_STROKE = "defaultChartBorderStroke";
	public static final String DEFAULT_CHART_BORDER_VISIBLE = "defaultChartBorderVisible";
	public static final String DEFAULT_CHART_ANTI_ALIAS = "defaultChartAntiAlias";
	public static final String DEFAULT_TEXT_ANTI_ALIAS = "defaultTextAntiAlias";
	public static final String DEFAULT_CHART_PADDING = "defaultChartPadding";
	public static final String DEFAULT_UNIT_TYPE = "defaultUnitType";
	public static final String DEFAULT_RENDERING_HINTS = "defaultRenderingHints";
	public static final String DEFAULT_TITLE = "defaultTitle";

    // generic chart title default properties names
	public static final String DEFAULT_TITLE_VISIBLE = "defaultTitleVisible";
	public static final String DEFAULT_TITLE_POSITION = "defaultTitlePosition";
	public static final String DEFAULT_TITLE_BASEFONT_BOLD_STYLE = "defaultTitleBaseFontBoldStyle";
	public static final String DEFAULT_TITLE_BASEFONT_ITALIC_STYLE = "defaultTitleBaseFontItalicStyle";
	public static final String DEFAULT_TITLE_FORECOLOR = "defaultTitleForecolor";
	public static final String DEFAULT_TITLE_BACKCOLOR = "defaultTitleBackcolor";
	public static final String DEFAULT_TITLE_BASEFONT_SIZE = "defaultTitleBaseFontSize";
	public static final String DEFAULT_TITLE_HORIZONTAL_ALIGNMENT = "defaultTitleHorizontalAlignment";
	public static final String DEFAULT_TITLE_VERTICAL_ALIGNMENT = "defaultTitleVerticalAlignment";
	public static final String DEFAULT_TITLE_HEIGHT = "defaultTitleHeight";
	public static final String DEFAULT_TITLE_WIDTH = "defaultTitleWidth";
	public static final String DEFAULT_TITLE_BOUNDS = "defaultTitleBounds";
	public static final String DEFAULT_TITLE_FRAME = "defaultTitleFrame";
	public static final String DEFAULT_TITLE_MARGIN = "defaultTitleMargin";
	public static final String DEFAULT_TITLE_PADDING = "defaultTitlePadding";
	public static final String DEFAULT_TITLE_TEXT = "defaultTitleText";
	public static final String DEFAULT_TITLE_TEXT_ALIGNMENT = "defaultTitleTextAlignment";
	public static final String DEFAULT_TITLE_MAXIMUM_LINES_TO_DISPLAY = "defaultTitleMaximumLinesToDisplay";
	public static final String DEFAULT_TITLE_TOOLTIP_TEXT = "defaultTitleTooltipText";
	public static final String DEFAULT_TITLE_URL_TEXT = "defaultTitleURLText";
	public static final String DEFAULT_TITLE_EXPAND_TO_FIT_SPACE = "defaultTitleExpandToFitSpace";
	
    // generic chart subtitle default properties names
	public static final String DEFAULT_SUBTITLE_VISIBLE = "defaultSubtitleVisible";
	public static final String DEFAULT_SUBTITLE_POSITION = "defaultSubtitlePosition";
	public static final String DEFAULT_SUBTITLE_BASEFONT_BOLD_STYLE = "defaultSubtitleBaseFontBoldStyle";
	public static final String DEFAULT_SUBTITLE_BASEFONT_ITALIC_STYLE = "defaultSubtitleBaseFontItalicStyle";
	public static final String DEFAULT_SUBTITLE_FORECOLOR = "defaultSubtitleForecolor";
	public static final String DEFAULT_SUBTITLE_BACKCOLOR = "defaultSubtitleBackcolor";
	public static final String DEFAULT_SUBTITLE_BASEFONT_SIZE = "defaultSubtitleBaseFontSize";
	public static final String DEFAULT_SUBTITLE_HORIZONTAL_ALIGNMENT = "defaultSubtitleHorizontalAlignment";
	public static final String DEFAULT_SUBTITLE_VERTICAL_ALIGNMENT = "defaultSubtitleVerticalAlignment";
	public static final String DEFAULT_SUBTITLE_HEIGHT = "defaultSubtitleHeight";
	public static final String DEFAULT_SUBTITLE_WIDTH = "defaultSubtitleWidth";
	public static final String DEFAULT_SUBTITLE_BOUNDS = "defaultSubtitleBounds";
	public static final String DEFAULT_SUBTITLE_FRAME = "defaultSubtitleFrame";
	public static final String DEFAULT_SUBTITLE_MARGIN = "defaultSubtitleMargin";
	public static final String DEFAULT_SUBTITLE_PADDING = "defaultSubtitlePadding";
	public static final String DEFAULT_SUBTITLE_TEXT = "defaultSubtitleText";
	public static final String DEFAULT_SUBTITLE_TEXT_ALIGNMENT = "defaultSubtitleTextAlignment";
	public static final String DEFAULT_SUBTITLE_MAXIMUM_LINES_TO_DISPLAY = "defaultSubtitleMaximumLinesToDisplay";
	public static final String DEFAULT_SUBTITLE_TOOLTIP_TEXT = "defaultSubtitleTooltipText";
	public static final String DEFAULT_SUBTITLE_URL_TEXT = "defaultSubtitleURLText";
	public static final String DEFAULT_SUBTITLE_EXPAND_TO_FIT_SPACE = "defaultSubtitleExpandToFitSpace";
	
    // generic chart legend default properties names
	public static final String DEFAULT_LEGEND_VISIBLE = "defaultLegendVisible";
	public static final String DEFAULT_LEGEND_POSITION = "defaultLegendPosition";
	public static final String DEFAULT_LEGEND_BASEFONT_BOLD_STYLE = "defaultLegendBaseFontBoldStyle";
	public static final String DEFAULT_LEGEND_BASEFONT_ITALIC_STYLE = "defaultLegendBaseFontItalicStyle";
	public static final String DEFAULT_LEGEND_FORECOLOR = "defaultLegendForecolor";
	public static final String DEFAULT_LEGEND_BACKCOLOR = "defaultLegendBackcolor";
	public static final String DEFAULT_LEGEND_BASEFONT_SIZE = "defaultLegendBaseFontSize";
	public static final String DEFAULT_LEGEND_HORIZONTAL_ALIGNMENT = "defaultLegendHorizontalAlignment";
	public static final String DEFAULT_LEGEND_FRAME = "defaultLegendFrame";
	public static final String DEFAULT_LEGEND_VERTICAL_ALIGNMENT = "defaultLegendVerticalAlignment";
	public static final String DEFAULT_LEGEND_HEIGHT = "defaultLegendHeight";
	public static final String DEFAULT_LEGEND_WIDTH = "defaultLegendWidth";
	public static final String DEFAULT_LEGEND_BOUNDS = "defaultLegendBounds";
	public static final String DEFAULT_LEGEND_MARGIN = "defaultLegendMargin";
	public static final String DEFAULT_LEGEND_PADDING = "defaultLegendPadding";
	public static final String DEFAULT_LEGEND_ITEM_GRAPHIC_PADDING = "defaultLegendItemGraphicPadding";
	public static final String DEFAULT_LEGEND_ITEM_GRAPHIC_LOCATION = "defaultLegendItemGraphicLocation";
	public static final String DEFAULT_LEGEND_ITEM_GRAPHIC_EDGE = "defaultLegendItemGraphicEdge";
	public static final String DEFAULT_LEGEND_ITEM_GRAPHIC_ANCHOR = "defaultLegendItemGraphicAnchor";
	public static final String DEFAULT_LEGEND_ITEM_LABEL_PADDING = "defaultLegendItemLabelPadding";
	
	// generic plot default properties names
	public static final String DEFAULT_PLOT_BACKGROUND_PAINT = "defaultPlotBackgroundPaint";
	public static final String DEFAULT_PLOT_BACKGROUND_ALPHA = "defaultPlotBackgroundAlpha";
	public static final String DEFAULT_PLOT_FOREGROUND_ALPHA = "defaultPlotForegroundAlpha";
	public static final String DEFAULT_PLOT_BACKGROUND_IMAGE = "defaultPlotBackgroundImage";
	public static final String DEFAULT_PLOT_BACKGROUND_IMAGE_ALIGNMENT = "defaultPlotBackgroundImageAlignment";
	public static final String DEFAULT_PLOT_BACKGROUND_IMAGE_ALPHA = "defaultPlotBackgroundImageAlpha";
	public static final String DEFAULT_PLOT_OUTLINE_PAINT_SEQUENCE = "defaultPlotOutlinePaintSequence";
	public static final String DEFAULT_PLOT_STROKE_SEQUENCE = "defaultPlotStrokeSequence";
	public static final String DEFAULT_PLOT_OUTLINE_STROKE_SEQUENCE = "defaultPlotOutlineStrokeSequence";
	public static final String DEFAULT_PLOT_SHAPE_SEQUENCE = "defaultPlotShapeSequence";
	public static final String DEFAULT_PLOT_LABEL_ROTATION = "defaultPlotLabelRotation";
	public static final String DEFAULT_PLOT_ORIENTATION = "defaultPlotOrientation";
	public static final String DEFAULT_PLOT_INSETS = "defaultPlotInsets";
	public static final String DEFAULT_PLOT_OUTLINE_PAINT = "defaultPlotOutlinePaint";
	public static final String DEFAULT_PLOT_OUTLINE_STROKE = "defaultPlotOutlineStroke";
	public static final String DEFAULT_PLOT_OUTLINE_VISIBLE = "defaultPlotOutlineVisible";
	
	// generic axis default properties names
	public static final String DEFAULT_AXIS_VISIBLE = "defaultAxisVisible";
	public static final String DEFAULT_AXIS_LINE_PAINT = "defaultAxisLinePaint";
	public static final String DEFAULT_AXIS_LINE_STROKE = "defaultAxisLineStroke";
	public static final String DEFAULT_AXIS_LINE_VISIBLE = "defaultAxisLineVisible";
	public static final String DEFAULT_AXIS_FIXED_DIMENSION = "defaultAxisFixedDimension";
	public static final String DEFAULT_AXIS_LABEL = "defaultAxisLabel";
	public static final String DEFAULT_AXIS_LABEL_ANGLE = "defaultAxisLabelAngle";
	public static final String DEFAULT_AXIS_LABEL_PAINT = "defaultAxisLabelPaint";
	public static final String DEFAULT_AXIS_LABEL_FONT = "defaultAxisLabelFont";
	public static final String DEFAULT_AXIS_LABEL_FONT_BOLD_STYLE = "defaultAxisLabelFontBoldStyle";
	public static final String DEFAULT_AXIS_LABEL_FONT_ITALIC_STYLE = "defaultAxisLabelFontItalicStyle";
	public static final String DEFAULT_AXIS_LABEL_INSETS = "defaultAxisLabelInsets";
	public static final String DEFAULT_AXIS_LABEL_VISIBLE = "defaultAxisLabelVisible";
	public static final String DEFAULT_AXIS_TICK_LABEL_PAINT = "defaultAxisTickLabelPaint";
	public static final String DEFAULT_AXIS_TICK_LABEL_FONT = "defaultAxisTickLabelFont";
	public static final String DEFAULT_AXIS_TICK_LABEL_FONT_BOLD_STYLE = "defaultAxisTickLabelFontBoldStyle";
	public static final String DEFAULT_AXIS_TICK_LABEL_FONT_ITALIC_STYLE = "defaultAxisTickLabelFontItalicStyle";
	public static final String DEFAULT_AXIS_TICK_LABEL_INSETS = "defaultAxisTickLabelInsets";
	public static final String DEFAULT_AXIS_TICK_LABELS_VISIBLE = "defaultAxisTickLabelsVisible";
	public static final String DEFAULT_AXIS_TICK_MARKS_INSIDE_LENGTH = "defaultAxisTickMarksInsideLength";
	public static final String DEFAULT_AXIS_TICK_MARKS_OUTSIDE_LENGTH = "defaultAxisTickMarksOutsideLength";
	public static final String DEFAULT_AXIS_TICK_MARKS_PAINT = "defaultAxisTickMarksPaint";
	public static final String DEFAULT_AXIS_TICK_MARKS_STROKE = "defaultAxisTickMarksStroke";
	public static final String DEFAULT_AXIS_TICK_MARKS_VISIBLE = "defaultAxisTickMarksVisible";
	
}
