/*
 * Copyright (C) 2005 - 2023 Cloud Software Group, Inc. All rights reserved.
 * http://www.jaspersoft.com
 * Licensed under commercial Jaspersoft Subscription License Agreement
 */
package net.sf.jasperreports.jakarta.ejbql;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.query.EjbqlConstants;
import net.sf.jasperreports.engine.query.JRQueryExecuterFactoryBundle;
import net.sf.jasperreports.engine.query.QueryExecuterFactory;
import net.sf.jasperreports.engine.util.JRSingletonCache;

/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 */
public class EjbqlQueryExecuterFactoryBundle implements JRQueryExecuterFactoryBundle {
	private static final JRSingletonCache<QueryExecuterFactory> cache = new JRSingletonCache<QueryExecuterFactory>(
			QueryExecuterFactory.class);
	private static final EjbqlQueryExecuterFactoryBundle INSTANCE = new EjbqlQueryExecuterFactoryBundle();
	private static final String[] LANGUAGES = new String[] { EjbqlConstants.QUERY_LANGUAGE_EJBQL, EjbqlConstants.QUERY_LANGUAGE_EJBQL.toUpperCase() };

	private EjbqlQueryExecuterFactoryBundle() {
	}

	/**
	 * 
	 */
	public static EjbqlQueryExecuterFactoryBundle getInstance() {
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
				return cache.getCachedInstance(JRJpaQueryExecuterFactory.class.getName());
		}
		return null;
	}

	public static boolean isDomainLanguage(String lang) {
		String[] langs = EjbqlQueryExecuterFactoryBundle.getInstance().getLanguages();
		for (String l : langs)
			if (l.equals(lang))
				return true;
		return false;
	}
}
