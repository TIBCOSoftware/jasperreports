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
package net.sf.jasperreports.engine;


/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id$
 */
public interface JRPrintHyperlink
{




	/**
	 * Retrieves the hyperlink type for the element.
	 * <p>
	 * The actual hyperlink type is determined by {@link #getLinkType() getLinkType()}.
	 * This method can is used to determine whether the hyperlink type is one of the
	 * built-in types or a custom type. 
	 * When hyperlink is of custom type, {@link JRHyperlink#HYPERLINK_TYPE_CUSTOM HYPERLINK_TYPE_CUSTOM} is returned.
	 * </p>
	 * @return one of the hyperlink type constants
	 * @see #getLinkType()
	 */
	public byte getHyperlinkType();

	
	/**
	 * Sets the link type as a built-in hyperlink type.
	 * 
	 * @param hyperlinkType the built-in hyperlink type
	 * @see #getLinkType()
	 */
	public void setHyperlinkType(byte hyperlinkType);
		
	/**
	 *
	 */
	public byte getHyperlinkTarget();
		
	/**
	 *
	 */
	public void setHyperlinkTarget(byte hyperlinkTarget);
		
	/**
	 *
	 */
	public String getHyperlinkReference();
		
	/**
	 *
	 */
	public void setHyperlinkReference(String hyperlinkReference);

	/**
	 *
	 */
	public String getHyperlinkAnchor();
		
	/**
	 *
	 */
	public void setHyperlinkAnchor(String hyperlinkAnchor);

	/**
	 *
	 */
	public Integer getHyperlinkPage();
		
	/**
	 *
	 */
	public void setHyperlinkPage(Integer hyperlinkPage);

	
	/**
	 * Returns the hyperlink type.
	 * 
	 * @return the hyperlink type
	 */
	public String getLinkType();
	
	
	/**
	 * Sets the hyperlink type.
	 * <p>
	 * The type can be one of the built-in types
	 * (Reference, LocalAnchor, LocalPage, RemoteAnchor, RemotePage),
	 * or can be an arbitrary type.
	 * </p>
	 * @param type the hyperlink type
	 */
	public void setLinkType(String type);

	
	/**
	 * Returns the set of custom hyperlink parameters.
	 * 
	 * @return the set of custom hyperlink parameters
	 * @see #setHyperlinkParameters(JRPrintHyperlinkParameters)
	 * @see JRPrintHyperlinkParameter
	 */
	public JRPrintHyperlinkParameters getHyperlinkParameters();
	

	/**
	 * Sets the custom hyperlink parameters.
	 * <p>
	 * These parameters will be used to produce the actual hyperlink
	 * when the report is exported.
	 * </p>
	 * 
	 * @param parameters the set of custom hyperlink parameters
	 */
	public void setHyperlinkParameters(JRPrintHyperlinkParameters parameters);

	
	/**
	 * Returns the hyperlink tooltip.
	 * 
	 * @return the hyperlink tooltip
	 */
	public String getHyperlinkTooltip();
	
	
	/**
	 * Sets the tooltip to be used for the hyperlink.
	 * 
	 * @param tooltip the tooltip
	 */
	public void setHyperlinkTooltip(String tooltip);
	
	/**
	 * Returns the hyperlink target name.
	 * <p>
	 * The target name can be one of the built-in names
	 * (Self, Blank, Top, Parent),
	 * or can be an arbitrary name.
	 * </p>
	 * @return the hyperlink type
	 */
	public String getLinkTarget();


	/**
	 * Sets the hyperlink target name.
	 * <p>
	 * The target name can be one of the built-in names
	 * (Self, Blank, Top, Parent),
	 * or can be an arbitrary name.
	 * </p>
	 * @param linkTarget the hyperlink target name
	 */
	public void setLinkTarget(String linkTarget);
	
}
