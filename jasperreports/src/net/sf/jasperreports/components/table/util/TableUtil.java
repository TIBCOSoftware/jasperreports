/*
 * JasperReports - Free Java Reporting Library.
 * Copyright (C) 2001 - 2009 Jaspersoft Corporation. All rights reserved.
 * http://www.jaspersoft.com
 *
 * Unless you have purchased a commercial license agreement from Jaspersoft,
 * the following license terms apply:
 *
 * This program is part of JasperReports.
 *
 * JasperReports is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * JasperReports is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with JasperReports. If not, see <http://www.gnu.org/licenses/>.
 */
package net.sf.jasperreports.components.table.util;

import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

import net.sf.jasperreports.components.table.BaseColumn;
import net.sf.jasperreports.components.table.Cell;
import net.sf.jasperreports.components.table.Column;
import net.sf.jasperreports.components.table.ColumnGroup;
import net.sf.jasperreports.components.table.TableComponent;
import net.sf.jasperreports.engine.JRDataset;
import net.sf.jasperreports.engine.JRDatasetRun;
import net.sf.jasperreports.engine.JRGroup;
import net.sf.jasperreports.engine.design.JasperDesign;

/**
 * @author Veaceslav Chicu (schicu@users.sourceforge.net)
 * @version $Id: Column.java 3586 2010-03-12 18:17:13Z lucianc $
 */
public class TableUtil 
{
	public static final int TABLE_HEADER = 0;
	public static final int TABLE_FOOTER = 1;
	public static final int COLUMN_HEADER = 2;
	public static final int COLUMN_FOOTER = 3;
	public static final int COLUMN_DETAIL = 4;
	public static final int COLUMN_GROUP_HEADER = 5;
	public static final int COLUMN_GROUP_FOOTER = 6;
	
	private TableComponent table;
	private Map<Cell, Rectangle> boundsMap = new HashMap<Cell, Rectangle>();
	private JasperDesign jasperDesign;

	public TableUtil(TableComponent table, JasperDesign jasperDesign) {
		this.table = table;
		this.jasperDesign = jasperDesign;
		init(table);
	}

	public Map<Cell, Rectangle> getCellBounds() {
		return boundsMap;
	}

	public void refresh() {
		init(table);
	}

	public void init(TableComponent table) {
		boundsMap.clear();

		List<BaseColumn> allColumns = getAllColumns(table.getColumns());
		int y = 0;
		int h = 0;
		Rectangle r = new Rectangle(0, 0, 0, 0);
		for (BaseColumn bc : table.getColumns()) {
			r = initHeader(r, bc, TABLE_HEADER, null);
			r.setLocation(r.x , y);
			if (h < r.height)
				h = r.height;
		}
		y += h;
		r = new Rectangle(0, y, 0, 0);
		h = 0;
		for (BaseColumn bc : table.getColumns()) {
			r = initHeader(r, bc, COLUMN_HEADER, null);
			r.setLocation(r.x, y);
			if (h < r.height)
				h = r.height;
		}

		List<?> groupsList = getGroupList();
		if (groupsList != null)
			for (Iterator<?> it = groupsList.iterator(); it.hasNext();) {
				JRGroup jrGroup = (JRGroup) it.next();
				y += h;
				r = new Rectangle(0, y, 0, 0);
				h = 0;
				for (BaseColumn bc : table.getColumns()) {
					r = initHeader(r, bc, COLUMN_GROUP_HEADER, jrGroup.getName());
					r.setLocation(r.x, y);
					if (h < r.height)
						h = r.height;
				}
			}

		y += h;
		r = new Rectangle(0, y, 0, 0);
		h = 0;
		for (BaseColumn bc : allColumns) {
			r = initDetail(r, bc);
			r.setLocation(r.x, y);
			if (h < r.height)
				h = r.height;
		}

		if (groupsList != null)
			for (ListIterator<?> it = groupsList.listIterator(groupsList.size()); it.hasPrevious();) {
				JRGroup jrGroup = (JRGroup) it.previous();
				y += h;
				r = new Rectangle(0, y, 0, 0);
				h = 0;
				for (BaseColumn bc : table.getColumns()) {
					r = initFooter(r, bc, COLUMN_GROUP_FOOTER, jrGroup.getName());
					r.setLocation(r.x, y);
					if (h < r.height)
						h = r.height;
				}
			}

		y += h;
		r = new Rectangle(0, y, 0, 0);
		h = 0;
		for (BaseColumn bc : table.getColumns()) {
			r = initFooter(r, bc, COLUMN_FOOTER, null);
			r.setLocation(r.x, y);
			if (h < r.height)
				h = r.height;
		}
		y += h;
		r = new Rectangle(0, y, 0, 0);
		for (BaseColumn bc : table.getColumns()) {
			r = initFooter(r, bc, TABLE_FOOTER, null);
			r.setLocation(r.x, y);
		}
	}

