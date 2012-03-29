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
import net.sf.jasperreports.repo.JasperDesignCache;
import net.sf.jasperreports.repo.JasperDesignReportResource;
import net.sf.jasperreports.web.actions.AbstractAction;
import net.sf.jasperreports.web.commands.CommandTarget;

/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id: JRAntApiWriteTask.java 4997 2012-02-12 20:54:35Z teodord $
 */
public abstract class AbstractTableAction extends AbstractAction 
{
	
	public AbstractTableAction() {
	}

//	public JRDesignDataset getDataset(String uuid) 
//	{
//		CommandTarget target = getCommandTarget(UUID.fromString(uuid));
//		if (target != null)
//		{
//			JRIdentifiable identifiable = target.getIdentifiable();
//			JRDesignComponentElement componentElement = identifiable instanceof JRDesignComponentElement ? (JRDesignComponentElement)identifiable : null;
//			StandardTable table = componentElement == null ? null : (StandardTable)componentElement.getComponent();
//			
//			JRDesignDatasetRun datasetRun = (JRDesignDatasetRun)table.getDatasetRun();
//			
//			String datasetName = datasetRun.getDatasetName();
//			
//			JasperDesignCache cache = JasperDesignCache.getInstance(getReportContext());
//
//			JasperDesign jasperDesign = cache.getJasperDesign(target.getUri());
//			return (JRDesignDataset)jasperDesign.getDatasetMap().get(datasetName);
//		}
//		return null;
//	}

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

}
