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

import java.io.IOException;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.ServerSocket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Enumeration;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import net.sf.jasperreports.engine.JRRuntimeException;

/**
 * @author Lucian Chirita (lucianc@users.sourceforge.net)
 */
public class InetUtil
{
	private static final Log log = LogFactory.getLog(InetUtil.class);
	
	public static Inet4Address getIPv4Loopback()
	{
		InetAddress loopbackAddress = InetAddress.getLoopbackAddress();
		//the phantomjs web server module only works with IPv4
		if (loopbackAddress instanceof Inet4Address)
		{
			return (Inet4Address) loopbackAddress;
		}
		
		try
		{
			InetAddress[] addresses = InetAddress.getAllByName(loopbackAddress.getHostName());
			for (InetAddress inetAddress : addresses)
			{
				if (inetAddress instanceof Inet4Address)
				{
					return (Inet4Address) inetAddress;
				}
			}
		}
		catch (UnknownHostException e)
		{
			log.warn("Error while determining loopback addresses for " + loopbackAddress.getHostName(), e);
		}
		
		try
		{
			//keep looking for a IPv4 loopback address
			for (Enumeration<NetworkInterface> itfs = NetworkInterface.getNetworkInterfaces();
					itfs.hasMoreElements();)
			{
				NetworkInterface itf = itfs.nextElement();
				if (itf.isLoopback())
				{
					for (Enumeration<InetAddress> addresses = itf.getInetAddresses();
							addresses.hasMoreElements();)
					{
						InetAddress address = addresses.nextElement();
						if (address instanceof Inet4Address)
						{
							return (Inet4Address) address;
						}
					}
				}
			}
		}
		catch (SocketException e)
		{
			log.warn("Error while listing network interfaces", e);
		}
		
		return null;
	}
	
	public static int getAvailablePort()
	{
		try (ServerSocket socket = new ServerSocket(0))
		{
			int port = socket.getLocalPort();
			if (log.isDebugEnabled())
			{
				log.debug("found available port " + port);
			}
			return port;
		}
		catch (IOException e)
		{
			throw new JRRuntimeException(e);
		}
		
	}

}
