/*****************************************************************************************
	1. program ID	: HttpCall_New.java
	2. create date	: 2025.09. 18
	3. auth		    : no name
	4. update date	:
	5. auth		    :
	6. description	: HttpCom
*****************************************************************************************/

package com.ecams.common.base;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.Logger;

import com.ecams.common.logger.EcamsLogger;

public class HttpCall_New {

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
			ecamsLogger.error("## HttpCall_New.httpCall_common() Exception START ##");
			ecamsLogger.error("## Error DESC : ", exception);
			ecamsLogger.error("## HttpCall_New.httpCall_common() Exception END ##");
			retMsg = "EREX1:처리 중 오류발생(httpCall_common)";
		} catch (Exception exception) {
			exception.getStackTrace();
			ecamsLogger.error("## HttpCall_New.httpCall_common() Exception START ##");
			ecamsLogger.error("## Error DESC : ", exception);
			ecamsLogger.error("## HttpCall_New.httpCall_common() Exception END ##");
			retMsg = "EREX2:처리 중 오류발생(httpCall_common)";
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
				retMsg = "ER:Http Connection Fail";
			} else if (("GET".equals(etcData.get("reqMethod")) || "DELETE".equals(etcData.get("reqMethod"))) &&
			   htpConn.getResponseCode() != htpConn.HTTP_OK) {
			   retMsg = "ER:[" + Integer.toString(htpConn.getResponseCode()) + "]" + htpConn.getResponseMessage();
			} else {
				if (etcData.get("sendData") != null && !"".equals(etcData.get("sendData"))) {
					retMsg = httpSend(htpConn,etcData.get("sendData").getBytes());
					if (htpConn.getResponseCode() != htpConn.HTTP_OK) {
						retMsg = "ER:[" + Integer.toString(htpConn.getResponseCode()) + "]" + htpConn.getResponseMessage();
					}
				}
			}
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
			ecamsLogger.error("## HttpCall_New.httpProcess() Exception START ##");
			ecamsLogger.error("## Error DESC : ", exception);
			ecamsLogger.error("## HttpCall_New.httpProcess() Exception END ##");
			retMsg = "EREX1:처리 중 오류발생(httpProcess)";
			byteData = retMsg.getBytes();
		} catch (Exception exception) {
			exception.getStackTrace();
			ecamsLogger.error("## HttpCall_New.httpProcess() Exception START ##");
			ecamsLogger.error("## Error DESC : ", exception);
			ecamsLogger.error("## HttpCall_New.httpProcess() Exception END ##");
			retMsg = "EREX2:처리 중 오류발생(httpProcess)";
			byteData = retMsg.getBytes();
		} finally {
			if(htpConn != null) {
				htpConn.disconnect();; //취약점 수정
				htpConn = null;
			}
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
			htpConn = (HttpURLConnection) url.openConnection(); 
			htpConn.setConnectTimeout(10000); //10초 연결제한
			htpConn.setReadTimeout(10000);    //10초 응답대기
		
		}
		catch (IOException exception)	{  //취약점 수정
			exception.getStackTrace();
			ecamsLogger.error("## HttpCall_New.httpOpen() Exception START ##");
			ecamsLogger.error("## Error DESC : ", exception);
			ecamsLogger.error("## HttpCall_New.httpOpen() Exception END ##");
			return null;
		}
		catch (Exception exception)	{
			exception.getStackTrace();
			ecamsLogger.error("## HttpCall_New.httpOpen() Exception START ##");
			ecamsLogger.error("## Error DESC : ", exception);
			ecamsLogger.error("## HttpCall_New.httpOpen() Exception END ##");
			return null;
		}
		htpConn.setRequestMethod(pMethod);
		htpConn.setRequestProperty("Accept", "application/json");
		//htpConn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
		htpConn.setRequestProperty("Content-Type", "application/json");
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
			ecamsLogger.error("## HttpCall_New.httpSend() Exception START ##");
			ecamsLogger.error("## Error DESC : ", exception);
			ecamsLogger.error("## HttpCall_New.httpSend() Exception END ##");
			return "EREX1:처리 중 오류발생(httpSend)";
		} catch (Exception exception)	{
			exception.getStackTrace();
			ecamsLogger.error("## HttpCall_New.httpSend() Exception START ##");
			ecamsLogger.error("## Error DESC : ", exception);
			ecamsLogger.error("## HttpCall_New.httpSend() Exception END ##");
			return "EREX2:처리 중 오류발생(httpSend)";
		}
	}
	public byte[] httpReceive(HttpURLConnection phtpConn,String savePath) throws Exception
	{
		String errMsg = "";
		String line = "";
		try {
			int code = phtpConn.getResponseCode();
			InputStream in = phtpConn.getInputStream();
			
			ByteArrayOutputStream baos = new ByteArrayOutputStream(1024);
			byte[] buf = new byte[8192];
			int n;
			try(InputStream is = new BufferedInputStream((in))) {
				
				while ((n = is.read(buf)) != -1){
					baos.write(buf,0,n);
				}
			}
			ecamsLogger.error("httpReceive 205 " + baos.toByteArray());
			return baos.toByteArray();
			
			
		} catch (IOException exception)	{  //취약점 수정
			exception.getStackTrace();
			ecamsLogger.error("## HttpCall_New.httpReceive() Exception START ##");
			ecamsLogger.error("## Error DESC : ", exception);
			ecamsLogger.error("## HttpCall_New.httpReceive() Exception END ##");
			errMsg = "EREX1:처리 중 오류발생(httpReceive)";
			return errMsg.getBytes();
		} catch (Exception exception)	{
			exception.getStackTrace();
			ecamsLogger.error("## HttpCall_New.httpReceive() Exception START ##");
			ecamsLogger.error("## Error DESC : ", exception);
			ecamsLogger.error("## HttpCall_New.httpReceive() Exception END ##");
			errMsg = "EREX2:처리 중 오류발생(httpReceive)";
			return errMsg.getBytes();
		}
	}
	public byte[] httpReceive_old(HttpURLConnection phtpConn,String savePath) throws Exception
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
			
			ByteArrayOutputStream baosRetStream = new ByteArrayOutputStream();
			while ((iLen = isResStream.read(byteTmpBuf)) != -1) {
				int newLen = new String(byteTmpBuf, "utf-8").length();
				baosRetStream.write(new String(byteTmpBuf,"utf-8").getBytes(),0,newLen);
			}
			baosRetStream.flush();
			baosRetStream.close();
			return baosRetStream.toByteArray();
			/*
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
			*/
			
		} catch (IOException exception)	{  //취약점 수정
			exception.getStackTrace();
			ecamsLogger.error("## HttpCall_New.httpReceive() Exception START ##");
			ecamsLogger.error("## Error DESC : ", exception);
			ecamsLogger.error("## HttpCall_New.httpReceive() Exception END ##");
			errMsg = "EREX1:처리 중 오류발생(httpReceive)";
			return errMsg.getBytes();
		} catch (Exception exception)	{
			exception.getStackTrace();
			ecamsLogger.error("## HttpCall_New.httpReceive() Exception START ##");
			ecamsLogger.error("## Error DESC : ", exception);
			ecamsLogger.error("## HttpCall_New.httpReceive() Exception END ##");
			errMsg = "EREX2:처리 중 오류발생(httpReceive)";
			return errMsg.getBytes();
		}
	}	
}