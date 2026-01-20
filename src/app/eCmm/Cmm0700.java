/*****************************************************************************************
	1. program ID	: eCmm0700.java
	2. create date	: 
	3. auth		    : min seuk
	4. update date	: 
	5. auth		    : No Name
	6. description	: [관리자] -> [환경설정]
*****************************************************************************************/

package app.eCmm;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.logging.log4j.Logger;
//import com.ecams.common.base.Encryptor;
import com.ecams.common.dbconn.ConnectionContext;
import com.ecams.common.dbconn.ConnectionResource;
import com.ecams.common.logger.EcamsLogger;
import app.common.LoggableStatement;



/**
 * @author bigeyes
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */


public class Cmm0700{
    /**
     * Logger Class Instance Creation
     * logger
     */
    Logger ecamsLogger  = EcamsLogger.getLoggerInstance();
    
    //Encryptor oEncryptor = Encryptor.instance();
    
	/**
	 * eCAMS Agent정보 조회
	 * @param  
	 * @return HashMap
	 * @throws SQLException
	 * @throws Exception
	 */
    public HashMap<String, String> getAgentInfo() throws SQLException, Exception {
    	Connection        conn        = null;
		PreparedStatement pstmt       = null;
		ResultSet         rs          = null;
		StringBuffer      strQuery    = new StringBuffer();
		HashMap<String, String>			  rst		  = null;

		ConnectionContext connectionContext = new ConnectionResource();
		
		try {
			conn = connectionContext.getConnection(); 

			strQuery.setLength(0);
			strQuery.append("select cm_ipaddr,cm_port,cm_passwd,cm_policypwd,cm_pwdcnt, \n");
			strQuery.append("cm_pwdterm,cm_pwdcd,cm_ipaddr2,cm_initpwd,cm_tstpwd,cm_proctot,cm_srcloc,cm_url,cm_mgrlog, cm_effskip \n");
			strQuery.append("from cmm0010 \n");
			strQuery.append("where cm_stno='ECAMS' \n");
			
			pstmt = conn.prepareStatement(strQuery.toString());
			
            rs = pstmt.executeQuery();

            rst = new HashMap<String, String>();
            
            if (rs.next())
            {
				rst.put("cm_ipaddr", rs.getString("cm_ipaddr"));
				rst.put("cm_port", rs.getString("cm_port"));
				rst.put("cm_passwd", rs.getString("cm_passwd"));
				/*
				if (rs.getString("cm_passwd") != null)
					rst.put("cm_passwd", oEncryptor.strGetDecrypt(rs.getString("cm_passwd")));
				else
					rst.put("cm_passwd", "");
			    */
				rst.put("cm_policypwd", rs.getString("cm_policypwd"));
				rst.put("cm_pwdcnt", rs.getString("cm_pwdcnt"));
				rst.put("cm_pwdterm", rs.getString("cm_pwdterm"));
				rst.put("cm_pwdcd", rs.getString("cm_pwdcd"));
				rst.put("cm_ipaddr2", rs.getString("cm_ipaddr2"));
				rst.put("cm_initpwd", rs.getString("cm_initpwd"));
				rst.put("cm_srcloc", rs.getString("cm_srcloc"));
				rst.put("cm_url", rs.getString("cm_url"));
				rst.put("cm_mgrlog", rs.getString("cm_mgrlog"));
				rst.put("cm_effskip", rs.getString("cm_effskip"));
				/*
				if (rs.getString("cm_initpwd") != null)
					rst.put("cm_initpwd", oEncryptor.strGetDecrypt(rs.getString("cm_initpwd")));
				else
					rst.put("cm_initpwd", "");
				*/
				rst.put("cm_tstpwd", rs.getString("cm_tstpwd"));
				if (rs.getString("cm_proctot") == null && rs.getString("cm_proctot") == ""){
					rst.put("cm_proctot", "50");
				}else{
					rst.put("cm_proctot", rs.getString("cm_proctot"));
				}
				/*
				if (rs.getString("cm_tstpwd") != null)
					rst.put("cm_tstpwd", oEncryptor.strGetDecrypt(rs.getString("cm_tstpwd")));
				else
					rst.put("cm_tstpwd", "");
				*/
			}
            rs.close();
            pstmt.close();
            conn.close();
            
            rs = null;
            pstmt = null;
            conn = null;
            
            return rst;
            
		} catch (SQLException sqlexception) {
			sqlexception.printStackTrace();
			ecamsLogger.error("## Cmm0700.getAgentInfo() SQLException START ##");
			ecamsLogger.error("## Error DESC : ", sqlexception);	
			ecamsLogger.error("## Cmm0700.getAgentInfo() SQLException END ##");			
			throw sqlexception;
		} catch (Exception exception) {
			exception.printStackTrace();
			ecamsLogger.error("## Cmm0700.getAgentInfo() Exception START ##");				
			ecamsLogger.error("## Error DESC : ", exception);	
			ecamsLogger.error("## Cmm0700.getAgentInfo() Exception END ##");				
			throw exception;
		}finally{
			if (strQuery != null) 	strQuery = null;
			if (rst != null)	rst = null;
			if (rs != null)     try{rs.close();}catch (Exception ex){ex.printStackTrace();}
			if (pstmt != null)  try{pstmt.close();}catch (Exception ex2){ex2.printStackTrace();}
			if (conn != null){
				try{
					ConnectionResource.release(conn);
				}catch(Exception ex3){
					ecamsLogger.error("## Cmm0700.getAgentInfo() connection release exception ##");
					ex3.printStackTrace();
				}
			}
		}
	}//end of getAgentInfo() method statement
    
    
    public HashMap<String, String> setAgentInfo(HashMap<String,String> objData) throws SQLException, Exception {
		Connection        conn        = null;
		PreparedStatement pstmt       = null;
		ResultSet         rs          = null;
		StringBuffer      strQuery    = new StringBuffer();
		HashMap<String, String>			  rst		  = null;
		
		int				  nret;
		
		ConnectionContext connectionContext = new ConnectionResource();
		
		
		try {
			conn = connectionContext.getConnection(); 
    
			strQuery.setLength(0);
			strQuery.append("select count(*) as cnt \n");
			strQuery.append("from cmm0010 \n");
			strQuery.append("where cm_stno='ECAMS' \n");
			
			pstmt = conn.prepareStatement(strQuery.toString());
			
            rs = pstmt.executeQuery();

            nret = 0;
            if (rs.next()){
            	nret = rs.getInt("cnt");
			}
            rs.close();
            pstmt.close();

            if (nret > 0){
    			strQuery.setLength(0);
    			strQuery.append("update cmm0010 \n");
    			strQuery.append("	set	cm_ipaddr = ? , \n");
    			strQuery.append("		cm_port   = ? , \n");
    			strQuery.append("		cm_passwd   = ? , \n");
    			strQuery.append("		cm_initpwd   = ? , \n");
    			strQuery.append("		cm_pwdcnt   = ? , \n");
    			strQuery.append("		cm_pwdterm   = ? , \n");
    			strQuery.append("		cm_pwdcd   = ? , \n");
    			strQuery.append("		cm_ipaddr2   = ? , \n");
    			strQuery.append("		cm_tstpwd   = ?,  \n");
    			strQuery.append("		cm_proctot   = ? , \n");
    			strQuery.append("		cm_srcloc   = ? ,  \n");
    			strQuery.append("		cm_mgrlog   = ? ,  \n");
    			strQuery.append("		cm_effskip   = ? ,  \n");
    			strQuery.append("		cm_url   = ?  \n");
    			strQuery.append("where cm_stno='ECAMS' \n");            	
            }
            else{
    			strQuery.setLength(0);
    			strQuery.append("insert into cmm0010 (CM_STNO,CM_IPADDR,CM_PORT,CM_PASSWD,CM_INITPWD, \n");
    			strQuery.append("CM_PWDCNT,CM_PWDTERM,CM_PWDCD,CM_IPADDR2,CM_TSTPWD,CM_PROCTOT,CM_SRCLOC, cm_mgrlog, cm_effskip, cm_url ) \n");
    			strQuery.append("values ('ECAMS', ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) \n");
            }
            
            
            pstmt = conn.prepareStatement(strQuery.toString());
            
            pstmt.setString(1, objData.get("cm_ipaddr"));
            pstmt.setString(2, objData.get("cm_port"));
            //pstmt.setString(3, objData.get("cm_passwd"));
            if (objData.get("cm_passwd") != null)
            	pstmt.setString(3, objData.get("cm_passwd"));
            else
            	pstmt.setString(3, "");
            //pstmt.setString(4, objData.get("cm_initpwd"));
            if (objData.get("cm_initpwd") != null)
            	pstmt.setString(4, objData.get("cm_initpwd"));
            else
            	pstmt.setString(4, "");
            pstmt.setString(5, objData.get("cm_pwdcnt"));
            pstmt.setString(6, objData.get("cm_pwdterm"));
            pstmt.setString(7, objData.get("cm_pwdcd"));
            pstmt.setString(8, objData.get("cm_ipaddr2"));
            //pstmt.setString(9, objData.get("cm_tstpwd"));
            if (objData.get("cm_tstpwd") != null)
            	pstmt.setString(9, objData.get("cm_tstpwd"));
            else
            	pstmt.setString(9, "");
            if (objData.get("cm_proctot") != null)
            	pstmt.setString(10, objData.get("cm_proctot"));
            else
            	pstmt.setString(10, "");
            
            pstmt.setString(11, objData.get("cm_srcloc"));
            pstmt.setString(12, objData.get("cm_mgrlog"));
            pstmt.setString(13, objData.get("cm_effskip"));
            pstmt.setString(14, objData.get("cm_url"));
            
            nret = pstmt.executeUpdate();

            rst = new HashMap<String, String>();
            if (nret > 0){
            	rst.put("retval", "0");
            	rst.put("retmsg", "환경설정등록 처리 완료하였습니다.");
            	conn.commit();
            	pstmt.close();
            } 
            else{
            	rst.put("retval", "1");
            	rst.put("retmsg", "환경설정등록 처리 실패.");
            	conn.rollback();
            	pstmt.close();
            }
            
            conn.close();
			conn = null;
			pstmt = null;
			rs = null;
            
            
            return rst;
		} catch (SQLException sqlexception) {
			sqlexception.printStackTrace();
			ecamsLogger.error("## Cmm0700.setAgentInfo() SQLException START ##");
			ecamsLogger.error("## Error DESC : ", sqlexception);	
			ecamsLogger.error("## Cmm0700.setAgentInfo() SQLException END ##");			
			throw sqlexception;
		} catch (Exception exception) {
			exception.printStackTrace();
			ecamsLogger.error("## Cmm0700.setAgentInfo() Exception START ##");				
			ecamsLogger.error("## Error DESC : ", exception);	
			ecamsLogger.error("## Cmm0700.setAgentInfo() Exception END ##");				
			throw exception;
		}finally{
			if (strQuery != null) 	strQuery = null;
			if (rst != null)	rst = null;
			if (rs != null)     try{rs.close();}catch (Exception ex){ex.printStackTrace();}
			if (pstmt != null)  try{pstmt.close();}catch (Exception ex2){ex2.printStackTrace();}
			if (conn != null){
				try{
					ConnectionResource.release(conn);
				}catch(Exception ex3){
					ecamsLogger.error("## Cmm0700.setAgentInfo() connection release exception ##");
					ex3.printStackTrace();
				}
			}
		}
	}//end of setAgentInfo() method statement
    
    
    public Object[] getTab1Info() throws SQLException, Exception {
		Connection        conn        = null;
		PreparedStatement pstmt       = null;
		ResultSet         rs          = null;
		StringBuffer      strQuery    = new StringBuffer();
		HashMap<String, String>			  rst		  = null;
		ArrayList<HashMap<String, String>>  rsval = new ArrayList<HashMap<String, String>>();

		ConnectionContext connectionContext = new ConnectionResource();
		
		
		try {
			conn = connectionContext.getConnection(); 
    
			strQuery.setLength(0);
			strQuery.append("select a.cm_stno,a.cm_timecd,a.cm_sttime,a.cm_edtime,b.cm_codename \n");
			strQuery.append("from cmm0020 b,cmm0014 a \n");
			strQuery.append("where b.cm_macode='ECAMSTIME' and b.cm_micode=a.cm_timecd \n");
		     
		     			
			pstmt = conn.prepareStatement(strQuery.toString());
			
			
			
            rs = pstmt.executeQuery();

           
            rsval.clear();
            
            while (rs.next()){
            	rst = new HashMap<String, String>();
				rst.put("cm_stno", rs.getString("cm_stno")); 		
				rst.put("cm_timecd", rs.getString("cm_timecd"));
				rst.put("stedtime", rs.getString("cm_sttime")+"~"+rs.getString("cm_edtime"));
				rst.put("cm_sttime", rs.getString("cm_sttime"));
				rst.put("cm_edtime", rs.getString("cm_edtime"));	
				rst.put("cm_codename", rs.getString("cm_codename"));
				rsval.add(rst);
				rst = null;
			}
            
            rs.close();
            pstmt.close();
            conn.close();
			conn = null;
			pstmt = null;
			rs = null;
            
            
            return rsval.toArray();
            
            
		} catch (SQLException sqlexception) {
			sqlexception.printStackTrace();
			ecamsLogger.error("## Cmm0700.getTab1Info() SQLException START ##");
			ecamsLogger.error("## Error DESC : ", sqlexception);	
			ecamsLogger.error("## Cmm0700.getTab1Info() SQLException END ##");			
			throw sqlexception;
		} catch (Exception exception) {
			exception.printStackTrace();
			ecamsLogger.error("## Cmm0700.getTab1Info() Exception START ##");				
			ecamsLogger.error("## Error DESC : ", exception);	
			ecamsLogger.error("## Cmm0700.getTab1Info() Exception END ##");				
			throw exception;
		}finally{
			if (strQuery != null) 	strQuery = null;
			if (rst != null)	rst = null;
			if (rs != null)     try{rs.close();}catch (Exception ex){ex.printStackTrace();}
			if (pstmt != null)  try{pstmt.close();}catch (Exception ex2){ex2.printStackTrace();}
			if (conn != null){
				try{
					ConnectionResource.release(conn);
				}catch(Exception ex3){
					ecamsLogger.error("## Cmm0700.getTab1Info() connection release exception ##");
					ex3.printStackTrace();
				}
			}
		}
		
		
	}//end of sql_Qry() method statement    
    
    
    public HashMap<String, String> delTab1Info(String timegb) throws SQLException, Exception {
		Connection        conn        = null;
		PreparedStatement pstmt       = null;
		StringBuffer      strQuery    = new StringBuffer();
		HashMap<String, String>			  rst		  = null;
		int		nret;
		ConnectionContext connectionContext = new ConnectionResource();
		
		
		try {
			conn = connectionContext.getConnection(); 
    
			strQuery.setLength(0);
			strQuery.append("delete cmm0014 where cm_stno='ECAMS' \n");
			strQuery.append("and CM_TIMECD = ? \n");
		     		     			
			pstmt = conn.prepareStatement(strQuery.toString());

			
			pstmt.setString(1, timegb);
			
            nret = pstmt.executeUpdate();

            if (nret > 0){
	            rst = new HashMap<String, String>();
	           	rst.put("retval", "0");
	        	rst.put("retmsg", "삭제처리를 완료하였습니다.");
	        	conn.commit();
            }
            else{
	            rst = new HashMap<String, String>();
	           	rst.put("retval", "1");
	        	rst.put("retmsg", "삭제처리를 실패하였습니다.");
	        	conn.rollback();      	
            }
        	
            pstmt.close();
            conn.close();
			conn = null;
			pstmt = null;

            
            return rst;
            
            
		} catch (SQLException sqlexception) {
			sqlexception.printStackTrace();
			ecamsLogger.error("## Cmm0700.delTab1Info() SQLException START ##");
			ecamsLogger.error("## Error DESC : ", sqlexception);	
			ecamsLogger.error("## Cmm0700.delTab1Info() SQLException END ##");			
			throw sqlexception;
		} catch (Exception exception) {
			exception.printStackTrace();
			ecamsLogger.error("## Cmm0700.delTab1Info() Exception START ##");				
			ecamsLogger.error("## Error DESC : ", exception);	
			ecamsLogger.error("## Cmm0700.delTab1Info() Exception END ##");				
			throw exception;
		}finally{
			if (strQuery != null) 	strQuery = null;
			if (rst != null)	rst = null;
			if (pstmt != null)  try{pstmt.close();}catch (Exception ex2){ex2.printStackTrace();}
			if (conn != null){
				try{
					ConnectionResource.release(conn);
				}catch(Exception ex3){
					ecamsLogger.error("## Cmm0700.delTab1Info() connection release exception ##");
					ex3.printStackTrace();
				}
			}
		}
	}//end of delTab1Info() method statement
    
    
    public HashMap<String, String> addTab1Info(String timegb,String stTime,String edTime) throws SQLException, Exception {
		Connection        conn        = null;
		PreparedStatement pstmt       = null;
		StringBuffer      strQuery    = new StringBuffer();
		HashMap<String, String>			  rst		  = null;
		ResultSet         rs          = null;
		ConnectionContext connectionContext = new ConnectionResource();
		int				  nret;
		
		try {
			conn = connectionContext.getConnection(); 
    
			strQuery.setLength(0);
			strQuery.append("select count(*) cnt from cmm0014 \n");
			strQuery.append("where cm_stno='ECAMS' \n"); 
			strQuery.append("and CM_TIMECD = ? \n");
		     		     			
			pstmt = conn.prepareStatement(strQuery.toString());

			pstmt.setString(1, timegb);
			
			rs = pstmt.executeQuery();
			
			nret = 0;
			if (rs.next()){
				nret = rs.getInt("cnt");
			}
			
			rs.close();
			pstmt.close();
			
			if (nret > 0){
				strQuery.setLength(0);
				strQuery.append("update cmm0014 set \n");
				strQuery.append("		cm_sttime = ?, \n");
				strQuery.append("		cm_edtime = ? \n");
				strQuery.append("where cm_stno = 'ECAMS' \n");
				strQuery.append("and   cm_timecd = ? \n");
				
			}
			else{
				strQuery.setLength(0);
				strQuery.append("insert into cmm0014 (CM_STNO,CM_STTIME,CM_EDTIME,CM_TIMECD) \n");
				strQuery.append(" values ( 'ECAMS', ?, ?, ? ) \n");
			}
			
			pstmt = conn.prepareStatement(strQuery.toString());
			
			
			pstmt.setString(1, stTime);
			pstmt.setString(2, edTime);
			pstmt.setString(3, timegb);
			
			nret = pstmt.executeUpdate();

            if (nret > 0){
            	rst = new HashMap<String, String>();
            	rst.put("retval", "0");
            	rst.put("retmsg", "등록처리를 완료하였습니다.");
            	conn.commit();
            }
            else{
            	rst = new HashMap<String, String>();
            	rst.put("retval", "1");
            	rst.put("retmsg", "등록처리를 실패하였습니다."); 
            	conn.rollback();
            }
        	
            pstmt.close();
            conn.close();
			conn = null;
			pstmt = null;
			rs = null;
            
            
            return rst;
            
            
		} catch (SQLException sqlexception) {
			sqlexception.printStackTrace();
			ecamsLogger.error("## Cmm0700.addTab1Info() SQLException START ##");
			ecamsLogger.error("## Error DESC : ", sqlexception);	
			ecamsLogger.error("## Cmm0700.addTab1Info() SQLException END ##");			
			throw sqlexception;
		} catch (Exception exception) {
			exception.printStackTrace();
			ecamsLogger.error("## Cmm0700.addTab1Info() Exception START ##");				
			ecamsLogger.error("## Error DESC : ", exception);	
			ecamsLogger.error("## Cmm0700.addTab1Info() Exception END ##");				
			throw exception;
		}finally{
			if (strQuery != null) 	strQuery = null;
			if (rst != null)	rst = null;
			if (pstmt != null)  try{pstmt.close();}catch (Exception ex2){ex2.printStackTrace();}
			if (conn != null){
				try{
					ConnectionResource.release(conn);
				}catch(Exception ex3){
					ecamsLogger.error("## Cmm0700.addTab1Info() connection release exception ##");
					ex3.printStackTrace();
				}
			}
		}
	}//end of addTab1Info() method statement            
    
    
    public Object[] getTab2Info() throws SQLException, Exception {
		Connection        conn        = null;
		PreparedStatement pstmt       = null;
		ResultSet         rs          = null;
		StringBuffer      strQuery    = new StringBuffer();
		HashMap<String, String>			  rst		  = null;
		ArrayList<HashMap<String, String>>  rsval = new ArrayList<HashMap<String, String>>();

		ConnectionContext connectionContext = new ConnectionResource();
		
		
		try {
			conn = connectionContext.getConnection(); 
    
	
			strQuery.setLength(0);
			strQuery.append("select a.cm_stno,a.cm_delcd,a.cm_delterm,a.cm_termcd, \n");
			strQuery.append("b.cm_codename,c.cm_codename termname \n");
			strQuery.append("from cmm0020 c,cmm0020 b,cmm0013 a  \n");
			strQuery.append("where b.cm_macode='ECAMSDIR' and b.cm_micode=a.cm_delcd \n");
			strQuery.append("and c.cm_macode='DBTERM' and c.cm_micode=a.cm_termcd \n");
		     
		     			
			pstmt = conn.prepareStatement(strQuery.toString());
			
			
			
            rs = pstmt.executeQuery();

           
            rsval.clear();
            
            while (rs.next()){
            	rst = new HashMap<String, String>();
				rst.put("cm_stno", rs.getString("cm_stno")); 		
				rst.put("cm_delcd", rs.getString("cm_delcd"));
				rst.put("delterm", rs.getString("cm_delterm")+" "+rs.getString("termname"));
				rst.put("cm_delterm", rs.getString("cm_delterm"));
				rst.put("cm_termcd", rs.getString("cm_termcd"));	
				rst.put("cm_codename", rs.getString("cm_codename"));
				rsval.add(rst);
				rst = null;
			}
            
            rs.close();
            pstmt.close();
            conn.close();
			conn = null;
			pstmt = null;
			rs = null;
            
            
            return rsval.toArray();
            
            
		} catch (SQLException sqlexception) {
			sqlexception.printStackTrace();
			ecamsLogger.error("## Cmm0700.getTab2Info() SQLException START ##");
			ecamsLogger.error("## Error DESC : ", sqlexception);	
			ecamsLogger.error("## Cmm0700.getTab2Info() SQLException END ##");			
			throw sqlexception;
		} catch (Exception exception) {
			exception.printStackTrace();
			ecamsLogger.error("## Cmm0700.getTab2Info() Exception START ##");				
			ecamsLogger.error("## Error DESC : ", exception);	
			ecamsLogger.error("## Cmm0700.getTab2Info() Exception END ##");				
			throw exception;
		}finally{
			if (strQuery != null) 	strQuery = null;
			if (rst != null)	rst = null;
			if (rs != null)     try{rs.close();}catch (Exception ex){ex.printStackTrace();}
			if (pstmt != null)  try{pstmt.close();}catch (Exception ex2){ex2.printStackTrace();}
			if (conn != null){
				try{
					ConnectionResource.release(conn);
				}catch(Exception ex3){
					ecamsLogger.error("## Cmm0700.getTab2Info() connection release exception ##");
					ex3.printStackTrace();
				}
			}
		}
	}//end of getTab2Info() method statement    
    
    
    public HashMap<String, String> delTab2Info(String delgb) throws SQLException, Exception {
		Connection        conn        = null;
		PreparedStatement pstmt       = null;
		StringBuffer      strQuery    = new StringBuffer();
		HashMap<String, String>			  rst		  = null;
		int		nret;
		ConnectionContext connectionContext = new ConnectionResource();
		
		
		try {
			conn = connectionContext.getConnection(); 
    
        
			strQuery.setLength(0);
			strQuery.append("delete cmm0013 where cm_stno='ECAMS' \n");
			strQuery.append("and CM_DELCD = ? \n");
		     		     			
			pstmt = conn.prepareStatement(strQuery.toString());

			
			pstmt.setString(1, delgb);
			
            nret = pstmt.executeUpdate();

            if (nret > 0){
	            rst = new HashMap<String, String>();
	           	rst.put("retval", "0");
	        	rst.put("retmsg", "삭제처리를 완료하였습니다.");
	        	conn.commit();
            }
            else{
	            rst = new HashMap<String, String>();
	           	rst.put("retval", "1");
	        	rst.put("retmsg", "삭제처리를 실패하였습니다.");
	        	conn.rollback();      	
            }
        	
            pstmt.close();
            conn.close();
			conn = null;
			pstmt = null;

            
            
            return rst;
            
            
		} catch (SQLException sqlexception) {
			sqlexception.printStackTrace();
			ecamsLogger.error("## Cmm0700.delTab2Info() SQLException START ##");
			ecamsLogger.error("## Error DESC : ", sqlexception);	
			ecamsLogger.error("## Cmm0700.delTab2Info() SQLException END ##");			
			throw sqlexception;
		} catch (Exception exception) {
			exception.printStackTrace();
			ecamsLogger.error("## Cmm0700.delTab2Info() Exception START ##");				
			ecamsLogger.error("## Error DESC : ", exception);	
			ecamsLogger.error("## Cmm0700.delTab2Info() Exception END ##");				
			throw exception;
		}finally{
			if (strQuery != null) 	strQuery = null;
			if (rst != null)	rst = null;
			if (pstmt != null)  try{pstmt.close();}catch (Exception ex2){ex2.printStackTrace();}
			if (conn != null){
				try{
					ConnectionResource.release(conn);
				}catch(Exception ex3){
					ecamsLogger.error("## Cmm0700.delTab2Info() connection release exception ##");
					ex3.printStackTrace();
				}
			}
		}
	}//end of delTab2Info() method statement
    
    
    public HashMap<String, String> addTab2Info(String delgb,String deljugi,String jugigb) throws SQLException, Exception {
		Connection        conn        = null;
		PreparedStatement pstmt       = null;
		StringBuffer      strQuery    = new StringBuffer();
		HashMap<String, String>			  rst		  = null;
		ResultSet         rs          = null;
		ConnectionContext connectionContext = new ConnectionResource();
		int				  nret;
		
		try {
			conn = connectionContext.getConnection(); 
    
			strQuery.setLength(0);
			strQuery.append("select count(*) cnt from cmm0013 \n");
			strQuery.append("where cm_stno='ECAMS' \n"); 
			strQuery.append("and cm_delcd = ? \n");
		     		     			
			pstmt = conn.prepareStatement(strQuery.toString());

			pstmt.setString(1, delgb);
			
			rs = pstmt.executeQuery();
			
			nret = 0;
			if (rs.next()){
				nret = rs.getInt("cnt");
			}
			
			rs.close();
			pstmt.close();
			
			if (nret > 0){
				strQuery.setLength(0);
				strQuery.append("update cmm0013 set \n");
				strQuery.append("		cm_DELTERM = ?, \n");
				strQuery.append("		cm_TERMCD = ? \n");
				strQuery.append("where cm_stno = 'ECAMS' \n");
				strQuery.append("and   cm_delcd = ? \n");
				
			}
			else{
				strQuery.setLength(0);
				strQuery.append("insert into cmm0013 (CM_STNO,cm_DELTERM,cm_TERMCD,cm_delcd,CM_AGENTDATE) \n");
				strQuery.append(" values ( 'ECAMS', ?, ?, ? ,sysdate) \n");
			}
			
			pstmt = conn.prepareStatement(strQuery.toString());
			
			pstmt.setString(1, deljugi);
			pstmt.setString(2, jugigb);
			pstmt.setString(3, delgb);
			
			nret = pstmt.executeUpdate();

            if (nret > 0){
            	rst = new HashMap<String, String>();
            	rst.put("retval", "0");
            	rst.put("retmsg", "등록처리를 완료하였습니다.");
            	conn.commit();
            }
            else{
            	rst = new HashMap<String, String>();
            	rst.put("retval", "1");
            	rst.put("retmsg", "등록처리를 실패하였습니다."); 
            	conn.rollback();
            }
        	
            pstmt.close();
            conn.close();
			conn = null;
			pstmt = null;
			
            
            
            return rst;
            
            
		} catch (SQLException sqlexception) {
			sqlexception.printStackTrace();
			ecamsLogger.error("## Cmm0700.addTab2Info() SQLException START ##");
			ecamsLogger.error("## Error DESC : ", sqlexception);	
			ecamsLogger.error("## Cmm0700.addTab2Info() SQLException END ##");			
			throw sqlexception;
		} catch (Exception exception) {
			exception.printStackTrace();
			ecamsLogger.error("## Cmm0700.addTab2Info() Exception START ##");				
			ecamsLogger.error("## Error DESC : ", exception);	
			ecamsLogger.error("## Cmm0700.addTab2Info() Exception END ##");				
			throw exception;
		}finally{
			if (strQuery != null) 	strQuery = null;
			if (rst != null)	rst = null;
			if (pstmt != null)  try{pstmt.close();}catch (Exception ex2){ex2.printStackTrace();}
			if (conn != null){
				try{
					ConnectionResource.release(conn);
				}catch(Exception ex3){
					ecamsLogger.error("## Cmm0700.addTab2Info() connection release exception ##");
					ex3.printStackTrace();
				}
			}
		}
	}//end of addTab2Info() method statement
    
    
    public Object[] getTab3Info() throws SQLException, Exception {
		Connection        conn        = null;
		PreparedStatement pstmt       = null;
		ResultSet         rs          = null;
		StringBuffer      strQuery    = new StringBuffer();
		HashMap<String, String>			  rst		  = null;
		ArrayList<HashMap<String, String>>  rsval = new ArrayList<HashMap<String, String>>();

		ConnectionContext connectionContext = new ConnectionResource();
		
		
		try {
			conn = connectionContext.getConnection(); 
    
		 
			strQuery.setLength(0);
			strQuery.append("select a.cm_stno,a.cm_pathcd,a.cm_path,a.cm_downip,a.cm_downport, \n");
			strQuery.append("a.cm_downusr, a.cm_downpass , b.cm_codename  \n");
			strQuery.append("from cmm0020 b,cmm0012 a \n");
			strQuery.append("where b.cm_macode='ECAMSDIR'  \n");
			strQuery.append("and a.cm_pathcd=b.cm_micode  \n");
		     
		     			
			pstmt = conn.prepareStatement(strQuery.toString());
			
			
			
            rs = pstmt.executeQuery();

           
            rsval.clear();
            
            while (rs.next()){
            	rst = new HashMap<String, String>();
				rst.put("cm_stno", rs.getString("cm_stno")); 		
				rst.put("cm_pathcd", rs.getString("cm_pathcd"));
				rst.put("cm_path", rs.getString("cm_path"));
				rst.put("cm_downip", rs.getString("cm_downip"));
				rst.put("cm_downport", rs.getString("cm_downport"));	
				rst.put("cm_downusr", rs.getString("cm_downusr"));
				rst.put("cm_downpass", rs.getString("cm_downpass"));	
				rst.put("cm_codename", rs.getString("cm_codename"));
				rsval.add(rst);
				rst = null;
			}
            
            rs.close();
            pstmt.close();
            conn.close();
			conn = null;
			pstmt = null;
			rs = null;
            
            return rsval.toArray();
            
            
		} catch (SQLException sqlexception) {
			sqlexception.printStackTrace();
			ecamsLogger.error("## Cmm0700.getTab3Info() SQLException START ##");
			ecamsLogger.error("## Error DESC : ", sqlexception);	
			ecamsLogger.error("## Cmm0700.getTab3Info() SQLException END ##");			
			throw sqlexception;
		} catch (Exception exception) {
			exception.printStackTrace();
			ecamsLogger.error("## Cmm0700.getTab3Info() Exception START ##");				
			ecamsLogger.error("## Error DESC : ", exception);	
			ecamsLogger.error("## Cmm0700.getTab3Info() Exception END ##");				
			throw exception;
		}finally{
			if (strQuery != null) 	strQuery = null;
			if (rst != null)	rst = null;
			if (rs != null)     try{rs.close();}catch (Exception ex){ex.printStackTrace();}
			if (pstmt != null)  try{pstmt.close();}catch (Exception ex2){ex2.printStackTrace();}
			if (conn != null){
				try{
					ConnectionResource.release(conn);
				}catch(Exception ex3){
					ecamsLogger.error("## Cmm0700.getTab3Info() connection release exception ##");
					ex3.printStackTrace();
				}
			}
		}
	}//end of getTab3Info() method statement    
    
    
    public HashMap<String, String> delTab3Info(String pathcd) throws SQLException, Exception {
		Connection        conn        = null;
		PreparedStatement pstmt       = null;
		StringBuffer      strQuery    = new StringBuffer();
		HashMap<String, String>			  rst		  = null;
		int		nret;
		ConnectionContext connectionContext = new ConnectionResource();
		
		
		try {
			conn = connectionContext.getConnection(); 
    
			strQuery.setLength(0);
			strQuery.append("delete cmm0012 where cm_stno='ECAMS' \n");
			strQuery.append("and CM_PATHCD = ? \n");
		     		     			
			pstmt = conn.prepareStatement(strQuery.toString());

			
			pstmt.setString(1, pathcd);
			
            nret = pstmt.executeUpdate();

            if (nret > 0){
	            rst = new HashMap<String, String>();
	           	rst.put("retval", "0");
	        	rst.put("retmsg", "삭제처리를 완료하였습니다.");
	        	conn.commit();
            }
            else{
	            rst = new HashMap<String, String>();
	           	rst.put("retval", "1");
	        	rst.put("retmsg", "삭제처리를 실패하였습니다.");
	        	conn.rollback();      	
            }
        	
            pstmt.close();
            conn.close();
			conn = null;
			pstmt = null;
			
            
            return rst;
            
            
		} catch (SQLException sqlexception) {
			sqlexception.printStackTrace();
			ecamsLogger.error("## Cmm0700.delTab3Info() SQLException START ##");
			ecamsLogger.error("## Error DESC : ", sqlexception);	
			ecamsLogger.error("## Cmm0700.delTab3Info() SQLException END ##");			
			throw sqlexception;
		} catch (Exception exception) {
			exception.printStackTrace();
			ecamsLogger.error("## Cmm0700.delTab3Info() Exception START ##");				
			ecamsLogger.error("## Error DESC : ", exception);	
			ecamsLogger.error("## Cmm0700.delTab3Info() Exception END ##");				
			throw exception;
		}finally{
			if (strQuery != null) 	strQuery = null;
			if (rst != null)	rst = null;
			if (pstmt != null)  try{pstmt.close();}catch (Exception ex2){ex2.printStackTrace();}
			if (conn != null){
				try{
					ConnectionResource.release(conn);
				}catch(Exception ex3){
					ecamsLogger.error("## Cmm0700.delTab3Info() connection release exception ##");
					ex3.printStackTrace();
				}
			}
		}
	}//end of delTab3Info() method statement
    
    
    public HashMap<String, String> addTab3Info(String pathcd,String path,String tip,String tport) throws SQLException, Exception {
		Connection        conn        = null;
		PreparedStatement pstmt       = null;
		StringBuffer      strQuery    = new StringBuffer();
		HashMap<String, String>			  rst		  = null;
		ResultSet         rs          = null;
		ConnectionContext connectionContext = new ConnectionResource();
		int				  nret;
		int				  pstmtcnt;
		
		try {
			conn = connectionContext.getConnection(); 
    
			strQuery.setLength(0);
			strQuery.append("select count(*) cnt from cmm0012 \n");
			strQuery.append("where cm_stno='ECAMS' \n"); 
			strQuery.append("and CM_PATHCD = ? \n");
		     		     			
			pstmt = conn.prepareStatement(strQuery.toString());

			pstmt.setString(1, pathcd);
			
			rs = pstmt.executeQuery();
			
			nret = 0;
			if (rs.next()){
				nret = rs.getInt("cnt");
			}
			
			rs.close();
			pstmt.close();
		        
			if (nret > 0){
				strQuery.setLength(0);
				strQuery.append("update cmm0012 set \n");
				if (tip != null){
					if (!tip.equals("")){
						strQuery.append("		cm_downip = ?, \n");
					}
					else{
						strQuery.append("		cm_downip = '', \n");
					}
				}
				else{
					strQuery.append("		cm_downip = '', \n");
				}
				if (tport != null){
					if (!tport.equals("")){
						strQuery.append("		cm_downport = ?, \n");
					}
					else{
						strQuery.append("		cm_downport = '', \n");
					}
				}
				else{
					strQuery.append("		cm_downport = '', \n");
				}
				strQuery.append("		cm_path = ? \n");
				strQuery.append("where cm_stno = 'ECAMS' \n");
				strQuery.append("and   cm_pathcd = ? \n");
				
			}
			else{
				strQuery.setLength(0);
				strQuery.append("insert into cmm0012 (CM_STNO,CM_downip,CM_downport,CM_path,CM_pathcd) \n");
				strQuery.append(" values ( 'ECAMS', \n");
				if (tip != null){
					if (!tip.equals("")){
						strQuery.append(" ?, \n");
					}
					else{
						strQuery.append(" '', \n");
					}
				}
				else{
					strQuery.append(" '', \n");					
				}
				if (tport != null){
					if (!tport.equals("")){
						strQuery.append(" ?, \n");
					}
					else{
						strQuery.append(" '', \n");
					}
				}
				else{
					strQuery.append(" '', \n");
				}
				strQuery.append(" ?, ? ) \n");
			}
			
			pstmt = conn.prepareStatement(strQuery.toString());
			pstmtcnt = 1;
			
			if (tip != null){
				if (!tip.equals("")){
					pstmt.setString(pstmtcnt++, tip);
				}
			}
			if (tport != null){
				if (!tport.equals("")){
					pstmt.setString(pstmtcnt++, tport);
				}
			}
			pstmt.setString(pstmtcnt++, path);
			pstmt.setString(pstmtcnt++, pathcd);
			
			nret = pstmt.executeUpdate();

            if (nret > 0){
            	rst = new HashMap<String, String>();
            	rst.put("retval", "0");
            	rst.put("retmsg", "등록처리를 완료하였습니다.");
            	conn.commit();
            }
            else{
            	rst = new HashMap<String, String>();
            	rst.put("retval", "1");
            	rst.put("retmsg", "등록처리를 실패하였습니다."); 
            	conn.rollback();
            }
        	
            pstmt.close();
            conn.close();
			conn = null;
			pstmt = null;
			rs = null;
            
            
            return rst;
            
            
		} catch (SQLException sqlexception) {
			sqlexception.printStackTrace();
			ecamsLogger.error("## Cmm0700.addTab3Info() SQLException START ##");
			ecamsLogger.error("## Error DESC : ", sqlexception);	
			ecamsLogger.error("## Cmm0700.addTab3Info() SQLException END ##");			
			throw sqlexception;
		} catch (Exception exception) {
			exception.printStackTrace();
			ecamsLogger.error("## Cmm0700.addTab3Info() Exception START ##");				
			ecamsLogger.error("## Error DESC : ", exception);	
			ecamsLogger.error("## Cmm0700.addTab3Info() Exception END ##");				
			throw exception;
		}finally{
			if (strQuery != null) 	strQuery = null;
			if (rst != null)	rst = null;
			if (pstmt != null)  try{pstmt.close();}catch (Exception ex2){ex2.printStackTrace();}
			if (conn != null){
				try{
					ConnectionResource.release(conn);
				}catch(Exception ex3){
					ecamsLogger.error("## Cmm0700.addTab3Info() connection release exception ##");
					ex3.printStackTrace();
				}
			}
		}
	}//end of addTab3Info() method statement
    
    
    public Object[] getTab4Info() throws SQLException, Exception {
		Connection        conn        = null;
		PreparedStatement pstmt       = null;
		ResultSet         rs          = null;
		StringBuffer      strQuery    = new StringBuffer();
		HashMap<String, String>			  rst		  = null;
		ArrayList<HashMap<String, String>>  rsval = new ArrayList<HashMap<String, String>>();

		ConnectionContext connectionContext = new ConnectionResource();
		
		
		try {
			conn = connectionContext.getConnection(); 
    
			strQuery.setLength(0);
			strQuery.append("select a.cm_stno,a.cm_gbncd,a.cm_svrip, \n");
			strQuery.append("a.cm_portno,a.cm_svrusr,a.cm_svrpass,b.cm_codename \n");
			strQuery.append("from cmm0020 b,cmm0015 a  \n");
			strQuery.append("where b.cm_macode='SVPROC' \n");
			strQuery.append("and b.cm_micode=a.cm_gbncd \n");
			
			pstmt = conn.prepareStatement(strQuery.toString());
			
            rs = pstmt.executeQuery();
            
            rsval.clear();
            
            while (rs.next()){
            	rst = new HashMap<String, String>();
				rst.put("cm_stno", rs.getString("cm_stno")); 		
				rst.put("cm_gbncd", rs.getString("cm_gbncd"));
				rst.put("cm_svrip", rs.getString("cm_svrip"));
				rst.put("cm_portno", rs.getString("cm_portno"));
				rst.put("cm_svrusr", rs.getString("cm_svrusr"));	
				//rst.put("cm_svrpass", rs.getString("cm_svrpass"));
				if (rs.getString("cm_svrpass") != null)
					//rst.put("cm_svrpass", oEncryptor.strGetDecrypt(rs.getString("cm_svrpass")));
					rst.put("cm_svrpass", rs.getString("cm_svrpass"));
				else
					rst.put("cm_svrpass", "");
				rst.put("cm_codename", rs.getString("cm_codename"));
				rsval.add(rst);
				rst = null;
			}
            
            rs.close();
            pstmt.close();
            conn.close();
			conn = null;
			pstmt = null;
			rs = null;
            
            return rsval.toArray();
            
            
		} catch (SQLException sqlexception) {
			sqlexception.printStackTrace();
			ecamsLogger.error("## Cmm0700.getTab4Info() SQLException START ##");
			ecamsLogger.error("## Error DESC : ", sqlexception);	
			ecamsLogger.error("## Cmm0700.getTab4Info() SQLException END ##");			
			throw sqlexception;
		} catch (Exception exception) {
			exception.printStackTrace();
			ecamsLogger.error("## Cmm0700.getTab4Info() Exception START ##");				
			ecamsLogger.error("## Error DESC : ", exception);	
			ecamsLogger.error("## Cmm0700.getTab4Info() Exception END ##");				
			throw exception;
		}finally{
			if (strQuery != null) 	strQuery = null;
			if (rst != null)	rst = null;
			if (rs != null)     try{rs.close();}catch (Exception ex){ex.printStackTrace();}
			if (pstmt != null)  try{pstmt.close();}catch (Exception ex2){ex2.printStackTrace();}
			if (conn != null){
				try{
					ConnectionResource.release(conn);
				}catch(Exception ex3){
					ecamsLogger.error("## Cmm0700.getTab4Info() connection release exception ##");
					ex3.printStackTrace();
				}
			}
		}
	}//end of getTab4Info() method statement    
    
    
    public HashMap<String, String> delTab4Info(String jobgb) throws SQLException, Exception {
		Connection        conn        = null;
		PreparedStatement pstmt       = null;
		StringBuffer      strQuery    = new StringBuffer();
		HashMap<String, String>			  rst		  = null;
		int		nret;
		ConnectionContext connectionContext = new ConnectionResource();
		
		
		try {
			conn = connectionContext.getConnection(); 
    
        
			strQuery.setLength(0);
			strQuery.append("delete cmm0015 where cm_stno='ECAMS' \n");
			strQuery.append("and CM_GBNCD = ? \n");
		     		     			
			pstmt = conn.prepareStatement(strQuery.toString());

			
			pstmt.setString(1, jobgb);
			
            nret = pstmt.executeUpdate();

            if (nret > 0){
	            rst = new HashMap<String, String>();
	           	rst.put("retval", "0");
	        	rst.put("retmsg", "삭제처리를 완료하였습니다.");
	        	conn.commit();
            }
            else{
	            rst = new HashMap<String, String>();
	           	rst.put("retval", "1");
	        	rst.put("retmsg", "삭제처리를 실패하였습니다.");
	        	conn.rollback();      	
            }
        	
            pstmt.close();
            conn.close();
			conn = null;
			pstmt = null;

            
            
            return rst;
            
            
		} catch (SQLException sqlexception) {
			sqlexception.printStackTrace();
			ecamsLogger.error("## Cmm0700.delTab4Info() SQLException START ##");
			ecamsLogger.error("## Error DESC : ", sqlexception);	
			ecamsLogger.error("## Cmm0700.delTab4Info() SQLException END ##");			
			throw sqlexception;
		} catch (Exception exception) {
			exception.printStackTrace();
			ecamsLogger.error("## Cmm0700.delTab4Info() Exception START ##");				
			ecamsLogger.error("## Error DESC : ", exception);	
			ecamsLogger.error("## Cmm0700.delTab4Info() Exception END ##");				
			throw exception;
		}finally{
			if (strQuery != null) 	strQuery = null;
			if (rst != null)	rst = null;
			if (pstmt != null)  try{pstmt.close();}catch (Exception ex2){ex2.printStackTrace();}
			if (conn != null){
				try{
					ConnectionResource.release(conn);
				}catch(Exception ex3){
					ecamsLogger.error("## Cmm0700.delTab4Info() connection release exception ##");
					ex3.printStackTrace();
				}
			}
		}
	}//end of delTab4Info() method statement
    
    
    public HashMap<String, String> addTab4Info(String jobgb,String tip,String tport,String tuserid,String tpwd) throws SQLException, Exception {
		Connection        conn        = null;
		PreparedStatement pstmt       = null;
		StringBuffer      strQuery    = new StringBuffer();
		HashMap<String, String>			  rst		  = null;
		ResultSet         rs          = null;
		ConnectionContext connectionContext = new ConnectionResource();
		int				  nret;
		int				  pstmtcnt;
		
		try {
			conn = connectionContext.getConnection(); 
    
			strQuery.setLength(0);
			strQuery.append("select count(*) cnt from cmm0015 \n");
			strQuery.append("where cm_stno='ECAMS' \n"); 
			strQuery.append("and CM_GBNCD = ? \n");
		     		     			
			pstmt = conn.prepareStatement(strQuery.toString());

			pstmt.setString(1, jobgb);
			
			rs = pstmt.executeQuery();
			
			nret = 0;
			if (rs.next()){
				nret = rs.getInt("cnt");
			}
			
			rs.close();
			pstmt.close();
			
		
			if (nret > 0){
				strQuery.setLength(0);
				strQuery.append("update cmm0015 set \n");
				if (tuserid != null){
					if (!tuserid.equals("")){
						strQuery.append("		CM_SVRUSR = ?, \n");
					}
					else{
						strQuery.append("		CM_SVRUSR = '', \n");
					}
				}
				else{
					strQuery.append("		CM_SVRUSR = '', \n");
				}
				if (tpwd != null){
					if (!tpwd.equals("")){
						strQuery.append("		CM_SVRPASS = ?, \n");
					}
					else{
						strQuery.append("		CM_SVRPASS = '', \n");
					}
				}
				else{
					strQuery.append("		CM_SVRPASS = '', \n");
				}				
				strQuery.append("		cm_svrip = ?, \n");
				strQuery.append("		cm_portno = ? \n");
				strQuery.append("where cm_stno = 'ECAMS' \n");
				strQuery.append("and   cm_gbncd = ? \n");
				
			}
			else{
				strQuery.setLength(0);
				strQuery.append("insert into cmm0015 (CM_STNO,CM_SVRUSR,CM_SVRPASS,CM_SVRIP,CM_PORTNO,CM_GBNCD) \n");
				strQuery.append(" values ( 'ECAMS', \n");
				if (tuserid != null){
					if (!tuserid.equals("")){
						strQuery.append(" ?, \n");
					}
					else{
						strQuery.append(" '', \n");
					}
				}
				else{
					strQuery.append(" '', \n");					
				}
				if (tpwd != null){
					if (!tpwd.equals("")){
						strQuery.append(" ?, \n");
					}
					else{
						strQuery.append(" '', \n");
					}
				}
				else{
					strQuery.append(" '', \n");
				}
				strQuery.append(" ?, ?, ? ) \n");
			}
			
			pstmt = conn.prepareStatement(strQuery.toString());
			
			pstmtcnt = 1;
			
			if (tuserid != null){
				if (!tuserid.equals("")){
					pstmt.setString(pstmtcnt++, tuserid);
				}
			}
			if (tpwd != null){
				if (!tpwd.equals("")){
					//pstmt.setString(pstmtcnt++, oEncryptor.strGetEncrypt(tpwd));
					pstmt.setString(pstmtcnt++, tpwd);
				}
			}
			
			pstmt.setString(pstmtcnt++, tip);
			pstmt.setString(pstmtcnt++, tport);
			pstmt.setString(pstmtcnt++, jobgb);
			
			nret = pstmt.executeUpdate();

            if (nret > 0){
            	rst = new HashMap<String, String>();
            	rst.put("retval", "0");
            	rst.put("retmsg", "등록처리를 완료하였습니다.");
            	conn.commit();
            }
            else{
            	rst = new HashMap<String, String>();
            	rst.put("retval", "1");
            	rst.put("retmsg", "등록처리를 실패하였습니다."); 
            	conn.rollback();
            }
        	
            pstmt.close();
            conn.close();
			conn = null;
			pstmt = null;
			rs = null;
            
            
            return rst;
            
            
		} catch (SQLException sqlexception) {
			sqlexception.printStackTrace();
			ecamsLogger.error("## Cmm0700.addTab4Info() SQLException START ##");
			ecamsLogger.error("## Error DESC : ", sqlexception);	
			ecamsLogger.error("## Cmm0700.addTab4Info() SQLException END ##");			
			throw sqlexception;
		} catch (Exception exception) {
			exception.printStackTrace();
			ecamsLogger.error("## Cmm0700.addTab4Info() Exception START ##");				
			ecamsLogger.error("## Error DESC : ", exception);	
			ecamsLogger.error("## Cmm0700.addTab4Info() Exception END ##");				
			throw exception;
		}finally{
			if (strQuery != null) 	strQuery = null;
			if (rst != null)	rst = null;
			if (pstmt != null)  try{pstmt.close();}catch (Exception ex2){ex2.printStackTrace();}
			if (conn != null){
				try{
					ConnectionResource.release(conn);
				}catch(Exception ex3){
					ecamsLogger.error("## Cmm0700.addTab4Info() connection release exception ##");
					ex3.printStackTrace();
				}
			}
		}
	}//end of addTab4Info() method statement 
    public Object[] getTab5Info() throws SQLException, Exception {
		Connection        conn        = null;
		PreparedStatement pstmt       = null;
		ResultSet         rs          = null;
		StringBuffer      strQuery    = new StringBuffer();
		HashMap<String, String>			  rst		  = null;
		ArrayList<HashMap<String, String>>  rsval = new ArrayList<HashMap<String, String>>();

		ConnectionContext connectionContext = new ConnectionResource();
		
		
		try {
			conn = connectionContext.getConnection(); 
    
			strQuery.setLength(0);
			strQuery.append("select a.cm_sysgbn,a.cm_rsrccd,b.cm_codename,      \n");
			strQuery.append("       c.cm_codename rsrccd                        \n");
			strQuery.append("  from cmm0020 c,cmm0020 b,cmm0016 a               \n");
			strQuery.append(" where b.cm_macode='SYSPROC'                       \n");
			strQuery.append("   and b.cm_micode=a.cm_sysgbn                     \n");
			strQuery.append("   and c.cm_macode='JAWON'                         \n");
			strQuery.append("   and c.cm_micode=a.cm_rsrccd                     \n");
			strQuery.append(" order by a.cm_sysgbn,a.cm_rsrccd                  \n");
			
			pstmt = conn.prepareStatement(strQuery.toString());
			
            rs = pstmt.executeQuery();
            
            rsval.clear();
            
            while (rs.next()){
            	rst = new HashMap<String, String>();
				rst.put("cm_sysgbn", rs.getString("cm_sysgbn")); 		
				rst.put("cm_rsrccd", rs.getString("cm_rsrccd"));
				rst.put("cm_codename", rs.getString("cm_codename"));
				rst.put("rsrccd", rs.getString("rsrccd"));
				rsval.add(rst);
				rst = null;
			}
            
            rs.close();
            pstmt.close();
            conn.close();
			conn = null;
			pstmt = null;
			rs = null;
            
            
            return rsval.toArray();
            
            
		} catch (SQLException sqlexception) {
			sqlexception.printStackTrace();
			ecamsLogger.error("## Cmm0700.getTab5Info() SQLException START ##");
			ecamsLogger.error("## Error DESC : ", sqlexception);	
			ecamsLogger.error("## Cmm0700.getTab5Info() SQLException END ##");			
			throw sqlexception;
		} catch (Exception exception) {
			exception.printStackTrace();
			ecamsLogger.error("## Cmm0700.getTab5Info() Exception START ##");				
			ecamsLogger.error("## Error DESC : ", exception);	
			ecamsLogger.error("## Cmm0700.getTab5Info() Exception END ##");				
			throw exception;
		}finally{
			if (strQuery != null) 	strQuery = null;
			if (rst != null)	rst = null;
			if (rs != null)     try{rs.close();}catch (Exception ex){ex.printStackTrace();}
			if (pstmt != null)  try{pstmt.close();}catch (Exception ex2){ex2.printStackTrace();}
			if (conn != null){
				try{
					ConnectionResource.release(conn);
				}catch(Exception ex3){
					ecamsLogger.error("## Cmm0700.getTab5Info() connection release exception ##");
					ex3.printStackTrace();
				}
			}
		}
	}//end of getTab5Info() method statement    
    

