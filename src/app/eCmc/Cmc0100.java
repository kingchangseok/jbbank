/*****************************************************************************************
	1. program ID	: Cmc0100.java
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

import app.common.AutoSeq;
import app.common.LoggableStatement;
import org.apache.logging.log4j.Logger;
import com.ecams.common.dbconn.ConnectionContext;
import com.ecams.common.dbconn.ConnectionResource;
import com.ecams.common.logger.EcamsLogger;
import java.util.ArrayList;
import java.util.HashMap;
import app.common.*;
import app.eCmr.Cmr0200;

/**
 * @author bigeyes
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class Cmc0100{

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
		PreparedStatement pstmt2      = null;
		ResultSet         rs          = null;
		ResultSet         rs2         = null;
		StringBuffer      strQuery    = new StringBuffer();
		Object[] returnObjectArray    = null;
		ArrayList<HashMap<String, String>>  rsval = new ArrayList<HashMap<String, String>>();
		HashMap<String, String> rst	  = null;
		int              parmCnt      = 0;
		ConnectionContext connectionContext = new ConnectionResource();		
		
		try {
			
			conn = connectionContext.getConnection();
			
			strQuery.setLength(0);//ISERID : R + YYYYMM + '-' + SEQ(4) : R201011-0001
			strQuery.append("select a.CC_ISRID,                              \n");
			strQuery.append("       nvl(f.CC_ISRTITLE,a.CC_ISRTITLE) CC_ISRTITLE, \n");
			strQuery.append("       nvl(f.CC_SAYU,a.CC_SAYU) CC_SAYU,        \n");
			strQuery.append("       TO_CHAR(a.CC_CREATDT,'yyyy/mm/dd hh24:mi') CC_CREATDT, \n");
			strQuery.append("       nvl(f.CC_LASTDT,a.CC_LASTDT) CC_LASTDT,  \n");
			strQuery.append("       nvl(f.CC_REQENDDT,a.CC_REQENDDT) CC_REQENDDT,\n");
			strQuery.append("       nvl(f.CC_REQGRADE,a.CC_REQGRADE) CC_REQGRADE,\n");
			strQuery.append("       DECODE(nvl(f.CC_TESTERYN,a.CC_TESTERYN),'Y','참여','불참') as TESTERYN, \n");
			strQuery.append("       nvl(f.CC_TESTERYN,a.CC_TESTERYN) CC_TESTERYN,\n");
			strQuery.append("       nvl(f.CC_DOCNO,a.CC_DOCNO) CC_DOCNO,     \n");
			strQuery.append("       nvl(f.CC_BUDGETPRICE,nvl(a.CC_BUDGETPRICE,'0')) as BUDGETPRICE,  \n");
			strQuery.append("       nvl(f.CC_DETAILSAYU,a.CC_DETAILSAYU) CC_DETAILSAYU, \n");
			strQuery.append("       b.CM_USERNAME,c.CM_CODENAME REQGRADE,d.CM_DEPTCD,   \n");
			strQuery.append("       d.CM_DEPTNAME,nvl(f.cc_status,a.cc_status) cc_status, \n");
			strQuery.append("       ISRUPDT_CHK(a.CC_ISRID) UPDTOK ,         \n");
			strQuery.append("       ISR_RECVPART(a.CC_ISRID) RECVPART,       \n");
			strQuery.append("       a.cc_status status                       \n");			
			strQuery.append("  from cmc0100 a,cmm0040 b,cmm0020 c,cmm0100 d, \n");			
			strQuery.append("       (select cc_isrid,max(cc_seqno) cc_seqno  \n");		
			strQuery.append("          from cmc0101                          \n");	
			strQuery.append("         group by cc_isrid) e,cmc0101 f         \n");
			strQuery.append(" where a.cc_editor = ?                          \n");
			if (etcData.get("qrygbn").equals("00")) {
				strQuery.append("and to_char(a.cc_creatdt,'yyyymmdd')>=?     \n");
				strQuery.append("and to_char(a.cc_creatdt,'yyyymmdd')<=?     \n");
			} else if (etcData.get("qrygbn").equals("02")) {
				strQuery.append("and ISR_NOEND(a.CC_ISRID)='OK'              \n");
			} else {
				strQuery.append("and ISRUPDT_CHK(a.CC_ISRID) in ('OK1','OK') \n");
			}
			strQuery.append("   and a.cc_editor = b.cm_userid   \n");
			strQuery.append("   and c.cm_macode = 'REQGRADE'    \n");
			strQuery.append("   and nvl(f.cc_reqgrade,a.cc_reqgrade)=c.cm_micode \n");
			strQuery.append("   and a.cc_reqdept=d.cm_deptcd  \n");
			strQuery.append("   and a.cc_isrid=e.cc_isrid(+)  \n");
			strQuery.append("   and e.cc_isrid=f.cc_isrid(+)  \n");
			strQuery.append("   and e.cc_seqno=f.cc_seqno(+)  \n");
	        pstmt = conn.prepareStatement(strQuery.toString());
	        //pstmt = new LoggableStatement(conn,strQuery.toString());
	        pstmt.setString(++parmCnt, etcData.get("userid"));
	        if (etcData.get("qrygbn").equals("00")) {
	        	pstmt.setString(++parmCnt, etcData.get("stday"));
	        	pstmt.setString(++parmCnt, etcData.get("edday"));
	        }
	        //ecamsLogger.error(((LoggableStatement)pstmt).getQueryString());
	        rs = pstmt.executeQuery();
	        
	        while(rs.next()) {
		  		rst = new HashMap<String, String>();
				rst.put("CC_ISRID", rs.getString("CC_ISRID"));
				rst.put("CC_ISRTITLE", rs.getString("CC_ISRTITLE"));
				rst.put("CC_CREATDT", rs.getString("CC_CREATDT"));
				rst.put("CC_LASTDT", rs.getString("CC_LASTDT"));
				
				rst.put("CC_REQENDDT", "");
				if (rs.getString("CC_REQENDDT") != "" && rs.getString("CC_REQENDDT") != null){
					rst.put("CC_REQENDDT", rs.getString("CC_REQENDDT").substring(0,4)+"/"+rs.getString("CC_REQENDDT").substring(4,6)+"/"+rs.getString("CC_REQENDDT").substring(6));
				}
				rst.put("CC_REQENDDT1", "");
				rst.put("CC_REQGRADE", rs.getString("CC_REQGRADE"));
				rst.put("TESTERYN", rs.getString("TESTERYN"));
				rst.put("CC_TESTERYN", rs.getString("CC_TESTERYN"));
				rst.put("CC_DOCNO", rs.getString("CC_DOCNO"));
				rst.put("BUDGETPRICE", rs.getString("BUDGETPRICE"));
				rst.put("CC_DETAILSAYU", rs.getString("CC_DETAILSAYU"));
				rst.put("CM_USERNAME", rs.getString("CM_USERNAME"));
				rst.put("REQGRADE", rs.getString("REQGRADE"));
				rst.put("CM_DEPTNAME", rs.getString("CM_DEPTNAME"));
				rst.put("CC_RECVPART", rs.getString("RECVPART"));
				if (rs.getString("CC_SAYU") != "" && rs.getString("CC_SAYU") != null){
					rst.put("CC_SAYU", rs.getString("CC_SAYU"));
				}
				if (rs.getString("cc_status").equals("0")) rst.put("isrsta", "ISR저장");
				else if (rs.getString("cc_status").equals("1")) rst.put("isrsta", "ISR결재상신");
				else if (rs.getString("cc_status").equals("3")) rst.put("isrsta", "ISR결재반려");
				else if (rs.getString("cc_status").equals("5")) rst.put("isrsta", "ISR진행중");
				else {
					if (rs.getString("status").equals("9"))  rst.put("isrsta", "요청종료");
					else rst.put("isrsta", "ISR진행중");
				}
				// 수정가능한 ISR 인지 여부 체크
				rst.put("editISRYN", rs.getString("UPDTOK"));
				
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
			ecamsLogger.error("## Cmc0100.getISRList() SQLException START ##");
			ecamsLogger.error("## Error DESC : ", sqlexception);	
			ecamsLogger.error("## Cmc0100.getISRList() SQLException END ##");			
			throw sqlexception;
		} catch (Exception exception) {
			exception.printStackTrace();
			ecamsLogger.error("## Cmc0100.getISRList() Exception START ##");				
			ecamsLogger.error("## Error DESC : ", exception);	
			ecamsLogger.error("## Cmc0100.getISRList() Exception END ##");				
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
					ecamsLogger.error("## Cmc0100.getISRList() connection release exception ##");
					ex3.printStackTrace();
				}
			}
		}
	}//end of getISRList() method statement

	public Object[] getISRInfo_Main(String IsrId) throws SQLException, Exception {
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
			strQuery.append("select a.cc_isrid,a.cc_isrsub,b.cm_deptname, \n");
			strQuery.append("       c.cm_username,d.cm_codename,          \n");
			strQuery.append("       f.cm_codename grade,e.cc_testeryn,    \n");
			strQuery.append("       e.cc_docno,e.cc_budgetprice,          \n");
			strQuery.append("       e.cc_detailsayu                       \n");
			strQuery.append("  from cmm0020 f,cmc0100 e,cmm0020 d,cmm0040 c,cmm0100 b,cmc0110 a  \n");
			strQuery.append(" where e.cc_isrid=?                \n");
			strQuery.append("   and e.cc_isrid=a.cc_isrid       \n");
			strQuery.append("   and d.cm_macode='ISRSTASUB'     \n");
			strQuery.append("   and a.cc_substatus=d.cm_micode  \n");
			strQuery.append("   and f.cm_macode='REQGRADE'      \n");
			strQuery.append("   and e.cc_reqgrade=f.cm_micode   \n");
			strQuery.append("   and a.cc_recvpart=b.cm_deptcd   \n");
			strQuery.append("   and a.cc_recvuser=c.cm_userid(+)\n");
	        pstmt = conn.prepareStatement(strQuery.toString());
	 //       pstmt = new LoggableStatement(conn,strQuery.toString());
	        pstmt.setString(1, IsrId);
	  //      ecamsLogger.error(((LoggableStatement)pstmt).getQueryString());
	        rs = pstmt.executeQuery();
	        
	        String RECVPART = "";
	        while(rs.next()) {
		  		rst = new HashMap<String, String>();
				rst.put("cc_isrid", rs.getString("cc_isrid"));
				rst.put("cc_isrsub", rs.getString("cc_isrsub"));
				rst.put("recvpart", rs.getString("cm_deptname"));
				rst.put("recvuser", rs.getString("cm_username"));
				rst.put("substatus", rs.getString("cm_codename"));
				rst.put("cc_docno", rs.getString("cc_docno"));
				rst.put("cc_budgetprice", rs.getString("cc_budgetprice"));
				rst.put("cc_detailsayu", rs.getString("cc_detailsayu"));
				rst.put("cc_testeryn", rs.getString("cc_testeryn"));
				rst.put("grade", rs.getString("grade"));
				
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
			ecamsLogger.error("## Cmc0100.getISRList() SQLException START ##");
			ecamsLogger.error("## Error DESC : ", sqlexception);	
			ecamsLogger.error("## Cmc0100.getISRList() SQLException END ##");			
			throw sqlexception;
		} catch (Exception exception) {
			exception.printStackTrace();
			ecamsLogger.error("## Cmc0100.getISRList() Exception START ##");				
			ecamsLogger.error("## Error DESC : ", exception);	
			ecamsLogger.error("## Cmc0100.getISRList() Exception END ##");				
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
					ecamsLogger.error("## Cmc0100.getISRList() connection release exception ##");
					ex3.printStackTrace();
				}
			}
		}
	}//end of getISRList() method statement
	
	
	/**
	 * <PRE>
	 * 1. MethodName	: getISRInfo
	 * 2. ClassName		: Cmc0100
	 * 3. Commnet			: ISR 기본정보 조회
	 * 4. 작성자				: no name
	 * 5. 작성일				: 2010. 12. 16. 오전 9:22:42
	 * </PRE>
	 * 		@return Object[]
	 * 		@param strISRID
	 * 		@param strISRSUBID
	 * 		@param UserId
	 * 		@return
	 * 		@throws SQLException
	 * 		@throws Exception
	 */
	public Object[] getISRInfo(String strISRID,String strISRSUBID,String UserId) throws SQLException, Exception {
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
			
			strQuery.setLength(0);
			strQuery.append("select a.CC_ISRTITLE,a.CC_EDITOR,a.CC_REQENDDT,b.CM_USERNAME, \n");
			strQuery.append("       d.CM_DEPTNAME,e.CC_MAINSTATUS,e.CC_SUBSTATUS,nvl(a.CC_DOCNO,'') CC_DOCNO, \n");
			strQuery.append("       f.cm_codename STATUS,g.cm_codename SUBSTATUS, \n");
			strQuery.append("       e.CC_RECVUSER,e.CC_HANDLER,e.CC_RFCUSER,e.CC_CHGUSER, \n");
			strQuery.append("       TO_CHAR(e.CC_RFCDATE,'yyyy/mm/dd hh24:mi') CC_RFCDATE,\n");
			strQuery.append("       e.CC_ENDACPT,e.CC_REALMM,e.CC_REALEDDAY,e.CC_ENDGBN,  \n");
			strQuery.append("       ISRSTA_MAIN(e.cc_isrid,e.cc_isrsub,?) maintab,        \n"); 
			strQuery.append("       c.CM_USERNAME HANDLERNAME \n");
			strQuery.append("  from cmc0100 a,cmm0040 b,cmm0040 c,cmm0100 d,cmc0110 e,cmm0020 f,cmm0020 g \n");
			strQuery.append(" where e.cc_isrid = ?  \n");
			strQuery.append("   and e.cc_isrsub = ? \n");
			strQuery.append("   and e.cc_isrid = a.cc_isrid \n");
			strQuery.append("   and a.cc_editor = b.cm_userid \n");
			strQuery.append("   and e.cc_handler = c.cm_userid \n");
			strQuery.append("   and b.cm_project = d.cm_deptcd \n");
			strQuery.append("   and f.cm_macode='ISRSTAMAIN' \n");
			strQuery.append("   and f.cm_micode = e.cc_mainstatus \n");
			strQuery.append("   and g.cm_macode = 'ISRSTASUB' \n");
			strQuery.append("   and g.cm_micode = e.cc_substatus \n");
			
	        pstmt = conn.prepareStatement(strQuery.toString());
	 //       pstmt = new LoggableStatement(conn,strQuery.toString());
	        pstmt.setString(1, UserId);
	        pstmt.setString(2, strISRID);
	        pstmt.setString(3, strISRSUBID);
	  //      ecamsLogger.error(((LoggableStatement)pstmt).getQueryString());
	        rs = pstmt.executeQuery();
	        
	        while(rs.next()) {
		  		rst = new HashMap<String, String>();
		  		rst.put("ISRIDTITLE", "["+strISRID+"-"+strISRSUBID+"] "+rs.getString("CC_ISRTITLE"));//ISR-ID+SUBNO+TITLE
				rst.put("CC_ISRID", strISRID);
				rst.put("CC_ISRSUB", strISRSUBID);
				rst.put("CC_ISRTITLE", rs.getString("CC_ISRTITLE"));
				rst.put("CC_DOCNO", rs.getString("CC_DOCNO"));
				rst.put("CC_EDITOR", rs.getString("CC_EDITOR"));
				rst.put("CC_REQENDDT", "");//20101206
				if (rs.getString("CC_REQENDDT") != "" && rs.getString("CC_REQENDDT") != null){
					rst.put("CC_REQENDDT", rs.getString("CC_REQENDDT").substring(0,4)+"/"+rs.getString("CC_REQENDDT").substring(4,6)+"/"+rs.getString("CC_REQENDDT").substring(6));
				}
				rst.put("CM_USERNAME", rs.getString("CM_USERNAME"));
				rst.put("CM_DEPTNAME", rs.getString("CM_DEPTNAME"));
				rst.put("CC_MAINSTATUS", rs.getString("CC_MAINSTATUS"));
				rst.put("CC_SUBSTATUS", rs.getString("CC_SUBSTATUS"));
				rst.put("STATUS", rs.getString("STATUS"));
				rst.put("SUBSTATUS", rs.getString("SUBSTATUS"));
				rst.put("CC_RECVUSER", rs.getString("CC_RECVUSER"));
				rst.put("CC_HANDLER", rs.getString("CC_HANDLER"));
				rst.put("CC_RFCUSER", rs.getString("CC_RFCUSER"));
				rst.put("CC_CHGUSER", rs.getString("CC_CHGUSER"));
				
				rst.put("HANDLERNAME", rs.getString("HANDLERNAME"));
        		rst.put("CC_ENDACPT", rs.getString("CC_ENDACPT"));
        		if (rs.getBigDecimal("CC_REALMM") != null){
        			rst.put("CC_REALMM", rs.getBigDecimal("CC_REALMM").toString());
        		} else {
        			rst.put("CC_REALMM", "");
        		}
        		rst.put("maintab", rs.getString("maintab"));
				rst.put("CC_REALEDDAY", "");
				if (rs.getString("CC_REALEDDAY") != "" && rs.getString("CC_REALEDDAY") != null){
					rst.put("CC_REALEDDAY", rs.getString("CC_REALEDDAY").substring(0,4)+"/"+rs.getString("CC_REALEDDAY").substring(4,6)+"/"+rs.getString("CC_REALEDDAY").substring(6));
				}
        		rst.put("CC_ENDGBN", rs.getString("CC_ENDGBN"));
				//rst.put("STATUS", rs.getString("STATUS")+rs.getString("SUBSTATUS"));
				
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
			ecamsLogger.error("## Cmc0100.getISRInfo() SQLException START ##");
			ecamsLogger.error("## Error DESC : ", sqlexception);	
			ecamsLogger.error("## Cmc0100.getISRInfo() SQLException END ##");			
			throw sqlexception;
		} catch (Exception exception) {
			exception.printStackTrace();
			ecamsLogger.error("## Cmc0100.getISRInfo() Exception START ##");				
			ecamsLogger.error("## Error DESC : ", exception);	
			ecamsLogger.error("## Cmc0100.getISRInfo() Exception END ##");				
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
					ecamsLogger.error("## Cmc0100.getISRInfo() connection release exception ##");
					ex3.printStackTrace();
				}
			}
		}
	}//end of getISRInfo() method statement
	
	public Object[] getReqCnt(String strISRID) throws SQLException, Exception {
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
			int i = 0;
			strQuery.setLength(0);
			strQuery.append("select to_char(a.cc_creatdt,'yyyy/mm/dd hh24:mi') CC_CREATDT, \n");
			strQuery.append("       a.cc_acptno,a.cc_status,a.CC_ISRTITLE,a.CC_SAYU,        \n");
			strQuery.append("       a.CC_LASTDT,a.CC_REQENDDT,a.CC_REQGRADE, \n");
			strQuery.append("       DECODE(a.CC_TESTERYN,'Y','참여','불참') as TESTERYN, \n");
			strQuery.append("       a.CC_TESTERYN,a.CC_DOCNO,                \n");
			strQuery.append("       NVL(a.CC_BUDGETPRICE,0) as BUDGETPRICE,  \n");
			strQuery.append("       a.CC_DETAILSAYU,b.CM_USERNAME,           \n");
			strQuery.append("       c.CM_CODENAME REQGRADE,d.CM_DEPTCD,      \n");
			strQuery.append("       d.CM_DEPTNAME,a.cc_seqno,nvl(e.cnt,0) cnt   \n");
			strQuery.append("  from (select cr_acptno,count(*) cnt from cmr9900 \n");
			strQuery.append("         where cr_qrycd='31'     \n");
			strQuery.append("         group by cr_acptno) e,  \n");
			strQuery.append("       cmm0100 d,cmm0020 c,cmm0040 b,cmc0101 a  \n");
			strQuery.append(" where a.cc_isrid = ?            \n");
			strQuery.append("   and a.cc_editor=b.cm_userid   \n");
			strQuery.append("   and a.cc_editor=b.cm_userid   \n");
			strQuery.append("   and c.cm_macode = 'REQGRADE'  \n");
			strQuery.append("   and a.cc_reqgrade=c.cm_micode \n");
			strQuery.append("   and a.cc_reqdept=d.cm_deptcd  \n");
			strQuery.append("   and a.cc_acptno=e.cr_acptno(+)\n");
			strQuery.append(" order by a.cc_seqno desc        \n");
			
	        pstmt = conn.prepareStatement(strQuery.toString());
	        //pstmt = new LoggableStatement(conn,strQuery.toString());
	        pstmt.setString(1, strISRID);
	        //ecamsLogger.error(((LoggableStatement)pstmt).getQueryString());
	        rs = pstmt.executeQuery();
	        
	        while(rs.next()) {
		  		rst = new HashMap<String, String>();
		  		
		  		rst.put("reqcnt", rs.getString("cc_seqno")+"차등록");//ISR-ID+SUBNO+TITLE
		  		if (rs.getInt("cc_seqno")<10) rst.put("cnt", "0"+rs.getString("cc_seqno"));
		  		else rst.put("cnt", rs.getString("cc_seqno"));
				rst.put("cc_acptno", rs.getString("cc_acptno"));
				rst.put("cc_status", rs.getString("cc_status"));
				if (rs.getString("cc_status") != null && rs.getString("cc_status") != "") {
					if (rs.getString("cc_status").equals("1")) rst.put("reqsta", "결재요청");
					else if (rs.getString("cc_status").equals("3")) rst.put("reqsta", "반송처리");
					else if (rs.getString("cc_status").equals("9")) rst.put("reqsta", "결재완료");
				}	
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
				if (rs.getString("CC_SAYU") != "" && rs.getString("CC_SAYU") != null){
					rst.put("CC_SAYU", rs.getString("CC_SAYU"));
				}
				if (rs.getInt("cnt")>0) rst.put("confsw", "Y");
				else rst.put("confsw", "N");
    	        rsval.add(rst);
				rst = null;
	        }
	        rs.close();
	        pstmt.close();
	        
	        if (rsval.size() == 0) {
	        	strQuery.setLength(0);
				strQuery.append("select to_char(a.cc_creatdt,'yyyy/mm/dd hh24:mi') CC_CREATDT, \n");
				strQuery.append("       a.cc_status,a.CC_ISRTITLE,a.CC_SAYU,     \n");
				strQuery.append("       a.CC_LASTDT,a.CC_REQENDDT,a.CC_REQENDDT1,a.CC_REQGRADE,\n");
				strQuery.append("       DECODE(a.CC_TESTERYN,'Y','참여','불참') as TESTERYN, \n");
				strQuery.append("       a.CC_TESTERYN,a.CC_DOCNO,                \n");
				strQuery.append("       NVL(a.CC_BUDGETPRICE,0) as BUDGETPRICE,  \n");
				strQuery.append("       a.CC_DETAILSAYU,b.CM_USERNAME,           \n");
				strQuery.append("       c.CM_CODENAME REQGRADE,d.CM_DEPTCD,      \n");
				strQuery.append("       d.CM_DEPTNAME                            \n");
				strQuery.append("  from cmm0100 d,cmm0020 c,cmm0040 b,cmc0100 a  \n");
				strQuery.append(" where a.cc_isrid = ?            \n");
				strQuery.append("   and a.cc_editor=b.cm_userid   \n");
				strQuery.append("   and a.cc_editor=b.cm_userid   \n");
				strQuery.append("   and c.cm_macode = 'REQGRADE'  \n");
				strQuery.append("   and a.cc_reqgrade=c.cm_micode \n");
				strQuery.append("   and a.cc_reqdept=d.cm_deptcd  \n");
				
		        pstmt = conn.prepareStatement(strQuery.toString());
		        //pstmt = new LoggableStatement(conn,strQuery.toString());
		        pstmt.setString(1, strISRID);
		        //ecamsLogger.error(((LoggableStatement)pstmt).getQueryString());
		        rs = pstmt.executeQuery();
		        
		        while(rs.next()) {
			  		rst = new HashMap<String, String>();
			  		++i;
			  		
			  		rst.put("reqcnt", Integer.toString(i)+"차등록");//ISR-ID+SUBNO+TITLE
			  		rst.put("cnt", "01");
					rst.put("cc_status", "0");
					if (rs.getString("cc_status") != null && rs.getString("cc_status") != "") {
						if (rs.getString("cc_status").equals("1")) rst.put("reqsta", "결재요청");
						else if (rs.getString("cc_status").equals("3")) rst.put("reqsta", "반송처리");
						else if (rs.getString("cc_status").equals("9")) rst.put("reqsta", "결재완료");
					}	
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
					if (rs.getString("CC_SAYU") != "" && rs.getString("CC_SAYU") != null){
						rst.put("CC_SAYU", rs.getString("CC_SAYU"));
					}
	    	        rsval.add(rst);
					rst = null;
		        }
		        rs.close();
		        pstmt.close();
	        }
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
			ecamsLogger.error("## Cmc0100.getReqCnt() SQLException START ##");
			ecamsLogger.error("## Error DESC : ", sqlexception);	
			ecamsLogger.error("## Cmc0100.getReqCnt() SQLException END ##");			
			throw sqlexception;
		} catch (Exception exception) {
			exception.printStackTrace();
			ecamsLogger.error("## Cmc0100.getReqCnt() Exception START ##");				
			ecamsLogger.error("## Error DESC : ", exception);	
			ecamsLogger.error("## Cmc0100.getReqCnt() Exception END ##");				
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
					ecamsLogger.error("## Cmc0100.getReqCnt() connection release exception ##");
					ex3.printStackTrace();
				}
			}
		}
	}//end of getISRInfo() method statement
	
	public String getCnclCnt(String strISRID) throws SQLException, Exception {
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
			String rstval = "0";
			strQuery.setLength(0);
			strQuery.append(" SELECT COUNT(*) as cnt       	\n");
			strQuery.append("FROM CMC0110 A,CMC0100 B     	\n");   
			strQuery.append("WHERE A.CC_ISRID= ?    		\n");
			strQuery.append("AND A.CC_ISRID=B.CC_ISRID      \n");
			strQuery.append("AND A.CC_SUBSTATUS IN ('49','26','27','41','28','2A','29','18','1A','19','1B')        \n");
			
	        pstmt = conn.prepareStatement(strQuery.toString());
	        //pstmt = new LoggableStatement(conn,strQuery.toString());
	        pstmt.setString(1, strISRID);
	        //ecamsLogger.error(((LoggableStatement)pstmt).getQueryString());
	        rs = pstmt.executeQuery();
	        
	        if(rs.next()) {
		  		
				if (rs.getInt("cnt")>0) {
					rstval = "1";
				}else{
					rstval = "0";
				}
    	       
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

			return rstval;
	        
	        
		} catch (SQLException sqlexception) {
			sqlexception.printStackTrace();
			ecamsLogger.error("## Cmc0100.getReqCnt() SQLException START ##");
			ecamsLogger.error("## Error DESC : ", sqlexception);	
			ecamsLogger.error("## Cmc0100.getReqCnt() SQLException END ##");			
			throw sqlexception;
		} catch (Exception exception) {
			exception.printStackTrace();
			ecamsLogger.error("## Cmc0100.getReqCnt() Exception START ##");				
			ecamsLogger.error("## Error DESC : ", exception);	
			ecamsLogger.error("## Cmc0100.getReqCnt() Exception END ##");				
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
					ecamsLogger.error("## Cmc0100.getReqCnt() connection release exception ##");
					ex3.printStackTrace();
				}
			}
		}
	}//end of getISRInfo() method statement
	
	public String delISRInfo(HashMap<String,String> etcData) throws SQLException, Exception {
		Connection        conn        = null;
		PreparedStatement pstmt       = null;
		ResultSet         rs          = null;
		StringBuffer      strQuery    = new StringBuffer();
		String            IsrId      = "0";
		String            retMsg      = "0";
		
		ConnectionContext connectionContext = new ConnectionResource();		
		
		try {
			
			conn = connectionContext.getConnection();
			conn.setAutoCommit(false);
			
			strQuery.setLength(0);
			strQuery.append("select cc_isrid, cc_editor										\n");
			strQuery.append("  from cmc0100													\n");
			strQuery.append(" where cc_isrid = ? 											\n");
			strQuery.append("   and cc_editor = ?											\n");
	        pstmt = conn.prepareStatement(strQuery.toString());
	        //pstmt = new LoggableStatement(conn,strQuery.toString());
	        pstmt.setString(1, etcData.get("IsrId"));
	        pstmt.setString(2, etcData.get("UserId"));
	        //ecamsLogger.error(((LoggableStatement)pstmt).getQueryString());
	        rs = pstmt.executeQuery();
	        if(rs.next()) {
	        	IsrId = rs.getString("cc_isrid");
	        }else{
	        	retMsg = "9";
	        }
	        rs.close();
	        pstmt.close();
	        
	  		rs = null;
	  		pstmt = null;
	        
	    	strQuery.setLength(0);
	    	strQuery.append("delete cmc0110 where cc_isrid=? ");
    		pstmt = conn.prepareStatement(strQuery.toString());
    		pstmt.setString(1, IsrId);
    		pstmt.executeUpdate();
    		pstmt.close();
    		
    		strQuery.setLength(0);
	    	strQuery.append("delete cmc0100 												\n");
	    	strQuery.append(" where cc_isrid = ? 											\n");
			strQuery.append("   and cc_editor = ?											\n");
    		pstmt = conn.prepareStatement(strQuery.toString());
    		pstmt.setString(1, IsrId);
    		pstmt.setString(2, etcData.get("UserId"));
    		pstmt.executeUpdate();
    		pstmt.close();
    		
		    
	        conn.commit();
	        conn.close();
	        
	        pstmt = null;
	        conn = null;
	        
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
					ecamsLogger.error("## Cmc0100.setISRInfo() connection release exception ##");
					ex3.printStackTrace();
				}				
			}			
			ecamsLogger.error("## Cmc0100.setISRInfo() SQLException START ##");
			ecamsLogger.error("## Error DESC : ", sqlexception);	
			ecamsLogger.error("## Cmc0100.setISRInfo() SQLException END ##");			
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
					ecamsLogger.error("## Cmc0100.setISRInfo() connection release exception ##");
					ex3.printStackTrace();
				}				
			}			
			ecamsLogger.error("## Cmc0100.setISRInfo() Exception START ##");				
			ecamsLogger.error("## Error DESC : ", exception);	
			ecamsLogger.error("## Cmc0100.setISRInfo() Exception END ##");				
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
					ecamsLogger.error("## Cmc0100.setISRInfo() connection release exception ##");
					ex3.printStackTrace();
				}
			}
		}
	}//end of setISRInfo() method statement

	
	
	public String setISRInfo(HashMap<String,String> etcData,ArrayList<HashMap<String,String>> treeDept,ArrayList<HashMap<String,Object>> ConfList) throws SQLException, Exception {
		Connection        conn        = null;
		PreparedStatement pstmt       = null;
		ResultSet         rs          = null;
		StringBuffer      strQuery    = new StringBuffer();
		String            retMsg      = "0";
		String            AcptNo      = "";
		ConnectionContext connectionContext = new ConnectionResource();		
		
		try {
			
			conn = connectionContext.getConnection();
			conn.setAutoCommit(false);
			
			String strISRID = "";
			int pstmtcount = 0;
			String tmpStr = "";
			String strStatus = "0";
			
			if (etcData.get("chkNew").equals("true")){
				//신규등록일때
	        	strQuery.setLength(0);                		
        		strQuery.append("select lpad(to_number(max(substr(cc_isrid,9,11))) + 1,4,'0') as max,to_char(SYSDATE,'yyyymm') datYYMM \n");               		
        		strQuery.append("  from cmc0100 \n"); 
        		strQuery.append(" where substr(cc_isrid,2,6)=to_char(SYSDATE,'yyyymm') \n"); 
        		pstmt = conn.prepareStatement(strQuery.toString());	
    	        rs = pstmt.executeQuery();
    	        if (rs.next()) {
    	        	if (rs.getString("max") == null) {
    	        		strISRID = "R" + rs.getString("datYYMM") + "-0001";
    	        	} 
    	        	else {
    	        		strISRID = "R" + rs.getString("datYYMM") + "-" + rs.getString("max");
    	        	}
    	        }
    	        rs.close();
    	        pstmt.close();
    	        
			}else{
				//ISR수정일때
				strISRID = etcData.get("txtISRID");
			}
			
			
			if (strISRID == ""){
				//ISRID 값 없을때 9값 리턴
				retMsg = "9";
			}else{
				if (etcData.get("chkNew").equals("true") || etcData.get("gbncd").equals("S")) {
					strQuery.setLength(0);
					if (etcData.get("chkNew").equals("true")){
						//신규등록일때 16
		        		strQuery.append("insert into cmc0100 (CC_ISRTITLE,CC_EDITOR,CC_STATUS,CC_CREATDT, \n");
		        		strQuery.append("   CC_LASTDT,CC_REQENDDT,CC_REQENDDT1,CC_REQGRADE,CC_TESTERYN,CC_DOCNO,CC_DOCNO1, \n");
		        		strQuery.append("   CC_BUDGETPRICE,CC_DETAILSAYU,CC_REQDEPT,CC_SAYU,CC_ISRID) \n");
		        		strQuery.append("values (?,?,?,sysdate,  sysdate,?,?,?,?,?,?,  ?,?,?,?,?) \n");
					}else {//if (etcData.get("gbncd").equals("S")){
	            		strQuery.append("update cmc0100 set CC_ISRTITLE=?, \n");
	            		strQuery.append("                   CC_EDITOR=?,   \n");
	            		strQuery.append("                   CC_STATUS=?,   \n");
	            		strQuery.append("                   CC_LASTDT=sysdate, \n");
	            		strQuery.append("                   CC_REQENDDT=?, \n");
	            		if(etcData.get("editKind").equals("FOK")||etcData.get("editKind").equals("OK") || etcData.get("editKind").equals("OK1")){
	            			strQuery.append("                   CC_REQENDDT1=?, \n");
	            		}
	            		strQuery.append("                   CC_REQGRADE=?, \n");
	            		strQuery.append("                   CC_TESTERYN=?, \n");
	            		strQuery.append("                   CC_DOCNO=?, \n");
	            		if(etcData.get("editKind").equals("FOK")||etcData.get("editKind").equals("OK") || etcData.get("editKind").equals("OK1")){
	            			strQuery.append("                   CC_DOCNO1=?, \n");
	            		}
	            		strQuery.append("                   CC_BUDGETPRICE=?, \n");
	            		strQuery.append("                   CC_DETAILSAYU=?, \n");
	            		strQuery.append("                   CC_REQDEPT=?, \n");
	            		strQuery.append("                   CC_SAYU=? \n");
	            		strQuery.append(" where CC_ISRID=? \n");
					}
					
					pstmt = conn.prepareStatement(strQuery.toString());
					pstmt = new LoggableStatement(conn,strQuery.toString());
					pstmtcount = 1;
	            	pstmt.setString(pstmtcount++, etcData.get("txtTitle"));
	            	pstmt.setString(pstmtcount++, etcData.get("UserId"));
	            	if (etcData.get("gbncd").equals("S")) pstmt.setString(pstmtcount++, "0");
	            	else pstmt.setString(pstmtcount++, "1");
	            	pstmt.setString(pstmtcount++, etcData.get("dfCompleteDT"));
	            	if(etcData.get("chkNew").equals("true")){
	            		pstmt.setString(pstmtcount++, etcData.get("dfCompleteDT"));
	            	}else if(etcData.get("editKind").equals("FOK")||etcData.get("editKind").equals("OK") || etcData.get("editKind").equals("OK1")){
            			pstmt.setString(pstmtcount++, etcData.get("dfCompleteDT"));
	            	}
	            	pstmt.setString(pstmtcount++, etcData.get("cboReqGrade"));
	            	pstmt.setString(pstmtcount++, etcData.get("chkTesterYN"));
	            	pstmt.setString(pstmtcount++, etcData.get("txtDocNo"));
	            	if(etcData.get("chkNew").equals("true")){
	            		pstmt.setString(pstmtcount++, etcData.get("txtDocNo"));
	            	}else if(etcData.get("editKind").equals("FOK")||etcData.get("editKind").equals("OK") || etcData.get("editKind").equals("OK1")){
            			pstmt.setString(pstmtcount++, etcData.get("txtDocNo"));
	            	}
	            	pstmt.setString(pstmtcount++, etcData.get("txtPrice"));
	            	pstmt.setString(pstmtcount++, etcData.get("txtDetailSayu"));
	            	pstmt.setString(pstmtcount++, etcData.get("cm_deptcd"));
	            	pstmt.setString(pstmtcount++, etcData.get("Sayu"));
	            	pstmt.setString(pstmtcount++, strISRID);
	            	ecamsLogger.error(((LoggableStatement)pstmt).getQueryString());
	            	pstmt.executeUpdate();
	            	pstmt.close();
				} else {
					strQuery.setLength(0);
					strQuery.append("update cmc0100 set cc_status='1'      \n");
					strQuery.append(" where cc_isrid=?                     \n");
					pstmt = conn.prepareStatement(strQuery.toString());
					pstmt.setString(1, strISRID);
					pstmt.executeUpdate();
	            	pstmt.close();
				}
			}
			if (retMsg.equals("0")) {
				if(etcData.get("editKind").equals("HOK")||etcData.get("editKind").equals("FOK")){
					
				}else{
			    	strQuery.setLength(0);
			    	strQuery.append("select count(*) cnt from cmc0110 \n");
			    	strQuery.append(" where cc_isrid=?           \n");
			    	strQuery.append("   and cc_substatus='11'    \n");
		    		pstmt = conn.prepareStatement(strQuery.toString());
		    		pstmt.setString(1, strISRID);
		    		rs = pstmt.executeQuery();
		    		if (rs.next()) {
		    			if (rs.getInt("cnt") > 0) strStatus = "11";
		    			else strStatus = "10";		    				
		    		}
		    		pstmt.executeUpdate();
		    		pstmt.close();
		    		
			    	strQuery.setLength(0);
			    	strQuery.append("delete cmc0110 where cc_isrid=? ");
		    		pstmt = conn.prepareStatement(strQuery.toString());
		    		pstmt.setString(1, strISRID);
		    		pstmt.executeUpdate();
		    		pstmt.close();

		    		for (int Y=0 ; Y<treeDept.size() ; Y++){
		            	tmpStr = "";
		    	        
		            	strQuery.setLength(0);
		            	strQuery.append("insert into cmc0110 (CC_ISRID,CC_ISRSUB,CC_RECVPART,CC_RECVGBN, \n");
		        		strQuery.append("   CC_MAINSTATUS,CC_SUBSTATUS,CC_CREATDT,CC_LASTDT) \n");
		        		strQuery.append("values (?,?,?,'0',  '01',?,sysdate,sysdate) \n");
		            	pstmt = conn.prepareStatement(strQuery.toString());
		   //         	pstmt = new LoggableStatement(conn,strQuery.toString());
		            	tmpStr = Integer.toString(Y+1);
		            	if (tmpStr.length() < 2){
		            		tmpStr = "0" + tmpStr;
		            	}
						pstmtcount = 1;
		            	pstmt.setString(pstmtcount++, strISRID);
		            	pstmt.setString(pstmtcount++, tmpStr);
		            	pstmt.setString(pstmtcount++, treeDept.get(Y).get("cm_deptcd"));
		            	pstmt.setString(pstmtcount++, strStatus);
		    //        	ecamsLogger.error(((LoggableStatement)pstmt).getQueryString());
			    		pstmt.executeUpdate();
			    		pstmt.close();
			    		
			    		pstmt = null;
		            }
				}
			}
			
			if (retMsg.equals("0")) {
				int i = 0;
				if (etcData.get("gbncd").equals("R")) {
					//신청번호 받아오기
					AutoSeq autoseq = new AutoSeq();
		        	do {
				        AcptNo = autoseq.getSeqNo(conn,etcData.get("reqcd"));

				        i = 0;
				        strQuery.setLength(0);		        
				        strQuery.append("select count(*) as cnt from cmc0101 \n");
			        	strQuery.append(" where cc_acptno= ?                 \n");		        
			        	
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
		        	strQuery.setLength(0);
		        	strQuery.append("delete cmc0101 where cc_isrid=? and cc_acptno=?  \n");
		        	pstmt = conn.prepareStatement(strQuery.toString());
		        	pstmt.setString(1, strISRID);
		        	pstmt.setString(2, AcptNo);
		        	pstmt.executeUpdate();
		        	pstmt.close();
		        	
		        	strQuery.setLength(0);
		        	strQuery.append("insert into cmc0101                              \n");
		        	strQuery.append("    (CC_ISRID,CC_SEQNO,CC_ACPTNO,CC_ISRTITLE,CC_EDITOR,CC_STATUS, \n");
		        	strQuery.append("     CC_CREATDT,CC_LASTDT,CC_REQENDDT,CC_REQGRADE,CC_TESTERYN, \n");
		        	strQuery.append("     CC_DOCNO,CC_BUDGETPRICE,CC_DETAILSAYU,CC_REQDEPT,CC_SAYU) \n");
		        	strQuery.append("(select ?, NVL(MAX(CC_SEQNO),0)+1,?,?,?,'1', \n");
		        	strQuery.append("        SYSDATE,SYSDATE,?,?,?,               \n");
		        	strQuery.append("        ?, ?, ?, ?, ?                        \n");
		        	strQuery.append("   from cmc0101      \n");
		        	strQuery.append("  where cc_isrid=?)  \n");
		        	pstmt = conn.prepareStatement(strQuery.toString());
		        	pstmtcount = 1;
		        	pstmt.setString(pstmtcount++, strISRID);
		        	pstmt.setString(pstmtcount++, AcptNo);
	            	pstmt.setString(pstmtcount++, etcData.get("txtTitle"));
	            	pstmt.setString(pstmtcount++, etcData.get("UserId"));
	            	
	            	pstmt.setString(pstmtcount++, etcData.get("dfCompleteDT"));
	            	pstmt.setString(pstmtcount++, etcData.get("cboReqGrade"));
	            	pstmt.setString(pstmtcount++, etcData.get("chkTesterYN"));
	            	
	            	pstmt.setString(pstmtcount++, etcData.get("txtDocNo"));
	            	pstmt.setString(pstmtcount++, etcData.get("txtPrice"));
	            	pstmt.setString(pstmtcount++, etcData.get("txtDetailSayu"));
	            	pstmt.setString(pstmtcount++, etcData.get("cm_deptcd"));
	            	pstmt.setString(pstmtcount++, etcData.get("Sayu"));
	            	pstmt.setString(pstmtcount++, strISRID);
	            	
		        	pstmt.executeUpdate();		        	
		        	pstmt.close();
		        	
		    		Cmc0010 cmc0010 = new Cmc0010();
		    		retMsg = cmc0010.base_Confirm(AcptNo,strISRID,"01",etcData.get("UserId"),etcData.get("reqcd"),conn);
		    		if (!retMsg.equals("0")) {
						conn.rollback();
						conn.close();
						throw new Exception("ISR승인요청기본 정보 등록 중 오류가 발생하였습니다. 관리자에게 연락하여 주시기 바랍니다.");
		    		}
		    		
		    		if (ConfList != null) {
			    		Cmr0200 cmr0200 = new Cmr0200();
						retMsg = cmr0200.request_Confirm(AcptNo,"99999",etcData.get("reqcd"),etcData.get("UserId"),true,ConfList,conn);
						if (!retMsg.equals("OK")) {
							conn.rollback();
							conn.close();
							 return "결재정보등록 중 오류가 발생하였습니다. 관리자에게 연락하여 주시기 바랍니다.";
						}
		    		} else {
		    			strQuery.setLength(0);
		    			strQuery.append("update cmc0300 set cc_status='9',  \n");
		    			strQuery.append("  cc_eddate=SYSDATE                \n");
		    			strQuery.append(" where cc_acptno=?                 \n");
		    			pstmt = conn.prepareStatement(strQuery.toString());
			        	pstmt.setString(1, AcptNo);
			        	pstmt.executeUpdate();		
			        	pstmt.close();
		    		}
				}
				conn.commit();
				retMsg = strISRID;
			} else {
				conn.rollback();
			}
	        
	        conn.close();
	        
	        rs = null;
	        pstmt = null;
	        conn = null;
	        
	        
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
					ecamsLogger.error("## Cmc0100.setISRInfo() connection release exception ##");
					ex3.printStackTrace();
				}
			}
			ecamsLogger.error("## Cmc0100.setISRInfo() SQLException START ##");
			ecamsLogger.error("## Error DESC : ", sqlexception);	
			ecamsLogger.error("## Cmc0100.setISRInfo() SQLException END ##");			
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
					ecamsLogger.error("## Cmc0100.setISRInfo() connection release exception ##");
					ex3.printStackTrace();
				}				
			}			
			ecamsLogger.error("## Cmc0100.setISRInfo() Exception START ##");				
			ecamsLogger.error("## Error DESC : ", exception);	
			ecamsLogger.error("## Cmc0100.setISRInfo() Exception END ##");				
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
					ecamsLogger.error("## Cmc0100.setISRInfo() connection release exception ##");
					ex3.printStackTrace();
				}
			}
		}
	}//end of setISRInfo() method statement

}//end of Cmc0100 class statement

