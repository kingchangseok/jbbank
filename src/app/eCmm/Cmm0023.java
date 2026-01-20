package app.eCmm;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.apache.logging.log4j.Logger;

import app.common.LoggableStatement;

import com.ecams.common.dbconn.ConnectionContext;
import com.ecams.common.dbconn.ConnectionResource;
import com.ecams.common.logger.EcamsLogger;

import java.util.ArrayList;
import java.util.HashMap;

public class Cmm0023 {
	
	Logger ecamsLogger  = EcamsLogger.getLoggerInstance();
	
	public Object[] getRsrcList(HashMap<String,String> rsrcList) throws SQLException, Exception{
		Connection conn = null;
		PreparedStatement pstmt = null;
		StringBuffer strQuery = new StringBuffer();
		ResultSet rs = null;
		ArrayList<HashMap<String, String>> rsval = new ArrayList<HashMap<String, String>>();
		HashMap<String, String> rst = null;
		Object[] returnObjectArray = null;
		
		ConnectionContext connectionContext = new ConnectionResource();
		
		try{
			conn = connectionContext.getConnection();
			strQuery.setLength(0);
			
			strQuery.append(" select a.CM_MICODE, a.CM_CODENAME, b.CM_EXENAME, b.CM_EXENO \n");
			strQuery.append(" from cmm0020 a, cmm0023 b \n");
			strQuery.append(" where a.CM_MACODE = 'LANGUAGE' \n");
			strQuery.append(" and a.CM_MICODE = b.CM_LANGCD \n");
			
			pstmt = conn.prepareStatement(strQuery.toString());
			//pstmt =  new LoggableStatement(conn, strQuery.toString());
			//ecamsLogger.error(((LoggableStatement)pstmt).getQueryString()); 
			rs = pstmt.executeQuery();
			
			while (rs.next()){
				rst = new HashMap<String,String>();
				rst.put("CM_MICODE", rs.getString("CM_MICODE"));
				rst.put("CM_CODENAME", rs.getString("CM_CODENAME"));
				rst.put("CM_EXENAME", rs.getString("CM_EXENAME"));
				rst.put("CM_EXENO", rs.getString("CM_EXENO"));
				
				rsval.add(rst);
				rst = null;
				
			}
			pstmt.close();
			rs.close();
			conn.close();
			
			pstmt = null;
			rs = null;
			conn = null;
			
			returnObjectArray = rsval.toArray();
			rsval.clear();
			rsval = null;
			
			return returnObjectArray;
			
		}catch (SQLException sqlexception) {
			sqlexception.printStackTrace();
			ecamsLogger.error("## Cmm0023.getRsrcList() SQLException START ##");
			ecamsLogger.error("## Error DESC : ", sqlexception);	
			ecamsLogger.error("## Cmm0023.getRsrcList() SQLException END ##");			
			throw sqlexception;
		} catch (Exception exception) {
			exception.printStackTrace();
			ecamsLogger.error("## Cmm0023.getRsrcList() Exception START ##");				
			ecamsLogger.error("## Error DESC : ", exception);	
			ecamsLogger.error("## Cmm0023.getRsrcList() Exception END ##");				
			throw exception;
		}finally{
			if (returnObjectArray != null)	returnObjectArray = null;
			if (strQuery != null)	strQuery = null;
			if (rs != null)     try{rs.close();}catch (Exception ex){ex.printStackTrace();}
			if (pstmt != null)  try{pstmt.close();}catch (Exception ex2){ex2.printStackTrace();}
			if (conn != null){
				try{
					ConnectionResource.release(conn);
				}catch(Exception ex3){
					ecamsLogger.error("## Cmm0023.getRsrcList() connection release exception ##");
					ex3.printStackTrace();
				}
			}
		}	
		
	}
	
