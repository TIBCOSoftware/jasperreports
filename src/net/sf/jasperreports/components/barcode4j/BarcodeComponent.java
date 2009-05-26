/*
 * ============================================================================
 * GNU Lesser General Public License
 * ============================================================================
 *
 * JasperReports - Free Java report-generating library.
 * Copyright (C) 2001-2009 JasperSoft Corporation http://www.jaspersoft.com
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
 * 539 Bryant Street, Suite 100
 * San Francisco, CA 94107
 * http://www.jaspersoft.com
 */
package net.sf.jasperreports.components.barcode4j;

import java.io.Serializable;

import org.krysalis.barcode4j.HumanReadablePlacement;

import net.sf.jasperreports.engine.JRConstants;
import net.sf.jasperreports.engine.JRExpression;
import net.sf.jasperreports.engine.JRRuntimeException;
import net.sf.jasperreports.engine.component.Component;
import net.sf.jasperreports.engine.util.JRProperties;

/**
 * 
 * @author Lucian Chirita (lucianc@users.sourceforge.net)
 * @version $Id$
 */
public abstract class BarcodeComponent implements Component, Serializable, Cloneable
{

	public static final String PROPERTY_PREFIX = 
		JRProperties.PROPERTY_PREFIX + "components.barcode4j.";
	
	private static final long serialVersionUID = JRConstants.SERIAL_VERSION_UID;
	
	private byte evaluationTime;
	private String evaluationGroup;
	
	private int orientation;
	private JRExpression codeExpression;
	private JRExpression patternExpression;
	private Double moduleWidth;
	private String textPosition;

	public byte getEvaluationTime()
	{
		return evaluationTime;
	}

	public void setEvaluationTime(byte evaluationTime)
	{
		this.evaluationTime = evaluationTime;
	}

	public String getEvaluationGroup()
	{
		return evaluationGroup;
	}

	public void setEvaluationGroup(String evaluationGroup)
	{
		this.evaluationGroup = evaluationGroup;
	}

	public int getOrientation()
	{
		return orientation;
	}

	public void setOrientation(int orientation)
	{
		this.orientation = orientation;
	}

	public JRExpression getCodeExpression()
	{
		return codeExpression;
	}

	public void setCodeExpression(JRExpression codeExpression)
	{
		this.codeExpression = codeExpression;
	}

	public JRExpression getPatternExpression()
	{
		return patternExpression;
	}

	public void setPatternExpression(JRExpression patternExpression)
	{
		this.patternExpression = patternExpression;
	}

	public Double getModuleWidth()
	{
		return moduleWidth;
	}

	public void setModuleWidth(Double moduleWidth)
	{
		this.moduleWidth = moduleWidth;
	}

	public String getTextPosition()
	{
		return textPosition;
	}

	public void setTextPosition(String textPosition)
	{
		this.textPosition = textPosition;
	}

	public void setTextPosition(HumanReadablePlacement textPosition)
	{
		setTextPosition(textPosition == null ? null : textPosition.getName());
	}

	public BarcodeComponent cloneBarcode()
	{
		try
		{
			return (BarcodeComponent) super.clone();
		}
		catch (CloneNotSupportedException e)
		{
			// never
			throw new JRRuntimeException(e);
		}
	}
	
	public abstract void receive(BarcodeVisitor visitor);
	
}
