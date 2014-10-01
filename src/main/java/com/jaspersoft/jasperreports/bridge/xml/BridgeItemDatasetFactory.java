/*
 * iReport - Visual Designer for JasperReports.
 * Copyright (C) 2002 - 2009 Jaspersoft Corporation. All rights reserved.
 * http://www.jaspersoft.com
 *
 * Unless you have purchased a commercial license agreement from Jaspersoft,
 * the following license terms apply:
 *
 * This program is part of iReport.
 *
 * iReport is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * iReport is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with iReport. If not, see <http://www.gnu.org/licenses/>.
 */
package com.jaspersoft.jasperreports.bridge.xml;

import net.sf.jasperreports.engine.design.JRDesignElementDataset;
import net.sf.jasperreports.engine.xml.JRElementDatasetFactory;

/**
 *
 * @author Giulio Toffoli (gtoffoli@tibco.com)
 */
public class BridgeItemDatasetFactory extends JRElementDatasetFactory
{

	@Override
	public JRDesignElementDataset getDataset()
	{
		return new JRDesignElementDataset();
	}

}
