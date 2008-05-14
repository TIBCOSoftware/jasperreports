/*
 * ============================================================================
 * GNU Lesser General Public License
 * ============================================================================
 *
 * JasperReports - Free Java report-generating library.
 * Copyright (C) 2001-2006 JasperSoft Corporation http://www.jaspersoft.com
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
 * 303 Second Street, Suite 450 North
 * San Francisco, CA 94107
 * http://www.jaspersoft.com
 */

/*
 * Contributors:
 * Ryan Johnson - delscovich@users.sourceforge.net
 * Carlton Moore - cmoore79@users.sourceforge.net
 */
package net.sf.jasperreports.engine.convert;

import java.awt.Color;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.sf.jasperreports.engine.JRBand;
import net.sf.jasperreports.engine.JRDefaultStyleProvider;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExpression;
import net.sf.jasperreports.engine.JRGroup;
import net.sf.jasperreports.engine.JRPen;
import net.sf.jasperreports.engine.JRPrintFrame;
import net.sf.jasperreports.engine.JRPrintPage;
import net.sf.jasperreports.engine.JRReport;
import net.sf.jasperreports.engine.JRReportTemplate;
import net.sf.jasperreports.engine.JRRuntimeException;
import net.sf.jasperreports.engine.JRStyle;
import net.sf.jasperreports.engine.JRStyleContainer;
import net.sf.jasperreports.engine.JRStyleSetter;
import net.sf.jasperreports.engine.JRTemplate;
import net.sf.jasperreports.engine.JRTemplateReference;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.base.JRBaseObjectFactory;
import net.sf.jasperreports.engine.base.JRBasePrintFrame;
import net.sf.jasperreports.engine.base.JRBasePrintPage;
import net.sf.jasperreports.engine.base.JRBaseStyle;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.util.JRExpressionUtil;
import net.sf.jasperreports.engine.util.JRProperties;
import net.sf.jasperreports.engine.xml.JRXmlTemplateLoader;

import org.apache.commons.collections.SequencedHashMap;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @author Sanda Zaharia (shertage@users.sourceforge.net)
 * @version $Id: JRPreviewBuilder.java 1857 2007-09-14 11:15:16Z lucianc $
 */
public class ReportConverter 
{

	private static final Log log = LogFactory.getLog(ReportConverter.class);
	public static final Color GRID_LINE_COLOR = new Color(170, 170, 255);
	
	private final JRReport report;
	private JasperPrint jasperPrint;
	private JRPrintPage page;
	int pageWidth;
	private int offsetY;
	private int upColumns;
	private int downColumns;
	
	/**
	 * List containing page elements in a given order 
	 */
	private List pageElements = new ArrayList();
	
	private StyleFactory styleFactory;
	protected Map stylesMap;
	protected final boolean cacheStyles;

	
	/**
	 *
	 */
	public ReportConverter(JRReport report, boolean ignoreContent, boolean cacheStyles)
	{
		this.report = report;
		this.cacheStyles = cacheStyles;
		
		if (report instanceof JasperDesign)
		{
			((JasperDesign)report).preprocess();
		}
		
		convert(ignoreContent);
	}
	
	/**
	 *
	 */
	protected void convert(boolean ignoreContent)
	{
		jasperPrint = new JasperPrint();
		
		jasperPrint.setDefaultFont(report.getDefaultFont());
		jasperPrint.setFormatFactoryClass(report.getFormatFactoryClass());
		//FIXME locale and timezone settings jasperprint object
		//jasperPrint.setLocaleCode(JRDataUtils.getLocaleCode(Locale.getDefault()));
		//jasperPrint.setTimeZoneId(JRDataUtils.getTimeZoneId(TimeZone.getDefault()));
		jasperPrint.setName(report.getName());
		jasperPrint.setOrientation(report.getOrientation());
		jasperPrint.setPageWidth(report.getPageWidth());
		jasperPrint.setPageHeight(report.getPageHeight());
		
		JRProperties.transferProperties(report, jasperPrint, JasperPrint.PROPERTIES_PRINT_TRANSFER_PREFIX);

		setStyles(report);

		if (!ignoreContent)
		{
			pageWidth = report.getPageWidth();
			page = new JRBasePrintPage();
			
			offsetY = report.getTopMargin();

			addBand(report.getBackground());
			addBand(report.getTitle());
			addBand(report.getPageHeader());
			upColumns = offsetY;
			addBand(report.getColumnHeader());

			JRGroup[] groups = report.getGroups();
			if (groups != null)
			{
				for (int i = 0; i < groups.length ; i++)
				{
					addBand(groups[i].getGroupHeader());
				}
			}
			
			addBand(report.getDetail());

			if (groups != null)
			{
				for (int i = 0; i < groups.length ; i++)
				{
					addBand(groups[i].getGroupFooter());
				}
			}
			
			addBand(report.getColumnFooter());
			downColumns = offsetY;
			addBand(report.getPageFooter());
			addBand(report.getLastPageFooter());
			addBand(report.getSummary());
			addBand(report.getNoData());
			
			jasperPrint.setPageHeight(offsetY + report.getBottomMargin());
			
			// column dotted delimitation 
			int colX = report.getLeftMargin();
			for(int i = 0; i < report.getColumnCount(); i++)
			{
				addColumnSeparator(colX);
				colX += report.getColumnWidth();
				addColumnSeparator(colX);
				colX += report.getColumnSpacing();
			}

			// page dotted contour line
			addHorizontalGridLine(0, report.getTopMargin(), pageWidth);
			addHorizontalGridLine(0, offsetY, pageWidth);
			addVerticalGridLine(report.getLeftMargin(), 0, jasperPrint.getPageHeight());
			addVerticalGridLine(pageWidth - report.getRightMargin(), 0, jasperPrint.getPageHeight());

			page.setElements(pageElements);
			jasperPrint.addPage(page);
		}
	}

