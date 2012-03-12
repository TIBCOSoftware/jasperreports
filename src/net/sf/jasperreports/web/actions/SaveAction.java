package net.sf.jasperreports.web.actions;

import java.io.File;
import java.util.Map;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRRuntimeException;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.util.JRSaver;
import net.sf.jasperreports.repo.JasperDesignCache;
import net.sf.jasperreports.repo.JasperDesignReportResource;



public class SaveAction extends AbstractAction {

	public SaveAction() {
	}

	public String getName() {
		return "save_action";
	}

	public void performAction() 
	{
//		JasperDesign jasperDesign = getJasperDesign();
		JasperDesignCache cache = JasperDesignCache.getInstance(getJasperReportsContext(), getReportContext());
		Map<String, JasperDesignReportResource> cachedResources = cache.getCachedResources();
		for (String uri : cachedResources.keySet())
		{
			JasperDesignReportResource resource = cachedResources.get(uri);
			JasperDesign jasperDesign = resource.getJasperDesign();
			if (jasperDesign != null)
			{
				JasperReport jasperReport = resource.getReport();
				String appRealPath = null;//FIXMECONTEXT WebFileRepositoryService.getApplicationRealPath();
				try
				{
					JRSaver.saveObject(jasperReport, new File(new File(new File(appRealPath), "WEB-INF/repository"), uri));//FIXMEJIVE harcoded
				}
				catch (JRException e)
				{
					throw new JRRuntimeException(e);
				}
			}
		}
	}

}
