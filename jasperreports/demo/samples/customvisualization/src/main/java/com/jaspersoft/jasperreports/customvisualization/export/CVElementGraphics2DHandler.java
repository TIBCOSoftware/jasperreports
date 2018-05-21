/*******************************************************************************
 * Copyright (C) 2005 - 2016 TIBCO Software Inc. All rights reserved.
 * http://www.jaspersoft.com.
 * 
 * Unless you have purchased  a commercial license agreement from Jaspersoft,
 * the following license terms  apply:
 * 
 * The Custom Visualization Component program and the accompanying materials
 * has been dual licensed under the the following licenses:
 * 
 * Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * The Custom Visualization Component is distributed in the hope that it will be
 * useful, but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License.
 * If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/
package com.jaspersoft.jasperreports.customvisualization.export;

import java.awt.Graphics2D;

import net.sf.jasperreports.engine.JRGenericPrintElement;
import net.sf.jasperreports.engine.export.GenericElementGraphics2DHandler;
import net.sf.jasperreports.engine.export.JRGraphics2DExporter;
import net.sf.jasperreports.engine.export.JRGraphics2DExporterContext;
import net.sf.jasperreports.engine.export.draw.ImageDrawer;
import net.sf.jasperreports.engine.export.draw.Offset;

/**
 * @author Giulio Toffoli (gtoffoli@tibco.com)
 */
public class CVElementGraphics2DHandler implements GenericElementGraphics2DHandler
{
	private static final CVElementGraphics2DHandler INSTANCE = new CVElementGraphics2DHandler();

	public static CVElementGraphics2DHandler getInstance()
	{
		return INSTANCE;
	}

	@Override
	public void exportElement(
		JRGraphics2DExporterContext exporterContext,
		JRGenericPrintElement element,
		Graphics2D grx,
		Offset offset
		)
	{
		try
		{
			JRGraphics2DExporter exporter = (JRGraphics2DExporter) exporterContext.getExporterRef();
			ImageDrawer imageDrawer = exporter.getDrawVisitor().getImageDrawer();

			imageDrawer.draw(
				grx,
				CVElementImageProvider.getDefaultProvider().getImage(exporterContext.getJasperReportsContext(), element, true),
				offset.getX(),
				offset.getY()
				);
		}
		catch (Exception e)
		{
			throw new RuntimeException(e);
		}
	}

	@Override
	public boolean toExport(JRGenericPrintElement element)
	{
		return true;
	}
}