    public Object[] getJawon() throws SQLException, Exception {
		Connection        conn        = null;
		PreparedStatement pstmt       = null;
		ResultSet         rs          = null;
		StringBuffer      strQuery    = new StringBuffer();
		HashMap<String, String>			  rst		  = null;
		ArrayList<HashMap<String, String>>  rsval = new ArrayList<HashMap<String, String>>();

		ConnectionContext connectionContext = new ConnectionResource();
		
		
		try {
			conn = connectionContext.getConnection(); 
    
			strQuery.setLength(0);
			strQuery.append("select c.cm_micode,c.cm_codename                      \n");
			strQuery.append("  from cmm0036 e,cmm0031 d,cmm0020 c,cmm0038 b,cmm0030 a \n");
			strQuery.append(" where substr(a.cm_sysinfo,6,1)='1'                   \n");
			strQuery.append("   and a.cm_closedt is null                           \n");
			strQuery.append("   and a.cm_syscd=d.cm_syscd and d.cm_closedt is null \n");
			strQuery.append("   and d.cm_svrcd='05'                                \n");
			strQuery.append("   and d.cm_syscd=b.cm_syscd and d.cm_svrcd=b.cm_svrcd\n");
			strQuery.append("   and d.cm_seqno=b.cm_seqno                          \n");
			strQuery.append("   and b.cm_syscd=e.cm_syscd and b.cm_rsrccd=e.cm_rsrccd\n");
			strQuery.append("   and substr(e.cm_info,35,1)='1'                     \n");
			strQuery.append("   and c.cm_macode='JAWON' and c.cm_micode=b.cm_rsrccd\n");
			strQuery.append(" group by c.cm_micode,c.cm_codename                   \n");
			strQuery.append(" order by c.cm_codename                               \n");
			
			pstmt = conn.prepareStatement(strQuery.toString());
		//	pstmt = new LoggableStatement(conn,strQuery.toString());
		//	//ecamsLogger.error(((LoggableStatement)pstmt).getQueryString());
            rs = pstmt.executeQuery();
            
            rsval.clear();
            
            while (rs.next()){
            	rst = new HashMap<String, String>();
				rst.put("cm_micode", rs.getString("cm_micode")); 		
				rst.put("cm_codename", rs.getString("cm_codename"));
				rsval.add(rst);
				rst = null;
			}
            
            rs.close();
            pstmt.close();
            conn.close();
			conn = null;
			pstmt = null;
			rs = null;
            
            
            return rsval.toArray();
            
            
		} catch (SQLException sqlexception) {
			sqlexception.printStackTrace();
			ecamsLogger.error("## Cmm0700.getJawon() SQLException START ##");
			ecamsLogger.error("## Error DESC : ", sqlexception);	
			ecamsLogger.error("## Cmm0700.getJawon() SQLException END ##");			
			throw sqlexception;
		} catch (Exception exception) {
			exception.printStackTrace();
			ecamsLogger.error("## Cmm0700.getJawon() Exception START ##");				
			ecamsLogger.error("## Error DESC : ", exception);	
			ecamsLogger.error("## Cmm0700.getJawon() Exception END ##");				
			throw exception;
		}finally{
			if (strQuery != null) 	strQuery = null;
			if (rst != null)	rst = null;
			if (rs != null)     try{rs.close();}catch (Exception ex){ex.printStackTrace();}
			if (pstmt != null)  try{pstmt.close();}catch (Exception ex2){ex2.printStackTrace();}
			if (conn != null){
				try{
					ConnectionResource.release(conn);
				}catch(Exception ex3){
					ecamsLogger.error("## Cmm0700.getJawon() connection release exception ##");
					ex3.printStackTrace();
				}
			}
		}
	}//end of getJawon() method statement  
    public int delTab5Info(String SysGbn,String Jawon) throws SQLException, Exception {
		Connection        conn        = null;
		PreparedStatement pstmt       = null;
		StringBuffer      strQuery    = new StringBuffer();
		ConnectionContext connectionContext = new ConnectionResource();
		int               i = 0;
		
		try {
			conn = connectionContext.getConnection(); 
			String strJawon[] = Jawon.split(",");
			
			for (i=0;strJawon.length>i;i++) {
				strQuery.setLength(0);
				strQuery.append("delete cmm0016           \n");
				strQuery.append(" where cm_sysgbn=?       \n");
				strQuery.append("   and cm_rsrccd=?       \n");
				pstmt = conn.prepareStatement(strQuery.toString());
				pstmt.setString(1, SysGbn);
				pstmt.setString(2, strJawon[i]);
				pstmt.executeUpdate();
				pstmt.close();
			}
			conn.close();
			conn = null;
			pstmt = null;

            
            return 0;
            
            
		} catch (SQLException sqlexception) {
			sqlexception.printStackTrace();
			ecamsLogger.error("## Cmm0700.delTab5Info() SQLException START ##");
			ecamsLogger.error("## Error DESC : ", sqlexception);	
			ecamsLogger.error("## Cmm0700.delTab5Info() SQLException END ##");			
			throw sqlexception;
		} catch (Exception exception) {
			exception.printStackTrace();
			ecamsLogger.error("## Cmm0700.delTab5Info() Exception START ##");				
			ecamsLogger.error("## Error DESC : ", exception);	
			ecamsLogger.error("## Cmm0700.delTab5Info() Exception END ##");				
			throw exception;
		}finally{
			if (strQuery != null) 	strQuery = null;
			if (pstmt != null)  try{pstmt.close();}catch (Exception ex2){ex2.printStackTrace();}
			if (conn != null){
				try{
					ConnectionResource.release(conn);
				}catch(Exception ex3){
					ecamsLogger.error("## Cmm0700.delTab5Info() connection release exception ##");
					ex3.printStackTrace();
				}
			}
		}
	}//end of delTab5Info() method statement
    
    
    public int addTab5Info(String SysGbn,String Jawon) throws SQLException, Exception {
		Connection        conn        = null;
		PreparedStatement pstmt       = null;
		PreparedStatement pstmt2      = null;
		StringBuffer      strQuery    = new StringBuffer();
		ResultSet         rs          = null;
		ConnectionContext connectionContext = new ConnectionResource();
		int               i = 0;
		
		try {
			conn = connectionContext.getConnection(); 
			String strJawon[] = Jawon.split(",");
			
			for (i=0;strJawon.length>i;i++) {
				strQuery.setLength(0);
				strQuery.append("select count(*) cnt from cmm0016 \n");
				strQuery.append("where cm_sysgbn=?                \n"); 
				strQuery.append("  and CM_rsrccd=?                \n");			     		     			
				pstmt = conn.prepareStatement(strQuery.toString());	
				pstmt.setString(1, SysGbn);
				pstmt.setString(2, strJawon[i]);				
				rs = pstmt.executeQuery();
				if (rs.next()){
					if (rs.getInt("cnt") == 0) {
						strQuery.setLength(0);
						strQuery.append("insert into cmm0016      \n");
						strQuery.append(" (cm_sysgbn,cm_rsrccd)   \n");
						strQuery.append("values (?, ?)            \n");
						pstmt2 = conn.prepareStatement(strQuery.toString());
						pstmt2.setString(1, SysGbn);
						pstmt2.setString(2, strJawon[i]);
						pstmt2.executeUpdate();
						pstmt2.close();
					}
				}				
				rs.close();
				pstmt.close();
			}
		
			conn.close();
			conn = null;
			pstmt = null;
			rs = null;
            
            
            return 0;
            
            
		} catch (SQLException sqlexception) {
			sqlexception.printStackTrace();
			ecamsLogger.error("## Cmm0700.addTab5Info() SQLException START ##");
			ecamsLogger.error("## Error DESC : ", sqlexception);	
			ecamsLogger.error("## Cmm0700.addTab5Info() SQLException END ##");			
			throw sqlexception;
		} catch (Exception exception) {
			exception.printStackTrace();
			ecamsLogger.error("## Cmm0700.addTab5Info() Exception START ##");				
			ecamsLogger.error("## Error DESC : ", exception);	
			ecamsLogger.error("## Cmm0700.addTab5Info() Exception END ##");				
			throw exception;
		}finally{
			if (strQuery != null) 	strQuery = null;
			if (rs != null)     try{rs.close();}catch (Exception ex){ex.printStackTrace();}
			if (pstmt != null)  try{pstmt.close();}catch (Exception ex2){ex2.printStackTrace();}
			if (pstmt2 != null) try{pstmt.close();}catch (Exception ex2){ex2.printStackTrace();}
			if (conn != null){
				try{
					ConnectionResource.release(conn);
				}catch(Exception ex3){
					ecamsLogger.error("## Cmm0700.addTab5Info() connection release exception ##");
					ex3.printStackTrace();
				}
			}
		}
	}//end of addTab5Info() method statement    
    

