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


header
{
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
}


/**
 * @author Narcis Marcu (narcism@users.sourceforge.net)
 */
class JsonQueryParser extends Parser;

options {
    buildAST=true;
    k=2;
    defaultErrorHandler=false;
}

tokens {
    PATH;
    MEMBER;

    SIMPLE_KEY;
    COMPLEX_KEY;
    OBJECT_CONSTRUCTION;
    ARRAY_INDEX;
    ARRAY_CONSTRUCTION;
    ARRAY_SLICE;
    MULTI_LEVEL_UP;

    FILTER;
}

pathExpr
    : (memberExpr)* EOF!
        { #pathExpr = #([PATH, "Path Expr:"], #pathExpr); }
    ;

memberExpr
    : pathNaviExpr (filterExprMain)?
        { #memberExpr = #([MEMBER, "Member expression:"], #memberExpr); }
    ;

pathNaviExpr
    : ( (DOT | DOTDOT)? (ID | WILDCARD ) ) => simpleKeyExpr
    | ( (DOTDOT)? LBRACKET STRING RBRACKET) => complexKeyExpr
    | ( (DOTDOT)? LBRACKET (STRING | ID) COMMA (STRING | ID) ) => objectConstructionExpr
    | ( (DOTDOT)? LBRACKET INT RBRACKET ) => arrayExpr
    | ( (DOTDOT)? LBRACKET INT COMMA INT ) => arrayConstructionExpr
    | arraySliceExpr
    | multiLevelUpExpr
    ;

simpleKeyExpr
    : (DOT | DOTDOT)? (ID | WILDCARD )
        { #simpleKeyExpr = #([SIMPLE_KEY, "Simple expression:"], #simpleKeyExpr); }
    ;

complexKeyExpr
    : (DOTDOT)? LBRACKET! STRING RBRACKET!
        { #complexKeyExpr = #([COMPLEX_KEY, "Complex expression:"], #complexKeyExpr); }
    ;

objectConstructionExpr
    : (DOTDOT)? LBRACKET! (STRING | ID) (COMMA! (STRING | ID))+ RBRACKET!
       { #objectConstructionExpr = #([OBJECT_CONSTRUCTION, "Object construction expression:"], #objectConstructionExpr); }
    ;

arrayExpr
    : (DOTDOT)? LBRACKET! INT RBRACKET!
        { #arrayExpr = #([ARRAY_INDEX, "Array expression:"], #arrayExpr); }
    ;

arrayConstructionExpr
    : (DOTDOT)? LBRACKET! INT (COMMA! INT)+ RBRACKET!
        { #arrayConstructionExpr = #([ARRAY_CONSTRUCTION, "Array construction expression:"], #arrayConstructionExpr); }
    ;

arraySliceExpr
    : (DOTDOT)? LBRACKET! ( (INT SEMI (INT)?) | (SEMI INT) ) RBRACKET!
        { #arraySliceExpr = #([ARRAY_SLICE, "Array slice expression:"], #arraySliceExpr); }
    ;

multiLevelUpExpr
    : BACKSP (LCURLY! INT RCURLY!)?
        { #multiLevelUpExpr = #([MULTI_LEVEL_UP, "Multi level up:"], #multiLevelUpExpr); }
    ;


filterExprMain
    : LPAREN! filterExpr RPAREN!
        { #filterExprMain = #([FILTER, "Filter expression main:"], #filterExprMain); }
    ;

filterExpr
    : andExpr (OR^ andExpr)*
    ;

andExpr
    : notExpr (AND^ notExpr)*
    ;

notExpr
    : (NOT^)? basicExpr
    ;

basicExpr
    : filterNaviExpr
    | LPAREN! filterExpr RPAREN!
    ;

filterNaviExpr
    : (pathNaviExpr)+ (sizeFnExpr | operator_to_value)
    ;

sizeFnExpr
    : AT_SIZE (EQ | NE | LT | LE | GT | GE) INT
    ;

operator_to_value
    : (EQ | NE ) value
    | (LT | LE | GT | GE) (INT | REAL)
    ;

value
    : "null"
    | "true"
    | "false"
    | STRING
    | INT
    | REAL
    ;


{
import net.sf.jasperreports.engine.json.expression.JsonPathExpression;
import net.sf.jasperreports.engine.json.expression.filter.FilterExpression.LOGICAL_OPERATOR;
import net.sf.jasperreports.engine.json.expression.filter.FilterExpression.VALUE_TYPE;
import net.sf.jasperreports.engine.json.expression.filter.*;
import net.sf.jasperreports.engine.json.expression.member.MemberExpression.DIRECTION;
import net.sf.jasperreports.engine.json.expression.member.*;

import net.sf.jasperreports.engine.type.JsonOperatorEnum;
}

/**
 * @author Narcis Marcu (narcism@users.sourceforge.net)
 */
class JsonQueryWalker extends TreeParser;

jsonPathExpression returns [JsonPathExpression jsonPathExpression = new JsonPathExpression()]
    : #(PATH (memberExpr[jsonPathExpression])*)
    ;

memberExpr [JsonPathExpression jsonPathExpression]
        {
            MemberExpression memberExpr = null;
            FilterExpression filterExpression = null;
        }
    : #(MEMBER
            memberExpr=pathNaviExpr (filterExpression = filterExprMain)?
      )
        {
            memberExpr.setFilterExpression(filterExpression);
            jsonPathExpression.addMemberExpression(memberExpr);
        }
    ;

pathNaviExpr returns [MemberExpression memberExpr = null]
        {
            DIRECTION dir = DIRECTION.DOWN;
        }
    : #(SIMPLE_KEY (dir=direction)? (id:ID | WILDCARD ))
        {
            if (id != null) {
                // object key
                memberExpr = new ObjectKeyExpression(dir, id.getText());
            } else {
                // wildcard
                memberExpr = new ObjectKeyExpression(dir);
            }
        }
    | #(COMPLEX_KEY (dir=direction)? s:STRING)
        {
            memberExpr = new ObjectKeyExpression(dir, s.getText());
        }
    |   {
            memberExpr = new ObjectConstructionExpression();
        }
      #(OBJECT_CONSTRUCTION (dir=direction)? (addObjectKey[(ObjectConstructionExpression)memberExpr])+)
        {
            ((ObjectConstructionExpression)memberExpr).setDirection(dir);
        }
    | #(ARRAY_INDEX (dir=direction)? n:INT)
        {
            memberExpr = new ArrayIndexExpression(dir, Integer.parseInt(n.getText()));
        }
    |   {
            memberExpr = new ArrayConstructionExpression();
        }
      #(ARRAY_CONSTRUCTION (dir=direction)? (addArrayIndex[(ArrayConstructionExpression)memberExpr])+)
        {
            ((ArrayConstructionExpression)memberExpr).setDirection(dir);
        }
    | #(ARRAY_SLICE (dir=direction)? (sliceStart:INT)? SEMI (sliceEnd:INT)?)
        {
            Integer start = null, end = null;

            if (sliceStart != null) {
                start = Integer.valueOf(sliceStart.getText());
            }
            if (sliceEnd != null) {
                end = Integer.valueOf(sliceEnd.getText());
            }

            memberExpr = new ArraySliceExpression(dir, start, end);
        }
    | #(MULTI_LEVEL_UP BACKSP (levelUp:INT)?)
        {
            int level = 1;
            if (levelUp != null) {
                level = Integer.parseInt(levelUp.getText());
            }
            memberExpr = new MultiLevelUpExpression(level);
        }
    ;

addObjectKey [ObjectConstructionExpression objConstrExpr]
    : (str:STRING | id:ID)
        {
            String key = null;
            if (str != null) {
                key = str.getText();
            } else {
                key = id.getText();
            }
            objConstrExpr.addKey(key);
        }
    ;

addArrayIndex [ArrayConstructionExpression arrayConstrExpr]
    : idx:INT
        {
            arrayConstrExpr.addIndex(Integer.parseInt(idx.getText()));
        }
    ;

filterExprMain returns [FilterExpression filterExpression = null]
    : #(FILTER filterExpression=filterExpr)
    ;

filterExpr returns [FilterExpression result = null]
        {
            FilterExpression fe1, fe2;
        }
    : #(AND fe1=filterExpr fe2=filterExpr)
        {
            result = new CompoundFilterExpression(fe1, fe2, LOGICAL_OPERATOR.AND);
        }
    | #(OR fe1=filterExpr fe2=filterExpr)
        {
            result = new CompoundFilterExpression(fe1, fe2, LOGICAL_OPERATOR.OR);
        }
    | #(NOT fe1=filterExpr)
        {
            result = new NotFilterExpression(fe1);
        }
    | result=filterExprMinimal
    ;

filterExprMinimal returns [BasicFilterExpression filterExpression = new BasicFilterExpression()]
        {
            ValueDescriptor val = null;
            JsonOperatorEnum op = null;
        }
    : (filterMemberExpr[filterExpression])+ (szFn:AT_SIZE)? op=operator val=value
        {
            if (szFn != null) {
                filterExpression.setIsSizeFunction(true);
            }
            filterExpression.setOperator(op);
            filterExpression.setValueDescriptor(val);
        }
    ;

filterMemberExpr [BasicFilterExpression filterExpression]
        {
            MemberExpression memberExpr = null;
        }
    :  memberExpr=pathNaviExpr
        {
            filterExpression.addMemberExpression(memberExpr);
        }
    ;

direction returns [DIRECTION dir = DIRECTION.DOWN]
    : DOT
    | DOTDOT
        { dir = DIRECTION.ANYWHERE_DOWN; }
    ;

operator returns [JsonOperatorEnum operator = null]
    : EQ
        { operator = JsonOperatorEnum.EQ; }
    | NE
        { operator = JsonOperatorEnum.NE; }
    | LT
        { operator = JsonOperatorEnum.LT; }
    | LE
        { operator = JsonOperatorEnum.LE; }
    | GT
        { operator = JsonOperatorEnum.GT; }
    | GE
        { operator = JsonOperatorEnum.GE; }
    ;

value returns [ValueDescriptor valueDescriptor = null]
    : "null"
        { valueDescriptor = new ValueDescriptor(VALUE_TYPE.NULL, "null"); }
    | "true"
        { valueDescriptor = new ValueDescriptor(VALUE_TYPE.BOOLEAN, "true"); }
    | "false"
        { valueDescriptor = new ValueDescriptor(VALUE_TYPE.BOOLEAN, "false"); }
    | integer: INT
        { valueDescriptor = new ValueDescriptor(VALUE_TYPE.INTEGER, integer.getText()); }
    | real: REAL
        { valueDescriptor = new ValueDescriptor(VALUE_TYPE.DOUBLE, real.getText()); }
    | string: STRING
        { valueDescriptor = new ValueDescriptor(VALUE_TYPE.STRING, string.getText()); }
    ;


/**
 * @author Narcis Marcu (narcism@users.sourceforge.net)
 */
class JsonQueryLexer extends Lexer;
options {
    k=2;
    charVocabulary='\u0000'..'\uFFFE';
    filter=WS;
}

SEMI
    : ':'
    ;
COMMA
    : ','
    ;
WILDCARD
    : '*'
    ;
BACKSP
    : '\\'
    ;
LCURLY
    : '{'
    ;
RCURLY
    : '}'
    ;
LBRACKET
    : '['
    ;
RBRACKET
    : ']'
    ;
LPAREN
    : '('
    ;
RPAREN
    : ')'
    ;
AND
    : "&&"
    ;
OR
    : "||"
    ;
NOT
    : '!'
    ;
EQ
    : "=="
    ;
NE
    : "!="
    ;
LT
    : "<"
    ;
LE
    : "<="
    ;
GT
    : ">"
    ;
GE
    : ">="
    ;
AT_SIZE
    : "@size"
    ;
STRING
    : '"'! (ESC | ~('"' | '\\'))* '"'!
    ;
ID
    : ID_START_LETTER (ID_LETTER)*
    ;
INT_OR_REAL_OR_DOTS
    :  ( ('-')? (DIGIT)* FRAC ) =>  REAL { $setType(REAL); }
    | ( "..") => DOTDOT { $setType(DOTDOT); }
    | INT { $setType(INT); }
    | DOT { $setType(DOT); }
    ;
NEWLINE
    : ("\r\n" // DOS
       | '\r'   // MAC
       | '\n'   // Unix
      )
        {
            newline();
            $setType(Token.SKIP);
        }
    ;
protected INT
    : ('-')? (DIGIT)+
    ;
protected REAL
    : ('-')? (DIGIT)* FRAC (EXP)?
    ;
protected DOT
    : '.'
    ;
protected DOTDOT
    : ".."
    ;
protected WS
    : ' '
    ;
protected DIGIT
    : '0'..'9'
    ;
protected FRAC
    : '.' (DIGIT)+
    ;
protected EXP
    : ('e'|'E') ('+'|'-')? (DIGIT)+
    ;
protected ESC
    : '\\'(
        'n' { $setText("\n"); }
        | 'r' { $setText("\r"); }
        | 't' { $setText("\t"); }
        | '"' { $setText("\""); }
        )
    ;
protected ID_START_LETTER
    : 'a'..'z'
    | 'A'..'Z'
    | '$'
    | '_'
    ;
protected ID_LETTER
    : ID_START_LETTER
    | DIGIT
    ;
