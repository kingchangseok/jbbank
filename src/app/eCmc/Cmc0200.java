/*****************************************************************************************
	1. program ID	: Cmc0100.java
	2. create date	: 2010.11.15
	3. auth		    : no Name
	4. update date	: 
	5. auth		    : 
	6. description	: 요구관리 ISR 등록
*****************************************************************************************/

package app.eCmc;

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
public class Cmc0200{

    /**
     * Logger Class Instance Creation
     * logger
     */
    Logger ecamsLogger  = EcamsLogger.getLoggerInstance();
    
	/**	
	 * 요구관리 ISRSUBID 조회
	 * @param UserId
	 * @return List
	 * @throws SQLException
	 * @throws Exception
	 */	
	public String setISRSUBInfo(HashMap<String,String> etcData) throws SQLException, Exception {
		Connection        conn        = null;
		PreparedStatement pstmt       = null;
		ResultSet         rs          = null;
		StringBuffer      strQuery    = new StringBuffer();
		String            retMsg      = "0";
		
		ConnectionContext connectionContext = new ConnectionResource();		
		
		try {
			
			conn = connectionContext.getConnection();
			conn.setAutoCommit(false);
			
			String strISRID = etcData.get("isrid");
			if (strISRID == "" && strISRID == null){
				return "ISRID 정보가 없습니다. 관리자에게 문의해주십시오.";
			}
			int pstmtcount = 1;
			String strSubNO = "";
			int Y = 0 ;
			
        	strQuery.setLength(0);
    		strQuery.append("update cmc0100 set CC_STATUS='1' \n");
    		strQuery.append(" where CC_ISRID=? \n");

	        pstmt = conn.prepareStatement(strQuery.toString());
	  //      pstmt = new LoggableStatement(conn,strQuery.toString());
			pstmtcount = 1;
	    	pstmt.setString(pstmtcount++, etcData.get("isrid"));
	  //  	ecamsLogger.error(((LoggableStatement)pstmt).getQueryString());
			pstmt.executeUpdate();
			pstmt.close();
			
			if (!etcData.get("CC_RECVGBN").equals("9")) {
				String[] strPart = etcData.get("addpart").split(",");
				for (Y=0 ; strPart.length>Y ; Y++){            	
	    			//ISR SUB 파트 존재여부 확인
	            	strQuery.setLength(0);
	        		strQuery.append("select CC_ISRSUB,CC_SUBSTATUS \n");
	        		strQuery.append("  from cmc0110                \n");
	        		strQuery.append(" where CC_ISRID=? and CC_RECVPART=? \n");
	        		pstmt = conn.prepareStatement(strQuery.toString());
	        //		pstmt = new LoggableStatement(conn,strQuery.toString());
	        		pstmtcount = 1;
	        		pstmt.setString(pstmtcount++, strISRID);
	        		pstmt.setString(pstmtcount++, strPart[Y]);
	        //		ecamsLogger.error(((LoggableStatement)pstmt).getQueryString());
	    	        rs = pstmt.executeQuery();
	    	        strSubNO = "";
	    	        if (rs.next()) {
	    	        	strSubNO = rs.getString("CC_ISRSUB");
	    	        	if (!rs.getString("CC_SUBSTATUS").equals("13")) {
	    	        		strSubNO = "이미 수신파트로 추가되어 있는 파트입니다.";
	    	        	}
	    	        }
	    	        rs.close();
	    	        pstmt.close();
	    	        
	    	        if (strSubNO == ""){
	    				//이관 또는 수신파트 추가에 의한 신규등록일때
	    	        	strQuery.setLength(0);
	            		strQuery.append("select lpad(max(CC_ISRSUB)+1,2,'0') as subMaxNO  \n");
	            		strQuery.append("  from cmc0110      \n");
	            		strQuery.append(" where cc_isrid = ? \n");
	            		pstmt = conn.prepareStatement(strQuery.toString());
	         //   		pstmt = new LoggableStatement(conn,strQuery.toString());
	            		pstmt.setString(1, strISRID);
	        //    		ecamsLogger.error(((LoggableStatement)pstmt).getQueryString());
	        	        rs = pstmt.executeQuery();
	        	        if (rs.next()) {
	        	        	if (rs.getString("subMaxNO") != null && rs.getString("subMaxNO") != "") {
	        	        		strSubNO = rs.getString("subMaxNO");
	        	        	}
	        	        }
	        	        rs.close();
	        	        pstmt.close();
	        	        
		            	strQuery.setLength(0);
		            	strQuery.append("insert into cmc0110 (CC_ISRID,CC_ISRSUB,CC_RECVPART, \n");
		            	strQuery.append("      CC_RECVGBN,CC_MAINSTATUS,CC_SUBSTATUS,         \n");
		            	strQuery.append("      CC_CREATDT,CC_LASTDT,CC_OUTPART)               \n");
		        		strQuery.append("(select cc_isrid,?,?,'0','01','11',                  \n");
		        		strQuery.append("        SYSDATE,SYSDATE,cc_recvpart                  \n");
		        		strQuery.append("    from cmc0110                                     \n");
		        		strQuery.append("   where cc_isrid=? and cc_isrsub=?)                 \n");
		        		pstmt = conn.prepareStatement(strQuery.toString());
		    //    		pstmt = new LoggableStatement(conn,strQuery.toString());
		        		pstmtcount = 1;
		        		pstmt.setString(pstmtcount++, strSubNO);
		        		pstmt.setString(pstmtcount++, strPart[Y]);
		        		pstmt.setString(pstmtcount++, etcData.get("isrid"));
		        		pstmt.setString(pstmtcount++, etcData.get("isrsub"));
		     //   		ecamsLogger.error(((LoggableStatement)pstmt).getQueryString());
		        		pstmt.executeUpdate();
		        		pstmt.close();
	    	        } else if (strSubNO.length() == 2) {
	    	        	strQuery.setLength(0);
		            	strQuery.append("update cmc0110 set                            \n");
		            	strQuery.append("      (CC_RECVGBN,CC_MAINSTATUS,CC_SUBSTATUS, \n");
		            	strQuery.append("      CC_LASTDT,CC_OUTPART) =                 \n");
		        		strQuery.append("(select '0','01','11',SYSDATE,cc_recvpart     \n");
		        		strQuery.append("    from cmc0110                              \n");
		        		strQuery.append("   where cc_isrid=? and cc_isrsub=?)          \n");
		        		strQuery.append(" where cc_isrid=? and cc_isrsub=?             \n");
		        		pstmt = conn.prepareStatement(strQuery.toString());
		     //   		pstmt = new LoggableStatement(conn,strQuery.toString());
		        		pstmtcount = 1;
		        		pstmt.setString(pstmtcount++, etcData.get("isrid"));
		        		pstmt.setString(pstmtcount++, etcData.get("isrsub"));
		        		pstmt.setString(pstmtcount++, etcData.get("isrid"));
		        		pstmt.setString(pstmtcount++, strSubNO);
		      //  		ecamsLogger.error(((LoggableStatement)pstmt).getQueryString());
		        		pstmt.executeUpdate();
		        		pstmt.close();
	    	        }
	            }
				
				if (etcData.get("CC_RECVGBN").equals("3")) {  //이관
					strQuery.setLength(0);
	        		strQuery.append("update cmc0110 set         \n");
	        		strQuery.append("       CC_SUBSTATUS='13',  \n");
	        		strQuery.append("       CC_RECVGBN=?,       \n");
	        		strQuery.append("       CC_RECVUSER=?,      \n");
	        		strQuery.append("       CC_RECVDATE=SYSDATE \n");
	        		strQuery.append(" where CC_ISRID=?          \n");
	        		strQuery.append("   and CC_ISRSUB=?         \n");
		        
			        pstmt = conn.prepareStatement(strQuery.toString());
			//        pstmt = new LoggableStatement(conn,strQuery.toString());
					pstmtcount = 1;
					pstmt.setString(pstmtcount++, etcData.get("CC_RECVGBN"));
		        	pstmt.setString(pstmtcount++, etcData.get("recvuser"));
		        	pstmt.setString(pstmtcount++, etcData.get("isrid"));
		        	pstmt.setString(pstmtcount++, etcData.get("isrsub"));
		    //    	ecamsLogger.error(((LoggableStatement)pstmt).getQueryString());
		    		pstmt.executeUpdate();
		    		pstmt.close();
				}
			} else {
				strQuery.setLength(0);
        		strQuery.append("update cmc0110 set    \n");
        		strQuery.append("       CC_RECVUSER=?, \n");
        		strQuery.append("       CC_RECVGBN=?,  \n");
        		strQuery.append("       CC_SUBSTATUS=decode(CC_SUBSTATUS,'11','12',CC_SUBSTATUS), \n");
        		strQuery.append("       CC_CATETYPE=?, \n");
        		strQuery.append("       CC_DETCATE=?,  \n");
        		strQuery.append("       CC_JOBRANK=?,  \n");
        		strQuery.append("       CC_HANDLER=?,  \n");
        		strQuery.append("       CC_RECVDATE=decode(CC_SUBSTATUS,'11',SYSDATE,CC_RECVDATE) \n");
        		strQuery.append(" where CC_ISRID=?     \n");
        		strQuery.append("   and CC_ISRSUB=?    \n");
	        
		        pstmt = conn.prepareStatement(strQuery.toString());
		 //       pstmt = new LoggableStatement(conn,strQuery.toString());
				pstmtcount = 1;
	        	pstmt.setString(pstmtcount++, etcData.get("recvuser"));
	        	pstmt.setString(pstmtcount++, etcData.get("CC_RECVGBN"));
	        	pstmt.setString(pstmtcount++, etcData.get("CC_CATETYPE"));
	        	pstmt.setString(pstmtcount++, etcData.get("CC_DETCATE"));
	        	pstmt.setString(pstmtcount++, etcData.get("CC_JOBRANK"));
	        	pstmt.setString(pstmtcount++, etcData.get("handler"));
	        	pstmt.setString(pstmtcount++, etcData.get("isrid"));
	        	pstmt.setString(pstmtcount++, etcData.get("isrsub"));
	       // 	ecamsLogger.error(((LoggableStatement)pstmt).getQueryString());
	    		pstmt.executeUpdate();
	    		pstmt.close();
			}
            
		    
	        conn.commit();
	        conn.close();
	        
	        rs = null;
			pstmt = null;
			conn = null;
	        
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
					ecamsLogger.error("## Cmc0100.setISRInfo() connection release exception ##");
					ex3.printStackTrace();
				}				
			}			
			ecamsLogger.error("## Cmc0100.setISRInfo() SQLException START ##");
			ecamsLogger.error("## Error DESC : ", sqlexception);	
			ecamsLogger.error("## Cmc0100.setISRInfo() SQLException END ##");			
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
					ecamsLogger.error("## Cmc0100.setISRInfo() connection release exception ##");
					ex3.printStackTrace();
				}				
			}			
			ecamsLogger.error("## Cmc0100.setISRInfo() Exception START ##");				
			ecamsLogger.error("## Error DESC : ", exception);	
			ecamsLogger.error("## Cmc0100.setISRInfo() Exception END ##");				
			throw exception;
		}finally{
			if (strQuery != null) 	strQuery = null;
			if (rs != null)     try{rs.close();}catch (Exception ex){ex.printStackTrace();}
			if (pstmt != null)  try{pstmt.close();}catch (Exception ex2){ex2.printStackTrace();}
			if (conn != null){
				try{
					ConnectionResource.release(conn);
					conn = null;
				}catch(Exception ex3){
					ecamsLogger.error("## Cmc0100.setISRInfo() connection release exception ##");
					ex3.printStackTrace();
				}
			}
		}
	}//end of setISRInfo() method statement

	public Object[] getIsrInfo(String IsrId,String IsrSub,String UserId,String ReqCd) throws SQLException, Exception {
		Connection        conn        = null;
		PreparedStatement pstmt       = null;
		ResultSet         rs          = null;
		PreparedStatement pstmt2      = null;
		ResultSet         rs2         = null;
		StringBuffer      strQuery    = new StringBuffer();
		ArrayList<HashMap<String, String>> rtList = new ArrayList<HashMap<String, String>>();
		HashMap<String, String>			   rst	  = null;
		
		ConnectionContext connectionContext = new ConnectionResource();
		
		try {
			
			conn = connectionContext.getConnection();
			
			strQuery.setLength(0);
			strQuery.append("select a.cc_substatus,a.cc_recvpart,a.cc_recvuser,  \n");
			strQuery.append("       a.cc_catetype,a.cc_detcate,a.cc_jobrank,     \n");
			strQuery.append("       a.cc_recvgbn,a.cc_handler,                   \n");
			strQuery.append("       to_char(a.cc_recvdate,'yyyy/mm/dd') recvdate,\n");
			strQuery.append("       nvl(a.cc_outpart,a.cc_recvpart) cc_outpart,  \n");
			strQuery.append("       b.cm_username,c.cm_codename,d.cm_deptname,   \n");
			strQuery.append("       a.cc_recvgbn,a.cc_handler,e.cm_username handler  \n");
			strQuery.append("  from cmm0040 e,cmm0100 d,cmm0020 c,cmm0040 b,cmc0110 a \n");			
			strQuery.append(" where a.cc_isrid=? and a.cc_isrsub=?               \n");
			strQuery.append("   and a.cc_recvuser=b.cm_userid(+)                 \n");
			strQuery.append("   and a.cc_handler=e.cm_userid(+)                  \n");
			strQuery.append("   and c.cm_macode='ISRSTASUB'                      \n");
			strQuery.append("   and a.cc_substatus=c.cm_micode                   \n");
			strQuery.append("   and nvl(a.cc_outpart,a.cc_recvpart)=d.cm_deptcd  \n");
	        pstmt = conn.prepareStatement(strQuery.toString());
	//        pstmt =  new LoggableStatement(conn, strQuery.toString());
	        
	        pstmt.setString(1, IsrId);
	        pstmt.setString(2, IsrSub);
   //         ecamsLogger.error(((LoggableStatement)pstmt).getQueryString());
            rs = pstmt.executeQuery();
            
			if (rs.next()){
				rst = new HashMap<String, String>();
				rst.put("cc_recvpart", rs.getString("cc_recvpart"));  
				rst.put("cc_substatus", rs.getString("cc_substatus"));         //상세진행상황 : cmc0110
				rst.put("cc_recvuser", rs.getString("cc_recvuser"));  
				rst.put("cc_catetype", rs.getString("cc_catetype"));  
				rst.put("cc_detcate", rs.getString("cc_detcate"));  
				rst.put("cc_jobrank", rs.getString("cc_jobrank"));  
				rst.put("recvdate", rs.getString("recvdate"));   
				rst.put("cm_username", rs.getString("cm_username"));  
				rst.put("sta", rs.getString("cm_codename"));  
				rst.put("cc_handler", rs.getString("cc_handler"));  
				rst.put("handler", rs.getString("handler"));  
			    rst.put("secusw", "N");
				rst.put("usersw", "N");
				rst.put("recvgbn", rs.getString("cc_recvgbn"));
			    if (rs.getString("cc_substatus").equals("11")) rst.put("sta", "ISR접수대기");
			    if (!rs.getString("cc_outpart").equals(rs.getString("cc_recvpart"))) {
			    	rst.put("sta", "[이관]" + rs.getString("cm_deptname"));
			    } 
				if (rs.getString("cc_substatus").equals("11") && ReqCd.equals("32")) {
					strQuery.setLength(0);
					strQuery.append("select SYSISR_SECUCHK(?,?,?,'32') rst from dual \n");
					pstmt2 = conn.prepareStatement(strQuery.toString());
					pstmt2.setString(1, UserId);
					pstmt2.setString(2, IsrId);
			        pstmt2.setString(3, IsrSub);
					rs2 = pstmt2.executeQuery();
					if (rs2.next()) {
						if (rs2.getString("rst").equals("OK")) {
							rst.put("secusw", "Y");
							rst.put("usersw", "Y");
						}
					}
					rs2.close();
					pstmt2.close();
				} else if (ReqCd.equals("32") && rs.getString("cc_recvuser") != null) {
					if (rs.getString("cc_recvuser").equals(UserId)) {
						//'11','12','14','15','16','21','2','23'	
						if (rs.getString("cc_substatus").equals("12") ||  //ISR접수완료
							rs.getString("cc_substatus").equals("14") ||  //RFC요청승인대기
							rs.getString("cc_substatus").equals("15") ||  //RFC발행완료
							rs.getString("cc_substatus").equals("16") ||  //RFC발행취소
							rs.getString("cc_substatus").equals("21") ||  //RFC접수
							rs.getString("cc_substatus").equals("23")) {  //단위테스트중
							rst.put("secusw", "Y");
							if (rs.getString("cc_substatus").equals("12") ||  //ISR접수완료
								rs.getString("cc_substatus").equals("16")) {  //RFC발행취소
								rst.put("usersw", "Y");
							}
						}
					}
				}
				rtList.add(rst);
				rst = null;
			}
			rs.close();
			pstmt.close();
			conn.close();
			
			rs = null;
			pstmt = null;
			conn = null;
			
			return rtList.toArray();
			
			
		} catch (SQLException sqlexception) {
			sqlexception.printStackTrace();
			ecamsLogger.error("## PrjInfo.getPrjInfoDetail() SQLException START ##");
			ecamsLogger.error("## Error DESC : ", sqlexception);	
			ecamsLogger.error("## PrjInfo.getPrjInfoDetail() SQLException END ##");			
			throw sqlexception;
		} catch (Exception exception) {
			exception.printStackTrace();
			ecamsLogger.error("## PrjInfo.getPrjInfoDetail() Exception START ##");				
			ecamsLogger.error("## Error DESC : ", exception);	
			ecamsLogger.error("## PrjInfo.getPrjInfoDetail() Exception END ##");				
			throw exception;
		}finally{
			if (strQuery != null) 	strQuery = null;
			if (rtList != null)		rtList = null;
			if (rs != null)     try{rs.close();}catch (Exception ex){ex.printStackTrace();}
			if (pstmt != null)  try{pstmt.close();}catch (Exception ex2){ex2.printStackTrace();}
			if (conn != null){
				try{
					ConnectionResource.release(conn);
				}catch(Exception ex3){
					ecamsLogger.error("## PrjInfo.getPrjInfoDetail() connection release exception ##");
					ex3.printStackTrace();
				}
			}
		}
	}//end of getPrjInfoDetail() method statement
	 public Object[] getHandlerList(String UserId,String ReqCd) throws SQLException, Exception {	
			Connection        conn        = null;
			PreparedStatement pstmt       = null;
			ResultSet         rs          = null;
			StringBuffer      strQuery    = new StringBuffer();
			ArrayList<HashMap<String, String>>  rtList = new ArrayList<HashMap<String, String>>();
			HashMap<String, String>			  rst		  = null;
			Object[]		  rtObj		  = null; 
			
			ConnectionContext connectionContext = new ConnectionResource();
			
			try {
				
				conn = connectionContext.getConnection();
				
				strQuery.setLength(0);
				strQuery.append("select b.cm_userid,b.cm_username,    \n");
				strQuery.append("       c.cm_deptcd,c.cm_deptname     \n");
				strQuery.append("  from cmm0043 d,cmm0040 a,cmm0040 b,cmm0100 c \n");
				strQuery.append(" where a.cm_userid=?                 \n");
				strQuery.append("   and a.cm_project=b.cm_project     \n");
				strQuery.append("   and a.cm_project=c.cm_deptcd      \n");
				strQuery.append("   and b.cm_active='1'               \n");
				strQuery.append("   and b.cm_userid=d.cm_userid       \n");
				if (ReqCd.equals("32")) {
					strQuery.append("and d.cm_rgtcd='R2'              \n");
				} else {
					strQuery.append("and d.cm_rgtcd='C2'              \n");
				}
				strQuery.append("order by b.cm_username               \n");
			    pstmt = conn.prepareStatement(strQuery.toString());
		//	    pstmt = new LoggableStatement(conn,strQuery.toString());
			    pstmt.setString(1, UserId);
		//	    ecamsLogger.error(((LoggableStatement)pstmt).getQueryString());
				rs = pstmt.executeQuery();
				
				rst = new HashMap<String,String>();
				rst.put("userid","선택하세요");
				rst.put("cm_userid","0000");
				rst.put("cm_username","선택하세요");
				rtList.add(rst);
				
				while (rs.next()) {
					rst = new HashMap<String,String>();
					rst.put("userid","[" + rs.getString("cm_userid") + "] " + rs.getString("cm_username"));
					rst.put("cm_userid",rs.getString("cm_userid"));
					rst.put("cm_username",rs.getString("cm_username"));
					rst.put("cm_deptcd",rs.getString("cm_deptcd"));
					rst.put("cm_deptname",rs.getString("cm_deptname"));
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
				rtList.clear();
				rtList = null;
				
				return rtObj;			
			    
			} catch (SQLException sqlexception) {
				sqlexception.printStackTrace();
				ecamsLogger.error("## PrjInfo.getHandlerList() SQLException START ##");
				ecamsLogger.error("## Error DESC : ", sqlexception);	
				ecamsLogger.error("## PrjInfo.getHandlerList() SQLException END ##");			
				throw sqlexception;
			} catch (Exception exception) {
				exception.printStackTrace();
				ecamsLogger.error("## PrjInfo.getHandlerList() Exception START ##");				
				ecamsLogger.error("## Error DESC : ", exception);	
				ecamsLogger.error("## PrjInfo.getHandlerList() Exception END ##");				
				throw exception;
			}finally{
				if (rtList != null) 	rtList = null;
				if (rtObj != null) 	rtObj = null;
				if (strQuery != null) 	strQuery = null;
				if (rtList != null)		rtList = null;
				if (rs != null)     try{rs.close();}catch (Exception ex){ex.printStackTrace();}
				if (pstmt != null)  try{pstmt.close();}catch (Exception ex2){ex2.printStackTrace();}
				if (conn != null){
					try{
						ConnectionResource.release(conn);
					}catch(Exception ex3){
						ecamsLogger.error("## PrjInfo.getHandlerList() connection release exception ##");
						ex3.printStackTrace();
					}
				}
			}
	    }//end of getHandlerList() method statement
	   
}//end of Cmc0100 class statement
