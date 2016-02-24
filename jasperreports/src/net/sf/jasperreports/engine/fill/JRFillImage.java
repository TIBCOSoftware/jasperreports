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

import java.awt.Image;
import java.awt.geom.Dimension2D;
import java.io.File;
import java.io.InputStream;
import java.net.URL;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExpression;
import net.sf.jasperreports.engine.JRExpressionCollector;
import net.sf.jasperreports.engine.JRGroup;
import net.sf.jasperreports.engine.JRHyperlinkParameter;
import net.sf.jasperreports.engine.JRImage;
import net.sf.jasperreports.engine.JRLineBox;
import net.sf.jasperreports.engine.JRPrintElement;
import net.sf.jasperreports.engine.JRPrintHyperlinkParameters;
import net.sf.jasperreports.engine.JRPrintImage;
import net.sf.jasperreports.engine.JRVisitor;
import net.sf.jasperreports.engine.Renderable;
import net.sf.jasperreports.engine.RenderableUtil;
import net.sf.jasperreports.engine.type.EvaluationTimeEnum;
import net.sf.jasperreports.engine.type.HorizontalImageAlignEnum;
import net.sf.jasperreports.engine.type.HyperlinkTypeEnum;
import net.sf.jasperreports.engine.type.ModeEnum;
import net.sf.jasperreports.engine.type.OnErrorTypeEnum;
import net.sf.jasperreports.engine.type.ScaleImageEnum;
import net.sf.jasperreports.engine.type.VerticalImageAlignEnum;
import net.sf.jasperreports.engine.util.Pair;
import net.sf.jasperreports.engine.util.StyleUtil;
import net.sf.jasperreports.renderers.ResourceRenderer;


/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 */
public class JRFillImage extends JRFillGraphicElement implements JRImage
{

	public static final String EXCEPTION_MESSAGE_KEY_UNKNOWN_SOURCE_CLASS = "fill.image.unknown.source.class";

	/**
	 *
	 */
	private JRGroup evaluationGroup;

	/**
	 *
	 */
	private Renderable renderer;
	private boolean hasOverflowed;
	private Integer imageHeight;
	private Integer imageWidth;
	private Integer imageX;
	private String anchorName;
	private String hyperlinkReference;
	private Boolean hyperlinkWhen;
	private String hyperlinkAnchor;
	private Integer hyperlinkPage;
	private String hyperlinkTooltip;
	private JRPrintHyperlinkParameters hyperlinkParameters;

	protected final JRLineBox initLineBox;
	protected JRLineBox lineBox;


	/**
	 *
	 */
	protected JRFillImage(
		JRBaseFiller filler,
		JRImage image, 
		JRFillObjectFactory factory
		)
	{
		super(filler, image, factory);
		
		initLineBox = image.getLineBox().clone(this);

		evaluationGroup = factory.getGroup(image.getEvaluationGroup());
	}


	protected JRFillImage(JRFillImage image, JRFillCloneFactory factory)
	{
		super(image, factory);
		
		initLineBox = image.getLineBox().clone(this);

		evaluationGroup = image.evaluationGroup;
	}


	@Override
	protected void evaluateStyle(
		byte evaluation
		) throws JRException
	{
		super.evaluateStyle(evaluation);

		lineBox = null;
		
		if (providerStyle != null)
		{
			lineBox = initLineBox.clone(this);
			StyleUtil.appendBox(lineBox, providerStyle.getLineBox());
		}
	}


	@Override
	public ModeEnum getModeValue()
	{
		return getStyleResolver().getMode(this, ModeEnum.TRANSPARENT);
	}

	@Override
	public ScaleImageEnum getScaleImageValue()
	{
		return getStyleResolver().getScaleImageValue(this);
	}
		
	@Override
	public ScaleImageEnum getOwnScaleImageValue()
	{
		return providerStyle == null || providerStyle.getOwnScaleImageValue() == null ? ((JRImage)this.parent).getOwnScaleImageValue() : providerStyle.getOwnScaleImageValue();
	}

	@Override
	public void setScaleImage(ScaleImageEnum scaleImage)
	{
		throw new UnsupportedOperationException();
	}

