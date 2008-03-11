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
package net.sf.jasperreports.view;

import java.io.File;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.swing.filechooser.FileFilter;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperPrint;

/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id$
 */
public abstract class JRSaveContributor extends FileFilter
{
	private Locale locale = null;
	private ResourceBundle resourceBundle = null;
	
	/**
	 * 
	 */
	public JRSaveContributor()
	{
		this(null, null);
	}
	
	/**
	 * 
	 */
	public JRSaveContributor(Locale locale, ResourceBundle resBundle)
	{
		if (locale != null)
			this.locale = locale;
		else
			this.locale = Locale.getDefault();

		if (resBundle == null)
		{
			this.resourceBundle = ResourceBundle.getBundle("net/sf/jasperreports/view/viewer", this.locale);
		}
		else
		{
			this.resourceBundle = resBundle;
		}
	}
	
	
	/**
	 * 
	 */
	protected String getBundleString(String key)
	{
		return resourceBundle.getString(key);
	}

	
	/**
	 * 
	 */
	public abstract void save(JasperPrint jasperPrint, File file) throws JRException;


}
