// $ANTLR 2.7.5 (20050128): "mapping.g" -> "MappingParser.java"$

/*
 * ============================================================================
 * GNU Lesser General Public License
 * ============================================================================
 *
 * JasperReports - Free Java report-generating library.
 * Copyright (C) 2001-2009 JasperSoft Corporation http://www.jaspersoft.com
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
 * 539 Bryant Street, Suite 100
 * San Francisco, CA 94107
 * http://www.jaspersoft.com
 */
package net.sf.jasperreports.olap.mapping;

import java.util.List;
import java.util.ArrayList;

import net.sf.jasperreports.olap.mapping.*;

public interface MappingParserTokenTypes {
	int EOF = 1;
	int NULL_TREE_LOOKAHEAD = 3;
	int LITERAL_Axis = 4;
	int LPAREN = 5;
	int RPAREN = 6;
	int LITERAL_Columns = 7;
	int LITERAL_Rows = 8;
	int LITERAL_Pages = 9;
	int LITERAL_Chapters = 10;
	int LITERAL_Sections = 11;
	int LITERAL_Data = 12;
	int LITERAL_FormattedData = 13;
	int COMMA = 14;
	int QMARK = 15;
	int POINT = 16;
	int INT = 17;
	int MONDRNAME = 18;
	int MONDRIDX = 19;
	int NAME = 20;
	int PLUS = 21;
	int MINUS = 22;
	int STAR = 23;
	int WS = 24;
	int MONDRCH = 25;
	int DIGIT = 26;
	int LETTER = 27;
}