	protected void setStyles(JRReport report)
	{
		styleFactory = new StyleFactory();
		stylesMap = new SequencedHashMap();
		
		loadReportStyles(report);
		
		try
		{
			for (Iterator it = stylesMap.values().iterator(); it.hasNext();)
			{
				JRStyle style = (JRStyle) it.next();
				jasperPrint.addStyle(style);
			}
		}
		catch (JRException e)
		{
			throw new JRRuntimeException(e);
		}

		JRStyle reportDefault = report.getDefaultStyle();
		JRStyle printDefault = null;
		if (reportDefault == null)
		{
			//search for the last default style
			for (Iterator it = stylesMap.values().iterator(); it.hasNext();)
			{
				JRStyle style = (JRStyle) it.next();
				if (style.isDefault())
				{
					printDefault = style;
				}
			}
		}
		else
		{
			printDefault = reportDefault;
		}
		
		if (printDefault != null)
		{
			jasperPrint.setDefaultStyle(printDefault);
		}		
	}

	protected void loadReportStyles(JRReport report)
	{
		JRReportTemplate[] templates = report.getTemplates();
		if (templates != null)
		{
			Set loadedLocations = new HashSet();
			for (int i = 0; i < templates.length; i++)
			{
				loadReportTemplateStyles(templates[i], loadedLocations);
			}
		}
		
		collectStyles(report.getStyles());
	}

	protected void loadReportTemplateStyles(JRReportTemplate template, Set loadedLocations)
	{
		JRExpression sourceExpression = template.getSourceExpression();
		if (sourceExpression != null)
		{
			String location = JRExpressionUtil.getSimpleExpressionText(sourceExpression);
			if (location == null)
			{
				log.warn("Template source expression " + sourceExpression.getText() + "cannot be evaluated; some styles might remain unresolved.");
			}
			else
			{
				loadTemplateStyles(location, loadedLocations);
			}
		}
	}

	protected void loadTemplateStyles(String location, Set loadedLocations)
	{
		if (!loadedLocations.add(location))
		{
			throw new JRRuntimeException("Circular dependency found for template at location " + location);
		}
		
		JRTemplate template;
		try
		{
			template = JRXmlTemplateLoader.load(location);
		}
		catch (Exception e)
		{
			log.warn("Could not load template from location " + location + "; some styles might remain unresolved.");
			return;
		}
		
		JRTemplateReference[] includedTemplates = template.getIncludedTemplates();
		if (includedTemplates != null)
		{
			for (int i = 0; i < includedTemplates.length; i++)
			{
				JRTemplateReference reference = includedTemplates[i];
				loadTemplateStyles(reference.getLocation(), loadedLocations);
			}
		}
		
		collectStyles(template.getStyles());
	}

	protected void collectStyles(JRStyle[] styles)
	{
		if (styles != null)
		{
			for (int i = 0; i < styles.length; i++)
			{
				JRStyle style = styles[i];
				JRStyle copy = styleFactory.getStyle(style);
				stylesMap.put(copy.getName(), copy);
			}
		}
	}

