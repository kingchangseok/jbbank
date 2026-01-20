/*****************************************************************************************
	1. program ID	: Cmc0300.java
	2. create date	: 2010.11.20
	3. auth		    : no name
	4. update date	: 2010.11.20
	5. auth		    : no name
	6. description	: RFC 결재상신화면
*****************************************************************************************/

package app.eCmc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.apache.logging.log4j.Logger;
import com.ecams.common.dbconn.ConnectionContext;
import com.ecams.common.dbconn.ConnectionResource;
import com.ecams.common.logger.EcamsLogger;
import java.util.ArrayList;
import java.util.HashMap;
import app.common.AutoSeq;
import app.common.LoggableStatement;
import app.common.UserInfo;
import app.eCmr.Cmr0200;

/**
 * @author bigeyes
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */


public class Cmc0500{ 
    /**
     * Logger Class Instance Creation
     * logger
     */	
    Logger ecamsLogger  = EcamsLogger.getLoggerInstance();
    // RFC발행회차
    
    public String setORDERInfo(HashMap<String,String> etcData, ArrayList<HashMap<String,String>> RunnerList, ArrayList<HashMap<String,String>> tmpRunnerList, ArrayList<HashMap<String,String>> ThirdList) throws SQLException, Exception {
		Connection        conn        = null;
		Connection        connD       = null;
		PreparedStatement pstmt       = null;
		PreparedStatement pstmt2      = null;
		ResultSet         rs          = null;
		ResultSet         rs2         = null;
		StringBuffer      strQuery    = new StringBuffer();
		String            retMsg      = "0";
		int				  index		  = 1;
		String			  index2	  = "000";
		String			  subid	  	  = "";
		String			  ordersubid	  	  = "";
		String 		  strORDERID	="";
		String			strTeamName = "";
		int nCnt       = 0;
		int pstmtcount = 0;
		ConnectionContext connectionContext = new ConnectionResource();		
		ConnectionContext connectionContextD = new ConnectionResource(false,"D");
		
		try {
			
			conn = connectionContext.getConnection();
			connD = connectionContextD.getConnection();
			conn.setAutoCommit(false);
			connD.setAutoCommit(false);
			
			strORDERID = "";
		        	
	        strQuery.setLength(0);                		
    		strQuery.append("select lpad(to_number(max(substr(cc_orderid,-4)))+ 1,4,'0') as max	 	\n");               		
    		strQuery.append("  from cmc0420 														\n"); 
    		strQuery.append(" where cc_orderid like ?										 		\n");
    		pstmt = conn.prepareStatement(strQuery.toString());
    		//pstmt = new LoggableStatement(conn,strQuery.toString());
    		pstmt.setString(1, etcData.get("CC_ORDERID")+"%");
    		//ecamsLogger.error(((LoggableStatement)pstmt).getQueryString());
	        rs = pstmt.executeQuery();
	        if (rs.next()) {
	        	if (rs.getString("max") == null || rs.getString("max") == "") {
	        		strORDERID = etcData.get("CC_ORDERID")+"-"+"0001";
	        	} 
	        	else {
	        		strORDERID = etcData.get("CC_ORDERID")+"-"+rs.getString("max");
	        	}
	        }
	        rs.close();
	        pstmt.close();
	     
	        strQuery.setLength(0);                		
    		strQuery.append("select cc_subid	 													\n");               		
    		strQuery.append("  from cmc0410 														\n"); 
    		strQuery.append(" where cc_reqid = ?									 				\n");
    		strQuery.append(" and cc_team = (select cm_project from cmm0040 where cm_userid = ?)	\n");
    		pstmt = conn.prepareStatement(strQuery.toString());
    		//pstmt = new LoggableStatement(conn,strQuery.toString());
    		pstmtcount=1;
    		pstmt.setString(pstmtcount++, etcData.get("CC_REQID"));
    		pstmt.setString(pstmtcount++, etcData.get("CC_ORDERUSER"));
    		//ecamsLogger.error(((LoggableStatement)pstmt).getQueryString());
	        rs = pstmt.executeQuery();
	        if (rs.next()) {
	        	subid=rs.getString("cc_subid");
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
			//pstmt = new LoggableStatement(conn,strQuery.toString());
			pstmtcount = 1;
        	pstmt.setString(pstmtcount++, strORDERID);
        	pstmt.setString(pstmtcount++, etcData.get("CC_REQID"));
        	pstmt.setString(pstmtcount++, subid);
        	pstmt.setString(pstmtcount++, etcData.get("CC_DOCNUM"));
        	pstmt.setString(pstmtcount++, etcData.get("CC_ENDPLAN"));
        	pstmt.setString(pstmtcount++, etcData.get("CC_REQSUB"));
        	pstmt.setString(pstmtcount++, etcData.get("CC_DETAILSAYU"));
        	pstmt.setString(pstmtcount++, etcData.get("CC_ORDERUSER"));
        	pstmt.executeUpdate();
        	pstmt.close();
        	
        	
        	for (int j=0; j < RunnerList.size(); j++){
        		
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
  		        		ordersubid = "01";
    	        	} 
    	        	else {
    	        		ordersubid = rs.getString("max");
    	        	}
  		        }
  		        
  		        rs.close();
  		        pstmt.close();
        		
        		
        		strQuery.setLength(0);
		    	strQuery.append("insert into cmc0450 \n");
		    	strQuery.append(" (CC_ORDERID, CC_ORDERSUBID, CC_RUNNER, CC_TEAM) \n");
		    	strQuery.append(" values \n");
		    	strQuery.append(" ( ?,?,?,? ) \n");
		    	 
		    	pstmt2 = conn.prepareStatement(strQuery.toString());
		    	//pstmt2 = new LoggableStatement(conn,strQuery.toString());
		    	int count = 1;
		    	//pstmt2.setString(pstmtcount++, strORDERID[i]);
		    	pstmt2.setString(count++, strORDERID);
		    	pstmt2.setString(count++, ordersubid);
		    	pstmt2.setString(count++, RunnerList.get(j).get("cm_userid"));
		    	pstmt2.setString(count++, RunnerList.get(j).get("cm_project"));
		    	
		    	//ecamsLogger.error(((LoggableStatement)pstmt2).getQueryString());
		        pstmt2.executeUpdate();
		        pstmt2.close();
        	}
        	
        	for(int k=0;k<tmpRunnerList.size();k++){
    			
    			String UserIp = "";
    			String url = "";
    			
    			strQuery.setLength(0);                		
  	    		strQuery.append("select a.cm_ipaddress, b.cm_url		\n");               		
  	    		strQuery.append("  from cmm0040 a, cmm0010 b 			\n"); 
  	    		strQuery.append(" where a.cm_userid = ?					\n");
  	    		strQuery.append("   and b.cm_stno = 'ECAMS'				\n");
  	    		pstmt = conn.prepareStatement(strQuery.toString());
  	    		//pstmt = new LoggableStatement(conn,strQuery.toString());
  	    		pstmtcount=1;
  	    		pstmt.setString(pstmtcount++, tmpRunnerList.get(k).get("cm_userid"));
  	    		//ecamsLogger.error(((LoggableStatement)pstmt).getQueryString());
  		        rs = pstmt.executeQuery();
  		        if (rs.next()) {
  		        	UserIp = rs.getString("cm_ipaddress");
  		        	url = rs.getString("cm_url");
  		        }
  		        rs.close();
  		        pstmt.close();
    			
    			
    			String Makegap="";
    			Makegap="작업시지제목 : " + etcData.get("CC_REQSUB")+"\n"+ "에 대한 작업지시서가 발행되었습니다. 형상관리시스템에 접속하여 확인하여 주시기 바랍니다.";
    			
		        strQuery.setLength(0);        	
	        	strQuery.append("Begin CMR9920_STR ( ");
	        	//strQuery.append("?, ?, ?, '업무지시서발행통보', ?, 'http://scm/jbbank.co.kr:8080'); End;");
	        	strQuery.append("?, ?, ?, '업무지시서발행통보', ?, ?); End;");
	        	pstmt = conn.prepareStatement(strQuery.toString());
	        	pstmtcount = 1;
	        	pstmt.setString(pstmtcount++, tmpRunnerList.get(k).get("cm_username"));
	        	pstmt.setString(pstmtcount++, tmpRunnerList.get(k).get("cm_username"));
	        	pstmt.setString(pstmtcount++, UserIp);
	        	pstmt.setString(pstmtcount++, Makegap);
	        	pstmt.setString(pstmtcount++, url);
	        	
	        	pstmt.executeUpdate();
	        	pstmt.close();
	        	rs = null;
	        	pstmt = null;
		    }
        	
        	
        	
        	for (int j=0; j < ThirdList.size(); j++){
        		
        		strQuery.setLength(0);
		    	strQuery.append("insert into cmc0421 \n");
		    	strQuery.append(" (CC_ORDERID, CC_THIRDUSER, CC_TEAM, CC_CHECK) \n");
		    	strQuery.append(" values \n");
		    	strQuery.append(" ( ?,?,?,? ) \n");
		    	 
		    	pstmt2 = conn.prepareStatement(strQuery.toString());
		    	//pstmt2 = new LoggableStatement(conn,strQuery.toString());
		    	int count = 1;
		    	//pstmt2.setString(pstmtcount++, strORDERID[i]);
		    	pstmt2.setString(count++, strORDERID);
		    	pstmt2.setString(count++, ThirdList.get(j).get("cm_userid"));
		    	pstmt2.setString(count++, ThirdList.get(j).get("cm_project"));
		    	pstmt2.setString(count++, ThirdList.get(j).get("CC_CHECK"));
		    	
		    	//ecamsLogger.error(((LoggableStatement)pstmt2).getQueryString());
		        pstmt2.executeUpdate();
		        pstmt2.close();
		        

        	}
        	
	     	strQuery.setLength(0);
			strQuery.append("update cmc0400 set cc_status='1'      \n");
			strQuery.append(" where CC_REQID=?                     \n");
			pstmt = conn.prepareStatement(strQuery.toString());
			//pstmt = new LoggableStatement(conn,strQuery.toString());
			pstmt.setString(1, etcData.get("CC_REQID"));
			//ecamsLogger.error(((LoggableStatement)pstmt).getQueryString());
			pstmt.executeUpdate();
			pstmt.close();
			

			strQuery.setLength(0);
			strQuery.append("update cmc0410 set cc_status='1'      \n");
			strQuery.append(" where CC_REQID=?                     \n");
			//strQuery.append(" and CC_SUBID=?                     \n");
			pstmt = conn.prepareStatement(strQuery.toString());
			//pstmt = new LoggableStatement(conn,strQuery.toString());
			pstmt.setString(1, etcData.get("CC_REQID"));
			//pstmt.setString(2, etcData.get("CC_SUBID"));
			//ecamsLogger.error(((LoggableStatement)pstmt).getQueryString());
			pstmt.executeUpdate();
			pstmt.close();
 
			rs = null;
	        pstmt = null;
	        
	        for (int j=0; j < RunnerList.size(); j++){
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
		    	int count = 1;
		    	pstmt.setString(count++, strORDERID);
		    	pstmt.setString(count++, RunnerList.get(j).get("cm_userid"));		    	
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
			    	pstmt2.setString(2, RunnerList.get(j).get("cm_userid"));
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
				    	int count1 = 1;
				    	pstmt2.setString(count1++, rs.getString("REQNO"));   
				    	pstmt2.setString(count1++, rs.getString("ISSUENO"));   
				    	pstmt2.setString(count1++, rs.getString("REQNM"));   
				    	pstmt2.setString(count1++, rs.getString("REQCTNT"));   
				    	pstmt2.setString(count1++, rs.getString("ISSUEUSR"));   
				    	pstmt2.setString(count1++, rs.getString("ISSUECAUSE"));   
				    	pstmt2.setString(count1++, rs.getString("DUEDATE"));   
				    	pstmt2.setString(count1++, rs.getString("ACCOUNTUSR"));   
				    	pstmt2.setString(count1++, rs.getString("THIRDACCOUNTUSR"));   
				    	pstmt2.setString(count1++, rs.getString("DOCTYPE"));   
				    	pstmt2.setString(count1++, rs.getString("DOCNO"));   
				    	pstmt2.setString(count1++, rs.getString("INSUB"));   
				    	pstmt2.setString(count1++, rs.getString("INSUBUSR"));   
				    	pstmt2.setString(count1++, rs.getString("CHARGETEAM"));   
				    	pstmt2.setString(count1++, rs.getString("BIZDETAILNM"));   
				    	pstmt2.setString(count1++, rs.getString("ACTTYPE"));   
				    	pstmt2.setString(count1++, rs.getString("BIZDIFFICULTY"));   
				    	pstmt2.setString(count1++, rs.getString("DEVPERIOD"));   
				    	pstmt2.setString(count1++, rs.getString("DOCNM"));   
				    	pstmt2.setString(count1++, rs.getString("EXORG"));   
				    	pstmt2.setString(count1++, rs.getString("EXORGUSR"));   
				    	pstmt2.setString(count1++, rs.getString("CHARGETEAMDOCNO"));   
				    	pstmt2.setString(count1++, rs.getString("REQTYPE"));   
				    	pstmt2.setString(count1++, rs.getString("DUEPERIOD"));  
				    	pstmt2.setString(count1++, rs.getString("BIZTEAM")); 
				    	pstmt2.setString(count1++, rs.getString("ADDEDBIZCTNT")); 
				    	pstmt2.setString(count1++, rs.getString("BIZNM"));
				    	pstmt2.setString(count1++, rs.getString("CREATER"));
			    		//ecamsLogger.error(((LoggableStatement)pstmt2).getQueryString());
				    	pstmt2.executeUpdate();			    	
				        pstmt2.close();			        
			        }
		        }
		        rs.close();
		        pstmt.close();
 	        }
	        
			conn.commit();
			connD.commit();
			
	        conn.close();
	        connD.close();
	        
	        conn = null;
	        connD = null;
	        
	        
	        return strORDERID;
	        
	        
		} catch (SQLException sqlexception) {
			sqlexception.printStackTrace();
			if (rs != null)     try{rs.close();}catch (Exception ex){ex.printStackTrace();}
			if (pstmt != null)  try{pstmt.close();}catch (Exception ex2){ex2.printStackTrace();}
			if (conn != null){
				try{
					conn.rollback();
					ConnectionResource.release(conn);
				}catch(Exception ex3){
					ecamsLogger.error("## Cmc0100.setISRInfo() connection release exception ##");
					ex3.printStackTrace();
				}
			}
			if (connD != null){
				try{
					connD.rollback();
					ConnectionResource.release(connD);
				}catch(Exception ex3){
					ecamsLogger.error("## Cmc0100.setISRInfo() connection release exception ##");
					ex3.printStackTrace();
				}
			}
			ecamsLogger.error("## Cmc0100.setISRInfo() SQLException START ##");
			ecamsLogger.error("## Error DESC : ", sqlexception);	
			ecamsLogger.error("## Cmc0100.setISRInfo() SQLException END ##");			
			throw sqlexception;
		} catch (Exception exception) {
			exception.printStackTrace();
			if (rs != null)     try{rs.close();}catch (Exception ex){ex.printStackTrace();}
			if (pstmt != null)  try{pstmt.close();}catch (Exception ex2){ex2.printStackTrace();}
			if (conn != null){
				try{
					conn.rollback();
					ConnectionResource.release(conn);
				}catch(Exception ex3){
					ecamsLogger.error("## Cmc0100.setISRInfo() connection release exception ##");
					ex3.printStackTrace();
				}				
			}			
			if (connD != null){
				try{
					connD.rollback();
					ConnectionResource.release(connD);
				}catch(Exception ex3){
					ecamsLogger.error("## Cmc0100.setISRInfo() connection release exception ##");
					ex3.printStackTrace();
				}				
			}			
			ecamsLogger.error("## Cmc0100.setISRInfo() Exception START ##");				
			ecamsLogger.error("## Error DESC : ", exception);	
			ecamsLogger.error("## Cmc0100.setISRInfo() Exception END ##");				
			throw exception;
		}finally{
			if (strQuery != null) 	strQuery = null;
			if (rs != null)     try{rs.close();}catch (Exception ex){ex.printStackTrace();}
			if (pstmt != null)  try{pstmt.close();}catch (Exception ex2){ex2.printStackTrace();}
			if (conn != null){
				try{
					ConnectionResource.release(conn);
					conn = null;
				}catch(Exception ex3){
					ecamsLogger.error("## Cmc0100.setISRInfo() connection release exception ##");
					ex3.printStackTrace();
				}
			}
			if (connD != null){
				try{
					ConnectionResource.release(connD);
					connD = null;
				}catch(Exception ex3){
					ecamsLogger.error("## Cmc0100.setISRInfo() connection release exception ##");
					ex3.printStackTrace();
				}
			}
		}
	}
    
    public Object[] getREQList(String status, String UserID, String cboGbn, String datStD, String datEdD, String Gubun) throws SQLException, Exception {
		Connection        conn        = null;
		PreparedStatement pstmt       = null;
		ResultSet         rs          = null;
		PreparedStatement pstmt2      = null;
		ResultSet         rs2         = null;
		StringBuffer      strQuery    = new StringBuffer();
		String 			  manID       = "";
		Object[] returnObjectArray    = null;
		ArrayList<HashMap<String, String>>  rsval = new ArrayList<HashMap<String, String>>();
		HashMap<String, String> rst	  = null;
		int              parmCnt      = 0;
		ConnectionContext connectionContext = new ConnectionResource();
		
		try {
			
				conn = connectionContext.getConnection();
				strQuery.setLength(0);
				/*
				//외주직원이고 책임자인지 확인
				strQuery.append(" select a.cm_userid, a.cm_username, a.cm_project															\n");
				strQuery.append(" from cmm0040 a, cmm0100 b, cmm0043 c, cmm0020 d											  				\n");
				strQuery.append(" where a.cm_manid = 'N'				  																	\n");
				strQuery.append(" and a.cm_userid = ?																						\n");
				strQuery.append(" and b.cm_deptcd = a.cm_project																			\n");
				strQuery.append(" and a.cm_userid = c.cm_userid																				\n");
				strQuery.append(" and c.cm_rgtcd = '91'																						\n");
				strQuery.append(" and d.cm_macode = 'RGTCD'																					\n");
				strQuery.append(" and c.cm_rgtcd = d.cm_micode																				\n");
				*/
				strQuery.append(" select cm_userid  	\n");
				strQuery.append(" from cmm0040			\n");
				strQuery.append(" where cm_userid = ? 	\n");
				strQuery.append(" and   cm_manid = 'N' 	\n");
				pstmt = conn.prepareStatement(strQuery.toString());
				pstmt.setString(1, UserID);
				rs = pstmt.executeQuery();
				
				if (rs.next()){
					manID = "N";
				}else{
					manID = "Y";
				}
				rs.close();
				pstmt.close();
				
				strQuery.setLength(0);//ISERID : R + YYYYMM + '-' + SEQ(4) : R201011-0001
				int ParmCnt = 0;
				strQuery.append("select distinct a.CC_REQID, a.CC_DOCTYPE, a.CC_DOCNUM, a.CC_DOCSUBJ, a.CC_DEPT1,		\n");
				strQuery.append("       a.CC_REQUSER1, a.CC_DEPT2, a.CC_REQUSER2,										\n");
				strQuery.append("       a.CC_DOCNUM2, a.CC_DETAILJOBN, a.CC_REQTYPE, a.CC_ENDPLAN, a.CC_JOBCD,  		\n");
				strQuery.append("   	a.CC_ACTTYPE, a.CC_JOBDIF, a.CC_CHKTEAM, a.CC_CHKDATA, a.CC_DEVSTDT,			\n");
				strQuery.append("   	a.CC_DEVEDDT, a.CC_DETAILSAYU, a.CC_STARTDT, a.CC_ENDDT, a.CC_STATUS,			\n");
				strQuery.append("   	a.CC_EDITOR, a.CC_ETTEAM, ISR_RECVPART(a.CC_REQID) RECVPART,					\n");
				strQuery.append("   	DECODE(a.cc_requser1,null,'',b.cm_userid,b.cm_username) as bname, 				\n");				
				strQuery.append("   	g.cm_username as editorName,													\n");
				strQuery.append(" 		DECODE(a.cc_dept1,null,'',f.cm_deptcd,f.cm_deptname) as CM_DEPTNAME				\n");
				strQuery.append(" from cmc0400 a, cmm0040 b,cmm0100 f, cmm0040 g										\n");
				if(status.equals("00")){
					strQuery.append(" where a.CC_STATUS not in ('9','3') \n" );
				}else if(status.equals("01")){
					strQuery.append(" where a.CC_STATUS in ('9') 		\n" );
				}else{
					strQuery.append(" where a.CC_STATUS in ('0','1','3','9') \n" );
				}
				
				if(cboGbn.equals("01")){
//					if(Gubun.equals("def")){
//						strQuery.append(" and cc_reqid in (select cc_reqid from cmc0410 where cc_team = (select cm_project from cmm0040 where cm_userid =?))	\n");
//						strQuery.append(" and a.CC_DOCTYPE <> '03'		\n");	
//					}else{
						if(manID.equals("N")){
							strQuery.append(" and cc_reqid in (select cc_reqid from cmc0410 where cc_team = ?)	\n");
							strQuery.append(" and a.CC_DOCTYPE = '03'	\n");
						}else{
							strQuery.append(" and cc_reqid in (select cc_reqid from cmc0410 where cc_team = (select cm_project from cmm0040 where cm_userid =?))	\n");
							strQuery.append(" and a.CC_DOCTYPE <> '03'		\n");
						}
//					}
				}
				else if(cboGbn.equals("00")){
					if(Gubun.equals("def")){
						strQuery.append(" and a.CC_DOCTYPE <> '03'  \n");
					}
				}
				
				if(!datStD.equals("") && datStD != null){
					strQuery.append("and to_char(a.CC_STARTDT,'yyyy/mm/dd') >= ? \n" );
					strQuery.append("and to_char(a.CC_STARTDT,'yyyy/mm/dd') <= ? \n" );
				}
				//strQuery.append("and b.cm_userid = a.CC_REQUSER1 \n");
				strQuery.append(" and b.cm_userid = nvl(a.cc_requser1,a.cc_editor) \n");
				strQuery.append(" and g.cm_userid = a.cc_EDITOR   \n");
				//strQuery.append(" and a.cc_dept1 = f.cm_deptcd 	  \n");
				strQuery.append(" and f.cm_deptcd = nvl(a.cc_dept1, a.cc_etteam)   \n");
				strQuery.append(" order by a.CC_STARTDT desc					   \n");
		        pstmt = conn.prepareStatement(strQuery.toString());
		        pstmt = new LoggableStatement(conn,strQuery.toString());
				
		        if(cboGbn.equals("01")){
//		        	if (!manID.equals("") && manID != null){
//		        		pstmt.setString(++ParmCnt, UserID);
//		        	}else{
		        		pstmt.setString(++ParmCnt, UserID);
//		        	}
					
				}
				if(!datStD.equals("") && datStD != null){
			        pstmt.setString(++ParmCnt, datStD);
			        pstmt.setString(++ParmCnt, datEdD);
				}
		        ecamsLogger.error(((LoggableStatement)pstmt).getQueryString());
		        rs = pstmt.executeQuery();
	        
	        while(rs.next()) {
		  		rst = new HashMap<String, String>();
		  		rst.put("CC_REQID", rs.getString("CC_REQID"));
				rst.put("CC_DOCTYPE", rs.getString("CC_DOCTYPE"));
				rst.put("CC_DOCNUM", rs.getString("CC_DOCNUM"));
				rst.put("CC_DOCSUBJ", rs.getString("CC_DOCSUBJ"));
				rst.put("CC_DEPT1", rs.getString("CC_DEPT1"));
				rst.put("CC_REQUSER1", rs.getString("CC_REQUSER1"));
				rst.put("CC_REQUSER1FULL", rs.getString("bname"));
				rst.put("CC_DEPT2", rs.getString("CC_DEPT2"));
				rst.put("CC_REQUSER2", rs.getString("CC_REQUSER2"));
				rst.put("CC_DOCNUM2", rs.getString("CC_DOCNUM2"));
				rst.put("CC_DETAILJOBN", rs.getString("CC_DETAILJOBN"));
				rst.put("CC_REQTYPE", rs.getString("CC_REQTYPE"));
				rst.put("CC_ENDPLAN", rs.getString("CC_ENDPLAN"));
				rst.put("CC_JOBCD", rs.getString("CC_JOBCD"));
				rst.put("CC_ACTTYPE", rs.getString("CC_ACTTYPE"));
				rst.put("CC_JOBDIF", rs.getString("CC_JOBDIF"));
				rst.put("CC_CHKTEAM", rs.getString("CC_CHKTEAM"));
				rst.put("CC_CHKDATA", rs.getString("CC_CHKDATA"));
				rst.put("CC_DEVSTDT", rs.getString("CC_DEVSTDT"));
				rst.put("CC_DEVEDDT", rs.getString("CC_DEVEDDT"));
				rst.put("CC_DETAILSAYU", rs.getString("CC_DETAILSAYU"));
				rst.put("CC_STARTDT", rs.getString("CC_STARTDT"));
				rst.put("CC_ENDDT", rs.getString("CC_ENDDT"));
				rst.put("CC_STATUS", rs.getString("CC_STATUS"));
				rst.put("CC_EDITOR", rs.getString("CC_EDITOR"));
				rst.put("CC_ETTEAM", rs.getString("CC_ETTEAM"));
				rst.put("RECVPART", rs.getString("RECVPART"));
				rst.put("CM_DEPTNAME", rs.getString("CM_DEPTNAME"));
				rst.put("editorName" , rs.getString("editorName"));
				rst.put("manID", manID);
				
				
				String deptreq = "";
				String requser = "";
				
				strQuery.setLength(0);//ISERID : R + YYYYMM + '-' + SEQ(4) : R201011-0001
				ParmCnt = 0;
				strQuery.append("select a.CC_REQID, a.CC_DEPT3, a.CC_REQUSER3, b.cm_username, c.cm_deptname, d.cm_posname	\n");
				strQuery.append(" from cmc0401 a, cmm0040 b, cmm0100 c, cmm0103 d							\n");
				strQuery.append(" where a.CC_REQID = ?									 					\n" );
				strQuery.append(" and a.CC_REQUSER3 = b.cm_userid						 					\n" );
				strQuery.append(" and b.cm_position = d.cm_poscd                                \n");
				strQuery.append(" and a.CC_DEPT3 = c.cm_deptcd							 					\n" );
		        pstmt2 = conn.prepareStatement(strQuery.toString());
		        //pstmt2 = new LoggableStatement(conn,strQuery.toString());
				pstmt2.setString(++ParmCnt, rs.getString("CC_REQID"));
				//ecamsLogger.error(((LoggableStatement)pstmt2).getQueryString());
		        rs2 = pstmt2.executeQuery();
	        
		        while(rs2.next()) {
		        	if(!requser.equals("")){
		        		requser=requser+",";
		        	}
			  		deptreq = rs2.getString("CC_DEPT3")+"^"+rs2.getString("cm_deptname")+"^"+rs2.getString("CC_REQUSER3")+"^"+rs2.getString("cm_username")+"^"+rs2.getString("cm_posname");
			  		requser = requser+deptreq;
			  		deptreq = "";
			  		
		        }
		        rst.put("CC_REQUSER3", requser);
		        
				rs2.close();
		        pstmt2.close();

		        String fileinfo = "";
				String fileinfos = "";
				
				strQuery.setLength(0);//ISERID : R + YYYYMM + '-' + SEQ(4) : R201011-0001
				ParmCnt = 0;
				strQuery.append("select a.CC_ID,a.CC_SUBID, a.CC_SUBREQ, a.CC_SEQNO, a.CC_SAVEFILE, a.CC_ATTFILE,	\n");
				strQuery.append(" a.CC_LASTDT, a.CC_EDITOR, a.CC_REQCD, B.CM_USERNAME						\n");
				strQuery.append(" from cmc1001 a, cmm0040 b													\n");
				strQuery.append(" where a.CC_ID = ?									 					\n" );
				strQuery.append(" and a.CC_EDITOR = b.cm_userid						 					\n" );
		        pstmt2 = conn.prepareStatement(strQuery.toString());
		        //pstmt2 = new LoggableStatement(conn,strQuery.toString());
				pstmt2.setString(++ParmCnt, rs.getString("CC_REQID"));
				//ecamsLogger.error(((LoggableStatement)pstmt2).getQueryString());
		        rs2 = pstmt2.executeQuery();
	        
		        while(rs2.next()) {
		        	if(!fileinfos.equals("")){
		        		fileinfos=fileinfos+"|";
		        	}
		        	fileinfo = rs2.getString("CC_ID")+"^"+rs2.getString("CC_SUBID")+"^"+rs2.getString("CC_SUBREQ")+"^"+rs2.getString("CC_SEQNO")+"^"
			  		+rs2.getString("CC_SAVEFILE")+"^"+rs2.getString("CC_ATTFILE")+"^"+rs2.getString("CC_LASTDT")+"^"
			  		+rs2.getString("CC_EDITOR")+"^"+rs2.getString("CC_REQCD")+"^"+rs2.getString("CM_USERNAME");
			  		fileinfos = fileinfos+fileinfo;
			  		fileinfo = "";
			  		
		        }
		        rst.put("fileinfos", fileinfos);
		        
				rsval.add(rst);
				rst = null;
				
				rs2.close();
		        pstmt2.close();
	        }
	        rs.close();
	        pstmt.close();
	        conn.close();
	        
	  		rs = null;
	  		pstmt = null;
	  		rs2 = null;
	  		pstmt2 = null;
	  		conn = null;
			
			returnObjectArray = rsval.toArray();
			rsval.clear();
			rsval = null;

			return returnObjectArray;
			
			
		} catch (SQLException sqlexception) {
			sqlexception.printStackTrace();
			ecamsLogger.error("## Cmc0500.getREQList() SQLException START ##");
			ecamsLogger.error("## Error DESC : ", sqlexception);	
			ecamsLogger.error("## Cmc0500.getREQList() SQLException END ##");			
			throw sqlexception;
		} catch (Exception exception) {
			exception.printStackTrace();
			ecamsLogger.error("## Cmc0500.getREQList() Exception START ##");				
			ecamsLogger.error("## Error DESC : ", exception);	
			ecamsLogger.error("## Cmc0500.getREQList() Exception END ##");				
			throw exception;
		}finally{
			if (strQuery != null) 	strQuery = null;
			if (returnObjectArray != null)	returnObjectArray = null;
			if (rs != null)     try{rs.close();}catch (Exception ex){ex.printStackTrace();}
			if (pstmt != null)  try{pstmt.close();}catch (Exception ex2){ex2.printStackTrace();}
			if (conn != null){
				try{
					ConnectionResource.release(conn);
				}catch(Exception ex3){
					ecamsLogger.error("## Cmc0500.getREQList() connection release exception ##");
					ex3.printStackTrace();
				}
			}
		}
	}//end of getISRList() method statement
    
    public String setORDERupdt(HashMap<String,String> etcData, ArrayList<HashMap<String,String>> RunnerList, ArrayList<HashMap<String,String>> tmpRunnerList, ArrayList<HashMap<String,String>> ThirdList) throws SQLException, Exception {
		Connection        conn        = null;
		Connection        connD        = null;
		PreparedStatement pstmt       = null;
		PreparedStatement pstmt2      = null;
		ResultSet         rs          = null;
		ResultSet         rs2         = null;
		StringBuffer      strQuery    = new StringBuffer();
		String            retMsg      = "0";
		int				  index		  = 1;
		String			  index2	  = "000";
		String			  subid	  	  = "";
		String	 		  strORDERID  ="";
		String			  ordersubid	  	  = "";
		String			  strTeamName  = "";
		int 			  nCnt        = 0;
		int pstmtcount = 0;
		ConnectionContext connectionContext = new ConnectionResource();		
		ConnectionContext connectionContextD = new ConnectionResource(false,"D");
		
		try {
			
			conn = connectionContext.getConnection();
			connD = connectionContextD.getConnection();
			conn.setAutoCommit(false);
			connD.setAutoCommit(false);
			
			
			strORDERID=etcData.get("CC_ORDERID");
			
			strQuery.setLength(0);
        	strQuery.append("update cmc0420	set					                              				\n");
        	strQuery.append("    CC_REQID=?,                   					    						\n");
        	strQuery.append("    CC_DOCNUM=?,CC_ENDPLAN=?,										 			\n");
        	strQuery.append("    CC_REQSUB=?,CC_DETAILSAYU=?												\n");
        	strQuery.append("where cc_orderid = ?															\n");
        	pstmt = conn.prepareStatement(strQuery.toString());
			pstmt = new LoggableStatement(conn,strQuery.toString());
			pstmtcount = 1;
        	pstmt.setString(pstmtcount++, etcData.get("CC_REQID"));
        	pstmt.setString(pstmtcount++, etcData.get("CC_DOCNUM"));
        	pstmt.setString(pstmtcount++, etcData.get("CC_ENDPLAN"));
        	pstmt.setString(pstmtcount++, etcData.get("CC_REQSUB"));
        	pstmt.setString(pstmtcount++, etcData.get("CC_DETAILSAYU"));
        	pstmt.setString(pstmtcount++, etcData.get("CC_ORDERID"));
            ecamsLogger.error(((LoggableStatement)pstmt).getQueryString());
        	pstmt.executeUpdate();
        	pstmt.close();
        	
        	strQuery.setLength(0);
			strQuery.append(" delete cmc0421 where CC_ORDERID = ? ");
    		//strQuery.append(" delete cmm0400 where cc_reqid = ? ");
    		
    		pstmt = conn.prepareStatement(strQuery.toString());
    		pstmt.setString(1, etcData.get("CC_ORDERID"));
    		
    		pstmt.executeUpdate();
    		pstmt.close();
        	
        	
        	for (int j=0; j < ThirdList.size(); j++){
        		
        		strQuery.setLength(0);
		    	strQuery.append("insert into cmc0421 \n");
		    	strQuery.append(" (CC_ORDERID, CC_THIRDUSER, CC_TEAM, CC_CHECK) \n");
		    	strQuery.append(" values \n");
		    	strQuery.append(" ( ?,?,?,? ) \n");
		    	 
		    	pstmt2 = conn.prepareStatement(strQuery.toString());
		    	//pstmt2 = new LoggableStatement(conn,strQuery.toString());
		    	int count = 1;
		    	//pstmt2.setString(pstmtcount++, strORDERID[i]);
		    	pstmt2.setString(count++, etcData.get("CC_ORDERID"));
		    	pstmt2.setString(count++, ThirdList.get(j).get("cm_userid"));
		    	pstmt2.setString(count++, ThirdList.get(j).get("cm_project"));
		    	pstmt2.setString(count++, ThirdList.get(j).get("CC_CHECK"));
		    	
		    	//ecamsLogger.error(((LoggableStatement)pstmt2).getQueryString());
		        pstmt2.executeUpdate();
		        pstmt2.close();
		        

        	}
        	
        	strQuery.setLength(0);
			strQuery.append(" delete cmc0450 where CC_ORDERID = ? ");
    		pstmt = conn.prepareStatement(strQuery.toString());
    		pstmt.setString(1, etcData.get("CC_ORDERID"));
    		
    		pstmt.executeUpdate();
    		pstmt.close();
    		
    		for (int j=0; j < RunnerList.size(); j++){
        		
        		strQuery.setLength(0);                		
  	    		strQuery.append("select lpad(to_number(max(CC_ORDERSUBID))+ 1,2,'0') as max				\n");               		
  	    		strQuery.append("  from cmc0450 														\n"); 
  	    		strQuery.append(" where CC_ORDERID = ?									 				\n");
  	    		pstmt = conn.prepareStatement(strQuery.toString());
  	    		//pstmt = new LoggableStatement(conn,strQuery.toString());
  	    		pstmtcount=1;
  	    		pstmt.setString(pstmtcount++, etcData.get("CC_ORDERID"));
  	    		//ecamsLogger.error(((LoggableStatement)pstmt).getQueryString());
  		        rs = pstmt.executeQuery();
  		        if (rs.next()) {
  		        	if (rs.getString("max") == null) {
  		        		ordersubid = "01";
    	        	} 
    	        	else {
    	        		ordersubid = rs.getString("max");
    	        	}
  		        }
  		        
  		        rs.close();
  		        pstmt.close();
        		
        		
        		strQuery.setLength(0);
		    	strQuery.append("insert into cmc0450 \n");
		    	strQuery.append(" (CC_ORDERID, CC_ORDERSUBID, CC_RUNNER, CC_TEAM) \n");
		    	strQuery.append(" values \n");
		    	strQuery.append(" ( ?,?,?,? ) \n");
		    	 
		    	pstmt2 = conn.prepareStatement(strQuery.toString());
		    	//pstmt2 = new LoggableStatement(conn,strQuery.toString());
		    	int count = 1;
		    	//pstmt2.setString(pstmtcount++, strORDERID[i]);
		    	pstmt2.setString(count++, etcData.get("CC_ORDERID"));
		    	pstmt2.setString(count++, ordersubid);
		    	pstmt2.setString(count++, RunnerList.get(j).get("cm_userid"));
		    	pstmt2.setString(count++, RunnerList.get(j).get("cm_project"));
		    	
		    	//ecamsLogger.error(((LoggableStatement)pstmt2).getQueryString());
		        pstmt2.executeUpdate();
		        pstmt2.close();
        	}
    		
    		for(int k=0;k<tmpRunnerList.size();k++){
    			
    			String UserIp = "";
    			String url = "";
    			
    			strQuery.setLength(0);                		
  	    		strQuery.append("select a.cm_ipaddress, b.cm_url	\n");               		
  	    		strQuery.append("  from cmm0040 a, cmm0010 b 		\n"); 
  	    		strQuery.append(" where a.cm_userid = ?				\n");
  	    		strQuery.append("   and b.cm_stno = 'ECAMS'			\n");
  	    		pstmt = conn.prepareStatement(strQuery.toString());
  	    		//pstmt = new LoggableStatement(conn,strQuery.toString());
  	    		pstmtcount=1;
  	    		pstmt.setString(pstmtcount++, tmpRunnerList.get(k).get("cm_userid"));
  	    		//ecamsLogger.error(((LoggableStatement)pstmt).getQueryString());
  		        rs = pstmt.executeQuery();
  		        if (rs.next()) {
  		        	UserIp = rs.getString("cm_ipaddress");
  		        	url = rs.getString("cm_url");
  		        }
  		        rs.close();
  		        pstmt.close();
    			
    			String Makegap="";
    			Makegap="작업시지제목 : " + etcData.get("CC_REQSUB")+"\n"+ "에 대한 작업지시서가 발행되었습니다. 형상관리시스템에 접속하여 확인하여 주시기 바랍니다.";
    			
		        strQuery.setLength(0);        	
	        	strQuery.append("Begin CMR9920_STR ( ");
	        	//strQuery.append("?, ?, ?, '업무지시서발행통보', ?, 'http://scm/jbbank.co.kr:8080'); End;");
	        	strQuery.append("?, ?, ?, '업무지시서발행통보', ?, ?); End;");
	        	pstmt = conn.prepareStatement(strQuery.toString());
	        	pstmtcount = 1;
	        	pstmt.setString(pstmtcount++, tmpRunnerList.get(k).get("cm_username"));
	        	pstmt.setString(pstmtcount++, tmpRunnerList.get(k).get("cm_username"));
	        	pstmt.setString(pstmtcount++, UserIp);
	        	pstmt.setString(pstmtcount++, Makegap);
	        	pstmt.setString(pstmtcount++, url);
	        	
	        	pstmt.executeUpdate();
	        	pstmt.close();
	        	rs = null;
	        	pstmt = null;
		    }
    	
	        for (int j=0; j < RunnerList.size(); j++){
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
		    	int count = 1;
		    	pstmt.setString(count++, strORDERID);
		    	pstmt.setString(count++, RunnerList.get(j).get("cm_userid"));		    	
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
			    	pstmt2.setString(2, RunnerList.get(j).get("cm_userid"));
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
				    	int count1 = 1;
				    	pstmt2.setString(count1++, rs.getString("REQNO"));   
				    	pstmt2.setString(count1++, rs.getString("ISSUENO"));   
				    	pstmt2.setString(count1++, rs.getString("REQNM"));   
				    	pstmt2.setString(count1++, rs.getString("REQCTNT"));   
				    	pstmt2.setString(count1++, rs.getString("ISSUEUSR"));   
				    	pstmt2.setString(count1++, rs.getString("ISSUECAUSE"));   
				    	pstmt2.setString(count1++, rs.getString("DUEDATE"));   
				    	pstmt2.setString(count1++, rs.getString("ACCOUNTUSR"));   
				    	pstmt2.setString(count1++, rs.getString("THIRDACCOUNTUSR"));   
				    	pstmt2.setString(count1++, rs.getString("DOCTYPE"));   
				    	pstmt2.setString(count1++, rs.getString("DOCNO"));   
				    	pstmt2.setString(count1++, rs.getString("INSUB"));   
				    	pstmt2.setString(count1++, rs.getString("INSUBUSR"));   
				    	pstmt2.setString(count1++, rs.getString("CHARGETEAM"));   
				    	pstmt2.setString(count1++, rs.getString("BIZDETAILNM"));   
				    	pstmt2.setString(count1++, rs.getString("ACTTYPE"));   
				    	pstmt2.setString(count1++, rs.getString("BIZDIFFICULTY"));   
				    	pstmt2.setString(count1++, rs.getString("DEVPERIOD"));   
				    	pstmt2.setString(count1++, rs.getString("DOCNM"));   
				    	pstmt2.setString(count1++, rs.getString("EXORG"));   
				    	pstmt2.setString(count1++, rs.getString("EXORGUSR"));   
				    	pstmt2.setString(count1++, rs.getString("CHARGETEAMDOCNO"));   
				    	pstmt2.setString(count1++, rs.getString("REQTYPE"));   
				    	pstmt2.setString(count1++, rs.getString("DUEPERIOD"));  
				    	pstmt2.setString(count1++, rs.getString("BIZTEAM")); 
				    	pstmt2.setString(count1++, rs.getString("ADDEDBIZCTNT")); 
				    	pstmt2.setString(count1++, rs.getString("BIZNM"));
				    	pstmt2.setString(count1++, rs.getString("CREATER"));
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
				    	int count1 = 1;
				    	pstmt2.setString(count1++, rs.getString("REQNO"));   
				    	pstmt2.setString(count1++, rs.getString("ISSUENO"));   
				    	pstmt2.setString(count1++, rs.getString("REQNM"));   
				    	pstmt2.setString(count1++, rs.getString("REQCTNT"));   
				    	pstmt2.setString(count1++, rs.getString("ISSUEUSR"));   
				    	pstmt2.setString(count1++, rs.getString("ISSUECAUSE"));   
				    	pstmt2.setString(count1++, rs.getString("DUEDATE"));   
				    	pstmt2.setString(count1++, rs.getString("ACCOUNTUSR"));   
				    	pstmt2.setString(count1++, rs.getString("THIRDACCOUNTUSR"));   
				    	pstmt2.setString(count1++, rs.getString("DOCTYPE"));   
				    	pstmt2.setString(count1++, rs.getString("DOCNO"));   
				    	pstmt2.setString(count1++, rs.getString("INSUB"));   
				    	pstmt2.setString(count1++, rs.getString("INSUBUSR"));   
				    	pstmt2.setString(count1++, rs.getString("CHARGETEAM"));   
				    	pstmt2.setString(count1++, rs.getString("BIZDETAILNM"));   
				    	pstmt2.setString(count1++, rs.getString("ACTTYPE"));   
				    	pstmt2.setString(count1++, rs.getString("BIZDIFFICULTY"));   
				    	pstmt2.setString(count1++, rs.getString("DEVPERIOD"));   
				    	pstmt2.setString(count1++, rs.getString("DOCNM"));   
				    	pstmt2.setString(count1++, rs.getString("EXORG"));   
				    	pstmt2.setString(count1++, rs.getString("EXORGUSR"));   
				    	pstmt2.setString(count1++, rs.getString("CHARGETEAMDOCNO"));   
				    	pstmt2.setString(count1++, rs.getString("REQTYPE"));   
				    	pstmt2.setString(count1++, rs.getString("DUEPERIOD"));  
				    	pstmt2.setString(count1++, rs.getString("BIZTEAM")); 
				    	pstmt2.setString(count1++, rs.getString("ADDEDBIZCTNT")); 
				    	pstmt2.setString(count1++, rs.getString("BIZNM"));
				    	pstmt2.setString(count1++, rs.getString("CREATER"));
			    		pstmt2.setString(count1++, strORDERID);
				    	pstmt2.setString(count1++, RunnerList.get(j).get("cm_userid"));
			    		//ecamsLogger.error(((LoggableStatement)pstmt2).getQueryString());
				    	pstmt2.executeUpdate();			    	
				        pstmt2.close();		
			        }
		        }
		        rs.close();
		        pstmt.close();
 	        }
	        
	        
	    conn.commit();
    	connD.commit();
    	
        conn.close();
        connD.close();
        
        rs = null;
        pstmt = null;
        conn = null;
        
        
        return strORDERID;
	        
	        
		} catch (SQLException sqlexception) {
			sqlexception.printStackTrace();
			if (rs != null)     try{rs.close();}catch (Exception ex){ex.printStackTrace();}
			if (pstmt != null)  try{pstmt.close();}catch (Exception ex2){ex2.printStackTrace();}
			if (conn != null){
				try{
					conn.rollback();
					ConnectionResource.release(conn);
				}catch(Exception ex3){
					ecamsLogger.error("## Cmc0100.setISRInfo() connection release exception ##");
					ex3.printStackTrace();
				}
			}
			if (connD != null){
				try{
					connD.rollback();
					ConnectionResource.release(connD);
				}catch(Exception ex3){
					ecamsLogger.error("## Cmc0100.setISRInfo() connection release exception ##");
					ex3.printStackTrace();
				}
			}
			ecamsLogger.error("## Cmc0100.setISRInfo() SQLException START ##");
			ecamsLogger.error("## Error DESC : ", sqlexception);	
			ecamsLogger.error("## Cmc0100.setISRInfo() SQLException END ##");			
			throw sqlexception;
		} catch (Exception exception) {
			exception.printStackTrace();
			if (rs != null)     try{rs.close();}catch (Exception ex){ex.printStackTrace();}
			if (pstmt != null)  try{pstmt.close();}catch (Exception ex2){ex2.printStackTrace();}
			if (conn != null){
				try{
					conn.rollback();
					ConnectionResource.release(conn);
				}catch(Exception ex3){
					ecamsLogger.error("## Cmc0100.setISRInfo() connection release exception ##");
					ex3.printStackTrace();
				}				
			}			
			if (connD != null){
				try{
					connD.rollback();
					ConnectionResource.release(connD);
				}catch(Exception ex3){
					ecamsLogger.error("## Cmc0100.setISRInfo() connection release exception ##");
					ex3.printStackTrace();
				}				
			}			
			ecamsLogger.error("## Cmc0100.setISRInfo() Exception START ##");				
			ecamsLogger.error("## Error DESC : ", exception);	
			ecamsLogger.error("## Cmc0100.setISRInfo() Exception END ##");				
			throw exception;
		}finally{
			if (strQuery != null) 	strQuery = null;
			if (rs != null)     try{rs.close();}catch (Exception ex){ex.printStackTrace();}
			if (pstmt != null)  try{pstmt.close();}catch (Exception ex2){ex2.printStackTrace();}
			if (conn != null){
				try{
					ConnectionResource.release(conn);
					conn = null;
				}catch(Exception ex3){
					ecamsLogger.error("## Cmc0100.setISRInfo() connection release exception ##");
					ex3.printStackTrace();
				}
			}
			if (connD != null){
				try{
					ConnectionResource.release(connD);
					connD = null;
				}catch(Exception ex3){
					ecamsLogger.error("## Cmc0100.setISRInfo() connection release exception ##");
					ex3.printStackTrace();
				}
			}
		}
	}
    
   public Object[] getORDERListSearch(String Status, String Stdt, String Eddt, String UserId) throws SQLException, Exception{
	   Connection conn = null;
	   PreparedStatement pstmt = null;
	   ResultSet rs = null;
	   ResultSet rs2 = null;
	   PreparedStatement pstmt2 = null;
	   Object[] returnObjectArray = null;
	   ArrayList<HashMap<String,String>> rsval = new ArrayList<HashMap<String, String>>();
	   HashMap<String, String> rst = null;
	   StringBuffer strQuery = new StringBuffer();
	   ConnectionContext connectionContext = new ConnectionResource();
	   int  QACnt = 0;
	   
	   try{
		   conn = connectionContext.getConnection();

		   strQuery.setLength(0);
		   strQuery.append(" select COUNT(1) AS CNT FROM CMM0043 WHERE CM_USERID = ? AND CM_RGTCD = 'Q1' \n");		   
		   pstmt = conn.prepareStatement(strQuery.toString());
		   //pstmt = new LoggableStatement(conn,strQuery.toString());
		   pstmt.setString(1, UserId);
		   //ecamsLogger.error(((LoggableStatement)pstmt).getQueryString());
		   rs = pstmt.executeQuery();
		   if (rs.next()){
			   QACnt =  rs.getInt("CNT");
		   }
		   rs.close();
		   pstmt.close();
		   
		   // 20230126 관리자 권한 있어도 QA처럼 전체 조회 되도록 로직 추가
		   UserInfo     userinfo = new UserInfo();
		   boolean adminYn = userinfo.isAdmin_conn(UserId,conn);
		   userinfo = null; 
		   
		   if(adminYn){
			   QACnt = 1;
		   }
		   
		   strQuery.setLength(0);
		   strQuery.append(" select A.CC_ORDERID, A.CC_REQID, A.CC_SUBID, A.CC_DOCNUM, A.CC_ENDPLAN,            			      \n");
		   strQuery.append(" A.CC_REQSUB, A.CC_DETAILSAYU, A.CC_STATUS,															  \n");
		   strQuery.append(" to_char(A.CC_STARTDT, 'yyyy/mm/dd')STARTDT, to_char(A.CC_ENDDT, 'yyyy/mm/dd') ENDDT, A.CC_ORDERUSER, \n");
		   strQuery.append(" (select cc_acttype from cmc0400 where cc_reqid = a.cc_reqid)  CC_ACTTYPE                             \n");
		   strQuery.append(" from   cmc0420 a																				      \n");
		   
		   if (QACnt == 0) {
			   strQuery.append(" where A.CC_ORDERUSER = ?																		  \n");
		   } else {
			   strQuery.append(" where A.CC_ORDERUSER = A.CC_ORDERUSER															  \n");			   
		   }
		   
		   if (Status.equals("0")){
			   strQuery.append(" and a.cc_status not in ('9','3')																  \n");
		   }else if (Status.equals("9")){
			   strQuery.append(" and a.cc_status in ('9')																          \n");
		   }
		   if ((!Stdt.equals("") && Stdt != null) && (!Eddt.equals("") && Eddt != null)) {
				strQuery.append(" and to_char(A.CC_STARTDT,'yyyy/mm/dd') between ? and ? 										  \n");
		   }
		   
		   strQuery.append(" UNION																								  \n");
		   strQuery.append(" select A.CC_ORDERID, A.CC_REQID, A.CC_SUBID, A.CC_DOCNUM, A.CC_ENDPLAN,           	                  \n");
		   strQuery.append(" A.CC_REQSUB, A.CC_DETAILSAYU, A.CC_STATUS,															  \n");
		   strQuery.append(" to_char(A.CC_STARTDT, 'yyyy/mm/dd')STARTDT, to_char(A.CC_ENDDT, 'yyyy/mm/dd') ENDDT, A.CC_ORDERUSER, \n");
		   strQuery.append(" (select cc_acttype from cmc0400 where cc_reqid = a.cc_reqid)  CC_ACTTYPE                             \n");
		   strQuery.append(" from   cmc0420 a, cmc0450 b																	      \n");
		   strQuery.append(" where a.cc_orderid = b.cc_orderid																      \n");
		   if (QACnt == 0) {
			   strQuery.append(" and b.cc_runner = ?                                                                              \n");
		   }
		   if (Status.equals("0")){
			   strQuery.append(" and a.cc_status not in ('9','3')																  \n");
		   }else if (Status.equals("9")){
			   strQuery.append(" and a.cc_status in ('9')																          \n");
		   }
		   
		   if ((!Stdt.equals("") && Stdt != null) && (!Eddt.equals("") && Eddt != null)) {
				strQuery.append(" and to_char(A.CC_STARTDT,'yyyy/mm/dd') between ? and ? 										  \n");
		   }
		   
		   pstmt = conn.prepareStatement(strQuery.toString());
		   pstmt = new LoggableStatement(conn,strQuery.toString());
		   int parmCnt = 0;
		   if (QACnt == 0) {
			   pstmt.setString(++parmCnt, UserId);
		   }
		   if ((!Stdt.equals("") && Stdt != null) && (!Eddt.equals("") && Eddt != null)) {
			   pstmt.setString(++parmCnt, Stdt);
			   pstmt.setString(++parmCnt, Eddt);
		   }
		   
		   if (QACnt == 0) {
			   pstmt.setString(++parmCnt, UserId);
		   }
		   if ((!Stdt.equals("") && Stdt != null) && (!Eddt.equals("") && Eddt != null)) {
			   pstmt.setString(++parmCnt, Stdt);
			   pstmt.setString(++parmCnt, Eddt);
		   }
		   
		   ecamsLogger.error(((LoggableStatement)pstmt).getQueryString());
		   rs = pstmt.executeQuery();
		   while (rs.next()){
		  		rst = new HashMap<String, String>();
		  		rst.put("CC_ORDERID", rs.getString("CC_ORDERID"));
		  		rst.put("CC_REQID", rs.getString("CC_REQID"));
		  		rst.put("CC_SUBID", rs.getString("CC_SUBID"));
				rst.put("CC_DOCNUM", rs.getString("CC_DOCNUM"));
				rst.put("CC_ENDPLAN", rs.getString("CC_ENDPLAN"));
				rst.put("CC_REQSUB", rs.getString("CC_REQSUB"));
				rst.put("CC_DETAILSAYU", rs.getString("CC_DETAILSAYU"));
				rst.put("CC_STATUS", rs.getString("CC_STATUS"));
				rst.put("STARTDT", rs.getString("STARTDT"));
				rst.put("ENDDT", rs.getString("ENDDT"));
				rst.put("CC_ORDERUSER", rs.getString("CC_ORDERUSER"));
				rst.put("CC_ACTTYPE", rs.getString("CC_ACTTYPE"));

				String thirduser = "";
				String thirdusers = "";
				
				
				
				parmCnt=0;
				strQuery.setLength(0);
				strQuery.append(" select a.cc_orderid, a.cc_thirduser, a.cc_team, a.cc_check, 	\n");
				strQuery.append(" b.cm_username, c.cm_deptname, d.cm_posname									 	\n");
				strQuery.append(" from cmc0421 a, cmm0040 b, cmm0100 c, cmm0103 d						 	\n");
				strQuery.append(" where a.cc_orderid = ?									 	\n");
				strQuery.append(" and a.cc_thirduser = b.cm_userid							 	\n");
				strQuery.append(" and b.cm_position = d.cm_poscd                                \n");
				strQuery.append(" and a.cc_team = c.cm_deptcd								 	\n");
				pstmt2 = conn.prepareStatement(strQuery.toString());
				//pstmt = new LoggableStatement(conn,strQuery.toString());
				pstmt2.setString(++parmCnt, rs.getString("CC_ORDERID"));
				//ecamsLogger.error(((LoggableStatement)pstmt).getQueryString());
		        rs2 = pstmt2.executeQuery();
		        while(rs2.next()) {
		        	if(!thirdusers.equals("")){
		        		thirdusers=thirdusers+",";
		        	}
		        	
		        	thirduser = rs2.getString("cc_team")+"^"+rs2.getString("cm_deptname")+"^"+rs2.getString("cc_thirduser")+"^"+rs2.getString("cm_username")+"^"+rs2.getString("cc_check")+"^"+rs2.getString("cm_posname");
			  		thirdusers = thirdusers+thirduser;
			  		thirduser = "";
			  		
		        }
		        rs2.close();
		        pstmt2.close();
		        
		        
		        rst.put("thirdusers", thirdusers);
		        
		        String runner = "";
				String runners = "";
				parmCnt=0;
				strQuery.setLength(0);
				strQuery.append(" select a.cc_orderid, a.cc_ordersubid, a.cc_runner,a.cc_team, 				\n");
				strQuery.append(" b.cm_username,c.cm_deptname,d.cm_posname									\n");
				strQuery.append(" from cmc0450 a, cmm0040 b, cmm0100 c, cmm0103 d						 	\n");
				strQuery.append(" where a.cc_orderid = ?									 	\n");
				strQuery.append(" and a.cc_runner = b.cm_userid							 	    \n");
				strQuery.append(" and b.cm_position = d.cm_poscd                                \n");
				strQuery.append(" and a.cc_team = c.cm_deptcd								 	\n");
				pstmt2 = conn.prepareStatement(strQuery.toString());
				//pstmt2 = new LoggableStatement(conn,strQuery.toString());
				pstmt2.setString(++parmCnt, rs.getString("CC_ORDERID"));
				//ecamsLogger.error(((LoggableStatement)pstmt2).getQueryString());
		        rs2 = pstmt2.executeQuery();
		        while(rs2.next()) {
		        	if(!runners.equals("")){
		        		runners=runners+",";
		        	}
		        	
		        	runner = rs2.getString("cc_team")+"^"+rs2.getString("cm_deptname")+"^"+rs2.getString("cc_runner")+"^"+rs2.getString("cm_username")+"^"+rs2.getString("cm_posname");
		        	runners = runners+runner;
		        	runner = "";
			  		
		        }
		        rs2.close();
		        pstmt2.close();
		        
		        
		        rst.put("runners", runners);
		        
		        String fileinfo = "";
				String fileinfos = "";
				
				strQuery.setLength(0);//ISERID : R + YYYYMM + '-' + SEQ(4) : R201011-0001
				parmCnt = 0;
				strQuery.append("select a.CC_ID,a.CC_SUBID, a.CC_SUBREQ, a.CC_SEQNO, a.CC_SAVEFILE, a.CC_ATTFILE,	\n");
				strQuery.append(" a.CC_LASTDT, a.CC_EDITOR, a.CC_REQCD, B.CM_USERNAME						\n");
				strQuery.append(" from cmc1001 a, cmm0040 b													\n");
				strQuery.append(" where a.CC_ID = ?									 					\n" );
				strQuery.append(" and a.CC_EDITOR = b.cm_userid						 					\n" );
		        pstmt2 = conn.prepareStatement(strQuery.toString());
		        //pstmt2 = new LoggableStatement(conn,strQuery.toString());
				pstmt2.setString(++parmCnt, rs.getString("CC_ORDERID"));
				//ecamsLogger.error(((LoggableStatement)pstmt2).getQueryString());
		        rs2 = pstmt2.executeQuery();
	        
		        while(rs2.next()) {
		        	if(!fileinfos.equals("")){
		        		fileinfos=fileinfos+"|";
		        	}
		        	fileinfo = rs2.getString("CC_ID")+"^"+rs2.getString("CC_SUBID")+"^"+rs2.getString("CC_SUBREQ")+"^"+rs2.getString("CC_SEQNO")+"^"
			  		+rs2.getString("CC_SAVEFILE")+"^"+rs2.getString("CC_ATTFILE")+"^"+rs2.getString("CC_LASTDT")+"^"
			  		+rs2.getString("CC_EDITOR")+"^"+rs2.getString("CC_REQCD")+"^"+rs2.getString("CM_USERNAME");
			  		fileinfos = fileinfos+fileinfo;
			  		fileinfo = "";
			  		
		        }
		        rst.put("fileinfos", fileinfos);
				
		        rs2.close();
		        pstmt2.close();
				
				rsval.add(rst);
				rst = null;
		   }
	        rs.close();
	        pstmt.close();
	        conn.close();
	        
	  		rs = null;
	  		pstmt = null;
	  		conn = null;
			
			returnObjectArray = rsval.toArray();
			rsval.clear();
			rsval = null;

			return returnObjectArray;
		   
	   }catch (SQLException sqlexception) {
			sqlexception.printStackTrace();
			ecamsLogger.error("## Cmc0500.getORDERListSearch() SQLException START ##");
			ecamsLogger.error("## Error DESC : ", sqlexception);	
			ecamsLogger.error("## Cmc0500.getORDERListSearch() SQLException END ##");			
			throw sqlexception;
		} catch (Exception exception) {
			exception.printStackTrace();
			ecamsLogger.error("## Cmc0500.getORDERListSearch() Exception START ##");				
			ecamsLogger.error("## Error DESC : ", exception);	
			ecamsLogger.error("## Cmc0500.getORDERListSearch() Exception END ##");				
			throw exception;
		}finally{
			if (strQuery != null) 	strQuery = null;
			if (returnObjectArray != null)	returnObjectArray = null;
			if (rs != null)     try{rs.close();}catch (Exception ex){ex.printStackTrace();}
			if (pstmt != null)  try{pstmt.close();}catch (Exception ex2){ex2.printStackTrace();}
			if (conn != null){
				try{
					ConnectionResource.release(conn);
				}catch(Exception ex3){
					ecamsLogger.error("## Cmc0500.getORDERListSearch() connection release exception ##");
					ex3.printStackTrace();
				}
			}
		}
	   
	   
   }//end getORDERListSearch Method
    
    public Object[] getORDERList(String Userid, String Status, String ReqID, String SubID, String Stdt, String Eddt, String Selfsw) throws SQLException, Exception {
		Connection        conn        = null;
		PreparedStatement pstmt       = null;
		PreparedStatement pstmt2      = null;
		ResultSet         rs          = null;
		ResultSet         rs2         = null;
		StringBuffer      strQuery    = new StringBuffer();
		Object[] returnObjectArray    = null;
		ArrayList<HashMap<String, String>>  rsval = new ArrayList<HashMap<String, String>>();
		HashMap<String, String> rst	  = null;
		int              parmCnt      = 0;
		ConnectionContext connectionContext = new ConnectionResource();		
		
		try {
			
			conn = connectionContext.getConnection();
				
			strQuery.setLength(0);
			strQuery.append(" select A.CC_ORDERID, A.CC_REQID, A.CC_SUBID, A.CC_DOCNUM, A.CC_ENDPLAN,                               	\n");
			strQuery.append(" A.CC_REQSUB, A.CC_DETAILSAYU, A.CC_STATUS, 							   									\n");
			strQuery.append(" to_char(A.CC_STARTDT, 'yyyy/mm/dd')STARTDT, to_char(A.CC_ENDDT, 'yyyy/mm/dd') ENDDT,					   	\n");
			strQuery.append(" A.CC_ORDERUSER																			   				\n");
			strQuery.append(" from   cmc0420 a																						   	\n");
			strQuery.append(" where cc_reqid = ?																				   		\n");
//					strQuery.append(" and cc_subid = ?																				   		\n");
			strQuery.append(" and a.cc_orderuser = ?                                                                                   	\n");
			if(Status.equals("0")){
				strQuery.append(" and a.cc_status not in ('9')                                                                         	\n");
			}else if(Status.equals("9")){
				strQuery.append(" and a.cc_status in ('9') 																			   	\n");
			}
 
			if ((!Stdt.equals("") && Stdt != null) && (!Eddt.equals("") && Eddt != null)) {
				strQuery.append(" and to_char(A.CC_STARTDT,'yyyy/mm/dd') between ? and ? 												 \n");
			}
			pstmt = conn.prepareStatement(strQuery.toString());
			pstmt = new LoggableStatement(conn,strQuery.toString());
			parmCnt = 0;
//			if(!Selfsw.equals("self")){
				pstmt.setString(++parmCnt, ReqID);
//			}
			pstmt.setString(++parmCnt, Userid);
			if ((!Stdt.equals("") && Stdt != null) && (!Eddt.equals("") && Eddt != null)) {
				pstmt.setString(++parmCnt, Stdt);
				pstmt.setString(++parmCnt, Eddt);
			}
			
			ecamsLogger.error(((LoggableStatement)pstmt).getQueryString());
	        rs = pstmt.executeQuery();
	        
	        while(rs.next()) {
		  		rst = new HashMap<String, String>();
		  		rst.put("CC_ORDERID", rs.getString("CC_ORDERID"));
		  		rst.put("CC_REQID", rs.getString("CC_REQID"));
		  		rst.put("CC_SUBID", rs.getString("CC_SUBID"));
				rst.put("CC_DOCNUM", rs.getString("CC_DOCNUM"));
				rst.put("CC_ENDPLAN", rs.getString("CC_ENDPLAN"));
				rst.put("CC_REQSUB", rs.getString("CC_REQSUB"));
				rst.put("CC_DETAILSAYU", rs.getString("CC_DETAILSAYU"));
				rst.put("CC_STATUS", rs.getString("CC_STATUS"));
				rst.put("STARTDT", rs.getString("STARTDT"));
				rst.put("ENDDT", rs.getString("ENDDT"));
				rst.put("CC_ORDERUSER", rs.getString("CC_ORDERUSER"));
				
				
				
				String thirduser = "";
				String thirdusers = "";
				parmCnt=0;
				strQuery.setLength(0);
				strQuery.append(" select a.cc_orderid, a.cc_thirduser, a.cc_team, a.cc_check, 	\n");
				strQuery.append(" b.cm_username, c.cm_deptname, d.cm_posname					\n");
				strQuery.append(" from cmc0421 a, cmm0040 b, cmm0100 c ,cmm0103 d				\n");
				strQuery.append(" where a.cc_orderid = ?									 	\n");
				strQuery.append(" and a.cc_thirduser = b.cm_userid							 	\n");
				strQuery.append(" and d.cm_poscd = b.cm_position                                \n");
				strQuery.append(" and a.cc_team = c.cm_deptcd								 	\n");
				pstmt2 = conn.prepareStatement(strQuery.toString());
				//pstmt = new LoggableStatement(conn,strQuery.toString());
				pstmt2.setString(++parmCnt, rs.getString("CC_ORDERID"));
				//ecamsLogger.error(((LoggableStatement)pstmt).getQueryString());
		        rs2 = pstmt2.executeQuery();
		        while(rs2.next()) {
		        	if(!thirdusers.equals("")){
		        		thirdusers=thirdusers+",";
		        	}
		        	thirduser = rs2.getString("cc_team")+"^"+rs2.getString("cm_deptname")+"^"+rs2.getString("cc_thirduser")+"^"+rs2.getString("cm_username")+"^"+rs2.getString("cc_check")+"^"+rs2.getString("cm_posname");
			  		thirdusers = thirdusers+thirduser;
			  		thirduser = "";
			  		
		        }
		        rs2.close();
		        pstmt2.close();
		        
		        
		        rst.put("thirdusers", thirdusers);
		        
		        String runner = "";
				String runners = "";
				parmCnt=0;
				strQuery.setLength(0);
				strQuery.append(" select a.cc_orderid, a.cc_ordersubid, a.cc_runner,a.cc_team, 	\n");
				strQuery.append(" b.cm_username,c.cm_deptname, d.cm_posname									\n");
				strQuery.append(" from cmc0450 a, cmm0040 b, cmm0100 c, cmm0103 d						 	\n");
				strQuery.append(" where a.cc_orderid = ?									 	\n");
				strQuery.append(" and a.cc_runner = b.cm_userid							 	\n");
				strQuery.append(" and d.cm_poscd = b.cm_position                                \n");
				strQuery.append(" and a.cc_team = c.cm_deptcd								 	\n");
				pstmt2 = conn.prepareStatement(strQuery.toString());
				//pstmt2 = new LoggableStatement(conn,strQuery.toString());
				pstmt2.setString(++parmCnt, rs.getString("CC_ORDERID"));
				//ecamsLogger.error(((LoggableStatement)pstmt2).getQueryString());
		        rs2 = pstmt2.executeQuery();
		        while(rs2.next()) {
		        	if(!runners.equals("")){
		        		runners=runners+",";
		        	}
		        	
		        	runner = rs2.getString("cc_team")+"^"+rs2.getString("cm_deptname")+"^"+rs2.getString("cc_runner")+"^"+rs2.getString("cm_username")+"^"+rs2.getString("cm_posname");
		        	runners = runners+runner;
		        	runner = "";
			  		
		        }
		        rs2.close();
		        pstmt2.close();
		        
		        
		        rst.put("runners", runners);
		        
		        String fileinfo = "";
				String fileinfos = "";
				
				strQuery.setLength(0);//ISERID : R + YYYYMM + '-' + SEQ(4) : R201011-0001
				parmCnt = 0;
				strQuery.append("select a.CC_ID,a.CC_SUBID, a.CC_SUBREQ, a.CC_SEQNO, a.CC_SAVEFILE, a.CC_ATTFILE,	\n");
				strQuery.append(" a.CC_LASTDT, a.CC_EDITOR, a.CC_REQCD, B.CM_USERNAME						\n");
				strQuery.append(" from cmc1001 a, cmm0040 b													\n");
				strQuery.append(" where a.CC_ID = ?									 					\n" );
				strQuery.append(" and a.CC_EDITOR = b.cm_userid						 					\n" );
		        pstmt2 = conn.prepareStatement(strQuery.toString());
		        pstmt2 = new LoggableStatement(conn,strQuery.toString());
				pstmt2.setString(++parmCnt, ReqID);
				//pstmt2.setString(++parmCnt, rs.getString("CC_ORDERID"));
				ecamsLogger.error(((LoggableStatement)pstmt2).getQueryString());
		        rs2 = pstmt2.executeQuery();
	        
		        while(rs2.next()) {
		        	if(!fileinfos.equals("")){
		        		fileinfos=fileinfos+"|";
		        	}
		        	fileinfo = rs2.getString("CC_ID")+"^"+rs2.getString("CC_SUBID")+"^"+rs2.getString("CC_SUBREQ")+"^"+rs2.getString("CC_SEQNO")+"^"
			  		+rs2.getString("CC_SAVEFILE")+"^"+rs2.getString("CC_ATTFILE")+"^"+rs2.getString("CC_LASTDT")+"^"
			  		+rs2.getString("CC_EDITOR")+"^"+rs2.getString("CC_REQCD")+"^"+rs2.getString("CM_USERNAME");
			  		fileinfos = fileinfos+fileinfo;
			  		fileinfo = "";
			  		
		        }
		        rst.put("fileinfos", fileinfos);
		        rs2.close();
		        pstmt2.close();
		        
				rsval.add(rst);
				rst = null;
	        }
	        rs.close();
	        pstmt.close();
	        conn.close();
	        
	  		rs = null;
	  		pstmt = null;
	  		conn = null;
			
			returnObjectArray = rsval.toArray();
			rsval.clear();
			rsval = null;

			return returnObjectArray;
			
			
		} catch (SQLException sqlexception) {
			sqlexception.printStackTrace();
			ecamsLogger.error("## Cmc0100.getISRList() SQLException START ##");
			ecamsLogger.error("## Error DESC : ", sqlexception);	
			ecamsLogger.error("## Cmc0100.getISRList() SQLException END ##");			
			throw sqlexception;
		} catch (Exception exception) {
			exception.printStackTrace();
			ecamsLogger.error("## Cmc0100.getISRList() Exception START ##");				
			ecamsLogger.error("## Error DESC : ", exception);	
			ecamsLogger.error("## Cmc0100.getISRList() Exception END ##");				
			throw exception;
		}finally{
			if (strQuery != null) 	strQuery = null;
			if (returnObjectArray != null)	returnObjectArray = null;
			if (rs != null)     try{rs.close();}catch (Exception ex){ex.printStackTrace();}
			if (pstmt != null)  try{pstmt.close();}catch (Exception ex2){ex2.printStackTrace();}
			if (conn != null){
				try{
					ConnectionResource.release(conn);
				}catch(Exception ex3){
					ecamsLogger.error("## Cmc0100.getISRList() connection release exception ##");
					ex3.printStackTrace();
				}
			}
		}
	}//end of getISRList() method statement
    
    public String delREQinfo(HashMap<String,String> delREQ) throws SQLException, Exception{
    	Connection conn = null;
    	PreparedStatement pstmt = null;
    	ResultSet rs = null;
    	StringBuffer strQuery = new StringBuffer();
    	String status = "";
    	String result = "";
    	String orderid = "";
    	String reqid = "";
    	
    	ConnectionContext connectionContext = new ConnectionResource();
    	
    	try{
    		conn = connectionContext.getConnection();
    		conn.setAutoCommit(false);
    		
    		strQuery.setLength(0);
    		strQuery.append("select count(*) as cnt 					\n");
			strQuery.append("from cmr1000								\n");
			strQuery.append("where cr_acptno in (select cr_acptno		\n");
			strQuery.append("                    from cmr1012			\n");
			strQuery.append("                    where cr_orderid = ?)	\n");
			strQuery.append("and cr_status != '3'						\n");
    		
			pstmt = conn.prepareStatement(strQuery.toString());
	        //pstmt = new LoggableStatement(conn,strQuery.toString());
	        pstmt.setString(1, delREQ.get("delORDERinfo"));
	        //ecamsLogger.error(((LoggableStatement)pstmt).getQueryString());
	        rs = pstmt.executeQuery();
	        
	        if(rs.next()) {
	        	if(rs.getInt("cnt")>0){
	        		result="C";
	        	}
	        }
	        rs.close();
	        pstmt.close();
	        
	  		rs = null;
	  		pstmt = null;
    		
	  		if(!result.equals("C")){
	    		strQuery.setLength(0);
	    		strQuery.append(" select cc_orderid,nvl(cc_reqid,'no') as cc_reqid	\n");
	    		strQuery.append(" from cmc0420							\n");
	    		strQuery.append(" where cc_orderid = ? 					\n");
	    		
	    		pstmt = conn.prepareStatement(strQuery.toString());
	    		pstmt.setString(1, delREQ.get("delORDERinfo"));
	    		
	    		
	    		rs = pstmt.executeQuery();
	    		
	    		while(rs.next()){
	    			orderid = rs.getString("cc_orderid");
	    			reqid = rs.getString("cc_reqid");
	    		}
	    		rs.close();
	    		pstmt.close();
	    		rs = null;
	    		pstmt = null;
	    		
	    		if(!reqid.equals("no")){

	    			strQuery.setLength(0);
	        		strQuery.append(" update cmc0420 set cc_status = '3', \n");
	        		strQuery.append(" cc_enddt = sysdate 				 \n");
	        		strQuery.append(" where cc_orderid = ? 				 \n");
	        		strQuery.append(" and cc_reqid = ?                  \n");
	        		strQuery.append(" and cc_orderuser = ?				 \n");
	        		
	        		pstmt = conn.prepareStatement(strQuery.toString());
	        		pstmt.setString(1, orderid);
	        		pstmt.setString(2, reqid);
	        		pstmt.setString(3, delREQ.get("delStrUserID"));
	        		pstmt.executeUpdate();
	        		pstmt.close();
	        		
	        		strQuery.setLength(0);
	        		strQuery.append(" update cmc0410 set cc_status = '3', \n");
	        		strQuery.append(" cc_enddt = sysdate 				 \n");
	        		strQuery.append(" where cc_reqid = ?                 \n");
	        		
	        		pstmt = conn.prepareStatement(strQuery.toString());
	        		pstmt.setString(1, reqid);
	        		pstmt.executeUpdate();
	        		pstmt.close();
	        		
	        		result="Y";
	    		}else{
	    			strQuery.setLength(0);
	        		strQuery.append(" update cmc0420 set cc_status = '3', \n");
	        		strQuery.append(" cc_enddt = sysdate 				 \n");
	        		strQuery.append(" where cc_orderid = ? 				 \n");
	        		strQuery.append(" and cc_orderuser = ?				 \n");
	        		
	        		pstmt = conn.prepareStatement(strQuery.toString());
	        		pstmt = new LoggableStatement(conn,strQuery.toString());
	        		pstmt.setString(1, orderid);
	        		pstmt.setString(2, delREQ.get("delStrUserID"));
	        		ecamsLogger.error(((LoggableStatement)pstmt).getQueryString());
	        		pstmt.executeUpdate();
	        		pstmt.close();
	    			
	        		result="Y";
	    		}
	  		}
    		pstmt = null;
    		
    		conn.commit();
    		conn.close();
    		conn = null;
    		
    		return result;
    	} catch (SQLException sqlexception) {
			sqlexception.printStackTrace();
			if (rs != null)     try{rs.close();}catch (Exception ex){ex.printStackTrace();}
			if (pstmt != null)  try{pstmt.close();}catch (Exception ex2){ex2.printStackTrace();}
			if (conn != null){
				try{
					conn.rollback();
					ConnectionResource.release(conn);
				}catch(Exception ex3){
					ecamsLogger.error("## Cmc0400.delREQinfo() connection release exception ##");
					ex3.printStackTrace();
				}				
			}			
			ecamsLogger.error("## delREQinfo.setISRInfo() SQLException START ##");
			ecamsLogger.error("## Error DESC : ", sqlexception);	
			ecamsLogger.error("## delREQinfo.setISRInfo() SQLException END ##");			
			throw sqlexception;
		} catch (Exception exception) {
			exception.printStackTrace();
			if (rs != null)     try{rs.close();}catch (Exception ex){ex.printStackTrace();}
			if (pstmt != null)  try{pstmt.close();}catch (Exception ex2){ex2.printStackTrace();}
			if (conn != null){
				try{
					conn.rollback();
					ConnectionResource.release(conn);
				}catch(Exception ex3){
					ecamsLogger.error("## Cmc0400.delREQinfo() connection release exception ##");
					ex3.printStackTrace();
				}				
			}			
			ecamsLogger.error("## Cmc0400.delREQinfo() Exception START ##");				
			ecamsLogger.error("## Error DESC : ", exception);	
			ecamsLogger.error("## Cmc0400.delREQinfo() Exception END ##");				
			throw exception;
		}finally{
			if (strQuery != null) 	strQuery = null;
			if (rs != null)     try{rs.close();}catch (Exception ex){ex.printStackTrace();}
			if (pstmt != null)  try{pstmt.close();}catch (Exception ex2){ex2.printStackTrace();}
			if (conn != null){
				try{
					ConnectionResource.release(conn);
					conn = null;
				}catch(Exception ex3){
					ecamsLogger.error("## Cmc0400.delREQinfo() connection release exception ##");
					ex3.printStackTrace();
				}
			}
		}
    	
    	
    	
    }
    
    public Object[] getAttfileInfo(String Userid, String Reqid ,String Subid) throws SQLException, Exception {
		Connection        conn        = null;
		PreparedStatement pstmt       = null;
		ResultSet         rs          = null;
		PreparedStatement pstmt2       = null;
		ResultSet         rs2          = null;
		
		StringBuffer      strQuery    = new StringBuffer();
		Object[] returnObjectArray    = null;
		ArrayList<HashMap<String, String>>  rsval = new ArrayList<HashMap<String, String>>();
		HashMap<String, String> rst	  = null;
		
		ConnectionContext connectionContext = new ConnectionResource();		
		
		try {
			conn = connectionContext.getConnection();
			
			strQuery.setLength(0);
			strQuery.append("select cc_attfile, cc_savefile from cmc1001 where cc_editor = ? and cc_reqid = ? and cc_subid = ? \n"); 
			
			
			pstmt = conn.prepareStatement(strQuery.toString());
			
			pstmt.setString(1, Userid);
			pstmt.setString(2, Reqid);
			pstmt.setString(3, Subid);
			
			rs = pstmt.executeQuery();
			
			while(rs.next()){
				rst = new HashMap<String, String>();
				rst.put("cc_attfile", rs.getString("cc_attfile"));
				rst.put("cc_savefile", rs.getString("cc_savefile"));
				rsval.add(rst);
				
				rst = null;
			}
	        rs.close();
	        pstmt.close();
	        conn.close();
	        
	  		rs = null;
	  		pstmt = null;
	  		conn = null;
			
			returnObjectArray = rsval.toArray();
			rsval.clear();
			rsval = null;
				
			return returnObjectArray;
			
			
		} catch (SQLException sqlexception) {
			sqlexception.printStackTrace();
			ecamsLogger.error("## Cmc0500.getAttfileInfo() SQLException START ##");
			ecamsLogger.error("## Error DESC : ", sqlexception);	
			ecamsLogger.error("## Cmc0500.getAttfileInfo() SQLException END ##");			
			throw sqlexception;
		} catch (Exception exception) {
			exception.printStackTrace();
			ecamsLogger.error("## Cmc0500.getAttfileInfo() Exception START ##");				
			ecamsLogger.error("## Error DESC : ", exception);	
			ecamsLogger.error("## Cmc0500.getAttfileInfo() Exception END ##");				
			throw exception;
		}finally{
			if (strQuery != null) 	strQuery = null;
			if (returnObjectArray != null)	returnObjectArray = null;
			if (rs != null)     try{rs.close();}catch (Exception ex){ex.printStackTrace();}
			if (pstmt != null)  try{pstmt.close();}catch (Exception ex2){ex2.printStackTrace();}
			if (conn != null){
				try{
					ConnectionResource.release(conn);
				}catch(Exception ex3){
					ecamsLogger.error("## Cmc0500.getAttfileInfo() connection release exception ##");
					ex3.printStackTrace();
				}
			}
		}
	}//end of getISRList() method statement
    
    public Object[] getReqInfo(String Userid, String OrderId) throws SQLException, Exception {
		Connection        conn        = null;
		PreparedStatement pstmt       = null;
		ResultSet         rs          = null;
		PreparedStatement pstmt2       = null;
		ResultSet         rs2          = null;
		
		StringBuffer      strQuery    = new StringBuffer();
		Object[] returnObjectArray    = null;
		ArrayList<HashMap<String, String>>  rsval = new ArrayList<HashMap<String, String>>();
		HashMap<String, String> rst	  = null;
		
		ConnectionContext connectionContext = new ConnectionResource();		
		
		try {
			conn = connectionContext.getConnection();
			
			strQuery.setLength(0);
			strQuery.append("select a.cc_orderid, a.cc_reqid, a.cc_subid, a.cc_team, a.cc_docnum, a.cc_endplan,			\n"); 
			strQuery.append("a.cc_chk1, a.cc_chk2, a.cc_reqsub, a.cc_detailsayu, a.cc_runner, a.cc_thirduser,			\n");
            strQuery.append("a.cc_status, a.cc_startdt, a.cc_enddt, a.cc_orderuser, b.cm_username, c.cm_deptname		\n");
            strQuery.append("from   cmc0420 a , cmm0040 b, cmm0100 c  \n");
			strQuery.append("	where  a.cc_orderid = ? \n");
			strQuery.append("	and    b.cm_userid = ?   \n"); 
			strQuery.append("	and    b.cm_userid = a.cc_runner \n");
			strQuery.append("	and    b.cm_project = c.cm_deptcd \n");
			
			
			pstmt = conn.prepareStatement(strQuery.toString());
			
			pstmt.setString(1, OrderId);
			pstmt.setString(2, Userid);
			
			rs = pstmt.executeQuery();
			
				rs.next();
				rst = new HashMap<String, String>();
				rst.put("cc_orderid", rs.getString("cc_orderid"));
				rst.put("cc_reqid", rs.getString("cc_reqid"));
				rst.put("cc_subid", rs.getString("cc_subid"));
				rst.put("cc_team", rs.getString("cc_team"));
				rst.put("cc_docnum", rs.getString("cc_docnum"));
				rst.put("cc_endplan", rs.getString("cc_endplan"));
				rst.put("cc_chk1", rs.getString("cc_chk1"));
				rst.put("cc_chk2", rs.getString("cc_chk2"));
				rst.put("cc_thirduser", rs.getString("cc_thirduser"));
				rst.put("cc_reqsub", rs.getString("cc_reqsub"));
				rst.put("cc_detailsayu", rs.getString("cc_detailsayu"));
				rst.put("cc_runner", rs.getString("cc_runner"));
				rst.put("cc_status", rs.getString("cc_status"));
				rst.put("cc_startdt", rs.getString("cc_startdt"));
				rst.put("cc_enddt", rs.getString("cc_enddt"));
				rst.put("cc_orderuser", rs.getString("cc_orderuser"));
				rst.put("cm_username", rs.getString("cm_username"));
				rst.put("cm_deptname", rs.getString("cm_deptname"));
				
				strQuery.setLength(0);
				strQuery.append("select cm_username from cmm0040 where cm_userid = ? \n");
				pstmt2 = conn.prepareStatement(strQuery.toString());
				pstmt2.setString(1,rs.getString("cc_thirduser"));
				
				rs2 = pstmt2.executeQuery();
				rs2.next();
				rst.put("cc_thirdusername", rs2.getString("cm_username"));
				
				rs2.close();
		        pstmt2.close();
		        
				rsval.add(rst);
				
				rst = null;
	        
	        rs.close();
	        pstmt.close();
	        conn.close();
	        
	  		rs = null;
	  		pstmt = null;
	  		conn = null;
			
			returnObjectArray = rsval.toArray();
			rsval.clear();
			rsval = null;
				
			return returnObjectArray;
			
			
		} catch (SQLException sqlexception) {
			sqlexception.printStackTrace();
			ecamsLogger.error("## Cmc0500.getReqInfo() SQLException START ##");
			ecamsLogger.error("## Error DESC : ", sqlexception);	
			ecamsLogger.error("## Cmc0500.getReqInfo() SQLException END ##");			
			throw sqlexception;
		} catch (Exception exception) {
			exception.printStackTrace();
			ecamsLogger.error("## Cmc0500.getReqInfo() Exception START ##");				
			ecamsLogger.error("## Error DESC : ", exception);	
			ecamsLogger.error("## Cmc0500.getReqInfo() Exception END ##");				
			throw exception;
		}finally{
			if (strQuery != null) 	strQuery = null;
			if (returnObjectArray != null)	returnObjectArray = null;
			if (rs != null)     try{rs.close();}catch (Exception ex){ex.printStackTrace();}
			if (pstmt != null)  try{pstmt.close();}catch (Exception ex2){ex2.printStackTrace();}
			if (conn != null){
				try{
					ConnectionResource.release(conn);
				}catch(Exception ex3){
					ecamsLogger.error("## Cmc0500.getReqInfo() connection release exception ##");
					ex3.printStackTrace();
				}
			}
		}
	}//end of getISRList() method statement
    
    public Object[] OrderSelect(String Userid, String SelMsg) throws SQLException, Exception {
		Connection        conn        = null;
		PreparedStatement pstmt       = null;
		PreparedStatement pstmt2      = null;
		ResultSet         rs          = null;
		ResultSet         rs2         = null;
		StringBuffer      strQuery    = new StringBuffer();
		Object[] returnObjectArray    = null;
		ArrayList<HashMap<String, String>>  rsval = new ArrayList<HashMap<String, String>>();
		HashMap<String, String> rst	  = null;
		int              parmCnt      = 0;
		String            strSelMsg   = "";
		ConnectionContext connectionContext = new ConnectionResource();		
		
		try {
			
			conn = connectionContext.getConnection();
			
			if (SelMsg != "" && SelMsg != null) {
				if (SelMsg.toUpperCase().equals("ALL")){
					strSelMsg = "전체";
				}
				else if(SelMsg.toUpperCase().equals("SEL")){
					strSelMsg = "선택하세요";
				}
				else{
					strSelMsg   = "";
				}
			}
			
			strQuery.setLength(0);//ISERID : R + YYYYMM + '-' + SEQ(4) : R201011-0001
			strQuery.append("select a.CC_ORDERID,a.CC_REQID,a.CC_SUBID,a.CC_TEAM,a.CC_DOCNUM,						\n");
			strQuery.append("       a.CC_ENDPLAN,a.CC_CHK1,a.CC_CHK2,a.CC_THIRDUSER,a.CC_REQSUB,					\n");
			strQuery.append("       a.CC_DETAILSAYU,a.CC_RUNNER,a.CC_STATUS,a.CC_STARTDT,a.CC_ENDDT,				\n");
			strQuery.append("   	a.CC_ORDERUSER, b.cm_username								 					\n");
			strQuery.append("from cmc0420 a, cmm0040 b												 				\n");
			strQuery.append("where cc_runner = ?																	\n");
			strQuery.append("and cc_status != '9'																		\n");
			strQuery.append("and a.cc_runner = b.cm_userid															\n");
			strQuery.append("and cc_team in (select cm_project from cmm0040 where cm_userid=?)						\n");
	        pstmt = conn.prepareStatement(strQuery.toString());
	        //pstmt = new LoggableStatement(conn,strQuery.toString());
	        pstmt.setString(++parmCnt, Userid);
	        pstmt.setString(++parmCnt, Userid);
	        //ecamsLogger.error(((LoggableStatement)pstmt).getQueryString());
	        rs = pstmt.executeQuery();
	        
	        while(rs.next()) {
	        	if (rs.getRow() ==1 && strSelMsg != "" && !strSelMsg.equals("") && strSelMsg.length() > 0) {
					   rst = new HashMap<String, String>();
					   rst.put("CC_ORDERID", "0000");
					   rst.put("CC_REQID", "0");
					   rst.put("OrderID", strSelMsg);
					   rst.put("cd_chgdet", "");
					   rst.put("CC_SUBID", "0");
					   rst.put("CC_TEAM", "");
					   rst.put("CC_DOCNUM", "");
					   rst.put("CC_ENDPLAN", "");
					   rst.put("CC_CHK1", "");
					   rst.put("CC_CHK2", "");
					   rst.put("CC_THIRDUSER", "");
					   rst.put("CC_REQSUB", "");
					   rst.put("CC_DETAILSAYU", "");
					   rst.put("CC_RUNNER", "");
					   rst.put("CC_STATUS", "");
					   rst.put("CC_STARTDT", "");
					   rst.put("CC_ENDDT", "");
					   rst.put("CC_ORDERUSER", "");
					   rst.put("cm_username", "");
					   rsval.add(rst);   
					   rst = null;
					}
		  		rst = new HashMap<String, String>();
		  		String OrderID = "";
		  		
		  		OrderID="["+rs.getString("CC_ORDERID")+"]:"+rs.getString("CC_REQSUB");
		  		
		  		rst.put("CC_ORDERID", rs.getString("CC_ORDERID"));
		  		rst.put("OrderID", OrderID);
		  		rst.put("CC_REQID", rs.getString("CC_REQID"));
		  		rst.put("CC_SUBID", rs.getString("CC_SUBID"));
		  		rst.put("CC_TEAM", rs.getString("CC_TEAM"));
		  		rst.put("CC_DOCNUM", rs.getString("CC_DOCNUM"));
		  		rst.put("CC_ENDPLAN", rs.getString("CC_ENDPLAN"));
				rst.put("CC_CHK1", rs.getString("CC_CHK1"));
				rst.put("CC_CHK2", rs.getString("CC_CHK2"));
				rst.put("CC_THIRDUSER", rs.getString("CC_THIRDUSER"));
				rst.put("CC_REQSUB", rs.getString("CC_REQSUB"));
				rst.put("CC_DETAILSAYU", rs.getString("CC_DETAILSAYU"));
				rst.put("CC_RUNNER", rs.getString("CC_RUNNER"));
				rst.put("CC_STATUS", rs.getString("CC_STATUS"));
				rst.put("CC_STARTDT", rs.getString("CC_STARTDT"));
				rst.put("CC_ENDDT", rs.getString("CC_ENDDT"));
				rst.put("CC_ORDERUSER", rs.getString("CC_ORDERUSER"));
				rst.put("cm_username", rs.getString("cm_username"));

				rsval.add(rst);
				rst = null;
	        }
	        rs.close();
	        pstmt.close();
	        conn.close();
	        
	  		rs = null;
	  		pstmt = null;
	  		conn = null;
			
			returnObjectArray = rsval.toArray();
			rsval.clear();
			rsval = null;

			return returnObjectArray;
			
			
		} catch (SQLException sqlexception) {
			sqlexception.printStackTrace();
			ecamsLogger.error("## Cmc0100.getISRList() SQLException START ##");
			ecamsLogger.error("## Error DESC : ", sqlexception);	
			ecamsLogger.error("## Cmc0100.getISRList() SQLException END ##");			
			throw sqlexception;
		} catch (Exception exception) {
			exception.printStackTrace();
			ecamsLogger.error("## Cmc0100.getISRList() Exception START ##");				
			ecamsLogger.error("## Error DESC : ", exception);	
			ecamsLogger.error("## Cmc0100.getISRList() Exception END ##");				
			throw exception;
		}finally{
			if (strQuery != null) 	strQuery = null;
			if (returnObjectArray != null)	returnObjectArray = null;
			if (rs != null)     try{rs.close();}catch (Exception ex){ex.printStackTrace();}
			if (pstmt != null)  try{pstmt.close();}catch (Exception ex2){ex2.printStackTrace();}
			if (conn != null){
				try{
					ConnectionResource.release(conn);
				}catch(Exception ex3){
					ecamsLogger.error("## Cmc0100.getISRList() connection release exception ##");
					ex3.printStackTrace();
				}
			}
		}
	}//end of getISRList() method statement
    
    public Object[] OrderSelect2(String Userid) throws SQLException, Exception {
		Connection        conn        = null;
		PreparedStatement pstmt       = null;
		PreparedStatement pstmt2      = null;
		ResultSet         rs          = null;
		ResultSet         rs2         = null;
		StringBuffer      strQuery    = new StringBuffer();
		Object[] returnObjectArray    = null;
		ArrayList<HashMap<String, String>>  rsval = new ArrayList<HashMap<String, String>>();
		HashMap<String, String> rst	  = null;
		int              parmCnt      = 0;
		String            strSelMsg   = "";
		ConnectionContext connectionContext = new ConnectionResource();		
		
		try {
			
			conn = connectionContext.getConnection();
			
			strSelMsg = "선택하세요";
			
			strQuery.setLength(0);//ISERID : R + YYYYMM + '-' + SEQ(4) : R201011-0001
			strQuery.append("select a.CC_ORDERID,a.CC_REQID,a.CC_SUBID,c.CC_TEAM,a.CC_DOCNUM,						\n");
			strQuery.append("       a.CC_ENDPLAN,a.CC_REQSUB,														\n");
			strQuery.append("       a.CC_DETAILSAYU,c.CC_RUNNER,a.CC_STATUS,a.CC_STARTDT,a.CC_ENDDT,				\n");
			strQuery.append("   	a.CC_ORDERUSER, b.cm_username, TRUNC(SYSDATE) - TRUNC(to_date(a.CC_ENDPLAN, 'YYYY/MM/DD')) NOWDATE	\n");
			strQuery.append("from cmc0420 a, cmm0040 b, cmc0450 c									 				\n");
			strQuery.append("where c.cc_runner = ?																	\n");
			strQuery.append("and a.cc_status not in ('0','9','3')													\n");
			strQuery.append("and c.cc_runner = b.cm_userid															\n");
			strQuery.append("and a.CC_ORDERID = c.CC_ORDERID														\n");
			strQuery.append("order by a.CC_STARTDT desc    						 								    \n");
			//strQuery.append("and c.cc_team in (select cm_project from cmm0040 where cm_userid=?)					\n");
	        pstmt = conn.prepareStatement(strQuery.toString());
	        //pstmt = new LoggableStatement(conn,strQuery.toString());
	        pstmt.setString(++parmCnt, Userid);
	        //pstmt.setString(++parmCnt, Userid);
	        //ecamsLogger.error(((LoggableStatement)pstmt).getQueryString());
	        rs = pstmt.executeQuery();
	        
	        while(rs.next()) {
		  		rst = new HashMap<String, String>();
		  		String OrderID = "";
		  		
		  		OrderID="["+rs.getString("CC_ORDERID")+"]:"+rs.getString("CC_REQSUB");
		  		
		  		rst.put("CC_ORDERID", rs.getString("CC_ORDERID"));
		  		rst.put("OrderID", OrderID);
		  		rst.put("CC_REQID", rs.getString("CC_REQID"));
		  		rst.put("CC_SUBID", rs.getString("CC_SUBID"));
		  		rst.put("CC_TEAM", rs.getString("CC_TEAM"));
		  		rst.put("CC_DOCNUM", rs.getString("CC_DOCNUM"));
		  		rst.put("CC_ENDPLAN", rs.getString("CC_ENDPLAN"));
				rst.put("CC_REQSUB", rs.getString("CC_REQSUB"));
				rst.put("CC_DETAILSAYU", rs.getString("CC_DETAILSAYU"));
				rst.put("CC_RUNNER", rs.getString("CC_RUNNER"));
				rst.put("CC_STATUS", rs.getString("CC_STATUS"));
				rst.put("CC_STARTDT", rs.getString("CC_STARTDT"));
				rst.put("CC_ENDDT", rs.getString("CC_ENDDT"));
				rst.put("CC_ORDERUSER", rs.getString("CC_ORDERUSER"));
				rst.put("cm_username", rs.getString("cm_username"));
// 2016.03.17 추가 
                if ( rs.getInt("NOWDATE") > 0 ) {
    				rst.put("CC_NOWDATE", "1");
                	
                } else {
    				rst.put("CC_NOWDATE", "0");

                }
                
//                System.out.println("11111111111" + rs.getInt("NOWDATE"));
				rsval.add(rst);
				rst = null;
	        }
	        rs.close();
	        pstmt.close();
	        conn.close();
	        
	  		rs = null;
	  		pstmt = null;
	  		conn = null;
			
			returnObjectArray = rsval.toArray();
			rsval.clear();
			rsval = null;

			return returnObjectArray;
			
			
		} catch (SQLException sqlexception) {
			sqlexception.printStackTrace();
			ecamsLogger.error("## Cmc0100.getISRList() SQLException START ##");
			ecamsLogger.error("## Error DESC : ", sqlexception);	
			ecamsLogger.error("## Cmc0100.getISRList() SQLException END ##");			
			throw sqlexception;
		} catch (Exception exception) {
			exception.printStackTrace();
			ecamsLogger.error("## Cmc0100.getISRList() Exception START ##");				
			ecamsLogger.error("## Error DESC : ", exception);	
			ecamsLogger.error("## Cmc0100.getISRList() Exception END ##");				
			throw exception;
		}finally{
			if (strQuery != null) 	strQuery = null;
			if (returnObjectArray != null)	returnObjectArray = null;
			if (rs != null)     try{rs.close();}catch (Exception ex){ex.printStackTrace();}
			if (pstmt != null)  try{pstmt.close();}catch (Exception ex2){ex2.printStackTrace();}
			if (conn != null){
				try{
					ConnectionResource.release(conn);
				}catch(Exception ex3){
					ecamsLogger.error("## Cmc0100.getISRList() connection release exception ##");
					ex3.printStackTrace();
				}
			} 
		}
	}//end of getISRList() method statement
    
    
    public Object[] getOrderInfo(String OrderId) throws SQLException, Exception {
		Connection        conn        = null;
		PreparedStatement pstmt       = null;
		PreparedStatement pstmt2      = null;
		ResultSet         rs          = null;
		
		StringBuffer      strQuery    = new StringBuffer();
		Object[] returnObjectArray    = null;
		ArrayList<HashMap<String, String>>  rsval = new ArrayList<HashMap<String, String>>();
		HashMap<String, String> rst	  = null;
		ConnectionContext connectionContext = new ConnectionResource();		
		
		try {
			
			conn = connectionContext.getConnection();
			
			
			strQuery.setLength(0);
		    strQuery.append("select a.cc_orderid, a.cc_reqid, a.cc_docnum, a.cc_endplan, a.cc_thirduser, 	b.cm_username,				\n");
			strQuery.append("   a.cc_startdt, a.cc_enddt, a.cc_orderuser, a.cc_reqsub, a.cc_detailsayu, a.cc_status			\n");
			strQuery.append("from   cmc0420 a, cmm0040 b																				\n");
			strQuery.append("	where  a.cc_orderid =?	 and a.cc_orderuser = b.cm_userid																	\n");
			pstmt = conn.prepareStatement(strQuery.toString());
	        //pstmt = new LoggableStatement(conn,strQuery.toString());
	        pstmt.setString(1, OrderId);
	        //ecamsLogger.error(((LoggableStatement)pstmt).getQueryString());
	        rs = pstmt.executeQuery();
	        while(rs.next()) {
	        	rst = new HashMap<String, String>();
		  		rst.put("cc_orderid", rs.getString("cc_orderid"));
		  		rst.put("cc_reqid", rs.getString("cc_reqid"));
		  		rst.put("cc_docnum", rs.getString("cc_docnum"));
		  		rst.put("cc_endplan", rs.getString("cc_endplan"));
		  		rst.put("cc_thirduser", rs.getString("cc_thirduser"));
		  		rst.put("cc_reqsub", rs.getString("cc_reqsub"));
		  		rst.put("cc_detailsayu", rs.getString("cc_detailsayu"));
		  		rst.put("cc_status", rs.getString("cc_status"));
		  		rst.put("cc_startdt", rs.getString("cc_startdt"));
		  		rst.put("cc_enddt", rs.getString("cc_enddt"));
		  		rst.put("cc_orderuser", rs.getString("cc_orderuser"));
		  		rst.put("cm_username", rs.getString("cm_username"));
		  		rsval.add(rst);
				rst = null;
	        }
	        rs.close();
	        pstmt.close();
	        conn.close();
	        
	  		rs = null;
	  		pstmt = null;
	  		conn = null;
			
			returnObjectArray = rsval.toArray();
			rsval.clear();
			rsval = null;

			return returnObjectArray;
			
			
		} catch (SQLException sqlexception) {
			sqlexception.printStackTrace();
			ecamsLogger.error("## Cmc0500.getOrderInfo() SQLException START ##");
			ecamsLogger.error("## Error DESC : ", sqlexception);	
			ecamsLogger.error("## Cmc0500.getOrderInfo() SQLException END ##");			
			throw sqlexception;
		} catch (Exception exception) {
			exception.printStackTrace();
			ecamsLogger.error("## Cmc0500.getOrderInfo() Exception START ##");				
			ecamsLogger.error("## Error DESC : ", exception);	
			ecamsLogger.error("## Cmc0500.getOrderInfo() Exception END ##");				
			throw exception;
		}finally{
			if (strQuery != null) 	strQuery = null;
			if (returnObjectArray != null)	returnObjectArray = null;
			if (rs != null)     try{rs.close();}catch (Exception ex){ex.printStackTrace();}
			if (pstmt != null)  try{pstmt.close();}catch (Exception ex2){ex2.printStackTrace();}
			if (conn != null){
				try{
					ConnectionResource.release(conn);
				}catch(Exception ex3){
					ecamsLogger.error("## Cmc0500.getOrderInfo() connection release exception ##");
					ex3.printStackTrace();
				}
			}
		}
	}//end of getOrderInfo() method statement
    
    public Object[] getOrderRunners(String OrderId) throws SQLException, Exception {
		Connection        conn        = null;
		PreparedStatement pstmt       = null;
		PreparedStatement pstmt2      = null;
		ResultSet         rs          = null;
		
		StringBuffer      strQuery    = new StringBuffer();
		Object[] returnObjectArray    = null;
		ArrayList<HashMap<String, String>>  rsval = new ArrayList<HashMap<String, String>>();
		HashMap<String, String> rst	  = null;
		ConnectionContext connectionContext = new ConnectionResource();		
		
		try {
			
			conn = connectionContext.getConnection();
			
			
			strQuery.setLength(0);
			strQuery.append("select a.cc_orderid, b.cc_ordersubid, b.cc_runner,											\n");
			strQuery.append(" c.cm_username, b.cc_team, d.cm_deptname													\n");
			strQuery.append("from   cmc0420 a, cmc0450 b, cmm0040 c, cmm0100 d											\n");
			strQuery.append("where  a.cc_orderid = ?																	\n");
			strQuery.append("and    a.cc_orderid = b.cc_orderid															\n");
			strQuery.append("and    b.cc_runner = c.cm_userid															\n");
			strQuery.append("and    b.cc_team = d.cm_deptcd 															\n");

			pstmt = conn.prepareStatement(strQuery.toString());
	        //pstmt = new LoggableStatement(conn,strQuery.toString());
	        pstmt.setString(1, OrderId);
	        //ecamsLogger.error(((LoggableStatement)pstmt).getQueryString());
	        rs = pstmt.executeQuery();
	        
	        while(rs.next()) {
		  		rst = new HashMap<String, String>();
		  		rst.put("cc_orderid", rs.getString("cc_orderid"));
		  		rst.put("cc_ordersubid", rs.getString("cc_ordersubid"));
		  		rst.put("cc_runner", rs.getString("cc_runner"));
		  		rst.put("cm_username", rs.getString("cm_username"));
		  		rst.put("cc_team", rs.getString("cc_team"));
		  		rst.put("cm_deptname", rs.getString("cm_deptname"));
		 		rsval.add(rst);
				rst = null;
	        }
	        rs.close();
	        pstmt.close();
	        conn.close();
	        
	  		rs = null;
	  		pstmt = null;
	  		conn = null;
			
			returnObjectArray = rsval.toArray();
			rsval.clear();
			rsval = null;

			return returnObjectArray;
			
			
		} catch (SQLException sqlexception) {
			sqlexception.printStackTrace();
			ecamsLogger.error("## Cmc0500.getOrderRunners() SQLException START ##");
			ecamsLogger.error("## Error DESC : ", sqlexception);	
			ecamsLogger.error("## Cmc0500.getOrderRunners() SQLException END ##");			
			throw sqlexception;
		} catch (Exception exception) {
			exception.printStackTrace();
			ecamsLogger.error("## Cmc0500.getOrderRunners() Exception START ##");				
			ecamsLogger.error("## Error DESC : ", exception);	
			ecamsLogger.error("## Cmc0500.getOrderRunners() Exception END ##");				
			throw exception;
		}finally{
			if (strQuery != null) 	strQuery = null;
			if (returnObjectArray != null)	returnObjectArray = null;
			if (rs != null)     try{rs.close();}catch (Exception ex){ex.printStackTrace();}
			if (pstmt != null)  try{pstmt.close();}catch (Exception ex2){ex2.printStackTrace();}
			if (conn != null){
				try{
					ConnectionResource.release(conn);
				}catch(Exception ex3){
					ecamsLogger.error("## Cmc0500.getOrderRunners() connection release exception ##");
					ex3.printStackTrace();
				}
			}
		}
	}//end of getOrderInfo() method statement
   
    public Object[] getOrderThirds(String OrderId) throws SQLException, Exception {
		Connection        conn        = null;
		PreparedStatement pstmt       = null;
		PreparedStatement pstmt2      = null;
		ResultSet         rs          = null;
		
		StringBuffer      strQuery    = new StringBuffer();
		Object[] returnObjectArray    = null;
		ArrayList<HashMap<String, String>>  rsval = new ArrayList<HashMap<String, String>>();
		HashMap<String, String> rst	  = null;
		ConnectionContext connectionContext = new ConnectionResource();		
		
		try {
			
			conn = connectionContext.getConnection();
			
			
			strQuery.setLength(0);
		
			strQuery.append("select a.cc_orderid,  b.cc_thirduser,  c.cm_username,			\n");
			strQuery.append("	    b.cc_team, d.cm_deptname, b.cc_check								\n");
			strQuery.append("	from   cmc0420 a, cmc0421 b, cmm0040 c, cmm0100 d			\n");
			strQuery.append("		where  a.cc_orderid = ?									\n");
			strQuery.append("		and    a.cc_orderid = b.cc_orderid						\n");
			strQuery.append("		and    b.cc_thirduser = c.cm_userid						\n");
			strQuery.append("		and    b.cc_team = d.cm_deptcd 							\n");
			
			
			pstmt = conn.prepareStatement(strQuery.toString());
	        //pstmt = new LoggableStatement(conn,strQuery.toString());
	        pstmt.setString(1, OrderId);
	        //ecamsLogger.error(((LoggableStatement)pstmt).getQueryString());
	        rs = pstmt.executeQuery();
	        
	        while(rs.next()) {
		  		rst = new HashMap<String, String>();
		  		rst.put("cc_orderid", rs.getString("cc_orderid"));
		  		rst.put("cc_thirduser", rs.getString("cc_thirduser"));
		  		rst.put("cm_username", rs.getString("cm_username"));
		  		rst.put("cc_team", rs.getString("cc_team"));
		  		rst.put("cm_deptname", rs.getString("cm_deptname"));
		  		rst.put("cc_check", rs.getString("cc_check"));
		 		rsval.add(rst);
				rst = null;
	        }
	        rs.close();
	        pstmt.close();
	        conn.close();
	        
	  		rs = null;
	  		pstmt = null;
	  		conn = null;
			
			returnObjectArray = rsval.toArray();
			rsval.clear();
			rsval = null;

			return returnObjectArray;
			
			
		} catch (SQLException sqlexception) {
			sqlexception.printStackTrace();
			ecamsLogger.error("## Cmc0500.getOrderThirds() SQLException START ##");
			ecamsLogger.error("## Error DESC : ", sqlexception);	
			ecamsLogger.error("## Cmc0500.getOrderThirds() SQLException END ##");			
			throw sqlexception;
		} catch (Exception exception) {
			exception.printStackTrace();
			ecamsLogger.error("## Cmc0500.getOrderThirds() Exception START ##");				
			ecamsLogger.error("## Error DESC : ", exception);	
			ecamsLogger.error("## Cmc0500.getOrderThirds() Exception END ##");				
			throw exception;
		}finally{
			if (strQuery != null) 	strQuery = null;
			if (returnObjectArray != null)	returnObjectArray = null;
			if (rs != null)     try{rs.close();}catch (Exception ex){ex.printStackTrace();}
			if (pstmt != null)  try{pstmt.close();}catch (Exception ex2){ex2.printStackTrace();}
			if (conn != null){
				try{
					ConnectionResource.release(conn);
				}catch(Exception ex3){
					ecamsLogger.error("## Cmc0500.getOrderThirds() connection release exception ##");
					ex3.printStackTrace();
				}
			}
		}
	}//end of getOrderInfo() method statement
    
    public String statusChk(String userID, String orderID, String reqID) throws SQLException, Exception{
    	Connection conn = null;
    	PreparedStatement pstmt = null;
    	StringBuffer strQuery = new StringBuffer();
    	String result = "Y"; // 업데이트 결과
    	String count = "";
    	ResultSet         rs          = null;
    	
    	ConnectionContext connectionContext = new ConnectionResource();	
    	
    	try{
    		conn = connectionContext.getConnection();
    		
    		strQuery.setLength(0);
    		strQuery.append(" select count(b.cr_acptno) cnt		\n");
    		strQuery.append(" from cmr1012 a, cmr1000 b      	\n");
    		strQuery.append(" where b.cr_acptno = a.cr_acptno	\n");
    		strQuery.append(" and a.cr_orderid = ?				\n");
    		pstmt = conn.prepareStatement(strQuery.toString());
    		//pstmt = new LoggableStatement(conn,strQuery.toString());
    		pstmt.setString(1, orderID);
    		//ecamsLogger.error(((LoggableStatement)pstmt).getQueryString());
    		rs = pstmt.executeQuery();
    		if (rs.next()){
    			if (rs.getInt("cnt") == 0 ){
        			result="C";    				
    			}

    		}
    		rs.close();
    		pstmt.close();
    		conn.close();
    		
    		pstmt = null;
    		conn = null;
	  		
    		return result;
    		
    	} catch (SQLException sqlexception) {
			sqlexception.printStackTrace();
			ecamsLogger.error("## Cmc0500.statusUpdt() SQLException START ##");
			ecamsLogger.error("## Error DESC : ", sqlexception);	
			ecamsLogger.error("## Cmc0500.statusUpdt() SQLException END ##");			
			throw sqlexception;
		} catch (Exception exception) {
			exception.printStackTrace();
			ecamsLogger.error("## Cmc0500.statusUpdt() Exception START ##");				
			ecamsLogger.error("## Error DESC : ", exception);	
			ecamsLogger.error("## Cmc0500.statusUpdt() Exception END ##");				
			throw exception;
		}finally{
			if (strQuery != null) 	strQuery = null;
			if (pstmt != null)  try{pstmt.close();}catch (Exception ex2){ex2.printStackTrace();}
			if (conn != null){
				try{
					ConnectionResource.release(conn);
				}catch(Exception ex3){
					ecamsLogger.error("## Cmc0500.statusUpdt() connection release exception ##");
					ex3.printStackTrace();
				}
			}
		}
   
    }
    
    
    public Object[] statusUpdt(String userID, String orderID, String reqID) throws SQLException, Exception{
    	Connection conn = null;
    	PreparedStatement pstmt = null;
    	StringBuffer strQuery = new StringBuffer();
    	String result = "Y"; // 업데이트 결과
    	String count = "";
    	Object[] returnObjectArray    = null;
		ArrayList<HashMap<String, String>>  rsval = new ArrayList<HashMap<String, String>>();
		HashMap<String, String> rst	  = null;
    	ResultSet         rs          = null;
    	
    	ConnectionContext connectionContext = new ConnectionResource();	
    	
    	try{
    		conn = connectionContext.getConnection();
    		
    		strQuery.setLength(0);
    		strQuery.append("select count(*) as cnt 					\n");
			strQuery.append("from cmr1000								\n");
			strQuery.append("where cr_acptno in (select cr_acptno		\n");
			strQuery.append("                    from cmr1012			\n");
			strQuery.append("                    where cr_orderid = ?)	\n");
			strQuery.append("and cr_status <> '9'						\n");
			strQuery.append("and cr_status <> '3'                       \n");
			
			pstmt = conn.prepareStatement(strQuery.toString());
	        //pstmt = new LoggableStatement(conn,strQuery.toString());
	        pstmt.setString(1, orderID);
	        //ecamsLogger.error(((LoggableStatement)pstmt).getQueryString());
	        rs = pstmt.executeQuery();
	        
	        if(rs.next()) {
	        	if(rs.getInt("cnt")>0){
	        		result="C";
	        	}
	        }
	        rs.close();
	        pstmt.close();
	        
	  		rs = null;
	  		pstmt = null;
	  		
	  		if(result.equals("C")){
	  			strQuery.setLength(0);
	  			strQuery.append("select b.cr_acptno,b.cr_itemid,c.cr_rsrcname, d.cm_username\n");
	  			strQuery.append("from cmr1000 a, cmr1012 b, cmr0020 c, cmm0040 d			\n");
	  			strQuery.append("where b.cr_orderid = ?										\n");
	  			strQuery.append("and b.cr_acptno = a.cr_acptno								\n");
	  			strQuery.append("and a.cr_status <> '9' and a.cr_status <> '3'				\n");
	  			strQuery.append("and b.cr_itemid = c.cr_itemid								\n");
	  			strQuery.append("and a.cr_editor = d.cm_userid								\n");
	  			pstmt = conn.prepareStatement(strQuery.toString());
	  			pstmt.setString(1, orderID);
	  			rs = pstmt.executeQuery();
	  			
	  			if(rs.next()) {
	  				
	  				rst = new HashMap<String, String>();
			  		rst.put("cr_acptno", rs.getString("cr_acptno"));
			  		rst.put("cr_itemid", rs.getString("cr_itemid"));
			  		rst.put("cr_rsrcname", rs.getString("cr_rsrcname"));
			  		rst.put("cm_username", rs.getString("cm_username"));
			 		rsval.add(rst);
	  				
	  			}
	  			
	  			rs.close();
		        pstmt.close();
		        
		        rs = null;
		  		pstmt = null;
	  		}
	  		
//    		strQuery.setLength(0);
//    		strQuery.append(" select count(b.cr_acptno) cnt		\n");
//    		strQuery.append(" from cmr1012 a, cmr1000 b      	\n");
//    		strQuery.append(" where b.cr_acptno = a.cr_acptno	\n");
//    		strQuery.append(" and a.cr_orderid = ?				\n");
//    		pstmt = conn.prepareStatement(strQuery.toString());
//    		//pstmt = new LoggableStatement(conn,strQuery.toString());
//    		pstmt.setString(1, orderID);
//    		//ecamsLogger.error(((LoggableStatement)pstmt).getQueryString());
//    		rs = pstmt.executeQuery();
//    		if (rs.next()){
//    			if (rs.getInt("cnt") == 0 ){
//        			result="C";    				
//    			}
//
//    		}
//    		rs.close();
//    		pstmt.close();
	  		
	  		 // 20230126 관리자 권한 있어도 QA처럼 전체 조회 되도록 로직 추가
		   UserInfo     userinfo = new UserInfo();
		   boolean adminYn = userinfo.isAdmin_conn(userID,conn);
		   userinfo = null; 
    		
	  		
	  		
    		if(!result.equals("C")){
    			result = "Y";
	    		//if ( !(reqID.equals("")) && reqID != null ){
	    		if ( reqID != null && !reqID.equals("")){
	        		strQuery.setLength(0);
	        		strQuery.append(" update cmc0420 set cc_status = '9', \n");
	        		strQuery.append(" cc_enddt = sysdate 				 \n");
	        		strQuery.append(" where cc_orderid = ? 				 \n");
	        		strQuery.append(" and cc_reqid = ?                  \n");
	        		if(!adminYn){
	        			strQuery.append(" and cc_orderuser = ?				 \n");
	        		}
	        		pstmt = conn.prepareStatement(strQuery.toString());
	        		pstmt.setString(1, orderID);
	        		pstmt.setString(2, reqID);
	        		if(!adminYn){
	        			pstmt.setString(3, userID);
	        		}
	        		pstmt.executeUpdate();
	        		pstmt.close();
	        		
	        		strQuery.setLength(0);
	        		strQuery.append(" update cmc0410 set cc_status = '9', \n");
	        		strQuery.append(" cc_enddt = sysdate 				 \n");
	        		strQuery.append(" where cc_reqid = ?                 \n");
	        		
	        		pstmt = conn.prepareStatement(strQuery.toString());
	        		pstmt.setString(1, reqID);
	        		pstmt.executeUpdate();
	        		pstmt.close();
	        		
	    		}else{
	    			strQuery.setLength(0);
	        		strQuery.append(" update cmc0420 set cc_status = '9', \n");
	        		strQuery.append(" cc_enddt = sysdate 				 \n");
	        		strQuery.append(" where cc_orderid = ? 				 \n");
	        		if(!adminYn){
	        			strQuery.append(" and cc_orderuser = ?				 \n");
	        		}
	        		pstmt = conn.prepareStatement(strQuery.toString());
	        		pstmt.setString(1, orderID);
	        		if(!adminYn){
	        			pstmt.setString(2, userID);
	        		}
	        		pstmt.executeUpdate();
	        		pstmt.close();
	        		
	        		
	    		}

	    		conn.close();
	    		
	    		pstmt = null;
	    		conn = null;
    		}
    		
    		returnObjectArray = rsval.toArray();
			rsval.clear();
			rsval = null;

			return returnObjectArray;
    		
    	} catch (SQLException sqlexception) {
			sqlexception.printStackTrace();
			ecamsLogger.error("## Cmc0500.statusUpdt() SQLException START ##");
			ecamsLogger.error("## Error DESC : ", sqlexception);	
			ecamsLogger.error("## Cmc0500.statusUpdt() SQLException END ##");			
			throw sqlexception;
		} catch (Exception exception) {
			exception.printStackTrace();
			ecamsLogger.error("## Cmc0500.statusUpdt() Exception START ##");				
			ecamsLogger.error("## Error DESC : ", exception);	
			ecamsLogger.error("## Cmc0500.statusUpdt() Exception END ##");				
			throw exception;
		}finally{
			if (strQuery != null) 	strQuery = null;
			if (pstmt != null)  try{pstmt.close();}catch (Exception ex2){ex2.printStackTrace();}
			if (conn != null){
				try{
					ConnectionResource.release(conn);
				}catch(Exception ex3){
					ecamsLogger.error("## Cmc0500.statusUpdt() connection release exception ##");
					ex3.printStackTrace();
				}
			}
		}
   
    }
    
    public Object[] getMainOrderList(String UserId) throws SQLException, Exception{
 	   Connection conn = null;
 	   PreparedStatement pstmt = null;
 	   ResultSet rs = null;
 	   Object[] returnObjectArray = null;
 	   ArrayList<HashMap<String,String>> rsval = new ArrayList<HashMap<String, String>>();
 	   HashMap<String, String> rst = null;
 	   StringBuffer strQuery = new StringBuffer();
 	   ConnectionContext connectionContext = new ConnectionResource();	
 	   
 	   String project = "";
 	   String manidYN = "N";
 	   
 	   try{
 		   conn = connectionContext.getConnection();
 		   
 		   strQuery.setLength(0);
 		   strQuery.append(" select cm_manid from cmm0040	\n");
 		   strQuery.append(" where cm_userid = ?			\n");
 		   strQuery.append(" and cm_manid = 'Y'				\n");
 		   pstmt = conn.prepareStatement(strQuery.toString());
 		   //pstmt = new LoggableStatement(conn,strQuery.toString());
 		   pstmt.setString(1, UserId);
 		   //ecamsLogger.error(((LoggableStatement)pstmt).getQueryString());
 		   rs = pstmt.executeQuery();
 		   
 		   if (rs.next()){
 			   manidYN = rs.getString("cm_manid");
 		   }
 		   rs.close();
 		   pstmt.close();
 		   
 		   strQuery.setLength(0);
 		   strQuery.append(" select cm_project from cmm0040 where cm_userid = ? \n");
 		   pstmt = conn.prepareStatement(strQuery.toString());
 		   pstmt.setString(1, UserId);
 		   rs = pstmt.executeQuery();
 		   if (rs.next()){
 			   project = rs.getString("cm_project");
 		   } 
 		   rs.close();
 		   pstmt.close();
 		   
 		   if (manidYN.equals("Y")){//일반직원이면 외주문서(CC_DOTYPE:03) 보이지 않기

 	 		   
 	 		   strQuery.setLength(0);
 	 		   strQuery.append(" select A.CC_ORDERID, A.CC_REQID, A.CC_SUBID, A.CC_DOCNUM, A.CC_ENDPLAN,							  \n");
 	 		   strQuery.append(" A.CC_REQSUB, A.CC_DETAILSAYU, A.CC_STATUS,	A.CC_STARTDT, 											  \n");
 	 		   strQuery.append(" to_char(A.CC_STARTDT, 'yyyy/mm/dd')STARTDT, to_char(A.CC_ENDDT, 'yyyy/mm/dd') ENDDT, A.CC_ORDERUSER, \n");
 	 		   strQuery.append(" B.CC_RUNNER, C.CM_USERNAME as runnerName, D.CM_USERNAME as orderName								  \n");
 	 		   strQuery.append(" from   cmc0420 a, cmc0450 b, cmm0040 c, cmm0040 d													  \n");
 	 		   strQuery.append(" where A.CC_ORDERUSER in (select cm_userid from cmm0040 where cm_project = ? )					      \n");
 	 		   strQuery.append(" and a.cc_status not in ('9','3')																  	  \n");
 	 		   strQuery.append(" and b.cc_orderid = a.cc_orderid																	  \n");
 	 		   strQuery.append(" and c.cm_userid = b.cc_runner																		  \n");
 	 		   strQuery.append(" and d.cm_userid = a.cc_orderuser																	  \n");
 	 		   strQuery.append(" and (a.cc_reqid is null or a.cc_reqid not in (select cc_reqid from cmc0400 where cc_doctype = '03')) \n");
// 	 		   strQuery.append(" and a.cc_reqid = e.cc_reqid																		  \n");
// 	 		   strQuery.append(" and e.cc_doctype <> '03'																			  \n");
 	 		   strQuery.append(" UNION																							      \n");
 	 		   strQuery.append(" select A.CC_ORDERID, A.CC_REQID, A.CC_SUBID, A.CC_DOCNUM, A.CC_ENDPLAN,							  \n");
 	 		   strQuery.append(" A.CC_REQSUB, A.CC_DETAILSAYU, A.CC_STATUS,	A.CC_STARTDT,											  \n");
 	 		   strQuery.append(" to_char(A.CC_STARTDT, 'yyyy/mm/dd')STARTDT, to_char(A.CC_ENDDT, 'yyyy/mm/dd') ENDDT, A.CC_ORDERUSER, \n");
 	 		   strQuery.append(" B.CC_RUNNER, C.CM_USERNAME as runnerName, D.CM_USERNAME as orderName								  \n");
 	 		   strQuery.append(" from   cmc0420 a, cmc0450 b, cmm0040 c, cmm0040 d													  \n");
 	 		   strQuery.append(" where a.cc_orderid = b.cc_orderid																      \n");
 	 		   strQuery.append(" and b.cc_runner in (select cm_userid from cmm0040 where cm_project = ?)							  \n");
 	 		   strQuery.append(" and a.cc_status not in ('9','3')																  	  \n");
 	 		   strQuery.append(" and c.cm_userid = b.cc_runner																		  \n");
 	 		   strQuery.append(" and d.cm_userid = a.cc_orderuser																	  \n");
 	 		   strQuery.append(" and (a.cc_reqid is null or a.cc_reqid not in (select cc_reqid from cmc0400 where cc_doctype = '03')) \n");
// 	 		   strQuery.append(" and a.cc_reqid = e.cc_reqid																		  \n");
// 	 		   strQuery.append(" and e.cc_doctype <> '03'																			  \n");
 	 		   strQuery.append(" order by CC_STARTDT desc																		      \n");
 	 		 
 	 		   pstmt = conn.prepareStatement(strQuery.toString());
 	 		   //pstmt = new LoggableStatement(conn,strQuery.toString());
 	 		   int parmCnt = 0;
 	 		   pstmt.setString(++parmCnt, project);
 	 		   pstmt.setString(++parmCnt, project);
 	 		   //ecamsLogger.error(((LoggableStatement)pstmt).getQueryString());
 	 		   rs = pstmt.executeQuery();
 	 		   
 		   }else{
 	 		   
 	 		   strQuery.setLength(0);
 	 		   strQuery.append(" select A.CC_ORDERID, A.CC_REQID, A.CC_SUBID, A.CC_DOCNUM, A.CC_ENDPLAN,							  \n");
 	 		   strQuery.append(" A.CC_REQSUB, A.CC_DETAILSAYU, A.CC_STATUS,	A.CC_STARTDT,											  \n");
 	 		   strQuery.append(" to_char(A.CC_STARTDT, 'yyyy/mm/dd')STARTDT, to_char(A.CC_ENDDT, 'yyyy/mm/dd') ENDDT, A.CC_ORDERUSER, \n");
 	 		   strQuery.append(" B.CC_RUNNER, C.CM_USERNAME as runnerName, D.CM_USERNAME as orderName								  \n");
 	 		   strQuery.append(" from   cmc0420 a, cmc0450 b, cmm0040 c, cmm0040 d													  \n");
 	 		   strQuery.append(" where A.CC_ORDERUSER in (select cm_userid from cmm0040 where cm_project = ? )					      \n");
 	 		   strQuery.append(" and a.cc_status not in ('9','3')																  	  \n");
 	 		   strQuery.append(" and b.cc_orderid = a.cc_orderid																	  \n");
 	 		   strQuery.append(" and c.cm_userid = b.cc_runner																		  \n");
 	 		   strQuery.append(" and d.cm_userid = a.cc_orderuser																	  \n");
 	 		   strQuery.append(" UNION																								  \n");
 	 		   strQuery.append(" select A.CC_ORDERID, A.CC_REQID, A.CC_SUBID, A.CC_DOCNUM, A.CC_ENDPLAN,							  \n");
 	 		   strQuery.append(" A.CC_REQSUB, A.CC_DETAILSAYU, A.CC_STATUS,	A.CC_STARTDT,											  \n");
 	 		   strQuery.append(" to_char(A.CC_STARTDT, 'yyyy/mm/dd')STARTDT, to_char(A.CC_ENDDT, 'yyyy/mm/dd') ENDDT, A.CC_ORDERUSER, \n");
 	 		   strQuery.append(" B.CC_RUNNER, C.CM_USERNAME as runnerName, D.CM_USERNAME as orderName								  \n");
 	 		   strQuery.append(" from   cmc0420 a, cmc0450 b, cmm0040 c, cmm0040 d													  \n");
 	 		   strQuery.append(" where a.cc_orderid = b.cc_orderid																      \n");
 	 		   strQuery.append(" and b.cc_runner in (select cm_userid from cmm0040 where cm_project = ?)							  \n");
 	 		   strQuery.append(" and a.cc_status not in ('9','3')																  	  \n");
 	 		   strQuery.append(" and c.cm_userid = b.cc_runner																		  \n");
 	 		   strQuery.append(" and d.cm_userid = a.cc_orderuser																	  \n");
 	 		   strQuery.append(" order by CC_STARTDT desc																		      \n");
 	 		   pstmt = conn.prepareStatement(strQuery.toString());
 	 		   //pstmt = new LoggableStatement(conn,strQuery.toString());
 	 		   int parmCnt = 0;
 	 		   pstmt.setString(++parmCnt, project);
 	 		   pstmt.setString(++parmCnt, project);
 	 		   //ecamsLogger.error(((LoggableStatement)pstmt).getQueryString());
 	 		   rs = pstmt.executeQuery();
 		   }
 		   
 		   

 		   
 		   while (rs.next()){
 		  		rst = new HashMap<String, String>();
 		  		rst.put("CC_ORDERID", rs.getString("CC_ORDERID"));
 		  		rst.put("CC_REQID", rs.getString("CC_REQID"));
 		  		rst.put("CC_SUBID", rs.getString("CC_SUBID"));
 				rst.put("CC_DOCNUM", rs.getString("CC_DOCNUM"));
 				rst.put("CC_ENDPLAN", rs.getString("CC_ENDPLAN"));
 				rst.put("CC_REQSUB", rs.getString("CC_REQSUB"));
 				rst.put("CC_DETAILSAYU", rs.getString("CC_DETAILSAYU"));
 				rst.put("CC_STATUS", rs.getString("CC_STATUS"));
 				rst.put("STARTDT", rs.getString("STARTDT"));
 				rst.put("ENDDT", rs.getString("ENDDT"));
 				rst.put("CC_ORDERUSER", rs.getString("CC_ORDERUSER"));
 				rst.put("runnerName", rs.getString("runnerName"));
 				rst.put("orderName", rs.getString("orderName"));
 				rsval.add(rst);
 				rst = null;
 		   }
 		   
 		   
 		   
 		   rs.close();
 		   pstmt.close();
 		   conn.close();
        
  		   rs = null;
  		   pstmt = null;
  		   conn = null;
		
  		   returnObjectArray = rsval.toArray();
		   rsval.clear();
		   rsval = null;

		   return returnObjectArray;
		
 	   }catch (SQLException sqlexception) {
			sqlexception.printStackTrace();
			ecamsLogger.error("## Cmc0500.getORDERListSearch() SQLException START ##");
			ecamsLogger.error("## Error DESC : ", sqlexception);	
			ecamsLogger.error("## Cmc0500.getORDERListSearch() SQLException END ##");			
			throw sqlexception;
		} catch (Exception exception) {
			exception.printStackTrace();
			ecamsLogger.error("## Cmc0500.getORDERListSearch() Exception START ##");				
			ecamsLogger.error("## Error DESC : ", exception);	
			ecamsLogger.error("## Cmc0500.getORDERListSearch() Exception END ##");				
			throw exception;
		}finally{
			if (strQuery != null) 	strQuery = null;
			if (returnObjectArray != null)	returnObjectArray = null;
			if (rs != null)     try{rs.close();}catch (Exception ex){ex.printStackTrace();}
			if (pstmt != null)  try{pstmt.close();}catch (Exception ex2){ex2.printStackTrace();}
			if (conn != null){
				try{
					ConnectionResource.release(conn);
				}catch(Exception ex3){
					ecamsLogger.error("## Cmc0500.getORDERListSearch() connection release exception ##");
					ex3.printStackTrace();
				}
			}
		}
	   
	   
   }//end getORDERListSearch Method
}//end of Cmc0500 class statement