    public Object[] getTab6Info() throws SQLException, Exception {
		Connection        conn        = null;
		PreparedStatement pstmt       = null;
		ResultSet         rs          = null;
		PreparedStatement pstmt2      = null;
		ResultSet         rs2         = null;
		StringBuffer      strQuery    = new StringBuffer();
		HashMap<String, String>			  rst		  = null;
		ArrayList<HashMap<String, String>>  rsval = new ArrayList<HashMap<String, String>>();
		String            strTerm    = "";
		String            strTime    = "";
		ConnectionContext connectionContext = new ConnectionResource();
		
		
		try {
			conn = connectionContext.getConnection(); 
    
			strQuery.setLength(0);
			strQuery.append("select a.cm_syscd,a.cm_termcd,a.cm_sttime,  \n");
			strQuery.append("       a.cm_edtime,a.cm_termsub,            \n");
			strQuery.append("       b.cm_codename,c.cm_sysmsg            \n");
			strQuery.append("  from cmm0030 c,cmm0020 b,cmm0330 a        \n");
			strQuery.append(" where b.cm_macode='STOPTERM'               \n");
			strQuery.append("   and b.cm_micode=a.cm_termcd              \n");
			strQuery.append("   and a.cm_syscd=c.cm_syscd                \n");
			strQuery.append(" order by a.cm_syscd                        \n");
			
			pstmt = conn.prepareStatement(strQuery.toString());
			
            rs = pstmt.executeQuery();
            
            rsval.clear();
            
            while (rs.next()){
            	rst = new HashMap<String, String>();
				rst.put("cm_syscd", rs.getString("cm_syscd")); 
				rst.put("cm_sysmsg", rs.getString("cm_sysmsg")); 		
				rst.put("cm_termcd", rs.getString("cm_termcd"));	
				rst.put("cm_sttime", rs.getString("cm_sttime"));	
				rst.put("cm_edtime", rs.getString("cm_edtime"));
				strTerm = rs.getString("cm_codename");
				if (rs.getString("cm_termsub") != null) {
					if (rs.getString("cm_termcd").equals("2")) {
						strQuery.setLength(0);
						strQuery.append("select cm_codename from cmm0020    \n");
						strQuery.append(" where cm_macode='WEEKDAY'         \n");
						strQuery.append("   and cm_micode=?                 \n");
						pstmt2 = conn.prepareStatement(strQuery.toString());
						pstmt2.setString(1, rs.getString("cm_termsub"));
						rs2 = pstmt2.executeQuery();
						if (rs2.next()) {
							strTerm = strTerm + " " + rs2.getString("cm_codename");
						}
						rs2.close();
						pstmt2.close();
					} else {
						strTerm = strTerm + " " + rs.getString("cm_termsub") + "일";
					}
					
					rst.put("cm_termsub", rs.getString("cm_termsub"));
				}
				rst.put("termmsg", strTerm);
				strTime = rs.getString("cm_sttime").substring(0,2) + ":" + rs.getString("cm_sttime").substring(2) 
				    + " - " + rs.getString("cm_edtime").substring(0,2) + ":" + rs.getString("cm_edtime").substring(2);
				rst.put("timemsg", strTime);
				rsval.add(rst);
				rst = null;
			}
            
            rs.close();
            pstmt.close();
            conn.close();
			conn = null;
			pstmt = null;
			rs = null;
            
            
            return rsval.toArray();
            
            
		} catch (SQLException sqlexception) {
			sqlexception.printStackTrace();
			ecamsLogger.error("## Cmm0700.getTab5Info() SQLException START ##");
			ecamsLogger.error("## Error DESC : ", sqlexception);	
			ecamsLogger.error("## Cmm0700.getTab5Info() SQLException END ##");			
			throw sqlexception;
		} catch (Exception exception) {
			exception.printStackTrace();
			ecamsLogger.error("## Cmm0700.getTab5Info() Exception START ##");				
			ecamsLogger.error("## Error DESC : ", exception);	
			ecamsLogger.error("## Cmm0700.getTab5Info() Exception END ##");				
			throw exception;
		}finally{
			if (strQuery != null) 	strQuery = null;
			if (rst != null)	rst = null;
			if (rs != null)     try{rs.close();}catch (Exception ex){ex.printStackTrace();}
			if (pstmt != null)  try{pstmt.close();}catch (Exception ex2){ex2.printStackTrace();}
			if (conn != null){
				try{
					ConnectionResource.release(conn);
				}catch(Exception ex3){
					ecamsLogger.error("## Cmm0700.getTab5Info() connection release exception ##");
					ex3.printStackTrace();
				}
			}
		}
	}//end of getTab6Info() method statement  
    public int addTab6Info(String TermCd,String StTime,String EdTime,String TermSub,String SysCd) throws SQLException, Exception {
		Connection        conn        = null;
		PreparedStatement pstmt       = null;
		StringBuffer      strQuery    = new StringBuffer();
		ConnectionContext connectionContext = new ConnectionResource();
		int               i = 0;
		
		try {
			conn = connectionContext.getConnection(); 
			String strSysCd[] = SysCd.split(",");
			
			for (i=0;strSysCd.length>i;i++) {
				strQuery.setLength(0);
				strQuery.append("delete cmm0330                \n");
				strQuery.append(" where cm_syscd=?             \n"); 		     		     			
				pstmt = conn.prepareStatement(strQuery.toString());	
				pstmt.setString(1, strSysCd[i]);				
				pstmt.executeUpdate();
				pstmt.close();
				
				strQuery.setLength(0);
				strQuery.append("insert into cmm0330                                 \n");
				strQuery.append(" (CM_SYSCD,CM_TERMCD,CM_STTIME,CM_EDTIME,CM_TERMSUB)\n");
				strQuery.append(" values (?, ?, ?, ?, ?)                             \n");
				pstmt = conn.prepareStatement(strQuery.toString());
				pstmt.setString(1, strSysCd[i]);
				pstmt.setString(2, TermCd);
				pstmt.setString(3, StTime);
				pstmt.setString(4, EdTime);
				pstmt.setString(5, TermSub);
				pstmt.executeUpdate();
				pstmt.close();
			}
		
			conn.close();
			conn = null;
			pstmt = null;
            
            
            return 0;
            
            
		} catch (SQLException sqlexception) {
			sqlexception.printStackTrace();
			ecamsLogger.error("## Cmm0700.addTab6Info() SQLException START ##");
			ecamsLogger.error("## Error DESC : ", sqlexception);	
			ecamsLogger.error("## Cmm0700.addTab6Info() SQLException END ##");			
			throw sqlexception;
		} catch (Exception exception) {
			exception.printStackTrace();
			ecamsLogger.error("## Cmm0700.addTab6Info() Exception START ##");				
			ecamsLogger.error("## Error DESC : ", exception);	
			ecamsLogger.error("## Cmm0700.addTab6Info() Exception END ##");				
			throw exception;
		}finally{
			if (strQuery != null) 	strQuery = null;
			if (pstmt != null)  try{pstmt.close();}catch (Exception ex2){ex2.printStackTrace();}
			if (conn != null){
				try{
					ConnectionResource.release(conn);
				}catch(Exception ex3){
					ecamsLogger.error("## Cmm0700.addTab6Info() connection release exception ##");
					ex3.printStackTrace();
				}
			}
		}
	}//end of addTab6Info() method statement  
    public int delTab6Info(String SysCd) throws SQLException, Exception {
		Connection        conn        = null;
		PreparedStatement pstmt       = null;
		StringBuffer      strQuery    = new StringBuffer();
		ConnectionContext connectionContext = new ConnectionResource();
		int               i = 0;
		
		try {
			conn = connectionContext.getConnection(); 
			String strSysCd[] = SysCd.split(",");
			
			for (i=0;strSysCd.length>i;i++) {
				strQuery.setLength(0);
				strQuery.append("delete cmm0330                \n");
				strQuery.append(" where cm_syscd=?             \n"); 		     		     			
				pstmt = conn.prepareStatement(strQuery.toString());	
				pstmt.setString(1, strSysCd[i]);				
				pstmt.executeUpdate();
				pstmt.close();
			}
		
			conn.close();
			conn = null;
			pstmt = null;
            
            return 0;            
            
		} catch (SQLException sqlexception) {
			sqlexception.printStackTrace();
			ecamsLogger.error("## Cmm0700.delTab6Info() SQLException START ##");
			ecamsLogger.error("## Error DESC : ", sqlexception);	
			ecamsLogger.error("## Cmm0700.delTab6Info() SQLException END ##");			
			throw sqlexception;
		} catch (Exception exception) {
			exception.printStackTrace();
			ecamsLogger.error("## Cmm0700.delTab6Info() Exception START ##");				
			ecamsLogger.error("## Error DESC : ", exception);	
			ecamsLogger.error("## Cmm0700.delTab6Info() Exception END ##");				
			throw exception;
		}finally{
			if (strQuery != null) 	strQuery = null;
			if (pstmt != null)  try{pstmt.close();}catch (Exception ex2){ex2.printStackTrace();}
			if (conn != null){
				try{
					ConnectionResource.release(conn);
				}catch(Exception ex3){
					ecamsLogger.error("## Cmm0700.delTab6Info() connection release exception ##");
					ex3.printStackTrace();
				}
			}
		}
	}//end of delTab6Info() method statement 
    
