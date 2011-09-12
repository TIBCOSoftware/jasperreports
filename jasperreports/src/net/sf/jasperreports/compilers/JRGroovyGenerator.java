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
 * Peter Severin - peter_p_s@users.sourceforge.net 
 * Gaganis Giorgos - gaganis@users.sourceforge.net
 */
package net.sf.jasperreports.compilers;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExpression;
import net.sf.jasperreports.engine.JRExpressionChunk;
import net.sf.jasperreports.engine.JRField;
import net.sf.jasperreports.engine.JRParameter;
import net.sf.jasperreports.engine.JRVariable;
import net.sf.jasperreports.engine.design.JRSourceCompileTask;
import net.sf.jasperreports.engine.util.JRStringUtil;


/**
 * @author Teodor Danciu (teodord@users.sourceforge.net), Peter Severin (peter_p_s@users.sourceforge.net)
 * @version $Id$
 */
public class JRGroovyGenerator
{
	
	/**
	 *
	 */
	private static final int EXPR_MAX_COUNT_PER_METHOD = 100;

	private static Map<Byte, String> fieldPrefixMap;
	private static Map<Byte, String> variablePrefixMap;
	private static Map<Byte, String> methodSuffixMap;

	static
	{
		fieldPrefixMap = new HashMap<Byte, String>();
		fieldPrefixMap.put(new Byte(JRExpression.EVALUATION_OLD),       "Old");
		fieldPrefixMap.put(new Byte(JRExpression.EVALUATION_ESTIMATED), "");
		fieldPrefixMap.put(new Byte(JRExpression.EVALUATION_DEFAULT),   "");
		
		variablePrefixMap = new HashMap<Byte, String>();
		variablePrefixMap.put(new Byte(JRExpression.EVALUATION_OLD),       "Old");
		variablePrefixMap.put(new Byte(JRExpression.EVALUATION_ESTIMATED), "Estimated");
		variablePrefixMap.put(new Byte(JRExpression.EVALUATION_DEFAULT),   "");
		
		methodSuffixMap = new HashMap<Byte, String>();
		methodSuffixMap.put(new Byte(JRExpression.EVALUATION_OLD),       "Old");
		methodSuffixMap.put(new Byte(JRExpression.EVALUATION_ESTIMATED), "Estimated");
		methodSuffixMap.put(new Byte(JRExpression.EVALUATION_DEFAULT),   "");
	}
	
	/**
	 *
	 */
	protected final JRSourceCompileTask sourceTask;

	protected Map<String, ? extends JRParameter> parametersMap;
	protected Map<String, JRField> fieldsMap;
	protected Map<String, JRVariable> variablesMap;
	protected JRVariable[] variables;

	
	protected JRGroovyGenerator(JRSourceCompileTask sourceTask)
	{
		this.sourceTask = sourceTask;
		
		this.parametersMap = sourceTask.getParametersMap();
		this.fieldsMap = sourceTask.getFieldsMap();
		this.variablesMap = sourceTask.getVariablesMap();
		this.variables = sourceTask.getVariables();
	}


	/**
	 *
	 */
	public static String generateClass(JRSourceCompileTask sourceTask) throws JRException
	{
		JRGroovyGenerator generator = new JRGroovyGenerator(sourceTask);
		return generator.generateClass();
	}
	
	
	protected String generateClass() throws JRException
	{
		StringBuffer sb = new StringBuffer();

		generateClassStart(sb);

		generateDeclarations(sb);		

		generateInitMethod(sb);
		generateInitParamsMethod(sb);
		if (fieldsMap != null)
		{
			generateInitFieldsMethod(sb);
		}
		generateInitVarsMethod(sb);

		List<JRExpression> expressions = sourceTask.getExpressions();
		sb.append(this.generateMethod(JRExpression.EVALUATION_DEFAULT, expressions));
		if (sourceTask.isOnlyDefaultEvaluation())
		{
			List<JRExpression> empty = new ArrayList<JRExpression>();
			sb.append(this.generateMethod(JRExpression.EVALUATION_OLD, empty));
			sb.append(this.generateMethod(JRExpression.EVALUATION_ESTIMATED, empty));
		}
		else
		{
			sb.append(this.generateMethod(JRExpression.EVALUATION_OLD, expressions));
			sb.append(this.generateMethod(JRExpression.EVALUATION_ESTIMATED, expressions));
		}

		sb.append("}\n");

		return sb.toString();
	}


