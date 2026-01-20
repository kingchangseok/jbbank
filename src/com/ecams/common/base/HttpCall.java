/*****************************************************************************************
	1. program ID	: HttpCall.java
	2. create date	: 2008.09. 21
	3. auth		    : no name
	4. update date	:
	5. auth		    :
	6. description	: HttpCom
*****************************************************************************************/

package com.ecams.common.base;

import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.Logger;

import com.ecams.common.logger.EcamsLogger;

public class HttpCall {

	Logger ecamsLogger  = EcamsLogger.getLoggerInstance();
	//private static String sName = "[WasCommDriver] ";

	public String httpCall_common(HashMap<String, String> etcData) {
		String retMsg = "";
		boolean errSw = false;

		byte[] receive;
		try {
			if (etcData.get("remoteURL") == null || "".equals(etcData.get("remoteURL"))) {
				retMsg = "ER:입력값없음 [접속URL]";
				errSw = true;
			}
			if (!errSw) {
				if (etcData.get("reqMethod") == null || "".equals(etcData.get("reqMethod"))) {
					retMsg = "ER:입력값없음 [Request Method]";
					errSw = true;
				}
			}
			if (!errSw) {
				receive = httpProcess(etcData);
				retMsg = new String(receive);
			}
		} catch (IOException exception) { //취약점 수정
			exception.getStackTrace();
			ecamsLogger.error("## HttpCall.httpCall_common() Exception START ##");
			ecamsLogger.error("## Error DESC : ", exception);
			ecamsLogger.error("## HttpCall.httpCall_common() Exception END ##");
			retMsg = "ER:처리 중 오류발생";
		} catch (Exception exception) {
			exception.getStackTrace();
			ecamsLogger.error("## HttpCall.httpCall_common() Exception START ##");
			ecamsLogger.error("## Error DESC : ", exception);
			ecamsLogger.error("## HttpCall.httpCall_common() Exception END ##");
			retMsg = "ER:처리 중 오류발생";
		}

		return retMsg;
	}
	public byte[] httpProcess(HashMap<String, String> etcData) throws Exception {
		HttpURLConnection htpConn = null;
		byte[] byteData = null;
		String retMsg = "OK";
		try {
			htpConn = httpOpen(etcData.get("remoteURL"),etcData.get("reqMethod"));
			if (htpConn == null) {
				retMsg = "Http Connection Fail";
			} else if (("GET".equals(etcData.get("reqMethod")) || "DELETE".equals(etcData.get("reqMethod"))) &&
			   htpConn.getResponseCode() != htpConn.HTTP_OK) {
			   retMsg = "[" + Integer.toString(htpConn.getResponseCode()) + "]" + htpConn.getResponseMessage();
			} else {
				if (etcData.get("sendData") != null && !"".equals(etcData.get("sendData"))) {
					retMsg = httpSend(htpConn,etcData.get("sendData").getBytes("UTF-8"));
					//retMsg = httpSend_new(htpConn,etcData.get("sendData"));
					if (htpConn.getResponseCode() != htpConn.HTTP_OK) {
						retMsg = "[" + Integer.toString(htpConn.getResponseCode()) + "]" + htpConn.getResponseMessage();
					}
				}
			}
			ecamsLogger.error("return=["+retMsg+"]");
			if ("OK".equals(retMsg)) {
				if (etcData.get("savePath") != null && !"".equals(etcData.get("savePath"))) {	//결과를 파일로 생성
					byteData = httpReceive(htpConn,etcData.get("savePath"));
				} else {
					byteData = httpReceive(htpConn,null);
				}
			} else {
				byteData = retMsg.getBytes();
			}
		} catch (IOException exception) { //취약점 수정
			exception.getStackTrace();
			ecamsLogger.error("## HttpCall.httpProcess() Exception START ##");
			ecamsLogger.error("## Error DESC : ", exception);
			ecamsLogger.error("## HttpCall.httpProcess() Exception END ##");
			retMsg = "ER:처리 중 오류발생";
			byteData = retMsg.getBytes();
		} catch (Exception exception) {
			exception.getStackTrace();
			ecamsLogger.error("## HttpCall.httpProcess() Exception START ##");
			ecamsLogger.error("## Error DESC : ", exception);
			ecamsLogger.error("## HttpCall.httpProcess() Exception END ##");
			retMsg = "ER:처리 중 오류발생";
			byteData = retMsg.getBytes();
		} finally {
			if(htpConn != null) close(htpConn); //취약점 수정
		}

		return byteData;
	}

