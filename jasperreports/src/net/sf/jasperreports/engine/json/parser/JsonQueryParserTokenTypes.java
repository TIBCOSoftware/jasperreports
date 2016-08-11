// $ANTLR 2.7.5 (20050128): "json_grammar.g" -> "JsonQueryParser.java"$

/*
 * JasperReports - Free Java Reporting Library.
 * Copyright (C) 2001 - 2016 TIBCO Software Inc. All rights reserved.
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
package net.sf.jasperreports.engine.json.parser;

public interface JsonQueryParserTokenTypes {
	int EOF = 1;
	int NULL_TREE_LOOKAHEAD = 3;
	int PATH = 4;
	int MEMBER = 5;
	int SIMPLE_KEY = 6;
	int COMPLEX_KEY = 7;
	int OBJECT_CONSTRUCTION = 8;
	int ARRAY_INDEX = 9;
	int ARRAY_CONSTRUCTION = 10;
	int ARRAY_SLICE = 11;
	int MULTI_LEVEL_UP = 12;
	int FILTER = 13;
	int DOT = 14;
	int DOTDOT = 15;
	int ID = 16;
	int WILDCARD = 17;
	int LBRACKET = 18;
	int STRING = 19;
	int RBRACKET = 20;
	int COMMA = 21;
	int INT = 22;
	int SEMI = 23;
	int BACKSP = 24;
	int LCURLY = 25;
	int RCURLY = 26;
	int LPAREN = 27;
	int RPAREN = 28;
	int OR = 29;
	int AND = 30;
	int NOT = 31;
	int AT_SIZE = 32;
	int EQ = 33;
	int NE = 34;
	int LT = 35;
	int LE = 36;
	int GT = 37;
	int GE = 38;
	int AT_VALUE = 39;
	int REAL = 40;
	int LITERAL_null = 41;
	int LITERAL_true = 42;
	int LITERAL_false = 43;
	int INT_OR_REAL_OR_DOTS = 44;
	int NEWLINE = 45;
	int WS = 46;
	int DIGIT = 47;
	int FRAC = 48;
	int EXP = 49;
	int ESC = 50;
	int ID_START_LETTER = 51;
	int ID_LETTER = 52;
}
