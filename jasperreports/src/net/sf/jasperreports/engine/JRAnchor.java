/*
 * JasperReports - Free Java Reporting Library.
 * Copyright (C) 2001 - 2014 TIBCO Software Inc. All rights reserved.
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


/**
 * An interface providing anchor functionality. It must be implemented by elements that can contain anchors for
 * hyperlinks.
 * <p/>
 * If present in a text field or image element declaration, the <code>&lt;anchorNameExpression&gt;</code>
 * tag (see {@link #getAnchorNameExpression()}) transforms that particular text field or image into a 
 * local anchor of the resulting document, to which hyperlinks can point. The anchor will bear the 
 * name returned after evaluation of the anchor name expression, which should always return
 * <code>java.lang.String</code> values.
 * 
 * @see net.sf.jasperreports.engine.JRHyperlink
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 */
public interface JRAnchor
{
	/**
	 * Value of the bookmark level that indicates that no bookmark should be created for the anchor.
	 */
	public static final int NO_BOOKMARK = 0;
	
	/**
	 * Returns an expression whose value represents the anchor name.
	 */
	public JRExpression getAnchorNameExpression();

	
	/**
	 * Returns the level of the bookmark corresponding to the anchor.
	 * 
	 * @return the level of the bookmark corresponding to the anchor (starting from 1)
	 * or {@link #NO_BOOKMARK NO_BOOKMARK} if no bookmark should be created for this anchor
	 */
	public int getBookmarkLevel();

}
