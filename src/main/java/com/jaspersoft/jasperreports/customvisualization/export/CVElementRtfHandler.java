/*
 * Copyright (C) 2005 - 2009 Jaspersoft Corporation. All rights reserved.
 * http://www.jaspersoft.com
 * Licensed under commercial Jaspersoft Subscription License Agreement
 */
package com.jaspersoft.jasperreports.customvisualization.export;

import net.sf.jasperreports.engine.JRGenericPrintElement;
import net.sf.jasperreports.engine.JRPrintImage;
import net.sf.jasperreports.engine.JRRuntimeException;
import net.sf.jasperreports.engine.export.GenericElementRtfHandler;
import net.sf.jasperreports.engine.export.JRRtfExporter;
import net.sf.jasperreports.engine.export.JRRtfExporterContext;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


/**
 *
 * @author Giulio Toffoli (gtoffoli@tibco.com)
 */
public class CVElementRtfHandler implements GenericElementRtfHandler {
	
	private static final CVElementRtfHandler INSTANCE = new CVElementRtfHandler();
	private static final Log log = LogFactory.getLog(CVElementRtfHandler.class);
	
        
        public static CVElementRtfHandler getInstance()
	{
		return INSTANCE;
	}

	@Override
	public boolean toExport(JRGenericPrintElement element) {
		return true;
	}

	@Override
	public void exportElement(JRRtfExporterContext exporterContext,
			JRGenericPrintElement element) {
		if (log.isDebugEnabled()) {
			log.debug("Exporting to RTF " + element);
		}
		
		try {
			JRPrintImage chartImage = CVElementImageProvider.getDefaultProvider().getImage(exporterContext.getJasperReportsContext(), element, false);
			JRRtfExporter exporter = (JRRtfExporter) exporterContext.getExporter();
			exporter.exportImage(chartImage);
		} catch (Exception e) {
			throw new JRRuntimeException(e);
		}
	}

	
}
