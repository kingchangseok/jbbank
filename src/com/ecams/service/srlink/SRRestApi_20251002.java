package com.ecams.service.srlink;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

import java.net.URLEncoder;
import org.apache.logging.log4j.Logger;

import com.ecams.common.base.HttpCall;
import com.ecams.common.dbconn.ConnectionContext;
import com.ecams.common.dbconn.ConnectionResource;
import com.ecams.common.logger.EcamsLogger;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import app.common.CodeInfo;
import app.common.LoggableStatement;
import app.eCmm.Cmm1000;
import app.eCmm.Cmm1600;
import html.app.common.ParsingCommon;

public class SRRestApi_20251002 {

    /**
     * Logger Class Instance Creation
     * logger
     */
    Logger ecamsLogger  = EcamsLogger.getLoggerInstance();
    public String InputConvStr(InputStream in) throws Exception{
    	String returnMsg = "";
    	
    	try {
    		BufferedReader bufferedReader = null;
    		bufferedReader = new BufferedReader(new InputStreamReader(in,"UTF-8"));
    		String line = "";
    		StringBuffer response = new StringBuffer();
    		while ((line = bufferedReader.readLine()) != null) {
    			response.append(line.trim());
    			line = "";
    		}
    		bufferedReader.close();
    		
    		returnMsg = response.toString();
    	} catch(Exception exception) {
			exception.printStackTrace();
			ecamsLogger.error("## SRRestApi.InputConvStr() Exception START ##");				
			ecamsLogger.error("## Error DESC : ", exception);	
			ecamsLogger.error("## Cmr0010.InputConvStr() Exception END ##");				
			returnMsg = "ERINX3:"+exception;
    	}
    	return returnMsg; 
    }
    public String setError(String errMsg) throws Exception{
    	String jsonStr = "";
    	String convStr = "";
    	
    	JsonObject jsonObj = new JsonObject();
    	Gson gson = new Gson();
    	try {
    		int nPos = -1;
    		nPos = errMsg.indexOf(":");
    		/*
    		jsonObj.addProperty("ResultCd", errMsg.substring(0,nPos));
    		convStr = URLEncoder.encode(errMsg.substring(nPos+1),"utf-8");
    		jsonObj.addProperty("ResultMsg", convStr);
    		*/
    		jsonObj.addProperty("ResultCd", errMsg.substring(0,nPos));
    		jsonObj.addProperty("ResultMsg", errMsg.substring(nPos+1));

    		
    	} catch(Exception exception) {
			exception.printStackTrace();
			ecamsLogger.error("## SRRestApi.setError() Exception START ##");				
			ecamsLogger.error("## Error DESC : ", exception);	
			ecamsLogger.error("## SRRestApi.setError() Exception END ##");		
    	}
    	jsonStr = gson.toJson(jsonObj);
    	gson = null;    	
    	
    	return jsonStr; 
    }
    public String InputCheck(String trCode,String inData) throws Exception{
    	JsonParser jsonparser = new JsonParser();
    	JsonObject jsonObj = new JsonObject();
    	JsonObject jsonObj2 = new JsonObject();
    	JsonObject jsonObj3 = new JsonObject();
    	String returnMsg = "";		
    	String itemData = "";
    	try {    		
    		if (trCode == null || "".equals(trCode)) returnMsg = "ERIN01:거래구분없음";
    		else if (inData == null || "".equals(inData)) returnMsg = "ERIN02:입력내용없음";
    		else {
    			jsonObj = (JsonObject) jsonparser.parse(inData);
    			if ("CC".equals(trCode.substring(0,2))) {
    				if (!jsonObj.has("SrDevReqNo")) returnMsg = "ERIN03:SR개발요청번호없음";
    			}
    		}
    		if (returnMsg.length()==0) { 
				if ("CC_REQ_REG".equals(trCode)) { //개발요청서 등록
					if (jsonObj.has("RequestDoc")) { //개발요청서
						jsonObj2 = jsonObj.getAsJsonObject("RequestDoc");
						if (returnMsg.length()==0) {
							if (!jsonObj2.has("DocType")) returnMsg = "ERIN05:Input형식불일치 ["+trCode+"-RequestDoc.DocType]";
						}
						if (returnMsg.length()==0) {
							if (!jsonObj2.has("DocNo")) returnMsg = "ERIN07:Input형식불일치 ["+trCode+"-RequestDoc.DocNo]";
						}
						if (returnMsg.length()==0) {
							if (!jsonObj2.has("DocSubject")) returnMsg = "ERIN08:Input형식불일치 ["+trCode+"-RequestDoc.DocSubject]";
						}
						if (returnMsg.length()==0) {
							if (!jsonObj2.has("ReqDocNo")) returnMsg = "ERIN09:Input형식불일치 ["+trCode+"-RequestDoc.ReqDocNo]";
						}
						if (returnMsg.length()==0) {
							if (!jsonObj2.has("BizManager")) returnMsg = "ERIN10:Input형식불일치 ["+trCode+"-RequestDoc.BizManager]";
						}
						if (returnMsg.length()==0) {
							if (!jsonObj2.has("JobCd")) returnMsg = "ERIN11:Input형식불일치 ["+trCode+"-RequestDoc.JobCd]";
						}
						if (returnMsg.length()==0) {
							if (!jsonObj2.has("Contents")) returnMsg = "ERIN12:Input형식불일치 ["+trCode+"-RequestDoc.Contents]";
						}
						if (returnMsg.length()==0) {
							if (!jsonObj2.has("StartDt")) returnMsg = "ERIN13:Input형식불일치 ["+trCode+"-RequestDoc.StartDt]";
						}
						if (returnMsg.length()==0) {
							if (!jsonObj2.has("EndDt")) returnMsg = "ERIN14:Input형식불일치 ["+trCode+"-RequestDoc.EndDt]";
						}
						if (returnMsg.length()==0) {
							if (!jsonObj2.has("EmpNo")) returnMsg = "ERIN15:Input형식불일치 ["+trCode+"-RequestDoc.EmpNo]";
						}
						if (returnMsg.length()==0) {
							if (jsonObj2.has("OrderDoc")) {
								jsonObj3 = jsonObj2.getAsJsonObject("OrderDoc");
								if (!jsonObj3.has("EndPlanDt")) returnMsg = "ERIN16:Input형식불일치 ["+trCode+"-RequestDoc.OrderDoc.EndPlanDt]";
								else {
									itemData = jsonObj3.get("EndPlanDt").toString().replace("\"", "");
			    					if (itemData.length()!=10) returnMsg = "ERIN17:업무지시서처리기한형식불일치["+itemData+"]";
								}
								if (returnMsg.length()==0) {
									if (!jsonObj3.has("Referencor")) returnMsg = "ERIN18:Input형식불일치 ["+trCode+"-RequestDoc.OrderDoc.Referencor]";									
								}
								if (returnMsg.length()==0) {
									if (!jsonObj3.has("Developer")) returnMsg = "ERIN19:Input형식불일치 ["+trCode+"-RequestDoc.OrderDoc.Developer]";	
								}
								if (returnMsg.length()==0) {
									if (!jsonObj3.has("EmpNo")) returnMsg = "ERIN20:Input형식불일치 ["+trCode+"-RequestDoc.OrderDoc.EmpNo]";	
								}
							}
						}
					}
					if (jsonObj.has("SmRequestDoc")) { //외주개발요청서
						jsonObj2 = jsonObj.getAsJsonObject("SmRequestDoc");
						if (returnMsg.length()==0) {
							if (!jsonObj2.has("DocType")) returnMsg = "ERIN31:Input형식불일치 ["+trCode+"-SmRequestDoc.DocType]";
						}
						if (returnMsg.length()==0) {
							if (!jsonObj2.has("DocNo")) returnMsg = "ERIN32:Input형식불일치 ["+trCode+"-SmRequestDoc.DocNo]";
						}
						if (returnMsg.length()==0) {
							if (!jsonObj2.has("JobCd")) returnMsg = "ERIN11:Input형식불일치 ["+trCode+"-SmRequestDoc.JobCd]";
						}
						if (returnMsg.length()==0) {
							if (!jsonObj2.has("BizManager")) returnMsg = "ERIN10:Input형식불일치 ["+trCode+"-SmRequestDoc.BizManager]";
						}
						if (returnMsg.length()==0) {
							if (!jsonObj2.has("DocSubject")) returnMsg = "ERIN12:Input형식불일치 ["+trCode+"-SmRequestDoc.DocSubject]";
						}
						if (returnMsg.length()==0) {
							if (!jsonObj2.has("Contents")) returnMsg = "ERIN12:Input형식불일치 ["+trCode+"-SmRequestDoc.Contents]";
						}
						if (returnMsg.length()==0) {
							if (!jsonObj2.has("StartDt")) returnMsg = "ERIN13:Input형식불일치 ["+trCode+"-SmRequestDoc.StartDt]";
						}
						if (returnMsg.length()==0) {
							if (!jsonObj2.has("EndDt")) returnMsg = "ERIN14:Input형식불일치 ["+trCode+"-SmRequestDoc.EndDt]";
						}
						if (returnMsg.length()==0) {
							if (!jsonObj2.has("EmpNo")) returnMsg = "ERIN15:Input형식불일치 ["+trCode+"-SmRequestDoc.EmpNo]";
						}
						if (returnMsg.length()==0) {
							if (jsonObj2.has("OrderDoc")) {
								jsonObj3 = jsonObj2.getAsJsonObject("OrderDoc");
								if (!jsonObj3.has("EndPlanDt")) returnMsg = "ERIN16:Input형식불일치 ["+trCode+"-SmRequestDoc.OrderDoc.EndPlanDt]";
								else {
									itemData = jsonObj3.get("EndPlanDt").toString().replace("\"", "");
			    					if (itemData.length()!=10) returnMsg = "ERIN17:업무지시서처리기한형식불일치["+itemData+"]";
								}
								if (returnMsg.length()==0) {
									if (!jsonObj3.has("Referencor")) returnMsg = "ERIN18:Input형식불일치 ["+trCode+"-SmRequestDoc.OrderDoc.Referencor]";									
								}
								if (returnMsg.length()==0) {
									if (!jsonObj3.has("Developer")) returnMsg = "ERIN19:Input형식불일치 ["+trCode+"-SmRequestDoc.OrderDoc.Developer]";	
								}
								if (returnMsg.length()==0) {
									if (!jsonObj3.has("EmpNo")) returnMsg = "ERIN20:Input형식불일치 ["+trCode+"-SmRequestDoc.OrderDoc.EmpNo]";	
								}
							}
						}
						
					}
					
				} else if ("CC_REQ_UPD".equals(trCode)) { //개발요청서 담당자변경
					if (jsonObj.has("RequestDoc")) { //개발요청서
						jsonObj2 = jsonObj.getAsJsonObject("RequestDoc");
						if (returnMsg.length()==0) {
							if (!jsonObj2.has("BizManager")) returnMsg = "ERIN10:Input형식불일치 ["+trCode+"-RequestDoc.BizManager]";
						}
						if (returnMsg.length()==0) {
							if (!jsonObj2.has("StartDt")) returnMsg = "ERIN13:Input형식불일치 ["+trCode+"-RequestDoc.StartDt]";
						}
						if (returnMsg.length()==0) {
							if (!jsonObj2.has("EndDt")) returnMsg = "ERIN14:Input형식불일치 ["+trCode+"-RequestDoc.EndDt]";
						}						
						if (returnMsg.length()==0) {
							if (!jsonObj2.has("EmpNo")) returnMsg = "ERIN15:Input형식불일치 ["+trCode+"-RequestDoc.EmpNo]";
						}
						if (returnMsg.length()==0) {
							if (!jsonObj2.has("Contents")) returnMsg = "ERIN12:Input형식불일치 ["+trCode+"-RequestDoc.Contents]";
						}
						if (returnMsg.length()==0) {
							if (jsonObj2.has("OrderDoc")) {
								jsonObj3 = jsonObj2.getAsJsonObject("OrderDoc");
								if (returnMsg.length()==0) {
									if (!jsonObj3.has("EndPlanDt")) returnMsg = "ERIN18:Input형식불일치 ["+trCode+"-RequestDoc.OrderDoc.EndPlanDt]";									
								}
								if (returnMsg.length()==0) {
									if (!jsonObj3.has("Referencor")) returnMsg = "ERIN18:Input형식불일치 ["+trCode+"-RequestDoc.OrderDoc.Referencor]";									
								}
								if (returnMsg.length()==0) {
									if (!jsonObj3.has("Developer")) returnMsg = "ERIN19:Input형식불일치 ["+trCode+"-RequestDoc.OrderDoc.Developer]";	
								}
								if (returnMsg.length()==0) {
									if (!jsonObj3.has("EmpNo")) returnMsg = "ERIN20:Input형식불일치 ["+trCode+"-RequestDoc.OrderDoc.EmpNo]";	
								}
							}
						}
					}
					if (jsonObj.has("SmRequestDoc")) { //외주개발요청서
						jsonObj2 = jsonObj.getAsJsonObject("SmRequestDoc");					
						if (returnMsg.length()==0) {
							if (!jsonObj2.has("BizManager")) returnMsg = "ERIN10:Input형식불일치 ["+trCode+"-SmRequestDoc.BizManager]";
						}
						if (returnMsg.length()==0) {
							if (!jsonObj2.has("StartDt")) returnMsg = "ERIN13:Input형식불일치 ["+trCode+"-SmRequestDoc.StartDt]";
						}
						if (returnMsg.length()==0) {
							if (!jsonObj2.has("EndDt")) returnMsg = "ERIN14:Input형식불일치 ["+trCode+"-SmRequestDoc.EndDt]";
						}
						if (returnMsg.length()==0) {
							if (!jsonObj2.has("EmpNo")) returnMsg = "ERIN15:Input형식불일치 ["+trCode+"-SmRequestDoc.EmpNo]";
						}
						if (returnMsg.length()==0) {
							if (!jsonObj2.has("Contents")) returnMsg = "ERIN12:Input형식불일치 ["+trCode+"-RequestDoc.Contents]";
						}
						if (returnMsg.length()==0) {
							if (jsonObj2.has("OrderDoc")) {
								jsonObj3 = jsonObj2.getAsJsonObject("OrderDoc");
								if (returnMsg.length()==0) {
									if (!jsonObj3.has("EndPlanDt")) returnMsg = "ERIN18:Input형식불일치 ["+trCode+"-SmRequestDoc.OrderDoc.EndPlanDt]";									
								}
								if (returnMsg.length()==0) {
									if (!jsonObj3.has("Referencor")) returnMsg = "ERIN18:Input형식불일치 ["+trCode+"-SmRequestDoc.OrderDoc.Referencor]";									
								}
								if (returnMsg.length()==0) {
									if (!jsonObj3.has("Developer")) returnMsg = "ERIN19:Input형식불일치 ["+trCode+"-SmRequestDoc.OrderDoc.Developer]";	
								}
								if (returnMsg.length()==0) {
									if (!jsonObj3.has("EmpNo")) returnMsg = "ERIN20:Input형식불일치 ["+trCode+"-SmRequestDoc.OrderDoc.EmpNo]";	
								}
							}
						}
						
					}
				} else if ("CC_REQ_PRD_UPD".equals(trCode)) { //개발요청서 일정변경
					if (jsonObj.has("RequestDoc")) { //개발요청서
						jsonObj2 = jsonObj.getAsJsonObject("RequestDoc");
						
						if (returnMsg.length()==0) {
							if (!jsonObj2.has("StartDt")) returnMsg = "ERIN13:Input형식불일치 ["+trCode+"-RequestDoc.StartDt]";
						}
						if (returnMsg.length()==0) {
							if (!jsonObj2.has("EndDt")) returnMsg = "ERIN14:Input형식불일치 ["+trCode+"-RequestDoc.EndDt]";
						}
						if (returnMsg.length()==0) {
							if (!jsonObj2.has("EmpNo")) returnMsg = "ERIN15:Input형식불일치 ["+trCode+"-RequestDoc.EmpNo]";
						}
					}
					if (jsonObj.has("SmRequestDoc")) { //외주개발요청서
						jsonObj2 = jsonObj.getAsJsonObject("SmRequestDoc");
						if (returnMsg.length()==0) {
							if (!jsonObj2.has("StartDt")) returnMsg = "ERIN13:Input형식불일치 ["+trCode+"-SmRequestDoc.StartDt]";
						}
						if (returnMsg.length()==0) {
							if (!jsonObj2.has("EndDt")) returnMsg = "ERIN14:Input형식불일치 ["+trCode+"-SmRequestDoc.EndDt]";
						}
						if (returnMsg.length()==0) {
							if (!jsonObj2.has("EmpNo")) returnMsg = "ERIN15:Input형식불일치 ["+trCode+"-SmRequestDoc.EmpNo]";
						}						
					}
				} else if ("CC_REQ_CLS".equals(trCode)) { //개발종료
					if (jsonObj.has("RequestDoc")) { //개발요청서
						jsonObj2 = jsonObj.getAsJsonObject("RequestDoc");
						if (returnMsg.length()==0) {
							if (!jsonObj2.has("EmpNo")) returnMsg = "ERIN15:Input형식불일치 ["+trCode+"-RequestDoc.EmpNo]";
						}
					}
					if (jsonObj.has("SmRequestDoc")) { //외주개발요청서
						jsonObj2 = jsonObj.getAsJsonObject("SmRequestDoc");
						if (returnMsg.length()==0) {
							if (!jsonObj2.has("EmpNo")) returnMsg = "ERIN15:Input형식불일치 ["+trCode+"-SmRequestDoc.EmpNo]";
						}						
					}
				}
    		} 
    		
		} catch (Exception exception) {
			exception.printStackTrace();
			ecamsLogger.error("## SRRestApi.InputCheck() Exception START ##");				
			ecamsLogger.error("## Error DESC : ", exception);	
			ecamsLogger.error("## SRRestApi.InputCheck() Exception END ##");				
			returnMsg = "ERINX4:"+exception;
		}finally{
		}
    	if (returnMsg.length()==0) return "OK";
    	else return returnMsg;
    }

