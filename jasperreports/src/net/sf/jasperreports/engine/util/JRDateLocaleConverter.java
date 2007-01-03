/*
 * ============================================================================
 * GNU Lesser General Public License
 * ============================================================================
 *
 * JasperReports - Free Java report-generating library.
 * Copyright (C) 2001-2006 JasperSoft Corporation http://www.jaspersoft.com
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
 * 303 Second Street, Suite 450 North
 * San Francisco, CA 94107
 * http://www.jaspersoft.com
 */

package net.sf.jasperreports.engine.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.TimeZone;

import org.apache.commons.beanutils.locale.converters.DateLocaleConverter;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * A Converter class dedicated for the java.util.Date type.
 * <p/>
 * In order to obtain a java.util.Date object from a given String, a JRJavaUtilDateConverter
 * object should be instantiated and it's inherited convert() method should be called. The 
 * final result is provided by the JRJavaUtilDateConverter's parse() invoked method.
 *  <p/>
 * If if any of constructor arguments is null, default values will be provided.
 *  <p/>
 * @see org.apache.commons.beanutils.locale.converters.DateLocaleConverter
 * @author szaharia
 * @version $Id: JRParameter.java 1485 2006-11-14 18:23:17 +0000 (Tue, 14 Nov 2006) teodord $
 */

public class JRDateLocaleConverter extends DateLocaleConverter 
{

	private static Log log = LogFactory.getLog(DateLocaleConverter.class);
	
	// holds the timezone's ID
	private TimeZone timeZone = null;
	
	//boolean isLenient = false;
	
    /**
     *
     */
    public JRDateLocaleConverter(TimeZone timeZone) {
        super();
        
        this.timeZone = timeZone;
    }

    /**
     *
     *
    public JRDateLocaleConverter(boolean locPattern) {
        super(Locale.getDefault(), locPattern);
    }

    /**
     *
     *
    public JRDateLocaleConverter(Locale locale) {
        super(locale, false);
    }

    /**
     *
     *
    public JRDateLocaleConverter(Locale locale, boolean locPattern) {
        super(locale, (String) null, locPattern);
    }

    /**
     *
     *
    public JRDateLocaleConverter(Locale locale, String pattern) {
        super(locale, pattern, false);
    }

    /**
     * Create a {@link org.apache.commons.beanutils.locale.LocaleConverter} 
     * that will throw a {@link org.apache.commons.beanutils.ConversionException}
     * if a conversion error occurs.
     *
     * @param locale        The locale
     * @param pattern       The convertion pattern
     * @param locPattern    Indicate whether the pattern is localized or not
     *
    public JRDateLocaleConverter(Locale locale, String pattern, boolean locPattern) {
        super(locale, pattern, locPattern);
    }

    /**
     *
     *
    public JRDateLocaleConverter(Object defaultValue) {
        super(defaultValue, false);
    }

    /**
     *
     *
    public JRDateLocaleConverter(Object defaultValue, boolean locPattern) {
        super(defaultValue, Locale.getDefault(), locPattern);
    }

    /**
     *
     *
    public JRDateLocaleConverter(Object defaultValue, Locale locale) {
        super(defaultValue, locale, false);
    }

    /**
     *
     *
    public JRDateLocaleConverter(Object defaultValue, Locale locale, boolean locPattern) {
        super(defaultValue, locale, null, locPattern);
    }


    /**
     *
     *
    public JRDateLocaleConverter(Object defaultValue, Locale locale, String pattern) {
        super(defaultValue, locale, pattern, false);
    }

    /**
     *
     *
    public JRDateLocaleConverter(Object defaultValue, Locale locale, String pattern, boolean locPattern) {
        super(defaultValue, locale, pattern, locPattern);
    }
    */

    protected Object parse(Object value, String pattern) throws ParseException {
        SimpleDateFormat formatter = getFormatter(pattern, locale);
        if (locPattern) {
            formatter.applyLocalizedPattern(pattern);
        }
        else {
            formatter.applyPattern(pattern);
        }
        return formatter.parse((String) value);
    }

    private SimpleDateFormat getFormatter(String pattern, Locale locale) {
    	
        if(pattern == null) {
            pattern = locPattern ? 
                new SimpleDateFormat().toLocalizedPattern() : new SimpleDateFormat().toPattern();
            log.warn("Null pattern was provided, defaulting to: " + pattern);
        }
        SimpleDateFormat format = new SimpleDateFormat(pattern, locale);
        if(timeZone != null)
        	format.setTimeZone(timeZone);
        format.setLenient(isLenient());
        return format;
    }

    /**
    *
    *
   public boolean isLenient() {
       return isLenient;
   }
   
   /**
    * 
    *
   public void setLenient(boolean lenient) {
       isLenient = lenient;
   }	
   */
	
}
