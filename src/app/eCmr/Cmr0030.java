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
public class Cmr0030{    
	

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
    
    
    public Object[] getUnitTest(String IsrId, String SubId, String UserId) throws SQLException, Exception{
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
			strQuery.append("select a.CC_CASESEQ,a.CC_SCMUSER,                        \n");
			strQuery.append("       nvl(a.CC_NOTHING,'N') CC_NOTHING,                 \n");
			strQuery.append(" 		to_char(a.CC_CREATDT,'yyyy/mm/dd') CREATDT, 	  \n");
			strQuery.append("		to_char(a.CC_LASTDT,'yyyy/mm/dd') LASTDT,		  \n");
			strQuery.append("       a.CC_TESTGBN,a.CC_CASENAME,a.CC_EXPRST,a.CC_ETC,  \n");
			strQuery.append("       a.CC_TESTDAY,a.CC_TESTRST,	                      \n");
			strQuery.append("       a.CC_LASTRST, b.cm_username			              \n");
			strQuery.append("  from cmc0230 a, cmm0040 b							  \n");
			strQuery.append(" where a.CC_ISRID=? and a.CC_ISRSUB=?					  \n");
			strQuery.append("   and nvl(a.cc_status,'0')<>'3'						  \n");
			strQuery.append("   and a.cc_scmuser=b.cm_userid						  \n");
			pstmt = conn.prepareStatement(strQuery.toString());	
			
	//		pstmt = new LoggableStatement(conn,strQuery.toString());
			
			pstmtcount = 1;
			pstmt.setString(pstmtcount++,IsrId);
			pstmt.setString(pstmtcount++,SubId);	
	//        //ecamsLogger.error(((LoggableStatement)pstmt).getQueryString());           
	        rs = pstmt.executeQuery();
	        rsval.clear();
			while (rs.next()){			
				rst = new HashMap<String, String>();
				rst.put("CC_CASESEQ",rs.getString("CC_CASESEQ"));
				rst.put("CC_SCMUSER",rs.getString("CC_SCMUSER"));
				rst.put("CC_CREATDT",rs.getString("CREATDT"));
				rst.put("CC_LASTDT",rs.getString("LASTDT"));
				rst.put("CC_TESTGBN",rs.getString("CC_TESTGBN"));
				if (rs.getString("CC_NOTHING").equals("Y")) rst.put("CC_CASENAME","해당사항없음");
				else rst.put("CC_CASENAME",rs.getString("CC_CASENAME"));
				rst.put("CC_EXPRST",rs.getString("CC_EXPRST"));
				rst.put("CC_ETC",rs.getString("CC_ETC"));
				if(rs.getString("CC_TESTDAY") != null){
					rst.put("CC_TESTDAY", rs.getString("CC_TESTDAY").substring(0,4)+"/"+
					        rs.getString("CC_TESTDAY").substring(4,6)+"/"+
					        rs.getString("CC_TESTDAY").substring(6));
				}
				
				rst.put("CC_TESTRST",rs.getString("CC_TESTRST"));
				rst.put("CC_LASTRST",rs.getString("CC_LASTRST"));
				rst.put("cm_username",rs.getString("cm_username"));
				rst.put("CC_NOTHING",rs.getString("CC_NOTHING"));
				rsval.add(rst);
				rst = null;
			}//end of while-loop statement
			rs.close();
			pstmt.close();
			conn.close();
			conn = null;
			rs = null;
			pstmt = null;
			
			returnObjectArray = rsval.toArray();
			rsval.clear();
			rsval = null;
			return returnObjectArray;			
			
		} catch (SQLException sqlexception) {
			sqlexception.printStackTrace();
			ecamsLogger.error("## Cmr0030.getUnitTest() SQLException START ##");
			ecamsLogger.error("## Error DESC : ", sqlexception);	
			ecamsLogger.error("## Cmr0030.getUnitTest() SQLException END ##");			
			throw sqlexception;
		} catch (Exception exception) {
			exception.printStackTrace();
			ecamsLogger.error("## Cmr0030.getUnitTest() Exception START ##");				
			ecamsLogger.error("## Error DESC : ", exception);	
			ecamsLogger.error("## Cmr0030.getUnitTest() Exception END ##");				
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
					ecamsLogger.error("## Cmr0030.getUnitTest() connection release exception ##");
					ex3.printStackTrace();
				}
			}
		}
	}//end of getUnitTest() method statement
    
    public Object[] getHisList(String IsrId, String SubId, String UserId,String qryGbn) throws SQLException, Exception{
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
			strQuery.append("select a.cr_acptno,d.cm_sysmsg,a.cr_editor,e.cm_username,           \n");
			strQuery.append(" 		 a.cr_syscd,a.cr_dsncd,a.cr_status,a.cr_rsrcname,            \n"); 
			strQuery.append("    	 TO_CHAR(b.cr_acptdate,'yyyy/mm/dd') as lastdate,            \n");	
			strQuery.append(" 		 c.cm_dirpath,f.cr_story,f.cr_itemid,g.cm_codename,          \n");
			strQuery.append(" 		 i.cm_codename qrycd                                         \n");	
			strQuery.append("  from cmr1010 a,cmr1000 b,cmm0070 c,cmm0030 d,cmm0040 e,           \n");	
			strQuery.append("       cmr0020 f,cmm0020 g,cmm0036 h,cmm0020 i                      \n"); 
			strQuery.append(" where b.cr_isrid = ? and b.cr_ISRSUB = ?                           \n");
			strQuery.append("   and b.cr_status <> '3'                                           \n");
			strQuery.append("   and b.cr_qrycd  in ('01','02','03')                              \n");
			strQuery.append("   and b.cr_acptno = a.cr_acptno	                                 \n");
			strQuery.append("   and a.cr_status <> '3'	                                         \n");
			strQuery.append("   and a.cr_confno is null                                          \n");
			strQuery.append("   and a.cr_itemid = a.cr_baseitem                                  \n");
			strQuery.append("   and a.cr_syscd = c. cm_syscd                                     \n");
			strQuery.append("   and a.cr_dsncd = c. cm_dsncd                                     \n");
			strQuery.append("   and a.cr_syscd = d. cm_syscd                                     \n");
			strQuery.append("   and a.cr_editor = e.cm_userid                                    \n");
			strQuery.append("   and a.cr_itemid = f.cr_itemid                                    \n");
			strQuery.append("   and g.cm_macode = 'CMR0020'                                      \n");
			strQuery.append("   and g.cm_micode = f.cr_status                                    \n");
			strQuery.append("   and i.cm_macode = 'REQUEST'                                      \n");
			strQuery.append("   and i.cm_micode = b.cr_qrycd                                     \n");
			strQuery.append("   and a.cr_syscd = h.cm_syscd                                      \n");
			strQuery.append("   and a.cr_rsrccd = h.cm_rsrccd                                    \n");
			if (qryGbn.equals("D")) {
				strQuery.append("   and substr(h.cm_info,46,1)<>'0'                              \n");
				
			} else if (qryGbn.equals("P")) {
				strQuery.append("   and substr(h.cm_info,46,1)<>'1'                              \n");
			}
			strQuery.append("union                                                               \n");
			strQuery.append("select a.cr_acptno,d.cm_sysmsg,a.cr_editor,e.cm_username,           \n");
			strQuery.append(" 		 a.cr_syscd,a.cr_dsncd,a.cr_status,a.cr_rsrcname,            \n"); 
			strQuery.append("    	 TO_CHAR(b.cr_acptdate,'yyyy/mm/dd') as lastdate,            \n");
			strQuery.append(" 		 c.cm_dirpath, f.cr_story,f.cr_itemid,g.cm_codename,         \n");
			strQuery.append(" 		 i.cm_codename qrycd                                         \n");	
			strQuery.append("  from cmr1010 a,cmr1000 b,cmm0070 c,cmm0030 d,cmm0040 e,           \n");	
			strQuery.append("       cmr0020 f,cmm0020 g,cmm0036 h,cmm0020 i                      \n"); 
			strQuery.append(" where b.cr_isrid = ? and b.cr_ISRSUB = ?                           \n");
			strQuery.append("   and b.cr_status <> '3'                                           \n");
			strQuery.append("   and b.cr_qrycd  = '04'                                           \n");
			strQuery.append("   and b.cr_acptno = a.cr_acptno                                    \n");
			strQuery.append("   and a.cr_status <> '3'                                           \n");
			strQuery.append("   and a.cr_itemid = a.cr_baseitem                                  \n");
			strQuery.append("   and a.cr_syscd = c. cm_syscd                                     \n");
			strQuery.append("   and a.cr_dsncd = c. cm_dsncd                                     \n");
			strQuery.append("   and a.cr_syscd = d. cm_syscd                                     \n");
			strQuery.append("   and a.cr_editor = e.cm_userid                                    \n");
			strQuery.append("   and a.cr_itemid = f.cr_itemid                                    \n");
			strQuery.append("   and g.cm_macode = 'CMR0020'                                      \n");
			strQuery.append("   and g.cm_micode = f.cr_status                                    \n");
			strQuery.append("   and i.cm_macode = 'REQUEST'                                      \n");
			strQuery.append("   and i.cm_micode = b.cr_qrycd                                     \n");
			strQuery.append("   and a.cr_syscd = h.cm_syscd                                      \n");
			strQuery.append("   and a.cr_rsrccd = h.cm_rsrccd                                    \n");
			if (qryGbn.equals("D")) {
				strQuery.append("   and substr(h.cm_info,46,1)<>'0'                              \n");
				
			} else if (qryGbn.equals("P")) {
				strQuery.append("   and substr(h.cm_info,46,1)<>'1'                              \n");
			}
			strQuery.append("union                                                               \n");
			strQuery.append("select '' cr_acptno,d.cm_sysmsg,f.cr_editor,e.cm_username,          \n");
			strQuery.append(" 		 f.cr_syscd,f.cr_dsncd,f.cr_status,f.cr_rsrcname, 	         \n");
			strQuery.append("    	 TO_CHAR(f.cr_lastdate,'yyyy/mm/dd') as lastdate,	         \n");
			strQuery.append(" 		 c.cm_dirpath,f.cr_story,f.cr_itemid,g.cm_codename,          \n");
			strQuery.append(" 		 '신규등록' qrycd                                            \n");
			strQuery.append("  from cmm0070 c,cmm0030 d,cmm0040 e,cmr0020 f,cmm0020 g,cmm0036 h  \n");
			strQuery.append(" where f.cr_lstver=0                                                \n");
			strQuery.append("   and f.cr_status='3'                                              \n");
			strQuery.append("   and f.cr_isrid is not null                                       \n");
			strQuery.append("   and f.cr_isrid = ? and f.cr_ISRSUB = ?                           \n");
			strQuery.append("   and f.cr_syscd = c. cm_syscd                                     \n");
			strQuery.append("   and f.cr_dsncd = c. cm_dsncd                                     \n");
			strQuery.append("   and f.cr_syscd = d. cm_syscd                                     \n");
			strQuery.append("   and f.cr_editor = e.cm_userid                                    \n");
			strQuery.append("   and f.cr_rsrccd not in (select cm_samersrc from cmm0037          \n");
			strQuery.append("                            where cm_syscd=f.cr_syscd               \n");
			strQuery.append("                              and cm_samersrc is not null           \n");
			strQuery.append("                              and cm_factcd='04')	                 \n");
			strQuery.append("   and g.cm_macode='CMR0020'                                        \n");
			strQuery.append("   and g.cm_micode = f.cr_status                                    \n");
			strQuery.append("   and f.cr_syscd = h.cm_syscd                                      \n");
			strQuery.append("   and f.cr_rsrccd = h.cm_rsrccd                                    \n");
			if (qryGbn.equals("D")) {
				strQuery.append("   and substr(h.cm_info,46,1)<>'0'                              \n");
				
			} else if (qryGbn.equals("P")) {
				strQuery.append("   and substr(h.cm_info,46,1)<>'1'                              \n");
			}
			pstmt = conn.prepareStatement(strQuery.toString());	
			//pstmt = new LoggableStatement(conn,strQuery.toString());
			pstmtcount = 1;
			pstmt.setString(pstmtcount++,IsrId);
			pstmt.setString(pstmtcount++,SubId);
			pstmt.setString(pstmtcount++,IsrId);
			pstmt.setString(pstmtcount++,SubId);
			pstmt.setString(pstmtcount++,IsrId);
			pstmt.setString(pstmtcount++,SubId);
	        //ecamsLogger.error(((LoggableStatement)pstmt).getQueryString());           
	        rs = pstmt.executeQuery();
	        rsval.clear();
			while (rs.next()){			
				rst = new HashMap<String, String>();
				rst.put("acptno",rs.getString("cr_acptno"));
				rst.put("sysmsg",rs.getString("cm_sysmsg"));
				rst.put("editor",rs.getString("cr_editor"));
				rst.put("username",rs.getString("cm_username"));
				rst.put("syscd",rs.getString("cr_syscd"));
				rst.put("dsncd",rs.getString("cr_dsncd"));
				rst.put("status",rs.getString("cr_status"));
				rst.put("rsrcname",rs.getString("cr_rsrcname"));
				rst.put("lastdate",rs.getString("lastdate"));
				rst.put("dirpath", rs.getString("cm_dirpath"));
				rst.put("story", rs.getString("cr_story"));
				rst.put("itemid", rs.getString("cr_itemid"));
				rst.put("sta", rs.getString("cm_codename"));
				rst.put("qrycd", rs.getString("qrycd"));
				rst.put("selfsw","Y");
				rsval.add(rst);
				rst = null;
			}//end of while-loop statement
			rs.close();
			pstmt.close();
			
			strQuery.setLength(0);
			strQuery.append("select '' cr_acptno,d.cm_sysmsg,a.cc_scmuser cr_editor,e.cm_username,\n");
			strQuery.append(" 		 f.cr_syscd,f.cr_dsncd,f.cr_status,f.cr_rsrcname, 	         \n");
			strQuery.append("    	 TO_CHAR(f.cr_lastdate,'yyyy/mm/dd') as lastdate,	         \n");
			strQuery.append(" 		 c.cm_dirpath,f.cr_story,f.cr_itemid,g.cm_codename,          \n");
			strQuery.append(" 		 '연관등록' qrycd,a.cc_base_isrid,a.cc_base_isrsub,           \n");
			strQuery.append(" 		 j.cc_isrtitle,k.cm_codename isrsta                          \n");
			strQuery.append("  from cmm0070 c,cmm0030 d,cmm0040 e,cmr0020 f,cmm0020 g,cmm0036 h, \n");
			strQuery.append("       cmc0260 a,cmc0100 j,cmm0020 k,cmc0110 l                      \n");
			strQuery.append(" where a.cc_isrid = ? and a.cc_isrsub = ?                           \n");
			strQuery.append("   and a.cc_itemid=f.cr_itemid                                      \n");
			strQuery.append("   and f.cr_syscd = c. cm_syscd                                     \n");
			strQuery.append("   and f.cr_dsncd = c. cm_dsncd                                     \n");
			strQuery.append("   and f.cr_syscd = d. cm_syscd                                     \n");
			strQuery.append("   and f.cr_editor = e.cm_userid                                    \n");
			strQuery.append("   and f.cr_rsrccd not in (select cm_samersrc from cmm0037          \n");
			strQuery.append("                            where cm_syscd=f.cr_syscd               \n");
			strQuery.append("                              and cm_samersrc is not null           \n");
			strQuery.append("                              and cm_factcd='04')	                 \n");
			strQuery.append("   and g.cm_macode='CMR0020'                                        \n");
			strQuery.append("   and g.cm_micode = f.cr_status                                    \n");
			strQuery.append("   and f.cr_syscd = h.cm_syscd                                      \n");
			strQuery.append("   and f.cr_rsrccd = h.cm_rsrccd                                    \n");
			strQuery.append("   and a.cc_base_isrid=j.cc_isrid                                   \n");
			strQuery.append("   and a.cc_base_isrid=l.cc_isrid and a.cc_base_isrsub=l.cc_isrsub  \n");
			strQuery.append("   and k.cm_macode='ISRSTASUB'                                      \n");
			strQuery.append("   and k.cm_micode=l.cc_substatus                                   \n");
			if (qryGbn.equals("D")) {
				strQuery.append("   and substr(h.cm_info,46,1)<>'0'                              \n");
				
			} else if (qryGbn.equals("P")) {
				strQuery.append("   and substr(h.cm_info,46,1)<>'1'                              \n");
			}

			pstmt = conn.prepareStatement(strQuery.toString());	
//			pstmt = new LoggableStatement(conn,strQuery.toString());
			pstmtcount = 1;
			pstmt.setString(pstmtcount++,IsrId);
			pstmt.setString(pstmtcount++,SubId);
	//        //ecamsLogger.error(((LoggableStatement)pstmt).getQueryString());           
	        rs = pstmt.executeQuery();
			while (rs.next()){			
				rst = new HashMap<String, String>();
				rst.put("acptno",rs.getString("cr_acptno"));
				rst.put("sysmsg",rs.getString("cm_sysmsg"));
				rst.put("editor",rs.getString("cr_editor"));
				rst.put("username",rs.getString("cm_username"));
				rst.put("syscd",rs.getString("cr_syscd"));
				rst.put("dsncd",rs.getString("cr_dsncd"));
				rst.put("status",rs.getString("cr_status"));
				rst.put("rsrcname",rs.getString("cr_rsrcname"));
				rst.put("lastdate",rs.getString("lastdate"));
				rst.put("dirpath", rs.getString("cm_dirpath"));
				rst.put("story", rs.getString("cr_story"));
				rst.put("itemid", rs.getString("cr_itemid"));
				rst.put("sta", rs.getString("cm_codename"));
				rst.put("qrycd", rs.getString("qrycd"));
				rst.put("isrid", rs.getString("cc_base_isrid")+ "-"+ rs.getString("cc_base_isrsub"));
				rst.put("isrtitle", rs.getString("cc_isrtitle"));
				rst.put("isrsta", rs.getString("isrsta"));
				rst.put("selfsw","N");
				rsval.add(rst);
				rst = null;
			}//end of while-loop statement
			rs.close();
			pstmt.close();
			
			int i = 0;
			strQuery.setLength(0);
			strQuery.append("select a.cc_itemid,a.cc_isrid,a.cc_isrsub,\n");
			strQuery.append("       c.cm_codename,d.cc_isrtitle        \n");
			strQuery.append("  from cmc0260 a,cmc0110 b,cmm0020 c,cmc0100 d \n");
			strQuery.append(" where a.cc_base_isrid=?                  \n");
			strQuery.append("   and a.cc_base_isrsub=?                 \n");
			strQuery.append("   and a.cc_isrid=b.cc_isrid              \n");
			strQuery.append("   and a.cc_isrsub=b.cc_isrsub            \n");
			strQuery.append("   and c.cm_macode='ISRSTASUB'            \n");
			strQuery.append("   and c.cm_micode=b.cc_substatus         \n");
			strQuery.append("   and a.cc_isrid=d.cc_isrid              \n");

			pstmt = conn.prepareStatement(strQuery.toString());	
//			pstmt = new LoggableStatement(conn,strQuery.toString());
			pstmtcount = 1;
			pstmt.setString(pstmtcount++,IsrId);
			pstmt.setString(pstmtcount++,SubId);
//	        //ecamsLogger.error(((LoggableStatement)pstmt).getQueryString());           
	        rs = pstmt.executeQuery();
			while (rs.next()){	
				for (i=0;rsval.size()>i;i++) {
					if (rsval.get(i).get("selfsw").equals("Y") && 
						rsval.get(i).get("itemid").equals(rs.getString("cc_itemid"))) {
						rst = new HashMap<String, String>();
						rst = rsval.get(i);
						rst.put("qrycd", "ISR연결");
						rst.put("isrsta", rs.getString("cm_codename"));
						rst.put("isrid", rs.getString("cc_isrid")+ "-"+ rs.getString("cc_isrsub"));
						rst.put("isrtitle", rs.getString("cc_isrtitle"));
						rsval.set(i, rst);
						rst = null;
					}
				}
				
			}//end of while-loop statement
			rs.close();
			pstmt.close();
			conn.close();
			conn = null;
			rs = null;
			pstmt = null;
			
			returnObjectArray = rsval.toArray();
			rsval.clear();
			rsval = null;
			return returnObjectArray;			
			
		} catch (SQLException sqlexception) {
			sqlexception.printStackTrace();
			ecamsLogger.error("## Cmr0030.getHisList() SQLException START ##");
			ecamsLogger.error("## Error DESC : ", sqlexception);	
			ecamsLogger.error("## Cmr0030.getHisList() SQLException END ##");			
			throw sqlexception;
		} catch (Exception exception) {
			exception.printStackTrace();
			ecamsLogger.error("## Cmr0030.getHisList() Exception START ##");				
			ecamsLogger.error("## Error DESC : ", exception);	
			ecamsLogger.error("## Cmr0030.getHisList() Exception END ##");				
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
					ecamsLogger.error("## Cmr0030.getHisList() connection release exception ##");
					ex3.printStackTrace();
				}
			}
		}
	}//end of getHisList() method statement
    public Object[] getProgList(String IsrId, String SubId, String UserId) throws SQLException, Exception{
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
			strQuery.append("select a.cr_acptno,d.cm_sysmsg,a.cr_editor,e.cm_username,           \n");
			strQuery.append(" 		 a.cr_syscd,a.cr_dsncd,a.cr_status,a.cr_rsrcname,            \n"); 
			strQuery.append("    	 TO_CHAR(b.cr_acptdate,'yyyy/mm/dd') as lastdate,            \n");	
			strQuery.append(" 		 c.cm_dirpath, f.cr_story,f.cr_itemid,g.cm_codename,         \n");
			strQuery.append(" 		 i.cm_codename qrycd                                         \n");	
			strQuery.append("  from cmr1010 a,cmr1000 b,cmm0070 c,cmm0030 d,cmm0040 e,           \n");	
			strQuery.append("       cmr0020 f,cmm0020 g,cmm0036 h,cmm0020 i                      \n"); 
			strQuery.append(" where b.cr_isrid = ? and b.cr_ISRSUB = ?                           \n");
			strQuery.append("   and b.cr_status <> '3'                                           \n");
			strQuery.append("   and b.cr_qrycd  in ('01','02','03')                              \n");
			strQuery.append("   and b.cr_acptno = a.cr_acptno	                                 \n");
			strQuery.append("   and a.cr_status <> '3'	                                         \n");
			strQuery.append("   and a.cr_confno is null                                          \n");
			strQuery.append("   and a.cr_itemid = a.cr_baseitem                                  \n");
			strQuery.append("   and a.cr_syscd = c. cm_syscd                                     \n");
			strQuery.append("   and a.cr_dsncd = c. cm_dsncd                                     \n");
			strQuery.append("   and a.cr_syscd = d. cm_syscd                                     \n");
			strQuery.append("   and a.cr_editor = e.cm_userid                                    \n");
			strQuery.append("   and a.cr_itemid = f.cr_itemid                                    \n");
			strQuery.append("   and g.cm_macode = 'CMR0020'                                      \n");
			strQuery.append("   and g.cm_micode = f.cr_status                                    \n");
			strQuery.append("   and i.cm_macode = 'REQUEST'                                      \n");
			strQuery.append("   and i.cm_micode = b.cr_qrycd                                     \n");
			strQuery.append("   and a.cr_syscd = h.cm_syscd                                      \n");
			strQuery.append("   and a.cr_rsrccd = h.cm_rsrccd                                    \n");
			strQuery.append("union                                                               \n");
			strQuery.append("select '' cr_acptno,d.cm_sysmsg,f.cr_editor,e.cm_username,          \n");
			strQuery.append(" 		 f.cr_syscd,f.cr_dsncd,f.cr_status,f.cr_rsrcname, 	         \n");
			strQuery.append("    	 TO_CHAR(f.cr_lastdate,'yyyy/mm/dd') as lastdate,	         \n");
			strQuery.append(" 		 c.cm_dirpath,f.cr_story,f.cr_itemid,g.cm_codename,          \n");
			strQuery.append(" 		 '신규등록' qrycd                                             \n");
			strQuery.append("  from cmm0070 c,cmm0030 d,cmm0040 e,cmr0020 f,cmm0020 g,cmm0036 h  \n");
			strQuery.append(" where f.cr_lstver=0                                                \n");
			strQuery.append("   and f.cr_status='3'                                              \n");
			strQuery.append("   and f.cr_isrid is not null                                       \n");
			strQuery.append("   and f.cr_isrid = ? and f.cr_ISRSUB = ?                           \n");
			strQuery.append("   and f.cr_syscd = c. cm_syscd                                     \n");
			strQuery.append("   and f.cr_dsncd = c. cm_dsncd                                     \n");
			strQuery.append("   and f.cr_syscd = d. cm_syscd                                     \n");
			strQuery.append("   and f.cr_editor = e.cm_userid                                    \n");
			strQuery.append("   and f.cr_rsrccd not in (select cm_samersrc from cmm0037          \n");
			strQuery.append("                            where cm_syscd=f.cr_syscd               \n");
			strQuery.append("                              and cm_samersrc is not null           \n");
			strQuery.append("                              and cm_factcd='04')	                 \n");
			strQuery.append("   and g.cm_macode='CMR0020'                                        \n");
			strQuery.append("   and g.cm_micode = f.cr_status                                    \n");
			strQuery.append("   and f.cr_syscd = h.cm_syscd                                      \n");
			strQuery.append("   and f.cr_rsrccd = h.cm_rsrccd                                    \n");
			
			pstmt = conn.prepareStatement(strQuery.toString());	
	//		pstmt = new LoggableStatement(conn,strQuery.toString());
			pstmtcount = 1;
			pstmt.setString(pstmtcount++,IsrId);
			pstmt.setString(pstmtcount++,SubId);
			pstmt.setString(pstmtcount++,IsrId);
			pstmt.setString(pstmtcount++,SubId);
	//        //ecamsLogger.error(((LoggableStatement)pstmt).getQueryString());           
	        rs = pstmt.executeQuery();
	        rsval.clear();
			while (rs.next()){			
				rst = new HashMap<String, String>();
				rst.put("acptno",rs.getString("cr_acptno"));
				rst.put("sysmsg",rs.getString("cm_sysmsg"));
				rst.put("editor",rs.getString("cr_editor"));
				rst.put("username",rs.getString("cm_username"));
				rst.put("syscd",rs.getString("cr_syscd"));
				rst.put("dsncd",rs.getString("cr_dsncd"));
				rst.put("status",rs.getString("cr_status"));
				rst.put("rsrcname",rs.getString("cr_rsrcname"));
				rst.put("lastdate",rs.getString("lastdate"));
				rst.put("dirpath", rs.getString("cm_dirpath"));
				rst.put("story", rs.getString("cr_story"));
				rst.put("itemid", rs.getString("cr_itemid"));
				rst.put("sta", rs.getString("cm_codename"));
				rst.put("qrycd", rs.getString("qrycd"));
				rst.put("checked", "0");
				rst.put("enabled", "1");
				rsval.add(rst);
				rst = null;
			}//end of while-loop statement
			rs.close();
			pstmt.close();
			conn.close();
			conn = null;
			rs = null;
			pstmt = null;
			
			returnObjectArray = rsval.toArray();
			rsval.clear();
			rsval = null;
			return returnObjectArray;			
			
		} catch (SQLException sqlexception) {
			sqlexception.printStackTrace();
			ecamsLogger.error("## Cmr0030.getProgList() SQLException START ##");
			ecamsLogger.error("## Error DESC : ", sqlexception);	
			ecamsLogger.error("## Cmr0030.getProgList() SQLException END ##");			
			throw sqlexception;
		} catch (Exception exception) {
			exception.printStackTrace();
			ecamsLogger.error("## Cmr0030.getProgList() Exception START ##");				
			ecamsLogger.error("## Error DESC : ", exception);	
			ecamsLogger.error("## Cmr0030.getProgList() Exception END ##");				
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
					ecamsLogger.error("## Cmr0030.getProgList() connection release exception ##");
					ex3.printStackTrace();
				}
			}
		}
	}//end of getProgList() method statement

    public String getRelatList(String IsrId, String SubId, String UserId) throws SQLException, Exception{
		Connection        conn        = null;
		PreparedStatement pstmt       = null;
		ResultSet         rs          = null;
		StringBuffer      strQuery    = new StringBuffer();
		ConnectionContext connectionContext = new ConnectionResource();	
		int				  pstmtcount  = 1;
		String            retMsg      = "";
		
		try {
			conn = connectionContext.getConnection();
			strQuery.append("select a.cc_itemid from cmc0260 a           \n");	
			strQuery.append(" where a.cc_isrid=? and a.cc_isrsub=?       \n");
			strQuery.append("   and a.cc_scmuser=?                       \n");
			pstmt = conn.prepareStatement(strQuery.toString());	
//			pstmt = new LoggableStatement(conn,strQuery.toString());
			pstmtcount = 1;
			pstmt.setString(pstmtcount++,IsrId);
			pstmt.setString(pstmtcount++,SubId);
			pstmt.setString(pstmtcount++,UserId);
//	        //ecamsLogger.error(((LoggableStatement)pstmt).getQueryString());           
	        rs = pstmt.executeQuery();
			while (rs.next()){			
				retMsg = retMsg + rs.getString("cc_itemid") + ",";
			}//end of while-loop statement
			rs.close();
			pstmt.close();
			conn.close();
			conn = null;
			rs = null;
			pstmt = null;
			
			return retMsg;			
			
		} catch (SQLException sqlexception) {
			sqlexception.printStackTrace();
			ecamsLogger.error("## Cmr0030.getRelatList() SQLException START ##");
			ecamsLogger.error("## Error DESC : ", sqlexception);	
			ecamsLogger.error("## Cmr0030.getRelatList() SQLException END ##");			
			throw sqlexception;
		} catch (Exception exception) {
			exception.printStackTrace();
			ecamsLogger.error("## Cmr0030.getRelatList() Exception START ##");				
			ecamsLogger.error("## Error DESC : ", exception);	
			ecamsLogger.error("## Cmr0030.getRelatList() Exception END ##");				
			throw exception;
		}finally{
			if (strQuery != null) 	strQuery = null;
			if (rs != null)     try{rs.close();}catch (Exception ex){ex.printStackTrace();}
			if (pstmt != null)  try{pstmt.close();}catch (Exception ex2){ex2.printStackTrace();}
			if (conn != null){
				try{
					ConnectionResource.release(conn);
				}catch(Exception ex3){
					ecamsLogger.error("## Cmr0030.getRelatList() connection release exception ##");
					ex3.printStackTrace();
				}
			}
		}
	}//end of getRelatList() method statement
    public String getProgIsr(String IsrId, String SubId, String UserId) throws SQLException, Exception{
		Connection        conn        = null;
		PreparedStatement pstmt       = null;
		ResultSet         rs          = null;
		StringBuffer      strQuery    = new StringBuffer();
		ConnectionContext connectionContext = new ConnectionResource();	
		String            retMsg      = "";
		
		try {
			conn = connectionContext.getConnection();
			strQuery.append("select a.cc_base_isrid,a.cc_base_isrsub    \n");
			strQuery.append("  from cmc0260 a                           \n");	
			strQuery.append(" where a.cc_isrid=? and a.cc_isrsub=?      \n");
			strQuery.append("   and a.cc_scmuser=?                      \n");
			strQuery.append(" group by a.cc_base_isrid,a.cc_base_isrsub \n");
			
			pstmt = conn.prepareStatement(strQuery.toString());	
	//		pstmt = new LoggableStatement(conn,strQuery.toString());
			pstmt.setString(1,IsrId);
			pstmt.setString(2,SubId);
			pstmt.setString(3,UserId);
	//        //ecamsLogger.error(((LoggableStatement)pstmt).getQueryString());           
	        rs = pstmt.executeQuery();
			if (rs.next()){	
				retMsg = rs.getString("cc_base_isrid")+ rs.getString("cc_base_isrsub");
			}//end of while-loop statement
			rs.close();
			pstmt.close();
			conn.close();
			conn = null;
			rs = null;
			pstmt = null;
			
			return retMsg;			
			
		} catch (SQLException sqlexception) {
			sqlexception.printStackTrace();
			ecamsLogger.error("## Cmr0030.getProgIsr() SQLException START ##");
			ecamsLogger.error("## Error DESC : ", sqlexception);	
			ecamsLogger.error("## Cmr0030.getProgIsr() SQLException END ##");			
			throw sqlexception;
		} catch (Exception exception) {
			exception.printStackTrace();
			ecamsLogger.error("## Cmr0030.getProgIsr() Exception START ##");				
			ecamsLogger.error("## Error DESC : ", exception);	
			ecamsLogger.error("## Cmr0030.getProgIsr() Exception END ##");				
			throw exception;
		}finally{
			if (strQuery != null) 	strQuery = null;
			if (rs != null)     try{rs.close();}catch (Exception ex){ex.printStackTrace();}
			if (pstmt != null)  try{pstmt.close();}catch (Exception ex2){ex2.printStackTrace();}
			if (conn != null){
				try{
					ConnectionResource.release(conn);
				}catch(Exception ex3){
					ecamsLogger.error("## Cmr0030.getProgIsr() connection release exception ##");
					ex3.printStackTrace();
				}
			}
		}
	}//end of getHisList() method statement
    public String setProgList(HashMap<String,String> etcData,ArrayList<HashMap<String,String>> CopyList) throws SQLException, Exception {
		Connection        conn        = null;
		PreparedStatement pstmt       = null;
		ResultSet         rs          = null;
		StringBuffer      strQuery    = new StringBuffer();
		String            retMsg      = "0";
		int				  i			  = 0;
		int				  pstmtcount  = 1;
		boolean           insSw       = false;
		ConnectionContext connectionContext = new ConnectionResource();		
		
		try {
			conn = connectionContext.getConnection();
			conn.setAutoCommit(false);
			for(i=0;i<CopyList.size();i++){				
				strQuery.setLength(0);
				strQuery.append("select count(*) cnt from cmc0260     \n");
				strQuery.append(" where cc_isrid=? and cc_isrsub=?    \n");
				strQuery.append("   and cc_scmuser=? and cc_itemid=?  \n");
				pstmt = conn.prepareStatement(strQuery.toString());
				pstmt.setString(1, etcData.get("toisrid"));
	          	pstmt.setString(2, etcData.get("toisrsub"));
	          	pstmt.setString(3, etcData.get("UserId"));
	      	    pstmt.setString(4, CopyList.get(i).get("itemid"));
	      	    rs = pstmt.executeQuery();
	      	    if (rs.next()) {
	      	    	if (rs.getInt("cnt")>0) insSw = false;
	      	    	else insSw = true;
	      	    }
	          	rs.close();
	          	pstmt.close();
	          	
	          	if (insSw == true) {
		        	strQuery.setLength(0);
		    		strQuery.append("insert into cmc0260 (CC_ISRID,CC_ISRSUB,CC_SCMUSER,CC_ITEMID,	\n");
		    		strQuery.append("   CC_CREATDT,CC_LASTDT,CC_BASE_ISRID,CC_BASE_ISRSUB)	    	\n");
		    		strQuery.append("values (?, ?, ?, ?, SYSDATE, SYSDATE, ?, ?)     	            \n");
		    		
		    		pstmt = conn.prepareStatement(strQuery.toString());
		    		//pstmt = new LoggableStatement(conn,strQuery.toString());
		    		pstmtcount = 1;
		          	pstmt.setString(pstmtcount++, etcData.get("toisrid"));
		          	pstmt.setString(pstmtcount++, etcData.get("toisrsub"));
		          	pstmt.setString(pstmtcount++, etcData.get("UserId"));
		          	pstmt.setString(pstmtcount++, CopyList.get(i).get("itemid"));
		          	pstmt.setString(pstmtcount++, etcData.get("fromisrid"));
		          	pstmt.setString(pstmtcount++, etcData.get("fromisrsub"));
		      	    ////ecamsLogger.error(((LoggableStatement)pstmt).getQueryString());
		      		pstmt.executeUpdate();
		      		pstmt.close();
	          	}
			}
			conn.commit();
	        conn.close();
			conn = null;
			rs = null;
			pstmt = null;
	        
	        return retMsg;
	        
	        
		} catch (SQLException sqlexception) {
			sqlexception.printStackTrace();
			if (rs != null)     try{rs.close();}catch (Exception ex){ex.printStackTrace();}
			if (pstmt != null)  try{pstmt.close();}catch (Exception ex2){ex2.printStackTrace();}
			if (conn != null){
				try{
					conn.rollback();
					ConnectionResource.release(conn);
					conn = null;
				}catch(Exception ex3){
					ecamsLogger.error("## Cmr0030.setProgList() connection release exception ##");
					ex3.printStackTrace();
				}				
			}
			ecamsLogger.error("## Cmr0030.setProgList() SQLException START ##");
			ecamsLogger.error("## Error DESC : ", sqlexception);	
			ecamsLogger.error("## Cmr0030.setProgList() SQLException END ##");			
			throw sqlexception;
		} catch (Exception exception) {
			exception.printStackTrace();
			if (rs != null)     try{rs.close();}catch (Exception ex){ex.printStackTrace();}
			if (pstmt != null)  try{pstmt.close();}catch (Exception ex2){ex2.printStackTrace();}
			if (conn != null){
				try{
					conn.rollback();
					ConnectionResource.release(conn);
					conn = null;
				}catch(Exception ex3){
					ecamsLogger.error("## Cmr0030.setProgList() connection release exception ##");
					ex3.printStackTrace();
				}				
			}
			ecamsLogger.error("## Cmr0030.setProgList() Exception START ##");				
			ecamsLogger.error("## Error DESC : ", exception);	
			ecamsLogger.error("## Cmr0030.setProgList() Exception END ##");				
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
					ecamsLogger.error("## Cmr0030.setProgList() connection release exception ##");
					ex3.printStackTrace();
				}
			}
		}
	}//end of setProgList() method statement
    public String delProg(String IsrId,String IsrSub,String UserId,String ItemId) throws SQLException, Exception {
		Connection        conn        = null;
		PreparedStatement pstmt       = null;
		ResultSet         rs          = null;
		StringBuffer      strQuery    = new StringBuffer();
		String            retMsg      = "0";
		int				  pstmtcount  = 1;
		ConnectionContext connectionContext = new ConnectionResource();		
		
		try {
			conn = connectionContext.getConnection();
						
        	strQuery.setLength(0);
    		strQuery.append("delete cmc0260     \n");
    		strQuery.append(" where cc_isrid=? 	\n");
    		strQuery.append("   and cc_isrsub=?	\n");
    		strQuery.append("   and cc_scmuser=?\n");
    		strQuery.append("   and cc_itemid=?	\n");
    		
    		pstmt = conn.prepareStatement(strQuery.toString());
    		//pstmt = new LoggableStatement(conn,strQuery.toString());
    		pstmtcount = 1;
          	pstmt.setString(pstmtcount++, IsrId);
          	pstmt.setString(pstmtcount++, IsrSub);
          	pstmt.setString(pstmtcount++, UserId);
          	pstmt.setString(pstmtcount++, ItemId);
      	    ////ecamsLogger.error(((LoggableStatement)pstmt).getQueryString());
      		pstmt.executeUpdate();
      		pstmt.close();
			
	        conn.close();
			conn = null;
			pstmt = null;
	        
	        return retMsg;
	        
	        
		} catch (SQLException sqlexception) {
			sqlexception.printStackTrace();
			ecamsLogger.error("## Cmr0030.delProg() SQLException START ##");
			ecamsLogger.error("## Error DESC : ", sqlexception);	
			ecamsLogger.error("## Cmr0030.delProg() SQLException END ##");			
			throw sqlexception;
		} catch (Exception exception) {
			exception.printStackTrace();
			ecamsLogger.error("## Cmr0030.delProg() Exception START ##");				
			ecamsLogger.error("## Error DESC : ", exception);	
			ecamsLogger.error("## Cmr0030.delProg() Exception END ##");				
			throw exception;
		}finally{
			if (strQuery != null) 	strQuery = null;
			if (rs != null)     try{rs.close();}catch (Exception ex){ex.printStackTrace();}
			if (pstmt != null)  try{pstmt.close();}catch (Exception ex2){ex2.printStackTrace();}
			if (conn != null){
				try{
					ConnectionResource.release(conn);
				}catch(Exception ex3){
					ecamsLogger.error("## Cmr0030.delProg() connection release exception ##");
					ex3.printStackTrace();
				}
			}
		}
	}//end of delProg() method statement
    public Object[] getHandlerList(String IsrId, String SubId, String UserId) throws SQLException, Exception{
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
			strQuery.append("select DISTINCT a.CC_SCMUSER, a.CC_ISRID, a.CC_ISRSUB,  					\n");
			strQuery.append("       b.cm_username														\n");
			strQuery.append("  from cmc0230 a, cmm0040 b												\n");
			strQuery.append(" where a.CC_SCMUSER = b.cm_userid                     						\n");
			strQuery.append(" and   a.CC_ISRID = ?														\n");
			strQuery.append(" and   a.CC_ISRSUB = ?														\n");
			pstmt = conn.prepareStatement(strQuery.toString());	
			//pstmt = new LoggableStatement(conn,strQuery.toString());
			pstmtcount = 1;
			pstmt.setString(pstmtcount++,IsrId);
			pstmt.setString(pstmtcount++,SubId);
	        ////ecamsLogger.error(((LoggableStatement)pstmt).getQueryString());           
	        rs = pstmt.executeQuery();
	        rsval.clear();
			while (rs.next()){			
				rst = new HashMap<String, String>();
				if(rs.getString("CC_SCMUSER").equals(UserId)){
					rst.put("CC_ISRID",rs.getString("CC_ISRID"));
					rst.put("CC_ISRSUB",rs.getString("CC_ISRSUB"));
					rst.put("CC_SCMUSER",rs.getString("CC_SCMUSER"));
					rst.put("cm_username",rs.getString("cm_username"));
					rsval.add(0, rst);
					rst = null;
				}else{
					rst.put("CC_ISRID",rs.getString("CC_ISRID"));
					rst.put("CC_ISRSUB",rs.getString("CC_ISRSUB"));
					rst.put("CC_SCMUSER",rs.getString("CC_SCMUSER"));
					rst.put("cm_username",rs.getString("cm_username"));
					rsval.add(rst);
					rst = null;
				}
			}//end of while-loop statement
			rs.close();
			pstmt.close();
			conn.close();
			conn = null;
			rs = null;
			pstmt = null;
			
			returnObjectArray = rsval.toArray();
			rsval.clear();
			rsval = null;
			return returnObjectArray;			
			
		} catch (SQLException sqlexception) {
			sqlexception.printStackTrace();
			ecamsLogger.error("## Cmr0030.getHandlerList() SQLException START ##");
			ecamsLogger.error("## Error DESC : ", sqlexception);	
			ecamsLogger.error("## Cmr0030.getHandlerList() SQLException END ##");			
			throw sqlexception;
		} catch (Exception exception) {
			exception.printStackTrace();
			ecamsLogger.error("## Cmr0030.getHandlerList() Exception START ##");				
			ecamsLogger.error("## Error DESC : ", exception);	
			ecamsLogger.error("## Cmr0030.getHandlerList() Exception END ##");				
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
					ecamsLogger.error("## Cmr0030.getHandlerList() connection release exception ##");
					ex3.printStackTrace();
				}
			}
		}
	}//end of getUnitTest() method statement
    
    
	public String setTcaseAdd(HashMap<String,String> etcData) throws SQLException, Exception {
		Connection        conn        = null;
		PreparedStatement pstmt       = null;
		ResultSet         rs          = null;
		StringBuffer      strQuery    = new StringBuffer();
		String            retMsg      = "0";
		boolean           insFg       = false;
		int				  seq		  = 1;
		int				  pstmtcount  = 1;
		boolean           findSw      = false;
		ConnectionContext connectionContext = new ConnectionResource();		
		
		try {
			conn = connectionContext.getConnection();
			
			if (etcData.get("testid") == null || etcData.get("testid") == "") {
				strQuery.setLength(0);
				strQuery.append("select nvl(max(cc_caseseq),0) max		\n");			
				strQuery.append("  from cmc0230  			            \n");
				strQuery.append(" where CC_ISRID = ?	    		    \n");
				strQuery.append("   and CC_ISRSUB = ?					\n");
				pstmt = conn.prepareStatement(strQuery.toString());	
				//pstmt = new LoggableStatement(conn,strQuery.toString());
				pstmtcount = 1;
				pstmt.setString(pstmtcount++,etcData.get("IsrId"));
				pstmt.setString(pstmtcount++,etcData.get("IsrSub"));
		        ////ecamsLogger.error(((LoggableStatement)pstmt).getQueryString());           
		        rs = pstmt.executeQuery();
				if (rs.next()){
					seq = rs.getInt("max")+1;
					insFg = true;
				}//end of while-loop statement
				rs.close();
				pstmt.close();
			}
			if(insFg == true){
	        	strQuery.setLength(0);
	    		strQuery.append("insert into cmc0230 (CC_ISRID,CC_ISRSUB,CC_CASESEQ,CC_SCMUSER,CC_STATUS,CC_CREATDT,    \n");
	    		strQuery.append("   CC_LASTDT,CC_TESTGBN,CC_CASENAME,CC_EXPRST,CC_ETC,CC_TESTDAY,CC_TESTRST,CC_LASTRST,	\n");
	    		strQuery.append("   CC_NOTHING)															    	\n");
	    		strQuery.append("	values (?,?,?,?,'0',SYSDATE,SYSDATE,?,?,?,?,?,?,?,?)						\n");
	    		
	    		pstmt = conn.prepareStatement(strQuery.toString());
	    		//pstmt = new LoggableStatement(conn,strQuery.toString());
	    		pstmtcount = 1;
	          	pstmt.setString(pstmtcount++, etcData.get("IsrId"));
	          	pstmt.setString(pstmtcount++, etcData.get("IsrSub"));
	      	    pstmt.setInt(pstmtcount++, seq);
	          	pstmt.setString(pstmtcount++, etcData.get("UserId"));
	      	    pstmt.setString(pstmtcount++, etcData.get("DevTit"));
	      	    pstmt.setString(pstmtcount++, etcData.get("Tcase"));
	      	    pstmt.setString(pstmtcount++, etcData.get("ExpResult"));
	      	    pstmt.setString(pstmtcount++, etcData.get("txtRef"));
	      	    pstmt.setString(pstmtcount++, etcData.get("testday"));
	      	    pstmt.setString(pstmtcount++, etcData.get("RealResult"));
	      	    pstmt.setString(pstmtcount++, etcData.get("LastResult"));
	      	    if (etcData.get("passsw").equals("1")) pstmt.setString(pstmtcount++, "Y");
	      	    else pstmt.setString(pstmtcount++, "N");
	      	    ////ecamsLogger.error(((LoggableStatement)pstmt).getQueryString());
	      		pstmt.executeUpdate();           		
		        pstmt.close();
		        
			}else {				
				seq = Integer.parseInt(etcData.get("testid"));
				
				strQuery.setLength(0);
	    		strQuery.append("update cmc0230 set CC_LASTDT = SYSDATE,CC_TESTGBN=?,CC_CASENAME=?, \n");
	    		strQuery.append("      CC_EXPRST=?,CC_ETC=?,CC_TESTDAY=?,CC_TESTRST=?,CC_LASTRST=?,	\n");
	    		if (etcData.get("passsw").equals("1")) {
	    			strQuery.append("CC_NOTHING='Y'                \n");
	    		} else {
	    			strQuery.append("CC_NOTHING='N'                \n");
	    		}
	    		strQuery.append("where CC_ISRID=?					\n");
	    		strQuery.append("and CC_ISRSUB=?					\n");
	    		strQuery.append("and CC_CASESEQ=?					\n");
	    		
	    		pstmt = conn.prepareStatement(strQuery.toString());
	    		//pstmt = new LoggableStatement(conn,strQuery.toString());
	    		pstmtcount = 1;
	      	    pstmt.setString(pstmtcount++, etcData.get("DevTit"));
	      	    pstmt.setString(pstmtcount++, etcData.get("Tcase"));
	      	    pstmt.setString(pstmtcount++, etcData.get("ExpResult"));
	      	    pstmt.setString(pstmtcount++, etcData.get("txtRef"));
	      	    pstmt.setString(pstmtcount++, etcData.get("testday"));
	      	    pstmt.setString(pstmtcount++, etcData.get("RealResult"));
	      	    pstmt.setString(pstmtcount++, etcData.get("LastResult"));
	      	    pstmt.setString(pstmtcount++, etcData.get("IsrId"));
	      	    pstmt.setString(pstmtcount++, etcData.get("IsrSub"));
	      	  	pstmt.setInt(pstmtcount++, seq);
	      		////ecamsLogger.error(((LoggableStatement)pstmt).getQueryString());
	      		pstmt.executeUpdate();           		
		        pstmt.close();
		        
			}
		    
			strQuery.setLength(0);                		
    		strQuery.append("update cmc0110 set 	  \n");             		
    		strQuery.append("    cc_substatus='23',	  \n");            		
    		strQuery.append("    cc_mainstatus='02'	  \n");
    		strQuery.append(" where CC_ISRID=?		  \n");
    		strQuery.append("   and CC_ISRSUB=?		  \n");
    		strQuery.append("   and CC_substatus in ('21','36') \n");
    		pstmt = conn.prepareStatement(strQuery.toString());	
			//pstmt = new LoggableStatement(conn,strQuery.toString());
			pstmtcount = 1;
			pstmt.setString(pstmtcount++,etcData.get("IsrId"));
			pstmt.setString(pstmtcount++,etcData.get("IsrSub"));
	        ////ecamsLogger.error(((LoggableStatement)pstmt).getQueryString());           
			pstmt.executeUpdate(); 
			pstmt.close();
			
			strQuery.setLength(0);                		
    		strQuery.append("select count(*) cnt   	               \n");      		
    		strQuery.append("  from cmc0260 a,cmc0110 b 	       \n");              		
    		strQuery.append(" where	b.cc_isrid=? and b.cc_isrsub=? \n");         		
    		strQuery.append("   and b.cc_substatus in ('23','24')  \n");  
    		strQuery.append("   and b.cc_isrid=a.cc_isrid  		   \n"); 
    		strQuery.append("   and b.cc_isrsub=a.cc_isrsub	       \n");
    		pstmt = conn.prepareStatement(strQuery.toString());	
			//pstmt = new LoggableStatement(conn,strQuery.toString());
			pstmtcount = 1;
			pstmt.setString(pstmtcount++,etcData.get("IsrId"));
			pstmt.setString(pstmtcount++,etcData.get("IsrSub"));
	        ////ecamsLogger.error(((LoggableStatement)pstmt).getQueryString());           
			rs = pstmt.executeQuery();
			if (rs.next()) {
				if (rs.getInt("cnt")>0) {
					findSw = true;
				}
			}
			pstmt.close();
			rs.close();
			
			if (findSw == true) {
				findSw = false;
				strQuery.setLength(0);                		
	    		strQuery.append("select count(*) cnt   	               \n");      		
	    		strQuery.append("  from cmc0110     	               \n");              		
	    		strQuery.append(" where	cc_isrid=? and cc_isrsub=?     \n");         		
	    		strQuery.append("   and TESTREQ_TOTAL(cc_isrid,cc_isrsub)='OK'       \n");  
	    		strQuery.append("   and TESTENDYN_TOTAL(cc_isrid,cc_isrsub,'Y')='OK' \n"); 
	    		pstmt = conn.prepareStatement(strQuery.toString());	
				//pstmt = new LoggableStatement(conn,strQuery.toString());
				pstmtcount = 1;
				pstmt.setString(pstmtcount++,etcData.get("IsrId"));
				pstmt.setString(pstmtcount++,etcData.get("IsrSub"));
		        ////ecamsLogger.error(((LoggableStatement)pstmt).getQueryString());           
				rs = pstmt.executeQuery();
				if (rs.next()) {
					if (rs.getInt("cnt")>0) {
						findSw = true;
					}
				}
				pstmt.close();
				rs.close();
				
				if (findSw == true) {
					strQuery.setLength(0);                		
		    		strQuery.append("update cmc0110 set 	  \n");             		
		    		strQuery.append("    cc_substatus='25'    \n");  
		    		strQuery.append(" where CC_ISRID=?		  \n");
		    		strQuery.append("   and CC_ISRSUB=?		  \n");
		    		pstmt = conn.prepareStatement(strQuery.toString());	
	//				pstmt = new LoggableStatement(conn,strQuery.toString());
					pstmtcount = 1;
					pstmt.setString(pstmtcount++,etcData.get("IsrId"));
					pstmt.setString(pstmtcount++,etcData.get("IsrSub"));
	//		        //ecamsLogger.error(((LoggableStatement)pstmt).getQueryString());           
					pstmt.executeUpdate(); 
					pstmt.close();
				}
			}
		    retMsg = "0";
	        
	        conn.close();
			conn = null;
			rs = null;
			pstmt = null;
	        
	        return retMsg;
	        
	        
		} catch (SQLException sqlexception) {
			sqlexception.printStackTrace();
			ecamsLogger.error("## Cmr0030.getUnitTest() SQLException START ##");
			ecamsLogger.error("## Error DESC : ", sqlexception);	
			ecamsLogger.error("## Cmr0030.getUnitTest() SQLException END ##");			
			throw sqlexception;
		} catch (Exception exception) {
			exception.printStackTrace();		
			ecamsLogger.error("## Cmr0030.getUnitTest() Exception START ##");				
			ecamsLogger.error("## Error DESC : ", exception);	
			ecamsLogger.error("## Cmr0030.getUnitTest() Exception END ##");				
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
					ecamsLogger.error("## Cmr0020.set_InsertList() connection release exception ##");
					ex3.printStackTrace();
				}
			}
		}
	}//end of set_InsertList() method statement
	
	public String TcaseDel(HashMap<String,String> etcData) throws SQLException, Exception {
		Connection        conn        = null;
		PreparedStatement pstmt       = null;
		ResultSet         rs          = null;
		StringBuffer      strQuery    = new StringBuffer();
		String            retMsg      = "0";
		int				  pstmtcount  = 1;
		ConnectionContext connectionContext = new ConnectionResource();		
		
		try {
			conn = connectionContext.getConnection();
			
        	strQuery.setLength(0);
    		strQuery.append("delete cmc0230 		\n");
    		strQuery.append(" where CC_ISRID=?		\n");
    		strQuery.append("   and CC_ISRSUB=?		\n");
    		strQuery.append("   and CC_CASESEQ=?	\n");
    		
    		pstmt = conn.prepareStatement(strQuery.toString());
    		pstmtcount = 1;
      	    pstmt.setString(pstmtcount++, etcData.get("IsrId"));
      	    pstmt.setString(pstmtcount++, etcData.get("IsrSub"));
      	  	pstmt.setString(pstmtcount++, etcData.get("testid"));
      		pstmt.executeUpdate();           		
	        pstmt.close();
		        
		    retMsg = "0";
	        conn.close();
			conn = null;
			pstmt = null;
	        return retMsg;
	        
	        
		} catch (SQLException sqlexception) {
			sqlexception.printStackTrace();
			ecamsLogger.error("## Cmr0030.TcaseDel() SQLException START ##");
			ecamsLogger.error("## Error DESC : ", sqlexception);	
			ecamsLogger.error("## Cmr0030.TcaseDel() SQLException END ##");			
			throw sqlexception;
		} catch (Exception exception) {
			exception.printStackTrace();
			ecamsLogger.error("## Cmr0030.TcaseDel() Exception START ##");				
			ecamsLogger.error("## Error DESC : ", exception);	
			ecamsLogger.error("## Cmr0030.TcaseDel() Exception END ##");				
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
					ecamsLogger.error("## Cmr0030.TcaseDel() connection release exception ##");
					ex3.printStackTrace();
				}
			}
		}
	}//end of TcaseDel() method statement
	
	public String setExcelList(ArrayList<HashMap<String,String>> FileList, HashMap<String,String> etcData) 
	throws SQLException, Exception {
		Connection        conn        = null;
		PreparedStatement pstmt       = null;
		ResultSet         rs          = null;
		StringBuffer      strQuery    = new StringBuffer();
		String            retMsg      = "0";
		int				  seq		  = 1;
		int				  i		  	  = 0;
		int				  pstmtcount  = 1;
		ConnectionContext connectionContext = new ConnectionResource();		
		
		try {
			conn = connectionContext.getConnection();
			strQuery.setLength(0);
			strQuery.append("select nvl(max(cc_caseseq),0) maxseq from cmc0230  \n");
			strQuery.append(" where CC_ISRID=? and   CC_ISRSUB=?	            \n");
			pstmt = conn.prepareStatement(strQuery.toString());	
	//		pstmt = new LoggableStatement(conn,strQuery.toString());
			pstmtcount = 1;
			pstmt.setString(pstmtcount++,etcData.get("isrid"));
			pstmt.setString(pstmtcount++,etcData.get("isrsub"));
	//        //ecamsLogger.error(((LoggableStatement)pstmt).getQueryString());           
	        rs = pstmt.executeQuery();
			if (rs.next()){
				seq = rs.getInt("maxseq");
				++seq;
			}//end of while-loop statement
			rs.close();
			pstmt.close();
			
			for (i=0 ; i<FileList.size() ; i++)			{
				strQuery.setLength(0);
	    		strQuery.append("insert into cmc0230 (CC_ISRID,CC_ISRSUB,CC_CASESEQ,CC_SCMUSER,   \n");
	    		strQuery.append("   CC_CREATDT,CC_LASTDT,CC_TESTGBN,CC_CASENAME,CC_EXPRST,CC_ETC) \n");
	    		strQuery.append("   values(?,?,?,?,SYSDATE,SYSDATE,?,?,?,?)						  \n");
	    		
	    		pstmt = conn.prepareStatement(strQuery.toString());
	    //		pstmt = new LoggableStatement(conn,strQuery.toString());
	    		pstmtcount = 1;
	          	pstmt.setString(pstmtcount++, etcData.get("isrid"));
	          	pstmt.setString(pstmtcount++, etcData.get("isrsub"));
	      	    pstmt.setInt(pstmtcount++, seq++);
	          	pstmt.setString(pstmtcount++, etcData.get("userid"));
	      	    pstmt.setString(pstmtcount++, FileList.get(i).get("casegbn"));
	      	    pstmt.setString(pstmtcount++, FileList.get(i).get("casename"));
	      	    pstmt.setString(pstmtcount++, FileList.get(i).get("caserst"));
	      	    pstmt.setString(pstmtcount++, FileList.get(i).get("caseetc"));
	    //  	    //ecamsLogger.error(((LoggableStatement)pstmt).getQueryString()); 
	      		pstmt.executeUpdate();
	      		pstmt.close();
			} 
			
			strQuery.setLength(0);                		
    		strQuery.append("update cmc0110 set 	  \n");             		
    		strQuery.append("    cc_substatus='23',	  \n");            		
    		strQuery.append("    cc_mainstatus='02'	  \n");
    		strQuery.append(" where CC_ISRID=?		  \n");
    		strQuery.append("   and CC_ISRSUB=?		  \n");
    		strQuery.append("   and CC_substatus in ('21','36') \n");
    		pstmt = conn.prepareStatement(strQuery.toString());	
	//		pstmt = new LoggableStatement(conn,strQuery.toString());
			pstmtcount = 1;
			pstmt.setString(pstmtcount++,etcData.get("isrid"));
			pstmt.setString(pstmtcount++,etcData.get("isrsub"));
	//        //ecamsLogger.error(((LoggableStatement)pstmt).getQueryString());           
			pstmt.executeUpdate(); 
			pstmt.close();
			
		    retMsg = "0";
	        
	        conn.close();
			conn = null;
			pstmt = null;
			rs = null;
	        
	        return retMsg;
	        
	        
		} catch (SQLException sqlexception) {
			sqlexception.printStackTrace();
			ecamsLogger.error("## Cmr0030.setExcelList() SQLException START ##");
			ecamsLogger.error("## Error DESC : ", sqlexception);	
			ecamsLogger.error("## Cmr0030.setExcelList() SQLException END ##");			
			throw sqlexception;
		} catch (Exception exception) {
			exception.printStackTrace();
			ecamsLogger.error("## Cmr0030.setExcelList() Exception START ##");				
			ecamsLogger.error("## Error DESC : ", exception);	
			ecamsLogger.error("## Cmr0030.setExcelList() Exception END ##");				
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
					ecamsLogger.error("## Cmr0030.setExcelList() connection release exception ##");
					ex3.printStackTrace();
				}
			}
		}
	}//end of setExcelList() method statement
}//end of Cmr0020 class statement