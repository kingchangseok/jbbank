/*****************************************************************************************
	1. program ID	: Cmr0250.java
	2. create date	: 2008. 08. 10
	3. auth		    : is.choi
	4. update date	: 2009. 05. 20
	5. auth		    : no name
	6. description	: [체크인신청상세]
*****************************************************************************************/

package app.eCmr;

import java.sql.Blob;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.apache.logging.log4j.Logger;
import com.ecams.common.dbconn.ConnectionContext;
import com.ecams.common.dbconn.ConnectionResource;
import com.ecams.common.logger.EcamsLogger;
import com.ecams.service.srlink.SRRestApi_20251119;

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
public class Cmr0250_20251119{


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
			strQuery.append("       a.cr_status,a.cr_prcreq,                            \n");
			strQuery.append("       a.cr_qrycd,a.cr_gyuljae,a.cr_svrcd,a.cr_svrseq,     \n");
			strQuery.append("       to_char(a.cr_cncllstd,'yyyymmddhh24miss') cncldat,  \n");
			strQuery.append("       a.cr_cnclstep,a.cr_cncllstd,a.cr_retryyn,           \n");
			strQuery.append("       a.cr_passok,b.cm_sysmsg,b.cm_sysinfo,d.cm_username, \n");
			strQuery.append("      (select cm_username from cmm0040 where cm_userid = a.cr_orgeditor) as cr_orgeditorname, \n");
			strQuery.append("      (select cm_deptname from cmm0100 where cm_deptcd=d.cm_project) as cm_dept, \n");
			strQuery.append("       DECODE(A.CR_PASSOK, '0', '일반적용','긴급적용') reqpass ,a.cr_orderid,c.cc_reqsub		\n");
			strQuery.append("  from cmc0420 c, cmm0040 d,cmm0030 b,cmr1000 a  \n");
			strQuery.append(" where a.cr_acptno=? and a.cr_orderid=c.cc_orderid(+)          \n");
			strQuery.append("   and a.cr_syscd=b.cm_syscd and a.cr_editor=d.cm_userid   \n");
	        
			pstmt = conn.prepareStatement(strQuery.toString());
			//pstmt = new LoggableStatement(conn,strQuery.toString());
			pstmt.setString(1,AcptNo);
	        //ecamsLogger.error(((LoggableStatement)pstmt).getQueryString());
	        rs = pstmt.executeQuery();
	        //ecamsLogger.error("++++Query End++++");
	        rsval.clear();

			if (rs.next()){
				rst = new HashMap<String, String>();
				strQuery.setLength(0);
				strQuery.append("select count(a.cr_acptno) as cnt   \n");
				strQuery.append("from cmr1015 a, cmr0020 b,cmm0040 d,cmm0040 e,cmm0040 f,cmm0030 g,cmr0020 h,cmr1000 i       \n");
				strQuery.append(" WHERE  EXISTS (SELECT  'X'                               \n");
				strQuery.append("         FROM  CMR1010 A1                                  \n");
				strQuery.append("             , CMR0020 B1                                  \n");
				strQuery.append("        WHERE  EXISTS (SELECT 'X'                         \n");
				strQuery.append("                         FROM CMR1010                     \n");
				strQuery.append("                        WHERE CR_ACPTNO = ?               \n");
				strQuery.append("                          AND CR_CONFNO = A1.CR_ACPTNO     \n");
				strQuery.append("                      )                                   \n");
				strQuery.append("          AND  A1.CR_ITEMID = B1.CR_BASEITEM                \n");
				strQuery.append("          AND  A1.CR_ACPTNO = a.CR_ACPTNO                  \n");
				strQuery.append("          AND  B1.CR_ITEMID = a.CR_ITEMID                  \n");
				strQuery.append("       )                                                  \n");				
				strQuery.append("and f.cm_userid = i.cr_editor        \n");
				strQuery.append("and i.cr_acptno = a.cr_acptno        \n");
				strQuery.append("and b.cr_itemid = a.cr_refitemid        \n");
				strQuery.append("and h.cr_itemid = a.cr_itemid        \n");
				strQuery.append("and a.cr_refuser = d.cm_userid          	\n");
				strQuery.append("and a.cr_chkusser = e.cm_userid(+)           		\n");
				strQuery.append("and b.cr_syscd = g.cm_syscd           		\n");
				pstmt2 = conn.prepareStatement(strQuery.toString());
				//pstmt = new LoggableStatement(conn,strQuery.toString());
				pstmt2.setString(1,AcptNo);
		        //ecamsLogger.error(((LoggableStatement)pstmt).getQueryString());
		        rs2 = pstmt2.executeQuery();
		        if(rs2.next()){
		        	if(rs2.getInt("cnt") > 0){
		        		rst.put("BtAsta", "1");
		        	}else{
		        		rst.put("BtAsta", "0");
		        	}
		        		
		        }
				
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
				rst.put("cm_sysinfo",rs.getString("cm_sysinfo"));
				if(rs.getString("cr_orgeditorname") != null && !"".equals(rs.getString("cr_orgeditorname"))){
					rst.put("cm_username",rs.getString("cm_username") + "(" + rs.getString("cr_orgeditorname") + ")");
				} else {
					rst.put("cm_username",rs.getString("cm_username"));
				}
				rst.put("cr_syscd",rs.getString("cr_syscd"));
				rst.put("cr_status",rs.getString("cr_status"));
				rst.put("cr_editor",rs.getString("cr_editor"));
				rst.put("cr_passok",rs.getString("cr_passok"));
				rst.put("cr_qrycd",rs.getString("cr_qrycd"));
				rst.put("reqpass",rs.getString("reqpass"));
				rst.put("sysedcnt", "Y");
				rst.put("srcview", "Y");
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


				if ("04".equals(rs.getString("cr_qrycd"))) {
					strQuery.setLength(0);
					strQuery.append("select count(*) cnt from cmr9900                   \n");
					strQuery.append(" where cr_acptno=? and cr_locat<>'00'              \n");
					strQuery.append("   and cr_teamcd='1' and cr_team='SYSED'           \n");
					pstmt2 = conn.prepareStatement(strQuery.toString());
					pstmt2.setString(1, AcptNo);
					rs2 = pstmt2.executeQuery();
					if (rs2.next()) {
						if (rs2.getInt("cnt")==0) rst.put("sysedcnt", "N");
					}
					rs2.close();
					pstmt2.close();
				}
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
					
					rsconf = cmr0150.confLocat_conn(AcptNo,strPrcSw,rs.getString("cr_cnclstep"),rs.getString("cncldat"),rs.getString("cr_retryyn"),conn);
					cmr0150 = null;
					//ecamsLogger.error("++++++++++Cmr0150.confLocat_conn End++++++++");
					rst.put("signteam", rsconf.get(0).get("signteam"));
					rst.put("signteamcd", rsconf.get(0).get("signteamcd"));
					rst.put("confusr", rsconf.get(0).get("confusr"));
					rst.put("cr_prcsw", rsconf.get(0).get("prcsw"));
					rst.put("cr_sgngbn", rsconf.get(0).get("cr_sgngbn"));
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
			ecamsLogger.error("## Cmr0250.getReqList() SQLException START ##");
			ecamsLogger.error("## Error DESC : ", sqlexception);
			ecamsLogger.error("## Cmr0250.getReqList() SQLException END ##");
			throw sqlexception;
		} catch (Exception exception) {
			exception.printStackTrace();
			ecamsLogger.error("## Cmr0250.getReqList() Exception START ##");
			ecamsLogger.error("## Error DESC : ", exception);
			ecamsLogger.error("## Cmr0250.getReqList() Exception END ##");
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
					ecamsLogger.error("## Cmr0250.getReqList() connection release exception ##");
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
			strQuery.append("select a.cr_rsrcname,a.cr_serno,a.cr_putcode,a.cr_rsrccd,a.cr_editcon, 												\n");
			strQuery.append("		a.cr_testyn,																									\n");
			strQuery.append("		decode(a.cr_testyn,null,'',decode(a.cr_testyn,'01','Y','N')) testyn,											\n");
			strQuery.append("		a.cr_important,																									\n"); 
			strQuery.append("		(select cm_codename from cmm0020 where cm_macode = 'IMPORTANT' and cm_micode = a.cr_important ) important,		\n");
			strQuery.append("		a.cr_newglo,																									\n");
			strQuery.append("		(select cm_codename from cmm0020 where cm_macode = 'NEW/GLO' and cm_micode = a.cr_newglo) newglo,				\n");
			strQuery.append("		a.cr_aplydate,																									\n"); 
//			strQuery.append("		(select cm_codename from cmm0020 where cm_macode = 'REQPASS' and cm_micode = decode(a.cr_aplydate,null,'0','4') and a.cr_qrycd = '04') aply,	\n"); // 20231130 일반으로만 보임
			strQuery.append("		(select cm_codename from cmm0020 where cm_macode = 'REQPASS' and cm_micode = g.cr_passok and a.cr_qrycd = '04') aply,	\n");
			strQuery.append("		a.cr_status,a.cr_confno,a.cr_itemid,a.cr_baseitem,a.cr_dsncd,													\n"); 	 
			strQuery.append("		a.cr_baseno,a.cr_confno,s.cm_sysinfo,g.cr_qrycd qrycd,															\n");          
			strQuery.append("		a.cr_prcdate,a.cr_priority,a.cr_version,a.cr_jobcd,																\n");
			strQuery.append("		a.cr_qrycd,b.cm_dirpath,c.cm_codename,e.cm_jobname,d.cm_info,													\n"); 	 
			strQuery.append("		f.cm_codename checkin,a.cr_editor,g.cr_teamcd,g.cr_syscd,														\n");       
			strQuery.append("		a.cr_jobcd,nvl(x.cnt,0) rst, h.cr_story,																		\n");                       
			strQuery.append("		nvl(z.cnt,0) basecnt,nvl(u.cnt,0) baserst, a.cr_befmd5,a.cr_md5sum												\n");        

			strQuery.append("  from cmr0020 h, cmm0030 s,cmr1000 g,cmm0020 f,cmm0102 e,cmm0036 d,cmm0020 c,cmm0070 b,cmr1010 a \n");
			strQuery.append(" 		,(select cr_serno,count(*) cnt from cmr1011						 \n");
			strQuery.append("   	   where cr_acptno=? and cr_prcdate is not null	                 \n");
			strQuery.append("    	   group by cr_serno) x						                     \n");
			strQuery.append(" 		,(select a.cr_baseitem,count(*) cnt from cmr1010 a    		     \n");
			strQuery.append("          where a.cr_acptno=? and a.cr_status='0'                       \n");
			strQuery.append("      		 and nvl(a.cr_putcode,'0000')<>'0000'                        \n");
			strQuery.append("    	   group by a.cr_baseitem) z		   		                     \n");
			strQuery.append(" 		,(select b.cr_baseitem,count(*) cnt from cmr1011 a,cmr1010 b	 \n");
			strQuery.append("   	   where a.cr_acptno=? and a.cr_prcdate is not null              \n");
			strQuery.append("      	     and a.cr_acptno=b.cr_acptno and a.cr_serno=b.cr_serno       \n");
			strQuery.append("    	   group by b.cr_baseitem) u		   		                     \n");
			strQuery.append(" where a.cr_acptno=?                    					         	 \n");
			if (qrySw == false) {
				strQuery.append("and a.cr_itemid=a.cr_baseitem                                   	 \n");
			}
			strQuery.append("   and a.cr_acptno=g.cr_acptno					                     	 \n");
			strQuery.append("   and a.cr_syscd=b.cm_syscd and a.cr_dsncd=b.cm_dsncd              	 \n");
			strQuery.append("   and c.cm_macode='JAWON' and c.cm_micode=a.cr_rsrccd              	 \n");
			strQuery.append("   and f.cm_macode='CHECKIN' and f.cm_micode=a.cr_qrycd             	 \n");
			strQuery.append("   and a.cr_syscd=d.cm_syscd and a.cr_rsrccd=d.cm_rsrccd            	 \n");
			strQuery.append("   and a.cr_jobcd=e.cm_jobcd and a.cr_syscd=s.cm_syscd		         	 \n");
	        strQuery.append("   and a.cr_serno=x.cr_serno(+)                                     	 \n");
	        strQuery.append("   and a.cr_baseitem=z.cr_baseitem(+)                               	 \n");
	        strQuery.append("   and a.cr_baseitem=u.cr_baseitem(+)                               	 \n");
	        strQuery.append("   and a.cr_itemid = h.cr_itemid 	                                 	 \n");
	        strQuery.append(" order by NVL(ROOT_RSRCNAME(a.cr_acptno, a.cr_itemid), A.CR_RSRCNAME), decode(a.cr_baseitem,  a.cr_itemid, 0, 1)	\n");
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
				if (rs.getString("cr_status").equals("3")){
					rst.put("checkin",rs.getString("checkin")+"[반려]");
				}
				else{
					rst.put("checkin", rs.getString("checkin"));
				}
				if (rs.getString("cr_itemid") != null && rs.getString("cr_itemid") != "") {
					if (rs.getString("cr_itemid").equals(rs.getString("cr_baseitem"))) {
						rst.put("sortfg", rs.getString("cr_baseitem")+"0");
					} else {
						rst.put("sortfg",rs.getString("cr_baseitem")+"2");
					}
				} else {
					rst.put("sortfg", rs.getString("cr_baseitem")+"1");
				}
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
				}
				
				rst.put("cr_itemid",rs.getString("cr_itemid"));
				rst.put("cr_baseitem",rs.getString("cr_baseitem"));
				rst.put("cr_dsncd",rs.getString("cr_dsncd"));
				rst.put("cm_dirpath",rs.getString("cm_dirpath"));
				rst.put("cm_codename",rs.getString("cm_codename"));
				rst.put("cm_info",rs.getString("cm_info"));
				rst.put("cr_qrycd",rs.getString("cr_qrycd"));
				rst.put("cr_story", rs.getString("cr_story"));
				
				rst.put("testyn",rs.getString("testyn"));
				rst.put("important",rs.getString("important"));
				rst.put("newglo",rs.getString("newglo"));
				rst.put("aply", rs.getString("aply"));
				//rst.put("cr_pgmtype", rs.getString("cr_pgmtype"));
				//rst.put("pgmtype", rs.getString("pgmtype"));
				
				if (AcptNo.substring(4,6).equals("04")){
				} else if (AcptNo.substring(4,6).equals("03")){
					rst.put("cr_confno", rs.getString("cr_confno"));
				}
				rst.put("cm_jobname",rs.getString("cm_jobname"));
				rst.put("priority", Integer.toString(rs.getInt("cr_priority")));
				rst.put("cr_version",Integer.toString(rs.getInt("cr_version")));
				if (rs.getString("cr_aplydate") != null){
					rst.put("cr_aplydate", rs.getString("cr_aplydate"));
				}
				rst.put("check", "true");
				rst.put("visible", "true");

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
				
				rst.put("changesw", "");
				if("04".equals(rs.getString("qrycd")) && rs.getString("cm_sysinfo").length() > 15 && "1".equals(rs.getString("cm_sysinfo").substring(15, 16))						//16:적용신청건소스변경여부체크
						&& "0".equals(rs.getString("cm_info").substring(9, 10)) && "1".equals(rs.getString("cm_info").substring(11, 12))	//10:바이너리, 12:버전관리
						&& rs.getString("cr_befmd5") != null && !"".equals(rs.getString("cr_befmd5"))
						&& rs.getString("cr_md5sum") != null && !"".equals(rs.getString("cr_md5sum"))) {
					if(rs.getString("cr_befmd5").equals(rs.getString("cr_md5sum"))) {
						rst.put("changesw", "변경없음");
					}
				}	
				
                /*
				if (rs.getString("cr_baseno") != null) {
					strQuery.setLength(0);				
					if (rs.getString("cr_baseno").substring(4,6).equals("01")) { 
						strQuery.append("select nvl(cr_analyn,'Z') cr_analyn        \n");
						strQuery.append("  from cmr1010                             \n");
						strQuery.append(" where cr_acptno=?                         \n");
						strQuery.append("   and cr_itemid=?                         \n");						
					} else {
						strQuery.append("select nvl(b.cr_analyn,'Z') cr_analyn       \n");
						strQuery.append("  from cmr1010 a,cmr1010 b                  \n");
						strQuery.append(" where a.cr_acptno=? and a.cr_itemid=?      \n");
						strQuery.append("   and a.cr_baseno=b.cr_acptno              \n");
					}
					pstmt2 = conn.prepareStatement(strQuery.toString());
					//pstmt2 = new LoggableStatement(conn,strQuery.toString());
					pstmt2.setString(1, rs.getString("cr_baseno"));
					pstmt2.setString(2, rs.getString("cr_itemid"));
					////ecamsLogger.error(((LoggableStatement)pstmt2).getQueryString());
					rs2 = pstmt2.executeQuery();
					if (rs2.next()) {
						if (rs2.getString("cr_analyn").equals("Y")) rst.put("analrst", "통보");
						else if (rs2.getString("cr_analyn").equals("N")) rst.put("analrst", "미통보");
						else if (rs2.getString("cr_analyn").equals("E")) rst.put("analrst", "오류");
						else if (rs2.getString("cr_analyn").equals("X")) rst.put("analrst", "대상없음");
						else rst.put("analrst", "해당무");
					}
					rs2.close();
					pstmt2.close();
				} else rst.put("analrst","해당무");
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
				/*else {
					strQuery.setLength(0);
					strQuery.append("select count(*) as cnt from cmm0044       \n");
					strQuery.append(" where cm_userid=?                        \n");
					strQuery.append("   and cm_syscd=? and cm_jobcd=?          \n");
					pstmt2 = conn.prepareStatement(strQuery.toString());
					pstmt2 = new LoggableStatement(conn,strQuery.toString());
					pstmt2.setString(1, UserId);
					pstmt2.setString(2, rs.getString("cr_syscd"));
					pstmt2.setString(3, rs.getString("cr_jobcd"));
					//ecamsLogger.error(((LoggableStatement)pstmt2).getQueryString());
					rs2 = pstmt2.executeQuery();
					if (rs2.next()) {
						if (rs2.getInt("cnt") > 0) rst.put("secusw", "Y");
						else rst.put("secusw", "N");
					}
					rs2.close();
					pstmt2.close();					
				}
				*/
				if (!rs.getString("cr_status").equals("3") && rs.getString("cr_baseitem") != null) {
					if (rs.getString("basecnt") != null) {
						if (rs.getInt("basecnt")>0) errSw = true;
					}
					/*
					strQuery.setLength(0);
					strQuery.append("select count(*) as cnt from cmr1010 b,cmr1011 a     \n");
					strQuery.append(" where b.cr_acptno=? and b.cr_baseitem=?            \n");
					strQuery.append("   and b.cr_acptno=a.cr_acptno                      \n");
					strQuery.append("   and b.cr_serno=a.cr_serno                        \n");
					strQuery.append("   and nvl(a.cr_prcrst,'0000')<>'0000'             \n");
					pstmt2 = conn.prepareStatement(strQuery.toString());
					pstmt2 = new LoggableStatement(conn,strQuery.toString());
					pstmt2.setString(1, AcptNo);
					pstmt2.setString(2, rs.getString("cr_baseitem"));
					//ecamsLogger.error(((LoggableStatement)pstmt2).getQueryString());
			        rs2 = pstmt2.executeQuery();
			        if (rs2.next()) {
			        	if (rs2.getInt("cnt")>0) errSw = true;
		        	}
			        pstmt2.close();
			        rs2.close();
			        */
				}