	/**
	 * @deprecated Replaced by {@link #getHorizontalImageAlign()}.
	 */
	@Override
	public net.sf.jasperreports.engine.type.HorizontalAlignEnum getHorizontalAlignmentValue()
	{
		return net.sf.jasperreports.engine.type.HorizontalAlignEnum.getHorizontalAlignEnum(getHorizontalImageAlign());
	}
		
	/**
	 * @deprecated Replaced by {@link #getOwnHorizontalImageAlign()}.
	 */
	@Override
	public net.sf.jasperreports.engine.type.HorizontalAlignEnum getOwnHorizontalAlignmentValue()
	{
		return net.sf.jasperreports.engine.type.HorizontalAlignEnum.getHorizontalAlignEnum(getOwnHorizontalImageAlign());
	}
		
	/**
	 * @deprecated Replaced by {@link #setHorizontalImageAlign(HorizontalImageAlignEnum)}.
	 */
	@Override
	public void setHorizontalAlignment(net.sf.jasperreports.engine.type.HorizontalAlignEnum horizontalAlignmentValue)
	{
		setHorizontalImageAlign(net.sf.jasperreports.engine.type.HorizontalAlignEnum.getHorizontalImageAlignEnum(horizontalAlignmentValue));
	}

	/**
	 * @deprecated Replaced by {@link #getVerticalImageAlign()}.
	 */
	@Override
	public net.sf.jasperreports.engine.type.VerticalAlignEnum getVerticalAlignmentValue()
	{
		return net.sf.jasperreports.engine.type.VerticalAlignEnum.getVerticalAlignEnum(getVerticalImageAlign());
	}
		
	/**
	 * @deprecated Replaced by {@link #getOwnVerticalImageAlign()}.
	 */
	@Override
	public net.sf.jasperreports.engine.type.VerticalAlignEnum getOwnVerticalAlignmentValue()
	{
		return net.sf.jasperreports.engine.type.VerticalAlignEnum.getVerticalAlignEnum(getOwnVerticalImageAlign());
	}
		
	/**
	 * @deprecated Replaced by {@link #setVerticalImageAlign(VerticalImageAlignEnum)}.
	 */
	@Override
	public void setVerticalAlignment(net.sf.jasperreports.engine.type.VerticalAlignEnum verticalAlignmentValue)
	{
		setVerticalImageAlign(net.sf.jasperreports.engine.type.VerticalAlignEnum.getVerticalImageAlignEnum(verticalAlignmentValue));
	}
		
	@Override
	public HorizontalImageAlignEnum getHorizontalImageAlign()
	{
		return getStyleResolver().getHorizontalImageAlign(this);
	}
		
	@Override
	public HorizontalImageAlignEnum getOwnHorizontalImageAlign()
	{
		return providerStyle == null || providerStyle.getOwnHorizontalImageAlign() == null ? ((JRImage)this.parent).getOwnHorizontalImageAlign() : providerStyle.getOwnHorizontalImageAlign();
	}

	@Override
	public void setHorizontalImageAlign(HorizontalImageAlignEnum horizontalAlignment)
	{
		throw new UnsupportedOperationException();
	}
		
	@Override
	public VerticalImageAlignEnum getVerticalImageAlign()
	{
		return getStyleResolver().getVerticalImageAlign(this);
	}
		
	@Override
	public VerticalImageAlignEnum getOwnVerticalImageAlign()
	{
		return providerStyle == null || providerStyle.getOwnVerticalImageAlign() == null ? ((JRImage)this.parent).getOwnVerticalImageAlign() : providerStyle.getOwnVerticalImageAlign();
	}

	@Override
	public void setVerticalImageAlign(VerticalImageAlignEnum verticalAlignment)
	{
		throw new UnsupportedOperationException();
	}
		
	/**
	 * @deprecated Replaced by {@link #getUsingCache()}.
	 */
	@Override
	public boolean isUsingCache()
	{
		return ((JRImage)this.parent).isUsingCache();
	}
		
	/**
	 * @deprecated Replaced by {@link #getUsingCache()}.
	 */
	@Override
	public Boolean isOwnUsingCache()
	{
		return ((JRImage)this.parent).isOwnUsingCache();
	}
		
