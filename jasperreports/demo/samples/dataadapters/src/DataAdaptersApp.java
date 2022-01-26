/*
 * JasperReports - Free Java Reporting Library.
 * Copyright (C) 2001 - 2019 TIBCO Software Inc. All rights reserved.
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
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;

import net.sf.jasperreports.data.DataAdapter;
import net.sf.jasperreports.data.DataAdapterParameterContributorFactory;
import net.sf.jasperreports.data.http.HttpDataLocation;
import net.sf.jasperreports.data.http.StandardHttpDataLocation;
import net.sf.jasperreports.engine.DefaultJasperReportsContext;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.util.AbstractSampleApp;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.util.CastorUtil;
import net.sf.jasperreports.web.util.JacksonUtil;


/**
 * @author Sanda Zaharia (shertage@users.sourceforge.net)
 */
public class DataAdaptersApp extends AbstractSampleApp
{
	

	/**
	 *
	 */
	public static void main(String[] args)
	{
		main(new DataAdaptersApp(), args);
	}
	
	
	@Override
	public void test() throws JRException
	{
		testXml();	
	}


	private void testXml() throws JRException
	
	{
		File[] files = getFiles(new File("data"), "xml");
		
		for(int i = 0; i < files.length; i++)
		{
			File daFile = files[i];
			String daFileName = daFile.getName().substring(0, daFile.getName().indexOf('.'));
			boolean isHttpLocation = daFileName != null && daFileName.contains("HttpDataLocation");

			long start = System.currentTimeMillis();
			try (FileInputStream fis = new FileInputStream(daFile))
			{
				// READ old Castor format from sample, which has deprecated fileName field
				Object da = CastorUtil.getInstance(DefaultJasperReportsContext.getInstance()).read(fis);
				
				// WRITE Castor new format with dataFile node
				CastorUtil.getInstance(DefaultJasperReportsContext.getInstance()).writeToFile(da, "build/" + daFileName + ".castor.xml");
			}
			catch (IOException e)
			{
				throw new JRException(e);
			}

			try (FileInputStream fis = new FileInputStream("build/" + daFileName + ".castor.xml"))
			{
				// READ Castor latest data adapter format with Jackson
				Object da = isHttpLocation 
						? JacksonUtil.getInstance(DefaultJasperReportsContext.getInstance()).loadXml(fis, StandardHttpDataLocation.class)
						: JacksonUtil.getInstance(DefaultJasperReportsContext.getInstance()).loadXml(fis, DataAdapter.class);
				
				// WRITE Jackson format
				String xml = JacksonUtil.getInstance(DefaultJasperReportsContext.getInstance()).getXmlString(da);
				FileWriter writer = new FileWriter("build/" + daFileName + ".jackson.xml");
				writer.write(xml);
				writer.close();
			}
			catch (IOException e)
			{
				throw new JRException(e);
			}

			try (FileInputStream fis = new FileInputStream("build/" + daFileName + ".jackson.xml"))
			{				
				// READ Jackson data adapter own format
				Object da = isHttpLocation 
						? JacksonUtil.getInstance(DefaultJasperReportsContext.getInstance()).loadXml(fis, StandardHttpDataLocation.class)
						: JacksonUtil.getInstance(DefaultJasperReportsContext.getInstance()).loadXml(fis, DataAdapter.class);

				// WRITE Jackson again just to check read/save works
				String xml = JacksonUtil.getInstance(DefaultJasperReportsContext.getInstance()).getXmlString(da);
				FileWriter writer = new FileWriter("build/" + daFileName + ".jackson2.xml");
				writer.write(xml);
				writer.close();
			}
			catch (IOException e)
			{
				throw new JRException(e);
			}
			
			System.err.println(daFileName + " data adapters saved in " + (System.currentTimeMillis() - start) + " ms");
		}
	}
}
