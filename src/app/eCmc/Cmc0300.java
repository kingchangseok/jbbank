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
import app.eCmc.Cmc0010;
import app.eCmr.Cmr0200;

/**
 * @author bigeyes
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */


public class Cmc0300{
    /**
     * Logger Class Instance Creation
     * logger
     */	
    Logger ecamsLogger  = EcamsLogger.getLoggerInstance();
    // RFC발행회차
    public Object[] getRfcCnt(String IsrId,String IsrSub,String UserId,String ReqCd) throws SQLException, Exception {
    	Connection        conn        = null;
		PreparedStatement pstmt       = null;
		ResultSet         rs          = null;
		StringBuffer      strQuery    = new StringBuffer();
		ConnectionContext connectionContext = new ConnectionResource();
		boolean           findSw = false;
		int				  pstmtcount  = 1;
		int				  seq		  = 1;
		
		HashMap<String, String>			  rst		  = null;
		ArrayList<HashMap<String, String>>		  rtList	  = new ArrayList<HashMap<String, String>>();

		try {
			conn = connectionContext.getConnection();
			
			strQuery.setLength(0);
			strQuery.append("select nvl(max(cc_seqno),0) max		\n");			
			strQuery.append("  from cmc0120  			            \n");
			strQuery.append(" where CC_ISRID = ?	    		    \n");
			strQuery.append("   and CC_ISRSUB = ?					\n");
			pstmt = conn.prepareStatement(strQuery.toString());	
			//pstmt = new LoggableStatement(conn,strQuery.toString());
			pstmtcount = 1;
			pstmt.setString(pstmtcount++,IsrId);
			pstmt.setString(pstmtcount++,IsrSub);
	        //ecamsLogger.error(((LoggableStatement)pstmt).getQueryString());           
	        rs = pstmt.executeQuery();
			if (rs.next()){
				seq = rs.getInt("max");
			}//end of while-loop statement
			rs.close();
			pstmt.close();
			
			
			
	        boolean secuSw = false;
			rtList.clear();			
			int rfcSeq = 0;	
        	strQuery.setLength(0);	
        	strQuery.append("select nvl(a.cc_handler,'') cc_handler,a.cc_substatus, \n");
        	strQuery.append("       b.cc_seqno,b.cc_acptno,b.cc_rfcuser, \n");
        	strQuery.append("       nvl(b.cc_status,'0') cc_status,      \n");
        	strQuery.append("       b.cc_status,b.cc_expruntime,b.cc_expmm,\n");
        	strQuery.append("       b.rfceddate,b.rfcreqdt,b.cc_expday,  \n");
        	strQuery.append("       b.cc_exprsc,b.cm_username            \n");
        	//strQuery.append("       ,ISRUPDT_CHK(a.CC_ISRID) UPDTOK        \n");//05.13 완료예정일 업데이트 가능 여부 체크.호윤
        	strQuery.append("  from cmc0110 a,                           \n");
        	strQuery.append("       (select x.cc_isrid,x.cc_seqno,x.cc_acptno,\n");
        	strQuery.append("               x.cc_rfcuser,x.cc_status,   \n");
        	strQuery.append("               to_char(x.cc_eddate,'yyyy/mm/dd hh24:mi') rfceddate,\n");
        	strQuery.append("               to_char(x.cc_rfcdate,'yyyy/mm/dd hh24:mi') rfcreqdt,\n");
        	strQuery.append("               x.cc_expruntime,x.cc_expmm, \n");
        	strQuery.append("               x.cc_expday,x.cc_exprsc,    \n");
        	strQuery.append("               y.cm_username               \n");
        	strQuery.append("          from cmc0120 x,cmm0040 y         \n");
        	strQuery.append("         where x.cc_isrid=? and x.cc_isrsub=? \n");
        	strQuery.append("           and x.cc_rfcuser=y.cm_userid) b \n");
        	strQuery.append(" where a.cc_isrid=? and a.cc_isrsub=?      \n");
        	strQuery.append("   and a.cc_isrid=b.cc_isrid(+)            \n");   	
        	strQuery.append(" order by b.cc_seqno desc                  \n");
        	pstmt = conn.prepareStatement(strQuery.toString());
        	//pstmt =  new LoggableStatement(conn, strQuery.toString());
        	pstmt.setString(1, IsrId);
        	pstmt.setString(2, IsrSub);
        	pstmt.setString(3, IsrId);
        	pstmt.setString(4, IsrSub);
        	//ecamsLogger.error(((LoggableStatement)pstmt).getQueryString());
        	rs = pstmt.executeQuery();
        	findSw = false;
        	while (rs.next()) {
        		//if (rs.getString("cc_seqno") == null && rs.getString("cc_handler") == null) break;
        		if (rs.getString("cc_handler") == null) break;
        		if (rs.getString("cc_seqno") == null && !rs.getString("cc_handler").equals(UserId)) break;
        		if (rs.getString("cc_handler").equals(UserId) && ReqCd.equals("33")) secuSw = true;
        		//20110531 광진 변경종료 시점까지 [완료 예정일] 변경
        		//if (rs.getString("cc_handler").equals(UserId) && !ReqCd.equals("39")) secuSw = true; 
        		rst = new HashMap<String, String>();
        		rst.put("cc_handler", rs.getString("cc_handler"));
        		rst.put("cc_substatus", rs.getString("cc_substatus"));
        		if (secuSw == true && rs.getRow() == 1) {
	        		if (rs.getString("cc_seqno") == null) {
	        			if (!rs.getString("cc_handler").equals(UserId)) break;
	        			if (rs.getString("cc_substatus").equals("12")) {
	        				rfcSeq = 1;
	        				findSw = true;
	        			}
	        		} else if(seq==Integer.parseInt(rs.getString("cc_seqno")) && secuSw == true) {
	        			rfcSeq = Integer.parseInt(rs.getString("cc_seqno"))+1;
	        			if (rs.getString("cc_substatus").equals("22")) {
	            			findSw = true;
	            		} else if (rs.getString("cc_status").equals("3") && rs.getString("cc_substatus").equals("16")) {
	        				findSw = true;
	        			}
	        		}
        		}
        		if (rs.getString("cc_seqno") != null){
	        		rst = new HashMap<String, String>();
	        		rst.put("cc_handler", rs.getString("cc_handler"));
	        		rst.put("cc_substatus", rs.getString("cc_substatus"));
	        		rst.put("rfcseq", rs.getString("cc_seqno")+"차 발행");
	        		rst.put("cc_seqno", rs.getString("cc_seqno"));
	        		rst.put("cc_status",rs.getString("cc_status"));
	        		rst.put("cc_acptno", rs.getString("cc_acptno"));
	        		rst.put("cc_rfcuser", rs.getString("cc_rfcuser"));
	        		rst.put("cc_expruntime", rs.getString("cc_expruntime"));
	        		if (rs.getBigDecimal("cc_expmm") != null){
	        			rst.put("cc_expmm", rs.getBigDecimal("cc_expmm").toString());
	        		} else{
	        			rst.put("cc_expmm", "");
	        		}
	        		rst.put("rfceddate", rs.getString("rfceddate"));
	        		rst.put("rfcreqdt", rs.getString("rfcreqdt"));
	        		rst.put("cc_expday", rs.getString("cc_expday"));
	        		rst.put("cc_exprsc", rs.getString("cc_exprsc"));
	        		rst.put("cm_username", rs.getString("cm_username"));
	        		if (rs.getString("cc_status").equals("0")) rst.put("sta", "RFC발행승인요청가능");
	        		else if (rs.getString("cc_status").equals("1")) rst.put("sta", "RFC발행승인대기");
	        		else if (rs.getString("cc_status").equals("3")) rst.put("sta", "RFC발행반려");
	        		else if (rs.getString("cc_status").equals("9")) rst.put("sta", "RFC발행완료");
	        		
	        		if (secuSw == true) {
	        			if (rs.getString("cc_acptno") == null) {
	        				rst.put("secusw", "Y");
	        			} else if (rs.getString("cc_status").equals("0")) rst.put("secusw", "Y"); 
	        			else rst.put("secusw", "N");
	        		}
	        		
	        		//rst.put("editISRYN", rs.getString("UPDTOK"));//05.13 완료예정일 업데이트 가능 여부 체크.호윤
	        		rtList.add(rst);
        		}
        	}
        	rs.close();
        	pstmt.close();
        	
        	if (findSw == true) {
        		rst = new HashMap<String, String>();
        		rst.put("rfcseq", Integer.toString(rfcSeq)+"차 발행");
				rst.put("cc_seqno", Integer.toString(rfcSeq));
				rst.put("sta", "RFC발행가능");
				rst.put("cc_status", "X");

        		rst.put("secusw", "Y");
        		rtList.add(0,rst);
        	}
        	conn.close();
        	
        	rs = null;
			pstmt = null;
			conn = null;
			
        	return rtList.toArray();
			
		} catch (SQLException sqlexception) {
			sqlexception.printStackTrace();
			ecamsLogger.error("## Cmc0300.getRfcCnt() SQLException START ##");
			ecamsLogger.error("## Error DESC : ", sqlexception);	
			ecamsLogger.error("## Cmc0300.getRfcCnt() SQLException END ##");			
			throw sqlexception;
		} catch (Exception exception) {
			exception.printStackTrace();
			ecamsLogger.error("## Cmc0300.getRfcCnt() Exception START ##");				
			ecamsLogger.error("## Error DESC : ", exception);	
			ecamsLogger.error("## Cmc0300.getRfcCnt() Exception END ##");				
			throw exception;
		}finally{
			if (strQuery != null) 	strQuery = null;
			if (rs != null)     try{rs.close();}catch (Exception ex){ex.printStackTrace();}
			if (pstmt != null)  try{pstmt.close();}catch (Exception ex2){ex2.printStackTrace();}
			if (conn != null){
				try{
					ConnectionResource.release(conn);
				}catch(Exception ex3){
					ecamsLogger.error("## Cmc0300.getRfcCnt() connection release exception ##");
					ex3.printStackTrace();
				}
			}
		}
	}
    //getTestInfo_Recv(strIsrId,strIsrSub,btCmd.enabled,cboTest.selectedItem.cc_testseq);
	
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
			int strMAXSEQNO = 1;
			String strISRID = etcData.get("isrid");
			if (strISRID == "" && strISRID == null){
				return "ISRID 정보가 없습니다. 관리자에게 문의해주십시오.";
			}
			