	@Override
	public Boolean getUsingCache()
	{
		return ((JRImage)this.parent).getUsingCache();
	}
		
	@Override
	public void setUsingCache(boolean isUsingCache)
	{
	}
		
	@Override
	public void setUsingCache(Boolean isUsingCache)
	{
	}
		
	@Override
	public boolean isLazy()
	{
		return ((JRImage)this.parent).isLazy();
	}
		
	@Override
	public void setLazy(boolean isLazy)
	{
	}

	@Override
	public OnErrorTypeEnum getOnErrorTypeValue()
	{
		return ((JRImage)this.parent).getOnErrorTypeValue();
	}
		
	@Override
	public void setOnErrorType(OnErrorTypeEnum onErrorType)
	{
		throw new UnsupportedOperationException();
	}

	@Override
	public EvaluationTimeEnum getEvaluationTimeValue()
	{
		return ((JRImage)this.parent).getEvaluationTimeValue();
	}
		
	@Override
	public JRGroup getEvaluationGroup()
	{
		return this.evaluationGroup;
	}
		
	@Override
	public JRLineBox getLineBox()
	{
		return lineBox == null ? initLineBox : lineBox;
	}

	/**
	 * @deprecated Replaced by {@link #getHyperlinkTypeValue()}.
	 */
	public byte getHyperlinkType()
	{
		return getHyperlinkTypeValue().getValue();
	}

	@Override
	public HyperlinkTypeEnum getHyperlinkTypeValue()
	{
		return ((JRImage)parent).getHyperlinkTypeValue();
	}
		
	@Override
	public byte getHyperlinkTarget()
	{
		return ((JRImage)this.parent).getHyperlinkTarget();
	}
		
	@Override
	public String getLinkTarget()
	{
		return ((JRImage)this.parent).getLinkTarget();
	}
		
	@Override
	public JRExpression getExpression()
	{
		return ((JRImage)this.parent).getExpression();
	}

	@Override
	public JRExpression getAnchorNameExpression()
	{
		return ((JRImage)this.parent).getAnchorNameExpression();
	}

	@Override
	public JRExpression getHyperlinkReferenceExpression()
	{
		return ((JRImage)this.parent).getHyperlinkReferenceExpression();
	}

	@Override
	public JRExpression getHyperlinkWhenExpression()
	{
		return ((JRImage)this.parent).getHyperlinkWhenExpression();
	}

	@Override
	public JRExpression getHyperlinkAnchorExpression()
	{
		return ((JRImage)this.parent).getHyperlinkAnchorExpression();
	}

	@Override
	public JRExpression getHyperlinkPageExpression()
	{
		return ((JRImage)this.parent).getHyperlinkPageExpression();
	}

		
	/**
	 *
	 */
	protected Renderable getRenderable()
	{
		return this.renderer;
	}
		
	/**
	 *
	 */
	protected String getAnchorName()
	{
		return this.anchorName;
	}

	/**
	 *
	 */
	protected String getHyperlinkReference()
	{
		return this.hyperlinkReference;
	}

	/**
	 *
	 */
	protected String getHyperlinkAnchor()
	{
		return this.hyperlinkAnchor;
	}

	/**
	 *
	 */
	protected Integer getHyperlinkPage()
	{
		return this.hyperlinkPage;
	}
		

	protected String getHyperlinkTooltip()
	{
		return this.hyperlinkTooltip;
	}
		

	/**
	 *
	 */
	protected JRTemplateImage getJRTemplateImage()
	{
		return (JRTemplateImage) getElementTemplate();
	}

	@Override
	protected JRTemplateElement createElementTemplate()
	{
		JRTemplateImage template = 
			new JRTemplateImage(
				getElementOrigin(), 
				filler.getJasperPrint().getDefaultStyleProvider(), 
				this
				);
		
		if (getScaleImageValue() == ScaleImageEnum.REAL_HEIGHT
				|| getScaleImageValue() == ScaleImageEnum.REAL_SIZE)
		{
			template.setScaleImage(ScaleImageEnum.RETAIN_SHAPE);
		}
		
		return template;
	}


