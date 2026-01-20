
/*****************************************************************************************
	1. program ID	: eCmr9999.java
	2. create date	: 
	3. auth		    : 
	4. update date	: 11.03.21
	5. auth		    : No Name
	6. description	: eCmr9999
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


public class Cmr9999{
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
    
    public String insert_Cmr9910(String CR_TITLE, String CR_CONTENT,String strUserId,ArrayList<HashMap<String,String>> CM_USERID ) throws SQLException, Exception {
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
					
					for (int i=0;CM_USERID.size()>i;i++){
						System.out.println("");
						strQuery.setLength(0);
						strQuery.append("INSERT INTO CMR9910(CR_TITLE,CR_SGNMSG,CR_SENDUSR,CR_CONUSR,CR_SENDDATE,CR_STATUS,CR_ACPTNO) \n");
						strQuery.append("values ( 			\n");
						strQuery.append("?, 	        	\n");//알림제목
						strQuery.append("?, 	        	\n");//발신내용
						strQuery.append("?, 	        	\n");//발신자					
						strQuery.append("?, 	        	\n");//수신자
						strQuery.append("SYSDATE,	        \n");//발신날짜
						strQuery.append("'0',				\n");//상태
						strQuery.append("'999999999999')	\n");//신청번호
				
						//pstmt = new LoggableStatement(conn,strQuery.toString());
						pstmt = conn.prepareStatement(strQuery.toString());
		            	pstmt.setString(1,CR_TITLE); //알림제목 
		            	pstmt.setString(2,CR_CONTENT); //발신내용
		            	pstmt.setString(3,strUserId);//발신자
		            	pstmt.setString(4,CM_USERID.get(i).get("CM_USERID"));//수신자
		                
		            	//ecamsLogger.error(((LoggableStatement)pstmt).getQueryString());
		            	pstmt.executeUpdate();
		            	pstmt.close();               
          
					}
	            	conn.commit();
	            	conn.close();
            
	            	rs = null ;
	            	pstmt = null;
	            	conn = null;
		
            
           return "okok";
            
		} catch (SQLException sqlexception) {
			sqlexception.printStackTrace();
			ecamsLogger.error("## Cmr9999.insert_Cmr9910() SQLException START ##");
			ecamsLogger.error("## Error DESC : ", sqlexception);
			ecamsLogger.error("## Cmr9999.insert_Cmr9910() SQLException END ##");			
			throw sqlexception;
		} catch (Exception exception) {
			exception.printStackTrace();
			ecamsLogger.error("## Cmr9999.insert_Cmr9910() Exception START ##");				
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
					ecamsLogger.error("## Cmr9999.insert_Cmr9910() connection release exception ##");
					ex3.printStackTrace();
				}
			}
		}

    }
  
    }//end of Cmr9910 class statement