	public Object[] getCboRsrc(String SelMsg) throws SQLException, Exception {
		Connection        conn        = null;
		PreparedStatement pstmt       = null;
		ResultSet         rs          = null;
		StringBuffer      strQuery    = new StringBuffer();
	
		ArrayList<HashMap<String, String>>  rsval = new ArrayList<HashMap<String, String>>();
		HashMap<String, String>			  rst		  = null;	
		
		Object[] returnObjectArray = null;
		String            strSelMsg   = "";
	    		
		ConnectionContext connectionContext = new ConnectionResource();		
		
		try {
			conn = connectionContext.getConnection();
			
			if (!"".equals(SelMsg) && SelMsg != null) {
				if (SelMsg.equals("ALL")) strSelMsg = "전체";
				else if (SelMsg.equals("SEL")) strSelMsg = "선택하세요";
			}
			strQuery.setLength(0);
			strQuery.append("select a.cm_macode, a.cm_micode ,a.cm_codename, b.cm_exename, b.cm_exeno  	\n");
			strQuery.append("  from cmm0020 a, cmm0023 b                     							\n");
			strQuery.append("  where  a.cm_closedt is null                   							\n");	
			strQuery.append("   and a.cm_macode='LANGUAGE'                   							\n");
			strQuery.append("   and a.cm_micode != '****'                    							\n");
			strQuery.append("   and a.cm_micode = b.cm_langcd(+)               							\n");
			
	        pstmt = conn.prepareStatement(strQuery.toString());	
	   
	                   
	        rs = pstmt.executeQuery();
	        
	        rsval.clear();
	        
			while (rs.next()){
				if (rs.getRow() == 1 && strSelMsg != "") {
				   rst = new HashMap<String, String>();
				   rst.put("ID", "0");
				   rst.put("cm_micode", "0000");
				   rst.put("cm_codename", strSelMsg);
				   rsval.add(rst);
				   rst = null;
				}
				rst = new HashMap<String, String>();
				rst.put("ID", Integer.toString(rs.getRow()));
				rst.put("cm_micode", rs.getString("cm_micode"));
				rst.put("cm_codename", rs.getString("cm_codename"));
				rst.put("cm_exename" , rs.getString("cm_exename"));
				rst.put("cm_exeno", rs.getString("cm_exeno"));

				rst.put("langCodeName", rs.getString("cm_codename")); //Cmm0200LangInfo 에서만 사용합니다.
				rsval.add(rst);
				rst = null;
			}//end of while-loop statement
			rs.close();
			pstmt.close();
			conn.close();
			
			rs = null;
			pstmt = null;
			conn = null;
			
			returnObjectArray = rsval.toArray();
			rsval = null;
			//ecamsLogger.debug(rsval.toString());		
			return returnObjectArray;			
			
		} catch (SQLException sqlexception) {
			sqlexception.printStackTrace();
			ecamsLogger.error("## Cmm0023.getCboRsrc() SQLException START ##");
			ecamsLogger.error("## Error DESC : ", sqlexception);	
			ecamsLogger.error("## Cmm0023.getCboRsrc() SQLException END ##");			
			throw sqlexception;
		} catch (Exception exception) {
			exception.printStackTrace();
			ecamsLogger.error("## Cmm0023.getCboRsrc() Exception START ##");				
			ecamsLogger.error("## Error DESC : ", exception);	
			ecamsLogger.error("## Cmm0023.getCboRsrc() Exception END ##");				
			throw exception;
		}finally{
			if (returnObjectArray != null)	returnObjectArray = null;
			if (strQuery != null)	strQuery = null;
			if (rs != null)     try{rs.close();}catch (Exception ex){ex.printStackTrace();}
			if (pstmt != null)  try{pstmt.close();}catch (Exception ex2){ex2.printStackTrace();}
			if (conn != null){
				try{
					ConnectionResource.release(conn);
				}catch(Exception ex3){
					ecamsLogger.error("## Cmm0023.getCboRsrc() connection release exception ##");
					ex3.printStackTrace();
				}
			}
		}		

	}
	
