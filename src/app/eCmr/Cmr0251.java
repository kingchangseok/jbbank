/*****************************************************************************************
	1. program ID	: Cmr0251.java
	2. create date	: 20011. 11. 01
	3. auth		    : is.choi
	4. update date	: 20011. 11. 01
	5. auth		    : no name
	6. description	: [홈페이지 체크인 상세]
*****************************************************************************************/

package app.eCmr;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.apache.logging.log4j.Logger;
import com.ecams.common.dbconn.ConnectionContext;
import com.ecams.common.dbconn.ConnectionResource;
import com.ecams.common.logger.EcamsLogger;
import java.util.ArrayList;
import java.util.HashMap;

import app.common.LoggableStatement;
import app.common.UserInfo;
import app.common.SystemPath;

/**
 * @author bigeyes
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class Cmr0251{


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
    public Object[] getReqList(String UserId,String AcptNo) throws SQLException, Exception {

		Connection        conn        = null;
		PreparedStatement pstmt       = null;
		PreparedStatement pstmt2       = null;
		ResultSet         rs          = null;
		ResultSet         rs2         = null;
		StringBuffer      strQuery    = new StringBuffer();
		ArrayList<HashMap<String, String>>  rsval  = new ArrayList<HashMap<String, String>>();
		HashMap<String, String>			  rst		  = null;
		Object[] 		  returnObject = null;
		
		ConnectionContext connectionContext = new ConnectionResource();
		
		try {
			conn = connectionContext.getConnection();
			
			strQuery.setLength(0);
			strQuery.append("select to_char(a.cr_acptdate,'yyyy/mm/dd hh24:mi:ss') acptdate,     \n");
			strQuery.append("       to_char(a.cr_prcdate,'yyyy/mm/dd hh24:mi:ss') prcdate,       \n");
			strQuery.append("       a.cr_acptno,a.cr_syscd,a.cr_editor,a.cr_sayu,       \n");
			strQuery.append("       a.cr_status,a.cr_prcreq, a.cr_devptime,                    \n");
			strQuery.append("       a.cr_qrycd,a.cr_gyuljae,a.cr_svrcd,a.cr_svrseq,     \n");
			strQuery.append("       to_char(a.cr_cncllstd,'yyyymmddhh24miss') cncldat,  \n");
			strQuery.append("       a.cr_cnclstep,a.cr_cncllstd,a.cr_retryyn,           \n");
			strQuery.append("       a.cr_passok,b.cm_sysmsg,b.cm_sysinfo,d.cm_username, \n");
			strQuery.append("      (select cm_deptname from cmm0100 where cm_deptcd=d.cm_project) as cm_dept, \n");
			strQuery.append("       DECODE(A.CR_PASSOK, '0', '일반적용','긴급적용') reqpass ,a.cr_orderid,c.cc_reqsub		\n");
			strQuery.append("  from cmc0420 c, cmm0040 d,cmm0030 b,cmr1000 a  \n");
			strQuery.append(" where a.cr_acptno=? and a.cr_orderid=c.cc_orderid(+)          \n");
			strQuery.append("   and a.cr_syscd=b.cm_syscd and a.cr_editor=d.cm_userid   \n");
		
	        
			pstmt = conn.prepareStatement(strQuery.toString());
			pstmt = new LoggableStatement(conn,strQuery.toString());
			pstmt.setString(1,AcptNo);
	        //ecamsLogger.error(((LoggableStatement)pstmt).getQueryString());
	        rs = pstmt.executeQuery();
	        //ecamsLogger.error("++++Query End++++");
	        rsval.clear();

			if (rs.next()){
				rst = new HashMap<String, String>();
				rst.put("ID", Integer.toString(rs.getRow()));
				rst.put("cr_acptno",rs.getString("cr_acptno"));
				rst.put("acptdate",rs.getString("acptdate"));
				if (rs.getString("prcdate") != null){
					rst.put("prcdate",rs.getString("prcdate"));
				}
				else if (rs.getString("cr_gyuljae").equals("1")){
					rst.put("prcdate","우선적용 중");
				}
				rst.put("cr_gyuljae",rs.getString("cr_gyuljae"));
				rst.put("cm_sysmsg",rs.getString("cm_sysmsg"));
				rst.put("cm_username",rs.getString("cm_username"));
				rst.put("cr_syscd",rs.getString("cr_syscd"));
				rst.put("cr_status",rs.getString("cr_status"));
				rst.put("cr_editor",rs.getString("cr_editor"));
				rst.put("cr_passok",rs.getString("cr_passok"));
				rst.put("cr_qrycd",rs.getString("cr_qrycd"));
				rst.put("reqpass",rs.getString("reqpass"));
				rst.put("srcview", "Y");
				rst.put("cr_devptime", rs.getString("cr_devptime"));
				
				if (rs.getString("cr_qrycd").equals("03") && rs.getString("prcdate") != null)
					rst.put("srcview", "N");

				rst.put("cr_qrycd",rs.getString("cr_qrycd"));
				if (rs.getString("cr_orderid") != null) {
					rst.put("cr_orderid",rs.getString("cr_orderid"));
					rst.put("orderid", "["+rs.getString("cr_orderid")+"]"+ rs.getString("cc_reqsub"));
				}
				
				if (rs.getString("cr_sayu") != null)
				   rst.put("cr_sayu",rs.getString("cr_sayu"));
				
				if (rs.getString("cr_passok").equals("4")) {
					rst.put("aplydate", rs.getString("cr_prcreq"));
				}
				if (rs.getString("cr_qrycd").equals("16")) {
					strQuery.setLength(0);
					strQuery.append("select cm_svrname from cmm0031    \n");
					strQuery.append(" where cm_syscd=? and cm_svrcd=?  \n");
					strQuery.append(" and cm_seqno=?                   \n");
					pstmt2 = conn.prepareStatement(strQuery.toString());
					//pstmt2 = new LoggableStatement(conn,strQuery.toString());
					pstmt2.setString(1, rs.getString("cr_syscd"));
					pstmt2.setString(2, rs.getString("cr_svrcd"));
					pstmt2.setInt(3, rs.getInt("cr_svrseq"));
					////ecamsLogger.error(((LoggableStatement)pstmt2).getQueryString());
					rs2 = pstmt2.executeQuery();
					if (rs2.next()) {
						rst.put("web", rs2.getString("cm_svrname"));
					}
					rs2.close();
					pstmt2.close();
				}
				
				strQuery.setLength(0);
				strQuery.append("select count(*) cnt from cmr9900                   \n");
				strQuery.append(" where cr_acptno=? and cr_locat<>'00'              \n");
				strQuery.append("   and cr_teamcd not in('1','2')                   \n");
				strQuery.append("   and (cr_team=? or cr_confusr=?)                 \n");
				pstmt2 = conn.prepareStatement(strQuery.toString());
				pstmt2.setString(1, AcptNo);
				pstmt2.setString(2, UserId);
				pstmt2.setString(3, UserId);
				rs2 = pstmt2.executeQuery();
				if (rs2.next()) {
					if (rs2.getInt("cnt")>0) rst.put("confsw", "1");
					else rst.put("confsw", "0");
				}
				rs2.close();
				pstmt2.close();

				if (rs.getString("cr_status").equals("9")) { //|| rs.getString("cr_status").equals("8")) {
					rst.put("confname","처리완료");
					rst.put("prcsw", "1");
					rst.put("endsw", "1");
					rst.put("updtsw1", "0");
					rst.put("updtsw2", "0");
					rst.put("updtsw3", "0");
					rst.put("log", "1");
				} else if (rs.getString("cr_status").equals("3")) {
					rst.put("confname","반려");
					rst.put("prcsw", "1");
					rst.put("endsw", "1");
					rst.put("updtsw1", "0");
					rst.put("updtsw2", "0");
					rst.put("updtsw3", "0");
					rst.put("log", "1");
				} else {
					String strPrcSw = "0";
					if (rs.getString("prcdate") != null) {
						rst.put("prcsw", "1");
						strPrcSw = "1";
					} else {
						rst.put("prcsw", "0");
						strPrcSw = "0";
						
						strQuery.setLength(0);
						strQuery.append("select count(*) cnt from cmr9900                   \n");
						strQuery.append(" where cr_acptno=? and cr_locat<>'00'              \n");
						strQuery.append("   and cr_teamcd='1' and cr_team='SYSUP'           \n");
						strQuery.append("   and cr_status='9'                               \n");
						pstmt2 = conn.prepareStatement(strQuery.toString());
						pstmt2.setString(1, AcptNo);
						rs2 = pstmt2.executeQuery();
						if (rs2.next()) {
							if (rs2.getInt("cnt")==0) rst.put("srcview", "N");
						}
						rs2.close();
						pstmt2.close();
					}
					rst.put("endsw", "0");
					
					//ecamsLogger.error("++++++++++Cmr0150.confLocat_conn Start++++++++");
					ArrayList<HashMap<String, String>>  rsconf = new ArrayList<HashMap<String, String>>();
					
					Cmr0150 cmr0150 = new Cmr0150();
					System.out.println("strPrcSW "+strPrcSw);
					System.out.println("cr_cnclstep "+ rs.getString("cr_cnclstep"));
					System.out.println("cncldat "+ rs.getString("cncldat"));
					System.out.println("cr_retryyn "+ rs.getString("cr_retryyn"));
					
					rsconf = cmr0150.confLocat_conn(AcptNo,strPrcSw,rs.getString("cr_cnclstep"),rs.getString("cncldat"),rs.getString("cr_retryyn"),conn);
					
					cmr0150 = null;
					//ecamsLogger.error("++++++++++Cmr0150.confLocat_conn End++++++++");
					rst.put("signteam", rsconf.get(0).get("signteam"));
					rst.put("signteamcd", rsconf.get(0).get("signteamcd"));
					rst.put("confusr", rsconf.get(0).get("confusr"));
					rst.put("cr_prcsw", rsconf.get(0).get("prcsw"));
					rst.put("confname", rsconf.get(0).get("confname"));
					rst.put("updtsw1", rsconf.get(0).get("updtsw1"));
					rst.put("updtsw2", rsconf.get(0).get("updtsw2"));
					rst.put("updtsw3", rsconf.get(0).get("updtsw3"));
					rst.put("errtry", rsconf.get(0).get("errtry"));
					rst.put("log", rsconf.get(0).get("log"));
					rst.put("sttry", rsconf.get(0).get("sttry"));
					rst.put("ermsg", rsconf.get(0).get("ermsg"));
					rst.put("skipsw", "N");
					if (rsconf.get(0).get("signteam").equals("SYSUP") && rs.getString("cr_syscd").equals("00202") && 
						rs.getString("cr_qrycd").equals("03") && Integer.parseInt(rsconf.get(0).get("errtry")) > 0) {
						strQuery.setLength(0);
						strQuery.append("select count(*) as cnt from cmr1010    \n");
						strQuery.append(" where cr_acptno=? and cr_rsrccd='92'  \n");
						strQuery.append("   and cr_status='0'                   \n");
						strQuery.append("   and nvl(cr_putcode,'0000')<>'0000'  \n");
				        pstmt2 = conn.prepareStatement(strQuery.toString());
						//pstmt2 = new LoggableStatement(conn,strQuery.toString());
						pstmt2.setString(1, AcptNo);
				        ////ecamsLogger.error(((LoggableStatement)pstmt2).getQueryString());
				        rs2 = pstmt2.executeQuery();
						if (rs2.next()){
							if (rs2.getInt("cnt") > 0){
								rst.put("skipsw","Y");
							}
						}
						rs2.close();
						pstmt2.close();
					}
					rsconf.clear();
					rsconf = null;
				}

				strQuery.setLength(0);
				strQuery.append("select count(*) as cnt from cmr1001               \n");
				strQuery.append(" where cr_acptno=? and cr_gubun='1'               \n");
		        pstmt2 = conn.prepareStatement(strQuery.toString());
				//pstmt2 = new LoggableStatement(conn,strQuery.toString());
				pstmt2.setString(1, AcptNo);
		        ////ecamsLogger.error(((LoggableStatement)pstmt2).getQueryString());
		        rs2 = pstmt2.executeQuery();
				if (rs2.next()){
					if (rs2.getInt("cnt") > 0){
						rst.put("file","1");
					}
					else{
						rst.put("file","0");
					}
				}
				rs2.close();
				pstmt2.close();

				strQuery.setLength(0);
				strQuery.append("select count(*) as cnt from cmr1030               \n");
				strQuery.append(" where cr_acptno=?                                \n");
		        pstmt2 = conn.prepareStatement(strQuery.toString());
				//pstmt2 = new LoggableStatement(conn,strQuery.toString());
				pstmt2.setString(1, AcptNo);
		        ////ecamsLogger.error(((LoggableStatement)pstmt2).getQueryString());
		        rs2 = pstmt2.executeQuery();
				if (rs2.next()){
					if (rs2.getInt("cnt") > 0){
						rst.put("befjob","1");
					}
					else{
						rst.put("befjob","0");
					}
				}
				rs2.close();
				pstmt2.close();
/*
				strQuery.setLength(0);
				strQuery.append("select count(*) as cnt from cmr1010 a,cmm0036 b   \n");
				strQuery.append(" where a.cr_acptno=? and a.cr_status<>'3'         \n");
				strQuery.append("   and a.cr_syscd=b.cm_syscd                      \n");
				strQuery.append("   and a.cr_rsrccd=b.cm_rsrccd                    \n");
				strQuery.append("   and substr(b.cm_info,34,1)='1'                 \n");
		        pstmt2 = conn.prepareStatement(strQuery.toString());	
				//pstmt = new LoggableStatement(conn,strQuery.toString());
				pstmt2.setString(1, AcptNo);
		        ////ecamsLogger.error(((LoggableStatement)pstmt).getQueryString());           
		        rs2 = pstmt2.executeQuery();
				if (rs2.next()){
					if (rs2.getInt("cnt") > 0) rst.put("analsw","Y");	
				}
				rs2.close();
				pstmt2.close();
*/
				rsval.add(rst);
				rst = null;

			}//end of while-loop statement
			rs.close();
			pstmt.close();
			conn.close();

	        //ecamsLogger.error("++++function End++++");
			rs = null;
			pstmt = null;
			rs2 = null;
			pstmt2 = null;
			conn = null;

			returnObject = rsval.toArray();
			rsval.clear();
			rsval = null;
			
			return returnObject;


		} catch (SQLException sqlexception) {
			sqlexception.printStackTrace();
			ecamsLogger.error("## Cmr0251.getReqList() SQLException START ##");
			ecamsLogger.error("## Error DESC : ", sqlexception);
			ecamsLogger.error("## Cmr0251.getReqList() SQLException END ##");
			throw sqlexception;
		} catch (Exception exception) {
			exception.printStackTrace();
			ecamsLogger.error("## Cmr0251.getReqList() Exception START ##");
			ecamsLogger.error("## Error DESC : ", exception);
			ecamsLogger.error("## Cmr0251.getReqList() Exception END ##");
			throw exception;
		}finally{
			if (strQuery != null) strQuery = null;
			if (returnObject != null)	returnObject = null;
			if (rs != null)     try{rs.close();}catch (Exception ex){ex.printStackTrace();}
			if (pstmt != null)  try{pstmt.close();}catch (Exception ex2){ex2.printStackTrace();}
			if (conn != null){
				try{
					ConnectionResource.release(conn);
				}catch(Exception ex3){
					ecamsLogger.error("## Cmr0251.getReqList() connection release exception ##");
					ex3.printStackTrace();
				}
			}
		}
	}//end of getReqList() method statement
    
    public Object[] getProgList(String UserId,String AcptNo,String chkYn,boolean qrySw) throws SQLException, Exception {
		Connection        conn        = null;
		PreparedStatement pstmt       = null;
		PreparedStatement pstmt2       = null;
		ResultSet         rs          = null;
		ResultSet         rs2         = null;
		StringBuffer      strQuery    = new StringBuffer();
		Object[] 		  returnObject = null;
		boolean           findSw      = false;
		boolean           errSw       = false;
		String            strPutCd    = "";
		ArrayList<HashMap<String, String>>  rsval = new ArrayList<HashMap<String, String>>();
		HashMap<String, String>			  rst		  = null;
		ConnectionContext connectionContext = new ConnectionResource();
		
		try {
			
			conn = connectionContext.getConnection();
			
			UserInfo         userinfo = new UserInfo();
			String strTeam = userinfo.getUserInfo_sub(conn, UserId, "CM_PROJECT");
			boolean adminSw = userinfo.isAdmin_conn(UserId, conn);
			userinfo = null;
		
			//ecamsLogger.error("++++proglist start++++++++++");
			strQuery.setLength(0);
			strQuery.append("select a.cr_rsrcname, a.cr_serno, a.cr_putcode, a.cr_rsrccd, a.cr_editcon, g.cr_status,															\n");
			strQuery.append("      	  a.cr_confno, a.cr_dsncd, a.cr_dsncd2, a.cr_baseno, a.cr_confno, s.cm_sysinfo, g.cr_qrycd qrycd,										\n");
			strQuery.append("        a.cr_prcdate, a.cr_priority, a.cr_jobcd, a.cr_qrycd, c.cm_codename, e.cm_jobname,														\n");
			strQuery.append("        d.cm_info, a.cr_editor, g.cr_teamcd, g.cr_syscd, a.cr_jobcd,nvl(x.cnt,0) rst,	nvl(z.cnt,0) basecnt, nvl(u.cnt,0) baserst					\n");
			strQuery.append("       from cmr1000 g, cmr1010 a, cmm0030 s, cmm0020 c, cmm0102 e, cmm0036 d															\n");
			strQuery.append(" 			   ,(select count(*) cnt from cmr1011						 																					\n");
			strQuery.append("    					where cr_acptno=? and cr_prcdate is not null	                														 		\n");
			strQuery.append("    			) x						                    																					 				\n");
			strQuery.append(" 				,(select count(*) cnt from cmr1010 a    		     																						\n");
			strQuery.append("    					where a.cr_acptno=? and a.cr_status='0'                        																\n");
			strQuery.append("      			and nvl(a.cr_putcode,'0000')<>'0000'                         																			\n");
			strQuery.append("   			) z		   		                         																										\n");
			strQuery.append(" 				,(select count(*) cnt from cmr1011 a,cmr1010 b		 																				\n");
			strQuery.append("    					where a.cr_acptno=? and a.cr_prcdate is not null                																\n");
			strQuery.append("      			 and a.cr_acptno=b.cr_acptno and a.cr_serno=b.cr_serno         															\n");
			strQuery.append("   			) u		   		                         																								\n");
			strQuery.append("    	  	       where  a.cr_acptno= ?																									\n");
			strQuery.append("    	  	       and    a.cr_acptno=g.cr_acptno																						\n");
			strQuery.append("					and    c.cm_macode='JAWON'																						\n");
			strQuery.append("					and    c.cm_micode=a.cr_rsrccd																						\n");
			strQuery.append("					and    a.cr_syscd=d.cm_syscd																							\n");
			strQuery.append("					and    a.cr_rsrccd=d.cm_rsrccd																						\n");
			strQuery.append("					and    a.cr_jobcd=e.cm_jobcd																							\n");
			strQuery.append("					and    a.cr_syscd=s.cm_syscd																							\n"); 
			strQuery.append("					order by cr_rsrcname																									\n");
			pstmt = conn.prepareStatement(strQuery.toString());
			//pstmt = new LoggableStatement(conn,strQuery.toString());
			pstmt.setString(1, AcptNo);
			pstmt.setString(2, AcptNo);
			pstmt.setString(3, AcptNo);
			pstmt.setString(4, AcptNo);
		
	        ////ecamsLogger.error(((LoggableStatement)pstmt).getQueryString());
	        rs = pstmt.executeQuery();

	        rsval.clear();
	        String tmpDate = "";
	        //ecamsLogger.error("+++++Query End++++++");
			while (rs.next()){
				findSw = true;
				errSw = false;
				rst = new HashMap<String, String>();
				rst.put("ID", Integer.toString(rs.getRow()));
				rst.put("cr_rsrcname",rs.getString("cr_rsrcname"));
				rst.put("cr_rsrccd",rs.getString("cr_rsrccd"));
				rst.put("cr_jobcd",rs.getString("cr_jobcd"));
				rst.put("cr_status",rs.getString("cr_status"));
				rst.put("cm_sysinfo",rs.getString("cm_sysinfo"));
				rst.put("cr_dsncd2", rs.getString("cr_dsncd2"));
				/*if (rs.getString("cr_status").equals("3")){
					rst.put("checkin",rs.getString("checkin")+"[반려]");
				}
				else{
					rst.put("checkin", rs.getString("checkin"));
				}*/
				/*
				if (rs.getString("cr_itemid") != null && rs.getString("cr_itemid") != "") {
					if (rs.getString("cr_itemid").equals(rs.getString("cr_baseitem"))) {
						rst.put("sortfg", rs.getString("cr_baseitem")+"0");
					} else {
						rst.put("sortfg",rs.getString("cr_baseitem")+"2");
					}
				} else {
					rst.put("sortfg", rs.getString("cr_baseitem")+"1");
				}*/
				/*
				if (rs.getString("cr_confno") != null) {
					if (rs.getString("cr_confno").substring(4,6).equals("01") || rs.getString("cr_confno").substring(4,6).equals("02")){
						rst.put("cr_confno",rs.getString("cr_confno"));
					} else {
						tmpDate = "";
						if (AcptNo.substring(4,6).equals("04")){//체크인(운영)상세 일 경우)
							strQuery.setLength(0);
							strQuery.append("select to_char(cr_prcdate,'yyyy/mm/dd hh24:mi') testprcdate  \n");
							strQuery.append("  from cmr1000 \n");
							strQuery.append(" where cr_acptno = ? \n");
					        pstmt2 = conn.prepareStatement(strQuery.toString());
					        //pstmt2 = new LoggableStatement(conn,strQuery.toString());
							pstmt2.setString(1, rs.getString("cr_confno"));
							////ecamsLogger.error(((LoggableStatement)pstmt2).getQueryString());
					        rs2 = pstmt2.executeQuery();
							if(rs2.next()){
								tmpDate = rs2.getString("testprcdate");
							}
							rs2.close();
							pstmt2.close();
						}
						rst.put("testprcdate", tmpDate);
					}
				}*/
				
				//rst.put("cr_itemid",rs.getString("cr_itemid"));
				//rst.put("cr_baseitem",rs.getString("cr_baseitem"));
				//rst.put("cr_dsncd",rs.getString("cr_dsncd"));
				//rst.put("cm_dirpath",rs.getString("cm_dirpath"));
				rst.put("cm_codename",rs.getString("cm_codename"));
				rst.put("cm_info",rs.getString("cm_info"));
				rst.put("cr_qrycd",rs.getString("cr_qrycd"));
				//rst.put("cr_story", rs.getString("cr_story"));
				
			
				//rst.put("cr_pgmtype", rs.getString("cr_pgmtype"));
				//rst.put("pgmtype", rs.getString("pgmtype"));
			
				rst.put("cm_jobname",rs.getString("cm_jobname"));
				rst.put("priority", Integer.toString(rs.getInt("cr_priority")));
				//rst.put("cr_version",Integer.toString(rs.getInt("cr_version")));
				/*if (rs.getString("cr_aplydate") != null){
					rst.put("cr_aplydate", rs.getString("cr_aplydate"));
				}*/
				rst.put("check", "false");
				rst.put("visible", "false");
				/*(
				if (rs.getString("cr_prcdate") != null){
					rst.put("cr_prcdate", rs.getString("cr_prcdate"));
				} else if (rs.getString("cr_itemid") != null) {
					if (rs.getString("qrycd").equals("03")) {
						if(rs.getString("cr_baseitem") != null && rs.getString("cr_baseitem") != ""){
							if(rs.getString("cr_baseitem").equals(rs.getString("cr_itemid"))) {
								if (UserId.equals(rs.getString("cr_editor")) || adminSw) {
									rst.put("visible", "true");
									rst.put("check", "true");
								}
							}
						}
					}
				}
      			*/
				
				rst.put("cr_serno",Integer.toString(rs.getInt("cr_serno")));
				if (rs.getString("cr_putcode") != null){
					if (rs.getString("cr_putcode") != null) {
					   if (!rs.getString("cr_putcode").equals("0000")) {
						   rst.put("cr_putcode",rs.getString("cr_putcode"));
					   } else if (strPutCd != "") rst.put("cr_putcode",strPutCd);
					   else rst.put("cr_putcode",rs.getString("cr_putcode"));
					} else rst.put("cr_putcode",strPutCd);
				}
				
				if (rs.getString("cr_editor").equals(UserId)) rst.put("secusw", "Y");
				else if (rs.getString("cr_teamcd").equals(strTeam)) {
					rst.put("secusw", "Y");
				} 
				
			
				if (!rs.getString("cr_status").equals("3")) {
					if (rs.getString("basecnt") != null) {
						if (rs.getInt("basecnt")>0) errSw = true;
					}
				}

				if (rs.getString("cr_editcon") != null)
				{
					rst.put("cr_editcon",rs.getString("cr_editcon"));
				}
				
				
				if (!rs.getString("cr_status").equals("3")) 
				{	
					if (rs.getString("cr_putcode") != null && !rs.getString("cr_putcode").equals("0000") && !rs.getString("cr_putcode").equals("RTRY"))
						rst.put("ColorSw","5");
		
					else if (errSw == true) 
						rst.put("ColorSw","5");
					
					else
					{
						if (rs.getString("cr_status").equals("8") || rs.getString("cr_status").equals("9"))
							rst.put("ColorSw","9");
						else 
							rst.put("ColorSw","0");
					}
					if (rs.getString("cr_status").equals("8") || rs.getString("cr_status").equals("9")) {
						rst.put("rst", "Y");
					}
					if(rs.getString("cr_putcode") == null && rs.getString("cr_status").equals("0")){
						if (rs.getString("baserst") != null) {
							if (rs.getInt("baserst") > 0) rst.put("rst", "Y");
							else rst.put("rst", "N");
						} else {
							rst.put("rst", "N");
						}
						if (rs.getString("rst") != null) {
							if (rs.getInt("rst") > 0) rst.put("rst", "Y");
							else rst.put("rst", "N");
						} else {
							rst.put("rst", "N");
						}
					}
					else
						rst.put("rst","Y");
				}
				else 
					rst.put("ColorSw","3");
				
		        if (findSw == true){
		        	
		        	rsval.add(rst);
		        	rst = null;
		        }
			}//end of while-loop statement
			rs.close();
			pstmt.close();
			conn.close();
			rs = null;
			pstmt = null;
	        rs2 = null;
	        pstmt2 = null;
			conn = null;
			//ecamsLogger.error("++++proglist end++++++++++");
			returnObject = rsval.toArray();
			rsval.clear();
			rsval = null;
			
			return returnObject;


		} catch (SQLException sqlexception) {
			sqlexception.printStackTrace();
			ecamsLogger.error("## Cmr0251.getFileList() SQLException START ##");
			ecamsLogger.error("## Error DESC : ", sqlexception);
			ecamsLogger.error("## Cmr0251.getFileList() SQLException END ##");
			throw sqlexception;
		} catch (Exception exception) {
			exception.printStackTrace();
			ecamsLogger.error("## Cmr0251.getFileList() Exception START ##");
			ecamsLogger.error("## Error DESC : ", exception);
			ecamsLogger.error("## Cmr0251.getFileList() Exception END ##");
			throw exception;
		}finally{
			if (strQuery != null) strQuery = null;
			if (returnObject != null) returnObject = null;
			if (rs != null)     try{rs.close();}catch (Exception ex){ex.printStackTrace();}
			if (pstmt != null)  try{pstmt.close();}catch (Exception ex2){ex2.printStackTrace();}
			if (conn != null){
				try{
					conn.close();conn = null;
				}catch(Exception ex3){
					ecamsLogger.error("## Cmr0251.getFileList() connection release exception ##");
					ex3.printStackTrace();
				}
			}
		}
	}//end of getFileList() method statement
    
    public Object[] chkBtnEnabled(String UserId,String AcptNo) throws SQLException, Exception {
		Connection        conn        = null;
		PreparedStatement pstmt       = null;
		ResultSet         rs          = null;
		StringBuffer      strQuery    = new StringBuffer();
		Object[] 		  returnObject = null;
		ArrayList<HashMap<String, Integer>>  rsval = new ArrayList<HashMap<String, Integer>>();
		HashMap<String, Integer>			  rst		  = null;
		ConnectionContext connectionContext = new ConnectionResource();
		
		try {
			
			conn = connectionContext.getConnection();
			
			//UserInfo         userinfo = new UserInfo();
			//String strTeam = userinfo.getUserInfo_sub(conn, UserId, "CM_PROJECT");
			//boolean adminSw = userinfo.isAdmin_conn(UserId, conn);
			//userinfo = null;
			int paramCnt = 0;
			
			//LIB: 프로그램관리자, A1: eCAMS 관리자
			
			strQuery.setLength(0);
			strQuery.append("		select																								\n");
			strQuery.append("				(select DECODE(COUNT(*),0,1,0)																\n");
			strQuery.append("						from cmr9900 b																		\n");
			strQuery.append("						where b.cr_acptno = a.cr_acptno														\n");				
			strQuery.append("						and substr(b.cr_team,0,3) != 'SYS' 													\n");		
			strQuery.append("						and b.cr_status != '0'																\n");
			strQuery.append("						and (a.cr_editor = ? or 															\n");
			strQuery.append("								1 = (select DECODE(COUNT(*),0,0,1)											\n"); 
			strQuery.append("												from cmm0043 												\n");
			strQuery.append("												where cm_userid = ? 										\n");
			strQuery.append("												and cm_rgtcd in ('LIB', 'A1')))								\n");
			strQuery.append("				) isBack,																					\n");
			paramCnt++;
			paramCnt++;
			
			strQuery.append("				(select DECODE(COUNT(*),0,0,1)																\n");
			strQuery.append("						from cmr1000																		\n");
			strQuery.append("						where cr_acptno = a.cr_acptno 														\n");
			strQuery.append("						and cr_status not in('9','3') 														\n");
			strQuery.append("						and cr_prcdate is null																\n");
			strQuery.append("						and (cr_editor = ? or 																\n");
			strQuery.append("								1 = (select DECODE(COUNT(*),0,0,1) 											\n");
			strQuery.append("												from cmm0043 												\n");
			strQuery.append("												where cm_userid = ?											\n"); 
			strQuery.append("												and cm_rgtcd in ('A1'))) 									\n");
			strQuery.append("				) isRetry,																					\n");
			paramCnt++;
			paramCnt++;
	
			strQuery.append("				(select DECODE(COUNT(*),0,0,1)																\n");	
			strQuery.append("						from cmr1011 b																		\n");		
			strQuery.append("						where cr_acptno = b.cr_acptno														\n");	
			strQuery.append("						and cr_acptno = b.cr_acptno															\n");
			strQuery.append("						and cr_status not in('9','3') 														\n");		
			strQuery.append("						and cr_prcdate is null																\n"); 
			strQuery.append("						and b.cr_prcsys not in('SYSED','SYSCB')												\n");
			strQuery.append("						and																					\n");
			strQuery.append("								1 = (select DECODE(COUNT(*),0,0,1) 											\n");
			strQuery.append("												from cmm0043 												\n");
			strQuery.append("												where cm_userid = ? 										\n");
			strQuery.append("												and cm_rgtcd in ('LIB','A1'))								\n");
			strQuery.append("				) isSeq,																					\n");
			paramCnt++;
			
			strQuery.append("				(select DECODE(COUNT(*),0,0,1)																\n");		
			strQuery.append("						from cmr1000																		\n");	
			strQuery.append("						where cr_acptno = a.cr_acptno 														\n");	
			strQuery.append("						and cr_status not in('9','3') 														\n");
			strQuery.append("						and cr_prcdate is null																\n");
			strQuery.append("						and (cr_editor = ? or 																\n");
			strQuery.append("								1 = (select DECODE(COUNT(*),0,0,1) 											\n");
			strQuery.append("												from cmm0043 												\n");
			strQuery.append("												where cm_userid = ?											\n"); 
			strQuery.append("												and cm_rgtcd in ('A1')))									\n");
			strQuery.append("				) ishidden, 																				\n");
			paramCnt++;
			paramCnt++;
			
			strQuery.append("				(select DECODE(COUNT(*),0,0,1)																\n");		
			strQuery.append("						from cmr1000																		\n");					
			strQuery.append("						where cr_acptno = a.cr_acptno 														\n");
			strQuery.append("						and cr_status not in('9','3') 														\n");
			strQuery.append("						and cr_prcdate is null																\n");
			strQuery.append("						and  																				\n");
			strQuery.append("								1 = (select DECODE(COUNT(*),0,0,1)											\n"); 
			strQuery.append("										from cmm0043 														\n");
			strQuery.append("										where cm_userid = ? 												\n");
			strQuery.append("										and cm_rgtcd in ('LIB','A1'))										\n");
			strQuery.append("				) isFirst,																					\n");
			paramCnt++;
			
			strQuery.append("				(select DECODE(cr_gyuljae,0,0,1)															\n");
			strQuery.append("						from cmr1000																		\n");	
			strQuery.append("						where cr_acptno = a.cr_acptno 														\n");
			strQuery.append("				) isFirst_sub																				\n");
			strQuery.append("				from cmr1000 a																				\n");
			strQuery.append("				where a.cr_acptno = ?																		\n");

			//pstmt = conn.prepareStatement(strQuery.toString());
			pstmt = new LoggableStatement(conn,strQuery.toString());
			
			for(int i=0;i<paramCnt;i++)
				pstmt.setString(i+1, UserId	);
			pstmt.setString(++paramCnt, AcptNo);
			
	        ecamsLogger.error(((LoggableStatement)pstmt).getQueryString());
	        rs = pstmt.executeQuery();

	        rsval.clear();
	       
	        //ecamsLogger.error("+++++Query End++++++");
			if (rs.next()){
				rst = new HashMap<String, Integer>();
				rst.put("isBack", rs.getInt("isBack"));
				rst.put("isRetry", rs.getInt("isRetry"));
				rst.put("isSeq", rs.getInt("isSeq"));
				rst.put("ishidden", rs.getInt("ishidden"));
				rst.put("isFirst", rs.getInt("isFirst"));
				rst.put("isFirst_sub", rs.getInt("isFirst_sub"));
				rsval.add(rst);
				rst = null;
			}//end of while-loop statement
			
			rs.close();
			pstmt.close();
			conn.close();
			
			rs = null;
			pstmt = null;
	     	conn = null;
	     	
			returnObject = rsval.toArray();
			rsval.clear();
			rsval = null;
			
			return returnObject;

		} catch (SQLException sqlexception) {
			sqlexception.printStackTrace();
			ecamsLogger.error("## Cmr0251.chkBtnEnabled() SQLException START ##");
			ecamsLogger.error("## Error DESC : ", sqlexception);
			ecamsLogger.error("## Cmr0251.chkBtnEnabled() SQLException END ##");
			throw sqlexception;
		} catch (Exception exception) {
			exception.printStackTrace();
			ecamsLogger.error("## Cmr0251.chkBtnEnabled() Exception START ##");
			ecamsLogger.error("## Error DESC : ", exception);
			ecamsLogger.error("## Cmr0251.chkBtnEnabled() Exception END ##");
			throw exception;
		}finally{
			if (strQuery != null) strQuery = null;
			if (returnObject != null) returnObject = null;
			if (rs != null)     try{rs.close();}catch (Exception ex){ex.printStackTrace();}
			if (pstmt != null)  try{pstmt.close();}catch (Exception ex2){ex2.printStackTrace();}
			if (conn != null){
				try{
					conn.close();conn = null;
				}catch(Exception ex3){
					ecamsLogger.error("## Cmr0251.chkBtnEnabled() connection release exception ##");
					ex3.printStackTrace();
				}
			}
		}
	}//end of getFileList() method statement
    
}//end of Cmr0250 class statement