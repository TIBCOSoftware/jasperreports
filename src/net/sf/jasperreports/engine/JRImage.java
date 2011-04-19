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
package net.sf.jasperreports.engine;

import net.sf.jasperreports.engine.type.EvaluationTimeEnum;
import net.sf.jasperreports.engine.type.OnErrorTypeEnum;
import net.sf.jasperreports.engine.type.ScaleImageEnum;


/**
 * An abstract representation of a graphic element representing an image. Images can be aligned and scaled. They can
 * also contain hyperlinks or be anchors for other hyperlinks.
 *
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id$
 */
public interface JRImage extends JRGraphicElement, JRAnchor, JRHyperlink, JRAlignment, JRCommonImage
{


	/**
	 * @deprecated Replaced by {@link ScaleImageEnum#CLIP}.
	 */
	public static final byte SCALE_IMAGE_CLIP = 1;

	/**
	 * @deprecated Replaced by {@link ScaleImageEnum#FILL_FRAME}.
	 */
	public static final byte SCALE_IMAGE_FILL_FRAME = 2;

	/**
	 * @deprecated Replaced by {@link ScaleImageEnum#RETAIN_SHAPE}.
	 */
	public static final byte SCALE_IMAGE_RETAIN_SHAPE = 3;
	
	/**
	 * @deprecated Replaced by {@link ScaleImageEnum#REAL_HEIGHT}.
	 */
	public static final byte SCALE_IMAGE_REAL_HEIGHT = 4;
	
	/**
	 * @deprecated Replaced by {@link ScaleImageEnum#REAL_SIZE}.
	 */
	public static final byte SCALE_IMAGE_REAL_SIZE = 5;


	/**
	 * @deprecated Replaced by {@link OnErrorTypeEnum#ERROR}.
	 */
	public static final byte ON_ERROR_TYPE_ERROR = 1;

	/**
	 * @deprecated Replaced by {@link OnErrorTypeEnum#BLANK}.
	 */
	public static final byte ON_ERROR_TYPE_BLANK = 2;

	/**
	 * @deprecated Replaced by {@link OnErrorTypeEnum#ICON}.
	 */
	public static final byte ON_ERROR_TYPE_ICON = 3;
	

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
	 * @deprecated Replaced by {@link #getOnErrorTypeValue()}.
	 */
	public byte getOnErrorType();

	/**
	 * @deprecated Replaced by {@link #setOnErrorType(OnErrorTypeEnum)}.
	 */
	public void setOnErrorType(byte onErrorType);

	/**
	 * Indicates how the engine will treat a missing image.
	 * @return a value representing one of the missing image handling constants in {@link OnErrorTypeEnum}
	 */
	public OnErrorTypeEnum getOnErrorTypeValue();
	
	/**
	 * Specifies how the engine should treat a missing image.
	 * @param onErrorTypeEnum a value representing one of the missing image handling constants in {@link OnErrorTypeEnum}
	 */
	public void setOnErrorType(OnErrorTypeEnum onErrorTypeEnum);
	
	/**
	 * @deprecated Replaced by {@link #getEvaluationTimeValue()}.
	 */
	public byte getEvaluationTime();
		
	/**
	 * Indicates the evaluation time for this image.
	 * @return one of the evaluation time constants in {@link EvaluationTimeEnum}
	 */
	public EvaluationTimeEnum getEvaluationTimeValue();
		
	/**
	 * Gets the evaluation group for this text field. Used only when evaluation time is group.
	 * @see EvaluationTimeEnum#GROUP
	 */
	public JRGroup getEvaluationGroup();
		
	/**
	 * 
	 */
	public JRExpression getExpression();

}
