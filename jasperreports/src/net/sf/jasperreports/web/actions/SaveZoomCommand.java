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

import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.export.type.ZoomTypeEnum;
import net.sf.jasperreports.web.commands.Command;

/**
 * @author Narcis Marcu (narcism@users.sourceforge.net)
 */
public class SaveZoomCommand implements Command {

	public static final String PROPERTY_VIEWER_ZOOM = "net.sf.jasperreports.viewer.zoom";

	private String zoomValue;
	private String oldZoomValue;
	private JasperDesign jasperDesign;

	public SaveZoomCommand(JasperDesign jasperDesign, String zoomValue) {
		this.zoomValue = zoomValue;
		this.jasperDesign = jasperDesign;
	}


	public void execute() {
		oldZoomValue = jasperDesign.getProperty(PROPERTY_VIEWER_ZOOM);
		if (oldZoomValue == null) {
			oldZoomValue = "1";
		}
		ZoomTypeEnum zoomType = ZoomTypeEnum.getByName(zoomValue);
		if (zoomType != null) {
			zoomValue = zoomType.getName();
		}
		jasperDesign.setProperty(PROPERTY_VIEWER_ZOOM, zoomValue);
	}


	public void undo() {
		jasperDesign.setProperty(PROPERTY_VIEWER_ZOOM, oldZoomValue);
	}


	public void redo() {
		jasperDesign.setProperty(PROPERTY_VIEWER_ZOOM, zoomValue);
	}

}
