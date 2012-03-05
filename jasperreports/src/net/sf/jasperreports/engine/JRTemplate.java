/*
 * JasperReports - Free Java Reporting Library.
 * Copyright (C) 2001 - 2011 Jaspersoft Corporation. All rights reserved.
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


/**
 * A template that can be used by report.
 * <p/>
 * Templates contain report styles that can be used by reports once the template is included in the report.
 * This allows styles to be externalized and reused across several reports.
 * <p/>
 * A template can in its turn include other templates, see {@link #getIncludedTemplates() getIncludedTemplates()}.
 * 
 * @author Lucian Chirita (lucianc@users.sourceforge.net)
 * @version $Id$
 * @see JRReport#getTemplates()
 * @see JRParameter#REPORT_TEMPLATES
 * @see JRStyleContainer#getStyleNameReference()
 */
public interface JRTemplate extends JRDefaultStyleProvider
{

	/**
	 * Returns the templates included/referenced by this template.
	 * 
	 * @return the included templates
	 */
	JRTemplateReference[] getIncludedTemplates();
	
	/**
	 * Returns the styles defined in this template.
	 * 
	 * @return the template styles
	 */
	JRStyle[] getStyles();

}
