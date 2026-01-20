
/*****************************************************************************************
	1. program ID	: eCAMSInfo.java
	2. create date	: 2007. 07. 23
	3. auth		    : k.m.s
	4. update date	: 
	5. auth		    : 
	6. description	: eCAMS Information
*****************************************************************************************/

package app.common;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import org.apache.logging.log4j.Logger;
import com.ecams.common.dbconn.ConnectionContext;
import com.ecams.common.dbconn.ConnectionResource;
import com.ecams.common.logger.EcamsLogger;


/**
 * @author bigeyes
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */


public class eCAMSInfo{
	
    /**
     * Logger Class Instance Creation
     * logger
     */
    Logger ecamsLogger  = EcamsLogger.getLoggerInstance();
    
	/**
	 * 게시판을 조회합니다.(구분값을 주어)
	 * @param  gbn
	 * @return ArrayList
	 * @throws SQLException
	 * @throws Exception
	 */
    

	public String getFileInfo(String GbnCd) throws SQLException, Exception {
		Connection        conn        = null;
		PreparedStatement pstmt       = null;
		ResultSet         rs          = null;
		StringBuffer      strQuery    = new StringBuffer();
		String            strPath     = null;
		ConnectionContext connectionContext = new ConnectionResource();
		
		try {
			conn = connectionContext.getConnection();
			
			strQuery.append("select cm_path from cmm0012                     \n");
			strQuery.append("where cm_pathcd=?                               \n");		
            pstmt = conn.prepareStatement(strQuery.toString());	
            pstmt.setString(1, GbnCd);	
            rs = pstmt.executeQuery();
			if (rs.next()){
				strPath = rs.getString("cm_path");
				if (!strPath.substring(strPath.length() - 1, strPath.length()).equals("/"))
				    strPath = strPath + "/";	
			}//end of while-loop statement	
			
			rs.close();
			pstmt.close();
			conn.close();
			
			return strPath;
			
		} catch (SQLException sqlexception) {
			sqlexception.printStackTrace();
			ecamsLogger.error("## eCAMSInfo.getFileInfo() SQLException START ##");
			ecamsLogger.error("## Error DESC : ", sqlexception);	
			ecamsLogger.error("## eCAMSInfo.getFileInfo() SQLException END ##");			
			throw sqlexception;
		} catch (Exception exception) {
			exception.printStackTrace();
			ecamsLogger.error("## eCAMSInfo.getFileInfo() Exception START ##");				
			ecamsLogger.error("## Error DESC : ", exception);	
			ecamsLogger.error("## eCAMSInfo.getFileInfo() Exception END ##");				
			throw exception;
		}finally{
			if (strQuery != null) 	strQuery = null;
			if (rs != null)     try{rs.close();}catch (Exception ex){ex.printStackTrace();}
			if (pstmt != null)  try{pstmt.close();}catch (Exception ex2){ex2.printStackTrace();}
			if (conn != null){
				try{
					ConnectionResource.release(conn);
				}catch(Exception ex3){
					ecamsLogger.error("## eCAMSInfo.getFileInfo() connection release exception ##");
					ex3.printStackTrace();
				}
			}
		}
	}//end of getFileInfo() method statement

	public String getFileInfo_conn(String GbnCd,Connection conn) throws SQLException, Exception {
		PreparedStatement pstmt       = null;
		ResultSet         rs          = null;
		StringBuffer      strQuery    = new StringBuffer();
		String            strPath     = null;
		
		try {
			strQuery.setLength(0);
			strQuery.append("select cm_path from cmm0012                     \n");
			strQuery.append("where cm_pathcd=?                               \n");		
            pstmt = conn.prepareStatement(strQuery.toString());	
            pstmt.setString(1, GbnCd);	
            rs = pstmt.executeQuery();
			if (rs.next()){
				strPath = rs.getString("cm_path");
				if (!strPath.substring(strPath.length() - 1, strPath.length()).equals("/"))
				    strPath = strPath + "/";	
			}//end of while-loop statement	
			
			rs.close();
			pstmt.close();
			rs = null;
			pstmt = null;
			
			return strPath;
			
			
		} catch (SQLException sqlexception) {
			sqlexception.printStackTrace();
			ecamsLogger.error("## eCAMSInfo.getFileInfo_conn() SQLException START ##");
			ecamsLogger.error("## Error DESC : ", sqlexception);	
			ecamsLogger.error("## eCAMSInfo.getFileInfo_conn() SQLException END ##");			
			throw sqlexception;
		} catch (Exception exception) {
			exception.printStackTrace();
			ecamsLogger.error("## eCAMSInfo.getFileInfo_conn() Exception START ##");				
			ecamsLogger.error("## Error DESC : ", exception);	
			ecamsLogger.error("## eCAMSInfo.getFileInfo_conn() Exception END ##");				
			throw exception;
		}finally{
			if (strQuery != null) 	strQuery = null;
			if (rs != null)     try{rs.close();}catch (Exception ex){ex.printStackTrace();}
			if (pstmt != null)  try{pstmt.close();}catch (Exception ex2){ex2.printStackTrace();}
		}
	}//end of getFileInfo_conn() method statement
	
}//end of eCAMSInfo class statement
