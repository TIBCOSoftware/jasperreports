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
package net.sf.jasperreports.engine.design;

import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExpression;
import net.sf.jasperreports.engine.JRExpressionChunk;
import net.sf.jasperreports.engine.JRField;
import net.sf.jasperreports.engine.JRParameter;
import net.sf.jasperreports.engine.JRVariable;


/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id$
 */
public class JRClassGenerator
{
	
	
	/**
	 *
	 */
	private JasperDesign jasperDesign = null;

	private static Map fieldPrefixMap = null;
	private static Map variablePrefixMap = null;
	private static Map methodSuffixMap = null;


	/**
	 *
	 */
	protected JRClassGenerator(JasperDesign jrDesign)
	{
		jasperDesign = jrDesign;

		fieldPrefixMap = new HashMap();
		fieldPrefixMap.put(new Byte(JRExpression.EVALUATION_OLD),       "Old");
		fieldPrefixMap.put(new Byte(JRExpression.EVALUATION_ESTIMATED), "");
		fieldPrefixMap.put(new Byte(JRExpression.EVALUATION_DEFAULT),   "");
		
		variablePrefixMap = new HashMap();
		variablePrefixMap.put(new Byte(JRExpression.EVALUATION_OLD),       "Old");
		variablePrefixMap.put(new Byte(JRExpression.EVALUATION_ESTIMATED), "Estimated");
		variablePrefixMap.put(new Byte(JRExpression.EVALUATION_DEFAULT),   "");
		
		methodSuffixMap = new HashMap();
		methodSuffixMap.put(new Byte(JRExpression.EVALUATION_OLD),       "Old");
		methodSuffixMap.put(new Byte(JRExpression.EVALUATION_ESTIMATED), "Estimated");
		methodSuffixMap.put(new Byte(JRExpression.EVALUATION_DEFAULT),   "");
	}


	/**
	 *
	 */
	public static String generateClass(JasperDesign jrDesign) throws JRException
	{
		JRClassGenerator generator = new JRClassGenerator(jrDesign);
		return generator.generateClass();
	}


	/**
	 *
	 */
	protected String generateClass() throws JRException
	{
		StringBuffer sb = new StringBuffer();

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
		String[] imports = jasperDesign.getImports();
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
		sb.append("public class ");
		sb.append(jasperDesign.getName());
		sb.append(" extends JRCalculator\n");
		sb.append("{\n"); 
		sb.append("\n");
		sb.append("\n");
		sb.append("    /**\n");
		sb.append("     *\n");
		sb.append("     */\n");

		/*   */
		Map parametersMap = jasperDesign.getParametersMap();
		if (parametersMap != null && parametersMap.size() > 0)
		{
			Collection parameterNames = parametersMap.keySet();
			for (Iterator it = parameterNames.iterator(); it.hasNext();)
			{
				sb.append("    private JRFillParameter parameter_");
				sb.append(it.next());
				sb.append(" = null;\n");
			}
		}
		
		/*   */
		sb.append("\n");

		/*   */
		Map fieldsMap = jasperDesign.getFieldsMap();
		if (fieldsMap != null && fieldsMap.size() > 0)
		{
			Collection fieldNames = fieldsMap.keySet();
			for (Iterator it = fieldNames.iterator(); it.hasNext();)
			{
				sb.append("    private JRFillField field_");
				sb.append(it.next());
				sb.append(" = null;\n");
			}
		}
		
		/*   */
		sb.append("\n");

		/*   */
		JRVariable[] variables = jasperDesign.getVariables();
		if (variables != null && variables.length > 0)
		{
			for (int i = 0; i < variables.length; i++)
			{
				sb.append("    private JRFillVariable variable_");
				sb.append(variables[i].getName());
				sb.append(" = null;\n");
			}
		}

		/*   */
		sb.append("\n");
		sb.append("\n");
		sb.append("    /**\n");
		sb.append("     *\n");
		sb.append("     */\n");
		sb.append("    public void customizedInit(\n"); 
		sb.append("        Map pm,\n");
		sb.append("        Map fm,\n"); 
		sb.append("        Map vm\n");
		sb.append("        ) throws JRException\n");
		sb.append("    {\n");

		/*   */
		parametersMap = jasperDesign.getParametersMap();
		if (parametersMap != null && parametersMap.size() > 0)
		{
			Collection parameterNames = parametersMap.keySet();
			String parameterName = null;
			for (Iterator it = parameterNames.iterator(); it.hasNext();)
			{
				parameterName = (String)it.next();
				sb.append("        parameter_");
				sb.append(parameterName);
				sb.append(" = (JRFillParameter)parsm.get(\"");
				sb.append(parameterName);
				sb.append("\");\n");
			}
		}
		
		/*   */
		sb.append("\n");

		/*   */
		fieldsMap = jasperDesign.getFieldsMap();
		if (fieldsMap != null && fieldsMap.size() > 0)
		{
			Collection fieldNames = fieldsMap.keySet();
			String fieldName = null;
			for (Iterator it = fieldNames.iterator(); it.hasNext();)
			{
				fieldName = (String)it.next();
				sb.append("        field_");
				sb.append(fieldName);
				sb.append(" = (JRFillField)fldsm.get(\"");
				sb.append(fieldName);
				sb.append("\");\n");
			}
		}
		
		/*   */
		sb.append("\n");

		/*   */
		variables = jasperDesign.getVariables();
		if (variables != null && variables.length > 0)
		{
			String variableName = null;
			for (int i = 0; i < variables.length; i++)
			{
				variableName = variables[i].getName();
				sb.append("        variable_");
				sb.append(variableName);
				sb.append(" = (JRFillVariable)varsm.get(\"");
				sb.append(variableName);
				sb.append("\");\n");
			}
		}

		/*   */
		sb.append("    }\n");
		sb.append("\n");
		sb.append("\n");

		sb.append(this.generateMethod(JRExpression.EVALUATION_DEFAULT));
		sb.append(this.generateMethod(JRExpression.EVALUATION_OLD));
		sb.append(this.generateMethod(JRExpression.EVALUATION_ESTIMATED));

		sb.append("}\n");

		return sb.toString();
	}		


