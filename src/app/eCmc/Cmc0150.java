/*****************************************************************************************
	1. program ID	: Cmr0250.java
	2. create date	: 2008. 08. 10
	3. auth		    : is.choi
	4. update date	: 2009. 05. 20
	5. auth		    : no name
	6. description	: [체크인신청상세]
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
import app.common.UserInfo;
import app.common.SystemPath;
import app.eCmr.*;

/**
 * @author bigeyes
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class Cmc0150{


    /**
     * Logger Class Instance Creation
     * logger
     */

    Logger ecamsLogger  = EcamsLogger.getLoggerInstance();

	/**
	 * USER NAME
	 * @param user_id
	 * @return String
	 * @throws SQLException
	 * @throws Exception
	 */
	public Object[] getReqList(String UserId,String AcptNo) throws SQLException, Exception {

		Connection        conn        = null;
		PreparedStatement pstmt       = null;
		PreparedStatement pstmt2       = null;
		PreparedStatement pstmt3       = null;
		ResultSet         rs          = null;
		ResultSet         rs2          = null;
		ResultSet         rs3          = null;
		StringBuffer      strQuery    = new StringBuffer();
	    Cmr0150           cmr0150     = new Cmr0150();
		ArrayList<HashMap<String, String>>  rsval  = new ArrayList<HashMap<String, String>>();
		HashMap<String, String>			  rst		  = null;
		Object[] 		  returnObject = null;
		
		ConnectionContext connectionContext = new ConnectionResource();
		try {
			
			rsval.clear();
			Integer           Cnt         = 0;
			
			conn = connectionContext.getConnection();
			
			strQuery.setLength(0);
			strQuery.append("SELECT TO_CHAR(A.CR_ACPTDATE,'YYYY/MM/DD HH24:MI') AS ACPTDATE,	\n");
			strQuery.append("TO_CHAR(A.CR_PRCDATE,'YYYY/MM/DD HH24:MI') AS PRCDATE,				\n");
			strQuery.append("SUBSTR(A.CR_ACPTNO,1,4) || '-' || SUBSTR(A.CR_ACPTNO,5,2) ||		\n");
			strQuery.append("'-' || SUBSTR(A.CR_ACPTNO,7,6) AS ACPTNO, A.CR_ACPTNO,				\n");
			strQuery.append("A.CR_EMGCD,A.CR_PRJNO,A.CR_PRJNAME,C.CM_CODENAME,					\n");
			strQuery.append("A.CR_EDITOR,A.CR_QRYCD,A.CR_SYSCD,A.CR_STATUS,a.cr_cnclstep,		\n");
			strQuery.append("A.CR_GYULJAE,A.CR_PRCDATE,A.CR_SYSGB,a.cr_prcreq,a.cr_retryyn,		\n");
			strQuery.append("SUBSTR(B.CR_REQEND,1,4) || '/' || SUBSTR(B.CR_REQEND,5,2) ||		\n");
			strQuery.append("'/' || SUBSTR(B.CR_REQEND,7,2) AS REQEND,							\n");
			strQuery.append("B.CR_RESTITLE, B.CR_RESCD, B.CR_STATUS,							\n");
			strQuery.append("       to_char(a.cr_cncllstd,'yyyymmddhh24miss') cncldat,          \n");
			strQuery.append("D.CM_DEPTNAME,														\n");
			strQuery.append("REPLACE(B.CR_REQDETAIL,CHR(13),' ')cr_reqdetail					\n");
			strQuery.append("from CMM0100 D, CMM0020 C, cmr1600 B, cmr1000 A					\n");
			strQuery.append("where A.CR_ACPTNO=? AND A.CR_ACPTNO=B.CR_ACPTNO					\n");
			strQuery.append("and C.CM_MACODE='REQTYPE' and C.CM_MICODE=b.CR_RESCD               \n");
			strQuery.append("and A.CR_TEAMCD=D.CM_DEPTCD										\n");
			strQuery.append("order by a.cr_acptdate desc										\n");
			
			pstmt = conn.prepareStatement(strQuery.toString());
			
			Cnt = 0;
			
			pstmt.setString(++Cnt, AcptNo);
			
			rs = pstmt.executeQuery();
			while (rs.next()){
				rst = new HashMap<String, String>();
				rst.put("rows",    Integer.toString(rs.getRow()));  //NO 
	        	rst.put("ACPTNO",   rs.getString("ACPTNO"));       
	        	rst.put("RESCD",   rs.getString("CR_RESCD"));//변경유형
	        	rst.put("REQEND",   rs.getString("REQEND"));
	        	rst.put("EDITOR",   rs.getString("CR_EDITOR"));
	        	rst.put("RESTITLE",   rs.getString("CR_RESTITLE"));
	        	rst.put("REQDETAIL",   rs.getString("CR_REQDETAIL"));
	        	rst.put("ACPTDATE",   rs.getString("ACPTDATE"));
	        	rst.put("CODENAME",   rs.getString("CM_CODENAME"));
	        	rst.put("DEPTNAME",   rs.getString("CM_DEPTNAME"));
	        	rst.put("cr_qrycd",   rs.getString("CR_QRYCD"));
	        	rst.put("cr_acptno",   rs.getString("CR_ACPTNO"));
	        	rst.put("cr_status",   rs.getString("CR_status"));
	        	if (rs.getString("prcdate") != null){
					rst.put("prcdate",rs.getString("prcdate"));
				}
				else if (rs.getString("cr_gyuljae").equals("1")){
					rst.put("prcdate","우선적용 중");
				}
	        	
	        	strQuery.setLength(0);
				strQuery.append("select count(*) cnt from cmr9900                   \n");
				strQuery.append(" where cr_acptno=? and cr_locat<>'00'              \n");
				strQuery.append("   and cr_teamcd not in('1','2')                   \n");
				strQuery.append("   and (cr_team=? or cr_confusr=?)                 \n");
				pstmt2 = conn.prepareStatement(strQuery.toString());
				pstmt2.setString(1, AcptNo);
				pstmt2.setString(2, UserId);
				pstmt2.setString(3, UserId);
				rs2 = pstmt2.executeQuery();
				if (rs2.next()) {
					if (rs2.getInt("cnt")>0) rst.put("confsw", "1");
					else rst.put("confsw", "0");
				}
				rs2.close();
				pstmt2.close();

				if (rs.getString("cr_status").equals("9")) {
					
					strQuery.setLength(0);
					strQuery.append("select cc_status from cmc0020			            \n");
					strQuery.append(" where cc_reqno=?	 					            \n");
					pstmt3 = conn.prepareStatement(strQuery.toString());
					pstmt3.setString(1, AcptNo);
					rs3 = pstmt3.executeQuery();
					if (rs3.next()) {
						if (rs3.getString("cc_status").equals("0")) {
							rst.put("confname","등록");
						}else if(rs3.getString("cc_status").equals("1")){
							rst.put("confname","접수");
						}else if(rs3.getString("cc_status").equals("2")){
							rst.put("confname","담당자지정");
						}else if(rs3.getString("cc_status").equals("3")){
							rst.put("confname","반려");
						}else if(rs3.getString("cc_status").equals("4")){
							rst.put("confname","개발진행");
						}else if(rs3.getString("cc_status").equals("9")){
							rst.put("confname","완료");
						}
					}
					rst.put("prcsw", "1");
					rst.put("endsw", "1");
					rst.put("updtsw1", "0");
					rst.put("updtsw2", "0");
					rst.put("updtsw3", "0");
					rst.put("log", "1");
					
					rs2.close();
					pstmt2.close();
				} else if (rs.getString("cr_status").equals("3")) {
					rst.put("confname","반려");
					rst.put("prcsw", "1");
					rst.put("endsw", "1");
					rst.put("updtsw1", "0");
					rst.put("updtsw2", "0");
					rst.put("updtsw3", "0");
					rst.put("log", "1");
				} else {
					String strPrcSw = "0";
					if (rs.getString("prcdate") != null) {
						rst.put("prcsw", "1");
						strPrcSw = "1";
					} else {
						rst.put("prcsw", "0");
						strPrcSw = "0";
					}
					rst.put("endsw", "0");

					//ecamsLogger.debug("++++++++++RetryYn++++++++"+rs.getString("cncldat"));
					ArrayList<HashMap<String, String>>  rsconf = new ArrayList<HashMap<String, String>>();

					rsconf = cmr0150.confLocat_conn(AcptNo,strPrcSw,rs.getString("cr_cnclstep"),rs.getString("cncldat"),rs.getString("cr_retryyn"),conn);

					rst.put("signteam", rsconf.get(0).get("signteam"));
					rst.put("signteamcd", rsconf.get(0).get("signteamcd"));
					rst.put("confusr", rsconf.get(0).get("confusr"));
					rst.put("cr_prcsw", rsconf.get(0).get("prcsw"));
					rst.put("confname", rsconf.get(0).get("confname"));
					rst.put("updtsw1", rsconf.get(0).get("updtsw1"));
					rst.put("updtsw2", rsconf.get(0).get("updtsw2"));
					rst.put("updtsw3", rsconf.get(0).get("updtsw3"));
					rst.put("errtry", rsconf.get(0).get("errtry"));
					rst.put("log", rsconf.get(0).get("log"));
					rst.put("sttry", rsconf.get(0).get("sttry"));
					rst.put("ermsg", rsconf.get(0).get("ermsg"));

					rsconf.clear();
					rsconf = null;
				}
				
				rst.put("file", "0");
				rst.put("testf", "0");
				strQuery.setLength(0);
				strQuery.append("select cr_gubun,count(*) as cnt from cmr1001      \n");
				strQuery.append(" where cr_acptno=?                                \n");
				strQuery.append(" group by cr_gubun                                \n");
		        pstmt2 = conn.prepareStatement(strQuery.toString());
				//pstmt2 = new LoggableStatement(conn,strQuery.toString());
				pstmt2.setString(1, AcptNo);
		        //ecamsLogger.error(((LoggableStatement)pstmt2).getQueryString());
		        rs2 = pstmt2.executeQuery();
				while (rs2.next()){
					if (rs2.getString("cr_gubun").equals("1") && rs2.getInt("cnt")>0) rst.put("file", "1");
					if (rs2.getString("cr_gubun").equals("2") && rs2.getInt("cnt")>0) rst.put("testf", "1");
				}
				rs2.close();
				pstmt2.close();

				strQuery.setLength(0);
				strQuery.append("select count(*) as cnt from cmr1030               \n");
				strQuery.append(" where cr_acptno=?                                \n");
		        pstmt2 = conn.prepareStatement(strQuery.toString());
				//pstmt2 = new LoggableStatement(conn,strQuery.toString());
				pstmt2.setString(1, AcptNo);
		        //ecamsLogger.error(((LoggableStatement)pstmt2).getQueryString());
		        rs2 = pstmt2.executeQuery();
				if (rs2.next()){
					if (rs2.getInt("cnt") > 0){
						rst.put("befjob","1");
					}
					else{
						rst.put("befjob","0");
					}
				}
				rs2.close();
				pstmt2.close();

				strQuery.setLength(0);
				strQuery.append("select count(*) as cnt from cmr9900               \n");
				strQuery.append(" where cr_acptno=? and cr_team='SYSANL'           \n");
		        pstmt2 = conn.prepareStatement(strQuery.toString());	
				//pstmt = new LoggableStatement(conn,strQuery.toString());
				pstmt2.setString(1, AcptNo);
		        //ecamsLogger.error(((LoggableStatement)pstmt).getQueryString());           
		        rs2 = pstmt2.executeQuery();
				if (rs2.next()){
					if (rs2.getInt("cnt") > 0) rst.put("analsw","Y");	
				}
				rs2.close();
				pstmt2.close();
				      	
	        	
	        	rsval.add(rst);
        		rst = null;
			}
			
			rs.close();
			pstmt.close();
			conn.close();

			rs = null;
			pstmt = null;
			conn = null;
			
			returnObject = rsval.toArray();
			rsval.clear();
			rsval = null;
			
			return returnObject;			

		} catch (SQLException sqlexception) {
			sqlexception.printStackTrace();
			ecamsLogger.error("## Cmc0150.getReqList() SQLException START ##");
			ecamsLogger.error("## Error DESC : ", sqlexception);
			ecamsLogger.error("## Cmc0150.getReqList() SQLException END ##");
			throw sqlexception;
		} catch (Exception exception) {
			exception.printStackTrace();
			ecamsLogger.error("## Cmc0150.getReqList() Exception START ##");
			ecamsLogger.error("## Error DESC : ", exception);
			ecamsLogger.error("## Cmc0150.getReqList() Exception END ##");
			throw exception;
		}finally{
			if (strQuery != null) strQuery = null;
			if (returnObject != null)	returnObject = null;
			if (rs != null)     try{rs.close();}catch (Exception ex){ex.printStackTrace();}
			if (pstmt != null)  try{pstmt.close();}catch (Exception ex2){ex2.printStackTrace();}
			if (conn != null){
				try{
					ConnectionResource.release(conn);
				}catch(Exception ex3){
					ecamsLogger.error("## Cmc0150.getReqList() connection release exception ##");
					ex3.printStackTrace();
				}
			}
		}
	}//end of getReqList() method statement
	
	 public String nextConf(String AcptNo,String UserId,String conMsg,String Cd,String ReqCd) throws SQLException, Exception {
			Connection        conn        = null;
			PreparedStatement pstmt       = null;
			StringBuffer      strQuery    = new StringBuffer();
			ConnectionContext connectionContext = new ConnectionResource();
			try {
				
				conn = connectionContext.getConnection();
				
	        	strQuery.setLength(0);
	        	strQuery.append("Begin CMR9900_STR ( ");
	        	strQuery.append("?, ?, ?, '9', ?, ? ); End;");
	        	pstmt = conn.prepareStatement(strQuery.toString());
	        	pstmt.setString(1, AcptNo);
	        	pstmt.setString(2,UserId);
	        	pstmt.setString(3,conMsg);
	        	pstmt.setString(4,ReqCd);
	        	pstmt.setString(5,Cd);
	        	
	        	pstmt.executeUpdate();
	        	pstmt.close();
	        	
	        	
	        	strQuery.setLength(0);
				strQuery.append("update cmr1600 set                                     \n");
				strQuery.append("CR_STATUS=?	\n"); 
				strQuery.append("where cr_acptno=? 										\n");
		        pstmt = conn.prepareStatement(strQuery.toString());
		        
		        pstmt.setString(1, "9" );
		        pstmt.setString(2, AcptNo);
	        	
	        	pstmt.executeUpdate();            	
	        	pstmt.close();
	        	
	        	
	        	
	        	
	        	conn.close();
	        	pstmt = null;
	        	conn = null;
	        	
	        	
	        	
	    		return "0";        	
				
			} catch (SQLException sqlexception) {
				sqlexception.printStackTrace();
				ecamsLogger.error("## Cmc0150.nextConf() SQLException START ##");
				ecamsLogger.error("## Error DESC : ", sqlexception);	
				ecamsLogger.error("## Cmc0150.nextConf() SQLException END ##");			
				throw sqlexception;
			} catch (Exception exception) {
				exception.printStackTrace();
				ecamsLogger.error("## Cmc0150.nextConf() Exception START ##");				
				ecamsLogger.error("## Error DESC : ", exception);	
				ecamsLogger.error("## Cmc0150.nextConf() Exception END ##");				
				throw exception;
			}finally{
				if (strQuery != null) 	strQuery = null;
				if (pstmt != null)  try{pstmt.close();}catch (Exception ex2){ex2.printStackTrace();}
				if (conn != null){
					try{
						conn.close();
					}catch(Exception ex3){
						ecamsLogger.error("## Cmc0150.nextConf() connection release exception ##");
						ex3.printStackTrace();
					}
				}
			}
		}//end of nextConf() method statement
	 
	 public Object[] getAcptList(String ReqNo) throws SQLException, Exception {
			Connection        conn        = null;
			PreparedStatement pstmt       = null;
			ResultSet         rs          = null;
			StringBuffer      strQuery    = new StringBuffer();
			
			Object[] 		  returnObject= null;
			ArrayList<HashMap<String, String>>  rtList	= new ArrayList<HashMap<String, String>>();
			HashMap<String, String>			  	rst	 	= null;	
			ConnectionContext connectionContext = new ConnectionResource();
			
			try {
				
		        rtList.clear();
		        rst = new HashMap<String, String>();
				conn = connectionContext.getConnection();
				
				strQuery.setLength(0);		
				strQuery.append("SELECT replace(a.cc_reqbody,chr(13),' ') reqbody, \n");
				strQuery.append("       to_char(a.cc_acptdate,'yyyy/mm/dd hh24:mi') acptdate,\n");	
				strQuery.append("       replace(a.cc_acptmsg,chr(13),' ') acptmsg, \n");
				strQuery.append("       b.cm_username,a.cc_acptproc,a.cc_acptuser, \n");
				strQuery.append("       a.cc_status, a.cc_reqno					   \n");
				strQuery.append("  from cmm0040 b,cmc0020 a                        \n");	
	            strQuery.append(" where a.cc_reqno=?                               \n");
	            strQuery.append("   and nvl(a.cc_acptuser,a.cc_editor)=b.cm_userid \n");	
	            
				pstmt = conn.prepareStatement(strQuery.toString());
				//pstmt = new LoggableStatement(conn,strQuery.toString());
				pstmt.setString(1, ReqNo);
				//ecamsLogger.debug(((LoggableStatement)pstmt).getQueryString());
	            rs = pstmt.executeQuery();
	            
				if (rs.next()){	
					rst.put("reqsayu",rs.getString("reqbody"));
					rst.put("acptdate", rs.getString("acptdate"));
					rst.put("acptmsg", rs.getString("acptmsg"));
					if (rs.getString("cc_acptuser") != null)
						rst.put("cm_username", rs.getString("cm_username"));
					rst.put("cc_acptproc", rs.getString("cc_acptproc"));
					rst.put("cc_status", rs.getString("cc_status"));
					
				}//end of while-loop statement
				
				rs.close();
				pstmt.close();
				
				strQuery.setLength(0);		
				strQuery.append("SELECT count(*) cnt from cmr1001              \n");
				strQuery.append(" where cr_acptno=?                            \n");	
	            
				pstmt = conn.prepareStatement(strQuery.toString());
				//pstmt = new LoggableStatement(conn,strQuery.toString());
				pstmt.setString(1, ReqNo);
				//ecamsLogger.debug(((LoggableStatement)pstmt).getQueryString());
	            rs = pstmt.executeQuery();
	            
				if (rs.next()){	
					if (rs.getInt("cnt")>0) rst.put("filesw", "1");
					else rst.put("filesw", "0");
				}//end of while-loop statement
				
				rs.close();
				pstmt.close();
				
				rtList.add(rst);
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
				ecamsLogger.error("## Cmc0150.getAcptList() SQLException START ##");
				ecamsLogger.error("## Error DESC : ", sqlexception);	
				ecamsLogger.error("## Cmc0150.getAcptList() SQLException END ##");			
				throw sqlexception;
			} catch (Exception exception) {
				exception.printStackTrace();
				ecamsLogger.error("## Cmc0150.getAcptList() Exception START ##");				
				ecamsLogger.error("## Error DESC : ", exception);	
				ecamsLogger.error("## Cmc0150.getAcptList() Exception END ##");				
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
						ecamsLogger.error("## Cmc0150.getAcptList() connection release exception ##");
						ex3.printStackTrace();
					}
				}
			}
		}//end of getAcptList() method statement    

	    public Object[] getAcptUser(String ReqNo) throws SQLException, Exception {
			Connection        conn        = null;
			PreparedStatement pstmt       = null;
			ResultSet         rs          = null;
			StringBuffer      strQuery    = new StringBuffer();
			
			Object[] 		  returnObject= null;
			ArrayList<HashMap<String, String>>  rtList	= new ArrayList<HashMap<String, String>>();
			HashMap<String, String>			  	rst	 	= null;	
			ConnectionContext connectionContext = new ConnectionResource();
			
			try {
		        rtList.clear();
		        
				conn = connectionContext.getConnection();
				
				strQuery.setLength(0);		
				strQuery.append("SELECT b.cm_userid,b.cm_username               \n");
				strQuery.append("  from cmm0040 b,cmc0022 a                     \n");	
	            strQuery.append(" where a.cc_reqno=?                            \n");
	            strQuery.append("   and a.cc_userid=b.cm_userid                 \n");	
	            
				pstmt = conn.prepareStatement(strQuery.toString());
				//pstmt = new LoggableStatement(conn,strQuery.toString());
				pstmt.setString(1, ReqNo);
				//ecamsLogger.debug(((LoggableStatement)pstmt).getQueryString());
	            rs = pstmt.executeQuery();
	            
				while (rs.next()){	
					rst = new HashMap<String, String>();
					rst.put("cm_username",rs.getString("cm_username"));
					rst.put("cm_userid", rs.getString("cm_userid"));
					rtList.add(rst);
					
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
				ecamsLogger.error("## Cmc0150.getUserList() SQLException START ##");
				ecamsLogger.error("## Error DESC : ", sqlexception);	
				ecamsLogger.error("## Cmc0150.getUserList() SQLException END ##");			
				throw sqlexception;
			} catch (Exception exception) {
				exception.printStackTrace();
				ecamsLogger.error("## Cmc0150.getUserList() Exception START ##");				
				ecamsLogger.error("## Error DESC : ", exception);	
				ecamsLogger.error("## Cmc0150.getUserList() Exception END ##");				
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
						ecamsLogger.error("## Cmc0150.getUserList() connection release exception ##");
						ex3.printStackTrace();
					}
				}
			}
		}//end of getUserList() method statement 

}//end of Cmr0250 class statement
