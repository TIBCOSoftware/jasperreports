package net.sf.jasperreports.charts.util;

import java.awt.Color;
import java.io.Serializable;

import net.sf.jasperreports.engine.JRConstants;
import net.sf.jasperreports.engine.JRFont;

/**
 * Represents all the formatting options of a chart axis.  The axis can be either a domain or
 * a range axis, and any options that do not apply to the current axis are simply ignored.
 * 
 * @author Barry Klawans (bklawans@users.sourceforge.net)
 *
 */
public class JRAxisFormat implements Serializable {
	
	private static final long serialVersionUID = JRConstants.SERIAL_VERSION_UID;
	
	/**
	 * The color to use when writing the label of the axis.
	 */
	protected Color labelColor = null;

	/**
	 * The font to use when writing the label of the axis.
	 */
	protected JRFont labelFont = null;

	/**
	 * The color to use when writing the label of each tick mark.  Ignored if tick marks are
	 * disabled.
	 */
	protected Color tickLabelColor = null;
	
	/**
	 * The font to use when writing the label of each tick mark.  Ignored if tick marks
	 * are disabled.
	 */
	protected JRFont tickLabelFont = null;

	/**
	 * The mask to use for formatting the label of each tick mark.  Ignored if tick marks
	 * are disabled, or if the axis being formatted is not either numeric or a date axis.
	 */
	protected String tickLabelMask = null;

	/**
	 * The color to use when drawing the axis line and tick marks, if enabled.
	 */
	protected Color lineColor = null;
	
	/**
	 * Constructor.
	 *
	 */
	public JRAxisFormat() {}
	
	/**
	 * Returns the color used when writing the label of the axis.
	 * 
	 * @return the color used when writing the label of the axis
	 */
	public Color getLabelColor()
	{
		return labelColor;
	}
	
	/**
	 * Sets the color used when writing the label of the axis.
	 * 
	 * @param labelColor the color to use when writing the label of the axis
	 */
	public void setLabelColor(Color labelColor)
	{
		this.labelColor = labelColor;
	}
	
	/**
	 * Returns the font used when writing the label of the axis.
	 * 
	 * @return the font used when writing the label of the axis
	 */
	public JRFont getLabelFont()
	{
		return labelFont;
	}
	
	/**
	 * Sets the font used when writing the label of the axis.
	 * 
	 * @param labelFont the font to use when writing the label of the axis
	 */
	public void setLabelFont(JRFont labelFont)
	{
		this.labelFont = labelFont;
	}

	/**
	 * Returns the color used when drawing the axis.  This color is used for both
	 * the axis line itself and any tick marks present on the axis.
	 * 
	 * @return the color used when drawing the axis.
	 */
	public Color getLineColor() {
		return lineColor;
	}

	/**
	 * Sets the color used when drawing the axis.  This color is used for both
	 * the axis line itself and any tick marks present on the axis.
	 * 
	 * @param lineColor the color to use when drawing the axis.
	 */
	public void setLineColor(Color lineColor) {
		this.lineColor = lineColor;
	}

	/**
	 * Returns the color used when writing the label of each tick mark.
	 * 
	 * @return the color used when writing the label of each tick mark
	 */
	public Color getTickLabelColor() {
		return tickLabelColor;
	}

	/**
	 * Sets the color to use when writing the label of each tick mark.
	 * 
	 * @param tickLabelColor the color to use when writing the label of each tick mark
	 */
	public void setTickLabelColor(Color tickLabelColor) {
		this.tickLabelColor = tickLabelColor;
	}

	/**
	 * Returns the font used when writing the label of each tick mark.
	 * 
	 * @return the font used when writing the label of each tick mark
	 */
	public JRFont getTickLabelFont() {
		return tickLabelFont;
	}

	/**
	 * Sets the font to use when writing the label of each tick mark.
	 * 
	 * @param tickLabelFont the font to use when writing the label of each tick mark
	 */
	public void setTickLabelFont(JRFont tickLabelFont) {
		this.tickLabelFont = tickLabelFont;
	}

	/**
	 * Returns the formatting mask used when writing the label of each tick mark.
	 * 
	 * @return the formatting mask used when writing the label of each tick mark
	 */
	public String getTickLabelMask() {
		return tickLabelMask;
	}

	/**
	 * Sets the formatting mask to user when writing the label of each tick mark.
	 * 
	 * @param mask the formatting mask to use when writing the label of each tick mark
	 */
	public void setTickLabelMask(String mask) {
		this.tickLabelMask = mask;
	}
}
