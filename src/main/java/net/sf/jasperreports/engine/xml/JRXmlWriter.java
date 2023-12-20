/*
 * JasperReports - Free Java Reporting Library.
 * Copyright (C) 2001 - 2023 Cloud Software Group, Inc. All rights reserved.
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
package net.sf.jasperreports.engine.xml;

import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

import net.sf.jasperreports.annotations.properties.Property;
import net.sf.jasperreports.annotations.properties.PropertyScope;
import net.sf.jasperreports.crosstabs.CrosstabColumnCell;
import net.sf.jasperreports.crosstabs.JRCellContents;
import net.sf.jasperreports.crosstabs.JRCrosstab;
import net.sf.jasperreports.crosstabs.JRCrosstabBucket;
import net.sf.jasperreports.crosstabs.JRCrosstabCell;
import net.sf.jasperreports.crosstabs.JRCrosstabColumnGroup;
import net.sf.jasperreports.crosstabs.JRCrosstabDataset;
import net.sf.jasperreports.crosstabs.JRCrosstabMeasure;
import net.sf.jasperreports.crosstabs.JRCrosstabParameter;
import net.sf.jasperreports.crosstabs.JRCrosstabRowGroup;
import net.sf.jasperreports.crosstabs.design.JRDesignCrosstab;
import net.sf.jasperreports.crosstabs.type.CrosstabColumnPositionEnum;
import net.sf.jasperreports.crosstabs.type.CrosstabPercentageEnum;
import net.sf.jasperreports.crosstabs.type.CrosstabRowPositionEnum;
import net.sf.jasperreports.crosstabs.type.CrosstabTotalPositionEnum;
import net.sf.jasperreports.crosstabs.xml.JRCellContentsFactory;
import net.sf.jasperreports.crosstabs.xml.JRCrosstabBucketFactory;
import net.sf.jasperreports.crosstabs.xml.JRCrosstabCellFactory;
import net.sf.jasperreports.crosstabs.xml.JRCrosstabColumnGroupFactory;
import net.sf.jasperreports.crosstabs.xml.JRCrosstabDatasetFactory;
import net.sf.jasperreports.crosstabs.xml.JRCrosstabFactory;
import net.sf.jasperreports.crosstabs.xml.JRCrosstabGroupFactory;
import net.sf.jasperreports.crosstabs.xml.JRCrosstabMeasureFactory;
import net.sf.jasperreports.crosstabs.xml.JRCrosstabParameterFactory;
import net.sf.jasperreports.crosstabs.xml.JRCrosstabRowGroupFactory;
import net.sf.jasperreports.engine.DatasetPropertyExpression;
import net.sf.jasperreports.engine.DefaultJasperReportsContext;
import net.sf.jasperreports.engine.ExpressionReturnValue;
import net.sf.jasperreports.engine.JRAnchor;
import net.sf.jasperreports.engine.JRBand;
import net.sf.jasperreports.engine.JRBreak;
import net.sf.jasperreports.engine.JRChild;
import net.sf.jasperreports.engine.JRComponentElement;
import net.sf.jasperreports.engine.JRConstants;
import net.sf.jasperreports.engine.JRDataset;
import net.sf.jasperreports.engine.JRDatasetParameter;
import net.sf.jasperreports.engine.JRDatasetRun;
import net.sf.jasperreports.engine.JRElement;
import net.sf.jasperreports.engine.JRElementDataset;
import net.sf.jasperreports.engine.JRElementGroup;
import net.sf.jasperreports.engine.JREllipse;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExpression;
import net.sf.jasperreports.engine.JRField;
import net.sf.jasperreports.engine.JRFont;
import net.sf.jasperreports.engine.JRFrame;
import net.sf.jasperreports.engine.JRGenericElement;
import net.sf.jasperreports.engine.JRGenericElementParameter;
import net.sf.jasperreports.engine.JRGenericElementType;
import net.sf.jasperreports.engine.JRGraphicElement;
import net.sf.jasperreports.engine.JRGroup;
import net.sf.jasperreports.engine.JRHyperlink;
import net.sf.jasperreports.engine.JRHyperlinkParameter;
import net.sf.jasperreports.engine.JRImage;
import net.sf.jasperreports.engine.JRLine;
import net.sf.jasperreports.engine.JRParameter;
import net.sf.jasperreports.engine.JRPart;
import net.sf.jasperreports.engine.JRPropertiesHolder;
import net.sf.jasperreports.engine.JRPropertiesMap;
import net.sf.jasperreports.engine.JRPropertiesUtil;
import net.sf.jasperreports.engine.JRPropertiesUtil.PropertySuffix;
import net.sf.jasperreports.engine.JRPropertyExpression;
import net.sf.jasperreports.engine.JRQuery;
import net.sf.jasperreports.engine.JRRectangle;
import net.sf.jasperreports.engine.JRReport;
import net.sf.jasperreports.engine.JRReportTemplate;
import net.sf.jasperreports.engine.JRRuntimeException;
import net.sf.jasperreports.engine.JRScriptlet;
import net.sf.jasperreports.engine.JRSection;
import net.sf.jasperreports.engine.JRSortField;
import net.sf.jasperreports.engine.JRStaticText;
import net.sf.jasperreports.engine.JRStyle;
import net.sf.jasperreports.engine.JRSubreport;
import net.sf.jasperreports.engine.JRSubreportParameter;
import net.sf.jasperreports.engine.JRSubreportReturnValue;
import net.sf.jasperreports.engine.JRTextElement;
import net.sf.jasperreports.engine.JRTextField;
import net.sf.jasperreports.engine.JRVariable;
import net.sf.jasperreports.engine.JasperReportsContext;
import net.sf.jasperreports.engine.ReturnValue;
import net.sf.jasperreports.engine.VariableReturnValue;
import net.sf.jasperreports.engine.analytics.dataset.BucketOrder;
import net.sf.jasperreports.engine.analytics.dataset.DataAxis;
import net.sf.jasperreports.engine.analytics.dataset.DataAxisLevel;
import net.sf.jasperreports.engine.analytics.dataset.DataLevelBucket;
import net.sf.jasperreports.engine.analytics.dataset.DataLevelBucketProperty;
import net.sf.jasperreports.engine.analytics.dataset.DataMeasure;
import net.sf.jasperreports.engine.analytics.dataset.MultiAxisData;
import net.sf.jasperreports.engine.component.ComponentKey;
import net.sf.jasperreports.engine.component.ComponentXmlWriter;
import net.sf.jasperreports.engine.component.ComponentsEnvironment;
import net.sf.jasperreports.engine.part.PartComponentXmlWriter;
import net.sf.jasperreports.engine.part.PartComponentsEnvironment;
import net.sf.jasperreports.engine.part.PartEvaluationTime;
import net.sf.jasperreports.engine.query.JRJdbcQueryExecuterFactory;
import net.sf.jasperreports.engine.type.BreakTypeEnum;
import net.sf.jasperreports.engine.type.CalculationEnum;
import net.sf.jasperreports.engine.type.DatasetResetTypeEnum;
import net.sf.jasperreports.engine.type.EvaluationTimeEnum;
import net.sf.jasperreports.engine.type.ExpressionTypeEnum;
import net.sf.jasperreports.engine.type.FooterPositionEnum;
import net.sf.jasperreports.engine.type.HyperlinkTargetEnum;
import net.sf.jasperreports.engine.type.HyperlinkTypeEnum;
import net.sf.jasperreports.engine.type.IncrementTypeEnum;
import net.sf.jasperreports.engine.type.LineDirectionEnum;
import net.sf.jasperreports.engine.type.OnErrorTypeEnum;
import net.sf.jasperreports.engine.type.OrientationEnum;
import net.sf.jasperreports.engine.type.PositionTypeEnum;
import net.sf.jasperreports.engine.type.PrintOrderEnum;
import net.sf.jasperreports.engine.type.ResetTypeEnum;
import net.sf.jasperreports.engine.type.RunDirectionEnum;
import net.sf.jasperreports.engine.type.SectionTypeEnum;
import net.sf.jasperreports.engine.type.SortFieldTypeEnum;
import net.sf.jasperreports.engine.type.SortOrderEnum;
import net.sf.jasperreports.engine.type.StretchTypeEnum;
import net.sf.jasperreports.engine.type.TextAdjustEnum;
import net.sf.jasperreports.engine.type.VerticalTextAlignEnum;
import net.sf.jasperreports.engine.type.WhenResourceMissingTypeEnum;
import net.sf.jasperreports.engine.util.JRExpressionUtil;
import net.sf.jasperreports.engine.util.JRStringUtil;
import net.sf.jasperreports.engine.util.JRXmlWriteHelper;
import net.sf.jasperreports.engine.util.XmlNamespace;
import net.sf.jasperreports.properties.PropertyConstants;


/**
 * A writer that produces the JRXML representation of an in-memory report.
 * <p>
 * Sometimes report designs are generated automatically using the JasperReports 
 * API. Report design objects obtained this way can be serialized for disk storage or 
 * transferred over the network, but they also can be stored in JRXML format.
 * </p><p>
 * The JRXML representation of a given report design object can be obtained by using one 
 * of the <code>public static writeReport()</code> methods exposed by this class.
 * </p>
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @author Minor enhancements by Barry Klawans (bklawans@users.sourceforge.net)
 */
public class JRXmlWriter extends JRXmlBaseWriter
{

	public static final XmlNamespace JASPERREPORTS_NAMESPACE = 
		new XmlNamespace(JRXmlConstants.JASPERREPORTS_NAMESPACE, null, JRXmlConstants.JASPERREPORT_XSD_SYSTEM_ID);
	public static final String EXCEPTION_MESSAGE_KEY_FILE_WRITE_ERROR = "xml.writer.file.write.error";
	public static final String EXCEPTION_MESSAGE_KEY_OUTPUT_STREAM_WRITE_ERROR = "xml.writer.output.stream.write.error";
	public static final String EXCEPTION_MESSAGE_KEY_REPORT_DESIGN_WRITE_ERROR = "xml.writer.report.design.write.error";
	public static final String EXCEPTION_MESSAGE_KEY_UNSUPPORTED_CHART_TYPE = "xml.writer.unsupported.chart.type";
	
	@Property(
			name = "net.sf.jasperreports.jrxml.writer.exclude.properties.{suffix}",
			category = PropertyConstants.CATEGORY_OTHER,
			scopes = {PropertyScope.CONTEXT},
			sinceVersion = PropertyConstants.VERSION_6_1_1
			)
	public static final String PREFIX_EXCLUDE_PROPERTIES = 
			JRPropertiesUtil.PROPERTY_PREFIX + "jrxml.writer.exclude.properties.";

	@Property(
			name = "net.sf.jasperreports.jrxml.writer.exclude.uuids",
			category = PropertyConstants.CATEGORY_OTHER,
			scopes = {PropertyScope.CONTEXT},
			sinceVersion = PropertyConstants.VERSION_6_5_1
			)
	public static final String PROPERTY_EXCLUDE_UUIDS = 
			JRPropertiesUtil.PROPERTY_PREFIX + "jrxml.writer.exclude.uuids";

	/**
	 *
	 */
	private JasperReportsContext jasperReportsContext;
	
