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

import net.sf.jasperreports.engine.type.HyperlinkTargetEnum;
import net.sf.jasperreports.engine.type.HyperlinkTypeEnum;


/**
 * Utility class that manages built-in hyperlink types.
 * 
 * @author Lucian Chirita (lucianc@users.sourceforge.net)
 * @version $Id$
 */
public final class JRHyperlinkHelper
{
	/**
	 * "None" link type, equivalent to {@link JRHyperlink#HYPERLINK_TYPE_NONE JRHyperlink.HYPERLINK_TYPE_NONE}.
	 * @deprecated Replaced by {@link HyperlinkTypeEnum#NONE}.
	 */
	public static final String HYPERLINK_TYPE_NONE = HyperlinkTypeEnum.NONE.getName();
	
	/**
	 * "Reference" link type, equivalent to {@link JRHyperlink#HYPERLINK_TYPE_REFERENCE JRHyperlink.HYPERLINK_TYPE_REFERENCE}.
	 * @deprecated Replaced by {@link HyperlinkTypeEnum#REFERENCE}.
	 */
	public static final String HYPERLINK_TYPE_REFERENCE = HyperlinkTypeEnum.REFERENCE.getName();
	
	/**
	 * "LocalAnchor" link type, equivalent to {@link JRHyperlink#HYPERLINK_TYPE_LOCAL_ANCHOR JRHyperlink.HYPERLINK_LOCAL_ANCHOR}.
	 * @deprecated Replaced by {@link HyperlinkTypeEnum#LOCAL_ANCHOR}.
	 */
	public static final String HYPERLINK_TYPE_LOCAL_ANCHOR = HyperlinkTypeEnum.LOCAL_ANCHOR.getName();
	
	/**
	 * "LocalPage" link type, equivalent to {@link JRHyperlink#HYPERLINK_TYPE_LOCAL_PAGE JRHyperlink.HYPERLINK_TYPE_LOCAL_PAGE}.
	 * @deprecated Replaced by {@link HyperlinkTypeEnum#LOCAL_PAGE}.
	 */
	public static final String HYPERLINK_TYPE_LOCAL_PAGE = HyperlinkTypeEnum.LOCAL_PAGE.getName();
	
	/**
	 * "RemoteAnchor" link type, equivalent to {@link JRHyperlink#HYPERLINK_TYPE_REMOTE_ANCHOR JRHyperlink.HYPERLINK_TYPE_REMOTE_ANCHOR}.
	 * @deprecated Replaced by {@link HyperlinkTypeEnum#REMOTE_ANCHOR}.
	 */
	public static final String HYPERLINK_TYPE_REMOTE_ANCHOR = HyperlinkTypeEnum.REMOTE_ANCHOR.getName();
	
	/**
	 * "RemotePage" link type, equivalent to {@link JRHyperlink#HYPERLINK_TYPE_REMOTE_PAGE JRHyperlink.HYPERLINK_TYPE_REMOTE_PAGE}.
	 * @deprecated Replaced by {@link HyperlinkTypeEnum#REMOTE_PAGE}.
	 */
	public static final String HYPERLINK_TYPE_REMOTE_PAGE = HyperlinkTypeEnum.REMOTE_PAGE.getName();
	
	/**
	 * "Self" link target name, equivalent to {@link JRHyperlink#HYPERLINK_TARGET_SELF JRHyperlink.HYPERLINK_TARGET_SELF}.
	 * @deprecated Replaced by {@link HyperlinkTargetEnum#SELF}.
	 */
	public static final String HYPERLINK_TARGET_SELF = HyperlinkTargetEnum.SELF.getName();

	/**
	 * "Blank" link target name, equivalent to {@link JRHyperlink#HYPERLINK_TARGET_BLANK JRHyperlink.HYPERLINK_TARGET_BLANK}.
	 * @deprecated Replaced by {@link HyperlinkTargetEnum#BLANK}.
	 */
	public static final String HYPERLINK_TARGET_BLANK = HyperlinkTargetEnum.BLANK.getName();

	/**
	 * "Parent" link target name, equivalent to {@link JRHyperlink#HYPERLINK_TARGET_PARENT JRHyperlink.HYPERLINK_TARGET_PARENT}.
	 * @deprecated Replaced by {@link HyperlinkTargetEnum#PARENT}.
	 */
	public static final String HYPERLINK_TARGET_PARENT = HyperlinkTargetEnum.PARENT.getName();

	/**
	 * "Top" link target name, equivalent to {@link JRHyperlink#HYPERLINK_TARGET_TOP JRHyperlink.HYPERLINK_TARGET_TOP}.
	 * @deprecated Replaced by {@link HyperlinkTargetEnum#TOP}.
	 */
	public static final String HYPERLINK_TARGET_TOP = HyperlinkTargetEnum.TOP.getName();
	
	
	/**
	 * @deprecated Replaced by {@link #getHyperlinkTypeValue(JRHyperlink)}.
	 */
	public static byte getHyperlinkType(JRHyperlink hyperlink)
	{
		return getHyperlinkTypeValue(hyperlink.getLinkType()).getValue();
	}
	

	/**
	 * Returns the built-in hyperlink type, or {@link JRHyperlink#HYPERLINK_TYPE_CUSTOM JRHyperlink.HYPERLINK_TYPE_CUSTOM}
	 * if the type is not a built-in type.
	 * 
	 * @param hyperlink the hyperlink object
	 * @return the hyperlink type
	 */
	public static HyperlinkTypeEnum getHyperlinkTypeValue(JRHyperlink hyperlink)
	{
		return getHyperlinkTypeValue(hyperlink.getLinkType());
	}
	

