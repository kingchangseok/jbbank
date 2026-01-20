
/*****************************************************************************************
	1. program ID	: eCmm1800.java
	2. create date	: 
	3. auth		    : 
	4. update date	: 
	5. auth		    : 
	6. description	: eCmm1800
*****************************************************************************************/

package app.eCmm;


import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.SocketTimeoutException;
import java.nio.channels.IllegalBlockingModeException;
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
import app.common.LoggableStatement;




/**
 * @author bigeyes
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */


public class Cmm1500{
    /**
     * Logger Class Instance Creation
     * logger
     */
    Logger ecamsLogger  = EcamsLogger.getLoggerInstance();
    
	/**
	 * project 등록
	 * @param  pPrjno,pDocname,pDocPath
	 * @return String
	 * @throws SQLException
	 * @throws Exception
	 */
		
    public Object[] getAgentState(String sysCD,String sType) throws SQLException, Exception {
		Connection        conn        = null;
		PreparedStatement pstmt       = null;
		PreparedStatement pstmt2       = null;
		StringBuffer      strQuery2    = new StringBuffer();
		ResultSet         rs          = null;
		StringBuffer      strQuery    = new StringBuffer();
		ArrayList<HashMap<String, String>>  rsval = new ArrayList<HashMap<String, String>>();
		HashMap<String, String>			  rst		  = null;
		SocketAddress socketAddress = null;
		Socket tSock = null;
		boolean errFlag;
		ConnectionContext connectionContext = new ConnectionResource();
		
		
		try {
			conn = connectionContext.getConnection(); 
			
// 20221220 Agent 모니터링 소켓연결 제거			
//			strQuery.setLength(0);
//			strQuery.append("select distinct cm_svrip, cm_portno \n");
//		    strQuery.append("  from cmm0031                      \n"); 
//			strQuery.append(" where cm_closedt is null           \n");
//			strQuery.append("   and substr(cm_svruse,2,1)='0'    \n");
//			if (!sysCD.equals("00000")){
//				strQuery.append("and cm_syscd = ? \n");
//			}
//			strQuery.append("order by cm_svrip \n");
//			pstmt = conn.prepareStatement(strQuery.toString());
//
//			if (!sysCD.equals("00000")){
//				pstmt.setString(1,sysCD);
//			}
//			
//            rs = pstmt.executeQuery();
//            while (rs.next()){
//            	errFlag = false;
//            	socketAddress = new InetSocketAddress(rs.getString("cm_svrip"),rs.getInt("cm_portno"));
//            	tSock = new Socket();
//            	
//            	try{
//            		tSock.connect(socketAddress, 5000);
//            	}catch(SocketTimeoutException e){
//            		errFlag = true;
//            		
//            	}catch(IOException e){
//            		errFlag = true;
//            		
//            	}catch(IllegalBlockingModeException e){
//            		errFlag = true;
//            	}catch(IllegalArgumentException e){
//            		errFlag = true;
//            	}            	
//            	finally{
//            		tSock.close();
//            		strQuery2.setLength(0);
//            		strQuery2.append("UPDATE  CMM0031 \n");
//            		if (errFlag == true){
//                		strQuery2.append("SET  CM_Agent = 'ER' \n");
//            		}
//            		else{
//            			strQuery2.append("SET  CM_Agent = '' \n");
//            		}
//            		strQuery2.append("WHERE  CM_SvrIP = ? \n");
//            		strQuery2.append("AND	CM_PORTNO = ? \n");
//            		
//            		pstmt2 = conn.prepareStatement(strQuery2.toString());
//            		
//            		pstmt2.setString(1, rs.getString("cm_svrip"));
//            		pstmt2.setInt(2, rs.getInt("cm_portno"));
//            		
//            		pstmt2.executeUpdate();
//            		pstmt2.close();
//            		conn.commit();
//            		
//            		
//            	}
//            }
//            rs.close();
//            pstmt.close();
			
			
			
			strQuery.setLength(0);
			strQuery.append("select a.cm_sysmsg, a.cm_syscd, b.cm_svrname, b.cm_svrip, \n");
			strQuery.append("b.cm_portno,b.cm_svrusr, b.cm_dir, nvl(cm_agent,'00') as status \n");
		    strQuery.append("from cmm0030 a,cmm0031 b \n"); 
			strQuery.append("where a.cm_closedt is null \n");
			if (!sysCD.equals("00000")){
				strQuery.append("and a.cm_syscd = ? \n");
			}
			if (!sType.equals("1")){
				if (sType.equals("2")){
					strQuery.append("and b.cm_agent is null \n");
				}
				else{
					strQuery.append("and b.cm_agent is not null \n");
				}
			}
			strQuery.append("and a.cm_syscd=b.cm_syscd \n");
			strQuery.append("and b.cm_closedt is null \n");
			strQuery.append("and substr(b.cm_svruse,2,1)='0' \n");
			strQuery.append("order by b.cm_syscd \n");
			pstmt = conn.prepareStatement(strQuery.toString());
			//pstmt = new LoggableStatement(conn,strQuery.toString());

			if (!sysCD.equals("00000")){
				pstmt.setString(1,sysCD);
			}
			////ecamsLogger.error(((LoggableStatement)pstmt).getQueryString());
            rs = pstmt.executeQuery();
            rsval.clear();
            while (rs.next()){
            	rst = new HashMap<String, String>();
				rst.put("sysCdMsg", rs.getString("cm_sysmsg")+"["+rs.getString("cm_syscd")+"]"); 		
				rst.put("cm_svrname", rs.getString("cm_svrname"));		
				rst.put("cm_svrip", rs.getString("cm_svrip"));
				rst.put("cm_portno", rs.getString("cm_portno"));	
				rst.put("cm_svrusr", rs.getString("cm_svrusr"));
				rst.put("cm_dir", rs.getString("cm_dir"));
				if (rs.getString("status").equals("00")){
					rst.put("statusName", "정상");
				}
				else{
					rst.put("statusName", "장애");
				}
				rst.put("status",rs.getString("status"));
				
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
			ecamsLogger.error("## Cmm1500.sql_Qry() SQLException START ##");
			ecamsLogger.error("## Error DESC : ", sqlexception);	
			ecamsLogger.error("## Cmm1500.sql_Qry() SQLException END ##");			
			throw sqlexception;
		} catch (Exception exception) {
			exception.printStackTrace();
			ecamsLogger.error("## Cmm1500.sql_Qry() Exception START ##");				
			ecamsLogger.error("## Error DESC : ", exception);	
			ecamsLogger.error("## Cmm1500.sql_Qry() Exception END ##");				
			throw exception;
		}finally{
			if (strQuery != null) 	strQuery = null;
			if (rsval != null)	rsval = null;
			if (rs != null)     try{rs.close();}catch (Exception ex){ex.printStackTrace();}
			if (pstmt != null)  try{pstmt.close();}catch (Exception ex2){ex2.printStackTrace();}
			if (conn != null){
				try{
					ConnectionResource.release(conn);
				}catch(Exception ex3){
					ecamsLogger.error("## Cmm1500.sql_Qry() connection release exception ##");
					ex3.printStackTrace();
				}
			}
		}
		
		
	}//end of sql_Qry() method statement
    
}//end of Cmm1500 class statement
