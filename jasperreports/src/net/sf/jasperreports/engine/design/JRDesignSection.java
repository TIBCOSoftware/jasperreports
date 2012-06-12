/*
 * JasperReports - Free Java Reporting Library.
 * Copyright (C) 2001 - 2011 Jaspersoft Corporation. All rights reserved.
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
package net.sf.jasperreports.engine.design;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.sf.jasperreports.engine.JRBand;
import net.sf.jasperreports.engine.JRConstants;
import net.sf.jasperreports.engine.JROrigin;
import net.sf.jasperreports.engine.base.JRBaseSection;


/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id$
 */
public class JRDesignSection extends JRBaseSection
{

	/**
	 *
	 */
	private static final long serialVersionUID = JRConstants.SERIAL_VERSION_UID;

	public static final String PROPERTY_BANDS = "bands";
	
	protected List<JRBand> bandsList = new ArrayList<JRBand>();

	private JROrigin origin;

	
	/**
	 * 
	 */
	public JRDesignSection(JROrigin origin) 
	{
		this.origin = origin;
	}
	
	/**
	 * Returns the section origin, i.e. its location/role within the report
	 * (e.g. detail/title/group header/etc).
	 * The location is automatically set when the section is inserted
	 * into the report (via one of the
	 * {@link JasperDesign#setPageHeader(JRBand)}/
	 * methods).
	 * 
	 * @return the section origin
	 */
	public JROrigin getOrigin()
	{
		return origin;
	}

	/**
	 *
	 */
	protected void setOrigin(JROrigin origin)
	{
		this.origin = origin;

		if (bandsList != null && bandsList.size() > 0)
		{
			for (Iterator<JRBand> it = bandsList.iterator(); it.hasNext();)
			{
				JRDesignBand band = (JRDesignBand)it.next();
				band.setOrigin(origin);
			}
		}
	}

	/**
	 *
	 */
	public JRBand[] getBands()
	{
		JRBand[] bandsArray = new JRBand[bandsList.size()];

		bandsList.toArray(bandsArray);

		return bandsArray;
	}

	/**
	 * Gets a list of all bands within the current section.
	 */
	public List<JRBand> getBandsList()
	{
		return bandsList;
	}
		
	/**
	 * Adds a band to the section.

	 * @param band the band to be added
	 */
	public void addBand(JRBand band)
	{
		((JRDesignBand)band).setOrigin(getOrigin());

		bandsList.add(band);
		
		getEventSupport().fireCollectionElementAddedEvent(PROPERTY_BANDS, band, bandsList.size() - 1);
	}

	/**
	 * Adds a band to the section.

	 * @param index the zero based index of the band to be added
	 * @param band the band to be added
	 */
	public void addBand(int index, JRBand band)
	{
		((JRDesignBand)band).setOrigin(getOrigin());

		bandsList.add(index, band);
		
		getEventSupport().fireCollectionElementAddedEvent(PROPERTY_BANDS, band, index);
	}

	/**
	 * Removes a band from the section.
	 * 
	 * @param band the band to be removed
	 * @return the band to be removed
	 */
	public JRBand removeBand(JRBand band)
	{
		if (band != null)
		{
			int idx = bandsList.indexOf(band);
			if (idx >= 0)
			{
				bandsList.remove(idx);
				getEventSupport().fireCollectionElementRemovedEvent(PROPERTY_BANDS, band, idx);
			}
		}

		return band;
	}

	/**
	 * Removes a band from the section.
	 * 
	 * @param index the index of the band to be removed
	 * @return the band to be removed
	 */
	public JRBand removeBand(int index)
	{
		JRBand band = bandsList.remove(index);

		getEventSupport().fireCollectionElementRemovedEvent(PROPERTY_BANDS, band, index);

		return band;
	}

}
