/*******************************************************************************
 * Copyright (C) 2005 - 2014 TIBCO Software Inc. All rights reserved.
 * http://www.jaspersoft.com.
 * 
 * Unless you have purchased  a commercial license agreement from Jaspersoft,
 * the following license terms  apply:
 * 
 * The Custom Visualization Component program and the accompanying materials
 * has been dual licensed under the the following licenses:
 * 
 * Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * The Custom Visualization Component is distributed in the hope that it will be
 * useful, but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License.
 * If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/
package com.jaspersoft.jasperreports.customvisualization.export;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import net.sf.jasperreports.engine.JRGenericPrintElement;
import net.sf.jasperreports.engine.JRRuntimeException;
import net.sf.jasperreports.engine.JasperReportsContext;
import net.sf.jasperreports.engine.type.OnErrorTypeEnum;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.jaspersoft.jasperreports.customvisualization.CVPrintElement;

/**
 * @author Giulio Toffoli (gtoffoli@tibco.com)
 */
public class CVElementPhantomJSImageProvider extends CVElementImageProvider
{
    
        private static final Log log = LogFactory.getLog(CVElementPhantomJSImageProvider.class);
        private static final Random RANDOM = new Random();
    
        public static final String PROPERTY_PHANTOMJS_EXECUTABLE_PATH = "com.jaspersoft.jasperreports.components.customvisualization.phantomjs.executable.path";
        public static final String PROPERTY_PHANTOMJS_EXECUTABLE_TIMEOUT = "com.jaspersoft.jasperreports.highcharts.phantomjs.executable.timeout";
        public static final String PROPERTY_PHANTOMJS_TEMPDIR_PATH = "com.jaspersoft.jasperreports.highcharts.phantomjs.tempdir.path";
        
        public static final String DIV2SVG_SCRIPT = "com/jaspersoft/jasperreports/customvisualization/scripts/div2svg.js";
        public static final String SVG2PNG_SCRIPT = "com/jaspersoft/jasperreports/customvisualization/scripts/svg2png.js";
        public static final String ERROR_SVG = "com/jaspersoft/jasperreports/customvisualization/export/error.svg";
        
        
        @Override
	public byte[] createPngImage(JasperReportsContext jasperReportsContext, JRGenericPrintElement element) {
                try {            
                        // Generates a PNG by using Phantom JS

                        return getImageData(jasperReportsContext, element, false);
                    
                } catch (Exception ex) {
                
                    throw new JRRuntimeException(ex);
                }
        }   
        
        @Override
        public String createSvgImage(JasperReportsContext jasperReportsContext, JRGenericPrintElement element) {
                try {            
                    // Generates a PNG by using Phantom JS
                    return new String(getImageData(jasperReportsContext, element, true), "UTF-8");
                
                } catch (Exception ex) {
                    
                    
                    log.error("Generating image for Custom Visualization element " + element.hashCode() +" failed.", ex);
                    
                    
                    OnErrorTypeEnum onErrorType = element.getParameterValue(CVPrintElement.PARAMETER_ON_ERROR_TYPE) == null 
				? CVPrintElement.DEFAULT_ON_ERROR_TYPE  
				: OnErrorTypeEnum.getByName((String)element.getParameterValue(CVPrintElement.PARAMETER_ON_ERROR_TYPE));
                    
                    
                    switch (onErrorType)
                    {
                        case ICON: 
	                    return createErrorSVG(ex.getMessage());
                        case BLANK:
                            return createBlankSVG(element.getWidth(), element.getHeight());
                        default: // ERROR
                            throw new JRRuntimeException(ex);
                    } 
                }
        }   
        
