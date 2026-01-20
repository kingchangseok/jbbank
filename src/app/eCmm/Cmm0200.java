/*****************************************************************************************
	1. program ID	: Cmr0200.java
	2. create date	: 2006.08. 08
	3. auth		    : is.choi
	4. update date	: 
	5. auth		    : 
	6. description	: 1. USER INFO.
*****************************************************************************************/

package app.eCmm;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.InputStreamReader;

import app.common.LoggableStatement;

import java.io.OutputStreamWriter;

import org.apache.logging.log4j.Logger;


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
public class Cmm0200{    
	

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
    public Object[] getSysInfo_List(boolean clsSw,String SysCd) throws SQLException, Exception {
    	Connection        conn        = null;
		PreparedStatement pstmt       = null;
		ResultSet         rs          = null;
		PreparedStatement pstmt2      = null;
		ResultSet         rs2        = null;
		StringBuffer      strQuery    = new StringBuffer();
		ArrayList<HashMap<String, String>>  rsval = new ArrayList<HashMap<String, String>>();
		HashMap<String, String>			  rst		  = null;
		ConnectionContext connectionContext = new ConnectionResource();
		
		try {
			conn = connectionContext.getConnection();
			strQuery.setLength(0);
			strQuery.append("select a.cm_syscd,a.cm_sysmsg,a.cm_sysgb,a.cm_sysopen,			\n");
			strQuery.append("       a.cm_scmopen,a.cm_sysinfo,a.cm_basesys,  				\n");
			strQuery.append("       a.cm_dirbase,d.cm_codename as dirbname,  				\n");
			strQuery.append("       a.cm_stopst,a.cm_stoped,c.cm_sysmsg basesys,   			\n");
			strQuery.append("       a.cm_closedt,a.cm_prccnt,b.cm_codename,a.cm_prjname    	\n");
			strQuery.append("  from cmm0030 a,cmm0020 b,cmm0030 c,cmm0020 d        			\n");
			strQuery.append(" where b.cm_macode='SYSGB' and b.cm_micode=a.cm_sysgb 			\n");
			if (!clsSw) strQuery.append("and a.cm_closedt is null     	          			\n");
			if (SysCd != null && !"".equals(SysCd)) strQuery.append("and a.cm_syscd=?	   	\n");
			strQuery.append("   and nvl(a.cm_basesys,a.cm_syscd)=c.cm_syscd        			\n");
			strQuery.append("	and d.cm_macode='SERVERCD' and a.cm_dirbase = d.cm_micode	\n");
			strQuery.append("order by a.cm_syscd                                   			\n");
			//pstmt = new LoggableStatement(conn,strQuery.toString());
            pstmt = conn.prepareStatement(strQuery.toString());	
            if (SysCd != null && !"".equals(SysCd)) pstmt.setString(1, SysCd);
            ////ecamsLogger.error(((LoggableStatement)pstmt).getQueryString());
            rs = pstmt.executeQuery();
            
			while (rs.next()){
				rst = new HashMap<String, String>();
				rst.put("ID", Integer.toString(rs.getRow()));
				rst.put("cm_syscd", rs.getString("cm_syscd"));
				rst.put("cm_sysmsg", rs.getString("cm_sysmsg"));
				rst.put("cm_sysgb", rs.getString("cm_sysgb"));
				rst.put("sysgb", rs.getString("cm_codename"));
				rst.put("cm_sysinfo", rs.getString("cm_sysinfo"));
				rst.put("cm_scmopen", rs.getString("cm_scmopen"));
				rst.put("cm_sysopen", rs.getString("cm_sysopen"));
				rst.put("cm_basesys", rs.getString("cm_basesys"));
				rst.put("base", rs.getString("cm_dirbase"));
				rst.put("dirbname", rs.getString("dirbname"));
				rst.put("basesys", rs.getString("basesys"));
				rst.put("sysopen", rs.getString("cm_sysopen").substring(0,4)+"/"+
						           rs.getString("cm_sysopen").substring(4,6)+"/"+
						           rs.getString("cm_sysopen").substring(6,8));
				rst.put("scmopen", rs.getString("cm_scmopen").substring(0,4)+"/"+
				           rs.getString("cm_scmopen").substring(4,6)+"/"+
				           rs.getString("cm_scmopen").substring(6,8));
				rst.put("cm_prccnt", Integer.toString(rs.getInt("cm_prccnt")));
				if (rs.getString("cm_closedt") != null) rst.put("closeSw", "Y");
				else  rst.put("closeSw", "N");
				if (rs.getString("cm_sysinfo").substring(4,5).equals("1")) {
					rst.put("cm_stdate", rs.getString("cm_stopst"));
					rst.put("cm_eddate", rs.getString("cm_stoped"));
				}
				rst.put("cm_prjname", rs.getString("cm_prjname"));
				
				String strInfo = "";
				String micode = "";
				for(int j=0; j<rs.getString("cm_sysinfo").length(); j++){
					if (rs.getString("cm_sysinfo").substring(j,j+1).equals("1")){
						if(j+1 > 9){
							if(micode.equals("")) {
								micode = Integer.toString(j+1);
							}else {
								micode = micode+","+Integer.toString(j+1);
							}
							
						}else{
							if(micode.equals("")) {
								micode = "0"+Integer.toString(j+1);
							}else {
								micode = micode+","+"0"+Integer.toString(j+1);
							}
						}
					}
				}
				
				String[] micodes = micode.split(",");
				if(micodes.length == 0) {
					rst.put("sysinfo", "");
				}else {
					strQuery.setLength(0);
					strQuery.append("select cm_codename from cmm0020  \n");
					strQuery.append("where cm_macode = 'SYSINFO'  \n");
					strQuery.append("and cm_micode in ( \n");
					if (micodes.length == 1)
						strQuery.append(" ? ");
					else{
						for (int i=0;i<micodes.length;i++){
							if (i == micodes.length-1)
								strQuery.append(" ? ");
							else
								strQuery.append(" ? ,");
						}
					}
					strQuery.append(" ) \n");
		            pstmt2 = conn.prepareStatement(strQuery.toString());
		            for (int i=0;i<micodes.length;i++){
		            	pstmt2.setString(i+1, micodes[i]);
					}
		            rs2 = pstmt2.executeQuery();
		            
		            while (rs2.next()){
		            	if(strInfo.equals("")){
		            		strInfo = rs2.getString("cm_codename");
						}else{
							strInfo = strInfo+","+rs2.getString("cm_codename");
						}
		            }
		            rs2.close();
		            pstmt2.close();
				}
				
				rst.put("sysinfo", strInfo);
				rsval.add(rst);
				rst = null;
			}//end of while-loop statement
			
			rs.close();
			pstmt.close();
			conn.close();
			
			rs = null;
			rs2 = null;
			pstmt = null;
			pstmt2 = null;
			conn = null;
			
			return rsval.toArray();
			
		} catch (SQLException sqlexception) {
			sqlexception.printStackTrace();
			ecamsLogger.error("## Cmm0200.getSysInfo_List() SQLException START ##");
			ecamsLogger.error("## Error DESC : ", sqlexception);	
			ecamsLogger.error("## Cmm0200.getSysInfo_List() SQLException END ##");			
			throw sqlexception;
		} catch (Exception exception) {
			exception.printStackTrace();
			ecamsLogger.error("## Cmm0200.getSysInfo_List() Exception START ##");				
			ecamsLogger.error("## Error DESC : ", exception);	
			ecamsLogger.error("## Cmm0200.getSysInfo_List() Exception END ##");				
			throw exception;
		}finally{
			if (strQuery != null) 	strQuery = null;
			if (rsval != null)  rsval = null;
			if (rs != null)     try{rs.close();}catch (Exception ex){ex.printStackTrace();}
			if (pstmt != null)  try{pstmt.close();}catch (Exception ex2){ex2.printStackTrace();}
			if (rs2 != null)     try{rs2.close();}catch (Exception ex){ex.printStackTrace();}
			if (pstmt2 != null)  try{pstmt2.close();}catch (Exception ex2){ex2.printStackTrace();}
			if (conn != null){
				try{
					ConnectionResource.release(conn);
				}catch(Exception ex3){
					ecamsLogger.error("## Cmm0200.getSysInfo_List() connection release exception ##");
					ex3.printStackTrace();
				}
			}
		}
	}
    
