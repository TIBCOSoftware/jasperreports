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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import net.sf.jasperreports.functions.AbstractFunctionSupport;
import net.sf.jasperreports.functions.annotations.Function;
import net.sf.jasperreports.functions.annotations.FunctionCategories;
import net.sf.jasperreports.functions.annotations.FunctionParameter;
import net.sf.jasperreports.functions.annotations.FunctionParameters;


/**
 * This class should maintain all function methods that belongs to the category {@link #DATE_TIME}.
 * 
 * @author Massimo Rabbi (mrabbi@users.sourceforge.net)
 * @version $Id: CastorUtil.java 5880 2013-01-07 20:40:06Z teodord $
 */
@FunctionCategories({DateTimeCategory.class})
public final class DateTimeFunctions
{
	
	// ===================== TODAY function ===================== //
	/**
	 * Returns the current date as date object.
	 */
	@Function("TODAY")
	public static Date TODAY(){
		return new Date();
	}
	
	// ===================== NOW function ===================== //
	/**
	 * Returns the current instant as date object.
	 */
	@Function("NOW")
	public static Date NOW(){
		return new Date();
	}
	
	// ===================== YEAR function ===================== //
	/**
	 * Returns the year of a given date. Date object can be a String, long value (milliseconds) or Date instance itself.
	 */
	@Function("YEAR")
	@FunctionParameters({
		@FunctionParameter("dateObject")})
	public static Integer YEAR(Object dateObject){
		return getCalendarFieldFromDate(dateObject,Calendar.YEAR);
	}
	
	// ===================== MONTH function ===================== //
	/**
	 * Returns the month of a given date. Date object can be a String, long value (milliseconds) or Date instance itself.
	 */
	@Function("MONTH")
	@FunctionParameters({
		@FunctionParameter("dateObject")})
	public static Integer MONTH(Object dateObject){
		return getCalendarFieldFromDate(dateObject,Calendar.MONTH)+1;	// January is 0
	}
	
	/*
	// ===================== DAY function ===================== //
	@Function(name="DAY",description="Returns the day of a given date. " +
			"Date object can be a String, long value (millis) or Date instance itself.")
	@FunctionParameters({
		@FunctionParameter(name="Date object",description="The object representing the date.")})
	public static Integer DAY(Object dateObject){
		return getCalendarFieldFromDate(dateObject,Calendar.DAY_OF_MONTH);
	}
	
	// ===================== WEEKDAY function ===================== //
	@Function(name="WEEKDAY",description="Returns the day of the week for a given date. " +
			"Date object can be a String, long value (millis) or Date instance itself.")
	@FunctionParameters({
		@FunctionParameter(name="Date object",description="The object representing the date."),
		@FunctionParameter(name="Sunday is first day",description="Boolean flag to decide if Sunday should be considered as first day." +
				"Default is not.")})
	public static Integer WEEKDAY(Object dateObject){
		return WEEKDAY(dateObject, false);
	}
	
	public static Integer WEEKDAY(Object dateObject, Boolean isSundayFirstDay){
		Integer dayOfWeek = getCalendarFieldFromDate(dateObject,Calendar.DAY_OF_WEEK);
		if(dayOfWeek==null) return null;
		if(isSundayFirstDay){
			// By default Sunday is considered first day in Java 
			// Calendar.SUNDAY should be a constant with value 1.
			// See the Calendar.DAY_OF_WEEK javadoc		
			return dayOfWeek;
		}
		else{
			// shift the days
			if(dayOfWeek==Calendar.SUNDAY){
				return 7;
			}
			else{
				return dayOfWeek-1;
			}
		}
	}
	
	// ===================== HOUR function ===================== //
	@Function(name="HOUR",description="Returns the hour (0-23) of the day for a given date. " +
			"Date object can be a String, long value (millis) or Date instance itself.")
	@FunctionParameters({
		@FunctionParameter(name="Date object",description="The object representing the date.")})
	public static Integer HOUR(Object dateObject){
		return getCalendarFieldFromDate(dateObject,Calendar.HOUR_OF_DAY);
	}	

	// ===================== MINUTE function ===================== //
	@Function(name="MINUTE",description="Returns the minute (0-59) of the hour for a given date. " +
			"Date object can be a String, long value (millis) or Date instance itself.")
	@FunctionParameters({
		@FunctionParameter(name="Date object",description="The object representing the date.")})
	public static Integer MINUTE(Object dateObject){
		return getCalendarFieldFromDate(dateObject,Calendar.MINUTE);
	}
	
	// ===================== SECOND function ===================== //
	@Function(name="SECOND",description="Returns the second (0-59) of the minute for a given date. " +
			"Date object can be a String, long value (millis) or Date instance itself.")
	@FunctionParameters({
		@FunctionParameter(name="Date object",description="The object representing the date.")})
	public static Integer SECOND(Object dateObject){
		return getCalendarFieldFromDate(dateObject,Calendar.SECOND);
	}
	
	// ===================== DATE function ===================== //
	@Function(name="DATE",description="Creates a date object using the specified information on day, month and year.")
	@FunctionParameters({
		@FunctionParameter(name="Year",description="The year of the new date."),
		@FunctionParameter(name="Month",description="The month of the new date."),
		@FunctionParameter(name="Day",description="The day of the new date.")})
	public static Date DATE(Integer year, Integer month, Integer dayOfMonth){
		if(year==null || month==null || dayOfMonth==null) return null;
		DateTime dt=new DateTime(year,month,dayOfMonth,0,0,0);
		return dt.toDate();
	}
	*/
	
