/*
 * ============================================================================
 *                   The JasperReports License, Version 1.0
 * ============================================================================
 * 
 * Copyright (C) 2001-2004 Teodor Danciu (teodord@users.sourceforge.net). All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without modification,
 * are permitted provided that the following conditions are met:
 * 
 * 1. Redistributions of source code must retain the above copyright notice,
 *    this list of conditions and the following disclaimer.
 * 
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 * 
 * 3. The end-user documentation included with the redistribution, if any, must
 *    include the following acknowledgment: "This product includes software
 *    developed by Teodor Danciu (http://jasperreports.sourceforge.net)."
 *    Alternately, this acknowledgment may appear in the software itself, if
 *    and wherever such third-party acknowledgments normally appear.
 * 
 * 4. The name "JasperReports" must not be used to endorse or promote products 
 *    derived from this software without prior written permission. For written 
 *    permission, please contact teodord@users.sourceforge.net.
 * 
 * 5. Products derived from this software may not be called "JasperReports", nor 
 *    may "JasperReports" appear in their name, without prior written permission
 *    of Teodor Danciu.
 * 
 * THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED WARRANTIES,
 * INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
 * FITNESS  FOR A PARTICULAR  PURPOSE ARE  DISCLAIMED.  IN NO  EVENT SHALL  THE
 * APACHE SOFTWARE  FOUNDATION  OR ITS CONTRIBUTORS  BE LIABLE FOR  ANY DIRECT,
 * INDIRECT, INCIDENTAL, SPECIAL,  EXEMPLARY, OR CONSEQUENTIAL  DAMAGES (INCLU-
 * DING, BUT NOT LIMITED TO, PROCUREMENT  OF SUBSTITUTE GOODS OR SERVICES; LOSS
 * OF USE, DATA, OR  PROFITS; OR BUSINESS  INTERRUPTION)  HOWEVER CAUSED AND ON
 * ANY  THEORY OF LIABILITY,  WHETHER  IN CONTRACT,  STRICT LIABILITY,  OR TORT
 * (INCLUDING  NEGLIGENCE OR  OTHERWISE) ARISING IN  ANY WAY OUT OF THE  USE OF
 * THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

/*
 * ============================================================================
 *                   GNU Lesser General Public License
 * ============================================================================
 *
 * JasperReports - Free Java report-generating library.
 * Copyright (C) 2001-2004 Teodor Danciu teodord@users.sourceforge.net
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
 * Teodor Danciu
 * 173, Calea Calarasilor, Bl. 42, Sc. 1, Ap. 18
 * Postal code 030615, Sector 3
 * Bucharest, ROMANIA
 * Email: teodord@users.sourceforge.net
 */
package dori.jasper.engine.fill;

import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.sql.Connection;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import dori.jasper.engine.JRDataSource;
import dori.jasper.engine.JRException;
import dori.jasper.engine.JRExpression;
import dori.jasper.engine.JRGraphicElement;
import dori.jasper.engine.JRPrintElement;
import dori.jasper.engine.JRPrintPage;
import dori.jasper.engine.JRPrintRectangle;
import dori.jasper.engine.JRReportFont;
import dori.jasper.engine.JRRewindableDataSource;
import dori.jasper.engine.JRSubreport;
import dori.jasper.engine.JRSubreportParameter;
import dori.jasper.engine.JasperReport;
import dori.jasper.engine.design.JRDesignRectangle;
import dori.jasper.engine.util.JRLoader;


/**
 *
 */
public class JRFillSubreport extends JRFillElement implements JRSubreport, Runnable
{


	/**
	 *
	 */
	private static final Log log = LogFactory.getLog(JRFillSubreport.class);

	/**
	 *
	 */
	private Map parameterValues = null;
	private JRSubreportParameter[] parameters = null;
	private Connection connection = null;
	private JRDataSource dataSource = null;
	private JasperReport jasperReport = null;

	/**
	 *
	 */
	private JRBaseFiller subreportFiller = null;
	private JRPrintPage printPage = null;
	private JRReportFont[] subreportFonts = null;

