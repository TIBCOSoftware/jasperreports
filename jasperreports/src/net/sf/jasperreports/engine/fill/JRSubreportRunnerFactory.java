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
package net.sf.jasperreports.engine.fill;

import net.sf.jasperreports.engine.util.JRProperties;


/**
 * Factory of {@link net.sf.jasperreports.engine.fill.JRSubreportRunner JRSubreportRunner} instances.
 * 
 * @author Lucian Chirita (lucianc@users.sourceforge.net)
 * @version $Id$
 * @see net.sf.jasperreports.engine.util.JRProperties#SUBREPORT_RUNNER_FACTORY
 */
public interface JRSubreportRunnerFactory
{
	/**
	 * Property specifying the {@link net.sf.jasperreports.engine.fill.JRSubreportRunnerFactory JRSubreportRunnerFactory}
	 * implementation to use for creating subreport runners.
	 */
	public static final String SUBREPORT_RUNNER_FACTORY = JRProperties.PROPERTY_PREFIX + "subreport.runner.factory";
	
	/**
	 * Creates a new {@link net.sf.jasperreports.engine.fill.JRSubreportRunner JRSubreportRunner} instance.
	 * 
	 * @param fillSubreport the subreport element of the master report
	 * @param subreportFiller the subreport filler created to fill the subreport
	 * @return a new {@link net.sf.jasperreports.engine.fill.JRSubreportRunner JRSubreportRunner} instance
	 */
	JRSubreportRunner createSubreportRunner(JRFillSubreport fillSubreport, JRBaseFiller subreportFiller);
}
