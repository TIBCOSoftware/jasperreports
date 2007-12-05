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
import java.util.StringTokenizer;

import net.sf.jasperreports.engine.JRConstants;
import net.sf.jasperreports.engine.JRExpression;
import net.sf.jasperreports.engine.JRExpressionChunk;
import net.sf.jasperreports.engine.JRRuntimeException;
import net.sf.jasperreports.engine.util.JRClassLoader;


/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id$
 */
public class JRBaseExpression implements JRExpression, Serializable
{


	/**
	 *
	 */
	private static final long serialVersionUID = JRConstants.SERIAL_VERSION_UID;

	/**
	 *
	 */
	protected String valueClassName = null;
	protected String valueClassRealName = null;
	protected int id = 0;

	protected transient Class valueClass = null;

	/**
	 *
	 */
	private JRExpressionChunk[] chunks = null;

	/**
	 *
	 */
	private static int lastId = 0;


	/**
	 *
	 */
	protected JRBaseExpression()
	{
	}
	
	
	/**
	 * Creates a copy of an expression.
	 * 
	 * @param expression the original expression
	 * @param factory the base object factory
	 * @param expressionId if not null, the created expression will use it as ID
	 * instead of the original expressions's ID 
	 */
	protected JRBaseExpression(JRExpression expression, JRBaseObjectFactory factory, Integer expressionId)
	{
		factory.put(expression, this);
		
		valueClassName = expression.getValueClassName();
		if (expressionId == null)
		{
			id = expression.getId();
		}
		else
		{
			id = expressionId.intValue();
		}
		
		/*   */
		JRExpressionChunk[] jrChunks = expression.getChunks();
		if (jrChunks != null && jrChunks.length > 0)
		{
			chunks = new JRExpressionChunk[jrChunks.length];
			for(int i = 0; i < chunks.length; i++)
			{
				chunks[i] = factory.getExpressionChunk(jrChunks[i]);
			}
		}
	}

	
	/**
	 * Creates a copy of an expression.
	 * 
	 * @param expression the original expression
	 * @param factory the base object factory
	 */
	protected JRBaseExpression(JRExpression expression, JRBaseObjectFactory factory)
	{
		this(expression, factory, null);
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
	public void regenerateId()
	{
		id = getNextId();
	}


	/**
	 *
	 */
	public Class getValueClass()
	{
		if (valueClass == null)
		{
			String className = getValueClassRealName();
			if (className != null)
			{
				try
				{
					valueClass = JRClassLoader.loadClassForName(className);
				}
				catch(ClassNotFoundException e)
				{
					throw new JRRuntimeException(e);
				}
			}
		}
		
		return valueClass;
	}
	
	/**
	 *
	 */
	public String getValueClassName()
	{
		return valueClassName;
	}
	
	/**
	 *
	 */
	private String getValueClassRealName()
	{
		if (valueClassRealName == null)
		{
			valueClassRealName = JRClassLoader.getClassRealName(valueClassName);
		}
		
		return valueClassRealName;
	}

	/**
	 *
	 */
	public int getId()
	{
		return id;
	}

	/**
	 *
	 */
	public JRExpressionChunk[] getChunks()
	{
		return chunks;
	}
			
	/**
	 *
	 */
	public String getText()
	{
		String text = "";
		
		chunks = getChunks();
		if (chunks != null && chunks.length > 0)
		{
			StringBuffer sbuffer = new StringBuffer();

			for(int i = 0; i < chunks.length; i++)
			{
				switch(chunks[i].getType())
				{
					case JRExpressionChunk.TYPE_PARAMETER :
					{
						sbuffer.append("$P{");
						sbuffer.append( chunks[i].getText() );
						sbuffer.append("}");
						break;
					}
					case JRExpressionChunk.TYPE_FIELD :
					{
						sbuffer.append("$F{");
						sbuffer.append( chunks[i].getText() );
						sbuffer.append("}");
						break;
					}
					case JRExpressionChunk.TYPE_VARIABLE :
					{
						sbuffer.append("$V{");
						sbuffer.append( chunks[i].getText() );
						sbuffer.append("}");
						break;
					}
					case JRExpressionChunk.TYPE_RESOURCE :
					{
						sbuffer.append("$R{");
						sbuffer.append( chunks[i].getText() );
						sbuffer.append("}");
						break;
					}
					case JRExpressionChunk.TYPE_TEXT :
					default :
					{
						String textChunk = chunks[i].getText();
						String escapedText = escapeTextChunk(textChunk);
						sbuffer.append(escapedText);
						break;
					}
				}
			}

			text = sbuffer.toString();
		}
		
		return text;
	}
	
	protected String escapeTextChunk(String text)
	{
		if (text == null || text.indexOf('$') < 0)
		{
			return text;
		}
		
		StringBuffer sb = new StringBuffer(text.length() + 4);
		StringTokenizer tkzer = new StringTokenizer(text, "$", true);
		boolean wasDelim = false;
		while (tkzer.hasMoreElements())
		{
			String token = tkzer.nextToken();
			if (wasDelim &&
					(token.startsWith("P{") || token.startsWith("F{") || token.startsWith("V{") || token.startsWith("R{")) && 
					token.indexOf('}') > 0)
			{
				sb.append('$');
			}
			sb.append(token);
			wasDelim = token.equals("$");
		}
		
		return sb.toString();
	}


	/**
	 * 
	 */
	public Object clone() 
	{
		JRBaseExpression clone = null;
		
		try
		{
			clone = (JRBaseExpression)super.clone();
		}
		catch (CloneNotSupportedException e)
		{
			throw new JRRuntimeException(e);
		}

		if (chunks != null)
		{
			clone.chunks = new JRExpressionChunk[chunks.length];
			for(int i = 0; i < chunks.length; i++)
			{
				clone.chunks[i] = (JRExpressionChunk)chunks[i].clone();
			}
		}

		return clone;
	}
	
	
}
