/*****************************************************************************************
	1. program ID	: UserInfo.java
	2. create date	: 2008.04. 08
	3. auth		    : teok.kang
	4. update date	: 2009.02.20
	5. auth		    : no name
	6. description	: UserInfo 
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

import java.util.ArrayList;
import java.util.HashMap;

/**
 * @author bigeyes
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */

public class UserInfo{
	
    /**
     * Logger Class Instance Creation
     * logger
     */
    Logger ecamsLogger  = EcamsLogger.getLoggerInstance();
    
	/**
	 * 사용자 정보 조회합니다.
	 * @param  UserID
	 * @return ArrayList<HashMap<String,String>>
	 * @throws SQLException
	 * @throws Exception
	 */
	
	public Object[] getUserInfo(String UserID) throws SQLException, Exception {
		Connection        conn        = null;
		PreparedStatement pstmt       = null;
		ResultSet         rs          = null;
		StringBuffer      strQuery    = new StringBuffer();
		HashMap<String, String>			  rst		  = null;
		ArrayList<HashMap<String, String>>		  rtList	  = new ArrayList<HashMap<String, String>>();
		Object[] returnObjectArray = null;
		
		ConnectionContext connectionContext = new ConnectionResource();
		
		try {
			conn = connectionContext.getConnection();
			
			strQuery.append("select a.cm_username,a.cm_manid,a.cm_position,a.cm_project,a.cm_duty, \n");
			strQuery.append("		a.cm_admin,c.cm_deptname teamname,a.cm_posname caption  \n");
			strQuery.append("  from cmm0100 c,cmm0040 a 				\n");
			strQuery.append(" where a.cm_userid= ? 				 				\n");  //Sv_UserID
			strQuery.append("   and a.cm_project=c.cm_deptcd 					\n");
			pstmt = conn.prepareStatement(strQuery.toString());
			pstmt.setString(1, UserID);
            rs = pstmt.executeQuery();
            rtList.clear();
			if (rs.next()){
				rst = new HashMap<String,String>();
				rst.put("cm_username",rs.getString("cm_username"));
				rst.put("cm_admin",rs.getString("cm_admin"));
				rst.put("cm_manid",rs.getString("cm_manid"));
				rst.put("cm_project",rs.getString("cm_project"));
				rst.put("cm_duty",rs.getString("cm_duty"));
				rst.put("cm_position",rs.getString("cm_position"));
				rst.put("teamcd",rs.getString("cm_project"));
				//rst.put("sv_pmo",getPMOInfo(UserID));
				rst.put("caption",rs.getString("caption"));
				rst.put("teamname",rs.getString("teamname"));
				rtList.add(rst);
				rst = null;
			}//end of while-loop statement
			
			rs.close();
			pstmt.close();
			conn.close();
			rs = null;
			pstmt = null;
			conn = null;
			
			returnObjectArray = rtList.toArray();
			rtList = null;
			
			return returnObjectArray;
			
			
		} catch (SQLException sqlexception) {
			sqlexception.printStackTrace();
			ecamsLogger.error("## UserInfo.SelectUserInfo() SQLException START ##");
			ecamsLogger.error("## Error DESC : ", sqlexception);	
			ecamsLogger.error("## UserInfo.SelectUserInfo() SQLException END ##");			
			throw sqlexception;
		} catch (Exception exception) {
			exception.printStackTrace();
			ecamsLogger.error("## UserInfo.SelectUserInfo() Exception START ##");				
			ecamsLogger.error("## Error DESC : ", exception);	
			ecamsLogger.error("## UserInfo.SelectUserInfo() Exception END ##");				
			throw exception;
		}finally{
			if (strQuery != null) 	strQuery = null;
			if (returnObjectArray != null)	returnObjectArray = null;
			if (rs != null)     try{rs.close();}catch (Exception ex){ex.printStackTrace();}
			if (pstmt != null)  try{pstmt.close();}catch (Exception ex2){ex2.printStackTrace();}
			if (conn != null){
				try{
					ConnectionResource.release(conn);
				}catch(Exception ex3){
					ecamsLogger.error("## UserInfo.SelectUserInfo() connection release exception ##");
					ex3.printStackTrace();
				}
			}
		}
	}//end of SelectUserInfo() method statement	


