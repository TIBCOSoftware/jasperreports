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
package net.sf.jasperreports.components.table;

import java.util.List;

import net.sf.jasperreports.components.sort.SortElement;
import net.sf.jasperreports.engine.JRChild;
import net.sf.jasperreports.engine.JRConstants;
import net.sf.jasperreports.engine.JRExpression;
import net.sf.jasperreports.engine.JRExpressionChunk;
import net.sf.jasperreports.engine.JRTextField;
import net.sf.jasperreports.engine.design.JRDesignExpression;
import net.sf.jasperreports.engine.design.JRDesignGenericElement;
import net.sf.jasperreports.engine.design.JRDesignGenericElementParameter;
import net.sf.jasperreports.engine.type.ModeEnum;
import net.sf.jasperreports.engine.util.JRCloneUtils;

/**
 * 
 * 
 * @author Lucian Chirita (lucianc@users.sourceforge.net)
 * @version $Id$
 */
public class StandardColumn extends StandardBaseColumn implements Column
{

	private static final long serialVersionUID = JRConstants.SERIAL_VERSION_UID;
	
	public static final String PROPERTY_DETAIL = "detail";

	private Cell detail;
	
	public StandardColumn()
	{
	}
	
	public StandardColumn(Column column, ColumnFactory factory)
	{
		super(column, factory);
		
		Cell columnCell = column.getDetailCell();
		if (columnCell != null)
		{
			this.detail = new CompiledCell(columnCell, factory.getBaseObjectFactory());
		}
	}

	public Cell getDetailCell()
	{
		return detail;
	}

	public void setDetailCell(Cell detail)
	{
		Object old = this.detail;
		this.detail = detail;

		//FIXMEJIVE find a better way to add the generic element for sorting
		List<JRChild> children = detail.getChildren();
		for (JRChild child: children) {
			if (child instanceof JRTextField) {
				JRTextField tf = (JRTextField) child;
				
				JRExpression expression = tf.getExpression();
				if (expression != null)
				{
					JRExpressionChunk[] chunks = expression.getChunks();
					if (
						chunks != null 
						&& chunks.length == 1 
						&& (chunks[0].getType() == JRExpressionChunk.TYPE_FIELD 
							|| chunks[0].getType() == JRExpressionChunk.TYPE_VARIABLE)
						)
					{
						addGenericElementToHeader(chunks[0]);
						break;
					}
				}
			}
		}
		
		getEventSupport().firePropertyChange(PROPERTY_DETAIL, old, this.detail);
	}

	public <R> R visitColumn(ColumnVisitor<R> visitor)
	{
		return visitor.visitColumn(this);
	}

	@Override
	public Object clone()
	{
		StandardColumn clone = (StandardColumn) super.clone();
		clone.detail = JRCloneUtils.nullSafeClone(detail);
		return clone;
	}

	public void addGenericElementToHeader(JRExpressionChunk chunk) {
		Cell header = getColumnHeader();
		
		if (header != null)
		{
			JRDesignGenericElement genericElement = new JRDesignGenericElement(header.getDefaultStyleProvider());

			genericElement.setGenericType(SortElement.SORT_ELEMENT_TYPE);
			genericElement.getPropertiesMap().setProperty(SortElement.PROPERTY_DYNAMIC_TABLE_BINDING, "true");
			genericElement.setPositionType(net.sf.jasperreports.engine.type.PositionTypeEnum.FIX_RELATIVE_TO_TOP);
			genericElement.setX(0);
			genericElement.setY(0);
			genericElement.setHeight(header.getHeight());
			genericElement.setMode(ModeEnum.TRANSPARENT);
			
			JRDesignGenericElementParameter paramSortColumnName = new JRDesignGenericElementParameter();
			paramSortColumnName.setName(SortElement.PARAMETER_SORT_COLUMN_NAME);
			JRDesignExpression paramSortColumnValueExpression = new JRDesignExpression();
			paramSortColumnValueExpression.setText("\"" + chunk.getText() + "\"");
			paramSortColumnName.setValueExpression(paramSortColumnValueExpression);
			genericElement.addParameter(paramSortColumnName);
			
			JRDesignGenericElementParameter paramColumnType = new JRDesignGenericElementParameter();
			paramColumnType.setName(SortElement.PARAMETER_SORT_COLUMN_TYPE);
			JRDesignExpression paramColumnTypeValueExpression = new JRDesignExpression();
			paramColumnTypeValueExpression.setText("\"" + ((chunk.getType() == JRExpressionChunk.TYPE_FIELD) ? SortElement.SORT_ELEMENT_TYPE_FIELD : SortElement.SORT_ELEMENT_TYPE_VARIABLE) + "\"");
			paramColumnType.setValueExpression(paramColumnTypeValueExpression);
			genericElement.addParameter(paramColumnType);
			
			JRDesignGenericElementParameter paramHorizontalAlign = new JRDesignGenericElementParameter();
			paramHorizontalAlign.setName(SortElement.PARAMETER_SORT_HANDLER_HORIZONTAL_ALIGN);
			JRDesignExpression paramHorizontalAlignValueExpression = new JRDesignExpression();
			paramHorizontalAlignValueExpression.setText("\"Right\"");
			paramHorizontalAlign.setValueExpression(paramHorizontalAlignValueExpression);
			genericElement.addParameter(paramHorizontalAlign);

			JRDesignGenericElementParameter paramVerticalAlign = new JRDesignGenericElementParameter();
			paramVerticalAlign.setName(SortElement.PARAMETER_SORT_HANDLER_VERTICAL_ALIGN);
			JRDesignExpression paramVerticalAlignValueExpression = new JRDesignExpression();
			paramVerticalAlignValueExpression.setText("\"Middle\"");
			paramVerticalAlign.setValueExpression(paramVerticalAlignValueExpression);
			genericElement.addParameter(paramVerticalAlign);
			
			header.getChildren().add(genericElement);
		}
	}
}