	public String setRsrc(HashMap<String,String> rsrc)throws SQLException, Exception{
		Connection conn = null;
		PreparedStatement pstmt = null;
		PreparedStatement pstmt2 = null;
		PreparedStatement pstmt3 = null;
		ResultSet         rs     = null;
		//ResultSet rs = null;
		StringBuffer strQuery = new StringBuffer();
		

		ConnectionContext connectionContext = new ConnectionResource();	

		
		try{
			conn = connectionContext.getConnection();

			strQuery.setLength(0);
			strQuery.append(" DELETE cmm0023 						\n");
			strQuery.append("  WHERE  CM_LANGCD = ?					\n");
			pstmt = conn.prepareStatement(strQuery.toString());
			//pstmt =  new LoggableStatement(conn, strQuery.toString());
			int parmCnt = 0;
			pstmt.setString(++parmCnt, rsrc.get("CM_LANGCD"));
			pstmt.executeUpdate();
			pstmt.close();
			
			strQuery.setLength(0);
			strQuery.append(" insert into cmm0023 					\n");
			strQuery.append(" ( CM_LANGCD, CM_EXENAME, CM_EXENO ) 	\n");
			strQuery.append(" values 								\n");
			strQuery.append(" ( ? , ? , ?)  						\n");
			pstmt = conn.prepareStatement(strQuery.toString());
			pstmt =  new LoggableStatement(conn, strQuery.toString());
			parmCnt = 0;
			pstmt.setString(++parmCnt, rsrc.get("CM_LANGCD"));
			pstmt.setString(++parmCnt, rsrc.get("CM_EXENAME"));
			pstmt.setString(++parmCnt, rsrc.get("CM_EXENO"));
			ecamsLogger.error(((LoggableStatement)pstmt).getQueryString());
			pstmt.executeUpdate();
			pstmt.close();
			
			strQuery.setLength(0);
			strQuery.append(" SELECT  CM_CODENAME, CM_CLOSEDT  	\n");
			strQuery.append("   FROM  CMM0020  		 			\n");
			strQuery.append("  WHERE  CM_MACODE = 'LANGUAGE'	\n");
			strQuery.append("    AND  CM_MICODE = ?				\n");			
			pstmt2 = conn.prepareStatement(strQuery.toString());
			pstmt2 =  new LoggableStatement(conn, strQuery.toString());
			pstmt2.setString(1, rsrc.get("CM_LANGCD"));
			ecamsLogger.error(((LoggableStatement)pstmt2).getQueryString());
			rs = pstmt2.executeQuery();
			if (rs.next()) {
				if (rs.getString("CM_CLOSEDT") != null || rs.getString("CM_CODENAME") !=  rsrc.get("CM_CODENAME")) {
					strQuery.setLength(0);
					strQuery.append(" UPDATE  CMM0020  		 			\n");
					strQuery.append("    SET  CM_CLOSEDT = NULL  		\n");
					strQuery.append("       , CM_CODENAME = ?			\n");
					strQuery.append("  WHERE  CM_MACODE = 'LANGUAGE'	\n");
					strQuery.append("    AND  CM_MICODE = ?				\n");			
					pstmt3 = conn.prepareStatement(strQuery.toString());
					//pstmt3 =  new LoggableStatement(conn, strQuery.toString());
					pstmt3.setString(1, rsrc.get("CM_CODENAME"));
					pstmt3.setString(2, rsrc.get("CM_LANGCD"));
					//ecamsLogger.error(((LoggableStatement)pstmt3).getQueryString());
					pstmt3.executeUpdate();
					pstmt3.close();
					System.out.println("if");
				}
			}else{
				strQuery.setLength(0);
				strQuery.append(" insert into cmm0020 \n");
				strQuery.append(" ( CM_MACODE, CM_MICODE, CM_CODENAME ) \n");
				strQuery.append(" values \n");
				strQuery.append(" (?, ?, ?) \n");
				
				pstmt3 = conn.prepareStatement(strQuery.toString());
				//pstmt3 =  new LoggableStatement(conn, strQuery.toString());
				//pstmt2 =  new LoggableStatement(conn, strQuery.toString());
				parmCnt = 0;
				pstmt3.setString(++parmCnt, "LANGUAGE");
				pstmt3.setString(++parmCnt, rsrc.get("CM_LANGCD"));
				pstmt3.setString(++parmCnt, rsrc.get("CM_CODENAME"));
				//ecamsLogger.error(((LoggableStatement)pstmt3).getQueryString());
				pstmt3.executeUpdate();
				pstmt3.close();
				System.out.println("else");
			}			
			rs.close();
			pstmt2.close();
			conn.close();
			
			rs = null;
			pstmt2 = null;
			pstmt3 = null;
			pstmt = null;
			conn = null;
			return "Y";
			
		}catch (SQLException sqlexception) {
			sqlexception.printStackTrace();
			ecamsLogger.error("## Cmm0023.setRsrc() SQLException START ##");
			ecamsLogger.error("## Error DESC : ", sqlexception);	
			ecamsLogger.error("## Cmm0023.setRsrc() SQLException END ##");			
			throw sqlexception;
		} catch (Exception exception) {
			exception.printStackTrace();
			ecamsLogger.error("## Cmm0023.setRsrc() Exception START ##");				
			ecamsLogger.error("## Error DESC : ", exception);	
			ecamsLogger.error("## Cmm0023.setRsrc() Exception END ##");				
			throw exception;
		}finally{
			if (strQuery != null)	strQuery = null;
			if (pstmt != null)  try{pstmt.close();}catch (Exception ex2){ex2.printStackTrace();}
			if (conn != null){
				try{
					ConnectionResource.release(conn);
				}catch(Exception ex3){
					ecamsLogger.error("## Cmm0023.setRsrc() connection release exception ##");
					ex3.printStackTrace();
				}
			}
		}		
		
	}