	private void generateInitMethod(StringBuffer sb)
	{
		sb.append("\n");
		sb.append("\n");
		sb.append("    /**\n");
		sb.append("     *\n");
		sb.append("     */\n");
		sb.append("    void customizedInit(\n"); 
		sb.append("        Map pm,\n");
		sb.append("        Map fm,\n"); 
		sb.append("        Map vm\n");
		sb.append("        )\n");
		sb.append("    {\n");
		sb.append("        initParams(pm);\n");
		if (fieldsMap != null)
		{
			sb.append("        initFields(fm);\n");
		}
		sb.append("        initVars(vm);\n");
		sb.append("    }\n");
		sb.append("\n");
		sb.append("\n");
	}

	
	protected final void generateClassStart(StringBuffer sb)
	{
		/*   */
		sb.append("/*\n");
		sb.append(" * Generated by JasperReports - ");
		sb.append((new SimpleDateFormat()).format(new java.util.Date()));
		sb.append("\n");
		sb.append(" */\n");
		sb.append("import net.sf.jasperreports.engine.*;\n");
		sb.append("import net.sf.jasperreports.engine.fill.*;\n");
		sb.append("\n");
		sb.append("import java.util.*;\n");
		sb.append("import java.math.*;\n");
		sb.append("import java.text.*;\n");
		sb.append("import java.io.*;\n");
		sb.append("import java.net.*;\n");
		sb.append("\n");
		
		/*   */
		String[] imports = sourceTask.getImports();
		if (imports != null && imports.length > 0)
		{
			for (int i = 0; i < imports.length; i++)
			{
				sb.append("import ");
				sb.append(imports[i]);
				sb.append(";\n");
			}
		}

		/*   */
		sb.append("\n");
		sb.append("\n");
		sb.append("/**\n");
		sb.append(" *\n");
		sb.append(" */\n");
		sb.append("class ");
		sb.append(sourceTask.getUnitName());
		sb.append(" extends JREvaluator\n");
		sb.append("{\n"); 
		sb.append("\n");
		sb.append("\n");
		sb.append("    /**\n");
		sb.append("     *\n");
		sb.append("     */\n");
	}


	protected final void generateDeclarations(StringBuffer sb)
	{
		if (parametersMap != null && parametersMap.size() > 0)
		{
			Collection<String> parameterNames = parametersMap.keySet();
			for (Iterator<String> it = parameterNames.iterator(); it.hasNext();)
			{
				sb.append("    private JRFillParameter parameter_");
				sb.append(JRStringUtil.getJavaIdentifier(it.next()));
				sb.append(" = null;\n");
			}
		}
		
		if (fieldsMap != null && fieldsMap.size() > 0)
		{
			Collection<String> fieldNames = fieldsMap.keySet();
			for (Iterator<String> it = fieldNames.iterator(); it.hasNext();)
			{
				sb.append("    private JRFillField field_");
				sb.append(JRStringUtil.getJavaIdentifier(it.next()));
				sb.append(" = null;\n");
			}
		}
		
		if (variables != null && variables.length > 0)
		{
			for (int i = 0; i < variables.length; i++)
			{
				sb.append("    private JRFillVariable variable_");
				sb.append(JRStringUtil.getJavaIdentifier(variables[i].getName()));
				sb.append(" = null;\n");
			}
		}
	}


	protected final void generateInitParamsMethod(StringBuffer sb) throws JRException
	{
		Iterator<String> parIt = null;
		if (parametersMap != null && parametersMap.size() > 0) 
		{
			parIt = parametersMap.keySet().iterator();
		}
		else
		{
			Set<String> emptySet = Collections.emptySet();
			parIt = emptySet.iterator();
		}
		generateInitParamsMethod(sb, parIt, 0);
	}


	protected final void generateInitFieldsMethod(StringBuffer sb) throws JRException
	{
		Iterator<String> fieldIt = null;
		if (fieldsMap != null && fieldsMap.size() > 0) 
		{
			fieldIt = fieldsMap.keySet().iterator();
		}
		else
		{
			Set<String> emptySet = Collections.emptySet();
			fieldIt = emptySet.iterator();
		}
		generateInitFieldsMethod(sb, fieldIt, 0);
	}


	protected final void generateInitVarsMethod(StringBuffer sb) throws JRException
	{
		Iterator<JRVariable> varIt = null;
		if (variables != null && variables.length > 0) 
		{
			varIt = Arrays.asList(variables).iterator();
		}
		else
		{
			List<JRVariable> emptyList = Collections.emptyList();
			varIt = emptyList.iterator();
		}
		generateInitVarsMethod(sb, varIt, 0);
	}		


