/*
 * JasperReports - Free Java Reporting Library.
 * Copyright (C) 2001 - 2013 Jaspersoft Corporation. All rights reserved.
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
package net.sf.jasperreports.engine.export;

import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import net.sf.jasperreports.engine.DefaultJasperReportsContext;
import net.sf.jasperreports.engine.JRAbstractExporter;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRPrintElement;
import net.sf.jasperreports.engine.JRPrintFrame;
import net.sf.jasperreports.engine.JRPrintHyperlink;
import net.sf.jasperreports.engine.JRPrintPage;
import net.sf.jasperreports.engine.JRPrintText;
import net.sf.jasperreports.engine.JRPropertiesMap;
import net.sf.jasperreports.engine.JRPropertiesUtil;
import net.sf.jasperreports.engine.JasperReportsContext;
import net.sf.jasperreports.engine.type.EnumUtil;
import net.sf.jasperreports.engine.type.JREnum;
import net.sf.jasperreports.export.ExporterInputItem;
import net.sf.jasperreports.export.JsonExporterConfiguration;
import net.sf.jasperreports.export.JsonReportConfiguration;
import net.sf.jasperreports.export.WriterExporterOutput;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.io.JsonStringEncoder;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;


/**
 * @author Narcis Marcu (narcism@users.sourceforge.net)
 * @version $Id$
 */
public class JsonMetadataExporter extends JRAbstractExporter<JsonReportConfiguration, JsonExporterConfiguration, WriterExporterOutput, JsonExporterContext>
{

	private static final Log log = LogFactory.getLog(JsonMetadataExporter.class);

	public static final String JSON_EXPORTER_KEY = JRPropertiesUtil.PROPERTY_PREFIX + "json";

	protected static final String JSON_EXPORTER_PROPERTIES_PREFIX = JRPropertiesUtil.PROPERTY_PREFIX + "export.json.";
	protected static final String JSON_EXPORTER_PATH_PROPERTY = JSON_EXPORTER_PROPERTIES_PREFIX + "path";
	protected static final String JSON_EXPORTER_SCHEMA_PROPERTY = JSON_EXPORTER_PROPERTIES_PREFIX + "schema";

	private static final String JSON_SCHEMA_ROOT_NAME = "___root";

	protected Writer writer;
	protected int reportIndex;
	protected int pageIndex;

	private Map<String, SchemaNode> pathToValueNode = new HashMap<String, SchemaNode>();
	private Map<String, SchemaNode> pathToObjectNode = new HashMap<String, SchemaNode>();

	private List<NodeTypeEnum> openedNodes = new ArrayList<NodeTypeEnum>();

	private String jsonSchema;
	private String previousPath;
	private boolean gotSchema;

	public void validateSchema(String jsonSchema) throws JRException {
		ObjectMapper mapper = new ObjectMapper();

		// relax the JSON rules
		mapper.configure(JsonParser.Feature.ALLOW_SINGLE_QUOTES, true);
		mapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);