	/**
	 *
	 */
	private void addBand(JRBand band)
	{
		if (band != null)
		{
			JRBasePrintFrame frame = new JRBasePrintFrame(null);
			frame.setX(report.getLeftMargin());
			frame.setY(offsetY);
			frame.setWidth(report.getPageWidth() - report.getLeftMargin() - report.getRightMargin());
			frame.setHeight(band.getHeight());
			
			band.visit(new ConvertVisitor(this, frame));
			
			pageElements.add(frame);
			
			offsetY += band.getHeight();
			addBandSeparator(offsetY);
		}
	}
	
	/**
	 *
	 */
	private void addBandSeparator(int bandY)
	{
		addHorizontalGridLine(0, bandY, pageWidth);
	}
	
	/**
	 *
	 */
	private void addColumnSeparator(int colX)
	{
		if (downColumns > upColumns)
		{
			addVerticalGridLine(colX, upColumns, downColumns - upColumns);
		}
	}
	
	/**
	 *
	 */
	private void addHorizontalGridLine(int x, int y, int width)
	{
		JRPrintFrame printFrame = new JRBasePrintFrame(getDefaultStyleProvider());
		printFrame.setX(x);
		printFrame.setY(y);
		printFrame.setWidth(width);
		printFrame.setHeight(1);
		printFrame.getLineBox().getPen().setLineWidth(0);
		printFrame.getLineBox().getPen().setLineStyle(JRPen.LINE_STYLE_SOLID);
		printFrame.getLineBox().getTopPen().setLineWidth(0.1f);
		printFrame.getLineBox().getTopPen().setLineStyle(JRPen.LINE_STYLE_DASHED);
		printFrame.getLineBox().getTopPen().setLineColor(GRID_LINE_COLOR);
		pageElements.add(0, printFrame);
	}
	
	/**
	 *
	 */
	private void addVerticalGridLine(int x, int y, int height)
	{
		JRPrintFrame printFrame = new JRBasePrintFrame(getDefaultStyleProvider());
		printFrame.setX(x);
		printFrame.setY(y);
		printFrame.setWidth(1);
		printFrame.setHeight(height);
		printFrame.getLineBox().getPen().setLineWidth(0);
		printFrame.getLineBox().getPen().setLineStyle(JRPen.LINE_STYLE_SOLID);
		printFrame.getLineBox().getLeftPen().setLineWidth(0.1f);
		printFrame.getLineBox().getLeftPen().setLineStyle(JRPen.LINE_STYLE_DASHED);
		printFrame.getLineBox().getLeftPen().setLineColor(GRID_LINE_COLOR);
		pageElements.add(0, printFrame);
	}
	
	/**
	 * 
	 */
	protected JRStyle resolveStyle(JRStyleContainer originalContainer)
	{
		JRStyle originalStyle = originalContainer.getStyle();
		String nameReference = originalContainer.getStyleNameReference();
		JRStyle style;
		if (originalStyle != null)
		{
			style = styleFactory.getStyle(originalStyle);
		}
		else if (nameReference != null)
		{
			style = (JRStyle) stylesMap.get(nameReference);
			if (style == null)
			{
				log.warn("Style " + nameReference + " could not be resolved.");
			}
		}
		else
		{
			style = null;
		}
		return style;
	}	


	/**
	 * 
	 */	
	public JasperPrint getJasperPrint()
	{
		return jasperPrint;
	}
	
	
	/**
	 * 
	 */	
	public JRDefaultStyleProvider getDefaultStyleProvider()
	{
		return jasperPrint.getDefaultStyleProvider();
	}

	
	/**
	 * 
	 */	
	protected class StyleFactory extends JRBaseObjectFactory
	{
		public StyleFactory()
		{
			super(ReportConverter.this.getDefaultStyleProvider());
		}

		public JRExpression getExpression(JRExpression expression, boolean assignNotUsedId)
		{
			return expression;
		}

		public JRStyle getStyle(JRStyle style)
		{
			JRBaseStyle baseStyle = null;

			if (style != null)
			{
				baseStyle = (JRBaseStyle)get(style);
				if (
					baseStyle == null
					|| !ReportConverter.this.cacheStyles
					)
				{
					baseStyle = new JRBaseStyle(style, this);
					put(style, baseStyle);
				}
			}

			return baseStyle;
		}

		protected void handleStyleNameReference(JRStyleSetter setter, String nameReference)
		{
			JRStyle style = (JRStyle) stylesMap.get(nameReference);
			if (style == null)
			{
				log.warn("Style " + nameReference + " could not be resolved.");
			}
			else
			{
				setter.setStyle(style);
			}
		}
	}

}
