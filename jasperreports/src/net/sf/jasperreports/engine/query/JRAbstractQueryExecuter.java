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
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.sf.jasperreports.engine.JRDataset;
import net.sf.jasperreports.engine.JRParameter;
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
	
	protected static final int CLAUSE_POSITION_ID = 0;

	/**
	 * A parameter present in the query.
	 */
	protected static class QueryParameter
	{
		protected static final int COUNT_SINGLE = -1;
		
		private final String name;
		private final int count;
		
		public QueryParameter(String name)
		{
			this(name, COUNT_SINGLE);
		}
		
		public QueryParameter(String name, int count)
		{
			this.name = name;
			this.count = count;
		}
		
		/**
		 * Decides whether the parameter has multiple values.
		 * 
		 * @return whether the parameter has multiple values.
		 */
		public boolean isMulti()
		{
			return count != COUNT_SINGLE;
		}
		
		/**
		 * Returns the number of parameter values.
		 * 
		 * @return the number of parameter values
		 * @see #isMulti()
		 */
		public int getCount()
		{
			return count;
		}
		
		/**
		 * Returns the name of the report parameter.
		 * 
		 * @return the name of the report parameter
		 */
		public String getName()
		{
			return name;
		}
	}
	
	/**
	 * Clause function registry.
	 */
	protected final Map clauseFunctions = new HashMap();
	
	protected final JRDataset dataset;
	private final Map parametersMap;
	
	private String queryString;
	
	/**
	 * List of {@link QueryParameter query parameters}.
	 */
	private List queryParameters;
	
	private Set parameterClauseStack;
	
	
	protected JRAbstractQueryExecuter(JRDataset dataset, Map parametersMap)
	{
		this.dataset = dataset;
		this.parametersMap = parametersMap;
		
		queryString = "";
		queryParameters = new ArrayList();
	}

	/**
	 * Registers a clause function.
	 * 
	 * @param id the function ID
	 * @param function the function
	 */
	protected void registerClauseFunction(String id, JRClauseFunction function)
	{
		clauseFunctions.put(id, function);
	}
	
	/**
	 * Unregisters a clause function.
	 * 
	 * @param id the function ID
	 */
	protected void unregisterClauseFunction(String id)
	{
		clauseFunctions.remove(id);
	}
	
	/**
	 * Resolves a clause function ID to a function instance.
	 * 
	 * @param id the function ID
	 * @return the clause function registered for the ID
	 * @throws JRRuntimeException if no function for the ID is found
	 */
	protected JRClauseFunction resolveFunction(String id)
	{
		JRClauseFunction function = (JRClauseFunction) clauseFunctions.get(id);
		if (function == null)
		{
			throw new JRRuntimeException("No clause function for id " + id + " found");
		}
		return function;
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
		switch (chunk.getType())
		{
			case JRQueryChunk.TYPE_PARAMETER_CLAUSE :
			{
				appendParameterClauseChunk(sbuffer, chunk.getText());
				break;
			}
			case JRQueryChunk.TYPE_PARAMETER :
			{
				appendParameterChunk(sbuffer, chunk.getText());
				break;
			}
			case JRQueryChunk.TYPE_CLAUSE_TOKENS :
			{
				appendClauseChunk(sbuffer, chunk.getTokens());
				break;
			}
			case JRQueryChunk.TYPE_TEXT :
			default :
			{
				appendTextChunk(sbuffer, chunk.getText());
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
		addQueryParameter(chunkText);
	}


	/**
	 * Records a query parameter.
	 * 
	 * @param parameterName the parameter name
	 * @see #getCollectedParameters()
	 */
	protected void addQueryParameter(String parameterName)
	{
		QueryParameter param = new QueryParameter(parameterName);
		queryParameters.add(param);
	}


	/**
	 * Records a multi-valued query parameter.
	 * 
	 * @param parameterName the parameter name
	 * @param count the value count
	 * @see #getCollectedParameters()
	 * @see QueryParameter#isMulti()
	 */
	protected void addQueryMultiParameters(String parameterName, int count)
	{
		QueryParameter param = new QueryParameter(parameterName, count);
		queryParameters.add(param);
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

				public void handleClauseChunk(String[] tokens)
				{
					appendClauseChunk(sbuffer, tokens);
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
	 * Handles a {@link JRQueryChunk#TYPE_CLAUSE_TOKENS clause query chunk}.
	 * <p>
	 * The default implementation considers the first token as a
	 * {@link JRClauseFunction clause function} ID and delegates the call to the
	 * function.
	 * </p>
	 * <p>
	 * Extending query executers can override this to implement custom query clause handling.
	 * </p>
	 * 
	 * @param sbuffer the query text buffer
	 * @param clauseTokens clause tokens
	 * @see #registerClauseFunction(String, JRClauseFunction)
	 * @throws JRRuntimeException if there is no first token or no clause function is found for the ID
	 */
	protected void appendClauseChunk(final StringBuffer sbuffer, String[] clauseTokens)
	{
		JRClauseTokens tokens = new JRClauseTokens(clauseTokens);
		String id = tokens.getToken(CLAUSE_POSITION_ID);
		if (id == null)
		{
			throw new JRRuntimeException("Query clause ID/first token missing");
		}
		
		JRClauseFunction function = resolveFunction(id);
		applyClause(function, tokens, sbuffer);
	}

	
	protected void applyClause(JRClauseFunction function, JRClauseTokens tokens, final StringBuffer sbuffer)
	{
		function.apply(tokens, new JRQueryClauseContext()
		{
			public void addQueryMultiParameters(String parameterName, int count)
			{
				JRAbstractQueryExecuter.this.addQueryMultiParameters(parameterName, count);
			}

			public void addQueryParameter(String parameterName)
			{
				JRAbstractQueryExecuter.this.addQueryParameter(parameterName);
			}

			public JRValueParameter getValueParameter(String parameterName)
			{
				return JRAbstractQueryExecuter.this.getValueParameter(parameterName);
			}

			public StringBuffer queryBuffer()
			{
				return sbuffer;
			}
		});
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
		List parameterNames = new ArrayList(queryParameters.size());
		for (Iterator it = queryParameters.iterator(); it.hasNext();)
		{
			QueryParameter param = (QueryParameter) it.next();
			parameterNames.add(param.getName());
		}
		return parameterNames;
	}
	
	
	/**
	 * Returns the list of {@link QueryParameter query parameters} in the order in which they appear in the query.
	 * 
	 * @return the list of query parameters
	 */
	protected List getCollectedParameters()
	{
		return queryParameters;
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
		if (ignoreMissing)
		{
			JRValueParameter parameter = getValueParameter(JRParameter.REPORT_PARAMETERS_MAP, false);
			return ((Map)parameter.getValue()).get(parameterName);
		}
		
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
	 * Return a value parameter from the parameters map.
	 * 
	 * @param parameterName the parameter name
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
