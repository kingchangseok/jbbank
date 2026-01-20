package app.eCmr;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.logging.log4j.Logger;
import app.common.LoggableStatement;
import com.ecams.common.dbconn.ConnectionContext;
import com.ecams.common.dbconn.ConnectionResource;
import com.ecams.common.logger.EcamsLogger;

public class Cmr0260{
 
    Logger ecamsLogger  = EcamsLogger.getLoggerInstance(); 

    public Object[] get_sql_Qry(String RsrcName,String UserId) throws SQLException, Exception {
		Connection        conn        = null;
		PreparedStatement pstmt       = null;
		ResultSet         rs          = null;
		StringBuffer      strQuery    = new StringBuffer();
		ArrayList<HashMap<String, String>>  rtList	= new ArrayList<HashMap<String, String>>();
		HashMap<String, String>			  	rst	 	= null;
        Object[]		 rtObj		  = null;
		
		//ConnectionContext connectionContext1 = new ConnectionResource();
		ConnectionContext connectionContext2 = new ConnectionResource(true,"P");//√Îæ‡¡°
		try {
			conn = connectionContext2.getConnection();
			strQuery.setLength(0);
			strQuery.append("SELECT PRJ.NAME,PRJV.NAME,DOCUMENTINFO.UPLOADDATE,SCAN.STARTDATE,DEFISSUE.FRIORITY, \n");
			strQuery.append("       DEFISSUE.ISSUETYPE,DEFISSUE.ISSUESUBTYPE,DEFISSUE.FILENAME AS FILENAME, \n");
			strQuery.append("       DEFISSUE.LINENUMBER, RULEVIEW.DETAIL, RULEVIEW.RECOMMENDATION \n");
			strQuery.append("  FROM PROJECT PRJ, PROJECTVERSION PRJV, DEFAULTISSUEVIEW DEFISSUE, RULEVIEW, SCAN, ARTIFACT, DOCUMENTINFO \n");
			strQuery.append(" WHERE PRJ.ID = PRJV.PROJECT_ID \n");
			strQuery.append("   AND PRJV.ID = DEFISSUE.PROJECTVERSION_ID \n");
			strQuery.append("   AND PRJV.ID = RULEVIEW.PROJECTVERSION_ID \n");
			strQuery.append("   AND PRJV.ID = SCAN.PROJECTVERSION_ID \n");
			strQuery.append("   AND PRJV.ID = ARTIFACT.PROJECTVERSION_ID \n");
			strQuery.append("   AND DEFISSUE.RULEGUID = RULEVIEW.RULEGUID \n");
			strQuery.append("   AND DEFISSUE.SUPPRESSED = 'N' \n");
			strQuery.append("   AND DEFISSUE.HIDDEN = 'N' \n");
			strQuery.append("   AND DEFISSUE.SCANSTATUS <> 'REMOVED' \n");
			strQuery.append("   AND DEFISSUE.LASTSCAN_ID = SCAN.ID \n");
			strQuery.append("   AND SCAN.ARTIFACT_ID = ARTIFACT.ID \n");
			strQuery.append("   AND ARTIFACT.DOCUMENTINFO_ID = DOCUMENTINFO.ID \n");
			
			pstmt = conn.prepareStatement(strQuery.toString());
			//pstmt = new LoggableStatement(conn,strQuery.toString());
			//ecamsLogger.error(((LoggableStatement)pstmt).getQueryString());
            rs = pstmt.executeQuery();            
            rtList.clear();
            while (rs.next()){
            	rst = new HashMap<String, String>();
            	
            	rst.put("PRJ.NAME", rs.getString("PRJ.NAME"));
				rst.put("PRJV.NAME", rs.getString("PRJV.NAME"));
				rst.put("DOCUMENTINFO.UPLOADDATE", rs.getString("DOCUMENTINFO.UPLOADDATE"));
				rst.put("SCAN.STARTDATE", rs.getString("SCAN.STARTDATE"));
				rst.put("DEFISSUE.FRIORITY", rs.getString("DEFISSUE.FRIORITY"));
				rst.put("DEFISSUE.ISSUETYPE", rs.getString("DEFISSUE.ISSUETYPE"));
				rst.put("DEFISSUE.ISSUESUBTYPE", rs.getString("DEFISSUE.ISSUESUBTYPE"));
				rst.put("DEFISSUE.FILENAME", rs.getString("DEFISSUE.FILENAME"));
				rst.put("DEFISSUE.LINENUMBER", rs.getString("DEFISSUE.LINENUMBER"));
				rst.put("RULEVIEW.DETAIL", rs.getString("RULEVIEW.DETAIL"));
				rst.put("RULEVIEW.RECOMMENDATION", rs.getString("RULEVIEW.RECOMMENDATION"));
				rtList.add(rst);
				rst = null;
            }
            rs.close();
            pstmt.close();
            conn.close();
            rs = null;
            pstmt = null;
            conn = null;
            
            rtObj =  rtList.toArray();
    		
    		rtList = null;
    		
    		return rtObj;            
			
		} catch (SQLException sqlexception) {
			sqlexception.printStackTrace();
			ecamsLogger.error("## Cmr0260.sql_Qry() SQLException START ##");
			ecamsLogger.error("## Error DESC : ", sqlexception);	
			ecamsLogger.error("## Cmm0260.sql_Qry() SQLException END ##");			
			throw sqlexception;
		} catch (Exception exception) {
			exception.printStackTrace();
			ecamsLogger.error("## Cmr0260.sql_Qry() Exception START ##");				
			ecamsLogger.error("## Error DESC : ", exception);	
			ecamsLogger.error("## Cmr0260.sql_Qry() Exception END ##");				
			throw exception;
		} finally{
			if (strQuery != null) 	strQuery = null;
			if (rtObj != null)	rtObj = null;
			if (rs != null)     try{rs.close();}catch (Exception ex){ex.printStackTrace();}
			if (pstmt != null)  try{pstmt.close();}catch (Exception ex2){ex2.printStackTrace();}
			if (conn != null){
				try{
					ConnectionResource.release(conn);
				}catch(Exception ex3){
					ecamsLogger.error("## Cmm0260.sql_Qry() connection release exception ##");
					ex3.printStackTrace();
				}
			}
		}
	}
    
}