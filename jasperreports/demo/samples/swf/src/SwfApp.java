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
import java.awt.Image;
import java.awt.MediaTracker;
import java.awt.Panel;
import java.awt.Toolkit;
import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.export.JRRtfExporter;
import net.sf.jasperreports.engine.export.JRSwfExporter;
import net.sf.jasperreports.engine.util.JRLoader;


/**
 * @author Sanda Zaharia (szaharia@users.sourceforge.net)
 * @version $Id$
 */
public class SwfApp
{


	/**
	 *
	 */
	private static final String TASK_FILL = "fill";
    private static final String TASK_SWF = "swf";
	private static final String TASK_WRITE_XML = "writeXml";

	//using empty data source
    private static final String[] reportNames1 = 
    {
    "reports/FontsReport",
    "reports/StyledTextReport",
    "reports/RotationReport",
    "reports/ShapesReport",
    "reports/ImagesReport"
    };

    //using connection
    private static final String[] reportNames2 = 
    {
        "reports/FirstJasper",
        "reports/HorizontalReport",
        "reports/PieChartReport",
        "reports/OrdersReport",
        "reports/ProductsReport",
        "reports/ShipmentsReport",
        "reports/LateOrdersReport"
    };

	/**
	 *
	 */
	public static void main(String[] args)
	{
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
			
			k++;	
		}

		try
		{
			if (TASK_FILL.equals(taskName))
			{
                //Preparing parameters
                Image image = Toolkit.getDefaultToolkit().createImage("reports/dukesign.jpg");
                MediaTracker traker = new MediaTracker(new Panel());
                traker.addImage(image, 0);
                try
                {
                    traker.waitForID(0);
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }

			    Map parameters = new HashMap();
				parameters.put("MaxOrderID", new Integer(12500));
				parameters.put("SummaryImage", image);
                for(int i = 0; i < reportNames1.length; i++)
                {
                    long start = System.currentTimeMillis();
                    JasperFillManager.fillReportToFile("./build/"+reportNames1[i] + ".jasper", parameters, new JREmptyDataSource());
                    System.err.println("Report : " + reportNames1[i] + ". Filling time : " + (System.currentTimeMillis() - start));
                }

                for(int i = 0; i < reportNames2.length; i++)
                {
                    long start = System.currentTimeMillis();
                    JasperFillManager.fillReportToFile("./build/"+reportNames2[i] + ".jasper", parameters, getConnection());
                    System.err.println("Report : " + reportNames2[i] + ". Filling time : " + (System.currentTimeMillis() - start));
                }

				System.exit(0);
			}
            else if (TASK_SWF.equals(taskName))
            {
                System.out.println("swf export ..... ");
                JRSwfExporter exporter = new JRSwfExporter();

                for(int i = 0; i < reportNames1.length; i++)
                {
                    exportReportToSWF(exporter,"./build/"+reportNames1[i]);
                }

                for(int i = 0; i < reportNames2.length; i++)
                {
                    exportReportToSWF(exporter,"./build/"+reportNames2[i]);
                }

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
		System.out.println( "ChartsApp usage:" );
		System.out.println( "\tjava ChartsApp -Ttask" );
		System.out.println( "\tTasks : fill | pdf | html" );
	}


	/**
	 *
	 */
	private static Connection getConnection() throws ClassNotFoundException, SQLException
	{
		//Change these settings according to your local configuration
		String driver = "org.hsqldb.jdbcDriver";
		String connectString = "jdbc:hsqldb:hsql://localhost";
		String user = "sa";
		String password = "";


		Class.forName(driver);
		Connection conn = DriverManager.getConnection(connectString, user, password);
		return conn;
	}
	
    public static final Date truncateToMonth(Date date)
    {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        calendar.clear();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month);
        return calendar.getTime();
    }
    
    
    public static final Date truncateToYear(Date date)
    {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int year = calendar.get(Calendar.YEAR);
        calendar.clear();
        calendar.set(Calendar.YEAR, year);
        return calendar.getTime();
    }

    private static void exportReportToSWF(JRSwfExporter exporter, String reportName) throws Exception
    {
        long start = System.currentTimeMillis();
        File sourceFile = new File(reportName + ".jrprint");
        
        JasperPrint jasperPrint = (JasperPrint)JRLoader.loadObject(sourceFile);

        File destFile = new File(reportName + ".swf");
        
        exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
        exporter.setParameter(JRExporterParameter.OUTPUT_FILE_NAME, destFile.toString());
        
        exporter.exportReport();

        System.err.println("Report : " + reportName + ". SWF export time : " + (System.currentTimeMillis() - start));
        
    }

}
