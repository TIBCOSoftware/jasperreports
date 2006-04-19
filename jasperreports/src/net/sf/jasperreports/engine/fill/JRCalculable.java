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

/**
 * Interface for objects that can be used by extended incrementers for calculations.
 * 
 * @author Lucian Chirita (lucianc@users.sourceforge.net)
 * @version $Id$
 * @see net.sf.jasperreports.engine.fill.JRExtendedIncrementer
 */
public interface JRCalculable
{
	/**
	 * Constant for the count helper variable.
	 */
	static final byte HELPER_COUNT = 0;
	
	/**
	 * Constant for the sum helper variable.
	 */
	static final byte HELPER_SUM = 1;
	
	/**
	 * Constant for the variance helper variable.
	 */
	static final byte HELPER_VARIANCE = 2;
	
	/**
	 * The number of defined helper variables.
	 */
	static final int HELPER_SIZE = 3;

	
	/**
	 * Returns <code>true</code> iff the calculable object was only initialized and not incremented.
	 * 
	 * @return <code>true</code> iff the calculable object was only initialized and not incremented
	 */
	boolean isInitialized();
	
	
	/**
	 * Sets the initialized flag for this calculable object.
	 * @param isInitialized the initialized flag
	 * @see #isInitialized()
	 */
	void setInitialized(boolean isInitialized);

	
	/**
	 * Returns the incremented value of the calculable object.
	 * 
	 * @return the incremented value
	 */
	Object getIncrementedValue();

	
	/**
	 * Returns the value of the calculable object.
	 * 
	 * @return the value
	 */
	Object getValue();

	
	/**
	 * Returns a helper variable.
	 * 
	 * @param helperType the desired helper variable type
	 * @return the helper variable
	 */
	JRCalculable getHelperVariable(byte helperType);
}
