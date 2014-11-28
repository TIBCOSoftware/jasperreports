/*
 * JasperReports - Free Java Reporting Library.
 * Copyright (C) 2001 - 2014 TIBCO Software Inc. All rights reserved.
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
package net.sf.jasperreports.engine;


/**
 * An abstract representation of a report static text. It provides functionality for static texts.
 * <p/>
 * Static texts are text elements with fixed content, which does not change during the
 * report-filling process. They are used mostly to introduce static text labels into the
 * generated documents.
 * <p/>
 * Besides the general element properties and the common text-specific properties, a static text 
 * definition has only the <code>&lt;text&gt;</code> tag, which introduces the fixed text content 
 * of the static text element. The text content can be collected using the {@link #getText()} method.
 * 
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 */
public interface JRStaticText extends JRTextElement
{


	/**
	 *
	 */
	public String getText();

	/**
	 *
	 */
	public void setText(String text);


}