	private List<Pattern> excludePropertiesPattern;
	private boolean excludeUuids;

	/**
	 *
	 */
	private JRReport report;

	private XmlWriterVisitor xmlWriterVisitor = new XmlWriterVisitor(this);


	/**
	 *
	 */
	public JRXmlWriter(JasperReportsContext jasperReportsContext)
	{
		this.jasperReportsContext = jasperReportsContext;
		
		initExcludeProperties();
	}


	private void initExcludeProperties()
	{
		JasperReportsContext context = jasperReportsContext == null 
				? DefaultJasperReportsContext.getInstance() : jasperReportsContext;
		List<PropertySuffix> excludeProperties = JRPropertiesUtil.getInstance(context).getProperties(
				PREFIX_EXCLUDE_PROPERTIES);
		
		excludePropertiesPattern = new ArrayList<>(excludeProperties.size());
		for (PropertySuffix propertySuffix : excludeProperties)
		{
			String regex = propertySuffix.getValue();
			Pattern pattern = Pattern.compile(regex);
			excludePropertiesPattern.add(pattern);
		}

		excludeUuids = JRPropertiesUtil.getInstance(context).getBooleanProperty(PROPERTY_EXCLUDE_UUIDS);
	}


	/**
	 *
	 */
	public JRReport getReport()
	{
		return report;
	}


	/**
	 *
	 */
	public boolean isExcludeUuids()
	{
		return excludeUuids;
	}


	/**
	 *
	 */
	public void setExcludeUuids(boolean excludeUuids)
	{
		this.excludeUuids = excludeUuids;
	}


	/**
	 *
	 */
	public String write(JRReport report, String encoding)
	{
		StringWriter buffer = new StringWriter();
		try
		{
			writeReport(report, encoding, buffer);
		}
		catch (IOException e)
		{
			// doesn't actually happen
			throw 
				new JRRuntimeException(
					EXCEPTION_MESSAGE_KEY_REPORT_DESIGN_WRITE_ERROR,
					(Object[])null,
					e);
		}
		return buffer.toString();
	}


	/**
	 *
	 */
	public void write(
		JRReport report,
		String destFileName,
		String encoding
		) throws JRException
	{
		try (
			Writer out = 
				new OutputStreamWriter(
					new BufferedOutputStream(
						new FileOutputStream(destFileName)
						), 
					encoding
					)
			)
		{
			writeReport(report, encoding, out);
		}
		catch (IOException e)
		{
			throw 
				new JRException(
					EXCEPTION_MESSAGE_KEY_FILE_WRITE_ERROR,
					new Object[]{destFileName},
					e);
		}
	}


	/**
	 *
	 */
	public void write(
		JRReport report,
		OutputStream outputStream,
		String encoding
		) throws JRException
	{
		try
		{
			Writer out = new OutputStreamWriter(outputStream, encoding);
			writeReport(report, encoding, out);
		}
		catch (Exception e)
		{
			throw 
				new JRException(
					EXCEPTION_MESSAGE_KEY_OUTPUT_STREAM_WRITE_ERROR,
					new Object[]{report.getName()},
					e);
		}
	}


	/**
	 * @see #write(JRReport, String)
	 */
	public static String writeReport(JRReport report, String encoding)
	{
		return new JRXmlWriter(DefaultJasperReportsContext.getInstance()).write(report, encoding);
	}


	/**
	 * @see #write(JRReport, String, String)
	 */
	public static void writeReport(
		JRReport report,
		String destFileName,
		String encoding
		) throws JRException
	{
		new JRXmlWriter(DefaultJasperReportsContext.getInstance()).write(report, destFileName, encoding);
	}


	/**
	 * @see #write(JRReport, OutputStream, String)
	 */
	public static void writeReport(
		JRReport report,
		OutputStream outputStream,
		String encoding
		) throws JRException
	{
			new JRXmlWriter(DefaultJasperReportsContext.getInstance()).write(report, outputStream, encoding);
	}


	/**
	 *
	 */
	protected void writeReport(JRReport report, String encoding, Writer out) throws IOException
	{
		this.report = report;
		
		String version = JRPropertiesUtil.getInstance(jasperReportsContext).getProperty(report, JRXmlBaseWriter.PROPERTY_REPORT_VERSION);
		useWriter(new JRXmlWriteHelper(out), version);

		writer.writeProlog(encoding);

		writer.startElement(JRXmlConstants.ELEMENT_jasperReport, getNamespace());
		writer.addEncodedAttribute(JRXmlConstants.ATTRIBUTE_name, report.getName());
		writer.addEncodedAttribute(JRXmlConstants.ATTRIBUTE_language, report.getLanguage(), JRReport.LANGUAGE_JAVA);
		writer.addAttribute(JRXmlConstants.ATTRIBUTE_columnCount, report.getColumnCount(), 1);
		writer.addAttribute(JRXmlConstants.ATTRIBUTE_printOrder, report.getPrintOrderValue(), PrintOrderEnum.VERTICAL);
		writer.addAttribute(JRXmlConstants.ATTRIBUTE_columnDirection, report.getColumnDirection(), RunDirectionEnum.LTR);
		writer.addAttribute(JRXmlConstants.ATTRIBUTE_pageWidth, report.getPageWidth());
		writer.addAttribute(JRXmlConstants.ATTRIBUTE_pageHeight, report.getPageHeight());
		writer.addAttribute(JRXmlConstants.ATTRIBUTE_orientation, report.getOrientationValue(), OrientationEnum.PORTRAIT);
		writer.addAttribute(JRXmlConstants.ATTRIBUTE_whenNoDataType, report.getWhenNoDataTypeValue());
		writer.addAttribute(JRXmlConstants.ATTRIBUTE_sectionType, report.getSectionType(), SectionTypeEnum.BAND);
		writer.addAttribute(JRXmlConstants.ATTRIBUTE_columnWidth, report.getColumnWidth());
		writer.addAttribute(JRXmlConstants.ATTRIBUTE_columnSpacing, report.getColumnSpacing(), 0);
		writer.addAttribute(JRXmlConstants.ATTRIBUTE_leftMargin, report.getLeftMargin());
		writer.addAttribute(JRXmlConstants.ATTRIBUTE_rightMargin, report.getRightMargin());
		writer.addAttribute(JRXmlConstants.ATTRIBUTE_topMargin, report.getTopMargin());
		writer.addAttribute(JRXmlConstants.ATTRIBUTE_bottomMargin, report.getBottomMargin());
		writer.addAttribute(JRXmlConstants.ATTRIBUTE_isTitleNewPage, report.isTitleNewPage(), false);
		writer.addAttribute(JRXmlConstants.ATTRIBUTE_isSummaryNewPage, report.isSummaryNewPage(), false);
		writer.addAttribute(JRXmlConstants.ATTRIBUTE_isSummaryWithPageHeaderAndFooter, report.isSummaryWithPageHeaderAndFooter(), false);
		writer.addAttribute(JRXmlConstants.ATTRIBUTE_isFloatColumnFooter, report.isFloatColumnFooter(), false);
		writer.addAttribute(JRXmlConstants.ATTRIBUTE_scriptletClass, report.getScriptletClass());
		writer.addAttribute(JRXmlConstants.ATTRIBUTE_formatFactoryClass, report.getFormatFactoryClass());
		writer.addEncodedAttribute(JRXmlConstants.ATTRIBUTE_resourceBundle, report.getResourceBundle());
		writer.addAttribute(JRXmlConstants.ATTRIBUTE_whenResourceMissingType, report.getWhenResourceMissingTypeValue(), WhenResourceMissingTypeEnum.NULL);
		writer.addAttribute(JRXmlConstants.ATTRIBUTE_isIgnorePagination, report.isIgnorePagination(), false);
		if (!isExcludeUuids())
		{
			writer.addAttribute(JRXmlConstants.ATTRIBUTE_uuid, report.getUUID().toString());
		}

		writeProperties(report);

		if (isNewerVersionOrEqual(JRConstants.VERSION_6_3_1))
		{
			writePropertyExpressions(report.getPropertyExpressions());
		}

		/*   */
		String[] imports = report.getImports();
		if (imports != null && imports.length > 0)
		{
			for(int i = 0; i < imports.length; i++)
			{
				String value = imports[i];
				if (value != null)
				{
					writer.startElement(JRXmlConstants.ELEMENT_import);
					writer.addEncodedAttribute(JRXmlConstants.ATTRIBUTE_value, value);
					writer.closeElement();
				}
			}
		}

		writeTemplates(report);

		/*   */
		JRStyle[] styles = report.getStyles();
		if (styles != null && styles.length > 0)
		{
			for(int i = 0; i < styles.length; i++)
			{
				writeStyle(styles[i]);
			}
		}

		JRDataset[] datasets = report.getDatasets();
		if (datasets != null && datasets.length > 0)
		{
			for (int i = 0; i < datasets.length; ++i)
			{
				writeDataset(datasets[i]);
			}
		}

		writeDatasetContents(report.getMainDataset());

		if (report.getBackground() != null)
		{
			writer.startElement(JRXmlConstants.ELEMENT_background);
			writeBand(report.getBackground());
			writer.closeElement();
		}

		if (report.getTitle() != null)
		{
			writer.startElement(JRXmlConstants.ELEMENT_title);
			writeBand(report.getTitle());
			writer.closeElement();
		}

		if (report.getPageHeader() != null)
		{
			writer.startElement(JRXmlConstants.ELEMENT_pageHeader);
			writeBand(report.getPageHeader());
			writer.closeElement();
		}

		if (report.getColumnHeader() != null)
		{
			writer.startElement(JRXmlConstants.ELEMENT_columnHeader);
			writeBand(report.getColumnHeader());
			writer.closeElement();
		}

		JRSection detail = report.getDetailSection();
		if (detail != null)
		{
			writer.startElement(JRXmlConstants.ELEMENT_detail);
			writeSection(detail);
			writer.closeElement(true);
		}

		if (report.getColumnFooter() != null)
		{
			writer.startElement(JRXmlConstants.ELEMENT_columnFooter);
			writeBand(report.getColumnFooter());
			writer.closeElement();
		}

		if (report.getPageFooter() != null)
		{
			writer.startElement(JRXmlConstants.ELEMENT_pageFooter);
			writeBand(report.getPageFooter());
			writer.closeElement();
		}

		if (report.getLastPageFooter() != null)
		{
			writer.startElement(JRXmlConstants.ELEMENT_lastPageFooter);
			writeBand(report.getLastPageFooter());
			writer.closeElement();
		}

		if (report.getSummary() != null)
		{
			writer.startElement(JRXmlConstants.ELEMENT_summary);
			writeBand(report.getSummary());
			writer.closeElement();
		}

		if (report.getNoData() != null)
		{
			writer.startElement(JRXmlConstants.ELEMENT_noData);
			writeBand(report.getNoData());
			writer.closeElement();
		}

		writer.closeElement();

		out.flush();
	}


