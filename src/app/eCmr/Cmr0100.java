package app.eCmr;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.OutputStreamWriter;

import org.apache.logging.log4j.Logger;

import app.common.AutoSeq;
import app.common.LoggableStatement;
import app.common.StreamGobbler;
import app.common.UserInfo;
import app.common.SysInfo;
import app.common.SystemPath;
import app.common.eCAMSInfo;
import app.eCmm.Cmm1600;

import com.ecams.common.dbconn.ConnectionContext;
import com.ecams.common.dbconn.ConnectionResource;
import com.ecams.common.logger.EcamsLogger;

/**
 * <PRE>
 * 1. ClassName	: 
 * 2. FileName		: Cmr0100.java
 * 3. Package		: app.eCmr
 * 4. Commnet		: 
 * 5. 작성자			: Administrator
 * 6. 작성일			: 2010. 12. 8. 오전 9:12:49
 * </PRE>
 */
public class Cmr0100 {

	Logger ecamsLogger  = EcamsLogger.getLoggerInstance();

	/**
	 * <PRE>
	 * 1. MethodName	: getTmpDirConf
	 * 2. ClassName		: Cmr0100
	 * 3. Commnet			: cmm0012테이블에서 경로 조회하기
	 * 4. 작성자				: no name
	 * 5. 작성일				: 2010. 12. 8. 오전 9:13:23
	 * </PRE>
	 * 		@return Object[]
	 * 		@param pCode : 경로구분코드값(cmm0012테이블의 cm_pathcd) 
	 * 		@return
	 * 		@throws SQLException
	 * 		@throws Exception
	 */
	public Object[] getTmpDirConf(String pCode) throws SQLException, Exception {
		Connection        conn        = null;
		PreparedStatement pstmt       = null;
		ResultSet         rs          = null;
		StringBuffer      strQuery    = new StringBuffer();
		ConnectionContext connectionContext = new ConnectionResource();
		ArrayList<HashMap<String, String>>		  rtList	  = new ArrayList<HashMap<String, String>>();
		HashMap<String, String>			  rst		  = null;
		
		try {
			conn = connectionContext.getConnection();
		    rtList.clear();
		    
			strQuery.append("SELECT a.CM_IPADDR,a.CM_PORT,b.CM_PATH \n");
			strQuery.append("FROM cmm0012 b,cmm0010 a               \n");
			strQuery.append("WHERE a.cm_stno = 'ECAMS'              \n");
			strQuery.append("  AND a.cm_stno=b.cm_stno              \n");
			strQuery.append("  AND cm_pathcd = ?                    \n");
			
			pstmt = conn.prepareStatement(strQuery.toString());
			pstmt.setString(1, pCode);

			
            rs = pstmt.executeQuery();
            
			if (rs.next()){
				rst = new HashMap<String,String>();
				rst.put("cm_ipaddr", rs.getString("cm_ipaddr"));
				rst.put("cm_port", rs.getString("cm_port"));
				rst.put("cm_path", rs.getString("cm_path"));
				rtList.add(rst);
			}//end of while-loop statement
			
			rs.close();
			pstmt.close();
			conn.close();
			conn = null;
			pstmt = null;
			rs = null;
			return rtList.toArray();
			
		} catch (SQLException sqlexception) {
			sqlexception.printStackTrace();
			ecamsLogger.error("## Cmr0100.getTmpDirConf() SQLException START ##");
			ecamsLogger.error("## Error DESC : ", sqlexception);	
			ecamsLogger.error("## Cmr0100.getTmpDirConf() SQLException END ##");			
			throw sqlexception;
		} catch (Exception exception) {
			exception.printStackTrace();
			ecamsLogger.error("## Cmr0100.getTmpDirConf() Exception START ##");				
			ecamsLogger.error("## Error DESC : ", exception);	
			ecamsLogger.error("## Cmr0100.getTmpDirConf() Exception END ##");				
			throw exception;
		}finally{
			if (strQuery != null) 	strQuery = null;
			if (rs != null)     try{rs.close();}catch (Exception ex){ex.printStackTrace();}
			if (pstmt != null)  try{pstmt.close();}catch (Exception ex2){ex2.printStackTrace();}
			if (conn != null){
				try{
					ConnectionResource.release(conn);
				}catch(Exception ex3){
					ecamsLogger.error("## SystemPath.getTmpDirConf() connection release exception ##");
					ex3.printStackTrace();
				}
			}
		}
		
	}//end of getTmpDirConf() method statement
	
	public String getDsnCD(String sysCD,String Path) throws SQLException, Exception {
		Connection        conn        = null;
		PreparedStatement pstmt       = null;
		ResultSet         rs          = null;
		StringBuffer      strQuery    = new StringBuffer();
		ConnectionContext connectionContext = new ConnectionResource();
		String			  rtString = null;
		
		try {
			conn = connectionContext.getConnection();
		
			strQuery.append("SELECT cm_dsncd \n");
			strQuery.append("FROM cmm0070 \n");
			strQuery.append("WHERE cm_syscd = ? \n");
			strQuery.append("AND cm_dirpath = ? \n");
			
			pstmt = conn.prepareStatement(strQuery.toString());
			pstmt.setString(1, sysCD);
			pstmt.setString(2, Path);
			
            rs = pstmt.executeQuery();
            
            rtString = "";
            
			if (rs.next()){
				rtString = rs.getString("cm_dsncd");
			}//end of while-loop statement
			rs.close();
			pstmt.close();
			conn.close();
			conn = null;
			pstmt = null;
			rs = null;
			return rtString;
			
		} catch (SQLException sqlexception) {
			sqlexception.printStackTrace();
			ecamsLogger.error("## Cmr0100.getDsnCD() SQLException START ##");
			ecamsLogger.error("## Error DESC : ", sqlexception);	
			ecamsLogger.error("## Cmr0100.getDsnCD() SQLException END ##");			
			throw sqlexception;
		} catch (Exception exception) {
			exception.printStackTrace();
			ecamsLogger.error("## Cmr0100.getDsnCD() Exception START ##");				
			ecamsLogger.error("## Error DESC : ", exception);	
			ecamsLogger.error("## Cmr0100.getDsnCD() Exception END ##");				
			throw exception;
		}finally{
			if (strQuery != null) 	strQuery = null;
			if (rs != null)     try{rs.close();}catch (Exception ex){ex.printStackTrace();}
			if (pstmt != null)  try{pstmt.close();}catch (Exception ex2){ex2.printStackTrace();}
			if (conn != null){
				try{
					ConnectionResource.release(conn);
				}catch(Exception ex3){
					ecamsLogger.error("## Cmr0100.getDsnCD() connection release exception ##");
					ex3.printStackTrace();
				}
			}
		}
	}//end of SelectUserInfo() method statement			
	
	public Boolean chk_Resouce(String syscd,String Rsrccd) throws SQLException, Exception {
		Connection        conn        = null;
		PreparedStatement pstmt       = null;
		ResultSet         rs          = null;
		StringBuffer      strQuery    = new StringBuffer();
		ConnectionContext connectionContext = new ConnectionResource();
		int				  rtnval	  = 0;
		try {
	        
			conn = connectionContext.getConnection();
			strQuery.append("select count(cm_rsrccd) as rowcnt from cmm0036 ");
			strQuery.append("where cm_syscd= ? ");
			strQuery.append("and   cm_rsrccd= ? ");
		
            pstmt = conn.prepareStatement(strQuery.toString());	
            pstmt.setString(1, syscd);
            pstmt.setString(2, Rsrccd);
            //pstmt.setInt(2, cnt);	            
            
            
            rs = pstmt.executeQuery();
                        
			while (rs.next()){
				rtnval = rs.getInt("rowcnt");	
			}//end of while-loop statement
			
			rs.close();
			pstmt.close();
			conn.close();
			conn = null;
			pstmt = null;
			rs = null;
			if (rtnval > 0){
				return true;
			}
			else{
				return false;
			}			
			
		} catch (SQLException sqlexception) {
			sqlexception.printStackTrace();
			ecamsLogger.error("## Cmr0100.chk_Resouce() SQLException START ##");
			ecamsLogger.error("## Error DESC : ", sqlexception);	
			ecamsLogger.error("## Cmr0100.chk_Resouce() SQLException END ##");			
			throw sqlexception;
		} catch (Exception exception) {
			exception.printStackTrace();
			ecamsLogger.error("## Cmr0100.chk_Resouce() Exception START ##");				
			ecamsLogger.error("## Error DESC : ", exception);	
			ecamsLogger.error("## Cmr0100.chk_Resouce() Exception END ##");				
			throw exception;
		}finally{
			if (strQuery != null) 	strQuery = null;
			if (rs != null)     try{rs.close();}catch (Exception ex){ex.printStackTrace();}
			if (pstmt != null)  try{pstmt.close();}catch (Exception ex2){ex2.printStackTrace();}
			if (conn != null){
				try{
					ConnectionResource.release(conn);
				}catch(Exception ex3){
					ecamsLogger.error("## Cmr0100.chk_Resouce() connection release exception ##");
					ex3.printStackTrace();
				}
			}
		}
	}
	
	public Object[] getDownFileList(String UserId,ArrayList<HashMap<String,String>> fileList,String ReqCd,String SysCd,String ReqSayu) throws SQLException, Exception {
		Connection        conn        = null;
		PreparedStatement pstmt       = null;
		ResultSet         rs          = null;
		String            strDevHome  = null;
		StringBuffer      strQuery    = new StringBuffer();
		Object[]		  rtObj		  = null;
		ArrayList<HashMap<String, String>>		  rtList	  = new ArrayList<HashMap<String, String>>();
		ArrayList<HashMap<String, String>>		  svrList	  = new ArrayList<HashMap<String, String>>();
		HashMap<String, String>			  rst		  = null;
		ConnectionContext connectionContext = new ConnectionResource();
		String            baseAcpt = "";
		
		try {
			conn = connectionContext.getConnection();

			rtList.clear();
			String            strVolPath = "";
			String            strDirPath = "";
			SysInfo sysinfo = new SysInfo();
			boolean localSw = sysinfo.getLocalYn(SysCd);
			
			if (localSw) {
				strQuery.setLength(0);
				strQuery.append("select cd_devhome from cmd0200               \n");
				strQuery.append(" where cd_syscd=? and cd_userid=?            \n");
				pstmt = conn.prepareStatement(strQuery.toString());
				//pstmt =  new LoggableStatement(conn, strQuery.toString());
				pstmt.setString(1, SysCd);
				pstmt.setString(2, UserId);
				////ecamsLogger.error(((LoggableStatement)pstmt).getQueryString());
				rs = pstmt.executeQuery();
				if (rs.next()) {
					strDevHome = rs.getString("cd_devhome") + "\\";
				}
				rs.close();
				pstmt.close();
				
				svrList = sysinfo.getHomePath_conn(SysCd, conn);
			}
			
			
			for (int i=0;i<fileList.size();i++){
				if (!ReqCd.equals("02")) {
					strQuery.setLength(0);
					strQuery.append("select a.cr_acptno from cmr0021 a, \n");
					strQuery.append("   (select max(cr_prcdate) maxacpt \n");
					strQuery.append("      from cmr0021                 \n");
					strQuery.append("     where cr_itemid=?) b          \n");
					strQuery.append(" where a.cr_itemid=?               \n");
					strQuery.append("   and a.cr_prcdate=b.maxacpt      \n");
					pstmt = conn.prepareStatement(strQuery.toString());
					//pstmt =  new LoggableStatement(conn, strQuery.toString());
					pstmt.setString(1, fileList.get(i).get("cr_itemid"));
		            pstmt.setString(2, fileList.get(i).get("cr_itemid"));		        
		            ////ecamsLogger.error(((LoggableStatement)pstmt).getQueryString());  
		            rs = pstmt.executeQuery(); 
					if (rs.next()){
						baseAcpt = rs.getString("cr_acptno");
					}
					rs.close();
					pstmt.close();
				} else {
					baseAcpt = fileList.get(i).get("selAcptno");
				}
				
				if (baseAcpt == null || "".equals(baseAcpt)) {
					if(conn != null) conn.close();
					conn = null;
					throw new Exception("체크아웃 기준이 되는 신청기록이 존재하지 않습니다.("+fileList.get(0).get("cr_rsrcname")+")");
				}
				rst = new HashMap<String,String>();
				rst.put("cm_dirpath",fileList.get(i).get("cm_dirpath"));
				rst.put("cr_rsrcname",fileList.get(i).get("cr_rsrcname"));
				rst.put("jobname", fileList.get(i).get("jobname"));
				rst.put("jawon", fileList.get(i).get("jawon"));
				rst.put("cr_lstver",fileList.get(i).get("cr_lstver"));
				rst.put("cr_itemid",fileList.get(i).get("cr_itemid"));
				rst.put("sysgb", fileList.get(i).get("sysgb"));
				rst.put("cr_syscd", fileList.get(i).get("cr_syscd"));
				rst.put("cr_rsrccd",fileList.get(i).get("cr_rsrccd"));
				rst.put("cr_langcd",fileList.get(i).get("cr_langcd"));
				rst.put("cr_dsncd",fileList.get(i).get("cr_dsncd"));
				rst.put("cr_jobcd",fileList.get(i).get("cr_jobcd"));
				rst.put("baseitemid",fileList.get(i).get("cr_itemid"));
				rst.put("cm_info",fileList.get(i).get("cm_info"));
				rst.put("pcdir",fileList.get(i).get("pcdir"));
				rst.put("cr_pgmtype",fileList.get(i).get("cr_pgmtype"));
				rst.put("pgmtype",fileList.get(i).get("pgmtype"));
				rst.put("cr_editcon",ReqSayu);
				rtList.add(rst);
				rst = null;
				
				strQuery.setLength(0);		
				strQuery.append("select a.cr_rsrccd,a.cr_rsrcname,a.cr_jobcd,a.cr_itemid,         \n");	
				if (ReqCd.equals("02")){ strQuery.append("f.cr_version ver,                       \n");
				} else strQuery.append("a.cr_lstver ver,                                          \n");
				strQuery.append("       a.cr_langcd,a.cr_dsncd,b.cm_dirpath,                      \n");
				strQuery.append("       C.CM_CODENAME as jawon,                                   \n");
				strQuery.append("       D.CM_INFO,f.cr_befver,g.cm_jobname                        \n");
				strQuery.append("  from CMM0036 D,cmm0070 b,cmr0020 a,CMM0020 C,cmr1010 f,cmm0102 g \n"); 
				strQuery.append(" where f.cr_acptno = ? and f.cr_baseitem=?                       \n");
				strQuery.append("   and f.cr_itemid<>f.cr_baseitem                                \n");
				strQuery.append("   and f.cr_itemid=a.cr_itemid                                   \n");
				strQuery.append("   and a.cr_status='0' and a.cr_lstver>0                         \n"); 
				strQuery.append("   and a.cr_syscd=d.cm_syscd and a.cr_rsrccd=d.cm_rsrccd         \n");
				strQuery.append("   and substr(d.cm_info,2,1)='1' and d.cm_closedt is null        \n");
				strQuery.append("   and a.cr_syscd = b.cm_syscd and a.cr_dsncd = b.cm_dsncd       \n");
				strQuery.append("   and C.CM_MACODE='JAWON' and C.CM_MICODE=A.CR_RSRCCD           \n");
				strQuery.append("   and a.cr_jobcd = g.cm_jobcd                                   \n");
	
				pstmt = conn.prepareStatement(strQuery.toString());
				//pstmt =  new LoggableStatement(conn, strQuery.toString());
				
				pstmt.setString(1, baseAcpt);
	            pstmt.setString(2, fileList.get(i).get("cr_itemid"));
	        
	            ////ecamsLogger.error(((LoggableStatement)pstmt).getQueryString());  
	            rs = pstmt.executeQuery();
	            
		            
				while (rs.next()){
					rst = new HashMap<String,String>();
					rst.put("cm_dirpath",rs.getString("cm_dirpath"));
					rst.put("cr_rsrcname",rs.getString("cr_rsrcname"));
					rst.put("jobname", rs.getString("cm_jobname"));
					rst.put("jawon", rs.getString("jawon"));
					rst.put("cr_lstver",rs.getString("ver"));
					rst.put("cr_itemid",rs.getString("cr_itemid"));
					rst.put("sysgb", fileList.get(i).get("sysgb"));
					rst.put("cr_syscd", fileList.get(i).get("cr_syscd"));
					rst.put("cr_rsrccd",rs.getString("cr_rsrccd"));
					rst.put("cr_langcd",rs.getString("cr_langcd"));
					rst.put("cr_dsncd",rs.getString("cr_dsncd"));
					rst.put("cr_jobcd",rs.getString("cr_jobcd"));
					rst.put("baseitemid",fileList.get(i).get("cr_itemid"));
					rst.put("cm_info",rs.getString("cm_info"));
					rst.put("pcdir",fileList.get(i).get("pcdir"));
					rst.put("cr_editcon",ReqSayu);
					if ((rs.getString("cm_info").substring(44,45).equals("1")  || 
						     rs.getString("cm_info").substring(37,38).equals("1")) && strDevHome != null) {
							for (int j=0;svrList.size()>j;j++) {
								if (svrList.get(j).get("cm_rsrccd").equals(rs.getString("cr_rsrccd"))) {
									strVolPath = svrList.get(j).get("cm_volpath");
									strDirPath = rs.getString("cm_dirpath");
									if (strVolPath != null && strVolPath != "") {
										if(strDirPath.length() >= strVolPath.length()){
											if (strDirPath.substring(0,strVolPath.length()).equals(strVolPath)) {
												rst.put("pcdir1", strDevHome + strDirPath.substring(strVolPath.length()).replace("/","\\"));
											}
										}
									} else {
										rst.put("pcdir1", strDevHome + rs.getString("cm_dirpath").replace("/", "\\"));
									}
								}
							}
						}
					rtList.add(rst);
					rst = null;
				}//end of while-loop statement
			}	
			conn.close();
			conn = null;
			pstmt = null;
			rs = null;
			rtObj =  rtList.toArray();
			rtList = null;
			
			return rtObj;			
			
		} catch (SQLException sqlexception) {
			sqlexception.printStackTrace();
			ecamsLogger.error("## Cmr0100.getDownFileList() SQLException START ##");
			ecamsLogger.error("## Error DESC : ", sqlexception);	
			ecamsLogger.error("## Cmr0100.getDownFileList() SQLException END ##");			
			throw sqlexception;
		} catch (Exception exception) {
			exception.printStackTrace();
			ecamsLogger.error("## Cmr0100.getDownFileList() Exception START ##");				
			ecamsLogger.error("## Error DESC : ", exception);	
			ecamsLogger.error("## Cmr0100.getDownFileList() Exception END ##");				
			throw exception;
		}finally{
			if (strQuery != null) 	strQuery = null;
			if (rtObj != null)  rtObj = null;
			if (rs != null)     try{rs.close();}catch (Exception ex){ex.printStackTrace();}
			if (pstmt != null)  try{pstmt.close();}catch (Exception ex2){ex2.printStackTrace();}
			if (conn != null){
				try{
					ConnectionResource.release(conn);
				}catch(Exception ex3){
					ecamsLogger.error("## Cmr0100.getDownFileList() connection release exception ##");
					ex3.printStackTrace();
				}
			}
		}
	}
	
