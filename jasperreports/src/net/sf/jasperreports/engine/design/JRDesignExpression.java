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
import net.sf.jasperreports.engine.util.JRCloneUtils;


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
	
	/**
	 * @deprecated To be removed.
	 */
	public static final String PROPERTY_VALUE_CLASS_NAME = "valueClassName";

	/**
	 *
	 */
	protected List<JRExpressionChunk> chunks = new ArrayList<JRExpressionChunk>();


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
	public JRDesignExpression(String text)
	{
		this();

		setText(text);
	}


	/**
	 * @deprecated To be removed.
	 */
	public void setValueClass(Class<?> clazz)
	{
		setValueClassName(clazz.getName());
	}

	/**
	 * @deprecated To be removed.
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
	public void setChunks(List<JRExpressionChunk> chunks)
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
		clone.chunks = JRCloneUtils.cloneList(chunks);
		clone.eventSupport = null;
		return clone;
	}
	
}
