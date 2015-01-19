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

import java.io.IOException;
import java.io.ObjectInputStream;

import net.sf.jasperreports.engine.JRAnchor;
import net.sf.jasperreports.engine.JRConstants;
import net.sf.jasperreports.engine.JRLineBox;
import net.sf.jasperreports.engine.JRPrintHyperlinkParameters;
import net.sf.jasperreports.engine.JRPrintImage;
import net.sf.jasperreports.engine.PrintElementVisitor;
import net.sf.jasperreports.engine.Renderable;
import net.sf.jasperreports.engine.RenderableUtil;
import net.sf.jasperreports.engine.type.HorizontalImageAlignEnum;
import net.sf.jasperreports.engine.type.HyperlinkTargetEnum;
import net.sf.jasperreports.engine.type.HyperlinkTypeEnum;
import net.sf.jasperreports.engine.type.OnErrorTypeEnum;
import net.sf.jasperreports.engine.type.ScaleImageEnum;
import net.sf.jasperreports.engine.type.VerticalImageAlignEnum;
import net.sf.jasperreports.engine.virtualization.VirtualizationInput;
import net.sf.jasperreports.engine.virtualization.VirtualizationOutput;


/**
 * Implementation of {@link net.sf.jasperreports.engine.JRPrintImage} that uses
 * a {@link net.sf.jasperreports.engine.fill.JRTemplateImage} instance to
 * store common attributes. 
 * 
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 */
public class JRTemplatePrintImage extends JRTemplatePrintGraphicElement implements JRPrintImage
{


	/**
	 *
	 */
	private static final long serialVersionUID = JRConstants.SERIAL_VERSION_UID;
	
	private static final int SERIALIZATION_FLAG_CACHED_RENDERER = 1;
	private static final int SERIALIZATION_FLAG_ANCHOR = 1 << 1;
	private static final int SERIALIZATION_FLAG_HYPERLINK = 1 << 2;

	/**
	 *
	 */
	private Renderable renderable;
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
	
	public JRTemplatePrintImage()
	{
		
	}
	
	/**
	 * Creates a print image element.
	 * 
	 * @param image the template image that the element will use
	 * @deprecated provide a source Id via {@link #JRTemplatePrintImage(JRTemplateImage, int)}
	 */
	public JRTemplatePrintImage(JRTemplateImage image)
	{
		super(image);
	}

	/**
	 * Creates a print image element.
	 * 
	 * @param image the template image that the element will use
	 * @param sourceElementId the Id of the source element
	 * @deprecated replaced by {@link #JRTemplatePrintImage(JRTemplateImage, PrintElementOriginator)}
	 */
	public JRTemplatePrintImage(JRTemplateImage image, int sourceElementId)
	{
		super(image, sourceElementId);
	}

	/**
	 * Creates a print image element.
	 * 
	 * @param image the template image that the element will use
	 * @param originator
	 */
	public JRTemplatePrintImage(JRTemplateImage image, PrintElementOriginator originator)
	{
		super(image, originator);
	}
	
	/**
	 *
	 */
	public Renderable getRenderable()
	{
		return renderable;
	}
		
	/**
	 *
	 */
	public void setRenderable(Renderable renderable)
	{
		this.renderable = renderable;
	}
		
	/**
	 * @deprecated Replaced by {@link #getRenderable()}.
	 */
	public net.sf.jasperreports.engine.JRRenderable getRenderer()
	{
		return getRenderable();
	}
		
