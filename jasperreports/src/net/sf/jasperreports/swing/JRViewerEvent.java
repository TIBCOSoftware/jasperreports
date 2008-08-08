/*
 * ============================================================================
 * GNU Lesser General Public License
 * ============================================================================
 *
 * JasperReports - Free Java report-generating library.
 * Copyright (C) 2001-2006 JasperSoft Corporation http://www.jaspersoft.com
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
package net.sf.jasperreports.swing;

import java.util.EventObject;

import net.sf.jasperreports.engine.JRConstants;

/**
 * @author Lucian Chirita (lucianc@users.sourceforge.net)
 * @version $Id$
 */
public class JRViewerEvent extends EventObject
{

	public static final int EVENT_REPORT_LOADED = 1;
	public static final int EVENT_REFRESH_PAGE = 2;
	public static final int EVENT_PAGE_CHANGED = 3;
	public static final int EVENT_ZOOM_CHANGED = 4;
	public static final int EVENT_FIT_PAGE = 5;
	public static final int EVENT_FIT_WIDTH = 6;
	public static final int EVENT_REPORT_LOAD_FAILED = 7;

	private static final long serialVersionUID = JRConstants.SERIAL_VERSION_UID;

	private final int code;
	
	public JRViewerEvent(JRViewerController controller, int code)
	{
		super(controller);
		
		this.code = code;
	}

	public int getCode()
	{
		return code;
	}

}
