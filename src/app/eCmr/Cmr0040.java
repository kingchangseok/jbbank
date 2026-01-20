/*****************************************************************************************
	1. program ID	: Cmr0020.java
	2. create date	: 2006.08. 08
	3. auth		    : is.choi
	4. update date	: 
	5. auth		    : 
	6. description	: 1. USER INFO.
*****************************************************************************************/

package app.eCmr;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
//import java.io.BufferedReader;
//import java.io.File;
//import java.io.FileInputStream;
//import java.io.FileOutputStream;
//import java.io.FileReader;
//import java.io.InputStreamReader;

import app.common.LoggableStatement;

//import java.io.OutputStreamWriter;

import org.apache.logging.log4j.Logger;
//import org.apache.poi.hssf.record.formula.functions.Sin;


import com.ecams.common.dbconn.ConnectionContext;
import com.ecams.common.dbconn.ConnectionResource;
import com.ecams.common.logger.EcamsLogger;
import java.util.ArrayList;
import java.util.HashMap;
import app.common.*;

import app.thread.*;

/**
 * @author bigeyes
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class Cmr0040{    
	

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
    
    public Object[] SearchTerm(HashMap<String,String> etcData) throws SQLException, Exception{
		Connection        conn        = null;
		PreparedStatement pstmt       = null;
		ResultSet         rs          = null;
		StringBuffer      strQuery    = new StringBuffer();
		ArrayList<HashMap<String, String>>  rsval  = new ArrayList<HashMap<String, String>>();
		ArrayList<HashMap<String, String>>  rsconf = new ArrayList<HashMap<String, String>>();
		HashMap<String, String>			  rst		  = null;
		ConnectionContext connectionContext = new ConnectionResource();	
		String           strPrcSw       = "N";
		int				  pstmtcount  = 1;
		Object[] returnObjectArray		 = null;
		
		try {
			conn = connectionContext.getConnection();
			strQuery.append("select a.CC_ISRID, a.CC_ISRSUB, a.CC_CASESEQ, a.CC_GBNCD, a.CC_SEQNO, 				\n");			
			strQuery.append("       a.CC_ITEMMSG, a.CC_EDITOR, b.cm_username editname,							\n");
			strQuery.append("       to_char(a.CC_LASTDT,'yyyy/mm/dd') laday										\n");
			strQuery.append("  from cmc0241 a, cmm0040 b														\n");
			strQuery.append(" where   a.CC_ISRID = ?							         						\n");
			strQuery.append(" and   a.CC_ISRSUB = ?							         							\n");
			strQuery.append(" and   a.CC_CASESEQ = ?							       							\n");
			strQuery.append(" and   a.cc_gbncd = 'C'							      					   		\n");
			strQuery.append(" and   a.CC_EDITOR = ?								      					   		\n");
			strQuery.append(" and 	a.CC_EDITOR = b.cm_userid                                              		\n");
			strQuery.append(" order by CC_SEQNO				                                              		\n");
			
			pstmt = conn.prepareStatement(strQuery.toString());	
			//pstmt = new LoggableStatement(conn,strQuery.toString());
			pstmtcount = 1;
			pstmt.setString(pstmtcount++,etcData.get("IsrId"));
			pstmt.setString(pstmtcount++,etcData.get("SubId"));
			pstmt.setString(pstmtcount++,etcData.get("SeqNo"));
			pstmt.setString(pstmtcount++,etcData.get("UserId"));
	        ////ecamsLogger.error(((LoggableStatement)pstmt).getQueryString());           
	        rs = pstmt.executeQuery();
	        rsval.clear();
			while (rs.next()){			
				rst = new HashMap<String, String>();
				rst.put("ISRID",rs.getString("CC_ISRID"));
				rst.put("ISRSUB",rs.getString("CC_ISRSUB"));
				rst.put("CASESEQ",rs.getString("CC_CASESEQ"));
				rst.put("GBNCD",rs.getString("CC_GBNCD"));
				rst.put("SEQNO",rs.getString("CC_SEQNO"));
				rst.put("ITEMMSG", rs.getString("CC_ITEMMSG"));
				rst.put("LASTDT", rs.getString("laday"));
				rst.put("EDITOR",rs.getString("CC_EDITOR"));
				rst.put("editname",rs.getString("editname"));
				rsval.add(rst);
				rst = null;
			}//end of while-loop statement
			rs.close();
			pstmt.close();
			conn.close();
			conn = null;
			rs = null;
			pstmt = null;
			returnObjectArray = rsval.toArray();
			rsval.clear();
			rsval = null;
			return returnObjectArray;			
			
		} catch (SQLException sqlexception) {
			sqlexception.printStackTrace();
			ecamsLogger.error("## Cmr0040.getUnitTest() SQLException START ##");
			ecamsLogger.error("## Error DESC : ", sqlexception);	
			ecamsLogger.error("## Cmr0040.getUnitTest() SQLException END ##");			
			throw sqlexception;
		} catch (Exception exception) {
			exception.printStackTrace();
			ecamsLogger.error("## Cmr0040.getUnitTest() Exception START ##");				
			ecamsLogger.error("## Error DESC : ", exception);	
			ecamsLogger.error("## Cmr0040.getUnitTest() Exception END ##");				
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
					ecamsLogger.error("## Cmr0040.getUnitTest() connection release exception ##");
					ex3.printStackTrace();
				}
			}
		}
	}//end of getUnitTest() method statement
    
    public Object[] SearchCheck(HashMap<String,String> etcData) throws SQLException, Exception{
		Connection        conn        = null;
		PreparedStatement pstmt       = null;
		ResultSet         rs          = null;
		StringBuffer      strQuery    = new StringBuffer();
		ArrayList<HashMap<String, String>>  rsval  = new ArrayList<HashMap<String, String>>();
		ArrayList<HashMap<String, String>>  rsconf = new ArrayList<HashMap<String, String>>();
		HashMap<String, String>			  rst		  = null;
		ConnectionContext connectionContext = new ConnectionResource();	
		String           strPrcSw       = "N";
		int				  pstmtcount  = 1;
		Object[] returnObjectArray		 = null;
		
		try {
			conn = connectionContext.getConnection();
			strQuery.append("select a.CC_ISRID, a.CC_ISRSUB, a.CC_CASESEQ, a.CC_GBNCD, a.CC_SEQNO, 				\n");			
			strQuery.append("       a.CC_ITEMMSG, a.CC_EDITOR, b.cm_username editname,							\n");
			strQuery.append("       to_char(a.CC_LASTDT,'yyyy/mm/dd') laday										\n");
			strQuery.append("  from cmc0241 a, cmm0040 b														\n");
			strQuery.append(" where   a.CC_ISRID = ?							         						\n");
			strQuery.append(" and   a.CC_ISRSUB = ?							         							\n");
			strQuery.append(" and   a.CC_CASESEQ = ?							       							\n");
			strQuery.append(" and   a.cc_gbncd = 'R'							      					   		\n");
			strQuery.append(" and   a.CC_EDITOR = ?								      					   		\n");
			strQuery.append(" and 	a.CC_EDITOR = b.cm_userid                                              		\n");
			strQuery.append(" order by CC_SEQNO				                                              		\n");
			
			pstmt = conn.prepareStatement(strQuery.toString());	
			//pstmt = new LoggableStatement(conn,strQuery.toString());
			pstmtcount = 1;
			pstmt.setString(pstmtcount++,etcData.get("IsrId"));
			pstmt.setString(pstmtcount++,etcData.get("SubId"));
			pstmt.setString(pstmtcount++,etcData.get("SeqNo"));
			pstmt.setString(pstmtcount++,etcData.get("UserId"));
	        ////ecamsLogger.error(((LoggableStatement)pstmt).getQueryString());           
	        rs = pstmt.executeQuery();
	        rsval.clear();
			while (rs.next()){			
				rst = new HashMap<String, String>();
				rst.put("ISRID",rs.getString("CC_ISRID"));
				rst.put("ISRSUB",rs.getString("CC_ISRSUB"));
				rst.put("CASESEQ",rs.getString("CC_CASESEQ"));
				rst.put("GBNCD",rs.getString("CC_GBNCD"));
				rst.put("SEQNO",rs.getString("CC_SEQNO"));
				rst.put("ITEMMSG", rs.getString("CC_ITEMMSG"));
				rst.put("LASTDT", rs.getString("laday"));
				rst.put("EDITOR",rs.getString("CC_EDITOR"));
				rst.put("editname",rs.getString("editname"));
				rsval.add(rst);
				rst = null;
			}//end of while-loop statement
			rs.close();
			pstmt.close();
			conn.close();
			conn = null;
			rs = null;
			pstmt = null;
			
			returnObjectArray = rsval.toArray();
			rsval.clear();
			rsval = null;
			return returnObjectArray;			
			
		} catch (SQLException sqlexception) {
			sqlexception.printStackTrace();
			ecamsLogger.error("## Cmr0040.getUnitTest() SQLException START ##");
			ecamsLogger.error("## Error DESC : ", sqlexception);	
			ecamsLogger.error("## Cmr0040.getUnitTest() SQLException END ##");			
			throw sqlexception;
		} catch (Exception exception) {
			exception.printStackTrace();
			ecamsLogger.error("## Cmr0040.getUnitTest() Exception START ##");				
			ecamsLogger.error("## Error DESC : ", exception);	
			ecamsLogger.error("## Cmr0040.getUnitTest() Exception END ##");				
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
					ecamsLogger.error("## Cmr0040.getUnitTest() connection release exception ##");
					ex3.printStackTrace();
				}
			}
		}
	}//end of getUnitTest() method statement
    
    public Object[] getUnitTest(String IsrId, String SubId, String UserId) throws SQLException, Exception{
		Connection        conn        = null;
		PreparedStatement pstmt       = null;
		ResultSet         rs          = null;
		StringBuffer      strQuery    = new StringBuffer();
		ArrayList<HashMap<String, String>>  rsval  = new ArrayList<HashMap<String, String>>();
		ArrayList<HashMap<String, String>>  rsconf = new ArrayList<HashMap<String, String>>();
		HashMap<String, String>			  rst		  = null;
		ConnectionContext connectionContext = new ConnectionResource();	
		String           strPrcSw       = "N";
		int				  pstmtcount  = 1;
		Object[] returnObjectArray		 = null;
		
		try {
			conn = connectionContext.getConnection();
			strQuery.append("select a.CC_ISRID, a.CC_ISRSUB, a.CC_CASESEQ, a.CC_SCMUSER, a.CC_STATUS, 			\n");			
			strQuery.append("       to_char(a.CC_CREATDT,'yyyy/mm/dd') crday, 					 				\n");
			strQuery.append("       to_char(a.CC_LASTDT,'yyyy/mm/dd') laday,									\n");
			strQuery.append("       a.CC_TESTUSER, a.CC_TESTDAY, a.CC_TESTRST,									\n");
			strQuery.append("       a.CC_CASENAME, b.CM_USERNAME scmname, c.cm_username testname				\n");
			strQuery.append("  from cmc0240 a, cmm0040 b, cmm0040 c												\n");
			strQuery.append(" where a.CC_SCMUSER = b.cm_userid                                                	\n");
			strQuery.append(" and   a.CC_TESTUSER = c.cm_userid				         							\n");
			strQuery.append(" and   a.CC_ISRID = ?							         							\n");
			strQuery.append(" and   a.CC_ISRSUB = ?							         							\n");
			strQuery.append(" and   a.CC_SCMUSER = ?							      					   		\n");
			pstmt = conn.prepareStatement(strQuery.toString());	
			//pstmt = new LoggableStatement(conn,strQuery.toString());
			pstmtcount = 1;
			pstmt.setString(pstmtcount++,IsrId);
			pstmt.setString(pstmtcount++,SubId);
			pstmt.setString(pstmtcount++,UserId);
	        ////ecamsLogger.error(((LoggableStatement)pstmt).getQueryString());           
	        rs = pstmt.executeQuery();
	        rsval.clear();
			while (rs.next()){			
				rst = new HashMap<String, String>();
				rst.put("CC_ISRID",rs.getString("CC_ISRID"));
				rst.put("CC_ISRSUB",rs.getString("CC_ISRSUB"));
				rst.put("CC_CASESEQ",rs.getString("CC_CASESEQ"));
				rst.put("CC_SCMUSER",rs.getString("CC_SCMUSER"));
				rst.put("CC_STATUS",rs.getString("CC_STATUS"));
				rst.put("CC_CREATDT", rs.getString("crday"));
				rst.put("CC_LASTDT", rs.getString("laday"));
				rst.put("CC_TESTUSER",rs.getString("CC_TESTUSER"));
				rst.put("CC_TESTDAY",rs.getString("CC_TESTDAY"));
				rst.put("CC_TESTRST",rs.getString("CC_TESTRST"));
				rst.put("CC_CASENAME",rs.getString("CC_CASENAME"));
				rst.put("scmname",rs.getString("scmname"));
				rst.put("testname",rs.getString("testname"));
				rsval.add(rst);
				rst = null;
			}//end of while-loop statement
			rs.close();
			pstmt.close();
			conn.close();
			conn = null;
			rs = null;
			pstmt = null;
			
			returnObjectArray = rsval.toArray();
			rsval.clear();
			rsval = null;
			return returnObjectArray;			
			
		} catch (SQLException sqlexception) {
			sqlexception.printStackTrace();
			ecamsLogger.error("## Cmr0040.getUnitTest() SQLException START ##");
			ecamsLogger.error("## Error DESC : ", sqlexception);	
			ecamsLogger.error("## Cmr0040.getUnitTest() SQLException END ##");			
			throw sqlexception;
		} catch (Exception exception) {
			exception.printStackTrace();
			ecamsLogger.error("## Cmr0040.getUnitTest() Exception START ##");				
			ecamsLogger.error("## Error DESC : ", exception);	
			ecamsLogger.error("## Cmr0040.getUnitTest() Exception END ##");				
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
					ecamsLogger.error("## Cmr0040.getUnitTest() connection release exception ##");
					ex3.printStackTrace();
				}
			}
		}
	}//end of getUnitTest() method statement
    
    
	public String setTcaseAdd(HashMap<String,String> etcData,ArrayList<HashMap<String,String>> TestList,ArrayList<HashMap<String,String>> CheckList) throws SQLException, Exception {
		Connection        conn        = null;
		PreparedStatement pstmt       = null;
		ResultSet         rs          = null;
		StringBuffer      strQuery    = new StringBuffer();
		String            retMsg      = "0";
		int				  seq		  = 1;
		int				  Tseq		  = 1;
		int				  Cseq		  = 1;
		String            strItemId   = null;
		boolean           insFg       = false;
		int				  pstmtcount  = 1;
		int               i = 0;
		boolean          findSw      = true;
		ConnectionContext connectionContext = new ConnectionResource();		
		
		try {
			conn = connectionContext.getConnection();
			
			if (etcData.get("scmsw").equals("1")) {
				if (etcData.get("testid") == null || etcData.get("testid") == "") { //INSERT
					strQuery.setLength(0);
					strQuery.append("select nvl(max(cc_caseseq),0) max		\n");			
					strQuery.append("  from cmc0240  			            \n");
					strQuery.append(" where CC_ISRID = ?	    		    \n");
					strQuery.append("   and CC_ISRSUB = ?					\n");
					pstmt = conn.prepareStatement(strQuery.toString());	
		//			pstmt = new LoggableStatement(conn,strQuery.toString());
					pstmtcount = 1;
					pstmt.setString(pstmtcount++,etcData.get("IsrId"));
					pstmt.setString(pstmtcount++,etcData.get("IsrSub"));
		//	        //ecamsLogger.error(((LoggableStatement)pstmt).getQueryString());           
			        rs = pstmt.executeQuery();
					if (rs.next()){
						seq = rs.getInt("max")+1;
						insFg = true;
					}//end of while-loop statement
					rs.close();
					pstmt.close();
				} 
				
				if (insFg == true) {
		        	strQuery.setLength(0);
		    		strQuery.append("insert into cmc0240 (CC_ISRID,CC_ISRSUB,CC_CASESEQ,CC_CASENAME,CC_SCMUSER,	\n");
		    		strQuery.append("   CC_CREATDT,CC_LASTDT,CC_TESTUSER,CC_TESTDAY,CC_TESTRST,CC_NOTHING)      \n");
		    		strQuery.append("   values															    	\n");
		    		strQuery.append("	(?,?,?,?,?,SYSDATE,SYSDATE,?,?,?,?) 								    \n");
		    		
		    		pstmt = conn.prepareStatement(strQuery.toString());
		//    		pstmt = new LoggableStatement(conn,strQuery.toString());
		    		pstmtcount = 1;
		          	pstmt.setString(pstmtcount++, etcData.get("IsrId"));
		          	pstmt.setString(pstmtcount++, etcData.get("IsrSub"));
		      	    pstmt.setInt(pstmtcount++, seq);
		      	    pstmt.setString(pstmtcount++, etcData.get("Tcase"));
		          	pstmt.setString(pstmtcount++, etcData.get("UserId"));
		          	pstmt.setString(pstmtcount++, etcData.get("UserId"));
		          	pstmt.setString(pstmtcount++, etcData.get("testday"));
		      	    pstmt.setString(pstmtcount++, etcData.get("testrst"));
		      	    if (etcData.get("passsw").equals("1")) pstmt.setString(pstmtcount++, "Y");
		      	    else pstmt.setString(pstmtcount++, "N");
	//	      	    //ecamsLogger.error(((LoggableStatement)pstmt).getQueryString());           
		      		pstmt.executeUpdate();           		
			        pstmt.close();
				} else {
					seq = Integer.parseInt(etcData.get("testid"));
		        	strQuery.setLength(0);
		    		strQuery.append("update cmc0240 set CC_CASENAME=?,  \n");
		    		if (etcData.get("testsw").equals("1")) {
		    			strQuery.append("   CC_TESTUSER=?,     	        \n");
		    			strQuery.append("   CC_TESTDAY=?,     	        \n");
		    			strQuery.append("   CC_TESTRST=?,     	        \n");
		    		}
		    		if (etcData.get("passsw").equals("1")) {
		    			strQuery.append("CC_NOTHING='Y',                \n");
		    		} else {
		    			strQuery.append("CC_NOTHING='N',                \n");
		    		}
		    		strQuery.append("   CC_LASTDT=SYSDATE	    	    \n");
					strQuery.append(" where CC_ISRID = ?	    	    \n");
					strQuery.append("   and CC_ISRSUB = ?				\n");
					strQuery.append("   and cc_caseseq = ?				\n");
		    		
		    		pstmt = conn.prepareStatement(strQuery.toString());
	//	    		pstmt = new LoggableStatement(conn,strQuery.toString());
		    		pstmtcount = 1;
		    		pstmt.setString(pstmtcount++, etcData.get("Tcase"));
		    		if (etcData.get("testsw").equals("1")) {
		    			pstmt.setString(pstmtcount++, etcData.get("UserId"));
			          	pstmt.setString(pstmtcount++, etcData.get("testday"));
			      	    pstmt.setString(pstmtcount++, etcData.get("testrst"));
		    		}
		          	pstmt.setString(pstmtcount++, etcData.get("IsrId"));
		          	pstmt.setString(pstmtcount++, etcData.get("IsrSub"));
		      	    pstmt.setInt(pstmtcount++, seq);
		//      	    //ecamsLogger.error(((LoggableStatement)pstmt).getQueryString());         
		      		pstmt.executeUpdate();           		
			        pstmt.close();
				}
			} else {
				seq = Integer.parseInt(etcData.get("testid"));
			}
				
			if (etcData.get("testsw").equals("1")) {
				insFg = false;
				strQuery.setLength(0);
				strQuery.append("select count(*) cnt from cmc0242     \n");
				strQuery.append(" where cc_isrid=? and cc_isrsub=?    \n");
				strQuery.append("   and cc_caseseq=? and cc_testseq=? \n");
	    		
	    		pstmt = conn.prepareStatement(strQuery.toString());
	  //  		pstmt = new LoggableStatement(conn,strQuery.toString());
	    		pstmtcount = 1;
	    		pstmt.setString(pstmtcount++, etcData.get("IsrId"));
	          	pstmt.setString(pstmtcount++, etcData.get("IsrSub"));
	          	pstmt.setInt(pstmtcount++, seq);
	          	pstmt.setString(pstmtcount++, etcData.get("testseq"));
	    //      	//ecamsLogger.error(((LoggableStatement)pstmt).getQueryString());         
	          	rs = pstmt.executeQuery();
	          	if (rs.next()) {
	          		if (rs.getInt("cnt")==0) {
	          			insFg = true;
	          		}
	          	}
	          	rs.close();
	          	pstmt.close();
	          	
	          	if (insFg == true) {
		        	strQuery.setLength(0);
		    		strQuery.append("insert into cmc0242 (CC_ISRID,CC_ISRSUB,CC_CASESEQ,CC_TESTSEQ,	\n");
		    		strQuery.append("   CC_LASTDT,CC_TESTUSER,CC_TESTDAY,CC_TESTRST)     	        \n");
		    		strQuery.append("   values	(?,?,?,?,SYSDATE,?,?,?) 						    \n");
		    		
		    		pstmt = conn.prepareStatement(strQuery.toString());
		//    		pstmt = new LoggableStatement(conn,strQuery.toString());
		    		pstmtcount = 1;
		          	pstmt.setString(pstmtcount++, etcData.get("IsrId"));
		          	pstmt.setString(pstmtcount++, etcData.get("IsrSub"));
		      	    pstmt.setInt(pstmtcount++, seq);
		      	    pstmt.setString(pstmtcount++, etcData.get("testseq"));
		          	pstmt.setString(pstmtcount++, etcData.get("UserId"));
		          	pstmt.setString(pstmtcount++, etcData.get("testday"));
		      	    pstmt.setString(pstmtcount++, etcData.get("testrst"));
		//      	    //ecamsLogger.error(((LoggableStatement)pstmt).getQueryString());         
		      		pstmt.executeUpdate();           		
			        pstmt.close();
				} else {
		        	strQuery.setLength(0);
		    		strQuery.append("update cmc0242 set CC_TESTUSER=?,  \n");
	    			strQuery.append("   CC_TESTDAY=?,     	            \n");
	    			strQuery.append("   CC_TESTRST=?,     	            \n");
		    		strQuery.append("   CC_LASTDT=SYSDATE	    	    \n");
					strQuery.append(" where CC_ISRID = ?	    	    \n");
					strQuery.append("   and CC_ISRSUB = ?				\n");
					strQuery.append("   and cc_caseseq = ?				\n");
					strQuery.append("   and cc_testseq = ?				\n");
		    		
		    		pstmt = conn.prepareStatement(strQuery.toString());
		//    		pstmt = new LoggableStatement(conn,strQuery.toString());
		    		pstmtcount = 1;
	    			pstmt.setString(pstmtcount++, etcData.get("UserId"));
		          	pstmt.setString(pstmtcount++, etcData.get("testday"));
		      	    pstmt.setString(pstmtcount++, etcData.get("testrst"));
		          	pstmt.setString(pstmtcount++, etcData.get("IsrId"));
		          	pstmt.setString(pstmtcount++, etcData.get("IsrSub"));
		      	    pstmt.setInt(pstmtcount++, seq);
		      	    pstmt.setString(pstmtcount++, etcData.get("testseq"));
		//      	    //ecamsLogger.error(((LoggableStatement)pstmt).getQueryString());         
		      		pstmt.executeUpdate();           		
			        pstmt.close();
				}
			}
			
			if (etcData.get("passsw").equals("0")) {	
				if (etcData.get("scmsw").equals("1")) {
					strQuery.setLength(0);
					strQuery.append("delete cmc0241      \n");
					strQuery.append(" where cc_isrid=?   \n");
					strQuery.append("   and cc_isrsub=?  \n");
					strQuery.append("   and cc_caseseq=? \n");
					pstmt = conn.prepareStatement(strQuery.toString());
			//		pstmt = new LoggableStatement(conn,strQuery.toString());
					pstmtcount = 1;
		          	pstmt.setString(pstmtcount++, etcData.get("IsrId"));
		          	pstmt.setString(pstmtcount++, etcData.get("IsrSub"));
		      	    pstmt.setInt(pstmtcount++, seq);
		   //   	    //ecamsLogger.error(((LoggableStatement)pstmt).getQueryString());         
		      	    pstmt.executeUpdate();
		      	    pstmt.close();
				}	
				if (etcData.get("testsw").equals("1")) {
					strQuery.setLength(0);
					strQuery.append("delete cmc0243      \n");
					strQuery.append(" where cc_isrid=?   \n");
					strQuery.append("   and cc_isrsub=?  \n");
					strQuery.append("   and cc_caseseq=? \n");
					strQuery.append("   and cc_testseq=? \n");
					pstmt = conn.prepareStatement(strQuery.toString());
			//		pstmt = new LoggableStatement(conn,strQuery.toString());
					pstmtcount = 1;
		          	pstmt.setString(pstmtcount++, etcData.get("IsrId"));
		          	pstmt.setString(pstmtcount++, etcData.get("IsrSub"));
		      	    pstmt.setInt(pstmtcount++, seq);
		      	    pstmt.setString(pstmtcount++, etcData.get("testseq"));
		    //  	    //ecamsLogger.error(((LoggableStatement)pstmt).getQueryString());         
		      	    pstmt.executeUpdate();
		      	    pstmt.close();
		      	    
		      	    
				}
			}
			if (etcData.get("passsw").equals("0")) {
	      	    Tseq = 0;
			    for(i=0;i<TestList.size();i++){		
			    	if (etcData.get("scmsw").equals("1")) {
				    	strQuery.setLength(0);
			    		strQuery.append("insert into cmc0241 (CC_ISRID,CC_ISRSUB,CC_CASESEQ,CC_GBNCD,CC_SEQNO,			\n");
			    		strQuery.append("   CC_ITEMMSG,CC_LASTDT,CC_EDITOR												\n");
			    		strQuery.append("   )values(															    	\n");
			    		strQuery.append("	?,?,?,'C',?,?,SYSDATE,?)													\n");
			    		
			    		pstmt = conn.prepareStatement(strQuery.toString());
			 //   		pstmt = new LoggableStatement(conn,strQuery.toString());
			    		pstmtcount = 1;
			          	pstmt.setString(pstmtcount++, etcData.get("IsrId"));
			          	pstmt.setString(pstmtcount++, etcData.get("IsrSub"));
			      	    pstmt.setInt(pstmtcount++, seq);
			      	    pstmt.setInt(pstmtcount++, ++Tseq);
			          	pstmt.setString(pstmtcount++, TestList.get(i).get("ITEMMSG"));
			      	    pstmt.setString(pstmtcount++, etcData.get("UserId"));
			  //    	    //ecamsLogger.error(((LoggableStatement)pstmt).getQueryString());         
			      		pstmt.executeUpdate();           		
				        pstmt.close();
			    	} 
			    }
		    	for(i=0;i<CheckList.size();i++){
		    		if (etcData.get("scmsw").equals("1")) {
				    	strQuery.setLength(0);
				    	strQuery.append("insert into cmc0241 (CC_ISRID,CC_ISRSUB,CC_CASESEQ,CC_GBNCD,CC_SEQNO,			\n");
			    		strQuery.append("   CC_ITEMMSG,CC_LASTDT,CC_EDITOR												\n");
			    		strQuery.append("   )values(													 	    	    \n");
			    		strQuery.append("	?,?,?,'R',?,?,SYSDATE,?)													\n");
			    		
			    		pstmt = conn.prepareStatement(strQuery.toString());
			   // 		pstmt = new LoggableStatement(conn,strQuery.toString());
			    		pstmtcount = 1;
			          	pstmt.setString(pstmtcount++, etcData.get("IsrId"));
			          	pstmt.setString(pstmtcount++, etcData.get("IsrSub"));
			      	    pstmt.setInt(pstmtcount++, seq);
			      	    pstmt.setInt(pstmtcount++, ++Tseq);
			      	    pstmt.setString(pstmtcount++, CheckList.get(i).get("ITEMMSG"));
			      	    pstmt.setString(pstmtcount++, etcData.get("UserId"));
			  //    	    //ecamsLogger.error(((LoggableStatement)pstmt).getQueryString());         
			      		pstmt.executeUpdate();           		
				        pstmt.close();
		    		} else {
		    			Tseq = Integer.parseInt(CheckList.get(i).get("cc_seqno"));
		    		}
		    		
		    		if (etcData.get("testsw").equals("1")) {				      	    
			    		strQuery.setLength(0);
			    		strQuery.append("insert into cmc0243 (CC_ISRID,CC_ISRSUB,CC_CASESEQ,CC_GBNCD,CC_SEQNO, \n");
			    		strQuery.append("          CC_TESTSEQ,CC_LASTDT,CC_TESTUSER,CC_TESTDAY,CC_TESTRST)     \n");
			    		strQuery.append("   values (?,?,?,'R',?,?,SYSDATE,?,?,?)             			       \n");				    		
			    		pstmt = conn.prepareStatement(strQuery.toString());
			   // 		pstmt = new LoggableStatement(conn,strQuery.toString());
			    		pstmtcount = 1;
			          	pstmt.setString(pstmtcount++, etcData.get("IsrId"));
			          	pstmt.setString(pstmtcount++, etcData.get("IsrSub"));
			      	    pstmt.setInt(pstmtcount++, seq);
			      	    pstmt.setInt(pstmtcount++, Tseq);
			      	    pstmt.setString(pstmtcount++, etcData.get("testseq"));
			      	    pstmt.setString(pstmtcount++, etcData.get("UserId"));
			          	pstmt.setString(pstmtcount++, etcData.get("testday"));
			          	if (CheckList.get(i).get("selected1").equals("1")) {
			          		pstmt.setString(pstmtcount++, "Y");
			          	} else {
			          		pstmt.setString(pstmtcount++, "N");
			          	}
			    //  	    //ecamsLogger.error(((LoggableStatement)pstmt).getQueryString());         
			      		pstmt.executeUpdate();           		
				        pstmt.close();
			    	}
			    }
			}
			strQuery.setLength(0);
			if (etcData.get("reqcd").equals("44")) {			
	    		strQuery.append("update cmc0110 set cc_substatus='23',  \n");
	    		strQuery.append("       cc_mainstatus=decode(cc_mainstatus,'02',cc_mainstatus,'02') \n");
		        strQuery.append(" where cc_isrid=? and cc_isrsub=?     \n");
		        strQuery.append("   and cc_substatus in ('21','36')    \n");
			} else {			
	    		strQuery.append("update cmc0110 set cc_substatus='32',  \n");
	    		strQuery.append("       cc_mainstatus=decode(cc_mainstatus,'02','03',cc_mainstatus) \n");
		        strQuery.append(" where cc_isrid=? and cc_isrsub=?     \n");
		        strQuery.append("   and cc_substatus in ('31','35')    \n");
			}
	        pstmt = conn.prepareStatement(strQuery.toString());
//	        pstmt = new LoggableStatement(conn,strQuery.toString());
	        pstmt.setString(1, etcData.get("IsrId"));
	        pstmt.setString(2, etcData.get("IsrSub"));
//	        //ecamsLogger.error(((LoggableStatement)pstmt).getQueryString());         
	        pstmt.executeUpdate();
        	pstmt.close();
        	
        	findSw = false;
        	if (etcData.get("reqcd").equals("44")) {
	        	strQuery.setLength(0);                		
	    		strQuery.append("select count(*) cnt   	               \n");      		
	    		strQuery.append("  from cmc0260 a,cmc0110 b 	       \n");              		
	    		strQuery.append(" where	b.cc_isrid=? and b.cc_isrsub=? \n");         		
	    		strQuery.append("   and b.cc_substatus in ('23','24')  \n");  
	    		strQuery.append("   and b.cc_isrid=a.cc_isrid  		   \n"); 
	    		strQuery.append("   and b.cc_isrsub=a.cc_isrsub	       \n");
	    		pstmt = conn.prepareStatement(strQuery.toString());	
				//pstmt = new LoggableStatement(conn,strQuery.toString());
				pstmtcount = 1;
				pstmt.setString(pstmtcount++,etcData.get("IsrId"));
				pstmt.setString(pstmtcount++,etcData.get("IsrSub"));
		        ////ecamsLogger.error(((LoggableStatement)pstmt).getQueryString());           
				rs = pstmt.executeQuery();
				if (rs.next()) {
					if (rs.getInt("cnt")>0) {
						findSw = true;
					}
				}
				pstmt.close();
				rs.close();
				
				if (findSw == true) {
					findSw = false;
					strQuery.setLength(0);                		
		    		strQuery.append("select count(*) cnt   	               \n");      		
		    		strQuery.append("  from cmc0110     	               \n");              		
		    		strQuery.append(" where	cc_isrid=? and cc_isrsub=?     \n");         		
		    		strQuery.append("   and TESTREQ_TOTAL(cc_isrid,cc_isrsub)='OK'       \n");  
		    		strQuery.append("   and TESTENDYN_TOTAL(cc_isrid,cc_isrsub,'Y')='OK' \n"); 
		    		pstmt = conn.prepareStatement(strQuery.toString());	
					//pstmt = new LoggableStatement(conn,strQuery.toString());
					pstmtcount = 1;
					pstmt.setString(pstmtcount++,etcData.get("IsrId"));
					pstmt.setString(pstmtcount++,etcData.get("IsrSub"));
			        ////ecamsLogger.error(((LoggableStatement)pstmt).getQueryString());           
					rs = pstmt.executeQuery();
					if (rs.next()) {
						if (rs.getInt("cnt")>0) {
							findSw = true;
						}
					}
					pstmt.close();
					rs.close();
					
					if (findSw == true) {
						strQuery.setLength(0);                		
			    		strQuery.append("update cmc0110 set 	  \n");             		
			    		strQuery.append("    cc_substatus='25'    \n");  
			    		strQuery.append(" where CC_ISRID=?		  \n");
			    		strQuery.append("   and CC_ISRSUB=?		  \n");
			    		pstmt = conn.prepareStatement(strQuery.toString());	
//						pstmt = new LoggableStatement(conn,strQuery.toString());
						pstmtcount = 1;
						pstmt.setString(pstmtcount++,etcData.get("IsrId"));
						pstmt.setString(pstmtcount++,etcData.get("IsrSub"));
	//			        //ecamsLogger.error(((LoggableStatement)pstmt).getQueryString());           
						pstmt.executeUpdate(); 
						pstmt.close();
					}
				}
        	}
	        conn.commit();
	        
	        conn.close();
			conn = null;
			rs = null;
			pstmt = null;
	        
	        return retMsg;
	        
	        
		} catch (SQLException sqlexception) {
			sqlexception.printStackTrace();
			if (rs != null)     try{rs.close();}catch (Exception ex){ex.printStackTrace();}
			if (pstmt != null)  try{pstmt.close();}catch (Exception ex2){ex2.printStackTrace();}
			if (conn != null){
				try{
					conn.rollback();
					ConnectionResource.release(conn);
				}catch(Exception ex3){
					ecamsLogger.error("## Cmr0040.setTcaseAdd() connection release exception ##");
					ex3.printStackTrace();
				}				
			}			
			ecamsLogger.error("## Cmr0040.setTcaseAdd() SQLException START ##");
			ecamsLogger.error("## Error DESC : ", sqlexception);	
			ecamsLogger.error("## Cmr0040.setTcaseAdd() SQLException END ##");			
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
					ecamsLogger.error("## Cmr0040.setTcaseAdd() connection release exception ##");
					ex3.printStackTrace();
				}				
			}			
			ecamsLogger.error("## Cmr0040.setTcaseAdd() Exception START ##");				
			ecamsLogger.error("## Error DESC : ", exception);	
			ecamsLogger.error("## Cmr0040.setTcaseAdd() Exception END ##");				
			throw exception;
		}finally{
			if (strQuery != null) 	strQuery = null;
			if (rs != null)     try{rs.close();}catch (Exception ex){ex.printStackTrace();}
			if (pstmt != null)  try{pstmt.close();}catch (Exception ex2){ex2.printStackTrace();}
			if (conn != null){
				try{
					conn.commit();
					conn.close();
					conn = null;
				}catch(Exception ex3){
					ecamsLogger.error("## Cmr0040.setTcaseAdd() connection release exception ##");
					ex3.printStackTrace();
				}
			}
		}
	}//end of setTcaseAdd() method statement
	
	public String delCase(HashMap<String,String> etcData) throws SQLException, Exception {
		Connection        conn        = null;
		PreparedStatement pstmt       = null;
		ResultSet         rs          = null;
		StringBuffer      strQuery    = new StringBuffer();
		String            retMsg      = "0";
		int				  pstmtcount  = 1;
		ConnectionContext connectionContext = new ConnectionResource();		
		
		try {
			conn = connectionContext.getConnection();
			
        	strQuery.setLength(0);
    		strQuery.append("update cmc0240 set cc_status='3',  \n");
    		strQuery.append("       cc_lastdt=SYSDATE           \n");
    		strQuery.append(" where CC_ISRID=?					\n");
    		strQuery.append("   and CC_ISRSUB=?					\n");
    		strQuery.append("   and CC_CASESEQ=?				\n");
    		
    		pstmt = conn.prepareStatement(strQuery.toString());
  //  		pstmt = new LoggableStatement(conn,strQuery.toString());     
    		pstmtcount = 1;
      	    pstmt.setString(pstmtcount++, etcData.get("IsrId"));
      	    pstmt.setString(pstmtcount++, etcData.get("IsrSub"));
      	  	pstmt.setString(pstmtcount++, etcData.get("testid"));
	//        //ecamsLogger.error(((LoggableStatement)pstmt).getQueryString()); 
      		pstmt.executeUpdate();           		
	        pstmt.close();
		        
		    retMsg = "0";
	        conn.close();
			conn = null;
			pstmt = null;
	        
	        return retMsg;
	        
	        
		} catch (SQLException sqlexception) {
			sqlexception.printStackTrace();
			ecamsLogger.error("## Cmr0040.delCase() SQLException START ##");
			ecamsLogger.error("## Error DESC : ", sqlexception);	
			ecamsLogger.error("## Cmr0040.delCase() SQLException END ##");			
			throw sqlexception;
		} catch (Exception exception) {
			exception.printStackTrace();
			ecamsLogger.error("## Cmr0040.delCase() Exception START ##");				
			ecamsLogger.error("## Error DESC : ", exception);	
			ecamsLogger.error("## Cmr0040.delCase() Exception END ##");				
			throw exception;
		}finally{
			if (strQuery != null) 	strQuery = null;
			if (rs != null)     try{rs.close();}catch (Exception ex){ex.printStackTrace();}
			if (pstmt != null)  try{pstmt.close();}catch (Exception ex2){ex2.printStackTrace();}
			if (conn != null){
				try{
					conn.close();
					conn = null;
				}catch(Exception ex3){
					ecamsLogger.error("## Cmr0040.TcaseDel() connection release exception ##");
					ex3.printStackTrace();
				}
			}
		}
	}//end of TcaseDel() method statement
	
	public Object[] chkExcelList(ArrayList<HashMap<String,String>> FileList,String ReqCd) 
	throws SQLException, Exception {
		boolean           errSw       = false;
		HashMap<String, String> rst	  = null;
		ArrayList<HashMap<String, String>>  rsval = new ArrayList<HashMap<String, String>>();
		
		try {
			
			for (int i=0 ; i<FileList.size() ; i++)	{
				errSw = false;
				rst = new HashMap<String, String>();
				rst = FileList.get(i);
				if (FileList.get(i).get("casename") == null || FileList.get(i).get("casename") == "") {					
					rst.put("errmsg", "테스트케이스명");
					errSw = true;
				}
				
				if (ReqCd.equals("44")) {           //통합테스트
					if (errSw == false) {
						if (FileList.get(i).get("casecond1") == null || FileList.get(i).get("casecond1") == "") {
							rst.put("errmsg", "테스트조건1");
							errSw = true;
						}
					}
	
					if (errSw == false) {
						if (FileList.get(i).get("casechk1") == null || FileList.get(i).get("casechk1") == "") {
							rst.put("errmsg", "확인사항1");
							errSw = true;
						}
					}
				} else {                            //단위테스트
					if (errSw == false) {
						if (FileList.get(i).get("casegbn") == null || FileList.get(i).get("casegbn") == "") {
							rst.put("errmsg", "테스트구분");
							errSw = true;
						}
					}
	
					if (errSw == false) {
						if (FileList.get(i).get("caserst") == null || FileList.get(i).get("caserst") == "") {
							rst.put("errmsg", "예상결과");
							errSw = true;
						}
					}
					
				}
				if (errSw == true) {
					rst.put("errsw", "Y");					
				} else {
					rst.put("errmsg", "정상");
					rst.put("errsw", "N");					
				}
				FileList.set(i, rst);
				rst = null;
			}
			
	        
	        return FileList.toArray();
	        
	        
		} catch (Exception exception) {
			exception.printStackTrace();
			ecamsLogger.error("## Cmr0040.chkExcelList() Exception START ##");				
			ecamsLogger.error("## Error DESC : ", exception);	
			ecamsLogger.error("## Cmr0040.chkExcelList() Exception END ##");				
			throw exception;
		}finally{
		}
	}//end of chkExcelList() method statement
	
	public String setExcelList(ArrayList<HashMap<String,String>> FileList, HashMap<String,String> etcData) 
	throws SQLException, Exception {
		Connection        conn        = null;
		PreparedStatement pstmt       = null;
		ResultSet         rs          = null;
		StringBuffer      strQuery    = new StringBuffer();
		String            retMsg      = "0";
		int				  seq		  = 1;
		int				  i		  	  = 0;
		int				  pstmtcount  = 1;
		ConnectionContext connectionContext = new ConnectionResource();		
		
		try {			
			conn = connectionContext.getConnection();
			
			strQuery.setLength(0);
			strQuery.append("select nvl(max(cc_caseseq),0) maxseq from cmc0240  \n");
			strQuery.append(" where CC_ISRID=? and   CC_ISRSUB=?	            \n");
			pstmt = conn.prepareStatement(strQuery.toString());	
			//pstmt = new LoggableStatement(conn,strQuery.toString());
			pstmtcount = 1;
			pstmt.setString(pstmtcount++,etcData.get("isrid"));
			pstmt.setString(pstmtcount++,etcData.get("isrsub"));
	        ////ecamsLogger.error(((LoggableStatement)pstmt).getQueryString());           
	        rs = pstmt.executeQuery();
			if (rs.next()){
				seq = rs.getInt("maxseq");
				++seq;
			}//end of while-loop statement
			rs.close();
			pstmt.close();
			
			int k = 0;
			int j = 0;
			String  itemMsg = "";
			boolean findSw = false;
			
			for(i=0;i<FileList.size();i++){
	        	strQuery.setLength(0);
	    		strQuery.append("insert into CMC0240(CC_ISRID,CC_ISRSUB,CC_CASESEQ,	    \n");
	    		strQuery.append("   CC_CASENAME,CC_SCMUSER,CC_STATUS,CC_CREATDT,        \n");
	    		strQuery.append("   CC_LASTDT,CC_NOTHING)				    	        \n");
	    		strQuery.append("	values(?,?,?,?,?,'0',SYSDATE,SYSDATE,'N')			\n");
	    		
	    		pstmt = conn.prepareStatement(strQuery.toString());
	    		pstmtcount = 1;
	          	pstmt.setString(pstmtcount++, etcData.get("isrid"));
	          	pstmt.setString(pstmtcount++, etcData.get("isrsub"));
	      	    pstmt.setInt(pstmtcount++, seq);
	          	pstmt.setString(pstmtcount++, FileList.get(i).get("casename"));
	          	pstmt.setString(pstmtcount++, etcData.get("userid"));
	      		pstmt.executeUpdate();
	      		pstmt.close();
	      		
	      		k = 0;
	      		for (j=0;5>j;j++) {
	      			itemMsg = "";
	      			findSw = false;
	      			if (j == 0) {
			      		if (FileList.get(i).get("casecond1") != null && FileList.get(i).get("casecond1") != "") {
			      			itemMsg = FileList.get(i).get("casecond1");
			      			findSw = true;
			      		}
	      			} else if (j == 1) {
			      		if (FileList.get(i).get("casecond2") != null && FileList.get(i).get("casecond2") != "") {
			      			itemMsg = FileList.get(i).get("casecond2");
			      			findSw = true;
			      		}
	      			}else if (j == 2) {
			      		if (FileList.get(i).get("casecond3") != null && FileList.get(i).get("casecond3") != "") {
			      			itemMsg = FileList.get(i).get("casecond3");
			      			findSw = true;
			      		}
	      			}else if (j == 3) {
			      		if (FileList.get(i).get("casecond4") != null && FileList.get(i).get("casecond4") != "") {
			      			itemMsg = FileList.get(i).get("casecond4");
			      			findSw = true;
			      		}
	      			}else if (j == 4) {
			      		if (FileList.get(i).get("casecond5") != null && FileList.get(i).get("casecond5") != "") {
			      			itemMsg = FileList.get(i).get("casecond5");
			      			findSw = true;
			      		}
	      			}
	      			
	      			if (findSw == true) {
			      		strQuery.setLength(0);
			    		strQuery.append("insert into CMC0241(CC_ISRID, CC_ISRSUB, CC_CASESEQ,					\n");
			    		strQuery.append("   CC_GBNCD, CC_SEQNO, CC_ITEMMSG, CC_LASTDT, CC_EDITOR)				\n");
			    		strQuery.append("   values(?,?,?,'C',?,?,SYSDATE,?)									    		\n");
			    		
			    		pstmt = conn.prepareStatement(strQuery.toString());
			    		pstmtcount = 1;
			          	pstmt.setString(pstmtcount++, etcData.get("isrid"));
			          	pstmt.setString(pstmtcount++, etcData.get("isrsub"));
			      	    pstmt.setInt(pstmtcount++, seq);
			      	    pstmt.setInt(pstmtcount++, ++k);
			          	pstmt.setString(pstmtcount++, itemMsg);
			          	pstmt.setString(pstmtcount++, etcData.get("userid"));
			      		pstmt.executeUpdate();
			      		pstmt.close();
	      			}
	      		}
	      		k = 0;
	      		for (j=0;5>j;j++) {
	      			itemMsg = "";
	      			findSw = false;
	      			if (j == 0) {
			      		if (FileList.get(i).get("casechk1") != null && FileList.get(i).get("casechk1") != "") {
			      			itemMsg = FileList.get(i).get("casechk1");
			      			findSw = true;
			      		}
	      			} else if (j == 1) {
			      		if (FileList.get(i).get("casechk2") != null && FileList.get(i).get("casechk2") != "") {
			      			itemMsg = FileList.get(i).get("casechk2");
			      			findSw = true;
			      		}
	      			}else if (j == 2) {
			      		if (FileList.get(i).get("casechk3") != null && FileList.get(i).get("casechk3") != "") {
			      			itemMsg = FileList.get(i).get("casechk3");
			      			findSw = true;
			      		}
	      			}else if (j == 3) {
			      		if (FileList.get(i).get("casechk4") != null && FileList.get(i).get("casechk4") != "") {
			      			itemMsg = FileList.get(i).get("casechk4");
			      			findSw = true;
			      		}
	      			}else if (j == 4) {
			      		if (FileList.get(i).get("casechk5") != null && FileList.get(i).get("casechk5") != "") {
			      			itemMsg = FileList.get(i).get("casechk5");
			      			findSw = true;
			      		}
	      			}
	      			
	      			if (findSw == true) {
			      		strQuery.setLength(0);
			    		strQuery.append("insert into CMC0241(CC_ISRID, CC_ISRSUB, CC_CASESEQ,					\n");
			    		strQuery.append("   CC_GBNCD, CC_SEQNO, CC_ITEMMSG, CC_LASTDT, CC_EDITOR)				\n");
			    		strQuery.append("   values(?,?,?,'R',?,?,SYSDATE,?)									    		\n");
			    		
			    		pstmt = conn.prepareStatement(strQuery.toString());
			    		pstmtcount = 1;
			          	pstmt.setString(pstmtcount++, etcData.get("isrid"));
			          	pstmt.setString(pstmtcount++, etcData.get("isrsub"));
			      	    pstmt.setInt(pstmtcount++, seq);
			      	    pstmt.setInt(pstmtcount++, ++k);
			          	pstmt.setString(pstmtcount++, itemMsg);
			          	pstmt.setString(pstmtcount++, etcData.get("userid"));
			      		pstmt.executeUpdate();
			      		pstmt.close();
	      			}
	      		}
	      		++seq;
	      		
			}
	        	
			strQuery.setLength(0);
	        strQuery.append("update cmc0110 set cc_substatus='23',  \n");
    		strQuery.append("       cc_mainstatus=decode(cc_mainstatus,'02',cc_mainstatus,'02') \n");
	        strQuery.append(" where cc_isrid=? and cc_isrsub=?     \n");
	        strQuery.append("   and cc_substatus in ('21','36')    \n");
	        pstmt = conn.prepareStatement(strQuery.toString());
	//        pstmt = new LoggableStatement(conn,strQuery.toString());
	        pstmt.setString(1, etcData.get("isrid"));
	        pstmt.setString(2, etcData.get("isrsub"));
	//        //ecamsLogger.error(((LoggableStatement)pstmt).getQueryString());         
	        pstmt.executeUpdate();
        	pstmt.close();
        	
		    retMsg = "0";
	        
	        conn.close();
	        rs.close();
	        
			conn = null;
			rs = null;
			pstmt = null;
	        
	        return retMsg;
	        
	        
		} catch (SQLException sqlexception) {
			sqlexception.printStackTrace();
			ecamsLogger.error("## Cmr0040.setExcelList() SQLException START ##");
			ecamsLogger.error("## Error DESC : ", sqlexception);	
			ecamsLogger.error("## Cmr0040.setExcelList() SQLException END ##");			
			throw sqlexception;
		} catch (Exception exception) {
			exception.printStackTrace();
			ecamsLogger.error("## Cmr0040.setExcelList() Exception START ##");				
			ecamsLogger.error("## Error DESC : ", exception);	
			ecamsLogger.error("## Cmr0040.setExcelList() Exception END ##");				
			throw exception;
		}finally{
			if (strQuery != null) 	strQuery = null;
			if (rs != null)     try{rs.close();}catch (Exception ex){ex.printStackTrace();}
			if (pstmt != null)  try{pstmt.close();}catch (Exception ex2){ex2.printStackTrace();}
			if (conn != null){
				try{
					conn.close();
					conn = null;
				}catch(Exception ex3){
					ecamsLogger.error("## Cmr0040.setExcelList() connection release exception ##");
					ex3.printStackTrace();
				}
			}
		}
	}//end of setExcelList() method statement
}//end of Cmr0020 class statement