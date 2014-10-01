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
import net.sf.jasperreports.engine.export.oasis.GenericElementOdtHandler;
import net.sf.jasperreports.engine.export.oasis.JROdtExporter;
import net.sf.jasperreports.engine.export.oasis.JROdtExporterContext;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


/**
 * @author Giulio Toffoli
 */
public class CVElementOdtHandler implements GenericElementOdtHandler {
	
	private static final CVElementOdtHandler INSTANCE = new CVElementOdtHandler();
	private static final Log log = LogFactory.getLog(CVElementOdtHandler.class);
	
        
        public static CVElementOdtHandler getInstance()
	{
		return INSTANCE;
	}

	@Override
	public boolean toExport(JRGenericPrintElement element) {
		return true;
	}

	@Override
	public void exportElement(JROdtExporterContext exporterContext,
			JRGenericPrintElement element, JRExporterGridCell gridCell) {
		if (log.isDebugEnabled()) {
			log.debug("Exporting to ODT " + element);
		}
		
		try {
			JRPrintImage chartImage = CVElementImageProvider.getDefaultProvider().getImage(exporterContext.getJasperReportsContext(), element, false);
			JROdtExporter exporter = (JROdtExporter) exporterContext.getExporterRef();
			exporter.exportImage(exporterContext.getTableBuilder(), chartImage, gridCell);
		} catch (Exception e) {
			throw new JRRuntimeException(e);
		}
	}

}