	@Override
	protected void evaluate(
		byte evaluation
		) throws JRException
	{
		initDelayedEvaluations();
		
		reset();
		
		evaluatePrintWhenExpression(evaluation);

		if (isPrintWhenExpressionNull() || isPrintWhenTrue())
		{
			if (isEvaluateNow())
			{
				hasOverflowed = false;
				evaluateImage(evaluation);
			}
		}
	}
	

	/**
	 *
	 */
	protected void evaluateImage(
		byte evaluation
		) throws JRException
	{
		evaluateProperties(evaluation);
		evaluateStyle(evaluation);
		
		JRExpression expression = this.getExpression();

		Renderable newRenderer = null;
		
		Object source = evaluateExpression(expression, evaluation);
		if (source != null)
		{
			Boolean isUsingCache = getUsingCache();
			if (isUsingCache == null)
			{
				isUsingCache = source instanceof String;
			}
			
			Object imgKey = source;
			if (source instanceof String)
			{
				imgKey = new Pair<Boolean, String>(isLazy(), (String)source);
			}

			if (isUsingCache && filler.fillContext.hasLoadedImage(imgKey))
			{
				newRenderer = filler.fillContext.getLoadedImage(imgKey).getRenderable();
			}
			else
			{
				@SuppressWarnings("deprecation")
				net.sf.jasperreports.engine.JRRenderable deprecatedRenderable = 
					source instanceof net.sf.jasperreports.engine.JRRenderable 
					? (net.sf.jasperreports.engine.JRRenderable)source 
					: null;

				if (source instanceof String)
				{
					String strSource = (String) source;
					if (isLazy())
					{
						newRenderer = ResourceRenderer.getInstance(strSource, true);
					}
					else
					{
						newRenderer = RenderableUtil.getInstance(filler.getJasperReportsContext()).getNonLazyRenderable(strSource, getOnErrorTypeValue());
					}
				}
				else if (source instanceof Image)
				{
					Image img = (Image) source;
					newRenderer = RenderableUtil.getInstance(filler.getJasperReportsContext()).getRenderable(img, getOnErrorTypeValue());
				}
				else if (source instanceof InputStream)
				{
					InputStream is = (InputStream) source;
					newRenderer = RenderableUtil.getInstance(filler.getJasperReportsContext()).getRenderable(is, getOnErrorTypeValue());
				}
				else if (source instanceof URL)
				{
					URL url = (URL) source;
					newRenderer = RenderableUtil.getInstance(filler.getJasperReportsContext()).getRenderable(url, getOnErrorTypeValue());
				}
				else if (source instanceof File)
				{
					File file = (File) source;
					newRenderer = RenderableUtil.getInstance(filler.getJasperReportsContext()).getRenderable(file, getOnErrorTypeValue());
				}
				else if (source instanceof Renderable)
				{
					newRenderer = (Renderable) source;
				}
				else if (deprecatedRenderable != null)
				{
					@SuppressWarnings("deprecation")
					Renderable wrappingRenderable = 
						RenderableUtil.getWrappingRenderable(deprecatedRenderable);
					newRenderer = wrappingRenderable;
				}
				else
				{
					newRenderer = 
						RenderableUtil.getInstance(filler.getJasperReportsContext()).getOnErrorRenderer(
							getOnErrorTypeValue(), 
							new JRException(
									EXCEPTION_MESSAGE_KEY_UNKNOWN_SOURCE_CLASS,  
									new Object[]{source.getClass().getName()} 
									)
							);
				}

				if (isUsingCache)
				{
					JRPrintImage img = new JRTemplatePrintImage(getJRTemplateImage(), 
							printElementOriginator);//doesn't actually need a printElementId
					img.setRenderable(newRenderer);
					filler.fillContext.registerLoadedImage(imgKey, img);
				}
			}
		}

		setValueRepeating(this.renderer == newRenderer);
	
		this.renderer = newRenderer;
		
		this.anchorName = (String) evaluateExpression(this.getAnchorNameExpression(), evaluation);
		this.hyperlinkReference = (String) evaluateExpression(this.getHyperlinkReferenceExpression(), evaluation);
		this.hyperlinkWhen = (Boolean) evaluateExpression(this.getHyperlinkWhenExpression(), evaluation);
		this.hyperlinkAnchor = (String) evaluateExpression(this.getHyperlinkAnchorExpression(), evaluation);
		this.hyperlinkPage = (Integer) evaluateExpression(this.getHyperlinkPageExpression(), evaluation);
		this.hyperlinkTooltip = (String) evaluateExpression(this.getHyperlinkTooltipExpression(), evaluation);
		hyperlinkParameters = JRFillHyperlinkHelper.evaluateHyperlinkParameters(this, expressionEvaluator, evaluation);
	}
	

