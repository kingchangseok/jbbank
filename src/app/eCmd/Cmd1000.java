package app.eCmd;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.logging.log4j.Logger;

import app.common.AutoSeq;
import app.common.LoggableStatement;
import app.common.UserInfo;

import com.ecams.common.dbconn.ConnectionContext;
import com.ecams.common.dbconn.ConnectionResource;
import com.ecams.common.logger.EcamsLogger;

public class Cmd1000 {
	
	Logger ecamsLogger  = EcamsLogger.getLoggerInstance();

	public Object[] getSysInfo(String UserId,String SecuYn,String SelMsg) throws SQLException, Exception {
		Connection        conn        = null;
		PreparedStatement pstmt       = null;
		ResultSet         rs          = null;
		StringBuffer      strQuery    = new StringBuffer();
		Object[]		  rtObj		  = null;
		ArrayList<HashMap<String, String>>		  rtList	  = new ArrayList<HashMap<String, String>>();
		String            strSelMsg   = "";
		String            strSysCd    = "";
		HashMap<String, String>			  rst		  = null;
		
		ConnectionContext connectionContext = new ConnectionResource();
		
		try {
			conn = connectionContext.getConnection();
					
			if (SelMsg != "") {
				if (SelMsg.toUpperCase().equals("ALL")){
					strSelMsg = "전체";
				}
				else if (SelMsg.toUpperCase().equals("SEL")){
					strSelMsg = "선택하세요";
				}
				else{
					strSelMsg = "";
				}
			}
			boolean adminYn = false;
			if (UserId != null && UserId != "") {
				UserInfo     userinfo = new UserInfo();
				adminYn = userinfo.isAdmin(UserId);
				if(adminYn) SecuYn = "N";
			}
			
			strQuery.setLength(0);
			strQuery.append("select a.cm_syscd,a.cm_sysmsg,a.cm_sysgb,a.cm_sysfc1, \n");
			strQuery.append("       a.cm_dirbase,a.cm_sysinfo,                     \n");
			strQuery.append("       sign(nvl(cm_stopst,to_char(sysdate,'yyyymmddhh24mi')) - to_char(sysdate,'yyyymmddhh24mi')) diff1,\n");
			strQuery.append("       sign(nvl(cm_stoped,to_char(sysdate,'yyyymmddhh24mi')) - to_char(sysdate,'yyyymmddhh24mi')) diff2 \n");
			strQuery.append("from cmm0030 a \n");
			if (SecuYn.toUpperCase().equals("Y")) {
				strQuery.append("where a.cm_syscd in (select cm_syscd from cmm0044 \n");
				strQuery.append("                      where cm_userid=?           \n");
				strQuery.append("                        and cm_closedt is null)   \n");
				strQuery.append("and a.cm_closedt is null                      	   \n");
			} else {
				strQuery.append("where a.cm_closedt is null                    	   \n");
			}
			strQuery.append("  and a.cm_syscd in (select cm_syscd from cmm0036     \n");
			strQuery.append("                      where substr(cm_info,9,1)='1'  \n");
			strQuery.append("                      and cm_closedt is null)  \n");
			strQuery.append("order by a.cm_sysmsg \n");
            pstmt = conn.prepareStatement(strQuery.toString());	
            //pstmt = new LoggableStatement(conn,strQuery.toString());
            
            if (SecuYn.toUpperCase().equals("Y")){
            	pstmt.setString(1, UserId);	
            }
            ////ecamsLogger.error(((LoggableStatement)pstmt).getQueryString());
            rs = pstmt.executeQuery();
            rtList.clear();
			while (rs.next()){
				if (rs.getRow() == 1 && strSelMsg.length() > 0 &&strSelMsg != "" && !strSelMsg.equals("")) {
					rst = new HashMap<String,String>();
					rst.put("cm_syscd", "00000");
					rst.put("cm_sysmsg", strSelMsg);
					rst.put("cm_sysgb", "0");
					rst.put("cm_sysfc1","00");
					rst.put("cm_dirbase","00");
					rst.put("cm_sysinfo", "0");
					rst.put("TstSw", "0");
					rst.put("setyn", "N");
					rtList.add(rst);
					rst = null;
				}
				String tstInfo = rs.getString("cm_sysinfo");
				rst = new HashMap<String,String>();
				rst.put("cm_syscd",rs.getString("cm_syscd"));
				rst.put("cm_sysmsg",rs.getString("cm_sysmsg"));
				rst.put("cm_sysgb",rs.getString("cm_sysgb"));
				rst.put("cm_sysfc1",rs.getString("cm_sysfc1"));
				rst.put("cm_dirbase",rs.getString("cm_dirbase"));
				
				if (rs.getString("cm_sysinfo").substring(4,5).equals("1")) {
					if (rs.getInt("diff1")<0 && rs.getInt("diff2")>0 && adminYn == false) {
						rst.put("cm_stopsw", "1");
						tstInfo = tstInfo.substring(0,4) + "1";
					} else {
						rst.put("cm_stopsw", "0");
						tstInfo = tstInfo.substring(0,4) + "0";
					}
				} else rst.put("cm_stopsw", "0");
				rst.put("cm_sysinfo",tstInfo);
				rst.put("TstSw", "0");
				rst.put("websw", "N");
				if (strSysCd != "") {
					if (strSysCd.equals(rs.getString("cm_syscd"))) rst.put("setyn","Y");
					else rst.put("setyn", "N");
				} else rst.put("setyn", "N");
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
			rtList.clear();
			rtList = null;
			
			return rtObj;			
			
		} catch (SQLException sqlexception) {
			sqlexception.printStackTrace();
			ecamsLogger.error("## Cmd1000.getSysInfo() SQLException START ##");
			ecamsLogger.error("## Error DESC : ", sqlexception);	
			ecamsLogger.error("## Cmd1000.getSysInfo() SQLException END ##");			
			throw sqlexception;
		} catch (Exception exception) {
			exception.printStackTrace();
			ecamsLogger.error("## Cmd1000.getSysInfo() Exception START ##");				
			ecamsLogger.error("## Error DESC : ", exception);	
			ecamsLogger.error("## Cmd1000.getSysInfo() Exception END ##");				
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
					ecamsLogger.error("## Cmd1000.getSysInfo() connection release exception ##");
					ex3.printStackTrace();
				}
			}
		}
	}//end of getSysInfo() method statement
	
	 public String request_Check_In(ArrayList<HashMap<String,String>> chkInList,HashMap<String,String> etcData) throws SQLException, Exception {
			Connection        conn        = null;
			PreparedStatement pstmt       = null;
			PreparedStatement pstmt2      = null;
			ResultSet         rs          = null;
			StringBuffer      strQuery    = new StringBuffer();
			ConnectionContext connectionContext = new ConnectionResource();

			HashMap<String,String>	rData = null;
			int Cnt = 0;
			int i;
			int[] result = new int[chkInList.size()];
			String returnMSG = "";
			int chkData = 0; /// 프로그램,모듈 존재 유무
			
			String itemid = "";
			String prcitem = "";
			int size = chkInList.size();
			
			try {
				conn = connectionContext.getConnection();
				for (i=0; i < chkInList.size(); i++){
					strQuery.setLength(0);
					//strQuery.append(" select cr_rsrcname, cr_itemid from cmr0020 									\n");
					//strQuery.append(" where cr_rsrcname in (														\n");
					strQuery.append(" select a.cr_rsrcname, a.cr_itemid  											\n");
					strQuery.append(" from cmr0020 a, cmm0070 b, cmm0020 c											\n");
					strQuery.append(" where a.cr_syscd = ?															\n");
					strQuery.append(" and a.cr_status <> '9'														\n");
					strQuery.append(" and    a.cr_rsrcname = ?														\n");
					strQuery.append(" and    b.cm_dirpath = ?														\n");
					strQuery.append(" and    a.cr_rsrccd in (                       								\n");
					strQuery.append("							select cm_rsrccd									\n");
					strQuery.append("							from cmm0036										\n");
					strQuery.append("							where cm_syscd = ?									\n");
					strQuery.append("							and substr(cm_info,9,1)='1'  )						\n");
					strQuery.append(" and a.cr_syscd=b.cm_syscd														\n");
					strQuery.append(" and a.cr_dsncd=b.cm_dsncd														\n");
					strQuery.append(" and c.cm_macode='JAWON'														\n");
					strQuery.append(" and c.cm_micode=a.cr_rsrccd 													\n");
					
					pstmt = conn.prepareStatement(strQuery.toString());
					Cnt = 0;
					//pstmt = new LoggableStatement(conn,strQuery.toString());
					pstmt.setString(++Cnt, etcData.get("cm_syscd"));
					pstmt.setString(++Cnt, chkInList.get(i).get("cr_rsrcname"));
					pstmt.setString(++Cnt, chkInList.get(i).get("cm_dirpath"));
					pstmt.setString(++Cnt, etcData.get("cm_syscd"));
					//ecamsLogger.error(((LoggableStatement)pstmt).getQueryString());
					
					rs = pstmt.executeQuery();
					if (rs.next()){
						itemid = rs.getString("cr_itemid");
						chkData = 1; ///DB에 값이 있다면 1
					}
					rs.close();
					pstmt.close();
					
					strQuery.setLength(0);
					strQuery.append(" select cr_rsrcname, cr_itemid from cmr0020 										\n");
					strQuery.append(" where cr_rsrcname in (															\n");
					strQuery.append(" 						select a.cr_rsrcname										\n");
					strQuery.append("						from cmr0020 a, cmm0070 b, cmm0020 c						\n");
					strQuery.append(" 						where a.cr_rsrccd in (	select cm_rsrccd					\n");
					strQuery.append("												from cmm0036						\n");
					strQuery.append("												where  cm_syscd=?					\n");
					strQuery.append("												and    substr(cm_info, 2, 1)='0'	\n");
					strQuery.append("												and    substr(cm_info, 26, 1)='0'	\n");
					strQuery.append("												and    cm_closedt is null        )  \n");
					strQuery.append("						and    a.cr_rsrccd in (	select cm_samersrc				    \n");
					strQuery.append("												from   cmm0037						\n");
					strQuery.append("												where  cm_syscd=?					\n");
					strQuery.append("												and    cm_factcd = '09'				\n");
					strQuery.append("												and    cm_samersrc is not null   )  \n");
					strQuery.append(" and a.cr_syscd=b.cm_syscd															\n");
					strQuery.append(" and    a.cr_dsncd=b.cm_dsncd														\n");
					strQuery.append(" and    c.cm_macode='JAWON'														\n");
					strQuery.append(" and    c.cm_micode=a.cr_rsrccd 													\n");
					strQuery.append(" and    a.cr_rsrcname = ?		 													\n");
					strQuery.append("  )																				\n");
					
					pstmt = conn.prepareStatement(strQuery.toString());
					//pstmt = new LoggableStatement(conn,strQuery.toString());
					Cnt = 0;
					pstmt.setString(++Cnt, etcData.get("cm_syscd"));
					pstmt.setString(++Cnt, etcData.get("cm_syscd"));
					pstmt.setString(++Cnt, chkInList.get(i).get("cr_rsrcname2"));
					//ecamsLogger.error(((LoggableStatement)pstmt).getQueryString());
					rs = pstmt.executeQuery();
					
					if (rs.next()){
						prcitem = rs.getString("cr_itemid");
						chkData = chkData + 1; ///DB에 값이 있다면 1
					}
					rs.close();
					pstmt.close();
					
					if (chkData == 2){///프로그램,모듈 둘 다 DB에 값이 있을 경우
						strQuery.setLength(0);
						strQuery.append(" select 	cd_itemid		\n");
						strQuery.append(" from 		cmd0011			\n");
						strQuery.append(" where		cd_itemid = ?	\n");
						strQuery.append(" and   	cd_prcitem = ? 	\n");
						
						pstmt = conn.prepareStatement(strQuery.toString());
						//pstmt = new LoggableStatement(conn,strQuery.toString());
						pstmt.setString(1, itemid);
						pstmt.setString(2, prcitem);
						//ecamsLogger.error(((LoggableStatement)pstmt).getQueryString());
						rs = pstmt.executeQuery();
						if (rs.next()){
							result[i] = 2;
							/// 중복된 itemid 와 prcitem 이 있으므로 등록실패
							///result[i] = 0;  
						}else{
							strQuery.setLength(0);
			            	strQuery.append("insert into cmd0011                                 \n");        	
			            	strQuery.append("  (cd_itemid,cd_prcitem,cd_editor,cd_lastdt)        \n");      	
			            	strQuery.append(" values                                             \n");
			                strQuery.append("  (?, ?, ?, SYSDATE)                                \n");
			                
			                pstmt2 = conn.prepareStatement(strQuery.toString());
			                //pstmt = new LoggableStatement(conn,strQuery.toString());
			                pstmt2.setString(1, itemid);
			    			pstmt2.setString(2, prcitem);
			                pstmt2.setString(3, etcData.get("UserID"));
			                //ecamsLogger.error(((LoggableStatement)pstmt).getQueryString());
			                
			                result[i] = pstmt2.executeUpdate();
			                pstmt2.close();							
						}
						
						rs.close();
						pstmt.close(); 
						
					}else{
						result[i] = 0; ///DB에 해당 데이터의 정보가 없으므로 등록실패
					}
					strQuery.setLength(0);

				}///for문 끝
				
				for (i=0; i < result.length; i++){
					if (result[i] == 0){
						//returnMSG = "N"; ///일괄등록 실패
						returnMSG = returnMSG + (i+1) + "번 실행모듈 등록 실패 \n"; 
					}else{
						//returnMSG = "Y"; //일괄등록 성공
						returnMSG = returnMSG + (i+1) + "번 실행모듈 등록 성공 \n"; 
					}
				}
	            conn.commit();
	            conn.close();
				pstmt = null;
				pstmt2 = null;
				conn = null;
				return returnMSG;

				
				
			} catch (SQLException sqlexception) {
				sqlexception.printStackTrace();
				if (rs != null)     try{rs.close();}catch (Exception ex){ex.printStackTrace();}
				if (pstmt != null)  try{pstmt.close();}catch (Exception ex2){ex2.printStackTrace();}
				if (conn != null){
					try{
						conn.rollback();
						ConnectionResource.release(conn);
					}catch(Exception ex3){
						ecamsLogger.error("## Cmd1000.request_Check_In() connection release exception ##");
						ex3.printStackTrace();
					}				
				}
				ecamsLogger.error("## Cmd1000.request_Check_In() SQLException START ##");
				ecamsLogger.error("## Error DESC : ", sqlexception);	
				ecamsLogger.error("## Cmd1000.request_Check_In() SQLException END ##");			
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
				ecamsLogger.error("## Cmd1000.request_Check_In() Exception START ##");				
				ecamsLogger.error("## Error DESC : ", exception);	
				ecamsLogger.error("## Cmd1000.request_Check_In() Exception END ##");				
				throw exception;
			}finally{
				if (strQuery != null) 	strQuery = null;
				if (rs != null)     try{rs.close();}catch (Exception ex){ex.printStackTrace();}
				if (pstmt != null)  try{pstmt.close();}catch (Exception ex2){ex2.printStackTrace();}
				if (conn != null){
					try{					
						ConnectionResource.release(conn);
					}catch(Exception ex3){
						ecamsLogger.error("## Cmd1000.request_Check_In() connection release exception ##");
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
				String _sysmsg = dataObj.get("sysmsg");
				
				String _cr_rsrcname = "";
				String _jawon = "";
				String _cm_dirpath = "";
				String _cr_rsrcname2 = "";
				String _jawon2 = "";
				String _cm_dirpath2 = "";
				
				String errMsg = "";
				boolean errsw = false;
				//String _baseItem = "";
				boolean tmpYN = false;
				
				int errCnt = 0;
				
				for (int i=0 ; i<fileList.size() ; i++)
				{
					rst = new HashMap<String, String>();
					rst.put("NO", Integer.toString(i+1));
					rst.put("sysmsg", fileList.get(i).get("sysmsg").trim());
					rst.put("cr_rsrcname", fileList.get(i).get("cr_rsrcname").trim());
					rst.put("jawon", fileList.get(i).get("jawon").trim());
					rst.put("cm_dirpath", fileList.get(i).get("cm_dirpath").trim());
					rst.put("cr_rsrcname2", fileList.get(i).get("cr_rsrcname2").trim());
					rst.put("jawon2", fileList.get(i).get("jawon2").trim());
					rst.put("cm_dirpath2", fileList.get(i).get("cm_dirpath2").trim());
					
					rst.put("_syscd", _syscd);
					rst.put("_sysmsg", _sysmsg);
					
					errMsg = "";
					errsw = false;
					
					if ( fileList.get(i).get("cr_rsrcname").equals("") || fileList.get(i).get("cr_rsrcname") == null ){
						errMsg = "프로그램명 입력없음";
						errsw = true;
					}else{
						strQuery.setLength(0);
						strQuery.append(" select cr_rsrcname from cmr0020 \n");
						strQuery.append(" where cr_rsrcname = ?           \n");
						
						pstmt = conn.prepareStatement(strQuery.toString());
						pstmt.setString(1, fileList.get(i).get("cr_rsrcname"));
						rs = pstmt.executeQuery();
						
						if (rs.next()){
							_cr_rsrcname = rs.getString("cr_rsrcname");
							rst.put("_cr_rsrcname", _cr_rsrcname);
						}else{
							errMsg = errMsg + "미등록 프로그램/";
							errsw = true;
						}
						rs.close();
						pstmt.close();
					}
					
					if (fileList.get(i).get("jawon").equals("") || fileList.get(i).get("jawon").equals(null)){
						errMsg = "프로그램종류 입력없음";
						errsw = true;
					}else{
						strQuery.setLength(0);
						strQuery.append(" select cm_codename from cmm0020 		\n");
						strQuery.append(" where cm_codename in ( ? )            \n");
						
						pstmt = conn.prepareStatement(strQuery.toString());
						pstmt.setString(1, fileList.get(i).get("jawon"));
						rs = pstmt.executeQuery();
						
						if (rs.next()){
							_jawon = rs.getString("cm_codename");
							rst.put("_jawon", _jawon);
						}else{
							errMsg = errMsg + "미등록 프로그램종류/";
							errsw = true;
						}
						rs.close();
						pstmt.close();
					}
					
					if (fileList.get(i).get("cm_dirpath").equals("") || fileList.get(i).get("cm_dirpath").equals(null)){
						errMsg = "프로그램경로 입력없음";
						errsw = true;
					}else{
						strQuery.setLength(0);
						strQuery.append(" select cm_dirpath from cmm0070 		\n");
						strQuery.append(" where cm_dirpath in ( ? )             \n");
						
						pstmt = conn.prepareStatement(strQuery.toString());
						pstmt.setString(1, fileList.get(i).get("cm_dirpath"));
						rs = pstmt.executeQuery();
						
						if (rs.next()){
							_cm_dirpath = rs.getString("cm_dirpath");
							rst.put("_cm_dirpath", _cm_dirpath);
						}else{
							errMsg = errMsg + "미등록 프로그램경로/";
							errsw = true;
						}
						rs.close();
						pstmt.close();
					}
					
					if (fileList.get(i).get("cr_rsrcname2").equals("") || fileList.get(i).get("cr_rsrcname2").equals(null)){
						errMsg = "모듈명 입력없음";
						errsw = true;
					}else{
						strQuery.setLength(0);
						strQuery.append(" select cr_rsrcname from cmr0020 		 \n");
						strQuery.append(" where cr_rsrcname in ( ? )             \n");
						
						pstmt = conn.prepareStatement(strQuery.toString());
						pstmt.setString(1, fileList.get(i).get("cr_rsrcname2"));
						rs = pstmt.executeQuery();
						
						if (rs.next()){
							_cr_rsrcname2 = rs.getString("cr_rsrcname");
							rst.put("_cr_rsrcname2", _cr_rsrcname2);
						}else{
							errMsg = errMsg + "미등록 모듈/";
							errsw = true;
						}
						rs.close();
						pstmt.close();
					}
					
					if (fileList.get(i).get("jawon2").equals("") || fileList.get(i).get("jawon2").equals(null)){
						errMsg = "모듈종류 입력없음";
						errsw = true;
					}else{
						strQuery.setLength(0);
						strQuery.append(" select cm_codename from cmm0020 		 \n");
						strQuery.append(" where cm_codename in ( ? )             \n");
						
						pstmt = conn.prepareStatement(strQuery.toString());
						pstmt.setString(1, fileList.get(i).get("jawon2"));
						rs = pstmt.executeQuery();
						
						if (rs.next()){
							_jawon2 = rs.getString("cm_codename");
							rst.put("_jawon2", _jawon2);
						}else{
							errMsg = errMsg + "미등록 모듈종류/";
							errsw = true;
						}
						rs.close();
						pstmt.close();
					}
					
					if (fileList.get(i).get("cm_dirpath2").equals("") || fileList.get(i).get("cm_dirpath2").equals(null)){
						errMsg = "모듈경로 입력없음";
						errsw = true;
					}else{
						strQuery.setLength(0);
						strQuery.append(" select cm_dirpath from cmm0070 		 \n");
						strQuery.append(" where cm_dirpath in ( ? )             \n");
						
						pstmt = conn.prepareStatement(strQuery.toString());
						pstmt.setString(1, fileList.get(i).get("cm_dirpath2"));
						rs = pstmt.executeQuery();
						
						if (rs.next()){
							_cm_dirpath2 = rs.getString("cm_dirpath");
							rst.put("_cm_dirpath2", _cm_dirpath2);
						}else{
							errMsg = errMsg + "미등록 모듈경로/";
							errsw = true;
						}
						
						if (errsw == true){//에러 있음
							++errCnt;
							rst.put("errsw", "1");
							rst.put("errmsg", errMsg);
						}else{
							rst.put("errsw", "0");
							errMsg = "정상";
							rst.put("errmsg", errMsg);
						}
						
						rs.close();
						pstmt.close();
					}
					

					rsval.add(rst);
					rst = null;
				}
				//rs.close();
				//pstmt.close();
				
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
				ecamsLogger.error("## Cmd1000.getFileList_excel() SQLException START ##");
				ecamsLogger.error("## Error DESC : ", sqlexception);	
				ecamsLogger.error("## Cmd1000.getFileList_excel() SQLException END ##");
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
				ecamsLogger.error("## Cmd1000.getFileList_excel() Exception START ##");				
				ecamsLogger.error("## Error DESC : ", exception);	
				ecamsLogger.error("## Cmd1000.getFileList_excel() Exception END ##");
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
						ecamsLogger.error("## Cmd1000.getFileList_excel() connection release exception ##");
						ex3.printStackTrace();
					}
				}
			}
		}
	
}
