
/*****************************************************************************************
	1. program ID	: FtpDAO.java
	2. create date	: 2007.11. 17
	3. auth		    : changsuk.kang
	4. update date	: 
	5. auth		    : 
	6. description	: 1. 메인화면 및  메뉴 DAO
*****************************************************************************************/

package com.ecams.service.ftp.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.logging.log4j.Logger;

import com.ecams.common.dbconn.ConnectionContext;
import com.ecams.common.dbconn.ConnectionResource;
import com.ecams.common.logger.EcamsLogger;
import com.ecams.common.base.eCAMSFtp; 
/**
 * @author bigeyes 
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class FtpDAO{
	 

    /**
     * Logger Class Instance Creation
     * logger
     */
    Logger ecamsLogger  = EcamsLogger.getLoggerInstance();
    
	/**
	 * @param  req_str
	 * @return ArrayList
	 * @throws SQLException
	 * @throws Exception
	 */

	public int SelectFile(String GbnCd,String AcptNo,String tblNm) throws SQLException, Exception {
		PreparedStatement pstmt       = null;
		ResultSet         rs          = null;
		StringBuffer      strQuery    = new StringBuffer();
        int               rtn_cnt     = 0;
        Connection        conn        = null;      
		String            filename    = null;      
		String            local_file  = null; 
		String            ServerIP    = null;
		String            loginID     = null;
		String            passwd      = null;		
        String  		  serverPath  = null;
        boolean  		  ascII       = true;
        String            local_DIR   = null;  
        boolean           up_result   = false;       
  		boolean con_result=false;

  		eCAMSFtp ecamsftp = null; 
		ConnectionContext connectionContext = new ConnectionResource();
		eCAMSFtp oecamsftp = eCAMSFtp.instance();

 
		try { 			
			conn = connectionContext.getConnection();
			strQuery.append("select a.*, b.* from cmm0010 a, cmm0012 b ");
			strQuery.append("where a.cm_stno = 'ECAMS' ");
			strQuery.append("  and a.cm_stno = b.cm_stno ");
			strQuery.append("  and b.cm_pathcd = ? ");		
			pstmt = conn.prepareStatement(strQuery.toString());
			pstmt.setString(1, GbnCd);
    		rs = pstmt.executeQuery();
			if (rs.next()){
				ServerIP = rs.getString("cm_downip");
				loginID = rs.getString("cm_downusr");
				passwd = rs.getString("cm_downpass");
				serverPath = rs.getString("cm_path");				
			}//end of while-loop statement
			strQuery.setLength(0);

			System.out.println("ServerIP   " + ServerIP);
			System.out.println("loginID   " +loginID);
			System.out.println("passwd   " +passwd);
			
			con_result = oecamsftp.ConnetServer(ServerIP,loginID,passwd);
			
			if (!con_result) 
				System.out.println("con_result   " +con_result);
			
			if (tblNm.equals("eCmr1001")) {
				strQuery.append("SELECT * from cmr1001 where cr_acptno = ? ");
				pstmt = conn.prepareStatement(strQuery.toString());
				pstmt.setString(1, AcptNo);
				rs = pstmt.executeQuery();
				
				if (rs.next()){
					filename = rs.getString("cr_reldoc");
					local_file = rs.getString("cr_filename");				
	
					System.out.println("filename11111111   " +   filename);
					System.out.println("local_file111111   " + local_file);
					up_result = oecamsftp.UploadFile(serverPath,filename,ascII,local_file);
					
				}//end of while-loop statement
			} else if (tblNm.equals("eCmr1002")){
				strQuery.append("SELECT * from cmr1002 where cr_acptno = ? ");
				pstmt = conn.prepareStatement(strQuery.toString());
				pstmt.setString(1, AcptNo);
				rs = pstmt.executeQuery();
				
				if (rs.next()){
					filename = rs.getString("cr_reldoc");
					local_file = rs.getString("cr_filename");				
	
					System.out.println("filename11111111   " +   filename);
					System.out.println("local_file111111   " + local_file);
					up_result = oecamsftp.UploadFile(serverPath,filename,ascII,local_file);
					
				}//end of while-loop statement
			} else if (tblNm.equals("eCmd0035")){
				strQuery.append("SELECT * from cmd0035 where cd_acptno = ? ");
				pstmt = conn.prepareStatement(strQuery.toString());
				pstmt.setString(1, AcptNo);
				rs = pstmt.executeQuery();
				
				if (rs.next()){
					filename = rs.getString("cr_reldoc");
					local_file = rs.getString("cr_filename");				
	
					up_result = oecamsftp.UploadFile(serverPath,filename,ascII,local_file);
					
				}//end of while-loop statement
			}
			oecamsftp.CloseServer();
			
		} catch (SQLException sqlexception) {
			sqlexception.printStackTrace();
			ecamsLogger.error("## FtpDAO.SelectFile() SQLException START ##");
			ecamsLogger.error("## Error DESC : ", sqlexception);	
			ecamsLogger.error("## FtpDAO.SelectFile() SQLException END ##");			
			throw sqlexception;
		} catch (Exception exception) {
			exception.printStackTrace();
			ecamsLogger.error("## FtpDAO.SelectFile() Exception START ##");	
			ecamsLogger.error("## FtpDAO.SelectFile() Exception END ##");				
			throw exception;
		}finally{
			if (rs != null)     try{rs.close();}catch (Exception ex){ex.printStackTrace();}
			if (pstmt != null)  try{pstmt.close();}catch (Exception ex2){ex2.printStackTrace();}
			if (conn != null){
				try{
					connectionContext.release();
				}catch(Exception ex3){
					ecamsLogger.error("## FtpDAO.SelectFile() connection release exception ##");
					ex3.printStackTrace();
				}
			}
		}		
		return rtn_cnt;
		
	}//end of insertRefAnalList() method statement
	public int FileRead(String GbnCd,String localFile,String remoteDir,String remoteFile) throws SQLException, Exception {
		PreparedStatement pstmt       = null;
		ResultSet         rs          = null;
		StringBuffer      strQuery    = new StringBuffer();
        int               rtn_cnt     = 0;
        Connection        conn        = null; 
		String            ServerIP    = null;
		String            loginID     = null;
		String            passwd      = null; 
		String            dataHome     = null;      
  		boolean           con_result  = false;     
  		boolean           up_result   = false;
  		System.out.println("GbnCd     ++++++++  " +GbnCd);
  		System.out.println("localFile ++++++++  " +localFile);
  		System.out.println("remoteDir ++++++++  " +remoteDir);
  		System.out.println("remoteDir ++++++++  " +remoteDir);
  		eCAMSFtp ecamsftp = null; 
		ConnectionContext connectionContext = new ConnectionResource();
		eCAMSFtp oecamsftp = eCAMSFtp.instance();
 
		try { 			
			conn = connectionContext.getConnection();
			strQuery.append("select b.cm_downip,b.cm_downusr,b.cm_downpass,b.cm_path ");
			strQuery.append("from cmm0010 a, cmm0012 b ");
			strQuery.append("where a.cm_stno = 'ECAMS' ");
			strQuery.append("  and a.cm_stno = b.cm_stno ");
			strQuery.append("  and b.cm_pathcd = ? ");		
			pstmt = conn.prepareStatement(strQuery.toString());
			pstmt.setString(1, GbnCd);
    		rs = pstmt.executeQuery();

      		System.out.println("strQuery ++++++++  " +strQuery);
			if (rs.next()){
				ServerIP = rs.getString("cm_downip");
				loginID = rs.getString("cm_downusr");
				passwd = rs.getString("cm_downpass");	
				dataHome = rs.getString("cm_path");			
			}//end of while-loop statement
			
			con_result = oecamsftp.ConnetServer(ServerIP,loginID,passwd);
			
			if (!con_result) 
				System.out.println("con_result   " +con_result);
			
			up_result = oecamsftp.downloadFile(dataHome + remoteDir,remoteFile,false,localFile);
			oecamsftp.CloseServer();
			if (!up_result){ 
				System.out.println("up_result   " + up_result);	
				rtn_cnt = 2;
			} else {
			   rtn_cnt = 0;
			}
			
		} catch (SQLException sqlexception) {
			sqlexception.printStackTrace();
			ecamsLogger.error("## FtpDAO.FileRead() SQLException START ##");
			ecamsLogger.error("## Error DESC : ", sqlexception);	
			ecamsLogger.error("## FtpDAO.FileRead() SQLException END ##");			
			throw sqlexception;
		} catch (Exception exception) {
			exception.printStackTrace();
			ecamsLogger.error("## FtpDAO.FileRead() Exception START ##");	
			ecamsLogger.error("## FtpDAO.FileRead() Exception END ##");				
			throw exception;
		}finally{
			if (rs != null)     try{rs.close();}catch (Exception ex){ex.printStackTrace();}
			if (pstmt != null)  try{pstmt.close();}catch (Exception ex2){ex2.printStackTrace();}
			if (conn != null){
				try{
					connectionContext.release();
				}catch(Exception ex3){
					ecamsLogger.error("## FtpDAO.FileRead() connection release exception ##");
					ex3.printStackTrace();
				}
			}
		}		
		return rtn_cnt;
		
	}//end of insertRefAnalList() method statement

}//end of RefAnalDAO class statement
