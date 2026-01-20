
/*****************************************************************************************
	1. program ID	: eCmr9910.java
	2. create date	: 
	3. auth		    : 
	4. update date	: 11.03.21
	5. auth		    : No Name
	6. description	: eCmr9910
******************************************************************************************/

package app.eCmr;

import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

import org.apache.logging.log4j.Logger;

import app.common.LoggableStatement;
import app.common.SystemPath;

import com.ecams.common.dbconn.ConnectionContext;
import com.ecams.common.dbconn.ConnectionResource;
import com.ecams.common.logger.EcamsLogger;




/**
 * @author bigeyes
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */


public class Cmr9910{
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
    
    public String insert_Cmr9910(String cboUser , String strUserId, String CR_TITLE, String CR_CONTENT) throws SQLException, Exception {
		Connection        	conn        = null;
		PreparedStatement 	pstmt       = null;
		ResultSet         	rs          = null;
		StringBuffer      strQuery    = new StringBuffer();
		int				  Cnt         = 0;
		int 			  SeqNo       = 0; 
		
		ConnectionContext connectionContext = new ConnectionResource();
		try {
			//ecamsLogger.error("ABCDEFG12 : "+AcptNo+",  "+UserID+", "+ Title+", "+Txt_Body+", "+NotiYN);
			conn = connectionContext.getConnection();			

					
					strQuery.setLength(0);
					strQuery.append("INSERT INTO CMR9910(CR_CONUSR,CR_SENDDATE,CR_STATUS,CR_SENDUSR,CR_SGNMSG,CR_ACPTNO,CR_TITLE) \n");
					strQuery.append("values ( 			\n");
					strQuery.append("?, 	        	\n");//수신자
					strQuery.append("SYSDATE,	        \n");//발신날짜
					strQuery.append("'0',				\n");//상태
					strQuery.append("?, 	        	\n");//발신자
					strQuery.append("?, 		        \n");//발신내용
					strQuery.append("'999999999999',	\n");//신청번호
					strQuery.append("? ) 	        	\n");//알람제목
					
				
					pstmt = conn.prepareStatement(strQuery.toString());
					pstmt = new LoggableStatement(conn,strQuery.toString());    	
	     
					pstmt.setString(++Cnt,cboUser);//수신자
	            	pstmt.setString(++Cnt,strUserId);//발신자
	            	pstmt.setString(++Cnt,CR_TITLE); //알림제목  
	            	pstmt.setString(++Cnt,CR_CONTENT); //발신내용

	                
	            	ecamsLogger.error(((LoggableStatement)pstmt).getQueryString());
	            	pstmt.executeUpdate();
	            	pstmt.close();               
          
             
	            	conn.commit();
	            	conn.close();
            
	            	rs = null ;
	            	pstmt = null;
	            	conn = null;
		
            
           return "ok";
            
		} catch (SQLException sqlexception) {
			sqlexception.printStackTrace();
			ecamsLogger.error("## Cmr9910.insert_Cmr9910() SQLException START ##");
			ecamsLogger.error("## Error DESC : ", sqlexception);
			ecamsLogger.error("## Cmr9910.insert_Cmr9910() SQLException END ##");			
			throw sqlexception;
		} catch (Exception exception) {
			exception.printStackTrace();
			ecamsLogger.error("## Cmr9910.insert_Cmr9910() Exception START ##");				
			ecamsLogger.error("## Error DESC : ", exception);	
			ecamsLogger.error("## insert_Cmr9910() Exception END ##");				
			throw exception;
		}finally{
			if (strQuery != null) 	strQuery = null;
			if (rs != null)     try{rs.close();}catch (Exception ex){ex.printStackTrace();}
			if (pstmt != null)  try{pstmt.close();}catch (Exception ex2){ex2.printStackTrace();}
			if (conn != null){
				try{
					ConnectionResource.release(conn);
				}catch(Exception ex3){
					ecamsLogger.error("## Cmr9910.insert_Cmr9910() connection release exception ##");
					ex3.printStackTrace();
				}
			}
		}

    }
  
