/*
 * ============================================================================
 *                   The JasperReports License, Version 1.0
 * ============================================================================
 * 
 * Copyright (C) 2001-2004 Teodor Danciu (teodord@users.sourceforge.net). All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without modification,
 * are permitted provided that the following conditions are met:
 * 
 * 1. Redistributions of source code must retain the above copyright notice,
 *    this list of conditions and the following disclaimer.
 * 
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 * 
 * 3. The end-user documentation included with the redistribution, if any, must
 *    include the following acknowledgment: "This product includes software
 *    developed by Teodor Danciu (http://jasperreports.sourceforge.net)."
 *    Alternately, this acknowledgment may appear in the software itself, if
 *    and wherever such third-party acknowledgments normally appear.
 * 
 * 4. The name "JasperReports" must not be used to endorse or promote products 
 *    derived from this software without prior written permission. For written 
 *    permission, please contact teodord@users.sourceforge.net.
 * 
 * 5. Products derived from this software may not be called "JasperReports", nor 
 *    may "JasperReports" appear in their name, without prior written permission
 *    of Teodor Danciu.
 * 
 * THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED WARRANTIES,
 * INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
 * FITNESS  FOR A PARTICULAR  PURPOSE ARE  DISCLAIMED.  IN NO  EVENT SHALL  THE
 * APACHE SOFTWARE  FOUNDATION  OR ITS CONTRIBUTORS  BE LIABLE FOR  ANY DIRECT,
 * INDIRECT, INCIDENTAL, SPECIAL,  EXEMPLARY, OR CONSEQUENTIAL  DAMAGES (INCLU-
 * DING, BUT NOT LIMITED TO, PROCUREMENT  OF SUBSTITUTE GOODS OR SERVICES; LOSS
 * OF USE, DATA, OR  PROFITS; OR BUSINESS  INTERRUPTION)  HOWEVER CAUSED AND ON
 * ANY  THEORY OF LIABILITY,  WHETHER  IN CONTRACT,  STRICT LIABILITY,  OR TORT
 * (INCLUDING  NEGLIGENCE OR  OTHERWISE) ARISING IN  ANY WAY OUT OF THE  USE OF
 * THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

/*
 * ============================================================================
 *                   GNU Lesser General Public License
 * ============================================================================
 *
 * JasperReports - Free Java report-generating library.
 * Copyright (C) 2001-2004 Teodor Danciu teodord@users.sourceforge.net
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
 * Teodor Danciu
 * 173, Calea Calarasilor, Bl. 42, Sc. 1, Ap. 18
 * Postal code 030615, Sector 3
 * Bucharest, ROMANIA
 * Email: teodord@users.sourceforge.net
 */

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.HashPrintServiceAttributeSet;
import javax.print.attribute.PrintRequestAttributeSet;
import javax.print.attribute.PrintServiceAttributeSet;
import javax.print.attribute.standard.MediaSizeName;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JRImage;
import net.sf.jasperreports.engine.JRImageRenderer;
import net.sf.jasperreports.engine.JRPrintImage;
import net.sf.jasperreports.engine.JRPrintLine;
import net.sf.jasperreports.engine.JRPrintPage;
import net.sf.jasperreports.engine.JRPrintText;
import net.sf.jasperreports.engine.JRTextElement;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.base.JRBasePrintImage;
import net.sf.jasperreports.engine.base.JRBasePrintLine;
import net.sf.jasperreports.engine.base.JRBasePrintPage;
import net.sf.jasperreports.engine.base.JRBasePrintText;
import net.sf.jasperreports.engine.design.JRDesignFont;
import net.sf.jasperreports.engine.design.JRDesignReportFont;
import net.sf.jasperreports.engine.export.JRPrintServiceExporter;
import net.sf.jasperreports.engine.export.JRPrintServiceExporterParameter;
import net.sf.jasperreports.engine.util.JRImageLoader;
import net.sf.jasperreports.engine.util.JRSaver;


/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id$
 */
public class PrintServiceApp
{


	/**
	 *
	 */
	private static final String TASK_FILL = "fill";
	private static final String TASK_PRINT = "print";
	
	
	/**
	 *
	 */
	public static void main(String[] args)
	{
		String fileName = null;
		String taskName = null;

		if(args.length == 0)
		{
			usage();
			return;
		}
				
		int k = 0;
		while ( args.length > k )
		{
			if ( args[k].startsWith("-T") )
				taskName = args[k].substring(2);
			if ( args[k].startsWith("-F") )
				fileName = args[k].substring(2);
			
			k++;	
		}

		try
		{
			long start = System.currentTimeMillis();
			if (TASK_FILL.equals(taskName))
			{
				JasperPrint jasperPrint = getJasperPrint();
				JRSaver.saveObject(jasperPrint, fileName);
				System.err.println("Filling time : " + (System.currentTimeMillis() - start));
				System.exit(0);
			}
			else if (TASK_PRINT.equals(taskName))
			{
				PrintRequestAttributeSet printRequestAttributeSet = new HashPrintRequestAttributeSet();
				printRequestAttributeSet.add(MediaSizeName.ISO_A4);

				PrintServiceAttributeSet printServiceAttributeSet = new HashPrintServiceAttributeSet();
				//printServiceAttributeSet.add(new PrinterName("Epson Stylus 800 ESC/P 2", null));
				//printServiceAttributeSet.add(new PrinterName("HP LaserJet 4P", null));
				
				JRPrintServiceExporter exporter = new JRPrintServiceExporter();
				
				exporter.setParameter(JRExporterParameter.INPUT_FILE_NAME, fileName);
				exporter.setParameter(JRPrintServiceExporterParameter.PRINT_REQUEST_ATTRIBUTE_SET, printRequestAttributeSet);
				exporter.setParameter(JRPrintServiceExporterParameter.PRINT_SERVICE_ATTRIBUTE_SET, printServiceAttributeSet);
				exporter.setParameter(JRPrintServiceExporterParameter.DISPLAY_PAGE_DIALOG, Boolean.FALSE);
				exporter.setParameter(JRPrintServiceExporterParameter.DISPLAY_PRINT_DIALOG, Boolean.TRUE);
				
				exporter.exportReport();

				System.err.println("Printing time : " + (System.currentTimeMillis() - start));
				System.exit(0);
			}
			else
			{
				usage();
				System.exit(0);
			}
		}
		catch (JRException e)
		{
			e.printStackTrace();
			System.exit(1);
		}
		catch (Exception e)
		{
			e.printStackTrace();
			System.exit(1);
		}
	}


