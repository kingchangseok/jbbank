/*****************************************************************************************
	1. program ID	: Cmr0200.java
	2. create date	: 2006.08. 08
	3. auth		    : is.choi
	4. update date	: 
	5. auth		    : 
	6. description	: 1. USER INFO.
*****************************************************************************************/

package app.eCmr;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
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
import com.google.gson.internal.LinkedTreeMap;

import app.common.AutoSeq;
import app.common.LoggableStatement;
import app.common.SysInfo;
import app.common.SystemPath;
import app.common.UserInfo;
import app.common.eCAMSInfo;
import app.eCmc.Cmc0010;
import app.eCmd.Cmd0100;
import app.thread.ThreadPool;

/** 
 * @author bigeyes  
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class Cmr0200{     
	

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
    public String bldcdChk(String SysCd,String JobCd,String RsrcCd,String rsrcInfo,String QryCd,String ReqCd,String ItemId,ArrayList<HashMap<String,Object>> ConfList,Connection conn) throws SQLException, Exception {
		PreparedStatement pstmt       = null;
		ResultSet         rs          = null;
		StringBuffer      strQuery    = new StringBuffer();
		String            strErrMsg   = null;
		int               cnt         = 0;
		int               i           = 0;
		
		try {
			cnt = 0;		
			RsrcCd = RsrcCd.substring(0,2);
			
			String strRsrc = RsrcCd;
			String strCmInfo = rsrcInfo;
			if (ReqCd.equals("16")) {
				strQuery.setLength(0);
	    		strQuery.append("select cm_info from cmm0036             \n");
	    		strQuery.append(" where cm_syscd=? and cm_rsrccd=?       \n");
	    		pstmt = conn.prepareStatement(strQuery.toString());
	    		pstmt.setString(1, SysCd);
	    		pstmt.setString(2, RsrcCd);
	    		rs = pstmt.executeQuery();
	    		if (rs.next()) {
	    			strCmInfo = rs.getString("cm_info");
	    		}
	    		rs.close();
	    		pstmt.close();
			}  
			
			strQuery.setLength(0);
    		strQuery.append("select a.cm_info,a.cm_rsrccd            \n");
    		strQuery.append("  from cmm0036 a,cmm0037 b              \n");
    		strQuery.append(" where b.cm_syscd=? and b.cm_rsrccd=?   \n");
    		strQuery.append("   and b.cm_syscd=a.cm_syscd            \n");
    		strQuery.append("   and b.cm_samersrc=a.cm_rsrccd        \n");
    		strQuery.append("   and a.cm_closedt is null             \n");
    		pstmt = conn.prepareStatement(strQuery.toString());
    		pstmt = new LoggableStatement(conn,strQuery.toString());
        	
        	
    		pstmt.setString(1, SysCd);
    		pstmt.setString(2, RsrcCd);
    		ecamsLogger.error(((LoggableStatement)pstmt).getQueryString());
    		rs = pstmt.executeQuery();
    		while (rs.next()) {
    			++i;
    			strRsrc = strRsrc + ","+ rs.getString("cm_rsrccd");
    			strCmInfo = strCmInfo + "," + rs.getString("cm_info");
    		}
    		rs.close();
    		pstmt.close();
    		
    		String strRsrcCd[] = strRsrc.split(",");
			String strInfo[] = strCmInfo.split(",");
			String strPrcSys[] = strRsrc.split(",");
			int j = 0;
			int k = 0;
			boolean findSw = false;
			String confTeam = "";
			ArrayList<LinkedTreeMap<String,Object>>	rData2 = null;
						
    		for (i=0;strRsrcCd.length>i;i++) {    			
    			if (strInfo[i].substring(7,8).equals("1") ||   //파일삭제(개발서버)
    				strInfo[i].substring(1,3).equals("10") ||   //체크아웃
					strInfo[i].substring(13,14).equals("1") || //체크아웃스크립트실행
					strInfo[i].substring(21,22).equals("1") || //형상관리저장스크립트실행
		        	strInfo[i].substring(23,24).equals("1") || //개발서버에서 체크인
		        	strInfo[i].substring(26,27).equals("1")) { //개발툴연계
    				findSw = false;
    				if (QryCd.equals("01") || QryCd.equals("02")) {
    					if (strInfo[i].substring(1,2).equals("1") || strInfo[i].substring(13,14).equals("1")) {
    						for (k=0;ConfList.size()>k;k++) {
		    					if (ConfList.get(k).get("cm_gubun").equals("1")) {
		    						rData2 = (ArrayList<LinkedTreeMap<String, Object>>) ConfList.get(k).get("arysv");
		        					confTeam = (String) rData2.get(0).get("SvUser");
		        					rData2 = null;
		        					if (confTeam.equals("SYSDN")) {
		        						findSw = true;
		        						break;
		        					}
		    					}
    						}
    					}
    				} else if (!ReqCd.equals("05")) {
    					if (strInfo[i].substring(21,22).equals("1") || strInfo[i].substring(23,24).equals("1")) {
    						for (k=0;ConfList.size()>k;k++) {
		    					if (ConfList.get(k).get("cm_gubun").equals("1")) {
		    						rData2 = (ArrayList<LinkedTreeMap<String, Object>>) ConfList.get(k).get("arysv");
		        					confTeam = (String) rData2.get(0).get("SvUser");
		        					rData2 = null;
		        					if (confTeam.equals("SYSUP")) {
		        						findSw = true;
		        						break;
		        					}
		    					}
    						}
    					} else if (strInfo[i].substring(7,8).equals("1")) findSw = true;
    				}
    					
    				if (findSw == true) {
			        	strQuery.setLength(0);
			        	strQuery.append("select count(*) cnt from cmm0038 b,cmm0031 a   \n");
			        	strQuery.append(" where a.cm_syscd=? and a.cm_svrcd='01'        \n");
			        	strQuery.append("   and a.cm_closedt is null                    \n");
			        	strQuery.append("   and a.cm_syscd=b.cm_syscd                   \n");
			        	strQuery.append("   and a.cm_svrcd=b.cm_svrcd                   \n");
			        	strQuery.append("   and a.cm_seqno=b.cm_seqno                   \n");
			        	strQuery.append("   and b.cm_rsrccd=?                           \n");			        	
			        	pstmt = conn.prepareStatement(strQuery.toString());
			        	
			        	pstmt.setString(1, SysCd);
			        	pstmt.setString(2, strRsrcCd[i]);
			        	
			        	rs = pstmt.executeQuery();
			        	if (rs.next()){
			        		if (rs.getInt("cnt") == 0) {
			        			strErrMsg = "체크아웃서버가 연결되어 있지 않습니다. [관리자 연락요망]. RsrcCd="+strRsrcCd[i];
			        			break;
			        		}
			        	}
			        	rs.close();
			        	pstmt.close();
    				}
				}
				if (strErrMsg == null && 
					strInfo[i].substring(0,1).equals("1") ||   //컴파일
					strInfo[i].substring(6,7).equals("1") ||   //파일삭제(컴파일)
					strInfo[i].substring(12,13).equals("1") || //빌드서버FileCopy
					strInfo[i].substring(24,25).equals("1") || //빌드서버체크인
					//strInfo[i].substring(27,28).equals("1") || //웹취약성검증	 (2024/04/24)
					strInfo[i].substring(43,44).equals("1")) { //검증최종컴파일					
					findSw  = false;
					if (QryCd.equals("01") || QryCd.equals("02")) findSw = false;
					//else if (QryCd.equals("04") || QryCd.equals("06")) findSw = false;
					else if (!ReqCd.equals("05")) {
    					if (strInfo[i].substring(0,1).equals("1") || strInfo[i].substring(12,13).equals("1") ||
    					    strInfo[i].substring(24,25).equals("1") || strInfo[i].substring(27,28).equals("1") ||
    					    strInfo[i].substring(39,40).equals("1") || strInfo[i].substring(43,44).equals("1")) {
    						for (k=0;ConfList.size()>k;k++) {
		    					if (ConfList.get(k).get("cm_gubun").equals("1")) {
		    						rData2 = (ArrayList<LinkedTreeMap<String, Object>>) ConfList.get(k).get("arysv");
		        					confTeam = (String) rData2.get(0).get("SvUser");
		        					rData2 = null;
		        					if (confTeam.equals("SYSFT") && strInfo[i].substring(27,28).equals("1")) {
		        						findSw = true;
		        						break;
		        					}
		        					if (confTeam.equals("SYSAC") && strInfo[i].substring(43,44).equals("1")) {
		        						findSw = true;
		        						break;
		        					}
		        					if (confTeam.equals("SYSCB") && 
		        						(strInfo[i].substring(0,1).equals("1") || strInfo[i].substring(12,13).equals("1") ||
		        						 strInfo[i].substring(24,25).equals("1"))) {
		        						findSw = true;
		        						break;
		        					}
		    					}
    						}
    					} else if (strInfo[i].substring(6,7).equals("1")) findSw = true;
    				}
					if (findSw == true) {
			        	strQuery.setLength(0);
			        	strQuery.append("select count(*) cnt from cmm0038 b,cmm0031 a   \n");
			        	strQuery.append(" where a.cm_syscd=? and a.cm_svrcd=?           \n");
			        	strQuery.append("   and a.cm_closedt is null                    \n");
			        	strQuery.append("   and a.cm_syscd=b.cm_syscd                   \n");
			        	strQuery.append("   and a.cm_svrcd=b.cm_svrcd                   \n");
			        	strQuery.append("   and a.cm_seqno=b.cm_seqno                   \n");
			        	strQuery.append("   and b.cm_rsrccd=?                           \n");
			        	
			        	pstmt = conn.prepareStatement(strQuery.toString());
			        	
			        	pstmt.setString(1, SysCd);
			        	if (QryCd.equals("03")) pstmt.setString(2, "13");
			        	else pstmt.setString(2, "03");
			        	pstmt.setString(3, strRsrcCd[i]);
			        	
			        	rs = pstmt.executeQuery();
			        	if (rs.next()){
			        		if (rs.getInt("cnt") == 0) {
			        			strErrMsg = "빌드서버가 연결되어 있지 않습니다. [관리자 연락요망]. RsrcCd="+strRsrcCd[i];
			        			break;
			        		}
			        	}
			        	rs.close();
			        	pstmt.close();	
					}
				}
				if (strErrMsg == null && 
					strInfo[i].substring(10,11).equals("1") ||   //운영서버FileCopy
					strInfo[i].substring(20,21).equals("1") ||   //릴리즈스크립트실행
					strInfo[i].substring(34,35).equals("1")) {   //적용스크립트실행
					
					findSw  = false;
					if (QryCd.equals("01") || QryCd.equals("02")) findSw = false;
					else if (!ReqCd.equals("05")) {
						for (k=0;ConfList.size()>k;k++) {
	    					if (ConfList.get(k).get("cm_gubun").equals("1")) {
	    						rData2 = (ArrayList<LinkedTreeMap<String, Object>>) ConfList.get(k).get("arysv");
	        					confTeam = (String) rData2.get(0).get("SvUser");
	        					rData2 = null;
	        					if (confTeam.equals("SYSRC") && strInfo[i].substring(34,35).equals("1")) {
	        						findSw = true;
	        						break;
	        					}
	        					if (confTeam.equals("SYSED") && 
	        						(strInfo[i].substring(10,11).equals("1") ||   //운영서버FileCopy
	        						 strInfo[i].substring(20,21).equals("1"))) {
	        						findSw = true;
	        						break;
	        					}
	    					}
						}
    				}
					if (findSw == true) {
			        	strQuery.setLength(0);
			        	strQuery.append("select count(*) cnt from cmm0038 b,cmm0031 a   \n");
			        	strQuery.append(" where a.cm_syscd=? and a.cm_svrcd=?           \n");
			        	strQuery.append("   and a.cm_closedt is null                    \n");
			        	strQuery.append("   and a.cm_syscd=b.cm_syscd                   \n");
			        	strQuery.append("   and a.cm_svrcd=b.cm_svrcd                   \n");
			        	strQuery.append("   and a.cm_seqno=b.cm_seqno                   \n");
			        	strQuery.append("   and b.cm_rsrccd=?                           \n");				        	
			        	pstmt = conn.prepareStatement(strQuery.toString());
			        	//pstmt = new LoggableStatement(conn,strQuery.toString());
			        	pstmt.setString(1, SysCd);
			        	pstmt.setString(2, "05");
			        	pstmt.setString(3, strRsrcCd[i]);
			        	////ecamsLogger.error(((LoggableStatement)pstmt).getQueryString());
			        	rs = pstmt.executeQuery();
			        	if (rs.next()){
			        		if (rs.getInt("cnt") == 0) {
			        			strErrMsg = "운영서버가 연결되어 있지 않습니다. [관리자 연락요망]. RsrcCd="+strRsrcCd[i];
			        			break;
			        		}
			        	}
			        	rs.close();
			        	pstmt.close();
					}
				}
				
				if (strErrMsg == null && 
					strInfo[i].substring(39,40).equals("1")) {   //사후처리쉘실행
					findSw  = false;
					if (QryCd.equals("01") || QryCd.equals("02") || QryCd.equals("03")) findSw = false;
					else if (!ReqCd.equals("05")) {
						for (k=0;ConfList.size()>k;k++) {
	    					if (ConfList.get(k).get("cm_gubun").equals("1")) {
	    						rData2 = (ArrayList<LinkedTreeMap<String, Object>>) ConfList.get(k).get("arysv");
	        					confTeam = (String) rData2.get(0).get("SvUser");
	        					rData2 = null;
	        					if (confTeam.equals("SYSAR")) {
	        						findSw = true;
	        						break;
	        					}
	    					}
						}
    				}
/////////////////////////////////////////////////////////////////2011년11월18일 최이사님이 빼랫음					
//					if (findSw == true) {
//			        	strQuery.setLength(0);
//			        	strQuery.append("select count(*) cnt from cmm0038 b,cmm0031 a   \n");
//			        	strQuery.append(" where a.cm_syscd=? and a.cm_svrcd='09'        \n");
//			        	strQuery.append("   and a.cm_closedt is null                    \n");
//			        	strQuery.append("   and a.cm_syscd=b.cm_syscd                   \n");
//			        	strQuery.append("   and a.cm_svrcd=b.cm_svrcd                   \n");
//			        	strQuery.append("   and a.cm_seqno=b.cm_seqno                   \n");
//			        	strQuery.append("   and b.cm_rsrccd=?                           \n");				        	
//			        	pstmt = conn.prepareStatement(strQuery.toString());
//			        	//pstmt = new LoggableStatement(conn,strQuery.toString());
//			        	pstmt.setString(1, SysCd);
//			        	pstmt.setString(2, strRsrcCd[i]);
//			        	////ecamsLogger.error(((LoggableStatement)pstmt).getQueryString());
//			        	rs = pstmt.executeQuery();
//			        	if (rs.next()){
//			        		if (rs.getInt("cnt") == 0) {
//			        			strErrMsg = "최종소스보관서버가 연결되어 있지 않습니다. [관리자 연락요망]. RsrcCd="+strRsrcCd[i];
//			        			break;
//			        		}
//			        	}
//			        	rs.close();
//			        	pstmt.close();
//					}
				}
    		}
    		
    		if (strErrMsg == null) {
	    		strQuery.setLength(0);
				strQuery.append("select a.cm_rsrccd,a.cm_prcsys          \n");
				strQuery.append("  from cmm0033 a,cmm0030 b              \n");
				strQuery.append(" where b.cm_syscd= ?                    \n");
				strQuery.append("   and b.cm_syscd=a.cm_syscd            \n");
				strQuery.append("   and a.cm_qrycd=?                     \n");
				if (ReqCd.equals("05")) 
					strQuery.append("and a.cm_prcsys='SYSDEL'            \n");
				strQuery.append("   and cm_jobcd=decode(substr(b.cm_sysinfo,8,1),'1',?,'****')\n");
				strQuery.append("   and cm_rsrccd in ( 				     \n");
				for (i=0;strRsrcCd.length>i;i++) {
					if (i>0) strQuery.append(", ? ");
					else strQuery.append("? ");
				}
			    strQuery.append(")                                       \n");
			    strQuery.append("order by a.cm_rsrccd                    \n");
	            pstmt = conn.prepareStatement(strQuery.toString());	
	            //pstmt = new LoggableStatement(conn,strQuery.toString());
	            pstmt.setString(++cnt, SysCd);
	            pstmt.setString(++cnt, QryCd); 
	            pstmt.setString(++cnt, JobCd); 
	            for (i=0;strRsrcCd.length>i;i++) {
					pstmt.setString(++cnt, strRsrcCd[i]);
				}
	            ////ecamsLogger.error(((LoggableStatement)pstmt).getQueryString());
	            rs = pstmt.executeQuery();
	                        
				while (rs.next()){
					for (i=0;strPrcSys.length>i;i++) {
						if (strPrcSys[i].substring(0,2).equals(rs.getString("cm_rsrccd"))) {
							strPrcSys[i] = strPrcSys[i] + "," + rs.getString("cm_prcsys");
						}
					}
				}//end of while-loop statement			
				rs.close();
				pstmt.close();		
				if (strErrMsg == null) {
					for (i=0;strRsrcCd.length>i;i++) {
						findSw = false;
						if (QryCd.equals("01") || QryCd.equals("02") || QryCd.equals("11") || QryCd.equals("04")) {
							if (strInfo[i].substring(5,6).equals("1")) {   //LOCK관리
								findSw = false;
								for (k=0;ConfList.size()>k;k++) {
									if (ConfList.get(k).get("cm_gubun").equals("1")) {
										rData2 = (ArrayList<LinkedTreeMap<String, Object>>) ConfList.get(k).get("arysv");
				    					confTeam = (String) rData2.get(0).get("SvUser");
				    					rData2 = null;
				    					if (confTeam.equals("SYSRK")) {
				    						findSw = true;
				    						break;
				    					}
									}
								}
								if (findSw == true) {
									findSw = false;
									for (j=0;strPrcSys.length>j;j++) {
										if (strPrcSys[j].substring(0,2).equals(strRsrcCd[i])) {
											findSw = true;
											if (strPrcSys[j].indexOf(confTeam)<0) {
												strErrMsg = "LOCK관리를 위한 스크립트정보가 등록되지 않았습니다.1";	
												break;
											} else break;
										}
									}
									if (findSw == false) {
										strErrMsg = "LOCK관리를 위한 스크립트정보가 등록되지 않았습니다.2";	
										break;
									}
								}
							}	
						}
						if (QryCd.equals("01") || QryCd.equals("02")) {
							if (strInfo[i].substring(13,14).equals("1")) {
								findSw = false;
								for (k=0;ConfList.size()>k;k++) {
									if (ConfList.get(k).get("cm_gubun").equals("1")) {
										rData2 = (ArrayList<LinkedTreeMap<String, Object>>) ConfList.get(k).get("arysv");
				    					confTeam = (String) rData2.get(0).get("SvUser");
				    					rData2 = null;
				    					if (confTeam.equals("SYSDN")) {
				    						findSw = true;
				    						break;
				    					}
									}
								}
								if (findSw == true) {
									findSw = false;
									for (j=0;strPrcSys.length>j;j++) {
										if (strPrcSys[j].substring(0,2).equals(strRsrcCd[i])) {
											findSw = true;
											if (strPrcSys[j].indexOf(confTeam)<0) {
												strErrMsg = "체크아웃스크립트정보가 등록되지 않았습니다.1";	
												break;
											} else break;
										}
									}
									if (findSw == false) {
										strErrMsg = "체크아웃스크립트정보가 등록되지 않았습니다.2";	
									}
								}
							}	
						} else if (!ReqCd.equals("05")) {
//							if (strInfo[i].substring(27,28).equals("1")) {
//								findSw = false;
//								for (k=0;ConfList.size()>k;k++) {
//									if (ConfList.get(k).get("cm_gubun").equals("1")) {
//										rData2 = (ArrayList<HashMap<String, Object>>) ConfList.get(k).get("arysv");
//				    					confTeam = (String) rData2.get(0).get("SvUser");
//				    					rData2 = null;
//				    					if (confTeam.equals("SYSFT")) {
//				    						findSw = true;
//				    						break;
//				    					}
//									}
//								}
//								if (findSw == true) {
//									findSw = false;
//									for (j=0;strPrcSys.length>j;j++) {
//										if (strPrcSys[j].substring(0,2).equals(strRsrcCd[i])) {
//											findSw = true;
//											if (strPrcSys[j].indexOf(confTeam)<0) {
//												strErrMsg = "웹취약성스크립트정보가 등록되지 않았습니다.1";	
//												break;
//											} else break;
//										}
//									}
//									if (findSw == false) {
//										strErrMsg = "웹취약성스크립트정보가 등록되지 않았습니다.2";	
//									}
//								}
//							}							
							if (strInfo[i].substring(21,22).equals("1")) {
								findSw = false;
								for (k=0;ConfList.size()>k;k++) {
									if (ConfList.get(k).get("cm_gubun").equals("1")) {
										rData2 = (ArrayList<LinkedTreeMap<String, Object>>) ConfList.get(k).get("arysv");
				    					confTeam = (String) rData2.get(0).get("SvUser");
				    					rData2 = null;
				    					if (confTeam.equals("SYSUP")) {
				    						findSw = true;
				    						break;
				    					}
									}
								}
								if (findSw == true) {
									findSw = false;
									for (j=0;strPrcSys.length>j;j++) {
										if (strPrcSys[j].substring(0,2).equals(strRsrcCd[i])) {
											findSw = true;
											if (strPrcSys[j].indexOf(confTeam)<0) {
												strErrMsg = "형상관리저장스크립트정보가 등록되지 않았습니다.1";	
												break;
											} else break;
										}
									}
									if (findSw == false) {
										strErrMsg = "형상관리저장스크립트정보가 등록되지 않았습니다.2";	
									}
								}								
							}
							if (strInfo[i].substring(0,1).equals("1")) {
								findSw = false;
								for (k=0;ConfList.size()>k;k++) {
									if (ConfList.get(k).get("cm_gubun").equals("1")) {
										rData2 = (ArrayList<LinkedTreeMap<String, Object>>) ConfList.get(k).get("arysv");
				    					confTeam = (String) rData2.get(0).get("SvUser");
				    					rData2 = null;
				    					if (confTeam.equals("SYSCB")) {
			    							findSw = true;
											break;
				    					}
									}
								}
								if (findSw == true) {
									findSw = false;
									if (strInfo[i].substring(47,48).equals("1")) {
										strQuery.setLength(0);
										strQuery.append("select count(*) cnt from cmr0026          \n");
										strQuery.append(" where cr_itemid=? and cr_prcsys='SYSCB'  \n");
										pstmt = conn.prepareStatement(strQuery.toString());
										pstmt.setString(1, ItemId);
										rs = pstmt.executeQuery();
										if (rs.next()) {
											if (rs.getInt("cnt")>0) findSw = true;
										}
										rs.close();
										pstmt.close();
									} else {
										for (j=0;strPrcSys.length>j;j++) {
											if (strPrcSys[j].substring(0,2).equals(strRsrcCd[i])) {
												findSw = true;
												if (strPrcSys[j].indexOf(confTeam)<0) {
													strErrMsg = "실행 할 빌드스크립트정보가 등록되지 않았습니다.1";	
													break;
												} else break;
											}
										}
									}
									if (findSw == false) {
										strErrMsg = "실행 할 빌드스크립트정보가 등록되지 않았습니다.2";	
									}
								}	
							}
							if (strInfo[i].substring(20,21).equals("1")) {
								findSw = false;
								for (k=0;ConfList.size()>k;k++) {
									if (ConfList.get(k).get("cm_gubun").equals("1")) {
										rData2 = (ArrayList<LinkedTreeMap<String, Object>>) ConfList.get(k).get("arysv");
				    					confTeam = (String) rData2.get(0).get("SvUser");
				    					rData2 = null;
				    					if (confTeam.equals("SYSED")) {
				    						findSw = true;
				    						break;
				    					}
									}
								}
								if (findSw == true) {
									findSw = false;
									if (strInfo[i].substring(47,48).equals("1")) {
										strQuery.setLength(0);
										strQuery.append("select count(*) cnt from cmr0026         \n");
										strQuery.append(" where cr_itemid=? and cr_prcsys='SYSED' \n");
										pstmt = conn.prepareStatement(strQuery.toString());
										pstmt.setString(1, ItemId);
										rs = pstmt.executeQuery();
										if (rs.next()) {
											if (rs.getInt("cnt")>0) findSw = true;
										}
										rs.close();
										pstmt.close();
									} else {
										for (j=0;strPrcSys.length>j;j++) {
											if (strPrcSys[j].substring(0,2).equals(strRsrcCd[i])) {
												findSw = true;
												if (strPrcSys[j].indexOf(confTeam)<0) {
													strErrMsg = "실행 할 배포스크립트정보가 등록되지 않았습니다.1";	
													break;
												} else break;
											}
										}
									}
									if (findSw == false) {
										strErrMsg = "실행 할 배포스크립트정보가 등록되지 않았습니다.2";	
									}
								}										
							}
							if (strInfo[i].substring(34,35).equals("1")) {
								findSw = false;
								for (k=0;ConfList.size()>k;k++) {
									if (ConfList.get(k).get("cm_gubun").equals("1")) {
										rData2 = (ArrayList<LinkedTreeMap<String, Object>>) ConfList.get(k).get("arysv");
				    					confTeam = (String) rData2.get(0).get("SvUser");
				    					rData2 = null;
				    					if (confTeam.equals("SYSRC")) {
				    						findSw = true;
				    						break;
				    					}
									}
								}
								if (findSw == true) {
									findSw = false;
									for (j=0;strPrcSys.length>j;j++) {
										if (strPrcSys[j].substring(0,2).equals(strRsrcCd[i])) {
											findSw = true;
											if (strPrcSys[j].indexOf(confTeam)<0) {
												strErrMsg = "실행 할 적용스크립트정보가 등록되지 않았습니다.1";	
												break;
											} else break;
										}
									}
									if (findSw == false) {
										strErrMsg = "실행 할 적용스크립트정보가 등록되지 않았습니다.2";	
									}
								}	
							}
							if (strInfo[i].substring(39,40).equals("1")) {
								findSw = false;
								for (k=0;ConfList.size()>k;k++) {
									if (ConfList.get(k).get("cm_gubun").equals("1")) {
										rData2 = (ArrayList<LinkedTreeMap<String, Object>>) ConfList.get(k).get("arysv");
				    					confTeam = (String) rData2.get(0).get("SvUser");
				    					rData2 = null;
				    					if (confTeam.equals("SYSAR")) {
				    						findSw = true;
				    						break;
				    					}
									}
								}
								if (findSw == true) {
									findSw = false;
									for (j=0;strPrcSys.length>j;j++) {
										if (strPrcSys[j].substring(0,2).equals(strRsrcCd[i])) {
											findSw = true;
											if (strPrcSys[j].indexOf(confTeam)<0) {
												strErrMsg = "사후처리스크립트정보가 등록되지 않았습니다.1";	
												break;
											} else break;
										}
									}
									if (findSw == false) {
										strErrMsg = "사후처리스크립트정보가 등록되지 않았습니다.2";	
									}
								}	
							}
							if (strInfo[i].substring(16,17).equals("1")) {
								findSw = false;
								for (k=0;ConfList.size()>k;k++) {
									if (ConfList.get(k).get("cm_gubun").equals("1")) {
										rData2 = (ArrayList<LinkedTreeMap<String, Object>>) ConfList.get(k).get("arysv");
				    					confTeam = (String) rData2.get(0).get("SvUser");
				    					rData2 = null;
				    					if (confTeam.equals("SYSCN")) {
				    						findSw = true;
				    						break;
				    					}
									}
								}
								if (findSw == true) {
									findSw = false;
									for (j=0;strPrcSys.length>j;j++) {
										if (strPrcSys[j].substring(0,2).equals(strRsrcCd[i])) {
											findSw = true;
											if (strPrcSys[j].indexOf(confTeam)<0) {
												strErrMsg = "체크인취소시실행할 스크립트정보가 등록되지 않았습니다.1";	
												break;
											} else break;
										}
									}
									if (findSw == false) {
										strErrMsg = "체크인취소시실행할 스크립트정보가 등록되지 않았습니다.2";	
									}
								}
							}
						} else if (ReqCd.equals("05")) {             //폐기
							if (strInfo[i].substring(17,18).equals("1")) {
								findSw = false;
								for (k=0;ConfList.size()>k;k++) {
									if (ConfList.get(k).get("cm_gubun").equals("1")) {
										rData2 = (ArrayList<LinkedTreeMap<String, Object>>) ConfList.get(k).get("arysv");
				    					confTeam = (String) rData2.get(0).get("SvUser");
				    					rData2 = null;
				    					if (confTeam.equals("SYSED")) {
				    						findSw = true;
				    						break;
				    					}
									}
								}
								if (findSw == true) {
									findSw = false;
									if (strPrcSys[i].indexOf("SYSDEL")<0) {
										strErrMsg = "파일삭제용 스크립트정보가 등록되지 않았습니다.1";	
									} else findSw = true;
								}
							}
						}
						if (strErrMsg != null) break;
					}
				}
    		}
			
			return strErrMsg;
			
		} catch (SQLException sqlexception) {
			sqlexception.printStackTrace();
			ecamsLogger.error("## Cmr0200.bldcdChk() SQLException START ##");
			ecamsLogger.error("## Error DESC : ", sqlexception);	
			ecamsLogger.error("## Cmr0200.bldcdChk() SQLException END ##");			
			throw sqlexception;
		} catch (Exception exception) {
			exception.printStackTrace();
			ecamsLogger.error("## Cmr0200.bldcdChk() Exception START ##");				
			ecamsLogger.error("## Error DESC : ", exception);	
			ecamsLogger.error("## Cmr0200.bldcdChk() Exception END ##");				
			throw exception;
		}finally{
			if (strQuery != null) 	strQuery = null;
			if (rs != null)     try{rs.close();}catch (Exception ex){ex.printStackTrace();}
			if (pstmt != null)  try{pstmt.close();}catch (Exception ex2){ex2.printStackTrace();}
		}
	}
    
    public Object[] getFileListIn_excel(ArrayList<HashMap<String,String>> fileList,HashMap<String,String> dataObj) throws SQLException, Exception {
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
			String _editor = "";
			String _dirpath = "";
			String errMsg = "";
			String strInfo = "";
			boolean errSw = false;
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
				rst.put("dirpath", fileList.get(i).get("dirpath").trim());
				rst.put("jawon", fileList.get(i).get("jawon").trim());
				rst.put("_syscd", _syscd);
				
				errMsg = "";
				errSw = false;
				_sourcename = fileList.get(i).get("rsrcname").trim();
				//_rsrccd = "";
				_jobcd = "";
				
				_editor = "";
				_dirpath = "";
				
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
				
				if ("".equals(fileList.get(i).get("jawon")) && fileList.get(i).get("jawon") == null){
					errMsg = "프로그램종류 입력없음/";
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
				
				
				String dirpt = "";
				String checkdirpt = "";
				
				if ("".equals(fileList.get(i).get("dirpath")) && fileList.get(i).get("dirpath") == null){
					errMsg = "프로그램경로 입력없음/";
					errSw = true;
				}else{
					strQuery.setLength(0);
					strQuery.append("select cm_volpath from cmm0038 							\n");
	                strQuery.append("where cm_syscd=? 											\n");
	                strQuery.append("  and cm_svrcd='01'										\n");
	                
	                pstmt = conn.prepareStatement(strQuery.toString());
	                pstmt.setString(1, _syscd);
	                rs = pstmt.executeQuery();
	                
	                if(rs.next()){
	                	dirpt = rs.getString("cm_volpath");
	                	checkdirpt = fileList.get(i).get("dirpath").substring(0, dirpt.length());
	                	
	                }else{
	                	errMsg = errMsg + "미등록 프로그램종류코드/";
	                	errSw = true;
	                }
	                
	                if(!dirpt.equals(checkdirpt)){
	                	errMsg = errMsg + "프로그램홈미등록경로/";
	                	errSw = true;
	                }
	                
					rs.close();
					pstmt.close();
				}
				
				if ("".equals(fileList.get(i).get("jobcd")) && fileList.get(i).get("jobcd") == null){
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
				
				
				_dirpath = fileList.get(i).get("dirpath").trim();
				rst.put("_dirpath", fileList.get(i).get("dirpath").trim());
				
				if ("".equals(_dirpath) & _dirpath == null){
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
				}else{
					
						rst.put("_itemid", "insert");
						rst.put("errmsg","정상");
						rst.put("errsw", "0");

				}
				rsval.add(rst);
				rst = null;
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

//	public Object[] getFileList_excel(ArrayList<HashMap<String,String>> fileList,String UserId,String SysCd,String SinCd,String TstSw,String SysInfo,String selectISRID) throws SQLException, Exception {
//		Connection        conn        = null;
//		PreparedStatement pstmt       = null;
//		ResultSet         rs          = null;
//		PreparedStatement pstmt2      = null;
//		ResultSet         rs2         = null;
//		StringBuffer      strQuery    = new StringBuffer();
//		ArrayList<HashMap<String, String>>  rsval	  = new ArrayList<HashMap<String, String>>();
//		HashMap<String, String>			    rst		  = null;
//		
//		ConnectionContext connectionContext = new ConnectionResource();
//		
//		int				  filecnt = 0;
//		int               cnt = 0;
//		int               i = 0;
//		int               j = 0;
//		String            svAcpt = "";
//		String			  strRsrcCd = "";
//		String            strRsrc[] = null;
//		
//		try {
//			
//			conn = connectionContext.getConnection();
//
//			rsval.clear();
//			int allCd = 0;
//			
//			//UserInfo     userinfo = new UserInfo();
//			boolean adminYn = false;//userinfo.isAdmin(UserId);
//			//userinfo = null;
//			
//			if (TstSw.equals("1") && SinCd.equals("04")) allCd = 1;
//			else allCd = 9;
//
//			strQuery.setLength(0);
//			strQuery.append("select cm_rsrccd from cmm0036                     \n"); 
//			strQuery.append(" where cm_syscd=? and cm_closedt is null          \n");
//			strQuery.append("   and substr(cm_info, 2, 1)='1'                  \n");
//			strQuery.append("   and substr(cm_info, 26, 1)='0'                 \n");
//			strQuery.append("   and cm_rsrccd not in (select cm_samersrc       \n");
//			strQuery.append("                           from cmm0037           \n");
//			strQuery.append("                          where cm_syscd=?)       \n");
//			pstmt = conn.prepareStatement(strQuery.toString());	
//			//pstmt =  new LoggableStatement(conn, strQuery.toString());			
//            pstmt.setString(1, SysCd);			
//            pstmt.setString(2, SysCd);
//            ////ecamsLogger.error(((LoggableStatement)pstmt).getQueryString());
//            rs = pstmt.executeQuery();
//            while (rs.next()) {
//            	if (!"".equals(strRsrcCd)) strRsrcCd = strRsrcCd + ",";
//            	strRsrcCd = strRsrcCd + rs.getString("cm_rsrccd");            	
//            }
//			rs.close();
//			pstmt.close();
//			
//			strRsrc = strRsrcCd.split(",");
//			
//			for (i=0;i<fileList.size();i++){
//				filecnt = 0;
//				cnt = 0;
//				if (allCd == 1 || allCd == 9) {
//					strQuery.setLength(0);
//					strQuery.append("select /*+ ALL ROWS */                                              \n");
//					strQuery.append("       a.cr_rsrcname,a.cr_lstver,a.cr_langcd,a.cr_syscd,a.cr_dsncd, \n");
//				    strQuery.append("       a.cr_itemid,a.cr_rsrccd,b.cr_acptno,a.cr_story,b.cr_jobcd,   \n");
//				    strQuery.append("       b.cr_baseno,b.cr_editcon,a.cr_status,c.cm_dirpath,i.cm_info, \n");
//				    strQuery.append("       lpad(i.cm_stepsta,4,'0') prcreq,nvl(i.cm_vercnt,9999) vercnt, \n");
//				    strQuery.append("       d.cm_codename,e.cm_codename jawon,b.cr_editor,h.cm_username, \n");
//				    strQuery.append("       f.cm_jobname,to_char(a.cr_lastdate,'yyyy/mm/dd') lastdate    \n");
//					strQuery.append("  from cmm0102 f,cmm0040 h,cmr1000 g,cmm0020 e,cmr0020 a,cmr1010 b, \n"); 
//					strQuery.append("       cmm0070 c,cmm0020 d,cmm0036 i                                \n"); 
//					if (adminYn == false && SysInfo.substring(2,3).equals("1")) {
//						strQuery.append(", cmm0044 k                              \n");
//					}
//					strQuery.append(" where a.cr_syscd=? and a.cr_rsrcname=?                             \n");
//					if (adminYn == false) {
//						if (SysInfo.substring(2,3).equals("1")) {
//							strQuery.append("and a.cr_jobcd=k.cm_jobcd and a.cr_syscd=k.cm_syscd  and a.cr_editor=k.cm_userid \n");   
//						} else strQuery.append("and a.cr_editor=?                                        \n");
//					} else strQuery.append("and a.cr_editor=?                                            \n");
//					strQuery.append("and a.cr_syscd=i.cm_syscd and a.cr_rsrccd=i.cm_rsrccd               \n");
//					if (SinCd.equals("03") || (SinCd.equals("04") && TstSw.equals("0"))) {
//						strQuery.append(" and a.cr_status in ('5','B')                                   \n"); 
//					} else {
//						strQuery.append(" and a.cr_status='B'                                           \n"); 						
//					}
//					strQuery.append("and b.cr_confno is null and b.cr_status in ('8','9')                \n");
//					strQuery.append("and b.cr_acptno=g.cr_acptno                                         \n");
//					if (SinCd.equals("03") || (SinCd.equals("04") && TstSw.equals("0")))
//						strQuery.append("and g.cr_qrycd in('01','02')                                    \n");
//					else {
//						strQuery.append("and b.cr_qrycd in ('03','04')                                   \n");
//					}
//					strQuery.append("and a.cr_itemid=b.cr_itemid and b.cr_itemid=b.cr_baseitem          \n");
//					strQuery.append("and a.cr_syscd=c.cm_syscd and a.cr_dsncd=c.cm_dsncd                \n");
//					strQuery.append("and d.cm_macode='CMR0020' and a.cr_status=d.cm_micode              \n");
//					strQuery.append("and e.cm_macode='JAWON' and a.cr_rsrccd=e.cm_micode                \n");
//					strQuery.append("and b.cr_editor=h.cm_userid and b.cr_jobcd=f.cm_jobcd              \n");
//					strQuery.append("and a.cr_syscd=i.cm_syscd and a.cr_rsrccd=i.cm_rsrccd              \n");				
//					strQuery.append("and i.cm_closedt is null                                           \n");
//					if(!"".equals(selectISRID) && selectISRID != null){
//						strQuery.append("and g.cr_isrid=? \n");
//						strQuery.append("and g.cr_isrsub=? \n");
//					}
//					pstmt = conn.prepareStatement(strQuery.toString());	
//					//pstmt = new LoggableStatement(conn,strQuery.toString());
//
//				    pstmt.setString(++cnt, SysCd);
//			        pstmt.setString(++cnt, fileList.get(i).get("cr_rsrcname"));
//		            if (adminYn == false) {
//					 	if (SysInfo.substring(2,3).equals("1")) {
//							//pstmt.setString(++cnt, UserId);
//						} else pstmt.setString(++cnt, UserId);
//				    } else pstmt.setString(++cnt, UserId);
//		            if(!"".equals(selectISRID) && selectISRID != null){
//						pstmt.setString(++cnt, selectISRID.substring(2,15));
//						pstmt.setString(++cnt, selectISRID.substring(15));
//					}
//					
//					////ecamsLogger.error(((LoggableStatement)pstmt).getQueryString());
//			        rs = pstmt.executeQuery();
//					while (rs.next()){
//						++filecnt;
//						if (filecnt==2) {
//							rst = new HashMap<String, String>();
//						    rst = rsval.get(rsval.size()-1);						    
//							rst.put("errmsg", "파일중복");
//							rsval.set(rsval.size()-1, rst);
//							rst = null;
//						}
//						rst = new HashMap<String, String>();
//						rst.put("ID", Integer.toString(rs.getRow()));
//						if (rs.getString("cr_acptno") != null) {
//							if (!svAcpt.equals(rs.getString("cr_acptno"))) {
//							   rst.put("acptno", rs.getString("cr_acptno").substring(2,4) + 
//									    "-" + rs.getString("cr_acptno").substring(4,6) + 
//									    "-" + rs.getString("cr_acptno").substring(6,12));
//							   svAcpt = rs.getString("cr_acptno");
//							} else {
//							   rst.put("acptno", "");	
//							}
//						} else {
//							rst.put("acptno", "");
//						}
//						rst.put("cr_rsrcname", rs.getString("cr_rsrcname"));
//						rst.put("jawon", rs.getString("jawon"));
//						rst.put("cm_dirpath", rs.getString("cm_dirpath"));
//						if (rs.getString("cr_story") != null) rst.put("cr_story", rs.getString("cr_story"));
//						else  rst.put("cr_story", "");
//						rst.put("cr_lstver", Integer.toString(rs.getInt("cr_lstver")));
//						rst.put("cr_editor", rs.getString("cr_editor"));
//						rst.put("cm_codename", rs.getString("cm_codename"));
//						rst.put("cr_syscd", rs.getString("cr_syscd"));
//						rst.put("cr_dsncd", rs.getString("cr_dsncd"));
//						rst.put("cr_itemid", rs.getString("cr_itemid"));
//						rst.put("baseitem", rs.getString("cr_itemid"));
//						rst.put("cr_jobcd", rs.getString("cr_jobcd"));
//						rst.put("cr_rsrccd", rs.getString("cr_rsrccd"));
//						if (rs.getInt("cr_lstver") == 0) rst.put("reqcd", "03");
//						else rst.put("reqcd", "04");
//						rst.put("cr_acptno", rs.getString("cr_acptno"));
//						rst.put("cm_username", rs.getString("cm_username"));
//						rst.put("cm_jobname", rs.getString("cm_jobname"));
//						rst.put("cr_lastdate", rs.getString("lastdate"));
//						rst.put("cm_info", rs.getString("cm_info"));
//						rst.put("cr_status", rs.getString("cr_status"));
//						rst.put("prcseq", rs.getString("prcreq"));
//						if (rs.getString("cr_baseno") != null) rst.put("cr_baseno", rs.getString("cr_baseno"));
//						else rst.put("cr_baseno", "");
//
//						if (rs.getString("cr_editcon") != null) {
//						   rst.put("cr_sayu", rs.getString("cr_editcon"));
//						} else rst.put("cr_sayu", "");
//						if (rs.getInt("vercnt") == 0) {
//							if (rs.getInt("cr_lstver") >= 9999) rst.put("cr_aftver", "1");
//							else rst.put("cr_aftver", Integer.toString(rs.getInt("cr_lstver")+1));
//						} else {
//							if (rs.getInt("cr_lstver") >= rs.getInt("vercnt")) {
//								rst.put("cr_aftver", "1");	
//							} else rst.put("cr_aftver", Integer.toString(rs.getInt("cr_lstver")+1));
//						}
//						if (filecnt>1) rst.put("errmsg", "파일중복");
//						else rst.put("errmsg", "정상");
//						
//						rst.put("selected_flag","0");
//						rsval.add(rst);
//						rst = null;
//					}//end of while-loop statement
//					rs.close();
//					pstmt.close();
//				}
//				
//				cnt = 0;
//	            if (allCd == 2 || allCd == 9) {
//	            	//if (allCd == 9) strQuery.append("union                                               \n");
//					strQuery.setLength(0);			
//					strQuery.append("select /*+ ALL ROWS */                                              \n");
//	            	strQuery.append("       a.cr_rsrcname,a.cr_lstver,a.cr_langcd,a.cr_syscd,a.cr_dsncd, \n");
//				    strQuery.append("       a.cr_itemid,a.cr_rsrccd,'' cr_acptno,a.cr_story,a.cr_jobcd,  \n");
//				    strQuery.append("       a.cr_acptno cr_baseno,                          \n");
//				    strQuery.append("       '' cr_editcon,a.cr_status,c.cm_dirpath,i.cm_info,            \n");
//				    strQuery.append("       lpad(i.cm_stepsta,4,'0') prcreq,nvl(i.cm_vercnt,50) vercnt,  \n");
//				    strQuery.append("       d.cm_codename,e.cm_codename jawon,a.cr_editor,f.cm_username, \n");
//				    strQuery.append("       b.cm_jobname,to_char(a.cr_lastdate,'yyyy/mm/dd') lastdate    \n");
//					strQuery.append("  from cmm0040 f,cmm0102 b,cmm0020 e,cmr0020 a,cmm0070 c,cmm0020 d,cmm0036 i \n"); 
//					if (adminYn == false) {
//						strQuery.append(", cmm0044 h                              \n");
//					}
//					strQuery.append(" where a.cr_syscd=? and a.cr_rsrcname=?                             \n");
//					strQuery.append(" and a.cr_lstver=0 and (a.cr_status='3' or                          \n");
//					strQuery.append("     (a.cr_status='B' and a.cr_editor=?))                           \n");
//					if (adminYn == false) { 
//						strQuery.append("and a.cr_syscd=h.cm_syscd and a.cr_jobcd=h.cm_jobcd and a.cr_editor=? and a.cr_editor=h.cm_userid \n");
//					}					
//					strQuery.append("and a.cr_syscd=c.cm_syscd and a.cr_dsncd=c.cm_dsncd                \n");
//					strQuery.append("and d.cm_macode='CMR0020' and a.cr_status=d.cm_micode              \n");
//					strQuery.append("and e.cm_macode='JAWON' and a.cr_rsrccd=e.cm_micode                \n");
//					strQuery.append("and nvl(a.cr_lstusr,a.cr_editor)=f.cm_userid                       \n");
//					strQuery.append("and a.cr_jobcd=b.cm_jobcd                                          \n");
//					strQuery.append("and a.cr_syscd=i.cm_syscd and a.cr_rsrccd=i.cm_rsrccd              \n");
//					strQuery.append("and a.cr_rsrccd in (");
//					for (j=0;strRsrc.length>j;j++) {
//						if (j>0) strQuery.append(", ? ");
//						else strQuery.append("? ");
//					}
//					strQuery.append(")                                \n");
//					
//					pstmt = conn.prepareStatement(strQuery.toString());	
//					//pstmt = new LoggableStatement(conn,strQuery.toString());
//					
//			        pstmt.setString(++cnt, SysCd);
//			        pstmt.setString(++cnt, fileList.get(i).get("cr_rsrcname"));
//			        pstmt.setString(++cnt, UserId);	
//			        if (adminYn == false) pstmt.setString(++cnt, UserId);	
//					for (j=0;strRsrc.length>j;j++) {
//	            		pstmt.setString(++cnt, strRsrc[j]);
//	            	}
//					////ecamsLogger.error(((LoggableStatement)pstmt).getQueryString());           
//			        rs = pstmt.executeQuery();
//			        		        
//					while (rs.next()){
//						++filecnt;
//						if (filecnt==2) {
//							rst = new HashMap<String, String>();
//						    rst = rsval.get(rsval.size()-1);						    
//							rst.put("errmsg", "파일중복");
//							rsval.set(rsval.size()-1, rst);
//							rst = null;
//						}
//						rst = new HashMap<String, String>();
//						rst.put("ID", Integer.toString(rs.getRow()));
//						if (rs.getString("cr_acptno") != null) {
//							if (!svAcpt.equals(rs.getString("cr_acptno"))) {
//							   rst.put("acptno", rs.getString("cr_acptno").substring(2,4) + 
//									    "-" + rs.getString("cr_acptno").substring(4,6) + 
//									    "-" + rs.getString("cr_acptno").substring(6,12));
//							   svAcpt = rs.getString("cr_acptno");
//							} else {
//							   rst.put("acptno", "");	
//							}
//						} else {
//							rst.put("acptno", "");
//						}
//						rst.put("cr_rsrcname", rs.getString("cr_rsrcname"));
//						rst.put("jawon", rs.getString("jawon"));
//						rst.put("cm_dirpath", rs.getString("cm_dirpath"));
//						if (rs.getString("cr_story") != null) rst.put("cr_story", rs.getString("cr_story"));
//						else  rst.put("cr_story", "");
//						rst.put("cr_lstver", Integer.toString(rs.getInt("cr_lstver")));
//						rst.put("cr_editor", rs.getString("cr_editor"));
//						rst.put("cm_codename", rs.getString("cm_codename"));
//						rst.put("cr_syscd", rs.getString("cr_syscd"));
//						rst.put("cr_dsncd", rs.getString("cr_dsncd"));
//						rst.put("cr_itemid", rs.getString("cr_itemid"));
//						rst.put("cr_jobcd", rs.getString("cr_jobcd"));
//						rst.put("cr_rsrccd", rs.getString("cr_rsrccd"));
//						rst.put("cr_acptno", rs.getString("cr_acptno"));
//						rst.put("cm_username", rs.getString("cm_username"));
//						rst.put("cm_jobname", rs.getString("cm_jobname"));
//						rst.put("cr_lastdate", rs.getString("lastdate"));
//						rst.put("cm_info", rs.getString("cm_info"));
//						rst.put("cr_status", rs.getString("cr_status"));
//						rst.put("prcseq", rs.getString("prcreq"));
//						if (rs.getString("cr_baseno") != null) rst.put("cr_baseno", rs.getString("cr_baseno"));
//						else rst.put("cr_baseno", "");
//
//						if (rs.getString("cr_editcon") != null) {
//						   rst.put("cr_sayu", rs.getString("cr_editcon"));
//						} else rst.put("cr_sayu", "");
//						if (rs.getInt("vercnt") == 0) {
//							if (rs.getInt("cr_lstver") >= 9999) rst.put("cr_aftver", "1");
//							else rst.put("cr_aftver", Integer.toString(rs.getInt("cr_lstver")+1));
//						} else {
//							if (rs.getInt("cr_lstver") >= rs.getInt("vercnt")) {
//								rst.put("cr_aftver", "1");	
//							} else rst.put("cr_aftver", Integer.toString(rs.getInt("cr_lstver")+1));
//						}
//						if (rs.getInt("cr_lstver") > 0) rst.put("reqcd", "04");
//						else rst.put("reqcd", "03");
//						if (filecnt>1) rst.put("errmsg", "파일중복");
//						else rst.put("errmsg", "정상");
//						rst.put("selected_flag","0");
//
//						rsval.add(rst);
//						rst = null;
//					}//end of while-loop statement
//					rs.close();
//					pstmt.close();
//	            }
//			}
//			
//			boolean findSw = false;
//			
//			for (i=0;fileList.size()>i;i++) {
//				findSw = false;
//				for (j=0;rsval.size()>j;j++) {
//					if (fileList.get(i).get("cr_rsrcname").equals(rsval.get(j).get("cr_rsrcname"))) {
//						findSw = true;
//						break;
//					}
//				}
//				if (findSw == false) {
//					rst = new HashMap<String,String>();
//					rst.put("linenum",Integer.toString(i));
//					rst.put("cr_rsrcname",fileList.get(i).get("cr_rsrcname"));
//					strQuery.setLength(0);
//					strQuery.append("select a.cr_rsrcname,a.cr_lstver,a.cr_syscd,a.cr_dsncd, \n");
//				    strQuery.append("       a.cr_itemid,a.cr_rsrccd,a.cr_story,a.cr_jobcd,               \n");
//				    strQuery.append("       a.cr_acptno cr_baseno,a.cr_status,c.cm_dirpath,              \n");
//				    strQuery.append("       d.cm_codename,e.cm_codename jawon,a.cr_editor,f.cm_username, \n");
//				    strQuery.append("       b.cm_jobname,to_char(a.cr_lastdate,'yyyy/mm/dd') lastdate    \n");
//					strQuery.append("  from cmm0040 f,cmm0102 b,cmm0020 e,cmr0020 a,cmm0070 c,cmm0020 d  \n"); 
//					strQuery.append(" where a.cr_syscd=? and a.cr_rsrcname=?                             \n");				
//					strQuery.append("   and a.cr_syscd=c.cm_syscd and a.cr_dsncd=c.cm_dsncd              \n");
//					strQuery.append("   and d.cm_macode='CMR0020' and a.cr_status=d.cm_micode            \n");
//					strQuery.append("   and e.cm_macode='JAWON' and a.cr_rsrccd=e.cm_micode              \n");
//					strQuery.append("   and nvl(a.cr_lstusr,a.cr_editor)=f.cm_userid                     \n");
//					strQuery.append("   and a.cr_jobcd=b.cm_jobcd                                        \n");					
//					pstmt = conn.prepareStatement(strQuery.toString());
//					pstmt.setString(1, SysCd);
//					pstmt.setString(2, fileList.get(i).get("cr_rsrcname"));
//					rs = pstmt.executeQuery();
//					if (rs.next()) {
//						rst.put("jawon", rs.getString("jawon"));
//						rst.put("cm_dirpath", rs.getString("cm_dirpath"));
//						if (rs.getString("cr_story") != null) rst.put("cr_story", rs.getString("cr_story"));
//						else  rst.put("cr_story", "");
//						rst.put("cr_lstver", Integer.toString(rs.getInt("cr_lstver")));
//						rst.put("cm_codename", rs.getString("cm_codename"));
//						rst.put("cr_syscd", rs.getString("cr_syscd"));
//						rst.put("cr_dsncd", rs.getString("cr_dsncd"));
//						rst.put("cr_itemid", rs.getString("cr_itemid"));
//						rst.put("cr_jobcd", rs.getString("cr_jobcd"));
//						rst.put("cr_rsrccd", rs.getString("cr_rsrccd"));
//						rst.put("cm_username", rs.getString("cm_username"));
//						rst.put("cm_jobname", rs.getString("cm_jobname"));
//						rst.put("cr_lastdate", rs.getString("lastdate"));
//						rst.put("cr_status", rs.getString("cr_status"));
//						rst.put("errmsg", rs.getString("cm_codename"));
//						rst.put("selected_flag","0");
//					} else {
//						rst.put("errmsg", "원장없음");
//						rst.put("cr_rsrccd", "");
//						rst.put("selected_flag","0");
//					}
//					rs.close();
//					pstmt.close();
//					
//					rsval.add(rst);
//					rst = null;
//				}
//			}
//			conn.close();
//			conn = null;
//			/*
//			returnObjectArray = rsval.toArray();
//			rsval.clear();
//			rsval = null;
//			*/
//			return rsval.toArray();	
//			
//		} catch (SQLException sqlexception) {
//			sqlexception.printStackTrace();
//			ecamsLogger.error("## Cmr0200.getFileList_excel() SQLException START ##");
//			ecamsLogger.error("## Error DESC : ", sqlexception);	
//			ecamsLogger.error("## Cmr0200.getFileList_excel() SQLException END ##");			
//			throw sqlexception;
//		} catch (Exception exception) {
//			exception.printStackTrace();
//			ecamsLogger.error("## Cmr0200.getFileList_excel() Exception START ##");				
//			ecamsLogger.error("## Error DESC : ", exception);	
//			ecamsLogger.error("## Cmr0200.getFileList_excel() Exception END ##");				
//			throw exception;
//		}finally{
//			if (strQuery != null) 	strQuery = null;
//			if (rs != null)     try{rs.close();}catch (Exception ex){ex.printStackTrace();}
//			if (pstmt != null)  try{pstmt.close();}catch (Exception ex2){ex2.printStackTrace();}
//			if (rs2 != null)    try{rs2.close();}catch (Exception ex){ex.printStackTrace();}
//			if (pstmt2 != null) try{pstmt2.close();}catch (Exception ex2){ex2.printStackTrace();}
//			//if (rsval != null) rsval=null;
//			if (conn != null){
//				try{
//					ConnectionResource.release(conn);
//				}catch(Exception ex3){
//					ecamsLogger.error("## Cmr0200.getFileList_excel() connection release exception ##");
//					ex3.printStackTrace();
//				}
//			}
//		}
//	}
	
    public Object[] getFileList_excel(ArrayList<HashMap<String,String>> fileList,HashMap<String, String> paramMap) throws SQLException, Exception {
    	Connection        conn        = null;
		Connection        connD       = null;
		PreparedStatement pstmt       = null;
		ResultSet         rs          = null;
		PreparedStatement pstmt2      = null;
		ResultSet         rs2         = null;
		StringBuffer      strQuery    = new StringBuffer();
		String            svAcpt      = "";
		int               cnt         = 0;
		int				  ccnt		  = 0;
	
		Object[] returnObjectArray = null;  
		ArrayList<HashMap<String, String>>  rsval = new ArrayList<HashMap<String, String>>();
		HashMap<String, String>			  rst		  = null;
		ConnectionContext connectionContext = new ConnectionResource();	
		ConnectionContext connectionContextD = new ConnectionResource(false,"D");	
		String			  szDsnCD = "";
		String			  strRsrcCd = "";
		String            strRsrc[] = null;
		int               i = 0;
		int               allCd = 0;
		boolean           delSw = false;
		boolean           ChkinSw = false;
		ArrayList<HashMap<String, String>> svrList = new ArrayList<HashMap<String, String>>();
		
		String UserId = paramMap.get("UserId");
		String SysCd = paramMap.get("SysCd");
		String SinCd = paramMap.get("SinCd");
		String ReqCd = paramMap.get("ReqCd");
		String TstSw = paramMap.get("TstSw");
		String DsnCd = paramMap.get("DsnCd");
		String SysInfo = paramMap.get("SysInfo");
		String RsrcCd = paramMap.get("RsrcCd");
		boolean UpLowSw =  Boolean.parseBoolean(paramMap.get("UpLowSw"));
		boolean selfSw = Boolean.parseBoolean(paramMap.get("selfSw"));
		boolean LikeSw = Boolean.parseBoolean(paramMap.get("LikeSw"));
		String txtORDERInfo = paramMap.get("txtORDERInfo");
		String itemid  = paramMap.get("itemid");
		
		try {
			
			conn = connectionContext.getConnection();
			connD = connectionContextD.getConnection();
			
			svAcpt = "";
			String strVolPath = "";
			String strDirPath = "";
			String strDevHome = "";
			SysInfo sysinfo = new SysInfo();
			if (sysinfo.getLocalYn(SysCd)) {
				strQuery.setLength(0);
				strQuery.append("select cd_devhome from cmd0200               \n");
				strQuery.append(" where cd_syscd=? and cd_userid=?            \n");
				pstmt = conn.prepareStatement(strQuery.toString());
				pstmt.setString(1, SysCd);
				pstmt.setString(2, UserId);
				rs = pstmt.executeQuery();
				if (rs.next()) {
					strDevHome = rs.getString("cd_devhome");
				}
				rs.close();
				pstmt.close();

				svrList = sysinfo.getHomePath_conn(SysCd, conn);
			}
			sysinfo = null;
			
            if (RsrcCd == null || "".equals(RsrcCd)) RsrcCd = "00";
            //ecamsLogger.error("++++++++++RsrcCd+++++++++"+RsrcCd);
            delSw = true;
            if (ReqCd.equals("05")) {
            	strQuery.setLength(0);
            	strQuery.append("select count(*) cnt from cmm0036              \n");
            	strQuery.append(" where cm_syscd=? and cm_closedt is null      \n");
            	strQuery.append("   and substr(cm_info,18,1)='1'               \n");
            	pstmt = conn.prepareStatement(strQuery.toString());
            	pstmt.setString(1, SysCd);
            	rs = pstmt.executeQuery();
            	if (rs.next()) {
            		if (rs.getInt("cnt")==0) delSw = false;            		
            	}
            }
			strQuery.setLength(0);
			strQuery.append("select cm_rsrccd from cmm0036                     \n"); 
			strQuery.append(" where cm_syscd=? and cm_closedt is null          \n");
			if (SinCd.equals("03") && ReqCd.equals("05")) {
				strQuery.append("   and (substr(cm_info,18, 1)='1' or          \n");
				strQuery.append("        cm_rsrccd in (select y.cm_rsrccd      \n");
				strQuery.append("                        from cmm0036 x,cmm0037 y \n");
				strQuery.append("                       where x.cm_syscd=?     \n");
				strQuery.append("                         and x.cm_syscd=y.cm_syscd \n");
				strQuery.append("                         and x.cm_rsrccd=y.cm_samersrc \n");
				strQuery.append("                         and substr(x.cm_info,18,1)='1')) \n");
			} else if (ReqCd.equals("09")) {
				strQuery.append("  and  (substr(cm_info, 1, 1)='1'             \n");				
				strQuery.append("   or  substr(cm_info, 15, 1)='1')            \n");				
			} else {
				strQuery.append("   and substr(cm_info, 2, 1)='1'              \n");
				strQuery.append("   and substr(cm_info, 26, 1)='0'             \n");
			}
			strQuery.append("   and cm_rsrccd not in (select cm_samersrc       \n");
			strQuery.append("                           from cmm0037           \n");
			strQuery.append("                          where cm_syscd=?)       \n");
			
			pstmt = conn.prepareStatement(strQuery.toString());	
			//pstmt =  new LoggableStatement(conn, strQuery.toString());			
            pstmt.setString(1, SysCd);			
            pstmt.setString(2, SysCd);
            if (SinCd.equals("03") && ReqCd.equals("05"))
            	pstmt.setString(3, SysCd);
            
            //ecamsLogger.error(((LoggableStatement)pstmt).getQueryString());
            rs = pstmt.executeQuery();
            while (rs.next()) {
            	if (strRsrcCd.equals("")){
            		strRsrcCd = rs.getString("cm_rsrccd") + ",";
            	} else{
            		strRsrcCd = strRsrcCd + rs.getString("cm_rsrccd") + ",";
            	}
            }
            if(strRsrcCd == null || strRsrcCd.equals("")){
            	returnObjectArray = rsval.toArray();
    			rsval = null;
    			return returnObjectArray;
            }
			rs.close();
			pstmt.close();
			
			if(strRsrcCd.substring(strRsrcCd.length()-1).indexOf(",")>-1){
				strRsrcCd = strRsrcCd.substring(0,strRsrcCd.length()-1);
			}
			strRsrc = strRsrcCd.split(",");

 			//UserInfo     userinfo = new UserInfo();
			boolean adminYn = false;//userinfo.isAdmin_conn(UserId,conn);
			//userinfo = null; 
			
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
			
			
			rsval.clear();
			
			//ecamsLogger.error("[1259] ++++++++++allCd+++++  :  "+allCd);
			for (int z=0;z<fileList.size();z++){
				cnt = 0;
				ccnt = 0;
				// 20230102 반복문 추가
				String RsrcName = fileList.get(z).get("rsrcname");
				String DirPath = fileList.get(z).get("dirpath"); 
				ecamsLogger.error("RsrcName  :  "+RsrcName);
				ecamsLogger.error("DirPath  :  "+DirPath);
	        	boolean isCheckIn = false;
	        	strQuery.setLength(0);
				strQuery.append("select /*+ ALL_ROWS */                                                  \n");
				strQuery.append("       a.cr_rsrcname,a.cr_lstver,a.cr_syscd,a.cr_dsncd,                 \n");
			    strQuery.append("       a.cr_itemid,a.cr_rsrccd,b.cr_acptno,a.cr_story,b.cr_jobcd,       \n");
			    strQuery.append("       b.cr_baseno,to_char(b.cr_prcdate,'yyyy/mm/dd hh24:mi') enddate,  \n");
			    strQuery.append("       b.cr_editcon,a.cr_status,c.cm_dirpath,i.cm_info,                 \n");
			    strQuery.append(	"       lpad(i.cm_stepsta,4,'0') prcreq,nvl(i.cm_vercnt,9999) vercnt,\n");
			    strQuery.append("       d.cm_codename,e.cm_codename jawon,b.cr_editor,h.cm_username,     \n");
			    strQuery.append("       f.cm_jobname,to_char(a.cr_lastdate,'yyyy/mm/dd') lastdate,       \n");
			    strQuery.append("       to_char(nvl(a.cr_lstdat,SYSDATE),'yyyymmdd') lstdat,a.cr_rpaflag,             \n");
			    strQuery.append("       g.cr_orderid,g.cr_emgcd,g.cr_sayu, a.cr_langcd,a.cr_compile, a.cr_teamcd, a.cr_sqlcheck, a.cr_document,	\n");
			    strQuery.append("       REPLACE(c.CM_DIRPATH, (SELECT CM_VOLPATH FROM CMM0038  							\n");
				strQuery.append("       WHERE CM_SYSCD = A.CR_SYSCD AND CM_RSRCCD = A.CR_RSRCCD AND CM_SVRCD = (select cm_dirbase from cmm0030 where cm_syscd = A.cr_syscd)),	\n");
				strQuery.append("       (SELECT CM_VOLPATH FROM CMM0038 WHERE CM_SYSCD = A.CR_SYSCD 					\n");
				strQuery.append("       AND CM_RSRCCD = A.CR_RSRCCD AND CM_SVRCD = '01')) as cm_dirpath2				\n");
				strQuery.append("  from cmm0102 f,cmm0040 h,cmr1000 g,cmm0020 e,cmr0020 a,cmr1010 b, \n"); 
				strQuery.append("       cmm0070 c,cmm0020 d,cmm0036 i                                \n"); 
				strQuery.append(" where a.cr_syscd=?                                                 \n");
				if (adminYn == false) {
					if (SysInfo.substring(2,3).equals("1") && selfSw == false) {
						strQuery.append("and h.cm_syscd=? and h.cm_userid=? and h.cm_closedt is null \n");
						strQuery.append("and h.cm_syscd=a.cr_syscd and h.cm_jobcd=a.cr_jobcd         \n");  
					} else if(selfSw==true){
						strQuery.append("and b.cr_editor=?                                        \n");
					} else{
						strQuery.append("and b.cr_jobcd in (select cm_jobcd from cmm0044			 \n");
						strQuery.append("					where cm_syscd = ?						 \n");
						strQuery.append("					and cm_userid = ?						)\n");
					}
				} else if (selfSw == true) strQuery.append("and a.cr_editor=?                        \n");
				if (!ReqCd.equals("00") && !ReqCd.equals("99")) {
					if (ReqCd.equals("03")) strQuery.append(" and a.cr_lstver=0                      \n"); 
					else                    strQuery.append(" and a.cr_lstver>0                      \n"); 
				}
				strQuery.append("and a.cr_syscd=i.cm_syscd and a.cr_rsrccd=i.cm_rsrccd               \n");

				if (SinCd.equals("04")) {
					strQuery.append(" AND ( EXISTS (select 'X'                                \n");
					strQuery.append("               from CMM0031 A1                           \n");
					strQuery.append("                   ,CMM0038 B1                           \n");
					strQuery.append("              where A1.CM_SYSCD = a.cr_syscd             \n");
					strQuery.append("                AND A1.CM_SVRCD IN ('13','15')           \n");
					strQuery.append("                AND A1.CM_CLOSEDT IS NULL                \n");
					strQuery.append("                AND A1.CM_SYSCD = B1.CM_SYSCD            \n");
					strQuery.append("                AND A1.CM_SVRCD = B1.CM_SVRCD            \n");
					strQuery.append("                AND A1.CM_SEQNO = B1.CM_SEQNO            \n");
					strQuery.append("                AND B1.CM_RSRCCD = a.cr_rsrccd           \n");
					strQuery.append("               )                                         \n");
					strQuery.append("       OR (                                              \n");
					strQuery.append("          NOT EXISTS (select 'X'                         \n");
					strQuery.append("               from CMM0031 A1                           \n");
					strQuery.append("                   ,CMM0038 B1                           \n");
					strQuery.append("              where A1.CM_SYSCD = a.cr_syscd             \n");
					strQuery.append("                AND A1.CM_SVRCD IN ('13','15')           \n");
					strQuery.append("                AND A1.CM_CLOSEDT IS NULL                \n");
					strQuery.append("                AND A1.CM_SYSCD = B1.CM_SYSCD            \n");
					strQuery.append("                AND A1.CM_SVRCD = B1.CM_SVRCD            \n");
					strQuery.append("                AND A1.CM_SEQNO = B1.CM_SEQNO            \n");
					strQuery.append("                AND B1.CM_RSRCCD = a.cr_rsrccd           \n");
					strQuery.append("               )                                         \n");
					strQuery.append("          and a.cr_status ='5'                           \n");
					strQuery.append("         )                                               \n");
					strQuery.append("    )                                                    \n");
				}else {
					strQuery.append(" and a.cr_status ='5'                                            \n"); 					
				}
				
				strQuery.append("and b.cr_confno is null and b.cr_status in ('8','9')                \n");
				strQuery.append("and b.cr_acptno=g.cr_acptno                                         \n");
				
				if (SinCd.equals("04") && (ReqCd.equals("05") || ReqCd.equals("09"))) {
					strQuery.append("and g.cr_qrycd='03'                                       \n");
					if (ReqCd.equals("05")) strQuery.append("and b.cr_qrycd='05'               \n");
					else strQuery.append("and b.cr_qrycd='09' and nvl(a.cr_nomodify,'0')='1'   \n");
				} else {
					strQuery.append("   and g.cr_qrycd in('01','02')								 \n");
				}
				
				if (!RsrcCd.equals("00")) strQuery.append("and a.cr_rsrccd=?                         \n");
				if(itemid.equals("")){
					if (RsrcName != null && !"".equals(RsrcName)) {
						if(LikeSw == true){
							if (UpLowSw == true) strQuery.append("and a.cr_rsrcname like ?    				\n");
							else strQuery.append("and upper(a.cr_rsrcname) like upper(?)      				\n");
						}else{
							if (UpLowSw == true) strQuery.append("and a.cr_rsrcname = ?					    \n");
							else strQuery.append("and upper(a.cr_rsrcname) = upper(?)					      \n");
						}
						
					}
				}
				else
					strQuery.append("and a.cr_itemid = ? \n");
				
				strQuery.append("and a.cr_itemid=b.cr_itemid and b.cr_itemid=b.cr_baseitem          \n");
				strQuery.append("and a.cr_syscd=c.cm_syscd and a.cr_dsncd=c.cm_dsncd                \n");
				strQuery.append("and d.cm_macode='CMR0020' and a.cr_status=d.cm_micode              \n");
				strQuery.append("and e.cm_macode='JAWON' and a.cr_rsrccd=e.cm_micode                \n");
				strQuery.append("and b.cr_editor=h.cm_userid and b.cr_jobcd=f.cm_jobcd              \n");
				strQuery.append("and a.cr_syscd=i.cm_syscd and a.cr_rsrccd=i.cm_rsrccd              \n");				
				strQuery.append("and c.cm_dirpath = ?												\n"); // 20230102 경로
				strQuery.append("and i.cm_closedt is null                                           \n");
			    if (!"".equals(txtORDERInfo) && txtORDERInfo != null && !"0000".equals(txtORDERInfo)){
			    	strQuery.append("and g.cr_orderid is not null \n");
			    	strQuery.append("and g.cr_orderid=? \n");
			    }
			    
				pstmt = conn.prepareStatement(strQuery.toString());	
//				pstmt = new LoggableStatement(conn,strQuery.toString());
				
				pstmt.setString(++cnt, SysCd);
				if (adminYn == false) {
					if(selfSw == true){
						pstmt.setString(++cnt, UserId);
					}else{
						pstmt.setString(++cnt, SysCd);
						pstmt.setString(++cnt, UserId);
					}
				} else if (selfSw == true) pstmt.setString(++cnt, UserId);
	           
				if (!RsrcCd.equals("00")) pstmt.setString(++cnt, RsrcCd); 
				if (RsrcName != null && !"".equals(RsrcName))
					if(itemid.equals("")){
						if(LikeSw==true){
							pstmt.setString(++cnt, "%"+RsrcName+"%");
						}else{
							pstmt.setString(++cnt, RsrcName);
						}
					}
					else
						pstmt.setString(++cnt, itemid);
				
				pstmt.setString(++cnt, DirPath);	// 20230102
				
				if (!"".equals(txtORDERInfo) && txtORDERInfo != null && !"0000".equals(txtORDERInfo)){
			    	pstmt.setString(++cnt, txtORDERInfo);
			    }
//			    ecamsLogger.error(((LoggableStatement)pstmt).getQueryString());
		        rs = pstmt.executeQuery();
		        
				while (rs.next()){	
					ChkinSw = true;
					isCheckIn = false;
					//차세대시스템이면 특정확장자만(CMM0020(CHKIN))보여주도록 조회
					if(SysCd.equals("01200")){
						if(rs.getString("cr_rsrccd").equals("25")){
							strQuery.setLength(0);			
							strQuery.append("select cm_codename 									\n");
							strQuery.append("from cmm0020 											\n");
							strQuery.append("where cm_macode ='CHKIN'								\n");
							pstmt2 = conn.prepareStatement(strQuery.toString());	
							//pstmt2 = new LoggableStatement(conn,strQuery.toString());
							//ecamsLogger.error(((LoggableStatement)pstmt2).getQueryString());
							rs2 = pstmt2.executeQuery();
							while(rs2.next()){
								if(rs.getString("cr_rsrcname").lastIndexOf(rs2.getString("cm_codename"))>0){
									ChkinSw = true;
									break;
								}else{
									ChkinSw = false;
								}
							}
							rs2.close();
							pstmt2.close();
						}
					}
					if(ChkinSw){//여기까지 140717 수진
						//=============================================================================
						if(rs.getString("cm_info").substring(57,58).equals("1")){
							ccnt = 0;
							strQuery.setLength(0);			
							strQuery.append("select count(*) cnt 									\n");
							strQuery.append("from cmr0020 a,cmm0070 b, CMR1010 c					\n");
							strQuery.append("where c.cr_syscd = ?  									\n");
							strQuery.append("and c.cr_rsrcname = ?									\n");
							strQuery.append("and b.cm_dirpath = ?									\n");
							//strQuery.append("and c.CR_EDITOR   = ?	   								\n");
							strQuery.append("and c.CR_SYSCD = B.CM_SYSCD							\n");
							strQuery.append("and c.CR_DSNCD = B.CM_DSNCD  							\n");
							strQuery.append("and c.CR_ITEMID = A.CR_ITEMID							\n");
							strQuery.append("AND c.CR_CONFNO IS NULL								\n");
							strQuery.append("AND c.CR_STATUS IN ('8','9')							\n");
							strQuery.append("AND substr(c.cr_acptno,5,2)='03'						\n");
							strQuery.append("AND a.cr_status = '0'                          		\n");						
							
							pstmt2 = connD.prepareStatement(strQuery.toString());	
//							pstmt2 = new LoggableStatement(connD,strQuery.toString());
							pstmt2.setString(++ccnt, rs.getString("cr_syscd"));
							pstmt2.setString(++ccnt, rs.getString("cr_rsrcname"));
							pstmt2.setString(++ccnt, rs.getString("cm_dirpath"));
							//pstmt2.setString(++ccnt, rs.getString("cr_editor"));
//							ecamsLogger.error(((LoggableStatement)pstmt2).getQueryString());
							rs2 = pstmt2.executeQuery();
							
							if(rs2.next()){
								if(rs2.getInt("cnt")>0) isCheckIn = true;
							}
							rs2.close();
							pstmt2.close();
						}else{
							isCheckIn = true;
						}
						
						if(isCheckIn){
							rst = new HashMap<String, String>();
							//rst.put("ID", Integer.toString(rs.getRow()));
							if (rs.getString("cr_acptno") != null) {
								if (!svAcpt.equals(rs.getString("cr_acptno"))) {
								   rst.put("acptno", rs.getString("cr_acptno").substring(2,4) + 
										    "-" + rs.getString("cr_acptno").substring(4,6) + 
										    "-" + rs.getString("cr_acptno").substring(6,12));
								   svAcpt = rs.getString("cr_acptno");
								} else {
								   rst.put("acptno", "");	
								}
							} else {
								rst.put("acptno", "");
							}
							rst.put("cr_rsrcname", rs.getString("cr_rsrcname"));
							rst.put("jawon", rs.getString("jawon"));
							
							if (rs.getString("cr_story") != null) rst.put("cr_story", rs.getString("cr_story"));
							else  rst.put("cr_story", "");
							rst.put("cr_lstver", Integer.toString(rs.getInt("cr_lstver")));
							rst.put("cr_editor", rs.getString("cr_editor"));
							rst.put("cm_codename", rs.getString("cm_codename"));
							rst.put("cr_syscd", rs.getString("cr_syscd"));
							rst.put("cr_dsncd", rs.getString("cr_dsncd"));
							rst.put("cr_itemid", rs.getString("cr_itemid"));
							rst.put("baseitem", rs.getString("cr_itemid"));
							rst.put("cr_jobcd", rs.getString("cr_jobcd"));
							rst.put("cr_rsrccd", rs.getString("cr_rsrccd"));
							rst.put("reqcd", ReqCd);
							rst.put("cr_acptno", rs.getString("cr_acptno"));
							rst.put("cm_username", rs.getString("cm_username"));
							rst.put("cm_jobname", rs.getString("cm_jobname"));
							rst.put("cr_lastdate", rs.getString("lastdate"));
							rst.put("lstdat", rs.getString("lstdat"));
							rst.put("cm_info", rs.getString("cm_info"));
							rst.put("cr_status", rs.getString("cr_status"));
							rst.put("prcseq", rs.getString("prcreq"));
							rst.put("cr_emgcd", rs.getString("cr_emgcd"));
							rst.put("cr_orderid", rs.getString("cr_orderid"));
							rst.put("enddate", rs.getString("enddate"));
							rst.put("cr_rpaflag", rs.getString("cr_rpaflag"));
							
							if (rs.getString("cr_baseno") != null) rst.put("cr_baseno", rs.getString("cr_baseno"));
							else rst.put("cr_baseno", "");
							rst.put("cm_dirpath", rs.getString("cm_dirpath"));
							rst.put("cm_dirpath2", rs.getString("cm_dirpath2"));
							if (rs.getString("cm_info").substring(44,45).equals("1") && strDevHome != null) {						
								for (i=0 ; svrList.size()>i ; i++) {
									if (svrList.get(i).get("cm_rsrccd").equals(rs.getString("cr_rsrccd"))) {
										strVolPath = svrList.get(i).get("cm_volpath");// /scmtst/hyjungtest/dev
										strDirPath = rs.getString("cm_dirpath");// /scmtst/hyjungtest/dev/kicc_flex/src
										
										if (strVolPath != null && !"".equals(strVolPath)) {
											if (strDirPath.length()>=strVolPath.length() && strDirPath.substring(0,strVolPath.length()).equals(strVolPath)) {
												rst.put("pcdir", strDevHome + strDirPath.substring(strVolPath.length()).replace("/", "\\"));
											}else{
												rst.put("pcdir", strDevHome + rs.getString("cm_dirpath").replace("/", "\\"));
											}
										} else {
											rst.put("pcdir", strDevHome + rs.getString("cm_dirpath").replace("/", "\\"));
										}
										break;
									}
								}
		
							}else {
								rst.put("cm_dirpath", rs.getString("cm_dirpath"));
								rst.put("cm_dirpath2", rs.getString("cm_dirpath2"));
							}
							
							if (rs.getString("cr_editcon") != null) {
							   rst.put("cr_sayu", rs.getString("cr_editcon"));
							} else rst.put("cr_sayu", "");
							if (rs.getInt("vercnt") == 0) {
								if (rs.getInt("cr_lstver") >= 9999) rst.put("cr_aftver", "1");
								else rst.put("cr_aftver", Integer.toString(rs.getInt("cr_lstver")+1));
							} else {
								if (rs.getInt("cr_lstver") >= rs.getInt("vercnt")) {
									rst.put("cr_aftver", "1");	
								} else rst.put("cr_aftver", Integer.toString(rs.getInt("cr_lstver")+1));
							}
							if (ReqCd.equals("05") || ReqCd.equals("09")) rst.put("reqcd", ReqCd);
							else {
								if (rs.getInt("cr_lstver") > 0) rst.put("reqcd", "04");
								else rst.put("reqcd", "03");
							}
							rst.put("selected_flag","0");
							rst.put("outSayu",rs.getString("cr_sayu"));
							rsval.add(rst);
							rst = null;
						}
					}
				}
				rs.close();
				pstmt.close();
			//} 
			
			
			cnt = 0;
			ccnt = 0;
			strQuery.setLength(0);			
			strQuery.append("select /*+ ALL_ROWS */                                                  \n");
        	strQuery.append("       a.cr_rsrcname,a.cr_lstver,a.cr_syscd,a.cr_dsncd, \n");
		    strQuery.append("       a.cr_itemid,a.cr_rsrccd,'' cr_acptno,a.cr_story,a.cr_jobcd,  \n");
		    strQuery.append("       a.cr_acptno cr_baseno,to_char(a.cr_lastdate,'yyyy/mm/dd hh24:mi') enddate, \n");
		    strQuery.append("       '' cr_editcon,a.cr_status,c.cm_dirpath,i.cm_info,            \n");
		    strQuery.append("       lpad(i.cm_stepsta,4,'0') prcreq,nvl(i.cm_vercnt,50) vercnt,  \n");
		    strQuery.append("       d.cm_codename,e.cm_codename jawon,a.cr_editor,f.cm_username, \n");
		    strQuery.append("       b.cm_jobname,to_char(a.cr_lastdate,'yyyy/mm/dd') lastdate,    \n");
		    strQuery.append("       to_char(nvl(a.cr_lstdat,SYSDATE),'yyyymmdd') lstdat,             \n");		    
		    strQuery.append("       nvl(a.cr_orderid,'') cr_orderid, a.cr_rpaflag,								 \n");
		    strQuery.append("       REPLACE(c.CM_DIRPATH, (SELECT CM_VOLPATH FROM CMM0038  							\n");
			strQuery.append("       WHERE CM_SYSCD = A.CR_SYSCD AND CM_RSRCCD = A.CR_RSRCCD AND CM_SVRCD = (select cm_dirbase from cmm0030 where cm_syscd = A.cr_syscd)),	\n");
			strQuery.append("       (SELECT CM_VOLPATH FROM CMM0038 WHERE CM_SYSCD = A.CR_SYSCD 					\n");
			strQuery.append("       AND CM_RSRCCD = A.CR_RSRCCD AND CM_SVRCD = '01')) as cm_dirpath2				\n");
			strQuery.append("  from cmm0040 f,cmm0102 b,cmm0020 e,cmr0020 a,cmm0070 c,cmm0020 d, \n"); 
			strQuery.append("       cmm0036 i                                                    \n"); 
			strQuery.append(" where a.cr_syscd=?                                                 \n");
	
			if (RsrcName != null && !"".equals(RsrcName)) 
			{	
					if (!RsrcCd.equals("00")) 
						strQuery.append("and a.cr_rsrccd=?                   			 		\n");
					
					if(itemid.equals("")){
						if(LikeSw == true)
						{
								if (UpLowSw == true) 
									strQuery.append("and a.cr_rsrcname like ?                  	\n");
								else 
									strQuery.append("and upper(a.cr_rsrcname) like upper(?)  \n");
						}	
						else	
						{
							if (UpLowSw == true) 
								strQuery.append("and a.cr_rsrcname = ?   	             		\n");
							else 
								strQuery.append("and upper(a.cr_rsrcname) = upper(?)      \n");
						}
					}
					else
						strQuery.append("and a.cr_itemid = ?   \n");
			} 
			else if (!RsrcCd.equals("00") && !RsrcCd.equals("ID")) 
				strQuery.append("and a.cr_rsrccd=?                                     \n");
		
		
			
			
			//05:폐기 또는 09:무수정
			if (ReqCd.equals("05") || ReqCd.equals("09")) {
				strQuery.append(" and a.cr_lstver>0 and a.cr_status='0'                         \n");
				if (ReqCd.equals("09")) 
					strQuery.append("and nvl(a.cr_nomodify,'0')='0'                             \n");	
			} else {
				strQuery.append(" and a.cr_lstver=0 and a.cr_status='3'							\n");
			}
			
			strQuery.append(" and a.cr_baseitem is null						\n");				
			
			
			
			if (SysInfo.substring(2,3).equals("1") && selfSw == false) {
				strQuery.append("and h.cm_syscd=? and h.cm_userid=? and h.cm_closedt is null    \n");
				strQuery.append("and h.cm_syscd=a.cr_syscd and h.cm_jobcd=a.cr_jobcd            \n");
			} else if (selfSw == true) {
				strQuery.append("and (nvl(a.cr_lstusr,a.cr_editor)=? or         \n");
				strQuery.append("     a.cr_editor=?) and  f.cm_userid=?         \n");
			}else{
				strQuery.append("and a.cr_jobcd in (select cm_jobcd from cmm0044    			\n");
				strQuery.append("					where cm_syscd = ? and cm_userid = ?)		 \n");
			}
			
			if (!"".equals(DsnCd)) {
				if (DsnCd.substring(0,1).equals("F")) {
					strQuery.append(" and c.cm_dirpath like ?                                   \n");
				} else strQuery.append(" and a.cr_dsncd=?                                       \n");
			}
			
			strQuery.append("and a.cr_syscd=c.cm_syscd and a.cr_dsncd=c.cm_dsncd                \n");
			strQuery.append("and d.cm_macode='CMR0020' and a.cr_status=d.cm_micode              \n");
			strQuery.append("and e.cm_macode='JAWON' and a.cr_rsrccd=e.cm_micode                \n");
			strQuery.append("and nvl(a.cr_lstusr,a.cr_editor)=f.cm_userid                       \n");
			strQuery.append("and a.cr_jobcd=b.cm_jobcd                                          \n");
			strQuery.append("and c.cm_dirpath = ?												\n"); // 20230102 경로
			strQuery.append("and a.cr_syscd=i.cm_syscd and a.cr_rsrccd=i.cm_rsrccd              \n");
			if (RsrcCd.equals("00")) {
				strQuery.append("and a.cr_rsrccd in (");
				for (i=0;strRsrc.length>i;i++) {
					if (i==0) strQuery.append("?");
					else strQuery.append(",?");
				}
				strQuery.append(") \n");
			}
		    if (!"".equals(txtORDERInfo) && txtORDERInfo != null && !"0000".equals(txtORDERInfo)){
		    	strQuery.append("and a.cr_orderid is not null \n");
		    	strQuery.append("and a.cr_orderid=? \n");
		    }
			//strQuery.append("order by cr_rsrcname                                               \n");
			
			pstmt = conn.prepareStatement(strQuery.toString());	
//			pstmt = new LoggableStatement(conn,strQuery.toString());
			
			pstmt.setString(++cnt, SysCd);
			if (RsrcName != null && !"".equals(RsrcName)) {
	    	   if (!RsrcCd.equals("00")) pstmt.setString(++cnt, RsrcCd);
	    	   if(itemid.equals("")){
		    	   if(LikeSw == true){
		    		   pstmt.setString(++cnt, "%" + RsrcName + "%");
		    	   }else{
		    		   pstmt.setString(++cnt, RsrcName);
		    	   }
	    	   }
	    	   else
	    		   pstmt.setString(++cnt, itemid);
			} else if (!RsrcCd.equals("00")) pstmt.setString(++cnt, RsrcCd);
			if (SysInfo.substring(2,3).equals("1") && selfSw == false) {
				pstmt.setString(++cnt, SysCd);
				pstmt.setString(++cnt, UserId);	
			} else if (selfSw == true) {
				pstmt.setString(++cnt, UserId);	
				pstmt.setString(++cnt, UserId);
				pstmt.setString(++cnt, UserId);
			}else{
				pstmt.setString(++cnt, SysCd);
				pstmt.setString(++cnt, UserId);	
			}
			if (!"".equals(DsnCd)) {
				if (DsnCd.substring(0,1).equals("F")) {
					pstmt.setString(++cnt, DsnCd.substring(1) + "%");
				} else pstmt.setString(++cnt, szDsnCD);
			}
			//if (DirPath != "") pstmt.setString(++cnt, DirPath);
			pstmt.setString(++cnt, DirPath);	// 20230102
			if (RsrcCd.equals("00")) {
				for (i=0 ; i<strRsrc.length ; i++) {
            		pstmt.setString(++cnt, strRsrc[i]);
            	}
			}
		    if (!"".equals(txtORDERInfo) && txtORDERInfo != null && !"0000".equals(txtORDERInfo)){
		    	pstmt.setString(++cnt, txtORDERInfo);
		    }
//		    ecamsLogger.error(((LoggableStatement)pstmt).getQueryString());           
	        rs = pstmt.executeQuery();
		        		        
				while (rs.next()){
					isCheckIn = false;
					ChkinSw = true;
					//차세대시스템이면 특정확장자만(CMM0020(CHKIN))보여주도록 조회
					if(SysCd.equals("01200")){
						if(rs.getString("cr_rsrccd").equals("25")){
							strQuery.setLength(0);			
							strQuery.append("select cm_codename 									\n");
							strQuery.append("from cmm0020 											\n");
							strQuery.append("where cm_macode ='CHKIN'								\n");
							pstmt2 = conn.prepareStatement(strQuery.toString());	
							//pstmt2 = new LoggableStatement(conn,strQuery.toString());
							//ecamsLogger.error(((LoggableStatement)pstmt2).getQueryString());
							rs2 = pstmt2.executeQuery();
							while(rs2.next()){
								if(rs.getString("cr_rsrcname").lastIndexOf(rs2.getString("cm_codename"))>0){
									ChkinSw = true;
									break;
								}else{
									ChkinSw = false;
								}
							}
							rs2.close();
							pstmt2.close();
						}
						
					}
					if(ChkinSw){//여기까지 140717 수진
					//=============================================================================
						if(rs.getString("cm_info").substring(57,58).equals("1")){
							ccnt = 0;
							strQuery.setLength(0);			
							strQuery.append("select count(*) cnt 									\n");
							strQuery.append("from cmr0020 a,cmm0070 b, CMR1010 c					\n");
							strQuery.append("where c.cr_syscd = ?  									\n");
							strQuery.append("and c.cr_rsrcname = ?									\n");
							strQuery.append("and b.cm_dirpath = ?									\n");
							//strQuery.append("and c.CR_EDITOR   = ?	   								\n");
							strQuery.append("and c.CR_SYSCD = B.CM_SYSCD							\n");
							strQuery.append("and c.CR_DSNCD = B.CM_DSNCD  							\n");
							strQuery.append("and c.CR_ITEMID = A.CR_ITEMID							\n");
							strQuery.append("AND c.CR_CONFNO IS NULL								\n");
							strQuery.append("AND c.CR_STATUS IN ('8','9')							\n");
							strQuery.append("AND substr(c.cr_acptno,5,2)='03'						\n");
							strQuery.append("AND a.cr_status = '0'          						\n");						
							
							pstmt2 = connD.prepareStatement(strQuery.toString());	
							pstmt2 = new LoggableStatement(connD,strQuery.toString());
							pstmt2.setString(++ccnt, rs.getString("cr_syscd"));
							pstmt2.setString(++ccnt, rs.getString("cr_rsrcname"));
							pstmt2.setString(++ccnt, rs.getString("cm_dirpath"));
							//pstmt2.setString(++ccnt, rs.getString("cr_editor"));
							ecamsLogger.error(((LoggableStatement)pstmt2).getQueryString());
							rs2 = pstmt2.executeQuery();
							
							if(rs2.next()){
								if(rs2.getInt("cnt")>0) isCheckIn = true;
							}
							rs2.close();
							pstmt2.close();
						}else{
							isCheckIn = true;
						}
						
						if(isCheckIn){
							rst = new HashMap<String, String>();
							if (rs.getString("cr_acptno") != null) {
								if (!svAcpt.equals(rs.getString("cr_acptno"))) {
								   rst.put("acptno", rs.getString("cr_acptno").substring(2,4) + 
										    "-" + rs.getString("cr_acptno").substring(4,6) + 
										    "-" + rs.getString("cr_acptno").substring(6,12));
								   svAcpt = rs.getString("cr_acptno");
								} else {
								   rst.put("acptno", "");	
								}
							} else {
								rst.put("acptno", "");
							}
							rst.put("cr_rsrcname", rs.getString("cr_rsrcname"));
							rst.put("jawon", rs.getString("jawon"));
							
							if (rs.getString("cr_story") != null) rst.put("cr_story", rs.getString("cr_story"));
							else  rst.put("cr_story", "");
							rst.put("cr_lstver", Integer.toString(rs.getInt("cr_lstver")));
							rst.put("cr_editor", rs.getString("cr_editor"));
							rst.put("cm_codename", rs.getString("cm_codename"));
							rst.put("cr_syscd", rs.getString("cr_syscd"));
							rst.put("cr_dsncd", rs.getString("cr_dsncd"));
							rst.put("cr_itemid", rs.getString("cr_itemid"));
							rst.put("cr_jobcd", rs.getString("cr_jobcd"));
							rst.put("cr_rsrccd", rs.getString("cr_rsrccd"));
							rst.put("reqcd", ReqCd);
							rst.put("cr_acptno", rs.getString("cr_acptno"));
							rst.put("cm_username", rs.getString("cm_username"));
							rst.put("cm_jobname", rs.getString("cm_jobname"));
							rst.put("cr_lastdate", rs.getString("lastdate"));
							rst.put("cm_info", rs.getString("cm_info"));
							rst.put("cr_status", rs.getString("cr_status"));
							rst.put("prcseq", rs.getString("prcreq"));
							
							rst.put("cr_orderid", rs.getString("cr_orderid"));
							rst.put("cr_rpaflag", rs.getString("cr_rpaflag"));
							
							rst.put("enddate", rs.getString("enddate"));
							rst.put("lstdat", rs.getString("lstdat"));							
							if (rs.getString("cr_baseno") != null) rst.put("cr_baseno", rs.getString("cr_baseno"));
							else rst.put("cr_baseno", "");
							
							if (rs.getString("cr_editcon") != null) {
							   rst.put("cr_sayu", rs.getString("cr_editcon"));
							} else rst.put("cr_sayu", "");
							if (rs.getInt("vercnt") == 0) {
								if (rs.getInt("cr_lstver") >= 9999) rst.put("cr_aftver", "1");
								else rst.put("cr_aftver", Integer.toString(rs.getInt("cr_lstver")+1));
							} else {
								if (rs.getInt("cr_lstver") >= rs.getInt("vercnt")) {
									rst.put("cr_aftver", "1");	
								} else rst.put("cr_aftver", Integer.toString(rs.getInt("cr_lstver")+1));
							}
							if (ReqCd.equals("05") || ReqCd.equals("09")) rst.put("reqcd", ReqCd);
							else {
								if (rs.getInt("cr_lstver") > 0) rst.put("reqcd", "04");
								else rst.put("reqcd", "03");
							}
							rst.put("cm_dirpath", rs.getString("cm_dirpath"));
							rst.put("cm_dirpath2", rs.getString("cm_dirpath2"));
							if (rs.getString("cm_info").substring(44,45).equals("1") && strDevHome != null) {						
								for (i=0 ; svrList.size()>i ; i++) {
									if (svrList.get(i).get("cm_rsrccd").equals(rs.getString("cr_rsrccd"))) {
										strVolPath = svrList.get(i).get("cm_volpath");// /scmtst/hyjungtest/dev
										strDirPath = rs.getString("cm_dirpath");// /scmtst/hyjungtest/dev/kicc_flex/src
										
										if (strVolPath != null && !"".equals(strVolPath)) {
											if (strDirPath.length()>=strVolPath.length() && strDirPath.substring(0,strVolPath.length()).equals(strVolPath)) {
												rst.put("pcdir", strDevHome + strDirPath.substring(strVolPath.length()).replace("/", "\\"));
											}else{
												rst.put("pcdir", strDevHome + rs.getString("cm_dirpath").replace("/", "\\"));
											}
										} else {
											rst.put("pcdir", strDevHome + rs.getString("cm_dirpath").replace("/", "\\"));
										}
										break;
									}
								}
							}else {
								rst.put("cm_dirpath", rs.getString("cm_dirpath"));
								rst.put("cm_dirpath2", rs.getString("cm_dirpath2"));
							}
							rst.put("selected_flag","0");
							rst.put("outSayu","");
							rsval.add(rst);
							rst = null;
						}
					}
				}//end of while-loop statement
				rs.close();
				pstmt.close();
            //}
			}
			conn.close();
			connD.close();
            rs = null;
            pstmt = null;
			rs2 = null;
			pstmt2 = null;
            conn = null;
            connD = null;
			//ecamsLogger.error(rsval.toString());					
			returnObjectArray = rsval.toArray();
			rsval = null;	
			
			return returnObjectArray;
			
		} catch (SQLException sqlexception) {
			sqlexception.printStackTrace();
			ecamsLogger.error("## Cmr0200.getFileList_excel() SQLException START ##");
			ecamsLogger.error("## Error DESC : ", sqlexception);	
			ecamsLogger.error("## Cmr0200.getFileList_excel() SQLException END ##");			
			throw sqlexception;
		} catch (Exception exception) {
			exception.printStackTrace();
			ecamsLogger.error("## Cmr0200.getFileList_excel() Exception START ##");				
			ecamsLogger.error("## Error DESC : ", exception);	
			ecamsLogger.error("## Cmr0200.getFileList_excel() Exception END ##");				
			throw exception;
		}finally{
			if (strQuery != null) 	strQuery = null;
			if (rs != null)     try{rs.close();}catch (Exception ex){ex.printStackTrace();}
			if (pstmt != null)  try{pstmt.close();}catch (Exception ex2){ex2.printStackTrace();}
			if (rs2 != null)    try{rs2.close();}catch (Exception ex){ex.printStackTrace();}
			if (pstmt2 != null) try{pstmt2.close();}catch (Exception ex2){ex2.printStackTrace();}
			//if (rsval != null) rsval=null;
			if (conn != null){
				try{
					ConnectionResource.release(conn);
				}catch(Exception ex3){
					ecamsLogger.error("## Cmr0200.getFileList_excel() connection release exception ##");
					ex3.printStackTrace();
				}
			}
		}
	}
	
//	public Object[] getReqList(String UserId,String SysCd,String SinCd,String ReqCd,String TstSw,String RsrcName,String DsnCd,
//			String DirPath,String SysInfo,String RsrcCd,boolean UpLowSw,boolean selfSw,boolean LikeSw,String txtORDERInfo, String itemid) throws SQLException, Exception {
	public Object[] getReqList(HashMap<String, String> paramMap) throws SQLException, Exception {
		
		Connection        conn        = null;
		Connection        connD       = null;
		PreparedStatement pstmt       = null;
		ResultSet         rs          = null;
		PreparedStatement pstmt2      = null;
		ResultSet         rs2         = null;
		StringBuffer      strQuery    = new StringBuffer();
		String            svAcpt      = "";
		int               cnt         = 0;
		int				  ccnt		  = 0;
	
		Object[] returnObjectArray = null;  
		ArrayList<HashMap<String, String>>  rsval = new ArrayList<HashMap<String, String>>();
		HashMap<String, String>			  rst		  = null;
		ConnectionContext connectionContext = new ConnectionResource();	
		ConnectionContext connectionContextD = new ConnectionResource(false,"D");	
		String			  szDsnCD = "";
		String			  strRsrcCd = "";
		String            strRsrc[] = null;
		int               i = 0;
		int               allCd = 0;
		boolean           delSw = false;
		boolean           ChkinSw = false;
		ArrayList<HashMap<String, String>> svrList = new ArrayList<HashMap<String, String>>();
		
		String UserId = paramMap.get("UserId");
		String SysCd = paramMap.get("SysCd");
		String JobCd = paramMap.get("JobCd");
		String SinCd = paramMap.get("SinCd");
		String ReqCd = paramMap.get("ReqCd");
		String TstSw = paramMap.get("TstSw");
		String RsrcName = paramMap.get("RsrcName");
		String DsnCd = paramMap.get("DsnCd");
		String DirPath = paramMap.get("DirPath");
		String SysInfo = paramMap.get("SysInfo");
		String RsrcCd = paramMap.get("RsrcCd");
		boolean UpLowSw =  Boolean.parseBoolean(paramMap.get("UpLowSw"));
		boolean selfSw = Boolean.parseBoolean(paramMap.get("selfSw"));
		boolean LikeSw = Boolean.parseBoolean(paramMap.get("LikeSw"));
		String txtORDERInfo = paramMap.get("txtORDERInfo");
		String itemid  = paramMap.get("itemid");
		
		try {
			
			conn = connectionContext.getConnection();
			connD = connectionContextD.getConnection();
			
			svAcpt = "";
			String strVolPath = "";
			String strDirPath = "";
			String strDevHome = "";
			SysInfo sysinfo = new SysInfo();
			if (sysinfo.getLocalYn(SysCd)) {
				strQuery.setLength(0);
				strQuery.append("select cd_devhome from cmd0200               \n");
				strQuery.append(" where cd_syscd=? and cd_userid=?            \n");
				pstmt = conn.prepareStatement(strQuery.toString());
				pstmt.setString(1, SysCd);
				pstmt.setString(2, UserId);
				rs = pstmt.executeQuery();
				if (rs.next()) {
					strDevHome = rs.getString("cd_devhome");
				}
				rs.close();
				pstmt.close();

				svrList = sysinfo.getHomePath_conn(SysCd, conn);
			}
			sysinfo = null;
			
            if (RsrcCd == null || "".equals(RsrcCd)) RsrcCd = "00";
            //ecamsLogger.error("++++++++++RsrcCd+++++++++"+RsrcCd);
            delSw = true;
            if (ReqCd.equals("05")) {
            	strQuery.setLength(0);
            	strQuery.append("select count(*) cnt from cmm0036              \n");
            	strQuery.append(" where cm_syscd=? and cm_closedt is null      \n");
            	strQuery.append("   and substr(cm_info,18,1)='1'               \n");
            	pstmt = conn.prepareStatement(strQuery.toString());
            	pstmt.setString(1, SysCd);
            	rs = pstmt.executeQuery();
            	if (rs.next()) {
            		if (rs.getInt("cnt")==0) delSw = false;            		
            	}
            }
			strQuery.setLength(0);
			strQuery.append("select cm_rsrccd from cmm0036                     \n"); 
			strQuery.append(" where cm_syscd=? and cm_closedt is null          \n");
			if (SinCd.equals("03") && ReqCd.equals("05")) {
				strQuery.append("   and (substr(cm_info,18, 1)='1' or          \n");
				strQuery.append("        cm_rsrccd in (select y.cm_rsrccd      \n");
				strQuery.append("                        from cmm0036 x,cmm0037 y \n");
				strQuery.append("                       where x.cm_syscd=?     \n");
				strQuery.append("                         and x.cm_syscd=y.cm_syscd \n");
				strQuery.append("                         and x.cm_rsrccd=y.cm_samersrc \n");
				strQuery.append("                         and substr(x.cm_info,18,1)='1')) \n");
			} else if (ReqCd.equals("09")) {
				strQuery.append("  and  (substr(cm_info, 1, 1)='1'             \n");				
				strQuery.append("   or  substr(cm_info, 15, 1)='1')            \n");				
			} else {
				strQuery.append("   and substr(cm_info, 2, 1)='1'              \n");
				strQuery.append("   and substr(cm_info, 26, 1)='0'             \n");
			}
			strQuery.append("   and cm_rsrccd not in (select cm_samersrc       \n");
			strQuery.append("                           from cmm0037           \n");
			strQuery.append("                          where cm_syscd=?)       \n");
			
			pstmt = conn.prepareStatement(strQuery.toString());	
			//pstmt =  new LoggableStatement(conn, strQuery.toString());			
            pstmt.setString(1, SysCd);			
            pstmt.setString(2, SysCd);
            if (SinCd.equals("03") && ReqCd.equals("05"))
            	pstmt.setString(3, SysCd);
            
            //ecamsLogger.error(((LoggableStatement)pstmt).getQueryString());
            rs = pstmt.executeQuery();
            while (rs.next()) {
            	if (strRsrcCd.equals("")){
            		strRsrcCd = rs.getString("cm_rsrccd") + ",";
            	} else{
            		strRsrcCd = strRsrcCd + rs.getString("cm_rsrccd") + ",";
            	}
            }
            if(strRsrcCd == null || strRsrcCd.equals("")){
            	returnObjectArray = rsval.toArray();
    			rsval = null;
    			return returnObjectArray;
            }
			rs.close();
			pstmt.close();
			
			if(strRsrcCd.substring(strRsrcCd.length()-1).indexOf(",")>-1){
				strRsrcCd = strRsrcCd.substring(0,strRsrcCd.length()-1);
			}
			strRsrc = strRsrcCd.split(",");

 			//UserInfo     userinfo = new UserInfo();
			boolean adminYn = false;//userinfo.isAdmin_conn(UserId,conn);
			//userinfo = null; 
			
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
			
			cnt = 0;
			ccnt = 0;
			//reqcd : 00신규+수정  03신규 04수정 05폐기 08임시저장 09무수정
			//SinCd : 03체크인(테스트신청) 04 체크인(REAL)
			//TstSw : 0 테스트신청 없음              1 테스트신청완료건
			//ecamsLogger.error("[1234] ++++++++++ReqCd+++++ ReqCd :  "+ReqCd + " /TstSw : " + TstSw + " /SinCd : " + SinCd + " /selfSw : " + selfSw);
			//ReqCd :  00 /TstSw : 0 /SinCd : 03 /selfSw : false
			if (ReqCd.equals("05")) {
				if (TstSw.equals("1") && SinCd.equals("04") && delSw == true) allCd = 1;
				else allCd = 2;
			} else if (TstSw.equals("1") && SinCd.equals("04")){
				allCd = 1;
			} else if (ReqCd.equals("00")){
				allCd = 9;
			} else if (ReqCd.equals("99")){
				allCd = 9;
			} else if (ReqCd.equals("04") || (TstSw.equals("1") && SinCd.equals("04"))) {
				// 수정신청 또는 테스트적용 후 운영적용요청건
				if (TstSw.equals("1") && SinCd.equals("04")) allCd = 1;
				else if (TstSw.equals("0") && SinCd.equals("04")) allCd = 1;
				else if (RsrcName != null && !"".equals(RsrcName)) allCd = 9;
				else allCd = 1; 
			} else { //신규(테스트적용 또는 테스트없는 운영요청),무수정,폐기신청
				if (!ReqCd.equals("05") && !ReqCd.equals("09")) { //신규
					if (RsrcName != null && !"".equals(RsrcName)) allCd = 9;
					else allCd = 2;
				} else {
					allCd = 2;
				}
			}
			/*091030_서정모
			if(ReqCd.equals("05")){
				allCd = 2;
			}
			*/
			rsval.clear();
			
			//ecamsLogger.error("[1259] ++++++++++allCd+++++  :  "+allCd);

	        boolean isCheckIn = false;
	        strQuery.setLength(0);
			/*if (allCd == 1 || allCd == 9) {*/
				strQuery.append("select /*+ ALL_ROWS */                                                  \n");
				strQuery.append("       a.cr_rsrcname,a.cr_lstver,a.cr_syscd,a.cr_dsncd,                 \n");
			    strQuery.append("       a.cr_itemid,a.cr_rsrccd,b.cr_acptno,a.cr_story,b.cr_jobcd,       \n");
			    strQuery.append("       b.cr_baseno,to_char(b.cr_prcdate,'yyyy/mm/dd hh24:mi') enddate,  \n");
			    strQuery.append("       b.cr_editcon,a.cr_status,c.cm_dirpath,i.cm_info, a.cr_rpaflag,                \n");
			    strQuery.append(	"       lpad(i.cm_stepsta,4,'0') prcreq,nvl(i.cm_vercnt,9999) vercnt,\n");
			    strQuery.append("       d.cm_codename,e.cm_codename jawon,b.cr_editor,h.cm_username,     \n");
			    strQuery.append("       f.cm_jobname,to_char(a.cr_lastdate,'yyyy/mm/dd') lastdate,       \n");
			    strQuery.append("       to_char(nvl(a.cr_lstdat,SYSDATE),'yyyymmdd') lstdat,             \n");
			    strQuery.append("       g.cr_orderid,g.cr_emgcd,g.cr_sayu, a.cr_langcd,a.cr_compile, a.cr_teamcd, a.cr_sqlcheck, a.cr_document,	\n");
			    strQuery.append("       REPLACE(c.CM_DIRPATH, (SELECT CM_VOLPATH FROM CMM0038  							\n");
				strQuery.append("       WHERE CM_SYSCD = A.CR_SYSCD AND CM_RSRCCD = A.CR_RSRCCD AND CM_SVRCD = (select cm_dirbase from cmm0030 where cm_syscd = A.cr_syscd)),	\n");
				strQuery.append("       (SELECT CM_VOLPATH FROM CMM0038 WHERE CM_SYSCD = A.CR_SYSCD 					\n");
				strQuery.append("       AND CM_RSRCCD = A.CR_RSRCCD AND CM_SVRCD = '01')) as cm_dirpath2				\n");
				strQuery.append("  from cmm0102 f,cmm0040 h,cmr1000 g,cmm0020 e,cmr0020 a,cmr1010 b, \n"); 
				strQuery.append("       cmm0070 c,cmm0020 d,cmm0036 i                                \n"); 
				strQuery.append(" where a.cr_syscd=?                                                 \n");
				if (JobCd != null && !"0000".equals(JobCd)) {
					strQuery.append("and b.cr_jobcd=? 	                                            \n");
				}
				if (adminYn == false) {
					if (SysInfo.substring(2,3).equals("1") && selfSw == false) {
						strQuery.append("and h.cm_syscd=? and h.cm_userid=? and h.cm_closedt is null \n");
						strQuery.append("and h.cm_syscd=a.cr_syscd and h.cm_jobcd=a.cr_jobcd         \n");  
					} else if(selfSw==true){
						strQuery.append("and b.cr_editor=?                                        \n");
					} else{
						strQuery.append("and b.cr_jobcd in (select cm_jobcd from cmm0044			 \n");
						strQuery.append("					where cm_syscd = ?						 \n");
						strQuery.append("					and cm_userid = ?						)\n");
					}
				} else if (selfSw == true) strQuery.append("and a.cr_editor=?                        \n");
				if (!ReqCd.equals("00") && !ReqCd.equals("99")) {
					if (ReqCd.equals("03")) strQuery.append(" and a.cr_lstver=0                      \n"); 
					else                    strQuery.append(" and a.cr_lstver>0                      \n"); 
				}
				strQuery.append("and a.cr_syscd=i.cm_syscd and a.cr_rsrccd=i.cm_rsrccd               \n");

				
				/*
				if (SinCd.equals("04") && TstSw.equals("0")) {
					strQuery.append(" and a.cr_status ='5'                                            \n"); 
				}else if (SinCd.equals("03")) {
					if (ReqCd.equals("05")) {
						strQuery.append(" and a.cr_status='5'                                        \n"); 
					} else {
						strQuery.append(" and a.cr_status='5'                                        \n"); 
					}
				} else {
					strQuery.append(" and a.cr_status='B'                                            \n"); 						
				}
				*/
				if (SinCd.equals("04")) {
					strQuery.append(" AND ( EXISTS (select 'X'                                \n");
					strQuery.append("               from CMM0031 A1                           \n");
					strQuery.append("                   ,CMM0038 B1                           \n");
					strQuery.append("              where A1.CM_SYSCD = a.cr_syscd             \n");
					strQuery.append("                AND A1.CM_SVRCD IN ('13','15')           \n");
					strQuery.append("                AND A1.CM_CLOSEDT IS NULL                \n");
					strQuery.append("                AND A1.CM_SYSCD = B1.CM_SYSCD            \n");
					strQuery.append("                AND A1.CM_SVRCD = B1.CM_SVRCD            \n");
					strQuery.append("                AND A1.CM_SEQNO = B1.CM_SEQNO            \n");
					strQuery.append("                AND B1.CM_RSRCCD = a.cr_rsrccd           \n");
					strQuery.append("               )                                         \n");
					strQuery.append("       OR (                                              \n");
					strQuery.append("          NOT EXISTS (select 'X'                         \n");
					strQuery.append("               from CMM0031 A1                           \n");
					strQuery.append("                   ,CMM0038 B1                           \n");
					strQuery.append("              where A1.CM_SYSCD = a.cr_syscd             \n");
					strQuery.append("                AND A1.CM_SVRCD IN ('13','15')           \n");
					strQuery.append("                AND A1.CM_CLOSEDT IS NULL                \n");
					strQuery.append("                AND A1.CM_SYSCD = B1.CM_SYSCD            \n");
					strQuery.append("                AND A1.CM_SVRCD = B1.CM_SVRCD            \n");
					strQuery.append("                AND A1.CM_SEQNO = B1.CM_SEQNO            \n");
					strQuery.append("                AND B1.CM_RSRCCD = a.cr_rsrccd           \n");
					strQuery.append("               )                                         \n");
					strQuery.append("          and a.cr_status ='5'                           \n");
					strQuery.append("         )                                               \n");
					strQuery.append("    )                                                    \n");

//					
//					strQuery.append(" and (   (TESTSVR_RSRC_YN(a.cr_syscd, a.cr_rsrccd)  = 'OK' )  \n");   
//					strQuery.append("      or (TESTSVR_RSRC_YN(a.cr_syscd, a.cr_rsrccd) != 'OK' and a.cr_status ='5')  \n");
//					strQuery.append("     ) \n");
				}else {
					strQuery.append(" and a.cr_status ='5'                                            \n"); 					
				}
				
				strQuery.append("and b.cr_confno is null and b.cr_status in ('8','9')                \n");
				strQuery.append("and b.cr_acptno=g.cr_acptno                                         \n");
				
				/*
				if (SinCd.equals("04") && (ReqCd.equals("05") || ReqCd.equals("09"))) {
					strQuery.append("and g.cr_qrycd='03'                                       \n");
					if (ReqCd.equals("05")) strQuery.append("and b.cr_qrycd='05'               \n");
					else strQuery.append("and b.cr_qrycd='09' and nvl(a.cr_nomodify,'0')='1'   \n");
				} else if (SinCd.equals("03") || (SinCd.equals("04") && TstSw.equals("0")))
					strQuery.append("and g.cr_qrycd in('01','02')                                   \n");
				else {
					strQuery.append("and g.cr_qrycd='03'                                            \n");
					if (ReqCd.equals("00") || ReqCd.equals("99")) 
						strQuery.append("and b.cr_qrycd in ('03','04')          \n");
					else strQuery.append("and b.cr_qrycd=?                                          \n");
				}
				*/
				
				if (SinCd.equals("04") && (ReqCd.equals("05") || ReqCd.equals("09"))) {
					strQuery.append("and g.cr_qrycd='03'                                       \n");
					if (ReqCd.equals("05")) strQuery.append("and b.cr_qrycd='05'               \n");
					else strQuery.append("and b.cr_qrycd='09' and nvl(a.cr_nomodify,'0')='1'   \n");
				} else {
					//strQuery.append(" and (   (TESTSVR_RSRC_YN(a.cr_syscd, a.cr_rsrccd)  = 'OK' and g.cr_qrycd in('03'     )) \n");   
					//strQuery.append("      OR (TESTSVR_RSRC_YN(a.cr_syscd, a.cr_rsrccd) != 'OK' and g.cr_qrycd in('01','02')) \n");   
					//strQuery.append("     )																					  \n");
					strQuery.append("   and g.cr_qrycd in('01','02')								 \n");
				}
				
				if (!RsrcCd.equals("00")) strQuery.append("and a.cr_rsrccd=?                         \n");
				if(itemid.equals("")){
					if (RsrcName != null && !"".equals(RsrcName)) {
						if(LikeSw == true){
							if (UpLowSw == true) strQuery.append("and a.cr_exename like ?    				\n");
							else strQuery.append("and upper(a.cr_exename) like upper(?)      				\n");
						}else{
							if (UpLowSw == true) strQuery.append("and a.cr_exename = ?					    \n");
							else strQuery.append("and upper(a.cr_exename) = upper(?)					      \n");
						}
						
					}
				}
				else
					strQuery.append("and a.cr_itemid = ? \n");
				
				strQuery.append("and a.cr_itemid=b.cr_itemid and b.cr_itemid=b.cr_baseitem          \n");
				strQuery.append("and a.cr_syscd=c.cm_syscd and a.cr_dsncd=c.cm_dsncd                \n");
				strQuery.append("and d.cm_macode='CMR0020' and a.cr_status=d.cm_micode              \n");
				strQuery.append("and e.cm_macode='JAWON' and a.cr_rsrccd=e.cm_micode                \n");
				strQuery.append("and b.cr_editor=h.cm_userid and b.cr_jobcd=f.cm_jobcd              \n");
				strQuery.append("and a.cr_syscd=i.cm_syscd and a.cr_rsrccd=i.cm_rsrccd              \n");				
				strQuery.append("and i.cm_closedt is null                                           \n");
			    if (!"".equals(txtORDERInfo) && txtORDERInfo != null && !"0000".equals(txtORDERInfo)){
			    	strQuery.append("and g.cr_orderid is not null \n");
			    	strQuery.append("and g.cr_orderid=? \n");
			    }
			    
				pstmt = conn.prepareStatement(strQuery.toString());	
				pstmt = new LoggableStatement(conn,strQuery.toString());
				
				pstmt.setString(++cnt, SysCd);
				if (JobCd != null && !"0000".equals(JobCd)) {
					pstmt.setString(++cnt, JobCd);
				}
				if (adminYn == false) {
					if(selfSw == true){
						pstmt.setString(++cnt, UserId);
					}else{
						pstmt.setString(++cnt, SysCd);
						pstmt.setString(++cnt, UserId);
					}
				} else if (selfSw == true) pstmt.setString(++cnt, UserId);
	           
				/*
				if (SinCd.equals("04") && (ReqCd.equals("05") || ReqCd.equals("09"))) {
				} 
				else if (SinCd.equals("03") || (SinCd.equals("04") && TstSw.equals("0"))){
				}					
				else {
					if (ReqCd.equals("00") || ReqCd.equals("99")) {
					}
					else{
						pstmt.setString(++cnt, ReqCd);
					}
				}
				*/
				
				if (!RsrcCd.equals("00")) pstmt.setString(++cnt, RsrcCd); 
				if (RsrcName != null && !"".equals(RsrcName))
					if(itemid.equals("")){
						if(LikeSw==true){
							pstmt.setString(++cnt, "%"+RsrcName+"%");
						}else{
							pstmt.setString(++cnt, RsrcName);
						}
					}
					else
						pstmt.setString(++cnt, itemid);
				if (!"".equals(txtORDERInfo) && txtORDERInfo != null && !"0000".equals(txtORDERInfo)){
			    	pstmt.setString(++cnt, txtORDERInfo);
			    }
			    ecamsLogger.error(((LoggableStatement)pstmt).getQueryString());
		        rs = pstmt.executeQuery();
		        
				while (rs.next()){	
					ChkinSw = true;
					isCheckIn = false;
					//차세대시스템이면 특정확장자만(CMM0020(CHKIN))보여주도록 조회
					if(SysCd.equals("01200")){
						if(rs.getString("cr_rsrccd").equals("25")){
							strQuery.setLength(0);			
							strQuery.append("select cm_codename 									\n");
							strQuery.append("from cmm0020 											\n");
							strQuery.append("where cm_macode ='CHKIN'								\n");
							pstmt2 = conn.prepareStatement(strQuery.toString());	
							//pstmt2 = new LoggableStatement(conn,strQuery.toString());
							//ecamsLogger.error(((LoggableStatement)pstmt2).getQueryString());
							rs2 = pstmt2.executeQuery();
							while(rs2.next()){
								if(rs.getString("cr_rsrcname").lastIndexOf(rs2.getString("cm_codename"))>0){
									ChkinSw = true;
									break;
								}else{
									ChkinSw = false;
								}
							}
							rs2.close();
							pstmt2.close();
						}
					}
					if(ChkinSw){//여기까지 140717 수진
						//=============================================================================
						if(rs.getString("cm_info").substring(57,58).equals("1")){
							ccnt = 0;
							strQuery.setLength(0);			
							strQuery.append("select count(*) cnt 									\n");
							strQuery.append("from cmr0020 a,cmm0070 b, CMR1010 c					\n");
							strQuery.append("where c.cr_syscd = ?  									\n");
							strQuery.append("and c.cr_rsrcname = ?									\n");
							strQuery.append("and b.cm_dirpath = ?									\n");
							//strQuery.append("and c.CR_EDITOR   = ?	   								\n");
							strQuery.append("and c.CR_SYSCD = B.CM_SYSCD							\n");
							strQuery.append("and c.CR_DSNCD = B.CM_DSNCD  							\n");
							strQuery.append("and c.CR_ITEMID = A.CR_ITEMID							\n");
							strQuery.append("AND c.CR_CONFNO IS NULL								\n");
							strQuery.append("AND c.CR_STATUS IN ('8','9')							\n");
							strQuery.append("AND substr(c.cr_acptno,5,2)='03'						\n");
							strQuery.append("AND a.cr_status = '0'                          		\n");						
							
							pstmt2 = connD.prepareStatement(strQuery.toString());	
							pstmt2 = new LoggableStatement(connD,strQuery.toString());
							pstmt2.setString(++ccnt, rs.getString("cr_syscd"));
							pstmt2.setString(++ccnt, rs.getString("cr_rsrcname"));
							pstmt2.setString(++ccnt, rs.getString("cm_dirpath"));
							//pstmt2.setString(++ccnt, rs.getString("cr_editor"));
							ecamsLogger.error(((LoggableStatement)pstmt2).getQueryString());
							rs2 = pstmt2.executeQuery();
							
							if(rs2.next()){
								if(rs2.getInt("cnt")>0) isCheckIn = true;
							}
							rs2.close();
							pstmt2.close();
						}else{
							isCheckIn = true;
						}
						
						if(isCheckIn){
							rst = new HashMap<String, String>();
							//rst.put("ID", Integer.toString(rs.getRow()));
							if (rs.getString("cr_acptno") != null) {
								if (!svAcpt.equals(rs.getString("cr_acptno"))) {
								   rst.put("acptno", rs.getString("cr_acptno").substring(2,4) + 
										    "-" + rs.getString("cr_acptno").substring(4,6) + 
										    "-" + rs.getString("cr_acptno").substring(6,12));
								   svAcpt = rs.getString("cr_acptno");
								} else {
								   rst.put("acptno", "");	
								}
							} else {
								rst.put("acptno", "");
							}
							rst.put("cr_rsrcname", rs.getString("cr_rsrcname"));
							rst.put("jawon", rs.getString("jawon"));
							
							if (rs.getString("cr_story") != null) rst.put("cr_story", rs.getString("cr_story"));
							else  rst.put("cr_story", "");
							rst.put("cr_lstver", Integer.toString(rs.getInt("cr_lstver")));
							rst.put("cr_editor", rs.getString("cr_editor"));
							rst.put("cm_codename", rs.getString("cm_codename"));
							rst.put("cr_syscd", rs.getString("cr_syscd"));
							rst.put("cr_dsncd", rs.getString("cr_dsncd"));
							rst.put("cr_itemid", rs.getString("cr_itemid"));
							rst.put("baseitem", rs.getString("cr_itemid"));
							rst.put("cr_jobcd", rs.getString("cr_jobcd"));
							rst.put("cr_rsrccd", rs.getString("cr_rsrccd"));
							rst.put("reqcd", ReqCd);
							rst.put("cr_acptno", rs.getString("cr_acptno"));
							rst.put("cm_username", rs.getString("cm_username"));
							rst.put("cm_jobname", rs.getString("cm_jobname"));
							rst.put("cr_lastdate", rs.getString("lastdate"));
							rst.put("lstdat", rs.getString("lstdat"));
							rst.put("cm_info", rs.getString("cm_info"));
							rst.put("cr_status", rs.getString("cr_status"));
							rst.put("prcseq", rs.getString("prcreq"));
							rst.put("cr_emgcd", rs.getString("cr_emgcd"));
							rst.put("cr_orderid", rs.getString("cr_orderid"));
							rst.put("enddate", rs.getString("enddate"));
							rst.put("cr_rpaflag", rs.getString("cr_rpaflag"));
							
							if (rs.getString("cr_baseno") != null) rst.put("cr_baseno", rs.getString("cr_baseno"));
							else rst.put("cr_baseno", "");
							rst.put("cm_dirpath", rs.getString("cm_dirpath"));
							rst.put("cm_dirpath2", rs.getString("cm_dirpath2"));
							if (rs.getString("cm_info").substring(44,45).equals("1") && strDevHome != null) {						
								for (i=0 ; svrList.size()>i ; i++) {
									if (svrList.get(i).get("cm_rsrccd").equals(rs.getString("cr_rsrccd"))) {
										strVolPath = svrList.get(i).get("cm_volpath");// /scmtst/hyjungtest/dev
										strDirPath = rs.getString("cm_dirpath");// /scmtst/hyjungtest/dev/kicc_flex/src
										
										if (strVolPath != null && !"".equals(strVolPath)) {
											if (strDirPath.length()>=strVolPath.length() && strDirPath.substring(0,strVolPath.length()).equals(strVolPath)) {
												rst.put("pcdir", strDevHome + strDirPath.substring(strVolPath.length()).replace("/", "\\"));
											}else{
												rst.put("pcdir", strDevHome + rs.getString("cm_dirpath").replace("/", "\\"));
											}
										} else {
											rst.put("pcdir", strDevHome + rs.getString("cm_dirpath").replace("/", "\\"));
										}
										break;
									}
								}
		
							}else {
								rst.put("cm_dirpath", rs.getString("cm_dirpath"));
								rst.put("cm_dirpath2", rs.getString("cm_dirpath2"));
							}
							
							if (rs.getString("cr_editcon") != null) {
							   rst.put("cr_sayu", rs.getString("cr_editcon"));
							} else rst.put("cr_sayu", "");
							if (rs.getInt("vercnt") == 0) {
								if (rs.getInt("cr_lstver") >= 9999) rst.put("cr_aftver", "1");
								else rst.put("cr_aftver", Integer.toString(rs.getInt("cr_lstver")+1));
							} else {
								if (rs.getInt("cr_lstver") >= rs.getInt("vercnt")) {
									rst.put("cr_aftver", "1");	
								} else rst.put("cr_aftver", Integer.toString(rs.getInt("cr_lstver")+1));
							}
							if (ReqCd.equals("05") || ReqCd.equals("09")) rst.put("reqcd", ReqCd);
							else {
								if (rs.getInt("cr_lstver") > 0) rst.put("reqcd", "04");
								else rst.put("reqcd", "03");
							}
							rst.put("selected_flag","0");
							rst.put("outSayu",rs.getString("cr_sayu"));
							rsval.add(rst);
							rst = null;
						}
					}
				}
				rs.close();
				pstmt.close();
			//} 
			
			
			cnt = 0;
			ccnt = 0;
           /* if (allCd == 2 || allCd == 9) {*/
            	//if (allCd == 9) strQuery.append("union                                               \n");
			strQuery.setLength(0);			
			strQuery.append("select /*+ ALL_ROWS */                                                  \n");
        	strQuery.append("       a.cr_rsrcname,a.cr_lstver,a.cr_syscd,a.cr_dsncd, \n");
		    strQuery.append("       a.cr_itemid,a.cr_rsrccd,'' cr_acptno,a.cr_story,a.cr_jobcd,  \n");
		    strQuery.append("       a.cr_acptno cr_baseno,to_char(a.cr_lastdate,'yyyy/mm/dd hh24:mi') enddate, \n");
		    strQuery.append("       '' cr_editcon,a.cr_status,c.cm_dirpath,i.cm_info,            \n");
		    strQuery.append("       lpad(i.cm_stepsta,4,'0') prcreq,nvl(i.cm_vercnt,50) vercnt,  \n");
		    strQuery.append("       d.cm_codename,e.cm_codename jawon,a.cr_editor,f.cm_username, \n");
		    strQuery.append("       b.cm_jobname,to_char(a.cr_lastdate,'yyyy/mm/dd') lastdate,    \n");
		    strQuery.append("       to_char(nvl(a.cr_lstdat,SYSDATE),'yyyymmdd') lstdat,             \n");		    
		    strQuery.append("       nvl(a.cr_orderid,'') cr_orderid, a.cr_rpaflag,								 \n");
		    strQuery.append("       REPLACE(c.CM_DIRPATH, (SELECT CM_VOLPATH FROM CMM0038  							\n");
			strQuery.append("       WHERE CM_SYSCD = A.CR_SYSCD AND CM_RSRCCD = A.CR_RSRCCD AND CM_SVRCD = (select cm_dirbase from cmm0030 where cm_syscd = A.cr_syscd)),	\n");
			strQuery.append("       (SELECT CM_VOLPATH FROM CMM0038 WHERE CM_SYSCD = A.CR_SYSCD 					\n");
			strQuery.append("       AND CM_RSRCCD = A.CR_RSRCCD AND CM_SVRCD = '01')) as cm_dirpath2				\n");
			strQuery.append("  from cmm0040 f,cmm0102 b,cmm0020 e,cmr0020 a,cmm0070 c,cmm0020 d, \n"); 
			strQuery.append("       cmm0036 i                                                    \n"); 
			strQuery.append(" where a.cr_syscd=?                                                 \n");
			if (JobCd != null && !"0000".equals(JobCd)) {
				strQuery.append("and a.cr_jobcd=? 	                                            \n");
			}
			if (RsrcName != null && !"".equals(RsrcName)) 
			{	
					if (!RsrcCd.equals("00")) 
						strQuery.append("and a.cr_rsrccd=?                   			 		\n");
					
					if(itemid.equals("")){
						if(LikeSw == true)
						{
								if (UpLowSw == true) 
									strQuery.append("and a.cr_rsrcname like ?                  	\n");
								else 
									strQuery.append("and upper(a.cr_exename) like upper(?)  \n");
						}	
						else	
						{
							if (UpLowSw == true) 
								strQuery.append("and a.cr_rsrcname = ?   	             		\n");
							else 
								strQuery.append("and upper(a.cr_exename) = upper(?)      \n");
						}
					}
					else
						strQuery.append("and a.cr_itemid = ?   \n");
			} 
			else if (!RsrcCd.equals("00") && !RsrcCd.equals("ID")) 
				strQuery.append("and a.cr_rsrccd=?                                     \n");
		
		
			
			
			//05:폐기 또는 09:무수정
			if (ReqCd.equals("05") || ReqCd.equals("09")) {
				strQuery.append(" and a.cr_lstver>0 and a.cr_status='0'                         \n");
				if (ReqCd.equals("09")) 
					strQuery.append("and nvl(a.cr_nomodify,'0')='0'                             \n");	
			} else {
				strQuery.append(" and a.cr_lstver=0 and a.cr_status='3'							\n");
			}
			
			strQuery.append(" and a.cr_baseitem is null						\n");				
			
			
			
			if (SysInfo.substring(2,3).equals("1") && selfSw == false) {
				strQuery.append("and h.cm_syscd=? and h.cm_userid=? and h.cm_closedt is null    \n");
				strQuery.append("and h.cm_syscd=a.cr_syscd and h.cm_jobcd=a.cr_jobcd            \n");
			} else if (selfSw == true) {
				strQuery.append("and (nvl(a.cr_lstusr,a.cr_editor)=? or         \n");
				strQuery.append("     a.cr_editor=?) and  f.cm_userid=?         \n");
			}else{
				strQuery.append("and a.cr_jobcd in (select cm_jobcd from cmm0044    			\n");
				strQuery.append("					where cm_syscd = ? and cm_userid = ?)		 \n");
			}
			
			if (!"".equals(DsnCd)) {
				if (DsnCd.substring(0,1).equals("F")) {
					strQuery.append(" and c.cm_dirpath like ?                                   \n");
				} else strQuery.append(" and a.cr_dsncd=?                                       \n");
			}
			
			strQuery.append("and a.cr_syscd=c.cm_syscd and a.cr_dsncd=c.cm_dsncd                \n");
			strQuery.append("and d.cm_macode='CMR0020' and a.cr_status=d.cm_micode              \n");
			strQuery.append("and e.cm_macode='JAWON' and a.cr_rsrccd=e.cm_micode                \n");
			strQuery.append("and nvl(a.cr_lstusr,a.cr_editor)=f.cm_userid                       \n");
			strQuery.append("and a.cr_jobcd=b.cm_jobcd                                          \n");
			strQuery.append("and a.cr_syscd=i.cm_syscd and a.cr_rsrccd=i.cm_rsrccd              \n");
			if (RsrcCd.equals("00")) {
				strQuery.append("and a.cr_rsrccd in (");
				for (i=0;strRsrc.length>i;i++) {
					if (i==0) strQuery.append("?");
					else strQuery.append(",?");
				}
				strQuery.append(") \n");
			}
		    if (!"".equals(txtORDERInfo) && txtORDERInfo != null && !"0000".equals(txtORDERInfo)){
		    	strQuery.append("and a.cr_orderid is not null \n");
		    	strQuery.append("and a.cr_orderid=? \n");
		    }
			//strQuery.append("order by cr_rsrcname                                               \n");
			
			pstmt = conn.prepareStatement(strQuery.toString());	
			//pstmt = new LoggableStatement(conn,strQuery.toString());
			
			pstmt.setString(++cnt, SysCd);
			if (JobCd != null && !"0000".equals(JobCd)) {
				pstmt.setString(++cnt, JobCd);
			}
			if (RsrcName != null && !"".equals(RsrcName)) {
	    	   if (!RsrcCd.equals("00")) pstmt.setString(++cnt, RsrcCd);
	    	   if(itemid.equals("")){
		    	   if(LikeSw == true){
		    		   pstmt.setString(++cnt, "%" + RsrcName + "%");
		    	   }else{
		    		   pstmt.setString(++cnt, RsrcName);
		    	   }
	    	   }
	    	   else
	    		   pstmt.setString(++cnt, itemid);
			} else if (!RsrcCd.equals("00")) pstmt.setString(++cnt, RsrcCd);
			if (SysInfo.substring(2,3).equals("1") && selfSw == false) {
				pstmt.setString(++cnt, SysCd);
				pstmt.setString(++cnt, UserId);	
			} else if (selfSw == true) {
				pstmt.setString(++cnt, UserId);	
				pstmt.setString(++cnt, UserId);
				pstmt.setString(++cnt, UserId);
			}else{
				pstmt.setString(++cnt, SysCd);
				pstmt.setString(++cnt, UserId);	
			}
			if (!"".equals(DsnCd)) {
				if (DsnCd.substring(0,1).equals("F")) {
					pstmt.setString(++cnt, DsnCd.substring(1) + "%");
				} else pstmt.setString(++cnt, szDsnCD);
			}
			//if (DirPath != "") pstmt.setString(++cnt, DirPath);
			if (RsrcCd.equals("00")) {
				for (i=0 ; i<strRsrc.length ; i++) {
            		pstmt.setString(++cnt, strRsrc[i]);
            	}
			}
		    if (!"".equals(txtORDERInfo) && txtORDERInfo != null && !"0000".equals(txtORDERInfo)){
		    	pstmt.setString(++cnt, txtORDERInfo);
		    }
		    //ecamsLogger.error(((LoggableStatement)pstmt).getQueryString());           
	        rs = pstmt.executeQuery();
		        		        
				while (rs.next()){
					isCheckIn = false;
					ChkinSw = true;
					//차세대시스템이면 특정확장자만(CMM0020(CHKIN))보여주도록 조회
					if(SysCd.equals("01200")){
						if(rs.getString("cr_rsrccd").equals("25")){
							strQuery.setLength(0);			
							strQuery.append("select cm_codename 									\n");
							strQuery.append("from cmm0020 											\n");
							strQuery.append("where cm_macode ='CHKIN'								\n");
							pstmt2 = conn.prepareStatement(strQuery.toString());	
							//pstmt2 = new LoggableStatement(conn,strQuery.toString());
							//ecamsLogger.error(((LoggableStatement)pstmt2).getQueryString());
							rs2 = pstmt2.executeQuery();
							while(rs2.next()){
								if(rs.getString("cr_rsrcname").lastIndexOf(rs2.getString("cm_codename"))>0){
									ChkinSw = true;
									break;
								}else{
									ChkinSw = false;
								}
							}
							rs2.close();
							pstmt2.close();
						}
						
					}
					if(ChkinSw){//여기까지 140717 수진
					//=============================================================================
						if(rs.getString("cm_info").substring(57,58).equals("1")){
							ccnt = 0;
							strQuery.setLength(0);			
							strQuery.append("select count(*) cnt 									\n");
							strQuery.append("from cmr0020 a,cmm0070 b, CMR1010 c					\n");
							strQuery.append("where c.cr_syscd = ?  									\n");
							strQuery.append("and c.cr_rsrcname = ?									\n");
							strQuery.append("and b.cm_dirpath = ?									\n");
							//strQuery.append("and c.CR_EDITOR   = ?	   								\n");
							strQuery.append("and c.CR_SYSCD = B.CM_SYSCD							\n");
							strQuery.append("and c.CR_DSNCD = B.CM_DSNCD  							\n");
							strQuery.append("and c.CR_ITEMID = A.CR_ITEMID							\n");
							strQuery.append("AND c.CR_CONFNO IS NULL								\n");
							strQuery.append("AND c.CR_STATUS IN ('8','9')							\n");
							strQuery.append("AND substr(c.cr_acptno,5,2)='03'						\n");
							strQuery.append("AND a.cr_status = '0'          						\n");						
							
							pstmt2 = connD.prepareStatement(strQuery.toString());	
							pstmt2 = new LoggableStatement(connD,strQuery.toString());
							pstmt2.setString(++ccnt, rs.getString("cr_syscd"));
							pstmt2.setString(++ccnt, rs.getString("cr_rsrcname"));
							pstmt2.setString(++ccnt, rs.getString("cm_dirpath"));
							//pstmt2.setString(++ccnt, rs.getString("cr_editor"));
							ecamsLogger.error(((LoggableStatement)pstmt2).getQueryString());
							rs2 = pstmt2.executeQuery();
							
							if(rs2.next()){
								if(rs2.getInt("cnt")>0) isCheckIn = true;
							}
							rs2.close();
							pstmt2.close();
						}else{
							isCheckIn = true;
						}
						
						if(isCheckIn){
							rst = new HashMap<String, String>();
							if (rs.getString("cr_acptno") != null) {
								if (!svAcpt.equals(rs.getString("cr_acptno"))) {
								   rst.put("acptno", rs.getString("cr_acptno").substring(2,4) + 
										    "-" + rs.getString("cr_acptno").substring(4,6) + 
										    "-" + rs.getString("cr_acptno").substring(6,12));
								   svAcpt = rs.getString("cr_acptno");
								} else {
								   rst.put("acptno", "");	
								}
							} else {
								rst.put("acptno", "");
							}
							rst.put("cr_rsrcname", rs.getString("cr_rsrcname"));
							rst.put("jawon", rs.getString("jawon"));
							
							if (rs.getString("cr_story") != null) rst.put("cr_story", rs.getString("cr_story"));
							else  rst.put("cr_story", "");
							rst.put("cr_lstver", Integer.toString(rs.getInt("cr_lstver")));
							rst.put("cr_editor", rs.getString("cr_editor"));
							rst.put("cm_codename", rs.getString("cm_codename"));
							rst.put("cr_syscd", rs.getString("cr_syscd"));
							rst.put("cr_dsncd", rs.getString("cr_dsncd"));
							rst.put("cr_itemid", rs.getString("cr_itemid"));
							rst.put("cr_jobcd", rs.getString("cr_jobcd"));
							rst.put("cr_rsrccd", rs.getString("cr_rsrccd"));
							rst.put("reqcd", ReqCd);
							rst.put("cr_acptno", rs.getString("cr_acptno"));
							rst.put("cm_username", rs.getString("cm_username"));
							rst.put("cm_jobname", rs.getString("cm_jobname"));
							rst.put("cr_lastdate", rs.getString("lastdate"));
							rst.put("cm_info", rs.getString("cm_info"));
							rst.put("cr_status", rs.getString("cr_status"));
							rst.put("prcseq", rs.getString("prcreq"));
							
							rst.put("cr_orderid", rs.getString("cr_orderid"));
							rst.put("cr_rpaflag", rs.getString("cr_rpaflag"));
							
							rst.put("enddate", rs.getString("enddate"));
							rst.put("lstdat", rs.getString("lstdat"));							
							if (rs.getString("cr_baseno") != null) rst.put("cr_baseno", rs.getString("cr_baseno"));
							else rst.put("cr_baseno", "");
							
							if (rs.getString("cr_editcon") != null) {
							   rst.put("cr_sayu", rs.getString("cr_editcon"));
							} else rst.put("cr_sayu", "");
							if (rs.getInt("vercnt") == 0) {
								if (rs.getInt("cr_lstver") >= 9999) rst.put("cr_aftver", "1");
								else rst.put("cr_aftver", Integer.toString(rs.getInt("cr_lstver")+1));
							} else {
								if (rs.getInt("cr_lstver") >= rs.getInt("vercnt")) {
									rst.put("cr_aftver", "1");	
								} else rst.put("cr_aftver", Integer.toString(rs.getInt("cr_lstver")+1));
							}
							if (ReqCd.equals("05") || ReqCd.equals("09")) rst.put("reqcd", ReqCd);
							else {
								if (rs.getInt("cr_lstver") > 0) rst.put("reqcd", "04");
								else rst.put("reqcd", "03");
							}
							rst.put("cm_dirpath", rs.getString("cm_dirpath"));
							rst.put("cm_dirpath2", rs.getString("cm_dirpath2"));
							if (rs.getString("cm_info").substring(44,45).equals("1") && strDevHome != null) {						
								for (i=0 ; svrList.size()>i ; i++) {
									if (svrList.get(i).get("cm_rsrccd").equals(rs.getString("cr_rsrccd"))) {
										strVolPath = svrList.get(i).get("cm_volpath");// /scmtst/hyjungtest/dev
										strDirPath = rs.getString("cm_dirpath");// /scmtst/hyjungtest/dev/kicc_flex/src
										
										if (strVolPath != null && !"".equals(strVolPath)) {
											if (strDirPath.length()>=strVolPath.length() && strDirPath.substring(0,strVolPath.length()).equals(strVolPath)) {
												rst.put("pcdir", strDevHome + strDirPath.substring(strVolPath.length()).replace("/", "\\"));
											}else{
												rst.put("pcdir", strDevHome + rs.getString("cm_dirpath").replace("/", "\\"));
											}
										} else {
											rst.put("pcdir", strDevHome + rs.getString("cm_dirpath").replace("/", "\\"));
										}
										break;
									}
								}
							}else {
								rst.put("cm_dirpath", rs.getString("cm_dirpath"));
								rst.put("cm_dirpath2", rs.getString("cm_dirpath2"));
							}
							rst.put("selected_flag","0");
							rst.put("outSayu","");
							rsval.add(rst);
							rst = null;
						}
					}
				}//end of while-loop statement
				rs.close();
				pstmt.close();
            //}
            conn.close();
			connD.close();
			
            rs = null;
            pstmt = null;
			rs2 = null;
			pstmt2 = null;
            conn = null;
            connD = null;
			//ecamsLogger.error(rsval.toString());					
			returnObjectArray = rsval.toArray();
			rsval = null;	
			
			return returnObjectArray;
			
		} catch (SQLException sqlexception) {
			sqlexception.printStackTrace();
			ecamsLogger.error("## Cmr0200.getReqList() SQLException START ##");
			ecamsLogger.error("## Error DESC : ", sqlexception);	
			ecamsLogger.error("## Cmr0200.getReqList() SQLException END ##");			
			throw sqlexception;
		} catch (Exception exception) {
			exception.printStackTrace();
			ecamsLogger.error("## Cmr0200.getReqList() Exception START ##");				
			ecamsLogger.error("## Error DESC : ", exception);	
			ecamsLogger.error("## Cmr0200.getReqList() Exception END ##");				
			throw exception;
		}finally{
			if (strQuery != null) 	strQuery = null;
			if (returnObjectArray != null)  returnObjectArray = null;
			if (rs != null)     try{rs.close();}catch (Exception ex){ex.printStackTrace();}
			if (pstmt != null)  try{pstmt.close();}catch (Exception ex2){ex2.printStackTrace();}
			if (rs2 != null)    try{rs2.close();}catch (Exception ex){ex.printStackTrace();}
			if (pstmt2 != null) try{pstmt2.close();}catch (Exception ex2){ex2.printStackTrace();}
			if (conn != null){
				try{
					ConnectionResource.release(conn);
				}catch(Exception ex3){
					ecamsLogger.error("## Cmr0200.getReqList() connection release exception ##");
					ex3.printStackTrace();
				}
			}
			if (connD != null){
				try{
					ConnectionResource.release(connD);
				}catch(Exception ex3){
					ecamsLogger.error("## Cmr0200.getReqList() connection release exception ##");
					ex3.printStackTrace();
				}
			}
		}
	}//end of getReqList() method statement
	
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
			strQuery.append("select a.cr_acptno, a.cr_syscd, substr(a.cr_acptno,5,2) as reqcd, b.cr_rsrcname, b.cr_qrycd, b.cr_itemid, d.cr_exename,a.cr_testdate,a.cr_etcsayu, \n");
			strQuery.append("		(select cm_sysinfo from cmm0030 where a.cr_syscd = cm_syscd) as cm_sysinfo 							\n");
			strQuery.append("		from cmr1000 a, cmr1010 b, cmm0036 c, cmr0020 d where a.cr_acptno = ? and a.cr_acptno = b.cr_acptno 			\n");
			strQuery.append("       and b.cr_baseitem = b.cr_itemid and b.cr_itemid = d.cr_itemid 																								\n");	
			strQuery.append("       and b.cr_syscd = c.cm_syscd and b.cr_rsrccd = c.cm_rsrccd and substr(c.cm_info,26,1) = '0'			\n");		
			pstmt = conn.prepareStatement(strQuery.toString());
			//pstmt =  new LoggableStatement(conn, strQuery.toString());		
			pstmt.setString(1, AcptNo);
			//ecamsLogger.error(((LoggableStatement)pstmt).getQueryString()); 
            rs = pstmt.executeQuery(); 
            rsval.clear();
           
            while(rs.next())
            {
            	rst = new HashMap<String,String>();
            	
            	//String ReqCd = rs.getString("cr_qrycd");
            
            	/* 2022.08.22 파라미터 map으로 변경
            	tmpObjs = getReqList(UserId, rs.getString("cr_syscd"), "04", "00", "0",
            			rs.getString("cr_exename"), "", "", rs.getString("cm_sysinfo"), "", false, true, false, "", rs.getString("cr_itemid"));
            	*/
            	HashMap<String, String> paramMap = new HashMap<String, String>();
            	paramMap.put("UserId",UserId);
            	paramMap.put("SysCd", rs.getString("cr_syscd"));
            	paramMap.put("SinCd", "04");
            	paramMap.put("ReqCd", "00");
            	paramMap.put("TstSw", "0");
            	paramMap.put("RsrcName", rs.getString("cr_exename"));
            	paramMap.put("DsnCd", "");
            	paramMap.put("DirPath", "");
            	paramMap.put("SysInfo", rs.getString("cm_sysinfo"));
            	paramMap.put("RsrcCd", "");
            	paramMap.put("UpLowSw", "false");
            	// 20251126 재적용시 다른 사람신청건이 list에 안보이는 현상 
            	paramMap.put("selfSw", "true");
            	//paramMap.put("selfSw", "false");
            	paramMap.put("LikeSw", "false");
            	paramMap.put("txtORDERInfo", "");
            	paramMap.put("itemid", rs.getString("cr_itemid"));
            	tmpObjs = getReqList(paramMap);
            	paramMap = null;
            	
            	for(int i=0;i<tmpObjs.length;i++){
            		
            		rst = (HashMap<String,String>)tmpObjs[i];
            	
            		
            		if(rst.get("cr_itemid").equals(rs.getString("cr_itemid"))){
            			strQuery.setLength(0);
                    	strQuery.append("select a.cr_devptime, b.cr_deploy, b.cr_aplydate, b.cr_testyn as testyn, b.cr_important as importancecode, a.cr_passok,a.cr_testdate,a.cr_etcsayu,a.cr_syscd, \n");
                    	strQuery.append("		b.cr_newglo as newgoodscode, b.cr_dealcode as dealcode, b.cr_editcon as sayu, decode(b.cr_testyn, '00','N','Y') as testynname, \n");
                    	strQuery.append("(select cm_codename from   cmm0020 where  cm_macode = 'IMPORTANT' and b.cr_important = cm_micode) as important,  \n");
                        strQuery.append("(select cm_codename from cmm0020 where cm_macode = 'NEW/GLO' and b.cr_newglo = cm_micode) as newgoods,		\n");
                        strQuery.append("(select cm_sysgb from cmm0030 where cm_syscd = ?) as cm_sysgb, b.cr_baseitem, b.cr_compdate,a.cr_reqsayu													\n");
                    	strQuery.append("		from cmr1000 a, cmr1010 b where a.cr_acptno = ? and a.cr_acptno = b.cr_acptno and b.cr_itemid = ?	\n");
                    	pstmt2 = conn.prepareStatement(strQuery.toString());
            			pstmt2 =  new LoggableStatement(conn, strQuery.toString());		
            			pstmt2.setString(1, rs.getString("cr_syscd"));
            			pstmt2.setString(2, AcptNo);
            			pstmt2.setString(3, rs.getString("cr_itemid"));
            			ecamsLogger.error(((LoggableStatement)pstmt2).getQueryString()); 
                        rs2 = pstmt2.executeQuery();
                        
                        if(rs2.next())
                        {
                        	rst.put("sysgb", rs2.getString("cm_sysgb"));
                        	rst.put("cm_sysgb", rs2.getString("cm_sysgb"));
                        	rst.put("cr_sysgb", rs2.getString("cm_sysgb"));
                        	rst.put("cr_syscd", rs2.getString("cr_syscd"));
                        	rst.put("cm_syscd", rs2.getString("cr_syscd"));
                        	rst.put("cr_devptime",rs2.getString("cr_devptime"));
                        	rst.put("cr_deploy", rs2.getString("cr_deploy"));
                        	rst.put("cr_aplydate", rs2.getString("cr_aplydate"));
                        	rst.put("testyn", rs2.getString("testyn"));
                        	rst.put("importancecode", rs2.getString("importancecode"));
                        	rst.put("newgoodscode", rs2.getString("newgoodscode"));
                        	rst.put("dealcode", rs2.getString("dealcode"));
                        	rst.put("sayu", rs2.getString("sayu"));
                        	rst.put("cr_sayu", rs2.getString("sayu"));
                        	rst.put("testynname", rs2.getString("testynname"));
                        	rst.put("importancecodecodename", rs2.getString ("important"));
                        	rst.put("newgoodscodecodename", rs2.getString("newgoods"));
                        	rst.put("newgoods", rs2.getString("newgoods"));
                        	rst.put("Deploy", rs2.getString("cr_passok"));
                        	rst.put("baseitem", rs2.getString("cr_baseitem"));
                        	rst.put("cr_testdate", rs2.getString("cr_testdate"));
                        	rst.put("cr_etcsayu", rs2.getString("cr_etcsayu"));
                        	rst.put("cr_compdate", rs2.getString("cr_compdate"));
                        	rst.put("AplyDate", rs2.getString("cr_aplydate"));
                        	rst.put("CompDate", rs2.getString("cr_compdate"));
                        	if(rs2.getString("cr_reqsayu") == null || rs2.getString("cr_reqsayu").equals("")){
                        		rst.put("cr_reqsayu", "0");
                        	}else{
                        		rst.put("cr_reqsayu", rs2.getString("cr_reqsayu"));
                        	}
                        }
                        
                        rs2.close();
                        pstmt2.close();
                        
        				rsval.add(rst);
        				rst = null;
            			break;
            		}
     
            	}
        	
            	
			}//end of while-loop statement
			
			rs.close();
			pstmt.close();
			conn.close();
			conn = null;
			pstmt = null;
			pstmt2 = null;
			rs = null;
			rs2 = null;
			
			rtObj =  rsval.toArray();
			//ecamsLogger.debug("+++ rsval ==="+rsval.toString());
			rsval = null;
			
			return rtObj;
			
			
		} catch (SQLException sqlexception) {
			sqlexception.printStackTrace();
			ecamsLogger.error("## Cmr0200.getReturnInfo() SQLException START ##");
			ecamsLogger.error("## Error DESC : ", sqlexception);	
			ecamsLogger.error("## Cmr0200.getReturnInfo() SQLException END ##");			
			throw sqlexception;
		} catch (Exception exception) {
			exception.printStackTrace();
			ecamsLogger.error("## Cmr0200.getReturnInfo() Exception START ##");				
			ecamsLogger.error("## Error DESC : ", exception);	
			ecamsLogger.error("## Cmr0200.getReturnInfo() Exception END ##");				
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
					ecamsLogger.error("## Cmr0200.getReturnInfo() connection release exception ##");
					ex3.printStackTrace();
				}
			}
		}
	}//end of getReturnInfo() method statement
	
	
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
            rs = null;
            pstmt = null;
            conn = null;
            
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
			if (rs != null)     try{rs.close();}catch (Exception ex){ex.printStackTrace();}
			if (pstmt != null)  try{pstmt.close();}catch (Exception ex2){ex2.printStackTrace();}
			if (conn != null){
				try{
					ConnectionResource.release(conn);
				}catch(Exception ex3){
					ecamsLogger.error("## BbsDAO.chk_Resouce() connection release exception ##");
					ex3.printStackTrace();
				}
			}
		}
	}

	public String confSelect(String SysCd,String ReqCd,String RsrcCd,String UserId,String QryCd) throws SQLException, Exception {
		Connection        conn        = null;
		PreparedStatement pstmt       = null;
		ResultSet         rs          = null;
		StringBuffer      strQuery    = new StringBuffer();
		ConnectionContext connectionContext = new ConnectionResource();
		String            retMsg      = "N";
		int               cnt         = 0;
		
		try {
			
			conn = connectionContext.getConnection();
			SysInfo sysinfo = new SysInfo();
			cnt = sysinfo.getTstSys_conn(SysCd,conn);
			boolean mkSw = false;
			boolean updownSw  = false;
			if (ReqCd.equals("04") && cnt > 0) {
			// 테스트서버가 있는 체크인요청은 SKIP	
			} else {
				cnt = 0;
				/*if (ReqCd.equals("01") || ReqCd.equals("02") || ReqCd.equals("03") || ReqCd.equals("04") || ReqCd.equals("11")) {
					strQuery.setLength(0); 
					strQuery.append("select distinct a.cm_jobcd                         \n");
					strQuery.append("  from cmm0060 a,cmm0036 c,cmm0040 b               \n");
					strQuery.append(" where a.cm_syscd=? and a.cm_reqcd=?               \n");
					strQuery.append("   and decode(a.cm_manid,'1','Y','N')=b.cm_manid   \n");
					strQuery.append("   and b.cm_userid=? and a.cm_gubun='1'            \n");
					strQuery.append("   and a.cm_syscd=c.cm_syscd                       \n");
					strQuery.append("   and instr(?,c.cm_rsrccd)>0                      \n");
					if (ReqCd.equals("01") || ReqCd.equals("02")) {
						strQuery.append("and (substr(c.cm_info,38,1)='1' or             \n");
						strQuery.append("     substr(c.cm_info,45,1)='1')               \n");
					} else {
						strQuery.append("and substr(c.cm_info,45,1)='1'                 \n");
					}
					
					pstmt = conn.prepareStatement(strQuery.toString());	
					pstmt = new LoggableStatement(conn,strQuery.toString());
		            pstmt.setString(1, SysCd);
		            pstmt.setString(2, ReqCd);
		            pstmt.setString(3, UserId); 
		            pstmt.setString(4, RsrcCd);  
		            ecamsLogger.error(((LoggableStatement)pstmt).getQueryString());  
		            rs = pstmt.executeQuery();
		            while (rs.next()) {
		            	++cnt;
	            		if (rs.getString("cm_jobcd").equals("SYSPDN") || rs.getString("cm_jobcd").equals("SYSPUP")) updownSw = true;
	            		else if (rs.getString("cm_jobcd").equals("SYSFMK")) mkSw = true;
		            }
		            rs.close();
		            pstmt.close();
		            if (cnt > 0) {
			            if (ReqCd.equals("01") || ReqCd.equals("02")) {
			            	if (updownSw == false || mkSw == false) {
			            		retMsg = "X";
			            	}
			            } else {
			            	if (updownSw == false) {
			            		retMsg = "X";
			            	}
			            }
		            }
				}*/
			}
			
			if (!retMsg.equals("X")) {
				cnt = 0;
				
				strQuery.setLength(0);
				strQuery.append("select a.cm_gubun,a.cm_rsrccd                           \n");
				strQuery.append("  from cmm0060 a,cmm0040 b                              \n");
				strQuery.append(" where a.cm_syscd=? and a.cm_reqcd=?                    \n");
				strQuery.append("   and decode(a.cm_manid,'1','Y','N')=b.cm_manid        \n");
				strQuery.append("   and b.cm_userid=?                                    \n");
				pstmt = conn.prepareStatement(strQuery.toString());	
				//pstmt = new LoggableStatement(conn,strQuery.toString());
	            pstmt.setString(1, SysCd);
	            pstmt.setString(2, ReqCd);
	            pstmt.setString(3, UserId);  
	            //ecamsLogger.error(((LoggableStatement)pstmt).getQueryString());  
	            rs = pstmt.executeQuery();
	            while (rs.next()){
	            	++cnt;
//	            	if (!rs.getString("cm_gubun").equals("1") && !rs.getString("cm_gubun").equals("2")) {
	            	if (!rs.getString("cm_gubun").equals("1") ) {
	            		if (rs.getString("cm_gubun").equals("C")) retMsg = "C";
	            		else {
		            		if (rs.getString("cm_rsrccd") != null) {
		            			String strRsrc[] = RsrcCd.split(",");
		            			
		            			for (int i = 0;strRsrc.length > i; i++) {
		            				if (rs.getString("cm_rsrccd").indexOf(strRsrc[i]) >= 0) {
		            					retMsg = "Y";
		            					break;
		            				}
		            			}            			
		            		} else {
		            			retMsg = "Y";
		            			break;
		            		}
	            		}
	            	}
	            }
	
//		        if (QryCd.equals("09")) {
//		        	retMsg = "N";
//		        }
		        if (cnt == 0) retMsg = "0";
	            rs.close();
	            pstmt.close();
			}
            conn.close();
            rs = null;
            pstmt = null;
            conn = null;
            
            return retMsg;
			
		} catch (SQLException sqlexception) {
			sqlexception.printStackTrace();
			ecamsLogger.error("## Cmr0200.confSelect() SQLException START ##");
			ecamsLogger.error("## Error DESC : ", sqlexception);	
			ecamsLogger.error("## Cmr0200.confSelect() SQLException END ##");			
			throw sqlexception;
		} catch (Exception exception) {
			exception.printStackTrace();
			ecamsLogger.error("## Cmr0200.confSelect() Exception START ##");				
			ecamsLogger.error("## Error DESC : ", exception);	
			ecamsLogger.error("## Cmr0200.confSelect() Exception END ##");				
			throw exception;
		}finally{
			if (strQuery != null) 	strQuery = null;
			if (rs != null)     try{rs.close();}catch (Exception ex){ex.printStackTrace();}
			if (pstmt != null)  try{pstmt.close();}catch (Exception ex2){ex2.printStackTrace();}
			if (conn != null){
				try{
					ConnectionResource.release(conn);
				}catch(Exception ex3){
					ecamsLogger.error("## Cmr0200.confSelect() connection release exception ##");
					ex3.printStackTrace();
				}
			}
		}
	}
	public Object[] dbioCheck(ArrayList<HashMap<String,String>> fileList) throws SQLException, Exception {

		Connection        conn        = null;
		PreparedStatement pstmt       = null;
		ConnectionContext connectionContext = new ConnectionResource();
		ResultSet         rs          = null;
		StringBuffer      strQuery    = new StringBuffer();
		Object[]		  rtObj		  = null;
		ArrayList<HashMap<String, String>>		  rtList	  = new ArrayList<HashMap<String, String>>();
		HashMap<String, String>			  rst		  = null;
		String            tableName = "";
		int               j = 0;
		try {
			conn = connectionContext.getConnection();
			
			rtList.clear();
			
			for (int i=0;i<fileList.size();i++){
				rst = new HashMap<String, String>();
				rst = fileList.get(i);
				rst.put("error", "N");
				rst.put("errmsg", "");
				//동일한 Table을 사용하는 DBIO 체크인신청여부 Check				
				if (fileList.get(i).get("cr_rsrccd").equals("12") ||
					fileList.get(i).get("cr_rsrccd").equals("46") ||
					fileList.get(i).get("cr_rsrccd").equals("47") ||
					fileList.get(i).get("cr_rsrccd").equals("48") ||
					fileList.get(i).get("cr_rsrccd").equals("49") ||
					fileList.get(i).get("cr_rsrccd").equals("50")) {
					tableName = fileList.get(i).get("cr_rsrcname");
					//substr('*',1,length('*')-7)
					j = tableName.lastIndexOf("_");
					if (j> 0) tableName = tableName.substring(0,j);
					else tableName = tableName.substring(0,tableName.length()-7);
					strQuery.setLength(0);
					strQuery.append("select c.cm_username,to_char(a.cr_acptdate,'yyyy-mm-dd') acptdate \n");
					strQuery.append("  from cmm0040 c,cmr1010 b,cmr1000 a           \n");
					strQuery.append(" where a.cr_status='0' and a.cr_qrycd='04'     \n");
					strQuery.append("   and a.cr_syscd=?                            \n");
					strQuery.append("   and a.cr_acptno=b.cr_acptno                 \n");
					strQuery.append("   and b.cr_status='0'                         \n");
					strQuery.append("   and b.cr_rsrccd in ('12','46','47','48','49','50') \n");
					strQuery.append("   and length(b.cr_rsrcname)=?                 \n");
					strQuery.append("   and b.cr_rsrcname like ?                    \n");
					strQuery.append("   and a.cr_editor=c.cm_userid                 \n");
					pstmt = conn.prepareStatement(strQuery.toString());
					//pstmt = new LoggableStatement(conn,strQuery.toString());
					pstmt.setString(1, fileList.get(i).get("cr_syscd"));
					pstmt.setInt(2, tableName.length()+7);
					pstmt.setString(3, tableName+"%");
					////ecamsLogger.error(((LoggableStatement)pstmt).getQueryString());
					rs = pstmt.executeQuery();
					if (rs.next()) {
						rst.put("error", "Y");
						rst.put("errmsg", "[Table : "+tableName+" 체크인 신청 중] 요청자:"+rs.getString("cm_username")+", 요청일:"+rs.getString("acptdate"));
					}
					rs.close();
					pstmt.close();
				} else if (fileList.get(i).get("cr_rsrccd").equals("20")) {
					//Table LayOut의 경우 DBIO에 대한 테스트적용요청이 없는 경우 오류처리
					String AcptDate = "";
					strQuery.setLength(0);
					if (fileList.get(i).get("reqcd").equals("03")) {
						strQuery.append("select to_char(cr_opendate,'yyyymmdd') acptdate \n");
						strQuery.append("  from cmr0020                                  \n");
						strQuery.append(" where cr_itemid=?                              \n");
						pstmt = conn.prepareStatement(strQuery.toString());
						//pstmt = new LoggableStatement(conn,strQuery.toString());
						pstmt.setString(1, fileList.get(i).get("cr_itemid"));
						////ecamsLogger.error(((LoggableStatement)pstmt).getQueryString());
						rs = pstmt.executeQuery();
						if (rs.next()) {
							AcptDate = rs.getString("acptdate");
						}
						rs.close();
						pstmt.close();
					} else {
						strQuery.append("select to_char(max(a.cr_acptdate),'yyyymmdd') acptdate \n");
						strQuery.append("  from cmr1010 b,cmr1000 a                      \n");
						strQuery.append(" where b.cr_itemid=? and b.cr_status<>'3'       \n");
						strQuery.append("   and b.cr_prcdate is not null                 \n");
						strQuery.append("   and b.cr_acptno=a.cr_acptno                  \n");
						strQuery.append("   and a.cr_qrycd='01'                          \n");
						pstmt = conn.prepareStatement(strQuery.toString());
						//pstmt = new LoggableStatement(conn,strQuery.toString());
						pstmt.setString(1, fileList.get(i).get("cr_itemid"));
						////ecamsLogger.error(((LoggableStatement)pstmt).getQueryString());
						rs = pstmt.executeQuery();
						if (rs.next()) {
							AcptDate = rs.getString("acptdate");
						}
						rs.close();
						pstmt.close();
					}
					
					tableName = fileList.get(i).get("cr_rsrcname");
					strQuery.setLength(0);
					strQuery.append("select count(*) as cnt                         \n");
					strQuery.append("  from cmr1010 b,cmr1000 a                     \n");
					strQuery.append(" where a.cr_qrycd='03' and a.cr_status<>'3'    \n");
					strQuery.append("   and a.cr_prcdate is not null                \n");
					strQuery.append("   and a.cr_syscd=?                            \n");
					strQuery.append("   and to_char(a.cr_acptdate,'yyyymmdd')>=?    \n");
					strQuery.append("   and a.cr_acptno=b.cr_acptno                 \n");
					strQuery.append("   and b.cr_rsrccd in ('12','46','47','48','49','50') \n");
					strQuery.append("   and length(b.cr_rsrcname)=?                 \n");
					strQuery.append("   and b.cr_rsrcname like ?                    \n");
					strQuery.append("   and b.cr_status<>'3'                        \n");
					pstmt = conn.prepareStatement(strQuery.toString());
					//pstmt = new LoggableStatement(conn,strQuery.toString());
					pstmt.setString(1, fileList.get(i).get("cr_syscd"));
					pstmt.setString(2, AcptDate);
					pstmt.setInt(3, tableName.length()+7);
					pstmt.setString(4, tableName+"%");
					////ecamsLogger.error(((LoggableStatement)pstmt).getQueryString());
					rs = pstmt.executeQuery();
					if (rs.next()) {
						if (rs.getInt("cnt") == 0) {
							rst.put("error", "Y");
							rst.put("errmsg", "[TableLayOut : " + tableName + "]에 대한 DBIO의  테스트적용요청기록이 없습니다.");
						}
					}
					rs.close();
					pstmt.close();
				}
				rtList.add(rst);
			}//end of while-loop statement

			conn.close();
			conn = null;
			
			rtObj =  rtList.toArray();
			rtList.clear();
			rtList = null;
			
			return rtObj;			
			
		} catch (SQLException sqlexception) {
			sqlexception.printStackTrace();
			ecamsLogger.error("## Cmr0200.dbioCheck() SQLException START ##");
			ecamsLogger.error("## Error DESC : ", sqlexception);	
			ecamsLogger.error("## Cmr0200.dbioCheck() SQLException END ##");			
			throw sqlexception;
		} catch (Exception exception) {
			exception.printStackTrace();
			ecamsLogger.error("## Cmr0200.dbioCheck() Exception START ##");				
			ecamsLogger.error("## Error DESC : ", exception);	
			ecamsLogger.error("## Cmr0200.dbioCheck() Exception END ##");				
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
					ecamsLogger.error("## Cmr0200.confSelect() connection release exception ##");
					ex3.printStackTrace();
				}
			}
		}
	}	
	public Object[] getDownFileList_save(ArrayList<HashMap<String,String>> fileList,HashMap<String,String> etcData) throws SQLException, Exception {
		eCAMSInfo         ecamsinfo   = new eCAMSInfo();
		StringBuffer      strQuery    = new StringBuffer();
		Object[]		  rtObj		  = null;
		ArrayList<HashMap<String, String>>		  rtList	  = new ArrayList<HashMap<String, String>>();

		String            strBinPath  = "";
		String            strTmpPath  = "";

		
		try {
			strBinPath = ecamsinfo.getFileInfo("14");
			if (strBinPath == "" || strBinPath == null)
				throw new Exception("관리자에게 연락하여 주시기 바랍니다. (형상관리환경설정 - 실행디렉토리)");
	
			strTmpPath = ecamsinfo.getFileInfo("99");
			if (strTmpPath == "" || strTmpPath == null) 
				throw new Exception("관리자에게 연락하여 주시기 바랍니다. (형상관리환경설정 - 실행디렉토리)");
			
			
			ecamsinfo = null;
			rtList.clear();
			
			ThreadPool pool = new ThreadPool(10);
			//ecamsLogger.error("+++++++++CHECK-IN LIST START+++");
			for (int i=0;i<fileList.size();i++){
				pool.assign(new Cmr0200_ThreadWorker(fileList.get(i),etcData,strBinPath,strTmpPath,rtList));
				Thread.sleep(100);
			}//end of while-loop statement

			pool.complete();
			
			for (int i=0;rtList.size()>i;i++) {
				for (int j=i+1;rtList.size()>j;j++) {
					if (rtList.get(i).get("cr_itemid").equals(rtList.get(j).get("cr_itemid"))){
						rtList.remove(j--);
					}
				}
			}
			rtObj =  rtList.toArray();
			rtList.clear();
			//ecamsLogger.error("+++++++++CHECK-IN LIST E N D+++");
			rtList = null;
			
			return rtObj;			
			
		} catch (SQLException sqlexception) {
			sqlexception.printStackTrace();
			ecamsLogger.error("## Cmr0200.getDownFileList() SQLException START ##");
			ecamsLogger.error("## Error DESC : ", sqlexception);	
			ecamsLogger.error("## Cmr0200.getDownFileList() SQLException END ##");			
			throw sqlexception;
		} catch (Exception exception) {
			exception.printStackTrace();
			ecamsLogger.error("## Cmr0200.getDownFileList() Exception START ##");				
			ecamsLogger.error("## Error DESC : ", exception);	
			ecamsLogger.error("## Cmr0200.getDownFileList() Exception END ##");				
			throw exception;
		}finally{
			if (strQuery != null) 	strQuery = null;
			if (rtObj != null)	rtObj = null;
		}
	}
	
	public Object[] getDownFileList(ArrayList<HashMap<String,String>> fileList,HashMap<String,String> etcData) throws SQLException, Exception {
		eCAMSInfo         ecamsinfo   = new eCAMSInfo();
		StringBuffer      strQuery    = new StringBuffer();
		Object[]		  rtObj		  = null;
		ArrayList<HashMap<String, String>>		  rtList	  = new ArrayList<HashMap<String, String>>();

		String            strBinPath  = "";
		String            strTmpPath  = "";
		Connection        conn        = null;
		PreparedStatement pstmt       = null;
		PreparedStatement pstmt2      = null;
		ConnectionContext connectionContext = new ConnectionResource();
		ResultSet         rs          = null;
		ResultSet         rs2         = null;
		HashMap<String, String>			  rst		  = null;
		HashMap<String, String>			  tmpObj      = null;
		int               reqCnt      = 0;
		int               addCnt      = 0;
		int               svCnt       = 0;
		boolean            ErrSw      = false;
		String            strErr      = "";
		String            strWork1    = null;
		String            strWork3    = null;
		String            strRsrcCd   = null;
		String            strRsrcName = null;
		
		ArrayList<HashMap<String, String>> svrList = new ArrayList<HashMap<String, String>>();
		
		int                 i = 0;
		int                 j = 0;
		int              parmCnt = 0;
		Cmd0100 cmd0100 = new Cmd0100();
		
		try {
			strBinPath = ecamsinfo.getFileInfo("14");
			if (strBinPath == "" || strBinPath == null)
				throw new Exception("관리자에게 연락하여 주시기 바랍니다. (형상관리환경설정 - 실행디렉토리)");
	
			strTmpPath = ecamsinfo.getFileInfo("99");
			if (strTmpPath == "" || strTmpPath == null) 
				throw new Exception("관리자에게 연락하여 주시기 바랍니다. (형상관리환경설정 - 실행디렉토리)");
			

			conn = connectionContext.getConnection();
			ecamsinfo = null;
			String strVolPath = "";
			String strDirPath = "";
			String strDevHome = "";
			String strItemId = "";
			String strDsnCd = "";
			String strInfo = "";
			
			SysInfo sysinfo = new SysInfo();
			if (sysinfo.getLocalYn(etcData.get("syscd"))) {
				strQuery.setLength(0);
				strQuery.append("select cd_devhome from cmd0200               \n");
				strQuery.append(" where cd_syscd=? and cd_userid=?            \n");
				pstmt = conn.prepareStatement(strQuery.toString());
				pstmt.setString(1, etcData.get("syscd"));
				pstmt.setString(2, etcData.get("userid"));
				rs = pstmt.executeQuery();
				if (rs.next()) {
					strDevHome = rs.getString("cd_devhome");
				}
				rs.close();
				pstmt.close();

				svrList = sysinfo.getHomePath_conn(etcData.get("syscd"), conn);
			}
			sysinfo = null;
			rtList.clear();
			for (i=0;fileList.size()>i;i++) {
				rst = new HashMap<String,String>();
				rst.put("cm_dirpath",fileList.get(i).get("cm_dirpath"));
				rst.put("cr_rsrcname",fileList.get(i).get("cr_rsrcname"));
				rst.put("cr_story",fileList.get(i).get("cr_story"));
				rst.put("cm_jobname", fileList.get(i).get("cm_jobname"));
				rst.put("jawon", fileList.get(i).get("jawon"));
				
				strQuery.setLength(0);
				strQuery.append("select cm_codename from cmm0020 where cm_macode='CHECKIN' and cm_micode=?  \n");
				pstmt = conn.prepareStatement(strQuery.toString());
				//pstmt =  new LoggableStatement(conn, strQuery.toString());
				
	            pstmt.setString(1, fileList.get(i).get("reqcd"));
	        
	            ////ecamsLogger.error(((LoggableStatement)pstmt).getQueryString());
	            rs = pstmt.executeQuery();
	            
	            if (rs.next()) {
	            	rst.put("checkin", rs.getString("cm_codename"));
	            }
	            rs.close();
	            pstmt.close();
	            
				//rst.put("checkin", etcData.get("QryName"));
				rst.put("prcseq", fileList.get(i).get("prcseq"));
				rst.put("cr_lstver",fileList.get(i).get("cr_lstver"));
				rst.put("cr_itemid",fileList.get(i).get("cr_itemid"));
				rst.put("sysgb", fileList.get(i).get("sysgb"));
				rst.put("cr_syscd", fileList.get(i).get("cr_syscd"));
				rst.put("cr_rsrccd",fileList.get(i).get("cr_rsrccd"));
				rst.put("cr_dsncd",fileList.get(i).get("cr_dsncd"));
				rst.put("cr_jobcd",fileList.get(i).get("cr_jobcd"));
				rst.put("baseitem",fileList.get(i).get("cr_itemid"));
				rst.put("cm_info",fileList.get(i).get("cm_info"));
				rst.put("cr_acptno",fileList.get(i).get("cr_acptno"));
				rst.put("cr_aftver",fileList.get(i).get("cr_aftver"));
				if (fileList.get(i).get("cr_sayu") != null && !"".equals(fileList.get(i).get("cr_sayu"))){ 
					rst.put("cr_sayu",fileList.get(i).get("cr_sayu"));
				}
				else{
					rst.put("cr_sayu",etcData.get("sayu"));	
				}
				rst.put("reqcd",fileList.get(i).get("reqcd"));
				rst.put("cr_baseno",fileList.get(i).get("cr_baseno"));
				rst.put("pcdir",fileList.get(i).get("pcdir"));
				rst.put("enable1","1");
				rst.put("selected","0");
				rst.put("cr_orderid", etcData.get("cc_orderid"));
				reqCnt = addCnt + 1;
				rst.put("seq", Integer.toString(reqCnt));
				rtList.add(addCnt++, rst);
				rst = null;
				svCnt = addCnt - 1;
				if (ErrSw == false && fileList.get(i).get("cm_info").substring(3,4).equals("1") && (etcData.get("ReqCD").equals("03") || etcData.get("TstSw").equals("0"))) {
					strQuery.setLength(0);
					strQuery.append("select b.cm_samename,b.cm_samersrc,b.cm_basedir,         \n");
					strQuery.append("       b.cm_samedir,b.cm_basename,b.cm_cmdyn,a.cm_info   \n");
					strQuery.append("  from cmm0036 a,cmm0037 b                               \n");
					strQuery.append(" where b.cm_syscd=? and b.cm_rsrccd=?                    \n");
					strQuery.append("   and b.cm_factcd='04' and substr(a.cm_info,24,1)='1'   \n");
					strQuery.append("   and b.cm_syscd=a.cm_syscd                             \n");
					strQuery.append("   and b.cm_samersrc=a.cm_rsrccd                         \n");
					//pstmt =  new LoggableStatement(conn, strQuery.toString());
					pstmt = conn.prepareStatement(strQuery.toString());	
					pstmt.setString(1, fileList.get(i).get("cr_syscd"));   	
			        pstmt.setString(2, fileList.get(i).get("cr_rsrccd"));   
		        
		            ////ecamsLogger.error(((LoggableStatement)pstmt).getQueryString());				             	     	
			        rs = pstmt.executeQuery();
			        
			        while (rs.next()) {
			            ErrSw = false;
			        	if (fileList.get(i).get("cr_rsrcname").indexOf(".") > -1) {	        		
			        		strWork1 = fileList.get(i).get("cr_rsrcname").substring(0,fileList.get(i).get("cr_rsrcname").indexOf("."));
			        	} else {
			        		strWork1 = fileList.get(i).get("cr_rsrcname");
			        	}
			        	//ecamsLogger.error("+++++++++++++++strWork1=========>"+strWork1);	
			        	if (rs.getString("cm_samename").indexOf("?#")>=0) {
			        		tmpObj = new HashMap<String,String>();
			        		tmpObj.put("cr_rsrcname",strWork1);
			        		tmpObj.put("cm_dirpath",fileList.get(i).get("cm_dirpath"));
			        		tmpObj.put("cm_samename",fileList.get(i).get("cm_samename"));
			        		
			        		strWork3 = nameChange(tmpObj,conn);
			        		if (strWork3.equals("ERROR")) {
			        			for (j=rtList.size()-1;j>=svCnt;j--) {
									rtList.remove(j);
								}
				            	strErr = "["+strRsrcName+"]에 대한 동시적용모듈정보가 정확하지 않습니다.";
								rst = new HashMap<String,String>();
								rst.put("cr_itemid","ERROR");
								rst.put("cm_dirpath",strErr);
								rst.put("cr_rsrcname",fileList.get(i).get("cr_rsrcname"));
								rtList.add(svCnt, rst); 
								rst = null;
								ErrSw = true;	
			        		}
			        	} else {
			        		strWork3 = rs.getString("cm_samename").replace("*", strWork1);
			        	}
			        	strDirPath = fileList.get(i).get("cm_dirpath");
			        	if (rs.getString("cm_basedir") != null) {
			        		if (!rs.getString("cm_basedir").equals("cm_samedir")){
			        			if (rs.getString("cm_basedir").equals("*")) strDirPath = rs.getString("cm_samedir");
			        			else {
			        				strDirPath = strDirPath.replace(rs.getString("cm_basedir"), rs.getString("cm_samedir"));
			        			}
			        		}
			        	} 
			        	if (ErrSw == false) {
				        	strRsrcCd = rs.getString("cm_samersrc");
				        	strInfo = rs.getString("cm_info");
				        	strItemId = "";
				        	strDsnCd = "";
				        	parmCnt = 0;
				        	strQuery.setLength(0);
							strQuery.append("select a.cr_itemid                     \n");
						   	strQuery.append("  from cmm0070 b,cmr0020 a             \n");
						   	strQuery.append(" where a.cr_syscd=? and a.cr_rsrccd=?  \n");
						   	strQuery.append("   and upper(a.cr_rsrcname)= upper(?)  \n");
						   	strQuery.append("   and upper(b.cm_dirpath)=upper(?)    \n");
						   	strQuery.append("   and a.cr_syscd=b.cm_syscd           \n");
						   	strQuery.append("   and a.cr_dsncd=b.cm_dsncd           \n");
						   pstmt2 = conn.prepareStatement(strQuery.toString());
		//					pstmt2 =  new LoggableStatement(conn, strQuery.toString());
				            pstmt2.setString(++parmCnt, fileList.get(i).get("cr_syscd"));
				            pstmt2.setString(++parmCnt, strRsrcCd);
						   	pstmt2.setString(++parmCnt,strWork3);
						   	pstmt2.setString(++parmCnt,strDirPath);				        
		//		            //ecamsLogger.error(((LoggableStatement)pstmt2).getQueryString());
				            rs2 = pstmt2.executeQuery();
		
				            if (rs2.next()) {
				            	strItemId = rs2.getString("cr_itemid");
				            }
				            rs2.close();
				            pstmt2.close();
				            
				            if (strItemId.length() == 0) {
				            	strQuery.setLength(0);
				            	strQuery.append("select cm_dsncd from cmm0070       \n");
				            	strQuery.append(" where cm_syscd=? and cm_dirpath=? \n");
				            	pstmt2 = conn.prepareStatement(strQuery.toString());
			//	            	pstmt2 =  new LoggableStatement(conn, strQuery.toString());
				            	pstmt2.setString(1, etcData.get("syscd"));
				            	pstmt2.setString(2,strDirPath);
			//	            	//ecamsLogger.error(((LoggableStatement)pstmt2).getQueryString());
				            	rs2 = pstmt2.executeQuery();
				            	if (rs2.next()) {
				            		strDsnCd  = rs2.getString("cm_dsncd");
				            	}
				            	rs2.close();
				            	pstmt2.close();
				            	
				            	if (strDsnCd.length() == 0) {
				            		strDsnCd = cmd0100.cmm0070_Insert(etcData.get("userid"), etcData.get("syscd"),"",strRsrcCd,fileList.get(i).get("cr_jobcd"),strDirPath,true,conn);	
				            		if (strDsnCd == null || "".equals(strDsnCd)) {
					        			for (j=rtList.size()-1;j>=svCnt;j--) {
											rtList.remove(j);
										}
						            	strErr = "["+strDirPath+"]에 대한 디렉토리등록에 실패하였습니다.";
										rst = new HashMap<String,String>();
										rst.put("cr_itemid","ERROR");
										rst.put("cm_dirpath",strErr);
										rst.put("cr_rsrcname",fileList.get(i).get("cr_rsrcname"));
										rtList.add(svCnt, rst); 
										rst = null;
										ErrSw = true;	
					        		}
				            		
				            	} else {
				            		strItemId = cmd0100.cmr0020_Insert(etcData.get("userid"), etcData.get("syscd"), etcData.get("cc_orderid"), strDsnCd, strWork3, strRsrcCd,
				            				fileList.get(i).get("cr_jobcd"), fileList.get(i).get("cr_story"), fileList.get(i).get("cr_itemid"), strInfo, conn, fileList.get(i).get("cr_langcd"),
				            				fileList.get(i).get("cr_compile"),fileList.get(i).get("cr_makecompile"), fileList.get(i).get("cr_teamcd"),fileList.get(i).get("cr_sqlcheck"), fileList.get(i).get("cr_document"),"","","","");
				            		if (!strItemId.substring(0,1).equals("0")) {
				            			for (j=rtList.size()-1;j>=svCnt;j--) {
											rtList.remove(j);
										}
						            	strErr = "["+strWork3+"]에 대한 프로그램등록에 실패하였습니다.";
										rst = new HashMap<String,String>();
										rst.put("cr_itemid","ERROR");
										rst.put("cm_dirpath",strErr);
										rst.put("cr_rsrcname",fileList.get(i).get("cr_rsrcname"));
										rtList.add(svCnt, rst); 
										rst = null;
										ErrSw = true;	
				            		} else {
				            			strItemId = strItemId.substring(1);
				            		}
				            	}
				            }
				            if (ErrSw == false) {
					        	parmCnt = 0;
					        	strQuery.setLength(0);
								strQuery.append("select a.cr_rsrccd,a.cr_rsrcname,a.cr_jobcd,a.cr_lstver,a.cr_itemid, \n");
								strQuery.append("       a.cr_langcd,a.cr_dsncd,b.cm_dirpath,e.CM_CODENAME as jawon,a.cr_story, \n");
								strQuery.append("       d.CM_INFO,lpad(d.cm_prcstep,4,'0') prcseq,         \n");
								strQuery.append("       nvl(d.cm_vercnt,50) vercnt,f.cm_codename checkin   \n");
							   	strQuery.append("  from cmm0070 b,cmr0020 a,cmm0036 d,cmm0020 e,cmm0020 f  \n");
							   	strQuery.append(" where a.cr_syscd=? and a.cr_rsrccd=?                     \n");
							   	strQuery.append("   and upper(a.cr_rsrcname)= upper(?)                     \n");
							   	strQuery.append("   and upper(b.cm_dirpath)=upper(?)                       \n");
							   	strQuery.append("   and a.cr_syscd=b.cm_syscd and a.cr_dsncd=b.cm_dsncd    \n");
							   	strQuery.append("   and a.cr_syscd=d.cm_syscd and a.cr_rsrccd=d.cm_rsrccd  \n");
								strQuery.append("   and e.cm_macode='JAWON' and e.cm_micode=a.cr_rsrccd    \n");
								strQuery.append("   and f.cm_macode='CHECKIN' and f.cm_micode=decode(a.cr_lstver,0,'03','04') \n");
							   //
								pstmt2 = conn.prepareStatement(strQuery.toString());
		//						pstmt2 =  new LoggableStatement(conn, strQuery.toString());
					            pstmt2.setString(++parmCnt, fileList.get(i).get("cr_syscd"));
					            pstmt2.setString(++parmCnt, strRsrcCd);
							   	pstmt2.setString(++parmCnt, strWork3);
							   	pstmt2.setString(++parmCnt, strDirPath);
		//			            //ecamsLogger.error(((LoggableStatement)pstmt2).getQueryString());
					            rs2 = pstmt2.executeQuery();
			
					            if (rs2.next()) {
					            	boolean fileSw = false;
					            	for (int k=0;rtList.size()>k;k++) {
					            		if (rtList.get(k).get("cr_itemid").equals(rs2.getString("cr_itemid"))) {
					            			fileSw = true;
					            		}
					            	}
					            	if (fileSw == false) {
						            	rst = new HashMap<String,String>();
						    			rst.put("cm_dirpath",rs2.getString("cm_dirpath"));
						    			rst.put("cr_rsrcname",rs2.getString("cr_rsrcname"));
						    			rst.put("cr_story",rs2.getString("cr_story"));
						    			rst.put("cm_jobname", fileList.get(i).get("cm_jobname"));
						    			rst.put("jawon", rs2.getString("jawon"));
						    			if (fileList.get(i).get("reqcd").equals("03") || fileList.get(i).get("reqcd").equals("04")){ 
						    				rst.put("checkin", rs2.getString("checkin"));
						    			}
						    			else{
						    				rst.put("checkin", fileList.get(i).get("checkin"));
						    			}
						    			rst.put("prcseq", rs2.getString("prcseq"));
						    			rst.put("cr_lstver",rs2.getString("cr_lstver"));
						    			rst.put("cr_itemid",rs2.getString("cr_itemid"));
						    			rst.put("sysgb", fileList.get(i).get("sysgb"));
						    			rst.put("cr_syscd", fileList.get(i).get("cr_syscd"));
						    			rst.put("cr_rsrccd",rs2.getString("cr_rsrccd"));
						    			rst.put("cr_langcd",rs2.getString("cr_langcd"));
						    			rst.put("cr_dsncd",rs2.getString("cr_dsncd"));
						    			rst.put("cr_jobcd",fileList.get(i).get("cr_jobcd"));
						    			rst.put("baseitem",fileList.get(i).get("cr_itemid"));
						    			if (fileList.get(i).get("cr_sayu") != null && !"".equals(fileList.get(i).get("cr_sayu"))){ 
						    				rst.put("cr_sayu",fileList.get(i).get("cr_sayu"));
						    			}
										else{
											rst.put("cr_sayu",etcData.get("sayu"));	
										}
						    			rst.put("cm_info",rs2.getString("cm_info"));
						    			if (fileList.get(i).get("reqcd").equals("03") || fileList.get(i).get("reqcd").equals("04")) {
						    				if (rs2.getInt("cr_lstver")== 0){
						    					rst.put("reqcd","03");
						    				}
						    				else{
						    					rst.put("reqcd","04");
						    				}
						    			}
						    			else{
						    				rst.put("reqcd",fileList.get(i).get("reqcd"));
						    			}
						    			rst.put("cr_acptno",fileList.get(i).get("cr_acptno"));
						    			if (rs2.getInt("cr_lstver") >= rs2.getInt("vercnt")) {
										   rst.put("cr_aftver", "1");	
										}
						    			else{
						    				rst.put("cr_aftver", Integer.toString(rs2.getInt("cr_lstver")+1));
						    			}
						    			rst.put("cr_baseno",fileList.get(i).get("cr_baseno"));
										rst.put("enable1","1");
										rst.put("selected","0");
										rst.put("cr_isrid", etcData.get("cc_isrid"));
										rst.put("cc_isrsub", etcData.get("cc_isrsub"));
										if (rs2.getString("cm_info").substring(44,45).equals("1") && strDevHome != null) {
											//rst.put("cm_dirpath", strDevHome + rs2.getString("cm_dirpath").replace("/", "\\"));									
											for (j=0 ; svrList.size()>j ; j++) {
												if (svrList.get(j).get("cm_rsrccd").equals(rs2.getString("cr_rsrccd"))) {
													strVolPath = svrList.get(j).get("cm_volpath");// /scmtst/hyjungtest/dev
													strDirPath = rs2.getString("cm_dirpath");// /scmtst/hyjungtest/dev/kicc_flex/src
													
													if (strVolPath != null && !"".equals(strVolPath)) {
														if (strDirPath.length()>=strVolPath.length() && strDirPath.substring(0,strVolPath.length()).equals(strVolPath)) {
															rst.put("pcdir", strDevHome + strDirPath.substring(strVolPath.length()).replace("/", "\\"));
														}else{
															rst.put("pcdir", strDevHome + rs2.getString("cm_dirpath").replace("/", "\\"));
														}
													} else {
														rst.put("pcdir", strDevHome + rs2.getString("cm_dirpath").replace("/", "\\"));
													}
													break;
												}
											}
										}
						    			reqCnt = addCnt + 1;
										rst.put("seq", Integer.toString(reqCnt));
						    			rtList.add(addCnt++, rst);
						    			rst = null;
					            	}
					            } 
					            else {
									for (j=rtList.size()-1;j>=svCnt;j--) {
										rtList.remove(j);
									}
					            	strErr = "["+strRsrcName+"]에 대한 프로그램정보를 찾을 수가 없습니다.";
									rst = new HashMap<String,String>();
									rst.put("cr_itemid","ERROR");
									rst.put("cm_dirpath",strErr);
									rst.put("cr_rsrcname",fileList.get(i).get("cr_rsrcname"));
									rtList.add(svCnt, rst); 
									rst = null;
									//ecamsLogger.error(strErr);
									
									ErrSw = true;								            	
					            }
					            pstmt2.close();
					            rs2.close();
				            }
			        	}
			        }
			        rs.close();
			        pstmt.close();
				}  
				if (ErrSw == false && fileList.get(i).get("cm_info").substring(8,9).equals("1") && (etcData.get("ReqCD").equals("03") || etcData.get("TstSw").equals("0"))) {       	
			        int readCnt = 0;	
				   	strQuery.setLength(0);
					strQuery.append("select a.cr_rsrccd,a.cr_rsrcname,a.cr_jobcd,a.cr_lstver,a.cr_itemid, \n");
					strQuery.append("       a.cr_langcd,a.cr_dsncd,b.cm_dirpath,e.CM_CODENAME as jawon,a.cr_story, \n");
					strQuery.append("       d.CM_INFO,lpad(d.cm_prcstep,4,'0') prcseq,                   \n");
					strQuery.append("       nvl(d.cm_vercnt,50) vercnt,f.cm_codename checkin             \n");
				   	strQuery.append("  from cmm0070 b,cmr0020 a,cmd0011 c,cmm0036 d,cmm0020 e,cmm0020 f  \n");
				   	strQuery.append(" where c.cd_itemid=?                                      \n");
				   	strQuery.append("   and c.cd_prcitem=a.cr_itemid                           \n");
				   	strQuery.append("   and a.cr_syscd=b.cm_syscd and a.cr_dsncd=b.cm_dsncd    \n");
				   	strQuery.append("   and a.cr_syscd=d.cm_syscd and a.cr_rsrccd=d.cm_rsrccd  \n");
				   	strQuery.append("   and substr(a.cm_info,24,1)='1'                         \n");
					strQuery.append("   and e.cm_macode='JAWON' and e.cm_micode=a.cr_rsrccd    \n"); 
					strQuery.append("   and f.cm_macode='CHECKIN' and f.cm_micode=decode(a.cr_lstver,0,'03','04') \n");
				   	//pstmt =  new LoggableStatement(conn, strQuery.toString());
					pstmt = conn.prepareStatement(strQuery.toString());
					pstmt.setString(1, fileList.get(i).get("cr_itemid"));
		        
		            ////ecamsLogger.error(((LoggableStatement)pstmt).getQueryString());
		            rs = pstmt.executeQuery();
	
		            while (rs.next()) {
		            	boolean fileSw = false;
		            	++readCnt;
		            	for (int k=0;rtList.size()>k;k++) {
		            		if (rtList.get(k).get("cr_itemid").equals(rs.getString("cr_itemid"))) {
		            			fileSw = true;
		            		}
		            	}
		            	if (fileSw == false) {
			            	if (rs.getString("cm_info").substring(0,1).equals("1") && rs.getString("cm_info").substring(6,7).equals("0")) {
			            		//strErr = bldcdChk(fileList.get(i).get("cr_syscd"),fileList.get(i).get("cr_jobcd"),fileList.get(i).get("cr_rsrccd"),fileList.get(i).get("cr_itemid"),InCd,BldCd,RelCd,conn);
			            		if (!"".equals(strErr)) {
									for (j=rtList.size()-1;j>=svCnt;j--) {
										rtList.remove(j);
									}
							    	rst = new HashMap<String,String>();
									rst.put("cm_dirpath",strErr);
									rst.put("cr_rsrcname",fileList.get(i).get("cr_rsrcname"));
									rtList.add(svCnt, rst); 
									ErrSw = true;
							    }
			            	}
			            	if (ErrSw == true) break;			            	
			            	
			            	rst = new HashMap<String,String>();
			    			rst.put("cm_dirpath",rs.getString("cm_dirpath"));
			    			rst.put("cr_rsrcname",rs.getString("cr_rsrcname"));
			    			if (rs.getString("cr_story") != null && !"".equals(rs.getString("cr_story")))
			    				rst.put("cr_story",rs.getString("cr_story"));
			    			rst.put("cm_jobname", fileList.get(i).get("cm_jobname"));
			    			rst.put("jawon", rs.getString("jawon"));
			    			if (fileList.get(i).get("reqcd").equals("03") || fileList.get(i).get("reqcd").equals("04")){ 
			    				rst.put("checkin", rs.getString("checkin"));
			    			}
			    			else{
			    				rst.put("checkin", fileList.get(i).get("checkin"));
			    			}
			    			rst.put("prcseq", rs.getString("prcseq"));
			    			rst.put("cr_lstver",rs.getString("cr_lstver"));
			    			rst.put("cr_itemid",rs.getString("cr_itemid"));
			    			rst.put("sysgb", fileList.get(i).get("sysgb"));
			    			rst.put("cr_syscd", fileList.get(i).get("cr_syscd"));
			    			rst.put("cr_rsrccd",rs.getString("cr_rsrccd"));
			    			rst.put("cr_langcd",rs.getString("cr_langcd"));
			    			rst.put("cr_dsncd",rs.getString("cr_dsncd"));
			    			rst.put("cr_jobcd",fileList.get(i).get("cr_jobcd"));
			    			rst.put("baseitem",fileList.get(i).get("cr_itemid"));
			    			rst.put("cm_info",rs.getString("cm_info"));
			    			rst.put("cr_acptno",fileList.get(i).get("cr_acptno"));
			    			if (fileList.get(i).get("cr_sayu") != null && !"".equals(fileList.get(i).get("cr_sayu"))){ 
			    				rst.put("cr_sayu",fileList.get(i).get("cr_sayu"));
			    			}
							else{
								rst.put("cr_sayu",etcData.get("sayu"));	
							}
			    			if (fileList.get(i).get("reqcd").equals("03") || fileList.get(i).get("reqcd").equals("04")) {
			    				if (rs.getInt("cr_lstver")== 0){
			    					rst.put("reqcd","03");
			    				}
			    				else{
			    					rst.put("reqcd","04");
			    				}
			    			}
			    			else{
			    				rst.put("reqcd",fileList.get(i).get("reqcd"));
			    			}
			    			if (rs.getInt("cr_lstver") >= rs.getInt("vercnt")) {
							   rst.put("cr_aftver", "1");	
							}
			    			else{
			    				rst.put("cr_aftver", Integer.toString(rs.getInt("cr_lstver")+1));
			    			}
			    			rst.put("cr_baseno",fileList.get(i).get("cr_baseno"));
							rst.put("enable1","1");
							rst.put("selected","0");
							rst.put("cr_isrid", etcData.get("cc_isrid"));
							rst.put("cc_isrsub", etcData.get("cc_isrsub"));
							if (rs.getString("cm_info").substring(44,45).equals("1") && strDevHome != null) {
								//rst.put("cm_dirpath", strDevHome + rs.getString("cm_dirpath").replace("/", "\\"));									
								for (j=0 ; svrList.size()>j ; j++) {
									if (svrList.get(j).get("cm_rsrccd").equals(rs.getString("cr_rsrccd"))) {
										strVolPath = svrList.get(j).get("cm_volpath");// /scmtst/hyjungtest/dev
										strDirPath = rs.getString("cm_dirpath");// /scmtst/hyjungtest/dev/kicc_flex/src
										
										if (strVolPath != null && !"".equals(strVolPath)) {
											if (strDirPath.length()>=strVolPath.length() && strDirPath.substring(0,strVolPath.length()).equals(strVolPath)) {
												rst.put("pcdir", strDevHome + strDirPath.substring(strVolPath.length()).replace("/", "\\"));
											}else{
												rst.put("pcdir", strDevHome + rs.getString("cm_dirpath").replace("/", "\\"));
											}
										} else {
											rst.put("pcdir", strDevHome + rs.getString("cm_dirpath").replace("/", "\\"));
										}
										break;
									}
								}
							}
			    			reqCnt = addCnt + 1;
							rst.put("seq", Integer.toString(reqCnt));
			    			rtList.add(addCnt++, rst);
			    			rst = null;
		            	}
		            }
		            pstmt.close();
		            rs.close();
		            
		            if (readCnt == 0) {
						for (j=rtList.size()-1;j>=svCnt;j--) {
							rtList.remove(j);
						}
	
						strErr = "["+strRsrcName+"]에 대한 실행모듈정보를 찾을 수가 없습니다.";
				    	rst = new HashMap<String,String>();
						rst.put("cr_itemid","ERROR");
						rst.put("cm_dirpath",strErr);
						rst.put("cr_rsrcname",strRsrcName);
						rtList.add(svCnt, rst);
						rst = null;
						//ecamsLogger.error(strErr);
						ErrSw = true;								            	
		            }
				}    // 실행모듈체크 처리 end
			}
			
			conn.close();
			conn = null;
			
			for (i=0;rtList.size()>i;i++) {
				for (j=i+1;rtList.size()>j;j++) {
					if (!rtList.get(i).get("cr_itemid").equals("ERROR")) {
						if (rtList.get(i).get("cr_itemid").equals(rtList.get(j).get("cr_itemid"))){
							rtList.remove(j--);
						}
					}
				}
			}
			//rtObj =  rtList.toArray();

			//ecamsLogger.error("+++++++++CHECK-IN LIST E N D+++");
			//rtList = null;
			
			return rtList.toArray();			
			
		} catch (SQLException sqlexception) {
			sqlexception.printStackTrace();
			ecamsLogger.error("## Cmr0200.getDownFileList() SQLException START ##");
			ecamsLogger.error("## Error DESC : ", sqlexception);	
			ecamsLogger.error("## Cmr0200.getDownFileList() SQLException END ##");			
			throw sqlexception;
		} catch (Exception exception) {
			exception.printStackTrace();
			ecamsLogger.error("## Cmr0200.getDownFileList() Exception START ##");				
			ecamsLogger.error("## Error DESC : ", exception);	
			ecamsLogger.error("## Cmr0200.getDownFileList() Exception END ##");				
			throw exception;
		}finally{
			if (strQuery != null) 	strQuery = null;
			if (rtObj != null)	rtObj = null;
			if (rtList != null) rtList = null;
			if (conn != null){
				try{
					ConnectionResource.release(conn);
				}catch(Exception ex3){
					ecamsLogger.error("## Cmr0200.confSelect() connection release exception ##");
					ex3.printStackTrace();
				}
			}
		}
	}
	public String request_Check_Bef(ArrayList<HashMap<String,String>> chkInList) throws SQLException, Exception {
		Connection        conn        = null;
		PreparedStatement pstmt       = null;
		PreparedStatement pstmt2      = null;
		PreparedStatement pstmt3      = null;
		ResultSet         rs          = null;
		ResultSet         rs2         = null;
		StringBuffer      strQuery    = new StringBuffer();
		ConnectionContext connectionContext = new ConnectionResource();
		int				  i=0;
		int               j = 0;
		String            strPath     = "";
		String            strRsrcName = "";
		String            strSysCd    = "";
		String            strErMsg    = "";

		try {		
			conn = connectionContext.getConnection();	
			ArrayList<HashMap<String, String>> svrList = new ArrayList<HashMap<String, String>>();
			SysInfo sysinfo = new SysInfo();
			svrList = sysinfo.getHomePath_Relat_conn(chkInList.get(i).get("cr_syscd"), conn);
			if (svrList.size() == 0) {
				strErMsg = "ERROR시스템별 홈디렉토리정보를 찾을 수가 없습니다. [관리자연락요망]";
			} else {
		        for (i=0;i<chkInList.size();i++){	
		        	if (chkInList.get(i).get("baseitem").equals(chkInList.get(i).get("cr_itemid"))) {		        		
		        		strPath = chkInList.get(i).get("cm_dirpath");
		        		strSysCd = svrList.get(0).get("basesys");
		        		for (j=0;svrList.size()>j;j++) {
		        			if (chkInList.get(i).get("cr_rsrccd").substring(0,2).equals(svrList.get(j).get("cm_rsrccd"))) {			        		
				        		if (!svrList.get(j).get("cm_volpath").equals(svrList.get(j).get("basehome"))) {
				        			if (strPath.substring(0,svrList.get(j).get("cm_volpath").length()).equals(svrList.get(j).get("cm_volpath"))) {
				        				strPath = svrList.get(j).get("basehome") + strPath.substring(svrList.get(j).get("cm_volpath").length());
				        			} 
				        		} else {
				        			strPath = chkInList.get(i).get("cm_dirpath");
				        		}
				        		break;
		        			}
			        	}
			        	if (strPath.length() == 0) {
			        		strErMsg = "ERROR : 프로그램유형에 대한 홈디렉토리정보가 부정확합니다. [" + chkInList.get(i).get("cr_rsrcname")+"]";
			        		break;
			        	} else {
			        		strRsrcName = chkInList.get(i).get("cr_rsrcname");
			        		strQuery.setLength(0);
				        	strQuery.append("select a.cr_status,a.cr_lstver          \n");
				        	strQuery.append("  from cmr0020 a,cmm0070 b              \n");
				        	strQuery.append(" where a.cr_syscd=? and a.cr_rsrcname=? \n");
				        	strQuery.append("   and a.cr_syscd=b.cm_syscd            \n");
				        	strQuery.append("   and a.cr_dsncd=b.cm_dsncd            \n");
				        	strQuery.append("   and b.cm_dirpath=?                   \n");
				        	pstmt = conn.prepareStatement(strQuery.toString());
				        	//pstmt =  new LoggableStatement(conn, strQuery.toString());
				        	pstmt.setString(1, strSysCd);
				        	pstmt.setString(2, strRsrcName);
				        	pstmt.setString(3, strPath);
				        	//ecamsLogger.error("[Cmr0200.request_Check_Bef] strSysCd : " + strSysCd + ", strRsrcName : " + strRsrcName + ", strPath : " + strPath);
				        	////ecamsLogger.error(((LoggableStatement)pstmt).getQueryString());
				        	rs = pstmt.executeQuery();
				        	if (rs.next()) {
				        		if (rs.getInt("cr_lstver")>0 && !rs.getString("cr_status").equals("5")) {
				        			if (strErMsg.length() > 0) strErMsg = strErMsg + ",";
				        			else strErMsg = "[" + svrList.get(0).get("cm_sysmsg") + "]에서 체크아웃하지 않은 파일이 존재합니다. \n"
				        			                + "계속 진행할까요? \n"
				        			                + "(";
				        			strErMsg = strErMsg + chkInList.get(i).get("cr_rsrcname");
				        		}
				        	}
				        	rs.close();
				        	pstmt.close();
			        	}
			        	
		        	}
		        }
			}
        	conn.close();
        	rs = null;
        	pstmt = null;
        	conn = null;
        	
        	if (strErMsg.length()==0) strErMsg = "OK";
        	else if (!strErMsg.substring(0,5).equals("ERROR")) {
        		strErMsg = strErMsg + ")";
        	}
        	//ecamsLogger.error("+++++++++Request E N D+++");
        	return strErMsg;
			
		} catch (SQLException sqlexception) {
			sqlexception.printStackTrace();
			ecamsLogger.error("## Cmr0200.request_Check_Bef() SQLException START ##");
			ecamsLogger.error("## Error DESC : ", sqlexception);	
			ecamsLogger.error("## Cmr0200.request_Check_Bef() SQLException END ##");			
			throw sqlexception;
		} catch (Exception exception) {
			exception.printStackTrace();
			ecamsLogger.error("## Cmr0200.request_Check_Bef() Exception START ##");				
			ecamsLogger.error("## Error DESC : ", exception);	
			ecamsLogger.error("## Cmr0200.request_Check_Bef() Exception END ##");				
			throw exception;
		}finally{
			if (strQuery != null) 	strQuery = null;
			if (rs != null)     try{rs.close();}catch (Exception ex){ex.printStackTrace();}
			if (pstmt != null)  try{pstmt.close();}catch (Exception ex2){ex2.printStackTrace();}
			if (rs2 != null)     try{rs2.close();}catch (Exception ex){ex.printStackTrace();}
			if (pstmt2 != null)  try{pstmt2.close();}catch (Exception ex2){ex2.printStackTrace();}
			if (pstmt3 != null)  try{pstmt3.close();}catch (Exception ex2){ex2.printStackTrace();}
			if (conn != null){
				try{					
					ConnectionResource.release(conn);
					conn = null;
				}catch(Exception ex3){
					ecamsLogger.error("## Cmr0200.request_Check_Bef() connection release exception ##");
					ex3.printStackTrace();
				}
			}
		}		
	}
	public String request_Check_In(ArrayList<HashMap<String,String>> chkInList,HashMap<String,String> etcData,ArrayList<HashMap<String,String>> befJob,ArrayList<HashMap<String,Object>> ConfList,ArrayList<HashMap<String,String>> TestList,ArrayList<HashMap<String,String>> OrderList,String confFg) throws SQLException, Exception {
		Connection        conn        = null;
		Connection        connD        = null;
		PreparedStatement pstmt       = null;
		PreparedStatement pstmt2      = null;
		PreparedStatement pstmt3      = null;
		PreparedStatement pstmt4      = null;
		ResultSet         rs          = null;
		ResultSet         rs2         = null;
		ResultSet         rs4         = null;
		ResultSet         rs5         = null;
		StringBuffer      strQuery    = new StringBuffer();
		ConnectionContext connectionContext = new ConnectionResource();
		ConnectionContext connectionContextD = new ConnectionResource(false,"D");
		int				  pstmtcount  = 1;
		AutoSeq			  autoseq	  = new AutoSeq();
		UserInfo		  userInfo	  = new UserInfo();
		String			  AcptNo	  = null;
		String			  AcptNoC	  = null;
		int				  i=0;
		int				  cnt=0;
		int				  vcnt=0;
		int				  countt=0;
		String 			  dir         = "";
		String 			  dsn         = "";
		String 			  AcptNo1         = null;
		String 			  bbb         = "";
		int 			  counttt = 1;
		
		
		/*
		CodeInfo		  codeInfo	  = new CodeInfo();
		boolean           InCd       = false;
		boolean           BldCd      = false;
		boolean           RelCd      = false;
		boolean           dbioSw     = false;
		HashMap<String,String>	rData = null;
		ArrayList<HashMap<String, String>>  chkInList = new ArrayList<HashMap<String, String>>();
		int               SeqNo       = 0;
		ArrayList<HashMap<String,Object>>	rData2 = null;
		*/

		try {
			//ecamsLogger.error("+++++++++CHECK-IN LIST Check START+++");
			/*
			chkInList = getDownFileList(reqList,etcData);
			if (chkInList.size() == 0) {
				AcptNo = "ERROR신청 중 오류가 발생하였습니다.";
				return AcptNo;
			}
			
			for (i=0;chkInList.size()>i;i++) {
				if (chkInList.get(i).get("cr_itemid").equals("ERROR")) {
					AcptNo = "ERROR" + chkInList.get(i).get("cm_dirpath");
					break;
				}
			}
			
			if (AcptNo != null) return AcptNo;
			*/
			conn = connectionContext.getConnection();
			connD = connectionContextD.getConnection();
		
			
			for (i=0;i<chkInList.size();i++){	
				AcptNo = bldcdChk(chkInList.get(i).get("cr_syscd"),chkInList.get(i).get("cr_jobcd"),chkInList.get(i).get("cr_rsrccd"),chkInList.get(i).get("cm_info"),etcData.get("ReqCD"),chkInList.get(i).get("reqcd"),chkInList.get(i).get("cr_itemid"),ConfList,conn);
				if (!"".equals(AcptNo) && AcptNo != null) AcptNo = "ERROR[" + chkInList.get(i).get("cr_rsrcname")+"]에 대하여 " + AcptNo;
				else AcptNo = null;
				
				if (AcptNo == null) {
		        	strQuery.setLength(0);
		        	strQuery.append("select a.cr_status,a.cr_editor,a.cr_nomodify,b.cm_codename,c.cm_info,a.cr_tmaxgrp, TESTSVR_RSRC_YN(a.cr_syscd, a.cr_rsrccd) TESTYN \n");
		        	strQuery.append("  from cmr0020 a,cmm0020 b,cmm0036 c                       \n");
		        	strQuery.append(" where a.cr_itemid = ?                                     \n");
		        	strQuery.append("   and b.cm_macode='CMR0020' and b.cm_micode=a.cr_status   \n");
		        	strQuery.append("   and a.cr_syscd=c.cm_syscd and a.cr_rsrccd=c.cm_rsrccd   \n");
		        	strQuery.append("   and (substr(c.cm_info,12,1)='1'                         \n");
		        	strQuery.append("    or substr(c.cm_info,2,1)='1')                          \n");
		        	
		        	pstmt = conn.prepareStatement(strQuery.toString());
		        	
		        	pstmt.setString(1, chkInList.get(i).get("cr_itemid"));
		        	
		        	rs = pstmt.executeQuery();
		        	if (rs.next()){
		        		if (etcData.get("ReqCD").equals("03") || (!rs.getString("TESTYN").equals("OK") && etcData.get("ReqCD").equals("04"))) {
							ecamsLogger.error("2. Cmr0200.request_Check_In AcptNo1  "+ AcptNo + " itemid: " +chkInList.get(i).get("cr_itemid") + " status: " + rs.getString("cr_status")  + " ReqCD: " + etcData.get("ReqCD").equals("03"));
		        			if (chkInList.get(i).get("reqcd").equals("03")) {
		        				if (chkInList.get(i).get("cr_itemid").equals(chkInList.get(i).get("baseitem"))) {
		        				   if (!rs.getString("cr_status").equals("3") && !rs.getString("cr_status").equals("B")) {
		        					   AcptNo = "ERROR["+ chkInList.get(i).get("cr_rsrcname") + "]\n 이미 요청가능한 상태가 아닙니다. [" + rs.getString("cm_codename") +"]\n\n화면을 새로고침 하시기 바랍니다.";
		        				   }
		        				} else {
		        					ecamsLogger.error("5. Cmr0200.request_Check_In AcptNo2  "+ AcptNo + " itemid: " +chkInList.get(i).get("cr_itemid") + " status: " + rs.getString("cr_status"));
		        				   if (!rs.getString("cr_status").equals("0") && !rs.getString("cr_status").equals("3") && !rs.getString("cr_status").equals("B")) {
		        					   AcptNo = "ERROR["+ chkInList.get(i).get("cr_rsrcname") + "]는 이미 요청가능한 상태가 아닙니다. [" + rs.getString("cm_codename") +"]";
		        				   }
		        				}
		        			} else if (chkInList.get(i).get("reqcd").equals("04")) {
								ecamsLogger.error("2. Cmr0200.request_Check_In AcptNo2  "+ AcptNo + " itemid: " +chkInList.get(i).get("cr_itemid") + " status: " + rs.getString("cr_status"));
		        				if (chkInList.get(i).get("reqcd").equals("05") && rs.getString("cm_info").substring(17,18).equals("0")) {
		        				   if (!rs.getString("cr_status").equals("0")) {
		        					   AcptNo = "ERROR["+ chkInList.get(i).get("cr_rsrcname") + "]는 이미 요청가능한 상태가 아닙니다. [" + rs.getString("cm_codename") +"]";
		        				   }
		        				} else {
			        				if (chkInList.get(i).get("cr_itemid").equals(chkInList.get(i).get("baseitem"))) {
			        				   if (!rs.getString("cr_status").equals("5") && !rs.getString("cr_status").equals("B")) {
			        					   AcptNo = "ERROR["+ chkInList.get(i).get("cr_rsrcname") + "]는 이미 요청가능한 상태가 아닙니다. [" + rs.getString("cm_codename") +"]";
			        				   }
			        				} else {
			        				   if (!rs.getString("cr_status").equals("0") && !rs.getString("cr_status").equals("3") && !rs.getString("cr_status").equals("5") && !rs.getString("cr_status").equals("B")) {
			        					   AcptNo = "ERROR["+ chkInList.get(i).get("cr_rsrcname") + "]는 이미 요청가능한 상태가 아닙니다. [" + rs.getString("cm_codename") +"]";
			        				   }
			        				}
		        				}
		        			} else if (chkInList.get(i).get("reqcd").equals("09")) {
	        				   if (!rs.getString("cr_status").equals("0") && !rs.getString("cr_status").equals("5")&& !rs.getString("cr_status").equals("B")) {
	        					   AcptNo = "ERROR["+ chkInList.get(i).get("cr_rsrcname") + "]는 요청가능한 상태가 아닙니다. [" + rs.getString("cm_codename") +"]";
	        				   }
	        				   if (rs.getString("cr_status").equals("B") && rs.getString("cr_nomodify").equals("0")) {
	        					   AcptNo = "ERROR["+ chkInList.get(i).get("cr_rsrcname") + "]는 테스트적용요청 중인 상태입니다 . ";
	        				   } else if (rs.getString("cr_status").equals("0") && !rs.getString("cr_nomodify").equals("0")) {
	        					   AcptNo = "ERROR["+ chkInList.get(i).get("cr_rsrcname") + "]는 현재 무수정요청 중인 상태입니다 . ";
	        				   }
			        		} else if (chkInList.get(i).get("reqcd").equals("05")) {
		        				   if (!rs.getString("cr_status").equals("0") && !rs.getString("cr_status").equals("B")) {
		        					   AcptNo = "ERROR["+ chkInList.get(i).get("cr_rsrcname") + "]는 요청가능한 상태가 아닙니다. [" + rs.getString("cm_codename") +"]";
		        				   }
				        		}
		        		} else if (etcData.get("ReqCD").equals("06")) {
		        			if (!rs.getString("cr_status").equals("0")) {
	        				   AcptNo = "ERROR["+ chkInList.get(i).get("cr_rsrcname") + "]는 이미 요청가능한 상태가 아닙니다 . [" + rs.getString("cm_codename") +"]";
	        				}
		        		} else {
		        			if (etcData.get("ReqCD").equals("04") && chkInList.get(i).get("reqcd").equals("05") && rs.getString("cm_info").substring(17,18).equals("0")) {
								ecamsLogger.error("3. Cmr0200.request_Check_In AcptNo2  "+ AcptNo + " itemid: " +chkInList.get(i).get("cr_itemid") + " status: " + rs.getString("cr_status"));
		        				
	        				   if (!rs.getString("cr_status").equals("0")) {
	        					   AcptNo = "ERROR["+ chkInList.get(i).get("cr_rsrcname") + "]는 이미 요청가능한 상태가 아닙니다 . [" + rs.getString("cm_codename") +"]";
	        				   }
		        			} else {
			        			if (chkInList.get(i).get("reqcd").equals("03") || chkInList.get(i).get("reqcd").equals("04") || chkInList.get(i).get("reqcd").equals("05")) {
		        				   if (!rs.getString("cr_status").equals("B")) {
		        					   //%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
		        					   //AcptNo = "ERROR["+ chkInList.get(i).get("cr_rsrcname") + "]는 이미 요청가능한 상태가 아닙니다 . [" + rs.getString("cm_codename") +"]";
		        				   } 	        				
			        			} else {
			        			   if (!rs.getString("TESTYN").equals("OK") && !rs.getString("cr_nomodify").equals("0")) {
		        					   AcptNo = "ERROR["+ chkInList.get(i).get("cr_rsrcname") + "]는 이미 요청가능한 상태가 아닙니다 . [" + rs.getString("cm_codename") +"]";
		        				   } else if (etcData.get("TstSw").equals("1") && !rs.getString("cr_nomodify").equals("1")) {
		        					   AcptNo = "ERROR["+ chkInList.get(i).get("cr_rsrcname") + "]는 이미 요청가능한 상태가 아닙니다 . [" + rs.getString("cm_codename") +"]";
		        				   }  
			        			}
		        			}
		        		}
		        		
		        		if (chkInList.get(i).get("cr_syscd").equals("00201") && chkInList.get(i).get("cm_info").substring(26,27).equals("1") 
		        				&& !chkInList.get(i).get("cr_rsrccd").substring(0,2).equals("34") && rs.getString("cr_tmaxgrp") == null) {
		        			//ecamsLogger.error("+++++ Tmax Group Update +++++++++");
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
		    				pstmt2 = connPfm.prepareStatement(strQuery.toString());
		    				pstmt2.setString(1, chkInList.get(i).get("cr_rsrcname"));
		    				rs2 = pstmt2.executeQuery();
		    				if (rs2.next()) {
		    					strQuery.setLength(0);
		    					strQuery.append("update cmr0020 set cr_tmaxgrp=?  \n");
		    					strQuery.append(" where cr_itemid=?               \n");
		    					pstmt3 = conn.prepareStatement(strQuery.toString());
			    				pstmt3.setString(1, rs2.getString("RESOURCE_GROUP"));
			    				pstmt3.setString(2, chkInList.get(i).get("cr_itemid"));
			    				pstmt3.executeUpdate();
		    				} else {
		    					AcptNo = "ERROR["+ chkInList.get(i).get("cr_rsrcname") + "]에 대하여 프로프레임 그룹코드 추출에 실패하였습니다.";
		    				}
		    				rs2.close();
		    				pstmt2.close();
		    				connPfm.close();
		    				
		    				connPfm = null;
		    				
		    			}
		        	}
		        	rs.close();
		        	pstmt.close();
	        	}
	        }
			
			if (AcptNo != null) {
				if (conn != null) conn.close();
				if (connD != null) connD.close();
				return AcptNo;
			}
			
			String strTeam = userInfo.getUserInfo_sub(conn,etcData.get("UserID"),"cm_project");
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
			//ArrayList<HashMap<String,Object>> conflist = null;
			int wkC = chkInList.size()/3000;
			int wkD = chkInList.size()%3000;
			if (wkD>0) wkC = wkC + 1;
			String svAcpt[] = null; 
			svAcpt = new String [wkC];
			int maxseq = 0;
			strQuery.setLength(0);                		
			strQuery.append("select max(cc_seq) as max	 	\n");               		
			strQuery.append("  from cmc0440 												\n");  
			pstmt = conn.prepareStatement(strQuery.toString());	
			rs = pstmt.executeQuery();
			if (rs.next()) {
				maxseq = rs.getInt("max");
			}
			rs.close();
			pstmt.close();
			
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
			for(int c =0;c<svAcpt.length;c++){
				AcptNoC=svAcpt[c];
				for(int v=0;v<TestList.size();v++){
					pstmtcount = 1;
					
					strQuery.setLength(0);
					strQuery.append("insert into cmc0440 \n");
					strQuery.append("(CC_ACPTNO,CC_SEQ,CC_PROGGBN,CC_SUBJECT,CC_SUBCD,CC_SUBSRC,CC_ETC,CC_GBN,CC_CONFSRC	\n");
					strQuery.append(") values ( 																\n");
					strQuery.append("?,?,?,?,?,?,?,?,?)									 						\n");
					pstmt = conn.prepareStatement(strQuery.toString());
					//pstmt = new LoggableStatement(conn,strQuery.toString());
					pstmt.setString(pstmtcount++, AcptNoC);
					pstmt.setInt(pstmtcount++, ++maxseq);
					pstmt.setString(pstmtcount++, TestList.get(v).get("cc_proggbn"));
					pstmt.setString(pstmtcount++, TestList.get(v).get("cc_subject"));
					pstmt.setString(pstmtcount++, TestList.get(v).get("cc_subcd"));
					pstmt.setString(pstmtcount++, TestList.get(v).get("cc_subsrc"));
					pstmt.setString(pstmtcount++, TestList.get(v).get("cc_bigo"));
					pstmt.setString(pstmtcount++, TestList.get(v).get("cc_gbn"));
					pstmt.setString(pstmtcount++, TestList.get(v).get("cc_confsrc"));
					
					//ecamsLogger.error(((LoggableStatement)pstmt).getQueryString());
					pstmt.executeUpdate();
					
					pstmt.close();
				}
			}
			
			int    seq = 0;
			int    j   = 0;
			String retMsg = "";
			autoseq = null;
			conn.setAutoCommit(false);			
			boolean insSw = false;
			String strBasePgm = "";
			String strBase2[] = null;
			for (i=0;i<chkInList.size();i++){
				insSw = false;
				if (i == 0) insSw = true;
				else {
					wkC = i%3000;
					if (wkC == 0) insSw = true;
				}
				if (insSw == true) {        			
					if (i>=3000) {
						if (etcData.get("ReqCD").equals("04") && etcData.get("TstSw").equals("1")) {
							strQuery.setLength(0);
							strQuery.append("insert into cmr1010 (CR_ACPTNO,CR_SERNO,CR_SYSCD,CR_SYSGB,CR_JOBCD,   							\n");
							strQuery.append("   CR_STATUS,CR_QRYCD,CR_RSRCCD,CR_DSNCD,CR_DSNCD2,CR_RSRCNAME,       							\n");
							strQuery.append("   CR_RSRCNAM2,CR_SRCCHG,CR_SRCCMP,CR_PRIORITY,CR_APLYDATE,           							\n");
							strQuery.append("   CR_VERSION,CR_BEFVER,CR_CONFNO,CR_EDITOR,CR_BASENO,CR_BASEITEM,    							\n");
							strQuery.append("   CR_ITEMID,CR_EDITCON,CR_BASEPGM,CR_IMPORTANT,CR_NEWGLO,CR_DEALCODE)							\n");
							strQuery.append(" (select a.cr_acptno,                                                							\n");
							strQuery.append("    (select max(cr_serno) from cmr1010 where cr_acptno=?)+rownum,     							\n");
							strQuery.append("    a.cr_syscd,a.CR_SYSGB,b.CR_JOBCD,'0',b.CR_QRYCD,b.CR_RSRCCD,     							\n");
							strQuery.append("    b.CR_DSNCD,b.CR_DSNCD2,b.CR_RSRCNAME,b.CR_RSRCNAM2,b.CR_SRCCHG,   							\n");
							strQuery.append("    'Y',b.CR_PRIORITY,a.CR_APLYDATE,b.CR_VERSION,b.CR_BEFVER,         							\n");
							strQuery.append("    a.CR_CONFNO,a.CR_EDITOR,a.CR_BASENO,a.CR_BASEITEM,b.CR_ITEMID,    							\n"); 
							strQuery.append("    a.CR_EDITCON,b.CR_BASEPGM,b.CR_IMPORTANT,b.CR_NEWGLO,b.CR_DEALCODE							\n");    
							strQuery.append("    from cmr1010 a,cmr1010 b                                          							\n");
							strQuery.append("  where a.cr_acptno=? and a.cr_itemid=a.cr_baseitem                   							\n"); 
							strQuery.append("    and a.cr_confno=b.cr_acptno and b.cr_status<>'3'                  							\n");
							strQuery.append("    and instr(nvl(b.cr_basepgm,b.cr_baseitem),a.cr_itemid)>0          							\n");
							strQuery.append("    and a.cr_itemid<>b.cr_itemid and a.cr_status<>'3')                							\n");  
							pstmt = conn.prepareStatement(strQuery.toString());
							pstmtcount = 1;
							pstmt.setString(pstmtcount++, AcptNo);
							pstmt.setString(pstmtcount++, AcptNo);
							
							pstmt.executeUpdate();
							pstmt.close();
							
							strQuery.setLength(0);
							strQuery.append("select a.cr_basepgm,a.cr_serno    \n");
							strQuery.append("  from cmr1010 a                  \n");
							strQuery.append(" where a.cr_acptno=?              \n");
							strQuery.append("   and a.cr_basepgm is not null   \n");
							strQuery.append("   and a.cr_itemid<>a.cr_baseitem \n");
							strQuery.append("   and a.cr_baseitem<>a.cr_basepgm \n");
							pstmt = conn.prepareStatement(strQuery.toString());
							pstmt.setString(1, AcptNo);
							rs = pstmt.executeQuery();
							while (rs.next()) {
								strBase2 = rs.getString("cr_basepgm").split(","); 
								strBasePgm = "";
								for (j=0;strBase2.length>j;j++) {
									strQuery.setLength(0);
									strQuery.append("select count(*) cnt from cmr1010 \n");
									strQuery.append(" where cr_acptno=?               \n");
									strQuery.append("   and cr_itemid=?               \n");
									pstmt2 = conn.prepareStatement(strQuery.toString());
									pstmt2.setString(1, AcptNo);
									pstmt2.setString(2, strBase2[j]);
									rs2 = pstmt2.executeQuery();
									if (rs2.next()) {
										if (rs2.getInt("cnt")>0) {
											if (strBasePgm.length()>0) strBasePgm = strBasePgm + ",";
											strBasePgm = strBasePgm + strBase2[j];
										}
									}
									rs2.close();
									pstmt2.close();
								}
								
								if (!rs.getString("cr_basepgm").equals(strBasePgm)) {
									strQuery.setLength(0);
									strQuery.append("update cmr1010 set cr_basepgm=?  \n");
									strQuery.append(" where cr_acptno=?               \n");
									strQuery.append("   and cr_serno=?                \n");
									pstmt2 = conn.prepareStatement(strQuery.toString());
									pstmt2 = new LoggableStatement(conn,strQuery.toString());	
									pstmt2.setString(1, strBasePgm);
									pstmt2.setString(2, AcptNo);
									pstmt2.setString(3, rs.getString("cr_serno"));
									ecamsLogger.error(((LoggableStatement)pstmt2).getQueryString());
									pstmt2.executeUpdate();
									pstmt2.close();
								}
								
							}
							rs.close();
							pstmt.close();
							
							strQuery.setLength(0);
							strQuery.append("select cr_itemid                          \n");
							strQuery.append("  from cmr1010 where cr_acptno=?          \n");
							strQuery.append(" group by cr_itemid                       \n");
							strQuery.append(" having count(*)>1                        \n");
							pstmt = conn.prepareStatement(strQuery.toString());
							pstmt.setString(1, AcptNo);
							rs = pstmt.executeQuery();
							while (rs.next()) {
								strBasePgm = "";
								strQuery.setLength(0);
								strQuery.append("select cr_baseitem,cr_serno   \n");
								strQuery.append("  from cmr1010                \n");
								strQuery.append(" where cr_acptno=?            \n");
								strQuery.append("   and cr_itemid=?            \n");
								strQuery.append("   and cr_itemid<>cr_baseitem \n");
								strQuery.append(" order by cr_serno            \n");
								pstmt2 = conn.prepareStatement(strQuery.toString());
								pstmt2.setString(1, AcptNo);
								pstmt2.setString(2, rs.getString("cr_itemid"));
								rs2 = pstmt2.executeQuery();
								while (rs2.next()) {            	            		
									if (strBasePgm.length() > 0) strBasePgm = strBasePgm + ",";
									strBasePgm = strBasePgm + rs2.getString("cr_baseitem");
									
									if (rs2.getRow()>1) {
										strQuery.setLength(0);
										strQuery.append("delete cmr1010      \n");
										strQuery.append(" where cr_acptno=?  \n");
										strQuery.append("   and cr_serno=?   \n");
										pstmt3 = conn.prepareStatement(strQuery.toString());
										pstmt3.setString(1, AcptNo);
										pstmt3.setString(2, rs2.getString("cr_serno"));
										pstmt3.executeUpdate();
										pstmt3.close();
									}
								}
								rs2.close();
								pstmt2.close();
								
								if (strBasePgm.length()>0) {
									strQuery.setLength(0);
									strQuery.append("update cmr1010 set cr_basepgm=?        \n");
									strQuery.append(" where cr_acptno=?                     \n");
									strQuery.append("   and cr_itemid=?                     \n");
									pstmt2 = conn.prepareStatement(strQuery.toString());
									pstmt2 = new LoggableStatement(conn,strQuery.toString());
									pstmt2.setString(1, strBasePgm);
									pstmt2.setString(2, AcptNo);
									pstmt2.setString(3, rs.getString("cr_itemid"));
									ecamsLogger.error(((LoggableStatement)pstmt2).getQueryString());
									pstmt2.executeUpdate();
									pstmt2.close();
								} else {
									strQuery.setLength(0);
									strQuery.append("delete cmr1010 where cr_acptno=?      \n");
									strQuery.append("   and cr_itemid=?                    \n");
									strQuery.append("   and cr_serno not in (select cr_serno from cmr1010 \n");
									strQuery.append("                         where cr_acptno=?           \n");
									strQuery.append("                           and cr_itemid=?           \n");
									strQuery.append("                           and rownum<2)             \n");
									pstmt2 = conn.prepareStatement(strQuery.toString());
									pstmt2.setString(1, AcptNo);
									pstmt2.setString(2, rs.getString("cr_itemid"));
									pstmt2.setString(3, AcptNo);
									pstmt2.setString(4, rs.getString("cr_itemid"));
									pstmt2.executeUpdate();
									pstmt2.close();
								}
							}
							rs.close();
							pstmt.close();
						}
						
						retMsg = cmr0200.request_Confirm(AcptNo,chkInList.get(0).get("cr_syscd"),etcData.get("ReqCD"),etcData.get("UserID"),true,ConfList,conn);
						if (!retMsg.equals("OK")) {
							conn.rollback();
							conn.close();
							throw new Exception("결재정보등록 중 오류가 발생하였습니다. 관리자에게 연락하여 주시기 바랍니다.");
						}
					}
					
					wkC = i/3000;
					AcptNo = svAcpt[wkC];
					//ecamsLogger.error("++++ i, wkC ++++++"+ Integer.toString(i)+", "+ Integer.toString(wkC));
					pstmtcount = 1;
					strQuery.setLength(0);
					strQuery.append("insert into cmr1000 \n");
					strQuery.append("(CR_ACPTNO,CR_SYSCD,CR_SYSGB,CR_JOBCD,CR_ACPTDATE,CR_STATUS,CR_TEAMCD,CR_QRYCD, \n");
					strQuery.append("CR_PASSOK,CR_PASSCD,CR_BEFJOB,CR_EMGCD,CR_EDITOR,CR_SAYU,CR_PASSSUB,CR_SAYUCD,  \n");
					strQuery.append("CR_BASEUP,CR_DEVPTIME,CR_TESTDATE,CR_ETCSAYU,CR_REQSAYU) values ( \n");
					strQuery.append("?,?,?,?,sysdate,'0',?,?,  ?,?,?,?,?,?,?,?,  ?,?,?,?,?) \n");
					
					pstmt = conn.prepareStatement(strQuery.toString());
					//pstmt = new LoggableStatement(conn,strQuery.toString());
					pstmt.setString(pstmtcount++, AcptNo);
					pstmt.setString(pstmtcount++, chkInList.get(0).get("cr_syscd"));
					pstmt.setString(pstmtcount++, chkInList.get(0).get("sysgb"));
					pstmt.setString(pstmtcount++, chkInList.get(0).get("cr_jobcd"));
					pstmt.setString(pstmtcount++, strTeam);
					pstmt.setString(pstmtcount++, etcData.get("ReqCD"));
					
					
					pstmt.setString(pstmtcount++, etcData.get("Deploy"));
					pstmt.setString(pstmtcount++, strRequest);
					if (befJob.size() > 0) pstmt.setString(pstmtcount++, "Y");
					else pstmt.setString(pstmtcount++, "N");
					pstmt.setString(pstmtcount++, etcData.get("ReqSayu"));
					pstmt.setString(pstmtcount++, etcData.get("UserID"));
					pstmt.setString(pstmtcount++, chkInList.get(0).get("cr_sayu"));
					pstmt.setString(pstmtcount++, etcData.get("EmgCd"));
					pstmt.setString(pstmtcount++, etcData.get("PassCd"));
					
					
					//pstmt.setString(pstmtcount++, etcData.get("DocNo"));
					pstmt.setString(pstmtcount++, etcData.get("upload"));
					//if (etcData.get("ReqCD").equals("03") || !etcData.get("Deploy").equals("4")) {
//                	if (etcData.get("Deploy").equals("4")) {
//                		pstmt.setString(pstmtcount++,etcData.get("AplyDate"));
//                	} else {
//                		pstmt.setString(pstmtcount++,"");
//                	}
					pstmt.setString(pstmtcount++, etcData.get("cr_devptime"));
					if(etcData.get("etcSayu")!=null && !etcData.get("etcSayu").equals("")){
						pstmt.setString(pstmtcount++, etcData.get("testdate"));
						pstmt.setString(pstmtcount++, etcData.get("etcSayu"));
						
					}else{
						pstmt.setString(pstmtcount++, "");
						pstmt.setString(pstmtcount++, "");
						
					}
					pstmt.setString(pstmtcount++, etcData.get("reqsayu"));	//reqsayu 추가 131105
					//ecamsLogger.error(((LoggableStatement)pstmt).getQueryString());
					pstmt.executeUpdate();
					
					pstmt.close();
					seq = 0;
					
					if(chkInList.get(0).get("cr_syscd").equals("00520") ||
					   chkInList.get(0).get("cr_syscd").equals("00550") ||
					   chkInList.get(0).get("cr_syscd").equals("00560") ) {	//홈페이지_WAS시 QA결재단계가 없어 기본값을 DB에 인서트
						strQuery.setLength(0);
						strQuery.append("insert into cmr0900 																	\n");
						strQuery.append("(cr_acptno,cr_qacheck,cr_totalcheck,cr_chgcode, cr_detailcode							\n");
						strQuery.append(") values ( 																			\n");
						strQuery.append("?,?,?,?,?)										 										\n");
						pstmt = conn.prepareStatement(strQuery.toString());
						//pstmt = new LoggableStatement(conn,strQuery.toString());
						pstmt.setString(1, AcptNo);
						pstmt.setString(2, "110");
						pstmt.setString(3, "AAAA");
						pstmt.setString(4, "07");
						pstmt.setString(5, "07");
						
						//ecamsLogger.error(((LoggableStatement)pstmt).getQueryString());
						pstmt.executeUpdate();
						pstmt.close();
					}
				}
				
				strQuery.setLength(0);
				strQuery.append("insert into cmr1010 ");
				strQuery.append("(CR_ACPTNO,CR_SERNO,CR_SYSCD,CR_SYSGB,CR_JOBCD,CR_STATUS,CR_QRYCD,  			\n");
				strQuery.append("CR_RSRCCD,CR_LANGCD,CR_DSNCD,CR_RSRCNAME,CR_RSRCNAM2,CR_SRCCHG,CR_SRCCMP, 		\n");
				strQuery.append("CR_PRIORITY,CR_APLYDATE, CR_COMPDATE,CR_VERSION,CR_BEFVER,CR_CONFNO,CR_EDITOR,CR_DSNCD2,   	\n");
				strQuery.append("CR_BASENO,CR_BASEITEM,CR_ITEMID,CR_EDITCON,CR_COACPT,CR_BASEPGM,		      	\n");
				strQuery.append("CR_IMPORTANT,CR_NEWGLO,CR_DEALCODE,CR_TESTYN) values (							\n");
				strQuery.append("?,?,?,?,?,'0',?,  ?,?,?,?,?,?,'Y',  ?,?,?,?,?,?,?,?,  ?,?,?,?,?,?,  ?,?,?,?) 	\n");
				
				pstmtcount = 1;
				pstmt = conn.prepareStatement(strQuery.toString());
				//pstmt = new LoggableStatement(conn,strQuery.toString());
				
				//ecamsLogger.error("++++++reqcd,rsrccd++++++"+chkInList.get(i).get("reqcd")+","
				//		+chkInList.get(i).get("cr_rsrccd"));
				
				pstmt.setString(pstmtcount++, AcptNo);
				pstmt.setInt(pstmtcount++, ++seq);
				pstmt.setString(pstmtcount++, chkInList.get(i).get("cr_syscd"));
				pstmt.setString(pstmtcount++, chkInList.get(i).get("sysgb"));
				pstmt.setString(pstmtcount++, chkInList.get(i).get("cr_jobcd"));
				pstmt.setString(pstmtcount++, chkInList.get(i).get("reqcd"));  
				String tmprsrc = chkInList.get(i).get("cr_rsrccd").substring(0,2);
				pstmt.setString(pstmtcount++, tmprsrc);
				pstmt.setString(pstmtcount++, chkInList.get(i).get("cr_langcd"));
				pstmt.setString(pstmtcount++, chkInList.get(i).get("cr_dsncd"));
				pstmt.setString(pstmtcount++, chkInList.get(i).get("cr_rsrcname"));
				pstmt.setString(pstmtcount++, chkInList.get(i).get("cr_rsrcname"));
				if (chkInList.get(i).get("reqcd").equals("04") || chkInList.get(i).get("reqcd").equals("03")) {
					pstmt.setString(pstmtcount++,"1");
				} else {
					pstmt.setString(pstmtcount++,"0");
				}
				
				
				pstmt.setInt(pstmtcount++, Integer.parseInt(chkInList.get(i).get("prcseq")));
				
				
				if (chkInList.get(i).get("Deploy").equals("4")) {
					pstmt.setString(pstmtcount++,chkInList.get(i).get("AplyDate"));
					pstmt.setString(pstmtcount++,chkInList.get(i).get("CompDate"));
				} else {
					pstmt.setString(pstmtcount++,"");
					pstmt.setString(pstmtcount++,"");
				}
				pstmt.setString(pstmtcount++, chkInList.get(i).get("cr_aftver"));
				pstmt.setString(pstmtcount++, chkInList.get(i).get("cr_lstver"));
				if (etcData.get("ReqCD").equals("04")) {
					if (!"".equals(chkInList.get(i).get("cr_acptno")) && chkInList.get(i).get("cr_acptno") != null) {  
						pstmt.setString(pstmtcount++,chkInList.get(i).get("cr_acptno"));
					} else {
						pstmt.setString(pstmtcount++,AcptNo);
					}
				} else {
					pstmt.setString(pstmtcount++,chkInList.get(i).get(""));
				}
				pstmt.setString(pstmtcount++, etcData.get("UserID"));
				pstmt.setString(pstmtcount++, chkInList.get(i).get("pcdir"));
				
				if (chkInList.get(i).get("reqcd").equals("05") || chkInList.get(i).get("reqcd").equals("09")) {
					pstmt.setString(pstmtcount++, chkInList.get(i).get("cr_baseno"));
				} else {
					if (!"".equals(chkInList.get(i).get("cr_acptno")) && chkInList.get(i).get("cr_acptno") != null) {  
						pstmt.setString(pstmtcount++,chkInList.get(i).get("cr_acptno"));
					} else {
						pstmt.setString(pstmtcount++,chkInList.get(i).get(""));
					}
				}
				pstmt.setString(pstmtcount++, chkInList.get(i).get("baseitem"));
				pstmt.setString(pstmtcount++, chkInList.get(i).get("cr_itemid"));
				if (chkInList.get(i).get("cr_sayu") != null && !"".equals(chkInList.get(i).get("cr_sayu"))) 
					pstmt.setString(pstmtcount++, chkInList.get(i).get("cr_sayu")); 
				else pstmt.setString(pstmtcount++, "");
				if (chkInList.get(i).get("reqcd").equals("09")) {
					pstmt.setString(pstmtcount++, selBaseno(chkInList.get(i).get("cr_itemid"),conn));
				} else {
					pstmt.setString(pstmtcount++, AcptNo);
				}
				pstmt.setString(pstmtcount++, chkInList.get(i).get("baseitem"));
				
				pstmt.setString(pstmtcount++, chkInList.get(i).get("importancecode"));
				pstmt.setString(pstmtcount++, chkInList.get(i).get("newgoodscode"));
				pstmt.setString(pstmtcount++, chkInList.get(i).get("dealcode"));
				pstmt.setString(pstmtcount++, chkInList.get(i).get("testyn"));
				
				////ecamsLogger.error(((LoggableStatement)pstmt).getQueryString());
				pstmt.executeUpdate();
				pstmt.close();
				
				//==========================================================================
				//처리팩터 58(이클립스사용)일경우
				if(chkInList.get(i).get("cm_info").substring(57,58).equals("1")){
					//new DB에서 syscd, rsrcname, dirpath로 모델의 itemid 를 구함
					String model_itemid = "";
					String model_acptno = "";
					strQuery.setLength(0);
					strQuery.append(" select a.cr_itemid, a.cr_acptno	\n");
					strQuery.append("   from cmr0020 a, cmm0070 b       \n");
					strQuery.append("  where a.cr_syscd = ?              \n");
					strQuery.append("    and a.cr_rsrcname = ?          \n");
					strQuery.append("    and b.cm_dirpath = ?           \n");
					strQuery.append("    and a.cr_syscd = b.cm_syscd    \n");
					strQuery.append("    and a.cr_dsncd = b.cm_dsncd    \n");
					pstmt = connD.prepareStatement(strQuery.toString());
					pstmt.setString(1, chkInList.get(i).get("cr_syscd"));
					pstmt.setString(2, chkInList.get(i).get("cr_rsrcname"));
					pstmt.setString(3, chkInList.get(i).get("cm_dirpath"));
					rs = pstmt.executeQuery();
					if(rs.next()){
						model_itemid = rs.getString("cr_itemid");
						model_acptno = rs.getString("cr_acptno");
					}
					rs.close();
					pstmt.close();
					
					ecamsLogger.error("@@@@@@@@@@ DEV CHECK >> " +  model_acptno + ":" + model_itemid + " > " + AcptNo); 
					
					//모델의 itemid가 있으면
					if(!"".equals(model_itemid)){
						//new DB에서 모델의 itemid를 baseitem으로 갖는 java을 구함
						strQuery.setLength(0);
						strQuery.append(" select a.cr_syscd, a.cr_rsrcname, b.cm_dirpath,   \n");
						strQuery.append("        c.cr_version, c.cr_befver, c.cr_rsrccd,    \n");
						strQuery.append("        c.cr_langcd, c.cr_baseno, c.CR_PRIORITY,a.cr_acptno    \n");
						strQuery.append("   from cmr0020 a, cmm0070 b, cmr1010 c            \n");
						strQuery.append("  where c.cr_baseitem = ?                          \n");
						strQuery.append("    and c.cr_acptno = ?                          	\n");
						strQuery.append("    and a.cr_syscd = b.cm_syscd                    \n");
						strQuery.append("    and a.cr_dsncd = b.cm_dsncd                    \n");
						strQuery.append("    and a.cr_syscd = c.cr_syscd        			\n");
						strQuery.append("    and a.cr_dsncd = c.cr_dsncd        			\n");
						strQuery.append("    and a.cr_itemid = c.cr_itemid        			\n");
						strQuery.append("    and c.cr_confno is null        				\n");
						strQuery.append("    and c.cr_status in ('8','9')        			\n");
						strQuery.append("    AND a.cr_acptno = c.cr_acptno					\n");
						pstmt = connD.prepareStatement(strQuery.toString());
			        	pstmt = new LoggableStatement(connD,strQuery.toString());						
						pstmt.setString(1, model_itemid);
						pstmt.setString(2, model_acptno);
			        	ecamsLogger.error(((LoggableStatement)pstmt).getQueryString());
						rs = pstmt.executeQuery();
						while(rs.next()){
							//java가 있을 경우 asis DB에서 dsncd를 찾음
							String new_dsncd = "";
							strQuery.setLength(0);
							strQuery.append("select cm_dsncd from cmm0070						\n");
							strQuery.append("where cm_dirpath = ? and cm_syscd = ?				\n");
							pstmt2 = conn.prepareStatement(strQuery.toString());
							pstmt2.setString(1, rs.getString("cm_dirpath"));
							pstmt2.setString(2, rs.getString("cr_syscd"));
							rs2 = pstmt2.executeQuery();
							if(rs2.next()){
								new_dsncd = rs2.getString("cm_dsncd");
							}else{
								//java의 경로를 찾지 못한경우 경로를 자동insert
								Cmd0100 cmd0100 = new Cmd0100();
								new_dsncd = cmd0100.cmm0070_Insert(etcData.get("UserID"),chkInList.get(i).get("cr_syscd"),rs.getString("cr_rsrcname"),rs.getString("cr_rsrccd"),chkInList.get(i).get("cr_jobcd"),rs.getString("cm_dirpath"),true,conn);	
								if (new_dsncd == null || new_dsncd == "") {
									conn.close();
									connD.close();
									conn = null;
									connD = null;
									return "ERROR디렉토리 등록 처리 중 오류가 발생하였습니다. ["+rs.getString("cm_dirpath")+"/"+rs.getString("cr_rsrcname")+"]";
								}
								cmd0100 = null;
							}
							rs2.close();
							pstmt2.close();
							
							//ASIS에서 java의 경로를 제대로 가져온경우
							if(!"".equals(new_dsncd)){
								//ASIS DB에서 java의 itemid를 구함
								String java_itemid = "";
								int java_version = 0;
								int java_befver = 0;
								strQuery.setLength(0);
								strQuery.append(" select a.cr_itemid,a.cr_lstver  								\n");
								strQuery.append("   from cmr0020 a, cmm0070 b                       \n");
								strQuery.append("  where a.cr_syscd = ?                        	 	\n");
								strQuery.append("    and a.cr_dsncd = ?                         	\n");
								strQuery.append("    and a.cr_rsrcname = ?                         	\n");
								strQuery.append("    and a.cr_syscd = b.cm_syscd                    \n");
								strQuery.append("    and a.cr_dsncd = b.cm_dsncd                    \n");
								pstmt2 = conn.prepareStatement(strQuery.toString());
								pstmt2.setString(1, rs.getString("cr_syscd"));
								pstmt2.setString(2, new_dsncd);
								pstmt2.setString(3, rs.getString("cr_rsrcname"));
								rs2 = pstmt2.executeQuery();
								if(rs2.next()){
									java_itemid = rs2.getString("cr_itemid");
									java_befver = rs2.getInt("cr_lstver");
									java_version = java_befver +1;
								}else{
									java_itemid = ""; //asis db에 java의 itemid가 없으면 cmr1010에 ""로 넣음
									java_befver = 0;
									java_version = 1;
								}
								
								rs2.close();
								pstmt2.close();
								//java신청하기전에 모델에있는지 확인
								int tmpcnt = 0;
								strQuery.setLength(0);
								strQuery.append(" select count(*)as cnt   				\n");
								strQuery.append("   from cmr1010                       \n");
								strQuery.append("  where cr_acptno = ?                 \n");
								strQuery.append("    and cr_rsrcname = ?                \n");
								strQuery.append("    and cr_syscd = ?                   \n");
								strQuery.append("    and cr_jobcd = ?                   \n");
								strQuery.append("    and cr_dsncd = ?                   \n");
								pstmt2 = conn.prepareStatement(strQuery.toString());
								pstmt2.setString(1, AcptNo);
								pstmt2.setString(2, rs.getString("cr_rsrcname"));
								pstmt2.setString(3, chkInList.get(i).get("cr_syscd"));
								pstmt2.setString(4, chkInList.get(i).get("cr_jobcd"));
								pstmt2.setString(5, new_dsncd);
								rs2 = pstmt2.executeQuery();
								while(rs2.next()){
									tmpcnt = rs2.getInt("cnt");
									ecamsLogger.error("@@@@@@@@@@ cmr1010tmpcnt >> " +  tmpcnt + " : " + AcptNo + " : " + rs.getString("cr_rsrcname"));
								}
								//모델과 함께 모델에 포함된 java도 cmr1010신청기록 입력
								if(tmpcnt == 0){
									strQuery.setLength(0);
									strQuery.append("insert into cmr1010 ");
									strQuery.append("(CR_ACPTNO,CR_SERNO,CR_SYSCD,CR_SYSGB,CR_JOBCD,CR_STATUS,CR_QRYCD,  			\n");
									strQuery.append("CR_RSRCCD,CR_LANGCD,CR_DSNCD,CR_RSRCNAME,CR_RSRCNAM2,CR_SRCCHG,CR_SRCCMP, 		\n");
									strQuery.append("CR_PRIORITY,CR_APLYDATE, CR_COMPDATE,CR_VERSION,CR_BEFVER,CR_CONFNO,CR_EDITOR,CR_DSNCD2,   	\n");
									strQuery.append("CR_BASENO,CR_BASEITEM,CR_ITEMID,CR_EDITCON,CR_COACPT,CR_BASEPGM,		      	\n");
									strQuery.append("CR_IMPORTANT,CR_NEWGLO,CR_DEALCODE,CR_TESTYN) values (							\n");
									strQuery.append("?,?,?,?,?,'0',?,  ?,?,?,?,?,?,'Y',  ?,?,?,?,?,?,?,?,  ?,?,?,?,?,?,  ?,?,?,?) 	\n");
									
									pstmtcount = 1;
									pstmt2 = conn.prepareStatement(strQuery.toString());
									pstmt2 = new LoggableStatement(conn,strQuery.toString());
									pstmt2.setString(pstmtcount++, AcptNo);
									pstmt2.setInt(pstmtcount++, ++seq);
									pstmt2.setString(pstmtcount++, chkInList.get(i).get("cr_syscd"));
									pstmt2.setString(pstmtcount++, chkInList.get(i).get("sysgb"));
									pstmt2.setString(pstmtcount++, chkInList.get(i).get("cr_jobcd"));
									pstmt2.setString(pstmtcount++, chkInList.get(i).get("reqcd"));  
									
									pstmt2.setString(pstmtcount++, rs.getString("cr_rsrccd"));
									pstmt2.setString(pstmtcount++, rs.getString("cr_langcd"));
									pstmt2.setString(pstmtcount++, new_dsncd);
									pstmt2.setString(pstmtcount++, rs.getString("cr_rsrcname"));
									pstmt2.setString(pstmtcount++, rs.getString("cr_rsrcname"));
									if (chkInList.get(i).get("reqcd").equals("04") || chkInList.get(i).get("reqcd").equals("03")) {
										pstmt2.setString(pstmtcount++,"1");
									} else {
										pstmt2.setString(pstmtcount++,"0");
									}
									
									
									//pstmt2.setString(pstmtcount++, "");
									pstmt2.setInt(pstmtcount++, rs.getInt("CR_PRIORITY"));
									if (chkInList.get(i).get("Deploy").equals("4")) {
										pstmt2.setString(pstmtcount++,chkInList.get(i).get("AplyDate"));
										pstmt2.setString(pstmtcount++,chkInList.get(i).get("CompDate"));
									} else {
										pstmt2.setString(pstmtcount++,"");
										pstmt2.setString(pstmtcount++,"");
									}
									pstmt2.setString(pstmtcount++, Integer.toString(java_version));
									pstmt2.setString(pstmtcount++, Integer.toString(java_befver));
									pstmt2.setString(pstmtcount++, rs.getString("CR_ACPTNO"));//cr_confno
									pstmt2.setString(pstmtcount++, etcData.get("UserID"));
									pstmt2.setString(pstmtcount++, "");
									
									
									pstmt2.setString(pstmtcount++, rs.getString("cr_baseno"));
									//pstmt2.setString(pstmtcount++, model_itemid);//baseitemid
									pstmt2.setString(pstmtcount++, chkInList.get(i).get("cr_itemid"));//baseitemid								
									pstmt2.setString(pstmtcount++, java_itemid);//itemid
									if (chkInList.get(i).get("cr_sayu") != null && !"".equals(chkInList.get(i).get("cr_sayu"))) 
										pstmt2.setString(pstmtcount++, chkInList.get(i).get("cr_sayu")); 
									else pstmt2.setString(pstmtcount++, "");
									pstmt2.setString(pstmtcount++, AcptNo);//CR_COACPT
									pstmt2.setString(pstmtcount++, chkInList.get(i).get("cr_itemid")); //CR_BASEPGM(chkInList.get(i).get("baseitem"))
									
									pstmt2.setString(pstmtcount++, "");
									pstmt2.setString(pstmtcount++, "");
									pstmt2.setString(pstmtcount++, "");
									pstmt2.setString(pstmtcount++, "");
									ecamsLogger.error(((LoggableStatement)pstmt2).getQueryString());
									pstmt2.executeUpdate();
									pstmt2.close();
								}
							}
						}
						rs.close();
						pstmt.close();
					
						strQuery.setLength(0);
						strQuery.append("update cmr1010 set cr_confno = ?							\n");
						strQuery.append("where NVL(cr_baseitem, cr_itemid) = ?   	  				\n");
						strQuery.append("AND cr_confno is null   									\n");
						strQuery.append("AND CR_STATUS IN ('8','9')			   						\n");
						strQuery.append("AND substr(cr_acptno,5,2)='03'						        \n");
						strQuery.append("AND cr_acptno = ?									        \n");
						pstmt = connD.prepareStatement(strQuery.toString());
						pstmt.setString(1, AcptNo);
						pstmt.setString(2, model_itemid);
						pstmt.setString(3, model_acptno);
						pstmt.executeUpdate();
						pstmt.close();						
						//==========================================================================
					}
				}
				
				
				for(int z =0;z<OrderList.size();z++){
					
					if(chkInList.get(i).get("cr_itemid").equals(OrderList.get(z).get("CR_ITEMID"))){
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
						strQuery.append("select count(*) as cnt from cmr1012 \n");
						strQuery.append("where cr_acptno=? and cr_itemid=? and cr_orderid=? \n");
						pstmtcount = 1;
						pstmt = conn.prepareStatement(strQuery.toString());
						pstmt.setString(pstmtcount++, AcptNo);
						pstmt.setString(pstmtcount++, OrderList.get(z).get("CR_ITEMID"));
						pstmt.setString(pstmtcount++, OrderList.get(z).get("CC_ORDERID"));
						rs = pstmt.executeQuery();
						if (rs.next()){
							vcnt = rs.getInt("cnt");
						}	        	
						rs.close();
						pstmt.close();
						
						if(vcnt == 0){
							strQuery.setLength(0);
							strQuery.append("insert into cmr1012 ");
							strQuery.append("(CR_ACPTNO,CR_SEQ,CR_ITEMID,CR_ORDERID,CR_REQSUB) values (					 \n");
							strQuery.append("?, ?, ?, ?, ?)");
							pstmtcount = 1;
							pstmt = conn.prepareStatement(strQuery.toString());
							//pstmt = new LoggableStatement(conn, strQuery.toString());
							pstmt.setString(pstmtcount++, AcptNo);
							pstmt.setInt(pstmtcount++, cnt++);
							pstmt.setString(pstmtcount++, OrderList.get(z).get("CR_ITEMID"));
							pstmt.setString(pstmtcount++, OrderList.get(z).get("CC_ORDERID"));
							pstmt.setString(pstmtcount++, OrderList.get(z).get("CC_REQSUB"));
							//ecamsLogger.error(((LoggableStatement)pstmt).getQueryString());
							pstmt.executeUpdate();
							pstmt.close();
						}
					}
				}
			}
			
			if (etcData.get("ReqCD").equals("04") && etcData.get("TstSw").equals("1")) {
				strQuery.setLength(0);
				strQuery.append("insert into cmr1010 (CR_ACPTNO,CR_SERNO,CR_SYSCD,CR_SYSGB,CR_JOBCD,   								\n");
				strQuery.append("   CR_STATUS,CR_QRYCD,CR_RSRCCD,CR_DSNCD,CR_DSNCD2,CR_RSRCNAME,       								\n");
				strQuery.append("   CR_RSRCNAM2,CR_SRCCHG,CR_SRCCMP,CR_PRIORITY,CR_APLYDATE,           								\n");
				strQuery.append("   CR_VERSION,CR_BEFVER,CR_CONFNO,CR_EDITOR,CR_BASENO,CR_BASEITEM,    								\n");
				strQuery.append("   CR_ITEMID,CR_EDITCON,CR_BASEPGM,CR_IMPORTANT,CR_NEWGLO,CR_DEALCODE) 	\n");
				strQuery.append(" (select a.cr_acptno,                                                 								\n");
				strQuery.append("    (select max(cr_serno) from cmr1010 where cr_acptno=?)+rownum,     								\n");
				strQuery.append("    a.cr_syscd,a.CR_SYSGB,b.CR_JOBCD,'0',b.CR_QRYCD,b.CR_RSRCCD,      								\n");
				strQuery.append("    b.CR_DSNCD,b.CR_DSNCD2,b.CR_RSRCNAME,b.CR_RSRCNAM2,b.CR_SRCCHG,   								\n");
				strQuery.append("    'Y',b.CR_PRIORITY,a.CR_APLYDATE,b.CR_VERSION,b.CR_BEFVER,         								\n");
				strQuery.append("    a.CR_CONFNO,a.CR_EDITOR,a.CR_BASENO,a.CR_BASEITEM,b.CR_ITEMID,    								\n"); 
				strQuery.append("    a.CR_EDITCON,b.CR_BASEPGM,b.CR_IMPORTANT,b.CR_NEWGLO,b.CR_DEALCODE\n");    
				strQuery.append("    from cmr1010 a,cmr1010 b                                          								\n");
				strQuery.append("  where a.cr_acptno=? and a.cr_itemid=a.cr_baseitem                   								\n"); 
				strQuery.append("    and b.cr_acptno=a.cr_confno and b.cr_status<>'3'                  								\n");
				strQuery.append("    and a.cr_itemid=b.cr_baseitem and a.cr_itemid<>b.cr_itemid        								\n");
				strQuery.append("    and a.cr_status<>'3')                                             								\n");  
				pstmt = conn.prepareStatement(strQuery.toString());
				pstmtcount = 1;
				pstmt.setString(pstmtcount++, AcptNo);
				pstmt.setString(pstmtcount++, AcptNo);
				pstmt.executeUpdate();
				pstmt.close();
				
				strQuery.setLength(0);
				strQuery.append("select a.cr_basepgm,a.cr_serno    \n");
				strQuery.append("  from cmr1010 a                  \n");
				strQuery.append(" where a.cr_acptno=?              \n");
				strQuery.append("   and a.cr_basepgm is not null   \n");
				strQuery.append("   and a.cr_itemid<>a.cr_baseitem \n");
				strQuery.append("   and a.cr_baseitem<>a.cr_basepgm \n");
				pstmt = conn.prepareStatement(strQuery.toString());
				pstmt.setString(1, AcptNo);
				rs = pstmt.executeQuery();
				while (rs.next()) {
					strBase2 = rs.getString("cr_basepgm").split(","); 
					strBasePgm = "";
					for (i=0;strBase2.length>i;i++) {
						strQuery.setLength(0);
						strQuery.append("select count(*) cnt from cmr1010 \n");
						strQuery.append(" where cr_acptno=?               \n");
						strQuery.append("   and cr_itemid=?               \n");
						pstmt2 = conn.prepareStatement(strQuery.toString());
						pstmt2.setString(1, AcptNo);
						pstmt2.setString(2, strBase2[i]);
						rs2 = pstmt2.executeQuery();
						if (rs2.next()) {
							if (rs2.getInt("cnt")>0) {
								if (strBasePgm.length()>0) strBasePgm = strBasePgm + ",";
								strBasePgm = strBasePgm + strBase2[i];
							}
						}
						rs2.close();
						pstmt2.close();
					}
					
					if (!rs.getString("cr_basepgm").equals(strBasePgm)) {
						strQuery.setLength(0);
						strQuery.append("update cmr1010 set cr_basepgm=?  \n");
						strQuery.append(" where cr_acptno=?               \n");
						strQuery.append("   and cr_serno=?                \n");
						pstmt2 = conn.prepareStatement(strQuery.toString());
						pstmt2 = new LoggableStatement(conn,strQuery.toString());
						pstmt2.setString(1, strBasePgm);
						pstmt2.setString(2, AcptNo);
						pstmt2.setString(3, rs.getString("cr_serno"));
						ecamsLogger.error(((LoggableStatement)pstmt2).getQueryString());
						pstmt2.executeUpdate();
						pstmt2.close();
					}
					
				}
				rs.close();
				pstmt.close();
				
				strQuery.setLength(0);
				strQuery.append("select cr_itemid                          \n");
				strQuery.append("  from cmr1010 where cr_acptno=?          \n");
				strQuery.append(" group by cr_itemid                       \n");
				strQuery.append(" having count(*)>1                        \n");
				pstmt = conn.prepareStatement(strQuery.toString());
				pstmt.setString(1, AcptNo);
				rs = pstmt.executeQuery();
				
				while (rs.next()) {
					strBasePgm = "";
					strQuery.setLength(0);
					strQuery.append("select cr_baseitem,cr_serno   \n");
					strQuery.append("  from cmr1010                \n");
					strQuery.append(" where cr_acptno=?            \n");
					strQuery.append("   and cr_itemid=?            \n");
					strQuery.append("   and cr_itemid<>cr_baseitem \n");
					strQuery.append(" order by cr_serno            \n");
					pstmt2 = conn.prepareStatement(strQuery.toString());
					//pstmt2 = new LoggableStatement(conn,strQuery.toString());
					pstmt2.setString(1, AcptNo);
					pstmt2.setString(2, rs.getString("cr_itemid"));
					// //ecamsLogger.error(((LoggableStatement)pstmt2).getQueryString());
					rs2 = pstmt2.executeQuery();
					while (rs2.next()) {            	            		
						//if (strBasePgm.length() > 0) strBasePgm = strBasePgm + ",";
						//strBasePgm = strBasePgm + rs2.getString("cr_baseitem");
						strBasePgm = rs2.getString("cr_baseitem");
						
						if (rs2.getRow()>1) {
							strQuery.setLength(0);
							strQuery.append("delete cmr1010      \n");
							strQuery.append(" where cr_acptno=?  \n");
							strQuery.append("   and cr_serno=?   \n");
							pstmt3 = conn.prepareStatement(strQuery.toString());
							//            		pstmt3 = new LoggableStatement(conn,strQuery.toString());
							pstmt3.setString(1, AcptNo);
							pstmt3.setString(2, rs2.getString("cr_serno"));
							//    	                //ecamsLogger.error(((LoggableStatement)pstmt3).getQueryString());
							pstmt3.executeUpdate();
							pstmt3.close();
						}
					}
					rs2.close();
					pstmt2.close();
					
					if (strBasePgm.length()>0) {
						strQuery.setLength(0);
						strQuery.append("update cmr1010 set cr_basepgm=?        \n");
						strQuery.append(" where cr_acptno=?                     \n");
						strQuery.append("   and cr_itemid=?                     \n");
						pstmt2 = conn.prepareStatement(strQuery.toString());
						pstmt2 = new LoggableStatement(conn,strQuery.toString());
						pstmt2.setString(1, strBasePgm);
						pstmt2.setString(2, AcptNo);
						pstmt2.setString(3, rs.getString("cr_itemid"));
						ecamsLogger.error(((LoggableStatement)pstmt2).getQueryString());
						pstmt2.executeUpdate();
						pstmt2.close();
					} else {
						strQuery.setLength(0);
						strQuery.append("delete cmr1010 where cr_acptno=?      \n");
						strQuery.append("   and cr_itemid=?                    \n");
						strQuery.append("   and cr_serno not in (select cr_serno from cmr1010 \n");
						strQuery.append("                         where cr_acptno=?           \n");
						strQuery.append("                           and cr_itemid=?           \n");
						strQuery.append("                           and rownum<2)             \n");
						pstmt2 = conn.prepareStatement(strQuery.toString());
						pstmt2.setString(1, AcptNo);
						pstmt2.setString(2, rs.getString("cr_itemid"));
						pstmt2.setString(3, AcptNo);
						pstmt2.setString(4, rs.getString("cr_itemid"));
						//        		//ecamsLogger.error(((LoggableStatement)pstmt2).getQueryString());
						pstmt2.executeUpdate();
						pstmt2.close();
					}
				}
				rs.close();
				pstmt.close();
			}   
			
			//ecamsLogger.error("+++++++++CHECK-IN LIST Update START (cmr0020)+++ 3705");
			etcData.put("CC_ACPTNO", AcptNo);
			//ecamsLogger.error("+++++++++CHECK-IN LIST Update START (cmr0020)+++ 3714");
			
			 
			retMsg = request_Confirm(AcptNo,chkInList.get(0).get("cr_syscd"),etcData.get("ReqCD"),etcData.get("UserID"),true,ConfList,conn);
			if (!retMsg.equals("OK")) {
				conn.rollback();
				conn.close();
				connD.close();
				throw new Exception("결재정보등록 중 오류가 발생하였습니다. 관리자에게 연락하여 주시기 바랍니다.");
			} 
			//ecamsLogger.error("+++++++++CHECK-IN LIST Update START (cmr0020)+++ 3721");
			conn.commit(); 
			
			for (j=0;svAcpt.length>j;j++) {
				strQuery.setLength(0);
				strQuery.append("select cr_acptno,cr_confno,cr_serno,cr_qrycd,cr_itemid,cr_baseitem      \n");
				strQuery.append("  from cmr1010 where cr_acptno=?                                        \n");
				pstmt = conn.prepareStatement(strQuery.toString());
				pstmt.setString(1, svAcpt[j]);
				rs = pstmt.executeQuery();
				while (rs.next()) {
					//String tempconf =rs.getString("cr_confno");
					//tempconf = tempconf.substring(4,6);
					pstmtcount = 1;
					strQuery.setLength(0);
					//if(!tempconf.equals("03")){
						strQuery.append("update cmr0020 set                           \n");
					
						if (etcData.get("ReqCD").equals("03")) {
							if (rs.getString("cr_qrycd").equals("09")) {
								strQuery.append("cr_nomodify='1',                     \n");
							} else {
								strQuery.append("cr_status='A',                       \n");
							}
						} else {
							if (rs.getString("cr_qrycd").equals("09")) {
								strQuery.append("cr_nomodify='1',                     \n");
							} else {
								strQuery.append("cr_status='7',                       \n");
							}            		
						}
						
						
							strQuery.append("cr_editor= ?                                 \n");
						
						strQuery.append("where cr_itemid= ?                           \n");
					
						pstmt2 = conn.prepareStatement(strQuery.toString());
						
						pstmt2.setString(pstmtcount++, etcData.get("UserID"));
						pstmt2.setString(pstmtcount++, rs.getString("cr_itemid"));
						
						pstmt2.executeUpdate();
						pstmt2.close();
					//}
					if (etcData.get("ReqCD").equals("04") && etcData.get("TstSw").equals("1")) {
						strQuery.setLength(0);
						
						strQuery.append("update cmr1010 set cr_baseno=(select cr_baseno from cmr1010  \n");
						strQuery.append("                               where cr_acptno=?             \n");
						strQuery.append("                                 and cr_itemid=?)            \n");
						strQuery.append(" where cr_acptno=? and cr_serno=?                            \n");
						pstmt2 = conn.prepareStatement(strQuery.toString());
						pstmtcount = 1;
						pstmt2.setString(pstmtcount++, rs.getString("cr_confno"));
						pstmt2.setString(pstmtcount++, rs.getString("cr_itemid"));
						pstmt2.setString(pstmtcount++, svAcpt[j]);
						pstmt2.setInt(pstmtcount++, rs.getInt("cr_serno"));
						pstmt2.executeUpdate();
						pstmt2.close();
					} else if (etcData.get("ReqCD").equals("03")) {
						strQuery.setLength(0);
						
						strQuery.append("update cmr1010 set cr_confno=?                               \n");
						strQuery.append(" where cr_confno is null and substr(cr_acptno,5,2)='03'      \n");
						strQuery.append("   and cr_itemid=? and cr_acptno<>?                          \n");
						pstmt2 = conn.prepareStatement(strQuery.toString());
						pstmtcount = 1;
						pstmt2.setString(pstmtcount++, svAcpt[j]);
						pstmt2.setString(pstmtcount++, rs.getString("cr_itemid"));
						pstmt2.setString(pstmtcount++, svAcpt[j]);
						
						pstmt2.executeUpdate();
						pstmt2.close();
					}
					if (!"".equals(rs.getString("cr_confno")) && rs.getString("cr_confno") != null && etcData.get("ReqCD").equals("04")) {
						strQuery.setLength(0);
						strQuery.append("update cmr1010 set cr_confno=?                               \n");
						strQuery.append(" where cr_acptno=? and cr_itemid=?                           \n");
						pstmt3 = conn.prepareStatement(strQuery.toString());
						//pstmt3 =  new LoggableStatement(conn, strQuery.toString());
						pstmtcount = 1;
						pstmt3.setString(pstmtcount++, svAcpt[j]);
						pstmt3.setString(pstmtcount++, rs.getString("cr_confno"));
						pstmt3.setString(pstmtcount++, rs.getString("cr_itemid"));
						////ecamsLogger.error(((LoggableStatement)pstmt3).getQueryString());
						pstmt3.executeUpdate();
						pstmt3.close();
						
						if (etcData.get("TstSw").equals("1") && rs.getString("cr_qrycd").equals("04")) {
							strQuery.setLength(0);
							
							strQuery.append("update cmr1010 set cr_confno=?                               \n");
							strQuery.append(" where cr_acptno=(select cr_baseno from cmr1010              \n");
							strQuery.append("                   where cr_acptno=? and cr_itemid=?)        \n");
							strQuery.append("   and cr_itemid=?                                           \n");
							pstmt2 = conn.prepareStatement(strQuery.toString());
							pstmtcount = 1;
							pstmt2.setString(pstmtcount++, svAcpt[j]);
							pstmt2.setString(pstmtcount++, rs.getString("cr_confno"));
							pstmt2.setString(pstmtcount++, rs.getString("cr_itemid"));
							pstmt2.setString(pstmtcount++, rs.getString("cr_itemid"));
							pstmt2.executeUpdate();
							pstmt2.close();
						}
					}
				}
				rs.close();
				pstmt.close();
				
				//ecamsLogger.error("+++++++++CHECK-IN LIST Update START (cmr0020)+++ 3824");
				for (i=0;i<befJob.size();i++){
					strQuery.setLength(0);
					strQuery.append("insert into cmr1030 ");
					strQuery.append("(CR_ACPTNO,CR_BEFACT) values (?, ?) \n");
					
					pstmtcount = 1;
					pstmt = conn.prepareStatement(strQuery.toString());
					//pstmt = new LoggableStatement(conn,strQuery.toString());
					
					pstmt.setString(pstmtcount++, svAcpt[j]);
					pstmt.setString(pstmtcount++, befJob.get(i).get("cr_befact"));
					pstmt.executeUpdate();
					pstmt.close();
				}
				//ecamsLogger.error("+++++++++CHECK-IN LIST Insert START (cmr9900)+++");
			}
        	//ecamsLogger.error("+++++++++CHECK-IN LIST Update START (cmr0020)+++ 3844");
        	conn.close();
        	connD.close();
        	
        	rs.close();
        	rs = null;
        	pstmt = null;
        	conn = null;
        	
        	//ecamsLogger.error("+++++++++Request E N D+++");
        	return AcptNo;
			
		} catch (SQLException sqlexception) {
			sqlexception.printStackTrace();
			if (rs != null)     try{rs.close();}catch (Exception ex){ex.printStackTrace();}
			if (pstmt != null)  try{pstmt.close();}catch (Exception ex2){ex2.printStackTrace();}
			if (rs2 != null)     try{rs2.close();}catch (Exception ex){ex.printStackTrace();}
			if (pstmt2 != null)  try{pstmt2.close();}catch (Exception ex2){ex2.printStackTrace();}
			if (pstmt3 != null)  try{pstmt3.close();}catch (Exception ex2){ex2.printStackTrace();}
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
			if (connD != null){
				try{
					connD.rollback();
					ConnectionResource.release(connD);
					connD = null;
				}catch(Exception ex3){
					ecamsLogger.error("## Cmr0200.request_Check_In() connection release exception ##");
					ex3.printStackTrace();
				}				
			}
			ecamsLogger.error("## Cmr0200.request_Check_In() SQLException START ##");
			ecamsLogger.error("## Error DESC : ", sqlexception);	
			ecamsLogger.error("## Cmr0200.request_Check_In() SQLException END ##");			
			throw sqlexception;
		} catch (Exception exception) {
			exception.printStackTrace();
			if (rs != null)     try{rs.close();}catch (Exception ex){ex.printStackTrace();}
			if (pstmt != null)  try{pstmt.close();}catch (Exception ex2){ex2.printStackTrace();}
			if (rs2 != null)     try{rs2.close();}catch (Exception ex){ex.printStackTrace();}
			if (pstmt2 != null)  try{pstmt2.close();}catch (Exception ex2){ex2.printStackTrace();}
			if (pstmt3 != null)  try{pstmt3.close();}catch (Exception ex2){ex2.printStackTrace();}
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
			ecamsLogger.error("## Cmr0200.request_Check_In() Exception START ##");				
			ecamsLogger.error("## Error DESC : ", exception);	
			ecamsLogger.error("## Cmr0200.request_Check_In() Exception END ##");				
			throw exception;
		}finally{
			if (strQuery != null) 	strQuery = null;
			if (rs != null)     try{rs.close();}catch (Exception ex){ex.printStackTrace();}
			if (pstmt != null)  try{pstmt.close();}catch (Exception ex2){ex2.printStackTrace();}
			if (rs2 != null)     try{rs2.close();}catch (Exception ex){ex.printStackTrace();}
			if (pstmt2 != null)  try{pstmt2.close();}catch (Exception ex2){ex2.printStackTrace();}
			if (pstmt3 != null)  try{pstmt3.close();}catch (Exception ex2){ex2.printStackTrace();}
			if (conn != null){
				try{					
					ConnectionResource.release(conn);
					conn = null;
				}catch(Exception ex3){
					ecamsLogger.error("## Cmr0200.request_Check_In() connection release exception ##");
					ex3.printStackTrace();
				}
			}
			if (connD != null){
				try{					
					ConnectionResource.release(connD);
					connD = null;
				}catch(Exception ex3){
					ecamsLogger.error("## Cmr0200.request_Check_In() connection release exception ##");
					ex3.printStackTrace();
				}
			}
		}		
	}

	public String request_Confirm(String AcptNo,String SysCd,String ReqCd,String UserId,boolean confSw,ArrayList<HashMap<String,Object>> ConfList,Connection conn) throws SQLException, Exception {
		
		PreparedStatement pstmt       = null;
		PreparedStatement pstmt2      = null;
		ResultSet         rs          = null;
		StringBuffer      strQuery    = new StringBuffer();
		int               i = 0;
		int               pstmtcount = 0;
		int               SeqNo = 0;
		ArrayList<LinkedTreeMap<String, Object>> rData2 = null;
		try {
        	if (confSw == true) {
        		for (i=0;i<ConfList.size();i++){
    	        	if (ConfList.get(i).get("cm_congbn").equals("1") || ConfList.get(i).get("cm_congbn").equals("2") ||
    	        		ConfList.get(i).get("cm_congbn").equals("3") ||	ConfList.get(i).get("cm_congbn").equals("4") ||	
    	        		ConfList.get(i).get("cm_congbn").equals("Z") ||	ConfList.get(i).get("cm_congbn").equals("X") ||
    	        		ConfList.get(i).get("cm_congbn").equals("5") || ConfList.get(i).get("cm_congbn").equals("6")) {
    	        		if (ConfList.get(i).get("cm_gubun").equals("8") && 
    	        			(ConfList.get(i).get("cm_baseuser") == null || "".equals(ConfList.get(i).get("cm_baseuser")))) {
    	        			strQuery.setLength(0);
        		        	strQuery.append("insert into cmr9900                                               \n");
        		        	strQuery.append("      (CR_ACPTNO,CR_SEQNO,CR_LOCAT,CR_CONFNAME,CR_TEAM,CR_TEAMCD, \n");
        		        	strQuery.append("       CR_STATUS,CR_CONGBN,CR_COMMON, CR_BLANK,CR_EMGER,CR_HOLI,  \n");
        		        	strQuery.append("       CR_SGNGBN,CR_ORGSTEP,CR_BASEUSR,CR_PRCSW,CR_QRYCD)         \n");
        		        	strQuery.append("(select c.cr_acptno, 1, lpad(?+rownum,2,'0'), ?, a.cm_userid, ?,  \n");
        		        	strQuery.append("        '0', ?, ?, ?, ?, ?, ?, ?, a.cm_userid, ?, ?               \n");
        		        	strQuery.append("   from cmm0043 b,cmm0040 a,cmr1000 c                             \n");
        		        	strQuery.append("  where c.cr_acptno=? and c.cr_teamcd<>a.cm_project               \n");
        		        	strQuery.append("    and a.cm_active='1' and a.cm_userid=b.cm_userid               \n");
        		        	strQuery.append("    and b.cm_rgtcd='61')                                          \n");
    	        		} else {
    	        			strQuery.setLength(0);
        		        	strQuery.append("insert into cmr9900                                               \n");
        		        	strQuery.append("      (CR_ACPTNO,CR_SEQNO,CR_LOCAT,CR_CONFNAME,CR_TEAM,CR_TEAMCD, \n");
        		        	strQuery.append("       CR_STATUS,CR_CONGBN,CR_COMMON, CR_BLANK,CR_EMGER,CR_HOLI,  \n");
        		        	strQuery.append("       CR_SGNGBN,CR_ORGSTEP,CR_BASEUSR,CR_PRCSW,CR_QRYCD)         \n");
        		        	strQuery.append("values                                                            \n");
        		        	strQuery.append("(?, 1, lpad(?,2,'0'), ?, ?, ?, '0', ?, ?, ?, ?, ?, ?, ?, ?, ?, ? ) \n");
    	        		}   	
    		        	pstmt = conn.prepareStatement(strQuery.toString());
    		        	pstmt = new LoggableStatement(conn,strQuery.toString());
    		        	
    		        	pstmtcount = 0;
    	        	    if (!ConfList.get(i).get("cm_gubun").equals("8") || (ConfList.get(i).get("cm_gubun").equals("8") && 
            	        	ConfList.get(i).get("cm_baseuser") != null && !"".equals(ConfList.get(i).get("cm_baseuser")))) {
    		        		pstmt.setString(++pstmtcount, AcptNo);	        	    
    		        	}
    	        	    pstmt.setInt(++pstmtcount, ++SeqNo);
    	        	    ecamsLogger.error("###########$$$$$$$$$$$$$$$$"+ConfList.get(i).get("cm_name"));
    	        	    pstmt.setString(++pstmtcount, (String) ConfList.get(i).get("cm_name"));

    	        	    if (!ConfList.get(i).get("cm_gubun").equals("8") || (ConfList.get(i).get("cm_gubun").equals("8") && 
        	        		ConfList.get(i).get("cm_baseuser") != null && !"".equals(ConfList.get(i).get("cm_baseuser")))) {
    	        	    	rData2 = (ArrayList<LinkedTreeMap<String, Object>>) ConfList.get(i).get("arysv");
	    					pstmt.setString(++pstmtcount, (String) rData2.get(0).get("SvUser"));
	    					rData2 = null;
    	        	    }
    	        	    if (ConfList.get(i).get("cm_gubun").equals("C")){
    	        	    	pstmt.setString(++pstmtcount,"3");
    	        	    } else {
    	        	    	pstmt.setString(++pstmtcount, (String) ConfList.get(i).get("cm_gubun"));
    	        	    }
    	        	    pstmt.setString(++pstmtcount, (String) ConfList.get(i).get("cm_congbn"));
    	        	    pstmt.setString(++pstmtcount, (String) ConfList.get(i).get("cm_common"));
    	        	    pstmt.setString(++pstmtcount, (String) ConfList.get(i).get("cm_blank"));
    	        	    pstmt.setString(++pstmtcount, (String) ConfList.get(i).get("cm_emg"));
    	        	    pstmt.setString(++pstmtcount, (String) ConfList.get(i).get("cm_holi"));
    	        	    pstmt.setString(++pstmtcount, (String) ConfList.get(i).get("cm_duty"));
    	        	    pstmt.setString(++pstmtcount, (String) ConfList.get(i).get("cm_orgstep"));  

    	        	    if (!ConfList.get(i).get("cm_gubun").equals("8") || (ConfList.get(i).get("cm_gubun").equals("8") && 
        	        		ConfList.get(i).get("cm_baseuser") != null && !"".equals(ConfList.get(i).get("cm_baseuser")))) {
    	        	    	pstmt.setString(++pstmtcount, (String) ConfList.get(i).get("cm_baseuser"));   
    	        	    }
    	        	    pstmt.setString(++pstmtcount, (String) ConfList.get(i).get("cm_prcsw")); 
    	        	    pstmt.setString(++pstmtcount, ReqCd);  
    	        	    if (ConfList.get(i).get("cm_gubun").equals("8") && 
        	        		(ConfList.get(i).get("cm_baseuser") == null || "".equals(ConfList.get(i).get("cm_baseuser")))) {
    	        	       pstmt.setString(++pstmtcount, AcptNo);	
    	        	    }
    	        	    ecamsLogger.error(((LoggableStatement)pstmt).getQueryString());
    		        	pstmt.executeUpdate();
    		        	pstmt.close();  
    		        	
    		        	if (ConfList.get(i).get("cm_gubun").equals("8") && 
            	        	(ConfList.get(i).get("cm_baseuser") == null || "".equals(ConfList.get(i).get("cm_baseuser")))) {
    		        		strQuery.setLength(0);
    		        		strQuery.append("select max(cr_locat) max from cmr9900   \n");
    		        		strQuery.append(" where cr_acptno=?                      \n");
    		        		pstmt = conn.prepareStatement(strQuery.toString());
    		        		pstmt.setString(1, AcptNo);
    		        		rs = pstmt.executeQuery();
    		        		if (rs.next()) {
    		        			SeqNo = rs.getInt("max");
    		        		}
    		        		rs.close();
    		        		pstmt.close();    		        		

    		        		strQuery.setLength(0);
    		        		strQuery.append("select c.cm_daeusr,b.cr_locat,c.cm_blankcd \n");
    		        		strQuery.append("  from cmm0040 a,cmr9900 b,cmm0042 c     \n");
    		        		strQuery.append(" where b.cr_acptno=? and b.cr_teamcd='8' \n");
    		        		strQuery.append("   and b.cr_team=a.cm_userid             \n");
    		        		strQuery.append("   and a.cm_userid=c.cm_userid(+)        \n");
    		        		strQuery.append("   and a.cm_status='9'                   \n");
    		        		strQuery.append("   and c.cm_daeusr(+) is not null          \n");
    		        		strQuery.append("   and c.cm_blkstdate(+) is not null         \n");
    		        		strQuery.append("   and c.cm_blkstdate(+)<=to_char(SYSDATE,'yyyymmdd') \n");
    		        		strQuery.append("   and c.cm_blkeddate(+)>=to_char(SYSDATE,'yyyymmdd') \n");
    		        		pstmt = conn.prepareStatement(strQuery.toString());
    		        		//pstmt = new LoggableStatement(conn,strQuery.toString());
    		        		pstmt.setString(1, AcptNo);
    		        		////ecamsLogger.error(((LoggableStatement)pstmt).getQueryString());
    		        		rs = pstmt.executeQuery();
    		        		while (rs.next()) {
    		        			strQuery.setLength(0);
    		        			strQuery.append("update cmr9900 set cr_team=?,        \n");
    		        			strQuery.append("       cr_blankcd=?                  \n");
    		        			strQuery.append(" where cr_acptno=? and cr_locat=?    \n");
    		        			pstmt2 = conn.prepareStatement(strQuery.toString());
    		  //      			pstmt = new LoggableStatement(conn,strQuery.toString());
    		        			pstmt2.setString(1, rs.getString("cm_daeusr"));
    		        			pstmt2.setString(2, rs.getString("cm_blankcd"));
    		        			pstmt2.setString(3, AcptNo);
    		        			pstmt2.setString(4, rs.getString("cr_locat"));
    		//        			//ecamsLogger.error(((LoggableStatement)pstmt).getQueryString());
    		        			pstmt2.executeUpdate();    	
    		        			pstmt2.close();
    		        		}
    		        		rs.close();
    		        		pstmt.close();
    		        	}
    		        }
            	} 
        	} else {
	        	strQuery.setLength(0);
	        	strQuery.append("insert into cmr9900 ");
	        	strQuery.append("(CR_ACPTNO,CR_SEQNO,CR_LOCAT,CR_CONFNAME,CR_TEAM,CR_TEAMCD,CR_STATUS,CR_CONGBN, ");
	        	strQuery.append("CR_COMMON, CR_BLANK,CR_EMGER,CR_HOLI,CR_SGNGBN,CR_ORGSTEP,CR_BASEUSR, CR_PRCSW,CR_QRYCD) ");
	        	strQuery.append("(SELECT ?,1,lpad(a.CM_seqno,2,'0'),a.CM_NAME,a.CM_JOBCD,a.CM_GUBUN, ");
	        	strQuery.append("'0',a.CM_COMMON,a.CM_COMMON,a.CM_BLANK,a.CM_EMG,a.CM_HOLIDAY,");
	        	strQuery.append("a.CM_POSITION,a.CM_ORGSTEP,a.CM_JOBCD,a.CM_PRCSW,? ");
	        	strQuery.append("FROM CMm0060 a,cmm0040 b ");
	        	strQuery.append("WHERE a.CM_SYSCD= ? ");
	        	strQuery.append("AND a.CM_REQCD= ? and b.cm_userid=?  ");
	        	strQuery.append("AND a.CM_MANID=decode(b.cm_manid,'N','2','1') ");	 
	        	if (!ReqCd.equals("16"))
	        		strQuery.append("AND CM_GUBUN='1') ");
	        	else strQuery.append(") ");
	
	        	////pstmt =  new LoggableStatement(conn, strQuery.toString());
	        	pstmt = conn.prepareStatement(strQuery.toString());
	        	pstmtcount = 1;			
	        	pstmt.setString(pstmtcount++, AcptNo);
	        	pstmt.setString(pstmtcount++, ReqCd);
	        	pstmt.setString(pstmtcount++, SysCd);
	        	pstmt.setString(pstmtcount++, ReqCd);
	        	pstmt.setString(pstmtcount++, UserId);
	        	////ecamsLogger.error(((LoggableStatement)pstmt).getQueryString());
	        	pstmt.executeUpdate();
	        	pstmt.close();


	        	strQuery.setLength(0);
	        	strQuery.append("update cmr9900 set cr_team=cr_sgngbn             \n");
	        	strQuery.append(" where cr_acptno=? and cr_teamcd='4'              \n");
	        	pstmt = conn.prepareStatement(strQuery.toString());
	        	pstmtcount = 1;
	        	pstmt.setString(pstmtcount++, AcptNo);        	
	        	pstmt.executeUpdate();
	        	pstmt.close();
	        	

	        	strQuery.setLength(0);
	        	strQuery.append("update cmr9900 set cr_team=?,cr_baseusr=?         \n");
	        	strQuery.append(" where cr_acptno=? and cr_teamcd='2'              \n");
	        	pstmt = conn.prepareStatement(strQuery.toString());
	        	pstmtcount = 1;
	        	pstmt.setString(pstmtcount++, UserId); 
	        	pstmt.setString(pstmtcount++, UserId); 
	        	pstmt.setString(pstmtcount++, AcptNo);        	
	        	pstmt.executeUpdate();
	        	pstmt.close();
        	}
        	
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
			
        	boolean findSw = false;        	
        	if (ReqCd.equals("16")) {
            	findSw = false;
        		strQuery.setLength(0);
        		strQuery.append("select sum(cnt) cnt from                       \n");
        		strQuery.append("(select count(*) cnt from cmr1010 a,cmm0036 b  \n");
        		strQuery.append(" where a.cr_acptno=?                           \n");
        		strQuery.append("   and a.cr_syscd=b.cm_syscd                   \n");
        		strQuery.append("   and a.cr_rsrccd=b.cm_rsrccd                 \n");
        		strQuery.append("   and (substr(b.cm_info,1,1)='1' or           \n");
        		strQuery.append("       substr(b.cm_info,25,1)='1')             \n");
        		strQuery.append(" union                                         \n");
        		strQuery.append("select count(*) cnt from cmr1010 a,cmm0036 b,cmm0037 c \n");
        		strQuery.append(" where a.cr_acptno=?                           \n");
        		strQuery.append("   and a.cr_syscd=c.cm_syscd                   \n");
        		strQuery.append("   and a.cr_rsrccd=c.cm_rsrccd                 \n");
        		strQuery.append("   and c.cm_syscd=b.cm_syscd                   \n");
        		strQuery.append("   and c.cm_samersrc=b.cm_rsrccd               \n");
        		strQuery.append("   and (substr(b.cm_info,1,1)='1' or           \n");
        		strQuery.append("       substr(b.cm_info,25,1)='1'))            \n");
        		pstmt = conn.prepareStatement(strQuery.toString());
            	pstmtcount = 1;
            	pstmt.setString(pstmtcount++, AcptNo);
            	pstmt.setString(pstmtcount++, AcptNo);
            	rs = pstmt.executeQuery();
            	if (rs.next()) {
            		if (rs.getString("cnt") == null) findSw = true;
            		else if (rs.getInt("cnt") == 0) findSw = true;
            	}
            	rs.close();
            	pstmt.close();
            	
            	if (findSw == true) {
	            	strQuery.setLength(0);
	            	strQuery.append("delete cmr9900 where cr_acptno=? and cr_teamcd='1' and cr_team='SYSCB' ");
	            	pstmt = conn.prepareStatement(strQuery.toString());	
	            	pstmt.setString(1, AcptNo);
	            	pstmt.executeUpdate();
	            	pstmt.close();
	            	
	            	strQuery.setLength(0);
	            	strQuery.append("delete cmr9900 where cr_acptno=? and cr_teamcd='1' and cr_team='SYSGB' ");
	            	pstmt = conn.prepareStatement(strQuery.toString());	
	            	pstmt.setString(1, AcptNo);
	            	pstmt.executeUpdate();
	            	pstmt.close();
            	}
            	

            	findSw = false;
        		strQuery.setLength(0);
        		strQuery.append("select sum(cnt) cnt from                       \n");
        		strQuery.append("(select count(*) cnt from cmr1010 a,cmm0036 b  \n");
        		strQuery.append(" where a.cr_acptno=?                           \n");
        		strQuery.append("   and a.cr_syscd=b.cm_syscd                   \n");
        		strQuery.append("   and a.cr_rsrccd=b.cm_rsrccd                 \n");
        		strQuery.append("   and (substr(b.cm_info,11,1)='1' or          \n");
        		strQuery.append("       substr(b.cm_info,21,1)='1')             \n");
        		strQuery.append(" union                                         \n");
        		strQuery.append("select count(*) cnt from cmr1010 a,cmm0036 b,cmm0037 c \n");
        		strQuery.append(" where a.cr_acptno=?                           \n");
        		strQuery.append("   and a.cr_syscd=c.cm_syscd                   \n");
        		strQuery.append("   and a.cr_rsrccd=c.cm_rsrccd                 \n");
        		strQuery.append("   and c.cm_syscd=b.cm_syscd                   \n");
        		strQuery.append("   and c.cm_samersrc=b.cm_rsrccd               \n");
        		strQuery.append("   and (substr(b.cm_info,11,1)='1' or          \n");
        		strQuery.append("       substr(b.cm_info,21,1)='1'))            \n");
        		pstmt = conn.prepareStatement(strQuery.toString());
            	pstmtcount = 1;
            	pstmt.setString(pstmtcount++, AcptNo);
            	pstmt.setString(pstmtcount++, AcptNo);
            	rs = pstmt.executeQuery();
            	if (rs.next()) {
            		if (rs.getString("cnt") == null) findSw = true;
            		else if (rs.getInt("cnt") == 0) findSw = true;
            	}
            	rs.close();
            	pstmt.close();
            	
            	if (findSw == true) {
	            	strQuery.setLength(0);
	            	strQuery.append("delete cmr9900 where cr_acptno=? and cr_teamcd='1' and cr_team='SYSED' ");
	            	pstmt = conn.prepareStatement(strQuery.toString());	
	            	pstmt.setString(1, AcptNo);
	            	pstmt.executeUpdate();
	            	pstmt.close();
            	}

            	findSw = false;
        		strQuery.setLength(0);
        		strQuery.append("select sum(cnt) cnt from                       \n");
        		strQuery.append("(select count(*) cnt from cmr1010 a,cmm0036 b  \n");
        		strQuery.append(" where a.cr_acptno=?                           \n");
        		strQuery.append("   and a.cr_syscd=b.cm_syscd                   \n");
        		strQuery.append("   and a.cr_rsrccd=b.cm_rsrccd                 \n");
        		strQuery.append("   and substr(b.cm_info,35,1)='1'              \n");
        		strQuery.append(" union                                         \n");
        		strQuery.append("select count(*) cnt from cmr1010 a,cmm0036 b,cmm0037 c \n");
        		strQuery.append(" where a.cr_acptno=?                           \n");
        		strQuery.append("   and a.cr_syscd=c.cm_syscd                   \n");
        		strQuery.append("   and a.cr_rsrccd=c.cm_rsrccd                 \n");
        		strQuery.append("   and c.cm_syscd=b.cm_syscd                   \n");
        		strQuery.append("   and c.cm_samersrc=b.cm_rsrccd               \n");
        		strQuery.append("   and substr(b.cm_info,35,1)='1')             \n");
        		pstmt = conn.prepareStatement(strQuery.toString());
            	pstmtcount = 1;
            	pstmt.setString(pstmtcount++, AcptNo);
            	pstmt.setString(pstmtcount++, AcptNo);
            	rs = pstmt.executeQuery();
            	if (rs.next()) {
            		if (rs.getString("cnt") == null) findSw = true;
            		else if (rs.getInt("cnt") == 0) findSw = true;
            	}
            	rs.close();
            	pstmt.close();
            	
            	if (findSw == true) {
	            	strQuery.setLength(0);
	            	strQuery.append("delete cmr9900 where cr_acptno=? and cr_teamcd='1' and cr_team='SYSRC' ");
	            	pstmt = conn.prepareStatement(strQuery.toString());	
	            	pstmt.setString(1, AcptNo);
	            	pstmt.executeUpdate();
	            	pstmt.close();
            	}
            	
        	}
        	
        
        	
        	
        	strQuery.setLength(0);        	
        	strQuery.append("Begin CMR9900_STR ( ");
        	strQuery.append("?, '000000', '', '9', ?, '1' ); End;");
        	pstmt = conn.prepareStatement(strQuery.toString());
        	pstmtcount = 1;
        	pstmt.setString(pstmtcount++, AcptNo);
        	pstmt.setString(pstmtcount++, ReqCd);
        	
        	pstmt.executeUpdate();
        	pstmt.close();
        	rs = null;
        	pstmt = null;
        	
        	//ecamsLogger.error("+++++++++Request E N D+++CMR0200");
        	
        	return "OK";
			
		} catch (SQLException sqlexception) {
			sqlexception.printStackTrace();
			ecamsLogger.error("## Cmr0200.request_Confirm() SQLException START ##");
			ecamsLogger.error("## Error DESC : ", sqlexception);	
			ecamsLogger.error("## Cmr0200.request_Confirm() SQLException END ##");			
			throw sqlexception;
		} catch (Exception exception) {
			exception.printStackTrace();
			ecamsLogger.error("## Cmr0200.request_Confirm() Exception START ##");				
			ecamsLogger.error("## Error DESC : ", exception);	
			ecamsLogger.error("## Cmr0200.request_Confirm() Exception END ##");				
			throw exception;
		}finally{
			if (strQuery != null) 	strQuery = null;
			if (rs != null)     try{rs.close();}catch (Exception ex){ex.printStackTrace();}
			if (pstmt != null)  try{pstmt.close();}catch (Exception ex2){ex2.printStackTrace();}
			if (pstmt2 != null)  try{pstmt2.close();}catch (Exception ex2){ex2.printStackTrace();}
		}		
	}
	public Object[] pgmCheck(String UserId,String SysCd,String DsnCd,String RsrcName,String RsrcCd,String JobCd,String LangCd,String ProgTit,String DirPath) throws SQLException, Exception {
		Connection        conn        = null;
		PreparedStatement pstmt       = null;
		ResultSet         rs          = null;
		StringBuffer      strQuery    = new StringBuffer();
		String            retMsg      = "0";	
		String            BaseItem    = "";
		Object[] returnObjectArray    = null;
		ArrayList<HashMap<String, String>>  rsval = new ArrayList<HashMap<String, String>>();
		HashMap<String, String>			  rst		  = null;	
		ConnectionContext connectionContext = new ConnectionResource();		
		
		try {
			conn = connectionContext.getConnection();
						
			strQuery.append("select a.cr_status,a.cr_lstver,a.cr_editor,b.cm_codename \n");
			strQuery.append("  from cmm0020 b,cmr0020 a                               \n");
			strQuery.append(" where a.cr_syscd=? and a.cr_dsncd=?                     \n");
			strQuery.append("   and a.cr_rsrcname=?                                   \n");
			strQuery.append("   and b.cm_macode='CMR0020' and b.cm_micode=a.cr_status \n");
						
	        pstmt = conn.prepareStatement(strQuery.toString());	
	        pstmt.setString(1, SysCd);   	
	        pstmt.setString(2, DsnCd);        		
	        pstmt.setString(3, RsrcName);        	     	
	        rs = pstmt.executeQuery();
	        rsval.clear();
	        if (rs.next() == true) {
	        	if (rs.getString("cr_status").equals("3") && !rs.getString("cr_editor").equals(UserId))
	        		retMsg = "다른 사용자가 신규등록한 프로그램ID입니다.";
	        	else if (rs.getString("cr_status").equals("3")) retMsg = "0";
	        	else retMsg = "기 등록하여 운영 중인 프로그램ID입니다.";
	        }
	        
	        if (retMsg == "0") {
	        	retMsg = null;
	        	retMsg = cmr0020_Insert(UserId,SysCd,DsnCd,RsrcName,RsrcCd,JobCd,LangCd,ProgTit,"");
	        	if (retMsg.substring(0,1).equals("0")) {
	        		BaseItem = retMsg.substring(1);
	        		retMsg = "";
	        		retMsg = moduleChk(UserId,SysCd,DsnCd,RsrcName,RsrcCd,JobCd,LangCd, ProgTit,DirPath,BaseItem);
	        	    
	        	}
	        }
	        
	        rs.close();
	        pstmt.close();
	        conn.close();
	        
	        rs = null;
	        pstmt = null;
	        conn = null;
	    	
			rst = new HashMap<String, String>();
			rst.put("retMsg", retMsg);
			rst = null;
			rsval.add(rst);
			
			returnObjectArray = rsval.toArray();
			rsval.clear();
			rsval = null;
			//ecamsLogger.error(rsval.toString());		
			return returnObjectArray;
			
			
		} catch (SQLException sqlexception) {
			sqlexception.printStackTrace();
			ecamsLogger.error("## Cmr0200.pgmCheck() SQLException START ##");
			ecamsLogger.error("## Error DESC : ", sqlexception);	
			ecamsLogger.error("## Cmr0200.pgmCheck() SQLException END ##");			
			throw sqlexception;
		} catch (Exception exception) {
			exception.printStackTrace();
			ecamsLogger.error("## Cmr0200.pgmCheck() Exception START ##");				
			ecamsLogger.error("## Error DESC : ", exception);	
			ecamsLogger.error("## Cmr0200.pgmCheck() Exception END ##");				
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
					ecamsLogger.error("## Cmr0200.pgmCheck() connection release exception ##");
					ex3.printStackTrace();
				}
			}
		}
	}//end of pgmCheck() method statement
	
	public String selBaseno(String ItemId,Connection conn) throws SQLException, Exception {
		PreparedStatement pstmt       = null;
		ResultSet         rs          = null;
		StringBuffer      strQuery    = new StringBuffer();
		String            baseAcpt    = "";
		
		try {
						
			strQuery.append("select cr_acptno from cmr0021         \n");
			strQuery.append(" where cr_itemid=?                    \n");
			strQuery.append(" order by cr_acptdate desc            \n");
						
	        pstmt = conn.prepareStatement(strQuery.toString());	
	        pstmt.setString(1, ItemId);        	     	
	        rs = pstmt.executeQuery();
	        if (rs.next()) {
	        	baseAcpt = rs.getString("cr_acptno");
	        }        
	        rs.close();
	        pstmt.close();
	        
	        return baseAcpt;		
			
		} catch (SQLException sqlexception) {
			sqlexception.printStackTrace();
			ecamsLogger.error("## Cmr0200.selBaseno() SQLException START ##");
			ecamsLogger.error("## Error DESC : ", sqlexception);	
			ecamsLogger.error("## Cmr0200.selBaseno() SQLException END ##");			
			throw sqlexception;
		} catch (Exception exception) {
			exception.printStackTrace();
			ecamsLogger.error("## Cmr0200.selBaseno() Exception START ##");				
			ecamsLogger.error("## Error DESC : ", exception);	
			ecamsLogger.error("## Cmr0200.selBaseno() Exception END ##");				
			throw exception;
		}finally{
			if (strQuery != null) 	strQuery = null;
			if (rs != null)     try{rs.close();}catch (Exception ex){ex.printStackTrace();}
			if (pstmt != null)  try{pstmt.close();}catch (Exception ex2){ex2.printStackTrace();}
		}
	}//end of selBaseno() method statement
	
	public String moduleChk(String UserId,String SysCd,String DsnCd,String RsrcName,String RsrcCd,String JobCd,String LangCd,String ProgTit,String DirPath,String BaseItem) throws SQLException, Exception {
		Connection        conn        = null;
		PreparedStatement pstmt       = null;
		PreparedStatement pstmt2       = null;
		ResultSet         rs          = null;
		ResultSet         rs2         = null;
		StringBuffer      strQuery    = new StringBuffer();
		String            retMsg      = "0";
		String            strBefDsn   = null;
		String            strAftDsn   = null;
		String            strWork1    = null;
		String            strWork3    = null;
		String            strDevPath  = null;
		String            strRsrcCd   = null;
		String            strRsrcName = null;
		int               j           = 0;
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
	        	if (!rs.getString("cm_basename").equals("*")) {
	        		strWork3 = rs.getString("cm_basename");
	        		while (strWork3 == "") {
	        			j = strWork3.indexOf("*");
	        			if (j > -1) {
	        				strWork3 = strWork3.substring(j + 1);
	        				if (strWork3.equals("*")) strWork3 = "";
	        			} else {
	        				strWork3 = "";
	        			}
	        			if (strWork3 == "") break;
	        		}
	        	}
	        	if (rs.getString("cm_cmdyn").equals("Y")) {
	        		strWork1 = rs.getString("cm_samename").replace("*",strWork1);
	        		
	        		strQuery.setLength(0);                		
	        		strQuery.append("select ? relatId  from dual                           \n"); 
	        		pstmt2 = conn.prepareStatement(strQuery.toString());	
	        		pstmt2.setString(1, strWork1); 
	    	        rs2 = pstmt2.executeQuery();
	    	        if (rs2.next()) {
	    	        	strWork1 = rs2.getString("relatId");
	    	        }
	    	        
	    	        rs2.close();
	    	        pstmt2.close();
	    	        
	        	} 
	        	else{
	        		strWork1 = rs.getString("cm_samename").replace("*",strWork1);
	        	}
	        	
	        	strRsrcName = strWork1;
	        	strRsrcCd = rs.getString("cm_samersrc");
	        	if (rs.getString("cm_samedir") != null){
	        		strAftDsn = rs.getString("cm_samedir");
	        	}
	        	
	        	if (rs.getString("cm_samersrc").equals("52")) {
	        		strQuery.setLength(0);                		
	        		strQuery.append("select a.cm_volpath  from cmm0038 a,cmm0031 b  \n");               		
	        		strQuery.append(" where a.cm_syscd=? and a.cm_svrcd='01'        \n");               		
	        		strQuery.append("   and a.cm_rsrccd=? and a.cm_syscd=b.cm_syscd \n");               		
	        		strQuery.append("   and a.cm_svrcd=b.cm_svrcd and a.cm_seqno=b.cm_seqno  \n");               		
	        		strQuery.append("   and b.cm_closedt is null                    \n"); 
	        		pstmt2 = conn.prepareStatement(strQuery.toString());	
	        		pstmt2.setString(1, SysCd); 
	        		pstmt2.setString(2, strRsrcCd); 
	    	        rs2 = pstmt2.executeQuery();
	    	        if (rs2.next()) {
	    	        	strDevPath = rs2.getString("cm_volpath");
	    	        }
	    	        rs2.close();
	    	        pstmt2.close();
	    	        
	    	        
	        	} 
	        	else{
	        		strDevPath = DirPath.replace(strBefDsn, strAftDsn);
	        	}
	        	
	        	strQuery.setLength(0);                		
        		strQuery.append("select cm_dsncd from cmm0070            \n");               		
        		strQuery.append(" where cm_syscd=? and cm_dirpath=?      \n"); 
        		pstmt2 = conn.prepareStatement(strQuery.toString());	
        		pstmt2.setString(1, SysCd); 
        		pstmt2.setString(2, strDevPath); 
    	        rs2 = pstmt2.executeQuery();
    	        if (rs2.next()) {
    	        	strAftDsn = rs2.getString("cm_dsncd");
    	        } 
    	        else {
    	        	retMsg = "[" + strDevPath + "]에 대한 디렉토리 등록을 관리자에게 요청한 후 처리하십시오.";
    	        }
    	        
    	        rs2.close();
    	        pstmt2.close();
    	        
    	        
    	        if (retMsg == "0") {
    	        	//ecamsLogger.error("+++++++++++++++rsrcname=========>"+strRsrcName);	
    	        	retMsg = cmr0020_Insert(UserId,SysCd,strAftDsn,strRsrcName,strRsrcCd,JobCd,LangCd,ProgTit,BaseItem) ;
    	        }
    	        
	        }
	        
	        rs.close();
	        pstmt.close();
	        conn.close();
	        
	        rs = null;
	        pstmt = null;
	        conn = null;
	        
	        return retMsg;
	        
	        
		} catch (SQLException sqlexception) {
			sqlexception.printStackTrace();
			ecamsLogger.error("## Cmr0200.moduleChk() SQLException START ##");
			ecamsLogger.error("## Error DESC : ", sqlexception);	
			ecamsLogger.error("## Cmr0200.moduleChk() SQLException END ##");			
			throw sqlexception;
		} catch (Exception exception) {
			exception.printStackTrace();
			ecamsLogger.error("## Cmr0200.moduleChk() Exception START ##");				
			ecamsLogger.error("## Error DESC : ", exception);	
			ecamsLogger.error("## Cmr0200.moduleChk() Exception END ##");				
			throw exception;
		}finally{
			if (strQuery != null) 	strQuery = null;
			if (rs != null)     try{rs.close();}catch (Exception ex){ex.printStackTrace();}
			if (pstmt != null)  try{pstmt.close();}catch (Exception ex2){ex2.printStackTrace();}
			if (conn != null){
				try{
					ConnectionResource.release(conn);
				}catch(Exception ex3){
					ecamsLogger.error("## Cmr0200.moduleChk() connection release exception ##");
					ex3.printStackTrace();
				}
			}
		}		
			
	}//end of moduleChk() method statement	

	public Object[]  moduleChk_new(ArrayList<HashMap<String,String>> fileList) throws SQLException, Exception {
		Connection        conn        = null;
		PreparedStatement pstmt       = null;
		ResultSet         rs          = null;
		StringBuffer      strQuery    = new StringBuffer();
		ArrayList<HashMap<String, String>>		  rtList	  = new ArrayList<HashMap<String, String>>();
		HashMap<String, String>			  rst		  = null;
		ConnectionContext connectionContext = new ConnectionResource();		
		
		try {
			conn = connectionContext.getConnection();
		    rtList.clear();
			for (int i=0;fileList.size()>i;i++) {
				rst = new HashMap<String, String>();
				rst = fileList.get(i);
        		rst.put("moderr", "0");
				if (fileList.get(i).get("cm_info").substring(8,9).equals("1")) {
					strQuery.setLength(0);
					strQuery.append("select count(*) cnt                                      \n");
					strQuery.append("  from cmm0037 b,cmd0011 c,cmr0020 d                     \n");
					strQuery.append(" where c.cd_itemid=?                                     \n");
					strQuery.append("   and c.cd_prcitem=d.cr_itemid                          \n");
					strQuery.append("   and d.cr_syscd=b.cm_syscd                             \n");
					strQuery.append("   and d.cr_rsrccd=b.cm_samersrc                         \n");
					strQuery.append("   and b.cm_factcd='09'                                  \n");
					strQuery.append("   and d.cr_status<>'9'                                  \n");
							
			        pstmt = conn.prepareStatement(strQuery.toString());	
			        //pstmt = new LoggableStatement(conn,strQuery.toString());		            
			        pstmt.setString(1, fileList.get(i).get("cr_itemid")); 
			        ////ecamsLogger.error(((LoggableStatement)pstmt).getQueryString());  
			        rs = pstmt.executeQuery();		        
			        if (rs.next()) {
			        	if (rs.getInt("cnt") == 0) {
			        		rst.put("moderr", "1");
			        	}
			        }
			        rs.close();
			        pstmt.close();
				}
				rtList.add(rst);
			}
	        conn.close();
	        
	        rs = null;
	        pstmt = null;
	        conn = null;
	        
	        return rtList.toArray();
	        
	        
		} catch (SQLException sqlexception) {
			sqlexception.printStackTrace();
			ecamsLogger.error("## Cmr0200.moduleChk_new() SQLException START ##");
			ecamsLogger.error("## Error DESC : ", sqlexception);	
			ecamsLogger.error("## Cmr0200.moduleChk_new() SQLException END ##");			
			throw sqlexception;
		} catch (Exception exception) {
			exception.printStackTrace();
			ecamsLogger.error("## Cmr0200.moduleChk_new() Exception START ##");				
			ecamsLogger.error("## Error DESC : ", exception);	
			ecamsLogger.error("## Cmr0200.moduleChk_new() Exception END ##");				
			throw exception;
		}finally{
			if (strQuery != null) 	strQuery = null;
			if (rs != null)     try{rs.close();}catch (Exception ex){ex.printStackTrace();}
			if (pstmt != null)  try{pstmt.close();}catch (Exception ex2){ex2.printStackTrace();}
			if (conn != null){
				try{
					ConnectionResource.release(conn);
				}catch(Exception ex3){
					ecamsLogger.error("## Cmr0200.moduleChk_new() connection release exception ##");
					ex3.printStackTrace();
				}
			}
		}		
			
	}//end of moduleChk_new() method statement	
	public String nameChange(HashMap<String,String> etcData,Connection conn) throws SQLException, Exception {
		PreparedStatement pstmt       = null;
		ResultSet         rs          = null;
		StringBuffer      strQuery    = new StringBuffer();
		String            retMsg      = "0";
		String            strWork1    = null;
		String            strWork2    = null;
		//String            strWork3    = null;
		int               j           = 0;
		
		try {
				
    	    strWork1 = etcData.get("cr_samename");
	        j = strWork1.indexOf("?#");
	        if (j>=0) {
	        	strWork1 = strWork1.substring(j+2);
	        } else return "ERROR";
	        
	        j = strWork1.indexOf("#");
	        if (j>0) {
	        	strWork2 = "?#" + strWork1.substring(2,j+1);
	        } else return "ERROR";
	        
	        if (strWork2.equals("?#TABLENM#")) {
	        	strWork2 = etcData.get("cr_samename");
	        	j = etcData.get("cr_rsrcname").lastIndexOf("_");
	        	if (j>0) {
	        		strWork1 = etcData.get("cr_rsrcname").substring(0,j);
	        	} else return "ERROR";
	        	//strWork3 = etcData.get("cm_samename").replace(strWork2, strWork1);
	        }
	        return retMsg;
	        
	        
		} /*catch (SQLException sqlexception) {
			sqlexception.printStackTrace();
			ecamsLogger.error("## Cmr0200.nameChange() SQLException START ##");
			ecamsLogger.error("## Error DESC : ", sqlexception);	
			ecamsLogger.error("## Cmr0200.moduleChk() SQLException END ##");			
			throw sqlexception;
		}*/
		catch (Exception exception) {
			exception.printStackTrace();
			ecamsLogger.error("## Cmr0200.nameChange() Exception START ##");				
			ecamsLogger.error("## Error DESC : ", exception);	
			ecamsLogger.error("## Cmr0200.nameChange() Exception END ##");				
			throw exception;
		}finally{
			if (strQuery != null) 	strQuery = null;
			if (rs != null)     try{rs.close();}catch (Exception ex){ex.printStackTrace();}
			if (pstmt != null)  try{pstmt.close();}catch (Exception ex2){ex2.printStackTrace();}
		}		
			
	}//end of nameChange() method statement	
	public String cmr0020_Insert(String UserId,String SysCd,String DsnCd,String RsrcName,String RsrcCd,String JobCd,String LangCd,String ProgTit,String BaseItem) throws SQLException, Exception {
		Connection        conn        = null;
		PreparedStatement pstmt       = null;
		//PreparedStatement pstmt2       = null;
		ResultSet         rs          = null;
		//ResultSet         rs2         = null;
		StringBuffer      strQuery    = new StringBuffer();
		String            retMsg      = "0";
		String            strItemId   = null;
		boolean           insFg       = true;
		//int               rsCnt       = 0;
		ConnectionContext connectionContext = new ConnectionResource();		
		
		try {
			conn = connectionContext.getConnection();
			conn.setAutoCommit(false);			
			strQuery.append("select cr_itemid from cmr0020                  \n");
			strQuery.append(" where cr_syscd=? and cr_dsncd=?               \n");
			strQuery.append("   and cr_rsrcname=?                                   \n");
						
	        pstmt = conn.prepareStatement(strQuery.toString());	
	        pstmt.setString(1, SysCd);   	
	        pstmt.setString(2, DsnCd);        		
	        pstmt.setString(3, RsrcName);        	     	
	        rs = pstmt.executeQuery();
	        
	        if (rs.next() == true) {
	        	insFg = false;
	        	strItemId = rs.getString("cr_itemid");
	        } 
	        else {
	        	insFg = true;
	        	/*
	        	strQuery.setLength(0);                		
        		strQuery.append("select to_number(max(cr_itemid)) + 1 as max,to_char(SYSDATE,'yyyymm') datYYMM  \n");               		
        		strQuery.append("  from cmr0020                                                  \n"); 
        		strQuery.append(" where substr(cr_itemid,1,6)=to_char(SYSDATE,'yyyymm')          \n"); 
        		pstmt2 = conn.prepareStatement(strQuery.toString());	
    	        rs2 = pstmt2.executeQuery();
    	        if (rs2.next()) {
    	        	if (rs2.getString("max") == null) {
    	        		strItemId = rs2.getString("datYYMM") + "000001";
    	        	} 
    	        	else {
    	        		strItemId = rs2.getString("max");
    	        	}
    	        }
    	        
    	        rs2.close();
    	        pstmt2.close();
    	        */
	        }
	        
	        rs.close();
	        pstmt.close();
	        
	        
	        if (insFg == true) {
	        	strQuery.setLength(0);                		
        		strQuery.append("insert into cmr0020 (CR_ITEMID,CR_SYSCD,CR_DSNCD,CR_RSRCNAME,  \n");
        		strQuery.append("   CR_RSRCCD,CR_JOBCD,CR_LANGCD,CR_STATUS,CR_CREATOR,CR_STORY, \n");
        		strQuery.append("   CR_OPENDATE,CR_LASTDATE,CR_LSTVER,CR_EDITOR,CR_NOMODIFY)    \n");
        		strQuery.append(" (select decode(nvl(max(cr_itemid),'0'),'0',to_char(SYSDATE,'yyyymm')||'000001', \n");
        		strQuery.append(" to_number(max(cr_itemid)) +1) as cr_itemid, \n");
        		strQuery.append(" ?, ?, ?, ?, ?, ?, '3', ?, ?, SYSDATE, SYSDATE, 0, ?, '0' \n");
        		strQuery.append(" from cmr0020 \n");
        		strQuery.append(" where substr(cr_itemid,1,6)=to_char(SYSDATE,'yyyymm') ) \n");
        		pstmt = conn.prepareStatement(strQuery.toString());	
          	    pstmt.setString(1, SysCd);         
          	    pstmt.setString(2, DsnCd);         
          	    pstmt.setString(3, RsrcName);         
          	    pstmt.setString(4, RsrcCd);         
          	    pstmt.setString(5, JobCd);         
          	    pstmt.setString(6, LangCd);        
          	    pstmt.setString(7, UserId);         
          	    pstmt.setString(8, ProgTit);         
          	    pstmt.setString(9, UserId);  
          		pstmt.executeUpdate();           		
	        } 
	        else {
	        	strQuery.setLength(0);                		
        		strQuery.append("update cmr0020 set CR_RSRCCD=?,CR_JOBCD=?,          \n");
        		strQuery.append("                   CR_LANGCD=?,CR_STATUS='3',       \n");
        		strQuery.append("                   CR_CREATOR=?,CR_STORY=?,         \n");
        		strQuery.append("                   CR_OPENDATE=SYSDATE,             \n");
        		strQuery.append("                   CR_LASTDATE=SYSDATE,CR_LSTVER=0, \n");
        		strQuery.append("                   CR_EDITOR=?,CR_NOMODIFY=0        \n");
        		strQuery.append("where cr_itemid=?                                   \n");        		
        		pstmt = conn.prepareStatement(strQuery.toString());	
          	    pstmt.setString(1, RsrcCd);         
          	    pstmt.setString(2, JobCd);         
          	    pstmt.setString(3, LangCd);        
          	    pstmt.setString(4, UserId);         
          	    pstmt.setString(5, ProgTit);         
          	    pstmt.setString(6, UserId);          
          	    pstmt.setString(7, strItemId);  
          		pstmt.executeUpdate();   
	        }
	        pstmt.close();
	        
	        if (insFg == true) {
		        strQuery.setLength(0);
				strQuery.append("select cr_itemid from cmr0020                  \n");
				strQuery.append(" where cr_syscd=? and cr_dsncd=?               \n");
				strQuery.append("   and cr_rsrcname=?                                   \n");
							
		        pstmt = conn.prepareStatement(strQuery.toString());	
		        pstmt.setString(1, SysCd);   	
		        pstmt.setString(2, DsnCd);        		
		        pstmt.setString(3, RsrcName);        	     	
		        rs = pstmt.executeQuery();
		        
		        strItemId = "";
		        if (rs.next()) {
		        	strItemId = rs.getString("cr_itemid");
		        }
		        
		        rs.close();
		        pstmt.close();		        
		        
		        if (strItemId.equals("")){
					if (rs != null)     try{rs.close();}catch (Exception ex){ex.printStackTrace();}
					if (pstmt != null)  try{pstmt.close();}catch (Exception ex2){ex2.printStackTrace();}
					if (conn != null){
						try{
							conn.rollback();
							ConnectionResource.release(conn);
						}catch(Exception ex3){
							ecamsLogger.error("## Cmr0200.cmr0020_Insert() connection release exception ##");
							ex3.printStackTrace();
						}				
					}
		        	throw new Exception("SysCD="+SysCd+" DsnCD="+DsnCd+" RsrcName="+RsrcName+" 자원의 ITEMID 조회 실패.");
		        }
	        }
	        
	        if ("".equals(BaseItem)){
	        	retMsg = "0" + strItemId;
	        }
	        else {
	        	strQuery.setLength(0);
	        	strQuery.append("select count(*) as cnt from cmr0022               \n");
				strQuery.append(" where cr_itemid=?                                \n");
				strQuery.append("   and cr_baseitem=?                              \n");
							
		        pstmt = conn.prepareStatement(strQuery.toString());	
		        pstmt.setString(1, strItemId);   	
		        pstmt.setString(2, BaseItem);        	     	
		        rs = pstmt.executeQuery();
		        if (rs.next() == false) {
		        	strQuery.setLength(0);                		
	        		strQuery.append("insert into cmr0022 (CR_ITEMID,CR_BASEITEM)  \n");
	        		strQuery.append("values (?, ?)                                \n");
	        		pstmt = conn.prepareStatement(strQuery.toString());	
	          	    pstmt.setString(1, strItemId); 	        
	          	    pstmt.setString(2, BaseItem); 
	          		pstmt.executeUpdate();   
		        }
		        
		        rs.close();
		        pstmt.close();
		        
		        
		        retMsg = "0";
	        }
	        
	        conn.commit();
	        conn.close();
	        conn = null;
	        
	        return retMsg;
	        
	        
		} catch (SQLException sqlexception) {
			sqlexception.printStackTrace();
			if (rs != null)     try{rs.close();}catch (Exception ex){ex.printStackTrace();}
			if (pstmt != null)  try{pstmt.close();}catch (Exception ex2){ex2.printStackTrace();}
			if (conn != null){
				try{
					conn.rollback();
					ConnectionResource.release(conn);
				}catch(Exception ex3){
					ecamsLogger.error("## Cmr0200.cmr0020_Insert() connection release exception ##");
					ex3.printStackTrace();
				}				
			}			
			ecamsLogger.error("## Cmr0200.cmr0020_Insert() SQLException START ##");
			ecamsLogger.error("## Error DESC : ", sqlexception);	
			ecamsLogger.error("## Cmr0200.cmr0020_Insert() SQLException END ##");			
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
					ecamsLogger.error("## Cmr0200.cmr0020_Insert() connection release exception ##");
					ex3.printStackTrace();
				}				
			}			
			ecamsLogger.error("## Cmr0200.cmr0020_Insert() Exception START ##");				
			ecamsLogger.error("## Error DESC : ", exception);	
			ecamsLogger.error("## Cmr0200.cmr0020_Insert() Exception END ##");				
			throw exception;
		}finally{
			if (strQuery != null) 	strQuery = null;
			if (rs != null)     try{rs.close();}catch (Exception ex){ex.printStackTrace();}
			if (pstmt != null)  try{pstmt.close();}catch (Exception ex2){ex2.printStackTrace();}
			if (conn != null){
				try{
					conn.commit();
					conn.close();
					conn = null;
				}catch(Exception ex3){
					ecamsLogger.error("## Cmr0200.cmr0020_Insert() connection release exception ##");
					ex3.printStackTrace();
				}
			}
		}
	}//end of cmr0020_Insert() method statement
	
	public Object[] cmr0020_Delete(String UserId,String ItemId) throws SQLException, Exception {
		Connection        conn        = null;
		PreparedStatement pstmt       = null;
		PreparedStatement pstmt2       = null;
		ResultSet         rs          = null;
		StringBuffer      strQuery    = new StringBuffer();
		Object[] returnObjectArray    = null;
		ArrayList<HashMap<String, String>>  rsval = new ArrayList<HashMap<String, String>>();
		HashMap<String, String>			  rst		  = null;	
		//int               rsCnt       = 0;
		ConnectionContext connectionContext = new ConnectionResource();		
		
		try {
			conn = connectionContext.getConnection();
			conn.setAutoCommit(false);		
			strQuery.append("select cr_itemid from cmr0022              \n");
			strQuery.append(" where cr_baseitem=?                       \n");
						
	        pstmt = conn.prepareStatement(strQuery.toString());	
	        pstmt.setString(1, ItemId);        	     	
	        rs = pstmt.executeQuery();
	        rsval.clear();
	        
	        while(rs.next()) {
	        	strQuery.setLength(0);
	        	strQuery.append("delete cmr0020 where cr_itemid=?  \n");
	        	pstmt2 = conn.prepareStatement(strQuery.toString());	
	        	pstmt2.setString(1, rs.getString("cr_itemid")); 
	      		pstmt2.executeUpdate();
	      		pstmt2.close();
	        }
	        rs.close();
	        pstmt.close();
	        
	        
	        strQuery.setLength(0);
	        strQuery.append("delete cmr0022 where cr_baseitem=?  \n");
			pstmt = conn.prepareStatement(strQuery.toString());	
	  	    pstmt.setString(1, ItemId); 
	  		pstmt.executeUpdate();
	  		
	  		pstmt.close();
	  		
	  		
	        strQuery.setLength(0);
	        strQuery.append("delete cmr0020 where cr_itemid=?  \n");
			pstmt = conn.prepareStatement(strQuery.toString());	
	  	    pstmt.setString(1, ItemId); 
	  		pstmt.executeUpdate();
	  		pstmt.close();
	  		
	  		
	  		conn.commit();
	  		rs = null;
	  		pstmt = null;
	  		conn = null;
	  		
	  		rst = new HashMap<String, String>();
			rst.put("retMsg", "0");
			rsval.add(rst);
			rst = null;
			
			returnObjectArray = rsval.toArray();
			rsval.clear();
			rsval = null;
			//ecamsLogger.error(rsval.toString());		
			return returnObjectArray;
			
			
		} catch (SQLException sqlexception) {
			sqlexception.printStackTrace();
			ecamsLogger.error("## Cmr0200.cmr0020_Delete() SQLException START ##");
			ecamsLogger.error("## Error DESC : ", sqlexception);	
			ecamsLogger.error("## Cmr0200.cmr0020_Delete() SQLException END ##");			
			throw sqlexception;
		} catch (Exception exception) {
			exception.printStackTrace();
			ecamsLogger.error("## Cmr0200.cmr0020_Delete() Exception START ##");				
			ecamsLogger.error("## Error DESC : ", exception);	
			ecamsLogger.error("## Cmr0200.cmr0020_Delete() Exception END ##");				
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
					ecamsLogger.error("## Cmr0200.cmr0020_Delete() connection release exception ##");
					ex3.printStackTrace();
				}
			}
		}
	}//end of cmr0020_Delete() method statement
	

	public Object[] checkTestCase(String UserId,String ItemId) throws SQLException, Exception {
		Connection        conn        = null;
		PreparedStatement pstmt       = null;
		PreparedStatement pstmt2       = null;
		ResultSet         rs          = null;
		StringBuffer      strQuery    = new StringBuffer();
		Object[] returnObjectArray    = null;
		ArrayList<HashMap<String, String>>  rsval = new ArrayList<HashMap<String, String>>();
		HashMap<String, String>			  rst		  = null;	
		//int               rsCnt       = 0;
		ConnectionContext connectionContext = new ConnectionResource();		
		
		try {
			conn = connectionContext.getConnection();
			conn.setAutoCommit(false);		
			strQuery.append("select cr_itemid from cmr0022              \n");
			strQuery.append(" where cr_baseitem=?                       \n");
						
	        pstmt = conn.prepareStatement(strQuery.toString());	
	        pstmt.setString(1, ItemId);        	     	
	        rs = pstmt.executeQuery();
	        rsval.clear();
	        
	        while(rs.next()) {
	        	strQuery.setLength(0);
	        	strQuery.append("delete cmr0020 where cr_itemid=?  \n");
	        	pstmt2 = conn.prepareStatement(strQuery.toString());	
	        	pstmt2.setString(1, rs.getString("cr_itemid")); 
	      		pstmt2.executeUpdate();
	      		pstmt2.close();
	        }
	        rs.close();
	        pstmt.close();
	        
	        
	        strQuery.setLength(0);
	        strQuery.append("delete cmr0022 where cr_baseitem=?  \n");
			pstmt = conn.prepareStatement(strQuery.toString());	
	  	    pstmt.setString(1, ItemId); 
	  		pstmt.executeUpdate();
	  		
	  		pstmt.close();
	  		
	  		
	        strQuery.setLength(0);
	        strQuery.append("delete cmr0020 where cr_itemid=?  \n");
			pstmt = conn.prepareStatement(strQuery.toString());	
	  	    pstmt.setString(1, ItemId); 
	  		pstmt.executeUpdate();
	  		pstmt.close();
	  		
	  		
	  		conn.commit();
	  		rs = null;
	  		pstmt = null;
	  		conn = null;
	  		
	  		rst = new HashMap<String, String>();
			rst.put("retMsg", "0");
			rsval.add(rst);
			rst = null;
			
			returnObjectArray = rsval.toArray();
			rsval.clear();
			rsval = null;
			//ecamsLogger.error(rsval.toString());		
			return returnObjectArray;
			
			
		} catch (SQLException sqlexception) {
			sqlexception.printStackTrace();
			ecamsLogger.error("## Cmr0200.cmr0020_Delete() SQLException START ##");
			ecamsLogger.error("## Error DESC : ", sqlexception);	
			ecamsLogger.error("## Cmr0200.cmr0020_Delete() SQLException END ##");			
			throw sqlexception;
		} catch (Exception exception) {
			exception.printStackTrace();
			ecamsLogger.error("## Cmr0200.cmr0020_Delete() Exception START ##");				
			ecamsLogger.error("## Error DESC : ", exception);	
			ecamsLogger.error("## Cmr0200.cmr0020_Delete() Exception END ##");				
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
					ecamsLogger.error("## Cmr0200.cmr0020_Delete() connection release exception ##");
					ex3.printStackTrace();
				}
			}
		}
	}//end of cmr0020_Delete() method statement

	/**
	 * <PRE>
	 * 1. MethodName	: checkReqYN
	 * 2. ClassName		: Cmr0200
	 * 3. Commnet			: 
	 * 4. 작성자				: no name
	 * 5. 작성일				: 2010. 12. 22. 오후 2:10:28
	 * </PRE>
	 * 		@return boolean
	 * 		@param selectISRID
	 * 		@param SysCd
	 * 		@param chkInList
	 * 		@param UserId
	 * 		@param strReqCD
	 * 		@return
	 * 		@throws SQLException
	 * 		@throws Exception
	 */
	public boolean checkReqYN(String selectORDERID,String SysCd,ArrayList<HashMap<String,String>> chkInList,
			String UserId,String strReqCD) throws SQLException, Exception {
		Connection        conn        = null;
		PreparedStatement pstmt       = null;
		ResultSet         rs          = null;
		StringBuffer      strQuery    = new StringBuffer();
		boolean           resBoolean  = false;
		
		ConnectionContext connectionContext = new ConnectionResource();
		
		try {

			conn = connectionContext.getConnection();
			
			int    i = 0;
			strQuery.setLength(0);
			strQuery.append("select a.cr_itemid \n");
			strQuery.append("  from cmr1010 a,cmr0020 b,cmr1000 c \n");
			strQuery.append(" where c.cr_orderid=? \n");
			strQuery.append("   and c.cr_editor=? \n");
			strQuery.append("   and a.cr_acptno=c.cr_acptno \n");
			strQuery.append("   and c.cr_qrycd in ('01','02') \n");
			strQuery.append("   and a.cr_confno is null \n");
			strQuery.append("   and a.cr_syscd=? \n");
			strQuery.append("   and a.cr_baseitem=a.cr_itemid \n");
			strQuery.append("   and a.cr_itemid=b.cr_itemid \n");
			strQuery.append("   and a.cr_version>0 \n");
			//strQuery.append("   and b.cr_status in (decode(d.cc_detcate,'07','5','B')) \n");
			strQuery.append(" union \n");
			strQuery.append("select b.cr_itemid \n");
			strQuery.append("  from cmr0020 b,cmm0036 c \n");
			strQuery.append(" where b.cr_orderid=? \n");
			strQuery.append("   and b.cr_editor=? \n");
			strQuery.append("   and b.cr_syscd=? \n");
			strQuery.append("   and b.cr_lstver=0 \n");
			strQuery.append("   and b.cr_status in ('3','B') \n");
			strQuery.append("   and b.cr_syscd=c.cm_syscd \n");
			strQuery.append("   and b.cr_rsrccd=c.cm_rsrccd \n");
			strQuery.append("   and substr(c.cm_info,2,1)='1' \n");
			strQuery.append("   and substr(c.cm_info,26,1)='0' \n");
			strQuery.append("   and c.cm_rsrccd not in (select cm_samersrc from cmm0037 \n");
			strQuery.append("                            where cm_syscd=?) \n");
			//strQuery.append("   and (b.cr_status='3' or (b.cr_status='B' and b.cr_editor=?)) \n");
			
			pstmt = conn.prepareStatement(strQuery.toString());
	//        pstmt = new LoggableStatement(conn,strQuery.toString());
	        
			int paramIndex = 1;
			pstmt.setString(paramIndex++, selectORDERID);
			pstmt.setString(paramIndex++, UserId);
			pstmt.setString(paramIndex++, SysCd);
			pstmt.setString(paramIndex++, selectORDERID);
			pstmt.setString(paramIndex++, UserId);
			pstmt.setString(paramIndex++, SysCd);
			pstmt.setString(paramIndex++, SysCd);
//			//ecamsLogger.error(((LoggableStatement)pstmt).getQueryString());
			rs = pstmt.executeQuery();
			
			while (rs.next()){
				resBoolean = false;
				for (i=0 ; i<chkInList.size() ; i++)
				{
					if (chkInList.get(i).get("cr_itemid").equals(rs.getString("cr_itemid"))){
						resBoolean = true;
						chkInList.remove(i);
						break;
					}
				}
				if (!resBoolean) break;
			}
			rs.close();
			pstmt.close();
			conn.close();
	        rs = null;
	        pstmt = null;
	        conn = null;
	        
			return resBoolean;
			
			
		} catch (SQLException sqlexception) {
			sqlexception.printStackTrace();
			ecamsLogger.error("## Cmr0200.checkReqYN() SQLException START ##");
			ecamsLogger.error("## Error DESC : ", sqlexception);	
			ecamsLogger.error("## Cmr0200.checkReqYN() SQLException END ##");			
			throw sqlexception;
		} catch (Exception exception) {
			exception.printStackTrace();
			ecamsLogger.error("## Cmr0200.checkReqYN() Exception START ##");				
			ecamsLogger.error("## Error DESC : ", exception);	
			ecamsLogger.error("## Cmr0200.checkReqYN() Exception END ##");				
			throw exception;
		}finally{
			if (strQuery != null) 	strQuery = null;
			if (rs != null)     try{rs.close();}catch (Exception ex){ex.printStackTrace();}
			if (pstmt != null)  try{pstmt.close();}catch (Exception ex2){ex2.printStackTrace();}
			if (conn != null){
				try{
					ConnectionResource.release(conn);
				}catch(Exception ex3){
					ecamsLogger.error("## Cmr0200.checkReqYN() connection release exception ##");
					ex3.printStackTrace();
				}
			}
		}
	}//end of checkReqYN() method statement
	
	
	public String setJobPlanInfo(HashMap<String,String> etcData) throws SQLException, Exception {
		Connection        conn        = null;
		PreparedStatement pstmt       = null;
		ResultSet         rs          = null;
		StringBuffer      strQuery    = new StringBuffer();

		ConnectionContext connectionContext = new ConnectionResource();		
		
		try {
			
			conn = connectionContext.getConnection();
			conn.setAutoCommit(false);
			
	        strQuery.setLength(0);
	        strQuery.append("delete CMC0250 where CC_ISRID=? and CC_ISRSUB=? and CC_SCMUSER=? \n");
			pstmt = conn.prepareStatement(strQuery.toString());	
	        pstmt.setString(1, etcData.get("CC_ISRID"));   	
	        pstmt.setString(2, etcData.get("CC_ISRSUB"));        		
	        pstmt.setString(3, etcData.get("CC_SCMUSER"));  
	  		pstmt.executeUpdate();
	  		pstmt.close();

	    	strQuery.setLength(0);                		
			strQuery.append("INSERT INTO CMC0250 (CC_ISRID,CC_ISRSUB,CC_SCMUSER,CC_CREATDT,  \n");
			strQuery.append("   CC_LASTDT,CC_JOBTITLE,CC_JOBGBN,CC_MONTERM,CC_ENDRPTDAY,CC_JOBDETAIL,CC_REFMSG, \n");
			strQuery.append("   CC_ACPTNO,CC_JOBDATE,CC_JOBTIME,CC_STATUS) \n");
			
			strQuery.append(" VALUES (?,?,?,SYSDATE,  SYSDATE,?,?,?,?,?,?,  ?,?,?,?) \n");
			
			pstmt = conn.prepareStatement(strQuery.toString());
//		pstmt = new LoggableStatement(conn,strQuery.toString());
			int paramIndex = 1;
	  	    pstmt.setString(paramIndex++, etcData.get("CC_ISRID"));         
	  	    pstmt.setString(paramIndex++, etcData.get("CC_ISRSUB"));         
	  	    pstmt.setString(paramIndex++, etcData.get("CC_SCMUSER"));
	  	    pstmt.setString(paramIndex++, etcData.get("CC_JOBTITLE"));         
	  	    pstmt.setString(paramIndex++, etcData.get("CC_JOBGBN"));         
	  	    pstmt.setString(paramIndex++, etcData.get("CC_MONTERM"));        
	  	    pstmt.setString(paramIndex++, etcData.get("CC_ENDRPTDAY"));         
	  	    pstmt.setString(paramIndex++, etcData.get("CC_JOBDETAIL"));         
	  	    pstmt.setString(paramIndex++, etcData.get("CC_REFMSG"));
	  	    pstmt.setString(paramIndex++, etcData.get("CC_ACPTNO"));         
	  	    pstmt.setString(paramIndex++, etcData.get("CC_JOBDATE"));         
	  	    pstmt.setString(paramIndex++, etcData.get("CC_JOBTIME"));       
	  	    pstmt.setString(paramIndex++, etcData.get("savecd"));
//	  	    //ecamsLogger.error(((LoggableStatement)pstmt).getQueryString());
	  		pstmt.executeUpdate();
	  		pstmt.close();
	  		
	  		strQuery.setLength(0);
	  		strQuery.append("update cmc0110 set cc_substatus=decode(cc_substatus,'39','26','23','26',cc_substatus) \n");
	  		strQuery.append("                   ,cc_mainstatus='02' \n");
	  		strQuery.append(" where cc_isrid=? and cc_isrsub=?      \n");
	  		pstmt = conn.prepareStatement(strQuery.toString());
	  		pstmt.setString(1, etcData.get("CC_ISRID"));         
	  	    pstmt.setString(2, etcData.get("CC_ISRSUB"));    
	  	    pstmt.executeUpdate();
	  	    pstmt.close();
	        conn.commit();
	        conn.close();
	        conn = null;
	        
	        return "0";
	        
	        
		} catch (SQLException sqlexception) {
			sqlexception.printStackTrace();
			if (rs != null)     try{rs.close();}catch (Exception ex){ex.printStackTrace();}
			if (pstmt != null)  try{pstmt.close();}catch (Exception ex2){ex2.printStackTrace();}
			if (conn != null){
				try{
					conn.rollback();
					ConnectionResource.release(conn);
				}catch(Exception ex3){
					ecamsLogger.error("## Cmr0200.setJobPlanInfo() connection release exception ##");
					ex3.printStackTrace();
				}				
			}			
			ecamsLogger.error("## Cmr0200.setJobPlanInfo() SQLException START ##");
			ecamsLogger.error("## Error DESC : ", sqlexception);	
			ecamsLogger.error("## Cmr0200.setJobPlanInfo() SQLException END ##");			
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
					ecamsLogger.error("## Cmr0200.setJobPlanInfo() connection release exception ##");
					ex3.printStackTrace();
				}				
			}			
			ecamsLogger.error("## Cmr0200.setJobPlanInfo() Exception START ##");				
			ecamsLogger.error("## Error DESC : ", exception);	
			ecamsLogger.error("## Cmr0200.setJobPlanInfo() Exception END ##");				
			throw exception;
		}finally{
			if (strQuery != null) 	strQuery = null;
			if (rs != null)     try{rs.close();}catch (Exception ex){ex.printStackTrace();}
			if (pstmt != null)  try{pstmt.close();}catch (Exception ex2){ex2.printStackTrace();}
			if (conn != null){
				try{
					conn.commit();
					conn.close();
					conn = null;
				}catch(Exception ex3){
					ecamsLogger.error("## Cmr0200.setJobPlanInfo() connection release exception ##");
					ex3.printStackTrace();
				}
			}
		}
	}//end of setJobPlanInfo() method statement

	public String setJobPlanInfo_conn(HashMap<String,String> etcData,Connection conn) throws SQLException, Exception {
		PreparedStatement pstmt       = null;
		ResultSet         rs          = null;
		StringBuffer      strQuery    = new StringBuffer();

		
		try {
			
			
	        strQuery.setLength(0);
	        strQuery.append("delete CMC0250 where CC_ISRID=? and CC_ISRSUB=? and CC_SCMUSER=? \n");
			pstmt = conn.prepareStatement(strQuery.toString());	
	        pstmt.setString(1, etcData.get("CC_ISRID"));   	
	        pstmt.setString(2, etcData.get("CC_ISRSUB"));        		
	        pstmt.setString(3, etcData.get("CC_SCMUSER"));  
	  		pstmt.executeUpdate();
	  		pstmt.close();

	    	strQuery.setLength(0);                		
			strQuery.append("INSERT INTO CMC0250 (CC_ISRID,CC_ISRSUB,CC_SCMUSER,CC_CREATDT,  \n");
			strQuery.append("   CC_LASTDT,CC_JOBTITLE,CC_JOBGBN,CC_MONTERM,CC_ENDRPTDAY,CC_JOBDETAIL,CC_REFMSG, \n");
			strQuery.append("   CC_ACPTNO,CC_JOBDATE,CC_JOBTIME,CC_STATUS) \n");
			
			strQuery.append(" VALUES (?,?,?,SYSDATE,  SYSDATE,?,?,?,?,?,?,  ?,?,?,?) \n");
			
			pstmt = conn.prepareStatement(strQuery.toString());
//		pstmt = new LoggableStatement(conn,strQuery.toString());
			int paramIndex = 1;
	  	    pstmt.setString(paramIndex++, etcData.get("CC_ISRID"));         
	  	    pstmt.setString(paramIndex++, etcData.get("CC_ISRSUB"));         
	  	    pstmt.setString(paramIndex++, etcData.get("CC_SCMUSER"));
	  	    pstmt.setString(paramIndex++, etcData.get("CC_JOBTITLE"));         
	  	    pstmt.setString(paramIndex++, etcData.get("CC_JOBGBN"));         
	  	    pstmt.setString(paramIndex++, etcData.get("CC_MONTERM"));        
	  	    pstmt.setString(paramIndex++, etcData.get("CC_ENDRPTDAY"));         
	  	    pstmt.setString(paramIndex++, etcData.get("CC_JOBDETAIL"));         
	  	    pstmt.setString(paramIndex++, etcData.get("CC_REFMSG"));
	  	    pstmt.setString(paramIndex++, etcData.get("CC_ACPTNO"));         
	  	    pstmt.setString(paramIndex++, etcData.get("CC_JOBDATE"));         
	  	    pstmt.setString(paramIndex++, etcData.get("CC_JOBTIME"));       
	  	    pstmt.setString(paramIndex++, etcData.get("savecd"));
//	  	    //ecamsLogger.error(((LoggableStatement)pstmt).getQueryString());
	  		pstmt.executeUpdate();
	  		pstmt.close();
	  		
	  		strQuery.setLength(0);
	  		strQuery.append("update cmc0110 set cc_substatus=decode(cc_substatus,'39','26','23','26',cc_substatus) \n");
	  		strQuery.append("                   ,cc_mainstatus='02' \n");
	  		strQuery.append(" where cc_isrid=? and cc_isrsub=?      \n");
	  		pstmt = conn.prepareStatement(strQuery.toString());
	  		pstmt.setString(1, etcData.get("CC_ISRID"));         
	  	    pstmt.setString(2, etcData.get("CC_ISRSUB"));    
	  	    pstmt.executeUpdate();
	  	    pstmt.close();
	        
	        return "0";
	        
	        
		} catch (SQLException sqlexception) {
			sqlexception.printStackTrace();
			if (rs != null)     try{rs.close();}catch (Exception ex){ex.printStackTrace();}
			if (pstmt != null)  try{pstmt.close();}catch (Exception ex2){ex2.printStackTrace();}
			ecamsLogger.error("## Cmr0200.setJobPlanInfo() SQLException START ##");
			ecamsLogger.error("## Error DESC : ", sqlexception);	
			ecamsLogger.error("## Cmr0200.setJobPlanInfo() SQLException END ##");			
			return "1";
		} catch (Exception exception) {
			exception.printStackTrace();
			if (rs != null)     try{rs.close();}catch (Exception ex){ex.printStackTrace();}
			if (pstmt != null)  try{pstmt.close();}catch (Exception ex2){ex2.printStackTrace();}
			ecamsLogger.error("## Cmr0200.setJobPlanInfo() Exception START ##");				
			ecamsLogger.error("## Error DESC : ", exception);	
			ecamsLogger.error("## Cmr0200.setJobPlanInfo() Exception END ##");				
			return "1";
		}finally{
			if (strQuery != null) 	strQuery = null;
			if (rs != null)     try{rs.close();}catch (Exception ex){ex.printStackTrace();}
			if (pstmt != null)  try{pstmt.close();}catch (Exception ex2){ex2.printStackTrace();}
		}
	}//end of setJobPlanInfo() method statement
	/**
	 * <PRE>
	 * 1. MethodName	: getJobPlanInfo
	 * 2. ClassName		: Cmr0200
	 * 3. Commnet			: 작업계획서 정보 조회
	 * 4. 작성자				: no name
	 * 5. 작성일				: 2011. 1. 21. 오후 4:34:00
	 * </PRE>
	 * 		@return Object[]
	 * 		@param etcData
	 * 		@return
	 * 		@throws SQLException
	 * 		@throws Exception
	 */
	public Object[] getJobPlanInfo(HashMap<String,String> etcData) throws SQLException, Exception {
		Connection        conn        = null;
		PreparedStatement pstmt       = null;
		ResultSet         rs          = null;
		StringBuffer      strQuery    = new StringBuffer();
		Object[] returnObjectArray    = null;
		ArrayList<HashMap<String, String>>  rsval = new ArrayList<HashMap<String, String>>();
		HashMap<String, String>			  rst		  = null;
		
		ConnectionContext connectionContext = new ConnectionResource();		
		
		try {
			
			conn = connectionContext.getConnection();
			
			int pstmtcount = 1;
			strQuery.setLength(0);
			strQuery.append("select a.CC_SCMUSER,a.CC_JOBTITLE,a.CC_JOBGBN,a.CC_MONTERM,   \n");
			strQuery.append("       a.CC_ENDRPTDAY,a.CC_JOBDETAIL,a.CC_REFMSG,a.cc_status, \n");
			strQuery.append("		to_char(a.CC_CREATDT,'yyyy/mm/dd') CC_CREATDT, \n");
			strQuery.append("		to_char(a.CC_LASTDT,'yyyy/mm/dd') CC_LASTDT, \n");
			//strQuery.append("		to_char(a.CC_LASTDT,'yyyy/mm/dd') CC_JOBDATE, \n");
			strQuery.append("		a.CC_JOBDATE,a.CC_JOBTIME,a.CC_ACPTNO,b.CC_SUBSTATUS,b.CC_CHGUSER \n");
			strQuery.append("  from cmc0250 a,cmc0110 b \n");
			strQuery.append(" where a.CC_ISRID=? and a.CC_ISRSUB=? \n");
			strQuery.append("   and a.CC_ISRID = b.CC_ISRID and a.CC_ISRSUB = b.CC_ISRSUB \n");
			strQuery.append("   and a.CC_SCMUSER = ? \n");
			
			pstmt = conn.prepareStatement(strQuery.toString());	
//			pstmt = new LoggableStatement(conn,strQuery.toString());
			pstmtcount = 1;
			pstmt.setString(pstmtcount++,etcData.get("CC_ISRID"));
			pstmt.setString(pstmtcount++,etcData.get("CC_ISRSUB"));
			pstmt.setString(pstmtcount++,etcData.get("CC_SCMUSER"));
//	        //ecamsLogger.error(((LoggableStatement)pstmt).getQueryString());           
	        rs = pstmt.executeQuery();
	        rsval.clear();
			if (rs.next()){
				rst = new HashMap<String, String>();
				rst.put("CC_SCMUSER", rs.getString("CC_SCMUSER"));
				rst.put("CC_JOBTITLE",rs.getString("CC_JOBTITLE"));
				rst.put("CC_JOBGBN",rs.getString("CC_JOBGBN"));
				rst.put("CC_MONTERM",rs.getString("CC_MONTERM"));
				rst.put("CC_ENDRPTDAY","");
				if (rs.getString("CC_ENDRPTDAY") != "" && rs.getString("CC_ENDRPTDAY") != null){
					rst.put("CC_ENDRPTDAY",rs.getString("CC_ENDRPTDAY").substring(0,4)+"/"+rs.getString("CC_ENDRPTDAY").substring(4,6)+"/"+rs.getString("CC_ENDRPTDAY").substring(6));
				}
				if (rs.getString("CC_JOBDATE") != "" && rs.getString("CC_JOBDATE") != null){
					rst.put("CC_JOBDATE",rs.getString("CC_JOBDATE").substring(0,4)+"/"+rs.getString("CC_JOBDATE").substring(4,6)+"/"+rs.getString("CC_JOBDATE").substring(6));
				}
				rst.put("CC_JOBDETAIL",rs.getString("CC_JOBDETAIL"));
				rst.put("CC_REFMSG",rs.getString("CC_REFMSG"));
				rst.put("CC_CREATDT",rs.getString("CC_CREATDT"));
				rst.put("CC_LASTDT",rs.getString("CC_LASTDT"));
				rst.put("CC_JOBTIME",rs.getString("CC_JOBTIME"));
				//rst.put("CC_JOBDATE",rs.getString("CC_JOBDATE"));
				rst.put("CC_ACPTNO",rs.getString("CC_ACPTNO"));
				rst.put("CC_SUBSTATUS", rs.getString("CC_SUBSTATUS"));
				rst.put("CC_CHGUSER", rs.getString("CC_CHGUSER"));
				rst.put("CC_CHGUSER", rs.getString("CC_CHGUSER"));
				rst.put("CC_STATUS", rs.getString("CC_STATUS"));
				rsval.add(rst);
				rst = null;
			}
			rs.close();
			pstmt.close();
			
			strQuery.setLength(0);
			strQuery.append("select JOBPLAN_JOBMAKE(?,?,?,?) jobsta from dual \n");
		    pstmt = conn.prepareStatement(strQuery.toString());
		    pstmtcount = 1;
			pstmt.setString(pstmtcount++,etcData.get("CC_SCMUSER"));
			pstmt.setString(pstmtcount++,etcData.get("CC_ISRID"));
			pstmt.setString(pstmtcount++,etcData.get("CC_ISRSUB"));
			pstmt.setString(pstmtcount++,etcData.get("REQCD"));
			rs = pstmt.executeQuery();
			if (rs.next()) {
				rst = new HashMap<String, String>();
				rst.put("CC_SCMUSER", "JOBSTA");
				rst.put("jobsta", rs.getString("jobsta"));
				rsval.add(0,rst);
				rst = null;
			}
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
			ecamsLogger.error("## Cmr0200.getJobPlanInfo() SQLException START ##");
			ecamsLogger.error("## Error DESC : ", sqlexception);	
			ecamsLogger.error("## Cmr0200.getJobPlanInfo() SQLException END ##");			
			throw sqlexception;
		} catch (Exception exception) {
			exception.printStackTrace();
			ecamsLogger.error("## Cmr0200.getJobPlanInfo() Exception START ##");				
			ecamsLogger.error("## Error DESC : ", exception);	
			ecamsLogger.error("## Cmr0200.getJobPlanInfo() Exception END ##");				
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
					ecamsLogger.error("## Cmr0200.getJobPlanInfo() connection release exception ##");
					ex3.printStackTrace();
				}
			}
		}
	}//end of getJobPlanInfo() method statement
	
	public Object[] fileOpenChk(String UserId,ArrayList<String> fileList,HashMap<String,String> baseFile) 
		throws SQLException, Exception {
		Connection        	conn        = null;
		PreparedStatement 	pstmt       = null;
		PreparedStatement 	pstmt2      = null;
		ResultSet         	rs          = null;
		ResultSet         	rs2         = null;
		StringBuffer      	strQuery    = new StringBuffer();
		ArrayList<HashMap<String, String>>  rsval = new ArrayList<HashMap<String, String>>();
		HashMap<String,String> rst = new HashMap<String,String>();
		int                 i = 0;
		
		ConnectionContext connectionContext = new ConnectionResource();
		try {
			conn = connectionContext.getConnection();
			rsval.clear();
			int parmCnt = 0;
			boolean insSw = true;
			
			String subItem = "";
			String tmpWork1 = "";
			String tmpExe = "";
			String tmpRsrcCd = "";
			String tmpInfo = "";
			String retMsg = "";
			boolean errSw = false;
			Cmd0100 cmd0100 = new Cmd0100();
			for (i=0;fileList.size()>i;i++){
				errSw = false;
				rst = new HashMap<String,String>(baseFile);
				rst.put("cr_rsrcname",fileList.get(i));
				rst.put("errsw", "1");
				
				insSw = true;
				parmCnt = 0;
				strQuery.setLength(0);
				strQuery.append("select cr_itemid from cmr0020   	\n");  
	            strQuery.append(" where cr_syscd=?           		\n");
	            strQuery.append("   and cr_dsncd=?                  \n");
	        	strQuery.append("   and upper(cr_rsrcname)=upper(?) \n");
				pstmt = conn.prepareStatement(strQuery.toString());
	//			pstmt = new LoggableStatement(conn,strQuery.toString());
	            pstmt.setString(++parmCnt, baseFile.get("cr_syscd"));
	            pstmt.setString(++parmCnt, baseFile.get("cr_dsncd"));
	            pstmt.setString(++parmCnt, fileList.get(i));
//	            //ecamsLogger.error(((LoggableStatement)pstmt).getQueryString());
	            rs = pstmt.executeQuery();
	            if (rs.next()) { 
	            	subItem = rs.getString("cr_itemid");
	            	rst.put("cr_itemid", subItem);
	            	rst.put("errsw","0");
	            	insSw = false;
	            }
				rs.close();
				pstmt.close();
				
				if (insSw == true) {
					insSw = false;
					tmpWork1 = fileList.get(i);
					tmpExe = "";
					if (tmpWork1.indexOf(".")>0) tmpExe = tmpWork1.substring(tmpWork1.indexOf("."));
					
					if (tmpExe.length() > 0) {
						tmpExe = tmpExe + ",";
						tmpRsrcCd = "";
						tmpInfo = "";
						strQuery.setLength(0);
						strQuery.append("select a.cm_rsrccd,a.cm_info       \n");
						strQuery.append("  from cmm0036 a                   \n");
						strQuery.append(" where cm_syscd=?                  \n");
						strQuery.append("   and cm_closedt is null          \n");
						strQuery.append("   and substr(cm_info,26,1)='1'    \n");
						strQuery.append("   and instr(upper(cm_exename),upper(?))>0 \n");
						pstmt2 = conn.prepareStatement(strQuery.toString());
	//					pstmt2 = new LoggableStatement(conn,strQuery.toString());
						parmCnt = 0;
						pstmt2.setString(++parmCnt, baseFile.get("cr_syscd"));
						pstmt2.setString(++parmCnt, tmpExe);
//						//ecamsLogger.error(((LoggableStatement)pstmt2).getQueryString());
						rs2 = pstmt2.executeQuery();
						if (rs2.next()) {
							tmpRsrcCd = rs2.getString("cm_rsrccd");
							tmpInfo = rs2.getString("cm_info");
						}
						rs2.close();
						pstmt2.close();
						
						if (tmpRsrcCd.length() > 0) insSw = true;
					}
					if (insSw == true) {
						retMsg = cmd0100.cmr0020_Insert(UserId, baseFile.get("cr_syscd"), "", baseFile.get("cr_dsncd"), tmpWork1, tmpRsrcCd, 
								baseFile.get("cr_jobcd"), baseFile.get("cr_story"), baseFile.get("cr_itemid"), tmpInfo, conn,
								baseFile.get("cr_langcd"), baseFile.get("cr_compile"), baseFile.get("cr_makecompile"), baseFile.get("cr_teamcd"),
								baseFile.get("cr_sqlcheck"), baseFile.get("cr_document"),"","","","");
						//(String UserId,String SysCd,String IsrId,String IsrSub,String DsnCd,String RsrcName,String RsrcCd,String JobCd,String ProgTit,String BaseItem,String rsrcInfo,Connection conn)
						if (!retMsg.substring(0,1).equals("0")) {
							rst.put("errmsg", retMsg);
							errSw = true;
						} else {
							rst.put("errmsg", "정상");
							rst.put("itemid", retMsg.substring(1));
							subItem = retMsg.substring(1);
							rst.put("errsw","0");
						}
					} else {
						rst.put("errmsg", "제외(관리대상아님)");
						errSw = true;
						rst.put("errsw","1");
					}
				} else {
					if (subItem.equals(baseFile.get("cr_itemid"))) {
						rst.put("errmsg", "제외(기준프로그램)");
						errSw = true;	
						rst.put("errsw","1");
					}
				}
				
				if (errSw == false) {
					strQuery.setLength(0);			
					strQuery.append("select a.cr_rsrcname,a.cr_lstver,a.cr_syscd,a.cr_dsncd,             \n");
				    strQuery.append("       a.cr_itemid,a.cr_rsrccd,'' cr_acptno,a.cr_story,a.cr_jobcd,  \n");
				    strQuery.append("       a.cr_status,i.cm_info,                                       \n");
				    strQuery.append("       lpad(i.cm_stepsta,4,'0') prcreq,nvl(i.cm_vercnt,50) vercnt,  \n");
				    strQuery.append("       e.cm_codename jawon,a.cr_editor,c.cm_codename,               \n");
				    strQuery.append("       to_char(a.cr_lastdate,'yyyy/mm/dd') lastdate                 \n");
					strQuery.append("  from cmm0020 e,cmm0020 c,cmr0020 a,cmm0036 i                      \n"); 
					strQuery.append(" where a.cr_itemid=?                                                \n");
					strQuery.append("   and e.cm_macode='JAWON' and a.cr_rsrccd=e.cm_micode              \n");
					strQuery.append("   and c.cm_macode='CHECKIN' and c.cm_micode=decode(a.cr_lstver,0,'03','04') \n");
					strQuery.append("   and a.cr_syscd=i.cm_syscd and a.cr_rsrccd=i.cm_rsrccd            \n");
					pstmt = conn.prepareStatement(strQuery.toString());	
//					pstmt = new LoggableStatement(conn,strQuery.toString());
					
					pstmt.setString(1, subItem);
	//				//ecamsLogger.error(((LoggableStatement)pstmt).getQueryString());           
			        rs = pstmt.executeQuery();
			        		        
					if (rs.next()){		
						//rst.put("ID", Integer.toString(rs.getRow()));
						rst.put("acptno", baseFile.get("acptno"));
						rst.put("cr_rsrcname", rs.getString("cr_rsrcname"));
						rst.put("jawon", rs.getString("jawon"));
						rst.put("checkin", rs.getString("cm_codename"));
						rst.put("cr_lstver", Integer.toString(rs.getInt("cr_lstver")));
						rst.put("cr_itemid", rs.getString("cr_itemid"));
						rst.put("cr_rsrccd", rs.getString("cr_rsrccd"));
						rst.put("cr_lastdate", rs.getString("lastdate"));
						rst.put("cm_info", rs.getString("cm_info"));
						rst.put("cr_status", rs.getString("cr_status"));
						rst.put("prcseq", rs.getString("prcreq"));
						if (rs.getInt("vercnt") == 0) {
							if (rs.getInt("cr_lstver") >= 9999) rst.put("cr_aftver", "1");
							else rst.put("cr_aftver", Integer.toString(rs.getInt("cr_lstver")+1));
						} else {
							if (rs.getInt("cr_lstver") >= rs.getInt("vercnt")) {
								rst.put("cr_aftver", "1");	
							} else rst.put("cr_aftver", Integer.toString(rs.getInt("cr_lstver")+1));
						}
						if (rs.getInt("cr_lstver") > 0) rst.put("reqcd", "04");
						else rst.put("reqcd", "03");
						
						rst.put("selected_flag","0");
						
					} else {
						rst.put("errsw","1");
						rst.put("errmsg","원장없음");
					}
					rs.close();
					pstmt.close();
				}
				
				
				rsval.add(rst);
				ecamsLogger.error("+++++rst+++++++"+fileList.get(i)+", "+rst.toString());
			}
			
			conn.close();
			conn = null;
			
			return rsval.toArray();
			
		} catch (SQLException sqlexception) {
			sqlexception.printStackTrace();
			ecamsLogger.error("## Cmr0200.fileOpenChk() SQLException START ##");
			ecamsLogger.error("## Error DESC : ", sqlexception);	
			ecamsLogger.error("## Cmr0200.fileOpenChk() SQLException END ##");			
			throw sqlexception;
		} catch (Exception exception) {
			exception.printStackTrace();
			ecamsLogger.error("## Cmr0200.fileOpenChk() Exception START ##");				
			ecamsLogger.error("## Error DESC : ", exception);	
			ecamsLogger.error("## Cmr0200.fileOpenChk() Exception END ##");				
			throw exception;
		}finally{
			if (rsval != null)  rsval = null;
			if (rs != null)     try{rs.close();}catch (Exception ex){ex.printStackTrace();}
			if (rs2 != null)    try{rs2.close();}catch (Exception ex1){ex1.printStackTrace();}
			if (pstmt != null)  try{pstmt.close();}catch (Exception ex2){ex2.printStackTrace();}
			if (pstmt2 != null) try{pstmt2.close();}catch (Exception ex4){ex4.printStackTrace();}
			if (conn != null){
				try{
					ConnectionResource.release(conn);
				}catch(Exception ex3){
					ecamsLogger.error("## Cmr0200.fileOpenChk() connection release exception ##");
					ex3.printStackTrace();
				}
			}
		}
	}//end of fileOpenChk() method statement

	public Object[] getProgOrders(String AcptNo) throws SQLException, Exception {
	Connection        	conn        = null;
	PreparedStatement 	pstmt       = null;
	PreparedStatement 	pstmt2      = null;
	ResultSet         	rs          = null;
	ResultSet         	rs2         = null;
	StringBuffer      	strQuery    = new StringBuffer();
	ArrayList<HashMap<String, String>>  rsval = new ArrayList<HashMap<String, String>>();
	HashMap<String,String> rst = new HashMap<String,String>();
	int                 i = 0;
	
	ConnectionContext connectionContext = new ConnectionResource();
	try {
		conn = connectionContext.getConnection();
		strQuery.setLength(0);
		strQuery.append("select cr_itemid,cr_orderid,cr_reqsub from cmr1012 where cr_acptno = ? order by cr_itemid\n");
		pstmt = conn.prepareStatement(strQuery.toString());
		pstmt.setString(1, AcptNo);
		rs = pstmt.executeQuery();
		
		
		while(rs.next()){
			rst = new HashMap<String, String>();
			
			rst.put("CC_ORDERID", rs.getString("cr_orderid"));
			rst.put("CC_REQSUB", rs.getString("cr_reqsub"));
			rst.put("CR_ITEMID", rs.getString("cr_itemid"));
			
			rsval.add(rst);
			rst = null;
		}
		
		rs.close();
		pstmt.close();
		rs = null;
		pstmt = null;
		
		conn.close();
		conn = null;
		
		return rsval.toArray();
		
	} catch (SQLException sqlexception) {
		sqlexception.printStackTrace();
		ecamsLogger.error("## Cmr0200.getProgOrders() SQLException START ##");
		ecamsLogger.error("## Error DESC : ", sqlexception);	
		ecamsLogger.error("## Cmr0200.getProgOrders() SQLException END ##");			
		throw sqlexception;
	} catch (Exception exception) {
		exception.printStackTrace();
		ecamsLogger.error("## Cmr0200.getProgOrders() Exception START ##");				
		ecamsLogger.error("## Error DESC : ", exception);	
		ecamsLogger.error("## Cmr0200.getProgOrders() Exception END ##");				
		throw exception;
	}finally{
		if (rsval != null)  rsval = null;
		if (rs != null)     try{rs.close();}catch (Exception ex){ex.printStackTrace();}
		if (rs2 != null)    try{rs2.close();}catch (Exception ex1){ex1.printStackTrace();}
		if (pstmt != null)  try{pstmt.close();}catch (Exception ex2){ex2.printStackTrace();}
		if (pstmt2 != null) try{pstmt2.close();}catch (Exception ex4){ex4.printStackTrace();}
		if (conn != null){
			try{
				ConnectionResource.release(conn);
			}catch(Exception ex3){
				ecamsLogger.error("## Cmr0200.getProgOrders() connection release exception ##");
				ex3.printStackTrace();
			}
		}
	}
}//end of getProgOrders() method statement
	
	/**
	 * <PRE>
	 * 1. MethodName	: setPlanConfirm
	 * 2. ClassName		: Cmr0200
	 * 3. Commnet			: 작업계획서 결재상신 요청.
	 * 4. 작성자				: no name
	 * 5. 작성일				: 2011. 1. 21. 오후 2:37:20
	 * </PRE>
	 * 		@return String
	 * 		@param etcData : 작업계획서 기본정보
	 * 		@param ConfList : 결재정보
	 * 		@return
	 * 		@throws SQLException
	 * 		@throws Exception
	 */
	public String setPlanConfirm(HashMap<String,String> etcData,ArrayList<HashMap<String,Object>> ConfList) throws SQLException, Exception {
		Connection        conn        = null;
		PreparedStatement pstmt       = null;
		ResultSet         rs          = null;
		StringBuffer      strQuery    = new StringBuffer();
		String            retMsg      = "0";
		AutoSeq			  autoseq	  = new AutoSeq();
		
		ConnectionContext connectionContext = new ConnectionResource();		
		
		try {
			
			conn = connectionContext.getConnection();
			
			String AcptNo = "";
			int i = 0;
			if ("".equals(etcData.get("CC_ISRID")) || etcData.get("CC_ISRID") == null){
				return "ISRID 정보가 없습니다. 관리자에게 문의해주십시오.";
			}
			
			//작업계획서 신청번호 받아오기 '47'
        	do {
		        AcptNo = autoseq.getSeqNo(conn,etcData.get("reqcd"));

		        i = 0;
		        strQuery.setLength(0);		        
		        strQuery.append("select count(*) as cnt from cmr9900 \n");
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
        	
        	
            //작업계획서작성 시작
            if (!this.setJobPlanInfo(etcData).equals("0")){
				conn.rollback();
				conn.close();
				throw new Exception("작업계획서 등록 중 오류가 발생하였습니다. 관리자에게 연락하여 주시기 바랍니다.");
            }
            //작업계획서작성 끝
            
			conn.setAutoCommit(false);  
			
			strQuery.setLength(0);
			strQuery.append("update cmc0250 set cc_status='1',    \n");
			strQuery.append("       cc_reqdate=SYSDATE,cc_acptno=? \n");
			strQuery.append(" where cc_isrid=? and cc_isrsub=?    \n");
			strQuery.append("   and cc_scmuser=?                  \n");
			pstmt = conn.prepareStatement(strQuery.toString());
//			pstmt = new LoggableStatement(conn,strQuery.toString());
			pstmt.setString(1, AcptNo);
			pstmt.setString(2, etcData.get("CC_ISRID"));
			pstmt.setString(3, etcData.get("CC_ISRSUB"));
			pstmt.setString(4, etcData.get("CC_SCMUSER"));
//			//ecamsLogger.error(((LoggableStatement)pstmt).getQueryString());  
			pstmt.executeUpdate();
			pstmt.close();
			
    		Cmc0010 cmc0010 = new Cmc0010();
    		retMsg = cmc0010.base_Confirm(AcptNo,etcData.get("CC_ISRID"),etcData.get("CC_ISRSUB"),etcData.get("CC_SCMUSER"),etcData.get("reqcd"),conn);
    		if (!retMsg.equals("0")) {
				conn.rollback();
				conn.close();
				throw new Exception("요청정보 등록 중 오류가 발생하였습니다. 관리자에게 연락하여 주시기 바랍니다.");
    		}
    		cmc0010 = null;
    		
    		Cmr0200 cmr0200 = new Cmr0200();
			retMsg = cmr0200.request_Confirm(AcptNo,"99999",etcData.get("reqcd"),etcData.get("CC_SCMUSER"),true,ConfList,conn);
			if (!retMsg.equals("OK")) {
				conn.rollback();
				conn.close();
				throw new Exception("결재정보 등록 중 오류가 발생하였습니다. 관리자에게 연락하여 주시기 바랍니다.");
			}
			cmr0200 = null;

			conn.commit();
	        conn.close();
	        conn = null;
	        
	        return "0";
	        
	        
		} catch (SQLException sqlexception) {
			sqlexception.printStackTrace();
			if (rs != null)     try{rs.close();}catch (Exception ex){ex.printStackTrace();}
			if (pstmt != null)  try{pstmt.close();}catch (Exception ex2){ex2.printStackTrace();}
			if (conn != null){
				try{
					conn.rollback();
					ConnectionResource.release(conn);
				}catch(Exception ex3){
					ecamsLogger.error("## Cmr0200.setPlanConfirm() connection release exception ##");
					ex3.printStackTrace();
				}				
			}			
			ecamsLogger.error("## Cmr0200.setPlanConfirm() SQLException START ##");
			ecamsLogger.error("## Error DESC : ", sqlexception);	
			ecamsLogger.error("## Cmr0200.setPlanConfirm() SQLException END ##");			
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
					ecamsLogger.error("## Cmr0200.setPlanConfirm() connection release exception ##");
					ex3.printStackTrace();
				}
			}
			ecamsLogger.error("## Cmr0200.setPlanConfirm() Exception START ##");				
			ecamsLogger.error("## Error DESC : ", exception);	
			ecamsLogger.error("## Cmr0200.setPlanConfirm() Exception END ##");				
			throw exception;
		}finally{
			if (strQuery != null) 	strQuery = null;
			if (rs != null)     try{rs.close();}catch (Exception ex){ex.printStackTrace();}
			if (pstmt != null)  try{pstmt.close();}catch (Exception ex2){ex2.printStackTrace();}
			if (conn != null){
				try{
					conn.commit();
					conn.close();
					conn = null;
				}catch(Exception ex3){
					ecamsLogger.error("## Cmr0200.setPlanConfirm() connection release exception ##");
					ex3.printStackTrace();
				}
			}
		}
	}//end of setPlanConfirm() method statement
	
	public Object[] getComProgLst() throws SQLException, Exception {
		Connection        conn        = null;
		PreparedStatement pstmt       = null;
		ResultSet         rs          = null;
		StringBuffer      strQuery    = new StringBuffer();
		Object[] 		  returnObject= null;
		ArrayList<HashMap<String, String>>  rtList	= new ArrayList<HashMap<String, String>>();
		HashMap<String, String>			  	rst	 	= null;	
		
		ConnectionContext connectionContext = new ConnectionResource();
		
		try {
			
			rtList.clear();
			
			conn = connectionContext.getConnection();
			
			
			
			
			strQuery.setLength(0);
			strQuery.append("select a.cc_seq, b.cm_codename, a.cc_subject, a.cc_subcd, a.cc_subsrc, a.cc_proggbn, a.cc_gbn, a.cc_confsrc	 \n");
			strQuery.append(" from cmc0430 a, cmm0020 b 													 	    		 				 \n");
			strQuery.append(" where b.cm_macode='PROGGBN' and b.cm_micode <> '****' and a.cc_proggbn = b.cm_micode 			 				 \n");
			strQuery.append(" group by a.cc_gbn, a.cc_proggbn,a.cc_seq, b.cm_codename, a.cc_subject, a.cc_subcd, a.cc_subsrc, a.cc_confsrc   \n");
		    strQuery.append(" order by a.cc_gbn, a.cc_proggbn, a.cc_seq																	     \n");
				
			pstmt = conn.prepareStatement(strQuery.toString());
			//pstmt = new LoggableStatement(conn,strQuery.toString());
		
			//ecamsLogger.error(((LoggableStatement)pstmt).getQueryString());
			rs = pstmt.executeQuery();
			
			while (rs.next()){
				rst = new HashMap<String, String>();
	        	rst.put("cc_seq", rs.getString("cc_seq"));
	        	rst.put("cc_gbn", rs.getString("cc_gbn"));
	        	rst.put("cc_proggbn", rs.getString("cc_proggbn"));
	        	rst.put("cc_subject", rs.getString("cc_subject"));	        	
	        	rst.put("cc_subcd", rs.getString("cc_subcd"));
	        	rst.put("cc_subsrc", rs.getString("cc_subsrc"));
	        	rst.put("cc_confsrc", rs.getString("cc_confsrc"));
	        	rst.put("cm_codename", rs.getString("cm_codename"));
	        	rtList.add(rst);
        		rst = null;
        		
			}
			
			rs.close();
			pstmt.close();
			conn.close();

			rs = null;
			pstmt = null;
			conn = null;
			
			returnObject = rtList.toArray();
			rtList.clear();
			rtList = null;
			
			return returnObject;			
			
		} catch (SQLException sqlexception) {
			sqlexception.printStackTrace();
			ecamsLogger.error("## CMR0200 SQLException START ##");
			ecamsLogger.error("## Error DESC : ", sqlexception);	
			ecamsLogger.error("## CMR0200 SQLException END ##");			
			throw sqlexception;
		} catch (Exception exception) {
			exception.printStackTrace();
			ecamsLogger.error("## CMR0200 Exception START ##");				
			ecamsLogger.error("## Error DESC : ", exception);	
			ecamsLogger.error("## CMR0200 Exception END ##");				
			throw exception;
		}finally{
			if (strQuery != null) 	strQuery = null;
			if (returnObject != null)	returnObject = null;
			if (rs != null)     try{rs.close();}catch (Exception ex){ex.printStackTrace();}
			if (pstmt != null)  try{pstmt.close();}catch (Exception ex2){ex2.printStackTrace();}
			if (conn != null){
				try{
					conn.close();
				}catch(Exception ex3){
					ecamsLogger.error("## CMR0200 getComProgLst() connection release exception ##");
					ex3.printStackTrace();
				}
			}
		}
	}//end of SelectList() method statement
	
	public Object[] getprivProgLst(String acptNO) throws SQLException, Exception {
		boolean           isCncl  			 = false;
		Connection        conn        = null;
		PreparedStatement pstmt       = null;
		ResultSet         rs          = null;
		StringBuffer      strQuery    = new StringBuffer();
		Object[] 		  returnObject= null;
		ArrayList<HashMap<String, String>>  rtList	= new ArrayList<HashMap<String, String>>();
		HashMap<String, String>			  	rst	 	= null;	
		ConnectionContext connectionContext = new ConnectionResource();
		
		try {
			
			rtList.clear();
			
			conn = connectionContext.getConnection();
		
			strQuery.setLength(0);
			strQuery.append("select a.cc_acptno,																								\n");
			strQuery.append("       a.cc_seq,																									\n");
			strQuery.append("       (select cm_codename from   cmm0020 																			\n");
			strQuery.append("        	where cm_macode = 'PROGGBN' 																			\n");
			strQuery.append("        		and cm_micode <> '****' 																			\n");
			strQuery.append("        			and a.cc_proggbn = cm_micode) as cm_codename ,													\n");
			strQuery.append("       a.cc_subject,																								\n");
			strQuery.append("       a.cc_subcd,																									\n");
			strQuery.append("       a.cc_subsrc,																								\n");
			strQuery.append("       a.cc_proggbn,																								\n");
			strQuery.append("       a.cc_gbn,																									\n");
			strQuery.append("       a.cc_confsrc,																								\n");
			strQuery.append("		 a.cc_etc																										\n");		
			strQuery.append("from   cmc0440 a																									\n");
			strQuery.append("where a.cc_acptno = ?																								\n");
			strQuery.append("group by a.cc_acptno, a.cc_gbn, a.cc_proggbn, a.cc_seq, a.cc_subject, a.cc_subcd, a.cc_subsrc, a.cc_confsrc, a.cc_etc		\n");
			strQuery.append("order by a.cc_gbn, a.cc_proggbn, a.cc_seq   																		\n");														 
				
			pstmt = conn.prepareStatement(strQuery.toString());
			pstmt.setString(1, acptNO);
		
			rs = pstmt.executeQuery();
			
			while (rs.next()){
			
				rst = new HashMap<String, String>();
	        	rst.put("cc_seq", rs.getString("cc_seq"));      
	        	rst.put("cc_proggbn", rs.getString("cc_proggbn"));
	        	rst.put("cc_gbn", rs.getString("cc_gbn"));
	        	rst.put("cc_subject", rs.getString("cc_subject"));	        	
	        	rst.put("cc_subcd", rs.getString("cc_subcd"));
	        	rst.put("cc_subsrc", rs.getString("cc_subsrc"));
	        	rst.put("cc_confsrc", rs.getString("cc_confsrc"));
	        	rst.put("cm_codename", rs.getString("cm_codename"));
	        	rst.put("cc_bigo", rs.getString("cc_etc"));
	        	rst.put("cc_etc", rs.getString("cc_etc"));
	        	
	        	rtList.add(rst);
        		rst = null;
        		
			}
			
			rs.close();
			pstmt.close();
			conn.close();

			rs = null;
			pstmt = null;
			conn = null;
			
			returnObject = rtList.toArray();
			rtList.clear();
			rtList = null;
			
			return returnObject;			
			
		} catch (SQLException sqlexception) {
			sqlexception.printStackTrace();
			ecamsLogger.error("## CMR0200 SQLException START ##");
			ecamsLogger.error("## Error DESC : ", sqlexception);	
			ecamsLogger.error("## CMR0200 SQLException END ##");			
			throw sqlexception;
		} catch (Exception exception) {
			exception.printStackTrace();
			ecamsLogger.error("## CMR0200 Exception START ##");				
			ecamsLogger.error("## Error DESC : ", exception);	
			ecamsLogger.error("## CMR0200 Exception END ##");				
			throw exception;
		}finally{
			if (strQuery != null) 	strQuery = null;
			if (returnObject != null)	returnObject = null;
			if (rs != null)     try{rs.close();}catch (Exception ex){ex.printStackTrace();}
			if (pstmt != null)  try{pstmt.close();}catch (Exception ex2){ex2.printStackTrace();}
			if (conn != null){
				try{
					conn.close();
				}catch(Exception ex3){
					ecamsLogger.error("## CMR0200 getprivProgLst() connection release exception ##");
					ex3.printStackTrace();
				}
			}
		}
	}//end of SelectList() method statement
	
	
	public Object[] getProgSubStat(String AcptNo, String UserId) throws SQLException, Exception {
		boolean           isCncl  			 = false;
		Connection        conn        = null;
		PreparedStatement pstmt       = null;
		ResultSet         rs          = null;
		StringBuffer      strQuery    = new StringBuffer();
		Object[] 		  returnObject= null;
		ArrayList<HashMap<String, String>>  rtList	= new ArrayList<HashMap<String, String>>();
		HashMap<String, String>			  	rst	 	= null;	
		ConnectionContext connectionContext = new ConnectionResource();
		
		try {
			
			rtList.clear();
			
			conn = connectionContext.getConnection();
			strQuery.setLength(0);
			strQuery.append("select decode (a.cr_status, '0', 1, 0) as isGoing, 																					\n");																		 																				
			strQuery.append("decode (a.cr_editor, ? ,1,0) as isCaller,																								\n");
			strQuery.append("(select count(*) from cmr9900 where cr_acptno = a.cr_acptno and cr_status = '3') as isCncl, 											\n");
			strQuery.append("(select decode(cr_status, '9', 1,0) from cmr9900 where cr_acptno = a.cr_acptno and cr_team = 'Q1' and cr_locat <> '00') as isQAConf	\n");
			strQuery.append("from cmr1000 a where a.cr_acptno = ? 																									\n");

																											
			pstmt = conn.prepareStatement(strQuery.toString());
			pstmt.setString(1, UserId);
			pstmt.setString(2,	AcptNo);
			rs = pstmt.executeQuery();
			
			while (rs.next()){
				rst = new HashMap<String, String>();
	        	rst.put("isQAConf", Integer.toString(rs.getInt("isQAConf")));
	        	rst.put("isCaller", Integer.toString(rs.getInt("isCaller")));
	        	rst.put("isCncl", Integer.toString(rs.getInt("isCncl")));
	        	rst.put("isGoing", Integer.toString(rs.getInt("isGoing")));
	        	rtList.add(rst);
        		rst = null;
        		
			}
			
			rs.close();
			pstmt.close();
			conn.close();

			rs = null;
			pstmt = null;
			conn = null;
			
			returnObject = rtList.toArray();
			rtList.clear();
			rtList = null;
			
			return returnObject;			
			
		} catch (SQLException sqlexception) {
			sqlexception.printStackTrace();
			ecamsLogger.error("## CMR0200.getProgSubStat() SQLException START ##");
			ecamsLogger.error("## Error DESC : ", sqlexception);	
			ecamsLogger.error("## CMR0200.getProgSubStat() SQLException END ##");			
			throw sqlexception;
		} catch (Exception exception) {
			exception.printStackTrace();
			ecamsLogger.error("## CMR0200.getProgSubStat() Exception START ##");				
			ecamsLogger.error("## Error DESC : ", exception);	
			ecamsLogger.error("## CMR0200.getProgSubStat() Exception END ##");				
			throw exception;
		}finally{
			if (strQuery != null) 	strQuery = null;
			if (returnObject != null)	returnObject = null;
			if (rs != null)     try{rs.close();}catch (Exception ex){ex.printStackTrace();}
			if (pstmt != null)  try{pstmt.close();}catch (Exception ex2){ex2.printStackTrace();}
			if (conn != null){
				try{
					conn.close();
				}catch(Exception ex3){
					ecamsLogger.error("## CMR0200 getProgSubStat() connection release exception ##");
					ex3.printStackTrace();
				}
			}
		}
	}//end of getProgSubStat() method statement
	
	public Object[] getProgListInfo(String AcptNo) throws SQLException, Exception {
		Connection        conn        = null;
		PreparedStatement pstmt       = null;
		ResultSet         rs          = null;
		StringBuffer      strQuery    = new StringBuffer();
		Object[] 		  returnObject= null;
		ArrayList<HashMap<String, String>>  rtList	= new ArrayList<HashMap<String, String>>();
		HashMap<String, String>			  	rst	 	= null;	
		
		ConnectionContext connectionContext = new ConnectionResource();
		
		try {
			
			rtList.clear();
			
			conn = connectionContext.getConnection();
			
			
			
			
			strQuery.setLength(0);
			strQuery.append("select a.cc_seq, b.cm_codename, a.cc_subject, a.cc_subcd, a.cc_subsrc, a.cc_proggbn, a.cc_gbn, a.cc_confsrc, a.cc_etc	 \n");
			strQuery.append(" from cmc0440 a, cmm0020 b 													 	    		 				 \n");
			strQuery.append(" where a.cc_acptno = ? and  b.cm_macode='PROGGBN' and b.cm_micode <> '****' and a.cc_proggbn = b.cm_micode 			 				 \n");
			strQuery.append(" group by a.cc_gbn, a.cc_proggbn,a.cc_seq, b.cm_codename, a.cc_subject, a.cc_subcd, a.cc_subsrc, a.cc_confsrc, a.cc_etc   \n");
		    strQuery.append(" order by a.cc_gbn, a.cc_proggbn, a.cc_seq																	     \n");
				
			pstmt = conn.prepareStatement(strQuery.toString());
			//pstmt = new LoggableStatement(conn,strQuery.toString());
			pstmt.setString(1, AcptNo);
			//ecamsLogger.error(((LoggableStatement)pstmt).getQueryString());
			rs = pstmt.executeQuery();
			
			while (rs.next()){
				rst = new HashMap<String, String>();
	        	rst.put("cc_seq", rs.getString("cc_seq"));
	        	rst.put("cc_gbn", rs.getString("cc_gbn"));
	        	rst.put("cc_proggbn", rs.getString("cc_proggbn"));
	        	rst.put("cc_subject", rs.getString("cc_subject"));	        	
	        	rst.put("cc_subcd", rs.getString("cc_subcd"));
	        	rst.put("cc_subsrc", rs.getString("cc_subsrc"));
	        	rst.put("cc_confsrc", rs.getString("cc_confsrc"));
	        	rst.put("cm_codename", rs.getString("cm_codename"));
	        	rst.put("cc_bigo", rs.getString("cc_etc"));
	        	rtList.add(rst);
        		rst = null;
        		
			}
			
			rs.close();
			pstmt.close();
			conn.close();

			rs = null;
			pstmt = null;
			conn = null;
			
			returnObject = rtList.toArray();
			rtList.clear();
			rtList = null;
			
			return returnObject;			
			
		} catch (SQLException sqlexception) {
			sqlexception.printStackTrace();
			ecamsLogger.error("## CMR0200.getProgListInfo SQLException START ##");
			ecamsLogger.error("## Error DESC : ", sqlexception);	
			ecamsLogger.error("## CMR0200.getProgListInfo SQLException END ##");			
			throw sqlexception;
		} catch (Exception exception) {
			exception.printStackTrace();
			ecamsLogger.error("## CMR0200.getProgListInfo Exception START ##");				
			ecamsLogger.error("## Error DESC : ", exception);	
			ecamsLogger.error("## CMR0200.getProgListInfo Exception END ##");				
			throw exception;
		}finally{
			if (strQuery != null) 	strQuery = null;
			if (returnObject != null)	returnObject = null;
			if (rs != null)     try{rs.close();}catch (Exception ex){ex.printStackTrace();}
			if (pstmt != null)  try{pstmt.close();}catch (Exception ex2){ex2.printStackTrace();}
			if (conn != null){
				try{
					conn.close();
				}catch(Exception ex3){
					ecamsLogger.error("## CMR0200.getProgListInfo() connection release exception ##");
					ex3.printStackTrace();
				}
			}
		}
	}//end of getProgListInfo() method statement
	
	
	public boolean chkRechkIn(String AcptNo, String UserId) throws SQLException, Exception {
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
				strQuery.append("select 																											\n");
				strQuery.append("(select cr_status from cmr0020 where b.cr_itemid = cr_itemid) as progstatus, b.cr_editor, b.cr_rsrcname,b.cr_qrycd	\n");
				strQuery.append("from cmr1000 a, cmr1010 b ,cmm0036 c																		\n");
				strQuery.append("where a.cr_acptno = ?																						\n");	
				strQuery.append("and a.cr_acptno = b.cr_acptno																				\n");	
				strQuery.append("and a.cr_editor = ?																						\n");
				strQuery.append("and b.cr_baseitem = b.cr_itemid																				\n");	
				strQuery.append("and b.cr_syscd = c.cm_syscd																				\n");
				strQuery.append("and b.cr_rsrccd = c.cm_rsrccd																				\n");
				strQuery.append("and substr(c.cm_info,26,1) = '0'																			\n");
				pstmt = conn.prepareStatement(strQuery.toString());
				pstmt.setString(1,AcptNo);
				pstmt.setString(2,UserId);
				rs = pstmt.executeQuery();
				String  tmp1 = "";
				String  tmp2 = "";
				String tmp3 = "";
				while (rs.next()){
					tmp1 = rs.getString("progstatus");
					tmp2 = rs.getString("cr_editor");
					tmp3 = rs.getString("cr_qrycd");
					if(tmp3.equals("04")){
						if(!tmp1.equals("5") || !tmp2.equals(UserId)){
							rtnValue = false;
							break;
						}
					}
					else {
						if(!tmp1.equals("3") || !tmp2.equals(UserId)){
							rtnValue = false;
							break;
						}
					}
						
				}//end of while-loop statement
				
				rs.close();
				pstmt.close();
				strQuery.setLength(0);
				strQuery.append("select (select cc_status from cmc0420 where b.cr_orderid = cc_orderid) as orderstatus		\n");								
				strQuery.append("from cmr1000 a, cmr1012 b  																\n");
				strQuery.append("where a.cr_acptno = ? 															 			\n");						
				strQuery.append("and a.cr_acptno = b.cr_acptno																\n");						
				strQuery.append("and a.cr_editor = ? 																		\n");
				pstmt = conn.prepareStatement(strQuery.toString());
				pstmt.setString(1,AcptNo);
				pstmt.setString(2,UserId);
				rs = pstmt.executeQuery();
				while (rs.next()){
					tmp1 = rs.getString("orderstatus");
					if(tmp1.equals("9")){
						rtnValue = false;
						break;
					}
				}//end of while-loop statement
				
			}
			else
				rtnValue = false;
	       
			rs.close();
			pstmt.close();
			conn.close();
			conn = null;
			pstmt = null;
			rs = null;
	
			return rtnValue;
			
		} catch (SQLException sqlexception) {
			sqlexception.printStackTrace();
			ecamsLogger.error("## Cmr0200.chkRechkIn() SQLException START ##");
			ecamsLogger.error("## Error DESC : ", sqlexception);	
			ecamsLogger.error("## Cmr0200.chkRechkIn() SQLException END ##");			
			throw sqlexception;
		} catch (Exception exception) {
			exception.printStackTrace();
			ecamsLogger.error("## Cmr0200.chkRechkIn() Exception START ##");				
			ecamsLogger.error("## Error DESC : ", exception);	
			ecamsLogger.error("## Cmr0200.chkRechkIn() Exception END ##");				
			throw exception;
		}finally{
			if (strQuery != null) 	strQuery = null;
			if (rs != null)     try{rs.close();}catch (Exception ex){ex.printStackTrace();}
			if (pstmt != null)  try{pstmt.close();}catch (Exception ex2){ex2.printStackTrace();}
			if (conn != null){
				try{
					ConnectionResource.release(conn);
				}catch(Exception ex3){
					ecamsLogger.error("## Cmr0200.chkRechkIn() connection release exception ##");
					ex3.printStackTrace();
				}
			}
		}
	}//end of chkRechkIn() method statement
	
	
	public boolean setConfList(String AcptNo, ArrayList<HashMap<String,String>> ConfList) throws SQLException, Exception {
		Connection        conn        = null;
		ResultSet         rs 		  = null;
		PreparedStatement pstmt       = null;
		StringBuffer      strQuery    = new StringBuffer();
		HashMap<String, String>			  rst		  		  = null;
		//String           strDevHome   = null;
		ConnectionContext connectionContext = new ConnectionResource();
		boolean 				rtnValue = true;
		try {
			conn = connectionContext.getConnection();
			strQuery.setLength(0);
			strQuery.append("select sum(decode(a.cc_acptno, ? ,1,0)) as cnt, max(a.cc_seq) as max from cmc0440 a \n");
			pstmt = conn.prepareStatement(strQuery.toString());
			pstmt.setString(1,AcptNo);
			rs = pstmt.executeQuery();
			rs.next();
			int existCnt = rs.getInt("cnt");
			int seqCnt = rs.getInt("max");
			if(existCnt > 0 ){
				pstmt.close();
				strQuery.setLength(0);
				strQuery.append("delete cmc0440 where cc_acptno = ?\n");
				pstmt = conn.prepareStatement(strQuery.toString());
				pstmt.setString(1, AcptNo);
				pstmt.executeUpdate();
				
			
				int i;
	         	for(i=0;i<ConfList.size();i++){
	        		
	         		pstmt.close();
		            strQuery.setLength(0);
		        	strQuery.append("insert into cmc0440 \n");
		        	strQuery.append("(CC_ACPTNO,CC_SEQ,CC_PROGGBN,CC_SUBJECT,CC_SUBCD,CC_SUBSRC,CC_ETC,CC_GBN,CC_CONFSRC	\n");
		        	strQuery.append(") values ( 																\n");
		        	strQuery.append("?,?,?,?,?,?,?,?,?)									 						\n");
		        	pstmt = conn.prepareStatement(strQuery.toString());
		        	pstmt = new LoggableStatement(conn,strQuery.toString());
		        	pstmt.setString(1, AcptNo);
		        	pstmt.setInt(2, ++seqCnt );
		        	pstmt.setString(3, ConfList.get(i).get("cc_proggbn"));
		        	pstmt.setString(4, ConfList.get(i).get("cc_subject"));
		        	pstmt.setString(5, ConfList.get(i).get("cc_subcd"));
		        	pstmt.setString(6, ConfList.get(i).get("cc_subsrc"));
		        	pstmt.setString(7, ConfList.get(i).get("cc_bigo"));
		        	pstmt.setString(8, ConfList.get(i).get("cc_gbn"));
		        	pstmt.setString(9, ConfList.get(i).get("cc_confsrc"));
		        	
		        	ecamsLogger.error(((LoggableStatement)pstmt).getQueryString());
		        	pstmt.executeUpdate();
		        	
	         	}
         		if(i == ConfList.size())
	        	rtnValue = true;
         		//conn.commit();
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
			ecamsLogger.error("## Cmr0200.setConfList() SQLException START ##");
			ecamsLogger.error("## Error DESC : ", sqlexception);	
			ecamsLogger.error("## Cmr0200.setConfList() SQLException END ##");			
			throw sqlexception;
		} catch (Exception exception) {
			exception.printStackTrace();
			ecamsLogger.error("## Cmr0200.setConfList() Exception START ##");				
			ecamsLogger.error("## Error DESC : ", exception);	
			ecamsLogger.error("## Cmr0200.setConfList() Exception END ##");				
			throw exception;
		}finally{
			if (strQuery != null) 	strQuery = null;
			if (rs != null)     try{rs.close();}catch (Exception ex){ex.printStackTrace();}
			if (pstmt != null)  try{pstmt.close();}catch (Exception ex2){ex2.printStackTrace();}
			if (conn != null){
				try{
					ConnectionResource.release(conn);
				}catch(Exception ex3){
					ecamsLogger.error("## Cmr0200.setConfList() connection release exception ##");
					ex3.printStackTrace();
				}
			}
		}
	}//end of setConfList() method statement
	
	public String request_homep_Check_In(ArrayList<HashMap<String,String>> chkInList,HashMap<String,String> etcData,ArrayList<HashMap<String,String>> befJob,ArrayList<HashMap<String,Object>> ConfList,ArrayList<HashMap<String,String>> TestList,ArrayList<HashMap<String,String>> OrderList,String confFg) throws SQLException, Exception {
		Connection        conn        = null;
		PreparedStatement pstmt       = null;
		PreparedStatement pstmt2      = null;
		PreparedStatement pstmt3      = null;
		ResultSet         rs          = null;
		ResultSet         rs2         = null;
		StringBuffer      strQuery    = new StringBuffer();
		ConnectionContext connectionContext = new ConnectionResource();
		int				  pstmtcount  = 1;
		AutoSeq			  autoseq	  = new AutoSeq();
		UserInfo		  userInfo	  = new UserInfo();
		String			  AcptNo	  = null;
		String			  AcptNoC	  = null;
		int				  i=0;
		int				  cnt=0;
		int				  vcnt=0;

		try {
			
			conn = connectionContext.getConnection();
	        
	        String strTeam = userInfo.getUserInfo_sub(conn,etcData.get("UserID"),"cm_project");
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
	        //ArrayList<HashMap<String,Object>> conflist = null;
	        int wkC = chkInList.size()/3000;
	        int wkD = chkInList.size()%3000;
	        if (wkD>0) wkC = wkC + 1;
            String svAcpt[] = null; 
            svAcpt = new String [wkC];
            int maxseq = 0;
            strQuery.setLength(0);                		
    		strQuery.append("select max(cc_seq) as max	 	\n");               		
    		strQuery.append("  from cmc0440 												\n");  
    		pstmt = conn.prepareStatement(strQuery.toString());	
	        rs = pstmt.executeQuery();
	        if (rs.next()) {
	        	maxseq = rs.getInt("max");
	        }
	        rs.close();
	        pstmt.close();
            
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
            for(int c =0;c<svAcpt.length;c++){
            	AcptNoC=svAcpt[c];
            	for(int v=0;v<TestList.size();v++){
            		pstmtcount = 1;
            		
		            strQuery.setLength(0);
		        	strQuery.append("insert into cmc0440 \n");
		        	strQuery.append("(CC_ACPTNO,CC_SEQ,CC_PROGGBN,CC_SUBJECT,CC_SUBCD,CC_SUBSRC,CC_ETC,CC_GBN,CC_CONFSRC	\n");
		        	strQuery.append(") values ( 																\n");
		        	strQuery.append("?,?,?,?,?,?,?,?,?)									 						\n");
		        	pstmt = conn.prepareStatement(strQuery.toString());
		        	//pstmt = new LoggableStatement(conn,strQuery.toString());
		        	pstmt.setString(pstmtcount++, AcptNoC);
		        	pstmt.setInt(pstmtcount++, ++maxseq);
		        	pstmt.setString(pstmtcount++, TestList.get(v).get("cc_proggbn"));
		        	pstmt.setString(pstmtcount++, TestList.get(v).get("cc_subject"));
		        	pstmt.setString(pstmtcount++, TestList.get(v).get("cc_subcd"));
		        	pstmt.setString(pstmtcount++, TestList.get(v).get("cc_subsrc"));
		        	pstmt.setString(pstmtcount++, TestList.get(v).get("cc_bigo"));
		        	pstmt.setString(pstmtcount++, TestList.get(v).get("cc_gbn"));
		        	pstmt.setString(pstmtcount++, TestList.get(v).get("cc_confsrc"));
		        	
		        	//ecamsLogger.error(((LoggableStatement)pstmt).getQueryString());
		        	pstmt.executeUpdate();
		        	
		        	pstmt.close();
            	}
			}
            
        	int    seq = 0;
        	int    j   = 0;
        	String retMsg = "";
            autoseq = null;
            conn.setAutoCommit(false);			
        	boolean insSw = false;
        	String strBasePgm = "";
        	String strBase2[] = null;
        	for (i=0;i<chkInList.size();i++){
        		insSw = false;
        		if (i == 0) insSw = true;
        		else {
        			wkC = i%3000;
        			if (wkC == 0) insSw = true;
        		}
        		if (insSw == true) {        			
        			if (i>=3000) {
        	        	if (etcData.get("ReqCD").equals("60") && etcData.get("TstSw").equals("1")) {
        	        		strQuery.setLength(0);
        	        		strQuery.append("insert into cmr1010 (CR_ACPTNO,CR_SERNO,CR_SYSCD,CR_SYSGB,CR_JOBCD,   							\n");
        	        		strQuery.append("   CR_STATUS,CR_QRYCD,CR_RSRCCD,CR_DSNCD,CR_DSNCD2,CR_RSRCNAME,       							\n");
        	        		strQuery.append("   CR_RSRCNAM2,CR_SRCCHG,CR_SRCCMP,CR_PRIORITY,CR_APLYDATE,           							\n");
        					strQuery.append("   CR_VERSION,CR_BEFVER,CR_CONFNO,CR_EDITOR,CR_BASENO,CR_BASEITEM,    							\n");
        					strQuery.append("   CR_ITEMID,CR_EDITCON,CR_BASEPGM,CR_IMPORTANT,CR_NEWGLO,CR_DEALCODE)							\n");
        					strQuery.append(" (select a.cr_acptno,                                                							\n");
        					strQuery.append("    (select max(cr_serno) from cmr1010 where cr_acptno=?)+rownum,     							\n");
        					strQuery.append("    a.cr_syscd,a.CR_SYSGB,b.CR_JOBCD,'0',b.CR_QRYCD,b.CR_RSRCCD,     							\n");
        					strQuery.append("    b.CR_DSNCD,b.CR_DSNCD2,b.CR_RSRCNAME,b.CR_RSRCNAM2,b.CR_SRCCHG,   							\n");
        					strQuery.append("    'Y',b.CR_PRIORITY,a.CR_APLYDATE,b.CR_VERSION,b.CR_BEFVER,         							\n");
        					strQuery.append("    a.CR_CONFNO,a.CR_EDITOR,a.CR_BASENO,a.CR_BASEITEM,b.CR_ITEMID,    							\n"); 
        					strQuery.append("    a.CR_EDITCON,b.CR_BASEPGM,b.CR_IMPORTANT,b.CR_NEWGLO,b.CR_DEALCODE							\n");    
        					strQuery.append("    from cmr1010 a,cmr1010 b                                          							\n");
        					strQuery.append("  where a.cr_acptno=? and a.cr_itemid=a.cr_baseitem                   							\n"); 
        					strQuery.append("    and a.cr_confno=b.cr_acptno and b.cr_status<>'3'                  							\n");
        					strQuery.append("    and instr(nvl(b.cr_basepgm,b.cr_baseitem),a.cr_itemid)>0          							\n");
        					strQuery.append("    and a.cr_itemid<>b.cr_itemid and a.cr_status<>'3')                							\n");  
        					pstmt = conn.prepareStatement(strQuery.toString());
        	        		pstmtcount = 1;
        	        		pstmt.setString(pstmtcount++, AcptNo);
        	        		pstmt.setString(pstmtcount++, AcptNo);

        	            	pstmt.executeUpdate();
        	            	pstmt.close();
        	            	
        	            	strQuery.setLength(0);
        	            	strQuery.append("select a.cr_basepgm,a.cr_serno    \n");
        	            	strQuery.append("  from cmr1010 a                  \n");
        	            	strQuery.append(" where a.cr_acptno=?              \n");
        	            	strQuery.append("   and a.cr_basepgm is not null   \n");
        	            	strQuery.append("   and a.cr_itemid<>a.cr_baseitem \n");
        	            	strQuery.append("   and a.cr_baseitem<>a.cr_basepgm \n");
        	            	pstmt = conn.prepareStatement(strQuery.toString());
        	                pstmt.setString(1, AcptNo);
        	                rs = pstmt.executeQuery();
        	            	while (rs.next()) {
        	            		strBase2 = rs.getString("cr_basepgm").split(","); 
        	            		strBasePgm = "";
        	            		for (j=0;strBase2.length>j;j++) {
        	            			strQuery.setLength(0);
        	            			strQuery.append("select count(*) cnt from cmr1010 \n");
        	            			strQuery.append(" where cr_acptno=?               \n");
        	            			strQuery.append("   and cr_itemid=?               \n");
        	            			pstmt2 = conn.prepareStatement(strQuery.toString());
        	            			pstmt2.setString(1, AcptNo);
        	            			pstmt2.setString(2, strBase2[j]);
        	            			rs2 = pstmt2.executeQuery();
        	            			if (rs2.next()) {
        	            				if (rs2.getInt("cnt")>0) {
        	            					if (strBasePgm.length()>0) strBasePgm = strBasePgm + ",";
        	            					strBasePgm = strBasePgm + strBase2[j];
        	            					 
        	            				}
        	            			}
        	            			rs2.close();
        	            			pstmt2.close();
        	            		}
        	            		
        	            		if (!rs.getString("cr_basepgm").equals(strBasePgm)) {
        		            		strQuery.setLength(0);
        		            		strQuery.append("update cmr1010 set cr_basepgm=?  \n");
        		            		strQuery.append(" where cr_acptno=?               \n");
        		            		strQuery.append("   and cr_serno=?                \n");
        		            		pstmt2 = conn.prepareStatement(strQuery.toString());
        		            		pstmt2 = new LoggableStatement(conn,strQuery.toString());
        		        			pstmt2.setString(1, strBasePgm);
        		        			pstmt2.setString(2, AcptNo);
        		        			pstmt2.setString(3, rs.getString("cr_serno"));
        		        			ecamsLogger.error(((LoggableStatement)pstmt2).getQueryString());
        		        			pstmt2.executeUpdate();
        		        			pstmt2.close();
        	            		}
        	        			
        	            	}
        	            	rs.close();
        	            	pstmt.close();
        	            	
        	            	strQuery.setLength(0);
        	            	strQuery.append("select cr_itemid                          \n");
        	            	strQuery.append("  from cmr1010 where cr_acptno=?          \n");
        	            	strQuery.append(" group by cr_itemid                       \n");
        	            	strQuery.append(" having count(*)>1                        \n");
        	            	pstmt = conn.prepareStatement(strQuery.toString());
        	                pstmt.setString(1, AcptNo);
        	                rs = pstmt.executeQuery();
        	            	while (rs.next()) {
        	            		strBasePgm = "";
        	            		strQuery.setLength(0);
            	            	strQuery.append("select cr_baseitem,cr_serno   \n");
            	            	strQuery.append("  from cmr1010                \n");
            	            	strQuery.append(" where cr_acptno=?            \n");
            	            	strQuery.append("   and cr_itemid=?            \n");
            	            	strQuery.append("   and cr_itemid<>cr_baseitem \n");
            	            	strQuery.append(" order by cr_serno            \n");
            	            	pstmt2 = conn.prepareStatement(strQuery.toString());
            	                pstmt2.setString(1, AcptNo);
            	                pstmt2.setString(2, rs.getString("cr_itemid"));
            	                rs2 = pstmt2.executeQuery();
            	            	while (rs2.next()) {            	            		
            	            		if (strBasePgm.length() > 0) strBasePgm = strBasePgm + ",";
            	            		strBasePgm = strBasePgm + rs2.getString("cr_baseitem");
            	            		
            	            		if (rs2.getRow()>1) {
	            	            		strQuery.setLength(0);
	            	            		strQuery.append("delete cmr1010      \n");
	            	            		strQuery.append(" where cr_acptno=?  \n");
	            	            		strQuery.append("   and cr_serno=?   \n");
	            	            		pstmt3 = conn.prepareStatement(strQuery.toString());
	                	                pstmt3.setString(1, AcptNo);
	                	                pstmt3.setString(2, rs2.getString("cr_serno"));
	                	                pstmt3.executeUpdate();
	                	                pstmt3.close();
            	            		}
            	            	}
            	            	rs2.close();
            	            	pstmt2.close();
            	            	
            	            	if (strBasePgm.length()>0) {
	        	            		strQuery.setLength(0);
	        	            		strQuery.append("update cmr1010 set cr_basepgm=?        \n");
	        	            		strQuery.append(" where cr_acptno=?                     \n");
	        	            		strQuery.append("   and cr_itemid=?                     \n");
	        	            		pstmt2 = conn.prepareStatement(strQuery.toString());
	        	            		pstmt2 = new LoggableStatement(conn,strQuery.toString());
	        	            		pstmt2.setString(1, strBasePgm);
	        	            		pstmt2.setString(2, AcptNo);
	        	            		pstmt2.setString(3, rs.getString("cr_itemid"));
	        	            		ecamsLogger.error(((LoggableStatement)pstmt2).getQueryString());
	        	            		pstmt2.executeUpdate();
	        	            		pstmt2.close();
            	            	} else {
	        	            		strQuery.setLength(0);
	        	            		strQuery.append("delete cmr1010 where cr_acptno=?      \n");
	        	            		strQuery.append("   and cr_itemid=?                    \n");
	        	            		strQuery.append("   and cr_serno not in (select cr_serno from cmr1010 \n");
	        	            		strQuery.append("                         where cr_acptno=?           \n");
	        	            		strQuery.append("                           and cr_itemid=?           \n");
	        	            		strQuery.append("                           and rownum<2)             \n");
	        	            		pstmt2 = conn.prepareStatement(strQuery.toString());
	        	            		pstmt2.setString(1, AcptNo);
	        	            		pstmt2.setString(2, rs.getString("cr_itemid"));
	        	            		pstmt2.setString(3, AcptNo);
	        	            		pstmt2.setString(4, rs.getString("cr_itemid"));
	        	            		pstmt2.executeUpdate();
	        	            		pstmt2.close();
            	            	}
        	            	}
        	            	rs.close();
        	            	pstmt.close();
        	        	}
        	        	
        				retMsg = cmr0200.request_Confirm(AcptNo,chkInList.get(0).get("cr_syscd"),etcData.get("ReqCD"),etcData.get("UserID"),true,ConfList,conn);
        				if (!retMsg.equals("OK")) {
        					conn.rollback();
        					conn.close();
        					throw new Exception("결재정보등록 중 오류가 발생하였습니다. 관리자에게 연락하여 주시기 바랍니다.");
        				}
        			}
        			
        			wkC = i/3000;
        			AcptNo = svAcpt[wkC];
        			//ecamsLogger.error("++++ i, wkC ++++++"+ Integer.toString(i)+", "+ Integer.toString(wkC));
        			pstmtcount = 1;
        			strQuery.setLength(0);
                	strQuery.append("insert into cmr1000 \n");
                	strQuery.append("(CR_ACPTNO,CR_SYSCD,CR_SYSGB,CR_JOBCD,CR_ACPTDATE,CR_STATUS,CR_TEAMCD,CR_QRYCD, \n");
                	strQuery.append("CR_PASSOK,CR_PASSCD,CR_BEFJOB,CR_EMGCD,CR_EDITOR,CR_SAYU,CR_PASSSUB,CR_SAYUCD,  \n");
                	strQuery.append("CR_BASEUP,CR_DEVPTIME,CR_TESTDATE,CR_ETCSAYU) values ( \n");
                	strQuery.append("?,?,?,?,sysdate,'0',?,?,  ?,?,?,?,?,?,?,?,  ?,?,?,?) \n");
                	
                	pstmt = conn.prepareStatement(strQuery.toString());
                	//pstmt = new LoggableStatement(conn,strQuery.toString());
                	pstmt.setString(pstmtcount++, AcptNo);
                	pstmt.setString(pstmtcount++, chkInList.get(0).get("cr_syscd"));
                	pstmt.setString(pstmtcount++, chkInList.get(0).get("sysgb"));
                	pstmt.setString(pstmtcount++, chkInList.get(0).get("cr_jobcd"));
                	pstmt.setString(pstmtcount++, strTeam);
                	pstmt.setString(pstmtcount++, etcData.get("ReqCD"));
                	
                	
                	pstmt.setString(pstmtcount++, etcData.get("Deploy"));
                	pstmt.setString(pstmtcount++, strRequest);
                	if (befJob.size() > 0) pstmt.setString(pstmtcount++, "Y");
                	else pstmt.setString(pstmtcount++, "N");
                	pstmt.setString(pstmtcount++, etcData.get("ReqSayu"));
                	pstmt.setString(pstmtcount++, etcData.get("UserID"));
                	pstmt.setString(pstmtcount++, chkInList.get(0).get("cr_sayu"));
                	pstmt.setString(pstmtcount++, etcData.get("EmgCd"));
                	pstmt.setString(pstmtcount++, etcData.get("PassCd"));
                	
                	
                	//pstmt.setString(pstmtcount++, etcData.get("DocNo"));
                	pstmt.setString(pstmtcount++, etcData.get("upload"));
                	//if (etcData.get("ReqCD").equals("03") || !etcData.get("Deploy").equals("4")) {
//                	if (etcData.get("Deploy").equals("4")) {
//                		pstmt.setString(pstmtcount++,etcData.get("AplyDate"));
//                	} else {
//                		pstmt.setString(pstmtcount++,"");
//                	}
                	pstmt.setString(pstmtcount++, etcData.get("cr_devptime"));
                	if(etcData.get("etcSayu")!=null && !etcData.get("etcSayu").equals("")){
	                	pstmt.setString(pstmtcount++, etcData.get("testdate"));
	                	pstmt.setString(pstmtcount++, etcData.get("etcSayu"));
                	}else{
                		pstmt.setString(pstmtcount++, "");
	                	pstmt.setString(pstmtcount++, "");
                	}
                	
                	//ecamsLogger.error(((LoggableStatement)pstmt).getQueryString());
                	pstmt.executeUpdate();
                	
                	pstmt.close();
                	seq = 0;
        		}
        		
        		strQuery.setLength(0);
            	strQuery.append("insert into cmr1010 ");
            	strQuery.append("(CR_ACPTNO,CR_SERNO,CR_SYSCD,CR_SYSGB,CR_JOBCD,CR_STATUS,CR_QRYCD,  			\n");
            	strQuery.append("CR_RSRCCD,CR_LANGCD,CR_DSNCD,CR_RSRCNAME,CR_RSRCNAM2,CR_SRCCHG,CR_SRCCMP, 		\n");
            	strQuery.append("CR_PRIORITY,CR_APLYDATE,CR_VERSION,CR_BEFVER,CR_CONFNO,CR_EDITOR,CR_DSNCD2,   	\n");
            	strQuery.append("CR_BASENO,CR_BASEITEM,CR_ITEMID,CR_EDITCON,CR_COACPT,CR_BASEPGM,		      	\n");
            	strQuery.append("CR_IMPORTANT,CR_NEWGLO,CR_DEALCODE,CR_TESTYN) values (\n");
            	strQuery.append("?,?,?,?,?,'0',?,  ?,?,?,?,?,?,'Y',  ?,?,?,?,?,?,?,  ?,?,?,?,?,?,  ?,?,?,?) \n");
            	
            	pstmtcount = 1;
            	pstmt = conn.prepareStatement(strQuery.toString());
            	//pstmt = new LoggableStatement(conn,strQuery.toString());
            	
            	//ecamsLogger.error("++++++reqcd,rsrccd++++++"+chkInList.get(i).get("reqcd")+","
            	//		+chkInList.get(i).get("cr_rsrccd"));
            	
            	pstmt.setString(pstmtcount++, AcptNo);
            	pstmt.setInt(pstmtcount++, ++seq);
            	pstmt.setString(pstmtcount++, chkInList.get(i).get("cr_syscd"));
            	pstmt.setString(pstmtcount++, chkInList.get(i).get("sysgb"));
            	pstmt.setString(pstmtcount++, chkInList.get(i).get("cr_jobcd"));
            	pstmt.setString(pstmtcount++, chkInList.get(i).get("reqcd")); 
            	String tmprsrc = chkInList.get(i).get("cr_rsrccd").substring(0,2);
            	pstmt.setString(pstmtcount++, tmprsrc);
            	pstmt.setString(pstmtcount++, chkInList.get(i).get("cr_langcd"));
            	pstmt.setString(pstmtcount++, chkInList.get(i).get("cr_dsncd"));
            	pstmt.setString(pstmtcount++, chkInList.get(i).get("cr_rsrcname"));
            	pstmt.setString(pstmtcount++, chkInList.get(i).get("cr_rsrcname"));
            	if (chkInList.get(i).get("reqcd").equals("60") || chkInList.get(i).get("reqcd").equals("03")) {
            		pstmt.setString(pstmtcount++,"1");
            	} else {
            		pstmt.setString(pstmtcount++,"0");
            	}
            	pstmt.setInt(pstmtcount++, Integer.parseInt(chkInList.get(i).get("prcseq")));

           
//            	if (chkInList.get(i).get("Deploy").equals("4")) {
//            		pstmt.setString(pstmtcount++,chkInList.get(i).get("AplyDate"));
//            	} else {
            	pstmt.setString(pstmtcount++,"");
//            	}
            	pstmt.setString(pstmtcount++, chkInList.get(i).get("cr_aftver"));
            	pstmt.setString(pstmtcount++, chkInList.get(i).get("cr_lstver"));
            	if (etcData.get("ReqCD").equals("60")) {
            		if (!"".equals(chkInList.get(i).get("cr_acptno")) && chkInList.get(i).get("cr_acptno") != null) {  
            			pstmt.setString(pstmtcount++,chkInList.get(i).get("cr_acptno"));
            		} else {
            			pstmt.setString(pstmtcount++,AcptNo);
            		}
            	} else {
            		pstmt.setString(pstmtcount++,chkInList.get(i).get(""));
            	}
            	pstmt.setString(pstmtcount++, etcData.get("UserID"));
            	pstmt.setString(pstmtcount++, chkInList.get(i).get("cm_dirpath"));
            	
            	if (chkInList.get(i).get("reqcd").equals("05") || chkInList.get(i).get("reqcd").equals("09")) {
            		pstmt.setString(pstmtcount++, chkInList.get(i).get("cr_baseno"));
            	} else {
	        		if (!"".equals(chkInList.get(i).get("cr_acptno")) && chkInList.get(i).get("cr_acptno") != null) {  
	        			pstmt.setString(pstmtcount++,chkInList.get(i).get("cr_acptno"));
	        		} else {
	        			pstmt.setString(pstmtcount++,chkInList.get(i).get(""));
	        		}
            	}
            	pstmt.setString(pstmtcount++, chkInList.get(i).get("baseitem"));
            	pstmt.setString(pstmtcount++, chkInList.get(i).get("cr_itemid"));
            	if (chkInList.get(i).get("cr_sayu") != null && !"".equals(chkInList.get(i).get("cr_sayu"))) 
            		pstmt.setString(pstmtcount++, chkInList.get(i).get("cr_sayu")); 
            	else pstmt.setString(pstmtcount++, "");
            	if (chkInList.get(i).get("reqcd").equals("09")) {
            		pstmt.setString(pstmtcount++, selBaseno(chkInList.get(i).get("cr_itemid"),conn));
            	} else {
            		pstmt.setString(pstmtcount++, AcptNo);
            	}
            	pstmt.setString(pstmtcount++, chkInList.get(i).get("baseitem"));
            	
            	pstmt.setString(pstmtcount++, chkInList.get(i).get("importancecode"));
        		pstmt.setString(pstmtcount++, chkInList.get(i).get("newgoodscode"));
        		pstmt.setString(pstmtcount++, chkInList.get(i).get("dealcode"));
        		pstmt.setString(pstmtcount++, chkInList.get(i).get("testyn"));
            	
            	
            	////ecamsLogger.error(((LoggableStatement)pstmt).getQueryString());
            	pstmt.executeUpdate();
            	pstmt.close();
            	
//            	}
        	}
        	
        	for(int z =0;z<OrderList.size();z++){
        		
//        		if(chkInList.get(i).get("cr_itemid").equals(OrderList.get(z).get("CR_ITEMID"))){
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
	    	        strQuery.append("select count(*) as cnt from cmr1012 \n");
	    	        strQuery.append("where cr_acptno=? and cr_orderid=? \n");
	    	        pstmtcount = 1;
	            	pstmt = conn.prepareStatement(strQuery.toString());
	            	pstmt.setString(pstmtcount++, AcptNo);
	            	//pstmt.setString(pstmtcount++, OrderList.get(z).get("CR_ITEMID"));
                	pstmt.setString(pstmtcount++, OrderList.get(z).get("CC_ORDERID"));
	            	rs = pstmt.executeQuery();
	            	if (rs.next()){
	            		vcnt = rs.getInt("cnt");
	            	}	        	
	            	rs.close();
	            	pstmt.close();
	            	
	            	if(vcnt == 0){
	                	strQuery.setLength(0);
	                	strQuery.append("insert into cmr1012 ");
	                	strQuery.append("(CR_ACPTNO,CR_SEQ,CR_ORDERID,CR_REQSUB) values (					 \n");
	                	strQuery.append("?, ?, ?, ?)");
	                	pstmtcount = 1;
	                	pstmt = conn.prepareStatement(strQuery.toString());
	                	//pstmt = new LoggableStatement(conn, strQuery.toString());
	                	pstmt.setString(pstmtcount++, AcptNo);
	                	pstmt.setInt(pstmtcount++, cnt++);
	                	//pstmt.setString(pstmtcount++, OrderList.get(z).get("CR_ITEMID"));
	                	pstmt.setString(pstmtcount++, OrderList.get(z).get("CC_ORDERID"));
	                	pstmt.setString(pstmtcount++, OrderList.get(z).get("CC_REQSUB"));
	                	//ecamsLogger.error(((LoggableStatement)pstmt).getQueryString());
	                	pstmt.executeUpdate();
	                	pstmt.close();
	            	}
        		}
        	
        	//ecamsLogger.error("+++++++++CHECK-IN LIST Update START (cmr0020)+++ 3705");
        	etcData.put("CC_ACPTNO", AcptNo);
            //ecamsLogger.error("+++++++++CHECK-IN LIST Update START (cmr0020)+++ 3714");
        	
        	
            retMsg = request_Confirm(AcptNo,chkInList.get(0).get("cr_syscd"),etcData.get("ReqCD"),etcData.get("UserID"),true,ConfList,conn);
			if (!retMsg.equals("OK")) {
				conn.rollback();
				conn.close();
				throw new Exception("결재정보등록 중 오류가 발생하였습니다. 관리자에게 연락하여 주시기 바랍니다.");
			} 
			//ecamsLogger.error("+++++++++CHECK-IN LIST Update START (cmr0020)+++ 3721");
  	        conn.commit();
  	        
        	//ecamsLogger.error("+++++++++CHECK-IN LIST Update START (cmr0020)+++ 3844");
        	conn.close();
        	rs = null;
        	pstmt = null;
        	conn = null;
        	
        	//ecamsLogger.error("+++++++++Request E N D+++");
        	return AcptNo;
			
		} catch (SQLException sqlexception) {
			sqlexception.printStackTrace();
			if (rs != null)     try{rs.close();}catch (Exception ex){ex.printStackTrace();}
			if (pstmt != null)  try{pstmt.close();}catch (Exception ex2){ex2.printStackTrace();}
			if (rs2 != null)     try{rs2.close();}catch (Exception ex){ex.printStackTrace();}
			if (pstmt2 != null)  try{pstmt2.close();}catch (Exception ex2){ex2.printStackTrace();}
			if (pstmt3 != null)  try{pstmt3.close();}catch (Exception ex2){ex2.printStackTrace();}
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
			ecamsLogger.error("## Cmr0200.request_Check_In() SQLException START ##");
			ecamsLogger.error("## Error DESC : ", sqlexception);	
			ecamsLogger.error("## Cmr0200.request_Check_In() SQLException END ##");			
			throw sqlexception;
		} catch (Exception exception) {
			exception.printStackTrace();
			if (rs != null)     try{rs.close();}catch (Exception ex){ex.printStackTrace();}
			if (pstmt != null)  try{pstmt.close();}catch (Exception ex2){ex2.printStackTrace();}
			if (rs2 != null)     try{rs2.close();}catch (Exception ex){ex.printStackTrace();}
			if (pstmt2 != null)  try{pstmt2.close();}catch (Exception ex2){ex2.printStackTrace();}
			if (pstmt3 != null)  try{pstmt3.close();}catch (Exception ex2){ex2.printStackTrace();}
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
			ecamsLogger.error("## Cmr0200.request_Check_In() Exception START ##");				
			ecamsLogger.error("## Error DESC : ", exception);	
			ecamsLogger.error("## Cmr0200.request_Check_In() Exception END ##");				
			throw exception;
		}finally{
			if (strQuery != null) 	strQuery = null;
			if (rs != null)     try{rs.close();}catch (Exception ex){ex.printStackTrace();}
			if (pstmt != null)  try{pstmt.close();}catch (Exception ex2){ex2.printStackTrace();}
			if (rs2 != null)     try{rs2.close();}catch (Exception ex){ex.printStackTrace();}
			if (pstmt2 != null)  try{pstmt2.close();}catch (Exception ex2){ex2.printStackTrace();}
			if (pstmt3 != null)  try{pstmt3.close();}catch (Exception ex2){ex2.printStackTrace();}
			if (conn != null){
				try{					
					ConnectionResource.release(conn);
					conn = null;
				}catch(Exception ex3){
					ecamsLogger.error("## Cmr0200.request_Check_In() connection release exception ##");
					ex3.printStackTrace();
				}
			}
		}		
	}
	
	/**	 반려된 건 적용 재요청시에 기존 파일을 옮겨줍니다. 
	 * @param AcptNo  		 새로 신청된 AcptNo
	 * @param befAcptNo		 재신청한 AcptNo
	 * @param FileList			 추가 또는 재신청된 파일 리스트 (FileList.get(n).get("isCopy") == "true" 이면 재신청 된 파일, 재신청된 파일을 새로 신청된 AcptNo로 DB에 값을 넣는다.)		
	 * @return boolean
	 * @throws SQLException
	 * @throws Exception
	 */
	public int copyDoc2(String AcptNo, ArrayList<HashMap<String,String>> FileList) throws SQLException, Exception {
		SystemPath		  cTempGet	  = new SystemPath();
		String			  tmpPath = "";
		String			  docPath = "";
		String			  strBinPath = "";
		Connection        conn        = null;
	
		PreparedStatement pstmt       = null;
		StringBuffer      strQuery    = new StringBuffer();
		
		//String           strDevHome   = null;
		ConnectionContext connectionContext = new ConnectionResource();
		
		File shfile=null;
		String  shFileName = "";
	
		OutputStreamWriter writer = null;
		String[] strAry = null;
		Runtime  run = null;
		Process p = null;
		int rtn = 0;
		
		try {
	
			conn = connectionContext.getConnection();
			
			tmpPath = cTempGet.getTmpDir("99");
			strBinPath = cTempGet.getTmpDir("14");
			docPath = cTempGet.getTmpDir("21");
			for(int i = 0;i<FileList.size();i++){
				if(FileList.get(i).get("isCopy").equals("false"))
					continue;
			
		    	shFileName = tmpPath+"/"+AcptNo+Integer.toString(i)+"_ecamcpydoc.sh"; 
				//fileName = tmpPath+"/"+AcptNo+rs.getString("cc_subreq")+rs.getString("cc_seq")+"_ecamcpysrc.sh";
				
				shfile = new File(shFileName);
				
				if( !(shfile.isFile()) )              //File이 없으면 
				{
					shfile.createNewFile();          //File 생성
				}
				
			
				String strcnt = "";
				if(Integer.toString(i).length() == 1)
					strcnt = "00"+Integer.toString(i);
				else if (Integer.toString(i).length() == 2)
					strcnt = "0" + Integer.toString(i);
				else
					strcnt = Integer.toString(i);
				String strtmp = FileList.get(i).get("cc_savefile");
				System.out.println(Integer.toString(FileList.size()));
				System.out.println(Integer.toString(FileList.size()));
				System.out.println(Integer.toString(FileList.size()));
				System.out.println(Integer.toString(FileList.size()));
				System.out.println(Integer.toString(FileList.size()));
				System.out.println(Integer.toString(FileList.size()));
				System.out.println(strtmp);
				System.out.println(strtmp);
				System.out.println(strtmp);
				strtmp = strtmp.replace(FileList.get(i).get("cc_id")+"/", "");
				
				String extname = strtmp.substring(5, strtmp.length());
				String filename = AcptNo+"/"+FileList.get(i).get("cc_subid")+strcnt+extname;
				
				
				writer = new OutputStreamWriter( new FileOutputStream(shFileName));
				writer.write("cd "+strBinPath +"\n");
				//ecamsLogger.debug("#########      ItemId : " + ItemId + "  Version : " + Version + "  AcptNo : " + AcptNo);
				writer.write("mkdir "+docPath+"/"+AcptNo+"\n");
				writer.write("cp '"+docPath+ "/"+FileList.get(i).get("cc_savefile")+"'"+" '"+docPath+"/"+filename+"'"+"\n");
				writer.close();
				

				System.out.println();
				System.out.println();
				System.out.println();
				System.out.println();
				System.out.println();
				
				
				System.out.println(filename);
				System.out.println(extname);
				System.out.println("mkdir "+docPath+"/"+AcptNo+"\n");
				System.out.println("cp  "+docPath+ "/"+FileList.get(i).get("cc_savefile")+" "+docPath+"/"+filename+"\n");
				
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
				
				
				if(p.exitValue() != 0){
					if(conn != null) {
						conn.close();
						conn = null;
					}
					throw new Exception("첨부 파일 전송 실패 : [FileName : "+FileList.get(i).get("cc_attfile")+"]" );
				}
		
				strQuery.setLength(0);
	    		strQuery.append("insert into cmc1001 (cc_id, cc_subid, cc_subreq, cc_seqno, cc_savefile, cc_attfile, cc_lastdt, cc_editor, cc_reqcd) 		\n");
	    		strQuery.append("select ?, cc_subid, cc_subreq, ?, ? , cc_attfile, cc_lastdt, cc_editor, cc_reqcd				\n");
	    		strQuery.append("from cmc1001																															\n");
	    		strQuery.append("where cc_id = ? and cc_seqno = ? 																								\n");
	         	pstmt = conn.prepareStatement(strQuery.toString());
	         	pstmt.setString(1, AcptNo);
	         	pstmt.setString(2, Integer.toString(i));
	         	pstmt.setString(3, filename);
	         	pstmt.setString(4, FileList.get(i).get("cr_acptno"));
	         	pstmt.setString(5, FileList.get(i).get("cc_seqno"));
	         	rtn = pstmt.executeUpdate();
				pstmt.close();
				pstmt = null;
				
				strcnt = "";
				filename = "";
				strtmp = "";
				extname = "";
				filename = "";
				shFileName = "";
				shfile = null;
			}
			conn.close();
			conn = null;
			
			return rtn;
			
		} catch (SQLException sqlexception) {
			sqlexception.printStackTrace();
			ecamsLogger.error("## Cmr0200.copyDoc2() SQLException START ##");
			ecamsLogger.error("## Error DESC : ", sqlexception);	
			ecamsLogger.error("## Cmr0200.copyDoc2() SQLException END ##");			
			throw sqlexception;
		} catch (Exception exception) {
			exception.printStackTrace();
			ecamsLogger.error("## Cmr0200.copyDoc2() Exception START ##");				
			ecamsLogger.error("## Error DESC : ", exception);	
			ecamsLogger.error("## Cmr0200.copyDoc2() Exception END ##");				
			throw exception;
		}finally{
			if (strQuery != null) 	strQuery = null;
			
				
			if (pstmt != null)  try{pstmt.close();}catch (Exception ex2){ex2.printStackTrace();}
			if (conn != null){
				try{
					ConnectionResource.release(conn);
				}catch(Exception ex3){
					ecamsLogger.error("## Cmr0200.copyDoc2() connection release exception ##");
					ex3.printStackTrace();
				}
			}
		}
	}//end of copyDoc2() method statement
	
	public int copyDoc(String AcptNo, String befAcptNo, ArrayList<HashMap<String,String>> FileList) throws SQLException, Exception {
		SystemPath		  cTempGet	  = new SystemPath();
		String			  tmpPath = "";
		String			  docPath = "";
		String			  strBinPath = "";
		Connection        conn        = null;
	
		PreparedStatement pstmt       = null;
		StringBuffer      strQuery    = new StringBuffer();
		
		//String           strDevHome   = null;
		ConnectionContext connectionContext = new ConnectionResource();
		
		File shfile=null;
		String  shFileName = "";
	
		OutputStreamWriter writer = null;
		String[] strAry = null;
		Runtime  run = null;
		Process p = null;
		int rtn = 0;
		
		try {
	
			conn = connectionContext.getConnection();
			
			tmpPath = cTempGet.getTmpDir("99");
			strBinPath = cTempGet.getTmpDir("14");
			docPath = cTempGet.getTmpDir("21");
			for(int i = 0;i<FileList.size();i++){
		    	shFileName = tmpPath+"/"+AcptNo+"_ecamcpydoc.sh"; 
				//fileName = tmpPath+"/"+AcptNo+rs.getString("cc_subreq")+rs.getString("cc_seq")+"_ecamcpysrc.sh";
				
				shfile = new File(shFileName);
				
				if( !(shfile.isFile()) )              //File이 없으면 
				{
					shfile.createNewFile();          //File 생성
				}
				String filename = FileList.get(i).get("cc_savefile").replace(befAcptNo+"/", "");
				writer = new OutputStreamWriter( new FileOutputStream(shFileName));
				writer.write("cd "+strBinPath +"\n");
				//ecamsLogger.debug("#########      ItemId : " + ItemId + "  Version : " + Version + "  AcptNo : " + AcptNo);
				writer.write("mkdir "+docPath+"/"+AcptNo+"\n");
				writer.write("cp  '"+docPath+ "/"+befAcptNo+"/"+filename+"'"+" '"+docPath+"/"+AcptNo+"/"+filename+"'"+"\n");
				
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
				
				filename = "";
				
				
				if(p.exitValue() != 0){
					if(conn != null) {
						conn.close();
						conn = null;
					}
					throw new Exception("첨부 파일 전송 실패 : [FileName : "+FileList.get(i).get("cc_attfile")+"]" );
				}
		
				strQuery.setLength(0);
	    		strQuery.append("insert into cmc1001 (cc_id, cc_subid, cc_subreq, cc_seqno, cc_savefile, cc_attfile, cc_lastdt, cc_editor, cc_reqcd) 		\n");
	    		strQuery.append("select ?, cc_subid, cc_subreq, cc_seqno, replace(cc_savefile,?, ?), cc_attfile, cc_lastdt, cc_editor, cc_reqcd				\n");
	    		strQuery.append("from cmc1001																															\n");
	    		strQuery.append("where cc_id = ? and cc_seqno = ? 																														\n");
	         	pstmt = conn.prepareStatement(strQuery.toString());
	         	pstmt.setString(1, AcptNo);
	         	pstmt.setString(2, befAcptNo);
	         	pstmt.setString(3, AcptNo);
	         	pstmt.setString(4, befAcptNo);
	         	pstmt.setString(5, FileList.get(i).get("cc_seqno"));
	         	rtn = pstmt.executeUpdate();
				pstmt.close();
				pstmt = null;
			}
			conn.close();
			conn = null;
			
			return rtn;
			
		} catch (SQLException sqlexception) {
			sqlexception.printStackTrace();
			ecamsLogger.error("## Cmr0200.copyDoc() SQLException START ##");
			ecamsLogger.error("## Error DESC : ", sqlexception);	
			ecamsLogger.error("## Cmr0200.copyDoc() SQLException END ##");			
			throw sqlexception;
		} catch (Exception exception) {
			exception.printStackTrace();
			ecamsLogger.error("## Cmr0200.copyDoc() Exception START ##");				
			ecamsLogger.error("## Error DESC : ", exception);	
			ecamsLogger.error("## Cmr0200.copyDoc() Exception END ##");				
			throw exception;
		}finally{
			if (strQuery != null) 	strQuery = null;
			
				
			if (pstmt != null)  try{pstmt.close();}catch (Exception ex2){ex2.printStackTrace();}
			if (conn != null){
				try{
					ConnectionResource.release(conn);
				}catch(Exception ex3){
					ecamsLogger.error("## Cmr0200.copyDoc() connection release exception ##");
					ex3.printStackTrace();
				}
			}
		}
	}//end of copyDoc() method statement
	public String filechk(ArrayList<HashMap<String,String>> fileList) throws SQLException, Exception {
		Connection        conn        = null;
		Connection        connD        = null;
		PreparedStatement pstmt       = null;
		ResultSet         rs          = null;
		StringBuffer      strQuery    = new StringBuffer();
		int               rsCnt       = 0;
		int				  i			  = 0;
		String			  tmprsrc     = "";
		ConnectionContext connectionContext = new ConnectionResource();
		ConnectionContext connectionContextD = new ConnectionResource(false,"D");
		
		try {
			conn = connectionContext.getConnection();
			connD = connectionContextD.getConnection();
			
			if(fileList.get(i).get("cm_info").substring(57,58).equals("1")){
				for (i=0;i<fileList.size();i++){
					String model_itemid = "";
					strQuery.setLength(0);
					strQuery.append(" select a.cr_itemid				\n");
					strQuery.append("   from cmr0020 a, cmm0070 b       \n");
					strQuery.append("  where a.cr_syscd = ?              \n");
					strQuery.append("    and a.cr_rsrcname = ?          \n");
					strQuery.append("    and b.cm_dirpath = ?           \n");
					strQuery.append("    and a.cr_syscd = b.cm_syscd    \n");
					strQuery.append("    and a.cr_dsncd = b.cm_dsncd    \n");
					pstmt = connD.prepareStatement(strQuery.toString());
					//pstmt = new LoggableStatement(connD, strQuery.toString());
					pstmt.setString(1, fileList.get(i).get("cr_syscd"));
					pstmt.setString(2, fileList.get(i).get("cr_rsrcname"));
					pstmt.setString(3, fileList.get(i).get("cm_dirpath"));
					//ecamsLogger.error(((LoggableStatement)pstmt).getQueryString());
					rs = pstmt.executeQuery();
					if(rs.next()){
						model_itemid = rs.getString("cr_itemid");
					}
					rs.close();
					pstmt.close();
				
					//모델의 itemid가 있으면
					if(!"".equals(model_itemid)){
						//new DB에서 모델의 itemid를 baseitem으로 갖는 java을 구함
						strQuery.setLength(0);
	//					strQuery.append(" select a.cr_syscd, a.cr_rsrcname, b.cm_dirpath,   \n");
	//					strQuery.append("        c.cr_version, c.cr_befver, c.cr_rsrccd,    \n");
	//					strQuery.append("        c.cr_langcd, c.cr_baseno, c.CR_PRIORITY,a.cr_acptno    \n");
						strQuery.append(" select distinct c.cr_rsrccd   					\n");
						strQuery.append("   from cmr0020 a, cmm0070 b, cmr1010 c            \n");
						strQuery.append("  where a.cr_baseitem = ?                          \n");
						strQuery.append("    and a.cr_syscd = b.cm_syscd                    \n");
						strQuery.append("    and a.cr_dsncd = b.cm_dsncd                    \n");
						strQuery.append("    and a.cr_syscd = c.cr_syscd        			\n");
						strQuery.append("    and a.cr_dsncd = c.cr_dsncd        			\n");
						strQuery.append("    and a.cr_itemid = c.cr_itemid        			\n");
						strQuery.append("    and c.cr_confno is null        				\n");
						strQuery.append("    and c.cr_status in ('8','9')        			\n");
						strQuery.append("    AND substr(c.cr_acptno,5,2)='03'				\n");
						pstmt = connD.prepareStatement(strQuery.toString());
						//pstmt = new LoggableStatement(connD, strQuery.toString());
						pstmt.setString(1, model_itemid);
						//ecamsLogger.error(((LoggableStatement)pstmt).getQueryString());
						rs = pstmt.executeQuery();
						while(rs.next()){
							if(tmprsrc.equals("")){
								tmprsrc = rs.getString("cr_rsrccd");
							}else{
								tmprsrc = tmprsrc + ","+ rs.getString("cr_rsrccd");
							}
						}
						rs.close();
					    pstmt.close();
					}
				}
			} 
       
	        conn.close();
	        connD.close();
			rs = null;
			pstmt = null;
			conn = null;
	        //ecamsLogger.error("++++++++confirm_doc end+++++++");
			return tmprsrc;
            
		} catch (SQLException sqlexception) {
			sqlexception.printStackTrace();
			ecamsLogger.error("## Confirm_select.confirmYN() SQLException START ##");
			ecamsLogger.error("## Error DESC : ", sqlexception);	
			ecamsLogger.error("## Confirm_select.confirmYN() SQLException END ##");
			throw sqlexception;
		} catch (Exception exception) {
			exception.printStackTrace();
			ecamsLogger.error("## Confirm_select.confirmYN() Exception START ##");				
			ecamsLogger.error("## Error DESC : ", exception);	
			ecamsLogger.error("## Confirm_select.confirmYN() Exception END ##");
			throw exception;
		}finally{
			if (rs != null)     try{rs.close();}catch (Exception ex){ex.printStackTrace();}
			if (pstmt != null)  try{pstmt.close();}catch (Exception ex2){ex2.printStackTrace();}
			if (conn != null){
				try{
					ConnectionResource.release(conn);
				}catch(Exception ex3){
					ecamsLogger.error("## Confirm_select.confirmYN() connection release exception ##");
					ex3.printStackTrace();
				}
			}
		}
	}//end of confSelect() method statement
	public String getRsaCheck_Dev(ArrayList<HashMap<String,String>> rsaList) throws SQLException, Exception {
		Connection        conn        = null;
		PreparedStatement pstmt       = null;
		ResultSet         rs          = null;
		StringBuffer      strQuery    = new StringBuffer();
		String            retMsg      = "";
		ConnectionContext connectionContext = new ConnectionResource(false,"D");
		try {
			conn = connectionContext.getConnection();
			
			for (int i=0;rsaList.size()>i;i++) {
				strQuery.setLength(0);
				strQuery.append("select to_char(nvl(cr_lstdat,cr_lastdate),'yyyymmdd') lstdat     \n");
				strQuery.append("  from cmr0020                                                   \n");
				strQuery.append(" where cr_itemid=?                                               \n");
				strQuery.append("   and to_char(nvl(cr_lstdat,cr_lastdate),'yyyymmdd')<'20170325' \n");
				pstmt = conn.prepareStatement(strQuery.toString());             
				pstmt.setString(1,rsaList.get(i).get("cr_itemid"));
				rs = pstmt.executeQuery();
				if (rs.next()) {
					if (rs.getInt("lstdat")>Integer.parseInt(rsaList.get(i).get("lstdat"))) {
						if (retMsg.length()>0) retMsg = retMsg + ",";
						retMsg = retMsg + rsaList.get(i).get("cr_rsrcname");
					}
				}
				rs.close();
				pstmt.close();
			}	       
			rs.close();
			pstmt.close();
			conn.close();
			conn = null;
			pstmt = null;
			rs = null;
			
			if (retMsg.length() == 0) retMsg = "OK";
			return retMsg;
			
		} catch (SQLException sqlexception) {
			sqlexception.printStackTrace();
			ecamsLogger.error("## Cmr0200.getRsaCheck_Dev() SQLException START ##");
			ecamsLogger.error("## Error DESC : ", sqlexception);	
			ecamsLogger.error("## Cmr0200.getRsaCheck_Dev() SQLException END ##");			
			throw sqlexception;
		} catch (Exception exception) {
			exception.printStackTrace();
			ecamsLogger.error("## Cmr0200.getRsaCheck_Dev() Exception START ##");				
			ecamsLogger.error("## Error DESC : ", exception);	
			ecamsLogger.error("## Cmr0200.getRsaCheck_Dev() Exception END ##");				
			throw exception;
		}finally{
			if (strQuery != null) 	strQuery = null;
			if (rs != null)     try{rs.close();}catch (Exception ex){ex.printStackTrace();}
			if (pstmt != null)  try{pstmt.close();}catch (Exception ex2){ex2.printStackTrace();}
			if (conn != null){
				try{
					ConnectionResource.release(conn);
				}catch(Exception ex3){
					ecamsLogger.error("## Cmr0200.getRsaCheck_Dev() connection release exception ##");
					ex3.printStackTrace();
				}
			}
		}
	}//end of getRsaCheck_Dev() method statement
}//end of Cmr0200 class statement