			if (etcData.get("gbncd").equals("REQ")) {
				//RFC 발행 신청번호 받아오기
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
			}
        	
			/* cmc0120
			ISR ID,ISR SUBNO,요청 SEQNO,RFC발행번호,RFC발행인,
			RFC발행일시,상태 0:저장 1:승인요청 3:반려 9:완료,예상소요시간,예상투입공수,
			완료예정일,예상소요자원 
			 */
			conn.setAutoCommit(false);  
			
			strQuery.setLength(0);
    		strQuery.append("delete cmc0120                     \n");
    		strQuery.append(" where CC_ISRID=? and CC_ISRSUB=?  \n");
    		strQuery.append("   and cc_seqno=?                  \n");
    		pstmt = conn.prepareStatement(strQuery.toString());
    		// pstmt = new LoggableStatement(conn,strQuery.toString());
    		pstmtcount = 1;
    		pstmt.setString(pstmtcount++, strISRID);
    		pstmt.setString(pstmtcount++, etcData.get("isrsub"));
    		pstmt.setInt(pstmtcount++, Integer.parseInt(etcData.get("seqno")));
    	//	ecamsLogger.error(((LoggableStatement)pstmt).getQueryString());
	        pstmt.executeUpdate();
	        pstmt.close();
	        
        	strQuery.setLength(0);
        	strQuery.append("insert into cmc0120 ");
        	strQuery.append("       (CC_ISRID,CC_ISRSUB,CC_SEQNO,CC_ACPTNO,CC_RFCUSER, \n");
        	strQuery.append("        CC_RFCDATE,CC_STATUS,CC_EXPRUNTIME,CC_EXPMM, \n");
        	strQuery.append("        CC_EXPDAY,CC_EXPRSC) ");
    		strQuery.append("values (?,?,?,?,?, SYSDATE,?,?,?, ?,?) \n");
    		
