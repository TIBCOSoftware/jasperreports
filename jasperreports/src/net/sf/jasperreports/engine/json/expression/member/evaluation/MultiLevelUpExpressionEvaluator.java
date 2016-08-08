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
import net.sf.jasperreports.engine.json.expression.member.MultiLevelUpExpression;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @author Narcis Marcu (narcism@users.sourceforge.net)
 */
public class MultiLevelUpExpressionEvaluator implements MemberExpressionEvaluator {
    private static final Log log = LogFactory.getLog(MultiLevelUpExpressionEvaluator.class);

    private MultiLevelUpExpression expression;


    public MultiLevelUpExpressionEvaluator(MultiLevelUpExpression expression) {
        this.expression = expression;
    }

    @Override
    public JsonNodeContainer evaluate(JsonNodeContainer contextNode) {
        int level = expression.getLevel();

        if (log.isDebugEnabled()) {
            log.debug("going up by " + level + " level(s)");
        }

        if (level >= 1) {
            JRJsonNode result = contextNode.getFirst();

            for(int i=0; i < level; i++) {
                result = result.getParent();

                if (result == null) {
                    return null;
                }
            }

            return new JsonNodeContainer(result);
        }

        return null;
    }

}
