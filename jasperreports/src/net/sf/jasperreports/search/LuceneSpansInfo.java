/*
 * JasperReports - Free Java Reporting Library.
 * Copyright (C) 2001 - 2014 TIBCO Software Inc. All rights reserved.
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
package net.sf.jasperreports.search;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Narcis Marcu (narcism@users.sourceforge.net)
 */
public class LuceneSpansInfo implements SpansInfo{

	private Map<String, List<HitTermInfo>> hitTermsInfo;
	private Map<String, Integer> hitTermsPerPage;
	private int termsPerQuery;

	public LuceneSpansInfo(int termsPerQuery) {
		this.termsPerQuery = termsPerQuery;
		this.hitTermsInfo = new LinkedHashMap<String, List<HitTermInfo>>();
		this.hitTermsPerPage = new LinkedHashMap<String, Integer>();
	}

	public void addTermInfo(String key, HitTermInfo hitTermInfo) {
		if (!hitTermsInfo.containsKey(key)) {
			hitTermsInfo.put(key, new ArrayList<HitTermInfo>());
		}

		if (!hitTermsPerPage.containsKey(hitTermInfo.getPageNo())) {
			hitTermsPerPage.put(hitTermInfo.getPageNo(), 0);
		}
		hitTermsInfo.get(key).add(hitTermInfo);
		hitTermsPerPage.put(hitTermInfo.getPageNo(), hitTermsPerPage.get(hitTermInfo.getPageNo()) + 1);
	}

	@Override
	public boolean hasHitTermsInfo(String key) {
		return hitTermsInfo.containsKey(key);
	}

	@Override
	public List<HitTermInfo> getHitTermsInfo(String key) {
		return hitTermsInfo.get(key);
	}

	@Override
	public Map<String, Integer> getHitTermsPerPage() {
		return hitTermsPerPage;
	}

	@Override
	public int getTermsPerQuery() {
		return termsPerQuery;
	}
}