    public Object[] getSvrInfo(String SysCd) throws SQLException, Exception {
    	Connection        conn        = null;
		PreparedStatement pstmt       = null;
		ResultSet         rs          = null;
		StringBuffer      strQuery    = new StringBuffer();
		ArrayList<HashMap<String, String>>  rsval = new ArrayList<HashMap<String, String>>();
		HashMap<String, String>			  rst		  = null;
		ConnectionContext connectionContext = new ConnectionResource();
		
		try {
			conn = connectionContext.getConnection();
			strQuery.append("select b.cm_micode,b.cm_codename                      \n");
			strQuery.append("  from cmm0031 a,cmm0020 b                            \n");
			strQuery.append(" where a.cm_syscd=? and a.cm_closedt is null          \n");
			strQuery.append("   and b.cm_macode='SERVERCD'                         \n");
			strQuery.append("   and b.cm_micode=a.cm_svrcd                         \n");
			strQuery.append("group by b.cm_micode,b.cm_codename                    \n");
			//pstmt = new LoggableStatement(conn,strQuery.toString());
            pstmt = conn.prepareStatement(strQuery.toString());	
            pstmt.setString(1, SysCd);
            ////ecamsLogger.error(((LoggableStatement)pstmt).getQueryString());
            rs = pstmt.executeQuery();

			while (rs.next()){
				rst = new HashMap<String, String>();
				rst.put("ID", Integer.toString(rs.getRow()));
				rst.put("cm_micode", rs.getString("cm_micode"));
				rst.put("cm_codename", rs.getString("cm_codename"));
				rsval.add(rst);
				rst = null;
			}//end of while-loop statement
			
			rs.close();
			pstmt.close();
			conn.close();
			
			rs = null;
			pstmt = null;
			conn = null;
			
			return rsval.toArray();
			
		} catch (SQLException sqlexception) {
			sqlexception.printStackTrace();
			ecamsLogger.error("## Cmm0200.getSvrInfo() SQLException START ##");
			ecamsLogger.error("## Error DESC : ", sqlexception);	
			ecamsLogger.error("## Cmm0200.getSvrInfo() SQLException END ##");			
			throw sqlexception;
		} catch (Exception exception) {
			exception.printStackTrace();
			ecamsLogger.error("## Cmm0200.getSvrInfo() Exception START ##");				
			ecamsLogger.error("## Error DESC : ", exception);	
			ecamsLogger.error("## Cmm0200.getSvrInfo() Exception END ##");				
			throw exception;
		}finally{
			if (strQuery != null) 	strQuery = null;
			if (rsval != null)  rsval = null;
			if (rs != null)     try{rs.close();}catch (Exception ex){ex.printStackTrace();}
			if (pstmt != null)  try{pstmt.close();}catch (Exception ex2){ex2.printStackTrace();}
			if (conn != null){
				try{
					ConnectionResource.release(conn);
				}catch(Exception ex3){
					ecamsLogger.error("## Cmm0200.getSvrInfo() connection release exception ##");
					ex3.printStackTrace();
				}
			}
		}
	}
    
