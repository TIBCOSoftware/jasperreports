/*
 * JasperReports - Free Java Reporting Library.
 * Copyright (C) 2001 - 2014 TIBCO Software Inc. All rights reserved.
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

import net.sf.jasperreports.engine.DefaultJasperReportsContext;
import net.sf.jasperreports.engine.JRCommonText;
import net.sf.jasperreports.engine.JRPropertiesUtil;
import net.sf.jasperreports.engine.JRRuntimeException;
import net.sf.jasperreports.engine.JasperReportsContext;
import net.sf.jasperreports.engine.fill.JRMeasuredText;
import net.sf.jasperreports.engine.fill.TextMeasurer;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * A text measurer implementation that extends
 * {@link TextMeasurer the default text measurer} and adds a workaround for
 * Sun JDK bug <a href="http://bugs.sun.com/bugdatabase/view_bug.do?bug_id=6367148">6367148</a>/
 * <a href="http://bugs.sun.com/bugdatabase/view_bug.do?bug_id=6611637">6611637</a>.
 * 
 * <p>
 * The workaround consists of simply reattempting the text measuring when 
 * a <code>java.lang.NullPointerException</code> is thrown from
 * <code>sun.font.GlyphLayout</code>. 
 * </p>
 * 
 * @author Lucian Chirita (lucianc@users.sourceforge.net)
 * @see JdkGlyphFixTextMeasurerFactory
 */
public class JdkGlyphFixTextMeasurer extends TextMeasurer
{

	private static final Log log = LogFactory.getLog(JdkGlyphFixTextMeasurer.class);
	
	protected static final String JDK_EXCEPTION_CLASS_PREFIX = "sun.font.GlyphLayout";
	
	/**
	 * The default attempt count.
	 */
	public static final int DEFAULT_ATTEMPTS = 20;
	
	/**
	 * The default between attempts sleep time.
	 */
	public static final int DEFAULT_ATTEMPT_SLEEP = 0;
	
	/**
	 * A property that specifies the number of times the measurer should attempt
	 * to measure a single text element before giving up.
	 * 
	 * The default value is 20.
	 * 
	 * @see #DEFAULT_ATTEMPTS
	 */
	public static final String PROPERTY_ATTEMPTS = JRPropertiesUtil.PROPERTY_PREFIX 
			+ "jdk.glyph.fix.text.measurer.attempts";
	
	/**
	 * A property that specifies the number of milliseconds to sleep between
	 * measuring reattempts.
	 * 
	 * The default value is 0, which means that the measurer will not pause
	 * between reattempts.
	 * 
	 * @see #DEFAULT_ATTEMPT_SLEEP
	 */
	public static final String PROPERTY_ATTEMPT_SLEEP = JRPropertiesUtil.PROPERTY_PREFIX 
			+ "jdk.glyph.fix.text.measurer.sleep";
	
	/**
	 * Whether <code>java.lang.NullPointer</code> exceptions with empty stacktraces
	 * should be caught.
	 * 
	 * This is useful when running on a Sun server JVM (java -server), which might omit
	 * exception stacktraces in some cases.
	 */
	public static final String PROPERTY_CATCH_EMPTY_STACKTRACE = JRPropertiesUtil.PROPERTY_PREFIX 
			+ "jdk.glyph.fix.text.measurer.catch.empty.stakctrace";
	
	private final int attempts;
	private final int sleep;
	private final boolean catchEmptyStacktrace;
	
	/**
	 * Create a text measurer for a text element.
	 * 
	 * @param textElement the text element
	 */
	public JdkGlyphFixTextMeasurer(JasperReportsContext jasperReportsContext, JRCommonText textElement)
	{
		super(jasperReportsContext, textElement);
		
		attempts = JRPropertiesUtil.getInstance(jasperReportsContext).getIntegerProperty(PROPERTY_ATTEMPTS, DEFAULT_ATTEMPTS);
		sleep = JRPropertiesUtil.getInstance(jasperReportsContext).getIntegerProperty(PROPERTY_ATTEMPT_SLEEP, DEFAULT_ATTEMPT_SLEEP);
		catchEmptyStacktrace = JRPropertiesUtil.getInstance(jasperReportsContext).getBooleanProperty(PROPERTY_CATCH_EMPTY_STACKTRACE);
	}

	/**
	 * @deprecated Replaced by {@link #JdkGlyphFixTextMeasurer(JasperReportsContext, JRCommonText)}.
	 */
	public JdkGlyphFixTextMeasurer(JRCommonText textElement)
	{
		this(DefaultJasperReportsContext.getInstance(), textElement);
	}

	/**
	 * Calls {@link TextMeasurer#measure(JRStyledText, int, int, boolean) super.measure}, catches
	 * <code>sun.font.GlyphLayout</code> NPEs and reattempts the call.
	 */
	public JRMeasuredText measure(JRStyledText styledText, int remainingTextStart, int availableStretchHeight, boolean canOverflow)
	{
		int count = 0;
		do
		{
			try
			{
				++count;

				return super.measure(styledText, remainingTextStart, availableStretchHeight, canOverflow);
			}
			catch (NullPointerException e) //NOPMD
			{
				if (isJdkGlyphError(e))
				{
					if (count >= attempts)
					{
						log.error("JDK Glyph exception caught " + attempts + " times, giving up attempts");
						throw e;
					}
				}
				else
				{
					throw e;
				}
				
				if (log.isDebugEnabled())
				{
					log.debug("Caught JDK Glyph exception " + e + " at attempt #" + count);
				}
				
				if (sleep > 0)
				{
					try
					{
						Thread.sleep(sleep);
					}
					catch (InterruptedException ie)
					{
						throw new JRRuntimeException(ie); //NOPMD
					}
				}
			}
		}
		while(true);
	}

	protected boolean isJdkGlyphError(NullPointerException e)
	{
		StackTraceElement[] stackTrace = e.getStackTrace();
		if (stackTrace.length == 0)
		{
			if (log.isDebugEnabled())
			{
				log.debug("Caught exception with no stacktrace; " 
						+ (catchEmptyStacktrace ? "" : "not ") + "treating as JDK Glyph exception");
			}
			
			return catchEmptyStacktrace;
		}
		
		StackTraceElement top = stackTrace[0];
		return top.getClassName().startsWith(JDK_EXCEPTION_CLASS_PREFIX);
	}
	
}
