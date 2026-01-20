
/*****************************************************************************************
	1. program ID	: eCmd3100.java
	2. create date	: 2008.07. 10
	3. auth		    : YOYO.JJANG
	4. update date	: 
	5. auth		    : 
	6. description	: eCmd3100
*****************************************************************************************/

package app.eCmd;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.logging.log4j.Logger;
import app.common.LoggableStatement;
import com.ecams.common.dbconn.ConnectionContext;
import com.ecams.common.dbconn.ConnectionResource;
import com.ecams.common.logger.EcamsLogger;



/**
 * @author bigeyes
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */


public class Cmd1200{
    /**
     * Logger Class Instance Creation
     * logger
     */
    Logger ecamsLogger  = EcamsLogger.getLoggerInstance();
    
	/**
	 * project 殿废
	 * @param  pPrjno,pDocname,pDocPath
	 * @return String
	 * @throws SQLException
	 * @throws Exception
	 */
	
    public Object[] getBldCd() throws SQLException, Exception {
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
			
			String svBldGbn = "";
				
			strQuery.setLength(0);
			//strQuery.append("select b.cm_gbncd, b.cm_bldcd, a.cm_codename 				\n");
			strQuery.append("select b.cm_gbncd, b.cm_bldcd, b.cm_bldname    			\n");			
			strQuery.append("  from cmm0022 b			 								\n");
			//strQuery.append(" where b.cm_bldcd = a.cm_micode and a.cm_macode = 'BLDCD' 	\n");
			strQuery.append(" Group by b.cm_gbncd, b.cm_bldcd, b.cm_bldname 		   \n");
			strQuery.append(" order by b.cm_gbncd, b.cm_bldcd, b.cm_bldname            \n");
		
	        pstmt = conn.prepareStatement(strQuery.toString());
            rs = pstmt.executeQuery();

			while (rs.next()){
				if (svBldGbn.length() == 0 || !svBldGbn.equals(rs.getString("cm_gbncd"))) {
					rst = new HashMap<String, String>();
					rst.put("cm_micode", "00");
					rst.put("cm_bldgbn", rs.getString("cm_gbncd"));
					rst.put("cm_codename", "蜡屈脚痹殿废");
					rsval.add(rst);
					rst = null;
					svBldGbn = rs.getString("cm_gbncd");
				}
				rst = new HashMap<String, String>();
				rst.put("cm_bldgbn", rs.getString("cm_gbncd"));
				rst.put("cm_micode", rs.getString("cm_bldcd"));
				rst.put("cm_codename", rs.getString("cm_bldname"));
				rsval.add(rst);
				rst = null;
			}
			
			rs.close();
			pstmt.close();
			conn.close();
			rs = null;
			pstmt = null;
			conn = null;

			rtObj = rsval.toArray();
			rsval = null;
			return rtObj;
			
		} catch (SQLException sqlexception) {
			sqlexception.printStackTrace();
			ecamsLogger.error("## Cnd1200.getCbo_BldCd0() SQLException START ##");
			ecamsLogger.error("## Error DESC : ", sqlexception);	
			ecamsLogger.error("## Cnd1200.getCbo_BldCd0() SQLException END ##");			
			throw sqlexception;
		} catch (Exception exception) {
			exception.printStackTrace();
			ecamsLogger.error("## Cnd1200.getCbo_BldCd0() Exception START ##");				
			ecamsLogger.error("## Error DESC : ", exception);	
			ecamsLogger.error("## Cnd1200.getCbo_BldCd0() Exception END ##");				
			throw exception;
		}finally{
			if (strQuery != null) 	strQuery = null;
			if (rtObj != null)	rtObj = null;
			if (rs != null)     try{rs.close();}catch (Exception ex){ex.printStackTrace();}
			if (pstmt != null)  try{pstmt.close();}catch (Exception ex2){ex2.printStackTrace();}
			if (conn != null){
				try{
					conn.close();
					conn = null;
				}catch(Exception ex3){
					ecamsLogger.error("## Cnd1200.getCbo_BldCd0() connection release exception ##");
					ex3.printStackTrace();
				}
			}
		}

	}//end of getCbo_BldCd0() method statement
    
    public Object[] getScript(String Cbo_BldGbn_code,String Cbo_BldCd0_code) throws SQLException, Exception {
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
			strQuery.append("select cm_seq,cm_cmdname,cm_errword,NVL(cm_runtype,'R') cm_runtype  \n");
			strQuery.append("  from cmm0022  \n");
			strQuery.append("where cm_gbncd=?  \n");//Cbo_BldGbn_code
			strQuery.append("  and cm_bldcd=? \n");//Cbo_BldCd0_code
			strQuery.append("order by cm_seq \n");
            pstmt = conn.prepareStatement(strQuery.toString());
		    //pstmt =  new LoggableStatement(conn, strQuery.toString());
            pstmt.setString(1, Cbo_BldGbn_code);
            pstmt.setString(2, Cbo_BldCd0_code);
            ////ecamsLogger.error(((LoggableStatement)pstmt).getQueryString());
            rs = pstmt.executeQuery();
			while (rs.next()){
				rst = new HashMap<String, String>();
				rst.put("ID", "Lv_File0");
				rst.put("cm_seq", rs.getString("cm_seq"));
				rst.put("cm_cmdname", rs.getString("cm_cmdname"));
				rst.put("cm_errword", rs.getString("cm_errword"));
				rst.put("cm_runtype", rs.getString("cm_runtype"));
	           	rsval.add(rst);
	           	rst = null;
			}
			rs.close();
			pstmt.close();
			conn.close();
			rs = null;
			pstmt = null;
			conn = null;
			rtObj = rsval.toArray();
			rsval = null;
			return rtObj;
			
		} catch (SQLException sqlexception) {
			sqlexception.printStackTrace();
			ecamsLogger.error("## Cnd1200.getSql_Qry_Sub() SQLException START ##");
			ecamsLogger.error("## Error DESC : ", sqlexception);	
			ecamsLogger.error("## Cnd1200.getSql_Qry_Sub() SQLException END ##");			
			throw sqlexception;
		} catch (Exception exception) {
			exception.printStackTrace();
			ecamsLogger.error("## Cnd1200.getSql_Qry_Sub() Exception START ##");				
			ecamsLogger.error("## Error DESC : ", exception);	
			ecamsLogger.error("## Cnd1200.getSql_Qry_Sub() Exception END ##");				
			throw exception;
		}finally{
			if (strQuery != null) 	strQuery = null;
			if (rtObj != null)	rtObj = null;
			if (rs != null)     try{rs.close();}catch (Exception ex){ex.printStackTrace();}
			if (pstmt != null)  try{pstmt.close();}catch (Exception ex2){ex2.printStackTrace();}
			if (conn != null){
				try{
					conn.close();
					conn = null;
				}catch(Exception ex3){
					ecamsLogger.error("## Cnd1200.getSql_Qry_Sub() connection release exception ##");
					ex3.printStackTrace();
				}
			}
		}

	}//end of getSql_Qry_Sub() method statement    

    public Object[] getQryCd(String SysCd,String TstSw) throws SQLException, Exception {
		Connection        conn        = null;
		PreparedStatement pstmt       = null;
		ResultSet         rs          = null;
		StringBuffer      strQuery    = new StringBuffer();
		ArrayList<HashMap<String, String>>  rsval = new ArrayList<HashMap<String, String>>();
		HashMap<String, String>			  rst		  = null;
		Object[]		  rtObj		  = null;
		String            svReq = "";
		boolean           findSw = false;
		ConnectionContext connectionContext = new ConnectionResource();
		try {
			conn = connectionContext.getConnection();
			
			strQuery.setLength(0);    
	        strQuery.append("select a.cm_reqcd,a.cm_jobcd,b.cm_codename, \n");  
	        strQuery.append("       c.cm_codename prcsys                 \n");  
	        strQuery.append("  from cmm0020 c,cmm0020 b,cmm0060 a        \n");  
	        strQuery.append(" where a.cm_syscd=? and a.cm_manid='1'      \n");  
	        strQuery.append("   and a.cm_gubun='1'                       \n"); 
	        if (TstSw.equals("1")) 
	        	strQuery.append("and a.cm_reqcd in ('01','02','03','04','06','11','12') \n");
	        else
	        	strQuery.append("and a.cm_reqcd in ('01','02','04','06','11') \n");
	        strQuery.append("   and b.cm_macode='REQUEST'                \n");  
	        strQuery.append("   and b.cm_micode=a.cm_reqcd               \n"); 
	        strQuery.append("   and c.cm_macode='SYSGBN'                 \n");  
	        strQuery.append("   and c.cm_micode=a.cm_jobcd               \n");  
	        strQuery.append(" order by a.cm_reqcd,a.cm_seqno             \n");
            pstmt = conn.prepareStatement(strQuery.toString());
            pstmt.setString(1, SysCd);
            rs = pstmt.executeQuery();
	        while(rs.next()){
	        	findSw = false;
	        	
	        	if (svReq.length() == 0) findSw = true;
	        	else if (!svReq.equals(rs.getString("cm_reqcd"))) {
	        		findSw = true;
	        		
	        		if (svReq.equals("04") || svReq.equals("03")) {
	        			if (svReq.equals("04")) {
		        			rst = new HashMap<String, String>();
		    		        rst.put("ID", "PRCSYS");
		    		        rst.put("cm_reqcd", svReq);
		    		        rst.put("cm_jobcd", "SYSCN");
		    				rst.put("prcsys", "秒家贸府");
		    				rsval.add(rst);
	        			}
	        			
	    				rst = new HashMap<String, String>();
	    		        rst.put("ID", "PRCSYS");
	    		        rst.put("cm_reqcd", svReq);
	    		        rst.put("cm_jobcd", "SYSDEL");
	    				rst.put("prcsys", "企扁贸府");
	    				rsval.add(rst);
	    				
	    				rst = new HashMap<String, String>();
	    		        rst.put("ID", "PRCSYS");
	    		        rst.put("cm_reqcd", svReq);
	    		        rst.put("cm_jobcd", "SYSCED");
	    				rst.put("prcsys", "盔汗贸府");
	    				rsval.add(rst);
	        		} else if (svReq.equals("06")) {
	        			rst = new HashMap<String, String>();
	    		        rst.put("ID", "PRCSYS");
	    		        rst.put("cm_reqcd", svReq);
	    		        rst.put("cm_jobcd", "SYSCN");
	    				rst.put("prcsys", "秒家贸府");
	    				rsval.add(rst);
	        		}
	        	}
	        	
	        	rst = new HashMap<String, String>();
	        	if (findSw == true) {
	        		rst.put("ID", "QRYCD");
	        		svReq = rs.getString("cm_reqcd");
	        	} else {
	        		rst.put("ID", "PRCSYS");
	        	}
	        	rst.put("cm_reqcd", rs.getString("cm_reqcd"));
	        	rst.put("cm_codename", rs.getString("cm_codename"));
				rst.put("cm_jobcd", rs.getString("cm_jobcd"));
				rst.put("prcsys", rs.getString("prcsys"));
	           	rsval.add(rst);
	           	rst = null;
	        }
	        rs.close();
	        pstmt.close();	        
	        
	        if (rsval.size()>0) {
	        	if (svReq.equals("04") || svReq.equals("03")) {
	        		if (svReq.equals("04")) {
				        rst = new HashMap<String, String>();
				        rst.put("ID", "PRCSYS");
				        rst.put("cm_reqcd", svReq);
				        rst.put("cm_jobcd", "SYSCN");
						rst.put("prcsys", "秒家贸府");
						rsval.add(rst);
	        		}
					rst = new HashMap<String, String>();
			        rst.put("ID", "PRCSYS");
			        rst.put("cm_reqcd", svReq);
			        rst.put("cm_jobcd", "SYSDEL");
					rst.put("prcsys", "企扁贸府");
					rsval.add(rst);
	        	} else if (svReq.equals("06")) {
					rst = new HashMap<String, String>();
			        rst.put("ID", "PRCSYS");
			        rst.put("cm_reqcd", svReq);
			        rst.put("cm_jobcd", "SYSCN");
					rst.put("prcsys", "秒家贸府");
					rsval.add(rst);
	        	}
	        }
			rtObj = rsval.toArray();
			rsval = null;
			
			conn.close();
			rs = null;
			pstmt = null;
			conn = null;
			return rtObj;
	        
		} catch (SQLException sqlexception) {
			sqlexception.printStackTrace();
			ecamsLogger.error("## Cnd1200.getQryCd() SQLException START ##");
			ecamsLogger.error("## Error DESC : ", sqlexception);	
			ecamsLogger.error("## Cnd1200.getQryCd() SQLException END ##");			
			throw sqlexception;
		} catch (Exception exception) {
			exception.printStackTrace();
			ecamsLogger.error("## Cnd1200.getQryCd() Exception START ##");				
			ecamsLogger.error("## Error DESC : ", exception);	
			ecamsLogger.error("## Cnd1200.getQryCd() Exception END ##");				
			throw exception;
		}finally{
			if (strQuery != null) 	strQuery = null;
			if (rsval != null)	rsval = null;
			if (rs != null)     try{rs.close();}catch (Exception ex){ex.printStackTrace();}
			if (pstmt != null)  try{pstmt.close();}catch (Exception ex2){ex2.printStackTrace();}
			if (conn != null){
				try{
					conn.close();
					conn = null;
				}catch(Exception ex3){
					ecamsLogger.error("## Cnd1200.getQryCd() connection release exception ##");
					ex3.printStackTrace();
				}
			}
		}
	}//end of getCbo_SysCd_Click() method statement
  
    public Object[] getBldList(String SysCd,String TstSw,String QryCd,String SysInfo) throws SQLException, Exception {
		Connection        conn        = null;
		PreparedStatement pstmt       = null;
		ResultSet         rs          = null;
		StringBuffer      strQuery    = new StringBuffer();
		ArrayList<HashMap<String, String>>  rsval = new ArrayList<HashMap<String, String>>();
		HashMap<String, String>			  rst		  = null;
		Object[]		  rtObj		  = null;
		String            sysConf     = "";
		
		ConnectionContext connectionContext = new ConnectionResource();
		try {
			conn = connectionContext.getConnection();
			
			strQuery.setLength(0);    
	        strQuery.append("select cm_jobcd from cmm0060     \n");  
	        strQuery.append(" where cm_syscd=? and cm_reqcd=? \n");  
	        strQuery.append("   and cm_gubun='1'              \n");  
	        strQuery.append(" group by cm_jobcd               \n");
            pstmt = conn.prepareStatement(strQuery.toString());
            pstmt.setString(1, SysCd);
            pstmt.setString(2, QryCd);
            rs = pstmt.executeQuery();
	        while(rs.next()){
	        	sysConf = sysConf + rs.getString("cm_jobcd")+",";
	        }
	        rs.close();
	        pstmt.close();
	        
	        strQuery.setLength(0);    
	        strQuery.append("select b.cm_micode,b.cm_codename  \n");  
	        strQuery.append("  from cmm0020 b,cmm0036 a        \n");
	        strQuery.append(" where a.cm_syscd=?               \n");//Cbo_SysCd_code
	        strQuery.append("   and b.cm_macode='JAWON'        \n");//Cbo_SysCd_code
	        strQuery.append("   and a.cm_rsrccd=b.cm_micode    \n");//Cbo_SysCd_code
	        if (QryCd.equals("01") || QryCd.equals("02")) {
	        	strQuery.append("and (substr(a.cm_info,6,1)='1'   \n");
	        	strQuery.append("  or substr(a.cm_info,14,1)='1') \n");
	        } else  {
	        	strQuery.append("and (substr(a.cm_info,18,1)='1' \n"); //企扁矫颇老昏力
	        	if (QryCd.equals("12")) {
	        		strQuery.append("or substr(a.cm_info,6,1)='1'    \n"); //Lock包府
	        	} else if (sysConf.indexOf("SYSRK")>=0) {
	        		strQuery.append("or substr(a.cm_info,6,1)='1'   \n"); //Lock包府
	        	}
	        	if (sysConf.indexOf("SYSFT")>=0) {
	        		strQuery.append("or substr(a.cm_info,28,1)='1' \n"); //昆秒距己八刘
	        	}
	        	if (sysConf.indexOf("SYSUP")>=0 && !QryCd.equals("06")) {
	        		strQuery.append("or substr(a.cm_info,22,1)='1' \n"); //屈惑包府历厘胶农赋飘
	        	}
	        	if (sysConf.indexOf("SYSCB")>=0) {
	        		strQuery.append("or substr(a.cm_info,1,1)='1'  \n"); //哪颇老
	        	}
	        	if (sysConf.indexOf("SYSED")>=0 || sysConf.indexOf("SYSCED")>=0) {
	        		strQuery.append("or substr(a.cm_info,21,1)='1' \n"); //副府令胶农赋飘
	        	}
	        	if (sysConf.indexOf("SYSRC")>=0) {
	        		strQuery.append("or substr(a.cm_info,35,1)='1' \n"); //利侩胶农赋飘
	        	}
	        	if (QryCd.equals("04")) {
	        		strQuery.append("or substr(a.cm_info,17,1)='1') \n"); //眉农牢秒家僵角青
	        	} else {
	        		strQuery.append(") \n");
	        	}
	        } 
	        strQuery.append(" and a.cm_closedt is null order by b.cm_codename \n");
            pstmt = conn.prepareStatement(strQuery.toString());
            //pstmt =  new LoggableStatement(conn, strQuery.toString());
            pstmt.setString(1, SysCd);
            ////ecamsLogger.error(((LoggableStatement)pstmt).getQueryString());
            rs = pstmt.executeQuery();
	        while(rs.next()){
				rst = new HashMap<String, String>();
				rst.put("ID", "RSRCCD");
				rst.put("cm_micode", rs.getString("cm_micode"));
				rst.put("cm_codename", rs.getString("cm_codename"));
	           	rsval.add(rst);
	           	rst = null;
	        }
	        rs.close();
	        pstmt.close();
	        
			strQuery.setLength(0);		    
		    strQuery.append("select c.cm_codename,a.cm_jobcd,a.cm_rsrccd,a.cm_bldcd,   \n");
		    if (SysInfo.substring(7,8).equals("1")) strQuery.append("b.cm_jobname,     \n");
		    strQuery.append("       a.cm_prcsys,d.cm_codename prcsys,a.cm_bldgbn,       \n");
		    strQuery.append("      (select cm_codename from cmm0020 where cm_macode = 'BLDCD' \n");
		    strQuery.append("		and a.cm_bldcd = cm_micode) as bldname 					\n");	
    	    strQuery.append("  from cmm0020 d,cmm0020 c,cmm0033 a            			\n");
    	    if (SysInfo.substring(7,8).equals("1")) strQuery.append(",cmm0102 b        \n");
    	    strQuery.append(" where a.cm_syscd=? and a.cm_qrycd=?                      \n");//Cbo_SysCd_code
    	    if (SysInfo.substring(7,8).equals("1")) 
    	    	strQuery.append("   and a.cm_jobcd=b.cm_jobcd                          \n");
    	    strQuery.append("   and c.cm_macode='JAWON' and c.cm_micode=a.cm_rsrccd    \n");
    	    strQuery.append("   and d.cm_macode='SYSGBN' and d.cm_micode=a.cm_prcsys   \n");
    	    strQuery.append("order by c.cm_codename,d.cm_seqno,a.cm_jobcd              \n");
            pstmt = conn.prepareStatement(strQuery.toString());
            pstmt =  new LoggableStatement(conn, strQuery.toString());
            pstmt.setString(1, SysCd);
            pstmt.setString(2, QryCd);
            ecamsLogger.error(((LoggableStatement)pstmt).getQueryString());
            rs = pstmt.executeQuery();
	        while(rs.next()){
	        	rst = new HashMap<String, String>();
				
				rst.put("ID", "BLDLIST");
				rst.put("cm_codename", rs.getString("cm_codename"));
				if (SysInfo.substring(7,8).equals("1")) rst.put("cm_jobname", rs.getString("cm_jobname"));
				else rst.put("cm_jobname", "葛电诀公");
				rst.put("prcsys", rs.getString("prcsys"));
				rst.put("bldcd", rs.getString("cm_bldCD"));
				rst.put("bldname", rs.getString("bldname"));
				rst.put("cm_jobcd", rs.getString("cm_jobcd"));
				rst.put("cm_rsrccd", rs.getString("cm_rsrccd"));
				rst.put("cm_bldgbn", rs.getString("cm_bldgbn"));
				rst.put("cm_bldcd", rs.getString("cm_bldcd"));
				rst.put("cm_prcsys", rs.getString("cm_prcsys"));
	           	rsval.add(rst);
	           	rst = null;
	        }
	        rs.close();
	        pstmt.close();
	        conn.close();
			rs = null;
			pstmt = null;
			conn = null;
	        
			rtObj = rsval.toArray();
			rsval = null;
			return rtObj;
	        
		} catch (SQLException sqlexception) {
			sqlexception.printStackTrace();
			ecamsLogger.error("## Cnd1200.getSql_Qry_Tab2() SQLException START ##");
			ecamsLogger.error("## Error DESC : ", sqlexception);	
			ecamsLogger.error("## Cnd1200.getSql_Qry_Tab2() SQLException END ##");			
			throw sqlexception;
		} catch (Exception exception) {
			exception.printStackTrace();
			ecamsLogger.error("## Cnd1200.getSql_Qry_Tab2() Exception START ##");				
			ecamsLogger.error("## Error DESC : ", exception);	
			ecamsLogger.error("## Cnd1200.getSql_Qry_Tab2() Exception END ##");				
			throw exception;
		}finally{
			if (strQuery != null) 	strQuery = null;
			if (rsval != null)	rsval = null;
			if (rs != null)     try{rs.close();}catch (Exception ex){ex.printStackTrace();}
			if (pstmt != null)  try{pstmt.close();}catch (Exception ex2){ex2.printStackTrace();}
			if (conn != null){
				try{
					conn.close();
					conn = null;
				}catch(Exception ex3){
					ecamsLogger.error("## Cnd1200.getSql_Qry_Tab2() connection release exception ##");
					ex3.printStackTrace();
				}
			}
		}
	}//end of getCbo_SysCd_Click() method statement
    
    public Object[] getSql_Qry_Sub_Tab2(String Cbo_BldCd_code,String index) throws SQLException, Exception {
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
		    
		    strQuery.append("select cm_seq,cm_cmdname from cmm0022 \n");
    		strQuery.append("where cm_bldcd=? \n");//Cbo_BldCd_code
    		strQuery.append("  and cm_gbncd=? \n");//index
    		strQuery.append("order by cm_seq \n");
		    
    		//pstmt =  new LoggableStatement(conn, strQuery.toString());
            pstmt = conn.prepareStatement(strQuery.toString());
            pstmt.setString(1, Cbo_BldCd_code);
            pstmt.setString(2, index);
            ////ecamsLogger.error(((LoggableStatement)pstmt).getQueryString());
            rs = pstmt.executeQuery();
	        while(rs.next()){
				rst = new HashMap<String, String>();
				rst.put("cm_seq", rs.getString("cm_seq"));
				rst.put("cm_cmdname", rs.getString("cm_cmdname"));
	           	rsval.add(rst);
	           	rst = null;
	        }
	        rs.close();
	        pstmt.close();
	        conn.close();
			rs = null;
			pstmt = null;
			conn = null;
			rtObj = rsval.toArray();
			rsval = null;
			return rtObj;
	        

		} catch (SQLException sqlexception) {
			sqlexception.printStackTrace();
			ecamsLogger.error("## Cnd1200.getSql_Qry_Sub_Tab2() SQLException START ##");
			ecamsLogger.error("## Error DESC : ", sqlexception);	
			ecamsLogger.error("## Cnd1200.getSql_Qry_Sub_Tab2() SQLException END ##");			
			throw sqlexception;
		} catch (Exception exception) {
			exception.printStackTrace();
			ecamsLogger.error("## Cnd1200.getSql_Qry_Sub_Tab2() Exception START ##");				
			ecamsLogger.error("## Error DESC : ", exception);	
			ecamsLogger.error("## Cnd1200.getSql_Qry_Sub_Tab2() Exception END ##");				
			throw exception;
		}finally{
			if (strQuery != null) 	strQuery = null;
			if (rsval != null)	rsval = null;
			if (rs != null)     try{rs.close();}catch (Exception ex){ex.printStackTrace();}
			if (pstmt != null)  try{pstmt.close();}catch (Exception ex2){ex2.printStackTrace();}
			if (conn != null){
				try{
					conn.close();
					conn = null;
				}catch(Exception ex3){
					ecamsLogger.error("## Cnd1200.getSql_Qry_Sub_Tab2() connection release exception ##");
					ex3.printStackTrace();
				}
			}
		}

	}//end of getSql_Qry_Sub_Tab2() method statement
    
    public Object[] getSql_Qry(String Cbo_BldGbn) throws SQLException, Exception {
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
			strQuery.append("select b.cm_codename, a.cm_bldcd,a.cm_seq,a.cm_cmdname,a.cm_errword,nvl(a.cm_runtype,'R') cm_runtype \n");
			strQuery.append(" from cmm0022 a, cmm0020 b where 																							 \n");
			strQuery.append(" a.cm_gbncd=? and  b.cm_micode=a.cm_bldcd and b.cm_macode='BLDCD'                             	    \n");// Cbo_BldGbn
			strQuery.append("order by a.cm_bldcd,a.cm_seq   																							 \n");     
			pstmt = conn.prepareStatement(strQuery.toString());
            //pstmt =  new LoggableStatement(conn, strQuery.toString());
            pstmt.setString(1, Cbo_BldGbn);
            ////ecamsLogger.error(((LoggableStatement)pstmt).getQueryString());
            rs = pstmt.executeQuery();
		    while(rs.next()){		    	
		   	 rst = new HashMap<String, String>();
					rst.put("cm_codename", rs.getString("cm_codename"));
					rst.put("cm_errword", rs.getString("cm_errword"));
					rst.put("cm_seq", rs.getString("cm_seq"));
					rst.put("cm_cmdname", rs.getString("cm_cmdname"));
					rst.put("cm_bldcd", rs.getString("cm_bldcd"));
					rst.put("cm_runtype", rs.getString("cm_runtype"));
					rsval.add(rst);
	           	rst = null;
		    }
		    rs.close();
		    pstmt.close();
		    conn.close();
			rs = null;
			pstmt = null;
			conn = null;
			rtObj = rsval.toArray();
			rsval = null;
			return rtObj;
		    
		} catch (SQLException sqlexception) {
			sqlexception.printStackTrace();
			ecamsLogger.error("## Cnd1200.getSql_Qry() SQLException START ##");
			ecamsLogger.error("## Error DESC : ", sqlexception);	
			ecamsLogger.error("## Cnd1200.getSql_Qry() SQLException END ##");			
			throw sqlexception;
		} catch (Exception exception) {
			exception.printStackTrace();
			ecamsLogger.error("## Cnd1200.getSql_Qry() Exception START ##");				
			ecamsLogger.error("## Error DESC : ", exception);	
			ecamsLogger.error("## Cnd1200.getSql_Qry() Exception END ##");				
			throw exception;
		}finally{
			if (strQuery != null) 	strQuery = null;
			if (rsval != null)	rsval = null;
			if (rs != null)     try{rs.close();}catch (Exception ex){ex.printStackTrace();}
			if (pstmt != null)  try{pstmt.close();}catch (Exception ex2){ex2.printStackTrace();}
			if (conn != null){
				try{
					conn.close();
					conn = null;
				}catch(Exception ex3){
					ecamsLogger.error("## Cnd1200.getSql_Qry() connection release exception ##");
					ex3.printStackTrace();
				}
			}
		}

	}//end of getSql_Qry_Sub_Tab2() method statement
    
    public int getCmm0022_Del(String Cbo_BldGbn,String Cbo_BldCd0) throws SQLException, Exception {	
		Connection        conn        = null;
		PreparedStatement pstmt       = null;
		ResultSet         rs          = null;
		StringBuffer      strQuery    = new StringBuffer();
		int				  rtn_cnt     = 0;
		
		ConnectionContext connectionContext = new ConnectionResource();
		try {
			conn = connectionContext.getConnection();
			
			strQuery.setLength(0);		    
	    	strQuery.append("DELETE CMM0022 WHERE CM_GBNCD=? \n");//Cbo_BldGbn
	    	strQuery.append("AND CM_BLDCD=? \n");//Cbo_BldCd0
			pstmt = conn.prepareStatement(strQuery.toString());
            pstmt.setString(1, Cbo_BldGbn);
            pstmt.setString(2, Cbo_BldCd0);
            rtn_cnt = pstmt.executeUpdate();
            pstmt.close();
			
            conn.close();
			
			pstmt = null;
			conn = null;
            return rtn_cnt;
            
		} catch (SQLException sqlexception) {
			sqlexception.printStackTrace();
			ecamsLogger.error("## Cnd1200.getCmm0022_Del() SQLException START ##");
			ecamsLogger.error("## Error DESC : ", sqlexception);	
			ecamsLogger.error("## Cnd1200.getCmm0022_Del() SQLException END ##");			
			throw sqlexception;
		} catch (Exception exception) {
			exception.printStackTrace();
			ecamsLogger.error("## Cnd1200.getCmm0022_Del() Exception START ##");				
			ecamsLogger.error("## Error DESC : ", exception);	
			ecamsLogger.error("## Cnd1200.getCmm0022_Del() Exception END ##");				
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
					ecamsLogger.error("## Cnd1200.getCmm0022_Del() connection release exception ##");
					ex3.printStackTrace();
				}
			}
		}
    }//end of getCmm0022_Del() method statement

    public int getCmm0022_Copy(String Cbo_BldGbn,String Cbo_BldCd0,String NewBld, String NewBldMsg) throws SQLException, Exception {	
 		Connection        					conn     = null;
 		PreparedStatement 					pstmt    = null;
 		ResultSet         					rs       = null;
 		StringBuffer     				 	strQuery = new StringBuffer();
 		ArrayList<HashMap<String, String>>  rsval    = new ArrayList<HashMap<String, String>>();
 		HashMap<String, String>			  	rst		 = null;
 		int				  					rtn_cnt  = 0;
 		
 		ConnectionContext connectionContext = new ConnectionResource();
 		try {
 			conn = connectionContext.getConnection();
 			int Cnt = 0;
 			int Temp = 0;
 			
 			strQuery.setLength(0);
 		    strQuery.append("insert into cmm0022 (CM_GBNCD,CM_BLDCD,CM_SEQ,CM_CMDNAME,CM_ERRWORD,CM_RUNTYPE,CM_BLDNAME) \n");
 		    strQuery.append("(SELECT CM_GBNCD,?,CM_SEQ,CM_CMDNAME,CM_ERRWORD,CM_RUNTYPE,? \n");//NewBld
 		    strQuery.append("   FROM CMM0022    \n");
 		    strQuery.append("  WHERE CM_GBNCD=? \n");//Cbo_BldGbn
 		    strQuery.append("    AND CM_BLDCD=?) \n");//Cbo_BldCd0
 			pstmt = conn.prepareStatement(strQuery.toString());
 			pstmt =  new LoggableStatement(conn, strQuery.toString());
 			pstmt.setString(1, NewBld);
            pstmt.setString(2, NewBldMsg);
            pstmt.setString(3, Cbo_BldGbn);
            pstmt.setString(4, Cbo_BldCd0);
            ecamsLogger.error(((LoggableStatement)pstmt).getQueryString());
             rtn_cnt = pstmt.executeUpdate();
             
             pstmt.close();
             conn.commit();
             conn.close();

             rs = null;
 			 pstmt = null;
 			 conn = null;
             return rtn_cnt;
             
 		} catch (SQLException sqlexception) {
 			sqlexception.printStackTrace();
 			ecamsLogger.error("## Cnd1200.getCmm0022_Copy() SQLException START ##");
 			ecamsLogger.error("## Error DESC : ", sqlexception);	
 			ecamsLogger.error("## Cnd1200.getCmm0022_Copy() SQLException END ##");			
 			throw sqlexception;
 		} catch (Exception exception) {
 			exception.printStackTrace();
 			ecamsLogger.error("## Cnd1200.getCmm0022_Copy() Exception START ##");				
 			ecamsLogger.error("## Error DESC : ", exception);	
 			ecamsLogger.error("## Cnd1200.getCmm0022_Copy() Exception END ##");				
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
 					ecamsLogger.error("## Cnd1200.getCmm0022_Copy() connection release exception ##");
 					ex3.printStackTrace();
 				}
 			}
 		}
     }//end of getCmm0022_Copy() method statement
    
    public int getCmm0022_Copy_old(String Cbo_BldGbn,String Cbo_BldCd0,String NewBld) throws SQLException, Exception {	
		Connection        					conn     = null;
		PreparedStatement 					pstmt    = null;
		ResultSet         					rs       = null;
		StringBuffer     				 	strQuery = new StringBuffer();
		ArrayList<HashMap<String, String>>  rsval    = new ArrayList<HashMap<String, String>>();
		HashMap<String, String>			  	rst		 = null;
		int				  					rtn_cnt  = 0;
		
		ConnectionContext connectionContext = new ConnectionResource();
		try {
			conn = connectionContext.getConnection();
			int Cnt = 0;
			int Temp = 0;
			strQuery.setLength(0);
			strQuery.append("select cm_micode from cmm0020  			   	  	 \n");
			strQuery.append("where cm_macode='BLDCD' order by cm_micode        \n");
			pstmt = conn.prepareStatement(strQuery.toString());
			rs = pstmt.executeQuery();
			while (rs.next())
			{
				String tmp = rs.getString("cm_micode");

				if (tmp.equals("****") || tmp.equals("0000"))
					continue;

				Temp = Integer.parseInt(tmp);
				int num = Temp - Cnt;
				if (num >= 2){
					break;
				}
				Cnt = Temp;
			}

			++Cnt;
			String bldcd = "";
			if (Cnt < 10)
				bldcd = "0" + Integer.toString(Cnt);
			else
				bldcd = Integer.toString(Cnt);

			pstmt.close();
			rs = null;
			/*
			int NewBld = 1;
			strQuery.setLength(0);
		    strQuery.append("SELECT MAX(CM_BLDCD) MAX FROM CMM0022 \n");
		    strQuery.append("WHERE CM_GBNCD=? \n");//Cbo_BldGbn
            pstmt = conn.prepareStatement(strQuery.toString());
            pstmt.setString(1, Cbo_BldGbn);
            rs = pstmt.executeQuery();
		    if(rs.next()){
		    	NewBld = Integer.parseInt(rs.getString("MAX"))+1;
		    }
		    rs.close();
		    pstmt.close();
		    */
			strQuery.setLength(0);
			strQuery.append("insert into cmm0020 (CM_MACODE,CM_MICODE,CM_CODENAME,CM_USEYN) values (?,?,?,?)");
			pstmt = conn.prepareStatement(strQuery.toString());

			pstmt.setString(1, "BLDCD");
			pstmt.setString(2, bldcd);
			pstmt.setString(3, NewBld);
			pstmt.setString(4, "Y");
			rtn_cnt = pstmt.executeUpdate();
			
			pstmt.close();

			strQuery.setLength(0);
			strQuery.append(" SELECT CM_GBNCD,CM_SEQ,CM_CMDNAME,CM_ERRWORD,CM_RUNTYPE 		   				  \n");// NewBld
			strQuery.append(" FROM CMM0022    															      \n");
			strQuery.append(" WHERE CM_GBNCD=? 																  \n");// Cbo_BldGbn
			strQuery.append(" AND CM_BLDCD=? 																  \n");// Cbo_BldCd0
			pstmt = conn.prepareStatement(strQuery.toString());
		    //pstmt =  new LoggableStatement(conn, strQuery.toString());
		    pstmt.setString(1, Cbo_BldGbn);
            pstmt.setString(2, Cbo_BldCd0);
            ////ecamsLogger.error(((LoggableStatement)pstmt).getQueryString());
        	rs = pstmt.executeQuery();
        	
        	while(rs.next()){
        		rst = new HashMap<String, String>();
        		rst.put("CM_GBNCD", rs.getString("CM_GBNCD"));
        		rst.put("CM_ERRWORD", rs.getString("CM_ERRWORD"));
				rst.put("CM_SEQ", rs.getString("CM_SEQ"));
				rst.put("CM_CMDNAME", rs.getString("CM_CMDNAME"));
				rst.put("CM_BLDCD", bldcd);
				rst.put("CM_RUNTYPE", rs.getString("CM_RUNTYPE"));
				rsval.add(rst);
				rst = null;	
        	}
            pstmt.close();
            rs = null;
            for(int i=0;i<rsval.size();i++){
            	strQuery.setLength(0);
            	strQuery.append(" insert into cmm0022 (CM_GBNCD,CM_BLDCD,CM_SEQ,CM_CMDNAME,CM_ERRWORD,CM_RUNTYPE) values (?,?,?,?,?,?) \n");
            	
            	pstmt = conn.prepareStatement(strQuery.toString());
            	
            	pstmt.setString(1, rsval.get(i).get("CM_GBNCD"));
            	pstmt.setString(2, rsval.get(i).get("CM_BLDCD"));
            	pstmt.setString(3, rsval.get(i).get("CM_SEQ"));
            	pstmt.setString(4, rsval.get(i).get("CM_CMDNAME"));
            	pstmt.setString(5, rsval.get(i).get("CM_ERRWORD"));
            	pstmt.setString(6, rsval.get(i).get("CM_RUNTYPE"));
            	
            	rtn_cnt = pstmt.executeUpdate();
            }
            pstmt.close();
            conn.commit();
            conn.close();
			rs = null;
			pstmt = null;
			conn = null;
            return rtn_cnt;
            
		} catch (SQLException sqlexception) {
			sqlexception.printStackTrace();
			ecamsLogger.error("## Cnd1200.getCmm0022_Copy() SQLException START ##");
			ecamsLogger.error("## Error DESC : ", sqlexception);	
			ecamsLogger.error("## Cnd1200.getCmm0022_Copy() SQLException END ##");			
			throw sqlexception;
		} catch (Exception exception) {
			exception.printStackTrace();
			ecamsLogger.error("## Cnd1200.getCmm0022_Copy() Exception START ##");				
			ecamsLogger.error("## Error DESC : ", exception);	
			ecamsLogger.error("## Cnd1200.getCmm0022_Copy() Exception END ##");				
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
					ecamsLogger.error("## Cnd1200.getCmm0022_Copy() connection release exception ##");
					ex3.printStackTrace();
				}
			}
		}
    }//end of getCmm0022_Copy() method statement
    
    public int getCmm0022_DBProc (String Cbo_BldGbn, String Cbo_BldCd0, String codename, String Txt_Comp2, String runType, ArrayList<HashMap<String, String>> Lv_File0_dp) throws SQLException, Exception {	
		
    	Connection        conn        = null;
		PreparedStatement pstmt       = null;
		ResultSet         rs          = null;
		StringBuffer      strQuery    = new StringBuffer();
		int				  rtn_cnt     = 0;
		
		ConnectionContext connectionContext = new ConnectionResource();
		try {
			conn = connectionContext.getConnection();
	
			strQuery.setLength(0);
		    strQuery.append("delete cmm0022 where cm_gbncd=? 									\n");//Cbo_BldGbn
		    strQuery.append("and cm_bldcd=? 				 									\n");//Cbo_BldCd0
			pstmt = conn.prepareStatement(strQuery.toString());
            pstmt.setString(1, Cbo_BldGbn);
            pstmt.setString(2, Cbo_BldCd0);
            rtn_cnt = pstmt.executeUpdate();
        
            pstmt.close();
			
			
            int x = 0;
		    for (x = 0 ; x<Lv_File0_dp.size() ; x++){
		    	strQuery.setLength(0);
		    	strQuery.append("insert into cmm0022 (CM_BLDCD,CM_SEQ,CM_CMDNAME,CM_GBNCD,CM_ERRWORD,CM_RUNTYPE,CM_BLDNAME) values (\n");
		    	strQuery.append("?, \n");//Cbo_BldCd0
		    	strQuery.append("?, \n");//Lv_File0.cm_seq
		        strQuery.append("?, \n");//replace(CM_CMDNAME)
		        strQuery.append("?, \n");//Cbo_BldGbn
		        strQuery.append("?, \n");//replace(Txt_Comp2)
		        strQuery.append("?, \n");//replace(Txt_Comp2)
		        strQuery.append("?) \n");//runtype
		        
				pstmt = conn.prepareStatement(strQuery.toString());
				//pstmt =  new LoggableStatement(conn, strQuery.toString());
				
				pstmt.setString(1, Cbo_BldCd0);
	            pstmt.setString(2, Lv_File0_dp.get(x).get("cm_seq"));
	            pstmt.setString(3, Lv_File0_dp.get(x).get("cm_cmdname"));
	            pstmt.setString(4, Cbo_BldGbn);
	            pstmt.setString(5, Txt_Comp2);
	            pstmt.setString(6, runType);
	            pstmt.setString(7, codename);

				////ecamsLogger.error(((LoggableStatement)pstmt).getQueryString());
	            rtn_cnt = pstmt.executeUpdate();
	            pstmt.close();
		    }
		    
		    conn.commit();
		    conn.close();
			rs = null;
			pstmt = null;
			conn = null;
		    return rtn_cnt;
		    

		} catch (SQLException sqlexception) {
			sqlexception.printStackTrace();
			ecamsLogger.error("## Cnd1200.getCmm0022_DBProc() SQLException START ##");
			ecamsLogger.error("## Error DESC : ", sqlexception);	
			ecamsLogger.error("## Cnd1200.getCmm0022_DBProc() SQLException END ##");			
			throw sqlexception;
		} catch (Exception exception) {
			exception.printStackTrace();
			ecamsLogger.error("## Cnd1200.getCmm0022_DBProc() Exception START ##");				
			ecamsLogger.error("## Error DESC : ", exception);	
			ecamsLogger.error("## Cnd1200.getCmm0022_DBProc() Exception END ##");				
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
					ecamsLogger.error("## Cnd1200.getCmm0022_DBProc() connection release exception ##");
					ex3.printStackTrace();
				}
			}
		}
    }//end of getCmm0022_DBProc() method statement
    
    public int getCmm0022_DBProc_old (String Cbo_BldGbn, String Cbo_BldCd0, String codename, String Txt_Comp2, String runType, ArrayList<HashMap<String, String>> Lv_File0_dp) throws SQLException, Exception {	
		
    	Connection        conn        = null;
		PreparedStatement pstmt       = null;
		ResultSet         rs          = null;
		StringBuffer      strQuery    = new StringBuffer();
		int				  rtn_cnt     = 0;
		
		ConnectionContext connectionContext = new ConnectionResource();
		try {
			conn = connectionContext.getConnection();
			
			if (Cbo_BldCd0.equals("00")){
				int Cnt = 0;
				int Temp = 0;
				strQuery.setLength(0);

				strQuery.append("select cm_micode from cmm0020  			     \n");
				strQuery.append("where cm_macode='BLDCD' order by cm_micode      \n");
				pstmt = conn.prepareStatement(strQuery.toString());
				rs = pstmt.executeQuery();
				while (rs.next())
				{
					String tmp = rs.getString("cm_micode");

					if (tmp.equals("****") || tmp.equals("0000"))
						continue;

					Temp = Integer.parseInt(tmp);
					int num = Temp - Cnt;
					if (num >= 2){
						break;
					}
						
						
					Cnt = Temp;
				}

				++Cnt;

				if (Cnt < 10)
					Cbo_BldCd0 = "0" + Integer.toString(Cnt);
				else
					Cbo_BldCd0 = Integer.toString(Cnt);

				pstmt.close();
			}
			strQuery.setLength(0);
		    strQuery.append("delete cmm0022 where cm_gbncd=? 									\n");//Cbo_BldGbn
		    strQuery.append("and cm_bldcd=? 				 									\n");//Cbo_BldCd0
			pstmt = conn.prepareStatement(strQuery.toString());
            pstmt.setString(1, Cbo_BldGbn);
            pstmt.setString(2, Cbo_BldCd0);
            rtn_cnt = pstmt.executeUpdate();
        
            pstmt.close();
            
            strQuery.setLength(0);
			strQuery.append("delete cmm0020 where cm_macode='BLDCD' and cm_micode=?				\n");
			pstmt = conn.prepareStatement(strQuery.toString());
			pstmt.setString(1, Cbo_BldCd0);
			rtn_cnt = pstmt.executeUpdate();
			pstmt.close();
			
			strQuery.setLength(0);
			strQuery.append("insert into cmm0020 (CM_MACODE,CM_MICODE,CM_CODENAME,CM_USEYN) values (?,?,?,?)");
			pstmt = conn.prepareStatement(strQuery.toString());

			pstmt.setString(1, "BLDCD");
			pstmt.setString(2, Cbo_BldCd0);
			pstmt.setString(3, codename);
			pstmt.setString(4, "Y");
			rtn_cnt = pstmt.executeUpdate();
			pstmt.close();

			
            int x = 0;
		    for (x = 0 ; x<Lv_File0_dp.size() ; x++){
		    	strQuery.setLength(0);
		    	strQuery.append("insert into cmm0022 (CM_BLDCD,CM_SEQ,CM_CMDNAME,CM_GBNCD,CM_ERRWORD,CM_RUNTYPE) values (\n");
		    	strQuery.append("?, \n");//Cbo_BldCd0
		    	strQuery.append("?, \n");//Lv_File0.cm_seq
		        strQuery.append("?, \n");//replace(CM_CMDNAME)
		        strQuery.append("?, \n");//Cbo_BldGbn
		        strQuery.append("?, \n");//replace(Txt_Comp2)
		        strQuery.append("?) \n");//runtype
		        
				pstmt = conn.prepareStatement(strQuery.toString());
				//pstmt =  new LoggableStatement(conn, strQuery.toString());
				
				pstmt.setString(1, Cbo_BldCd0);
	            pstmt.setString(2, Lv_File0_dp.get(x).get("cm_seq"));
	            pstmt.setString(3, Lv_File0_dp.get(x).get("cm_cmdname"));
	            pstmt.setString(4, Cbo_BldGbn);
	            pstmt.setString(5, Txt_Comp2);
	            pstmt.setString(6, runType);

				////ecamsLogger.error(((LoggableStatement)pstmt).getQueryString());
	            rtn_cnt = pstmt.executeUpdate();
	            pstmt.close();
		    }
		    
		    conn.commit();
		    conn.close();
			rs = null;
			pstmt = null;
			conn = null;
		    return rtn_cnt;
		    

		} catch (SQLException sqlexception) {
			sqlexception.printStackTrace();
			ecamsLogger.error("## Cnd1200.getCmm0022_DBProc() SQLException START ##");
			ecamsLogger.error("## Error DESC : ", sqlexception);	
			ecamsLogger.error("## Cnd1200.getCmm0022_DBProc() SQLException END ##");			
			throw sqlexception;
		} catch (Exception exception) {
			exception.printStackTrace();
			ecamsLogger.error("## Cnd1200.getCmm0022_DBProc() Exception START ##");				
			ecamsLogger.error("## Error DESC : ", exception);	
			ecamsLogger.error("## Cnd1200.getCmm0022_DBProc() Exception END ##");				
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
					ecamsLogger.error("## Cnd1200.getCmm0022_DBProc() connection release exception ##");
					ex3.printStackTrace();
				}
			}
		}
    }//end of getCmm0022_DBProc() method statement
    
    public int getCmm0033_DBProc(HashMap<String,String> etcData) throws SQLException, Exception {	
		Connection        conn        = null;
		PreparedStatement pstmt       = null;
		ResultSet         rs          = null;
		StringBuffer      strQuery    = new StringBuffer();
		int				  rtn_cnt     = 0;
		
		ConnectionContext connectionContext = new ConnectionResource();
		try {
			conn = connectionContext.getConnection();
		    
			strQuery.setLength(0);
			strQuery.append("delete cmm0033                \n");
			strQuery.append(" where cm_syscd=? and cm_qrycd=?  \n");//Cbo_SysCd
			strQuery.append("   and cm_prcsys=?                \n");//Cbo_SysCd
			strQuery.append("   and instr(?,cm_rsrccd)>0       \n");//Cbo_SysCd
			strQuery.append("   and instr(?,cm_jobcd)>0        \n");//Cbo_SysCd
			pstmt = conn.prepareStatement(strQuery.toString());
            pstmt.setString(1, etcData.get("cm_syscd"));
            pstmt.setString(2, etcData.get("cm_qrycd"));
            pstmt.setString(3, etcData.get("cm_prcsys"));
            pstmt.setString(4, etcData.get("cm_rsrccd"));
            pstmt.setString(5, etcData.get("cm_jobcd"));
            rtn_cnt = pstmt.executeUpdate();
            pstmt.close();
		    
            String svRsrc[] = etcData.get("cm_rsrccd").split(",");
            String svJob[] = etcData.get("cm_jobcd").split(",");
            int x = 0;
            int y = 0;
		    for (x=0;svRsrc.length>x;x++){
		    	for (y=0;svJob.length>y;y++) {
			    	strQuery.setLength(0);
			    	strQuery.append("insert into cmm0033 \n");
			    	strQuery.append(" (CM_SYSCD,CM_RSRCCD,CM_JOBCD,CM_QRYCD,CM_PRCSYS,CM_BLDGBN,CM_BLDCD) \n");
			    	strQuery.append("values (\n");
			    	strQuery.append("?, \n");//CM_SYSCD
			    	strQuery.append("?, \n");//CM_RSRCCD
			        strQuery.append("?, \n");//CM_JOBCD
			    	strQuery.append("?, \n");//CM_QRYCD
			    	strQuery.append("?, \n");//CM_PRCSYS
			    	strQuery.append("?, \n");//CM_BLDGBN
			        strQuery.append("?) \n");//CM_BLDCD
			        
					pstmt = conn.prepareStatement(strQuery.toString());
					//pstmt =  new LoggableStatement(conn, strQuery.toString());
					
					pstmt.setString(1, etcData.get("cm_syscd"));
		            pstmt.setString(2, svRsrc[x]);
		            pstmt.setString(3, svJob[y]);
		            pstmt.setString(4, etcData.get("cm_qrycd"));
		            pstmt.setString(5, etcData.get("cm_prcsys"));
		            pstmt.setString(6, etcData.get("cm_bldgbn"));
		            pstmt.setString(7, etcData.get("cm_bldcd"));
		            
					////ecamsLogger.error(((LoggableStatement)pstmt).getQueryString());
		            rtn_cnt = pstmt.executeUpdate();
		            pstmt.close();
		    	}
		    }
		    
		    conn.commit();
		    conn.close();
			rs = null;
			pstmt = null;
			conn = null;
		    return rtn_cnt;
		    
		} catch (SQLException sqlexception) {
			sqlexception.printStackTrace();
			ecamsLogger.error("## Cnd1200.getCmm0033_DBProc() SQLException START ##");
			ecamsLogger.error("## Error DESC : ", sqlexception);	
			ecamsLogger.error("## Cnd1200.getCmm0033_DBProc() SQLException END ##");			
			throw sqlexception;
		} catch (Exception exception) {
			exception.printStackTrace();
			ecamsLogger.error("## Cnd1200.getCmm0033_DBProc() Exception START ##");				
			ecamsLogger.error("## Error DESC : ", exception);	
			ecamsLogger.error("## Cnd1200.getCmm0033_DBProc() Exception END ##");				
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
					ecamsLogger.error("## Cnd1200.getCmm0033_DBProc() connection release exception ##");
					ex3.printStackTrace();
				}
			}
		}
    }//end of getCmm0033_DBProc() method statement

    public int getCmm0033_Del(ArrayList<HashMap<String,String>> delList) throws SQLException, Exception {	
		Connection        conn        = null;
		PreparedStatement pstmt       = null;
		ResultSet         rs          = null;
		StringBuffer      strQuery    = new StringBuffer();
		
		ConnectionContext connectionContext = new ConnectionResource();
		try {
			conn = connectionContext.getConnection();
			
			for (int i=0;delList.size()>i;i++) {
				strQuery.setLength(0);		    
		    	strQuery.append("DELETE cmm0033    \n");   
		    	strQuery.append(" WHERE CM_syscd=?     \n");
		    	strQuery.append("   AND CM_qrycd=?     \n");
		    	strQuery.append("   AND CM_prcsys=?    \n");
		    	strQuery.append("   AND CM_rsrccd=?    \n");
		    	strQuery.append("   AND CM_jobcd=?     \n");
				pstmt = conn.prepareStatement(strQuery.toString());
	            pstmt.setString(1, delList.get(i).get("cm_syscd"));
	            pstmt.setString(2, delList.get(i).get("cm_qrycd"));
	            pstmt.setString(3, delList.get(i).get("cm_prcsys"));
	            pstmt.setString(4, delList.get(i).get("cm_rsrccd"));
	            pstmt.setString(5, delList.get(i).get("cm_jobcd"));
	            pstmt.executeUpdate();
	            pstmt.close();
			}
			conn.close();
			rs = null;
			pstmt = null;
			conn = null;
            return 0;
            
		} catch (SQLException sqlexception) {
			sqlexception.printStackTrace();
			ecamsLogger.error("## Cnd1200.getCmm0033_Del() SQLException START ##");
			ecamsLogger.error("## Error DESC : ", sqlexception);	
			ecamsLogger.error("## Cnd1200.getCmm0033_Del() SQLException END ##");			
			throw sqlexception;
		} catch (Exception exception) {
			exception.printStackTrace();
			ecamsLogger.error("## Cnd1200.getCmm0033_Del() Exception START ##");				
			ecamsLogger.error("## Error DESC : ", exception);	
			ecamsLogger.error("## Cnd1200.getCmm0033_Del() Exception END ##");				
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
					ecamsLogger.error("## Cnd1200.getCmm0022_Del() connection release exception ##");
					ex3.printStackTrace();
				}
			}
		}
    }//end of getCmm0022_Del() method statement
}//end of Cmd1200 class statement
