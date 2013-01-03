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

import java.util.Collection;

import net.sf.jasperreports.engine.JRDefaultScriptlet;
import net.sf.jasperreports.engine.JRScriptletException;


/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id$
 */
public class HeadingsScriptlet extends JRDefaultScriptlet
{


	/**
	 *
	 */
	public Boolean addHeading(String groupName) throws JRScriptletException
	{
		Collection headings = (Collection)this.getVariableValue("HeadingsCollection");

		Integer type = null;
		String text = null;
		String reference = null;
		Integer pageIndex = (Integer)this.getVariableValue("PAGE_NUMBER");
		
		if ("FirstLetterGroup".equals(groupName))
		{
			type = new Integer(1);
			text = "Letter " + this.getVariableValue("FirstLetter");
			reference = "FirstLetterGroup_" + this.getVariableValue("FirstLetter");
		
			headings.add(new HeadingBean(type, text, reference, pageIndex));
		}
		else if ("ShipCountryGroup".equals(groupName))
		{
			type = new Integer(2);
			text = (String)this.getFieldValue("ShipCountry");
			reference = "ShipCountryGroup_" + this.getVariableValue("ShipCountryNumber");
		
			headings.add(new HeadingBean(type, text, reference, pageIndex));
		}
		
		return Boolean.FALSE;
	}


}
