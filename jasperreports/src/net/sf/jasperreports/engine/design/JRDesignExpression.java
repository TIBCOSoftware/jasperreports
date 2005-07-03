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

/*
 * Contributors:
 * Ryan Johnson - delscovich@users.sourceforge.net 
 */
package net.sf.jasperreports.engine.design;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import net.sf.jasperreports.engine.JRExpressionChunk;
import net.sf.jasperreports.engine.base.JRBaseExpression;


/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id$
 */
public class JRDesignExpression extends JRBaseExpression
{
	/** Mechanism for firing property change events. */
	private transient PropertyChangeSupport propSupport;

	/** Bean property name for name. */
	public static final String NAME_PROPERTY = "name";

	/** Bean property name for id. */
	public static final String ID_PROPERTY = "id";

	/** Bean property name for value class name. */
	public static final String VALUE_CLASS_PROPERTY = "valueClassName";

	/**
	 *
	 */
	private static final long serialVersionUID = 608;

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
		Object oldValue = this.valueClassName;
		valueClassName = className;
		valueClass = null;
		getPropertyChangeSupport().firePropertyChange(VALUE_CLASS_PROPERTY, oldValue, this.valueClassName);
	}

	/**
	 *
	 */
	public void setId(int id)
	{
		int oldValue = this.id;
		this.id = id;
		getPropertyChangeSupport().firePropertyChange(ID_PROPERTY, oldValue, this.id);
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
	public void addResourceChunk(String text)
	{
		JRDesignExpressionChunk chunk = new JRDesignExpressionChunk();
		chunk.setType(JRDesignExpressionChunk.TYPE_RESOURCE);
		chunk.setText(text);
		
		this.chunks.add(chunk);
	}

	/**
	 *
	 */
	public void setText(String text)
	{
		chunks.clear();
		
		if (text != null)
		{
			int end = 0;
			StringBuffer textChunk = new StringBuffer();
			
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
								addTextChunk(textChunk.toString());					
							}
							addParameterChunk(token.substring(2, end));					
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
								addTextChunk(textChunk.toString());					
							}
							addFieldChunk(token.substring(2, end));					
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
								addTextChunk(textChunk.toString());					
							}
							addVariableChunk(token.substring(2, end));					
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
					else if ( token.startsWith("R{") && wasDelim )
					{
						end = token.indexOf('}');
						if (end > 0)
						{
							if (textChunk.length() > 0)
							{
								addTextChunk(textChunk.toString());					
							}
							addResourceChunk(token.substring(2, end));					
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


	/**
	 * Add a property listener to listen to all properties of this class.
	 * @param l The property listener to add.
	 */
	public void addPropertyChangeListener(PropertyChangeListener l) {
		getPropertyChangeSupport().addPropertyChangeListener(l);
	}

	/**
	 * Remove a property change listener.  This will remove any listener that was added
	 * through either of the addPropertyListener methods.
	 * @param l The listener to remove.
	 */
	public void removePropertyChangeListener(PropertyChangeListener l) {
		getPropertyChangeSupport().removePropertyChangeListener(l);
	}

	/**
	 * Get the property change support object for this class.  Because the
	 * property change support object has to be transient, it may need to be
	 * created.
	 * @return The property change support object.
	 */
	protected PropertyChangeSupport getPropertyChangeSupport() {
		if (propSupport == null) {
			propSupport = new PropertyChangeSupport(this);
		}
		return propSupport;
	}
}
