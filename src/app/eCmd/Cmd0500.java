/*****************************************************************************************
	1. program ID	: eCmd0500.java
	2. create date	: 2008.07. 10
	3. auth		    : no name
	4. update date	: 09.07.16
	5. auth		    : 
	6. description	: [프로그램]->[프로그램정보] 화면
*****************************************************************************************/

package app.eCmd;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;

import org.apache.logging.log4j.Logger;
import app.common.LoggableStatement;
import app.common.StreamGobbler;
import app.common.SystemPath;
import app.common.eCAMSInfo;

import com.ecams.common.dbconn.ConnectionContext;
import com.ecams.common.dbconn.ConnectionResource;
import com.ecams.common.logger.EcamsLogger;
import app.common.SysInfo;


/**
 * @author bigeyes
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */


public class Cmd0500{
    /**
     * Logger Class Instance Creation
     * logger
     */
	Logger ecamsLogger  = EcamsLogger.getLoggerInstance();
    
	/**
	 * project 등록
	 * @param  pPrjno,pDocname,pDocPath
	 * @return String
	 * @throws SQLException
	 * @throws Exception
	 */
	
    public Object[] Cmd0500_Cbo_Set(String L_Syscd,String SecuYn,String UserId,String ItemId) throws SQLException, Exception {
		Connection        conn        = null;
		PreparedStatement pstmt       = null;
		ResultSet         rs          = null;
		StringBuffer      strQuery    = new StringBuffer();
		ArrayList<HashMap<String, String>>  rsval = new ArrayList<HashMap<String, String>>();
		HashMap<String, String>			  rst		  = null;
		Object[]		  rtObj		  = null;
		int             parmCnt       = 0;
		
		ConnectionContext connectionContext = new ConnectionResource();
		try {
			conn = connectionContext.getConnection();
			
			strQuery.setLength(0);
			strQuery.append("SELECT c.cm_jobcd,c.cm_jobname from cmm0102 c,cmm0034 a   \n");
			strQuery.append(" where a.cm_syscd=? and a.cm_jobcd=c.cm_jobcd             \n");
			if (SecuYn.equals("N")) {
				strQuery.append("and a.cm_jobcd in (select cm_jobcd from cmm0044       \n");
				strQuery.append("                    where cm_syscd=? and cm_userid=?  \n");
				strQuery.append("                      and cm_closedt is null)         \n");
			}
			if (!"".equals(ItemId) && ItemId != null && SecuYn.equals("N")) {
				strQuery.append("union                                                   \n");
				strQuery.append("select d.cm_jobcd,d.cm_jobname from cmm0102 d,cmr0020 b \n"); 
				strQuery.append(" where b.cr_itemid=? and b.cr_jobcd=d.cm_jobcd          \n"); 
				strQuery.append(" group by cm_jobcd,cm_jobname \n");
				strQuery.append(" order by cm_jobcd,cm_jobname \n");
			} else {
				strQuery.append(" group by c.cm_jobcd,c.cm_jobname \n");
				strQuery.append(" order by c.cm_jobcd,c.cm_jobname \n");
			}
			
			//pstmt =  new LoggableStatement(conn, strQuery.toString());	
            pstmt = conn.prepareStatement(strQuery.toString());
		    pstmt.setString(++parmCnt, L_Syscd);
		    if (SecuYn.equals("N")) {
		    	pstmt.setString(++parmCnt, L_Syscd);
		    	pstmt.setString(++parmCnt, UserId);
		    }
		    if (!"".equals(ItemId) && ItemId != null && SecuYn.equals("N")) 
		    	pstmt.setString(++parmCnt, ItemId);
		    ////ecamsLogger.error(((LoggableStatement)pstmt).getQueryString());
            rs = pstmt.executeQuery();
			while (rs.next()){
				rst = new HashMap<String, String>();
				rst.put("ID", "Cbo_Job");
				rst.put("cm_jobcd", rs.getString("cm_jobcd"));
				rst.put("cm_jobname", rs.getString("cm_jobname"));
	           	rsval.add(rst);
	           	rst = null;
			}
			rs.close();
			pstmt.close();
			
			strQuery.setLength(0);
			strQuery.append("select a.cm_micode,a.cm_codename,b.cm_info,d.cm_volpath,b.cm_exename \n");
			strQuery.append("  from cmm0020 a,cmm0036 b,cmm0031 c,cmm0038 d          \n");
			strQuery.append(" where b.cm_syscd=?                                     \n");
			strQuery.append("   and b.cm_closedt is null                             \n");
			strQuery.append("   and substr(b.cm_info,26,1)='0'                       \n");
			strQuery.append("   and b.cm_rsrccd not in (select cm_samersrc           \n");
			strQuery.append("                             from cmm0037               \n");
			strQuery.append("                            where cm_syscd=?            \n");
			strQuery.append("                              and cm_factcd='04')       \n");
			strQuery.append("   and a.cm_macode='JAWON' and a.cm_micode=b.cm_rsrccd  \n");
			strQuery.append("   and b.cm_syscd=d.cm_syscd and b.cm_rsrccd=d.cm_rsrccd\n");
			strQuery.append("   and d.cm_svrcd='01'                                  \n");
			strQuery.append("   and d.cm_syscd=c.cm_syscd and d.cm_svrcd=c.cm_svrcd  \n");
			strQuery.append("   and d.cm_seqno=c.cm_seqno and c.cm_closedt is null   \n");
            pstmt = conn.prepareStatement(strQuery.toString());
			pstmt.setString(1, L_Syscd);
			pstmt.setString(2, L_Syscd);
			////ecamsLogger.error(((LoggableStatement)pstmt).getQueryString());
            rs = pstmt.executeQuery();
			while (rs.next()){
				rst = new HashMap<String, String>();
				rst.put("ID", "Cbo_RsrcCd");
				rst.put("cm_micode", rs.getString("cm_micode"));
				rst.put("cm_codename", rs.getString("cm_codename"));
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
			rsval.clear();
			rsval = null;
			
			return rtObj;
			
			
		} catch (SQLException sqlexception) {
			if (conn != null){
				conn.close();conn = null;
			}
			sqlexception.printStackTrace();
			throw sqlexception;
		} catch (Exception exception) {
			if (conn != null){
				conn.close();conn = null;
			}
			exception.printStackTrace();
			throw exception;
		}finally{
			if (strQuery != null)	strQuery = null;
			if (rtObj != null)	rtObj = null;
			if (rs != null)     try{rs.close();}catch (Exception ex){ex.printStackTrace();}
			if (pstmt != null)  try{pstmt.close();}catch (Exception ex2){ex2.printStackTrace();}
			if (conn != null){
				try{
					ConnectionResource.release(conn);
				}catch(Exception ex3){
					ex3.printStackTrace();
				}
			}
		}
	}//end of getCbo_Set() method statement
    
    public Object[] getCbo_SysCd(String UserId,String SecuYn,String WkSys) throws SQLException, Exception {
		Connection        conn        = null;
		PreparedStatement pstmt       = null;
		ResultSet         rs          = null;
		StringBuffer      strQuery    = new StringBuffer();
		ArrayList<Hashtable<String, String>>  rsval = new ArrayList<Hashtable<String, String>>();
		Hashtable<String, String>			  rst  = null;
		Object[]		  rtObj		  = null;
		
		ConnectionContext connectionContext = new ConnectionResource();
		try {
			conn = connectionContext.getConnection();
			SysInfo sysinfo = new SysInfo();
			
			strQuery.setLength(0);
			strQuery.append("select a.cm_syscd,a.cm_sysmsg,a.cm_sysgb \n");
	        if (!SecuYn.equals("Y") && ("".equals(WkSys) || WkSys == null)){
	        	strQuery.append("from cmm0044 b, cmm0030 a where \n");
	        	strQuery.append("b.cm_userid=? and \n");//UserId
	        	strQuery.append("a.cm_syscd=b.cm_syscd and \n");
	        	strQuery.append("b.cm_closedt is null and \n");
	        //}else if (!Sv_Admin.toUpperCase().equals("Y") && ViewFg.equals("true")){
	        }else if (!"".equals(WkSys) && WkSys != null){
	        	strQuery.append("from cmm0030 a where \n");
	           	strQuery.append(" a.cm_syscd=? and \n");//WkSys
	        }else{
	        	strQuery.append("from cmm0030 a where \n");
	        }
	        strQuery.append("a.cm_closedt is null and substr(a.cm_sysinfo,1,1)='0' \n");
	        strQuery.append("group by a.cm_syscd,a.cm_sysmsg,a.cm_sysgb \n");
	        strQuery.append("order by a.cm_syscd,a.cm_sysmsg,a.cm_sysgb \n");
	        
	        pstmt = conn.prepareStatement(strQuery.toString());
	        //pstmt = new LoggableStatement(conn,strQuery.toString());    
            
		    int Cnt = 0;
		    int tstCnt = 0;
	        if (!SecuYn.equals("Y") && ("".equals(WkSys) || WkSys == null)){
	        	pstmt.setString(++Cnt, UserId);
	        }else if (!"".equals(WkSys) && WkSys != null){
	        	pstmt.setString(++Cnt, WkSys);
	        }
	        
	        //ecamsLogger.debug(((LoggableStatement)pstmt).getQueryString());
		    rs = pstmt.executeQuery();
		    
			while (rs.next()){
				tstCnt = 0;
				rst = new Hashtable<String, String>();
				rst.put("cm_sysmsg", rs.getString("cm_sysmsg"));
				rst.put("cm_sysgb", rs.getString("cm_sysgb"));
				rst.put("cm_syscd", rs.getString("cm_syscd"));
				
				if (sysinfo.getTstSys_conn(rs.getString("cm_syscd"),conn) > 0) tstCnt = 1;
				rst.put("TstSw", Integer.toString(tstCnt));
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
			rsval.clear();
			rsval = null;

			return rtObj;
			
		} catch (SQLException sqlexception) {
			if (conn != null){
				conn.close();conn = null;
			}
			sqlexception.printStackTrace();			
			throw sqlexception;
		} catch (Exception exception) {
			if (conn != null){
				conn.close();conn = null;
			}
			exception.printStackTrace();				
			throw exception;
		}finally{
			if (strQuery != null)	strQuery = null;
			if (rtObj != null)	rtObj = null;			
			if (rs != null)     try{rs.close();}catch (Exception ex){ex.printStackTrace();}
			if (pstmt != null)  try{pstmt.close();}catch (Exception ex2){ex2.printStackTrace();}
			if (conn != null){
				try{
					ConnectionResource.release(conn);
				}catch(Exception ex3){
					ex3.printStackTrace();
				}
			}
		}
	}//end of getCbo_SysCd() method statement
    
    public Object[] getCbo_LangCd(String L_SysCd,String RsrcCd) throws SQLException, Exception {
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
			strQuery.append("SELECT b.cm_micode,b.cm_codename from cmm0020 b,cmm0032 a where \n");
			strQuery.append(" a.cm_syscd=? and \n");	//L_Syscd
			strQuery.append(" a.cm_rsrccd=? and \n");	//L_Rsrccd
			strQuery.append(" b.cm_macode='LANGUAGE' and \n");
			strQuery.append(" a.cm_langcd=b.cm_micode \n");
			strQuery.append(" order by b.cm_micode,b.cm_codename \n");
            pstmt = conn.prepareStatement(strQuery.toString());
		    //pstmt =  new LoggableStatement(conn, strQuery.toString());
	    	pstmt.setString(1, L_SysCd);
	    	pstmt.setString(2, RsrcCd);
	    	////ecamsLogger.error(((LoggableStatement)pstmt).getQueryString());
            rs = pstmt.executeQuery();
            while (rs.next()){
				rst = new HashMap<String, String>();
				//rst.put("ID", "Cbo_LangCd");
				rst.put("cm_micode", rs.getString("cm_micode"));
				rst.put("cm_codename", rs.getString("cm_codename"));
	           	rsval.add(rst);
	           	rst = null;
			}
			rs.close();
			pstmt.close();
			conn.close();
			rs=null;
			pstmt=null;
			conn=null;
			
			rtObj = rsval.toArray();
			rsval.clear();
			rsval = null;
			return rtObj;			
			
		} catch (SQLException sqlexception) {
			sqlexception.printStackTrace();
			throw sqlexception;
		} catch (Exception exception) {
			exception.printStackTrace();
			throw exception;
		}finally{
			if (strQuery != null)	strQuery = null;
			if (rtObj != null)	rtObj = null;				
			if (rs != null)     try{rs.close();}catch (Exception ex){ex.printStackTrace();}
			if (pstmt != null)  try{pstmt.close();}catch (Exception ex2){ex2.printStackTrace();}
			if (conn != null){
				try{
					ConnectionResource.release(conn);
				}catch(Exception ex3){
					ex3.printStackTrace();
				}
			}
		}
	}//end of getCbo_LangCd() method statement
    
    
    public Object[] getDir_Check(String UserId,String SecuYn,String L_Syscd,
			String L_ItemId,String RsrcCd,String L_DsnCd,String FindFg) throws SQLException, Exception {
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
			strQuery.append("select a.cm_dirpath,a.cm_dsncd \n");
		    if (!SecuYn.equals("Y")){
		        strQuery.append("from cmm0070 a,cmm0073 b,cmm0072 c,cmm0044 d,cmr0020 e \n");
		        strQuery.append("where e.cr_itemid=?                                    \n");		
		        strQuery.append("  and e.cr_syscd=d.cm_syscd and e.cr_jobcd=d.cm_jobcd  \n");
		        strQuery.append("  and d.cm_userid=?                                    \n");	
		        strQuery.append("  and d.cm_syscd=b.cm_syscd and d.cm_jobcd=b.cm_jobcd  \n");
		    } else{
		        strQuery.append(" from cmm0070 a,cmm0073 b,cmm0072 c,cmr0020 e          \n");
		        strQuery.append("where e.cr_itemid=?                                    \n");
		        strQuery.append("  and e.cr_syscd=b.cm_syscd and e.cr_jobcd=b.cm_jobcd  \n");
		    }
	        strQuery.append("and a.cm_syscd=b.cm_syscd and a.cm_dsncd=b.cm_dsncd        \n");
	        strQuery.append("and a.cm_syscd=c.cm_syscd and a.cm_dsncd=c.cm_dsncd        \n");
	        strQuery.append("and c.cm_rsrccd=?                                          \n");
		    
	        pstmt = conn.prepareStatement(strQuery.toString());
	        //pstmt =  new LoggableStatement(conn, strQuery.toString());
	        int CNT = 0;
	        pstmt.setString(++CNT, L_ItemId);
		    if (!SecuYn.equals("Y")){
		    	pstmt.setString(++CNT, UserId);
		    }	    
			pstmt.setString(++CNT, RsrcCd);
			////ecamsLogger.error(((LoggableStatement)pstmt).getQueryString());
	        rs = pstmt.executeQuery();
			while (rs.next()){
				if (!"".equals(L_DsnCd) && FindFg.equals("false")){
					if (L_DsnCd.equals(rs.getString("cm_dsncd"))) FindFg = "true";
				}	             
				rst = new HashMap<String, String>();
				rst.put("ID", "Cbo_Dir");				
				rst.put("cm_dsncd", rs.getString("cm_dsncd"));
				rst.put("cm_dirpath", rs.getString("cm_dirpath"));
	           	rsval.add(rst);
	           	rst = null;
			}
			rs.close();
			pstmt.close();
			
			if (FindFg.equals("false")){
				strQuery.setLength(0);
	    	   	strQuery.append("select cm_dirpath from cmm0070 where \n");
	    	   	strQuery.append(" cm_syscd=? and \n");	//L_Syscd
	    	   	strQuery.append(" cm_dsncd=? \n");	//L_DsnCd
				pstmt = conn.prepareStatement(strQuery.toString());
				pstmt.setString(1, L_Syscd);
				pstmt.setString(2, L_DsnCd);				
				rs = pstmt.executeQuery();
				if (rs.next()){
					rst = new HashMap<String, String>();
					rst.put("ID", "Cbo_Dir");
					rst.put("cm_dsncd", L_DsnCd);
					rst.put("cm_dirpath", rs.getString("cm_dirpath"));
		           	rsval.add(rst);
		           	rst = null;
				}
				rs.close();
				pstmt.close();

			}
			conn.close();
			rs = null;
			pstmt = null;
			conn = null;
			
			rtObj = rsval.toArray();
			rsval.clear();
			rsval = null;
			
			return rtObj;			
			
		} catch (SQLException sqlexception) {
			if (conn != null){
				conn.close();conn = null;
			}
			sqlexception.printStackTrace();
			throw sqlexception;
		} catch (Exception exception) {
			if (conn != null){
				conn.close();conn = null;
			}
			exception.printStackTrace();
			throw exception;
		}finally{
			if (strQuery != null)	strQuery = null;
			if (rtObj != null)	rtObj = null;				
			if (rs != null)     try{rs.close();}catch (Exception ex){ex.printStackTrace();}
			if (pstmt != null)  try{pstmt.close();}catch (Exception ex2){ex2.printStackTrace();}
			if (conn != null){
				try{
					ConnectionResource.release(conn);
				}catch(Exception ex3){
					ex3.printStackTrace();
				}
			}
		}
	}//end of getCbo_Set() method statement

    
    //검색창에 공백 제거
    public static String allTrim(String s)
    {
        if (s == null)
            return null;
        else if (s.length() == 0)
            return "";

        int len = s.length();
        int i = 0;
        int j = len;

        for (i = 0; i < len; i++) {
            if ( s.charAt(i) != ' ' && s.charAt(i) != 't' && s.charAt(i) != 'r' && s.charAt(i) != 'n' )
                break;
        }
        if (i == len)
            return "";

        for (j = len - 1; j >= i; j--) {
            if ( s.charAt(i) != ' ' && s.charAt(i) != 't' && s.charAt(i) != 'r' && s.charAt(i) != 'n' )
                break;
        }
        return s.substring(i, j + 1);
    }


