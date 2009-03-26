/*
 * ============================================================================
 * GNU Lesser General Public License
 * ============================================================================
 *
 * JasperReports - Free Java report-generating library.
 * Copyright (C) 2005 Works, Inc.  http://www.works.com/
 * 
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307, USA.
 * 
 * Works, Inc.
 * 6034 West Courtyard Drive
 * Suite 210
 * Austin, TX 78730-5032
 * USA
 * http://www.works.com/
 */

/*
 * Licensed to JasperSoft Corporation under a Contributer Agreement
 */
package net.sf.jasperreports.engine.fill;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import net.sf.jasperreports.engine.JRVirtualizable;

/**
 * GZips the pages that it doesn't need, but keeps them in memory.
 * 
 * @author John Bindel
 * @version $Id$
 */
public class JRGzipVirtualizer extends JRAbstractLRUVirtualizer
{
	private final Map zippedData;

	/**
	 * @param maxSize
	 *            the maximum size (in JRVirtualizable objects) of the paged in
	 *            cache.
	 */
	public JRGzipVirtualizer(int maxSize) {
		super(maxSize);
		this.zippedData = Collections.synchronizedMap(new HashMap());
	}

	protected void dispose(String virtualId) {
		zippedData.remove(virtualId);
	}

	protected void pageOut(JRVirtualizable o) throws IOException {
		if (!zippedData.containsKey(o.getUID())) {
			GZIPOutputStream gos = null;
			try {
				ByteArrayOutputStream baos = new ByteArrayOutputStream(3000);
				gos = new GZIPOutputStream(baos);
				writeData(o, gos);
				gos.finish();
				gos.flush();

				byte[] data = baos.toByteArray();
				zippedData.put(o.getUID(), data);
			}
			finally {
				if (gos != null) {
					gos.close();
				}
			}
		}
		else {
			if (!isReadOnly(o)) {
				throw new IllegalStateException(
						"Cannot virtualize data because the data for object UID \"" + o.getUID()
						+ "\" already exists.");
			}
		}
	}

	protected void pageIn(JRVirtualizable o) throws IOException {
		GZIPInputStream gis = null;
		try {
			byte[] data = (byte[]) zippedData.get(o.getUID());
			if (data == null) {
				throw new NullPointerException("No data found for object with UID " + o.getUID());
			}
			ByteArrayInputStream bais = new ByteArrayInputStream(data);
			gis = new GZIPInputStream(bais);
			readData(o, gis);
		}
		finally {
			if (gis != null) {
				gis.close();
			}
		}

		if (!isReadOnly(o)) {
			// Wait until we know it worked before tossing the data.
			zippedData.remove(o.getUID());
		}
	}

	public void cleanup()
	{
		zippedData.clear();
		reset();
	}
}