    public Object[] getScriptList(String SysCd) throws SQLException, Exception {
    	Connection        conn        = null;
		PreparedStatement pstmt       = null;
		ResultSet         rs          = null;
		StringBuffer      strQuery    = new StringBuffer();
		ArrayList<HashMap<String, String>>  rsval = new ArrayList<HashMap<String, String>>();
		HashMap<String, String>			  rst		  = null;
		ConnectionContext connectionContext = new ConnectionResource();
		
		try {
			conn = connectionContext.getConnection();
			strQuery.setLength(0);
			strQuery.append("select cm_syscd, cm_compcd, cm_compscript,cm_gubun					   	\n");
			strQuery.append("from cmm0400          				           					\n");
			strQuery.append("where  cm_syscd = ?                   							\n");	
			strQuery.append("order by cm_compcd               								\n");
			//pstmt = new LoggableStatement(conn,strQuery.toString());
            pstmt = conn.prepareStatement(strQuery.toString());	
            pstmt.setString(1, SysCd);
            ////ecamsLogger.error(((LoggableStatement)pstmt).getQueryString());
            rs = pstmt.executeQuery();

			while (rs.next()){
				rst = new HashMap<String, String>();
				rst.put("ID", Integer.toString(rs.getRow()));
				rst.put("cm_syscd", rs.getString("cm_syscd"));
				rst.put("cm_compcd", rs.getString("cm_compcd"));
				rst.put("cm_compscript", rs.getString("cm_compscript"));
				rst.put("cm_gubun", rs.getString("cm_gubun"));
				rsval.add(rst);
				rst = null;
			}//end of while-loop statement
			
			rs.close();
			pstmt.close();
			conn.close();
			
			rs = null;
			pstmt = null;
			conn = null;
			
			return rsval.toArray();
			
		} catch (SQLException sqlexception) {
			sqlexception.printStackTrace();
			ecamsLogger.error("## Cmm0200.getSvrInfo() SQLException START ##");
			ecamsLogger.error("## Error DESC : ", sqlexception);	
			ecamsLogger.error("## Cmm0200.getSvrInfo() SQLException END ##");			
			throw sqlexception;
		} catch (Exception exception) {
			exception.printStackTrace();
			ecamsLogger.error("## Cmm0200.getSvrInfo() Exception START ##");				
			ecamsLogger.error("## Error DESC : ", exception);	
			ecamsLogger.error("## Cmm0200.getSvrInfo() Exception END ##");				
			throw exception;
		}finally{
			if (strQuery != null) 	strQuery = null;
			if (rsval != null)  rsval = null;
			if (rs != null)     try{rs.close();}catch (Exception ex){ex.printStackTrace();}
			if (pstmt != null)  try{pstmt.close();}catch (Exception ex2){ex2.printStackTrace();}
			if (conn != null){
				try{
					ConnectionResource.release(conn);
				}catch(Exception ex3){
					ecamsLogger.error("## Cmm0200.getSvrInfo() connection release exception ##");
					ex3.printStackTrace();
				}
			}
		}
	}
    