	private HttpURLConnection httpOpen(String pURL, String pMethod) throws Exception
	{
		HttpURLConnection htpConn = null;

		System.setProperty("sun.net.client.defaultConnectTimeout","86400000");
		System.setProperty("sun.net.client.defaultReadTimeout","86400000");
		URL url = new URL(pURL);

		try	{
			htpConn = (HttpURLConnection) url.openConnection(); }
		catch (IOException exception)	{  //취약점 수정
			exception.getStackTrace();
			ecamsLogger.error("## HttpCall.httpOpen() Exception START ##");
			ecamsLogger.error("## Error DESC : ", exception);
			ecamsLogger.error("## HttpCall.httpOpen() Exception END ##");
			throw new Exception(exception.getMessage());
		}
		catch (Exception exception)	{
			exception.getStackTrace();
			ecamsLogger.error("## HttpCall.httpOpen() Exception START ##");
			ecamsLogger.error("## Error DESC : ", exception);
			ecamsLogger.error("## HttpCall.httpOpen() Exception END ##");
			throw new Exception(exception.getMessage());
		}
		htpConn.setRequestMethod(pMethod);
		htpConn.setRequestProperty("Accept", "application/json;charset=UTF-8");
		htpConn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
		htpConn.setDoOutput(true);
		htpConn.setDoInput(true);
		htpConn.setUseCaches(false);
		htpConn.setDefaultUseCaches(false);
		return htpConn;
	}
	private String httpSend(HttpURLConnection phtpConn, byte[] pbyteData) throws Exception
	{
		try {
			OutputStream outStream = phtpConn.getOutputStream();
			outStream.write(pbyteData);
			outStream.flush();
			outStream.close();

			return "OK";
		} catch (IOException exception)	{  //취약점 수정
			exception.getStackTrace();
			ecamsLogger.error("## HttpCall.httpSend() Exception START ##");
			ecamsLogger.error("## Error DESC : ", exception);
			ecamsLogger.error("## HttpCall.httpSend() Exception END ##");
			return "ER";
		} catch (Exception exception)	{
			exception.getStackTrace();
			ecamsLogger.error("## HttpCall.httpSend() Exception START ##");
			ecamsLogger.error("## Error DESC : ", exception);
			ecamsLogger.error("## HttpCall.httpSend() Exception END ##");
			return "ER";
		}
	}
	private String httpSend_new(HttpURLConnection phtpConn, String pbyteData) throws Exception
	{
		try {
			//OutputStream outStream = phtpConn.getOutputStream();
			BufferedWriter outStream = new BufferedWriter(new OutputStreamWriter(phtpConn.getOutputStream(),"UTF-8"));
			ecamsLogger.error("httpSend_new = pbyteData "+ pbyteData);
			outStream.write(pbyteData);
			outStream.flush();
			outStream.close();

			return "OK";
		} catch (IOException exception)	{  //취약점 수정
			exception.getStackTrace();
			ecamsLogger.error("## HttpCall.httpSend() Exception START ##");
			ecamsLogger.error("## Error DESC : ", exception);
			ecamsLogger.error("## HttpCall.httpSend() Exception END ##");
			return "ER";
		} catch (Exception exception)	{
			exception.getStackTrace();
			ecamsLogger.error("## HttpCall.httpSend() Exception START ##");
			ecamsLogger.error("## Error DESC : ", exception);
			ecamsLogger.error("## HttpCall.httpSend() Exception END ##");
			return "ER";
		}
	}
	public byte[] httpReceive(HttpURLConnection phtpConn,String savePath) throws Exception
	{
		String errMsg = "";
		try {
			byte[] byteTmpBuf = new byte[10240];
			int iLen = 0;

			InputStream isResStream = phtpConn.getInputStream();
			String disposition = phtpConn.getHeaderField("Content-Disposition");
			phtpConn.getContentType();
			int i = 0;
			String inFile = "RstUrl.log";
			String saveFilePath = "";
			//if (disposition != null && !"".equals(disposition)) {
				//i = disposition.indexOf("filename=");
				//if (i>0) {
					//inFile = disposition.substring(i+10,disposition.length() - 1);
					if (savePath != null && !"".equals(savePath)) {
						if (!"/".equals(savePath.substring(savePath.length()-1))) {
							savePath = savePath + "/";
						}
						saveFilePath = savePath + inFile;
					} else {
						saveFilePath = "./tmp"+inFile;
					}
					FileOutputStream outputStream = new FileOutputStream(saveFilePath);
					while ((iLen = isResStream.read(byteTmpBuf)) != -1) {
						outputStream.write(byteTmpBuf,0,iLen);
					}
					outputStream.close();
					isResStream.close();
				//}
			//}
			if (saveFilePath.length() == 0) {
				ByteArrayOutputStream baosRetStream = new ByteArrayOutputStream();
				while ((iLen = isResStream.read(byteTmpBuf)) != -1) {
					int newLen = new String(byteTmpBuf, "utf-8").length();
					baosRetStream.write(new String(byteTmpBuf,"utf-8").getBytes(),0,newLen);
				}
				baosRetStream.flush();
				baosRetStream.close();
				return baosRetStream.toByteArray();
			} else {
				return saveFilePath.getBytes();
			}
		} catch (IOException exception)	{  //취약점 수정
			exception.getStackTrace();
			ecamsLogger.error("## HttpCall.httpReceive() Exception START ##");
			ecamsLogger.error("## Error DESC : ", exception);
			ecamsLogger.error("## HttpCall.httpReceive() Exception END ##");
			return ("ER"+exception.getMessage()).getBytes();
		} catch (Exception exception)	{
			exception.getStackTrace();
			ecamsLogger.error("## HttpCall.httpReceive() Exception START ##");
			ecamsLogger.error("## Error DESC : ", exception);
			ecamsLogger.error("## HttpCall.httpReceive() Exception END ##");
			return ("ER"+exception.getMessage()).getBytes();
		}
	}
	public byte[] process(String psSvrURL, String psAct, byte[] pbyteData) throws Exception
	{
		HttpURLConnection htpConn = null;
		byte[] byteData = null;
		String errMsg = "";
		System.out.println("psSvrURL ----> " + psSvrURL);
		try
		{
			htpConn = open(psSvrURL, psAct);
			if (htpConn == null) {
				errMsg = "-1:http Connection Fail ["+psSvrURL+"/"+psAct+"]";
				byteData = errMsg.getBytes();
			} else {
				if (send(htpConn, pbyteData) == 0) {
					byteData = receive(htpConn);
					errMsg = new String(byteData);
				} else {
					errMsg = "-1:send fail ="+psSvrURL+"/"+psAct;
					byteData = errMsg.getBytes();
				}
			}
		}
		catch (IOException exception) //취약점 수정
		{
			exception.getStackTrace();
			ecamsLogger.error("## HttpCom.process() Exception START ##");
			ecamsLogger.error("## Error DESC : ", exception);
			ecamsLogger.error("## HttpCom.process() Exception END ##");
			throw new Exception(exception.getMessage());
		}
		catch (Exception exception)
		{
			exception.getStackTrace();
			ecamsLogger.error("## HttpCom.process() Exception START ##");
			ecamsLogger.error("## Error DESC : ", exception);
			ecamsLogger.error("## HttpCom.process() Exception END ##");
			throw new Exception(exception.getMessage());
		}
		finally
		{
			if (htpConn != null) close(htpConn);
		}

		return byteData;
	}

