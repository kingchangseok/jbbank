/*****************************************************************************************
	1. program ID	: Cmr0250.java
	2. create date	: 2008.08.10
	3. auth		    : is.choi
	4. update date	: 2009.07.10
	5. auth		    : no name
	6. description	: 서비스변경요청상세화면
*****************************************************************************************/

package app.eCmr;

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

/**
 * @author bigeyes
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class Cmr0750{    
	
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
	public Object[] getReqList(String UserId,String AcptNo) throws SQLException, Exception{
		Connection        conn        = null;
		PreparedStatement pstmt       = null;
		ResultSet         rs          = null;
		StringBuffer      strQuery    = new StringBuffer();
		ArrayList<HashMap<String, String>>  rsval  = new ArrayList<HashMap<String, String>>();
		ArrayList<HashMap<String, String>>  rsconf = new ArrayList<HashMap<String, String>>();
		HashMap<String, String>			  rst		  = null;
		ConnectionContext connectionContext = new ConnectionResource();	
		String           strPrcSw       = "N";
		Object[] returnObjectArray		 = null;
		
		try {
			conn = connectionContext.getConnection();
			
			strQuery.append("select to_char(a.cr_acptdate,'yyyy/mm/dd hh24:mi') acptdate,        \n");			
			strQuery.append("       to_char(a.cr_prcdate,'yyyy/mm/dd hh24:mi') prcdate,          \n");				
			strQuery.append("       a.cr_acptno,a.cr_syscd,a.cr_editor,a.cr_sayu,a.cr_status,    \n");		
			strQuery.append("       a.cr_qrycd,a.cr_passok,a.cr_noauto,b.cm_sysmsg,b.cm_sysinfo, \n");		
			strQuery.append("       d.cm_username                                                \n");			
			strQuery.append("  from cmm0040 d,cmm0030 b,cmr1000 a                                \n"); 
			strQuery.append(" where a.cr_acptno=?                                                \n");
			strQuery.append("   and a.cr_syscd=b.cm_syscd and a.cr_editor=d.cm_userid            \n");
			pstmt = conn.prepareStatement(strQuery.toString());	
			//pstmt = new LoggableStatement(conn,strQuery.toString());
			pstmt.setString(1,AcptNo);
	       ////ecamsLogger.error(((LoggableStatement)pstmt).getQueryString());           
	        rs = pstmt.executeQuery();
	        
	        rsval.clear();
	        
			if (rs.next()){			
				rst = new HashMap<String, String>();
				//rst.put("ID", Integer.toString(rs.getRow()));
				rst.put("cr_acptno",rs.getString("cr_acptno"));
				rst.put("acptdate",rs.getString("acptdate"));
				if (rs.getString("prcdate") != null) rst.put("prcdate",rs.getString("prcdate"));
				rst.put("cm_sysmsg",rs.getString("cm_sysmsg"));
				rst.put("cm_username",rs.getString("cm_username"));
				rst.put("cr_syscd",rs.getString("cr_syscd"));
				rst.put("cr_status",rs.getString("cr_status"));
				rst.put("cr_editor",rs.getString("cr_editor"));
				rst.put("cr_passok",rs.getString("cr_passok"));
				rst.put("cr_qrycd",rs.getString("cr_qrycd"));
				if (rs.getString("cr_noauto").equals("0")) 
					rst.put("gubun","운영");
				else rst.put("gubun","테스트");
				
				if (rs.getString("cr_sayu") != null)
				   rst.put("cr_sayu",rs.getString("cr_sayu"));
			
				if (rs.getString("cr_status").equals("9") || rs.getString("cr_status").equals("8")) {
					rst.put("confname","처리완료");
					rst.put("prcsw", "1");
					rst.put("endsw", "1");
					rst.put("updtsw3", "0");
					rst.put("log", "1");
				} else if (rs.getString("cr_status").equals("3")) {
					rst.put("confname","반려");	
					rst.put("prcsw", "1");
					rst.put("endsw", "1");
					rst.put("updtsw3", "0");
					rst.put("log", "1");
				} else {
					if (rs.getString("prcdate") != null) {
						rst.put("prcsw", "1");
						strPrcSw = "1";
					} else {
						rst.put("prcsw", "0");
						strPrcSw = "0";
					}
					rst.put("endsw", "0");
					
					//rsconf = confLocat(AcptNo,strPrcSw);
					rsconf = confLocat_conn(AcptNo,strPrcSw,conn);
					
					rst.put("signteam", rsconf.get(0).get("signteam"));
					rst.put("signteamcd", rsconf.get(0).get("signteamcd"));
					rst.put("confusr", rsconf.get(0).get("confusr"));
					rst.put("cr_prcsw", rsconf.get(0).get("prcsw"));
					rst.put("confname", rsconf.get(0).get("confname"));
					rst.put("updtsw3", rsconf.get(0).get("updtsw3"));
					rst.put("errtry", rsconf.get(0).get("errtry"));
					rst.put("log", rsconf.get(0).get("log"));
					rsconf = null;
				}
				
				rsval.add(rst);
				rst = null;
			}//end of while-loop statement
			rs.close();
			pstmt.close();
			conn.close();
			conn = null;
			pstmt = null;
			rs = null;
			
			returnObjectArray = rsval.toArray();
			rsval.clear();
			rsval = null;
			//ecamsLogger.debug(rsval.toString());
			
			return returnObjectArray;			
			
		} catch (SQLException sqlexception) {
			sqlexception.printStackTrace();
			ecamsLogger.error("## Cmr0750.getReqList() SQLException START ##");
			ecamsLogger.error("## Error DESC : ", sqlexception);	
			ecamsLogger.error("## Cmr0750.getReqList() SQLException END ##");			
			throw sqlexception;
		} catch (Exception exception) {
			exception.printStackTrace();
			ecamsLogger.error("## Cmr0750.getReqList() Exception START ##");				
			ecamsLogger.error("## Error DESC : ", exception);	
			ecamsLogger.error("## Cmr0750.getReqList() Exception END ##");				
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
					ecamsLogger.error("## Cmr0750.getReqList() connection release exception ##");
					ex3.printStackTrace();
				}
			}
		}
	}//end of getReqList() method statement
	
	
	public Object[] getProgList(String UserId,String AcptNo,String chkYn,String UpdtSw) throws SQLException, Exception
	{
		Connection        conn        = null;
		PreparedStatement pstmt       = null;
		ResultSet         rs          = null;
		StringBuffer      strQuery    = new StringBuffer();
		ArrayList<HashMap<String, String>>  rsval = new ArrayList<HashMap<String, String>>();
		HashMap<String, String>			  rst		  = null;
		ConnectionContext connectionContext = new ConnectionResource();		
		Object[] returnObjectArray = null;
		
		try {
			conn = connectionContext.getConnection();	
			strQuery.append("select a.cr_svrname,a.cr_service,a.cr_xayn,a.cr_timeout,a.cr_gbncd,     \n");
			strQuery.append("       a.cr_proccd,a.cr_status,a.cr_putcode,a.cr_itemid,a.cr_prcdate,   \n");
			strQuery.append("       a.cr_serno,a.cr_etc                                              \n");
			strQuery.append("  from cmr1200 a                                                        \n"); 
			strQuery.append(" where a.cr_acptno=?                                                    \n"); 
			strQuery.append(" order by a.cr_serno                                                    \n");
	        pstmt = conn.prepareStatement(strQuery.toString());	
			//pstmt = new LoggableStatement(conn,strQuery.toString());
			pstmt.setString(1, AcptNo);
	        ////ecamsLogger.error(((LoggableStatement)pstmt).getQueryString());           
	        rs = pstmt.executeQuery();
	        
	        rsval.clear();
	        
			while (rs.next()){
				rst = new HashMap<String, String>();
				//rst.put("ID", Integer.toString(rs.getRow()));
				rst.put("cr_svrname",rs.getString("cr_svrname"));
				rst.put("cr_service",rs.getString("cr_service"));
				rst.put("cr_xayn",rs.getString("cr_xayn"));
				rst.put("cr_timeout",rs.getString("cr_timeout"));
				rst.put("cr_proccd",rs.getString("cr_proccd"));
				rst.put("cr_gbncd",rs.getString("cr_gbncd"));
				if (rs.getString("cr_xayn").equals("0")){
					rst.put("xa", "XA");
				}
				else{
					rst.put("xa", "NX"); 
				}
				if (rs.getString("cr_gbncd").equals("0")){
					rst.put("gbn", "등록");
				}
				else{
					rst.put("gbn", "삭제"); 
				}
				if (rs.getString("cr_proccd").equals("0")){
					rst.put("pass", "정기");
				}
				else{
					rst.put("pass", "긴급");
				}
				if (rs.getString("cr_etc") != null){
					rst.put("etc", rs.getString("cr_etc"));						
				}
				if (rs.getString("cr_prcdate") != null){
					rst.put("cr_prcdate", rs.getString("cr_prcdate"));						
				}
				rst.put("cr_serno",Integer.toString(rs.getInt("cr_serno")));
				
				if (rs.getString("cr_putcode") != null){
					rst.put("cr_putcode",rs.getString("cr_putcode"));
				}
				if (rs.getString("cr_status").equals("0")) {
					if (rs.getString("cr_putcode") != null && !rs.getString("cr_putcode").equals("0000")){
						rst.put("ColorSw","5");
					}
					else{
						rst.put("ColorSw","0");
					}
				} 
				else if (rs.getString("cr_status").equals("3")){
					rst.put("ColorSw","3"); 
				}
				else if (rs.getString("cr_status").equals("9")){
					rst.put("ColorSw","9");
				}
				else if (rs.getString("cr_prcdate") != null){
					rst.put("ColorSw","9");
				}
				rsval.add(rst);
				rst = null;
			}//end of while-loop statement
			rs.close();
			pstmt.close();
			conn.close();
			conn = null;
			pstmt = null;
			rs = null;
			
			returnObjectArray = rsval.toArray();
			rsval.clear();
			rsval = null;
			
			return returnObjectArray;			
			
		} catch (SQLException sqlexception) {
			sqlexception.printStackTrace();
			ecamsLogger.error("## Cmr0750.getProgList() SQLException START ##");
			ecamsLogger.error("## Error DESC : ", sqlexception);	
			ecamsLogger.error("## Cmr0750.getProgList() SQLException END ##");			
			throw sqlexception;
		} catch (Exception exception) {
			exception.printStackTrace();
			ecamsLogger.error("## Cmr0750.getProgList() Exception START ##");				
			ecamsLogger.error("## Error DESC : ", exception);	
			ecamsLogger.error("## Cmr0750.getProgList() Exception END ##");				
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
					ecamsLogger.error("## Cmr0750.getProgList() connection release exception ##");
					ex3.printStackTrace();
				}
			}
		}
	}//end of getProgList() method statement
	
	
	public Object[] getRstList(String UserId,String AcptNo) throws SQLException, Exception{
		Connection        conn        = null;
		PreparedStatement pstmt       = null;
		PreparedStatement pstmt2       = null;
		ResultSet         rs          = null;
		ResultSet         rs2         = null;
		StringBuffer      strQuery    = new StringBuffer();
		int               parmCnt     = 0;
		Object[] returnObjectArray = null;
		ArrayList<HashMap<String, String>>  rsval = new ArrayList<HashMap<String, String>>();
		HashMap<String, String>			  rst		  = null;
		ConnectionContext connectionContext = new ConnectionResource();		
		
		try {
			conn = connectionContext.getConnection();
			strQuery.append("select b.cr_svrname,b.cr_ipaddr,b.cr_svrcd,b.cr_seqno,b.cr_portno,      \n");
			strQuery.append("       to_char(b.cr_prcdate,'yyyy/mm/dd hh24:mi') prcdate,              \n");	
			strQuery.append("       b.cr_prcrst,b.cr_prcdate,b.cr_prcsys,b.cr_putcode,               \n");
			strQuery.append("       b.cr_rstfile,b.cr_wait,b.cr_serno,e.cm_codename sysgbn           \n");
			strQuery.append("  from cmm0020 e,cmr1011 b                                              \n"); 
			strQuery.append(" where b.cr_acptno=?                                                    \n"); 
			//strQuery.append("   and (nvl(b.cr_putcode,'NONE')<>'0000' or b.cr_rstfile is not null)   \n");
			strQuery.append("   and e.cm_macode='SYSGBN' and e.cm_micode=b.cr_prcsys                 \n");
			strQuery.append(" order by b.cr_seqno                                                    \n");
	        pstmt = conn.prepareStatement(strQuery.toString());	
			//pstmt = new LoggableStatement(conn,strQuery.toString());
			pstmt.setString(++parmCnt, AcptNo);
	        ////ecamsLogger.error(((LoggableStatement)pstmt).getQueryString());           
	        rs = pstmt.executeQuery();

	        rsval.clear();
	        
			while (rs.next()){
				rst = new HashMap<String, String>();
				rst.put("ID", Integer.toString(rs.getRow()));
				rst.put("prcsys",rs.getString("sysgbn"));	
				rst.put("cr_seqno",rs.getString("cr_seqno"));
				rst.put("cr_svrname",rs.getString("cr_svrname"));
				if (rs.getInt("cr_serno")>0) {
					strQuery.setLength(0);
					strQuery.append("select cr_svrname from cmr1200       \n");
					strQuery.append(" where cr_acptno=? and cr_serno=?    \n");
					pstmt2 = conn.prepareStatement(strQuery.toString());
					pstmt2.setString(1,AcptNo);
					pstmt2.setInt(2,rs.getInt("cr_serno"));
					rs2 = pstmt2.executeQuery();
					if (rs2.next()){
						rst.put("svrname", rs2.getString("cr_svrname"));
					}
					rs2.close();
					pstmt2.close();
				}
				if (rs.getString("cr_prcrst") != null) {
					strQuery.setLength(0);
					strQuery.append("select cm_codename from cmm0020             \n");
					strQuery.append(" where cm_macode='ERRACCT' and cm_micode=?  \n");
					pstmt2 = conn.prepareStatement(strQuery.toString());
					pstmt2.setString(1,rs.getString("cr_prcrst"));
					rs2 = pstmt2.executeQuery();
					if (rs2.next()){
						rst.put("prcrst", rs2.getString("cm_codename"));
					}
					else{
						rst.put("prcrst", rs.getString("cr_prcrst"));
					}
					rs2.close();
					pstmt2.close();
				}
				if (rs.getString("cr_svrname") != null){
					rst.put("cr_svrname", rs.getString("cr_svrname"));		
				}
				if (rs.getString("cr_prcdate") != null){
					rst.put("cr_prcdate", rs.getString("cr_prcdate"));						
				}
				if (rs.getString("prcdate") != null){
					rst.put("prcdate", rs.getString("prcdate"));						
				}
				if (rs.getString("cr_putcode") != null){
					rst.put("cr_putcode",rs.getString("cr_putcode"));
				}
				if (rs.getString("cr_prcdate") != null) {
					if (rs.getString("cr_prcrst") != null && !rs.getString("cr_prcrst").equals("0000")){
						rst.put("ColorSw","5");
					}
					else{
						rst.put("ColorSw","9");
					}
				} 
				else{
					rst.put("ColorSw","0"); 
				}
						        
		        rsval.add(rst);
		        rst = null;
			}//end of while-loop statement
			rs.close();
			pstmt.close();
			conn.close();
			conn = null;
			pstmt = null;
			rs = null;
			
			returnObjectArray = rsval.toArray();
			rsval.clear();
			rsval = null;
			
			
			return returnObjectArray;			
			
			
		} catch (SQLException sqlexception) {
			sqlexception.printStackTrace();
			ecamsLogger.error("## Cmr0750.getRstList() SQLException START ##");
			ecamsLogger.error("## Error DESC : ", sqlexception);	
			ecamsLogger.error("## Cmr0750.getRstList() SQLException END ##");			
			throw sqlexception;
		} catch (Exception exception) {
			exception.printStackTrace();
			ecamsLogger.error("## Cmr0750.getRstList() Exception START ##");				
			ecamsLogger.error("## Error DESC : ", exception);	
			ecamsLogger.error("## Cmr0750.getRstList() Exception END ##");				
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
					ecamsLogger.error("## Cmr0750.getRstList() connection release exception ##");
					ex3.printStackTrace();
				}
			}
		}
	}//end of getFileList() method statement
	
	
	public String svrProc(String AcptNo,String prcCd,String prcSys) throws SQLException, Exception {
		Connection        conn        = null;
		PreparedStatement pstmt       = null;
		ResultSet         rs          = null;
		StringBuffer      strQuery    = new StringBuffer();
		String            strErMsg    = "";
		ConnectionContext connectionContext = new ConnectionResource();		
		
		try {
			conn = connectionContext.getConnection();
			conn.setAutoCommit(false);				
			strQuery.setLength(0);
			strQuery.append("delete cmr1011                                          \n");
			strQuery.append(" where cr_acptno=?                                      \n"); 
			strQuery.append("   and cr_prcsys=?                                      \n"); 
	        pstmt = conn.prepareStatement(strQuery.toString());	
			//pstmt = new LoggableStatement(conn,strQuery.toString());
			pstmt.setString(1, AcptNo);
			pstmt.setString(2,prcSys);
	        ////ecamsLogger.error(((LoggableStatement)pstmt).getQueryString());           
	        pstmt.executeUpdate();
	        pstmt.close();

			strQuery.setLength(0);
			strQuery.append("update cmr1200                                         \n");
			strQuery.append("   set cr_putcode='',cr_sysstep=0,cr_pid=''            \n"); 
			strQuery.append(" where cr_acptno=? and cr_prcdate is null              \n"); 
			strQuery.append("   and cr_status='0'                                   \n"); 
	        pstmt = conn.prepareStatement(strQuery.toString());	
			//pstmt = new LoggableStatement(conn,strQuery.toString());
			pstmt.setString(1,AcptNo);
	        ////ecamsLogger.error(((LoggableStatement)pstmt).getQueryString());           
	        pstmt.executeUpdate(); 	        
	        pstmt.close();
	        
	        
			strQuery.setLength(0);
			strQuery.append("update cmr1000 set cr_notify='0'                            \n"); 
			strQuery.append(" where cr_acptno=?                                          \n"); 
	        pstmt = conn.prepareStatement(strQuery.toString());	
			//pstmt = new LoggableStatement(conn,strQuery.toString());
			pstmt.setString(1,AcptNo);
	        ////ecamsLogger.error(((LoggableStatement)pstmt).getQueryString());           
	        pstmt.executeUpdate();
	        pstmt.close();
	        	        
	        conn.commit();	        
	        conn.close();
			conn = null;
			pstmt = null;
			rs = null;
	        
	        strErMsg = "0";
	        
	        return strErMsg;

		} catch (SQLException sqlexception) {
			sqlexception.printStackTrace();
			if (rs != null)     try{rs.close();}catch (Exception ex){ex.printStackTrace();}
			if (pstmt != null)  try{pstmt.close();}catch (Exception ex2){ex2.printStackTrace();}
			if (conn != null){
				try{
					conn.rollback();
					ConnectionResource.release(conn);
				}catch(Exception ex3){
					ecamsLogger.error("## Cmr0750.svrProc() connection release exception ##");
					ex3.printStackTrace();
				}				
			}
			ecamsLogger.error("## Cmr0750.svrProc() SQLException START ##");
			ecamsLogger.error("## Error DESC : ", sqlexception);	
			ecamsLogger.error("## Cmr0750.svrProc() SQLException END ##");			
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
					ecamsLogger.error("## Cmr0200.svrProc() connection release exception ##");
					ex3.printStackTrace();
				}				
			}			
			ecamsLogger.error("## Cmr0750.svrProc() Exception START ##");				
			ecamsLogger.error("## Error DESC : ", exception);	
			ecamsLogger.error("## Cmr0750.putDeploy() Exception END ##");				
			throw exception;
		}finally{
			if (strQuery != null) 	strQuery = null;
			if (rs != null)     try{rs.close();}catch (Exception ex){ex.printStackTrace();}
			if (pstmt != null)  try{pstmt.close();}catch (Exception ex2){ex2.printStackTrace();}
			if (conn != null){
				try{
					ConnectionResource.release(conn);
				}catch(Exception ex3){
					ecamsLogger.error("## Cmr0750.svrProc() connection release exception ##");
					ex3.printStackTrace();
				}
			}
		}
	}//end of svrProc() method statement
	
	
	public String progCncl(String AcptNo,String SerNo) throws SQLException, Exception {
		Connection        conn        = null;
		PreparedStatement pstmt       = null;
		StringBuffer      strQuery    = new StringBuffer();
		
		ConnectionContext connectionContext = new ConnectionResource();		
		
		try {
			conn = connectionContext.getConnection();
			conn.setAutoCommit(false);
            
            strQuery.setLength(0);
        	strQuery.append("update cmr1010 set cr_status='3',cr_prcdate=SYSDATE \n");
        	strQuery.append(" where cr_acptno=? and cr_serno=?                   \n");
            pstmt = conn.prepareStatement(strQuery.toString());
            pstmt.setString(1,AcptNo);
            pstmt.setInt(2,Integer.parseInt(SerNo));
            pstmt.executeUpdate();
            pstmt.close();
            
            conn.commit();
            conn.close();
			conn = null;
			pstmt = null;
            return "0";
            
		} catch (SQLException sqlexception) {
			sqlexception.printStackTrace();
			if (pstmt != null)  try{pstmt.close();}catch (Exception ex2){ex2.printStackTrace();}
			if (conn != null){
				try{
					conn.rollback();
					ConnectionResource.release(conn);
				}catch(Exception ex3){
					ecamsLogger.error("## Cmr0750.progCncl() connection release exception ##");
					ex3.printStackTrace();
				}				
			}
			ecamsLogger.error("## Cmr0750.progCncl() SQLException START ##");
			ecamsLogger.error("## Error DESC : ", sqlexception);	
			ecamsLogger.error("## Cmr0750.progCncl() SQLException END ##");			
			throw sqlexception;
		} catch (Exception exception) {
			exception.printStackTrace();
			if (pstmt != null)  try{pstmt.close();}catch (Exception ex2){ex2.printStackTrace();}
			if (conn != null){
				try{
					conn.rollback();
					ConnectionResource.release(conn);
				}catch(Exception ex3){
					ecamsLogger.error("## Cmr0750.progCncl() connection release exception ##");
					ex3.printStackTrace();
				}				
			}			
			ecamsLogger.error("## Cmr0750.progCncl() Exception START ##");				
			ecamsLogger.error("## Error DESC : ", exception);	
			ecamsLogger.error("## Cmr0750.progCncl() Exception END ##");				
			throw exception;
		}finally{
			if (strQuery != null) 	strQuery = null;
			if (pstmt != null)  try{pstmt.close();}catch (Exception ex2){ex2.printStackTrace();}
			if (conn != null){
				try{
					ConnectionResource.release(conn);
				}catch(Exception ex3){
					ecamsLogger.error("## Cmr0750.progCncl() connection release exception ##");
					ex3.printStackTrace();
				}
			}
		}
	}//end of progCncl() method statement
	
	
	public ArrayList<HashMap<String, String>> confLocat(String AcptNo,String PrcSw) throws SQLException, Exception {
		Connection        conn        = null;
		PreparedStatement pstmt       = null;
		PreparedStatement pstmt2       = null;
		ResultSet         rs          = null;
		ResultSet         rs2         = null;
		StringBuffer      strQuery    = new StringBuffer();
		
		ArrayList<HashMap<String, String>>  rsval = new ArrayList<HashMap<String, String>>();
		HashMap<String, String>			  rst		  = null;
		ConnectionContext connectionContext = new ConnectionResource();
		String            strTeamCd   = "";  
		String            strTeam     = "";
		rsval.clear();
		
		try {
			
			conn = connectionContext.getConnection();
			
			strQuery.append("select cr_team,cr_teamcd,cr_confname,cr_prcsw,cr_confusr            \n");
			strQuery.append("  from cmr9900                                                      \n"); 
			strQuery.append(" where cr_acptno=? and cr_locat='00'                                \n");
	        pstmt = conn.prepareStatement(strQuery.toString());	
			//pstmt = new LoggableStatement(conn,strQuery.toString());
			pstmt.setString(1, AcptNo);
	        ////ecamsLogger.error(((LoggableStatement)pstmt).getQueryString());           
	        rs = pstmt.executeQuery();
			if (rs.next()){
				rst = new HashMap<String, String>();
				rst.put("signteam", rs.getString("cr_team"));
				rst.put("signteamcd", rs.getString("cr_teamcd"));
				rst.put("confusr", rs.getString("cr_confusr"));
				rst.put("prcsw", rs.getString("cr_prcsw"));
				strTeamCd = rs.getString("cr_teamcd"); 
				strTeam = rs.getString("cr_team"); 
				if (rs.getString("cr_teamcd").equals("1")) {
					strQuery.setLength(0);
					strQuery.append("select cm_codename from cmm0020             \n");
					strQuery.append(" where cm_macode='SYSGBN' and cm_micode=?   \n");
					pstmt2 = conn.prepareStatement(strQuery.toString());	
					pstmt2.setString(1,rs.getString("cr_team"));
					rs2 = pstmt2.executeQuery();
					if (rs2.next()){
						rst.put("confname", rs2.getString("cm_codename"));
					}
					rs2.close();
					pstmt2.close();
					
				}else if (rs.getString("cr_teamcd").equals("2") || rs.getString("cr_teamcd").equals("3") ||
						rs.getString("cr_teamcd").equals("6") || rs.getString("cr_teamcd").equals("7") ||
						rs.getString("cr_teamcd").equals("8")) {
					strQuery.setLength(0);
					strQuery.append("select cm_username from cmm0040             \n");
					strQuery.append(" where cm_userid=?                          \n");
					pstmt2 = conn.prepareStatement(strQuery.toString());	
					pstmt2.setString(1,rs.getString("cr_team"));
					rs2 = pstmt2.executeQuery();
					if (rs2.next()){
						rst.put("confname", rs2.getString("cm_username") + "결재대기 중");
					}
					rs2.close();
					pstmt2.close();
				}else {
					rst.put("confname", rs.getString("cr_confname"));
				}
			}
			rs.close();
			pstmt.close();
			            
			strQuery.setLength(0);
			strQuery.append("select count(*) cnt from cmr9900                 \n"); 
			strQuery.append(" where cr_acptno=?                               \n");
			strQuery.append("   and cr_status<>'0'                            \n");
			//pstmt = new LoggableStatement(conn,strQuery.toString());
			pstmt = conn.prepareStatement(strQuery.toString());	
			pstmt.setString(1, AcptNo);
	        ////ecamsLogger.error(((LoggableStatement)pstmt).getQueryString());           
	        rs = pstmt.executeQuery();
			if (rs.next()){
				if (rs.getInt("cnt") == 0){
					rst.put("updtsw3", "1");
				}
				else{
					rst.put("updtsw3", "0");
				}
			}
            rs.close();
            pstmt.close();
            
            if (strTeamCd.equals("1")) {
            	rst.put("log", "1");
            	
            	strQuery.setLength(0);
    			strQuery.append("select count(*) cnt from cmr1011                 \n"); 
    			strQuery.append(" where cr_acptno=? and cr_prcsys=?               \n");
    			strQuery.append("   and nvl(cr_prcrst,'0000')<>'0000'             \n");
    			
    			pstmt = conn.prepareStatement(strQuery.toString());	
    			//pstmt = new LoggableStatement(conn,strQuery.toString());
    			pstmt.setString(1, AcptNo);
    			pstmt.setString(2, strTeam);
    	        ////ecamsLogger.error(((LoggableStatement)pstmt).getQueryString());           
    	        rs = pstmt.executeQuery();
    			if (rs.next()){
    				if (rs.getInt("cnt") > 0){
    					rst.put("errtry", "1");
    				}
    				else{
    					rst.put("errtry", "0");
    				}
    			}
                rs.close();
                pstmt.close();
            }
			else {
				strQuery.setLength(0);
				strQuery.append("select count(*) cnt                                                 \n");
				strQuery.append("  from cmr1011                                                      \n"); 
				strQuery.append(" where cr_acptno=?                                                  \n");
				
				pstmt = conn.prepareStatement(strQuery.toString());	
				//pstmt = new LoggableStatement(conn,strQuery.toString());
				pstmt.setString(1, AcptNo);
		        ////ecamsLogger.error(((LoggableStatement)pstmt).getQueryString());           
		        rs = pstmt.executeQuery();
				if (rs.next()){
					if (rs.getInt("cnt") > 0){
						rst.put("log", "1");
					}
					else{
						rst.put("log", "0");
					}
				}
	            rs.close();
	            pstmt.close();
			}
			rs.close();
			pstmt.close();
            conn.close();
			conn = null;
			pstmt = null;
			rs = null;
            
            rsval.add(rst);
            rst = null;
            
            return rsval;
            
		} catch (SQLException sqlexception) {
			sqlexception.printStackTrace();
			if (rs != null)     try{rs.close();}catch (Exception ex){ex.printStackTrace();}
			if (pstmt != null)  try{pstmt.close();}catch (Exception ex2){ex2.printStackTrace();}
			if (conn != null){
				try{
					ConnectionResource.release(conn);
				}catch(Exception ex3){
					ecamsLogger.error("## Cmr0750.confLocat() connection release exception ##");
					ex3.printStackTrace();
				}				
			}
			ecamsLogger.error("## Cmr0750.confLocat() SQLException START ##");
			ecamsLogger.error("## Error DESC : ", sqlexception);	
			ecamsLogger.error("## Cmr0750.confLocat() SQLException END ##");			
			throw sqlexception;
		} catch (Exception exception) {
			exception.printStackTrace();
			if (rs != null)     try{rs.close();}catch (Exception ex){ex.printStackTrace();}
			if (pstmt != null)  try{pstmt.close();}catch (Exception ex2){ex2.printStackTrace();}
			if (conn != null){
				try{
					ConnectionResource.release(conn);
				}catch(Exception ex3){
					ecamsLogger.error("## Cmr0750.confLocat() connection release exception ##");
					ex3.printStackTrace();
				}				
			}			
			ecamsLogger.error("## Cmr0750.confLocat() Exception START ##");				
			ecamsLogger.error("## Error DESC : ", exception);	
			ecamsLogger.error("## Cmr0750.confLocat() Exception END ##");				
			throw exception;
		}finally{
			if (strQuery != null) 	strQuery = null;
			if (rsval != null)	rsval = null;
			if (rs != null)     try{rs.close();}catch (Exception ex){ex.printStackTrace();}
			if (pstmt != null)  try{pstmt.close();}catch (Exception ex2){ex2.printStackTrace();}
			if (conn != null){
				try{
					ConnectionResource.release(conn);
				}catch(Exception ex3){
					ecamsLogger.error("## Cmr0750.confLocat() connection release exception ##");
					ex3.printStackTrace();
				}
			}
		}
	}//end of confLocat() method statement
	
	public ArrayList<HashMap<String, String>> confLocat_conn(String AcptNo,String PrcSw,Connection conn) throws SQLException, Exception {
		PreparedStatement pstmt       = null;
		PreparedStatement pstmt2       = null;
		ResultSet         rs          = null;
		ResultSet         rs2         = null;
		StringBuffer      strQuery    = new StringBuffer();
		
		ArrayList<HashMap<String, String>>  rsval = new ArrayList<HashMap<String, String>>();
		HashMap<String, String>			  rst		  = null;
		String            strTeamCd   = "";  
		String            strTeam     = "";
		rsval.clear();
		
		try {
			strQuery.setLength(0);
			strQuery.append("select cr_team,cr_teamcd,cr_confname,cr_prcsw,cr_confusr            \n");
			strQuery.append("  from cmr9900                                                      \n"); 
			strQuery.append(" where cr_acptno=? and cr_locat='00'                                \n");
	        pstmt = conn.prepareStatement(strQuery.toString());	
			//pstmt = new LoggableStatement(conn,strQuery.toString());
			pstmt.setString(1, AcptNo);
	        ////ecamsLogger.error(((LoggableStatement)pstmt).getQueryString());           
	        rs = pstmt.executeQuery();
			if (rs.next()){
				rst = new HashMap<String, String>();
				rst.put("signteam", rs.getString("cr_team"));
				rst.put("signteamcd", rs.getString("cr_teamcd"));
				rst.put("confusr", rs.getString("cr_confusr"));
				rst.put("prcsw", rs.getString("cr_prcsw"));
				strTeamCd = rs.getString("cr_teamcd"); 
				strTeam = rs.getString("cr_team"); 
				if (rs.getString("cr_teamcd").equals("1")) {
					strQuery.setLength(0);
					strQuery.append("select cm_codename from cmm0020             \n");
					strQuery.append(" where cm_macode='SYSGBN' and cm_micode=?   \n");
					pstmt2 = conn.prepareStatement(strQuery.toString());	
					pstmt2.setString(1,rs.getString("cr_team"));
					rs2 = pstmt2.executeQuery();
					if (rs2.next()){
						rst.put("confname", rs2.getString("cm_codename"));
					}
					rs2.close();
					pstmt2.close();
					
				}else if (rs.getString("cr_teamcd").equals("2") || rs.getString("cr_teamcd").equals("3") ||
						rs.getString("cr_teamcd").equals("6") || rs.getString("cr_teamcd").equals("7") ||
						rs.getString("cr_teamcd").equals("8")) {
					strQuery.setLength(0);
					strQuery.append("select cm_username from cmm0040             \n");
					strQuery.append(" where cm_userid=?                          \n");
					pstmt2 = conn.prepareStatement(strQuery.toString());	
					pstmt2.setString(1,rs.getString("cr_team"));
					rs2 = pstmt2.executeQuery();
					if (rs2.next()){
						rst.put("confname", rs2.getString("cm_username") + "결재대기 중");
					}
					rs2.close();
					pstmt2.close();
				}else {
					rst.put("confname", rs.getString("cr_confname"));
				}
			}
			rs.close();
			pstmt.close();
			            
			strQuery.setLength(0);
			strQuery.append("select count(*) cnt from cmr9900                 \n"); 
			strQuery.append(" where cr_acptno=?                               \n");
			strQuery.append("   and cr_status<>'0'                            \n");
			//pstmt = new LoggableStatement(conn,strQuery.toString());
			pstmt = conn.prepareStatement(strQuery.toString());	
			pstmt.setString(1, AcptNo);
	        ////ecamsLogger.error(((LoggableStatement)pstmt).getQueryString());           
	        rs = pstmt.executeQuery();
			if (rs.next()){
				if (rs.getInt("cnt") == 0){
					rst.put("updtsw3", "1");
				}
				else{
					rst.put("updtsw3", "0");
				}
			}
            rs.close();
            pstmt.close();
            
            if (strTeamCd.equals("1")) {
            	rst.put("log", "1");
            	
            	strQuery.setLength(0);
    			strQuery.append("select count(*) cnt from cmr1011                 \n"); 
    			strQuery.append(" where cr_acptno=? and cr_prcsys=?               \n");
    			strQuery.append("   and nvl(cr_prcrst,'0000')<>'0000'             \n");
    			
    			pstmt = conn.prepareStatement(strQuery.toString());	
    			//pstmt = new LoggableStatement(conn,strQuery.toString());
    			pstmt.setString(1, AcptNo);
    			pstmt.setString(2, strTeam);
    	        ////ecamsLogger.error(((LoggableStatement)pstmt).getQueryString());           
    	        rs = pstmt.executeQuery();
    			if (rs.next()){
    				if (rs.getInt("cnt") > 0){
    					rst.put("errtry", "1");
    				}
    				else{
    					rst.put("errtry", "0");
    				}
    			}
                rs.close();
                pstmt.close();
            }
			else {
				strQuery.setLength(0);
				strQuery.append("select count(*) cnt                                                 \n");
				strQuery.append("  from cmr1011                                                      \n"); 
				strQuery.append(" where cr_acptno=?                                                  \n");
				
				pstmt = conn.prepareStatement(strQuery.toString());	
				//pstmt = new LoggableStatement(conn,strQuery.toString());
				pstmt.setString(1, AcptNo);
		        ////ecamsLogger.error(((LoggableStatement)pstmt).getQueryString());           
		        rs = pstmt.executeQuery();
				if (rs.next()){
					if (rs.getInt("cnt") > 0){
						rst.put("log", "1");
					}
					else{
						rst.put("log", "0");
					}
				}
	            rs.close();
	            pstmt.close();
			}
			rs.close();
			pstmt.close();
			
            rsval.add(rst);
            rst = null;
            
            return rsval;
            
		} catch (SQLException sqlexception) {
			sqlexception.printStackTrace();
			if (rs != null)     try{rs.close();}catch (Exception ex){ex.printStackTrace();}
			if (pstmt != null)  try{pstmt.close();}catch (Exception ex2){ex2.printStackTrace();}
			ecamsLogger.error("## Cmr0750.confLocat() SQLException START ##");
			ecamsLogger.error("## Error DESC : ", sqlexception);	
			ecamsLogger.error("## Cmr0750.confLocat() SQLException END ##");			
			throw sqlexception;
		} catch (Exception exception) {
			exception.printStackTrace();
			if (rs != null)     try{rs.close();}catch (Exception ex){ex.printStackTrace();}
			if (pstmt != null)  try{pstmt.close();}catch (Exception ex2){ex2.printStackTrace();}
			ecamsLogger.error("## Cmr0750.confLocat() Exception START ##");				
			ecamsLogger.error("## Error DESC : ", exception);	
			ecamsLogger.error("## Cmr0750.confLocat() Exception END ##");				
			throw exception;
		}finally{
			if (strQuery != null) 	strQuery = null;
			if (rsval != null)	rsval = null;
			if (rs != null)     try{rs.close();}catch (Exception ex){ex.printStackTrace();}
			if (pstmt != null)  try{pstmt.close();}catch (Exception ex2){ex2.printStackTrace();}
		}
	}//end of confLocat() method statement
	
}//end of Cmr0750 class statement
