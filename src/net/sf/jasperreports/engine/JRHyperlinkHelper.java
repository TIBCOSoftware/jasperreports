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

import java.util.HashMap;
import java.util.Map;


/**
 * Utility class that manages built-in hyperlink types.
 * 
 * @author Lucian Chirita (lucianc@users.sourceforge.net)
 * @version $Id$
 */
public class JRHyperlinkHelper
{
	/**
	 * "None" link type, equivalent to {@link JRHyperlink#HYPERLINK_TYPE_NONE JRHyperlink.HYPERLINK_TYPE_NONE}.
	 */
	public static final String HYPERLINK_TYPE_NONE = "None";
	
	/**
	 * "Reference" link type, equivalent to {@link JRHyperlink#HYPERLINK_TYPE_REFERENCE JRHyperlink.HYPERLINK_TYPE_REFERENCE}.
	 */
	public static final String HYPERLINK_TYPE_REFERENCE = "Reference";
	
	/**
	 * "LocalAnchor" link type, equivalent to {@link JRHyperlink#HYPERLINK_TYPE_LOCAL_ANCHOR JRHyperlink.HYPERLINK_LOCAL_ANCHOR}.
	 */
	public static final String HYPERLINK_TYPE_LOCAL_ANCHOR = "LocalAnchor";
	
	/**
	 * "LocalPage" link type, equivalent to {@link JRHyperlink#HYPERLINK_TYPE_LOCAL_PAGE JRHyperlink.HYPERLINK_TYPE_LOCAL_PAGE}.
	 */
	public static final String HYPERLINK_TYPE_LOCAL_PAGE = "LocalPage";
	
	/**
	 * "RemoteAnchor" link type, equivalent to {@link JRHyperlink#HYPERLINK_TYPE_REMOTE_ANCHOR JRHyperlink.HYPERLINK_TYPE_REMOTE_ANCHOR}.
	 */
	public static final String HYPERLINK_TYPE_REMOTE_ANCHOR = "RemoteAnchor";
	
	/**
	 * "RemotePage" link type, equivalent to {@link JRHyperlink#HYPERLINK_TYPE_REMOTE_PAGE JRHyperlink.HYPERLINK_TYPE_REMOTE_PAGE}.
	 */
	public static final String HYPERLINK_TYPE_REMOTE_PAGE = "RemotePage";
	
	/**
	 * "Self" link target name, equivalent to {@link JRHyperlink#HYPERLINK_TARGET_SELF JRHyperlink.HYPERLINK_TARGET_SELF}.
	 */
	public static final String HYPERLINK_TARGET_SELF = "Self";

	/**
	 * "Blank" link target name, equivalent to {@link JRHyperlink#HYPERLINK_TARGET_BLANK JRHyperlink.HYPERLINK_TARGET_BLANK}.
	 */
	public static final String HYPERLINK_TARGET_BLANK = "Blank";

	/**
	 * "Parent" link target name, equivalent to {@link JRHyperlink#HYPERLINK_TARGET_PARENT JRHyperlink.HYPERLINK_TARGET_PARENT}.
	 */
	public static final String HYPERLINK_TARGET_PARENT = "Parent";

	/**
	 * "Top" link target name, equivalent to {@link JRHyperlink#HYPERLINK_TARGET_TOP JRHyperlink.HYPERLINK_TARGET_TOP}.
	 */
	public static final String HYPERLINK_TARGET_TOP = "Top";
	
	private static final Map builtinTypes;
	private static final Map builtinTargets;
	
	static
	{
		builtinTypes = createBuiltinTypes();
		builtinTargets = createBuiltinTargets();
	}

	private static Map createBuiltinTypes()
	{
		Map types = new HashMap();
		types.put(HYPERLINK_TYPE_NONE, new Byte(JRHyperlink.HYPERLINK_TYPE_NONE));
		types.put(HYPERLINK_TYPE_REFERENCE, new Byte(JRHyperlink.HYPERLINK_TYPE_REFERENCE));
		types.put(HYPERLINK_TYPE_LOCAL_ANCHOR, new Byte(JRHyperlink.HYPERLINK_TYPE_LOCAL_ANCHOR));
		types.put(HYPERLINK_TYPE_LOCAL_PAGE, new Byte(JRHyperlink.HYPERLINK_TYPE_LOCAL_PAGE));
		types.put(HYPERLINK_TYPE_REMOTE_ANCHOR, new Byte(JRHyperlink.HYPERLINK_TYPE_REMOTE_ANCHOR));
		types.put(HYPERLINK_TYPE_REMOTE_PAGE, new Byte(JRHyperlink.HYPERLINK_TYPE_REMOTE_PAGE));
		return types;
	}
	
