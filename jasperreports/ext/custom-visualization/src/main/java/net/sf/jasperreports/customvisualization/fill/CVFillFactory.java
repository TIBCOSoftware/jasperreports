/*
 * JasperReports - Free Java Reporting Library.
 * Copyright (C) 2001 - 2019 TIBCO Software Inc. All rights reserved.
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
package net.sf.jasperreports.customvisualization.fill;

import java.io.Serializable;

import net.sf.jasperreports.customvisualization.CVComponent;
import net.sf.jasperreports.customvisualization.CVConstants;
import net.sf.jasperreports.engine.JRPropertiesUtil;
import net.sf.jasperreports.engine.component.Component;
import net.sf.jasperreports.engine.component.ComponentFillFactory;
import net.sf.jasperreports.engine.component.FillComponent;
import net.sf.jasperreports.engine.fill.JRFillCloneFactory;
import net.sf.jasperreports.engine.fill.JRFillObjectFactory;

public class CVFillFactory implements ComponentFillFactory, Serializable
{
	private static final long serialVersionUID = CVConstants.SERIAL_VERSION_UID;

	@Override
	public FillComponent toFillComponent(Component component, JRFillObjectFactory factory)
	{
		JRPropertiesUtil properties = JRPropertiesUtil.getInstance(factory.getFiller().getJasperReportsContext());
		boolean generateImage = properties.getBooleanProperty(CVConstants.PROPERTY_GENERATE_IMAGE);

		CVComponent cvComponent = (CVComponent) component;
		FillComponent fillComponent = generateImage ?
				new CVFillImage(cvComponent, factory) :
				new CVFillComponent(cvComponent, factory);

		return fillComponent;
	}

	@Override
	public FillComponent cloneFillComponent(FillComponent component, JRFillCloneFactory factory)
	{
		throw new UnsupportedOperationException();
	}

}
