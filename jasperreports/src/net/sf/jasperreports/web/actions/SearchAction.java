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
package net.sf.jasperreports.web.actions;

import java.util.Map;

import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReportsContext;
import net.sf.jasperreports.engine.ReportContext;
import net.sf.jasperreports.search.LuceneUtil;
import net.sf.jasperreports.search.SpansInfo;
import net.sf.jasperreports.web.WebReportContext;
import net.sf.jasperreports.web.servlets.JasperPrintAccessor;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

/**
 * @author Narcis Marcu (narcism@users.sourceforge.net)
 */
public class SearchAction extends AbstractAction {

	private SearchData searchData;

	public SearchData getSearchData() {
		return searchData;
	}

	public void setSearchData(SearchData searchData) {
		this.searchData = searchData;
	}

	@Override
	public void performAction() throws ActionException {
		if (searchData != null && searchData.getSearchString() != null && searchData.getSearchString().length() > 0) {
			JasperReportsContext jasperReportsContext = getJasperReportsContext();
			ReportContext reportContext = getReportContext();

			JasperPrintAccessor jasperPrintAccessor = (JasperPrintAccessor) reportContext.getParameterValue(
					WebReportContext.REPORT_CONTEXT_PARAMETER_JASPER_PRINT_ACCESSOR);

			JasperPrint jasperPrint = jasperPrintAccessor.getFinalJasperPrint();
			LuceneUtil luceneUtil = new LuceneUtil(jasperReportsContext, searchData.isCaseSensitive(), searchData.isWholeWordsOnly(), searchData.isRemoveAccents());

			try {
				SpansInfo spansInfo = luceneUtil.getSpansInfo(jasperPrint, searchData.getSearchString());
				reportContext.setParameterValue("net.sf.jasperreports.search.term.highlighter", spansInfo);

				ObjectMapper mapper = new ObjectMapper();
				ObjectNode result = mapper.createObjectNode();

				Map<String, Integer> hitTermsPerPage = spansInfo.getHitTermsPerPage();
				result.put("searchString", searchData.getSearchString());

				if (hitTermsPerPage.size() > 0) {
					ArrayNode arrayNode = mapper.createArrayNode();
					ObjectNode item;
					result.put("searchResults", arrayNode);
					for (Map.Entry<String, Integer> entry: hitTermsPerPage.entrySet()) {
						item = mapper.createObjectNode();
						item.put("page", Integer.parseInt(entry.getKey()) + 1);
						item.put("hitCount", entry.getValue()/spansInfo.getTermsPerQuery());
						arrayNode.add(item);
					}
				}
				reportContext.setParameterValue("net.sf.jasperreports.web.actions.result.json", result);

			} catch (Exception e) {
				throw new ActionException(e);
			}
		}
	}

	@Override
	public boolean requiresRefill() {
		return false;
	}

}

