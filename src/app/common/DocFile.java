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

public class DocFile {
	Logger ecamsLogger  = EcamsLogger.getLoggerInstance();

	public String setDocFile(ArrayList<HashMap<String,String>> fileList) throws SQLException, Exception {
		Connection        conn        = null;
		PreparedStatement pstmt       = null;
		ResultSet         rs          = null;
		StringBuffer      strQuery    = new StringBuffer();
		ConnectionContext connectionContext = new ConnectionResource();
		String			  rtString = null;
		try {
			conn = connectionContext.getConnection();

			for (int i=0;i<fileList.size();i++){
				strQuery.setLength(0);
				strQuery.append("insert into cmr1001 (cr_acptno,cr_gubun,cr_seqno,cr_filename,cr_reldoc) values ( \n");
				strQuery.append(" ? , ? , ? , ? , ? ) \n");
				
				pstmt = conn.prepareStatement(strQuery.toString());
				
				pstmt.setString(1, fileList.get(i).get("acptno"));
				pstmt.setString(2, fileList.get(i).get("filegb"));
				pstmt.setInt(3, Integer.parseInt(fileList.get(i).get("seq")));
				pstmt.setString(4, fileList.get(i).get("realName"));
				pstmt.setString(5, fileList.get(i).get("saveName"));

	            pstmt.executeUpdate();
	            pstmt.close();
			}
			conn.close();
			return "ok";
	
		} catch (SQLException sqlexception) {
			sqlexception.printStackTrace();
			ecamsLogger.error("## SystemPath.getSysPath() SQLException START ##");
			ecamsLogger.error("## Error DESC : ", sqlexception);	
			ecamsLogger.error("## SystemPath.getSysPath() SQLException END ##");			
			throw sqlexception;
		} catch (Exception exception) {
			exception.printStackTrace();
			ecamsLogger.error("## SystemPath.getSysPath() Exception START ##");				
			ecamsLogger.error("## Error DESC : ", exception);	
			ecamsLogger.error("## SystemPath.getSysPath() Exception END ##");				
			throw exception;
		}finally{
			if (strQuery != null) 	strQuery = null;
			if (rs != null)     try{rs.close();}catch (Exception ex){ex.printStackTrace();}
			if (pstmt != null)  try{pstmt.close();}catch (Exception ex2){ex2.printStackTrace();}
			if (conn != null){
				try{
					ConnectionResource.release(conn);
				}catch(Exception ex3){
					ecamsLogger.error("## Cmr0100.request_Check_Out() connection release exception ##");
					ex3.printStackTrace();
				}
			}
		}
		
		
	}//end of SelectUserInfo() method statement
	
	public String delDocFile(String AcptNo,String GuBun,int SeqNo) throws SQLException, Exception {
		Connection        conn        = null;
		PreparedStatement pstmt       = null;
		StringBuffer      strQuery    = new StringBuffer();
		ConnectionContext connectionContext = new ConnectionResource();
		
		try {
			conn = connectionContext.getConnection();
			
			strQuery.setLength(0);
			strQuery.append("Delete from cmr1001 ");
			strQuery.append("where cr_acptno=? and cr_gubun=? and cr_seqno=? ");
			
			pstmt = conn.prepareStatement(strQuery.toString());
			pstmt.setString(1, AcptNo);
			pstmt.setString(2, GuBun);
			pstmt.setInt(3, SeqNo);
            pstmt.executeUpdate();
            pstmt.close();
			conn.close();
			return "ok";

		} catch (SQLException sqlexception) {
			sqlexception.printStackTrace();
			ecamsLogger.error("## DocFile.delDocFile() SQLException START ##");
			ecamsLogger.error("## Error DESC : ", sqlexception);
			ecamsLogger.error("## DocFile.delDocFile() SQLException END ##");
			throw sqlexception;
		} catch (Exception exception) {
			exception.printStackTrace();
			ecamsLogger.error("## DocFile.delDocFile() Exception START ##");
			ecamsLogger.error("## Error DESC : ", exception);
			ecamsLogger.error("## DocFile.delDocFile() Exception END ##");
			throw exception;
		}finally{
			if (strQuery != null) 	strQuery = null;
			if (pstmt != null)  try{pstmt.close();}catch (Exception ex2){ex2.printStackTrace();}
			if (conn != null){
				try{
					ConnectionResource.release(conn);
				}catch(Exception ex3){
					ecamsLogger.error("## DocFile.delDocFile() connection release exception ##");
					ex3.printStackTrace();
				}
			}
		}
	}//end of delDocFile() method statement
	
}
