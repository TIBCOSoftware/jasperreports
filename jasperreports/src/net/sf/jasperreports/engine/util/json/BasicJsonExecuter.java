/*
 * JasperReports - Free Java Reporting Library.
 * Copyright (C) 2001 - 2016 TIBCO Software Inc. All rights reserved.
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
package net.sf.jasperreports.engine.util.json;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.json.JRJsonNode;
import net.sf.jasperreports.engine.util.JsonUtil;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;

/**
 * @author Narcis Marcu (narcism@users.sourceforge.net)
 */
public class BasicJsonExecuter implements JsonExecuter {

    public static final String EXCEPTION_MESSAGE_KEY_INVALID_EXPRESSION = "data.json.invalid.expression";
    public static final String EXCEPTION_MESSAGE_KEY_INVALID_ATTRIBUTE_SELECTION = "data.json.invalid.attribute.selection";

    private final String PROPERTY_SEPARATOR = ".";//FIXME static?

    private final String ARRAY_LEFT = "[";

    private final String ARRAY_RIGHT = "]";

    private final String ATTRIBUTE_LEFT = "(";

    private final String ATTRIBUTE_RIGHT = ")";

    private ObjectMapper mapper;

    public BasicJsonExecuter() {
        mapper = JsonUtil.createObjectMapper();
    }

    @Override
    public List<JRJsonNode> selectNodes(JRJsonNode contextNode, String expression) throws JRException {
        List<JRJsonNode> result = new ArrayList<>();

        JsonNode rootNode = getJsonData(contextNode.getDataNode(), expression);

        if (rootNode != null && rootNode.isObject()) {
            result.add(new JRJsonNode(null, rootNode));
        } else if (rootNode != null && rootNode.isArray()) {
            for (JsonNode node: rootNode) {
                result.add(new JRJsonNode(null, node));
            }
        }

        return result;
    }

    @Override
    public JRJsonNode selectObjectNode(JRJsonNode contextNode, String expression) throws JRException {
        return new JRJsonNode(null, getJsonData(contextNode.getDataNode(), expression));
    }

    /**
     * Extracts the JSON nodes based on the query expression
     *
     * @param rootNode
     * @param jsonExpression
     * @throws JRException
     */
    protected JsonNode getJsonData(JsonNode rootNode, String jsonExpression) throws JRException {
        if (jsonExpression == null || jsonExpression.length() == 0) {
            return rootNode;
        }
        JsonNode tempNode = rootNode;
        StringTokenizer tokenizer = new StringTokenizer(jsonExpression, PROPERTY_SEPARATOR);

        while(tokenizer.hasMoreTokens()) {
            String currentToken = tokenizer.nextToken();
            int currentTokenLength = currentToken.length();
            int indexOfLeftSquareBracket = currentToken.indexOf(ARRAY_LEFT);

            // got Left Square Bracket - LSB
            if (indexOfLeftSquareBracket != -1) {
                // a Right Square Bracket must be the last character in the current token
                if(currentToken.lastIndexOf(ARRAY_RIGHT) != (currentTokenLength-1)) {
                    throw
                            new JRException(
                                    EXCEPTION_MESSAGE_KEY_INVALID_EXPRESSION,
                                    new Object[]{jsonExpression, currentToken});
                }

                // LSB not first character
                if (indexOfLeftSquareBracket > 0) {
                    // extract nodes at property
                    String property = currentToken.substring(0, indexOfLeftSquareBracket);
                    tempNode = goDownPathWithAttribute(tempNode, property);
                }

                String arrayOperators = currentToken.substring(indexOfLeftSquareBracket);
                StringTokenizer arrayOpsTokenizer = new StringTokenizer(arrayOperators,ARRAY_RIGHT);
                while(arrayOpsTokenizer.hasMoreTokens()) {
                    if (!tempNode.isMissingNode() && tempNode.isArray()) {
                        String currentArrayOperator = arrayOpsTokenizer.nextToken();
                        tempNode = tempNode.path(Integer.parseInt(currentArrayOperator.substring(1)));
                    }
                }
            } else {
                tempNode = goDownPathWithAttribute(tempNode, currentToken);
            }
        }

        return tempNode;
    }