	        pstmt = conn.prepareStatement(strQuery.toString());
	      // pstmt = new LoggableStatementnt(conn,strQuery.toString());
			pstmtcount = 1;
			pstmt.setString(pstmtcount++, etcData.get("isrid"));
        	pstmt.setString(pstmtcount++, etcData.get("isrsub"));
        	pstmt.setInt(pstmtcount++, Integer.parseInt(etcData.get("seqno")));
        	if (etcData.get("gbncd").equals("REQ")) pstmt.setString(pstmtcount++, AcptNo);
        	else pstmt.setString(pstmtcount++, "");
        	pstmt.setString(pstmtcount++, etcData.get("userid"));
        	
        	if (etcData.get("gbncd").equals("REQ")) pstmt.setString(pstmtcount++, "1");
        	else pstmt.setString(pstmtcount++, "0");
        	pstmt.setString(pstmtcount++, etcData.get("CC_EXPRUNTIME"));
        	pstmt.setString(pstmtcount++, etcData.get("CC_EXPMM"));
        	
        	pstmt.setString(pstmtcount++, etcData.get("CC_EXPDAY"));
        	pstmt.setString(pstmtcount++, etcData.get("CC_EXPRSC"));
        	
        //	ecamsLogger.error(((LoggableStatement)pstmt).getQueryString());
    		pstmt.executeUpdate();
    		pstmt.close(); 
    		