	/**
	 *
	 */
	private static void usage()
	{
		System.out.println( "PrintServiceApp usage:" );
		System.out.println( "\tjava PrintServiceApp -Ttask -Ffile" );
		System.out.println( "\tTasks : fill | print" );
	}


	/**
	 *
	 */
	private static JasperPrint getJasperPrint() throws JRException
	{
		//JasperPrint
		JasperPrint jasperPrint = new JasperPrint();
		jasperPrint.setName("NoReport");
		jasperPrint.setPageWidth(595);
		jasperPrint.setPageHeight(842);
		
		//Fonts
		JRDesignReportFont normalFont = new JRDesignReportFont();
		normalFont.setName("Arial_Normal");
		normalFont.setDefault(true);
		normalFont.setFontName("Arial");
		normalFont.setSize(8);
		normalFont.setPdfFontName("Helvetica");
		normalFont.setPdfEncoding("Cp1252");
		normalFont.setPdfEmbedded(false);
		jasperPrint.addFont(normalFont);

		JRDesignReportFont boldFont = new JRDesignReportFont();
		boldFont.setName("Arial_Bold");
		boldFont.setDefault(false);
		boldFont.setFontName("Arial");
		boldFont.setSize(8);
		boldFont.setBold(true);
		boldFont.setPdfFontName("Helvetica-Bold");
		boldFont.setPdfEncoding("Cp1252");
		boldFont.setPdfEmbedded(false);
		jasperPrint.addFont(boldFont);

		JRDesignReportFont italicFont = new JRDesignReportFont();
		italicFont.setName("Arial_Italic");
		italicFont.setDefault(false);
		italicFont.setFontName("Arial");
		italicFont.setSize(8);
		italicFont.setItalic(true);
		italicFont.setPdfFontName("Helvetica-Oblique");
		italicFont.setPdfEncoding("Cp1252");
		italicFont.setPdfEmbedded(false);
		jasperPrint.addFont(italicFont);
		
		JRPrintPage page = new JRBasePrintPage();

		JRPrintLine line = new JRBasePrintLine();
		line.setX(40);
		line.setY(50);
		line.setWidth(515);
		line.setHeight(0);
		page.addElement(line);
		
		JRPrintImage image = new JRBasePrintImage();
		image.setX(45);
		image.setY(55);
		image.setWidth(165);
		image.setHeight(40);
		image.setScaleImage(JRImage.SCALE_IMAGE_CLIP);
		image.setRenderer(
			JRImageRenderer.getInstance(
				JRImageLoader.loadImageDataFromLocation("jasperreports.gif")
				)
			);
		page.addElement(image);

		JRPrintText text = new JRBasePrintText();
		text.setX(210);
		text.setY(55);
		text.setWidth(345);
		text.setHeight(30);
		text.setTextAlignment(JRTextElement.TEXT_ALIGN_RIGHT);
		text.setLineSpacingFactor(1.3133681f);
		text.setLeadingOffset(-4.955078f);
		JRDesignFont font = new JRDesignFont();
		font.setReportFont(boldFont);
		font.setSize(18);
		text.setFont(font);
		text.setText("JasperReports Project Description");
		page.addElement(text);

		text = new JRBasePrintText();
		text.setX(210);
		text.setY(85);
		text.setWidth(325);
		text.setHeight(15);
		text.setTextAlignment(JRTextElement.TEXT_ALIGN_RIGHT);
		text.setLineSpacingFactor(1.329241f);
		text.setLeadingOffset(-4.076172f);
		font = new JRDesignFont();
		font.setReportFont(italicFont);
		font.setSize(12);
		text.setFont(font);
		text.setText((new SimpleDateFormat("EEE, MMM d, yyyy")).format(new Date()));
		page.addElement(text);

		text = new JRBasePrintText();
		text.setX(40);
		text.setY(150);
		text.setWidth(515);
		text.setHeight(200);
		text.setTextAlignment(JRTextElement.TEXT_ALIGN_JUSTIFIED);
		text.setLineSpacingFactor(1.329241f);
		text.setLeadingOffset(-4.076172f);
		font = new JRDesignFont();
		font.setReportFont(normalFont);
		font.setSize(14);
		text.setFont(font);
		text.setText(
			"JasperReports is a powerful report-generating tool that has the ability to deliver rich content onto the screen, to the printer or into PDF, HTML, XLS, CSV or XML files.\n\n" +
			"It is entirely written in Java and can be used in a variety of Java enabled applications, including J2EE or Web applications, to generate dynamic content.\n\n" +
			"Its main purpose is to help creating page oriented, ready to print documents in a simple and flexible manner."
			);
		page.addElement(text);

		jasperPrint.addPage(page);

		return jasperPrint;
	}
	

}
