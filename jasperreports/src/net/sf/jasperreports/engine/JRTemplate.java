/*
 * ============================================================================
 * GNU Lesser General Public License
 * ============================================================================
 *
 * JasperReports - Free Java report-generating library.
 * Copyright (C) 2001-2006 JasperSoft Corporation http://www.jaspersoft.com
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
 * 303 Second Street, Suite 450 North
 * San Francisco, CA 94107
 * http://www.jaspersoft.com
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
