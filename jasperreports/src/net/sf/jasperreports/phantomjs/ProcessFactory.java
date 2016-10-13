/*
 * JasperReports - Free Java Reporting Library.
 * Copyright (C) 2001 - 2016 TIBCO Software Inc. All rights reserved.
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
package net.sf.jasperreports.phantomjs;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.pool2.BasePooledObjectFactory;
import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.impl.DefaultPooledObject;

import net.sf.jasperreports.engine.JRPropertiesUtil;
import net.sf.jasperreports.engine.JRRuntimeException;

/**
 * @author Lucian Chirita (lucianc@users.sourceforge.net)
 */
public class ProcessFactory extends BasePooledObjectFactory<PhantomJSProcess>
{
	private static final Log log = LogFactory.getLog(ProcessFactory.class);

	private ProcessDirector director;
	private int expirationCount;
	private int expirationTime;

	public ProcessFactory(ProcessDirector director, JRPropertiesUtil properties)
	{
		this.director = director;
		
		this.expirationCount = properties.getIntegerProperty(PhantomJS.PROPERTY_PHANTOMJS_EXPIRATION_COUNT,
				PhantomJS.DEFAULT_PHANTOMJS_EXPIRATION_COUNT);
		this.expirationTime = properties.getIntegerProperty(PhantomJS.PROPERTY_PHANTOMJS_EXPIRATION_TIME,
				PhantomJS.DEFAULT_PHANTOMJS_EXPIRATION_TIME);
	}
	
	@Override
	public PhantomJSProcess create() throws Exception
	{
		int port = InetUtil.getAvailablePort();
		PhantomJSProcess process = new PhantomJSProcess(director, port);
		process.startPhantomJS();
		return process;
	}

	@Override
	public PooledObject<PhantomJSProcess> wrap(PhantomJSProcess process)
	{
		return new DefaultPooledObject<PhantomJSProcess>(process);
	}

	@Override
	public void activateObject(PooledObject<PhantomJSProcess> pooledObject) throws Exception
	{
		super.activateObject(pooledObject);
		
		PhantomJSProcess process = pooledObject.getObject();
		if (process.hasEnded())
		{
			if (log.isDebugEnabled())
			{
				log.debug(process.getId() + " has ended");
			}
			
			throw new JRRuntimeException("Process " + process.getId() + " has ended");
		}
		
		long borrowedCount = ((DefaultPooledObject<PhantomJSProcess>) pooledObject).getBorrowedCount();
		if (borrowedCount >= expirationCount)
		{
			if (log.isDebugEnabled())
			{
				log.debug(process.getId() + " borrow count " + borrowedCount 
						+ " exceeded expiration count " + expirationCount);
			}
			
			throw new JRRuntimeException("Process " + process.getId() + " borrow count exceeded");
		}
		
		long now = System.currentTimeMillis();
		if (now >= pooledObject.getCreateTime() + expirationTime)
		{
			if (log.isDebugEnabled())
			{
				log.debug(process.getId() + " expiration time " + expirationTime 
						+ " from " + pooledObject.getCreateTime() + " exceeded");
			}
			
			throw new JRRuntimeException("Process " + process.getId() + " expiration time exceeded");
		}
	}

	@Override
	public boolean validateObject(PooledObject<PhantomJSProcess> pooledObject)
	{
		PhantomJSProcess process = pooledObject.getObject();
		
		String message = "validate" + System.currentTimeMillis();
		String data = "{\"echo\": \"" + message + "\"}";
		if (log.isDebugEnabled())
		{
			log.debug(process.getId() + " pinging with " + data);
		}
		
		try
		{
			String answer = process.getProcessConnection().runRequest(data);
			if (log.isDebugEnabled())
			{
				log.debug(process.getId() + " got answer " + answer);
			}
			return answer.equals(message);
		}
		catch (Exception e)
		{
			if (log.isDebugEnabled())
			{
				log.debug(process.getId() + " ping failed", e);
			}
			return false;
		}
	}

	@Override
	public void destroyObject(PooledObject<PhantomJSProcess> pooledObject) throws Exception
	{
		pooledObject.getObject().dispose();
	}

}
