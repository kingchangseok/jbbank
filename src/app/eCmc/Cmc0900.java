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
import app.eCmr.Cmr0200;

/**
 * @author bigeyes
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */

 
public class Cmc0900{
    /**
     * Logger Class Instance Creation
     * logger
     */
    Logger ecamsLogger  = EcamsLogger.getLoggerInstance();
    
    
    
	/**
	 * <PRE>
	 * 1. MethodName	: request_reqend
	 * 2. ClassName		: Cmc0900
	 * 3. Commnet			: ISR종료 신청
	 * 4. 작성자				: no name
	 * 5. 작성일				: 2010. 12. 13. 오전 9:46:44
	 * </PRE>
	 * 		@return String
	 * 		@param etcData
	 * 		@param ConfList
	 * 		@return
	 * 		@throws SQLException
	 * 		@throws Exception
	 */
	public String request_reqend(HashMap<String,String> etcData,ArrayList<HashMap<String,Object>> ConfList) throws SQLException, Exception {
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
			
			//신청번호 받아오기
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
    		
        	conn.setAutoCommit(false);
    		Cmc0010 cmc0010 = new Cmc0010();
    		retMsg = cmc0010.base_Confirm(AcptNo,etcData.get("IsrId"),etcData.get("IsrSub"),etcData.get("userid"),etcData.get("reqcd"),conn);
    		if (!retMsg.equals("0")) {
				conn.rollback();
				conn.close();
				throw new Exception("ISR승인요청기본 정보 등록 중 오류가 발생하였습니다. 관리자에게 연락하여 주시기 바랍니다.");
    		}
    		
    		Cmr0200 cmr0200 = new Cmr0200();
			retMsg = cmr0200.request_Confirm(AcptNo,"99999",etcData.get("reqcd"),etcData.get("userid"),true,ConfList,conn);
			if (!retMsg.equals("OK")) {
				conn.rollback();
				conn.close();
				 return "결재정보등록 중 오류가 발생하였습니다. 관리자에게 연락하여 주시기 바랍니다.";
			}
			retMsg = "0";
			
        	//MAX 이용해서 SEQNO 값 구하기 시작
        	int SEQNOMAX = 0;
        	strQuery.setLength(0);                		
    		strQuery.append("select nvl(max(CC_SEQNO),0)+1 as max \n");
    		strQuery.append("  from cmc0190 \n"); 
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
	        //MAX 이용해서 SEQNO 값 구하기 끝
	        
	        //cmc0190 테이블에 신청내용 저장 시작
        	strQuery.setLength(0);
        	strQuery.append("insert into cmc0190 \n");
        	strQuery.append("    (CC_ISRID,CC_ISRSUB,CC_SEQNO,CC_ACPTNO, \n");
        	strQuery.append("     CC_EDDATE,CC_LASTDT,CC_REALMM,CC_REALEDDAY, \n");
        	strQuery.append("     CC_REQETC,CC_STATUS,CC_REALTIME) \n");
    		strQuery.append("values (?,?,?,?, SYSDATE,SYSDATE,?,?, ?,'1',?) \n");
    		
	        pstmt = conn.prepareStatement(strQuery.toString());
	        //pstmt = new LoggableStatement(conn,strQuery.toString());
			pstmtcount = 1;
			pstmt.setString(pstmtcount++, etcData.get("IsrId"));
        	pstmt.setString(pstmtcount++, etcData.get("IsrSub"));
        	pstmt.setInt(pstmtcount++, SEQNOMAX);
        	pstmt.setString(pstmtcount++, AcptNo);
        	pstmt.setString(pstmtcount++, etcData.get("txtREALMM"));
        	pstmt.setString(pstmtcount++, etcData.get("datREALEDDAY"));
        	pstmt.setString(pstmtcount++, etcData.get("txtEtc"));
        	pstmt.setString(pstmtcount++, etcData.get("txtREALTime"));

        	//ecamsLogger.error(((LoggableStatement)pstmt).getQueryString());
    		pstmt.executeUpdate();
    		pstmt.close();
    		//cmc0190 테이블에 신청내용 저장 끝
    		
			/*
			CC_ENDACPT	VARCHAR2(12)		ISR종료번호
			CC_REALMM	NUMBER		실투입공수
			CC_REALEDDAY	VARCHAR2(8)		실완료일
			CC_ENDGBN	VARCHAR2(1)		ISR완료구분
			 */
			//최신상태를 cmc0110 테이블에 업데이트 해준다.
        	strQuery.setLength(0);
    		strQuery.append("update cmc0110 set     \n");
    		strQuery.append("       CC_EDDATE=SYSDATE, \n");
    		strQuery.append("       CC_SUBSTATUS='18', \n");
    		strQuery.append("       CC_ENDACPT=?,   \n");
    		strQuery.append("       CC_REALMM=?,    \n");
    		strQuery.append("       CC_REALTIME=?,    \n");
    		strQuery.append("       CC_REALEDDAY=?, \n");
    		//'16','22','29','1A'
    		strQuery.append("       CC_ENDGBN=decode(cc_substatus,'16','3','22','3','29','9',CC_ENDGBN) \n");
    		strQuery.append(" where CC_ISRID=?      \n");
    		strQuery.append("   and CC_ISRSUB=?     \n");

	        pstmt = conn.prepareStatement(strQuery.toString());
	        //pstmt = new LoggableStatement(conn,strQuery.toString());
			pstmtcount = 1;
			pstmt.setString(pstmtcount++, AcptNo);
			pstmt.setString(pstmtcount++, etcData.get("txtREALMM"));
			pstmt.setString(pstmtcount++, etcData.get("txtREALTime"));
        	pstmt.setString(pstmtcount++, etcData.get("datREALEDDAY"));
        	//pstmt.setString(pstmtcount++, etcData.get("endgbn"));
        	pstmt.setString(pstmtcount++, etcData.get("IsrId"));
        	pstmt.setString(pstmtcount++, etcData.get("IsrSub"));
        	//ecamsLogger.error(((LoggableStatement)pstmt).getQueryString());
    		pstmt.executeUpdate();
    		pstmt.close();
    		conn.commit();
	        conn.close();
	        rs = null;
	        pstmt = null;
	        conn = null;
	        
	        if (retMsg.equals("0")) return "OK" + Integer.toString(SEQNOMAX);
	        else return retMsg;
	        
	        
		} catch (SQLException sqlexception) {
			sqlexception.printStackTrace();
			if (rs != null)     try{rs.close();}catch (Exception ex){ex.printStackTrace();}
			if (pstmt != null)  try{pstmt.close();}catch (Exception ex2){ex2.printStackTrace();}
			if (pstmt2 != null)  try{pstmt2.close();}catch (Exception ex2){ex2.printStackTrace();}
			if (conn != null){
				try{
					conn.rollback();
					ConnectionResource.release(conn);
					conn = null;
				}catch(Exception ex3){
					ecamsLogger.error("## Cmc0900.request_reqend() connection release exception ##");
					ex3.printStackTrace();
				}				
			}
			ecamsLogger.error("## Cmc0900.request_reqend() SQLException START ##");
			ecamsLogger.error("## Error DESC : ", sqlexception);	
			ecamsLogger.error("## Cmc0900.request_reqend() SQLException END ##");			
			throw sqlexception;
		} catch (Exception exception) {
			exception.printStackTrace();
			if (rs != null)     try{rs.close();}catch (Exception ex){ex.printStackTrace();}
			if (pstmt != null)  try{pstmt.close();}catch (Exception ex2){ex2.printStackTrace();}
			if (pstmt2 != null)  try{pstmt2.close();}catch (Exception ex2){ex2.printStackTrace();}
			if (conn != null){
				try{
					conn.rollback();
					ConnectionResource.release(conn);
					conn = null;
				}catch(Exception ex3){
					ecamsLogger.error("## Cmc0900.request_reqend() connection release exception ##");
					ex3.printStackTrace();
				}				
			}
			ecamsLogger.error("## Cmc0900.request_reqend() Exception START ##");				
			ecamsLogger.error("## Error DESC : ", exception);	
			ecamsLogger.error("## Cmc0900.request_reqend() Exception END ##");				
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
					ecamsLogger.error("## Cmc0900.request_reqend() connection release exception ##");
					ex3.printStackTrace();
				}
			}
		}
	}//end of request_reqend() method statement
	

}//end of Cmc0900 class statement
