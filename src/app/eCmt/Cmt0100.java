/*****************************************************************************************
	1. program ID	: Cmt0100.java
	2. create date	: 2010.11. 23
	3. auth		    : is.choi
	4. update date	: 
	5. auth		    : 
	6. description	: 1. Test Management Common
*****************************************************************************************/

package app.eCmt;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.io.BufferedReader;

import org.apache.logging.log4j.Logger;
import org.w3c.dom.Document;

import com.ecams.common.dbconn.ConnectionContext;
import com.ecams.common.dbconn.ConnectionResource;
import com.ecams.common.logger.EcamsLogger;
import java.util.ArrayList;
import java.util.HashMap;
import app.common.*;

/**
 * @author bigeyes
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class Cmt0100{    
	

    /**
     * Logger Class Instance Creation
     * logger
     */
	
    Logger ecamsLogger  = EcamsLogger.getLoggerInstance();
    
	

    public Object[] getTestCnt(String IsrId,String IsrSub,String UserId,String subStatus,String ReqCd) throws SQLException, Exception {
    	Connection        conn        = null;
		PreparedStatement pstmt       = null;
		ResultSet         rs          = null;
		PreparedStatement pstmt2      = null;
		ResultSet         rs2         = null;
		StringBuffer      strQuery    = new StringBuffer();
		ConnectionContext connectionContext = new ConnectionResource();
		boolean           findSw = false;
		HashMap<String, String>			  rst		  = null;
		ArrayList<HashMap<String, String>>		  rtList	  = new ArrayList<HashMap<String, String>>();
		String          strTestUser = "";
		try {
			conn = connectionContext.getConnection();
	        UserInfo userinfo = new UserInfo();
	        boolean secuSw = false;
	        if (ReqCd.equals("44")) {
	        	strQuery.setLength(0);	
	        	strQuery.append("select count(*) cnt from cmc0210         \n");
	        	strQuery.append(" where cc_isrid=? and cc_isrsub=?        \n");
	        	strQuery.append("   and cc_scmuser=?                      \n");
	        	strQuery.append("   and cc_eddate is null                 \n");
	        	pstmt = conn.prepareStatement(strQuery.toString());
	      //  	pstmt =  new LoggableStatement(conn, strQuery.toString());
	        	pstmt.setString(1, IsrId);
	        	pstmt.setString(2, IsrSub);
	        	pstmt.setString(3, UserId);
	       // 	//ecamsLogger.error(((LoggableStatement)pstmt).getQueryString());
	        	rs = pstmt.executeQuery();
	        	if (rs.next()) {
	        		if (rs.getInt("cnt")>0) secuSw = true;
	        	}
	        	rs.close();
	        	pstmt.close();
	        } else {
	        	//SYSTEST_SECUCHK    
	        	//(USERID IN VARCHAR2,ISRID IN VARCHAR2,ISRSUB IN VARCHAR2,REQCD IN VARCHAR2)   
	        	strQuery.setLength(0);	
	        	strQuery.append("select SYSTEST_SECUCHK(?,?,?,?) secuok from dual \n");
	        	pstmt = conn.prepareStatement(strQuery.toString());
	      //  	pstmt =  new LoggableStatement(conn, strQuery.toString());
	        	pstmt.setString(1, UserId);
	        	pstmt.setString(2, IsrId);
	        	pstmt.setString(3, IsrSub);
	        	pstmt.setString(4, ReqCd);
	      //  	//ecamsLogger.error(((LoggableStatement)pstmt).getQueryString());
	        	rs = pstmt.executeQuery();
	        	if (rs.next()) {
	        		if (rs.getString("secuok").equals("OK")) secuSw = true;
	        		else secuSw = false;
	        	}
	        	rs.close();
	        	pstmt.close();
	        }
			rtList.clear();
			
			int testSeq = 0;	
        	strQuery.setLength(0);	
        	strQuery.append("select a.cc_testseq,a.cc_editor,a.cc_testterm, \n");
        	strQuery.append("       a.cc_eddate,a.cc_endreqdt,				  \n");
        	strQuery.append("       to_char(a.cc_eddate,'yyyy/mm/dd hh24:mi') enddate,\n");
        	strQuery.append("       to_char(a.cc_endreqdt,'yyyy/mm/dd hh24:mi') endreqdt,\n");
        	strQuery.append("       a.cc_testrst,a.cc_testmsg,        \n");
        	strQuery.append("       d.cm_username testmanager,        \n");
        	strQuery.append("       e.cm_username endrequser,         \n");
        	strQuery.append("       a.cc_testmsg,nvl(a.cc_status,'0') status \n");
        	strQuery.append("  from cmc0310 a,cmm0040 d,cmm0040 e     \n");
        	strQuery.append(" where a.cc_isrid=? and a.cc_isrsub=?    \n");
        	strQuery.append("   and a.cc_editor=d.cm_userid           \n");     
        	strQuery.append("   and a.cc_endrequsr=e.cm_userid(+)     \n"); 
        	strQuery.append(" order by a.cc_testseq                   \n");
        	pstmt = conn.prepareStatement(strQuery.toString());
    //    	pstmt =  new LoggableStatement(conn, strQuery.toString());
        	pstmt.setString(1, IsrId);
        	pstmt.setString(2, IsrSub);
      //  	//ecamsLogger.error(((LoggableStatement)pstmt).getQueryString());
        	rs = pstmt.executeQuery();
        	while (rs.next()) {
        		rst = new HashMap<String, String>();
        		rst.put("testseq", rs.getString("cc_testseq")+"차 테스트");
        		rst.put("cc_testseq", rs.getString("cc_testseq"));
        		rst.put("cc_editor", rs.getString("cc_editor"));
        		rst.put("cc_testterm", rs.getString("cc_testterm"));
        		rst.put("testmanager", rs.getString("testmanager"));
        		rst.put("cc_testmsg", rs.getString("cc_testmsg"));
        		rst.put("secusw", "N");
        		rst.put("data", "Y");
        		if (rs.getString("status").equals("0")) rst.put("status", "테스트진행중");
        		else if (rs.getString("status").equals("3")) rst.put("status", "종료반려");
        		else if (rs.getString("status").equals("9")) rst.put("status", "종료");
        		
        		if (rs.getString("cc_eddate") != null && rs.getString("cc_eddate") != "") {
        			rst.put("endyn", "Y");
        			rst.put("enddate", rs.getString("enddate"));
        			
        			if (rs.getString("cc_testrst").equals("Y")) {
        				rst.put("endgbn", "정상");
        			} else {
        				rst.put("endgbn", "비정상");
        			}
        		} else {
        			rst.put("endyn", "N");
        			if (ReqCd.equals("51") || ReqCd.equals("59")) {
	        			if (rs.getString("cc_editor").equals(UserId)) {
	        				rst.put("secusw", "Y");
	        			}
        			} 
        			findSw = true;
        		}
        		if (rs.getString("cc_endreqdt") != null) {
        			rst.put("endreqdt", rs.getString("endreqdt"));
        			rst.put("endrequser", rs.getString("endrequser"));
        		}
        		testSeq = rs.getInt("cc_testseq");
        		strTestUser = "";
        		if (ReqCd.equals("52")) {
        			strQuery.setLength(0);	
                	strQuery.append("select b.cc_testuser,c.cm_username       \n");
                    strQuery.append("  from cmc0320 b,cmm0040 c               \n");
                	strQuery.append(" where b.cc_isrid=? and b.cc_isrsub=?    \n");
            		strQuery.append("   and b.cc_testseq=?                    \n");
            		strQuery.append("   and nvl(b.cc_status,'0')<>'3'         \n");
            		strQuery.append("   and b.cc_testuser=c.cm_userid         \n");
                	pstmt2 = conn.prepareStatement(strQuery.toString());
          //      	pstmt2 =  new LoggableStatement(conn, strQuery.toString());
                	pstmt2.setString(1, IsrId);
                	pstmt2.setString(2, IsrSub);
                	pstmt2.setString(3, rs.getString("cc_testseq"));
         //       	//ecamsLogger.error(((LoggableStatement)pstmt2).getQueryString());
                	rs2 = pstmt2.executeQuery();
                	while (rs2.next()) {
                		if (strTestUser.length() > 0) strTestUser = strTestUser + ",";
                		strTestUser = strTestUser + rs2.getString("cc_testuser");
                		if (rs2.getString("cc_testuser").equals(UserId)) {
                			rst.put("secusw", "Y");
                			rst.put("cm_username", rs2.getString("cm_username"));
                		}
                	}
                	rs2.close();
                	pstmt2.close();
                	rst.put("testuser", strTestUser);
        		}
        		rtList.add(rst);
        	}
        	rs.close();
        	pstmt.close();
        	
        	if (ReqCd.equals("51") || ReqCd.equals("44")) {
	        	if (findSw == false) {
		        	if (subStatus.equals("25") && ReqCd.equals("51")) {
		        		findSw = true;
		        	} else if (ReqCd.equals("44")) {
		        		if (subStatus.equals("21") || subStatus.equals("23") || subStatus.equals("24") || subStatus.equals("25") || subStatus.equals("36")) findSw = true;
		        	}
		        	
		        	if (findSw == true) {
		        		rst = new HashMap<String, String>();
		        		rst.put("testseq", Integer.toString(++testSeq)+"차 테스트");
		        		rst.put("cc_testseq", Integer.toString(testSeq));
		        		rst.put("cc_editor", UserId);
		        		if (secuSw == true) rst.put("secusw", "Y");
		        		else rst.put("secusw", "N");
		        	    rst.put("endyn", "N");
		        	    rst.put("data", "N");
		        		rtList.add(rst);
		        	}
	        	}
        	}
        	conn.close();
        	conn = null;
        	return rtList.toArray();
			
		} catch (SQLException sqlexception) {
			sqlexception.printStackTrace();
			ecamsLogger.error("## Cmt0100.getTestCnt() SQLException START ##");
			ecamsLogger.error("## Error DESC : ", sqlexception);	
			ecamsLogger.error("## Cmt0100.getTestCnt() SQLException END ##");			
			throw sqlexception;
		} catch (Exception exception) {
			exception.printStackTrace();
			ecamsLogger.error("## Cmt0100.getTestCnt() Exception START ##");				
			ecamsLogger.error("## Error DESC : ", exception);	
			ecamsLogger.error("## Cmt0100.getTestCnt() Exception END ##");				
			throw exception;
		}finally{
			if (strQuery != null) 	strQuery = null;
			if (rs != null)     try{rs.close();}catch (Exception ex){ex.printStackTrace();}
			if (pstmt != null)  try{pstmt.close();}catch (Exception ex2){ex2.printStackTrace();}
			if (rs2 != null)     try{rs2.close();}catch (Exception ex){ex.printStackTrace();}
			if (pstmt2 != null)  try{pstmt2.close();}catch (Exception ex2){ex2.printStackTrace();}
			if (conn != null){
				try{
					ConnectionResource.release(conn);
				}catch(Exception ex3){
					ecamsLogger.error("## Cmt0100.getTestCnt() connection release exception ##");
					ex3.printStackTrace();
				}
			}
		}
	}
    //getTestInfo_Recv(strIsrId,strIsrSub,btCmd.enabled,cboTest.selectedItem.cc_testseq);	
    public Object[] getTestInfo_Recv(String IsrId,String IsrSub,boolean secuSw,String selSeq,String dataYn,String ReqCd) throws SQLException, Exception {
    	Connection        conn        = null;
		PreparedStatement pstmt       = null;
		ResultSet         rs          = null;
		PreparedStatement pstmt2      = null;
		ResultSet         rs2         = null;
		StringBuffer      strQuery    = new StringBuffer();
		ConnectionContext connectionContext = new ConnectionResource();
		int				  cnt  = 0;
		int               i = 0;
		boolean           findSw = false;
		HashMap<String, String>			  rst		  = null;
		ArrayList<HashMap<String, String>>		  rtList	  = new ArrayList<HashMap<String, String>>();

		try {
			conn = connectionContext.getConnection();
	        rtList.clear();
	        strQuery.setLength(0);
	        if (dataYn.equals("Y")) {
	        	strQuery.append("select '1' cd,b.cm_userid,b.cm_username,c.cm_deptname  \n");
	        	strQuery.append("  from cmm0100 c,cmm0040 b,cmc0320 a        \n");
	        	strQuery.append(" where a.cc_isrid=? and a.cc_isrsub=?       \n");
	        	strQuery.append("   and a.cc_testseq=?                       \n");
	        	strQuery.append("   and nvl(a.cc_status,'0')<>'3'            \n");
	        	strQuery.append("   and a.cc_testuser=b.cm_userid            \n");
	        	strQuery.append("   and b.cm_project=c.cm_deptcd             \n");
	        } else {
	        	strQuery.append("select '0' cd,b.cm_userid,b.cm_username,c.cm_deptname  \n");
	        	strQuery.append("  from cmm0100 c,cmm0040 b,cmc0100 a \n");
	        	strQuery.append(" where a.cc_isrid=?               \n");
	        	strQuery.append("   and a.cc_testeryn='Y'          \n");
	        	strQuery.append("   and a.cc_editor=b.cm_userid    \n");
	        	strQuery.append("   and b.cm_project=c.cm_deptcd   \n");
	        }
	        if (ReqCd.equals("51") && secuSw == true) {
		        strQuery.append(" union    \n");
		        strQuery.append("select '2' cd,b.cm_userid,b.cm_username,c.cm_deptname  \n");
	        	strQuery.append("  from cmc0110 d,cmm0100 c,cmm0040 b,cmm0043 a,cmm0047 e \n");
	        	strQuery.append(" where a.cm_rgtcd='T2'            \n");
	        	strQuery.append("   and a.cm_userid=b.cm_userid    \n");
	        	strQuery.append("   and b.cm_active='1'            \n");
	        	strQuery.append("   and b.cm_project=c.cm_deptcd   \n");
	        	strQuery.append("   and a.cm_userid=e.cm_userid    \n");
	        	strQuery.append("   and a.cm_rgtcd=e.cm_rgtcd      \n");
	        	strQuery.append("   and d.cc_isrid=? and d.cc_isrsub=? \n");
	        	strQuery.append("   and d.cc_recvpart=e.cm_deptcd  \n");
	        }
        	strQuery.append(" order by cd                      \n");
        	
        	pstmt = conn.prepareStatement(strQuery.toString());
    //    	pstmt =  new LoggableStatement(conn, strQuery.toString());
        	if (dataYn.equals("Y")) {
        		pstmt.setString(++cnt, IsrId);
        		pstmt.setString(++cnt, IsrSub);
        		pstmt.setString(++cnt, selSeq);
        	} else {
        		pstmt.setString(++cnt, IsrId);
        	}
        	if (ReqCd.equals("51") && secuSw == true) {
        		pstmt.setString(++cnt, IsrId);
        		pstmt.setString(++cnt, IsrSub);
        	}
    //    	//ecamsLogger.error(((LoggableStatement)pstmt).getQueryString());
        	rs = pstmt.executeQuery();
        	while (rs.next()){        	
        		findSw = false;
        		
        		for (i=0;rtList.size()>i;i++) {
        			if (rtList.get(i).get("cm_userid").equals(rs.getString("cm_userid"))) {
        				findSw = true;
        				break;
        			}
        		}
        		
        		if (findSw == false) {
	        		rst = new HashMap<String, String>();
	        		rst.put("selected", "0"); 
	        		rst.put("enabled", "0");
	        		rst.put("secusw", "N");
	        		if (secuSw == true) { 
	        			if (rs.getString("cd").equals("0")) {
	        				rst.put("enabled", "0");       
	        				rst.put("selected", "1");        	 				
	        			} else if (rs.getString("cd").equals("1")) {
	        				rst.put("enabled", "1");      
	        				rst.put("selected", "1");
	        			} else {
	        				rst.put("enabled", "1");      
	        				rst.put("selected", "0");
	        			}
	        		} else {
	        			rst.put("enabled", "0");       
        				rst.put("selected", "1");
	        		}
	        		rst.put("cm_userid", rs.getString("cm_userid"));
	        		rst.put("cm_username", rs.getString("cm_username"));
	        		rst.put("cm_deptname", rs.getString("cm_deptname"));
	        		if (dataYn.equals("Y")) {
	        			strQuery.setLength(0);
	        			strQuery.append("select cc_testrst,count(*) cnt from cmc0243 \n");
	        			strQuery.append(" where cc_isrid=? and cc_isrsub=?          \n");
	        			strQuery.append("   and cc_testseq=? and cc_gbncd='R'       \n");
	        			strQuery.append(" group by cc_testrst                       \n");
	        			pstmt2 = conn.prepareStatement(strQuery.toString());
	   //     			pstmt2 =  new LoggableStatement(conn, strQuery.toString());
	        			cnt = 0;
	        			pstmt2.setString(++cnt, IsrId);
	            		pstmt2.setString(++cnt, IsrSub);
	            		pstmt2.setString(++cnt, selSeq);
	  //          		//ecamsLogger.error(((LoggableStatement)pstmt2).getQueryString());
	            		rs2 = pstmt2.executeQuery();
	            		while (rs2.next()) {
	            			if (rs2.getString("cc_testrst").equals("N") && rs2.getInt("cnt")>0) {
	            				rst.put("totrst", "비정상");
	            				break;
	            			} else if (rs2.getString("cc_testrst").equals("Y") && rs2.getInt("cnt")>0) {
	            				rst.put("totrst", "정상");
	            			}
	            		}
	            		rs2.close();
	            		pstmt2.close();
	        		}
	        		rtList.add(rst);
        		}
        	}
        	rs.close();
        	pstmt.close();
        	
        	conn.close();
        	conn = null;
        	return rtList.toArray();
			
		} catch (SQLException sqlexception) {
			sqlexception.printStackTrace();
			ecamsLogger.error("## Cmt0100.getTestInfo_Recv() SQLException START ##");
			ecamsLogger.error("## Error DESC : ", sqlexception);	
			ecamsLogger.error("## Cmt0100.getTestInfo_Recv() SQLException END ##");			
			throw sqlexception;
		} catch (Exception exception) {
			exception.printStackTrace();
			ecamsLogger.error("## Cmt0100.getTestInfo_Recv() Exception START ##");				
			ecamsLogger.error("## Error DESC : ", exception);	
			ecamsLogger.error("## Cmt0100.getTestInfo_Recv() Exception END ##");				
			throw exception;
		}finally{
			if (strQuery != null) 	strQuery = null;
			if (rs != null)     try{rs.close();}catch (Exception ex){ex.printStackTrace();}
			if (pstmt != null)  try{pstmt.close();}catch (Exception ex2){ex2.printStackTrace();}
			if (conn != null){
				try{
					ConnectionResource.release(conn);
				}catch(Exception ex3){
					ecamsLogger.error("## Cmt0100.getTestInfo_Recv() connection release exception ##");
					ex3.printStackTrace();
				}
			}
		}
	}

    public String insTest_Recv(HashMap<String,String> etcData) throws SQLException, Exception {
    	Connection        conn        = null;
		PreparedStatement pstmt       = null;
		ResultSet         rs          = null;
		StringBuffer      strQuery    = new StringBuffer();
		ConnectionContext connectionContext = new ConnectionResource();
		int				  cnt  = 0;
		boolean           insSw = false;

		try {
			conn = connectionContext.getConnection();       
			
			String tstUser[] = etcData.get("testuser").split(",");
			
			insSw = true;
	        strQuery.setLength(0);	
	        strQuery.append("select count(*) cnt from cmc0310       \n");
	        strQuery.append(" where cc_isrid=? and cc_isrsub=?      \n");
	        strQuery.append("   and cc_testseq=?                    \n");
	        pstmt = conn.prepareStatement(strQuery.toString());
	        pstmt.setString(1, etcData.get("isrid"));
	        pstmt.setString(2, etcData.get("isrsub"));
	        pstmt.setString(3, etcData.get("testseq"));
	        rs = pstmt.executeQuery();
	        if (rs.next()) {
	        	if (rs.getInt("cnt")>0) insSw = false;
	        }
	        rs.close();
	        pstmt.close();
	        
	        if (insSw == true) {
        		strQuery.setLength(0);
        		strQuery.append("insert into cmc0310      \n");
    	        strQuery.append(" (CC_ISRID,CC_ISRSUB,CC_TESTSEQ,CC_EDITOR,CC_CREATDT,CC_LASTDT,CC_TESTTERM,CC_STATUS) \n");
    	        strQuery.append("values (?,?,?,?,SYSDATE,SYSDATE,?,'0') \n");
    	        pstmt = conn.prepareStatement(strQuery.toString());
    	        pstmt.setString(1, etcData.get("isrid"));
    	        pstmt.setString(2, etcData.get("isrsub"));
    	        pstmt.setString(3, etcData.get("testseq"));
    	        pstmt.setString(4, etcData.get("userid"));
    	        pstmt.setString(5, etcData.get("testterm"));
    	        pstmt.executeUpdate();
            	pstmt.close();
        	} else {
        		strQuery.setLength(0);
        		strQuery.append("update cmc0310 set CC_LASTDT=SYSDATE, \n");
        		strQuery.append("       CC_TESTTERM=?                  \n");
    	        strQuery.append(" where cc_isrid=? and cc_isrsub=?     \n");
    	        strQuery.append("   and cc_testseq=?                   \n");
    	        pstmt = conn.prepareStatement(strQuery.toString());
    	        pstmt.setString(1, etcData.get("testterm"));
    	        pstmt.setString(2, etcData.get("isrid"));
    	        pstmt.setString(3, etcData.get("isrsub"));
    	        pstmt.setString(4, etcData.get("testseq"));
    	        pstmt.executeUpdate();
            	pstmt.close();
        	}
	        	        
	        strQuery.setLength(0);	
	        strQuery.append("update cmc0320 set cc_status='3',cc_eddate=SYSDATE \n");
	        strQuery.append(" where cc_isrid=? and cc_isrsub=?      \n");
	        strQuery.append("   and cc_testseq=?                    \n");
	        pstmt = conn.prepareStatement(strQuery.toString());
	        pstmt.setString(1, etcData.get("isrid"));
	        pstmt.setString(2, etcData.get("isrsub"));
	        pstmt.setString(3, etcData.get("testseq"));
	        pstmt.executeUpdate();
        	pstmt.close();
        	
        	for (int i=0;tstUser.length>i;i++) {
        		insSw = true;
        		strQuery.setLength(0);
        		strQuery.append("select count(*) cnt from cmc0320      \n");
    	        strQuery.append(" where cc_isrid=? and cc_isrsub=?     \n");
    	        strQuery.append("   and cc_testseq=? and cc_testuser=? \n");
    	        pstmt = conn.prepareStatement(strQuery.toString());
    	        pstmt.setString(1, etcData.get("isrid"));
    	        pstmt.setString(2, etcData.get("isrsub"));
    	        pstmt.setString(3, etcData.get("testseq"));
    	        pstmt.setString(4, tstUser[i]);
    	        rs = pstmt.executeQuery();
    	        if (rs.next()) {
    	        	if (rs.getInt("cnt")>0) {
    	        		insSw = false;
    	        	}
    	        }
            	rs.close();
            	pstmt.close();
            	
            	if (insSw == true) {
            		strQuery.setLength(0);
            		strQuery.append("insert into cmc0320      \n");
        	        strQuery.append(" (CC_ISRID,CC_ISRSUB,CC_TESTSEQ,CC_TESTUSER,CC_LASTDT,CC_STATUS) \n");
        	        strQuery.append("values (?,?,?,?,SYSDATE,'0') \n");
        	        pstmt = conn.prepareStatement(strQuery.toString());
        	        pstmt.setString(1, etcData.get("isrid"));
        	        pstmt.setString(2, etcData.get("isrsub"));
        	        pstmt.setString(3, etcData.get("testseq"));
        	        pstmt.setString(4, tstUser[i]);
        	        pstmt.executeUpdate();
                	pstmt.close();
            	} else {
            		strQuery.setLength(0);
            		strQuery.append("update cmc0320 set cc_status='0',cc_eddate='' \n");
        	        strQuery.append(" where cc_isrid=? and cc_isrsub=?     \n");
        	        strQuery.append("   and cc_testseq=? and cc_testuser=? \n");
        	        pstmt = conn.prepareStatement(strQuery.toString());
        	        pstmt.setString(1, etcData.get("isrid"));
        	        pstmt.setString(2, etcData.get("isrsub"));
        	        pstmt.setString(3, etcData.get("testseq"));
        	        pstmt.setString(4, tstUser[i]);
        	        pstmt.executeUpdate();
                	pstmt.close();
            	}
        	}
        	strQuery.setLength(0);
    		strQuery.append("update cmc0110 set cc_substatus='31',  \n");
    		strQuery.append("       cc_mainstatus=decode(cc_mainstatus,'02','03',cc_mainstatus) \n");
	        strQuery.append(" where cc_isrid=? and cc_isrsub=?     \n");
	        strQuery.append("   and cc_substatus='25'              \n");
	        pstmt = conn.prepareStatement(strQuery.toString());
	        pstmt.setString(1, etcData.get("isrid"));
	        pstmt.setString(2, etcData.get("isrsub"));
	        pstmt.executeUpdate();
        	pstmt.close();
        	
        	conn.close();
        	conn = null;
        	return "OK";
			
		} catch (SQLException sqlexception) {
			sqlexception.printStackTrace();
			ecamsLogger.error("## Cmt0100.insTest_Recv() SQLException START ##");
			ecamsLogger.error("## Error DESC : ", sqlexception);	
			ecamsLogger.error("## Cmt0100.insTest_Recv() SQLException END ##");			
			throw sqlexception;
		} catch (Exception exception) {
			exception.printStackTrace();
			ecamsLogger.error("## Cmt0100.insTest_Recv() Exception START ##");				
			ecamsLogger.error("## Error DESC : ", exception);	
			ecamsLogger.error("## Cmt0100.insTest_Recv() Exception END ##");				
			throw exception;
		}finally{
			if (strQuery != null) 	strQuery = null;
			if (rs != null)     try{rs.close();}catch (Exception ex){ex.printStackTrace();}
			if (pstmt != null)  try{pstmt.close();}catch (Exception ex2){ex2.printStackTrace();}
			if (conn != null){
				try{
					ConnectionResource.release(conn);
				}catch(Exception ex3){
					ecamsLogger.error("## Cmt0100.insTest_Recv() connection release exception ##");
					ex3.printStackTrace();
				}
			}
		}
	}
}//end of Cmt0100 class statement