	/**
	 * <PRE>
	 * 1. MethodName	: getFileList
	 * 2. ClassName		: Cmr0100
	 * 3. Commnet			: 
	 * 4. 작성자				: no name
	 * 5. 작성일				: 2010. 12. 23. 오후 2:43:59
	 * </PRE>
	 * 		@return Object[]
	 * 		@param UserId
	 * 		@param SysCd
	 * 		@param SysGb
	 * 		@param DsnCd
	 * 		@param RsrcCd
	 * 		@param RsrcName
	 * 		@param ReqCd
	 * 		@param JobCd
	 * 		@param UpLowSw
	 * 		@param selfSw
	 * 		@return
	 * 		@throws SQLException
	 * 		@throws Exception
	 */
	public Object[] getFileList(String UserId,String SysCd,String SysGb,String DsnCd,String RsrcCd,String RsrcName,String ReqCd,
			String JobCd,boolean UpLowSw,boolean selfSw,boolean LikeSw) throws SQLException, Exception {
		Connection        conn        = null;
		Connection        connD        = null;
		PreparedStatement pstmt       = null;
		ResultSet         rs          = null;
		PreparedStatement pstmt2       = null;
		ResultSet         rs2          = null;
		String            strDevHome  = null;
		StringBuffer      strQuery    = new StringBuffer();
		Object[]		  rtObj		  = null;
		ArrayList<HashMap<String, String>>		  svrList	  = new ArrayList<HashMap<String, String>>();
		ArrayList<HashMap<String, String>>		  rtList	  = new ArrayList<HashMap<String, String>>();
		HashMap<String, String>			  rst		  = null;
		
		ConnectionContext connectionContext = new ConnectionResource();
		ConnectionContext connectionContextD = new ConnectionResource(false,"D");

		try {
			conn = connectionContext.getConnection();
			connD = connectionContextD.getConnection();

			int				  pstmtcount  = 1;
			String			 szDsnCD = "";
			String			  strRsrcCd = "";
			String            strRsrc[] = null;
			String            strVolPath = "";
			String            strDirPath = "";
			int               i = 0;
			
			//UserInfo     userinfo = new UserInfo();
			boolean adminYn = false;//userinfo.isAdmin(UserId);
			//userinfo = null;
			
			SysInfo sysinfo = new SysInfo();
			boolean localSw = sysinfo.getLocalYn(SysCd);
			
			if (localSw) {
				strQuery.setLength(0);
				strQuery.append("select cd_devhome from cmd0200               \n");
				strQuery.append(" where cd_syscd=? and cd_userid=?            \n");
				pstmt = conn.prepareStatement(strQuery.toString());
				//pstmt =  new LoggableStatement(conn, strQuery.toString());
				pstmt.setString(1, SysCd);
				pstmt.setString(2, UserId);
				////ecamsLogger.error(((LoggableStatement)pstmt).getQueryString());
				rs = pstmt.executeQuery();
				if (rs.next()) {
					strDevHome = rs.getString("cd_devhome") + "\\";
				}
				rs.close();
				pstmt.close();
				
				svrList = sysinfo.getHomePath_conn(SysCd, conn);
			}
			
			if (!DsnCd.equals("")){
				if (DsnCd.length() == 7 && DsnCd.indexOf("/") < 0){
					szDsnCD = DsnCd;
					/*
					szDsnCD = getDsnCD(SysCd,DsnCd);
					if (szDsnCD.equals("")){
						return rtList.toArray();
					}*/
				}
			}
			strQuery.setLength(0);
			strQuery.append("select cm_rsrccd from cmm0036                     \n"); 
			strQuery.append(" where cm_syscd=? and cm_closedt is null          \n");
			strQuery.append("   and substr(cm_info, 2, 1)='1'                  \n");
			strQuery.append("   and substr(cm_info, 26, 1)='0'                 \n");
			strQuery.append("   and cm_rsrccd not in (select cm_samersrc       \n");
			strQuery.append("                           from cmm0037           \n");
			strQuery.append("                          where cm_syscd=?)       \n");
			pstmt = conn.prepareStatement(strQuery.toString());	
			//pstmt =  new LoggableStatement(conn, strQuery.toString());			
            pstmt.setString(1, SysCd);			
            pstmt.setString(2, SysCd);
            
            ////ecamsLogger.error(((LoggableStatement)pstmt).getQueryString());
            rs = pstmt.executeQuery();
            while (rs.next()) {
            	if (strRsrcCd != "") strRsrcCd = strRsrcCd + ",";
            	strRsrcCd = strRsrcCd + rs.getString("cm_rsrccd");            	
            }
			rs.close();
			pstmt.close();
			
			strRsrc = strRsrcCd.split(",");
			strQuery.setLength(0);
			strQuery.append("select /*+ ALL ROWS */                                    								\n");
			strQuery.append("       a.CR_RSRCNAME,a.CR_STORY,a.CR_LSTVER,a.cr_acptno,  								\n");
			strQuery.append("       TO_CHAR(a.CR_LASTDATE,'yyyy-mm-dd') as LASTDT,     								\n");
			strQuery.append("       a.CR_SYSCD,a.CR_RSRCCD,a.CR_LANGCD,a.CR_DSNCD,     								\n");
			strQuery.append("       a.CR_JOBCD,a.CR_FILESIZE,a.CR_FILEDATE,a.cr_nomodify,							\n");
			strQuery.append("       a.CR_ITEMID,a.CR_STATUS,c.CM_CODENAME as CODENAME, 								\n");
			strQuery.append("       d.CM_USERNAME,e.CM_DIRPATH, f.CM_CODENAME as JAWON,								\n");
			strQuery.append("       b.CM_INFO,g.cm_jobname,a.cr_orderid,                							\n");
			strQuery.append("       REPLACE(e.CM_DIRPATH, (SELECT CM_VOLPATH FROM CMM0038  							\n");
			strQuery.append("       WHERE CM_SYSCD = A.CR_SYSCD AND CM_RSRCCD = A.CR_RSRCCD AND CM_SVRCD = (select cm_dirbase from cmm0030 where cm_syscd = A.cr_syscd)),	\n");
			strQuery.append("       (SELECT CM_VOLPATH FROM CMM0038 WHERE CM_SYSCD = A.CR_SYSCD 					\n");
			strQuery.append("       AND CM_RSRCCD = A.CR_RSRCCD AND CM_SVRCD = '03')) as cm_dirpath2				\n");

			strQuery.append("  from cmm0020 f,cmm0070 e,cmm0040 d,cmm0020 c,cmm0036 b, \n"); 
			strQuery.append("       cmr0020 a ,cmm0102 g "); 
			if (!adminYn) {
			   strQuery.append(",cmm0044 h ");   
			}
			strQuery.append("\n where a.cr_syscd = ? and                                  \n");
            if (!RsrcCd.equals("")){
				strQuery.append(" a.cr_rsrccd=? and                                    \n");
			}
            if (!JobCd.equals("0000")){
				strQuery.append(" (a.cr_jobcd=? or substr(b.cm_info,43,1)='1') and     \n");
			}
			if (!adminYn) {
				strQuery.append("a.cr_syscd=h.cm_syscd and a.cr_jobcd=h.cm_jobcd and   \n");
				strQuery.append("h.cm_closedt is null and h.cm_userid=? and            \n");
			}
			if (selfSw == true) {
				strQuery.append("nvl(a.cr_lstusr,a.cr_editor)=?   and                  \n");
			}
			if (!RsrcName.equals("")){
				if (!RsrcName.equals("*")){
					if (RsrcName.indexOf(",") >= 0){
						strQuery.append(" a.cr_dsncd= ? and \n");
						if(LikeSw==true){
							if (UpLowSw == true) strQuery.append(" a.cr_exename like ? and \n");
							else strQuery.append(" upper(a.cr_exename) like upper(?) and   \n");
						}else{
							if (UpLowSw == true) strQuery.append(" a.cr_rsrcname = ? and \n");
							else strQuery.append(" upper(a.cr_exename) = upper(?) and   \n");
						}
					}
					else{
						if(LikeSw==true){
							if (UpLowSw == true) strQuery.append(" (a.cr_exename like ? or e.cm_dirpath like ?) and \n");
							else strQuery.append(" (upper(a.cr_exename) like upper(?) or upper(e.cm_dirpath) like upper(?)) and   \n");
						}else{
							if (UpLowSw == true) strQuery.append(" (a.cr_exename = ? or e.cm_dirpath=?) and \n");
							else strQuery.append(" (upper(a.cr_exename) = upper(?) or upper(e.cm_dirpath) = upper(?))  and   \n");
						}
					}
				}
			}
			strQuery.append("a.cr_syscd=e.cm_syscd and a.cr_dsncd=e.cm_dsncd and        \n");			
			if (!DsnCd.equals("")){
				if (!DsnCd.substring(0, 1).equals("F")){
					strQuery.append(" a.cr_dsncd= ? and                           \n");
				}
				else{
					strQuery.append(" e.cm_dirpath like ? and                     \n");
				}
			}
			if (ReqCd.equals("03")){
				strQuery.append(" a.cr_status='3' and                             \n");
				strQuery.append(" to_number(a.cr_lstver)=0  and                   \n");
			}
			else if (ReqCd.equals("01") || ReqCd.equals("02") || ReqCd.equals("08")){
			/*	strQuery.append(" substr(b.cm_info,2,1)='1' and substr(b.cm_info,26,1)='0' and \n");				
				strQuery.append(" b.cm_closedt is null and                                    \n");
				strQuery.append(" b.cm_rsrccd not in (select cm_samersrc from cmm0037         \n");
				strQuery.append("                      where cm_syscd=? and cm_factcd='04')   \n"); */
				if (RsrcCd.equals("") || RsrcCd == null) {
					strQuery.append("a.cr_rsrccd in (");
					for (i=0;strRsrc.length>i;i++) {
						if (i>0) strQuery.append(", ? ");
						else strQuery.append("? ");
					}
					strQuery.append(") and                                             \n");
				}
				strQuery.append("a.cr_status not in('9') and                           \n");
				/*if (ReqCd.equals("01") || ReqCd.equals("08")){
					strQuery.append(" to_number(a.cr_lstver)>0  and \n");
				}
				else */
				if (ReqCd.equals("02")){
					strQuery.append(" to_number(a.cr_lstver)>1  and                     \n");					
				}
			}
			strQuery.append("a.cr_editor=d.cm_userid and a.cr_jobcd = g.cm_jobcd and    \n");
			strQuery.append("c.cm_macode='CMR0020' and a.cr_status=c.cm_micode and      \n");
			strQuery.append("f.cm_macode='JAWON' and a.cr_rsrccd=f.cm_micode and        \n");
			strQuery.append("a.cr_syscd=b.cm_syscd and a.cr_rsrccd=b.cm_rsrccd          \n");			
			
            pstmt = conn.prepareStatement(strQuery.toString());	
			pstmt =  new LoggableStatement(conn, strQuery.toString());
            
            pstmt.setString(pstmtcount++, SysCd);
            if (!RsrcCd.equals("")) pstmt.setString(pstmtcount++, RsrcCd);
            if (!JobCd.equals("0000")) pstmt.setString(pstmtcount++, JobCd);
            if (adminYn == false) pstmt.setString(pstmtcount++, UserId);
			if (selfSw == true) {
				pstmt.setString(pstmtcount++, UserId);
			}
			if (!RsrcName.equals("")){
				if (!RsrcName.equals("*")){
					if (RsrcName.indexOf(",") >= 0){
						pstmt.setString(pstmtcount++, RsrcName.substring(0, 5));
						pstmt.setString(pstmtcount++, RsrcName.substring(6));
					}
					else{
						if(LikeSw==true){
							pstmt.setString(pstmtcount++, "%"+RsrcName.replaceAll("[*]","")+"%");
							pstmt.setString(pstmtcount++, "%"+RsrcName.replaceAll("[*]","")+"%");
						}else{
							pstmt.setString(pstmtcount++, RsrcName.replaceAll("[*]",""));
							pstmt.setString(pstmtcount++, RsrcName.replaceAll("[*]",""));
						}
					}
				}
			}
			if (!DsnCd.equals("")){
				if (!DsnCd.substring(0, 1).equals("F")){
					pstmt.setString(pstmtcount++, szDsnCD);
				}
				else{
					pstmt.setString(pstmtcount++, DsnCd.substring(1)+ "%");
				}
			}

            if (ReqCd.equals("01") || ReqCd.equals("02") || ReqCd.equals("08")) {
            	if (RsrcCd.equals("") || RsrcCd == null) {
	            	for (i=0;strRsrc.length>i;i++) {
	            		pstmt.setString(pstmtcount++, strRsrc[i]);
	            	}
            	}
            }
            
            ecamsLogger.error(((LoggableStatement)pstmt).getQueryString());
            rs = pstmt.executeQuery();
            
            rtList.clear();
            
			while (rs.next()){
				
				rst = new HashMap<String,String>();
				rst.put("ID", Integer.toString(rs.getRow()));
				rst.put("cm_dirpath",rs.getString("cm_dirpath"));
				rst.put("cm_dirpath2",rs.getString("cm_dirpath2"));
				rst.put("cr_rsrcname",rs.getString("cr_rsrcname"));
				/*
				rst.put("cr_story",rs.getString("cr_story"));
				*/
				rst.put("jobname", rs.getString("cm_jobname"));
				rst.put("jawon",rs.getString("jawon"));
				if (ReqCd.equals("02")){
					rst.put("cr_lstver","sel");
				}
				else{
					rst.put("cr_lstver",rs.getString("cr_lstver"));
				}
				rst.put("cm_username",rs.getString("cm_username"));
				rst.put("lastdt",rs.getString("lastdt"));
				//rst.put("codename",rs.getString("codename"));
				rst.put("sysgb",SysGb);
				rst.put("cr_syscd",rs.getString("cr_syscd"));
				rst.put("cr_rsrccd",rs.getString("cr_rsrccd"));
				rst.put("cr_langcd",rs.getString("cr_langcd"));
				rst.put("cr_dsncd",rs.getString("cr_dsncd"));
				rst.put("cr_jobcd",rs.getString("cr_jobcd"));
				/*
				rst.put("cr_filesize",rs.getString("cr_filesize"));
				rst.put("cr_filedate",rs.getString("cr_filedate"));
				*/
				rst.put("cr_itemid",rs.getString("cr_itemid"));
				rst.put("cr_status",rs.getString("cr_status"));
				rst.put("selAcptno",rs.getString("cr_acptno"));
				rst.put("cr_orderid",rs.getString("cr_orderid"));
				if (rs.getString("cr_nomodify") != null)
					rst.put("cr_nomodify",rs.getString("cr_nomodify"));
				else
					rst.put("cr_nomodify","0");
					rst.put("cm_info",rs.getString("cm_info"));
					rst.put("selected_flag","0");
				
				if ((rs.getString("cm_info").substring(44,45).equals("1")  || 
				     rs.getString("cm_info").substring(37,38).equals("1")) && strDevHome != null) {
					for (i=0;svrList.size()>i;i++) {
						if (svrList.get(i).get("cm_rsrccd").equals(rs.getString("cr_rsrccd"))) {
							strVolPath = svrList.get(i).get("cm_volpath");
							strDirPath = rs.getString("cm_dirpath");
							if (strVolPath != null && strVolPath != "") {
								if(strDirPath.length() >= strVolPath.length()){
									if (strDirPath.substring(0,strVolPath.length()).equals(strVolPath)) {
										rst.put("pcdir", strDevHome + strDirPath.substring(strVolPath.length()).replace("/","\\"));
									}
								}
							} else {
								rst.put("pcdir", strDevHome + rs.getString("cm_dirpath").replace("/", "\\"));
							}
						}
					}
					rst.put("pcdir1", rst.get("pcdir"));
				}
				//==========================================================================
				if(rs.getString("cm_info").substring(57,58).equals("1")){
	            	strQuery.setLength(0);
					strQuery.append("select a.CR_STATUS, c.CM_CODENAME 					\n");
					strQuery.append("from cmr0020 a, cmm0070 b, cmm0020 c				\n");
					strQuery.append("where a.CR_syscd = ?								\n");
					strQuery.append("and b.cm_dirpath = ?								\n");
					strQuery.append("and a.cr_rsrcname = ?								\n");
					strQuery.append("and a.CR_syscd = b.cm_syscd						\n");
					strQuery.append("and a.cr_dsncd = b.cm_dsncd						\n");
					strQuery.append("and a.cr_status = c.CM_MICODE						\n");
					strQuery.append("and c.cm_macode = 'CMR0020'						\n");
					
					pstmt2 = connD.prepareStatement(strQuery.toString());	
					pstmt2 = new LoggableStatement(connD,strQuery.toString());
					pstmt2.setString(1, rs.getString("CR_syscd"));
					pstmt2.setString(2, rs.getString("cm_dirpath"));
					pstmt2.setString(3, rs.getString("cr_rsrcname"));
					
					ecamsLogger.error(((LoggableStatement)pstmt2).getQueryString());
					rs2 = pstmt2.executeQuery();
	            	//==========================================================================
	            	//==========================================================================				
					if(rs.getString("cr_status").equals("0")){
						if(rs2.next()) {//rs2.next()가 있으면
							if(rs2.getString("cr_status").equals(rs.getString("cr_status"))){
								rst.put("unyoung", "0");
								rst.put("codename",rs.getString("codename"));
							}else{
								rst.put("unyoung", "1");
								rst.put("codename",rs.getString("codename") +"["+rs2.getString("CM_CODENAME")+"]");
							}
						}else{
							rst.put("unyoung", "1");
							rst.put("codename",rs.getString("codename") + "[미등록]");
						}
					}
					else{
						rst.put("codename",rs.getString("codename"));
					}
					rs2.close();
					pstmt2.close();
				}else{
					rst.put("codename",rs.getString("codename"));
				}
            	//==========================================================================
				rtList.add(rst);
				rst = null;

			}//end of while-loop statement
			
			rs.close();
			//rs2.close();
			pstmt.close();
			//pstmt2.close();
			conn.close();
			connD.close();
			
			rs = null;
			rs2 = null;
			pstmt = null;
			pstmt2 = null;
			conn = null;
			
			rtObj =  rtList.toArray();
			rtList = null;
			
			
			return rtObj;
			
		} catch (SQLException sqlexception) {
			sqlexception.printStackTrace();
			ecamsLogger.error("## Cmr0100.getFileList() SQLException START ##");
			ecamsLogger.error("## Error DESC : ", sqlexception);	
			ecamsLogger.error("## Cmr0100.getFileList() SQLException END ##");			
			throw sqlexception;
		} catch (Exception exception) {
			exception.printStackTrace();
			ecamsLogger.error("## Cmr0100.getFileList() Exception START ##");				
			ecamsLogger.error("## Error DESC : ", exception);	
			ecamsLogger.error("## Cmr0100.getFileList() Exception END ##");				
			throw exception;
		}finally{
			if (strQuery != null) 	strQuery = null;
			if (rtObj != null)  rtObj = null;
			if (rs != null)     try{rs.close();}catch (Exception ex){ex.printStackTrace();}
			if (pstmt != null)  try{pstmt.close();}catch (Exception ex2){ex2.printStackTrace();}
			if (conn != null){
				try{
					ConnectionResource.release(conn);
				}catch(Exception ex3){
					ecamsLogger.error("## Cmr0100.getFileList() connection release exception ##");
					ex3.printStackTrace();
				}
			}
		}
	}
	
