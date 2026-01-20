/*****************************************************************************************
	1. program ID	: Cmt0900.java
	2. create date	: 2010.12. 3
	3. auth		    : is.choi
	4. update date	: 
	5. auth		    : 
	6. description	: 1. Test Management End Proc
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
public class Cmt0900{    
	

    /**
     * Logger Class Instance Creation
     * logger
     */
	
    Logger ecamsLogger  = EcamsLogger.getLoggerInstance();
    
	
    public String setTestEnd_OK(HashMap<String,String> etcData) throws SQLException, Exception {
    	Connection        conn        = null;
		PreparedStatement pstmt       = null;
		ResultSet         rs          = null;
		StringBuffer      strQuery    = new StringBuffer();
		ConnectionContext connectionContext = new ConnectionResource();

		try {
			conn = connectionContext.getConnection();       
						
	        strQuery.setLength(0);	
	        strQuery.append("update cmc0310 set cc_eddate=SYSDATE,  \n");
	        strQuery.append("       cc_testrst=?,cc_testmsg=?,      \n");
	        strQuery.append("       cc_status='9'                   \n");
	        strQuery.append(" where cc_isrid=? and cc_isrsub=?      \n");
	        strQuery.append("   and cc_testseq=?                    \n");
	        pstmt = conn.prepareStatement(strQuery.toString());
	        pstmt.setString(1, etcData.get("endgbn"));
	        pstmt.setString(2, etcData.get("endmsg"));
	        pstmt.setString(3, etcData.get("isrid"));
	        pstmt.setString(4, etcData.get("isrsub"));
	        pstmt.setString(5, etcData.get("testseq"));
	        pstmt.executeUpdate();
	        pstmt.close();
	        
        	strQuery.setLength(0);
    		strQuery.append("update cmc0110 set        \n");
    		strQuery.append("       cc_substatus=decode(?,'Y','39','36'), \n");
    		strQuery.append("       cc_mainstatus=decode(cc_mainstatus,'02','03',cc_mainstatus) \n");
	        strQuery.append(" where cc_isrid=? and cc_isrsub=?     \n");
	        pstmt = conn.prepareStatement(strQuery.toString());
	        pstmt.setString(1, etcData.get("endgbn"));
	        pstmt.setString(2, etcData.get("isrid"));
	        pstmt.setString(3, etcData.get("isrsub"));
	        pstmt.executeUpdate();
        	pstmt.close();
        	
        	conn.close();
        	conn = null;
        	return "OK";
			
		} catch (SQLException sqlexception) {
			sqlexception.printStackTrace();
			ecamsLogger.error("## Cmt0900.setTestEnd_OK() SQLException START ##");
			ecamsLogger.error("## Error DESC : ", sqlexception);	
			ecamsLogger.error("## Cmt0900.setTestEnd_OK() SQLException END ##");			
			throw sqlexception;
		} catch (Exception exception) {
			exception.printStackTrace();
			ecamsLogger.error("## Cmt0900.setTestEnd_OK() Exception START ##");				
			ecamsLogger.error("## Error DESC : ", exception);	
			ecamsLogger.error("## Cmt0900.setTestEnd_OK() Exception END ##");				
			throw exception;
		}finally{
			if (strQuery != null) 	strQuery = null;
			if (rs != null)     try{rs.close();}catch (Exception ex){ex.printStackTrace();}
			if (pstmt != null)  try{pstmt.close();}catch (Exception ex2){ex2.printStackTrace();}
			if (conn != null){
				try{
					ConnectionResource.release(conn);
				}catch(Exception ex3){
					ecamsLogger.error("## Cmt0900.setTestEnd_OK() connection release exception ##");
					ex3.printStackTrace();
				}
			}
		}
	} //setTestEnd_OK

    public String setTestEnd_Cncl(HashMap<String,String> etcData) throws SQLException, Exception {
    	Connection        conn        = null;
		PreparedStatement pstmt       = null;
		ResultSet         rs          = null;
		StringBuffer      strQuery    = new StringBuffer();
		ConnectionContext connectionContext = new ConnectionResource();

		try {
			conn = connectionContext.getConnection();       
						
	        strQuery.setLength(0);	
	        strQuery.append("update cmc0310 set cc_endreqdt='',     \n");
	        strQuery.append("       cc_endrequsr='',cc_testmsg=?,   \n");
	        strQuery.append("       cc_status='3'                   \n");
	        strQuery.append(" where cc_isrid=? and cc_isrsub=?      \n");
	        strQuery.append("   and cc_testseq=?                    \n");
	        pstmt = conn.prepareStatement(strQuery.toString());
	        pstmt.setString(1, etcData.get("endmsg"));
	        pstmt.setString(2, etcData.get("isrid"));
	        pstmt.setString(3, etcData.get("isrsub"));
	        pstmt.setString(4, etcData.get("testseq"));
	        pstmt.executeUpdate();
	        pstmt.close();
	        
        	strQuery.setLength(0);
    		strQuery.append("update cmc0110 set        \n");
    		strQuery.append("       cc_substatus='35', \n");
    		strQuery.append("       cc_mainstatus=decode(cc_mainstatus,'02','03',cc_mainstatus) \n");
	        strQuery.append(" where cc_isrid=? and cc_isrsub=?     \n");
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
			ecamsLogger.error("## Cmt0900.setTestEnd_Cncl() SQLException START ##");
			ecamsLogger.error("## Error DESC : ", sqlexception);	
			ecamsLogger.error("## Cmt0900.setTestEnd_Cncl() SQLException END ##");			
			throw sqlexception;
		} catch (Exception exception) {
			exception.printStackTrace();
			ecamsLogger.error("## Cmt0900.setTestEnd_Cncl() Exception START ##");				
			ecamsLogger.error("## Error DESC : ", exception);	
			ecamsLogger.error("## Cmt0900.v() Exception END ##");				
			throw exception;
		}finally{
			if (strQuery != null) 	strQuery = null;
			if (rs != null)     try{rs.close();}catch (Exception ex){ex.printStackTrace();}
			if (pstmt != null)  try{pstmt.close();}catch (Exception ex2){ex2.printStackTrace();}
			if (conn != null){
				try{
					ConnectionResource.release(conn);
				}catch(Exception ex3){
					ecamsLogger.error("## Cmt0900.setTestEnd_Cncl() connection release exception ##");
					ex3.printStackTrace();
				}
			}
		}
	} //setTestEnd_Cncl
}//end of Cmt0900 class statement