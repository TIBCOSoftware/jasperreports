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
package net.sf.jasperreports.engine;


/**
 * An interface implemented by objects upon which style attributes can be set.
 * 
 * @author Lucian Chirita (lucianc@users.sourceforge.net)
 * @version $Id$
 * @see JRAbstractObjectFactory#setStyle(JRStyleSetter, JRStyleContainer)
 */
public interface JRStyleSetter
{

	/**
	 * Set the style on the object.
	 * 
	 * @param style the style to be used by the object
	 */
	void setStyle(JRStyle style);
	
	/**
	 * Set the name of an external style that is to be used by the object.
	 * 
	 * @param name the name of an external style
	 */
	void setStyleNameReference(String name);
	
	/**
	 * Delayed set of the style on the object.
	 * <p/>
	 * This method can be invoked after the object has requested its style to be set.
	 * It is currently used to set the external style on an object after resolving it.
	 * 
	 * @param style the style to be used by the object
	 */
	void setStyleDelayed(JRStyle style);

}