	public void writeProperties(JRPropertiesHolder propertiesHolder) throws IOException
	{
		if (propertiesHolder.hasProperties())
		{
			JRPropertiesMap propertiesMap = propertiesHolder.getPropertiesMap();
			String[] propertyNames = propertiesMap.getPropertyNames();
			if (propertyNames != null && propertyNames.length > 0)
			{
				for(int i = 0; i < propertyNames.length; i++)
				{
					String propertyName = propertyNames[i];
					if (isPropertyToWrite(propertiesHolder, propertyName))
					{
						writer.startElement(JRXmlConstants.ELEMENT_property, getNamespace());
						writer.addEncodedAttribute(JRXmlConstants.ATTRIBUTE_name, propertyName);
						String value = propertiesMap.getProperty(propertyName);
						if (value != null)
						{
							String encodedValue = JRStringUtil.encodeXmlAttribute(value);
							if (
								isNewerVersionOrEqual(JRConstants.VERSION_6_4_0)
								&& encodedValue.length() != value.length()
								&& value.trim().equals(value)
								)
							{
								writer.writeCDATA(value);
							}
							else
							{
								writer.addAttribute(JRXmlConstants.ATTRIBUTE_value, encodedValue);
							}
						}
						writer.closeElement();
					}
				}
			}
		}
	}
	
	protected boolean isPropertyToWrite(JRPropertiesHolder propertiesHolder, String propertyName)
	{
		// currently the properties holder does not matter, we just look at the property name
		boolean toWrite = true;
		for (Pattern pattern : excludePropertiesPattern)
		{
			if (pattern.matcher(propertyName).matches())
			{
				// excluding
				toWrite = false;
				break;
			}
		}
		return toWrite;
	}


	protected void writeTemplates(JRReport report) throws IOException
	{
		JRReportTemplate[] templates = report.getTemplates();
		if (templates != null)
		{
			for (int i = 0; i < templates.length; i++)
			{
				JRReportTemplate template = templates[i];
				writeTemplate(template);
			}
		}
	}


	/**
	 * 
	 * @param template
	 * @throws IOException
	 */
	protected void writeTemplate(JRReportTemplate template) throws IOException
	{
		writer.writeExpression(JRXmlConstants.ELEMENT_template, template.getSourceExpression());
	}


	/**
	 *
	 */
	private void writeScriptlet(JRScriptlet scriptlet) throws IOException
	{
		writer.startElement(JRXmlConstants.ELEMENT_scriptlet);
		writer.addEncodedAttribute(JRXmlConstants.ATTRIBUTE_name, scriptlet.getName());
		writer.addAttribute(JRXmlConstants.ATTRIBUTE_class, scriptlet.getValueClassName());

		writeProperties(scriptlet);
		if (isNewerVersionOrEqual(JRConstants.VERSION_6_19_0))
		{
			writePropertyExpressions(scriptlet.getPropertyExpressions());
		}

		writer.writeCDATAElement(JRXmlConstants.ELEMENT_scriptletDescription, scriptlet.getDescription());

		writer.closeElement();
	}


	/**
	 *
	 */
	private void writeParameter(JRParameter parameter) throws IOException
	{
		writer.startElement(JRXmlConstants.ELEMENT_parameter);
		writer.addEncodedAttribute(JRXmlConstants.ATTRIBUTE_name, parameter.getName());
		writer.addEncodedAttribute(JRXmlConstants.ATTRIBUTE_class, parameter.getValueClassName());
		writer.addAttribute(JRXmlConstants.ATTRIBUTE_nestedType, parameter.getNestedTypeName());
		writer.addAttribute(JRXmlConstants.ATTRIBUTE_isForPrompting, parameter.isForPrompting(), true);
		if(isNewerVersionOrEqual(JRConstants.VERSION_6_3_1))
		{
			writer.addAttribute(JRXmlConstants.ATTRIBUTE_evaluationTime, parameter.getEvaluationTime());
		}

		writeProperties(parameter);

		writer.writeCDATAElement(JRXmlConstants.ELEMENT_parameterDescription, parameter.getDescription());
		writer.writeExpression(JRXmlConstants.ELEMENT_defaultValueExpression, parameter.getDefaultValueExpression());

		writer.closeElement();
	}


	/**
	 *
	 */
	private void writeQuery(JRQuery query) throws IOException
	{
		writer.startElement(JRXmlConstants.ELEMENT_queryString);
		writer.addEncodedAttribute(JRXmlConstants.ATTRIBUTE_language, query.getLanguage(), JRJdbcQueryExecuterFactory.QUERY_LANGUAGE_SQL);
		writer.writeCDATA(query.getText());
		writer.closeElement();
	}


	/**
	 *
	 */
	private void writeField(JRField field) throws IOException
	{
		writer.startElement(JRXmlConstants.ELEMENT_field);
		writer.addEncodedAttribute(JRXmlConstants.ATTRIBUTE_name, field.getName());
		writer.addEncodedAttribute(JRXmlConstants.ATTRIBUTE_class, field.getValueClassName());

		writeProperties(field);
		if (isNewerVersionOrEqual(JRConstants.VERSION_6_3_1))
		{
			writePropertyExpressions(field.getPropertyExpressions());
		}

		writer.writeCDATAElement(JRXmlConstants.ELEMENT_fieldDescription, field.getDescription());

		writer.closeElement();
	}


	/**
	 *
	 */
	private void writeSortField(JRSortField sortField) throws IOException
	{
		writer.startElement(JRXmlConstants.ELEMENT_sortField);
		writer.addEncodedAttribute(JRXmlConstants.ATTRIBUTE_name, sortField.getName());
		writer.addAttribute(JRXmlConstants.ATTRIBUTE_order, sortField.getOrderValue(), SortOrderEnum.ASCENDING);
		writer.addAttribute(JRXmlConstants.ATTRIBUTE_type, sortField.getType(), SortFieldTypeEnum.FIELD);
		writer.closeElement();
	}


	/**
	 *
	 */
	private void writeVariable(JRVariable variable) throws IOException
	{
		writer.startElement(JRXmlConstants.ELEMENT_variable);
		writer.addEncodedAttribute(JRXmlConstants.ATTRIBUTE_name, variable.getName());
		writer.addEncodedAttribute(JRXmlConstants.ATTRIBUTE_class, variable.getValueClassName());
		writer.addAttribute(JRXmlConstants.ATTRIBUTE_resetType, variable.getResetTypeValue(), ResetTypeEnum.REPORT);
		if (variable.getResetGroup() != null)
		{
			writer.addEncodedAttribute(JRXmlConstants.ATTRIBUTE_resetGroup, variable.getResetGroup().getName());
		}
		writer.addAttribute(JRXmlConstants.ATTRIBUTE_incrementType, variable.getIncrementTypeValue(), IncrementTypeEnum.NONE);
		if (variable.getIncrementGroup() != null)
		{
			writer.addEncodedAttribute(JRXmlConstants.ATTRIBUTE_incrementGroup, variable.getIncrementGroup().getName());
		}
		writer.addAttribute(JRXmlConstants.ATTRIBUTE_calculation, variable.getCalculationValue(), CalculationEnum.NOTHING);
		writer.addAttribute(JRXmlConstants.ATTRIBUTE_incrementerFactoryClass, variable.getIncrementerFactoryClassName());

		writer.writeCDATAElement(JRXmlConstants.ELEMENT_variableDescription, variable.getDescription());

		writer.writeExpression(JRXmlConstants.ELEMENT_variableExpression, variable.getExpression());
		writer.writeExpression(JRXmlConstants.ELEMENT_initialValueExpression, variable.getInitialValueExpression());

		writer.closeElement();
	}


	/**
	 *
	 */
	private void writeGroup(JRGroup group) throws IOException
	{
		writer.startElement(JRXmlConstants.ELEMENT_group);
		writer.addEncodedAttribute(JRXmlConstants.ATTRIBUTE_name, group.getName());
		writer.addAttribute(JRXmlConstants.ATTRIBUTE_isStartNewColumn, group.isStartNewColumn(), false);
		writer.addAttribute(JRXmlConstants.ATTRIBUTE_isStartNewPage, group.isStartNewPage(), false);
		writer.addAttribute(JRXmlConstants.ATTRIBUTE_isResetPageNumber, group.isResetPageNumber(), false);
		writer.addAttribute(JRXmlConstants.ATTRIBUTE_isReprintHeaderOnEachPage, group.isReprintHeaderOnEachPage(), false);
		if (isNewerVersionOrEqual(JRConstants.VERSION_6_5_1))
		{
			writer.addAttribute(JRXmlConstants.ATTRIBUTE_isReprintHeaderOnEachColumn, group.isReprintHeaderOnEachColumn(), false);
		}
		writer.addAttributePositive(JRXmlConstants.ATTRIBUTE_minHeightToStartNewPage, group.getMinHeightToStartNewPage());
		if (isNewerVersionOrEqual(JRConstants.VERSION_6_4_3))
		{
			writer.addAttributePositive(JRXmlConstants.ATTRIBUTE_minDetailsToStartFromTop, group.getMinDetailsToStartFromTop());
		}
		writer.addAttribute(JRXmlConstants.ATTRIBUTE_footerPosition, group.getFooterPositionValue(), FooterPositionEnum.NORMAL);
		writer.addAttribute(JRXmlConstants.ATTRIBUTE_keepTogether, group.isKeepTogether(), false);
		if (isNewerVersionOrEqual(JRConstants.VERSION_6_4_3))
		{
			writer.addAttribute(JRXmlConstants.ATTRIBUTE_preventOrphanFooter, group.isPreventOrphanFooter(), false);
		}

		writer.writeExpression(JRXmlConstants.ELEMENT_groupExpression, group.getExpression());

		JRSection groupHeader = group.getGroupHeaderSection();
		if (groupHeader != null)
		{
			writer.startElement(JRXmlConstants.ELEMENT_groupHeader);
			writeSection(groupHeader);
			writer.closeElement(true);
		}

		JRSection groupFooter = group.getGroupFooterSection();
		if (groupFooter != null)
		{
			writer.startElement(JRXmlConstants.ELEMENT_groupFooter);
			writeSection(groupFooter);
			writer.closeElement(true);
		}

		writer.closeElement();
	}


	/**
	 *
	 */
	protected void writeSection(JRSection section) throws IOException
	{
		if (section != null)
		{
			JRBand[] bands = section.getBands();
			if (bands != null && bands.length > 0)
			{
				for(int i = 0; i < bands.length; i++)
				{
					writeBand(bands[i]);
				}
			}

			if (isNewerVersionOrEqual(JRConstants.VERSION_6_0_0))
			{
				JRPart[] parts = section.getParts();
				if (parts != null && parts.length > 0)
				{
					for(int i = 0; i < parts.length; i++)
					{
						writePart(parts[i]);
					}
				}
			}
		}
	}


	/**
	 *
	 */
	private void writeBand(JRBand band) throws IOException
	{
		writer.startElement(JRXmlConstants.ELEMENT_band);
		writer.addAttributePositive(JRXmlConstants.ATTRIBUTE_height, band.getHeight());
		writer.addAttribute(JRXmlConstants.ATTRIBUTE_splitType, band.getSplitTypeValue());

		writeProperties(band);
		writer.writeExpression(JRXmlConstants.ELEMENT_printWhenExpression, band.getPrintWhenExpression());
		
		writeChildElements(band);

		if (isNewerVersionOrEqual(JRConstants.VERSION_6_1_1))
		{
			List<ExpressionReturnValue> returnValues = band.getReturnValues();
			if (returnValues != null && !returnValues.isEmpty())
			{
				for (ExpressionReturnValue returnValue : returnValues)
				{
					writeReturnValue(returnValue);
				}
			}
		}
		
		writer.closeElement();
	}


