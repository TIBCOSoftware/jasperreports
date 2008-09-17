package net.sf.jasperreports.ohloh;

import net.sf.jasperreports.engine.JRGenericPrintElement;
import net.sf.jasperreports.engine.export.GenericElementHtmlHandler;
import net.sf.jasperreports.engine.export.JRHtmlExporterContext;

public class OhlohWidgetHtmlHandler implements
		GenericElementHtmlHandler
{

	private String projectIDParameter;
	private String widgetName;
	
	public String getWidgetName()
	{
		return widgetName;
	}

	public void setWidgetName(String widgetName)
	{
		this.widgetName = widgetName;
	}

	public String getProjectIDParameter()
	{
		return projectIDParameter;
	}

	public void setProjectIDParameter(String projectIDParameter)
	{
		this.projectIDParameter = projectIDParameter;
	}

	public boolean toExport(JRGenericPrintElement element)
	{
		return getProjectID(element) != null;
	}

	protected Integer getProjectID(JRGenericPrintElement element)
	{
		return (Integer) element.getParameterValue(getProjectIDParameter());
	}

	public String getHtmlFragment(JRHtmlExporterContext context, JRGenericPrintElement element)
	{
		StringBuffer script = new StringBuffer(128);
		script.append("<script type=\"text/javascript\" src=\"http://www.ohloh.net/projects/");
		script.append(getProjectID(element));
		script.append("/widgets/");
		script.append(getWidgetName());
		script.append("\"></script>");
		return script.toString();
	}
	
}
