/*
 * ============================================================================
 * GNU Lesser General Public License
 * ============================================================================
 *
 * JasperReports - Free Java report-generating library.
 * Copyright (C) 2001-2005 JasperSoft Corporation http://www.jaspersoft.com
 * 
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307, USA.
 * 
 * JasperSoft Corporation
 * 185, Berry Street, Suite 6200
 * San Francisco CA 94107
 * http://www.jaspersoft.com
 */

/*
 * Contributors:
 * Artur Biesiadowski - abies@users.sourceforge.net 
 */
package net.sf.jasperreports.engine.xml;

import java.io.InputStream;
import java.net.URL;

import org.apache.commons.digester.Digester;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;


/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id$
 */
public class JRXmlDigester extends Digester
{


	/**
	 *
	 */
	//private static boolean wasWarning = false;


	/**
	 *
	 */
	public JRXmlDigester()
	{
		super();
	}


	/**
	 *
	 */
	public JRXmlDigester(XMLReader xmlReader)
	{
		super(xmlReader);
	}


	/**
	 *
	 */
	public InputSource resolveEntity(
		String publicId,
		String systemId
		) throws SAXException//, java.io.IOException
	{
		InputSource inputSource = null;

		if (systemId != null)
		{
			String dtd = null;
			
			if (
				systemId.equals("http://jasperreports.sourceforge.net/dtds/jasperreport.dtd") ||
				systemId.equals("http://www.jasperreports.com/dtds/jasperreport.dtd")
				)
			{
				dtd = "net/sf/jasperreports/engine/dtds/jasperreport.dtd";
			}
			else if (
				systemId.equals("http://jasperreports.sourceforge.net/dtds/jasperprint.dtd") ||
				systemId.equals("http://www.jasperreports.com/dtds/jasperprint.dtd")
				)
			{
				dtd = "net/sf/jasperreports/engine/dtds/jasperprint.dtd";
			}
			else
			{
				return new InputSource(systemId);
			}
			

			ClassLoader classLoader = Thread.currentThread().getContextClassLoader();

			URL url = null;
			if (classLoader != null)
			{
				url = classLoader.getResource(dtd);
			}
			if (url == null)
			{
				//if (!wasWarning)
				//{
				//	if (log.isWarnEnabled())
				//		log.warn("Failure using Thread.currentThread().getContextClassLoader() in JRXmlDigester class. Using JRXmlDigester.class.getClassLoader() instead.");
				//	wasWarning = true;
				//}
				classLoader = JRXmlDigester.class.getClassLoader();
			}
			
			InputStream is;
			if (classLoader == null)
			{
				is = JRXmlDigester.class.getResourceAsStream("/" + dtd);
			}
			else
			{
				is = classLoader.getResourceAsStream(dtd);
			}
			
			if (is != null)
			{
				inputSource = new InputSource(is);
			}
		}

		return inputSource;
	}

	
}