    public String factUpdt() throws SQLException, Exception {
    	Connection        conn        = null;
		PreparedStatement pstmt       = null;
		PreparedStatement pstmt2      = null;
		ResultSet         rs          = null;
		StringBuffer      strQuery    = new StringBuffer();
		
		ConnectionContext connectionContext = new ConnectionResource();		
		try {
			conn = connectionContext.getConnection();
			strQuery.append("select cm_macode,max(cm_micode) max                   \n");
			strQuery.append("  from cmm0020                                        \n");
			strQuery.append(" where cm_macode in ('SYSINFO','SVRINFO','RSCHKITEM') \n");
			strQuery.append("   and cm_closedt is null                             \n");
			strQuery.append("   and cm_micode<>'****'                              \n");
			strQuery.append(" group by cm_macode	                              \n");
			//pstmt = new LoggableStatement(conn,strQuery.toString());
            pstmt = conn.prepareStatement(strQuery.toString());	
            //ecamsLogger.error(((LoggableStatement)pstmt).getQueryString());
            rs = pstmt.executeQuery();
                        
			while (rs.next()){
				if (rs.getString("cm_macode").equals("SYSINFO")) {
					strQuery.setLength(0);
					strQuery.append("update cmm0030 set                            \n");
					strQuery.append("       cm_sysinfo=rpad(cm_sysinfo,?,'0')      \n");
					strQuery.append(" where length(cm_sysinfo)<?                   \n");
					pstmt2 = conn.prepareStatement(strQuery.toString());	
					pstmt2.setInt(1, Integer.parseInt(rs.getString("max")));	
					pstmt2.setInt(2, Integer.parseInt(rs.getString("max")));
					pstmt2.executeUpdate();
					pstmt2.close();
				} else if (rs.getString("cm_macode").equals("SVRINFO")) {
					strQuery.setLength(0);
					strQuery.append("update cmm0031 set                            \n");
					strQuery.append("       cm_svruse=rpad(cm_svruse,?,'0')        \n");
					strQuery.append(" where length(cm_svruse)<?                    \n");
					pstmt2 = conn.prepareStatement(strQuery.toString());	
					pstmt2.setInt(1, Integer.parseInt(rs.getString("max")));	
					pstmt2.setInt(2, Integer.parseInt(rs.getString("max")));
					pstmt2.executeUpdate();
					pstmt2.close();
				} else if (rs.getString("cm_macode").equals("RSCHKITEM")) {
					strQuery.setLength(0);
					strQuery.append("update cmm0036 set                            \n");
					strQuery.append("       cm_info=rpad(cm_info,?,'0')            \n");
					strQuery.append(" where length(cm_info)<?                      \n");
					pstmt2 = conn.prepareStatement(strQuery.toString());	
					pstmt2.setInt(1, Integer.parseInt(rs.getString("max")));	
					pstmt2.setInt(2, Integer.parseInt(rs.getString("max")));
					pstmt2.executeUpdate();
					pstmt2.close();
				}
			}//end of while-loop statement
			
			rs.close();
			pstmt.close();
			conn.close();
			
			rs= null;
			pstmt = null;
			conn = null;
			
			return "OK";
			
		} catch (SQLException sqlexception) {
			sqlexception.printStackTrace();
			ecamsLogger.error("## Cmm0200.factUpdt() SQLException START ##");
			ecamsLogger.error("## Error DESC : ", sqlexception);	
			ecamsLogger.error("## Cmm0200.factUpdt() SQLException END ##");			
			throw sqlexception;
		} catch (Exception exception) {
			exception.printStackTrace();
			ecamsLogger.error("## Cmm0200.factUpdt() Exception START ##");				
			ecamsLogger.error("## Error DESC : ", exception);	
			ecamsLogger.error("## Cmm0200.factUpdt() Exception END ##");				
			throw exception;
		}finally{
			if (strQuery != null) 	strQuery = null;
			if (rs != null)     try{rs.close();}catch (Exception ex){ex.printStackTrace();}
			if (pstmt != null)  try{pstmt.close();}catch (Exception ex2){ex2.printStackTrace();}
			if (conn != null){
				try{
					ConnectionResource.release(conn);
				}catch(Exception ex3){
					ecamsLogger.error("## Cmm0200.factUpdt() connection release exception ##");
					ex3.printStackTrace();
				}
			}
		}
		
	}
    public String sysInfo_Updt(HashMap<String,String> etcData) throws SQLException, Exception {
    	Connection        conn        = null;
		PreparedStatement pstmt       = null;
		PreparedStatement pstmt2      = null;
		ResultSet         rs          = null;
		StringBuffer      strQuery    = new StringBuffer();
		boolean           isrtSw      = false;
		String            strSysCd    = "";
		int               parmCnt     = 0;
		
		ConnectionContext connectionContext = new ConnectionResource();		
		try {
			conn = connectionContext.getConnection();
			if (etcData.get("cm_syscd") != null && etcData.get("cm_syscd") != "") {				
				strQuery.append("select count(*) cnt from cmm0030      \n");
				strQuery.append(" where cm_syscd=?                     \n");
				//pstmt = new LoggableStatement(conn,strQuery.toString());
	            pstmt = conn.prepareStatement(strQuery.toString());	
	            pstmt.setString(1, etcData.get("cm_syscd"));
	            ////ecamsLogger.error(((LoggableStatement)pstmt).getQueryString());
	            rs = pstmt.executeQuery();
	                        
				if (rs.next()){
					if (rs.getInt("cnt") == 0) isrtSw = true;
					else isrtSw = false;
				}		
				rs.close();
				pstmt.close();
				strSysCd = etcData.get("cm_syscd");
			} else {				
				strQuery.append("select lpad(nvl(max(cm_syscd),'00001') + 1,5,'0') max \n");			
				strQuery.append("  from cmm0030                                        \n");			
				strQuery.append(" where substr(cm_sysinfo,1,1)='0'                     \n");
				//pstmt = new LoggableStatement(conn,strQuery.toString());
	            pstmt = conn.prepareStatement(strQuery.toString());	
	            ////ecamsLogger.error(((LoggableStatement)pstmt).getQueryString());
	            rs = pstmt.executeQuery();
	                        
				if (rs.next()){
					strSysCd = rs.getString("max");
				}		
				rs.close();
				pstmt.close();				
			}	
			if (isrtSw == true) {
				strQuery.setLength(0);
				strQuery.append("insert into cmm0030                                      \n");
				strQuery.append("  (CM_SYSCD,CM_SYSMSG,CM_SYSGB,CM_CREATDT,CM_LASTUPDT,   \n");
				strQuery.append("   CM_ONLINE,CM_SYSFC1,CM_SYSFC2,CM_DIRBASE,CM_SYSINFO,  \n");
				strQuery.append("   CM_PRCCNT,CM_STOPST,CM_STOPED,CM_SYSTIME,             \n");
				strQuery.append("   CM_SYSOPEN,CM_SCMOPEN,CM_BASESYS, cm_prjname) values              \n");
				strQuery.append("(?, ?, ?, SYSDATE, SYSDATE, 'N', 'CD', 'DC', ?,       \n");
				strQuery.append("   ?, ?, ?, ?, ?, ?, ?, ?, ?)                               \n");
				pstmt = conn.prepareStatement(strQuery.toString());
				pstmt.setString(++parmCnt, strSysCd);
				pstmt.setString(++parmCnt, etcData.get("cm_sysmsg"));
				pstmt.setString(++parmCnt, etcData.get("cm_sysgb"));
				pstmt.setString(++parmCnt, etcData.get("base"));
				pstmt.setString(++parmCnt, etcData.get("cm_sysinfo"));
				pstmt.setInt(++parmCnt, Integer.parseInt(etcData.get("cm_prccnt")));
				pstmt.setString(++parmCnt, etcData.get("cm_stdate"));
				pstmt.setString(++parmCnt, etcData.get("cm_eddate"));
				if (etcData.get("cm_sysinfo").substring(5,6).equals("1"))
					pstmt.setString(++parmCnt, "0300");
				else
				    pstmt.setString(++parmCnt, "");
				pstmt.setString(++parmCnt, etcData.get("sysopen"));
				pstmt.setString(++parmCnt, etcData.get("scmopen"));
				if (etcData.get("basesys") == null || etcData.get("basesys") == "") {
					pstmt.setString(++parmCnt, strSysCd);
				} else {
					pstmt.setString(++parmCnt, etcData.get("basesys"));
				}
				pstmt.setString(++parmCnt, etcData.get("cm_prjname"));
			} else {
				strQuery.setLength(0);
				strQuery.append("update cmm0030 set cm_sysmsg=?,cm_closedt='',            \n");
				strQuery.append("   CM_SYSGB=?,CM_LASTUPDT=SYSDATE,CM_SYSINFO=?,CM_DIRBASE=?,\n");
				strQuery.append("   CM_PRCCNT=?,CM_STOPST=?,CM_STOPED=?,CM_SYSTIME=?,     \n");
				strQuery.append("   CM_SYSOPEN=?,CM_SCMOPEN=?,CM_BASESYS=?,cm_prjname=?   \n");
				strQuery.append("where cm_syscd=?                                         \n");
				pstmt = conn.prepareStatement(strQuery.toString());
				//pstmt = new LoggableStatement(conn,strQuery.toString());
				pstmt.setString(++parmCnt, etcData.get("cm_sysmsg"));
				pstmt.setString(++parmCnt, etcData.get("cm_sysgb"));
				pstmt.setString(++parmCnt, etcData.get("cm_sysinfo"));
				pstmt.setString(++parmCnt, etcData.get("base"));
				pstmt.setInt(++parmCnt, Integer.parseInt(etcData.get("cm_prccnt")));
				pstmt.setString(++parmCnt, etcData.get("cm_stdate"));
				pstmt.setString(++parmCnt, etcData.get("cm_eddate"));
				if (etcData.get("cm_sysinfo").substring(5,6).equals("1"))
					pstmt.setString(++parmCnt, "0300");
				else
				    pstmt.setString(++parmCnt, "");
				pstmt.setString(++parmCnt, etcData.get("sysopen"));
				pstmt.setString(++parmCnt, etcData.get("scmopen"));
				pstmt.setString(++parmCnt, etcData.get("basesys"));
				pstmt.setString(++parmCnt, etcData.get("cm_prjname"));
				pstmt.setString(++parmCnt, strSysCd);
			}
			////ecamsLogger.error(((LoggableStatement)pstmt).getQueryString());
			pstmt.executeUpdate();
			pstmt.close();
			
			strQuery.setLength(0);
			strQuery.append("update cmm0030 set cm_prccnt=?                  \n");
			strQuery.append(" where cm_sysgb=?                               \n");
			pstmt = conn.prepareStatement(strQuery.toString());
			pstmt.setInt(1, Integer.parseInt(etcData.get("cm_prccnt")));
			pstmt.setString(2, etcData.get("cm_sysgb"));
			pstmt.executeUpdate();
			pstmt.close();

			parmCnt = 0;
			if (etcData.get("cm_jobcd") != "" && etcData.get("cm_jobcd") != null) {
				strQuery.setLength(0);
				strQuery.append("update cmm0034 set cm_closedt=SYSDATE                    \n");
				strQuery.append("where cm_syscd=?                                         \n");
				pstmt = conn.prepareStatement(strQuery.toString());
				pstmt.setString(++parmCnt, strSysCd);
				pstmt.executeUpdate();
				pstmt.close();
				
				String[] strJob = etcData.get("cm_jobcd").split(",");
				int      i = 0;
				
				for (i=0;strJob.length>i;i++) {
					strQuery.setLength(0);
					parmCnt = 0;
					strQuery.append("select count(*) cnt from cmm0034         \n");
					strQuery.append(" where cm_syscd=? and cm_jobcd=?         \n");
					pstmt = conn.prepareStatement(strQuery.toString());
					pstmt.setString(++parmCnt, strSysCd);
					pstmt.setString(++parmCnt, strJob[i]);
					rs = pstmt.executeQuery();
					if (rs.next()) {
						if (rs.getInt("cnt") == 0) isrtSw = true;
						else isrtSw = false;
					}
					pstmt.close();
					
					strQuery.setLength(0);
					parmCnt = 0;
					if (isrtSw == true) {						
						strQuery.append("insert into cmm0034                                 \n");
						strQuery.append(" (cm_syscd,cm_jobcd,cm_creatdt,cm_lastdt,cm_editor) \n");
						strQuery.append(" values (?, ?, SYSDATE, SYSDATE, ?)                 \n");
						pstmt = conn.prepareStatement(strQuery.toString());
						pstmt.setString(++parmCnt, strSysCd);
						pstmt.setString(++parmCnt, strJob[i]);
						pstmt.setString(++parmCnt, etcData.get("cm_editor"));
					} else {						
						strQuery.append("update cmm0034 set cm_lastdt=SYSDATE,cm_editor=?,   \n");
						strQuery.append("                   cm_closedt=''                    \n");
						strQuery.append(" where cm_syscd=? and cm_jobcd=?                    \n");
						pstmt = conn.prepareStatement(strQuery.toString());
						pstmt.setString(++parmCnt, etcData.get("cm_editor"));
						pstmt.setString(++parmCnt, strSysCd);
						pstmt.setString(++parmCnt, strJob[i]);
					}
					pstmt.executeUpdate();
					pstmt.close();
				}
			}
			rs.close(); //수정
			conn.close(); //수정
			
			rs = null; //수정
			pstmt = null; //수정
			conn = null; //수정
			
			return strSysCd;
			
		} catch (SQLException sqlexception) {
			sqlexception.printStackTrace();
			ecamsLogger.error("## Cmm0200.sysInfo_Updt() SQLException START ##");
			ecamsLogger.error("## Error DESC : ", sqlexception);	
			ecamsLogger.error("## Cmm0200.sysInfo_Updt() SQLException END ##");			
			throw sqlexception;
		} catch (Exception exception) {
			exception.printStackTrace();
			ecamsLogger.error("## Cmm0200.sysInfo_Updt() Exception START ##");				
			ecamsLogger.error("## Error DESC : ", exception);	
			ecamsLogger.error("## Cmm0200.sysInfo_Updt() Exception END ##");				
			throw exception;
		}finally{
			if (strQuery != null) 	strQuery = null;
			if (rs != null)     try{rs.close();}catch (Exception ex){ex.printStackTrace();}
			if (pstmt != null)  try{pstmt.close();}catch (Exception ex2){ex2.printStackTrace();}
			if (conn != null){
				try{
					ConnectionResource.release(conn);
				}catch(Exception ex3){
					ecamsLogger.error("## Cmm0200.sysInfo_Updt() connection release exception ##");
					ex3.printStackTrace();
				}
			}
		}
		
	}

