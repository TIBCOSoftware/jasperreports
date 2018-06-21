/*
 * JasperReports - Free Java Reporting Library.
 * Copyright (C) 2001 - 2018 TIBCO Software Inc. All rights reserved.
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
package com.update;

import java.io.ByteArrayInputStream;
import java.io.UnsupportedEncodingException;

import net.sf.jasperreports.engine.DefaultJasperReportsContext;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRRuntimeException;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.util.ReportUpdater;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.engine.xml.JRXmlWriter;


/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 */
public class RenewUuidsUpdater implements ReportUpdater 
{
	
	@Override
	public JasperDesign update(JasperDesign jasperDesign)
	{
		JRXmlWriter writer = new JRXmlWriter(DefaultJasperReportsContext.getInstance());
		writer.setExcludeUuids(true);
		String jrxml = writer.write(jasperDesign, "UTF-8");
		
		try
		{
			jasperDesign = JRXmlLoader.load(new ByteArrayInputStream(jrxml.getBytes("UTF-8")));
		}
		catch (UnsupportedEncodingException e)
		{
			throw new JRRuntimeException(e);
		}
		catch (JRException e)
		{
			throw new JRRuntimeException(e);
		}
		
		return jasperDesign;
	}
}
