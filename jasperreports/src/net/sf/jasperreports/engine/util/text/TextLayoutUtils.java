/*
 * JasperReports - Free Java Reporting Library.
 * Copyright (C) 2001 - 2022 TIBCO Software Inc. All rights reserved.
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
package net.sf.jasperreports.engine.util.text;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * 
 * @author Lucian Chirita (lucianc@users.sourceforge.net)
 */
public class TextLayoutUtils
{

	private static final Log log = LogFactory.getLog(TextLayoutUtils.class);

	private static final TextLayoutAssessor ASSESSOR;

	static
	{
		TextLayoutAssessor assessor;
		try
		{
			assessor = new FontTextLayoutAssessor();
			//testing whether Java 9 Font method is available
			assessor.hasComplexLayout(new char[] {'a'});
		}
		catch (Error e)
		{
			if (log.isDebugEnabled())
			{
				log.debug("Java 9 complex text layout check not available: " + e.getMessage());
			}
			assessor = new LegacyTextLayoutAssessor();
		}
		
		ASSESSOR = assessor;
	}

	public static TextLayoutAssessor textLayoutAssessor()
	{
		return ASSESSOR;
	}

}
