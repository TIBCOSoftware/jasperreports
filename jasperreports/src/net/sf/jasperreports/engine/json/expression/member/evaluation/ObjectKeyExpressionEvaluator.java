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
package net.sf.jasperreports.engine.json.expression.member.evaluation;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import net.sf.jasperreports.engine.json.JRJsonNode;
import net.sf.jasperreports.engine.json.JsonNodeContainer;
import net.sf.jasperreports.engine.json.expression.EvaluationContext;
import net.sf.jasperreports.engine.json.expression.member.MemberExpression;
import net.sf.jasperreports.engine.json.expression.member.ObjectKeyExpression;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;

/**
 * @author Narcis Marcu (narcism@users.sourceforge.net)
 */
public class ObjectKeyExpressionEvaluator extends AbstractMemberExpressionEvaluator {
    private static final Log log = LogFactory.getLog(ObjectKeyExpressionEvaluator.class);

    private ObjectKeyExpression expression;
    private boolean keepMissingNode;


    public ObjectKeyExpressionEvaluator(EvaluationContext evaluationContext, ObjectKeyExpression expression) {
        this(evaluationContext, expression, false);
    }

    public ObjectKeyExpressionEvaluator(EvaluationContext evaluationContext,
                                        ObjectKeyExpression expression, boolean keepMissingNode) {
        super(evaluationContext);

        this.expression = expression;
        this.keepMissingNode = keepMissingNode;
    }

    @Override
    public JsonNodeContainer evaluate(JsonNodeContainer contextNode) {
        List<JRJsonNode> nodes = contextNode.getNodes();

        if (log.isDebugEnabled()) {
            log.debug("---> evaluating expression [" + expression +
                    "] on a node with (size: " + contextNode.getSize() +
                    ", cSize: " + contextNode.getContainerSize() + ")");
        }

        JsonNodeContainer result = new JsonNodeContainer();

        for (JRJsonNode node: nodes) {
            List<JRJsonNode> evaluatedNodes = singleEval(node);

            if (evaluatedNodes.size() > 0) {
                result.addNodes(evaluatedNodes);
            }
        }

        if (result.getSize() > 0) {
            return result;
        }

        return null;
    }

    @Override
    public MemberExpression getMemberExpression() {
        return expression;
    }

    private List<JRJsonNode> singleEval(JRJsonNode jrJsonNode) {
        if (log.isDebugEnabled()) {
            log.debug("going " + expression.getDirection() + " by " +
                    (expression.isWildcard() ?
                            "wildcard" :
                            "key: [" + expression.getObjectKey() + "]") + " on " + jrJsonNode.getDataNode());
        }

        switch (expression.getDirection()) {
            case DOWN:
                return goDown(jrJsonNode);
            case ANYWHERE_DOWN:
                return goAnywhereDown(jrJsonNode);
        }

        return null;
    }

    private List<JRJsonNode> goDown(JRJsonNode jrJsonNode) {
        List<JRJsonNode> result = new ArrayList<>();
        JsonNode dataNode = jrJsonNode.getDataNode();

        // advance into object
        if (dataNode.isObject()) {
            // if wildcard => filter and add all its children(the values for each key) to an arrayNode
            if (expression.isWildcard()) {
                ArrayNode container = getEvaluationContext().getObjectMapper().createArrayNode();
                Iterator<Map.Entry<String, JsonNode>> it = dataNode.fields();

                while (it.hasNext()) {
                    JsonNode current = it.next().getValue();

                    if (applyFilter(jrJsonNode.createChild(current))) {
                        container.add(current);
                    }
                }

                if (container.size() > 0) {
                    result.add(new JRJsonNode(jrJsonNode, container));
                }
            }
            // else go down and filter
            else {
                // allow missing nodes to be returned
                JsonNode asd = dataNode.path(expression.getObjectKey());

                // if the deeper node is object/value => filter and add it
                if (!asd.isMissingNode() && (asd.isObject() || asd.isValueNode())) {
                    if (applyFilter(jrJsonNode.createChild(asd))) {
                        result.add(new JRJsonNode(jrJsonNode, asd));
                    }
                }
                // Filtering expressions need the missing node to check for null
                else if (keepMissingNode && asd.isMissingNode()) {
                    result.add(new JRJsonNode(jrJsonNode, asd));
                }
                // if the deeper node is an array => filter and add each child
                else if (!asd.isMissingNode() && asd.isArray()) {
                    result = filterArrayNode(jrJsonNode, (ArrayNode) asd);
                }
            }
        }
        // advance into array
        else if (dataNode.isArray()) {
            if (expression.isWildcard()) {
                result = filterArrayNode(jrJsonNode, (ArrayNode) dataNode);
            } else {
                result = filterArrayNode(jrJsonNode, (ArrayNode) dataNode, expression.getObjectKey());
            }
        }
        // advance into a value node
        else if (dataNode.isValueNode()) {
            if (expression.isWildcard()) {
                if (applyFilter(jrJsonNode)) {
                    result.add(jrJsonNode);
                }
            }
        }

        return result;
    }

    private List<JRJsonNode> goAnywhereDown(JRJsonNode jrJsonNode) {
        List<JRJsonNode> result = new ArrayList<>();
        Deque<JRJsonNode> stack = new ArrayDeque<>();

        stack.push(jrJsonNode);

        while (!stack.isEmpty()) {
            JRJsonNode parent = stack.pop();
            JsonNode dataNode = parent.getDataNode();

            // if object => push the keys into the stack
            if (dataNode.isObject()) {
                Iterator<Map.Entry<String, JsonNode>> it = dataNode.fields();

                while (it.hasNext()) {
                    JsonNode current = it.next().getValue();
                    stack.addLast(parent.createChild(current));
                }
            }
            // if array => push all children
            else if (dataNode.isArray()) {
                for (JsonNode deeper: dataNode) {
                    stack.addLast(parent.createChild(deeper));
                }
            }

            List<JRJsonNode> partialResults = goDown(parent);

            if (partialResults.size() > 0) {
                result.addAll(partialResults);
            }
        }

        return result;
    }

}