	public String getUserInfo_sub(Connection conn,String UserID,String colName) throws SQLException, Exception {
		PreparedStatement pstmt       = null;
		ResultSet         rs          = null;
		StringBuffer      strQuery    = new StringBuffer();
		String            retMsg      = "";
		
		try {
			strQuery.setLength(0);
			strQuery.append("select cm_project from cmm0040     \n");
			strQuery.append(" where cm_userid= ?  				\n");  //Sv_UserID
			pstmt = conn.prepareStatement(strQuery.toString());	
			pstmt.setString(1, UserID);
            rs = pstmt.executeQuery();
                        
			if (rs.next()){
				if (colName.equals("cm_project")) retMsg = rs.getString("cm_project");
			}//end of while-loop statement
			rs.close();
			pstmt.close();
			rs = null;
			pstmt = null;
			
			return retMsg;
			
		} catch (SQLException sqlexception) {
			sqlexception.printStackTrace();
			ecamsLogger.error("## UserInfo.getUserInfo_sub() SQLException START ##");
			ecamsLogger.error("## Error DESC : ", sqlexception);	
			ecamsLogger.error("## UserInfo.getUserInfo_sub() SQLException END ##");			
			throw sqlexception;
		} catch (Exception exception) {
			exception.printStackTrace();
			ecamsLogger.error("## UserInfo.getUserInfo_sub() Exception START ##");				
			ecamsLogger.error("## Error DESC : ", exception);	
			ecamsLogger.error("## UserInfo.getUserInfo_sub() Exception END ##");				
			throw exception;
		}finally{
			if (strQuery != null) 	strQuery = null;
			if (rs != null)     try{rs.close();}catch (Exception ex){ex.printStackTrace();}
			if (pstmt != null)  try{pstmt.close();}catch (Exception ex2){ex2.printStackTrace();}
		}
	}//end of getUserInfo_sub() method statement
	
	
	/**
	 * 
	 * <PRE>
	 * 1. MethodName	: getPMOInfo
	 * 2. ClassName		: UserInfo
	 * 3. Commnet			: 본부장/부서장/팀장/파트장/프로그램조회 권한확인 여부
	 * 4. 작성자				: Administrator
	 * 5. 작성일				: 2010. 11. 30. 오후 7:43:48
	 * </PRE>
	 * 		@return String
	 * 		@param Sv_UserID
	 * 		@return
	 * 		@throws SQLException
	 * 		@throws Exception
	 */
	public String getPMOInfo(String Sv_UserID) throws SQLException, Exception {
		Connection        conn        = null;
		PreparedStatement pstmt       = null;
		ResultSet         rs          = null;
		StringBuffer      strQuery    = new StringBuffer();
		String			  ret         = "";
		
		ConnectionContext connectionContext = new ConnectionResource();
		
		try {
			
			conn = connectionContext.getConnection();
			
			if (isAdmin_conn(Sv_UserID, conn)) ret = "A";
			else {
				strQuery.setLength(0);
				strQuery.append("SELECT min(a.CM_RGTCD) rgtcd,b.cm_project  \n");
				strQuery.append("  FROM CMM0040 b,CMM0043 a                 \n");
				strQuery.append(" WHERE b.CM_USERID= ? 				    	\n");
				strQuery.append("   AND b.CM_USERID=a.CM_USERID         	\n");
				strQuery.append("GROUP BY b.CM_PROJECT \n");
				
				pstmt = conn.prepareStatement(strQuery.toString());
				pstmt.setString(1, Sv_UserID);
				rs = pstmt.executeQuery();
				
				if (rs.next()){
					if (rs.getString("rgtcd") == null) ret = "X";
					else if (rs.getString("rgtcd").equals("31")) ret = "C";//본부장권한
					else if (rs.getString("rgtcd").equals("41")) ret = "D";
					else if (rs.getString("rgtcd").equals("51")) ret = "D";
					else if (rs.getString("rgtcd").equals("61")) ret = "P";
					else ret = "S";
					ret = ret + rs.getString("cm_project");
				}//end of while-loop statement
				
				rs.close();
				pstmt.close();
			}
			conn.close();
			rs = null;
			pstmt = null;
			conn = null;
			
			return ret;
			
		} catch (SQLException sqlexception) {
			sqlexception.printStackTrace();
			ecamsLogger.error("## UserInfo.getPMOInfo() SQLException START ##");
			ecamsLogger.error("## Error DESC : ", sqlexception);	
			ecamsLogger.error("## UserInfo.getPMOInfo() SQLException END ##");			
			throw sqlexception;
		} catch (Exception exception) {
			exception.printStackTrace();
			ecamsLogger.error("## UserInfo.getPMOInfo() Exception START ##");				
			ecamsLogger.error("## Error DESC : ", exception);	
			ecamsLogger.error("## UserInfo.getPMOInfo() Exception END ##");				
			throw exception;
		}finally{
			if (strQuery != null) 	strQuery = null;
			if (rs != null)     try{rs.close();}catch (Exception ex){ex.printStackTrace();}
			if (pstmt != null)  try{pstmt.close();}catch (Exception ex2){ex2.printStackTrace();}
			if (conn != null){
				try{
					ConnectionResource.release(conn);
				}catch(Exception ex3){
					ecamsLogger.error("## UserInfo.getPMOInfo() connection release exception ##");
					ex3.printStackTrace();
				}
			}
		}
	}//end of getPMOInfo() method statement
	
	public String getCheckInfo(String Sv_UserID) throws SQLException, Exception {
		Connection        conn        = null;
		PreparedStatement pstmt       = null;
		ResultSet         rs          = null;
		StringBuffer      strQuery    = new StringBuffer();
		String			  ret         = "";
		String			  project         = "";
		
		ConnectionContext connectionContext = new ConnectionResource();
		
		try {
			
			conn = connectionContext.getConnection();
			
			if (isAdmin_conn(Sv_UserID, conn)) ret = "A";
			else {
				strQuery.setLength(0);
				strQuery.append("SELECT a.CM_RGTCD,b.cm_project 		\n");
				strQuery.append("FROM CMM0040 b,CMM0043 a               \n");
				strQuery.append("WHERE b.CM_USERID= ? 				    \n");
				strQuery.append("AND b.CM_USERID=a.CM_USERID         	\n");
				
				pstmt = conn.prepareStatement(strQuery.toString());
				pstmt.setString(1, Sv_UserID);
				rs = pstmt.executeQuery();
				
				while (rs.next()){
					project="";
					project=rs.getString("cm_project");
					if (rs.getString("CM_RGTCD") == null) ret = "X";
					else if (rs.getString("CM_RGTCD").equals("LIB")){
						ret = "P";
						break;
					}
					else ret = "S";
				}//end of while-loop statement
				ret = ret + project;
				
				rs.close();
				pstmt.close();
			}
			conn.close();
			rs = null;
			pstmt = null;
			conn = null;
			
			return ret;
			
		} catch (SQLException sqlexception) {
			sqlexception.printStackTrace();
			ecamsLogger.error("## UserInfo.getPMOInfo() SQLException START ##");
			ecamsLogger.error("## Error DESC : ", sqlexception);	
			ecamsLogger.error("## UserInfo.getPMOInfo() SQLException END ##");			
			throw sqlexception;
		} catch (Exception exception) {
			exception.printStackTrace();
			ecamsLogger.error("## UserInfo.getPMOInfo() Exception START ##");				
			ecamsLogger.error("## Error DESC : ", exception);	
			ecamsLogger.error("## UserInfo.getPMOInfo() Exception END ##");				
			throw exception;
		}finally{
			if (strQuery != null) 	strQuery = null;
			if (rs != null)     try{rs.close();}catch (Exception ex){ex.printStackTrace();}
			if (pstmt != null)  try{pstmt.close();}catch (Exception ex2){ex2.printStackTrace();}
			if (conn != null){
				try{
					ConnectionResource.release(conn);
				}catch(Exception ex3){
					ecamsLogger.error("## UserInfo.getPMOInfo() connection release exception ##");
					ex3.printStackTrace();
				}
			}
		}
	}//end of getPMOInfo() method statement
	
