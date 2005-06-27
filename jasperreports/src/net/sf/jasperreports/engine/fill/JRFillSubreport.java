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
package net.sf.jasperreports.engine.fill;

import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.sql.Connection;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import net.sf.jasperreports.engine.JRAbstractObjectFactory;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRElement;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExpression;
import net.sf.jasperreports.engine.JRExpressionCollector;
import net.sf.jasperreports.engine.JRGraphicElement;
import net.sf.jasperreports.engine.JRParameter;
import net.sf.jasperreports.engine.JRPrintElement;
import net.sf.jasperreports.engine.JRPrintPage;
import net.sf.jasperreports.engine.JRPrintRectangle;
import net.sf.jasperreports.engine.JRReportFont;
import net.sf.jasperreports.engine.JRRewindableDataSource;
import net.sf.jasperreports.engine.JRSubreport;
import net.sf.jasperreports.engine.JRSubreportParameter;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.design.JRDesignRectangle;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.engine.xml.JRXmlWriter;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id$
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
	private Throwable error = null;
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

		parameters = subreport.getParameters();
	}


	/**
	 *
	 */
	public boolean isUsingCache()
	{
		return ((JRSubreport)parent).isUsingCache();
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
		return ((JRSubreport)parent).getParametersMapExpression();
	}

	/**
	 *
	 */
	public JRSubreportParameter[] getParameters()
	{
		return parameters;
	}

	/**
	 *
	 */
	public JRExpression getConnectionExpression()
	{
		return ((JRSubreport)parent).getConnectionExpression();
	}

	/**
	 *
	 */
	public JRExpression getDataSourceExpression()
	{
		return ((JRSubreport)parent).getDataSourceExpression();
	}

	/**
	 *
	 */
	public JRExpression getExpression()
	{
		return ((JRSubreport)parent).getExpression();
	}

	/**
	 *
	 */
	protected JasperReport getJasperReport()
	{
		return jasperReport;
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

			rectangle.setKey(getKey());
			rectangle.setPositionType(getPositionType());
			//rectangle.setPrintRepeatedValues(isPrintRepeatedValues());
			rectangle.setMode(getMode());
			rectangle.setX(getX());
			rectangle.setY(getY());
			rectangle.setWidth(getWidth());
			rectangle.setHeight(getHeight());
			rectangle.setRemoveLineWhenBlank(isRemoveLineWhenBlank());
			rectangle.setPrintInFirstWholeBand(isPrintInFirstWholeBand());
			rectangle.setPrintWhenDetailOverflows(isPrintWhenDetailOverflows());
			rectangle.setPrintWhenGroupChanges(getPrintWhenGroupChanges());
			rectangle.setForecolor(getForecolor());
			rectangle.setBackcolor(getBackcolor());
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
		reset();
		
		evaluatePrintWhenExpression(evaluation);

		if (
			(isPrintWhenExpressionNull() ||
			(!isPrintWhenExpressionNull() && 
			isPrintWhenTrue()))
			)
		{
			JRExpression expression = getExpression();
			Object source = filler.calculator.evaluate(expression, evaluation);
			if (source != null) // FIXME put some default broken image like in browsers
			{
				Class expressionClass = expression.getValueClass();
				
				if (expressionClass.equals(net.sf.jasperreports.engine.JasperReport.class))
				{
					JasperReport jrReport = (JasperReport)source;
					setJasperReport(jrReport);
				}
				else if (expressionClass.equals(java.io.InputStream.class))
				{
					InputStream is = (InputStream)source;
					setJasperReport((JasperReport)JRLoader.loadObject(is));
				}
				else if (expressionClass.equals(java.net.URL.class))
				{
					URL url = (URL)source;
					setJasperReport((JasperReport)JRLoader.loadObject(url));
				}
				else if (expressionClass.equals(java.io.File.class))
				{
					File file = (File)source;
					setJasperReport((JasperReport)JRLoader.loadObject(file));
				}
				else if (expressionClass.equals(java.lang.String.class))
				{
					String location = (String)source;
					if (isUsingCache())
					{
						if ( filler.loadedSubreports.containsKey(location) )
						{
							setJasperReport(
								(JasperReport)filler.loadedSubreports.get(location)
								);
						}
						else
						{
							JasperReport jrReport = (JasperReport)JRLoader.loadObjectFromLocation(location);
							setJasperReport(jrReport);
							filler.loadedSubreports.put(location, jrReport);
						}
					}
					else
					{
						setJasperReport((JasperReport)JRLoader.loadObjectFromLocation(location));
					}
				}
				
				if (jasperReport != null)
				{
					/*   */
					expression = getParametersMapExpression();
					parameterValues = (Map)filler.calculator.evaluate(expression, evaluation);
					
					if (parameterValues != null)
					{
						parameterValues.remove(JRParameter.REPORT_LOCALE);
						parameterValues.remove(JRParameter.REPORT_RESOURCE_BUNDLE);
						parameterValues.remove(JRParameter.REPORT_CONNECTION);
						parameterValues.remove(JRParameter.REPORT_MAX_COUNT);
						parameterValues.remove(JRParameter.REPORT_DATA_SOURCE);
						parameterValues.remove(JRParameter.REPORT_SCRIPTLET);
						parameterValues.remove(JRParameter.REPORT_PARAMETERS_MAP);
					}

					/*   */
					expression = getConnectionExpression();
					connection = (Connection)filler.calculator.evaluate(expression, evaluation);
			
					/*   */
					expression = getDataSourceExpression();
					dataSource = (JRDataSource)filler.calculator.evaluate(expression, evaluation);
					
					/*   */
					JRSubreportParameter[] subreportParameters = getParameters();
					if (subreportParameters != null && subreportParameters.length > 0)
					{
						if (parameterValues == null)
						{
							parameterValues = new HashMap();
						}
						Object parameterValue = null;
						for(int i = 0; i < subreportParameters.length; i++)
						{
							expression = subreportParameters[i].getExpression();
							parameterValue = filler.calculator.evaluate(expression, evaluation);
							if (parameterValue == null)
							{
								parameterValues.remove(subreportParameters[i].getName());
							}
							else
							{
								parameterValues.put(subreportParameters[i].getName(), parameterValue);
							}
						}
					}
		
					/*   */
					switch (jasperReport.getPrintOrder())
					{
						case JasperReport.PRINT_ORDER_HORIZONTAL :
						{
							subreportFiller = new JRHorizontalFiller(jasperReport, filler);
							break;
						}
						case JasperReport.PRINT_ORDER_VERTICAL :
						{
							subreportFiller = new JRVerticalFiller(jasperReport, filler);
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
		isRunning = true;
		
		error = null;
		
		try
		{
			if (getConnectionExpression() != null)
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
		catch(Throwable t)
		{
			error = t;
		}
		
		isRunning = false;

		synchronized (subreportFiller)
		{
			//main filler notified that the subreport has finished
			subreportFiller.notifyAll();
		}

/*
		if (error != null)
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
			setToPrint(false);
		}

		if (!isToPrint())
		{
			return willOverflow;
		}

		//willOverflow = prepareTextField((JRFillTextField)fillElement, availableStretchHeight);
		
		//subreportFiller.setPageHeight(getHeight() + availableStretchHeight);
		subreportFiller.setPageHeight(getHeight() + availableStretchHeight - getRelativeY() + getY() + getBandBottomY());

		synchronized (subreportFiller)
		{
			if (fillThread == null)
			{
				if (!isOverflow || (isOverflow && isPrintWhenDetailOverflows()))
				{
					if (isOverflow)
					{
						setReprinted(true);
					}
					
					fillThread = new Thread(this);
					fillThread.start();
				}
				else
				{
					printPage = null;
					subreportFonts = null;
					setStretchHeight(getHeight());
					setToPrint(false);
					
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
			
			if (error != null)
			{
				if (error instanceof JRException)
				{
					throw (JRException)error;
				}
				else if (error instanceof RuntimeException)
				{
					throw (RuntimeException)error;
				}
				else
				{
					throw new JRException(error);
				}
			}

			printPage = subreportFiller.getCurrentPage();
			subreportFonts = subreportFiller.getFonts();
			setStretchHeight(subreportFiller.getCurrentPageStretchHeight());

			//if the subreport fill thread has not finished, 
			// it means that the subreport will overflow on the next page
			willOverflow = isRunning;
			
			if (!willOverflow)
			{
				//the subreport fill thread has finished and the next time we shall create a new one
				fillThread = null;
			}
		}// synchronized
		
		Collection printElements = getPrintElements();
		if (
			(printElements == null || printElements.size() == 0) &&
			isRemoveLineWhenBlank() //FIXME if the line won't be removed, the background does not appear
			)
		{
			setToPrint(false);
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

			if (isRunning)
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
				subreportFiller = new JRHorizontalFiller(jasperReport, filler);
				break;
			}
			case JasperReport.PRINT_ORDER_VERTICAL :
			{
				subreportFiller = new JRVerticalFiller(jasperReport, filler);
				break;
			}
		}

		if (getConnectionExpression() == null)
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

		printRectangle = new JRTemplatePrintRectangle(getJRTemplateRectangle());
		printRectangle.setX(getX());
		printRectangle.setY(getRelativeY());
		printRectangle.setHeight(getStretchHeight());
		
		return printRectangle;
	}


	/**
	 *
	 */
	public JRElement getCopy(JRAbstractObjectFactory factory)
	{
		return factory.getSubreport(this);
	}

	/**
	 *
	 */
	public void collectExpressions(JRExpressionCollector collector)
	{
		collector.collect(this);
	}

	/**
	 *
	 */
	public void writeXml(JRXmlWriter xmlWriter)
	{
		xmlWriter.writeSubreport(this);
	}


}
