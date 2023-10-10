/*
 * Copyright (C) 2005 - 2023 Cloud Software Group, Inc. All rights reserved.
 * http://www.jaspersoft.com
 * Licensed under commercial Jaspersoft Subscription License Agreement
 */
package net.sf.jasperreports.olap;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.query.JRQueryExecuterFactoryBundle;
import net.sf.jasperreports.engine.query.QueryExecuterFactory;
import net.sf.jasperreports.engine.util.JRSingletonCache;

/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 */
public class OlapQueryExecuterFactoryBundle implements JRQueryExecuterFactoryBundle {
	private static final JRSingletonCache<QueryExecuterFactory> cache = new JRSingletonCache<QueryExecuterFactory>(
			QueryExecuterFactory.class);
	private static final OlapQueryExecuterFactoryBundle INSTANCE = new OlapQueryExecuterFactoryBundle();
	private static final String[] LANGUAGES = new String[] { "mdx", "MDX", "olap4j", "OLAP4J" };

	private OlapQueryExecuterFactoryBundle() {
	}

	/**
	 * 
	 */
	public static OlapQueryExecuterFactoryBundle getInstance() {
		return INSTANCE;
	}

	@Override
	public String[] getLanguages() {
		return LANGUAGES;
	}

	@Override
	public QueryExecuterFactory getQueryExecuterFactory(String language) throws JRException 
	{
		language = language.toUpperCase();
		if (language.equals("MDX"))
		{
			return cache.getCachedInstance(JRMdxQueryExecuterFactory.class.getName());
		}
		if (language.equals("OLAP4J"))
		{
			return cache.getCachedInstance(Olap4jQueryExecuterFactory.class.getName());
		}
		return null;
	}
}
