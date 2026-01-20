import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;



public class HttpsCom {
	
	public void disavleSslVerification() throws NoSuchAlgorithmException, KeyManagementException{
		TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManager() {
			
			@Override
			public X509Certificate[] getAcceptedIssuers() {
				// TODO Auto-generated method stub
				return null;
			}
			
			@Override
			public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
				// TODO Auto-generated method stub
				
			}
		}};
		
		SSLContext sc = SSLContext.getInstance("SSL");
		sc.init(null, trustAllCerts, new java.security.SecureRandom());
		HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
		
		HostnameVerifier allHostsValid = new HostnameVerifier() {
			public boolean verify(String ohstname, SSLSession session){
				return true;
			}
		};
		
		HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);
	}

	private static String sName = "[WasCommDriver] ";

	public byte[] process(String LinkUrl, byte[] pbyteData) throws Exception
	{
		HttpsURLConnection htpConn = null;
		byte[] byteData = null;

		try
		{
			htpConn = open(LinkUrl);
			send(htpConn, pbyteData);
			byteData = receive(htpConn);
		}
		catch (Exception e)
		{
			e.printStackTrace();
			throw new Exception(e.getMessage());
		}
		finally
		{
			close(htpConn);
		}

		return byteData;
	}

	private HttpsURLConnection open(String LinkUrl) throws Exception
	{
		String sSvrUrl = "https://" + LinkUrl;
		HttpsURLConnection htpConn = null;
		System.setProperty("sun.net.client.defaultConnectTimeout","86400000");
		System.setProperty("sun.net.client.defaultReadTimeout","86400000");

		//System.out.println(sSvrUrl);

		try	{
			disavleSslVerification();
			htpConn = (HttpsURLConnection) new URL(sSvrUrl).openConnection(); 
		}
		catch (Exception e)	{
        }
		htpConn.setRequestMethod("GET");
		htpConn.setDoOutput(true);
		htpConn.setDoInput(true);
		htpConn.setUseCaches(false);
		htpConn.setDefaultUseCaches(false);


		return htpConn;
	}

	private void close(HttpURLConnection phtpConn)
	{
		phtpConn.disconnect();
	}

	private void send(HttpURLConnection phtpConn, byte[] pbyteData) throws Exception
	{
		//System.out.println("# send start");
		OutputStream outStream = phtpConn.getOutputStream();
		outStream.write(pbyteData);
		outStream.flush();
		outStream.close();
		//System.out.println("# send end");
	}

	public byte[] receive(HttpURLConnection phtpConn) throws Exception
	{
		byte[] byteTmpBuf = new byte[8192];
		int iLen = 0;
		//System.out.println("# get start");
		InputStream isResStream = phtpConn.getInputStream();
    	ByteArrayOutputStream baosRetStream = new ByteArrayOutputStream();
    	//System.out.println("# get start2");
		while ((iLen = isResStream.read(byteTmpBuf)) != -1)
			baosRetStream.write(byteTmpBuf, 0, iLen);

		baosRetStream.flush();
		baosRetStream.close();

		//System.out.println("# get end");
		return baosRetStream.toByteArray();
	}

}
