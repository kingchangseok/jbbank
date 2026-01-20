/*****************************************************************************************
	1. program ID	: Cmd010.java
	2. create date	: 2006.08. 08
	3. auth		    : is.choi
	4. update date	: 
	5. auth		    : 
	6. description	: 1. USER INFO.
*****************************************************************************************/

package app.eCmd;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.File;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.apache.logging.log4j.Logger;

import app.common.LoggableStatement;
import app.common.SystemPath;
import app.common.UserInfo;

import com.ecams.common.dbconn.ConnectionContext;
import com.ecams.common.dbconn.ConnectionResource;
import com.ecams.common.logger.EcamsLogger;

import java.util.ArrayList;
import java.util.HashMap;
import app.common.*;
import app.eCmr.Cmr0200;

/**
 * @author bigeyes
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class Cmd0101{     

    /**
     * Logger Class Instance Creation
     * logger
     */
    Logger ecamsLogger  = EcamsLogger.getLoggerInstance();
    
	/**
	 * USER NAME
	 * @param user_id
	 * @return String
	 * @throws SQLException
	 * @throws Exception
	 */


	public Object[] getSysCd(String UserID) throws SQLException, Exception {
		Connection        conn        = null;
		PreparedStatement pstmt       = null;
		PreparedStatement pstmt2       = null;
		ResultSet         rs          = null;
		ResultSet         rs2          = null;
		StringBuffer      strQuery    = new StringBuffer();
		Object[]		  rtObj		  = null;
		ArrayList<HashMap<String, String>>  rtList = new ArrayList<HashMap<String, String>>();
		HashMap<String, String>			  rst		  = null;	
		int     parmCnt = 0;		
		ConnectionContext connectionContext = new ConnectionResource();		
		
		try {	
			conn = connectionContext.getConnection();

			UserInfo     userinfo = new UserInfo();
			boolean       stopSw      = false;
			boolean       chkSw      = false;
			boolean adminYn = userinfo.isAdmin_conn(UserID, conn);
            
			strQuery.setLength(0);
		    strQuery.append("select a.cm_syscd,a.cm_sysmsg,a.cm_sysinfo,\n");
		    strQuery.append("       sign(nvl(a.cm_stopst,to_char(sysdate,'yyyymmddhh24mi')) - to_char(sysdate,'yyyymmddhh24mi')) diff1,\n");
			strQuery.append("       sign(nvl(a.cm_stoped,to_char(sysdate,'yyyymmddhh24mi')) - to_char(sysdate,'yyyymmddhh24mi')) diff2 \n");
		    strQuery.append("  from cmm0030 a,cmm0036 b               \n");
		    strQuery.append(" where a.cm_closedt is null              \n");
		    strQuery.append("   and a.cm_syscd=b.cm_syscd             \n");
		    if (adminYn == false){
		       strQuery.append("and a.cm_syscd in (select distinct cm_syscd \n");
		       strQuery.append("                     from cmm0044           \n");
		       strQuery.append("                    where cm_userid=?       \n");
		       strQuery.append("                      and cm_closedt is null)\n");
		    }
		    strQuery.append("  and b.cm_closedt is null              \n");
		    strQuery.append("  and substr(b.cm_info,27,1)='1'        \n");
		    strQuery.append("group by a.cm_syscd,a.cm_sysmsg,a.cm_sysinfo,a.cm_stopst,a.cm_stoped         \n");			
			//pstmt = new LoggableStatement(conn,strQuery.toString());
            pstmt = conn.prepareStatement(strQuery.toString());	
		    if (adminYn == false){
		    	pstmt.setString(++parmCnt, UserID);
			}
            //ecamsLogger.error(((LoggableStatement)pstmt).getQueryString());           
            rs = pstmt.executeQuery();
            
            rtList.clear();
            String tstInfo = "";
            String tmpSyscd = "";
			while (rs.next()){	
				tstInfo = rs.getString("cm_sysinfo");
				rst = new HashMap<String,String>();
				tmpSyscd = rs.getString("cm_syscd");
				rst.put("cm_syscd", rs.getString("cm_syscd"));
				rst.put("cm_sysmsg", rs.getString("cm_sysmsg"));
				
				stopSw = false;
				if (rs.getString("cm_sysinfo").substring(4,5).equals("1")) {
					if (rs.getInt("diff1")<0 && rs.getInt("diff2")>0 && adminYn == false) {
						rst.put("cm_stopsw", "1");
						tstInfo = tstInfo.substring(0,4) + "1" + tstInfo.substring(5);
						stopSw = true;
					} else {
						rst.put("cm_stopsw", "0");
						tstInfo = tstInfo.substring(0,4) + "0" + tstInfo.substring(5);
					}
				} else{
					rst.put("cm_stopsw", "0");
				}
				if (stopSw == false && adminYn == false) {
					strQuery.setLength(0);
					chkSw = false;
					strQuery.append("select cm_termcd,cm_sttime,cm_edtime,cm_termsub,\n");
					strQuery.append("       to_char(SYSDATE,'d') weekday,            \n");
					strQuery.append("       to_char(SYSDATE,'dd') monday,            \n");
					strQuery.append("       sign(nvl(cm_sttime,to_char(sysdate,'hh24mi')) - to_char(sysdate,'hh24mi')) diff1,\n");
					strQuery.append("       sign(nvl(cm_edtime,to_char(sysdate,'hh24mi')) - to_char(sysdate,'hh24mi')) diff2 \n");
					strQuery.append("  from cmm0330                                 \n");
					strQuery.append(" where cm_syscd=?                              \n");
					pstmt2 = conn.prepareStatement(strQuery.toString());
					//pstmt2 = new LoggableStatement(conn,strQuery.toString());
		            pstmt2.setString(1, tmpSyscd);
		            //ecamsLogger.error(((LoggableStatement)pstmt2).getQueryString());
		            rs2 = pstmt2.executeQuery();
					if (rs2.next()){
						if (rs2.getString("cm_termcd").equals("1")) {        //매일
							chkSw = true;
						} else if (rs2.getString("cm_termcd").equals("2")) { //매주
							if (rs2.getString("weekday").equals(rs2.getString("cm_termsub"))) {
								chkSw = true;
							}
						} else if (rs2.getString("cm_termcd").equals("3")) { //매월
							if (rs2.getString("monday").equals(rs2.getString("cm_termsub"))) {
								chkSw = true;
							}
						} 
						
						if (chkSw == true) {
							//ecamsLogger.debug("++++check++++++++"+tstInfo);
							if (rs2.getInt("diff1")<0 && rs2.getInt("diff2")>0 && adminYn == false) {
								rst.put("cm_stopsw", "1");
								tstInfo = tstInfo.substring(0,4) + "1" +  tstInfo.substring(5);
							}
							//ecamsLogger.debug("++++check2++++++++"+tstInfo);
						}
					} 
					rs2.close();
					pstmt2.close();
				}
				rst.put("cm_sysinfo",tstInfo);
				
				
				rtList.add(rst);
				rst = null;
			}
			rs.close();
			pstmt.close();
			conn.close();
			
			rs = null;
			pstmt = null;
			conn = null;
			
			rtObj =  rtList.toArray();
			rtList = null;
			
			return rtObj;	
			
		} catch (SQLException sqlexception) {
			sqlexception.printStackTrace();
			ecamsLogger.error("## Cmd0101.getSysCd() SQLException START ##");
			ecamsLogger.error("## Error DESC : ", sqlexception);
			ecamsLogger.error("## Cmd0101.getSysCd() SQLException END ##");
			throw sqlexception;
		} catch (Exception exception) {
			exception.printStackTrace();
			ecamsLogger.error("## Cmd0101.getSysCd Exception START ##");
			ecamsLogger.error("## Error DESC : ", exception);
			ecamsLogger.error("## Cmd0101.getSysCd Exception END ##");
			throw exception;
		}finally{
			if (strQuery != null) 	strQuery = null;
			if (rtObj != null)  rtObj = null;
			if (rs != null)     try{rs.close();}catch (Exception ex){ex.printStackTrace();}
			if (pstmt != null)  try{pstmt.close();}catch (Exception ex2){ex2.printStackTrace();}
			if (conn != null){
				try{
					ConnectionResource.release(conn);
				}catch(Exception ex3){
					ecamsLogger.error("## Cmd0101.getSysCd() connection release exception ##");
					ex3.printStackTrace();
				}
			}
		}	
	}
	

	public Object[] getPfmList(String fileName, String SysCd) throws SQLException, Exception {
		Connection        connPfm        = null;
		PreparedStatement pstmtPfm       = null;
		ResultSet         rsPfm          = null;
		Connection        conn           = null;
		PreparedStatement pstmt          = null;
		ResultSet         rs             = null;
		
		
		StringBuffer      strQuery    = new StringBuffer();
		Object[]		  rtObj		  = null;
		ArrayList<HashMap<String, String>>  rtList = new ArrayList<HashMap<String, String>>();
		ArrayList<HashMap<String, String>>  rtRsrcCd = new ArrayList<HashMap<String, String>>();
		HashMap<String, String>			  rst		  = null;
		int				  pstmtcnt = 0;
		boolean          findSw = false;
		int              i = 0;
		
		
		ConnectionContext connectionContextPfm;
		ConnectionContext connectionContext;
		
		try {	
			//ecamsLogger.debug("+++++++sysGb++++"+sysGB);
			connectionContextPfm = new ConnectionResource(false,"MSM001");			
			connPfm = connectionContextPfm.getConnection();
			
			connectionContext = new ConnectionResource();			
			conn = connectionContext.getConnection();
			
			rtRsrcCd.clear();
			strQuery.setLength(0);
			strQuery.append("select a.cm_rsrccd,a.cm_volpath,b.cm_info \n");
			strQuery.append("  from cmm0038 a,cmm0036 b,cmm0031 c \n");
			strQuery.append(" where c.cm_syscd=?                  \n");
			strQuery.append("   and c.cm_svrcd='01'               \n");
			strQuery.append("   and c.cm_closedt is null          \n");
			strQuery.append("   and c.cm_syscd=a.cm_syscd         \n");
			strQuery.append("   and c.cm_svrcd=a.cm_svrcd         \n");
			strQuery.append("   and c.cm_seqno=a.cm_seqno         \n");
			strQuery.append("   and c.cm_syscd=b.cm_syscd         \n");
			strQuery.append("   and b.cm_closedt is null          \n");
			strQuery.append("   and substr(b.cm_info,27,1)='1'    \n");
			strQuery.append("   and a.cm_rsrccd=b.cm_rsrccd       \n");
			pstmt = conn.prepareStatement(strQuery.toString());
			//pstmt = new LoggableStatement(conn,strQuery.toString());
			pstmt.setString(1, SysCd);
			//ecamsLogger.error(((LoggableStatement)pstmt).getQueryString());  
			rs = pstmt.executeQuery();
			while (rs.next()) {
				rst = new HashMap<String, String>();
				rst.put("cm_rsrccd", rs.getString("cm_rsrccd"));
				rst.put("cm_info", rs.getString("cm_info"));
				rst.put("cm_volpath", rs.getString("cm_volpath"));
				rtRsrcCd.add(rst);
			}
			rs.close();
			pstmt.close();
			
//			strQuery.setLength(0);
//			strQuery.append("select cm_jobcd from cmm0044                \n");
//			strQuery.append(" where cm_syscd=? and cm_userid=?           \n");
//			strQuery.append("   and cm_closedt is null                   \n");
//			pstmt = conn.prepareStatement(strQuery.toString());
//			//pstmt = new LoggableStatement(conn,strQuery.toString());
//			pstmt.setString(1, SysCd);
//			pstmt.setString(2, UserId);
//			//ecamsLogger.error(((LoggableStatement)pstmt).getQueryString());  
//			rs = pstmt.executeQuery();
//			while (rs.next()) {
//				strJobCd = strJobCd + rs.getString("cm_jobcd")+",";
//			}
//			rs.close();
//			pstmt.close();
//			
//			rtList.clear();
			/*
			-----------------------------------------------------------------------
			-- 사용자 CHECK OUT RESOURCE 정보
			-----------------------------------------------------------------------
			-- 항목 설명 (사용자 ID 를 변수로 함
			-----------------------------------------------------------------------
			-- 1. CHECK_USER               : 체크아웃 사용자 ID (D~, S~)
			-- 2. RESOURCE_TYPE            : 리소스 종류
			-- 3. RESOURCE_GROUP           : 리소스 그룹
			-- 4. PHYSICAL_NAME            : 물리명
			-- 5. LOGICAL_NAME             : 논리명
			-- 6. OWNER                    : 소유자
			-- 7. CREATE_TIME              : 최초 생성 시간
			-- 8. UPDATE_TIME              : 최종 수정 시간
			-- 9. PLATFORM                 : 개발 환경 (소문자 c - C FRAMEWORK, 소문자 java - JAVA FRAMEWORK)
			-- 10. DEPLOY_REQUEST_YN       : 일괄 요청 여부
			-- 11. REQUEST_DTM             : 일괄 요청 시간
			-- 12. C_PATH                  : '.c' 위치 (상대 위치를 기준으로 FULL)
			-- 13. H_PATH                  : '.h' 위치 (상대 위치를 기준으로 FULL, MAP 의 경우 공백)
			-- 14. XML_PATH                : '.xml' 위치 (상대 위치를 기준으로 FULL)
			-- 15. PDW_C_PATH              : pdw source 위치 (C 에 한정됨, 공백일 수 있음)
			-- 16. PDW_H_PATH              : pdw header 위치 (C 에 한정됨, 공백일 수 있음)
			-----------------------------------------------------------------------
			*/
			strQuery.setLength(0);
			strQuery.append("SELECT RESOURCE_TYPE,        	                        				 \n");
			strQuery.append("       RESOURCE_GROUP,                               				 	 \n");
			strQuery.append("       PHYSICAL_NAME,                                			 		 \n");
			strQuery.append("       LOGICAL_NAME,                                 			 		 \n");
			strQuery.append("       OWNER,                                        					 \n");
			strQuery.append("       MODIFIER,                                      			 		 \n");
			strQuery.append("       TO_CHAR(CREATE_TIME,'YY/MM/DD') CREATE_TIME,  					 \n");
			strQuery.append("       TO_CHAR(UPDATE_TIME,'YY/MM/DD') UPDATE_TIME,  					 \n");
			strQuery.append("       CASE WHEN RESOURCE_TYPE = 'SERVICE_MODULE' THEN '01'             \n"); 
			strQuery.append("            WHEN RESOURCE_TYPE = 'BATCH_MODULE' THEN '02'               \n"); 
			strQuery.append("            WHEN RESOURCE_TYPE IN ('BIZ_MODULE', 'PRE_PROC_MODULE', 'POST_PROC_MODULE') THEN '03' \n"); 
			strQuery.append("            WHEN RESOURCE_TYPE IN ('PERSIST', 'VIEW', 'EXECSQL', 'DYNAMICSQL') THEN '12' \n"); 
			strQuery.append("            WHEN RESOURCE_TYPE IN ('STRUCTURE') THEN '13'               \n"); 
			strQuery.append("            WHEN RESOURCE_TYPE IN ('GENERAL_BATCH_SERVER') THEN '23'    \n");  
			strQuery.append("            WHEN RESOURCE_TYPE IN ('RESIDENT_UCS_BATCH_SERVER') THEN '24' \n"); 
			strQuery.append("            WHEN RESOURCE_TYPE IN ('BATCH_SHELL') THEN '25'             \n"); 
			strQuery.append("            WHEN RESOURCE_TYPE IN ('HEADER') THEN '09'                  \n"); 
			strQuery.append("       END RSRCCD                                      				 \n");
			strQuery.append("FROM   DEV_RESOURCE		                               				 \n");
			strQuery.append("WHERE	RESOURCE_TYPE not in ('MAP','MESSAGE','PROPERTY','MODULE','FILEIO') \n");
			strQuery.append("  AND  (RESOURCE_DIV is null or RESOURCE_DIV = 'FILEIO')				 \n");
			if (fileName != "" && fileName != null) {
				strQuery.append("AND UPPER(PHYSICAL_NAME) like UPPER(?)   				         	 \n");
			}
			strQuery.append("ORDER BY                                      					         \n");
			strQuery.append("       RESOURCE_TYPE,                       					         \n");
			strQuery.append("       RESOURCE_GROUP,                      					         \n");
			strQuery.append("       PHYSICAL_NAME,                        					         \n");
			strQuery.append("       LOGICAL_NAME                           						     \n");
			
			
            pstmtPfm = connPfm.prepareStatement(strQuery.toString());
            //pstmtPfm = new LoggableStatement(connPfm,strQuery.toString());
			pstmtcnt = 0;
			if (fileName != null && fileName != ""){
				pstmtPfm.setString(++pstmtcnt, "%"+fileName+"%");
			}
			
			//ecamsLogger.error(((LoggableStatement)pstmtPfm).getQueryString());  
			rsPfm = pstmtPfm.executeQuery();
			
			while (rsPfm.next()){	
				findSw = false;
				strQuery.setLength(0);
				strQuery.append("select count(*) cnt from cmr0020            \n");
				strQuery.append(" where cr_syscd=? and cr_rsrccd=?           \n");
				strQuery.append("   and cr_rsrcname=?                        \n");
				pstmt = conn.prepareStatement(strQuery.toString());
			//	pstmt = new LoggableStatement(conn,strQuery.toString());
				pstmt.setString(1, SysCd);
				pstmt.setString(2, rsPfm.getString("RSRCCD"));
				pstmt.setString(3, rsPfm.getString("PHYSICAL_NAME"));
				rs = pstmt.executeQuery();
				if (rs.next()) {
					if (rs.getInt("cnt")==0) {
						findSw = true;
					}
				}
				rs.close();
				pstmt.close();
				
				if (findSw == true) {
					rst = new HashMap<String, String>();
					rst.put("cr_rsrcname", rsPfm.getString("PHYSICAL_NAME"));
					rst.put("cr_story", rsPfm.getString("LOGICAL_NAME"));
					rst.put("cr_rsrccd", rsPfm.getString("RSRCCD"));				
					rst.put("rsrctype", rsPfm.getString("RESOURCE_TYPE"));
					rst.put("rsrcgroup", rsPfm.getString("RESOURCE_GROUP"));
					rst.put("OWNER", rsPfm.getString("OWNER"));
					rst.put("MODIFIER", rsPfm.getString("MODIFIER"));
					rst.put("creatdt", rsPfm.getString("CREATE_TIME"));
					rst.put("updtdt", rsPfm.getString("UPDATE_TIME"));
					rst.put("cr_status", "Z");
					rst.put("staname", "신규대상");
					rst.put("cr_syscd", SysCd);
					rst.put("visible", "1");
					rst.put("checked", "0");
					
					for (i=0;rtRsrcCd.size()>i;i++) {
						if (rtRsrcCd.get(i).get("cm_rsrccd").equals(rsPfm.getString("RSRCCD"))) {
							rst.put("cm_dirpath",rtRsrcCd.get(i).get("cm_volpath"));
							rst.put("cm_info", rtRsrcCd.get(i).get("cm_info"));
							break;
						}
					}
					rtList.add(rst);
					rst = null;
				}
							
			}
			rsPfm.close();
			pstmtPfm.close();
			connPfm.close();
			conn.close();
			
			rsPfm = null;
			pstmtPfm = null;
			connPfm = null;
			conn = null;
			rs = null;
			pstmt = null;
			
			rtObj = rtList.toArray();
			//ecamsLogger.debug("++++++pfmList+++++++"+rtList.toString());
			//return rtList;	
			return rtObj;
			
		} catch (SQLException sqlexception) {
			sqlexception.printStackTrace();
			ecamsLogger.error("## Cmd0101.getPfmList() SQLException START ##");
			ecamsLogger.error("## Error DESC : ", sqlexception);
			ecamsLogger.error("## Cmd0101.getPfmList() SQLException END ##");
			throw sqlexception;
		} catch (Exception exception) {
			exception.printStackTrace();
			ecamsLogger.error("## Cmd0101.getPfmList Exception START ##");
			ecamsLogger.error("## Error DESC : ", exception);
			ecamsLogger.error("## Cmd0101.getPfmList Exception END ##");
			throw exception;
		}finally{
			if (strQuery != null) 	strQuery = null;
			if (rtObj != null)  rtObj = null;
			if (rsPfm != null)     try{rsPfm.close();}catch (Exception ex){ex.printStackTrace();}
			if (pstmtPfm != null)  try{pstmtPfm.close();}catch (Exception ex2){ex2.printStackTrace();}
			if (rs != null)     try{rs.close();}catch (Exception ex){ex.printStackTrace();}
			if (pstmt != null)  try{pstmt.close();}catch (Exception ex2){ex2.printStackTrace();}
			if (connPfm != null){
				try{
					ConnectionResource.release(connPfm);
				}catch(Exception ex3){
					ecamsLogger.error("## Cmd0101.getPfmList() connection release exception ##");
					ex3.printStackTrace();
				}
			}	
			if (conn != null){
				try{
					ConnectionResource.release(conn);
				}catch(Exception ex3){
					ecamsLogger.error("## Cmd0101.getPfmList() connection release exception ##");
					ex3.printStackTrace();
				}
			}			
		}				 
	}
	public String insCmr0020(HashMap<String,String> etcData,ArrayList<HashMap<String,String>> fileList) throws SQLException, Exception {
		Connection        conn        = null;
		ArrayList<HashMap<String, String>>  rtList = new ArrayList<HashMap<String, String>>();
		ConnectionContext connectionContext = new ConnectionResource();	
		Cmd0100 cmd0100 = new Cmd0100();
		svrOpen svropen = new svrOpen();
		HashMap<String, String> rst = null;
		String          strMsg = "OK";
				
		try {
			conn = connectionContext.getConnection();
	        String strDsnCd = "";
	        int j = 0;
	        rtList.clear();
			for (int i=0;i<fileList.size();i++){
				rst = new HashMap<String, String>();
				rst = fileList.get(i);
				if (fileList.get(i).get("cm_dsncd") == null || fileList.get(i).get("cm_dsncd") == "") {
					strDsnCd = cmd0100.cmm0070_Insert(etcData.get("userid"),etcData.get("cr_syscd"),fileList.get(i).get("cr_rsrcname"),fileList.get(i).get("cr_rsrccd"),etcData.get("cr_jobcd"),fileList.get(i).get("cm_dirpath"),false,conn);
					rst.put("cm_dsncd", strDsnCd);
				}
				rtList.add(rst);
				rst = null;
				if ((i + 2) < fileList.size()) {
					for (j=i+1;fileList.size()>j;j++) {
						if (fileList.get(j).get("cm_dsncd") == null || fileList.get(j).get("cm_dsncd") == "") {
							if (fileList.get(i).get("cm_dirpath").equals(fileList.get(j).get("cm_dirpath"))) {
								rst = new HashMap<String, String>();
								rst = fileList.get(j);
								rst.put("cm_dsncd", strDsnCd);
								fileList.set(j, rst);
								rst = null;
							}
						}
					}
				}
			}//end of while-loop statement
			
			//ecamsLogger.error("+++++++++fileList+++"+fileList.toString());
			String retMsg = "";
			for (int i=0;i<rtList.size();i++){
				retMsg = svropen.statusCheck(etcData.get("cr_syscd"),rtList.get(i).get("cm_dsncd"),rtList.get(i).get("cr_rsrcname"),etcData.get("userid"),conn);
	        	if (retMsg.equals("0")) {
	        		//cmr0020_Insert(UserId,SysCd,DsnCd,RsrcName,RsrcCd,JobCd,LangCd,ProgTit,Service,"",conn);
	        		//(String UserId,String SysCd,String DsnCd,String RsrcName,String RsrcCd,String JobCd,String ProgTit,String BaseItem,String rsrcInfo,Connection conn)
	        		retMsg = cmd0100.cmr0020_Insert(etcData.get("userid"),etcData.get("cr_syscd"),etcData.get("OrderId"),
	        				rtList.get(i).get("cm_dsncd"),rtList.get(i).get("cr_rsrcname"),fileList.get(i).get("cr_rsrccd"),
	        				etcData.get("cr_jobcd"),rtList.get(i).get("cr_story"),"",rtList.get(i).get("cm_info"),conn,
	        				rtList.get(i).get("cr_langcd"),rtList.get(i).get("cr_compile"),rtList.get(i).get("cr_makecompile"),rtList.get(i).get("cr_teamcd"),
	        				rtList.get(i).get("cr_sqlcheck"),rtList.get(i).get("cr_document"),"","","","");
		        	//ecamsLogger.debug("++++filename,result++"+file.get("filename")+","+retMsg);
		        	if (!retMsg.substring(0,1).equals("0")) {
		        		strMsg = "ER"+retMsg.substring(1);
		        		break;
		        	}
	        	} else {	
	        		strMsg = "ER"+retMsg.substring(1);
	        		break;
	        	}
			}//end of while-loop statement
			
			conn.close();
			conn = null;
			return strMsg;		    
	        
		} catch (Exception exception) {
			exception.printStackTrace();
			ecamsLogger.error("## Cmd0101.insCmr0020() Exception START ##");				
			ecamsLogger.error("## Error DESC : ", exception);	
			ecamsLogger.error("## Cmd0101.insCmr0020() Exception END ##");				
			throw exception;
		}finally{
			if (conn != null){
				try{
					ConnectionResource.release(conn);
				}catch(Exception ex3){
					ecamsLogger.error("## Cmd0101.insCmr0020() connection release exception ##");
					ex3.printStackTrace();
				}
			}
		}		
	}//end of insCmr0020() method statement	
}//end of Cmd0101 class statement