		try {
			JsonNode root = mapper.readTree(jsonSchema);
			if (root.isObject()) {
				pathToValueNode = new HashMap<String, SchemaNode>();
				pathToObjectNode = new HashMap<String, SchemaNode>();

				openedNodes = new ArrayList<NodeTypeEnum>();
				previousPath = null;

				if (!isValid((ObjectNode) root, JSON_SCHEMA_ROOT_NAME, "", null)) {
					throw new JRException("Invalid JSON object provided: semantically invalid!");
				}
			} else {
				throw new JRException("Invalid JSON object provided: expected object, received array!");
			}

		} catch (IOException e) {
			throw new JRException("Invalid JSON object provided!");
		}
	}

	private boolean isValid(ObjectNode objectNode, String objectName, String currentPath, SchemaNode parent) {
		JsonNode typeNode = objectNode.path("_type");

		// type is mandatory and must be text
		if (typeNode.isMissingNode() || !typeNode.isTextual()) {
			return false;
		}

		NodeTypeEnum nodeType = NodeTypeEnum.getByName(typeNode.asText());

		// enforce type "object" or "array" with "_children"
		if (!(NodeTypeEnum.OBJECT.equals(nodeType) || (NodeTypeEnum.ARRAY.equals(nodeType) && objectNode.has("_children")))) {
			return false;
		}

		// enforce "_children" of type Object when typeNode is "array"
		if (NodeTypeEnum.ARRAY.equals(nodeType) && !objectNode.path("_children").isObject()) {
			return false;
		}

		boolean result = true;
		String availablePath = currentPath;
		SchemaNode schemaNode;

		availablePath = availablePath.length() > 0 ? (availablePath.endsWith(".") ? availablePath : availablePath + ".") + objectName : objectName;

		// _children properties are passed to the parent array
		if (parent != null) {
			schemaNode = parent;
		} else {

			int level;

			if (JSON_SCHEMA_ROOT_NAME.equals(objectName)) {
				level = 0;
			} else if (availablePath.length() > 0 && availablePath.indexOf(".") > 0) {
				level = availablePath.split("\\.").length - 1;
			} else {
				level = 1;
			}

			schemaNode = new SchemaNode(level, objectName, nodeType, currentPath);
			pathToObjectNode.put(availablePath, schemaNode);
		}

		Iterator<String> it = objectNode.fieldNames();
		while (it.hasNext()) {
			String field = it.next();
			JsonNode node = objectNode.path(field);
			String localPath = availablePath;

			if (!field.startsWith("_")) {
				schemaNode.addMember(field);
				if (node.isTextual() && node.asText().equals("value")) {
					localPath = localPath.length() > 0 ? (localPath.endsWith(".") ? localPath : localPath + ".") + field : field;
					pathToValueNode.put(localPath, schemaNode);
				} else if ((node.isObject() && !isValid((ObjectNode) node, field, availablePath, null)) || !node.isObject()) {
					result = false;
					break;
				}
			} else if (field.equals("_children") && !isValid((ObjectNode) node, "", availablePath, schemaNode)) {
				result = false;
				break;
			}
		}

		if (log.isDebugEnabled()) {
			log.debug("object is valid: " + objectNode);
			log.debug("objectName: " + objectName);
			log.debug("currentPath: " + currentPath);
		}

		return result;
	}

	public JsonMetadataExporter()
	{
		this(DefaultJasperReportsContext.getInstance());
	}

	public JsonMetadataExporter(JasperReportsContext jasperReportsContext)
	{
		super(jasperReportsContext);

		exporterContext = new ExporterContext();
	}


	/**
	 *
	 */
	protected Class<JsonExporterConfiguration> getConfigurationInterface()
	{
		return JsonExporterConfiguration.class;
	}


	/**
	 *
	 */
	protected Class<JsonReportConfiguration> getItemConfigurationInterface()
	{
		return JsonReportConfiguration.class;
	}
	

	/**
	 *
	 */
	@Override
	@SuppressWarnings("deprecation")
	protected void ensureOutput()
	{
		if (exporterOutput == null)
		{
			exporterOutput = 
				new net.sf.jasperreports.export.parameters.ParametersWriterExporterOutput(
					getJasperReportsContext(),
					getParameters(),
					getCurrentJasperPrint()
					);
		}
	}
	

	@Override
	public String getExporterKey()
	{
		return JSON_EXPORTER_KEY;
	}

	@Override
	public String getExporterPropertiesPrefix()
	{
		return JSON_EXPORTER_PROPERTIES_PREFIX;
	}

	@Override
	public void exportReport() throws JRException
	{
		/*   */
		ensureJasperReportsContext();
		ensureInput();

		//FIXMENOW check all exporter properties that are supposed to work at report level
		
		initExport();
		
		ensureOutput();

		writer = getExporterOutput().getWriter();

		try
		{
			exportReportToWriter();
		}
		catch (IOException e)
		{
			throw new JRException("Error writing to output writer : " + jasperPrint.getName(), e);
		}
		finally
		{
			getExporterOutput().close();
			resetExportContext();//FIXMEEXPORT check if using same finally is correct; everywhere
		}
	}
	
	@Override
	protected void initExport()
	{
		super.initExport();
	}
	
	@Override
	protected void initReport()
	{
		super.initReport();
	}
	
	protected void exportReportToWriter() throws JRException, IOException
	{
		List<ExporterInputItem> items = exporterInput.getItems();

		for(reportIndex = 0; reportIndex < items.size(); reportIndex++)
		{
			ExporterInputItem item = items.get(reportIndex);

			setCurrentExporterInputItem(item);

			jsonSchema = jasperPrint.getProperty(JSON_EXPORTER_SCHEMA_PROPERTY);

			if (jsonSchema != null) {
				validateSchema(jsonSchema);
				gotSchema = true;
			} else {
				log.warn("No JSON Schema provided!");
			}

			List<JRPrintPage> pages = jasperPrint.getPages();
			if (pages != null && pages.size() > 0)
			{
				PageRange pageRange = getPageRange();
				int startPageIndex = (pageRange == null || pageRange.getStartPageIndex() == null) ? 0 : pageRange.getStartPageIndex();
				int endPageIndex = (pageRange == null || pageRange.getEndPageIndex() == null) ? (pages.size() - 1) : pageRange.getEndPageIndex();

				JRPrintPage page = null;
				for(pageIndex = startPageIndex; pageIndex <= endPageIndex; pageIndex++)
				{
					if (Thread.interrupted())
					{
						throw new JRException("Current thread interrupted.");
					}

					page = pages.get(pageIndex);

					exportPage(page);
				}

				closeOpenNodes();
			}

			if (log.isDebugEnabled()) {
				for (Map.Entry<String, SchemaNode> entry: pathToValueNode.entrySet()) {
					log.debug("pathToValueNode: path: " + entry.getKey() + "; node: " + entry.getValue());
				}

				for (Map.Entry<String, SchemaNode> entry: pathToObjectNode.entrySet()) {
					log.debug("pathToObjectNode: path: " + entry.getKey() + "; node: " + entry.getValue());
				}
			}
		}

		boolean flushOutput = getCurrentConfiguration().isFlushOutput();
		if (flushOutput)
		{
			writer.flush();
		}
	}
	
	protected void exportPage(JRPrintPage page) throws IOException
	{
		Collection<JRPrintElement> elements = page.getElements();

		exportElements(elements);

		JRExportProgressMonitor progressMonitor = getCurrentItemConfiguration().getProgressMonitor();
		if (progressMonitor != null)
		{
			progressMonitor.afterPageExport();
		}
	}


	protected void exportElements(Collection<JRPrintElement> elements) throws IOException
	{
		if (elements != null && elements.size() > 0)
		{
			for(Iterator<JRPrintElement> it = elements.iterator(); it.hasNext();)
			{
				JRPrintElement element = it.next();

				if (filter == null || filter.isToExport(element))
				{
					exportElement(element);
				}
			}
		}
	}

	public void exportElement(JRPrintElement element) throws IOException
	{
		if (filter == null || filter.isToExport(element))
		{
			if (element instanceof JRPrintText)
			{
				exportText((JRPrintText)element);
			}
			else if (element instanceof JRPrintFrame)
			{
				exportElements(((JRPrintFrame) element).getElements());
			}
		}
	}

	protected void exportText(JRPrintText printText) throws IOException {
		JRPropertiesMap propMap = printText.getPropertiesMap();

		if (propMap.containsProperty(JSON_EXPORTER_PATH_PROPERTY)) {
			String propertyPath = propMap.getProperty(JSON_EXPORTER_PATH_PROPERTY);

			if (propertyPath.length() > 0) {
				String currentPath = JSON_SCHEMA_ROOT_NAME + "." + propertyPath;

				// we have a mapped node for this path
				if (gotSchema && pathToValueNode.containsKey(currentPath)) {

					if (log.isDebugEnabled()) {
						log.debug("found element with path: " + propertyPath);
					}

					processTextElement(printText, propertyPath, currentPath);
				} else if (!gotSchema) {
					prepareSchema(currentPath);
					processTextElement(printText, propertyPath, currentPath);
				}
			}
		}
	}

	private void prepareSchema(String currentPath) {
		if (!pathToValueNode.containsKey(currentPath)) {
			String valueProperty = currentPath.substring(currentPath.lastIndexOf(".") + 1);
			String[] objectPathSegments = currentPath.substring(0, currentPath.lastIndexOf(".")).split("\\.");
			SchemaNode node = null;

			for (int i = 0; i < objectPathSegments.length; i++) {
				StringBuilder objectPath = new StringBuilder(objectPathSegments[0]);
				for (int j = 1; j <= i; j++) {
					objectPath.append(".").append(objectPathSegments[j]);
				}

				if (!pathToObjectNode.containsKey(objectPath.toString())) {
					String schemaNodePath = "";

					for (int k = 0; k < i; k++) {
						schemaNodePath += schemaNodePath.length() > 0 ? "." + objectPathSegments[k] : objectPathSegments[k];
					}

					node = new SchemaNode(i, objectPathSegments[i], NodeTypeEnum.ARRAY, schemaNodePath);

					pathToObjectNode.put(objectPath.toString(), node);
				} else {
					node = pathToObjectNode.get(objectPath.toString());
				}
			}

			node.addMember(valueProperty);
			pathToValueNode.put(currentPath, node);
		}
	}

	private void processTextElement(JRPrintText printText, String propertyPath, String currentPath) throws IOException {
		if (openedNodes.size() == 0) {
			// initialize the json for the first time
			initJson(currentPath, printText.getValue());
		} else {
			String valueProperty = propertyPath.indexOf(".") > 0 ? propertyPath.substring(propertyPath.lastIndexOf(".") + 1) : propertyPath;

			String[] curSegments = currentPath.substring(0, currentPath.lastIndexOf(".")).split("\\.");
			String[] prevSegments = previousPath.substring(0, previousPath.lastIndexOf(".")).split("\\.");

			List<String> commonSegments = new ArrayList<String>();

			int ln = Math.min(curSegments.length, prevSegments.length);
			int lastCommonIndex = -1;

			for (int i = 0; i < ln; i++) {
				if (curSegments[i].equals(prevSegments[i])) {
					commonSegments.add(curSegments[i]);
					lastCommonIndex = i;
				} else {
					break;
				}
			}

			// compared to previous path, we have different path with common segments
			if (commonSegments.size() < prevSegments.length) {
				if (log.isDebugEnabled()) {
					log.debug("found different paths with common segments");
				}

				// close the extra path segments of the previous path
				for (int i = prevSegments.length - 1; i > lastCommonIndex; i--) {
					StringBuilder sb = new StringBuilder(prevSegments[0]);
					for (int j=1; j <= i; j++) {
						sb.append(".").append(prevSegments[j]);
					}

					SchemaNode toClose = pathToObjectNode.get(sb.toString());
					if (toClose.isObject()) {
						writer.write("},\n");
						openedNodes.remove(openedNodes.size()-1);
					} else {
						writer.write("}],\n");
						openedNodes.remove(openedNodes.size()-1);
						openedNodes.remove(openedNodes.size()-1);
					}
				}

				// open new path segments for the current path
				for (int i = lastCommonIndex + 1; i < curSegments.length; i++) {
					StringBuilder sb = new StringBuilder(curSegments[0]);
					for (int j=1; j <= i; j++) {
						sb.append(".").append(curSegments[j]);
					}

					writer.write(curSegments[i] + ":");

					SchemaNode toOpen = pathToObjectNode.get(sb.toString());
					if (toOpen.isObject()) {
						writer.write("{");
						openedNodes.add(NodeTypeEnum.OBJECT);
					} else {
						writer.write("[{");
						openedNodes.add(NodeTypeEnum.ARRAY);
						openedNodes.add(NodeTypeEnum.OBJECT);
					}
				}
			}
			// we have a longer path that extends previous path
			else if (commonSegments.size() == prevSegments.length && curSegments.length > prevSegments.length) {
				if (log.isDebugEnabled()) {
					log.debug("found longer path than previous one");
				}

				writer.write(",\n");

				// open new paths
				for (int i = lastCommonIndex + 1; i < curSegments.length; i++) {
					StringBuilder sb = new StringBuilder(curSegments[0]);
					for (int j=1; j <= i; j++) {
						sb.append(".").append(curSegments[j]);
					}

					writer.write(curSegments[i] + ":");

					SchemaNode toOpen = pathToObjectNode.get(sb.toString());
					if (toOpen.isObject()) {
						writer.write("{");
						openedNodes.add(NodeTypeEnum.OBJECT);
					} else {
						writer.write("[{");
						openedNodes.add(NodeTypeEnum.ARRAY);
						openedNodes.add(NodeTypeEnum.OBJECT);
					}
				}
			}
			// have the same path
			else if (commonSegments.size() == prevSegments.length) {
				if (log.isDebugEnabled()) {
					log.debug("found same path");
				}

				SchemaNode currentNode = pathToValueNode.get(currentPath);
				String prevValProp = previousPath.indexOf(".") > 0 ? previousPath.substring(previousPath.lastIndexOf(".") + 1) : previousPath;

				int valPropIdx = currentNode.indexOfMember(valueProperty);
				int prevValPropIdx = currentNode.indexOfMember(prevValProp);

				// the property is after the previous one => same object
				if (valPropIdx > prevValPropIdx) {
					writer.write(", \n");
				} else {
					writer.write("},\n{");
				}
			}

			writer.write(valueProperty + ":");
			writeValue(printText.getValue() != null ? printText.getValue() : printText.getFullText());
		}

		previousPath = currentPath;
	}

	private void closeOpenNodes() throws IOException {
		if (openedNodes == null) {
			return;
		}
		for (int i = openedNodes.size() - 1; i >= 0; i--) {
			if (NodeTypeEnum.OBJECT.equals(openedNodes.get(i))) {
				writer.write("}");
			} else {
				writer.write("]");
			}
		}
	}

	private void initJson(String firstPath, Object firstValue) throws IOException {
		if (log.isDebugEnabled()) {
			log.debug("Initializing JSON with first path: " + firstPath);
		}
		String[] segments = firstPath.split("\\.");

		String currentPath = "";
		SchemaNode n;
		int i;

		for (i=0; i < segments.length - 1; i++) {
			currentPath = currentPath.length() > 0 ? currentPath + "." + segments[i] : segments[i];
			n = pathToObjectNode.get(currentPath);

			if (i == 0) { // got root node
				if (n.isObject()) {
					writer.write("{");
					openedNodes.add(NodeTypeEnum.OBJECT);
				} else {
					writer.write("[{");
					openedNodes.add(NodeTypeEnum.ARRAY);
					openedNodes.add(NodeTypeEnum.OBJECT);
				}
			} else {
				if (n.isObject()) {
					writer.write(segments[i] + ":" + "{");
					openedNodes.add(NodeTypeEnum.OBJECT);
				} else {
					writer.write(segments[i] + ":" + "[{");
					openedNodes.add(NodeTypeEnum.ARRAY);
					openedNodes.add(NodeTypeEnum.OBJECT);
				}
			}
		}

		writer.write(segments[i] + ":");
		writeValue(firstValue);
	}

	private void writeValue(Object value)throws IOException {
		if (value != null) {
			boolean isString = value instanceof String;
			if (isString) {
				writer.write("\"");
				writer.write(JsonStringEncoder.getInstance().quoteAsString((String)value));
				writer.write("\"");
			} else {
				writer.write(value.toString());
			}
		} else {
			writer.write("null");  // TODO: how to treat null values?
		}
	}

	protected class ExporterContext extends BaseExporterContext implements JsonExporterContext
	{
		@Override
		public String getHyperlinkURL(JRPrintHyperlink link)
		{
			return ""; // FIXME should we treat hyperlinks?
		}
	}


	private class SchemaNode {
		private int level;
		private String name;
		private NodeTypeEnum type;
		private String path;
		private List<String> members;

		public SchemaNode(int _level, String _name, NodeTypeEnum _type, String _path) {
			level = _level;
			name = _name;
			type = _type;
			path = _path;
			members = new ArrayList<String>();

		}

		public void addMember(String member) {
			members.add(member);
		}

		public boolean isObject() {
			return NodeTypeEnum.OBJECT.equals(type);
		}

		public int indexOfMember(String member) {
			return members.indexOf(member);
		}

		@Override
		public String toString() {
			StringBuilder out = new StringBuilder("{");
			boolean isArray = NodeTypeEnum.ARRAY.equals(type);

			out.append("level: ").append(level).append(", ");
			out.append("name: \"").append(name).append("\", ");
			out.append("type: \"").append(type.getName()).append("\", ");
			out.append("path: \"").append(path).append("\", ");
			out.append("members: [");
			if (isArray) {
				out.append("{");
			}
			for (int i=0, ln = members.size(); i < ln; i++) {
				out.append("\"").append(members.get(i)).append("\"");
				if (i < ln-1) {
					out.append(", ");
				}
			}
			if (isArray) {
				out.append("}");
			}
			out.append("]}");
			return out.toString();
		}

		@Override
		public boolean equals(Object obj) {
			return this.level == ((SchemaNode)obj).level
					&& this.name.equals(((SchemaNode)obj).name)
					&& this.type.equals(((SchemaNode)obj).type)
					&& this.path.equals(((SchemaNode)obj).path);
		}

		@Override
		public int hashCode() {
			int hash = level !=0 ? level : 41;
			hash = hash * 41 + name.hashCode();
			hash = hash * 41 + type.hashCode();
			hash = hash * 41 + path.hashCode();
			return hash;
		}
	}


	private enum NodeTypeEnum implements JREnum{

		/**
		 *
		 */
		OBJECT((byte) 0, "object"),

		/**
		 *
		 */
		ARRAY((byte) 1, "array");

		private final byte value;
		private final String name;

		private NodeTypeEnum(byte _value, String _name) {
			value = _value;
			name = _name;
		}

		public Byte getValueByte()
		{
			return new Byte(value);
		}

		public byte getValue() {
			return value;
		}

		public String getName() {
			return name;
		}

		public static NodeTypeEnum getByName(String name)
		{
			return (NodeTypeEnum) EnumUtil.getByName(values(), name);
		}
	}

}
