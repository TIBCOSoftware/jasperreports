/*
 * JasperReports - Free Java Reporting Library.
 * Copyright (C) 2001 - 2011 Jaspersoft Corporation. All rights reserved.
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
package net.sf.jasperreports.components.headertoolbar.actions;

import java.util.Map;
import java.util.Set;
import java.util.UUID;

import net.sf.jasperreports.components.table.StandardTable;
import net.sf.jasperreports.engine.JRBand;
import net.sf.jasperreports.engine.JRElement;
import net.sf.jasperreports.engine.JRIdentifiable;
import net.sf.jasperreports.engine.design.JRDesignComponentElement;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.util.DefaultFormatFactory;
import net.sf.jasperreports.engine.util.FormatFactory;
import net.sf.jasperreports.repo.JasperDesignCache;
import net.sf.jasperreports.repo.JasperDesignReportResource;
import net.sf.jasperreports.web.actions.AbstractAction;
import net.sf.jasperreports.web.actions.ActionException;
import net.sf.jasperreports.web.commands.CommandTarget;

/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id$
 */
public abstract class AbstractVerifiableTableAction extends AbstractAction 
{
	protected BaseColumnData columnData;
	
	protected StandardTable table;
	protected String targetUri;
	
	protected static FormatFactory formatFactory = new DefaultFormatFactory();
	
	public AbstractVerifiableTableAction()
	{
	}
	
	@Override
	public String getMessagesBundle() {
		return "net.sf.jasperreports.components.headertoolbar.actions.messages";
	}

	public StandardTable getTable(String uuid) 
	{
		CommandTarget target = getCommandTarget(UUID.fromString(uuid));
		if (target != null)
		{
			JRIdentifiable identifiable = target.getIdentifiable();
			JRDesignComponentElement componentElement = identifiable instanceof JRDesignComponentElement ? (JRDesignComponentElement)identifiable : null;
			return componentElement == null ? null : (StandardTable)componentElement.getComponent();
		}
		return null;
	}

	/**
	 * 
	 */
	public CommandTarget getCommandTarget(UUID uuid)
	{
		JasperDesignCache cache = JasperDesignCache.getInstance(getJasperReportsContext(), getReportContext());

		Map<String, JasperDesignReportResource> cachedResources = cache.getCachedResources();
		Set<String> uris = cachedResources.keySet();
		for (String uri : uris)
		{
			CommandTarget target = new CommandTarget();
			target.setUri(uri);
			
			JasperDesign jasperDesign = cache.getJasperDesign(uri);
			
			//FIXMEJIVE now we just look for table components in title and summary bands
			// this is strongly hardcoded to allow the reports in the webapp-repo sample to work
			JRBand[] bands = new JRBand[]{jasperDesign.getTitle(), jasperDesign.getSummary()};
			for (JRBand band : bands)
			{
				if (band != null)
				{
					for (JRElement element : band.getElements())
					{
						if (element instanceof JRDesignComponentElement) 
						{
							if (uuid.equals(element.getUUID()))
							{
								target.setIdentifiable(element);
								return target;
							}
						}
					}
				}
			}
		}
		return null;
	}
	
	public void prepare() throws ActionException 
	{
		if (columnData == null) { 
			errors.addAndThrow("net.sf.jasperreports.components.headertoolbar.actions.validate.no.data");
		}
		if(columnData.getTableUuid() == null || columnData.getTableUuid().trim().length() == 0) {
			errors.addAndThrow("net.sf.jasperreports.components.headertoolbar.actions.validate.no.table");
		}
		CommandTarget target = getCommandTarget(UUID.fromString(columnData.getTableUuid()));
		if (target != null)
		{
			JRIdentifiable identifiable = target.getIdentifiable();
			JRDesignComponentElement componentElement = identifiable instanceof JRDesignComponentElement ? (JRDesignComponentElement)identifiable : null;
			
			if (componentElement == null) {
				errors.addAndThrow("net.sf.jasperreports.components.headertoolbar.actions.validate.no.table.match", new Object[] {columnData.getTableUuid()});
			}
			
			table = (StandardTable)componentElement.getComponent();
			targetUri = target.getUri();
		}
	}
	
	@Override
	public void run() throws ActionException 
	{
		prepare();
		verify();
		errors.throwAll();
		performAction();
	}
	
	public abstract void verify() throws ActionException;

}
