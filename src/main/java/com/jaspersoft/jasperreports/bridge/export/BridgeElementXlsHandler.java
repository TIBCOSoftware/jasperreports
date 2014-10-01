/*
 * Copyright (C) 2005 - 2009 Jaspersoft Corporation. All rights reserved.
 * http://www.jaspersoft.com
 * Licensed under commercial Jaspersoft Subscription License Agreement
 */
package com.jaspersoft.jasperreports.bridge.export;

import net.sf.jasperreports.engine.JRGenericPrintElement;
import net.sf.jasperreports.engine.JRPrintImage;
import net.sf.jasperreports.engine.JRRuntimeException;
import net.sf.jasperreports.engine.export.GenericElementXlsHandler;
import net.sf.jasperreports.engine.export.JRExporterGridCell;
import net.sf.jasperreports.engine.export.JRGridLayout;
import net.sf.jasperreports.engine.export.JRXlsExporter;
import net.sf.jasperreports.engine.export.JRXlsExporterContext;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * @author Giulio Toffoli (gtoffoli@tibco.com)
 */
public class BridgeElementXlsHandler implements GenericElementXlsHandler {
	
	private static final BridgeElementXlsHandler INSTANCE = new BridgeElementXlsHandler();
	private static final Log log = LogFactory.getLog(BridgeElementXlsHandler.class);
	
        
        public static BridgeElementXlsHandler getInstance()
	{
		return INSTANCE;
	}

	@Override
	public boolean toExport(JRGenericPrintElement element) {
		return true;
	}

	@Override
	public void exportElement(JRXlsExporterContext exporterContext,
			JRGenericPrintElement element, JRExporterGridCell gridCell,
			int colIndex, int rowIndex, int emptyCols, int yCutsRow,
			JRGridLayout layout) {
		if (log.isDebugEnabled()) {
			log.debug("Exporting to XLS " + element);
		}
		
		try {
			JRPrintImage chartImage = BridgeElementImageProvider.getDefaultProvider().getImage(exporterContext.getJasperReportsContext(), element, false);
			JRXlsExporter exporter = (JRXlsExporter) exporterContext.getExporter();
			exporter.exportImage(chartImage, gridCell, colIndex, rowIndex, emptyCols, yCutsRow, layout);
		} catch (Exception e) {
			throw new JRRuntimeException(e);
		}
	}

	
}
