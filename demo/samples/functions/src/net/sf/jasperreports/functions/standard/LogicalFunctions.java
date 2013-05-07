/*
 * JasperReports - Free Java Reporting Library.
 * Copyright (C) 2001 - 2013 Jaspersoft Corporation. All rights reserved.
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
package net.sf.jasperreports.functions.standard;

import net.sf.jasperreports.functions.annotations.Function;
import net.sf.jasperreports.functions.annotations.FunctionCategories;
import net.sf.jasperreports.functions.annotations.FunctionParameter;
import net.sf.jasperreports.functions.annotations.FunctionParameters;


/**
 * This class should maintain all functions that allows logic decisions, and belong to the category {@link #LOGICAL}.
 * 
 * @author Massimo Rabbi (mrabbi@users.sourceforge.net)
 * @version $Id: CastorUtil.java 5880 2013-01-07 20:40:06Z teodord $
 */
@FunctionCategories({LogicalCategory.class})
public final class LogicalFunctions 
{
	
	// ===================== AND function ===================== //
	/**
	 * Returns true if all arguments are considered true, false otherwise. Argument must be a logical result or a direct boolean value.
	 */
	@Function("AND")
	@FunctionParameters({
		@FunctionParameter("arguments")})
	public static Boolean AND(Boolean ... arguments){
		if(arguments.length==0) return null;
		boolean result=true;
		for(Boolean arg : arguments){
			result=result && arg;
			// Stops at first false argument
			if (!result) return false;
		}
		return result;
	}

	/*
	// ===================== FALSE function ===================== //
	@Function(name="FALSE",description="Returns the logical value FALSE.")
	public static Boolean FALSE(){
		return Boolean.FALSE;
	}
	
	// ===================== TRUE function ===================== //
	@Function(name="TRUE",description="Returns the logical value TRUE.")
	public static Boolean TRUE(){
		return Boolean.TRUE;
	}
	
	// ===================== NOT function ===================== //
	@Function(name="NOT",description="Returns the negation of the specified boolean expression.")
	@FunctionParameters({
		@FunctionParameter(name="Argument",description="A boolean expression or value.")})
	public static Boolean NOT(Boolean boolValue){
		if (boolValue==null){
			return null;
		}
		else{
			return !boolValue;			
		}
	}
	
	// ===================== OR function ===================== //
	@Function(name="OR",description="Returns true if any of the arguments is considered true, false otherwise. " +
			"Argument must be a logical result or a direct boolean value.")
	@FunctionParameters({
		@FunctionParameter(name="Argument",description="A boolean expression or value.")})
	public static Boolean OR(Boolean ... arguments){
		if(arguments.length==0) return null;
		boolean result=false;
		for(Boolean arg : arguments){
			result=result || arg;
			// Stops at first true argument
			if (result) return true;
		}
		return result;
	}
	
	// ===================== IF function ===================== //
	@Function(name="IF",description="Returns one of two values, depending on a test condition.")
	@FunctionParameters({
		@FunctionParameter(name="Test condition",description="An expression returning a boolean value."),
		@FunctionParameter(name="Value 1 (true)",description="The value returned when the test is true."),
		@FunctionParameter(name="Value 2 (false)",description="The value returned when the test is false.")})
	public static Object IF(Boolean test, Object value1, Object value2){
		if(test==null) return null;
		return test ? value1 : value2; 
	}
	
	// ===================== EQUALS function ===================== //
	@Function(name="EQUALS",description="Checks if the two specified objects are equals.")
	@FunctionParameters({
		@FunctionParameter(name="Object 1",description="The first element to be compared."),
		@FunctionParameter(name="Object 2",description="The second element to be compared.")})
	public static Boolean EQUALS(Object obj1, Object obj2){
		if(obj1!=null){
			return obj1.equals(obj2);
		}
		else if (obj2!=null){
			return obj2.equals(obj1);
		}
		return true;	// both null
	}	
	*/
}
