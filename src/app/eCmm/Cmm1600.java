/*****************************************************************************************
	1. program ID	: Cmm1600.java
	2. create date	: 2009. 01. 09
	3. auth		    : No Name
	4. update date	: 2009. 03. 04
	5. auth		    : No Name
	6. description	: [관리자] -> [일괄이행]
*****************************************************************************************/

package app.eCmm;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.sql.ResultSetMetaData;

import org.apache.logging.log4j.Logger;

import com.ecams.common.base.Encryptor;
import com.ecams.common.base.HttpCall;
import com.ecams.common.dbconn.ConnectionContext;
import com.ecams.common.dbconn.ConnectionResource;
import com.ecams.common.logger.EcamsLogger;
import app.common.LoggableStatement;
import app.common.SystemPath;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Calendar;

import app.common.AutoSeq;
//import app.common.CodeInfo;
//import app.common.SystemPath;
import app.common.UserInfo;

/**
 * @author bigeyes
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class Cmm1600{    
	
    /**
     * Logger Class Instance Creation
     * logger
     */
	
    Logger ecamsLogger  = EcamsLogger.getLoggerInstance();
    
    //String _baseItem	= "";
    
    public static void makeLogFile(String log,String fullpath){
		SimpleDateFormat formatter = new SimpleDateFormat ("yyyyMMddHHmmss");
		Calendar cal = Calendar.getInstance();
		String today = formatter.format(cal.getTime());
		String filename = fullpath+"/"+today+"_Cmm1600.log";
		try {
			BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(filename),"euc-kr"));
			bw.write(log);
			bw.newLine();
			bw.close();
			
		}catch(IOException ie){
			System.err.println("Error");
			System.exit(1);
		}
	}
    
	
    /**
	 * USER NAME
	 * @param user_id
	 * @return String
	 * @throws SQLException
	 * @throws Exception
	 */
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
			
			strQuery.setLength(0);
			strQuery.append("select a.cm_svrcd,a.cm_svrip,a.cm_svrname,a.cm_volpath,a.cm_portno, \n");
			strQuery.append("       a.cm_sysos,a.cm_dir,b.cm_codename \n");
			strQuery.append("  from cmm0031 a,cmm0020 b \n");
			strQuery.append(" where a.cm_syscd=? \n");
			strQuery.append("   and a.cm_closedt is null \n");
			strQuery.append("   and a.cm_cmpsvr='Y' \n");
			strQuery.append("   and b.cm_macode='SERVERCD' and b.cm_micode=a.cm_svrcd \n");
			strQuery.append(" order by a.cm_svrname \n");
			
            pstmt = conn.prepareStatement(strQuery.toString());
            pstmt.setString(1, SysCd);
            rs = pstmt.executeQuery();
            
			while (rs.next()){
				rst = new HashMap<String, String>();
				rst.put("cm_svrcd", rs.getString("cm_svrcd"));
				rst.put("cm_svrip", rs.getString("cm_svrip"));
				rst.put("cm_svrname", rs.getString("cm_svrname"));
				rst.put("cm_volpath", rs.getString("cm_volpath"));
				rst.put("cm_portno", rs.getString("cm_portno"));
				rst.put("cm_sysos", rs.getString("cm_sysos"));
				rst.put("cm_dir", rs.getString("cm_dir"));
				rst.put("cm_codename", rs.getString("cm_codename"));
				rst.put("svrname", rs.getString("cm_svrname") + "[" + rs.getString("cm_codename") + "]");
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
			ecamsLogger.error("## Cmm1600.getSvrInfo() SQLException START ##");
			ecamsLogger.error("## Error DESC : ", sqlexception);	
			ecamsLogger.error("## Cmm1600.getSvrInfo() SQLException END ##");			
			throw sqlexception;
		} catch (Exception exception) {
			exception.printStackTrace();
			ecamsLogger.error("## Cmm1600.getSvrInfo() Exception START ##");				
			ecamsLogger.error("## Error DESC : ", exception);	
			ecamsLogger.error("## Cmm1600.getSvrInfo() Exception END ##");				
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
					ecamsLogger.error("## Cmm1600.getSvrInfo() connection release exception ##");
					ex3.printStackTrace();
				}
			}
		}
	}
	
	
	public Object[] getFileList_excel(ArrayList<HashMap<String,String>> fileList,HashMap<String,String> dataObj) throws SQLException, Exception {
		Connection        conn        = null;
		PreparedStatement pstmt       = null;
		ResultSet         rs          = null;
		PreparedStatement pstmt2      = null;
		ResultSet         rs2         = null;
		StringBuffer      strQuery    = new StringBuffer();
		ArrayList<HashMap<String, String>>  rsval = new ArrayList<HashMap<String, String>>();
		HashMap<String, String> rst	  = null;
		
		ConnectionContext connectionContext = new ConnectionResource();

		try {
			conn = connectionContext.getConnection();
			conn.setAutoCommit(false);
			
			String _syscd = dataObj.get("cm_syscd");
			//String _sysmsg = dataObj.get("cm_sysmsg");
			String _sourcename = "";
			String _jobcd = "";
			//String _rsrccd = "";
			String _langcd = "";
			String _editor = "";
			String _dirpath = "";
			String _dsncd = "";
			String errMsg = "";
			String strInfo = "";
			String _comp = "";
			String _makescript = "";
			String _rteam = "";
//			String _sqlck = "";
			String _doc = "";
			String _master = "";
			String _etcdsnhome = "";
			String _etcdsn = "";
			boolean errSw = false;
			//String _baseItem = "";
			boolean tmpYN = false;
			
			int errCnt = 0;
			rsval.clear();
			
			for (int i=0 ; i<fileList.size() ; i++)
			{
				
				rst = new HashMap<String, String>();
				rst.put("NO",Integer.toString(i+1));
				rst.put("sysmsg", fileList.get(i).get("sysmsg").trim());
				rst.put("jobcd", fileList.get(i).get("jobcd").trim());
				rst.put("userid", fileList.get(i).get("userid").trim());
				rst.put("rsrcname", fileList.get(i).get("rsrcname").trim());
				rst.put("story", fileList.get(i).get("story").trim());
				rst.put("dirpath", fileList.get(i).get("dirpath").trim());
				rst.put("jawon", fileList.get(i).get("jawon").trim());
				rst.put("lang", fileList.get(i).get("lang").trim());
				rst.put("comp", fileList.get(i).get("comp").trim());
				if(fileList.get(i).get("makescript").length() > 0){
					rst.put("makescript", fileList.get(i).get("makescript").trim());
				}else{
					rst.put("makescript", "");
				}
				rst.put("rteam", fileList.get(i).get("rteam").trim());
//				rst.put("sqlck", fileList.get(i).get("sqlck").trim());
				rst.put("doc", fileList.get(i).get("doc").trim());
				rst.put("master", fileList.get(i).get("master").trim());
				if(fileList.get(i).get("etcdsnhome").length() > 0){
					rst.put("etcdsnhome", fileList.get(i).get("etcdsnhome").trim());
				}else{
					rst.put("etcdsnhome", "");
				}
				if(fileList.get(i).get("etcdsn").length() > 0){
					rst.put("etcdsn", fileList.get(i).get("etcdsn").trim());
				}else{
					rst.put("etcdsn", "");
				}
				rst.put("_syscd", _syscd);
				
				errMsg = "";
				errSw = false;
				_sourcename = fileList.get(i).get("rsrcname").trim();
				//_rsrccd = "";
				_jobcd = "";
				
				_editor = "";
				_dirpath = "";
				_dsncd = "";
				_langcd = "";
				_comp = "";
				_makescript = "";
				_rteam = "";
//				_sqlck = "";
				_doc = "";
				_master = "";
				
				if(fileList.get(i).get("rsrcname").trim().indexOf(",") != -1){
					errMsg = errMsg +"프로그램명에 (,)있음/";
					errSw = true;
				}
					
				
				if(fileList.get(i).get("rsrcname").trim().indexOf("'") != -1){
					errMsg = errMsg +"프로그램명에(')있음/";
					errSw = true;
				}
				
				if (fileList.get(i).get("lang") == "" && fileList.get(i).get("lang") == null){
					errMsg = errMsg +"사용언어 입력없음/";
					errSw = true;
				}else{
					strQuery.setLength(0);
					strQuery.append("select b.cm_micode from cmm0020 b ");
	                strQuery.append("where b.cm_macode='LANGUAGE' and upper(b.cm_codename)=upper(?) ");
	                
	                pstmt = conn.prepareStatement(strQuery.toString());
	                pstmt.setString(1, fileList.get(i).get("lang").trim());
	                rs = pstmt.executeQuery();
	                
	                if(rs.next()){
	                	_langcd = rs.getString("cm_micode");
	                }else{
	                	errMsg = errMsg + "미등록 사용언어/";
	                	errSw = true;
	                }
					rs.close();
					pstmt.close();
				}
				
				if (fileList.get(i).get("doc") == "" && fileList.get(i).get("doc") == null){
					errMsg = errMsg +"DOCUMENT 입력없음/";
					errSw = true;
				}else{
					strQuery.setLength(0);
					strQuery.append("select b.cm_micode from cmm0020 b ");
	                strQuery.append("where b.cm_macode='DOCUMENT' and upper(b.cm_codename)=upper(?) ");
	                
	                pstmt = conn.prepareStatement(strQuery.toString());
	                pstmt.setString(1, fileList.get(i).get("doc").trim());
	                rs = pstmt.executeQuery();
	                
	                if(rs.next()){
	                	_doc = rs.getString("cm_micode");
	                }else{
	                	errMsg = errMsg + "미등록 DOCUMENT코드명/";
	                	errSw = true;
	                }
					rs.close();
					pstmt.close();
				}
				
//				if (fileList.get(i).get("sqlck") == "" && fileList.get(i).get("sqlck") == null){
//					errMsg = "SQLCHECK 입력없음/";
//					errSw = true;
//				}else{
//					strQuery.setLength(0);
//					strQuery.append("select b.cm_micode from cmm0020 b ");
//	                strQuery.append("where b.cm_macode='SQLCHECK' and upper(b.cm_codename)=upper(?) ");
//	                
//	                pstmt = conn.prepareStatement(strQuery.toString());
//	                pstmt.setString(1, fileList.get(i).get("sqlck").trim());
//	                rs = pstmt.executeQuery();
//	                
//	                if(rs.next()){
//	                	_sqlck = rs.getString("cm_micode");
//	                }else{
//	                	errMsg = errMsg + "미등록 SQLCHECK코드명/";
//	                	errSw = true;
//	                }
//					rs.close();
//					pstmt.close();
//				}
				
				if (fileList.get(i).get("rteam") == "" && fileList.get(i).get("rteam") == null){
					errMsg = errMsg +"관련팀 입력없음/";
					errSw = true;
				}else{
					strQuery.setLength(0);
					strQuery.append("select cm_micode from cmm0020     \n");
	                strQuery.append("where  cm_macode='ETCTEAM' 		\n");
	                strQuery.append("and cm_codename = ? 				\n");
	                
	                pstmt = conn.prepareStatement(strQuery.toString());
	                pstmt.setString(1, fileList.get(i).get("rteam").trim());
	                rs = pstmt.executeQuery();
	                
	                if(rs.next()){
	                	_rteam = rs.getString("cm_micode");
	                }else{
	                	errMsg = errMsg + "미등록 관련팀/";
	                	errSw = true;
	                }
					rs.close();
					pstmt.close();
				}
				
				if (fileList.get(i).get("comp") == "" && fileList.get(i).get("comp") == null){
					errMsg = errMsg +"컴파일모드 입력없음/";
					errSw = true;
				}else{
					strQuery.setLength(0);
					strQuery.append("select cm_compcd from cmm0400 ");
	                strQuery.append("where cm_syscd = ? and cm_compscript = ? and cm_gubun='01'");
	                
	                pstmt = conn.prepareStatement(strQuery.toString());
	                pstmt.setString(1, _syscd);
	                pstmt.setString(2, fileList.get(i).get("comp").trim());
	                rs = pstmt.executeQuery();
	                
	                if(rs.next()){
	                	_comp = rs.getString("cm_compcd");
	                }else{
	                	errMsg = errMsg + "미등록 컴파일모드코드명/";
	                	errSw = true;
	                }
					rs.close();
					pstmt.close();
				}
				
				if (fileList.get(i).get("makescript") == "" && fileList.get(i).get("makescript") == null){
					errMsg = errMsg +"메이크스크립트 입력없음/";
					errSw = true;
				}else{
					strQuery.setLength(0);
					strQuery.append("select cm_compcd from cmm0400 ");
	                strQuery.append("where cm_syscd = ? and cm_compscript = ? and cm_gubun='02'");
	                
	                pstmt = conn.prepareStatement(strQuery.toString());
	                pstmt.setString(1, _syscd);
	                pstmt.setString(2, fileList.get(i).get("makescript").trim());
	                rs = pstmt.executeQuery();
	                
	                if(rs.next()){
	                	_makescript = rs.getString("cm_compcd");
	                }else{
	                	errMsg = errMsg + "미등록 메이크스크립트명/";
	                	errSw = true;
	                }
					rs.close();
					pstmt.close();
				}
				
				if (fileList.get(i).get("jawon") == "" && fileList.get(i).get("jawon") == null){
					errMsg = errMsg +"프로그램종류 입력없음/";
					errSw = true;
				}else{
					strQuery.setLength(0);
					strQuery.append("select a.cm_rsrccd,a.cm_info from cmm0036 a,cmm0020 b ");
	                strQuery.append("where a.cm_syscd=? ");
	                strQuery.append("  and b.cm_macode='JAWON' and upper(b.cm_codename)=upper(?) ");
	                strQuery.append("  and a.cm_rsrccd=b.cm_micode ");
	                
	                pstmt = conn.prepareStatement(strQuery.toString());
	                pstmt.setString(1, _syscd);
	                pstmt.setString(2, fileList.get(i).get("jawon").trim());
	                rs = pstmt.executeQuery();
	                
	                if(rs.next()){
	                	//_rsrccd = rs.getString("cm_rsrccd");
	                	strInfo = rs.getString("cm_info");
	                	rst.put("_rsrccd",rs.getString("cm_rsrccd"));
	                }else{
	                	errMsg = errMsg + "미등록 프로그램종류코드/";
	                	errSw = true;
	                }
					rs.close();
					pstmt.close();
				}
				
				
				if (fileList.get(i).get("jobcd") == "" && fileList.get(i).get("jobcd") == null){
					errMsg = errMsg + "업무코드  입력없음/";
					errSw = true;
				}else{
					strQuery.setLength(0);
					strQuery.append("select cm_jobcd,cm_jobname from cmm0102 where ");
					strQuery.append("cm_jobcd=? or cm_jobname=? ");
					
	                pstmt = conn.prepareStatement(strQuery.toString());
	                pstmt.setString(1, fileList.get(i).get("jobcd").trim());
	                pstmt.setString(2, fileList.get(i).get("jobcd").trim());
	                rs = pstmt.executeQuery();
	                
	                if(rs.next()){
	                	_jobcd = rs.getString("cm_jobcd");
	                    rst.put("_jobcd",rs.getString("cm_jobcd"));
	                    tmpYN = true;
	                }else{
	                	tmpYN = false;
	                	errMsg = errMsg + "미등록 업무코드/";
	                	errSw = true;
	                }
					rs.close();
					pstmt.close();
					
					if (tmpYN){
		            	strQuery.setLength(0);
		            	strQuery.append("select count(*) as cnt from cmm0034 where ");
		            	strQuery.append("cm_syscd=? and cm_jobcd=? ");
		            	
		                pstmt = conn.prepareStatement(strQuery.toString());
		                pstmt.setString(1, _syscd);
		                pstmt.setString(2, _jobcd);
		                rs = pstmt.executeQuery();
		                
		                if (rs.next()){
		                	if (rs.getInt("cnt")==0){
		                		strQuery.setLength(0);
		                		strQuery.append("insert into cmm0034 (CM_SYSCD,CM_JOBCD,CM_CREATDT,CM_LASTDT,CM_EDITOR) ");
		                		strQuery.append("values (?,?,SYSDATE,SYSDATE,?) ");
		                		
		                		pstmt2 = conn.prepareStatement(strQuery.toString());
		                		pstmt2.setString(1,_syscd);
		                		pstmt2.setString(2,_jobcd);
		                		pstmt2.setString(3,fileList.get(i).get("userid"));
		                		pstmt2.executeUpdate();
		                		
		                		pstmt2.close();
		                	}
		                }
		                rs.close();
		                pstmt.close();
					}
				}
				
				strQuery.setLength(0);
				strQuery.append("select cm_userid from cmm0040 where ");
				strQuery.append("cm_userid=? ");
				
                pstmt = conn.prepareStatement(strQuery.toString());
                pstmt.setString(1, fileList.get(i).get("userid").trim());
                rs = pstmt.executeQuery();
                
                if(rs.next()){
                	_editor = rs.getString("cm_userid");
                }else{
                	errMsg = errMsg + "미등록 사용자/";
                	_editor = "admin";
                	errSw = true;
                }
                rst.put("_editor",_editor);
				rs.close();
				pstmt.close();
				
				strQuery.setLength(0);
				strQuery.append("select cm_userid from cmm0040 where ");
				strQuery.append("cm_userid=? ");
				
                pstmt = conn.prepareStatement(strQuery.toString());
                pstmt.setString(1, fileList.get(i).get("master").trim());
                rs = pstmt.executeQuery();
                
                if(rs.next()){
                	_editor = rs.getString("cm_userid");
                }else{
                	errMsg = errMsg + "관리담당자 미등록 사용자/";
                	_master = "admin";
                	errSw = true;
                }
                rst.put("_master",_master);
				rs.close();
				pstmt.close();
				
				_dirpath = fileList.get(i).get("dirpath").trim();
				rst.put("_dirpath", fileList.get(i).get("dirpath").trim());
				
				if (_dirpath == "" & _dirpath == null){
					errMsg = errMsg + "프로그램경로 입력없음/";
					errSw = true;
				}
				if ( _dirpath.substring(_dirpath.length()-1).indexOf("/")>-1 ){
					_dirpath = _dirpath.substring(0, _dirpath.length()-1);
				}
				
				if (errSw == false) {
					rst.put("_dirpath", _dirpath);
					for (int j=0;rsval.size()>j;j++) {
						if (rsval.get(j).get("_dirpath").equals(_dirpath) &&
							rsval.get(j).get("rsrcname").equals(_sourcename)) {
							++errCnt;
							errMsg = errMsg + "중복Data/";
							errSw = true;
							break;
						}
					}
				}
				
				if (errSw == true){//에러 있음
					++errCnt;
					rst.put("errsw", "1");
					rst.put("errmsg", errMsg);
					errSw = true;
				}else{
					strQuery.setLength(0);
					strQuery.append("select cm_dsncd from cmm0070 where cm_syscd=? ");
					strQuery.append("and cm_dirpath=? ");
					
					pstmt = conn.prepareStatement(strQuery.toString());
					//pstmt = new LoggableStatement(conn,strQuery.toString()) ;
					
					pstmt.setString(1, _syscd);
					pstmt.setString(2, _dirpath);
					
					//ecamsLogger.debug(((LoggableStatement)pstmt).getQueryString());
					rs  = pstmt.executeQuery();
					if (rs.next()){
						_dsncd = rs.getString("cm_dsncd");
					}else{
						_dsncd = "0000001";
						
						strQuery.setLength(0);
	                    strQuery.append("select lpad(max(cm_dsncd)+1,7,'0') max from cmm0070 where cm_syscd=? ");
	                    pstmt2 = conn.prepareStatement(strQuery.toString());
	                    pstmt2.setString(1, _syscd);
	                    rs2 = pstmt2.executeQuery();
	                    if (rs2.next()){
	                        if (rs2.getString("max") != null){
	                        	_dsncd = rs2.getString("max");
	        				}
	                    }
	                    rs2.close();
	                    pstmt2.close();
	                    
	                    strQuery.setLength(0);
	                    strQuery.append("insert into cmm0070 (CM_SYSCD,CM_DSNCD,CM_DIRPATH,CM_EDITOR,CM_OPENDT,CM_LASTUPDT) ");
	                    strQuery.append("values (?,?,?,'MASTER',SYSDATE,SYSDATE)");
	                    pstmt2 = conn.prepareStatement(strQuery.toString());
	                    pstmt2.setString(1, _syscd);
	                    pstmt2.setString(2, _dsncd);
	                    pstmt2.setString(3, _dirpath);
	                    pstmt2.executeUpdate();
	                    pstmt2.close();
	                    conn.commit();
					}
					rst.put("_dsncd",_dsncd);
					rs.close();
					pstmt.close();
					
					rst.put("story", fileList.get(i).get("story").replace("'", "''"));
					rst.put("_syscd",_syscd);
					
					strQuery.setLength(0);
					strQuery.append("select count(*) as cnt from cmm0044      \n");
					strQuery.append(" where cm_userid=? and cm_syscd=?        \n");
					strQuery.append("   and cm_jobcd=?                        \n");
					
					pstmt = conn.prepareStatement(strQuery.toString());
					//pstmt = new LoggableStatement(conn,strQuery.toString());
					pstmt.setString(1, rst.get("_editor"));
					pstmt.setString(2, _syscd);
					pstmt.setString(3, rst.get("_jobcd"));
					//ecamsLogger.error(((LoggableStatement)pstmt).getQueryString());
					rs  = pstmt.executeQuery();
					if (rs.next()){
						if (rs.getInt("cnt")==0) {
							strQuery.setLength(0);
							strQuery.append("insert into cmm0044                      \n");
							strQuery.append("(cm_userid,cm_syscd,cm_jobcd,cm_creatdt) \n");
							strQuery.append("values (?, ?, ?, SYSDATE)                \n");
							pstmt2 = conn.prepareStatement(strQuery.toString());
							//pstmt2 = new LoggableStatement(conn,strQuery.toString());
							pstmt2.setString(1, rst.get("_editor"));
							pstmt2.setString(2, _syscd);
							pstmt2.setString(3, rst.get("_jobcd"));
							pstmt2.executeUpdate();
							//ecamsLogger.error(((LoggableStatement)pstmt2).getQueryString());
							pstmt2.close();
						}
					}
					
					rs.close();
					pstmt.close();
					//_baseItem = Cmr0020_Insert(conn,rst);
					
					strQuery.setLength(0);
					strQuery.append("select cr_itemid,cr_status,cr_lstver from cmr0020    ");
					strQuery.append(" where cr_syscd=? and cr_dsncd=? ");
					strQuery.append("   and cr_rsrcname=?             ");
					
					pstmt = conn.prepareStatement(strQuery.toString());
					//pstmt = new LoggableStatement(conn,strQuery.toString());
					
					pstmt.setString(1, _syscd);
					pstmt.setString(2, _dsncd);
					pstmt.setString(3, rst.get("rsrcname"));
					
					////ecamsLogger.error(((LoggableStatement)pstmt).getQueryString());
					rs  = pstmt.executeQuery();
					if (rs.next()){						
						if (rs.getString("cr_status").equals("9") ||
							(dataObj.get("base").equals("1") && "3".equals(rs.getString("cr_status")))) {
			        		//_baseItem = rs.getString("cr_itemid");
							rst.put("_itemid", rs.getString("cr_itemid"));
							rst.put("errmsg","정상");
							rst.put("errsw", "0");
							
				        	strQuery.setLength(0);
			        		strQuery.append("update cmr0020 set CR_RSRCCD=?,CR_JOBCD=?,		");
			        		strQuery.append("                   CR_STORY=?,		");
			        		//strQuery.append("                   CR_LANGCD=?,	");
			        		strQuery.append("                   CR_LASTDATE=SYSDATE, 		");
			        		strQuery.append("                   CR_EDITOR=?,CR_NOMODIFY=0	");
			        		strQuery.append("where cr_itemid=? \n");
			        		pstmt2 = conn.prepareStatement(strQuery.toString());
			        		pstmt2.setString(1, rst.get("_rsrccd"));
			        		pstmt2.setString(2, rst.get("_jobcd"));
			        		//pstmt2.setString(3, rst.get("_langcd"));
			        		pstmt2.setString(3, rst.get("story"));
			        		pstmt2.setString(4, rst.get("_editor"));
			        		pstmt2.setString(5, rs.getString("cr_itemid"));
			        		pstmt2.executeUpdate();
			        		pstmt2.close();
						} else {
							++errCnt;
							errMsg = errMsg + "기등록/";
							rst.put("errsw", "1");
							rst.put("errmsg", errMsg);
							errSw = true;
						}
					}else{
						rs.close();
						pstmt.close();

						strQuery.setLength(0);
						strQuery.append("select cr_status from cmr1010			\n");
						strQuery.append("where cr_syscd=? and cr_dsncd=?		\n");
						strQuery.append("and cr_rsrcname=?             			\n");
						strQuery.append("and cr_status = '0'           			\n");
						pstmt = conn.prepareStatement(strQuery.toString());
						pstmt.setString(1, _syscd);
						pstmt.setString(2, _dsncd);
						pstmt.setString(3, fileList.get(i).get("rsrcname"));						
						rs = pstmt.executeQuery();

						if (rs.next()){
							++errCnt;
							errMsg = errMsg + "신청중/";
							rst.put("errsw", "1");
							rst.put("errmsg", errMsg);
							errSw = true;
						} 
						else {
							rst.put("_itemid", "insert");
							rst.put("errmsg","정상");
							rst.put("errsw", "0");
						}
					}
			        rs.close();
			        pstmt.close();
				}

				rsval.add(rst);
				rst = null;
				conn.commit();
			}
			rs.close();
			pstmt.close();
			
			conn.commit();
			conn.close();
			
			rs = null;
			pstmt = null;
			conn = null;
						
			return rsval.toArray();
			
			
		} catch (SQLException sqlexception) {
			sqlexception.printStackTrace();
			if (rs != null)     try{rs.close();}catch (Exception ex){ex.printStackTrace();}
			if (pstmt != null)  try{pstmt.close();}catch (Exception ex2){ex2.printStackTrace();}
			if (conn != null){
				try{
					conn.rollback();
					ConnectionResource.release(conn);
				}catch(Exception ex3){
					ex3.printStackTrace();
				}
			}
			ecamsLogger.error("## Cmm1600.getFileList_excel() SQLException START ##");
			ecamsLogger.error("## Error DESC : ", sqlexception);	
			ecamsLogger.error("## Cmm1600.getFileList_excel() SQLException END ##");
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
					ex3.printStackTrace();
				}
			}
			ecamsLogger.error("## Cmm1600.getFileList_excel() Exception START ##");				
			ecamsLogger.error("## Error DESC : ", exception);	
			ecamsLogger.error("## Cmm1600.getFileList_excel() Exception END ##");
			throw exception;
		}finally{
			if (strQuery != null)	strQuery = null;
			if (rsval != null)	rsval = null;
			if (rs != null)     try{rs.close();}catch (Exception ex){ex.printStackTrace();}
			if (pstmt != null)  try{pstmt.close();}catch (Exception ex2){ex2.printStackTrace();}
			if (rs2 != null)     try{rs2.close();}catch (Exception ex1){ex1.printStackTrace();}
			if (pstmt2 != null)  try{pstmt2.close();}catch (Exception ex3){ex3.printStackTrace();}
			if (conn != null){
				try{
					ConnectionResource.release(conn);
				}catch(Exception ex3){
					ecamsLogger.error("## Cmm1600.getFileList_excel() connection release exception ##");
					ex3.printStackTrace();
				}
			}
		}
	}
	
	/*
	private String Cmr0020_Insert(Connection pconn,HashMap<String,String> dataObj) throws SQLException, Exception{
		PreparedStatement pstmt       = null;
		ResultSet         rs          = null;
		StringBuffer      strQuery    = new StringBuffer();
		
		try {
			
			String _ItemId = "";
			String _reStr = "";
			
			strQuery.setLength(0);
			strQuery.append("select cr_itemid,cr_status,cr_lstver from cmr0020    ");
			strQuery.append(" where cr_syscd=? and cr_dsncd=? ");
			strQuery.append("   and cr_rsrcname=?             ");
			
	        pstmt = pconn.prepareStatement(strQuery.toString());
	        pstmt.setString(1, dataObj.get("_syscd"));
	        pstmt.setString(2, dataObj.get("_dsncd"));
	        pstmt.setString(3, dataObj.get("rsrcname"));
	        rs = pstmt.executeQuery();
	        
	        if (rs.next()) {
	        	_ItemId = rs.getString("cr_itemid");
	        	if (rs.getInt("cr_lstver") == 0 && rs.getString("cr_status") == "3"){
	        		_reStr = "";
	        	} else if(rs.getInt("cr_lstver") >0){
	        		_reStr = "버전 "+rs.getString("cr_lstver");
	        	} else if(rs.getInt("cr_status") != 3){
	        		_reStr = "상태값 "+rs.getString("cr_status");
	        	}
	        } else {
	        	_ItemId = "insert";
	        	_reStr = "";
	        }
	        rs.close();
	        pstmt.close();
	        
	        if (_ItemId != "insert") {
	        	strQuery.setLength(0);
        		strQuery.append("update cmr0020 set CR_RSRCCD=?,CR_JOBCD=?,    				");
        		strQuery.append("                   CR_LANGCD=?,CR_CREATOR=?,  				");
        		strQuery.append("                   CR_STORY=?,CR_LASTDATE=SYSDATE, 		");
        		strQuery.append("                   CR_EDITOR=?,CR_NOMODIFY=0,CR_LSTUSR=?	");
        		strQuery.append("where cr_itemid=? \n");
        		pstmt = pconn.prepareStatement(strQuery.toString());
          	    pstmt.setString(1, dataObj.get("_rsrccd"));
          	    pstmt.setString(2, dataObj.get("_jobcd"));
          	    pstmt.setString(3, dataObj.get("_langcd"));
          	    pstmt.setString(4, dataObj.get("_editor"));
          	    pstmt.setString(5, dataObj.get("story"));
          	    pstmt.setString(6, dataObj.get("_editor"));
          	    pstmt.setString(7, dataObj.get("_editor"));
          	    pstmt.setString(8, _ItemId);
          		pstmt.executeUpdate();
          		//ecamsLogger.error("+++++++_ItemId 2+++++++"+_ItemId);
	        }
	        pstmt.close();
            
	        rs = null;
	        pstmt = null;
	        
	        if (_reStr != ""){
	        	return _reStr;
	        } else{
	        	return _ItemId;
	        }
			
			
		} catch (SQLException sqlexception) {
			sqlexception.printStackTrace();
			ecamsLogger.error("## Cmm1600.Cmr0020_Insert() SQLException START ##");
			ecamsLogger.error("## Error DESC : ", sqlexception);	
			ecamsLogger.error("## Cmm1600.Cmr0020_Insert() SQLException END ##");			
			throw sqlexception;
		} catch (Exception exception) {
			exception.printStackTrace();
			ecamsLogger.error("## Cmm1600.Cmr0020_Insert() Exception START ##");				
			ecamsLogger.error("## Error DESC : ", exception);	
			ecamsLogger.error("## Cmm1600.Cmr0020_Insert() Exception END ##");				
			throw exception;
		}
	}
	*/
	
	
	
	public String request_Check_In(ArrayList<HashMap<String,String>> chkInList,HashMap<String,String> etcData) throws SQLException, Exception {
		Connection        conn        = null;
		PreparedStatement pstmt       = null;
		PreparedStatement pstmt2      = null;
		ResultSet         rs          = null;
		StringBuffer      strQuery    = new StringBuffer();
		ConnectionContext connectionContext = new ConnectionResource();
		int				  pstmtcount  = 1;
		AutoSeq			  autoseq	  = new AutoSeq();
		UserInfo		  userInfo	  = new UserInfo();
		String			  AcptNo	  = null;
		String            strReqCd    = "";
		
		String            strLangcd = "";
		String            strComp = "";
		String            strMake = "";
		String            strRteam = "";
//		String            strSqlck = "";
		String            strDoc = "";
		
		int				  i;
		int               lstVer     = 0;
		int               aftVer     = 0;
		int               prcSeq     = 0;
		boolean           dbioSw     = false; 
		HashMap<String,String>	rData = null;

		try {
			conn = connectionContext.getConnection();
			//ecamsLogger.debug("++++++++++List++"+etcData);			
	        for (i=0;i<chkInList.size();i++){	
	        	strReqCd = "03";
	        	lstVer = 0;
	        	aftVer = 1;
	        	
	        	if (!chkInList.get(i).get("_itemid").equals("insert")) {
	        		//ecamsLogger.debug("++++++++++List++"+chkInList.get(i).get("_itemid"));
		        	strQuery.setLength(0);
		        	strQuery.append("select a.cr_status,a.cr_editor,a.cr_nomodify,b.cm_codename,\n");
		        	strQuery.append("       a.cr_lstver,c.cm_info,c.cm_vercnt,c.cm_stepsta      \n");
		        	strQuery.append("  from cmr0020 a,cmm0020 b,cmm0036 c                       \n");
		        	strQuery.append(" where a.cr_itemid = ?                                     \n");
		        	strQuery.append("   and b.cm_macode='CMR0020' and b.cm_micode=a.cr_status   \n");
		        	strQuery.append("   and a.cr_syscd=c.cm_syscd and a.cr_rsrccd=c.cm_rsrccd   \n");
		        	
		        	pstmt = conn.prepareStatement(strQuery.toString());
		        	
		        	pstmt.setString(1, chkInList.get(i).get("_itemid"));
		        	
		        	rs = pstmt.executeQuery();
		        	if (rs.next()){
		        		if (!rs.getString("cr_status").equals("3") && !rs.getString("cr_status").equals("0")) {
		        			AcptNo = "ERROR["+ chkInList.get(i).get("rsrcname") + "]는 이미 요청가능한 상태가 아닙니다 . [" + rs.getString("cm_codename") +"]";
		        			break;
		        		} else {
		        			if (etcData.get("base").equals("3")) {
			        			if (rs.getInt("cr_lstver")>0) strReqCd = "04";
			        			else strReqCd = "03";
			        			lstVer = rs.getInt("cr_lstver");
			        			if (rs.getInt("cm_vercnt") == 0) {
									if (rs.getInt("cr_lstver") >= 9999) aftVer = 1;
									else aftVer = rs.getInt("cr_lstver")+1;
								} else {
									if (rs.getInt("cr_lstver") >= rs.getInt("cm_vercnt")) {
										aftVer = 1;	
									} else aftVer = rs.getInt("cr_lstver")+1;
								}
		        			} else if (etcData.get("base").equals("1")) {
		        				strReqCd = "03";
		        				lstVer = 0;
		        				aftVer = 1;
		        			} else {
		        				strReqCd = "03";
		        				lstVer = 0;
		        				aftVer = 0;
		        			}
		        			prcSeq = rs.getInt("cm_stepsta");
		        		}
		        	}
		        	rs.close();
		        	pstmt.close();
	        	} else {
		        	strQuery.setLength(0);
		        	strQuery.append("select c.cm_info,c.cm_vercnt,c.cm_stepsta from cmm0036 c   \n");
		        	strQuery.append(" where c.cm_syscd=? and c.cm_rsrccd=?                      \n");		        	
		        	pstmt = conn.prepareStatement(strQuery.toString());

		        	pstmt.setString(1, etcData.get("cm_syscd"));
		        	pstmt.setString(2, chkInList.get(i).get("_rsrccd"));
		        	
		        	rs = pstmt.executeQuery();
		        	if (rs.next()){
		        		prcSeq = rs.getInt("cm_stepsta");
		        	}
		        	rs.close();
		        	pstmt.close();
		        	
		        	if (etcData.get("base").equals("3") || etcData.get("base").equals("1")) {
        				strReqCd = "03";
        				lstVer = 0;
        				aftVer = 1;
        			} else {
        				strReqCd = "03";
        				lstVer = 0;
        				aftVer = 0;
        			}
	        	}
	        }
	        if (AcptNo != null) return AcptNo; 
	        
	        Object[] uInfo = userInfo.getUserInfo(etcData.get("UserID"));
        	rData = (HashMap<String, String>) uInfo[0];
        	String strTeam = rData.get("teamcd");
        	String strManId = rData.get("cm_manid");
        	String strQryCd = etcData.get("ReqCD");
        	rData = null;
        	uInfo = null;

        	String strTitle = "";
        	strQuery.setLength(0);
        	strQuery.append("select cm_codename from cmm0020     \n");
        	strQuery.append(" where cm_macode='REQUEST'          \n");
        	strQuery.append("   and cm_micode=?                  \n");
        	pstmt = conn.prepareStatement(strQuery.toString());
        	pstmt.setString(1, etcData.get("ReqCD"));        	
        	rs = pstmt.executeQuery();
        	if (rs.next()){
        		strTitle = rs.getString("cm_codename");
        	}
        	rs.close();
        	pstmt.close();
        	
        	
        	
        	
        	int wkC = chkInList.size()/300;
	        int wkD = chkInList.size()%300;
	        if (wkD>0) wkC = wkC + 1;
            String svAcpt[] = null; 
            svAcpt = new String [wkC];
            for (int j=0;wkC>j;j++) {
            	do {
    		        AcptNo = autoseq.getSeqNo(conn,strQryCd);    		        
    		        
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
            String strRsrc = "";
        	int    cnt = 0;
        	int    pri = 0;
        	
            autoseq = null;
            conn.setAutoCommit(false);			
        	boolean insSw = false;
        	for (i=0;i<chkInList.size();i++){
        		insSw = false;
        		if (i == 0) insSw = true;
        		else {
        			wkC = i%300;
        			if (wkC == 0) insSw = true;
        		}
        		
        		if (insSw == true) {        			
        			if (i>=300) {        	        	
        	        	strQuery.setLength(0);
        	        	strQuery.append("insert into cmr9900 ");
        	        	strQuery.append("(CR_ACPTNO,CR_SEQNO,CR_LOCAT,CR_CONFNAME,CR_TEAM,CR_TEAMCD,CR_STATUS,CR_CONGBN, ");
        	        	strQuery.append("CR_COMMON, CR_BLANK,CR_EMGER,CR_HOLI,CR_SGNGBN,CR_ORGSTEP,CR_BASEUSR, CR_PRCSW) ");
        	        	strQuery.append("(SELECT ?,1,lpad(CM_seqno,2,'0'),CM_NAME,decode(CM_JOBCD,null,'MASTER',CM_JOBCD) CM_JOBCD,CM_GUBUN, ");
        	        	strQuery.append("'0',CM_COMMON,CM_COMMON,CM_BLANK,CM_EMG,CM_HOLIDAY,CM_POSITION, CM_ORGSTEP,decode(CM_JOBCD,null,'MASTER',CM_JOBCD) CM_JOBCD,CM_PRCSW ");
        	        	strQuery.append("FROM CMm0060 ");
        	        	strQuery.append("WHERE CM_SYSCD= ? ");
        	        	strQuery.append("AND CM_REQCD= ? ");
        	        	
        	        	if (strManId.equals("N")){
        	        		strQuery.append("AND CM_MANID='2') ");
        	        	}
        	        	else{
        	        		strQuery.append("AND CM_MANID='1') ");
        	        	}
        	        	
        	        	//strQuery.append("AND CM_GUBUN='1') ");

        	        	pstmt = conn.prepareStatement(strQuery.toString());
        	        	//pstmt =  new LoggableStatement(conn, strQuery.toString());
        	        	pstmtcount = 1;
        	        	pstmt.setString(pstmtcount++, AcptNo);
        	        	pstmt.setString(pstmtcount++, chkInList.get(0).get("_syscd"));
        	        	pstmt.setString(pstmtcount++, etcData.get("ReqCD"));
        	        	////ecamsLogger.error(((LoggableStatement)pstmt).getQueryString());
        	        	pstmt.executeUpdate();
        	        	pstmt.close();

        	        	
        	        	strQuery.setLength(0);
        	        	
        	        	strQuery.append("update cmr9900 set cr_team=?,cr_baseusr=?          ");
        	        	strQuery.append(" where cr_acptno=? and cr_teamcd='2'               ");
        	        	
        	        	pstmt = conn.prepareStatement(strQuery.toString());
        	        	pstmtcount = 1;
        	        	pstmt.setString(pstmtcount++, etcData.get("UserID"));
        	        	pstmt.setString(pstmtcount++, etcData.get("UserID"));
        	        	pstmt.setString(pstmtcount++, AcptNo);
        	        	
        	        	pstmt.executeUpdate();
        	        	pstmt.close();
        	        	
        	        	strQuery.setLength(0);
        	        	
        	        	strQuery.append("insert into cmr9900 ");
        	        	strQuery.append("(CR_ACPTNO,CR_SEQNO,CR_LOCAT,CR_STATUS,CR_CONFUSR) ");
        	        	strQuery.append("values ( ");
        	        	strQuery.append("?, '1', '00', '0', '9999' ) ");
        	        	
        	        	pstmt = conn.prepareStatement(strQuery.toString());
        	        	pstmtcount = 1;
        	        	pstmt.setString(pstmtcount++, AcptNo);
        	        	
        	        	pstmt.executeUpdate();
        	        	pstmt.close();
        				
        	        	strQuery.setLength(0);
        	        	
        	        	strQuery.append("Begin CMR9900_STR ( ");
        	        	strQuery.append("?, '000000', '', '9', ?, '1' ); End;");
        	        	pstmt = conn.prepareStatement(strQuery.toString());
        	        	pstmtcount = 1;
        	        	pstmt.setString(pstmtcount++, AcptNo);
        	        	pstmt.setString(pstmtcount++, etcData.get("ReqCD"));
        	        	
        	        	pstmt.executeUpdate();
        	        	pstmt.close();
        			}
        			dbioSw = false;
        			wkC = i/300;
        			AcptNo = svAcpt[wkC];
        			//ecamsLogger.debug("+++++++++AcptNo++++++"+AcptNo);
        			pstmtcount = 1;
                	strQuery.setLength(0);
                	strQuery.append("insert into cmr1000 ");
                	strQuery.append("(CR_ACPTNO,CR_SYSCD,CR_SYSGB,CR_JOBCD,CR_ACPTDATE,CR_STATUS,CR_TEAMCD,CR_QRYCD, ");
                	strQuery.append("CR_PASSOK,CR_PASSCD,CR_BEFJOB,CR_EMGCD,CR_EDITOR,CR_SAYU,CR_PASSSUB,CR_SAYUCD, ");
                	strQuery.append("CR_SVRCD,CR_SVRSEQ) values ( ");
                	strQuery.append("?,?,?,?,sysdate,'0',?,?,   '0',?,'N','11',?,'일괄등록',?,'',  ?,?) ");
                	
                	pstmt = conn.prepareStatement(strQuery.toString());
                	//pstmt = new LoggableStatement(conn,strQuery.toString());
                	
                	pstmt.setString(pstmtcount++, AcptNo);
                	pstmt.setString(pstmtcount++, etcData.get("cm_syscd"));
                	pstmt.setString(pstmtcount++, etcData.get("sysgb"));
                	pstmt.setString(pstmtcount++, chkInList.get(0).get("_jobcd"));                	
                	pstmt.setString(pstmtcount++, strTeam);                	
                	pstmt.setString(pstmtcount++, etcData.get("ReqCD"));
                	
                	pstmt.setString(pstmtcount++, strTitle);
                	pstmt.setString(pstmtcount++, etcData.get("UserID"));
                	pstmt.setString(pstmtcount++, etcData.get("base"));//CR_PASSSUB
                	
                	pstmt.setString(pstmtcount++, etcData.get("svrcd"));
                	pstmt.setInt(pstmtcount++, Integer.parseInt(etcData.get("svrseq")));
                	
                	////ecamsLogger.error(((LoggableStatement)pstmt).getQueryString());
                	pstmt.executeUpdate();
                	
                	pstmt.close();
        		}
        		
        		//#############################################################
    			strLangcd = "";
    			strComp = "";
    			strRteam = "";
//    			strSqlck = "";
         		strDoc = "";
            	
            	strQuery.setLength(0);
            	strQuery.append("select cm_micode from cmm0020     \n");
            	strQuery.append(" where cm_macode='LANGUAGE'          \n");
            	strQuery.append("   and cm_codename=?                  \n");
            	pstmt = conn.prepareStatement(strQuery.toString());
            	//pstmt =  new LoggableStatement(conn, strQuery.toString());
            	pstmt.setString(1, chkInList.get(i).get("lang"));   
            	//ecamsLogger.error(((LoggableStatement)pstmt).getQueryString());
            	rs = pstmt.executeQuery();
            	if (rs.next()){
            		strLangcd = rs.getString("cm_micode");
            	}
            	rs.close();
            	pstmt.close();
            	
            	strQuery.setLength(0);
            	strQuery.append("select cm_micode from cmm0020     \n");
            	strQuery.append(" where cm_macode='DOCUMENT'          \n");
            	strQuery.append("   and cm_codename=?                  \n");
            	pstmt = conn.prepareStatement(strQuery.toString());
            	pstmt.setString(1, chkInList.get(i).get("doc"));        	
            	rs = pstmt.executeQuery();
            	if (rs.next()){
            		strDoc = rs.getString("cm_micode");
            	}
            	rs.close();
            	pstmt.close();
            	
//            	strQuery.setLength(0);
//            	strQuery.append("select cm_micode from cmm0020     \n");
//            	strQuery.append(" where cm_macode='SQLCHECK'          \n");
//            	strQuery.append("   and cm_codename=?                  \n");
//            	pstmt = conn.prepareStatement(strQuery.toString());
//            	pstmt.setString(1, chkInList.get(i).get("sqlck"));        	
//            	rs = pstmt.executeQuery();
//            	if (rs.next()){
//            		strSqlck = rs.getString("cm_micode");
//            	}
//            	rs.close();
//            	pstmt.close();
            	
            	
            	strQuery.setLength(0);
            	strQuery.append("select cm_micode from cmm0020     \n");
                strQuery.append("where  cm_macode='ETCTEAM' 		\n");
                strQuery.append("and cm_codename=?			 		\n");
            	pstmt = conn.prepareStatement(strQuery.toString());
            	pstmt.setString(1, chkInList.get(i).get("rteam"));        	
            	rs = pstmt.executeQuery();
            	if (rs.next()){
            		strRteam = rs.getString("cm_micode");
            	}
            	rs.close();
            	pstmt.close();
            	
            	
            	strQuery.setLength(0);
            	strQuery.append("select cm_compcd from cmm0400     \n");
            	strQuery.append(" where cm_syscd = ?          \n");
            	strQuery.append("   and cm_compscript=? and cm_gubun = '01'                 \n");
            	pstmt = conn.prepareStatement(strQuery.toString());
            	pstmt.setString(1, etcData.get("cm_syscd"));
            	pstmt.setString(2, chkInList.get(i).get("comp")); 
            	rs = pstmt.executeQuery();
            	if (rs.next()){
            		strComp = rs.getString("cm_compcd");
            	}
            	rs.close();
            	pstmt.close();
            	
            	strQuery.setLength(0);
            	strQuery.append("select cm_compcd from cmm0400     \n");
            	strQuery.append(" where cm_syscd = ?          \n");
            	strQuery.append("   and cm_compscript=?  and cm_gubun = '02'                \n");
            	pstmt = conn.prepareStatement(strQuery.toString());
            	pstmt.setString(1, etcData.get("cm_syscd"));
            	pstmt.setString(2, chkInList.get(i).get("makescript"));  
            	rs = pstmt.executeQuery();
            	if (rs.next()){
            		strMake = rs.getString("cm_compcd");
            	}
            	rs.close();
            	pstmt.close();
            	       	
            
            	
            	
            	//#########################################################
            	strQuery.setLength(0);
            	strQuery.append("insert into cmr1010 ");
            	strQuery.append("(CR_ACPTNO,CR_SERNO,CR_SYSCD,CR_SYSGB,CR_JOBCD,CR_STATUS,CR_QRYCD,        \n");
            	//strQuery.append("CR_RSRCCD,CR_LANGCD,CR_DSNCD,CR_RSRCNAME,CR_RSRCNAM2,CR_SRCCHG,CR_SRCCMP, \n");
            	strQuery.append("CR_RSRCCD,CR_DSNCD,CR_RSRCNAME,CR_RSRCNAM2,CR_SRCCHG,CR_SRCCMP, \n");
            	strQuery.append("CR_PRIORITY,CR_VERSION,CR_BEFVER,CR_CONFNO,CR_EDITOR,  \n");
            	strQuery.append("CR_BASENO,CR_BASEITEM,CR_ITEMID,CR_EDITCON,CR_LANGCD,CR_COMPILE,CR_RTEAMCD,CR_DOCUMENT,CR_MASTER,CR_DSNCD2,CR_MAKESCRIPT,CR_DSNCD2HOME) values (   \n");
            	strQuery.append("?,?,?,?,?,'0',?,  ?,?,?,?,?,'Y', ?,?,?,?,?,  ?,?,?,?,?,  ?,?,?,?,?,?,?) \n");
            	
            	pstmtcount = 1;
            	pstmt = conn.prepareStatement(strQuery.toString());
            	//pstmt = new LoggableStatement(conn,strQuery.toString());
            	            	
            	pstmt.setString(pstmtcount++, AcptNo);
            	pstmt.setInt(pstmtcount++, i + 1);
            	pstmt.setString(pstmtcount++, etcData.get("cm_syscd"));
            	pstmt.setString(pstmtcount++, etcData.get("sysgb"));
            	pstmt.setString(pstmtcount++, chkInList.get(i).get("_jobcd"));
            	pstmt.setString(pstmtcount++, strReqCd);
            	
            	pstmt.setString(pstmtcount++, chkInList.get(i).get("_rsrccd"));
            	//pstmt.setString(pstmtcount++, chkInList.get(i).get("_langcd"));
            	pstmt.setString(pstmtcount++, chkInList.get(i).get("_dsncd"));
            	pstmt.setString(pstmtcount++, chkInList.get(i).get("rsrcname"));
            	pstmt.setString(pstmtcount++, chkInList.get(i).get("rsrcname"));
            	if (strReqCd.equals("04") || strReqCd.equals("03")) {
            		pstmt.setString(pstmtcount++,"1");
            	} else {
            		pstmt.setString(pstmtcount++,"0");
            	}
            	
            	pstmt.setInt(pstmtcount++, prcSeq);            	
            	pstmt.setInt(pstmtcount++, aftVer);
            	pstmt.setInt(pstmtcount++, lstVer);
            	pstmt.setString(pstmtcount++,AcptNo);
            	//pstmt.setString(pstmtcount++, etcData.get("UserID"));
            	pstmt.setString(pstmtcount++, chkInList.get(i).get("userid"));
            	
//            	pstmt.setString(pstmtcount++,chkInList.get(i).get("cr_acptno"));
            	pstmt.setString(pstmtcount++,AcptNo);
            	if (chkInList.get(i).get("_itemid") != null && chkInList.get(i).get("_itemid") != "" &&
            		!chkInList.get(i).get("_itemid").equals("insert")) {
            		pstmt.setString(pstmtcount++, chkInList.get(i).get("_itemid"));
                	pstmt.setString(pstmtcount++, chkInList.get(i).get("_itemid"));
            	} else {
            		pstmt.setString(pstmtcount++, "");
                	pstmt.setString(pstmtcount++, "");
            	}  
            	pstmt.setString(pstmtcount++, chkInList.get(i).get("story"));

            	pstmt.setString(pstmtcount++, strLangcd);
            	pstmt.setString(pstmtcount++, strComp);
            	pstmt.setString(pstmtcount++, strRteam);
            	pstmt.setString(pstmtcount++, strDoc);
            	pstmt.setString(pstmtcount++, chkInList.get(i).get("master"));
            	pstmt.setString(pstmtcount++, chkInList.get(i).get("etcdsn"));
            	pstmt.setString(pstmtcount++, strMake);
            	pstmt.setString(pstmtcount++, chkInList.get(i).get("etcdsnhome"));
            	
            	//ecamsLogger.error(((LoggableStatement)pstmt).getQueryString());
            	pstmt.executeUpdate();
            	pstmt.close();
            	
            	if (chkInList.get(i).get("_itemid") != null && chkInList.get(i).get("_itemid") != "" && 
            		!chkInList.get(i).get("_itemid").equals("insert")) {
	            	pstmtcount = 1;
	            	strQuery.setLength(0);
//	            	strQuery.append("update cmr0020 set cr_status='7',            \n");
	            	strQuery.append("update cmr0020 set cr_status='3',            \n");	// 20231130 7 -> 3으로 변경 요청 
	            	// 최초이행이나 목록이행일 경우 기록 삭제 버전=0
	            	if (etcData.get("base").equals("1") || etcData.get("base").equals("2")) 
	            		strQuery.append("cr_lstver= 0,                            \n");
	            	strQuery.append("cr_editor= ?                                 \n");
	            	
	            	strQuery.append("where cr_itemid= ?                           \n");
	            	
	            	pstmt = conn.prepareStatement(strQuery.toString());
	            	
	            	//pstmt.setString(pstmtcount++, etcData.get("UserID"));
	            	pstmt.setString(pstmtcount++, chkInList.get(i).get("userid"));
	            	pstmt.setString(pstmtcount++, chkInList.get(i).get("cr_itemid"));
	            	pstmt.executeUpdate();
	            	pstmt.close();
	            	// 최초이행이나 목록이행일 경우 기록 삭제
	            	if (etcData.get("base").equals("1") || etcData.get("base").equals("2")) {
	            		strQuery.setLength(0);
	            		strQuery.append("delete cmr0021 where cr_itemid=?    \n");
	            		pstmt = conn.prepareStatement(strQuery.toString());
		            	pstmt.setString(1, chkInList.get(i).get("cr_itemid"));
		            	pstmt.executeUpdate();
		            	pstmt.close();
		            	
	            		strQuery.setLength(0);
	            		strQuery.append("delete cmr0025 where cr_itemid=?    \n");
	            		pstmt = conn.prepareStatement(strQuery.toString());
		            	pstmt.setString(1, chkInList.get(i).get("cr_itemid"));
		            	pstmt.executeUpdate();
		            	pstmt.close();
	            	}
            	}
            	strComp = "";
            	strMake = "";
        	}

        	strQuery.setLength(0);
        	strQuery.append("insert into cmr9900 ");
        	strQuery.append("(CR_ACPTNO,CR_SEQNO,CR_LOCAT,CR_CONFNAME,CR_TEAM,CR_TEAMCD,CR_STATUS,CR_CONGBN, ");
        	strQuery.append("CR_COMMON, CR_BLANK,CR_EMGER,CR_HOLI,CR_SGNGBN,CR_ORGSTEP,CR_BASEUSR, CR_PRCSW) ");
        	strQuery.append("(SELECT ?,1,lpad(CM_seqno,2,'0'),CM_NAME,decode(CM_JOBCD,null,'MASTER',CM_JOBCD) CM_JOBCD,CM_GUBUN, ");
        	strQuery.append("'0',CM_COMMON,CM_COMMON,CM_BLANK,CM_EMG,CM_HOLIDAY,CM_POSITION, CM_ORGSTEP,decode(CM_JOBCD,null,'MASTER',CM_JOBCD) CM_JOBCD,CM_PRCSW ");
        	strQuery.append("FROM CMm0060 ");
        	strQuery.append("WHERE CM_SYSCD= ? ");
        	strQuery.append("AND CM_REQCD= ? ");
        	
        	if (strManId.equals("N")){
        		strQuery.append("AND CM_MANID='2' ");
        	}
        	else{
        		strQuery.append("AND CM_MANID='1' ");
        	}
        	strQuery.append(") ");
        	//strQuery.append("AND CM_GUBUN='1') ");

        	pstmt = conn.prepareStatement(strQuery.toString());
        	//pstmt =  new LoggableStatement(conn, strQuery.toString());
        	pstmtcount = 1;
        	pstmt.setString(pstmtcount++, AcptNo);
        	pstmt.setString(pstmtcount++, chkInList.get(0).get("_syscd"));
        	pstmt.setString(pstmtcount++, etcData.get("ReqCD"));
        	////ecamsLogger.error(((LoggableStatement)pstmt).getQueryString());
        	pstmt.executeUpdate();
        	pstmt.close();
        	
        	strQuery.setLength(0);
        	
        	strQuery.append("insert into cmr9900 ");
        	strQuery.append("(CR_ACPTNO,CR_SEQNO,CR_LOCAT,CR_STATUS,CR_CONFUSR) ");
        	strQuery.append("values ( ");
        	strQuery.append("?, '1', '00', '0', '9999' ) ");
        	
        	pstmt = conn.prepareStatement(strQuery.toString());
        	pstmtcount = 1;
        	pstmt.setString(pstmtcount++, AcptNo);
        	pstmt.executeUpdate();
        	pstmt.close();
			
        	strQuery.setLength(0);
        	strQuery.append("Begin CMR9900_STR ( ");
        	strQuery.append("?, '000000', '', '9', ?, '1' ); End;");
        	pstmt = conn.prepareStatement(strQuery.toString());
        	pstmtcount = 1;
        	pstmt.setString(pstmtcount++, AcptNo);
        	pstmt.setString(pstmtcount++, etcData.get("ReqCD"));
        	pstmt.executeUpdate();
        	
        	pstmt.close();
        	conn.commit();
        	conn.close();
        	uInfo = null;
        	conn = null;
			pstmt = null;
			rs = null;
        	//ecamsLogger.debug("+++++++++Request E N D+++");
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
					ecamsLogger.error("## Cmm1600.request_Check_In() connection release exception ##");
					ex3.printStackTrace();
				}				
			}
			ecamsLogger.error("## Cmm1600.request_Check_In() SQLException START ##");
			ecamsLogger.error("## Error DESC : ", sqlexception);	
			ecamsLogger.error("## Cmm1600.request_Check_In() SQLException END ##");			
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
					ecamsLogger.error("## Cmm1600.request_Check_In() connection release exception ##");
					ex3.printStackTrace();
				}				
			}			
			ecamsLogger.error("## Cmm1600.request_Check_In() Exception START ##");				
			ecamsLogger.error("## Error DESC : ", exception);	
			ecamsLogger.error("## Cmm1600.request_Check_In() Exception END ##");				
			throw exception;
		}finally{
			if (strQuery != null) 	strQuery = null;
			if (rs != null)     try{rs.close();}catch (Exception ex){ex.printStackTrace();}
			if (pstmt != null)  try{pstmt.close();}catch (Exception ex2){ex2.printStackTrace();}
			if (conn != null){
				try{					
					ConnectionResource.release(conn);
				}catch(Exception ex3){
					ecamsLogger.error("## Cmm1600.request_Check_In() connection release exception ##");
					ex3.printStackTrace();
				}
			}
		}		
	}
	public int execShell(String shFile,String parmName,boolean viewSw) throws SQLException, Exception {
		Connection        conn        = null;
		PreparedStatement pstmt       = null;
		ResultSet         rs          = null;
		StringBuffer      strQuery    = new StringBuffer();
		
		SystemPath		  cTempGet	  = new SystemPath();
		String			  tmpPath = "";
		String			  strBinPath = "";
		String            ecamsIP = "";
		String            ecamsPort = "";
		int               retCD = 0;
		
		File shfile=null;
		String  shFileName = "";
		OutputStreamWriter writer = null;
		String[] strAry = null;
		Runtime  run = null;
		Process p = null;

		String outFile = "";
		File outf = null;
		ConnectionContext connectionContext = new ConnectionResource();
		try {
			conn = connectionContext.getConnection();
			
			tmpPath = cTempGet.getTmpDir_conn("99",conn);
			strBinPath = cTempGet.getTmpDir_conn("14",conn);
			
			strQuery.setLength(0);
			strQuery.append("select cm_ipaddr,cm_port from cmm0010 where cm_stno='ECAMS' ");
			pstmt = conn.prepareStatement(strQuery.toString());	
            rs = pstmt.executeQuery();
            if (rs.next()) {
            	ecamsIP = rs.getString("cm_ipaddr");
            	ecamsPort = rs.getString("cm_port");
            }
            rs.close();
            pstmt.close();
            conn.close();
            
            if (ecamsIP.length()>0) { 
				shFileName = tmpPath + "/" + shFile; 			
				shfile = new File(shFileName);
				
				if( !(shfile.isFile()) )              //File존재여부
				{
					shfile.createNewFile();          //File생성
				}
				
				if (viewSw) {
					outFile = shFileName.replace(".sh", ".out"); 			
					outf = new File(outFile);
					
					if((outf.isFile()))              //File존재여부
					{
						outf.delete();         //File생성
					}
					parmName = parmName + " >"+outFile;
				}
				writer = new OutputStreamWriter( new FileOutputStream(shFileName));
				writer.write("cd "+strBinPath +"\n");
				writer.write("./ecams_batexec "+ ecamsIP + " " + ecamsPort + " 0 \""+ parmName + "\" \n");
				//writer.write("rtval=`./ecams_quesend \""+ parmName + "\"`\n");
				//writer.write("./ecams_quesend \"ecams_chkdoc "+ parmName + " B\" \n");
				writer.write("exit $?\n");
				writer.close();
				
				strAry = new String[3];
				strAry[0] = "chmod";
				strAry[1] = "777";
				strAry[2] = shFileName;			
				
				run = Runtime.getRuntime();
	
				
				p = run.exec(strAry);
				p.waitFor();
				
				run = Runtime.getRuntime();
				
				strAry = new String[2];
				
				strAry[0] = "/bin/sh";
				strAry[1] = shFileName;
				
				p = run.exec(strAry);
				p.waitFor();
	
				if (p.exitValue() == 0) {
					shfile.delete();
				}
				retCD = p.exitValue();
            } else retCD = 9;
            
            conn = null;
            
			return retCD;		
		} catch (SQLException sqlexception) {
			sqlexception.printStackTrace();
			ecamsLogger.error("## Cmm1600.execShell() SQLException START ##");
			ecamsLogger.error("## Error DESC : ", sqlexception);	
			ecamsLogger.error("## Cmm1600.execShell() SQLException END ##");			
			throw sqlexception;
		} catch (Exception exception) {
			exception.printStackTrace();
			ecamsLogger.error("## Cmm1600.execShell() Exception START ##");				
			ecamsLogger.error("## Error DESC : ", exception);	
			ecamsLogger.error("## Cmm1600.execShell() Exception END ##");				
			throw exception;
		}finally{
			if (strQuery != null)	strQuery = null;
			if (rs != null)     try{rs.close();}catch (Exception ex){ex.printStackTrace();}
			if (pstmt != null)  try{pstmt.close();}catch (Exception ex2){ex2.printStackTrace();}
			if (conn != null){
				try{
					ConnectionResource.release(conn);
				}catch(Exception ex3){
					ecamsLogger.error("## Cmm1600.execShell() connection release exception ##");
					ex3.printStackTrace();
				}
			}
		}
	}//execShell

	public int execShell_web(String shFile,String parmName,boolean viewSw) throws Exception {
		SystemPath		  cTempGet	  = new SystemPath();
		String			  tmpPath = "";
		String			  strBinPath = "";
		File shfile=null;
		String  shFileName = "";
		OutputStreamWriter writer = null;
		String[] strAry = null;
		Runtime  run = null;
		Process p = null;

		String outFile = "";
		File outf = null;
		//ByteArrayOutputStream fileStream = null;
		
		try {
			
			tmpPath = cTempGet.getTmpDir("99");
			strBinPath = cTempGet.getTmpDir("14");
			
			shFileName = tmpPath + "/" + shFile; 			
			shfile = new File(shFileName);
			
			if( !(shfile.isFile()) )              //File존재여부
			{
				shfile.createNewFile();          //File생성
			}
			
			if (viewSw) {
				outFile = shFileName.replace(".sh", ".out"); 			
				outf = new File(outFile);
				
				if((outf.isFile()))              //File존재여부
				{
					outf.delete();         //File생성
				}
				parmName = parmName + " >"+outFile;
			}
			writer = new OutputStreamWriter( new FileOutputStream(shFileName));
			writer.write("cd "+strBinPath +"\n");
			if (viewSw) writer.write(parmName + ">"+outFile+"\n");
			else writer.write(parmName + "\n");
			writer.write("exit $?\n");
			writer.close();
			
			strAry = new String[3];
			strAry[0] = "chmod";
			strAry[1] = "777";
			strAry[2] = shFileName;			
			
			run = Runtime.getRuntime();

			
			p = run.exec(strAry);
			p.waitFor();
			
			run = Runtime.getRuntime();
			
			strAry = new String[2];
			
			strAry[0] = "/bin/sh";
			strAry[1] = shFileName;
			
			p = run.exec(strAry);
			p.waitFor();

			if (p.exitValue() == 0) {
				shfile.delete();
			}								
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
	public String execCmd(String cmdText,String UserId,String gbnCd,boolean viewSw) throws SQLException, Exception {
		SystemPath		  cTempGet	  = new SystemPath();
		String			  tmpPath = "";
		String			  strBinPath = "";
		File shfile=null;
		String  shFileName = "";
		OutputStreamWriter writer = null;
		String[] strAry = null;
		Runtime  run = null;
		Process p = null;
		String outFile = "";
		File outf = null;
		
		try {
        	if (gbnCd.equals("A")) {
				int diffRst = execShell(UserId+"apcmd.sh",cmdText,viewSw);
				if (diffRst != 0) {
					return "커맨드 수행 중 오류가 발생하였습니다."+ Integer.toString(diffRst);
				}
        	} else {
        		tmpPath = cTempGet.getTmpDir("99");
    			strBinPath = cTempGet.getTmpDir("14");
    			
    			shFileName = tmpPath + "/" + UserId + "webcmd.sh"; 			
    			shfile = new File(shFileName);
    			
    			if( !(shfile.isFile()) ) {              //File존재여부	    			{
    				shfile.createNewFile();          //File생성
    			}
    			if (viewSw) {
    				outFile = tmpPath + "/" + UserId + "webcmd.out"; 			
    				outf = new File(outFile);
	    			
	    			if((outf.isFile()) ) {              //File존재여부	    			{
	    				outf.delete();
	    			}
    			}
    			writer = new OutputStreamWriter( new FileOutputStream(shFileName));
    			writer.write("cd "+strBinPath +"\n");
    			if (viewSw) writer.write(cmdText + ">"+outFile+"\n");
    			else writer.write(cmdText + "\n");
    			writer.write("exit $?   \n");
    			writer.close();
    			
    			strAry = new String[3];
    			strAry[0] = "chmod";
    			strAry[1] = "777";
    			strAry[2] = shFileName;			
    			
    			run = Runtime.getRuntime();
    			
    			p = run.exec(strAry);
    			p.waitFor();
    			
    			run = Runtime.getRuntime();
    			
    			strAry = new String[2];
    			
    			strAry[0] = "/bin/sh";
    			strAry[1] = shFileName;
    			
    			p = run.exec(strAry);
    			p.waitFor();

    			if (p.exitValue() == 0) {
    				shfile.delete();
    			}	
        	}
        	return "0";
		} catch (Exception exception) {
			exception.printStackTrace();
			ecamsLogger.error("## Cmm1600.execCmd() Exception START ##");				
			ecamsLogger.error("## Error DESC : ", exception);	
			ecamsLogger.error("## Cmm1600.execCmd() Exception END ##");				
			throw exception;
		}finally{
			
		}		
	}	

	public String getRemoteUrl(String cmdText, String UserId, String gbnCd, String savePath)
			throws SQLException, Exception {
		try {
			HttpCall httpcall = new HttpCall();
			String retMsg = "";
			HashMap<String, String> etcObj = new HashMap<String, String>();
			etcObj.put("reqMethod", "POST");
			etcObj.put("savePath", savePath);
			if (!"http".equals(cmdText.substring(0, 4))) {
				etcObj.put("reqMethod", cmdText.substring(0, cmdText.indexOf(" ")));

				if ("GET".equals(etcObj.get("reqMethod")) || "DELETE".equals(etcObj.get("reqMethod"))) {
					etcObj.put("remoteURL", cmdText.substring(cmdText.indexOf("http")));
				} else {
					if (cmdText.indexOf("?") >= 0) {
						etcObj.put("remoteURL", cmdText.substring(cmdText.indexOf("http"), cmdText.indexOf("?")));
						etcObj.put("sendData", cmdText.substring(cmdText.indexOf("?") + 1));
					} else {
						etcObj.put("remoteURL", cmdText.substring(cmdText.indexOf("http")));
					}
				}
			} else {
				etcObj.put("remoteURL", cmdText);
			}

			retMsg = httpcall.httpCall_common(etcObj);
			return retMsg;
		} catch (RuntimeException exception) { // 취약점 수정
			exception.getStackTrace();
			ecamsLogger.error("## Cmm1600.getRemoteUrl() Exception START ##");
			ecamsLogger.error("## Error DESC : ", exception);
			ecamsLogger.error("## Cmm1600.getRemoteUrl() Exception END ##");
			throw exception;
		} catch (Exception exception) {
			exception.getStackTrace();
			ecamsLogger.error("## Cmm1600.getRemoteUrl() Exception START ##");
			ecamsLogger.error("## Error DESC : ", exception);
			ecamsLogger.error("## Cmm1600.getRemoteUrl() Exception END ##");
			throw exception;
		} finally {

		}
	}

 	public String fileAttUpdt(String cmdText) throws SQLException, Exception {
		File shfile=null;
		String[] strAry = null;
		Runtime  run = null;
		Process p = null;

		try {
			cmdText = cmdText.replace("\\\\", "\\");
			shfile = new File(cmdText);

			if( !(shfile.isFile()) ) {
				ecamsLogger.error("#### "+cmdText+"### return : 1");
				shfile.deleteOnExit();
				shfile = null;
				return "1";
			}
			shfile.deleteOnExit();
			shfile = null;

			strAry = new String[3];
			strAry[0] = "chmod";
			strAry[1] = "777";
			strAry[2] = cmdText;

			run = Runtime.getRuntime();


			p = run.exec(strAry);
			p.waitFor();

			run = null;
			p = null;
			strAry = null;
			ecamsLogger.error("#### "+cmdText+"### return : 0");
	        return "0";
		} catch (IOException exception) { //취약점 수정
			exception.getStackTrace();
			ecamsLogger.error("## Cmm1600.fileAttUpdt() Exception START ##");
			ecamsLogger.error("## Error DESC : ", exception);
			ecamsLogger.error("## Cmm1600.fileAttUpdt() Exception END ##");
			throw exception;
		} catch (Exception exception) {
			exception.getStackTrace();
			ecamsLogger.error("## Cmm1600.fileAttUpdt() Exception START ##");
			ecamsLogger.error("## Error DESC : ", exception);
			ecamsLogger.error("## Cmm1600.fileAttUpdt() Exception END ##");
			throw exception;
		}finally {
		}
	}

 	public String getEncrypt(HashMap<String, String> cmdData) throws SQLException, Exception {
		try {

			Encryptor oEncryptor = Encryptor.instance();
			String cmdText = cmdData.get("txtcmd");
			String retMsg = "ER";
			if ("EA".equals(cmdData.get("gbnCd"))) { // Encrypt(AES)
				retMsg = oEncryptor.strGetEncrypt_AES256(cmdText);
			} else if ("DA".equals(cmdData.get("gbnCd"))) { // Decrypt(AES)
				retMsg = oEncryptor.strGetDecrypt_AES256(cmdText);
			} else if ("ED".equals(cmdData.get("gbnCd"))) { // Encrypt(DES)
				retMsg = oEncryptor.strGetEncrypt(cmdText);
			} else if ("DD".equals(cmdData.get("gbnCd"))) { // Decrypt(DES)
				retMsg = oEncryptor.strGetDecrypt(cmdText);
			} else if ("ES".equals(cmdData.get("gbnCd"))) { // Encrypt(SHA)
				retMsg = oEncryptor.SHA256(cmdText);
			}
			oEncryptor = null;
			return retMsg;
		} catch (IOException exception) {
			exception.getStackTrace();
			ecamsLogger.error("## Cmm1600.getEncryptor() Exception START ##");
			ecamsLogger.error("## Error DESC : ", exception);
			ecamsLogger.error("## Cmm1600.getEncryptor() Exception END ##");
			return "ER";
		} catch (Exception exception) {
			exception.getStackTrace();
			ecamsLogger.error("## Cmm1600.getEncryptor() Exception START ##");
			ecamsLogger.error("## Error DESC : ", exception);
			ecamsLogger.error("## Cmm1600.getEncryptor() Exception END ##");
			return "ER";
		} finally {

		}
	}


 	public String getUnicode(HashMap<String, String> cmdData) throws SQLException, Exception {
		try {
			String cmdText = cmdData.get("txtcmd");
			String retMsg = "";
			StringBuilder sb = new StringBuilder();
			int i = 0;
			if ("KU".equals(cmdData.get("gbnCd"))) { //Korea-->Unicode
				char[] code = new char[cmdText.length()*3];
				int[] realCode = new int[cmdText.length()*3];

				for (i=0;cmdText.length()>i;i++) {
					code[i] = cmdText.charAt(i);
					realCode[i] = (int)(code[i]);
					sb.append("\\u"+Integer.toHexString((int)(code[i])));
				}
				retMsg = sb.toString();
			} else { //Unicode-->Korea
				char ch;
				int len = cmdText.length();

				for (i=0;len>i;i++) {
					ch = cmdText.charAt(i);
					if (ch == '\\' && cmdText.charAt(i+1) == 'u') {
						if (cmdText.charAt(i+4) == '\\' && cmdText.charAt(i+5) == 'u') {
							sb.append((char) Integer.parseInt(cmdText.substring(i+2,i+4), 16));
							i+=3;
						} else {
							sb.append((char) Integer.parseInt(cmdText.substring(i+2,i+6), 16));
							i+=5;
						}

						continue;
					}
					sb.append(ch);
				}
			    retMsg = sb.toString();
			}
        	return retMsg;
		} catch (RuntimeException exception) { //취약점 수정
			exception.getStackTrace();
			ecamsLogger.error("## Cmm1600.getUnicode() Exception START ##");
			ecamsLogger.error("## Error DESC : ", exception);
			ecamsLogger.error("## Cmm1600.getUnicode() Exception END ##");
			throw exception;
		} catch (Exception exception) {
			exception.getStackTrace();
			ecamsLogger.error("## Cmm1600.getUnicode() Exception START ##");
			ecamsLogger.error("## Error DESC : ", exception);
			ecamsLogger.error("## Cmm1600.getUnicode() Exception END ##");
			throw exception;
		}finally {

		}
	}
 	public Object[] get_SqlList(String txtSql,HashMap<String, String> cmdData) throws SQLException, Exception {
		Connection        conn        = null;
		PreparedStatement pstmt       = null;
		ResultSet         rs          = null;
		StringBuffer      strQuery    = new StringBuffer();
		ArrayList<HashMap<String, String>>  rtList	= new ArrayList<HashMap<String, String>>();
		HashMap<String, String>			  	rst	 	= null;
			if("D".equals(cmdData.get("dbGbnCd"))){
				ConnectionContext connectionContext = new ConnectionResource(false,"D");
				conn = connectionContext.getConnection();
			}else if("F".equals(cmdData.get("dbGbnCd"))){
				ConnectionContext connectionContext = new ConnectionResource(false,"F");
				conn = connectionContext.getConnection();
			}else {
				ConnectionContext connectionContext = new ConnectionResource();		
				conn = connectionContext.getConnection();
			}
		try {
			String tmpSql = txtSql.trim();
			String tmpSql1 = "";
			String ERRMSG = "";
			boolean errSw = false;
			boolean readSw = false;
			
			int i = 0;
			int cnt = 0;
			tmpSql = tmpSql.trim();
			tmpSql1 = tmpSql.substring(0,tmpSql.indexOf(" "));
			tmpSql1 = tmpSql1.toUpperCase();
			if (tmpSql1.equals("SELECT") || tmpSql1.equals("DESC")) readSw = true;
			else if (tmpSql1.equals("EXEC")) {  //procedure실행
				if (tmpSql.substring(tmpSql.length()-1).equals(";")) tmpSql = "BEGIN " + tmpSql + " end;";
				else tmpSql = "BEGIN " + tmpSql + "; end;";
			}
			
			if (!readSw) {  //update, insert등
				strQuery.setLength(0);
				strQuery.append(tmpSql);
				pstmt = conn.prepareStatement(strQuery.toString());
				pstmt = new LoggableStatement(conn,strQuery.toString());
				ecamsLogger.error(((LoggableStatement)pstmt).getQueryString());
	            i = pstmt.executeUpdate();
	            pstmt.close();

	            rst = new HashMap<String, String>();
	            if (tmpSql1.equals("EXEC")) {
		            if (i == 0) {
		            	rst.put("ERROR", "Y");
		            	rst.put("ERRMSG", "변경된 Row가 없습니다. 쿼리를 확인하여 주시기 바랍니다.");
		            } else {
		            	rst.put("ERROR", "N");
		            	rst.put("rowcnt", Integer.toString(i));
		            }	
	            }
	            rst.put("readsw", "N");
	            rst.put("rowcnt", Integer.toString(i));
	            rtList.add(rst);
	            rst = null;
			} else {
				if ( !errSw ) {
					strQuery.setLength(0);
					strQuery.append(tmpSql);
					pstmt = conn.prepareStatement(strQuery.toString());
					pstmt = new LoggableStatement(conn,strQuery.toString());
					ecamsLogger.error(((LoggableStatement)pstmt).getQueryString());
		            rs = pstmt.executeQuery();

					while (rs.next()){
						if ( rtList.size()>=60000 ){
							ERRMSG = ERRMSG + "결과10000건이상/";
							break;
						}
						ResultSetMetaData metadata = rs.getMetaData();
						cnt = metadata.getColumnCount();
						if (rs.getRow() == 1) {
							rst = new HashMap<String, String>();
				            rst.put("ERROR", "N");
				            rst.put("readsw", "Y");
				            rst.put("header", "Y");	
				            
				            for (i=1;cnt>=i;i++) {
				            	//ecamsLogger.error("+++colname++++++"+colName[i]);
				            	rst.put("col"+Integer.toString(i-1), metadata.getColumnLabel(i));
				            }
				            rst.put("colcount", Integer.toString(cnt));
			        		rtList.add(rst);
			        		rst = null;
						}
						
			            rst = new HashMap<String, String>();
			            rst.put("ERROR", "N");
			            rst.put("readsw", "Y");
			            rst.put("header", "N");	
			            for (i=1;cnt>=i;i++) {
			            	//ecamsLogger.error("+++colname++++++"+colName[i]);
			            	rst.put("col"+Integer.toString(i-1), rs.getString(i));
			            }
			            rst.put("colcount", Integer.toString(cnt));
		        		rtList.add(rst);
		        		rst = null;
					}//end of while-loop statement

					rs.close();
					pstmt.close();
					

					rs = null;
					pstmt = null;
				} else {
					rst = new HashMap<String, String>();
					rst.put("ERROR", "Y");
					rst.put("ERRMSG", "SQL문 확인 요망["+ERRMSG+"]");
					rtList.add(rst);
					rst = null;
				}
			}
			
			conn.close();
			
			rs = null;
			pstmt = null;
			conn = null;
			//ecamsLogger.error("++++ Query Result ==>"+rtList.toString());
			return rtList.toArray();

		} catch (SQLException sqlexception) {
			sqlexception.printStackTrace();
			ecamsLogger.error("## Cmm1600.get_SqlList() SQLException START ##");
			ecamsLogger.error("## Error DESC : ", sqlexception);
			ecamsLogger.error("## Cmm1600.get_SqlList() SQLException END ##");
			throw sqlexception;
		} catch (Exception exception) {
			exception.printStackTrace();
			ecamsLogger.error("## Cmm1600.get_SqlList() Exception START ##");
			ecamsLogger.error("## Error DESC : ", exception);
			ecamsLogger.error("## Cmm1600.get_SqlList() Exception END ##");
			throw exception;
		}finally{
			if (strQuery != null) 	strQuery = null;
			if (rtList != null)	rtList = null;
			if (rs != null)     try{rs.close();}catch (Exception ex){ex.printStackTrace();}
			if (pstmt != null)  try{pstmt.close();}catch (Exception ex2){ex2.printStackTrace();}
			if (conn != null){
				try{
					conn.close();
				}catch(Exception ex3){
					ecamsLogger.error("## Cmm1600.SelectList() connection release exception ##");
					ex3.printStackTrace();
				}
			}
		}
	}//end of get_SqlList() method statement
 	
 	public String getFileView(String saveFile) throws Exception {
		StringBuffer      strQuery    = new StringBuffer();
		
		File shfile			= null;
		String	retMsg		= "";
		BufferedReader in1	= null;
		String	readline1 	= "";
		
		try {	
			shfile = new File(saveFile);
			if (!shfile.isFile()) {              //File존재여부
				retMsg = "ER파일이 존재하지 않습니다. ["+ saveFile+"]";
				shfile = null;
				return retMsg;
			}
			shfile = null;
			/*int i = 0;
			String strLine = "";*/
			in1 = new BufferedReader(new InputStreamReader(new FileInputStream(saveFile),"MS949"));
			strQuery.setLength(0);
			while( (readline1 = in1.readLine()) != null ){
				/*strLine = String.format("%5d", ++i);
				strQuery.append(strLine+" : " + readline1+"\n");*/
				strQuery.append(readline1+"\n");
			}

			in1.close();
			in1 = null;
			
			return "OK"+strQuery.toString();
			
		} catch (Exception exception) {
			exception.printStackTrace();
			ecamsLogger.error("## Cmm1600.getFileText() Exception START ##");				
			ecamsLogger.error("## Error DESC : ", exception);	
			ecamsLogger.error("## Cmm1600.getFileText() Exception END ##");				
			throw exception;
		}finally{
				
		}
	}
}