	private static Map createBuiltinTargets()
	{
		Map targets = new HashMap();
		targets.put(HYPERLINK_TARGET_BLANK, new Byte(JRHyperlink.HYPERLINK_TARGET_BLANK));
		targets.put(HYPERLINK_TARGET_PARENT, new Byte(JRHyperlink.HYPERLINK_TARGET_PARENT));
		targets.put(HYPERLINK_TARGET_SELF, new Byte(JRHyperlink.HYPERLINK_TARGET_SELF));
		targets.put(HYPERLINK_TARGET_TOP, new Byte(JRHyperlink.HYPERLINK_TARGET_TOP));
		return targets;
	}
	
	
	/**
	 * Returns the built-in hyperlink type, or {@link JRHyperlink#HYPERLINK_TYPE_CUSTOM JRHyperlink.HYPERLINK_TYPE_CUSTOM}
	 * if the type is not a built-in type.
	 * 
	 * @param hyperlink the hyperlink object
	 * @return the hyperlink type
	 */
	public static byte getHyperlinkType(JRHyperlink hyperlink)
	{
		return getHyperlinkType(hyperlink.getLinkType());
	}
	

	/**
	 * Returns the built-in hyperlink type, or {@link JRHyperlink#HYPERLINK_TYPE_CUSTOM JRHyperlink.HYPERLINK_TYPE_CUSTOM}
	 * if the type is not a built-in type.
	 * 
	 * @param linkType the link type
	 * @return the hyperlink type
	 */
	public static byte getHyperlinkType(String linkType)
	{
		byte type;
		if (linkType == null)
		{
			type = JRHyperlink.HYPERLINK_TYPE_NONE;
		}
		else
		{
			Byte builtinType = (Byte) builtinTypes.get(linkType);
			if (builtinType == null)
			{
				type = JRHyperlink.HYPERLINK_TYPE_CUSTOM;
			}
			else
			{
				type = builtinType.byteValue();
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
	 * Returns the built-in hyperlink target, or {@link JRHyperlink#HYPERLINK_TARGET_CUSTOM JRHyperlink.HYPERLINK_TARGET_CUSTOM}
	 * if the target name is not a built-in one.
	 * 
	 * @param linkTarget the link target type
	 * @return the hyperlink target
	 */
	public static byte getHyperlinkTarget(String linkTarget)
	{
		byte target;
		if (linkTarget == null)
		{
			target = JRHyperlink.HYPERLINK_TARGET_SELF;
		}
		else
		{
			Byte builtinTarget = (Byte) builtinTargets.get(linkTarget);
			if (builtinTarget == null)
			{
				target = JRHyperlink.HYPERLINK_TARGET_CUSTOM;
			}
			else
			{
				target = builtinTarget.byteValue();
			}
		}
		return target;
	}
	
	
	/**
	 * Returns the link type associated with a built-in type.
	 * 
	 * @param hyperlinkType the built-in type
	 * @return the String link type
	 */
	public static String getLinkType(byte hyperlinkType)
	{
		String type;
		switch (hyperlinkType)
		{
			case JRHyperlink.HYPERLINK_TYPE_NULL:
			case JRHyperlink.HYPERLINK_TYPE_NONE:
				type = null;
				break;
			case JRHyperlink.HYPERLINK_TYPE_REFERENCE:
				type = HYPERLINK_TYPE_REFERENCE;
				break;
			case JRHyperlink.HYPERLINK_TYPE_LOCAL_ANCHOR:
				type = HYPERLINK_TYPE_LOCAL_ANCHOR;
				break;
			case JRHyperlink.HYPERLINK_TYPE_LOCAL_PAGE:
				type = HYPERLINK_TYPE_LOCAL_PAGE;
				break;
			case JRHyperlink.HYPERLINK_TYPE_REMOTE_ANCHOR:
				type = HYPERLINK_TYPE_REMOTE_ANCHOR;
				break;
			case JRHyperlink.HYPERLINK_TYPE_REMOTE_PAGE:
				type = HYPERLINK_TYPE_REMOTE_PAGE;
				break;
			case JRHyperlink.HYPERLINK_TYPE_CUSTOM:
				throw new JRRuntimeException("Custom hyperlink types cannot be specified using the byte constant");
			default:
				throw new JRRuntimeException("Unknown hyperlink type " + hyperlinkType);
		}
		return type;
	}

	/**
	 * Returns the link target associated with a built-in target.
	 * 
	 * @param hyperlinkTarget the built-in target type
	 * @return the String link target
	 */
	public static String getLinkTarget(byte hyperlinkTarget)
	{
		String target;
		switch (hyperlinkTarget)
		{
			case JRHyperlink.HYPERLINK_TARGET_SELF:
				target = null;
				break;
			case JRHyperlink.HYPERLINK_TARGET_BLANK:
				target = HYPERLINK_TARGET_BLANK;
				break;
			case JRHyperlink.HYPERLINK_TARGET_PARENT:
				target = HYPERLINK_TARGET_PARENT;
				break;
			case JRHyperlink.HYPERLINK_TARGET_TOP:
				target = HYPERLINK_TARGET_TOP;
				break;
			case JRHyperlink.HYPERLINK_TARGET_CUSTOM:
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
	 * its type is {@link JRHyperlink#HYPERLINK_TYPE_NONE HYPERLINK_TYPE_NONE}
	 * and it doesn't include a tooltip expression
	 * </p>
	 * @param hyperlink the hyperlink
	 * @return whether the hyperlink is empty
	 */
	public static boolean isEmpty(JRHyperlink hyperlink)
	{
		return hyperlink == null
			|| (hyperlink.getHyperlinkType() == JRHyperlink.HYPERLINK_TYPE_NONE
				&& hyperlink.getHyperlinkTooltipExpression() == null);
	}
}
