/*
 * Copyright (C) 2005 - 2009 Jaspersoft Corporation. All rights reserved.
 * http://www.jaspersoft.com
 * Licensed under commercial Jaspersoft Subscription License Agreement
 */
package com.jaspersoft.jasperreports.bridge.export;

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
public class BridgeElementPptxHandler implements GenericElementPptxHandler {
	
	private static final BridgeElementPptxHandler INSTANCE = new BridgeElementPptxHandler();
	private static final Log log = LogFactory.getLog(BridgeElementPptxHandler.class);
	
        
        public static BridgeElementPptxHandler getInstance()
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
			JRPrintImage chartImage = BridgeElementImageProvider.getDefaultProvider().getImage(exporterContext.getJasperReportsContext(), element, false);
			JRPptxExporter exporter = (JRPptxExporter) exporterContext.getExporterRef();
			exporter.exportImage(chartImage);
		} catch (Exception e) {
			throw new JRRuntimeException(e);
		}
	}

}