    public Object[] getSql_Qry(String UserId,String SecuYn,String ViewFg, String L_Syscd,String Txt_ProgId,String DsnCd, String DirPath, boolean LikeSw) 
		throws SQLException, Exception {
    	
		allTrim(Txt_ProgId);
		
		Connection        conn        = null;
		PreparedStatement pstmt       = null;
		ResultSet         rs          = null;
		StringBuffer      strQuery    = new StringBuffer();
		ArrayList<Hashtable<String, String>>  rtList = new ArrayList<Hashtable<String, String>>();
		Hashtable<String, String>			  rst    = null;
		Object[]		  rtObj		  = null;
		
		ConnectionContext connectionContext = new ConnectionResource();
		try {
			conn = connectionContext.getConnection();
			String			  strRsrcCd = "";
			int               i = 0;
			boolean           findSw = false;
			
			strQuery.setLength(0);
			strQuery.append("select cm_rsrccd from cmm0036                     \n"); 
			strQuery.append(" where cm_syscd=? and cm_closedt is null          \n");
			strQuery.append("   and substr(cm_info, 2, 1)='1'                  \n");
			strQuery.append("   and substr(cm_info, 26, 1)='0'                 \n");
			strQuery.append("   and cm_rsrccd not in (select distinct cm_samersrc \n");
			strQuery.append("                           from cmm0037           \n");
			strQuery.append("                          where cm_syscd=?)       \n");
			
			pstmt = conn.prepareStatement(strQuery.toString());	
			pstmt =  new LoggableStatement(conn, strQuery.toString());			
            pstmt.setString(1, L_Syscd);			
            pstmt.setString(2, L_Syscd);
            
            ecamsLogger.error(((LoggableStatement)pstmt).getQueryString());
            rs = pstmt.executeQuery();
            while (rs.next()) {
            	if (!"".equals(strRsrcCd)) strRsrcCd = strRsrcCd + ",";
            	strRsrcCd = strRsrcCd + rs.getString("cm_rsrccd");            	
            }
			rs.close();
			pstmt.close();
			
			//strRsrc = strRsrcCd.split(",");
			
			if (strRsrcCd.length() > 0) {
				strQuery.setLength(0);
				strQuery.append("select a.cr_syscd,a.cr_dsncd,a.cr_rsrcname,a.cr_jobcd,a.cr_itemid,   					\n");
				strQuery.append("       a.cr_lstver,b.cm_dirpath,c.cm_jobname,a.cr_status,a.cr_story,   				\n");			
				strQuery.append("       a.cr_rsrccd,d.cm_codename,e.cm_codename as sta,g.cm_info,      					\n");
				strQuery.append("       nvl(a.cr_compile,'01') as cr_compile,nvl(a.cr_teamcd,'000010505') as cr_teamcd, \n");
				strQuery.append("       nvl(a.cr_document,'01') as cr_document  \n");
		        strQuery.append("from cmr0020 a,cmm0070 b,cmm0102 c,cmm0020 d,cmm0020 e,cmm0036 g     \n");
		        if (!SecuYn.equals("Y") && ViewFg.equals("false")){
					strQuery.append(",cmm0044 k \n"); 
				}
		        strQuery.append("where a.cr_syscd=?                                                     \n");
		        if (!SecuYn.equals("Y") && ViewFg.equals("false")){
		        	strQuery.append("and a.cr_syscd=k.cm_syscd and k.cm_userid=? and k.cm_closedt is null and k.cm_jobcd=a.cr_jobcd \n");
		        }
		        if (!"".equals(DsnCd) && DsnCd != null) {
			    	strQuery.append("and a.cr_dsncd=?                                  \n");	//L_Dsncd
			    	strQuery.append("and a.cr_rsrcname=?                               \n");	// %Txt_ProgId%
		        } else strQuery.append("and upper(a.cr_rsrcname) like upper(?)         \n");	// %Txt_ProgId%
			    /*
		        strQuery.append("and a.cr_rsrccd in (");
				for (i=0;strRsrc.length>i;i++) {
					if (i>0) strQuery.append(", ? ");
					else strQuery.append("? ");
				}
				strQuery.append(")                                                     \n");
				*/
			    strQuery.append("and a.cr_syscd=b.cm_syscd and a.cr_dsncd=b.cm_dsncd   \n");
			    strQuery.append("and a.cr_jobcd=c.cm_jobcd                             \n");
			    strQuery.append("and d.cm_macode='JAWON' and a.cr_rsrccd=d.cm_micode   \n");
			    strQuery.append("and e.cm_macode='CMR0020' and a.cr_status=e.cm_micode \n");
			    strQuery.append("and a.cr_syscd=g.cm_syscd and a.cr_rsrccd=g.cm_rsrccd \n");
			    
			    if(!"".equals(DirPath) && DirPath != null){ //프로그램 경로 검색 추가
			    	strQuery.append("and b.cm_dirpath like ? ");
			    }
			    
//			    pstmt = conn.prepareStatement(strQuery.toString());
		        pstmt =  new LoggableStatement(conn, strQuery.toString());
		        int CNT = 0;
			    pstmt.setString(++CNT, L_Syscd);
			    if (!SecuYn.equals("Y") && ViewFg.equals("false")){
			    	pstmt.setString(++CNT, UserId);
			    }
			    if (!"".equals(DsnCd) && DsnCd != null) {
			    	pstmt.setString(++CNT, DsnCd);
			    	pstmt.setString(++CNT, Txt_ProgId);
			    } else pstmt.setString(++CNT, "%"+Txt_ProgId+"%");
			    
			    if(!"".equals(DirPath) && DirPath != null){ 
			    	if(LikeSw == true)pstmt.setString(++CNT, "%"+DirPath+"%"); //like검색 체크
			    	else pstmt.setString(++CNT, DirPath);			    	   //like검색 미체크
			    }
			    /*
			    for (i=0;strRsrc.length>i;i++) {
	        		pstmt.setString(++CNT, strRsrc[i]);
	        	}*/
			    ecamsLogger.error(((LoggableStatement)pstmt).getQueryString());
	            rs = pstmt.executeQuery();
	            
	            rtList.clear();
				while (rs.next()){
//					ecamsLogger.error("strRsrcCd : "+strRsrcCd);
//					if (strRsrcCd.indexOf(rs.getString("cr_rsrccd"))>=0) findSw = true;
//					else findSw = false;
					 
					//if (findSw) {
						rst = new Hashtable<String,String>();
						rst.put("cm_jobname",rs.getString("cm_jobname"));
						rst.put("cm_dirpath",rs.getString("cm_dirpath"));
						rst.put("cm_codename",rs.getString("cm_codename"));
						rst.put("cr_rsrcname",rs.getString("cr_rsrcname"));
						rst.put("cr_lstver",rs.getString("cr_lstver"));
						rst.put("sta",rs.getString("sta"));
						rst.put("cr_dsncd",rs.getString("cr_dsncd"));
						rst.put("cr_syscd",rs.getString("cr_syscd"));
						rst.put("cr_jobcd",rs.getString("cr_jobcd"));
						rst.put("cr_itemid",rs.getString("cr_itemid"));
						rst.put("baseitem",rs.getString("cr_itemid"));
						rst.put("cm_info", rs.getString("cm_info"));
						rst.put("cr_status", rs.getString("cr_status"));
						rst.put("cr_compile", rs.getString("cr_compile"));
						rst.put("cr_teamcd", rs.getString("cr_teamcd"));
						rst.put("cr_document", rs.getString("cr_document"));
						rst.put("subset", "N");
						rst.put("base","0");
						if("".equals(rs.getString("cr_story")) || rs.getString("cr_story") == null) rst.put("cr_story", "");
						else rst.put("cr_story", rs.getString("cr_story"));
						rtList.add(rst);
						rst = null;
					//}
				}//end of while-loop statement
				rs.close();
				pstmt.close();
			}
			conn.close();
			rs = null;
			pstmt = null;
			conn = null;
			
			rtObj = rtList.toArray();
			rtList.clear();
			rtList = null;
						
			return rtObj;
			
		} catch (SQLException sqlexception) {
			if (conn != null){
				conn.close();conn = null;
			}
			sqlexception.printStackTrace();
			throw sqlexception;
		} catch (Exception exception) {
			if (conn != null){
				conn.close();conn = null;
			}
			exception.printStackTrace();
			throw exception;
		}finally{
			if (rtObj != null)	rtObj = null;
			if (strQuery != null)	strQuery = null;			
			if (rs != null)     try{rs.close();}catch (Exception ex){ex.printStackTrace();}
			if (pstmt != null)  try{pstmt.close();}catch (Exception ex2){ex2.printStackTrace();}
			if (conn != null){
				try{
					ConnectionResource.release(conn);
				}catch(Exception ex3){
					ex3.printStackTrace();
				}
			}
		}
	}//end of getSql_Qry() method statement
	

