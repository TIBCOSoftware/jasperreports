/*
 * ============================================================================
 *                   The JasperReports License, Version 1.0
 * ============================================================================
 * 
 * Copyright (C) 2001-2004 Teodor Danciu (teodord@users.sourceforge.net). All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without modification,
 * are permitted provided that the following conditions are met:
 * 
 * 1. Redistributions of source code must retain the above copyright notice,
 *    this list of conditions and the following disclaimer.
 * 
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 * 
 * 3. The end-user documentation included with the redistribution, if any, must
 *    include the following acknowledgment: "This product includes software
 *    developed by Teodor Danciu (http://jasperreports.sourceforge.net)."
 *    Alternately, this acknowledgment may appear in the software itself, if
 *    and wherever such third-party acknowledgments normally appear.
 * 
 * 4. The name "JasperReports" must not be used to endorse or promote products 
 *    derived from this software without prior written permission. For written 
 *    permission, please contact teodord@users.sourceforge.net.
 * 
 * 5. Products derived from this software may not be called "JasperReports", nor 
 *    may "JasperReports" appear in their name, without prior written permission
 *    of Teodor Danciu.
 * 
 * THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED WARRANTIES,
 * INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
 * FITNESS  FOR A PARTICULAR  PURPOSE ARE  DISCLAIMED.  IN NO  EVENT SHALL  THE
 * APACHE SOFTWARE  FOUNDATION  OR ITS CONTRIBUTORS  BE LIABLE FOR  ANY DIRECT,
 * INDIRECT, INCIDENTAL, SPECIAL,  EXEMPLARY, OR CONSEQUENTIAL  DAMAGES (INCLU-
 * DING, BUT NOT LIMITED TO, PROCUREMENT  OF SUBSTITUTE GOODS OR SERVICES; LOSS
 * OF USE, DATA, OR  PROFITS; OR BUSINESS  INTERRUPTION)  HOWEVER CAUSED AND ON
 * ANY  THEORY OF LIABILITY,  WHETHER  IN CONTRACT,  STRICT LIABILITY,  OR TORT
 * (INCLUDING  NEGLIGENCE OR  OTHERWISE) ARISING IN  ANY WAY OUT OF THE  USE OF
 * THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

/*
 * ============================================================================
 *                   GNU Lesser General Public License
 * ============================================================================
 *
 * JasperReports - Free Java report-generating library.
 * Copyright (C) 2001-2004 Teodor Danciu teodord@users.sourceforge.net
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
 * Teodor Danciu
 * 173, Calea Calarasilor, Bl. 42, Sc. 1, Ap. 18
 * Postal code 030615, Sector 3
 * Bucharest, ROMANIA
 * Email: teodord@users.sourceforge.net
 */

/*
 * Contributors:
 * Ryan Johnson - delscovich@users.sourceforge.net 
 */
package dori.jasper.engine.design;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import dori.jasper.engine.JRExpressionChunk;
import dori.jasper.engine.base.JRBaseExpression;


/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id$
 */
public class JRDesignExpression extends JRBaseExpression
{


	/**
	 *
	 */
	private static final long serialVersionUID = 502;

	/**
	 *
	 */
	private static int lastId = 0;

	/**
	 *
	 */
	protected List chunks = new ArrayList();


	/**
	 *
	 */
	public JRDesignExpression()
	{
		super();
		
		this.id = getNextId();
	}


	/**
	 *
	 */
	private static synchronized int getNextId()
	{ 
		return lastId++; 
	}


	/**
	 *
	 */
	public void setValueClass(Class clazz)
	{
		setValueClassName(clazz.getName());
	}

	/**
	 *
	 */
	public void setValueClassName(String className)
	{
		valueClassName = className;
		valueClass = null;
	}

	/**
	 *
	 */
	public void setName(String name)
	{
		this.name = name;
	}

	/**
	 *
	 */
	public void setId(int id)
	{
		this.id = id;
	}

	/**
	 *
	 */
	public JRExpressionChunk[] getChunks()
	{
		JRExpressionChunk[] chunkArray = null;
		
		if (chunks != null && chunks.size() > 0)
		{
			chunkArray = new JRExpressionChunk[chunks.size()];
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
	public void addChunk(JRDesignExpressionChunk chunk)
	{
		this.chunks.add(chunk);
	}
		
	/**
	 *
	 */
	public void addTextChunk(String text)
	{
		JRDesignExpressionChunk chunk = new JRDesignExpressionChunk();
		chunk.setType(JRDesignExpressionChunk.TYPE_TEXT);
		chunk.setText(text);
		
		this.chunks.add(chunk);
	}
		
	/**
	 *
	 */
	public void addParameterChunk(String text)
	{
		JRDesignExpressionChunk chunk = new JRDesignExpressionChunk();
		chunk.setType(JRDesignExpressionChunk.TYPE_PARAMETER);
		chunk.setText(text);
		
		this.chunks.add(chunk);
	}
		
	/**
	 *
	 */
	public void addFieldChunk(String text)
	{
		JRDesignExpressionChunk chunk = new JRDesignExpressionChunk();
		chunk.setType(JRDesignExpressionChunk.TYPE_FIELD);
		chunk.setText(text);
		
		this.chunks.add(chunk);
	}
		
	/**
	 *
	 */
	public void addVariableChunk(String text)
	{
		JRDesignExpressionChunk chunk = new JRDesignExpressionChunk();
		chunk.setType(JRDesignExpressionChunk.TYPE_VARIABLE);
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
			String fieldChunk = null;
			String variableChunk = null;
			
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
					else if ( token.startsWith("F{") && wasDelim )
					{
						end = token.indexOf('}');
						if (end > 0)
						{
							if (textChunk.length() > 0)
							{
								this.addTextChunk(textChunk.toString());					
							}
							fieldChunk = token.substring(2, end);
							this.addFieldChunk(fieldChunk);					
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
					else if ( token.startsWith("V{") && wasDelim )
					{
						end = token.indexOf('}');
						if (end > 0)
						{
							if (textChunk.length() > 0)
							{
								this.addTextChunk(textChunk.toString());					
							}
							variableChunk = token.substring(2, end);
							this.addVariableChunk(variableChunk);					
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