    public ArrayList<HashMap<String,String>> dataConv(String trCode,String itemName,JsonObject jsonObj,Connection conn) throws Exception{

		PreparedStatement pstmt       = null;
		ResultSet         rs          = null;
		StringBuffer      strQuery    = new StringBuffer();
		
    	JsonObject jsonObj2 = new JsonObject();
    	JsonObject jsonObj3 = new JsonObject();
    	JsonArray jsonAry = new JsonArray();
    	String returnMsg = "";		
    	String itemData = "";
    	String ecamsREQID = "";
    	String strYear = "";
    	String strEditor = "";
    	
    	int i = 0;
    	HashMap<String,String> etcData = new HashMap<String,String>();
    	HashMap<String,String> addData = new HashMap<String,String>();
    	ArrayList<HashMap<String,String>> rstList = new ArrayList<HashMap<String,String>>();
    	
    	Cmm1000 cmm1000 = new Cmm1000();
    	rstList.clear();
    	try {    
			if ("CC_REQ_REG".equals(trCode)) { //개발요청서 등록
				if ("RequestDoc".equals(itemName) || "SmRequestDoc".equals(itemName)) { //개발요청서					
					itemData = jsonObj.get("DocType").toString().replace("\"", "");       //문서유형
					etcData.put("CC_DOCTYPE", itemData);
					itemData = jsonObj.get("DocNo").toString().replace("\"", "");         //문서번호
					etcData.put("CC_DOCNUM", itemData);
					itemData = jsonObj.get("DocSubject").toString().replace("\"", "");    //문서제목
					etcData.put("CC_DOCSUBJ", itemData);
					etcData.put("CC_DETAILJOBN", itemData);  //업무상세명 (없음)
					itemData = "";
					if (jsonObj.has("ReqDeptCd")) { //요청부점
						itemData = jsonObj.get("ReqDeptCd").toString().replace("\"", "");
						if ("02".equals(etcData.get("CC_DOCTYPE")) || "99".equals(etcData.get("CC_DOCTYPE"))) {  //문서유형이  02:대외 99:기타   
							etcData.put("CC_DEPT1", "");
							etcData.put("CC_DEPT2", itemData);
						}else if ("99".equals(etcData.get("CC_DOCTYPE"))) {  //문서유형이  02:대외 99:기타   
							etcData.put("CC_DEPT1", "");
							etcData.put("CC_DEPT2", itemData);
						} 
						else {   
							etcData.put("CC_DEPT1", itemData);
							etcData.put("CC_DEPT2", "");
						}
					} else {
						etcData.put("CC_DEPT1", "");
						etcData.put("CC_DEPT2", "");
					}
					if (jsonObj.has("ReqEmpNo")) {  //요청자
						itemData = jsonObj.get("ReqEmpNo").toString().replace("\"", "");
						if ("02".equals(etcData.get("CC_DOCTYPE")) || "99".equals(etcData.get("CC_DOCTYPE"))) {  //문서유형이 대외  
							etcData.put("CC_REQUSER1", "");
							etcData.put("CC_REQUSER2", itemData);
						} else {
							itemData = jsonObj.get("ReqEmpNo").toString().replace("\"", "");    
							etcData.put("CC_REQUSER1", itemData);
							etcData.put("CC_REQUSER2", "");
						}
					} else {
						etcData.put("CC_REQUSER1", "");
						etcData.put("CC_REQUSER2", "");
					}
					if (jsonObj.has("ReqDocNo")) {
						itemData = jsonObj.get("ReqDocNo").toString().replace("\"", "");      
						etcData.put("CC_DOCNUM2", itemData);  //주관부서문서번호					
					} else etcData.put("CC_DOCNUM2", "");
					
					
					if (jsonObj.has("RequestType")) {  //요청유형
						itemData = jsonObj.get("RequestType").toString().replace("\"", "");   
						etcData.put("CC_REQTYPE", itemData);  //요청유형
					} else {
						etcData.put("CC_REQTYPE","");
					}

					if (jsonObj.has("EndDt")) {  //처리기한(개발종료일자로 대체함)
						itemData = jsonObj.get("EndDt").toString().replace("\"", "");   
						etcData.put("CC_ENDPLAN", itemData); 
					} else {
						etcData.put("CC_ENDPLAN","");
					}

					if (jsonObj.has("JobCd")) {  //업무
						itemData = jsonObj.get("JobCd").toString().replace("\"", "");   
						etcData.put("CC_JOBCD", itemData); 
					} else {
						etcData.put("CC_JOBCD","");
					}

					if (jsonObj.has("ManageType")) {  //조치유형
						itemData = jsonObj.get("ManageType").toString().replace("\"", "");   
						etcData.put("CC_ACTTYPE", itemData); 
					} else {
						etcData.put("CC_ACTTYPE","");
					}

					if (jsonObj.has("DifficultyLevel")) {  //업무난이도
						itemData = jsonObj.get("DifficultyLevel").toString().replace("\"", "");   
						etcData.put("CC_JOBDIF", itemData); 
					} else {
						etcData.put("CC_JOBDIF","");
					}
					
					etcData.put("CC_CHKTEAM","");  //없음 (형상관리)
					etcData.put("CC_CHKDATA","");  //없음 (형상관리)
					
					if (jsonObj.has("StartDt")) {  //개발기간(시작일)
						itemData = jsonObj.get("StartDt").toString().replace("\"", "");   
						etcData.put("CC_DEVSTDT", itemData); 
					} else {
						etcData.put("CC_DEVSTDT","");
					}

					if (jsonObj.has("EndDt")) {  //개발기간(종료일)
						itemData = jsonObj.get("EndDt").toString().replace("\"", "");   
						etcData.put("CC_DEVEDDT", itemData); 
					} else {
						etcData.put("CC_DEVEDDT","");
					}

					if (jsonObj.has("Contents")) {  //추가업무내용
						itemData = jsonObj.get("Contents").toString().replace("\"", ""); 
						//itemData = jsonObj.get("Contents").toString().replace("\"", ""); 
						ecamsLogger.error("SRRestApi dataconv= Contents "+ itemData);
						ecamsLogger.error("SRRestApi dataconv= Contents1 "+ itemData.replace("\\r", "").replace("\\n", ""));
						ecamsLogger.error("SRRestApi dataconv= Contents2 "+ itemData.replace("\\r\\n", "'chr(13)'"));
						
						//itemData = jsonObj.get("Contents").toString().replace("\\r\\n", "chr(13)");
						//itemData = jsonObj.get("Contents").toString().replace("\\n", "chr(13)"); 
						itemData = itemData.replace("\\r\\n", "\n");
						itemData = itemData.replace("\\n", "\n"); 
						itemData = itemData.replace("\\r", "\n"); 
						
						etcData.put("CC_DETAILSAYU", itemData);
						//etcData.put("CC_DETAILSAYU", itemData);
					} else {
						etcData.put("CC_DETAILSAYU","");
					}

					if (jsonObj.has("EmpNo")) {  //담당자
						itemData = jsonObj.get("EmpNo").toString().replace("\"", "");   
						etcData.put("CC_EDITOR", itemData); 

						strQuery.setLength(0);
						strQuery.append("select cm_project from cmm0040   \n");
						strQuery.append(" where cm_userid=?               \n");
						pstmt = conn.prepareStatement(strQuery.toString());
						//pstmt = new LoggableStatement(conn,strQuery.toString());
						pstmt.setString(1, itemData);
						//ecamsLogger.error(((LoggableStatement)pstmt).getQueryString());
						rs = pstmt.executeQuery();
						if (rs.next()) {
							itemData = rs.getString("cm_project");
						} else itemData = "";
						rs.close();
						pstmt.close();
						etcData.put("CC_ETTEAM",itemData); //담당팀
						if ("02".equals(etcData.get("CC_DOCTYPE")) || "99".equals(etcData.get("CC_DOCTYPE"))) {  //문서유형이 대외인 경우 형상관리 부서코드가 없었으나 empno의부서코드로 등록하기로함
							etcData.put("CC_DEPT1",itemData); 
						}
						
					} else {
						etcData.put("CC_EDITOR","");
						etcData.put("CC_ETTEAM","");
					}
					
					if (etcData.get("CC_DEVSTDT") != null && !"".equals(etcData.get("CC_DEVSTDT")) && 
						etcData.get("CC_DEVEDDT") != null && !"".equals(etcData.get("CC_DEVEDDT"))) {
						cmm1000 = new Cmm1000();
						int dateGap = cmm1000.getHoliDayAll_conn(etcData.get("CC_DEVSTDT"),etcData.get("CC_DEVEDDT"),conn);
						cmm1000 = null;
						etcData.put("CC_DEVPERIOD",Integer.toString(dateGap));
					}

					strQuery.setLength(0);
					strQuery.append("select to_char(sysdate,'yyyy') curryear from dual \n");
					pstmt = conn.prepareStatement(strQuery.toString());
					rs = pstmt.executeQuery();
					if (rs.next()) {
						strYear = rs.getString("curryear");
					}
					rs.close();
					pstmt.close();
					
					/*  job코드 값으로 나와서 변경함
					if ("RequestDoc".equals(itemName)) itemData = jsonObj.get("ReqDeptCd").toString().replace("\"", "");
					else {
						itemData = jsonObj.get("JobCd").toString().replace("\"", ""); 

						strQuery.setLength(0);
						strQuery.append("select cm_jobname from cmm0102 \n");
						strQuery.append(" where cm_jobcd=?              \n");
						pstmt = conn.prepareStatement(strQuery.toString());
						pstmt.setString(1, itemData);
						rs = pstmt.executeQuery();
						if (rs.next()) {
							itemData = rs.getString("cm_jobname");
						} else itemData = "";
						rs.close();
						pstmt.close();
					}
					*/
					
					if ("RequestDoc".equals(itemName)){
						itemData = jsonObj.get("ReqDeptCd").toString().replace("\"", "");
						if ("02".equals(etcData.get("CC_DOCTYPE")) || "99".equals(etcData.get("CC_DOCTYPE"))) {  //문서유형이 대외   
							strQuery.setLength(0);
							strQuery.append("select cm_codename from cmm0020    \n");
							strQuery.append(" where cm_macode='FOREIGN'         \n");
							pstmt = conn.prepareStatement(strQuery.toString());
							rs = pstmt.executeQuery();
							if (rs.next()) {
								itemData = rs.getString("cm_codename");
							} else itemData = "";
							rs.close();
							pstmt.close();
						}else if ("01".equals(etcData.get("CC_DOCTYPE"))) {  //문서유형이 대외   
							strQuery.setLength(0);
							strQuery.append("select cm_deptname from cmm0100 \n");
							strQuery.append(" where cm_deptcd=?              \n");
							pstmt = conn.prepareStatement(strQuery.toString());
							pstmt.setString(1, itemData);
							rs = pstmt.executeQuery();
							if (rs.next()) {
								itemData = rs.getString("cm_deptname");
							} else itemData = "";
							rs.close();
							pstmt.close();
						}
					}else{
						itemData = jsonObj.get("JobCd").toString().replace("\"", ""); 
						strQuery.setLength(0);
						strQuery.append("select cm_jobname from cmm0102 \n");
						strQuery.append(" where cm_jobcd=?              \n");
						pstmt = conn.prepareStatement(strQuery.toString());
						pstmt.setString(1, itemData);
						rs = pstmt.executeQuery();
						if (rs.next()) {
							itemData = rs.getString("cm_jobname");
						} else itemData = "";
						rs.close();
						pstmt.close();
					}
					ecamsREQID = "요청-"+itemData+"-"+strYear;  					
					etcData.put("CC_REQID", ecamsREQID);	
					
					rstList.add(etcData);
					etcData = null;
				} else if ("RecvDept".equals(itemName)) {
					if (jsonObj.has("EmpNo")) {  //담당자
						strEditor = jsonObj.get("EmpNo").toString().replace("\"", "");   
					}
					jsonAry = jsonObj.getAsJsonArray("RecvDept");
					if (jsonAry == null) returnMsg = "ERCV01:"+trCode+":"+itemName;
					
					if (returnMsg.length()==0) {
						for (i=0;jsonAry.size()>i;i++) {
							etcData = new HashMap<String,String>();
							jsonObj2 = jsonAry.get(i).getAsJsonObject();
							if (jsonObj2.has("RecvDeptCd")) {
								itemData = jsonObj2.get("RecvDeptCd").toString().replace("\"", "");
								etcData.put("cm_project", itemData);								
								etcData.put("cm_userid", strEditor);			
								rstList.add(etcData);
								etcData = null;
							}
						}
					}
				} else if ("BizManager".equals(itemName)) {
					jsonAry = jsonObj.getAsJsonArray("BizManager");
					if (jsonAry == null) returnMsg = "ERCV02:"+trCode+":"+itemName;
					if (returnMsg.length()==0) {
						for (i=0;jsonAry.size()>i;i++) {
							etcData = new HashMap<String,String>();
							etcData.put("gbn", "biz");
							jsonObj2 = jsonAry.get(i).getAsJsonObject();
							if (jsonObj2.has("EmpNo")) {
								itemData = jsonObj2.get("EmpNo").toString().replace("\"", "");
								etcData.put("cm_userid", itemData);								

								strQuery.setLength(0);
								strQuery.append("select cm_project from cmm0040   \n");
								strQuery.append(" where cm_userid=?               \n");
								pstmt = conn.prepareStatement(strQuery.toString());
								//pstmt = new LoggableStatement(conn,strQuery.toString());
								pstmt.setString(1, itemData);
								//ecamsLogger.error(((LoggableStatement)pstmt).getQueryString());
								rs = pstmt.executeQuery();
								if (rs.next()) {
									itemData = rs.getString("cm_project");
								} else itemData = "";
								rs.close();
								pstmt.close();
								
								etcData.put("cm_project", itemData);	
								
								if (jsonObj2.has("Status")) {
									itemData = jsonObj2.get("Status").toString().replace("\"", "");
									etcData.put("Status", itemData);	
								}		
								rstList.add(etcData);
								etcData = null;
							}
						}
					}
				} else if ("OrderDoc".equals(itemName)) {
					jsonObj2 = jsonObj.getAsJsonObject("OrderDoc");
					if (jsonObj2 == null) returnMsg = "ERCV03:"+trCode+":"+itemName;

					if (returnMsg.length()==0) {
						etcData.put("gbn", "main");
						
						if (jsonObj2.has("JobTeamCd")) {  //업무지시서발행코드
							itemData = jsonObj2.get("JobTeamCd").toString().replace("\"", "");

							strQuery.setLength(0);
							strQuery.append("select cm_codename from cmm0020 \n");
							strQuery.append(" where cm_macode='JOBTEAM'      \n");
							strQuery.append("   and cm_micode=?             \n");						
							pstmt = conn.prepareStatement(strQuery.toString());
							pstmt.setString(1, itemData);
							rs = pstmt.executeQuery();
							if (rs.next()) {
								itemData = rs.getString("cm_codename");
							} else itemData = "";
							rs.close();
							pstmt.close();
							
							etcData.put("CC_ORDERID", itemData);
						} else etcData.put("CC_ORDERID", "");

						strQuery.setLength(0);
						strQuery.append("select to_char(sysdate,'yyyy') curryear from dual \n");
						pstmt = conn.prepareStatement(strQuery.toString());
						rs = pstmt.executeQuery();
						if (rs.next()) {
							strYear = rs.getString("curryear");
						}
						rs.close();
						pstmt.close();

						etcData.put("CC_ORDERID", "지시-"+itemData+"-"+strYear);
							
					}
					if (returnMsg.length()==0) {
						if (jsonObj2.has("DocNo")) {  //발행근거
							itemData = jsonObj2.get("DocNo").toString().replace("\"", "");
							etcData.put("CC_DOCNUM", itemData);
						} else etcData.put("CC_DOCNUM", "");
					}
					if (returnMsg.length()==0) {
						if (jsonObj2.has("EndPlanDt")) {  //처리기한
							itemData = jsonObj2.get("EndPlanDt").toString().replace("\"", "");
							etcData.put("CC_ENDPLAN", itemData);
						} else etcData.put("CC_ENDPLAN", "");
					}
					if (returnMsg.length()==0) {
						if (jsonObj2.has("EmpNo")) {  //작성자
							itemData = jsonObj2.get("EmpNo").toString().replace("\"", "");
							etcData.put("CC_ORDERUSER", itemData);
						} else etcData.put("CC_ORDERUSER", "");
					}
					if (returnMsg.length()==0) {
						rstList.add(etcData);
						if (jsonObj2.has("Referencor")) {
							jsonAry = jsonObj2.getAsJsonArray("Referencor");
							if (jsonAry != null) {
								for (i=0;jsonAry.size()>i;i++) {
									etcData = new HashMap<String,String>();
									etcData.put("gbn", "third");
									jsonObj3 = jsonAry.get(i).getAsJsonObject();
									if (jsonObj3.has("EmpNo")) {
										itemData = jsonObj3.get("EmpNo").toString().replace("\"", "");
										etcData.put("cm_userid", itemData);							
										

										strQuery.setLength(0);
										strQuery.append("select cm_project from cmm0040   \n");
										strQuery.append(" where cm_userid=?               \n");
										pstmt = conn.prepareStatement(strQuery.toString());
										//pstmt = new LoggableStatement(conn,strQuery.toString());
										pstmt.setString(1, itemData);
										//ecamsLogger.error(((LoggableStatement)pstmt).getQueryString());
										rs = pstmt.executeQuery();
										if (rs.next()) {
											itemData = rs.getString("cm_project");
										} else itemData = "";
										rs.close();
										pstmt.close();
										etcData.put("cm_project", itemData);
										
										if (jsonObj3.has("Status")) {
											itemData = jsonObj3.get("Status").toString().replace("\"", "");
											etcData.put("Status", itemData);	
										}			
										rstList.add(etcData);
										etcData = null;
									}
								}
							}
						}
						if (jsonObj2.has("Developer")) {
							jsonAry = jsonObj2.getAsJsonArray("Developer");
							if (jsonAry != null) {
								for (i=0;jsonAry.size()>i;i++) {
									etcData = new HashMap<String,String>();
									etcData.put("gbn", "dev");
									jsonObj3 = jsonAry.get(i).getAsJsonObject();
									if (jsonObj3.has("EmpNo")) {
										itemData = jsonObj3.get("EmpNo").toString().replace("\"", "");
										etcData.put("cm_userid", itemData);							
										
										strQuery.setLength(0);
										strQuery.append("select cm_project,cm_username from cmm0040   \n");
										strQuery.append(" where cm_userid=?               \n");
										pstmt = conn.prepareStatement(strQuery.toString());
										//pstmt = new LoggableStatement(conn,strQuery.toString());
										pstmt.setString(1, itemData);
										//ecamsLogger.error(((LoggableStatement)pstmt).getQueryString());
										rs = pstmt.executeQuery();
										if (rs.next()) {
											etcData.put("cm_project", rs.getString("cm_project"));
											etcData.put("cm_username", rs.getString("cm_username"));
										} else itemData = "";
										rs.close();
										pstmt.close();
										
										if (jsonObj3.has("SeqNo")) {
											itemData = jsonObj3.get("SeqNo").toString().replace("\"", "");
											etcData.put("ordersubid", itemData);
										}
										if (jsonObj3.has("Status")) {
											itemData = jsonObj3.get("Status").toString().replace("\"", "");
											etcData.put("Status", itemData);	
										}
										rstList.add(etcData);
										etcData = null;
									}
								}
							}
						}
					}
					
				}
				
			} else if ("CC_REQ_UPD".equals(trCode)) { //개발요청서 담당자변경
				if ("RequestDoc".equals(itemName) || "SmRequestDoc".equals(itemName)) { //개발요청서
					if ("RequestDoc".equals(itemName)) itemData = jsonObj.get("RequestDocNo").toString().replace("\"", "");       //개발요청서번호
					else itemData = jsonObj.get("SmRequestDocNo").toString().replace("\"", "");     //외주개발요청서번호
					addData.put("CC_REQID", itemData);

					if (jsonObj.has("EmpNo")) {  //담당자
						itemData = jsonObj.get("EmpNo").toString().replace("\"", "");   
						addData.put("CC_EDITOR", itemData); 
						
						strQuery.setLength(0);
						strQuery.append("select cm_project from cmm0040   \n");
						strQuery.append(" where cm_userid=?               \n");
						pstmt = conn.prepareStatement(strQuery.toString());
						//pstmt = new LoggableStatement(conn,strQuery.toString());
						pstmt.setString(1, itemData);
						//ecamsLogger.error(((LoggableStatement)pstmt).getQueryString());
						rs = pstmt.executeQuery();
						if (rs.next()) {
							itemData = rs.getString("cm_project");
						} else itemData = "";
						rs.close();
						pstmt.close();
						
						addData.put("CC_ETTEAM",itemData); //담당팀
						
					} else {
						addData.put("CC_EDITOR","");
						addData.put("CC_ETTEAM","");
					}
					if (jsonObj.has("Contents")) {  //추가업무내용
						itemData = jsonObj.get("Contents").toString().replace("\"", ""); 
						//ecamsLogger.error("SRRestApi dataconv= Contents "+ itemData);
						//ecamsLogger.error("SRRestApi dataconv= Contents1 "+ itemData.replace("\\r", "").replace("\\n", ""));
						//ecamsLogger.error("SRRestApi dataconv= Contents2 "+ itemData.replace("\\r\\n", "'chr(13)'"));
						
						itemData = itemData.replace("\\r\\n", "\n");
						itemData = itemData.replace("\\n", "\n"); 
						itemData = itemData.replace("\\r", "\n"); 
						
						addData.put("CC_DETAILSAYU", itemData);
						//etcData.put("CC_DETAILSAYU", itemData);
					} else {
						addData.put("CC_DETAILSAYU","");
					}
					if (jsonObj.has("StartDt")) {  //개발기간(시작일)
						itemData = jsonObj.get("StartDt").toString().replace("\"", "");   
						addData.put("CC_DEVSTDT", itemData); 
					} else {
						addData.put("CC_DEVSTDT","");
					}

					if (jsonObj.has("EndDt")) {  //개발기간(종료일)
						itemData = jsonObj.get("EndDt").toString().replace("\"", "");   
						addData.put("CC_DEVEDDT", itemData); 
					} else {
						addData.put("CC_DEVEDDT","");
					}
					if (jsonObj.has("OrderDoc")) {
						jsonObj2 = jsonObj.getAsJsonObject("OrderDoc");

						if (jsonObj2 == null) returnMsg = "ERCV03:"+trCode+":"+itemName+":OrderDoc";

						if (returnMsg.length()==0) {
							if (jsonObj2.has("OrderDocNo")) {  //업무지시서발행코드
								itemData = jsonObj2.get("OrderDocNo").toString().replace("\"", "");
								addData.put("CC_ORDERID", itemData);
							} else addData.put("CC_ORDERID", "");
								
						}
						if (returnMsg.length()==0) {
							if (jsonObj2.has("EmpNo")) {  //처리기한
								itemData = jsonObj2.get("EmpNo").toString().replace("\"", "");
								addData.put("CC_ORDERUSER", itemData);
							} else addData.put("CC_ORDERUSER", "");							
						}
						if (returnMsg.length()==0) {
							if (jsonObj2.has("EndPlanDt")) {  //처리기한
								itemData = jsonObj2.get("EndPlanDt").toString().replace("\"", "");
								addData.put("CC_ENDPLAN", itemData);
							} else addData.put("CC_ENDPLAN", "");
						}						
						if (returnMsg.length()==0) {
							if (jsonObj2.has("Referencor")) {
								jsonAry = jsonObj2.getAsJsonArray("Referencor");
								if (jsonAry != null) {
									for (i=0;jsonAry.size()>i;i++) {
										etcData = new HashMap<String,String>();
										etcData.put("gbn", "third");
										etcData.put("CC_EDITOR", addData.get("CC_EDITOR"));
										etcData.put("CC_REQID", addData.get("CC_REQID"));
										etcData.put("CC_ORDERID", addData.get("CC_ORDERID"));
										etcData.put("CC_ORDERUSER", addData.get("CC_ORDERUSER"));
										etcData.put("CC_DETAILSAYU", addData.get("CC_DETAILSAYU"));
										etcData.put("CC_DEVSTDT", addData.get("CC_DEVSTDT"));
										etcData.put("CC_DEVEDDT", addData.get("CC_DEVEDDT"));
										etcData.put("CC_ENDPLAN", addData.get("CC_ENDPLAN"));
										
										jsonObj3 = jsonAry.get(i).getAsJsonObject();
										if (jsonObj3.has("EmpNo")) {
											itemData = jsonObj3.get("EmpNo").toString().replace("\"", "");
											etcData.put("cm_userid", itemData);							

											strQuery.setLength(0);
											strQuery.append("select cm_project from cmm0040   \n");
											strQuery.append(" where cm_userid=?               \n");
											pstmt = conn.prepareStatement(strQuery.toString());
											//pstmt = new LoggableStatement(conn,strQuery.toString());
											pstmt.setString(1, itemData);
											//ecamsLogger.error(((LoggableStatement)pstmt).getQueryString());
											rs = pstmt.executeQuery();
											if (rs.next()) {
												itemData = rs.getString("cm_project");
											} else itemData = "";
											rs.close();
											pstmt.close();
											etcData.put("cm_project", itemData);

											if (jsonObj3.has("EmpStatus")) {
												itemData = jsonObj3.get("EmpStatus").toString().replace("\"", "");
												etcData.put("Status", itemData);	
											}			
											rstList.add(etcData);
											ecamsLogger.error("Referencor rstList="+rstList.toString());
											etcData = null;
										}
									}
								}
							}
							if (jsonObj2.has("Developer")) {
								jsonAry = jsonObj2.getAsJsonArray("Developer");
								if (jsonAry != null) {
									for (i=0;jsonAry.size()>i;i++) {
										etcData = new HashMap<String,String>();
										etcData.put("CC_EDITOR", addData.get("CC_EDITOR"));
										etcData.put("CC_REQID", addData.get("CC_REQID"));
										etcData.put("CC_ORDERID", addData.get("CC_ORDERID"));
										etcData.put("CC_ORDERUSER", addData.get("CC_ORDERUSER"));
										etcData.put("CC_DETAILSAYU", addData.get("CC_DETAILSAYU"));
										etcData.put("CC_DEVSTDT", addData.get("CC_DEVSTDT"));
										etcData.put("CC_DEVEDDT", addData.get("CC_DEVEDDT"));
										etcData.put("CC_ENDPLAN", addData.get("CC_ENDPLAN"));
										etcData.put("gbn", "dev");
										jsonObj3 = jsonAry.get(i).getAsJsonObject();
										if (jsonObj3.has("EmpNo")) {
											itemData = jsonObj3.get("EmpNo").toString().replace("\"", "");
											etcData.put("cm_userid", itemData);							
											
											strQuery.setLength(0);
											strQuery.append("select cm_project from cmm0040   \n");
											strQuery.append(" where cm_userid=?               \n");
											pstmt = conn.prepareStatement(strQuery.toString());
											//pstmt = new LoggableStatement(conn,strQuery.toString());
											pstmt.setString(1, itemData);
											//ecamsLogger.error(((LoggableStatement)pstmt).getQueryString());
											rs = pstmt.executeQuery();
											if (rs.next()) {
												itemData = rs.getString("cm_project");
											} else itemData = "";
											rs.close();
											pstmt.close();
											
											etcData.put("cm_project", itemData);										

											if (jsonObj3.has("SeqNo")) {
												itemData = jsonObj3.get("SeqNo").toString().replace("\"", "");
												etcData.put("ordersubid", itemData);
											}
											if (jsonObj3.has("EmpStatus")) {
												itemData = jsonObj3.get("EmpStatus").toString().replace("\"", "");
												etcData.put("Status", itemData);	
											}
											rstList.add(etcData);
											ecamsLogger.error("Developer rstList="+rstList.toString());
											etcData = null;
										}
									}
								}
							}
						}
					}

					if (jsonObj.has("BizManager")) {
						jsonAry = jsonObj.getAsJsonArray("BizManager");
						if (jsonAry == null) returnMsg = "ERCV01:"+trCode+":"+itemName+":BizManager";
						
						if (returnMsg.length()==0) {
							for (i=0;jsonAry.size()>i;i++) {
								etcData = new HashMap<String,String>();
								etcData.put("gbn", "biz");
								etcData.put("CC_EDITOR", addData.get("CC_EDITOR"));
								etcData.put("CC_REQID", addData.get("CC_REQID"));
								etcData.put("CC_ORDERID", addData.get("CC_ORDERID"));
								etcData.put("CC_ORDERUSER", addData.get("CC_ORDERUSER"));
								etcData.put("CC_DETAILSAYU", addData.get("CC_DETAILSAYU"));
								etcData.put("CC_DEVSTDT", addData.get("CC_DEVSTDT"));
								etcData.put("CC_DEVEDDT", addData.get("CC_DEVEDDT"));
								etcData.put("CC_ENDPLAN", addData.get("CC_ENDPLAN"));
								
								jsonObj2 = jsonAry.get(i).getAsJsonObject();
								if (jsonObj2.has("EmpNo")) {
									itemData = jsonObj2.get("EmpNo").toString().replace("\"", "");	
									etcData.put("cm_userid", itemData);	
									
									strQuery.setLength(0);
									strQuery.append("select cm_project from cmm0040   \n");
									strQuery.append(" where cm_userid=?               \n");
									pstmt = conn.prepareStatement(strQuery.toString());
									//pstmt = new LoggableStatement(conn,strQuery.toString());
									pstmt.setString(1, itemData);
									//ecamsLogger.error(((LoggableStatement)pstmt).getQueryString());
									rs = pstmt.executeQuery();
									if (rs.next()) {
										itemData = rs.getString("cm_project");
									} else itemData = "";
									rs.close();
									pstmt.close();
									
									etcData.put("cm_project", itemData);
									
									if (jsonObj2.has("EmpStatus")) {
										itemData = jsonObj2.get("EmpStatus").toString().replace("\"", "");
										etcData.put("Status", itemData);	
									}

									rstList.add(etcData);
									etcData = null;
									ecamsLogger.error("bizmanager rstList="+rstList.toString());
								}
							}
						}
					}					
				}
			} else if ("CC_REQ_PRD_UPD".equals(trCode)) { //개발요청서 일정변경
				if ("RequestDoc".equals(itemName) || "SmRequestDoc".equals(itemName)) { //개발요청서
					if ("RequestDoc".equals(itemName)) itemData = jsonObj.get("RequestDocNo").toString().replace("\"", "");       //개발요청서번호
					else itemData = jsonObj.get("SmRequestDocNo").toString().replace("\"", "");     //외주개발요청서번호
					etcData.put("CC_REQID", itemData);
					
					if (jsonObj.has("StartDt")) {  //개발기간(시작일)
						itemData = jsonObj.get("StartDt").toString().replace("\"", "");   
						etcData.put("CC_DEVSTDT", itemData); 
					} else {
						etcData.put("CC_DEVSTDT","");
					}

					if (jsonObj.has("EndDt")) {  //개발기간(종료일)
						itemData = jsonObj.get("EndDt").toString().replace("\"", "");   
						etcData.put("CC_DEVEDDT", itemData); 
					} else {
						etcData.put("CC_DEVEDDT","");
					}

					if (jsonObj.has("EmpNo")) {  //담당자
						itemData = jsonObj.get("EmpNo").toString().replace("\"", "");   
						etcData.put("CC_EDITOR", itemData); 
						
						strQuery.setLength(0);
						strQuery.append("select cm_project from cmm0040   \n");
						strQuery.append(" where cm_userid=?               \n");
						pstmt = conn.prepareStatement(strQuery.toString());
						//pstmt = new LoggableStatement(conn,strQuery.toString());
						pstmt.setString(1, itemData);
						//ecamsLogger.error(((LoggableStatement)pstmt).getQueryString());
						rs = pstmt.executeQuery();
						if (rs.next()) {
							itemData = rs.getString("cm_project");
						} else itemData = "";
						rs.close();
						pstmt.close();						

						etcData.put("CC_ETTEAM",itemData); //담당팀
						
					} else {
						etcData.put("CC_EDITOR","");
						etcData.put("CC_ETTEAM","");
					}
					
					if (etcData.get("CC_DEVSTDT") != null && !"".equals(etcData.get("CC_DEVSTDT")) && 
						etcData.get("CC_DEVEDDT") != null && !"".equals(etcData.get("CC_DEVEDDT"))) {
						cmm1000 = new Cmm1000();
						int dateGap = cmm1000.getHoliDayAll_conn(etcData.get("CC_DEVSTDT"),etcData.get("CC_DEVEDDT"),conn);
						cmm1000 = null;
						etcData.put("CC_DEVPERIOD",Integer.toString(dateGap));
						//etcData.put("CC_DEVPERIOD","3"); //kcs
					}	
					rstList.add(etcData);
					etcData = null;
				} 
			}	else if ("CC_REQ_CLS".equals(trCode)) { //개발종료
				if ("RequestDoc".equals(itemName) || "SmRequestDoc".equals(itemName)) { //개발요청서
					if ("RequestDoc".equals(itemName)) itemData = jsonObj.get("RequestDocNo").toString().replace("\"", "");       //개발요청서번호
					else itemData = jsonObj.get("SmRequestDocNo").toString().replace("\"", "");     //외주개발요청서번호
					etcData.put("CC_REQID", itemData);

					if (jsonObj.has("EmpNo")) {  //담당자
						itemData = jsonObj.get("EmpNo").toString().replace("\"", "");   
						etcData.put("CC_EDITOR", itemData); 

						strQuery.setLength(0);
						strQuery.append("select cm_project from cmm0040   \n");
						strQuery.append(" where cm_userid=?               \n");
						pstmt = conn.prepareStatement(strQuery.toString());
						//pstmt = new LoggableStatement(conn,strQuery.toString());
						pstmt.setString(1, itemData);
						//ecamsLogger.error(((LoggableStatement)pstmt).getQueryString());
						rs = pstmt.executeQuery();
						if (rs.next()) {
							itemData = rs.getString("cm_project");
						} else itemData = "";
						rs.close();
						pstmt.close();
						
						etcData.put("CC_ETTEAM",itemData); //담당팀
						
					} else {
						etcData.put("CC_EDITOR","");
						etcData.put("CC_ETTEAM","");
					}
					rstList.add(etcData);
					etcData = null;
				}
			}		
    		
		} catch (Exception exception) {
			exception.printStackTrace();
			ecamsLogger.error("## SRRestApi.InputCheck() Exception START ##");				
			ecamsLogger.error("## Error DESC : ", exception);	
			ecamsLogger.error("## SRRestApi.InputCheck() Exception END ##");				
			returnMsg = "ERINX4:"+exception;
		}finally{
		}

		if (returnMsg.length()>0) {
			etcData = new HashMap<String,String>();
			etcData.put("errsw", "Y");
			etcData.put("errmsg", returnMsg);
			rstList.add(0, etcData);
			etcData = null;
		}
    	return rstList;
    }
    
