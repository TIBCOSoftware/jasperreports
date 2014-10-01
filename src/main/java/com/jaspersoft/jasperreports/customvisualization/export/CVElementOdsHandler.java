/*
 * Copyright (C) 2005 - 2014 Jaspersoft Corporation. All rights reserved.
 * http://www.jaspersoft.com
 * Licensed under commercial Jaspersoft Subscription License Agreement
 */
package com.jaspersoft.jasperreports.customvisualization.export;

import net.sf.jasperreports.engine.JRGenericPrintElement;
import net.sf.jasperreports.engine.JRPrintImage;
import net.sf.jasperreports.engine.JRRuntimeException;
import net.sf.jasperreports.engine.export.JRExporterGridCell;
import net.sf.jasperreports.engine.export.JRGridLayout;
import net.sf.jasperreports.engine.export.oasis.GenericElementOdsHandler;
import net.sf.jasperreports.engine.export.oasis.JROdsExporter;
import net.sf.jasperreports.engine.export.oasis.JROdsExporterContext;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * @author Giulio Toffoli (gtoffoli@tibco.com)
 */
public class CVElementOdsHandler implements GenericElementOdsHandler {
	
	private static final CVElementOdsHandler INSTANCE = new CVElementOdsHandler();
	private static final Log log = LogFactory.getLog(CVElementOdsHandler.class);
	
        
        public static CVElementOdsHandler getInstance()
	{
		return INSTANCE;
	}
	
	@Override
	public boolean toExport(JRGenericPrintElement element) {
		return true;
	}

	@Override
	public void exportElement(
		JROdsExporterContext exporterContext,
		JRGenericPrintElement element, 
		JRExporterGridCell gridCell,
		int colIndex,
		int rowIndex,
		int emptyCols,
		int yCutsRow, 
		JRGridLayout layout
		) 
	{
		if (log.isDebugEnabled()) {
			log.debug("Exporting to ODS " + element);
		}
		
		try {
			JRPrintImage chartImage = CVElementImageProvider.getDefaultProvider().getImage(exporterContext.getJasperReportsContext(), element, false);
			JROdsExporter exporter = (JROdsExporter) exporterContext.getExporterRef();
			exporter.exportImage(chartImage, gridCell, colIndex, rowIndex, emptyCols, yCutsRow, layout);
		} catch (Exception e) {
			throw new JRRuntimeException(e);
		}
	}

}
