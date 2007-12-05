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
package net.sf.jasperreports.engine.base;

import java.io.Serializable;

import net.sf.jasperreports.engine.JRConstants;
import net.sf.jasperreports.engine.JRQueryChunk;
import net.sf.jasperreports.engine.JRRuntimeException;
import net.sf.jasperreports.engine.util.JRQueryParser;


/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id$
 */
public class JRBaseQueryChunk implements JRQueryChunk, Serializable
{


	/**
	 *
	 */
	private static final long serialVersionUID = JRConstants.SERIAL_VERSION_UID;

	/**
	 *
	 */
	protected byte type = TYPE_TEXT;
	protected String text = null;
	protected String[] tokens;


	/**
	 *
	 */
	protected JRBaseQueryChunk()
	{
	}
	
	
	/**
	 *
	 */
	protected JRBaseQueryChunk(JRQueryChunk queryChunk, JRBaseObjectFactory factory)
	{
		factory.put(queryChunk, this);
		
		type = queryChunk.getType();
		text = queryChunk.getText();
		
		String[] chunkTokens = queryChunk.getTokens();
		if (chunkTokens == null)
		{
			tokens = null;
		}
		else
		{
			tokens = new String[chunkTokens.length];
			System.arraycopy(chunkTokens, 0, tokens, 0, chunkTokens.length);
		}
	}
		

	/**
	 *
	 */
	public byte getType()
	{
		return this.type;
	}
		
	/**
	 *
	 */
	public String getText()
	{
		if (type == TYPE_CLAUSE_TOKENS)
		{
			return JRQueryParser.instance().asClauseText(getTokens());
		}
		
		return this.text;
	}


	public String[] getTokens()
	{
		return tokens;
	}
		

	/**
	 * 
	 */
	public Object clone() 
	{
		try
		{
			return super.clone();
		}
		catch (CloneNotSupportedException e)
		{
			throw new JRRuntimeException(e);
		}
	}


}
