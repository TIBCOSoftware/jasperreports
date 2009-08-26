/*
 * JasperReports - Free Java Reporting Library.
 * Copyright (C) 2001 - 2009 Jaspersoft Corporation. All rights reserved.
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
 *(at your option) any later version.
 *
 * JasperReports is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with JasperReports. If not, see <http://www.gnu.org/licenses/>.
 */
package net.sf.jasperreports.components.list;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExpression;
import net.sf.jasperreports.engine.JRPrintElement;
import net.sf.jasperreports.engine.JRPrintElementContainer;
import net.sf.jasperreports.engine.JRRuntimeException;
import net.sf.jasperreports.engine.JRStyle;
import net.sf.jasperreports.engine.component.BaseFillComponent;
import net.sf.jasperreports.engine.component.FillPrepareResult;
import net.sf.jasperreports.engine.fill.JRFillExpressionEvaluator;
import net.sf.jasperreports.engine.fill.JRFillObjectFactory;
import net.sf.jasperreports.engine.fill.JRTemplateFrame;
import net.sf.jasperreports.engine.fill.JRTemplatePrintFrame;

/**
 * Fill list component implementation.
 * 
 * @author Lucian Chirita (lucianc@users.sourceforge.net)
 * @version $Id$
 */
public class FillList extends BaseFillComponent
{
	
	private static final Log log = LogFactory.getLog(FillList.class);

	protected static class AppendingPrintElementContainer implements JRPrintElementContainer
	{
		private final JRPrintElementContainer container;
		private final int initialContainerHeight;

		public AppendingPrintElementContainer(JRPrintElementContainer container)
		{
			this.container = container;
			this.initialContainerHeight = container.getHeight();
		}
		
		public void addElement(JRPrintElement element)
		{
			element.setY(initialContainerHeight + element.getY());
			container.addElement(element);
		}

		public List getElements()
		{
			return container.getElements();
		}

		public int getHeight()
		{
			return 0;
		}

		public void setHeight(int height)
		{
			container.setHeight(initialContainerHeight + height);
		}
	}
	
	private final FillListContents listContents;
	private final FillDatasetRun datasetRun;
	
	private Map printFrameTemplates = new HashMap();
	private JRTemplatePrintFrame printFrame;
	private boolean filling;
	private boolean fillStarted;
	private boolean overflow;
	
	public FillList(ListComponent component, JRFillObjectFactory factory) throws JRException
	{
		JRFillObjectFactory datasetFactory = new JRFillObjectFactory(factory, 
				createDatasetExpressionEvaluator());
		this.listContents = new FillListContents(component.getContents(), datasetFactory);
		
		this.datasetRun = new FillDatasetRun(component.getDatasetRun(), factory);
	}

	private JRFillExpressionEvaluator createDatasetExpressionEvaluator()
	{
		return new JRFillExpressionEvaluator()
		{
			public Object evaluate(JRExpression expression,
					byte evaluationType) throws JRException
			{
				return datasetRun.evaluateDatasetExpression(
						expression, evaluationType);
			}
		};
	}

	public void evaluate(byte evaluation) throws JRException
	{
		if (filling)
		{
			log.warn("List fill did not complete, closing previous dataset run");
			datasetRun.end();
		}
		
		filling = false;
		fillStarted = false;
		
		datasetRun.evaluate(evaluation);
	}

	public FillPrepareResult prepare(int availableHeight)
	{
		createPrintFrame();
		try
		{
			boolean hadData = false;
			overflow = false;
			
			if (filling)
			{
				// continuing after an overflow
				if (log.isDebugEnabled())
				{
					log.debug("Continuing list after overflow");
				}
				
				hadData = true;
				fillContents(availableHeight);
			}
			else
			{
				if (log.isDebugEnabled())
				{
					log.debug("Starting list rendering");
				}
				
				if (fillStarted)
				{
					// if already started and finished, rewind the data source
					if (log.isDebugEnabled())
					{
						log.debug("List reprinted, rewinding data source");
					}
					
					datasetRun.rewind();
				}
				
				datasetRun.start();
				fillStarted = true;
			}
			
			while(!overflow && datasetRun.next())
			{
				hadData = true;
				
				listContents.evaluateContents();
				fillContents(availableHeight);
			}

			if (overflow)
			{
				if (log.isDebugEnabled())
				{
					log.debug("List has overflowed");
				}
				
				// set the filling flag so that we know that we are continuing
				filling = true;
				return FillPrepareResult.printStretch(availableHeight, overflow);
			}
			else
			{
				// list has completed;
				
				if (log.isDebugEnabled())
				{
					log.debug("List has completed rendering");
				}
				
				filling = false;
				datasetRun.end();
				
				if (!hadData)
				{
					//if no data, set as no print
					return FillPrepareResult.NO_PRINT_NO_OVERFLOW;
				}

				return FillPrepareResult.printStretch(printFrame.getHeight(), false);
			}
		}
		catch (JRException e)
		{
			throw new JRRuntimeException(e);
		}
	}

	protected void fillContents(int availableHeight) throws JRException
	{
		int contentsStretchHeight = availableHeight - printFrame.getHeight()
				- listContents.getHeight();
		if (contentsStretchHeight < 0)
		{
			overflow = true;
		}
		else
		{
			listContents.prepare(contentsStretchHeight);
			
			listContents.fillElements(
					new AppendingPrintElementContainer(printFrame));
			
			if (listContents.willOverflow())
			{
				overflow = true;
			}
		}
	}

	protected void createPrintFrame()
	{
		printFrame = new JRTemplatePrintFrame(getFrameTemplate());
		printFrame.setX(fillContext.getComponentElement().getX());
		printFrame.setWidth(fillContext.getComponentElement().getWidth());
	}

	protected JRTemplateFrame getFrameTemplate()
	{
		JRStyle style = fillContext.getElementStyle();
		JRTemplateFrame frameTemplate = (JRTemplateFrame) printFrameTemplates.get(style);
		if (frameTemplate == null)
		{
			frameTemplate = new JRTemplateFrame(
						fillContext.getElementOrigin(),
						fillContext.getDefaultStyleProvider());
			frameTemplate.setElement(fillContext.getComponentElement());
			
			printFrameTemplates.put(style, frameTemplate);
		}

		return frameTemplate;
	}

	public JRPrintElement fill()
	{
		printFrame.setY(fillContext.getElementPrintY());
		return printFrame;
	}

	public void rewind()
	{
		try
		{
			if (filling)
			{
				// if currently running, close the query
				datasetRun.end();
			}
			
			if (fillStarted)
			{
				if (log.isDebugEnabled())
				{
					log.debug("Rewinding started list");
				}
				
				// rewind the data source
				datasetRun.rewind();
			}
				
			filling = false;
			fillStarted = false;
		}
		catch (JRException e)
		{
			throw new JRRuntimeException(e);
		}
	}
}
