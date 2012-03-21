package net.sf.jasperreports.web.commands;

import net.sf.jasperreports.engine.JasperReportsContext;
import net.sf.jasperreports.engine.ReportContext;
import net.sf.jasperreports.repo.JasperDesignCache;

public class ResetInCacheCommand implements Command 
{
	
	private Command command;
	private JasperReportsContext jasperReportsContext;
	private ReportContext reportContext;
	private String uri;
	
	public ResetInCacheCommand(
		Command command, 
		JasperReportsContext jasperReportsContext, 
		ReportContext reportContext, 
		String uri
		) 
	{
		this.command = command;
		this.jasperReportsContext = jasperReportsContext;
		this.reportContext = reportContext;
		this.uri = uri;
	}

	public void execute() 
	{
		command.execute();
		
		JasperDesignCache.getInstance(jasperReportsContext, reportContext).resetJasperReport(uri);
	}
	
	public void undo() 
	{
		command.undo();
		
		JasperDesignCache.getInstance(jasperReportsContext, reportContext).resetJasperReport(uri);
	}

	public void redo() 
	{
		command.redo();
		
		JasperDesignCache.getInstance(jasperReportsContext, reportContext).resetJasperReport(uri);
	}

}
