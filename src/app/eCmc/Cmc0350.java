/*****************************************************************************************
	1. program ID	: Cmc0350.java
	2. create date	: 2010.11.20
	3. auth		    : no name
	4. update date	: 2010.11.22
	5. auth		    : no name
	6. description	: RFC 결재화면
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


public class Cmc0350{
    /**
     * Logger Class Instance Creation
     * logger
     */
    Logger ecamsLogger  = EcamsLogger.getLoggerInstance();
    
	/**	
	 * 의뢰건에 대한 RFC 접수건 기본정보(요청사유) 조회
	 */
    public Object[] getRFCInfo(HashMap<String,String> etcData) throws SQLException, Exception {
		Connection        conn        = null;
		PreparedStatement pstmt       = null;
		ResultSet         rs          = null;
		StringBuffer      strQuery    = new StringBuffer();
		
		Object[] 		  returnObject= null;
		ArrayList<HashMap<String, String>>  rtList	= new ArrayList<HashMap<String, String>>();
		HashMap<String, String>			  	rst	 	= null;	
		
		ConnectionContext connectionContext = new ConnectionResource();
		
		try {
			
			rtList.clear();
	        
			conn = connectionContext.getConnection();
			
			strQuery.setLength(0);		
			strQuery.append("select a.CC_EXPRUNTIME,a.CC_EXPMM,a.CC_EXPDAY,a.CC_EXPRSC,b.CM_USERNAME HANDLER, \n");
			strQuery.append("       DECODE(a.CC_RFCDATE,null,TO_CHAR(SYSDATE,'yyyy/mm/dd hh24:mi'),TO_CHAR(a.CC_RFCDATE,'yyyy/mm/dd hh24:mi')) CC_RFCDATE \n");
			strQuery.append("  from cmc0110 a, cmm0040 b \n");
			strQuery.append(" where a.CC_ISRID = ? \n");
			strQuery.append("   and a.CC_ISRSUB = ? \n");
			strQuery.append("   and a.CC_HANDLER = ? \n");
			strQuery.append("   and a.CC_HANDLER = b.CM_USERID \n");
			
			pstmt = conn.prepareStatement(strQuery.toString());
			// pstmt = new LoggableStatement(conn,strQuery.toString());
            pstmt.setString(1, etcData.get("CC_ISRID"));
            pstmt.setString(2, etcData.get("CC_ISRSUB"));
            pstmt.setString(3, etcData.get("CC_HANDLER"));
		//	ecamsLogger.error(((LoggableStatement)pstmt).getQueryString());
            rs = pstmt.executeQuery();
            
			while (rs.next()){	
	            rst = new HashMap<String, String>();
        		rst.put("CC_EXPRUNTIME", "");
        		if (rs.getString("CC_EXPRUNTIME") != null && rs.getString("CC_EXPRUNTIME") != ""){
        			rst.put("CC_EXPRUNTIME",   rs.getString("CC_EXPRUNTIME"));
        		}
        		if (rs.getBigDecimal("CC_EXPMM") != null){
        			rst.put("CC_EXPMM", rs.getBigDecimal("CC_EXPMM").toString());
        		} else{
        			rst.put("CC_EXPMM", "");
        		}
        		rst.put("CC_EXPDAY", "");
        		if (rs.getString("CC_EXPDAY") != null && rs.getString("CC_EXPDAY") != ""){
        			rst.put("CC_EXPDAY",   rs.getString("CC_EXPDAY"));
        		}
        		rst.put("CC_EXPRSC", "");
        		if (rs.getString("CC_EXPRSC") != null && rs.getString("CC_EXPRSC") != ""){
        			rst.put("CC_EXPRSC",   rs.getString("CC_EXPRSC"));
        		}
        		rst.put("CC_RFCDATE", "");
        		if (rs.getString("CC_RFCDATE") != null && rs.getString("CC_RFCDATE") != ""){
        			rst.put("CC_RFCDATE",   rs.getString("CC_RFCDATE"));
        		}
        		rst.put("HANDLER", rs.getString("HANDLER"));
        		rtList.add(rst);
        		rst = null;
			}
			
			rs.close();
			pstmt.close();
			conn.close();

			rs = null;
			pstmt = null;
			conn = null;
			
			returnObject = rtList.toArray();
			rtList.clear();
			rtList = null;
			
			return returnObject;			
			
		} catch (SQLException sqlexception) {
			sqlexception.printStackTrace();
			ecamsLogger.error("## Cmc0350.getRFCInfo() SQLException START ##");
			ecamsLogger.error("## Error DESC : ", sqlexception);	
			ecamsLogger.error("## Cmc0350.getRFCInfo() SQLException END ##");			
			throw sqlexception;
		} catch (Exception exception) {
			exception.printStackTrace();
			ecamsLogger.error("## Cmc0350.getRFCInfo() Exception START ##");				
			ecamsLogger.error("## Error DESC : ", exception);	
			ecamsLogger.error("## Cmc0350.getRFCInfo() Exception END ##");				
			throw exception;
		}finally{
			if (strQuery != null) 	strQuery = null;
			if (returnObject != null)	returnObject = null;
			if (rs != null)     try{rs.close();}catch (Exception ex){ex.printStackTrace();}
			if (pstmt != null)  try{pstmt.close();}catch (Exception ex2){ex2.printStackTrace();}
			if (conn != null){
				try{
					conn.close();
				}catch(Exception ex3){
					ecamsLogger.error("## Cmc0350.getRFCInfo() connection release exception ##");
					ex3.printStackTrace();
				}
			}
		}
	}//end of getRFCInfo() method statement

	public String setRFCInfoSave(HashMap<String,String> etcData) throws SQLException, Exception {
		Connection        conn        = null;
		PreparedStatement pstmt       = null;
		StringBuffer      strQuery    = new StringBuffer();
		String            retMsg      = "0";
		
		ConnectionContext connectionContext = new ConnectionResource();		
		
		try {
			
			conn = connectionContext.getConnection();
			conn.setAutoCommit(false);
			
			String strISRID = etcData.get("CC_ISRID");
			if (strISRID == "" && strISRID == null){
				return "ISRID 정보가 없습니다. 관리자에게 문의해주십시오.";
			}
			String strMAXSEQNO = "00";
			int pstmtcount = 1;
			
			//최신상태를 cmc0110 테이블에 업데이트 해준다.
        	strQuery.setLength(0);
    		strQuery.append("update cmc0110 set \n");
    		strQuery.append("       CC_RFCUSER=?, \n");
    		strQuery.append("       CC_RFCDATE=SYSDATE, \n");
    		strQuery.append("       CC_EXPRUNTIME=?, \n");
    		strQuery.append("       CC_EXPMM=?, \n");
    		strQuery.append("       CC_EXPDAY=?, \n");
    		strQuery.append("       CC_EXPRSC=? \n");
    		strQuery.append(" where CC_ISRID=? \n");
    		strQuery.append("   and CC_ISRSUB=? \n");

	        pstmt = conn.prepareStatement(strQuery.toString());
	        // pstmt = new LoggableStatement(conn,strQuery.toString());
			pstmtcount = 1;
			pstmt.setString(pstmtcount++, etcData.get("strUserId"));
        	pstmt.setString(pstmtcount++, etcData.get("CC_EXPRUNTIME"));
        	pstmt.setString(pstmtcount++, etcData.get("CC_EXPMM"));
        	pstmt.setString(pstmtcount++, etcData.get("CC_EXPDAY"));
        	pstmt.setString(pstmtcount++, etcData.get("CC_EXPRSC"));
        	pstmt.setString(pstmtcount++, strISRID);
        	pstmt.setString(pstmtcount++, etcData.get("CC_ISRSUB"));
        //	ecamsLogger.error(((LoggableStatement)pstmt).getQueryString());
    		pstmt.executeUpdate();
    		pstmt.close();

/*
    		//RFC 기본정보 입력 후 저장 히스토리 CMC0120 에 등록
        	strQuery.setLength(0);
    		strQuery.append("select lpad(max(CC_SEQNO)+1,2,'0') as MAXSEQNO \n");
    		strQuery.append("  from cmc0120 \n");
    		strQuery.append(" where CC_ISRID=? and CC_ISRSUB=? \n");
    		pstmt = conn.prepareStatement(strQuery.toString());
    		pstmt = new LoggableStatement(conn,strQuery.toString());
    		pstmtcount = 1;
    		pstmt.setString(pstmtcount++, strISRID);
    		pstmt.setString(pstmtcount++, etcData.get("CC_ISRSUB"));
    		ecamsLogger.error(((LoggableStatement)pstmt).getQueryString());
	        rs = pstmt.executeQuery();
	        strMAXSEQNO = "00";
	        if (rs.next()) {
	        	if (rs.getString("MAXSEQNO") != null && rs.getString("subMaxNO") != "") {
	        		strMAXSEQNO = rs.getString("MAXSEQNO");
	        	}
	        }
	        rs.close();
	        pstmt.close();
	        
	        if (!strMAXSEQNO.equals("00")){
	        //최초 등록이 아닐때 00번째 값을 MAX+1 값에 insert 하기
	        	strQuery.setLength(0);
	        	strQuery.append("insert into cmc0120 ");
	        	strQuery.append("       (CC_ISRID,CC_ISRSUB,CC_SEQNO,CC_ACPTNO,CC_RFCUSER, \n");
	        	strQuery.append("        CC_RFCDATE,CC_STATUS,CC_EXPRUNTIME,CC_EXPMM, \n");
	        	strQuery.append("        CC_EXPDAY,CC_EXPRSC) ");
	    		strQuery.append(" (select CC_ISRID,CC_ISRSUB,?,CC_ACPTNO,CC_RFCUSER, \n");
	    		strQuery.append("         CC_RFCDATE,CC_STATUS,CC_EXPRUNTIME,CC_EXPMM, \n");
	    		strQuery.append("         CC_EXPDAY,CC_EXPRSC  \n");
	    		strQuery.append("    from cmc0120 \n");
	    		strQuery.append("   where CC_ISRID=? \n");
	    		strQuery.append("     and CC_ISRSUB=? \n");
	    		strQuery.append("     and CC_SEQNO='00') \n");

		        pstmt = conn.prepareStatement(strQuery.toString());
		        pstmt = new LoggableStatement(conn,strQuery.toString());
				pstmtcount = 1;
				pstmt.setString(pstmtcount++, strMAXSEQNO);
				pstmt.setString(pstmtcount++, etcData.get("CC_ISRID"));
	        	pstmt.setString(pstmtcount++, etcData.get("CC_ISRSUB"));
	        	ecamsLogger.error(((LoggableStatement)pstmt).getQueryString());
	    		pstmt.executeUpdate();
	    		pstmt.close();
	    		
	        }
        	strQuery.setLength(0);
        	strQuery.append("insert into cmc0120 ");
        	strQuery.append("       (CC_ISRID,CC_ISRSUB,CC_SEQNO,CC_RFCUSER, \n");
        	strQuery.append("        CC_RFCDATE,CC_STATUS,CC_EXPRUNTIME,CC_EXPMM, \n");
        	strQuery.append("        CC_EXPDAY,CC_EXPRSC) ");
    		strQuery.append("values (?,?,'00',?, SYSDATE,'0',?,?, ?,?) \n");
    		
	        pstmt = conn.prepareStatement(strQuery.toString());
	        pstmt = new LoggableStatement(conn,strQuery.toString());
			pstmtcount = 1;
			pstmt.setString(pstmtcount++, etcData.get("CC_ISRID"));
        	pstmt.setString(pstmtcount++, etcData.get("CC_ISRSUB"));
        	pstmt.setString(pstmtcount++, etcData.get("strUserId"));
        	
        	pstmt.setString(pstmtcount++, etcData.get("CC_EXPRUNTIME"));
        	pstmt.setString(pstmtcount++, etcData.get("CC_EXPMM"));
        	
        	pstmt.setString(pstmtcount++, etcData.get("CC_EXPDAY"));
        	pstmt.setString(pstmtcount++, etcData.get("CC_EXPRSC"));
        	
        	ecamsLogger.error(((LoggableStatement)pstmt).getQueryString());
    		pstmt.executeUpdate();
    		pstmt.close();
*/
    		
	        conn.commit();
	        conn.close();
	        
	        pstmt = null;
	        conn = null;
	        
	        return retMsg;
	        
	        
		} catch (SQLException sqlexception) {
			sqlexception.printStackTrace();
			if (pstmt != null)  try{pstmt.close();}catch (Exception ex2){ex2.printStackTrace();}
			if (conn != null){
				try{
					conn.rollback();
					ConnectionResource.release(conn);
				}catch(Exception ex3){
					ecamsLogger.error("## Cmc0350.setReqInfoSave() connection release exception ##");
					ex3.printStackTrace();
				}				
			}			
			ecamsLogger.error("## Cmc0350.setRFCInfoSave() SQLException START ##");
			ecamsLogger.error("## Error DESC : ", sqlexception);	
			ecamsLogger.error("## Cmc0350.setRFCInfoSave() SQLException END ##");			
			throw sqlexception;
		} catch (Exception exception) {
			exception.printStackTrace();
			if (pstmt != null)  try{pstmt.close();}catch (Exception ex2){ex2.printStackTrace();}
			if (conn != null){
				try{
					conn.rollback();
					ConnectionResource.release(conn);
				}catch(Exception ex3){
					ecamsLogger.error("## Cmc0350.setRFCInfoSave() connection release exception ##");
					ex3.printStackTrace();
				}				
			}			
			ecamsLogger.error("## Cmc0350.setRFCInfoSave() Exception START ##");				
			ecamsLogger.error("## Error DESC : ", exception);	
			ecamsLogger.error("## Cmc0350.setRFCInfoSave() Exception END ##");				
			throw exception;
		}finally{
			if (strQuery != null) 	strQuery = null;
			if (pstmt != null)  try{pstmt.close();}catch (Exception ex2){ex2.printStackTrace();}
			if (conn != null){
				try{
					ConnectionResource.release(conn);
					conn = null;
				}catch(Exception ex3){
					ecamsLogger.error("## Cmc0350.setRFCInfoSave() connection release exception ##");
					ex3.printStackTrace();
				}
			}
		}
	}//end of setRFCInfoSave() method statement   

	public String setRFCConfirm(HashMap<String,String> etcData,ArrayList<HashMap<String,Object>> ConfList) throws SQLException, Exception {
		Connection        conn        = null;
		PreparedStatement pstmt       = null;
		ResultSet         rs          = null;
		StringBuffer      strQuery    = new StringBuffer();
		String            retMsg      = "0";
		AutoSeq			  autoseq	  = new AutoSeq();
		
		ConnectionContext connectionContext = new ConnectionResource();		
		
		try {
			
			conn = connectionContext.getConnection();
			
			String AcptNo = "";
			int i = 0;
			int pstmtcount = 1;
			String strMAXSEQNO = "00";
			String strISRID = etcData.get("CC_ISRID");
			if (strISRID == "" && strISRID == null){
				return "ISRID 정보가 없습니다. 관리자에게 문의해주십시오.";
			}
			
			//RFC 발행 신청번호 받아오기
        	do {
		        AcptNo = autoseq.getSeqNo(conn,etcData.get("strSubReqCD"));

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
        	
			/* cmc0120
			ISR ID,ISR SUBNO,요청 SEQNO,RFC발행번호,RFC발행인,
			RFC발행일시,상태 0:저장 1:승인요청 3:반려 9:완료,예상소요시간,예상투입공수,
			완료예정일,예상소요자원 
			 */
			conn.setAutoCommit(false);
        	
			
    		//RFC 기본정보 입력 후 저장 히스토리 CMC0120 에 등록
        	strQuery.setLength(0);
    		strQuery.append("select lpad(nvl(max(CC_SEQNO),0)+1,2,'0') as MAXSEQNO \n");
    		strQuery.append("  from cmc0120                    \n");
    		strQuery.append(" where CC_ISRID=? and CC_ISRSUB=? \n");
    		pstmt = conn.prepareStatement(strQuery.toString());
    		// pstmt = new LoggableStatement(conn,strQuery.toString());
    		pstmtcount = 1;
    		pstmt.setString(pstmtcount++, strISRID);
    		pstmt.setString(pstmtcount++, etcData.get("CC_ISRSUB"));
    	//	ecamsLogger.error(((LoggableStatement)pstmt).getQueryString());
	        rs = pstmt.executeQuery();
	        strMAXSEQNO = "00";
	        if (rs.next()) {
	        	strMAXSEQNO = rs.getString("MAXSEQNO");
	        }
	        rs.close();
	        pstmt.close();
	        
        	strQuery.setLength(0);
        	strQuery.append("insert into cmc0120 ");
        	strQuery.append("       (CC_ISRID,CC_ISRSUB,CC_SEQNO,CC_ACPTNO,CC_RFCUSER, \n");
        	strQuery.append("        CC_RFCDATE,CC_STATUS,CC_EXPRUNTIME,CC_EXPMM, \n");
        	strQuery.append("        CC_EXPDAY,CC_EXPRSC) ");
    		strQuery.append("values (?,?,?,?,?, SYSDATE,'1',?,?, ?,?) \n");
    		
	        pstmt = conn.prepareStatement(strQuery.toString());
	        // pstmt = new LoggableStatement(conn,strQuery.toString());
			pstmtcount = 1;
			pstmt.setString(pstmtcount++, etcData.get("CC_ISRID"));
        	pstmt.setString(pstmtcount++, etcData.get("CC_ISRSUB"));
        	pstmt.setString(pstmtcount++, strMAXSEQNO);
        	pstmt.setString(pstmtcount++, AcptNo);
        	pstmt.setString(pstmtcount++, etcData.get("strUserId"));
        	
        	pstmt.setString(pstmtcount++, etcData.get("CC_EXPRUNTIME"));
        	pstmt.setString(pstmtcount++, etcData.get("CC_EXPMM"));
        	
        	pstmt.setString(pstmtcount++, etcData.get("CC_EXPDAY"));
        	pstmt.setString(pstmtcount++, etcData.get("CC_EXPRSC"));
        	
        //	ecamsLogger.error(((LoggableStatement)pstmt).getQueryString());
    		pstmt.executeUpdate();
    		pstmt.close();
    		
    		Cmr0200 cmr0200 = new Cmr0200();
			retMsg = cmr0200.request_Confirm(AcptNo,"99999",etcData.get("strSubReqCD"),etcData.get("strUserId"),true,ConfList,conn);
			if (!retMsg.equals("OK")) {
				conn.rollback();
				conn.close();
				throw new Exception("결재정보등록 중 오류가 발생하였습니다. 관리자에게 연락하여 주시기 바랍니다.");
			}

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
			//최신상태를 cmc0110 테이블에 업데이트 해준다.
        	strQuery.setLength(0);
    		strQuery.append("update cmc0110 set        \n");
    		strQuery.append("       CC_SUBSTATUS='14', \n");
    		strQuery.append("       CC_RFCACPT=?,      \n");
    		strQuery.append("       CC_RFCUSER=?,      \n");
    		strQuery.append("       CC_RFCDATE=SYSDATE,\n");
    		strQuery.append("       CC_EXPRUNTIME=?,   \n");
    		strQuery.append("       CC_EXPMM=?,        \n");
    		strQuery.append("       CC_EXPDAY=?,       \n");
    		strQuery.append("       CC_EXPRSC=?        \n");
    		strQuery.append(" where CC_ISRID=?         \n");
    		strQuery.append("   and CC_ISRSUB=?        \n");
    		    		
	        pstmt = conn.prepareStatement(strQuery.toString());
	        // pstmt = new LoggableStatement(conn,strQuery.toString());
			pstmtcount = 1;
			pstmt.setString(pstmtcount++, AcptNo);
			pstmt.setString(pstmtcount++, etcData.get("strUserId"));
        	pstmt.setString(pstmtcount++, etcData.get("CC_EXPRUNTIME"));
        	pstmt.setString(pstmtcount++, etcData.get("CC_EXPMM"));
        	pstmt.setString(pstmtcount++, etcData.get("CC_EXPDAY"));
        	pstmt.setString(pstmtcount++, etcData.get("CC_EXPRSC"));
        	pstmt.setString(pstmtcount++, etcData.get("CC_ISRID"));
        	pstmt.setString(pstmtcount++, etcData.get("CC_ISRSUB"));
        //	ecamsLogger.error(((LoggableStatement)pstmt).getQueryString());
    		pstmt.executeUpdate();
    		pstmt.close();
		    
	        conn.commit();
	        conn.close();

        	rs = null;
			pstmt = null;
			conn = null;
			
	        
	        return "0";
	        
	        
		} catch (SQLException sqlexception) {
			sqlexception.printStackTrace();
			if (rs != null)     try{rs.close();}catch (Exception ex){ex.printStackTrace();}
			if (pstmt != null)  try{pstmt.close();}catch (Exception ex2){ex2.printStackTrace();}
			if (conn != null){
				try{
					conn.rollback();
					ConnectionResource.release(conn);
				}catch(Exception ex3){
					ecamsLogger.error("## Cmc0350.setRFCConfirm() connection release exception ##");
					ex3.printStackTrace();
				}				
			}			
			ecamsLogger.error("## Cmc0350.setRFCConfirm() SQLException START ##");
			ecamsLogger.error("## Error DESC : ", sqlexception);	
			ecamsLogger.error("## Cmc0350.setRFCConfirm() SQLException END ##");			
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
					ecamsLogger.error("## Cmc0350.setRFCConfirm() connection release exception ##");
					ex3.printStackTrace();
				}				
			}			
			ecamsLogger.error("## Cmc0350.setRFCConfirm() Exception START ##");				
			ecamsLogger.error("## Error DESC : ", exception);	
			ecamsLogger.error("## Cmc0350.setRFCConfirm() Exception END ##");				
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
					ecamsLogger.error("## Cmc0350.setRFCConfirm() connection release exception ##");
					ex3.printStackTrace();
				}
			}
		}
	}//end of setRFCConfirm() method statement 
    
}//end of Cmc0350 class statement
