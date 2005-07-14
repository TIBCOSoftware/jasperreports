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
 * 185, Berry Street, Suite 6200
 * San Francisco CA 94107
 * http://www.jaspersoft.com
 */
package net.sf.jasperreports.engine.design;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import net.sf.jasperreports.engine.JRQueryChunk;
import net.sf.jasperreports.engine.base.JRBaseQuery;


/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id$
 */
public class JRDesignQuery extends JRBaseQuery
{


	/**
	 *
	 */
	private static final long serialVersionUID = 10000;

	/**
	 *
	 */
	protected List chunks = new ArrayList();


	/**
	 *
	 */
	public JRQueryChunk[] getChunks()
	{
		JRQueryChunk[] chunkArray = null;
		
		if (chunks != null && chunks.size() > 0)
		{
			chunkArray = new JRQueryChunk[chunks.size()];
			chunks.toArray(chunkArray);
		}
		
		return chunkArray;
	}
		
	/**
	 *
	 */
	public void setChunks(List chunks)
	{
		this.chunks = chunks;
	}
		
	/**
	 *
	 */
	public void addChunk(JRDesignQueryChunk chunk)
	{
		this.chunks.add(chunk);
	}
		
	/**
	 *
	 */
	public void addTextChunk(String text)
	{
		JRDesignQueryChunk chunk = new JRDesignQueryChunk();
		chunk.setType(JRQueryChunk.TYPE_TEXT);
		chunk.setText(text);

		this.chunks.add(chunk);
	}
		
	/**
	 *
	 */
	public void addParameterChunk(String text)
	{
		JRDesignQueryChunk chunk = new JRDesignQueryChunk();
		chunk.setType(JRQueryChunk.TYPE_PARAMETER);
		chunk.setText(text);

		this.chunks.add(chunk);
	}
		
	/**
	 *
	 */
	public void addParameterClauseChunk(String text)
	{
		JRDesignQueryChunk chunk = new JRDesignQueryChunk();
		chunk.setType(JRQueryChunk.TYPE_PARAMETER_CLAUSE);
		chunk.setText(text);

		this.chunks.add(chunk);
	}
		

	/**
	 *
	 */
	public void setText(String text)
	{
		chunks = new ArrayList();
		
		if (text != null)
		{
			int end = 0;
			StringBuffer textChunk = new StringBuffer();
			String parameterChunk = null;
			String parameterClauseChunk = null;
			
			StringTokenizer tkzer = new StringTokenizer(text, "$", true);
			String token = null;
			boolean wasDelim = false;
			while (tkzer.hasMoreTokens())
			{
				token = tkzer.nextToken();
	
				if (token.equals("$"))
				{
					if (wasDelim)
					{
						textChunk.append("$");
					}
	
					wasDelim = true;
				}
				else
				{
					if ( token.startsWith("P{") && wasDelim )
					{
						end = token.indexOf('}');
						if (end > 0)
						{
							if (textChunk.length() > 0)
							{
								this.addTextChunk(textChunk.toString());					
							}
							parameterChunk = token.substring(2, end);
							this.addParameterChunk(parameterChunk);					
							textChunk = new StringBuffer(token.substring(end + 1));
						}
						else
						{
							if (wasDelim)
							{
								textChunk.append("$");
							}
							textChunk.append(token);
						}
					}
					else if ( token.startsWith("P!{") && wasDelim )
					{
						end = token.indexOf('}');
						if (end > 0)
						{
							if (textChunk.length() > 0)
							{
								this.addTextChunk(textChunk.toString());					
							}
							parameterClauseChunk = token.substring(3, end);
							this.addParameterClauseChunk(parameterClauseChunk);					
							textChunk = new StringBuffer(token.substring(end + 1));
						}
						else
						{
							if (wasDelim)
							{
								textChunk.append("$");
							}
							textChunk.append(token);
						}
					}
					else
					{
						if (wasDelim)
						{
							textChunk.append("$");
						}
						textChunk.append(token);
					}
	
					wasDelim = false;
				}
			}
			if (wasDelim)
			{
				textChunk.append("$");
			}
			if (textChunk.length() > 0)
			{
				this.addTextChunk(textChunk.toString());					
			}
		}
	}
			

}