	/**
	 *
	 */
	private void writePart(JRPart part) throws IOException
	{
		ComponentKey componentKey = part.getComponentKey();
		PartComponentXmlWriter componentXmlWriter = 
			PartComponentsEnvironment.getInstance(jasperReportsContext).getManager(componentKey).getComponentXmlWriter(jasperReportsContext);
		
		if (componentXmlWriter.isToWrite(part, this))
		{
			writer.startElement(JRXmlConstants.ELEMENT_part, getNamespace());
			PartEvaluationTime evaluationTime = part.getEvaluationTime();
			if (evaluationTime != null)
			{
				writer.addAttribute(JRXmlConstants.ATTRIBUTE_evaluationTime, evaluationTime.getEvaluationTimeType());
				writer.addAttribute(JRXmlConstants.ATTRIBUTE_evaluationGroup, evaluationTime.getEvaluationGroup());
			}
			if (!isExcludeUuids())
			{
				writer.addAttribute(JRXmlConstants.ATTRIBUTE_uuid, part.getUUID().toString());
			}

			writeProperties(part);
			if (isNewerVersionOrEqual(JRConstants.VERSION_6_20_2))
			{
				writePropertyExpressions(part.getPropertyExpressions());
			}
			writer.writeExpression(JRXmlConstants.ELEMENT_printWhenExpression, part.getPrintWhenExpression());
			writer.writeExpression(JRXmlConstants.ELEMENT_partNameExpression, part.getPartNameExpression());
			
			componentXmlWriter.writeToXml(part, this);

			writer.closeElement();
		}
	}

	
	/**
	 * Writes the contents (child elements) of an element container.
	 * 
	 * @param elementContainer the element container
	 */
	public void writeChildElements(JRElementGroup elementContainer)
	{
		List<JRChild> children = elementContainer.getChildren();
		if (children != null && children.size() > 0)
		{
			for(int i = 0; i < children.size(); i++)
			{
				children.get(i).visit(xmlWriterVisitor);
			}
		}
	}

	/**
	 *
	 */
	public void writeElementGroup(JRElementGroup elementGroup) throws IOException
	{
		writer.startElement(JRXmlConstants.ELEMENT_elementGroup, getNamespace());

		/*   */
		writeChildElements(elementGroup);

		writer.closeElement();
	}


	/**
	 *
	 */
	public void writeBreak(JRBreak breakElement) throws IOException
	{
		writer.startElement(JRXmlConstants.ELEMENT_break, getNamespace());
		writer.addAttribute(JRXmlConstants.ATTRIBUTE_type, breakElement.getTypeValue(), BreakTypeEnum.PAGE);

		writeReportElement(breakElement);

		writer.closeElement();
	}


	/**
	 *
	 */
	public void writeLine(JRLine line) throws IOException
	{
		writer.startElement(JRXmlConstants.ELEMENT_line, getNamespace());
		writer.addAttribute(JRXmlConstants.ATTRIBUTE_direction, line.getDirectionValue(), LineDirectionEnum.TOP_DOWN);

		writeReportElement(line);
		writeGraphicElement(line);

		writer.closeElement();
	}


	/**
	 *
	 */
	public void writeReportElement(JRElement element) throws IOException
	{
		writer.startElement(JRXmlConstants.ELEMENT_reportElement);
		writer.addEncodedAttribute(JRXmlConstants.ATTRIBUTE_key, element.getKey());
		writeStyleReferenceAttr(element);
		writer.addAttribute(JRXmlConstants.ATTRIBUTE_positionType, element.getPositionTypeValue(), PositionTypeEnum.FIX_RELATIVE_TO_TOP);
		writeStretchType(element.getStretchTypeValue());
		writer.addAttribute(JRXmlConstants.ATTRIBUTE_isPrintRepeatedValues, element.isPrintRepeatedValues(), true);
		writer.addAttribute(JRXmlConstants.ATTRIBUTE_mode, element.getOwnModeValue());

		writer.addAttribute(JRXmlConstants.ATTRIBUTE_x, element.getX());
		writer.addAttribute(JRXmlConstants.ATTRIBUTE_y, element.getY());
		writer.addAttribute(JRXmlConstants.ATTRIBUTE_width, element.getWidth());
		writer.addAttribute(JRXmlConstants.ATTRIBUTE_height, element.getHeight());
		writer.addAttribute(JRXmlConstants.ATTRIBUTE_isRemoveLineWhenBlank, element.isRemoveLineWhenBlank(), false);
		writer.addAttribute(JRXmlConstants.ATTRIBUTE_isPrintInFirstWholeBand, element.isPrintInFirstWholeBand(), false);
		writer.addAttribute(JRXmlConstants.ATTRIBUTE_isPrintWhenDetailOverflows, element.isPrintWhenDetailOverflows(), false);

		if (element.getPrintWhenGroupChanges() != null)
		{
			writer.addEncodedAttribute(JRXmlConstants.ATTRIBUTE_printWhenGroupChanges, element.getPrintWhenGroupChanges().getName());
		}

		writer.addAttribute(JRXmlConstants.ATTRIBUTE_forecolor, element.getOwnForecolor());
		writer.addAttribute(JRXmlConstants.ATTRIBUTE_backcolor, element.getOwnBackcolor());
		
		if (!isExcludeUuids())
		{
			writer.addEncodedAttribute(JRXmlConstants.ATTRIBUTE_uuid, element.getUUID().toString());
		}

		writeProperties(element);
		writePropertyExpressions(element.getPropertyExpressions());
		writer.writeExpression(JRXmlConstants.ELEMENT_printWhenExpression, element.getPrintWhenExpression());
		if (isNewerVersionOrEqual(JRConstants.VERSION_6_20_6))
		{
			writer.writeExpression(JRXmlConstants.ELEMENT_styleExpression, element.getStyleExpression());
		}
		writer.closeElement();
	}


	private void writeStretchType(StretchTypeEnum stretchType)
	{
		String stretchTypeStr = stretchType == null ? null : stretchType.getName();
		
		if (isOlderVersionThan(JRConstants.VERSION_6_2_2))
		{
			switch (stretchType)
			{
				case CONTAINER_HEIGHT :
				case CONTAINER_BOTTOM :
				{
					stretchTypeStr = StretchTypeEnum.RELATIVE_TO_BAND_HEIGHT_deprecated;
					break;
				}
				case ELEMENT_GROUP_HEIGHT :
				case ELEMENT_GROUP_BOTTOM :
				{
					stretchTypeStr = StretchTypeEnum.RELATIVE_TO_TALLEST_OBJECT_deprecated;
					break;
				}
				default :
			}
		}
		writer.addAttribute(JRXmlConstants.ATTRIBUTE_stretchType, stretchTypeStr, StretchTypeEnum.NO_STRETCH.getName());
	}


	public void writePropertyExpressions(JRPropertyExpression[] propertyExpressions) throws IOException
	{
		if (propertyExpressions != null)
		{
			for (int i = 0; i < propertyExpressions.length; i++)
			{
				writePropertyExpression(propertyExpressions[i]);
			}
		}
	}


	public void writePropertyExpressions(DatasetPropertyExpression[] propertyExpressions) throws IOException
	{
		if (propertyExpressions != null)
		{
			for (int i = 0; i < propertyExpressions.length; i++)
			{
				writePropertyExpression(propertyExpressions[i]);
			}
		}
	}


	protected void writePropertyExpression(JRPropertyExpression propertyExpression) throws IOException
	{
		String expressionText = "";
		ExpressionTypeEnum expressionType = null;

		JRExpression valueExpression = propertyExpression.getValueExpression();
		if (valueExpression != null)
		{
			expressionText = valueExpression.getText();
			expressionType = valueExpression.getType();
			if (
				expressionType == ExpressionTypeEnum.SIMPLE_TEXT
				&& isOlderVersionThan(JRConstants.VERSION_6_4_3)
				)
			{
				expressionType = null;
				expressionText = JRExpressionUtil.convertSimpleTextExpression(valueExpression);
			}
		}
		
		writer.writeCDATAElement(
			JRXmlConstants.ELEMENT_propertyExpression, 
			getNamespace(), 
			expressionText, 
			new String[]{
				JRXmlConstants.ATTRIBUTE_name, 
				JRXmlConstants.ATTRIBUTE_type}, 
			new Object[]{
				propertyExpression.getName(), 
				expressionType == null ? null : expressionType.getName()}
			);
	}


	protected void writePropertyExpression(DatasetPropertyExpression propertyExpression) throws IOException
	{
		String expressionText = "";
		ExpressionTypeEnum expressionType = null;

		JRExpression valueExpression = propertyExpression.getValueExpression();
		if (valueExpression != null)
		{
			expressionText = valueExpression.getText();
			expressionType = valueExpression.getType();
			if (
				expressionType == ExpressionTypeEnum.SIMPLE_TEXT
				&& isOlderVersionThan(JRConstants.VERSION_6_4_3)
				)
			{
				expressionType = null;
				expressionText = JRExpressionUtil.convertSimpleTextExpression(valueExpression);
			}
		}
		
		writer.writeCDATAElement(
			JRXmlConstants.ELEMENT_propertyExpression, 
			getNamespace(), 
			expressionText, 
			new String[]{
				JRXmlConstants.ATTRIBUTE_name, 
				JRXmlConstants.ATTRIBUTE_type, 
				JRXmlConstants.ATTRIBUTE_evaluationTime}, 
			new Object[]{
				propertyExpression.getName(),
				expressionType == null ? null : expressionType.getName(),
				propertyExpression.getEvaluationTime() == null ? null : propertyExpression.getEvaluationTime().getName()}
			);
	}


	/**
	 *
	 */
	private void writeGraphicElement(JRGraphicElement element) throws IOException
	{
		writer.startElement(JRXmlConstants.ELEMENT_graphicElement);
		writer.addAttribute(JRXmlConstants.ATTRIBUTE_fill, element.getOwnFillValue());
		writePen(element.getLinePen());
		writer.closeElement(true);
	}


	/**
	 *
	 */
	public void writeRectangle(JRRectangle rectangle) throws IOException
	{
		writer.startElement(JRXmlConstants.ELEMENT_rectangle, getNamespace());
		writer.addAttribute(JRXmlConstants.ATTRIBUTE_radius, rectangle.getOwnRadius());

		writeReportElement(rectangle);
		writeGraphicElement(rectangle);

		writer.closeElement();
	}


	/**
	 *
	 */
	public void writeEllipse(JREllipse ellipse) throws IOException
	{
		writer.startElement(JRXmlConstants.ELEMENT_ellipse, getNamespace());

		writeReportElement(ellipse);
		writeGraphicElement(ellipse);

		writer.closeElement();
	}