	public String getPMOInfo_conn(String Sv_UserID,Connection conn) throws SQLException, Exception {
		PreparedStatement pstmt       = null;
		ResultSet         rs          = null;
		StringBuffer      strQuery    = new StringBuffer();
		String			  req         = null;
		int			      CNT         = 0;
				
		try {
			strQuery.append("SELECT count(*) CNT FROM CMM0043	    	\n");
			strQuery.append("where cm_userid= ? 				    	\n");  //Sv_UserID
			strQuery.append("  AND cm_rgtcd in ('15','74','78','82')	\n");
			
			pstmt = conn.prepareStatement(strQuery.toString());	
		//	pstmt =  new LoggableStatement(conn, strQuery.toString());
			pstmt.setString(1, Sv_UserID);
		//	ecamsLogger.error(((LoggableStatement)pstmt).getQueryString());
			rs = pstmt.executeQuery();
			
			if (rs.next()){
				CNT = rs.getInt("CNT");
			}//end of while-loop statement
			
			rs.close();
			pstmt.close();
			rs = null;
			pstmt = null;
			
			if(CNT == 0)		
				req = "0";
			else
				req = "1";
			
			return req;
			
		} catch (SQLException sqlexception) {
			sqlexception.printStackTrace();
			ecamsLogger.error("## UserInfo.getPMOInfo_conn() SQLException START ##");
			ecamsLogger.error("## Error DESC : ", sqlexception);	
			ecamsLogger.error("## UserInfo.getPMOInfo_conn() SQLException END ##");			
			throw sqlexception;
		} catch (Exception exception) {
			exception.printStackTrace();
			ecamsLogger.error("## UserInfo.getPMOInfo_conn() Exception START ##");				
			ecamsLogger.error("## Error DESC : ", exception);	
			ecamsLogger.error("## UserInfo.getPMOInfo_conn() Exception END ##");				
			throw exception;
		}finally{
			if (strQuery != null) 	strQuery = null;
			if (rs != null)     try{rs.close();}catch (Exception ex){ex.printStackTrace();}
			if (pstmt != null)  try{pstmt.close();}catch (Exception ex2){ex2.printStackTrace();}
		}
	}//end of getPMOInfo_conn() method statement

	/**
	 * 
	 * <PRE>
	 * 1. MethodName	: getManagerInfo
	 * 2. ClassName		: UserInfo
	 * 3. Commnet			: 본부장/부서장/팀장/파트장/프로그램조회 권한확인 여부
	 * 4. 작성자				: Administrator
	 * 5. 작성일				: 2010. 11. 30. 오후 7:43:48
	 * </PRE>
	 * 		@return String
	 * 		@param Sv_UserID
	 * 		@return
	 * 		@throws SQLException
	 * 		@throws Exception
	 */
	public String getRGTCDInfo(String Sv_UserID) throws SQLException, Exception {
		Connection        conn        = null;
		PreparedStatement pstmt       = null;
		ResultSet         rs          = null;
		StringBuffer      strQuery    = new StringBuffer();
		String			  ret         = "";
		
		ConnectionContext connectionContext = new ConnectionResource();
		
		try {
			
			conn = connectionContext.getConnection();
			
			if (isAdmin_conn(Sv_UserID, conn)) ret = "A";
			else {
				strQuery.setLength(0);
				strQuery.append("SELECT CM_RGTCD \n");
				strQuery.append("  FROM CMM0043 \n");
				strQuery.append(" WHERE CM_USERID= ? \n");
				strQuery.append("   AND CM_RGTCD in ('31','41','51','61','81') \n");
				strQuery.append(" ORDER BY CM_RGTCD \n");
				
				pstmt = conn.prepareStatement(strQuery.toString());
				pstmt.setString(1, Sv_UserID);
				rs = pstmt.executeQuery();
				
				while (rs.next()){
					if (rs.getString("CM_RGTCD").equals("31")) ret = ret + "C";//본부장권한
					if (rs.getString("CM_RGTCD").equals("41")) ret = ret + "D";
					if (rs.getString("CM_RGTCD").equals("51")) ret = ret + "D";
					if (rs.getString("CM_RGTCD").equals("61")) ret = ret + "P";
					if (rs.getString("CM_RGTCD").equals("81")) ret = ret + "M";
				}//end of while-loop statement
				
				rs.close();
				pstmt.close();
			}
			conn.close();
			rs = null;
			pstmt = null;
			conn = null;
			
			return ret;
			
		} catch (SQLException sqlexception) {
			sqlexception.printStackTrace();
			ecamsLogger.error("## UserInfo.getRGTCDInfo() SQLException START ##");
			ecamsLogger.error("## Error DESC : ", sqlexception);	
			ecamsLogger.error("## UserInfo.getRGTCDInfo() SQLException END ##");			
			throw sqlexception;
		} catch (Exception exception) {
			exception.printStackTrace();
			ecamsLogger.error("## UserInfo.getRGTCDInfo() Exception START ##");				
			ecamsLogger.error("## Error DESC : ", exception);	
			ecamsLogger.error("## UserInfo.getRGTCDInfo() Exception END ##");				
			throw exception;
		}finally{
			if (strQuery != null) 	strQuery = null;
			if (rs != null)     try{rs.close();}catch (Exception ex){ex.printStackTrace();}
			if (pstmt != null)  try{pstmt.close();}catch (Exception ex2){ex2.printStackTrace();}
			if (conn != null){
				try{
					ConnectionResource.release(conn);
				}catch(Exception ex3){
					ecamsLogger.error("## UserInfo.getRGTCDInfo() connection release exception ##");
					ex3.printStackTrace();
				}
			}
		}
	}//end of getRGTCDInfo() method statement
	
