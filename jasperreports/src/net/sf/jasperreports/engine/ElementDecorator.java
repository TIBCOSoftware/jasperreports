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
package net.sf.jasperreports.engine;

import java.awt.Color;
import java.util.UUID;

import net.sf.jasperreports.engine.type.ModeEnum;
import net.sf.jasperreports.engine.type.PositionTypeEnum;
import net.sf.jasperreports.engine.type.StretchTypeEnum;

/**
 * 
 * 
 * @author Lucian Chirita (lucianc@users.sourceforge.net)
 */
public abstract class ElementDecorator implements JRElement
{
	
	private final JRElement element;

	public ElementDecorator(JRElement decorated)
	{
		this.element = decorated;
	}
	
	public Object clone()
	{
		try
		{
			return super.clone();
		} 
		catch (CloneNotSupportedException e)
		{
			// never
			throw new JRRuntimeException(e);
		}
	}
	
	public void collectExpressions(JRExpressionCollector collector)
	{
		element.collectExpressions(collector);
	}

	public JRElementGroup getElementGroup()
	{
		return element.getElementGroup();
	}

	public UUID getUUID()
	{
		return element.getUUID();
	}

	public String getKey()
	{
		return element.getKey();
	}

	public PositionTypeEnum getPositionTypeValue()
	{
		return element.getPositionTypeValue();
	}

	public JRExpression getPrintWhenExpression()
	{
		return element.getPrintWhenExpression();
	}

	public JRGroup getPrintWhenGroupChanges()
	{
		return element.getPrintWhenGroupChanges();
	}

	public JRPropertyExpression[] getPropertyExpressions()
	{
		return element.getPropertyExpressions();
	}

	public StretchTypeEnum getStretchTypeValue()
	{
		return element.getStretchTypeValue();
	}

	public int getX()
	{
		return element.getX();
	}

	public int getY()
	{
		return element.getY();
	}

	public boolean isPrintInFirstWholeBand()
	{
		return element.isPrintInFirstWholeBand();
	}

	public boolean isPrintRepeatedValues()
	{
		return element.isPrintRepeatedValues();
	}

	public boolean isPrintWhenDetailOverflows()
	{
		return element.isPrintWhenDetailOverflows();
	}

	public boolean isRemoveLineWhenBlank()
	{
		return element.isRemoveLineWhenBlank();
	}

	@Deprecated
	public void setPositionType(byte positionType)
	{
		throw new UnsupportedOperationException();
	}

	public void setPositionType(PositionTypeEnum positionType)
	{
		throw new UnsupportedOperationException();
	}

	public void setPrintInFirstWholeBand(boolean isPrintInFirstWholeBand)
	{
		throw new UnsupportedOperationException();
	}

	public void setPrintRepeatedValues(boolean isPrintRepeatedValues)
	{
		throw new UnsupportedOperationException();
	}

	public void setPrintWhenDetailOverflows(boolean isPrintWhenDetailOverflows)
	{
		throw new UnsupportedOperationException();
	}

	public void setRemoveLineWhenBlank(boolean isRemoveLineWhenBlank)
	{
		throw new UnsupportedOperationException();
	}

	@Deprecated
	public void setStretchType(byte stretchType)
	{
		throw new UnsupportedOperationException();
	}

	public void setStretchType(StretchTypeEnum stretchTypeEnum)
	{
		throw new UnsupportedOperationException();
	}

	public void setWidth(int width)
	{
		throw new UnsupportedOperationException();
	}

	public void setX(int x)
	{
		throw new UnsupportedOperationException();
	}

	public Object clone(JRElementGroup parentGroup)
	{
		throw new UnsupportedOperationException();
	}

	@Override
	public JRElement clone(JRElementGroup parentGroup, int y)
	{
		throw new UnsupportedOperationException();
	}

	public void visit(JRVisitor visitor)
	{
		element.visit(visitor);
	}

	public Color getBackcolor()
	{
		return element.getBackcolor();
	}

	public Color getForecolor()
	{
		return element.getForecolor();
	}

	public int getHeight()
	{
		return element.getHeight();
	}

	public ModeEnum getModeValue()
	{
		return element.getModeValue();
	}

	public Color getOwnBackcolor()
	{
		return element.getOwnBackcolor();
	}

	public Color getOwnForecolor()
	{
		return element.getOwnForecolor();
	}

	public ModeEnum getOwnModeValue()
	{
		return element.getOwnModeValue();
	}

	public int getWidth()
	{
		return element.getWidth();
	}

	public void setBackcolor(Color backcolor)
	{
		throw new UnsupportedOperationException();
	}

	public void setForecolor(Color forecolor)
	{
		throw new UnsupportedOperationException();
	}

	@Deprecated
	public void setMode(byte mode)
	{
		throw new UnsupportedOperationException();
	}

	@Deprecated
	public void setMode(Byte mode)
	{
		throw new UnsupportedOperationException();
	}

	public void setMode(ModeEnum mode)
	{
		throw new UnsupportedOperationException();
	}

	public JRDefaultStyleProvider getDefaultStyleProvider()
	{
		return element.getDefaultStyleProvider();
	}

	public JRStyle getStyle()
	{
		return element.getStyle();
	}

	public String getStyleNameReference()
	{
		return element.getStyleNameReference();
	}

	public JRPropertiesHolder getParentProperties()
	{
		return element.getParentProperties();
	}

	public JRPropertiesMap getPropertiesMap()
	{
		return element.getPropertiesMap();
	}

	public boolean hasProperties()
	{
		return element.hasProperties();
	}

}
