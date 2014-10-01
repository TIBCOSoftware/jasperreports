/*
 * Copyright (C) 2005 - 2009 Jaspersoft Corporation. All rights reserved.
 * http://www.jaspersoft.com
 * Licensed under commercial Jaspersoft Subscription License Agreement
 */
package com.jaspersoft.jasperreports.customvisualization.export;

import net.sf.jasperreports.engine.JRGenericPrintElement;
import net.sf.jasperreports.engine.JRPrintImage;
import net.sf.jasperreports.engine.JRRuntimeException;
import net.sf.jasperreports.engine.export.ooxml.GenericElementPptxHandler;
import net.sf.jasperreports.engine.export.ooxml.JRPptxExporter;
import net.sf.jasperreports.engine.export.ooxml.JRPptxExporterContext;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


/**
 *
 * @author Giulio Toffoli (gtoffoli@tibco.com)
 */
public class CVElementPptxHandler implements GenericElementPptxHandler {
	
	private static final CVElementPptxHandler INSTANCE = new CVElementPptxHandler();
	private static final Log log = LogFactory.getLog(CVElementPptxHandler.class);
	
        
        public static CVElementPptxHandler getInstance()
	{
		return INSTANCE;
	}

	@Override
	public boolean toExport(JRGenericPrintElement element) {
		return true;
	}

	@Override
	public void exportElement(JRPptxExporterContext exporterContext,
			JRGenericPrintElement element) {
		if (log.isDebugEnabled()) {
			log.debug("Exporting to PPTX " + element);
		}
		try {
			JRPrintImage chartImage = CVElementImageProvider.getDefaultProvider().getImage(exporterContext.getJasperReportsContext(), element, false);
			JRPptxExporter exporter = (JRPptxExporter) exporterContext.getExporterRef();
			exporter.exportImage(chartImage);
		} catch (Exception e) {
			throw new JRRuntimeException(e);
		}
	}

}
