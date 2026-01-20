
/*****************************************************************************************
	1. program ID	: MenuList.java
	2. create date	: 2006.08. 08
	3. auth		    : teok.kang
	4. update date	: 
	5. auth		    : 
	6. description	: BBS DAO
*****************************************************************************************/

package app.common;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

import org.apache.logging.log4j.Logger;
import com.ecams.common.dbconn.ConnectionContext;
import com.ecams.common.dbconn.ConnectionResource;
import com.ecams.common.logger.EcamsLogger;
import org.w3c.dom.Document;
import app.common.CreateXml;
import html.app.common.token.service.TokenService;




/**
 * @author bigeyes
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */


public class MenuList{
	
    /**
     * Logger Class Instance Creation
     * logger
     */
    Logger ecamsLogger  = EcamsLogger.getLoggerInstance();
    
	/**
	 * 게시판을 조회합니다.(구분값을 주어)
	 * @param  gbn
	 * @return ArrayList
	 * @throws SQLException
	 * @throws Exception
	 */
	
    public Object[] SelectMenuList(String user_id) throws SQLException, Exception {
		Connection        conn        = null;
		PreparedStatement pstmt       = null;
		ResultSet         rs          = null;
		StringBuffer      strQuery    = new StringBuffer();
		CreateXml      ecmmtb      = new CreateXml();
		Object[] returnObjectArray = null;
		ArrayList<Document> list = new ArrayList<Document>();
		ConnectionContext connectionContext = new ConnectionResource();	
		
		
		try {			
			conn = connectionContext.getConnection();

            strQuery.append("SELECT DISTINCT B.CM_MENUCD   CM_MENUCD");
            strQuery.append(" , B.CM_MANAME   CM_MANAME");
            strQuery.append(" , B.CM_BEFMENU  CM_BEFMENU");
            strQuery.append(" , B.CM_FILENAME CM_FILENAME");
            strQuery.append(" , B.CM_ORDER    CM_ORDER");
            strQuery.append(" FROM CMM0080 B , CMM0090 A"); 
            strQuery.append(" WHERE A.CM_RGTCD IN(SELECT CM_RGTCD"); 
            strQuery.append(" FROM CMM0043 ");
            strQuery.append(" WHERE CM_USERID= ?)");   
            strQuery.append(" AND A.CM_MENUCD  = B.CM_MENUCD");
            strQuery.append(" AND B.CM_BEFMENU <> 999");
            strQuery.append(" ORDER BY  B.CM_BEFMENU, B.CM_ORDER, B.CM_MENUCD, B.CM_MANAME, B.CM_FILENAME");   	            
            
            
            pstmt = conn.prepareStatement(strQuery.toString());
            pstmt.setString(1, user_id);		
			
            rs = pstmt.executeQuery();	            
            
            ecmmtb.init_Xml("ID","cm_menucd","cm_maname","cm_befmenu","cm_filename","cm_order");
            
			while (rs.next()){
				ecmmtb.addXML(rs.getString("cm_menucd"),rs.getString("cm_menucd"),
						rs.getString("cm_maname"),rs.getString("cm_befmenu"),
						rs.getString("cm_filename"),rs.getString("cm_order"),
						rs.getString("cm_befmenu"));
			}//end of while-loop statement
			
			rs.close();
			pstmt.close();
			conn.close();
			
			
			list.add(ecmmtb.getDocument());
			
			returnObjectArray = list.toArray();

			list = null;
			ecmmtb = null;
			//ecamsLogger.error(ecmmtb.xml_toStr());
			
			return returnObjectArray;			
						
			
		} catch (SQLException sqlexception) {
			sqlexception.printStackTrace();
			ecamsLogger.error("## MenuList.SelectMenuList() SQLException START ##");
			ecamsLogger.error("## Error DESC : ", sqlexception);	
			ecamsLogger.error("## MenuList.SelectMenuList() SQLException END ##");			
			throw sqlexception;
		} catch (Exception exception) {
			exception.printStackTrace();
			ecamsLogger.error("## MenuList.SelectMenuList() Exception START ##");				
			ecamsLogger.error("## Error DESC : ", exception);	
			ecamsLogger.error("## MenuList.SelectMenuList() Exception END ##");				
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
					ecamsLogger.error("## MenuList.SelectMenuList() connection release exception ##");
					ex3.printStackTrace();
				}
			}
		}		
	}//end of SelectMenuList() method statement
    
    public Object[] secuMenuList_html(String user_id) throws SQLException, Exception {
    	Connection        conn        = null;
		PreparedStatement pstmt       = null;
		ResultSet         rs          = null;
		StringBuffer      strQuery    = new StringBuffer();
		Object[]		  rtObj		  = null;
		ConnectionContext connectionContext = new ConnectionResource();	
		HashMap<String, String>			    rst	   = null;
		ArrayList<HashMap<String, String>>  rtList = new ArrayList<HashMap<String, String>>();
				
		try {			
			conn = connectionContext.getConnection();
			
			strQuery.setLength(0);
			strQuery.append("SELECT																	\n");
			strQuery.append("		A.ID,                                                       	\n");
			strQuery.append("		A.PID,                                                      	\n");
			strQuery.append("		A.MENUORDER,                                                	\n");
			strQuery.append("		A.TEXT,                                                     	\n");
			strQuery.append("		DECODE(A.PID,0,'',A.LINK) AS LINK,                          	\n");
			strQuery.append("		A.CM_MENUCD,                                                	\n");
			strQuery.append("		A.CM_REQCD,                                                 	\n");
			strQuery.append("		DECODE(A.PID,0,'','ecamsSubframe') AS TARGETNAME            	\n");
			strQuery.append("  FROM (SELECT CASE WHEN A.CM_BEFMENU = 0 THEN TO_CHAR(A.CM_MENUCD)	\n");
			strQuery.append("			    ELSE A.CM_MENUCD || '_' || A.CM_BEFMENU             	\n");
			strQuery.append("			    END ID,                                             	\n");
			strQuery.append("		   		A.CM_BEFMENU AS PID,                                	\n");
			strQuery.append("		   		A.CM_ORDER AS MENUORDER,                            	\n");
			strQuery.append("		   		A.CM_MANAME AS TEXT,                                	\n");
			strQuery.append("		  		NVL(A.CM_HTMLFILE,'/') AS LINK,                         \n");
			strQuery.append("		   		A.CM_MENUCD,                                        	\n");
			strQuery.append("		   		A.CM_REQCD                                          	\n");
			strQuery.append("	       FROM CMM0080 A, CMM0090 B                                	\n");
			strQuery.append("	      WHERE B.CM_RGTCD IN (SELECT CM_RGTCD FROM CMM0043         	\n");
			strQuery.append("	                            WHERE CM_USERID = ?)                	\n");
			strQuery.append("	  		AND A.CM_MENUCD = B.CM_MENUCD                           	\n");
			strQuery.append("	  		AND a.CM_BEFMENU <> '0'                                 	\n");
			strQuery.append("  		  GROUP BY A.CM_MENUCD,A.CM_BEFMENU,A.CM_ORDER,A.CM_MANAME,A.CM_HTMLFILE,A.CM_REQCD	\n");
			strQuery.append("		UNION ALL 														\n");
			strQuery.append("		 SELECT CASE WHEN A.CM_BEFMENU = 0 THEN TO_CHAR(A.CM_MENUCD) 	\n");
			strQuery.append("		  		ELSE A.CM_MENUCD || '_' || A.CM_BEFMENU            		\n");                                       
			strQuery.append("		  		END ID,                                           		\n");                                       
			strQuery.append("	   			A.CM_BEFMENU AS PID,                                  	\n");                                       
			strQuery.append("	   			A.CM_ORDER AS MENUORDER,                              	\n");                                       
			strQuery.append("	   			A.CM_MANAME AS TEXT,                                  	\n");                                       
			strQuery.append("	   			NVL(A.CM_HTMLFILE,'/') AS LINK,                         \n");                                       
			strQuery.append("	   			A.CM_MENUCD,                                          	\n");                                       
			strQuery.append("	   			A.CM_REQCD											 	\n");
			strQuery.append("		   FROM cmm0080 A, cmm0090 B, cmm0080 C						 	\n");
			strQuery.append("		  WHERE A.CM_BEFMENU = '0'									 	\n");
			strQuery.append("			AND B.CM_RGTCD IN( SELECT CM_RGTCD FROM CMM0043             \n");                                     
			strQuery.append("                      			WHERE CM_USERID = ?)			        \n");
			strQuery.append("			AND B.CM_MENUCD = C.CM_MENUCD 								\n");
			strQuery.append("			AND A.CM_MENUCD = C.CM_BEFMENU								\n");
			strQuery.append("			AND A.CM_MENUCD <> '900'									\n");	//2022.09.08 산출물관리 제외 (CMM0080에서 빼면 FLEX 메뉴JSP에서 배열오류)
			strQuery.append("		  GROUP BY A.CM_MENUCD,A.CM_BEFMENU,A.CM_ORDER,A.CM_MANAME,A.CM_HTMLFILE,A.CM_REQCD \n");
			strQuery.append("		) A                                                             \n");
			strQuery.append("START WITH A.PID = 0                                                   \n");
			strQuery.append("CONNECT BY PRIOR A.CM_MENUCD = A.PID                                   \n");
			strQuery.append("ORDER SIBLINGS BY A.MENUORDER                                          \n");  
			
            pstmt = conn.prepareStatement(strQuery.toString());
            //pstmt = new LoggableStatement(conn, strQuery.toString());
            pstmt.setString(1, user_id);
            pstmt.setString(2, user_id);
            //ecamsLogger.error(((LoggableStatement)pstmt).getQueryString());
            rs = pstmt.executeQuery();    
			while (rs.next()){
				rst = new HashMap<String, String>();
				rst.put("id", 		rs.getString("ID"));
				rst.put("pid", 		rs.getString("PID"));
				rst.put("order", 	rs.getString("MENUORDER"));
				rst.put("text", 	rs.getString("TEXT"));
				rst.put("link", 	rs.getString("LINK"));	
				rst.put("reqcd", 	rs.getString("CM_REQCD"));	
				rst.put("targetname",rs.getString("TARGETNAME"));	
				rtList.add(rst);
				rst = null;				
			}//end of while-loop statement
			
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
			sqlexception.getStackTrace();
			ecamsLogger.error("## MenuList.secuMenuList_html() SQLException START ##");
			ecamsLogger.error("## Error DESC : ", sqlexception);	
			ecamsLogger.error("## MenuList.secuMenuList_html() SQLException END ##");			
			throw sqlexception;
		} catch (Exception exception) {
			exception.getStackTrace();
			ecamsLogger.error("## MenuList.secuMenuList_html() Exception START ##");				
			ecamsLogger.error("## Error DESC : ", exception);	
			ecamsLogger.error("## MenuList.secuMenuList_html() Exception END ##");				
			throw exception;
		}finally { if(connectionContext != null) {connectionContext.release();connectionContext = null;}
			if (strQuery != null) 	strQuery = null;
			if (rs != null)     try{rs.close();}catch (Exception ex){ex.getStackTrace();}
			if (pstmt != null)  try{pstmt.close();}catch (Exception ex2){ex2.getStackTrace();}
			if (conn != null){
				try{
					ConnectionResource.release(conn);
				}catch(Exception ex3){
					ecamsLogger.error("## MenuList.secuMenuList_html() connection release exception ##");
					ex3.getStackTrace();
				}
			}
		}
	}//end of SelectMenuList_html() method statement
    
    public String chkMenuSecu(HttpServletRequest request,String pathName) throws SQLException, Exception {
 		Connection        conn        = null;
 		PreparedStatement pstmt       = null;
 		ResultSet         rs          = null;
 		StringBuffer      strQuery    = new StringBuffer();
 		ConnectionContext connectionContext = new ConnectionResource();	
 		String retMsg = "NO";

 		TokenService tokenService = new TokenService();
 		String jwt = tokenService.getToken(request);                 // 요청에서 JWT 추출
 		if (jwt == null || jwt.isEmpty() || !tokenService.validateToken(jwt)) {
 			return "NO"; // 미인증
 		}
 		String user_id = tokenService.getUserId(jwt);                // 토큰에서 userId 추출
 		if (user_id == null || user_id.trim().isEmpty()) {
 			return "NO";
 		}
 		
 		try {			
 			conn = connectionContext.getConnection();

 			// 1. 메뉴 등록 여부 확인 (등록 안 된 경로는 거부)
 			strQuery.setLength(0);
 			strQuery.append("SELECT count(*) cnt            	 \n");
 			strQuery.append("  FROM cmm0080 b               	 \n");
 			strQuery.append(" WHERE b.cm_htmlfile IS NOT NULL	 \n");
 			strQuery.append("   and b.cm_htmlfile like '%.jsp'   \n");
 			strQuery.append("   AND b.cm_htmlfile = ?      	 	 \n");
 			strQuery.append("   and b.cm_befmenu<>0              \n");
 			strQuery.append("   and b.cm_befmenu<>999            \n");
 			pstmt = conn.prepareStatement(strQuery.toString());
 			pstmt.setString(1, pathName);
 			rs = pstmt.executeQuery();    

 			int menuCnt = 0;
 			if (rs.next()) {
 				menuCnt = rs.getInt("cnt");
 			}
 			rs.close();
 			pstmt.close();

 			if (menuCnt == 0) {
 				// 등록되지 않은 페이지는 접근 불가
 				return "NO";
 			}

 			// 2. 권한 확인
 			strQuery.setLength(0);
 			strQuery.append("SELECT count(*) cnt            \n");
 			strQuery.append("  FROM cmm0080 b, cmm0090 a    \n");
 			strQuery.append(" WHERE EXISTS (SELECT cm_rgtcd FROM cmm0043 \n");
 			strQuery.append("                 WHERE cm_userid=?          \n");
 			strQuery.append("                   AND cm_rgtcd=a.cm_rgtcd) \n");
 			strQuery.append("   AND a.cm_menucd = b.cm_menucd            \n");
 			strQuery.append("   AND b.cm_htmlfile = ?                    \n");

 			pstmt = conn.prepareStatement(strQuery.toString());
 			pstmt.setString(1, user_id);
 			pstmt.setString(2, pathName);

 			rs = pstmt.executeQuery();    
 			if (rs.next()) {
 				if (rs.getInt("cnt") > 0) {
 					retMsg = "OK";
 				}
 			}
 			rs.close();
 			pstmt.close();

 			conn.close();
 			rs = null;
 			pstmt = null;
 			conn = null;

 			return retMsg;			

 		} catch (SQLException sqlexception) {
 			ecamsLogger.error("## MenuList.chkMenuSecu() SQLException START ##");
 			ecamsLogger.error("## Error DESC : ", sqlexception);	
 			ecamsLogger.error("## MenuList.chkMenuSecu() SQLException END ##");			
 			throw sqlexception;
 		} catch (Exception exception) {
 			ecamsLogger.error("## MenuList.chkMenuSecu() Exception START ##");				
 			ecamsLogger.error("## Error DESC : ", exception);	
 			ecamsLogger.error("## MenuList.chkMenuSecu() Exception END ##");				
 			throw exception;
 		} finally { 
 			if(connectionContext != null) {
 				connectionContext.release();
 				connectionContext = null;
 			}
 			if (strQuery != null) 	strQuery = null;
 			if (rs != null)     try{rs.close();}catch (Exception ex){}
 			if (pstmt != null)  try{pstmt.close();}catch (Exception ex2){}
 			if (conn != null){
 				try{
 					ConnectionResource.release(conn);
 				}catch(Exception ex3){
 					ecamsLogger.error("## MenuList.chkMenuSecu() connection release exception ##");
 				}
 			}
 		}
 	}
    
    /* 취약점수정 (2025) start*/
    public String chkMenuSecu_20251014(String user_id,String pathName) throws SQLException, Exception {
    	Connection        conn        = null;
		PreparedStatement pstmt       = null;
		ResultSet         rs          = null;
		StringBuffer      strQuery    = new StringBuffer();
		ConnectionContext connectionContext = new ConnectionResource();	
		String retMsg = "NO";
				
		try {			
			conn = connectionContext.getConnection();
			strQuery.setLength(0);
			strQuery.append("SELECT	count(*) cnt			       \n");
			strQuery.append("  FROM cmm0080 b   	               \n");
			strQuery.append(" where b.cm_htmlfile is not null  \n");
			strQuery.append("   and b.cm_htmlfile like '%.jsp' \n");
			strQuery.append("   and b.cm_htmlfile=?            \n");
			strQuery.append("   and b.cm_befmenu<>0                \n");
			strQuery.append("   and b.cm_befmenu<>999              \n");			
            pstmt = conn.prepareStatement(strQuery.toString());
            //pstmt = new LoggableStatement(conn, strQuery.toString());
            //pstmt.setString(1, user_id);
            pstmt.setString(1, pathName);
            //ecamsLogger.error(((LoggableStatement)pstmt).getQueryString());
            rs = pstmt.executeQuery();    
			if (rs.next()){
				if (rs.getInt("cnt")==0) retMsg = "OK";	
			}//end of while-loop statement			
			rs.close();
			pstmt.close();
			
			if ("NO".equals(retMsg)) {
				strQuery.setLength(0);
				strQuery.append("SELECT	count(*) cnt			\n");
				strQuery.append("  FROM cmm0080 b,cmm0090 a  	\n");
				strQuery.append(" where exists (select cm_rgtcd from cmm0043   	\n");
				strQuery.append("			     where cm_userid=?             	\n");
				strQuery.append("		   		   and cm_rgtcd=a.cm_rgtcd)    	\n");
				strQuery.append("   and a.cm_menucd=b.cm_menucd \n");
				strQuery.append("   and b.cm_htmlfile=?     \n");			
	            pstmt = conn.prepareStatement(strQuery.toString());
	            //pstmt = new LoggableStatement(conn, strQuery.toString());
	            pstmt.setString(1, user_id);
	            pstmt.setString(2, pathName);
	            //ecamsLogger.error(((LoggableStatement)pstmt).getQueryString());
	            rs = pstmt.executeQuery();    
				if (rs.next()){
					if (rs.getInt("cnt")>0)	retMsg = "OK";	
				}//end of while-loop statement				
				rs.close();
				pstmt.close();
			}
			conn.close();

        	rs = null;
            pstmt = null;
            conn = null;

			return retMsg;			
		} catch (SQLException sqlexception) {
			sqlexception.getStackTrace();
			ecamsLogger.error("## MenuList.chkMenuSecu() SQLException START ##");
			ecamsLogger.error("## Error DESC : ", sqlexception);	
			ecamsLogger.error("## MenuList.chkMenuSecu() SQLException END ##");			
			throw sqlexception;
		} catch (Exception exception) {
			exception.getStackTrace();
			ecamsLogger.error("## MenuList.chkMenuSecu() Exception START ##");				
			ecamsLogger.error("## Error DESC : ", exception);	
			ecamsLogger.error("## MenuList.chkMenuSecu() Exception END ##");				
			throw exception;
		}finally { if(connectionContext != null) {connectionContext.release();connectionContext = null;}
			if (strQuery != null) 	strQuery = null;
			if (rs != null)     try{rs.close();}catch (Exception ex){ex.getStackTrace();}
			if (pstmt != null)  try{pstmt.close();}catch (Exception ex2){ex2.getStackTrace();}
			if (conn != null){
				try{
					ConnectionResource.release(conn);
				}catch(Exception ex3){
					ecamsLogger.error("## MenuList.chkMenuSecu() connection release exception ##");
					ex3.getStackTrace();
				}
			}
		}
	}//end of chkMenuSecu() method statement
    /* 취약점수정 (2025) end*/    
}//end of MenuList class statement