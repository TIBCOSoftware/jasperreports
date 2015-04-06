/*
 * JasperReports - Free Java Reporting Library.
 * Copyright (C) 2001 - 2014 TIBCO Software Inc. All rights reserved.
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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.sf.jasperreports.engine.DefaultJasperReportsContext;
import net.sf.jasperreports.engine.JRConstants;
import net.sf.jasperreports.engine.JRExpressionChunk;
import net.sf.jasperreports.engine.JRPropertiesUtil;
import net.sf.jasperreports.engine.JRRuntimeException;
import net.sf.jasperreports.engine.base.JRBaseExpression;
import net.sf.jasperreports.engine.design.events.JRChangeEventsSupport;
import net.sf.jasperreports.engine.design.events.JRPropertyChangeSupport;
import net.sf.jasperreports.engine.util.JRCloneUtils;
import net.sf.jasperreports.engine.util.JRExpressionUtil;


/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 */
public class JRDesignExpression extends JRBaseExpression implements JRChangeEventsSupport
{
	/**
	 *
	 */
	private static final long serialVersionUID = JRConstants.SERIAL_VERSION_UID;

	public static final String PROPERTY_LEGACY_PARSER = 
			JRPropertiesUtil.PROPERTY_PREFIX + "legacy.expression.parser";
	
	protected static final boolean LEGACY_PARSER;
	static
	{
		JRPropertiesUtil properties = JRPropertiesUtil.getInstance(DefaultJasperReportsContext.getInstance());
		LEGACY_PARSER = properties.getBooleanProperty(PROPERTY_LEGACY_PARSER, false);
	}
	
	public static final Pattern PLACEHOLDER_PATTERN = 
			Pattern.compile("\\$([RPFV])\\{(.*?)\\}", Pattern.MULTILINE | Pattern.DOTALL);
	
	protected static final int PLACEHOLDER_TYPE_INDEX = 1;
	protected static final int PLACEHOLDER_TEXT_INDEX = 2;
	
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
			if (LEGACY_PARSER)
			{
				//FIXME remove this at some point
				legacyParseText(text);
			}
			else
			{
				parseText(text);
			}
		}
		
		getEventSupport().firePropertyChange(PROPERTY_TEXT, old, text);
	}

	protected void legacyParseText(String text)
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
	
	protected void parseText(String text)
	{
		Matcher matcher = PLACEHOLDER_PATTERN.matcher(text);

		int textChunkStart = 0;
		StringBuilder textChunk = new StringBuilder(text.length());
		while(matcher.find())
		{
			int matchStart = matcher.start();
			int matchEnd = matcher.end();
			if (matchStart > 0 && text.charAt(matchStart - 1) == '$')
			{
				// we have a $$ escape, append it to the text chunk with a single $
				textChunk.append(text, textChunkStart, matchStart - 1);
				textChunk.append(text, matchStart, matchEnd);
			}
			else
			{
				// we have a proper placeholder
				textChunk.append(text, textChunkStart, matchStart);
				if (textChunk.length() > 0)
				{
					addTextChunk(textChunk.toString());
					textChunk.delete(0, textChunk.length());
				}
				
				String chunkStringType = matcher.group(PLACEHOLDER_TYPE_INDEX);
				byte chunkType = chunkStringToType(chunkStringType);
				String chunkText = matcher.group(PLACEHOLDER_TEXT_INDEX);
				addChunk(chunkType, chunkText);
			}
			
			textChunkStart = matchEnd;
		}
		
		textChunk.append(text, textChunkStart, text.length());
		if (textChunk.length() > 0)
		{
			addTextChunk(textChunk.toString());
		}
	}
	
	protected static byte chunkStringToType(String chunkStringType)
	{
		byte chunkType;
		//FIXME faster way to do this
		if (chunkStringType.startsWith("P"))
		{
			chunkType = JRExpressionChunk.TYPE_PARAMETER;
		}
		else if (chunkStringType.startsWith("F"))
		{
			chunkType = JRExpressionChunk.TYPE_FIELD;
		}
		else if (chunkStringType.startsWith("V"))
		{
			chunkType = JRExpressionChunk.TYPE_VARIABLE;
		}
		else if (chunkStringType.startsWith("R"))
		{
			chunkType = JRExpressionChunk.TYPE_RESOURCE;
		}
		else
		{
			throw 
				new JRRuntimeException(
					JRExpressionUtil.EXCEPTION_MESSAGE_KEY_UNKNOWN_EXPRESSION_CHUNK_TYPE,
					new Object[]{chunkStringType});
		}
		return chunkType;
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
