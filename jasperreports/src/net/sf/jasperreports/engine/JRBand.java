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

import net.sf.jasperreports.engine.type.SplitTypeEnum;
import net.sf.jasperreports.engine.util.JRProperties;


/**
 * Implementations of this interface represent various bands in the report template. A report can contain the following
 * bands: background, title, summary, page header, page footer, last page footer, column header and column footer.
 * @see JRSection
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id$
 */
public interface JRBand extends JRElementGroup
{
	

	/**
	 * 
	 */
	public static final String PROPERTY_SPLIT_TYPE = JRProperties.PROPERTY_PREFIX + "band.split.type";

	/**
	 * The band is allowed to split, but never within its declared height. 
	 * This means the band splits only when its content stretches.
	 * @deprecated Replaced by {@link SplitTypeEnum#STRETCH}.
	 */
	public static final Byte SPLIT_TYPE_STRETCH = new Byte((byte)1);

	/**
	 * Prevents the band from splitting on first break attempt. 
	 * On subsequent pages/columns, the band is allowed to split, to avoid infinite loops.
	 * @deprecated Replaced by {@link SplitTypeEnum#PREVENT}.
	 */
	public static final Byte SPLIT_TYPE_PREVENT = new Byte((byte)2);

	/**
	 * The band is allowed to split anywhere, as early as needed, 
	 * but not before at least one element being printed on the current page/column.
	 * @deprecated Replaced by {@link SplitTypeEnum#IMMEDIATE}.
	 */
	public static final Byte SPLIT_TYPE_IMMEDIATE = new Byte((byte)3);


	/**
	 *
	 */
	public int getHeight();

	/**
	 * Specifies if the band can be split between two pages.
	 * @deprecated Replaced by {@link #getSplitType()}.
	 */
	public boolean isSplitAllowed();

	/**
	 * @deprecated Replaced by {@link #setSplitType(Byte)}.
	 */
	public void setSplitAllowed(boolean isSplitAllowed);

	/**
	 * Specifies the band split behavior.
	 * @deprecated Replaced by {@link #getSplitTypeValue()}.
	 */
	public Byte getSplitType();

	/**
	 * Specifies the band split behavior.
	 */
	public SplitTypeEnum getSplitTypeValue();

	/**
	 * @deprecated Replaced by {@link #setSplitType(SplitTypeEnum)}.
	 */
	public void setSplitType(Byte splitType);

	/**
	 *
	 */
	public void setSplitType(SplitTypeEnum splitType);

	/**
	 * Returns the boolean expression that specifies if the band will be displayed.
	 */
	public JRExpression getPrintWhenExpression();

		
}