	public String getSecuInfo(String Sv_UserID) throws SQLException, Exception {
		Connection        conn        = null;
		PreparedStatement pstmt       = null;
		ResultSet         rs          = null;
		StringBuffer      strQuery    = new StringBuffer();
		String			  req         = null;
		int			      CNT         = 0;
		
		ConnectionContext connectionContext = new ConnectionResource();
		
		try {
			conn = connectionContext.getConnection();
			strQuery.append("SELECT count(*) CNT FROM CMM0043	\n");
			strQuery.append("where cm_userid= ? 				\n");
			strQuery.append("  AND cm_rgtcd in ('A1','74')  	\n");
			
			pstmt = conn.prepareStatement(strQuery.toString());	
			pstmt.setString(1, Sv_UserID);
			rs = pstmt.executeQuery();
			
			if (rs.next()){
				CNT = rs.getInt("CNT");
			}//end of while-loop statement
			
			rs.close();
			pstmt.close();
			conn.close();
			
			rs = null;
			pstmt = null;
			conn = null;
			
			if(CNT == 0)		
				req = "0";
			else
				req = "1";
			
			return req;		
			
		} catch (SQLException sqlexception) {
			sqlexception.printStackTrace();
			ecamsLogger.error("## UserInfo.getSecuInfo() SQLException START ##");
			ecamsLogger.error("## Error DESC : ", sqlexception);	
			ecamsLogger.error("## UserInfo.getSecuInfo() SQLException END ##");			
			throw sqlexception;
		} catch (Exception exception) {
			exception.printStackTrace();
			ecamsLogger.error("## UserInfo.getSecuInfo() Exception START ##");				
			ecamsLogger.error("## Error DESC : ", exception);	
			ecamsLogger.error("## UserInfo.getSecuInfo() Exception END ##");				
			throw exception;
		}finally{
			if (strQuery != null) 	strQuery = null;
			if (rs != null)     try{rs.close();}catch (Exception ex){ex.printStackTrace();}
			if (pstmt != null)  try{pstmt.close();}catch (Exception ex2){ex2.printStackTrace();}
			if (conn != null){
				try{
					ConnectionResource.release(conn);
				}catch(Exception ex3){
					ecamsLogger.error("## UserInfo.getSecuInfo() connection release exception ##");
					ex3.printStackTrace();
				}
			}
		}
	}//end of getSecuInfo() method statement
	public boolean getSecuInfo_conn(String Sv_UserID,String RgtCd,Connection conn) throws SQLException, Exception {
		PreparedStatement pstmt       = null;
		ResultSet         rs          = null;
		StringBuffer      strQuery    = new StringBuffer();
		int			      CNT         = 0;
		
		
		try {
			strQuery.append("SELECT count(*) CNT FROM CMM0043	\n");
			strQuery.append("where cm_userid= ? 				\n");
			strQuery.append("  AND instr(?,cm_rgtcd)>0       	\n");
			
			pstmt = conn.prepareStatement(strQuery.toString());	
			pstmt.setString(1, Sv_UserID);
			pstmt.setString(2, RgtCd);
			rs = pstmt.executeQuery();
			
			if (rs.next()){				
				CNT = rs.getInt("CNT");
			}//end of while-loop statement
			
			rs.close();
			pstmt.close();
			
			rs = null;
			pstmt = null;
			
			if(CNT == 0)		
				return false;
			else
				return true;
			
		} catch (SQLException sqlexception) {
			sqlexception.printStackTrace();
			ecamsLogger.error("## UserInfo.getSecuInfo() SQLException START ##");
			ecamsLogger.error("## Error DESC : ", sqlexception);	
			ecamsLogger.error("## UserInfo.getSecuInfo() SQLException END ##");			
			throw sqlexception;
		} catch (Exception exception) {
			exception.printStackTrace();
			ecamsLogger.error("## UserInfo.getSecuInfo() Exception START ##");				
			ecamsLogger.error("## Error DESC : ", exception);	
			ecamsLogger.error("## UserInfo.getSecuInfo() Exception END ##");				
			throw exception;
		}finally{
			if (strQuery != null) 	strQuery = null;
			if (rs != null)     try{rs.close();}catch (Exception ex){ex.printStackTrace();}
			if (pstmt != null)  try{pstmt.close();}catch (Exception ex2){ex2.printStackTrace();}
		}
	}//end of getSecuInfo() method statement
	
	
	public Object[] getSecuList(String Sv_UserID) throws SQLException, Exception {
		Connection        conn        = null;
		PreparedStatement pstmt       = null;
		ResultSet         rs          = null;
		StringBuffer      strQuery    = new StringBuffer();
		HashMap<String, String>			  rst		  = null;
		ArrayList<HashMap<String, String>>		  rtList	  = new ArrayList<HashMap<String, String>>();
		
		ConnectionContext connectionContext = new ConnectionResource();
		
		try {
			conn = connectionContext.getConnection();
			rtList.clear();
			strQuery.append("SELECT c.cm_filename   	          \n");
			strQuery.append("  FROM cmm0080 c,cmm0090 b,CMM0043 a \n");
			strQuery.append(" where a.cm_userid= ? 		          \n");  //Sv_UserID
			strQuery.append("   and a.cm_rgtcd=b.cm_rgtcd         \n");  	
			strQuery.append("   and b.cm_menucd=c.cm_menucd       \n"); 
			strQuery.append(" union     \n");
			strQuery.append("SELECT c.cm_filename   	          \n");
			strQuery.append("  FROM cmm0080 c,cmm0090 b,CMM0043 a,cmm0040 d,cmm0042 e \n");
			strQuery.append(" where e.cm_blkstdate is not null     \n");
			strQuery.append("   and e.cm_blkeddate is not null     \n");
			strQuery.append("   and to_char(e.cm_blkstdate,'yyyymmdd')<=to_char(SYSDATE,'yyyymmdd') \n");
			strQuery.append("   and to_char(e.cm_blkeddate,'yyyymmdd')>=to_char(SYSDATE,'yyyymmdd') \n");
			strQuery.append("   and e.cm_daeusr(+)=?                \n");
			strQuery.append("   and d.cm_userid=e.cm_userid(+)      \n");  //Sv_UserID
			strQuery.append("   and d.cm_userid=a.cm_userid      \n");  //Sv_UserID
			strQuery.append("   and a.cm_rgtcd=b.cm_rgtcd         \n");  	
			strQuery.append("   and b.cm_menucd=c.cm_menucd       \n");  
			strQuery.append(" group by cm_filename                \n");  		
			pstmt = conn.prepareStatement(strQuery.toString());	
			pstmt.setString(1, Sv_UserID);
			pstmt.setString(2, Sv_UserID);
			rs = pstmt.executeQuery();
			while (rs.next()){
				rst = new HashMap<String, String>();
				rst.put("cm_filename", rs.getString("cm_filename"));
				rtList.add(rst);
				
			}//end of while-loop statement			
			rs.close();
			pstmt.close();
			conn.close();
			
			rs = null;
			pstmt = null;
			conn = null;
			
			
			return rtList.toArray();		
			
		} catch (SQLException sqlexception) {
			sqlexception.printStackTrace();
			ecamsLogger.error("## UserInfo.getSecuList() SQLException START ##");
			ecamsLogger.error("## Error DESC : ", sqlexception);	
			ecamsLogger.error("## UserInfo.getSecuList() SQLException END ##");			
			throw sqlexception;
		} catch (Exception exception) {
			exception.printStackTrace();
			ecamsLogger.error("## UserInfo.getSecuList() Exception START ##");				
			ecamsLogger.error("## Error DESC : ", exception);	
			ecamsLogger.error("## UserInfo.getSecuList() Exception END ##");				
			throw exception;
		}finally{
			if (strQuery != null) 	strQuery = null;
			if (rs != null)     try{rs.close();}catch (Exception ex){ex.printStackTrace();}
			if (pstmt != null)  try{pstmt.close();}catch (Exception ex2){ex2.printStackTrace();}
			if (conn != null){
				try{
					ConnectionResource.release(conn);
				}catch(Exception ex3){
					ecamsLogger.error("## UserInfo.getSecuList() connection release exception ##");
					ex3.printStackTrace();
				}
			}
		}
	}//end of getSecuList() method statement
	
