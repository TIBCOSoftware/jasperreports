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

import net.sf.jasperreports.engine.query.JRClauseFunction;


/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id$
 */
public interface JRQueryChunk extends JRCloneable
{


	/**
	 *
	 */
	public static final byte TYPE_TEXT = 1;
	public static final byte TYPE_PARAMETER = 2;
	public static final byte TYPE_PARAMETER_CLAUSE = 3;
	
	/**
	 * A <code>$X{..}</code> query clause containing one or several comma-separated tokens.
	 * <p>
	 * The clause will be processed by the query executer.
	 * The default implementation treats the first token as a function ID and delegates the processing
	 * to a {@link JRClauseFunction function} registered for the ID. 
	 * </p>
	 * @see #getTokens()
	 */
	public static final byte TYPE_CLAUSE_TOKENS = 4;


	/**
	 *
	 */
	public byte getType();
		
	/**
	 *
	 */
	public String getText();
		

	/**
	 * Returns the chunk tokens for {@link #TYPE_CLAUSE_TOKENS clause} chunks.
	 * 
	 * @return the chunk tokens
	 */
	public String[] getTokens();
}
