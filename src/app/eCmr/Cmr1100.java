package app.eCmr;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import org.apache.logging.log4j.Logger;

import app.common.LoggableStatement;
import app.common.UserInfo;

import com.ecams.common.dbconn.ConnectionContext;
import com.ecams.common.dbconn.ConnectionResource;
import com.ecams.common.logger.EcamsLogger;


public class Cmr1100 {
	Logger ecamsLogger  = EcamsLogger.getLoggerInstance();

	public Object[] getFileList_detail(HashMap<String,String> etcData) throws SQLException, Exception {
		Connection        conn        = null;
		PreparedStatement pstmt       = null;
		PreparedStatement pstmt2       = null;
		ResultSet         rs          = null;
		ResultSet         rs2          = null;
		StringBuffer      strQuery    = new StringBuffer();
		StringBuffer      strQuery2    = new StringBuffer();
		Object[]		  rtObj		  = null;
		ArrayList<HashMap<String, String>>		  rtList	  = new ArrayList<HashMap<String, String>>();
		HashMap<String, String>			  rst		  = null;
		ConnectionContext connectionContext = new ConnectionResource();
		int				  pstmtcount  = 1;
		Boolean			  AdminChk	  = false;
		UserInfo		  userInfo	  = new UserInfo();
		int				 subrowcnt    = 0;
		String			 sin		  = null;

		try {
			conn = connectionContext.getConnection();

			AdminChk = userInfo.isAdmin(etcData.get("UserID"));
			userInfo = null;
			
			pstmtcount=1;
			strQuery.setLength(0);
			strQuery.append("select a.cr_acptno,a.cr_sysgb,to_char(a.cr_acptdate,'yyyy/mm/dd') as acptdate, \n");
			strQuery.append("       a.cr_syscd,a.cr_editor,a.cr_qrycd qry,a.cr_passok,   \n");
			strQuery.append("       (select cm_codename from cmm0020 where cm_macode ='REQPASS' and cm_micode = a.cr_passok) passok,    \n");			
			strQuery.append("       b.cm_codename,d.cm_SYSMSG sysgb,e.cm_jobname,f.cm_codename sin,\n");
			strQuery.append("       c.cr_serno,c.cr_qrycd,a.cr_prcreq,a.cr_sayu,                   \n");
			strQuery.append("       c.cr_rsrcname,c.cr_dsncd,c.cr_confno,c.cr_baseno,              \n"); 
			strQuery.append("       to_char(c.cr_prcdate,'yyyy/mm/dd') as prcdate,         \n"); 
			strQuery.append("       c.cr_status,c.cr_editcon,g.cm_dirpath                          \n"); 
			strQuery.append("from cmm0020 f,cmm0030 d,cmm0102 e, cmm0020 b, cmr1000 a,cmr1010 c,cmm0070 g, \n");
			strQuery.append("(select distinct nvl(b.cr_baseno,b.cr_acptno) cr_baseno,b.cr_syscd,b.cr_dsncd,b.cr_rsrcname \n");
			strQuery.append("from cmr1000 a,cmr1010 b \n");
			strQuery.append("where a.cr_qrycd<'50' \n");

			if (AdminChk == false){
				strQuery.append("and a.cr_editor= ? \n");
			}
			if (etcData.get("req").equals("1")){
				strQuery.append("and a.cr_qrycd in ('01','02','11') \n");
			}
			else{
				strQuery.append("and a.cr_qrycd in ('03','04','06','12') \n");
			}

		    if (!etcData.get("reqcd").equals("00")){
		    	strQuery.append("and a.cr_qrycd= ? \n");
		    }
		    
			if (etcData.get("stepcd").equals("0")){
				strQuery.append("and ((a.cr_prcdate is not null \n");
				strQuery.append("and to_char(a.cr_acptdate,'yyyymmdd')>= replace(? ,'/','') \n");
				strQuery.append("and to_char(a.cr_acptdate,'yyyymmdd')<= replace(? ,'/','') ) or \n");
				strQuery.append("a.cr_status='0') \n");				
			}
			else if (etcData.get("stepcd").equals("1")){
				strQuery.append("and a.cr_status='0' \n");
			}
			else if (etcData.get("stepcd").equals("9")){
				strQuery.append("and a.cr_prcdate is not null \n");
				strQuery.append("and to_char(a.cr_acptdate,'yyyymmdd')>= replace(? ,'/','') \n");
				strQuery.append("and to_char(a.cr_acptdate,'yyyymmdd')<= replace(? ,'/','') \n");
			}
			strQuery.append("and a.cr_acptno=b.cr_acptno \n");
			strQuery.append("and b.cr_itemid is not null \n");
			strQuery.append("and b.cr_itemid=b.cr_baseitem \n");
			if (etcData.get("prgname") != null && !etcData.get("prgname").equals("")){
				if (etcData.get("prgname").length() > 0){
					strQuery.append("and upper(b.cr_rsrcname) like upper(?) \n");
				}
			}
			strQuery.append(") h \n");
			strQuery.append("where nvl(c.cr_baseno,c.cr_acptno)=h.cr_baseno and c.cr_syscd=h.cr_syscd \n");
			strQuery.append("and c.cr_dsncd=h.cr_dsncd and c.cr_rsrcname=h.cr_rsrcname \n");
			strQuery.append("and c.cr_acptno=a.cr_acptno \n");
			strQuery.append("and c.cr_syscd=g.cm_syscd and c.cr_dsncd=g.cm_dsncd \n");
			strQuery.append("and b.cm_macode = 'CMR1000' and a.cr_status = b.cm_micode \n");
			strQuery.append("and a.cr_sysCD = d.cm_SYSCD \n");
			strQuery.append("and a.cr_jobcd = e.cm_jobcd \n");
			if (etcData.get("emgSw").equals("2")) {
				strQuery.append("and a.cr_passok='2'                                             \n");
			}
			strQuery.append("and f.cm_macode = 'REQUEST' and a.cr_qrycd = f.cm_micode \n");
			if (etcData.get("qrycd").equals("A")) {
				strQuery.append("order by a.cr_acptno,c.cr_serno           \n");
			} else {
			   strQuery.append("order by c.cr_rsrcname,a.cr_acptdate       \n");
			}
	        pstmt = conn.prepareStatement(strQuery.toString());	
			pstmt =  new LoggableStatement(conn, strQuery.toString());
			
            

			if (AdminChk == false){
				pstmt.setString(pstmtcount++, etcData.get("UserID"));
			}
			if (!etcData.get("reqcd").equals("00")){
				pstmt.setString(pstmtcount++, etcData.get("reqcd"));
			}
		
			if (etcData.get("stepcd").equals("0") || etcData.get("stepcd").equals("9") ){
				pstmt.setString(pstmtcount++, etcData.get("stDt"));
				pstmt.setString(pstmtcount++, etcData.get("edDt")); 
			}
			
			if (etcData.get("prgname") != null && !etcData.get("prgname").equals("")){
				if (etcData.get("prgname").length() > 0){
					pstmt.setString(pstmtcount++, "%"+etcData.get("prgname")+"%");
				}
			}
			
			ecamsLogger.error(((LoggableStatement)pstmt).getQueryString());
			
            rs = pstmt.executeQuery();
            
            rtList.clear();
			while (rs.next()){

				rst = new HashMap<String,String>();
				//rst.put("ID", Integer.toString(rs.getRow()));
				rst.put("acptno", rs.getString("cr_acptno").substring(0,4)+"-"+rs.getString("cr_acptno").substring(4,6)+"-"+rs.getString("cr_acptno").substring(6));
				rst.put("acptdate",rs.getString("acptdate"));
				rst.put("sysgbname",rs.getString("SysGb"));
				rst.put("cm_jobname",rs.getString("cm_jobname"));
				rst.put("cm_dirpath",rs.getString("cm_dirpath"));
				rst.put("cr_rsrcname", rs.getString("cr_rsrcname"));
				rst.put("cr_status",rs.getString("cr_status"));	
				if (rs.getString("cr_editcon") != null) rst.put("cr_editcon", rs.getString("cr_editcon"));
				if (rs.getString("cr_sayu") != null) rst.put("cr_sayu", rs.getString("cr_sayu"));
				
				if (rs.getString("qry").equals("03") || rs.getString("qry").equals("04")){
					strQuery2.setLength(0);
					strQuery2.append("select cm_codename from cmm0020 \n");
					strQuery2.append("where cm_macode='CHECKIN' and \n");
					strQuery2.append("cm_micode= ? \n");
					
					
					pstmt2 = conn.prepareStatement(strQuery2.toString());
					
					pstmt2.setString(1,rs.getString("cr_qrycd"));
					
					rs2 = pstmt2.executeQuery();
					
					while (rs2.next()){
						sin = rs2.getString("cm_codename");
					}
					rs2.close();
					pstmt2.close();
					
					rst.put("sin", rs.getString("sin") + "(" + sin + ")");
				}
				else{
					rst.put("sin", rs.getString("sin"));				
				}
				rst.put("cr_acptno", rs.getString("cr_acptno"));
				rst.put("acptno", rs.getString("cr_acptno").substring(0,4)+"-"+rs.getString("cr_acptno").substring(4,6)+"-"+rs.getString("cr_acptno").substring(6));
				/*
				if (rs.getString("cr_prjno") != null && rs.getString("cr_prjno") != "") {
					rst.put("cr_prjno",rs.getString("cr_prjno"));
				}
				*/
				/*
				if (!(rs.getString("cr_prjno").equals("") || rs.getString("cr_prjno") == null)){
					rst.put("cr_prjno_cr_devno",rs.getString("cr_prjno")+"-"+rs.getString("cr_devno"));
				}
				*/				
				rst.put("cm_codename", rs.getString("cm_codename"));
				rst.put("cr_serno",rs.getString("cr_serno"));
				rst.put("cr_dsncd",rs.getString("cr_dsncd"));
				rst.put("cr_qrycd",rs.getString("qry"));
				rst.put("cr_baseno",rs.getString("cr_baseno"));
				rst.put("cr_editor",rs.getString("cr_editor"));
        		rst.put("cr_passok",  rs.getString("cr_passok"));     
        		rst.put("passok",  rs.getString("passok"));  				

				if (rs.getString("prcdate") != null) rst.put("prcdate", rs.getString("prcdate"));
				if (rs.getString("cr_prcreq") != null) 
					rst.put("prcreq", rs.getString("cr_prcreq").substring(0, 4) + "/" + 
							rs.getString("cr_prcreq").substring(4, 6) + "/" +
							rs.getString("cr_prcreq").substring(6, 8) + " " +
							rs.getString("cr_prcreq").substring(8, 10) + "/" +
							rs.getString("cr_prcreq").substring(10, 12));
				
			    strQuery2.setLength(0);
				strQuery2.append("select count(*) as cnt from cmr1011 where \n");
				strQuery2.append("cr_acptno= ? and \n");
				strQuery2.append("cr_serno= ? and \n"); 
				strQuery2.append("cr_rstfile is not null \n");
				
				
				pstmt2 = conn.prepareStatement(strQuery2.toString());
				
				pstmt2.setString(1,rs.getString("cr_acptno"));
				pstmt2.setInt(2, rs.getInt("cr_serno"));
				
				rs2 = pstmt2.executeQuery();
				
				while (rs2.next()){
					subrowcnt = rs2.getInt("cnt");
				}
				
				rs2.close();
				pstmt2.close();
				
				if (subrowcnt > 0){
					rst.put("result","1");
				}
				else{
					rst.put("result","0");
				}
				
				rst.put("cr_syscd",rs.getString("cr_syscd"));
				
				if (rs.getString("prcdate") == null){
					strQuery2.setLength(0);
					strQuery2.append("select count(*) as cnt from cmr1010 where \n");
					strQuery2.append("cr_acptno= ? and \n");
					strQuery2.append("cr_putcode is not null and cr_putcode='WAIT' \n");
					
					
					pstmt2 = conn.prepareStatement(strQuery2.toString());
					
					pstmt2.setString(1,rs.getString("cr_acptno"));
					
					rs2 = pstmt2.executeQuery();
					subrowcnt = 0;
					while (rs2.next()){
						subrowcnt = subrowcnt + rs2.getInt("cnt");
					}
					rs2.close();
					pstmt2.close();
					
					strQuery2.setLength(0);
					strQuery2.append("select count(*) as cnt from cmr1011 where  \n");
					strQuery2.append("cr_acptno= ? and \n");
					strQuery2.append("(cr_prcrst='WAIT' or cr_wait='W') \n");
					
					
					pstmt2 = conn.prepareStatement(strQuery2.toString());
					
					pstmt2.setString(1,rs.getString("cr_acptno"));
					
					rs2 = pstmt2.executeQuery();
					while (rs2.next()){
						subrowcnt = subrowcnt + rs2.getInt("cnt");
					}
					
					rs2.close();
					pstmt2.close();
					
					if (subrowcnt > 0){
						rst.put("errflag","1");
					}
					else{
						rst.put("errflag","0");
					}
				}
				else{
					rst.put("errflag","0");
				}
				rtList.add(rst);
				rst = null;
			}//end of while-loop statement
			
			rs.close();
			pstmt.close();
			conn.close();
			conn = null;
			pstmt = null;
			rs = null;
			rtObj =  rtList.toArray();

			rtList = null;
			
			return rtObj;
			
		} catch (SQLException sqlexception) {
			sqlexception.printStackTrace();
			ecamsLogger.error("## Cmr1100.getFileList_detail() SQLException START ##");
			ecamsLogger.error("## Error DESC : ", sqlexception);	
			ecamsLogger.error("## Cmr1100.getFileList_detail() SQLException END ##");			
			throw sqlexception;
		} catch (Exception exception) {
			exception.printStackTrace();
			ecamsLogger.error("## Cmr1100.getFileList_detail() Exception START ##");				
			ecamsLogger.error("## Error DESC : ", exception);	
			ecamsLogger.error("## Cmr1100.getFileList_detail() Exception END ##");				
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
					ecamsLogger.error("## Cmr1100.getFileList_detail() connection release exception ##");
					ex3.printStackTrace();
				}
			}
		}

	}
	
	public Object[] getFileList(HashMap<String,String> etcData) throws SQLException, Exception {
		Connection        conn        = null;
		PreparedStatement pstmt       = null;
		PreparedStatement pstmt2       = null;
		ResultSet         rs          = null;
		ResultSet         rs2          = null;
		StringBuffer      strQuery    = new StringBuffer();
		StringBuffer      strQuery2    = new StringBuffer();
		Object[]		  rtObj		  = null;
		ArrayList<HashMap<String, String>>		  rtList	  = new ArrayList<HashMap<String, String>>();
		HashMap<String, String>			  rst		  = null;
		ConnectionContext connectionContext = new ConnectionResource();
		int				  pstmtcount  = 1;
		Boolean			  AdminChk	  = false;
		UserInfo		  userInfo	  = new UserInfo();
		int				 subrowcnt    = 0;
		String			 prjDevNo ="";

		try {
			conn = connectionContext.getConnection();
			AdminChk = userInfo.isAdmin(etcData.get("UserID"));			
			userInfo = null;
			
			pstmtcount=1;
			strQuery.setLength(0);
			strQuery.append("select to_char(a.CR_ACPTDATE,'yyyy/mm/dd hh24:mi') as acptdate, a.cr_passok, \n");					
			strQuery.append("a.cr_acptno, a.cr_editor, a.cr_syscd, a.cr_status,a.cr_sayu, \n");					
			strQuery.append("a.cr_sysgb, a.cr_qrycd, to_char(a.cr_prcdate,'yyyy/mm/dd hh24:mi') as prcdate,  \n");					
			strQuery.append("a.cr_prcreq,a.cr_passok,                                               \n");
			strQuery.append("(select cm_codename from cmm0020 where cm_macode ='REQPASS' and cm_micode = a.cr_passok) passok,          \n");			
			strQuery.append("d.cm_SYSMSG sysgb,e.cm_jobname,f.cm_codename sin, g.cnt as subrowcnt, h.cr_rsrcname \n");
			strQuery.append("from cmm0020 f,cmm0030 d,cmm0102 e, cmr1000 a ,            \n");
			strQuery.append("    (select cr_acptno,count(*) as cnt from cmr1010         \n");
			if (AdminChk == false){
				strQuery.append("Where cr_editor= ?                                     \n");
			    strQuery.append("  and cr_baseitem=cr_itemid                            \n");
			} else {
			    strQuery.append("where cr_baseitem=cr_itemid                            \n");
			}
			
			strQuery.append("       group by cr_acptno) g, cmr1010 h                    \n");

			if (etcData.get("req").equals("1")){
				strQuery.append("where a.cr_qrycd in ('01','02','11') \n");
			}
			else if(etcData.get("req").equals("3")){
				strQuery.append("where a.cr_qrycd in ('01','02') \n");
				strQuery.append("and a.cr_status not in ('3') \n");
				strQuery.append("and h.cr_acptno in (select cr_acptno from cmr1010 \n");
				strQuery.append("where cr_confno is null) \n");
			}
			else if(etcData.get("req").equals("4")){
				strQuery.append("where a.cr_qrycd in ('04','06') \n");
			}
			else {
				strQuery.append("where a.cr_qrycd in ('04','06','12') \n");
			}

			if (AdminChk == false){
				strQuery.append("and a.cr_editor= ? \n");
			}
			
		    if (!etcData.get("reqcd").equals("00")){
		    	strQuery.append("and a.cr_qrycd= ? \n");
		    }
			
			if (etcData.get("stepcd").equals("0")){
				strQuery.append("and ((a.cr_prcdate is not null \n");
				strQuery.append("and to_char(a.cr_acptdate,'yyyymmdd')>= replace(? ,'/','') \n");
				strQuery.append("and to_char(a.cr_acptdate,'yyyymmdd')<= replace(? ,'/','')) or \n");
				strQuery.append("a.cr_status='0') \n");				
			}
			else if (etcData.get("stepcd").equals("1")){
				strQuery.append("and a.cr_status='0' \n");
			}
			else if (etcData.get("stepcd").equals("9")){
				strQuery.append("and a.cr_prcdate is not null \n");
				strQuery.append("and to_char(a.cr_acptdate,'yyyymmdd')>= replace(? ,'/','') \n");
				strQuery.append("and to_char(a.cr_acptdate,'yyyymmdd')<= replace(? ,'/','') \n");
			}
			if (etcData.get("emgSw").equals("2")){
				strQuery.append("and a.cr_passok='2'                                        \n");
			}
			strQuery.append("and a.cr_acptno = g.cr_acptno \n");
			strQuery.append("and g.cr_acptno = h.cr_acptno \n"); 
			strQuery.append("and h.cR_serno = (select cr_serno from cmr1010 where cr_acptno=h.cr_acptno and rownum=1 ) \n");
			strQuery.append("and a.cr_sysCD = d.cm_SYSCD \n");
			strQuery.append("and a.cr_jobcd = e.cm_jobcd \n");
			strQuery.append("and f.cm_macode = 'REQUEST' and a.cr_qrycd = f.cm_micode \n");
			strQuery.append("and a.cr_editor = ? \n");
            pstmt = conn.prepareStatement(strQuery.toString());	
			//pstmt =  new LoggableStatement(conn, strQuery.toString());

			if (AdminChk == false){
				pstmt.setString(pstmtcount++, etcData.get("UserID"));
				pstmt.setString(pstmtcount++, etcData.get("UserID"));
			}
			
			if (!etcData.get("reqcd").equals("00")){
				pstmt.setString(pstmtcount++, etcData.get("reqcd"));
				
			}

			if (etcData.get("stepcd").equals("0") || etcData.get("stepcd").equals("9") ){
				pstmt.setString(pstmtcount++, etcData.get("stDt"));
				pstmt.setString(pstmtcount++, etcData.get("edDt"));
			}
			if ( etcData.get("UserID") != null && !"".equals(etcData.get("UserID"))){
				pstmt.setString(pstmtcount++, etcData.get("UserID"));
			}

			//ecamsLogger.error(((LoggableStatement)pstmt).getQueryString());
            rs = pstmt.executeQuery();
            
            rtList.clear();
			while (rs.next()){
				prjDevNo="";
				
				rst = new HashMap<String,String>();
				//rst.put("ID", Integer.toString(rs.getRow()));
				rst.put("req", etcData.get("req"));
				
				rst.put("sysgbname",rs.getString("SysGb"));
				rst.put("cm_jobname",rs.getString("cm_jobname"));
				rst.put("acptdate",rs.getString("acptdate"));
				if (rs.getString("cr_sayu") != null) rst.put("cr_sayu", rs.getString("cr_sayu"));
				/* 20190919
				if (rs.getString("cr_passok").equals("2")){
					rst.put("sin","[긴급]" + rs.getString("sin"));
				}
				else{
					rst.put("sin",rs.getString("sin"));
				}
				*/
				rst.put("sin",rs.getString("sin"));
				
				rst.put("acptno", rs.getString("cr_acptno").substring(0,4)+"-"+rs.getString("cr_acptno").substring(4,6)+"-"+rs.getString("cr_acptno").substring(6));
				rst.put("cr_acptno", rs.getString("cr_acptno"));
				/*
				if (rs.getString("cr_prjno") != null && rs.getString("cr_prjno") != "") {
					prjDevNo=rs.getString("cr_prjno");
					rst.put("cr_prjno",prjDevNo);
				}
				*/
				/*
				if (!(rs.getString("cr_prjno").equals("") || rs.getString("cr_prjno") == null)){
					rst.put("cr_prjno_cr_devno",rs.getString("cr_prjno")+"-"+rs.getString("cr_devno"));
				}
				*/

        		rst.put("cr_passok",  rs.getString("cr_passok"));     
        		rst.put("passok",  rs.getString("passok"));  
        		
				if (rs.getString("cr_status").equals("3")) 
					rst.put("cm_codename", "반려");
				else if (rs.getString("cr_status").equals("9")) 
					rst.put("cm_codename", "처리완료");
				else {					
					strQuery2.setLength(0);
					strQuery2.append("select cr_teamcd,cr_confname from cmr9900  \n");
					strQuery2.append(" where cr_acptno= ? and cr_locat='00'      \n");
					pstmt2 = conn.prepareStatement(strQuery2.toString());					
					pstmt2.setString(1,rs.getString("cr_acptno"));
					rs2 = pstmt2.executeQuery();
					if (rs2.next()){
						if (rs2.getString("cr_teamcd").equals("1"))
						   rst.put("cm_codename", rs2.getString("cr_confname") + "중");
						else rst.put("cm_codename", rs2.getString("cr_confname") + "결재대기중");
					}
					rs2.close();
					pstmt2.close();
				}
				
				if (rs.getInt("subrowcnt") > 1){
					rst.put("rsrcnamememo", rs.getString("cr_rsrcname")+ "외 " + Integer.toString(rs.getInt("subrowcnt")-1) + "건" );
				}
				else{
					rst.put("rsrcnamememo", rs.getString("cr_rsrcname"));
				}
				rst.put("cr_status",rs.getString("cr_status"));
				rst.put("cr_sysgb",rs.getString("cr_sysgb"));
				rst.put("cr_qrycd",rs.getString("cr_qrycd"));
				if (rs.getString("prcdate") != null) rst.put("prcdate", rs.getString("prcdate"));
				if (rs.getString("cr_prcreq") != null) 
					rst.put("prcreq", rs.getString("cr_prcreq").substring(0, 4) + "/" + 
							rs.getString("cr_prcreq").substring(4, 6) + "/" +
							rs.getString("cr_prcreq").substring(6, 8) + " " +
							rs.getString("cr_prcreq").substring(8, 10) + "/" +
							rs.getString("cr_prcreq").substring(10, 12));
				
					
				strQuery2.setLength(0);
				strQuery2.append("select count(*) as cnt from cmr1010 where \n");
				strQuery2.append("cr_acptno= ? and \n");
				strQuery2.append("cr_confno is null \n");
				
				
				pstmt2 = conn.prepareStatement(strQuery2.toString());
				
				pstmt2.setString(1,rs.getString("cr_acptno"));
				
				rs2 = pstmt2.executeQuery();
				
				if (rs2.next()){
					subrowcnt = rs2.getInt("cnt");
				}
				rs2.close();
				pstmt2.close();
				
				if (subrowcnt > 0){
					rst.put("relno","1");
				}
				else{
					rst.put("relno","0");
				}
				
				rst.put("cr_editor",rs.getString("cr_editor"));
				
				
				strQuery2.setLength(0);
				strQuery2.append("select count(*) as cnt from cmr1011 where \n");
				strQuery2.append("cr_acptno= ? and \n");
				strQuery2.append("cr_rstfile is not null \n");
				
				
				pstmt2 = conn.prepareStatement(strQuery2.toString());
				
				pstmt2.setString(1,rs.getString("cr_acptno"));
				
				rs2 = pstmt2.executeQuery();
				
				while (rs2.next()){
					subrowcnt = rs2.getInt("cnt");
				}
				rs2.close();
				pstmt2.close();
				
				if (subrowcnt > 0){
					rst.put("result","1");
				}
				else{
					rst.put("result","0");
				}
				
				rst.put("cr_syscd",rs.getString("cr_syscd"));
				
				if (rs.getString("prcdate") == null){

					strQuery2.setLength(0);
					strQuery2.append("select count(*) as cnt from cmr1010   \n");
					strQuery2.append(" where cr_acptno= ?                   \n");
					strQuery2.append("   and cr_putcode is not null         \n");
					strQuery2.append("   and nvl(cr_putcode,'0000')<>'0000' \n");
					strQuery2.append("   and nvl(cr_putcode,'RTRY')<>'RTRY' \n");
					
					
					pstmt2 = conn.prepareStatement(strQuery2.toString());
					
					pstmt2.setString(1,rs.getString("cr_acptno"));
					
					rs2 = pstmt2.executeQuery();
					subrowcnt = 0;
					while (rs2.next()){
						subrowcnt = subrowcnt + rs2.getInt("cnt");
					}
					rs2.close();
					pstmt2.close();
					
					if (subrowcnt == 0) {
						strQuery2.setLength(0);
						strQuery2.append("select count(*) as cnt from cmr1011      \n");
						strQuery2.append(" where cr_acptno= ?                      \n");
						strQuery2.append("   and cr_prcrst is not null             \n");
						strQuery2.append("   and(nvl(cr_prcrst,'0000')<>'0000'     \n");
						strQuery2.append("  or nvl(cr_wait,'0')='W')               \n");
						
						
						pstmt2 = conn.prepareStatement(strQuery2.toString());
						
						pstmt2.setString(1,rs.getString("cr_acptno"));
						
						rs2 = pstmt2.executeQuery();
						while (rs2.next()){
							subrowcnt = subrowcnt + rs2.getInt("cnt");
						}
						rs2.close();
						pstmt2.close();
					}
					if (subrowcnt > 0){
						rst.put("errflag","1");
					}
					else{
						rst.put("errflag","0");
					}
				}
				else{
					rst.put("errflag","0");
				}
				

				rtList.add(rst);
				rst = null;
			}//end of while-loop statement
			
			rs.close();
			pstmt.close();
			conn.close();
			conn = null;
			pstmt = null;
			rs = null;
			rtObj =  rtList.toArray();

			rtList = null;
			
			return rtObj;			
			
		} catch (SQLException sqlexception) {
			sqlexception.printStackTrace();
			ecamsLogger.error("## Cmr1100.getFileList() SQLException START ##");
			ecamsLogger.error("## Error DESC : ", sqlexception);	
			ecamsLogger.error("## Cmr1100.getFileList() SQLException END ##");			
			throw sqlexception;
		} catch (Exception exception) {
			exception.printStackTrace();
			ecamsLogger.error("## Cmr1100.getFileList() Exception START ##");				
			ecamsLogger.error("## Error DESC : ", exception);	
			ecamsLogger.error("## Cmr1100.getFileList() Exception END ##");				
			throw exception;
		}finally{
			if (strQuery != null) 	strQuery = null;
			if (strQuery2 != null) 	strQuery2 = null;
			if (rtObj != null)	rtObj = null;
			if (rs != null)     try{rs.close();}catch (Exception ex){ex.printStackTrace();}
			if (pstmt != null)  try{pstmt.close();}catch (Exception ex2){ex2.printStackTrace();}
			if (conn != null){
				try{
					ConnectionResource.release(conn);
				}catch(Exception ex3){
					ecamsLogger.error("## Cmr1100.getFileList() connection release exception ##");
					ex3.printStackTrace();
				}
			}
		}

	}	

}
