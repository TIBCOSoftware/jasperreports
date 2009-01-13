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
package net.sf.jasperreports.engine.component;

import net.sf.jasperreports.engine.JRExpressionCollector;
import net.sf.jasperreports.engine.base.JRBaseObjectFactory;
import net.sf.jasperreports.engine.design.JRVerifier;

/**
 * Responsible with handling a componet during report compile.
 * 
 * @author Lucian Chirita (lucianc@users.sourceforge.net)
 * @version $Id: JRCrosstab.java 1741 2007-06-08 10:53:33Z lucianc $
 */
public interface ComponentCompiler
{

	/**
	 * Collects report expressions from a component.
	 * 
	 * @param component the component
	 * @param collector the expression collector
	 */
	void collectExpressions(Component component, JRExpressionCollector collector);

	/**
	 * Logically verifies a component.
	 * 
	 * @param component the component
	 * @param verifier the verifier object, which can be used to raise validation
	 * errors
	 */
	void verify(Component component, JRVerifier verifier);

	/**
	 * Provides a "compiled" component instance that will be included in the
	 * compiled report.
	 * 
	 * @param component the component from the design report
	 * @param baseFactory the factory of base/compiled report elements
	 * @return a component instance that is to be included in the compiled
	 * report
	 */
	Component toCompiledComponent(Component component, JRBaseObjectFactory baseFactory);

}