    		if (etcData.get("gbncd").equals("REQ")) {    		
	    		Cmc0010 cmc0010 = new Cmc0010();
	    		retMsg = cmc0010.base_Confirm(AcptNo,etcData.get("isrid"),etcData.get("isrsub"),etcData.get("userid"),etcData.get("reqcd"),conn);
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
					throw new Exception("결재정보등록 중 오류가 발생하였습니다. 관리자에게 연락하여 주시기 바랍니다.");
				}
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
    		strQuery.append("update cmc0110 set \n");
    		if (etcData.get("gbncd").equals("REQ")) {
	    		strQuery.append("   CC_SUBSTATUS='14', \n");
	    		strQuery.append("   CC_RFCACPT=?, \n");
    		}
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
			if (etcData.get("gbncd").equals("REQ"))
				pstmt.setString(pstmtcount++, AcptNo);
			pstmt.setString(pstmtcount++, etcData.get("userid"));
        	pstmt.setString(pstmtcount++, etcData.get("CC_EXPRUNTIME"));
        	pstmt.setString(pstmtcount++, etcData.get("CC_EXPMM"));
        	pstmt.setString(pstmtcount++, etcData.get("CC_EXPDAY"));
        	pstmt.setString(pstmtcount++, etcData.get("CC_EXPRSC"));
        	pstmt.setString(pstmtcount++, etcData.get("isrid"));
        	pstmt.setString(pstmtcount++, etcData.get("isrsub"));
       // 	ecamsLogger.error(((LoggableStatement)pstmt).getQueryString());
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
					ecamsLogger.error("## Cmc0300.setRFCConfirm() connection release exception ##");
					ex3.printStackTrace();
				}				
			}			
			ecamsLogger.error("## Cmc0300.setRFCConfirm() SQLException START ##");
			ecamsLogger.error("## Error DESC : ", sqlexception);	
			ecamsLogger.error("## Cmc0300.setRFCConfirm() SQLException END ##");			
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
					ecamsLogger.error("## Cmc0300.setRFCConfirm() connection release exception ##");
					ex3.printStackTrace();
				}				
			}			
			ecamsLogger.error("## Cmc0300.setRFCConfirm() Exception START ##");				
			ecamsLogger.error("## Error DESC : ", exception);	
			ecamsLogger.error("## Cmc0300.setRFCConfirm() Exception END ##");				
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
					ecamsLogger.error("## Cmc0300.setRFCConfirm() connection release exception ##");
					ex3.printStackTrace();
				}
			}
		}
	}//end of setRFCConfirm() method statement 
	
	public String updateRFC(HashMap<String,String> etcData) throws SQLException, Exception {
		Connection        conn        = null;
		PreparedStatement pstmt       = null;
		ResultSet         rs          = null;
		StringBuffer      strQuery    = new StringBuffer();
		String            retMsg      = "0";
		AutoSeq			  autoseq	  = new AutoSeq();
		
		ConnectionContext connectionContext = new ConnectionResource();		
		
		try {
			
			conn = connectionContext.getConnection();
			int pstmtcount = 1;
			
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
    		strQuery.append("update cmc0110 set \n");
    		strQuery.append("       CC_EXPDAY=? \n");
    		strQuery.append(" where CC_ISRID=? \n");
    		strQuery.append("   and CC_ISRSUB=? \n");
    		    		
	        pstmt = conn.prepareStatement(strQuery.toString());
	        // pstmt = new LoggableStatement(conn,strQuery.toString());
			pstmtcount = 1;
        	pstmt.setString(pstmtcount++, etcData.get("CC_EXPDAY"));
        	pstmt.setString(pstmtcount++, etcData.get("isrid"));
        	pstmt.setString(pstmtcount++, etcData.get("isrsub"));
       // 	ecamsLogger.error(((LoggableStatement)pstmt).getQueryString());
    		pstmt.executeUpdate();
    		pstmt.close();
		    
    		strQuery.setLength(0);
    		strQuery.append("update cmc0120 set \n");
    		strQuery.append("       CC_EXPDAY=? \n");
    		strQuery.append(" where CC_ISRID=? \n");
    		strQuery.append("   and CC_ISRSUB=? \n");
    		strQuery.append("   and CC_SEQNO=? \n");
    		    		
	        pstmt = conn.prepareStatement(strQuery.toString());
	        // pstmt = new LoggableStatement(conn,strQuery.toString());
			pstmtcount = 1;
        	pstmt.setString(pstmtcount++, etcData.get("CC_EXPDAY"));
        	pstmt.setString(pstmtcount++, etcData.get("isrid"));
        	pstmt.setString(pstmtcount++, etcData.get("isrsub"));
        	pstmt.setInt(pstmtcount++, Integer.parseInt(etcData.get("seqno")));
       // 	ecamsLogger.error(((LoggableStatement)pstmt).getQueryString());
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
					ecamsLogger.error("## Cmc0300.setRFCConfirm() connection release exception ##");
					ex3.printStackTrace();
				}				
			}			
			ecamsLogger.error("## Cmc0300.setRFCConfirm() SQLException START ##");
			ecamsLogger.error("## Error DESC : ", sqlexception);	
			ecamsLogger.error("## Cmc0300.setRFCConfirm() SQLException END ##");			
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
					ecamsLogger.error("## Cmc0300.setRFCConfirm() connection release exception ##");
					ex3.printStackTrace();
				}				
			}			
			ecamsLogger.error("## Cmc0300.setRFCConfirm() Exception START ##");				
			ecamsLogger.error("## Error DESC : ", exception);	
			ecamsLogger.error("## Cmc0300.setRFCConfirm() Exception END ##");				
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
					ecamsLogger.error("## Cmc0300.setRFCConfirm() connection release exception ##");
					ex3.printStackTrace();
				}
			}
		}
	}//end of setRFCConfirm() method statement 
    
}//end of Cmc0300 class statement