    /**
     * Extracts the JSON nodes that match the attribute expression
     *
     * @param rootNode
     * @param pathWithAttributeExpression : e.g. Orders(CustomerId == HILAA)
     * @throws JRException
     */
    protected JsonNode goDownPathWithAttribute(JsonNode rootNode, String pathWithAttributeExpression) throws JRException {
        // check if path has attribute selector
        int indexOfLeftRoundBracket = pathWithAttributeExpression.indexOf(ATTRIBUTE_LEFT);
        if (indexOfLeftRoundBracket != -1) {

            // a Right Round Bracket must be the last character in the current pathWithAttribute
            if(pathWithAttributeExpression.indexOf(ATTRIBUTE_RIGHT) != (pathWithAttributeExpression.length() - 1)) {
                throw
                        new JRException(
                                EXCEPTION_MESSAGE_KEY_INVALID_ATTRIBUTE_SELECTION,
                                new Object[]{pathWithAttributeExpression});
            }

            if(rootNode != null && !rootNode.isMissingNode()) {

                String path = pathWithAttributeExpression.substring(0, indexOfLeftRoundBracket);

                // an expression in a form like: attribute==value
                String attributeExpression = pathWithAttributeExpression.substring(indexOfLeftRoundBracket + 1, pathWithAttributeExpression.length() - 1);

                JsonNode result = null;
                if (rootNode.isObject()) {
                    // select only those nodes for which the attribute expression applies
                    if (!rootNode.path(path).isMissingNode()) {
                        if (rootNode.path(path).isObject()) {
                            if (isValidExpression(rootNode.path(path), attributeExpression)) {
                                result = rootNode.path(path);
                            }
                        } else if (rootNode.path(path).isArray()) {
                            result = mapper.createArrayNode();
                            for (JsonNode node: rootNode.path(path)) {
                                if (isValidExpression(node, attributeExpression)) {
                                    ((ArrayNode)result).add(node);
                                }
                            }
                        }
                    }
                } else if (rootNode.isArray()) {
                    result = mapper.createArrayNode();
                    for (JsonNode node: rootNode) {
                        JsonNode deeperNode = node.path(path);
                        if (!deeperNode.isMissingNode()) {
                            if (deeperNode.isArray()) {
                                for(JsonNode arrayNode: deeperNode) {
                                    if (isValidExpression(arrayNode, attributeExpression)) {
                                        ((ArrayNode)result).add(arrayNode);
                                    }
                                }
                            } else if (isValidExpression(deeperNode, attributeExpression)){
                                ((ArrayNode)result).add(deeperNode);
                            }
                        }
                    }
                }
                return result;
            }

        } else { // path has no attribute selectors
            return goDownPath(rootNode, pathWithAttributeExpression);
        }
        return rootNode;
    }


    /**
     * Extracts the JSON nodes under the simple path
     *
     * @param rootNode
     * @param simplePath - a simple field name, with no selection by attribute
     */
    protected JsonNode goDownPath(JsonNode rootNode, String simplePath) {
        if(rootNode != null && !rootNode.isMissingNode()) {
            JsonNode result = null;
            if (rootNode.isObject()) {
                result = rootNode.path(simplePath);
            } else if (rootNode.isArray()) {
                result = mapper.createArrayNode();
                for (JsonNode node: rootNode) {
                    JsonNode deeperNode = node.path(simplePath);
                    if (!deeperNode.isMissingNode()) {
                        if (deeperNode.isArray()) {
                            for(JsonNode arrayNode: deeperNode) {
                                ((ArrayNode)result).add(arrayNode);
                            }
                        } else {
                            ((ArrayNode)result).add(deeperNode);
                        }
                    }
                }
            }
            return result;
        }
        return rootNode;
    }


    /**
     * Validates an attribute expression on a JsonNode
     *
     * @param operand
     * @param attributeExpression
     * @throws JRException
     */
    protected boolean isValidExpression(JsonNode operand, String attributeExpression) throws JRException {
        return JsonUtil.evaluateJsonExpression(operand, attributeExpression);
    }
}
