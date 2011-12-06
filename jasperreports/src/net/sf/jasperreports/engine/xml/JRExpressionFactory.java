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
public class JRExpressionFactory extends JRBaseFactory
{
	
	public Object createObject( Attributes attrs )
	{
		return new JRDesignExpression();
	}

	/**
	 * @deprecated To be removed.
	 */
	public static class ObjectExpressionFactory extends JRBaseFactory {
		public Object createObject( Attributes attrs ){
			JRDesignExpression expression = new JRDesignExpression();
			expression.setValueClassName( Object.class.getName() );
			return expression;
		}
	}
	
	/**
	 * @deprecated To be removed.
	 */
	public static class ConnectionExpressionFactory extends JRBaseFactory {
		public Object createObject( Attributes attrs ){
			JRDesignExpression expression = new JRDesignExpression();
			expression.setValueClassName( Connection.class.getName() );
			return expression;
		}
	}
	
	/**
	 * @deprecated To be removed.
	 */
	public static class DataSourceExpressionFactory extends JRBaseFactory {
		public Object createObject( Attributes attrs ){
			JRDesignExpression expression = new JRDesignExpression();
			expression.setValueClassName( JRDataSource.class.getName() );
			return expression;
		}
	}
	
	/**
	 * @deprecated To be removed.
	 */
	public static class StringExpressionFactory extends JRBaseFactory {
		public Object createObject( Attributes attrs ){
			JRDesignExpression expression = new JRDesignExpression();
			expression.setValueClassName( String.class.getName() );
			return expression;
		}
	}
	
	/**
	 * @deprecated To be removed.
	 */
	public static class DateExpressionFactory extends JRBaseFactory {
		public Object createObject( Attributes attrs ){
			JRDesignExpression expression = new JRDesignExpression();
			expression.setValueClassName( Date.class.getName() );
			return expression;
		}
	}
	
	/**
	 * @deprecated To be removed.
	 */
	public static class ComparableExpressionFactory extends JRBaseFactory {
		public Object createObject( Attributes attrs ){
			JRDesignExpression expression = new JRDesignExpression();
			expression.setValueClassName( Comparable.class.getName() );
			return expression;
		}
	}
	
	/**
	 * @deprecated To be removed.
	 */
	public static class IntegerExpressionFactory extends JRBaseFactory {
		public Object createObject( Attributes attrs ){
			JRDesignExpression expression = new JRDesignExpression();
			expression.setValueClassName( Integer.class.getName() );
			return expression;
		}
	}
	
	/**
	 * @deprecated To be removed.
	 */
	public static class DoubleExpressionFactory extends JRBaseFactory {
		public Object createObject( Attributes attrs ){
			JRDesignExpression expression = new JRDesignExpression();
			expression.setValueClassName( Double.class.getName() );
			return expression;
		}
	}

	/**
	 * @deprecated To be removed.
	 */
	public static class NumberExpressionFactory extends JRBaseFactory {
		public Object createObject( Attributes attrs ){
			JRDesignExpression expression = new JRDesignExpression();
			expression.setValueClassName( Number.class.getName() );
			return expression;
		}
	}
	
	/**
	 * @deprecated To be removed.
	 */
	public static class BooleanExpressionFactory extends JRBaseFactory {
		public Object createObject( Attributes attrs ){
			JRDesignExpression expression = new JRDesignExpression();
			expression.setValueClassName( Boolean.class.getName() );
			return expression;
		}
	}

	/**
	 * @deprecated To be removed.
	 */
	public static class MapExpressionFactory extends JRBaseFactory {
		public Object createObject( Attributes attrs ){
			JRDesignExpression expression = new JRDesignExpression();
			expression.setValueClassName( Map.class.getName() );
			return expression;
		}
	}

	/**
	 * @deprecated To be removed.
	 */
	public static class ComparatorExpressionFactory extends JRBaseFactory
	{
		public Object createObject(Attributes attrs)
		{
			JRDesignExpression expression = new JRDesignExpression();
			expression.setValueClassName(Comparator.class.getName());
			return expression;
		}
	}

	/**
	 * A {@link JRExpressionFactory} that uses an attribute named
	 * <code>class</code> to determine the expression value class.
	 * 
	 * @author Lucian Chirita (lucianc@users.sourceforge.net)
	 * @deprecated To be removed.
	 */
	public static class ArbitraryExpressionFactory extends JRBaseFactory
	{
		private final String defaultValueClass;
		
		public ArbitraryExpressionFactory()
		{
			this((String) null);
		}
		
		public ArbitraryExpressionFactory(String defaultValueClass)
		{
			this.defaultValueClass = defaultValueClass;
		}
		
		public ArbitraryExpressionFactory(Class<?> defaultValueClass)
		{
			this(defaultValueClass == null ? null : defaultValueClass.getName());
		}

		public Object createObject(Attributes attrs)
		{
			JRDesignExpression expression = new JRDesignExpression();
			String className = attrs.getValue(JRXmlConstants.ATTRIBUTE_class);
			if (className != null)
			{
				expression.setValueClassName(className);
			}
			else if (defaultValueClass != null)
			{
				expression.setValueClassName(defaultValueClass);
			}
			return expression;
		}
	}

}
