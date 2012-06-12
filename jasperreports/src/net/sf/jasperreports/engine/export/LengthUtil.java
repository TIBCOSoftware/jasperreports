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
package net.sf.jasperreports.engine.export;



/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id$
 */
public final class LengthUtil 
{

	/**
	 * 
	 */
	public static double inch(double pixels)
	{
		double inches = 0.0;
		inches = pixels/72.0;
		inches = (Math.floor(inches * 100.0))/100.0;
		return inches;
	}
	
	/**
	 * 
	 */
	public static double inchRound(double pixels)
	{
		double inches = 0.0;
		inches = pixels/72.0;
		inches = (Math.round(inches * 100.0))/100.0;
		return inches;
	}

	/**
	 * 
	 */
	public static double inchNoRound(double pixels)
	{
		double inches = 0.0;
		inches = pixels/72.0;
		return inches;
	}
	
	/**
	 * Convert a float value to twips (multiply with 20)
	 * @param points value that need to be converted
	 * @return converted value in twips
	 */
	public static int twip(float points) 
	{
		return (int)(points * 20);
	}

	/**
	 * Convert an int value from points to EMU (multiply with 12700)
	 * @param points value that needs to be converted
	 * @return converted value in EMU
	 */
	public static int emu(float points) 
	{
		return (int)(points * 12700);
	}
	
	/**
	 * 
	 */
	public static int halfPoint(float pixels) 
	{
		return (int)(pixels * 8);
	}

	
	private LengthUtil()
	{
	}

}
