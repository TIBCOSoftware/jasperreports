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
import net.sf.jasperreports.engine.JRQuery;
import net.sf.jasperreports.engine.JRQueryChunk;
import net.sf.jasperreports.engine.JRRuntimeException;
import net.sf.jasperreports.engine.query.JRJdbcQueryExecuterFactory;
import net.sf.jasperreports.engine.util.JRQueryParser;


/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id$
 */
public class JRBaseQuery implements JRQuery, Serializable
{
	
	
	/**
	 *
	 */
	private static final long serialVersionUID = JRConstants.SERIAL_VERSION_UID;

	/**
	 *
	 */
	private JRQueryChunk[] chunks = null;
	
	protected String language = JRJdbcQueryExecuterFactory.QUERY_LANGUAGE_SQL;


	/**
	 *
	 */
	protected JRBaseQuery()
	{
	}


	/**
	 *
	 */
	protected JRBaseQuery(JRQuery query, JRBaseObjectFactory factory)
	{
		factory.put(query, this);
		
		/*   */
		JRQueryChunk[] jrChunks = query.getChunks();
		if (jrChunks != null && jrChunks.length > 0)
		{
			chunks = new JRQueryChunk[jrChunks.length];
			for(int i = 0; i < chunks.length; i++)
			{
				chunks[i] = factory.getQueryChunk(jrChunks[i]);
			}
		}
		
		language = query.getLanguage();
	}
		

	/**
	 *
	 */
	public JRQueryChunk[] getChunks()
	{
		return this.chunks;
	}


	/**
	 *
	 */
	public String getText()
	{
		return JRQueryParser.instance().asText(getChunks());
	}


	public String getLanguage()
	{
		return language;
	}
	

	/**
	 * 
	 */
	public Object clone() 
	{
		JRBaseQuery clone = null;

		try
		{
			clone = (JRBaseQuery)super.clone();
		}
		catch (CloneNotSupportedException e)
		{
			throw new JRRuntimeException(e);
		}
		
		if (chunks != null)
		{
			clone.chunks = new JRQueryChunk[chunks.length];
			for(int i = 0; i < chunks.length; i++)
			{
				clone.chunks[i] = (JRQueryChunk)chunks[i].clone();
			}
		}

		return clone;
	}
	
	
}
