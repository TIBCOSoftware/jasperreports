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
import net.sf.jasperreports.engine.type.HyperlinkTargetEnum;
import net.sf.jasperreports.engine.type.HyperlinkTypeEnum;
import net.sf.jasperreports.engine.util.JRCloneUtils;

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

	/**
	 * @deprecated Replaced by {@link #getHyperlinkTypeValue()}.
	 */
	public byte getHyperlinkType()
	{
		return getHyperlinkTypeValue().getValue();
	}

	public HyperlinkTypeEnum getHyperlinkTypeValue()
	{
		return JRHyperlinkHelper.getHyperlinkTypeValue(this);
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
		
		clone.hyperlinkParameters = JRCloneUtils.cloneArray(hyperlinkParameters);
		clone.hyperlinkReferenceExpression = JRCloneUtils.nullSafeClone(hyperlinkReferenceExpression);
		clone.hyperlinkAnchorExpression = JRCloneUtils.nullSafeClone(hyperlinkAnchorExpression);
		clone.hyperlinkPageExpression = JRCloneUtils.nullSafeClone(hyperlinkPageExpression);
		clone.hyperlinkTooltipExpression = JRCloneUtils.nullSafeClone(hyperlinkTooltipExpression);

		return clone;
	}

	/*
	 * These fields are only for serialization backward compatibility.
	 */
	/**
	 * @deprecated
	 */
	private byte hyperlinkTarget;
	
	private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException
	{
		in.defaultReadObject();

		if (linkTarget == null)
		{
			 linkTarget = JRHyperlinkHelper.getLinkTarget(HyperlinkTargetEnum.getByValue(hyperlinkTarget));
		}
	}
	
}
