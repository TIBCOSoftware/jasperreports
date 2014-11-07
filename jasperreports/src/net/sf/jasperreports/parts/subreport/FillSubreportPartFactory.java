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
package net.sf.jasperreports.parts.subreport;

import net.sf.jasperreports.components.list.BaseFillList;
import net.sf.jasperreports.engine.fill.JRFillCloneFactory;
import net.sf.jasperreports.engine.fill.JRFillObjectFactory;
import net.sf.jasperreports.engine.part.PartComponent;
import net.sf.jasperreports.engine.part.PartComponentFillFactory;
import net.sf.jasperreports.engine.part.PartFillComponent;

/**
 * Factory of {@link BaseFillList list fill component} instances.
 * 
 * @author Lucian Chirita (lucianc@users.sourceforge.net)
 * @version $Id: FillListFactory.java 5877 2013-01-07 19:51:14Z teodord $
 */
public class FillSubreportPartFactory implements PartComponentFillFactory
{

	public PartFillComponent cloneFillComponent(PartFillComponent component,
			JRFillCloneFactory factory)
	{
		//TODO implement
		throw new UnsupportedOperationException();
	}

	public PartFillComponent toFillComponent(PartComponent component,
			JRFillObjectFactory factory)
	{
		SubreportPartComponent subreportPart = (SubreportPartComponent) component;
		SubreportFillPart fillSubreport = new SubreportFillPart(subreportPart, factory);
		return fillSubreport;
	}

}
