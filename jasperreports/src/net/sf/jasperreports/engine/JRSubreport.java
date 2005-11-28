/*
 * ============================================================================
 * GNU Lesser General Public License
 * ============================================================================
 *
 * JasperReports - Free Java report-generating library.
 * Copyright (C) 2001-2005 JasperSoft Corporation http://www.jaspersoft.com
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
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id$
 */
public interface JRSubreport extends JRElement
{


	/**
	 * Indicates if the engine is loading the current subreport from cache.
	 * Implementations of this method rely on default values that depend on the type of the subreport expression
	 * if a value was not explicitly set of this flag.
	 * @return true if the subreport should be loaded from cache, false otherwise
	 */
	public boolean isUsingCache();

	/**
	 * Specifies if the engine should be loading the current subreport from cache. If set to true, the reporting engine
	 * will try to recognize previously loaded subreports using their specified source. For example, it will recognize
	 * a subreport if the subreport source is a file name that it has already loaded, or if it is the same URL.
	 * <p>
	 * For subreports that have expressions returning <tt>java.lang.String</tt> objects as the subreport source, 
	 * representing file names, URLs or classpath resources, the default value for this flag is true.
	 * 
	 * @deprecated use {@link #setUsingCache(Boolean) setUsingCache(Boolean)} instead.
	 */
	public void setUsingCache(boolean isUsingCache);

	/**
	 *
	 */
	public JRExpression getParametersMapExpression();

	/**
	 *
	 */
	public JRSubreportParameter[] getParameters();

	/**
	 *
	 */
	public JRExpression getConnectionExpression();

	/**
	 *
	 */
	public JRExpression getDataSourceExpression();

	/**
	 *
	 */
	public JRExpression getExpression();
	
	/**
	 * Returns the list of subreport copied values.
	 *
	 * @return the list of subreport copied values.
	 */
	public JRSubreportReturnValue[] getReturnValues();

	

	/**
	 * Indicates if the engine is loading the current subreport from cache.
	 * Implementations of this method return the actual value for the internal flag that was explicitly 
	 * set on this subreport.
	 * @return Boolean.TRUE if the subreport should be loaded from cache, Boolean.FALSE otherwise 
	 * or null in case the flag was never explicitly set on this subreport element
	 */
	public Boolean isOwnUsingCache();
	
	
	/**
	 * Specifies if the engine should be loading the current subreport from cache. If set to Boolean.TRUE, the reporting engine
	 * will try to recognize previously loaded subreports using their specified source. For example, it will recognize
	 * an subreport if the subreport source is a file name that it has already loaded, or if it is the same URL.
	 * <p>
	 * If set to null, the engine will rely on some default value which depends on the type of the subreport expression.
	 * The cache is turned on by default only for subreports that have <tt>java.lang.String</tt> objects in their expressions.
	 */
	public void setUsingCache(Boolean isUsingCache);
}
