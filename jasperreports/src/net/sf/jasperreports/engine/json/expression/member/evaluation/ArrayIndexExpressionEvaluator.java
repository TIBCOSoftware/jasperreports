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

import net.sf.jasperreports.engine.json.JRJsonNode;
import net.sf.jasperreports.engine.json.JsonNodeContainer;
import net.sf.jasperreports.engine.json.expression.EvaluationContext;
import net.sf.jasperreports.engine.json.expression.member.ArrayIndexExpression;
import net.sf.jasperreports.engine.json.expression.member.MemberExpression;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @author Narcis Marcu (narcism@users.sourceforge.net)
 */
public class ArrayIndexExpressionEvaluator extends AbstractMemberExpressionEvaluator {
    private static final Log log = LogFactory.getLog(ArrayIndexExpressionEvaluator.class);

    private ArrayIndexExpression expression;

    public ArrayIndexExpressionEvaluator(EvaluationContext evaluationContext, ArrayIndexExpression expression) {
        super(evaluationContext);
        this.expression = expression;
    }

    @Override
    public JsonNodeContainer evaluate(JsonNodeContainer contextNode) {
        JsonNodeContainer result = new JsonNodeContainer();

        switch(expression.getDirection()) {
            case DOWN:
            case ANYWHERE_DOWN:
                // this only make sense for containers with appropriate size
                if (contextNode.isContainer() && expression.getIndex() < contextNode.getContainerSize()) {
                    JRJsonNode nodeAtIndex;

                    // look for the node inside the container directly
                    if (contextNode.getSize() > 1) {
                        nodeAtIndex =  contextNode.getNodes().get(expression.getIndex());
                    }
                    // look for the node inside the first element which is supposed to be an ArrayNode
                    else {
                        JRJsonNode parentNode = contextNode.getFirst();
                        nodeAtIndex =  new JRJsonNode(parentNode, parentNode.getDataNode().get(expression.getIndex()));
                    }

                    if (applyFilter(nodeAtIndex)) {
                        result.add(nodeAtIndex);
                    }
                }
                break;
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
}