	/**
	 * <PRE>
	 * 1. MethodName	: isAdmin
	 * 2. ClassName		: UserInfo
	 * 3. Commnet			: 관리자여부 체크. CM_ADMIN = '1' 이며 true
	 * 4. 작성자				: no name
	 * 5. 작성일				: 2011. 1. 12. 오후 1:11:56
	 * </PRE>
	 * 		@return Boolean
	 * 		@param UserID
	 * 		@return
	 * 		@throws SQLException
	 * 		@throws Exception
	 */
	public Boolean isAdmin(String UserID) throws SQLException, Exception {
		Connection        conn        = null;
		PreparedStatement pstmt       = null;
		ResultSet         rs          = null;
		StringBuffer      strQuery    = new StringBuffer();
		int				  countPermit;
		
		ConnectionContext connectionContext = new ConnectionResource();
		
		try {
			
			if (UserID.equals("") || UserID == null){
				return false;
			}
			conn = connectionContext.getConnection();
			
			strQuery.append("SELECT distinct count(*) as countPermit \n");
			strQuery.append("  FROM CMM0040 \n");
			strQuery.append(" WHERE cm_userid = ? \n");
			strQuery.append("   AND CM_ADMIN = '1' \n");
			strQuery.append("   AND CM_ACTIVE = '1' \n");
			strQuery.append("   AND CM_CLSDATE is null \n");
            pstmt = conn.prepareStatement(strQuery.toString());	
            pstmt.setString(1, UserID);	
            rs = pstmt.executeQuery();
            countPermit = 0;
			if (rs.next()){
				countPermit = rs.getInt("countPermit");				
			}//end of while-loop statement
			rs.close();
			pstmt.close();
			conn.close();
			rs = null;
			pstmt = null;
			conn = null;
			
			if (countPermit > 0)
				return true;
			else
				return false;			
	
			
		} catch (SQLException sqlexception) {
			sqlexception.printStackTrace();
			ecamsLogger.error("## UserInfo.isAdmin() SQLException START ##");
			ecamsLogger.error("## Error DESC : ", sqlexception);	
			ecamsLogger.error("## UserInfo.isAdmin() SQLException END ##");			
			throw sqlexception;
		} catch (Exception exception) {
			exception.printStackTrace();
			ecamsLogger.error("## UserInfo.isAdmin() Exception START ##");				
			ecamsLogger.error("## Error DESC : ", exception);	
			ecamsLogger.error("## UserInfo.isAdmin() Exception END ##");				
			throw exception;
		}finally{
			if (strQuery != null) 	strQuery = null;
			if (rs != null)     try{rs.close();}catch (Exception ex){ex.printStackTrace();}
			if (pstmt != null)  try{pstmt.close();}catch (Exception ex2){ex2.printStackTrace();}
			if (conn != null){
				try{
					ConnectionResource.release(conn);
				}catch(Exception ex3){
					ecamsLogger.error("## UserInfo.isAdmin() connection release exception ##");
					ex3.printStackTrace();
				}
			}
		}
	}//end of isAdmin() method statement

