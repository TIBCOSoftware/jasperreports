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
package net.sf.jasperreports.engine.util;

import java.text.AttributedString;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id$
 */
public class JRStyledText
{

	/**
	 *
	 */
	private StringBuffer sbuffer = new StringBuffer();
	private List runs = new ArrayList();
	private AttributedString attributedString = null;

	
	/**
	 *
	 */
	public JRStyledText()
	{
	}


	/**
	 *
	 */
	public void append(String text)
	{
		sbuffer.append(text);
		attributedString = null;
	}

	/**
	 *
	 */
	public void addRun(Run run)
	{
		runs.add(run);
		attributedString = null;
	}

	/**
	 *
	 */
	public int length()
	{
		return sbuffer.length();
	}

	/**
	 *
	 */
	public String getText()
	{
		return sbuffer.toString();
	}

	/**
	 *
	 */
	public AttributedString getAttributedString()
	{
		if (attributedString == null)
		{
			attributedString = new AttributedString(sbuffer.toString());

			for(int i = runs.size() - 1; i >= 0; i--)
			{
				Run run = (Run)runs.get(i);
				if (run.startIndex != run.endIndex)
				{
					attributedString.addAttributes(run.attributes, run.startIndex, run.endIndex);
				}
			}
		}
		
		return attributedString;
	}


	/**
	 *
	 */
	public List getRuns()
	{
		return runs;
	}

	/**
	 * 
	 */
	public static class Run 
	{
		/**
		 *
		 */
		public Map attributes = null;
		public int startIndex = 0;
		public int endIndex = 0;

		/**
		 *
		 */
		public Run(Map attributes, int startIndex, int endIndex) 
		{
			this.attributes = attributes;
			this.startIndex = startIndex;
			this.endIndex = endIndex;
		}
	}
	
}
