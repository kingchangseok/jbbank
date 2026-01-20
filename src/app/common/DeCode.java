package app.common;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
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



public class DeCode {
	
	Logger ecamsLogger  = EcamsLogger.getLoggerInstance();
	
	
	public int sendFile( String UserId, String FileName) throws SQLException, Exception {
		SystemPath		  cTempGet	  = new SystemPath();
		String			  tmpPath = "";
		String			  docPath = "";
		String			  strBinPath = "";
		Connection        conn        = null;
		ResultSet         rs          = null;
		PreparedStatement pstmt       = null;
		StringBuffer      strQuery    = new StringBuffer();
		
		//String           strDevHome   = null;
		ConnectionContext connectionContext = new ConnectionResource();
		
		File shfile=null;
		String  shFileName = "";
	
		OutputStreamWriter writer = null;
		String[] strAry = null;
		Runtime  run = null;
		Process p = null;
		int rtn = 0;
		String rtString = "";
		try {
	
			tmpPath = cTempGet.getTmpDir("99");
			strBinPath = cTempGet.getTmpDir("14");
			docPath = cTempGet.getTmpDir("21");
			shFileName = tmpPath + "/" + UserId + "_Cmm1600.sh"; 
			shfile = new File(shFileName);
			
			if( !(shfile.isFile()) )              //File이 없으면 
			{
				shfile.createNewFile();          //File 생성
			}
			
			// 20221219 ecams_batexec 추가 쿼리
			conn = connectionContext.getConnection();
			conn.setAutoCommit(false);
			strQuery.setLength(0);
			strQuery.append("select cm_ipaddr, cm_port 	\n");
			strQuery.append("  from cmm0010 			\n");
			strQuery.append(" where cm_stno = 'ECAMS'	\n");
			
			pstmt = conn.prepareStatement(strQuery.toString());	
			rs = pstmt.executeQuery();
			if(rs.next()){
				rtString = rs.getString("cm_ipaddr") + " " + rs.getString("cm_port") + " 0";
			}
			
			writer = new OutputStreamWriter( new FileOutputStream(shFileName));
			writer.write("cd "+strBinPath +"\n");
//			writer.write("rtval=`./ecams_fdsextract " + FileName + "`\n");
			writer.write("./ecams_batexec " +rtString + " \"cd " + strBinPath+ ";" +"./ecams_fdsextract " + FileName + "\" \n");
//			writer.write("exit $rtval\n");
			writer.write("exit $?\n");
			writer.close();
			strAry = new String[3];
			strAry[0] = "chmod";
			strAry[1] = "777";
			strAry[2] = shFileName;			
			
			run = Runtime.getRuntime();
			
			p = run.exec(strAry);
			p.waitFor();
			
			run = Runtime.getRuntime();
			
			strAry = new String[2];
			
			strAry[0] = "/bin/sh";
			
			strAry[1] = shFileName;
			
			p = run.exec(strAry);
			p.waitFor();
			
				
				
			if(p.exitValue() != 0){
				throw new Exception("파일 전송 실패 : [FileName : "+FileName + " ]" );
			}
		
			
			pstmt.close();
			conn.close();
			pstmt = null;
			conn = null;
			rs.close();// 20221219 ecams_batexec 추가 쿼리
			rs = null;
			return rtn;
			
		} catch (SQLException sqlexception) {
			sqlexception.printStackTrace();
			ecamsLogger.error("## DeCode.sendFile() SQLException START ##");
			ecamsLogger.error("## Error DESC : ", sqlexception);	
			ecamsLogger.error("## DeCode.sendFile() SQLException END ##");			
			throw sqlexception;
		} catch (Exception exception) {
			exception.printStackTrace();
			ecamsLogger.error("## DeCode.sendFile() Exception START ##");				
			ecamsLogger.error("## Error DESC : ", exception);	
			ecamsLogger.error("## DeCode.sendFile() Exception END ##");				
			throw exception;
		}finally{
			if (strQuery != null) 	strQuery = null;
			
				
			if (pstmt != null)  try{pstmt.close();}catch (Exception ex2){ex2.printStackTrace();}
			if (conn != null){
				try{
					ConnectionResource.release(conn);
				}catch(Exception ex3){
					ecamsLogger.error("## DeCode.sendFile() connection release exception ##");
					ex3.printStackTrace();
				}
			}
		}
	}//end of copyDoc() method statement
	

}