	public Object[] getSql_Qry_Sub(String UserId,String ItemId,String SysCd) 
    	throws SQLException, Exception {
    	
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
			strQuery.append("select a.cr_syscd,a.cr_dsncd,a.cr_rsrcname,a.cr_jobcd,  \n");
			strQuery.append("       a.cr_itemid,a.cr_lstver,b.cm_dirpath,c.cm_jobname\n");
			strQuery.append("       ,d.cm_codename,e.cm_codename as sta,g.cm_info    \n");		
	        strQuery.append("from cmr0020 a,cmm0070 b,cmm0102 c,cmm0020 d,cmm0020 e, \n");		
	        strQuery.append("     cmm0036 g,cmr1010 f,cmr0020 h                      \n");
	        strQuery.append("where h.cr_itemid=? and h.cr_acptno=f.cr_acptno         \n");
	        strQuery.append("  and h.cr_itemid=f.cr_baseitem                      \n");
	        strQuery.append("  and f.cr_itemid<>?                                    \n");
	        strQuery.append("  and f.cr_rsrccd not in (select cm_samersrc            \n");
	        strQuery.append("                            from cmm0037                \n");
	        strQuery.append("                           where cm_syscd=?)            \n");
	        strQuery.append("  and f.cr_itemid=a.cr_itemid                           \n");
		    strQuery.append("  and a.cr_syscd=b.cm_syscd and a.cr_dsncd=b.cm_dsncd   \n");
		    strQuery.append("  and a.cr_jobcd=c.cm_jobcd                             \n");
		    strQuery.append("  and d.cm_macode='JAWON' and a.cr_rsrccd=d.cm_micode   \n");
		    strQuery.append("  and e.cm_macode='CMR0020' and a.cr_status=e.cm_micode \n");
		    strQuery.append("  and a.cr_syscd=g.cm_syscd and a.cr_rsrccd=g.cm_rsrccd \n");
		    
		    pstmt = conn.prepareStatement(strQuery.toString());
	        //pstmt =  new LoggableStatement(conn, strQuery.toString());
		    pstmt.setString(1,ItemId );
		    pstmt.setString(2,ItemId );
		    pstmt.setString(3,SysCd );
	        ////ecamsLogger.error(((LoggableStatement)pstmt).getQueryString());
            rs = pstmt.executeQuery();
            rtList.clear();
			while (rs.next()){
				rst = new HashMap<String,String>();
				rst.put("cm_jobname",rs.getString("cm_jobname"));
				rst.put("cm_dirpath",rs.getString("cm_dirpath"));
				rst.put("cm_codename",rs.getString("cm_codename"));
				rst.put("cr_rsrcname",rs.getString("cr_rsrcname"));
				rst.put("cr_lstver",rs.getString("cr_lstver"));
				rst.put("sta",rs.getString("sta"));
				rst.put("cr_dsncd",rs.getString("cr_dsncd"));
				rst.put("cr_syscd",rs.getString("cr_syscd"));
				rst.put("cr_jobcd",rs.getString("cr_jobcd"));
				rst.put("cr_itemid",rs.getString("cr_itemid"));  
				rst.put("cm_info", rs.getString("cm_info"));  
				rst.put("baseitem",ItemId);   
				rst.put("base","1"); 
				rst.put("subset", "N");
				rtList.add(rst);
				rst = null;
			}//end of while-loop statement
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
			if (conn != null){
				conn.close();conn = null;
			}
			sqlexception.printStackTrace();
			ecamsLogger.error("## Cmd0500.getSql_Qry_Sub() SQLException START ##");
			ecamsLogger.error("## Error DESC : ", sqlexception);	
			ecamsLogger.error("## Cmd0500.getSql_Qry_Sub() SQLException END ##");			
			throw sqlexception;
		} catch (Exception exception) {
			if (conn != null){
				conn.close();conn = null;
			}
			exception.printStackTrace();
			ecamsLogger.error("## Cmd0500.getSql_Qry_Sub() Exception START ##");				
			ecamsLogger.error("## Error DESC : ", exception);	
			ecamsLogger.error("## Cmd0500.getSql_Qry_Sub() Exception END ##");				
			throw exception;
		}finally{
			if (rtObj != null)	rtObj = null;
			if (strQuery != null)	strQuery = null;			
			if (rs != null)     try{rs.close();}catch (Exception ex){ex.printStackTrace();}
			if (pstmt != null)  try{pstmt.close();}catch (Exception ex2){ex2.printStackTrace();}
			if (conn != null){
				try{
					ConnectionResource.release(conn);
				}catch(Exception ex3){
					ecamsLogger.error("## Cmd0500.getSql_Qry_Sub() connection release exception ##");
					ex3.printStackTrace();
				}
			}
		}
	}//end of getSql_Qry_Sub() method statement
	
	public String getProgInfo(String ItemId) throws SQLException, Exception {
    	
		Connection        conn        = null;
		PreparedStatement pstmt       = null;
		ResultSet         rs          = null;
		StringBuffer      strQuery    = new StringBuffer();
		String            strMsg      = "";
		ConnectionContext connectionContext = new ConnectionResource();
		try {
			conn = connectionContext.getConnection();
			
			strQuery.setLength(0);
			strQuery.append("select cr_syscd,cr_rsrcname,cr_dsncd from cmr0020     \n");
		    strQuery.append(" where cr_itemid=?                                    \n");
		    
	        //pstmt =  new LoggableStatement(conn, strQuery.toString());
            pstmt = conn.prepareStatement(strQuery.toString());
            pstmt.setString(1, ItemId);		    
	        ////ecamsLogger.error(((LoggableStatement)pstmt).getQueryString());
            rs = pstmt.executeQuery();
			if (rs.next()){
				strMsg = rs.getString("cr_syscd");
				strMsg = strMsg+rs.getString("cr_dsncd");
				strMsg = strMsg+rs.getString("cr_rsrcname");
			}//end of while-loop statement
			rs.close();
			pstmt.close();
			conn.close();
			rs = null;
			pstmt = null;
			conn = null;
			
			return strMsg;
			
		} catch (SQLException sqlexception) {
			if (conn != null){
				conn.close();conn = null;
			}
			sqlexception.printStackTrace();
			throw sqlexception;
		} catch (Exception exception) {
			if (conn != null){
				conn.close();conn = null;
			}
			exception.printStackTrace();
			throw exception;
		}finally{
			if (strQuery != null)	strQuery = null;
			if (rs != null)     try{rs.close();}catch (Exception ex){ex.printStackTrace();}
			if (pstmt != null)  try{pstmt.close();}catch (Exception ex2){ex2.printStackTrace();}
			if (conn != null){
				try{
					ConnectionResource.release(conn);
				}catch(Exception ex3){
					ex3.printStackTrace();
				}
			}
		}
	}//end of getSql_Qry() method statement
	
    public Object[] getItemId(String ItemId) throws SQLException, Exception {	
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
			strQuery.append("select cr_syscd, cr_rsrcname from cmr0020 \n");
			strQuery.append("where cr_itemid=? \n");//ItemId			
	        pstmt = conn.prepareStatement(strQuery.toString());
		    pstmt.setString(1, ItemId);
	        rs = pstmt.executeQuery();
			if (rs.next()){
				rst = new HashMap<String,String>();
				rst.put("cr_syscd",rs.getString("cr_syscd"));
				rst.put("cr_rsrcname",rs.getString("cr_rsrcname"));
				rtList.add(rst);
				rst = null;
			}//end of while-loop statement
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
			if (conn != null){
				conn.close();conn = null;
			}
			sqlexception.printStackTrace();
			throw sqlexception;
		} catch (Exception exception) {
			if (conn != null){
				conn.close();conn = null;
			}
			exception.printStackTrace();
			throw exception;
		}finally{
			if (strQuery != null)	strQuery = null;
			if (rtObj != null)	rtObj = null;				
			if (rs != null)     try{rs.close();}catch (Exception ex){ex.printStackTrace();}
			if (pstmt != null)  try{pstmt.close();}catch (Exception ex2){ex2.printStackTrace();}
			if (conn != null){
				try{
					ConnectionResource.release(conn);
				}catch(Exception ex3){
					ex3.printStackTrace();
				}
			}
		}
	}//end of getItemId() method statement
    
    public Object[] Cmd0500_Lv_File_ItemClick(String UserId,String SecuYn,String L_SysCd,String L_JobCd,
    		String L_ItemId)	throws SQLException, Exception {
	
		Connection        conn        = null;
		PreparedStatement pstmt       = null;
		PreparedStatement pstmt2       = null;
		ResultSet         rs          = null;
		ResultSet         rs2          = null;
		StringBuffer      strQuery    = new StringBuffer();
		ArrayList<HashMap<String, String>>  rtList = new ArrayList<HashMap<String, String>>();
		HashMap<String, String>			  rst		  = null;
		Object[]		  rtObj		  = null;
		String            strJobCd    = null;
		
		ConnectionContext connectionContext = new ConnectionResource();
		try {
			conn = connectionContext.getConnection();
			
			strQuery.setLength(0);
			//ecamsLogger.debug("+++++++SecuYn++++"+SecuYn+", "+UserId);		    
    		strQuery.setLength(0);
    		strQuery.append("select a.cr_rsrcname,a.cr_story,a.cr_langcd,a.cr_editor,a.cr_master,d.cm_username as masname,\n");
    		strQuery.append("       to_char(a.cr_opendate,'yyyy/mm/dd hh24:mi') cr_opendate, 					\n");
    		strQuery.append("       to_char(a.cr_lastdate,'yyyy/mm/dd hh24:mi') cr_lastdate, 					\n");
    		strQuery.append("       a.cr_jobcd,a.cr_creator,a.cr_status,a.cr_lstver,         					\n");
    		strQuery.append("       a.cr_rsrccd,a.cr_lstusr,a.cr_orderid,b.cm_info,				\n");
    		strQuery.append("       nvl(a.cr_rpaflag,'N') cr_rpaflag,					\n");
    		strQuery.append("       to_char(a.cr_lstdat,'yyyy/mm/dd hh24:mi') cr_lstdat,     					\n");
    		strQuery.append("       c.cm_codename,e.cnt,a.cr_compile,a.cr_makecompile,cr_teamcd,a.cr_document, f.cm_codename as fname,\n");
    		strQuery.append("       (select cm_codename from cmm0020 											\n");
    		strQuery.append("         where cm_macode='CMR0020' and cm_micode=a.cr_status) sta					\n");
    		strQuery.append("  from cmm0020 c,cmm0036 b,cmr0020 a,cmm0040 d,         							\n");
    		strQuery.append("       (select cm_micode, cm_codename from cmm0020 where cm_macode='LANGUAGE') f,	\n");
    		strQuery.append("       (select count(*) cnt from cmr0021                        					\n");
    		strQuery.append("         where cr_Itemid=?) e                                   					\n");
    		strQuery.append(" where a.cr_itemid=?                                            					\n");
    		strQuery.append("   and a.cr_syscd=b.cm_syscd and a.cr_rsrccd=b.cm_rsrccd        					\n");
    		strQuery.append("   and c.cm_macode='JAWON' and c.cm_micode=a.cr_rsrccd          					\n");
    		strQuery.append("   and a.cr_langcd=f.cm_micode                					                    \n");
    		strQuery.append("   and nvl(a.cr_master,a.cr_editor) = d.cm_userid	 	          					\n");
    		
		    pstmt = conn.prepareStatement(strQuery.toString());
		    pstmt =  new LoggableStatement(conn, strQuery.toString());
	    	pstmt.setString(1, L_ItemId);
	    	pstmt.setString(2, L_ItemId);
	    	ecamsLogger.error(((LoggableStatement)pstmt).getQueryString());
	    	rs = pstmt.executeQuery();
	    	if (rs.next()) {	
				rst = new HashMap<String,String>();				
				rst.put("ID","Sql_Qry_Prog1");
				rst.put("Lbl_ProgName",rs.getString("cr_story"));
				rst.put("Lbl_CreatDt",rs.getString("cr_opendate"));
				rst.put("Lbl_LastDt",rs.getString("cr_lastdate"));
				rst.put("WkJobCd",rs.getString("cr_jobcd"));
				rst.put("WkSta",rs.getString("cr_status"));
				rst.put("cm_info",rs.getString("cm_info"));
				rst.put("WkVer",Integer.toString(rs.getInt("cr_lstver")));
				rst.put("WkRsrcCd",rs.getString("cr_rsrccd"));
				rst.put("Lbl_LstDat",rs.getString("cr_lstdat"));
				rst.put("cr_editor",rs.getString("cr_editor"));
				rst.put("RsrcName", rs.getString("cm_codename"));
				rst.put("cr_orderid", rs.getString("cr_orderid"));
				rst.put("cr_compile", rs.getString("cr_compile"));
				rst.put("cr_makecompile", rs.getString("cr_makecompile"));
				rst.put("cr_teamcd", rs.getString("cr_teamcd"));
				rst.put("cr_document", rs.getString("cr_document"));
				rst.put("vercnt", rs.getString("cnt"));
				rst.put("fname", rs.getString("fname"));
				rst.put("cr_master", rs.getString("cr_master"));
				rst.put("cr_masname", rs.getString("masname"));
				rst.put("sta", rs.getString("sta"));
				rst.put("cr_rpaflag", rs.getString("cr_rpaflag"));
				
				if (SecuYn.equals("Y") || UserId.equals(rs.getString("cr_editor"))) {
					rst.put("WkSecu","true");
				} else  rst.put("WkSecu","false");
				
				if (rs.getString("cr_orderid")!=null && !rs.getString("cr_orderid").equals("0000")) {
					strQuery.setLength(0);					
	        	   	strQuery.append("select cc_reqsub from cmc0420 where cc_orderid=? \n");
	        	   	pstmt2 = conn.prepareStatement(strQuery.toString());
	        	   	pstmt2.setString(1, rs.getString("cr_orderid"));
					rs2 = pstmt2.executeQuery();
					if (rs2.next()) rst.put("cc_reqsub",rs2.getString("cc_reqsub"));
					rst.put("orderid", "["+rs.getString("cr_orderid")+"]:"+rs2.getString("cc_reqsub"));
					rs2.close();
					pstmt2.close();
				}
				
				if (rs.getString("cr_creator").length() > 0 ){
					strQuery.setLength(0);					
	        	   	strQuery.append("select cm_username from cmm0040 where cm_userid=? \n");
	        	   	pstmt2 = conn.prepareStatement(strQuery.toString());
	        	   	pstmt2.setString(1, rs.getString("cr_creator"));
					rs2 = pstmt2.executeQuery();
					if (rs2.next()) rst.put("Lbl_Creator",rs2.getString("cm_username"));
					rs2.close();
					pstmt2.close();
				}
				if (rs.getString("cr_editor").length() > 0 ){
	        	   	strQuery.setLength(0);	        	   	
	        	   	strQuery.append("select cm_username from cmm0040 where cm_userid=? \n");
	        	   	pstmt2 = conn.prepareStatement(strQuery.toString());
	        	   	pstmt2.setString(1, rs.getString("cr_editor"));
					rs2 = pstmt2.executeQuery();
					if (rs2.next()) rst.put("Lbl_Editor",rs2.getString("cm_username"));
					rs2.close();
					pstmt2.close();
		        }
				if (rs.getString("cr_lstusr") != null){
					if (rs.getString("cr_lstusr").length() > 0 ){
		        	   	strQuery.setLength(0);	        	   	
		        	   	strQuery.append("select cm_username from cmm0040 where cm_userid=? \n");
		        	   	pstmt2 = conn.prepareStatement(strQuery.toString());
		        	   	pstmt2.setString(1, rs.getString("cr_lstusr"));
						rs2 = pstmt2.executeQuery();
						if (rs2.next()) rst.put("Lbl_LstUsr",rs2.getString("cm_username"));
						rs2.close();
						pstmt2.close();
					}
				}
	    		rtList.add(rst);
	    		rst = null;
		    }else{
				rst = new HashMap<String,String>();				
				rst.put("ID","Sql_Qry_Prog2");
		    	rst.put("WkSecu","false");
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
			if (conn != null){
				conn.close();conn = null;
			}
			sqlexception.printStackTrace();
			throw sqlexception;
		} catch (Exception exception) {
			if (conn != null){
				conn.close();conn = null;
			}
			exception.printStackTrace();
			throw exception;
		}finally{
			if (strQuery != null)	strQuery = null;
			if (rtObj != null)	rtObj = null;				
			if (rs != null)     try{rs.close();}catch (Exception ex){ex.printStackTrace();}
			if (pstmt != null)  try{pstmt.close();}catch (Exception ex2){ex2.printStackTrace();}
			if (conn != null){
				try{
					ConnectionResource.release(conn);
				}catch(Exception ex3){
					ex3.printStackTrace();
				}
			}
		}
    }//end of getGrid1_Click() method statement
    	
    public Object[] getTeamInfoGrid(String SelMsg,String cm_useyn) throws SQLException, Exception {
		Connection        conn        = null;
		PreparedStatement pstmt       = null;
		ResultSet         rs          = null;
		StringBuffer      strQuery    = new StringBuffer();
		Object[]		  rtObj		  = null;
		ArrayList<HashMap<String, String>>	rtList  = new ArrayList<HashMap<String, String>>();
		HashMap<String, String>			  	rst	 	= null;
		String            strSelMsg   = "";
		
		ConnectionContext connectionContext = new ConnectionResource();
		
		try {
			conn = connectionContext.getConnection();
			
			if (!"".equals(SelMsg) && SelMsg != null) {
				if (SelMsg.toUpperCase().equals("ALL")){
					strSelMsg = "전체";
				}
				else if(SelMsg.toUpperCase().equals("SEL")){
					strSelMsg = "선택하세요";
				}
				else{
					strSelMsg   = "";
				}
			}			
			
			strQuery.append("select cm_deptcd,cm_deptname \n");
			strQuery.append("  from cmm0100               \n");
			strQuery.append(" where cm_useyn='Y'          \n");
			strQuery.append("   and cm_updeptcd ='000010505'  \n");
			strQuery.append("order by cm_deptname         \n"); 			

            pstmt = conn.prepareStatement(strQuery.toString());	
            
            rs = pstmt.executeQuery();
            
            rtList.clear();
            
			while (rs.next()){
				if (rs.getRow() ==1 && !"".equals(strSelMsg) && strSelMsg.length() > 0) {
					rst = new HashMap<String, String>();
					rst.put("ID", "0");
					rst.put("cm_deptname", strSelMsg);
					rst.put("cm_deptcd", "");
					rtList.add(rst);
					rst = null;
				}	
				rst = new HashMap<String,String>();
				rst.put("ID", Integer.toString(rs.getRow()));
				rst.put("cm_deptcd", rs.getString("cm_deptcd"));
				rst.put("cm_deptname", rs.getString("cm_deptname"));
				rtList.add(rst);
				rst = null;
			}//end of while-loop statement
			rs.close();
			pstmt.close();
			conn.close();
			
			rs = null;
			pstmt = null;
			conn = null;
			
			rtObj =  rtList.toArray();
			rtList = null;
			return rtObj;
			
		} catch (SQLException sqlexception) {
			sqlexception.printStackTrace();
			ecamsLogger.error("## TeamInfo.getTeamInfoGrid() SQLException START ##");
			ecamsLogger.error("## Error DESC : ", sqlexception);	
			ecamsLogger.error("## TeamInfo.getTeamInfoGrid() SQLException END ##");
			throw sqlexception;
		} catch (Exception exception) {
			exception.printStackTrace();
			ecamsLogger.error("## TeamInfo.getTeamInfoGrid() Exception START ##");				
			ecamsLogger.error("## Error DESC : ", exception);	
			ecamsLogger.error("## TeamInfo.getTeamInfoGrid() Exception END ##");
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
					ecamsLogger.error("## TeamInfo.getTeamInfoGrid() connection release exception ##");
					ex3.printStackTrace();
				}
			}
		}
	}//end of getTeamInfoGrid() method statement	

    public Object[] getSql_Qry_Hist(String UserId,String L_SysCd,String L_JobCd,
    		String Cbo_ReqCd, String L_ItemId)	throws SQLException, Exception {	
		Connection        conn        = null;
		PreparedStatement pstmt       = null;
		PreparedStatement pstmt2       = null;
		ResultSet         rs          = null;
		ResultSet         rs2      = null;
		StringBuffer      strQuery    = new StringBuffer();
		ArrayList<HashMap<String, String>>  rtList = new ArrayList<HashMap<String, String>>();
		HashMap<String, String>			  rst		  = null;
		Object[]		  rtObj		  = null;
		
		ConnectionContext connectionContext = new ConnectionResource();
		try {
			conn = connectionContext.getConnection();
			
		    strQuery.setLength(0);
	    	strQuery.append("select a.cr_acptno,a.cr_prcdate,a.cr_aplydate,a.cr_status, \n");
	    	strQuery.append(" a.cr_rsrccd,a.cr_qrycd,b.cm_username,c.cm_codename, \n");
	    	strQuery.append(" d.cr_qrycd qrycd,d.cr_acptdate, \n");
	    	strQuery.append(" d.cr_sayu,d.cr_passok,d.cr_orderid,e.cc_reqsub \n");
	    	strQuery.append(" from cmc0420 e,cmr1010 a,cmr1000 d,cmm0040 b,cmm0020 c \n");
	    	strQuery.append(" where a.cr_itemid=?     \n"); //L_ItemId
	    	//strQuery.append(" a.cr_status <> '3' and \n");
            if (!Cbo_ReqCd.equals("ALL")){
            	strQuery.append("and d.cr_qrycd=?     \n"); //Cbo_ReqCd
            }
            strQuery.append("  and a.cr_acptno=d.cr_acptno \n");
            strQuery.append("  and a.cr_editor=b.cm_userid \n");
            strQuery.append("  and c.cm_macode='REQUEST' and d.cr_qrycd=c.cm_micode \n");
            strQuery.append("  and d.cr_orderid=e.cc_orderid(+) \n");
            if (Cbo_ReqCd.equals("ALL") || Cbo_ReqCd.equals("04")){
	            strQuery.append(" union \n");
	            strQuery.append(" select b.cr_acptno,b.cr_acptdate as cr_prcdate,'' as cr_aplydate,'9' cr_status,a.cr_rsrccd, \n");
	            strQuery.append("        decode(b.cr_qrycd,'03','최초이행','추가이행') as cr_qrycd,c.cm_username,  \n");
	            strQuery.append("        decode(b.cr_qrycd,'03','최초이행','추가이행') as cm_codename,             \n");
	            strQuery.append("        '04' as qrycd, b.cr_acptdate as cr_acptdate,			                 \n");
	            strQuery.append("        '형상관리 일괄이행' as cr_passcd, '0' as cr_passok,                        \n");
	            strQuery.append("        '' cr_orderid,'' cc_reqsub                                               \n");
	            strQuery.append(" from cmr0020 a,cmr0021 b,cmm0040 c \n");
	            strQuery.append(" where a.cr_itemid=? and \n"); //L_ItemId
	            strQuery.append(" b.cr_qrycd in ('03','05') and \n");
	            strQuery.append(" a.cr_itemid = b.cr_itemid and \n");
	            strQuery.append(" b.cr_editor=c.cm_userid \n");
            }
            strQuery.append(" order by cr_prcdate desc \n");
            
		    pstmt = conn.prepareStatement(strQuery.toString());
		    pstmt =  new LoggableStatement(conn, strQuery.toString());

		    int CNT = 0;
	    	pstmt.setString(++CNT, L_ItemId);
	    	if (!Cbo_ReqCd.equals("ALL")) pstmt.setString(++CNT, Cbo_ReqCd);
	    	if (Cbo_ReqCd.equals("ALL") || Cbo_ReqCd.equals("04")) pstmt.setString(++CNT, L_ItemId);
	    	
	    	ecamsLogger.error(((LoggableStatement)pstmt).getQueryString());
	    	rs = pstmt.executeQuery();
		    while (rs.next()){
				rst = new HashMap<String,String>();				
				//rst.put("NO",Integer.toString(rs.getRow()));				
				rst.put("SubItems1",rs.getString("cr_acptdate").substring(0,rs.getString("cr_acptdate").length()-2));
				rst.put("SubItems2",rs.getString("cm_username"));				
				
				rst.put("SubItems3",rs.getString("cm_codename"));
				rst.put("cr_acptno",rs.getString("cr_acptno"));
				rst.put("qrycd",rs.getString("qrycd"));
				if (rs.getString("cr_orderid") != null && !"".equals(rs.getString("cr_orderid"))) {
					rst.put("cc_orderid", rs.getString("cr_orderid"));
					rst.put("cc_reqsub", rs.getString("cc_reqsub"));
				}
				if (rs.getString("cr_acptno").substring(4,6).equals("04")){
					strQuery.setLength(0);
					strQuery.append("select cm_codename from cmm0020 where cm_macode='CHECKIN' and cm_micode=? \n");
				    pstmt2 = conn.prepareStatement(strQuery.toString());
				    pstmt2.setString(1, rs.getString("cr_qrycd"));
			    	rs2 = pstmt2.executeQuery();
			    	if (rs2.next())
			    		rst.put("SubItems3",rs.getString("cm_codename") + "[" + rs2.getString("cm_codename") + "]");
			    	rs2.close();
			    	pstmt2.close();
                }
                
                rst.put("SubItems4",rs.getString("cr_acptno"));

                if (!rs.getString("qrycd").equals("04")){
	                rst.put("SubItems5","");
                }else{
                	strQuery.setLength(0);
                	strQuery.append("select cm_codename from cmm0020 \n");
                	strQuery.append("where cm_macode='REQPASS' and cm_micode=? \n");//cr_passok
                	pstmt2 = conn.prepareStatement(strQuery.toString());
                	pstmt2.setString(1, rs.getString("cr_passok"));
			    	rs2 = pstmt2.executeQuery();
			    	if (rs2.next())
			    		rst.put("SubItems5",rs2.getString("cm_codename"));
			    	rs2.close();
			    	pstmt2.close();
	                 
					if (rs.getString("cr_aplydate") != null){
	                    if (rs.getString("cr_aplydate").equals("9"))
	                    	rst.put("SubItems5","적용제외");
	                    else
	                    	rst.put("SubItems5",rs.getString("cr_aplydate"));
					}
                }
                /*
                if (rs.getString("cr_passok").equals("2")) rst.put("SubItems5","긴급적용");
                else {
                	if (rs.getString("cr_aplydate") != "" && rs.getString("cr_aplydate") != null){
                		if (rs.getString("cr_aplydate").equals("9")) rst.put("SubItems5","적용제외");
                        else rst.put("SubItems5", rs.getString("cr_aplydate").substring(1,12));
                	}else{
                		strQuery.setLength(0);
                    	strQuery.append("select cm_time from cmm0038 where cm_syscd=? \n");//L_SysCd
                    	strQuery.append("and cm_rsrccd=? \n");  //DbSet!cr_rsrccd
                    	pstmt = conn.prepareStatement(strQuery.toString());
                    	pstmt.setString(1, L_SysCd);
                    	pstmt.setString(2, rs.getString("cr_rsrccd"));
                    	rs_tmp = pstmt.executeQuery();
                    	
                        if (rs_tmp.next() && rs_tmp.getString("cm_time").length() > 0){
                        	if (Integer.parseInt(rs_tmp.getString("cm_time")) > 1200){
                        		rst.put("SubItems5", acptdate.substring(0,4) + "/" + acptdate.substring(5,7) + "/" + acptdate.substring(8,10) + " " + 
                        		rs_tmp.getString("cm_time").substring(0,2) + ":" + rs_tmp.getString("cm_time").substring(2) + " 이후");
                        	}else{
                        		String sdate = dateAdd(acptdate.substring(0,4)+acptdate.substring(5,7)+acptdate.substring(8,10),1);
                        		rst.put("SubItems5", sdate.substring(0,4) + "/" + sdate.substring(4,6) + "/" +  sdate.substring(6,8) + " " + 
                        				rs_tmp.getString("cm_time").substring(0,2) + " : " + rs_tmp.getString("cm_time").substring(2) + " 이후");
                        	}
                        }else rst.put("SubItems5", "즉시적용");
                	}
                }*/
                
                if (rs.getString("cr_prcdate") != null){
	                if (rs.getString("cr_prcdate").length() > 0){
	                	if (rs.getString("cr_status").equals("3"))	
		                	   rst.put("SubItems6", "[반려]" + rs.getString("cr_prcdate").substring(5,rs.getString("cr_prcdate").length()-2));
	                	else	
	                	   rst.put("SubItems6", rs.getString("cr_prcdate").substring(0,rs.getString("cr_prcdate").length()-2));
	                	//rst.put("SubItems6", rs.getString("cr_prcdate").substring(5,7) + "/" +
	                		//rs.getString("cr_prcdate").substring(8,10) + "  " +
	                		//rs.getString("cr_prcdate").substring(10,12) + ":" +
	                		//rs.getString("cr_prcdate").substring(12,14) + ":" +
	                		//rs.getString("cr_prcdate").substring(14));
	                }
                } else {
                	rst.put("SubItems6","진행중");
                }
                rst.put("SubItems7", rs.getString("cr_sayu"));
                rst.put("SubItems8", rs.getString("cr_acptno"));
                rst.put("SubItems9", rs.getString("cr_status"));
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
			if (conn != null){
				conn.close();conn = null;
			}
			sqlexception.printStackTrace();
			throw sqlexception;
		} catch (Exception exception) {
			if (conn != null){
				conn.close();conn = null;
			}
			exception.printStackTrace();
			throw exception;
		}finally{
			if (strQuery != null)	strQuery = null;
			if (rtObj != null)	rtObj = null;			
			if (rs != null)     try{rs.close();}catch (Exception ex){ex.printStackTrace();}
			if (pstmt != null)  try{pstmt.close();}catch (Exception ex2){ex2.printStackTrace();}
			if (conn != null){
				try{
					ConnectionResource.release(conn);
				}catch(Exception ex3){
					ex3.printStackTrace();
				}
			}
		}
    }//end of getSql_Qry_Hist() method statement
    
	/*
    public String dateAdd(String sDate, int iDay) { 

        try{ 
            Calendar cDate = Calendar.getInstance(); // Calendar 객체선언 
            int iYyyy = Integer.parseInt(sDate.substring(0, 4)); // 년 YYYY 
            int iMm = Integer.parseInt(sDate.substring(4, 6)) - 1; // 월은 0~11 
            int iDd = Integer.parseInt(sDate.substring(6, 8)); // 일 1 ~ 31 
     
            cDate.set(iYyyy, iMm, iDd); // 입력된 날짜 set 
            cDate.add(Calendar.DATE, iDay); // 객체 cDate 에 날짜더함 set 
     
            iYyyy = cDate.get(Calendar.YEAR); 
            iMm = cDate.get(Calendar.MONTH); 
            iDd = cDate.get(Calendar.DATE); // Calendar 객체 cDate 에서 년월일을 차례로 받는다. 
     
            // 날짜를 String 객체로 변환 "YYYYMMDD" 
            if (iMm<9 && iDd<10) sDate = String.valueOf(iYyyy)+"0"+String.valueOf(iMm+1)+"0"+String.valueOf(iDd); 
            else if (iMm<9 && iDd>=10) sDate = String.valueOf(iYyyy)+"0"+String.valueOf(iMm+1)+String.valueOf(iDd); 
            else if (iMm>=9 && iDd<10) sDate = String.valueOf(iYyyy)+String.valueOf(iMm+1)+"0"+String.valueOf(iDd); 
            else sDate = String.valueOf(iYyyy)+String.valueOf(iMm+1)+String.valueOf(iDd); 
     
        }catch (Exception e) { 
            return null; 
        } 
        return sDate; 
    }
    */
    public Object[] getSql_Info(String L_SysCd,String WkRsrcCd,String WkVer,int Lv_Src_Cnt) throws SQLException, Exception {	
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
			strQuery.append("select cm_info from cmm0036 where \n");
			strQuery.append(" cm_syscd=? and \n");  //L_SysCd
			strQuery.append(" cm_rsrccd=? \n");  //WkRsrcCd
			
			//pstmt =  new LoggableStatement(conn, strQuery.toString());
		    pstmt = conn.prepareStatement(strQuery.toString());
			pstmt.setString(1, L_SysCd);
			pstmt.setString(2, WkRsrcCd);
			
			////ecamsLogger.error(((LoggableStatement)pstmt).getQueryString());
			rs = pstmt.executeQuery();
			if (rs.next()) {
				rst = new HashMap<String,String>();
				rst.put("ID","getSql_Info");
				if (rs.getString("cm_info").substring(11,12).equals("1") &&	(Integer.parseInt(WkVer)>0 || Lv_Src_Cnt>0)){
					rst.put("Lbl_Tab2","true");
					if (Integer.parseInt(WkVer)>1) rst.put("Lbl_Tab3","true");
					else rst.put("Lbl_Tab3","false");
				}else{
					rst.put("Lbl_Tab2","false");
					rst.put("Lbl_Tab3","false");
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
		    
			rtObj = rtList.toArray();
			rtList.clear();
			rtList = null;
			
			return rtObj;			
		    
		} catch (SQLException sqlexception) {
			if (conn != null){
				conn.close();conn = null;
			}
			sqlexception.printStackTrace();
			throw sqlexception;
		} catch (Exception exception) {
			if (conn != null){
				conn.close();conn = null;
			}
			exception.printStackTrace();
			throw exception;
		}finally{
			if (strQuery != null)	strQuery = null;
			if (rtObj != null)	rtObj = null;			
			if (rs != null)     try{rs.close();}catch (Exception ex){ex.printStackTrace();}
			if (pstmt != null)  try{pstmt.close();}catch (Exception ex2){ex2.printStackTrace();}
			if (conn != null){
				try{
					ConnectionResource.release(conn);
				}catch(Exception ex3){
					ex3.printStackTrace();
				}
			}
		}
    }//end of getSql_Info() method statement
    
    public Object[] getCbo_ReqCd_Add() throws SQLException, Exception {	
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
			strQuery.append("select cm_micode,cm_codename from cmm0020 \n");
			strQuery.append(" where cm_macode='REQUEST' and cm_micode<'10' \n");
			strQuery.append(" and cm_micode<>'****' and cm_closedt is null \n");
			strQuery.append(" order by cm_micode \n");			
		    pstmt = conn.prepareStatement(strQuery.toString());
			rs = pstmt.executeQuery();
			rst = new HashMap<String,String>();
			rst.put("cm_micode","ALL");
			rst.put("cm_codename","전체");
			rtList.add(rst);
			rst = null;
			/*
			rst = new HashMap<String,String>();
			rst.put("cm_micode","99");
			rst.put("cm_codename","업무변경History");
			rtList.add(rst);
			rst = null;
			*/
			while (rs.next()) {
				rst = new HashMap<String,String>();
				rst.put("cm_micode",rs.getString("cm_micode"));
				rst.put("cm_codename",rs.getString("cm_codename"));
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
			if (conn != null){
				conn.close();conn = null;
			}
			sqlexception.printStackTrace();
			throw sqlexception;
		} catch (Exception exception) {
			if (conn != null){
				conn.close();conn = null;
			}
			exception.printStackTrace();
			throw exception;
		}finally{
			if (strQuery != null)	strQuery = null;
			if (rtObj != null)	rtObj = null;			
			if (rs != null)     try{rs.close();}catch (Exception ex){ex.printStackTrace();}
			if (pstmt != null)  try{pstmt.close();}catch (Exception ex2){ex2.printStackTrace();}
			if (conn != null){
				try{
					ConnectionResource.release(conn);
				}catch(Exception ex3){
					ex3.printStackTrace();
				}
			}
		}
    }//end of getCbo_ReqCd_Add() method statement
    
    public int getTbl_Update(HashMap<String,String> etcData) throws SQLException, Exception {							
		Connection        conn        = null;					
		PreparedStatement pstmt       = null;					
		ResultSet         rs          = null;					
		StringBuffer      strQuery    = new StringBuffer();					
		int				  rtn_cnt     = 0;
		int               parmCnt     = 0;					
		String            strOrderId    = "";					
		String            strIsrSub   = "";					
							
		ConnectionContext connectionContext = new ConnectionResource();					
		try {					
			conn = connectionContext.getConnection();	
			conn.setAutoCommit(false);
			
			strQuery.setLength(0);				
	    	strQuery.append("select cr_orderid from cmr0020 \n");					
	    	strQuery.append(" where cr_itemid=? \n");//L_ItemId					
	    	strQuery.append("   and cr_orderid is not null \n");//L_ItemId					
			pstmt = conn.prepareStatement(strQuery.toString());				
            pstmt.setString(1, etcData.get("itemid"));							
            rs = pstmt.executeQuery();							
            if (rs.next()) {							
            	strOrderId = rs.getString("cr_orderid");						
            }							
            rs.close();							
            pstmt.close();							
            							
			//String      strJob[] = L_JobCd.split(",");						
			strQuery.setLength(0);						
			strQuery.append("update cmr0020 set     \n");						
			if (!"".equals(etcData.get("jobcd")) && etcData.get("jobcd") != null) {						
				strQuery.append("   cr_jobcd=?,     \n");					
			}						
			if (!"".equals(etcData.get("rsrccd")) && etcData.get("rsrccd") != null) {						
				strQuery.append("   cr_rsrccd=?,     \n");					
			}						
			if (!"".equals(etcData.get("story")) && etcData.get("story") != null) {						
				strQuery.append("   cr_story=?,     \n");					
			}						
			if (!"".equals(etcData.get("editor")) && etcData.get("editor") != null) {						
				strQuery.append("   cr_editor=?,     \n");					
			}						
			if (!"".equals(etcData.get("master")) && etcData.get("master") != null) {						
				strQuery.append("   cr_master=?,     \n");					
			}						
			if (!"".equals(etcData.get("dsncd")) && etcData.get("dsncd") != null) {						
				strQuery.append("   cr_dsncd=?,     \n");					
			}						
			if (!"".equals(etcData.get("order")) && etcData.get("order") != null) {						
				strQuery.append("   cr_orderid=?,     \n");					
			}						
			if (!"".equals(etcData.get("Compile")) && etcData.get("Compile") != null) {						
				strQuery.append("   cr_Compile=?,     \n");					
			}						
			if (!"".equals(etcData.get("CompileMake")) && etcData.get("CompileMake") != null) {						
				strQuery.append("   cr_MakeCompile=?,     \n");					
			}						
			if (!"".equals(etcData.get("Team")) && etcData.get("Team") != null) {						
				strQuery.append("   cr_Teamcd=?,     \n");					
			}						
			if (!"".equals(etcData.get("document")) && etcData.get("document") != null) {						
				strQuery.append("   cr_Document=?,     \n");					
			}
			if (!"".equals(etcData.get("cr_rpaflag")) && etcData.get("cr_rpaflag") != null) {	// 20230131					
				strQuery.append("   cr_rpaflag=?,     \n");					
			}			
		    strQuery.append(" cr_lastdate=SYSDATE               \n");						
		    strQuery.append(" where cr_itemid=?                 \n");//L_ItemId						
			pstmt = conn.prepareStatement(strQuery.toString());						
			pstmt =  new LoggableStatement(conn, strQuery.toString());            						
									
			if (!"".equals(etcData.get("jobcd")) && etcData.get("jobcd") != null) {						
				pstmt.setString(++parmCnt, etcData.get("jobcd"));					
			}						
			if (!"".equals(etcData.get("rsrccd")) && etcData.get("rsrccd") != null) {						
				pstmt.setString(++parmCnt, etcData.get("rsrccd"));					
			}						
			if (!"".equals(etcData.get("story")) && etcData.get("story") != null) {						
				pstmt.setString(++parmCnt, etcData.get("story"));					
			}						
			if (!"".equals(etcData.get("editor")) && etcData.get("editor") != null) {						
				pstmt.setString(++parmCnt, etcData.get("editor"));					
			}						
			if (!"".equals(etcData.get("master")) && etcData.get("master") != null) {						
				pstmt.setString(++parmCnt, etcData.get("master"));					
			}						
			if (!"".equals(etcData.get("dsncd")) && etcData.get("dsncd") != null) {						
				pstmt.setString(++parmCnt, etcData.get("dsncd"));					
			}						
			if (!"".equals(etcData.get("order")) && etcData.get("order") != null) {						
				pstmt.setString(++parmCnt, etcData.get("order"));					
			}						
			if (!"".equals(etcData.get("Compile")) && etcData.get("Compile") != null) {						
				pstmt.setString(++parmCnt, etcData.get("Compile"));					
			}						
			if (!"".equals(etcData.get("CompileMake")) && etcData.get("CompileMake") != null) {						
				pstmt.setString(++parmCnt, etcData.get("CompileMake"));					
			}						
			if (!"".equals(etcData.get("Team")) && etcData.get("Team") != null) {						
				pstmt.setString(++parmCnt, etcData.get("Team"));					
			}						
			if (!"".equals(etcData.get("document")) && etcData.get("document") != null) {						
				pstmt.setString(++parmCnt, etcData.get("document"));					
			}					
			if (!"".equals(etcData.get("cr_rpaflag")) && etcData.get("cr_rpaflag") != null) {	// 20230131					
				pstmt.setString(++parmCnt, etcData.get("cr_rpaflag"));					
			}	
            pstmt.setString(++parmCnt, etcData.get("itemid"));							
            ecamsLogger.error(((LoggableStatement)pstmt).getQueryString());							
            rtn_cnt = pstmt.executeUpdate();							
            pstmt.close();							
            							
            if (!"".equals(etcData.get("rsrccd")) && etcData.get("rsrccd") != null ||							
            	!"".equals(etcData.get("jobcd")) && etcData.get("jobcd") != null ||						
            	!"".equals(etcData.get("dsncd")) && etcData.get("dsncd") != null){						
            	parmCnt = 0;						
				strQuery.setLength(0);						
				strQuery.append("update cmr1010 set             \n");//L_RsrcCd						
				if (!"".equals(etcData.get("rsrccd")) && etcData.get("rsrccd") != null) {						
					strQuery.append("   cr_rsrccd=?,            \n");					
				}						
				if (!"".equals(etcData.get("jobcd")) && etcData.get("jobcd") != null) {						
					strQuery.append("   cr_jobcd=?,             \n");					
				}						
				if (!"".equals(etcData.get("dsncd")) && etcData.get("dsncd") != null) {						
					strQuery.append("   cr_dsncd=?,             \n");					
				}						
				strQuery.append("   cr_editdate=SYSDATE         \n");						
				strQuery.append(" where cr_itemid=?             \n");//L_ItemId						
				pstmt = conn.prepareStatement(strQuery.toString());						
				//pstmt =  new LoggableStatement(conn, strQuery.toString());     						
				if (!"".equals(etcData.get("rsrccd")) && etcData.get("rsrccd") != null) {						
					pstmt.setString(++parmCnt, etcData.get("rsrccd"));					
				}						
				if (!"".equals(etcData.get("jobcd")) && etcData.get("jobcd") != null) {						
					pstmt.setString(++parmCnt, etcData.get("jobcd"));					
				}						
				if (!"".equals(etcData.get("dsncd")) && etcData.get("dsncd") != null) {						
					pstmt.setString(++parmCnt, etcData.get("dsncd"));					
				}						
				pstmt.setString(++parmCnt, etcData.get("itemid"));
				////ecamsLogger.error(((LoggableStatement)pstmt).getQueryString());
	            rtn_cnt = pstmt.executeUpdate();
	            pstmt.close();
		    }
            
            conn.commit();
            conn.close();
		    pstmt = null;
		    rs = null;
		    conn = null;
		    
            return rtn_cnt;
            
		} catch (SQLException sqlexception) {
			if (conn != null){
				conn.close();conn = null;
			}
			sqlexception.printStackTrace();
			ecamsLogger.error("## Cmd0500.getTbl_Update() SQLException START ##");
			ecamsLogger.error("## Error DESC : ", sqlexception);	
			ecamsLogger.error("## Cmr0Cmd0500200.getTbl_Update() SQLException END ##");			
			throw sqlexception;
		} catch (Exception exception) {
			if (conn != null){
				conn.close();conn = null;
			}
			exception.printStackTrace();
			ecamsLogger.error("## Cmd0500.getTbl_Update() Exception START ##");				
			ecamsLogger.error("## Error DESC : ", exception);	
			ecamsLogger.error("## Cmd0500.getTbl_Update() Exception END ##");				
			throw exception;
		}finally{
			if (strQuery != null) 	strQuery = null;
			if (rs != null)  try{rs.close();}catch (Exception ex2){ex2.printStackTrace();}
			if (pstmt != null)  try{pstmt.close();}catch (Exception ex2){ex2.printStackTrace();}
			if (conn != null){
				try{
					ConnectionResource.release(conn);
				}catch(Exception ex3){
					ecamsLogger.error("## Cmd0500.getTbl_Update() connection release exception ##");
					ex3.printStackTrace();
				}
			}
		}
    }//end of getTbl_Update() method statement
    
    
    public int getTbl_Delete(String L_ItemId) throws SQLException, Exception {	
		Connection        conn        = null;
		PreparedStatement pstmt       = null;
		ResultSet         rs          = null;
		StringBuffer      strQuery    = new StringBuffer();
		int				  rtn_cnt     = 0;
		String            strIsrId    = "";
		String            strIsrSub   = "";
		ConnectionContext connectionContext = new ConnectionResource();
		try {
			conn = connectionContext.getConnection();
			/*strQuery.setLength(0);
	    	strQuery.append("select cr_isrid,cr_isrsub from cmr0020 \n");
	    	strQuery.append(" where cr_itemid=? \n");//L_ItemId
	    	strQuery.append("   and cr_isrid is not null \n");//L_ItemId
			pstmt = conn.prepareStatement(strQuery.toString());			
            pstmt.setString(1, L_ItemId);
            rs = pstmt.executeQuery();
            if (rs.next()) {
            	strIsrId = rs.getString("cr_isrid");
            	strIsrSub = rs.getString("cr_isrsub");
            }
            rs.close();
            pstmt.close();
            */
	        strQuery.setLength(0);
	    	strQuery.append("delete cmr0025 \n");
	    	strQuery.append(" where cr_itemid=? \n");//L_ItemId
			pstmt = conn.prepareStatement(strQuery.toString());
            pstmt.setString(1, L_ItemId);
            rtn_cnt = pstmt.executeUpdate();
            pstmt.close();

	        strQuery.setLength(0);
	    	strQuery.append("delete cmr0021 \n");
	    	strQuery.append(" where cr_itemid=? \n");//L_ItemId
			pstmt = conn.prepareStatement(strQuery.toString());
            pstmt.setString(1, L_ItemId);
            rtn_cnt = pstmt.executeUpdate();
            pstmt.close();
            
	        strQuery.setLength(0);
	    	strQuery.append("delete cmr1010 \n");
	    	strQuery.append(" where cr_itemid=? \n");//L_ItemId
			pstmt = conn.prepareStatement(strQuery.toString());
            pstmt.setString(1, L_ItemId);
            rtn_cnt = pstmt.executeUpdate();
            pstmt.close();
            
	        strQuery.setLength(0);
	    	strQuery.append("delete cmr0022 \n");
	    	strQuery.append(" where cr_itemid=? \n");//L_ItemId
			pstmt = conn.prepareStatement(strQuery.toString());
            pstmt.setString(1, L_ItemId);
            rtn_cnt = pstmt.executeUpdate();
            pstmt.close();
            
	        strQuery.setLength(0);
	    	strQuery.append("delete cmr0020 \n");
	    	strQuery.append(" where cr_itemid=? \n");//L_ItemId
			pstmt = conn.prepareStatement(strQuery.toString());
            pstmt.setString(1, L_ItemId);
            rtn_cnt = pstmt.executeUpdate();
            pstmt.close();
            /*
            if (strIsrId.length()>0) {
	        	strQuery.setLength(0);
        		strQuery.append("UPDATE CMC0110 \n");
        		strQuery.append("   SET CC_SUBSTATUS=DECODE(TESTENDYN('',?,?),'OK','25',CC_SUBSTATUS) \n");
        		strQuery.append(" WHERE CC_ISRID=? \n");
        		strQuery.append("   AND CC_ISRSUB=? \n");
        		pstmt = conn.prepareStatement(strQuery.toString());
        		// pstmt = new LoggableStatement(conn,strQuery.toString());
	          	pstmt.setString(1, strIsrId);
          	    pstmt.setString(2, strIsrSub);
	          	pstmt.setString(3, strIsrId);
          	    pstmt.setString(4, strIsrSub);
         	//    ecamsLogger.error(((LoggableStatement)pstmt).getQueryString());
          	    pstmt.executeUpdate();
    	        pstmt.close();
	        }*/
            conn.close();
		    pstmt = null;
		    rs = null;
		    conn = null;
		    
            return rtn_cnt;
            
		} catch (SQLException sqlexception) {
			if (conn != null){
				conn.close();conn = null;
			}
			sqlexception.printStackTrace();
			throw sqlexception;
		} catch (Exception exception) {
			if (conn != null){
				conn.close();conn = null;
			}
			exception.printStackTrace();
			throw exception;
		}finally{
			if (strQuery != null)	strQuery = null;
			if (pstmt != null)  try{pstmt.close();}catch (Exception ex2){ex2.printStackTrace();}
			if (rs != null)  try{rs.close();}catch (Exception ex2){ex2.printStackTrace();}
			if (conn != null){
				try{
					ConnectionResource.release(conn);
				}catch(Exception ex3){
					ex3.printStackTrace();
				}
			}
		}
    }//end of getTbl_Delete() method statement
    
    public int getAllTbl_Delete(String itemId) throws SQLException, Exception {	
		Connection        conn        = null;
		PreparedStatement pstmt       = null;
		ResultSet         rs          = null;
		StringBuffer      strQuery    = new StringBuffer();
		int				  rtn_cnt     = 0;
		String            strIsrId    = "";
		String            strIsrSub   = "";
		ConnectionContext connectionContext = new ConnectionResource();
		try {
			conn = connectionContext.getConnection();

			String[] itemidary = itemId.split(",");
			
			for(int i=0; i<itemidary.length; i++){
		        strQuery.setLength(0);
		    	strQuery.append("delete cmr0025 \n");
		    	strQuery.append(" where cr_itemid=? \n");//L_ItemId
				pstmt = conn.prepareStatement(strQuery.toString());
	            pstmt.setString(1, itemidary[i]);
	            rtn_cnt = pstmt.executeUpdate();
	            pstmt.close();
	
		        strQuery.setLength(0);
		    	strQuery.append("delete cmr0021 \n");
		    	strQuery.append(" where cr_itemid=? \n");//L_ItemId
				pstmt = conn.prepareStatement(strQuery.toString());
	            pstmt.setString(1, itemidary[i]);
	            rtn_cnt = pstmt.executeUpdate();
	            pstmt.close();
	            
		        strQuery.setLength(0);
		    	strQuery.append("delete cmr1010 \n");
		    	strQuery.append(" where cr_itemid=? \n");//L_ItemId
				pstmt = conn.prepareStatement(strQuery.toString());
	            pstmt.setString(1, itemidary[i]);
	            rtn_cnt = pstmt.executeUpdate();
	            pstmt.close();
	            
		        strQuery.setLength(0);
		    	strQuery.append("delete cmr0022 \n");
		    	strQuery.append(" where cr_itemid=? \n");//L_ItemId
				pstmt = conn.prepareStatement(strQuery.toString());
	            pstmt.setString(1, itemidary[i]);
	            rtn_cnt = pstmt.executeUpdate();
	            pstmt.close();
	            
		        strQuery.setLength(0);
		    	strQuery.append("delete cmr0020 \n");
		    	strQuery.append(" where cr_itemid=? \n");//L_ItemId
				pstmt = conn.prepareStatement(strQuery.toString());
	            pstmt.setString(1, itemidary[i]);
	            rtn_cnt = pstmt.executeUpdate();
	            pstmt.close();

			}
			
            conn.close();
		    pstmt = null;
		    rs = null;
		    conn = null;
		    
            return rtn_cnt;
            
		} catch (SQLException sqlexception) {
			if (conn != null){
				conn.close();conn = null;
			}
			sqlexception.printStackTrace();
			throw sqlexception;
		} catch (Exception exception) {
			if (conn != null){
				conn.close();conn = null;
			}
			exception.printStackTrace();
			throw exception;
		}finally{
			if (strQuery != null)	strQuery = null;
			if (pstmt != null)  try{pstmt.close();}catch (Exception ex2){ex2.printStackTrace();}
			if (rs != null)  try{rs.close();}catch (Exception ex2){ex2.printStackTrace();}
			if (conn != null){
				try{
					ConnectionResource.release(conn);
				}catch(Exception ex3){
					ex3.printStackTrace();
				}
			}
		}
    }//end of getAllTbl_Delete() method statement
    
    public int getTbl_Close(String L_ItemId, String editor) throws SQLException, Exception {	
		Connection        conn        = null;
		PreparedStatement pstmt       = null;
		StringBuffer      strQuery    = new StringBuffer();
		int				  rtn_cnt     = 0;
		ConnectionContext connectionContext = new ConnectionResource();
		try {
			conn = connectionContext.getConnection();
			
	        strQuery.setLength(0);
	    	strQuery.append("update cmr0020 \n");
	    	strQuery.append("   set cr_status='9', cr_clsdate = sysdate, cr_editor=? \n");
	    	strQuery.append(" where cr_itemid=? \n");//L_ItemId
			pstmt = conn.prepareStatement(strQuery.toString());
            pstmt.setString(1, editor);
            pstmt.setString(2, L_ItemId);
            rtn_cnt = pstmt.executeUpdate();
            pstmt.close();


            conn.close();
		    pstmt.close();
		    pstmt = null;
		    conn = null;
		    
            return rtn_cnt;
            
		} catch (SQLException sqlexception) {
			if (conn != null){
				conn.close();conn = null;
			}
			sqlexception.printStackTrace();
			throw sqlexception;
		} catch (Exception exception) {
			if (conn != null){
				conn.close();conn = null;
			}
			exception.printStackTrace();
			throw exception;
		}finally{
			if (strQuery != null)	strQuery = null;
			if (pstmt != null)  try{pstmt.close();}catch (Exception ex2){ex2.printStackTrace();}
			if (conn != null){
				try{
					ConnectionResource.release(conn);
				}catch(Exception ex3){
					ex3.printStackTrace();
				}
			}
		}
    }//end of getTbl_Delete() method statement
    
    public int getTbl_alive(String L_ItemId, String editor) throws SQLException, Exception {	
		Connection        conn        = null;
		PreparedStatement pstmt       = null;
		StringBuffer      strQuery    = new StringBuffer();
		int				  rtn_cnt     = 0;
		ConnectionContext connectionContext = new ConnectionResource();
		try {
			conn = connectionContext.getConnection();
			
	        strQuery.setLength(0);
	    	strQuery.append("update cmr0020 \n");
	    	strQuery.append("   set cr_status = decode(cr_lstver,0,'3','0'), cr_clsdate = null, cr_editor=? \n");
	    	strQuery.append(" where cr_itemid=? \n");//L_ItemId
			pstmt = conn.prepareStatement(strQuery.toString());
            pstmt.setString(1, editor);
            pstmt.setString(2, L_ItemId);
            rtn_cnt = pstmt.executeUpdate();
            pstmt.close();

            conn.close();
		    pstmt.close();
		    pstmt = null;
		    conn = null;
		    
            return rtn_cnt;
            
		} catch (SQLException sqlexception) {
			if (conn != null){
				conn.close();conn = null;
			}
			sqlexception.printStackTrace();
			throw sqlexception;
		} catch (Exception exception) {
			if (conn != null){
				conn.close();conn = null;
			}
			exception.printStackTrace();
			throw exception;
		}finally{
			if (strQuery != null)	strQuery = null;
			if (pstmt != null)  try{pstmt.close();}catch (Exception ex2){ex2.printStackTrace();}
			if (conn != null){
				try{
					ConnectionResource.release(conn);
				}catch(Exception ex3){
					ex3.printStackTrace();
				}
			}
		}
    }//end of getTbl_alive() method statement
    
    public int setTbl_Close(String itemId, String editor) throws SQLException, Exception {	
		Connection        conn        = null;
		PreparedStatement pstmt       = null;
		ResultSet         rs          = null;
		StringBuffer      strQuery    = new StringBuffer();
		int				  rtn_cnt     = 0;
		ConnectionContext connectionContext = new ConnectionResource();
		try {
			conn = connectionContext.getConnection();
			String[] itemidary = itemId.split(",");
			
			
			for(int i=0; i<itemidary.length; i++){
				strQuery.setLength(0);
		    	strQuery.append("update cmr0020               \n");
		    	strQuery.append("   set cr_status = '9',      \n");
		    	strQuery.append("       cr_clsdate = sysdate, \n");
		    	strQuery.append("       cr_editor = ?         \n");
		    	strQuery.append(" where cr_itemid=?           \n");
				pstmt = conn.prepareStatement(strQuery.toString());
	            pstmt.setString(1, editor);
	            pstmt.setString(2, itemidary[i]);
	            rtn_cnt = pstmt.executeUpdate();
	            pstmt.close();
			}
            conn.close();
		    pstmt = null;
		    rs = null;
		    conn = null;
		    
            return rtn_cnt;
            
		} catch (SQLException sqlexception) {
			if (conn != null){
				conn.close();conn = null;
			}
			sqlexception.printStackTrace();
			throw sqlexception;
		} catch (Exception exception) {
			if (conn != null){
				conn.close();conn = null;
			}
			exception.printStackTrace();
			throw exception;
		}finally{
			if (strQuery != null)	strQuery = null;
			if (pstmt != null)  try{pstmt.close();}catch (Exception ex2){ex2.printStackTrace();}
			if (rs != null)  try{rs.close();}catch (Exception ex2){ex2.printStackTrace();}
			if (conn != null){
				try{
					ConnectionResource.release(conn);
				}catch(Exception ex3){
					ex3.printStackTrace();
				}
			}
		}
    }//end of getTbl_Delete() method statement
    
    public Object[] getCbo_Editor_Add(String ItemId,String Editor) throws SQLException, Exception {	
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
			strQuery.append("select a.cm_username,a.cm_userid          \n");
			strQuery.append("  from cmm0040 a,cmm0044 b,cmr0020 c      \n");
			strQuery.append(" where c.cr_itemid=?                      \n");//Txt_Editor
			strQuery.append("   and c.cr_syscd=b.cm_syscd              \n");
			strQuery.append("   and c.cr_jobcd=b.cm_jobcd              \n");
			strQuery.append("   and b.cm_closedt is null               \n");
			strQuery.append("   and b.cm_userid=a.cm_userid            \n");
			strQuery.append("   and a.cm_active='1'                    \n");
			strQuery.append("union                                     \n");
			strQuery.append("select cm_username,cm_userid              \n");
			strQuery.append("  from cmm0040                            \n");
			strQuery.append(" where cm_userid=?                        \n");//Txt_Editor
			strQuery.append("order by cm_username                      \n");
		    pstmt = conn.prepareStatement(strQuery.toString());
		    pstmt.setString(1, ItemId);
		    pstmt.setString(2, Editor);
			rs = pstmt.executeQuery();
			//boolean listFg = true;
			
			rst = new HashMap<String,String>();
			rst.put("userid","선택하세요");
			rtList.add(rst);
			
			while (rs.next()) {
				rst = new HashMap<String,String>();
				rst.put("userid","[" + rs.getString("cm_userid") + "] " + rs.getString("cm_username"));
				rst.put("cm_userid",rs.getString("cm_userid"));
				rst.put("cm_username",rs.getString("cm_username"));
				rtList.add(rst);
				rst = null;
				//listFg = false;
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
			if (conn != null){
				conn.close();conn = null;
			}
			sqlexception.printStackTrace();
			throw sqlexception;
		} catch (Exception exception) {
			if (conn != null){
				conn.close();conn = null;
			}
			exception.printStackTrace();
			throw exception;
		}finally{
			if (strQuery != null)	strQuery = null;
			if (rtObj != null)	rtObj = null;			
			if (rs != null)     try{rs.close();}catch (Exception ex){ex.printStackTrace();}
			if (pstmt != null)  try{pstmt.close();}catch (Exception ex2){ex2.printStackTrace();}
			if (conn != null){
				try{
					ConnectionResource.release(conn);
				}catch(Exception ex3){
					ex3.printStackTrace();
				}
			}
		}
    }//end of getCbo_ReqCd_Add() method statement
    
    public String verSync(String itemID, String userID, String Syscd) throws SQLException, Exception{
    	Connection        conn        = null;
    	ConnectionContext connectionContext = new ConnectionResource();
		PreparedStatement pstmt       = null;
		String[] strAry = null;
		Runtime  run = null;
		File shfile=null;
		String            strTmpPath  = "";
		String            strBinPath    = "";
		String            makeFile    = "";
		String			  returnck    = "";
		String            strFile     = "";
		String            returnStr   = "Y";
		Process p = null;
		OutputStreamWriter writer = null;
		SystemPath		  systemPath = new SystemPath();
		eCAMSInfo         ecamsinfo   = new eCAMSInfo();
		StringBuffer      strQuery    = new StringBuffer();
		ResultSet         rs          = null;
		String rtString = "";
		try{
			conn = connectionContext.getConnection();
			
			strTmpPath = ecamsinfo.getFileInfo("99");
			shfile=null;
	   		shfile = new File(strTmpPath + makeFile+".sh");              //File 불러온다.
	   		if( !(shfile.isFile()) )              //File이 없으면 
	   		{
	   			shfile.createNewFile();          //File 생성
	   		}
	   		
	   		// 20221219 ecams_batexec 추가 쿼리
			conn.setAutoCommit(false);
			strQuery.setLength(0);
			strQuery.append("select cm_ipaddr, cm_port 	\n");
			strQuery.append("  from cmm0010 			\n");
			strQuery.append(" where cm_stno = 'ECAMS'	\n");
			
			pstmt = conn.prepareStatement(strQuery.toString());	
			rs = pstmt.executeQuery();
			if(rs.next()){
				rtString = rs.getString("cm_ipaddr") + " " + rs.getString("cm_port") + " 0";
			}// 20221219 ecams_batexec 추가 쿼리
	   		
			strBinPath = systemPath.getTmpDir("14");
			strFile = strTmpPath + itemID + "." + userID + ".sh";
			writer = new OutputStreamWriter( new FileOutputStream(strFile));
			writer.write("cd "+strBinPath +"\n");
			//writer.write("ecams_sync " + itemID + " " + userID + "\n");
			writer.write("./ecams_batexec " +rtString + " \"cd " + strBinPath+ ";" +"./ecams_sync " + itemID + " " + userID + "\" \n");
			writer.write("exit $?\n");
			writer.close();
			
			strAry = new String[3]; 
			
			strAry[0] = "chmod";
			strAry[1] = "777";
			strAry[2] = strFile;
			run = Runtime.getRuntime();

			p = run.exec(strAry);
			p.waitFor();
			StreamGobbler outgb = new StreamGobbler(p.getInputStream());
			StreamGobbler errgb = new StreamGobbler(p.getErrorStream());

			outgb.start();
			errgb.start();				
			run = Runtime.getRuntime();
			
			strAry = new String[2];
			
			strAry[0] = "/bin/sh";
			strAry[1] = strFile;
			
			p = run.exec(strAry);
			outgb = new StreamGobbler(p.getInputStream());
			errgb = new StreamGobbler(p.getErrorStream());

			outgb.start();
			errgb.start();
			p.waitFor();
			
			if (p.exitValue() != 0) {
	   				returnStr = "N";

	   		}
	   		else{
	   			shfile.delete();
	   		}	 	   				
	   		outgb = null;
	   		errgb = null;
	   		
	   		conn.close(); //수정
	   		pstmt.close();
			pstmt = null;
			conn = null;
			rs.close();// 20221219 ecams_batexec 추가 쿼리
			rs = null;
	   		return returnStr;
	    } catch (SQLException sqlexception) {
			if (conn != null){
				conn.close();conn = null;
			}
			sqlexception.printStackTrace();
			throw sqlexception;
		} catch (Exception exception) {
			if (conn != null){
				conn.close();conn = null;
			}
			exception.printStackTrace();
			throw exception;
		}finally{
	
			if (pstmt != null)  try{pstmt.close();}catch (Exception ex2){ex2.printStackTrace();}
			if (conn != null){
				try{
					ConnectionResource.release(conn);
				}catch(Exception ex3){
					ex3.printStackTrace();
				}
			}
		}
    }
    
	public String adminUser(String userID) throws SQLException, Exception {
		Connection        conn        = null;
		PreparedStatement pstmt       = null;
		ResultSet         rs          = null;
		StringBuffer      strQuery    = new StringBuffer();
		String 			  strReturn   = "";

		
		ConnectionContext connectionContext = new ConnectionResource();
		
		try {
			conn = connectionContext.getConnection();
			
	        strQuery.setLength(0);
	        strQuery.append(" select cm_userid			 	\n");
	        strQuery.append(" from cmm0040          		\n");
	        strQuery.append(" where cm_userid = ?		    \n");
	        strQuery.append(" and   cm_admin = '1'			\n");
	        
	        pstmt = conn.prepareStatement(strQuery.toString());
	        pstmt.setString(1, userID);
	        rs = pstmt.executeQuery();
			if (rs.next()){
				strReturn = "Y";
			}else{
				strReturn = "N";
			}
			rs.close();
			pstmt.close();
			
			strQuery.setLength(0);
			if (strReturn != "Y"){
		        strQuery.append(" select cm_userid			 	\n");
		        strQuery.append(" from cmm0043          		\n");
		        strQuery.append(" where cm_userid = ?		    \n");
		        strQuery.append(" and   cm_rgtcd = 'Q1'			\n");
		        
		        pstmt = conn.prepareStatement(strQuery.toString());
		        pstmt.setString(1, userID);
		        rs = pstmt.executeQuery();
		        
		        if (rs.next()){
		        	strReturn = "Y";
		        }else{
		        	strReturn = "N";
		        }
		        rs = pstmt.executeQuery();
				rs.close();
				pstmt.close();
			}
		


			conn.close();
			rs = null;
			pstmt = null;
			conn = null;
			
			return strReturn;


		}catch (SQLException sqlexception) {
			sqlexception.printStackTrace();
			ecamsLogger.error("## eCmd0500.adminUser() SQLException START ##");
			ecamsLogger.error("## Error DESC : ", sqlexception);	
			ecamsLogger.error("## eCmd0500.adminUser() SQLException END ##");			
			throw sqlexception;
		} catch (Exception exception) {
			exception.printStackTrace();
			ecamsLogger.error("## eCmd0500.adminUser() Exception START ##");				
			ecamsLogger.error("## Error DESC : ", exception);	
			ecamsLogger.error("## eCmd0500.adminUser() Exception END ##");				
			throw exception;
		}finally{
			if (strQuery != null) 	strQuery = null;
			if (rs != null)     try{rs.close();}catch (Exception ex){ex.printStackTrace();}
			if (pstmt != null)  try{pstmt.close();}catch (Exception ex2){ex2.printStackTrace();}
			if (conn != null){
				try{
					ConnectionResource.release(conn);
				}catch(Exception ex3){
					ecamsLogger.error("## eCmd0500.adminUser() connection release exception ##");
					ex3.printStackTrace();
				}
			}
		}
	}//end of normalOtherUser() method statemen
	
	public String insertProject(ArrayList<HashMap<String,String>> etcData) throws SQLException, Exception {
		Connection        conn        = null;
		PreparedStatement pstmt       = null;
		ResultSet         rs          = null;
		StringBuffer      strQuery    = new StringBuffer();
		String            returnMSG   = "N";
		String            sameDocID        = "";
		String            CONN        = "";
		ConnectionContext connectionContext = new ConnectionResource();
		
		try {
			conn = connectionContext.getConnection();
			
			for (int i=0; i < etcData.size(); i++){
				
				/*
				strQuery.setLength(0);
				strQuery.append(" select CR_DOCID from cmr0040		\n");
				strQuery.append(" where CR_DOCID = ?				\n");
				strQuery.append(" and CR_SYSCD = ?                  \n");
				strQuery.append(" and CR_ITEMID = ?					\n");
				pstmt = conn.prepareStatement(strQuery.toString());
				//pstmt = new LoggableStatement(conn,strQuery.toString());
				pstmt.setString(1, etcData.get(i).get("CR_DOCID"));
				pstmt.setString(2, etcData.get(i).get("CR_SYSCD"));
				pstmt.setString(3, etcData.get(i).get("CR_ITEMID"));
				//ecamsLogger.error(((LoggableStatement)pstmt).getQueryString());
				rs = pstmt.executeQuery();
				if (rs.next()){
					sameDocID = "Y";
				}else{
					sameDocID = "N";
				}
				
				rs.close();
				pstmt.close();
				
				strQuery.setLength(0);
				strQuery.append(" delete cmr0040		\n");
				strQuery.append(" where CR_DOCID = ?	\n");
				strQuery.append(" and CR_SYSCD = ? 		\n");
				strQuery.append(" and CR_ITEMID = ?		\n");
				pstmt = conn.prepareStatement(strQuery.toString());
		        //pstmt = new LoggableStatement(conn,strQuery.toString());
				pstmt.setString(1, etcData.get(i).get("CR_DOCID"));
				pstmt.setString(2, etcData.get(i).get("CR_SYSCD"));
				pstmt.setString(3, etcData.get(i).get("CR_ITEMID"));
				//ecamsLogger.error(((LoggableStatement)pstmt).getQueryString());
				pstmt.executeUpdate();
				pstmt.close();
				*/
				CONN = etcData.get(i).get("CR_CONN");
				if (  CONN.equals("N") ){
					System.out.println("if");
					strQuery.setLength(0);
					strQuery.append(" delete cmr0040		\n");
					strQuery.append(" where CR_DOCID = ?	\n");
					strQuery.append(" and CR_SYSCD = ? 		\n");
					strQuery.append(" and CR_ITEMID = ?		\n");
					pstmt = conn.prepareStatement(strQuery.toString());
			        //pstmt = new LoggableStatement(conn,strQuery.toString());
					pstmt.setString(1, etcData.get(i).get("CR_DOCID"));
					pstmt.setString(2, etcData.get(i).get("CR_SYSCD"));
					pstmt.setString(3, etcData.get(i).get("CR_ITEMID"));
					//ecamsLogger.error(((LoggableStatement)pstmt).getQueryString());
					pstmt.executeUpdate();
					pstmt.close();
					returnMSG = "Y";
				}else{
					System.out.println("else");
					strQuery.setLength(0);
					strQuery.append(" delete cmr0040		\n");
					strQuery.append(" where CR_DOCID = ?	\n");
					strQuery.append(" and CR_SYSCD = ? 		\n");
					strQuery.append(" and CR_ITEMID = ?		\n");
					pstmt = conn.prepareStatement(strQuery.toString());
			        //pstmt = new LoggableStatement(conn,strQuery.toString());
					pstmt.setString(1, etcData.get(i).get("CR_DOCID"));
					pstmt.setString(2, etcData.get(i).get("CR_SYSCD"));
					pstmt.setString(3, etcData.get(i).get("CR_ITEMID"));
					//ecamsLogger.error(((LoggableStatement)pstmt).getQueryString());
					pstmt.executeUpdate();
					pstmt.close();
					
			        strQuery.setLength(0);
			        strQuery.append(" insert into cmr0040 values		\n");
			        strQuery.append(" ( ?, ?, ?, ?, ?, ?, ? )			\n");
			        
			        pstmt = conn.prepareStatement(strQuery.toString());
			        //pstmt = new LoggableStatement(conn,strQuery.toString());
			        pstmt.setString(1, etcData.get(i).get("CR_PRJNO"));
			        pstmt.setString(2, etcData.get(i).get("CR_DOCID"));
			        pstmt.setString(3, etcData.get(i).get("CR_DOCFILE"));
			        pstmt.setString(4, etcData.get(i).get("CR_ITEMID"));
			        pstmt.setString(5, etcData.get(i).get("CR_SYSCD"));
			        pstmt.setString(6, etcData.get(i).get("CR_RSRCNAME"));
			        pstmt.setString(7, etcData.get(i).get("CR_CONN"));
			        //ecamsLogger.error(((LoggableStatement)pstmt).getQueryString());
			        pstmt.executeUpdate();
					pstmt.close();
					returnMSG = "Y";
				}

			}

			conn.close();
			pstmt = null;
			conn = null;
			return returnMSG;		


		}catch (SQLException sqlexception) {
			sqlexception.printStackTrace();
			ecamsLogger.error("## eCmd0500.adminUser() SQLException START ##");
			ecamsLogger.error("## Error DESC : ", sqlexception);	
			ecamsLogger.error("## eCmd0500.adminUser() SQLException END ##");			
			throw sqlexception;
		} catch (Exception exception) {
			exception.printStackTrace();
			ecamsLogger.error("## eCmd0500.adminUser() Exception START ##");				
			ecamsLogger.error("## Error DESC : ", exception);	
			ecamsLogger.error("## eCmd0500.adminUser() Exception END ##");				
			throw exception;
		}finally{
			if (strQuery != null) 	strQuery = null;
			if (pstmt != null)  try{pstmt.close();}catch (Exception ex2){ex2.printStackTrace();}
			if (conn != null){
				try{
					ConnectionResource.release(conn);
				}catch(Exception ex3){
					ecamsLogger.error("## eCmd0500.adminUser() connection release exception ##");
					ex3.printStackTrace();
				}
			}
		}
	}//end of normalOtherUser() method statemen
	
	public Object[] getCheckConn(String itemID, String syscd) throws SQLException, Exception {
		Connection        conn        = null;
		PreparedStatement pstmt       = null;
		ResultSet         rs          = null;
		StringBuffer      strQuery    = new StringBuffer();
		String 			  strReturn   = "";
		ArrayList<HashMap<String, String>>  rsval = new ArrayList<HashMap<String, String>>();
		HashMap<String, String>			  	rst	  = null;
		
		ConnectionContext connectionContext = new ConnectionResource();
		
		try {
			conn = connectionContext.getConnection();
			
	        strQuery.setLength(0);
	        strQuery.append(" select b.CR_DOCID, b.CR_PRJNO, b.CR_DOCSEQ 			\n");
	        strQuery.append(" from cmr0040 a, cmr0030 b          					\n");
	        strQuery.append(" where a.CR_ITEMID = ?									\n");
	        strQuery.append(" and   a.CR_SYSCD = ?            						\n");
	        strQuery.append(" and   a.CR_DOCID = a.CR_DOCID            				\n");
	        strQuery.append(" and   a.CR_PRJNO = a.CR_PRJNO            				\n");
	        pstmt = conn.prepareStatement(strQuery.toString());
	        //pstmt = new LoggableStatement(conn,strQuery.toString());
	        pstmt.setString(1, itemID);
	        pstmt.setString(2, syscd);
	        //ecamsLogger.error(((LoggableStatement)pstmt).getQueryString());
	        rs = pstmt.executeQuery();
	        
	        while (rs.next()){
	        	rst = new HashMap<String, String>();
	        	rst.put("CR_DOCID", rs.getString("CR_DOCID"));
	        	rst.put("CR_PRJNO", rs.getString("CR_PRJNO"));
	        	rst.put("CR_DOCSEQ", rs.getString("CR_DOCSEQ"));
	        	
	        	rsval.add(rst);
	        	rst = null;
	        }
	        rs.close();
	        pstmt.close();
			conn.close();
			rs = null;
			pstmt = null;
			conn = null;
			
			return rsval.toArray();


		}catch (SQLException sqlexception) {
			sqlexception.printStackTrace();
			ecamsLogger.error("## eCmd0500.adminUser() SQLException START ##");
			ecamsLogger.error("## Error DESC : ", sqlexception);	
			ecamsLogger.error("## eCmd0500.adminUser() SQLException END ##");			
			throw sqlexception;
		} catch (Exception exception) {
			exception.printStackTrace();
			ecamsLogger.error("## eCmd0500.adminUser() Exception START ##");				
			ecamsLogger.error("## Error DESC : ", exception);	
			ecamsLogger.error("## eCmd0500.adminUser() Exception END ##");				
			throw exception;
		}finally{
			if (strQuery != null) 	strQuery = null;
			if (rs != null)     try{rs.close();}catch (Exception ex){ex.printStackTrace();}
			if (pstmt != null)  try{pstmt.close();}catch (Exception ex2){ex2.printStackTrace();}
			if (conn != null){
				try{
					ConnectionResource.release(conn);
				}catch(Exception ex3){
					ecamsLogger.error("## eCmd0500.adminUser() connection release exception ##");
					ex3.printStackTrace();
				}
			}
		}
	}
	
	public Object[] getDoc(String PrjNo, String ItemId) throws SQLException, Exception {	
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
			strQuery.append("select a.cr_prjno, a.cr_docid, a.cr_docfile, a.cr_itemid, a.cr_syscd, a.cr_rsrcname, a.cr_conn, 	\n");
			strQuery.append("		b.cr_lstver, b.cr_dirpath, b.cr_docseq, b.cr_docsta, b.cr_status, c.cm_codename 			\n");
			strQuery.append("from cmr0040 a, cmr0030 b ,cmm0020	c																\n");
			strQuery.append("where a.cr_itemid=? and a.cr_prjno = ? and b.cr_prjno = a.cr_prjno and a.cr_docid = b.cr_docid 	\n");
			strQuery.append("and   b.cr_status = c.cm_micode and c.cm_macode = 'CMR0020' \n");
	        pstmt = conn.prepareStatement(strQuery.toString());
		    pstmt.setString(1, ItemId);
		    pstmt.setString(2, PrjNo);
	        rs = pstmt.executeQuery();
			while (rs.next()){
				rst = new HashMap<String,String>();
				rst.put("cr_prjno",rs.getString("cr_prjno"));
				rst.put("cr_rsrcname",rs.getString("cr_rsrcname"));
				rst.put("cr_docid",rs.getString("cr_docid"));
				rst.put("cr_docfile",rs.getString("cr_docfile"));
				rst.put("DocName",rs.getString("cr_docfile"));
				rst.put("cr_itemid",rs.getString("cr_itemid"));
				rst.put("cr_syscd",rs.getString("cr_syscd"));
				rst.put("cr_rsrcname",rs.getString("cr_rsrcname"));
				rst.put("cr_conn",rs.getString("cr_conn"));
				rst.put("cr_lstver",rs.getString("cr_lstver"));
				rst.put("cr_dirpath",rs.getString("cr_dirpath"));
				rst.put("cr_docseq",rs.getString("cr_docseq"));
				rst.put("cr_docsta",rs.getString("cr_docsta"));
				rst.put("status",rs.getString("cr_status"));
				rst.put("cr_status", rs.getString("cm_codename"));
				rtList.add(rst);
				rst = null;
			}//end of while-loop statement
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
			if (conn != null){
				conn.close();conn = null;
			}
			sqlexception.printStackTrace();
			throw sqlexception;
		} catch (Exception exception) {
			if (conn != null){
				conn.close();conn = null;
			}
			exception.printStackTrace();
			throw exception;
		}finally{
			if (strQuery != null)	strQuery = null;
			if (rtObj != null)	rtObj = null;				
			if (rs != null)     try{rs.close();}catch (Exception ex){ex.printStackTrace();}
			if (pstmt != null)  try{pstmt.close();}catch (Exception ex2){ex2.printStackTrace();}
			if (conn != null){
				try{
					ConnectionResource.release(conn);
				}catch(Exception ex3){
					ex3.printStackTrace();
				}
			}
		}
	}//end of getItemId() method statement

	// 20230131 RPA 변경 버튼
	  public int btnRPA_Click(String itemId) throws SQLException, Exception {	
			Connection        conn        = null;
			PreparedStatement pstmt       = null;
			ResultSet         rs          = null;
			StringBuffer      strQuery    = new StringBuffer();
			int				  rtn_cnt     = 0;
			String            strIsrId    = "";
			String            strIsrSub   = "";
			ConnectionContext connectionContext = new ConnectionResource();
			try {
				conn = connectionContext.getConnection();

		        strQuery.setLength(0);
		    	strQuery.append("update cmr0020 set cr_rpaflag = 'Y' \n");
		    	strQuery.append(" where cr_itemid=? \n");//L_ItemId
				pstmt = conn.prepareStatement(strQuery.toString());
	            pstmt.setString(1, itemId);
	            rtn_cnt = pstmt.executeUpdate();
	            pstmt.close();
		
	            conn.close();
			    pstmt = null;
			    rs = null;
			    conn = null;
			    
	            return rtn_cnt;
	            
			} catch (SQLException sqlexception) {
				if (conn != null){
					conn.close();conn = null;
				}
				sqlexception.printStackTrace();
				throw sqlexception;
			} catch (Exception exception) {
				if (conn != null){
					conn.close();conn = null;
				}
				exception.printStackTrace();
				throw exception;
			}finally{
				if (strQuery != null)	strQuery = null;
				if (pstmt != null)  try{pstmt.close();}catch (Exception ex2){ex2.printStackTrace();}
				if (rs != null)  try{rs.close();}catch (Exception ex2){ex2.printStackTrace();}
				if (conn != null){
					try{
						ConnectionResource.release(conn);
					}catch(Exception ex3){
						ex3.printStackTrace();
					}
				}
			}
	    }//end of btnRPA_Click() method statement
	
}//end of Cmd0500 class statement
