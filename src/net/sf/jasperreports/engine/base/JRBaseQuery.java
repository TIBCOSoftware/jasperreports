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
package net.sf.jasperreports.engine.base;

import java.io.Serializable;

import net.sf.jasperreports.engine.JRConstants;
import net.sf.jasperreports.engine.JRQuery;
import net.sf.jasperreports.engine.JRQueryChunk;


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
		String text = "";
		
		JRQueryChunk[] cks = this.getChunks();
		if (cks != null && cks.length > 0)
		{
			StringBuffer sbuffer = new StringBuffer();

			for(int i = 0; i < cks.length; i++)
			{
				switch(cks[i].getType())
				{
					case JRQueryChunk.TYPE_PARAMETER :
					{
						sbuffer.append("$P{");
						sbuffer.append( cks[i].getText() );
						sbuffer.append("}");
						break;
					}
					case JRQueryChunk.TYPE_PARAMETER_CLAUSE :
					{
						sbuffer.append("$P!{");
						sbuffer.append( cks[i].getText() );
						sbuffer.append("}");
						break;
					}
					case JRQueryChunk.TYPE_TEXT :
					default :
					{
						sbuffer.append( cks[i].getText() );
						break;
					}
				}
			}

			text = sbuffer.toString();
		}
		
		return text;
	}
	

}
