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
package net.sf.jasperreports.engine.base;

import java.io.Serializable;

import net.sf.jasperreports.engine.JRConstants;
import net.sf.jasperreports.engine.JRHyperlink;
import net.sf.jasperreports.engine.JRHyperlinkHelper;
import net.sf.jasperreports.engine.JRPrintHyperlink;
import net.sf.jasperreports.engine.JRPrintHyperlinkParameter;
import net.sf.jasperreports.engine.JRPrintHyperlinkParameters;


/**
 * Stand-alone implementation of {@link JRPrintHyperlink JRPrintHyperlink}.
 * 
 * @author Lucian Chirita (lucianc@users.sourceforge.net)
 * @version $Id$
 */
public class JRBasePrintHyperlink implements JRPrintHyperlink, Serializable
{
	
	private static final long serialVersionUID = JRConstants.SERIAL_VERSION_UID;
	
	private String linkType;
	private byte hyperlinkTarget = JRHyperlink.HYPERLINK_TARGET_SELF;
	private String linkTarget;
	private String hyperlinkReference;
	private String hyperlinkAnchor;
	private Integer hyperlinkPage;
	private String hyperlinkTooltip;
	private JRPrintHyperlinkParameters hyperlinkParameters;

	
	/**
	 * Creates a blank hyperlink.
	 */
	public JRBasePrintHyperlink()
	{
	}
	
	public String getHyperlinkAnchor()
	{
		return hyperlinkAnchor;
	}

	public Integer getHyperlinkPage()
	{
		return hyperlinkPage;
	}

	public JRPrintHyperlinkParameters getHyperlinkParameters()
	{
		return hyperlinkParameters;
	}

	public String getHyperlinkReference()
	{
		return hyperlinkReference;
	}

	public byte getHyperlinkTarget()
	{
		return JRHyperlinkHelper.getHyperlinkTarget(getLinkTarget());
	}

	public byte getHyperlinkType()
	{
		return JRHyperlinkHelper.getHyperlinkType(getLinkType());
	}

	public String getLinkType()
	{
		return linkType;
	}

	public String getLinkTarget()
	{
		return linkTarget;
	}

	public void setHyperlinkAnchor(String hyperlinkAnchor)
	{
		this.hyperlinkAnchor = hyperlinkAnchor;
	}

	public void setHyperlinkPage(Integer hyperlinkPage)
	{
		this.hyperlinkPage = hyperlinkPage;
	}

	public void setHyperlinkParameters(JRPrintHyperlinkParameters parameters)
	{
		this.hyperlinkParameters = parameters;
	}

	public void setHyperlinkReference(String hyperlinkReference)
	{
		this.hyperlinkReference = hyperlinkReference;
	}

	public void setHyperlinkTarget(byte hyperlinkTarget)
	{
		this.hyperlinkTarget = hyperlinkTarget;
	}

	public void setLinkTarget(String linkTarget)
	{
		this.linkTarget = linkTarget;
	}

	public void setHyperlinkType(byte hyperlinkType)
	{
		setLinkType(JRHyperlinkHelper.getLinkType(hyperlinkType));
	}

	public void setLinkType(String type)
	{
		this.linkType = type;
	}

	
	/**
	 * Adds a custom hyperlink parameter.
	 * 
	 * @param parameter the parameter to add
	 * @see #getHyperlinkParameters()
	 * @see JRPrintHyperlinkParameters#addParameter(JRPrintHyperlinkParameter)
	 */
	public void addHyperlinkParameter(JRPrintHyperlinkParameter parameter)
	{
		if (hyperlinkParameters == null)
		{
			hyperlinkParameters = new JRPrintHyperlinkParameters();
		}
		hyperlinkParameters.addParameter(parameter);
	}

	
	public String getHyperlinkTooltip()
	{
		return hyperlinkTooltip;
	}

	
	public void setHyperlinkTooltip(String hyperlinkTooltip)
	{
		this.hyperlinkTooltip = hyperlinkTooltip;
	}

}
