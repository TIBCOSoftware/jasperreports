/*
 * Copyright (C) 2005 - 2009 Jaspersoft Corporation. All rights reserved.
 * http://www.jaspersoft.com
 * Licensed under commercial Jaspersoft Subscription License Agreement
 */
package com.jaspersoft.jasperreports.customvisualization.export;

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
public class CVElementXlsHandler implements GenericElementXlsHandler {
	
	private static final CVElementXlsHandler INSTANCE = new CVElementXlsHandler();
	private static final Log log = LogFactory.getLog(CVElementXlsHandler.class);
	
        
        public static CVElementXlsHandler getInstance()
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
			JRPrintImage chartImage = CVElementImageProvider.getDefaultProvider().getImage(exporterContext.getJasperReportsContext(), element, false);
			JRXlsExporter exporter = (JRXlsExporter) exporterContext.getExporter();
			exporter.exportImage(chartImage, gridCell, colIndex, rowIndex, emptyCols, yCutsRow, layout);
		} catch (Exception e) {
			throw new JRRuntimeException(e);
		}
	}

	
}