    /*
     * 개발요청서등록 (CC_REQ_REG)  Request_CC_Reg.jsp
     */    
    public String setSRInfo(String inData) throws SQLException, Exception{
		Connection        conn        = null;
		PreparedStatement pstmt       = null;
		ResultSet         rs          = null;
		StringBuffer      strQuery    = new StringBuffer();
		ArrayList<HashMap<String, String>>  rsval  = new ArrayList<HashMap<String, String>>();
		ConnectionContext connectionContext = new ConnectionResource();	
		String            retMsg      = "";

    	JsonParser jsonparser = new JsonParser();
    	JsonObject jsonObj = new JsonObject();
    	JsonObject jsonObj2 = new JsonObject();
    	JsonObject jsonObj3 = new JsonObject();
    	String jsonStr = "";
    	
    	
    	HashMap<String,String> etcData = new HashMap<String,String>();
    	String strECAMSID1 = "";
    	String strECAMSSta1 = "0";
    	String strOrderID1 = "";
    	String strOrderSta1 = "0";
    	String strECAMSID2 = "";
    	String strECAMSSta2 = "0";
    	String strOrderID2 = "";
    	String strOrderSta2 = "0";
    	String strSRReqID = "";
    	String trCode = "CC_REQ_REG";
    	
		try {
			conn = connectionContext.getConnection();
			if (conn == null) retMsg = "ERDB00:DB Connection Fail";
			else retMsg = InputCheck(trCode,inData);
			
			if (!"OK".equals(retMsg)) retMsg = setError(retMsg);
			else {
				retMsg = "";
				//CMC0400 Insert Start!
				jsonObj = (JsonObject) jsonparser.parse(inData);
				strSRReqID = jsonObj.get("SrDevReqNo").toString().replace("\"", "");
				
				
				if (retMsg.length() == 0) {
					if (jsonObj.has("RequestDoc")) {
						jsonObj2 = jsonObj.getAsJsonObject("RequestDoc");
						rsval = dataConv(trCode,"RequestDoc",jsonObj2,conn);
						if (rsval.size()==0) retMsg = "ER0101:등록데이타 오류";
						else {
						    etcData = rsval.get(0);	
						
							if (etcData.get("errsw") != null && !"".equals(etcData.get("errsw"))) {
								retMsg = "ER0102:"+etcData.get("errmsg");
							} else if (etcData.get("CC_REQID") == null || "".equals(etcData.get("CC_REQID"))) retMsg = "ER0103:형상관리부여번호 없음";
						}
						
						if (retMsg.length()==0) {
							ecamsLogger.error("RequestDoc rsval="+rsval.toString());
							
							conn.setAutoCommit(false);
							
							etcData.put("SRReqID", strSRReqID);
							retMsg = insertCMC0400(etcData,conn);
							
							if ("OK".equals(retMsg.substring(0,2))) {								
								strECAMSID1 = retMsg.substring(2);
								retMsg = "";
							}
						}
						
						if (retMsg.length()==0) {
							if (jsonObj2.has("RecvDept") && strECAMSID1 != null && !"".equals(strECAMSID1)) {
								rsval.clear();
								rsval = dataConv(trCode,"RecvDept",jsonObj2,conn); 

								ecamsLogger.error("RecvDept rsval="+rsval.toString());							
								if (rsval.size()==0) retMsg = "ER0104:등록데이타 오류";
								else {
									
								    etcData = new HashMap<String,String>();
								    etcData = rsval.get(0);	
								
									if (etcData.get("errsw") != null && !"".equals(etcData.get("errsw"))) {
										retMsg = "ER0105:"+etcData.get("errmsg");
									} 
								}
								
								if (retMsg.length()==0) {
									retMsg = insertCMC0410(strECAMSID1,rsval,conn);
								}
							}
							//retMsg = "";        //kcs
							//strECAMSID1 = "OK"; //kcs 
						}
						
						if (retMsg.length()==0) {						
							if (jsonObj2.has("BizManager") && strECAMSID1 != null && !"".equals(strECAMSID1)) {
								rsval.clear();
								rsval = dataConv(trCode,"BizManager",jsonObj2,conn);
								
								ecamsLogger.error("BizManager rsval="+rsval.toString());
								
								if (rsval.size()==0) retMsg = "ER0106:등록데이타 오류";
								else {
									
								    etcData = new HashMap<String,String>();
								    etcData = rsval.get(0);	
								
									if (etcData.get("errsw") != null && !"".equals(etcData.get("errsw"))) {
										retMsg = "ER0107:"+etcData.get("errmsg");
									} 
								}
								
								if (retMsg.length()==0) {
									retMsg = BizManager_Cmc0401(strECAMSID1,rsval,conn);
								}
							}
						} 
						if (retMsg.length()==0) {						
							if (jsonObj2.has("OrderDoc") && strECAMSID1 != null && !"".equals(strECAMSID1)) {
								rsval.clear();
								rsval = dataConv(trCode,"OrderDoc",jsonObj2,conn);

								ecamsLogger.error("OrderDoc rsval="+rsval.toString());
								if (rsval.size()==0) retMsg = "ER0108:등록데이타 오류";
								else {
									
								    etcData = new HashMap<String,String>();
								    etcData = rsval.get(0);	
								
									if (etcData.get("errsw") != null && !"".equals(etcData.get("errsw"))) {
										retMsg = "ER0109:"+etcData.get("errmsg");
									} 
								}
								
								if (retMsg.length()==0) {
									retMsg = insertSROrder(strECAMSID1,rsval,conn);	

									if ("OK".equals(retMsg.substring(0,2))) {								
										strOrderID1 = retMsg.substring(2);
										retMsg = "";
									}
								}
							}
						} 
					}

					if (jsonObj.has("SmRequestDoc")) {
						jsonObj2 = jsonObj.getAsJsonObject("SmRequestDoc");
						rsval = dataConv(trCode,"SmRequestDoc",jsonObj2,conn);

						ecamsLogger.error("SmRequestDoc RequestDoc rsval="+rsval.toString());
						if (rsval.size()==0) retMsg = "ER0110:등록데이타 오류";
						else {
						    etcData = rsval.get(0);	
						
							if (etcData.get("errsw") != null && !"".equals(etcData.get("errsw"))) {
								retMsg = "ER0111:"+etcData.get("errmsg");
							} else if (etcData.get("CC_REQID") == null || "".equals(etcData.get("CC_REQID"))) retMsg = "ER0104:형상관리부여번호 없음";
						}
						
						if (retMsg.length()==0) {
							etcData.put("SRReqID", strSRReqID);
							retMsg = insertCMC0400(etcData,conn);
							
							if ("OK".equals(retMsg.substring(0,2))) {								
								strECAMSID2 = retMsg.substring(2);
								retMsg = "";
							}
						}
						
						if (retMsg.length()==0) {						
							if (jsonObj2.has("BizManager") && strECAMSID2 != null && !"".equals(strECAMSID2)) {
								rsval.clear();
								rsval = dataConv(trCode,"BizManager",jsonObj2,conn);
								if (rsval.size()==0) retMsg = "ER0112:등록데이타 오류";
								else {
									
								    etcData = new HashMap<String,String>();
								    etcData = rsval.get(0);	
								
									if (etcData.get("errsw") != null && !"".equals(etcData.get("errsw"))) {
										retMsg = "ER0113:"+etcData.get("errmsg");
									} 
								}
								
								if (retMsg.length()==0) {
									retMsg = BizManager_Cmc0401(strECAMSID2,rsval,conn);									
								}
							}
							if (retMsg.length()==0) {						
								if (jsonObj2.has("OrderDoc") && strECAMSID2 != null && !"".equals(strECAMSID2)) {
									rsval.clear();
									rsval = dataConv(trCode,"OrderDoc",jsonObj2,conn);
									if (rsval.size()==0) retMsg = "ER0114:등록데이타 오류";
									else {
										
									    etcData = new HashMap<String,String>();
									    etcData = rsval.get(0);	
									
										if (etcData.get("errsw") != null && !"".equals(etcData.get("errsw"))) {
											retMsg = "ER0115:"+etcData.get("errmsg");
										} 
									}
									
									if (retMsg.length()==0) {
										retMsg = insertSROrder(strECAMSID2,rsval,conn);	

										if ("OK".equals(retMsg.substring(0,2))) {								
											strOrderID2 = retMsg.substring(2);	
											retMsg = "";
										}					
									}
								}
							} 
						} 
					} 
				} 

				ecamsLogger.error("1.retMsg="+retMsg);
				
				//2024-08-27 17:56:49,502 ERROR (SRRestApi.java:1242) - 1.retMsg=OK지시-대여금고-2024-0002
				//2024-08-27 17:56:49,503 ERROR (SRRestApi.java:1251) - 2.retMsg=OK지시-대여금고-2024-0002
				//2024-08-27 17:56:49,504 ERROR (__request_cc_reg.java:116) - Request_CC_Reg.jsp Return={"ResultCD":"ERROR","ResultMsg":"OK지시-대여금고-2024-0002"}
				
				if (retMsg.length()==0) {
					if (!conn.getAutoCommit()) conn.commit();
				} else {
					if (!conn.getAutoCommit()) conn.rollback();
				}

			}
			ecamsLogger.error("2.retMsg="+retMsg);
			if (retMsg.length()==0) {
				jsonObj = new JsonObject();
				jsonObj.addProperty("ResultCD", "OK");
				jsonObj.addProperty("ResultMsg", "Success");
				jsonObj.addProperty("SrDevReqNo", strSRReqID);
				if (strECAMSID1.length()>0) {
					jsonObj2 = new JsonObject();
					jsonObj2.addProperty("RequestDocNo", strECAMSID1);
					jsonObj2.addProperty("RequestStatus", strECAMSSta1);
					
					if (strOrderID1.length()>0) {
						jsonObj3 = new JsonObject();
						jsonObj3.addProperty("OrderDocNo", strOrderID1);
						jsonObj3.addProperty("OrderStatus", strOrderSta1);
						jsonObj2.add("OrderDoc", jsonObj3);
					}
					jsonObj.add("RequestDoc", jsonObj2);
				}
				if (strECAMSID2.length()>0) {
					jsonObj2 = new JsonObject();
					jsonObj2.addProperty("SmRequestDocNo", strECAMSID2);
					jsonObj2.addProperty("SmRequestStatus", strECAMSSta2);
					
					if (strOrderID2.length()>0) {
						jsonObj3 = new JsonObject();
						jsonObj3.addProperty("OrderDocNo", strOrderID2);
						jsonObj3.addProperty("OrderStatus", strOrderSta2);
						jsonObj2.add("OrderDoc", jsonObj3);
					}
					jsonObj.add("SmRequestDoc", jsonObj2);
				}
			} else {
				jsonObj = new JsonObject();
				if (retMsg.indexOf(":")>0) {
					jsonObj.addProperty("ResultCD", retMsg.substring(0,retMsg.indexOf(":")));
					jsonObj.addProperty("ResultMsg", retMsg.substring(retMsg.indexOf(":")));
				} else {
					jsonObj.addProperty("ResultCD", "ERROR");
					jsonObj.addProperty("ResultMsg", retMsg);
				}
			}
			
			Gson gson = new Gson();
			jsonStr = gson.toJson(jsonObj);
			jsonObj = null;
			
			conn.close();
			conn = null;
			pstmt = null;
			rs = null;	
			
		} catch (SQLException sqlexception) {
			sqlexception.printStackTrace();
			ecamsLogger.error("## SRRestApi.setSRInfo() SQLException START ##");
			ecamsLogger.error("## Error DESC : ", sqlexception);	
			ecamsLogger.error("## SRRestApi.setSRInfo() SQLException END ##");			
			throw sqlexception;
		} catch (Exception exception) {
			exception.printStackTrace();
			ecamsLogger.error("## SRRestApi.setSRInfo() Exception START ##");				
			ecamsLogger.error("## Error DESC : ", exception);	
			ecamsLogger.error("## SRRestApi.setSRInfo() Exception END ##");				
			throw exception;
		}finally{
			if (strQuery != null) 	strQuery = null;
			if (rs != null)     try{rs.close();}catch (Exception ex){ex.printStackTrace();}
			if (pstmt != null)  try{pstmt.close();}catch (Exception ex2){ex2.printStackTrace();}
			if (conn != null){
				try{
					ConnectionResource.release(conn);
				}catch(Exception ex3){
					ecamsLogger.error("## SRRestApi.setSRInfo() connection release exception ##");
					ex3.printStackTrace();
				}
			}
		}
		
		return jsonStr;
	}//end of setSRInfo() method statement
    
    /*
     * 담당자변경 (CC_REQ_UPD)  Request_CC_Updt.jsp
     */
    public String setSRUpdt(String inData) throws SQLException, Exception{
		Connection        conn        = null;
		PreparedStatement pstmt       = null;
		ResultSet         rs          = null;
		StringBuffer      strQuery    = new StringBuffer();
		ArrayList<HashMap<String, String>>  rsval  = new ArrayList<HashMap<String, String>>();
		ConnectionContext connectionContext = new ConnectionResource();	
		String            retMsg      = "";

    	JsonParser jsonparser = new JsonParser();
    	JsonObject jsonObj = new JsonObject();
    	JsonObject jsonObj2 = new JsonObject();
    	JsonObject jsonObj3 = new JsonObject();
    	String jsonStr = "";
    	
    	
    	HashMap<String,String> etcData = new HashMap<String,String>();
    	String strECAMSID1 = "";
    	String strECAMSSta1 = "0";
    	String strOrderID1 = "";
    	String strOrderSta1 = "0";
    	String strECAMSID2 = "";
    	String strECAMSSta2 = "0";
    	String strOrderID2 = "";
    	String strOrderSta2 = "0";
    	String strSRReqID = "";
    	String trCode = "CC_REQ_UPD";
		try {
			conn = connectionContext.getConnection();
			if (conn == null) retMsg = "ERDB00:DB Connection Fail";
			else retMsg = InputCheck(trCode,inData);
			
			if (!"OK".equals(retMsg)) retMsg = setError(retMsg);
			else {
				retMsg = "";
				//CMC0400 Insert Start!
				jsonObj = (JsonObject) jsonparser.parse(inData);
				strSRReqID = jsonObj.get("SrDevReqNo").toString().replace("\"", "");
				
				if (retMsg.length() == 0) {
					if (jsonObj.has("RequestDoc")) {
						jsonObj2 = jsonObj.getAsJsonObject("RequestDoc");
						rsval = dataConv(trCode,"RequestDoc",jsonObj2,conn);

						ecamsLogger.error("setSRUpdt SrDevReqNo RequestDoc="+rsval.toString());
						
						if (rsval.size()==0) retMsg = "ER0201:등록데이타 오류";
						else {
						    etcData = rsval.get(0);	
						
							if (etcData.get("errsw") != null && !"".equals(etcData.get("errsw"))) {
								retMsg = "ER0202:"+etcData.get("errmsg");
							} else if (etcData.get("CC_REQID") == null || "".equals(etcData.get("CC_REQID"))) retMsg = "ER0203:형상관리부여번호 없음";
							else strECAMSID1 = etcData.get("CC_REQID");
						}
						
						if (retMsg.length()==0) {
							conn.setAutoCommit(false);
							retMsg = updateCMC0400(etcData,conn);
							
							if ("OK".equals(retMsg)) retMsg = "";
							
						}
						if (retMsg.length()==0) {
							
							conn.setAutoCommit(false);
							
							
							retMsg = BizManager_Cmc0401(strECAMSID1,rsval,conn);
							
							if ("OK".equals(retMsg)) retMsg = "";
							
							if (retMsg.length()==0) {
								retMsg = updateSROrder(rsval,conn);
								if ("OK".equals(retMsg.substring(0,2))) {								
									strOrderID1 = retMsg.substring(2);	
									retMsg = "";
								}	
								//if (retMsg.length()>2 && "OK".equals(retMsg.substring(0,2))) strOrderID1 = retMsg.substring(2);
							}
							
						}
					}

					if (jsonObj.has("SmRequestDoc")) {
						jsonObj2 = jsonObj.getAsJsonObject("SmRequestDoc");
						rsval = dataConv(trCode,"SmRequestDoc",jsonObj2,conn);

						ecamsLogger.error("setSRUpdt SmRequestDoc SmRequestDoc="+rsval.toString());
						
						if (rsval.size()==0) retMsg = "ER0204:등록데이타 오류";
						else {
						    etcData = rsval.get(0);	
						
							if (etcData.get("errsw") != null && !"".equals(etcData.get("errsw"))) {
								retMsg = "ER0205:"+etcData.get("errmsg");
							} else if (etcData.get("CC_REQID") == null || "".equals(etcData.get("CC_REQID"))) retMsg = "ER0206:형상관리부여번호 없음";
							else strECAMSID2 = etcData.get("CC_REQID");
						}

						if (retMsg.length()==0) {
							conn.setAutoCommit(false);
							retMsg = updateCMC0400(etcData,conn);
							
							if ("OK".equals(retMsg)) retMsg = "";
							
						}
						
						if (retMsg.length()==0) {
							
							conn.setAutoCommit(false);
							
							retMsg = BizManager_Cmc0401(strECAMSID2,rsval,conn);
							
							if ("OK".equals(retMsg)) retMsg = "";
							
							if (retMsg.length()==0) {
								retMsg = updateSROrder(rsval,conn);
								if ("OK".equals(retMsg.substring(0,2))) {								
									strOrderID2 = retMsg.substring(2);	
									retMsg = "";
								}	
								//if (retMsg.length()>2 && "OK".equals(retMsg.substring(0,2))) strOrderID2 = retMsg.substring(2);
							}
							
						}
					} 
				} 
				
				if (retMsg.length()==0) {
					if (!conn.getAutoCommit()) conn.commit();
				} else {
					if (!conn.getAutoCommit()) conn.rollback();
				}

			}
			
			if (retMsg.length()==0) {
				jsonObj = new JsonObject();
				jsonObj.addProperty("ResultCD", "OK");
				jsonObj.addProperty("ResultMsg", "Success");
				jsonObj.addProperty("SrDevReqNo", strSRReqID);
				if (strECAMSID1.length()>0) {
					jsonObj2 = new JsonObject();
					jsonObj2.addProperty("RequestDocNo", strECAMSID1);
					jsonObj2.addProperty("RequestStatus", strECAMSSta1);
					
					if (strOrderID1.length()>0) {
						jsonObj3 = new JsonObject();
						jsonObj3.addProperty("OrderDocNo", strOrderID1);
						jsonObj3.addProperty("OrderStatus", strOrderSta1);
						jsonObj2.add("OrderDoc", jsonObj3);
					}
					jsonObj.add("RequestDoc", jsonObj2);
				}
				if (strECAMSID2.length()>0) {
					jsonObj2 = new JsonObject();
					jsonObj2.addProperty("SmRequestDocNo", strECAMSID2);
					jsonObj2.addProperty("SmRequestStatus", strECAMSSta2);
					
					if (strOrderID2.length()>0) {
						jsonObj3 = new JsonObject();
						jsonObj3.addProperty("OrderDocNo", strOrderID2);
						jsonObj3.addProperty("OrderStatus", strOrderSta2);
						jsonObj2.add("OrderDoc", jsonObj3);
					}
					jsonObj.add("SmRequestDoc", jsonObj2);
				}
			} else {
				jsonObj = new JsonObject();
				if (retMsg.indexOf(":")>0) {
					jsonObj.addProperty("ResultCD", retMsg.substring(0,retMsg.indexOf(":")));
					jsonObj.addProperty("ResultMsg", retMsg.substring(retMsg.indexOf(":")));
				} else {
					jsonObj.addProperty("ResultCD", "ERROR");
					jsonObj.addProperty("ResultMsg", retMsg);
				}
			}
			Gson gson = new Gson();
			jsonStr = gson.toJson(jsonObj);
			jsonObj = null;
			
			conn.close();
			conn = null;
			pstmt = null;
			rs = null;	
			
		} catch (SQLException sqlexception) {
			sqlexception.printStackTrace();
			ecamsLogger.error("## SRRestApi.setSRUpdt() SQLException START ##");
			ecamsLogger.error("## Error DESC : ", sqlexception);	
			ecamsLogger.error("## SRRestApi.setSRUpdt() SQLException END ##");			
			throw sqlexception;
		} catch (Exception exception) {
			exception.printStackTrace();
			ecamsLogger.error("## SRRestApi.setSRUpdt() Exception START ##");				
			ecamsLogger.error("## Error DESC : ", exception);	
			ecamsLogger.error("## SRRestApi.setSRUpdt() Exception END ##");				
			throw exception;
		}finally{
			if (strQuery != null) 	strQuery = null;
			if (rs != null)     try{rs.close();}catch (Exception ex){ex.printStackTrace();}
			if (pstmt != null)  try{pstmt.close();}catch (Exception ex2){ex2.printStackTrace();}
			if (conn != null){
				try{
					ConnectionResource.release(conn);
				}catch(Exception ex3){
					ecamsLogger.error("## SRRestApi.setSRUpdt() connection release exception ##");
					ex3.printStackTrace();
				}
			}
		}

		
		if (retMsg.length()==0) {
			jsonObj = new JsonObject();
			jsonObj.addProperty("SrDevReqNo", strSRReqID);
			if (strECAMSID1.length()>0) {
				jsonObj2 = new JsonObject();
				jsonObj2.addProperty("RequestDocNo", strECAMSID1);
				jsonObj2.addProperty("RequestStatus", strECAMSSta1);
				
				if (strOrderID1.length()>0) {
					jsonObj3 = new JsonObject();
					jsonObj3.addProperty("OrderDocNo", strOrderID1);
					jsonObj3.addProperty("OrderStatus", strOrderSta1);
					jsonObj2.add("OrderDoc", jsonObj3);
				}
				jsonObj.add("RequestDoc", jsonObj2);
			}
			if (strECAMSID2.length()>0) {
				jsonObj2 = new JsonObject();
				jsonObj2.addProperty("SmRequestDocNo", strECAMSID2);
				jsonObj2.addProperty("SmRequestStatus", strECAMSSta2);
				
				if (strOrderID2.length()>0) {
					jsonObj3 = new JsonObject();
					jsonObj3.addProperty("OrderDocNo", strOrderID2);
					jsonObj3.addProperty("OrderStatus", strOrderSta2);
					jsonObj2.add("OrderDoc", jsonObj3);
				}
				jsonObj.add("SmRequestDoc", jsonObj2);
			}
			
			Gson gson = new Gson();
			jsonStr = gson.toJson(jsonObj);
		} else {
			
		}
		return jsonStr;
	}//end of setSRUpdt() method statement