    public String select_name(String strUserId) throws SQLException, Exception 					{
    	Connection			conn		= null;
    	PreparedStatement	pstmt		= null;
		ResultSet         	rs          = null;  	
		StringBuffer      	strQuery    = new StringBuffer();
		//ArrayList<HashMap<String, String>>  rsval = new ArrayList<HashMap<String, String>>();
		String			    ret         = "";
    	
		ConnectionContext connectionContext = new ConnectionResource();
		
		
		try{
			conn = connectionContext.getConnection();
			
			strQuery.setLength(0);
			strQuery.append("select cm_username ");
			strQuery.append("from cmm0040 ");
			strQuery.append("where CM_USERID= ? ");
			pstmt = conn.prepareStatement(strQuery.toString());
			pstmt.setString(1, strUserId);
			rs = pstmt.executeQuery();
			while (rs.next()) {
			rs.getString("cm_username");
			ret = rs.getString("cm_username");
			}
				
			rs.close();
	        pstmt.close();
	        conn.close();
	            
	        strQuery = null;
	        rs = null;
	        pstmt = null;
	        conn = null;	
			return ret;
			
		} catch (SQLException sqlexception){
			sqlexception.printStackTrace();
			ecamsLogger.error("## Cmr9910.select_name() SQLException START ##");
			ecamsLogger.error("## Error DESC : ", sqlexception);
			ecamsLogger.error("## Cmr9910.select_name() SQLException END ##");
			throw sqlexception;
		}//catch
		 catch (Exception exception) {
			exception.printStackTrace();
			ecamsLogger.error("## Cmr9910.select_name() Exception START ##");
			ecamsLogger.error("## Error DESC : ", exception);
			ecamsLogger.error("## Cmr9910.select_name() Exception END ##");
			throw exception;
		}//catch
		finally{
			if (strQuery != null) 	strQuery = null;
	//		if (rsval != null)  	rsval = null;
			if (rs != null)     try{rs.close();}catch (Exception ex){ex.printStackTrace();}
			if (pstmt != null)  try{pstmt.close();}catch (Exception ex2){ex2.printStackTrace();}
			if (conn != null){
				try{
					ConnectionResource.release(conn);
				}catch(Exception ex3){
					ecamsLogger.error("## Cmr9910.select_name() connection release exception ##");
					ex3.printStackTrace();
				}
			}
		}  
    } 
    public Object[] get_select_grid(String UserId) throws SQLException, Exception 					{
    	Connection			conn		= null;
    	PreparedStatement	pstmt		= null;
		ResultSet         	rs          = null;  	
		StringBuffer      	strQuery    = new StringBuffer();
		HashMap<String, String>			  	rst	 	= null;
		ArrayList<HashMap<String, String>>  rtList	= new ArrayList<HashMap<String, String>>();
		Object[]		 rtObj		  = null;	
	    Integer          Cnt          = 0;
    	
		ConnectionContext connectionContext = new ConnectionResource();
		
		try{
			conn = connectionContext.getConnection();
			
			strQuery.setLength(0);
			strQuery.append("select CR_SENDUSR, CR_TITLE, CR_SENDDATE ");
			strQuery.append("from cmr9910 ");
			strQuery.append("where CR_CONUSR = ? ");
			strQuery.append("where CR_SENDUSR = ? ");
			
			pstmt = conn.prepareStatement(strQuery.toString());
			pstmt.setString(1, UserId);
			pstmt.setString(2, UserId);
			
			rs = pstmt.executeQuery();
			rtList.clear();
			
			while (rs.next()) {
			rst = new HashMap<String, String>();
			rst.put("rows", Integer.toString(rs.getRow()));    		// NO
			rst.put("CR_TITLE", rs.getString("CR_TITLE")); 			// 제목
			rst.put("CR_CONUSR", rs.getString("CR_CONUSR")); 		// 수신자
			rst.put("CR_SENDUSR", rs.getString("CR_SENDUSR")); 		// 발신자
			rst.put("CR_SENDDATE", rs.getString("CR_SENDDATE"));	// 발신날짜 
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
			ecamsLogger.error("## Cmr9910.get_select_grid() SQLException START ##");
			ecamsLogger.error("## Error DESC : ", sqlexception);
			ecamsLogger.error("## Cmr9910.get_select_grid() SQLException END ##");
			throw sqlexception;
		}//catch
		 catch (Exception exception) {
			exception.printStackTrace();
			ecamsLogger.error("## Cmr9910.get_select_grid() Exception START ##");
			ecamsLogger.error("## Error DESC : ", exception);
			ecamsLogger.error("## Cmr9910.get_select_grid() Exception END ##");
			throw exception;
		}//catch
		finally{
			if (strQuery != null) 	strQuery = null;
	//		if (rsval != null)  	rsval = null;
			if (rs != null)     try{rs.close();}catch (Exception ex){ex.printStackTrace();}
			if (pstmt != null)  try{pstmt.close();}catch (Exception ex2){ex2.printStackTrace();}
			if (conn != null){
				try{
					ConnectionResource.release(conn);
				}catch(Exception ex3){
					ecamsLogger.error("## Cmr9910.get_select_grid() connection release exception ##");
					ex3.printStackTrace();
				}
			}
		}  
    } 
     
 }//end of Cmr9910 class statement
