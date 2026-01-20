package app.eCmr;

import java.io.BufferedReader;
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
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.logging.log4j.Logger;

import app.common.LoggableStatement;
import app.common.SystemPath;

import com.ecams.common.dbconn.ConnectionContext;
import com.ecams.common.dbconn.ConnectionResource;
import com.ecams.common.logger.EcamsLogger;

public class Cmr5300 {
	
	Logger ecamsLogger  = EcamsLogger.getLoggerInstance();
	
	/**
	 * <PRE>
	 * 1. MethodName	: getFileText
	 * 2. ClassName		: Cmr5300
	 * 3. Commnet			: 소스의 버전에 맞게 bin/tmp 파일 임시저장 또는 임시저장된 내용을 화면에 출력
	 * 4. 작성자				: no name
	 * 5. 작성일				: 2010. 12. 22. 오전 9:56:53
	 * </PRE>
	 * 		@return String
	 * 		@param ItemId
	 * 		@param Version
	 * 		@param AcptNo
	 * 		@param ynDocFile : 산출물문서 여부(true:산출물)
	 * 		@return
	 * 		@throws Exception
	 */
	public Object[] getFileText(String ItemId,String Version,String AcptNo,boolean ynDocFile,boolean gubun) throws Exception {
		SystemPath		  cTempGet	  = new SystemPath();
		String			  tmpPath = "";
		String			  strBinPath = "";
		Connection        conn        = null;
		PreparedStatement pstmt       = null;
		ResultSet         rs          = null;
		StringBuffer      strQuery    = new StringBuffer();
		ConnectionContext connectionContext = new ConnectionResource();
		
		ArrayList<HashMap<String, String>>  rtList = new ArrayList<HashMap<String, String>>();
		HashMap<String, String>			  	rst	   = null;
		
		File shfile=null;
		String  shFileName = "";
		String	fileName = "";
		OutputStreamWriter writer = null;
		String[] strAry = null;
		Runtime  run = null;
		Process p = null;
		//byte[] byteTmpBuf = null;
		//FileInputStream fis =null;
		BufferedReader in1 = null;
		String	readline1 = "";
		String  rtString ="";
		try {	
			
			tmpPath = cTempGet.getTmpDir("99");
			strBinPath = cTempGet.getTmpDir("14");
			shFileName = tmpPath + "/" + ItemId +"_gensrc.sh"; 
			fileName = tmpPath + "/" + ItemId +"_gensrc.file";
			shfile = new File(shFileName);
			conn = connectionContext.getConnection();
			
			if( !(shfile.isFile()) )              //File이 없으면 
			{
				shfile.createNewFile();          //File 생성
			}
			if (!Version.equals("D")) {				
				if (Version.equals("")){
					if (AcptNo.substring(4,6).equals("04")) {
						strQuery.setLength(0);
						strQuery.append("select cr_status,cr_prcdate,cr_version from cmr1010      \n");
						strQuery.append(" where cr_acptno=? and cr_itemid=?                       \n");
						pstmt = conn.prepareStatement(strQuery.toString());
						pstmt.setString(1, AcptNo);
						pstmt.setString(2, ItemId);
						rs = pstmt.executeQuery();
						if (rs.next()) {
							if (rs.getString("cr_prcdate") != null && !rs.getString("cr_status").equals("3")) {
								Version = Integer.toString(rs.getInt("cr_version"));
							}
						}
						rs.close();
						pstmt.close();
					}
				} 
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

			
			writer = new OutputStreamWriter( new FileOutputStream(shFileName));
			writer.write("cd "+strBinPath +"\n");
			//ecamsLogger.debug("#########      ItemId : " + ItemId + "  Version : " + Version + "  AcptNo : " + AcptNo);
			if (Version.equals("D")) {
//				writer.write("ecams_gensrc RSA " + ItemId + " " + ItemId +"_gensrc.file" + " " + Version + "\n");
				writer.write("./ecams_batexec " +rtString + " \"cd " + strBinPath+ ";" +"./ecams_gensrc RSA " + ItemId + " " + ItemId +"_gensrc.file" + " " + Version + "\" \n");
			} else if (Version.equals("DEV")) {
//				writer.write("ecams_gensrc DEVSVR " + ItemId + " " + ItemId +"_gensrc.file" + " " + Version + "\n");
				writer.write("./ecams_batexec " +rtString + " \"cd " + strBinPath+ ";" +"./ecams_gensrc DEVSVR " + ItemId + " " + ItemId +"_gensrc.file" + " " + Version + "\" \n");
			} else if (!Version.equals("")){
//				writer.write("ecams_gensrc CMR0025 " + ItemId + " " + ItemId +"_gensrc.file" + " " + Version + "\n");
				writer.write("./ecams_batexec " +rtString + " \"cd " + strBinPath+ ";" +"./ecams_gensrc CMR0025 " + ItemId + " " + ItemId +"_gensrc.file" + " " + Version + "\" \n");
			}
			else{
//				writer.write("ecams_gensrc CMR0027 " + ItemId + " " + ItemId +"_gensrc.file" + " " + AcptNo + "\n");
				writer.write("./ecams_batexec " +rtString + " \"cd " + strBinPath+ ";" +"./ecams_gensrc CMR0027 " + ItemId + " " + ItemId +"_gensrc.file" + " " + AcptNo + "\" \n");
			}			
			
			writer.write("exit $?\n");
			writer.close();
						
			strAry = new String[3];
			strAry[0] = "chmod";
			strAry[1] = "777";
			strAry[2] = shFileName;			
			
			run = Runtime.getRuntime();

			//ecamsLogger.error("====chmod==== 1111 "+"chmod "+"777 "+shFileName+" \n");
			
			p = run.exec(strAry);
			p.waitFor();
			
			//ecamsLogger.debug("====chmod return===="+Integer.toString(p.exitValue())+" \n");
			
			run = Runtime.getRuntime();
			
			strAry = new String[2];
			
			strAry[0] = "/bin/sh";
			strAry[1] = shFileName;
			
			p = run.exec(strAry);
			p.waitFor(); 
			
			//ecamsLogger.error("====return==== 22222 "+Integer.toString(p.exitValue())+" \n");
			if (p.exitValue() != 0) {
				//shfile.delete();
				throw new Exception("해당 소스 생성  실패. run=["+shFileName +"]" + " return=[" + p.exitValue() + "]" );
			}
			else{
				shfile.delete();
			}			
			
			//fileStream = new ByteArrayOutputStream();
			//shfile = new File(fileName);
			
			
			//산출물일경우 tmp 파일 생성후 종료
			if (ynDocFile){
				writer = null;
				shfile = null;
				
				rst = new HashMap<String, String>();
				rst.put("line", "1");
				rst.put("src", fileName);
				rst.put("error", "N");
				rtList.add(rst);
				rst = null;
				
				//return fileName;
				return rtList.toArray();
			}
			
			//8859_1, MS949
			
			in1 = new BufferedReader(new InputStreamReader(new FileInputStream(fileName),"MS949"));

			//in1 = new BufferedReader(new InputStreamReader(new FileInputStream(fileName)));
			
			strQuery.setLength(0);
			int i = 0;
			int linecnt = 0;
			String strLine = "";
			/* 2022.08.31 html전환
			while( (readline1 = in1.readLine()) != null ){
				if(gubun==true){
					strLine = String.format("%5d", ++i);
				}else{
					strLine = "";
				}
				strQuery.append(strLine+" " + readline1+"\n");
			}
			*/
			while ((readline1 = in1.readLine()) != null) {
				rst = new HashMap<String, String>();
				strLine = String.format("%5d", ++linecnt);
				rst.put("line", strLine);
				rst.put("src", readline1+"\n");
				rst.put("error", "N");
				rtList.add(rst);
				rst = null;
			}
			in1.close();
			in1 = null;
			
			writer = null;
			shfile = null;
			//fis.close();
			
			//shfile = new File(fileName);
			//shfile.delete();
			
			//return strQuery.toString();
			conn.close();
			conn = null;
			return rtList.toArray();
			
		} catch (SQLException sqlexception) {
			sqlexception.printStackTrace();
			ecamsLogger.error("## Cmr5300.getFileText() SQLException START ##");
			ecamsLogger.error("## Error DESC : ", sqlexception);	
			ecamsLogger.error("## Cmr5300.getFileText() SQLException END ##");			
			throw sqlexception;
		} catch (IOException exception) {
	        exception.printStackTrace();
	        ecamsLogger.error("## Error IOException : ", exception);
	        if ((exception instanceof java.nio.charset.MalformedInputException)){
				in1.close();
				in1 = null;
				//return strQuery.toString();
				return rtList.toArray();
	   		}else{
	   			throw exception;
			}
	    } catch (Exception exception) {
			exception.printStackTrace();
			ecamsLogger.error("## Cmr5300.getFileText() Exception START ##");
			ecamsLogger.error("## Error DESC : ", exception);
			ecamsLogger.error("## Cmr5300.getFileText() Exception END ##");
			throw exception;
		}finally{
			if (in1 != null) in1 = null;
			if (strQuery != null) 	strQuery = null;
			if (rs != null)     try{rs.close();}catch (Exception ex){ex.printStackTrace();}
			if (pstmt != null)  try{pstmt.close();}catch (Exception ex2){ex2.printStackTrace();}
			if (conn != null){
				try{
					ConnectionResource.release(conn);
				}catch(Exception ex3){
					ecamsLogger.error("## Cmr5300.getFileText() connection release exception ##");
					ex3.printStackTrace();
				}
			}
		}
	}
	
	/**
	 * <PRE>
	 * 1. MethodName	: getFileVer
	 * 2. ClassName		: Cmr5300
	 * 3. Commnet			: 소스의 버전별 기본정보 리스트 출력
	 * 4. 작성자				: no name
	 * 5. 작성일				: 2010. 12. 22. 오전 9:58:59
	 * </PRE>
	 * 		@return Object[]
	 * 		@param ItemID
	 * 		@return
	 * 		@throws SQLException
	 * 		@throws Exception
	 */
	public Object[] getFileVer(String ItemID,String viewGbn) throws SQLException, Exception {
		Connection        conn        = null;
		PreparedStatement pstmt       = null;
		PreparedStatement pstmt2       = null;
		ResultSet         rs          = null;
		ResultSet         rs2          = null;
		StringBuffer      strQuery    = new StringBuffer();
		Object[]		  rtObj		  = null;
		ArrayList<HashMap<String, String>>		  rtList	  = new ArrayList<HashMap<String, String>>();
		HashMap<String, String>			  rst		  = null;
		ConnectionContext connectionContext = new ConnectionResource();

		try {
			conn = connectionContext.getConnection();

			strQuery.setLength(0);
			strQuery.append("select to_char(a.cr_opendate,'yyyy/mm/dd hh24:mi') as opendt, \n");
			strQuery.append("       a.cr_lstver,a.cr_status,a.cr_rsrcname,b.cm_info,       \n");
			strQuery.append("       d.cm_dirpath,g.cm_sysmsg,                              \n");
			strQuery.append("       to_char(sysdate,'yyyy/mm/dd hh24:mi') as nowdt         \n");
			strQuery.append("from cmm0036 b,cmr0020 a ,cmm0070 d,cmm0030 g                 \n");
			strQuery.append("where a.cr_itemid= ?                                          \n");
			strQuery.append("and a.cr_syscd=b.cm_syscd and a.cr_rsrccd=b.cm_rsrccd         \n"); 
			strQuery.append("and a.cr_syscd=d.cm_syscd and a.cr_dsncd=d.cm_dsncd           \n");
			strQuery.append("and a.cr_syscd=g.cm_syscd                                     \n");
            
			pstmt = conn.prepareStatement(strQuery.toString());	
			//pstmt =  new LoggableStatement(conn, strQuery.toString());
			
            pstmt.setString(1, ItemID);
			
            ////ecamsLogger.error(((LoggableStatement)pstmt).getQueryString());	
            rs = pstmt.executeQuery();
            
            rtList.clear();
            
			if (rs.next()){
				if (rs.getString("cm_info").substring(11,12).equals("0")){
					throw new Exception("버전관리를 하지 않는 파일입니다. 소스View를 하실수 없습니다.");
				}
				
				if (rs.getString("cr_lstver").equals("0") && rs.getString("cr_status").equals("3") && rs.getString("cm_info").substring(44,45).equals("1")) {
					throw new Exception("현재 개발 중인 소스 입니다. 체크인 이후에 소스View를 하실수 있습니다.");
				}
				
				if (rs.getString("cr_status").equals("7") || rs.getString("cr_status").equals("A") || rs.getString("cr_status").equals("B")){
					strQuery.setLength(0);
					strQuery.append("select b.cr_acptno,b.cr_sayu,b.cr_acptdate,c.cm_info, 				\n");
					strQuery.append("       (select cm_username from cmm0040 where b.cr_editor = cm_userid ) username	\n");
					strQuery.append("  from cmr1000 b,cmm0036 c,cmr1010 d 								\n");
					strQuery.append(" where d.cr_itemid= ? and b.cr_status<>'3' and d.cr_status = '0' 	\n");
					strQuery.append("   and d.cr_acptno=b.cr_acptno 									\n");
					strQuery.append("   and d.cr_syscd=c.cm_syscd   									\n");
					strQuery.append("   and d.cr_rsrccd=c.cm_rsrccd 									\n");
					strQuery.append(" order by b.cr_acptdate desc   									\n");
					
					pstmt2 = conn.prepareStatement(strQuery.toString());
					//pstmt2 =  new LoggableStatement(conn, strQuery.toString());
					pstmt2.setString(1, ItemID);
					////ecamsLogger.error(((LoggableStatement)pstmt2).getQueryString());
					rs2 = pstmt2.executeQuery();
					
					if (rs2.next()){
						rst = new HashMap<String,String>();
						rst.put("cr_itemid", ItemID);
						rst.put("cr_rsrcname", rs.getString("cr_rsrcname"));
						rst.put("cm_dirpath", rs.getString("cm_dirpath"));
						rst.put("cm_sysmsg", rs.getString("cm_sysmsg"));
						rst.put("filedt",rs.getString("opendt"));
						rst.put("cr_acptno",rs2.getString("cr_acptno"));
						if (rs2.getString("cr_sayu") != null) rst.put("cr_sayu", rs2.getString("cr_sayu"));
						rst.put("cr_ver", "");
						rst.put("viewver", "체크인중");
						rst.put("cm_info", rs.getString("cm_info"));
						rst.put("labelmsg", "체크인 중:" + rs.getString("nowdt")+" / 신청번호:"+rs2.getString("cr_acptno"));
						rst.put("username", rs2.getString("username"));
						rtList.add(rst);
						rst = null;
					}
					
					rs2.close();
					pstmt2.close();
					rs2 = null;
					pstmt2 = null;
				}
				if (viewGbn.equals("D")) {
					if (rs.getString("cm_info").substring(57,58).equals("1")) {
						rst = new HashMap<String,String>();
						rst.put("cr_itemid", ItemID);
						rst.put("cr_rsrcname", rs.getString("cr_rsrcname"));
						rst.put("cm_dirpath", rs.getString("cm_dirpath"));
						rst.put("cm_sysmsg", rs.getString("cm_sysmsg"));
						rst.put("filedt",rs.getString("opendt"));
						rst.put("cr_acptno","");
						rst.put("viewver", "RSA소스확인");
						rst.put("cr_ver", "D");
						rst.put("cm_info", rs.getString("cm_info"));
						rst.put("labelmsg", "RSA소스확인");
						rtList.add(rst);
						rst = null;
					}
				} else if (!rs.getString("cm_info").substring(44,45).equals("1") && !rs.getString("cm_info").substring(57,58).equals("1")) {
					rst = new HashMap<String,String>();
					rst.put("cr_itemid", ItemID);
					rst.put("cr_rsrcname", rs.getString("cr_rsrcname"));
					rst.put("cm_dirpath", rs.getString("cm_dirpath"));
					rst.put("cm_sysmsg", rs.getString("cm_sysmsg"));
					rst.put("filedt",rs.getString("opendt"));
					rst.put("cr_acptno","");
					rst.put("viewver", "개발서버소스확인");
					rst.put("cr_ver", "DEV");
					rst.put("cm_info", rs.getString("cm_info"));
					rst.put("labelmsg", "개발서버소스확인");
					rtList.add(rst);
					rst = null;
				}
			}
			else{
				throw new Exception("해당 자원의 정보가 없습니다1.");
			}
			
			rs.close();
			pstmt.close();
			
			strQuery.setLength(0);
			strQuery.append("SELECT b.cr_acptno,b.cr_ver,                                 \n");
			strQuery.append("       to_char(h.cr_acptdate,'yyyy/mm/dd hh24:mi') as opendt,\n");
			strQuery.append("       a.cr_rsrcname,d.cm_dirpath,                           \n");
			strQuery.append("       g.cm_sysmsg,h.cr_qrycd, 														\n");
			strQuery.append("       (select cr_sayu from cmr1000 where b.cr_acptno = cr_acptno ) cr_sayu,e.cm_info,	\n");
			strQuery.append("       (select cm_username from cmm0040 where b.cr_editor = cm_userid ) username       \n");			
			strQuery.append("  FROM cmr0021 b,cmr0020 a,cmm0070 d,cmm0030 g,cmr0021 h,cmm0036 e 					\n");
			strQuery.append(" where a.cr_itemid= ?                                        \n");
			strQuery.append("   and a.cr_itemid=b.cr_itemid                               \n"); 
			strQuery.append("   and b.cr_itemid=h.cr_itemid and b.cr_acptno=h.cr_acptno   \n"); 
			strQuery.append("   and h.cr_qrycd not in ('03','05')                         \n");
			strQuery.append("   and a.cr_syscd=d.cm_syscd and a.cr_dsncd=d.cm_dsncd       \n");
			strQuery.append("   and a.cr_syscd=g.cm_syscd 							      \n");
			strQuery.append("   and g.cm_syscd=e.cm_syscd and a.cr_rsrccd=e.cm_rsrccd     \n");
			strQuery.append("union                                                        \n");
			strQuery.append("SELECT b.cr_acptno,b.cr_ver,                                 \n");
			strQuery.append("       to_char(h.cr_acptdate,'yyyy/mm/dd hh24:mi') as opendt,\n");
			strQuery.append("       a.cr_rsrcname,d.cm_dirpath,g.cm_sysmsg,h.cr_qrycd,    \n");
			strQuery.append("       decode(h.cr_qrycd,'03','최초이행','추가이행') cr_sayu,c.cm_info, 					\n");
			strQuery.append("       (select cm_username from cmm0040 where b.cr_editor = cm_userid ) username       \n");
			strQuery.append("  FROM cmr0021 b,cmr0020 a,cmm0070 d,cmm0030 g,cmr0021 h,cmm0036 c 					\n");
			strQuery.append(" where a.cr_itemid= ?                                         \n");
			strQuery.append("   and a.cr_itemid=b.cr_itemid                                \n"); 
			strQuery.append("   and b.cr_itemid=h.cr_itemid and b.cr_acptno=h.cr_acptno    \n"); 
			strQuery.append("   and h.cr_qrycd in ('03','05')                              \n");
			strQuery.append("   and a.cr_syscd=d.cm_syscd and a.cr_dsncd=d.cm_dsncd        \n");
			strQuery.append("   and a.cr_syscd=g.cm_syscd                                  \n");
			strQuery.append("   and g.cm_syscd=c.cm_syscd and a.cr_rsrccd=c.cm_rsrccd      \n");
			strQuery.append(" order by opendt desc                                         \n");
			
            pstmt = conn.prepareStatement(strQuery.toString());	
			//pstmt =  new LoggableStatement(conn, strQuery.toString());
			
            pstmt.setString(1, ItemID);
            pstmt.setString(2, ItemID);
			
            ////ecamsLogger.error(((LoggableStatement)pstmt).getQueryString());	
            rs = pstmt.executeQuery();
			
            while(rs.next()){
				rst = new HashMap<String,String>();
				rst.put("cr_itemid", ItemID);
				rst.put("cr_rsrcname", rs.getString("cr_rsrcname"));
				rst.put("cm_dirpath", rs.getString("cm_dirpath"));	
				rst.put("cm_sysmsg", rs.getString("cm_sysmsg"));
				rst.put("cr_acptno",rs.getString("cr_acptno"));
				rst.put("viewver", rs.getString("cr_ver"));
				rst.put("cr_ver", rs.getString("cr_ver"));
				rst.put("cm_info", rs.getString("cm_info"));
				rst.put("username", rs.getString("username"));
				
				if (rs.getString("cr_qrycd").equals("03")){
					rst.put("filedt",rs.getString("opendt"));
					rst.put("labelmsg", "최초이행:" +rs.getString("opendt")+" / 버전:"+rs.getString("cr_ver"));
				} else if (rs.getString("cr_qrycd").equals("05")) {
					rst.put("filedt",rs.getString("opendt"));
					rst.put("labelmsg", "추가이행:" +rs.getString("opendt")+" / 버전:"+rs.getString("cr_ver"));					
				}
				else{
					rst.put("filedt",rs.getString("opendt"));
					rst.put("labelmsg", "체크인:" +rs.getString("opendt")+" / 버전:"+rs.getString("cr_ver"));
				}
				if (rs.getString("cr_sayu") != null) rst.put("cr_sayu",rs.getString("cr_sayu"));
				else rst.put("cr_sayu", "");
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
			ecamsLogger.error("## Cmr5300.getFileList() SQLException START ##");
			ecamsLogger.error("## Error DESC : ", sqlexception);	
			ecamsLogger.error("## Cmr5300.getFileList() SQLException END ##");			
			throw sqlexception;
		} catch (Exception exception) {
			exception.printStackTrace();
			ecamsLogger.error("## Cmr5300.getFileList() Exception START ##");				
			ecamsLogger.error("## Error DESC : ", exception);	
			ecamsLogger.error("## Cmr5300.getFileList() Exception END ##");				
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
					ecamsLogger.error("## Cmr5300.getFileList() connection release exception ##");
					ex3.printStackTrace();
				}
			}
		}
	}

