/*****************************************************************************************
	1. program ID	: Cm0010.java
	2. create date	: 2010.11. 18
	3. auth		    : is.choi
	4. update date	: 
	5. auth		    : 
	6. description	: 1. ISR STATUS COMMON
*****************************************************************************************/

package app.eCmc;

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
import app.eCmr.*;

/**
 * @author bigeyes 
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class Cmc0010{    
	

    /**
     * Logger Class Instance Creation
     * logger
     */
	
    Logger ecamsLogger  = EcamsLogger.getLoggerInstance();
    
	
    
    public String ISRSta_common_Cmc0100(String IsrId) throws SQLException, Exception {
    	Connection        conn        = null;
		PreparedStatement pstmt       = null;
		ResultSet         rs          = null;
		StringBuffer      strQuery    = new StringBuffer();
		ConnectionContext connectionContext = new ConnectionResource();
		int				  cnt  = 0;
		String            strSta = "";
		String            svSta = "0";
		boolean           findSw = false;

		try {
			conn = connectionContext.getConnection();
	        
	        strQuery.setLength(0);	        
	        strQuery.append("select cc_status from cmc0100 \n");
        	strQuery.append(" where cc_isrid=?                \n");
	        
        	pstmt = conn.prepareStatement(strQuery.toString());
        	pstmt.setString(1, IsrId);        	
        	rs = pstmt.executeQuery();
        	if (rs.next()){        		
        		svSta = rs.getString("cc_status");
        	}
        	rs.close();
        	pstmt.close();
        	
        	String arrSta[] = svSta.split(",");
        	
	        strQuery.setLength(0);	        
	        strQuery.append("select cc_substatus from cmc0110 \n");
        	strQuery.append(" where cc_isrid=?                \n");	        
        	pstmt = conn.prepareStatement(strQuery.toString());
        	pstmt.setString(1, IsrId);        	
        	rs = pstmt.executeQuery();
        	while (rs.next()){
        		if (!rs.getString("cc_substatus").equals("13")) {
	        		if (strSta.length()>0) strSta = strSta + ",";
	        		strSta = strSta + rs.getString("cc_substatus"); 
        		}
        	}
        	rs.close();
        	pstmt.close();
        	//0:등록,1:ISR접수중,5:개발진행 중,7:요청자완료대기,C:진행중단,9:정상종료,8:ISR완료대기        
        	findSw = true;
        	for (int i=0;arrSta.length>i;i++) {
        		if (!arrSta[i].equals("11") && !arrSta[i].equals("13")) {
        			findSw = false;
        		}
        	}
        	
        	if (findSw == true) {
        		svSta = "0";  // 등록상태로 Set
        	} else {        	
	        	//ISR접수중 Check
	        	findSw = true;
	        	for (int i=0;arrSta.length>i;i++) {
	        		if (arrSta[i].equals("11") ||        //ISR요청
	        			arrSta[i].equals("12") ||        //ISR접수완료 
	        			arrSta[i].equals("14") ||        //RFC발행승인요청
	        			arrSta[i].equals("15") ||        //RFC발행요청에 대한 승인
	        			arrSta[i].equals("16") ||        //RFC발행요청에 대한 반려
	        			arrSta[i].equals("22")) {        //RFC접수에서 반려
	        			findSw = false;
	        			break;
	        		}
	        	}
	        	if (findSw == true) {
	        		svSta = "1";  // ISR접수 중 상태로 Set
	        	} 
        	}
        	
        	if (findSw == false) {      	
	        	//개발진행 중 Check 
	        	findSw = true;
	        	for (int i=0;arrSta.length>i;i++) {
	        		if (arrSta[i].equals("21") ||        //RFC접수
	        			arrSta[i].equals("23") ||        //단위테스트중
	        			arrSta[i].equals("24") ||        //테스트적용요청승인대기
	        			arrSta[i].equals("25") ||        //테스트적용완료
	        			arrSta[i].equals("26") ||        //작업계획서 작성
	        			arrSta[i].equals("27") ||        //운영적용승인대기
	        			arrSta[i].equals("28") ||        //변경종료승인대기
	        			arrSta[i].equals("2A") ||        //변경완료요청반려
	        			arrSta[i].equals("31") ||        //테스트접수완료
	        			arrSta[i].equals("32") ||        //테스트중
	        			arrSta[i].equals("34") ||        //테스트종료승인대기
	        			arrSta[i].equals("35") ||        //테스트종료(반려)
	        			arrSta[i].equals("36") ||        //테스트종료(비정상)
	        			arrSta[i].equals("39") ||        //테스트종료(정상)
	        			arrSta[i].equals("41") ||        //릴리즈승인대기
	        			arrSta[i].equals("49")) {        //릴리즈종료
	        			findSw = false;
	        			break;
	        		}
	        	}
	        	if (findSw == true) {
	        		svSta = "5";  // 개발진행 중 상태로 Set
	        	}
        	}
        	if (findSw == false) {      	
	        	//요청자완료대기 Check 
	        	findSw = true;
	        	for (int i=0;arrSta.length>i;i++) {
	        		if (!arrSta[i].equals("19")) {        //ISR종료
	        			findSw = false;
	        			break;
	        		}
	        	}
	        	if (findSw == true) {
	        		svSta = "7";  // 요청자종료대기로 Set
	        	}
        	}
        	if (findSw == false) {      	
	        	//ISR완료대기 Check (11,12,14,15,16)
	        	findSw = true;
	        	for (int i=0;arrSta.length>i;i++) {
	        		if (!arrSta[i].equals("29")) {        //변경관리종료
	        			findSw = false;
	        			break;
	        		}
	        	}
	        	if (findSw == true) {
	        		svSta = "7";                         // ISR종료대기로 Set
	        	}
        	}
        	
        	if (svSta.length()>0) {
	        	strQuery.setLength(0);
	        	strQuery.append("update cmc0100 set cc_status=?     \n");
	        	strQuery.append(" where cc_isrid=? and cc_status<>? \n");
	        	cnt = 0;
	        	pstmt = conn.prepareStatement(strQuery.toString());
	        	 
	        	pstmt.setString(cnt, svSta);
	        	pstmt.setString(cnt, IsrId);
	        	
	        	pstmt.executeUpdate();            	
	        	pstmt.close();
        	}
        	conn.close();
        	
        	rs = null;
        	pstmt = null;
        	conn = null;
        	return "OK";
			
		} catch (SQLException sqlexception) {
			sqlexception.printStackTrace();
			ecamsLogger.error("## Cmc0010.ISRSta_common_Cmc0100() SQLException START ##");
			ecamsLogger.error("## Error DESC : ", sqlexception);	
			ecamsLogger.error("## Cmc0010.ISRSta_common_Cmc0100() SQLException END ##");			
			throw sqlexception;
		} catch (Exception exception) {
			exception.printStackTrace();
			ecamsLogger.error("## Cmc0010.ISRSta_common_Cmc0100() Exception START ##");				
			ecamsLogger.error("## Error DESC : ", exception);	
			ecamsLogger.error("## Cmc0010.ISRSta_common_Cmc0100() Exception END ##");				
			throw exception;
		}finally{
			if (strQuery != null) 	strQuery = null;
			if (rs != null)     try{rs.close();}catch (Exception ex){ex.printStackTrace();}
			if (pstmt != null)  try{pstmt.close();}catch (Exception ex2){ex2.printStackTrace();}
			if (conn != null){
				try{
					ConnectionResource.release(conn);
				}catch(Exception ex3){
					ecamsLogger.error("## Cmc0010.ISRSta_common_Cmc0100() connection release exception ##");
					ex3.printStackTrace();
				}
			}
		}
	}
    public String ISRSta_common_Cmc0110(String IsrId,String IsrSub,String ReqCd) throws SQLException, Exception {
    	Connection        conn        = null;
		PreparedStatement pstmt       = null;
		ResultSet         rs          = null;
		StringBuffer      strQuery    = new StringBuffer();
		ConnectionContext connectionContext = new ConnectionResource();
		int				  cnt  = 0;
		String            strSta = "";
		String            svSta = "0";
		boolean           findSw = false;

		try {
			conn = connectionContext.getConnection();
	        
			
        	conn.close();
        	conn = null;
        	return "OK";
			
		} catch (SQLException sqlexception) {
			sqlexception.printStackTrace();
			ecamsLogger.error("## Cmc0010.ISRSta_common_Cmc0100() SQLException START ##");
			ecamsLogger.error("## Error DESC : ", sqlexception);	
			ecamsLogger.error("## Cmc0010.ISRSta_common_Cmc0100() SQLException END ##");			
			throw sqlexception;
		} catch (Exception exception) {
			exception.printStackTrace();
			ecamsLogger.error("## Cmc0010.ISRSta_common_Cmc0100() Exception START ##");				
			ecamsLogger.error("## Error DESC : ", exception);	
			ecamsLogger.error("## Cmc0010.ISRSta_common_Cmc0100() Exception END ##");				
			throw exception;
		}finally{
			if (strQuery != null) 	strQuery = null;
			if (rs != null)     try{rs.close();}catch (Exception ex){ex.printStackTrace();}
			if (pstmt != null)  try{pstmt.close();}catch (Exception ex2){ex2.printStackTrace();}
			if (conn != null){
				try{
					ConnectionResource.release(conn);
				}catch(Exception ex3){
					ecamsLogger.error("## Cmc0010.ISRSta_common_Cmc0100() connection release exception ##");
					ex3.printStackTrace();
				}
			}
		}
	}

	public String base_Confirm(String AcptNo,String IsrId,String IsrSub,String UserId,String ReqCd,Connection conn) throws SQLException, Exception {
		
		PreparedStatement pstmt       = null;
		StringBuffer      strQuery    = new StringBuffer();
		int               i = 0;
		int               pstmtcount = 0;
		int               SeqNo = 0;
		try {
    		strQuery.setLength(0);        	
        	strQuery.append("insert into cmc0300 \n");
        	strQuery.append("(CC_ACPTNO,CC_ISRID,CC_ISRSUB,CC_EDITOR,CC_TEAMCD,CC_QRYCD,CC_ACPTDATE,CC_STATUS) \n");
        	strQuery.append("(select ?, ?, ?, cm_userid,cm_project,?,SYSDATE,'0'   \n");
        	strQuery.append("   from cmm0040 where cm_userid=?)   \n");
        	
        	pstmt = conn.prepareStatement(strQuery.toString());
        	pstmtcount = 1;
        	pstmt.setString(pstmtcount++, AcptNo);
        	pstmt.setString(pstmtcount++, IsrId);
        	pstmt.setString(pstmtcount++, IsrSub);
        	pstmt.setString(pstmtcount++, ReqCd);
        	pstmt.setString(pstmtcount++, UserId);        	
        	pstmt.executeUpdate();
        	pstmt.close();
        	pstmt = null;
        	
        	//ecamsLogger.error("+++++++++Request E N D+++");
        	return "0";
			
		} catch (SQLException sqlexception) {
			sqlexception.printStackTrace();
			ecamsLogger.error("## Cmc0010.base_Confirm() SQLException START ##");
			ecamsLogger.error("## Error DESC : ", sqlexception);	
			ecamsLogger.error("## Cmc0010.base_Confirm() SQLException END ##");			
			return "1";
		} catch (Exception exception) {
			exception.printStackTrace();
			ecamsLogger.error("## Cmc0010.base_Confirm() Exception START ##");				
			ecamsLogger.error("## Error DESC : ", exception);	
			ecamsLogger.error("## Cmc0010.base_Confirm() Exception END ##");				
			return "2";
		}finally{
			if (strQuery != null) 	strQuery = null;
			if (pstmt != null)  try{pstmt.close();}catch (Exception ex2){ex2.printStackTrace();}
		}		
	}
	
	public int maxFileSeq(String ReqId,String ReqCd) throws SQLException, Exception {
		Connection        conn        = null;
		PreparedStatement pstmt       = null;
		ResultSet         rs          = null;
		StringBuffer      strQuery    = new StringBuffer();
		ConnectionContext connectionContext = new ConnectionResource();
		try {
			
			int seq = 1;
			conn = connectionContext.getConnection();
    		strQuery.setLength(0);        	
        	strQuery.append("select nvl(max(cc_seqno),0) maxseq   \n");      	
        	strQuery.append("  from cmc1001                       \n");
        	strQuery.append(" where cc_id=? 					  \n");
        	strQuery.append("   and cc_reqcd=?                    \n");
        	pstmt = conn.prepareStatement(strQuery.toString());
        	pstmt =  new LoggableStatement(conn, strQuery.toString());
        	pstmt.setString(1, ReqId);
        	pstmt.setString(2, ReqCd);  
        	ecamsLogger.error(((LoggableStatement)pstmt).getQueryString());
        	rs = pstmt.executeQuery();
        	if (rs.next()) {
        		seq = rs.getInt("maxseq") + 1;
        	}
        	rs.close();
        	pstmt.close();
        	conn.close();
        	
        	rs = null;
        	pstmt = null;
        	conn = null;
        	
        	return seq;
        	
			
		} catch (SQLException sqlexception) {
			sqlexception.printStackTrace();
			ecamsLogger.error("## Cmc0010.maxFileSeq() SQLException START ##");
			ecamsLogger.error("## Error DESC : ", sqlexception);	
			ecamsLogger.error("## Cmc0010.maxFileSeq() SQLException END ##");	
			return 0;
		} catch (Exception exception) {
			exception.printStackTrace();
			ecamsLogger.error("## Cmc0010.maxFileSeq() Exception START ##");				
			ecamsLogger.error("## Error DESC : ", exception);	
			ecamsLogger.error("## Cmc0010.base_Confirm() Exception END ##");	
			return 0;
		}finally{
			if (strQuery != null) 	strQuery = null;
			if (rs != null)     try{rs.close();}catch (Exception ex){ex.printStackTrace();}
			if (pstmt != null)  try{pstmt.close();}catch (Exception ex2){ex2.printStackTrace();}
			if (conn != null){
				try{
					ConnectionResource.release(conn);
				}catch(Exception ex3){
					ecamsLogger.error("## Cmc0010.maxFileSeq() connection release exception ##");
					ex3.printStackTrace();
				}
			}	
		}	
	} //maxFileSeq

	public String setDocFile(HashMap<String,String> docFile) throws SQLException, Exception {
		Connection        conn        = null;
		PreparedStatement pstmt       = null;
		ResultSet         rs          = null;
		StringBuffer      strQuery    = new StringBuffer();
		ConnectionContext connectionContext = new ConnectionResource();
		
		try {
			conn = connectionContext.getConnection();
			String retMsg = "OK";
			
			strQuery.setLength(0);
			strQuery.append("select count(*) cnt from cmc1001     \n");
			strQuery.append(" where cc_id=?   				   \n");
			strQuery.append("   and cc_reqcd=? and cc_seqno=?     \n");		
			pstmt = conn.prepareStatement(strQuery.toString());		
			pstmt =  new LoggableStatement(conn, strQuery.toString());
			pstmt.setString(1, docFile.get("reqid"));
			pstmt.setString(2, docFile.get("reqcd"));
			pstmt.setString(3, docFile.get("fileseq"));	
			ecamsLogger.error(((LoggableStatement)pstmt).getQueryString());
            rs = pstmt.executeQuery();
            if (rs.next()) {
            	if (rs.getInt("cnt") > 0) retMsg = "기 등록된 일련번호입니다. 다시 전송하여 주시기 바랍니다.";
            }
            rs.close();
            pstmt.close();
            
            if (retMsg.equals("OK")) {
				strQuery.setLength(0);
				strQuery.append("insert into cmc1001                  \n");
			    strQuery.append("  (CC_ID,CC_SUBID,			      \n");
			    strQuery.append("   CC_SUBREQ,CC_SEQNO,CC_SAVEFILE,   \n");
			    strQuery.append("   CC_ATTFILE,CC_LASTDT,CC_EDITOR,CC_REQCD)   \n");
			    strQuery.append("values                               \n");
				strQuery.append("(?, ?, ?, ?, ?, ?, SYSDATE,?,?)     \n");		
				pstmt = conn.prepareStatement(strQuery.toString());		
				pstmt =  new LoggableStatement(conn, strQuery.toString());
				pstmt.setString(1, docFile.get("reqid"));
				pstmt.setString(2, docFile.get("reqcd"));
				pstmt.setString(3, docFile.get("subreq"));
				pstmt.setString(4, docFile.get("fileseq"));
				pstmt.setString(5, docFile.get("savefile"));
				pstmt.setString(6, docFile.get("attfile"));
				pstmt.setString(7, docFile.get("userid"));
				pstmt.setString(8, docFile.get("reqcd"));	
				ecamsLogger.error(((LoggableStatement)pstmt).getQueryString());
	            pstmt.executeUpdate();
	            pstmt.close();
            }
            conn.close();
            
			rs = null;
			pstmt = null;
			conn = null;            
			
			return retMsg;
	
		} catch (SQLException sqlexception) {
			sqlexception.printStackTrace();
			ecamsLogger.error("## Cmd0500.setDocFile() SQLException START ##");
			ecamsLogger.error("## Error DESC : ", sqlexception);	
			ecamsLogger.error("## Cmd0500.setDocFile() SQLException END ##");			
			throw sqlexception;
		} catch (Exception exception) {
			exception.printStackTrace();
			ecamsLogger.error("## Cmd0500.setDocFile() Exception START ##");				
			ecamsLogger.error("## Error DESC : ", exception);	
			ecamsLogger.error("## Cmd0500.setDocFile() Exception END ##");				
			throw exception;
		}finally{
			if (strQuery != null) 	strQuery = null;
			if (rs != null)     try{rs.close();}catch (Exception ex){ex.printStackTrace();}
			if (pstmt != null)  try{pstmt.close();}catch (Exception ex2){ex2.printStackTrace();}
			if (conn != null){
				try{
					ConnectionResource.release(conn);
				}catch(Exception ex3){
					ecamsLogger.error("## Cmd0500.setDocFile() connection release exception ##");
					ex3.printStackTrace();
				}
			}
		}
		
		
	}//end of setDocFile() method statement
	//delFile(strIsrId,strIsrSub,strReqCd,strSubReq,grdFile.selectedItem.cc_seqno)
	
	public int DelDocFile(HashMap<String,String> docFile) throws SQLException, Exception {
		Connection        conn        = null;
		PreparedStatement pstmt       = null;
		ResultSet         rs          = null;
		StringBuffer      strQuery    = new StringBuffer();
		ConnectionContext connectionContext = new ConnectionResource();
		int rtn = 0;
		try {
			conn = connectionContext.getConnection();
		    strQuery.setLength(0);
			strQuery.append("delete from cmr0040 where cr_prjno = ? and cr_docid = ? and cr_itemid = ? and cr_syscd = ?  \n");	
			pstmt = conn.prepareStatement(strQuery.toString());		
			pstmt =  new LoggableStatement(conn, strQuery.toString());
			pstmt.setString(1, docFile.get("cr_prjno"));
			pstmt.setString(2, docFile.get("cr_docid"));
			
			pstmt.setString(3, docFile.get("cr_itemid"));
			pstmt.setString(4  , docFile.get("cr_syscd"));
			ecamsLogger.error(((LoggableStatement)pstmt).getQueryString());
            rtn = pstmt.executeUpdate();
            
            pstmt.close();
            
           
            conn.close();
            
			rs = null;
			pstmt = null;
			conn = null;            
			
			return rtn;
	
		} catch (SQLException sqlexception) {
			sqlexception.printStackTrace();
			ecamsLogger.error("## Cmd0500.DelDocFile() SQLException START ##");
			ecamsLogger.error("## Error DESC : ", sqlexception);	
			ecamsLogger.error("## Cmd0500.DelDocFile() SQLException END ##");			
			throw sqlexception;
		} catch (Exception exception) {
			exception.printStackTrace();
			ecamsLogger.error("## Cmd0500.DelDocFile() Exception START ##");				
			ecamsLogger.error("## Error DESC : ", exception);	
			ecamsLogger.error("## Cmd0500.DelDocFile() Exception END ##");				
			throw exception;
		}finally{
			if (strQuery != null) 	strQuery = null;
			if (rs != null)     try{rs.close();}catch (Exception ex){ex.printStackTrace();}
			if (pstmt != null)  try{pstmt.close();}catch (Exception ex2){ex2.printStackTrace();}
			if (conn != null){
				try{
					ConnectionResource.release(conn);
				}catch(Exception ex3){
					ecamsLogger.error("## Cmd0500.DelDocFile() connection release exception ##");
					ex3.printStackTrace();
				}
			}
		}
		
		
	}//end of setDocFile() method statement
	
	public int DelDocFile2(HashMap<String,String> docFile) throws SQLException, Exception {
		Connection        conn        = null;
		PreparedStatement pstmt       = null;
		ResultSet         rs          = null;
		StringBuffer      strQuery    = new StringBuffer();
		ConnectionContext connectionContext = new ConnectionResource();
		int rtn = 0;
		try {
			conn = connectionContext.getConnection();
		    strQuery.setLength(0);
			strQuery.append("delete from cmr0040 where cr_prjno = ? and cr_docid = ?  and cr_itemid = ? and cr_syscd = ?  \n");
			
			pstmt = conn.prepareStatement(strQuery.toString());		
			pstmt =  new LoggableStatement(conn, strQuery.toString());
			pstmt.setString(1, docFile.get("cr_prjno"));
			pstmt.setString(2, docFile.get("cr_docid"));
			pstmt.setString(3, docFile.get("cr_itemid"));
			pstmt.setString(4, docFile.get("cr_syscd"));
			ecamsLogger.error(((LoggableStatement)pstmt).getQueryString());
            rtn = pstmt.executeUpdate();
            pstmt.close();
            
			strQuery.setLength(0);
			strQuery.append("update cmr0030 set cr_closedt = SYSDATE where cr_prjno = ? and cr_docid = ? and cr_docseq = ?  \n");	
			pstmt = conn.prepareStatement(strQuery.toString());		
			pstmt =  new LoggableStatement(conn, strQuery.toString());
			pstmt.setString(1, docFile.get("cr_prjno"));
			pstmt.setString(2, docFile.get("cr_docid"));
			pstmt.setString(3, docFile.get("cr_docseq"));
			ecamsLogger.error(((LoggableStatement)pstmt).getQueryString());
            rtn = pstmt.executeUpdate();
            
            
            pstmt.close();
            conn.close();
            
			rs = null;
			pstmt = null;
			conn = null;            
			
			return rtn;
	
		} catch (SQLException sqlexception) {
			sqlexception.printStackTrace();
			ecamsLogger.error("## Cmd0500.DelDocFile2() SQLException START ##");
			ecamsLogger.error("## Error DESC : ", sqlexception);	
			ecamsLogger.error("## Cmd0500.DelDocFile2() SQLException END ##");			
			throw sqlexception;
		} catch (Exception exception) {
			exception.printStackTrace();
			ecamsLogger.error("## Cmd0500.DelDocFile2() Exception START ##");				
			ecamsLogger.error("## Error DESC : ", exception);	
			ecamsLogger.error("## Cmd0500.DelDocFile2() Exception END ##");				
			throw exception;
		}finally{
			if (strQuery != null) 	strQuery = null;
			if (rs != null)     try{rs.close();}catch (Exception ex){ex.printStackTrace();}
			if (pstmt != null)  try{pstmt.close();}catch (Exception ex2){ex2.printStackTrace();}
			if (conn != null){
				try{
					ConnectionResource.release(conn);
				}catch(Exception ex3){
					ecamsLogger.error("## Cmd0010.DelDocFile2() connection release exception ##");
					ex3.printStackTrace();
				}
			}
		}
		
		
	}//end of setDocFile() method statement
	//delFile(strIsrId,strIsrSub,strReqCd,strSubReq,grdFile.selectedItem.cc_seqno)
	
	public String maxDocId() throws SQLException, Exception {
		Connection        conn        = null;
		PreparedStatement pstmt       = null;
		ResultSet         rs          = null;
		StringBuffer      strQuery    = new StringBuffer();
		ConnectionContext connectionContext = new ConnectionResource();
		try {
			
			String maxId = "0";
			conn = connectionContext.getConnection();
    		strQuery.setLength(0);        	
        	strQuery.append("select nvl(max(cr_docid),0) maxseq   \n");      	
        	strQuery.append("  from cmr0030                       \n");
        
        	pstmt = conn.prepareStatement(strQuery.toString());
        	pstmt =  new LoggableStatement(conn, strQuery.toString());
        	
        	ecamsLogger.error(((LoggableStatement)pstmt).getQueryString());
        	rs = pstmt.executeQuery();
        	if (rs.next()) {
        		maxId = rs.getString("maxseq");
        	}
        	rs.close();
        	pstmt.close();
        	conn.close();
        	
        	rs = null;
        	pstmt = null;
        	conn = null;
        	
        	return maxId;
			
		} catch (SQLException sqlexception) {
			sqlexception.printStackTrace();
			ecamsLogger.error("## Cmc0010.maxDocId() SQLException START ##");
			ecamsLogger.error("## Error DESC : ", sqlexception);	
			ecamsLogger.error("## Cmc0010.maxDocId() SQLException END ##");	
			return "0";
		} catch (Exception exception) {
			exception.printStackTrace();
			ecamsLogger.error("## Cmc0010.maxDocId() Exception START ##");				
			ecamsLogger.error("## Error DESC : ", exception);	
			ecamsLogger.error("## Cmc0010.base_Confirm() Exception END ##");	
			return "0";
		}finally{
			if (strQuery != null) 	strQuery = null;
			if (rs != null)     try{rs.close();}catch (Exception ex){ex.printStackTrace();}
			if (pstmt != null)  try{pstmt.close();}catch (Exception ex2){ex2.printStackTrace();}
			if (conn != null){
				try{
					ConnectionResource.release(conn);
				}catch(Exception ex3){
					ecamsLogger.error("## Cmc0010.maxFileSeq() connection release exception ##");
					ex3.printStackTrace();
				}
			}	
		}	
	} //maxFileSeq
	
	public String setDocInfo(HashMap<String,String> docFile) throws SQLException, Exception {
		Connection        conn        = null;
		PreparedStatement pstmt       = null;
		ResultSet         rs          = null;
		StringBuffer      strQuery    = new StringBuffer();
		ConnectionContext connectionContext = new ConnectionResource();
		
		try {
			conn = connectionContext.getConnection();
			String retMsg = "OK";
			int rtn = 0;
			strQuery.setLength(0);
			strQuery.append("select count(*) cnt from cmr0030     \n");
			strQuery.append(" where cr_docid = ?   				   \n");
			pstmt = conn.prepareStatement(strQuery.toString());		
			pstmt =  new LoggableStatement(conn, strQuery.toString());
			pstmt.setString(1, docFile.get("cr_docid"));
			ecamsLogger.error(((LoggableStatement)pstmt).getQueryString());
            rs = pstmt.executeQuery();
            if (rs.next()) {
            	if (rs.getInt("cnt") > 0) retMsg = "기 등록된 일련번호입니다. 다시 전송하여 주시기 바랍니다.";
            }
            rs.close();
            pstmt.close();
            
            if ("OK".equals(retMsg)) {
				strQuery.setLength(0);
				strQuery.append("insert into cmr0030                  \n");
			    strQuery.append("  (CR_DOCID,CR_DOCFILE,CR_STATUS,CR_CREATDT,CR_LASTDT,CR_CREATOR,CR_EDITOR,  \n");
			    strQuery.append("  CR_LSTVER,CR_PRJNO,CR_DIRPATH, CR_DOCSEQ,CR_DOCSTA) values				  \n");
			    strQuery.append("  (?, ?, '0', SYSDATE, SYSDATE, ?, ?, '1', ?, ?, ?, '0')     \n");		
				pstmt = conn.prepareStatement(strQuery.toString());		
				pstmt =  new LoggableStatement(conn, strQuery.toString());
				pstmt.setString(1, docFile.get("cr_docid"));
				pstmt.setString(2, docFile.get("cr_docfile"));
				pstmt.setString(3, docFile.get("cr_creator"));
				pstmt.setString(4, docFile.get("cr_editor"));
				pstmt.setString(5, docFile.get("cr_prjno"));
				pstmt.setString(6, docFile.get("cr_dirpath"));
				pstmt.setString(7, docFile.get("cr_docseq"));
		
				ecamsLogger.error(((LoggableStatement)pstmt).getQueryString());
	            rtn = pstmt.executeUpdate();
	            pstmt.close();
	            if(rtn <= 0)
	            	throw new Exception("데이터 저장 실패  [ 파일명 : " + docFile.get("cr_docfile")+"]" );
	            
	            strQuery.setLength(0);
				strQuery.append("insert into cmr0031                  \n");
			    strQuery.append("  (CR_PRJNO,CR_DOCID,CR_CREATDT,CR_LASTDT,CR_EDITOR,  \n");
			    strQuery.append("  CR_DOCSEQ) values				  \n");
			    strQuery.append("  (?, ?, SYSDATE, SYSDATE, ?, ?)     \n");		
				pstmt = conn.prepareStatement(strQuery.toString());		
				pstmt =  new LoggableStatement(conn, strQuery.toString());
				pstmt.setString(1, docFile.get("cr_prjno"));
				pstmt.setString(2, docFile.get("cr_docid"));
				pstmt.setString(3, docFile.get("cr_editor"));
				pstmt.setString(4, docFile.get("cr_docseq"));
				if(rtn <= 0)
	            	throw new Exception("데이터 저장 실패  [ 파일명 : " + docFile.get("cr_docfile")+"]" );
				ecamsLogger.error(((LoggableStatement)pstmt).getQueryString());
	            pstmt.executeUpdate();
	            pstmt.close();
            }
        	strQuery.setLength(0);
			strQuery.append("select count(*) cnt from cmr0040     						\n");
			strQuery.append(" where cr_docid = ? and cr_syscd = ? and cr_itemid = ?  	\n");
			pstmt = conn.prepareStatement(strQuery.toString());		
			pstmt =  new LoggableStatement(conn, strQuery.toString());
			pstmt.setString(1, docFile.get("cr_docid"));
			pstmt.setString(2, docFile.get("cr_syscd"));
			pstmt.setString(3, docFile.get("cr_itemid"));
			ecamsLogger.error(((LoggableStatement)pstmt).getQueryString());
            rs = pstmt.executeQuery();
            if (rs.next()) {
            	if (rs.getInt("cnt") > 0) retMsg = "기 등록된 일련번호입니다. 다시 전송하여 주시기 바랍니다.";
            }
            rs.close();
            pstmt.close();
            
            if ("OK".equals(retMsg)) {
            	strQuery.setLength(0);
				strQuery.append("insert into cmr0040                  									  \n");
			    strQuery.append("  (CR_PRJNO,CR_DOCID,CR_DOCFILE,CR_ITEMID,CR_SYSCD,CR_RSRCNAME,CR_CONN)  \n");
			    strQuery.append("  	values																  \n");
			    strQuery.append("  (?, ?, ?, ?, ?, ?, 'Y') 									   			  \n");		
				pstmt = conn.prepareStatement(strQuery.toString());		
				pstmt =  new LoggableStatement(conn, strQuery.toString());
				pstmt.setString(1, docFile.get("cr_prjno"));
				pstmt.setString(2, docFile.get("cr_docid"));
				pstmt.setString(3, docFile.get("cr_docfile"));
				pstmt.setString(4, docFile.get("cr_itemid"));
				pstmt.setString(5, docFile.get("cr_syscd"));
				pstmt.setString(6, docFile.get("cr_rsrcname"));
			
				ecamsLogger.error(((LoggableStatement)pstmt).getQueryString());
	            rtn = pstmt.executeUpdate();
	            pstmt.close();
	            if(rtn <= 0)
	            	throw new Exception("데이터 저장 실패  [ 파일명 : " + docFile.get("cr_docfile")+"]" );
	    
            }
            rs.close();
            pstmt.close();
            conn.close();
            
			rs = null;
			pstmt = null;
			conn = null;            
			
			return retMsg;
	
		} catch (SQLException sqlexception) {
			sqlexception.printStackTrace();
			ecamsLogger.error("## Cmc0010.setDocInfo() SQLException START ##");
			ecamsLogger.error("## Error DESC : ", sqlexception);	
			ecamsLogger.error("## Cmc0010.setDocInfo() SQLException END ##");			
			throw sqlexception;
		} catch (Exception exception) {
			exception.printStackTrace();
			ecamsLogger.error("## Cmc0010.setDocInfo() Exception START ##");				
			ecamsLogger.error("## Error DESC : ", exception);	
			ecamsLogger.error("## Cmc0010.setDocInfo() Exception END ##");				
			throw exception;
		}finally{
			if (strQuery != null) 	strQuery = null;
			if (rs != null)     try{rs.close();}catch (Exception ex){ex.printStackTrace();}
			if (pstmt != null)  try{pstmt.close();}catch (Exception ex2){ex2.printStackTrace();}
			if (conn != null){
				try{
					ConnectionResource.release(conn);
				}catch(Exception ex3){
					ecamsLogger.error("## Cmc0010.setDocInfo() connection release exception ##");
					ex3.printStackTrace();
				}
			}
		}
	}//end of setDocFile() method statement
	
	public String delFile(String Id,String ReqCd,String seqNo) throws SQLException, Exception {
		Connection        conn        = null;
		PreparedStatement pstmt       = null;
		ResultSet         rs          = null;
		StringBuffer      strQuery    = new StringBuffer();
		ConnectionContext connectionContext = new ConnectionResource();
		
		try {
			conn = connectionContext.getConnection();
			String retMsg = "OK";

			ecamsLogger.error("Cmc0010 delFile Start ~~~~~~~~~~~~~~~~~~~~");
			
			strQuery.setLength(0);
			strQuery.append("delete cmc1001                        \n");
			strQuery.append(" where cc_id=? 					   \n");
			strQuery.append("   and cc_reqcd=? and cc_seqno=?      \n");	
			pstmt = conn.prepareStatement(strQuery.toString());		
			pstmt =  new LoggableStatement(conn, strQuery.toString());
			pstmt.setString(1, Id);
			pstmt.setString(2, ReqCd);
			pstmt.setInt(3, Integer.parseInt(seqNo));	
			ecamsLogger.error(((LoggableStatement)pstmt).getQueryString());
            pstmt.executeUpdate();
            
			ecamsLogger.error("Cmc0010 delFile END ~~~~~~~~~~~~~~~~~~~~");
			
			pstmt.close();
			conn.close();
            pstmt = null;
            conn = null;
            
			return retMsg;
	
		} catch (SQLException sqlexception) {
			sqlexception.printStackTrace();
			ecamsLogger.error("## Cmc0010.delFile() SQLException START ##");
			ecamsLogger.error("## Error DESC : ", sqlexception);	
			ecamsLogger.error("## Cmc0010.delFile() SQLException END ##");			
			throw sqlexception;
		} catch (Exception exception) {
			exception.printStackTrace();
			ecamsLogger.error("## Cmc0010.delFile() Exception START ##");				
			ecamsLogger.error("## Error DESC : ", exception);	
			ecamsLogger.error("## Cmc0010.delFile() Exception END ##");				
			throw exception;
		}finally{
			if (strQuery != null) 	strQuery = null;
			if (rs != null)     try{rs.close();}catch (Exception ex){ex.printStackTrace();}
			if (pstmt != null)  try{pstmt.close();}catch (Exception ex2){ex2.printStackTrace();}
			if (conn != null){
				try{
					ConnectionResource.release(conn);
				}catch(Exception ex3){
					ecamsLogger.error("## Cmc0010.delFile() connection release exception ##");
					ex3.printStackTrace();
				}
			}
		}
	}//end of setDocFile() method statement
	
	public Object[] getDocList(String ReqId,String ReqCd,String SubReq) throws SQLException, Exception {
		Connection        conn        = null;
		PreparedStatement pstmt       = null;
		ResultSet         rs          = null;
		StringBuffer      strQuery    = new StringBuffer();
		ArrayList<HashMap<String, String>> rtList = new ArrayList<HashMap<String, String>>();
		HashMap<String, String>			   rst	  = null;
		
		ConnectionContext connectionContext = new ConnectionResource();
		int cnt = 0;
		try {
			
			conn = connectionContext.getConnection();
			
			strQuery.setLength(0);
			strQuery.append("select a.cc_seqno,a.cc_attfile,a.cc_savefile,    \n");
			strQuery.append("       to_char(a.cc_lastdt,'yyyy/mm/dd') lastdt, \n");
			strQuery.append("       a.cc_editor,b.cm_username,a.cc_subreq     \n");
			strQuery.append("  from cmm0040 b,cmc1001 a                       \n");
			strQuery.append(" where a.cc_reqid=? 					          \n");
			strQuery.append("   and a.cc_reqcd=?                              \n");
			if (SubReq != "" && SubReq != null) {
				if (!SubReq.equals("00")) 
					strQuery.append("and a.cc_subreq=?                        \n");
			}
			
			strQuery.append("   and a.cc_editor=b.cm_userid                   \n");
			strQuery.append(" order by a.cc_seqno                             \n");
			
	        pstmt = conn.prepareStatement(strQuery.toString());
	        pstmt = new LoggableStatement(conn, strQuery.toString());
	        pstmt.setString(++cnt, ReqId);
	        pstmt.setString(++cnt, ReqCd);
	        if (SubReq != "" && SubReq != null) {
				if (!SubReq.equals("00")) 
					pstmt.setString(++cnt, SubReq);
			}
	        
            ecamsLogger.error(((LoggableStatement)pstmt).getQueryString());
            rs = pstmt.executeQuery();
            
			while(rs.next()){
				rst = new HashMap<String, String>();
				rst.put("cc_seqno", rs.getString("cc_seqno"));
				rst.put("cc_attfile", rs.getString("cc_attfile"));
				rst.put("cc_savefile", rs.getString("cc_savefile"));
				rst.put("cc_editor", rs.getString("cc_editor"));
				rst.put("cc_subreq", rs.getString("cc_subreq"));
				rst.put("cm_username", rs.getString("cm_username"));
				rst.put("lastdt", rs.getString("lastdt"));
				rtList.add(rst);
				rst = null;
			}
			rs.close();
			pstmt.close();
			conn.close();
			
			rs = null;
			pstmt = null;
			conn = null;
			
			return rtList.toArray();
			
			
		} catch (SQLException sqlexception) {
			sqlexception.printStackTrace();
			ecamsLogger.error("## Cmc0010.getDocList() SQLException START ##");
			ecamsLogger.error("## Error DESC : ", sqlexception);	
			ecamsLogger.error("## Cmc0010.getDocList() SQLException END ##");			
			throw sqlexception;
		} catch (Exception exception) {
			exception.printStackTrace();
			ecamsLogger.error("## Cmc0010.getDocList() Exception START ##");				
			ecamsLogger.error("## Error DESC : ", exception);	
			ecamsLogger.error("## Cmc0010.getDocList() Exception END ##");				
			throw exception;
		}finally{
			if (strQuery != null) 	strQuery = null;
			if (rtList != null)		rtList = null;
			if (rs != null)     try{rs.close();}catch (Exception ex){ex.printStackTrace();}
			if (pstmt != null)  try{pstmt.close();}catch (Exception ex2){ex2.printStackTrace();}
			if (conn != null){
				try{
					ConnectionResource.release(conn);
				}catch(Exception ex3){
					ecamsLogger.error("## Cmc0010.getDocList() connection release exception ##");
					ex3.printStackTrace();
				}
			}
		}
	}//end of getDocList() method statement
}//end of Cmc0010 class statement