    /*
     * 개발요청서 일정 변경 (CC_REQ_PRD_UPD)  Request_CC_PRD_Updt.jsp
     */
    public String setSRUpdt_PRD(String inData) throws SQLException, Exception{
		Connection        conn        = null;
		PreparedStatement pstmt       = null;
		ResultSet         rs          = null;
		StringBuffer      strQuery    = new StringBuffer();
		ArrayList<HashMap<String, String>>  rsval  = new ArrayList<HashMap<String, String>>();
		ConnectionContext connectionContext = new ConnectionResource();	
		String            retMsg      = "";

    	JsonParser jsonparser = new JsonParser();
    	JsonObject jsonObj = new JsonObject();
    	JsonObject jsonObj2 = new JsonObject();
    	JsonObject jsonObj3 = new JsonObject();
    	String jsonStr = "";
    	
    	
    	HashMap<String,String> etcData = new HashMap<String,String>();
    	String strECAMSID1 = "";
    	String strECAMSSta1 = "0";
    	String strOrderID1 = "";
    	String strOrderSta1 = "0";
    	String strECAMSID2 = "";
    	String strECAMSSta2 = "0";
    	String strOrderID2 = "";
    	String strOrderSta2 = "0";
    	String strSRReqID = "";
    	String trCode = "CC_REQ_PRD_UPD";
		try {
			conn = connectionContext.getConnection();
			if (conn == null) retMsg = "ERDB00:DB Connection Fail";
			else retMsg = InputCheck(trCode,inData);

			retMsg = InputCheck(trCode,inData); 
			
			if (!"OK".equals(retMsg)) retMsg = setError(retMsg);
			else {
				retMsg = "";
				//CMC0400 Insert Start!
				jsonObj = (JsonObject) jsonparser.parse(inData);
				strSRReqID = jsonObj.get("SrDevReqNo").toString().replace("\"", "");
				
				
				if (retMsg.length() == 0) {
					if (jsonObj.has("RequestDoc")) {
						jsonObj2 = jsonObj.getAsJsonObject("RequestDoc");
						rsval = dataConv(trCode,"RequestDoc",jsonObj2,conn);
						ecamsLogger.error("CC_REQ_PRD_UPD setSRUpdt_PRD RequestDoc="+rsval.toString());
						
						if (rsval.size()==0) retMsg = "ER0201:등록데이타 오류";
						else {
						    etcData = rsval.get(0);	
						
							if (etcData.get("errsw") != null && !"".equals(etcData.get("errsw"))) {
								retMsg = "ER0202:"+etcData.get("errmsg");
							} else if (etcData.get("CC_REQID") == null || "".equals(etcData.get("CC_REQID"))) retMsg = "ER0203:형상관리부여번호 없음";
							else strECAMSID1 = etcData.get("CC_REQID");
						}
						
						if (retMsg.length()==0) {
							conn.setAutoCommit(false);
							retMsg = updateCMC0400(etcData,conn);
							
							if ("OK".equals(retMsg)) retMsg = "";
							
						}
					}
					if (jsonObj.has("SmRequestDoc")) {

						ecamsLogger.error("CC_REQ_PRD_UPD SmRequestDoc START!!");
						
						jsonObj2 = jsonObj.getAsJsonObject("SmRequestDoc");
						rsval = dataConv(trCode,"SmRequestDoc",jsonObj2,conn);

						ecamsLogger.error("CC_REQ_PRD_UPD setSRUpdt_PRD SmRequestDoc="+rsval.toString());
						
						if (rsval.size()==0) retMsg = "ER0204:등록데이타 오류";
						else {
						    etcData = rsval.get(0);	
						
							if (etcData.get("errsw") != null && !"".equals(etcData.get("errsw"))) {
								retMsg = "ER0205:"+etcData.get("errmsg");
							} else if (etcData.get("CC_REQID") == null || "".equals(etcData.get("CC_REQID"))) retMsg = "ER0206:형상관리부여번호 없음";
							else strECAMSID2 = etcData.get("CC_REQID");
						}
						
						if (retMsg.length()==0) {
							conn.setAutoCommit(false);							
							retMsg = updateCMC0400(etcData,conn);
							
							if ("OK".equals(retMsg)) retMsg = "";
							
						}
					} 
				} 
				
				if (retMsg.length()==0) {
					if (!conn.getAutoCommit()) conn.commit();
				} else {
					if (!conn.getAutoCommit()) conn.rollback();
				}

			}
			
			if (retMsg.length()==0) {
				jsonObj = new JsonObject();
				jsonObj.addProperty("ResultCD", "OK");
				jsonObj.addProperty("ResultMsg", "Success");
				jsonObj.addProperty("SrDevReqNo", strSRReqID);
				if (strECAMSID1.length()>0) {
					jsonObj2 = new JsonObject();
					jsonObj2.addProperty("RequestDocNo", strECAMSID1);
					jsonObj2.addProperty("RequestStatus", strECAMSSta1);
					
					if (strOrderID1.length()>0) {
						jsonObj3 = new JsonObject();
						jsonObj3.addProperty("OrderDocNo", strOrderID1);
						jsonObj3.addProperty("OrderStatus", strOrderSta1);
						jsonObj2.add("OrderDoc", jsonObj3);
					}
					jsonObj.add("RequestDoc", jsonObj2);
				}
				if (strECAMSID2.length()>0) {
					jsonObj2 = new JsonObject();
					jsonObj2.addProperty("SmRequestDocNo", strECAMSID2);
					jsonObj2.addProperty("SmRequestStatus", strECAMSSta2);
					
					if (strOrderID2.length()>0) {
						jsonObj3 = new JsonObject();
						jsonObj3.addProperty("OrderDocNo", strOrderID2);
						jsonObj3.addProperty("OrderStatus", strOrderSta2);
						jsonObj2.add("OrderDoc", jsonObj3);
					}
					jsonObj.add("SmRequestDoc", jsonObj2);
				}
			} else {
				jsonObj = new JsonObject();
				if (retMsg.indexOf(":")>0) {
					jsonObj.addProperty("ResultCD", retMsg.substring(0,retMsg.indexOf(":")));
					jsonObj.addProperty("ResultMsg", retMsg.substring(retMsg.indexOf(":")));
				} else {
					jsonObj.addProperty("ResultCD", "ERROR");
					jsonObj.addProperty("ResultMsg", retMsg);
				}
			}
			Gson gson = new Gson();
			jsonStr = gson.toJson(jsonObj);
			jsonObj = null;
			
			//conn.close();
			conn = null;
			pstmt = null;
			rs = null;	
			
		} catch (SQLException sqlexception) {
			sqlexception.printStackTrace();
			ecamsLogger.error("## SRRestApi.setSRUpdt_PRD() SQLException START ##");
			ecamsLogger.error("## Error DESC : ", sqlexception);	
			ecamsLogger.error("## SRRestApi.setSRUpdt_PRD() SQLException END ##");			
			throw sqlexception;
		} catch (Exception exception) {
			exception.printStackTrace();
			ecamsLogger.error("## SRRestApi.setSRUpdt_PRD() Exception START ##");				
			ecamsLogger.error("## Error DESC : ", exception);	
			ecamsLogger.error("## SRRestApi.setSRUpdt_PRD() Exception END ##");				
			throw exception;
		}finally{
			if (strQuery != null) 	strQuery = null;
			if (rs != null)     try{rs.close();}catch (Exception ex){ex.printStackTrace();}
			if (pstmt != null)  try{pstmt.close();}catch (Exception ex2){ex2.printStackTrace();}
			if (conn != null){
				try{
					ConnectionResource.release(conn);
				}catch(Exception ex3){
					ecamsLogger.error("## SRRestApi.setSRUpdt_PRD() connection release exception ##");
					ex3.printStackTrace();
				}
			}
		}

		
		if (retMsg.length()==0) {
			jsonObj = new JsonObject();
			jsonObj.addProperty("SrDevReqNo", strSRReqID);
			if (strECAMSID1.length()>0) {
				jsonObj2 = new JsonObject();
				jsonObj2.addProperty("RequestDocNo", strECAMSID1);
				jsonObj2.addProperty("RequestStatus", strECAMSSta1);
				
				if (strOrderID1.length()>0) {
					jsonObj3 = new JsonObject();
					jsonObj3.addProperty("OrderDocNo", strOrderID1);
					jsonObj3.addProperty("OrderStatus", strOrderSta1);
					jsonObj2.add("OrderDoc", jsonObj3);
				}
				jsonObj.add("RequestDoc", jsonObj2);
			}
			if (strECAMSID2.length()>0) {
				jsonObj2 = new JsonObject();
				jsonObj2.addProperty("SmRequestDocNo", strECAMSID2);
				jsonObj2.addProperty("SmRequestStatus", strECAMSSta2);
				
				if (strOrderID2.length()>0) {
					jsonObj3 = new JsonObject();
					jsonObj3.addProperty("OrderDocNo", strOrderID2);
					jsonObj3.addProperty("OrderStatus", strOrderSta2);
					jsonObj2.add("OrderDoc", jsonObj3);
				}
				jsonObj.add("SmRequestDoc", jsonObj2);
			}
			
			Gson gson = new Gson();
			jsonStr = gson.toJson(jsonObj);
		} else {
			
		}
		return jsonStr;
	}//end of setSRUpdt_PRD() method statement
    
    /*
     * 개발요청서 종료(CC_REQ_CLS)  Request_CC_Close.jsp
     */
    public String setSRClose(String inData) throws SQLException, Exception{
		Connection        conn        = null;
		PreparedStatement pstmt       = null;
		ResultSet         rs          = null;
		StringBuffer      strQuery    = new StringBuffer();
		ArrayList<HashMap<String, String>>  rsval  = new ArrayList<HashMap<String, String>>();
		ConnectionContext connectionContext = new ConnectionResource();	
		String            retMsg      = "";

    	JsonParser jsonparser = new JsonParser();
    	JsonObject jsonObj = new JsonObject();
    	JsonObject jsonObj2 = new JsonObject();
    	JsonObject jsonObj3 = new JsonObject();
    	String jsonStr = "";
    	
    	
    	HashMap<String,String> etcData = new HashMap<String,String>();
    	String strECAMSID1 = "";
    	String strECAMSSta1 = "9";
    	String strOrderID1 = "";
    	String strOrderSta1 = "9";
    	String strECAMSID2 = "";
    	String strECAMSSta2 = "9";
    	String strOrderID2 = "";
    	String strOrderSta2 = "9";
    	String strSRReqID = "";
    	String trCode = "CC_REQ_CLS";
		try {
			conn = connectionContext.getConnection();
			if (conn == null) retMsg = "ERDB00:DB Connection Fail";
			else retMsg = InputCheck(trCode,inData);
			
			if (!"OK".equals(retMsg)) retMsg = setError(retMsg);
			else {
				retMsg = "";
				//CMC0400 Insert Start!
				jsonObj = (JsonObject) jsonparser.parse(inData);
				strSRReqID = jsonObj.get("SrDevReqNo").toString().replace("\"", "");
				
				
				if (retMsg.length() == 0) {
					if (jsonObj.has("RequestDoc")) {
						jsonObj2 = jsonObj.getAsJsonObject("RequestDoc");
						rsval = dataConv(trCode,"RequestDoc",jsonObj2,conn);

						ecamsLogger.error("setSRClose RequestDoc="+rsval.toString());
						
						if (rsval.size()==0) retMsg = "ER0201:등록데이타 오류";
						else {
						    etcData = rsval.get(0);	
						
							if (etcData.get("errsw") != null && !"".equals(etcData.get("errsw"))) {
								retMsg = "ER0202:"+etcData.get("errmsg");
							} else if (etcData.get("CC_REQID") == null || "".equals(etcData.get("CC_REQID"))) retMsg = "ER0203:형상관리부여번호 없음";
							else strECAMSID1 = etcData.get("CC_REQID");
						}
						
						if (retMsg.length()==0) {

							conn.setAutoCommit(false);							
							retMsg = closeCMC0400(etcData,conn);
							if ("OK".equals(retMsg)) retMsg = "";
							
						}
					}
					if (jsonObj.has("SmRequestDoc")) {
						jsonObj2 = jsonObj.getAsJsonObject("SmRequestDoc");
						rsval = dataConv(trCode,"SmRequestDoc",jsonObj2,conn);

						ecamsLogger.error("setSRClose SmRequestDoc="+rsval.toString());
						if (rsval.size()==0) retMsg = "ER0204:등록데이타 오류";
						else {
						    etcData = rsval.get(0);	
						
							if (etcData.get("errsw") != null && !"".equals(etcData.get("errsw"))) {
								retMsg = "ER0205:"+etcData.get("errmsg");
							} else if (etcData.get("CC_REQID") == null || "".equals(etcData.get("CC_REQID"))) retMsg = "ER0206:형상관리부여번호 없음";
							else strECAMSID2 = etcData.get("CC_REQID");
						}
						
						if (retMsg.length()==0) {

							conn.setAutoCommit(false);							
							retMsg = closeCMC0400(etcData,conn);
							
							if ("OK".equals(retMsg)) retMsg = "";
							
						}
					} 
				} 
				
				if (retMsg.length()==0) {
					if (!conn.getAutoCommit()) conn.commit();
				} else {
					if (!conn.getAutoCommit()) conn.rollback();
				}

			}
			
			if (retMsg.length()==0) {
				jsonObj = new JsonObject();
				jsonObj.addProperty("SrDevReqNo", strSRReqID);
				if (strECAMSID1.length()>0) {
					jsonObj2 = new JsonObject();
					jsonObj2.addProperty("RequestDocNo", strECAMSID1);
					jsonObj2.addProperty("RequestStatus", strECAMSSta1);
					
					if (strOrderID1.length()>0) {
						jsonObj3 = new JsonObject();
						jsonObj3.addProperty("OrderDocNo", strOrderID1);
						jsonObj3.addProperty("OrderStatus", strOrderSta1);
						jsonObj2.add("OrderDoc", jsonObj3);
					}
					jsonObj.add("RequestDoc", jsonObj2);
				}
				if (strECAMSID2.length()>0) {
					jsonObj2 = new JsonObject();
					jsonObj2.addProperty("SmRequestDocNo", strECAMSID2);
					jsonObj2.addProperty("SmRequestStatus", strECAMSSta2);
					
					if (strOrderID2.length()>0) {
						jsonObj3 = new JsonObject();
						jsonObj3.addProperty("OrderDocNo", strOrderID2);
						jsonObj3.addProperty("OrderStatus", strOrderSta2);
						jsonObj2.add("OrderDoc", jsonObj3);
					}
					jsonObj.add("SmRequestDoc", jsonObj2);
				}
				
				Gson gson = new Gson();
				jsonStr = gson.toJson(jsonObj);
			} else {
				
			}
			conn.close();
			conn = null;
			pstmt = null;
			rs = null;	
			
		} catch (SQLException sqlexception) {
			sqlexception.printStackTrace();
			ecamsLogger.error("## SRRestApi.setSRClose() SQLException START ##");
			ecamsLogger.error("## Error DESC : ", sqlexception);	
			ecamsLogger.error("## SRRestApi.setSRClose() SQLException END ##");			
			throw sqlexception;
		} catch (Exception exception) {
			exception.printStackTrace();
			ecamsLogger.error("## SRRestApi.setSRClose() Exception START ##");				
			ecamsLogger.error("## Error DESC : ", exception);	
			ecamsLogger.error("## SRRestApi.setSRClose() Exception END ##");				
			throw exception;
		}finally{
			if (strQuery != null) 	strQuery = null;
			if (rs != null)     try{rs.close();}catch (Exception ex){ex.printStackTrace();}
			if (pstmt != null)  try{pstmt.close();}catch (Exception ex2){ex2.printStackTrace();}
			if (conn != null){
				try{
					ConnectionResource.release(conn);
				}catch(Exception ex3){
					ecamsLogger.error("## SRRestApi.setSRClose() connection release exception ##");
					ex3.printStackTrace();
				}
			}
		}

		
		if (retMsg.length()==0) {
			jsonObj = new JsonObject();
			jsonObj.addProperty("SrDevReqNo", strSRReqID);
			if (strECAMSID1.length()>0) {
				jsonObj2 = new JsonObject();
				jsonObj2.addProperty("RequestDocNo", strECAMSID1);
				jsonObj2.addProperty("RequestStatus", strECAMSSta1);
				
				if (strOrderID1.length()>0) {
					jsonObj3 = new JsonObject();
					jsonObj3.addProperty("OrderDocNo", strOrderID1);
					jsonObj3.addProperty("OrderStatus", strOrderSta1);
					jsonObj2.add("OrderDoc", jsonObj3);
				}
				jsonObj.add("RequestDoc", jsonObj2);
			}
			if (strECAMSID2.length()>0) {
				jsonObj2 = new JsonObject();
				jsonObj2.addProperty("SmRequestDocNo", strECAMSID2);
				jsonObj2.addProperty("SmRequestStatus", strECAMSSta2);
				
				if (strOrderID2.length()>0) {
					jsonObj3 = new JsonObject();
					jsonObj3.addProperty("OrderDocNo", strOrderID2);
					jsonObj3.addProperty("OrderStatus", strOrderSta2);
					jsonObj2.add("OrderDoc", jsonObj3);
				}
				jsonObj.add("SmRequestDoc", jsonObj2);
			}
			
			Gson gson = new Gson();
			jsonStr = gson.toJson(jsonObj);
		} else {
			
		}
		return jsonStr;
	}//end of setSRClose() method statement
    public String insertCMC0400(HashMap<String, String> etcData,Connection conn) throws SQLException, Exception{
		PreparedStatement pstmt       = null;
		ResultSet         rs          = null;
		StringBuffer      strQuery    = new StringBuffer();
		int				  pstmtcount  = 1;
		String            retMsg      = "";

    	
    	
    	String strECAMSID = "";
		try {			
			strQuery.setLength(0);                		
    		strQuery.append("select lpad(to_number(max(substr(cc_reqid,-4)))+ 1,4,'0') as max	 	\n");               		
    		strQuery.append("  from cmc0400 												\n"); 
    		strQuery.append(" where cc_reqid like ?									 		\n"); 
    		pstmt = conn.prepareStatement(strQuery.toString());	
    		pstmt.setString(1, etcData.get("CC_REQID")+"%");
	        rs = pstmt.executeQuery();
	        if (rs.next()) {
	        	if (rs.getString("max") == null) {
	        		strECAMSID = etcData.get("CC_REQID") + "-0001";
	        	} 
	        	else {
	        		strECAMSID = etcData.get("CC_REQID") + "-" + rs.getString("max");
	        	}
	        }
	        rs.close();
	        pstmt.close();
	        
			strQuery.setLength(0);
			strQuery.append("select count(*) cnt from cmc0400   \n");
			strQuery.append(" where cc_reqid=?                  \n");
			pstmt = conn.prepareStatement(strQuery.toString());
			//pstmt = new LoggableStatement(conn,strQuery.toString());
			pstmt.setString(1, strECAMSID);
			//ecamsLogger.error(((LoggableStatement)pstmt).getQueryString());
			rs = pstmt.executeQuery();
			if (rs.next()) {
				if (rs.getInt("cnt")>0) retMsg = "ER0104:기 등록된 SR개발요청번호";
			}
			rs.close();
			pstmt.close();
						
			if (retMsg.length()==0) {
				strQuery.setLength(0);
	        	strQuery.append("insert into cmc0400				                              				\n");
	        	strQuery.append("    (CC_REQID,CC_DOCTYPE,CC_DOCNUM,CC_DOCSUBJ,CC_DEPT1,CC_REQUSER1, 			\n");
	        	strQuery.append("     CC_DEPT2,CC_REQUSER2,CC_DOCNUM2,CC_DETAILJOBN, 							\n");
	        	strQuery.append("     CC_REQTYPE,CC_ENDPLAN,CC_JOBCD,CC_ACTTYPE,CC_JOBDIF,CC_CHKTEAM, 			\n");
	        	strQuery.append("     CC_CHKDATA,CC_DEVSTDT,CC_DEVEDDT,CC_DETAILSAYU,CC_STARTDT,				\n");
	        	strQuery.append("     CC_STATUS,CC_EDITOR,CC_ETTEAM,CC_DEVPERIOD,CC_SRREQID) values						\n");
	        	strQuery.append("(?,?,?,?,?,?,    ?,?,?,?,    ?,?,?,?,?,?,    ?,?,?,?,sysdate,	   '0',?,?,?,?)	\n");
	        	
	        	pstmt = conn.prepareStatement(strQuery.toString());
				pstmt = new LoggableStatement(conn,strQuery.toString());
				pstmtcount = 1;
	        	pstmt.setString(pstmtcount++, strECAMSID);
	        	pstmt.setString(pstmtcount++, etcData.get("CC_DOCTYPE"));
	        	pstmt.setString(pstmtcount++, etcData.get("CC_DOCNUM"));
	        	pstmt.setString(pstmtcount++, etcData.get("CC_DOCSUBJ"));
	        	pstmt.setString(pstmtcount++, etcData.get("CC_DEPT1"));
	        	pstmt.setString(pstmtcount++, etcData.get("CC_REQUSER1"));
	        	pstmt.setString(pstmtcount++, etcData.get("CC_DEPT2"));
	        	pstmt.setString(pstmtcount++, etcData.get("CC_REQUSER2"));
	        	pstmt.setString(pstmtcount++, etcData.get("CC_DOCNUM2"));
	        	pstmt.setString(pstmtcount++, etcData.get("CC_DETAILJOBN"));
	        	pstmt.setString(pstmtcount++, etcData.get("CC_REQTYPE"));
	        	pstmt.setString(pstmtcount++, etcData.get("CC_ENDPLAN"));
	        	pstmt.setString(pstmtcount++, etcData.get("CC_JOBCD"));
	        	pstmt.setString(pstmtcount++, etcData.get("CC_ACTTYPE"));
	        	pstmt.setString(pstmtcount++, etcData.get("CC_JOBDIF"));
	        	pstmt.setString(pstmtcount++, etcData.get("CC_CHKTEAM"));
	        	pstmt.setString(pstmtcount++, etcData.get("CC_CHKDATA"));
	        	pstmt.setString(pstmtcount++, etcData.get("CC_DEVSTDT"));
	        	pstmt.setString(pstmtcount++, etcData.get("CC_DEVEDDT"));
	        	pstmt.setString(pstmtcount++, etcData.get("CC_DETAILSAYU"));
	        	pstmt.setString(pstmtcount++, etcData.get("CC_EDITOR"));
	        	pstmt.setString(pstmtcount++, etcData.get("CC_ETTEAM"));
	        	pstmt.setString(pstmtcount++, etcData.get("CC_DEVPERIOD"));
	        	pstmt.setString(pstmtcount++, etcData.get("SRReqID"));
	        	
	        	ecamsLogger.error(((LoggableStatement)pstmt).getQueryString());
	        	pstmt.executeUpdate();
	        	pstmt.close();
	        	
	        	retMsg = "OK"+strECAMSID;
			}
			pstmt = null;
			rs = null;
			
			return retMsg;			
			
		} catch (SQLException sqlexception) {
			sqlexception.printStackTrace();
			ecamsLogger.error("## SRRestApi.insertCMC0400() SQLException START ##");
			ecamsLogger.error("## Error DESC : ", sqlexception);	
			ecamsLogger.error("## SRRestApi.insertCMC0400() SQLException END ##");			
			throw sqlexception;
		} catch (Exception exception) {
			exception.printStackTrace();
			ecamsLogger.error("## SRRestApi.insertCMC0400() Exception START ##");				
			ecamsLogger.error("## Error DESC : ", exception);	
			ecamsLogger.error("## SRRestApi.insertCMC0400() Exception END ##");				
			throw exception;
		}finally{
			if (strQuery != null) 	strQuery = null;
			if (rs != null)     try{rs.close();}catch (Exception ex){ex.printStackTrace();}
			if (pstmt != null)  try{pstmt.close();}catch (Exception ex2){ex2.printStackTrace();}
		}
	}//end of insertCMC0400() method statement
    
