package net.sf.jasperreports.engine.util;

import javax.net.ssl.X509TrustManager;
import java.security.cert.X509Certificate;


/**
 * Based on the code of DummyTrustManager by Eugen Kuleshov
 */
public class JRDummyTrustManager implements X509TrustManager {

  public void checkClientTrusted(java.security.cert.X509Certificate[] cert,java.lang.String s) {
    return;
  }

  public void checkServerTrusted( X509Certificate[] cert, String s) {
    return;
  }
  

  public X509Certificate[] getAcceptedIssuers() {
    return new X509Certificate[ 0];
  }

}