	public static List<BaseColumn> getAllColumns(TableComponent table) {
		return getAllColumns(table.getColumns());
	}

	public static List<BaseColumn> getAllColumns(List<BaseColumn> cols) {
		List<BaseColumn> lst = new ArrayList<BaseColumn>();
		for (BaseColumn bc : cols) {
			if (bc instanceof ColumnGroup)
				lst.addAll(getAllColumns(((ColumnGroup) bc).getColumns()));
			else
				lst.add(bc);
		}
		return lst;
	}

	private Rectangle initDetail(Rectangle p, BaseColumn bc) {
		int h = 0;
		int w = 0;
		if (bc != null && bc instanceof Column) {
			Cell c = ((Column) bc).getDetailCell();
			w = bc.getWidth();
			if (c != null)
				h = c.getHeight();
			boundsMap.put(c, new Rectangle(p.x, p.y, w, h));
		}
		return new Rectangle(p.x + w, p.y, w, h);
	}

	private Rectangle initHeader(Rectangle p, BaseColumn bc, int type, String grName) {
		int y = p.y;
		int h = 0;
		int w = bc.getWidth();
		Cell c = getCell(bc, type, grName);
		if (c != null) {
			y = p.y + c.getHeight();
			h = c.getHeight();
			boundsMap.put(c, new Rectangle(p.x, p.y, w, h));
		}
		if (bc instanceof ColumnGroup) {
			Rectangle pi = new Rectangle(p.x, y, w, h);
			int hi = 0;
			for (BaseColumn bcg : ((ColumnGroup) bc).getColumns()) {
				pi = initHeader(pi, bcg, type, grName);
				pi.setLocation(pi.x, y);
				if (hi < pi.height)
					hi = pi.height;
			}
			h += hi;
		}
		return new Rectangle(p.x + w, y, w, h);
	}

	private Rectangle initFooter(Rectangle p, BaseColumn bc, int type, String grName) {
		int y = p.y;
		int h = 0;
		int w = bc.getWidth();
		Cell c = getCell(bc, type, grName);
		if (bc instanceof ColumnGroup) {
			Rectangle pi = new Rectangle(p.x, y, w, h);
			int hi = 0;
			for (BaseColumn bcg : ((ColumnGroup) bc).getColumns()) {
				pi = initFooter(pi, bcg, type, grName);
				pi.setLocation(pi.x, y);
				if (hi < pi.height)
					hi = pi.height;
			}
			h += hi;
		}
		if (c != null) {
			y = p.y + h;
			h = c.getHeight();
			boundsMap.put(c, new Rectangle(p.x, y, w, h));
		}
		return new Rectangle(p.x + w, y, w, h);
	}

	public Rectangle getBounds(int width, Cell cell, BaseColumn col) {
		Rectangle b = boundsMap.get(cell);
		if (b != null)
			return b;
		int w = col != null ? col.getWidth() : 0;
		int h = cell != null ? cell.getHeight() : 0;
		return new Rectangle(0, 0, w, h);
	}

	public List<?> getGroupList() {
		return getGroupList(table, jasperDesign);
	}

	public static List<?> getGroupList(TableComponent table, JasperDesign jd) {
		List<?> groupsList = null;
		JRDatasetRun datasetRun = table.getDatasetRun();
		if (datasetRun != null) {
			String dataSetName = datasetRun.getDatasetName();
			JRDataset ds = (JRDataset) jd.getDatasetMap().get(dataSetName);
			groupsList = (ds != null ? Arrays.asList(ds.getGroups()) : null);
		}
		return groupsList;
	}

	public static Cell getCell(BaseColumn bc, int type, String grName) {
		Cell cell = null;
		switch (type) {
		case TABLE_HEADER:
			cell = bc.getTableHeader();
			break;
		case TABLE_FOOTER:
			cell = bc.getTableFooter();
			break;
		case COLUMN_HEADER:
			cell = bc.getColumnHeader();
			break;
		case COLUMN_FOOTER:
			cell = bc.getColumnFooter();
			break;
		case COLUMN_DETAIL:
			if (bc instanceof Column)
				cell = ((Column) bc).getDetailCell();
			break;
		case COLUMN_GROUP_HEADER:
			cell = bc.getGroupHeader(grName);
			break;
		case COLUMN_GROUP_FOOTER:
			cell = bc.getGroupFooter(grName);
			break;
		}
		return cell;
	}

}