    public String sysInfo_Close(String SysCd,String UserId) throws SQLException, Exception {
    	Connection        conn        = null;
		PreparedStatement pstmt       = null;
		StringBuffer      strQuery    = new StringBuffer();
		
		ConnectionContext connectionContext = new ConnectionResource();		
		try {
			conn = connectionContext.getConnection();
			strQuery.setLength(0);
			strQuery.append("update cmm0030 set cm_closedt=SYSDATE,          \n");
			strQuery.append("   cm_lastupdt=SYSDATE                          \n");
			strQuery.append("where cm_syscd=?                                \n");
			pstmt = conn.prepareStatement(strQuery.toString());
			pstmt.setString(1, SysCd);
			pstmt.executeUpdate();
			pstmt.close();
			
			conn.close(); //수정
			
			pstmt = null; //수정
			conn = null; //수정
			
			return "OK";
			
		} catch (SQLException sqlexception) {
			sqlexception.printStackTrace();
			ecamsLogger.error("## Cmm0200.sysInfo_Close() SQLException START ##");
			ecamsLogger.error("## Error DESC : ", sqlexception);	
			ecamsLogger.error("## Cmm0200.sysInfo_Close() SQLException END ##");			
			throw sqlexception;
		} catch (Exception exception) {
			exception.printStackTrace();
			ecamsLogger.error("## Cmm0200.sysInfo_Close() Exception START ##");				
			ecamsLogger.error("## Error DESC : ", exception);	
			ecamsLogger.error("## Cmm0200.sysInfo_Close() Exception END ##");				
			throw exception;
		}finally{
			if (strQuery != null) 	strQuery = null;
			if (pstmt != null)  try{pstmt.close();}catch (Exception ex2){ex2.printStackTrace();}
			if (conn != null){
				try{
					ConnectionResource.release(conn);
				}catch(Exception ex3){
					ecamsLogger.error("## Cmm0200.sysInfo_Close() connection release exception ##");
					ex3.printStackTrace();
				}
			}
		}
		
	}//end of sysInfo_Close
    
