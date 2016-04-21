/*******************************************************************************
 * Copyright (C) 2005 - 2014 TIBCO Software Inc. All rights reserved.
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

import net.sf.jasperreports.engine.JRGenericPrintElement;
import net.sf.jasperreports.engine.JRPrintImage;
import net.sf.jasperreports.engine.JRRuntimeException;
import net.sf.jasperreports.engine.export.GenericElementPdfHandler;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.engine.export.JRPdfExporterContext;

/**
 *
 * @author Giulio Toffoli (gtoffoli@tibco.com)
 */
public class CVElementPdfHandler implements GenericElementPdfHandler
{
	private static final CVElementPdfHandler INSTANCE = new CVElementPdfHandler();

	public static CVElementPdfHandler getInstance()
	{
		return INSTANCE;
	}

	@Override
	public void exportElement(
		JRPdfExporterContext exporterContext,
		JRGenericPrintElement element
		)
	{
		try
		{
			JRPdfExporter exporter = (JRPdfExporter) exporterContext.getExporterRef();
			JRPrintImage printImage = 
				CVElementImageProvider.getDefaultProvider()
					.getImage(exporterContext.getJasperReportsContext(), element, true);
			exporter.exportImage(printImage);
		}
		catch (JRRuntimeException ex)
		{
			throw ex;
		}
		catch (Exception ex)
		{
			throw new JRRuntimeException(ex);
		}
	}

	@Override
	public boolean toExport(JRGenericPrintElement element)
	{
		return true;
	}
}
