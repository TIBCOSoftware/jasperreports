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
package net.sf.jasperreports.engine.fill;

import net.sf.jasperreports.engine.JRAnchor;
import net.sf.jasperreports.engine.JRConstants;
import net.sf.jasperreports.engine.JRLineBox;
import net.sf.jasperreports.engine.JRPrintHyperlinkParameters;
import net.sf.jasperreports.engine.JRPrintImage;
import net.sf.jasperreports.engine.JRRenderable;
import net.sf.jasperreports.engine.PrintElementVisitor;
import net.sf.jasperreports.engine.type.HorizontalAlignEnum;
import net.sf.jasperreports.engine.type.HyperlinkTargetEnum;
import net.sf.jasperreports.engine.type.HyperlinkTypeEnum;
import net.sf.jasperreports.engine.type.OnErrorTypeEnum;
import net.sf.jasperreports.engine.type.ScaleImageEnum;
import net.sf.jasperreports.engine.type.VerticalAlignEnum;


/**
 * Implementation of {@link net.sf.jasperreports.engine.JRPrintImage} that uses
 * a {@link net.sf.jasperreports.engine.fill.JRTemplateImage} instance to
 * store common attributes. 
 * 
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id$
 */
public class JRTemplatePrintImage extends JRTemplatePrintGraphicElement implements JRPrintImage
{


	/**
	 *
	 */
	private static final long serialVersionUID = JRConstants.SERIAL_VERSION_UID;

	/**
	 *
	 */
	private JRRenderable renderer;
	private String anchorName;
	private String hyperlinkReference;
	private String hyperlinkAnchor;
	private Integer hyperlinkPage;
	private String hyperlinkTooltip;
	private JRPrintHyperlinkParameters hyperlinkParameters;

	/**
	 * The bookmark level for the anchor associated with this field.
	 * @see JRAnchor#getBookmarkLevel()
	 */
	protected int bookmarkLevel = JRAnchor.NO_BOOKMARK;


	/**
	 * Creates a print image element.
	 * 
	 * @param image the template image that the element will use
	 */
	public JRTemplatePrintImage(JRTemplateImage image)
	{
		super(image);
	}

	/**
	 *
	 */
	public JRRenderable getRenderer()
	{
		return this.renderer;
	}
		
	/**
	 *
	 */
	public void setRenderer(JRRenderable renderer)
	{
		this.renderer = renderer;
	}
		
	/**
	 * @deprecated Replaced by {@link #getScaleImageValue()}.
	 */
	public byte getScaleImage()
	{
		return getScaleImageValue().getValue();
	}

	/**
	 * @deprecated Replaced by {@link #getOwnScaleImageValue()}.
	 */
	public Byte getOwnScaleImage()
	{
		return getOwnScaleImageValue() == null ? null : getOwnScaleImageValue().getValueByte();
	}

	/**
	 * @deprecated Replaced by {@link #setScaleImage(ScaleImageEnum)}.
	 */
	public void setScaleImage(byte scaleImage)
	{
		throw new UnsupportedOperationException();
	}

	/**
	 * @deprecated Replaced by {@link #setScaleImage(ScaleImageEnum)}.
	 */
	public void setScaleImage(Byte scaleImage)
	{
		throw new UnsupportedOperationException();
	}

	/**
	 *
	 */
	public ScaleImageEnum getScaleImageValue()
	{
		return ((JRTemplateImage)this.template).getScaleImageValue();
	}

	/**
	 *
	 */
	public ScaleImageEnum getOwnScaleImageValue()
	{
		return ((JRTemplateImage)this.template).getOwnScaleImageValue();
	}

	/**
	 *
	 */
	public void setScaleImage(ScaleImageEnum scaleImage)
	{
		throw new UnsupportedOperationException();
	}

	/**
	 *
	 */
	public boolean isUsingCache()
	{
		return ((JRTemplateImage)this.template).isUsingCache();
	}

	/**
	 *
	 */
	public void setUsingCache(boolean isUsingCache)
	{
	}

	/**
	 * @deprecated Replaced by {@link #getHorizontalAlignmentValue()}.
	 */
	public byte getHorizontalAlignment()
	{
		return getHorizontalAlignmentValue().getValue();
	}
		
