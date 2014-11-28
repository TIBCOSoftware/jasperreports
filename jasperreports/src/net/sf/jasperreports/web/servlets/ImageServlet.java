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
package net.sf.jasperreports.web.servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.jasperreports.engine.JRConstants;
import net.sf.jasperreports.web.util.ImageWebResourceHandler;


/**
 * @deprecated Replaced by {@link ImageWebResourceHandler} and {@link ResourceServlet}.
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 */
public class ImageServlet extends AbstractServlet
{
	private static final long serialVersionUID = JRConstants.SERIAL_VERSION_UID;


	/**
	 * @deprecated Replaced by {@link ImageWebResourceHandler#REQUEST_PARAMETER_IMAGE_NAME}.
	 */
	public static final String REQUEST_PARAMETER_IMAGE_NAME = ImageWebResourceHandler.REQUEST_PARAMETER_IMAGE_NAME;

			
	/**
	 *
	 */
	public void service(
		HttpServletRequest request,
		HttpServletResponse response
		) throws IOException, ServletException
	{
		ImageWebResourceHandler handler = new ImageWebResourceHandler();
		handler.handleResource(getJasperReportsContext(), request, response);
	}


}