	/**
	 *
	 */
	public void writeImage(JRImage image) throws IOException
	{
		writer.startElement(JRXmlConstants.ELEMENT_image, getNamespace());
		writer.addAttribute(JRXmlConstants.ATTRIBUTE_scaleImage, image.getOwnScaleImageValue());
		if (isNewerVersionOrEqual(JRConstants.VERSION_6_10_0))
		{
			writer.addAttribute(JRXmlConstants.ATTRIBUTE_rotation, image.getOwnRotation());
		}
		writer.addAttribute(JRXmlConstants.ATTRIBUTE_hAlign, image.getOwnHorizontalImageAlign());
		writer.addAttribute(JRXmlConstants.ATTRIBUTE_vAlign, image.getOwnVerticalImageAlign());
		writer.addAttribute(JRXmlConstants.ATTRIBUTE_isUsingCache, image.getUsingCache());
		writer.addAttribute(JRXmlConstants.ATTRIBUTE_isLazy, image.isLazy(), false);
		writer.addAttribute(JRXmlConstants.ATTRIBUTE_onErrorType, image.getOnErrorTypeValue(), OnErrorTypeEnum.ERROR);
		writer.addAttribute(JRXmlConstants.ATTRIBUTE_evaluationTime, image.getEvaluationTimeValue(), EvaluationTimeEnum.NOW);

		if (image.getEvaluationGroup() != null)
		{
			writer.addEncodedAttribute(JRXmlConstants.ATTRIBUTE_evaluationGroup, image.getEvaluationGroup().getName());
		}

		writer.addEncodedAttribute(JRXmlConstants.ATTRIBUTE_hyperlinkType, image.getLinkType(), HyperlinkTypeEnum.NONE.getName());
		writer.addEncodedAttribute(JRXmlConstants.ATTRIBUTE_hyperlinkTarget, image.getLinkTarget(), HyperlinkTargetEnum.SELF.getName());
		writer.addAttribute(JRXmlConstants.ATTRIBUTE_bookmarkLevel, image.getBookmarkLevel(), JRAnchor.NO_BOOKMARK);

		writeReportElement(image);
		writeBox(image.getLineBox());
		writeGraphicElement(image);

		//FIXME class is mandatory in verifier

		writer.writeExpression(JRXmlConstants.ELEMENT_imageExpression, image.getExpression());
		writer.writeExpression(JRXmlConstants.ELEMENT_anchorNameExpression, image.getAnchorNameExpression());
		if(isNewerVersionOrEqual(JRConstants.VERSION_6_13_0))
		{
			writer.writeExpression(JRXmlConstants.ELEMENT_bookmarkLevelExpression, image.getBookmarkLevelExpression());
		}
		writer.writeExpression(JRXmlConstants.ELEMENT_hyperlinkReferenceExpression, image.getHyperlinkReferenceExpression());
		writer.writeExpression(JRXmlConstants.ELEMENT_hyperlinkWhenExpression, image.getHyperlinkWhenExpression());//FIXMENOW can we reuse method for writing hyperlink?
		writer.writeExpression(JRXmlConstants.ELEMENT_hyperlinkAnchorExpression, image.getHyperlinkAnchorExpression());
		writer.writeExpression(JRXmlConstants.ELEMENT_hyperlinkPageExpression, image.getHyperlinkPageExpression());
		writer.writeExpression(JRXmlConstants.ELEMENT_hyperlinkTooltipExpression, image.getHyperlinkTooltipExpression());
		writeHyperlinkParameters(image.getHyperlinkParameters());

		writer.closeElement();
	}


	/**
	 *
	 */
	public void writeStaticText(JRStaticText staticText) throws IOException
	{
		writer.startElement(JRXmlConstants.ELEMENT_staticText, getNamespace());

		writeReportElement(staticText);
		writeBox(staticText.getLineBox());
		writeTextElement(staticText);

		writer.writeCDATAElement(JRXmlConstants.ELEMENT_text, staticText.getText());

		writer.closeElement();
	}


	/**
	 *
	 */
	private void writeTextElement(JRTextElement textElement) throws IOException
	{
		writer.startElement(JRXmlConstants.ELEMENT_textElement);
		writer.addAttribute(JRXmlConstants.ATTRIBUTE_textAlignment, textElement.getOwnHorizontalTextAlign());
		VerticalTextAlignEnum vTextAlign = textElement.getOwnVerticalTextAlign();
		if (isOlderVersionThan(JRConstants.VERSION_6_2_1))
		{
			vTextAlign = vTextAlign == VerticalTextAlignEnum.JUSTIFIED ? VerticalTextAlignEnum.TOP : vTextAlign;
		}
		writer.addAttribute(JRXmlConstants.ATTRIBUTE_verticalAlignment, vTextAlign);
		writer.addAttribute(JRXmlConstants.ATTRIBUTE_rotation, textElement.getOwnRotationValue());
		writer.addAttribute(JRXmlConstants.ATTRIBUTE_markup, textElement.getOwnMarkup());

		writeFont(textElement);
		writeParagraph(textElement.getParagraph());

		writer.closeElement(true);
	}


	/**
	 *
	 */
	public void writeFont(JRFont font) throws IOException
	{
		if (font != null)
		{
			writer.startElement(JRXmlConstants.ELEMENT_font, getNamespace());
			writer.addEncodedAttribute(JRXmlConstants.ATTRIBUTE_fontName, font.getOwnFontName());
			writer.addAttribute(JRXmlConstants.ATTRIBUTE_size, font.getOwnFontsize(), true);
			writer.addAttribute(JRXmlConstants.ATTRIBUTE_isBold, font.isOwnBold());
			writer.addAttribute(JRXmlConstants.ATTRIBUTE_isItalic, font.isOwnItalic());
			writer.addAttribute(JRXmlConstants.ATTRIBUTE_isUnderline, font.isOwnUnderline());
			writer.addAttribute(JRXmlConstants.ATTRIBUTE_isStrikeThrough, font.isOwnStrikeThrough());
			writer.addEncodedAttribute(JRXmlConstants.ATTRIBUTE_pdfFontName, font.getOwnPdfFontName());
			writer.addEncodedAttribute(JRXmlConstants.ATTRIBUTE_pdfEncoding, font.getOwnPdfEncoding());
			writer.addAttribute(JRXmlConstants.ATTRIBUTE_isPdfEmbedded, font.isOwnPdfEmbedded());
			writer.closeElement(true);
		}
	}


	/**
	 *
	 */
	
	public void writeTextField(JRTextField textField) throws IOException
	{
		writer.startElement(JRXmlConstants.ELEMENT_textField, getNamespace());
		if(isNewerVersionOrEqual(JRConstants.VERSION_6_11_0))
		{
			writer.addAttribute(JRXmlConstants.ATTRIBUTE_textAdjust, textField.getTextAdjust(), TextAdjustEnum.CUT_TEXT);
		}
		else
		{
			writer.addAttribute(JRXmlConstants.ATTRIBUTE_isStretchWithOverflow, textField.getTextAdjust() == TextAdjustEnum.STRETCH_HEIGHT, false);
		}
		writer.addAttribute(JRXmlConstants.ATTRIBUTE_evaluationTime, textField.getEvaluationTimeValue(), EvaluationTimeEnum.NOW);

		if (textField.getEvaluationGroup() != null)
		{
			writer.addEncodedAttribute(JRXmlConstants.ATTRIBUTE_evaluationGroup, textField.getEvaluationGroup().getName());
		}

		writer.addEncodedAttribute(JRXmlConstants.ATTRIBUTE_pattern, textField.getOwnPattern());
		writer.addAttribute(JRXmlConstants.ATTRIBUTE_isBlankWhenNull, textField.isOwnBlankWhenNull());

		writer.addEncodedAttribute(JRXmlConstants.ATTRIBUTE_hyperlinkType, textField.getLinkType(), HyperlinkTypeEnum.NONE.getName());
		writer.addEncodedAttribute(JRXmlConstants.ATTRIBUTE_hyperlinkTarget, textField.getLinkTarget(), HyperlinkTargetEnum.SELF.getName());
		writer.addAttribute(JRXmlConstants.ATTRIBUTE_bookmarkLevel, textField.getBookmarkLevel(), JRAnchor.NO_BOOKMARK);

		writeReportElement(textField);
		writeBox(textField.getLineBox());
		writeTextElement(textField);

		writer.writeExpression(JRXmlConstants.ELEMENT_textFieldExpression, textField.getExpression());
		
		writer.writeExpression(JRXmlConstants.ELEMENT_patternExpression, textField.getPatternExpression());
		writer.writeExpression(JRXmlConstants.ELEMENT_anchorNameExpression, textField.getAnchorNameExpression());
		if(isNewerVersionOrEqual(JRConstants.VERSION_6_13_0))
		{
			writer.writeExpression(JRXmlConstants.ELEMENT_bookmarkLevelExpression, textField.getBookmarkLevelExpression());
		}
		writer.writeExpression(JRXmlConstants.ELEMENT_hyperlinkReferenceExpression, textField.getHyperlinkReferenceExpression());
		writer.writeExpression(JRXmlConstants.ELEMENT_hyperlinkWhenExpression, textField.getHyperlinkWhenExpression());
		writer.writeExpression(JRXmlConstants.ELEMENT_hyperlinkAnchorExpression, textField.getHyperlinkAnchorExpression());
		writer.writeExpression(JRXmlConstants.ELEMENT_hyperlinkPageExpression, textField.getHyperlinkPageExpression());
		writer.writeExpression(JRXmlConstants.ELEMENT_hyperlinkTooltipExpression, textField.getHyperlinkTooltipExpression());
		writeHyperlinkParameters(textField.getHyperlinkParameters());

		writer.closeElement();
	}


	/**
	 *
	 */
	
	public void writeSubreport(JRSubreport subreport) throws IOException
	{
		writer.startElement(JRXmlConstants.ELEMENT_subreport, getNamespace());
		writer.addAttribute(JRXmlConstants.ATTRIBUTE_isUsingCache, subreport.getUsingCache());
		writer.addAttribute(JRXmlConstants.ATTRIBUTE_runToBottom, subreport.isRunToBottom());
		writer.addAttribute(JRXmlConstants.ATTRIBUTE_overflowType, subreport.getOverflowType());

		writeReportElement(subreport);

		writer.writeExpression(JRXmlConstants.ELEMENT_parametersMapExpression, subreport.getParametersMapExpression());

		/*   */
		JRSubreportParameter[] parameters = subreport.getParameters();
		if (parameters != null && parameters.length > 0)
		{
			for(int i = 0; i < parameters.length; i++)
			{
				writeSubreportParameter(parameters[i], getNamespace());
			}
		}

		writer.writeExpression(JRXmlConstants.ELEMENT_connectionExpression, subreport.getConnectionExpression());
		writer.writeExpression(JRXmlConstants.ELEMENT_dataSourceExpression, subreport.getDataSourceExpression());

		JRSubreportReturnValue[] returnValues = subreport.getReturnValues();
		if (returnValues != null && returnValues.length > 0)
		{
			for(int i = 0; i < returnValues.length; i++)
			{
				writeSubreportReturnValue(returnValues[i], getNamespace());
			}
		}

		writer.writeExpression(JRXmlConstants.ELEMENT_subreportExpression, subreport.getExpression());

		writer.closeElement();
	}


	/**
	 *
	 */
	
