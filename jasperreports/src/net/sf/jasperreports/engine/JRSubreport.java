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
	 * @deprecated Replaced by {@link #getUsingCache()}.
	 */
	public boolean isUsingCache();

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
	 * @deprecated Replaced by {@link #getUsingCache()}.
	 */
	public Boolean isOwnUsingCache();
	
	
	/**
	 * Indicates if the engine is loading the current subreport from cache.
	 * Implementations of this method return the actual value for the internal flag that was explicitly 
	 * set on this subreport.
	 * @return Boolean.TRUE if the subreport should be loaded from cache, Boolean.FALSE otherwise 
	 * or null in case the flag was never explicitly set on this subreport element
	 */
	public Boolean getUsingCache();
	
	
	/**
	 * Specifies if the engine should be loading the current subreport from cache. If set to Boolean.TRUE, the reporting engine
	 * will try to recognize previously loaded subreports using their specified source. For example, it will recognize
	 * an subreport if the subreport source is a file name that it has already loaded, or if it is the same URL.
	 * <p>
	 * If set to null, the engine will rely on some default value which depends on the type of the subreport expression.
	 * The cache is turned on by default only for subreports that have <tt>java.lang.String</tt> objects in their expressions.
	 */
	public void setUsingCache(Boolean isUsingCache);
	
	/**
	 * Specifies whether the subreport element will consume the entire vertical
	 * space available on the report page.
	 * 
	 * @return whether the subreport element will consume the entire space down to 
	 * the bottom of the page
	 * @see #setRunToBottom(Boolean) 
	 */
	public Boolean isRunToBottom();

	/**
	 * Sets the flag that Specifies whether the subreport element will consume the
	 * entire vertical space available on the report page.
	 * 
	 * <p>
	 * This flag should be set to <code>true</code> if the subreport needs to always
	 * print its column and page footers at the bottom of the report page, even when
	 * the subreport data does not stretch to the bottom.
	 * 
	 * <p>
	 * Note that when {@link JRReport#isFloatColumnFooter() isFloatColumnFooter}
	 * is set for the subreport, the column footers will not be printed at the bottom
	 * of the page even if this flag is set.
	 * 
	 * @param runToBottom whether the subreport element will consume the entire
	 * space down to the bottom of the page
	 */
	public void setRunToBottom(Boolean runToBottom);
	
}
