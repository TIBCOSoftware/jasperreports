/*
 * JasperReports - Free Java Reporting Library.
 * Copyright (C) 2001 - 2025 Cloud Software Group, Inc. All rights reserved.
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
package net.sf.jasperreports.engine.util;

/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @deprecated Replaced by {@link EditorKitMarkupProcessor}.
 */
public abstract class JEditorPaneMarkupProcessor extends EditorKitMarkupProcessor
{

	/**
	 * 
	 */
	public static final class RtfFactory implements MarkupProcessorFactory
	{ 
		@Override
		public MarkupProcessor createMarkupProcessor()
		{
			return JEditorPaneRtfMarkupProcessor.getInstance();
		}
	}

	/**
	 * 
	 */
	public static final class HtmlFactory implements MarkupProcessorFactory
	{ 
		@Override
		public MarkupProcessor createMarkupProcessor()
		{
			return JEditorPaneHtmlMarkupProcessor.getInstance();
		}
	}

}