    public int addTab7Info(String Year,String Day) throws SQLException, Exception {
		Connection        conn        = null;
		PreparedStatement pstmt       = null;
		StringBuffer      strQuery    = new StringBuffer();
		ConnectionContext connectionContext = new ConnectionResource();
		int               i = 0;
		
		try {
			conn = connectionContext.getConnection(); 
			
			strQuery.setLength(0);
			strQuery.append("delete cmm0017                \n");
			strQuery.append(" where cm_year=?             \n"); 		     		     			
			pstmt = conn.prepareStatement(strQuery.toString());	
			pstmt.setString(1, Year);				
			pstmt.executeUpdate();
			pstmt.close();
			
			strQuery.setLength(0);
			strQuery.append("insert into cmm0017                                 \n");
			strQuery.append(" (cm_year,cm_monthday)								 \n");
			strQuery.append(" values (?, ?)			                             \n");
			pstmt = conn.prepareStatement(strQuery.toString());
			pstmt.setString(1, Year);
			pstmt.setString(2, Day);
			pstmt.executeUpdate();
			pstmt.close();
		
			conn.close();
			conn = null;
			pstmt = null;
            
            return 0;
            
            
		} catch (SQLException sqlexception) {
			sqlexception.printStackTrace();
			ecamsLogger.error("## Cmm0700.addTab6Info() SQLException START ##");
			ecamsLogger.error("## Error DESC : ", sqlexception);	
			ecamsLogger.error("## Cmm0700.addTab6Info() SQLException END ##");			
			throw sqlexception;
		} catch (Exception exception) {
			exception.printStackTrace();
			ecamsLogger.error("## Cmm0700.addTab6Info() Exception START ##");				
			ecamsLogger.error("## Error DESC : ", exception);	
			ecamsLogger.error("## Cmm0700.addTab6Info() Exception END ##");				
			throw exception;
		}finally{
			if (strQuery != null) 	strQuery = null;
			if (pstmt != null)  try{pstmt.close();}catch (Exception ex2){ex2.printStackTrace();}
			if (conn != null){
				try{
					ConnectionResource.release(conn);
				}catch(Exception ex3){
					ecamsLogger.error("## Cmm0700.addTab6Info() connection release exception ##");
					ex3.printStackTrace();
				}
			}
		}
	}//end of addTab6Info() method statement  
    