	private HttpURLConnection open(String psSvrURL, String psAct) throws Exception
	{
		String sSvrUrl = psSvrURL + "/" + psAct;
		HttpURLConnection htpConn = null;

		System.setProperty("sun.net.client.defaultConnectTimeout","86400000");
		System.setProperty("sun.net.client.defaultReadTimeout","86400000");


		try	{ htpConn = (HttpURLConnection) new URL(sSvrUrl).openConnection(); }
		catch (IOException exception)	{  //취약점 수정
			exception.getStackTrace();
			ecamsLogger.error("## HttpCom.process() Exception START ##");
			ecamsLogger.error("## Error DESC : ", exception);
			ecamsLogger.error("## HttpCom.process() Exception END ##");
			throw new Exception(exception.getMessage());
		}
		catch (Exception exception)	{
			exception.getStackTrace();
			ecamsLogger.error("## HttpCom.process() Exception START ##");
			ecamsLogger.error("## Error DESC : ", exception);
			ecamsLogger.error("## HttpCom.process() Exception END ##");
			throw new Exception(exception.getMessage());
		}
		htpConn.setRequestMethod("POST");
		htpConn.setDoOutput(true);
		htpConn.setDoInput(true);
		htpConn.setUseCaches(false);
		htpConn.setDefaultUseCaches(false);

		return htpConn;
	}