	public Object[] getReqList(String AcptNo,String UserId) throws SQLException, Exception {
		Connection        conn        = null;
		PreparedStatement pstmt       = null;
		ResultSet         rs          = null;
		PreparedStatement pstmt2      = null;
		ResultSet         rs2         = null;
		StringBuffer      strQuery    = new StringBuffer();
		ArrayList<HashMap<String, String>>		  rtList	  = new ArrayList<HashMap<String, String>>();
		HashMap<String, String>			  rst		  = null;
		ConnectionContext connectionContext = new ConnectionResource();
		boolean           findSw = false;
		String            strAcpt = AcptNo;
		try {
			conn = connectionContext.getConnection();
			strQuery.setLength(0);
			
			strQuery.append("select a.cr_rsrcname,a.cr_version,a.cr_itemid,a.cr_befver,\n");
			strQuery.append("       a.cr_qrycd,a.cr_status,b.cm_dirpath,c.cm_info,    \n");
			strQuery.append("       d.cr_rsrcname basename,f.cm_codename,a.cr_confno, \n");
			strQuery.append("       a.cr_prcdate,e.cr_acptno                          \n");
			strQuery.append("  from cmm0020 f,cmm0036 c,cmm0070 b,cmr1010 a,cmr1010 d,cmr1000 e \n");
			if (AcptNo.length() == 12) {
				strQuery.append(" where e.cr_acptno=?                                 \n");
			} else {
				strQuery.append(" where e.cr_qrycd='01' and e.cr_editor=?             \n");
				strQuery.append("   and e.cr_status='9' and e.cr_isrid=?              \n");
				strQuery.append("   and e.cr_isrsub=?                                 \n");
			}
			strQuery.append("   and e.cr_acptno=a.cr_acptno                           \n");
			strQuery.append("   and a.cr_status<>'3'                                  \n");
			strQuery.append("   and substr(nvl(a.cr_confno,a.cr_acptno),5,2) in ('01','03','04','16') \n");
			strQuery.append("   and a.cr_syscd=b.cm_syscd and a.cr_dsncd=b.cm_dsncd   \n");
			strQuery.append("   and a.cr_syscd=c.cm_syscd and a.cr_rsrccd=c.cm_rsrccd \n");
			strQuery.append("   and substr(c.cm_info,10,1)='0'                        \n");
			strQuery.append("   and substr(c.cm_info,27,1)='0'                        \n");
			strQuery.append("   and substr(c.cm_info,46,1)='0'                        \n");
			strQuery.append("   and substr(c.cm_info,12,1)='1'                        \n");
			strQuery.append("   and a.cr_acptno=d.cr_acptno and a.cr_baseitem=d.cr_itemid\n");
			strQuery.append("   and f.cm_macode='CHECKIN' and f.cm_micode=decode(e.cr_qrycd,'01','04',a.cr_qrycd)  \n");
			strQuery.append(" order by a.cr_baseitem,b.cm_dirpath,a.cr_rsrcname       \n");
            pstmt = conn.prepareStatement(strQuery.toString());	
			//pstmt =  new LoggableStatement(conn, strQuery.toString());	
            if (AcptNo.length() == 12) {
            	pstmt.setString(1, AcptNo); 
            } else {
            	pstmt.setString(1, UserId); 
            	pstmt.setString(2, AcptNo.substring(0,12)); 
            	pstmt.setString(3, AcptNo.substring(13)); 
            }
			//ecamsLogger.error(((LoggableStatement)pstmt).getQueryString());
            rs = pstmt.executeQuery();            
            rtList.clear();
            
			while (rs.next()){
				findSw = true;
				if (rs.getString("cr_acptno").substring(4,6).equals("01")) {
					if (rs.getString("cr_confno") != null) {
						if (!rs.getString("cr_confno").substring(4,6).equals("03") && !rs.getString("cr_confno").substring(4,6).equals("04")) {
							findSw = false;
						}
					} else {
						findSw = true;
					}
				}
				
				if (findSw == true) {
					rst = new HashMap<String,String>();
					rst.put("cr_itemid", rs.getString("cr_itemid"));
					rst.put("cr_rsrcname", rs.getString("cr_rsrcname"));
					rst.put("cm_dirpath", rs.getString("cm_dirpath"));
					rst.put("cr_befver", rs.getString("cr_befver"));
					rst.put("cr_ver", rs.getString("cr_version"));
					rst.put("cr_aftver", rs.getString("cr_version"));
					rst.put("cm_info", rs.getString("cm_info"));
					rst.put("basename", rs.getString("basename"));
					rst.put("cr_status", rs.getString("cr_status"));
					if (rs.getString("cr_acptno").substring(4,6).equals("01")) {
						rst.put("cr_qrycd", "04");	
						rst.put("cr_befver", rs.getString("cr_version"));
					} else {
						rst.put("cr_qrycd", rs.getString("cr_qrycd"));
						rst.put("cr_status", rs.getString("cr_status"));
					}
					rst.put("cm_codename", rs.getString("cm_codename"));
					findSw = false;
					if (rs.getString("cr_acptno").substring(4,6).equals("01")) {
						strQuery.setLength(0);
						strQuery.append("select cr_acptno from cmr1010  \n");
						strQuery.append(" where cr_baseno=?             \n");
						strQuery.append("   and cr_acptno<>cr_baseno    \n");
						strQuery.append("   and substr(nvl(cr_confno,cr_acptno),5,2) in ('03','04') \n");
						strQuery.append("   and cr_itemid=?             \n");
						strQuery.append("   and cr_status<>'3'          \n");
						pstmt2 = conn.prepareStatement(strQuery.toString());
						//pstmt2 =  new LoggableStatement(conn, strQuery.toString());	
						pstmt2.setString(1, rs.getString("cr_acptno"));
						pstmt2.setString(2, rs.getString("cr_itemid"));
						//ecamsLogger.error(((LoggableStatement)pstmt2).getQueryString());
						rs2 = pstmt2.executeQuery();
						if (rs2.next()) {
							findSw = true;
							strAcpt = rs2.getString("cr_acptno");
						} else {
							rst.put("cr_ver", "T");
							rst.put("cr_aftver", "개발영역");
						}
						rs2.close();
						pstmt2.close();
					} else if (AcptNo.substring(4,6).equals("03")) {
						findSw = true;
						strAcpt = AcptNo;
					} else if (rs.getString("cr_status").equals("0") && AcptNo.substring(4,6).equals("04")) {
						findSw = true;
						strAcpt = AcptNo;
					}
					if (findSw == true) {
						strQuery.setLength(0);
						strQuery.append("select max(cr_acptno) acptno from cmr0027  \n");
						strQuery.append(" where cr_acptno in (select cr_acptno from cmr1010 \n");
						strQuery.append("                      where cr_itemid=?            \n");
						strQuery.append("                        and cr_status<>'3'         \n");
						strQuery.append("                        and (cr_acptno=?           \n");
						strQuery.append("                         or nvl(cr_confno,cr_acptno)=?)) \n");
						strQuery.append("   and cr_itemid=?                \n");
						pstmt2 = conn.prepareStatement(strQuery.toString());
						//pstmt2 =  new LoggableStatement(conn, strQuery.toString());	
						pstmt2.setString(1, rs.getString("cr_itemid"));
						pstmt2.setString(2, strAcpt);
						pstmt2.setString(3, strAcpt);
						pstmt2.setString(4, rs.getString("cr_itemid"));
						//ecamsLogger.error(((LoggableStatement)pstmt2).getQueryString());
						rs2 = pstmt2.executeQuery();
						if (rs2.next()) {
							if (rs2.getString("acptno") != null) strAcpt = rs2.getString("acptno");
						} else {
							strAcpt = "";
						}
						rs2.close();
						pstmt2.close();
						
						if (strAcpt == "" || strAcpt == null) {
							rst.put("cr_ver", "T");
							rst.put("cr_aftver", "개발영역");							
						} else {
							if (strAcpt.substring(4,6).equals("03")) rst.put("cr_aftver", "테스트체크인");
							else rst.put("cr_aftver", "운영체크인");
							rst.put("cr_ver", strAcpt);
							if (rs.getString("cr_acptno").substring(4,6).equals("01")) rst.put("cr_status", "0");
							rst.put("acptno", strAcpt);
						}
					} 
					
					rtList.add(rst);
					rst = null;
				}
			}
			rs.close();
			pstmt.close();
			conn.close();
			conn = null;
			pstmt = null;
			rs = null;
			return rtList.toArray();			
			//end of while-loop statement
			
		} catch (SQLException sqlexception) {
			sqlexception.printStackTrace();
			ecamsLogger.error("## Cmr5300.getReqList() SQLException START ##");
			ecamsLogger.error("## Error DESC : ", sqlexception);	
			ecamsLogger.error("## Cmr5300.getReqList() SQLException END ##");			
			throw sqlexception;
		} catch (Exception exception) {
			exception.printStackTrace();
			ecamsLogger.error("## Cmr5300.getReqList() Exception START ##");				
			ecamsLogger.error("## Error DESC : ", exception);	
			ecamsLogger.error("## Cmr5300.getReqList() Exception END ##");				
			throw exception;
		}finally{
			if (strQuery != null) 	strQuery = null;
			if (rtList != null)	rtList = null;
			if (rs != null)     try{rs.close();}catch (Exception ex){ex.printStackTrace();}
			if (pstmt != null)  try{pstmt.close();}catch (Exception ex2){ex2.printStackTrace();}
			if (rs2 != null)    try{rs2.close();}catch (Exception ex){ex.printStackTrace();}
			if (pstmt2 != null) try{pstmt2.close();}catch (Exception ex2){ex2.printStackTrace();}
			if (conn != null){
				try{
					ConnectionResource.release(conn);
				}catch(Exception ex3){
					ecamsLogger.error("## Cmr5300.getReqList() connection release exception ##");
					ex3.printStackTrace();
				}
			}
		}

	}	
}