    public String updateCMC0400_2(HashMap<String, String> etcData,Connection conn) throws SQLException, Exception{
 		PreparedStatement pstmt       = null;
 		StringBuffer      strQuery    = new StringBuffer();
 		int				  pstmtcount  = 1;
 		String            retMsg      = "";

     	
 		try {			
 			strQuery.setLength(0);
         	strQuery.append("update cmc0400 set	    \n");
         	strQuery.append("     CC_EDITOR=? 		\n");
         	strQuery.append("where CC_REQID = ?		\n");
         	pstmt = conn.prepareStatement(strQuery.toString());
 			pstmt = new LoggableStatement(conn,strQuery.toString());
 			pstmtcount = 1;
         	pstmt.setString(pstmtcount++, etcData.get("CC_EDITOR"));
         	pstmt.setString(pstmtcount++, etcData.get("CC_REQID"));
         	ecamsLogger.error(((LoggableStatement)pstmt).getQueryString());
         	pstmt.executeUpdate();
         	pstmt.close();
 	        	
         	
 	        retMsg = "OK";
 			pstmt = null;
 			
 			return retMsg;			
 			
 		} catch (SQLException sqlexception) {
 			sqlexception.printStackTrace();
 			ecamsLogger.error("## SRRestApi.updateCMC0400() SQLException START ##");
 			ecamsLogger.error("## Error DESC : ", sqlexception);	
 			ecamsLogger.error("## SRRestApi.updateCMC0400() SQLException END ##");			
 			throw sqlexception;
 		} catch (Exception exception) {
 			exception.printStackTrace();
 			ecamsLogger.error("## SRRestApi.updateCMC0400() Exception START ##");				
 			ecamsLogger.error("## Error DESC : ", exception);	
 			ecamsLogger.error("## SRRestApi.updateCMC0400() Exception END ##");				
 			throw exception;
 		}finally{
 			if (strQuery != null) 	strQuery = null;
 			if (pstmt != null)  try{pstmt.close();}catch (Exception ex2){ex2.printStackTrace();}
 		}
 	}//end of updateCMC0400() method statement
    public String updateCMC0400(HashMap<String, String> etcData,Connection conn) throws SQLException, Exception{
		PreparedStatement pstmt       = null;
		StringBuffer      strQuery    = new StringBuffer();
		int				  pstmtcount  = 1;
		String            retMsg      = "";

    	
		try {			
			strQuery.setLength(0);
        	strQuery.append("update cmc0400 set				                              					\n");
        	strQuery.append("     CC_DEVSTDT=?,CC_DEVEDDT=?,CC_EDITOR=?,CC_ETTEAM=?,CC_DEVPERIOD=?,   		\n");
        	strQuery.append("     CC_DETAILSAYU=?,CC_ENDPLAN=?                                         		\n");        	
        	strQuery.append("where CC_REQID = ?																\n");
        	pstmt = conn.prepareStatement(strQuery.toString());
			pstmt = new LoggableStatement(conn,strQuery.toString());
			pstmtcount = 1;
        	pstmt.setString(pstmtcount++, etcData.get("CC_DEVSTDT"));
        	pstmt.setString(pstmtcount++, etcData.get("CC_DEVEDDT"));
        	pstmt.setString(pstmtcount++, etcData.get("CC_EDITOR"));
        	pstmt.setString(pstmtcount++, etcData.get("CC_ETTEAM"));
        	pstmt.setString(pstmtcount++, etcData.get("CC_DEVPERIOD"));
        	pstmt.setString(pstmtcount++, etcData.get("CC_DETAILSAYU"));
        	pstmt.setString(pstmtcount++, etcData.get("CC_ENDPLAN"));
        	pstmt.setString(pstmtcount++, etcData.get("CC_REQID"));
        	ecamsLogger.error(((LoggableStatement)pstmt).getQueryString());
        	pstmt.executeUpdate();
        	pstmt.close();
	        
        	
        	strQuery.setLength(0);
        	strQuery.append("update cmc0420 set		\n");
        	strQuery.append("       CC_ENDPLAN=?, 	\n");
        	strQuery.append("       CC_DETAILSAYU=?,\n");
        	strQuery.append("       CC_ORDERUSER=?	\n");
        	strQuery.append("where  CC_ORDERID =?	\n");
        	pstmt = conn.prepareStatement(strQuery.toString());
			pstmt = new LoggableStatement(conn,strQuery.toString());
			pstmtcount = 1;
        	pstmt.setString(pstmtcount++, etcData.get("CC_ENDPLAN"));
        	pstmt.setString(pstmtcount++, etcData.get("CC_DETAILSAYU"));
        	pstmt.setString(pstmtcount++, etcData.get("CC_ORDERUSER"));
        	pstmt.setString(pstmtcount++, etcData.get("CC_ORDERID"));
        	ecamsLogger.error(((LoggableStatement)pstmt).getQueryString());
        	pstmt.executeUpdate();
        	pstmt.close();
        	
	        retMsg = "OK";
			pstmt = null;
			
			return retMsg;			
			
		} catch (SQLException sqlexception) {
			sqlexception.printStackTrace();
			ecamsLogger.error("## SRRestApi.updateCMC0400() SQLException START ##");
			ecamsLogger.error("## Error DESC : ", sqlexception);	
			ecamsLogger.error("## SRRestApi.updateCMC0400() SQLException END ##");			
			throw sqlexception;
		} catch (Exception exception) {
			exception.printStackTrace();
			ecamsLogger.error("## SRRestApi.updateCMC0400() Exception START ##");				
			ecamsLogger.error("## Error DESC : ", exception);	
			ecamsLogger.error("## SRRestApi.updateCMC0400() Exception END ##");				
			throw exception;
		}finally{
			if (strQuery != null) 	strQuery = null;
			if (pstmt != null)  try{pstmt.close();}catch (Exception ex2){ex2.printStackTrace();}
		}
	}//end of updateCMC0400() method statement
    public String closeCMC0400(HashMap<String, String> etcData,Connection conn) throws SQLException, Exception{
		PreparedStatement pstmt       = null;
		StringBuffer      strQuery    = new StringBuffer();
		int				  pstmtcount  = 1;
		String            retMsg      = "";

    	
		try {		
			//수신부서
			strQuery.setLength(0);
        	strQuery.append("update cmc0410 set				   		\n");
        	strQuery.append("     CC_STATUS='9',CC_ENDDT=SYSDATE	\n");
        	strQuery.append("where CC_REQID = ?						\n");
        	pstmt = conn.prepareStatement(strQuery.toString());
			pstmt = new LoggableStatement(conn,strQuery.toString());
			pstmtcount = 1;
        	pstmt.setString(pstmtcount++, etcData.get("CC_REQID"));
        	ecamsLogger.error(((LoggableStatement)pstmt).getQueryString());
        	pstmt.executeUpdate();
        	pstmt.close();	
        	
        	//업무지시서
			strQuery.setLength(0);
        	strQuery.append("update cmc0420 set				   		\n");
        	strQuery.append("     CC_STATUS='9',CC_ENDDT=SYSDATE	\n");
        	strQuery.append("where CC_REQID = ?						\n");
        	pstmt = conn.prepareStatement(strQuery.toString());
			pstmt = new LoggableStatement(conn,strQuery.toString());
			pstmtcount = 1;
        	pstmt.setString(pstmtcount++, etcData.get("CC_REQID"));
        	ecamsLogger.error(((LoggableStatement)pstmt).getQueryString());
        	pstmt.executeUpdate();
        	pstmt.close();
        	
        	//개발요청서
			strQuery.setLength(0);
        	strQuery.append("update cmc0400 set				   		\n");
        	strQuery.append("     CC_STATUS='9',CC_ENDDT=SYSDATE	\n");
        	strQuery.append("where CC_REQID = ?						\n");
        	pstmt = conn.prepareStatement(strQuery.toString());
			pstmt = new LoggableStatement(conn,strQuery.toString());
			pstmtcount = 1;
        	pstmt.setString(pstmtcount++, etcData.get("CC_REQID"));
        	ecamsLogger.error(((LoggableStatement)pstmt).getQueryString());
        	pstmt.executeUpdate();
        	pstmt.close();
	        	
	        retMsg = "OK";
			pstmt = null;
			
			return retMsg;			
			
		} catch (SQLException sqlexception) {
			sqlexception.printStackTrace();
			ecamsLogger.error("## SRRestApi.closeCMC0400() SQLException START ##");
			ecamsLogger.error("## Error DESC : ", sqlexception);	
			ecamsLogger.error("## SRRestApi.closeCMC0400() SQLException END ##");			
			throw sqlexception;
		} catch (Exception exception) {
			exception.printStackTrace();
			ecamsLogger.error("## SRRestApi.closeCMC0400() Exception START ##");				
			ecamsLogger.error("## Error DESC : ", exception);	
			ecamsLogger.error("## SRRestApi.closeCMC0400() Exception END ##");				
			throw exception;
		}finally{
			if (strQuery != null) 	strQuery = null;
			if (pstmt != null)  try{pstmt.close();}catch (Exception ex2){ex2.printStackTrace();}
		}
	}//end of closeCMC0400() method statement
    public String insertCMC0410(String strECAMSID,ArrayList<HashMap<String, String>> rsval,Connection conn) throws SQLException, Exception{
		PreparedStatement pstmt       = null;
		ResultSet         rs          = null;
		StringBuffer      strQuery    = new StringBuffer();
		int				  pstmtcount  = 1;
		String            retMsg      = "";

    	int      i = 0;
		String   strSUBID      = "";
		try {
			//수신부서
			for (i=0;rsval.size()>i;i++) {
				strSUBID = "";
        		strQuery.setLength(0);                		
        		strQuery.append("select lpad(to_number(max(CC_SUBID))+ 1,2,'0') as max 	\n");               		
        		strQuery.append("  from cmc0410 										\n"); 
        		strQuery.append(" where CC_REQID=?									 	\n"); 
        		pstmt = conn.prepareStatement(strQuery.toString());	
        		pstmt.setString(1, strECAMSID);
    	        rs = pstmt.executeQuery();
    	        if (rs.next()) {
    	        	if (rs.getString("max") == null) {
    	        		strSUBID = "01";
    	        	} 
    	        	else {
    	        		strSUBID = rs.getString("max");
    	        	}
    	        }
    	        rs.close();
    	        pstmt.close();
    	        
	        	strQuery.setLength(0);
	        	strQuery.append("insert into cmc0410				                  	\n");
	        	strQuery.append("    (CC_SUBID,CC_REQID,CC_TEAM,CC_STATUS,CC_STARTDT) 	\n");
	        	strQuery.append("values(?,?,?,'0',sysdate)								\n");						        	
	        	pstmt = conn.prepareStatement(strQuery.toString());
				pstmt = new LoggableStatement(conn,strQuery.toString());
				pstmtcount = 1;
	        	pstmt.setString(pstmtcount++, strSUBID);
	        	pstmt.setString(pstmtcount++, strECAMSID);
	        	pstmt.setString(pstmtcount++, rsval.get(i).get("cm_project"));						        	
	        	ecamsLogger.error(((LoggableStatement)pstmt).getQueryString());
	        	pstmt.executeUpdate();
	        	pstmt.close();					        	
        	}
			pstmt = null;
			rs = null;
			
			return retMsg;			
			
		} catch (SQLException sqlexception) {
			sqlexception.printStackTrace();
			ecamsLogger.error("## SRRestApi.insertCMC0410() SQLException START ##");
			ecamsLogger.error("## Error DESC : ", sqlexception);	
			ecamsLogger.error("## SRRestApi.insertCMC0410() SQLException END ##");			
			throw sqlexception;
		} catch (Exception exception) {
			exception.printStackTrace();
			ecamsLogger.error("## SRRestApi.insertCMC0410() Exception START ##");				
			ecamsLogger.error("## Error DESC : ", exception);	
			ecamsLogger.error("## SRRestApi.insertCMC0410() Exception END ##");				
			throw exception;
		}finally{
			if (strQuery != null) 	strQuery = null;
			if (rs != null)     try{rs.close();}catch (Exception ex){ex.printStackTrace();}
			if (pstmt != null)  try{pstmt.close();}catch (Exception ex2){ex2.printStackTrace();}
		}
	}//end of insertCMC0410() method statement

    public String BizManager_Cmc0401(String strECAMSID,ArrayList<HashMap<String, String>> rsval,Connection conn) throws SQLException, Exception{
		PreparedStatement pstmt       = null;
		ResultSet         rs          = null;
		StringBuffer      strQuery    = new StringBuffer();
		int				  pstmtcount  = 1;
		String            retMsg      = "";
		boolean           findSw      = false;

    	int      i = 0;
		try {
			//주관부서,담당자
			for (i=0;rsval.size()>i;i++) {
				if ("biz".equals(rsval.get(i).get("gbn"))) {
					findSw = false;
					if (rsval.get(i).get("Status") != null && !"".equals(rsval.get(i).get("Status")) && "00".equals(rsval.get(i).get("Status"))) {
						strQuery.setLength(0);
			        	strQuery.append("delete cmc0401   		\n");
			        	strQuery.append(" where cc_reqid=?		\n");
			        	strQuery.append("   and cc_requser3=?	\n");			        	
			        	pstmt = conn.prepareStatement(strQuery.toString());
						pstmt = new LoggableStatement(conn,strQuery.toString());
						pstmtcount = 1;
						pstmt.setString(pstmtcount++, strECAMSID);
			        	pstmt.setString(pstmtcount++, rsval.get(i).get("cm_userid"));
			        	ecamsLogger.error(((LoggableStatement)pstmt).getQueryString());
			        	pstmt.executeUpdate();
			        	pstmt.close();
					} else {
						strQuery.setLength(0);
			        	strQuery.append("select count(*) cnt	\n");
			        	strQuery.append("   from cmc0401		\n");
			        	strQuery.append(" where cc_reqid=?		\n");
			        	strQuery.append("   and cc_requser3=?	\n");			        	
			        	pstmt = conn.prepareStatement(strQuery.toString());
						pstmt = new LoggableStatement(conn,strQuery.toString());
						pstmtcount = 1;
						pstmt.setString(pstmtcount++, strECAMSID);
			        	pstmt.setString(pstmtcount++, rsval.get(i).get("cm_userid"));
			        	ecamsLogger.error(((LoggableStatement)pstmt).getQueryString());
			        	rs = pstmt.executeQuery();
			        	if (rs.next()) {
			        		if (rs.getInt("cnt")>0) findSw = true;
			        		
			        	} 
			        	rs.close();
			        	pstmt.close();
			        	
			        	if (findSw) {
			        		strQuery.setLength(0);
				        	strQuery.append("delete cmc0401   		\n");
				        	strQuery.append(" where cc_reqid=?		\n");
				        	strQuery.append("   and cc_requser3=?	\n");			        	
				        	pstmt = conn.prepareStatement(strQuery.toString());
							pstmt = new LoggableStatement(conn,strQuery.toString());
							pstmtcount = 1;
							pstmt.setString(pstmtcount++, strECAMSID);
				        	pstmt.setString(pstmtcount++, rsval.get(i).get("cm_userid"));
				        	ecamsLogger.error(((LoggableStatement)pstmt).getQueryString());
				        	pstmt.executeUpdate();
				        	pstmt.close();
			        	} 
						strQuery.setLength(0);
			        	strQuery.append("insert into cmc0401					\n");
			        	strQuery.append("    (CC_REQID,CC_DEPT3,CC_REQUSER3)	\n");
			        	strQuery.append("values(?,?,?)			   			    \n");			        	
			        	pstmt = conn.prepareStatement(strQuery.toString());
						pstmt = new LoggableStatement(conn,strQuery.toString());
						pstmtcount = 1;
						pstmt.setString(pstmtcount++, strECAMSID);
						pstmt.setString(pstmtcount++, rsval.get(i).get("cm_project"));
			        	pstmt.setString(pstmtcount++, rsval.get(i).get("cm_userid"));
			        	ecamsLogger.error(((LoggableStatement)pstmt).getQueryString());
			        	pstmt.executeUpdate();
			        	pstmt.close();
					}
				}
        	}
			pstmt = null;
			rs = null;
			
			return retMsg;			
			
		} catch (SQLException sqlexception) {
			sqlexception.printStackTrace();
			ecamsLogger.error("## SRRestApi.insertCMC0401() SQLException START ##");
			ecamsLogger.error("## Error DESC : ", sqlexception);	
			ecamsLogger.error("## SRRestApi.insertCMC0401() SQLException END ##");			
			throw sqlexception;
		} catch (Exception exception) {
			exception.printStackTrace();
			ecamsLogger.error("## SRRestApi.insertCMC0401() Exception START ##");				
			ecamsLogger.error("## Error DESC : ", exception);	
			ecamsLogger.error("## SRRestApi.insertCMC0401() Exception END ##");				
			throw exception;
		}finally{
			if (strQuery != null) 	strQuery = null;
			if (rs != null)     try{rs.close();}catch (Exception ex){ex.printStackTrace();}
			if (pstmt != null)  try{pstmt.close();}catch (Exception ex2){ex2.printStackTrace();}
		}
	}//end of insertCMC0401() method statement