	/**
	 *
	 */
	private void generateInitParamsMethod(StringBuffer sb, Iterator<String> it, int index) throws JRException
	{
		sb.append("    /**\n");
		sb.append("     *\n");
		sb.append("     */\n");
		sb.append("    void initParams");
		if(index > 0)
		{
			sb.append(index);
		}
		sb.append("(Map pm)\n");
		sb.append("    {\n");
		for (int i = 0; i < EXPR_MAX_COUNT_PER_METHOD && it.hasNext(); i++)
		{
			String parameterName = it.next();
			sb.append("        parameter_");
			sb.append(JRStringUtil.getJavaIdentifier(parameterName));
			sb.append(" = (JRFillParameter)pm.get(\"");
			sb.append(JRStringUtil.escapeJavaStringLiteral(parameterName));
			sb.append("\");\n");
		}
		if(it.hasNext())
		{
			sb.append("        initParams");
			sb.append(index + 1);
			sb.append("(pm);\n");
		}
		sb.append("    }\n");
		sb.append("\n");
		sb.append("\n");

		if(it.hasNext())
		{
			generateInitParamsMethod(sb, it, index + 1);
		}
	}		


	/**
	 *
	 */
	private void generateInitFieldsMethod(StringBuffer sb, Iterator<String> it, int index) throws JRException
	{
		sb.append("    /**\n");
		sb.append("     *\n");
		sb.append("     */\n");
		sb.append("    void initFields");
		if(index > 0)
		{
			sb.append(index);
		}
		sb.append("(Map fm)\n");
		sb.append("    {\n");
		for (int i = 0; i < EXPR_MAX_COUNT_PER_METHOD && it.hasNext(); i++)
		{
			String fieldName = it.next();
			sb.append("        field_");
			sb.append(JRStringUtil.getJavaIdentifier(fieldName));
			sb.append(" = (JRFillField)fm.get(\"");
			sb.append(JRStringUtil.escapeJavaStringLiteral(fieldName));
			sb.append("\");\n");
		}
		if(it.hasNext())
		{
			sb.append("        initFields");
			sb.append(index + 1);
			sb.append("(fm);\n");
		}
		sb.append("    }\n");
		sb.append("\n");
		sb.append("\n");

		if(it.hasNext())
		{
			generateInitFieldsMethod(sb, it, index + 1);
		}
	}		


	/**
	 *
	 */
	private void generateInitVarsMethod(StringBuffer sb, Iterator<JRVariable> it, int index) throws JRException
	{
		sb.append("    /**\n");
		sb.append("     *\n");
		sb.append("     */\n");
		sb.append("    void initVars");
		if(index > 0)
		{
			sb.append(index);
		}
		sb.append("(Map vm)\n");
		sb.append("    {\n");
		for (int i = 0; i < EXPR_MAX_COUNT_PER_METHOD && it.hasNext(); i++)
		{
			String variableName = it.next().getName();
			sb.append("        variable_");
			sb.append(JRStringUtil.getJavaIdentifier(variableName));
			sb.append(" = (JRFillVariable)vm.get(\"");
			sb.append(JRStringUtil.escapeJavaStringLiteral(variableName));
			sb.append("\");\n");
		}
		if(it.hasNext())
		{
			sb.append("        initVars");
			sb.append(index + 1);
			sb.append("(vm);\n");
		}
		sb.append("    }\n");
		sb.append("\n");
		sb.append("\n");

		if(it.hasNext())
		{
			generateInitVarsMethod(sb, it, index + 1);
		}
	}		


	/**
	 *  
	 */
	protected final String generateMethod(byte evaluationType, List<JRExpression> expressionsList) throws JRException 
	{
		StringBuffer sb = new StringBuffer();
		
		if (expressionsList != null && !expressionsList.isEmpty())
		{
			sb.append(generateMethod(expressionsList.iterator(), 0, evaluationType, expressionsList.size()));
		}
		else
		{
			/*   */
			sb.append("    /**\n");
			sb.append("     *\n");
			sb.append("     */\n");
			sb.append("    Object evaluate");
			sb.append(methodSuffixMap.get(new Byte(evaluationType)));
			sb.append("(int id)\n");
			sb.append("    {\n");
			sb.append("        return null;\n");
			sb.append("    }\n");
			sb.append("\n");
			sb.append("\n");
		}
		
		return sb.toString();
	}


