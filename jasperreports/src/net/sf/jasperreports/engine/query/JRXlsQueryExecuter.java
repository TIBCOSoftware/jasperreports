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
package net.sf.jasperreports.engine.query;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import jxl.Workbook;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRDataset;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRValueParameter;
import net.sf.jasperreports.engine.JasperReportsContext;

/**
 * XLS query executer implementation.
 * 
 * @deprecated Replaced by {@link XlsQueryExecuter}.
 * @author Narcis Marcu (narcism@users.sourceforge.net)
 */
public class JRXlsQueryExecuter extends AbstractXlsQueryExecuter {
	
	private static final Log log = LogFactory.getLog(JRXlsQueryExecuter.class);
	
	/**
	 * 
	 */
	protected JRXlsQueryExecuter(
		JasperReportsContext jasperReportsContext, 
		JRDataset dataset, 
		Map<String,? extends JRValueParameter> parametersMap
		) 
	{
		super(jasperReportsContext, dataset, parametersMap);
	}

	@Override
	public JRDataSource createDatasource() throws JRException {
		net.sf.jasperreports.engine.data.JRXlsDataSource datasource = null;
		
		try {
			Workbook workbook = (Workbook) getParameterValue(AbstractXlsQueryExecuterFactory.XLS_WORKBOOK);
			if (workbook != null) {
				datasource = new net.sf.jasperreports.engine.data.JRXlsDataSource(workbook);
			} else {
				InputStream xlsInputStream = (InputStream) getParameterValue(AbstractXlsQueryExecuterFactory.XLS_INPUT_STREAM);
				if (xlsInputStream != null) {
					datasource = new net.sf.jasperreports.engine.data.JRXlsDataSource(xlsInputStream);
				} else {
					File xlsFile = (File) getParameterValue(AbstractXlsQueryExecuterFactory.XLS_FILE);
					if (xlsFile != null) {
						datasource = new net.sf.jasperreports.engine.data.JRXlsDataSource(xlsFile);
					} else {
						String xlsSource = getStringParameterOrProperty(AbstractXlsQueryExecuterFactory.XLS_SOURCE);
						if (xlsSource != null) {
							datasource = new net.sf.jasperreports.engine.data.JRXlsDataSource(getJasperReportsContext(), xlsSource);
						} else {
							if (log.isWarnEnabled()){
								log.warn("No XLS source was provided.");
							}
						}
					}
				}
			}
		} catch (IOException e) {
			throw new JRException(e);
		}
		
		if (datasource != null) {
			initDatasource(datasource);
		}
		
		return datasource;
	}
	
}