    public boolean delScriptInfo(String SysCd, String CompCd, String gubun) throws SQLException, Exception{
    	Connection			conn		= null;
    	PreparedStatement	pstmt		= null;
		StringBuffer      	strQuery    = new StringBuffer();
		
		ConnectionContext connectionContext = new ConnectionResource();
		
		try{
			conn = connectionContext.getConnection();
			conn.setAutoCommit(false);
			
			strQuery.setLength(0);
			strQuery.append("delete cmm0400 ");
		    strQuery.append(" where cm_syscd = ? ");
		    strQuery.append("   and cm_compcd = ? ");
		    strQuery.append("   and cm_gubun = ? "); //2022.09.13 보완 (01:compile스크립트, 02:make스크립트)
            //pstmt = conn.prepareStatement(strQuery.toString());
            pstmt = new LoggableStatement(conn, strQuery.toString());
            pstmt.setString(1, SysCd);
            pstmt.setString(2, CompCd);
            pstmt.setString(3, gubun);
            ecamsLogger.error(((LoggableStatement)pstmt).getQueryString());
            pstmt.executeUpdate();
            pstmt.close();
			
		    conn.commit();
            conn.close();
            strQuery = null;
            pstmt = null;
            conn = null;
            
			return true;
			
		} catch (SQLException sqlexception) {
			sqlexception.printStackTrace();
			conn.rollback();
			ecamsLogger.error("## Cmm0100.delJobInfo() SQLException START ##");
			ecamsLogger.error("## Error DESC : ", sqlexception);
			ecamsLogger.error("## Cmm0100.delJobInfo() SQLException END ##");
			throw sqlexception;
		} catch (Exception exception) {
			exception.printStackTrace();
			conn.rollback();
			ecamsLogger.error("## Cmm0100.delJobInfo() Exception START ##");
			ecamsLogger.error("## Error DESC : ", exception);
			ecamsLogger.error("## Cmm0100.delJobInfo() Exception END ##");
			throw exception;
		}finally{
			if (strQuery != null) 	strQuery = null;
			if (pstmt != null)  try{pstmt.close();}catch (Exception ex2){ex2.printStackTrace();}
			if (conn != null){
				try{
					ConnectionResource.release(conn);
				}catch(Exception ex3){
					ecamsLogger.error("## Cmm0100.delJobInfo() connection release exception ##");
					ex3.printStackTrace();
				}
			}
		}
    }
    
