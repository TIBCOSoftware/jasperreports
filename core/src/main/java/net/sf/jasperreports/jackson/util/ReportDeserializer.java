/*
 * JasperReports - Free Java Reporting Library.
 * Copyright (C) 2001 - 2023 Cloud Software Group, Inc. All rights reserved.
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
package net.sf.jasperreports.jackson.util;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

import net.sf.jasperreports.engine.JRDefaultStyleProvider;
import net.sf.jasperreports.engine.JRReport;
import net.sf.jasperreports.engine.JasperReportsContext;
import net.sf.jasperreports.engine.design.JasperDesign;


/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 */
public class ReportDeserializer extends StdDeserializer<JRReport>
{
	private static final long serialVersionUID = 1L;
	
	private JasperReportsContext jasperReportsContext;
	private static final ThreadLocal<JRDefaultStyleProvider> defaultStyleProvider = new ThreadLocal<JRDefaultStyleProvider>();

	public ReportDeserializer(JasperReportsContext jasperReportsContext)
	{
		this((Class<?>)null);
		
		this.jasperReportsContext = jasperReportsContext;
	}
	
	public ReportDeserializer(Class<?> vc)
	{
		super(vc);
	}

	@Override
	public JRReport deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException 
	{
		JasperDesign jasperDesign = new JasperDesign(jasperReportsContext);
		defaultStyleProvider.set(jasperDesign);
		return jasperDesign;
    }
	
	public static JRDefaultStyleProvider getDefaultStyleProvider()
	{
		return defaultStyleProvider.get();
	}
}