	/**
	 * @deprecated Replaced by {@link #getHyperlinkTypeValue(String)}.
	 */
	public static byte getHyperlinkType(String linkType)
	{
		return getHyperlinkTypeValue(linkType).getValue();
	}
	
	
	/**
	 * Returns the built-in hyperlink type, or {@link JRHyperlink#HYPERLINK_TYPE_CUSTOM JRHyperlink.HYPERLINK_TYPE_CUSTOM}
	 * if the type is not a built-in type.
	 * 
	 * @param linkType the link type
	 * @return the hyperlink type
	 */
	public static HyperlinkTypeEnum getHyperlinkTypeValue(String linkType)
	{
		HyperlinkTypeEnum type;
		if (linkType == null)
		{
			type = HyperlinkTypeEnum.NONE;
		}
		else
		{
			HyperlinkTypeEnum builtinType = HyperlinkTypeEnum.getByName(linkType);
			if (builtinType == null)
			{
				type = HyperlinkTypeEnum.CUSTOM;
			}
			else
			{
				type = builtinType;
			}
		}
		return type;
	}
	

	/**
	 * Returns the built-in hyperlink target, or {@link JRHyperlink#HYPERLINK_TARGET_CUSTOM JRHyperlink.HYPERLINK_TARGET_CUSTOM}
	 * if the target name is not a built-in one.
	 * 
	 * @param hyperlink the hyperlink object
	 * @return the hyperlink target
	 */
	public static byte getHyperlinkTarget(JRHyperlink hyperlink)
	{
		return getHyperlinkTarget(hyperlink.getLinkTarget());
	}
	

	/**
	 * @deprecated Replaced by {@link #getHyperlinkTargetValue(String)}.
	 */
	public static byte getHyperlinkTarget(String linkTarget)
	{
		return getHyperlinkTargetValue(linkTarget).getValue();
	}
	
	
	/**
	 * Returns the built-in hyperlink target, or {@link HyperlinkTargetEnum#CUSTOM HyperlinkTargetEnum.CUSTOM}
	 * if the target name is not a built-in one.
	 * 
	 * @param linkTarget the link target type
	 * @return the hyperlink target
	 */
	public static HyperlinkTargetEnum getHyperlinkTargetValue(String linkTarget)
	{
		HyperlinkTargetEnum target;
		if (linkTarget == null)
		{
			target = HyperlinkTargetEnum.SELF;
		}
		else
		{
			HyperlinkTargetEnum builtinTarget = HyperlinkTargetEnum.getByName(linkTarget);
			if (builtinTarget == null)
			{
				target = HyperlinkTargetEnum.CUSTOM;
			}
			else
			{
				target = builtinTarget;
			}
		}
		return target;
	}
	
	
	/**
	 * @deprecated Replaced by {@link #getLinkType(HyperlinkTypeEnum)}.
	 */
	public static String getLinkType(byte hyperlinkType)
	{
		return getLinkType(HyperlinkTypeEnum.getByValue(hyperlinkType));
	}

	
	/**
	 * Returns the link type associated with a built-in type.
	 * 
	 * @param hyperlinkType the built-in type
	 * @return the String link type
	 */
	public static String getLinkType(HyperlinkTypeEnum hyperlinkType)
	{
		String type;
		switch (hyperlinkType)
		{
			case NULL:
			case NONE:
				type = null;
				break;
			case REFERENCE:
			case LOCAL_ANCHOR:
			case LOCAL_PAGE:
			case REMOTE_ANCHOR:
			case REMOTE_PAGE:
				type = hyperlinkType.getName();
				break;
			case CUSTOM:
				throw new JRRuntimeException("Custom hyperlink types cannot be specified using the byte constant");
			default:
				throw new JRRuntimeException("Unknown hyperlink type " + hyperlinkType);
		}
		return type;
	}

	
	/**
	 * @deprecated Replaced by {@link #getLinkTarget(HyperlinkTargetEnum)}.
	 */
	public static String getLinkTarget(byte hyperlinkTarget)
	{
		return getLinkTarget(HyperlinkTargetEnum.getByValue(hyperlinkTarget));
	}

	
	/**
	 * Returns the link target associated with a built-in target.
	 * 
	 * @param hyperlinkTarget the built-in target type
	 * @return the String link target
	 */
	public static String getLinkTarget(HyperlinkTargetEnum hyperlinkTarget)
	{
		String target;
		switch (hyperlinkTarget)
		{
			case NONE:
			case SELF:
				target = null;
				break;
			case BLANK:
				target = HYPERLINK_TARGET_BLANK;
				break;
			case PARENT:
				target = HYPERLINK_TARGET_PARENT;
				break;
			case TOP:
				target = HYPERLINK_TARGET_TOP;
				break;
			case CUSTOM:
				throw new JRRuntimeException("Custom hyperlink targets cannot be specified using the byte constant");
			default:
				throw new JRRuntimeException("Unknown hyperlink target " + hyperlinkTarget);
		}
		return target;
	}

	
	/**
	 * Decides whether a hyperlink is empty or not.
	 * <p>
	 * The hyperlink is considered empty when it's <code>null</code> or when
	 * its type is {@link HyperlinkTypeEnum#NONE NONE}
	 * and it doesn't include a tooltip expression
	 * </p>
	 * @param hyperlink the hyperlink
	 * @return whether the hyperlink is empty
	 */
	public static boolean isEmpty(JRHyperlink hyperlink)
	{
		return hyperlink == null
			|| (hyperlink.getHyperlinkTypeValue() == HyperlinkTypeEnum.NONE
				&& hyperlink.getHyperlinkTooltipExpression() == null);
	}
	
	
	private JRHyperlinkHelper()
	{
	}
}
