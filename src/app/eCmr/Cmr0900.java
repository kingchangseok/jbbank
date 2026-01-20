/*****************************************************************************************
	1. program ID	: Cmr0900.java
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
import app.common.LoggableStatement;
import org.apache.logging.log4j.Logger;
//import org.apache.poi.hssf.record.formula.functions.Sin;


import com.ecams.common.dbconn.ConnectionContext;
import com.ecams.common.dbconn.ConnectionResource;
import com.ecams.common.logger.EcamsLogger;
import java.util.ArrayList;
import java.util.HashMap;
import app.common.*;
import app.eCmc.Cmc0010;

import app.thread.*;

/**
 * @author bigeyes
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class Cmr0900{    
	
 
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

    
	public String request_chgend(HashMap<String,String> etcData,ArrayList<HashMap<String,Object>> ConfList) throws SQLException, Exception {
		Connection        conn        = null;
		PreparedStatement pstmt       = null;
		PreparedStatement pstmt2      = null;
		ResultSet         rs          = null;
		StringBuffer      strQuery    = new StringBuffer();
		String            retMsg      = "0";
		int				  pstmtcount  = 1;
		int               i = 0;
		String            AcptNo  = "";
		ConnectionContext connectionContext = new ConnectionResource();		
		
		try {
			conn = connectionContext.getConnection();
			//RFC 발행 신청번호 받아오기
			AutoSeq autoseq = new AutoSeq();
        	do {
		        AcptNo = autoseq.getSeqNo(conn,etcData.get("reqcd"));

		        i = 0;
		        strQuery.setLength(0);		        
		        strQuery.append("select count(*) as cnt from cmr9900 \n");
	        	strQuery.append(" where cr_acptno= ?                 \n");		        
	        	
	        	pstmt = conn.prepareStatement(strQuery.toString());
	        	pstmt.setString(1, AcptNo);
	        	
	        	rs = pstmt.executeQuery();
	        	
	        	if (rs.next()){
	        		i = rs.getInt("cnt");
	        	}	        	
	        	rs.close();
	        	pstmt.close();
	        } while(i>0);
        	
        	int SEQNOMAX = 0;
        	strQuery.setLength(0);                		
        	strQuery.append("select nvl(max(CC_SEQNO),0)+1 as max \n");
    		strQuery.append("  from cmc0290 \n");
    		strQuery.append(" where CC_ISRID=? \n");
    		strQuery.append("   and CC_ISRSUB=? \n");
    		pstmt = conn.prepareStatement(strQuery.toString());
			pstmtcount = 1;
			pstmt.setString(pstmtcount++, etcData.get("IsrId"));
        	pstmt.setString(pstmtcount++, etcData.get("IsrSub"));
	        rs = pstmt.executeQuery();
	        if (rs.next() && rs.getString("max") != null) {
	        	SEQNOMAX = rs.getInt("max");
	        }
	        rs.close();
	        pstmt.close();
	        
	        conn.setAutoCommit(false);
        	//CC_ISRID,CC_ISRSUB,CC_SCMUSER,CC_LASTDT,CC_ACPTNO,CC_JOBSTDAY,CC_JOBEDDAY,CC_JOBETC
        	strQuery.setLength(0);
        	strQuery.append("insert into cmc0290 ");
        	strQuery.append("    (CC_ISRID,CC_ISRSUB,CC_SCMUSER,CC_LASTDT,     \n");
        	strQuery.append("     CC_ACPTNO,CC_JOBSTDAY,CC_JOBEDDAY,           \n");
        	strQuery.append("     CC_JOBETC,CC_ENDGBN,CC_SEQNO,CC_STATUS)      \n");
    		strQuery.append("values (?,?,?,SYSDATE,?,?,?,?,?,?,'1')            \n");
    		
	        pstmt = conn.prepareStatement(strQuery.toString());
	//        pstmt = new LoggableStatement(conn,strQuery.toString());
			pstmtcount = 1;
			pstmt.setString(pstmtcount++, etcData.get("IsrId"));
        	pstmt.setString(pstmtcount++, etcData.get("IsrSub"));
        	pstmt.setString(pstmtcount++, etcData.get("userid"));
        	pstmt.setString(pstmtcount++, AcptNo);        	
        	pstmt.setString(pstmtcount++, etcData.get("stday"));
        	pstmt.setString(pstmtcount++, etcData.get("edday"));        	
        	pstmt.setString(pstmtcount++, etcData.get("jobetc"));       	
        	pstmt.setString(pstmtcount++, etcData.get("endgbncd"));        	
        	pstmt.setInt(pstmtcount++, SEQNOMAX);
   //     	//ecamsLogger.error(((LoggableStatement)pstmt).getQueryString());
    		pstmt.executeUpdate();
    		pstmt.close();
    		
    		Cmc0010 cmc0010 = new Cmc0010();
    		retMsg = cmc0010.base_Confirm(AcptNo,etcData.get("IsrId"),etcData.get("IsrSub"),etcData.get("userid"),etcData.get("reqcd"),conn);
    		if (!retMsg.equals("0")) {
				conn.rollback();
				conn.close();
				return "ERISR승인요청기본 정보 등록 중 오류가 발생하였습니다. 관리자에게 연락하여 주시기 바랍니다.";
    		}
    		
    		Cmr0200 cmr0200 = new Cmr0200();
			retMsg = cmr0200.request_Confirm(AcptNo,"99999",etcData.get("reqcd"),etcData.get("userid"),true,ConfList,conn);
			if (!retMsg.equals("OK")) {
				conn.rollback();
				conn.close();
				 return "ER결재정보등록 중 오류가 발생하였습니다. 관리자에게 연락하여 주시기 바랍니다.";
			}
			retMsg = "0";
			/*
				CC_RFCACPT	RFC발행번호
				CC_RFCUSER	RFC발행인
				CC_RFCDATE	RFC등록일시
				CC_RFCEDDATE	RFC발행완료일시
				CC_EXPRUNTIME	예상소요시간
				CC_EXPMM	예상투입공수
				CC_EXPDAY	완료예정일
				CC_EXPRSC	예상소요자원
			 */
			//최신상태를 cmc0210 테이블에 업데이트 해준다.
        	strQuery.setLength(0);
    		strQuery.append("update cmc0210 set        \n");
    		strQuery.append("       CC_STATUS='1',     \n");
    		strQuery.append("       CC_JOBSTDAY=?,     \n");
    		strQuery.append("       CC_JOBEDDAY=?,     \n");
    		strQuery.append("       CC_EDREQDT=SYSDATE,\n");
    		strQuery.append("       CC_EDGBNCD=?,      \n");
    		strQuery.append("       CC_JOBETC=?,       \n");
    		strQuery.append("       CC_CHGEDACPT=?     \n");
    		strQuery.append(" where CC_ISRID=?         \n");
    		strQuery.append("   and CC_ISRSUB=?        \n");
    		strQuery.append("   and CC_SCMUSER=?       \n");
    		    		
	        pstmt = conn.prepareStatement(strQuery.toString());
	//        pstmt = new LoggableStatement(conn,strQuery.toString());
			pstmtcount = 1;
			pstmt.setString(pstmtcount++, etcData.get("stday"));
        	pstmt.setString(pstmtcount++, etcData.get("edday"));
        	pstmt.setString(pstmtcount++, etcData.get("edgbncd"));
        	pstmt.setString(pstmtcount++, etcData.get("jobetc"));        	
        	pstmt.setString(pstmtcount++, AcptNo);
        	
        	pstmt.setString(pstmtcount++, etcData.get("IsrId"));
        	pstmt.setString(pstmtcount++, etcData.get("IsrSub"));
        	pstmt.setString(pstmtcount++, etcData.get("userid"));
    //    	//ecamsLogger.error(((LoggableStatement)pstmt).getQueryString());
    		pstmt.executeUpdate();
    		pstmt.close();
	        
			//최신상태를 cmc0110 테이블에 업데이트 해준다.
        	strQuery.setLength(0);
    		strQuery.append("update cmc0110 set        \n");
    		strQuery.append("       CC_SUBSTATUS='28', \n");
    		strQuery.append("       CC_MAINSTATUS='02' \n");
    		strQuery.append(" where CC_ISRID=?         \n");
    		strQuery.append("   and CC_ISRSUB=?        \n");
    		    		
	        pstmt2 = conn.prepareStatement(strQuery.toString());
	  //      pstmt2 = new LoggableStatement(conn,strQuery.toString());
			pstmtcount = 1;
        	pstmt2.setString(pstmtcount++, etcData.get("IsrId"));
        	pstmt2.setString(pstmtcount++, etcData.get("IsrSub"));
    //    	//ecamsLogger.error(((LoggableStatement)pstmt2).getQueryString());
    		pstmt2.executeUpdate();
    		pstmt2.close();
	    		
    		conn.commit();
    		conn.setAutoCommit(true);
	        conn.close();
			conn = null;
			pstmt = null;
			rs = null;
			pstmt2 = null;

	        
	        if (retMsg.equals("0")) return "OK" + Integer.toString(SEQNOMAX);
	        else return "ER"+retMsg;
	        
	        
		} catch (SQLException sqlexception) {
			sqlexception.printStackTrace();
			ecamsLogger.error("## Cmr0900.request_chgend() SQLException START ##");
			ecamsLogger.error("## Error DESC : ", sqlexception);	
			ecamsLogger.error("## Cmr0900.request_chgend() SQLException END ##");			
			throw sqlexception;
		} catch (Exception exception) {
			exception.printStackTrace();
			ecamsLogger.error("## Cmr0900.request_chgend() Exception START ##");				
			ecamsLogger.error("## Error DESC : ", exception);	
			ecamsLogger.error("## Cmr0900.request_chgend() Exception END ##");				
			throw exception;
		}finally{
			if (strQuery != null) 	strQuery = null;
			if (rs != null)     try{rs.close();}catch (Exception ex){ex.printStackTrace();}
			if (pstmt != null)  try{pstmt.close();}catch (Exception ex2){ex2.printStackTrace();}
			if (pstmt2 != null)  try{pstmt2.close();}catch (Exception ex2){ex2.printStackTrace();}
			if (conn != null){
				try{
					conn.close();
					conn = null;
				}catch(Exception ex3){
					ecamsLogger.error("## Cmr0900.request_chgend() connection release exception ##");
					ex3.printStackTrace();
				}
			}
		}
	}//end of request_chgend() method statement
	
}//end of Cmr0900 class statement