				if (rs.getString("cr_editcon") != null){
					rst.put("cr_editcon",rs.getString("cr_editcon"));
				}
				if (!rs.getString("cr_status").equals("3")) {
					if (rs.getString("cr_putcode") != null && !rs.getString("cr_putcode").equals("0000") && !rs.getString("cr_putcode").equals("RTRY")){
						rst.put("ColorSw","5");
					} else if (errSw == true) rst.put("ColorSw","5");
					else{
						if (rs.getString("cr_status").equals("8") || rs.getString("cr_status").equals("9"))
							rst.put("ColorSw","9");
						else rst.put("ColorSw","0");
					}
					if (rs.getString("cr_status").equals("8") || rs.getString("cr_status").equals("9")) {
						rst.put("rst", "Y");
					}
					if (rs.getString("cr_putcode") == null && rs.getString("cr_status").equals("0") && rs.getString("cr_itemid") != null) {
						strQuery.setLength(0);
						if (rs.getString("cr_itemid").equals(rs.getString("cr_baseitem"))) {
							if (rs.getString("baserst") != null) {
								if (rs.getInt("baserst") > 0) rst.put("rst", "Y");
								else rst.put("rst", "N");
							} else {
								rst.put("rst", "N");
							}
						} else {
							if (rs.getString("rst") != null) {
								if (rs.getInt("rst") > 0) rst.put("rst", "Y");
								else rst.put("rst", "N");
							} else {
								rst.put("rst", "N");
							}
						}
					} else rst.put("rst", "Y");
				} else rst.put("ColorSw","3");
				
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
			ecamsLogger.error("## Cmr0250.getFileList() SQLException START ##");
			ecamsLogger.error("## Error DESC : ", sqlexception);
			ecamsLogger.error("## Cmr0250.getFileList() SQLException END ##");
			throw sqlexception;
		} catch (Exception exception) {
			exception.printStackTrace();
			ecamsLogger.error("## Cmr0250.getFileList() Exception START ##");
			ecamsLogger.error("## Error DESC : ", exception);
			ecamsLogger.error("## Cmr0250.getFileList() Exception END ##");
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
					ecamsLogger.error("## Cmr0250.getFileList() connection release exception ##");
					ex3.printStackTrace();
				}
			}
		}
	}//end of getFileList() method statement
	
	public String setQACheck(String Acptno, HashMap<String,String> etcData) throws SQLException, Exception {

		Connection        conn        = null;
		PreparedStatement pstmt       = null;
		PreparedStatement pstmt2      = null;
		ResultSet         rs          = null;
		StringBuffer      strQuery    = new StringBuffer();
		String			  Result      = "No";
		
		ConnectionContext connectionContext = new ConnectionResource();
		
		try {
			conn = connectionContext.getConnection();
			strQuery.setLength(0);
        	strQuery.append("select cr_acptno from cmr0900 where cr_acptno = ? 							\n");
        	pstmt = conn.prepareStatement(strQuery.toString());
        	pstmt.setString(1, Acptno);
        	rs = pstmt.executeQuery();
        	
        	if(rs.next()){
        		strQuery.setLength(0);
            	strQuery.append("update cmr0900 set cr_qacheck = ?,cr_totalcheck=?, cr_chgcode = ?, cr_detailcode = ? 	\n");
            	strQuery.append("where cr_acptno = ?													\n");			
            	//pstmt2 = conn.prepareStatement(strQuery.toString());
            	pstmt2 = new LoggableStatement(conn,strQuery.toString());
            	
            	pstmt2.setString(1, etcData.get("cr_qacheck"));
            	pstmt2.setString(2, etcData.get("cr_totalcheck"));
            	pstmt2.setString(3, etcData.get("cr_chgcode"));
            	pstmt2.setString(4, etcData.get("cr_detailcode"));
            	pstmt2.setString(5, Acptno);
            	ecamsLogger.error(((LoggableStatement)pstmt2).getQueryString());
            	pstmt2.executeUpdate();
            	pstmt2.close();
            	Result = "Yes";
        	}
        	else {
        		strQuery.setLength(0);
        		strQuery.append("insert into cmr0900 																	\n");
	        	strQuery.append("(cr_acptno,cr_qacheck,cr_totalcheck,cr_chgcode, cr_detailcode							\n");
	        	strQuery.append(") values ( 																			\n");
	        	strQuery.append("?,?,?,?,?)										 										\n");
	        	//pstmt2 = conn.prepareStatement(strQuery.toString());
	        	pstmt2 = new LoggableStatement(conn,strQuery.toString());
	        	pstmt2.setString(1, Acptno);
	        	pstmt2.setString(2, etcData.get("cr_qacheck"));
	        	pstmt2.setString(3, etcData.get("cr_totalcheck"));
	        	pstmt2.setString(4, etcData.get("cr_chgcode"));
	        	pstmt2.setString(5, etcData.get("cr_detailcode"));
	        	
	        	ecamsLogger.error(((LoggableStatement)pstmt2).getQueryString());
	        	pstmt2.executeUpdate();
	        	pstmt2.close();
	        	Result = "Yes";
	        }
        	
        	rs.close();
        	pstmt.close();
        	
        	//conn.commit();
        	conn.close();
        	
        	rs = null;
			pstmt = null;
			pstmt2 = null;
			conn = null;
			
			return Result;

		} catch (SQLException sqlexception) {
			sqlexception.printStackTrace();
			ecamsLogger.error("## Cmr0250.getPrjInfo() SQLException START ##");
			ecamsLogger.error("## Error DESC : ", sqlexception);
			ecamsLogger.error("## Cmr0250.getPrjInfo() SQLException END ##");
			throw sqlexception;
		} catch (Exception exception) {
			exception.printStackTrace();
			ecamsLogger.error("## Cmr0250.getPrjInfo() Exception START ##");
			ecamsLogger.error("## Error DESC : ", exception);
			ecamsLogger.error("## Cmr0250.getPrjInfo() Exception END ##");
			throw exception;
		}finally{
			if (strQuery != null) strQuery = null;
			if (rs != null)     try{rs.close();}catch (Exception ex){ex.printStackTrace();}
			if (pstmt != null)  try{pstmt.close();}catch (Exception ex2){ex2.printStackTrace();}
			if (pstmt2 != null)  try{pstmt2.close();}catch (Exception ex2){ex2.printStackTrace();}
			if (conn != null){
				try{
					ConnectionResource.release(conn);
				}catch(Exception ex3){
					ecamsLogger.error("## Cmr0250.getPrjInfo() connection release exception ##");
					ex3.printStackTrace();
				}
			}
		}
	}
	
	public Object[] getQACheckbeforeGyul(String Acptno) throws SQLException, Exception {

		Connection        conn        = null;
		PreparedStatement pstmt       = null;
		ResultSet         rs          = null;
		StringBuffer      strQuery    = new StringBuffer();
		Object[] 		  returnObject = null;
		int				pstmtcount = 1;
		ArrayList<HashMap<String, String>>  rsval = new ArrayList<HashMap<String, String>>();
		HashMap<String, String>			  rst		  = null;
		
		ConnectionContext connectionContext = new ConnectionResource();
		
		try {
			conn = connectionContext.getConnection();
			
			strQuery.setLength(0);
        	strQuery.append("select cr_devptime from cmr1000									 	 \n");
        	strQuery.append("where cr_acptno =?														\n");
        	pstmt = conn.prepareStatement(strQuery.toString());
        	pstmt = new LoggableStatement(conn,strQuery.toString());
        	pstmt.setString(pstmtcount++, Acptno);
        	//ecamsLogger.error(((LoggableStatement)pstmt).getQueryString());
        	rs = pstmt.executeQuery();
			if (rs.next()) {
				rst = new HashMap<String, String>();
				rst.put("cr_devptime", rs.getString("cr_devptime"));
				/* 2022.09.16 빈값대신 초기화 값으로 셋팅
				rst.put("cr_qacheck","   ");
				rst.put("cr_totalcheck","    ");
				*/
				rst.put("cr_qacheck","XXX");
				rst.put("cr_totalcheck","0000");
				rst.put("cr_chgcode","00");
				rst.put("cr_detailcode","00");
				rsval.add(rst);
				rst = null;
			}
			rs.close();
        	pstmt.close();
        	
        	conn.close();

			pstmt = null;
			conn = null;
        	
        	returnObject = rsval.toArray();
			rsval.clear();
			rsval = null;

			return returnObject;

		} catch (SQLException sqlexception) {
			sqlexception.printStackTrace();
			ecamsLogger.error("## Cmr0250.getQACheckbeforeGyul() SQLException START ##");
			ecamsLogger.error("## Error DESC : ", sqlexception);
			ecamsLogger.error("## Cmr0250.getQACheckbeforeGyul() SQLException END ##");
			throw sqlexception;
		} catch (Exception exception) {
			exception.printStackTrace();
			ecamsLogger.error("## Cmr0250.getQACheckbeforeGyul() Exception START ##");
			ecamsLogger.error("## Error DESC : ", exception);
			ecamsLogger.error("## Cmr0250.getQACheckbeforeGyul() Exception END ##");
			throw exception;
		}finally{
			if (strQuery != null) strQuery = null;
			if (rs != null)     try{rs.close();}catch (Exception ex){ex.printStackTrace();}
			if (pstmt != null)  try{pstmt.close();}catch (Exception ex2){ex2.printStackTrace();}
			if (conn != null){
				try{
					ConnectionResource.release(conn);
				}catch(Exception ex3){
					ecamsLogger.error("## Cmr0250.getQACheckbeforeGyul() connection release exception ##");
					ex3.printStackTrace();
				}
			}
		}
	}
	
	public Object[] getQACheck(String Acptno) throws SQLException, Exception {

		Connection        conn        = null;
		PreparedStatement pstmt       = null;
		ResultSet         rs          = null;
		StringBuffer      strQuery    = new StringBuffer();
		Object[] 		  returnObject = null;
		int				pstmtcount = 1;
		ArrayList<HashMap<String, String>>  rsval = new ArrayList<HashMap<String, String>>();
		HashMap<String, String>			  rst		  = null;
		
		ConnectionContext connectionContext = new ConnectionResource();
		
		try {
			conn = connectionContext.getConnection();
			
			strQuery.setLength(0);
        	strQuery.append("select a.cr_devptime 		    \n");
        	strQuery.append("  from cmr1000 a 		  		\n");
        	strQuery.append(" where a.cr_acptno=? 			\n");
        	pstmt = conn.prepareStatement(strQuery.toString());
        	pstmt = new LoggableStatement(conn,strQuery.toString());
        	pstmt.setString(1, Acptno);
        	//ecamsLogger.error(((LoggableStatement)pstmt).getQueryString());
        	rs = pstmt.executeQuery();
			if (rs.next()) {
				rst = new HashMap<String, String>();
				rst.put("cr_devptime", rs.getString("cr_devptime"));
				rst.put("cr_qacheck","XXX");
				rst.put("cr_totalcheck","0000");
				rst.put("cr_chgcode","00");
				rst.put("cr_detailcode","00");
				rsval.add(rst);
			}
			rs.close();
        	pstmt.close();
        	
        	if (rsval.size()==1) {
				strQuery.setLength(0);
	        	strQuery.append("select b.cr_qacheck,b.cr_totalcheck,cr_devptime,cr_totalcheck, \n");
	        	strQuery.append("       b.cr_chgcode,b.cr_detailcode  \n");
	        	strQuery.append("  from cmr0900 b 		  	          \n");
	        	strQuery.append(" where b.cr_acptno=?				  \n");
	        	pstmt = conn.prepareStatement(strQuery.toString());
	        	pstmt = new LoggableStatement(conn,strQuery.toString());
	        	pstmt.setString(1, Acptno);
	        	ecamsLogger.error(((LoggableStatement)pstmt).getQueryString());
	        	rs = pstmt.executeQuery();
				if (rs.next()) {
					rst = new HashMap<String, String>();
					rst = rsval.get(0);
					rst.put("cr_devptime", rs.getString("cr_devptime"));
					rst.put("cr_qacheck",rs.getString("cr_qacheck"));
					rst.put("cr_totalcheck",rs.getString("cr_totalcheck"));
					rst.put("cr_chgcode", rs.getString("cr_chgcode"));
					rst.put("cr_detailcode", rs.getString("cr_detailcode"));
					rsval.set(0, rst);
					rst = null;
				}
				rs.close();
	        	pstmt.close();
        	}
        	conn.close();

			pstmt = null;
			conn = null;
        	
        	returnObject = rsval.toArray();
			rsval.clear();
			rsval = null;

			return returnObject;

		} catch (SQLException sqlexception) {
			sqlexception.printStackTrace();
			ecamsLogger.error("## Cmr0250.getQACheck() SQLException START ##");
			ecamsLogger.error("## Error DESC : ", sqlexception);
			ecamsLogger.error("## Cmr0250.getQACheck() SQLException END ##");
			throw sqlexception;
		} catch (Exception exception) {
			exception.printStackTrace();
			ecamsLogger.error("## Cmr0250.getQACheck() Exception START ##");
			ecamsLogger.error("## Error DESC : ", exception);
			ecamsLogger.error("## Cmr0250.getQACheck() Exception END ##");
			throw exception;
		}finally{
			if (strQuery != null) strQuery = null;
			if (rs != null)     try{rs.close();}catch (Exception ex){ex.printStackTrace();}
			if (pstmt != null)  try{pstmt.close();}catch (Exception ex2){ex2.printStackTrace();}
			if (conn != null){
				try{
					ConnectionResource.release(conn);
				}catch(Exception ex3){
					ecamsLogger.error("## Cmr0250.getQACheck() connection release exception ##");
					ex3.printStackTrace();
				}
			}
		}
	}
	
	public Object[] getDevTime(String Acptno) throws SQLException, Exception {

		Connection        conn        = null;
		PreparedStatement pstmt       = null;
		ResultSet         rs          = null;
		StringBuffer      strQuery    = new StringBuffer();
		Object[] 		  returnObject = null;
		int				pstmtcount = 1;
		ArrayList<HashMap<String, String>>  rsval = new ArrayList<HashMap<String, String>>();
		HashMap<String, String>			  rst		  = null;
		ConnectionContext connectionContext = new ConnectionResource();
		
		try {
			conn = connectionContext.getConnection();
			
			strQuery.setLength(0);
        	strQuery.append("select cr_devptime from cmr1000										 \n");
        	strQuery.append("where cr_acptno =?											       		\n");
        	pstmt = conn.prepareStatement(strQuery.toString());
        	pstmt = new LoggableStatement(conn,strQuery.toString());
        	pstmt.setString(pstmtcount++, Acptno);
        	ecamsLogger.error(((LoggableStatement)pstmt).getQueryString());
        	rs = pstmt.executeQuery();
			if (rs.next()) {
				rst = new HashMap<String, String>();
				rst.put("cr_devptime", rs.getString("cr_devptime"));
				rst.put("cr_qacheck",rs.getString("   "));
				rst.put("cr_totalcheck",rs.getString("    "));
				rsval.add(rst);
				rst = null;
			}
			
			
			rs.close();
        	pstmt.close();
        	
        	conn.close();

			pstmt = null;
			conn = null;
        	
        	returnObject = rsval.toArray();
			rsval.clear();
			rsval = null;

			return returnObject;

		} catch (SQLException sqlexception) {
			sqlexception.printStackTrace();
			ecamsLogger.error("## Cmr0250.getDevTime() SQLException START ##");
			ecamsLogger.error("## Error DESC : ", sqlexception);
			ecamsLogger.error("## Cmr0250.getDevTime() SQLException END ##");
			throw sqlexception;
		} catch (Exception exception) {
			exception.printStackTrace();
			ecamsLogger.error("## Cmr0250.getDevTime() Exception START ##");
			ecamsLogger.error("## Error DESC : ", exception);
			ecamsLogger.error("## Cmr0250.getDevTime() Exception END ##");
			throw exception;
		}finally{
			if (strQuery != null) strQuery = null;
			if (rs != null)     try{rs.close();}catch (Exception ex){ex.printStackTrace();}
			if (pstmt != null)  try{pstmt.close();}catch (Exception ex2){ex2.printStackTrace();}
			if (conn != null){
				try{
					ConnectionResource.release(conn);
				}catch(Exception ex3){
					ecamsLogger.error("## Cmr0250.getDevTime() connection release exception ##");
					ex3.printStackTrace();
				}
			}
		}
	}

	public String setDevTime(String Acptno, String time) throws SQLException, Exception {

		Connection        conn        = null;
		PreparedStatement pstmt       = null;
		ResultSet         rs          = null;
		StringBuffer      strQuery    = new StringBuffer();
		String			  Result		="No";
		
		
		ConnectionContext connectionContext = new ConnectionResource();
		
		try {
			conn = connectionContext.getConnection();
			
			strQuery.setLength(0);
        	strQuery.append("select cr_devptime from cmr1000 where cr_acptno = ?							\n");
        	pstmt = conn.prepareStatement(strQuery.toString());
        	
        	pstmt.setString(1, Acptno);
        	
        	rs = pstmt.executeQuery();
			
				
        	if(rs.next())
        	{
        		pstmt.close();
        		strQuery.setLength(0);
        		strQuery.append("update cmr1000 set cr_devptime = ? where cr_acptno = ? 					\n");
        		pstmt = conn.prepareStatement(strQuery.toString());
        		pstmt.setString(1, time);
        		pstmt.setString(2, Acptno);
        		pstmt.executeUpdate();
        		Result = "Yes";
        	}
        	
        	rs.close();
        	pstmt.close();

        	conn.close();

			pstmt = null;
			conn = null;
			
			return Result;


		} catch (SQLException sqlexception) {
			sqlexception.printStackTrace();
			ecamsLogger.error("## Cmr0250.setDevTime() SQLException START ##");
			ecamsLogger.error("## Error DESC : ", sqlexception);
			ecamsLogger.error("## Cmr0250.setDevTime() SQLException END ##");
			throw sqlexception;
		} catch (Exception exception) {
			exception.printStackTrace();
			ecamsLogger.error("## Cmr0250.setDevTime() Exception START ##");
			ecamsLogger.error("## Error DESC : ", exception);
			ecamsLogger.error("## Cmr0250.setDevTime() Exception END ##");
			throw exception;
		}finally{
			if (strQuery != null) strQuery = null;
			if (rs != null)     try{rs.close();}catch (Exception ex){ex.printStackTrace();}
			if (pstmt != null)  try{pstmt.close();}catch (Exception ex2){ex2.printStackTrace();}
			if (conn != null){
				try{
					ConnectionResource.release(conn);
				}catch(Exception ex3){
					ecamsLogger.error("## Cmr0250.setDevTime() connection release exception ##");
					ex3.printStackTrace();
				}
			}
		}
	}
	
	public Object[] getPoc(String AcptNo, String UserId) throws SQLException, Exception {
		boolean 		  isQA 		  = false;
		Connection        conn        = null;
		PreparedStatement pstmt       = null;
		ResultSet         rs          = null;
		StringBuffer      strQuery    = new StringBuffer();
		String			  Result	  = "";
		ArrayList<HashMap<String, String>>  rsval = new ArrayList<HashMap<String, String>>();
		HashMap<String, String>			  rst		  = null;
		Object[] 		  returnObject = null;
		

		ConnectionContext connectionContext = new ConnectionResource();
		
		try {
			conn = connectionContext.getConnection();
			
			strQuery.setLength(0);
			strQuery.append("select count(*) cnt from cmm0043    \n");	
			strQuery.append(" where cm_userid=? and cm_rgtcd='Q1'\n");						
			pstmt = conn.prepareStatement(strQuery.toString());
			pstmt.setString(1, UserId);
			rs = pstmt.executeQuery();
			if(rs.next()){
				if(rs.getInt("cnt")>0) isQA = true;
			}
			rs.close();
			pstmt.close();
			
			strQuery.setLength(0);
			strQuery.append("select a.cr_locat,a.cr_team,a.cr_status,a.cr_teamcd,  \n");
			strQuery.append("       b.cr_editor,b.cr_status as stat,               \n");
			strQuery.append("	     a.cr_confname,a.cr_sgngbn 		    	       \n");
			strQuery.append("  from cmr9900 a,cmr1000 b					    	   \n");
			strQuery.append(" where a.cr_acptno=? and b.cr_acptno = a.cr_acptno    \n");
			strQuery.append("  order by cr_locat            					   \n");
			pstmt = conn.prepareStatement(strQuery.toString());
			//pstmt = new LoggableStatement(conn,strQuery.toString());
			pstmt.setString(1,AcptNo);
	       // //ecamsLogger.error(((LoggableStatement)pstmt).getQueryString());
	        rs = pstmt.executeQuery();
			while (rs.next()){
				rst = new HashMap<String,String>();
				rst.put("cr_locat", rs.getString("cr_locat"));
				rst.put("cr_team", rs.getString("cr_team"));
				rst.put("cr_status", rs.getString("cr_status"));
				rst.put("cr_temacd", rs.getString("cr_teamcd"));
				rst.put("cr_editor", rs.getString("cr_editor"));
				rst.put("cr_confname", rs.getString("cr_confname"));
				rst.put("cr_sgngbn", rs.getString("cr_sgngbn"));
				rst.put("status", rs.getString("stat"));
				if(isQA)
					rst.put("isQA", "true");
				else
					rst.put("isQA", "false");
				//if(rs.getString("cr_team").equals("Q1") && rs.getString("cr_status").equals("0")){
					//Result = "YES";
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
			ecamsLogger.error("## Cmr0250.getPrjInfo() SQLException START ##");
			ecamsLogger.error("## Error DESC : ", sqlexception);
			ecamsLogger.error("## Cmr0250.getPrjInfo() SQLException END ##");
			throw sqlexception;
		} catch (Exception exception) {
			exception.printStackTrace();
			ecamsLogger.error("## Cmr0250.getPrjInfo() Exception START ##");
			ecamsLogger.error("## Error DESC : ", exception);
			ecamsLogger.error("## Cmr0250.getPrjInfo() Exception END ##");
			throw exception;
		}finally{
			if (strQuery != null) strQuery = null;
			if (rs != null)     try{rs.close();}catch (Exception ex){ex.printStackTrace();}
			if (pstmt != null)  try{pstmt.close();}catch (Exception ex2){ex2.printStackTrace();}
			if (conn != null){
				try{
					ConnectionResource.release(conn);
				}catch(Exception ex3){
					ecamsLogger.error("## Cmr0250.getPrjInfo() connection release exception ##");
					ex3.printStackTrace();
				}
			}
		}
	}//end of getPrjInfo() method statement
	
	public Object[] getPrjInfo(String AcptNo) throws SQLException, Exception {

		Connection        conn        = null;
		PreparedStatement pstmt       = null;
		ResultSet         rs          = null;
		StringBuffer      strQuery    = new StringBuffer();
		ArrayList<HashMap<String, String>>  rsval  = new ArrayList<HashMap<String, String>>();
		HashMap<String, String>			  rst		  = null;
		Object[] 		  returnObject = null;
		
		ConnectionContext connectionContext = new ConnectionResource();
		
		try {
			conn = connectionContext.getConnection();
			
			strQuery.setLength(0);
			strQuery.append("select b.cd_chgcd,d.cm_codename,b.cd_reqdept,b.cd_chgdept,c.cr_reqtit,\n");
			strQuery.append("       b.cd_chgdet,c.cr_reqmsg,c.cr_requsr,c.cr_reqmeth,c.cr_reqdet,\n");
			strQuery.append("       b.cd_pjid,b.cd_devno,c.cr_reqdate                            \n");
			strQuery.append("  from cmm0020 d,cmr1020 c,cmd0110 b,cmr1000 a                      \n");
			strQuery.append(" where a.cr_acptno=?                                                \n");
			strQuery.append("   and a.cr_prjno=b.cd_pjid and a.cr_devno=b.cd_devno               \n");
			strQuery.append("   and a.cr_acptno=c.cr_acptno                                      \n");
			strQuery.append("   and d.cm_macode='REQSAYU' and d.cm_micode=b.cd_chgsayu           \n");
	        
			pstmt = conn.prepareStatement(strQuery.toString());
			//pstmt = new LoggableStatement(conn,strQuery.toString());
			pstmt.setString(1,AcptNo);
	       // //ecamsLogger.error(((LoggableStatement)pstmt).getQueryString());
	        rs = pstmt.executeQuery();

	        rsval.clear();

			if (rs.next()){
				rst = new HashMap<String, String>();

				rst.put("cd_pjid", rs.getString("cd_pjid"));
				rst.put("cd_devno", rs.getString("cd_devno"));
				rst.put("cd_reqdept", rs.getString("cd_reqdept"));
				rst.put("cd_chgdept", rs.getString("cd_chgdept"));
	        	if (rs.getString("cd_chgcd").equals("1") || rs.getString("cd_chgcd").equals("2")){
	        		rst.put("cd_chgcd", "정상변경");
	        	} else if(rs.getString("cd_chgcd").equals("3")){
	        		rst.put("cd_chgcd", "단순변경");
	        	} else if(rs.getString("cd_chgcd").equals("4")){
	        		rst.put("cd_chgcd", "비상변경");
	        	}
	        	rst.put("cd_chgtit", rs.getString("cr_reqtit"));
	        	rst.put("cd_chgdet", rs.getString("cr_reqmsg"));
	        	rst.put("cd_chgsayu", rs.getString("cm_codename"));
	        	rst.put("cr_requser", rs.getString("cr_requsr"));
	        	rst.put("cr_reqmeth", rs.getString("cr_reqmeth"));
	        	rst.put("cr_reqdet", rs.getString("cr_reqdet"));
	        	rst.put("cr_reqdate", rs.getString("cr_reqdate"));
				
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
			ecamsLogger.error("## Cmr0250.getPrjInfo() SQLException START ##");
			ecamsLogger.error("## Error DESC : ", sqlexception);
			ecamsLogger.error("## Cmr0250.getPrjInfo() SQLException END ##");
			throw sqlexception;
		} catch (Exception exception) {
			exception.printStackTrace();
			ecamsLogger.error("## Cmr0250.getPrjInfo() Exception START ##");
			ecamsLogger.error("## Error DESC : ", exception);
			ecamsLogger.error("## Cmr0250.getPrjInfo() Exception END ##");
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
					ecamsLogger.error("## Cmr0250.getPrjInfo() connection release exception ##");
					ex3.printStackTrace();
				}
			}
		}
	}//end of getPrjInfo() method statement

	public Object[] getRstList(String UserId,String AcptNo,String prcSys) throws SQLException, Exception {
		Connection        conn        = null;
		PreparedStatement pstmt       = null;
		ResultSet         rs          = null;
		StringBuffer      strQuery    = new StringBuffer();
		Object[] 		  returnObject = null;
		
		ArrayList<HashMap<String, String>>  rsval = new ArrayList<HashMap<String, String>>();
		HashMap<String, String>			  rst		  = null;
		ConnectionContext connectionContext = new ConnectionResource();

		try {
			
			conn = connectionContext.getConnection();
			
			int			parmCnt	 = 0;
			UserInfo	userinfo = new UserInfo();
			boolean 	adminYn  = userinfo.isAdmin_conn(UserId, conn);
			userinfo = null;
			boolean    comSw = false;
						
			strQuery.setLength(0);
			strQuery.append("select a.cr_acptno,a.cr_syscd,a.cr_serno,a.cr_rsrcname,                 \n");
			strQuery.append("       a.cr_rsrccd,a.cr_aplydate,a.cr_sysstep,b.cr_ipaddr,b.cr_seqno,   \n");
			strQuery.append("       to_char(b.cr_prcdate,'yyyy/mm/dd hh24:mi') prcdate,b.cr_svrseq,  \n");
			strQuery.append("       b.cr_prcrst,b.cr_prcdate,b.cr_prcsys,b.cr_putcode,b.cr_rstfile,  \n");
			strQuery.append("       b.cr_svrcd,c.cm_codename jawon,b.cr_svrname,b.cr_portno,         \n");
			strQuery.append("       b.cr_wait,d.cm_dirpath,e.cm_codename SYSGBN,f.cm_info            \n");
			strQuery.append("  from cmm0036 f,cmm0020 c,cmm0020 e,cmm0070 d, cmr1011 b,cmr1010 a     \n");
			strQuery.append(" where a.cr_acptno=?                                                    \n");

			strQuery.append("   and a.cr_acptno=b.cr_acptno and a.cr_serno=b.cr_serno                \n");
			strQuery.append("   and (nvl(b.cr_putcode,'NONE')<>'0000' or b.cr_rstfile is not null)   \n");
			if (!prcSys.equals("") && prcSys != null) {
				if (prcSys.equals("SYSCB")) strQuery.append(" and b.cr_prcsys in ('SYSCB','SYSGB')   \n");
				if (prcSys.equals("SYSAC")) strQuery.append(" and b.cr_prcsys in ('SYSAC','SYSAG')   \n");
				else strQuery.append("and b.cr_prcsys=?                                              \n");
			}
			if (!adminYn && AcptNo.substring(4,6).equals("04")){
				strQuery.append(" and b.cr_svrcd<>'90'                             					 \n");
			}
			strQuery.append("   and a.cr_syscd=d.cm_syscd and a.cr_dsncd=d.cm_dsncd                  \n");
			strQuery.append("   and c.cm_macode='JAWON' and c.cm_micode=a.cr_rsrccd                  \n");
			strQuery.append("   and e.cm_macode='SYSGBN' and e.cm_micode=b.cr_prcsys                 \n");
			strQuery.append("   and a.cr_syscd=f.cm_syscd and a.cr_rsrccd=f.cm_rsrccd                \n");
			strQuery.append("union                                                                   \n");
			strQuery.append("select a.cr_acptno,a.cr_syscd,b.cr_serno,'파일전송결과' cr_rsrcname,       \n");
			strQuery.append("       '00' cr_rsrccd,'' cr_aplydate,0 cr_sysstep,b.cr_ipaddr,b.cr_seqno, \n");
			strQuery.append("       to_char(b.cr_prcdate,'yyyy/mm/dd hh24:mi') prcdate,b.cr_svrseq,  \n");
			strQuery.append("       b.cr_prcrst,b.cr_prcdate,b.cr_prcsys,b.cr_putcode,               \n");
			strQuery.append("       b.cr_rstfile,b.cr_svrcd,'' jawon,b.cr_svrname,b.cr_portno,       \n");
			strQuery.append("       b.cr_wait,'' cm_dirpath,e.cm_codename SYSGBN,'' cm_info          \n");
			strQuery.append("  from cmm0020 e,cmr1011 b,cmr1000 a                                    \n");
			strQuery.append(" where a.cr_acptno=?                                                    \n");
			strQuery.append("   and a.cr_acptno=b.cr_acptno and b.cr_serno=0                         \n");
			strQuery.append("   and (nvl(b.cr_putcode,'NONE')<>'0000' or b.cr_rstfile is not null)   \n");
			if (!prcSys.equals("") && prcSys != null) {
				if (prcSys.equals("SYSCB")) strQuery.append(" and b.cr_prcsys in ('SYSCB','SYSGB')   \n");
				if (prcSys.equals("SYSAC")) strQuery.append(" and b.cr_prcsys in ('SYSAC','SYSAG')   \n");
				else strQuery.append("and b.cr_prcsys=?                                              \n");
			}
			if (!adminYn){
				strQuery.append(" and b.cr_svrcd<>'90'                             					 \n");
			}
			strQuery.append("   and e.cm_macode='SYSGBN' and e.cm_micode=b.cr_prcsys                 \n");
			strQuery.append(" order by cr_prcdate,cr_prcsys,cr_seqno,cr_ipaddr                       	 \n");

			pstmt = conn.prepareStatement(strQuery.toString());
			//pstmt = new LoggableStatement(conn,strQuery.toString());
			pstmt.setString(++parmCnt, AcptNo);
			if (!prcSys.equals("") && prcSys != null){
				if (!prcSys.equals("SYSCB") && !prcSys.equals("SYSAC")) pstmt.setString(++parmCnt, prcSys);
			}
			pstmt.setString(++parmCnt, AcptNo);
			if (!prcSys.equals("") && prcSys != null){
				if (!prcSys.equals("SYSCB") && !prcSys.equals("SYSAC")) pstmt.setString(++parmCnt, prcSys);
			}
	        ////ecamsLogger.error(((LoggableStatement)pstmt).getQueryString());
	        rs = pstmt.executeQuery();

	        rsval.clear();

			while (rs.next()){
				rst = new HashMap<String, String>();
				//rst.put("ID", Integer.toString(rs.getRow()));
				rst.put("prcsys",rs.getString("SYSGBN"));
				rst.put("cr_rsrcname",rs.getString("cr_rsrcname"));
				rst.put("cr_rsrccd",rs.getString("cr_rsrccd"));
				rst.put("jawon",rs.getString("jawon"));
				rst.put("cr_seqno",rs.getString("cr_seqno"));
				rst.put("cr_acptno", rs.getString("cr_acptno"));
				rst.put("cr_prcsys", rs.getString("cr_prcsys"));
				if (!rs.getString("cr_rsrccd").equals("00")) {
					rst.put("cm_dirpath", chgVolPath(rs.getString("cr_syscd"),rs.getString("cm_dirpath"),rs.getString("cr_svrcd"),rs.getString("cr_rsrccd"),rs.getInt("cr_svrseq"),conn));
				}
				rst.put("cr_svrname",rs.getString("cr_svrname"));
				if (rs.getString("cr_putcode") != null) {
					PreparedStatement pstmt2       = null;
					ResultSet         rs2         = null;
					
					strQuery.setLength(0);
					strQuery.append("select cm_codename from cmm0020             \n");
					strQuery.append(" where cm_macode='ERRACCT' and cm_micode=?  \n");
					pstmt2 = conn.prepareStatement(strQuery.toString());
					pstmt2.setString(1,rs.getString("cr_putcode"));
					rs2 = pstmt2.executeQuery();
					if (rs2.next()){
						rst.put("prcrst", rs2.getString("cm_codename"));
					}
					else{
						rst.put("prcrst", rs.getString("cr_putcode"));
					}
					rs2.close();
					pstmt2.close();
					rs2 = null;
					pstmt2 = null;
				}

				if (rs.getString("cr_svrname") != null){
					rst.put("cr_svrname", rs.getString("cr_svrname"));
				}
				if (rs.getString("cr_prcdate") != null){
					rst.put("cr_prcdate", rs.getString("cr_prcdate"));
				}
				if (rs.getString("prcdate") != null){
					rst.put("prcdate", rs.getString("prcdate"));
				}
				rst.put("cr_serno",Integer.toString(rs.getInt("cr_serno")));
				if (rs.getString("cr_putcode") != null){
					rst.put("cr_putcode",rs.getString("cr_putcode"));
				}
				if (rs.getString("cr_prcdate") != null) {
					if (rs.getString("cr_prcrst") != null && !rs.getString("cr_prcrst").equals("0000")){
						rst.put("ColorSw","5");
					} else rst.put("ColorSw","9");
				}
				else{
					if (rs.getString("cr_prcrst") != null && !rs.getString("cr_prcrst").equals("0000")){
						rst.put("ColorSw","5");
					} else rst.put("ColorSw","0");
				}
				
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
			ecamsLogger.error("## Cmr0250.getRstList() SQLException START ##");
			ecamsLogger.error("## Error DESC : ", sqlexception);
			ecamsLogger.error("## Cmr0250.getRstList() SQLException END ##");
			throw sqlexception;
		} catch (Exception exception) {
			exception.printStackTrace();
			ecamsLogger.error("## Cmr0250.getRstList() Exception START ##");
			ecamsLogger.error("## Error DESC : ", exception);
			ecamsLogger.error("## Cmr0250.getRstList() Exception END ##");
			throw exception;
		}finally{
			if (strQuery != null) strQuery = null;
			if (returnObject !=  null)	returnObject = null;
			if (rs != null)     try{rs.close();}catch (Exception ex){ex.printStackTrace();}
			if (pstmt != null)  try{pstmt.close();}catch (Exception ex2){ex2.printStackTrace();}
			if (conn != null){
				try{
					conn.close();conn=null;
				}catch(Exception ex3){
					ecamsLogger.error("## Cmr0250.getRstList() connection release exception ##");
					ex3.printStackTrace();
				}
			}
		}
	}//end of getFileList() method statement

	public String svrProc(String AcptNo,String prcCd,String prcSys,String UserId) throws SQLException, Exception {
		Connection        conn        = null;
		PreparedStatement pstmt       = null;
		ResultSet         rs          = null;
		StringBuffer      strQuery    = new StringBuffer();
		String            strErMsg    = "";
		String            wkPrcSys    = "";
		boolean           endSw       = false;
		Runtime  run = null;
		Process p = null;
		SystemPath		  systemPath = new SystemPath();
		
		ConnectionContext connectionContext = new ConnectionResource();
		
		try {
			//ecamsLogger.debug("+++++++++prcCd========"+prcCd);
			String  binpath;
			String[] chkAry;
			int	cmdrtn=0;
			
			binpath = systemPath.getTmpDir("14");

			run = Runtime.getRuntime();

			chkAry = new String[4];
			chkAry[0] = "/bin/sh";
//			chkAry[1] = "procck2";
			chkAry[1] = binpath+"/procck2"; // 20221219
			chkAry[2] = "ecams_acct";
			chkAry[3] = AcptNo;


			p = run.exec(chkAry);
			p.waitFor();
			
			
			cmdrtn = p.exitValue();
		
			if (p.exitValue() != 0) {
				//ecamsLogger.error(chkAry[0]+" " + chkAry[1] + " " + chkAry[2]);
				//ecamsLogger.error(cmdrtn);
				return "2";
			}

			//ecamsLogger.error("++++++++++ error retry AcptNo="+AcptNo+", PrcSys="+prcCd+", UserId="+UserId);
			conn = connectionContext.getConnection();
			conn.setAutoCommit(false);
			
			if (prcSys.equals("SYSCB")) wkPrcSys = prcSys + ",SYSGB";
			else wkPrcSys = prcSys;
			
			if (prcCd.equals("Retry") || prcCd.equals("Sttry")) {
				if (prcCd.equals("Retry")) {
					strQuery.setLength(0);
					strQuery.append("update cmr1010 set cr_putcode=''                        \n");
					strQuery.append(" where cr_acptno=? and cr_prcdate is null               \n");
					pstmt = conn.prepareStatement(strQuery.toString());
					//pstmt = new LoggableStatement(conn,strQuery.toString());
					pstmt.setString(1, AcptNo);
					pstmt.executeUpdate();
				} 
				
				strQuery.setLength(0);
		        strQuery.append("select count(*) as cnt                                      \n");
		        strQuery.append("  from cmr1010 a,cmm0036 b                                  \n");
		        strQuery.append(" where a.cr_acptno=? and a.cr_prcdate is null               \n");
		        strQuery.append("   and nvl(a.cr_putcode,'NONE')<>'0000'                     \n");
		        strQuery.append("   and a.cr_syscd=b.cm_syscd and a.cr_rsrccd=b.cm_rsrccd    \n");
		        if (prcSys.equals("SYSPF")) {
		        	strQuery.append("and (substr(b.cm_info,4,1)='1' or                       \n");
		        	strQuery.append("     substr(b.cm_info,9,1)='1' or                       \n");
		        	strQuery.append("     substr(b.cm_info,27,1)='1')                        \n");
		        }else if (prcSys.equals("SYSUP")) {
		        	strQuery.append("and (substr(b.cm_info,22,1)='1' or                      \n");
		        	strQuery.append("     substr(b.cm_info,24,1)='1')                        \n");
		        }else if (prcSys.equals("SYSCB")) {
		        	strQuery.append("and (substr(b.cm_info,1,1)='1' or                       \n");
		        	strQuery.append("     substr(b.cm_info,13,1)='1' or                      \n");
		        	strQuery.append("     substr(b.cm_info,25,1)='1')                        \n");
		        }else if (prcSys.equals("SYSCF")) {
		        	strQuery.append("and (substr(b.cm_info,45,1)='1' or                      \n");
		        	strQuery.append("     substr(b.cm_info,46,1)='1')                        \n");
		        }else if (prcSys.equals("SYSAC")) {
		        	strQuery.append("and substr(b.cm_info,44,1)='1'                          \n");
		        }else if (prcSys.equals("SYSED")) {
		        	strQuery.append("and (substr(b.cm_info,11,1)='1' or                      \n");
		        	strQuery.append("     substr(b.cm_info,21,1)='1')                        \n");
		        }else if (prcSys.equals("SYSRC")) {
		        	strQuery.append("and (substr(b.cm_info,35,1)='1')                        \n");
		        }else if (prcSys.equals("SYSAR")) {
		        	strQuery.append("and (substr(b.cm_info,40,1)='1')                        \n");
		        }
		        strQuery.append("union all                                                   \n");
		        strQuery.append("select count(*) as cnt                                      \n");
		        strQuery.append("  from cmr1011 a,cmr1010 b                                  \n");
		        strQuery.append(" where b.cr_acptno=? and b.cr_prcdate is null               \n");
		        strQuery.append("   and b.cr_acptno=a.cr_acptno                              \n");
		        strQuery.append("   and b.cr_serno=a.cr_serno                                \n");
		        strQuery.append("   and instr(?,a.cr_prcsys)>0                               \n");
		        strQuery.append("   and nvl(a.cr_putcode,'NONE')<>'0000'                     \n");
		        
		        //pstmt = conn.prepareStatement(strQuery.toString());
		        pstmt = new LoggableStatement(conn,strQuery.toString());
				pstmt.setString(1, AcptNo);
				pstmt.setString(2, AcptNo);
				pstmt.setString(3, wkPrcSys);
				ecamsLogger.error(((LoggableStatement)pstmt).getQueryString());
				rs = pstmt.executeQuery();
				endSw = true;
				while (rs.next()) {
					if (rs.getInt("cnt") > 0) {
						endSw = false;
						break;
					}
				}
				rs.close();
				pstmt.close();
			}
			/*ecamsLogger.error(" prcCd : " + prcCd + ", endSw : " + endSw);
			if (endSw == true) {
				
				Cmr3100 cmr3100 = new Cmr3100();
				strErMsg = cmr3100.nextConf(AcptNo,prcSys, "단계완료처리", "1", UserId);
				cmr3100 = null;
				if (strErMsg != "" && strErMsg != null) {
					conn.rollback();
					return strErMsg;
				}
				
				strQuery.setLength(0);
				strQuery.append("update cmr1010 set cr_pid='',cr_srccmp='Y',cr_putcode=''     \n");
	            strQuery.append(" where cr_acptno=? and cr_status='0'                         \n");
	            strQuery.append("   and cr_serno in (select cr_serno from cmr1011             \n");
	            strQuery.append("                         where cr_acptno=?                   \n");
	            strQuery.append("                           and instr(?,cr_prcsys)>0          \n");
	            strQuery.append("                           and (nvl(cr_prcrst,'0000')='0000' \n");
	            strQuery.append("                           or   nvl(cr_prcrst,'RTRY')='RTRY')) \n");
	            pstmt = conn.prepareStatement(strQuery.toString());
	            pstmt = new LoggableStatement(conn,strQuery.toString());
	            pstmt.setString(1,AcptNo);
	            pstmt.setString(2,AcptNo);
	            pstmt.setString(3,wkPrcSys);
	            //ecamsLogger.error(((LoggableStatement)pstmt).getQueryString());
	            pstmt.executeUpdate();
		        pstmt.close();
			}*/
			if (endSw == true) {
				Cmr3100 cmr3100 = new Cmr3100();
				strErMsg = cmr3100.nextConf(AcptNo,prcSys, "단계완료처리", "1", UserId);
				cmr3100 = null;
				if (strErMsg != "" && strErMsg != null) {
					conn.rollback();
					return strErMsg;
				}
			}
			if (prcCd.equals("Retry") && endSw == false) {  //전체재처리
				strQuery.setLength(0);
				strQuery.append("delete cmr1011                                          \n");
				strQuery.append(" where cr_acptno=?                                      \n");
				strQuery.append("   and (cr_serno in (select cr_serno from cmr1010       \n");
				strQuery.append("                     where cr_acptno=?                  \n");
				strQuery.append("                       and cr_prcdate is null           \n");
				strQuery.append("                       and cr_status='0')               \n");
				strQuery.append("                 or cr_serno=0)                         \n");
				strQuery.append("   and instr(?,cr_prcsys)>0                             \n");
				pstmt = conn.prepareStatement(strQuery.toString());
		//		pstmt = new LoggableStatement(conn,strQuery.toString());
				pstmt.setString(1, AcptNo);
				pstmt.setString(2,AcptNo);
				pstmt.setString(3,wkPrcSys);
		  //      //ecamsLogger.error(((LoggableStatement)pstmt).getQueryString());
		        pstmt.executeUpdate();
		        pstmt.close();
		        		        
				strQuery.setLength(0);
				strQuery.append("update cmr1010                                              \n");
				strQuery.append("   set cr_putcode='',cr_pid='',cr_srccmp='Y',cr_sysstep=0   \n");
				strQuery.append(" where cr_acptno=? and cr_prcdate is null                   \n");
				strQuery.append("   and cr_status='0'                                        \n");
		        pstmt = conn.prepareStatement(strQuery.toString());
				pstmt = new LoggableStatement(conn,strQuery.toString());
				pstmt.setString(1,AcptNo);
		        ecamsLogger.error(((LoggableStatement)pstmt).getQueryString());
		        pstmt.executeUpdate();
		        pstmt.close();
			} else if (prcCd.equals("Sttry") && endSw == false) {   //다음단계진행
				strQuery.setLength(0);
				strQuery.append("update cmr1010 set cr_pid='',cr_srccmp='Y',cr_putcode=''     \n");
	            strQuery.append(" where cr_acptno=? and cr_status='0'                         \n");
	            if (prcSys.equals("SYSPDN") || prcSys.equals("SYSPUP")) {
	            	strQuery.append("and nvl(cr_putcode,'0000')<>'0000'                       \n");
	            } else {
		            strQuery.append("   and cr_serno not in (select cr_serno from cmr1011         \n");
		            strQuery.append("                         where cr_acptno=?                   \n");
		            strQuery.append("                           and instr(?,cr_prcsys)>0          \n");
		            strQuery.append("                           and nvl(cr_prcrst,'0000')<>'0000' \n");
		            strQuery.append("                           and nvl(cr_prcrst,'RTRY')<>'RTRY') \n");
	            }
	            pstmt = conn.prepareStatement(strQuery.toString());
	            pstmt.setString(1,AcptNo);
	            pstmt.setString(2,AcptNo);
	            pstmt.setString(3,wkPrcSys);
	            pstmt.executeUpdate();
		        pstmt.close();
			} else if (endSw == false) {                           //다음단계진행
				strQuery.setLength(0);
				strQuery.append("update cmr1010 set cr_pid='',cr_putcode=''                   \n");
				if (prcCd.equals("Errtry")) strQuery.append(",cr_srccmp='Y'                   \n");
	            strQuery.append(" where cr_acptno=? and cr_status='0'                         \n");
	            strQuery.append("   and cr_serno not in (select cr_serno from cmr1011         \n");
	            strQuery.append("                         where cr_acptno=?                   \n");
	            strQuery.append("                           and instr(?,cr_prcsys)>0          \n");
	            strQuery.append("                           and (nvl(cr_prcrst,'0000')='0000' \n");
	            strQuery.append("                           or   nvl(cr_prcrst,'RTRY')='RTRY')) \n");
	            pstmt = conn.prepareStatement(strQuery.toString());
	            pstmt.setString(1,AcptNo);
	            pstmt.setString(2,AcptNo);
	            pstmt.setString(3,wkPrcSys);
	            pstmt.executeUpdate();
		        pstmt.close();
		        
		        if (prcSys.equals("SYSCB")) {
					strQuery.setLength(0);
					strQuery.append("delete cmr1011                                             \n");
					strQuery.append(" where cr_acptno=?                                         \n");
					strQuery.append("   and cr_serno in (select cr_serno from cmr1010           \n");
					strQuery.append("                     where cr_acptno=?                     \n");
					strQuery.append("                       and cr_prcdate is null              \n");
					strQuery.append("                       and cr_sysstep<4                    \n");
					strQuery.append("                       and cr_status='0')                  \n");
					strQuery.append("   and cr_prcsys='SYSCB' and nvl(cr_putcode,'NONE')<>'0000' \n");
			        pstmt = conn.prepareStatement(strQuery.toString());
					//pstmt = new LoggableStatement(conn,strQuery.toString());
					pstmt.setString(1, AcptNo);
					pstmt.setString(2,AcptNo);
			        ////ecamsLogger.error(((LoggableStatement)pstmt).getQueryString());
			        pstmt.executeUpdate();
			        pstmt.close();
			        
					strQuery.setLength(0);
					strQuery.append("delete cmr1011                                             \n");
					strQuery.append(" where cr_acptno=?                                         \n");
					strQuery.append("   and cr_serno in (select cr_serno from cmr1010           \n");
					strQuery.append("                     where cr_acptno=?                     \n");
					strQuery.append("                       and cr_prcdate is null              \n");
					strQuery.append("                       and cr_sysstep>3                    \n");
					strQuery.append("                       and cr_status='0')                  \n");
					strQuery.append("   and cr_prcsys='SYSGB' and nvl(cr_putcode,'NONE')<>'0000' \n");
			        pstmt = conn.prepareStatement(strQuery.toString());
					//pstmt = new LoggableStatement(conn,strQuery.toString());
					pstmt.setString(1, AcptNo);
					pstmt.setString(2,AcptNo);
			        ////ecamsLogger.error(((LoggableStatement)pstmt).getQueryString());
			        pstmt.executeUpdate();
			        pstmt.close();
		        } else if (prcSys.equals("SYSAC")) {
					strQuery.setLength(0);
					strQuery.append("delete cmr1011                                             \n");
					strQuery.append(" where cr_acptno=?                                         \n");
					strQuery.append("   and cr_serno in (select cr_serno from cmr1010           \n");
					strQuery.append("                     where cr_acptno=?                     \n");
					strQuery.append("                       and cr_prcdate is null              \n");
					strQuery.append("                       and cr_sysstep<3                    \n");
					strQuery.append("                       and cr_status='0')                  \n");
					strQuery.append("   and cr_prcsys='SYSAC' and nvl(cr_putcode,'NONE')<>'0000' \n");
			        pstmt = conn.prepareStatement(strQuery.toString());
					//pstmt = new LoggableStatement(conn,strQuery.toString());
					pstmt.setString(1, AcptNo);
					pstmt.setString(2,AcptNo);
			        ////ecamsLogger.error(((LoggableStatement)pstmt).getQueryString());
			        pstmt.executeUpdate();
			        pstmt.close();
			        
					strQuery.setLength(0);
					strQuery.append("delete cmr1011                                             \n");
					strQuery.append(" where cr_acptno=?                                         \n");
					strQuery.append("   and cr_serno in (select cr_serno from cmr1010           \n");
					strQuery.append("                     where cr_acptno=?                     \n");
					strQuery.append("                       and cr_prcdate is null              \n");
					strQuery.append("                       and cr_sysstep>2                    \n");
					strQuery.append("                       and cr_status='0')                  \n");
					strQuery.append("   and cr_prcsys='SYSAG' and nvl(cr_putcode,'NONE')<>'0000' \n");
			        pstmt = conn.prepareStatement(strQuery.toString());
					//pstmt = new LoggableStatement(conn,strQuery.toString());
					pstmt.setString(1, AcptNo);
					pstmt.setString(2,AcptNo);
			        ////ecamsLogger.error(((LoggableStatement)pstmt).getQueryString());
			        pstmt.executeUpdate();
			        pstmt.close();
		        } else {
					strQuery.setLength(0);
					strQuery.append("delete cmr1011                                             \n");
					strQuery.append(" where cr_acptno=?                                         \n");
					strQuery.append("   and cr_serno in (select cr_serno from cmr1010           \n");
					strQuery.append("                     where cr_acptno=?                     \n");
					strQuery.append("                       and cr_prcdate is null              \n");
					strQuery.append("                       and cr_status='0')                  \n");
					strQuery.append("   and cr_prcsys=? and nvl(cr_putcode,'NONE')<>'0000'      \n");
			        pstmt = conn.prepareStatement(strQuery.toString());
					//pstmt = new LoggableStatement(conn,strQuery.toString());
					pstmt.setString(1, AcptNo);
					pstmt.setString(2,AcptNo);
					pstmt.setString(3,prcSys);
			        ////ecamsLogger.error(((LoggableStatement)pstmt).getQueryString());
			        pstmt.executeUpdate();
			        pstmt.close();
		        }
			}
			strQuery.setLength(0);
			strQuery.append("update cmr1000 set cr_notify='0',cr_retryyn='Y'             \n");
			strQuery.append(" where cr_acptno=?                                          \n");
	        pstmt = conn.prepareStatement(strQuery.toString());
			//pstmt = new LoggableStatement(conn,strQuery.toString());
			pstmt.setString(1,AcptNo);
	        ////ecamsLogger.error(((LoggableStatement)pstmt).getQueryString());
	        pstmt.executeUpdate();
	        pstmt.close();

	        conn.commit();
	        conn.close();

			rs = null;
			pstmt = null;
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
					ecamsLogger.error("## Cmr0250.svrProc() connection release exception ##");
					ex3.printStackTrace();
				}
			}
			ecamsLogger.error("## Cmr0250.svrProc() SQLException START ##");
			ecamsLogger.error("## Error DESC : ", sqlexception);
			ecamsLogger.error("## Cmr0250.svrProc() SQLException END ##");
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
					ecamsLogger.error("## Cmr0200.svrProc() connection release exception ##");
					ex3.printStackTrace();
				}
			}
			ecamsLogger.error("## Cmr0250.svrProc() Exception START ##");
			ecamsLogger.error("## Error DESC : ", exception);
			ecamsLogger.error("## Cmr0250.putDeploy() Exception END ##");
			throw exception;
		}finally{
			if (strQuery != null) strQuery = null;
			if (rs != null)     try{rs.close();}catch (Exception ex){ex.printStackTrace();}
			if (pstmt != null)  try{pstmt.close();}catch (Exception ex2){ex2.printStackTrace();}
			if (conn != null){
				try{
					ConnectionResource.release(conn);
				}catch(Exception ex3){
					ecamsLogger.error("## Cmr0250.svrProc() connection release exception ##");
					ex3.printStackTrace();
				}
			}
		}
	}//end of putDeploy() method statement

	public String delReq(String AcptNo,String ItemId,String SignTeam,String ReqCd) throws SQLException, Exception {
		Connection        conn        = null;
		PreparedStatement pstmt       = null;
		PreparedStatement pstmt2       = null;
		ResultSet         rs          = null;
		ResultSet         rs2          = null;
		StringBuffer      strQuery    = new StringBuffer();
		String            strErMsg    = "";
		
		ConnectionContext connectionContext = new ConnectionResource();

		try {
			conn = connectionContext.getConnection();
			conn.setAutoCommit(false);
			
            strQuery.setLength(0);
            strQuery.append("delete cmr1011 where cr_acptno=?                     \n");
            strQuery.append("   and cr_serno in (select cr_serno from cmr1010     \n");
            strQuery.append("                     where cr_acptno=?               \n");
            strQuery.append("                       and cr_baseitem=?)            \n");
            pstmt = conn.prepareStatement(strQuery.toString());
            pstmt.setString(1,AcptNo);
            pstmt.setString(2,AcptNo);
            pstmt.setString(3,ItemId);
            pstmt.executeUpdate();
            pstmt.close();

            strQuery.setLength(0);
            strQuery.append("delete cmr1010 where cr_acptno=? and cr_baseitem=?    \n");
            pstmt = conn.prepareStatement(strQuery.toString());
            pstmt.setString(1,AcptNo);
            pstmt.setString(2,ItemId);
            pstmt.executeUpdate();
            pstmt.close();

            strQuery.setLength(0);
            strQuery.append("update cmr0020 set cr_status='0'                      \n");
            strQuery.append(" where cr_itemid in (select cr_itemid from cmr1010    \n");
            strQuery.append("                      where cr_acptno=?               \n");
            strQuery.append("                        and cr_baseitem=?)            \n");
            pstmt = conn.prepareStatement(strQuery.toString());
            pstmt.setString(1,AcptNo);
            pstmt.setString(2,ItemId);
            pstmt.executeUpdate();
            pstmt.close();

            strErMsg = "0";
            strQuery.setLength(0);
            strQuery.append("select count(*) as cnt from cmr1010                    \n");
            strQuery.append(" where cr_acptno=?                                     \n");
            pstmt = conn.prepareStatement(strQuery.toString());
            pstmt.setString(1,AcptNo);
            rs = pstmt.executeQuery();
            if (rs.next()) {
            	if (rs.getInt("cnt") == 0) {
            		strQuery.setLength(0);
                    strQuery.append("delete cmr9920 where cr_acptno=?               \n");
                    pstmt2 = conn.prepareStatement(strQuery.toString());
                    pstmt2.setString(1,AcptNo);
                    pstmt2.executeUpdate();
                    pstmt2.close();

            		strQuery.setLength(0);
                    strQuery.append("delete cmr9910 where cr_acptno=?               \n");
                    pstmt2 = conn.prepareStatement(strQuery.toString());
                    pstmt2.setString(1,AcptNo);
                    pstmt2.executeUpdate();
                    pstmt2.close();

                    strQuery.setLength(0);
                    strQuery.append("delete cmr9900 where cr_acptno=?               \n");
                    pstmt2 = conn.prepareStatement(strQuery.toString());
                    pstmt2.setString(1,AcptNo);
                    pstmt2.executeUpdate();
                    pstmt2.close();

            		strQuery.setLength(0);
                    strQuery.append("delete cmr1001 where cr_acptno=?               \n");
                    pstmt2 = conn.prepareStatement(strQuery.toString());
                    pstmt2.setString(1,AcptNo);
                    pstmt2.executeUpdate();
                    pstmt2.close();

            		strQuery.setLength(0);
                    strQuery.append("delete cmr1000 where cr_acptno=?               \n");
                    pstmt2 = conn.prepareStatement(strQuery.toString());
                    pstmt2.setString(1,AcptNo);
                    pstmt2.executeUpdate();
                    pstmt2.close();
            	}
            }
            else {
	            strQuery.setLength(0);
	            strQuery.append("select count(*) as cnt from cmr1010                    \n");
	            strQuery.append(" where cr_acptno=?                                     \n");
	            strQuery.append("   and (cr_putcode is null or cr_putcode != '0000')    \n");
	            pstmt2 = conn.prepareStatement(strQuery.toString());
	            pstmt2.setString(1,AcptNo);
	            rs2 = pstmt2.executeQuery();
	            if (rs2.next()) {
	            	if (rs2.getInt("cnt") == 0) {
	            		Cmr3100 gyulProc = new Cmr3100();
	            		strErMsg = gyulProc.nextConf(AcptNo, SignTeam, "삭제 후 자동완료", "1", ReqCd);
	            		gyulProc = null;
	            	}
	            }
	            rs2.close();
	            pstmt2.close();
            }

            rs.close();
            pstmt.close();

            if (strErMsg.equals("0")){
            	conn.commit();
            }
            else{
            	conn.rollback();
            }

            conn.close();

			rs = null;
			pstmt = null;
	        rs2 = null;
	        pstmt2 = null;
			conn = null;

            return strErMsg;

		} catch (SQLException sqlexception) {
			sqlexception.printStackTrace();
			if (rs != null)     try{rs.close();}catch (Exception ex){ex.printStackTrace();}
			if (pstmt != null)  try{pstmt.close();}catch (Exception ex2){ex2.printStackTrace();}
			if (conn != null){
				try{
					conn.rollback();
					ConnectionResource.release(conn);
				}catch(Exception ex3){
					ecamsLogger.error("## Cmr0250.delReq() connection release exception ##");
					ex3.printStackTrace();
				}
			}
			ecamsLogger.error("## Cmr0250.delReq() SQLException START ##");
			ecamsLogger.error("## Error DESC : ", sqlexception);
			ecamsLogger.error("## Cmr0250.delReq() SQLException END ##");
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
					ecamsLogger.error("## Cmr0200.delReq() connection release exception ##");
					ex3.printStackTrace();
				}
			}
			ecamsLogger.error("## Cmr0250.delReq() Exception START ##");
			ecamsLogger.error("## Error DESC : ", exception);
			ecamsLogger.error("## Cmr0250.delReq() Exception END ##");
			throw exception;
		}finally{
			if (strQuery != null) strQuery = null;
			if (rs != null)     try{rs.close();}catch (Exception ex){ex.printStackTrace();}
			if (pstmt != null)  try{pstmt.close();}catch (Exception ex2){ex2.printStackTrace();}
			if (conn != null){
				try{
					ConnectionResource.release(conn);
				}catch(Exception ex3){
					ecamsLogger.error("## Cmr0250.delReq() connection release exception ##");
					ex3.printStackTrace();
				}
			}
		}
	}//end of delReq() method statement

	public String chgVolPath(String SysCd,String DirPath,String SvrCd,String RsrcCd,int SvrSeq,Connection conn) throws SQLException, Exception {
		PreparedStatement pstmt       = null;
		ResultSet         rs          = null;
		StringBuffer      strQuery    = new StringBuffer();
		String            strPath     = null;
		String            baseDir     = null;
		String            svrDir     = null;

		try {
			strQuery.append("select a.cm_svrcd,a.cm_volpath from cmm0038 a,cmm0031 b   \n");
			strQuery.append(" where b.cm_syscd=? and b.cm_svrcd=(select cm_dirbase from cmm0030 where cm_syscd=b.cm_syscd) and b.cm_closedt is null \n");
			strQuery.append("   and b.cm_syscd=a.cm_syscd and b.cm_svrcd=a.cm_svrcd    \n");
			strQuery.append("   and b.cm_seqno=a.cm_seqno and a.cm_rsrccd=?            \n");
			
			pstmt = conn.prepareStatement(strQuery.toString());
			//pstmt = new LoggableStatement(conn,strQuery.toString());
			pstmt.setString(1, SysCd);
			pstmt.setString(2, RsrcCd);
            ////ecamsLogger.error(((LoggableStatement)pstmt).getQueryString());
            rs = pstmt.executeQuery();
            if (rs.next()){
            	if (rs.getString("cm_volpath") != "") baseDir = rs.getString("cm_volpath");
			}
            rs.close();
            pstmt.close();

            strQuery.setLength(0);
			strQuery.append("select a.cm_svrcd,a.cm_volpath                            \n");
			strQuery.append("  from cmm0038 a,cmm0031 b                                \n");
			strQuery.append(" where b.cm_syscd=? and b.cm_svrcd=?                      \n");	//AcptNo
			strQuery.append("   and b.cm_seqno=?                                       \n");	//AcptNo
			strQuery.append("   and b.cm_syscd=a.cm_syscd and b.cm_svrcd=a.cm_svrcd    \n");
			strQuery.append("   and b.cm_seqno=a.cm_seqno and a.cm_rsrccd=?            \n");
			
			pstmt = conn.prepareStatement(strQuery.toString());
			//pstmt = new LoggableStatement(conn,strQuery.toString());
			pstmt.setString(1, SysCd);
			pstmt.setString(2, SvrCd);
			pstmt.setInt(3, SvrSeq);
			pstmt.setString(4, RsrcCd);
            ////ecamsLogger.error(((LoggableStatement)pstmt).getQueryString());
            rs = pstmt.executeQuery();
            if (rs.next()){
            	if (rs.getString("cm_volpath") != ""){
            		svrDir = rs.getString("cm_volpath");
            	}
			}

            rs.close();
            pstmt.close();

            if (svrDir != null && baseDir != null) {
            	if (svrDir.equals(baseDir)){
            		strPath = DirPath;
            	} else if (DirPath.length()>0 && DirPath.length()>baseDir.length()){
            		strPath = svrDir + DirPath.substring(baseDir.length());
            	} else {
            		strPath = svrDir;
            	}
            }else{
            	strPath = DirPath;
            }

			rs = null;
			pstmt = null;

            return strPath;


		} catch (SQLException sqlexception) {
			sqlexception.printStackTrace();
			ecamsLogger.error("## Cmr0250.chgVolPath() SQLException START ##");
			ecamsLogger.error("## Error DESC : ", sqlexception);
			ecamsLogger.error("## Cmr0250.chgVolPath() SQLException END ##");
			throw sqlexception;
		} catch (Exception exception) {
			exception.printStackTrace();
			ecamsLogger.error("## Cmr0250.chgVolPath() Exception START ##");
			ecamsLogger.error("## Error DESC : ", exception);
			ecamsLogger.error("## Cmr0250.chgVolPath() Exception END ##");
			throw exception;
		}finally{
			if (strQuery != null) strQuery = null;
			if (rs != null)     try{rs.close();}catch (Exception ex){ex.printStackTrace();}
			if (pstmt != null)  try{pstmt.close();}catch (Exception ex2){ex2.printStackTrace();}
		}
	}//end of chgVolPath() method statement

	public Object[] getPrcSys(String AcptNo) throws SQLException, Exception {
		Connection        conn        = null;
		PreparedStatement pstmt       = null;
		ResultSet         rs          = null;
		StringBuffer      strQuery    = new StringBuffer();
		ArrayList<HashMap<String, String>>  rsval = new ArrayList<HashMap<String, String>>();
		HashMap<String, String>			  rst		  = null;
		Object[] 		  returnObject = null;
		//String            strAcptNo    = "";
		//int               i = 0;
		int               parmCnt = 0;
		
		ConnectionContext connectionContext = new ConnectionResource();
		
		try {
			conn = connectionContext.getConnection();
			//String strKey = "";
			
			strQuery.setLength(0);
			strQuery.append("select b.cm_micode,b.cm_codename,a.cr_confdate            \n");
			strQuery.append("  from cmr9900 a,cmm0020 b                                \n");
			strQuery.append(" where a.cr_acptno=?                                      \n");
			strQuery.append("   and ((a.cr_locat<>'00' and a.cr_teamcd='1'             \n");
			strQuery.append("         and a.cr_status<>'3'                             \n");
			strQuery.append("         and a.cr_confdate is not null) or                \n");
			strQuery.append("        (a.cr_locat='00' and a.cr_teamcd='1'              \n");
			strQuery.append("                         and a.cr_status in ('0','3')))   \n");			
			strQuery.append("   and b.cm_macode='SYSGBN' and b.cm_micode=a.cr_team     \n");
			strQuery.append(" order by a.cr_confdate                                   \n");
			
			pstmt = conn.prepareStatement(strQuery.toString());
			//pstmt = new LoggableStatement(conn,strQuery.toString());
			pstmt.setString(++parmCnt, AcptNo);
            ////ecamsLogger.error(((LoggableStatement)pstmt).getQueryString());
            rs = pstmt.executeQuery();
            
            //boolean findSw = false;
            while (rs.next()){
            	if (rs.getRow() == 1) {
            		//findSw = true;
            		rst = new HashMap<String, String>();
    				//rst.put("ID", Integer.toString(rs.getRow()));
    				rst.put("cm_codename", "전체");
    				rst.put("cm_micode", "0");
    				rsval.add(rst);
            	} 
            	rst = new HashMap<String, String>();
				//rst.put("ID", Integer.toString(rs.getRow()));
            	rst.put("cm_codename", rs.getString("cm_codename"));
				rst.put("cm_micode", rs.getString("cm_micode"));
				rst.put("qrycd", AcptNo.substring(4,6));
				rsval.add(rst);
				rst = null;
			}            
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
			ecamsLogger.error("## Cmr0250.getPrcSys() SQLException START ##");
			ecamsLogger.error("## Error DESC : ", sqlexception);
			ecamsLogger.error("## Cmr0250.getPrcSys() SQLException END ##");
			throw sqlexception;
		} catch (Exception exception) {
			exception.printStackTrace();
			ecamsLogger.error("## Cmr0250.getPrcSys() Exception START ##");
			ecamsLogger.error("## Error DESC : ", exception);
			ecamsLogger.error("## Cmr0250.getPrcSys() Exception END ##");
			throw exception;
		}finally{
			if (strQuery != null) strQuery = null;
			if (returnObject != null) returnObject = null;
			if (rs != null)     try{rs.close();}catch (Exception ex){ex.printStackTrace();}
			if (pstmt != null)  try{pstmt.close();}catch (Exception ex2){ex2.printStackTrace();}
			if (conn != null){
				try{
					ConnectionResource.release(conn);
				}catch(Exception ex3){
					ecamsLogger.error("## Cmr0250.getPrcSys() connection release exception ##");
					ex3.printStackTrace();
				}
			}
		}
	}//end of getPrcSys() method statement

	public String updtSeq(String AcptNo,ArrayList<HashMap<String,String>> fileList) throws SQLException, Exception {
		Connection        conn        = null;
		PreparedStatement pstmt       = null;
		StringBuffer      strQuery    = new StringBuffer();

		ConnectionContext connectionContext = new ConnectionResource();

		try {
			conn = connectionContext.getConnection();
			conn.setAutoCommit(false);
            for (int i=0;i<fileList.size();i++){
            	strQuery.setLength(0);
            	strQuery.append("update cmr1010 set cr_priority=?                    \n");
                strQuery.append(" where cr_acptno=? and cr_serno=?                   \n");
                pstmt = conn.prepareStatement(strQuery.toString());
                pstmt.setInt(1,Integer.parseInt(fileList.get(i).get("priority")));
                pstmt.setString(2,AcptNo);
                pstmt.setInt(3,Integer.parseInt(fileList.get(i).get("cr_serno")));
                pstmt.executeUpdate();
                pstmt.close();
            }
            conn.commit();
            conn.close();
			pstmt = null;
			conn = null;

            return "0";


		} catch (SQLException sqlexception) {
			sqlexception.printStackTrace();
			if (pstmt != null)  try{pstmt.close();}catch (Exception ex2){ex2.printStackTrace();}
			if (conn != null){
				try{
					conn.rollback();
					ConnectionResource.release(conn);
				}catch(Exception ex3){
					ecamsLogger.error("## Cmr0250.updtSeq() connection release exception ##");
					ex3.printStackTrace();
				}
			}
			ecamsLogger.error("## Cmr0250.updtSeq() SQLException START ##");
			ecamsLogger.error("## Error DESC : ", sqlexception);
			ecamsLogger.error("## Cmr0250.updtSeq() SQLException END ##");
			throw sqlexception;
		} catch (Exception exception) {
			exception.printStackTrace();
			if (pstmt != null)  try{pstmt.close();}catch (Exception ex2){ex2.printStackTrace();}
			if (conn != null){
				try{
					conn.rollback();
					ConnectionResource.release(conn);
				}catch(Exception ex3){
					ecamsLogger.error("## Cmr0250.updtSeq() connection release exception ##");
					ex3.printStackTrace();
				}
			}
			ecamsLogger.error("## Cmr0250.updtSeq() Exception START ##");
			ecamsLogger.error("## Error DESC : ", exception);
			ecamsLogger.error("## Cmr0250.updtSeq() Exception END ##");
			throw exception;
		}finally{
			if (strQuery != null) strQuery = null;
			if (pstmt != null)  try{pstmt.close();}catch (Exception ex2){ex2.printStackTrace();}
			if (conn != null){
				try{
					ConnectionResource.release(conn);
				}catch(Exception ex3){
					ecamsLogger.error("## Cmr0250.updtSeq() connection release exception ##");
					ex3.printStackTrace();
				}
			}
		}
	}//end of updtSeq() method statement

	public String updtDeploy(String AcptNo,String ReqPass,String DeployDate,String PassCd) throws SQLException, Exception {
		Connection        conn        = null;
		PreparedStatement pstmt       = null;
		ResultSet         rs          = null;
		StringBuffer      strQuery    = new StringBuffer();
		int               parmCnt     = 0;
		ConnectionContext connectionContext = new ConnectionResource();

		try {
			conn = connectionContext.getConnection();
			
			String erMsg = "";
			strQuery.setLength(0);
			strQuery.append("select count(*) cnt from cmr1011    \n"); 
			strQuery.append(" where cr_acptno=?                  \n");
			strQuery.append("   and cr_prcsys='SYSED'            \n");
			//pstmt = new LoggableStatement(conn,strQuery.toString());
			pstmt = conn.prepareStatement(strQuery.toString());	
			pstmt.setString(1, AcptNo);
	        ////ecamsLogger.error(((LoggableStatement)pstmt).getQueryString());           
	        rs = pstmt.executeQuery();
			if (rs.next()){
				if (rs.getInt("cnt")>0) {
					erMsg = "이미 배포처리가 시작되었습니다. 적용구분을 수정할 수 가 없습니다.";					
				}
			}
            rs.close();
            pstmt.close();
            
            if (erMsg.length() == 0) {
				conn.setAutoCommit(false);
	            strQuery.setLength(0);
	        	strQuery.append("update cmr1000 set cr_passok=?             \n");
	        	if (ReqPass.equals("2")) strQuery.append(",cr_sayucd=?      \n");
	            strQuery.append(" where cr_acptno=?                         \n");
	            pstmt = conn.prepareStatement(strQuery.toString());
	            pstmt.setString(++parmCnt,ReqPass);
	            if (ReqPass.equals("2")) pstmt.setString(++parmCnt, PassCd);
	            pstmt.setString(++parmCnt,AcptNo);
	            pstmt.executeUpdate();
	            pstmt.close();
	
	            if (ReqPass.equals("4")) {
	            	strQuery.setLength(0);
	            	strQuery.append("update cmr1010 set cr_aplydate=?           \n");
	                strQuery.append(" where cr_acptno=?                         \n");
	                pstmt = conn.prepareStatement(strQuery.toString());
	                pstmt.setString(1,DeployDate);
	                pstmt.setString(2,AcptNo);
	                pstmt.executeUpdate();
	                pstmt.close();
	                
	                strQuery.setLength(0);
	            	strQuery.append("update cmr1000 set cr_prcreq=?             \n");
	                strQuery.append(" where cr_acptno=?                         \n");
	                pstmt = conn.prepareStatement(strQuery.toString());
	                pstmt.setString(1,DeployDate);
	                pstmt.setString(2,AcptNo);
	                pstmt.executeUpdate();
	                pstmt.close();
	                
	
	            } else {
	            	strQuery.setLength(0);
	            	strQuery.append("update cmr1010 set cr_aplydate=''          \n");
	                strQuery.append(" where cr_acptno=?                         \n");
	                pstmt = conn.prepareStatement(strQuery.toString());
	                pstmt.setString(1,AcptNo);
	                pstmt.executeUpdate();
	                pstmt.close();
	                
	                strQuery.setLength(0);
	            	strQuery.append("update cmr1000 set cr_prcreq=''            \n");
	                strQuery.append(" where cr_acptno=?                         \n");
	                pstmt = conn.prepareStatement(strQuery.toString());
	                pstmt.setString(1,AcptNo);
	                pstmt.executeUpdate();
	                pstmt.close();
	
	            }
	
	            conn.commit();
	            erMsg = "0";
            }
            
            conn.close();
			pstmt = null;
			conn = null;

            return erMsg;

		} catch (SQLException sqlexception) {
			sqlexception.printStackTrace();
			if (pstmt != null)  try{pstmt.close();}catch (Exception ex2){ex2.printStackTrace();}
			if (conn != null){
				try{
					conn.rollback();
					ConnectionResource.release(conn);
				}catch(Exception ex3){
					ecamsLogger.error("## Cmr0250.updtDeploy() connection release exception ##");
					ex3.printStackTrace();
				}
			}
			ecamsLogger.error("## Cmr0250.updtDeploy() SQLException START ##");
			ecamsLogger.error("## Error DESC : ", sqlexception);
			ecamsLogger.error("## Cmr0250.updtDeploy() SQLException END ##");
			throw sqlexception;
		} catch (Exception exception) {
			exception.printStackTrace();
			if (strQuery != null) strQuery = null;
			if (pstmt != null)  try{pstmt.close();}catch (Exception ex2){ex2.printStackTrace();}
			if (conn != null){
				try{
					conn.rollback();
					ConnectionResource.release(conn);
				}catch(Exception ex3){
					ecamsLogger.error("## Cmr0250.updtDeploy() connection release exception ##");
					ex3.printStackTrace();
				}
			}
			ecamsLogger.error("## Cmr0250.updtDeploy() Exception START ##");
			ecamsLogger.error("## Error DESC : ", exception);
			ecamsLogger.error("## Cmr0250.updtDeploy() Exception END ##");
			throw exception;
		}finally{
			if (pstmt != null)  try{pstmt.close();}catch (Exception ex2){ex2.printStackTrace();}
			if (conn != null){
				try{
					ConnectionResource.release(conn);
				}catch(Exception ex3){
					ecamsLogger.error("## Cmr0250.updtDeploy() connection release exception ##");
					ex3.printStackTrace();
				}
			}
		}
	}//end of updtDeploy() method statement

	public String updtDeploy_2(String AcptNo,String CD) throws SQLException, Exception {
		Connection        conn        = null;
		PreparedStatement pstmt       = null;
		StringBuffer      strQuery    = new StringBuffer();

		ConnectionContext connectionContext = new ConnectionResource();

		try {
			conn = connectionContext.getConnection();
			conn.setAutoCommit(false);
            strQuery.setLength(0);
        	strQuery.append("update cmr1000 set cr_gyuljae=?            \n");
            strQuery.append(" where cr_acptno=?                         \n");
            pstmt = conn.prepareStatement(strQuery.toString());
            pstmt.setString(1,CD);
            pstmt.setString(2,AcptNo);
            pstmt.executeUpdate();
            pstmt.close();

            if (CD.equals("1")) {
            	strQuery.setLength(0);
            	strQuery.append("update cmr1010                                        \n");
            	strQuery.append("   set cr_aplydate=to_char(SYSDATE,'yyyymmddhh24mi')  \n");
                strQuery.append(" where cr_acptno=?                                    \n");
                pstmt = conn.prepareStatement(strQuery.toString());
                pstmt.setString(1,AcptNo);
                pstmt.executeUpdate();
                pstmt.close();

            } else {
            	strQuery.setLength(0);
            	strQuery.append("update cmr1010 set cr_aplydate=''          \n");
                strQuery.append(" where cr_acptno=?                         \n");
                pstmt = conn.prepareStatement(strQuery.toString());
                pstmt.setString(1,AcptNo);
                pstmt.executeUpdate();
                pstmt.close();

            }

            conn.commit();
            conn.close();
			pstmt = null;
			conn = null;

            return "0";

		} catch (SQLException sqlexception) {
			sqlexception.printStackTrace();
			if (pstmt != null)  try{pstmt.close();}catch (Exception ex2){ex2.printStackTrace();}
			if (conn != null){
				try{
					conn.rollback();
					ConnectionResource.release(conn);
				}catch(Exception ex3){
					ecamsLogger.error("## Cmr0250.updtDeploy_2() connection release exception ##");
					ex3.printStackTrace();
				}
			}
			ecamsLogger.error("## Cmr0250.updtDeploy_2() SQLException START ##");
			ecamsLogger.error("## Error DESC : ", sqlexception);
			ecamsLogger.error("## Cmr0250.updtDeploy_2() SQLException END ##");
			throw sqlexception;
		} catch (Exception exception) {
			exception.printStackTrace();
			if (pstmt != null)  try{pstmt.close();}catch (Exception ex2){ex2.printStackTrace();}
			if (conn != null){
				try{
					conn.rollback();
					ConnectionResource.release(conn);
				}catch(Exception ex3){
					ecamsLogger.error("## updtDeploy_2.updtDeploy() connection release exception ##");
					ex3.printStackTrace();
				}
			}
			ecamsLogger.error("## Cmr0250.updtDeploy_2() Exception START ##");
			ecamsLogger.error("## Error DESC : ", exception);
			ecamsLogger.error("## Cmr0250.updtDeploy_2() Exception END ##");
			throw exception;
		}finally{
			if (strQuery != null) strQuery = null;
			if (pstmt != null)  try{pstmt.close();}catch (Exception ex2){ex2.printStackTrace();}
			if (conn != null){
				try{
					ConnectionResource.release(conn);
				}catch(Exception ex3){
					ecamsLogger.error("## Cmr0250.updtDeploy_2() connection release exception ##");
					ex3.printStackTrace();
				}
			}
		}
	}//end of updtDeploy_2() method statement

	public String updtPrjInfo(HashMap<String,String> etcData) throws SQLException, Exception {
		Connection        conn        = null;
		PreparedStatement pstmt       = null;
		StringBuffer      strQuery    = new StringBuffer();
		ConnectionContext connectionContext = new ConnectionResource();

		try {
			conn = connectionContext.getConnection();
            strQuery.setLength(0);
        	strQuery.append("update cmr1020 set CR_REQTIT=?,            \n");
        	strQuery.append("       CR_REQUSR=?,CR_REQDATE=?,           \n");
        	strQuery.append("       CR_REQMETH=?,CR_REQDET=?,           \n");
        	strQuery.append("       CR_REQMSG=?                         \n");
            strQuery.append(" where cr_acptno=?                         \n");
            pstmt = conn.prepareStatement(strQuery.toString());
            pstmt.setString(1,etcData.get("reqtit"));
            pstmt.setString(2,etcData.get("requser"));
            pstmt.setString(3,etcData.get("reqdate"));
            pstmt.setString(4,etcData.get("reqmeth"));
            pstmt.setString(5,etcData.get("reqdet"));
            pstmt.setString(6,etcData.get("reqmsg"));
            pstmt.setString(7,etcData.get("acptno"));
            pstmt.executeUpdate();
            pstmt.close();

            conn.commit();
            conn.close();
			pstmt = null;
			conn = null;

            return "0";

		} catch (SQLException sqlexception) {
			sqlexception.printStackTrace();
			ecamsLogger.error("## Cmr0250.updtPrjInfo() SQLException START ##");
			ecamsLogger.error("## Error DESC : ", sqlexception);
			ecamsLogger.error("## Cmr0250.updtPrjInfo() SQLException END ##");
			throw sqlexception;
		} catch (Exception exception) {
			exception.printStackTrace();
			ecamsLogger.error("## Cmr0250.updtPrjInfo() Exception START ##");
			ecamsLogger.error("## Error DESC : ", exception);
			ecamsLogger.error("## Cmr0250.updtPrjInfo() Exception END ##");
			throw exception;
		}finally{
			if (pstmt != null)  try{pstmt.close();}catch (Exception ex2){ex2.printStackTrace();}
			if (conn != null){
				try{
					ConnectionResource.release(conn);
				}catch(Exception ex3){
					ecamsLogger.error("## Cmr0250.updtPrjInfo() connection release exception ##");
					ex3.printStackTrace();
				}
			}
		}
	}//end of updtPrjInfo() method statement
	
	public String updtTemp(String AcptNo,String ItemId) throws SQLException, Exception {
		Connection        conn        = null;
		PreparedStatement pstmt       = null;
		StringBuffer      strQuery    = new StringBuffer();

		ConnectionContext connectionContext = new ConnectionResource();

		try {
			conn = connectionContext.getConnection();
			conn.setAutoCommit(false);
            strQuery.setLength(0);
        	strQuery.append("update cmr1010 set cr_tmpdelyn='N'             \n");
            strQuery.append(" where cr_acptno=? and cr_baseitem=?           \n");
            pstmt = conn.prepareStatement(strQuery.toString());
            pstmt.setString(1,AcptNo);
            pstmt.setString(2,ItemId);
            pstmt.executeUpdate();
            pstmt.close();

            conn.commit();
            conn.close();
			pstmt = null;
			conn = null;

            return "0";

		} catch (SQLException sqlexception) {
			sqlexception.printStackTrace();
			if (pstmt != null)  try{pstmt.close();}catch (Exception ex2){ex2.printStackTrace();}
			if (conn != null){
				try{
					conn.rollback();
					ConnectionResource.release(conn);
				}catch(Exception ex3){
					ecamsLogger.error("## Cmr0250.updtTemp() connection release exception ##");
					ex3.printStackTrace();
				}
			}
			ecamsLogger.error("## Cmr0250.updtTemp() SQLException START ##");
			ecamsLogger.error("## Error DESC : ", sqlexception);
			ecamsLogger.error("## Cmr0250.updtTemp() SQLException END ##");
			throw sqlexception;
		} catch (Exception exception) {
			exception.printStackTrace();
			if (pstmt != null)  try{pstmt.close();}catch (Exception ex2){ex2.printStackTrace();}
			if (conn != null){
				try{
					conn.rollback();
					ConnectionResource.release(conn);
				}catch(Exception ex3){
					ecamsLogger.error("## Cmr0250.updtTemp() connection release exception ##");
					ex3.printStackTrace();
				}
			}
			ecamsLogger.error("## Cmr0250.updtTemp() Exception START ##");
			ecamsLogger.error("## Error DESC : ", exception);
			ecamsLogger.error("## Cmr0250.updtTemp() Exception END ##");
			throw exception;
		}finally{
			if (strQuery != null) strQuery = null;
			if (pstmt != null)  try{pstmt.close();}catch (Exception ex2){ex2.printStackTrace();}
			if (conn != null){
				try{
					ConnectionResource.release(conn);
				}catch(Exception ex3){
					ecamsLogger.error("## Cmr0250.updtTemp() connection release exception ##");
					ex3.printStackTrace();
				}
			}
		}
	}//end of updtTemp() method statement

	public String progCncl(String AcptNo,String ItemId,String PrcSys) throws SQLException, Exception {
		Connection        conn        = null;
		Connection        connD        = null;
		ResultSet         rs          = null;
		ResultSet         rs2          = null;
		PreparedStatement pstmt       = null;
		PreparedStatement pstmt2      = null;
		PreparedStatement pstmt3      = null;
		StringBuffer      strQuery    = new StringBuffer();
		ConnectionContext connectionContext = new ConnectionResource();
		ConnectionContext connectionContextD = new ConnectionResource(false,"D");
		Runtime  run = null;
		Process p = null;
		

		try {
			//ecamsLogger.debug("+++++++++prcCd========"+prcCd);
			String  binpath;
			String[] chkAry;
			int		cmdrtn;
			
			SystemPath systemPath = new SystemPath();
			binpath = systemPath.getTmpDir("14");
			systemPath = null;
			
			run = Runtime.getRuntime();

			chkAry = new String[4];
			chkAry[0] = "/bin/sh";
//			chkAry[1] = "procck2";
			chkAry[1] = binpath+"/procck2"; // 20221219
			chkAry[2] = "ecams_acct";
			chkAry[3] = AcptNo;

			p = run.exec(chkAry);
			p.waitFor();

			cmdrtn = p.exitValue();
			if (cmdrtn > 1) {
				//ecamsLogger.error(chkAry[0]+" " + chkAry[1] + " " + chkAry[2]);
				//ecamsLogger.error(cmdrtn);
				return "2";
			}

			conn = connectionContext.getConnection();
			connD = connectionContextD.getConnection();
			if (PrcSys.equals("SYSCB") && !AcptNo.substring(4,6).equals("16")) {
				boolean findSw = false;
				strQuery.setLength(0);
	        	strQuery.append("select count(*) cnt from cmr1010 a,cmm0036 b     \n");
	        	strQuery.append(" where a.cr_acptno=? and a.cr_baseitem=?         \n");
	            strQuery.append("   and a.cr_syscd=b.cm_syscd                     \n");
	            strQuery.append("   and a.cr_rsrccd=b.cm_rsrccd                   \n");
	            strQuery.append("   and substr(b.cm_info,7,1)='1'                 \n");
	            pstmt = conn.prepareStatement(strQuery.toString());
	            pstmt.setString(1,AcptNo);
	            pstmt.setString(2,ItemId);
	            rs = pstmt.executeQuery();
	            if (rs.next()) {
	            	if (rs.getInt("cnt") > 0) {
	            		findSw = true;
	            	}
	            }
	            pstmt.close();
	            rs.close();

	            if (findSw == true) {
	            	/*
	            	run = Runtime.getRuntime();

	    			chkAry = new String[3];
	    			chkAry[0] = binpath+"/procck2";
	    			if (AcptNo.substring(4,6).equals("16")) chkAry[1] = "ecams_ih_new";
	    			else chkAry[1] = "ecams_acct";
	    			chkAry[2] = AcptNo;

	    			p = run.exec(chkAry);
	    			p.waitFor();

	    			cmdrtn = p.exitValue();
	    			if (cmdrtn > 1) {
	    				ecamsLogger.error(chkAry[0]+" " + chkAry[1] + " " + chkAry[2]);
	    				ecamsLogger.error(cmdrtn);
	    				return "2";
	    			}
	    			*/
	            }
			}

			conn.setAutoCommit(false);
            
            strQuery.setLength(0);
        	strQuery.append("update cmr1010 set cr_status='3',cr_prcdate=SYSDATE,\n");
        	strQuery.append("                   cr_cnclstep=?                    \n");
        	strQuery.append(" where cr_acptno=? and cr_itemid=?                  \n");
            pstmt = conn.prepareStatement(strQuery.toString());
            pstmt.setString(1,PrcSys);
            pstmt.setString(2,AcptNo);
            pstmt.setString(3,ItemId);
            pstmt.executeUpdate();
            pstmt.close();

            strQuery.setLength(0);
	    	strQuery.append("update cmr1010 set cr_confno=''             \n");
        	strQuery.append(" where cr_confno=? and instr(nvl(cr_basepgm,cr_baseitem),?)>0 \n");
	    	strQuery.append("   and cr_acptno<>cr_confno                 \n");
	        pstmt = conn.prepareStatement(strQuery.toString());
	        pstmt.setString(1,AcptNo);
	        pstmt.setString(2,ItemId);
	        pstmt.executeUpdate();
	        pstmt.close();
	        
	        strQuery.setLength(0);
	        strQuery.append("select count(*) cnt from cmr1010 a,cmr1010 b        \n");
	        strQuery.append(" where a.cr_acptno=? and instr(nvl(a.cr_basepgm,a.cr_baseitem),?)>0  \n");
	        strQuery.append("   and a.cr_acptno=b.cr_acptno                      \n");
	        strQuery.append("   and instr(nvl(a.cr_basepgm,a.cr_baseitem),b.cr_itemid)>0 \n");
	        strQuery.append("   and b.cr_status<>'3'                             \n");
	        pstmt = conn.prepareStatement(strQuery.toString());
	        pstmt.setString(1, AcptNo);
	        pstmt.setString(2, ItemId);
	        rs = pstmt.executeQuery();
	        if (rs.next()) {
	        	if (rs.getInt("cnt")== 0) {
	        		strQuery.setLength(0);
			    	strQuery.append("update cmr1010 set cr_status='3',cr_prcdate=SYSDATE,\n");
			    	strQuery.append("                   cr_cnclstep=?                    \n");
			    	strQuery.append(" where cr_acptno=? and instr(nvl(cr_basepgm,cr_baseitem),?)>0 \n");
			    	strQuery.append("   and cr_status<>'3'                               \n");
			        pstmt2 = conn.prepareStatement(strQuery.toString());
			        pstmt2.setString(1,PrcSys);
			        pstmt2.setString(2,AcptNo);
			        pstmt2.setString(3,ItemId);
			        pstmt2.executeUpdate();
			        pstmt2.close();
	        	}
	        }
	        rs.close();
	        pstmt.close();
	        
            strQuery.setLength(0);
            strQuery.append("update cmr1000 set cr_cnclstep=?,           \n");
            strQuery.append("       cr_cncllstd=SYSDATE,cr_retryyn='N'   \n");
            strQuery.append(" where cr_acptno=?                          \n");
            pstmt = conn.prepareStatement(strQuery.toString());
            pstmt.setString(1, PrcSys);
            pstmt.setString(2, AcptNo);
            pstmt.executeUpdate();
            pstmt.close();
            /////////////////////////////////////차세대DB 컨프넘버 삭제///////////

        	
            strQuery.setLength(0);
            strQuery.append("select a.cr_rsrcname,a.cr_jobcd,a.cr_syscd from cmr1010 a, cmm0036 d     \n");
	        strQuery.append("where a.cr_acptno = ? 		  \n");
	        strQuery.append("and a.cr_baseitem =  a.cr_itemid            \n");
	        strQuery.append("and a.cr_syscd = d.cm_syscd \n");
	        strQuery.append("and a.cr_rsrccd = d.cm_rsrccd \n");
	        strQuery.append("and substr(d.cm_info,58,1) = '1' \n");
	        pstmt = conn.prepareStatement(strQuery.toString());
	        pstmt = new LoggableStatement(conn,strQuery.toString());
	        pstmt.setString(1, AcptNo);
			ecamsLogger.error(((LoggableStatement)pstmt).getQueryString());
			rs = pstmt.executeQuery();
	        while (rs.next()) {
	        	strQuery.setLength(0);
	            strQuery.append("select cr_itemid from cmr0020           \n");
	            strQuery.append("where  cr_rsrcname = ?	   				 \n");
	            strQuery.append("and  cr_jobcd = ?		   				 \n");
	            strQuery.append("and  cr_syscd = ?	    				\n");
	            pstmt3 = connD.prepareStatement(strQuery.toString());
	            pstmt3 = new LoggableStatement(connD,strQuery.toString());
		        pstmt3.setString(1, rs.getString("cr_rsrcname"));
		        pstmt3.setString(2, rs.getString("cr_jobcd"));
	            pstmt3.setString(3, rs.getString("cr_syscd"));
	            ecamsLogger.error(((LoggableStatement)pstmt3).getQueryString());
	            rs2 = pstmt3.executeQuery();
	            while (rs2.next()) {
		            strQuery.setLength(0);
		            strQuery.append("update cmr1010 set cr_confno=null           \n");
		            strQuery.append("where cr_confno=?          \n");
		            strQuery.append("and cr_baseitem=?          \n");
		            pstmt2 = connD.prepareStatement(strQuery.toString());
		            pstmt2.setString(1, AcptNo);
		            pstmt2.setString(2, rs2.getString("cr_itemid"));
		            pstmt2.executeUpdate();
		            pstmt2.close();
	            }
	            rs2.close();
	            pstmt3.close();
	        }
	        pstmt.close();
	        rs.close();
            ///////////////////////////////////////////
            conn.commit();
            conn.close();
            connD.close();
			pstmt = null;
			pstmt2 = null;
			pstmt3 = null;
			
			conn = null;

    		return "0";


		} catch (SQLException sqlexception) {
			sqlexception.printStackTrace();
			if (pstmt != null)  try{pstmt.close();}catch (Exception ex2){ex2.printStackTrace();}
			if (pstmt2 != null)  try{pstmt2.close();}catch (Exception ex2){ex2.printStackTrace();}
			if (pstmt3 != null)  try{pstmt3.close();}catch (Exception ex2){ex2.printStackTrace();}
			if (rs != null)     try{rs.close();}catch (Exception ex){ex.printStackTrace();}
			if (rs2 != null)     try{rs2.close();}catch (Exception ex){ex.printStackTrace();}
			if (conn != null){
				try{
					conn.rollback();
					ConnectionResource.release(conn);
				}catch(Exception ex3){
					ecamsLogger.error("## Cmr0250.progCncl() connection release exception ##");
					ex3.printStackTrace();
				}
			}
			if (connD != null){
				try{
					connD.rollback();
					ConnectionResource.release(connD);
				}catch(Exception ex3){
					ecamsLogger.error("## Cmr0250.progCncl() connection release exception ##");
					ex3.printStackTrace();
				}
			}
			ecamsLogger.error("## Cmr0250.progCncl() SQLException START ##");
			ecamsLogger.error("## Error DESC : ", sqlexception);
			ecamsLogger.error("## Cmr0250.progCncl() SQLException END ##");
			throw sqlexception;
		} catch (Exception exception) {
			exception.printStackTrace();
			if (pstmt != null)  try{pstmt.close();}catch (Exception ex2){ex2.printStackTrace();}
			if (rs != null)     try{rs.close();}catch (Exception ex){ex.printStackTrace();}
			if (conn != null){
				try{
					conn.rollback();
					ConnectionResource.release(conn);
				}catch(Exception ex3){
					ecamsLogger.error("## Cmr0250.progCncl() connection release exception ##");
					ex3.printStackTrace();
				}
			}
			if (connD != null){
				try{
					connD.rollback();
					ConnectionResource.release(connD);
				}catch(Exception ex3){
					ecamsLogger.error("## Cmr0250.progCncl() connection release exception ##");
					ex3.printStackTrace();
				}
			}
			ecamsLogger.error("## Cmr0250.progCncl() Exception START ##");
			ecamsLogger.error("## Error DESC : ", exception);
			ecamsLogger.error("## Cmr0250.progCncl() Exception END ##");
			throw exception;
		}finally{
			if (strQuery != null) strQuery = null;
			if (pstmt != null)  try{pstmt.close();}catch (Exception ex2){ex2.printStackTrace();}
			if (conn != null){
				try{
					ConnectionResource.release(conn);
				}catch(Exception ex3){
					ecamsLogger.error("## Cmr0250.progCncl() connection release exception ##");
					ex3.printStackTrace();
				}
			}
			if (connD != null){
				try{
					ConnectionResource.release(connD);
				}catch(Exception ex3){
					ecamsLogger.error("## Cmr0250.progCncl() connection release exception ##");
					ex3.printStackTrace();
				}
			}
		}
	}//end of progCncl() method statement
    
	public String progCncl_sel(String AcptNo,ArrayList<HashMap<String,String>> fileList,String PrcSys) throws SQLException, Exception {
		Connection        conn        = null;
		Connection        connD        = null;
		ResultSet         rs          = null;
		ResultSet         rs2          = null;
		PreparedStatement pstmt       = null;
		PreparedStatement pstmt2      = null;
		PreparedStatement pstmt3      = null;
		StringBuffer      strQuery    = new StringBuffer();
		ConnectionContext connectionContext = new ConnectionResource();
		ConnectionContext connectionContextD = new ConnectionResource(false,"D");
		Runtime  run = null;
		Process p = null;
		

		try {
			String  binpath;
			String[] chkAry;
			int		cmdrtn;
			
			SystemPath systemPath = new SystemPath();
			binpath = systemPath.getTmpDir("14");
			systemPath = null;
			
			run = Runtime.getRuntime();

			chkAry = new String[4];
			chkAry[0] = "/bin/sh";
//			chkAry[1] = "procck2";
			chkAry[1] = binpath+"/procck2"; // 20221219
			chkAry[2] = "ecams_acct";
			chkAry[3] = AcptNo;
			
			p = run.exec(chkAry);
			p.waitFor();

			cmdrtn = p.exitValue();
			if (cmdrtn > 1) {
				//ecamsLogger.error(chkAry[0]+" " + chkAry[1] + " " + chkAry[2]);
				//ecamsLogger.error(cmdrtn);
				return "2";
			}
			conn = connectionContext.getConnection();
			connD = connectionContextD.getConnection();

			conn.setAutoCommit(false);
			connD.setAutoCommit(false);
			for (int i=0;fileList.size()>i;i++) {
//				////////////////자바에딸린 클래스 찾아서 반려
		        strQuery.setLength(0);
		        strQuery.append("select a.cr_itemid from cmr1010 a,cmr0020 b  					\n");
		        strQuery.append(" where a.cr_acptno = ?                          				\n");
		        strQuery.append(" and a.cr_baseitem in (select a.cr_itemid 						\n");
		        strQuery.append("                         from cmr1010 a,cmr0020 b				\n");
		        strQuery.append(" 			  		     where a.cr_acptno= ?                	\n");
		        strQuery.append(" 					       and a.cr_baseitem= ?              	\n");
		        strQuery.append(" 					       and a.cr_syscd=b.cr_syscd         	\n");
		        strQuery.append(" 					       and a.cr_rsrcname = b.cr_rsrcname 	\n");
		        strQuery.append("      				 	   and a.cr_baseitem <> b.cr_itemid  	\n");
		        strQuery.append(" 		     			   and a.cr_rsrccd ='F1'             	\n");
		        strQuery.append(" 			     		   and a.cr_dsncd = b.cr_dsncd       	\n");		        
		        strQuery.append(" 				     	   and a.cr_status <>'3'             	\n");
		        strQuery.append(" 				       )   						            	\n");
		        strQuery.append(" and a.cr_rsrcname = b.cr_rsrcname                         	\n");
		        strQuery.append(" and a.cr_baseitem <> b.cr_itemid                          	\n");
		        strQuery.append(" and a.cr_dsncd = b.cr_dsncd                         			\n");		        
		        pstmt = conn.prepareStatement(strQuery.toString());
		        pstmt = new LoggableStatement(conn,strQuery.toString());
		        pstmt.setString(1, AcptNo);
		        pstmt.setString(2, AcptNo);
		        pstmt.setString(3,fileList.get(i).get("cr_itemid"));
		        ecamsLogger.error(((LoggableStatement)pstmt).getQueryString());
		        rs = pstmt.executeQuery();
		        while (rs.next()) {
		        		   	strQuery.setLength(0);
					    	strQuery.append("update cmr1010 set cr_status='3',cr_prcdate=SYSDATE,\n");
					    	strQuery.append("                   cr_cnclstep=?                    \n");
					    	strQuery.append(" where cr_acptno=? and cr_itemid = ? \n");
					    	strQuery.append("   and cr_status<>'3'                               \n");
					        pstmt3 = conn.prepareStatement(strQuery.toString());
					        pstmt3 = new LoggableStatement(conn,strQuery.toString());
					        pstmt3.setString(1,PrcSys);
					        pstmt3.setString(2,AcptNo);
					        pstmt3.setString(3,rs.getString("cr_itemid"));
					        ecamsLogger.error(((LoggableStatement)pstmt3).getQueryString());
					        pstmt3.executeUpdate();
					        pstmt3.close();
				     
		        	
		        }
		        rs.close();
		        pstmt.close(); 
		        
		        ///////////
				/*
		        strQuery.setLength(0);
		    	strQuery.append("delete cmr1011 where cr_acptno=?                 \n");
		        strQuery.append("   and cr_serno in (select cr_serno from cmr1010 \n");
		        strQuery.append("                     where cr_acptno=?           \n");
		        strQuery.append("                       and cr_baseitem=?)        \n");
		        pstmt = conn.prepareStatement(strQuery.toString());
		        pstmt.setString(1,AcptNo);
		        pstmt.setString(2,AcptNo);
		        pstmt.setString(3,fileList.get(i).get("cr_itemid"));
		        pstmt.executeUpdate();
		        pstmt.close();
                */
		        strQuery.setLength(0);
		    	strQuery.append("update cmr1010 set cr_status='3',cr_prcdate=SYSDATE,\n");
		    	strQuery.append("                   cr_cnclstep=?                    \n");
		    	strQuery.append(" where cr_acptno=? and cr_itemid=?                  \n");
		        pstmt = conn.prepareStatement(strQuery.toString());
		        pstmt.setString(1,PrcSys);
		        pstmt.setString(2,AcptNo);
		        pstmt.setString(3,fileList.get(i).get("cr_itemid"));
		        pstmt.executeUpdate();
		        pstmt.close();
		        
		        strQuery.setLength(0);
		    	strQuery.append("update cmr1010 set cr_confno=''             \n");
	        	strQuery.append(" where cr_confno=? and instr(nvl(cr_basepgm,cr_baseitem),?)>0 \n");
		    	strQuery.append("   and cr_acptno<>cr_confno                 \n");
		        pstmt = conn.prepareStatement(strQuery.toString());
		        pstmt.setString(1,AcptNo);
		        pstmt.setString(2,fileList.get(i).get("cr_itemid"));
		        pstmt.executeUpdate();
		        pstmt.close();
		        
		        strQuery.setLength(0);
		        strQuery.append("select count(*) cnt from cmr1010 a,cmr1010 b        \n");
		        strQuery.append(" where a.cr_acptno=? and instr(nvl(a.cr_basepgm,a.cr_baseitem),?)>0  \n");
		        strQuery.append("   and a.cr_acptno=b.cr_acptno                      \n");
		        strQuery.append("   and instr(nvl(a.cr_basepgm,a.cr_baseitem),b.cr_itemid)>0 \n");
		        strQuery.append("   and b.cr_status<>'3'                             \n");
		        pstmt = conn.prepareStatement(strQuery.toString());
		        pstmt.setString(1, AcptNo);
		        pstmt.setString(2, fileList.get(i).get("cr_itemid"));
		        rs = pstmt.executeQuery();
		        if (rs.next()) {
		        	if (rs.getInt("cnt")== 0) {
		        		strQuery.setLength(0);
				    	strQuery.append("update cmr1010 set cr_status='3',cr_prcdate=SYSDATE,\n");
				    	strQuery.append("                   cr_cnclstep=?                    \n");
				    	strQuery.append(" where cr_acptno=? and instr(nvl(cr_basepgm,cr_baseitem),?)>0 \n");
				    	strQuery.append("   and cr_status<>'3'                               \n");
				        pstmt2 = conn.prepareStatement(strQuery.toString());
				        pstmt2.setString(1,PrcSys);
				        pstmt2.setString(2,AcptNo);
				        pstmt2.setString(3,fileList.get(i).get("cr_itemid"));
				        pstmt2.executeUpdate();
				        pstmt2.close();
		        	}
		        }
		        rs.close();
		        pstmt.close();
		        
		        strQuery.setLength(0);
		        strQuery.append("update cmr1000 set cr_cnclstep=?,           \n");
		        strQuery.append("       cr_cncllstd=SYSDATE,cr_retryyn='N'   \n");
		        strQuery.append(" where cr_acptno=?                          \n");
		        pstmt = conn.prepareStatement(strQuery.toString());
		        pstmt.setString(1, PrcSys);
		        pstmt.setString(2, AcptNo);
		        pstmt.executeUpdate();
		        pstmt.close();
		        
		        
		        
		       	//====================================================
	            /////////////////////////////////////차세대DB 컨프넘버 삭제///////////
	        	
		        strQuery.setLength(0);
	            strQuery.append("select a.cr_rsrcname,a.cr_jobcd,a.cr_syscd from cmr1010 a, cmm0036 d     \n");
		        strQuery.append("where a.cr_acptno = ? 		  \n");
		        strQuery.append("and a.cr_baseitem =  a.cr_itemid            \n");
		        strQuery.append("and a.cr_syscd = d.cm_syscd \n");
		        strQuery.append("and a.cr_rsrccd = d.cm_rsrccd \n");
		        strQuery.append("and substr(d.cm_info,58,1) = '1' \n");
		        pstmt = conn.prepareStatement(strQuery.toString());
		        pstmt = new LoggableStatement(conn,strQuery.toString());
		        pstmt.setString(1, AcptNo);
				ecamsLogger.error(((LoggableStatement)pstmt).getQueryString());
				rs = pstmt.executeQuery();
		        while (rs.next()) {
		        	strQuery.setLength(0);
		            strQuery.append("select cr_itemid from cmr0020           \n");
		            strQuery.append("where  cr_rsrcname = ?	   				 \n");
		            strQuery.append("and  cr_jobcd = ?		   				 \n");
		            strQuery.append("and  cr_syscd = ?	    				\n");
		            pstmt3 = connD.prepareStatement(strQuery.toString());
		            pstmt3 = new LoggableStatement(connD,strQuery.toString());
			        pstmt3.setString(1, rs.getString("cr_rsrcname"));
			        pstmt3.setString(2, rs.getString("cr_jobcd"));
		            pstmt3.setString(3, rs.getString("cr_syscd"));
		            ecamsLogger.error(((LoggableStatement)pstmt3).getQueryString());
		            rs2 = pstmt3.executeQuery();
		            while (rs2.next()) {
			            strQuery.setLength(0);
			            strQuery.append("update cmr1010 set cr_confno=null           \n");
			            strQuery.append("where cr_confno=?          \n");
			            strQuery.append("and cr_baseitem=?          \n");
			            pstmt2 = connD.prepareStatement(strQuery.toString());
			            pstmt2.setString(1, AcptNo);
			            pstmt2.setString(2, rs2.getString("cr_itemid"));
			            pstmt2.executeUpdate();
			            pstmt2.close();
		            }
		            rs2.close();
		            pstmt3.close();
		        }
		        pstmt.close();
		        rs.close();
	        	
	        	//====================================================   
			}
	        conn.commit();
	        connD.commit();
	        conn.close();
	        connD.close();
			pstmt = null;
			pstmt2 = null;
			pstmt3 = null;
			conn = null;

			return "0";


		} catch (SQLException sqlexception) {
			sqlexception.printStackTrace();
			if (pstmt != null)  try{pstmt.close();}catch (Exception ex2){ex2.printStackTrace();}
			if (pstmt2 != null)  try{pstmt2.close();}catch (Exception ex2){ex2.printStackTrace();}
			if (pstmt3 != null)  try{pstmt3.close();}catch (Exception ex2){ex2.printStackTrace();}
			if (rs != null)     try{rs.close();}catch (Exception ex){ex.printStackTrace();}
			if (rs2 != null)     try{rs2.close();}catch (Exception ex){ex.printStackTrace();}
			if (conn != null){
				try{
					conn.rollback();
					ConnectionResource.release(conn);
				}catch(Exception ex3){
					ecamsLogger.error("## Cmr0250.progCncl_sel() connection release exception ##");
					ex3.printStackTrace();
				}
			}
			if (connD != null){
				try{
					connD.rollback();
					ConnectionResource.release(connD);
				}catch(Exception ex3){
					ecamsLogger.error("## Cmr0250.progCncl_sel() connection release exception ##");
					ex3.printStackTrace();
				}
			}
			ecamsLogger.error("## Cmr0250.progCncl_sel() SQLException START ##");
			ecamsLogger.error("## Error DESC : ", sqlexception);
			ecamsLogger.error("## Cmr0250.progCncl_sel() SQLException END ##");
			throw sqlexception;
		} catch (Exception exception) {
			exception.printStackTrace();
			if (pstmt != null)  try{pstmt.close();}catch (Exception ex2){ex2.printStackTrace();}
			if (pstmt2 != null)  try{pstmt2.close();}catch (Exception ex2){ex2.printStackTrace();}
			if (rs != null)     try{rs.close();}catch (Exception ex){ex.printStackTrace();}
			if (conn != null){
				try{
					conn.rollback();
					ConnectionResource.release(conn);
				}catch(Exception ex3){
					ecamsLogger.error("## Cmr0250.progCncl_sel() connection release exception ##");
					ex3.printStackTrace();
				}
			}
			if (connD != null){
				try{
					connD.rollback();
					ConnectionResource.release(connD);
				}catch(Exception ex3){
					ecamsLogger.error("## Cmr0250.progCncl_sel() connection release exception ##");
					ex3.printStackTrace();
				}
			}
			ecamsLogger.error("## Cmr0250.progCncl_sel() Exception START ##");
			ecamsLogger.error("## Error DESC : ", exception);
			ecamsLogger.error("## Cmr0250.progCncl_sel() Exception END ##");
			throw exception;
		}finally{
			if (strQuery != null) strQuery = null;
			if (pstmt != null)  try{pstmt.close();}catch (Exception ex2){ex2.printStackTrace();}
			if (pstmt2 != null)  try{pstmt2.close();}catch (Exception ex2){ex2.printStackTrace();}
			if (rs != null)     try{rs.close();}catch (Exception ex){ex.printStackTrace();}
			if (conn != null){
				try{
					ConnectionResource.release(conn);
				}catch(Exception ex3){
					ecamsLogger.error("## Cmr0250.progCncl_sel() connection release exception ##");
					ex3.printStackTrace();
				}
			}
			if (connD != null){
				try{
					ConnectionResource.release(connD);
				}catch(Exception ex3){
					ecamsLogger.error("## Cmr0250.progCncl_sel() connection release exception ##");
					ex3.printStackTrace();
				}
			}
		}
	}//end of progCncl_sel() method statement
	
	public String progCncl_Mapping(String AcptNo) throws SQLException, Exception {
		Connection        conn        = null;
		ResultSet         rs          = null;
		PreparedStatement pstmt       = null;
		PreparedStatement pstmt2       = null;
		StringBuffer      strQuery    = new StringBuffer();
		ConnectionContext connectionContext = new ConnectionResource();
		Runtime  run = null;
		Process p = null;
		

		try {
			//ecamsLogger.debug("+++++++++prcCd========"+prcCd);
			String  binpath;
			String[] chkAry;
			int		cmdrtn;
			
			SystemPath systemPath = new SystemPath();
			binpath = systemPath.getTmpDir("14");
			systemPath = null;
			/*
			run = Runtime.getRuntime();

			chkAry = new String[3];
			chkAry[0] = binpath+"/procck2";
			chkAry[1] = "ecams_acct";
			chkAry[2] = AcptNo;

			p = run.exec(chkAry);
			p.waitFor();

			cmdrtn = p.exitValue();
			if (cmdrtn > 1) {
				//ecamsLogger.error(chkAry[0]+" " + chkAry[1] + " " + chkAry[2]);
				//ecamsLogger.error(cmdrtn);
				return "2";
			}
            */
			conn = connectionContext.getConnection();

			conn.setAutoCommit(false);
	        
            strQuery.setLength(0);
        	strQuery.append("update cmr1010 set          \n");
        	strQuery.append("       cr_status='3',       \n");
        	strQuery.append("       cr_prcdate=SYSDATE,  \n");
        	strQuery.append("       cr_cnclstep='SYSUP'  \n");
        	strQuery.append(" where cr_acptno=?          \n");
        	strQuery.append("   and cr_rsrccd='92'       \n");
        	strQuery.append("   and nvl(cr_putcode,'0000')<>'0000' \n");
            pstmt = conn.prepareStatement(strQuery.toString());
            pstmt.setString(1,AcptNo);
            pstmt.executeUpdate();
            pstmt.close();
	        
	        strQuery.setLength(0);
	        strQuery.append("select count(*) cnt from cmr1010      \n");
	        strQuery.append(" where cr_acptno=?                    \n");
	        strQuery.append("   and cr_status='0'                  \n");
	        strQuery.append("   and nvl(cr_putcode,'0000')<>'0000' \n");
	        pstmt = conn.prepareStatement(strQuery.toString());
	        pstmt.setString(1, AcptNo);
	        rs = pstmt.executeQuery();
	        if (rs.next()) {
	        	if (rs.getInt("cnt")== 0) {
	        		strQuery.setLength(0);
			    	strQuery.append("update cmr1010 set    \n");
			    	strQuery.append("       cr_putcode='', \n");
			    	strQuery.append("       cr_pid=''      \n");
			    	strQuery.append(" where cr_acptno=?    \n");
			    	strQuery.append("   and cr_status='0'  \n");
			        pstmt2 = conn.prepareStatement(strQuery.toString());
			        pstmt2.setString(1,AcptNo);
			        pstmt2.executeUpdate();
			        pstmt2.close();
	        	} else {
	        		strQuery.setLength(0);
	                strQuery.append("update cmr1000 set cr_cnclstep='SYSUP',     \n");
	                strQuery.append("       cr_cncllstd=SYSDATE,cr_retryyn='N'   \n");
	                strQuery.append(" where cr_acptno=?                          \n");
	                pstmt2 = conn.prepareStatement(strQuery.toString());
	                pstmt2.setString(1, AcptNo);
	                pstmt2.executeUpdate();
	                pstmt2.close();
	        	}
	        }
	        rs.close();
	        pstmt.close();
	        
            

            conn.commit();
            conn.close();
			pstmt = null;
			pstmt2 = null;
			rs = null;
			conn = null;

    		return "0";


		} catch (SQLException sqlexception) {
			sqlexception.printStackTrace();
			if (pstmt != null)  try{pstmt.close();}catch (Exception ex2){ex2.printStackTrace();}
			if (rs != null)     try{rs.close();}catch (Exception ex){ex.printStackTrace();}
			if (conn != null){
				try{
					conn.rollback();
					ConnectionResource.release(conn);
				}catch(Exception ex3){
					ecamsLogger.error("## Cmr0250.progCncl_Mapping() connection release exception ##");
					ex3.printStackTrace();
				}
			}
			ecamsLogger.error("## Cmr0250.progCncl_Mapping() SQLException START ##");
			ecamsLogger.error("## Error DESC : ", sqlexception);
			ecamsLogger.error("## Cmr0250.progCncl_Mapping() SQLException END ##");
			throw sqlexception;
		} catch (Exception exception) {
			exception.printStackTrace();
			if (pstmt != null)  try{pstmt.close();}catch (Exception ex2){ex2.printStackTrace();}
			if (rs != null)     try{rs.close();}catch (Exception ex){ex.printStackTrace();}
			if (conn != null){
				try{
					conn.rollback();
					ConnectionResource.release(conn);
				}catch(Exception ex3){
					ecamsLogger.error("## Cmr0250.progCncl_Mapping() connection release exception ##");
					ex3.printStackTrace();
				}
			}
			ecamsLogger.error("## Cmr0250.progCncl_Mapping() Exception START ##");
			ecamsLogger.error("## Error DESC : ", exception);
			ecamsLogger.error("## Cmr0250.progCncl_Mapping() Exception END ##");
			throw exception;
		}finally{
			if (strQuery != null) strQuery = null;
			if (pstmt != null)  try{pstmt.close();}catch (Exception ex2){ex2.printStackTrace();}
			if (conn != null){
				try{
					ConnectionResource.release(conn);
				}catch(Exception ex3){
					ecamsLogger.error("## Cmr0250.progCncl_Mapping() connection release exception ##");
					ex3.printStackTrace();
				}
			}
		}
	}//end of progCncl_Mapping() method statement
	
	public Object[] getProgDetail(String AcptNo, String ItemId) throws SQLException, Exception {
			Connection        conn        = null;
			PreparedStatement pstmt       = null;
			ResultSet         rs          = null;
			StringBuffer      strQuery    = new StringBuffer();
			String			  Result	  = "";
			ArrayList<HashMap<String, String>>  rsval = new ArrayList<HashMap<String, String>>();
			HashMap<String, String>			  rst		  = null;
			Object[] 		  returnObject = null;
			

			ConnectionContext connectionContext = new ConnectionResource();
			
			try {
				conn = connectionContext.getConnection();
				
				strQuery.setLength(0);
				strQuery.append("select a.cr_testyn, a.cr_rsrcname, b.cr_sayu, a.cr_qrycd, b.cr_devptime, b.cr_editor, 			\n");
				strQuery.append("						a.cr_jobcd, a.cr_important, a.cr_newglo, a.cr_dealcode, c.cr_itemid, 	\n");			
				strQuery.append("						a.cr_syscd, a.cr_aplydate, b.cr_status, a.cr_editcon , a.cr_expday, a.cr_status, a.cr_compdate		\n");		  
				strQuery.append("					from cmr1010 a, cmr1000 b, cmr0020 c										\n");
				strQuery.append("							      where a.cr_acptno = ?											\n");	
				strQuery.append("				                   and a.cr_acptno = b.cr_acptno								\n");
				strQuery.append("                                   and a.cr_itemid = ?											\n");
				strQuery.append("									and a.cr_itemid = c.cr_itemid								\n");
				strQuery.append("								    and c.cr_rsrcname = a.cr_rsrcname							\n");
		
				pstmt = conn.prepareStatement(strQuery.toString());	
				//pstmt = new LoggableStatement(conn,strQuery.toString());
				pstmt.setString(1,AcptNo);
				pstmt.setString(2, ItemId);
		        //ecamsLogger.error(((LoggableStatement)pstmt).getQueryString());
		        rs = pstmt.executeQuery();
				while (rs.next()){
					rst = new HashMap<String,String>();
					//rst.put("cr_open", rs.getString("cr_open"));
					rst.put("cr_testyn", rs.getString("cr_testyn"));
					rst.put("cr_rsrcname", rs.getString("cr_rsrcname"));
					//rst.put("cr_deploy", rs.getString("cr_deploy"));
					rst.put("cr_sayu", rs.getString("cr_sayu"));
					rst.put("cr_jobcd", rs.getString("cr_jobcd"));
					rst.put("cr_syscd", rs.getString("cr_syscd"));
					rst.put("cr_important", rs.getString("cr_important"));
					rst.put("cr_newglo", rs.getString("cr_newglo"));
					rst.put("cr_dealcode", rs.getString("cr_dealcode"));
					rst.put("cr_aplydate", rs.getString("cr_aplydate"));
					rst.put("cr_qrycd", rs.getString("cr_qrycd"));
					rst.put("cr_devptime", rs.getString("cr_devptime"));
					rst.put("cr_itemid", rs.getString("cr_itemid"));
					rst.put("cr_status", rs.getString("cr_status"));
					rst.put("cr_editcon", rs.getString("cr_editcon"));
					rst.put("cr_expday", rs.getString("cr_expday"));
					rst.put("cr_editor", rs.getString("cr_editor"));
					rst.put("cr_status", rs.getString("cr_status"));
					rst.put("cr_compdate", rs.getString("cr_compdate"));
					//if(rs.getString("cr_team").equals("Q1") && rs.getString("cr_status").equals("0")){
						//Result = "YES";
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
				ecamsLogger.error("## Cmr0250.getProgDetail() SQLException START ##");
				ecamsLogger.error("## Error DESC : ", sqlexception);
				ecamsLogger.error("## Cmr0250.getProgDetail() SQLException END ##");
				throw sqlexception;
			} catch (Exception exception) {
				exception.printStackTrace();
				ecamsLogger.error("## Cmr0250.getProgDetail() Exception START ##");
				ecamsLogger.error("## Error DESC : ", exception);
				ecamsLogger.error("## Cmr0250.getProgDetail() Exception END ##");
				throw exception;
			}finally{
				if (strQuery != null) strQuery = null;
				if (rs != null)     try{rs.close();}catch (Exception ex){ex.printStackTrace();}
				if (pstmt != null)  try{pstmt.close();}catch (Exception ex2){ex2.printStackTrace();}
				if (conn != null){
					try{
						ConnectionResource.release(conn);
					}catch(Exception ex3){
						ecamsLogger.error("## Cmr0250.getProgDetail() connection release exception ##");
						ex3.printStackTrace();
					}
				}
			}
		
	}  

	public String setProgDetail(String AcptNo, String ItemId, HashMap<String,String> etcData1 , ArrayList<HashMap<String,String>> etcData2 ) throws SQLException, Exception {
	
		Connection        conn        = null;
		PreparedStatement pstmt       = null;
		ResultSet         rs          = null;
		StringBuffer      strQuery    = new StringBuffer();
		String			Result		="No";
		int				pstmtcount = 1;
		
		ConnectionContext connectionContext = new ConnectionResource();
		
		try {
			conn = connectionContext.getConnection();
			
			strQuery.setLength(0);
			strQuery.append("select count(cr_acptno)  																	\n"); 
			strQuery.append("	from  cmr1010  																			\n");
			strQuery.append("		where cr_acptno = ?																	\n"); 
			strQuery.append("		and cr_itemid = ?																	\n"); 

			pstmt = conn.prepareStatement(strQuery.toString());
			pstmt.setString(1, AcptNo);
			pstmt.setString(2, ItemId);
			rs = pstmt.executeQuery();
			if(rs.next()){
				pstmt.close();
				
				strQuery.setLength(0);
				strQuery.append("update cmr1010 a																		\n");
				strQuery.append("	set a.cr_aplydate = ?, a.cr_testyn = ? ,											\n");
				strQuery.append("		 a.cr_important = ?, a.cr_newglo = ?, a.cr_dealcode = ?, a.cr_editcon = ?, a.cr_compdate = ?		\n");
				strQuery.append("			where a.cr_acptno = ?														\n");
				strQuery.append("				and a.cr_itemid = ?														\n");
				pstmt = conn.prepareStatement(strQuery.toString());
				//pstmt = new LoggableStatement(conn,strQuery.toString());
				pstmt.setString(1,etcData1.get("aplydate"));
				pstmt.setString(2,etcData1.get("testyn"));
				pstmt.setString(3, etcData1.get("important"));
				pstmt.setString(4, etcData1.get("newglo"));
				pstmt.setString(5, etcData1.get("dealcode"));
				pstmt.setString(6, etcData1.get("editcon"));
				pstmt.setString(7, etcData1.get("compdate"));
				pstmt.setString(8, AcptNo);
				pstmt.setString(9, ItemId);
				
				//ecamsLogger.error(((LoggableStatement)pstmt).getQueryString());
				pstmt.executeUpdate();
					
				rs.close();
	        	pstmt.close();
			}
			strQuery.setLength(0);
    		strQuery.append("select * 																				\n");
    		strQuery.append("	from cmr1012  																		\n");
    		strQuery.append("		where cr_acptno = ? 															\n");
			strQuery.append("	      and cr_itemid = ?												       		    \n");    	
    		pstmt = conn.prepareStatement(strQuery.toString());
    		pstmt.setString(1, AcptNo);	   
			pstmt.setString(2, ItemId);     		
    		rs = pstmt.executeQuery();
			
			if(rs.next()){
				rs.close();
				pstmt.close();
				strQuery.setLength(0);
				//    strQuery.append("delete cmr1010 where cr_acptno=? and cr_baseitem=?    \n");
				strQuery.append("delete cmr1012  																	\n");
				strQuery.append("		where cr_acptno = ?															\n");
				strQuery.append("	      and cr_itemid = ?												       		    \n");   
				pstmt = conn.prepareStatement(strQuery.toString());
				pstmt.setString(1, AcptNo);
				pstmt.setString(2, ItemId);
        		pstmt.executeUpdate();
			}
		
			int i = 0;
	        for(i=0;i<etcData2.size();i++)
        	{
        		
        		rs.close();
				pstmt.close();
				strQuery.setLength(0);
				strQuery.append("insert into cmr1012  																	\n");
				strQuery.append("		(cr_acptno, cr_seq, cr_reqsub, cr_orderid, cr_itemid) values					\n");
				strQuery.append("		(? , (select nvl(max(cr_seq)+1,0) from cmr1012 where cr_acptno = ?) , ? , ? , ?)  \n");
				pstmt = conn.prepareStatement(strQuery.toString());
				pstmt = new LoggableStatement(conn,strQuery.toString());
				pstmt.setString(1, AcptNo);
				pstmt.setString(2, AcptNo);				
        		pstmt.setString(3, etcData2.get(i).get("reqsub"));
        		pstmt.setString(4, etcData2.get(i).get("orderid"));
        		pstmt.setString(5, ItemId);
				ecamsLogger.error(((LoggableStatement)pstmt).getQueryString());
				pstmt.executeUpdate();
				
        	}
        	
	        if(i==etcData2.size())
	        	Result = "Yes";
		
		    conn.close();

			pstmt = null;
			conn = null;
			
			return Result;				

		} catch (SQLException sqlexception) {
			sqlexception.printStackTrace();
			ecamsLogger.error("## Cmr0250.setProgDetail() SQLException START ##");
			ecamsLogger.error("## Error DESC : ", sqlexception);
			ecamsLogger.error("## Cmr0250.setProgDetail() SQLException END ##");
			throw sqlexception;
		} catch (Exception exception) {
			exception.printStackTrace();
			ecamsLogger.error("## Cmr0250.setProgDetail() Exception START ##");
			ecamsLogger.error("## Error DESC : ", exception);
			ecamsLogger.error("## Cmr0250.setProgDetail() Exception END ##");
			throw exception;
		}finally{
			if (strQuery != null) strQuery = null;
			if (rs != null)     try{rs.close();}catch (Exception ex){ex.printStackTrace();}
			if (pstmt != null)  try{pstmt.close();}catch (Exception ex2){ex2.printStackTrace();}
			if (conn != null){
				try{
					ConnectionResource.release(conn);
				}catch(Exception ex3){
					ecamsLogger.error("## Cmr0250.setProgDetail() connection release exception ##");
					ex3.printStackTrace();
				}
			}
		}
	}
	
	public Object[] getOrders(String AcptNo, String ItemId) throws SQLException, Exception {
		Connection        conn        = null;
		PreparedStatement pstmt       = null;
		ResultSet         rs          = null;
		StringBuffer      strQuery    = new StringBuffer();
		String			  Result	  = "";
		ArrayList<HashMap<String, String>>  rsval = new ArrayList<HashMap<String, String>>();
		HashMap<String, String>			  rst		  = null;
		Object[] 		  returnObject = null;
		

		ConnectionContext connectionContext = new ConnectionResource();
			
		try {
			conn = connectionContext.getConnection();
			
			strQuery.setLength(0);
				
			strQuery.append("select a.cr_acptno, a.cr_itemid, b.cc_orderid, \n"); 
			strQuery.append("	b.cc_reqsub, b.cc_reqid 					\n"); 	
			strQuery.append("from   cmr1012 a, cmc0420 b 					\n");
			strQuery.append("where  cr_acptno = ?							\n");				
			strQuery.append("and    cr_itemid =? 							\n"); 					
			strQuery.append("and    a.cr_orderid=b.cc_orderid  				\n"); 		
			strQuery.append("order by a.cr_acptno								\n");
		
			pstmt = conn.prepareStatement(strQuery.toString());	
			//pstmt = new LoggableStatement(conn,strQuery.toString());	
			pstmt.setString(1,AcptNo);
			pstmt.setString(2,ItemId);
	       // //ecamsLogger.error(((LoggableStatement)pstmt).getQueryString());
		
	        rs = pstmt.executeQuery();
			while (rs.next()){
				rst = new HashMap<String,String>();
				rst.put("acptno", rs.getString("cr_acptno"));
				rst.put("itmeid", rs.getString("cr_itemid"));
				rst.put("orderid", rs.getString("cc_orderid"));
				rst.put("reqsub", rs.getString("cc_reqsub"));
				rst.put("reqid", rs.getString("cc_reqid"));
				
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
			ecamsLogger.error("## Cmr0250.getOrders() SQLException START ##");
			ecamsLogger.error("## Error DESC : ", sqlexception);
			ecamsLogger.error("## Cmr0250.getOrders() SQLException END ##");
			throw sqlexception;
		} catch (Exception exception) {
			exception.printStackTrace();
			ecamsLogger.error("## Cmr0250.getOrders() Exception START ##");
			ecamsLogger.error("## Error DESC : ", exception);
			ecamsLogger.error("## Cmr0250.getOrders() Exception END ##");
			throw exception;
		}finally{
			if (strQuery != null) strQuery = null;
			if (rs != null)     try{rs.close();}catch (Exception ex){ex.printStackTrace();}
			if (pstmt != null)  try{pstmt.close();}catch (Exception ex2){ex2.printStackTrace();}
			if (conn != null){
				try{
					ConnectionResource.release(conn);
				}catch(Exception ex3){
					ecamsLogger.error("## Cmr0250.getOrders() connection release exception ##");
					ex3.printStackTrace();
				}
			}
		}
	
	}
	
	public Object[] getOrders2(String orderName) throws SQLException, Exception {
		Connection        conn        = null;
		PreparedStatement pstmt       = null;
		ResultSet         rs          = null;
		StringBuffer      strQuery    = new StringBuffer();
		String			  Result	  = "";
		ArrayList<HashMap<String, String>>  rsval = new ArrayList<HashMap<String, String>>();
		HashMap<String, String>			  rst		  = null;
		Object[] 		  returnObject = null;
		

		ConnectionContext connectionContext = new ConnectionResource();
			
		try {
			conn = connectionContext.getConnection();
			
			strQuery.setLength(0);
			strQuery.append("select cc_orderid, cc_reqid, cc_reqsub from cmc0420	\n");
			strQuery.append(" 	where cc_status not in('3','9') 					\n");
			if(!"".equals(orderName) && orderName != null) {
				strQuery.append(" 	and  upper(cc_reqsub) like upper(?) ");
			}
			
			pstmt = conn.prepareStatement(strQuery.toString());	
			//pstmt =  new LoggableStatement(conn, strQuery.toString());
			
			if(!"".equals(orderName) && orderName != null) {
				pstmt.setString(1, "%"+orderName+"%");
			}
			
			//ecamsLogger.error(((LoggableStatement)pstmt).getQueryString());
	        rs = pstmt.executeQuery();
			while (rs.next()){
				rst = new HashMap<String,String>();
				rst.put("orderid", rs.getString("cc_orderid"));
				rst.put("reqsub", rs.getString("cc_reqsub"));
				rst.put("reqid", rs.getString("cc_reqid"));
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
			ecamsLogger.error("## Cmr0250.getOrders2() SQLException START ##");
			ecamsLogger.error("## Error DESC : ", sqlexception);
			ecamsLogger.error("## Cmr0250.getOrders2() SQLException END ##");
			throw sqlexception;
		} catch (Exception exception) {
			exception.printStackTrace();
			ecamsLogger.error("## Cmr0250.getOrders2() Exception START ##");
			ecamsLogger.error("## Error DESC : ", exception);
			ecamsLogger.error("## Cmr0250.getOrders2() Exception END ##");
			throw exception;
		}finally{
			if (strQuery != null) strQuery = null;
			if (rs != null)     try{rs.close();}catch (Exception ex){ex.printStackTrace();}
			if (pstmt != null)  try{pstmt.close();}catch (Exception ex2){ex2.printStackTrace();}
			if (conn != null){
				try{
					ConnectionResource.release(conn);
				}catch(Exception ex3){
					ecamsLogger.error("## Cmr0250.getOrders2() connection release exception ##");
					ex3.printStackTrace();
				}
			}
		}
	
	}
	
	public String getTeamCD(String AcptNo) throws SQLException, Exception {
		Connection        conn        = null;
		PreparedStatement pstmt       = null;
		ResultSet         rs          = null;
		StringBuffer      strQuery    = new StringBuffer();
		String 		 	  teamCd	  = "";
		Object[] 		  returnObject = null;
		

		ConnectionContext connectionContext = new ConnectionResource();
			
		try {
			conn = connectionContext.getConnection();
			
			strQuery.setLength(0);
			strQuery.append("select cr_teamcd from cmr9900 where cr_acptno = ? and cr_locat = '00' \n");
			
			pstmt = conn.prepareStatement(strQuery.toString());	
			//pstmt = new LoggableStatement(conn,strQuery.toString());
			pstmt.setString(1,AcptNo);
		   // //ecamsLogger.error(((LoggableStatement)pstmt).getQueryString());
	        rs = pstmt.executeQuery();
	        if(rs.next()){
	           teamCd = rs.getString("cr_teamcd");  
	        }
	        
	        rs.close();
			pstmt.close();
					
			conn.close();

			rs = null;
			pstmt = null;
			conn = null;
	    	
	    	
			return teamCd;

		} catch (SQLException sqlexception) {
			sqlexception.printStackTrace();
			ecamsLogger.error("## Cmr0250.getProgDetail() SQLException START ##");
			ecamsLogger.error("## Error DESC : ", sqlexception);
			ecamsLogger.error("## Cmr0250.getProgDetail() SQLException END ##");
			throw sqlexception;
		} catch (Exception exception) {
			exception.printStackTrace();
			ecamsLogger.error("## Cmr0250.getProgDetail() Exception START ##");
			ecamsLogger.error("## Error DESC : ", exception);
			ecamsLogger.error("## Cmr0250.getProgDetail() Exception END ##");
			throw exception;
		}finally{
			if (strQuery != null) strQuery = null;
			if (rs != null)     try{rs.close();}catch (Exception ex){ex.printStackTrace();}
			if (pstmt != null)  try{pstmt.close();}catch (Exception ex2){ex2.printStackTrace();}
			if (conn != null){
				try{
					ConnectionResource.release(conn);
				}catch(Exception ex3){
					ecamsLogger.error("## Cmr0250.getProgDetail() connection release exception ##");
					ex3.printStackTrace();
				}
			}
		}
	
	}
	
	public Object[] getReqs(String OrderId) throws SQLException, Exception {
		
		Connection        conn        = null;
		PreparedStatement pstmt       = null;
		ResultSet         rs          = null;
		StringBuffer      strQuery    = new StringBuffer();
		String			  Result	  = "";
		ArrayList<HashMap<String, String>>  rsval = new ArrayList<HashMap<String, String>>();
		HashMap<String, String>			  rst		  = null;
		Object[] 		  returnObject = null;
		

		ConnectionContext connectionContext = new ConnectionResource();
			
		try {
			conn = connectionContext.getConnection();
			
			strQuery.setLength(0);
			strQuery.append("select a.cc_orderid,b.cc_docnum,b.cc_reqid,b.cc_docsubj,b.cc_doctype from cmc0420 a, cmc0400 b 					\n"); 
			strQuery.append("		where a.cc_orderid = ? and a.cc_reqid is not null and a.cc_reqid = b.cc_reqid 		\n");
			pstmt = conn.prepareStatement(strQuery.toString());	
			//pstmt = new LoggableStatement(conn,strQuery.toString());	
			pstmt.setString(1,OrderId);
		
	       // //ecamsLogger.error(((LoggableStatement)pstmt).getQueryString());
	        rs = pstmt.executeQuery();
			while (rs.next()){
				rst = new HashMap<String,String>();
				rst.put("cc_reqid", rs.getString("cc_reqid"));
				rst.put("cc_orderid", rs.getString("cc_orderid"));
				rst.put("cc_docsubj", rs.getString("cc_docsubj"));
				rst.put("cc_doctype", rs.getString("cc_doctype"));
				rst.put("cc_docnum", rs.getString("cc_docnum"));
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
			ecamsLogger.error("## Cmr0250.getReqs() SQLException START ##");
			ecamsLogger.error("## Error DESC : ", sqlexception);
			ecamsLogger.error("## Cmr0250.getReqs() SQLException END ##");
			throw sqlexception;
		} catch (Exception exception) {
			exception.printStackTrace();
			ecamsLogger.error("## Cmr0250.getReqs() Exception START ##");
			ecamsLogger.error("## Error DESC : ", exception);
			ecamsLogger.error("## Cmr0250.getReqs() Exception END ##");
			throw exception;
		}finally{
			if (strQuery != null) strQuery = null;
			if (rs != null)     try{rs.close();}catch (Exception ex){ex.printStackTrace();}
			if (pstmt != null)  try{pstmt.close();}catch (Exception ex2){ex2.printStackTrace();}
			if (conn != null){
				try{
					ConnectionResource.release(conn);
				}catch(Exception ex3){
					ecamsLogger.error("## Cmr0250.getReqs() connection release exception ##");
					ex3.printStackTrace();
				}
			}
		}
	
	}

	
	public Object[] getSRReqList(HashMap<String,String> etcData) throws SQLException, Exception {
		
		Connection        conn        = null;
		PreparedStatement pstmt       = null;
		ResultSet         rs          = null;
		StringBuffer      strQuery    = new StringBuffer();
		ArrayList<HashMap<String, String>>  rsval = new ArrayList<HashMap<String, String>>();
		HashMap<String, String>			  rst		  = null;
		Object[] 		  returnObject = null;
		boolean           testSw = false;

		ConnectionContext connectionContext = new ConnectionResource();
			
		try {
			conn = connectionContext.getConnection();
			
			strQuery.setLength(0);
			strQuery.append("select a.cc_srreqid,a.cc_docsubj,              \n");
			strQuery.append("       nvl(a.cc_testreq_yn,'X') cc_testreq_yn, \n");
			strQuery.append("       a.cc_testpass_cd,a.cc_jobdif,  		    \n"); 
			strQuery.append("       (select cm_codename from cmm0020 where cm_macode ='PASSTYPE' and cm_micode = a.cc_testpass_cd) testpass, \n"); 
			strQuery.append("       (select cm_codename from cmm0020 where cm_macode ='JOBGRADE' and cm_micode = a.cc_jobdif) jobdif		 \n"); 
			strQuery.append("  from cmc0400 a,  			                \n"); 
			strQuery.append("       (select x.cc_reqid                      \n");
			strQuery.append("          from cmc0420 x,cmr1012 y             \n"); 
			strQuery.append("         where y.cr_acptno=?                   \n"); 
			strQuery.append("           and y.cr_orderid=x.cc_orderid       \n"); 
			strQuery.append("           and x.cc_reqid is not null          \n"); 
			strQuery.append("         group by x.cc_reqid) b                \n"); 
			strQuery.append(" where b.cc_reqid=a.cc_reqid	                \n");
			strQuery.append(" order by a.cc_srreqid        					\n");
			pstmt = conn.prepareStatement(strQuery.toString());	
			pstmt = new LoggableStatement(conn,strQuery.toString());	
			pstmt.setString(1,etcData.get("acptno"));		
	        ecamsLogger.error(((LoggableStatement)pstmt).getQueryString());
	        rs = pstmt.executeQuery();
			while (rs.next()){
				rst = new HashMap<String,String>();
				rst.put("baseyn", "N");
				rst.put("cc_srreqid", rs.getString("cc_srreqid"));
				rst.put("cc_docsubj", rs.getString("cc_docsubj"));
				rst.put("cc_testreq_yn", rs.getString("cc_testreq_yn"));				
				if (rs.getString("cc_testreq_yn") != null && "Y".equals(rs.getString("cc_testreq_yn"))) testSw = true;
				rst.put("cc_testpass_cd", rs.getString("cc_testpass_cd"));
				rst.put("cc_jobdif", rs.getString("cc_jobdif"));
				if (rs.getString("cc_testreq_yn") != null && "Y".equals(rs.getString("cc_testreq_yn"))){
					rst.put("testpass", ""); // 테스트전담여부  Y인경우 테스트생략코드는 보여주지 않는다
				}else{
					rst.put("testpass", rs.getString("testpass"));
				}
				rst.put("jobdif", rs.getString("jobdif"));
				rsval.add(rst);					
				rst = null;
			}//end of while-loop statement
			rs.close();
			pstmt.close();
			
			rst = new HashMap<String,String>();
			rst.put("baseyn", "Y");
			if (testSw) rst.put("testyn", "Y");
			else rst.put("testyn", "N");
			rst.put("errsw", "N");
			rst.put("checkyn", "N");
			rst.put("errmsg", "");
			
			
			if (testSw = true) {

				/*  테스트를 위하여 
				if (etcData.get("qayn") != null && !"".equals(etcData.get("qayn")) && "Y".equals(etcData.get("qayn"))) { //QA결재
					if (testSw && etcData.get("passok")!=null && !"2".equals(etcData.get("passok"))) { //테스트대상이고 긴급이 아닌 경우
				*/
					rst.put("checkyn", "Y");
					SRRestApi_20251119 srrestapi = new SRRestApi_20251119();
					String retMsg = srrestapi.callSRTestYn(etcData.get("acptno"),rsval,conn);

					//if (retMsg.length()==4 && "OK".equals(retMsg)) {
					if (retMsg != null && retMsg.startsWith("OK:")){					
						retMsg = retMsg.substring(3).trim();
						ecamsLogger.error("getSRReqList retMsg>  " + retMsg);
						if ("Y".equals(retMsg)){
							rst.put("errsw", "Y");
							rst.put("errmsg", "완료");
						}else if ("N".equals(retMsg)){
							rst.put("errsw", "Y");
							rst.put("errmsg", "비대상");
						}else if ("F".equals(retMsg)){
							rst.put("errsw", "N");
							rst.put("errmsg", "미완료");
						}else{
							rst.put("errsw", "Y");
							rst.put("retcd", retMsg);
							rst.put("errmsg", "알수없는응답["+retMsg+"]");
						}
						
					} else {
						rst.put("errsw", "Y");
						rst.put("retcd", retMsg);
						rst.put("errmsg", "SR연계Fail["+retMsg+"]");
					}
					/*
				} else {
					rst.put("errsw", "N");
					rst.put("errmsg", "");
				}
				*/
			}
			rsval.add(rst);
			rst = null;
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
			ecamsLogger.error("## Cmr0250.getSRReqList() SQLException START ##");
			ecamsLogger.error("## Error DESC : ", sqlexception);
			ecamsLogger.error("## Cmr0250.getSRReqList() SQLException END ##");
			throw sqlexception;
		} catch (Exception exception) {
			exception.printStackTrace();
			ecamsLogger.error("## Cmr0250.getSRReqList() Exception START ##");
			ecamsLogger.error("## Error DESC : ", exception);
			ecamsLogger.error("## Cmr0250.getSRReqList() Exception END ##");
			throw exception;
		}finally{
			if (strQuery != null) strQuery = null;
			if (rs != null)     try{rs.close();}catch (Exception ex){ex.printStackTrace();}
			if (pstmt != null)  try{pstmt.close();}catch (Exception ex2){ex2.printStackTrace();}
			if (conn != null){
				try{
					ConnectionResource.release(conn);
				}catch(Exception ex3){
					ecamsLogger.error("## Cmr0250.getSRReqList() connection release exception ##");
					ex3.printStackTrace();
				}
			}
		}
	
	}
	public Object[] getFiles(String Id) throws SQLException, Exception {
				
		Connection        conn        = null;
		PreparedStatement pstmt       = null;
		ResultSet         rs          = null;
		StringBuffer      strQuery    = new StringBuffer();
		String			  Result	  = "";
		ArrayList<HashMap<String, String>>  rsval = new ArrayList<HashMap<String, String>>();
		HashMap<String, String>			  rst		  = null;
		Object[] 		  returnObject = null;
		
		
		ConnectionContext connectionContext = new ConnectionResource();
			
		try {
			conn = connectionContext.getConnection();
			
			strQuery.setLength(0);
			strQuery.append("select a.cc_id, a.cc_savefile, a.cc_attfile , a.cc_subid, a.cc_subreq, a.cc_seqno, a.cc_editor, a.cc_reqcd, b.cm_username  \n");
			strQuery.append("from cmc1001 a, cmm0040 b where cc_id = ? and a.cc_editor = b.cm_userid 													  \n");
			pstmt = conn.prepareStatement(strQuery.toString());	
			pstmt = new LoggableStatement(conn,strQuery.toString());	
			pstmt.setString(1, Id);
		
		    ecamsLogger.error(((LoggableStatement)pstmt).getQueryString());
		    rs = pstmt.executeQuery();
			while (rs.next()){
				rst = new HashMap<String,String>();
				if(rs.getString("cc_id").length() == 12)
					rst.put("cr_acptno",rs.getString("cc_id"));
				rst.put("cc_id", rs.getString("cc_id"));
				rst.put("cc_savefile", rs.getString("cc_savefile"));
				rst.put("cc_attfile", rs.getString("cc_attfile"));
				rst.put("cc_subid", rs.getString("cc_subid"));
				rst.put("cc_subreq", rs.getString("cc_subreq"));
				rst.put("cc_seqno", rs.getString("cc_seqno"));
				rst.put("cc_editor", rs.getString("cc_editor"));
				rst.put("cc_reqcd", rs.getString("cc_reqcd"));
				rst.put("cm_username", rs.getString("cm_username"));
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
			ecamsLogger.error("## Cmr0250.getFiles() SQLException START ##");
			ecamsLogger.error("## Error DESC : ", sqlexception);
			ecamsLogger.error("## Cmr0250.getFiles() SQLException END ##");
			throw sqlexception;
		} catch (Exception exception) {
			exception.printStackTrace();
			ecamsLogger.error("## Cmr0250.getSRReqList() Exception START ##");
			ecamsLogger.error("## Error DESC : ", exception);
			ecamsLogger.error("## Cmr0250.getSRReqList() Exception END ##");
			throw exception;
		}finally{
			if (strQuery != null) strQuery = null;
			if (rs != null)     try{rs.close();}catch (Exception ex){ex.printStackTrace();}
			if (pstmt != null)  try{pstmt.close();}catch (Exception ex2){ex2.printStackTrace();}
			if (conn != null){
				try{
					ConnectionResource.release(conn);
				}catch(Exception ex3){
					ecamsLogger.error("## Cmr0250.getSRReqList() connection release exception ##");
						ex3.printStackTrace();
					}
				}
			}
		}
	
	public Object[] getStatus(String AcptNo) throws SQLException, Exception {
		
		Connection        conn        = null;
		PreparedStatement pstmt       = null;
		ResultSet         rs          = null;
		StringBuffer      strQuery    = new StringBuffer();
		String			  Result	  = "";
		ArrayList<HashMap<String, String>>  rsval = new ArrayList<HashMap<String, String>>();
		HashMap<String, String>			  rst		  = null;
		Object[] 		  returnObject = null;
		
		
		ConnectionContext connectionContext = new ConnectionResource();
			
		try {
			conn = connectionContext.getConnection();
			
			strQuery.setLength(0);
			strQuery.append("select cr_qrycd, cr_status from cmr1000 where cr_acptno = ? \n");
			pstmt = conn.prepareStatement(strQuery.toString());	
			//pstmt = new LoggableStatement(conn,strQuery.toString());	
			pstmt.setString(1, AcptNo);
		
		   // //ecamsLogger.error(((LoggableStatement)pstmt).getQueryString());
		    rs = pstmt.executeQuery();
			while (rs.next()){
				rst = new HashMap<String,String>();
				rst.put("cr_qrycd", rs.getString("cr_qrycd"));
				rst.put("cr_status", rs.getString("cr_status"));
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
			ecamsLogger.error("## Cmr0250.getStatus() SQLException START ##");
			ecamsLogger.error("## Error DESC : ", sqlexception);
			ecamsLogger.error("## Cmr0250.getStatus() SQLException END ##");
			throw sqlexception;
		} catch (Exception exception) {
			exception.printStackTrace();
			ecamsLogger.error("## Cmr0250.getStatus() Exception START ##");
			ecamsLogger.error("## Error DESC : ", exception);
			ecamsLogger.error("## Cmr0250.getStatus() Exception END ##");
			throw exception;
		}finally{
			if (strQuery != null) strQuery = null;
			if (rs != null)     try{rs.close();}catch (Exception ex){ex.printStackTrace();}
			if (pstmt != null)  try{pstmt.close();}catch (Exception ex2){ex2.printStackTrace();}
			if (conn != null){
				try{
					ConnectionResource.release(conn);
				}catch(Exception ex3){
					ecamsLogger.error("## Cmr0250.getStatus() connection release exception ##");
						ex3.printStackTrace();
					}
				}
			}
		}
	
	public boolean getSrc(String AcptNo, String ItemAll) throws SQLException, Exception {
		
		Connection        conn        = null;
		PreparedStatement pstmt       = null;
		ResultSet         rs          = null;
		StringBuffer      strQuery    = new StringBuffer();
		HashMap<String, String>			  rst		  = null;
		boolean rtnValue = false;
		int rtn = 0;
		String LOC = "";
		
		
		ConnectionContext connectionContext = new ConnectionResource();
			
		try {
			conn = connectionContext.getConnection();
			strQuery.setLength(0);
			//부부장 결재 선 이후 결재자 중에 결재한 사람의 카운트수를 셈
			strQuery.append("select COUNT(*) CNT																	\n");			
			strQuery.append("		from cmr9900 a																	\n");			
			strQuery.append("		where cr_acptno= ?																\n");			
			strQuery.append("			and cr_locat >= (select  cr_locat											\n");
			strQuery.append("										from cmr9900										\n");
			strQuery.append("  										where cr_acptno = a.cr_acptno				\n");
			strQuery.append("     									and cr_locat > '00'								\n");
			strQuery.append("     									and cr_sgngbn = '41'							\n");
			strQuery.append(" 										 )														\n");
			strQuery.append("  			and cr_locat != '00'															\n");
			strQuery.append("  			and cr_status != '0'															\n");
			pstmt = conn.prepareStatement(strQuery.toString());	
			//pstmt = new LoggableStatement(conn,strQuery.toString());	
			pstmt.setString(1, AcptNo);
			//ecamsLogger.error(((LoggableStatement)pstmt).getQueryString());
	        rs = pstmt.executeQuery();
	      
	        //부부장 이후 결재자 중에 결재가 되어있다면 return false;
	        if (rs.next()) {
	      
	        	if(rs.getInt("CNT") > 0){
	        		rs.close();
	        		pstmt.close();
	        		conn.close();
	        		return rtnValue;
	        	}
	        }
	        
	        rs.close();
	        pstmt.close();
	        
	        //System.out.println(ItemAll);
	        String[] itemid = ItemAll.split(",");
	        
//	        System.out.println(itemid.length);
//	        for (int i=0;i<itemid.length;i++){
//				System.out.println("itmid="+itemid[i]);
//			}
	        
	        strQuery.setLength(0);
	        strQuery.append("select cr_serno 	\n");
	        
	        
	        strQuery.setLength(0);
		    //CMR1011테이블에 해당신청건, SYSUP, SYSPUP 의 데이터가 올라온 기록을 삭제
	        strQuery.append("DELETE CMR1011																\n");
		    strQuery.append("		WHERE CR_ACPTNO = ?												\n");
		    strQuery.append("		and CR_SERNO in (select cr_serno from cmr1010					\n");
		    strQuery.append("							where cr_acptno=?							\n");
		    strQuery.append("							  and cr_itemid in (						\n");
		    for (int i=0;i<itemid.length;i++){
				if (i == itemid.length-1)
					strQuery.append(" ? ");
				else
					strQuery.append(" ? ,");
			}
		    strQuery.append("						))												\n");
		    strQuery.append("  			AND CR_PRCSYS IN ('SYSUP', 'SYSPUP')						\n");
			pstmt = conn.prepareStatement(strQuery.toString());	
			//pstmt = new LoggableStatement(conn,strQuery.toString());	
			pstmt.setString(1, AcptNo);
			pstmt.setString(2, AcptNo);
			for (int i=0;i<itemid.length;i++){
				pstmt.setString(i+3, itemid[i]);
			}
	        pstmt.executeUpdate();
	        //ecamsLogger.error(((LoggableStatement)pstmt).getQueryString());
	    	pstmt.close();
	    	
	    	
	    	strQuery.setLength(0);
			//업데이트 ; 형상관리저장 conf 관련 컬럼을 "" 로 바꿈.
			strQuery.append("UPDATE cmr1010 set													 							\n");
			strQuery.append("			cr_sysuprst = ''																	\n");
			strQuery.append("WHERE  cr_acptno = ? AND CR_ITEMID in (														\n");
			for (int i=0;i<itemid.length;i++){
				if (i == itemid.length-1)
					strQuery.append(" ? ");
				else
					strQuery.append(" ? ,");
			}
			strQuery.append("										)														\n");
			pstmt = conn.prepareStatement(strQuery.toString());
			//pstmt = new LoggableStatement(conn,strQuery.toString());	
			pstmt.setString(1, AcptNo);
			for (int i=0;i<itemid.length;i++){
				pstmt.setString(i+2, itemid[i]);
			}
			rtn = 0;
			rtn = pstmt.executeUpdate();
			if(rtn >= 0)
				rtnValue = true;
			else
				rtnValue = false;
			pstmt.close();
	    	
	    	
	    	
			strQuery.setLength(0);
			//업데이트 ; 형상관리저장 conf 관련 컬럼을 "" 로 바꿈.
			strQuery.append("UPDATE cmr9900 set													 							\n");
			strQuery.append("			cr_status = '0' , 																			\n");
			strQuery.append("			cr_confusr = '', cr_conmsg = '', cr_confdate = ''	    							\n");
			strQuery.append("WHERE  cr_acptno = ? AND CR_SGNGBN IN ('SYSUP', 'SYSPUP', 'SYSMV')			\n");
			pstmt = conn.prepareStatement(strQuery.toString());
			//pstmt = new LoggableStatement(conn,strQuery.toString());	
			pstmt.setString(1, AcptNo);
			rtn = 0;
			rtn = pstmt.executeUpdate();
			if(rtn >= 0)
				rtnValue = true;
			else
				rtnValue = false;
		
			
			pstmt.close();
			strQuery.setLength(0);
			//부장 결재 이후에 결재 안한 사람의 위치 값 중 제일 작은 값
			strQuery.append("SELECT  MIN(CR_LOCAT) LOC													\n");
			strQuery.append("   FROM CMR9900																	\n");
			strQuery.append("   WHERE CR_ACPTNO = ?														\n");
			strQuery.append("		AND CR_LOCAT > '00'														\n");
			strQuery.append("		AND CR_STATUS = '0'														\n");
			strQuery.append("		AND CR_CONGBN = '2'													\n");
			pstmt = conn.prepareStatement(strQuery.toString());
			//pstmt = new LoggableStatement(conn,strQuery.toString());	
			pstmt.setString(1, AcptNo);
			//ecamsLogger.error(((LoggableStatement)pstmt).getQueryString());
			rs = pstmt.executeQuery();
			
			if(rs.next())	 {
				LOC = rs.getString("LOC");
				System.out.println(LOC);
			}
			
			else {
				rs.close();
				
				pstmt.close();
				strQuery.setLength(0);
				//결재 안한 사람의 최소 위치 값
				strQuery.append("SELECT  MIN(CR_LOCAT) LOC												\n");
				strQuery.append(" FROM CMR9900																\n");
				strQuery.append(" WHERE CR_ACPTNO = ?														\n");
				strQuery.append("	AND CR_LOCAT > '00'														\n");
				strQuery.append("	AND CR_STATUS = '0'													\n");
				pstmt = conn.prepareStatement(strQuery.toString());
				//pstmt = new LoggableStatement(conn,strQuery.toString());	
				pstmt.setString(1, AcptNo);
				//ecamsLogger.error(((LoggableStatement)pstmt).getQueryString());
				rs = pstmt.executeQuery();
				
				if(rs.next())
					LOC = rs.getString("LOC");
				
			}
			
			pstmt.close();
			strQuery.setLength(0);
			//해당 신청건의 결재
			strQuery.append("UPDATE  CMR9900 a 																																						\n");
			strQuery.append("		set  (CR_CONFNAME, CR_TEAM, CR_TEAMCD, CR_CONGBN, CR_COMMON, CR_BLANK, CR_EMGER,										\n");
			strQuery.append("	    	   CR_HOLI, CR_SGNGBN, CR_BLANKCD, CR_ORGSTEP, CR_BASEUSR, CR_CONFUSR) =													\n");
			strQuery.append(" 		(select   CR_CONFNAME, CR_TEAM, CR_TEAMCD, CR_CONGBN, CR_COMMON, CR_BLANK, CR_EMGER,								\n");
			strQuery.append("					CR_HOLI, CR_SGNGBN, CR_BLANKCD, CR_ORGSTEP, CR_BASEUSR, ? || ?			 													\n");
			strQuery.append("				from cmr9900																																					\n");
			strQuery.append("				where cr_acptno =a.cr_acptno																															\n");
			strQuery.append("    			and cr_locat = ?)																																  			    \n");
			strQuery.append("  		WHERE CR_ACPTNO = ?																																			\n");
			strQuery.append(" 		AND CR_LOCAT = '00'																																				\n");
			pstmt = conn.prepareStatement(strQuery.toString());
			//pstmt = new LoggableStatement(conn,strQuery.toString());	
			pstmt.setString(1, LOC);
			pstmt.setString(2, LOC);
			pstmt.setString(3, LOC);
			pstmt.setString(4, AcptNo);
			//ecamsLogger.error(((LoggableStatement)pstmt).getQueryString());
			rtn = 0;
			rtn = pstmt.executeUpdate();
			if(rtn >= 0)
				rtnValue = true;
			else
				rtnValue = false;
			
			rs.close();
			pstmt.close();
		    //conn.commit();
			conn.close();
		
			rs = null;
			pstmt = null;
			conn = null;
		
			return rtnValue;
		
		} catch (SQLException sqlexception) {
			sqlexception.printStackTrace();
			ecamsLogger.error("## Cmr0250.getSrc() SQLException START ##");
			ecamsLogger.error("## Error DESC : ", sqlexception);
			ecamsLogger.error("## Cmr0250.getSrc() SQLException END ##");
			throw sqlexception;
		} catch (Exception exception) {
			exception.printStackTrace();
			ecamsLogger.error("## Cmr0250.getSrc() Exception START ##");
			ecamsLogger.error("## Error DESC : ", exception);
			ecamsLogger.error("## Cmr0250.getSrc() Exception END ##");
			throw exception;
		}finally{
			if (strQuery != null) strQuery = null;
			if (rs != null)     try{rs.close();}catch (Exception ex){ex.printStackTrace();}
			if (pstmt != null)  try{pstmt.close();}catch (Exception ex2){ex2.printStackTrace();}
			if (conn != null){
				try{
					ConnectionResource.release(conn);
				}catch(Exception ex3){
					ecamsLogger.error("## Cmr0250.getSrc() connection release exception ##");
						ex3.printStackTrace();
					}
				}
			}
		}

	public Object[] getTestData(String AcptNo) throws SQLException, Exception {
			
			Connection        conn        = null;
			PreparedStatement pstmt       = null;
			ResultSet         rs          = null;
			StringBuffer      strQuery    = new StringBuffer();
			ArrayList<HashMap<String, String>>  rsval = new ArrayList<HashMap<String, String>>();
			HashMap<String, String>			  rst		  = null;
			Object[] 		  returnObject = null;
			
			
			ConnectionContext connectionContext = new ConnectionResource();
				
			try {
				conn = connectionContext.getConnection();
				
				strQuery.setLength(0);
				strQuery.append("select cr_etcsayu, cr_testdate, cr_testrslt, cr_cnclsayu from cmr1000 where cr_acptno = ? \n");
				pstmt = conn.prepareStatement(strQuery.toString());	
				//pstmt = new LoggableStatement(conn,strQuery.toString());	
				pstmt.setString(1, AcptNo);
				//ecamsLogger.error(((LoggableStatement)pstmt).getQueryString());
			    rs = pstmt.executeQuery();
				while (rs.next()){
					rst = new HashMap<String,String>();
					rst.put("cr_testdate", rs.getString("cr_testdate"));
					rst.put("cr_etcsayu", rs.getString("cr_etcsayu"));
					rst.put("cr_testrslt", rs.getString("cr_testrslt"));
					rst.put("cr_cnclsayu", rs.getString("cr_cnclsayu"));
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
				ecamsLogger.error("## Cmr0250.getTestData() SQLException START ##");
				ecamsLogger.error("## Error DESC : ", sqlexception);
				ecamsLogger.error("## Cmr0250.getTestData() SQLException END ##");
				throw sqlexception;
			} catch (Exception exception) {
				exception.printStackTrace();
				ecamsLogger.error("## Cmr0250.getTestData() Exception START ##");
				ecamsLogger.error("## Error DESC : ", exception);
				ecamsLogger.error("## Cmr0250.getTestData() Exception END ##");
				throw exception;
			}finally{
				if (strQuery != null) strQuery = null;
				if (rs != null)     try{rs.close();}catch (Exception ex){ex.printStackTrace();}
				if (pstmt != null)  try{pstmt.close();}catch (Exception ex2){ex2.printStackTrace();}
				if (conn != null){
					try{
						ConnectionResource.release(conn);
					}catch(Exception ex3){
						ecamsLogger.error("## Cmr0250.getTestData() connection release exception ##");
							ex3.printStackTrace();
						}
					}
				}
			}
	
	
	public boolean setTestData(String AcptNo, HashMap<String,String> Data ) throws SQLException, Exception {

		Connection        conn        = null;
		PreparedStatement pstmt       = null;
		StringBuffer      strQuery    = new StringBuffer();
		boolean rtnValue = false;
		
		ConnectionContext connectionContext = new ConnectionResource();
		String date = Data.get("cr_testdate").replaceAll("/", "");
		try {
			conn = connectionContext.getConnection();
			strQuery.setLength(0);
			strQuery.append("update cmr1000 a																		\n");
			strQuery.append("	set a.cr_testdate = ?, a.cr_etcsayu = ? ,										\n");
			strQuery.append("		 a.cr_cnclsayu = ?, a.cr_testrslt= ?											\n");
			strQuery.append("			where a.cr_acptno = ?														\n");
			pstmt = conn.prepareStatement(strQuery.toString());
			pstmt = new LoggableStatement(conn,strQuery.toString());
			pstmt.setString(1, date);
			pstmt.setString(2, Data.get("cr_etcsayu"));
			pstmt.setString(3, Data.get("cr_cnclsayu"));
			pstmt.setString(4, Data.get("cr_testrslt"));
			pstmt.setString(5, AcptNo);
			ecamsLogger.error(((LoggableStatement)pstmt).getQueryString());
		    int rtn = pstmt.executeUpdate();
		    if(rtn > 0)
		    	rtnValue = true;
		    conn.close();

			pstmt = null;
			conn = null;
			
			return rtnValue;			

		} catch (SQLException sqlexception) {
			sqlexception.printStackTrace();
			ecamsLogger.error("## Cmr0250.setTestData() SQLException START ##");
			ecamsLogger.error("## Error DESC : ", sqlexception);
			ecamsLogger.error("## Cmr0250.setTestData() SQLException END ##");
			throw sqlexception;
		} catch (Exception exception) {
			exception.printStackTrace();
			ecamsLogger.error("## Cmr0250.setTestData() Exception START ##");
			ecamsLogger.error("## Error DESC : ", exception);
			ecamsLogger.error("## Cmr0250.setTestData() Exception END ##");
			throw exception;
		}finally{
			if (strQuery != null) strQuery = null;
			if (pstmt != null)  try{pstmt.close();}catch (Exception ex2){ex2.printStackTrace();}
			if (conn != null){
				try{
					ConnectionResource.release(conn);
				}catch(Exception ex3){
					ecamsLogger.error("## Cmr0250.setTestData() connection release exception ##");
					ex3.printStackTrace();
				}
			}
		}
	}

	
	public Object[] getBeforeFiles(String UserId, String StDate, String EdDate) throws SQLException, Exception {
		Connection        conn        = null;
		PreparedStatement pstmt       = null;
		ResultSet         rs          = null;
		StringBuffer      strQuery    = new StringBuffer();
	
		ArrayList<HashMap<String, String>>  rsval = new ArrayList<HashMap<String, String>>();
		HashMap<String, String>			  rst		  = null;
		Object[] 		  returnObject = null;
		

		ConnectionContext connectionContext = new ConnectionResource();
			
		try {
			conn = connectionContext.getConnection();
			
			strQuery.setLength(0);
	
			strQuery.append("select b.cr_acptno, b.cr_syscd, c.cm_sysmsg, a.cc_id, a.cc_subid, a.cc_subreq,a.cc_seqno, a.cc_savefile, 												\n");
			strQuery.append("a.cc_attfile, b.cr_editor, a.cc_reqcd, b.cr_sayu, to_char(b.cr_acptdate,'yyyy/mm/dd') as acptdate, \n");
			strQuery.append("(select cm_username from cmm0040 where b.cr_editor = cm_userid) as cm_username	\n");
			strQuery.append("from   cmc1001 a,																														\n");
			strQuery.append(" cmr1000 b	, cmm0030 c																												\n");
			strQuery.append(" where  b.cr_editor = ?																												\n");
			strQuery.append(" and    substr(b.cr_acptno,5,2) = '04'																								\n");
			strQuery.append(" and    b.cr_acptno = a.cc_id																										\n");
			strQuery.append(" and    b.cr_syscd = c.cm_syscd																										\n");
			strQuery.append(" and    to_char(b.cr_acptdate, 'yyyy/mm/dd') >= ?																					\n");
			strQuery.append(" and    to_char(b.cr_acptdate, 'yyyy/mm/dd') <= ?																					\n");
			strQuery.append(" order by b.cr_acptdate																													\n");			
			pstmt = conn.prepareStatement(strQuery.toString());	
			pstmt = new LoggableStatement(conn,strQuery.toString());	
			pstmt.setString(1,UserId);
			pstmt.setString(2,StDate);
			pstmt.setString(3,EdDate);
			ecamsLogger.error(((LoggableStatement)pstmt).getQueryString());
		
	        rs = pstmt.executeQuery();
			while (rs.next()){
				rst = new HashMap<String,String>();
				
				rst.put("cr_acptno", rs.getString("cr_acptno"));
				rst.put("cc_id", rs.getString("cc_id"));
				rst.put("cc_subid", rs.getString("cc_subid"));
				rst.put("cc_subreq", rs.getString("cc_subreq"));
				rst.put("cc_seqno", rs.getString("cc_seqno"));
				rst.put("cc_savefile", rs.getString("cc_savefile"));
				rst.put("cc_attfile", rs.getString("cc_attfile"));
				rst.put("cr_editor", rs.getString("cr_editor"));
				rst.put("cc_reqcd", rs.getString("cc_reqcd"));
				rst.put("cr_sayu", rs.getString("cr_sayu"));
				rst.put("cm_sysmsg", rs.getString("cm_sysmsg"));
				rst.put("cr_syscd", rs.getString("cr_syscd"));
				rst.put("cr_acptdate", rs.getString("acptdate"));
				rst.put("cm_username",rs.getString("cm_username"));
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
			ecamsLogger.error("## Cmr0250.getBeforeFiles() SQLException START ##");
			ecamsLogger.error("## Error DESC : ", sqlexception);
			ecamsLogger.error("## Cmr0250.getBeforeFiles() SQLException END ##");
			throw sqlexception;
		} catch (Exception exception) {
			exception.printStackTrace();
			ecamsLogger.error("## Cmr0250.getBeforeFiles() Exception START ##");
			ecamsLogger.error("## Error DESC : ", exception);
			ecamsLogger.error("## Cmr0250.getBeforeFiles() Exception END ##");
			throw exception;
		}finally{
			if (strQuery != null) strQuery = null;
			if (rs != null)     try{rs.close();}catch (Exception ex){ex.printStackTrace();}
			if (pstmt != null)  try{pstmt.close();}catch (Exception ex2){ex2.printStackTrace();}
			if (conn != null){
				try{
					ConnectionResource.release(conn);
				}catch(Exception ex3){
					ecamsLogger.error("## Cmr0250.getBeforeFiles() connection release exception ##");
					ex3.printStackTrace();
				}
			}
		}
	
	}
	
	  public Object[] getReqRunners(String AcptNo) throws SQLException, Exception {
	    	Connection        conn        = null;
	    	PreparedStatement pstmt       = null;
	    	ResultSet         rs          = null;
	    	StringBuffer      strQuery    = new StringBuffer();
	    	Object[] returnObjectArray    = null;
	    	ArrayList<HashMap<String, String>>  rsval = new ArrayList<HashMap<String, String>>();
	    	HashMap<String, String> rst	  = null;
	    	int              parmCnt      = 0;
	    	ConnectionContext connectionContext = new ConnectionResource();		
	    	
	    	try {
	    		
	    		conn = connectionContext.getConnection();
	    		strQuery.setLength(0);
				strQuery.append("select a.cc_reqid, b.cm_username, c.cm_deptname, d.cc_docsubj,	   																						\n");
				strQuery.append("a.cc_requser3, a.cc_dept3 					   																									\n");
				strQuery.append("from cmc0401 a, cmm0040 b, cmm0100 c,cmc0400 d 																									\n");	
				strQuery.append("where a.cc_reqid in 																																\n");
				strQuery.append("(select b.cc_reqid from cmr1012 a,cmc0420 b where a.cr_acptno = ? and a.cr_orderid = b.cc_orderid group by b.cc_reqid)	\n");				    			
				strQuery.append("and	  a.cc_requser3 = b.cm_userid 																											\n");	
				strQuery.append("and	  a.cc_dept3 = c.cm_deptcd 																												\n");	
				strQuery.append("and	  a.cc_reqid = d.cc_reqid																												\n");	
				strQuery.append("order by a.cc_reqid 																																\n");	
	    		pstmt = conn.prepareStatement(strQuery.toString());
	    		pstmt = new LoggableStatement(conn,strQuery.toString());
	    		pstmt.setString(1, AcptNo);
	    		ecamsLogger.error(((LoggableStatement)pstmt).getQueryString());
	    		rs = pstmt.executeQuery();
	            while(rs.next()) {
	    	  		rst = new HashMap<String, String>();
	    	  		rst.put("cc_reqid", rs.getString("cc_reqid"));
	    			rst.put("cm_username", rs.getString("cm_username"));
	    			rst.put("cm_deptname", rs.getString("cm_deptname"));
	    			rst.put("cc_requser3", rs.getString("cc_requser3"));
	    			rst.put("cc_dept3", rs.getString("cc_dept3"));

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
	    		rsval.clear();
	    		rsval = null;

	    		return returnObjectArray;
	    		
	    		
	    	} catch (SQLException sqlexception) {
	    		sqlexception.printStackTrace();
	    		ecamsLogger.error("## Cmr0250.getReqRunners() SQLException START ##");
	    		ecamsLogger.error("## Error DESC : ", sqlexception);	
	    		ecamsLogger.error("## Cmr0250.getReqRunners() SQLException END ##");			
	    		throw sqlexception;
	    	} catch (Exception exception) {
	    		exception.printStackTrace();
	    		ecamsLogger.error("## Cmr0250.getReqRunners() Exception START ##");				
	    		ecamsLogger.error("## Error DESC : ", exception);	
	    		ecamsLogger.error("## Cmr0250.getReqRunners() Exception END ##");				
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
	    				ecamsLogger.error("## Cmr0250.getReqRunners() connection release exception ##");
	    				ex3.printStackTrace();
	    			}
	    		}
	    	}
	  }
	
	  
	  
	  public Object[] getFortify(String AcptNo) throws SQLException, Exception {
	    	Connection        conn        = null;
	    	Connection        connF        = null;
	    	PreparedStatement pstmt       = null;
	    	PreparedStatement pstmt2       = null;
	    	ResultSet         rs          = null;
	    	ResultSet         rs2          = null;
	    	StringBuffer      strQuery    = new StringBuffer();
	    	Object[] returnObjectArray    = null;
	    	ArrayList<HashMap<String, String>>  rsval = new ArrayList<HashMap<String, String>>();
	    	HashMap<String, String> rst	  = null;
	    	//int              parmCnt      = 0;
	    	
	    	ConnectionContext connectionContext = new ConnectionResource();
	    	ConnectionContext connectionContextF = new ConnectionResource(false,"F");
	    	
	    	try {
	    		conn = connectionContext.getConnection();
	    		connF = connectionContextF.getConnection();
	    		
	    		strQuery.setLength(0);
				strQuery.append("select pro_filename,			\n");	//프로그램명
				strQuery.append("		sca_end_clss,			\n");	//상태	
				strQuery.append("		decode(sca_end_clss,'Y','취약점있음','N','취약점없음','E','대상업무아님',sca_end_clss) sca_end_clss_msg,	\n");	//상태	
				strQuery.append("       pro_itemid, 			\n");	//itemid
				strQuery.append("       PROJECT_ID, 			\n");
				strQuery.append("       SCAN_ID   , 			\n");
				strQuery.append("       SCANFILEORIGINAL,		\n");
				strQuery.append("       PRO_SEND_DT,    		\n");
				strQuery.append("       REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(SCA_SEND_DATA, 'C:', 'Critical:'), 'H:', 'Hight:'), 'L:', 'Low:'), 'M:', 'Medium:'), '/', ' / ') SCA_SEND_DATA \n");	//결과				
				strQuery.append("  from FORTIFY_SCA_NAME		\n");				    			
				strQuery.append(" where pro_acptno=?			\n");	
	    		pstmt = connF.prepareStatement(strQuery.toString());
	    		pstmt = new LoggableStatement(connF,strQuery.toString());
	    		pstmt.setString(1, AcptNo);
	    		ecamsLogger.error(((LoggableStatement)pstmt).getQueryString());
	    		rs = pstmt.executeQuery();
	            while(rs.next()) {
	    	  		rst = new HashMap<String, String>();
	    	  		if (rs.getString("pro_itemid") != null){
	    	  			strQuery.setLength(0);
	    				strQuery.append("select decode(cr_qrycd,'03','신규','04','수정','') checkin,		\n");
	    				strQuery.append("       cr_editcon 			\n");
	    				strQuery.append("  from cmr1010				\n");				    			
	    				strQuery.append(" where cr_acptno = ?		\n");
	    				strQuery.append("   and cr_itemid = ?		\n");
	    				pstmt2 = conn.prepareStatement(strQuery.toString());
	    	    		pstmt2 = new LoggableStatement(conn,strQuery.toString());
	    	    		pstmt2.setString(1, AcptNo);
	    	    		pstmt2.setString(2, rs.getString("pro_itemid"));
	    	    		ecamsLogger.error(((LoggableStatement)pstmt2).getQueryString());
	    	    		rs2 = pstmt2.executeQuery();
	    	    		while(rs2.next()) {
	    	    			rst.put("checkin", rs2.getString("checkin"));
	    	    			rst.put("cr_sayu", rs2.getString("cr_editcon"));
	    	    		}
	    	    		rs2.close();
	    	    		pstmt2.close();
	    	  			rst.put("pro_itemid", rs.getString("pro_itemid"));	    	  			
	    	  		}

	    	  		rst.put("cr_rsrcname", rs.getString("pro_filename"));
	    	  		if (rs.getString("sca_end_clss") == null || rs.getString("sca_end_clss") == ""){
	    	  			rst.put("cr_status", "");
	    	  			rst.put("cr_status_msg", "진행 중");
	    	  			rst.put("cr_result", "");
	    	  		}else{
	    	  			rst.put("cr_status", rs.getString("sca_end_clss"));
	    	  			rst.put("cr_status_msg", rs.getString("sca_end_clss_msg"));
	    	  			rst.put("cr_result", rs.getString("SCA_SEND_DATA"));	   
	    	  			rst.put("PROJECT_ID", rs.getString("PROJECT_ID"));	    
	    	  			rst.put("SCAN_ID", rs.getString("SCAN_ID"));	       
	    	  			rst.put("SCANFILEORIGINAL", rs.getString("SCANFILEORIGINAL"));	      	  			
	    	  			rst.put("PRO_SEND_DT", rs.getString("PRO_SEND_DT"));	      	  			
	    	  		}
	    			rsval.add(rst);
	    			rst = null;
	            }
	            rs.close();
	            pstmt.close();
	            conn.close();
	            connF.close();
	            
	      		rs = null;
	      		pstmt = null;
	      		conn = null;
	      		connF = null;
	    		
	    		returnObjectArray = rsval.toArray();
	    		rsval.clear();
	    		rsval = null;

	    		return returnObjectArray;
	    		
	    		
	    	} catch (SQLException sqlexception) {
	    		sqlexception.printStackTrace();
	    		ecamsLogger.error("## Cmr0250.getFortify() SQLException START ##");
	    		ecamsLogger.error("## Error DESC : ", sqlexception);	
	    		ecamsLogger.error("## Cmr0250.getFortify() SQLException END ##");			
	    		throw sqlexception;
	    	} catch (Exception exception) {
	    		exception.printStackTrace();
	    		ecamsLogger.error("## Cmr0250.getFortify() Exception START ##");				
	    		ecamsLogger.error("## Error DESC : ", exception);	
	    		ecamsLogger.error("## Cmr0250.getFortify() Exception END ##");				
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
	    				ecamsLogger.error("## Cmr0250.getFortify() connection release exception ##");
	    				ex3.printStackTrace();
	    			}
	    		}
	    		if (connF != null){
	    			try{
	    				ConnectionResource.release(connF);
	    			}catch(Exception ex3){
	    				ecamsLogger.error("## Cmr0250.getFortify() connection release exception ##");
	    				ex3.printStackTrace();
	    			}
	    		}
	    	}
	  }
	  
	  public Object[] getFortify_Result(String AcptNo, String ItemID) throws SQLException, Exception {
	    	Connection        conn        = null;
	    	Connection        connF        = null;
	    	PreparedStatement pstmt       = null;
	    	PreparedStatement pstmt2       = null;
	    	ResultSet         rs          = null;
	    	ResultSet         rs2          = null;
	    	StringBuffer      strQuery    = new StringBuffer();
	    	Object[] returnObjectArray    = null;
	    	ArrayList<HashMap<String, String>>  rsval = new ArrayList<HashMap<String, String>>();
	    	HashMap<String, String> rst	  = null;
	    	//int              parmCnt      = 0;
	    	
	    	ConnectionContext connectionContextF = new ConnectionResource(false,"F");
	    	
	    	try {
	    		connF = connectionContextF.getConnection();
	    		
//	    		strQuery.setLength(0);	    		
//	    		strQuery.append("select                                                                                            \n");
//	    		strQuery.append("        category45_,                                                                              \n");
//	    		strQuery.append("        fileName45_,                                                                              \n");
//	    		strQuery.append("        lineNumber45_,                                                                            \n");
//	    		strQuery.append("        friority45_,                                                                              \n");
//	    		strQuery.append("        shortFi60_45_,                                                                            \n");
//	    		strQuery.append("        lastScan36_45_,                                                                           \n");
//	    		strQuery.append("        DBMS_LOB.SUBSTR( RAWDETAIL     , 2242,1) as issueAb28_45_1,                                 \n");
//	    		strQuery.append("        DBMS_LOB.SUBSTR( RECOMMENDATION, 2242,1) as issueRe30_45_1,                                 \n");
//	    		strQuery.append("        project44_45_                                                                             \n");
//	    		strQuery.append("        ,PROJECTNAME                                                                              \n");
//	    		strQuery.append("        ,UPLOADDATE                                                                               \n");
//	    		strQuery.append(" from                                                                                             \n");
//	    		strQuery.append("( select                                                                                          \n");
//	    		strQuery.append("                modelissue0_.MAPPEDCATEGORY as category45_,                                       \n");
//	    		strQuery.append("                modelissue0_.fileName as fileName45_,                                             \n");
//	    		strQuery.append("                modelissue0_.friority as friority45_,                                             \n");
//	    		strQuery.append("                modelissue0_.issueAbstract as issueAb28_45_,                                      \n");
//	    		strQuery.append("                modelissue0_.issueRecommendation as issueRe30_45_,                                \n");
//	    		strQuery.append("                modelissue0_.lineNumber as lineNumber45_ ,                                        \n");                strQuery.append("                RULEVIEW.RAWDETAIL     ,  					                                       \n");
//	    		strQuery.append("                RULEVIEW.RECOMMENDATION,                    				 	                   \n");
//	    		strQuery.append("                modelissue0_.shortFileName as shortFi60_45_,                                      \n");
//	    		strQuery.append("                 modelissue0_.lastScan_id as lastScan36_45_,                                      \n");
//	    		strQuery.append("                modelissue0_.projectVersion_id as project44_45_                                   \n");
//	    		strQuery.append("                ,aaa.PROJECTNAME as PROJECTNAME                                                   \n");
//	    		strQuery.append("                ,TO_CHAR(aaa.UPLOADDATE, 'YYYY-MM-DD')  as UPLOADDATE                             \n");
//	    		strQuery.append("    from                                                                                          \n");
//	    		strQuery.append("    (                                                                                             \n");
//	    		strQuery.append("     SELECT PROJECT.NAME AS GROUPNAME                                                             \n");
//	    		strQuery.append("        , PROJECTVERSION.NAME AS PROJECTNAME                                                      \n");
//	    		strQuery.append("        ,ARTIFACT.PROJECTVERSION_ID AS PROJECTVERSION_ID                                          \n");
//	    		strQuery.append("        , DOCUMENTINFO.UPLOADDATE AS UPLOADDATE                                                   \n");
//	    		strQuery.append("        FROM DOCUMENTINFO, ARTIFACT, PROJECT, PROJECTVERSION                                      \n");
//	    		strQuery.append("        WHERE PROJECT.ID = PROJECTVERSION.PROJECT_ID                                              \n");
//	    		strQuery.append("        AND ARTIFACT.PROJECTVERSION_ID = PROJECTVERSION.ID                                        \n");
//	    		strQuery.append("        AND DOCUMENTINFO.ID = ARTIFACT.DOCUMENTINFO_ID                                            \n");
//	    		strQuery.append("        AND ARTIFACT.ARTIFACTTYPE = 'FPR'                                                         \n");
//	    		strQuery.append("        AND ARTIFACT.STATUS = 'PROCESS_COMPLETE'                                                  \n");
//	    		strQuery.append("        AND ARTIFACT.PROJECTVERSION_ID = ?  		                                               \n");
//	    		strQuery.append("        ORDER BY DOCUMENTINFO.UPLOADDATE DESC                                                     \n");
//	    		strQuery.append("    )aaa,    RULEVIEW ,                                                                           \n");
//	    		strQuery.append("    issue modelissue0_ cross join issuecache issuecache1_                                         \n");
//	    		strQuery.append("    where modelissue0_.projectVersion_id= ?                                                 	   \n");
//	    		strQuery.append("      and modelissue0_.RULEGUID = RULEVIEW.RULEGUID                                         \n");
//	    		strQuery.append("      and modelissue0_.PROJECTVERSION_ID = RULEVIEW.PROJECTVERSION_ID						 \n");
//	    		strQuery.append("          and modelissue0_.projectVersion_id= aaa.PROJECTVERSION_ID                               \n");
//	    		strQuery.append("                and modelissue0_.suppressed='N'                                                   \n");
//	    		strQuery.append("                and issuecache1_.projectVersion_id=?                                        \n");
//	    		strQuery.append("                and modelissue0_.shortFileName = ?                                   \n");
//	    		strQuery.append("                and issuecache1_.issue_id=modelissue0_.id                                         \n");
//	    		strQuery.append("                and issuecache1_.hidden='N'                                                       \n");
//	    		strQuery.append("                and TO_CHAR(aaa.UPLOADDATE, 'YYYY-MM-DD') = ?                          \n");
//	    		strQuery.append("    order by modelissue0_.shortFileName, modelissue0_.lineNumber, aaa.PROJECTNAME                 \n");
//	    		strQuery.append("                ,aaa.UPLOADDATE )                                                                 \n");
//	    		strQuery.append("                 group by category45_,                                                            \n");
//	    		strQuery.append("                 fileName45_,                                                                     \n");
//	    		strQuery.append("                 friority45_,                                                                     \n");
//	    		strQuery.append("        DBMS_LOB.SUBSTR( RAWDETAIL     , 2242,1) ,                                 \n");
//	    		strQuery.append("        DBMS_LOB.SUBSTR( RECOMMENDATION, 2242,1) ,                                 \n");
//	    		strQuery.append("                lineNumber45_,                                                                    \n");
//	    		strQuery.append("                 shortFi60_45_,                                                                   \n");
//	    		strQuery.append("                 lastScan36_45_,                                                                  \n");
//	    		strQuery.append("                project44_45_                                                                     \n");
//	    		strQuery.append("                ,PROJECTNAME                                                                      \n");
//	    		strQuery.append("                ,UPLOADDATE                                                                       \n");
//
//	    		
//	    		pstmt = connF.prepareStatement(strQuery.toString());
//	    		pstmt = new LoggableStatement(connF,strQuery.toString());
//	    		pstmt.setString(1, PROJECTVERSION_ID);
//	    		pstmt.setString(2, PROJECTVERSION_ID);
//	    		pstmt.setString(3, PROJECTVERSION_ID);
//	    		pstmt.setString(4, FILENAME);
//	    		pstmt.setString(5, ScanDate);
//	    		ecamsLogger.error(((LoggableStatement)pstmt).getQueryString());	    		
//	    		rs = pstmt.executeQuery();
//	    		Blob tmpblob = null;
//	    		byte[] tmpByte = null;
//	    		String tmpStr = "";
//	            while(rs.next()) {
//	    	  		rst = new HashMap<String, String>();
//		    		rst.put("PROJECTNAME", rs.getString("PROJECTNAME"));
//    	  			rst.put("PROJECTVERSION_ID", Integer.toString(rs.getInt("project44_45_")));
//    	  			rst.put("LASTSCAN_ID", Integer.toString(rs.getInt("lastScan36_45_")));	   
//    	  			rst.put("UPLOADDATE", rs.getString("UPLOADDATE"));	    
//    	  			rst.put("FRIORITY", rs.getString("friority45_"));	    
//    	  			rst.put("FILENAME", rs.getString("fileName45_"));	    
//    	  			rst.put("ISSUETYPE", rs.getString("category45_"));	    
//    	  			//rst.put("ISSUESUBTYPE", rs.getString("ISSUESUBTYPE"));    
//    	  			rst.put("LINENUMBER", Integer.toString(rs.getInt("lineNumber45_")));
//    	  			rst.put("RAWDETAIL", rs.getString("issueAb28_45_1").replace("<br>", "\r"));	    
//    	  			rst.put("RECOMMENDATION", rs.getString("issueRe30_45_1").replace("<br>", "\r")); 
////    	  			tmpblob = rs.getBlob("PROGSOURCE");
////    	    		int tmpInt = (int)tmpblob.length();
////    	  			tmpByte = tmpblob.getBytes(1, tmpInt);
////    	  			tmpStr = new String(tmpByte);
////    	  			rst.put("PROGSOURCE", tmpStr);
////    	  			ecamsLogger.error("tmpStr=======>>>>>>"+tmpStr);
//    	  			
//	    			rsval.add(rst);
//	    			rst = null;  
//	            }
//	            rs.close();
//	            pstmt.close();
//	            connF.close();
	            
	    		
	    		strQuery.setLength(0);	    		
	    		strQuery.append("SELECT                                                                         			\n");
	    		strQuery.append("        A.PRO_FILENAME,                                                        			\n");
	    		strQuery.append("        B.SCA_ISSUE_LEVEL,		                                                			\n");
	    		strQuery.append("        B.SCA_ISSUE_LINE_NUMBER,	                                            			\n");
	    		strQuery.append("        C.SCA_CATEGORY_NAME,		                                           				\n");
	    		strQuery.append("        DBMS_LOB.SUBSTR(C.SCA_CATEGORY_EXPLANATION, 2242,1) as SCA_CATEGORY_EXPLANATION ,	\n");
	    		strQuery.append("        DBMS_LOB.SUBSTR(C.SCA_CATEGORY_RECOMMEND  , 2242,1) as SCA_CATEGORY_RECOMMEND      \n");
	    		strQuery.append("  FROM  FORTIFY_SCA_NAME A, FORTIFY_SCA_ISSUE B, FORTIFY_SCA_CATEGORY C        			\n");
	    		strQuery.append(" WHERE  A.PRO_ACPTNO = ?                                                       			\n");
	    		strQuery.append("   AND  A.PRO_ITEMID = ?                                                    				\n");
	    		strQuery.append("   AND  B.PRO_ACPTNO = A.PRO_ACPTNO                                            			\n");
	    		strQuery.append("   AND  B.PRO_ITEMID = A.PRO_ITEMID                                          				\n");
	    		strQuery.append("   AND  C.SCA_CATEGORY_ID = B.SCA_CATEGORY_ID                                				\n");
	    			    		
	    		pstmt = connF.prepareStatement(strQuery.toString());
	    		pstmt = new LoggableStatement(connF,strQuery.toString());
	    		pstmt.setString(1, AcptNo);
	    		pstmt.setString(2, ItemID);
	    		ecamsLogger.error(((LoggableStatement)pstmt).getQueryString());	    		
	    		rs = pstmt.executeQuery();
	    		Blob tmpblob = null;
	    		byte[] tmpByte = null;
	    		String tmpStr = "";
	            while(rs.next()) {
	    	  		rst = new HashMap<String, String>();
		    		rst.put("PRO_FILENAME", rs.getString("PRO_FILENAME"));
    	  			rst.put("SCA_ISSUE_LEVEL", rs.getString("SCA_ISSUE_LEVEL"));
    	  			rst.put("SCA_ISSUE_LINE_NUMBER", Integer.toString(rs.getInt("SCA_ISSUE_LINE_NUMBER")));	   
    	  			rst.put("SCA_CATEGORY_NAME", rs.getString("SCA_CATEGORY_NAME"));	    
    	  			rst.put("SCA_CATEGORY_EXPLANATION", rs.getString("SCA_CATEGORY_EXPLANATION").replace("<br>", "\r"));	    
    	  			rst.put("SCA_CATEGORY_RECOMMEND", rs.getString("SCA_CATEGORY_RECOMMEND").replace("<br>", "\r"));	    
    	  			 
	    			rsval.add(rst);
	    			rst = null;  
	            }
	            rs.close();
	            pstmt.close();
	            connF.close();

	    		rs = null;
	      		pstmt = null;
	      		connF = null;
	    		
	    		returnObjectArray = rsval.toArray();
	    		rsval.clear();
	    		rsval = null;
	    		
	    		return returnObjectArray;
	    		
	    		
	    	} catch (SQLException sqlexception) {
	    		sqlexception.printStackTrace();
	    		ecamsLogger.error("## Cmr0250.getFortify_Result() SQLException START ##");
	    		ecamsLogger.error("## Error DESC : ", sqlexception);	
	    		ecamsLogger.error("## Cmr0250.getFortify_Result() SQLException END ##");			
	    		throw sqlexception;
	    	} catch (Exception exception) {
	    		exception.printStackTrace();
	    		ecamsLogger.error("## Cmr0250.getFortify_Result() Exception START ##");				
	    		ecamsLogger.error("## Error DESC : ", exception);	
	    		ecamsLogger.error("## Cmr0250.getFortify_Result() Exception END ##");				
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
	    				ecamsLogger.error("## Cmr0250.getFortify_Result() connection release exception ##");
	    				ex3.printStackTrace();
	    			}
	    		}
	    		if (connF != null){
	    			try{
	    				ConnectionResource.release(connF);
	    			}catch(Exception ex3){
	    				ecamsLogger.error("## Cmr0250.getFortify_Result() connection release exception ##");
	    				ex3.printStackTrace();
	    			}
	    		}
	    	}
	  }
	  
}//end of Cmr0250 class statement

