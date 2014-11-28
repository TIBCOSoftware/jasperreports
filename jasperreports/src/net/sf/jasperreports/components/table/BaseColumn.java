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
package net.sf.jasperreports.components.table;

import java.util.List;

import net.sf.jasperreports.engine.JRCloneable;
import net.sf.jasperreports.engine.JRExpression;
import net.sf.jasperreports.engine.JRIdentifiable;
import net.sf.jasperreports.engine.JRPropertiesHolder;
import net.sf.jasperreports.engine.JRPropertyExpression;

/**
 * 
 * 
 * @author Lucian Chirita (lucianc@users.sourceforge.net)
 */
public interface BaseColumn extends JRCloneable, JRPropertiesHolder, JRIdentifiable
{

	JRExpression getPrintWhenExpression();
	
	Cell getTableHeader();
	
	Cell getTableFooter();
	
	List<GroupCell> getGroupHeaders();
	
	Cell getGroupHeader(String groupName);
	
	List<GroupCell> getGroupFooters();
	
	Cell getGroupFooter(String groupName);
	
	Cell getColumnHeader();
	
	Cell getColumnFooter();
	
	Integer getWidth();

	<R> R visitColumn(ColumnVisitor<R> visitor);

	JRPropertyExpression[] getPropertyExpressions();

}