	/**
	 * @deprecated Replaced by {@link #getOwnHorizontalAlignmentValue()}.
	 */
	public Byte getOwnHorizontalAlignment()
	{
		return getOwnHorizontalAlignmentValue() == null ? null : getOwnHorizontalAlignmentValue().getValueByte();
	}
		
	/**
	 *
	 */
	public HorizontalAlignEnum getHorizontalAlignmentValue()
	{
		return ((JRTemplateImage)this.template).getHorizontalAlignmentValue();
	}
		
	/**
	 *
	 */
	public HorizontalAlignEnum getOwnHorizontalAlignmentValue()
	{
		return ((JRTemplateImage)this.template).getOwnHorizontalAlignmentValue();
	}
		
	/**
	 * @deprecated Replaced by {@link #setHorizontalAlignment(HorizontalAlignEnum)}.
	 */
	public void setHorizontalAlignment(byte horizontalAlignment)
	{
		throw new UnsupportedOperationException();
	}
		
	/**
	 * @deprecated Replaced by {@link #setHorizontalAlignment(HorizontalAlignEnum)}.
	 */
	public void setHorizontalAlignment(Byte horizontalAlignment)
	{
		throw new UnsupportedOperationException();
	}
		
	/**
	 *
	 */
	public void setHorizontalAlignment(HorizontalAlignEnum horizontalAlignment)
	{
		throw new UnsupportedOperationException();
	}
		
	/**
	 * @deprecated Replaced by {@link #getVerticalAlignmentValue()}.
	 */
	public byte getVerticalAlignment()
	{
		return getVerticalAlignmentValue().getValue();
	}
		
	/**
	 * @deprecated Replaced by {@link #getOwnVerticalAlignmentValue()}.
	 */
	public Byte getOwnVerticalAlignment()
	{
		return getOwnVerticalAlignmentValue() == null ? null : getOwnVerticalAlignmentValue().getValueByte();
	}
		
	/**
	 *
	 */
	public VerticalAlignEnum getVerticalAlignmentValue()
	{
		return ((JRTemplateImage)this.template).getVerticalAlignmentValue();
	}
		
	/**
	 *
	 */
	public VerticalAlignEnum getOwnVerticalAlignmentValue()
	{
		return ((JRTemplateImage)this.template).getOwnVerticalAlignmentValue();
	}
		
	/**
	 * @deprecated Replaced by {@link #setVerticalAlignment(VerticalAlignEnum)}.
	 */
	public void setVerticalAlignment(byte verticalAlignment)
	{
		throw new UnsupportedOperationException();
	}
		
	/**
	 * @deprecated Replaced by {@link #setVerticalAlignment(VerticalAlignEnum)}.
	 */
	public void setVerticalAlignment(Byte verticalAlignment)
	{
		throw new UnsupportedOperationException();
	}
		
	/**
	 *
	 */
	public void setVerticalAlignment(VerticalAlignEnum verticalAlignment)
	{
		throw new UnsupportedOperationException();
	}
		
	/**
	 *
	 */
	public boolean isLazy()
	{
		return ((JRTemplateImage)this.template).isLazy();
	}

	/**
	 *
	 */
	public void setLazy(boolean isLazy)
	{
	}

	/**
	 * @deprecated Replaced by {@link #getOnErrorTypeValue()}.
	 */
	public byte getOnErrorType()
	{
		return getOnErrorTypeValue().getValue();
	}
		
	/**
	 * @deprecated Replaced by {@link #setOnErrorType(OnErrorTypeEnum)}.
	 */
	public void setOnErrorType(byte onErrorType)
	{
		throw new UnsupportedOperationException();
	}

	/**
	 *
	 */
	public OnErrorTypeEnum getOnErrorTypeValue()
	{
		return ((JRTemplateImage)this.template).getOnErrorTypeValue();
	}
		
	/**
	 *
	 */
	public void setOnErrorType(OnErrorTypeEnum onErrorType)
	{
		throw new UnsupportedOperationException();
	}

	/**
	 *
	 */
	public JRLineBox getLineBox()
	{
		return ((JRTemplateImage)template).getLineBox();
	}
		
