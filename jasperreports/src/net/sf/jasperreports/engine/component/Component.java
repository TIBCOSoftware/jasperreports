/*
 * JasperReports - Free Java Reporting Library.
 * Copyright (C) 2001 - 2014 TIBCO Software Inc. All rights reserved.
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
package net.sf.jasperreports.engine.component;

import net.sf.jasperreports.engine.JRCloneable;
import net.sf.jasperreports.engine.JRComponentElement;

/**
 * A marker interface that is to be implemented by classes that can be used
 * as report components.
 * 
 * <p>
 * Implementing classes should consider implementing {@link JRCloneable} as well
 * in order to have component clones created when components elements are cloned.
 * 
 * @author Lucian Chirita (lucianc@users.sourceforge.net)
 * @see JRComponentElement
 */
public interface Component
{
	
}
