/*
 * ============================================================================
 *                   GNU Lesser General Public License
 * ============================================================================
 *
 * JasperReports - Free Java report-generating library.
 * Copyright (C) 2001-2005 Teodor Danciu teodord@users.sourceforge.net
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
 * Teodor Danciu
 * 173, Calea Calarasilor, Bl. 42, Sc. 1, Ap. 18
 * Postal code 030615, Sector 3
 * Bucharest, ROMANIA
 * Email: teodord@users.sourceforge.net
 */
package net.sf.jasperreports.engine.fill;

import net.sf.jasperreports.engine.JRAlignment;
import net.sf.jasperreports.engine.JRBox;
import net.sf.jasperreports.engine.JRHyperlink;
import net.sf.jasperreports.engine.JRImage;


/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id$
 */
public class JRTemplateImage extends JRTemplateGraphicElement
{


	/**
	 *
	 */
	private static final long serialVersionUID = 604;

	/**
	 *
	 */
	private byte scaleImage = JRImage.SCALE_IMAGE_CLIP;
	private byte horizontalAlignment = JRAlignment.HORIZONTAL_ALIGN_LEFT;
	private byte verticalAlignment = JRAlignment.VERTICAL_ALIGN_TOP;
	private byte hyperlinkType = JRHyperlink.HYPERLINK_TYPE_NONE;
	private byte hyperlinkTarget = JRHyperlink.HYPERLINK_TARGET_SELF;
	private JRBox box = null;


	/**
	 *
	 */
	protected JRTemplateImage(JRImage image)
	{
		setImage(image);
	}


	/**
	 *
	 */
	protected void setImage(JRImage image)
	{
		super.setGraphicElement(image);
		
		setScaleImage(image.getScaleImage());
		setHorizontalAlignment(image.getHorizontalAlignment());
		setVerticalAlignment(image.getVerticalAlignment());
		setHyperlinkType(image.getHyperlinkType());
		setHyperlinkTarget(image.getHyperlinkTarget());

		box = image.getBox();
	}

	/**
	 *
	 */
	public byte getScaleImage()
	{
		return this.scaleImage;
	}
		
	/**
	 *
	 */
	protected void setScaleImage(byte scaleImage)
	{
		this.scaleImage = scaleImage;
	}

	/**
	 *
	 */
	public byte getHorizontalAlignment()
	{
		return this.horizontalAlignment;
	}
		
	/**
	 *
	 */
	protected void setHorizontalAlignment(byte horizontalAlignment)
	{
		this.horizontalAlignment = horizontalAlignment;
	}
		
	/**
	 *
	 */
	public byte getVerticalAlignment()
	{
		return this.verticalAlignment;
	}
		
	/**
	 *
	 */
	protected void setVerticalAlignment(byte verticalAlignment)
	{
		this.verticalAlignment = verticalAlignment;
	}
		
	/**
	 *
	 */
	public JRBox getBox()
	{
		return box;
	}
		
	/**
	 *
	 */
	public byte getHyperlinkType()
	{
		return this.hyperlinkType;
	}
		
	/**
	 *
	 */
	protected void setHyperlinkType(byte hyperlinkType)
	{
		this.hyperlinkType = hyperlinkType;
	}
		
	/**
	 *
	 */
	public byte getHyperlinkTarget()
	{
		return this.hyperlinkTarget;
	}
		
	/**
	 *
	 */
	protected void setHyperlinkTarget(byte hyperlinkTarget)
	{
		this.hyperlinkTarget = hyperlinkTarget;
	}
	

}
