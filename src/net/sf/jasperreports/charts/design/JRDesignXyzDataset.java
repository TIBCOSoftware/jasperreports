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
 * 185, Berry Street, Suite 6200
 * San Francisco CA 94107
 * http://www.jaspersoft.com
 */

package net.sf.jasperreports.charts.design;

import net.sf.jasperreports.charts.JRXyzDataset;
import net.sf.jasperreports.charts.JRXyzSeries;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.design.JRDesignChartDataset;
import java.util.List;
import java.util.ArrayList;

/**
 * @author Flavius Sana (flavius_sana@users.sourceforge.net)
 * @version $Id$ 
 */
public class JRDesignXyzDataset extends JRDesignChartDataset implements JRXyzDataset {
	
	private static final long serialVersionUID = 608;
	
	private List xyzSeriesList = new ArrayList();
	
	public JRXyzSeries[] getSeries(){
		JRXyzSeries[] xyzSeriesArray = new JRXyzSeries[ xyzSeriesList.size() ];
		xyzSeriesList.toArray( xyzSeriesArray );
		
		return xyzSeriesArray;
	}
	
	public void addXyzSeries( JRXyzSeries xyzSeries ) throws JRException {
		xyzSeriesList.add( xyzSeries );
	}
	
	public JRXyzSeries removeXyzSeries( JRXyzSeries xyzSeries ) throws JRException {
		if( xyzSeries != null ){
			xyzSeriesList.remove( xyzSeries );
		}
		
		return xyzSeries;
	}
	

}
