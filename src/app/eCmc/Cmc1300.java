/*****************************************************************************************
	1. program ID	: Cmc1300.java
	2. create date	: 2009.08.17
	3. auth		    : i.s.choi
	4. update date	: 2009.08.17
	5. auth		    : no name
	6. description	: Request List
*****************************************************************************************/

package app.eCmc;

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

import app.common.LoggableStatement;


/**
 * @author bigeyes
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */


public class Cmc1300{
    /**
     * Logger Class Instance Creation
     * logger
     */
    Logger ecamsLogger  = EcamsLogger.getLoggerInstance();
    
	/**
	 * ÀÇ·Ú°Ç¿¡ ´ëÇÑ Á¢¼ö´ë»ó °Ç Á¶È¸.
	 */
    //getReqList(tmpSta,tmpTeam,strStD,strEdD,tmpUser);
    public Object[] getReqList(String pSta,String pTeam,String pStD,String pEdD,String pUser1,String pUser2) throws SQLException, Exception {
		Connection        conn        = null;
		PreparedStatement pstmt       = null;
		ResultSet         rs          = null;
		StringBuffer      strQuery    = new StringBuffer();
		
		Object[] 		  returnObject= null;
		ArrayList<HashMap<String, String>>  rtList	= new ArrayList<HashMap<String, String>>();
		HashMap<String, String>			  	rst	 	= null;	
		boolean           findSw      = false;
		ConnectionContext connectionContext = new ConnectionResource();
		
		try {
			
			rtList.clear();
	        
			conn = connectionContext.getConnection();
			
			strQuery.setLength(0);		
			strQuery.append("SELECT a.cc_reqno,a.cc_status,a.cc_acptuser,                       \n");
			strQuery.append("       replace(a.cc_title,chr(13),' ') cc_title,                   \n");
			strQuery.append("       to_char(a.cc_reqdate,'yyyy/mm/dd hh24:mi') as reqdate,      \n");
			strQuery.append("       substr(a.cc_reqno,1,4) || '-' || substr(a.cc_reqno,5,2) ||  \n");
			strQuery.append("       '-' || substr(a.cc_reqno,7,6) as reqno,                     \n");
			strQuery.append("       substr(a.cc_reqend,1,4) || '-' || substr(a.cc_reqend,5,2) ||\n");
			strQuery.append("       '-' || substr(a.cc_reqend,7,2) as reqend,                   \n");
			strQuery.append("       to_char(a.cc_acptdate,'yyyy/mm/dd hh24:mi') as acptdate,    \n");
			strQuery.append("       b.cm_username,c.cm_deptname,d.cm_codename,e.cm_codename sta,\n");
			strQuery.append("       f.cm_username acptuser                                      \n");
			strQuery.append("  FROM cmm0040 f,CMM0020 E,CMM0020 D,CMM0100 C,CMM0040 B,CMc0020 A \n"); 
			strQuery.append(" where a.cc_reqday>=? and a.cc_reqday<=?                           \n");
			if (pSta != null && pSta != "") {	
				strQuery.append("and a.cc_status=?                                              \n");
			} 
			if (pTeam != null && pTeam != "") {
				strQuery.append("and a.cc_teamcd=?                                              \n");
			}
			if (pUser1 != null && pUser1 != "") {
				strQuery.append("and b.cm_username=?                                            \n");
			}
			if (pUser2 != null && pUser2 != "") {
				strQuery.append("and f.cm_username=?                                            \n");
			}
	
			strQuery.append("and nvl(a.cc_acptuser,a.cc_editor) = f.cm_userid					\n");
			strQuery.append("and a.cc_editor=b.cm_userid										\n");
			strQuery.append("and a.cc_teamcd=c.cm_deptcd                                        \n");
			strQuery.append("and d.cm_macode='REQTYPE' and d.cm_micode=a.cc_reqtype            	\n");
			strQuery.append("and e.cm_macode='REQSTA' and e.cm_micode=a.cc_status               \n");
			strQuery.append("order by a.cc_reqdate desc                                         \n");		
            
			pstmt = conn.prepareStatement(strQuery.toString());
			//pstmt = new LoggableStatement(conn,strQuery.toString());
            int Cnt = 0;
            pstmt.setString(++Cnt, pStD);
            pstmt.setString(++Cnt, pEdD);
            if (pSta != null && pSta != "") {
            	pstmt.setString(++Cnt, pSta);
            } 
            if (pTeam != null && pTeam != "") {
            	pstmt.setString(++Cnt, pTeam);
            } 
            if (pUser1 != null && pUser1 != "") {
            	pstmt.setString(++Cnt, pUser1);
            }  
            if (pUser2 != null && pUser2 != "") {
            	pstmt.setString(++Cnt, pUser2);
            } 
			//ecamsLogger.debug(((LoggableStatement)pstmt).getQueryString());
            rs = pstmt.executeQuery();
            
			while (rs.next()){	
				if (pUser2 != null && pUser2 != "") {
					findSw = false;
					if (rs.getString("cc_acptuser") != null) findSw = true;
				} else findSw = true;
				
				if (findSw == true) {
		            rst = new HashMap<String, String>();
		            rst.put("rows",    Integer.toString(rs.getRow()));  //NO 
	        		rst.put("reqno",   rs.getString("reqno"));          //ÀÇ·Ú¹øÈ£
	        		rst.put("reqdate",   rs.getString("reqdate"));        //ÀÇ·ÚÀÏ½Ã
	        		rst.put("cm_deptname",  rs.getString("cm_deptname"));    //ÀÇ·ÚÆÀ
	        		rst.put("cm_username",rs.getString("cm_username"));    //ÀÇ·ÚÀÎ
	        		rst.put("cm_codename",  rs.getString("cm_codename"));    //ÀÇ·ÚÀ¯Çü
	        		rst.put("reqend",  rs.getString("reqend"));         //¿Ï·áÈñ¸ÁÀÏ½Ã
	        		rst.put("cc_title",   rs.getString("cc_title"));       //ÀÇ·ÚÁ¦¸ñ
	        		rst.put("sta",    rs.getString("sta"));	        //»óÅÂ
	        		rst.put("cc_reqno",  rs.getString("cc_reqno"));     //Qrycd
	        		rst.put("cc_status",  rs.getString("cc_status"));   //Editor
	        		rst.put("acptdate",   rs.getString("acptdate"));        //ÀÇ·ÚÀÏ½Ã
	        		if (rs.getString("cc_acptuser") != null)
	        			rst.put("acptuser", rs.getString("acptuser"));
	        		rst.put("reqdate",   rs.getString("reqdate"));        //ÀÇ·ÚÀÏ½Ã
	        		rtList.add(rst);
	        		rst = null;
				}
			}//end of while-loop statement
			
			rs.close();
			pstmt.close();
			conn.close();

			rs = null;
			pstmt = null;
			conn = null;
			
			returnObject = rtList.toArray();
			rtList.clear();
			rtList = null;
			
			return returnObject;			
			
		} catch (SQLException sqlexception) {
			sqlexception.printStackTrace();
			ecamsLogger.error("## Cmc1300.getReqList() SQLException START ##");
			ecamsLogger.error("## Error DESC : ", sqlexception);	
			ecamsLogger.error("## Cmc1300.getReqList() SQLException END ##");			
			throw sqlexception;
		} catch (Exception exception) {
			exception.printStackTrace();
			ecamsLogger.error("## Cmc1300.getReqList() Exception START ##");				
			ecamsLogger.error("## Error DESC : ", exception);	
			ecamsLogger.error("## Cmc1300.getReqList() Exception END ##");				
			throw exception;
		}finally{
			if (strQuery != null) 	strQuery = null;
			if (returnObject != null)	returnObject = null;
			if (rs != null)     try{rs.close();}catch (Exception ex){ex.printStackTrace();}
			if (pstmt != null)  try{pstmt.close();}catch (Exception ex2){ex2.printStackTrace();}
			if (conn != null){
				try{
					conn.close();
				}catch(Exception ex3){
					ecamsLogger.error("## Cmc1300.getReqList() connection release exception ##");
					ex3.printStackTrace();
				}
			}
		}
	}//end of getReqList() method statement  
    
}//end of Cmc0300 class statement