	@Override
	protected boolean prepare(
		int availableHeight,
		boolean isOverflow
		) throws JRException
	{
		boolean willOverflow = false;

		if (
			this.isPrintWhenExpressionNull() ||
			( !this.isPrintWhenExpressionNull() && 
			this.isPrintWhenTrue() )
			)
		{
			this.setToPrint(true);
		}
		else
		{
			this.setToPrint(false);
		}

		if (!this.isToPrint())
		{
			return willOverflow;
		}
		
		boolean isToPrint = true;
		boolean isReprinted = false;

		if (isEvaluateNow())
		{
			if (isOverflow && this.isAlreadyPrinted() && !this.isPrintWhenDetailOverflows())
			{
				isToPrint = false;
			}
	
			if (
				isToPrint && 
				this.isPrintWhenExpressionNull() &&
				!this.isPrintRepeatedValues() &&
				isValueRepeating()
				)
			{
				if (
					( !this.isPrintInFirstWholeBand() || !this.getBand().isFirstWholeOnPageColumn() ) &&
					( this.getPrintWhenGroupChanges() == null || !this.getBand().isNewGroup(this.getPrintWhenGroupChanges()) ) &&
					( !isOverflow || !this.isPrintWhenDetailOverflows() )
					)
				{
					isToPrint = false;
				}
			}

			if (
				isToPrint && 
				this.isRemoveLineWhenBlank() &&
				this.getRenderable() == null
				)
			{
				isToPrint = false;
			}
	
			if (isToPrint)
			{
				if (availableHeight < getRelativeY() + getHeight())
				{
					isToPrint = false;
					willOverflow = true;
				}
				else if (
					!isLazy() 
					&& (getScaleImageValue() == ScaleImageEnum.REAL_HEIGHT || getScaleImageValue() == ScaleImageEnum.REAL_SIZE)
					)
				{
					int padding = getLineBox().getBottomPadding().intValue() 
							+ getLineBox().getTopPadding().intValue();
					boolean reprinted = isOverflow 
						&& (this.isPrintWhenDetailOverflows() 
								&& (this.isAlreadyPrinted() 
										|| (!this.isAlreadyPrinted() && !this.isPrintRepeatedValues())));
					boolean imageOverflowAllowed = 
							filler.isBandOverFlowAllowed() && !reprinted && !hasOverflowed;

					if (renderer == null)
					{
						// if renderer is null, it means the isRemoveLineWhenBlank was false further up; 
						// no need to do anything here 
					}
					else
					{
						try
						{
							renderer.getDimension(filler.getJasperReportsContext());
						}
						catch (Exception e)
						{
							renderer = RenderableUtil.getInstance(filler.getJasperReportsContext()).handleImageError(e, getOnErrorTypeValue());
						}
						
						if (renderer == null) // OnErrorTypeEnum.BLANK can return null above
						{
							isToPrint = !isRemoveLineWhenBlank();
						}
						else
						{
							if (renderer instanceof ResourceRenderer)
							{
								renderer = filler.fillContext.getResourceRendererCache().getLoadedRenderer((ResourceRenderer)renderer);
							}
							
							boolean fits = true; 

							Dimension2D imageSize = renderer.getDimension(filler.getJasperReportsContext());
							if (imageSize != null)
							{
								fits = 
									fitImage(
										imageSize, 
										availableHeight - getRelativeY() - padding, 
										imageOverflowAllowed, 
										getHorizontalImageAlign()
										);
							}

							if (fits)
							{
								if (imageHeight != null)
								{
									setPrepareHeight(imageHeight.intValue() + padding);
								}
							}
							else
							{
								hasOverflowed = true;
								isToPrint = false;
								willOverflow = true;
								setPrepareHeight(availableHeight - getRelativeY() - padding);
							}
						}
					}
				}
			}
			
			if (
				isToPrint && 
				isOverflow && 
				//(this.isAlreadyPrinted() || !this.isPrintRepeatedValues())
				(this.isPrintWhenDetailOverflows() && (this.isAlreadyPrinted() || (!this.isAlreadyPrinted() && !this.isPrintRepeatedValues())))
				)
			{
				isReprinted = true;
			}
		}
		else
		{
			if (isOverflow && this.isAlreadyPrinted() && !this.isPrintWhenDetailOverflows())
			{
				isToPrint = false;
			}
	
			if (
				isToPrint && 
				availableHeight < this.getRelativeY() + getHeight()
				)
			{
				isToPrint = false;
				willOverflow = true;
			}
			
			if (
				isToPrint && 
				isOverflow && 
				//(this.isAlreadyPrinted() || !this.isPrintRepeatedValues())
				(this.isPrintWhenDetailOverflows() && (this.isAlreadyPrinted() || (!this.isAlreadyPrinted() && !this.isPrintRepeatedValues())))
				)
			{
				isReprinted = true;
			}
		}

		this.setToPrint(isToPrint);
		this.setReprinted(isReprinted);
		
		return willOverflow;
	}