	public void writeSubreportParameter(JRSubreportParameter subreportParameter, XmlNamespace namespace) throws IOException
	{
		writer.startElement(JRXmlConstants.ELEMENT_subreportParameter, namespace);
		writer.addEncodedAttribute(JRXmlConstants.ATTRIBUTE_name, subreportParameter.getName());

		writer.writeExpression(JRXmlConstants.ELEMENT_subreportParameterExpression, subreportParameter.getExpression());

		writer.closeElement();
	}


	
	private void writeDatasetParameter(JRDatasetParameter datasetParameter) throws IOException
	{
		writer.startElement(JRXmlConstants.ELEMENT_datasetParameter);
		writer.addEncodedAttribute(JRXmlConstants.ATTRIBUTE_name, datasetParameter.getName());

		writer.writeExpression(JRXmlConstants.ELEMENT_datasetParameterExpression, datasetParameter.getExpression());

		writer.closeElement();
	}


	/**
	 * Writes the JRXML representation of an {@link JRElementDataset element dataset}.
	 * 
	 * <p>
	 * The method produces a <code>&lt;dataset&gt;</code> XML element.
	 * 
	 * @param dataset the element dataset
	 * @throws IOException any I/O exception that occurred while writing the
	 * XML output
	 */
	public void writeElementDataset(JRElementDataset dataset) throws IOException
	{
		writeElementDataset(dataset, DatasetResetTypeEnum.REPORT, true);
	}

	/**
	 * Writes the JRXML representation of an {@link JRElementDataset element dataset}.
	 * 
	 * <p>
	 * The method produces a <code>&lt;dataset&gt;</code> XML element.
	 * 
	 * @param dataset the element dataset
	 * @param skipIfEmpty if set, no output will be produced if the element dataset
	 * only has default attribute values
	 * @throws IOException any I/O exception that occurred while writing the
	 * XML output
	 */
	public void writeElementDataset(JRElementDataset dataset, boolean skipIfEmpty) throws IOException
	{
		writeElementDataset(dataset, DatasetResetTypeEnum.REPORT, skipIfEmpty);
	}
	
	
	/**
	 * Writes the JRXML representation of an {@link JRElementDataset element dataset}.
	 * 
	 * <p>
	 * The method produces a <code>&lt;dataset&gt;</code> XML element.
	 * 
	 * @param dataset the element dataset
	 * @param defaultResetType the default dataset reset type
	 * @param skipIfEmpty if set, no output will be produced if the element dataset
	 * only has default attribute values
	 * @throws IOException any I/O exception that occurred while writing the
	 * XML output
	 */
	
	public void writeElementDataset(JRElementDataset dataset, DatasetResetTypeEnum defaultResetType, 
			boolean skipIfEmpty) throws IOException
	{
		writer.startElement(JRXmlConstants.ELEMENT_dataset, getNamespace());
		writer.addAttribute(JRXmlConstants.ATTRIBUTE_resetType, dataset.getDatasetResetType(), defaultResetType);

		if (dataset.getDatasetResetType() == DatasetResetTypeEnum.GROUP)
		{
			writer.addEncodedAttribute(JRXmlConstants.ATTRIBUTE_resetGroup, dataset.getResetGroup().getName());
		}
		writer.addAttribute(JRXmlConstants.ATTRIBUTE_incrementType, dataset.getIncrementTypeValue(), IncrementTypeEnum.NONE);

		if (dataset.getIncrementTypeValue() == IncrementTypeEnum.GROUP)
		{
			writer.addEncodedAttribute(JRXmlConstants.ATTRIBUTE_incrementGroup, dataset.getIncrementGroup().getName());
		}

		writer.writeExpression(JRXmlConstants.ELEMENT_incrementWhenExpression, dataset.getIncrementWhenExpression());

		JRDatasetRun datasetRun = dataset.getDatasetRun();
		if (datasetRun != null)
		{
			writeDatasetRun(datasetRun);
		}

		writer.closeElement(skipIfEmpty);
	}


	public void writeSubreportReturnValue(JRSubreportReturnValue returnValue, XmlNamespace namespace) throws IOException
	{
		writer.startElement(JRXmlConstants.ELEMENT_returnValue, namespace);
		writer.addEncodedAttribute(JRXmlConstants.ATTRIBUTE_subreportVariable, returnValue.getFromVariable());
		writer.addEncodedAttribute(JRXmlConstants.ATTRIBUTE_toVariable, returnValue.getToVariable());
		writer.addAttribute(JRXmlConstants.ATTRIBUTE_calculation, returnValue.getCalculation(), CalculationEnum.NOTHING);
		writer.addAttribute(JRXmlConstants.ATTRIBUTE_incrementerFactoryClass, returnValue.getIncrementerFactoryClassName());
		writer.closeElement();
	}

	
	protected void writeReturnValue(VariableReturnValue returnValue) throws IOException
	{
		writer.startElement(JRXmlConstants.ELEMENT_returnValue);
		writer.addEncodedAttribute(JRXmlConstants.ATTRIBUTE_fromVariable, returnValue.getFromVariable());
		writer.addEncodedAttribute(JRXmlConstants.ATTRIBUTE_toVariable, returnValue.getToVariable());
		writer.addAttribute(JRXmlConstants.ATTRIBUTE_calculation, returnValue.getCalculation(), CalculationEnum.NOTHING);
		writer.addAttribute(JRXmlConstants.ATTRIBUTE_incrementerFactoryClass, returnValue.getIncrementerFactoryClassName());
		writer.closeElement();
	}


	protected void writeReturnValue(ExpressionReturnValue returnValue) throws IOException
	{
		writer.startElement(JRXmlConstants.ELEMENT_returnValue);
		writer.addEncodedAttribute(JRXmlConstants.ATTRIBUTE_toVariable, returnValue.getToVariable());
		writer.addAttribute(JRXmlConstants.ATTRIBUTE_calculation, returnValue.getCalculation(), CalculationEnum.NOTHING);
		writer.addAttribute(JRXmlConstants.ATTRIBUTE_incrementerFactoryClass, returnValue.getIncrementerFactoryClassName());
		writer.writeExpression(JRXmlConstants.ELEMENT_expression, returnValue.getExpression());
		writer.closeElement();
	}

	
	public void writeCrosstab(JRCrosstab crosstab) throws IOException
	{
		writer.startElement(JRCrosstabFactory.ELEMENT_crosstab, getNamespace());
		writer.addAttribute(JRCrosstabFactory.ATTRIBUTE_isRepeatColumnHeaders, crosstab.isRepeatColumnHeaders(), true);
		writer.addAttribute(JRCrosstabFactory.ATTRIBUTE_isRepeatRowHeaders, crosstab.isRepeatRowHeaders(), true);
		writer.addAttribute(JRCrosstabFactory.ATTRIBUTE_columnBreakOffset, crosstab.getColumnBreakOffset(), JRCrosstab.DEFAULT_COLUMN_BREAK_OFFSET);
		writer.addAttribute(JRXmlConstants.ATTRIBUTE_runDirection, crosstab.getRunDirectionValue(), RunDirectionEnum.LTR);
		writer.addAttribute(JRXmlConstants.ATTRIBUTE_horizontalPosition, crosstab.getHorizontalPosition());
		writer.addAttribute(JRCrosstabFactory.ATTRIBUTE_ignoreWidth, crosstab.getIgnoreWidth());
		writeReportElement(crosstab);
		writeBox(crosstab.getLineBox());

		JRCrosstabParameter[] parameters = crosstab.getParameters();
		if (parameters != null)
		{
			for (int i = 0; i < parameters.length; i++)
			{
				if (!parameters[i].isSystemDefined())
				{
					writeCrosstabParameter(parameters[i]);
				}
			}
		}

		writer.writeExpression(JRCrosstabFactory.ELEMENT_parametersMapExpression, crosstab.getParametersMapExpression());

		writeCrosstabDataset(crosstab);

		writeCrosstabTitle(crosstab);
		
		writeCrosstabHeaderCell(crosstab);

		JRCrosstabRowGroup[] rowGroups = crosstab.getRowGroups();
		for (int i = 0; i < rowGroups.length; i++)
		{
			writeCrosstabRowGroup(rowGroups[i]);
		}

		JRCrosstabColumnGroup[] columnGroups = crosstab.getColumnGroups();
		for (int i = 0; i < columnGroups.length; i++)
		{
			writeCrosstabColumnGroup(columnGroups[i]);
		}

		JRCrosstabMeasure[] measures = crosstab.getMeasures();
		for (int i = 0; i < measures.length; i++)
		{
			writeCrosstabMeasure(measures[i]);
		}

		if (crosstab instanceof JRDesignCrosstab)
		{
			List<JRCrosstabCell> cellsList = ((JRDesignCrosstab) crosstab).getCellsList();
			for (Iterator<JRCrosstabCell> it = cellsList.iterator(); it.hasNext();)
			{
				JRCrosstabCell cell = it.next();
				writeCrosstabCell(cell);
			}
		}
		else
		{
			JRCrosstabCell[][] cells = crosstab.getCells();
			Set<JRCrosstabCell> cellsSet = new HashSet<>();
			for (int i = cells.length - 1; i >= 0 ; --i)
			{
				for (int j = cells[i].length - 1; j >= 0 ; --j)
				{
					JRCrosstabCell cell = cells[i][j];
					if (cell != null && cellsSet.add(cell))
					{
						writeCrosstabCell(cell);
					}
				}
			}
		}

		writeCrosstabWhenNoDataCell(crosstab);

		writer.closeElement();
	}


	private void writeCrosstabDataset(JRCrosstab crosstab) throws IOException
	{
		JRCrosstabDataset dataset = crosstab.getDataset();
		writer.startElement(JRCrosstabDatasetFactory.ELEMENT_crosstabDataset);
		writer.addAttribute(JRCrosstabDatasetFactory.ATTRIBUTE_isDataPreSorted, dataset.isDataPreSorted(), false);
		writeElementDataset(dataset);
		writer.closeElement(true);
	}


	private void writeCrosstabTitle(JRCrosstab crosstab) throws IOException
	{
		CrosstabColumnCell titleCell = crosstab.getTitleCell();
		if (titleCell != null)
		{
			writer.startElement(JRCrosstabFactory.ELEMENT_titleCell);
			writer.addAttribute(JRXmlConstants.ATTRIBUTE_height, titleCell.getHeight());
			writer.addAttribute(JRXmlConstants.ATTRIBUTE_contentsPosition, titleCell.getContentsPosition(), CrosstabColumnPositionEnum.LEFT);
			
			writeCellContents(titleCell.getCellContents());
			
			writer.closeElement();
		}
	}


	private void writeCrosstabWhenNoDataCell(JRCrosstab crosstab) throws IOException
	{
		JRCellContents whenNoDataCell = crosstab.getWhenNoDataCell();
		if (whenNoDataCell != null)
		{
			writer.startElement(JRCrosstabFactory.ELEMENT_whenNoDataCell);
			writeCellContents(whenNoDataCell);
			writer.closeElement();
		}
	}


	private void writeCrosstabHeaderCell(JRCrosstab crosstab) throws IOException
	{
		JRCellContents headerCell = crosstab.getHeaderCell();
		if (headerCell != null)
		{
			writer.startElement(JRCrosstabFactory.ELEMENT_crosstabHeaderCell);
			writeCellContents(headerCell);
			writer.closeElement();
		}
	}


