/*
 * Copyright (C) 2005 - 2023 Cloud Software Group, Inc. All rights reserved.
 * http://www.jaspersoft.com
 * Licensed under commercial Jaspersoft Subscription License Agreement
 */
package net.sf.jasperreports.poi.query;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.query.JRQueryExecuterFactoryBundle;
import net.sf.jasperreports.engine.query.QueryExecuterFactory;
import net.sf.jasperreports.engine.util.JRSingletonCache;

/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 */
public class PoiQueryExecuterFactoryBundle implements JRQueryExecuterFactoryBundle {
	private static final JRSingletonCache<QueryExecuterFactory> cache = new JRSingletonCache<QueryExecuterFactory>(
			QueryExecuterFactory.class);
	private static final PoiQueryExecuterFactoryBundle INSTANCE = new PoiQueryExecuterFactoryBundle();
	private static final String[] LANGUAGES = new String[] { "xls", "XLS", "xlsx", "XLSX" };

	private PoiQueryExecuterFactoryBundle() {
	}

	/**
	 * 
	 */
	public static PoiQueryExecuterFactoryBundle getInstance() {
		return INSTANCE;
	}

	@Override
	public String[] getLanguages() {
		return LANGUAGES;
	}

	@Override
	public QueryExecuterFactory getQueryExecuterFactory(String language) throws JRException {
		for (String lang : getLanguages()) {
			if (lang.equalsIgnoreCase(language))
				return cache.getCachedInstance(ExcelQueryExecuterFactory.class.getName());
		}
		return null;
	}
}