	public Object[] getDelFileList(String UserId,String SysCd,String SysGb,String DsnCd,String RsrcCd,String RsrcName,String ReqCd,
			String JobCd,boolean UpLowSw,boolean selfSw,boolean LikeSw) throws SQLException, Exception {
		Connection        conn        = null;
		PreparedStatement pstmt       = null;
		ResultSet         rs          = null;
		String            strDevHome  = null;
		StringBuffer      strQuery    = new StringBuffer();
		Object[]		  rtObj		  = null;
		ArrayList<HashMap<String, String>>		  svrList	  = new ArrayList<HashMap<String, String>>();
		ArrayList<HashMap<String, String>>		  rtList	  = new ArrayList<HashMap<String, String>>();
		HashMap<String, String>			  rst		  = null;
		
		ConnectionContext connectionContext = new ConnectionResource();

		try {
			conn = connectionContext.getConnection();

			int				  pstmtcount  = 1;
			String			 szDsnCD = "";
			String			  strRsrcCd = "";
			String            strRsrc[] = null;
			String            strVolPath = "";
			String            strDirPath = "";
			int               i = 0;
			
			//UserInfo     userinfo = new UserInfo();
			boolean adminYn = false;//userinfo.isAdmin(UserId);
			//userinfo = null;
			
			SysInfo sysinfo = new SysInfo();
			boolean localSw = sysinfo.getLocalYn(SysCd);
			
			if (localSw) {
				strQuery.setLength(0);
				strQuery.append("select cd_devhome from cmd0200               \n");
				strQuery.append(" where cd_syscd=? and cd_userid=?            \n");
				pstmt = conn.prepareStatement(strQuery.toString());
				//pstmt =  new LoggableStatement(conn, strQuery.toString());
				pstmt.setString(1, SysCd);
				pstmt.setString(2, UserId);
				////ecamsLogger.error(((LoggableStatement)pstmt).getQueryString());
				rs = pstmt.executeQuery();
				if (rs.next()) {
					strDevHome = rs.getString("cd_devhome") + "\\";
				}
				rs.close();
				pstmt.close();
				
				svrList = sysinfo.getHomePath_conn(SysCd, conn);
			}
			
			if (!DsnCd.equals("")){
				if (DsnCd.length() == 7 && DsnCd.indexOf("/") < 0){
					szDsnCD = DsnCd;
					/*
					szDsnCD = getDsnCD(SysCd,DsnCd);
					if (szDsnCD.equals("")){
						return rtList.toArray();
					}*/
				}
			}
			strQuery.setLength(0);
			strQuery.append("select cm_rsrccd from cmm0036                     \n"); 
			strQuery.append(" where cm_syscd=? and cm_closedt is null          \n");
			strQuery.append("   and substr(cm_info, 2, 1)='1'                  \n");
			strQuery.append("   and substr(cm_info, 26, 1)='0'                 \n");
			strQuery.append("   and cm_rsrccd not in (select cm_samersrc       \n");
			strQuery.append("                           from cmm0037           \n");
			strQuery.append("                          where cm_syscd=?)       \n");
			pstmt = conn.prepareStatement(strQuery.toString());	
			//pstmt =  new LoggableStatement(conn, strQuery.toString());			
            pstmt.setString(1, SysCd);			
            pstmt.setString(2, SysCd);
            
            ////ecamsLogger.error(((LoggableStatement)pstmt).getQueryString());
            rs = pstmt.executeQuery();
            while (rs.next()) {
            	if (strRsrcCd != "") strRsrcCd = strRsrcCd + ",";
            	strRsrcCd = strRsrcCd + rs.getString("cm_rsrccd");            	
            }
			rs.close();
			pstmt.close();
			
			strRsrc = strRsrcCd.split(",");
			strQuery.setLength(0);
			strQuery.append("select /*+ ALL ROWS */                                    								\n");
			strQuery.append("       a.CR_RSRCNAME,a.CR_STORY,a.CR_LSTVER,a.cr_acptno,  								\n");
			strQuery.append("       TO_CHAR(a.CR_LASTDATE,'yyyy-mm-dd') as LASTDT,     								\n");
			strQuery.append("       a.CR_SYSCD,a.CR_RSRCCD,a.CR_LANGCD,a.CR_DSNCD,     								\n");
			strQuery.append("       a.CR_JOBCD,a.CR_FILESIZE,a.CR_FILEDATE,a.cr_nomodify,							\n");
			strQuery.append("       a.CR_ITEMID,a.CR_STATUS,c.CM_CODENAME as CODENAME, 								\n");
			strQuery.append("       d.CM_USERNAME,e.CM_DIRPATH, f.CM_CODENAME as JAWON,								\n");
			strQuery.append("       b.CM_INFO,g.cm_jobname,a.cr_orderid,                							\n");
			strQuery.append("       REPLACE(e.CM_DIRPATH, (SELECT CM_VOLPATH FROM CMM0038  							\n");
			strQuery.append("       WHERE CM_SYSCD = A.CR_SYSCD AND CM_RSRCCD = A.CR_RSRCCD AND CM_SVRCD = (select cm_dirbase from cmm0030 where cm_syscd = A.cr_syscd)),	\n");
			strQuery.append("       (SELECT CM_VOLPATH FROM CMM0038 WHERE CM_SYSCD = A.CR_SYSCD 					\n");
			strQuery.append("       AND CM_RSRCCD = A.CR_RSRCCD AND CM_SVRCD = '03')) as cm_dirpath2				\n");

			strQuery.append("  from cmm0020 f,cmm0070 e,cmm0040 d,cmm0020 c,cmm0036 b, \n"); 
			strQuery.append("       cmr0020 a ,cmm0102 g "); 
			if (!adminYn) {
			   strQuery.append(",cmm0044 h ");   
			}
			strQuery.append("\n where a.cr_syscd = ? and                                  \n");
            if (!RsrcCd.equals("")){
				strQuery.append(" a.cr_rsrccd=? and                                    \n");
			}
            if (!JobCd.equals("0000")){
				strQuery.append(" (a.cr_jobcd=? or substr(b.cm_info,43,1)='1') and     \n");
			}
			if (!adminYn) {
				strQuery.append("a.cr_syscd=h.cm_syscd and a.cr_jobcd=h.cm_jobcd and   \n");
				strQuery.append("h.cm_closedt is null and h.cm_userid=? and            \n");
			}
			if (selfSw == true) {
				strQuery.append("nvl(a.cr_lstusr,a.cr_editor)=?   and                  \n");
			}
			if (!RsrcName.equals("")){
				if (!RsrcName.equals("*")){
					if (RsrcName.indexOf(",") >= 0){
						strQuery.append(" a.cr_dsncd= ? and \n");
						if(LikeSw==true){
							if (UpLowSw == true) strQuery.append(" a.cr_rsrcname like ? and \n");
							else strQuery.append(" upper(a.cr_rsrcname) like upper(?) and   \n");
						}else{
							if (UpLowSw == true) strQuery.append(" a.cr_rsrcname = ? and \n");
							else strQuery.append(" upper(a.cr_rsrcname) = upper(?) and   \n");
						}
					}
					else{
						if(LikeSw==true){
							if (UpLowSw == true) strQuery.append(" a.cr_rsrcname like ? and \n");
							else strQuery.append(" upper(a.cr_rsrcname) like upper(?) and   \n");
						}else{
							if (UpLowSw == true) strQuery.append(" a.cr_rsrcname = ? and \n");
							else strQuery.append(" upper(a.cr_rsrcname) = upper(?) and   \n");
						}
					}
				}
			}
			strQuery.append("a.cr_syscd=e.cm_syscd and a.cr_dsncd=e.cm_dsncd and        \n");			
			if (!DsnCd.equals("")){
				if (!DsnCd.substring(0, 1).equals("F")){
					strQuery.append(" a.cr_dsncd= ? and                           \n");
				}
				else{
					strQuery.append(" e.cm_dirpath like ? and                     \n");
				}
			}
			if (ReqCd.equals("03")){
				strQuery.append(" a.cr_status='3' and                             \n");
				strQuery.append(" to_number(a.cr_lstver)=0  and                   \n");
			}
			else if (ReqCd.equals("01") || ReqCd.equals("02") || ReqCd.equals("08")){
			/*	strQuery.append(" substr(b.cm_info,2,1)='1' and substr(b.cm_info,26,1)='0' and \n");				
				strQuery.append(" b.cm_closedt is null and                                    \n");
				strQuery.append(" b.cm_rsrccd not in (select cm_samersrc from cmm0037         \n");
				strQuery.append("                      where cm_syscd=? and cm_factcd='04')   \n"); */
				if ("".equals(RsrcCd) || RsrcCd == null) {
					strQuery.append("a.cr_rsrccd in (");
					for (i=0;strRsrc.length>i;i++) {
						if (i>0) strQuery.append(", ? ");
						else strQuery.append("? ");
					}
					strQuery.append(") and                                             \n");
				}
				//strQuery.append("a.cr_status not in('9') and                           \n");
				/*if (ReqCd.equals("01") || ReqCd.equals("08")){
					strQuery.append(" to_number(a.cr_lstver)>0  and \n");
				}
				else */
				if (ReqCd.equals("02")){
					strQuery.append(" to_number(a.cr_lstver)>1  and                     \n");					
				}
			}
			strQuery.append("a.cr_editor=d.cm_userid and a.cr_jobcd = g.cm_jobcd and    \n");
			strQuery.append("c.cm_macode='CMR0020' and a.cr_status=c.cm_micode and      \n");
			strQuery.append("f.cm_macode='JAWON' and a.cr_rsrccd=f.cm_micode and        \n");
			strQuery.append("a.cr_syscd=b.cm_syscd and a.cr_rsrccd=b.cm_rsrccd          \n");			
			
            pstmt = conn.prepareStatement(strQuery.toString());	
			pstmt =  new LoggableStatement(conn, strQuery.toString());
            
            pstmt.setString(pstmtcount++, SysCd);
            if (!RsrcCd.equals("")) pstmt.setString(pstmtcount++, RsrcCd);
            if (!JobCd.equals("0000")) pstmt.setString(pstmtcount++, JobCd);
            if (adminYn == false) pstmt.setString(pstmtcount++, UserId);
			if (selfSw == true) {
				pstmt.setString(pstmtcount++, UserId);
			}
			if (!RsrcName.equals("")){
				if (!RsrcName.equals("*")){
					if (RsrcName.indexOf(",") >= 0){
						pstmt.setString(pstmtcount++, RsrcName.substring(0, 5));
						pstmt.setString(pstmtcount++, RsrcName.substring(6));
					}
					else{
						if(LikeSw==true){
							pstmt.setString(pstmtcount++, "%"+RsrcName.replaceAll("[*]","")+"%");
						}else{
							pstmt.setString(pstmtcount++, RsrcName.replaceAll("[*]",""));
						}
					}
				}
			}
			if (!DsnCd.equals("")){
				if (!DsnCd.substring(0, 1).equals("F")){
					pstmt.setString(pstmtcount++, szDsnCD);
				}
				else{
					pstmt.setString(pstmtcount++, DsnCd.substring(1)+ "%");
				}
			}

            if (ReqCd.equals("01") || ReqCd.equals("02") || ReqCd.equals("08")) {
            	if ("".equals(RsrcCd) || RsrcCd == null) {
	            	for (i=0;strRsrc.length>i;i++) {
	            		pstmt.setString(pstmtcount++, strRsrc[i]);
	            	}
            	}
            }
            
            ecamsLogger.error(((LoggableStatement)pstmt).getQueryString());
            rs = pstmt.executeQuery();
            
            rtList.clear();
			while (rs.next()){
				rst = new HashMap<String,String>();
				rst.put("ID", Integer.toString(rs.getRow()));
				rst.put("cm_dirpath",rs.getString("cm_dirpath"));
				rst.put("cm_dirpath2",rs.getString("cm_dirpath2"));
				rst.put("cr_rsrcname",rs.getString("cr_rsrcname"));
				/*
				rst.put("cr_story",rs.getString("cr_story"));
				*/
				rst.put("jobname", rs.getString("cm_jobname"));
				rst.put("jawon",rs.getString("jawon"));
				if (ReqCd.equals("02")){
					rst.put("cr_lstver","sel");
				}
				else{
					rst.put("cr_lstver",rs.getString("cr_lstver"));
				}
				rst.put("cm_username",rs.getString("cm_username"));
				rst.put("lastdt",rs.getString("lastdt"));
				rst.put("codename",rs.getString("codename"));
				rst.put("sysgb",SysGb);
				rst.put("cr_syscd",rs.getString("cr_syscd"));
				rst.put("cr_rsrccd",rs.getString("cr_rsrccd"));
				rst.put("cr_langcd",rs.getString("cr_langcd"));
				rst.put("cr_dsncd",rs.getString("cr_dsncd"));
				rst.put("cr_jobcd",rs.getString("cr_jobcd"));
				/*
				rst.put("cr_filesize",rs.getString("cr_filesize"));
				rst.put("cr_filedate",rs.getString("cr_filedate"));
				*/
				rst.put("cr_itemid",rs.getString("cr_itemid"));
				rst.put("cr_status",rs.getString("cr_status"));
				rst.put("selAcptno",rs.getString("cr_acptno"));
				rst.put("cr_orderid",rs.getString("cr_orderid"));
				if (rs.getString("cr_nomodify") != null)
					rst.put("cr_nomodify",rs.getString("cr_nomodify"));
				else
					rst.put("cr_nomodify","0");
					rst.put("cm_info",rs.getString("cm_info"));
					rst.put("selected_flag","0");
				
				if ((rs.getString("cm_info").substring(44,45).equals("1")  || 
				     rs.getString("cm_info").substring(37,38).equals("1")) && strDevHome != null) {
					for (i=0;svrList.size()>i;i++) {
						if (svrList.get(i).get("cm_rsrccd").equals(rs.getString("cr_rsrccd"))) {
							strVolPath = svrList.get(i).get("cm_volpath");
							strDirPath = rs.getString("cm_dirpath");
							if (strVolPath != null && !"".equals(strVolPath)) {
								if(strDirPath.length() >= strVolPath.length()){
									if (strDirPath.substring(0,strVolPath.length()).equals(strVolPath)) {
										rst.put("pcdir", strDevHome + strDirPath.substring(strVolPath.length()).replace("/","\\"));
									}
								}
							} else {
								rst.put("pcdir", strDevHome + rs.getString("cm_dirpath").replace("/", "\\"));
							}
						}
					}
					rst.put("pcdir1", rst.get("pcdir"));
				}
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
			ecamsLogger.error("## Cmr0100.getFileList() SQLException START ##");
			ecamsLogger.error("## Error DESC : ", sqlexception);	
			ecamsLogger.error("## Cmr0100.getFileList() SQLException END ##");			
			throw sqlexception;
		} catch (Exception exception) {
			exception.printStackTrace();
			ecamsLogger.error("## Cmr0100.getFileList() Exception START ##");				
			ecamsLogger.error("## Error DESC : ", exception);	
			ecamsLogger.error("## Cmr0100.getFileList() Exception END ##");				
			throw exception;
		}finally{
			if (strQuery != null) 	strQuery = null;
			if (rtObj != null)  rtObj = null;
			if (rs != null)     try{rs.close();}catch (Exception ex){ex.printStackTrace();}
			if (pstmt != null)  try{pstmt.close();}catch (Exception ex2){ex2.printStackTrace();}
			if (conn != null){
				try{
					ConnectionResource.release(conn);
				}catch(Exception ex3){
					ecamsLogger.error("## Cmr0100.getFileList() connection release exception ##");
					ex3.printStackTrace();
				}
			}
		}
	}
	
//	public Object[] getFileList_excel(ArrayList<HashMap<String,String>> fileList,String SysCd,String SysGb,String ReqCd) throws SQLException, Exception {
//		Connection        conn        = null;
//		PreparedStatement pstmt       = null;
//		ResultSet         rs          = null;
//		Connection        connD        = null;
//		PreparedStatement pstmt2       = null;
//		ResultSet         rs2          = null;
//		String            strDevHome  = null;
//		StringBuffer      strQuery    = new StringBuffer();
//		Object[]		  rtObj		  = null;
//		ArrayList<HashMap<String, String>>		  rtList	  = new ArrayList<HashMap<String, String>>();
//		HashMap<String, String>			  rst		  = null;
//		ConnectionContext connectionContext = new ConnectionResource();
//		ConnectionContext connectionContextD = new ConnectionResource(false,"D");
//		int				  pstmtcount  = 1;
//		int				  filecnt = 0;
//		int				  cmr0020cnt = 0;
//
//		try {
//			conn = connectionContext.getConnection();
//			connD = connectionContextD.getConnection();
//			rtList.clear();
//			for (int i=1;i<fileList.size();i++){
//				/*
//				strQuery.setLength(0);
//				pstmtcount = 1;
//				strQuery.append("select count(a.cr_itemid) as cnt \n");
//				strQuery.append("from cmr0020 a,cmm0036 b \n"); 
//				strQuery.append("where a.cr_syscd = ? and \n");
//				strQuery.append(" a.cr_rsrcname= ? and \n");
//				strQuery.append(" a.cr_syscd = b.cm_syscd and \n");
//				strQuery.append(" a.cr_rsrccd = b.cm_rsrccd and \n");
//				if (ReqCd.equals("03")){
//					strQuery.append(" a.cr_status='3' and \n");
//					strQuery.append(" to_number(a.cr_lstver)=0 \n");
//				}
//				else if (ReqCd.equals("01") || ReqCd.equals("02") || ReqCd.equals("08")){
//					strQuery.append(" substr(b.cm_info,2,1)='1' and substr(b.cm_info,26,1)='0' and \n");
//					strQuery.append(" b.cm_closedt is null and \n");
//					strQuery.append(" a.cr_status not in('9') \n");
//					if (ReqCd.equals("01") || ReqCd.equals("08")){
//						strQuery.append("and to_number(a.cr_lstver)>0  \n");
//					}
//					else if (ReqCd.equals("02")){
//						strQuery.append("and to_number(a.cr_lstver)>1  \n");					
//					}
//				}
//				
//	            pstmt = conn.prepareStatement(strQuery.toString());	
//				//pstmt =  new LoggableStatement(conn, strQuery.toString());
//				
//	            pstmt.setString(pstmtcount++, SysCd);
//	            pstmt.setString(pstmtcount++, fileList.get(i).get("cr_rsrcname"));
//	
//	            ////ecamsLogger.error(((LoggableStatement)pstmt).getQueryString());
//				
//	            rs = pstmt.executeQuery();
//            
//				while (rs.next()){
//					cmr0020cnt = rs.getInt("cnt");
//				}
//				
//				if (cmr0020cnt != 1){
//					rst = new HashMap<String,String>();
//					rst.put("linenum",Integer.toString(i));
//					rst.put("cr_rsrcname",fileList.get(i).get("cr_rsrcname"));
//					rst.put("cmr0020cnt",Integer.toString(cmr0020cnt));
//					rtList.add(rst);
//					rst = null;
//					rs.close();
//					pstmt.close();
//					continue;
//				}
//				
//				rs.close();
//				pstmt.close();
//				*/
//				
//				strQuery.setLength(0);
//				pstmtcount = 1;
//				strQuery.append("select a.CR_RSRCNAME,a.CR_STORY,a.CR_LSTVER,         \n");
//				strQuery.append("       TO_CHAR(a.CR_LASTDATE,'yyyy-mm-dd') as LASTDT,\n");
//				strQuery.append("       a.cr_nomodify,a.CR_SYSCD,a.CR_RSRCCD,         \n");
//				strQuery.append("       a.CR_DSNCD,a.CR_JOBCD,            \n");
//				strQuery.append("       a.CR_FILESIZE,a.CR_FILEDATE,a.CR_ITEMID,      \n");
//				strQuery.append("       a.CR_STATUS,c.CM_CODENAME as CODENAME,        \n");
//				strQuery.append("       d.CM_USERNAME,e.CM_DIRPATH,f.CM_CODENAME as JAWON,\n");
//				strQuery.append("       b.CM_INFO,g.cm_jobname           \n");
//				strQuery.append("  from cmm0020 f,cmm0070 e,cmm0040 d,cmm0020 c,      \n"); 
//				strQuery.append("       cmm0036 b,cmr0020 a ,cmm0102 g                \n"); 
//				strQuery.append("where a.cr_syscd = ? and \n");
//				strQuery.append(" a.cr_rsrcname= ? and \n");
//	
//				if (ReqCd.equals("03")){
//					strQuery.append(" a.cr_status='3' and \n");
//					strQuery.append(" to_number(a.cr_lstver)=0  and \n");
//				}
//				else if (ReqCd.equals("01") || ReqCd.equals("02") || ReqCd.equals("08")){
//					strQuery.append(" substr(b.cm_info,2,1)='1' and substr(b.cm_info,26,1)='0' and \n");
//					strQuery.append(" b.cm_closedt is null and \n");
//					strQuery.append(" a.cr_status not in('C','9') and \n");
//					if (ReqCd.equals("01") || ReqCd.equals("08")){
//						strQuery.append(" to_number(a.cr_lstver)>0  and \n");
//					}
//					else if (ReqCd.equals("02")){
//						strQuery.append(" to_number(a.cr_lstver)>1  and \n");					
//					}
//				}
//				strQuery.append("a.cr_editor=d.cm_userid and a.cr_jobcd = g.cm_jobcd and \n");
//				strQuery.append("c.cm_macode='CMR0020' and a.cr_status=c.cm_micode and \n");
//				strQuery.append("f.cm_macode='JAWON' and a.cr_rsrccd=f.cm_micode and \n");
//				strQuery.append("a.cr_syscd=e.cm_syscd and a.cr_dsncd=e.cm_dsncd and \n");
//				strQuery.append("a.cr_syscd=b.cm_syscd and a.cr_rsrccd=b.cm_rsrccd   \n");	
//				strQuery.append("order by a.cr_rsrcname                              \n");		
//				
//	            pstmt = conn.prepareStatement(strQuery.toString());	
//				//pstmt =  new LoggableStatement(conn, strQuery.toString());
//				
//	            pstmt.setString(pstmtcount++, SysCd);
//	            pstmt.setString(pstmtcount++, fileList.get(i).get("cr_rsrcname"));
//	
//				////ecamsLogger.error(((LoggableStatement)pstmt).getQueryString());
//				
//	            rs = pstmt.executeQuery();
//	            
//	            cmr0020cnt = 0;
//	            
//				while (rs.next()){
//					++cmr0020cnt;
//					if (cmr0020cnt > 1) {
//						rst = new HashMap<String,String>();
//						rst = rtList.get(rtList.size()-1);
//						rst.put("errmsg", "파일명중복");	
//						rtList.set(rtList.size()-1, rst);
//					}
//					rst = new HashMap<String,String>();
//					rst.put("ID", Integer.toString(++filecnt));
//					rst.put("linenum",Integer.toString(i));
//					rst.put("cmr0020cnt","1");
//					rst.put("cm_dirpath",rs.getString("cm_dirpath"));
//					rst.put("cr_rsrcname",rs.getString("cr_rsrcname"));
//					rst.put("jobname", rs.getString("cm_jobname"));
//					rst.put("jawon",rs.getString("jawon"));
//					if (ReqCd.equals("02")){
//						rst.put("cr_lstver","sel");
//					}
//					else{
//						rst.put("cr_lstver",rs.getString("cr_lstver"));
//					}
//					rst.put("cm_username",rs.getString("cm_username"));
//					rst.put("lastdt",rs.getString("lastdt"));
//					rst.put("codename",rs.getString("codename"));
//					rst.put("sysgb",SysGb);
//					rst.put("cr_syscd",rs.getString("cr_syscd"));
//					rst.put("cr_rsrccd",rs.getString("cr_rsrccd"));
//					rst.put("cr_dsncd",rs.getString("cr_dsncd"));
//					rst.put("cr_jobcd",rs.getString("cr_jobcd"));
//					if (rs.getString("cr_nomodify") != null)
//						rst.put("cr_nomodify",rs.getString("cr_nomodify"));
//					else
//						rst.put("cr_nomodify","0");
//					rst.put("baseitemid",rs.getString("cr_itemid"));
//					rst.put("cr_itemid",rs.getString("cr_itemid"));
//					rst.put("cr_status",rs.getString("cr_status"));
//					rst.put("cm_info",rs.getString("cm_info"));
//					rst.put("selected_flag","0");
//					if (cmr0020cnt > 1) rst.put("errmsg", "파일명중복");
//					else if (rs.getString("cr_status").equals("0")){
//						rst.put("errmsg", "정상");
//					} else rst.put("errmsg",rs.getString("codename"));
//					rst.put("selected_flag","0");
//					rtList.add(rst);
//					rst = null;
//				}//end of while-loop statement
//				
//				rs.close();
//				pstmt.close();
//				
//				if (filecnt == 0) {
//					rst = new HashMap<String,String>();
//					rst.put("linenum",Integer.toString(i));
//					rst.put("cr_rsrcname",fileList.get(i).get("cr_rsrcname"));
//					rst.put("cmr0020cnt","0");
//					rst.put("errmsg", "원장없음");
//					rst.put("selected_flag","0");
//					rtList.add(rst);
//					rst = null;
//				}
//			}
//			
//			conn.close();
//			conn = null;
//			pstmt = null;
//			rs = null;
//			
//			rtObj =  rtList.toArray();
//			rtList = null;
//			
//			return rtObj;			
//			
//		} catch (SQLException sqlexception) {
//			sqlexception.printStackTrace();
//			ecamsLogger.error("## Cmr0100.getFileList() SQLException START ##");
//			ecamsLogger.error("## Error DESC : ", sqlexception);
//			ecamsLogger.error("## Cmr0100.getFileList() SQLException END ##");			
//			throw sqlexception;
//		} catch (Exception exception) {
//			exception.printStackTrace();
//			ecamsLogger.error("## Cmr0100.getFileList() Exception START ##");				
//			ecamsLogger.error("## Error DESC : ", exception);
//			ecamsLogger.error("## Cmr0100.getFileList() Exception END ##");
//			throw exception;
//		}finally{
//			if (strQuery != null) 	strQuery = null;
//			if (rtObj != null)  rtObj = null;
//			if (rs != null)     try{rs.close();}catch (Exception ex){ex.printStackTrace();}
//			if (pstmt != null)  try{pstmt.close();}catch (Exception ex2){ex2.printStackTrace();}
//			if (conn != null){
//				try{
//					ConnectionResource.release(conn);
//				}catch(Exception ex3){
//					ecamsLogger.error("## Cmr0100.getFileList() connection release exception ##");
//					ex3.printStackTrace();
//				}
//			}
//		}
//	}
	
	//20221222
	public Object[] getFileList_excel(ArrayList<HashMap<String,String>> fileList,String SysCd,String SysGb,String ReqCd, String UserId, boolean selfSw, boolean UpLowSw) throws SQLException, Exception {
		Connection        conn        = null;
		PreparedStatement pstmt       = null;
		ResultSet         rs          = null;
		Connection        connD        = null;
		PreparedStatement pstmt2       = null;
		ResultSet         rs2          = null;
		String            strDevHome  = null;
		StringBuffer      strQuery    = new StringBuffer();
		Object[]		  rtObj		  = null;
		ArrayList<HashMap<String, String>>		  rtList	  = new ArrayList<HashMap<String, String>>();
		HashMap<String, String>			  rst		  = null;
		ConnectionContext connectionContext = new ConnectionResource();
		ConnectionContext connectionContextD = new ConnectionResource(false,"D");
		int				  pstmtcount  = 1;
		int				  filecnt = 0;
		int				  cmr0020cnt = 0;

		try {
			conn = connectionContext.getConnection();
			connD = connectionContextD.getConnection();
			rtList.clear();
			for (int i=0;i<fileList.size();i++){
				strQuery.setLength(0);
				pstmtcount = 1;
				strQuery.append("select /*+ ALL ROWS */                                    								\n");
				strQuery.append("       a.CR_RSRCNAME,a.CR_STORY,a.CR_LSTVER,a.cr_acptno,  								\n");
				strQuery.append("       TO_CHAR(a.CR_LASTDATE,'yyyy-mm-dd') as LASTDT,     								\n");
				strQuery.append("       a.CR_SYSCD,a.CR_RSRCCD,a.CR_LANGCD,a.CR_DSNCD,     								\n");
				strQuery.append("       a.CR_JOBCD,a.CR_FILESIZE,a.CR_FILEDATE,a.cr_nomodify,							\n");
				strQuery.append("       a.CR_ITEMID,a.CR_STATUS,c.CM_CODENAME as CODENAME, 								\n");
				strQuery.append("       d.CM_USERNAME,e.CM_DIRPATH, f.CM_CODENAME as JAWON,								\n");
				strQuery.append("       b.CM_INFO,g.cm_jobname,a.cr_orderid,                							\n");
				strQuery.append("       REPLACE(e.CM_DIRPATH, (SELECT CM_VOLPATH FROM CMM0038  							\n");
				strQuery.append("       WHERE CM_SYSCD = A.CR_SYSCD AND CM_RSRCCD = A.CR_RSRCCD AND CM_SVRCD = (select cm_dirbase from cmm0030 where cm_syscd = A.cr_syscd)),	\n");
				strQuery.append("       (SELECT CM_VOLPATH FROM CMM0038 WHERE CM_SYSCD = A.CR_SYSCD 					\n");
				strQuery.append("       AND CM_RSRCCD = A.CR_RSRCCD AND CM_SVRCD = '03')) as cm_dirpath2				\n");

				strQuery.append("  from cmm0020 f,cmm0070 e,cmm0040 d,cmm0020 c,cmm0036 b, \n"); 
				strQuery.append("       cmr0020 a ,cmm0102 g "); 
				strQuery.append(",cmm0044 h ");   
				strQuery.append("\n where a.cr_syscd = ? and                                  \n");
				strQuery.append("a.cr_syscd=h.cm_syscd and a.cr_jobcd=h.cm_jobcd and   \n");
				strQuery.append("h.cm_closedt is null and h.cm_userid=? and            \n");
				if (UpLowSw == true) strQuery.append(" a.cr_rsrcname = ? and \n");
				else strQuery.append(" upper(a.cr_rsrcname) = upper(?) and   \n");
				strQuery.append("e.cm_dirpath=? and 						 \n");
				strQuery.append("a.cr_syscd=e.cm_syscd and a.cr_dsncd=e.cm_dsncd and        \n");	
				if (ReqCd.equals("03")){
					strQuery.append(" a.cr_status='3' and                             \n");
					strQuery.append(" to_number(a.cr_lstver)=0  and                   \n");
				}
				else if (ReqCd.equals("01") || ReqCd.equals("02") || ReqCd.equals("08")){
					strQuery.append("a.cr_status not in('9') and                           \n");
					if (ReqCd.equals("02")){
						strQuery.append(" to_number(a.cr_lstver)>1  and                     \n");					
					}
				}
				strQuery.append("a.cr_editor=d.cm_userid and a.cr_jobcd = g.cm_jobcd and    \n");
				strQuery.append("c.cm_macode='CMR0020' and a.cr_status=c.cm_micode and      \n");
				strQuery.append("f.cm_macode='JAWON' and a.cr_rsrccd=f.cm_micode and        \n");
				strQuery.append("a.cr_syscd=b.cm_syscd and a.cr_rsrccd=b.cm_rsrccd          \n");			
				
	            pstmt = conn.prepareStatement(strQuery.toString());	
				pstmt =  new LoggableStatement(conn, strQuery.toString());
	            
	            pstmt.setString(pstmtcount++, SysCd);
	            pstmt.setString(pstmtcount++, UserId);
	            pstmt.setString(pstmtcount++, fileList.get(i).get("rsrcname"));
	            pstmt.setString(pstmtcount++, fileList.get(i).get("dirpath"));
	            ecamsLogger.error(((LoggableStatement)pstmt).getQueryString());
	            rs = pstmt.executeQuery();
	            
				if (rs.next()){
					rst = new HashMap<String,String>();
					rst.put("ID", Integer.toString(rs.getRow()));
					rst.put("cm_dirpath",rs.getString("cm_dirpath"));
					rst.put("cm_dirpath2",rs.getString("cm_dirpath2"));
					rst.put("cr_rsrcname",rs.getString("cr_rsrcname"));
					/*
					rst.put("cr_story",rs.getString("cr_story"));
					*/
					rst.put("jobname", rs.getString("cm_jobname"));
					rst.put("jawon",rs.getString("jawon"));
					if (ReqCd.equals("02")){
						rst.put("cr_lstver","sel");
					}
					else{
						rst.put("cr_lstver",rs.getString("cr_lstver"));
					}
					rst.put("cm_username",rs.getString("cm_username"));
					rst.put("lastdt",rs.getString("lastdt"));
					//rst.put("codename",rs.getString("codename"));
					rst.put("sysgb",SysGb);
					rst.put("cr_syscd",rs.getString("cr_syscd"));
					rst.put("cr_rsrccd",rs.getString("cr_rsrccd"));
					rst.put("cr_langcd",rs.getString("cr_langcd"));
					rst.put("cr_dsncd",rs.getString("cr_dsncd"));
					rst.put("cr_jobcd",rs.getString("cr_jobcd"));
					/*
					rst.put("cr_filesize",rs.getString("cr_filesize"));
					rst.put("cr_filedate",rs.getString("cr_filedate"));
					*/
					rst.put("cr_itemid",rs.getString("cr_itemid"));
					rst.put("cr_status",rs.getString("cr_status"));
					rst.put("selAcptno",rs.getString("cr_acptno"));
					rst.put("cr_orderid",rs.getString("cr_orderid"));
					if (rs.getString("cr_nomodify") != null)
						rst.put("cr_nomodify",rs.getString("cr_nomodify"));
					else
						rst.put("cr_nomodify","0");
						rst.put("cm_info",rs.getString("cm_info"));
						rst.put("selected_flag","0");
			
					//==========================================================================
					if(rs.getString("cm_info").substring(57,58).equals("1")){
		            	strQuery.setLength(0);
						strQuery.append("select a.CR_STATUS, c.CM_CODENAME 					\n");
						strQuery.append("from cmr0020 a, cmm0070 b, cmm0020 c				\n");
						strQuery.append("where a.CR_syscd = ?								\n");
						strQuery.append("and b.cm_dirpath = ?								\n");
						strQuery.append("and a.cr_rsrcname = ?								\n");
						strQuery.append("and a.CR_syscd = b.cm_syscd						\n");
						strQuery.append("and a.cr_dsncd = b.cm_dsncd						\n");
						strQuery.append("and a.cr_status = c.CM_MICODE						\n");
						strQuery.append("and c.cm_macode = 'CMR0020'						\n");
						
						pstmt2 = connD.prepareStatement(strQuery.toString());	
						pstmt2 = new LoggableStatement(connD,strQuery.toString());
						pstmt2.setString(1, rs.getString("CR_syscd"));
						pstmt2.setString(2, rs.getString("cm_dirpath"));
						pstmt2.setString(3, rs.getString("cr_rsrcname"));
						
						ecamsLogger.error(((LoggableStatement)pstmt2).getQueryString());
						rs2 = pstmt2.executeQuery();
		            	//==========================================================================
		            	//==========================================================================				
						if(rs.getString("cr_status").equals("0")){
							if(rs2.next()) {//rs2.next()가 있으면
								if(rs2.getString("cr_status").equals(rs.getString("cr_status"))){
									rst.put("unyoung", "0");
									rst.put("codename",rs.getString("codename"));
								}else{
									rst.put("unyoung", "1");
									rst.put("codename",rs.getString("codename") +"["+rs2.getString("CM_CODENAME")+"]");
								}
							}else{
								rst.put("unyoung", "1");
								rst.put("codename",rs.getString("codename") + "[미등록]");
							}
						}
						else{
							rst.put("codename",rs.getString("codename"));
						}
						rs2.close();
						pstmt2.close();
					}else{
						rst.put("codename",rs.getString("codename"));
					}
	            	//==========================================================================
					rtList.add(rst);
					rst = null;
				} else {
					rst = new HashMap<String,String>();
					rst.put("cm_dirpath",fileList.get(i).get("dirpath"));
					rst.put("cm_dirpath2",fileList.get(i).get("dirpath"));
					rst.put("codename","[프로그램정보 없음]");
					rst.put("cr_lstver","sel");
					rst.put("cr_rsrcname",fileList.get(i).get("rsrcname"));
					rst.put("selected_flag","1");
					rtList.add(rst);
					rst = null;
				}
			}
			rtObj =  rtList.toArray();
			rtList = null;
			rs.close();
			//rs2.close();
			pstmt.close();
			//pstmt2.close();
			conn.close();
			connD.close();
			
			rs = null;
			rs2 = null;
			pstmt = null;
			pstmt2 = null;
			conn = null;
			return rtObj;			
			
		} catch (SQLException sqlexception) {
			sqlexception.printStackTrace();
			ecamsLogger.error("## Cmr0100.getFileList() SQLException START ##");
			ecamsLogger.error("## Error DESC : ", sqlexception);
			ecamsLogger.error("## Cmr0100.getFileList() SQLException END ##");			
			throw sqlexception;
		} catch (Exception exception) {
			exception.printStackTrace();
			ecamsLogger.error("## Cmr0100.getFileList() Exception START ##");				
			ecamsLogger.error("## Error DESC : ", exception);
			ecamsLogger.error("## Cmr0100.getFileList() Exception END ##");
			throw exception;
		}finally{
			if (strQuery != null) 	strQuery = null;
			if (rtObj != null)  rtObj = null;
			if (rs != null)     try{rs.close();}catch (Exception ex){ex.printStackTrace();}
			if (pstmt != null)  try{pstmt.close();}catch (Exception ex2){ex2.printStackTrace();}
			if (conn != null){
				try{
					ConnectionResource.release(conn);
				}catch(Exception ex3){
					ecamsLogger.error("## Cmr0100.getFileList() connection release exception ##");
					ex3.printStackTrace();
				}
			}
		}
	}
	
	public String request_Check_Out(ArrayList<HashMap<String,String>> chkOutList,HashMap<String,String> etcData, ArrayList<HashMap<String,Object>> ConfList, ArrayList<HashMap<String,String>> OrderList) throws SQLException, Exception {
		Connection        conn        = null;
		PreparedStatement pstmt       = null;
		ResultSet         rs          = null;
		ResultSet         rs2          = null;
		StringBuffer      strQuery    = new StringBuffer();
		ConnectionContext connectionContext = new ConnectionResource();
		int				  pstmtcount  = 1;
		AutoSeq			  autoseq	  = new AutoSeq();
		UserInfo		  userInfo	  = new UserInfo();
		//CodeInfo		  codeInfo	  = new CodeInfo();
		String			  AcptNo	  = "";
		String			  returnck    = "true";
		int				  i;
		int				  cnt		=0;
		String[] strAry = null;
		Runtime  run = null;
		Process p = null;
		SystemPath		  systemPath = new SystemPath();
		eCAMSInfo         ecamsinfo   = new eCAMSInfo();
		String			chgcode		="0";
		File shfile=null;
		OutputStreamWriter writer = null;
		String rtString ="";
		try {
			conn = connectionContext.getConnection();
			conn.setAutoCommit(false);
			int totcnt = 0;
			
	        for (i=0;i<chkOutList.size();i++){
	        	if (chkOutList.get(i).get("baseitemid").equals(chkOutList.get(i).get("cr_itemid"))) {
	        		++totcnt;
		        	strQuery.setLength(0);
		        	strQuery.append("select cr_status,cr_editor,cr_story,cr_rsrcname from cmr0020 \n");
		        	strQuery.append("where cr_itemid = ?  \n");
		        	strQuery.append("and cr_status <> '0' \n");
		        	
		        	pstmt = conn.prepareStatement(strQuery.toString());
		        	
		        	pstmt.setString(1, chkOutList.get(i).get("cr_itemid"));
		        	
		        	rs = pstmt.executeQuery();
		        	
		        	while (rs.next()){
		        		if(conn != null) conn.close();
	        			conn = null;
		        		throw new Exception(rs.getString("cr_rsrcname")+" 파일은 " + rs.getString("cr_editor") +"님이 Check-Out 하셨습니다.");
		        	}
		        	
		        	rs.close();
		        	pstmt.close();
	        	}
	        	
	        	strQuery.setLength(0);		        
 		        strQuery.append("select a.cm_info, b.cr_itemid, b.cr_rsrcname\n");
 		        strQuery.append("from cmm0036 a, cmr0020 b				\n");
 	        	strQuery.append(" where b.cr_itemid= ?                	\n");
 	        	strQuery.append(" and a.cm_rsrccd = b.cr_rsrccd        	\n");
 	        	strQuery.append(" and a.cm_syscd = b.cr_syscd        	\n");
 	        	
 	        	pstmt = conn.prepareStatement(strQuery.toString());
 	        	//pstmt = new LoggableStatement(conn, strQuery.toString());
 	        	pstmt.setString(1, chkOutList.get(i).get("cr_itemid"));
 	        	//ecamsLogger.error(((LoggableStatement)pstmt).getQueryString());
 	        	rs = pstmt.executeQuery();
 	        	if (rs.next()){
 	        		chgcode="0";
 	        		if(rs.getString("cm_info").substring(52, 53).equals("1")  && !chkOutList.get(i).get("cr_lstver").equals("0")){
 	        			
 	        			System.out.println("File Diff Check");
 	        			
 	        			String            makeFile    = "";
 	        			String            strBinPath    = "";
 	        			String            strFile     = "";
 	        			String            strTmpPath  = "";
 	        			
	 	        		makeFile = "filediff" + etcData.get("UserID");
	 	        		strBinPath = systemPath.getTmpDir("14");
	 	   				strFile = strBinPath + makeFile + "." + chkOutList.get(i).get("cr_syscd") + ".ih.cs";
	 	   				
	 	   				strTmpPath = ecamsinfo.getFileInfo("99");
	 	   				shfile=null;
	 	   				shfile = new File(strTmpPath + makeFile+".sh");              //File 불러온다.
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
		 				rs2 = pstmt.executeQuery();
		 				if(rs2.next()){
		 					rtString = rs2.getString("cm_ipaddr") + " " + rs2.getString("cm_port") + " 0";
		 				}
	 	   				rs2.close();
	 	   				writer = new OutputStreamWriter( new FileOutputStream(strTmpPath + makeFile+".sh"));
	 	   				writer.write("cd "+strBinPath +"\n");
//	 	   				writer.write("./ecams_diffck " + chkOutList.get(i).get("cr_itemid") + " " + etcData.get("UserID") +  " " + "PROD"+" "+"N"+"\n");
	 	   				writer.write("./ecams_batexec " +rtString + " \"cd " + strBinPath+ ";" +"./ecams_diffck " + chkOutList.get(i).get("cr_itemid") + " " + etcData.get("UserID") +  " " + "PROD"+" "+"N" + "\" \n");
	 	   				writer.write("exit $?\n");
	 	   				writer.close();
	 	   				
	 	   				strAry = new String[3]; 
	 	   				
	 	   				strAry[0] = "chmod";
	 	   				strAry[1] = "777";
	 	   				strAry[2] = strTmpPath + makeFile+".sh";
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
	 	   				strAry[1] = strTmpPath + makeFile+".sh";
	 	   				
	 	   				p = run.exec(strAry);
	 	   				outgb = new StreamGobbler(p.getInputStream());
	 	   				errgb = new StreamGobbler(p.getErrorStream());
	
	 	   				outgb.start();
	 	   				errgb.start();
	 	   				p.waitFor();
	 	   				
	 	   				//ecamsLogger.debug("====return===="+Integer.toString(p.exitValue())+" \n");  
	 	   				if (p.exitValue() != 0) {
	 	   					returnck = "false";
	 	   					AcptNo = rs.getString("cr_rsrcname")+"\n"+"운영영역과 형상관리 소스 버전이 일치하지 않습니다.\n형상관리 관리자에게 문의하십시오.";
	 	   					break;
	 	   				}
	 	   				else{
	 	   					shfile.delete();
	 	   				}	 	   				
	 	   				outgb = null;
	 	   				errgb = null;
 	        		}
 	        	}	        	
 	        	rs.close();
 	        	pstmt.close();
	        }
		    if(returnck.equals("true")){    
		        String strTeam = userInfo.getUserInfo_sub(conn,etcData.get("UserID"),"cm_project");
		        userInfo = null;
		        String strRequest = "";
		        strQuery.setLength(0);
		        strQuery.append("select cm_codename from cmm0020       \n");
		        strQuery.append(" where cm_macode='REQUEST'            \n");
		        strQuery.append("   and cm_micode=?                    \n");
		        pstmt = conn.prepareStatement(strQuery.toString());
		        pstmt.setString(1, etcData.get("ReqCD"));
		        rs = pstmt.executeQuery();
		        if (rs.next()) strRequest = rs.getString("cm_codename");
		        rs.close();
		        pstmt.close(); 
		        
		        Cmr0200 cmr0200 = new Cmr0200();
		        
		        int wkC = totcnt/300;
		        int wkD = totcnt%300;
		        if (wkD>0) wkC = wkC + 1;
	            String svAcpt[] = null; 
	            svAcpt = new String [wkC];
	            for (int j=0;wkC>j;j++) {
	            	do {
	    		        AcptNo = autoseq.getSeqNo(conn,etcData.get("ReqCD"));    		        
	    		        
	    		        i = 0;
	    		        strQuery.setLength(0);		        
	    		        strQuery.append("select count(*) as cnt from cmr1000 \n");
	    	        	strQuery.append(" where cr_acptno= ?                 \n");		        
	    	        	
	    	        	pstmt = conn.prepareStatement(strQuery.toString());
	    	        	pstmt.setString(1, AcptNo);
	    	        	
	    	        	rs = pstmt.executeQuery();
	    	        	
	    	        	if (rs.next()){
	    	        		i = rs.getInt("cnt");
	    	        	}	        	
	    	        	rs.close();
	    	        	pstmt.close();
	    	        } while(i>0);
	            	svAcpt[j] = AcptNo;
	            }
	        	int    seq = 0;
	        	int    reqcnt = 0;
	        	String retMsg = "";
	            autoseq = null;
	            conn.setAutoCommit(false);			
	        	boolean insSw = false;
	        	for (i=0 ; i<chkOutList.size() ; i++){
	        		insSw = false;
	        		
	        		if (i == 0) insSw = true;
	        		else {
	        			wkC = reqcnt%300;
	        			if (wkC == 0) insSw = true;
	        		}
	        		
	        		if (insSw == true) {        			
	        			if (reqcnt>=300) {
	        				retMsg = cmr0200.request_Confirm(AcptNo,chkOutList.get(0).get("cr_syscd"),etcData.get("ReqCD"),etcData.get("UserID"),true,ConfList,conn);
	        				if (!retMsg.equals("OK")) {
	        					conn.rollback();
	        					conn.close();
	        					throw new Exception("결재정보등록 중 오류가 발생하였습니다. 관리자에게 연락하여 주시기 바랍니다.");
	        				}
	        			}
	        			wkC = reqcnt/300;
	        			AcptNo = svAcpt[wkC];
	        			
	        			strQuery.setLength(0);
	                	strQuery.append("insert into cmr1000 ");
	                	strQuery.append("(CR_ACPTNO,CR_SYSCD,CR_SYSGB,CR_JOBCD,CR_ACPTDATE,CR_STATUS,CR_TEAMCD,CR_QRYCD, ");
	                	strQuery.append("CR_PASSOK,CR_PASSCD,CR_EMGCD,CR_EDITOR,CR_SAYU) values ( ");
	                	strQuery.append("?,?,?,?,sysdate,'0',?,?,'0',?,?,?,?) ");
	                	
	                	pstmt = conn.prepareStatement(strQuery.toString());
	                	//pstmt = new LoggableStatement(conn, strQuery.toString());
	                	pstmtcount = 1;
	                	pstmt.setString(pstmtcount++, AcptNo);
	                	pstmt.setString(pstmtcount++, chkOutList.get(0).get("cr_syscd"));
	                	pstmt.setString(pstmtcount++, chkOutList.get(0).get("sysgb"));
	                	pstmt.setString(pstmtcount++, chkOutList.get(0).get("cr_jobcd"));
	                	
	                	pstmt.setString(pstmtcount++, strTeam);
	                	
	                	pstmt.setString(pstmtcount++, etcData.get("ReqCD"));
	                	pstmt.setString(pstmtcount++, strRequest);
	                	
	                	pstmt.setString(pstmtcount++, etcData.get("ReqSayu"));
	                	
	                	pstmt.setString(pstmtcount++, etcData.get("UserID"));
	                	pstmt.setString(pstmtcount++, chkOutList.get(0).get("cr_sayu"));
	                	
	                	//ecamsLogger.error(((LoggableStatement)pstmt).getQueryString());
	                	pstmt.executeUpdate();
	                	
	                	pstmt.close();
	                	seq = 0;
	        		}
	        		
	        		strQuery.setLength(0);
	            	strQuery.append("insert into cmr1010 ");
	            	strQuery.append("(CR_ACPTNO, CR_SERNO, CR_SYSCD, CR_SYSGB, CR_JOBCD, CR_STATUS, CR_QRYCD, ");
	            	strQuery.append("CR_RSRCCD, CR_LANGCD, CR_DSNCD, CR_DSNCD2, CR_RSRCNAME, CR_RSRCNAM2, CR_CHGCD, CR_TSTCHG,");
	            	strQuery.append("CR_VERSION,CR_EDITOR,CR_BASENO,CR_BASEITEM,CR_ITEMID,CR_EDITCON,CR_EXPDAY,CR_ANALYN) values ( ");
	            	strQuery.append("?, ?, ?, ?, ?, '0', ? ,        ?, ?, ?, ?, ?, ?, ?, ?,                 ?, ?, ?, ?, ?, ?, ?,?)");
	            	
	            	pstmtcount = 1;
	            	pstmt = conn.prepareStatement(strQuery.toString());
	            	pstmt.setString(pstmtcount++, AcptNo);
	            	pstmt.setInt(pstmtcount++, ++seq);
	            	pstmt.setString(pstmtcount++, chkOutList.get(i).get("cr_syscd"));
	            	pstmt.setString(pstmtcount++, chkOutList.get(i).get("sysgb"));
	            	pstmt.setString(pstmtcount++, chkOutList.get(i).get("cr_jobcd"));
	            	pstmt.setString(pstmtcount++, etcData.get("ReqCD"));            	
	            	pstmt.setString(pstmtcount++, chkOutList.get(i).get("cr_rsrccd"));
	            	pstmt.setString(pstmtcount++, chkOutList.get(i).get("cr_langcd"));
	            	pstmt.setString(pstmtcount++, chkOutList.get(i).get("cr_dsncd"));
	            	pstmt.setString(pstmtcount++, chkOutList.get(i).get("pcdir1"));
	            	pstmt.setString(pstmtcount++, chkOutList.get(i).get("cr_rsrcname"));
	            	pstmt.setString(pstmtcount++, chkOutList.get(i).get("cr_rsrcname"));
	            	pstmt.setString(pstmtcount++, chgcode);
	            	pstmt.setString(pstmtcount++, chkOutList.get(i).get("overwrite"));
	            	pstmt.setString(pstmtcount++, chkOutList.get(i).get("cr_lstver"));
	            	pstmt.setString(pstmtcount++, etcData.get("UserID"));
	            	pstmt.setString(pstmtcount++, AcptNo);
	            	pstmt.setString(pstmtcount++, chkOutList.get(i).get("baseitemid"));
	            	pstmt.setString(pstmtcount++, chkOutList.get(i).get("cr_itemid"));
	            	if (chkOutList.get(i).get("cr_sayu") != null && chkOutList.get(i).get("cr_sayu") != "")
	            	   pstmt.setString(pstmtcount++, chkOutList.get(i).get("cr_sayu"));
	            	else{
	             	   pstmt.setString(pstmtcount++, "");
	            	}
	            	pstmt.setString(pstmtcount++, chkOutList.get(i).get("cr_expday"));
	            	pstmt.setString(pstmtcount++, etcData.get("chktool"));
	            		
	            	pstmt.executeUpdate();
	            	pstmt.close();	
	            	
	            	strQuery.setLength(0);
	            	strQuery.append("update cmr0020 set ");
	            	strQuery.append("cr_status='4', ");
	            	strQuery.append("cr_editor= ?, ");
	            	strQuery.append("cr_orderid= ? ");
	            	strQuery.append("where cr_itemid= ? ");
	            	
	            	pstmt = conn.prepareStatement(strQuery.toString());
	            	pstmtcount = 1;
	            	pstmt.setString(pstmtcount++, etcData.get("UserID"));
	            	pstmt.setString(pstmtcount++, etcData.get("OrderId"));
	            	pstmt.setString(pstmtcount++, chkOutList.get(i).get("cr_itemid"));
	
	            	pstmt.executeUpdate();
	            	pstmt.close();
	            	
	            	if (chkOutList.get(i).get("baseitemid").equals(chkOutList.get(i).get("cr_itemid"))) {
	        			++reqcnt;
	        		}
	            	
	            	for(int j =0;j<OrderList.size();j++){
	            		
	            		if(chkOutList.get(i).get("cr_itemid").equals(OrderList.get(j).get("CR_ITEMID"))){
	            			strQuery.setLength(0);		        
			    	        strQuery.append("select nvl(max(cr_seq),'0') as max from cmr1012 \n");
			    	        strQuery.append("where cr_acptno=?								 \n");
			    	        pstmtcount = 1;
			            	pstmt = conn.prepareStatement(strQuery.toString());
			            	pstmt.setString(pstmtcount++, AcptNo);
			            	rs = pstmt.executeQuery();
			            	if (rs.next()){
			            		cnt = rs.getInt("max")+1;
			            	}	        	
			            	rs.close();
			            	pstmt.close();
		            	
		            	
		                	strQuery.setLength(0);
		                	strQuery.append("insert into cmr1012 ");
		                	strQuery.append("(CR_ACPTNO,CR_SEQ,CR_ITEMID,CR_ORDERID,CR_REQSUB) values (					 \n");
		                	strQuery.append("?, ?, ?, ?, ?)");
		                	pstmtcount = 1;
		                	pstmt = conn.prepareStatement(strQuery.toString());
		                	//pstmt = new LoggableStatement(conn, strQuery.toString());
		                	pstmt.setString(pstmtcount++, AcptNo);
		                	pstmt.setInt(pstmtcount++, cnt++);
		                	pstmt.setString(pstmtcount++, OrderList.get(j).get("CR_ITEMID"));
		                	pstmt.setString(pstmtcount++, OrderList.get(j).get("CC_ORDERID"));
		                	pstmt.setString(pstmtcount++, OrderList.get(j).get("CC_REQSUB"));
		                	//ecamsLogger.error(((LoggableStatement)pstmt).getQueryString());
		                	pstmt.executeUpdate();
		                	pstmt.close();
	            		}
	            	}
	            	
	            	
	        	}
	        	
	        	
	        	retMsg = cmr0200.request_Confirm(AcptNo,chkOutList.get(0).get("cr_syscd"),etcData.get("ReqCD"),etcData.get("UserID"),true,ConfList,conn);
				if (!retMsg.equals("OK")) {
					conn.rollback();
					conn.close();
					throw new Exception("결재정보등록 중 오류가 발생하였습니다. 관리자에게 연락하여 주시기 바랍니다.");
				}
		    }
	        	conn.commit();
	        	conn.close();
	        	
	        	rs = null;
	    		pstmt = null;
	    		conn = null;
    		
        	return AcptNo;
        	
        	
		} catch (SQLException sqlexception) {
			sqlexception.printStackTrace();
			if (rs != null)     try{rs.close();}catch (Exception ex){ex.printStackTrace();}
			if (pstmt != null)  try{pstmt.close();}catch (Exception ex2){ex2.printStackTrace();}
			if (conn != null){
				try{
					conn.rollback();
					ConnectionResource.release(conn);
				}catch(Exception ex3){
					ecamsLogger.error("## Cmr0100.request_Check_Out() connection release exception ##");
					ex3.printStackTrace();
				}
			}
			ecamsLogger.error("## Cmr0100.request_Check_Out() SQLException START ##");
			ecamsLogger.error("## Error DESC : ", sqlexception);
			ecamsLogger.error("## Cmr0100.request_Check_Out() SQLException END ##");
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
					ecamsLogger.error("## Cmr0100.request_Check_Out() connection release exception ##");
					ex3.printStackTrace();
				}
			}
			ecamsLogger.error("## Cmr0100.request_Check_Out() Exception START ##");
			ecamsLogger.error("## Error DESC : ", exception);
			ecamsLogger.error("## Cmr0100.request_Check_Out() Exception END ##");
			throw exception;
		}finally{
			if (strQuery != null) 	strQuery = null;
			if (rs != null)     try{rs.close();}catch (Exception ex){ex.printStackTrace();}
			if (pstmt != null)  try{pstmt.close();}catch (Exception ex2){ex2.printStackTrace();}
			if (conn != null){
				try{
					ConnectionResource.release(conn);
				}catch(Exception ex3){
					ecamsLogger.error("## Cmr0100.request_Check_Out() connection release exception ##");
					ex3.printStackTrace();
				}
			}
		}
	}

	public Object[] getLinkList(String UserId,String AcptNo) throws SQLException, Exception {
		Connection        conn        = null;
		PreparedStatement pstmt       = null;
		ResultSet         rs          = null;
		PreparedStatement pstmt2      = null;
		ResultSet         rs2         = null;
		StringBuffer      strQuery    = new StringBuffer();
		Object[]		  rtObj		  = null;
		ArrayList<HashMap<String, String>>		  rtList	  = new ArrayList<HashMap<String, String>>();
		HashMap<String, String>			  rst		  = null;
		ConnectionContext connectionContext = new ConnectionResource();
		int				  pstmtcount  = 1;
		
		try {	
			conn = connectionContext.getConnection();
			
			pstmtcount = 1;
			strQuery.setLength(0);	
			strQuery.append("select /*+ ALL ROWS */                                    \n");
			strQuery.append("       a.CR_RSRCNAME,a.CR_STORY,a.CR_LSTVER,a.cr_acptno,  \n");
			strQuery.append("       TO_CHAR(a.CR_LASTDATE,'yyyy-mm-dd') as LASTDT,     \n");
			strQuery.append("       a.CR_SYSCD,a.CR_RSRCCD,a.CR_LANGCD,a.CR_DSNCD,     \n");
			strQuery.append("       a.CR_JOBCD,a.CR_FILESIZE,a.CR_FILEDATE,a.cr_nomodify,\n");
			strQuery.append("       a.CR_ITEMID,a.CR_STATUS,c.CM_CODENAME as CODENAME, \n");
			strQuery.append("       d.CM_USERNAME,e.CM_DIRPATH, f.CM_CODENAME as JAWON,\n");
			strQuery.append("       b.CM_INFO,g.cm_jobname                             \n");
			strQuery.append("  from cmm0020 f,cmm0070 e,cmm0040 d,cmm0020 c,cmm0036 b, \n"); 
			strQuery.append("       cmr0020 a,cmm0102 g,cmm0044 h,cmr1090 i            \n"); 
			strQuery.append(" where i.cr_acptno=? and i.cr_syscd=a.cr_syscd            \n");
			strQuery.append("   and i.cr_itemid=a.cr_itemid and a.cr_lstver>0          \n");
			strQuery.append("   and a.cr_syscd=h.cm_syscd and a.cr_jobcd=h.cm_jobcd    \n");
			strQuery.append("   and h.cm_closedt is null and h.cm_userid=?             \n");
			strQuery.append("   and a.cr_syscd=e.cm_syscd and a.cr_dsncd=e.cm_dsncd    \n");
			strQuery.append("   and a.cr_editor=d.cm_userid and a.cr_jobcd = g.cm_jobcd\n");
			strQuery.append("   and c.cm_macode='CMR0020' and a.cr_status=c.cm_micode  \n");
			strQuery.append("   and f.cm_macode='JAWON' and a.cr_rsrccd=f.cm_micode    \n");
			strQuery.append("   and a.cr_syscd=b.cm_syscd and a.cr_rsrccd=b.cm_rsrccd  \n");			
			
            pstmt = conn.prepareStatement(strQuery.toString());	
			//pstmt =  new LoggableStatement(conn, strQuery.toString());
            
            pstmt.setString(pstmtcount++, AcptNo);
            pstmt.setString(pstmtcount++, UserId);
            ////ecamsLogger.error(((LoggableStatement)pstmt).getQueryString());
            rs = pstmt.executeQuery();
            
            rtList.clear();
			while (rs.next()){
				rst = new HashMap<String,String>();
				rst.put("ID", Integer.toString(rs.getRow()));
				rst.put("cm_dirpath",rs.getString("cm_dirpath"));
				rst.put("cr_rsrcname",rs.getString("cr_rsrcname"));
				/*
				rst.put("cr_story",rs.getString("cr_story"));
				*/
				rst.put("jobname", rs.getString("cm_jobname"));
				rst.put("jawon",rs.getString("jawon"));
				rst.put("cr_lstver",rs.getString("cr_lstver"));
				rst.put("cm_username",rs.getString("cm_username"));
				rst.put("lastdt",rs.getString("lastdt"));
				rst.put("codename",rs.getString("codename"));
				rst.put("cr_syscd",rs.getString("cr_syscd"));
				rst.put("cr_rsrccd",rs.getString("cr_rsrccd"));
				rst.put("cr_langcd",rs.getString("cr_langcd"));
				rst.put("cr_dsncd",rs.getString("cr_dsncd"));
				rst.put("cr_jobcd",rs.getString("cr_jobcd"));
				
				rst.put("cr_itemid",rs.getString("cr_itemid"));
				rst.put("cr_status",rs.getString("cr_status"));
				rst.put("selAcptno",rs.getString("cr_acptno"));
				if (rs.getString("cr_nomodify") != null)
					rst.put("cr_nomodify",rs.getString("cr_nomodify"));
				else
					rst.put("cr_nomodify","0");
				rst.put("cm_info",rs.getString("cm_info"));
				rst.put("selected_flag","0");
				rtList.add(rst);
				rst = null;
			}//end of while-loop statement
			
			rs.close();
			pstmt.close();
			
			strQuery.setLength(0);
			strQuery.append("delete cmr1090 where cr_acptno=?  \n");
			pstmt = conn.prepareStatement(strQuery.toString());
			pstmt.setString(1, AcptNo);
			pstmt.executeUpdate();
			
			conn.close();			
			conn = null;
			pstmt = null;
			rs = null;
			rtObj =  rtList.toArray();
			rtList = null;
			
			return rtObj;
			
		} catch (SQLException sqlexception) {
			sqlexception.printStackTrace();
			ecamsLogger.error("## Cmr0100.getLinkList() SQLException START ##");
			ecamsLogger.error("## Error DESC : ", sqlexception);	
			ecamsLogger.error("## Cmr0100.getLinkList() SQLException END ##");			
			throw sqlexception;
		} catch (Exception exception) {
			exception.printStackTrace();
			ecamsLogger.error("## Cmr0100.getLinkList() Exception START ##");				
			ecamsLogger.error("## Error DESC : ", exception);	
			ecamsLogger.error("## Cmr0100.getLinkList() Exception END ##");				
			throw exception;
		}finally{
			if (strQuery != null) 	strQuery = null;
			if (rtObj != null)  rtObj = null;
			if (rs != null)     try{rs.close();}catch (Exception ex){ex.printStackTrace();}
			if (pstmt != null)  try{pstmt.close();}catch (Exception ex2){ex2.printStackTrace();}
			if (rs2 != null)     try{rs2.close();}catch (Exception ex){ex.printStackTrace();}
			if (pstmt2 != null)  try{pstmt2.close();}catch (Exception ex2){ex2.printStackTrace();}
			if (conn != null){
				try{
					ConnectionResource.release(conn);
				}catch(Exception ex3){
					ecamsLogger.error("## Cmr0100.getLinkList() connection release exception ##");
					ex3.printStackTrace();
				}
			}
		}
	} //getLinkList	
	
	public String svrFileMake(String AcptNo) throws SQLException, Exception {
		Connection        conn        = null;
		PreparedStatement pstmt       = null;
		ResultSet         rs          = null;
		StringBuffer      strQuery    = new StringBuffer();
		String            strErMsg    = "0";
		ConnectionContext connectionContext = new ConnectionResource();
		
		try {
			//ecamsLogger.debug("+++++++++prcCd========"+prcCd);
			
			int	cmdrtn;
			
			
			conn = connectionContext.getConnection();
			strQuery.setLength(0);
			strQuery.append("delete cmr1011                                \n");
			strQuery.append(" where cr_acptno=? and cr_prcsys='SYSFMK'     \n");
			strQuery.append("   and cr_serno in                            \n");
			strQuery.append("    (select cr_serno from cmr1010             \n");
			strQuery.append("      where cr_acptno=?                       \n");
			strQuery.append("        and nvl(cr_putcode,'ERR1')<>'0000')   \n");
			pstmt = conn.prepareStatement(strQuery.toString());
			pstmt.setString(1, AcptNo);
			pstmt.setString(2, AcptNo);
			pstmt.executeUpdate();
			pstmt.close();
			
			strQuery.setLength(0);
			strQuery.append("update cmr1010 set cr_putcode='',cr_pid='',cr_sysstep=0  \n");
			strQuery.append(" where cr_acptno=? and nvl(cr_putcode,'ERR1')<>'0000'    \n");
			pstmt = conn.prepareStatement(strQuery.toString());
			pstmt.setString(1, AcptNo);
			pstmt.executeUpdate();
			pstmt.close();
			
			cmdrtn = execShell(AcptNo+"filemake.sh","./ecams_acct "+AcptNo);
			
			if (cmdrtn != 0) {
				strErMsg = "파일생성작업에 실패하였습니다. [" + "ecams_acct " + AcptNo + "]";
				return strErMsg;
			}
			
			//ecamsLogger.debug("++++ run ecams_acct ++++ecams_acct " + AcptNo + "]");
			strQuery.setLength(0);
	        strQuery.append("select count(*) as cnt                     \n");
	        strQuery.append("  from cmr9900                             \n");
	        strQuery.append(" where cr_acptno=? and cr_locat='00'       \n");
	        strQuery.append("   and cr_team='SYSFMK' and cr_status='0'  \n");
	        pstmt = conn.prepareStatement(strQuery.toString());
			//pstmt = new LoggableStatement(conn,strQuery.toString());
			pstmt.setString(1, AcptNo);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				if (rs.getInt("cnt") > 0) strErMsg = "파일생성 작업 중 오류가 발생한 건이 있습니다. 확인 후 조치하시기 바랍니다.";
			}
			rs.close();
			pstmt.close();
			
	        conn.close();

			rs = null;
			pstmt = null;
			conn = null;

	        return strErMsg;

		} catch (SQLException sqlexception) {
			sqlexception.printStackTrace();
			ecamsLogger.error("## Cmr0100.svrFileMake() SQLException START ##");
			ecamsLogger.error("## Error DESC : ", sqlexception);
			ecamsLogger.error("## Cmr0100.svrFileMake() SQLException END ##");
			throw sqlexception;
		} catch (Exception exception) {
			exception.printStackTrace();
			ecamsLogger.error("## Cmr0100.svrFileMake() Exception START ##");
			ecamsLogger.error("## Error DESC : ", exception);
			ecamsLogger.error("## Cmr0100.svrFileMake() Exception END ##");
			throw exception;
		}finally{
			if (strQuery != null) strQuery = null;
			if (rs != null)     try{rs.close();}catch (Exception ex){ex.printStackTrace();}
			if (pstmt != null)  try{pstmt.close();}catch (Exception ex2){ex2.printStackTrace();}
			if (conn != null){
				try{
					ConnectionResource.release(conn);
				}catch(Exception ex3){
					ecamsLogger.error("## Cmr0100.svrFileMake() connection release exception ##");
					ex3.printStackTrace();
				}
			}
		}
	}//end of svrFileMake() method statement
	
	public int execShell(String shFile,String parmName) throws Exception {
		SystemPath		  cTempGet	  = new SystemPath();
		String			  tmpPath = "";
		String			  strBinPath = "";
		File shfile=null;
		String  shFileName = "";
		OutputStreamWriter writer = null;
		String[] strAry = null;
		Runtime  run = null;
		Process p = null;
		//ByteArrayOutputStream fileStream = null;
		
		//byte[] byteTmpBuf = null;
		Connection        conn        = null;
		ResultSet         rs          = null;
		PreparedStatement pstmt       = null;
		ConnectionContext connectionContext = new ConnectionResource();
		StringBuffer      strQuery    = new StringBuffer();
		String rtString = "";
		
		try {
			
			tmpPath = cTempGet.getTmpDir("99");
			strBinPath = cTempGet.getTmpDir("14");
			
			shFileName = tmpPath + "/" + shFile; 			
			shfile = new File(shFileName);
			
			if( !(shfile.isFile()) )              //File이 없으면 
			{
				shfile.createNewFile();          //File 생성
			}
			
			// 20221219 ecams_batexec 추가 쿼리
			conn = connectionContext.getConnection();
			conn.setAutoCommit(false);
			strQuery.setLength(0);
			strQuery.append("select cm_ipaddr, cm_port 	\n");
			strQuery.append("  from cmm0010 			\n");
			strQuery.append(" where cm_stno = 'ECAMS'	\n");
			
			pstmt = conn.prepareStatement(strQuery.toString());	
			rs = pstmt.executeQuery();
			if(rs.next()){
				rtString = rs.getString("cm_ipaddr") + " " + rs.getString("cm_port") + " 0";
			}
			
			writer = new OutputStreamWriter( new FileOutputStream(shFileName));
			writer.write("cd "+strBinPath +"\n");
//			writer.write(parmName +"\n");
			writer.write("./ecams_batexec " +rtString + " \"cd " + strBinPath+ ";" + parmName + "\" \n");
			writer.write("exit $?\n");
			writer.close();
			
			
			strAry = new String[3];
			strAry[0] = "chmod";
			strAry[1] = "777";
			strAry[2] = shFileName;			
			
			run = Runtime.getRuntime();

			//ecamsLogger.debug("====chmod===="+"chmod "+"777 "+shFileName+" \n");
			p = run.exec(strAry);
			p.waitFor();
			
			//ecamsLogger.debug("====chmod return===="+Integer.toString(p.exitValue())+" \n");
			
			
			run = Runtime.getRuntime();
			
			strAry = new String[2];
			
			strAry[0] = "/bin/sh";
			strAry[1] = shFileName;
			
			p = run.exec(strAry);
			p.waitFor();
			
			
			//ecamsLogger.debug("====return===="+Integer.toString(p.exitValue())+" \n");
			if (p.exitValue() != 0) {
				ecamsLogger.debug("====execShell shellfile===="+shFileName+","+Integer.toString(p.exitValue())+" \n");
				//shfile.delete();
				
			}
			else{
				//임시막음 확인을 위하여
				shfile.delete();
			}			
			//ecamsLogger.debug("====execShell===="+Integer.toString(p.exitValue())+"\n");
			pstmt.close();
			conn.close();
			pstmt = null;
			conn = null;
			rs.close();// 20221219 ecams_batexec 추가 쿼리
			rs = null;		
			return p.exitValue();			
			
		} catch (Exception exception) {
			exception.printStackTrace();
			ecamsLogger.error("## Cmr0200.execShell() Exception START ##");				
			ecamsLogger.error("## Error DESC : ", exception);	
			ecamsLogger.error("## Cmr0200" +
					".execShell() Exception END ##");				
			throw exception;
		}finally{
			//fileStr = fileStream.toString("EUC-KR");	
		}
		

	}//execShell

	public Object[] getSysInfo(String UserId,String AcptNo) throws SQLException, Exception {
		Connection        conn        = null;
		PreparedStatement pstmt       = null;
		ResultSet         rs          = null;
		StringBuffer      strQuery    = new StringBuffer();
		Object[]		  rtObj		  = null;
		ArrayList<HashMap<String, String>>		  rtList	  = new ArrayList<HashMap<String, String>>();
		HashMap<String, String>			  rst		  = null;
		ConnectionContext connectionContext = new ConnectionResource();
		
		try {			
			conn = connectionContext.getConnection();
							
			strQuery.setLength(0);
			strQuery.append("select a.cm_syscd,a.cm_sysmsg,a.cm_sysgb,a.cm_sysfc1, \n");
			strQuery.append("       a.cm_dirbase,a.cm_sysinfo,                     \n");
			strQuery.append("       sign(nvl(cm_stopst,to_char(sysdate,'yyyymmddhh24mi')) - to_char(sysdate,'yyyymmddhh24mi')) diff1,\n");
			strQuery.append("       sign(nvl(cm_stoped,to_char(sysdate,'yyyymmddhh24mi')) - to_char(sysdate,'yyyymmddhh24mi')) diff2 \n");
			strQuery.append("  from cmm0030 a,                                    \n");
			strQuery.append("      (select cm_syscd from cmm0044                  \n");
			strQuery.append("        where cm_userid=? and cm_closedt is null     \n");
			strQuery.append("        group by cm_syscd) b,                        \n");
			strQuery.append("      (select cr_syscd from cmr1090                  \n");
			strQuery.append("        where cr_acptno=?                            \n");
			strQuery.append("        group by cr_syscd) c                         \n");
			strQuery.append(" where c.cr_syscd=a.cm_syscd                         \n");
			strQuery.append("   and a.cm_syscd=b.cm_syscd                         \n");
			strQuery.append(" order by a.cm_sysmsg                                \n");
            pstmt = conn.prepareStatement(strQuery.toString());	
            //pstmt = new LoggableStatement(conn,strQuery.toString());
            
            pstmt.setString(1, UserId);	
            pstmt.setString(2, AcptNo);	
            ////ecamsLogger.error(((LoggableStatement)pstmt).getQueryString());
            rs = pstmt.executeQuery();
            rtList.clear();
            //String tmpSyscd = "";
            String tstInfo = "";
            //boolean stopSw = false;
			while (rs.next()){
				tstInfo = rs.getString("cm_sysinfo");
				rst = new HashMap<String,String>();
				rst.put("ID", Integer.toString(rs.getRow()));
				rst.put("cm_syscd",rs.getString("cm_syscd"));
				//tmpSyscd = rs.getString("cm_syscd");
				rst.put("cm_sysmsg",rs.getString("cm_sysmsg"));
				rst.put("cm_sysgb",rs.getString("cm_sysgb"));
				rst.put("cm_sysfc1",rs.getString("cm_sysfc1"));
				rst.put("cm_dirbase",rs.getString("cm_dirbase"));
				rst.put("websw", "N");
				rst.put("delsw", "Y");
				
				//stopSw = false;
				if (rs.getString("cm_sysinfo").substring(4,5).equals("1")) {
					if (rs.getInt("diff1")<0 && rs.getInt("diff2")>0) {
						rst.put("cm_stopsw", "1");
						tstInfo = tstInfo.substring(0,4) + "1" + tstInfo.substring(5);
						//stopSw = true;
					} else {
						rst.put("cm_stopsw", "0");
						tstInfo = tstInfo.substring(0,4) + "0" + tstInfo.substring(5);
					}
				} else{
					rst.put("cm_stopsw", "0");
				}
				rst.put("cm_sysinfo",tstInfo);
				rst.put("TstSw", "0");
				rst.put("setyn", "N");
				rtList.add(rst);
				rst = null;
			}
			
			rs.close();
			pstmt.close();
			conn.close();
			rs = null;
			pstmt = null;
			conn = null;
			
			rtObj =  rtList.toArray();
			rtList.clear();
			rtList = null;
			
			return rtObj;			
			
		} catch (SQLException sqlexception) {
			sqlexception.printStackTrace();
			ecamsLogger.error("## Cmr0100.getSysInfo() SQLException START ##");
			ecamsLogger.error("## Error DESC : ", sqlexception);	
			ecamsLogger.error("## Cmr0100.getSysInfo() SQLException END ##");			
			throw sqlexception;
		} catch (Exception exception) {
			exception.printStackTrace();
			ecamsLogger.error("## Cmr0100.getSysInfo() Exception START ##");				
			ecamsLogger.error("## Error DESC : ", exception);	
			ecamsLogger.error("## Cmr0100.getSysInfo() Exception END ##");				
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
					ecamsLogger.error("## Cmr0100.getSysInfo() connection release exception ##");
					ex3.printStackTrace();
				}
			}
		}
	}//end of getSysInfo() method statement
	
		
	public Object[] getProgFileList(String AcptNo,String fileGb) throws SQLException, Exception {
		Connection        conn        = null;
		PreparedStatement pstmt       = null;
		ResultSet         rs          = null;
		StringBuffer      strQuery    = new StringBuffer();
		ArrayList<HashMap<String, String>>  rsval = new ArrayList<HashMap<String, String>>();
		HashMap<String, String>			  rst		  = null;
		Object[]		  rtObj		  = null;
		//String           strDevHome   = null;
		ConnectionContext connectionContext = new ConnectionResource();
		
		try {
			conn = connectionContext.getConnection();
			
			strQuery.setLength(0);
			strQuery.append("update cmr1010 set cr_putcode='0000'               \n");
			strQuery.append(" where cr_acptno=?                                 \n");
			strQuery.append("   and cr_serno in (select a.cr_serno              \n");
			strQuery.append("                      from cmr1010 a,cmm0036 b     \n");
			strQuery.append("                     where a.cr_acptno=?           \n");
			strQuery.append("                       and a.cr_syscd=b.cm_syscd   \n");
			strQuery.append("                       and a.cr_rsrccd=b.cm_rsrccd \n");
			if (fileGb.equals("G")) {
				strQuery.append("                   and (substr(b.cm_info,45,1)='1' or \n");
			    strQuery.append("                        substr(b.cm_info,38,1)='1')\n");
			    strQuery.append("                   and substr(b.cm_info,3,1)='1') \n");
			} else {
				strQuery.append("                   and (substr(b.cm_info,45,1)='0' \n");
				strQuery.append("                   or substr(b.cm_info,24,1)='0')) \n");
			}
			
            pstmt = conn.prepareStatement(strQuery.toString());
 //           pstmt = new LoggableStatement(conn,strQuery.toString());
            pstmt.setString(1, AcptNo);
            pstmt.setString(2, AcptNo);
  //          //ecamsLogger.error(((LoggableStatement)pstmt).getQueryString());
            pstmt.executeUpdate();
            pstmt.close();
            
            strQuery.setLength(0);
			strQuery.append("update cmr1010 set cr_putcode=''                   \n");
			strQuery.append(" where cr_acptno=?                                 \n");
			strQuery.append("   and cr_serno in (select a.cr_serno              \n");
			strQuery.append("                      from cmr1010 a,cmm0036 b     \n");
			strQuery.append("                     where a.cr_acptno=?           \n");
			strQuery.append("                       and a.cr_syscd=b.cm_syscd   \n");
			strQuery.append("                       and a.cr_rsrccd=b.cm_rsrccd \n");
			strQuery.append("                       and substr(b.cm_info,45,1)='1' \n");
			strQuery.append("                       and substr(b.cm_info,24,1)='1') \n");
			
            pstmt = conn.prepareStatement(strQuery.toString());
 //           pstmt = new LoggableStatement(conn,strQuery.toString());
            pstmt.setString(1, AcptNo);
            pstmt.setString(2, AcptNo);
  //          //ecamsLogger.error(((LoggableStatement)pstmt).getQueryString());
            pstmt.executeUpdate();
            pstmt.close();
            
			strQuery.setLength(0);
			strQuery.append("select A.CR_SERNO,a.cr_rsrcname,a.cr_dsncd2, \n");
			strQuery.append("   replace(a.cr_dsncd2,'\\','\\\\') as dir2, \n");
			strQuery.append("   c.cm_info                                 \n");
			strQuery.append("  from cmr1010 A,CMM0036 C                   \n");
			strQuery.append(" Where A.cr_acptno=?  		                  \n");
			strQuery.append("   and nvl(a.cr_putcode,'ERR1')<>'0000'      \n");
			strQuery.append("   AND a.cr_syscd=c.cm_syscd 	              \n");
			strQuery.append("   and a.cr_rsrccd=c.cm_rsrccd	              \n");
			if (fileGb.equals("G")) {
				strQuery.append("      and (substr(c.cm_info,45,1)='1' or \n");
			    strQuery.append("              substr(c.cm_info,38,1)='1')\n");
			    strQuery.append("      and substr(c.cm_info,3,1)='0'      \n");
			} else {
				strQuery.append("      and substr(c.cm_info,45,1)='1'     \n");
				strQuery.append("      and substr(c.cm_info,24,1)='1'    \n");
			}
            
            pstmt = conn.prepareStatement(strQuery.toString());
            //pstmt = new LoggableStatement(conn,strQuery.toString());
            pstmt.setString(1, AcptNo);
            //ecamsLogger.error(((LoggableStatement)pstmt).getQueryString());
            rs = pstmt.executeQuery();
                        
            rsval.clear();
            
			while (rs.next()){
				rst = new HashMap<String, String>();
				rst.put("cr_serno", rs.getString("CR_SERNO"));
				rst.put("cr_rsrcname", rs.getString("cr_rsrcname"));
				rst.put("cr_acptno", AcptNo);
				rst.put("cm_dirpath", rs.getString("cr_dsncd2"));
				rst.put("pcdir", rs.getString("dir2"));
				rst.put("cm_info", rs.getString("cm_info"));
				rst.put("sendflag","0");
				rst.put("errflag","0");
				//ecamsLogger.error("+++ rst ==="+rst.toString());
				rsval.add(rst);
				rst = null;
			}//end of while-loop statement
			
			rs.close();
			pstmt.close();
			conn.close();
			conn = null;
			pstmt = null;
			rs = null;
			if (rsval.size() == 0 ) {
				boolean ret = callCmr9900_Str(AcptNo);
				
				if (ret == false) {
					rst = new HashMap<String, String>();
					rst.put("cr_rsrcname", "ERROR");
					rsval.add(rst);
					rst = null;
				}
			}
			rtObj =  rsval.toArray();
			//ecamsLogger.debug("+++ rsval ==="+rsval.toString());
			rsval = null;
			
			return rtObj;
			
			
		} catch (SQLException sqlexception) {
			sqlexception.printStackTrace();
			ecamsLogger.error("## Cmr0100.getProgFileList() SQLException START ##");
			ecamsLogger.error("## Error DESC : ", sqlexception);	
			ecamsLogger.error("## Cmr0100.getProgFileList() SQLException END ##");			
			throw sqlexception;
		} catch (Exception exception) {
			exception.printStackTrace();
			ecamsLogger.error("## Cmr0100.getProgFileList() Exception START ##");				
			ecamsLogger.error("## Error DESC : ", exception);	
			ecamsLogger.error("## Cmr0100.getProgFileList() Exception END ##");				
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
					ecamsLogger.error("## Cmr0100.getProgFileList() connection release exception ##");
					ex3.printStackTrace();
				}
			}
		}
	}//end of getProgFileList() method statement
	
	public Boolean setTranResult(String acptNo,String serNo,String ret) throws SQLException, Exception {
		Connection        conn        = null;
		PreparedStatement pstmt       = null;
		ResultSet         rs          = null;
		StringBuffer      strQuery    = new StringBuffer();
		ConnectionContext connectionContext = new ConnectionResource();
		String 			  errCode = "";
		int				  nerrCode  = 0;
		try {
			conn = connectionContext.getConnection();

            
			
			try {
				nerrCode = Integer.parseInt(ret);
				if (nerrCode == -8){
					errCode = "SVER";
				}
				else if (nerrCode == -7){
					errCode = "EROR";
				}
				else{
					errCode = String.format("%04d", nerrCode);
				}
			} catch (NumberFormatException e) {
		        errCode = "SRER";
		    }
		    
			strQuery.setLength(0);
			strQuery.append("update cmr1010 set cr_putcode = ? \n");
			strQuery.append("where cr_acptno= ? \n");
			strQuery.append("and cr_serno= ? \n");
			
			pstmt = conn.prepareStatement(strQuery.toString());
			//pstmt =  new LoggableStatement(conn, strQuery.toString());	
            
			pstmt.setString(1, errCode);
			pstmt.setString(2, acptNo);
			pstmt.setInt(3, Integer.parseInt(serNo));
			
			////ecamsLogger.error(((LoggableStatement)pstmt).getQueryString());
            pstmt.executeUpdate();
            pstmt.close();
            conn.close();//수정
            pstmt = null; //수정
            conn = null; //수정
            
		} catch (SQLException sqlexception) {
			sqlexception.printStackTrace();
			ecamsLogger.error("## Cmr0100.setTranResult() SQLException START ##");
			ecamsLogger.error("## Error DESC : ", sqlexception);	
			ecamsLogger.error("## Cmr0100.setTranResult() SQLException END ##");			
			throw sqlexception;
		} catch (Exception exception) {
			exception.printStackTrace();
			ecamsLogger.error("## Cmr0100.setTranResult() Exception START ##");				
			ecamsLogger.error("## Error DESC : ", exception);	
			ecamsLogger.error("## Cmr0100.setTranResult() Exception END ##");				
			throw exception;
		}finally{
			if (strQuery != null) 	strQuery = null;
			if (rs != null)     try{rs.close();}catch (Exception ex){ex.printStackTrace();}
			if (pstmt != null)  try{pstmt.close();}catch (Exception ex2){ex2.printStackTrace();}
			if (conn != null){
				try{
					ConnectionResource.release(conn);
				}catch(Exception ex3){
					ecamsLogger.error("## Cmr0100.setTranResult() connection release exception ##");
					ex3.printStackTrace();
				}
			}
		}
		
		return true;
	}//end of setTranResult() method statement	
	public Boolean callCmr9900_Str(String acptNo) throws SQLException, Exception {
		Connection        conn        = null;
		PreparedStatement pstmt       = null;
		ResultSet         rs          = null;
		StringBuffer      strQuery    = new StringBuffer();
		ConnectionContext connectionContext = new ConnectionResource();
		int				  errcnt = 0;
		String			  szJobCD = "";
		
		try {
			conn = connectionContext.getConnection();
			strQuery.setLength(0);
			strQuery.append("select count(*) errcnt          \n");
			strQuery.append("  from cmr1010 a,cmm0036 b      \n");
			strQuery.append(" where a.cr_acptno= ?           \n");
			strQuery.append("  and a.cr_status <> '3'        \n");
			strQuery.append("  and a.cr_syscd=b.cm_syscd     \n");
			strQuery.append("  and a.cr_rsrccd=b.cm_rsrccd   \n");
			strQuery.append("  and substr(b.cm_info,45,1)='1'\n");
			strQuery.append("  and nvl(a.cr_putcode,'ERR1') != '0000' \n");
			pstmt = conn.prepareStatement(strQuery.toString());
			pstmt.setString(1, acptNo);
			rs = pstmt.executeQuery();
			errcnt = -1;
			if (rs.next()){
				errcnt = rs.getInt("errcnt");
			}
			
			if (errcnt != 0){
				if(conn != null) conn.close();
				conn = null;
				throw new Exception("정상적으로 송수신하지  못한 파일이 있으니 목록에서 확인, 원인 조치 후 다시처리 하십시오.");
			}
			
			rs.close();
			pstmt.close();
			
			strQuery.setLength(0);
			strQuery.append("select cr_team from cmr9900 \n");
			strQuery.append("where cr_acptno= ? \n");
			strQuery.append("  and cr_locat = '00' \n");
			strQuery.append("  and cr_status = '0' \n");
			pstmt = conn.prepareStatement(strQuery.toString());
			pstmt.setString(1, acptNo);
			rs = pstmt.executeQuery();
			szJobCD = "";
			if (rs.next()){
				szJobCD = rs.getString("cr_team");
			}

			if ("".equals(szJobCD)){
				if(conn != null) conn.close();
				conn = null;
				throw new Exception("결재정보 오류입니다.");
			}
			rs.close();
			pstmt.close();
			
        	strQuery.setLength(0);
        	strQuery.append("Begin CMR9900_STR ( ");
        	strQuery.append("?, ?, 'eCAMS자동처리', '9', '', '1' ); End;");
        	pstmt = conn.prepareStatement(strQuery.toString());
        	pstmt.setString(1, acptNo);
        	pstmt.setString(2, szJobCD);
        	pstmt.executeUpdate();
        	pstmt.close();
        	conn.close();//수정
        	pstmt = null; //수정
        	conn =null; //수정
        	
        	return true;
	
		} catch (SQLException sqlexception) {
			sqlexception.printStackTrace();
			ecamsLogger.error("## Cmr0100.callCmr9900_Str() SQLException START ##");
			ecamsLogger.error("## Error DESC : ", sqlexception);	
			ecamsLogger.error("## Cmr0100.callCmr9900_Str() SQLException END ##");			
			throw sqlexception;
		} catch (Exception exception) {
			exception.printStackTrace();
			ecamsLogger.error("## Cmr0100.callCmr9900_Str() Exception START ##");				
			ecamsLogger.error("## Error DESC : ", exception);	
			ecamsLogger.error("## Cmr0100.callCmr9900_Str() Exception END ##");				
			throw exception;
		}finally{
			if (strQuery != null) 	strQuery = null;
			if (rs != null)     try{rs.close();}catch (Exception ex){ex.printStackTrace();}
			if (pstmt != null)  try{pstmt.close();}catch (Exception ex2){ex2.printStackTrace();}
			if (conn != null){
				try{
					ConnectionResource.release(conn);
				}catch(Exception ex3){
					ecamsLogger.error("## Cmr0100.callCmr9900_Str() connection release exception ##");
					ex3.printStackTrace();
				}
			}
		}
	}//end of callCmr9900_Str() method statement
	
	public Object[] getReturnInfo(String UserId,String AcptNo) throws SQLException, Exception {
		Connection        conn        = null;
		PreparedStatement pstmt       = null;
		ResultSet         rs          = null;
		PreparedStatement pstmt2       = null;
		ResultSet         rs2          = null;
		StringBuffer      strQuery    = new StringBuffer();
		ArrayList<HashMap<String, String>>  	  rsval 	  = new ArrayList<HashMap<String, String>>();
		HashMap<String, String>			  rst		  		  = null;
		Object[]		  rtObj		  = null;
		Object[]		  tmpObjs		  = null;
		//String           strDevHome   = null;
		ConnectionContext connectionContext = new ConnectionResource();
		
		try {
			conn = connectionContext.getConnection();
			strQuery.setLength(0);
			strQuery.append("select a.cr_acptno, a.cr_syscd, substr(a.cr_acptno,5,2) as reqcd, b.cr_rsrcname, b.cr_qrycd, b.cr_itemid,  \n");
			strQuery.append("(select cm_sysgb from cmm0030 where cm_syscd = a.cr_syscd) as cm_sysgb										\n");
			strQuery.append("		from cmr1000 a, cmr1010 b where a.cr_acptno = ? and a.cr_acptno = b.cr_acptno 						\n");
			pstmt = conn.prepareStatement(strQuery.toString());
			//pstmt =  new LoggableStatement(conn, strQuery.toString());		
			pstmt.setString(1, AcptNo);
			//ecamsLogger.error(((LoggableStatement)pstmt).getQueryString()); 
            rs = pstmt.executeQuery(); 
            rsval.clear();
            while(rs.next()){
            	
            	String ReqCd = rs.getString("cr_qrycd");
            	String rsrcName = rs.getString("cr_rsrcname");
            	int Idx = rsrcName.indexOf(".");
            	rsrcName = rsrcName.substring(0, Idx);
            	tmpObjs = getFileList(UserId, rs.getString("cr_syscd"), rs.getString("cm_sysgb"), "","", 
            									rsrcName, ReqCd,"0000", false, false, false);
            	for(int i=0;i<tmpObjs.length;i++){
            		rst = (HashMap<String,String>)tmpObjs[i];
            		
            		if(rst.get("cr_itemid") == rs.getString("cr_itemid") && rst.get("cr_rsrcname") == rs.getString("cr_rsrcname")){
            			break;
            		}
            			
            	}
            	strQuery.setLength(0);
            	strQuery.append("select b.cr_editcon, b.cr_expday, b.cr_tstchg,	b.cr_syscd, b.cr_itemid, a.cr_passok				\n");
            	strQuery.append("from cmr1000 a, cmr1010 b where a.cr_acptno = ? and a.cr_acptno = b.cr_acptno and b.cr_itemid = ? 		\n");
            	pstmt2 = conn.prepareStatement(strQuery.toString());
    			//pstmt2 =  new LoggableStatement(conn, strQuery.toString());		
    			pstmt2.setString(1, AcptNo);
    			pstmt2.setString(2, rs.getString("cr_itemid"));
    			//ecamsLogger.error(((LoggableStatement)pstmt2).getQueryString()); 
                rs2 = pstmt2.executeQuery();
                if(rs2.next())
                {
                	rst.put("cr_syscd", rs2.getString("cr_syscd"));
                	rst.put("overwrite", rs2.getString("cr_tstchg"));
                	rst.put("cr_sayu", rs2.getString("cr_editcon"));
                	rst.put("cr_expday", rs2.getString("cr_expday"));
                	rst.put("baseitemid", rs2.getString("cr_itemid"));
                	rst.put("cr_itemid", rs2.getString("cr_itemid"));
                	//rst.put("Deploy", rs2.getString("cr_passok"));
                }
                rs2.close();
                pstmt2.close();
                rsval.add(rst);
                rst = null;
            }
			rs.close();
			pstmt.close();
			conn.close();
			conn = null;
			pstmt = null;
			rs = null;
			pstmt2 = null;
			rs2 = null;
	
			rtObj =  rsval.toArray();
			//ecamsLogger.debug("+++ rsval ==="+rsval.toString());
			rsval = null;
			
			return rtObj;
			
			
		} catch (SQLException sqlexception) {
			sqlexception.printStackTrace();
			ecamsLogger.error("## Cmr0100.getReturnInfo() SQLException START ##");
			ecamsLogger.error("## Error DESC : ", sqlexception);	
			ecamsLogger.error("## Cmr0100.getReturnInfo() SQLException END ##");			
			throw sqlexception;
		} catch (Exception exception) {
			exception.printStackTrace();
			ecamsLogger.error("## Cmr0100.getReturnInfo() Exception START ##");				
			ecamsLogger.error("## Error DESC : ", exception);	
			ecamsLogger.error("## Cmr0100.getReturnInfo() Exception END ##");				
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
					ecamsLogger.error("## Cmr0100.getReturnInfo() connection release exception ##");
					ex3.printStackTrace();
				}
			}
		}
	}//end of getReturnInfo() method statement
	
	
	public boolean chkRechkOut(String AcptNo, String UserId) throws SQLException, Exception {
		Connection        conn        = null;
		PreparedStatement pstmt       = null;
		ResultSet         rs          = null;
		StringBuffer      strQuery    = new StringBuffer();
		HashMap<String, String>			  rst		  		  = null;
		//String           strDevHome   = null;
		ConnectionContext connectionContext = new ConnectionResource();
		boolean 				rtnValue = true;
		try {
			conn = connectionContext.getConnection();
			strQuery.setLength(0);
			strQuery.append("select count(*) as cnt from cmr1000 where cr_acptno = ? and cr_editor = ? and cr_status = '3' \n");
			pstmt = conn.prepareStatement(strQuery.toString());
			pstmt.setString(1,AcptNo);
			pstmt.setString(2,UserId);
			rs = pstmt.executeQuery();
			rs.next();
			int cnt = rs.getInt("cnt");
		
			if(cnt > 0){
				
				rs.close();
				pstmt.close();
				strQuery.setLength(0);
				strQuery.append("select c.cr_status as progstatus																\n");
				strQuery.append("from cmr1000 a, cmr1010 b, cmr0020 c													\n");
				strQuery.append("where a.cr_acptno = ? 																			\n");
				strQuery.append("and a.cr_acptno = b.cr_acptno																\n");
				strQuery.append("and a.cr_editor = ?																				\n");
				strQuery.append("and b.cr_itemid = c.cr_itemid																\n"); 
				strQuery.append("and b.cr_baseitem = b.cr_itemid															\n");
				pstmt = conn.prepareStatement(strQuery.toString());
				pstmt.setString(1,AcptNo);
				pstmt.setString(2,UserId);
				rs = pstmt.executeQuery();
				String  tmp = "";
				
				while (rs.next()){
					tmp = rs.getString("progstatus");
				
					if(!tmp.equals("0")){
						rtnValue = false;
						break;
					}
				}//end of while-loop statement
				
				rs.close();
				pstmt.close();
				strQuery.setLength(0);
				strQuery.append("select c.cc_status as orderstatus														\n");
				strQuery.append("from cmr1000 a, cmr1012 b,cmc0420 c 													\n");
				strQuery.append("where a.cr_acptno = ? 																	\n");
				strQuery.append("and a.cr_acptno = b.cr_acptno															\n");
				strQuery.append("and a.cr_editor = ?																	\n");
				strQuery.append("and b.cr_orderid = c.cc_orderid														\n"); 
				pstmt = conn.prepareStatement(strQuery.toString());
				pstmt.setString(1,AcptNo);
				pstmt.setString(2,UserId);
				rs = pstmt.executeQuery();
				while (rs.next()){
					tmp = rs.getString("orderstatus");
					
					if(tmp.equals("9")){
						rtnValue = false;
						
						break;
					}
				}//end of while-loop statement
				
			}
			else {
				rtnValue = false;
				
			}
			rs.close();
			pstmt.close();
			conn.close();
			conn = null;
			pstmt = null;
			rs = null;
			
	
			return rtnValue;
			
		} catch (SQLException sqlexception) {
			sqlexception.printStackTrace();
			ecamsLogger.error("## Cmr0100.chkRechkOut() SQLException START ##");
			ecamsLogger.error("## Error DESC : ", sqlexception);	
			ecamsLogger.error("## Cmr0100.chkRechkOut() SQLException END ##");			
			throw sqlexception;
		} catch (Exception exception) {
			exception.printStackTrace();
			ecamsLogger.error("## Cmr0100.chkRechkOut() Exception START ##");				
			ecamsLogger.error("## Error DESC : ", exception);	
			ecamsLogger.error("## Cmr0100.chkRechkOut() Exception END ##");				
			throw exception;
		}finally{
			if (strQuery != null) 	strQuery = null;
			if (rs != null)     try{rs.close();}catch (Exception ex){ex.printStackTrace();}
			if (pstmt != null)  try{pstmt.close();}catch (Exception ex2){ex2.printStackTrace();}
			if (conn != null){
				try{
					ConnectionResource.release(conn);
				}catch(Exception ex3){
					ecamsLogger.error("## Cmr0100.chkRechkOut() connection release exception ##");
					ex3.printStackTrace();
				}
			}
		}
	}//end of chkRechkOut() method statement
	
	public Object[] Cmr0100_Parasearch(ArrayList<HashMap<String, String>>Itemid, String userid, String syscod) throws SQLException, Exception {
		Connection        conn        = null;
		PreparedStatement pstmt       = null;
		ResultSet         rs          = null;
		StringBuffer      strQuery    = new StringBuffer();
		eCAMSInfo         ecamsinfo   = new eCAMSInfo();
		String            strFile     = "";
		String            strBinPath  = "";
		String            strTmpPath  = "";
		String            strShlFile  = "";
		int				  i			  = 0;
		String			  itemid      = "";
		
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
		String rtString = "";
		try {
			conn = connectionContext.getConnection();
			strQuery.setLength(0);
			for(i=0; i<Itemid.size(); i++){
				itemid = itemid + Itemid.get(i).get("cr_itemid") + ",";
			}
			try {
				strBinPath = ecamsinfo.getFileInfo("14");
				ErrSw = false;
				if (strBinPath == "" || strBinPath == null) {
					if(conn != null) conn.close();
					conn = null;
					throw new Exception("관리자에게 연락하여 주시기 바랍니다. (형상관리환경설정 - 실행디렉토리)");
				}
				
				strTmpPath = ecamsinfo.getFileInfo("99");
				if (strTmpPath == "" || strTmpPath == null) {
					if(conn != null) conn.close();
					conn = null;
					throw new Exception("관리자에게 연락하여 주시기 바랍니다. (형상관리환경설정 - 실행디렉토리)");
				}	
				
				strShlFile = strTmpPath + "pararsrc" + userid +".sh";
				shfile = new File(strShlFile); 
				strFile = strTmpPath + "Eff_" + userid + ".txt";
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
				}
				
				writer = new OutputStreamWriter( new FileOutputStream(strShlFile));
				writer.write("cd "+strBinPath +"\n");
				//writer.write("ecams_parmeff.sh " +   syscod + " " + itemid+ " " + userid + "\n");
//				writer.write("ecams_parmeff " + syscod + " " + itemid+ " " + userid + "\n");
				writer.write("./ecams_batexec " +rtString + " \"cd " + strBinPath+ ";" +"./ecams_parmeff " + syscod + " " + itemid+ " " + userid + "\" \n");
//				writer.write("exit $rtval\n");
				writer.write("exit $?\n");
				writer.close();
				strAry = new String[3];
				strAry[0] = "chmod";
				strAry[1] = "777";
				strAry[2] = strShlFile;
				
				run = Runtime.getRuntime();

				p = run.exec(strAry);
				p.waitFor();
								
				run = Runtime.getRuntime();
				
				strAry = new String[2];
				
				strAry[0] = "/bin/sh";
//				strAry[1] = strTmpPath + userid+".sh";
				strAry[1] = strShlFile;
				
				p = run.exec(strAry);
				p.waitFor();
				//ecamsLogger.debug("====return===="+Integer.toString(p.exitValue())+" \n");
				if (p.exitValue() != 0) {
					ErrSw = true;
					if(conn != null) conn.close();
					conn = null;
					throw new Exception("신규대상목록 추출을 위한 작업에 실패하였습니다 ");
				}
				else{
//					shfile.delete();
				}	
			} catch (Exception e) {
				if(conn != null) conn.close();
				conn = null;
				throw new Exception(e);
			} 
			if (ErrSw == false) {
				File mFile = new File(strFile);	//파일을 생성한다.
		        if (!mFile.isFile() || !mFile.exists()) {	//파일을 생성하지 못하면
		        	ErrSw = true;
//					throw new Exception("신규대상목록 추출을 위한 작업에 실패하였습니다 [작성된 파일 없음]");
		        	if(conn != null) conn.close();
					conn = null;
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
			            int x = 0;
			            int y = 0;

			            while ((str = in.readLine()) != null) {			//str 변수에 읽어온 파일을 넣고 없지 않을 때까지 반복한다.
			            	rst = new HashMap<String, String>();
			            	
			            	for(i = 0; i<13 ; i++){
				            	x = str.indexOf("\t");
				            	if(x==-1){
				            		break;
				            	}
				            	wcod = str.substring(0,x);
				            	str = str.substring(x+1);
				            	rst.put("column" + i, wcod);
			            	}
			            	lcod = str.trim();
			            	rst.put("column13", lcod);
			            	//text파일 잘라서 colum에 넣는 부분
        					rsval.add(rst);
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
    		//ecamsLogger.error("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@7");
    		return returnObjectArray;
			
		} catch (SQLException sqlexception) {
			sqlexception.printStackTrace();
			ecamsLogger.error("## Cmr0100.Cmr0100_Parasearch() SQLException START ##");
			ecamsLogger.error("## Error DESC : ", sqlexception);	
			ecamsLogger.error("## Cmr0100.Cmr0100_Parasearch() SQLException END ##");			
			throw sqlexception;
		} catch (Exception exception) {
			exception.printStackTrace();
			ecamsLogger.error("## Cmr0100.Cmr0100_Parasearch() Exception START ##");				
			ecamsLogger.error("## Error DESC : ", exception);	
			ecamsLogger.error("## Cmr0100.Cmr0100_Parasearch() Exception END ##");
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
					ecamsLogger.error("## Cmr0100.Cmr0100_Parasearch() connection release exception ##");
					ex3.printStackTrace();
				}
			}
		}
	}//end of Cmr0100_Parasearch() method statement
	
	public Object[] cmr0100_ParaSearch_Search(ArrayList<HashMap<String, String>>Itemid) throws SQLException, Exception {
		Connection        conn        = null;
		PreparedStatement pstmt       = null;
		ResultSet         rs          = null;
		String            strDevHome  = null;
		StringBuffer      strQuery    = new StringBuffer();
		Object[]		  rtObj		  = null;
		ArrayList<HashMap<String, String>>		  svrList	  = new ArrayList<HashMap<String, String>>();
		ArrayList<HashMap<String, String>>		  rtList	  = new ArrayList<HashMap<String, String>>();
		HashMap<String, String>			  rst		  = null;
		
		ConnectionContext connectionContext = new ConnectionResource();
		
		try {
			conn = connectionContext.getConnection();
			int				  pstmtcount  = 1;
			String			  szDsnCD = "";
			String			  strRsrcCd = "";
			String            strRsrc[] = null;
			String            strVolPath = "";
			String            strDirPath = "";
			int               i = 0;
			int 			  parmCnt = 0;
			
			//UserInfo     userinfo = new UserInfo();
			boolean adminYn = false;//userinfo.isAdmin(UserId);
			//userinfo = null;
			SysInfo sysinfo = new SysInfo();
			rtList.clear();
			
			for(i=0;i<Itemid.size();i++){
				parmCnt = 0;
				strQuery.setLength(0);
				strQuery.append("select /*+ ALL ROWS */                                    								\n");
				strQuery.append("       a.CR_RSRCNAME,a.CR_STORY,a.CR_LSTVER,a.cr_acptno,  								\n");
				strQuery.append("       TO_CHAR(a.CR_LASTDATE,'yyyy-mm-dd') as LASTDT,     								\n");
				strQuery.append("       a.CR_SYSCD,a.CR_RSRCCD,a.CR_LANGCD,a.CR_DSNCD,     								\n");
				strQuery.append("       a.CR_JOBCD,a.CR_FILESIZE,a.CR_FILEDATE,a.cr_nomodify,							\n");
				strQuery.append("       a.CR_ITEMID,a.CR_STATUS,c.CM_CODENAME as CODENAME, 								\n");
				strQuery.append("       d.CM_USERNAME,e.CM_DIRPATH, f.CM_CODENAME as JAWON,								\n");
				strQuery.append("       b.CM_INFO,g.cm_jobname,a.cr_orderid,                							\n");
				strQuery.append("       REPLACE(e.CM_DIRPATH, (SELECT CM_VOLPATH FROM CMM0038  							\n");
				strQuery.append("       WHERE CM_SYSCD = A.CR_SYSCD AND CM_RSRCCD = A.CR_RSRCCD AND CM_SVRCD = (select cm_dirbase from cmm0030 where cm_syscd = A.cr_syscd)),	\n");
				strQuery.append("       (SELECT CM_VOLPATH FROM CMM0038 WHERE CM_SYSCD = A.CR_SYSCD 					\n");
				strQuery.append("       AND CM_RSRCCD = A.CR_RSRCCD AND CM_SVRCD = '03')) as cm_dirpath2				\n");
				strQuery.append("  from cmm0020 f,cmm0070 e,cmm0040 d,cmm0020 c,cmm0036 b, 		\n"); 
				strQuery.append("       cmr0020 a ,cmm0102 g 									\n"); 
				strQuery.append("  where a.cr_editor=d.cm_userid and a.cr_jobcd = g.cm_jobcd and    \n");
				strQuery.append("c.cm_macode='CMR0020' and a.cr_status=c.cm_micode and      	\n");
				strQuery.append("f.cm_macode='JAWON' and a.cr_rsrccd=f.cm_micode and        	\n");
				strQuery.append("a.cr_syscd=b.cm_syscd and a.cr_rsrccd=b.cm_rsrccd and      	\n");	
				strQuery.append("a.cr_dsncd = e.cm_dsncd and a.cr_syscd = e.cm_syscd and		\n");
				strQuery.append("a.cr_itemid = ?												\n");
				
	            pstmt = conn.prepareStatement(strQuery.toString());	
//	            pstmt = new LoggableStatement(conn,strQuery.toString());
	    		
	    		pstmt.setString(++parmCnt, Itemid.get(i).get("ItemID"));
	    		//ecamsLogger.error("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@2");
//	    		ecamsLogger.error(((LoggableStatement)pstmt).getQueryString());
	    		rs = pstmt.executeQuery();
				while (rs.next()){
					//ecamsLogger.error("$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$3");
					rst = new HashMap<String,String>();
					rst.put("ID", Integer.toString(rs.getRow()));
					rst.put("cm_dirpath",rs.getString("cm_dirpath"));
					rst.put("cm_dirpath2",rs.getString("cm_dirpath2"));
					rst.put("cr_rsrcname",rs.getString("cr_rsrcname"));
					/*
					rst.put("cr_story",rs.getString("cr_story"));
					*/
					rst.put("jobname", rs.getString("cm_jobname"));
					rst.put("jawon",rs.getString("jawon"));
	//				if (ReqCd.equals("02")){
	//					rst.put("cr_lstver","sel");
	//				}
	//				else{
						rst.put("cr_lstver",rs.getString("cr_lstver"));
	//				}
					rst.put("cm_username",rs.getString("cm_username"));
					rst.put("lastdt",rs.getString("lastdt"));
					rst.put("codename",rs.getString("codename"));
					rst.put("cr_syscd",rs.getString("cr_syscd"));
					rst.put("cr_rsrccd",rs.getString("cr_rsrccd"));
					rst.put("cr_langcd",rs.getString("cr_langcd"));
					rst.put("cr_dsncd",rs.getString("cr_dsncd"));
					rst.put("cr_jobcd",rs.getString("cr_jobcd"));
					/*
					rst.put("cr_filesize",rs.getString("cr_filesize"));
					rst.put("cr_filedate",rs.getString("cr_filedate"));
					*/
					rst.put("cr_itemid",rs.getString("cr_itemid"));
					rst.put("cr_status",rs.getString("cr_status"));
					rst.put("selAcptno",rs.getString("cr_acptno"));
					rst.put("cr_orderid",rs.getString("cr_orderid"));
					if (rs.getString("cr_nomodify") != null)
						rst.put("cr_nomodify",rs.getString("cr_nomodify"));
					else
						rst.put("cr_nomodify","0");
						rst.put("cm_info",rs.getString("cm_info"));
						rst.put("selected_flag","0");
					
					if ((rs.getString("cm_info").substring(44,45).equals("1")  || 
					     rs.getString("cm_info").substring(37,38).equals("1")) && strDevHome != null) {
						for (i=0;svrList.size()>i;i++) {
							if (svrList.get(i).get("cm_rsrccd").equals(rs.getString("cr_rsrccd"))) {
								strVolPath = svrList.get(i).get("cm_volpath");
								strDirPath = rs.getString("cm_dirpath");
								if (strVolPath != null && strVolPath != "") {
									if(strDirPath.length() >= strVolPath.length()){
										if (strDirPath.substring(0,strVolPath.length()).equals(strVolPath)) {
											rst.put("pcdir", strDevHome + strDirPath.substring(strVolPath.length()).replace("/","\\"));
										}
									}
								} else {
									rst.put("pcdir", strDevHome + rs.getString("cm_dirpath").replace("/", "\\"));
								}
							}
						}
						rst.put("pcdir1", rst.get("pcdir"));
					}
					rtList.add(rst);
					//ecamsLogger.error("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@"+rtList.toString());
					rst = null;
				}//end of while-loop statement
			}//end for문
			rs.close();
			pstmt.close();
			conn.close();
			
			rs = null;
			pstmt = null;
			conn = null;
			
			rtObj =  rtList.toArray();
			rtList = null;
			//ecamsLogger.error("//////////////////////////////////////////////////////////////////////");
			
			return rtObj;
			
		} catch (SQLException sqlexception) {
			sqlexception.printStackTrace();
			ecamsLogger.error("## Cmr0100.cmr0100_ParaSearch_Search() SQLException START ##");
			ecamsLogger.error("## Error DESC : ", sqlexception);	
			ecamsLogger.error("## Cmr0100.cmr0100_ParaSearch_Search() SQLException END ##");			
			throw sqlexception;
		} catch (Exception exception) {
			exception.printStackTrace();
			ecamsLogger.error("## Cmr0100.cmr0100_ParaSearch_Search() Exception START ##");				
			ecamsLogger.error("## Error DESC : ", exception);	
			ecamsLogger.error("## Cmr0100.cmr0100_ParaSearch_Search() Exception END ##");				
			throw exception;
		}finally{
			if (strQuery != null) 	strQuery = null;
			if (rtObj != null)  rtObj = null;
			if (rs != null)     try{rs.close();}catch (Exception ex){ex.printStackTrace();}
			if (pstmt != null)  try{pstmt.close();}catch (Exception ex2){ex2.printStackTrace();}
			if (conn != null){
				try{
					ConnectionResource.release(conn);
				}catch(Exception ex3){
					ecamsLogger.error("## Cmr0100.cmr0100_ParaSearch_Search() connection release exception ##");
					ex3.printStackTrace();
				}
			}
		}
	}//end cmr0100_ParaSearch_Search
	public Object[] Gitlab_Export_ChgFile(HashMap<String, String> gitData) throws SQLException, Exception {
		Connection        conn        = null;
		PreparedStatement pstmt       = null;
		ResultSet         rs          = null;
		StringBuffer      strQuery    = new StringBuffer();

		ArrayList<HashMap<String, String>>  rsval = new ArrayList<HashMap<String, String>>();
		HashMap<String, String>			  rst		  = null;	
		
		String            strTmpPath  = "";
		String			  parmName    = "";
		String            shlName     = "";
		String            retMsg      = "";
		String            outFile     = "";
				
		int               parmCnt     = 0;		
		File             rstFile      = null;
		
		ConnectionContext connectionContext = new ConnectionResource();
		try {		
			
			SystemPath systempath = new SystemPath();
			strTmpPath = systempath.getTmpDir("99");
			outFile = strTmpPath + "/" + gitData.get("syscd") + "_" + gitData.get("jobcd") + "_" + gitData.get("userid") + "_"+gitData.get("reqcd")+".GIT";
			Cmm1600 cmm1600 = new Cmm1600();
			parmName = "./ecams_gitlab_export "+gitData.get("syscd")+" "+gitData.get("jobcd")+" "+gitData.get("userid")+" "+gitData.get("reqcd");
			shlName = gitData.get("syscd") + "_" + gitData.get("userid") + " "+gitData.get("reqcd") + "_gitcmd.sh";
			int retCd = cmm1600.execShell(shlName, parmName, false);
			if (retCd != 0) {
				retMsg = "GITLAB Command 수행 중 오류가 발생하였습니다.[command="+parmName+"] result=["+retCd+"]";
			} else {
				rstFile = new File(outFile);
				
				if (!rstFile.isFile() && !rstFile.exists()) {
					retMsg = "GIBLAB의 수정프로그램 목록작성결과가 없습니다.["+outFile+"]";
				}
			}
		    rsval.clear();
			if (retMsg.length()==0) {	
				conn = connectionContext.getConnection();
				
		        BufferedReader in = null;
		        try {
		            in = new BufferedReader(new FileReader(rstFile));	//파일을 읽어온다
		            String str = null;
	
		            while ((str = in.readLine()) != null) {			//str 변수에 읽어온 파일을 넣고 없지 않을 때까지 반복한다.
		            	if (str.trim().length()==0) continue;
		            	if (str.trim().length()!=12) continue;
		            	
		            	parmCnt = 0;
		            	strQuery.setLength(0);
		        		strQuery.append("select a.CR_RSRCNAME,a.CR_STORY,a.CR_LSTVER,a.cr_acptno,  								\n");
		        		strQuery.append("       TO_CHAR(a.CR_LASTDATE,'yyyy-mm-dd') as LASTDT,     								\n");
		        		strQuery.append("       a.CR_SYSCD,a.CR_RSRCCD,a.CR_LANGCD,a.CR_DSNCD,     								\n");
		        		strQuery.append("       a.CR_JOBCD,a.CR_FILESIZE,a.CR_FILEDATE,a.cr_nomodify,							\n");
		        		strQuery.append("       a.CR_ITEMID,a.CR_STATUS,           								                \n");
		        		strQuery.append("       (select CM_CODENAME from cmm0020 where cm_macode='CMR0020' and cm_micode=a.cr_status) CODENAME,\n");
		        		strQuery.append("       (select CM_USERNAME from cmm0040 where cm_userid=a.cr_editor) CM_USERNAME,                     \n");
		        		strQuery.append("       (select CM_CODENAME from cmm0020 where cm_macode='JAWON' and cm_micode=a.cr_rsrccd) JAWON,     \n");
		        		strQuery.append("       (select CM_DIRPATH from cmm0070 where cm_syscd=a.cr_syscd and cm_dsncd=a.cr_dsncd) CM_DIRPATH, \n");
		        		strQuery.append("       (select cm_jobname from cmm0102 where cm_jobcd=a.cr_jobcd) cm_jobname,          \n");
		        		strQuery.append("       b.CM_INFO,a.cr_orderid                							                \n");
		        		strQuery.append("  from cmm0044 h,cmm0036 b,cmr0020 a    \n"); 
		        		strQuery.append(" where a.cr_syscd = ?                   \n");
		        		strQuery.append("   and a.cr_itemid = ?                  \n");
		        		strQuery.append("   and a.cr_syscd=h.cm_syscd            \n");
		        		strQuery.append("   and a.cr_jobcd=h.cm_jobcd            \n");
		        		strQuery.append("   and h.cm_userid=?                    \n");
		        		strQuery.append("   and h.cm_closedt is null             \n");
		        		strQuery.append("   and a.cr_syscd=b.cm_syscd            \n");
		        		strQuery.append("   and a.cr_rsrccd=b.cm_rsrccd          \n");
		        		pstmt = conn.prepareStatement(strQuery.toString());	
		        		pstmt =  new LoggableStatement(conn, strQuery.toString());	                
		                pstmt.setString(++parmCnt, gitData.get("syscd"));                
		                pstmt.setString(++parmCnt, str.trim());	                
		                pstmt.setString(++parmCnt, gitData.get("userid"));   
		                ecamsLogger.error(((LoggableStatement)pstmt).getQueryString());
		                rs = pstmt.executeQuery();	                
		        		while (rs.next()){	        			
		        			rst = new HashMap<String,String>();
		        			rst.put("ID", Integer.toString(rs.getRow()));
		        			rst.put("cm_dirpath",rs.getString("cm_dirpath"));
		        			rst.put("cm_dirpath2",rs.getString("cm_dirpath"));
		        			rst.put("cr_rsrcname",rs.getString("cr_rsrcname"));
		        			rst.put("jobname", rs.getString("cm_jobname"));
		        			rst.put("jawon",rs.getString("jawon"));
		        			rst.put("cr_lstver",rs.getString("cr_lstver"));
		        			rst.put("cm_username",rs.getString("cm_username"));
		        			rst.put("lastdt",rs.getString("lastdt"));
		        			rst.put("sysgb",gitData.get("sysgb"));
		        			rst.put("cr_syscd",rs.getString("cr_syscd"));
		        			rst.put("cr_rsrccd",rs.getString("cr_rsrccd"));
		        			rst.put("cr_langcd",rs.getString("cr_langcd"));
		        			rst.put("cr_dsncd",rs.getString("cr_dsncd"));
		        			rst.put("cr_jobcd",rs.getString("cr_jobcd"));
		        			rst.put("cr_itemid",rs.getString("cr_itemid"));
		        			rst.put("cr_status",rs.getString("cr_status"));
		        			rst.put("selAcptno",rs.getString("cr_acptno"));
		        			rst.put("cr_orderid",rs.getString("cr_orderid"));
		        			if (rs.getString("cr_nomodify") != null)
		        				rst.put("cr_nomodify",rs.getString("cr_nomodify"));
		        			else
		        				rst.put("cr_nomodify","0");
	        				rst.put("cm_info",rs.getString("cm_info"));
	        				rst.put("selected_flag","0");
		        			rst.put("codename",rs.getString("codename"));
		        			rsval.add(rst);
		        			rst = null;
		        		}
		        		rs.close();
		        		pstmt.close();
		            }
		        } finally {
		            if (in != null)
		                in.close();
		        }
			}
			
			if (conn != null) conn.close();
			
			if (retMsg.length()>0) {
				rst = new HashMap<String,String>();
				rst.put("errmsg", retMsg);
				rsval.add(rst);
				rst = null;
			}
			rs = null;
			pstmt = null;
			conn = null;
			
			return rsval.toArray();		
		} catch (SQLException sqlexception) {
			sqlexception.printStackTrace();
			ecamsLogger.error("## Cmr0100.Gitlab_Export_ChgFile() SQLException START ##");
			ecamsLogger.error("## Error DESC : ", sqlexception);	
			ecamsLogger.error("## Cmr0100.Gitlab_Export_ChgFile() SQLException END ##");			
			throw sqlexception;
		} catch (Exception exception) {
			exception.printStackTrace();
			ecamsLogger.error("## Cmr0100.Gitlab_Export_ChgFile() Exception START ##");				
			ecamsLogger.error("## Error DESC : ", exception);	
			ecamsLogger.error("## Cmr0100.Gitlab_Export_ChgFile() Exception END ##");
			throw exception;
		}finally{ 
			if (rsval != null)	rsval = null;
			if (strQuery != null)	strQuery = null;
			if (rs != null)     try{rs.close();}catch (Exception ex){ex.printStackTrace();}
			if (pstmt != null)  try{pstmt.close();}catch (Exception ex2){ex2.printStackTrace();}
			if (conn != null){
				try{
					ConnectionResource.release(conn);
				}catch(Exception ex3){
					ecamsLogger.error("## Cmr0100.Gitlab_Export_ChgFile() connection release exception ##");
					ex3.printStackTrace();
				}
			}
		}
	}
}
