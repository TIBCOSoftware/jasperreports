/*
 * JasperReports - Free Java Reporting Library.
 * Copyright (C) 2001 - 2013 Jaspersoft Corporation. All rights reserved.
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
package net.sf.jasperreports.data.xmla;

import java.util.Map;

import net.sf.jasperreports.data.AbstractDataAdapterService;
import net.sf.jasperreports.engine.DefaultJasperReportsContext;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperReportsContext;
import net.sf.jasperreports.olap.xmla.JRXmlaQueryExecuterFactory;
import net.sf.jasperreports.util.SecretsUtil;

/**
 * @author Veaceslov Chicu (schicu@users.sourceforge.net)
 * @version $Id$
 */
public class XmlaDataAdapterService extends AbstractDataAdapterService {

	/**
	 * 
	 */
	public XmlaDataAdapterService(JasperReportsContext jasperReportsContext, XmlaDataAdapter jsonDataAdapter) {
		super(jasperReportsContext, jsonDataAdapter);
	}

	/**
	 * @deprecated Replaced by
	 *             {@link #XmlaDataAdapterService(JasperReportsContext, XmlaDataAdapter)}
	 *             .
	 */
	public XmlaDataAdapterService(XmlaDataAdapter jsonDataAdapter) {
		this(DefaultJasperReportsContext.getInstance(), jsonDataAdapter);
	}

	public XmlaDataAdapter getHibernateDataAdapter() {
		return (XmlaDataAdapter) getDataAdapter();
	}

	@Override
	public void contributeParameters(Map<String, Object> parameters) throws JRException {
		XmlaDataAdapter hbmDA = getHibernateDataAdapter();
		if (hbmDA != null) {
			parameters.put(JRXmlaQueryExecuterFactory.PARAMETER_XMLA_URL, hbmDA.getXmlaUrl());
			parameters.put(JRXmlaQueryExecuterFactory.PARAMETER_XMLA_DATASOURCE, hbmDA.getDatasource());
			parameters.put(JRXmlaQueryExecuterFactory.PARAMETER_XMLA_CATALOG, hbmDA.getCatalog());

			String username = hbmDA.getUsername();
			if (username != null && !username.isEmpty())
				parameters.put(JRXmlaQueryExecuterFactory.PARAMETER_XMLA_USER, username);

			String password = hbmDA.getPassword();
			SecretsUtil secretService = SecretsUtil.getInstance(getJasperReportsContext());
			if (secretService != null)
				password = secretService.getSecret(SECRETS_CATEGORY, password);
			if (password != null && !password.isEmpty())
				parameters.put(JRXmlaQueryExecuterFactory.PARAMETER_XMLA_PASSWORD, password);
		}
	}

}
