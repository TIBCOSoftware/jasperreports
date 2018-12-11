/*
 * JasperReports - Free Java Reporting Library.
 * Copyright (C) 2001 - 2018 TIBCO Software Inc. All rights reserved.
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
package net.sf.jasperreports.engine.xml;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.map.ReferenceMap;

import net.sf.jasperreports.engine.JasperReportsContext;

/**
 * @author Lucian Chirita (lucianc@users.sourceforge.net)
 */
public class TemplateSaxParserFactory extends BaseSaxParserFactory
{
	private final static ThreadLocal<ReferenceMap> GRAMMAR_POOL_CACHE = new ThreadLocal<ReferenceMap>();

	public TemplateSaxParserFactory(JasperReportsContext jasperReportsContext) 
	{
		super(jasperReportsContext);
	}

	@Override
	protected boolean isValidating()
	{
		return true;
	}

	@Override
	protected List<String> getSchemaLocations()
	{
		List<String> schemas = new ArrayList<String>(2);
		schemas.add(getResourceURI(JRXmlConstants.JASPERTEMPLATE_XSD_RESOURCE));
		schemas.add(getResourceURI(JRXmlConstants.JASPERTEMPLATE_XSD_DTD_COMPAT_RESOURCE));
		return schemas;
	}

	@Override
	protected ThreadLocal<ReferenceMap> getGrammarPoolCache()
	{
		return GRAMMAR_POOL_CACHE;
	}

}
