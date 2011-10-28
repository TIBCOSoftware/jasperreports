/*
 * JasperReports - Free Java Reporting Library.
 * Copyright (C) 2001 - 2011 Jaspersoft Corporation. All rights reserved.
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

import net.sf.jasperreports.engine.type.HyperlinkTargetEnum;
import net.sf.jasperreports.engine.type.HyperlinkTypeEnum;


/**
 * An interface providing hyperlink functionality. It must be implemented by elements that can contain hyperlinks.
 *
 * There are three types of hyperlinks: reference, anchor and page. The reference type just points to an external resource.
 * The anchor type can point to an anchor in the current document or inside an external referenced document. In the latter
 * case, users have to specify both an anchor expression and a reference expression. The page type can point to the
 * beginning of a specific page in the current document or an external document (in the same way that anchor type does).
 *
 * @see JRAnchor
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id$
 */
public interface JRHyperlink extends JRCloneable
{


	/**
	 * @deprecated Replaced by {@link #getHyperlinkTypeValue()}.
	 */
	public byte getHyperlinkType();


	/**
	 * Retrieves the hyperlink type for the element.
	 * <p>
	 * The actual hyperlink type is determined by {@link #getLinkType() getLinkType()}.
	 * This method can is used to determine whether the hyperlink type is one of the
	 * built-in types or a custom type. 
	 * When hyperlink is of custom type, {@link HyperlinkTypeEnum#CUSTOM CUSTOM} is returned.
	 * </p>
	 * @return one of the hyperlink type constants
	 * @see #getLinkType()
	 */
	public HyperlinkTypeEnum getHyperlinkTypeValue();


	/**
	 * Retrieves the hyperlink target for the element.
	 * <p>
	 * The actual hyperlink target is determined by {@link #getLinkTarget() getLinkTarget()}.
	 * This method can is used to determine whether the hyperlink target is one of the
	 * built-in target names or a custom one. 
	 * When hyperlink has a custom target name, {@link HyperlinkTargetEnum#CUSTOM} is returned.
	 * </p>
	 * @return one of the hyperlink target constants
	 * @see #getLinkTarget()
	 */
	public byte getHyperlinkTarget();


	/**
	 * Returns the expression whose value represents the hyperlink reference. It is only used when the hyperlink type is
	 * reference or anchor
	 */
	public JRExpression getHyperlinkReferenceExpression();


	/**
	 * Returns the expression whose value represents the anchor. It is only used when the hyperlink type is anchor.
	 */
	public JRExpression getHyperlinkAnchorExpression();


	/**
	 * Returns an integer representing the page index of the link. It is only used when the hyperlink type is page.
	 * If the expression does not evaluate to an integer, an exception will be thrown.
	 */
	public JRExpression getHyperlinkPageExpression();

	
	/**
	 * Returns the hyperlink type.
	 * <p>
	 * The type can be one of the built-in types
	 * (Reference, LocalAnchor, LocalPage, RemoteAnchor, RemotePage),
	 * or can be an arbitrary type.
	 * </p>
	 * @return the hyperlink type
	 */
	public String getLinkType();
	
	/**
	 * Returns the hyperlink target name.
	 * <p>
	 * The type can be one of the built-in names
	 * (Self, Blank, Top, Parent),
	 * or can be an arbitrary name.
	 * </p>
	 * @return the hyperlink target name
	 */
	public String getLinkTarget();
	
	
	/**
	 * Returns the list of hyperlink parameters.
	 * <p>
	 * The parameters can be used by custom hyperlink types to generate
	 * dynamic links.
	 * </p>
	 * @return the list of hyperlink parameters
	 */
	public JRHyperlinkParameter[] getHyperlinkParameters();
	
	
	/**
	 * Returns the expression which will generate the hyperlink tooltip.
	 * 
	 * @return the expression which will generate the hyperlink tooltip
	 */
	public JRExpression getHyperlinkTooltipExpression();

}