	/**
	 *
	 */
	private JRException error = null;
	private Thread fillThread = null;
	private boolean isRunning = false;


	/**
	 *
	 */
	protected JRFillSubreport(
		JRBaseFiller filler,
		JRSubreport subreport, 
		JRFillObjectFactory factory
		)
	{
		super(filler, subreport, factory);

		this.parameters = subreport.getParameters();
	}


	/**
	 *
	 */
	public boolean isUsingCache()
	{
		return ((JRSubreport)this.parent).isUsingCache();
	}
		
	/**
	 *
	 */
	public void setUsingCache(boolean isUsingCache)
	{
	}
		
	/**
	 *
	 */
	public JRExpression getParametersMapExpression()
	{
		return ((JRSubreport)this.parent).getParametersMapExpression();
	}

	/**
	 *
	 */
	public JRSubreportParameter[] getParameters()
	{
		return this.parameters;
	}

	/**
	 *
	 */
	public JRExpression getConnectionExpression()
	{
		return ((JRSubreport)this.parent).getConnectionExpression();
	}

	/**
	 *
	 */
	public JRExpression getDataSourceExpression()
	{
		return ((JRSubreport)this.parent).getDataSourceExpression();
	}

	/**
	 *
	 */
	public JRExpression getExpression()
	{
		return ((JRSubreport)this.parent).getExpression();
	}

	/**
	 *
	 */
	protected JasperReport getJasperReport()
	{
		return this.jasperReport;
	}
		
	/**
	 *
	 */
	protected void setJasperReport(JasperReport jasperReport)
	{
		this.jasperReport = jasperReport;
	}

	/**
	 *
	 */
	protected JRTemplateRectangle getJRTemplateRectangle()
	{
		if (template == null)
		{
			JRDesignRectangle rectangle = new JRDesignRectangle();

			rectangle.setKey(this.getKey());
			rectangle.setPositionType(this.getPositionType());
			//rectangle.setPrintRepeatedValues(this.isPrintRepeatedValues());
			rectangle.setMode(this.getMode());
			rectangle.setX(this.getX());
			rectangle.setY(this.getY());
			rectangle.setWidth(this.getWidth());
			rectangle.setHeight(this.getHeight());
			rectangle.setRemoveLineWhenBlank(this.isRemoveLineWhenBlank());
			rectangle.setPrintInFirstWholeBand(this.isPrintInFirstWholeBand());
			rectangle.setPrintWhenDetailOverflows(this.isPrintWhenDetailOverflows());
			rectangle.setPrintWhenGroupChanges(this.getPrintWhenGroupChanges());
			rectangle.setForecolor(this.getForecolor());
			rectangle.setBackcolor(this.getBackcolor());
			rectangle.setPen(JRGraphicElement.PEN_NONE);

			template = new JRTemplateRectangle(rectangle);
		}
		
		return (JRTemplateRectangle)template;
	}


	/**
	 *
	 */
	protected JRReportFont[] getFonts()
	{
		return subreportFonts;
	}


	/**
	 *
	 */
	protected Collection getPrintElements()
	{
		Collection printElements = null;
		
		if (printPage != null)
		{
			printElements = printPage.getElements();
		}
		
		return printElements;
	}


