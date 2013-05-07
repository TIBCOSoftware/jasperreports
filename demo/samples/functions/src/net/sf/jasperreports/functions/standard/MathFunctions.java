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
 * This class should maintain all function methods that belongs to the category {@link #MATH}.
 * 
 * @author Massimo Rabbi (mrabbi@users.sourceforge.net)
 * @version $Id: CastorUtil.java 5880 2013-01-07 20:40:06Z teodord $
 */
@FunctionCategories({MathCategory.class})
public final class MathFunctions 
{
	
	// ===================== ABS function ===================== //
	/**
	 * Returns the absolute value of a number.
	 */
	@Function("ABS")
	@FunctionParameters({
			@FunctionParameter("number")})
	public static Number ABS(Number number){
		if(number==null) {
			return null;
		}
		else{
			if(number instanceof Integer){
				return Math.abs((Integer)number);
			}
			else if(number instanceof Double){
				return Math.abs((Double)number);
			}
			else if(number instanceof Float){
				return Math.abs((Float)number);
			}
			else if(number instanceof Long){
				return Math.abs((Long)number);
			}
			else{
				// fall-back
				return Math.abs(number.doubleValue());
			}
		}
	}
	
	/*
	// ===================== FACT function ===================== //
	@Function(name="FACT",description="Returns the factorial of a number")
	@FunctionParameters({
			@FunctionParameter(name="Integer number",description="The argument.")})
	public static Long FACT(Integer number){
		if(number==null) {
			return null;
		}
		if(number<0){
			throw new JRRuntimeException("Unable to calculate the factorial number of a negative number.");
		}
		else{
	       Long result = 1l;
	       for (int i = 1; i <= number; ++i) {
	    	   result *= i;
	       }
	       return result;
		}
	}
	
	// ===================== ISEVEN function ===================== //
	@Function(name="ISEVEN",description="Checks if a number is even. " +
			"If a non-integer number is specified, any digits after the decimal point are ignored.")
	@FunctionParameters({
			@FunctionParameter(name="Number",description="The number to check.")})
	public static Boolean ISEVEN(Number number){
		if(number==null) {
			return null;
		}
		else{
			return number.intValue()%2==0;			
		}
	}

	// ===================== ISODD function ===================== //
	@Function(name="ISODD",description="Checks if a number is odd. " +
			"If a non-integer number is specified, any digits after the decimal point are ignored.")
	@FunctionParameters({
			@FunctionParameter(name="Number",description="The number to check.")})
	public static Boolean ISODD(Number number){
		if(number==null) {
			return null;
		}
		else{
			return number.intValue()%2==1;			
		}
	}
	
	// ===================== PRODUCT function ===================== //
	@Function(name="PRODUCT",description="Returns the product of a list of numbers")
	@FunctionParameters({
			@FunctionParameter(name="Number",description="Argument")})
	public static Number PRODUCT(Number ...numbers){
		if(numbers.length==0) return null;		
		double result=1;
		for (int i=0;i<numbers.length;i++){
			result*=numbers[i].doubleValue();
		}
		return result;
	}

	// ===================== RAND function ===================== //
	@Function(name="RAND",description="Returns a random number between 0.0 and 1.0.")
	public static Double RAND(){
		return Math.random();
	}
	
	// ===================== RAND function ===================== //
	@Function(name="RANDBETWEEN",description="Returns an Integer random number between bottom and top range (both inclusive).")
	@FunctionParameters({
		@FunctionParameter(name="Bottom range",description="Integer number for the bottom range"),
		@FunctionParameter(name="Top range",description="Integer number for the top range")})
	public static Integer RANDBETWEEN(Integer bottomRange, Integer topRange){
		int min=bottomRange.intValue();
		int max=topRange.intValue();
		return min + (int)(Math.random() * ((max - min) + 1));
	}	
	
	// ===================== SIGN function ===================== //
	@Function(name="SIGN",description="Returns the sign of a number.")
	@FunctionParameters({
			@FunctionParameter(name="Number",description="The number to check.")})
	public static Integer SIGN(Number number){
		if(number==null) {
			return null;
		}
		else{
			return (int) Math.signum(number.doubleValue());			
		}
	}
	
	// ===================== SQRT function ===================== //
	@Function(name="SQRT",description="Returns the positive square root of a number. The number must be positive")
	@FunctionParameters({
			@FunctionParameter(name="Positive number",description="Argument.")})
	public static Number SQRT(Number number){
		if(number==null) {
			return null;
		}
		else{
			return Math.sqrt(number.doubleValue());			
		}
	}
	
	// ===================== SUM function ===================== //
	@Function(name="SUM",description="Returns the sum of a list of numbers")
	@FunctionParameters({
			@FunctionParameter(name="Number",description="Addendum")})
	public static Number SUM(Number ...numbers){
		if(numbers.length==0) return null;		
		double result=0;
		for (int i=0;i<numbers.length;i++){
			result+=numbers[i].doubleValue();
		}
		return result;
	}
	
	// ===================== MIN function ===================== //
	@Function(name="MIN",description="Returns the minimum of a list of numeric values.")
	@FunctionParameters({
		@FunctionParameter(name="Number",description="Number to compare")})
	public static Number MIN(Number ...numbers){
		if(numbers.length==0) throw new IllegalArgumentException("No numbers have been specified");
		if(!isNumberListValid(numbers)) throw new IllegalArgumentException("No null element are allowed");
		double min= numbers[0].doubleValue();
		for (int i=1;i<numbers.length;i++){
			if(numbers[i].doubleValue()<min){
				min = numbers[i].doubleValue();
			}
		}
		return fixNumberReturnType(min, numbers);
	}	
	
	// ===================== MAX function ===================== //
	@Function(name="MAX",description="Returns the maximum of a list of numeric values.")
	@FunctionParameters({
		@FunctionParameter(name="Number",description="Number to compare")})
	public static Number MAX(Number ...numbers){
		if(numbers.length==0) throw new IllegalArgumentException("No numbers have been specified");
		if(!isNumberListValid(numbers)) throw new IllegalArgumentException("No null element are allowed");
		double max= numbers[0].doubleValue();
		for (int i=1;i<numbers.length;i++){
			if(numbers[i].doubleValue()>max){
				max = numbers[i].doubleValue();
			}
		}
		return fixNumberReturnType(max, numbers);
	}
	
	// ===================== FLOOR function ===================== //
	@Function(name="FLOOR",description="" +
			"Returns the largest (closest to positive infinity) double value that is less " +
			"than or equal to the argument and is equal to a mathematical integer")
	@FunctionParameters({
		@FunctionParameter(name="Number",description="Value")})
	public static Double FLOOR(Number number){
		if(number == null) throw new IllegalArgumentException("The value number can not be null");
		return Math.floor(number.doubleValue());
	}

	// ===================== CEIL function ===================== //
	@Function(name="CEIL",description="" +
			"Returns the smallest (closest to negative infinity) double value that is greater " +
			"than or equal to the argument and is equal to a mathematical integer")
	@FunctionParameters({
		@FunctionParameter(name="Number",description="Value")})
	public static Double CEIL(Number number){
		if(number == null) throw new IllegalArgumentException("The value number can not be null");
		return Math.ceil(number.doubleValue());
	}
	
	/*
	 * Checks if the array of numbers is valid. 
	 * No null element must be contained.
	 */
	private static boolean isNumberListValid(Number ...numbers){
		for(int i=0;i<numbers.length;i++){
			if(numbers[i]==null) return false;
		}
		return true;
	}
	
	/*
	 * Fixes the return type for the numeric result value.
	 */
	private static Number fixNumberReturnType(Number returnValue, Number ...numbers){
		if(haveSameType(Integer.class, numbers)) return returnValue.intValue();
		if(haveSameType(Long.class, numbers)) return returnValue.intValue();
		if(haveSameType(Float.class, numbers)) return returnValue.intValue();
		return returnValue.doubleValue();
	}
	
	/*
	 * Checks if the list of generic numbers have all the same type.
	 */
	private static boolean haveSameType(
			Class<? extends Number> clazz, Number ...numbers){
		for(int i=0; i<numbers.length; i++){
			if(numbers[i].getClass() != clazz){
				return false;
			}
		}
		return true;
	}
	
	
}
