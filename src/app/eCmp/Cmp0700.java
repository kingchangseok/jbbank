package app.eCmp;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.io.BufferedReader;

import org.apache.logging.log4j.Logger;
import org.w3c.dom.Document;

import com.ecams.common.dbconn.ConnectionContext;
import com.ecams.common.dbconn.ConnectionResource;
import com.ecams.common.logger.EcamsLogger;
import java.util.ArrayList;
import java.util.HashMap;
import app.common.*;

public class Cmp0700 {
	
	Logger ecamsLogger  = EcamsLogger.getLoggerInstance();
	
	public Object[] getReqList(String UserId, String sDate, String eDate) throws SQLException, Exception{
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		StringBuffer strQuery = new StringBuffer();
		
		ArrayList<HashMap<String, String>>  rsval = new ArrayList<HashMap<String, String>>();
		HashMap<String, String>			    rst		  = null;
		Object[] rtObj = null;
		
		ConnectionContext connectionContext = new ConnectionResource();	

		String sdate = sDate;
		String edate = eDate;
 
		
		sdate = sdate.substring(0, 10);
		edate = edate.substring(0, 10);
		/*
		sdate = sdate.replaceAll("/", "");
		edate = edate.replaceAll("/", "");
		
		System.out.println("$$$$$$$" + sdate); 
		*/
		try{
			conn = connectionContext.getConnection();
			
			
			strQuery.setLength(0);
			strQuery.append("select d.cm_deptname, e.cm_codename, a.cr_acptdate, b.cr_rsrcname, a.cr_sayu, c.cm_username  		\n");
			strQuery.append("  from cmr1000 a, cmr1010 b, cmm0040 c, cmm0100 d, cmm0020 e 										\n");
			strQuery.append(" where to_char(b.cr_prcdate,'yyyy/mm/dd') >= ? 													\n");
			strQuery.append("   and to_char(b.cr_prcdate,'yyyy/mm/dd') <= ? 													\n");
			strQuery.append("   and a.cr_qrycd = '01' 																			\n");
			strQuery.append("   and SUBSTR(a.cr_acptno,5,2) = '01' 																\n");
			strQuery.append("   AND not exists (select 'X' 																	    \n");
			strQuery.append("                     FROM cmr1010 																	\n");
			strQuery.append("                    WHERE cr_baseno = b.cr_acptno													\n");						
			strQuery.append("                      AND cr_itemid = b.cr_itemid													\n");						
			strQuery.append("                      AND to_char(cr_prcdate,'yyyymmdd') = to_char(b.cr_prcdate,'yyyymmdd')		\n");
			strQuery.append("                      AND cr_status IN ('9','8') 													\n");
			strQuery.append("                      AND SUBSTR(cr_acptno,5,2) = '11' 											\n");
			strQuery.append("                  )																				\n");
			strQuery.append("and a.cr_acptno = b.cr_acptno 																		\n");
			strQuery.append("and a.cr_editor = c.cm_userid 																		\n");
			strQuery.append("and a.cr_teamcd = d.cm_deptcd 																		\n");
			strQuery.append("and b.cr_rsrccd = e.cm_micode 																		\n");
			strQuery.append("and e.cm_macode = 'JAWON' 																			\n");
			
			pstmt = conn.prepareStatement(strQuery.toString());
			pstmt.setString(1, sdate); 
			pstmt.setString(2, edate);
			//pstmt = new LoggableStatement(conn,strQuery.toString());
			//ecamsLogger.error(((LoggableStatement)pstmt).getQueryString());
			rs = pstmt.executeQuery();
			
			while(rs.next()){ 
				rst = new HashMap<String, String>();
				 
				rst.put("cm_deptname", rs.getString("cm_deptname"));
				rst.put("cm_codename", rs.getString("cm_codename"));
				rst.put("cr_acptdate", rs.getString("cr_acptdate"));
				rst.put("cr_rsrcname", rs.getString("cr_rsrcname"));
				rst.put("cr_sayu", rs.getString("cr_sayu"));				
				rst.put("cm_username", rs.getString("cm_username"));
				
				
				rsval.add(rst);
				rst = null;

			}
			rs.close();
			pstmt.close();
			conn.close();
			
			rs = null;
			pstmt = null;
			conn = null;
			
			rtObj = rsval.toArray();
			rsval.clear();
			rsval = null;
    		
    		return rtObj; 
		}
		catch (SQLException sqlexception) {
			sqlexception.printStackTrace();
			ecamsLogger.error("## Cmp0700.getReqList() SQLException START ##");
			ecamsLogger.error("## Error DESC : ", sqlexception);	
			ecamsLogger.error("## Cmp0700.getReqList() SQLException END ##");			
			throw sqlexception;
		} catch (Exception exception) {
			exception.printStackTrace();
			ecamsLogger.error("## Cmp0700.getReqList() Exception START ##");				
			ecamsLogger.error("## Error DESC : ", exception);	
			ecamsLogger.error("## Cmp0700.getReqList() Exception END ##");				
			throw exception;
		}finally{
			if (strQuery != null)	strQuery = null;
			if (rtObj != null)	rtObj = null;			
			if (rs != null)     try{rs.close();}catch (Exception ex){ex.printStackTrace();}
			if (pstmt != null)  try{pstmt.close();}catch (Exception ex2){ex2.printStackTrace();}
			if (conn != null){
				try{
					ConnectionResource.release(conn);
				}catch(Exception ex3){
					ecamsLogger.error("## Cmp0700.getReqList() connection release exception ##");
					ex3.printStackTrace();
				}
			}
		}		
		
	}
}