	/**
	 *
	 */
	public String getAnchorName()
	{
		return this.anchorName;
	}
		
	/**
	 *
	 */
	public void setAnchorName(String anchorName)
	{
		this.anchorName = anchorName;
	}
		
	/**
	 * @deprecated Replaced by {@link #getHyperlinkTypeValue()}.
	 */
	public byte getHyperlinkType()
	{
		return getHyperlinkTypeValue().getValue();
	}
		
	/**
	 *
	 */
	public HyperlinkTypeEnum getHyperlinkTypeValue()
	{
		return ((JRTemplateImage)this.template).getHyperlinkTypeValue();
	}
		
	/**
	 * @deprecated Replaced by {@link #setHyperlinkType(HyperlinkTypeEnum)}.
	 */
	public void setHyperlinkType(byte hyperlinkType)
	{
		setHyperlinkType(HyperlinkTypeEnum.getByValue(hyperlinkType));
	}

	/**
	 *
	 */
	public void setHyperlinkType(HyperlinkTypeEnum hyperlinkType)
	{
		throw new UnsupportedOperationException();
	}

	/**
	 * @deprecated Replaced by {@link #getHyperlinkTargetValue()}.
	 */
	public byte getHyperlinkTarget()
	{
		return getHyperlinkTargetValue().getValue();
	}
		
	/**
	 *
	 */
	public HyperlinkTargetEnum getHyperlinkTargetValue()
	{
		return ((JRTemplateImage)this.template).getHyperlinkTargetValue();
	}
		
	/**
	 * @deprecated Replaced by {@link #setHyperlinkTarget(HyperlinkTargetEnum)}.
	 */
	public void setHyperlinkTarget(byte hyperlinkTarget)
	{
		setHyperlinkTarget(HyperlinkTargetEnum.getByValue(hyperlinkTarget));
	}

	/**
	 *
	 */
	public void setHyperlinkTarget(HyperlinkTargetEnum hyperlinkTarget)
	{
		throw new UnsupportedOperationException();
	}

	/**
	 *
	 */
	public String getHyperlinkReference()
	{
		return this.hyperlinkReference;
	}
		
	/**
	 *
	 */
	public void setHyperlinkReference(String hyperlinkReference)
	{
		this.hyperlinkReference = hyperlinkReference;
	}
		
	/**
	 *
	 */
	public String getHyperlinkAnchor()
	{
		return this.hyperlinkAnchor;
	}
		
	/**
	 *
	 */
	public void setHyperlinkAnchor(String hyperlinkAnchor)
	{
		this.hyperlinkAnchor = hyperlinkAnchor;
	}
		
	/**
	 *
	 */
	public Integer getHyperlinkPage()
	{
		return this.hyperlinkPage;
	}
		
	/**
	 *
	 */
	public void setHyperlinkPage(Integer hyperlinkPage)
	{
		this.hyperlinkPage = hyperlinkPage;
	}


	public int getBookmarkLevel()
	{
		return bookmarkLevel;
	}


	public void setBookmarkLevel(int bookmarkLevel)
	{
		this.bookmarkLevel = bookmarkLevel;
	}

	
	public JRPrintHyperlinkParameters getHyperlinkParameters()
	{
		return hyperlinkParameters;
	}

	
	public void setHyperlinkParameters(JRPrintHyperlinkParameters parameters)
	{
		this.hyperlinkParameters = parameters;
	}

	
	public String getLinkType()
	{
		return ((JRTemplateImage) this.template).getLinkType();
	}

	public void setLinkType(String type)
	{
	}

	public String getLinkTarget()
	{
		return ((JRTemplateImage) this.template).getLinkTarget();
	}

	public void setLinkTarget(String target)
	{
	}

	
	public String getHyperlinkTooltip()
	{
		return hyperlinkTooltip;
	}

	
	public void setHyperlinkTooltip(String hyperlinkTooltip)
	{
		this.hyperlinkTooltip = hyperlinkTooltip;
	}

	public <T> void accept(PrintElementVisitor<T> visitor, T arg)
	{
		visitor.visit(this, arg);
	}

}
