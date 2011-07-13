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
import net.sf.jasperreports.components.sort.SortElementHandlerBundle;
import net.sf.jasperreports.engine.JRChild;
import net.sf.jasperreports.engine.JRConstants;
import net.sf.jasperreports.engine.JRGenericElementType;
import net.sf.jasperreports.engine.JRTextField;
import net.sf.jasperreports.engine.design.JRDesignExpression;
import net.sf.jasperreports.engine.design.JRDesignGenericElement;
import net.sf.jasperreports.engine.design.JRDesignGenericElementParameter;
import net.sf.jasperreports.engine.type.ModeEnum;
import net.sf.jasperreports.engine.util.JRCloneUtils;
import net.sf.jasperreports.engine.xml.JRXmlConstants;

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
				String expressionString = tf.getExpression() != null ? tf.getExpression().getText() : null;
				if (expressionString != null && expressionString.indexOf("{") != -1) {
					String columnName = expressionString.substring(expressionString.indexOf("{") + 1, expressionString.length()-1);
					addGenericElementToHeader(columnName);
					break;
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

	public void addGenericElementToHeader(String columnName) {
		Cell header = getColumnHeader();
		
		JRDesignGenericElement genericElement = new JRDesignGenericElement(header.getDefaultStyleProvider());
		genericElement.setPositionType(net.sf.jasperreports.engine.type.PositionTypeEnum.FIX_RELATIVE_TO_TOP);
		genericElement.setX(0);
		genericElement.setY(0);
		genericElement.setHeight(header.getHeight());
		genericElement.setMode(ModeEnum.TRANSPARENT);
		
		JRGenericElementType genericType = new JRGenericElementType(JRXmlConstants.JASPERREPORTS_NAMESPACE, SortElementHandlerBundle.NAME);//FIXMEJIVE make sort element
		genericElement.setGenericType(genericType);
		
		JRDesignGenericElementParameter paramColumnName = new JRDesignGenericElementParameter();
		paramColumnName.setName(SortElement.PARAMETER_SORT_COLUMN_NAME);
		JRDesignExpression paramColumnNameValueExpression = new JRDesignExpression();
		paramColumnNameValueExpression.setText("\"" + columnName + "\"");
		paramColumnName.setValueExpression(paramColumnNameValueExpression);
		genericElement.addParameter(paramColumnName);
		
		JRDesignGenericElementParameter paramColumnType = new JRDesignGenericElementParameter();
		paramColumnType.setName(SortElement.PARAMETER_SORT_COLUMN_TYPE);
		JRDesignExpression paramColumnTypeValueExpression = new JRDesignExpression();
		paramColumnTypeValueExpression.setText("\"Field\"");
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

		JRDesignGenericElementParameter paramForControl = new JRDesignGenericElementParameter();
		paramForControl.setName(SortElement.PARAMETER_DYNAMIC_TABLE_BINDING);
		JRDesignExpression paramForControlValueExpression = new JRDesignExpression();
		paramForControlValueExpression.setText("\"dinamicallyAdded\"");
		paramForControl.setValueExpression(paramForControlValueExpression);
		genericElement.addParameter(paramForControl);
		
		header.getChildren().add(genericElement);
	}
}
