/*****************************************************************************************
	1. program ID	: Cmr0010.java
	2. create date	: 2010.11.15
	3. auth		    : no Name
	4. update date	: 
	5. auth		    : 
	6. description	: 변경관리 RFC 접수
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
public class Cmr0010{

    /**
     * Logger Class Instance Creation
     * logger
     */
    Logger ecamsLogger  = EcamsLogger.getLoggerInstance();
    
	/**
	 * 변경관리 RFC 접수 정보 셋팅
	 * @param HashMap<String,String> etcData
	 * @return String
	 * @throws SQLException
	 * @throws Exception
	 */

    public Object[] getRFCInfo(String IsrId, String SubId,String UserId) throws SQLException, Exception{
		Connection        conn        = null;
		PreparedStatement pstmt       = null;
		ResultSet         rs          = null;
		StringBuffer      strQuery    = new StringBuffer();
		ArrayList<HashMap<String, String>>  rsval  = new ArrayList<HashMap<String, String>>();
		HashMap<String, String>			  rst		  = null;
		ConnectionContext connectionContext = new ConnectionResource();	
		int				  pstmtcount  = 1;
		boolean           findSw      = false;
		
		try {
			conn = connectionContext.getConnection();
			rst = new HashMap<String, String>();
			strQuery.setLength(0);
			strQuery.append("select a.cc_rfcrecvcd,a.cc_chgtype,a.cc_recvmsg,         \n");
			strQuery.append(" 		to_char(a.cc_rfcrecvdt,'yyyy/mm/dd') recvdt, 	  \n");
			strQuery.append("		a.cc_chguser,b.cm_username,a.cc_substatus         \n");
			strQuery.append("  from cmc0110 a, cmm0040 b 				              \n");
			strQuery.append(" where a.CC_ISRID=? and a.CC_ISRSUB=?					  \n");
			strQuery.append("   and a.cc_chguser=b.cm_userid(+)						  \n");
			pstmt = conn.prepareStatement(strQuery.toString());	
			
	//		pstmt = new LoggableStatement(conn,strQuery.toString());
			
			pstmtcount = 1;
			pstmt.setString(pstmtcount++,IsrId);
			pstmt.setString(pstmtcount++,SubId);	
	 //       //ecamsLogger.error(((LoggableStatement)pstmt).getQueryString());           
	        rs = pstmt.executeQuery();
	        rsval.clear();
			if (rs.next()){	
				if (rs.getString("cc_chguser") == null && !rs.getString("cc_substatus").equals("15")) findSw = true;
				if (rs.getString("cc_chguser") != null && !rs.getString("cc_substatus").equals("15")) {
					if (rs.getString("cc_rfcrecvcd").equals("Y")) rst.put("recvgbn", "접수");
					else rst.put("recvgbn", "반려");
					rst.put("cc_rfcrecvcd",rs.getString("cc_rfcrecvcd"));
					rst.put("cc_chgtype",rs.getString("cc_chgtype"));
					rst.put("cc_recvmsg",rs.getString("cc_recvmsg"));
					rst.put("recvdt",rs.getString("recvdt"));
					rst.put("cc_chguser",rs.getString("cc_chguser"));
					rst.put("cm_username",rs.getString("cm_username"));
					rst.put("secusw", "N");
					if (rs.getString("cc_substatus").equals("22") || rs.getString("cc_substatus").equals("23") || rs.getString("cc_substatus").equals("24")) {
						if (rs.getString("cc_chguser").equals(UserId)) rst.put("secusw", "Y");
					}
					findSw = true;
				} else if (rs.getString("cc_substatus").equals("15")) {
					findSw = false;
				}
			}//end of while-loop statement
			rs.close();
			pstmt.close();
			
			if (findSw == false) {
				strQuery.setLength(0);			
				strQuery.append("select SYSCHG_SECUCHK(?,?,?,'41') rst from dual   \n");
				pstmt = conn.prepareStatement(strQuery.toString());	
				
				//pstmt = new LoggableStatement(conn,strQuery.toString());
				
				pstmtcount = 1;
				pstmt.setString(pstmtcount++,UserId);
				pstmt.setString(pstmtcount++,IsrId);
				pstmt.setString(pstmtcount++,SubId);	
		        //ecamsLogger.error(((LoggableStatement)pstmt).getQueryString()); /
		        rs = pstmt.executeQuery();
		        rsval.clear();
				if (rs.next()){	
					rst.put("cc_chguser",UserId);
					if (rs.getString("rst").equals("OK")) rst.put("secusw", "Y");
					else rst.put("secusw", "N");
					
				}//end of while-loop statement
				rs.close();
				pstmt.close();
			}
			rsval.add(rst);
			rst = null;
			conn.close();
			conn = null;
			pstmt = null;
			rs = null;
			
			return rsval.toArray();			
			
		} catch (SQLException sqlexception) {
			sqlexception.printStackTrace();
			ecamsLogger.error("## Cmr0010.getRFCInfo() SQLException START ##");
			ecamsLogger.error("## Error DESC : ", sqlexception);	
			ecamsLogger.error("## Cmr0010.getRFCInfo() SQLException END ##");			
			throw sqlexception;
		} catch (Exception exception) {
			exception.printStackTrace();
			ecamsLogger.error("## Cmr0010.getRFCInfo() Exception START ##");				
			ecamsLogger.error("## Error DESC : ", exception);	
			ecamsLogger.error("## Cmr0010.getRFCInfo() Exception END ##");				
			throw exception;
		}finally{
			if (strQuery != null) 	strQuery = null;
			if (rs != null)     try{rs.close();}catch (Exception ex){ex.printStackTrace();}
			if (pstmt != null)  try{pstmt.close();}catch (Exception ex2){ex2.printStackTrace();}
			if (conn != null){
				try{
					ConnectionResource.release(conn);
				}catch(Exception ex3){
					ecamsLogger.error("## Cmr0010.getRFCInfo() connection release exception ##");
					ex3.printStackTrace();
				}
			}
		}
	}//end of getUnitTest() method statement
    public Object[] getChgUser(String IsrId, String SubId) throws SQLException, Exception{
		Connection        conn        = null;
		PreparedStatement pstmt       = null;
		ResultSet         rs          = null;
		StringBuffer      strQuery    = new StringBuffer();
		ArrayList<HashMap<String, String>>  rsval  = new ArrayList<HashMap<String, String>>();
		HashMap<String, String>			  rst		  = null;
		ConnectionContext connectionContext = new ConnectionResource();	
		int				  pstmtcount  = 1;
		
		try {
			conn = connectionContext.getConnection();
			
			strQuery.setLength(0);
			strQuery.append("select b.cm_userid,b.cm_username,        \n");
			strQuery.append("       c.cm_deptname,c.cm_deptcd,        \n");
			strQuery.append("       PRJDEV_USER(a.cc_isrid,a.cc_isrsub,a.cc_scmuser) devcnt,  \n");
			strQuery.append("       PRJPROG_USER(a.cc_isrid,a.cc_isrsub,a.cc_scmuser) pgmcnt  \n");
			strQuery.append("  from cmc0210 a,cmm0040 b,cmm0100 c     \n");
			strQuery.append(" where a.CC_ISRID=? and a.CC_ISRSUB=?    \n");
			strQuery.append("   and a.cc_scmuser=b.cm_userid	      \n");
			strQuery.append("   and nvl(a.cc_status,'0')<>'C'	      \n");
			strQuery.append("   and b.cm_project=c.cm_deptcd	      \n");
			pstmt = conn.prepareStatement(strQuery.toString());	
			
		//	pstmt = new LoggableStatement(conn,strQuery.toString());
			
			pstmtcount = 1;
			pstmt.setString(pstmtcount++,IsrId);
			pstmt.setString(pstmtcount++,SubId);	
	    //   //ecamsLogger.error(((LoggableStatement)pstmt).getQueryString());           
	        rs = pstmt.executeQuery();
	        rsval.clear();
			while (rs.next()){		
				rst = new HashMap<String, String>();
				rst.put("cm_username",rs.getString("cm_username"));
				rst.put("cm_userid",rs.getString("cm_userid"));
				rst.put("cm_deptcd",rs.getString("cm_deptcd"));
				rst.put("cm_deptname",rs.getString("cm_deptname"));
				rst.put("pgmcnt",rs.getString("pgmcnt"));
				rst.put("devcnt",rs.getString("devcnt"));
				rsval.add(rst);
				rst = null;
			}//end of while-loop statement
			rs.close();
			pstmt.close();
			
			conn.close();
			conn = null;
			pstmt = null;
			rs = null;
			
			return rsval.toArray();			
			
		} catch (SQLException sqlexception) {
			sqlexception.printStackTrace();
			ecamsLogger.error("## Cmr0010.getChgUser() SQLException START ##");
			ecamsLogger.error("## Error DESC : ", sqlexception);	
			ecamsLogger.error("## Cmr0010.getChgUser() SQLException END ##");			
			throw sqlexception;
		} catch (Exception exception) {
			exception.printStackTrace();
			ecamsLogger.error("## Cmr0010.getChgUser() Exception START ##");				
			ecamsLogger.error("## Error DESC : ", exception);	
			ecamsLogger.error("## Cmr0010.getChgUser() Exception END ##");				
			throw exception;
		}finally{
			if (strQuery != null) 	strQuery = null;
			if (rs != null)     try{rs.close();}catch (Exception ex){ex.printStackTrace();}
			if (pstmt != null)  try{pstmt.close();}catch (Exception ex2){ex2.printStackTrace();}
			if (conn != null){
				try{
					ConnectionResource.release(conn);
				}catch(Exception ex3){
					ecamsLogger.error("## Cmr0010.getChgUser() connection release exception ##");
					ex3.printStackTrace();
				}
			}
		}
	}//end of getChgUser() method statement
	public String setAcceptRFCInfo(HashMap<String,String> etcData) throws SQLException, Exception {
		Connection        conn        = null;
		PreparedStatement pstmt       = null;
		ResultSet         rs          = null;
		StringBuffer      strQuery    = new StringBuffer();
		String            retMsg      = "0";
		boolean           insSw       = false;
		
		ConnectionContext connectionContext = new ConnectionResource();		
		
		try {
			
			conn = connectionContext.getConnection();
			
			String strISRID = etcData.get("CC_ISRID");
			if (strISRID == "" && strISRID == null){
				return "ISRID 정보가 없습니다. 관리자에게 문의해주십시오.";
			}
			String strSubNO = etcData.get("CC_ISRSUB");
			if (strSubNO == "" && strSubNO == null){
				return "ISRSUB 정보가 없습니다. 관리자에게 문의해주십시오.";
			}
			int pstmtcount = 1;
			int Y = 0 ;

			conn.setAutoCommit(false);
			if (etcData.get("recvgbn").equals("1")) {
				strQuery.setLength(0);
	    		strQuery.append("update cmc0110 set CC_MAINSTATUS='02', CC_SUBSTATUS=?,    \n");
	    		strQuery.append("                   CC_CHGTYPE=?,CC_RECVMSG=?,CC_CHGUSER=?,\n");
	    		strQuery.append("                   CC_RFCRECVDT=SYSDATE,CC_RFCRECVCD=?    \n");
	    		strQuery.append(" where CC_ISRID=? \n");
	    		strQuery.append("   and CC_ISRSUB=? \n");

		        pstmt = conn.prepareStatement(strQuery.toString());
		 //       pstmt = new LoggableStatement(conn,strQuery.toString());
				pstmtcount = 1;

		    	pstmt.setString(pstmtcount++, etcData.get("CMC0210_STATUS"));
		    	pstmt.setString(pstmtcount++, etcData.get("CC_CHGTYPE"));
		    	pstmt.setString(pstmtcount++, etcData.get("CC_RECVMSG"));
		    	pstmt.setString(pstmtcount++, etcData.get("CC_CHGUSER"));
		    	if (etcData.get("CMC0210_STATUS").equals("21")) pstmt.setString(pstmtcount++, "Y");
		    	else pstmt.setString(pstmtcount++, "N");
		    	
		    	pstmt.setString(pstmtcount++, etcData.get("CC_ISRID"));
		    	pstmt.setString(pstmtcount++, etcData.get("CC_ISRSUB"));
		    	
		  //  	//ecamsLogger.error(((LoggableStatement)pstmt).getQueryString());
				pstmt.executeUpdate();
				pstmt.close();
			}
        	
			strQuery.setLength(0);
			strQuery.append("update cmc0210 set cc_status='C'       \n");
			strQuery.append(" where cc_isrid=? and cc_isrsub=?      \n");
			strQuery.append("   and cc_status='0'                   \n");
			pstmt = conn.prepareStatement(strQuery.toString());
	   //     pstmt = new LoggableStatement(conn,strQuery.toString());
			pstmtcount = 1;
        	pstmt.setString(pstmtcount++, strISRID);
        	pstmt.setString(pstmtcount++, strSubNO);
        //	//ecamsLogger.error(((LoggableStatement)pstmt).getQueryString());
    		pstmt.executeUpdate();
    		pstmt.close();
    		
			String SCMUSER[] = etcData.get("CC_SCMUSER").split(",");
            for (Y=0 ; Y<SCMUSER.length ; Y++){
    			//RFC 접수해서 변경담당자 INSERT
            	strQuery.setLength(0);
    			strQuery.append("select count(*) cnt from cmc0210 a       \n");
    			strQuery.append(" where a.CC_ISRID=? and a.CC_ISRSUB=?    \n");
    			strQuery.append("   and a.cc_scmuser=?         	          \n");
    			pstmt = conn.prepareStatement(strQuery.toString());
    			
    	//		pstmt = new LoggableStatement(conn,strQuery.toString());
    			
    			pstmtcount = 1;
    			pstmt.setString(pstmtcount++, strISRID);
            	pstmt.setString(pstmtcount++, strSubNO);	
            	pstmt.setString(pstmtcount++, SCMUSER[Y]);
    	 //       //ecamsLogger.error(((LoggableStatement)pstmt).getQueryString());           
    	        rs = pstmt.executeQuery();
    	        if (rs.next()) {
    	        	if (rs.getInt("cnt")==0) insSw = true;
    	        }
    	        rs.close();
    	        pstmt.close();
    	        
    	        if (insSw == true) {
	    	        strQuery.setLength(0);
	            	strQuery.append("insert into cmc0210                \n");
	            	strQuery.append("   (CC_ISRID,CC_ISRSUB,CC_SCMUSER, \n");
	        		strQuery.append("    CC_STATUS,CC_CREATDT,CC_LASTDT,\n");
	        		strQuery.append("    CC_TEAMCD)                     \n");      
	        		strQuery.append("(select ?,?,?,'0',SYSDATE,SYSDATE,cm_project \n");  
	        		strQuery.append("   from cmm0040                    \n");
	        		strQuery.append("  where cm_userid=?)               \n");
	
	    	        pstmt = conn.prepareStatement(strQuery.toString());
	    	//        pstmt = new LoggableStatement(conn,strQuery.toString());
					pstmtcount = 1;
	            	pstmt.setString(pstmtcount++, strISRID);
	            	pstmt.setString(pstmtcount++, strSubNO);
	            	pstmt.setString(pstmtcount++, SCMUSER[Y]);
	            	pstmt.setString(pstmtcount++, SCMUSER[Y]);
	          //  	//ecamsLogger.error(((LoggableStatement)pstmt).getQueryString());
		    		pstmt.executeUpdate();
		    		pstmt.close();
    	        } else {
	    	        strQuery.setLength(0);
	            	strQuery.append("update cmc0210 set                 \n");
	            	strQuery.append("   cc_status=decode(cc_status,'C','0',cc_status),\n");
	            	strQuery.append("   CC_LASTDT=SYSDATE,cc_teamcd=    \n");    
	        		strQuery.append("    (select cm_project from cmm0040\n");  
	        		strQuery.append("      where cm_userid=?)           \n"); 
	        		strQuery.append(" where cc_isrid=? and cc_isrsub=?  \n"); 
	        		strQuery.append("   and cc_scmuser=?                \n");
	
	    	        pstmt = conn.prepareStatement(strQuery.toString());
	    	//        pstmt = new LoggableStatement(conn,strQuery.toString());
					pstmtcount = 1;
	            	pstmt.setString(pstmtcount++, SCMUSER[Y]);
	            	pstmt.setString(pstmtcount++, strISRID);
	            	pstmt.setString(pstmtcount++, strSubNO);
	            	pstmt.setString(pstmtcount++, SCMUSER[Y]);
	          //  	//ecamsLogger.error(((LoggableStatement)pstmt).getQueryString());
		    		pstmt.executeUpdate();
		    		pstmt.close();
    	        }
            }
		    
	        conn.commit();
	        conn.close();
			conn = null;
			pstmt = null;
			rs = null;
	        
	        return retMsg;
	        
	        
		} catch (SQLException sqlexception) {
			sqlexception.printStackTrace();
			if (rs != null)     try{rs.close();}catch (Exception ex){ex.printStackTrace();}
			if (pstmt != null)  try{pstmt.close();}catch (Exception ex2){ex2.printStackTrace();}
			if (conn != null){
				try{
					conn.rollback();
					ConnectionResource.release(conn);
				}catch(Exception ex3){
					ecamsLogger.error("## Cmr0010.setAcceptRFCInfo() connection release exception ##");
					ex3.printStackTrace();
				}				
			}			
			ecamsLogger.error("## Cmr0010.setAcceptRFCInfo() SQLException START ##");
			ecamsLogger.error("## Error DESC : ", sqlexception);	
			ecamsLogger.error("## Cmr0010.setAcceptRFCInfo() SQLException END ##");			
			throw sqlexception;
		} catch (Exception exception) {
			exception.printStackTrace();
			if (rs != null)     try{rs.close();}catch (Exception ex){ex.printStackTrace();}
			if (pstmt != null)  try{pstmt.close();}catch (Exception ex2){ex2.printStackTrace();}
			if (conn != null){
				try{
					conn.rollback();
					ConnectionResource.release(conn);
				}catch(Exception ex3){
					ecamsLogger.error("## Cmr0010.setAcceptRFCInfo() connection release exception ##");
					ex3.printStackTrace();
				}				
			}			
			ecamsLogger.error("## Cmr0010.setAcceptRFCInfo() Exception START ##");				
			ecamsLogger.error("## Error DESC : ", exception);	
			ecamsLogger.error("## Cmr0010.setAcceptRFCInfo() Exception END ##");				
			throw exception;
		}finally{
			if (strQuery != null) 	strQuery = null;
			if (rs != null)     try{rs.close();}catch (Exception ex){ex.printStackTrace();}
			if (pstmt != null)  try{pstmt.close();}catch (Exception ex2){ex2.printStackTrace();}
			if (conn != null){
				try{
					conn.commit();
					conn.close();
					conn = null;
				}catch(Exception ex3){
					ecamsLogger.error("## Cmr0010.setAcceptRFCInfo() connection release exception ##");
					ex3.printStackTrace();
				}
			}
		}
	}//end of setAcceptRFCInfo() method statement

	   
}//end of Cmr0010 class statement