	protected void writeCrosstabRowGroup(JRCrosstabRowGroup group) throws IOException
	{
		writer.startElement(JRCrosstabRowGroupFactory.ELEMENT_rowGroup);
		writer.addEncodedAttribute(JRCrosstabGroupFactory.ATTRIBUTE_name, group.getName());
		writer.addAttribute(JRCrosstabRowGroupFactory.ATTRIBUTE_width, group.getWidth());
		writer.addAttribute(JRCrosstabGroupFactory.ATTRIBUTE_totalPosition, group.getTotalPositionValue(), CrosstabTotalPositionEnum.NONE);
		writer.addAttribute(JRCrosstabRowGroupFactory.ATTRIBUTE_headerPosition, group.getPositionValue(), CrosstabRowPositionEnum.TOP);
		if (isNewerVersionOrEqual(JRConstants.VERSION_6_2_0))
		{
			writer.addAttribute(JRCrosstabGroupFactory.ATTRIBUTE_mergeHeaderCells, group.getMergeHeaderCells());
		}

		writeBucket(group.getBucket());

		JRCellContents header = group.getHeader();
		writer.startElement(JRCrosstabRowGroupFactory.ELEMENT_crosstabRowHeader);
		writeCellContents(header);
		writer.closeElement();

		JRCellContents totalHeader = group.getTotalHeader();
		writer.startElement(JRCrosstabRowGroupFactory.ELEMENT_crosstabTotalRowHeader);
		writeCellContents(totalHeader);
		writer.closeElement();

		writer.closeElement();
	}


	protected void writeCrosstabColumnGroup(JRCrosstabColumnGroup group) throws IOException
	{
		writer.startElement(JRCrosstabGroupFactory.ELEMENT_columnGroup);
		writer.addEncodedAttribute(JRCrosstabGroupFactory.ATTRIBUTE_name, group.getName());
		writer.addAttribute(JRCrosstabColumnGroupFactory.ATTRIBUTE_height, group.getHeight());
		writer.addAttribute(JRCrosstabGroupFactory.ATTRIBUTE_totalPosition, group.getTotalPositionValue(), CrosstabTotalPositionEnum.NONE);
		writer.addAttribute(JRCrosstabColumnGroupFactory.ATTRIBUTE_headerPosition, group.getPositionValue(), CrosstabColumnPositionEnum.LEFT);
		if (isNewerVersionOrEqual(JRConstants.VERSION_6_2_0))
		{
			writer.addAttribute(JRCrosstabGroupFactory.ATTRIBUTE_mergeHeaderCells, group.getMergeHeaderCells());
		}

		writeBucket(group.getBucket());

		JRCellContents crosstabHeader = group.getCrosstabHeader();
		if (crosstabHeader != null)
		{
			writer.startElement(JRCrosstabColumnGroupFactory.ELEMENT_crosstabHeader);
			writeCellContents(crosstabHeader);
			writer.closeElement();
		}
		
		JRCellContents header = group.getHeader();
		writer.startElement(JRCrosstabGroupFactory.ELEMENT_crosstabColumnHeader);
		writeCellContents(header);
		writer.closeElement();

		JRCellContents totalHeader = group.getTotalHeader();
		writer.startElement(JRCrosstabGroupFactory.ELEMENT_crosstabTotalColumnHeader);
		writeCellContents(totalHeader);
		writer.closeElement();

		writer.closeElement();
	}


	
	protected void writeBucket(JRCrosstabBucket bucket) throws IOException
	{
		writer.startElement(JRCrosstabBucketFactory.ELEMENT_bucket);
		writer.addAttribute(JRCrosstabBucketFactory.ATTRIBUTE_order, bucket.getOrder(), BucketOrder.ASCENDING);
		writer.addAttribute(JRCrosstabMeasureFactory.ATTRIBUTE_class, bucket.getValueClassName());
		writer.writeExpression(JRCrosstabBucketFactory.ELEMENT_bucketExpression, bucket.getExpression());
		writer.writeExpression(JRCrosstabBucketFactory.ELEMENT_orderByExpression, bucket.getOrderByExpression());
		writer.writeExpression(JRCrosstabBucketFactory.ELEMENT_comparatorExpression, bucket.getComparatorExpression());

		writer.closeElement();
	}


	
	protected void writeCrosstabMeasure(JRCrosstabMeasure measure) throws IOException
	{
		writer.startElement(JRCrosstabMeasureFactory.ELEMENT_measure);
		writer.addEncodedAttribute(JRCrosstabMeasureFactory.ATTRIBUTE_name, measure.getName());
		writer.addAttribute(JRCrosstabMeasureFactory.ATTRIBUTE_class, measure.getValueClassName());
		writer.addAttribute(JRCrosstabMeasureFactory.ATTRIBUTE_calculation, measure.getCalculationValue(), CalculationEnum.NOTHING);
		writer.addAttribute(JRCrosstabMeasureFactory.ATTRIBUTE_incrementerFactoryClass, measure.getIncrementerFactoryClassName());
		writer.addAttribute(JRCrosstabMeasureFactory.ATTRIBUTE_percentageOf, measure.getPercentageType(), CrosstabPercentageEnum.NONE);
		writer.addAttribute(JRCrosstabMeasureFactory.ATTRIBUTE_percentageCalculatorClass, measure.getPercentageCalculatorClassName());
		writer.writeExpression(JRCrosstabMeasureFactory.ELEMENT_measureExpression, measure.getValueExpression());

		writer.closeElement();
	}


	protected void writeCrosstabCell(JRCrosstabCell cell) throws IOException
	{
		writer.startElement(JRCrosstabCellFactory.ELEMENT_crosstabCell);
		writer.addAttribute(JRCrosstabCellFactory.ATTRIBUTE_width, cell.getWidth());
		writer.addAttribute(JRCrosstabCellFactory.ATTRIBUTE_height, cell.getHeight());
		writer.addEncodedAttribute(JRCrosstabCellFactory.ATTRIBUTE_rowTotalGroup, cell.getRowTotalGroup());
		writer.addEncodedAttribute(JRCrosstabCellFactory.ATTRIBUTE_columnTotalGroup, cell.getColumnTotalGroup());

		writeCellContents(cell.getContents());

		writer.closeElement();
	}


	protected void writeCellContents(JRCellContents contents) throws IOException
	{
		if (contents != null)
		{
			writer.startElement(JRCellContentsFactory.ELEMENT_cellContents);
			writer.addAttribute(JRCellContentsFactory.ATTRIBUTE_backcolor, contents.getBackcolor());
			writer.addAttribute(JRCellContentsFactory.ATTRIBUTE_mode, contents.getModeValue());
			writeStyleReferenceAttr(contents);

			writeProperties(contents);
			writeBox(contents.getLineBox());

			writeChildElements(contents);

			writer.closeElement();
		}
	}


	
	protected void writeCrosstabParameter(JRCrosstabParameter parameter) throws IOException
	{
		writer.startElement(JRCrosstabParameterFactory.ELEMENT_crosstabParameter);
		writer.addEncodedAttribute(JRCrosstabParameterFactory.ATTRIBUTE_name, parameter.getName());
		writer.addAttribute(JRCrosstabParameterFactory.ATTRIBUTE_class, parameter.getValueClassName(), "java.lang.String");
		writer.writeExpression(JRCrosstabParameterFactory.ELEMENT_parameterValueExpression, parameter.getExpression());

		writer.closeElement();
	}


	public void writeDataset(JRDataset dataset) throws IOException
	{
		writer.startElement(JRXmlConstants.ELEMENT_subDataset, getNamespace());
		writer.addEncodedAttribute(JRXmlConstants.ATTRIBUTE_name, dataset.getName());
		writer.addAttribute(JRXmlConstants.ATTRIBUTE_scriptletClass, dataset.getScriptletClass());
		writer.addEncodedAttribute(JRXmlConstants.ATTRIBUTE_resourceBundle, dataset.getResourceBundle());
		writer.addAttribute(JRXmlConstants.ATTRIBUTE_whenResourceMissingType, dataset.getWhenResourceMissingTypeValue(), WhenResourceMissingTypeEnum.NULL);
		if (!isExcludeUuids())
		{
			writer.addAttribute(JRXmlConstants.ATTRIBUTE_uuid, dataset.getUUID().toString());
		}

		writeProperties(dataset);

		if (isNewerVersionOrEqual(JRConstants.VERSION_6_3_1))
		{
			writePropertyExpressions(dataset.getPropertyExpressions());
		}

		writeDatasetContents(dataset);

		writer.closeElement();
	}

	
	protected void writeDatasetContents(JRDataset dataset) throws IOException
	{
		/*   */
		JRScriptlet[] scriptlets = dataset.getScriptlets();
		if (scriptlets != null && scriptlets.length > 0)
		{
			for(int i = 0; i < scriptlets.length; i++)
			{
				writeScriptlet(scriptlets[i]);
			}
		}

		/*   */
		JRParameter[] parameters = dataset.getParameters();
		if (parameters != null && parameters.length > 0)
		{
			for(int i = 0; i < parameters.length; i++)
			{
				if (!parameters[i].isSystemDefined())
				{
					writeParameter(parameters[i]);
				}
			}
		}

		/*   */
		if(dataset.getQuery() != null)
		{
			writeQuery(dataset.getQuery());
		}

		/*   */
		JRField[] fields = dataset.getFields();
		if (fields != null && fields.length > 0)
		{
			for(int i = 0; i < fields.length; i++)
			{
				writeField(fields[i]);
			}
		}

		/*   */
		JRSortField[] sortFields = dataset.getSortFields();
		if (sortFields != null && sortFields.length > 0)
		{
			for(int i = 0; i < sortFields.length; i++)
			{
				writeSortField(sortFields[i]);
			}
		}

		/*   */
		JRVariable[] variables = dataset.getVariables();
		if (variables != null && variables.length > 0)
		{
			for(int i = 0; i < variables.length; i++)
			{
				if (!variables[i].isSystemDefined())
				{
					writeVariable(variables[i]);
				}
			}
		}

		writer.writeExpression(JRXmlConstants.ELEMENT_filterExpression, dataset.getFilterExpression());

		/*   */
		JRGroup[] groups = dataset.getGroups();
		if (groups != null && groups.length > 0)
		{
			for(int i = 0; i < groups.length; i++)
			{
				writeGroup(groups[i]);
			}
		}
	}


	/**
	 * Outputs the XML representation of a subdataset run object.
	 * 
	 * @param datasetRun the subdataset run
	 * @throws IOException
	 */
	
