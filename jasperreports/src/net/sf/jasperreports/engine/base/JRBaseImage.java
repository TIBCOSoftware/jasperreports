/*
 * ============================================================================
 *                   The JasperReports License, Version 1.0
 * ============================================================================
 * 
 * Copyright (C) 2001-2004 Teodor Danciu (teodord@users.sourceforge.net). All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without modification,
 * are permitted provided that the following conditions are met:
 * 
 * 1. Redistributions of source code must retain the above copyright notice,
 *    this list of conditions and the following disclaimer.
 * 
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 * 
 * 3. The end-user documentation included with the redistribution, if any, must
 *    include the following acknowledgment: "This product includes software
 *    developed by Teodor Danciu (http://jasperreports.sourceforge.net)."
 *    Alternately, this acknowledgment may appear in the software itself, if
 *    and wherever such third-party acknowledgments normally appear.
 * 
 * 4. The name "JasperReports" must not be used to endorse or promote products 
 *    derived from this software without prior written permission. For written 
 *    permission, please contact teodord@users.sourceforge.net.
 * 
 * 5. Products derived from this software may not be called "JasperReports", nor 
 *    may "JasperReports" appear in their name, without prior written permission
 *    of Teodor Danciu.
 * 
 * THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED WARRANTIES,
 * INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
 * FITNESS  FOR A PARTICULAR  PURPOSE ARE  DISCLAIMED.  IN NO  EVENT SHALL  THE
 * APACHE SOFTWARE  FOUNDATION  OR ITS CONTRIBUTORS  BE LIABLE FOR  ANY DIRECT,
 * INDIRECT, INCIDENTAL, SPECIAL,  EXEMPLARY, OR CONSEQUENTIAL  DAMAGES (INCLU-
 * DING, BUT NOT LIMITED TO, PROCUREMENT  OF SUBSTITUTE GOODS OR SERVICES; LOSS
 * OF USE, DATA, OR  PROFITS; OR BUSINESS  INTERRUPTION)  HOWEVER CAUSED AND ON
 * ANY  THEORY OF LIABILITY,  WHETHER  IN CONTRACT,  STRICT LIABILITY,  OR TORT
 * (INCLUDING  NEGLIGENCE OR  OTHERWISE) ARISING IN  ANY WAY OUT OF THE  USE OF
 * THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

/*
 * ============================================================================
 *                   GNU Lesser General Public License
 * ============================================================================
 *
 * JasperReports - Free Java report-generating library.
 * Copyright (C) 2001-2004 Teodor Danciu teodord@users.sourceforge.net
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
package dori.jasper.engine.base;

import java.util.Map;

import dori.jasper.engine.JRExpression;
import dori.jasper.engine.JRGroup;
import dori.jasper.engine.JRHyperlink;
import dori.jasper.engine.JRImage;


/**
 *
 */
public class JRBaseImage extends JRBaseGraphicElement implements JRImage
{


	/**
	 *
	 */
	private static final long serialVersionUID = 500;

	/**
	 *
	 */
	protected byte scaleImage = SCALE_IMAGE_RETAIN_SHAPE;
	protected byte horizontalAlignment = HORIZONTAL_ALIGN_LEFT;
	protected byte verticalAlignment = VERTICAL_ALIGN_TOP;
	protected boolean isUsingCache = true;
	protected byte evaluationTime = JRExpression.EVALUATION_TIME_NOW;
	protected byte hyperlinkType = JRHyperlink.HYPERLINK_TYPE_NONE;

	/**
	 *
	 */
	protected JRGroup evaluationGroup = null;
	protected JRExpression expression = null;
	protected JRExpression anchorNameExpression = null;
	protected JRExpression hyperlinkReferenceExpression = null;
	protected JRExpression hyperlinkAnchorExpression = null;
	protected JRExpression hyperlinkPageExpression = null;


	/**
	 *
	 */
	protected JRBaseImage()
	{
		super();
		
		this.mode = MODE_TRANSPARENT;
		this.pen = PEN_NONE;
	}
		

	/**
	 *
	 */
	protected JRBaseImage(JRImage image, Map baseObjectsMap)
	{
		super(image, baseObjectsMap);
		
		scaleImage = image.getScaleImage();
		horizontalAlignment = image.getHorizontalAlignment();
		verticalAlignment = image.getVerticalAlignment();
		isUsingCache = image.isUsingCache();
		evaluationTime = image.getEvaluationTime();
		hyperlinkType = image.getHyperlinkType();

		evaluationGroup = JRBaseObjectFactory.getGroup(image.getEvaluationGroup(), baseObjectsMap);
		expression = JRBaseObjectFactory.getExpression(image.getExpression(), baseObjectsMap);
		anchorNameExpression = JRBaseObjectFactory.getExpression(image.getAnchorNameExpression(), baseObjectsMap);
		hyperlinkReferenceExpression = JRBaseObjectFactory.getExpression(image.getHyperlinkReferenceExpression(), baseObjectsMap);
		hyperlinkAnchorExpression = JRBaseObjectFactory.getExpression(image.getHyperlinkAnchorExpression(), baseObjectsMap);
		hyperlinkPageExpression = JRBaseObjectFactory.getExpression(image.getHyperlinkPageExpression(), baseObjectsMap);
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
	public void setScaleImage(byte scaleImage)
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
	public void setHorizontalAlignment(byte horizontalAlignment)
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
	public void setVerticalAlignment(byte verticalAlignment)
	{
		this.verticalAlignment = verticalAlignment;
	}
		
	/**
	 *
	 */
	public boolean isUsingCache()
	{
		return this.isUsingCache;
	}

	/**
	 *
	 */
	public void setUsingCache(boolean isUsingCache)
	{
		this.isUsingCache = isUsingCache;
	}

	/**
	 *
	 */
	public byte getEvaluationTime()
	{
		return this.evaluationTime;
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
	public JRGroup getEvaluationGroup()
	{
		return this.evaluationGroup;
	}
		
	/**
	 *
	 */
	public JRExpression getExpression()
	{
		return this.expression;
	}

	/**
	 *
	 */
	public JRExpression getAnchorNameExpression()
	{
		return this.anchorNameExpression;
	}

	/**
	 *
	 */
	public JRExpression getHyperlinkReferenceExpression()
	{
		return this.hyperlinkReferenceExpression;
	}

	/**
	 *
	 */
	public JRExpression getHyperlinkAnchorExpression()
	{
		return this.hyperlinkAnchorExpression;
	}

	/**
	 *
	 */
	public JRExpression getHyperlinkPageExpression()
	{
		return this.hyperlinkPageExpression;
	}
	

}
