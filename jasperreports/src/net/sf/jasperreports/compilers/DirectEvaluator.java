/*
 * JasperReports - Free Java Reporting Library.
 * Copyright (C) 2001 - 2019 TIBCO Software Inc. All rights reserved.
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
package net.sf.jasperreports.compilers;

import java.util.Map;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.fill.JREvaluator;
import net.sf.jasperreports.engine.fill.JRFillField;
import net.sf.jasperreports.engine.fill.JRFillParameter;
import net.sf.jasperreports.engine.fill.JRFillVariable;

/**
 * @author Lucian Chirita (lucianc@users.sourceforge.net)
 */
public class DirectEvaluator extends JREvaluator
{

	@Override
	protected void customizedInit(Map<String, JRFillParameter> parametersMap, Map<String, JRFillField> fieldsMap,
			Map<String, JRFillVariable> variablesMap) throws JRException
	{
		//NOOP
	}

	@Override
	protected Object evaluate(int id) throws Throwable
	{
		throw new UnsupportedOperationException();
	}

	@Override
	protected Object evaluateOld(int id) throws Throwable
	{
		throw new UnsupportedOperationException();
	}

	@Override
	protected Object evaluateEstimated(int id) throws Throwable
	{
		throw new UnsupportedOperationException();
	}

}