	public void writeDatasetRun(JRDatasetRun datasetRun) throws IOException
	{
		writer.startElement(JRXmlConstants.ELEMENT_datasetRun, getNamespace());
		writer.addEncodedAttribute(JRXmlConstants.ATTRIBUTE_subDataset, datasetRun.getDatasetName());
		if (!isExcludeUuids())
		{
			writer.addEncodedAttribute(JRXmlConstants.ATTRIBUTE_uuid, datasetRun.getUUID().toString());
		}
		writeProperties(datasetRun);

		writer.writeExpression(JRXmlConstants.ELEMENT_parametersMapExpression, datasetRun.getParametersMapExpression());

		/*   */
		JRDatasetParameter[] parameters = datasetRun.getParameters();
		if (parameters != null && parameters.length > 0)
		{
			for(int i = 0; i < parameters.length; i++)
			{
				writeDatasetParameter(parameters[i]);
			}
		}

		writer.writeExpression(JRXmlConstants.ELEMENT_connectionExpression, datasetRun.getConnectionExpression());
		writer.writeExpression(JRXmlConstants.ELEMENT_dataSourceExpression, datasetRun.getDataSourceExpression());

		List<ReturnValue> returnValues = datasetRun.getReturnValues();
		if (returnValues != null && !returnValues.isEmpty())
		{
			for (ReturnValue returnValue : returnValues)
			{
				writeReturnValue(returnValue);
			}
		}
		
		writer.closeElement();
	}


	public void writeFrame(JRFrame frame) throws IOException
	{
		writer.startElement(JRXmlConstants.ELEMENT_frame, getNamespace());
		if (isNewerVersionOrEqual(JRConstants.VERSION_6_0_0))
		{
			writer.addAttribute(JRXmlConstants.ATTRIBUTE_borderSplitType, frame.getBorderSplitType());
		}

		writeReportElement(frame);
		writeBox(frame.getLineBox());

		writeChildElements(frame);

		writer.closeElement();
	}


	public void writeHyperlinkParameters(JRHyperlinkParameter[] parameters) throws IOException
	{
		if (parameters != null)
		{
			for (int i = 0; i < parameters.length; i++)
			{
				JRHyperlinkParameter parameter = parameters[i];
				writeHyperlinkParameter(parameter);
			}
		}
	}


	
	protected void writeHyperlinkParameter(JRHyperlinkParameter parameter) throws IOException
	{
		if (parameter != null)
		{
			writer.startElement(JRXmlConstants.ELEMENT_hyperlinkParameter, JASPERREPORTS_NAMESPACE);
			writer.addEncodedAttribute(JRXmlConstants.ATTRIBUTE_name, parameter.getName());

			writer.writeExpression(JRXmlConstants.ELEMENT_hyperlinkParameterExpression, parameter.getValueExpression());

			writer.closeElement();
		}
	}

	public void writeHyperlink(String tagName, JRHyperlink hyperlink) throws IOException
	{
		writeHyperlink(tagName, null, hyperlink);
	}

	
	public void writeHyperlink(String tagName, XmlNamespace namespace, 
			JRHyperlink hyperlink) throws IOException
	{
		if (hyperlink != null)
		{
			writer.startElement(tagName, namespace);

			writer.addEncodedAttribute(JRXmlConstants.ATTRIBUTE_hyperlinkType, hyperlink.getLinkType(), HyperlinkTypeEnum.NONE.getName());
			writer.addEncodedAttribute(JRXmlConstants.ATTRIBUTE_hyperlinkTarget, hyperlink.getLinkTarget(), HyperlinkTargetEnum.SELF.getName());

			writer.writeExpression(JRXmlConstants.ELEMENT_hyperlinkReferenceExpression, JASPERREPORTS_NAMESPACE,
					hyperlink.getHyperlinkReferenceExpression());
			writer.writeExpression(JRXmlConstants.ELEMENT_hyperlinkWhenExpression, JASPERREPORTS_NAMESPACE,
					hyperlink.getHyperlinkWhenExpression());
			writer.writeExpression(JRXmlConstants.ELEMENT_hyperlinkAnchorExpression, JASPERREPORTS_NAMESPACE,
					hyperlink.getHyperlinkAnchorExpression());
			writer.writeExpression(JRXmlConstants.ELEMENT_hyperlinkPageExpression, JASPERREPORTS_NAMESPACE,
					hyperlink.getHyperlinkPageExpression());
			writer.writeExpression(JRXmlConstants.ELEMENT_hyperlinkTooltipExpression, JASPERREPORTS_NAMESPACE,
					hyperlink.getHyperlinkTooltipExpression());
			writeHyperlinkParameters(hyperlink.getHyperlinkParameters());
			
			writer.closeElement(true);
		}
	}


	@Override
	protected boolean toWriteConditionalStyles()
	{
		return true;
	}

	/**
	 * Returns the XML write helper used by this report writer.
	 * 
	 * The helper can be used to output XML elements and attributes.
	 * 
	 * @return the XML write helper used by this report writer
	 */
	public JRXmlWriteHelper getXmlWriteHelper()
	{
		return writer;
	}

	/**
	 * Returns the underlying stream to which this writer outputs to.
	 * 
	 * @return the underlying stream used by this writer
	 */
	public Writer getUnderlyingWriter()
	{
		return writer.getUnderlyingWriter();
	}

	public void writeComponentElement(JRComponentElement componentElement) throws IOException
	{
		ComponentKey componentKey = componentElement.getComponentKey();
		ComponentXmlWriter componentXmlWriter = 
			ComponentsEnvironment.getInstance(jasperReportsContext).getManager(componentKey).getComponentXmlWriter(jasperReportsContext);
		
		if (componentXmlWriter.isToWrite(componentElement, this))
		{
			writer.startElement(JRXmlConstants.ELEMENT_componentElement, getNamespace());
			writeReportElement(componentElement);
			
			componentXmlWriter.writeToXml(componentElement, this);
			
			writer.closeElement();
		}
	}
	
	public XmlNamespace getNamespace()
	{
		return JASPERREPORTS_NAMESPACE;
	}


	
	public void writeGenericElement(JRGenericElement element) throws IOException
	{
		writer.startElement(JRXmlConstants.ELEMENT_genericElement, getNamespace());
		
		writer.addAttribute(JRXmlConstants.ATTRIBUTE_evaluationTime, 
				element.getEvaluationTimeValue(),
				EvaluationTimeEnum.NOW);
		if (element.getEvaluationGroupName() != null)
		{
			writer.addEncodedAttribute(JRXmlConstants.ATTRIBUTE_evaluationGroup, 
					element.getEvaluationGroupName());
		}

		writeReportElement(element);
		
		writer.startElement(JRXmlConstants.ELEMENT_genericElementType);
		JRGenericElementType printKey = element.getGenericType();
		if (printKey != null)
		{
			writer.addEncodedAttribute(JRXmlConstants.ATTRIBUTE_namespace, 
					printKey.getNamespace());
			writer.addEncodedAttribute(JRXmlConstants.ATTRIBUTE_name, 
					printKey.getName());
		}
		writer.closeElement();//genericElementType

		JRGenericElementParameter[] params = element.getParameters();
		for (int i = 0; i < params.length; i++)
		{
			JRGenericElementParameter param = params[i];
			writer.startElement(JRXmlConstants.ELEMENT_genericElementParameter);
			writer.addEncodedAttribute(JRXmlConstants.ATTRIBUTE_name, 
					param.getName());
			writer.addAttribute(JRXmlConstants.ATTRIBUTE_skipWhenNull, 
					param.isSkipWhenEmpty(), false);
			
			JRExpression valueExpression = param.getValueExpression();
			if (valueExpression != null)
			{
				writer.writeExpression(JRXmlConstants.ELEMENT_genericElementParameter_valueExpression, valueExpression);
			}
			
			writer.closeElement();//genericElementParameter
		}
		
		writer.closeElement();//genericElement
	}
	
	public void writeMultiAxisData(MultiAxisData data) throws IOException
	{
		writer.startElement(JRXmlConstants.ELEMENT_multiAxisData, getNamespace());
		
		writer.startElement(JRXmlConstants.ELEMENT_multiAxisDataset);
		writeElementDataset(data.getDataset());
		writer.closeElement();//JRXmlConstants.ELEMENT_multiAxisDataset
		
		for (DataAxis dataAxis : data.getDataAxisList())
		{
			writer.startElement(JRXmlConstants.ELEMENT_dataAxis);
			writer.addAttribute(JRXmlConstants.ATTRIBUTE_axis, dataAxis.getAxis());
			
			for (DataAxisLevel level : dataAxis.getLevels())
			{
				writer.startElement(JRXmlConstants.ELEMENT_axisLevel);
				writer.addEncodedAttribute(JRXmlConstants.ATTRIBUTE_name, level.getName());
				writer.writeExpression(JRXmlConstants.ELEMENT_labelExpression, level.getLabelExpression());
				writeDataLevelBucket(level.getBucket());
				writer.closeElement();//JRXmlConstants.ELEMENT_axisLevel
			}
			
			writer.closeElement();//JRXmlConstants.ELEMENT_dataAxis
		}
		
		for (DataMeasure measure : data.getMeasures())
		{
			writer.startElement(JRXmlConstants.ELEMENT_multiAxisMeasure);
			writer.addEncodedAttribute(JRXmlConstants.ATTRIBUTE_name, measure.getName());
			writer.addEncodedAttribute(JRXmlConstants.ATTRIBUTE_class, measure.getValueClassName());
			writer.addAttribute(JRXmlConstants.ATTRIBUTE_calculation, measure.getCalculation());
			writer.addAttribute(JRXmlConstants.ATTRIBUTE_incrementerFactoryClass, measure.getIncrementerFactoryClassName());
			writer.writeExpression(JRXmlConstants.ELEMENT_labelExpression, measure.getLabelExpression());
			writer.writeExpression(JRXmlConstants.ELEMENT_valueExpression, measure.getValueExpression());
			writer.closeElement();//JRXmlConstants.ELEMENT_multiAxisMeasure
		}
		
		writer.closeElement();//JRXmlConstants.ELEMENT_multiAxisData
	}

	protected void writeDataLevelBucket(DataLevelBucket bucket) throws IOException
	{
		writer.startElement(JRXmlConstants.ELEMENT_axisLevelBucket);
		writer.addAttribute(JRCrosstabBucketFactory.ATTRIBUTE_order, bucket.getOrder(), BucketOrder.ASCENDING);
		writer.addAttribute(JRCrosstabMeasureFactory.ATTRIBUTE_class, bucket.getValueClassName());
		writer.writeExpression(JRCrosstabBucketFactory.ELEMENT_bucketExpression, bucket.getExpression());
		if(isNewerVersionOrEqual(JRConstants.VERSION_6_4_3))
		{
			writer.writeExpression(JRXmlConstants.ELEMENT_MULTI_AXIS_BUCKET_LABEL_EXPRESSION, bucket.getLabelExpression());
		}
		writer.writeExpression(JRCrosstabBucketFactory.ELEMENT_comparatorExpression, bucket.getComparatorExpression());
		
		List<DataLevelBucketProperty> bucketProperties = bucket.getBucketProperties();
		if (bucketProperties != null)
		{
			for (DataLevelBucketProperty bucketProperty : bucketProperties)
			{
				JRExpression valueExpression = bucketProperty.getExpression();
				String expressionText = valueExpression == null ? "" : valueExpression.getText();
				writer.writeCDATAElement(JRXmlConstants.ELEMENT_bucketProperty, getNamespace(), expressionText, 
						JRXmlConstants.ATTRIBUTE_name, bucketProperty.getName());
			}
		}
		
		writer.closeElement();//JRXmlConstants.ELEMENT_axisLevelBucket
	}
	
	
}
