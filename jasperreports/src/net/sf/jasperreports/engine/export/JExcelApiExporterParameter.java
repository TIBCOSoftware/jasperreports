/*
 * ============================================================================
 *                   GNU Lesser General Public License
 * ============================================================================
 *
 * JasperReports - Free Java report-generating library.
 * Copyright (C) 2001-2003 Teodor Danciu teodord@hotmail.com
 * 
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307, USA.
 * 
 * JasperSoft Corporation
 * 303 Second Street, Suite 450 North
 * San Francisco, CA 94107
 * http://www.jaspersoft.com
 */

/*
 * Contributor: Manuel Paul <mpaul@ratundtat.com>, 
 *				Rat & Tat Beratungsgesellschaft mbH, 
 *				Muehlenkamp 6c,
 *				22303 Hamburg,
 *				Germany.
 */
package net.sf.jasperreports.engine.export;

import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.util.JRProperties;



/**
 * @author Manuel Paul (mpaul@ratundtat.com)
 * @version $Id$
 */
public class JExcelApiExporterParameter extends JRXlsAbstractExporterParameter {

	protected JExcelApiExporterParameter(String name) {
		super(name);
	}

	/**
	 * A boolean value specifying whether the standard color palette should be customized
	 * so that the XLS result uses the original report colors.
	 * <p/>
	 * The default state of this flag is given by the
	 * {@link #PROPERTY_CREATE_CUSTOM_PALETTE net.sf.jasperreports.export.xls.create.custom.palette} property.
	 * <p/>
	 * The colors used in the result XLS are determined in the following manner:
	 * <ol>
	 * 	<li>
	 * If this flag is not set, the nearest color from the standard XLS palette is chosen
	 * for a report color.
	 * 	</li>
	 * 	<li>
	 * If the flag is set, the nearest not yet modified color from the palette is chosen
	 * and modified to exactly match the report color.  If all the colors from the palette
	 * are modified (the palette has a fixed size), the nearest color from the palette is
	 * chosen for further report colors.
	 * 	</li>
	 * </ol> 
	 * 
	 * @see #PROPERTY_CREATE_CUSTOM_PALETTE
	 */
	public static final JRExporterParameter CREATE_CUSTOM_PALETTE = new JExcelApiExporterParameter("Create Custom Palette");

	/**
	 * Property whose value is used as default state of the {@link #CREATE_CUSTOM_PALETTE CREATE_CUSTOM_PALETTE} export flag.
	 * <p/>
	 * This property is by default not set (<code>false</code>).
	 * 
	 * @see JRProperties
	 */
	public static final String PROPERTY_CREATE_CUSTOM_PALETTE = JRProperties.PROPERTY_PREFIX + "export.xls.create.custom.palette";

}
