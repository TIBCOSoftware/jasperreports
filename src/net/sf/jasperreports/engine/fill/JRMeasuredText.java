/*
 * ============================================================================
 * GNU Lesser General Public License
 * ============================================================================
 *
 * JasperReports - Free Java report-generating library.
 * Copyright (C) 2001-2006 JasperSoft Corporation http://www.jaspersoft.com
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
 * 303 Second Street, Suite 450 North
 * San Francisco, CA 94107
 * http://www.jaspersoft.com
 */
package net.sf.jasperreports.engine.fill;

/**
 * Text measuring information as produced by a {@link JRTextMeasurer text measurer}.
 * 
 * @author Lucian Chirita (lucianc@users.sourceforge.net)
 * @version $Id: JRCrosstab.java 1741 2007-06-08 10:53:33Z lucianc $
 * @see JRTextMeasurer#measure(net.sf.jasperreports.engine.util.JRStyledText, String, int, int)
 */
public interface JRMeasuredText
{

	/**
	 * Returns the text leading offset.
	 * 
	 * @return the text leading offset
	 */
	float getLeadingOffset();

	/**
	 * Return the line spacing factor for the text.
	 * 
	 * @return the line spacing factor
	 */
	float getLineSpacingFactor();

	/**
	 * Returns the text's measure height.
	 * 
	 * @return the text's measure height
	 */
	float getTextHeight();

	/**
	 * Returns the offset up to which text fitted.
	 * 
	 * @return the offset up to which text fitted
	 */
	int getTextOffset();

	/**
	 * Returns whether the text was determined to be left to right or not.
	 * 
	 * @return whether the text was determined to be left to right
	 */
	boolean isLeftToRight();

	/**
	 * Returns the suffix that was appended to the text
	 * (after {@link #getTextOffset()}).
	 * 
	 * @return the suffix that was appended to the text
	 */
	String getTextSuffix();
	
}
