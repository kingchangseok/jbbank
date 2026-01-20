/*****************************************************************************************
	1. program ID	: Cmt0200.java
	2. create date	: 2010.11. 29
	3. auth		    : is.choi
	4. update date	: 
	5. auth		    : 
	6. description	: 1. Test Management Test result
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
public class Cmt0200{    
	

    /**
     * Logger Class Instance Creation
     * logger
     */
	
    Logger ecamsLogger  = EcamsLogger.getLoggerInstance();
    
	

    public Object[] getTestCase(String IsrId,String IsrSub,String UserId,String ReqCd,String TestSeq) throws SQLException, Exception {
    	Connection        conn        = null;
		PreparedStatement pstmt       = null;
		ResultSet         rs          = null;
		StringBuffer      strQuery    = new StringBuffer();
		ConnectionContext connectionContext = new ConnectionResource();
		HashMap<String, String>			  rst		  = null;
		ArrayList<HashMap<String, String>>		  rtList	  = new ArrayList<HashMap<String, String>>();

		try {
			conn = connectionContext.getConnection();
			
	        int svTestSeq = 0;
	        int i = 0;
	        
        	strQuery.setLength(0);	
        	strQuery.append("select nvl(max(cc_testseq),0) maxseq     \n");
        	strQuery.append("  from cmc0310 a                         \n");
        	strQuery.append(" where a.cc_isrid=? and a.cc_isrsub=?    \n"); 
        	pstmt = conn.prepareStatement(strQuery.toString());
        	pstmt.setString(1, IsrId);
        	pstmt.setString(2, IsrSub);
        	rs = pstmt.executeQuery();
        	if (rs.next()) {
        		svTestSeq = rs.getInt("maxseq");
        	}
        	rs.close();
        	pstmt.close();
        	        	
			rtList.clear();		
        	strQuery.setLength(0);	
        	strQuery.append("select a.cc_caseseq,a.cc_scmuser,a.cc_casename,a.cc_nothing,\n");	
        	strQuery.append("   e.cm_username,to_char(a.cc_lastdt,'yyyy/mm/dd hh24:mi') lastdt     \n");
        	strQuery.append("  from cmc0240 a,cmm0040 e                       \n");
        	strQuery.append(" where a.cc_isrid=? and a.cc_isrsub=?            \n");   
        	strQuery.append("   and nvl(a.cc_status,'0')<>'3'                 \n");
        	strQuery.append("   and a.cc_scmuser=e.cm_userid                  \n");  	
        	strQuery.append(" order by a.cc_caseseq                           \n");
        	
        	pstmt = conn.prepareStatement(strQuery.toString());
   //     	pstmt =  new LoggableStatement(conn, strQuery.toString());
        	pstmt.setString(1, IsrId);
        	pstmt.setString(2, IsrSub);
   //     	//ecamsLogger.error(((LoggableStatement)pstmt).getQueryString());
        	rs = pstmt.executeQuery();
        	while (rs.next()) {        		
        		rst = new HashMap<String, String>();
        		rst.put("cc_testseq", TestSeq+"차 테스트");        		
        		rst.put("cc_scmuser", rs.getString("cc_scmuser"));
        		rst.put("scmuser", rs.getString("cm_username"));
        		rst.put("cc_casename", rs.getString("cc_casename"));
        		rst.put("cc_caseseq", rs.getString("cc_caseseq"));
        		rst.put("cc_nothing", rs.getString("cc_nothing"));
        		if (rs.getString("cc_nothing").equals("Y")) {
        			rst.put("cc_casename", "해당사항없음");
        		}
        		rst.put("lastdt", rs.getString("lastdt"));
        		rtList.add(rst);
        	}
        	rs.close();
        	pstmt.close();
        	
        	if (svTestSeq > 0) {
	        	strQuery.setLength(0);	
	        	strQuery.append("select a.cc_caseseq,a.cc_testseq,                \n");
	        	strQuery.append("       a.cc_testday,a.cc_testrst,a.cc_testuser,  \n");	
	        	strQuery.append("       d.cm_username                             \n");
	        	strQuery.append("  from cmc0242 a,cmm0040 d                       \n");	
	        	strQuery.append(" where a.cc_isrid=? and a.cc_isrsub=?            \n");  
	        	strQuery.append("   and a.cc_testseq=?                            \n"); 
	        	strQuery.append("   and a.cc_testuser=d.cm_userid                 \n");  	
	        	strQuery.append(" order by a.cc_caseseq                           \n");
	        	pstmt = conn.prepareStatement(strQuery.toString());
//	        	pstmt =  new LoggableStatement(conn, strQuery.toString());
	        	pstmt.setString(1, IsrId);
	        	pstmt.setString(2, IsrSub);
	        	pstmt.setString(3, TestSeq);
	//        	//ecamsLogger.error(((LoggableStatement)pstmt).getQueryString());
	        	rs = pstmt.executeQuery();
	        	while (rs.next()) {   
	        		for (i=0;rtList.size()>i;i++) {
	        			if (rtList.get(i).get("cc_caseseq").equals(rs.getString("cc_caseseq"))) {
	        				rst = new HashMap<String, String>();
	        				rst = rtList.get(i);
	        				if (rs.getString("cc_testday") != null) {	        					
	        					rst.put("testday", rs.getString("cc_testday").substring(0,4)+"/"+
	        							           rs.getString("cc_testday").substring(4,6)+"/"+
	        							           rs.getString("cc_testday").substring(6,8));
	        				}
	        				rst.put("cc_testrst", rs.getString("cc_testrst"));
	        				rst.put("tester", rs.getString("cm_username"));
	        				rst.put("cc_testuser", rs.getString("cc_testuser"));
	        				rtList.set(i, rst);
	        				break;
	        			}
	        		}
	        	}
	        	rs.close();
	        	pstmt.close();
        	}
        	
        	conn.close();
        	conn = null;
        	return rtList.toArray();
			
		} catch (SQLException sqlexception) {
			sqlexception.printStackTrace();
			ecamsLogger.error("## Cmt0200.getTestCase() SQLException START ##");
			ecamsLogger.error("## Error DESC : ", sqlexception);	
			ecamsLogger.error("## Cmt0200.getTestCase() SQLException END ##");			
			throw sqlexception;
		} catch (Exception exception) {
			exception.printStackTrace();
			ecamsLogger.error("## Cmt0200.getTestCase() Exception START ##");				
			ecamsLogger.error("## Error DESC : ", exception);	
			ecamsLogger.error("## Cmt0200.getTestCase() Exception END ##");				
			throw exception;
		}finally{
			if (strQuery != null) 	strQuery = null;
			if (rs != null)     try{rs.close();}catch (Exception ex){ex.printStackTrace();}
			if (pstmt != null)  try{pstmt.close();}catch (Exception ex2){ex2.printStackTrace();}
			if (conn != null){
				try{
					ConnectionResource.release(conn);
				}catch(Exception ex3){
					ecamsLogger.error("## Cmt0200.getTestCase() connection release exception ##");
					ex3.printStackTrace();
				}
			}
		}
	}
    //getTestCase
    public Object[] getTestCase_Sub(String IsrId,String IsrSub,String UserId,String ReqCd,String TestSeq,boolean scmSw,boolean testSw) throws SQLException, Exception {
    	Connection        conn        = null;
		PreparedStatement pstmt       = null;
		ResultSet         rs          = null;
		StringBuffer      strQuery    = new StringBuffer();
		ConnectionContext connectionContext = new ConnectionResource();
		boolean           findSw = false;
		HashMap<String, String>			  rst		  = null;
		ArrayList<HashMap<String, String>>		  rtList	  = new ArrayList<HashMap<String, String>>();

		try {
			conn = connectionContext.getConnection();
			int svTestSeq = 0;
	        int i = 0;
	        
        	strQuery.setLength(0);	
        	strQuery.append("select nvl(max(cc_testseq),0) maxseq     \n");
        	strQuery.append("  from cmc0310 a                         \n");
        	strQuery.append(" where a.cc_isrid=? and a.cc_isrsub=?    \n"); 
        	pstmt = conn.prepareStatement(strQuery.toString());
        	pstmt.setString(1, IsrId);
        	pstmt.setString(2, IsrSub);
        	rs = pstmt.executeQuery();
        	if (rs.next()) {
        		svTestSeq = rs.getInt("maxseq");
        	}
        	rs.close();
        	pstmt.close();
        	
			rtList.clear();		
        	strQuery.setLength(0);	
        	strQuery.append("select a.cc_caseseq,a.cc_itemmsg,a.cc_gbncd,a.cc_seqno,b.cc_scmuser \n");
        	strQuery.append("  from cmc0241 a,cmc0240 b                   \n");
        	strQuery.append(" where b.cc_isrid=? and b.cc_isrsub=?        \n");  
        	strQuery.append("   and nvl(b.cc_status,'0')<>'3'             \n");
        	strQuery.append("   and b.cc_isrid=a.cc_isrid                 \n"); 
        	strQuery.append("   and b.cc_isrsub=a.cc_isrsub               \n");
        	strQuery.append("   and b.cc_caseseq=a.cc_caseseq             \n");    	
        	strQuery.append(" order by a.cc_caseseq,a.cc_gbncd,a.cc_seqno \n");
        	pstmt = conn.prepareStatement(strQuery.toString());
        	//pstmt =  new LoggableStatement(conn, strQuery.toString());
        	pstmt.setString(1, IsrId);
        	pstmt.setString(2, IsrSub);
        	////ecamsLogger.error(((LoggableStatement)pstmt).getQueryString());
        	rs = pstmt.executeQuery();
        	while (rs.next()) {        		
        		rst = new HashMap<String, String>();
        		rst.put("cc_caseseq", rs.getString("cc_caseseq"));
        		rst.put("ITEMMSG", rs.getString("cc_itemmsg"));
        		rst.put("cc_gbncd", rs.getString("cc_gbncd"));
        		rst.put("cc_seqno", rs.getString("cc_seqno"));
        		if (scmSw == true) {
	        		if (rs.getString("cc_scmuser").equals(UserId)) {
	        			rst.put("editable", "1");
	        		} else {
	        			rst.put("editable", "0");
	        		}
        		} else {
        			rst.put("editable", "0");
        		}
        		rst.put("selected1","0");
        		rst.put("selected2","0");
        		rtList.add(rst);
        	}
        	rs.close();
        	pstmt.close();
        	

        	if (svTestSeq > 0) {
	        	strQuery.setLength(0);	
	        	strQuery.append("select a.cc_testrst,a.cc_caseseq,a.cc_seqno \n");
	        	strQuery.append("  from cmc0243 a                       \n");	
	        	strQuery.append(" where a.cc_isrid=? and a.cc_isrsub=?  \n");  
	        	strQuery.append("   and a.cc_testseq=?                  \n"); 
	        	strQuery.append(" order by a.cc_caseseq,a.cc_seqno      \n");
	        	pstmt = conn.prepareStatement(strQuery.toString());
	        	//pstmt =  new LoggableStatement(conn, strQuery.toString());
	        	pstmt.setString(1, IsrId);
	        	pstmt.setString(2, IsrSub);
	        	pstmt.setString(3, TestSeq);
	        	////ecamsLogger.error(((LoggableStatement)pstmt).getQueryString());
	        	rs = pstmt.executeQuery();
	        	while (rs.next()) {   
	        		for (i=0;rtList.size()>i;i++) {
	        			if (rtList.get(i).get("cc_caseseq").equals(rs.getString("cc_caseseq")) &&
	    	        		rtList.get(i).get("cc_seqno").equals(rs.getString("cc_seqno")) &&
	        				rtList.get(i).get("cc_gbncd").equals("R")) {
        					if (rs.getString("cc_testrst") != null) {	
	        					rst = new HashMap<String, String>();
		        				rst = rtList.get(i);
		        				if (rs.getString("cc_testrst").equals("Y")) {
		        					rst.put("selected1","1");
		        				} else {
		        					rst.put("selected2","1");
		        				}
	        					rtList.set(i, rst);
	        				}	
        					if (rs.getString("cc_testrst").equals("Y"))
        						rst.put("testrst", "정상");
        					else
        						rst.put("testrst", "비정상");
	        				       				
	        				break;
	        			}
	        		}
	        	}
	        	rs.close();
	        	pstmt.close();
        	}
        	conn.close();
        	
        	rst = new HashMap<String, String>();
    		rst.put("cc_caseseq", "999");
    		rst.put("ITEMMSG", "");
    		rst.put("cc_gbncd", "C");
    		rst.put("cc_seqno", "000");
    		rst.put("editable", "1");
    		rtList.add(rst);
    		
    		rst = new HashMap<String, String>();
    		rst.put("cc_caseseq", "999");
    		rst.put("ITEMMSG", "");
    		rst.put("cc_gbncd", "R");
    		rst.put("cc_seqno", "000");
    		rst.put("editable", "1");
    		rst.put("selected1", "0");
    		rst.put("selected2", "0");
    		rtList.add(rst);
        	conn = null;
        	return rtList.toArray();
			
		} catch (SQLException sqlexception) {
			sqlexception.printStackTrace();
			ecamsLogger.error("## Cmt0200.getTestCase_Sub() SQLException START ##");
			ecamsLogger.error("## Error DESC : ", sqlexception);	
			ecamsLogger.error("## Cmt0200.getTestCase_Sub() SQLException END ##");			
			throw sqlexception;
		} catch (Exception exception) {
			exception.printStackTrace();
			ecamsLogger.error("## Cmt0200.getTestCase_Sub() Exception START ##");				
			ecamsLogger.error("## Error DESC : ", exception);	
			ecamsLogger.error("## Cmt0200.getTestCase_Sub() Exception END ##");				
			throw exception;
		}finally{
			if (strQuery != null) 	strQuery = null;
			if (rs != null)     try{rs.close();}catch (Exception ex){ex.printStackTrace();}
			if (pstmt != null)  try{pstmt.close();}catch (Exception ex2){ex2.printStackTrace();}
			if (conn != null){
				try{
					ConnectionResource.release(conn);
				}catch(Exception ex3){
					ecamsLogger.error("## Cmt0200.getTestCase_Sub() connection release exception ##");
					ex3.printStackTrace();
				}
			}
		}
	}
    //getTestCase_Sub

    public String endTestCase(String IsrId,String IsrSub,String TestSeq) throws SQLException, Exception {
    	Connection        conn        = null;
		PreparedStatement pstmt       = null;
		ResultSet         rs          = null;
		StringBuffer      strQuery    = new StringBuffer();
		ConnectionContext connectionContext = new ConnectionResource();

		try {
			conn = connectionContext.getConnection();
			boolean endSw = true;
	        
        	strQuery.setLength(0);	
        	strQuery.append("select count(*) cnt from cmc0310 a,cmc0110 b \n");
        	strQuery.append(" where b.cc_isrid=? and b.cc_isrsub=?    \n"); 
        	strQuery.append("   and b.cc_substatus='32'               \n"); 
			strQuery.append("   and b.cc_isrid=a.cc_isrid             \n");
			strQuery.append("   and b.cc_isrsub=a.cc_isrsub           \n");
        	strQuery.append("   and a.cc_testseq=?                    \n");
        	strQuery.append("   and a.cc_eddate is null               \n");
        	strQuery.append("   and a.cc_endreqdt is null             \n");
        	pstmt = conn.prepareStatement(strQuery.toString());
        	pstmt.setString(1, IsrId);
        	pstmt.setString(2, IsrSub);
        	pstmt.setString(3, TestSeq);
        	rs = pstmt.executeQuery();
        	if (rs.next()) {
        		if (rs.getInt("cnt") == 0) endSw = false;
        	}
        	rs.close();
        	pstmt.close();
        	        	
			if (endSw == true) {
	        	strQuery.setLength(0);	
	        	strQuery.append("select count(*) cnt from cmc0240 a,                     \n");
	        	strQuery.append("     (select cc_caseseq,cc_testrst from cmc0242         \n");
	        	strQuery.append("       where cc_isrid=? and cc_isrsub=?                 \n");         
	        	strQuery.append("         and cc_testseq=?) b                            \n");
	        	strQuery.append(" where a.cc_isrid=? and a.cc_isrsub=?                   \n");
	        	strQuery.append("   and nvl(a.cc_status,'0')<>'3'                        \n");
	        	strQuery.append("   and nvl(a.cc_nothing,'N')='N'                        \n");
	        	strQuery.append("   and a.cc_caseseq=b.cc_caseseq(+)                     \n");
	        	strQuery.append("   and b.cc_testrst is null                             \n");
        	
	        	pstmt = conn.prepareStatement(strQuery.toString());
	        	//pstmt =  new LoggableStatement(conn, strQuery.toString());
	        	pstmt.setString(1, IsrId);
	        	pstmt.setString(2, IsrSub);
	        	pstmt.setString(3, TestSeq);
	        	pstmt.setString(4, IsrId);
	        	pstmt.setString(5, IsrSub);
	        	////ecamsLogger.error(((LoggableStatement)pstmt).getQueryString());
	        	rs = pstmt.executeQuery();
	        	if (rs.next()) {
	        		if (rs.getInt("cnt") > 0) endSw = false;
	        	}
	        	rs.close();
	        	pstmt.close();
			}
			
			if (endSw == true) {
				strQuery.setLength(0);
				strQuery.append("select count(*) cnt from (                                  \n");                           
				strQuery.append("    select c.cc_caseseq,c.cc_seqno from cmc0240 a,cmc0241 c \n");      
				strQuery.append("     where a.cc_isrid=? and a.cc_isrsub=?                   \n");
				strQuery.append("       and nvl(a.cc_status,'0')<>'3'                        \n");
	        	strQuery.append("       and nvl(a.cc_nothing,'N')='N'                        \n");
				strQuery.append("       and a.cc_isrid=c.cc_isrid                            \n");
				strQuery.append("       and a.cc_isrsub=c.cc_isrsub                          \n");
				strQuery.append("       and a.cc_caseseq=c.cc_caseseq                        \n");
				strQuery.append("       and c.cc_gbncd='R'                                   \n");
				strQuery.append("     minus                                                  \n");
				strQuery.append("    select cc_caseseq,cc_seqno from cmc0243                 \n");
				strQuery.append("     where cc_isrid=? and cc_isrsub=?                       \n");
				strQuery.append("       and cc_testseq=?                                     \n");
				strQuery.append("       and cc_testrst is not null                           \n");
				strQuery.append("    )                                                       \n");
				
	        	pstmt = conn.prepareStatement(strQuery.toString());
	        	//pstmt =  new LoggableStatement(conn, strQuery.toString());
	        	pstmt.setString(1, IsrId);
	        	pstmt.setString(2, IsrSub);
	        	pstmt.setString(3, IsrId);
	        	pstmt.setString(4, IsrSub);
	        	pstmt.setString(5, TestSeq);
	        	////ecamsLogger.error(((LoggableStatement)pstmt).getQueryString());
	        	rs = pstmt.executeQuery();
	        	if (rs.next()) {
	        		if (rs.getInt("cnt") > 0) endSw = false;
	        	}
	        	rs.close();
	        	pstmt.close();
			}
        	conn.close();
        	conn = null;
        	
        	if (endSw == true) return "Y";
        	else return "N";
        	
		} catch (SQLException sqlexception) {
			sqlexception.printStackTrace();
			ecamsLogger.error("## Cmt0200.endTestCase() SQLException START ##");
			ecamsLogger.error("## Error DESC : ", sqlexception);	
			ecamsLogger.error("## Cmt0200.endTestCase() SQLException END ##");			
			throw sqlexception;
		} catch (Exception exception) {
			exception.printStackTrace();
			ecamsLogger.error("## Cmt0200.endTestCase() Exception START ##");				
			ecamsLogger.error("## Error DESC : ", exception);	
			ecamsLogger.error("## Cmt0200.endTestCase() Exception END ##");				
			throw exception;
		}finally{
			if (strQuery != null) 	strQuery = null;
			if (rs != null)     try{rs.close();}catch (Exception ex){ex.printStackTrace();}
			if (pstmt != null)  try{pstmt.close();}catch (Exception ex2){ex2.printStackTrace();}
			if (conn != null){
				try{
					ConnectionResource.release(conn);
				}catch(Exception ex3){
					ecamsLogger.error("## Cmt0200.endTestCase() connection release exception ##");
					ex3.printStackTrace();
				}
			}
		}
	}
    //endTestCase
    public Object[] getTestInfo_Recv(String IsrId,String IsrSub,boolean secuSw,String selSeq,String dataYn) throws SQLException, Exception {
    	Connection        conn        = null;
		PreparedStatement pstmt       = null;
		ResultSet         rs          = null;
		StringBuffer      strQuery    = new StringBuffer();
		ConnectionContext connectionContext = new ConnectionResource();
		int				  cnt  = 0;
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
	        	strQuery.append("   and a.cc_testseq=? and nvl(a.cc_status,'0')<>'3' \n");
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
	        
	        strQuery.append(" union    \n");
	        strQuery.append("select '2' cd,b.cm_userid,b.cm_username,c.cm_deptname  \n");
        	strQuery.append("  from cmm0100 c,cmm0040 b,cmm0043 a        \n");
        	strQuery.append(" where a.cm_rgtcd='T2'            \n");
        	strQuery.append("   and a.cm_userid=b.cm_userid    \n");
        	strQuery.append("   and b.cm_active='1'            \n");
        	strQuery.append("   and b.cm_project=c.cm_deptcd   \n");
        	
        	pstmt = conn.prepareStatement(strQuery.toString());
        	if (dataYn.equals("Y")) {
        		pstmt.setString(++cnt, IsrId);
        		pstmt.setString(++cnt, IsrSub);
        		pstmt.setString(++cnt, selSeq);
        	} else {
        		pstmt.setString(++cnt, IsrId);
        	}
        	rs = pstmt.executeQuery();
        	while (rs.next()){        		
        		rst = new HashMap<String, String>();
        		rst.put("selected", "0"); 
        		rst.put("enabled", "0");
        		rst.put("secusw", "N");
        		if (secuSw == true) { 
        			if (rs.getString("cd").equals("0")) {
        				rst.put("enabled", "0");       
        				rst.put("selected", "1");        	 				
        			} else {
        				rst.put("enabled", "1");      
        				rst.put("selected", "1");
        			}
        		}
        		rst.put("cm_userid", rs.getString("cm_userid"));
        		rst.put("cm_username", rs.getString("cm_username"));
        		rst.put("cm_deptname", rs.getString("cm_deptname"));
        		rtList.add(rst);
        	}
        	rs.close();
        	pstmt.close();
        	
        	conn.close();
        	conn = null;
        	return rtList.toArray();
			
		} catch (SQLException sqlexception) {
			sqlexception.printStackTrace();
			ecamsLogger.error("## Cmt0200.getTestInfo_Recv() SQLException START ##");
			ecamsLogger.error("## Error DESC : ", sqlexception);	
			ecamsLogger.error("## Cmt0200.getTestInfo_Recv() SQLException END ##");			
			throw sqlexception;
		} catch (Exception exception) {
			exception.printStackTrace();
			ecamsLogger.error("## Cmt0200.getTestInfo_Recv() Exception START ##");				
			ecamsLogger.error("## Error DESC : ", exception);	
			ecamsLogger.error("## Cmt0200.getTestInfo_Recv() Exception END ##");				
			throw exception;
		}finally{
			if (strQuery != null) 	strQuery = null;
			if (rs != null)     try{rs.close();}catch (Exception ex){ex.printStackTrace();}
			if (pstmt != null)  try{pstmt.close();}catch (Exception ex2){ex2.printStackTrace();}
			if (conn != null){
				try{
					ConnectionResource.release(conn);
				}catch(Exception ex3){
					ecamsLogger.error("## Cmt0200.getTestInfo_Recv() connection release exception ##");
					ex3.printStackTrace();
				}
			}
		}
	}

    
    public String setTestEnd(String IsrId,String IsrSub,String TestSeq,String UserId) throws SQLException, Exception {
    	Connection        conn        = null;
		PreparedStatement pstmt       = null;
		ResultSet         rs          = null;
		StringBuffer      strQuery    = new StringBuffer();
		ConnectionContext connectionContext = new ConnectionResource();

		try {
			String endChk = endTestCase(IsrId,IsrSub,TestSeq);
			if (!endChk.equals("Y")) {
				return "테스트케이스에 결과가 등록되지 않은 건이 존재합니다. 확인 후 종료하시기 바랍니다.";				
			}
			conn = connectionContext.getConnection();       
			
	        strQuery.setLength(0);	
	        strQuery.append("update cmc0320 set cc_eddate=SYSDATE   \n");
	        strQuery.append(" where cc_isrid=? and cc_isrsub=?      \n");
	        strQuery.append("   and cc_testseq=?                    \n");
	        strQuery.append("   and cc_eddate is null               \n");
	        pstmt = conn.prepareStatement(strQuery.toString());
	        pstmt.setString(1, IsrId);
	        pstmt.setString(2, IsrSub);
	        pstmt.setString(3, TestSeq);
	        pstmt.executeUpdate();
        	pstmt.close();
        	
	        strQuery.setLength(0);	
	        strQuery.append("update cmc0310 set cc_endrequsr=?,     \n");
	        strQuery.append("       cc_endreqdt=SYSDATE             \n");
	        strQuery.append(" where cc_isrid=? and cc_isrsub=?      \n");
	        strQuery.append("   and cc_testseq=?                    \n");
	        pstmt = conn.prepareStatement(strQuery.toString());
	        pstmt.setString(1, UserId);
	        pstmt.setString(2, IsrId);
	        pstmt.setString(3, IsrSub);
	        pstmt.setString(4, TestSeq);
	        pstmt.executeUpdate();
        	pstmt.close();
        	
        	strQuery.setLength(0);
    		strQuery.append("update cmc0110 set cc_substatus='34',  \n");
    		strQuery.append("       cc_mainstatus=decode(cc_mainstatus,'02','03',cc_mainstatus) \n");
	        strQuery.append(" where cc_isrid=? and cc_isrsub=?     \n");
	        pstmt = conn.prepareStatement(strQuery.toString());
	        pstmt.setString(1, IsrId);
	        pstmt.setString(2, IsrSub);
	        pstmt.executeUpdate();
        	pstmt.close();
        	conn.close();
        	conn = null;
        	return "OK";
			
		} catch (SQLException sqlexception) {
			sqlexception.printStackTrace();
			ecamsLogger.error("## Cmt0200.setTestEnd() SQLException START ##");
			ecamsLogger.error("## Error DESC : ", sqlexception);	
			ecamsLogger.error("## Cmt0200.setTestEnd() SQLException END ##");			
			throw sqlexception;
		} catch (Exception exception) {
			exception.printStackTrace();
			ecamsLogger.error("## Cmt0200.setTestEnd() Exception START ##");				
			ecamsLogger.error("## Error DESC : ", exception);	
			ecamsLogger.error("## Cmt0200.setTestEnd() Exception END ##");				
			throw exception;
		}finally{
			if (strQuery != null) 	strQuery = null;
			if (rs != null)     try{rs.close();}catch (Exception ex){ex.printStackTrace();}
			if (pstmt != null)  try{pstmt.close();}catch (Exception ex2){ex2.printStackTrace();}
			if (conn != null){
				try{
					ConnectionResource.release(conn);
				}catch(Exception ex3){
					ecamsLogger.error("## Cmt0200.setTestEnd() connection release exception ##");
					ex3.printStackTrace();
				}
			}
		}
	}//setTestEnd
}//end of Cmt0200 class statement