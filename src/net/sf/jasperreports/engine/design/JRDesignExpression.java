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

/*
 * Contributors:
 * Ryan Johnson - delscovich@users.sourceforge.net 
 */
package net.sf.jasperreports.engine.design;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import net.sf.jasperreports.engine.JRConstants;
import net.sf.jasperreports.engine.JRExpressionChunk;
import net.sf.jasperreports.engine.base.JRBaseExpression;
import net.sf.jasperreports.engine.design.events.JRChangeEventsSupport;
import net.sf.jasperreports.engine.design.events.JRPropertyChangeSupport;


/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id$
 */
public class JRDesignExpression extends JRBaseExpression implements JRChangeEventsSupport
{
	/**
	 *
	 */
	private static final long serialVersionUID = JRConstants.SERIAL_VERSION_UID;
	
	public static final String PROPERTY_TEXT = "text";
	
	public static final String PROPERTY_VALUE_CLASS_NAME = "valueClassName";

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

		regenerateId();
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
		Object old = this.valueClassName;
		valueClassName = className;
		valueClass = null;
		valueClassRealName = null;
		getEventSupport().firePropertyChange(PROPERTY_VALUE_CLASS_NAME, old, this.valueClassName);
	}

	/**
	 * FIXMENOW remove me?
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
	 * Clears the current list of chunks and adds the passed list of chunks.  The reference
	 * to the list passed is not kept.
	 */
	public void setChunks(List chunks)
	{
		this.chunks.clear();
		this.chunks.addAll(chunks);
	}

	/**
	 *
	 */
	public void addChunk(JRDesignExpressionChunk chunk)
	{
		this.chunks.add(chunk);
	}
		
	protected void addChunk(byte type, String text)
	{
		JRDesignExpressionChunk chunk = new JRDesignExpressionChunk();
		chunk.setType(type);
		chunk.setText(text);
		
		this.chunks.add(chunk);
	}
		
	/**
	 *
	 */
	public void addTextChunk(String text)
	{
		JRDesignExpressionChunk chunk = new JRDesignExpressionChunk();
		chunk.setType(JRExpressionChunk.TYPE_TEXT);
		chunk.setText(text);
		
		this.chunks.add(chunk);
	}
		
	/**
	 *
	 */
	public void addParameterChunk(String text)
	{
		JRDesignExpressionChunk chunk = new JRDesignExpressionChunk();
		chunk.setType(JRExpressionChunk.TYPE_PARAMETER);
		chunk.setText(text);
		
		this.chunks.add(chunk);
	}
		
	/**
	 *
	 */
	public void addFieldChunk(String text)
	{
		JRDesignExpressionChunk chunk = new JRDesignExpressionChunk();
		chunk.setType(JRExpressionChunk.TYPE_FIELD);
		chunk.setText(text);
		
		this.chunks.add(chunk);
	}
		
	/**
	 *
	 */
	public void addVariableChunk(String text)
	{
		JRDesignExpressionChunk chunk = new JRDesignExpressionChunk();
		chunk.setType(JRExpressionChunk.TYPE_VARIABLE);
		chunk.setText(text);
		
		this.chunks.add(chunk);
	}

	/**
	 *
	 */
	public void addResourceChunk(String text)
	{
		JRDesignExpressionChunk chunk = new JRDesignExpressionChunk();
		chunk.setType(JRExpressionChunk.TYPE_RESOURCE);
		chunk.setText(text);
		
		this.chunks.add(chunk);
	}

	/**
	 *
	 */
	public void setText(String text)
	{
		Object old = getText();
		
		chunks.clear();
		
		if (text != null)
		{
			StringBuffer textChunk = new StringBuffer();
			
			StringTokenizer tkzer = new StringTokenizer(text, "$", true);
			int behindDelims = 0;
			while (tkzer.hasMoreTokens())
			{
				String token = tkzer.nextToken();
	
				if (token.equals("$"))
				{
					if (behindDelims > 0)
					{
						textChunk.append("$");
					}
	
					++behindDelims;
				}
				else
				{
					byte chunkType = JRExpressionChunk.TYPE_TEXT;
					if (behindDelims > 0)
					{
						if (token.startsWith("P{"))
						{
							chunkType = JRExpressionChunk.TYPE_PARAMETER;
						}
						else if (token.startsWith("F{"))
						{
							chunkType = JRExpressionChunk.TYPE_FIELD;
						}
						else if (token.startsWith("V{"))
						{
							chunkType = JRExpressionChunk.TYPE_VARIABLE;
						}
						else if (token.startsWith("R{"))
						{
							chunkType = JRExpressionChunk.TYPE_RESOURCE;
						}
					}
					
					if (chunkType == JRExpressionChunk.TYPE_TEXT)
					{
						if (behindDelims > 0)
						{
							textChunk.append("$");
						}
						textChunk.append(token);
					}
					else
					{
						int end = token.indexOf('}');
						if (end > 0)
						{
							if (behindDelims > 1)
							{
								textChunk.append(token);
							}
							else
							{
								if (textChunk.length() > 0)
								{
									addTextChunk(textChunk.toString());					
								}
								
								addChunk(chunkType, token.substring(2, end));					
								textChunk = new StringBuffer(token.substring(end + 1));
							}
						}
						else
						{
							if (behindDelims > 0)
							{
								textChunk.append("$");
							}
							textChunk.append(token);
						}
					}
	
					behindDelims = 0;
				}
			}

			if (behindDelims > 0)
			{
				textChunk.append("$");
			}

			if (textChunk.length() > 0)
			{
				this.addTextChunk(textChunk.toString());					
			}
		}
		
		getEventSupport().firePropertyChange(PROPERTY_TEXT, old, text);
	}
	
	private transient JRPropertyChangeSupport eventSupport;
	
	public JRPropertyChangeSupport getEventSupport()
	{
		synchronized (this)
		{
			if (eventSupport == null)
			{
				eventSupport = new JRPropertyChangeSupport(this);
			}
		}
		
		return eventSupport;
	}

	/**
	 * 
	 */
	public Object clone() 
	{
		JRDesignExpression clone = (JRDesignExpression)super.clone();
		
		if (chunks != null)
		{
			clone.chunks = new ArrayList(chunks.size());
			for(int i = 0; i < chunks.size(); i++)
			{
				clone.chunks.add(((JRExpressionChunk)chunks.get(i)).clone());
			}
		}

		return clone;
	}
	
}