    public String insertSROrder(String strECAMSID,ArrayList<HashMap<String, String>> rsval,Connection conn) throws SQLException, Exception{
		Connection        connD       = null;
		PreparedStatement pstmt       = null;
		PreparedStatement pstmt2      = null;
		ResultSet         rs          = null;
		ResultSet         rs2         = null;
		StringBuffer      strQuery    = new StringBuffer();
		int				  pstmtcount  = 1;
		String            retMsg      = "";

    	int      i = 0;
		String   strSUBID      = "";
		String   strORDERID  = "";
		String   strOrderSubId = "";
		String   strDetailSayu = "";
		String   strDocSub   = "";
		String   strDocNum   = "";

		boolean  findSw = false;
		boolean  selfSw = false;
		
		
		ConnectionContext connectionContextD = new ConnectionResource(false,"D");
		try {

			connD = connectionContextD.getConnection();
			
			for (i=0;rsval.size()>i;i++) {
				if ("main".equals(rsval.get(i).get("gbn"))) {
					
					//if("자체발행".equals(rsval.get(i).get("CC_DOCNUM"))) selfSw = true;
					//else selfSw = false;
					
					//업무지시서					
					strQuery.setLength(0);                		
		    		strQuery.append("select lpad(to_number(max(substr(cc_orderid,-4)))+ 1,4,'0') as max	 	\n");               		
		    		strQuery.append("  from cmc0420 														\n"); 
		    		strQuery.append(" where cc_orderid like ?										 		\n");
		    		pstmt = conn.prepareStatement(strQuery.toString());
		    		//pstmt = new LoggableStatement(conn,strQuery.toString());
		    		pstmt.setString(1, rsval.get(i).get("CC_ORDERID")+"%");
		    		//ecamsLogger.error(((LoggableStatement)pstmt).getQueryString());
			        rs = pstmt.executeQuery();
			        if (rs.next()) {
			        	if (rs.getString("max") == null || rs.getString("max") == "") {
			        		strORDERID = rsval.get(i).get("CC_ORDERID")+"-"+"0001";
			        	} 
			        	else {
			        		strORDERID = rsval.get(i).get("CC_ORDERID")+"-"+rs.getString("max");
			        	}
			        }
			        rs.close();
			        pstmt.close();
			        
			        //수신부서
			        strQuery.setLength(0);                		
		    		strQuery.append("select cc_subid	 													\n");               		
		    		strQuery.append("  from cmc0410 														\n"); 
		    		strQuery.append(" where cc_reqid = ?									 				\n");
		    		strQuery.append(" and cc_team = (select cm_project from cmm0040 where cm_userid = ?)	\n");
		    		pstmt = conn.prepareStatement(strQuery.toString());
		    		//pstmt = new LoggableStatement(conn,strQuery.toString());
		    		pstmtcount=1;
		    		pstmt.setString(pstmtcount++, strECAMSID);
		    		pstmt.setString(pstmtcount++, rsval.get(i).get("CC_ORDERUSER"));
		    		//ecamsLogger.error(((LoggableStatement)pstmt).getQueryString());
			        rs = pstmt.executeQuery();
			        if (rs.next()) {
			        	strSUBID=rs.getString("cc_subid");
			        }
			        rs.close();
			        pstmt.close();
			        

			        //상세제목 등
			        strQuery.setLength(0);                		
		    		strQuery.append("select CC_DOCSUBJ,CC_DETAILSAYU  \n");                 		
		    		strQuery.append("  from cmc0400 				  \n"); 
		    		strQuery.append(" where cc_reqid = ?			  \n");
		    		pstmt = conn.prepareStatement(strQuery.toString());
		    		//pstmt = new LoggableStatement(conn,strQuery.toString());
		    		pstmtcount=1;
		    		pstmt.setString(pstmtcount++, strECAMSID);
		    		//ecamsLogger.error(((LoggableStatement)pstmt).getQueryString());
			        rs = pstmt.executeQuery();
			        if (rs.next()) {
			        	strDocSub=rs.getString("CC_DOCSUBJ");
			        	strDetailSayu = rs.getString("CC_DETAILSAYU");
			        }
			        rs.close();
			        pstmt.close();
			     
					strQuery.setLength(0);
		        	strQuery.append("insert into cmc0420				                              				\n");
		        	strQuery.append("    (CC_ORDERID,CC_REQID,CC_SUBID,CC_DOCNUM,CC_ENDPLAN,	 					\n");
		        	strQuery.append("     CC_REQSUB,CC_DETAILSAYU,										 			\n");
		        	strQuery.append("     CC_STATUS,CC_STARTDT,CC_ORDERUSER) 										\n");
		        	strQuery.append("     values																	\n");
		        	strQuery.append("(?,?,?,?,?,    ?,?,    '1',sysdate,?)											\n");
		        	
		        	pstmt = conn.prepareStatement(strQuery.toString());
					pstmt = new LoggableStatement(conn,strQuery.toString());
					pstmtcount = 1;
		        	pstmt.setString(pstmtcount++, strORDERID);
		        	pstmt.setString(pstmtcount++, strECAMSID);
		        	pstmt.setString(pstmtcount++, strSUBID);
		        	pstmt.setString(pstmtcount++, rsval.get(i).get("CC_DOCNUM"));
		        	pstmt.setString(pstmtcount++, rsval.get(i).get("CC_ENDPLAN"));
		        	/*
		        	pstmt.setString(pstmtcount++, rsval.get(i).get("CC_REQSUB"));
		        	*/
		        	pstmt.setString(pstmtcount++, strDocSub);
		        	//pstmt.setString(pstmtcount++, strDocSub);  //20241010
		        	pstmt.setString(pstmtcount++, strDetailSayu);

		        	pstmt.setString(pstmtcount++, rsval.get(i).get("CC_ORDERUSER"));
		        	ecamsLogger.error(((LoggableStatement)pstmt).getQueryString());
		        	pstmt.executeUpdate();
		        	pstmt.close();
		        	
				    selfSw = false;
				    //상세제목 등
				    strQuery.setLength(0);                		
					strQuery.append("select decode(CC_DOCNUM,'자체발행','Y','N') selfsw             \n"); 
					strQuery.append("  from cmc0420 											  \n"); 
					strQuery.append(" where CC_ORDERID = ?									 	  \n");
					pstmt = conn.prepareStatement(strQuery.toString());
					//pstmt = new LoggableStatement(conn,strQuery.toString());
					pstmtcount=1;
		        	pstmt.setString(pstmtcount++, strORDERID);
					//ecamsLogger.error(((LoggableStatement)pstmt).getQueryString());
				    rs = pstmt.executeQuery();
				    if (rs.next()) {
						ecamsLogger.error("20241029 selefsw Apply strORDERID > " + strORDERID + ", selfsw > "   + rs.getString("selfsw"));
				    	if ("Y".equals(rs.getString("selfsw"))) selfSw = true;
				    }
				    rs.close();
				    pstmt.close();
		        	
		        	//자체발행인 경우 개발요청서/주관부서/수신부서 삭제 로직 추가 20241029
		        	 if(selfSw){
		        		strQuery.setLength(0);
			        	strQuery.append("UPDATE CMC0420     		\n");
			        	strQuery.append("   SET CC_REQID=''  	    \n");
			        	strQuery.append("      ,CC_SUBID=''	        \n");
			        	strQuery.append(" where CC_ORDERID=?	    \n");
			        	pstmt = conn.prepareStatement(strQuery.toString());
						pstmt = new LoggableStatement(conn,strQuery.toString());
						pstmtcount = 1;
						pstmt.setString(pstmtcount++, strORDERID);
			        	ecamsLogger.error(((LoggableStatement)pstmt).getQueryString());
			        	pstmt.executeUpdate();
			        	pstmt.close();
		        		 
		        		 
		        		strQuery.setLength(0);
			        	strQuery.append("delete cmc0410   		\n");
			        	strQuery.append(" where CC_REQID=?	    \n");
			        	pstmt = conn.prepareStatement(strQuery.toString());
						pstmt = new LoggableStatement(conn,strQuery.toString());
						pstmtcount = 1;
						pstmt.setString(pstmtcount++, strECAMSID);
			        	ecamsLogger.error(((LoggableStatement)pstmt).getQueryString());
			        	pstmt.executeUpdate();
			        	pstmt.close();
			        	
			        	strQuery.setLength(0);
			        	strQuery.append("delete cmc0401   		\n");
			        	strQuery.append(" where CC_REQID=?	    \n");
			        	pstmt = conn.prepareStatement(strQuery.toString());
						pstmt = new LoggableStatement(conn,strQuery.toString());
						pstmtcount = 1;
						pstmt.setString(pstmtcount++, strECAMSID);
			        	ecamsLogger.error(((LoggableStatement)pstmt).getQueryString());
			        	pstmt.executeUpdate();
			        	pstmt.close();
			        	
			        	strQuery.setLength(0);
			        	strQuery.append("delete cmc0400   		\n");
			        	strQuery.append(" where CC_REQID=?	    \n");
			        	pstmt = conn.prepareStatement(strQuery.toString());
						pstmt = new LoggableStatement(conn,strQuery.toString());
						pstmtcount = 1;
						pstmt.setString(pstmtcount++, strECAMSID);
			        	ecamsLogger.error(((LoggableStatement)pstmt).getQueryString());
			        	pstmt.executeUpdate();
			        	pstmt.close();
		        	}
		        	
				}
				
			}

			for (i=0;rsval.size()>i;i++) {
				//담당자
				if ("dev".equals(rsval.get(i).get("gbn"))) {
					findSw = false;
					if (rsval.get(i).get("Status") != null && !"".equals(rsval.get(i).get("Status")) && "0".equals(rsval.get(i).get("Status"))) {
						strQuery.setLength(0);
			        	strQuery.append("delete cmc0450   		\n");
			        	strQuery.append(" where CC_ORDERID=?	\n");
			        	strQuery.append("   and CC_RUNNER=?	    \n");			        	
			        	pstmt = conn.prepareStatement(strQuery.toString());
						pstmt = new LoggableStatement(conn,strQuery.toString());
						pstmtcount = 1;
						pstmt.setString(pstmtcount++, strORDERID);
			        	pstmt.setString(pstmtcount++, rsval.get(i).get("cm_userid"));
			        	ecamsLogger.error(((LoggableStatement)pstmt).getQueryString());
			        	pstmt.executeUpdate();
			        	pstmt.close();
					} else {
						strQuery.setLength(0);
			        	strQuery.append("select count(*) cnt	\n");
			        	strQuery.append("   from cmc0450		\n");
			        	strQuery.append(" where CC_ORDERID=?	\n");
			        	strQuery.append("   and CC_RUNNER=?	    \n");			        	
			        	pstmt = conn.prepareStatement(strQuery.toString());
						pstmt = new LoggableStatement(conn,strQuery.toString());
						pstmtcount = 1;
						pstmt.setString(pstmtcount++, strORDERID);
			        	pstmt.setString(pstmtcount++, rsval.get(i).get("cm_userid"));
			        	ecamsLogger.error(((LoggableStatement)pstmt).getQueryString());
			        	rs = pstmt.executeQuery();
			        	if (rs.next()) {
			        		if (rs.getInt("cnt")>0) findSw = true;
			        		
			        	} 
			        	rs.close();
			        	pstmt.close();
			        	
			        	if (findSw) {
			        		strQuery.setLength(0);
				        	strQuery.append("delete cmc0450   		\n");
				        	strQuery.append(" where CC_ORDERID=?	\n");
				        	strQuery.append("   and CC_RUNNER=?	    \n");			        	
				        	pstmt = conn.prepareStatement(strQuery.toString());
							pstmt = new LoggableStatement(conn,strQuery.toString());
							pstmtcount = 1;
							pstmt.setString(pstmtcount++, strORDERID);
				        	pstmt.setString(pstmtcount++, rsval.get(i).get("cm_userid"));
				        	ecamsLogger.error(((LoggableStatement)pstmt).getQueryString());
				        	pstmt.executeUpdate();
				        	pstmt.close();
			        	}
						strOrderSubId = "";
						strQuery.setLength(0);                		
		  	    		strQuery.append("select lpad(to_number(max(CC_ORDERSUBID))+ 1,2,'0') as max				\n");               		
		  	    		strQuery.append("  from cmc0450 														\n"); 
		  	    		strQuery.append(" where CC_ORDERID = ?									 				\n");
		  	    		pstmt = conn.prepareStatement(strQuery.toString());
		  	    		pstmt = new LoggableStatement(conn,strQuery.toString());
		  	    		pstmtcount=1;
		  	    		pstmt.setString(pstmtcount++, strORDERID);
		  	    		ecamsLogger.error(((LoggableStatement)pstmt).getQueryString());
		  		        rs = pstmt.executeQuery();
		  		        if (rs.next()) {
		  		        	if (rs.getString("max") == null) {
		  		        		strOrderSubId = "01";
		    	        	} 
		    	        	else {
		    	        		strOrderSubId = rs.getString("max");
		    	        	}
		  		        }
		  		        
		  		        rs.close();
		  		        pstmt.close();
		        		
		        		
		        		strQuery.setLength(0);
				    	strQuery.append("insert into cmc0450 \n");
				    	strQuery.append(" (CC_ORDERID, CC_ORDERSUBID, CC_RUNNER, CC_TEAM) \n");
				    	strQuery.append(" values \n");
				    	strQuery.append(" ( ?,?,?,? ) \n");
				    	 
				    	pstmt = conn.prepareStatement(strQuery.toString());
				    	pstmt = new LoggableStatement(conn,strQuery.toString());
				    	pstmtcount = 1;
				    	pstmt.setString(pstmtcount++, strORDERID);
				    	pstmt.setString(pstmtcount++, strOrderSubId);
				    	pstmt.setString(pstmtcount++, rsval.get(i).get("cm_userid"));
				    	pstmt.setString(pstmtcount++, rsval.get(i).get("cm_project"));
				    	
				    	ecamsLogger.error(((LoggableStatement)pstmt).getQueryString());
				        pstmt.executeUpdate();
				        pstmt.close();
					}
				}
			}
			


			for (i=0;rsval.size()>i;i++) {
				//제3자
				if ("third".equals(rsval.get(i).get("gbn"))) {
					findSw = false;
					if (rsval.get(i).get("Status") != null && !"".equals(rsval.get(i).get("Status")) && "0".equals(rsval.get(i).get("Status"))) {
						strQuery.setLength(0);
			        	strQuery.append("delete cmc0421   		\n");
			        	strQuery.append(" where CC_ORDERID=?	\n");
			        	strQuery.append("   and CC_THIRDUSER=?	\n");			        	
			        	pstmt = conn.prepareStatement(strQuery.toString());
						pstmt = new LoggableStatement(conn,strQuery.toString());
						pstmtcount = 1;
						pstmt.setString(pstmtcount++, strORDERID);
			        	pstmt.setString(pstmtcount++, rsval.get(i).get("cm_userid"));
			        	ecamsLogger.error(((LoggableStatement)pstmt).getQueryString());
			        	pstmt.executeUpdate();
			        	pstmt.close();
					} else {
						strQuery.setLength(0);
			        	strQuery.append("select count(*) cnt	\n");
			        	strQuery.append("   from cmc0421		\n");
			        	strQuery.append(" where CC_ORDERID=?	\n");
			        	strQuery.append("   and CC_THIRDUSER=?	\n");			        	
			        	pstmt = conn.prepareStatement(strQuery.toString());
						pstmt = new LoggableStatement(conn,strQuery.toString());
						pstmtcount = 1;
						pstmt.setString(pstmtcount++, strORDERID);
			        	pstmt.setString(pstmtcount++, rsval.get(i).get("cm_userid"));
			        	ecamsLogger.error(((LoggableStatement)pstmt).getQueryString());
			        	rs = pstmt.executeQuery();
			        	if (rs.next()) {
			        		if (rs.getInt("cnt")>0) findSw = true;
			        	} 
			        	rs.close();
			        	pstmt.close();
			        	
			        	if (findSw) {
							strQuery.setLength(0);
				        	strQuery.append("delete cmc0421   		\n");
				        	strQuery.append(" where CC_ORDERID=?	\n");
				        	strQuery.append("   and CC_THIRDUSER=?	\n");			        	
				        	pstmt = conn.prepareStatement(strQuery.toString());
							pstmt = new LoggableStatement(conn,strQuery.toString());
							pstmtcount = 1;
							pstmt.setString(pstmtcount++, strORDERID);
				        	pstmt.setString(pstmtcount++, rsval.get(i).get("cm_userid"));
				        	ecamsLogger.error(((LoggableStatement)pstmt).getQueryString());
				        	pstmt.executeUpdate();
				        	pstmt.close();
			        	}
						strQuery.setLength(0);
				    	strQuery.append("insert into cmc0421 \n");
				    	strQuery.append(" (CC_ORDERID, CC_THIRDUSER, CC_TEAM, CC_CHECK) \n");
				    	strQuery.append(" values \n");
				    	strQuery.append(" ( ?,?,?,'1' ) \n");
				    	 
				    	pstmt = conn.prepareStatement(strQuery.toString());
				    	pstmt = new LoggableStatement(conn,strQuery.toString());
				    	pstmtcount = 1;
				    	pstmt.setString(pstmtcount++, strORDERID);
				    	pstmt.setString(pstmtcount++, rsval.get(i).get("cm_userid"));
				    	pstmt.setString(pstmtcount++, rsval.get(i).get("cm_project"));
				    	
				    	ecamsLogger.error(((LoggableStatement)pstmt).getQueryString());
				        pstmt.executeUpdate();
				        pstmt.close();
					}
				}
			}
			
			strQuery.setLength(0);
			strQuery.append("update cmc0400 set cc_status='1'      \n");
			strQuery.append(" where CC_REQID=?                     \n");
			pstmt = conn.prepareStatement(strQuery.toString());
			//pstmt = new LoggableStatement(conn,strQuery.toString());
			pstmt.setString(1, strECAMSID);
			//ecamsLogger.error(((LoggableStatement)pstmt).getQueryString());
			pstmt.executeUpdate();
			pstmt.close();
			

			strQuery.setLength(0);
			strQuery.append("update cmc0410 set cc_status='1'      \n");
			strQuery.append(" where CC_REQID=?                     \n");
			pstmt = conn.prepareStatement(strQuery.toString());
			pstmt = new LoggableStatement(conn,strQuery.toString());
			pstmt.setString(1, strECAMSID);
			ecamsLogger.error(((LoggableStatement)pstmt).getQueryString());
			pstmt.executeUpdate();
			pstmt.close();
			
			for (i=0;rsval.size()>i;i++) {
				//담당자
				if ("dev".equals(rsval.get(i).get("gbn"))) {
					String UserIp = "";
					String url = "";
					
					strQuery.setLength(0);                		
		    		strQuery.append("select nvl(a.cm_ipaddress,'127.0.0.1') cm_ipaddress, b.cm_url		\n");               		
		    		strQuery.append("  from cmm0040 a, cmm0010 b 			\n"); 
		    		strQuery.append(" where a.cm_userid = ?					\n");
		    		strQuery.append("   and b.cm_stno = 'ECAMS'				\n");
		    		pstmt = conn.prepareStatement(strQuery.toString());
		    		//pstmt = new LoggableStatement(conn,strQuery.toString());
		    		pstmtcount=1;
		    		pstmt.setString(pstmtcount++, rsval.get(i).get("cm_userid"));
		    		//ecamsLogger.error(((LoggableStatement)pstmt).getQueryString());
			        rs = pstmt.executeQuery();
			        if (rs.next()) {
			        	UserIp = rs.getString("cm_ipaddress");
			        	url = rs.getString("cm_url");
			        }
			        rs.close();
			        pstmt.close();
				
				
					String Makegap="";
					Makegap="작업시지제목 : " + rsval.get(i).get("CC_REQSUB")+"\n"+ "에 대한 작업지시서가 발행되었습니다. 형상관리시스템에 접속하여 확인하여 주시기 바랍니다.";
					
			        strQuery.setLength(0);        	
		        	strQuery.append("Begin CMR9920_STR ( ");
		        	//strQuery.append("?, ?, ?, '업무지시서발행통보', ?, 'http://scm/jbbank.co.kr:8080'); End;");
		        	strQuery.append("?, ?, ?, '업무지시서발행통보', ?, ?); End;");
		        	pstmt = conn.prepareStatement(strQuery.toString());
		        	pstmtcount = 1;
		        	pstmt.setString(pstmtcount++, rsval.get(i).get("cm_username"));
		        	pstmt.setString(pstmtcount++, rsval.get(i).get("cm_username"));
		        	pstmt.setString(pstmtcount++, UserIp);
		        	pstmt.setString(pstmtcount++, Makegap);
		        	pstmt.setString(pstmtcount++, url);
		        	
		        	pstmt.executeUpdate();
		        	pstmt.close();
				}
			}
        	
			connD.setAutoCommit(false);
			for (i=0; i < rsval.size(); i++){
				if ("dev".equals(rsval.get(i).get("gbn"))) {
		        	strQuery.setLength(0);
		        	strQuery.append("SELECT  A.CC_REQID                                                                                                         AS REQNO           		\n");
		        	strQuery.append("      , A.CC_ORDERID                                                                                                       AS ISSUENO         		\n");
		        	strQuery.append("      , A.CC_REQSUB                                                                                                        AS REQNM           		\n");
		        	strQuery.append("      , A.CC_DETAILSAYU                                                                                                    AS REQCTNT         		\n");
		        	strQuery.append("      , A.CC_ORDERUSER                                                                                                     AS ISSUEUSR        		\n");
		        	strQuery.append("      , A.CC_DOCNUM                                                                                                        AS ISSUECAUSE      		\n");
		        	strQuery.append("      , A.CC_ENDPLAN                                                                                                       AS DUEDATE         		\n");
		        	strQuery.append("      , B.CC_RUNNER                                                                                                        AS ACCOUNTUSR      		\n");
		        	strQuery.append("      , (SELECT CC_THIRDUSER FROM CMC0421 WHERE CC_ORDERID = A.CC_ORDERID AND ROWNUM = 1)                                  AS THIRDACCOUNTUSR 		\n");
		        	strQuery.append("      , (SELECT CM_CODENAME  FROM CMM0020 WHERE CM_MACODE = 'DOCTYPE' AND CM_MICODE = C.CC_DOCTYPE)                        AS DOCTYPE         		\n");
		        	strQuery.append("      , C.CC_DOCNUM                                                                                                        AS DOCNO           		\n");
		        	strQuery.append("      , (SELECT CM_DEPTNAME FROM CMM0100 WHERE CM_DEPTCD = C.CC_DEPT1)                                                     AS INSUB           		\n");
		        	strQuery.append("      , (SELECT CM_USERNAME FROM CMM0040 WHERE CM_USERID = C.CC_REQUSER1)                                                  AS INSUBUSR        		\n");
		        	strQuery.append("      , (SELECT CM_DEPTNAME FROM CMC0401 A, CMM0100 WHERE CC_REQID = C.CC_REQID AND CC_DEPT3 = CM_DEPTCD AND ROWNUM = 1)   AS CHARGETEAM      		\n");
		        	strQuery.append("      , CC_DETAILJOBN                                                                                                      AS BIZDETAILNM     		\n");
		        	strQuery.append("      , (SELECT CM_CODENAME  FROM CMM0020 WHERE CM_MACODE = 'CONTYPE' AND CM_MICODE = C.CC_ACTTYPE)                        AS ACTTYPE         		\n");
		        	strQuery.append("      , (SELECT CM_CODENAME  FROM CMM0020 WHERE CM_MACODE = 'JOBGRADE' AND CM_MICODE = C.CC_JOBDIF)                        AS BIZDIFFICULTY   		\n");
		        	strQuery.append("      , DECODE(C.CC_DEVSTDT, NULL, NULL, C.CC_DEVSTDT || '~' || C.CC_DEVEDDT)                                              AS DEVPERIOD       		\n");
		        	strQuery.append("      , C.CC_DOCSUBJ                                                                                                       AS DOCNM           		\n");
		        	strQuery.append("      , (SELECT CM_CODENAME  FROM CMM0020 WHERE CM_MACODE = 'FOREIGN' AND CM_MICODE = C.CC_DEPT2)                          AS EXORG           		\n");
		        	strQuery.append("      , C.CC_REQUSER2                                                                                                      AS EXORGUSR        		\n");
		        	strQuery.append("      , C.CC_DOCNUM2                                                                                                       AS CHARGETEAMDOCNO 		\n");
		        	strQuery.append("      , (SELECT CM_CODENAME  FROM CMM0020 WHERE CM_MACODE = 'REQTYPE' AND CM_MICODE = C.CC_REQTYPE)                        AS REQTYPE         		\n");
		        	strQuery.append("      , C.CC_ENDPLAN                                                                                                       AS DUEPERIOD       		\n");
		        	strQuery.append("      , (SELECT CM_DEPTNAME FROM CMC0410 A, CMM0100 B WHERE CC_REQID = C.CC_REQID AND CC_TEAM = CM_DEPTCD AND ROWNUM = 1)  AS BIZTEAM         		\n");
		        	strQuery.append("      , C.CC_DETAILSAYU                                                                                                    AS ADDEDBIZCTNT    		\n");
		        	strQuery.append("      , (SELECT CM_DEPTNAME FROM CMM0100 WHERE CM_DEPTCD = B.CC_TEAM)                                                      AS BIZNM           		\n");
		        	strQuery.append("      , A.CC_ORDERUSER                                                                                                     AS CREATER         		\n");
		        	strQuery.append("      , A.CC_STARTDT                                                                                                       AS CREATETIME      		\n");
		        	strQuery.append("  FROM  CMC0420 A                                                                                                                             		\n");
		        	strQuery.append("      , CMC0450 B                                                                                                                             		\n");
		        	strQuery.append("      , CMC0400 C                                                                                                                             		\n");
		        	strQuery.append(" WHERE  A.CC_ORDERID = B.CC_ORDERID                                                                                                           		\n");
		        	strQuery.append("   AND  A.CC_REQID = C.CC_REQID(+)                                                                                                            		\n");
		        	strQuery.append("   AND  A.CC_ORDERID = ?                                                                                                							\n");        	
		        	strQuery.append("   AND  B.CC_RUNNER  = ?                                                                                                							\n");        	
			    	pstmt = conn.prepareStatement(strQuery.toString());
			    	//pstmt = new LoggableStatement(conn,strQuery.toString());
			    	pstmtcount = 1;
			    	int nCnt = 0;
			    	pstmt.setString(pstmtcount++, strORDERID);
			    	pstmt.setString(pstmtcount++, rsval.get(i).get("cm_userid"));		    	
			    	//ecamsLogger.error(((LoggableStatement)pstmt).getQueryString());
			        rs = pstmt.executeQuery();
			        if (rs.next()) {
				        strQuery.setLength(0);                		
			    		strQuery.append("select count(*) as cnt	 	\n");               		
			    		strQuery.append("  from ECAMS_ALM_REQ 		\n"); 
			    		strQuery.append(" where ISSUENO    = ?		\n"); 
			    		strQuery.append("   and ACCOUNTUSR = ?		\n");
			    		pstmt2 = connD.prepareStatement(strQuery.toString());
			    		//pstmt2 = new LoggableStatement(connD,strQuery.toString());
			    		pstmt2.setString(1, strORDERID);
				    	pstmt2.setString(2, rsval.get(i).get("cm_userid"));
			    		//ecamsLogger.error(((LoggableStatement)pstmt2).getQueryString());
				        rs2 = pstmt2.executeQuery();
				        if (rs2.next()) {
				        	nCnt = rs2.getInt("cnt");
				        }
				        rs2.close();
				        pstmt2.close();
				     
				        if (nCnt == 0) {
			        		strQuery.setLength(0);
					    	strQuery.append("insert into ECAMS_ALM_REQ \n");
					    	strQuery.append("(SEQNO           ,REQNO           ,ISSUENO         ,REQNM           ,REQCTNT         ,ISSUEUSR        ,	\n");
					    	strQuery.append(" ISSUECAUSE      ,DUEDATE         ,ACCOUNTUSR      ,THIRDACCOUNTUSR ,DOCTYPE         ,DOCNO           ,	\n");
					    	strQuery.append(" INSUB           ,INSUBUSR        ,CHARGETEAM      ,BIZDETAILNM     ,ACTTYPE         ,BIZDIFFICULTY   ,	\n");
					    	strQuery.append(" DEVPERIOD       ,DOCNM           ,EXORG           ,EXORGUSR        ,CHARGETEAMDOCNO ,REQTYPE         ,	\n");
					    	strQuery.append(" DUEPERIOD       ,BIZTEAM         ,ADDEDBIZCTNT    ,BIZNM           ,CREATER         ,CREATETIME      )	\n");
					    	strQuery.append(" values \n");
					    	strQuery.append(" (QC_SEQ.NEXTVAL, ?, ?, ?, ?, ?, 		\n");
					    	strQuery.append("               ?, ?, ?, ?, ?, ?, 		\n");
					    	strQuery.append("               ?, ?, ?, ?, ?, ?, 		\n");
					    	strQuery.append("               ?, ?, ?, ?, ?, ?, 		\n");
					    	strQuery.append("               ?, ?, ?, ?, ?, SYSDATE) \n");
					    	 
					    	pstmt2 = connD.prepareStatement(strQuery.toString());
					    	//pstmt2 = new LoggableStatement(connD,strQuery.toString());
					    	pstmtcount = 1;
					    	pstmt2.setString(pstmtcount++, rs.getString("REQNO"));   
					    	pstmt2.setString(pstmtcount++, rs.getString("ISSUENO"));   
					    	pstmt2.setString(pstmtcount++, rs.getString("REQNM"));   
					    	pstmt2.setString(pstmtcount++, rs.getString("REQCTNT"));   
					    	pstmt2.setString(pstmtcount++, rs.getString("ISSUEUSR"));   
					    	pstmt2.setString(pstmtcount++, rs.getString("ISSUECAUSE"));   
					    	pstmt2.setString(pstmtcount++, rs.getString("DUEDATE"));   
					    	pstmt2.setString(pstmtcount++, rs.getString("ACCOUNTUSR"));   
					    	pstmt2.setString(pstmtcount++, rs.getString("THIRDACCOUNTUSR"));   
					    	pstmt2.setString(pstmtcount++, rs.getString("DOCTYPE"));   
					    	pstmt2.setString(pstmtcount++, rs.getString("DOCNO"));   
					    	pstmt2.setString(pstmtcount++, rs.getString("INSUB"));   
					    	pstmt2.setString(pstmtcount++, rs.getString("INSUBUSR"));   
					    	pstmt2.setString(pstmtcount++, rs.getString("CHARGETEAM"));   
					    	pstmt2.setString(pstmtcount++, rs.getString("BIZDETAILNM"));   
					    	pstmt2.setString(pstmtcount++, rs.getString("ACTTYPE"));   
					    	pstmt2.setString(pstmtcount++, rs.getString("BIZDIFFICULTY"));   
					    	pstmt2.setString(pstmtcount++, rs.getString("DEVPERIOD"));   
					    	pstmt2.setString(pstmtcount++, rs.getString("DOCNM"));   
					    	pstmt2.setString(pstmtcount++, rs.getString("EXORG"));   
					    	pstmt2.setString(pstmtcount++, rs.getString("EXORGUSR"));   
					    	pstmt2.setString(pstmtcount++, rs.getString("CHARGETEAMDOCNO"));   
					    	pstmt2.setString(pstmtcount++, rs.getString("REQTYPE"));   
					    	pstmt2.setString(pstmtcount++, rs.getString("DUEPERIOD"));  
					    	pstmt2.setString(pstmtcount++, rs.getString("BIZTEAM")); 
					    	pstmt2.setString(pstmtcount++, rs.getString("ADDEDBIZCTNT")); 
					    	pstmt2.setString(pstmtcount++, rs.getString("BIZNM"));
					    	pstmt2.setString(pstmtcount++, rs.getString("CREATER"));
				    		//ecamsLogger.error(((LoggableStatement)pstmt2).getQueryString());
					    	pstmt2.executeUpdate();			    	
					        pstmt2.close();			        
				        }
			        }
			        rs.close();
			        pstmt.close();
				}
 	        }
			connD.commit();
			connD.close();
			
			pstmt = null;
			rs = null;
			pstmt2 = null;
			rs2 = null;
			connD = null;
			
		} catch (SQLException sqlexception) {
			sqlexception.printStackTrace();
			ecamsLogger.error("## SRRestApi.insertSROrder() SQLException START ##");
			ecamsLogger.error("## Error DESC : ", sqlexception);	
			ecamsLogger.error("## SRRestApi.insertSROrder() SQLException END ##");			
			throw sqlexception;
		} catch (Exception exception) {
			exception.printStackTrace();
			ecamsLogger.error("## SRRestApi.insertSROrder() Exception START ##");				
			ecamsLogger.error("## Error DESC : ", exception);	
			ecamsLogger.error("## SRRestApi.insertSROrder() Exception END ##");				
			throw exception;
		}finally{
			if (strQuery != null) 	strQuery = null;
			if (rs != null)     try{rs.close();}catch (Exception ex){ex.printStackTrace();}
			if (pstmt != null)  try{pstmt.close();}catch (Exception ex2){ex2.printStackTrace();}
			if (rs2 != null)     try{rs.close();}catch (Exception ex){ex.printStackTrace();}
			if (pstmt2 != null)  try{pstmt.close();}catch (Exception ex2){ex2.printStackTrace();}
			if (connD != null){
				try{
					ConnectionResource.release(connD);
				}catch(Exception ex3){
					ecamsLogger.error("## SRRestApi.insertSROrder() connection release exception ##");
					ex3.printStackTrace();
				}
			}
		}
		
		if (retMsg.length()==0) retMsg = "OK"+strORDERID;
		
		return retMsg;
	}//end of insertSROrder() method statement

