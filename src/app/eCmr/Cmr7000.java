/*****************************************************************************************
	1. program ID	: Cmr7000.java
	2. create date	: 2013.02.04
	3. auth		    : no Name
	4. update date	: 
	5. auth		    : 
	6. description	:
*****************************************************************************************/

package app.eCmr;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import app.common.LoggableStatement;
import org.apache.logging.log4j.Logger;
import com.ecams.common.dbconn.ConnectionContext;
import com.ecams.common.dbconn.ConnectionResource;
import com.ecams.common.logger.EcamsLogger;
import java.util.ArrayList;
import java.util.HashMap;
import app.common.*;

/**
 * @author bigeyes
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class Cmr7000{

    /**
     * Logger Class Instance Creation
     * logger
     */
    Logger ecamsLogger  = EcamsLogger.getLoggerInstance();
    
	/**
	 * @param HashMap<String,String> etcData
	 * @return String
	 * @throws SQLException
	 * @throws Exception
	 */

    public Object[] get_node(String SysCd) throws SQLException, Exception{
		Connection        conn        = null;
		PreparedStatement pstmt       = null;
		ResultSet         rs          = null;
		StringBuffer      strQuery    = new StringBuffer();
		ArrayList<HashMap<String, String>>  rsval  = new ArrayList<HashMap<String, String>>();
		HashMap<String, String>			  rst		  = null;		
		ConnectionContext connectionContext = new ConnectionResource();		
		
		
		try {
			conn = connectionContext.getConnection();
			rst = new HashMap<String, String>();
			strQuery.setLength(0);
			strQuery.append("select cm_svrname      \n");
			strQuery.append("from cmm0031      	 	\n");
			strQuery.append("where cm_syscd = ?     \n");
			strQuery.append("select cm_svrname      \n");
			strQuery.append("and cm_svrcd = '03'    \n");
			
			pstmt = conn.prepareStatement(strQuery.toString());	
			
	//		pstmt = new LoggableStatement(conn,strQuery.toString());			
	
			pstmt.setString(1,SysCd);
		
	 //       //ecamsLogger.error(((LoggableStatement)pstmt).getQueryString());           
	        rs = pstmt.executeQuery();
	        rsval.clear();
	        while (rs.next()){
					rst = new HashMap<String,String>();
					rst.put("cm_svrname", rs.getString("cm_svrname"));			
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
			ecamsLogger.error("## Cmr7000.get_node() SQLException START ##");
			ecamsLogger.error("## Error DESC : ", sqlexception);	
			ecamsLogger.error("## Cmr7000.get_node() SQLException END ##");			
			throw sqlexception;
		} catch (Exception exception) {
			exception.printStackTrace();
			ecamsLogger.error("## Cmr7000.get_node() Exception START ##");				
			ecamsLogger.error("## Error DESC : ", exception);	
			ecamsLogger.error("## Cmr7000.get_node() Exception END ##");				
			throw exception;
		}finally{
			if (strQuery != null) 	strQuery = null;
			if (rs != null)     try{rs.close();}catch (Exception ex){ex.printStackTrace();}
			if (pstmt != null)  try{pstmt.close();}catch (Exception ex2){ex2.printStackTrace();}
			if (conn != null){
				try{
					ConnectionResource.release(conn);
				}catch(Exception ex3){
					ecamsLogger.error("## Cmr7000.get_node() connection release exception ##");
					ex3.printStackTrace();
				}
			}
		}
	}//end of get_node() method statement   
}//end of Cmr7000 class statement

