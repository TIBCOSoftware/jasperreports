/*
 * ============================================================================
 * GNU Lesser General Public License
 * ============================================================================
 *
 * JasperReports - Free Java report-generating library.
 * Copyright (C) 2001-2005 JasperSoft Corporation http://www.jaspersoft.com
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
package net.sf.jasperreports.charts.util;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import net.sf.jasperreports.engine.JRConstants;


/**
 * @author Flavius Sana (flavius_sana@users.sourceforge.net)
 * @version $Id$
 */
public class XYZElement implements Serializable {

	private static final long serialVersionUID = JRConstants.SERIAL_VERSION_UID;
	
	private Comparable series = null;
	private List xElements = null;
	private List yElements = null;
	private List zElements = null;
	
	public XYZElement(){
		xElements = new ArrayList();
		yElements = new ArrayList();
		zElements = new ArrayList();
	}

	public void setSeries( Comparable series ){
		this.series = series;
	}

	public Comparable getSeries(){
		return series;
	}
	
	public void addElement( Number xElement, Number yElement, Number zElement ){
		xElements.add( xElement );
		yElements.add( yElement );
		zElements.add( zElement );
		
	}

	
	public Number getXElement( int index ){
		return (Number)xElements.get( index );
	}
	
	public Number getYElement( int index ){
		return (Number)yElements.get( index );
	}
	
	public Number getZElement( int index ){
		return (Number)zElements.get( index );
	}
	
	
	public int getCount(){
		int retVal = 0;
		if( xElements != null ){
			retVal = xElements.size();
		}
		
		return retVal;
	}

}