    public Object[] getTab7Info() throws SQLException, Exception {
		Connection        conn        = null;
		PreparedStatement pstmt       = null;
		ResultSet         rs          = null;
		StringBuffer      strQuery    = new StringBuffer();
		HashMap<String, String>			  rst		  = null;
		ArrayList<HashMap<String, String>>  rsval = new ArrayList<HashMap<String, String>>();

		ConnectionContext connectionContext = new ConnectionResource();
		
		
		try {
			conn = connectionContext.getConnection(); 
    
			strQuery.setLength(0);
			strQuery.append("select cm_year, cm_monthday 					\n");
			strQuery.append("from cmm0017  									\n");
		     			
			pstmt = conn.prepareStatement(strQuery.toString());
            rs = pstmt.executeQuery();
           
            rsval.clear();
            while (rs.next()){
            	rst = new HashMap<String, String>();
				rst.put("year", rs.getString("cm_year"));	
				rst.put("monthday", rs.getString("cm_monthday"));
				rsval.add(rst);
				rst = null;
			}
            
            rs.close();
            pstmt.close();
            conn.close();
			conn = null;
			pstmt = null;
			rs = null;
            
            
            return rsval.toArray();
            
            
		} catch (SQLException sqlexception) {
			sqlexception.printStackTrace();
			ecamsLogger.error("## Cmm0700.getTab1Info() SQLException START ##");
			ecamsLogger.error("## Error DESC : ", sqlexception);	
			ecamsLogger.error("## Cmm0700.getTab1Info() SQLException END ##");			
			throw sqlexception;
		} catch (Exception exception) {
			exception.printStackTrace();
			ecamsLogger.error("## Cmm0700.getTab1Info() Exception START ##");				
			ecamsLogger.error("## Error DESC : ", exception);	
			ecamsLogger.error("## Cmm0700.getTab1Info() Exception END ##");				
			throw exception;
		}finally{
			if (strQuery != null) 	strQuery = null;
			if (rst != null)	rst = null;
			if (rs != null)     try{rs.close();}catch (Exception ex){ex.printStackTrace();}
			if (pstmt != null)  try{pstmt.close();}catch (Exception ex2){ex2.printStackTrace();}
			if (conn != null){
				try{
					ConnectionResource.release(conn);
				}catch(Exception ex3){
					ecamsLogger.error("## Cmm0700.getTab1Info() connection release exception ##");
					ex3.printStackTrace();
				}
			}
		}
		
		
	}//end of sql_Qry() method statement    
}//end of Cmm0700 class statement
