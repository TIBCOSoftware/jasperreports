/*
 * ============================================================================
 *                   GNU Lesser General Public License
 * ============================================================================
 *
 * JasperReports - Free Java report-generating library.
 * Copyright (C) 2001-2005 Teodor Danciu teodord@users.sourceforge.net
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
package net.sf.jasperreports.engine.base;

import java.io.Serializable;

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
	private static final long serialVersionUID = 605;

	/**
	 *
	 */
	protected String valueClassName = null;
	protected String name = null;
	protected int id = 0;

	protected transient Class valueClass = null;

	/**
	 *
	 */
	private JRExpressionChunk[] chunks = null;


	/**
	 *
	 */
	protected JRBaseExpression()
	{
	}
	
	
	/**
	 *
	 */
	protected JRBaseExpression(JRExpression expression, JRBaseObjectFactory factory)
	{
		factory.put(expression, this);
		
		valueClassName = expression.getValueClassName();
		name = expression.getName();
		id = expression.getId();
		
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
	 *
	 */
	public Class getValueClass()
	{
		if (valueClass == null)
		{
			if (valueClassName != null)
			{
				try
				{
					valueClass = JRClassLoader.loadClassForName(valueClassName);
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
	public String getName()
	{
		return name;
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
						sbuffer.append( chunks[i].getText() );
						break;
					}
				}
			}

			text = sbuffer.toString();
		}
		
		return text;
	}


}
