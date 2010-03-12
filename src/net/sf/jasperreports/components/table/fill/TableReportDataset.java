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
package net.sf.jasperreports.components.table.fill;

import net.sf.jasperreports.engine.JRDataset;
import net.sf.jasperreports.engine.JRExpression;
import net.sf.jasperreports.engine.JRField;
import net.sf.jasperreports.engine.JRGroup;
import net.sf.jasperreports.engine.JRParameter;
import net.sf.jasperreports.engine.JRPropertiesHolder;
import net.sf.jasperreports.engine.JRPropertiesMap;
import net.sf.jasperreports.engine.JRQuery;
import net.sf.jasperreports.engine.JRScriptlet;
import net.sf.jasperreports.engine.JRSortField;
import net.sf.jasperreports.engine.JRVariable;
import net.sf.jasperreports.engine.type.WhenResourceMissingTypeEnum;

/**
 * 
 * 
 * @author Lucian Chirita (lucianc@users.sourceforge.net)
 * @version $Id$
 */
public class TableReportDataset implements JRDataset
{

	private final JRDataset tableSubdataset;
	private final String name;
	
	public TableReportDataset(JRDataset tableSubdataset, String name)
	{
		this.tableSubdataset = tableSubdataset;
		this.name = name;
	}

	public JRField[] getFields()
	{
		return tableSubdataset.getFields();
	}

	public JRExpression getFilterExpression()
	{
		return tableSubdataset.getFilterExpression();
	}

	public JRGroup[] getGroups()
	{
		return tableSubdataset.getGroups();
	}

	public String getName()
	{
		return name;
	}

	public JRParameter[] getParameters()
	{
		return tableSubdataset.getParameters();
	}

	public JRQuery getQuery()
	{
		return tableSubdataset.getQuery();
	}

	public String getResourceBundle()
	{
		// TODO default to report?
		return tableSubdataset.getResourceBundle();
	}

	public String getScriptletClass()
	{
		return tableSubdataset.getScriptletClass();
	}

	public JRScriptlet[] getScriptlets()
	{
		return tableSubdataset.getScriptlets();
	}

	public JRSortField[] getSortFields()
	{
		return tableSubdataset.getSortFields();
	}

	public JRVariable[] getVariables()
	{
		return tableSubdataset.getVariables();
	}

	@Deprecated
	public byte getWhenResourceMissingType()
	{
		return tableSubdataset.getWhenResourceMissingType();
	}

	public WhenResourceMissingTypeEnum getWhenResourceMissingTypeValue()
	{
		return tableSubdataset.getWhenResourceMissingTypeValue();
	}

	public boolean isMainDataset()
	{
		// used as main dataset
		return true;
	}

	@Deprecated
	public void setWhenResourceMissingType(byte whenResourceMissingType)
	{
		throw new UnsupportedOperationException();
	}

	public void setWhenResourceMissingType(
			WhenResourceMissingTypeEnum whenResourceMissingType)
	{
		throw new UnsupportedOperationException();
	}

	public JRPropertiesHolder getParentProperties()
	{
		return tableSubdataset.getParentProperties();
	}

	public JRPropertiesMap getPropertiesMap()
	{
		return tableSubdataset.getPropertiesMap();
	}

	public boolean hasProperties()
	{
		return tableSubdataset.hasProperties();
	}

	public Object clone()
	{
		throw new UnsupportedOperationException();
	}
	
}