    public String updateSROrder(ArrayList<HashMap<String, String>> rsval,Connection conn) throws SQLException, Exception{
		Connection        connD       = null;
		PreparedStatement pstmt       = null;
		PreparedStatement pstmt2      = null;
		ResultSet         rs          = null;
		ResultSet         rs2         = null;
		StringBuffer      strQuery    = new StringBuffer();
		int				  pstmtcount  = 1;
		String            retMsg      = "";

    	int      i = 0;
    	int      cnt = 0;
		String   strORDERID  = "";
		String   strOrderSubId = "";
		boolean  findSw = false;
		ConnectionContext connectionContextD = new ConnectionResource(false,"D");
		try {

			connD = connectionContextD.getConnection();
        	
        	
        	for (i=0; i<rsval.size(); i++){
        		if ("third".equals(rsval.get(i).get("gbn"))) {
        			if (cnt == 0) {
        	    		strORDERID=rsval.get(i).get("CC_ORDERID");
        			}
        			++cnt;
					findSw = false;
					if (rsval.get(i).get("Status") != null && !"".equals(rsval.get(i).get("Status")) && "00".equals(rsval.get(i).get("Status"))) {
						strQuery.setLength(0);
			        	strQuery.append("delete cmc0421   		\n");
			        	strQuery.append(" where CC_ORDERID=?	\n");
			        	strQuery.append("   and CC_THIRDUSER=?	\n");			        	
			        	pstmt = conn.prepareStatement(strQuery.toString());
						//pstmt = new LoggableStatement(conn,strQuery.toString());
						pstmtcount = 1;
						pstmt.setString(pstmtcount++, strORDERID);
			        	pstmt.setString(pstmtcount++, rsval.get(i).get("cm_userid"));
			        	//ecamsLogger.error(((LoggableStatement)pstmt).getQueryString());
			        	pstmt.executeUpdate();
			        	pstmt.close();
					} else {
						strQuery.setLength(0);
			        	strQuery.append("select count(*) cnt	\n");
			        	strQuery.append("   from cmc0421		\n");
			        	strQuery.append(" where CC_ORDERID=?	\n");
			        	strQuery.append("   and CC_THIRDUSER=?	\n");			        	
			        	pstmt = conn.prepareStatement(strQuery.toString());
						//pstmt = new LoggableStatement(conn,strQuery.toString());
						pstmtcount = 1;
						pstmt.setString(pstmtcount++, strORDERID);
			        	pstmt.setString(pstmtcount++, rsval.get(i).get("cm_userid"));
			        	//ecamsLogger.error(((LoggableStatement)pstmt).getQueryString());
			        	rs = pstmt.executeQuery();
			        	if (rs.next()) {
			        		if (rs.getInt("cnt")>0) findSw = true;
			        	} 
			        	rs.close();
			        	pstmt.close();
			        	
			        	if (findSw) {
							strQuery.setLength(0);
				        	strQuery.append("delete cmc0421   		\n");
				        	strQuery.append(" where CC_ORDERID=?	\n");
				        	strQuery.append("   and CC_THIRDUSER=?	\n");			        	
				        	pstmt = conn.prepareStatement(strQuery.toString());
							//pstmt = new LoggableStatement(conn,strQuery.toString());
							pstmtcount = 1;
							pstmt.setString(pstmtcount++, strORDERID);
				        	pstmt.setString(pstmtcount++, rsval.get(i).get("cm_userid"));
				        	//ecamsLogger.error(((LoggableStatement)pstmt).getQueryString());
				        	pstmt.executeUpdate();
				        	pstmt.close();
			        	}
		        		strQuery.setLength(0);
				    	strQuery.append("insert into cmc0421 \n");
				    	strQuery.append(" (CC_ORDERID, CC_THIRDUSER, CC_TEAM, CC_CHECK) \n");
				    	strQuery.append(" values \n");
				    	strQuery.append(" ( ?,?,?,'1' ) \n");
				    	 
				    	pstmt = conn.prepareStatement(strQuery.toString());
				    	pstmt = new LoggableStatement(conn,strQuery.toString());
				    	pstmtcount = 1;
				    	pstmt.setString(pstmtcount++, strORDERID);
				    	pstmt.setString(pstmtcount++, rsval.get(i).get("cm_userid"));
				    	pstmt.setString(pstmtcount++, rsval.get(i).get("cm_project"));
				    	
				    	ecamsLogger.error(((LoggableStatement)pstmt).getQueryString());
				        pstmt.executeUpdate();
				        pstmt.close();
					}
        		}
        	}
    		
    		cnt = 0;
        	for (i=0; i<rsval.size(); i++){
        		if ("dev".equals(rsval.get(i).get("gbn"))) {
        			if (cnt == 0) {
        	    		strORDERID=rsval.get(i).get("CC_ORDERID");
        			}
        			++cnt;
					findSw = false;
					if (rsval.get(i).get("Status") != null && !"".equals(rsval.get(i).get("Status")) && "00".equals(rsval.get(i).get("Status"))) {
						strQuery.setLength(0);
			        	strQuery.append("delete cmc0450   		\n");
			        	strQuery.append(" where CC_ORDERID=?	\n");
			        	strQuery.append("   and CC_RUNNER=?	    \n");			        	
			        	pstmt = conn.prepareStatement(strQuery.toString());
						pstmt = new LoggableStatement(conn,strQuery.toString());
						pstmtcount = 1;
						pstmt.setString(pstmtcount++, strORDERID);
			        	pstmt.setString(pstmtcount++, rsval.get(i).get("cm_userid"));
			        	ecamsLogger.error(((LoggableStatement)pstmt).getQueryString());
			        	pstmt.executeUpdate();
			        	pstmt.close();
					} else {
						strQuery.setLength(0);
			        	strQuery.append("select count(*) cnt	\n");
			        	strQuery.append("   from cmc0450		\n");
			        	strQuery.append(" where CC_ORDERID=?	\n");
			        	strQuery.append("   and CC_RUNNER=?	    \n");			        	
			        	pstmt = conn.prepareStatement(strQuery.toString());
						pstmt = new LoggableStatement(conn,strQuery.toString());
						pstmtcount = 1;
						pstmt.setString(pstmtcount++, strORDERID);
			        	pstmt.setString(pstmtcount++, rsval.get(i).get("cm_userid"));
			        	ecamsLogger.error(((LoggableStatement)pstmt).getQueryString());
			        	rs = pstmt.executeQuery();
			        	if (rs.next()) {
			        		if (rs.getInt("cnt")>0) findSw = true;
			        	} 
			        	rs.close();
			        	pstmt.close();
			        	
			        	if (findSw) {
			        		strQuery.setLength(0);
				        	strQuery.append("delete cmc0450   		\n");
				        	strQuery.append(" where CC_ORDERID=?	\n");
				        	strQuery.append("   and CC_RUNNER=?	    \n");			        	
				        	pstmt = conn.prepareStatement(strQuery.toString());
							pstmt = new LoggableStatement(conn,strQuery.toString());
							pstmtcount = 1;
							pstmt.setString(pstmtcount++, strORDERID);
				        	pstmt.setString(pstmtcount++, rsval.get(i).get("cm_userid"));
				        	ecamsLogger.error(((LoggableStatement)pstmt).getQueryString());
				        	pstmt.executeUpdate();
				        	pstmt.close();
			        	}
	        			strQuery.setLength(0);                		
	      	    		strQuery.append("select lpad(to_number(max(CC_ORDERSUBID))+ 1,2,'0') as max				\n");               		
	      	    		strQuery.append("  from cmc0450 														\n"); 
	      	    		strQuery.append(" where CC_ORDERID = ?									 				\n");
	      	    		pstmt = conn.prepareStatement(strQuery.toString());
	      	    		//pstmt = new LoggableStatement(conn,strQuery.toString());
	      	    		pstmtcount=1;
	      	    		pstmt.setString(pstmtcount++, strORDERID);
	      	    		//ecamsLogger.error(((LoggableStatement)pstmt).getQueryString());
	      		        rs = pstmt.executeQuery();
	      		        if (rs.next()) {
	      		        	if (rs.getString("max") == null) {
	      		        		strOrderSubId = "01";
	        	        	} 
	        	        	else {
	        	        		strOrderSubId = rs.getString("max");
	        	        	}
	      		        }
	      		        rs.close();
	      		        pstmt.close();
            		
	            		strQuery.setLength(0);
	    		    	strQuery.append("insert into cmc0450 \n");
	    		    	strQuery.append(" (CC_ORDERID, CC_ORDERSUBID, CC_RUNNER, CC_TEAM) \n");
	    		    	strQuery.append(" values \n");
	    		    	strQuery.append(" ( ?,?,?,? ) \n");
	    		    	 
	    		    	pstmt = conn.prepareStatement(strQuery.toString());
	    		    	pstmt = new LoggableStatement(conn,strQuery.toString());
	    		    	pstmtcount = 1;
	    		    	//pstmt.setString(pstmtcount++, strORDERID[i]);
	    		    	pstmt.setString(pstmtcount++, strORDERID);
	    		    	pstmt.setString(pstmtcount++, strOrderSubId);
	    		    	pstmt.setString(pstmtcount++, rsval.get(i).get("cm_userid"));
	    		    	pstmt.setString(pstmtcount++, rsval.get(i).get("cm_project"));
	    		    	
	    		    	ecamsLogger.error(((LoggableStatement)pstmt).getQueryString());
	    		        pstmt.executeUpdate();
	    		        pstmt.close();
					}
        		}
        		
        	}
        	
        	for (i=0; i<rsval.size(); i++){
        		if ("dev".equals(rsval.get(i).get("gbn"))) {
        			if (rsval.get(i).get("Status") != null && !"".equals(rsval.get(i).get("Status")) && "00".equals(rsval.get(i).get("Status"))) {
        				strQuery.setLength(0);
				    	strQuery.append("delete ECAMS_ALM_REQ       \n");
			    		strQuery.append(" where ISSUENO    = ?		\n"); 
			    		strQuery.append("   and ACCOUNTUSR = ?		\n");				    	 
				    	pstmt = connD.prepareStatement(strQuery.toString());
				    	//pstmt = new LoggableStatement(connD,strQuery.toString());
			    		pstmt.setString(1, strORDERID);
				    	pstmt.setString(2, rsval.get(i).get("cm_userid"));
			    		//ecamsLogger.error(((LoggableStatement)pstmt).getQueryString());
				    	pstmt.executeUpdate();			    	
				        pstmt.close();			        
        			} else {        			
			        	strQuery.setLength(0);
			        	strQuery.append("SELECT  A.CC_REQID                                                                                                         AS REQNO           		\n");
			        	strQuery.append("      , A.CC_ORDERID                                                                                                       AS ISSUENO         		\n");
			        	strQuery.append("      , A.CC_REQSUB                                                                                                        AS REQNM           		\n");
			        	strQuery.append("      , A.CC_DETAILSAYU                                                                                                    AS REQCTNT         		\n");
			        	strQuery.append("      , A.CC_ORDERUSER                                                                                                     AS ISSUEUSR        		\n");
			        	strQuery.append("      , A.CC_DOCNUM                                                                                                        AS ISSUECAUSE      		\n");
			        	strQuery.append("      , A.CC_ENDPLAN                                                                                                       AS DUEDATE         		\n");
			        	strQuery.append("      , B.CC_RUNNER                                                                                                        AS ACCOUNTUSR      		\n");
			        	strQuery.append("      , (SELECT CC_THIRDUSER FROM CMC0421 WHERE CC_ORDERID = A.CC_ORDERID AND ROWNUM = 1)                                  AS THIRDACCOUNTUSR 		\n");
			        	strQuery.append("      , (SELECT CM_CODENAME  FROM CMM0020 WHERE CM_MACODE = 'DOCTYPE' AND CM_MICODE = C.CC_DOCTYPE)                        AS DOCTYPE         		\n");
			        	strQuery.append("      , C.CC_DOCNUM                                                                                                        AS DOCNO           		\n");
			        	strQuery.append("      , (SELECT CM_DEPTNAME FROM CMM0100 WHERE CM_DEPTCD = C.CC_DEPT1)                                                     AS INSUB           		\n");
			        	strQuery.append("      , (SELECT CM_USERNAME FROM CMM0040 WHERE CM_USERID = C.CC_REQUSER1)                                                  AS INSUBUSR        		\n");
			        	strQuery.append("      , (SELECT CM_DEPTNAME FROM CMC0401 A, CMM0100 WHERE CC_REQID = C.CC_REQID AND CC_DEPT3 = CM_DEPTCD AND ROWNUM = 1)   AS CHARGETEAM      		\n");
			        	strQuery.append("      , CC_DETAILJOBN                                                                                                      AS BIZDETAILNM     		\n");
			        	strQuery.append("      , (SELECT CM_CODENAME  FROM CMM0020 WHERE CM_MACODE = 'CONTYPE' AND CM_MICODE = C.CC_ACTTYPE)                        AS ACTTYPE         		\n");
			        	strQuery.append("      , (SELECT CM_CODENAME  FROM CMM0020 WHERE CM_MACODE = 'JOBGRADE' AND CM_MICODE = C.CC_JOBDIF)                        AS BIZDIFFICULTY   		\n");
			        	strQuery.append("      , DECODE(C.CC_DEVSTDT, NULL, NULL, C.CC_DEVSTDT || '~' || C.CC_DEVEDDT)                                              AS DEVPERIOD       		\n");
			        	strQuery.append("      , C.CC_DOCSUBJ                                                                                                       AS DOCNM           		\n");
			        	strQuery.append("      , (SELECT CM_CODENAME  FROM CMM0020 WHERE CM_MACODE = 'FOREIGN' AND CM_MICODE = C.CC_DEPT2)                          AS EXORG           		\n");
			        	strQuery.append("      , C.CC_REQUSER2                                                                                                      AS EXORGUSR        		\n");
			        	strQuery.append("      , C.CC_DOCNUM2                                                                                                       AS CHARGETEAMDOCNO 		\n");
			        	strQuery.append("      , (SELECT CM_CODENAME  FROM CMM0020 WHERE CM_MACODE = 'REQTYPE' AND CM_MICODE = C.CC_REQTYPE)                        AS REQTYPE         		\n");
			        	strQuery.append("      , C.CC_ENDPLAN                                                                                                       AS DUEPERIOD       		\n");
			        	strQuery.append("      , (SELECT CM_DEPTNAME FROM CMC0410 A, CMM0100 B WHERE CC_REQID = C.CC_REQID AND CC_TEAM = CM_DEPTCD AND ROWNUM = 1)  AS BIZTEAM         		\n");
			        	strQuery.append("      , C.CC_DETAILSAYU                                                                                                    AS ADDEDBIZCTNT    		\n");
			        	strQuery.append("      , (SELECT CM_DEPTNAME FROM CMM0100 WHERE CM_DEPTCD = B.CC_TEAM)                                                      AS BIZNM           		\n");
			        	strQuery.append("      , A.CC_ORDERUSER                                                                                                     AS CREATER         		\n");
			        	strQuery.append("      , A.CC_STARTDT                                                                                                       AS CREATETIME      		\n");
			        	strQuery.append("  FROM  CMC0420 A                                                                                                                             		\n");
			        	strQuery.append("      , CMC0450 B                                                                                                                             		\n");
			        	strQuery.append("      , CMC0400 C                                                                                                                             		\n");
			        	strQuery.append(" WHERE  A.CC_ORDERID = B.CC_ORDERID                                                                                                           		\n");
			        	strQuery.append("   AND  A.CC_REQID = C.CC_REQID(+)                                                                                                            		\n");
			        	strQuery.append("   AND  A.CC_ORDERID = ?                                                                                                							\n");        	
			        	strQuery.append("   AND  B.CC_RUNNER  = ?                                                                                                							\n");        	
				    	pstmt = conn.prepareStatement(strQuery.toString());
				    	//pstmt = new LoggableStatement(conn,strQuery.toString());
				    	pstmtcount = 1;
				    	int nCnt = 0;
				    	pstmt.setString(pstmtcount++, strORDERID);
				    	pstmt.setString(pstmtcount++, rsval.get(i).get("cm_userid"));		    	
				    	//ecamsLogger.error(((LoggableStatement)pstmt).getQueryString());
				        rs = pstmt.executeQuery();
				        if (rs.next()) {
					        strQuery.setLength(0);                		
				    		strQuery.append("select count(*) as cnt	 	\n");               		
				    		strQuery.append("  from ECAMS_ALM_REQ 		\n"); 
				    		strQuery.append(" where ISSUENO    = ?		\n"); 
				    		strQuery.append("   and ACCOUNTUSR = ?		\n");
				    		pstmt2 = connD.prepareStatement(strQuery.toString());
				    		//pstmt2 = new LoggableStatement(connD,strQuery.toString());
				    		pstmt2.setString(1, strORDERID);
					    	pstmt2.setString(2, rsval.get(i).get("cm_userid"));
				    		//ecamsLogger.error(((LoggableStatement)pstmt2).getQueryString());
					        rs2 = pstmt2.executeQuery();
					        if (rs2.next()) {
					        	nCnt = rs2.getInt("cnt");
					        }
					        rs2.close();
					        pstmt2.close();
					     
					        if (nCnt == 0) {
				        		strQuery.setLength(0);
						    	strQuery.append("insert into ECAMS_ALM_REQ \n");
						    	strQuery.append("(SEQNO           ,REQNO           ,ISSUENO         ,REQNM           ,REQCTNT         ,ISSUEUSR        ,	\n");
						    	strQuery.append(" ISSUECAUSE      ,DUEDATE         ,ACCOUNTUSR      ,THIRDACCOUNTUSR ,DOCTYPE         ,DOCNO           ,	\n");
						    	strQuery.append(" INSUB           ,INSUBUSR        ,CHARGETEAM      ,BIZDETAILNM     ,ACTTYPE         ,BIZDIFFICULTY   ,	\n");
						    	strQuery.append(" DEVPERIOD       ,DOCNM           ,EXORG           ,EXORGUSR        ,CHARGETEAMDOCNO ,REQTYPE         ,	\n");
						    	strQuery.append(" DUEPERIOD       ,BIZTEAM         ,ADDEDBIZCTNT    ,BIZNM           ,CREATER         ,CREATETIME      )	\n");
						    	strQuery.append(" values \n");
						    	strQuery.append(" (QC_SEQ.NEXTVAL, ?, ?, ?, ?, ?, 		\n");
						    	strQuery.append("               ?, ?, ?, ?, ?, ?, 		\n");
						    	strQuery.append("               ?, ?, ?, ?, ?, ?, 		\n");
						    	strQuery.append("               ?, ?, ?, ?, ?, ?, 		\n");
						    	strQuery.append("               ?, ?, ?, ?, ?, SYSDATE) \n");
						    	 
						    	pstmt2 = connD.prepareStatement(strQuery.toString());
						    	//pstmt2 = new LoggableStatement(connD,strQuery.toString());
						    	pstmtcount = 1;
						    	pstmt2.setString(pstmtcount++, rs.getString("REQNO"));   
						    	pstmt2.setString(pstmtcount++, rs.getString("ISSUENO"));   
						    	pstmt2.setString(pstmtcount++, rs.getString("REQNM"));   
						    	pstmt2.setString(pstmtcount++, rs.getString("REQCTNT"));   
						    	pstmt2.setString(pstmtcount++, rs.getString("ISSUEUSR"));   
						    	pstmt2.setString(pstmtcount++, rs.getString("ISSUECAUSE"));   
						    	pstmt2.setString(pstmtcount++, rs.getString("DUEDATE"));   
						    	pstmt2.setString(pstmtcount++, rs.getString("ACCOUNTUSR"));   
						    	pstmt2.setString(pstmtcount++, rs.getString("THIRDACCOUNTUSR"));   
						    	pstmt2.setString(pstmtcount++, rs.getString("DOCTYPE"));   
						    	pstmt2.setString(pstmtcount++, rs.getString("DOCNO"));   
						    	pstmt2.setString(pstmtcount++, rs.getString("INSUB"));   
						    	pstmt2.setString(pstmtcount++, rs.getString("INSUBUSR"));   
						    	pstmt2.setString(pstmtcount++, rs.getString("CHARGETEAM"));   
						    	pstmt2.setString(pstmtcount++, rs.getString("BIZDETAILNM"));   
						    	pstmt2.setString(pstmtcount++, rs.getString("ACTTYPE"));   
						    	pstmt2.setString(pstmtcount++, rs.getString("BIZDIFFICULTY"));   
						    	pstmt2.setString(pstmtcount++, rs.getString("DEVPERIOD"));   
						    	pstmt2.setString(pstmtcount++, rs.getString("DOCNM"));   
						    	pstmt2.setString(pstmtcount++, rs.getString("EXORG"));   
						    	pstmt2.setString(pstmtcount++, rs.getString("EXORGUSR"));   
						    	pstmt2.setString(pstmtcount++, rs.getString("CHARGETEAMDOCNO"));   
						    	pstmt2.setString(pstmtcount++, rs.getString("REQTYPE"));   
						    	pstmt2.setString(pstmtcount++, rs.getString("DUEPERIOD"));  
						    	pstmt2.setString(pstmtcount++, rs.getString("BIZTEAM")); 
						    	pstmt2.setString(pstmtcount++, rs.getString("ADDEDBIZCTNT")); 
						    	pstmt2.setString(pstmtcount++, rs.getString("BIZNM"));
						    	pstmt2.setString(pstmtcount++, rs.getString("CREATER"));
					    		//ecamsLogger.error(((LoggableStatement)pstmt2).getQueryString());
						    	pstmt2.executeUpdate();			    	
						        pstmt2.close();			        
					        }
					        else {
				        		strQuery.setLength(0);
						    	strQuery.append("update  ECAMS_ALM_REQ \n");
						    	strQuery.append("set REQNO      = ?, ISSUENO  = ?, REQNM        = ?, REQCTNT         = ?, ISSUEUSR        = ?,                		\n");
						    	strQuery.append("    ISSUECAUSE = ?, DUEDATE  = ?, ACCOUNTUSR   = ?, THIRDACCOUNTUSR = ?, DOCTYPE         = ?, DOCNO         = ?,	\n");
						    	strQuery.append("    INSUB      = ?, INSUBUSR = ?, CHARGETEAM   = ?, BIZDETAILNM     = ?, ACTTYPE         = ?, BIZDIFFICULTY = ?,	\n");
						    	strQuery.append("    DEVPERIOD  = ?, DOCNM    = ?, EXORG        = ?, EXORGUSR        = ?, CHARGETEAMDOCNO = ?, REQTYPE       = ?,	\n");
						    	strQuery.append("    DUEPERIOD  = ?, BIZTEAM  = ?, ADDEDBIZCTNT = ?, BIZNM           = ?, CREATER         = ?         				\n");
					    		strQuery.append(" where ISSUENO    = ?		\n"); 
					    		strQuery.append("   and ACCOUNTUSR = ?		\n");
						    	 
						    	pstmt2 = connD.prepareStatement(strQuery.toString());
						    	//pstmt2 = new LoggableStatement(connD,strQuery.toString());
						    	pstmtcount = 1;
						    	pstmt2.setString(pstmtcount++, rs.getString("REQNO"));   
						    	pstmt2.setString(pstmtcount++, rs.getString("ISSUENO"));   
						    	pstmt2.setString(pstmtcount++, rs.getString("REQNM"));   
						    	pstmt2.setString(pstmtcount++, rs.getString("REQCTNT"));   
						    	pstmt2.setString(pstmtcount++, rs.getString("ISSUEUSR"));   
						    	pstmt2.setString(pstmtcount++, rs.getString("ISSUECAUSE"));   
						    	pstmt2.setString(pstmtcount++, rs.getString("DUEDATE"));   
						    	pstmt2.setString(pstmtcount++, rs.getString("ACCOUNTUSR"));   
						    	pstmt2.setString(pstmtcount++, rs.getString("THIRDACCOUNTUSR"));   
						    	pstmt2.setString(pstmtcount++, rs.getString("DOCTYPE"));   
						    	pstmt2.setString(pstmtcount++, rs.getString("DOCNO"));   
						    	pstmt2.setString(pstmtcount++, rs.getString("INSUB"));   
						    	pstmt2.setString(pstmtcount++, rs.getString("INSUBUSR"));   
						    	pstmt2.setString(pstmtcount++, rs.getString("CHARGETEAM"));   
						    	pstmt2.setString(pstmtcount++, rs.getString("BIZDETAILNM"));   
						    	pstmt2.setString(pstmtcount++, rs.getString("ACTTYPE"));   
						    	pstmt2.setString(pstmtcount++, rs.getString("BIZDIFFICULTY"));   
						    	pstmt2.setString(pstmtcount++, rs.getString("DEVPERIOD"));   
						    	pstmt2.setString(pstmtcount++, rs.getString("DOCNM"));   
						    	pstmt2.setString(pstmtcount++, rs.getString("EXORG"));   
						    	pstmt2.setString(pstmtcount++, rs.getString("EXORGUSR"));   
						    	pstmt2.setString(pstmtcount++, rs.getString("CHARGETEAMDOCNO"));   
						    	pstmt2.setString(pstmtcount++, rs.getString("REQTYPE"));   
						    	pstmt2.setString(pstmtcount++, rs.getString("DUEPERIOD"));  
						    	pstmt2.setString(pstmtcount++, rs.getString("BIZTEAM")); 
						    	pstmt2.setString(pstmtcount++, rs.getString("ADDEDBIZCTNT")); 
						    	pstmt2.setString(pstmtcount++, rs.getString("BIZNM"));
						    	pstmt2.setString(pstmtcount++, rs.getString("CREATER"));
					    		pstmt2.setString(pstmtcount++, strORDERID);
						    	pstmt2.setString(pstmtcount++, rsval.get(i).get("cm_userid"));
					    		//ecamsLogger.error(((LoggableStatement)pstmt2).getQueryString());
						    	pstmt2.executeUpdate();			    	
						        pstmt2.close();		
					        }
				        }
				        rs.close();
				        pstmt.close();
        			}
        		}
 	        }
        	
			connD.commit();
			connD.close();
			
			pstmt = null;
			rs = null;
			pstmt2 = null;
			rs2 = null;
			connD = null;
			
		} catch (SQLException sqlexception) {
			sqlexception.printStackTrace();
			ecamsLogger.error("## SRRestApi.updateSROrder() SQLException START ##");
			ecamsLogger.error("## Error DESC : ", sqlexception);	
			ecamsLogger.error("## SRRestApi.updateSROrder() SQLException END ##");			
			throw sqlexception;
		} catch (Exception exception) {
			exception.printStackTrace();
			ecamsLogger.error("## SRRestApi.updateSROrder() Exception START ##");				
			ecamsLogger.error("## Error DESC : ", exception);	
			ecamsLogger.error("## SRRestApi.updateSROrder() Exception END ##");				
			throw exception;
		}finally{
			if (strQuery != null) 	strQuery = null;
			if (rs != null)     try{rs.close();}catch (Exception ex){ex.printStackTrace();}
			if (pstmt != null)  try{pstmt.close();}catch (Exception ex2){ex2.printStackTrace();}
			if (rs2 != null)     try{rs.close();}catch (Exception ex){ex.printStackTrace();}
			if (pstmt2 != null)  try{pstmt.close();}catch (Exception ex2){ex2.printStackTrace();}
			if (connD != null){
				try{
					ConnectionResource.release(connD);
				}catch(Exception ex3){
					ecamsLogger.error("## SRRestApi.updateSROrder() connection release exception ##");
					ex3.printStackTrace();
				}
			}
		}
		
		if (retMsg.length()==0) retMsg = "OK"+strORDERID;
		
		return retMsg;
	}//end of updateSROrder() method statement

