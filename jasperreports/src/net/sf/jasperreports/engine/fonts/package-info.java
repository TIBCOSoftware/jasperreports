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
/**
 * Provides support for font extensions. 
 * <br/>
 * <h2>The Font Extension Point</h2>
 * Among the various extension points that JasperReports exposes, this one 
 * allows users to make use of fonts that are not available to the JVM at runtime in normal 
 * circumstances. 
 * <p>
 * Usually, in order to make a font file available to the JVM, one would need to install that 
 * font file into the system, or make some configuration changes in the local JVM. But if 
 * the application ends up being deployed on some other machine, the same font file 
 * installation process has to be performed on that machine as well.
 * </p><p> 
 * When designing reports, we are immediately faced with the selection of fonts to be used 
 * by the various text fields present in the report template. Using fonts that are available 
 * only on the local JVM where we design and test our reports poses the risk of those fonts 
 * not being available on the system where the reports are actually generated at runtime. 
 * </p><p> 
 * When specified fonts are missing on a system, the JVM uses some default fonts as 
 * on-the-fly replacements and these might have totally different properties and font metrics, 
 * producing various side effects, including but not limited to chunks of text being cut from 
 * the generated output. 
 * </p><p> 
 * This is why it is so important for an application to control and make sure that the fonts used 
 * in its reports are available at runtime on the machines on which the application is 
 * running. 
 * </p><p> 
 * The best way to control fonts in JasperReports is to provide the font files as extensions to 
 * the library. Basically, this involves putting True Type Font files in a JAR file, together 
 * with an XML file that describes the content of the JAR and the various relationships 
 * between the fonts and the locales. 
 * </p><p> 
 * JasperReports raises a {@link net.sf.jasperreports.engine.util.JRFontNotFoundException} in the case where the font used inside 
 * a report template is not available to the JVM as either as a system font or a font coming 
 * from a JR font extension. This ensure that all problems caused by font metrics 
 * mismatches are avoided and we have an early warning about the inconsistency. 
 * </p><p> 
 * However, for backward compatibility reasons, this font runtime validation can be turned 
 * off using the <code>net.sf.jasperreports.awt.ignore.missing.font</code> configuration 
 * property, which can be employed either globally or at report level. 
 * </p><p> 
 * The font extension point in JasperReports is exposed as the 
 * {@link net.sf.jasperreports.engine.fonts.FontFamily} public interface. Each font 
 * family is made out of 4 font faces: normal, bold, italic and bolditalic. Only the normal 
 * face is required in a font family. Font faces are described by the 
 * {@link net.sf.jasperreports.engine.fonts.FontFace} interface. Besides the font faces, 
 * a font family also supports certain locales and tells if it is able to simulate the bold style 
 * and the italic style in PDF export, in case the corresponding font faces are missing from 
 * its declaration. 
 * </p><p> 
 * JasperReports is shipped with convenience implementations for the above mentioned 
 * interfaces. The {@link net.sf.jasperreports.engine.fonts.SimpleFontFamily} is the default 
 * implementation of the {@link net.sf.jasperreports.engine.fonts.FontFamily} interface. This 
 * works with the {@link net.sf.jasperreports.engine.fonts.SimpleFontFace} default implementation 
 * of the font face.
 * </p><p> 
 * The best way to deploy font files as extensions is to rely on the Spring-based extension 
 * registry factory shipped with JasperReports and make use of the default font interfaces. 
 * </p>
 * <h2>Related Documentation</h2>
 * <a href="http://community.jaspersoft.com/wiki/jasperreports-library-tutorial">JasperReports Tutorial</a>
 * 
 * @see net.sf.jasperreports.engine.util.JRFontNotFoundException
 */
package net.sf.jasperreports.engine.fonts;