	/**
	 * <PRE>
	 * 1. MethodName	: isAdmin_conn
	 * 2. ClassName		: UserInfo
	 * 3. Commnet			: 관리자여부 체크. CM_ADMIN = '1' 이며 true
	 * 4. 작성자				: Administrator
	 * 5. 작성일				: 2011. 1. 12. 오후 1:12:57
	 * </PRE>
	 * 		@return Boolean
	 * 		@param UserID
	 * 		@param conn
	 * 		@return
	 * 		@throws SQLException
	 * 		@throws Exception
	 */
	public Boolean isAdmin_conn(String UserID,Connection conn) throws SQLException, Exception {
		PreparedStatement pstmt       = null;
		ResultSet         rs          = null;
		StringBuffer      strQuery    = new StringBuffer();
		int				  countPermit;
		
		
		try {
			
			if (UserID.equals("") || UserID == null){
				return false;
			}
			
			strQuery.append("SELECT distinct count(*) as countPermit \n");
			strQuery.append("FROM CMM0040 \n");
			strQuery.append("WHERE cm_userid = ? \n");
			strQuery.append("AND CM_ADMIN = '1' \n");
			strQuery.append("AND CM_ACTIVE = '1' \n");
			strQuery.append("AND CM_CLSDATE is null \n");
			
            pstmt = conn.prepareStatement(strQuery.toString());	
            //pstmt = new LoggableStatement(conn,strQuery.toString());
            pstmt.setString(1, UserID);	
            
            
            //ecamsLogger.error(((LoggableStatement)pstmt).getQueryString());  
            rs = pstmt.executeQuery();
            
            countPermit = 0;
			while (rs.next()){
				countPermit = rs.getInt("countPermit");				
			}//end of while-loop statement
			rs.close();
			pstmt.close();
			rs = null;
			pstmt = null;
			
			if (countPermit > 0)
				return true;
			else
				return false;			
	
			
		} catch (SQLException sqlexception) {
			sqlexception.printStackTrace();
			ecamsLogger.error("## UserInfo.isAdmin_conn() SQLException START ##");
			ecamsLogger.error("## Error DESC : ", sqlexception);	
			ecamsLogger.error("## UserInfo.isAdmin_conn() SQLException END ##");			
			throw sqlexception;
		} catch (Exception exception) {
			exception.printStackTrace();
			ecamsLogger.error("## UserInfo.isAdmin_conn() Exception START ##");				
			ecamsLogger.error("## Error DESC : ", exception);	
			ecamsLogger.error("## UserInfo.isAdmin_conn() Exception END ##");				
			throw exception;
		}finally{
			if (strQuery != null) 	strQuery = null;
			if (rs != null)     try{rs.close();}catch (Exception ex){ex.printStackTrace();}
			if (pstmt != null)  try{pstmt.close();}catch (Exception ex2){ex2.printStackTrace();}
		}
	}//end of isAdmin_conn() method statement
	
	
	/**
	 * <PRE>
	 * 1. MethodName	: getUserRGTCD
	 * 2. ClassName		: UserInfo
	 * 3. Commnet			: 
	 * 4. 작성자				: no name
	 * 5. 작성일				: 2011. 4. 1. 오후 1:47:05
	 * </PRE>
	 * 		@return Boolean
	 * 		@param UserID : 사용자ID
	 * 		@return
	 * 		@throws SQLException
	 * 		@throws Exception
	 */
	public String getUserRGTCD(String UserID,String RGTCD,String closeYn) throws SQLException, Exception {
		Connection        conn        = null;
		PreparedStatement pstmt       = null;
		ResultSet         rs          = null;
		StringBuffer      strQuery    = new StringBuffer();
		String            returnStr = "";
		
		ConnectionContext connectionContext = new ConnectionResource();
		
		try {
			conn = connectionContext.getConnection();

			strQuery.append("select b.cm_rgtcd,c.cm_codename \n");
			strQuery.append("  from cmm0043 b,cmm0020 c \n");
			strQuery.append(" where b.cm_userid= ? \n");
			strQuery.append("   and b.cm_rgtcd=c.cm_micode \n");
			strQuery.append("   and c.cm_macode='RGTCD' \n");
			String[] rgtcd = RGTCD.split(",");
			
			if(!"".equals(RGTCD) && RGTCD != null) {
				if (rgtcd.length > 0){
					strQuery.append(" and b.cm_rgtcd in ( \n");
					if (rgtcd.length == 1)
						strQuery.append(" ? ");
					else{
						for (int i=0;i<rgtcd.length;i++){
							if (i == rgtcd.length-1)
								strQuery.append(" ? ");
							else
								strQuery.append(" ? ,");
						}
					}
					strQuery.append(" ) \n");
				}
			}
	        if (closeYn.toUpperCase().equals("N") || closeYn.toUpperCase().equals("0")){
	        	strQuery.append("   and c.cm_closedt is null \n");
	        }
	        
            pstmt = conn.prepareStatement(strQuery.toString());
	        //pstmt = new LoggableStatement(conn, strQuery.toString());
	        int paramInd = 0;
	        pstmt.setString(++paramInd, UserID);
	        if(!"".equals(RGTCD) && RGTCD != null) {
				for (int i=0;i<rgtcd.length;i++){
					pstmt.setString(++paramInd, rgtcd[i]);
				}
	        }
            //ecamsLogger.error(((LoggableStatement)pstmt).getQueryString());
            rs = pstmt.executeQuery();
            returnStr = "";
			while (rs.next()){
				returnStr  = returnStr + rs.getString("cm_rgtcd") + ",";
			}//end of while-loop statement
			
			rs.close();
			pstmt.close();
			conn.close();
			rs = null;
			pstmt = null;
			conn = null;
			
			return returnStr;
			
		} catch (SQLException sqlexception) {
			sqlexception.printStackTrace();
			ecamsLogger.error("## UserInfo.getUserRGTCD() SQLException START ##");
			ecamsLogger.error("## Error DESC : ", sqlexception);	
			ecamsLogger.error("## UserInfo.getUserRGTCD() SQLException END ##");			
			throw sqlexception;
		} catch (Exception exception) {
			exception.printStackTrace();
			ecamsLogger.error("## UserInfo.getUserRGTCD() Exception START ##");				
			ecamsLogger.error("## Error DESC : ", exception);	
			ecamsLogger.error("## UserInfo.getUserRGTCD() Exception END ##");				
			throw exception;
		}finally{
			if (strQuery != null) 	strQuery = null;
			if (rs != null)     try{rs.close();}catch (Exception ex){ex.printStackTrace();}
			if (pstmt != null)  try{pstmt.close();}catch (Exception ex2){ex2.printStackTrace();}
			if (conn != null){
				try{
					ConnectionResource.release(conn);
				}catch(Exception ex3){
					ecamsLogger.error("## UserInfo.getUserRGTCD() connection release exception ##");
					ex3.printStackTrace();
				}
			}
		}
	}//end of getUserRGTCD() method statement
	
	public String getUserRGTCDList(String UserID,String closeYn) throws SQLException, Exception {
		Connection        conn        = null;
		PreparedStatement pstmt       = null;
		ResultSet         rs          = null;
		StringBuffer      strQuery    = new StringBuffer();
		String            returnStr = "";
		
		ConnectionContext connectionContext = new ConnectionResource();
		
		try {
			conn = connectionContext.getConnection();

			strQuery.append("select b.cm_rgtcd,c.cm_codename \n");
			strQuery.append("  from cmm0043 b,cmm0020 c \n");
			strQuery.append(" where b.cm_userid= ? \n");
			strQuery.append("   and b.cm_rgtcd=c.cm_micode \n");
			strQuery.append("   and c.cm_macode='RGTCD' \n");

	        if (closeYn.toUpperCase().equals("N") || closeYn.toUpperCase().equals("0")){
	        	strQuery.append("   and c.cm_closedt is null \n");
	        }
	        
            pstmt = conn.prepareStatement(strQuery.toString());
	        //pstmt = new LoggableStatement(conn, strQuery.toString());
	       
	        pstmt.setString(1 , UserID);
		
            //ecamsLogger.error(((LoggableStatement)pstmt).getQueryString());
            rs = pstmt.executeQuery();
            returnStr = "";
			while (rs.next()){
				returnStr  = returnStr + rs.getString("cm_rgtcd") + ",";
			}//end of while-loop statement
			
			rs.close();
			pstmt.close();
			conn.close();
			rs = null;
			pstmt = null;
			conn = null;
			
			return returnStr;
			
		} catch (SQLException sqlexception) {
			sqlexception.printStackTrace();
			ecamsLogger.error("## UserInfo.getUserRGTCDList() SQLException START ##");
			ecamsLogger.error("## Error DESC : ", sqlexception);	
			ecamsLogger.error("## UserInfo.getUserRGTCDList() SQLException END ##");			
			throw sqlexception;
		} catch (Exception exception) {
			exception.printStackTrace();
			ecamsLogger.error("## UserInfo.getUserRGTCDList() Exception START ##");				
			ecamsLogger.error("## Error DESC : ", exception);	
			ecamsLogger.error("## UserInfo.getUserRGTCDList() Exception END ##");				
			throw exception;
		}finally{
			if (strQuery != null) 	strQuery = null;
			if (rs != null)     try{rs.close();}catch (Exception ex){ex.printStackTrace();}
			if (pstmt != null)  try{pstmt.close();}catch (Exception ex2){ex2.printStackTrace();}
			if (conn != null){
				try{
					ConnectionResource.release(conn);
				}catch(Exception ex3){
					ecamsLogger.error("## UserInfo.getUserRGTCDList() connection release exception ##");
					ex3.printStackTrace();
				}
			}
		}
	}//end of getUserRGTCD() method statement
	
