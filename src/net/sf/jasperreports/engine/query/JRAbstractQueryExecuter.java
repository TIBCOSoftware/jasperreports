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
package net.sf.jasperreports.engine.query;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.sf.jasperreports.engine.JRDataset;
import net.sf.jasperreports.engine.JRQuery;
import net.sf.jasperreports.engine.JRQueryChunk;
import net.sf.jasperreports.engine.JRRuntimeException;
import net.sf.jasperreports.engine.JRValueParameter;
import net.sf.jasperreports.engine.fill.JRFillParameter;
import net.sf.jasperreports.engine.util.JRQueryChunkHandler;
import net.sf.jasperreports.engine.util.JRQueryParser;

/**
 * Base abstract query executer.
 * 
 * @author Lucian Chirita (lucianc@users.sourceforge.net)
 * @version $Id$
 */
public abstract class JRAbstractQueryExecuter implements JRQueryExecuter
{
	protected final JRDataset dataset;
	private final Map parametersMap;
	
	private String queryString;
	private List parameterNames;
	private Set parameterClauseStack;
	
	
	protected JRAbstractQueryExecuter(JRDataset dataset, Map parametersMap)
	{
		this.dataset = dataset;
		this.parametersMap = parametersMap;
		
		queryString = "";
		parameterNames = new ArrayList();
	}
	
	
	/**
	 * Parses the query and replaces the parameter clauses by the paramter values and
	 * the parameters by the return value of {@link #getParameterReplacement(String) getParameterReplacement}.
	 *
	 */
	protected void parseQuery()
	{
		parameterClauseStack = new HashSet();
		
		JRQuery query = dataset.getQuery();
		
		if (query != null)
		{
			JRQueryChunk[] chunks = query.getChunks();
			if (chunks != null && chunks.length > 0)
			{
				StringBuffer sbuffer = new StringBuffer();
				for(int i = 0; i < chunks.length; i++)
				{
					JRQueryChunk chunk = chunks[i];
					appendQueryChunk(sbuffer, chunk);
				}

				queryString = sbuffer.toString();
			}
		}
	}


	protected void appendQueryChunk(StringBuffer sbuffer, JRQueryChunk chunk)
	{
		String chunkText = chunk.getText();
		switch (chunk.getType())
		{
			case JRQueryChunk.TYPE_PARAMETER_CLAUSE :
			{
				appendParameterClauseChunk(sbuffer, chunkText);
				break;
			}
			case JRQueryChunk.TYPE_PARAMETER :
			{
				appendParameterChunk(sbuffer, chunkText);
				break;
			}
			case JRQueryChunk.TYPE_TEXT :
			default :
			{
				appendTextChunk(sbuffer, chunkText);
				break;
			}
		}
	}


	protected void appendTextChunk(StringBuffer sbuffer, String text)
	{
		sbuffer.append(text);
	}


	protected void appendParameterChunk(StringBuffer sbuffer, String chunkText)
	{
		String parameterName = chunkText;
		checkParameter(parameterName);

		sbuffer.append(getParameterReplacement(parameterName));
		parameterNames.add(chunkText);
	}


	protected void appendParameterClauseChunk(final StringBuffer sbuffer, String chunkText)
	{
		String parameterName = chunkText;
		checkParameter(parameterName);
		
		if (!parameterClauseStack.add(parameterName))
		{
			throw new JRRuntimeException("The query contains circularly nested parameter clauses starting with " + parameterName);
		}
		
		try
		{
			Object parameterValue = getParameterValue(parameterName);
			String clauseText = String.valueOf(parameterValue);
			JRQueryChunkHandler nestedChunkHandler = new JRQueryChunkHandler()
			{

				public void handleParameterChunk(String text)
				{
					appendParameterChunk(sbuffer, text);
				}

				public void handleParameterClauseChunk(String text)
				{
					appendParameterClauseChunk(sbuffer, text);
				}

				public void handleTextChunk(String text)
				{
					appendTextChunk(sbuffer, text);
				}
			};
			JRQueryParser.instance().parse(clauseText, nestedChunkHandler);
		}
		finally
		{
			parameterClauseStack.remove(parameterName);
		}
	}

	
	/**
	 * Returns the parsed query string with the paramter clauses replaced by the paramter values and 
	 * the parameters replaced by {@link #getParameterReplacement(String) getParameterReplacement}.
	 * 
	 * @return the parsed query string
	 */
	protected String getQueryString()
	{
		return queryString;
	}
	
	
	/**
	 * Returns the list of parameter names in the order in which they appear in the query.
	 * 
	 * @return the list of parameter names
	 */
	protected List getCollectedParameterNames()
	{
		return parameterNames;
	}
	
	
	/**
	 * Returns the value of a fill paramter.
	 * @param parameterName the paramter name
	 * @param ignoreMissing if <code>true</code>, the method will return null for non existing parameters;
	 * 		otherwise, an exception will be thrown if the parameter does not exist
	 * @return the parameter value
	 */
	protected Object getParameterValue(String parameterName, boolean ignoreMissing)
	{
		JRValueParameter parameter = getValueParameter(parameterName, ignoreMissing);
		return parameter == null ? null : parameter.getValue();
	}
	
	
	/**
	 * Returns the value of a fill paramter.
	 * @param parameterName the paramter name
	 * @return the parameter value
	 */
	protected Object getParameterValue(String parameterName)
	{
		return getParameterValue(parameterName, false);
	}
	
	
	/**
	 * Return a fill parameter from the paramter map.
	 * 
	 * @param parameterName the paramter name
	 * @return the parameter
	 * @deprecated {@link #getValueParameter(String) getValueParameter(String)} should be used instead
	 */
	protected JRFillParameter getParameter(String parameterName)
	{
		JRFillParameter parameter = (JRFillParameter) parametersMap.get(parameterName);
		
		if (parameter == null)
		{
			throw new JRRuntimeException("Parameter \"" + parameterName + "\" does not exist.");
		}
		
		return parameter;
	}

	
	protected void checkParameter(String parameterName)
	{
		if (!parametersMap.containsKey(parameterName))
		{
			throw new JRRuntimeException("Parameter \"" + parameterName + "\" does not exist.");
		}
	}
	
	
	/**
	 * Return a value parameter from the paramters map.
	 * 
	 * @param parameterName the paramter name
	 * @param ignoreMissing if <code>true</code>, the method will return null for non existing parameters;
	 * 		otherwise, an exception will be thrown if the parameter does not exist
	 * @return the parameter
	 */
	protected JRValueParameter getValueParameter(String parameterName, boolean ignoreMissing)
	{
		JRValueParameter parameter = (JRValueParameter) parametersMap.get(parameterName);
		
		if (parameter == null && !ignoreMissing)
		{
			throw new JRRuntimeException("Parameter \"" + parameterName + "\" does not exist.");
		}
		
		return parameter;
	}

	
	/**
	 * Return a value parameter from the paramters map.
	 * 
	 * @param parameterName the paramter name
	 * @return the parameter
	 */
	protected JRValueParameter getValueParameter(String parameterName)
	{
		return getValueParameter(parameterName, false);
	}

	
	/**
	 * Returns the replacement text for a query paramter.
	 * 
	 * @param parameterName the paramter name
	 * @return the replacement text
	 * @see JRQueryChunk#TYPE_PARAMETER
	 */
	protected abstract String getParameterReplacement(String parameterName);
}
