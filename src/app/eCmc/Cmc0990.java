/*****************************************************************************************
	1. program ID	: Cmc0990.java
	2. create date	: 2010.11.15
	3. auth		    : no Name
	4. update date	: 
	5. auth		    : 
	6. description	: 요구관리 ISR 등록
*****************************************************************************************/

package app.eCmc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import app.common.LoggableStatement;
import org.apache.logging.log4j.Logger;
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
public class Cmc0990{

    /**
     * Logger Class Instance Creation
     * logger
     */
    Logger ecamsLogger  = EcamsLogger.getLoggerInstance();
    
	/**	
	 * 요구관리 ISRID 조회
	 * @param UserId
	 * @return List
	 * @throws SQLException
	 * @throws Exception
	 */
	public Object[] getISRList(HashMap<String,String> etcData) throws SQLException, Exception {
		Connection        conn        = null;
		PreparedStatement pstmt       = null;
		ResultSet         rs          = null;
		StringBuffer      strQuery    = new StringBuffer();
		Object[] returnObjectArray    = null;
		ArrayList<HashMap<String, String>>  rsval = new ArrayList<HashMap<String, String>>();
		HashMap<String, String> rst	  = null;
		int              parmCnt      = 0;
		ConnectionContext connectionContext = new ConnectionResource();		
		
		try {
			
			conn = connectionContext.getConnection();
			
			strQuery.setLength(0);//ISERID : R + YYYYMM + '-' + SEQ(4) : R201011-0001
			strQuery.append("select a.CC_ISRID,a.CC_ISRTITLE,               \n");
			strQuery.append("       TO_CHAR(a.CC_CREATDT,'yyyy/mm/dd hh24:mi') CC_CREATDT, \n");
			strQuery.append("       TO_CHAR(a.CC_EDCOMDT,'yyyy/mm/dd hh24:mi') ENDDT,   \n");
			strQuery.append("       a.CC_LASTDT,a.CC_REQENDDT,a.CC_REQGRADE, \n");
			strQuery.append("       DECODE(a.CC_TESTERYN,'Y','참여','불참') as TESTERYN, \n");
			strQuery.append("       a.CC_TESTERYN,a.CC_DOCNO,                \n");
			strQuery.append("       NVL(a.CC_BUDGETPRICE,0) as BUDGETPRICE,  \n");
			strQuery.append("       a.CC_DETAILSAYU,b.CM_USERNAME,           \n");
			strQuery.append("       c.CM_CODENAME REQGRADE,d.CM_DEPTCD,      \n");
			strQuery.append("       d.CM_DEPTNAME,                           \n");
			strQuery.append("       ISREND_CHK(a.CC_ISRID) ENDOK             \n");	
			strQuery.append("  from Cmc0100 a,cmm0040 b,cmm0020 c,cmm0100 d  \n");
			strQuery.append(" where a.cc_editor = ?                          \n");
			strQuery.append("   and a.cc_edcomdt is null                      \n");
			if (etcData.get("qrygbn").equals("00")) {
				strQuery.append("and to_char(a.cc_creatdt,'yyyymmdd')>=?     \n");
				strQuery.append("and to_char(a.cc_creatdt,'yyyymmdd')<=?     \n");
			} else if (etcData.get("qrygbn").equals("02")) {
				strQuery.append("and ISREND_CHK(a.CC_ISRID)='N1'             \n");
			} else {
				strQuery.append("and ISREND_CHK(a.CC_ISRID)='OK'             \n");
			}
			strQuery.append("   and a.cc_editor = b.cm_userid   \n");
			strQuery.append("   and c.cm_macode = 'REQGRADE'    \n");
			strQuery.append("   and a.cc_reqgrade = c.cm_micode \n");
			strQuery.append("   and b.cm_project = d.cm_deptcd  \n");
	        pstmt = conn.prepareStatement(strQuery.toString());
	        // pstmt = new LoggableStatement(conn,strQuery.toString());
	        pstmt.setString(++parmCnt, etcData.get("userid"));
	        if (etcData.get("qrygbn").equals("00")) {
	        	pstmt.setString(++parmCnt, etcData.get("stday"));
	        	pstmt.setString(++parmCnt, etcData.get("edday"));
	        }
	     //   ecamsLogger.error(((LoggableStatement)pstmt).getQueryString());
	        rs = pstmt.executeQuery();
	        
	        while(rs.next()) {
		  		rst = new HashMap<String, String>();
				rst.put("cc_isrid", rs.getString("CC_ISRID"));
				rst.put("CC_ISRTITLE", rs.getString("CC_ISRTITLE"));
				rst.put("CC_CREATDT", rs.getString("CC_CREATDT"));
				rst.put("CC_LASTDT", rs.getString("CC_LASTDT"));
				
				rst.put("CC_REQENDDT", "");
				if (rs.getString("CC_REQENDDT") != "" && rs.getString("CC_REQENDDT") != null){
					rst.put("CC_REQENDDT", rs.getString("CC_REQENDDT").substring(0,4)+"/"+rs.getString("CC_REQENDDT").substring(4,6)+"/"+rs.getString("CC_REQENDDT").substring(6));
				}
				rst.put("CC_REQGRADE", rs.getString("CC_REQGRADE"));
				rst.put("TESTERYN", rs.getString("TESTERYN"));
				rst.put("CC_TESTERYN", rs.getString("CC_TESTERYN"));
				rst.put("CC_DOCNO", rs.getString("CC_DOCNO"));
				rst.put("BUDGETPRICE", rs.getString("BUDGETPRICE"));
				rst.put("CC_DETAILSAYU", rs.getString("CC_DETAILSAYU"));
				rst.put("CM_USERNAME", rs.getString("CM_USERNAME"));
				rst.put("REQGRADE", rs.getString("REQGRADE"));
				rst.put("CM_DEPTNAME", rs.getString("CM_DEPTNAME"));
				rst.put("ENDDT", rs.getString("ENDDT"));

				// 종료가능한 ISR 인지 여부 체크
				rst.put("endok", rs.getString("ENDOK"));
				
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
			ecamsLogger.error("## Cmc0990.getISRList() SQLException START ##");
			ecamsLogger.error("## Error DESC : ", sqlexception);	
			ecamsLogger.error("## Cmc0990.getISRList() SQLException END ##");			
			throw sqlexception;
		} catch (Exception exception) {
			exception.printStackTrace();
			ecamsLogger.error("## Cmc0990.getISRList() Exception START ##");				
			ecamsLogger.error("## Error DESC : ", exception);	
			ecamsLogger.error("## Cmc0990.getISRList() Exception END ##");				
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
					ecamsLogger.error("## Cmc0990.getISRList() connection release exception ##");
					ex3.printStackTrace();
				}
			}
		}
	}//end of getISRList() method statement

	public Object[] getISRInfo_End(String IsrId) throws SQLException, Exception {
		Connection        conn        = null;
		PreparedStatement pstmt       = null;
		ResultSet         rs          = null;
		StringBuffer      strQuery    = new StringBuffer();
		Object[] returnObjectArray    = null;
		ArrayList<HashMap<String, String>>  rsval = new ArrayList<HashMap<String, String>>();
		HashMap<String, String> rst	  = null;
		
		ConnectionContext connectionContext = new ConnectionResource();		
		
		try {
			
			conn = connectionContext.getConnection();
			
			strQuery.setLength(0);//ISERID : R + YYYYMM + '-' + SEQ(4) : R201011-0001
			strQuery.append("select ISREND_CHK(a.CC_ISRID) ENDOK,a.cc_editor, \n");	
			strQuery.append("       to_char(a.cc_edcomdt,'yyyy/mm/dd hh24:mi') enddt, \n");		
			strQuery.append("       a.cc_edcomsayu,a.cc_satiscd,a.cc_edgbncd \n");	
			strQuery.append("  from cmc0100 a                   \n");
			strQuery.append(" where a.cc_isrid=?                \n");
	        pstmt = conn.prepareStatement(strQuery.toString());
	        // pstmt = new LoggableStatement(conn,strQuery.toString());
	        pstmt.setString(1, IsrId);
	   //     ecamsLogger.error(((LoggableStatement)pstmt).getQueryString());
	        rs = pstmt.executeQuery();
	        
	        while(rs.next()) {
		  		rst = new HashMap<String, String>();
				rst.put("enddt", rs.getString("enddt"));
				rst.put("cc_edcomsayu", rs.getString("cc_edcomsayu"));
				rst.put("cc_satiscd", rs.getString("cc_satiscd"));
				rst.put("cc_edgbncd", rs.getString("cc_edgbncd"));
				rst.put("cc_editor", rs.getString("cc_editor"));

				// 종료가능한 ISR 인지 여부 체크
				rst.put("endok", rs.getString("ENDOK"));
				
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
			ecamsLogger.error("## Cmc0990.getISRInfo_End() SQLException START ##");
			ecamsLogger.error("## Error DESC : ", sqlexception);	
			ecamsLogger.error("## Cmc0990.getISRInfo_End() SQLException END ##");			
			throw sqlexception;
		} catch (Exception exception) {
			exception.printStackTrace();
			ecamsLogger.error("## Cmc0990.getISRInfo_End() Exception START ##");				
			ecamsLogger.error("## Error DESC : ", exception);	
			ecamsLogger.error("## Cmc0990.getISRInfo_End() Exception END ##");				
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
					ecamsLogger.error("## Cmc0990.getISRInfo_End() connection release exception ##");
					ex3.printStackTrace();
				}
			}
		}
	}//end of getISRInfo_End() method statement
	
	
	/**
	 * <PRE>
	 * 1. MethodName	: setISRInfo
	 * 2. ClassName		: Cmc0990
	 * 3. Commnet			: 요구 요청자 종료 셋
	 * 4. 작성자				: no name
	 * 5. 작성일				: 2010. 12. 14. 오후 8:58:30
	 * </PRE>
	 * 		@return String
	 * 		@param etcData
	 * 		@return
	 * 		@throws SQLException
	 * 		@throws Exception
	 */
	public String setISRInfo(HashMap<String,String> etcData) throws SQLException, Exception {
		Connection        conn        = null;
		PreparedStatement pstmt       = null;
		StringBuffer      strQuery    = new StringBuffer();
		
		ConnectionContext connectionContext = new ConnectionResource();		
		
		try {
			
			conn = connectionContext.getConnection();
			conn.setAutoCommit(false);

			strQuery.setLength(0);
    		strQuery.append("update cmc0100 set cc_status='9', \n");
    		strQuery.append("       cc_edcomdt=SYSDATE,        \n");
    		strQuery.append("       cc_edcomsayu=?,            \n");
    		strQuery.append("       cc_satiscd=?,              \n");
    		strQuery.append("       cc_edgbncd='9'             \n");
    		strQuery.append(" where cc_isrid=?                 \n");
			pstmt = conn.prepareStatement(strQuery.toString());
			// pstmt = new LoggableStatement(conn,strQuery.toString());
        	pstmt.setString(1, etcData.get("Confirm"));
        	pstmt.setString(2, etcData.get("grpRdo"));
        	pstmt.setString(3, etcData.get("isrid"));
        //	ecamsLogger.error(((LoggableStatement)pstmt).getQueryString());
        	pstmt.executeUpdate();
        	pstmt.close();
        	
        	strQuery.setLength(0);
    		strQuery.append("update cmc0110 set cc_substatus='1B', \n");
    		strQuery.append("       cc_mainstatus='01'             \n");
    		strQuery.append(" where cc_isrid=?                     \n");
    		strQuery.append("   and cc_substatus<>'13'             \n");
			pstmt = conn.prepareStatement(strQuery.toString());
			// pstmt = new LoggableStatement(conn,strQuery.toString());
        	pstmt.setString(1, etcData.get("isrid"));
        //	ecamsLogger.error(((LoggableStatement)pstmt).getQueryString());
        	pstmt.executeUpdate();
        	pstmt.close();
			conn.commit();
			
			
	        conn.close();
	        pstmt.close();
	        
	        pstmt = null;
	        conn = null;
	        
	        
	        return "0";
	        
	        
		} catch (SQLException sqlexception) {
			sqlexception.printStackTrace();
			if (pstmt != null)  try{pstmt.close();}catch (Exception ex2){ex2.printStackTrace();}
			if (conn != null){
				try{
					conn.rollback();
					ConnectionResource.release(conn);
				}catch(Exception ex3){
					ecamsLogger.error("## Cmc0990.setISRInfo() connection release exception ##");
					ex3.printStackTrace();
				}
			}
			ecamsLogger.error("## Cmc0990.setISRInfo() SQLException START ##");
			ecamsLogger.error("## Error DESC : ", sqlexception);	
			ecamsLogger.error("## Cmc0990.setISRInfo() SQLException END ##");			
			throw sqlexception;
		} catch (Exception exception) {
			exception.printStackTrace();
			if (pstmt != null)  try{pstmt.close();}catch (Exception ex2){ex2.printStackTrace();}
			if (conn != null){
				try{
					conn.rollback();
					ConnectionResource.release(conn);
				}catch(Exception ex3){
					ecamsLogger.error("## Cmc0990.setISRInfo() connection release exception ##");
					ex3.printStackTrace();
				}				
			}			
			ecamsLogger.error("## Cmc0990.setISRInfo() Exception START ##");				
			ecamsLogger.error("## Error DESC : ", exception);	
			ecamsLogger.error("## Cmc0990.setISRInfo() Exception END ##");				
			throw exception;
		}finally{
			if (strQuery != null) 	strQuery = null;
			if (pstmt != null)  try{pstmt.close();}catch (Exception ex2){ex2.printStackTrace();}
			if (conn != null){
				try{
					ConnectionResource.release(conn);
					conn = null;
				}catch(Exception ex3){
					ecamsLogger.error("## Cmc0990.setISRInfo() connection release exception ##");
					ex3.printStackTrace();
				}
			}
		}
	}//end of setISRInfo() method statement

}//end of Cmc0990 class statement