	public boolean setScriptcode(HashMap<String,String> Data) throws SQLException, Exception{
    	Connection			conn		= null;
    	PreparedStatement	pstmt		= null;
    	ResultSet         	rs          = null;
		StringBuffer      	strQuery    = new StringBuffer();
		String 				maxnum		="";
		boolean				flag 		= false;
		ConnectionContext connectionContext = new ConnectionResource();
		
		try{
			conn = connectionContext.getConnection();
			conn.setAutoCommit(false);
			
			if(!"".equals(Data.get("code")) && Data.get("code") != null){
				strQuery.setLength(0);
			    strQuery.append("delete cmm0400 ");
			    strQuery.append("where cm_syscd = ? ");
			    strQuery.append("and cm_compcd = ? ");
	            pstmt = conn.prepareStatement(strQuery.toString());
	            pstmt.setString(1, Data.get("syscd"));
	            pstmt.setString(2, Data.get("code"));
	            pstmt.executeUpdate();
	            pstmt.close();
			}else{
				strQuery.setLength(0);                		
	    		strQuery.append("select NVL(lpad((to_number(max(cm_compcd)+ 1)),2,0),'00') as max	 	\n");               		
	    		strQuery.append("  from cmm0400 														\n"); 
	    		strQuery.append(" where cm_syscd=? and cm_gubun = ?						 				\n"); 
	    		pstmt = conn.prepareStatement(strQuery.toString());	
	    		pstmt.setString(1, Data.get("syscd"));
		        pstmt.setString(2, Data.get("gubun"));
	    		rs = pstmt.executeQuery();
		        if (rs.next()) {
		        	maxnum = rs.getString("max");
		        	flag = true;
		        }
		        rs.close();
		        pstmt.close();
			}
			
            strQuery.setLength(0);
            strQuery.append("insert into cmm0400 (cm_syscd,cm_compcd,cm_compscript,cm_gubun) ");
            strQuery.append("values (?,?,?,?) ");
            pstmt = conn.prepareStatement(strQuery.toString());
            pstmt.setString(1, Data.get("syscd"));
            if(flag)
            	pstmt.setString(2,	maxnum);
            else
            	pstmt.setString(2, Data.get("code"));
            pstmt.setString(3, Data.get("value"));
            pstmt.setString(4, Data.get("gubun"));
            pstmt.executeUpdate();
            pstmt.close();
            
		    conn.commit();
            conn.close();
            strQuery = null;
            pstmt = null;
            conn = null;
            
			return true;
			
		} catch (SQLException sqlexception) {
			sqlexception.printStackTrace();
			conn.rollback();
			ecamsLogger.error("## Cmm0100.setJobInfo_individual() SQLException START ##");
			ecamsLogger.error("## Error DESC : ", sqlexception);
			ecamsLogger.error("## Cmm0100.setJobInfo_individual() SQLException END ##");
			throw sqlexception;
		} catch (Exception exception) {
			exception.printStackTrace();
			conn.rollback();
			ecamsLogger.error("## Cmm0100.setJobInfo_individual() Exception START ##");
			ecamsLogger.error("## Error DESC : ", exception);
			ecamsLogger.error("## Cmm0100.setJobInfo_individual() Exception END ##");
			throw exception;
		}finally{
			if (strQuery != null) 	strQuery = null;
			if (pstmt != null)  try{pstmt.close();}catch (Exception ex2){ex2.printStackTrace();}
			if (conn != null){
				try{
					ConnectionResource.release(conn);
				}catch(Exception ex3){
					ecamsLogger.error("## Cmm0100.setJobInfo_individual() connection release exception ##");
					ex3.printStackTrace();
				}
			}
		}
    }
	
	
	
	
	public String cmd0200_Excption(ArrayList<HashMap<String, String>>  getCmd0200List) throws SQLException, Exception {
		//플렉스에서 받아온 arraylist 값
		Connection        conn        = null;	
		PreparedStatement pstmt       = null;
		ResultSet         rs          = null;
		StringBuffer      strQuery    = new StringBuffer();
		
		String            retMsg      = "0";
		int 		      parmCnt     = 0;
		String            strGrpCd    = "";
		String            prjnmck     = "";
		ConnectionContext connectionContext = new ConnectionResource();
		try {
		
		conn = connectionContext.getConnection();
		conn.setAutoCommit(false);
		
			for(int i = 0; i<getCmd0200List.size();i++){
				parmCnt = 0;
				strQuery.setLength(0);
				strQuery.append("update cmm0034 set CM_PRJNMCK = ? 		 \n");
				strQuery.append("where cm_syscd = ? 				     \n");
				strQuery.append("and cm_jobcd = ? 				         \n");
				
				pstmt = conn.prepareStatement(strQuery.toString());
		//		pstmt = new LoggableStatement(conn,strQuery.toString());
				prjnmck = getCmd0200List.get(i).get("prjnmck");
				
				if(prjnmck.equals("true")){
					pstmt.setString(++parmCnt, "1");
				}else if(prjnmck.equals("false")){
					pstmt.setString(++parmCnt, "0");
				}
				pstmt.setString(++parmCnt, getCmd0200List.get(i).get("syscd"));
				pstmt.setString(++parmCnt, getCmd0200List.get(i).get("jobcd"));
				
		//		ecamsLogger.error(((LoggableStatement)pstmt).getQueryString());
				pstmt.executeUpdate();
				pstmt.close();
				conn.commit();
			}
			conn.commit();
			conn.close();
			conn = null;
			rs = null;
			pstmt = null;
			return retMsg;
			
			} catch (SQLException sqlexception) {
				sqlexception.printStackTrace();
				ecamsLogger.error("## cmd0200.cmd0200_Excption() SQLException START ##");
				ecamsLogger.error("## Error DESC : ", sqlexception);	
				ecamsLogger.error("## cmd0200.cmd0200_Excption() SQLException END ##");			
			throw sqlexception;
			} catch (Exception exception) {
				exception.printStackTrace();
				ecamsLogger.error("## cmd0200.cmd0200_Excption() Exception START ##");				
				ecamsLogger.error("## Error DESC : ", exception);	
				ecamsLogger.error("## cmd0200.cmd0200_Excption() Exception END ##");				
				throw exception;
			}finally{
				if (strQuery != null)	strQuery = null;
				if (rs != null)     try{rs.close();}catch (Exception ex){ex.printStackTrace();}
				if (pstmt != null)  try{pstmt.close();}catch (Exception ex2){ex2.printStackTrace();}
				if (conn != null){
				try{
				ConnectionResource.release(conn);
				}catch(Exception ex3){
					ecamsLogger.error("## cmd0200.cmd0200_Excption() connection release exception ##");
					ex3.printStackTrace();
				}
			}
		}		
	}//end of cmd0200_Excption() method statement
	public Object[] getSysJobInfo(String SysCd) throws SQLException, Exception {
		Connection        conn        = null;
		PreparedStatement pstmt       = null;
		ResultSet         rs          = null;
		StringBuffer      strQuery    = new StringBuffer();

		ArrayList<HashMap<String, String>>  rsval = new ArrayList<HashMap<String, String>>();
		HashMap<String, String>			  rst		  = null;	
		Object[]		  rtObj		  = null;
	    		
		ConnectionContext connectionContext = new ConnectionResource();		
		
		try {
			conn = connectionContext.getConnection();
			
			strQuery.setLength(0);
			strQuery.append("select a.cm_jobcd,a.cm_prjnmck,b.cm_jobname,           \n");
			strQuery.append("       a.cm_prjnm,a.cm_prjid,a.cm_branch,a.cm_prjhome	\n");
			strQuery.append("  from cmm0034 a,cmm0102 b  				            \n");
			strQuery.append(" where a.cm_syscd=?                     	            \n");
			strQuery.append("   and a.cm_closedt is null  	                        \n");
			strQuery.append("   and a.cm_jobcd=b.cm_jobcd 	                        \n");
			strQuery.append(" order by a.cm_jobcd 	                                \n");
			pstmt = conn.prepareStatement(strQuery.toString());
            //pstmt = new LoggableStatement(conn,strQuery.toString());
            
            pstmt.setString(1, SysCd);
            
            //ecamsLogger.error(((LoggableStatement)pstmt).getQueryString());           
            rs = pstmt.executeQuery();
            rsval.clear();
            
			while (rs.next()){
				rst = new HashMap<String, String>();
				rst.put("cm_jobcd", rs.getString("cm_jobcd"));
				rst.put("cm_jobname", rs.getString("cm_jobname"));
				rst.put("cm_prjnmck", rs.getString("cm_prjnmck"));
				rst.put("cm_prjname", rs.getString("cm_prjnm"));
				rst.put("cm_prjid", rs.getString("cm_prjid"));
				rst.put("cm_branch", rs.getString("cm_branch"));
				rst.put("cm_prjhome", rs.getString("cm_prjhome"));
				rsval.add(rst);
				rst = null;
			}//end of while-loop statement
			rs.close();
			pstmt.close();
			conn.close();
			rs = null;
			pstmt = null;
			conn = null;
			
			rtObj =  rsval.toArray();
			rsval.clear();
			rsval = null;
			
			return rtObj;
			
		} catch (SQLException sqlexception) {
			sqlexception.printStackTrace();
			ecamsLogger.error("## Cmm0200.getSysJobInfo() SQLException START ##");
			ecamsLogger.error("## Error DESC : ", sqlexception);
			ecamsLogger.error("## Cmm0200.getSysJobInfo() SQLException END ##");
			throw sqlexception;
		} catch (Exception exception) {
			exception.printStackTrace();
			ecamsLogger.error("## Cmm0200.getSysJobInfo() Exception START ##");
			ecamsLogger.error("## Error DESC : ", exception);
			ecamsLogger.error("## Cmm0200.getSysJobInfo() Exception END ##");
			throw exception;
		}finally{
			if (strQuery != null) 	strQuery = null;
			if (rtObj != null)	rtObj = null;
			if (rs != null)     try{rs.close();}catch (Exception ex){ex.printStackTrace();}
			if (pstmt != null)  try{pstmt.close();}catch (Exception ex2){ex2.printStackTrace();}
			if (conn != null){
				try{
					ConnectionResource.release(conn);
				}catch(Exception ex3){
					ecamsLogger.error("## Cmm0200.getSysJobInfo() connection release exception ##");
					ex3.printStackTrace();
				}
			}
		}
	}//end of getSysJobInfo() method statement
	public String setGitJobInfo(HashMap<String, String> gitData) throws SQLException, Exception {
		Connection        conn        = null;	
		PreparedStatement pstmt       = null;
		ResultSet         rs          = null;
		StringBuffer      strQuery    = new StringBuffer();
		
		int 		      parmCnt     = 0;
		ConnectionContext connectionContext = new ConnectionResource();
		try {
		
			conn = connectionContext.getConnection();
			conn.setAutoCommit(false);
			parmCnt = 0;
			strQuery.setLength(0);
			strQuery.append("update cmm0034 		 \n");
			strQuery.append("   set cm_prjnm=?       \n");
			strQuery.append("      ,cm_prjid=?       \n");
			strQuery.append("      ,cm_branch=?      \n");
			strQuery.append("      ,cm_prjhome=?     \n");
			strQuery.append(" where cm_syscd=? 		 \n");
			strQuery.append("   and cm_jobcd=? 		 \n");			
			pstmt = conn.prepareStatement(strQuery.toString());
	//		pstmt = new LoggableStatement(conn,strQuery.toString());
			pstmt.setString(++parmCnt, gitData.get("prjname"));
			pstmt.setString(++parmCnt, gitData.get("prjid"));
			pstmt.setString(++parmCnt, gitData.get("branch"));
			pstmt.setString(++parmCnt, gitData.get("prjhome"));
			pstmt.setString(++parmCnt, gitData.get("syscd"));
			pstmt.setString(++parmCnt, gitData.get("jobcd"));			
	//		ecamsLogger.error(((LoggableStatement)pstmt).getQueryString());
			pstmt.executeUpdate();
			pstmt.close();
			conn.commit();
			
			conn.close();
			conn = null;
			rs = null;
			pstmt = null;
			
			return "OK";
			
			} catch (SQLException sqlexception) {
				sqlexception.printStackTrace();
				ecamsLogger.error("## cmm0200.setGitJobInfo() SQLException START ##");
				ecamsLogger.error("## Error DESC : ", sqlexception);	
				ecamsLogger.error("## cmm0200.setGitJobInfo() SQLException END ##");			
			throw sqlexception;
			} catch (Exception exception) {
				exception.printStackTrace();
				ecamsLogger.error("## cmm0200.setGitJobInfo() Exception START ##");				
				ecamsLogger.error("## Error DESC : ", exception);	
				ecamsLogger.error("## cmm0200.setGitJobInfo() Exception END ##");				
				throw exception;
			}finally{
				if (strQuery != null)	strQuery = null;
				if (rs != null)     try{rs.close();}catch (Exception ex){ex.printStackTrace();}
				if (pstmt != null)  try{pstmt.close();}catch (Exception ex2){ex2.printStackTrace();}
				if (conn != null){
				try{
				ConnectionResource.release(conn);
				}catch(Exception ex3){
					ecamsLogger.error("## cmm0200.setGitJobInfo() connection release exception ##");
					ex3.printStackTrace();
				}
			}
		}		
	}//end of setGitJobInfo() method statement
		
}//end of Cmm0200 class statement