	/** 부장, 부부장만 검색 **/
	public String getUserB(String UserID) throws SQLException, Exception {
		Connection        conn        = null;
		PreparedStatement pstmt       = null;
		ResultSet         rs          = null;
		StringBuffer      strQuery    = new StringBuffer();
		String            returnStr = "";
		
		ConnectionContext connectionContext = new ConnectionResource();
		
		try {
			conn = connectionContext.getConnection();
			
			strQuery.setLength(0);
			//부장, 감사 검색
			/*		31	부장
					Q2	감사			*/
			strQuery.append(" select cm_rgtcd from cmm0043  \n");
			strQuery.append(" where cm_userid = ?			\n");
//			strQuery.append(" and cm_rgtcd in ('31', 'Q2')	\n");
			strQuery.append(" and cm_rgtcd in ('31','Q1','Q2','32')	\n"); // 20221214 팀장도 일괄결재, 일괄반려 가능하게 추가
			
			//변경 전(부장, 부부장, 팀장 검색)
			/*			00019	부장
						01005	부부장
						01057	팀장			*/
			//strQuery.append(" select cm_position from cmm0040  				 \n");
			//strQuery.append(" where cm_userid = ?  							 \n");
			//strQuery.append(" and cm_position in ('00019' , '01005', '01057')  \n");
			
			pstmt = conn.prepareStatement(strQuery.toString());
			pstmt.setString(1, UserID);
			rs = pstmt.executeQuery();
			
			if (rs.next()){
				returnStr = "Y";
			}else{
				returnStr = "N";
			}
			
			rs.close();
			pstmt.close();
			conn.close();
			rs = null;
			pstmt = null;
			conn = null;
			
			return returnStr;

		}catch (SQLException sqlexception) {
			sqlexception.printStackTrace();
			ecamsLogger.error("## UserInfo.getUserB() SQLException START ##");
			ecamsLogger.error("## Error DESC : ", sqlexception);	
			ecamsLogger.error("## UserInfo.getUserB() SQLException END ##");			
			throw sqlexception;
		} catch (Exception exception) {
			exception.printStackTrace();
			ecamsLogger.error("## UserInfo.getUserB() Exception START ##");				
			ecamsLogger.error("## Error DESC : ", exception);	
			ecamsLogger.error("## UserInfo.getUserB() Exception END ##");				
			throw exception;
		}finally{
			if (strQuery != null) 	strQuery = null;
			if (rs != null)     try{rs.close();}catch (Exception ex){ex.printStackTrace();}
			if (pstmt != null)  try{pstmt.close();}catch (Exception ex2){ex2.printStackTrace();}
			if (conn != null){
				try{
					ConnectionResource.release(conn);
				}catch(Exception ex3){
					ecamsLogger.error("## UserInfo.getUserB() connection release exception ##");
					ex3.printStackTrace();
				}
			}
		}
	}//end of getUserB() method statemen
	
	public Object[] getOtherUser() throws SQLException, Exception {
		Connection        conn        = null;
		PreparedStatement pstmt       = null;
		ResultSet         rs          = null;
		StringBuffer      strQuery    = new StringBuffer();
		Object[]		  rtObj		  = null;
		ArrayList<HashMap<String, String>>	rtList  = new ArrayList<HashMap<String, String>>();
		HashMap<String, String>			  	rst	 	= null;
		
		ConnectionContext connectionContext = new ConnectionResource();
		
		try {
			conn = connectionContext.getConnection();
			
			strQuery.setLength(0);
			strQuery.append(" select a.cm_userid, a.cm_username, a.cm_project, a.cm_position, a.cm_duty, b.cm_deptname, d.cm_codename 	\n");
			strQuery.append(" from cmm0040 a, cmm0100 b, cmm0043 c, cmm0020 d											  				\n");
			strQuery.append(" where a.cm_manid = 'N'				  																	\n");
			strQuery.append(" and b.cm_deptcd = a.cm_project																			\n");
			strQuery.append(" and a.cm_userid = c.cm_userid																				\n");
			strQuery.append(" and c.cm_rgtcd = '92'																						\n");
			strQuery.append(" and d.cm_macode = 'RGTCD'																					\n");
			strQuery.append(" and c.cm_rgtcd = d.cm_micode																				\n");
			
			pstmt = conn.prepareStatement(strQuery.toString());
			rs = pstmt.executeQuery();
			
			while (rs.next()){
				rst = new HashMap<String, String>();
				rst.put("cm_userid", rs.getString("cm_userid"));
				rst.put("cm_username", rs.getString("cm_username"));
				rst.put("user", rs.getString("cm_username") +" ["+ rs.getString("cm_deptname") +"]" );
				rst.put("cm_project", rs.getString("cm_project"));
				rst.put("cm_position", rs.getString("cm_position"));
				rst.put("cm_duty", rs.getString("cm_duty"));
				rst.put("cm_deptname", rs.getString("cm_deptname"));
				rst.put("cm_codename", rs.getString("cm_codename"));
				rst.put("check" , "Y");
				
				rtList.add(rst);
				rst = null;

			}
			rs.close();
			pstmt.close();
			conn.close();
			rs = null;
			pstmt = null;
			conn = null;
			
			rtObj = rtList.toArray();
			rtList = null;
			return rtObj;

		}catch (SQLException sqlexception) {
			sqlexception.printStackTrace();
			ecamsLogger.error("## UserInfo.getOtherUser() SQLException START ##");
			ecamsLogger.error("## Error DESC : ", sqlexception);	
			ecamsLogger.error("## UserInfo.getOtherUser() SQLException END ##");			
			throw sqlexception;
		} catch (Exception exception) {
			exception.printStackTrace();
			ecamsLogger.error("## UserInfo.getOtherUser() Exception START ##");				
			ecamsLogger.error("## Error DESC : ", exception);	
			ecamsLogger.error("## UserInfo.getOtherUser() Exception END ##");				
			throw exception;
		}finally{
			if (strQuery != null) 	strQuery = null;
			if (rs != null)     try{rs.close();}catch (Exception ex){ex.printStackTrace();}
			if (pstmt != null)  try{pstmt.close();}catch (Exception ex2){ex2.printStackTrace();}
			if (conn != null){
				try{
					ConnectionResource.release(conn);
				}catch(Exception ex3){
					ecamsLogger.error("## UserInfo.getOtherUser() connection release exception ##");
					ex3.printStackTrace();
				}
			}
		}
	}//end of getOtherUser() method statemen

