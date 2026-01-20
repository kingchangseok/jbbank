
/*****************************************************************************************
	1. program ID	: GibLabInfo.java
	2. create date	: 2007. 07. 23
	3. auth		    : k.m.s
	4. update date	: 
	5. auth		    : 
	6. description	: eCAMS Information
*****************************************************************************************/

package app.common;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import org.apache.logging.log4j.Logger;
import com.ecams.common.dbconn.ConnectionContext;
import com.ecams.common.dbconn.ConnectionResource;
import com.ecams.common.logger.EcamsLogger;
import com.google.gson.JsonParser;

import app.eCmm.Cmm1600;


/**
 * @author bigeyes
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */


public class GibLabInfo{
	
    /**
     * Logger Class Instance Creation
     * logger
     */
    Logger ecamsLogger  = EcamsLogger.getLoggerInstance();
    
	/**
	 * 게시판을 조회합니다.(구분값을 주어)
	 * @param  gbn
	 * @return ArrayList
	 * @throws SQLException
	 * @throws Exception
	 */
    

	public Object[] gitExecCmd(HashMap<String,String> gitData) throws SQLException, Exception {
		Connection        conn        = null;
		PreparedStatement pstmt       = null;
		ResultSet         rs          = null;
		StringBuffer      strQuery    = new StringBuffer();
		ArrayList<HashMap<String, String>>  rsval = new ArrayList<HashMap<String, String>>();
		ArrayList<HashMap<String, String>>  gitList = new ArrayList<HashMap<String, String>>();
		HashMap<String, String>			  rst		  = null;
		String            gitURL      = "";
		String            gitToken    = "";
		String            gitPrjID    = "";
		String            gitBaseBR   = "";
		String            gitHomeDir  = "";
		String            gitCommand  = "";
		String            gitBaseCmd  = "";
		String            gitSubCmd   = "";
		String            retMsg      = "";
		String            shlName     = "";
		String            outData     = "";
		String            outFile     = "";
		String            tarFile     = "";
		File              outf        = null;
		int               i           = 0;
		ConnectionContext connectionContext = new ConnectionResource();
		
		try {
			conn = connectionContext.getConnection();
			
			SystemPath systempath = new SystemPath();
			String tmpPath = systempath.getTmpDir_conn("99", conn);
			systempath = null;
			
			gitList.clear();
			rsval.clear();
			strQuery.setLength(0);
			strQuery.append("select cm_svrip,cm_port,cm_svrusr token \n");
			strQuery.append("  from cmm0039                 \n");		
			strQuery.append(" where cm_syscd=?              \n");		
			strQuery.append("   and cm_dircd='RI'           \n");		
            pstmt = conn.prepareStatement(strQuery.toString());	
            pstmt.setString(1, gitData.get("syscd"));	
            rs = pstmt.executeQuery();
			if (rs.next()){
				gitURL = "http://"+rs.getString("cm_svrip")+":"+rs.getString("cm_port");
				gitToken = rs.getString("token");
			}//end of while-loop statement				
			rs.close();
			pstmt.close();
			
			if (gitURL.length() == 0) retMsg = "GIBLAB서버정보등록 후 진행하시기 바랍니다. [관리자>시스템정보>공통디렉토리]";
			else {
				/*
				 *  alter table cmm0034
 add (CM_PRJNM   varchar2(100),
      CM_PRJID   varchar2(10),
      CM_BRANCH  varchar2(50));
				 */
				strQuery.setLength(0);
				strQuery.append("select cm_jobcd,cm_prjnm,cm_prjid,cm_branch \n");
				strQuery.append("  from cmm0034                 \n");		
				strQuery.append(" where cm_syscd=?              \n");		
				strQuery.append("   and cm_closedt is null      \n");		
				strQuery.append("   and cm_prjnm is not null    \n");		
	            pstmt = conn.prepareStatement(strQuery.toString());	
	            pstmt.setString(1, gitData.get("syscd"));	
	            rs = pstmt.executeQuery();
				if (rs.next()){
					rst = new HashMap<String,String>();
					rst.put("cm_jobcd", rs.getString("cm_jobcd"));
					rst.put("cm_prjnm", rs.getString("cm_prjnm"));
					rst.put("cm_prjid", rs.getString("cm_prjid"));
					rst.put("cm_branch", rs.getString("cm_branch"));
					gitList.add(rst);
					rst = null;
				}//end of while-loop statement				
				rs.close();
				pstmt.close();
			}
			if (gitList.size()==0) {
				retMsg = "업무별 GIBLAB정보등록 후 진행하시기 바랍니다. [관리자>시스템정보>업무등록]";
			} else {
				for (i=0;gitList.size()>i;i++) {
					gitBaseCmd = "curl --header \"PRIVATE-TOKEN:"+gitToken+"\" \""+gitURL+"/api/v4/";
					
					if ("EXPORT".equals(gitData.get("cmdgbn"))) {
						tarFile = tmpPath + "/" + gitData.get("syscd") + "_" + gitData.get("userid") + "_" + gitList.get(i).get("prjid")+".tar";
						gitSubCmd = "projects/"+gitList.get(i).get("cm_prjid")+"/repository/archive.tar?ref="+gitList.get(i).get("cm_branch")+"\" --output "+tarFile;								
					}
					gitCommand = gitBaseCmd + gitSubCmd;
					
					shlName = gitData.get("syscd") + "_" + gitData.get("userid") + "gitcmd.sh";
					Cmm1600 cmm1600 = new Cmm1600();
					int retCd = cmm1600.execShell(shlName, gitCommand, true);
					if (retCd != 0) {
						retMsg = "GITLAB Command 수행 중 오류가 발생하였습니다.[command="+gitCommand+"] result=["+retCd+"]";
					}
					if (retMsg.length()==0) {
						ByteArrayOutputStream fileStream = null;
						byte[] byteTmpBuf = null;
						FileInputStream fis = null;
						int nCnt = 0;
						
						JsonParser jsonparser = new JsonParser();
						outFile = tmpPath + "/" + shlName.replace(".sh", ".out");
						outf = new File(outFile);
						if (outf.isFile() && outf.exists()) {
							fileStream = new ByteArrayOutputStream();
							byteTmpBuf = new byte[8192];
							fis = new FileInputStream(outFile);
							
							while ((nCnt=fis.read(byteTmpBuf))>-1) {
								fileStream.write(byteTmpBuf,0,nCnt);
							}
							fis.close();
							
							outData = fileStream.toString("EUC-KR");
							if (outData.length()>2 && ("[4".equals(outData.substring(0,2)) || "[5".equals(outData.substring(0,2)))) {
								retMsg = outData;
							}
						}
					}
					
					if (retMsg.length()==0) {
						if ("EXPORT".equals(gitData.get("cmdgbn"))) {
							outf = new File(tarFile);
							if (outf.isFile() && outf.exists()) {
								rst = new HashMap<String,String>();
								rst = gitList.get(i);
								
								// rsval = gitExportFile(rst);
							}
						}
					}
				}
			}
			
			
			conn.close();
			
			return null;
			
		} catch (SQLException sqlexception) {
			sqlexception.printStackTrace();
			ecamsLogger.error("## GibLabInfo.getFileInfo() SQLException START ##");
			ecamsLogger.error("## Error DESC : ", sqlexception);	
			ecamsLogger.error("## GibLabInfo.getFileInfo() SQLException END ##");			
			throw sqlexception;
		} catch (Exception exception) {
			exception.printStackTrace();
			ecamsLogger.error("## GibLabInfo.getFileInfo() Exception START ##");				
			ecamsLogger.error("## Error DESC : ", exception);	
			ecamsLogger.error("## GibLabInfo.getFileInfo() Exception END ##");				
			throw exception;
		}finally{
			if (strQuery != null) 	strQuery = null;
			if (rs != null)     try{rs.close();}catch (Exception ex){ex.printStackTrace();}
			if (pstmt != null)  try{pstmt.close();}catch (Exception ex2){ex2.printStackTrace();}
			if (conn != null){
				try{
					ConnectionResource.release(conn);
				}catch(Exception ex3){
					ecamsLogger.error("## GibLabInfo.getFileInfo() connection release exception ##");
					ex3.printStackTrace();
				}
			}
		}
	}//end of getFileInfo() method statement