	// ===================== DATEVALUE function ===================== //
	/**
	 * Gives the corresponding numeric value (long milliseconds) for a specified date object.
	 */
	@Function("DATEVALUE")
	@FunctionParameters({
		@FunctionParameter("dateObject")})
	public static Long DATEVALUE(Object dateObject){
		Date convertedDate = convertDateObject(dateObject);
		return (convertedDate!=null) ? convertedDate.getTime() : null; 
	}
	
	/*
	// ===================== TIME function ===================== //
	@Function(name="TIME",description="Returns a text string representing a time value (hours, seconds and minutes). " +
			"If no specific pattern is specified a default formatter is used.")
	@FunctionParameters({
		@FunctionParameter(name="Hours",description="The hours for the new time value."),
		@FunctionParameter(name="Minutes",description="The minutes for the new time value."),
		@FunctionParameter(name="Seconds",description="The seconds for the new time value."),
		@FunctionParameter(name="Format pattern",description="The pattern to format the time value.")})
	public static String TIME(Integer hours, Integer minutes, Integer seconds){
		return TIME(hours, minutes, seconds, null);
	}
	
	public static String TIME(Integer hours, Integer minutes, Integer seconds, String timePattern){
		if(hours==null || minutes==null || seconds==null) return null;
		LocalTime lt=new LocalTime(hours,minutes,seconds);
		if(timePattern==null) {
			return lt.toString(DateTimeFormat.longTime()); 
		}
		else{
			try{
				// Try to convert to a pattern
				DateTimeFormatter dtf = DateTimeFormat.forPattern(timePattern);
				return lt.toString(dtf);
			}
			catch (IllegalArgumentException ex){
				// Fallback to the default solution
				return lt.toString(DateTimeFormat.longTime()); 
			}			
		}
	}
	
	// ===================== EDATE function ===================== //
	@Function(name="EDATE",description="Returns a date a number of months away.")
	@FunctionParameters({
		@FunctionParameter(name="Date object",description="The object representing the date."),
		@FunctionParameter(name="Months",description="The number of months after the given date.")})
	public static Date EDATE(Object dateObject, Integer months){
		Date convertedDate = convertDateObject(dateObject);
		if(convertedDate==null){
			return null;
		}
		else{
			DateTime dt=new DateTime(convertedDate);
			dt = dt.plusMonths(months);
			return dt.toDate();
		}
	}
	
	// ===================== WORKDAY function ===================== //
	@Function(name="WORKDAY",description="Returns a date a number of workdays away. " +
			"Saturday and Sundays are not considered working days.")
	@FunctionParameters({
		@FunctionParameter(name="Date object",description="The object representing the date."),
		@FunctionParameter(name="Working days",description="The number of days after the given date.")})
	public static Date WORKDAY(Object dateObject, Integer workdays){
		Date convertedDate = convertDateObject(dateObject);
		if(convertedDate==null){
			return null;
		}
		else{
			DateTime cursorDT=new DateTime(convertedDate);
			int remainingDays=workdays;
			while(remainingDays>0){
				int dayOfWeek = cursorDT.getDayOfWeek();
				if(!(dayOfWeek==DateTimeConstants.SATURDAY || 
						dayOfWeek==DateTimeConstants.SUNDAY)){
					// Decrement remaining days only when it is not Saturday or Sunday
					remainingDays--;
				}
				cursorDT=cursorDT.plusDays(1);
			}
			return cursorDT.toDate();
		}
	}
	
	// ===================== NETWORKDAYS function ===================== //
	@Function(name="NETWORKDAYS",description="Returns the number of working days between two dates (inclusive)." +
			"Saturday and Sundays are not considered working days.")
	@FunctionParameters({
		@FunctionParameter(name="Start date",description="The initial date."),
		@FunctionParameter(name="End date",description="The end date.")})
	public static Integer NETWORKDAYS(Object startDate, Object endDate){
		Date startDateObj = convertDateObject(startDate);
		Date endDateObj = convertDateObject(endDate);
		if(startDateObj==null || endDateObj==null){
			return null;
		}
		else{
			DateTime cursorDateTime=new DateTime(startDateObj);
			DateTime endDateTime=new DateTime(endDateObj);
			int workingDays=0;
			if(cursorDateTime.isAfter(endDateTime)){
				// Swap data information
				DateTime tmp=cursorDateTime;
				cursorDateTime=endDateTime;
				endDateTime=tmp;
			}
			while (Days.daysBetween(cursorDateTime, endDateTime).getDays()>0){
				int dayOfWeek = cursorDateTime.getDayOfWeek();
				if(!(dayOfWeek==DateTimeConstants.SATURDAY || 
						dayOfWeek==DateTimeConstants.SUNDAY)){
					workingDays++;
				}
				cursorDateTime=cursorDateTime.plusDays(1);
			}
			return workingDays;
		}
	}
	
	// ===================== DAYS function ===================== //
	@Function(name="DAYS",description="Returns the number of days between two dates.")
	@FunctionParameters({
		@FunctionParameter(name="Start date",description="The initial date."),
		@FunctionParameter(name="End date",description="The end date.")})
	public static Integer DAYS(Object startDate, Object endDate){
		Date startDateObj = convertDateObject(startDate);
		Date endDateObj = convertDateObject(endDate);
		if(startDateObj==null || endDateObj==null){
			return null;
		}
		else{
			DateTime dt1=new DateTime(startDateObj);
			DateTime dt2=new DateTime(endDateObj);
			return Days.daysBetween(dt1, dt2).getDays();
		}
	}
	
	// ===================== DAYSINMONTH function ===================== //
	@Function(name="DAYSINMONTH",description="Returns the number of days in a month.")
	@FunctionParameters({
		@FunctionParameter(name="Selected date",description="The date to check.")})
	public static Integer DAYSINMONTH(Object dateObj){
		Date date = convertDateObject(dateObj);
		if(date==null){
			return null;
		}
		else{
			DateTime dt=new DateTime(date);
			return dt.dayOfMonth().getMaximumValue();
		}
	}
	
	// ===================== DAYSINYEAR function ===================== //
	@Function(name="DAYSINYEAR",description="Returns the number of days in a year.")
	@FunctionParameters({
		@FunctionParameter(name="Selected date",description="The date to check.")})
	public static Integer DAYSINYEAR(Object dateObj){
		Date date = convertDateObject(dateObj);
		if(date==null){
			return null;
		}
		else{
			DateTime dt=new DateTime(date);
			return dt.dayOfYear().getMaximumValue();
		}
	}
	
	// ===================== WEEKS function ===================== //
	@Function(name="WEEKS",description="Returns the number of weeks between two dates.")
	@FunctionParameters({
		@FunctionParameter(name="Start date",description="The initial date."),
		@FunctionParameter(name="End date",description="The end date.")})
	public static Integer WEEKS(Object startDate, Object endDate){
		Date startDateObj = convertDateObject(startDate);
		Date endDateObj = convertDateObject(endDate);
		if(startDateObj==null || endDateObj==null){
			return null;
		}
		else{
			DateTime dt1=new DateTime(startDateObj);
			DateTime dt2=new DateTime(endDateObj);
			return Weeks.weeksBetween(dt1, dt2).getWeeks();
		}
	}
	
	// ===================== WEEKSINYEAR function ===================== //
	@Function(name="WEEKSINYEAR",description="Returns the number of weeks in a year.")
	@FunctionParameters({
		@FunctionParameter(name="Selected date",description="The date to check.")})
	public static Integer WEEKSINYEAR(Object dateObj){
		Date date = convertDateObject(dateObj);
		if(date==null){
			return null;
		}
		else{
			DateTime dt=new DateTime(date);
			return dt.weekOfWeekyear().getMaximumValue();
		}
	}
	
	// ===================== WEEKNUM function ===================== //
	@Function(name="WEEKNUM",description="Returns the week number of a given date.")
	@FunctionParameters({
		@FunctionParameter(name="Selected date",description="The date to check.")})
	public static Integer WEEKNUM(Object dateObj){
		Date date = convertDateObject(dateObj);
		if(date==null){
			return null;
		}
		else{
			DateTime dt=new DateTime(date);
			return dt.getWeekOfWeekyear();
		}
	}
	
	// ===================== MONTHS function ===================== //
	@Function(name="MONTHS",description="Returns the number of months between two dates.")
	@FunctionParameters({
		@FunctionParameter(name="Start date",description="The initial date."),
		@FunctionParameter(name="End date",description="The end date.")})
	public static Integer MONTHS(Object startDate, Object endDate){
		Date startDateObj = convertDateObject(startDate);
		Date endDateObj = convertDateObject(endDate);
		if(startDateObj==null || endDateObj==null){
			return null;
		}
		else{
			DateTime dt1=new DateTime(startDateObj);
			DateTime dt2=new DateTime(endDateObj);
			return Months.monthsBetween(dt1, dt2).getMonths();
		}
	}
	
	// ===================== YEARS function ===================== //
	@Function(name="YEARS",description="Returns the number of years between two dates.")
	@FunctionParameters({
		@FunctionParameter(name="Start date",description="The initial date."),
		@FunctionParameter(name="End date",description="The end date.")})
	public static Integer YEARS(Object startDate, Object endDate){
		Date startDateObj = convertDateObject(startDate);
		Date endDateObj = convertDateObject(endDate);
		if(startDateObj==null || endDateObj==null){
			return null;
		}
		else{
			DateTime dt1=new DateTime(startDateObj);
			DateTime dt2=new DateTime(endDateObj);
			return Years.yearsBetween(dt1, dt2).getYears();
		}
	}
	
	// ===================== ISLEAPYEAR function ===================== //
	@Function(name="ISLEAPYEAR",description="Checks if the given date occurs in a leap year.")
	@FunctionParameters({
		@FunctionParameter(name="Selected date",description="The date to check.")})
	public static Boolean ISLEAPYEAR(Object dateObj){
		Date date = convertDateObject(dateObj);
		if(date==null){
			return null;
		}
		else{
			DateTime dt=new DateTime(date);
			return dt.year().isLeap();
		}
	}
	
	// ===================== FORMAT function ===================== //
	@Function(name="DATEFORMAT",description="Format the specified date object using the chosen format pattern")
	@FunctionParameters({
		@FunctionParameter(name="Selected date",description="The date to format."),
		@FunctionParameter(name="Format pattern",description="Format pattern to apply when printing the date.")})
	public static String DATEFORMAT(Date dateObj, String formatPattern){
		if(dateObj==null){
			return null;
		}
		else{
			DateTimeFormatter formatter = DateTimeFormat.forPattern(formatPattern);
			DateTime dt = new DateTime(dateObj);
			return dt.toString(formatter);
		}
	}
	
	// ===================== DATERANGE function ===================== //
	@Function(name="DATERANGE",description="Allows to create a JasperReports DateRange instance " +
			"starting from either a String expression or a Date instance.")
	@FunctionParameters({
		@FunctionParameter(name="Date Range Details",description="The date range information.")})
	public static DateRange DATERANGE(Object dateExprObj){
		DateRangeBuilder dateRangeBuilder = null;
		if(dateExprObj instanceof String){
			dateRangeBuilder = new DateRangeBuilder((String)dateExprObj);
		}
		else if(dateExprObj instanceof Date) {
			dateRangeBuilder = new DateRangeBuilder((Date)dateExprObj);
		}
		else {
			throw new IllegalArgumentException(
					"The input parameter for DATERANGE function can be only a String or a Date object");
		}
		return dateRangeBuilder.toDateRange();
	}
	
	/* Internal private methods */
	
	/*
	 * This methods tries to convert a generic object into a java.util.Date instance.
	 * Supported types are for now String, Long values (time millis) and Date subtypes
	 * like for example java.sql.Date.
	 */
	private static Date convertDateObject(Object dateObject){
		if(dateObject==null){
			return null;
		}
		else if(dateObject instanceof String){
			SimpleDateFormat simpleFormat=new SimpleDateFormat();
			try {
				return simpleFormat.parse((String)dateObject);
			} catch (ParseException e) {
				// Unable to parse the string as Date
				return null;
			}
		}
		else if(dateObject instanceof Long){
			return new Date((Long)dateObject);
		}
		else if(dateObject instanceof Date){
			return (Date)dateObject;
		}
		return null;
	}
	
	/*
	 * Tries to recover a specific detail (given by Calendar field type) 
	 * from an input date object.
	 */
	private static Integer getCalendarFieldFromDate(Object dateObject,int field){
		Date convertedDate = convertDateObject(dateObject);
		if(convertedDate==null) {
			return null;
		}
		else{
			Calendar cal=new GregorianCalendar();
			cal.setTime(convertedDate);
			return cal.get(field);			
		}
	}
}
