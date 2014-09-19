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
package net.sf.jasperreports.data.hibernate;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.HibernateException;
import org.hibernate.cfg.Environment;
import org.hibernate.connection.ConnectionProvider;
import org.hibernate.connection.ConnectionProviderFactory;
import org.hibernate.connection.DriverManagerConnectionProvider;
import org.hibernate.util.PropertiesHelper;
import org.hibernate.util.ReflectHelper;

/**
 * This connection provider is a modified version of the original {@link DriverManagerConnectionProvider} class
 * in order to be used inside an Eclipse based environment like Jaspersoft Studio.
 * 
 * This connection provider does <b>NOT USE</b> the {@link DriverManager}.
 * A rudimentary connection pool is implemented like in the original class.
 *  
 * @author Massimo Rabbi (mrabbi@users.sourceforge.net)
 *
 */
public class HibernateConnectionProvider implements ConnectionProvider {

    private String url;
    private Properties connectionProps;
    private Integer isolation;
    private final ArrayList<Connection> pool = new ArrayList<Connection>();
    private int poolSize;
    private int checkedOut = 0;
    private boolean autocommit;
    private Driver driver;

    private static final Log log = LogFactory.getLog(HibernateConnectionProvider.class);

    @Override
    public void configure(Properties props) throws HibernateException {

        String driverClass = props.getProperty(Environment.DRIVER);

        poolSize = PropertiesHelper.getInt(Environment.POOL_SIZE, props, 20); //default pool size 20
        log.info("Using Hibernate built-in connection pool (not for production use!)");
        log.info("Hibernate connection pool size: " + poolSize);

        autocommit = PropertiesHelper.getBoolean(Environment.AUTOCOMMIT, props);
        log.info("autocommit mode: " + autocommit);

        isolation = PropertiesHelper.getInteger(Environment.ISOLATION, props);
        if (isolation!=null)
        log.info( "JDBC isolation level: " + Environment.isolationLevelToString( isolation.intValue() ) );

        if (driverClass==null) {
            log.warn("no JDBC Driver class was specified by property " + Environment.DRIVER);
        }
        else {
            try {
                // trying via forName() first to be as close to DriverManager's semantics
            	// NOTE for JSS: we use the context class loader because it will be able to locate the database drivers
            	// already loaded in our plug-ins or projects
                driver = (Driver) Class.forName(driverClass, true, Thread.currentThread().getContextClassLoader()).newInstance();
            }
            catch (Exception e) {
                try {
                    driver = (Driver) ReflectHelper.classForName(driverClass).newInstance();
                }
                catch (Exception e1) {
                    log.error(e1.getMessage());
                    throw new HibernateException(e1);
                }
            }
        }

        url = props.getProperty( Environment.URL );
        if ( url == null ) {
            String msg = "JDBC URL was not specified by property " + Environment.URL;
            log.error( msg );
            throw new HibernateException( msg );
        }

        connectionProps = ConnectionProviderFactory.getConnectionProperties( props );

        log.info( "using driver: " + driverClass + " at URL: " + url );
        // if debug level is enabled, then log the password, otherwise mask it
        if ( log.isDebugEnabled() ) {
            log.info( "connection properties: " + connectionProps );
        }
        else if ( log.isInfoEnabled() ) {
            log.info( "connection properties: " + PropertiesHelper.maskOut(connectionProps, "password") );
        }

    }

    @Override
    public Connection getConnection() throws SQLException {

        if ( log.isTraceEnabled() ) log.trace( "total checked-out connections: " + checkedOut );

        synchronized (pool) {
            if ( !pool.isEmpty() ) {
                int last = pool.size() - 1;
                if ( log.isTraceEnabled() ) {
                    log.trace("using pooled JDBC connection, pool size: " + last);
                    checkedOut++;
                }
                Connection pooled = pool.remove(last);
                if (isolation!=null) pooled.setTransactionIsolation( isolation.intValue() );
                if ( pooled.getAutoCommit()!=autocommit ) pooled.setAutoCommit(autocommit);
                return pooled;
            }
        }

        log.debug("opening new JDBC connection");
        Connection conn = driver.connect(url, connectionProps);
        if (isolation!=null) conn.setTransactionIsolation( isolation.intValue() );
        if ( conn.getAutoCommit()!=autocommit ) conn.setAutoCommit(autocommit);

        if ( log.isDebugEnabled() ) {
            log.debug( "created connection to: " + url + ", Isolation Level: " + conn.getTransactionIsolation() );
        }
        if ( log.isTraceEnabled() ) checkedOut++;

        return conn;
    }

    @Override
    public void closeConnection(Connection conn) throws SQLException {

        if ( log.isDebugEnabled() ) checkedOut--;

        synchronized (pool) {
            int currentSize = pool.size();
            if ( currentSize < poolSize ) {
                if ( log.isTraceEnabled() ) log.trace("returning connection to pool, pool size: " + (currentSize + 1) );
                pool.add(conn);
                return;
            }
        }

        log.debug("closing JDBC connection");

        conn.close();

    }

    @Override
    public void close() {

        log.info("cleaning up connection pool: " + url);

        Iterator<Connection> iter = pool.iterator();
        while ( iter.hasNext() ) {
            try {
                iter.next().close();
            }
            catch (SQLException sqle) {
                log.warn("problem closing pooled connection", sqle);
            }
        }
        pool.clear();

    }

	@Override
    public boolean supportsAggressiveRelease() {
        return false;
    }

}