	/**
	 * 
	 */
	private String generateMethod(Iterator<JRExpression> it, int index, byte evaluationType, int expressionCount) throws JRException 
	{
		StringBuffer sb = new StringBuffer();
		
		/*   */
		sb.append("    /**\n");
		sb.append("     *\n");
		sb.append("     */\n");
		sb.append("    Object evaluate");
		sb.append( methodSuffixMap.get(new Byte(evaluationType)));
		if (index > 0)
		{
			sb.append(index);
		}
		sb.append("(int id)\n");
		sb.append("    {\n");
		sb.append("        Object value = null;\n");
		sb.append("\n");
		
		//NB: relying on the fact that the expression id is the same as the index of the expression in the list
		int expressionIdLimit = (index + 1) * EXPR_MAX_COUNT_PER_METHOD;
		boolean nextMethod = expressionCount > expressionIdLimit;
		
		if (nextMethod)
		{
			sb.append("        if (id >= ");
			sb.append(expressionIdLimit);
			sb.append(")\n");
			sb.append("        {\n");
			sb.append("            value = evaluate");
			sb.append(methodSuffixMap.get(new Byte(evaluationType)));
			sb.append(index + 1);
			sb.append("(id);\n");
			sb.append("        }\n");
		}
		
		for (int i = 0; it.hasNext() && i < EXPR_MAX_COUNT_PER_METHOD; i++) 
		{
			JRExpression expression = it.next();
			
			sb.append("        ");
			if (i > 0 || nextMethod)
			{
				sb.append("else ");
			}
			sb.append("if (id == ");
			sb.append(sourceTask.getExpressionId(expression));
			sb.append(")\n");
			sb.append("        {\n");
			sb.append("            value = (");
			sb.append(this.generateExpression(expression, evaluationType));
			sb.append(");\n");
			sb.append("        }\n");
		}

		sb.append("\n");
		sb.append("        return value;\n");
		sb.append("    }\n");
		sb.append("\n");
		sb.append("\n");
		
		if (nextMethod)
		{
			sb.append(generateMethod(it, index + 1, evaluationType, expressionCount));
		}
		
		return sb.toString();
	}


	/**
	 *
	 */
	private String generateExpression(
		JRExpression expression,
		byte evaluationType
		)
	{
		StringBuffer sb = new StringBuffer();

		JRExpressionChunk[] chunks = expression.getChunks();
		if (chunks != null && chunks.length > 0)
		{
			for(int i = 0; i < chunks.length; i++)
			{
				JRExpressionChunk chunk = chunks[i];

				String chunkText = chunk.getText();
				if (chunkText == null)
				{
					chunkText = "";
				}
				
				switch (chunk.getType())
				{
					case JRExpressionChunk.TYPE_TEXT :
					{
						sb.append(chunkText);
						break;
					}
					case JRExpressionChunk.TYPE_PARAMETER :
					{
						JRParameter jrParameter = parametersMap.get(chunkText);
	
						sb.append("(");
						if (!"java.lang.Object".equals(jrParameter.getValueClassName()))
						{
							sb.append("(");
							sb.append(jrParameter.getValueClassName());
							sb.append(")");
						}
						sb.append("parameter_");
						sb.append(JRStringUtil.getJavaIdentifier(chunkText));
						sb.append(".getValue())");
	
						break;
					}
					case JRExpressionChunk.TYPE_FIELD :
					{
						JRField jrField = fieldsMap.get(chunkText);

						sb.append("(");
						if (!"java.lang.Object".equals(jrField.getValueClassName()))
						{
							sb.append("(");
							sb.append(jrField.getValueClassName());
							sb.append(")");
						}
						sb.append("field_");
						sb.append(JRStringUtil.getJavaIdentifier(chunkText)); 
						sb.append(".get");
						sb.append(fieldPrefixMap.get(new Byte(evaluationType))); 
						sb.append("Value())");
	
						break;
					}
					case JRExpressionChunk.TYPE_VARIABLE :
					{
						JRVariable jrVariable = variablesMap.get(chunkText);
	
						sb.append("(");
						if (!"java.lang.Object".equals(jrVariable.getValueClassName()))
						{
							sb.append("(");
							sb.append(jrVariable.getValueClassName());
							sb.append(")"); 
						}
						sb.append("variable_"); 
						sb.append(JRStringUtil.getJavaIdentifier(chunkText)); 
						sb.append(".get");
						sb.append(variablePrefixMap.get(new Byte(evaluationType))); 
						sb.append("Value())");
	
						break;
					}
					case JRExpressionChunk.TYPE_RESOURCE :
					{
						sb.append("str(\"");
						sb.append(chunkText);
						sb.append("\")");
	
						break;
					}
				}
			}
		}
		
		if (sb.length() == 0)
		{
			sb.append("null");
		}

		return sb.toString();
	}
}
