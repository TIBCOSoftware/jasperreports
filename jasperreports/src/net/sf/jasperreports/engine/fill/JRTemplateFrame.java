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
package net.sf.jasperreports.engine.fill;

import java.awt.Color;
import java.io.IOException;
import java.io.ObjectInputStream;

import net.sf.jasperreports.engine.JRBoxContainer;
import net.sf.jasperreports.engine.JRConstants;
import net.sf.jasperreports.engine.JRDefaultStyleProvider;
import net.sf.jasperreports.engine.JRLineBox;
import net.sf.jasperreports.engine.JROrigin;
import net.sf.jasperreports.engine.base.JRBaseLineBox;
import net.sf.jasperreports.engine.type.ModeEnum;
import net.sf.jasperreports.engine.util.JRBoxUtil;
import net.sf.jasperreports.engine.util.JRStyleResolver;
import net.sf.jasperreports.engine.util.ObjectUtils;

/**
 * Frame information shared by multiple print frame objects.
 * 
 * @author Lucian Chirita (lucianc@users.sourceforge.net)
 * @see net.sf.jasperreports.engine.fill.JRTemplatePrintFrame
 */
public class JRTemplateFrame extends JRTemplateElement implements JRBoxContainer
{
	private static final long serialVersionUID = JRConstants.SERIAL_VERSION_UID;

	protected JRLineBox lineBox;


	/**
	 * Creates a template frame for a crosstab cell.
	 * 
	 * @param cell the cell
	 */
	public JRTemplateFrame(JROrigin origin, JRDefaultStyleProvider defaultStyleProvider, JRFillCellContents cell)
	{
		super(origin, defaultStyleProvider);
	
		//FIXME properties?
		parentStyle = cell.getStyle();
		
		setBackcolor(cell.getBackcolor());
		setMode(cell.getModeValue());
		copyBox(cell.getLineBox());
	}
	
	
	/**
	 * Creates a template frame for a frame.
	 * 
	 * @param frame the frame
	 */
	public JRTemplateFrame(JROrigin origin, JRDefaultStyleProvider defaultStyleProvider, JRFillFrame frame)
	{
		super(origin, defaultStyleProvider);

		setElement(frame);
		
		copyBox(frame.getLineBox());
	}

	/**
	 * Creates a template frame.
	 * 
	 * @param origin the origin of the elements that will use this template
	 * @param defaultStyleProvider the default style provider to use for
	 * this template
	 */
	public JRTemplateFrame(JROrigin origin, JRDefaultStyleProvider defaultStyleProvider)
	{
		super(origin, defaultStyleProvider);
		
		this.lineBox = new JRBaseLineBox(this);
	}
	
	/**
	 *
	 */
	public JRLineBox getLineBox()
	{
		return lineBox;
	}

	/**
	 *
	 */
	public void copyBox(JRLineBox lineBox)
	{
		this.lineBox = lineBox.clone(this);
	}

	/**
	 *
	 */
	public ModeEnum getModeValue()
	{
		return JRStyleResolver.getMode(this, ModeEnum.TRANSPARENT);
	}

	/**
	 * 
	 */
	public Color getDefaultLineColor() 
	{
		return getForecolor();
	}

	
	/*
	 * These fields are only for serialization backward compatibility.
	 */
	/**
	 * @deprecated
	 */
	private Byte border;
	/**
	 * @deprecated
	 */
	private Byte topBorder;
	/**
	 * @deprecated
	 */
	private Byte leftBorder;
	/**
	 * @deprecated
	 */
	private Byte bottomBorder;
	/**
	 * @deprecated
	 */
	private Byte rightBorder;
	/**
	 * @deprecated
	 */
	private Color borderColor;
	/**
	 * @deprecated
	 */
	private Color topBorderColor;
	/**
	 * @deprecated
	 */
	private Color leftBorderColor;
	/**
	 * @deprecated
	 */
	private Color bottomBorderColor;
	/**
	 * @deprecated
	 */
	private Color rightBorderColor;
	/**
	 * @deprecated
	 */
	private Integer padding;
	/**
	 * @deprecated
	 */
	private Integer topPadding;
	/**
	 * @deprecated
	 */
	private Integer leftPadding;
	/**
	 * @deprecated
	 */
	private Integer bottomPadding;
	/**
	 * @deprecated
	 */
	private Integer rightPadding;
	
	@SuppressWarnings("deprecation")
	private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException
	{
		in.defaultReadObject();

		if (lineBox == null)
		{
			lineBox = new JRBaseLineBox(this);
			JRBoxUtil.setToBox(
				border,
				topBorder,
				leftBorder,
				bottomBorder,
				rightBorder,
				borderColor,
				topBorderColor,
				leftBorderColor,
				bottomBorderColor,
				rightBorderColor,
				padding,
				topPadding,
				leftPadding,
				bottomPadding,
				rightPadding,
				lineBox
				);
			border = null;
			topBorder = null;
			leftBorder = null;
			bottomBorder = null;
			rightBorder = null;
			borderColor = null;
			topBorderColor = null;
			leftBorderColor = null;
			bottomBorderColor = null;
			rightBorderColor = null;
			padding = null;
			topPadding = null;
			leftPadding = null;
			bottomPadding = null;
			rightPadding = null;
		}
	}


	public int getHashCode()
	{
		ObjectUtils.HashCode hash = ObjectUtils.hash();
		addTemplateHash(hash);
		hash.addIdentical(lineBox);
		return hash.getHashCode();
	}


	public boolean isIdentical(Object object)
	{
		if (this == object)
		{
			return true;
		}
		
		if (!(object instanceof JRTemplateFrame))
		{
			return false;
		}
		
		JRTemplateFrame template = (JRTemplateFrame) object;
		return templateIdentical(template)
				&& ObjectUtils.identical(lineBox, template.lineBox);
	}
}
