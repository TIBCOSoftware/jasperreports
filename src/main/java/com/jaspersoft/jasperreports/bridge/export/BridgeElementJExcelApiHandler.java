/*
 * Copyright (C) 2005 - 2009 Jaspersoft Corporation. All rights reserved.
 * http://www.jaspersoft.com
 * Licensed under commercial Jaspersoft Subscription License Agreement
 */
package com.jaspersoft.jasperreports.bridge.export;

import net.sf.jasperreports.engine.JRGenericPrintElement;
import net.sf.jasperreports.engine.JRPrintImage;
import net.sf.jasperreports.engine.JRRuntimeException;
import net.sf.jasperreports.engine.export.GenericElementJExcelApiHandler;
import net.sf.jasperreports.engine.export.JExcelApiExporter;
import net.sf.jasperreports.engine.export.JExcelApiExporterContext;
import net.sf.jasperreports.engine.export.JRExporterGridCell;
import net.sf.jasperreports.engine.export.JRGridLayout;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id$
 */
public class BridgeElementJExcelApiHandler implements GenericElementJExcelApiHandler {
	
	private static final BridgeElementJExcelApiHandler INSTANCE = new BridgeElementJExcelApiHandler();
	private static final Log log = LogFactory.getLog(BridgeElementJExcelApiHandler.class);
	
        
        public static BridgeElementJExcelApiHandler getInstance()
	{
		return INSTANCE;
	}

	@Override
	public void exportElement(JExcelApiExporterContext exporterContext,
			JRGenericPrintElement element, JRExporterGridCell gridCell,
			int colIndex, int rowIndex, int emptyCols, int yCutsRow,
			JRGridLayout layout) {
		if (log.isDebugEnabled()) {
			log.debug("Exporting to JExcel " + element);
		}
		
		try {
			JRPrintImage chartImage = BridgeElementImageProvider.getDefaultProvider().getImage(exporterContext.getJasperReportsContext(), element, false);
			JExcelApiExporter exporter = (JExcelApiExporter) exporterContext.getExporter();
			exporter.exportImage(chartImage, gridCell, colIndex, rowIndex, emptyCols, yCutsRow, layout);
		} catch (Exception e) {
			throw new JRRuntimeException(e);
		}
	}

        @Override
        public boolean toExport(JRGenericPrintElement jrgpe) {
            return true;
        }

}
