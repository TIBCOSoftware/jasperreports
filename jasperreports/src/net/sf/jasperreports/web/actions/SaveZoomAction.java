/*
 * JasperReports - Free Java Reporting Library.
 * Copyright (C) 2001 - 2014 TIBCO Software Inc. All rights reserved.
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
package net.sf.jasperreports.web.actions;

import java.util.Map;
import java.util.Set;

import net.sf.jasperreports.engine.JasperReportsContext;
import net.sf.jasperreports.engine.ReportContext;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.repo.JasperDesignCache;
import net.sf.jasperreports.repo.JasperDesignReportResource;
import net.sf.jasperreports.web.commands.CommandException;
import net.sf.jasperreports.web.commands.CommandTarget;
import net.sf.jasperreports.web.commands.ResetInCacheCommand;
import net.sf.jasperreports.web.util.WebUtil;

/**
 * @author Narcis Marcu (narcism@users.sourceforge.net)
 */
public class SaveZoomAction extends AbstractAction {

	private String zoomValue;

	public String getZoomValue() {
		return zoomValue;
	}

	public void setZoomValue(String zoomValue) {
		this.zoomValue = zoomValue;
	}

	@Override
	public void performAction() throws ActionException {
		if (zoomValue != null && zoomValue.length() > 0) {
			CommandTarget target = getCommandTarget(getJasperReportsContext(), getReportContext());
			if (target != null) {
				// execute command
				try {
					getCommandStack().execute(
						new ResetInCacheCommand(
								new SaveZoomCommand((JasperDesign)target.getIdentifiable(), zoomValue),
								getJasperReportsContext(),
								getReportContext(),
								target.getUri()
						)
					);
				} catch (CommandException e) {
					throw new ActionException(e);
				}
			}
		} else {
			errors.addAndThrow("net.sf.jasperreports.web.actions.empty.zoom");
		}
	}

	@Override
	public boolean requiresRefill() {
		return false;
	}

	public CommandTarget getCommandTarget(JasperReportsContext jasperReportsContext, ReportContext reportContext) {
		JasperDesignCache cache = JasperDesignCache.getInstance(jasperReportsContext, reportContext);
		Map<String, JasperDesignReportResource> cachedResources = cache.getCachedResources();

		Set<String> uris = cachedResources.keySet();
		String reportUri = (String) reportContext.getParameterValue(WebUtil.REQUEST_PARAMETER_REPORT_URI);

		if (reportUri != null) {
			for (String uri : uris) {
				if (reportUri.equals(uri)) {
					CommandTarget target = new CommandTarget();
					target.setUri(uri);
					target.setIdentifiable(cache.getJasperDesign(uri));
					return target;
				}
			}
		}

		return null;
	}

}