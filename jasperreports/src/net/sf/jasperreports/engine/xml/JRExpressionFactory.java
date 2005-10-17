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
package net.sf.jasperreports.engine.xml;

import java.sql.Connection;
import java.util.Comparator;
import java.util.Date;
import java.util.Map;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.design.JRDesignExpression;

import org.xml.sax.Attributes;


/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id$
 */
public class JRExpressionFactory 
{
	
	/**
	 * 
	 */
	public static class ObjectExpressionFactory extends JRBaseFactory {
		public Object createObject( Attributes attrs ){
			JRDesignExpression expression = new JRDesignExpression();
			expression.setValueClassName( Object.class.getName() );
			return expression;
		}
	}
	
	/**
	 * 
	 */
	public static class ConnectionExpressionFactory extends JRBaseFactory {
		public Object createObject( Attributes attrs ){
			JRDesignExpression expression = new JRDesignExpression();
			expression.setValueClassName( Connection.class.getName() );
			return expression;
		}
	}
	
	/**
	 * 
	 */
	public static class DataSourceExpressionFactory extends JRBaseFactory {
		public Object createObject( Attributes attrs ){
			JRDesignExpression expression = new JRDesignExpression();
			expression.setValueClassName( JRDataSource.class.getName() );
			return expression;
		}
	}
	
	/**
	 * 
	 */
	public static class StringExpressionFactory extends JRBaseFactory {
		public Object createObject( Attributes attrs ){
			JRDesignExpression expression = new JRDesignExpression();
			expression.setValueClassName( String.class.getName() );
			return expression;
		}
	}
	
	/**
	 * 
	 */
	public static class DateExpressionFactory extends JRBaseFactory {
		public Object createObject( Attributes attrs ){
			JRDesignExpression expression = new JRDesignExpression();
			expression.setValueClassName( Date.class.getName() );
			return expression;
		}
	}
	
	/**
	 * 
	 */
	public static class ComparableExpressionFactory extends JRBaseFactory {
		public Object createObject( Attributes attrs ){
			JRDesignExpression expression = new JRDesignExpression();
			expression.setValueClassName( Comparable.class.getName() );
			return expression;
		}
	}
	
	/**
	 * 
	 */
	public static class IntegerExpressionFactory extends JRBaseFactory {
		public Object createObject( Attributes attrs ){
			JRDesignExpression expression = new JRDesignExpression();
			expression.setValueClassName( Integer.class.getName() );
			return expression;
		}
	}

	/**
	 * 
	 */
	public static class NumberExpressionFactory extends JRBaseFactory {
		public Object createObject( Attributes attrs ){
			JRDesignExpression expression = new JRDesignExpression();
			expression.setValueClassName( Number.class.getName() );
			return expression;
		}
	}
	
	/**
	 * 
	 */
	public static class BooleanExpressionFactory extends JRBaseFactory {
		public Object createObject( Attributes attrs ){
			JRDesignExpression expression = new JRDesignExpression();
			expression.setValueClassName( Boolean.class.getName() );
			return expression;
		}
	}

	/**
	 * 
	 */
	public static class MapExpressionFactory extends JRBaseFactory {
		public Object createObject( Attributes attrs ){
			JRDesignExpression expression = new JRDesignExpression();
			expression.setValueClassName( Map.class.getName() );
			return expression;
		}
	}

	public static class ComparatorExpressionFactory extends JRBaseFactory
	{
		public Object createObject(Attributes attrs)
		{
			JRDesignExpression expression = new JRDesignExpression();
			expression.setValueClassName(Comparator.class.getName());
			return expression;
		}
	}

}
