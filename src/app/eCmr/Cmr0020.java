/*****************************************************************************************
	1. program ID	: Cmr0020.java
	2. create date	: 2006.08. 08
	3. auth		    : is.choi
	4. update date	: 
	5. auth		    : 
	6. description	: 1. USER INFO.
*****************************************************************************************/

package app.eCmr;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
//import java.io.BufferedReader;
//import java.io.File;
//import java.io.FileInputStream;
//import java.io.FileOutputStream;
//import java.io.FileReader;
//import java.io.InputStreamReader;

import app.common.LoggableStatement;

//import java.io.OutputStreamWriter;

import org.apache.logging.log4j.Logger;
//import org.apache.poi.hssf.record.formula.functions.Sin;


import com.ecams.common.dbconn.ConnectionContext;
import com.ecams.common.dbconn.ConnectionResource;
import com.ecams.common.logger.EcamsLogger;
import java.util.ArrayList;
import java.util.HashMap;
import app.common.*;

import app.thread.*;

/**
 * @author bigeyes
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class Cmr0020{    
	

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

    public Object[] getSelectList(String IsrId, String SubId, String UserId,String ReqCd) throws SQLException, Exception{
		Connection        conn        = null;
		PreparedStatement pstmt       = null;
		ResultSet         rs          = null;
		PreparedStatement pstmt2      = null;
		ResultSet         rs2         = null;
		StringBuffer      strQuery    = new StringBuffer();
		ArrayList<HashMap<String, String>>  rsval  = new ArrayList<HashMap<String, String>>();
		HashMap<String, String>			  rst		  = null;
		ConnectionContext connectionContext = new ConnectionResource();	
		int				  pstmtcount  = 1;
		String            strEdGbn    = "";
		Object[] returnObjectArray		 = null;
		
		try {
			
			conn = connectionContext.getConnection();
			if (ReqCd.equals("49")) {
				strQuery.setLength(0);
				strQuery.append("select nvl(cc_edgbncd,'0') edgbn   \n");
				strQuery.append("  from cmc0210                     \n");
				strQuery.append(" where cc_isrid=? and cc_isrsub=?  \n");
				strQuery.append("   and nvl(cc_status,'0')='1'      \n");
				pstmt = conn.prepareStatement(strQuery.toString());
				pstmt.setString(pstmtcount++,IsrId);
				pstmt.setString(pstmtcount++,SubId);
				rs = pstmt.executeQuery();
				if (rs.next()) {
					strEdGbn = rs.getString("edgbn");
				}
				rs.close();
				pstmt.close();
			}
			strQuery.setLength(0);
			strQuery.append("select a.CC_SCMUSER,        			                \n");
			if (ReqCd.equals("42")) strQuery.append("nvl(c.cnt,0) cnt, 			                \n");
			strQuery.append("		to_char(a.CC_CREATDT,'yyyy/mm/dd') CREATDT,					\n");
			strQuery.append("		to_char(a.CC_LASTDT,'yyyy/mm/dd') LASTDT,					\n");
			strQuery.append("		a.CC_DEVSTDAY DEVSTDAY, a.CC_DEVEDDAY DEVEDDAY,				\n");
			strQuery.append("       a.CC_DEVTIME, a.CC_DEVMM, a.CC_UNITDATE, b.cm_username,		\n");
			strQuery.append("       a.cc_jobstday,a.cc_jobedday,a.cc_jobetc,a.cc_edgbncd,		\n");
			strQuery.append("       to_char(a.cc_edreqdt,'yyyy/mm/dd') edreqdt,		            \n");
			strQuery.append("       to_char(a.cc_eddate,'yyyy/mm/dd') eddate,                   \n");
			strQuery.append("       nvl(a.cc_status,'0') cc_status,a.cc_chgedacpt,c.cc_substatus \n");				
			strQuery.append("  from cmc0210 a,cmm0040 b,cmc0110 c 							    \n");	
			if (ReqCd.equals("42")) {
				strQuery.append("      ,(select cc_scmuser,count(*) cnt from cmc0211		    \n");				
				strQuery.append("        where cc_isrid=? and cc_isrsub=?  	 			        \n");				
				strQuery.append("        group by cc_scmuser) c    	 			                \n");
			}
			strQuery.append(" where a.CC_ISRID=? and a.CC_ISRSUB=?   	        			    \n");
			if (UserId != null && UserId != "") {
				strQuery.append(" and a.CC_SCMUSER = ?							         		\n");
			}
			strQuery.append(" and a.cc_scmuser=b.cm_userid						         		\n");
			strQuery.append(" and a.cc_isrid=c.cc_isrid and a.cc_isrsub=c.cc_isrsub       		\n");
			strQuery.append(" and nvl(a.cc_status,'0')<>'C'						         		\n");
			if (ReqCd.equals("42")) {
				strQuery.append(" and a.cc_scmuser=c.cc_scmuser(+)				        		\n");
			}
			pstmt = conn.prepareStatement(strQuery.toString());	
			pstmt = new LoggableStatement(conn,strQuery.toString());
			pstmtcount = 1;
			if (ReqCd.equals("42")) {
				pstmt.setString(pstmtcount++,IsrId);
				pstmt.setString(pstmtcount++,SubId);
			}
			pstmt.setString(pstmtcount++,IsrId);
			pstmt.setString(pstmtcount++,SubId);
			if (UserId != null && UserId != "") pstmt.setString(pstmtcount++,UserId);
	        ecamsLogger.error(((LoggableStatement)pstmt).getQueryString());           
	        rs = pstmt.executeQuery();
	        rsval.clear();
			while (rs.next()){			
				rst = new HashMap<String, String>();
				
				rst.put("cc_scmuser",rs.getString("CC_SCMUSER"));
				//rst.put("cc_status",rs.getString("CC_STATUS"));
				rst.put("creatdt",rs.getString("CREATDT"));
				rst.put("lastdt",rs.getString("LASTDT"));
				rst.put("devstday",rs.getString("DEVSTDAY"));
				rst.put("devedday",rs.getString("DEVEDDAY"));
				rst.put("devtime",rs.getString("CC_DEVTIME"));
				rst.put("cc_substatus",rs.getString("cc_substatus"));
				if (rs.getBigDecimal("CC_DEVMM") != null){
					rst.put("devmm",rs.getBigDecimal("CC_DEVMM").toString());
				} else{
					rst.put("devmm", "");
				}
				rst.put("unitdate",rs.getString("CC_UNITDATE"));
				rst.put("cm_username",rs.getString("cm_username"));
				if (ReqCd.equals("42")) rst.put("cnt",rs.getString("cnt"));
				rst.put("cc_status", rs.getString("cc_status"));
				
				rst.put("pgmcnt", "");
				rst.put("realday", "");
				rst.put("realmm", "");
				rst.put("monterm", "");
				rst.put("prcdate", "");
				rst.put("cc_edgbncd", "");
				rst.put("cc_jobetc", "");
				rst.put("cc_jobstday", "");
				rst.put("cc_jobedday", "");
				rst.put("edreqdt", "");
				rst.put("eddate", "");
				rst.put("cc_seqno", "");
				rst.put("pgmsw", "N");
				if (ReqCd.equals("43")) {  //단위테스트작성
					pstmtcount = 0;
					strQuery.setLength(0); 
					strQuery.append("select PRJPROG_USER(?,?,?) pgmcnt from dual    \n");
					pstmt2 = conn.prepareStatement(strQuery.toString());
					pstmt2.setString(1, IsrId);
					pstmt2.setString(2,SubId);
					pstmt2.setString(3, rs.getString("cc_scmuser"));
					rs2 = pstmt2.executeQuery();
					if (rs2.next()) {
						if (rs2.getInt("pgmcnt")>0) rst.put("pgmsw", "Y");
					}
					rs2.close();
					pstmt2.close();
					
					if (rst.get("pgmsw").equals("N")) {
						strQuery.setLength(0); 
						strQuery.append("select count(*) pgmcnt from cmc0260  \n");
						strQuery.append(" where cc_isrid=? and cc_isrsub=?    \n");
						strQuery.append("   and cc_scmuser=?                  \n");
						pstmt2 = conn.prepareStatement(strQuery.toString());
						pstmt2.setString(1, IsrId);
						pstmt2.setString(2,SubId);
						pstmt2.setString(3, rs.getString("cc_scmuser"));
						rs2 = pstmt2.executeQuery();
						if (rs2.next()) {
							if (rs2.getInt("pgmcnt")>0) rst.put("pgmsw", "Y");
						}
						rs2.close();
						pstmt2.close();
					}
				}
				if (ReqCd.equals("47")) { //작업계획서작성
					strQuery.setLength(0); 
					strQuery.append("select cc_acptno from cmc0250     \n");
					strQuery.append(" where cc_isrid=? and cc_isrsub=? \n");
					strQuery.append("   and cc_scmuser=?               \n");
					pstmt2 = conn.prepareStatement(strQuery.toString());
					//pstmt2 = new LoggableStatement(conn,strQuery.toString());
					pstmt2.setString(1,IsrId);
					pstmt2.setString(2,SubId);
					pstmt2.setString(3, rs.getString("cc_scmuser"));
					rs2 = pstmt2.executeQuery();
					if (rs2.next()) {
						rst.put("cc_jobacpt", rs2.getString("cc_acptno"));
					}
					rs2.close();
					pstmt2.close();
				}
				if (ReqCd.equals("49")) {
					rst.put("cc_acptno", rs.getString("cc_chgedacpt"));
					
					pstmtcount = 0;
					strQuery.setLength(0); //실소요일,실소요공수,모니터링기간,적용일
					//PRJPROG_USER(a.cc_isrid,a.cc_isrsub,?)=0
					strQuery.append("select PRJPROG_USER(?,?,?) pgmcnt,                \n");
					strQuery.append("       to_date(to_char(SYSDATE,'yyyymmdd'),'yyyymmdd') \n");
					strQuery.append("             - to_date(?,'yyyymmdd') realday,      \n");
					strQuery.append("       (select sum(cc_jobtime) from cmc0211        \n");
					strQuery.append("         where cc_isrid=? and cc_isrsub=?          \n");
					strQuery.append("           and cc_scmuser=?) jobtime,              \n");
					strQuery.append("       (select b.cm_codename from cmc0250 a,cmm0020 b \n");
					strQuery.append("         where a.cc_isrid=? and a.cc_isrsub=?      \n");
					strQuery.append("           and a.cc_scmuser=?                      \n");
					strQuery.append("           and b.cm_macode='MONTERM'               \n");
					strQuery.append("           and b.cm_micode=a.cc_monterm) monterm,  \n");
					strQuery.append("       (select to_char(b.cr_prcdate,'yyyy/mm/dd')  \n");
					strQuery.append("          from cmc0250 a,cmr1000 b                 \n");
					strQuery.append("         where a.cc_isrid=? and a.cc_isrsub=?      \n");
					strQuery.append("           and a.cc_scmuser=?                      \n");
					strQuery.append("           and a.cc_acptno=b.cr_acptno             \n");
					strQuery.append("           and b.cr_status<>'3'                    \n");
					strQuery.append("           and b.cr_prcdate is not null) prcdate   \n");
					strQuery.append("  from dual                                        \n");
					pstmt2 = conn.prepareStatement(strQuery.toString());
		//			pstmt2 = new LoggableStatement(conn,strQuery.toString());
					pstmt2.setString(++pstmtcount,IsrId);
					pstmt2.setString(++pstmtcount,SubId);
					pstmt2.setString(++pstmtcount, rs.getString("cc_scmuser"));
					pstmt2.setString(++pstmtcount,rs.getString("DEVSTDAY"));
					pstmt2.setString(++pstmtcount,IsrId);
					pstmt2.setString(++pstmtcount,SubId);
					pstmt2.setString(++pstmtcount, rs.getString("cc_scmuser"));
					pstmt2.setString(++pstmtcount,IsrId);
					pstmt2.setString(++pstmtcount,SubId);
					pstmt2.setString(++pstmtcount, rs.getString("cc_scmuser"));
					pstmt2.setString(++pstmtcount,IsrId);
					pstmt2.setString(++pstmtcount,SubId);
					pstmt2.setString(++pstmtcount, rs.getString("cc_scmuser"));
			//		//ecamsLogger.error(((LoggableStatement)pstmt2).getQueryString());  
					rs2 = pstmt2.executeQuery();
					if (rs2.next()) {
						rst.put("pgmcnt", rs2.getString("pgmcnt"));
						rst.put("realday", rs2.getString("realday"));
						rst.put("realmm", rs2.getString("jobtime"));
						rst.put("monterm", rs2.getString("monterm"));
						rst.put("prcdate", rs2.getString("prcdate"));						
					}
					rs2.close();
					pstmt2.close();
					
					if (ReqCd.equals("49") && rs.getString("cc_chgedacpt") != null) {
						strQuery.setLength(0);
						strQuery.append("select cc_acptno,cc_seqno,cc_status,         			\n");
						strQuery.append("       cc_endgbn,cc_jobetc,cc_jobstday,cc_jobedday,    \n");
						strQuery.append("       to_char(cc_lastdt,'yyyy/mm/dd hh24:mi') edreqdt,\n");
						strQuery.append("       to_char(cc_eddate,'yyyy/mm/dd hh24:mi') eddate  \n");
						strQuery.append("  from cmc0290                    \n");			
						strQuery.append(" where cc_isrid=? and cc_isrsub=? \n");			
						strQuery.append("   and cc_scmuser=?               \n");			
						strQuery.append("   and cc_acptno=?                \n");
						pstmt2 = conn.prepareStatement(strQuery.toString());
			//			pstmt2 = new LoggableStatement(conn,strQuery.toString());
						pstmt2.setString(1,IsrId);
						pstmt2.setString(2,SubId);
						pstmt2.setString(3, rs.getString("cc_scmuser"));
						pstmt2.setString(4, rs.getString("cc_chgedacpt"));
				//		//ecamsLogger.error(((LoggableStatement)pstmt2).getQueryString());
			            rs2 = pstmt2.executeQuery();
						if (rs2.next()){
							rst.put("cc_edgbncd", rs2.getString("cc_endgbn"));
							rst.put("cc_jobetc", rs2.getString("cc_jobetc"));
							rst.put("cc_jobstday", rs2.getString("cc_jobstday"));
							rst.put("cc_jobedday", rs2.getString("cc_jobedday"));					
							rst.put("edreqdt", rs2.getString("edreqdt"));
							rst.put("eddate", rs2.getString("eddate"));
							rst.put("cc_status", rs2.getString("cc_status"));
							rst.put("cc_seqno", rs2.getString("cc_seqno"));
						}
						rs2.close();
						pstmt2.close();
					}
					rst.put("edgbn", strEdGbn);
				}
				rsval.add(rst);
				rst = null;
			}//end of while-loop statement
			
			rs.close();
			pstmt.close();
			conn.close();
			conn = null;
			pstmt = null;
			rs = null;
			
			if (rsval.size() > 0 && !ReqCd.equals("42") && !ReqCd.equals("49")&& !ReqCd.equals("47")) {
				rst = new HashMap<String, String>();
				rst.put("cc_scmuser","");
				rst.put("cm_username","전체");
				rsval.add(0,rst);
				rst = null;
			}
			
			returnObjectArray = rsval.toArray();
			rsval.clear();
			rsval = null;
			return returnObjectArray;			
			
		} catch (SQLException sqlexception) {
			sqlexception.printStackTrace();
			ecamsLogger.error("## Cmr0020.getSelectList() SQLException START ##");
			ecamsLogger.error("## Error DESC : ", sqlexception);	
			ecamsLogger.error("## Cmr0020.getSelectList() SQLException END ##");			
			throw sqlexception;
		} catch (Exception exception) {
			exception.printStackTrace();
			ecamsLogger.error("## Cmr0020.getSelectList() Exception START ##");				
			ecamsLogger.error("## Error DESC : ", exception);	
			ecamsLogger.error("## Cmr0020.getSelectList() Exception END ##");				
			throw exception;
		}finally{
			if (strQuery != null) 	strQuery = null;
			if (returnObjectArray != null)	returnObjectArray = null;
			if (rs != null)     try{rs.close();}catch (Exception ex){ex.printStackTrace();}
			if (pstmt != null)  try{pstmt.close();}catch (Exception ex2){ex2.printStackTrace();}
			if (rs2 != null)     try{rs2.close();}catch (Exception ex){ex.printStackTrace();}
			if (pstmt2 != null)  try{pstmt2.close();}catch (Exception ex2){ex2.printStackTrace();}
			if (conn != null){
				try{
					ConnectionResource.release(conn);
				}catch(Exception ex3){
					ecamsLogger.error("## Cmr0020.getSelectList() connection release exception ##");
					ex3.printStackTrace();
				}
			}
		}
	}//end of getReqList() method statement
    
    public Object[] get_Worktime(String IsrId, String IsrSub) throws SQLException, Exception{
		Connection        conn        = null;
		PreparedStatement pstmt       = null;
		ResultSet         rs          = null;
		StringBuffer      strQuery    = new StringBuffer();
		ArrayList<HashMap<String, String>>  rsval  = new ArrayList<HashMap<String, String>>();
		HashMap<String, String>			  rst		  = null;
		ConnectionContext connectionContext = new ConnectionResource();	
		int				  pstmtcount  = 1;
		Object[] returnObjectArray		 = null;
		
		try {
			conn = connectionContext.getConnection();
			strQuery.setLength(0);
			strQuery.append("select a.CC_SCMUSER,a.CC_JOBDAY,      \n");	
			strQuery.append("       a.CC_JOBTIME,b.cm_username	   \n");				
			strQuery.append("  from cmc0211 a,cmm0040 b			   \n");
			strQuery.append(" where a.CC_ISRID=? and a.CC_ISRSUB=? \n");
			strQuery.append("   and a.cc_scmuser=b.cm_userid	   \n");
			pstmt = conn.prepareStatement(strQuery.toString());	
//			pstmt = new LoggableStatement(conn,strQuery.toString());
			pstmtcount = 1;
			pstmt.setString(pstmtcount++,IsrId);
			pstmt.setString(pstmtcount++,IsrSub);
	//        //ecamsLogger.error(((LoggableStatement)pstmt).getQueryString());           
	        rs = pstmt.executeQuery();
	        rsval.clear();
			while (rs.next()){			
				rst = new HashMap<String, String>();
				rst.put("CC_SCMUSER",rs.getString("CC_SCMUSER"));
				rst.put("CC_JOBDAY", rs.getString("CC_JOBDAY").substring(0,4)+"/"+
			               rs.getString("CC_JOBDAY").substring(4,6)+"/"+
			               rs.getString("CC_JOBDAY").substring(6));
				rst.put("CC_JOBTIME",rs.getString("CC_JOBTIME"));
				rst.put("cm_username",rs.getString("cm_username"));
				rsval.add(rst);
				rst = null;
			}//end of while-loop statement	
			rs.close();
			pstmt.close();
			conn.close();
			conn = null;
			pstmt = null;
			rs = null;
			
			returnObjectArray = rsval.toArray();
			rsval.clear();
			rsval = null;
			return returnObjectArray;			
			
		} catch (SQLException sqlexception) {
			sqlexception.printStackTrace();
			ecamsLogger.error("## Cmr0020.get_Worktime() SQLException START ##");
			ecamsLogger.error("## Error DESC : ", sqlexception);	
			ecamsLogger.error("## Cmr0020.get_Worktime() SQLException END ##");			
			throw sqlexception;
		} catch (Exception exception) {
			exception.printStackTrace();
			ecamsLogger.error("## Cmr0020.get_Worktime() Exception START ##");				
			ecamsLogger.error("## Error DESC : ", exception);	
			ecamsLogger.error("## Cmr0020.get_Worktime() Exception END ##");				
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
					ecamsLogger.error("## Cmr0020.get_Worktime() connection release exception ##");
					ex3.printStackTrace();
				}
			}
		}
	}//end of getReqList() method statement
    
	public String setInsertList(HashMap<String,String> etcData) throws SQLException, Exception {
		Connection        conn        = null;
		PreparedStatement pstmt       = null;
		ResultSet         rs          = null;
		StringBuffer      strQuery    = new StringBuffer();
		String            retMsg      = "0";
		boolean           insFg       = true;
		int				  pstmtcount  = 1;
		ConnectionContext connectionContext = new ConnectionResource();		
		
		try {
			conn = connectionContext.getConnection();
			
			strQuery.setLength(0);                		
    		strQuery.append("select count(*) cnt from cmc0210	\n");
    		strQuery.append(" where CC_ISRID=? and CC_ISRSUB=?	\n");
    		strQuery.append("   and CC_SCMUSER=?			    \n");
    		pstmt = conn.prepareStatement(strQuery.toString());	
			//pstmt = new LoggableStatement(conn,strQuery.toString());
			pstmtcount = 1;
			pstmt.setString(pstmtcount++,etcData.get("IsrId"));
			pstmt.setString(pstmtcount++,etcData.get("SubId"));
			pstmt.setString(pstmtcount++,etcData.get("Writer"));
	        ////ecamsLogger.error(((LoggableStatement)pstmt).getQueryString());           
	        rs = pstmt.executeQuery();
			if (rs.next()){
				if (rs.getInt("cnt")>0) {
					insFg = false;
				}
			}    		
			rs.close();
			pstmt.close();
			
			if(insFg == true){
	        	strQuery.setLength(0);                		
	    		strQuery.append("insert into cmc0210 	                        \n");            		
	    		strQuery.append("  (CC_ISRID,CC_ISRSUB,CC_SCMUSER,CC_STATUS, 	\n");
	    		strQuery.append("   CC_CREATDT,CC_LASTDT,CC_DEVSTDAY,			\n");
	    		strQuery.append("   CC_DEVEDDAY,CC_DEVTIME,CC_DEVMM)		    \n");
	    		strQuery.append("   values(?,?,?,'0',SYSDATE,SYSDATE,?,?,?,?)   \n");
	    		pstmt = conn.prepareStatement(strQuery.toString());
	    		pstmtcount = 1;
	          	pstmt.setString(pstmtcount++, etcData.get("IsrId"));
	          	pstmt.setString(pstmtcount++, etcData.get("SubId"));
	      	    pstmt.setString(pstmtcount++, etcData.get("Writer"));
	      	    pstmt.setString(pstmtcount++, etcData.get("ExpDevSt"));
	      	    pstmt.setString(pstmtcount++, etcData.get("ExpDevEd"));
	      	    pstmt.setString(pstmtcount++, etcData.get("ExpTime"));
	      	    pstmt.setString(pstmtcount++, etcData.get("ExpDev"));
	//      	    pstmt.setString(pstmtcount++, etcData.get(""));
	      		pstmt.executeUpdate();           		
		        pstmt.close();
			}else{
				strQuery.setLength(0);              		
	    		strQuery.append("update cmc0210 set CC_LASTDT=SYSDATE,    \n");              		
	    		strQuery.append("       CC_DEVSTDAY=?,CC_DEVEDDAY=?,	  \n");
	    		strQuery.append("       CC_DEVTIME=?,CC_DEVMM=?			  \n");
	    		strQuery.append(" where CC_ISRID=? and CC_ISRSUB=?		  \n");
	    		strQuery.append("   and CC_SCMUSER=?					  \n");
	    		
	    		pstmt = conn.prepareStatement(strQuery.toString());
	    		pstmtcount = 1;
	      	    pstmt.setString(pstmtcount++, etcData.get("ExpDevSt"));
	      	    pstmt.setString(pstmtcount++, etcData.get("ExpDevEd"));
	      	    pstmt.setString(pstmtcount++, etcData.get("ExpTime"));
	      	    pstmt.setString(pstmtcount++, etcData.get("ExpDev"));
	      	    pstmt.setString(pstmtcount++, etcData.get("IsrId"));
	          	pstmt.setString(pstmtcount++, etcData.get("SubId"));
	      	    pstmt.setString(pstmtcount++, etcData.get("Writer"));
	//      	    pstmt.setString(pstmtcount++, etcData.get(""));
	      		pstmt.executeUpdate();           		
		        pstmt.close();
			}
			
			strQuery.setLength(0);                		
    		strQuery.append("update cmc0110 set 	  \n");             		
    		strQuery.append("    cc_substatus='23'	  \n");
    		strQuery.append(" where CC_ISRID=?		  \n");
    		strQuery.append("   and CC_ISRSUB=?		  \n");
    		strQuery.append("   and CC_substatus='21' \n");
    		pstmt = conn.prepareStatement(strQuery.toString());	
			//pstmt = new LoggableStatement(conn,strQuery.toString());
			pstmtcount = 1;
			pstmt.setString(pstmtcount++,etcData.get("IsrId"));
			pstmt.setString(pstmtcount++,etcData.get("SubId"));
	        ////ecamsLogger.error(((LoggableStatement)pstmt).getQueryString());           
			pstmt.executeUpdate();           		
			rs.close();
			pstmt.close();
	        
	        conn.close();
			conn = null;
			pstmt = null;
			rs = null;
	        
	        return retMsg;
	        
	        
		} catch (SQLException sqlexception) {
			sqlexception.printStackTrace();
			ecamsLogger.error("## Cmr0020.setInsertList() SQLException START ##");
			ecamsLogger.error("## Error DESC : ", sqlexception);	
			ecamsLogger.error("## Cmr0020.setInsertList() SQLException END ##");			
			throw sqlexception;
		} catch (Exception exception) {
			exception.printStackTrace();
			ecamsLogger.error("## Cmr0020.setInsertList() Exception START ##");				
			ecamsLogger.error("## Error DESC : ", exception);	
			ecamsLogger.error("## Cmr0020.setInsertList() Exception END ##");				
			throw exception;
		}finally{
			if (strQuery != null) 	strQuery = null;
			if (rs != null)     try{rs.close();}catch (Exception ex){ex.printStackTrace();}
			if (pstmt != null)  try{pstmt.close();}catch (Exception ex2){ex2.printStackTrace();}
			if (conn != null){
				try{
					conn.close();
					conn = null;
				}catch(Exception ex3){
					ecamsLogger.error("## Cmr0020.setInsertList() connection release exception ##");
					ex3.printStackTrace();
				}
			}
		}
	}//end of cmr0020_Insert() method statement
	
	public String setTimeInsertList(HashMap<String,String> etcData) throws SQLException, Exception {
		Connection        conn        = null;
		PreparedStatement pstmt       = null;
		ResultSet         rs          = null;
		StringBuffer      strQuery    = new StringBuffer();
		String            retMsg      = "0";
		boolean           insFg       = true;
		int				  pstmtcount  = 1;
		ConnectionContext connectionContext = new ConnectionResource();		
		
		try {
			conn = connectionContext.getConnection();
			
        	strQuery.setLength(0);  
        	strQuery.append("delete cmc0211      \n");
    		strQuery.append(" where cc_isrid=?   \n");
    		strQuery.append("   and cc_isrsub=?	 \n");
    		strQuery.append("   and cc_scmuser=? \n");
    		strQuery.append("   and cc_jobday=?  \n");
    		pstmt = conn.prepareStatement(strQuery.toString());
    		pstmtcount = 1;
      	    pstmt.setString(pstmtcount++, etcData.get("IsrId"));
      	    pstmt.setString(pstmtcount++, etcData.get("SubId"));
      	    pstmt.setString(pstmtcount++, etcData.get("Writer"));
      	    pstmt.setString(pstmtcount++, etcData.get("ReDevDate"));
      	    pstmt.executeUpdate();           		
	        pstmt.close();
	        
        	strQuery.setLength(0);                		
    		strQuery.append("insert into cmc0211 		        \n");
    		strQuery.append("  (CC_ISRID,CC_ISRSUB,CC_SCMUSER,  \n");
    		strQuery.append("   CC_JOBDAY,CC_CREATDT,CC_JOBTIME)\n");
    		strQuery.append(" values(?,?,?,?,SYSDATE,?)			\n");
    		pstmt = conn.prepareStatement(strQuery.toString());
    		pstmtcount = 1;
      	    pstmt.setString(pstmtcount++, etcData.get("IsrId"));
      	    pstmt.setString(pstmtcount++, etcData.get("SubId"));
      	    pstmt.setString(pstmtcount++, etcData.get("Writer"));
      	    pstmt.setString(pstmtcount++, etcData.get("ReDevDate"));
      	    pstmt.setString(pstmtcount++, etcData.get("ReDevTime"));
//      	    pstmt.setString(pstmtcount++, etcData.get(""));
      		pstmt.executeUpdate();           		
	        pstmt.close();
		        
		    retMsg = "0";
	        
	        conn.close();
			conn = null;
			pstmt = null;
	        
	        return retMsg;
	        
	        
		} catch (SQLException sqlexception) {
			sqlexception.printStackTrace();
			ecamsLogger.error("## Cmr0020.setTimeInsertList() SQLException START ##");
			ecamsLogger.error("## Error DESC : ", sqlexception);	
			ecamsLogger.error("## Cmr0020.setTimeInsertList() SQLException END ##");			
			throw sqlexception;
		} catch (Exception exception) {
			exception.printStackTrace();
			ecamsLogger.error("## Cmr0020.setTimeInsertList() Exception START ##");				
			ecamsLogger.error("## Error DESC : ", exception);	
			ecamsLogger.error("## Cmr0020.setTimeInsertList() Exception END ##");				
			throw exception;
		}finally{
			if (strQuery != null) 	strQuery = null;
			if (rs != null)     try{rs.close();}catch (Exception ex){ex.printStackTrace();}
			if (pstmt != null)  try{pstmt.close();}catch (Exception ex2){ex2.printStackTrace();}
			if (conn != null){
				try{
					conn.close();
					conn = null;
				}catch(Exception ex3){
					ecamsLogger.error("## Cmr0020.setTimeInsertList() connection release exception ##");
					ex3.printStackTrace();
				}
			}
		}
	}//end of cmr0020_Insert() method statement
	
	public String setTimeDeleteList(HashMap<String,String> etcData) throws SQLException, Exception {
		Connection        conn        = null;
		PreparedStatement pstmt       = null;
		ResultSet         rs          = null;
		StringBuffer      strQuery    = new StringBuffer();
		
		ConnectionContext connectionContext = new ConnectionResource();		
		
		try {
			
			conn = connectionContext.getConnection();
			
			int	pstmtcount  = 1;

        	strQuery.setLength(0);  
        	strQuery.append("delete cmc0211      \n");
    		strQuery.append(" where cc_isrid=?   \n");
    		strQuery.append("   and cc_isrsub=?	 \n");
    		strQuery.append("   and cc_scmuser=? \n");
    		strQuery.append("   and cc_jobday=?  \n");
    		pstmt = conn.prepareStatement(strQuery.toString());
    		pstmtcount = 1;
      	    pstmt.setString(pstmtcount++, etcData.get("IsrId"));
      	    pstmt.setString(pstmtcount++, etcData.get("SubId"));
      	    pstmt.setString(pstmtcount++, etcData.get("Writer"));
      	    pstmt.setString(pstmtcount++, etcData.get("ReDevDate"));
      	    pstmt.executeUpdate();           		
	        pstmt.close();
	        
	        conn.close();
			conn = null;
			pstmt = null;
	        
	        return "0";
	        
	        
		} catch (SQLException sqlexception) {
			sqlexception.printStackTrace();
			ecamsLogger.error("## Cmr0020.setTimeInsertList() SQLException START ##");
			ecamsLogger.error("## Error DESC : ", sqlexception);	
			ecamsLogger.error("## Cmr0020.setTimeInsertList() SQLException END ##");			
			throw sqlexception;
		} catch (Exception exception) {
			exception.printStackTrace();
			ecamsLogger.error("## Cmr0020.setTimeInsertList() Exception START ##");				
			ecamsLogger.error("## Error DESC : ", exception);	
			ecamsLogger.error("## Cmr0020.setTimeInsertList() Exception END ##");				
			throw exception;
		}finally{
			if (strQuery != null) 	strQuery = null;
			if (rs != null)     try{rs.close();}catch (Exception ex){ex.printStackTrace();}
			if (pstmt != null)  try{pstmt.close();}catch (Exception ex2){ex2.printStackTrace();}
			if (conn != null){
				try{
					conn.close();
					conn = null;
				}catch(Exception ex3){
					ecamsLogger.error("## Cmr0020.setTimeInsertList() connection release exception ##");
					ex3.printStackTrace();
				}
			}
		}
	}//end of cmr0020_Insert() method statement
	public String getWorkDays(String strYear) throws SQLException, Exception {
		Connection        conn        = null;
		PreparedStatement pstmt       = null;
		ResultSet         rs          = null;
		StringBuffer      strQuery    = new StringBuffer();
		String            retMsg      = "";
		boolean           insFg       = true;
		int				  pstmtcount  = 1;
		ConnectionContext connectionContext = new ConnectionResource();		
			
		try {
			conn = connectionContext.getConnection();
			
        	strQuery.setLength(0);  
        	strQuery.append("select cm_monthday from cmm0017      \n");
    		strQuery.append(" where cm_year=?   \n");
    		pstmt = conn.prepareStatement(strQuery.toString());
			pstmt.setString(pstmtcount++,strYear);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				retMsg = rs.getString("cm_monthday");
			}else{
				retMsg = "No";
			}
			rs.close();
			pstmt.close();
	        
	        conn.close();
			conn = null;
			pstmt = null;
	        
	        return retMsg;
	        
	        
		} catch (SQLException sqlexception) {
			sqlexception.printStackTrace();
			ecamsLogger.error("## Cmr0020.setTimeInsertList() SQLException START ##");
			ecamsLogger.error("## Error DESC : ", sqlexception);	
			ecamsLogger.error("## Cmr0020.setTimeInsertList() SQLException END ##");			
			throw sqlexception;
		} catch (Exception exception) {
			exception.printStackTrace();
			ecamsLogger.error("## Cmr0020.setTimeInsertList() Exception START ##");				
			ecamsLogger.error("## Error DESC : ", exception);	
			ecamsLogger.error("## Cmr0020.setTimeInsertList() Exception END ##");				
			throw exception;
		}finally{
			if (strQuery != null) 	strQuery = null;
			if (rs != null)     try{rs.close();}catch (Exception ex){ex.printStackTrace();}
			if (pstmt != null)  try{pstmt.close();}catch (Exception ex2){ex2.printStackTrace();}
			if (conn != null){
				try{
					conn.close();
					conn = null;
				}catch(Exception ex3){
					ecamsLogger.error("## Cmr0020.setTimeInsertList() connection release exception ##");
					ex3.printStackTrace();
				}
			}
		}
	}//end of cmr0020_Insert() method statement
	
}//end of Cmr0020 class statement