	public String delRsrc(HashMap<String,String> rsrcDel) throws SQLException, Exception{
		Connection conn = null;
		PreparedStatement pstmt = null;
		PreparedStatement pstmt2 = null;
		PreparedStatement pstmt3 = null;
		ResultSet rs = null;
		StringBuffer strQuery = new StringBuffer();

		
		ConnectionContext connectionContext = new ConnectionResource();	
		try{
			conn = connectionContext.getConnection();

			strQuery.setLength(0);
			strQuery.append(" delete from cmm0023 where CM_LANGCD = ? \n");
			pstmt = conn.prepareStatement(strQuery.toString());
			pstmt.setString(1, rsrcDel.get("CM_LANGCD"));
			pstmt.executeUpdate();
			
			strQuery.setLength(0);
			strQuery.append(" update cmm0020 set cm_closedt = sysdate 	\n");
			strQuery.append("  where cm_macode = 'LANGUAGE' 			\n");
			strQuery.append("    and cm_micode = ? 						\n");
			pstmt2 = conn.prepareStatement(strQuery.toString());
			//pstmt3 =  new LoggableStatement(conn, strQuery.toString());
			pstmt2.setString(1, rsrcDel.get("CM_LANGCD"));
			//ecamsLogger.error(((LoggableStatement)pstmt).getQueryString()); 
			pstmt2.executeUpdate();
			
			pstmt.close();
			pstmt2.close();
			conn.close();
			
			pstmt = null;
			pstmt2 = null;
			rs = null;
			conn = null;

			
			return "Y";
		}catch (SQLException sqlexception) {
			sqlexception.printStackTrace();
			ecamsLogger.error("## Cmm0023.delRsrc() SQLException START ##");
			ecamsLogger.error("## Error DESC : ", sqlexception);	
			ecamsLogger.error("## Cmm0023.delRsrc() SQLException END ##");			
			throw sqlexception;
		} catch (Exception exception) {
			exception.printStackTrace();
			ecamsLogger.error("## Cmm0023.delRsrc() Exception START ##");				
			ecamsLogger.error("## Error DESC : ", exception);	
			ecamsLogger.error("## Cmm0023.delRsrc() Exception END ##");				
			throw exception;
		}finally{
			if (strQuery != null)	strQuery = null;
			if (pstmt != null)  try{pstmt.close();}catch (Exception ex2){ex2.printStackTrace();}
			if (conn != null){
				try{
					ConnectionResource.release(conn);
				}catch(Exception ex3){
					ecamsLogger.error("## Cmm0023.delRsrc() connection release exception ##");
					ex3.printStackTrace();
				}
			}
		}	
	}
}
