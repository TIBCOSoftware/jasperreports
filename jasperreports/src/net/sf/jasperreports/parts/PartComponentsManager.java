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
package net.sf.jasperreports.parts;

import net.sf.jasperreports.engine.JasperReportsContext;
import net.sf.jasperreports.engine.part.DefaultPartComponentManager;
import net.sf.jasperreports.engine.part.PartComponentXmlWriter;

/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id: ComponentsManager.java 5877 2013-01-07 19:51:14Z teodord $
 */
public class PartComponentsManager extends DefaultPartComponentManager
{

	public PartComponentXmlWriter getComponentXmlWriter(JasperReportsContext jasperReportsContext)
	{
		return new PartComponentsXmlWriter(jasperReportsContext);
	}

}