    public String callSRResult(String AcptNo) throws SQLException, Exception{
		Connection        conn        = null;
		PreparedStatement pstmt       = null;
		ResultSet         rs          = null;
		StringBuffer      strQuery    = new StringBuffer();
		ConnectionContext connectionContext = new ConnectionResource();	
		String            retMsg      = "";
		String            convStr     = "";
		String            gitBaseCmd  = "";
    	String 			  shlName	  = "";
    	String 			  wkSrid	  = "";
    	String 			  wkFg   	  = "T";
    	JsonParser jsonparser = new JsonParser();
    	JsonObject jsonObj = new JsonObject();
    	JsonObject jsonObj2 = new JsonObject();
    	String jsonStr = "";
    	JsonArray jsonAry = new JsonArray();
    	
		try {
			conn = connectionContext.getConnection();
			if (conn == null) retMsg = "ERDB00:DB Connection Fail";
			else {

				// 일괄등록 신청 제외
				if (!AcptNo.substring(4,6).equals("16")){
					// 신청건 적합성 체크 로직 추가1
					strQuery.setLength(0);
					strQuery.append("select count(*) cnt                  \n");
					strQuery.append("  from cmr1000                       \n");		
					strQuery.append(" where cr_acptno=?                   \n");
					strQuery.append("   and cr_status='3'                 \n");
					pstmt = conn.prepareStatement(strQuery.toString());	
					//pstmt = new LoggableStatement(conn,strQuery.toString());
					pstmt.setString(1,AcptNo);
					rs = pstmt.executeQuery();
					if (rs.next()){
						if (rs.getInt("cnt")>0) {
							wkFg = "F";  //fail
						}
					}
					rs.close();
					pstmt.close();	

					ecamsLogger.error("SRRestApi AcptNo !!! ["+ AcptNo + "] wkFg [" + wkFg +"] " );
					
					if (!"F".equals(wkFg)){
						String srURL = "";
						strQuery.setLength(0);
						strQuery.append("select  cm_svrip                     \n");
						strQuery.append("  from cmm0015                       \n");		
						strQuery.append(" where cm_gbncd='SR'                 \n");
						pstmt = conn.prepareStatement(strQuery.toString());	
						//pstmt = new LoggableStatement(conn,strQuery.toString());
						rs = pstmt.executeQuery();
						if (rs.next()){
							srURL = "http://" + rs.getString("cm_svrip") +"/srs/ecams/v1/applyResult";
						}
						rs.close();
						pstmt.close();			
						
						jsonObj = new JsonObject();
						strQuery.setLength(0);
						strQuery.append("select to_char(a.cr_acptdate,'yyyy-mm-dd hh24:miss') cr_acptdate,  \n");
						strQuery.append("       to_char(a.cr_prcdate, 'yyyy-mm-dd hh24:miss') cr_prcdate,   \n");
						strQuery.append("       a.cr_editor                   \n");
						strQuery.append("from cmr1000 a                       \n");		
						strQuery.append("where a.cr_acptno = ?                \n");
						pstmt = conn.prepareStatement(strQuery.toString());	
						//pstmt = new LoggableStatement(conn,strQuery.toString());
						pstmt.setString(1,AcptNo);
						rs = pstmt.executeQuery();
						if (rs.next()){
							jsonObj.addProperty("ApplyNo", AcptNo);
							jsonObj.addProperty("ApplyReqDt", rs.getString("cr_acptdate"));
							jsonObj.addProperty("CompleteDt", rs.getString("cr_prcdate"));
							jsonObj.addProperty("EmpNo", rs.getString("cr_editor"));
						}
						rs.close();
						pstmt.close();				
						
						jsonAry = new JsonArray();
						strQuery.setLength(0);
						strQuery.append("select b.cc_reqid,b.CC_ORDERID,  \n");
						strQuery.append("(select cc_srreqid from cmc0400 where cc_reqid = b.cc_reqid) cc_srreqid  \n");				
						strQuery.append("  from cmc0420 b,cmr1012 c                   \n");		
						strQuery.append(" where c.cr_acptno=?                         \n");	
						strQuery.append("   and c.cr_orderid=b.cc_orderid             \n");
						strQuery.append(" group by b.cc_reqid,b.CC_ORDERID \n");
						pstmt = conn.prepareStatement(strQuery.toString());	
						//pstmt = new LoggableStatement(conn,strQuery.toString());
						pstmt.setString(1,AcptNo);
						rs = pstmt.executeQuery();
						while (rs.next()){
							jsonObj2 = new JsonObject();
							
							wkSrid = rs.getString("cc_srreqid");
							/*
							jsonObj2.addProperty("SrDevReqNo", rs.getString("cc_srreqid"));
							jsonObj2.addProperty("RequestDocNo", rs.getString("cc_reqid"));
							jsonObj2.addProperty("OrderDocNo", rs.getString("CC_ORDERID"));
							*/
							
							if (rs.getString("cc_srreqid") != null) {
								jsonObj2.addProperty("SrDevReqNo", rs.getString("cc_srreqid"));
							} else jsonObj2.addProperty("SrDevReqNo", "");
							if (rs.getString("cc_reqid") != null) {
								convStr =  URLEncoder.encode(rs.getString("cc_reqid"),"utf-8");
							    jsonObj2.addProperty("RequestDocNo", convStr);
							} else jsonObj2.addProperty("RequestDocNo", "");
							convStr =  URLEncoder.encode(rs.getString("CC_ORDERID"),"utf-8");
							jsonObj2.addProperty("OrderDocNo", convStr);
							
							
							jsonAry.add(jsonObj2);
						}
						rs.close();
						pstmt.close();
						
						jsonObj.add("DocList", jsonAry);
						
						Gson gson = new Gson();
						jsonStr = gson.toJson(jsonObj);
						
		
						ecamsLogger.error("SRRestApi callSRResult= jsonStr["+ AcptNo +jsonStr.toString());
						ecamsLogger.error("SRRestApi callSRResult= UTF8["+ AcptNo +URLEncoder.encode(jsonStr,"UTF-8") );
						ecamsLogger.error("SRRestApi callSRResult= EUKR["+ AcptNo +URLEncoder.encode(jsonStr,"EUC-KR") );
						
						/*
						HashMap<String,String> etcData = new HashMap<String,String>();
						//etcData.put("remoteURL", srURL+"/srs/v1/applyResult");
						etcData.put("remoteURL", srURL+"/srs/ecams/v1/applyResult");
						etcData.put("reqMethod", "POST");
						etcData.put("sendData", jsonStr);
						
						ecamsLogger.error("SRRestApi callSRResult= ACPTNO[URL >" + srURL+"/srs/ecams/v1/applyResult");
						ecamsLogger.error("SRRestApi callSRResult= ACPTNO["+ AcptNo +jsonStr.toString());
						HttpCall httpcall = new HttpCall();
						retMsg = httpcall.httpCall_common(etcData);
		
						ecamsLogger.error("SRRestApi callSRResult= retMsg["+ retMsg.toString());
						
						if (retMsg.length()==0 || (retMsg.length()>2 && "ER".equals(retMsg.substring(0,2)))) {
							if (retMsg.length()==0) retMsg = "ER:SR호출 실패 ["+etcData.toString()+"]";
						} else {
							jsonObj = (JsonObject) jsonparser.parse(retMsg);
							if (!jsonObj.has("SrDevReqNo")) retMsg = "ER:SR호출 리턴값 invalid ["+retMsg+"]";
							else {
								jsonStr = jsonObj.get("SrDevReqNo").toString().replace("\"", "");
								retMsg = "OK:Success:"+jsonStr;
							}
						}
						*/
						
						//curl -v --request POST 'http://192.168.12.72:18081/srs/ecams/v1/applyResult' --header 'Content-Type: application/json' --data-raw '{"ApplyNo":"202404005927","ApplyReqDt":"2024-08-28 09:2840","CompleteDt":"2024-08-28 09:3628","EmpNo":"219091","DocList":[{"SrDevReqNo":"SR2024-000001SD-0001","RequestDocNo":"TEST","OrderDocNo":"TEST"}]}'
						//gitBaseCmd = "curl -v --request POST 'http://192.168.12.72:18081/srs/ecams/v1/applyResult' --header 'Content-Type: application/json' --data-raw " + "'" + jsonStr.toString() + "'" ; 
						//gitBaseCmd = "curl -v --request POST " + srURL + " --header 'Content-Type: application/json' --data-raw " + "'" + jsonStr.toString() + "'" ;
						
						gitBaseCmd = "curl -v --request POST " + srURL + " --header 'Content-Type: application/json; charset=utf-8' --data-raw " + "'" + jsonStr + "'" ;
						ecamsLogger.error("SRRestApi callSRResult= gitBaseCmd  "+ gitBaseCmd);
		
						shlName = AcptNo + "_" + wkSrid + "gitcmd.sh";
		
						Cmm1600 cmm1600 = new Cmm1600();
						int retCd = cmm1600.execShell_web(shlName, gitBaseCmd, true);
						if (retCd != 0) {
							retMsg = "SRRestApi Command 수행 중 오류가 발생하였습니다.[command="+gitBaseCmd+"] result=["+retCd+"]";
						}
						retMsg = "OK";
						ecamsLogger.error("SRRestApi callSRResult= retCd  "+ retMsg);
					}
				}
			}
			conn.close();
			conn = null;
			pstmt = null;
			rs = null;	
			
		} catch (SQLException sqlexception) {
			sqlexception.printStackTrace();
			ecamsLogger.error("## SRRestApi.setSRClose() SQLException START ##");
			ecamsLogger.error("## Error DESC : ", sqlexception);	
			ecamsLogger.error("## SRRestApi.setSRClose() SQLException END ##");			
			throw sqlexception;
		} catch (Exception exception) {
			exception.printStackTrace();
			ecamsLogger.error("## SRRestApi.setSRClose() Exception START ##");				
			ecamsLogger.error("## Error DESC : ", exception);	
			ecamsLogger.error("## SRRestApi.setSRClose() Exception END ##");				
			throw exception;
		}finally{
			if (strQuery != null) 	strQuery = null;
			if (rs != null)     try{rs.close();}catch (Exception ex){ex.printStackTrace();}
			if (pstmt != null)  try{pstmt.close();}catch (Exception ex2){ex2.printStackTrace();}
			if (conn != null){
				try{
					ConnectionResource.release(conn);
				}catch(Exception ex3){
					ecamsLogger.error("## SRRestApi.setSRClose() connection release exception ##");
					ex3.printStackTrace();
				}
			}
		}
		return retMsg;
	}//end of setSRClose() method statement

    /*
     * 코드정보 (JOB_TEAM JOB_CD CM_MA_CD)  Request_CC_Reg.jsp
     */   
    public String getCodeInfo(String inData) throws SQLException, Exception{
		Connection        conn        = null;
		PreparedStatement pstmt       = null;
		ResultSet         rs          = null;
		StringBuffer      strQuery    = new StringBuffer();
		ArrayList<HashMap<String, String>>  rsval  = new ArrayList<HashMap<String, String>>();
		ConnectionContext connectionContext = new ConnectionResource();	
		String            retMsg      = "";

    	JsonParser jsonparser = new JsonParser();
    	JsonObject jsonObj = new JsonObject();
    	JsonObject jsonObj2 = new JsonObject();
    	JsonArray jsonAry = new JsonArray();
    	String jsonStr = "";
    	
    	CodeInfo codeinfo = new CodeInfo();
    	String GbnCd = "";
    	String MaCode = "0";
    	int i = 0;

		Gson gson = new Gson();
		try {
			conn = connectionContext.getConnection();
			if (conn == null) {
				retMsg = "ERDB00:DB Connection Fail";
			} else {
				retMsg = "";
				//CMC0400 Insert Start!
				jsonObj = (JsonObject) jsonparser.parse(inData);
				if (jsonObj.has("CodeGbn")) {
					ecamsLogger.error("getCodeInfo CodeGbn="+jsonObj.get("CodeGbn").toString().replace("\"", ""));
					GbnCd = jsonObj.get("CodeGbn").toString().replace("\"", "");
					if (jsonObj.has("CmMaCd")) {
						ecamsLogger.error("getCodeInfo CmMaCd="+jsonObj.get("CmMaCd").toString().replace("\"", ""));
						MaCode = jsonObj.get("CmMaCd").toString().replace("\"", "");
					}
				}
				
				if (GbnCd.length()==0 || MaCode.length()==0) {
					retMsg = "ER0501:입력데이타 오류";
				}
				
			}

			if (retMsg.length()==0) {
				strQuery.setLength(0);
				jsonAry = new JsonArray();
				if ("JOB_CD".equals(GbnCd)) {
					rsval = codeinfo.getJobCd_Array("", "N");
					for (i=0;rsval.size()>i;i++) {
						jsonObj2 = new JsonObject();
						jsonObj2.addProperty("CmJobCd", rsval.get(i).get("cm_jobcd"));
						jsonObj2.addProperty("CmJobName", rsval.get(i).get("cm_jobname"));
						jsonObj2.addProperty("CmUseYn", rsval.get(i).get("cm_useyn"));
						jsonAry.add(jsonObj2);
					}
				} else {
					rsval = codeinfo.getCodeInfo_Array(MaCode, "", "N","3");
					for (i=0;rsval.size()>i;i++) {
						jsonObj2 = new JsonObject();
						jsonObj2.addProperty("CmMiCd", rsval.get(i).get("cm_micode"));
						jsonObj2.addProperty("CmCdNm", rsval.get(i).get("cm_codename"));
						jsonObj2.addProperty("CmSeqNo", rsval.get(i).get("cm_seqno"));
						jsonObj2.addProperty("CmUseYn", rsval.get(i).get("cm_useyn"));
						jsonAry.add(jsonObj2);
					}
				}	
				
				jsonObj.add("CodeList", jsonAry);
				jsonStr = gson.toJson(jsonObj);
			} else {
				retMsg = setError(retMsg);
				jsonStr = gson.toJson(retMsg);
			}
			conn.close();
			conn = null;
			pstmt = null;
			rs = null;	
			
		} catch (SQLException sqlexception) {
			sqlexception.printStackTrace();
			ecamsLogger.error("## SRRestApi.getCodeInfo() SQLException START ##");
			ecamsLogger.error("## Error DESC : ", sqlexception);	
			ecamsLogger.error("## SRRestApi.getCodeInfo() SQLException END ##");			
			throw sqlexception;
		} catch (Exception exception) {
			exception.printStackTrace();
			ecamsLogger.error("## SRRestApi.getCodeInfo() Exception START ##");				
			ecamsLogger.error("## Error DESC : ", exception);	
			ecamsLogger.error("## SRRestApi.getCodeInfo() Exception END ##");				
			throw exception;
		}finally{
			if (strQuery != null) 	strQuery = null;
			if (rs != null)     try{rs.close();}catch (Exception ex){ex.printStackTrace();}
			if (pstmt != null)  try{pstmt.close();}catch (Exception ex2){ex2.printStackTrace();}
			if (conn != null){
				try{
					ConnectionResource.release(conn);
				}catch(Exception ex3){
					ecamsLogger.error("## SRRestApi.getCodeInfo() connection release exception ##");
					ex3.printStackTrace();
				}
			}
		}
		return jsonStr;
	}//end of getCodeInfo() method statement
}
