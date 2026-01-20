
/*****************************************************************************************
	1. program ID	: PassWdDAO.java
	2. create date	: 2007. 8. 6
	3. auth		    : C.I.S
	4. update date	: 
	5. auth		    : 
	6. description	: 1. 비밀번호 암호화/복호화
*****************************************************************************************/

package com.ecams.service.passwd.dao;



import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.ArrayList;
import java.lang.*;


import org.apache.logging.log4j.Logger;

import com.ecams.common.dbconn.ConnectionContext;
import com.ecams.common.dbconn.ConnectionResource;
import com.ecams.common.logger.EcamsLogger;
import com.ecams.common.base.Encryptor;

import com.ecams.common.base.Encryptor_SHA256;


/**
 * @author bigeyes
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class PassWdDAO{      

    /**
     * Logger Class Instance Creation
     * logger
     */
    Logger ecamsLogger  = EcamsLogger.getLoggerInstance();    
	    

	/**
	 * <PRE>
	 * 1. MethodName	: selectPassWd
	 * 2. ClassName		: PassWdDAO
	 * 3. Commnet			: 사용자 비밀번호 조회
	 * 4. 작성자				: no name
	 * 5. 작성일				: 2011. 1. 6. 오후 3:45:02
	 * </PRE>
	 * 		@return String
	 * 		@param user_id
	 * 		@return
	 * 		@throws SQLException
	 * 		@throws Exception
	 */
	public String selectPassWd(String user_id) throws SQLException, Exception {
		Connection        conn        = null;
		PreparedStatement pstmt       = null;
		ResultSet         rs          = null;
		StringBuffer      strQuery    = new StringBuffer();
		String usr_cpasswd = null;
		String strEnPassWd = null;
		Encryptor oEncryptor = Encryptor.instance();
		ConnectionContext connectionContext = new ConnectionResource();		
		try {
			conn = connectionContext.getConnection();
			strQuery.append("SELECT CM_CPASSWD,CM_DUMYPW FROM CMM0040  ");
			strQuery.append(" WHERE CM_USERID = ? ");
			pstmt = conn.prepareStatement(strQuery.toString());
            pstmt.setString(1, user_id);
            rs = pstmt.executeQuery();	
			if (rs.next()){
				usr_cpasswd = rs.getString("CM_CPASSWD");
				if (usr_cpasswd == null) strEnPassWd = rs.getString("CM_DUMYPW");
				else strEnPassWd = usr_cpasswd;
				//else strEnPassWd = oEncryptor.strGetDecrypt(usr_cpasswd);
				
				
			}//end of while-loop statement
		} catch (SQLException sqlexception) {
			sqlexception.printStackTrace();
			ecamsLogger.error("## PassWdDAO.selectPassWd() SQLException START ##");
			ecamsLogger.error("## Error DESC : ", sqlexception);	
			ecamsLogger.error("## PassWdDAO.selectPassWd() SQLException END ##");			
			throw sqlexception;
		} catch (Exception exception) {
			exception.printStackTrace();
			ecamsLogger.error("## PassWdDAO.selectPassWd() Exception START ##");				
			ecamsLogger.error("## Error DESC : ", exception);	
			ecamsLogger.error("## PassWdDAO.selectPassWd() Exception END ##");				
			throw exception;
		}finally{
			if (rs != null)     try{rs.close();}catch (Exception ex){ex.printStackTrace();}
			if (pstmt != null)  try{pstmt.close();}catch (Exception ex2){ex2.printStackTrace();}
			if (conn != null){
				try{
					ConnectionResource.release(conn);
				}catch(Exception ex3){
					ecamsLogger.error("## PassWdDAO.selectPassWd() connection release exception ##");
					ex3.printStackTrace();
				}
			}
		}
		
		return strEnPassWd;
		
	}//end of selectUserName() method statement
	public String BefPassWd(String user_id) throws SQLException, Exception {
		Connection        conn        = null;
		PreparedStatement pstmt       = null;
		ResultSet         rs          = null;
		StringBuffer      strQuery    = new StringBuffer();
		String usr_cpasswd = null;
		String strEnPassWd = null;
		String RetPass = null;
		Encryptor oEncryptor = Encryptor.instance();
		int Cnt = 0;

		ConnectionContext connectionContext = new ConnectionResource();
		
		try {
			conn = connectionContext.getConnection();
			strQuery.append("SELECT CM_PASSWD FROM CMM0041  ");
			strQuery.append(" WHERE CM_USERID = ? ");
			pstmt = conn.prepareStatement(strQuery.toString());
            pstmt.setString(1, user_id);
            rs = pstmt.executeQuery();
			while (rs.next()){
				usr_cpasswd = rs.getString("CM_PASSWD");
				
				if (usr_cpasswd != null) {
					strEnPassWd = usr_cpasswd;
					//strEnPassWd = oEncryptor.strGetDecrypt(usr_cpasswd);
					if (RetPass != null) RetPass = RetPass + "," + strEnPassWd;
				}
			}//end of while-loop statement
			//return RetPass;
		} catch (SQLException sqlexception) {
			sqlexception.printStackTrace();
			ecamsLogger.error("## PassWdDAO.BefPassWd() SQLException START ##");
			ecamsLogger.error("## Error DESC : ", sqlexception);	
			ecamsLogger.error("## PassWdDAO.BefPassWd() SQLException END ##");			
			throw sqlexception;
		} catch (Exception exception) {
			exception.printStackTrace();
			ecamsLogger.error("## PassWdDAO.BefPassWd() Exception START ##");				
			ecamsLogger.error("## Error DESC : ", exception);	
			ecamsLogger.error("## PassWdDAO.BefPassWd() Exception END ##");				
			throw exception;
		}finally{
			if (rs != null)     try{rs.close();}catch (Exception ex){ex.printStackTrace();}
			if (pstmt != null)  try{pstmt.close();}catch (Exception ex2){ex2.printStackTrace();}
			if (conn != null){
				try{
					ConnectionResource.release(conn);
				}catch(Exception ex3){
					ecamsLogger.error("## PassWdDAO.BefPassWd() connection release exception ##");
					ex3.printStackTrace();
				}
			}
		}
		
		return RetPass;
		
	}//end of selectUserName() method statement
	public int PassWdChk(String user_id,String usr_passwd) throws SQLException, Exception {
		Connection        conn        = null;
		PreparedStatement pstmt       = null;
		ResultSet         rs          = null;
		StringBuffer      strQuery    = new StringBuffer();
		String usr_cpasswd = null;
		String strEnPassWd = null;
		Encryptor oEncryptor = Encryptor.instance();
		int Cnt = 0;

		ConnectionContext connectionContext = new ConnectionResource();
		
		try {
			conn = connectionContext.getConnection();
			strQuery.append("SELECT CM_PASSWD FROM CMM0041  ");
			strQuery.append(" WHERE CM_USERID = ? ");
			pstmt = conn.prepareStatement(strQuery.toString());
            pstmt.setString(1, user_id);
            rs = pstmt.executeQuery();
			while (rs.next()){
				usr_cpasswd = rs.getString("CM_PASSWD");
				
				if (usr_cpasswd != null) {
					strEnPassWd = usr_cpasswd;
					//strEnPassWd = oEncryptor.strGetDecrypt(usr_cpasswd);					
					if (!strEnPassWd.equals(usr_passwd)) return 1;					
				}
			}//end of while-loop statement
			Cnt = 0;
		} catch (SQLException sqlexception) {
			sqlexception.printStackTrace();
			ecamsLogger.error("## PassWdDAO.PassWdChk() SQLException START ##");
			ecamsLogger.error("## Error DESC : ", sqlexception);	
			ecamsLogger.error("## PassWdDAO.PassWdChk() SQLException END ##");			
			throw sqlexception;
		} catch (Exception exception) {
			exception.printStackTrace();
			ecamsLogger.error("## PassWdDAO.PassWdChk() Exception START ##");				
			ecamsLogger.error("## Error DESC : ", exception);	
			ecamsLogger.error("## PassWdDAO.PassWdChk() Exception END ##");				
			throw exception;
		}finally{
			if (rs != null)     try{rs.close();}catch (Exception ex){ex.printStackTrace();}
			if (pstmt != null)  try{pstmt.close();}catch (Exception ex2){ex2.printStackTrace();}
			if (conn != null){
				try{
					ConnectionResource.release(conn);
				}catch(Exception ex3){
					ecamsLogger.error("## PassWdDAO.PassWdChk() connection release exception ##");
					ex3.printStackTrace();
				}
			}
		}
		
		return Cnt;
		
	}//end of selectUserName() method statement
	
	public String encPassWd(String user_id,String usr_passwd) throws SQLException, Exception {
		Connection        conn        = null;
		PreparedStatement pstmt       = null;
		ResultSet         rs          = null;

		try {
			
			Encryptor_SHA256 oEncryptor_sha256 = Encryptor_SHA256.instance();
			String strEnbefPasswd = oEncryptor_sha256.encryptSHA256(usr_passwd); //암호화된 변경전비밀번호

			return strEnbefPasswd;
			
		} catch (Exception exception) {
			exception.printStackTrace();
			ecamsLogger.error("## PassWdDAO.encPassWd() Exception START ##");				
			ecamsLogger.error("## Error DESC : ", exception);	
			ecamsLogger.error("## PassWdDAO.encPassWd() Exception END ##");				
			throw exception;
		}finally{
			if (rs != null)     try{rs.close();}catch (Exception ex){ex.printStackTrace();}
			if (pstmt != null)  try{pstmt.close();}catch (Exception ex2){ex2.printStackTrace();}
			if (conn != null){
				try{
					ConnectionResource.release(conn);
				}catch(Exception ex3){
					ecamsLogger.error("## PassWdDAO.encPassWd() connection release exception ##");
					ex3.printStackTrace();
				}
			}
		}
	}//end of selectUserName() method statement
	
	/**
	 * <PRE>
	 * 1. MethodName	: updtPassWd
	 * 2. ClassName		: PassWdDAO
	 * 3. Commnet			: 비밀번호 업데이트
	 * 4. 작성자				: no name
	 * 5. 작성일				: 2011. 1. 6. 오후 3:46:20
	 * </PRE>
	 * 		@return int
	 * 		@param user_id
	 * 		@param usr_passwd
	 * 		@return
	 * 		@throws SQLException
	 * 		@throws Exception
	 */
	public int updtPassWd(String user_id,String usr_passwd,String bef_passwd) throws SQLException, Exception {
		Connection        conn        = null;
		PreparedStatement pstmt       = null;
		ResultSet         rs          = null;
		StringBuffer      strQuery    = new StringBuffer();
        int               rtn_cnt     = 0;
        
//		Encryptor oEncryptor = Encryptor.instance();
//		String strEnPassWd = oEncryptor.strGetEncrypt(usr_passwd);

		Encryptor_SHA256 oEncryptor_sha256 = Encryptor_SHA256.instance();
		String strEnbefPasswd = oEncryptor_sha256.encryptSHA256(bef_passwd); //암호화된 변경전비밀번호
		String strEnaftPasswd = oEncryptor_sha256.encryptSHA256(usr_passwd); //암호화된 변경후비밀번호
		
		ConnectionContext connectionContext = new ConnectionResource();
		
		try {
			
			conn = connectionContext.getConnection();

            if(strEnaftPasswd.equals(strEnbefPasswd)) {
                conn.close();
                conn = null;
            	return -2;
            }
            
			strQuery.append("UPDATE CMM0040 ");
			strQuery.append("   SET CM_CPASSWD = ?, ");
			strQuery.append("       CM_CHANGEDT = SYSDATE, ");
			strQuery.append("       CM_ERCOUNT = 0 ");
			strQuery.append(" WHERE CM_USERID = ? ");
   		    pstmt = conn.prepareStatement(strQuery.toString());	
            pstmt.setString(1, strEnaftPasswd);
            pstmt.setString(2, user_id);
            rtn_cnt = pstmt.executeUpdate();
            pstmt.close();
            
			strQuery.setLength(0);
			strQuery.append("INSERT INTO CMM0041 ");
			strQuery.append("(CM_USERID, CM_CHANGDT, CM_PASSWD) VALUES \n");
			strQuery.append("(?, SYSDATE, ? )");
   		    pstmt = conn.prepareStatement(strQuery.toString());	
            pstmt.setString(1, user_id);
            pstmt.setString(2, strEnaftPasswd);
            rtn_cnt = pstmt.executeUpdate();
            pstmt.close();
            
            conn.close();
            conn = null;
            pstmt = null;
           
		} catch (SQLException sqlexception) {
			sqlexception.printStackTrace();
			ecamsLogger.error("## PassWdDAO.updtPassWd() SQLException START ##");
			ecamsLogger.error("## Error DESC : ", sqlexception);	
			ecamsLogger.error("## PassWdDAO.updtPassWd() SQLException END ##");			
			throw sqlexception;
		} catch (Exception exception) {
			exception.printStackTrace();
			ecamsLogger.error("## PassWdDAO.updtPassWd() Exception START ##");				
			ecamsLogger.error("## Error DESC : ", exception);	
			ecamsLogger.error("## PassWdDAO.updtPassWd() Exception END ##");				
			throw exception;
		}finally{
			if (rs != null)     try{rs.close();}catch (Exception ex){ex.printStackTrace();}
			if (pstmt != null)  try{pstmt.close();}catch (Exception ex2){ex2.printStackTrace();}
			if (conn != null){
				try{
					ConnectionResource.release(conn);
				}catch(Exception ex3){
					ecamsLogger.error("## PassWdDAO.updtPassWd() connection release exception ##");
					ex3.printStackTrace();
				}
			}
		}
		return rtn_cnt;
		
	}//end of updtPassWd() method statement
	
	
	/**
	 * <PRE>
	 * 1. MethodName	: initPassWd
	 * 2. ClassName		: PassWdDAO
	 * 3. Commnet			: 
	 * 4. 작성자				: no name
	 * 5. 작성일				: 2011. 1. 6. 오후 3:47:44
	 * </PRE>
	 * 		@return int
	 * 		@param user_id
	 * 		@return
	 * 		@throws SQLException
	 * 		@throws Exception
	 */
	public int initPassWd(String user_id) throws SQLException, Exception {
		Connection        conn        = null;
		PreparedStatement pstmt       = null;
		ResultSet         rs          = null;
		StringBuffer      strQuery    = new StringBuffer();
        int               rtn_cnt     = 0;    
        String            usr_passwd  = null;
        
		Encryptor oEncryptor = Encryptor.instance();		
		ConnectionContext connectionContext = new ConnectionResource();		
		try {
			conn = connectionContext.getConnection();
			strQuery.append("SELECT CM_JUMINNUM,CM_DUMYPW FROM CMM0040  ");
			strQuery.append(" WHERE CM_USERID = ? ");
			pstmt = conn.prepareStatement(strQuery.toString());
            pstmt.setString(1, user_id);
            rs = pstmt.executeQuery();	
			if (rs.next()){
				usr_passwd = rs.getString("CM_JUMINNUM");
				if (usr_passwd == null) usr_passwd = rs.getString("CM_DUMYPW");
				String strEnPassWd = oEncryptor.strGetEncrypt(usr_passwd);
				
				strQuery.setLength(0);
				strQuery.append("UPDATE CMM0040 ");
				strQuery.append("   SET CM_CPASSWD = ?, ");
				strQuery.append("       CM_CHANGEDT = SYSDATE, ");
				strQuery.append("       CM_ERCOUNT = 0 ");
				strQuery.append(" WHERE CM_USERID = ? ");
	   		    pstmt = conn.prepareStatement(strQuery.toString());	
	            pstmt.setString(1, strEnPassWd);
	            pstmt.setString(2, user_id);
	            rtn_cnt = pstmt.executeUpdate();
			}//end of while-loop statement
		} catch (SQLException sqlexception) {
			sqlexception.printStackTrace();
			ecamsLogger.error("## PassWdDAO.initPassWd() SQLException START ##");
			ecamsLogger.error("## Error DESC : ", sqlexception);	
			ecamsLogger.error("## PassWdDAO.initPassWd() SQLException END ##");			
			throw sqlexception;
		} catch (Exception exception) {
			exception.printStackTrace();
			ecamsLogger.error("## PassWdDAO.initPassWd() Exception START ##");				
			ecamsLogger.error("## Error DESC : ", exception);	
			ecamsLogger.error("## PassWdDAO.initPassWd() Exception END ##");				
			throw exception;
		}finally{
			if (rs != null)     try{rs.close();}catch (Exception ex){ex.printStackTrace();}
			if (pstmt != null)  try{pstmt.close();}catch (Exception ex2){ex2.printStackTrace();}
			if (conn != null){
				try{
					connectionContext.release();
				}catch(Exception ex3){
					ecamsLogger.error("## PassWdDAO.initPassWd() connection release exception ##");
					ex3.printStackTrace();
				}
			}
		}
		return rtn_cnt;
		
	}//end of initPassWd() method statement

	public Object selectLastPassWd(String user_id) throws SQLException, Exception {
		Connection        conn        = null;
		PreparedStatement pstmt       = null;
		ResultSet         rs          = null;
		StringBuffer      strQuery    = new StringBuffer();
		ArrayList<HashMap<String, String>> rsval = new ArrayList<HashMap<String, String>>();

		HashMap <String ,String>  rst = null;   
		
		String lstPasswd = null;
		String strEnPassWd = null;
		String RetPass = null;
		Encryptor oEncryptor = Encryptor.instance();
		int Cnt = 0;

		ConnectionContext connectionContext = new ConnectionResource();

		try {
			conn = connectionContext.getConnection();
			strQuery.append("SELECT CM_PASSWD FROM (SELECT CM_PASSWD FROM CMM0041 \n");
			strQuery.append("                       WHERE CM_USERID = ?           \n");
			strQuery.append("                       ORDER BY CM_CHANGDT DESC)     \n");
			strQuery.append(" WHERE ROWNUM <= 5                                   \n");
			pstmt = conn.prepareStatement(strQuery.toString()); 
            pstmt.setString(1, user_id);
            rs = pstmt.executeQuery();
			while (rs.next()){
				rst = new HashMap<String,String>();
				lstPasswd = rs.getString("CM_PASSWD");
				
				if (lstPasswd != null) {
					//strEnPassWd = oEncryptor.strGetDecrypt(lstPasswd);
					strEnPassWd = lstPasswd;
				}
				
				rst.put("lst_passwd", strEnPassWd);
				
				rsval.add(rst);
				rst = null;
			}
			//end of while-loop statement
			//return RetPass;
		} catch (SQLException sqlexception) {
			sqlexception.printStackTrace();
			ecamsLogger.error("## PassWdDAO.selectLastPassWd() SQLException START ##");
			ecamsLogger.error("## Error DESC : ", sqlexception);	
			ecamsLogger.error("## PassWdDAO.selectLastPassWd() SQLException END ##");			
			throw sqlexception;
		} catch (Exception exception) {
			exception.printStackTrace();
			ecamsLogger.error("## PassWdDAO.selectLastPassWd() Exception START ##");				
			ecamsLogger.error("## Error DESC : ", exception);	
			ecamsLogger.error("## PassWdDAO.selectLastPassWd() Exception END ##");				
			throw exception;
		}finally{
			if (rs != null)     try{rs.close();}catch (Exception ex){ex.printStackTrace();}
			if (pstmt != null)  try{pstmt.close();}catch (Exception ex2){ex2.printStackTrace();}
			if (conn != null){
				try{
					ConnectionResource.release(conn);
				}catch(Exception ex3){
					ecamsLogger.error("## PassWdDAO.selectLastPassWd() connection release exception ##");
					ex3.printStackTrace();
				}
			}
		}
		
		return rsval.toArray();

		
	}//end of selectLastPassWd() method statement		

}//end of MemberDAO class statement
