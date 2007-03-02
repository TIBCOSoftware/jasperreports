package net.sf.jasperreports.engine.util;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import javax.net.ssl.*;

import javax.net.SocketFactory;
import javax.net.ssl.SSLSocketFactory;


/**
 * Based on the code of DummySSLSocketFactory by Eugen Kuleshov
 */
public class JRDummySSLSocketFactory extends SSLSocketFactory {
  private SSLSocketFactory factory;

  public JRDummySSLSocketFactory() {
    try {
      SSLContext sslcontext = null;
      
      try {
            sslcontext = SSLContext.getInstance( "TLS");
      } catch (java.security.NoSuchAlgorithmException ex)
      {
      	    boolean found_a = false;
      	    sslcontext = SSLContext.getInstance( "TLS");
      }
            
            sslcontext.init( null,
                       new TrustManager[] { new JRDummyTrustManager()},
                       new java.security.SecureRandom());
      factory = ( SSLSocketFactory) sslcontext.getSocketFactory();

    } catch( Exception ex) {
    }
    
  }

  public static SocketFactory getDefault() {
    return new JRDummySSLSocketFactory();
  }

  public Socket createSocket( Socket socket, String s, int i, boolean flag)
      throws IOException {
    return factory.createSocket( socket, s, i, flag);
  }

  public Socket createSocket( InetAddress inaddr, int i,
                              InetAddress inaddr1, int j) throws IOException {
    return factory.createSocket( inaddr, i, inaddr1, j);
  }

  public Socket createSocket( InetAddress inaddr, int i)
      throws IOException {
    return factory.createSocket( inaddr, i);
  }

  public Socket createSocket( String s, int i, InetAddress inaddr, int j)
      throws IOException {
    return factory.createSocket( s, i, inaddr, j);
  }

  public Socket createSocket( String s, int i) throws IOException {
    try {
    	if (factory == null)
    	{
    		
	      SSLContext sslcontext = null;
      
	      sslcontext = SSLContext.getInstance( "SSL");
              sslcontext.init( null,
                       new TrustManager[] { new JRDummyTrustManager()},
                       new java.security.SecureRandom());
      	      factory = ( SSLSocketFactory) sslcontext.getSocketFactory();
      	}
     } catch (Exception ex)
    {
    	ex.printStackTrace();
    }
       
    Socket sock = factory.createSocket( s, i);

    return sock;
  }

  public String[] getDefaultCipherSuites() {
    return factory.getSupportedCipherSuites();
  }

  public String[] getSupportedCipherSuites() {
    return factory.getSupportedCipherSuites();
  }
}