	@Override
	protected void reset()
	{
		imageHeight = null;
		imageWidth = null;
		imageX = null;
		
		super.reset();
	}

	protected boolean fitImage(
		Dimension2D imageSize, 
		int availableHeight, 
		boolean overflowAllowed,
		HorizontalImageAlignEnum hAlign
		) throws JRException
	{
		imageHeight = null;
		imageWidth = null;
		imageX = null;
		
		int realHeight = (int) imageSize.getHeight();
		int realWidth = (int) imageSize.getWidth();
		boolean fitted;
		
		int reducedHeight = realHeight;
		int reducedWidth = realWidth;
		if (realWidth > getWidth())
		{
			double wRatio = ((double) getWidth()) / realWidth;
			reducedHeight = (int) (wRatio * realHeight);
			reducedWidth = getWidth();
		}		
		
		if (reducedHeight <= availableHeight)
		{
			imageHeight = Integer.valueOf(reducedHeight);
			if (getScaleImageValue() == ScaleImageEnum.REAL_SIZE)
			{
				imageWidth = Integer.valueOf(reducedWidth);
			}
			fitted = true;
		}
		else if (overflowAllowed)
		{
			fitted = false;
		}
		else
		{
			imageHeight = Integer.valueOf(availableHeight);
			if (getScaleImageValue() == ScaleImageEnum.REAL_SIZE)
			{
				double hRatio = ((double) availableHeight) / realHeight;
				imageWidth = Integer.valueOf((int) (hRatio * realWidth));
			}
			fitted = true;
		}

		if (imageWidth != null && imageWidth.intValue() != getWidth())
		{
			switch (hAlign)
			{
			case RIGHT:
				imageX = Integer.valueOf(getX() + getWidth() - imageWidth.intValue());
				break;
			case CENTER:
				imageX = Integer.valueOf(getX() + (getWidth() - imageWidth.intValue()) / 2);
				break;
			default:
				break;
			}
		}
		
		return fitted;
	}

	@Override
	protected JRPrintElement fill() throws JRException
	{
		EvaluationTimeEnum evaluationTime = this.getEvaluationTimeValue();
		JRTemplatePrintImage printImage;
		JRRecordedValuesPrintImage recordedValuesImage;
		if (isEvaluateAuto())
		{
			printImage = recordedValuesImage = new JRRecordedValuesPrintImage(getJRTemplateImage(), printElementOriginator);
		}
		else
		{
			printImage = new JRTemplatePrintImage(getJRTemplateImage(), printElementOriginator);
			recordedValuesImage = null;
		}
		
		printImage.setUUID(this.getUUID());
		printImage.setX(this.getX());
		printImage.setY(this.getRelativeY());
		printImage.setWidth(getWidth());
		printImage.setHeight(this.getStretchHeight());
		printImage.setBookmarkLevel(getBookmarkLevel());

		if (isEvaluateNow())
		{
			this.copy(printImage);
		}
		else if (isEvaluateAuto())
		{
			initDelayedEvaluationPrint(recordedValuesImage);
		}
		else
		{
			filler.addBoundElement(this, printImage, evaluationTime, getEvaluationGroup(), band);
		}
		
		return printImage;
	}
		