	/**
	 *
	 */
	protected void evaluate(
		byte evaluation
		) throws JRException
	{
		this.reset();
		
		this.evaluatePrintWhenExpression(evaluation);

		if (
			(this.isPrintWhenExpressionNull() ||
			(!this.isPrintWhenExpressionNull() && 
			this.isPrintWhenTrue()))
			)
		{
			JRExpression expression = this.getExpression();
			Object source = this.filler.calculator.evaluate(expression, evaluation);
			if (source != null) // FIXME put some default broken image like in browsers
			{
				Class expressionClass = expression.getValueClass();
				
				if (expressionClass.equals(dori.jasper.engine.JasperReport.class))
				{
					JasperReport jrReport = (JasperReport)source;
					this.setJasperReport(jrReport);
				}
				else if (expressionClass.equals(java.io.InputStream.class))
				{
					InputStream is = (InputStream)source;
					this.setJasperReport((JasperReport)JRLoader.loadObject(is));
				}
				else if (expressionClass.equals(java.net.URL.class))
				{
					URL url = (URL)source;
					this.setJasperReport((JasperReport)JRLoader.loadObject(url));
				}
				else if (expressionClass.equals(java.io.File.class))
				{
					File file = (File)source;
					this.setJasperReport((JasperReport)JRLoader.loadObject(file));
				}
				else if (expressionClass.equals(java.lang.String.class))
				{
					String location = (String)source;
					if (this.isUsingCache())
					{
						if ( this.filler.loadedSubreports.containsKey(location) )
						{
							this.setJasperReport(
								(JasperReport)this.filler.loadedSubreports.get(location)
								);
						}
						else
						{
							JasperReport jrReport = (JasperReport)JRLoader.loadObjectFromLocation(location);
							this.setJasperReport(jrReport);
							this.filler.loadedSubreports.put(location, jrReport);
						}
					}
					else
					{
						this.setJasperReport((JasperReport)JRLoader.loadObjectFromLocation(location));
					}
				}
				
				if (jasperReport != null)
				{
					/*   */
					expression = this.getConnectionExpression();
					this.connection = (Connection)this.filler.calculator.evaluate(expression, evaluation);
			
					/*   */
					expression = this.getDataSourceExpression();
					this.dataSource = (JRDataSource)this.filler.calculator.evaluate(expression, evaluation);
					
					/*   */
					expression = this.getParametersMapExpression();
					this.parameterValues = (Map)this.filler.calculator.evaluate(expression, evaluation);
					
					/*   */
					JRSubreportParameter[] subreportParameters = this.getParameters();
					if (subreportParameters != null && subreportParameters.length > 0)
					{
						if (this.parameterValues == null)
						{
							this.parameterValues = new HashMap();
						}
						Object parameterValue = null;
						for(int i = 0; i < subreportParameters.length; i++)
						{
							expression = subreportParameters[i].getExpression();
							parameterValue = this.filler.calculator.evaluate(expression, evaluation);
							this.parameterValues.put(subreportParameters[i].getName(), parameterValue);
						}
					}
		
					/*   */
					switch (jasperReport.getPrintOrder())
					{
						case JasperReport.PRINT_ORDER_HORIZONTAL :
						{
							subreportFiller = new JRHorizontalFiller(jasperReport, this.filler);
							break;
						}
						case JasperReport.PRINT_ORDER_VERTICAL :
						{
							subreportFiller = new JRVerticalFiller(jasperReport, this.filler);
							break;
						}
					}
				}
			}
		}
	}


	/**
	 *
	 */
	public void run()
	{
		this.isRunning = true;
		
		this.error = null;
		
		try
		{
			if (this.getConnectionExpression() != null)
			{
				subreportFiller.fill(parameterValues, connection);
			}
			else
			{
				subreportFiller.fill(parameterValues, dataSource);
			}
		}
		catch(JRFillInterruptedException e)
		{
			//If the subreport filler was interrupted, we should remain silent
		}
		catch(JRException e)
		{
			this.error = e;
		}
		
		this.isRunning = false;

		synchronized (subreportFiller)
		{
			//main filler notified that the subreport has finished
			subreportFiller.notifyAll();
		}

/*
		if (this.error != null)
		{
			synchronized (subreportFiller)
			{
				//if an exception occured then we should notify the main filler that we have finished the subreport
				subreportFiller.notifyAll();
			}
		}
		*/
	}
	