	/**
	 * @deprecated Replaced by {@link #setRenderable(Renderable)}.
	 */
	public void setRenderer(net.sf.jasperreports.engine.JRRenderable renderer)
	{
		setRenderable(RenderableUtil.getWrappingRenderable(renderer));
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
	 * @deprecated Replaced by {@link #getHorizontalImageAlign()}.
	 */
	public net.sf.jasperreports.engine.type.HorizontalAlignEnum getHorizontalAlignmentValue()
	{
		return ((JRTemplateImage)this.template).getHorizontalAlignmentValue();
	}
		
	/**
	 * @deprecated Replaced by {@link #getOwnHorizontalImageAlign()}.
	 */
	public net.sf.jasperreports.engine.type.HorizontalAlignEnum getOwnHorizontalAlignmentValue()
	{
		return ((JRTemplateImage)this.template).getOwnHorizontalAlignmentValue();
	}
		
	/**
	 * @deprecated Replaced by {@link #setHorizontalImageAlign(HorizontalImageAlignEnum)}.
	 */
	public void setHorizontalAlignment(net.sf.jasperreports.engine.type.HorizontalAlignEnum horizontalAlignment)
	{
		throw new UnsupportedOperationException();
	}
		
	/**
	 * @deprecated Replaced by {@link #getVerticalImageAlign()}.
	 */
	public net.sf.jasperreports.engine.type.VerticalAlignEnum getVerticalAlignmentValue()
	{
		return ((JRTemplateImage)this.template).getVerticalAlignmentValue();
	}
		
	/**
	 * @deprecated Replaced by {@link #getOwnVerticalImageAlign()}.
	 */
	public net.sf.jasperreports.engine.type.VerticalAlignEnum getOwnVerticalAlignmentValue()
	{
		return ((JRTemplateImage)this.template).getOwnVerticalAlignmentValue();
	}
		
	/**
	 * @deprecated Replaced by {@link #setVerticalImageAlign(VerticalImageAlignEnum)}.
	 */
	public void setVerticalAlignment(net.sf.jasperreports.engine.type.VerticalAlignEnum verticalAlignment)
	{
		throw new UnsupportedOperationException();
	}
		
	/**
	 *
	 */
	public HorizontalImageAlignEnum getHorizontalImageAlign()
	{
		return ((JRTemplateImage)this.template).getHorizontalImageAlign();
	}
		
	/**
	 *
	 */
	public HorizontalImageAlignEnum getOwnHorizontalImageAlign()
	{
		return ((JRTemplateImage)this.template).getOwnHorizontalImageAlign();
	}
		
	/**
	 *
	 */
	public void setHorizontalImageAlign(HorizontalImageAlignEnum horizontalAlignment)
	{
		throw new UnsupportedOperationException();
	}
		
	/**
	 *
	 */
	public VerticalImageAlignEnum getVerticalImageAlign()
	{
		return ((JRTemplateImage)this.template).getVerticalImageAlign();
	}
		
	/**
	 *
	 */
	public VerticalImageAlignEnum getOwnVerticalImageAlign()
	{
		return ((JRTemplateImage)this.template).getOwnVerticalImageAlign();
	}
		
	/**
	 *
	 */
	public void setVerticalImageAlign(VerticalImageAlignEnum verticalAlignment)
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
	 *
	 */
	public HyperlinkTypeEnum getHyperlinkTypeValue()
	{
		return ((JRTemplateImage)this.template).getHyperlinkTypeValue();
	}
		
	/**
	 *
	 */
	public void setHyperlinkType(HyperlinkTypeEnum hyperlinkType)
	{
		throw new UnsupportedOperationException();
	}

	/**
	 *
	 */
	public HyperlinkTargetEnum getHyperlinkTargetValue()
	{
		return ((JRTemplateImage)this.template).getHyperlinkTargetValue();
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

	
	/*
	 * These fields are only for serialization backward compatibility.
	 */
	/**
	 * @deprecated
	 */
	private net.sf.jasperreports.engine.JRRenderable renderer;
	
	@SuppressWarnings("deprecation")
	private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException
	{
		in.defaultReadObject();

		if (renderer != null && renderable == null)
		{
			if (renderer instanceof Renderable)
			{
				renderable = (Renderable)renderer;
			}
			else
			{
				renderable = RenderableUtil.getWrappingRenderable(renderer);
			}
		}
	}


	public <T> void accept(PrintElementVisitor<T> visitor, T arg)
	{
		visitor.visit(this, arg);
	}

	@Override
	public void writeVirtualized(VirtualizationOutput out) throws IOException
	{
		super.writeVirtualized(out);
		
		JRVirtualizationContext context = out.getVirtualizationContext();
		
		int flags = 0;
		boolean cachedRenderer = renderable != null && context.hasCachedRenderer(renderable.getId());
		boolean hasAnchor = anchorName != null || bookmarkLevel != JRAnchor.NO_BOOKMARK;
		boolean hasHyperlink = hyperlinkReference != null || hyperlinkAnchor != null
				|| hyperlinkPage != null || hyperlinkTooltip != null || hyperlinkParameters != null;
		
		if (cachedRenderer)
		{
			flags |= SERIALIZATION_FLAG_CACHED_RENDERER;
		}
		if (hasAnchor)
		{
			flags |= SERIALIZATION_FLAG_ANCHOR;
		}
		if (hasHyperlink)
		{
			flags |= SERIALIZATION_FLAG_HYPERLINK;
		}
		
		out.writeByte(flags);
		
		if (cachedRenderer)
		{
			out.writeJRObject(renderable.getId());
		}
		else
		{
			out.writeJRObject(renderable);
		}
		
		if (hasAnchor)
		{
			out.writeJRObject(anchorName);
			out.writeIntCompressed(bookmarkLevel);
		}

		if (hasHyperlink)
		{
			out.writeJRObject(hyperlinkReference);
			out.writeJRObject(hyperlinkAnchor);
			out.writeJRObject(hyperlinkPage);
			out.writeJRObject(hyperlinkTooltip);
			out.writeJRObject(hyperlinkParameters);
		}		
	}

	@Override
	public void readVirtualized(VirtualizationInput in) throws IOException
	{
		super.readVirtualized(in);
		
		JRVirtualizationContext context = in.getVirtualizationContext();
		int flags = in.readUnsignedByte();
		
		if ((flags & SERIALIZATION_FLAG_CACHED_RENDERER) != 0)
		{
			String renderedId = (String) in.readJRObject();
			renderable = context.getCachedRenderer(renderedId);
			if (renderable == null)
			{
				throw new RuntimeException();
			}
		}
		else
		{
			renderable = (Renderable) in.readJRObject();
		}
		
		if ((flags & SERIALIZATION_FLAG_ANCHOR) != 0)
		{
			anchorName = (String) in.readJRObject();
			bookmarkLevel = in.readIntCompressed();
		}
		else
		{
			bookmarkLevel = JRAnchor.NO_BOOKMARK;
		}

		if ((flags & SERIALIZATION_FLAG_HYPERLINK) != 0)
		{
			hyperlinkReference = (String) in.readJRObject();
			hyperlinkAnchor = (String) in.readJRObject();
			hyperlinkPage = (Integer) in.readJRObject();
			hyperlinkTooltip = (String) in.readJRObject();
			hyperlinkParameters = (JRPrintHyperlinkParameters) in.readJRObject();
		}
	}

}
