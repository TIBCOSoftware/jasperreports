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

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;

import net.sf.jasperreports.engine.JRConstants;
import net.sf.jasperreports.engine.JRExpression;
import net.sf.jasperreports.engine.JRHyperlink;
import net.sf.jasperreports.engine.JRHyperlinkHelper;
import net.sf.jasperreports.engine.JRHyperlinkParameter;
import net.sf.jasperreports.engine.JRRuntimeException;

/**
 * Read-only implementation of {@link JRHyperlink JRHyperlink}.
 * 
 * @author Lucian Chirita (lucianc@users.sourceforge.net)
 * @version $Id$
 */
public class JRBaseHyperlink implements JRHyperlink, Serializable
{
	
	private static final long serialVersionUID = JRConstants.SERIAL_VERSION_UID;
	
	protected String linkType;
	protected String linkTarget;
	protected JRExpression hyperlinkReferenceExpression;
	protected JRExpression hyperlinkAnchorExpression;
	protected JRExpression hyperlinkPageExpression;
	protected JRExpression hyperlinkTooltipExpression;
	protected JRHyperlinkParameter[] hyperlinkParameters;

	
	/**
	 * Create an empty hyperlink.
	 */
	public JRBaseHyperlink()
	{
	}

	
	protected JRBaseHyperlink(JRHyperlink link, JRBaseObjectFactory factory)
	{
		factory.put(link, this);
		
		linkType = link.getLinkType();
		linkTarget = link.getLinkTarget();
		hyperlinkReferenceExpression = factory.getExpression(link.getHyperlinkReferenceExpression());
		hyperlinkAnchorExpression = factory.getExpression(link.getHyperlinkAnchorExpression());
		hyperlinkPageExpression = factory.getExpression(link.getHyperlinkPageExpression());
		hyperlinkTooltipExpression = factory.getExpression(link.getHyperlinkTooltipExpression());
		hyperlinkParameters = copyHyperlinkParameters(link, factory);
	}

	public static JRHyperlinkParameter[] copyHyperlinkParameters(JRHyperlink link, JRBaseObjectFactory factory)
	{
		JRHyperlinkParameter[] linkParameters = link.getHyperlinkParameters();
		JRHyperlinkParameter[] parameters = null;
		if (linkParameters != null && linkParameters.length > 0)
		{
			parameters = new JRHyperlinkParameter[linkParameters.length];
			for (int i = 0; i < linkParameters.length; i++)
			{
				JRHyperlinkParameter parameter = linkParameters[i];
				parameters[i] = factory.getHyperlinkParameter(parameter);
			}
		}
		return parameters;
	}
	
	public JRExpression getHyperlinkAnchorExpression()
	{
		return hyperlinkAnchorExpression;
	}

	public JRExpression getHyperlinkPageExpression()
	{
		return hyperlinkPageExpression;
	}

	public JRHyperlinkParameter[] getHyperlinkParameters()
	{
		return hyperlinkParameters;
	}

	public JRExpression getHyperlinkReferenceExpression()
	{
		return hyperlinkReferenceExpression;
	}

	public byte getHyperlinkTarget()
	{
		return JRHyperlinkHelper.getHyperlinkTarget(this);
	}

	public byte getHyperlinkType()
	{
		return JRHyperlinkHelper.getHyperlinkType(this);
	}

	public String getLinkType()
	{
		return linkType;
	}
	
	public String getLinkTarget()
	{
		return linkTarget;
	}
	
	public JRExpression getHyperlinkTooltipExpression()
	{
		return hyperlinkTooltipExpression;
	}

	/**
	 * 
	 */
	public Object clone() 
	{
		JRBaseHyperlink clone = null;

		try
		{
			clone = (JRBaseHyperlink)super.clone();
		}
		catch (CloneNotSupportedException e)
		{
			throw new JRRuntimeException(e);
		}
		
		if (hyperlinkParameters != null)
		{
			clone.hyperlinkParameters = new JRHyperlinkParameter[hyperlinkParameters.length];
			for(int i = 0; i < hyperlinkParameters.length; i++)
			{
				clone.hyperlinkParameters[i] = (JRHyperlinkParameter)hyperlinkParameters[i].clone();
			}
		}

		if (hyperlinkReferenceExpression != null)
		{
			clone.hyperlinkReferenceExpression = (JRExpression)hyperlinkReferenceExpression.clone();
		}
		if (hyperlinkAnchorExpression != null)
		{
			clone.hyperlinkAnchorExpression = (JRExpression)hyperlinkAnchorExpression.clone();
		}
		if (hyperlinkPageExpression != null)
		{
			clone.hyperlinkPageExpression = (JRExpression)hyperlinkPageExpression.clone();
		}
		if (hyperlinkTooltipExpression != null)
		{
			clone.hyperlinkTooltipExpression = (JRExpression)hyperlinkTooltipExpression.clone();
		}

		return clone;
	}

	/**
	 * These fields are only for serialization backward compatibility.
	 */
	private byte hyperlinkTarget = JRHyperlink.HYPERLINK_TARGET_SELF;
	
	private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException
	{
		in.defaultReadObject();

		if (linkTarget == null)
		{
			 linkTarget = JRHyperlinkHelper.getLinkTarget(hyperlinkTarget);
		}
		hyperlinkTarget = JRHyperlink.HYPERLINK_TARGET_SELF;
	}
	
}