	public String getFileInfo_conn(String GbnCd,Connection conn) throws SQLException, Exception {
		PreparedStatement pstmt       = null;
		ResultSet         rs          = null;
		StringBuffer      strQuery    = new StringBuffer();
		String            strPath     = null;
		
		try {
			strQuery.setLength(0);
			strQuery.append("select cm_path from cmm0012                     \n");
			strQuery.append("where cm_pathcd=?                               \n");		
            pstmt = conn.prepareStatement(strQuery.toString());	
            pstmt.setString(1, GbnCd);	
            rs = pstmt.executeQuery();
			if (rs.next()){
				strPath = rs.getString("cm_path");
				if (!strPath.substring(strPath.length() - 1, strPath.length()).equals("/"))
				    strPath = strPath + "/";	
			}//end of while-loop statement	
			
			rs.close();
			pstmt.close();
			rs = null;
			pstmt = null;
			
			return strPath;
			
			
		} catch (SQLException sqlexception) {
			sqlexception.printStackTrace();
			ecamsLogger.error("## GibLabInfo.getFileInfo_conn() SQLException START ##");
			ecamsLogger.error("## Error DESC : ", sqlexception);	
			ecamsLogger.error("## GibLabInfo.getFileInfo_conn() SQLException END ##");			
			throw sqlexception;
		} catch (Exception exception) {
			exception.printStackTrace();
			ecamsLogger.error("## GibLabInfo.getFileInfo_conn() Exception START ##");				
			ecamsLogger.error("## Error DESC : ", exception);	
			ecamsLogger.error("## GibLabInfo.getFileInfo_conn() Exception END ##");				
			throw exception;
		}finally{
			if (strQuery != null) 	strQuery = null;
			if (rs != null)     try{rs.close();}catch (Exception ex){ex.printStackTrace();}
			if (pstmt != null)  try{pstmt.close();}catch (Exception ex2){ex2.printStackTrace();}
		}
	}//end of getFileInfo_conn() method statement
	
}//end of GibLabInfo class statement
