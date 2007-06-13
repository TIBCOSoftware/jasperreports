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
package net.sf.jasperreports.engine.design;

import net.sf.jasperreports.engine.JRExpression;


/**
 * A source code unit to be compiled by a report compiler.
 * <p/>
 * In addition to the source code itself, the unit offers the possibility of determining whether
 * a line of code corresponds to a report expression.
 *  
 * 
 * @author Lucian Chirita (lucianc@users.sourceforge.net)
 * @version $Id$
 */
public interface JRCompilationSourceCode
{
	
	/**
	 * Returns the source code.
	 * 
	 * @return the source code
	 */
	String getCode();
	
	/**
	 * Determines whether a line of code corresponds to a report expression.
	 * 
	 * @param line the line number
	 * @return the expression to which the line of code corresponds if any and null otherwise
	 */
	JRExpression getExpressionAtLine(int line);
	
}
