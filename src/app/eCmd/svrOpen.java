/*****************************************************************************************
	1. program ID	: Cmd0101.java
	2. create date	: 2006.08. 08
	3. auth		    : is.choi
	4. update date	: 
	5. auth		    : 
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
import org.w3c.dom.Document;

import app.common.CreateXml;
import app.common.DeepCopy;
import app.common.LoggableStatement;
import app.common.StreamGobbler;
import app.common.UserInfo;
import app.common.eCAMSInfo;
import app.thread.ThreadPool;

import com.ecams.common.dbconn.ConnectionContext;
import com.ecams.common.dbconn.ConnectionResource;
import com.ecams.common.logger.EcamsLogger;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * @author bigeyes
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class svrOpen{     

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

	public Object[] getSvrDir(String UserID,String SysCd,String SvrIp,String SvrPort,String BufSize,String BaseDir,String AgentDir,
			String SysOs,String HomeDir,String svrName) throws SQLException, Exception {
		eCAMSInfo         ecamsinfo   = new eCAMSInfo();
		CreateXml         ecmmtb      = new CreateXml();
		ArrayList<HashMap<String, String>>  rsval = new ArrayList<HashMap<String, String>>();
		HashMap<String, String>			  rst		  = null;
		String[]          pathDepth   = null;
		String            strDir      = null;
		boolean           findSw      = false;
		boolean           fileSw      = false;
		boolean           ErrSw      = false;
		String            strBinPath  = "";
		String            strTmpPath  = "";
		String            strFile     = "";
		String            makeFile    = "";
		String            strBaseDir  = "";
		int               upSeq       = 0; 
		int               maxSeq      = 0;
		boolean           dirSw       = false;
		File shfile=null;
		OutputStreamWriter writer = null;
		String[] strAry = null;
		Runtime  run = null;
		Process p = null;
		ArrayList<Document> list = null;
		Object[] returnObjectArray = null;
		
		rsval.clear();
		Connection        conn        = null;
		ResultSet         rs          = null;
		PreparedStatement pstmt       = null;
		StringBuffer      strQuery    = new StringBuffer();
		ConnectionContext connectionContext = new ConnectionResource();
		String rtString = "";
		try {
			strBinPath = ecamsinfo.getFileInfo("14");
			ErrSw = false;
			if (strBinPath == "" || strBinPath == null)
				throw new Exception("관리자에게 연락하여 주시기 바랍니다. (형상관리환경설정 - 실행디렉토리)");
	
			strTmpPath = ecamsinfo.getFileInfo("99");
			if (strTmpPath == "" || strTmpPath == null) 
				throw new Exception("관리자에게 연락하여 주시기 바랍니다. (형상관리환경설정 - 실행디렉토리)");
			
			if (HomeDir == null) HomeDir = "";
			
			try {

				shfile = new File(strTmpPath);
								
				if (!shfile.isFile()){
					shfile.mkdirs();
				}

				if (SysOs.equals("03")) {
					BaseDir = AgentDir + BaseDir;
				}
				
				shfile=null;
				makeFile = "dir" + UserID;
				strFile = strTmpPath + makeFile;				
				shfile = new File(strTmpPath + makeFile+".sh");              //File 불러온다.

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
				
				writer = new OutputStreamWriter( new FileOutputStream(strTmpPath + makeFile+".sh"));
				writer.write("cd "+strBinPath +"\n");
//				writer.write("./ecams_dir " + SvrIp + " " + SvrPort + " " + BufSize + " " + BaseDir + " " + makeFile +"\n");
				writer.write("./ecams_batexec " +rtString + " \"cd " + strBinPath+ ";" +"./ecams_dir " + SvrIp + " " + SvrPort + " " + BufSize + " " + BaseDir + " " + makeFile + "\" \n");
				writer.write("exit $?\n");
				writer.close();

				strAry = new String[3];
				
				strAry[0] = "chmod";
				strAry[1] = "777";
				strAry[2] = strTmpPath + makeFile+".sh";
				
				run = Runtime.getRuntime();
				
				p = run.exec(strAry);
				p.waitFor();
								
				run = Runtime.getRuntime();

				strAry = new String[2];				
				strAry[0] = "/bin/sh";
				strAry[1] = strTmpPath + makeFile+".sh";
				
				p = run.exec(strAry);
				StreamGobbler outgb = new StreamGobbler(p.getInputStream());
				StreamGobbler errgb = new StreamGobbler(p.getErrorStream());

				outgb.start();
				errgb.start();
				p.waitFor();
				
				//ecamsLogger.debug("====return===="+Integer.toString(p.exitValue())+" \n");
				if (p.exitValue() != 0) {
					if (p.exitValue() == 1) {
						throw new Exception("추출 디렉토리가 없습니다");
					}else if (p.exitValue() == 2) {
						throw new Exception("디렉토리추출을 위한 분석작업 실패하였습니다");
					}else if (p.exitValue() == 3) {	
						throw new Exception("해당서버에서 Tmp파일 삭제  실패하였습니다");
					}
					
					ErrSw = true;
					
				}
				else{
					shfile.delete();
				}
				outgb = null;
				errgb = null;
			} catch (Exception e) {
				throw new Exception(e);
			}
			
			//strFile = "c:\\dir20020206";
            if (ErrSw == false) {
            	if (BaseDir.indexOf(":") >= 0) {			                			
        			BaseDir = BaseDir.substring(BaseDir.indexOf(":")+1);
        		}
    			BaseDir = BaseDir.replace("\\", "");
    			if (BaseDir.substring(BaseDir.length()-1).equals("/")) BaseDir = BaseDir.substring(0,BaseDir.length()-1);
				File mFile = new File(strFile);
		        if (!mFile.isFile() || !mFile.exists()) {
					ErrSw = true;
					throw new Exception("디렉토리추출을 위한 작업에 실패하였습니다 [작성된 파일 없음]");
		        } else {
			        BufferedReader in = null;
			        fileSw = true;
			        //PrintWriter out = null;
			        
			        try {
			            //in = new BufferedReader(new FileReader(mFile));
			            in = new BufferedReader(new InputStreamReader(new FileInputStream(mFile),"MS949"));
			            ecmmtb.init_Xml("ID","cm_seqno","cm_dirpath","cm_upseq","cm_fullpath","cm_dsncd","isBranch");
			            String str = null;
			            //strBaseDir = BaseDir;

						maxSeq = maxSeq + 1;
                        
						//ecamsLogger.error("dirpath,full,upseq,seq====>" + pathDepth[i] + "  , " + strDir  + "  , " + Integer.toString(upSeq)  + "  , " + Integer.toString(maxSeq));
						rst = new HashMap<String,String>();
						rst.put("cm_dirpath","["+svrName+"]"+HomeDir);
						rst.put("cm_fullpath",HomeDir);
						rst.put("cm_upseq","0");
						rst.put("cm_seqno",Integer.toString(maxSeq));
						rsval.add(maxSeq - 1, rst); 
						upSeq = maxSeq;
						
			            while ((str = in.readLine()) != null) {
			                if (str.length() > 0) {
			                	dirSw = false;
			                	if (SysOs.equals("03")) {
			                		str = str.trim();
			                		str = str.replace("\\", "/");
			                		if (str.indexOf(":") >= 0) {			                			
			                			str = str.substring(str.indexOf(":")+1);
			                		}
			                				
			                		if (str.indexOf("디렉터리")>0) {
			                			if (str.substring(0,BaseDir.length()).equals(BaseDir)) {			                				
			                				strBaseDir = str.substring(0,str.indexOf("디렉터리"));
				                			strBaseDir = strBaseDir.trim();
				                			//ecamsLogger.error("+++++++str,basedir2+++++++++"+str+", "+ strBaseDir);
				                			/*
				                			strBaseDir = "/" + strBaseDir.substring(AgentDir.length());
				                			strBaseDir = strBaseDir.substring(strBaseDir.indexOf(":")+1);
				                			do {
				                				strBaseDir = strBaseDir.replace("\\", "/");
				                			} while (strBaseDir.indexOf("\\")>=0);
				                			*/
				                			
				                			str = strBaseDir;
				                			dirSw = true;
			                			}		
			                		} else {
			                			//ecamsLogger.error("+++++++str,basedir2+++++++++"+str+", "+ BaseDir);
			                		}
			                		
			                	} else {			                	
				                	if (str.substring(str.length() - 1).equals(":")) {
				                		strBaseDir = str.substring(0,str.length() - 1);
				                		//if (!strBaseDir.substring(strBaseDir.length() - 1).equals("/")) strBaseDir = strBaseDir + "/"; 
				                		str = strBaseDir;
				                		dirSw = true;
				                	}
				                	/*
				                	else {	
				                		if (str.substring(str.length() - 1).equals("/")) str = str.substring(0,str.length() - 1);			                		
				                		str = strBaseDir + str;
				                		//str = str.replace(HomeDir, "/" + SysMsg);
				                		dirSw = true;
				                	}
				                	*/
			                	}
			                	if (dirSw == true) {
			                		//ecamsLogger.error("+++++++str1+++++++++"+str);
			                		if (HomeDir.length() < str.length()){
				                		str = str.substring(HomeDir.length());
				                		//ecamsLogger.error("+++++++str2+++++++++"+str);
				                		if (str.length() != 0 ) {
					                		//ecamsLogger.error("+++++++str3+++++++++"+str);
					                		pathDepth = str.substring(1).split("/");
					                		strDir = HomeDir;
											upSeq = 1;
											findSw = false;
											for (int i = 0;pathDepth.length > i;i++) {
												if (pathDepth[i].length() > 0) {
													if (strDir.length() > 1 ) {
														strDir = strDir + "/";
													}
													strDir = strDir + pathDepth[i];
													findSw = false;
													if (rsval.size() > 0) {
														for (int j = 0;rsval.size() > j;j++) {
															if (rsval.get(j).get("cm_fullpath").equals(strDir)) {
																upSeq = Integer.parseInt(rsval.get(j).get("cm_seqno"));
																findSw = true;
															}
														}
													} else {
														findSw = false;
													}
													if (findSw == false) {
														maxSeq = maxSeq + 1;
								                        
														//ecamsLogger.debug("dirpath,full,upseq,seq====>" + pathDepth[i] + "  , " + strDir  + "  , " + Integer.toString(upSeq)  + "  , " + Integer.toString(maxSeq));
														rst = new HashMap<String,String>();
														rst.put("cm_dirpath",pathDepth[i]);
														rst.put("cm_fullpath",strDir);
														rst.put("cm_upseq",Integer.toString(upSeq));
														rst.put("cm_seqno",Integer.toString(maxSeq));
														rsval.add(maxSeq - 1, rst); 
														upSeq = maxSeq;
													}
												}
											}
				                		}
			                	    }
			                	}
		                	
			                }
			            }
			        } finally {
			            if (in != null)
			                in.close();
			        }
		        }
		        if (mFile.isFile() && mFile.exists()) mFile.delete();
            }
            //ecamsLogger.error("+++++++rsval+++++++"+rsval.toString());
            if (rsval.size() > 0) {
				for (int i = 0;rsval.size() > i;i++) {
					ecmmtb.addXML(rsval.get(i).get("cm_seqno"),rsval.get(i).get("cm_seqno"),
							rsval.get(i).get("cm_dirpath"),rsval.get(i).get("cm_upseq"),
							rsval.get(i).get("cm_fullpath"),rsval.get(i).get("cm_dsncd"),
							"true",rsval.get(i).get("cm_upseq"));
				}
			}
	
    		list = new ArrayList<Document>();
    		list.add(ecmmtb.getDocument());		
    		returnObjectArray = list.toArray();
    		
    		list = null;
    		pstmt.close();
			conn.close();
			pstmt = null;
			conn = null;
			rs.close();// 20221219 ecams_batexec 추가 쿼리
			rs = null;	
    		//ecamsLogger.error(ecmmtb.xml_toStr());		
    		return returnObjectArray;            
            
		} catch (SQLException sqlexception) {
			sqlexception.printStackTrace();
			ecamsLogger.error("## SystemPath.getSvrDir() SQLException START ##");
			ecamsLogger.error("## Error DESC : ", sqlexception);	
			ecamsLogger.error("## SystemPath.getSvrDir() SQLException END ##");			
			throw sqlexception;
		} catch (Exception exception) {
			exception.printStackTrace();
			ecamsLogger.error("## SystemPath.getSvrDir() Exception START ##");				
			ecamsLogger.error("## Error DESC : ", exception);	
			ecamsLogger.error("## SystemPath.getSvrDir() Exception END ##");				
			throw exception;
		}finally{
			if (returnObjectArray != null)	returnObjectArray = null;
		}

	}//end of getDirPath() method statement
	public Object[] getFileList(String UserID,String SysCd,String SvrIp,String SvrPort,String BufSize,String BaseDir,String SvrCd,String SvrSeq,String GbnCd) throws SQLException, Exception {
		Connection        conn        = null;
		PreparedStatement pstmt       = null;
		ResultSet         rs          = null;
		StringBuffer      strQuery    = new StringBuffer();
		eCAMSInfo         ecamsinfo   = new eCAMSInfo();
		ArrayList<HashMap<String, String>>  rsval = new ArrayList<HashMap<String, String>>();
		HashMap<String, String>			  rst		  = null;
		String[]          pathDepth   = null;
		String            strDir      = null;
		boolean           findSw      = false;
		boolean           fileSw      = false;
		boolean           ErrSw      = false;
		String            strBinPath  = "";
		String            strTmpPath  = "";
		String            strFile     = "";
		String            makeFile    = "";
		String            strBaseDir  = "";
		File shfile=null;
		OutputStreamWriter writer = null;
		String[] strAry = null;
		Runtime  run = null;
		Process p = null;
		Object[] returnObjectArray = null;
		ConnectionContext connectionContext = new ConnectionResource();
		rsval.clear();
		String rtString = "";
		try {
			conn = connectionContext.getConnection();
			strBinPath = ecamsinfo.getFileInfo("14");
			ErrSw = false;
			if (strBinPath == "" || strBinPath == null)
				throw new Exception("관리자에게 연락하여 주시기 바랍니다. (형상관리환경설정 - 실행디렉토리)");
	
			strTmpPath = ecamsinfo.getFileInfo("99");
			if (strTmpPath == "" || strTmpPath == null) 
				throw new Exception("관리자에게 연락하여 주시기 바랍니다. (형상관리환경설정 - 실행디렉토리)");
			
			try {
				makeFile = "filelist" + UserID;
				strFile = strBinPath + makeFile + "." + SysCd + ".ih.cs";
				
				shfile=null;
				shfile = new File(strTmpPath + makeFile+".sh");              //File 불러온다.
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
				pstmt.close();
				writer = new OutputStreamWriter( new FileOutputStream(strTmpPath + makeFile+".sh"));
				writer.write("cd "+strBinPath +"\n");
//				writer.write("rtval=`./ecams_ih_cs " + SysCd + " " + SvrIp + " " + SvrPort + " " + BufSize + " " + BaseDir + " " + makeFile + " " + GbnCd + "`\n");
				writer.write("./ecams_batexec " +rtString + " \"cd " + strBinPath+ ";" +"./ecams_ih_cs " + SysCd + " " + SvrIp + " " + SvrPort + " " + BufSize + " " + BaseDir + " " + makeFile + " " + GbnCd + "\" \n");
//				writer.write("exit $rtval\n");
				writer.write("exit $?\n");
				writer.close();
				
				strAry = new String[3];
				
				strAry[0] = "chmod";
				strAry[1] = "777";
				strAry[2] = strTmpPath + makeFile+".sh";
				
				run = Runtime.getRuntime();

				p = run.exec(strAry);
				p.waitFor();
								
				run = Runtime.getRuntime();
				
				strAry = new String[2];
				
				strAry[0] = "/bin/sh";
				strAry[1] = strTmpPath + makeFile+".sh";
				
				p = run.exec(strAry);
				p.waitFor();
				
				//ecamsLogger.debug("====return===="+Integer.toString(p.exitValue())+" \n");
				if (p.exitValue() != 0) {
					ErrSw = true;
					throw new Exception("신규대상목록 추출을 위한 작업에 실패하였습니다 ");
				}
				else{
					shfile.delete();
				}			
			} catch (Exception e) {
				throw new Exception(e);
			} 
			
			//strFile = "c:\\eCAMS\\filelist" + UserID + "."+SysCd+".ih.cs";
            if (ErrSw == false) {
				File mFile = new File(strFile);
		        if (!mFile.isFile() || !mFile.exists()) {
					ErrSw = true;
					throw new Exception("신규대상목록 추출을 위한 작업에 실패하였습니다 [작성된 파일 없음]");
		        } else {				        
			        BufferedReader in = null;
			        fileSw = true;
			        //PrintWriter out = null;
			        
			        try {
			            in = new BufferedReader(new FileReader(mFile));
			            String str = null;
			            strBaseDir = BaseDir;
			            String wkF = "";
			            String wkA = "";
			            String wkB = "";
			            String wkDir = "";
			            String wkDsnCd = null;
			            String wkJobCd = null;
			            String wkRsrcCd = null;
			            while ((str = in.readLine()) != null) {
			                if (str.length() > 0) {
			                	if (!str.substring(0,1).equals("d")) {
			                		if (str.substring(0,1).equals("/")) {
			                			wkDir = str.substring(0, str.length() - 1);	
			                		} else if (str.substring(0,1).equals("-")) {
			                			wkF = str;
			                			int Y = 0;
			                			int X = 0;
			                			while (wkF.length() > 0) {
			                				Y = Y + 1;
			                				X = wkF.indexOf(" ");
			                				if (X >= 0) {
			                					wkB = wkF.substring(0,X).trim();
			                					wkF = wkF.substring(X).trim();
			                				} else {
			                					wkB = wkF.trim();
			                					wkF = "";
			                				}
			                				if (Y == 9) {
			                					wkRsrcCd = "";
			                					wkJobCd = "";
			                					wkDsnCd = "";
			                					strQuery.setLength(0);
			                					strQuery.append("select cm_dsncd from cmm0070            \n");
			                					strQuery.append(" where cm_syscd=? and cm_dirpath=?      \n");
			                					pstmt = conn.prepareStatement(strQuery.toString());
			                		            pstmt.setString(1, SysCd);
			                		            pstmt.setString(2, wkDir);
			                		                       
			                		            rs = pstmt.executeQuery();
			                		            if (rs.next()) {
			                		            	wkDsnCd = rs.getString("cm_dsncd");
			                		            }
			                		            rs.close();
			                		            pstmt.close();
			                		            
			                		            findSw = false;
			                		            if (wkDsnCd != null && wkDsnCd != "") {			                		            	
			                		            	strQuery.setLength(0);
			                		            	strQuery.append("select count(*) cnt from cmr0020        \n");
				                					strQuery.append(" where cr_syscd=? and cr_dsncd=?        \n");
				                					strQuery.append("   and cr_rsrcname=?                    \n");
				                					pstmt = conn.prepareStatement(strQuery.toString());
				                					//pstmt = new LoggableStatement(conn,strQuery.toString());
				                		            pstmt.setString(1, SysCd);
				                		            pstmt.setString(2, wkDsnCd);
				                		            pstmt.setString(3, wkB);
				                		            ////ecamsLogger.error(((LoggableStatement)pstmt).getQueryString());
				                		            rs = pstmt.executeQuery();
				                		            if (rs.next()) {
				                		            	if (rs.getInt("cnt") > 0) findSw = true;
				                		            }
				                		            rs.close();
				                		            pstmt.close();			                		            	
			                		            }
			                		            
			                		            if (findSw == false && wkDsnCd != null && wkDsnCd != "") {
			                		            	strQuery.setLength(0);
			                		            	strQuery.append("select cm_rsrccd from cmm0072           \n");
				                					strQuery.append(" where cm_syscd=? and cm_dsncd=?        \n");
				                					pstmt = conn.prepareStatement(strQuery.toString());
				                					//pstmt = new LoggableStatement(conn,strQuery.toString());
				                		            pstmt.setString(1, SysCd);
				                		            pstmt.setString(2, wkDsnCd);
				                		            ////ecamsLogger.error(((LoggableStatement)pstmt).getQueryString());
				                		            rs = pstmt.executeQuery();
				                		            while (rs.next()) {
				                		            	if (wkRsrcCd.length()>0) wkRsrcCd = wkRsrcCd+",";
				                		            	wkRsrcCd = wkRsrcCd+rs.getString("cm_rsrccd");
				                		            }
				                		            rs.close();
				                		            pstmt.close();	
				                		            
			                		            	strQuery.setLength(0);
			                		            	strQuery.append("select cm_jobcd from cmm0073           \n");
				                					strQuery.append(" where cm_syscd=? and cm_dsncd=?        \n");
				                					pstmt = conn.prepareStatement(strQuery.toString());
				                		            pstmt.setString(1, SysCd);
				                		            pstmt.setString(2, wkDsnCd);
				                		                       
				                		            rs = pstmt.executeQuery();
				                		            while (rs.next()) {
				                		            	if (wkJobCd.length()>0) wkJobCd = wkJobCd+",";
				                		            	wkJobCd = wkJobCd+rs.getString("cm_jobcd");
				                		            }
				                		            rs.close();
				                		            pstmt.close();	
			                		            	
			                		            }
			                		            
			                		            if (findSw == false) {
			                		            	String strRsrc = "";
			                		            	String strExt = "";
			                		            	int    i = 0;
			                		            	strQuery.setLength(0);
			                		            	strQuery.append("select cm_rsrccd from cmm0038                     \n");
			                		    			strQuery.append("where cm_syscd=? and cm_svrcd=? and cm_seqno=?    \n");
			                		    			strQuery.append("  and instr(?,cm_volpath)>0                       \n");			                		    			
			                		                pstmt = conn.prepareStatement(strQuery.toString());
			                		    			//pstmt = new LoggableStatement(conn,strQuery.toString());
			                		    			pstmt.setString(1, SysCd);
			                		                pstmt.setString(2, SvrCd);
			                		                pstmt.setInt(3, Integer.parseInt(SvrSeq));
			                		                pstmt.setString(4, wkDir);
			                		                ////ecamsLogger.error(((LoggableStatement)pstmt).getQueryString());           
			                		                rs = pstmt.executeQuery();            
			                		    			while (rs.next()){
			                		    				if (wkRsrcCd != "" && wkRsrcCd != null) {
			                		    					if (wkRsrcCd.indexOf(rs.getString("cm_rsrccd")) >= 0) {
			                		    						if (strRsrc != null && strRsrc != "") {
			                		    							strRsrc = strRsrc+","+rs.getString("cm_rsrccd");
			                		    						} else strRsrc = rs.getString("cm_rsrccd");
			                		    					} wkRsrcCd = wkRsrcCd+","+rs.getString("cm_rsrccd");
			                		    				} else {
		                		    						if (strRsrc != null && strRsrc != "") {
		                		    							strRsrc = strRsrc+","+rs.getString("cm_rsrccd");
		                		    						} else strRsrc = rs.getString("cm_rsrccd");
			                		    				}
			                		    			}//end of while-loop statement
			                		    			rs.close();
			                		    			pstmt.close();
			                		    			if (strRsrc == null || strRsrc == "") 
			                		    				if (wkRsrcCd != null && wkRsrcCd != "") strRsrc = wkRsrcCd;
			                		    			//if (strRsrc.indexOf(",") >= 0 || strRsrc == null || strRsrc == "") {
			                		    				wkRsrcCd = "";
			                		    				String svRsrc = "";
				                		    			if (wkB.indexOf(".") >= 0) {
				                							i = wkB.lastIndexOf(".");
				                							if (i>=0) strExt = wkB.substring(i);
				                						}
				                						strQuery.setLength(0);
				                						strQuery.append("select a.cm_rsrccd from cmm0032 a,cmm0023 b        \n");
				                						strQuery.append(" where a.cm_syscd=? and a.cm_langcd=b.cm_langcd    \n");
				                						strQuery.append("and a.cm_rsrccd not in (select cm_samersrc from cmm0037  \n");
				                						strQuery.append("                         where cm_syscd=?          \n");
				                						strQuery.append("                           and cm_factcd='04')     \n");
				                						if (strExt != null && strExt != "") 
				                							strQuery.append("   and instr(b.cm_exename,?)>0                 \n");
				                						else
				                							strQuery.append("   and b.cm_exeno='Y'                          \n");
				                						pstmt = conn.prepareStatement(strQuery.toString());
				                						//pstmt = new LoggableStatement(conn,strQuery.toString());
				                			            pstmt.setString(1, SysCd);
				                			            pstmt.setString(2, SysCd);
				                			            if (strExt != null && strExt != "") pstmt.setString(3, strExt);
				                			            ////ecamsLogger.error(((LoggableStatement)pstmt).getQueryString());
				                			            rs = pstmt.executeQuery();            
				                						while (rs.next()){
				                		    				if (strRsrc != "" && strRsrc != null) {
				                		    					if (strRsrc.indexOf(rs.getString("cm_rsrccd")) >= 0) {
				                		    						if (wkRsrcCd != null && wkRsrcCd != "") {
				                		    							wkRsrcCd = wkRsrcCd+","+rs.getString("cm_rsrccd");
				                		    						} else wkRsrcCd = rs.getString("cm_rsrccd");
				                		    					} 
				                		    					svRsrc = svRsrc+","+rs.getString("cm_rsrccd"); 
				                		    				} else {
			                		    						if (wkRsrcCd != null && wkRsrcCd != "") {
			                		    							wkRsrcCd = wkRsrcCd+","+rs.getString("cm_rsrccd");
			                		    						} else wkRsrcCd = rs.getString("cm_rsrccd");
				                		    				}
				                							
				                						}//end of while-loop statement				                						
				                						rs.close();
				                						pstmt.close();
				                						if (svRsrc != null && svRsrc != "") {
				                							if (wkRsrcCd == null || wkRsrcCd == "") wkRsrcCd = svRsrc;
				                						}
			                		    			//} else wkRsrcCd = strRsrc;
				                					if (wkRsrcCd == null || wkRsrcCd == "") {	
				                						strQuery.setLength(0);
				                						strQuery.append("select cm_rsrccd from cmm0036   \n");
				                						strQuery.append(" where cm_syscd=?               \n");
				                						strQuery.append("  and cm_closedt is null        \n");
				                						pstmt = conn.prepareStatement(strQuery.toString());
				                						pstmt.setString(1, SysCd);
				                						rs = pstmt.executeQuery();
				                						if (rs.next()) {
				                							wkRsrcCd = rs.getString("cm_rsrccd");
				                						}
				                						rs.close();
				                						pstmt.close();
				                					}
			                		    			if (wkRsrcCd != null && wkRsrcCd != "") {
				                		            	rst = new HashMap<String, String>();
					                					rst.put("cm_dirpath", wkDir);
					                					rst.put("filename", wkB);
					                					if (wkDsnCd != null && wkDsnCd != "") rst.put("cm_dsncd", wkDsnCd);
					                					if (wkRsrcCd != null && wkRsrcCd != "") rst.put("cm_rsrccd", wkRsrcCd);
					                					if (wkJobCd != null && wkJobCd != "") rst.put("cm_jobcd", wkJobCd);
					                					rsval.add(rst);
			                		    			}
			                		            }
			                		            break;
			                				}
			                			}
			                		}
			                	}
			                }
			            }
			        } finally {
			            if (in != null)
			                in.close();
			        }
		        }
		        if (mFile.isFile() && mFile.exists()) mFile.delete();
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
			ecamsLogger.error("## Cmd0101.getFileList() SQLException START ##");
			ecamsLogger.error("## Error DESC : ", sqlexception);	
			ecamsLogger.error("## Cmd0101.getFileList() SQLException END ##");			
			throw sqlexception;
		} catch (Exception exception) {
			exception.printStackTrace();
			ecamsLogger.error("## Cmd0101.getFileList() Exception START ##");				
			ecamsLogger.error("## Error DESC : ", exception);	
			ecamsLogger.error("## Cmd0101.getFileList() Exception END ##");				
			throw exception;
		}finally{
			if (returnObjectArray != null)	returnObjectArray = null;
			if (rs != null)     try{rs.close();}catch (Exception ex){ex.printStackTrace();}
			if (pstmt != null)  try{pstmt.close();}catch (Exception ex2){ex2.printStackTrace();}
			if (conn != null){
				try{
					ConnectionResource.release(conn);
				}catch(Exception ex3){
					ecamsLogger.error("## Cmd0101.getFileList() connection release exception ##");
					ex3.printStackTrace();
				}
			}
		}

	}//end of getFileList() method statement		
	public Object[] getFileList_thread(String UserID,String SysCd,String SvrIp,String SvrPort,String BufSize,String HomeDir,String BaseDir,String SvrCd,String GbnCd,String exeName1,String exeName2,String SysInfo,String AgentDir,String SysOs) throws SQLException, Exception {
		Connection        conn        = null;
		PreparedStatement pstmt       = null;
		ResultSet         rs          = null;
		StringBuffer      strQuery    = new StringBuffer();
		eCAMSInfo         ecamsinfo   = new eCAMSInfo();
		ArrayList<HashMap<String, String>>  rsval = new ArrayList<HashMap<String, String>>();
		HashMap<String, String>			  rst		  = null;
		String[]          pathDepth   = null;
		String            strDir      = null;
		boolean           findSw      = false;
		boolean           fileSw      = false;
		boolean           ErrSw      = false;
		String            strBinPath  = "";
		String            strTmpPath  = "";
		String            strFile     = "";
		String            makeFile    = "";
		int               i           = 0;
		int               svCnt       = 0;
		boolean           overSw      = false;
		File shfile=null;
		OutputStreamWriter writer = null;
		String[] strAry = null;
		Runtime  run = null;
		Process p = null;
		Object[] returnObjectArray = null;
		ConnectionContext connectionContext = new ConnectionResource();
		rsval.clear();
		String rtString = "";
		try {
			conn = connectionContext.getConnection();
			strBinPath = ecamsinfo.getFileInfo("14");
			ErrSw = false;
			if (strBinPath == "" || strBinPath == null)
				throw new Exception("관리자에게 연락하여 주시기 바랍니다. (형상관리환경설정 - 실행디렉토리)");
	
			strTmpPath = ecamsinfo.getFileInfo("99");
			if (strTmpPath == "" || strTmpPath == null) 
				throw new Exception("관리자에게 연락하여 주시기 바랍니다. (형상관리환경설정 - 실행디렉토리)");

//			ecamsLogger.error("rtval=`./ecams_ih_cs " + SysCd + " " + SvrIp + " "+ BaseDir + " "  + SvrPort + " " + makeFile + " " + GbnCd + "`\n");
           
			try {	
				makeFile = "filelist" + UserID.replace("*", "S");;
				strFile = strBinPath + makeFile + "." + SysCd + ".ih.cs";
				
				System.out.println(AgentDir);
				System.out.println(AgentDir);
				System.out.println(AgentDir);
				System.out.println(AgentDir);
				System.out.println(AgentDir);
				
				shfile=null;
				shfile = new File(strTmpPath + makeFile+".sh");              //File 불러온다.
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
				
				if (SysOs.equals("03")) BaseDir = AgentDir + BaseDir;
				//ecamsLogger.error("rtval=`./ecams_ih_cs " + SysCd + " " + SvrIp + " "+ BaseDir + " "  + SvrPort + " " + makeFile + " " + GbnCd + "`\n");
				writer = new OutputStreamWriter( new FileOutputStream(strTmpPath + makeFile+".sh"));
				writer.write("cd "+strBinPath +"\n");
//				writer.write("./ecams_ih_cs " + SysCd + " " + SvrIp +  " " + SvrPort + " " + BufSize + " " + BaseDir +" " + makeFile + " " + GbnCd + " " + AgentDir + "\n");
				writer.write("./ecams_batexec " +rtString + " \"cd " + strBinPath+ ";" +"./ecams_ih_cs " + SysCd + " " + SvrIp +  " " + SvrPort + " " + BufSize + " " + BaseDir +" " + makeFile + " " + GbnCd + " " + AgentDir + "\" \n");
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
					ErrSw = true;
					throw new Exception("신규대상목록 추출을 위한 작업에 실패하였습니다 ");
				}
				else{
					shfile.delete();
				}
				outgb = null;
				errgb = null;
			} catch (Exception e) {
				throw new Exception(e);
			} 
			//strFile = "c:\\filelist20020206.00100.ih.cs";
            if (ErrSw == false) {
				File mFile = new File(strFile);
		        if (!mFile.isFile() || !mFile.exists()) {
					ErrSw = true;
					throw new Exception("신규대상목록 추출을 위한 작업에 실패하였습니다 [작성된 파일 없음]");
		        } else {				        
			        BufferedReader in = null;
			        fileSw = true;
			        //PrintWriter out = null;
			        
			        try {
			            //in = new BufferedReader(new FileReader(mFile));
			            in = new BufferedReader(new InputStreamReader(new FileInputStream(mFile),"MS949"));
			            String str = null;
			            String wkF = "";
			            String wkB = "";
			            String wkDir = "";
			            String wkExe   = "";
			            ThreadPool pool = new ThreadPool(10);
			            String[] strExe1 = exeName1.split(",");
			            String[] strExe2 = exeName2.split(",");
			            int z = 0;
			            int k = 0;
			            
			            while ((str = in.readLine()) != null) {
			                fileSw = false;
			            	if (str.length() > 0) {
			                	if (SysOs.equals("03")) {
			                		if (str.indexOf("디렉터리")>0) {
			                			wkDir = str.substring(0,str.indexOf("디렉터리"));
			                			wkDir = wkDir.trim();
			                			wkDir = wkDir.substring(wkDir.indexOf(":")+1);
			                			do {
			                				wkDir = wkDir.replace("\\", "/");
			                			} while (wkDir.indexOf("\\")>=0);
			                		} else if (str.indexOf("<DIR>")<0 && !str.substring(0,1).equals(" ")) {
			                			//ecamsLogger.error("++++++++wkDir,str1++++"+wkDir + " " + str);
			                			for (k=0;4>k;k++) {
			                				if (str.indexOf(" ")>=0) {
			                					str = str.substring(str.indexOf(" ")).trim();
			                				} else str = "";
			                			}

			                			//ecamsLogger.error("++++++++wkDir,str2++++"+wkDir + " " + str);
			                			if (str.length()>0) {
				                			wkF = str;
				                			wkB = str;
				                			fileSw = true;
			                			} 
			                		}
			                		
			                	} else {
			                		//ecamsLogger.error("+++++++str++++++++"+str);
			                		if (!str.substring(0,1).equals("d")) {
				                		if (str.substring(0,1).equals("/")) {
				                			wkDir = str.substring(0, str.length() - 1);

				                		} else if (str.substring(0,1).equals("-")) {
				                			wkF = str;
				                			int Y = 0;
				                			int X = 0;
				                			while (wkF.length() > 0) {
				                				Y = Y + 1;
				                				X = wkF.indexOf(" ");
				                				if (X >= 0) {
				                					wkB = wkF.substring(0,X).trim();
				                					wkF = wkF.substring(X).trim();
				                				} else {
				                					wkB = wkF.trim();
				                					wkF = "";
				                				}
				                				if (Y == 9 && wkDir.length()>0 && wkB.length()>0) {
				                					//ecamsLogger.error("+++++++str,wkDir,wkB++++++++"+wkDir+" "+wkB);
				                					fileSw = true;
				                					break;
				                				}
				                			}
				                		}
			                		}			                		
			                	}
			                	if (fileSw == true) {
                					findSw = false;
                					if (exeName1 == null || exeName1 == "") {
                						if (exeName2 == null || exeName2 == "") findSw = true;
                						else {
                							findSw = true;
                							wkExe = "";
                    						if (wkB.indexOf(".")>0) {
                    							wkExe = wkB.substring(wkB.lastIndexOf("."));
                    						}
    	                					for (z=0;strExe2.length>z;z++) {
    	                						if (strExe2[z] == null || strExe2[z].equals("")) {
    	                							if (wkExe == null || wkExe.equals("")) {
    		                							findSw = false;
    		                							break;
    	                							}
    	                						} else {
    	                							if (strExe2[z].equals(wkExe)) {
    		                							findSw = false;
    		                							break;
    		                						}
    	                						}
    	                					}
                						}
                					} else {
                						wkExe = "";
                						if (wkB.indexOf(".")>0) {
                							wkExe = wkB.substring(wkB.lastIndexOf("."));
                						}
	                					for (z=0;strExe1.length>z;z++) {
	                						if (strExe1[z] == null || strExe1[z].equals("")) {
	                							if (wkExe == null || wkExe.equals("")) {
		                							findSw = true;
		                							break;
	                							}
	                						} else {
	                							if (strExe1[z].equals(wkExe)) {
		                							findSw = true;
		                							break;
		                						}
	                						}
	                					}
                					}			                					
                					if (findSw == true) {
	                					rst = new HashMap<String, String>();
	                					rst.put("syscd", SysCd);
	                					rst.put("dirpath", wkDir);
	                					rst.put("filename", wkB);
	                					++svCnt;
	                					//ecamsLogger.error("++++fileList+++++"+ Integer.toString(svCnt)+" " +wkDir+"/"+wkB);
                						pool.assign(new svrOpen_thread_file(rst,rsval,conn));
                						if (svCnt%5 == 0){
                							Thread.sleep(30);
                						}
                						if (rsval.size()>1000) {
                							overSw = true;
                							break;
                						}
                					}
			                	}
			                }
			            }
			            if (svCnt>0) {
			            	pool.complete();
			            }
			        } finally {
			            if (in != null)
			                in.close();
			        }
		        } 
		        
		        // if (mFile.isFile() && mFile.exists()) mFile.delete();
            }            
            conn.close();
            
            int j = 0;
            strDir = "";
            String wkExe1 = "";
            String wkExe2 = "";
            String wkB1 = "";
            String wkB2 = "";
            for (i=0;rsval.size()>i;i++) { 
            	if (i >= rsval.size()) break;
            	if (rsval.get(i).get("filename") != null && rsval.get(i).get("filename") != "") {
	            	wkExe1 = "";
	            	
	            	wkB1 = rsval.get(i).get("filename");
	            	
					if (wkB1.indexOf(".")>0) {
						wkExe1 = wkB1.substring(wkB1.lastIndexOf("."));
						wkB1 = wkB1.substring(0,wkB1.lastIndexOf("."));
					}
					//ecamsLogger.error("+++ base name ++"+wkB1+", "+ wkExe1);
					if (wkExe1.equals(".c") || wkExe1.equals(".pc")) {
		            	for (j=i+1;rsval.size()>j;j++) {
		            		if (j>=rsval.size()) break;
		            		if (!rsval.get(i).get("cm_dirpath").equals(rsval.get(j).get("cm_dirpath"))) break;
		            		wkExe2 = "";
		                	wkB2 = rsval.get(j).get("filename");
		                	
		    				if (wkB2.indexOf(".")>0) {
		    					wkExe2 = wkB2.substring(wkB2.lastIndexOf("."));
		    					wkB2 = wkB2.substring(0,wkB2.lastIndexOf("."));
		    				}
		    				//ecamsLogger.error("+++ same name ++"+wkB1+", "+ wkExe1+"==>"+wkB2+", "+ wkExe2);
		    				if (wkB1.equals(wkB2) && (wkExe2.equals(".c") || wkExe2.equals(".pc"))) {
		    					if (wkExe1.equals(".c")) {
		    						rsval.remove(i--);
		    						break;
		    					}
		    					if (wkExe2.equals(".c")) {
		    						rsval.remove(j);
		    						break;
		    					}
		    				}
		            	}
					}
            	} else {
            		rsval.remove(i--);
            	}
            }
            if (overSw == true) {
            	rst = new HashMap<String, String>();
				rst.put("cm_dirpath", "등록대상 파일의 갯수가 많아서 일부만 먼저 추출하였습니다. 등록하신 후 계속 추출하여 등록하시기 바랍니다.");
				rst.put("filename", "");
				rst.put("cm_dsncd", "");
				
				rst.put("enable1", "1");
				rst.put("selected", "0");
				rst.put("error", "W");
				rsval.add(0,rst);
            }
    		returnObjectArray = rsval.toArray();
    		//ecamsLogger.error("+++++++++++E N D getFileList_thread+++++++++");
    		rsval = null;
    		conn.close();
    		pstmt.close();
			pstmt = null;
			conn = null;
			rs.close();// 20221219 ecams_batexec 추가 쿼리
			rs = null;
			pstmt = null;
    		return returnObjectArray;            
	
		} catch (SQLException sqlexception) {
			sqlexception.printStackTrace();
			ecamsLogger.error("## svrOpen_svr.getFileList() SQLException START ##");
			ecamsLogger.error("## Error DESC : ", sqlexception);	
			ecamsLogger.error("## svrOpen_svr.getFileList() SQLException END ##");			
			throw sqlexception;
		} catch (Exception exception) {
			exception.printStackTrace();
			ecamsLogger.error("## svrOpen_svr.getFileList() Exception START ##");				
			ecamsLogger.error("## Error DESC : ", exception);	
			ecamsLogger.error("## svrOpen_svr.getFileList() Exception END ##");				
			throw exception;
		}finally{
			if (returnObjectArray != null)	returnObjectArray = null;
			if (rs != null)     try{rs.close();}catch (Exception ex){ex.printStackTrace();}
			if (pstmt != null)  try{pstmt.close();}catch (Exception ex2){ex2.printStackTrace();}
			if (conn != null){
				try{
					ConnectionResource.release(conn);
				}catch(Exception ex3){
					ecamsLogger.error("## svrOpen_svr.getFileList() connection release exception ##");
					ex3.printStackTrace();
				}
			}
		}

	}//end of getFileList() method statement
	
	
	public Object[] cmr0020_Insert_thread(ArrayList<HashMap<String,String>> fileList,HashMap<String,String> etcData) throws SQLException, Exception {
		Connection        conn        = null;
		ArrayList<HashMap<String, String>>  rsval = new ArrayList<HashMap<String, String>>();
		ArrayList<HashMap<String, String>>  rtList = new ArrayList<HashMap<String, String>>();
		Object[] returnObjectArray = null;
		ConnectionContext connectionContext = new ConnectionResource();	
		Cmd0100 cmd0100 = new Cmd0100();
		HashMap<String, String> rst = null;
		HashMap<String, String> rst2 = null;
				
		try {
			
			conn = connectionContext.getConnection();
			rsval.clear();
	        String strDsnCd = "";
	        int j = 0;
	        rtList.clear();
			for (int i=0;i<fileList.size();i++){
				rst = new HashMap<String, String>();
				rst = fileList.get(i);
				if (fileList.get(i).get("cm_dsncd") == null || fileList.get(i).get("cm_dsncd") == "") {
					//cmm0070_Insert(String UserID,String SysCd,String RsrcName,String RsrcCd,String JobCd,String DirPath,boolean tmaxFg)
					strDsnCd = cmd0100.cmm0070_Insert(etcData.get("userid"),etcData.get("syscd"),fileList.get(i).get("filename"),etcData.get("rsrccd"),etcData.get("jobcd"),fileList.get(i).get("cm_dirpath"),false,conn);
					rst.put("cm_dsncd", strDsnCd);
					
					if ((i + 2) < fileList.size()) {
						for (j=i+1;fileList.size()>j;j++) {
							if (fileList.get(j).get("cm_dsncd") == null || fileList.get(j).get("cm_dsncd") == "") {
								if (fileList.get(i).get("cm_dirpath").equals(fileList.get(j).get("cm_dirpath"))) {
									rst2 = new HashMap<String, String>();
									rst2 = fileList.get(j);
									rst2.put("cm_dsncd", strDsnCd);
									fileList.set(j, rst2);
								}
							}
						}
					}
				}
				rtList.add(rst);
			}//end of while-loop statement
			
			//ecamsLogger.error("+++++++++fileList+++"+rtList.toString());
			String retMsg = "";
			for (int i=0;i<rtList.size();i++){
				rst = new HashMap<String, String>();
				rst = rtList.get(i);
				retMsg = statusCheck(etcData.get("syscd"),rtList.get(i).get("cm_dsncd"),rtList.get(i).get("filename"),etcData.get("userid"),conn);
	        	if (retMsg.equals("0")) {
	        		//cmr0020_Insert(UserId,SysCd,DsnCd,RsrcName,RsrcCd,JobCd,LangCd,ProgTit,Service,"",conn);
	        		//(String UserId,String SysCd,String DsnCd,String RsrcName,String RsrcCd,String JobCd,String ProgTit,String BaseItem,String rsrcInfo,Connection conn)
	        		retMsg = cmd0100.cmr0020_Insert(etcData.get("userid"),etcData.get("syscd"),etcData.get("OrderId"),
	        				rtList.get(i).get("cm_dsncd"),rtList.get(i).get("filename"),etcData.get("rsrccd"),
	        				etcData.get("jobcd"),rtList.get(i).get("story"),"",etcData.get("cm_info"),conn,
	        				rtList.get(i).get("langcd"),rtList.get(i).get("compile"),rtList.get(i).get("makecompile"),rtList.get(i).get("teamcd"),
	        				rtList.get(i).get("sqlcheck"),rtList.get(i).get("document"), "","","","");
		        	//ecamsLogger.debug("++++filename,result++"+file.get("filename")+","+retMsg);
		        	if (retMsg.substring(0,1).equals("0")) {
	        			rst.put("cr_itemid", retMsg.substring(1));
	        			rst.put("baseitem", retMsg.substring(1));
	        			rst.put("cm_info", etcData.get("cm_info"));
		        	} else {
		        		rst.put("error", "1");
		        		rst.put("errmsg", "등록실패");
		        	}
	        	} else {
	        		rst.put("error", "1");
	        		rst.put("errmsg", retMsg);
	        	}
			}//end of while-loop statement
			
			conn.close();
			conn = null;
			//ecamsLogger.error("+++++++++rsval+++"+rsval.toString());
			returnObjectArray =  rtList.toArray();
			rtList.clear();
			rtList = null;
			
			return returnObjectArray;		    
	        
		} catch (Exception exception) {
			exception.printStackTrace();
			ecamsLogger.error("## svrOpen_svr.cmr0020_Insert_thread() Exception START ##");				
			ecamsLogger.error("## Error DESC : ", exception);	
			ecamsLogger.error("## svrOpen_svr.cmr0020_Insert_thread() Exception END ##");				
			throw exception;
		}finally{
			if (conn != null){
				try{
					ConnectionResource.release(conn);
				}catch(Exception ex3){
					ecamsLogger.error("## svrOpen_svr.cmr0020_Insert() connection release exception ##");
					ex3.printStackTrace();
				}
			}
		}		
	}//end of cmr0020_Insert_thread() method statement	
	
	
	public String statusCheck(String SysCd, String DsnCd, String RsrcNm, String UserId,Connection conn) throws SQLException, Exception {
		PreparedStatement pstmt       = null;
		ResultSet         rs          = null;
		StringBuffer      strQuery    = new StringBuffer();
		Object[] returnObjectArray = null;
		String wkB   = "0";
		
		try {
	        
		    strQuery.append("select cr_status,cr_lstver,cr_editor from cmr0020 \n");
		    strQuery.append(" where cr_syscd = ? and cr_dsncd = ?              \n");
		    strQuery.append("   and cr_rsrcname = ?                            \n");      
	        pstmt = conn.prepareStatement(strQuery.toString());
	        //pstmt =  new LoggableStatement(conn, strQuery.toString());            
			
            pstmt.setString(1, SysCd);            	
            pstmt.setString(2, DsnCd);              	
            pstmt.setString(3, RsrcNm);          	

            //ecamsLogger.debug(((LoggableStatement)pstmt).getQueryString());           

            rs = pstmt.executeQuery();
            
			if (rs.next()){
				if(rs.getInt("cr_lstver") > 0){
					wkB = "기 등록된 프로그램ID입니다.";
				} else if(rs.getString("cr_status").equals("3")){
					if(!UserId.equals(rs.getString("cr_editor"))){
						wkB = "다른개발자가 기 등록한 프로그램ID입니다.";
					}
				} else{
					wkB = "기 등록된 프로그램ID입니다.";
				}
				
			}//end of while-loop statement
			rs.close();
			pstmt.close();		
            
			return wkB;
			
		} catch (SQLException sqlexception) {
			sqlexception.printStackTrace();
			ecamsLogger.error("## svrOpen.statusCheck() SQLException START ##");
			ecamsLogger.error("## Error DESC : ", sqlexception);	
			ecamsLogger.error("## svrOpen.statusCheck() SQLException END ##");			
			throw sqlexception;
		} catch (Exception exception) {
			exception.printStackTrace();
			ecamsLogger.error("## svrOpen.statusCheck() Exception START ##");				
			ecamsLogger.error("## Error DESC : ", exception);	
			ecamsLogger.error("## svrOpen.statusCheck() Exception END ##");				
			throw exception;
		}finally{
			if (strQuery != null)	strQuery = null;
			if (returnObjectArray != null)	returnObjectArray = null;
			if (rs != null)     try{rs.close();}catch (Exception ex){ex.printStackTrace();}
			if (pstmt != null)  try{pstmt.close();}catch (Exception ex2){ex2.printStackTrace();}
		}		
	}//end of statusCheck() method statement

	
	/**
	 * getHomeDirList : cmm0031 과 cmm0038 테이블에서 homedir 리스트 조회
	 * @param String SysCd
	 * @return String
	 * @throws SQLException
	 * @throws Exception
	 */
	public Object[] getHomeDirList(String SysCd) throws SQLException, Exception {
		Connection        conn        = null;
		PreparedStatement pstmt       = null;
		ResultSet         rs          = null;
		StringBuffer      strQuery    = new StringBuffer();
		Object[] returnObjectArray = null;
		ArrayList<HashMap<String, String>>  rsval = new ArrayList<HashMap<String, String>>();
		HashMap<String, String>			  rst		  = null;	
		
		ConnectionContext connectionContext = new ConnectionResource();		
		
		try {
			
			conn = connectionContext.getConnection();
			
			strQuery.append("select a.cm_volpath             \n");
			strQuery.append("  from cmm0038 a,cmm0031 b,cmm0036 c \n");
			strQuery.append(" where b.cm_syscd=?             \n");
			strQuery.append("   and b.cm_svrcd='01'          \n");
			strQuery.append("   and b.cm_closedt is null     \n");
			strQuery.append("   and a.cm_volpath is not null \n");
			strQuery.append("   and b.cm_syscd=a.cm_syscd    \n");
			strQuery.append("   and b.cm_svrcd=a.cm_svrcd    \n");
			strQuery.append("   and b.cm_seqno=a.cm_seqno    \n");
			strQuery.append("   and a.cm_syscd=c.cm_syscd    \n");
			strQuery.append("   and a.cm_rsrccd=c.cm_rsrccd  \n");
			strQuery.append("   and substr(c.cm_info,45,1)='0'\n");
			strQuery.append("   and substr(c.cm_info,26,1)='0'\n");
			strQuery.append(" group by a.cm_volpath          \n");
			strQuery.append(" order by a.cm_volpath          \n");
			pstmt = conn.prepareStatement(strQuery.toString());
           	pstmt.setString(1, SysCd);
            rs = pstmt.executeQuery();
            rsval.clear();

			while (rs.next()){
				rst = new HashMap<String, String>();
				rst.put("cm_volpath", rs.getString("cm_volpath"));
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
			//ecamsLogger.debug(rsval.toString());		
			return returnObjectArray;
			
		} catch (SQLException sqlexception) {
			sqlexception.printStackTrace();
			ecamsLogger.error("## svrOpen.getHomeDirList() SQLException START ##");
			ecamsLogger.error("## Error DESC : ", sqlexception);	
			ecamsLogger.error("## svrOpen.getHomeDirList() SQLException END ##");			
			throw sqlexception;
		} catch (Exception exception) {
			exception.printStackTrace();
			ecamsLogger.error("## svrOpen.getHomeDirList() Exception START ##");				
			ecamsLogger.error("## Error DESC : ", exception);	
			ecamsLogger.error("## svrOpen.getHomeDirList() Exception END ##");				
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
					ecamsLogger.error("## svrOpen.getHomeDirList() connection release exception ##");
					ex3.printStackTrace();
				}
			}
		}
	}//end of getHomeDirList() method statement
	
}//end of svrOpen class statement