	private void close(HttpURLConnection phtpConn)
	{
		if(phtpConn != null) {	//취약점 수정
			phtpConn.disconnect();
		}
	}

	private int send(HttpURLConnection phtpConn, byte[] pbyteData) throws Exception
	{
		try {
			OutputStream outStream = phtpConn.getOutputStream();
			outStream.write(pbyteData);
			outStream.flush();
			outStream.close();

			return 0;
		} catch (IOException exception)	{  //취약점 수정
			exception.getStackTrace();
			ecamsLogger.error("## HttpCom.send() Exception START ##");
			ecamsLogger.error("## Error DESC : ", exception);
			ecamsLogger.error("## HttpCom.send() Exception END ##");
			return 1;
		} catch (Exception exception)	{
			exception.getStackTrace();
			ecamsLogger.error("## HttpCom.send() Exception START ##");
			ecamsLogger.error("## Error DESC : ", exception);
			ecamsLogger.error("## HttpCom.send() Exception END ##");
			return 1;
		}
	}

	public byte[] receive(HttpURLConnection phtpConn) throws Exception
	{
		String errMsg = "";
		try {
			byte[] byteTmpBuf = new byte[8192];
			int iLen = 0;
			InputStream isResStream = phtpConn.getInputStream();
	    	ByteArrayOutputStream baosRetStream = new ByteArrayOutputStream();
			while ((iLen = isResStream.read(byteTmpBuf)) != -1)
				baosRetStream.write(byteTmpBuf, 0, iLen);

			baosRetStream.flush();
			baosRetStream.close();

			return baosRetStream.toByteArray();
		} catch (IOException exception)	{  //취약점 수정
			exception.getStackTrace();
			ecamsLogger.error("## HttpCom.receive() Exception START ##");
			ecamsLogger.error("## Error DESC : ", exception);
			ecamsLogger.error("## HttpCom.receive() Exception END ##");
			errMsg = "-2:receive fail";
			return errMsg.getBytes();
		} catch (Exception exception)	{
			exception.getStackTrace();
			ecamsLogger.error("## HttpCom.receive() Exception START ##");
			ecamsLogger.error("## Error DESC : ", exception);
			ecamsLogger.error("## HttpCom.receive() Exception END ##");
			errMsg = "-2:receive fail";
			return errMsg.getBytes();
		}
	}
	public Map<String,Map<String,String>> strToMap (String recvStr) throws Exception
	{
		Map<String,Map<String,String>> resultMap = new HashMap<String,Map<String,String>>();
		Map<String,String> rst = null;

		String[] strList1 = recvStr.split("}");
		String[] strList2 = recvStr.split("}");
		String strWork = "";
		String strKey = "";
		int j = 0;
		try {
			for (int i=0;strList1.length>i;i++) {
				strWork = strList1[i].trim();
				ecamsLogger.error("++ strList1 1 ++"+strWork);
				if (strWork.length()<=6) continue;
				strWork = strWork.replaceAll("\"", "");
				strKey = strWork.substring(1,strWork.indexOf(":"));
				strWork = strWork.substring(strWork.indexOf(":")+2);
				ecamsLogger.error("++ strList1 2 ++"+strKey + ", "+strWork);
				strList2 = strWork.split(",");
				rst = new HashMap<String,String>();
				for (j=0;strList2.length>j;j++) {
					strWork = strList2[j].trim();
					ecamsLogger.error("++ strList2 3 ++"+strWork);
					rst.put(strWork.substring(0,strWork.indexOf(":")), strWork.substring(strWork.indexOf(":")+1));
				}
				resultMap.put(strKey, rst);
			}
			ecamsLogger.error("+++ resultMap  ++"+resultMap.toString());
		} catch (RuntimeException exception)	{  //취약점 수정
			exception.getStackTrace();
			ecamsLogger.error("## HttpCom.strToMap() Exception START ##");
			ecamsLogger.error("## Error DESC : ", exception);
			ecamsLogger.error("## HttpCom.strToMap() Exception END ##");
			throw new Exception(exception.getMessage());
			//throw new Exception(PfmMessage.get("COMM-E0003"));
		} catch (Exception exception)	{
			exception.getStackTrace();
			ecamsLogger.error("## HttpCom.strToMap() Exception START ##");
			ecamsLogger.error("## Error DESC : ", exception);
			ecamsLogger.error("## HttpCom.strToMap() Exception END ##");
			throw new Exception(exception.getMessage());
			//throw new Exception(PfmMessage.get("COMM-E0003"));
		}
		return resultMap;
	}
}