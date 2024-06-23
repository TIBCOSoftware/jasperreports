/*
 * Copyright (C) 2005 - 2023 Cloud Software Group, Inc. All rights reserved.
 * http://www.jaspersoft.com
 * Licensed under commercial Jaspersoft Subscription License Agreement
 */
package net.sf.jasperreports.hibernate;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.query.HibernateConstants;
import net.sf.jasperreports.engine.query.JRQueryExecuterFactoryBundle;
import net.sf.jasperreports.engine.query.QueryExecuterFactory;
import net.sf.jasperreports.engine.util.JRSingletonCache;

/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 */
public class HibernateQueryExecuterFactoryBundle implements JRQueryExecuterFactoryBundle {
	private static final JRSingletonCache<QueryExecuterFactory> cache = new JRSingletonCache<QueryExecuterFactory>(
			QueryExecuterFactory.class);
	private static final HibernateQueryExecuterFactoryBundle INSTANCE = new HibernateQueryExecuterFactoryBundle();
	private static final String[] LANGUAGES = new String[] { HibernateConstants.QUERY_LANGUAGE_HQL, HibernateConstants.QUERY_LANGUAGE_HQL.toUpperCase() };

	private HibernateQueryExecuterFactoryBundle() {
	}

	/**
	 * 
	 */
	public static HibernateQueryExecuterFactoryBundle getInstance() {
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
				return cache.getCachedInstance(JRHibernateQueryExecuterFactory.class.getName());
		}
		return null;
	}

	public static boolean isDomainLanguage(String lang) {
		String[] langs = HibernateQueryExecuterFactoryBundle.getInstance().getLanguages();
		for (String l : langs)
			if (l.equals(lang))
				return true;
		return false;
	}
}
