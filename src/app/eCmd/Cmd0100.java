/*****************************************************************************************
	1. program ID	: Cmd0100.java
	2. create date	: 2006.08. 08
	3. auth		    : is.choi
	4. update date	: 2009.03
	5. auth		    : No Name
	6. description	: 1. USER INFO.
*****************************************************************************************/

package app.eCmd;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.apache.logging.log4j.Logger;

import app.common.LoggableStatement;
import app.common.eCAMSInfo;

import com.ecams.common.dbconn.ConnectionContext;
import com.ecams.common.dbconn.ConnectionResource;
import com.ecams.common.logger.EcamsLogger;

import java.util.ArrayList;
import java.util.HashMap;
import app.common.UserInfo;

/**
 * @author bigeyes
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class Cmd0100{     
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
	public Object[] getDir(String UserID,String SysCd,String SecuYn,String RsrcCd,String JobCd,String SelMsg) throws SQLException, Exception {
		Connection        conn        = null;
		PreparedStatement pstmt       = null;
		ResultSet         rs          = null;
		StringBuffer      strQuery    = new StringBuffer();

		ArrayList<HashMap<String, String>>  rsval = new ArrayList<HashMap<String, String>>();
		HashMap<String, String>			  rst		  = null;	
		Object[] returnObjectArray = null;
		
		String   strSelMsg   = "";
		String   strJob[] = null;
		if (JobCd != null && !"".equals(JobCd)) {
			strJob = JobCd.split(",");
		} else {
			JobCd = "";
		}
	    int      i = 0;
	    int      parmCnt = 0;
	    
		ConnectionContext connectionContext = new ConnectionResource();		
		
		try {
			conn = connectionContext.getConnection();
			
			if (!"".equals(SelMsg)) {
				if (SelMsg.equals("ALL")) strSelMsg = "전체";
				else if (SelMsg.equals("SEL")) strSelMsg = "선택하세요";
			}
			//JobCd =JobCd.replace(",", "','");
			strQuery.append("select a.cm_dsncd,a.cm_dirpath                             \n");
			strQuery.append(" from cmm0070 a,cmm0072 c,cmm0073 b                        \n");
			strQuery.append("where b.cm_syscd=?                                         \n");
			if (!SecuYn.equals("Y")) {
				strQuery.append("  and b.cm_jobcd in (select cm_jobcd from cmm0044      \n");
				strQuery.append("                      where cm_userid=? and cm_syscd=? \n");
				strQuery.append("                        and cm_closedt is null         \n");
				if (JobCd.length()>0) {
					strQuery.append("                        and cm_jobcd in ( \n");
					for (i=0;strJob.length>i;i++) {
						if (i>0) strQuery.append(", ? ");
						else strQuery.append("? ");
					}
					strQuery.append("))                                                  \n");
				} else {
					strQuery.append(")                                                   \n");
				}
			} else if (JobCd.length()>0) {
				strQuery.append("and b.cm_jobcd in ( \n");
				for (i=0;strJob.length>i;i++) {
					if (i>0) strQuery.append(", ? ");
					else strQuery.append("? ");
				}
				strQuery.append(")                                                  \n");
			}
			strQuery.append("  and b.cm_syscd=a.cm_syscd and b.cm_dsncd=a.cm_dsncd  \n");
			strQuery.append("  and c.cm_rsrccd=?                                    \n");
			strQuery.append("  and c.cm_syscd=a.cm_syscd and c.cm_dsncd=a.cm_dsncd  \n");
			strQuery.append("  and a.cm_clsdt is null                               \n");
			strQuery.append("group by a.cm_dirpath,a.cm_dsncd                       \n"); 	
			strQuery.append("order by a.cm_dirpath                                  \n"); 			
			
            pstmt = conn.prepareStatement(strQuery.toString());
 //           pstmt =  new LoggableStatement(conn, strQuery.toString());
            pstmt.setString(++parmCnt, SysCd);
            if (!SecuYn.equals("Y")) {
            	pstmt.setString(++parmCnt, UserID);
            	pstmt.setString(++parmCnt, SysCd);
            }
            if (JobCd.length()>0) {
	            for (i=0;strJob.length>i;i++) {
	            	pstmt.setString(++parmCnt, strJob[i]);
	            }
            }
            pstmt.setString(++parmCnt, RsrcCd);
  //          ecamsLogger.error(((LoggableStatement)pstmt).getQueryString());           
            rs = pstmt.executeQuery();
            
            rsval.clear();
            
			while (rs.next()){
				if (rs.getRow() == 1 && !"".equals(strSelMsg)) {
				   rst = new HashMap<String, String>();
				   rst.put("ID", "0");
				   rst.put("cm_dsncd", "0000");
				   rst.put("cm_dirpath", strSelMsg);
				   rsval.add(rst);
				   rst = null;
				}
				rst = new HashMap<String, String>();
				rst.put("ID", Integer.toString(rs.getRow()));
				rst.put("cm_dsncd", rs.getString("cm_dsncd"));
				rst.put("cm_dirpath", rs.getString("cm_dirpath"));
				rsval.add(rst);
				rst = null;
			}//end of while-loop statement
			rs.close();
			pstmt.close();
			conn.close();
			

			rs = null;
			pstmt = null;
			conn = null;
			
			returnObjectArray = rsval.toArray();
			rsval.clear();
			rsval = null;

			return returnObjectArray;
			
		} catch (SQLException sqlexception) {
			sqlexception.printStackTrace();
			ecamsLogger.error("## Cmd0100.getDir() SQLException START ##");
			ecamsLogger.error("## Error DESC : ", sqlexception);	
			ecamsLogger.error("## Cmd0100.getDir() SQLException END ##");			
			throw sqlexception;
		} catch (Exception exception) {
			exception.printStackTrace();
			ecamsLogger.error("## Cmd0100.getDir() Exception START ##");				
			ecamsLogger.error("## Error DESC : ", exception);	
			ecamsLogger.error("## Cmd0100.getDir() Exception END ##");				
			throw exception;
		}finally{
			if (strQuery != null)	strQuery = null;
			if (returnObjectArray != null)	returnObjectArray = null;
			if (rs != null)     try{rs.close();}catch (Exception ex){ex.printStackTrace();}
			if (pstmt != null)  try{pstmt.close();}catch (Exception ex2){ex2.printStackTrace();}
			if (conn != null){
				try{
					ConnectionResource.release(conn);
				}catch(Exception ex3){
					ecamsLogger.error("## Cmd0100.getDir() connection release exception ##");
					ex3.printStackTrace();
				}
			}
		}		
	}//end of getDir() method statement
	
	public Object[] getDir_Tmax(String UserID,String SysCd,String RsrcCd) throws SQLException, Exception {
		Connection        conn        = null;
		PreparedStatement pstmt       = null;
		ResultSet         rs          = null;
		StringBuffer      strQuery    = new StringBuffer();

		ArrayList<HashMap<String, String>>  rsval = new ArrayList<HashMap<String, String>>();
		HashMap<String, String>			  rst		  = null;	
		
		Object[] returnObjectArray = null;
		ConnectionContext connectionContext = new ConnectionResource();		
		
		try {
			conn = connectionContext.getConnection();
						
			strQuery.append("select substr(b.cm_dirpath,1,instr(b.cm_dirpath,'/',-1,1)-1) volpath \n");
			strQuery.append(" from cmm0070 b,cmm0072 a                                            \n");
			strQuery.append("where a.cm_syscd=? and a.cm_rsrccd=?                                 \n");
			strQuery.append("  and a.cm_syscd=b.cm_syscd and a.cm_dsncd=b.cm_dsncd                \n");
			strQuery.append("  and b.cm_dirpath like '%/src/serviceModule/%'                      \n");
			strQuery.append(" group by substr(b.cm_dirpath,1,instr(b.cm_dirpath,'/',-1,1)-1)      \n");
			strQuery.append(" order by substr(b.cm_dirpath,1,instr(b.cm_dirpath,'/',-1,1)-1)      \n");
			
            pstmt = conn.prepareStatement(strQuery.toString());
            pstmt.setString(1, SysCd);
            pstmt.setString(2, RsrcCd);
                       
            rs = pstmt.executeQuery();
            
            rsval.clear();
            while (rs.next()){
            	rst = new HashMap<String, String>();
				rst.put("ID", Integer.toString(rs.getRow()));
				rst.put("cm_dirpath", rs.getString("volpath"));
				rsval.add(rst);
				rst = null;
            }
            rs.close();
            pstmt.close();
            conn.close();

			rs = null;
			pstmt = null;
			conn = null;
			
    		returnObjectArray = rsval.toArray();
    		
    		rsval = null;
    		//ecamsLogger.debug(rsval.toString());		
    		return returnObjectArray;            
            
            
		} catch (SQLException sqlexception) {
			sqlexception.printStackTrace();
			ecamsLogger.error("## Cmd0100.getDir_Tmax() SQLException START ##");
			ecamsLogger.error("## Error DESC : ", sqlexception);	
			ecamsLogger.error("## Cmd0100.getDir_Tmax() SQLException END ##");			
			throw sqlexception;
		} catch (Exception exception) {
			exception.printStackTrace();
			ecamsLogger.error("## Cmd0100.getDir_Tmax() Exception START ##");				
			ecamsLogger.error("## Error DESC : ", exception);	
			ecamsLogger.error("## Cmd0100.getDir_Tmax() Exception END ##");				
			throw exception;
		}finally{
			if (strQuery != null)	strQuery = null;
			if (returnObjectArray != null)	returnObjectArray = null;			
			if (rs != null)     try{rs.close();}catch (Exception ex){ex.printStackTrace();}
			if (pstmt != null)  try{pstmt.close();}catch (Exception ex2){ex2.printStackTrace();}
			if (conn != null){
				try{
					ConnectionResource.release(conn);
				}catch(Exception ex3){
					ecamsLogger.error("## Cmd0100.getDir_Tmax() connection release exception ##");
					ex3.printStackTrace();
				}
			}
		}		

		
	}//end of getDir_Tmax() method statement
	
	
	public String getDir_Tmax2(String UserID,String SysCd,String RsrcCd) throws SQLException, Exception {
		Connection        conn        = null;
		PreparedStatement pstmt       = null;
		ResultSet         rs          = null;
		StringBuffer      strQuery    = new StringBuffer();
		String pt = "";

		ArrayList<HashMap<String, String>>  rsval = new ArrayList<HashMap<String, String>>();
		HashMap<String, String>			  rst		  = null;	
		
		Object[] returnObjectArray = null;
		ConnectionContext connectionContext = new ConnectionResource();		
		
		try {
			conn = connectionContext.getConnection();
						
			strQuery.append("select a.cm_volpath volpath1						    \n");
			strQuery.append(" from cmm0038 a,cmm0031 b                              \n");
			strQuery.append("where b.cm_syscd=? and b.cm_svrcd='01'                 \n");
			strQuery.append("  and b.cm_syscd=a.cm_syscd and b.cm_svrcd=a.cm_svrcd  \n");
			strQuery.append("  and b.cm_seqno=a.cm_seqno and a.cm_rsrccd=?          \n");
			
            pstmt = conn.prepareStatement(strQuery.toString());
            pstmt.setString(1, SysCd);
            pstmt.setString(2, RsrcCd);
                       
            rs = pstmt.executeQuery();
            
            if (rs.next()){
            	pt = rs.getString("cm_volpath");
            }	
            
            rs.close();
            pstmt.close();
            conn.close();

			rs = null;
			pstmt = null;
			conn = null;
					
    		return pt;            
            
            
		} catch (SQLException sqlexception) {
			sqlexception.printStackTrace();
			ecamsLogger.error("## Cmd0100.getDir_Tmax() SQLException START ##");
			ecamsLogger.error("## Error DESC : ", sqlexception);	
			ecamsLogger.error("## Cmd0100.getDir_Tmax() SQLException END ##");			
			throw sqlexception;
		} catch (Exception exception) {
			exception.printStackTrace();
			ecamsLogger.error("## Cmd0100.getDir_Tmax() Exception START ##");				
			ecamsLogger.error("## Error DESC : ", exception);	
			ecamsLogger.error("## Cmd0100.getDir_Tmax() Exception END ##");				
			throw exception;
		}finally{
			if (strQuery != null)	strQuery = null;
			if (returnObjectArray != null)	returnObjectArray = null;			
			if (rs != null)     try{rs.close();}catch (Exception ex){ex.printStackTrace();}
			if (pstmt != null)  try{pstmt.close();}catch (Exception ex2){ex2.printStackTrace();}
			if (conn != null){
				try{
					ConnectionResource.release(conn);
				}catch(Exception ex3){
					ecamsLogger.error("## Cmd0100.getDir_Tmax() connection release exception ##");
					ex3.printStackTrace();
				}
			}
		}		

		
	}//end of getDir_Tmax() method statement

	public String cmm0070_Insert(String UserID,String SysCd,String RsrcName,String RsrcCd,String JobCd,String DirPath,boolean tmaxFg,Connection conn) throws SQLException, Exception {
		PreparedStatement pstmt       = null;
		PreparedStatement pstmt2       = null;
		PreparedStatement pstmt3       = null;
		ResultSet         rs          = null;
		ResultSet         rs2         = null;
		StringBuffer      strQuery    = new StringBuffer();
		
	    String            strDsnCD    = null;
	    String            strBaseDir  = "";
	    int               rsCnt       = 0;
	    String            retMsg      = "";
		
		try {
			strBaseDir = DirPath;
			
			strQuery.append("select cm_dsncd from cmm0070                           \n");
			strQuery.append(" where cm_syscd=?                                      \n");
			strQuery.append(" and cm_dirpath=?                                      \n");
            pstmt = conn.prepareStatement(strQuery.toString());
            //pstmt = new LoggableStatement(conn,strQuery.toString());
            pstmt.setString(1, SysCd);
            pstmt.setString(2, strBaseDir);
            //ecamsLogger.error(((LoggableStatement)pstmt).getQueryString());
            rs = pstmt.executeQuery();
            
            if (!rs.next()){ 
            	strDsnCD = "0000001";
            	strQuery.setLength(0);                		
        		strQuery.append("select lpad(to_number(max(cm_dsncd)) + 1,7,'0') max  \n");
    			strQuery.append("from cmm0070                                         \n");
    			strQuery.append("where cm_syscd=?                                     \n");
    			
    			pstmt2 = conn.prepareStatement(strQuery.toString());
    			pstmt2.setString(1, SysCd);
                rs2 = pstmt2.executeQuery();
                if (rs2.next()) {
                	if (rs2.getString("max") != null){
                		strDsnCD = rs2.getString("max");
                	}
                	
                	strQuery.setLength(0);                		
            		strQuery.append("insert into cmm0070                                                        \n");              		
            		strQuery.append("  (CM_SYSCD,CM_DSNCD,CM_DIRPATH,CM_EDITOR,CM_OPENDT,CM_LASTUPDT,CM_RUNSTA) \n");        		
            		strQuery.append("  values                                                                   \n");
            		strQuery.append("  (?, ?, ?, ?, SYSDATE, SYSDATE, '0')                                      \n");
            		pstmt3 = conn.prepareStatement(strQuery.toString());	
            		pstmt3.setString(1, SysCd); 	        
            		pstmt3.setString(2, strDsnCD);         
            		pstmt3.setString(3, strBaseDir);         
            		pstmt3.setString(4, UserID); 
            		pstmt3.executeUpdate();
            		pstmt3.close();
            		
              		retMsg = strDsnCD;
              		
              		strQuery.setLength(0);                		
            		strQuery.append("insert into cmm0072                               \n");              		
            		strQuery.append("  (CM_SYSCD,CM_DSNCD,CM_RSRCCD) values            \n"); 
            		strQuery.append("  (?, ?, ?)                                       \n");
            		pstmt3 = conn.prepareStatement(strQuery.toString());	
            		pstmt3.setString(1, SysCd); 	        
            		pstmt3.setString(2, strDsnCD);         
            		pstmt3.setString(3, RsrcCd); 
            		pstmt3.executeUpdate();
            		pstmt3.close();
              		
            		int i=0;
            		String JobCdzip[] = JobCd.split(",");
            		for (i=0 ; i<JobCdzip.length ; i++){
                  		strQuery.setLength(0);
                		strQuery.append("insert into cmm0073                               \n");
                		strQuery.append("  (CM_SYSCD,CM_DSNCD,CM_JOBCD) values             \n");
                		strQuery.append("  (?, ?, ?)                                       \n");
                		pstmt3 = conn.prepareStatement(strQuery.toString());
                		pstmt3.setString(1, SysCd);
                		pstmt3.setString(2, strDsnCD);
                		pstmt3.setString(3, JobCdzip[i].toString());
                		pstmt3.executeUpdate();
                		pstmt3.close();
            		}
            		JobCdzip = null;
                }
                rs2.close();
                pstmt2.close();
                
            }
            else {
            	retMsg = rs.getString("cm_dsncd");
            }
            rs.close();
            pstmt.close();
            
            rs = null;
            pstmt = null;
            rs2 = null;
            pstmt2 = null;
            pstmt3 = null;
            
            return retMsg;
            
            
		} catch (SQLException sqlexception) {
			sqlexception.printStackTrace();
			ecamsLogger.error("## Cmd0100.cmm0070_Insert() SQLException START ##");
			ecamsLogger.error("## Error DESC : ", sqlexception);	
			ecamsLogger.error("## Cmd0100.cmm0070_Insert() SQLException END ##");			
			throw sqlexception;
		} catch (Exception exception) {
			exception.printStackTrace();
			ecamsLogger.error("## Cmd0100.cmm0070_Insert() Exception START ##");				
			ecamsLogger.error("## Error DESC : ", exception);	
			ecamsLogger.error("## Cmd0100.cmm0070_Insert() Exception END ##");				
			throw exception;
		}finally{
			if (strQuery != null)	strQuery = null;
			if (rs != null)     try{rs.close();}catch (Exception ex){ex.printStackTrace();}
			if (pstmt != null)  try{pstmt.close();}catch (Exception ex2){ex2.printStackTrace();}
			if (rs2 != null)     try{rs2.close();}catch (Exception ex){ex.printStackTrace();}
			if (pstmt2 != null)  try{pstmt2.close();}catch (Exception ex2){ex2.printStackTrace();}
			if (pstmt3 != null)  try{pstmt3.close();}catch (Exception ex2){ex2.printStackTrace();}
		}		
		
		
	}//end of cmm0070_Insert() method statement
	public Object[] getRsrcOpen(String SysCd,String SelMsg) throws SQLException, Exception {
		Connection        conn        = null;
		PreparedStatement pstmt       = null;
		ResultSet         rs          = null;
		StringBuffer      strQuery    = new StringBuffer();
	
		ArrayList<HashMap<String, String>>  rsval = new ArrayList<HashMap<String, String>>();
		HashMap<String, String>			  rst		  = null;	
		
		Object[] returnObjectArray = null;
		String            strSelMsg   = "";
	    		
		ConnectionContext connectionContext = new ConnectionResource();		
		
		try {
			conn = connectionContext.getConnection();
			
			if (!"".equals(SelMsg)) {
				if (SelMsg.equals("ALL")) strSelMsg = "전체";
				else if (SelMsg.equals("SEL")) strSelMsg = "선택하세요";
			}
			strQuery.setLength(0);
			strQuery.append("select a.cm_micode,a.cm_codename,b.cm_info,d.cm_volpath,b.cm_exename 	\n");
			strQuery.append("  from cmm0020 a,cmm0036 b,cmm0031 c,cmm0038 d          				\n");
			strQuery.append(" where b.cm_syscd=?                                     				\n");
			strQuery.append("   and b.cm_closedt is null                             				\n");
			strQuery.append("   and substr(b.cm_info,26,1)='0'                       				\n");
			strQuery.append("   and b.cm_rsrccd not in (select cm_samersrc           				\n");
			strQuery.append("                             from cmm0037               				\n");
			strQuery.append("                            where cm_syscd=?            				\n");
			strQuery.append("                              and cm_factcd='04')       				\n");
			strQuery.append("   and a.cm_macode='JAWON' and a.cm_micode=b.cm_rsrccd  				\n");
			strQuery.append("   and b.cm_syscd=d.cm_syscd and b.cm_rsrccd=d.cm_rsrccd				\n");
			strQuery.append("   and d.cm_svrcd = (select cm_dirbase          		   				\n");
			strQuery.append("   					from cmm0030 where cm_syscd=c.cm_syscd) 			\n");
			strQuery.append("   and d.cm_syscd=c.cm_syscd and d.cm_svrcd=c.cm_svrcd  				\n");
			strQuery.append("   and d.cm_seqno=c.cm_seqno and c.cm_closedt is null   				\n");
			strQuery.append("   order by a.cm_codename   											\n");
						
	        pstmt = conn.prepareStatement(strQuery.toString());	
	        pstmt.setString(1, SysCd);
	        pstmt.setString(2, SysCd);
	                   
	        rs = pstmt.executeQuery();
	        
	        rsval.clear();
	        
			while (rs.next()){
				if (rs.getRow() == 1 && !"".equals(strSelMsg)) {
				   rst = new HashMap<String, String>();
				   rst.put("ID", "0");
				   rst.put("cm_micode", "0000");
				   rst.put("cm_codename", strSelMsg);
				   rst.put("cm_info", "");
				   rsval.add(rst);
				   rst = null;
				}
				rst = new HashMap<String, String>();
				rst.put("ID", Integer.toString(rs.getRow()));
				rst.put("cm_micode", rs.getString("cm_micode"));
				rst.put("cm_codename", rs.getString("cm_codename"));
				rst.put("cm_info", rs.getString("cm_info"));
				rst.put("cm_volpath", rs.getString("cm_volpath"));
				rst.put("modsw", "N");
				if (rs.getString("cm_exename") != null && !"".equals(rs.getString("cm_exename"))) {
					if (rs.getString("cm_exename").substring(rs.getString("cm_exename").length() - 1).equals(","))
						rst.put("cm_exename", rs.getString("cm_exename"));
					else rst.put("cm_exename", rs.getString("cm_exename")+",");
				} else rst.put("cm_exename", "");
				rsval.add(rst);
				rst = null;
			}//end of while-loop statement
			rs.close();
			pstmt.close();
			conn.close();
			
			rs = null;
			pstmt = null;
			conn = null;
			
			returnObjectArray = rsval.toArray();
			rsval = null;
			//ecamsLogger.debug(rsval.toString());		
			return returnObjectArray;			
			
		} catch (SQLException sqlexception) {
			sqlexception.printStackTrace();
			ecamsLogger.error("## Cmd0100.getRsrcOpen() SQLException START ##");
			ecamsLogger.error("## Error DESC : ", sqlexception);	
			ecamsLogger.error("## Cmd0100.getRsrcOpen() SQLException END ##");			
			throw sqlexception;
		} catch (Exception exception) {
			exception.printStackTrace();
			ecamsLogger.error("## Cmd0100.getRsrcOpen() Exception START ##");				
			ecamsLogger.error("## Error DESC : ", exception);	
			ecamsLogger.error("## Cmd0100.getRsrcOpen() Exception END ##");				
			throw exception;
		}finally{
			if (returnObjectArray != null)	returnObjectArray = null;
			if (strQuery != null)	strQuery = null;
			if (rs != null)     try{rs.close();}catch (Exception ex){ex.printStackTrace();}
			if (pstmt != null)  try{pstmt.close();}catch (Exception ex2){ex2.printStackTrace();}
			if (conn != null){
				try{
					ConnectionResource.release(conn);
				}catch(Exception ex3){
					ecamsLogger.error("## Cmd0100.getRsrcOpen() connection release exception ##");
					ex3.printStackTrace();
				}
			}
		}		

	}//end of SelectRsrcOpen() method statement	
	
	public Object[] getLang(String SysCd,String RsrcCd,String SelMsg) throws SQLException, Exception {
		Connection        conn        = null;
		PreparedStatement pstmt       = null;
		ResultSet         rs          = null;
		StringBuffer      strQuery    = new StringBuffer();
	
		ArrayList<HashMap<String, String>>  rsval = new ArrayList<HashMap<String, String>>();
		HashMap<String, String>			  rst		  = null;	
		
		String            strSelMsg   = "";
		Object[] returnObjectArray = null;
	    		
		ConnectionContext connectionContext = new ConnectionResource();		
		
		try {
			conn = connectionContext.getConnection();
			
			if (!"".equals(SelMsg)) {
				if (SelMsg.equals("ALL")) strSelMsg = "전체";
				else if (SelMsg.equals("SEL")) strSelMsg = "선택하세요";
			}
			
			strQuery.append("select a.cm_micode,a.cm_codename             \n");
			strQuery.append("  from cmm0020 a,cmm0032 b                   \n");
			strQuery.append(" where b.cm_syscd=? and b.cm_rsrccd=?        \n");
			strQuery.append("   and a.cm_macode='LANGUAGE'                \n");
			strQuery.append("   and a.cm_micode=b.cm_langcd               \n");
						
	        pstmt = conn.prepareStatement(strQuery.toString());
	        //pstmt =  new LoggableStatement(conn, strQuery.toString());
	        pstmt.setString(1, SysCd);
	        pstmt.setString(2, RsrcCd);
	        //ecamsLogger.error(((LoggableStatement)pstmt).getQueryString());  
	        rs = pstmt.executeQuery();
	        
	        rsval.clear();
	        
			while (rs.next()){
				if (rs.getRow() == 1 && !"".equals(strSelMsg)) {
				   rst = new HashMap<String, String>();
				   rst.put("ID", "0");
				   rst.put("cm_micode", "0000");
				   rst.put("cm_codename", strSelMsg);
				   rsval.add(rst);
				   rst = null;
				}
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
			
			returnObjectArray = rsval.toArray();
			rsval = null;
			//ecamsLogger.debug(rsval.toString());		
			return returnObjectArray;			
			
		} catch (SQLException sqlexception) {
			sqlexception.printStackTrace();
			ecamsLogger.error("## Cmd0100.getLang() SQLException START ##");
			ecamsLogger.error("## Error DESC : ", sqlexception);	
			ecamsLogger.error("## Cmd0100.getLang() SQLException END ##");			
			throw sqlexception;
		} catch (Exception exception) {
			exception.printStackTrace();
			ecamsLogger.error("## Cmd0100.getLang() Exception START ##");				
			ecamsLogger.error("## Error DESC : ", exception);	
			ecamsLogger.error("## Cmd0100.getLang() Exception END ##");				
			throw exception;
		}finally{
			if (returnObjectArray != null)	returnObjectArray = null;
			if (strQuery != null)	strQuery = null;			
			if (rs != null)     try{rs.close();}catch (Exception ex){ex.printStackTrace();}
			if (pstmt != null)  try{pstmt.close();}catch (Exception ex2){ex2.printStackTrace();}
			if (conn != null){
				try{
					ConnectionResource.release(conn);
				}catch(Exception ex3){
					ecamsLogger.error("## Cmd0100.getLang() connection release exception ##");
					ex3.printStackTrace();
				}
			}
		}		

	}//end of getLang() method statement

	
	
	public Object[] getOpenList(String UserId,String SysCd,String RsrcCd,String Isrid,String RsrcName,boolean SecuSw) throws SQLException, Exception {
		Connection        conn        = null;
		PreparedStatement pstmt       = null;
		ResultSet         rs          = null;
		PreparedStatement pstmt2      = null;
		ResultSet         rs2         = null;
		StringBuffer      strQuery    = new StringBuffer();
	
		ArrayList<HashMap<String, String>>  rsval = new ArrayList<HashMap<String, String>>();
		HashMap<String, String>			  rst		  = null;	
		Object[] returnObjectArray = null;
		int               parmCnt  = 0;
		String			  strRsrcCd = "";
		String            strRsrc[] = null;
		int               i = 0;
		ConnectionContext connectionContext = new ConnectionResource();		
		
		try {
			conn = connectionContext.getConnection();
			
			UserInfo userinfo = new UserInfo();
			if (userinfo.isAdmin(UserId)) SecuSw = true;
			else SecuSw = false;

			strQuery.setLength(0);
			strQuery.append("select cm_rsrccd from cmm0036                     \n"); 
			strQuery.append(" where cm_syscd=? and cm_closedt is null          \n");
			//strQuery.append("   and substr(cm_info, 26, 1)='0'                 \n");
			strQuery.append("   and cm_rsrccd not in (select cm_samersrc       \n");
			strQuery.append("                           from cmm0037           \n");
			strQuery.append("                          where cm_syscd=?        \n");
			strQuery.append("                            and cm_factcd='04')   \n");
			strQuery.append(" order by cm_rsrccd 							   \n");
			
			pstmt = conn.prepareStatement(strQuery.toString());
			pstmt =  new LoggableStatement(conn, strQuery.toString());
            pstmt.setString(1, SysCd);
            pstmt.setString(2, SysCd);
            
            ecamsLogger.error(((LoggableStatement)pstmt).getQueryString());
            rs = pstmt.executeQuery();
            while (rs.next()) {
            	if (!"".equals(strRsrcCd)) strRsrcCd = strRsrcCd + ",";
            	strRsrcCd = strRsrcCd + rs.getString("cm_rsrccd");
            }
			rs.close();
			pstmt.close();
			
			strRsrc = strRsrcCd.split(",");
			
			rsval.clear();
			strQuery.setLength(0);
			strQuery.append("select a.cr_syscd,a.cr_dsncd,a.cr_rsrccd,a.cr_langcd,a.cr_extname,a.cr_exename,\n");
			strQuery.append("       a.cr_rsrcname,a.cr_story,a.cr_jobcd,                     				\n");
			strQuery.append("       to_char(a.cr_lastdate,'yyyy-mm-dd') lastdt,                             \n");
			strQuery.append("       a.cr_itemid,a.cr_jobcd,f.cm_jobname,                    				\n");
			strQuery.append("       e.cm_dirpath,b.cm_sysgb,c.cm_codename jawon, 							 \n");
			strQuery.append("       a.cr_orderid , g.cm_codename jawongubun , a.cr_langcd ,a.cr_compile ,   \n");
			strQuery.append("       a.cr_teamcd, a.cr_document, a.cr_sqlcheck,a.cr_master,i.cm_username,     \n");
			strQuery.append("       a.cr_dsncd2, a.cr_makecompile, a.cr_dsncd2home							     \n");
			strQuery.append("  from cmm0102 f,cmm0070 e,cmm0020 c, cmm0040 i,                               \n");
			strQuery.append("       cmm0030 b,cmr0020 a,cmm0020 g, cmm0020 h			 				    \n");
			if (!SecuSw) {
				   strQuery.append(",cmm0044 j                                              \n");   
			}
			strQuery.append(" where a.cr_syscd=?                                            \n");
			strQuery.append("   and a.cr_status='3'                                         \n");
			if (!SecuSw) {
				strQuery.append("and j.cm_userid=? and j.cm_closedt is null                 \n");
				strQuery.append("and a.cr_syscd=j.cm_syscd and a.cr_jobcd=j.cm_jobcd        \n");
			}
			if (!"".equals(RsrcCd) && RsrcCd != null) {
				strQuery.append("and a.cr_rsrccd=?                                          \n");
			} else {
				strQuery.append("and a.cr_rsrccd in (");
				for (i=0;strRsrc.length>i;i++) {
					if (i>0) strQuery.append(", ? ");
					else strQuery.append("? ");
				}
				strQuery.append(")                                                          \n");
			}
			if (!"".equals(RsrcName) && RsrcName != null) {
				strQuery.append("and upper(a.cr_rsrcname) like upper(?)                     \n");
			}
			strQuery.append("   and a.cr_syscd=b.cm_syscd                                   \n");
			strQuery.append("   and c.cm_macode='JAWON' and c.cm_micode=a.cr_rsrccd         \n");
			strQuery.append("   and g.cm_macode='LANGUAGE' and g.cm_micode=a.cr_langcd      \n");
			strQuery.append("   and a.cr_syscd=e.cm_syscd and a.cr_dsncd=e.cm_dsncd         \n");
			strQuery.append("   and a.cr_jobcd=f.cm_jobcd                                   \n");
			strQuery.append("   and h.cm_macode(+)='ETCTEAM' and a.cr_teamcd= h.cm_micode(+)      \n");
			strQuery.append("   and i.cm_userid=a.cr_master							      \n");
				
			pstmt = conn.prepareStatement(strQuery.toString());
			pstmt =  new LoggableStatement(conn, strQuery.toString());
	        
	        pstmt.setString(++parmCnt, SysCd);
	        if (!SecuSw) {
	        	pstmt.setString(++parmCnt,UserId);
	        }
	        if (!"".equals(RsrcCd) && RsrcCd != null) {
	        	pstmt.setString(++parmCnt, RsrcCd);
	        } else {
            	for (i=0;strRsrc.length>i;i++) {
            		pstmt.setString(++parmCnt, strRsrc[i]);
            	}
	        }
	        if (!"".equals(RsrcName) && RsrcName != null) {
	        	pstmt.setString(++parmCnt, "%"+RsrcName+"%");
			} 
	       
	        	
	        ecamsLogger.error(((LoggableStatement)pstmt).getQueryString());  
	        rs = pstmt.executeQuery();
	                
			while (rs.next()){
				rst = new HashMap<String, String>();
				rst.put("ID",Integer.toString(rs.getRow()));
				rst.put("cr_rsrcname",rs.getString("cr_rsrcname"));
				rst.put("cr_teamcd",rs.getString("cr_teamcd"));
				rst.put("cr_story",rs.getString("cr_story"));
				rst.put("cr_compile",rs.getString("cr_compile"));
				rst.put("cm_jobname",rs.getString("cm_jobname"));
				rst.put("jawon_code",rs.getString("jawon"));
				rst.put("langcd",rs.getString("jawongubun"));
				rst.put("cr_langcd",rs.getString("cr_langcd"));
				rst.put("cm_dirpath",rs.getString("cm_dirpath"));
				rst.put("cr_lastdate",rs.getString("lastdt"));
				rst.put("cr_syscd",rs.getString("cr_syscd"));
				rst.put("cr_dsncd",rs.getString("cr_dsncd"));
				rst.put("cm_sysgb",rs.getString("cm_sysgb"));
				rst.put("cr_jobcd",rs.getString("cr_jobcd"));
				rst.put("cm_jobname",rs.getString("cm_jobname"));
				rst.put("cr_rsrccd",rs.getString("cr_rsrccd"));
				rst.put("cr_itemid",rs.getString("cr_itemid"));
				rst.put("cr_document", rs.getString("cr_document"));
				rst.put("cr_sqlcheck", rs.getString("cr_sqlcheck"));
				rst.put("cr_master", rs.getString("cr_master"));
				rst.put("cm_username", rs.getString("cm_username"));
				rst.put("cr_exename", rs.getString("cr_exename"));
				rst.put("cr_extname", rs.getString("cr_extname"));
				rst.put("cr_dsncd2", rs.getString("cr_dsncd2"));
				rst.put("cr_makecompile", rs.getString("cr_makecompile"));
				rst.put("cr_dsncd2home", rs.getString("cr_dsncd2home"));
				
				rst.put("enabled", "1");
				rst.put("selected", "0");
				
				strQuery.setLength(0);
				strQuery.append("select count(*) cnt from cmr1010     \n");
				strQuery.append(" where cr_baseitem=?                 \n");
				strQuery.append("   and cr_status='9'                 \n");
				pstmt2 = conn.prepareStatement(strQuery.toString());
		        pstmt2.setString(1, rs.getString("cr_itemid"));        	
		        rs2 = pstmt2.executeQuery();		                
				if (rs2.next()){
					if (rs2.getInt("cnt") > 0) {
						rst.put("enabled", "0");
					}
				}
				rs2.close();
				pstmt2.close();
				
				rsval.add(rst);
				rst = null;
			}//end of while-loop statement
			rs.close();
			pstmt.close();
			conn.close();
			
			rs = null;
			pstmt = null;
			rs2 = null;
			pstmt2 = null;
			conn = null;
			
			returnObjectArray = rsval.toArray();
			rsval = null;
			//ecamsLogger.debug(rsval.toString());		
			return returnObjectArray;		
			
		} catch (SQLException sqlexception) {
			sqlexception.printStackTrace();
			ecamsLogger.error("## Cmd0100.getOpenList() SQLException START ##");
			ecamsLogger.error("## Error DESC : ", sqlexception);	
			ecamsLogger.error("## Cmd0100.getOpenList() SQLException END ##");			
			throw sqlexception;
		} catch (Exception exception) {
			exception.printStackTrace();
			ecamsLogger.error("## Cmd0100.getOpenList() Exception START ##");				
			ecamsLogger.error("## Error DESC : ", exception);	
			ecamsLogger.error("## Cmd0100.getOpenList() Exception END ##");				
			throw exception;
		}finally{
			if (returnObjectArray != null)	returnObjectArray = null;
			if (strQuery != null)	strQuery = null;			
			if (rs != null)     try{rs.close();}catch (Exception ex){ex.printStackTrace();}
			if (pstmt != null)  try{pstmt.close();}catch (Exception ex2){ex2.printStackTrace();}		
			if (rs2 != null)     try{rs2.close();}catch (Exception ex){ex.printStackTrace();}
			if (pstmt2 != null)  try{pstmt2.close();}catch (Exception ex2){ex2.printStackTrace();}
			if (conn != null){
				try{
					ConnectionResource.release(conn);
				}catch(Exception ex3){
					ecamsLogger.error("## Cmd0100.getOpenList() connection release exception ##");
					ex3.printStackTrace();
				}
			}
		}
	}//end of getOpenList() method statement
	
	
	public Object[] pgmCheck(String UserId,String SysCd,String OrderId, String DsnCd,String RsrcName,String RsrcCd,String JobCd,String ProgTit,String DirPath,String CM_info,boolean dirSw,String RsRcGuBun, String ComPile, String MakeComPile, String Team,
			String DOCCD, String ExeName, String MasterID, String EtcDsn, String EtcDsnHome) throws SQLException, Exception {
		Connection        conn        = null;
		PreparedStatement pstmt       = null;
		ResultSet         rs          = null;
		StringBuffer      strQuery    = new StringBuffer();
		String            retMsg      = "0";	
		String            BaseItem    = "";
		Object[] returnObjectArray = null;
		ArrayList<HashMap<String, String>>  rsval = new ArrayList<HashMap<String, String>>();
		HashMap<String, String>			  rst		  = null;	
		ConnectionContext connectionContext = new ConnectionResource();		
		
		try {
			
			conn = connectionContext.getConnection();
			
			if (dirSw) {
				int x = DirPath.lastIndexOf("/");
				String strPath = "";
				if (x >= 0) strPath = DirPath.substring(x);
				else strPath = DirPath;

				if (!strPath.equals(RsrcName)) {
					DirPath = DirPath + "/" + RsrcName;

					String strDsn = cmm0070_Insert(UserId,SysCd,RsrcName,RsrcCd,JobCd,DirPath,true,conn);	
					if (strDsn == null || strDsn.equals("")) {
						retMsg = "디렉토리 등록 처리 중 오류가 발생하였습니다. ["+DirPath+"/"+RsrcName+"]";
					} else DsnCd = strDsn;
				}
			} 
			if (retMsg.equals("0")) {
				strQuery.append("select a.cr_status,a.cr_lstver,a.cr_editor,b.cm_codename \n");
				strQuery.append("  from cmm0020 b,cmr0020 a                               \n");
				strQuery.append(" where a.cr_syscd=? and a.cr_dsncd=?                     \n");
				strQuery.append("   and a.cr_rsrcname=?                                   \n");
				strQuery.append("   and b.cm_macode='CMR0020' and b.cm_micode=a.cr_status \n");
							
		        pstmt = conn.prepareStatement(strQuery.toString());
		        pstmt =  new LoggableStatement(conn, strQuery.toString());
		        pstmt.setString(1, SysCd);   	
		        pstmt.setString(2, DsnCd);
//		        pstmt.setString(3, RsrcName);	// 20231130 확장자 필수 체크 - 시호정대리 요청
		        if (ExeName.length() > 0)
		        	pstmt.setString(3, RsrcName + "." + ExeName);
		        else
		        	pstmt.setString(3, RsrcName);
		        	
		        ecamsLogger.error(((LoggableStatement)pstmt).getQueryString());
		        rs = pstmt.executeQuery();
		        rsval.clear();
		        if (rs.next()) {
		        	if (rs.getString("cr_status").equals("3") && !rs.getString("cr_editor").equals(UserId))
		        		retMsg = "다른 사용자가 신규등록한 프로그램ID입니다.";
		        	else if (rs.getString("cr_status").equals("3")) retMsg = "0";
		        	else retMsg = "기 등록하여 운영 중인 프로그램ID입니다.";
		        }
		        rs.close();
		        pstmt.close();
		        
		        System.out.println("---------------------------------------------");
		        System.out.println(retMsg);
		        System.out.println("---------------------------------------------");
			}
	        if (retMsg.equals("0")) {
	        	retMsg = null;
	        	retMsg = cmr0020_Insert(UserId,SysCd,OrderId,DsnCd,RsrcName,RsrcCd,JobCd,ProgTit,"",CM_info,conn,RsRcGuBun,ComPile,MakeComPile,Team, "", DOCCD, ExeName, MasterID, EtcDsn, EtcDsnHome);
	        	if (retMsg.substring(0,1).equals("0")) {
	        		BaseItem = retMsg.substring(1);
	        		retMsg = "0";
	        		
	        		//retMsg = moduleChk(UserId,SysCd,DsnCd,RsrcName,RsrcCd,JobCd,ProgTit,DirPath,BaseItem,CM_info);
	        		
	        		strQuery.setLength(0);
	    			strQuery.append("select a.cr_syscd,a.cr_dsncd,a.cr_rsrccd,a.cr_langcd,a.cr_rsrcname,            \n");
	    			strQuery.append("       a.cr_story,to_char(a.cr_lastdate,'yyyy-mm-dd') lastdt,                  \n");
	    			strQuery.append("       a.cr_jobcd,f.cm_jobname,e.cm_dirpath,a.cr_jobcd,                        \n");
	    			strQuery.append("       c.cm_codename jawon,b.cm_sysgb,a.cr_orderid ,                           \n");
	    			strQuery.append("       a.CR_LANGCD,a.CR_TEAMCD,a.CR_COMPILE,                                   \n");
	    			strQuery.append("       a.CR_SQLCHECK, a.CR_DOCUMENT ,  							            \n");
	    			strQuery.append("       h.cm_codename cm_codename3  , i.cm_codename cm_codename4,               \n");
	    			strQuery.append("       j.cm_compscript			                                                \n");
	    			strQuery.append("  from cmm0400 j, cmm0020 i,cmm0020 h,cmm0102 f,cmm0070 e,         			\n");
	    			strQuery.append("   	cmm0020 c,cmm0030 b,cmr0020 a  											\n");
	    			strQuery.append(" where a.cr_itemid=?                                                           \n");
	    			strQuery.append("   and a.cr_syscd=b.cm_syscd                                                   \n");
	    			strQuery.append("   and c.cm_macode='JAWON' and c.cm_micode=a.cr_rsrccd                         \n");
	    			strQuery.append("   and a.cr_syscd=e.cm_syscd and a.cr_dsncd=e.cm_dsncd                         \n");
	    			strQuery.append("   and a.cr_jobcd=f.cm_jobcd                                                   \n");
	    			strQuery.append("   and h.cm_macode='DOCUMENT' and h.cm_micode=a.CR_DOCUMENT                    \n");
	    			strQuery.append("   and i.cm_macode='LANGUAGE' and i.cm_micode=a.cr_langcd                      \n");
	    			strQuery.append("   and j.cm_syscd = a.cr_syscd						                      		\n");
	    			strQuery.append("   and j.cm_gubun='01' and j.cm_compcd=a.cr_compile                      		\n");
	    			
	    	        //pstmt = conn.prepareStatement(strQuery.toString());
	    	        pstmt = new LoggableStatement(conn, strQuery.toString());
	    	        pstmt.setString(1, BaseItem);
	    	        ecamsLogger.error(((LoggableStatement)pstmt).getQueryString());
	    	        rs = pstmt.executeQuery();
	    	        
	    			if (rs.next()){
	    				rst = new HashMap<String, String>();
	    				rst.put("ID","ADD");
	    				rst.put("cr_rsrcname",rs.getString("cr_rsrcname"));
	    				rst.put("cr_story",rs.getString("cr_story"));
	    				rst.put("cm_jobname",rs.getString("cm_jobname"));
	    				rst.put("jawon_code",rs.getString("jawon"));
	    				rst.put("cm_dirpath",rs.getString("cm_dirpath"));
	    				rst.put("cr_lastdate",rs.getString("lastdt"));
	    				rst.put("cr_syscd",rs.getString("cr_syscd"));
	    				rst.put("cr_dsncd",rs.getString("cr_dsncd"));
	    				rst.put("cm_sysgb",rs.getString("cm_sysgb"));
	    				rst.put("cr_jobcd",rs.getString("cr_jobcd"));
	    				rst.put("cr_rsrccd",rs.getString("cr_rsrccd"));
	    				rst.put("cr_itemid",BaseItem);
	    				rst.put("cr_orderid",rs.getString("cr_orderid"));
	    				rst.put("cr_langcd",rs.getString("CR_LANGCD"));
	    				rst.put("cr_teamcd",rs.getString("cr_teamcd"));
	    				rst.put("cr_compile",rs.getString("cr_compile"));
	    				rst.put("cr_sqlcheck",rs.getString("cr_sqlcheck"));
	    				rst.put("cr_document",rs.getString("cr_document"));
	    				rst.put("docname",rs.getString("cm_codename3"));
	    				rst.put("langcd",rs.getString("cm_codename4"));
	    				rst.put("cr_compile",rs.getString("cm_compscript"));
	    				
	    				//rst.put("cr_isrsub",rs.getString("cr_isrsub"));
	    				rst.put("enabled", "1");
	    				rst.put("selected", "0");
	    				ecamsLogger.error(((LoggableStatement)pstmt).getQueryString());
	    				rsval.add(rst);
	    				
	    				rst = null;
	    			}//end of while-loop statement
	    			rs.close();
	    			pstmt.close();
	        	} else {
		        	rst = new HashMap<String, String>();
					rst.put("ID", retMsg);
					rsval.add(rst);
					rst = null;
	        	}
	        } else {
	        	rst = new HashMap<String, String>();
				rst.put("ID", retMsg);
				rsval.add(rst);
				rst = null;
	        }
			
			conn.close();

			rs = null;
			pstmt = null;
			conn = null;
			
			//ecamsLogger.error(rsval.toString());	
			returnObjectArray = rsval.toArray();
			rsval = null;
				
			return returnObjectArray;			
			
		} catch (SQLException sqlexception) {
			sqlexception.printStackTrace();
			ecamsLogger.error("## Cmd0100.pgmCheck() SQLException START ##");
			ecamsLogger.error("## Error DESC : ", sqlexception);	
			ecamsLogger.error("## Cmd0100.pgmCheck() SQLException END ##");			
			throw sqlexception;
		} catch (Exception exception) {
			exception.printStackTrace();
			ecamsLogger.error("## Cmd0100.pgmCheck() Exception START ##");				
			ecamsLogger.error("## Error DESC : ", exception);	
			ecamsLogger.error("## Cmd0100.pgmCheck() Exception END ##");				
			throw exception;
		}finally{
			if (returnObjectArray != null)	returnObjectArray = null;
			if (strQuery != null)	strQuery = null;			
			if (rs != null)     try{rs.close();}catch (Exception ex){ex.printStackTrace();}
			if (pstmt != null)  try{pstmt.close();}catch (Exception ex2){ex2.printStackTrace();}
			if (conn != null){
				try{
					ConnectionResource.release(conn);
				}catch(Exception ex3){
					ecamsLogger.error("## Cmd0100.pgmCheck() connection release exception ##");
					ex3.printStackTrace();
				}
			}
		}

	}//end of pgmCheck() method statement		
	public String moduleChk(String UserId,String SysCd,String OrderId,String DsnCd,String RsrcName,String RsrcCd,String JobCd,String ProgTit,String DirPath,String BaseItem,String CM_info, String RsRcGuBun,String ComPile,String Team,
							String SQLCD, String DOCCD, String ExeName) 
		throws SQLException, Exception {
		Connection        conn        = null;
		PreparedStatement pstmt       = null;
		PreparedStatement pstmt2       = null;
		ResultSet         rs          = null;
		ResultSet         rs2         = null;
		StringBuffer      strQuery    = new StringBuffer();
		String            retMsg      = "0";
		String            strItemId   = null;
		String            strBefDsn   = null;
		String            strAftDsn   = null;
		String            strWork1    = null;
		String            strWork2    = null;
		String            strWork3    = null;
		String            strDevPath  = null;
		String            strRsrcCd   = null;
		String            strRsrcName = null;
		boolean           insFg       = true;
		int               rsCnt       = 0;
		int               j           = 0;
		ArrayList<String>	qryAry = null;
		String				qryTmp = null;
		int					nRet1 = 0;
		int					nRet2 = 0;
		int					qryFlag = 0;
		
		ConnectionContext connectionContext = new ConnectionResource();		
		
		try {
			conn = connectionContext.getConnection();
						
			strQuery.append("select b.cm_samename,b.cm_samersrc,b.cm_basedir,         \n");
			strQuery.append("       b.cm_samedir,b.cm_basename,b.cm_cmdyn,a.cm_info   \n");
			strQuery.append("  from cmm0036 a,cmm0037 b                               \n");
			strQuery.append(" where b.cm_syscd=? and b.cm_rsrccd=?                    \n");
			strQuery.append("   and b.cm_factcd='04'                                  \n");
			strQuery.append("   and b.cm_syscd=a.cm_syscd                             \n");
			strQuery.append("   and b.cm_samersrc=a.cm_rsrccd                         \n");
						
	        pstmt = conn.prepareStatement(strQuery.toString());	
	        pstmt.setString(1, SysCd);   	
	        pstmt.setString(2, RsrcCd);        	     	
	        rs = pstmt.executeQuery();
	        
	        while (rs.next()) {
	        	strBefDsn = "";
	        	strAftDsn = "";
	        	
	        	if (rs.getString("cm_basedir") != null) strBefDsn = rs.getString("cm_basedir");
	        	if (RsrcName.indexOf(".") > -1) {	        		
	        		strWork1 = RsrcName.substring(0,RsrcName.indexOf("."));
	        	}
	        	else strWork1 = RsrcName;
	        	//ecamsLogger.debug("+++++++++++++++cm_basedir,strWork1=========>"+strBefDsn+ ","+strWork1);	
	        	if (!rs.getString("cm_basename").equals("*")) {
	        		strWork3 = rs.getString("cm_basename");
	        		while ("".equals(strWork3)) {
	        			j = strWork3.indexOf("*");
	        			if (j > -1) {
	        				strWork2 = strWork3.substring(0, j);
	        				strWork3 = strWork3.substring(j + 1);
	        				if (strWork3.equals("*")) strWork3 = "";
	        			} else {
	        				strWork2 = strWork3;
	        				strWork3 = "";
	        			}
	        			if ("".equals(strWork3)) break;
	        		}
	        	}
	        	strQuery.setLength(0);  
	        	strQuery.append("select \n");
	        	if (rs.getString("cm_cmdyn").equals("Y")) {
	        		strWork1 = rs.getString("cm_samename").replace("*",strWork1);
	        		qryTmp = "";
			   		qryAry = new ArrayList<String>();
			   		nRet1 = 0;
			   		nRet2 = 0;
			   		
			   		while( (nRet2 = strWork1.indexOf("'")) != -1){
			   			if (qryFlag == 0){
			   				strQuery.append(strWork1.substring(0, nRet2)+ " \n");
			   				strWork1 = strWork1.substring(nRet2+1);
			   				qryFlag = 1;
			   			}
			   			else{
			   				qryAry.add(strWork1.substring(0, nRet2));
			   				strWork1 = strWork1.substring(nRet2+1);
			   				strQuery.append(" ? \n");
			   				qryFlag = 0;
			   			}
			   		}
			   		strQuery.append(strWork1+ " \n");
	        	}
	        	else{
	        		strQuery.append(" ? \n");
	        	}
	        	strQuery.append("as relatId  from dual \n");
	        	
	        	//pstmt2 =  new LoggableStatement(conn, strQuery.toString());
	        	pstmt2 = conn.prepareStatement(strQuery.toString());
	        	
			   	nRet1 = 1;
				if (rs.getString("cm_cmdyn").equals("Y")){
	        		for (nRet2 = 0;nRet2<qryAry.size();nRet2++){
	        			pstmt2.setString(nRet1++,qryAry.get(nRet2));
	        		}
	        	}
	        	else{
			   		strWork1 = rs.getString("cm_samename").replace("*",strWork1);
			   		pstmt2.setString(nRet1++,strWork1);	        		
	        	} 	
	        	
				//ecamsLogger.debug(((LoggableStatement)pstmt2).getQueryString());
				
    	        rs2 = pstmt2.executeQuery();
    	        if (rs2.next()) {
    	        	strWork1 = rs2.getString("relatId");
    	        }
    	        else{
    	        	strWork1 = "";
    	        }
    	        rs2.close();
    	        pstmt2.close();
    	        
	        	strRsrcName = strWork1;
	        	strRsrcCd = rs.getString("cm_samersrc");
	        	if (rs.getString("cm_samedir") != null) strAftDsn = rs.getString("cm_samedir");
	        	
	        	if (rs.getString("cm_samersrc").equals("52")) {
	        		strQuery.setLength(0);                		
	        		strQuery.append("select a.cm_volpath  from cmm0038 a,cmm0031 b           \n");               		
	        		strQuery.append(" where a.cm_syscd=? and a.cm_svrcd='01'                 \n");               		
	        		strQuery.append("   and a.cm_rsrccd=? and a.cm_syscd=b.cm_syscd          \n");               		
	        		strQuery.append("   and a.cm_svrcd=b.cm_svrcd and a.cm_seqno=b.cm_seqno  \n");               		
	        		strQuery.append("   and b.cm_closedt is null                             \n"); 
	        		pstmt2 = conn.prepareStatement(strQuery.toString());	
	        		pstmt2.setString(1, SysCd); 
	        		pstmt2.setString(2, strRsrcCd); 
	    	        rs2 = pstmt2.executeQuery();
	    	        if (rs2.next()) {
	    	        	strDevPath = rs2.getString("cm_volpath");
	    	        }
	    	        
	    	        rs2.close();
	    	        pstmt2.close();
	        	} else strDevPath = DirPath.replace(strBefDsn, strAftDsn);
	        	//ecamsLogger.debug("+++++++++++++++strRsrcCd,strDevPath=========>"+strRsrcCd+ ","+strDevPath);	
	        	
	        	strQuery.setLength(0);                		
        		strQuery.append("select cm_dsncd from cmm0070            \n");               		
        		strQuery.append(" where cm_syscd=? and cm_dirpath=?      \n"); 
        		pstmt2 = conn.prepareStatement(strQuery.toString());	
        		pstmt2.setString(1, SysCd); 
        		pstmt2.setString(2, strDevPath); 
    	        rs2 = pstmt2.executeQuery();
    	        if (rs2.next()) {
    	        	strAftDsn = rs2.getString("cm_dsncd");
    	        } else {
    	        	strAftDsn = cmm0070_Insert(UserId,SysCd,strRsrcName,strRsrcCd,JobCd,strDevPath,false,conn);
    	        }    	        
    	        rs2.close();
    	        pstmt2.close();
    	        
    	        if (retMsg == "0") {
    	        	//ecamsLogger.debug("+++++++++++++++rsrcname,BaseItem=========>"+strRsrcName+","+BaseItem);	
    	        	retMsg = cmr0020_Insert(UserId,SysCd,OrderId,strAftDsn,strRsrcName,strRsrcCd,JobCd,ProgTit,BaseItem,CM_info,conn,RsRcGuBun,ComPile,"",Team, SQLCD, DOCCD, ExeName, "", "", "") ;  //cmr0020 cr_Master 관리담당자
    	        		   //cmr0020_Insert(UserId,SysCd,DsnCd,RsrcName,RsrcCd,JobCd,ProgTit,"",CM_info,conn);
    	        }
	        }
	        rs.close();
	        pstmt.close();
	        conn.close();
	        

			rs = null;
			pstmt = null;
			rs2 = null;
			pstmt2 = null;
			conn = null;
			
	        return retMsg;
	        
	        
		} catch (SQLException sqlexception) {
			sqlexception.printStackTrace();
			ecamsLogger.error("## Cmd0100.moduleChk() SQLException START ##");
			ecamsLogger.error("## Error DESC : ", sqlexception);	
			ecamsLogger.error("## Cmd0100.moduleChk() SQLException END ##");			
			throw sqlexception;
		} catch (Exception exception) {
			exception.printStackTrace();
			ecamsLogger.error("## Cmd0100.moduleChk() Exception START ##");				
			ecamsLogger.error("## Error DESC : ", exception);	
			ecamsLogger.error("## Cmd0100.moduleChk() Exception END ##");				
			throw exception;
		}finally{
			if (strQuery !=  null)	strQuery = null;
			if (rs != null)     try{rs.close();}catch (Exception ex){ex.printStackTrace();}
			if (pstmt != null)  try{pstmt.close();}catch (Exception ex2){ex2.printStackTrace();}
			if (rs2 != null)     try{rs2.close();}catch (Exception ex){ex.printStackTrace();}
			if (pstmt2 != null)  try{pstmt2.close();}catch (Exception ex2){ex2.printStackTrace();}
			if (conn != null){
				try{
					ConnectionResource.release(conn);
				}catch(Exception ex3){
					ecamsLogger.error("## Cmd0100.moduleChk() connection release exception ##");
					ex3.printStackTrace();
				}
			}
		}		

	}//end of moduleChk() method statement	
	
	
	//cmr0020_Insert(etcData.get("userid"),etcData.get("syscd"),rtList.get(i).get("cm_dsncd"),rtList.get(i).get("filename"),etcData.get("rsrccd"),etcData.get("jobcd"),rtList.get(i).get("story"),etcData.get("pgmtype"),"",conn);
	public String cmr0020_Insert(String UserId,String SysCd,String OrderId,String DsnCd,String RsrcName,String RsrcCd,String JobCd,String ProgTit,String BaseItem,String rsrcInfo,Connection conn,
			                     String RsRcGuBun, String ComPile, String MakeComPile, String Team, String SQLCD , String DOCCD, String ExeName, String MasterID, String EtcDsn, String EtcDsnHome) throws SQLException, Exception {
		PreparedStatement pstmt       = null;
		ResultSet         rs          = null;
		StringBuffer      strQuery    = new StringBuffer();
		String            retMsg      = "0";
		String            strItemId   = "";
		String            oldIsrId    = "";
		String            oldIsrSub   = "";
		boolean           insFg       = true;
		boolean           errSw       = false;
		int 		      parmCnt     = 0;
		String            strGrpCd    = "";
		boolean           findSw      = false;
		
		try {
			
			if (rsrcInfo.substring(26,27).equals("1") && !RsrcCd.equals("34")) {
				Connection        connPfm        = null;				
				ConnectionContext connectionContextPfm = new ConnectionResource(false,"MSM001");			
				connPfm = connectionContextPfm.getConnection();
				/*
				 * SELECT RESOURCE_GROUP
    FROM DEV_RESOURCE
    WHERE PHYSICAL_NAME = '$PHYSICAL_NAME' --물리명 입력
    ;
				 */
				strQuery.setLength(0);
				strQuery.append("SELECT RESOURCE_GROUP FROM DEV_RESOURCE \n");
				strQuery.append(" WHERE PHYSICAL_NAME=?                  \n");
				pstmt = connPfm.prepareStatement(strQuery.toString());
				pstmt.setString(1, RsrcName);
				rs = pstmt.executeQuery();
				if (rs.next()) {
					strGrpCd = rs.getString("RESOURCE_GROUP");
				} else {
					retMsg = "프로프레임 그룹코드 추출에 실패하였습니다.";
				}
				rs.close();
				pstmt.close();
				connPfm.close();
				
				connPfm = null;
				if (!retMsg.equals("0")) {
					return retMsg;
				}
				
			}
					
			//conn.setAutoCommit(false);			
			strQuery.setLength(0);
			strQuery.append("select cr_itemid,cr_orderid \n");
			strQuery.append("  from cmr0020              \n");
			strQuery.append(" where cr_syscd=?           \n");
		    strQuery.append("   and cr_dsncd=?           \n");
			strQuery.append("   and cr_rsrcname=?        \n");

	        pstmt = conn.prepareStatement(strQuery.toString());	
	        pstmt.setString(++parmCnt, SysCd);   	
	        pstmt.setString(++parmCnt, DsnCd);
	        if(!"".equals(ExeName) && ExeName != null){
      	    	pstmt.setString(++parmCnt, RsrcName + "." + ExeName);
      	    }else{
      	    	pstmt.setString(++parmCnt, RsrcName);
      	    }        	     	
	        rs = pstmt.executeQuery();
	        
	        if (rs.next()) {
	        	insFg = false;
	        	strItemId = rs.getString("cr_itemid");
	        	if (rs.getString("cr_orderid") != null && !"".equals(rs.getString("cr_orderid"))){
		        	oldIsrId = rs.getString("cr_orderid");
	        	}
	        }
	        rs.close();
	        pstmt.close();
	        
	        parmCnt = 0;
	        strQuery.setLength(0);
	        if (insFg) {
        		strQuery.append("insert into cmr0020 (CR_ITEMID,CR_SYSCD,CR_DSNCD,CR_RSRCNAME,                                       \n");
        		strQuery.append("   CR_RSRCCD,CR_JOBCD,CR_STATUS,CR_CREATOR,CR_STORY,                                                \n");
        		strQuery.append("   CR_OPENDATE,CR_LASTDATE,CR_LSTVER,CR_EDITOR,CR_NOMODIFY,CR_ORDERID,CR_TMAXGRP,                   \n");
        		strQuery.append("   CR_LANGCD,CR_COMPILE,CR_TEAMCD ,CR_SQLCHECK,CR_DOCUMENT, CR_EXENAME,CR_EXTNAME,CR_MASTER,CR_DSNCD2,CR_MAKECOMPILE,CR_DSNCD2HOME)                \n");
        		strQuery.append("values                                                                                              \n");
        		strQuery.append("  (lpad(ITEMID_SEQ.nextval,12,'0'),?,?,?,  ?,?,'3',?,?,  SYSDATE,SYSDATE,0,?,'0',?,?,?,?,?,?,?,?,?,?,?,?,?) \n");
        		
        		pstmt = conn.prepareStatement(strQuery.toString());
        		pstmt = new LoggableStatement(conn,strQuery.toString());
        		
          	    pstmt.setString(++parmCnt, SysCd);
          	    pstmt.setString(++parmCnt, DsnCd);
          	    if(!"".equals(ExeName) && ExeName != null){
        	    	pstmt.setString(++parmCnt, RsrcName + "." + ExeName);
        	    }else{
        	    	pstmt.setString(++parmCnt, RsrcName);
        	    }  
          	    pstmt.setString(++parmCnt, RsrcCd);
          	    pstmt.setString(++parmCnt, JobCd);
          	    pstmt.setString(++parmCnt, UserId);
          	    pstmt.setString(++parmCnt, ProgTit);
          	    
          	    pstmt.setString(++parmCnt, UserId);
          	    pstmt.setString(++parmCnt, OrderId);
          	    pstmt.setString(++parmCnt, strGrpCd);
          	   
          	    pstmt.setString(++parmCnt, RsRcGuBun);
        	    pstmt.setString(++parmCnt, ComPile);
        	    pstmt.setString(++parmCnt, Team);
        	    
        	    pstmt.setString(++parmCnt, SQLCD);
        	    pstmt.setString(++parmCnt, DOCCD);
        	    /// 추가
        	    pstmt.setString(++parmCnt, RsrcName);
        	    pstmt.setString(++parmCnt, ExeName);
        	    pstmt.setString(++parmCnt, MasterID);
        	    pstmt.setString(++parmCnt, EtcDsn);
        	    pstmt.setString(++parmCnt, MakeComPile);
        	    pstmt.setString(++parmCnt, EtcDsnHome);
        	    
        	    ecamsLogger.error(((LoggableStatement)pstmt).getQueryString());
          	    
          		pstmt.executeUpdate();
    	        pstmt.close();
    	        
	        } else {
	        	
        		strQuery.append("update cmr0020 set CR_RSRCCD=?,CR_JOBCD=?,                       \n");
        		strQuery.append("                   CR_STATUS='3',CR_TMAXGRP=?,                   \n");
        		strQuery.append("                   CR_CREATOR=?,CR_STORY=?,                      \n");
        		strQuery.append("                   CR_OPENDATE=SYSDATE,                          \n");
        		strQuery.append("                   CR_LASTDATE=SYSDATE,CR_LSTVER=0,              \n");
        		strQuery.append("                   CR_EDITOR=?,CR_NOMODIFY=0,                    \n");
        		strQuery.append("                   CR_ORDERID=?,                                 \n");
        		strQuery.append("                   CR_LANGCD =?,CR_COMPILE =?,CR_TEAMCD =? ,     \n");
        		strQuery.append("                   CR_SQLCHECK = ?, CR_DOCUMENT = ?, CR_MASTER=?, \n");
        		strQuery.append("                   CR_DSNCD2 = ?, CR_MAKECOMPILE = ?, CR_DSNCD2HOME=? \n");
        		strQuery.append("where cr_itemid=?                                                \n");
        		
        		pstmt = conn.prepareStatement(strQuery.toString());
        		//pstmt = new LoggableStatement(conn,strQuery.toString());
        		parmCnt = 0;
          	    pstmt.setString(++parmCnt, RsrcCd);
          	    pstmt.setString(++parmCnt, JobCd);
          	    pstmt.setString(++parmCnt, strGrpCd);
          	    pstmt.setString(++parmCnt, UserId);
          	    pstmt.setString(++parmCnt, ProgTit);
          	    pstmt.setString(++parmCnt, UserId);
          	    pstmt.setString(++parmCnt, OrderId);
          	    pstmt.setString(++parmCnt, RsRcGuBun);
      	        pstmt.setString(++parmCnt, ComPile);
      	        pstmt.setString(++parmCnt, Team);
      	        pstmt.setString(++parmCnt, SQLCD);
    	        pstmt.setString(++parmCnt, DOCCD);
    	        pstmt.setString(++parmCnt, MasterID);
    	        pstmt.setString(++parmCnt, EtcDsn);
        	    pstmt.setString(++parmCnt, MakeComPile);
        	    pstmt.setString(++parmCnt, EtcDsnHome);
      	        pstmt.setString(++parmCnt, strItemId); 
     
          	    //ecamsLogger.error(((LoggableStatement)pstmt).getQueryString());
          	    pstmt.executeUpdate();
    	        pstmt.close();
          	    
    	        findSw = false;
	        	if (!"".equals(oldIsrId) && oldIsrId != null){
	        		findSw = true;
	        		if (OrderId != null && !"".equals(OrderId)) {
	        			if (oldIsrId.equals(OrderId)) {
	        				findSw = false;
	        			}
	        		}
	        	}
	        	/*	
	        	if (findSw) {
	        		strQuery.setLength(0);
	        		strQuery.append("UPDATE CMC0110 \n");
	        		strQuery.append("   SET CC_SUBSTATUS=DECODE(TESTENDYN('',?,?),'OK','25',CC_SUBSTATUS) \n");
	        		strQuery.append(" WHERE CC_ISRID=? \n");
	        		strQuery.append("   AND CC_ISRSUB=? \n");
	        		pstmt = conn.prepareStatement(strQuery.toString());
	        		// pstmt = new LoggableStatement(conn,strQuery.toString());
	        		parmCnt = 0;
		          	pstmt.setString(++parmCnt, oldIsrId);
	          	    pstmt.setString(++parmCnt, oldIsrSub);
		          	pstmt.setString(++parmCnt, oldIsrId);
	          	    pstmt.setString(++parmCnt, oldIsrSub);
	         	    //ecamsLogger.error(((LoggableStatement)pstmt).getQueryString());
	          	    pstmt.executeUpdate();
	    	        pstmt.close();
	        	}
	        */
	        }
	        
	        if (insFg) {
	        	strQuery.setLength(0);
				strQuery.append("select cr_itemid from cmr0020                  \n");
				strQuery.append(" where cr_syscd=?                              \n");
			    strQuery.append("   and cr_dsncd=?                              \n");
				strQuery.append("   and cr_rsrcname=?                           \n");
				
				parmCnt= 0;
		        pstmt = conn.prepareStatement(strQuery.toString());
		        pstmt.setString(++parmCnt, SysCd);   	
		        pstmt.setString(++parmCnt, DsnCd);  
		        if(!"".equals(ExeName) && ExeName != null){
        	    	pstmt.setString(++parmCnt, RsrcName + "." + ExeName);
        	    }else{
        	    	pstmt.setString(++parmCnt, RsrcName);
        	    }
		        rs = pstmt.executeQuery();
		        
		        if (rs.next()) {
		        	strItemId = rs.getString("cr_itemid");
		        }
		        rs.close();
		        pstmt.close();
	        }
      		retMsg = "0" + strItemId;
      		
      		rs = null;
      		pstmt = null;
	        return retMsg;
	        
		} catch (SQLException sqlexception) {
			sqlexception.printStackTrace();
			ecamsLogger.error("## Cmd0100.cmr0020_Insert() SQLException START ##");
			ecamsLogger.error("## Error DESC : ", sqlexception);	
			ecamsLogger.error("## Cmd0100.cmr0020_Insert() SQLException END ##");			
			throw sqlexception;
		} catch (Exception exception) {
			exception.printStackTrace();
			ecamsLogger.error("## Cmd0100.cmr0020_Insert() Exception START ##");				
			ecamsLogger.error("## Error DESC : ", exception);	
			ecamsLogger.error("## Cmd0100.cmr0020_Insert() Exception END ##");				
			throw exception;
		}finally{
			if (strQuery != null)	strQuery = null;
			if (rs != null)     try{rs.close();}catch (Exception ex){ex.printStackTrace();}
			if (pstmt != null)  try{pstmt.close();}catch (Exception ex2){ex2.printStackTrace();}
		}		
	}//end of cmr0020_Insert() method statement
	
	
	public String cmr0020_Delete(String UserId,ArrayList<HashMap<String,String>> dataList) throws SQLException, Exception {
		Connection        conn        = null;
		PreparedStatement pstmt       = null;
		PreparedStatement pstmt2      = null;
		ResultSet         rs          = null;
		ResultSet         rs2         = null;
		StringBuffer      strQuery    = new StringBuffer();
		boolean           findSw      = false;
		
		ConnectionContext connectionContext = new ConnectionResource();		
		
		try {
			
			conn = connectionContext.getConnection();
			conn.setAutoCommit(false);	
			
			String ItemId = "";
			for (int i=0 ; i<dataList.size() ; i++){
				ItemId = dataList.get(i).get("cr_itemid");
				findSw = false;
				strQuery.setLength(0);
	        	strQuery.append("select cr_status from cmr0020      \n");
	        	strQuery.append(" where cr_itemid=?                 \n");
	        	strQuery.append("   and cr_status='3'               \n");
	        	pstmt = conn.prepareStatement(strQuery.toString());
	        	pstmt.setString(1, ItemId);
	        	rs = pstmt.executeQuery();
	        	if (rs.next()) {
	        		if (!rs.getString("cr_status").equals("3")) findSw = true;
	        	}
	        	rs.close();
	        	pstmt.close();
	        	
	        	if (!findSw) {
					strQuery.setLength(0);
					strQuery.append("select a.cr_itemid                     \n");                       
					strQuery.append("  from cmr1010 a,cmr0020 b             \n");
					strQuery.append(" where a.cr_baseitem=?                 \n");
					strQuery.append("   and a.cr_itemid<>?                  \n");
					strQuery.append("   and a.cr_itemid=b.cr_itemid         \n");
					strQuery.append("   and b.cr_lstver=0                   \n");
					strQuery.append("  group by a.cr_itemid                 \n");
					strQuery.append(" minus                                 \n");
					strQuery.append("select a.cr_itemid                     \n");
					strQuery.append("  from cmr1010 a,cmr1010 b             \n");
					strQuery.append(" where a.cr_baseitem=?                 \n");
					strQuery.append("   and a.cr_itemid=b.cr_itemid         \n");
					strQuery.append("   and b.cr_baseitem<>a.cr_baseitem    \n");
					strQuery.append("  group by a.cr_itemid                 \n");
			        pstmt = conn.prepareStatement(strQuery.toString());
			        //pstmt = new LoggableStatement(conn,strQuery.toString());
			        pstmt.setString(1, ItemId);
			        pstmt.setString(2, ItemId);
			        pstmt.setString(3, ItemId);
			        //ecamsLogger.error(((LoggableStatement)pstmt).getQueryString());
			        rs = pstmt.executeQuery();
			        
			        while(rs.next()) {
			        	strQuery.setLength(0);
			        	strQuery.append("delete cmr0020 where cr_itemid=?  ");
			        	pstmt2 = conn.prepareStatement(strQuery.toString());
			        	//pstmt2 = new LoggableStatement(conn,strQuery.toString());
			        	pstmt2.setString(1, rs.getString("cr_itemid"));
			        	//ecamsLogger.error(((LoggableStatement)pstmt2).getQueryString());  
			      		pstmt2.executeUpdate();
			      		pstmt2.close();
			      		
			        	strQuery.setLength(0);
			        	strQuery.append("delete cmr1010 where cr_itemid=?  ");
			        	pstmt2 = conn.prepareStatement(strQuery.toString());
			        	//pstmt2 = new LoggableStatement(conn,strQuery.toString());
			        	pstmt2.setString(1, rs.getString("cr_itemid"));
			        	//ecamsLogger.error(((LoggableStatement)pstmt2).getQueryString());  
			      		pstmt2.executeUpdate();
			      		pstmt2.close();
			        }
			        rs.close();
			        pstmt.close(); 
			        conn.commit();
			        
			        if (!cmr0020_Delete_sub(UserId,ItemId,conn).equals("0")){
    					conn.rollback();
    					conn.close();
    					throw new Exception("cmr0020_Delete_sub 중 오류가 발생하였습니다. 관리자에게 연락하여 주시기 바랍니다.");
			        }
	        	}		  		
			}
	  		
	  		conn.commit();
	  		conn.close();	  		
			
	  		rs = null;
	  		pstmt = null;
	  		pstmt2 = null;
	  		conn = null;
	  		
			return "0";		
			
			
		} catch (SQLException sqlexception) {
			sqlexception.printStackTrace();
			if (rs != null)     try{rs.close();}catch (Exception ex){ex.printStackTrace();}
			if (pstmt != null)  try{pstmt.close();}catch (Exception ex2){ex2.printStackTrace();}
			if (rs2 != null)     try{rs2.close();}catch (Exception ex){ex.printStackTrace();}
			if (pstmt2 != null)  try{pstmt2.close();}catch (Exception ex2){ex2.printStackTrace();}
			if (conn != null){
				try{
					conn.rollback();
					ConnectionResource.release(conn);
					conn = null;
				}catch(Exception ex3){
					ecamsLogger.error("## Cmr0200.request_Check_In() connection release exception ##");
					ex3.printStackTrace();
				}				
			}
			ecamsLogger.error("## Cmd0100.cmr0020_Delete() SQLException START ##");
			ecamsLogger.error("## Error DESC : ", sqlexception);	
			ecamsLogger.error("## Cmd0100.cmr0020_Delete() SQLException END ##");			
			throw sqlexception;
		} catch (Exception exception) {
			exception.printStackTrace();
			if (rs != null)     try{rs.close();}catch (Exception ex){ex.printStackTrace();}
			if (pstmt != null)  try{pstmt.close();}catch (Exception ex2){ex2.printStackTrace();}
			if (rs2 != null)     try{rs2.close();}catch (Exception ex){ex.printStackTrace();}
			if (pstmt2 != null)  try{pstmt2.close();}catch (Exception ex2){ex2.printStackTrace();}
			if (conn != null){
				try{
					conn.rollback();
					ConnectionResource.release(conn);
					conn = null;
				}catch(Exception ex3){
					ecamsLogger.error("## Cmr0200.request_Check_In() connection release exception ##");
					ex3.printStackTrace();
				}				
			}
			ecamsLogger.error("## Cmd0100.cmr0020_Delete() Exception START ##");				
			ecamsLogger.error("## Error DESC : ", exception);	
			ecamsLogger.error("## Cmd0100.cmr0020_Delete() Exception END ##");				
			throw exception;
		}finally{
			if (strQuery != null)	strQuery = null;
			if (rs != null)     try{rs.close();}catch (Exception ex){ex.printStackTrace();}
			if (pstmt != null)  try{pstmt.close();}catch (Exception ex2){ex2.printStackTrace();}
			if (rs2 != null)     try{rs2.close();}catch (Exception ex){ex.printStackTrace();}
			if (pstmt2 != null)  try{pstmt2.close();}catch (Exception ex2){ex2.printStackTrace();}
			if (conn != null){
				try{
					ConnectionResource.release(conn);
				}catch(Exception ex3){
					ecamsLogger.error("## Cmd0100.cmr0020_Delete() connection release exception ##");
					ex3.printStackTrace();
				}
			}
		}
	}//end of cmr0020_Delete() method statement

	/**
	 * <PRE>
	 * 1. MethodName	: cmr0020_Delete_sub
	 * 2. ClassName		: Cmd0100
	 * 3. Commnet			: cmr0022,cmr1010,cmr0020 테이블에서 관련 프로그램삭제
	 * 4. 작성자				: Administrator
	 * 5. 작성일				: 2010. 12. 8. 오전 11:08:08
	 * </PRE>
	 * 		@return String
	 * 		@param UserId
	 * 		@param ItemId : 프로그램ID
	 * 		@param conn : Connection
	 * 		@return
	 * 		@throws SQLException
	 * 		@throws Exception
	 */
	public String cmr0020_Delete_sub(String UserId,String ItemId,Connection conn) throws SQLException, Exception {
		PreparedStatement pstmt       = null;
		ResultSet         rs          = null;
		StringBuffer      strQuery    = new StringBuffer();
		boolean            findSw      = false;
		String            strIsrId    = "";
		String            strIsrSub   = "";
		
		try {
			strQuery.setLength(0);
			strQuery.append("select cr_orderid,cr_status from cmr0020   \n");
        	strQuery.append(" where cr_itemid=?                         \n");
        	strQuery.append("   and cr_status='3'                       \n");
        	pstmt = conn.prepareStatement(strQuery.toString());
        	pstmt.setString(1, ItemId);
        	rs = pstmt.executeQuery();
        	if (rs.next()) {
        		if (!rs.getString("cr_status").equals("3")) findSw = true;
        		strIsrId = rs.getString("cr_orderid");
        		//strIsrSub = rs.getString("cr_isrsub");
        	}
        	rs.close();
        	pstmt.close();
        	
        	pstmt = conn.prepareStatement(strQuery.toString());
        	pstmt.setString(1, ItemId);
        	rs = pstmt.executeQuery();
        	if (rs.next()) {
        		if (!rs.getString("cr_status").equals("3")) findSw = true;
        	}
        	rs.close();
        	pstmt.close();
	        	
        	if (!findSw) {
		        strQuery.setLength(0);
		        strQuery.append("delete cmr0022 where cr_baseitem=?  ");
				pstmt = conn.prepareStatement(strQuery.toString());	
		  	    pstmt.setString(1, ItemId); 
		  		pstmt.executeUpdate();
		  		pstmt.close();
		  		
		        strQuery.setLength(0);
		        strQuery.append("delete cmr1010 where cr_itemid=?  ");
				pstmt = conn.prepareStatement(strQuery.toString());	
		  	    pstmt.setString(1, ItemId); 
		  		pstmt.executeUpdate();
		  		pstmt.close();		
		  		
		        strQuery.setLength(0);
		        strQuery.append("delete cmr0020 where cr_itemid=?  ");
				pstmt = conn.prepareStatement(strQuery.toString());	
		  	    pstmt.setString(1, ItemId); 
		  		pstmt.executeUpdate();
		  		pstmt.close();
		  		
		  		/*
		  		if (strIsrId != null || strIsrId != "") {
		        	strQuery.setLength(0);
	        		strQuery.append("UPDATE CMC0110 \n");
	        		strQuery.append("   SET CC_SUBSTATUS=DECODE(TESTENDYN('',?,?),'OK','25',CC_SUBSTATUS) \n");
	        		strQuery.append(" WHERE CC_ISRID=? \n");
	        	//	strQuery.append("   AND CC_ISRSUB=? \n");
	        		strQuery.append("   AND PROGDEVYN_TESTNO(?,?)='OK' \n"); //20110622 광진수정
	        		pstmt = conn.prepareStatement(strQuery.toString());
	        		//pstmt= new LoggableStatement(conn,strQuery.toString());
		          	pstmt.setString(1, strIsrId);
	          	    pstmt.setString(2, strIsrSub);
		          	pstmt.setString(3, strIsrId);
	          	    pstmt.setString(4, strIsrSub);
		          	pstmt.setString(5, strIsrId);//20110622 광진수정
	          	    pstmt.setString(6, strIsrSub);//20110622 광진수정
	         	    //ecamsLogger.error(((LoggableStatement)pstmt).getQueryString());
	          	    pstmt.executeUpdate();
	    	        pstmt.close();
		        }
        	*/
        	}	  		
			
        	rs = null;
        	pstmt = null;
			return "0";		
			
			
			
		} catch (SQLException sqlexception) {
			sqlexception.printStackTrace();
			ecamsLogger.error("## Cmd0100.cmr0020_Delete_sub() SQLException START ##");
			ecamsLogger.error("## Error DESC : ", sqlexception);	
			ecamsLogger.error("## Cmd0100.cmr0020_Delete_sub() SQLException END ##");			
			throw sqlexception;
		} catch (Exception exception) {
			exception.printStackTrace();
			ecamsLogger.error("## Cmd0100.cmr0020_Delete_sub() Exception START ##");				
			ecamsLogger.error("## Error DESC : ", exception);	
			ecamsLogger.error("## Cmd0100.cmr0020_Delete_sub() Exception END ##");				
			throw exception;
		}finally{
			if (strQuery != null)	strQuery = null;
			if (rs != null)     try{rs.close();}catch (Exception ex){ex.printStackTrace();}
			if (pstmt != null)  try{pstmt.close();}catch (Exception ex2){ex2.printStackTrace();}
			
		}
	}//end of cmr0020_Delete_sub() method statement
	public Object[] getRsrcOpen2(String SelMsg) throws SQLException, Exception {
		Connection        conn        = null;
		PreparedStatement pstmt       = null;
		ResultSet         rs          = null;
		StringBuffer      strQuery    = new StringBuffer();
	
		ArrayList<HashMap<String, String>>  rsval = new ArrayList<HashMap<String, String>>();
		HashMap<String, String>			  rst		  = null;	
		
		Object[] returnObjectArray = null;
		String            strSelMsg   = "";
	    		
		ConnectionContext connectionContext = new ConnectionResource();		
		
		try {
			conn = connectionContext.getConnection();
			
			if (!"".equals(SelMsg)) {
				if (SelMsg.equals("ALL")) strSelMsg = "전체";
				else if (SelMsg.equals("SEL")) strSelMsg = "선택하세요";
			}
			strQuery.setLength(0);
			strQuery.append("select a.cm_macode, a.cm_micode ,a.cm_codename, nvl(b.cm_exename,'z') as exename,cm_exeno   \n");
			strQuery.append("  from cmm0020 a, cmm0023 b                     				\n");
			strQuery.append("  where  a.cm_closedt is null                   				\n");	
			strQuery.append("   and a.cm_macode='LANGUAGE'                   				\n");
			strQuery.append("   and a.cm_micode != '****'                    				\n");
			strQuery.append(" and    a.cm_micode = b.cm_langcd(+)              				\n");
	        pstmt = conn.prepareStatement(strQuery.toString());	
	   
	                   
	        rs = pstmt.executeQuery();
	        
	        rsval.clear();
	        
			while (rs.next()){
				if (rs.getRow() == 1 && !"".equals(strSelMsg)) {
				   rst = new HashMap<String, String>();
				   rst.put("ID", "0");
				   rst.put("cm_micode", "0000");
				   rst.put("cm_codename", strSelMsg);
				   rsval.add(rst);
				   rst = null;
				}
				rst = new HashMap<String, String>();
				rst.put("ID", Integer.toString(rs.getRow()));
				rst.put("cm_micode", rs.getString("cm_micode"));
				rst.put("cm_codename", rs.getString("cm_codename"));
				rst.put("cm_exename", rs.getString("exename"));
				rst.put("cm_exeno", rs.getString("cm_exeno"));
				rsval.add(rst);
				rst = null;
			}//end of while-loop statement
			rs.close();
			pstmt.close();
			conn.close();
			
			rs = null;
			pstmt = null;
			conn = null;
			
			returnObjectArray = rsval.toArray();
			rsval = null;
			//ecamsLogger.debug(rsval.toString());		
			return returnObjectArray;			
			
		} catch (SQLException sqlexception) {
			sqlexception.printStackTrace();
			ecamsLogger.error("## Cmd0100.getRsrcOpen2() SQLException START ##");
			ecamsLogger.error("## Error DESC : ", sqlexception);	
			ecamsLogger.error("## Cmd0100.getRsrcOpen2() SQLException END ##");			
			throw sqlexception;
		} catch (Exception exception) {
			exception.printStackTrace();
			ecamsLogger.error("## Cmd0100.getRsrcOpen2() Exception START ##");				
			ecamsLogger.error("## Error DESC : ", exception);	
			ecamsLogger.error("## Cmd0100.getRsrcOpen2() Exception END ##");				
			throw exception;
		}finally{
			if (returnObjectArray != null)	returnObjectArray = null;
			if (strQuery != null)	strQuery = null;
			if (rs != null)     try{rs.close();}catch (Exception ex){ex.printStackTrace();}
			if (pstmt != null)  try{pstmt.close();}catch (Exception ex2){ex2.printStackTrace();}
			if (conn != null){
				try{
					ConnectionResource.release(conn);
				}catch(Exception ex3){
					ecamsLogger.error("## Cmd0100.getRsrcOpen2() connection release exception ##");
					ex3.printStackTrace();
				}
			}
		}		

	}
	public Object[] getCompile(String Syscd, String SelMsg) throws SQLException, Exception {
		Connection        conn        = null;
		PreparedStatement pstmt       = null;
		ResultSet         rs          = null;
		StringBuffer      strQuery    = new StringBuffer();
	
		ArrayList<HashMap<String, String>>  rsval = new ArrayList<HashMap<String, String>>();
		HashMap<String, String>			  rst		  = null;	
		
		Object[] returnObjectArray = null;
		String            strSelMsg   = "";
		
		ConnectionContext connectionContext = new ConnectionResource();
		
		try {
			conn = connectionContext.getConnection();
			if (!("").equals(SelMsg)) {
				if (SelMsg.equals("ALL")) strSelMsg = "전체";
				else if (SelMsg.equals("SEL")) strSelMsg = "선택하세요";
			}
			strQuery.setLength(0);
			strQuery.append("select cm_syscd, cm_compcd, cm_compscript,cm_gubun			   	\n");
			strQuery.append("from cmm0400          				           					\n");
			strQuery.append("where  cm_syscd = ?                   							\n");	
			strQuery.append("order by cm_compcd               				\n");
	        pstmt = conn.prepareStatement(strQuery.toString());	
	        int parmCnt = 0;
      	    pstmt.setString(++parmCnt, Syscd);
	        rs = pstmt.executeQuery();
	        
	        rsval.clear();
	        
			while (rs.next()){
				if (rs.getRow() == 1 && !"".equals(strSelMsg)) {
				   rst = new HashMap<String, String>();
				   rst.put("ID", "0");
				   rst.put("cm_syscd", "00000");
				   rst.put("cm_compcd", "0000");
				   rst.put("cm_gubun", "00");
				   rst.put("cm_compscript", strSelMsg);
				   rsval.add(rst);
				   rst = null;
				}
				rst = new HashMap<String, String>();
				rst.put("ID", Integer.toString(rs.getRow()));
				rst.put("cm_syscd", rs.getString("cm_syscd"));
				rst.put("cm_compcd", rs.getString("cm_compcd"));
				rst.put("cm_gubun", rs.getString("cm_gubun"));
				rst.put("cm_compscript", rs.getString("cm_compscript"));
				rsval.add(rst);
				rst = null;
			}//end of while-loop statement
			rs.close();
			pstmt.close();
			conn.close();
			
			rs = null;
			pstmt = null;
			conn = null;
			
			returnObjectArray = rsval.toArray();
			rsval = null;
			//ecamsLogger.debug(rsval.toString());		
			return returnObjectArray;			

		} catch (SQLException sqlexception) {
			sqlexception.printStackTrace();
			ecamsLogger.error("## Cmd0100.getRsrcOpen2() SQLException START ##");
			ecamsLogger.error("## Error DESC : ", sqlexception);	
			ecamsLogger.error("## Cmd0100.getRsrcOpen2() SQLException END ##");			
			throw sqlexception;
		} catch (Exception exception) {
			exception.printStackTrace();
			ecamsLogger.error("## Cmd0100.getRsrcOpen2() Exception START ##");				
			ecamsLogger.error("## Error DESC : ", exception);	
			ecamsLogger.error("## Cmd0100.getRsrcOpen2() Exception END ##");				
			throw exception;
		}finally{
			if (returnObjectArray != null)	returnObjectArray = null;
			if (strQuery != null)	strQuery = null;
			if (rs != null)     try{rs.close();}catch (Exception ex){ex.printStackTrace();}
			if (pstmt != null)  try{pstmt.close();}catch (Exception ex2){ex2.printStackTrace();}
			if (conn != null){
				try{
					ConnectionResource.release(conn);
				}catch(Exception ex3){
					ecamsLogger.error("## Cmd0100.getRsrcOpen2() connection release exception ##");
					ex3.printStackTrace();
				}
			}
		}

		
	}//end of getCodeInfo() method statement
	public Object[] getPrjList_Chg(String SelMsg, String User) throws SQLException, Exception {
		Connection        conn        = null;
		PreparedStatement pstmt       = null;
		ResultSet         rs          = null;
		StringBuffer      strQuery    = new StringBuffer();
	
		ArrayList<HashMap<String, String>>  rsval = new ArrayList<HashMap<String, String>>();
		HashMap<String, String>			  rst		  = null;	
		
		Object[] returnObjectArray = null;
		String            strSelMsg   = "";
	    		
		ConnectionContext connectionContext = new ConnectionResource();		
		
		try {
			conn = connectionContext.getConnection();
			
			if (!"".equals(SelMsg)) {
				if (SelMsg.equals("ALL")) strSelMsg = "전체";
				else if (SelMsg.equals("SEL")) strSelMsg = "선택하세요";
			}
			strQuery.setLength(0);
			
			strQuery.append(" select a.cc_orderid , a.cc_subid , a.cc_reqsub ,             \n");
			strQuery.append(" b.cm_username requser , c.cm_deptname reqdept, a.CC_REQSUB   \n");
			strQuery.append(" from   cmc0420 a ,cmm0040 b , cmm0100 c                      \n");
			strQuery.append(" where a.cc_orderuser = b.cm_userid                           \n");
			strQuery.append(" and a.cc_orderuser = ?                                       \n");
			strQuery.append(" and a.cc_team=c.cm_deptcd                                    \n");
				
	        pstmt = conn.prepareStatement(strQuery.toString());	
	   
	        pstmt.setString(1, User);           
	        rs = pstmt.executeQuery();
	        
	        rsval.clear();
	        
			while (rs.next()){
				if (rs.getRow() == 1 && !"".equals(strSelMsg)) {
				   rst = new HashMap<String, String>();
				   rst.put("ID", "0");
				   rst.put("cm_micode", "0000");
				   rst.put("cc_orderid2", strSelMsg);
				   rsval.add(rst);
				   rst = null;
				}
				rst = new HashMap<String, String>();
				rst.put("cc_orderid", rs.getString("cc_orderid"));
				rst.put("cc_reqsub", rs.getString("cc_reqsub"));
				rst.put("cc_orderid2", rs.getString("cc_orderid")+ " (" + rs.getString("cc_reqsub")+  ")");
				rsval.add(rst);
				rst = null;	
			}//end of while-loop statement
			rs.close();
			pstmt.close();
			conn.close();
			
			rs = null;
			pstmt = null;
			conn = null;
			
			returnObjectArray = rsval.toArray();
			rsval = null;
			//ecamsLogger.debug(rsval.toString());		
			return returnObjectArray;			
			
		} catch (SQLException sqlexception) {
			sqlexception.printStackTrace();
			ecamsLogger.error("## Cmd0100.getRsrcOpen2() SQLException START ##");
			ecamsLogger.error("## Error DESC : ", sqlexception);	
			ecamsLogger.error("## Cmd0100.getRsrcOpen2() SQLException END ##");			
			throw sqlexception;
		} catch (Exception exception) {
			exception.printStackTrace();
			ecamsLogger.error("## Cmd0100.getRsrcOpen2() Exception START ##");				
			ecamsLogger.error("## Error DESC : ", exception);	
			ecamsLogger.error("## Cmd0100.getRsrcOpen2() Exception END ##");				
			throw exception;
		}finally{ 
			if (returnObjectArray != null)	returnObjectArray = null;
			if (strQuery != null)	strQuery = null;
			if (rs != null)     try{rs.close();}catch (Exception ex){ex.printStackTrace();}
			if (pstmt != null)  try{pstmt.close();}catch (Exception ex2){ex2.printStackTrace();}
			if (conn != null){
				try{
					ConnectionResource.release(conn);
				}catch(Exception ex3){
					ecamsLogger.error("## Cmd0100.getRsrcOpen2() connection release exception ##");
					ex3.printStackTrace();
				}
			}
		}		

	}

	public Object[] Cmd0100_search(String userid, String syscod) throws SQLException, Exception {
		Connection        conn        = null;
		PreparedStatement pstmt       = null;
		ResultSet         rs          = null;
		StringBuffer      strQuery    = new StringBuffer();
		eCAMSInfo         ecamsinfo   = new eCAMSInfo();
		String            strFile     = "";
		String            strBinPath  = "";
		String            strTmpPath  = "";
		
		boolean           ErrSw      = false;
		boolean           fileSw      = false;
		boolean           findSw      = false;
		
		File shfile=null;
		OutputStreamWriter writer = null;
		Runtime  run = null;
		String[] strAry = null;
		Process p = null;
		
		ArrayList<HashMap<String, String>>  rsval = new ArrayList<HashMap<String, String>>();
		HashMap<String, String>			  rst		  = null;	
		
		Object[] returnObjectArray = null;
		String            strSelMsg   = "";
	    		
		ConnectionContext connectionContext = new ConnectionResource();		
		
		try {
			conn = connectionContext.getConnection();
			strQuery.setLength(0);
			
			try {
				strBinPath = ecamsinfo.getFileInfo("14");
				ErrSw = false;
				if ("".equals(strBinPath) || strBinPath == null) {
					if(conn != null) {
						conn.close();
						conn=null;
					}
					throw new Exception("관리자에게 연락하여 주시기 바랍니다. (형상관리환경설정 - 실행디렉토리)");
				}	
		
				strTmpPath = ecamsinfo.getFileInfo("99");
				if ("".equals(strTmpPath) || strTmpPath == null) {
					if(conn != null) {
						conn.close();
						conn=null;
					}
					throw new Exception("관리자에게 연락하여 주시기 바랍니다. (형상관리환경설정 - 실행디렉토리)");
				}
					
//				ecamsLogger.error("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@1");
				
				shfile=null;
				shfile = new File(strBinPath + userid+".sh"); 
				strFile = strTmpPath + userid + ".txt";

//				if( !(shfile.isFile()) )              //File이 없으면 
//				{
//					shfile.createNewFile();          //File 생성
//				}
//				
//				writer = new OutputStreamWriter( new FileOutputStream(strTmpPath + userid+".sh"));
//				writer.write("cd "+strBinPath +"\n");
//				writer.write("ecams_autorsrc.sh " +   syscod + " " + userid + "\n");
//				writer.write("exit $rtval\n");
//				writer.close();
//				
//				strAry = new String[3];
//				
//				strAry[0] = "chmod";
//				strAry[1] = "777";
//				strAry[2] = strTmpPath + userid+".sh";
//				
//				run = Runtime.getRuntime();
//
//				p = run.exec(strAry);
//				p.waitFor();
//								
//				run = Runtime.getRuntime();
//				
//				strAry = new String[2];
//				
//				strAry[0] = "/bin/sh";
//				strAry[1] = strTmpPath + userid+".sh";
//				
//				p = run.exec(strAry);
//				p.waitFor();
//
//				ecamsLogger.error("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@2");
//				//ecamsLogger.debug("====return===="+Integer.toString(p.exitValue())+" \n");
//				if (p.exitValue() != 0) {
//					ErrSw = true;
//					throw new Exception("신규대상목록 추출을 위한 작업에 실패하였습니다 ");
//				}
//				else{
//					shfile.delete();
//				}	//밑에서 실행할때 텍스트 파일을 만들기 때문에 필요 없다.
				
				
			} catch (Exception e) {
				throw new Exception(e);
			} 
			if (!ErrSw) {
				File mFile = new File(strFile);	//파일을 생성한다.
		        if (!mFile.isFile() || !mFile.exists()) {	//파일을 생성하지 못하면
		        	ErrSw = true;
//					throw new Exception("신규대상목록 추출을 위한 작업에 실패하였습니다 [작성된 파일 없음]");
		        	if(conn != null) {
						conn.close();
						conn=null;
					}
					return null;	//null
					//null값을 플렉스로 리턴시켜준다.
		        } else {				        
			        BufferedReader in = null;
			        fileSw = true;
			        //PrintWriter out = null;
			        
			        try {
			            in = new BufferedReader(new FileReader(mFile));	//파일을 읽어온다
			            String str = null;
			            String wcod = "";
			            String lcod = "";
			            int last = 0;
			            int x = 0;
			            int i = 0;
			            

			            while ((str = in.readLine()) != null) {			//str 변수에 읽어온 파일을 넣고 없지 않을 때까지 반복한다.
			            	rst = new HashMap<String, String>();
			            	last = str.lastIndexOf(" ");				//끝에서 찾아라 공백을(숫자로 표시)
			            	
			            	if(last>0){									//찾은 것이 있으면
			            		for(i = 0; i<9 ; i++){
					            	x = str.indexOf("\t");
					            	//ecamsLogger.error("/////"+i+"///////////////////////////" + x + "//////////////////////////////");
					            	
					            	//System.out.println("////////i ="+i+"/////////////x//////=////"+x+"///////" );
				            		wcod = str.substring(0,x).trim();
					            	str = str.substring(x).trim();
					            	rst.put("column" + i, wcod);

			            		}
			            		lcod = str.trim();
			            		rst.put("column9", lcod);
	        					rsval.add(rst);
	        					rst = null;
		            		}
			            	//text파일 잘라서 colum에 넣는 부분
        					rst = null;
			            }
			        }finally {
			            if (in != null)
			                in.close();
			        }
		        }
//		        if (mFile.isFile() && mFile.exists()) mFile.delete();
            }
            conn.close();
			rs = null;
			pstmt = null;
			conn = null;
    		returnObjectArray = rsval.toArray();
    		//ecamsLogger.debug(rsval.toString());
    		rsval = null;
    		return returnObjectArray;
			
		} catch (SQLException sqlexception) {
			sqlexception.printStackTrace();
			ecamsLogger.error("## Cmd0100.Cmd0100_search() SQLException START ##");
			ecamsLogger.error("## Error DESC : ", sqlexception);	
			ecamsLogger.error("## Cmd0100.Cmd0100_search() SQLException END ##");			
			throw sqlexception;
		} catch (Exception exception) {
			exception.printStackTrace();
			ecamsLogger.error("## Cmd0100.Cmd0100_search() Exception START ##");				
			ecamsLogger.error("## Error DESC : ", exception);	
			ecamsLogger.error("## Cmd0100.Cmd0100_search() Exception END ##");
			throw exception;
		}finally{ 
			if (returnObjectArray != null)	returnObjectArray = null;
			if (strQuery != null)	strQuery = null;
			if (rs != null)     try{rs.close();}catch (Exception ex){ex.printStackTrace();}
			if (pstmt != null)  try{pstmt.close();}catch (Exception ex2){ex2.printStackTrace();}
			if (conn != null){
				try{
					ConnectionResource.release(conn);
				}catch(Exception ex3){
					ecamsLogger.error("## Cmd0100.Cmd0100_search2() connection release exception ##");
					ex3.printStackTrace();
				}
			}
		}
	}
	
	public Object[] Cmd0100_search2(String userid, String syscod,String jobcd,String progName) throws SQLException, Exception {
		Connection        conn        = null;
		PreparedStatement pstmt       = null;
		ResultSet         rs          = null;
		StringBuffer      strQuery    = new StringBuffer();
		eCAMSInfo         ecamsinfo   = new eCAMSInfo();
		String            strFile     = "";
		String            strBinPath  = "";
		String            strTmpPath  = "";
		
		boolean           ErrSw      = false;
		
		File shfile=null;
		OutputStreamWriter writer = null;
		Runtime  run = null;
		String[] strAry = null;
		Process p = null;
		
		ArrayList<HashMap<String, String>>  rsval = new ArrayList<HashMap<String, String>>();
		HashMap<String, String>			  rst		  = null;	
				
		Object[] returnObjectArray = null;
	    		
		ConnectionContext connectionContext = new ConnectionResource();		
		String rtString = "";
		try {
			conn = connectionContext.getConnection();
			
			try {
				strBinPath = ecamsinfo.getFileInfo("14");
				ErrSw = false;
				if ("".equals(strBinPath) || strBinPath == null) {
					if(conn != null) {
						conn.close();
						conn=null;
					}
					throw new Exception("관리자에게 연락하여 주시기 바랍니다. (형상관리환경설정 - 실행디렉토리)");
				}	
		
				strTmpPath = ecamsinfo.getFileInfo("99");
				if ("".equals(strTmpPath) || strTmpPath == null) {
					if(conn != null) {
						conn.close();
						conn=null;
					}
					throw new Exception("관리자에게 연락하여 주시기 바랍니다. (형상관리환경설정 - 실행디렉토리)");
				}	

				shfile=null;
				shfile = new File(strBinPath + userid+".sh"); 
				strFile = strTmpPath + userid + ".txt";
				

				if( !(shfile.isFile()) )              //File이 없으면 
				{
					shfile.createNewFile();          //File 생성
				}
				
				// 20221219 ecams_batexec 추가 쿼리
				strQuery.setLength(0);
				strQuery.append("select cm_ipaddr, cm_port 	\n");
				strQuery.append("  from cmm0010 			\n");
				strQuery.append(" where cm_stno = 'ECAMS'	\n");
				
				pstmt = conn.prepareStatement(strQuery.toString());	
				rs = pstmt.executeQuery();
				if(rs.next()){
					rtString = rs.getString("cm_ipaddr") + " " + rs.getString("cm_port") + " 0";
				}// 20221219 ecams_batexec 추가 쿼리
				rs.close();
				pstmt.close();
				
				writer = new OutputStreamWriter( new FileOutputStream(strTmpPath + userid+".sh"));
				writer.write("cd "+strBinPath +"\n");
				if (jobcd != null && !"".equals(jobcd) && !"XXX".equals(jobcd)) {
					if (progName != null && !"".equals(progName)) writer.write("./ecams_batexec " +rtString + " \"cd " + strBinPath+ ";" +"./ecams_autorsrc " + syscod + " " + userid + " " + jobcd + " " + progName + "\" \n");
					else writer.write("./ecams_batexec " +rtString + " \"cd " + strBinPath+ ";" +"./ecams_autorsrc " + syscod + " " + userid + " " + jobcd + "\" \n");
				} else writer.write("./ecams_batexec " +rtString + " \"cd " + strBinPath+ ";" +"./ecams_autorsrc " + syscod + " " + userid + "\" \n");
				writer.close();
				
				strAry = new String[3];
				
				strAry[0] = "chmod";
				strAry[1] = "777";
				strAry[2] = strTmpPath + userid+".sh";
				
				run = Runtime.getRuntime();

				p = run.exec(strAry);
				p.waitFor();
								
				run = Runtime.getRuntime();
				
				strAry = new String[2];
				
				strAry[0] = "/bin/sh";
				strAry[1] = strTmpPath + userid+".sh";
				
				p = run.exec(strAry);
				p.waitFor();

				//ecamsLogger.error("====return===="+Integer.toString(p.exitValue())+" \n");
				if (p.exitValue() != 0) {
					ErrSw = true;
					if(conn != null) {
						conn.close();
						conn = null;
					}
					throw new Exception("신규대상목록 추출을 위한 작업에 실패하였습니다 ");
				}
				else{
					shfile.delete();
				}	
			} catch (Exception e) {
				if(conn != null) {
					conn.close();
					conn = null;
				}
				throw new Exception(e);
			} 
			if (!ErrSw) {
				File mFile = new File(strFile);	//파일을 생성한다.
		        if (!mFile.isFile() || !mFile.exists()) {	//파일을 생성하지 못하면
		        	ErrSw = true;
//					throw new Exception("신규대상목록 추출을 위한 작업에 실패하였습니다 [작성된 파일 없음]");
		        	if(conn != null) {
						conn.close();
						conn = null;
					}
					return null;	//null
					//null값을 플렉스로 리턴시켜준다.
		        } else {				        
			        BufferedReader in = null;
			        
			        try {
			            in = new BufferedReader(new FileReader(mFile));	//파일을 읽어온다
			        	String str = null;
			            String wcod = "";
			            String lcod = "";
			            int last = 0;
			            int x = 0;
			            int i = 0;

			            while ((str = in.readLine()) != null) {			//str 변수에 읽어온 파일을 넣고 없지 않을 때까지 반복한다.
			            	rst = new HashMap<String, String>();
			            	last = str.lastIndexOf(" ");				//끝에서 찾아라(숫자로 표시)
			            	/*
			            	if(last<0){									//찾은것이 없으면
			            		for(i = 0; i<11 ; i++){
					            	x = str.indexOf("\t");
//					            	ecamsLogger.error("///////////////////////////" + x + "//////////////////////////////");
				            		wcod = str.substring(0,x).trim();
					            	str = str.substring(x).trim();
					            	rst.put("col" + i, wcod);
			            		}
			            		lcod = str.trim();
			            		rst.put("col11", lcod);
	        					rsval.add(rst);
	        					rst = null;
		            		}*/
			            	if(last>0){									//찾은 것이 있으면
			            		for(i = 0; i<9 ; i++){
					            	x = str.indexOf("\t");
					            	//ecamsLogger.error("/////"+i+"///////////////////////////" + x + "//////////////////////////////");
					            	
					            	//System.out.println("////////i ="+i+"/////////////x//////=////"+x+"///////" );
				            		wcod = str.substring(0,x).trim();
					            	str = str.substring(x).trim();
					            	rst.put("column" + i, wcod);

			            		}
			            		lcod = str.trim();
			            		rst.put("column9", lcod);
			            		
			            		if (progName != null && progName.length()>0 && !"".equals(progName)) {
			            			if (rst.get("column2").indexOf(progName)<0) continue;
			            		}
	        					rsval.add(rst);
	        					rst = null;
		            		}
			            	//text파일 잘라서 colum에 넣는 부분
        					rst = null;
			            }
			        }finally {
			            if (in != null)
			                in.close();
			        }
		        }
		        if (mFile.isFile() && mFile.exists()) mFile.delete();
            }
			conn.close();
			pstmt = null;
			conn = null;
			rs = null;
    		returnObjectArray = rsval.toArray();
    		rsval = null;
    		return returnObjectArray;
			
		} catch (SQLException sqlexception) {
			sqlexception.printStackTrace();
			ecamsLogger.error("## Cmd0100.Cmd0100_search2() SQLException START ##");
			ecamsLogger.error("## Error DESC : ", sqlexception);	
			ecamsLogger.error("## Cmd0100.Cmd0100_search2() SQLException END ##");			
			throw sqlexception;
		} catch (Exception exception) {
			exception.printStackTrace();
			ecamsLogger.error("## Cmd0100.Cmd0100_search2() Exception START ##");				
			ecamsLogger.error("## Error DESC : ", exception);	
			ecamsLogger.error("## Cmd0100.Cmd0100_search2() Exception END ##");
			throw exception;
		}finally{ 
			if (returnObjectArray != null)	returnObjectArray = null;
			if (strQuery != null)	strQuery = null;
			if (rs != null)     try{rs.close();}catch (Exception ex){ex.printStackTrace();}
			if (pstmt != null)  try{pstmt.close();}catch (Exception ex2){ex2.printStackTrace();}
			if (conn != null){
				try{
					ConnectionResource.release(conn);
				}catch(Exception ex3){
					ecamsLogger.error("## Cmd0100.Cmd0100_search2() connection release exception ##");
					ex3.printStackTrace();
				}
			}
		}
	}

	public String cmd0100_Insert(ArrayList<HashMap<String, String>>  getCmd0100List) throws SQLException, Exception {
		//플렉스에서 받아온 arraylist 값
		Connection        conn        = null;	
		PreparedStatement pstmt       = null;
		ResultSet         rs          = null;
		StringBuffer      strQuery    = new StringBuffer();
		HashMap<String, String>			  rst		  = null;
		
		String            retMsg      = "0";
		String            strItemId   = "";
		String            oldIsrId    = "";
		String            oldIsrSub   = "";
		boolean           insFg       = true;
		boolean           errSw       = false;
		int 		      parmCnt     = 0;
		String            strGrpCd    = "";
		boolean           findSw      = false;
		String			  ExeName     = "";
		String str = null;
		String OrderId = "";
		String ComPile = "";
		String Team = "";
		String SQLCD = "";
		String DOCCD = "02";
		String EtcDsn = "";
		String MakeComPile = "";
		String EtcDsnHome = "";
		
		rst = new HashMap<String, String>();
		
		int x = 0;
		ConnectionContext connectionContext = new ConnectionResource();
		try {
		
			conn = connectionContext.getConnection();
			conn.setAutoCommit(false);
		for(int i = 0; i<getCmd0100List.size();i++){
			ecamsLogger.error("111111 RsrcName   " + getCmd0100List.get(i).get("RsrcName"  + getCmd0100List.get(i).get("ItemID")));
		}
		
		for(int i = 0; i<getCmd0100List.size();i++){
			parmCnt = 0;
			strQuery.setLength(0);
			strQuery.append("select cr_itemid,cr_orderid \n");
			strQuery.append("  from cmr0020              \n");
			strQuery.append(" where cr_syscd=?           \n");
			strQuery.append("   and cr_dsncd=?           \n");
			strQuery.append("   and cr_rsrcname=?        \n");	//syscd와 dsncd, rsrcname 를 비교하여 itemid,orderid를 찾는다.
			
			pstmt = conn.prepareStatement(strQuery.toString());	
			pstmt.setString(++parmCnt, getCmd0100List.get(i).get("syscd"));   	
			pstmt.setString(++parmCnt, getCmd0100List.get(i).get("Dsncd"));
			pstmt.setString(++parmCnt, getCmd0100List.get(i).get("RsrcName"));		//itemid 찾는 부분
			rs = pstmt.executeQuery();
		
			if (rs.next()) {
				insFg = false;
				strItemId = rs.getString("cr_itemid");
				if (rs.getString("cr_orderid") != null && !"".equals(rs.getString("cr_orderid"))){
					oldIsrId = rs.getString("cr_orderid");
				}
				ecamsLogger.error("@ItemID   " + getCmd0100List.get(i).get("ItemID") + "@RsrcName   " + getCmd0100List.get(i).get("RsrcName"));
				
			}
		
			rs.close();
			pstmt.close();
			
			parmCnt = 0;
			strQuery.setLength(0);
			
			if (insFg) {
				strQuery.append("insert into cmr0020 (CR_ITEMID,CR_SYSCD,CR_DSNCD,CR_RSRCNAME,                                       \n");
				strQuery.append("   CR_RSRCCD,CR_JOBCD,CR_STATUS,CR_CREATOR,CR_STORY,                                                \n");
				strQuery.append("   CR_OPENDATE,CR_LASTDATE,CR_LSTVER,CR_EDITOR,CR_NOMODIFY,CR_ORDERID,CR_TMAXGRP,                   \n");
				strQuery.append("   CR_LANGCD,CR_COMPILE,CR_TEAMCD ,CR_SQLCHECK,CR_DOCUMENT, CR_EXENAME,CR_EXTNAME,CR_MASTER,  CR_DSNCD2,CR_MAKECOMPILE,CR_DSNCD2HOME)                \n");
				strQuery.append("values                                                                                              \n");
				strQuery.append("  (lpad(ITEMID_SEQ.nextval,12,'0'),?,?,?,  ?,?,'3',?,?,  SYSDATE,SYSDATE,0,?,'0',?,?,     ?,?,?,?,?  ,?,?,?,?,?,?) \n");
									//Itemid만드는 부분
				pstmt = conn.prepareStatement(strQuery.toString());
				pstmt = new LoggableStatement(conn,strQuery.toString());
		
				pstmt.setString(++parmCnt, getCmd0100List.get(i).get("syscd"));		//시스템코드
				pstmt.setString(++parmCnt, getCmd0100List.get(i).get("Dsncd"));		//디렉토리 코드
				pstmt.setString(++parmCnt, getCmd0100List.get(i).get("RsrcName"));	//프로그램명
				  
				pstmt.setString(++parmCnt, getCmd0100List.get(i).get("Rsrccd"));	//프로그램 코드
				pstmt.setString(++parmCnt, getCmd0100List.get(i).get("jobcd"));		//업무코드
				pstmt.setString(++parmCnt, getCmd0100List.get(i).get("UserId"));	//사원코드
				pstmt.setString(++parmCnt, getCmd0100List.get(i).get("Story"));		//프로그램설명
				  
				pstmt.setString(++parmCnt, getCmd0100List.get(i).get("UserId"));	//editor
				pstmt.setString(++parmCnt, OrderId);								//orderid
				pstmt.setString(++parmCnt, strGrpCd);								//tmaxgrp
				  
				pstmt.setString(++parmCnt, getCmd0100List.get(i).get("LangCd"));	//langcd
				pstmt.setString(++parmCnt, ComPile);								//compile
				pstmt.setString(++parmCnt, Team);									//teamcd
				pstmt.setString(++parmCnt, SQLCD);									//sqlcheck
				pstmt.setString(++parmCnt, DOCCD);									//document
				
				String tmp = getCmd0100List.get(i).get("RsrcName");
				x = tmp.lastIndexOf(".");
				String ExtName = "";
				if (x>=0) {
					ExeName = getCmd0100List.get(i).get("RsrcName").substring(0,x).trim();		
					ExtName = getCmd0100List.get(i).get("RsrcName").substring(x+1).trim();	//파일명과 확장자 자르는 부분//+1하는 이유는 점을 빼기위해
				} else {
					ExeName = "";
					ExtName = getCmd0100List.get(i).get("RsrcName");
				}
				
				pstmt.setString(++parmCnt, ExeName);								//파일명
				pstmt.setString(++parmCnt, ExtName);								//확장자
				pstmt.setString(++parmCnt, getCmd0100List.get(i).get("UserId"));	//Master
				pstmt.setString(++parmCnt, EtcDsn);									//dsncd
				pstmt.setString(++parmCnt, MakeComPile);							//makecompile
				pstmt.setString(++parmCnt, EtcDsnHome);								//dsncd2home
				
				ecamsLogger.error(((LoggableStatement)pstmt).getQueryString());	
				
				pstmt.executeUpdate();
				pstmt.close();
				
				}else{
					strQuery.append("update cmr0020 set CR_RSRCCD=?,CR_JOBCD=?,                       \n");
					strQuery.append("                   CR_STATUS='3',CR_TMAXGRP=?,                   \n");
					strQuery.append("                   CR_CREATOR=?,CR_STORY=?,                      \n");
					strQuery.append("                   CR_OPENDATE=SYSDATE,                          \n");
					strQuery.append("                   CR_LASTDATE=SYSDATE,CR_LSTVER=0,              \n");
					strQuery.append("                   CR_EDITOR=?,CR_NOMODIFY=0,                    \n");
					strQuery.append("                   CR_ORDERID=?,                                 \n");
					strQuery.append("                   CR_LANGCD =?,CR_COMPILE =?,CR_TEAMCD =? ,     \n");
					strQuery.append("                   CR_SQLCHECK = ?, CR_DOCUMENT = ?, CR_MASTER=?, \n");
					strQuery.append("                   CR_DSNCD2 = ?, CR_MAKECOMPILE = ?, CR_DSNCD2HOME=? \n");
					strQuery.append("where cr_itemid=?                                                \n");
											        	    
					pstmt = conn.prepareStatement(strQuery.toString());
					pstmt = new LoggableStatement(conn,strQuery.toString());
					
					pstmt.setString(++parmCnt, getCmd0100List.get(i).get("Rsrccd"));
					pstmt.setString(++parmCnt, getCmd0100List.get(i).get("jobcd"));
					pstmt.setString(++parmCnt, strGrpCd);
					pstmt.setString(++parmCnt, getCmd0100List.get(i).get("UserId"));
					pstmt.setString(++parmCnt, getCmd0100List.get(i).get("Story"));
					pstmt.setString(++parmCnt, getCmd0100List.get(i).get("UserId"));
					pstmt.setString(++parmCnt, OrderId);
					pstmt.setString(++parmCnt, getCmd0100List.get(i).get("LangCd"));
					pstmt.setString(++parmCnt, ComPile);
					pstmt.setString(++parmCnt, Team);
					pstmt.setString(++parmCnt, SQLCD);
					pstmt.setString(++parmCnt, DOCCD);
					pstmt.setString(++parmCnt, getCmd0100List.get(i).get("UserId"));
					pstmt.setString(++parmCnt, EtcDsn);
					pstmt.setString(++parmCnt, MakeComPile);
					pstmt.setString(++parmCnt, EtcDsnHome);
					pstmt.setString(++parmCnt, strItemId);
					
					ecamsLogger.error(((LoggableStatement)pstmt).getQueryString());
					pstmt.executeUpdate();
					pstmt.close();
					
					ecamsLogger.error("22222 RsrcName   " + getCmd0100List.get(i).get("RsrcName"  + getCmd0100List.get(i).get("ItemID")));
					insFg = true;
					strItemId = "";
					
				}
			}
			retMsg = "0" + strItemId;
			conn.commit();
			conn.close();
			conn = null;
			rs = null;
			pstmt = null;
			return retMsg;
			} catch (SQLException sqlexception) {
				sqlexception.printStackTrace();
				ecamsLogger.error("## Cmd0100.cmd0100_Insert() SQLException START ##");
				ecamsLogger.error("## Error DESC : ", sqlexception);	
				ecamsLogger.error("## Cmd0100.cmd0100_Insert() SQLException END ##");			
			throw sqlexception;
			} catch (Exception exception) {
				exception.printStackTrace();
				ecamsLogger.error("## Cmd0100.cmd0100_Insert() Exception START ##");				
				ecamsLogger.error("## Error DESC : ", exception);	
				ecamsLogger.error("## Cmd0100.cmd0100_Insert() Exception END ##");				
				throw exception;
			}finally{
				if (strQuery != null)	strQuery = null;
			if (rs != null)     try{rs.close();}catch (Exception ex){ex.printStackTrace();}
			if (pstmt != null)  try{pstmt.close();}catch (Exception ex2){ex2.printStackTrace();}
			if (conn != null){
			try{
				ConnectionResource.release(conn);
			}catch(Exception ex3){
				ecamsLogger.error("## Cmd0100.cmd0100_Insert() connection release exception ##");
				ex3.printStackTrace();
				}
			}
		}		
	}//end of cmd0100_Insert() method statement	
	
	public String cmd0100_Insert2(ArrayList<HashMap<String, String>> getCmd0100List, ArrayList<HashMap<String, String>> getCmd0100List2) throws SQLException, Exception {
			//플렉스에서 받아온 arraylist 값
			Connection        conn        = null;
			PreparedStatement pstmt       = null;
			PreparedStatement pstmt2       = null;
			ResultSet         rs2          = null;
			StringBuffer      strQuery    = new StringBuffer();
			
			String            retMsg      = "0";
			String            strItemId   = "";
			boolean           insFg       = true;
			int 		      parmCnt     = 0;
			String            strGrpCd    = "";
			boolean           findSw      = false;
			String			  ExeName     = "";
			String			  ExeName2     = "";
			
			String str = null;
			String OrderId = "";
			String ComPile = "";
			String Team = "";
			String SQLCD = "";
			String DOCCD = "02";
			String EtcDsn = "";
			String MakeComPile = "";
			String EtcDsnHome = "";
			String Basedir = "";
			String Jawon = "";
			String rsrcname = "";
			
			int x = 0;
			ConnectionContext connectionContext = new ConnectionResource();
			try {
			
				conn = connectionContext.getConnection();
				conn.setAutoCommit(false);
				for(int j= 0; j<getCmd0100List2.size();j++){
				parmCnt = 0;
				strQuery.setLength(0);
				strQuery.append("select cr_itemid, cr_rsrcname   \n");
				strQuery.append("from cmr0020                	 \n");
				strQuery.append("where cr_syscd=?            	 \n");
				strQuery.append("and cr_rsrcname=?           	 \n");	//syscd와 dsncd, rsrcname 를 비교하여 itemid,orderid를 찾는다.
				
				pstmt = conn.prepareStatement(strQuery.toString());	
				pstmt.setString(++parmCnt, getCmd0100List2.get(j).get("syscd"));   	
				pstmt.setString(++parmCnt, getCmd0100List2.get(j).get("Jawon"));		//itemid 찾는 부분
				rs2 = pstmt.executeQuery();
				
				if (rs2.next()) {
					strItemId = rs2.getString("cr_itemid");
					rsrcname = rs2.getString("cr_rsrcname");
				
					parmCnt = 0;
					strQuery.setLength(0);
			
					strQuery.append("insert into cmr0020 (CR_ITEMID,CR_SYSCD,CR_DSNCD,CR_RSRCNAME,                                       \n");
					strQuery.append("   CR_RSRCCD,CR_JOBCD,CR_STATUS,CR_CREATOR,CR_STORY,                                                \n");
					strQuery.append("   CR_OPENDATE,CR_LASTDATE,CR_LSTVER,CR_EDITOR,CR_NOMODIFY, CR_BASEITEM, CR_ORDERID,CR_TMAXGRP,     \n");
					strQuery.append("   CR_LANGCD,CR_COMPILE,CR_TEAMCD ,CR_SQLCHECK,CR_DOCUMENT, CR_EXENAME,CR_EXTNAME,CR_MASTER, 		 \n");
					strQuery.append("   CR_DSNCD2,CR_MAKECOMPILE,CR_DSNCD2HOME)                											 \n");
					strQuery.append("values                                                                                              \n");
					strQuery.append("  (lpad(ITEMID_SEQ.nextval,12,'0'),?,?,?,  ?,?,'3',?,?,  SYSDATE,SYSDATE,0,?,'0',?,?,?,     ?,?,?,?,?,?,?,?,   ?,?,?) \n");
										//Itemid만드는 부분
					pstmt2 = conn.prepareStatement(strQuery.toString());
					pstmt2 = new LoggableStatement(conn,strQuery.toString());
		
					pstmt2.setString(++parmCnt, getCmd0100List2.get(j).get("syscd"));		//시스템코드
					pstmt2.setString(++parmCnt, getCmd0100List2.get(j).get("Dsncd"));		//디렉토리 코드
					pstmt2.setString(++parmCnt, getCmd0100List2.get(j).get("RsrcName"));	//프로그램명
					  
					pstmt2.setString(++parmCnt, getCmd0100List2.get(j).get("Rsrccd"));		//프로그램 코드
					pstmt2.setString(++parmCnt, getCmd0100List2.get(j).get("jobcd"));		//업무코드
					pstmt2.setString(++parmCnt, getCmd0100List2.get(j).get("UserId"));		//사원코드
					pstmt2.setString(++parmCnt, getCmd0100List2.get(j).get("Story"));		//프로그램설명
					 
					pstmt2.setString(++parmCnt, getCmd0100List2.get(j).get("UserId"));	//editor
					pstmt2.setString(++parmCnt, strItemId);								//baseitem
					pstmt2.setString(++parmCnt, OrderId);								//orderid
					pstmt2.setString(++parmCnt, strGrpCd);								//tmaxgrp
					  
					pstmt2.setString(++parmCnt, getCmd0100List2.get(j).get("LangCd"));	//langcd
					pstmt2.setString(++parmCnt, ComPile);								//compile
					pstmt2.setString(++parmCnt, Team);									//teamcd
					pstmt2.setString(++parmCnt, SQLCD);									//sqlcheck
					pstmt2.setString(++parmCnt, DOCCD);									//document
					String tmp2 = getCmd0100List2.get(j).get("RsrcName");
					x = tmp2.lastIndexOf(".");
					ExeName2 = getCmd0100List2.get(j).get("RsrcName").substring(0,x).trim();		
					String ExtName2 = getCmd0100List2.get(j).get("RsrcName").substring(x+1).trim();	//파일명과 확장자 자르는 부분
																									//+1하는 이유는 점을 빼기위해
					pstmt2.setString(++parmCnt, ExeName2);								//파일명
					pstmt2.setString(++parmCnt, ExtName2);								//확장자
					pstmt2.setString(++parmCnt, getCmd0100List2.get(j).get("UserId"));	//Master
					
					
					pstmt2.setString(++parmCnt, EtcDsn);								//dsncd
					pstmt2.setString(++parmCnt, MakeComPile);							//makecompile
					pstmt2.setString(++parmCnt, EtcDsnHome);							//dsncd2home
					ecamsLogger.error(((LoggableStatement)pstmt2).getQueryString());	
					pstmt2.executeUpdate();
					pstmt2.close();
				}
				rs2.close();
				pstmt.close();
			}
				
			retMsg = "0" + strItemId;
			
			conn.commit();
			conn.close();
			conn = null;
			rs2 = null;
			pstmt = null;
			pstmt2 = null;
			return retMsg;
			} catch (SQLException sqlexception) {
				sqlexception.printStackTrace();
				ecamsLogger.error("## Cmd0100.cmd0100_Insert() SQLException START ##");
				ecamsLogger.error("## Error DESC : ", sqlexception);	
				ecamsLogger.error("## Cmd0100.cmd0100_Insert() SQLException END ##");			
				throw sqlexception;
			} catch (Exception exception) {
				exception.printStackTrace();
				ecamsLogger.error("## Cmd0100.cmd0100_Insert() Exception START ##");				
				ecamsLogger.error("## Error DESC : ", exception);	
				ecamsLogger.error("## Cmd0100.cmd0100_Insert() Exception END ##");				
				throw exception;
			}finally{
				if (strQuery != null)	strQuery = null;
				if (rs2 != null)     try{rs2.close();}catch (Exception ex){ex.printStackTrace();}
				if (pstmt2 != null)  try{pstmt2.close();}catch (Exception ex2){ex2.printStackTrace();}
				if (conn != null){
				try{
					ConnectionResource.release(conn);
				}catch(Exception ex3){
					ecamsLogger.error("## Cmd0100.cmd0100_Insert() connection release exception ##");
					ex3.printStackTrace();
				}
			}
		}		
	}//end of cmd0100_Insert2() method statement	
	
		public String cmd0100_Insert3(ArrayList<HashMap<String, String>>  getCmd0100List) throws SQLException, Exception {
			//플렉스에서 받아온 arraylist 값
			Connection        conn        = null;	
			PreparedStatement pstmt       = null;
			ResultSet         rs          = null;
			StringBuffer      strQuery    = new StringBuffer();
			HashMap<String, String>			  rst		  = null;
			
			String            retMsg      = "0";
			String            strItemId   = "";
			String            oldIsrId    = "";
			String            oldIsrSub   = "";
			boolean           insFg       = true;
			boolean           errSw       = false;
			int 		      parmCnt     = 0;
			String            strGrpCd    = "";
			boolean           findSw      = false;
			String			  ExeName     = "";
			String str = null;
			String OrderId = "";
			String ComPile = "";
			String Team = "";
			String SQLCD = "";
			String DOCCD = "02";
			String EtcDsn = "";
			String MakeComPile = "";
			String EtcDsnHome = "";
			
			rst = new HashMap<String, String>();
			
			int x = 0;
			ConnectionContext connectionContext = new ConnectionResource();
			try {
			
				conn = connectionContext.getConnection();
				conn.setAutoCommit(false);
				for(int i = 0; i<getCmd0100List.size();i++){
					ecamsLogger.error("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@itemid   " + getCmd0100List.get(i).get("syscd"));
					ecamsLogger.error("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@status   " + getCmd0100List.get(i).get("Dsncd"));
					ecamsLogger.error("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@rsrcname   " + getCmd0100List.get(i).get("RsrcName"));
				}
				for(int i = 0; i<getCmd0100List.size();i++){
					parmCnt = 0;
					strQuery.setLength(0);
					strQuery.append("select cr_itemid,cr_orderid \n");
					strQuery.append("  from cmr0020              \n");
					strQuery.append(" where cr_syscd=?           \n");
					strQuery.append("   and cr_dsncd=?           \n");
					strQuery.append("   and cr_rsrcname=?        \n");	//syscd와 dsncd, rsrcname 를 비교하여 itemid,orderid를 찾는다.
					
					pstmt = conn.prepareStatement(strQuery.toString());	
					pstmt = new LoggableStatement(conn,strQuery.toString());
					pstmt.setString(++parmCnt, getCmd0100List.get(i).get("syscd"));   	
					pstmt.setString(++parmCnt, getCmd0100List.get(i).get("Dsncd"));
					pstmt.setString(++parmCnt, getCmd0100List.get(i).get("RsrcName"));		//itemid 찾는 부분
					ecamsLogger.error(((LoggableStatement)pstmt).getQueryString());
					rs = pstmt.executeQuery();
				
					if (rs.next()) {
						insFg = false;
						strItemId = rs.getString("cr_itemid");
						if (rs.getString("cr_orderid") != null && !"".equals(rs.getString("cr_orderid"))){
							oldIsrId = rs.getString("cr_orderid");
						}	
					}
					ecamsLogger.error("=================================itemid   " + strItemId);
					rs.close();
					pstmt.close();
					
					parmCnt = 0;
					strQuery.setLength(0);
					if (insFg) {
						strQuery.append("insert into cmr0020 (CR_ITEMID,CR_SYSCD,CR_DSNCD,CR_RSRCNAME,                                       \n");
						strQuery.append("   CR_RSRCCD,CR_JOBCD,CR_STATUS,CR_CREATOR,CR_STORY,                                                \n");
						strQuery.append("   CR_OPENDATE,CR_LASTDATE,CR_LSTVER,CR_EDITOR,CR_NOMODIFY,CR_ORDERID,CR_TMAXGRP,                   \n");
						strQuery.append("   CR_LANGCD,CR_COMPILE,CR_TEAMCD ,CR_SQLCHECK,CR_DOCUMENT, CR_EXENAME,CR_EXTNAME,CR_MASTER,  CR_DSNCD2,CR_MAKECOMPILE,CR_DSNCD2HOME)                \n");
						strQuery.append("values                                                                                              \n");
						strQuery.append("  (lpad(ITEMID_SEQ.nextval,12,'0'),?,?,?,  ?,?,'3',?,?,  SYSDATE,SYSDATE,0,?,'0',?,?,     ?,?,?,?,?  ,?,?,?,?,?,?) \n");
											//Itemid만드는 부분
						pstmt = conn.prepareStatement(strQuery.toString());
						pstmt = new LoggableStatement(conn,strQuery.toString());
						
						pstmt.setString(++parmCnt, getCmd0100List.get(i).get("syscd"));		//시스템코드
						pstmt.setString(++parmCnt, getCmd0100List.get(i).get("Dsncd"));		//디렉토리 코드
						pstmt.setString(++parmCnt, getCmd0100List.get(i).get("RsrcName"));	//프로그램명
						  
						pstmt.setString(++parmCnt, getCmd0100List.get(i).get("Rsrccd"));	//프로그램 코드
						pstmt.setString(++parmCnt, getCmd0100List.get(i).get("jobcd"));		//업무코드
						pstmt.setString(++parmCnt, getCmd0100List.get(i).get("UserId"));	//사원코드
						pstmt.setString(++parmCnt, getCmd0100List.get(i).get("Story"));		//프로그램설명
						  
						pstmt.setString(++parmCnt, getCmd0100List.get(i).get("UserId"));	//editor
						pstmt.setString(++parmCnt, OrderId);								//orderid
						pstmt.setString(++parmCnt, strGrpCd);								//tmaxgrp
						  
						pstmt.setString(++parmCnt, getCmd0100List.get(i).get("LangCd"));	//langcd
						pstmt.setString(++parmCnt, ComPile);								//compile
						pstmt.setString(++parmCnt, Team);									//teamcd
						pstmt.setString(++parmCnt, SQLCD);									//sqlcheck
						pstmt.setString(++parmCnt, DOCCD);									//document
						
						String tmp = getCmd0100List.get(i).get("RsrcName");
						x = tmp.lastIndexOf(".");
						ExeName = getCmd0100List.get(i).get("RsrcName").substring(0,x).trim();		
						String ExtName = getCmd0100List.get(i).get("RsrcName").substring(x+1).trim();	//파일명과 확장자 자르는 부분
																										//+1하는 이유는 점을 빼기위해
						
						pstmt.setString(++parmCnt, ExeName);								//파일명
						pstmt.setString(++parmCnt, ExtName);								//확장자
						pstmt.setString(++parmCnt, getCmd0100List.get(i).get("UserId"));	//Master
						pstmt.setString(++parmCnt, EtcDsn);									//dsncd
						pstmt.setString(++parmCnt, MakeComPile);							//makecompile
						pstmt.setString(++parmCnt, EtcDsnHome);								//dsncd2home
						
						ecamsLogger.error(((LoggableStatement)pstmt).getQueryString());	
						
						pstmt.executeUpdate();
						pstmt.close();
					
		
					}else{
						strQuery.append("update cmr0020 set CR_RSRCCD=?,CR_JOBCD=?,                       \n");
						strQuery.append("                   CR_STATUS='3',CR_TMAXGRP=?,                   \n");
						strQuery.append("                   CR_CREATOR=?,CR_STORY=?,                      \n");
						strQuery.append("                   CR_OPENDATE=SYSDATE,                          \n");
						strQuery.append("                   CR_LASTDATE=SYSDATE,CR_LSTVER=0,              \n");
						strQuery.append("                   CR_EDITOR=?,CR_NOMODIFY=0,                    \n");
						strQuery.append("                   CR_ORDERID=?,                                 \n");
						strQuery.append("                   CR_LANGCD =?,CR_COMPILE =?,CR_TEAMCD =? ,     \n");
						strQuery.append("                   CR_SQLCHECK = ?, CR_DOCUMENT = ?, CR_MASTER=?, \n");
						strQuery.append("                   CR_DSNCD2 = ?, CR_MAKECOMPILE = ?, CR_DSNCD2HOME=? \n");
						strQuery.append("where cr_itemid=?                                                \n");
												        	    
						pstmt = conn.prepareStatement(strQuery.toString());
						pstmt = new LoggableStatement(conn,strQuery.toString());
						
						pstmt.setString(++parmCnt, getCmd0100List.get(i).get("Rsrccd"));
						pstmt.setString(++parmCnt, getCmd0100List.get(i).get("jobcd"));
						pstmt.setString(++parmCnt, strGrpCd);
						pstmt.setString(++parmCnt, getCmd0100List.get(i).get("UserId"));
						pstmt.setString(++parmCnt, getCmd0100List.get(i).get("Story"));
						pstmt.setString(++parmCnt, getCmd0100List.get(i).get("UserId"));
						pstmt.setString(++parmCnt, OrderId);
						pstmt.setString(++parmCnt, getCmd0100List.get(i).get("LangCd"));
						pstmt.setString(++parmCnt, ComPile);
						pstmt.setString(++parmCnt, Team);
						pstmt.setString(++parmCnt, SQLCD);
						pstmt.setString(++parmCnt, DOCCD);
						pstmt.setString(++parmCnt, getCmd0100List.get(i).get("UserId"));
						pstmt.setString(++parmCnt, EtcDsn);
						pstmt.setString(++parmCnt, MakeComPile);
						pstmt.setString(++parmCnt, EtcDsnHome);
						pstmt.setString(++parmCnt, strItemId);
						
						ecamsLogger.error(((LoggableStatement)pstmt).getQueryString());
						pstmt.executeUpdate();
						pstmt.close();
						insFg = true;
						}
					}
					retMsg = "0" + strItemId;
					conn.commit();
					conn.close();
					conn = null;
					rs = null;
					pstmt = null;
					return retMsg;
				} catch (SQLException sqlexception) {
					sqlexception.printStackTrace();
					ecamsLogger.error("## Cmd0100.cmd0100_Insert3() SQLException START ##");
					ecamsLogger.error("## Error DESC : ", sqlexception);	
					ecamsLogger.error("## Cmd0100.cmd0100_Insert3() SQLException END ##");			
					throw sqlexception;
				} catch (Exception exception) {
					exception.printStackTrace();
					ecamsLogger.error("## Cmd0100.cmd0100_Insert3() Exception START ##");				
					ecamsLogger.error("## Error DESC : ", exception);	
					ecamsLogger.error("## Cmd0100.cmd0100_Insert3() Exception END ##");				
					throw exception;
				}finally{
				if (strQuery != null)	strQuery = null;
				if (rs != null)     try{rs.close();}catch (Exception ex){ex.printStackTrace();}
				if (pstmt != null)  try{pstmt.close();}catch (Exception ex2){ex2.printStackTrace();}
				if (conn != null){
				try{
					ConnectionResource.release(conn);
				}catch(Exception ex3){
					ecamsLogger.error("## Cmd0100.cmd0100_Insert3() connection release exception ##");
					ex3.printStackTrace();
				}
			}
		}		
	}//end of cmd0100_Insert3() method statement			
}//end of Cmd0100 class statement