	/**
	 *
	 */
	protected void copy(JRPrintImage printImage)
	{
		printImage.setUUID(getUUID());

		if (imageX != null)
		{
			printImage.setX(imageX.intValue());
		}
		if (imageWidth != null)
		{
			printImage.setWidth(imageWidth.intValue());
		}
		
		printImage.setRenderable(getRenderable());
		printImage.setAnchorName(getAnchorName());
		if (getHyperlinkWhenExpression() == null || hyperlinkWhen == Boolean.TRUE)
		{
			printImage.setHyperlinkReference(getHyperlinkReference());
			printImage.setHyperlinkAnchor(getHyperlinkAnchor());
			printImage.setHyperlinkPage(getHyperlinkPage());
			printImage.setHyperlinkTooltip(getHyperlinkTooltip());
			printImage.setHyperlinkParameters(hyperlinkParameters);
		}
		else
		{
			if (printImage instanceof JRTemplatePrintImage)//this is normally the case
			{
				((JRTemplatePrintImage) printImage).setHyperlinkOmitted(true);
			}
			
			printImage.setHyperlinkReference(null);
		}
		transferProperties(printImage);
	}


	@Override
	public void collectExpressions(JRExpressionCollector collector)
	{
		collector.collect(this);
	}

	@Override
	public void visit(JRVisitor visitor)
	{
		visitor.visitImage(this);
	}

	
	@Override
	protected void resolveElement(JRPrintElement element, byte evaluation) throws JRException
	{
		evaluateImage(evaluation);

		JRPrintImage printImage = (JRPrintImage) element;

		if (getScaleImageValue() == ScaleImageEnum.REAL_SIZE)//to avoid get dimension and thus unnecessarily load the image
		{
			if (renderer != null)
			{
				try
				{
					renderer.getDimension(filler.getJasperReportsContext());
				}
				catch (Exception e)
				{
					renderer = RenderableUtil.getInstance(filler.getJasperReportsContext()).handleImageError(e, getOnErrorTypeValue());
				}
				
				if (renderer != null) // OnErrorTypeEnum.BLANK can return null above
				{
					if (renderer instanceof ResourceRenderer)
					{
						renderer = filler.fillContext.getResourceRendererCache().getLoadedRenderer((ResourceRenderer)renderer);
					}
					
					Dimension2D imageSize = renderer.getDimension(filler.getJasperReportsContext());
					if (imageSize != null)
					{
						int padding = 
							printImage.getLineBox().getBottomPadding().intValue() 
							+ printImage.getLineBox().getTopPadding().intValue();
							
						fitImage(
							imageSize,
							getHeight() - padding, 
							false, 
							printImage.getHorizontalImageAlign()
							);
					}
				}
			}
		}
		
		copy(printImage);
		filler.updateBookmark(element);
	}


	@Override
	public int getBookmarkLevel()
	{
		return ((JRImage)this.parent).getBookmarkLevel();
	}


	@Override
	public JRFillCloneable createClone(JRFillCloneFactory factory)
	{
		return new JRFillImage(this, factory);
	}


	@Override
	protected void collectDelayedEvaluations()
	{
		super.collectDelayedEvaluations();
		
		collectDelayedEvaluations(getExpression());
		collectDelayedEvaluations(getAnchorNameExpression());
		collectDelayedEvaluations(getHyperlinkReferenceExpression());
		collectDelayedEvaluations(getHyperlinkWhenExpression());
		collectDelayedEvaluations(getHyperlinkAnchorExpression());
		collectDelayedEvaluations(getHyperlinkPageExpression());	
	}


	@Override
	public JRHyperlinkParameter[] getHyperlinkParameters()
	{
		return ((JRImage) parent).getHyperlinkParameters();
	}


	@Override
	public String getLinkType()
	{
		return ((JRImage) parent).getLinkType();
	}


	@Override
	public JRExpression getHyperlinkTooltipExpression()
	{
		return ((JRImage) parent).getHyperlinkTooltipExpression();
	}

}