	public String normalOtherUser(String userID) throws SQLException, Exception {
		Connection        conn        = null;
		PreparedStatement pstmt       = null;
		ResultSet         rs          = null;
		StringBuffer      strQuery    = new StringBuffer();
		String 			  strReturn   = "";

		
		ConnectionContext connectionContext = new ConnectionResource();
		
		try {
			conn = connectionContext.getConnection();
			
	        strQuery.setLength(0);
	        strQuery.append(" select cm_userid from cmm0040 \n");
	        strQuery.append(" where cm_userid = ?           \n");
	        strQuery.append(" and cm_manid = 'N'            \n");
	        pstmt = conn.prepareStatement(strQuery.toString());
	        pstmt.setString(1, userID);
	        rs = pstmt.executeQuery();

			if (rs.next()){
				strReturn = "otherUser";
			}else{
				strReturn = "normalUser";
			}
		

			rs.close();
			pstmt.close();
			conn.close();
			rs = null;
			pstmt = null;
			conn = null;
			
			return strReturn;


		}catch (SQLException sqlexception) {
			sqlexception.printStackTrace();
			ecamsLogger.error("## UserInfo.normalOtherUser() SQLException START ##");
			ecamsLogger.error("## Error DESC : ", sqlexception);	
			ecamsLogger.error("## UserInfo.normalOtherUser() SQLException END ##");			
			throw sqlexception;
		} catch (Exception exception) {
			exception.printStackTrace();
			ecamsLogger.error("## UserInfo.normalOtherUser() Exception START ##");				
			ecamsLogger.error("## Error DESC : ", exception);	
			ecamsLogger.error("## UserInfo.normalOtherUser() Exception END ##");				
			throw exception;
		}finally{
			if (strQuery != null) 	strQuery = null;
			if (rs != null)     try{rs.close();}catch (Exception ex){ex.printStackTrace();}
			if (pstmt != null)  try{pstmt.close();}catch (Exception ex2){ex2.printStackTrace();}
			if (conn != null){
				try{
					ConnectionResource.release(conn);
				}catch(Exception ex3){
					ecamsLogger.error("## UserInfo.normalOtherUser() connection release exception ##");
					ex3.printStackTrace();
				}
			}
		}
	}//end of normalOtherUser() method statemen

	/** eCAMS관리자, 프로그램관리자 조회 **/
	public int getAdminRGTCD(String UserID) throws SQLException, Exception {
		Connection        conn        = null;
		PreparedStatement pstmt       = null;
		ResultSet         rs          = null;
		StringBuffer      strQuery    = new StringBuffer();
		int count = 0;
		
		ConnectionContext connectionContext = new ConnectionResource();
		
		try {
			conn = connectionContext.getConnection();
			
			strQuery.setLength(0);
			strQuery.append(" select count(*) as count  					  \n");
			strQuery.append(" from cmm0043 a, cmm0020 b						  \n");
			strQuery.append(" where a.cm_userid= ?   				       	  \n");
			strQuery.append(" and a.cm_rgtcd in('LIB','A1')				      \n");
			strQuery.append(" and a.cm_rgtcd= b.cm_micode    				  \n");
			strQuery.append(" and b.cm_macode='RGTCD'    				      \n");
			
			pstmt = conn.prepareStatement(strQuery.toString());
			pstmt.setString(1, UserID);
			rs = pstmt.executeQuery();
			
			if(rs.next()){
				count = rs.getInt("COUNT");
			}
			
			rs.close();
			pstmt.close();
			conn.close();
			rs = null;
			pstmt = null;
			conn = null; 
			
			return count;

		}catch (SQLException sqlexception) {
			sqlexception.printStackTrace();
			ecamsLogger.error("## UserInfo.getAdminRGTCD() SQLException START ##");
			ecamsLogger.error("## Error DESC : ", sqlexception);	
			ecamsLogger.error("## UserInfo.getAdminRGTCD() SQLException END ##");			
			throw sqlexception;
		} catch (Exception exception) {
			exception.printStackTrace();
			ecamsLogger.error("## UserInfo.getAdminRGTCD() Exception START ##");				
			ecamsLogger.error("## Error DESC : ", exception);	
			ecamsLogger.error("## UserInfo.getAdminRGTCD() Exception END ##");				
			throw exception;
		}finally{
			if (strQuery != null) 	strQuery = null;
			if (rs != null)     try{rs.close();}catch (Exception ex){ex.printStackTrace();}
			if (pstmt != null)  try{pstmt.close();}catch (Exception ex2){ex2.printStackTrace();}
			if (conn != null){
				try{
					ConnectionResource.release(conn);
				}catch(Exception ex3){
					ecamsLogger.error("## UserInfo.getAdminRGTCD() connection release exception ##");
					ex3.printStackTrace();
				}
			}
		}
	}//end of getAdminRGTCD() method statemen
}//end of UserInfo class statement
