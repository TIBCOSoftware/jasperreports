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
package net.sf.jasperreports.view;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import net.sf.jasperreports.engine.JasperReportsContext;
import net.sf.jasperreports.engine.util.JRClassLoader;

/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 */
public class SaveContributorUtils
{

	private static final Log log = LogFactory.getLog(SaveContributorUtils.class);
	
	private static final String[] DEFAULT_CONTRIBUTORS = {
		"net.sf.jasperreports.view.save.JRPrintSaveContributor",
		"net.sf.jasperreports.view.save.JRPdfSaveContributor",
		"net.sf.jasperreports.view.save.JRRtfSaveContributor",
		"net.sf.jasperreports.view.save.JROdtSaveContributor",
		"net.sf.jasperreports.view.save.JRDocxSaveContributor",
		"net.sf.jasperreports.view.save.JRHtmlSaveContributor",
		"net.sf.jasperreports.view.save.JRSingleSheetXlsSaveContributor",
		"net.sf.jasperreports.view.save.JRMultipleSheetsXlsSaveContributor",
		"net.sf.jasperreports.view.save.JRCsvSaveContributor",
		"net.sf.jasperreports.view.save.JRXmlSaveContributor",
		"net.sf.jasperreports.view.save.JREmbeddedImagesXmlSaveContributor"
	};
	
	private static final Class<?>[] CONSTRUCTOR_SIGNATURE = {
		JasperReportsContext.class, Locale.class, ResourceBundle.class};
	
	public static List<JRSaveContributor> createBuiltinContributors(JasperReportsContext context,
			Locale locale, ResourceBundle resourceBundle)
	{
		ArrayList<JRSaveContributor> contributors = new ArrayList<JRSaveContributor>(DEFAULT_CONTRIBUTORS.length);
		for (String contributorClassName : DEFAULT_CONTRIBUTORS)
		{
			try
			{
				Class<?> saveContribClass = JRClassLoader.loadClassForName(contributorClassName);
				Constructor<?> constructor = saveContribClass.getConstructor(CONSTRUCTOR_SIGNATURE);
				JRSaveContributor saveContrib = (JRSaveContributor) constructor.newInstance(context, locale, resourceBundle);
				contributors.add(saveContrib);
			}
			catch (Exception e)
			{
				// shouldn't happen, but log anyway
				log.warn("Error creating save contributor of type " + contributorClassName, e);
			}
		}
		return contributors;
	}
	
}
