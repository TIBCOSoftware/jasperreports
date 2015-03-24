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
import net.sf.jasperreports.engine.JRImageRenderer;
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
import net.sf.jasperreports.engine.util.JRStyleResolver;


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


	/**
	 *
	 */
	protected void evaluateStyle(
		byte evaluation
		) throws JRException
	{
		super.evaluateStyle(evaluation);

		lineBox = null;
		
		if (providerStyle != null)
		{
			lineBox = initLineBox.clone(this);
			JRStyleResolver.appendBox(lineBox, providerStyle.getLineBox());
		}
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
	public ScaleImageEnum getScaleImageValue()
	{
		return JRStyleResolver.getScaleImageValue(this);
	}
		
	public ScaleImageEnum getOwnScaleImageValue()
	{
		return providerStyle == null || providerStyle.getOwnScaleImageValue() == null ? ((JRImage)this.parent).getOwnScaleImageValue() : providerStyle.getOwnScaleImageValue();
	}

	/**
	 *
	 */
	public void setScaleImage(ScaleImageEnum scaleImage)
	{
		throw new UnsupportedOperationException();
	}

	/**
	 * @deprecated Replaced by {@link #getHorizontalImageAlign()}.
	 */
	public net.sf.jasperreports.engine.type.HorizontalAlignEnum getHorizontalAlignmentValue()
	{
		return net.sf.jasperreports.engine.type.HorizontalAlignEnum.getHorizontalAlignEnum(getHorizontalImageAlign());
	}
		
	/**
	 * @deprecated Replaced by {@link #getOwnHorizontalImageAlign()}.
	 */
	public net.sf.jasperreports.engine.type.HorizontalAlignEnum getOwnHorizontalAlignmentValue()
	{
		return net.sf.jasperreports.engine.type.HorizontalAlignEnum.getHorizontalAlignEnum(getOwnHorizontalImageAlign());
	}
		
	/**
	 * @deprecated Replaced by {@link #setHorizontalImageAlign(HorizontalImageAlignEnum)}.
	 */
	public void setHorizontalAlignment(net.sf.jasperreports.engine.type.HorizontalAlignEnum horizontalAlignmentValue)
	{
		setHorizontalImageAlign(net.sf.jasperreports.engine.type.HorizontalAlignEnum.getHorizontalImageAlignEnum(horizontalAlignmentValue));
	}

	/**
	 * @deprecated Replaced by {@link #getVerticalImageAlign()}.
	 */
	public net.sf.jasperreports.engine.type.VerticalAlignEnum getVerticalAlignmentValue()
	{
		return net.sf.jasperreports.engine.type.VerticalAlignEnum.getVerticalAlignEnum(getVerticalImageAlign());
	}
		
	/**
	 * @deprecated Replaced by {@link #getOwnVerticalImageAlign()}.
	 */
	public net.sf.jasperreports.engine.type.VerticalAlignEnum getOwnVerticalAlignmentValue()
	{
		return net.sf.jasperreports.engine.type.VerticalAlignEnum.getVerticalAlignEnum(getOwnVerticalImageAlign());
	}
		
	/**
	 * @deprecated Replaced by {@link #setVerticalImageAlign(VerticalImageAlignEnum)}.
	 */
	public void setVerticalAlignment(net.sf.jasperreports.engine.type.VerticalAlignEnum verticalAlignmentValue)
	{
		setVerticalImageAlign(net.sf.jasperreports.engine.type.VerticalAlignEnum.getVerticalImageAlignEnum(verticalAlignmentValue));
	}
		
	/**
	 *
	 */
	public HorizontalImageAlignEnum getHorizontalImageAlign()
	{
		return JRStyleResolver.getHorizontalImageAlign(this);
	}
		
	public HorizontalImageAlignEnum getOwnHorizontalImageAlign()
	{
		return providerStyle == null || providerStyle.getOwnHorizontalImageAlign() == null ? ((JRImage)this.parent).getOwnHorizontalImageAlign() : providerStyle.getOwnHorizontalImageAlign();
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
		return JRStyleResolver.getVerticalImageAlign(this);
	}
		
	public VerticalImageAlignEnum getOwnVerticalImageAlign()
	{
		return providerStyle == null || providerStyle.getOwnVerticalImageAlign() == null ? ((JRImage)this.parent).getOwnVerticalImageAlign() : providerStyle.getOwnVerticalImageAlign();
	}

	/**
	 *
	 */
	public void setVerticalImageAlign(VerticalImageAlignEnum verticalAlignment)
	{
		throw new UnsupportedOperationException();
	}
		
	/**
	 * @deprecated Replaced by {@link #getUsingCache()}.
	 */
	public boolean isUsingCache()
	{
		return ((JRImage)this.parent).isUsingCache();
	}
		
	/**
	 * @deprecated Replaced by {@link #getUsingCache()}.
	 */
	public Boolean isOwnUsingCache()
	{
		return ((JRImage)this.parent).isOwnUsingCache();
	}
		
	/**
	 *
	 */
	public Boolean getUsingCache()
	{
		return ((JRImage)this.parent).getUsingCache();
	}
		
	/**
	 *
	 */
	public void setUsingCache(boolean isUsingCache)
	{
	}
		
	/**
	 *
	 */
	public void setUsingCache(Boolean isUsingCache)
	{
	}
		
	/**
	 *
	 */
	public boolean isLazy()
	{
		return ((JRImage)this.parent).isLazy();
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
		return ((JRImage)this.parent).getOnErrorTypeValue();
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
	public EvaluationTimeEnum getEvaluationTimeValue()
	{
		return ((JRImage)this.parent).getEvaluationTimeValue();
	}
		
	/**
	 *
	 */
	public JRGroup getEvaluationGroup()
	{
		return this.evaluationGroup;
	}
		
	/**
	 *
	 */
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

	/**
	 *
	 */
	public HyperlinkTypeEnum getHyperlinkTypeValue()
	{
		return ((JRImage)parent).getHyperlinkTypeValue();
	}
		
	/**
	 *
	 */
	public byte getHyperlinkTarget()
	{
		return ((JRImage)this.parent).getHyperlinkTarget();
	}
		
	/**
	 *
	 */
	public String getLinkTarget()
	{
		return ((JRImage)this.parent).getLinkTarget();
	}
		
	/**
	 *
	 */
	public JRExpression getExpression()
	{
		return ((JRImage)this.parent).getExpression();
	}

	/**
	 *
	 */
	public JRExpression getAnchorNameExpression()
	{
		return ((JRImage)this.parent).getAnchorNameExpression();
	}

	/**
	 *
	 */
	public JRExpression getHyperlinkReferenceExpression()
	{
		return ((JRImage)this.parent).getHyperlinkReferenceExpression();
	}

	/**
	 *
	 */
	public JRExpression getHyperlinkWhenExpression()
	{
		return ((JRImage)this.parent).getHyperlinkWhenExpression();
	}

	/**
	 *
	 */
	public JRExpression getHyperlinkAnchorExpression()
	{
		return ((JRImage)this.parent).getHyperlinkAnchorExpression();
	}

	/**
	 *
	 */
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


	/**
	 *
	 */
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
			
			if (isUsingCache && filler.fillContext.hasLoadedImage(source))
			{
				newRenderer = filler.fillContext.getLoadedImage(source).getRenderable();
			}
			else
			{
				@SuppressWarnings("deprecation")
				net.sf.jasperreports.engine.JRRenderable deprecatedRenderable = 
					source instanceof net.sf.jasperreports.engine.JRRenderable 
					? (net.sf.jasperreports.engine.JRRenderable)source 
					: null;

				if (source instanceof Image)
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
				else if (source instanceof String)
				{
					String location = (String) source;
					newRenderer = 
						RenderableUtil.getInstance(filler.getJasperReportsContext()).getRenderable(
							location, 
							getOnErrorTypeValue(), 
							isLazy()//, 
//							filler.reportClassLoader,
//							filler.urlHandlerFactory,
//							filler.fileResolver
							);
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
						JRImageRenderer.getOnErrorRenderer(
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
					filler.fillContext.registerLoadedImage(source, img);
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
				else if (!isLazy() && (getScaleImageValue() == ScaleImageEnum.REAL_HEIGHT
						|| getScaleImageValue() == ScaleImageEnum.REAL_SIZE))
				{
					int padding = getLineBox().getBottomPadding().intValue() 
							+ getLineBox().getTopPadding().intValue();
					boolean reprinted = isOverflow 
						&& (this.isPrintWhenDetailOverflows() 
								&& (this.isAlreadyPrinted() 
										|| (!this.isAlreadyPrinted() && !this.isPrintRepeatedValues())));
					boolean imageOverflowAllowed = 
							filler.isBandOverFlowAllowed() && !reprinted && !hasOverflowed;
					boolean fits = fitImage(availableHeight - getRelativeY() - padding, imageOverflowAllowed, 
							getHorizontalImageAlign());
					if (fits)
					{
						if (imageHeight != null)
						{
							setStretchHeight(imageHeight.intValue() + padding);
						}
					}
					else
					{
						hasOverflowed = true;
						isToPrint = false;
						willOverflow = true;
						setStretchHeight(availableHeight - getRelativeY() - padding);
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

	protected void reset()
	{
		imageHeight = null;
		imageWidth = null;
		imageX = null;
		
		super.reset();
	}

	protected boolean fitImage(int availableHeight, boolean overflowAllowed,
			HorizontalImageAlignEnum hAlign) throws JRException
	{
		imageHeight = null;
		imageWidth = null;
		imageX = null;
		
		Dimension2D imageSize = null;
		try
		{
			imageSize = renderer == null ? null : renderer.getDimension(filler.getJasperReportsContext());
		}
		catch (Exception e)
		{
			//ignore exception here; will be raised again when rendering the image
		}
		
		if (imageSize == null)
		{
			return true;
		}
		
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

	/**
	 *
	 */
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
			printImage.setHyperlinkReference(null);
		}
		transferProperties(printImage);
	}


	/**
	 *
	 */
	public void collectExpressions(JRExpressionCollector collector)
	{
		collector.collect(this);
	}

	/**
	 *
	 */
	public void visit(JRVisitor visitor)
	{
		visitor.visitImage(this);
	}

	
	protected void resolveElement(JRPrintElement element, byte evaluation) throws JRException
	{
		evaluateImage(evaluation);

		JRPrintImage printImage = (JRPrintImage) element;

		if (getScaleImageValue() == ScaleImageEnum.REAL_SIZE)//to avoid get dimension and thus unnecessarily load the image
		{
			int padding = printImage.getLineBox().getBottomPadding().intValue() 
			+ printImage.getLineBox().getTopPadding().intValue();
			fitImage(getHeight() - padding, false, 
					printImage.getHorizontalImageAlign());
		}
		
		copy(printImage);
		filler.updateBookmark(element);
	}


	public int getBookmarkLevel()
	{
		return ((JRImage)this.parent).getBookmarkLevel();
	}


	public JRFillCloneable createClone(JRFillCloneFactory factory)
	{
		return new JRFillImage(this, factory);
	}
	
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


	public JRHyperlinkParameter[] getHyperlinkParameters()
	{
		return ((JRImage) parent).getHyperlinkParameters();
	}


	public String getLinkType()
	{
		return ((JRImage) parent).getLinkType();
	}


	public JRExpression getHyperlinkTooltipExpression()
	{
		return ((JRImage) parent).getHyperlinkTooltipExpression();
	}

}