	/**
	 *
	 */
	private String generateMethod(byte evaluationType) throws JRException
	{
		StringBuffer sb = new StringBuffer();

		/*   */
		sb.append("    /**\n");
		sb.append("     *\n");
		sb.append("     */\n");
		sb.append("    public Object evaluate");
		sb.append((String)methodSuffixMap.get(new Byte(evaluationType)));
		sb.append("(int id) throws Throwable\n");
		sb.append("    {\n");
		sb.append("        Object value = null;\n");
		sb.append("\n");
		sb.append("        switch (id)\n");
		sb.append("        {\n");

		Collection expressions = jasperDesign.getExpressions();
		if (expressions != null && expressions.size() > 0)
		{
			JRExpression expression = null;
			for (Iterator it = expressions.iterator(); it.hasNext();)
			{
				expression = (JRExpression)it.next();
				
				sb.append("            case "); 
				sb.append(expression.getId()); 
				sb.append(" : // ");
				sb.append(expression.getName()); 
				sb.append("\n");
				sb.append("            {\n");
				sb.append("                value = (");
				sb.append(expression.getValueClassName());
				sb.append(")(");
				sb.append(this.generateExpression(expression, evaluationType));
				sb.append(");\n");
				sb.append("                break;\n");
				sb.append("            }\n");
			}
		}

		/*   */
		sb.append("           default :\n");
		sb.append("           {\n");
		sb.append("           }\n");
		sb.append("        }\n");
		sb.append("        \n");
		sb.append("        return value;\n");
		sb.append("    }\n");
		sb.append("\n");
		sb.append("\n");
		
		return sb.toString();
	}


	/**
	 *
	 */
	private String generateExpression(
		JRExpression expression,
		byte evaluationType
		) throws JRException
	{
		Map parametersMap = jasperDesign.getParametersMap();
		Map fieldsMap = jasperDesign.getFieldsMap();
		Map variablesMap = jasperDesign.getVariablesMap();

		JRParameter jrParameter = null;
		JRField jrField = null;
		JRVariable jrVariable = null;

		StringBuffer sb = new StringBuffer();

		JRExpressionChunk[] chunks = expression.getChunks();
		JRExpressionChunk chunk = null;
		String chunkText = null;
		if (chunks != null && chunks.length > 0)
		{
			for(int i = 0; i < chunks.length; i++)
			{
				chunk = chunks[i];

				chunkText = chunk.getText();
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
						jrParameter = (JRParameter)parametersMap.get(chunkText);
	
						sb.append("((");
						sb.append(jrParameter.getValueClassName());
						sb.append(")parameter_");
						sb.append(chunkText);
						sb.append(".getValue())");
	
						break;
					}
					case JRExpressionChunk.TYPE_FIELD :
					{
						jrField = (JRField)fieldsMap.get(chunkText);
	
						sb.append("((");
						sb.append(jrField.getValueClassName());
						sb.append(")field_");
						sb.append(chunkText); 
						sb.append(".get");
						sb.append((String)fieldPrefixMap.get(new Byte(evaluationType))); 
						sb.append("Value())");
	
						break;
					}
					case JRExpressionChunk.TYPE_VARIABLE :
					{
						jrVariable = (JRVariable)variablesMap.get(chunkText);
	
						sb.append("((");
						sb.append(jrVariable.getValueClassName());
						sb.append(")variable_"); 
						sb.append(chunkText); 
						sb.append(".get");
						sb.append((String)variablePrefixMap.get(new Byte(evaluationType))); 
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