        public static String createErrorSVG(String error)
        {
            try {
                ByteArrayOutputStream os = new ByteArrayOutputStream();
                copyStream( CVElementPhantomJSImageProvider.class.getClassLoader().getResourceAsStream(ERROR_SVG) , os);
                String svg = os.toString("UTF-8");

                
                // tokenize the message and prepare it...
                if (error != null)
                {
                    String[] errorLines = error.split("\n");

                    StringBuffer svgMessage = new StringBuffer();
                    int dy = 0;
                    
                    for (int i=0; i<errorLines.length; ++i)
                    {
                        String line = errorLines[i];
                        
                        // Split string in max 55 characters...
                        while (line.length() > 0)
                        {
                            String printable = line.substring(0, Math.min(line.length(), 55));
                            line = line.length() <= 55 ? "" : line.substring(55);
                            svgMessage.append("<tspan x=\"280\" y=\"" + (255 + dy) + "\" id=\"line2" + i + "\"><![CDATA[" + printable.trim() + "]]></tspan>");
                            dy += 15;
                       }
                    }

                    if (svgMessage.length() > 0)
                    {
                        svg = svg.replaceFirst("<tspan x=\"280\" y=\"255\" id=\"line2\">Check the log for more details.</tspan>", svgMessage.toString() );
                    }
                }
                
                return svg;
                
            } catch (Exception ex){
                ex.printStackTrace();
            }
            
            return null;
        }
        
        
        public static String createBlankSVG(int w, int h)
        {
                return "<svg xmlns=\"http://www.w3.org/2000/svg\"  xmlns:xlink=\"http://www.w3.org/1999/xlink\"  width='" + w + "px' height='" + h + "px'></svg>";
   
        }
        
        
        /**
         * Returns the location of a newly created image,
         * 
         * @param exporterContext
         * @param element
         * @param svg boolean, true to get an SVG, false to get a PNG
         * @return
         * @throws Exception 
         */
        public byte[] getImageData(JasperReportsContext jasperReportsContext, JRGenericPrintElement element, boolean svg) throws Exception {
            
                if (element.getParameterValue( CVPrintElement.CONFIGURATION) == null) throw new JRRuntimeException("Configuration object is null.");
            
		String phantomjsExecutablePath = "phantomjs"; 
                
                if (jasperReportsContext.getProperty(CVElementPhantomJSImageProvider.PROPERTY_PHANTOMJS_EXECUTABLE_PATH) != null)
                {
                    phantomjsExecutablePath = jasperReportsContext.getProperty(CVElementPhantomJSImageProvider.PROPERTY_PHANTOMJS_EXECUTABLE_PATH);
                }
		
                int phantomjsTimeout; 
                String timeoutProperty = jasperReportsContext.getProperty(CVElementPhantomJSImageProvider.PROPERTY_PHANTOMJS_EXECUTABLE_TIMEOUT);
                if (timeoutProperty != null) {
                        phantomjsTimeout = Integer.parseInt(timeoutProperty);
                } else {
                        phantomjsTimeout = 60000;
                }
                            
		String phantomjsTempFolderPath = jasperReportsContext.getProperty(CVElementPhantomJSImageProvider.PROPERTY_PHANTOMJS_TEMPDIR_PATH);
		
		if (phantomjsTempFolderPath == null) {
			phantomjsTempFolderPath = System.getProperty("java.io.tmpdir");
		}
                File tempFolder = new File(phantomjsTempFolderPath);
                
                if (tempFolder.exists()) {
			String tempFileName = TEMP_RESOURCE_PREFIX + System.currentTimeMillis() + "_" + RANDOM.nextInt(Integer.MAX_VALUE);

                        // Collect the files that must be deleted as we finish to render
                        // everything...
                        List<File> cleanableResourcePaths = new ArrayList<File>();
                        
                        try {
                        
                                log.debug("Saving Div2Svg.js in a temporary directory: " + CVElementPhantomJSImageProvider.class.getClassLoader().getResource(DIV2SVG_SCRIPT) + " " + new File(tempFolder, tempFileName+ "_div2svg.js"));
                                File div2svgFile = createFile(new File(tempFolder, tempFileName+ "_div2svg.js"), CVElementPhantomJSImageProvider.class.getClassLoader().getResourceAsStream(DIV2SVG_SCRIPT));
                                cleanableResourcePaths.add(div2svgFile);

                                File svg2pngFile = createFile(new File(tempFolder, tempFileName+ "_svg2png.js"), CVElementPhantomJSImageProvider.class.getClassLoader().getResourceAsStream(SVG2PNG_SCRIPT));
                                cleanableResourcePaths.add(svg2pngFile);


                                StringBuilder htmlPage = new StringBuilder();
                                htmlPage.append("<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">\n<html><head><meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\"/></head><body>");
                                
                                String htmlFragment = CVElementHtmlHandler.getInstance().getHtmlFragment(jasperReportsContext, null, element);
                                
                                
                                htmlPage.append( htmlFragment );
                                htmlPage.append("</body></html>");

                                File htmlPageFile = createFile(new File(tempFolder, tempFileName+ ".html"), new ByteArrayInputStream( htmlPage.toString().getBytes() ));
                                cleanableResourcePaths.add(htmlPageFile);

                                File outputSvgFile = new File(tempFolder, tempFileName + ".svg");
                                
                                // We should not delete the SVG file until it is actually consumed...
                                cleanableResourcePaths.add(outputSvgFile);

                                try {
                                    runCommand(new String[]{phantomjsExecutablePath,
                                                        div2svgFile.toString(),
                                                        htmlPageFile.toURI().toURL().toString(),
                                                        outputSvgFile.toString() }, tempFolder, phantomjsTimeout);

                                } catch (Exception ex)
                                {
                                    throw new JRRuntimeException("Error while executing the javascript file to generate the SVG image: " + ex.getMessage());
                                }
                                
                                if (!outputSvgFile.exists() || outputSvgFile.length() <= 0)
                                {
                                    throw new JRRuntimeException("Error while executing the javascript file to generate the SVG image.");
                                }

                                if (svg) {
                                    
                                     ByteArrayOutputStream os = new ByteArrayOutputStream();
                                     copyStream( new FileInputStream(outputSvgFile) , os);

                                     return os.toByteArray();
                                }


                                File outputPngFile = new File(tempFolder, tempFileName + ".png");
                                cleanableResourcePaths.add(outputPngFile);

                                try {
                                    runCommand(new String[]{phantomjsExecutablePath, 
                                                        svg2pngFile.toString(),
                                                        outputSvgFile.toURI().toURL().toString(),
                                                        outputPngFile.toString() }, tempFolder, phantomjsTimeout);
                                } catch (Exception ex)
                                {
                                    throw new JRRuntimeException("Unable to convert the SVG file into a PNG image: " + ex.getMessage());
                                }
                                
                                if (!outputPngFile.exists() || outputPngFile.length() <= 0)
                                {
                                    throw new JRRuntimeException("Unable to convert the SVG file into a PNG image");
                                }

                                
                                ByteArrayOutputStream os = new ByteArrayOutputStream();
                                copyStream( new FileInputStream(outputPngFile) , os);

                                return os.toByteArray();
        	
                        } finally {
                            
                            for (File cleanableResource: cleanableResourcePaths) {
                                if (cleanableResource.exists() && cleanableResource.canWrite()) {
                                        if (element.getPropertiesMap().getProperty("cv.keepTemporaryFiles") == null ||
                                            !element.getPropertiesMap().getProperty("cv.keepTemporaryFiles").equals("true"))
                                        {
                                            if (log.isDebugEnabled()) {
                                                log.debug("Cleaning up resource after rendering of element " + element.hashCode() + ": " + cleanableResource.getAbsolutePath());
                                            }
                                            //cleanableResource.delete();
                                        }
                                }
                            }
                        }
                        
                        
                        
                } else {
                        throw new JRRuntimeException("Temp folder '" + tempFolder + "' does not exist!");
                }
        }
        
        
        /**
         * Executes a command within the given timeout.
         * 
         * @param args
         * @param currentDirectory
         * @param timeout
         * @return 
         */
        private static void runCommand(String[] args, File currentDirectory, final int timeout)
        {
            
                        Thread loggingThread = null;
                        Thread interruptingThread = null;
                                
                 	try {
				
                            String cmd = "";
                            for (String arg : args)
                            {
                                cmd += " " + arg;
                            }
                            
                            if (log.isDebugEnabled()) {
                                    log.debug("Executing external command: " + cmd);
                            }
                            System.out.println(cmd);
			
                            ProcessBuilder pb = new ProcessBuilder(Arrays.asList(args));
                            pb.directory(currentDirectory);
					
			    final Process externalProcess = pb.start();
                            final StringBuilder processOutput = new StringBuilder();
                            
                            final boolean[] success = new boolean[1];
                            success[0] = false;
                            
                            loggingThread = new Thread(new Runnable(){
                                    @Override
                                    public void run() {
                                            BufferedReader br = null;
                                            try {
                                                    br = new BufferedReader(new InputStreamReader(externalProcess.getInputStream()));
                                                    String line;
                                                    while((line = br.readLine()) != null) {
                                                            processOutput.append(line).append("\n");
                                                            
                                                            if (line.indexOf("SCRIPT_SUCCESS") >= 0)
                                                            {
                                                                success[0] = true;
                                                                killProcess(externalProcess, 100);
                                                            }
                                                            else if (line.indexOf("SCRIPT_ERROR") >= 0)
                                                            {
                                                                success[0] = false;
                                                                killProcess(externalProcess, 100);
                                                            }
                                                    }

                                                    if (log.isDebugEnabled()) {
                                                            log.debug("External process output:\n" + processOutput.toString());
                                                    }
                                            } catch (IOException e) {
                                                    if (log.isDebugEnabled()) {
                                                            log.debug(e.getMessage());
                                                    }
                                            } finally {
                                                    if (br != null) {
                                                            try {
                                                                    br.close();
                                                            } catch (IOException e) {
                                                                    if (log.isWarnEnabled()) {
                                                                            log.warn("Failed to close phantomjs process' inputstream", e);
                                                                    }
                                                            }
                                                    }
                                            }
                                    }
                            });
					
                            interruptingThread = new Thread(new Runnable() {
                                    @Override
                                    public void run() {
                                        if (killProcess(externalProcess, timeout))
                                        {
                                            success[0] = false;
                                        }
                                    }

                                
                            });
                            loggingThread.start();
                            interruptingThread.start();
                            externalProcess.waitFor();

                            if (externalProcess.exitValue() != 0 || !success[0]) {
                                    //FIXME we should do loggingThread.join(millis) because the process might end before its output if fully processed
                                
                                    throw new JRRuntimeException(
                                                    "External process did not end properly; exit value: " + externalProcess.exitValue() 
                                                    + (processOutput.length() > 0 ? "; process output:\n" + processOutput + "\n" : "."));
                            }
                            
                            

                        } catch (IOException e) {
				throw new JRRuntimeException(e);
			} catch (InterruptedException e) {
				throw new JRRuntimeException(e);
			} finally {
                            
                            if (interruptingThread != null && interruptingThread.isAlive()) try { interruptingThread.interrupt(); } catch (Exception ex){}
                            if (loggingThread != null && loggingThread.isAlive()) try { loggingThread.interrupt(); } catch (Exception ex){}
                           
                            
			}
		
        }
	
        
        /**
         * Kill a process, if still active, after millisDelay
         * 
         * @param externalProcess
         * @param millisDelay
         * @return true if the process had to be terminated, false if the process exited before the timeout
         */
        public static boolean killProcess(Process externalProcess, int millisDelay) {
                    
                    try {
                            Thread.sleep(millisDelay);
                    } catch (InterruptedException e) {
                          //  e.printStackTrace();
                    }
            
                    try {
                            int exitValue = externalProcess.exitValue();
                            if (log.isDebugEnabled()) {
                                    log.debug("External Process monitoring thread - exit value: " + exitValue);
                            }
                            return false;
                    } catch(IllegalThreadStateException e) {
                            if (log.isDebugEnabled()) {
                                    log.debug("External Process monitoring thread - destroying process");
                            }
                            externalProcess.destroy();
                            return true;
                    }
        }
}
