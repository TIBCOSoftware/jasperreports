/*
 * JasperReports - Free Java Reporting Library.
 * Copyright (C) 2001 - 2019 TIBCO Software Inc. All rights reserved.
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
package net.sf.jasperreports.chrome;

import java.io.IOException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.github.kklisura.cdt.protocol.commands.Page;
import com.github.kklisura.cdt.protocol.commands.Runtime;
import com.github.kklisura.cdt.protocol.types.page.CaptureScreenshotFormat;
import com.github.kklisura.cdt.protocol.types.page.Viewport;
import com.github.kklisura.cdt.protocol.types.runtime.AwaitPromise;
import com.github.kklisura.cdt.protocol.types.runtime.Evaluate;
import com.github.kklisura.cdt.protocol.types.runtime.ExceptionDetails;
import com.github.kklisura.cdt.protocol.types.runtime.RemoteObject;
import com.github.kklisura.cdt.services.ChromeDevToolsService;

import net.sf.jasperreports.engine.JRRuntimeException;
import net.sf.jasperreports.util.Base64Util;

/**
 * @author Lucian Chirita (lucianc@users.sourceforge.net)
 */
public class ChromePage
{
	
	private static final Log log = LogFactory.getLog(ChromePage.class);

	private final ChromeDevToolsService devToolsService;
	
	public ChromePage(ChromeDevToolsService devToolsService)
	{
		this.devToolsService = devToolsService;
	}
	
	public <T> T evaluatePromise(String promiseScript)
	{
		Runtime runtime = devToolsService.getRuntime();
		Evaluate evaluate = runtime.evaluate(promiseScript);
		checkException(evaluate.getExceptionDetails());
		RemoteObject evaluateResult = evaluate.getResult();
		
		AwaitPromise promise = runtime.awaitPromise(evaluateResult.getObjectId(), true, false);
		checkException(promise.getExceptionDetails());
		
		RemoteObject pResult = promise.getResult();
		@SuppressWarnings("unchecked")
		T result = (T) pResult.getValue();
		return result;
	}
	
	protected void checkException(ExceptionDetails exceptionDetails)
	{
		if (exceptionDetails != null)
		{
			String exceptionString = null;
			RemoteObject exception = exceptionDetails.getException();
			if (exception != null)
			{
				if (exception.getDescription() != null)
				{
					exceptionString = exception.getDescription();
				}
				else if (exception.getValue() != null)
				{
					exceptionString = exception.getValue().toString();
				}
			}
			log.error("Script error: " + exceptionDetails.getText() 
				+ ", exception: " + exceptionString);
			
			throw new JRRuntimeException("Script failed: " + exceptionDetails.getText());
		}
	}
	
	public byte[] captureScreenshot(int width, int height, double zoomFactor)
	{
		Viewport viewport = new Viewport();
		viewport.setX(0d);
		viewport.setY(0d);
		viewport.setWidth((double) width);
		viewport.setHeight((double) height);
		viewport.setScale(zoomFactor);
		
		Page page = devToolsService.getPage();
		String screenshotString = page.captureScreenshot(CaptureScreenshotFormat.PNG, 100,
				viewport, true);
		try
		{
			byte[] imageData = Base64Util.decode(screenshotString);
			return imageData;
		}
		catch (IOException e)
		{
			throw new JRRuntimeException(e);
		}
	}
	
}
