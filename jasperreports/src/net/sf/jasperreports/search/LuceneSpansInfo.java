package net.sf.jasperreports.search;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Narcis Marcu (narcism@users.sourceforge.net)
 * @version $Id$
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
