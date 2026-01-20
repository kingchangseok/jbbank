
/*****************************************************************************************
	1. program ID	: Holiday_Check.java
	2. create date	: 2008.05. 16
	3. author       : k.m.s
	4. update date	: 
	5. auth		    : 
	6. description	: Holiday Check 
*****************************************************************************************/

package app.common;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.apache.logging.log4j.Logger;
import com.ecams.common.dbconn.ConnectionContext;
import com.ecams.common.dbconn.ConnectionResource;
import com.ecams.common.logger.EcamsLogger;

/**
 * @author bigeyes
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */

public class Holiday_Check{
	
    /**
     * Logger Class Instance Creation
     * logger
     */
    Logger ecamsLogger  = EcamsLogger.getLoggerInstance();
    
	/**
	 * ø¿¥√¿Ã »ﬁ¿œ¿Œ¡ˆ √º≈©
	 * @param  
	 * @return String
	 * @throws SQLException
	 * @throws Exception
	 */
	
	public String SelectHoli() throws SQLException, Exception {
		Connection        conn        = null;
		PreparedStatement pstmt       = null;
		ResultSet         rs          = null;
		StringBuffer      strQuery    = new StringBuffer();
		String			  HoliSw	  = null;
		ConnectionContext connectionContext = new ConnectionResource();
		
		try {
			conn = connectionContext.getConnection();
			strQuery.append("select count(*) as cnt from cmm0050                     \n");
			strQuery.append(" where cm_holiday=to_char(SYSDATE,'yyyymmdd')           \n");	
			strQuery.append("   and cm_closedt is null                               \n");				
			pstmt = conn.prepareStatement(strQuery.toString());	
			//pstmt = new LoggableStatement(conn,strQuery.toString());
			//ecamsLogger.error(((LoggableStatement)pstmt).getQueryString());
            rs = pstmt.executeQuery();
			while (rs.next()){
			   HoliSw = Integer.toString(rs.getInt("cnt"));	
			}//end of while-loop statement
			rs.close();
			pstmt.close();
			conn.close();
			return HoliSw;	
			
		} catch (SQLException sqlexception) {
			sqlexception.printStackTrace();
			ecamsLogger.error("## Holiday_Check.SelectHoli() SQLException START ##");
			ecamsLogger.error("## Error DESC : ", sqlexception);	
			ecamsLogger.error("## Holiday_Check.SelectHoli() SQLException END ##");			
			throw sqlexception;
		} catch (Exception exception) {
			exception.printStackTrace();
			ecamsLogger.error("## Holiday_Check.SelectHoli() Exception START ##");				
			ecamsLogger.error("## Error DESC : ", exception);	
			ecamsLogger.error("## Holiday_Check.SelectHoli() Exception END ##");				
			throw exception;
		}finally{
			if (strQuery != null) 	strQuery = null;
			if (rs != null)     try{rs.close();}catch (Exception ex){ex.printStackTrace();}
			if (pstmt != null)  try{pstmt.close();}catch (Exception ex2){ex2.printStackTrace();}
			if (conn != null){
				try{
					ConnectionResource.release(conn);
				}catch(Exception ex3){
					ecamsLogger.error("## Holiday_Check.SelectHoli() connection release exception ##");
					ex3.printStackTrace();
				}
			}
		}
			
	}//end of SelectHoli() method statement	
	
}//end of Holiday_Check class statement