	/**
	 *
	 */
	protected boolean prepare(
		int availableStretchHeight,
		boolean isOverflow
		) throws JRException
	{
		boolean willOverflow = false;

		super.prepare(availableStretchHeight, isOverflow);
		
		if (subreportFiller == null)
		{
			this.setToPrint(false);
		}

		if (!this.isToPrint())
		{
			return willOverflow;
		}

		//willOverflow = prepareTextField((JRFillTextField)fillElement, availableStretchHeight);
		
		//subreportFiller.setPageHeight(this.getHeight() + availableStretchHeight);
		subreportFiller.setPageHeight(this.getHeight() + availableStretchHeight - this.getRelativeY() + this.getY() + this.getBandBottomY());

		synchronized (subreportFiller)
		{
			if (this.fillThread == null)
			{
				if (!isOverflow || (isOverflow && this.isPrintWhenDetailOverflows()))
				{
					if (isOverflow)
					{
						this.setReprinted(true);
					}
					
					this.fillThread = new Thread(this);
					this.fillThread.start();
				}
				else
				{
					this.printPage = null;
					this.subreportFonts = null;
					this.setStretchHeight(this.getHeight());
					this.setToPrint(false);
					
					return willOverflow;
				}
			}
			else
			{
				//notifing the subreport fill thread that it can continue on the next page
				subreportFiller.notifyAll();
			}
		
			try
			{
				//waiting for the subreport fill thread to fill the current page
				subreportFiller.wait(); // FIXME maybe this is useless since you cannot enter 
										// the synchornized bloc if the subreport filler hasn't 
										// finished the page and passed to the wait state.
			}
			catch (InterruptedException e)
			{
				throw new JRException("Error encountered while waiting on the report filling thread.", e);
			}
			
			if (this.error != null)
			{
				throw this.error;
			}

			this.printPage = subreportFiller.getCurrentPage();
			this.subreportFonts = subreportFiller.getFonts();
			this.setStretchHeight(subreportFiller.getCurrentPageStretchHeight());

			//if the subreport fill thread has not finished, 
			// it means that the subreport will overflow on the next page
			willOverflow = isRunning;
			
			if (!willOverflow)
			{
				//the subreport fill thread has finished and the next time we shall create a new one
				this.fillThread = null;
			}
		}// synchronized
		
		Collection printElements = this.getPrintElements();
		if (
			(printElements == null || printElements.size() == 0) &&
			this.isRemoveLineWhenBlank() //FIXME if the line won't be removed, the background does not appear
			)
		{
			this.setToPrint(false);
		}

		return willOverflow;
	}
	

	/**
	 *
	 */
	public void rewind() throws JRException
	{
		if (subreportFiller == null)
		{
			return;
		}
		
		// marking the subreport filler for interruption
		subreportFiller.setInterrupted(true);
		// notifying the subreport filling thread that it can continue.
		// it will stop anyway when trying to fill the current band
		synchronized (subreportFiller)
		{
			subreportFiller.notifyAll();

			if (this.isRunning)
			{
				try
				{
					//waits until the master filler notifies it that can continue with the next page
					subreportFiller.wait();
				}
				catch(InterruptedException e)
				{
					throw new JRException("Error encountered while waiting on the subreport filling thread.", e);
				}
			}
		}

		// forcing the creation of a new thread and a new subreport filler
		fillThread = null;

		/*   */
		switch (jasperReport.getPrintOrder())
		{
			case JasperReport.PRINT_ORDER_HORIZONTAL :
			{
				subreportFiller = new JRHorizontalFiller(jasperReport, this.filler);
				break;
			}
			case JasperReport.PRINT_ORDER_VERTICAL :
			{
				subreportFiller = new JRVerticalFiller(jasperReport, this.filler);
				break;
			}
		}

		if (this.getConnectionExpression() == null)
		{
			if(dataSource instanceof JRRewindableDataSource)
			{
				((JRRewindableDataSource)dataSource).moveFirst();
			}
			else
			{
				if (log.isDebugEnabled())
					log.debug("The subreport is placed on a non-splitting band, but it does not have a rewindable data source.");
			}
		}
	}


	/**
	 *
	 */
	protected JRPrintElement fill() throws JRException
	{
		JRPrintRectangle printRectangle = null;

		printRectangle = new JRTemplatePrintRectangle(this.getJRTemplateRectangle());
		printRectangle.setX(this.getX());
		printRectangle.setY(this.getRelativeY());
		printRectangle.setHeight(this.getStretchHeight());
		
		return printRectangle;
	}


}
