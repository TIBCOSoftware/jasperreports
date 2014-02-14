package net.sf.jasperreports.search;

import java.util.List;
import java.util.Map;

/**
 * @author Narcis Marcu (narcism@users.sourceforge.net)
 * @version $Id$
 */
public interface SpansInfo {

    boolean hasHitTermsInfo(String key);

    List<HitTermInfo> getHitTermsInfo(String key);

    Map<String, Integer> getHitTermsPerPage();

    int getTermsPerQuery();

}
