/*
 * ============================================================================
 * GNU Lesser General Public License
 * ============================================================================
 *
 * JasperReports - Free Java report-generating library.
 * Copyright (C) 2001-2005 JasperSoft Corporation http://www.jaspersoft.com
 * 
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307, USA.
 * 
 * JasperSoft Corporation
 * 185, Berry Street, Suite 6200
 * San Francisco CA 94107
 * http://www.jaspersoft.com
 */
package net.sf.jasperreports.engine;


/**
 * An abstract representation of a graphic element representing an image. Images can be aligned and scaled. They can
 * also contain hyperlinks or be anchors for other hyperlinks.
 *
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id$
 */
public interface JRImage extends JRGraphicElement, JRAnchor, JRHyperlink, JRAlignment, JRBox
{


	/**
	 * A constant value specifying that if the actual image is larger than the image element size, it will be cut off so
	 * that it keeps its original resolution, and only the region that fits the specified size will be displayed.
	 */
	public static final byte SCALE_IMAGE_CLIP = 1;

	/**
	 * A constant value specifying that if the dimensions of the actual image do not fit those specified for the
	 * image element that displays it, the image can be forced to obey them and stretch itself so that it fits
	 * in the designated output area.
	 */
	public static final byte SCALE_IMAGE_FILL_FRAME = 2;

	/**
	 * A constant value specifying that if the actual image does not fit into the image element, it can be adapted
	 * to those dimensions without needing to change its original proportions.
	 */
	public static final byte SCALE_IMAGE_RETAIN_SHAPE = 3;


	/**
	 * A constant used for specifying that the engine should raise an exception if the image is not found.
	 */
	public static final byte ON_ERROR_TYPE_ERROR = 1;

	/**
	 * A constant used for specifying that the engine should display blank space if the image is not found.
	 */
	public static final byte ON_ERROR_TYPE_BLANK = 2;

	/**
	 * A constant used for specifying that the engine should display a replacement icon if the image is not found.
	 */
	public static final byte ON_ERROR_TYPE_ICON = 3;
	

	/**
	 * Gets the image scale type.
	 * @return one of the scale constants in this class
	 */
	public byte getScaleImage();

	public Byte getOwnScaleImage();

	/**
	 * Sets the image scale type.
	 * @param scaleImage one of the scale constants in this class
	 */
	public void setScaleImage(byte scaleImage);

	/**
	 * Gets the horizontal alignment of the element.
	 * @return one of the alignment values defined in {@link JRAlignment}
	 */
	public byte getHorizontalAlignment();

	public Byte getOwnHorizontalAlignment();

	/**
	 * Sets the horizontal alignment of the element.
	 * @param horizontalAlignment one of the alignment values defined in {@link JRAlignment}
	 */
	public void setHorizontalAlignment(byte horizontalAlignment);

	/**
	 * Gets the vertical alignment of the element.
	 * @return one of the alignment values defined in {@link JRAlignment}
	 */
	public byte getVerticalAlignment();
		
	public Byte getOwnVerticalAlignment();

	/**
	 * Sets the vertical alignment of the element.
	 * @param verticalAlignment one of the alignment values defined in {@link JRAlignment}
	 */
	public void setVerticalAlignment(byte verticalAlignment);
		
	/**
	 * Indicates if the engine is loading the current image from cache.
	 * Implementations of this method rely on default values that depend on the type of the image expression
	 * if a value was not explicitly set of this flag.
	 * @return true if the image should be loaded from cache, false otherwise
	 */
	public boolean isUsingCache();

	/**
	 * Indicates if the engine is loading the current image from cache.
	 * Implementations of this method return the actual value for the internal flag that was explicitly 
	 * set on this image element.
	 * @return Boolean.TRUE if the image should be loaded from cache, Boolean.FALSE otherwise 
	 * or null in case the flag was never explicitly set on this image element
	 */
	public Boolean isOwnUsingCache();

	/**
	 * Specifies if the engine should be loading the current image from cache. If set to true, the reporting engine
	 * will try to recognize previously loaded images using their specified source. For example, it will recognize
	 * an image if the image source is a file name that it has already loaded, or if it is the same URL.
	 * <p>
	 * For image elements that have expressions returning <tt>java.lang.String</tt> objects as the image source, 
	 * representing file names, URLs or classpath resources, the default value for this flag is true. 
	 */
	public void setUsingCache(boolean isUsingCache);

	/**
	 * Specifies if the engine should be loading the current image from cache. If set to Boolean.TRUE, the reporting engine
	 * will try to recognize previously loaded images using their specified source. For example, it will recognize
	 * an image if the image source is a file name that it has already loaded, or if it is the same URL.
	 * <p>
	 * If set to null, the engine will rely on some default value which depends on the type of the image expression.
	 * The cache is turned on by default only for images that have <tt>java.lang.String</tt> objects in their expressions.
	 */
	public void setUsingCache(Boolean isUsingCache);

	/**
	 * Indicates if the images will be loaded lazily or not.
	 */
	public boolean isLazy();

	/**
	 * Gives control over when the images are retrieved from their specified location. If set to true, the image is
	 * loaded from the specified location only when the document is viewed or exported to other formats. Otherwise
	 * it is loaded during the report filling process and stored in the resulting document.
	 * @param isLazy specifies whether
	 */
	public void setLazy(boolean isLazy);

	/**
	 * Indicates how the engine will treat a missing image.
	 * @return one of the constants for missing image error types
	 */
	public byte getOnErrorType();

	/**
	 * Specifies how the engine should treat a missing image.
	 * @param onErrorType one of the constants for missing image error types
	 */
	public void setOnErrorType(byte onErrorType);

	/**
	 * Indicates the evaluation time for this image.
	 * @return one of the evaluation time constants in {@link JRExpression}
	 */
	public byte getEvaluationTime();
		
	/**
	 * Indicates the evaluation time for this image.
	 * @return one of the evaluation time constants in {@link JRExpression}
	 */
	public JRGroup getEvaluationGroup();
		
	/**
	 * Gets the evaluation group for this text field. Used only when evaluation time is group.
	 * @see JRExpression#EVALUATION_TIME_GROUP
	 */
	public JRExpression getExpression();
	
	/**
	 * Returns an object containing all border and padding properties for this text element
	 * @deprecated
	 */
	public JRBox getBox();


}
