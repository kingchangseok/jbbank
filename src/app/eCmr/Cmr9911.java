
/*****************************************************************************************
	1. program ID	: eCmr9911.java
	2. create date	: 
	3. auth		    : 
	4. update date	: 11.03.28
	5. auth		    : 
	6. description	: eCmr9911
******************************************************************************************/

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


/**
 * @author bigeyes
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */


public class Cmr9911{
    /**
     * Logger Class Instance Creation
     * logger
     */ 
    Logger ecamsLogger  = EcamsLogger.getLoggerInstance();
    
	/**
	 * project 등록
	 *
	 * @return String
	 * @throws SQLException
	 * @throws Exception
	 */
    
      public Object[] get_select_grid(String Cbo_Find,String Text_Find,String UserId) throws SQLException, Exception 					{
    	Connection			conn		= null;
    	PreparedStatement	pstmt		= null;
		ResultSet         	rs          = null;  	
		StringBuffer      	strQuery    = new StringBuffer();
		HashMap<String, String>			  	rst	 	= null;
		ArrayList<HashMap<String, String>>  rtList	= new ArrayList<HashMap<String, String>>();
		Object[]		 rtObj		  = null;	
	    int              Cnt          = 0;
    	
		ConnectionContext connectionContext = new ConnectionResource();
		
		try{
			conn = connectionContext.getConnection();
			
			strQuery.setLength(0);
			strQuery.append("select a.CR_TITLE CR_TITLE,a.CR_CONUSR CR_CONUSR,c.cm_username CONUSRNAME,a.CR_SENDUSR CR_SENDUSR, \n");
			strQuery.append("       b.cm_username SENDUSRNAME,a.CR_SENDDATE CR_SENDDATE,a.CR_SGNMSG CR_SGNMSG\n");
			strQuery.append("  from cmr9910 a,cmm0040 b,cmm0040 c \n");
		
			if (Cbo_Find.equals("00")){
				strQuery.append("where a.CR_ACPTNO='999999999999' \n");
			}else if (Cbo_Find.equals("01")){
				strQuery.append("where instr(a.cr_title,?) > 0 \n");
			}else if (Cbo_Find.equals("02")){
				strQuery.append("where (a.CR_SENDUSR = ? or b.cm_username = ?) \n");
			}else {//(Cbo_Find.equals("03")){
				strQuery.append("where (a.CR_CONUSR = ? or c.cm_username = ?) \n");
			}
			strQuery.append("   and a.CR_SENDUSR = b.cm_userid \n");
			strQuery.append("   and a.CR_CONUSR = c.cm_userid \n");
			
			pstmt = conn.prepareStatement(strQuery.toString());
			pstmt = new LoggableStatement(conn,strQuery.toString());
			Cnt = 0;
			if (Cbo_Find.equals("01")){
				pstmt.setString(++Cnt, Text_Find);
			}else if (Cbo_Find.equals("02") || Cbo_Find.equals("03")){
				pstmt.setString(++Cnt, Text_Find);
				pstmt.setString(++Cnt, Text_Find);
			}
			/*
			if (!Cbo_Find.equals("00")){
				pstmt.setString(++Cnt, Text_Find);
			}else if (Cbo_Find.equals("01")){
				pstmt.setString(++Cnt, Text_Find);
			}else if (Cbo_Find.equals("02")){
				pstmt.setString(++Cnt, Text_Find);
			}else {
				pstmt.setString(++Cnt, Text_Find);
			}
			*/
			ecamsLogger.error(((LoggableStatement)pstmt).getQueryString());
			rs = pstmt.executeQuery();
			rtList.clear();
			
			while (rs.next()) {
				rst = new HashMap<String, String>(); 
				rst.put("rows", Integer.toString(rs.getRow()));    // NO
				rst.put("CR_TITLE", rs.getString("CR_TITLE")); 	// 제목
				rst.put("CR_CONUSR", rs.getString("CR_CONUSR")); 	// 수신자
				rst.put("CONUSRNAME", rs.getString("CONUSRNAME")); 	// 수신자명
				rst.put("CR_SENDUSR", rs.getString("CR_SENDUSR")); // 발신자
				rst.put("SENDUSRNAME", rs.getString("SENDUSRNAME"));// 발신자명
				rst.put("CR_SENDDATE", rs.getString("CR_SENDDATE"));// 발신날짜 
				rst.put("CR_SGNMSG", rs.getString("CR_SGNMSG"));	// 내용
				strQuery.setLength(0);
	
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
			
		} catch (SQLException sqlexception){
			sqlexception.printStackTrace();
			ecamsLogger.error("## Cmr9911.get_select_grid() SQLException START ##");
			ecamsLogger.error("## Error DESC : ", sqlexception);
			ecamsLogger.error("## Cmr9911.get_select_grid() SQLException END ##");
			throw sqlexception;
		} catch (Exception exception) {
			exception.printStackTrace();
			ecamsLogger.error("## Cmr9911.get_select_grid() Exception START ##");
			ecamsLogger.error("## Error DESC : ", exception);
			ecamsLogger.error("## Cmr9911.get_select_grid() Exception END ##");
			throw exception;
		} finally{
			if (strQuery != null) 	strQuery = null;
			if (rtObj != null)  	rtObj = null;
			if (rs != null)     try{rs.close();}catch (Exception ex){ex.printStackTrace();}
			if (pstmt != null)  try{pstmt.close();}catch (Exception ex2){ex2.printStackTrace();}
			if (conn != null){
				try{
					ConnectionResource.release(conn);
				}catch(Exception ex3){
					ecamsLogger.error("## Cmr9911.get_select_grid() connection release exception ##");
					ex3.printStackTrace();
				}
			}
		}  
    } 
     
 }//end of Cmr9910 class statement
