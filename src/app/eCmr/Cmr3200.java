/*****************************************************************************************
	1. program ID	: Cmr3200.java
	2. create date	: 2008.04.08
	3. auth		    : m.s.kang
	4. update date	: 2009.02.21
	5. auth		    : no name
	6. description	: Request List
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
import app.common.SystemPath;

	
/**
 * @author bigeyes
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
 

public class Cmr3200{
    /**
     * Logger Class Instance Creation
     * logger
     */
    Logger ecamsLogger  = EcamsLogger.getLoggerInstance();

	/**
	 * 요청현황을 조회합니다.
	 * @param  syscd,reqcd,teamcd,sta,requser,startdt,enddt,userid
	 * @return json
	 * @throws SQLException	
	 * @throws Exception
	 */
    public Object[] get_SelectList(String pSysCd,String pReqCd,String pTeamCd,String pStateCd,String pReqUser,String pOrderId,String pStartDt,String pEndDt,String pUserId,String emgSw) throws SQLException, Exception {
		Connection        conn        = null;
		PreparedStatement pstmt       = null;
		PreparedStatement pstmt2      = null;
		ResultSet         rs          = null;
		ResultSet         rs2         = null;
		StringBuffer      strQuery    = new StringBuffer();
		Object[] 		  returnObject= null;
		ArrayList<HashMap<String, String>>  rtList	= new ArrayList<HashMap<String, String>>();
		HashMap<String, String>			  	rst	 	= null;
		allTrim(pReqUser);
		 
		ConnectionContext connectionContext = new ConnectionResource();

		try {
	
			rtList.clear();
			
			String			  ConfName    	= "";
			String			  PgmSayu     	= "";
			String			  ColorSw     	= "";
	        Integer           Cnt         	= 0;
	        String            Sunhang	  	= "";
	        String            strOrderid    = "";
	        String			  strOrderTitle = "";
	        boolean           sysSw       	= false;
	        String            strSysGbn  	= "";
	        
			conn = connectionContext.getConnection();

			strQuery.setLength(0);
			strQuery.append("SELECT /*+FULL(A)*/ A.CR_ACPTNO,A.CR_ACPTDATE,A.CR_EMGCD,A.CR_ORDERID,   			\n");
			strQuery.append("       A.CR_EDITOR,A.CR_QRYCD,A.CR_SYSCD,A.CR_STATUS,A.CR_SAYU,        			\n");
			strQuery.append("       A.CR_GYULJAE,A.CR_PRCDATE,A.CR_SYSGB,a.cr_prcreq,               			\n");
			strQuery.append("       B.CM_SYSMSG,C.CM_DEPTNAME,D.CM_USERNAME,E.CM_CODENAME SIN,      			\n");
			strQuery.append("       to_char(a.cr_acptdate,'yyyy/mm/dd hh24:mi') as acptdate,        			\n");
			strQuery.append("       to_char(a.cr_prcdate,'yyyy/mm/dd hh24:mi') as prcdate,          			\n");
			strQuery.append("       substr(a.cr_acptno,1,4) || '-' || substr(a.cr_acptno,5,2) ||    			\n");
			strQuery.append("       '-' || substr(a.cr_acptno,7,6) as acptno,a.cr_passok,         				\n"); //g.CC_REQSUB,
			strQuery.append("       (select cm_codename from cmm0020 where cm_macode ='REQPASS' and cm_micode = a.cr_passok) passok          \n");
			strQuery.append("  FROM	CMM0020 E,CMM0040 D,CMM0100 C,CMM0030 B,CMR1000 A    						\n"); //CMC0420 G,
			strQuery.append(" WHERE (a.cr_qrycd in('01','02','04','06','11','16','60'))                         \n");

			if (pStateCd == null || "".equals(pStateCd)) {
				strQuery.append("and ((A.CR_PRCDATE IS NOT NULL                                    				\n");
				if(pStartDt.equals("")){
					strQuery.append("  and to_char(a.cr_acptdate,'yyyymmdd')>=to_char(sysdate,'yyyymmdd')      	\n");
					strQuery.append("  and to_char(a.cr_acptdate,'yyyymmdd')<=to_char(sysdate,'yyyymmdd'))	    \n");
				}else{
//					strQuery.append("  and to_char(a.cr_acptdate,'yyyymmdd')>=?                   \n");
//					strQuery.append("  and to_char(a.cr_acptdate,'yyyymmdd')<=?)                  \n");
					strQuery.append("	AND A.CR_ACPTDATE >= TO_DATE(?||'000000','YYYYMMDDHH24MISS')           \n");
					strQuery.append("	AND A.CR_ACPTDATE <= TO_DATE(?||'235959','YYYYMMDDHH24MISS'))          \n");
				}
				strQuery.append("  or   a.cr_status='0')  								 		  \n");
			} else if (pStateCd.equals("1")){
				strQuery.append("and   a.cr_status='0'                                            \n");
			} else if (pStateCd.equals("3")){
				strQuery.append("and   a.cr_status='3'                                            \n");
				strQuery.append("  and A.CR_PRCDATE IS NOT NULL                                   \n");
//				strQuery.append("  and to_char(a.cr_acptdate,'yyyymmdd')>=?                       \n");
//				strQuery.append("  and to_char(a.cr_acptdate,'yyyymmdd')<=?                       \n");
				strQuery.append("	AND A.CR_ACPTDATE >= TO_DATE(?||'000000','YYYYMMDDHH24MISS')  \n");
				strQuery.append("	AND A.CR_ACPTDATE <= TO_DATE(?||'235959','YYYYMMDDHH24MISS')  \n");
			}else if (pStateCd.equals("9")){
				strQuery.append("and   A.CR_PRCDATE IS NOT NULL and a.cr_status<>'3'              \n");
//				strQuery.append("  and to_char(a.cr_acptdate,'yyyymmdd')>=?                       \n");
//				strQuery.append("  and to_char(a.cr_acptdate,'yyyymmdd')<=?                       \n");
				strQuery.append("	AND A.CR_ACPTDATE >= TO_DATE(?||'000000','YYYYMMDDHH24MISS')  \n");
				strQuery.append("	AND A.CR_ACPTDATE <= TO_DATE(?||'235959','YYYYMMDDHH24MISS')  \n");
			}else if (pStateCd.equals("C1")){
				strQuery.append("and   A.CR_PRCDATE IS NOT NULL and a.cr_status<>'3'              \n");
				strQuery.append("and a.cr_acptno in (select a.cr_acptno from cmr1010 a,cmr0020 b  \n");
				strQuery.append("                     where b.cr_status='5'                       \n");
				strQuery.append("                       and b.cr_itemid=a.cr_itemid               \n");
				strQuery.append("                       and substr(a.cr_acptno,5,2) in ('01','02') \n");
				strQuery.append("                       and a.cr_confno is null                   \n");
				strQuery.append("                     group by a.cr_acptno)                       \n");
			}else if (pStateCd.equals("C2")){
				strQuery.append("and   A.CR_PRCDATE IS NOT NULL and a.cr_status<>'3'              \n");
				strQuery.append("and a.cr_acptno in (select a.cr_acptno from cmr1010 a,cmr0020 b  \n");
				strQuery.append("                     where b.cr_status='B'                       \n");
				strQuery.append("                       and b.cr_itemid=a.cr_itemid               \n");
				strQuery.append("                       and substr(a.cr_acptno,5,2)='03'          \n");
				strQuery.append("                       and a.cr_confno is null                   \n");
				strQuery.append("                     group by a.cr_acptno)                       \n");
			} else if (pStateCd.equals("C3")){
				strQuery.append("and a.cr_orderid is not null                                     \n");
				strQuery.append("and a.cr_orderid in                               			      \n");
				strQuery.append("                   (select cc_orderid from cmc0430				  \n");
				strQuery.append("                     where cc_orderid=a.cr_orderid)              \n");
				
			}
			if (pSysCd != null && !"".equals(pSysCd)) strQuery.append("and a.cr_syscd = ?         \n");
			if (pReqCd != null && !"".equals(pReqCd)){
				if(pReqCd.equals("04"))
					strQuery.append("and a.cr_qrycd in ('04', '60')								  \n");
				else
					strQuery.append("and a.cr_qrycd = ?               							  \n");
			}
			if (pTeamCd != null && !"".equals(pTeamCd))  {
				strQuery.append("and a.cr_teamcd in (select cm_deptcd from (select * from cmm0100 where cm_useyn='Y')  \n");
				strQuery.append("                      start with cm_updeptcd=?                   \n");
				strQuery.append("                      connect by prior cm_deptcd=cm_updeptcd     \n");
				strQuery.append("                     union    									  \n");
				strQuery.append("                     select ? from dual)     					  \n");
			}
			if (pReqUser != null && pReqUser.length() > 0) {
				strQuery.append("and a.cr_editor in (select cm_userid from cmm0040            	  \n");
				strQuery.append("                     where (cm_username=?                    	  \n");
				strQuery.append("                        or cm_userid=?))                    	  \n");
			} 
			if (pOrderId != null && pOrderId.length() > 0) {
//				strQuery.append("and (g.cc_orderid like ? or g.CC_REQSUB like ?)                  \n");
				strQuery.append("and a.cr_acptno in (select cr_acptno from cmr1012 where cr_acptno = a.cr_acptno	\n");
				strQuery.append("					    and (CR_ORDERID like ? or CR_REQSUB like ?)) 				\n");
			} 

			if ("2".equals(emgSw)){
				strQuery.append("and a.cr_passok='2'                                              \n");
			}
			strQuery.append("and a.cr_syscd=b.cm_syscd                                            \n");
			strQuery.append("and d.cm_project = c.cm_deptcd                                       \n");
			strQuery.append("and a.cr_editor = d.cm_userid                                        \n");
			strQuery.append("and e.cm_macode='REQUEST' and e.cm_micode=a.cr_qrycd                 \n");
//			strQuery.append("and a.cr_orderid=g.cc_orderid(+)                                     \n");
			strQuery.append("order by a.cr_acptdate desc                                          \n");

			//pstmt = conn.prepareStatement(strQuery.toString());
			pstmt = new LoggableStatement(conn,strQuery.toString());
            Cnt = 0;
            
			if (((pStateCd == null || "".equals(pStateCd)) &&!pStartDt.equals("")) 
				|| pStateCd.equals("3") 
				|| pStateCd.equals("9")) {
	            pstmt.setString(++Cnt, pStartDt);
	            pstmt.setString(++Cnt, pEndDt);
			}
            if (pSysCd != null && !"".equals(pSysCd)) pstmt.setString(++Cnt, pSysCd);
           
            if (pReqCd != null && !"".equals(pReqCd)){
				if(!pReqCd.equals("04"))
					pstmt.setString(++Cnt, pReqCd);
			}
			if (pTeamCd != null && !"".equals(pTeamCd)){
				pstmt.setString(++Cnt, pTeamCd);
				pstmt.setString(++Cnt, pTeamCd);
			}
			if (pReqUser != null && pReqUser.length() > 0){
				pstmt.setString(++Cnt, pReqUser);
				pstmt.setString(++Cnt, pReqUser);
			}
			if (pOrderId != null && pOrderId.length() > 0) {
				pstmt.setString(++Cnt, "%"+pOrderId+"%");
				pstmt.setString(++Cnt, "%"+pOrderId+"%");
			}
			ecamsLogger.error(((LoggableStatement)pstmt).getQueryString());
            rs = pstmt.executeQuery();

			while (rs.next()){
				
				ConfName = "";
				PgmSayu = "";
			    ColorSw = "0";
			    Sunhang = "";
			    strOrderid = "";
			    strOrderTitle = "";
			    strSysGbn = "";
			    sysSw = false;
			    
//	            if (rs.getString("cr_orderid") != null ) {
//	            	strOrderid = rs.getString("cr_orderid");
//	            	strOrderTitle = rs.getString("CC_REQSUB");
//	            }

	            if (rs.getString("cr_status").equals("3")) {
	            	ConfName = "반려";
	            	ColorSw = "3";
	            } else if (rs.getString("cr_status").equals("9")) {	//2009_0706 책임자 ->결재완료
	            	ConfName = "처리완료";
	            	ColorSw = "9";
	            } else {
	            	strQuery.setLength(0);
					strQuery.append("select cr_teamcd,cr_team,cr_confname,cr_congbn from cmr9900   \n");
				    strQuery.append(" where cr_acptno=? and cr_locat='00'        \n");
					pstmt2 = conn.prepareStatement(strQuery.toString());
					//pstmt2 = new LoggableStatement(conn,strQuery.toString());
		            pstmt2.setString(1, rs.getString("cr_acptno"));
		            ////ecamsLogger.error(((LoggableStatement)pstmt2).getQueryString());
		            rs2 = pstmt2.executeQuery();
		            if (rs2.next()){
		            	if (rs2.getString("cr_teamcd").equals("1")) sysSw = true;
		            	strSysGbn = rs2.getString("cr_team");
		            	ConfName = rs2.getString("cr_confname");
		            	if (rs2.getString("cr_congbn").equals("4")){
		            		ConfName = ConfName + "(후결)";
		            	}
		            }
		            rs2.close();
		            pstmt2.close();
	            }
	            if (rs.getString("cr_prcdate") == null && sysSw == true) {
	            	if (Integer.parseInt(rs.getString("cr_qrycd")) < 30 || "60".equals(rs.getString("cr_qrycd"))) {
	            		strQuery.setLength(0);
						strQuery.append("select count(*) as cnt from cmr1010   \n");
					    strQuery.append(" where cr_acptno=?                    \n");
					    strQuery.append("   and cr_putcode is not null         \n");
					    strQuery.append("   and cr_prcdate is null             \n");
					    strQuery.append("   and nvl(cr_putcode,'RTRY')!='RTRY' \n");
					    strQuery.append("   and nvl(cr_putcode,'0000')!='0000' \n");
					    pstmt2 = conn.prepareStatement(strQuery.toString());
						//pstmt2 = new LoggableStatement(conn,strQuery.toString());
					    pstmt2.setString(1, rs.getString("cr_acptno"));
			            ////ecamsLogger.error(((LoggableStatement)pstmt2).getQueryString());
			            rs2 = pstmt2.executeQuery();
			            if (rs2.next()) {
			            	if (!rs.getString("cr_qrycd").equals("46")){
			            		if (rs2.getInt("cnt") > 0) ColorSw = "5";
			            	}
			            }

			            rs2.close();
			            pstmt2.close();

			            if (!ColorSw.equals("5")) {
			            	strQuery.setLength(0);
			            	/*
			            	select count(*) as cnt from cmr1011 a,
			                (select cr_serno from cmr1010 
			                  where cr_acptno='201116000362'
			                    and cr_status='0'
			                    and nvl(cr_putcode,'0000')<>'0000') b 
			           where a.cr_acptno='201116000362'
			             and a.cr_prcsys='SYSUP'
			             and a.cr_serno=b.cr_serno
			             and (nvl(a.cr_prcrst,'0000')!='0000'
			              or nvl(a.cr_wait,'0')='W')*/
							strQuery.append("select count(*) as cnt from cmr1011 a,  \n");
							strQuery.append("     (select cr_serno from cmr1010      \n");
							strQuery.append("       where cr_acptno=?                \n");
							strQuery.append("         and cr_status='0'              \n");
							strQuery.append("         and nvl(cr_putcode,'0000')<>'0000') b \n");
						    strQuery.append(" where a.cr_acptno=?                    \n");
						    strQuery.append("   and a.cr_prcsys=?                    \n");
						    strQuery.append("   and a.cr_serno=b.cr_serno            \n");
						    strQuery.append("   and (nvl(a.cr_prcrst,'0000')!='0000' \n");
						    strQuery.append("    or nvl(a.cr_wait,'0')='W')          \n");
							pstmt2 = conn.prepareStatement(strQuery.toString());
							//pstmt2 = new LoggableStatement(conn,strQuery.toString());
				            pstmt2.setString(1, rs.getString("cr_acptno"));
				            pstmt2.setString(2, rs.getString("cr_acptno"));
				            pstmt2.setString(3, strSysGbn);
				            ////ecamsLogger.error(((LoggableStatement)pstmt2).getQueryString());
				            rs2 = pstmt2.executeQuery();
				            if (rs2.next()){
				            	if (rs2.getInt("cnt") > 0) ColorSw = "5";
				            }
				            rs2.close();
				            pstmt2.close();
		            	}
	            	}
	            }
	            else if (ColorSw.equals("0") && rs.getString("cr_prcdate") != null){
	            	ColorSw = "9";
	            }

	            if (Integer.parseInt(rs.getString("cr_qrycd")) < 30) {
	            	strQuery.setLength(0);
					strQuery.append("select a.cr_rsrcname from cmr1010 a            \n");
				    strQuery.append(" where a.cr_acptno=?                           \n");
				    strQuery.append("   and ((a.cr_itemid is null and a.cr_baseitem is null) \n");
				    strQuery.append("     or  a.cr_itemid=a.cr_baseitem)            \n");
					pstmt2 = conn.prepareStatement(strQuery.toString());
					//pstmt2 = new LoggableStatement(conn,strQuery.toString());
		            pstmt2.setString(1, rs.getString("cr_acptno"));
		            ////ecamsLogger.error(((LoggableStatement)pstmt2).getQueryString());
		            rs2 = pstmt2.executeQuery();
		            if (rs2.next()){
		            	PgmSayu = rs2.getString("cr_rsrcname");
		            }
		            rs2.close();
		            pstmt2.close();

	            	strQuery.setLength(0);
					strQuery.append("select count(*) as cnt from cmr1010 a          \n");
				    strQuery.append(" where a.cr_acptno=?                           \n");
				    strQuery.append("   and ((a.cr_itemid is null and a.cr_baseitem is null) \n");
				    strQuery.append("     or  a.cr_itemid=a.cr_baseitem)            \n");
					pstmt2 = conn.prepareStatement(strQuery.toString());
					//pstmt2 = new LoggableStatement(conn,strQuery.toString());
		            pstmt2.setString(1, rs.getString("cr_acptno"));
		            ////ecamsLogger.error(((LoggableStatement)pstmt2).getQueryString());
		            rs2 = pstmt2.executeQuery();
		            if (rs2.next()){
		            	if (rs2.getInt("cnt") > 1)
		            	   PgmSayu = PgmSayu + "외 " + Integer.toString(rs2.getInt("cnt") - 1) + "건";
		            }
		            rs2.close();
		            pstmt2.close();

	            } 
	            strQuery.setLength(0);
				strQuery.append("select count(*) as cnt from cmr1030 					 \n");
			    strQuery.append(" where cr_acptno=?                                      \n");;
				pstmt2 = conn.prepareStatement(strQuery.toString());
				//pstmt2 = new LoggableStatement(conn,strQuery.toString());
	            pstmt2.setString(1, rs.getString("cr_acptno"));
	            ////ecamsLogger.error(((LoggableStatement)pstmt2).getQueryString());
	            rs2 = pstmt2.executeQuery();
	            if (rs2.next()){
	            	if (rs2.getInt("cnt") > 0) Sunhang = "유";
	            	else Sunhang = "무";
	            }
	            rs2.close();
	            pstmt2.close();
	            
	            if (rs.getString("prcdate") != null) {
	            	ConfName = ConfName + "["+rs.getString("prcdate")+"]";
	            }
	            
	            rst = new HashMap<String, String>();
	            rst.put("rows",    Integer.toString(rs.getRow()));  //NO
        		rst.put("syscd",   rs.getString("cm_sysmsg"));      //시스템
        		rst.put("acptno",  rs.getString("acptno"));         //요청번호
        		rst.put("deptname",rs.getString("cm_deptname"));    //팀명
        		rst.put("editor",  rs.getString("cm_username"));    //요청자
        		rst.put("qrycd",   rs.getString("sin"));	        //요청구분
        		rst.put("sayu",    rs.getString("cr_sayu"));	    //요청구분
        		rst.put("acptdate",rs.getString("acptdate"));       //요청일시
        		rst.put("sta",     ConfName);                       //상태
        		rst.put("pgmid",   PgmSayu);  	                    //프로그램명
//        		rst.put("orderid",   strOrderid);                   //프로젝트
//        		rst.put("isrtitle",   strOrderTitle);               //프로젝트
        		rst.put("qrycd2",  rs.getString("cr_qrycd"));       //Qrycd
        		rst.put("editor2",  rs.getString("cr_editor"));     //Editor
        		rst.put("sysgb",  rs.getString("cr_sysgb"));        //SysGb
        		rst.put("endyn",  rs.getString("cr_status"));       //처리완료여부
        		rst.put("syscd2",  rs.getString("cr_syscd"));       //SysCd
        		rst.put("acptno2",  rs.getString("cr_acptno"));     //AcptNo
        		rst.put("Sunhang",  Sunhang);     //선행작업 유무
        		rst.put("cr_passok",  rs.getString("cr_passok"));     
        		rst.put("passok",  rs.getString("passok"));  
        			
				if (rs.getString("prcdate") != null) rst.put("prcdate", rs.getString("prcdate"));
				if (rs.getString("cr_prcreq") != null)
					rst.put("prcreq", rs.getString("cr_prcreq").substring(0, 4) + "/" +
							rs.getString("cr_prcreq").substring(4, 6) + "/" +
							rs.getString("cr_prcreq").substring(6, 8) + " " +
							rs.getString("cr_prcreq").substring(8, 10) + ":" +
							rs.getString("cr_prcreq").substring(10, 12));
				/*else {
					if (rs.getString("cr_passok").equals("0")) {

					} else {

					}
				}*/
        		rst.put("colorsw",  ColorSw);
        		rtList.add(rst);
        		rst = null;
			}//end of while-loop statement

			rs.close();
			pstmt.close();
			conn.close();
			//ecamsLogger.error("+++++ query end +++++");
			rs = null;
			pstmt = null;
			rs2 = null;
			pstmt2 = null;
			conn = null;

			returnObject = rtList.toArray();
			rtList.clear();
			rtList = null;

			return returnObject;

		} catch (SQLException sqlexception) {
			sqlexception.printStackTrace();
			ecamsLogger.error("## Cmr3200.SelectList() SQLException START ##");
			ecamsLogger.error("## Error DESC : ", sqlexception);
			ecamsLogger.error("## Cmr3200.SelectList() SQLException END ##");
			throw sqlexception;
		} catch (Exception exception) {
			exception.printStackTrace();
			ecamsLogger.error("## Cmr3200.SelectList() Exception START ##");
			ecamsLogger.error("## Error DESC : ", exception);
			ecamsLogger.error("## Cmr3200.SelectList() Exception END ##");
			throw exception;
		}finally{
			if (strQuery != null) 	strQuery = null;
			if (returnObject != null)	returnObject = null;
			if (rs != null)     try{rs.close();}catch (Exception ex){ex.printStackTrace();}
			if (rs2 != null)    try{rs2.close();}catch (Exception ex){ex.printStackTrace();}
			if (pstmt != null)  try{pstmt.close();}catch (Exception ex2){ex2.printStackTrace();}
			if (pstmt2 != null)  try{pstmt2.close();}catch (Exception ex2){ex2.printStackTrace();}
			if (conn != null){
				try{
					conn.close();
				}catch(Exception ex3){
					ecamsLogger.error("## Cmr3200.SelectList() connection release exception ##");
					ex3.printStackTrace();
				}
			}
		}
	}//end of SelectList() method statement
    
    public static String allTrim(String s)
    {
        if (s == null)
            return null;
        else if (s.length() == 0)
            return "";

        int len = s.length();
        int i = 0;
        int j = len;

        for (i = 0; i < len; i++) {
            if ( s.charAt(i) != ' ' && s.charAt(i) != 't' && s.charAt(i) != 'r' && s.charAt(i) != 'n' )
                break;
        }
        if (i == len)
            return "";

        for (j = len - 1; j >= i; j--) {
            if ( s.charAt(i) != ' ' && s.charAt(i) != 't' && s.charAt(i) != 'r' && s.charAt(i) != 'n' )
                break;
        }
        return s.substring(i, j + 1);
    }


    public Object[] get_SelectDashBoard(String UserId,String Cnt) throws SQLException, Exception {
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
			strQuery.append("SELECT '요청건' GBN,A.CR_ACPTNO,a.cr_qrycd    \n");
            strQuery.append("     , TO_CHAR(A.CR_ACPTDATE, 'YYYY-MM-DD hh24:mi') CR_ACPTDATE \n");
            strQuery.append("     , SUBSTR(A.CR_SAYU,0,55)               CR_TITLE    \n");
            strQuery.append("     , B.CM_CODENAME REQUEST                \n");
            strQuery.append("     , D.CM_USERNAME USERNAME               \n");
            strQuery.append("     , C.CR_CONFNAME CONFNAME               \n");
            strQuery.append("  FROM CMM0040 D,CMR9900 C,CMM0020 B,CMR1000 A        \n");
            strQuery.append(" WHERE A.CR_EDITOR=? AND A.CR_STATUS='0'    \n");
            strQuery.append("   AND A.CR_ACPTNO=C.CR_ACPTNO              \n");
            strQuery.append("   AND C.CR_LOCAT='00'                      \n");
            strQuery.append("   AND B.CM_MACODE='REQUEST'                \n");
            strQuery.append("   AND B.CM_MICODE=A.CR_QRYCD               \n");
            strQuery.append("   AND A.CR_EDITOR=D.CM_USERID              \n");
            strQuery.append("UNION             \n");
			strQuery.append("SELECT '결재대상' GBN,A.CR_ACPTNO,a.cr_qrycd  \n");
            strQuery.append("     , TO_CHAR(A.CR_ACPTDATE, 'YYYY-MM-DD') CR_ACPTDATE \n");
            strQuery.append("     , SUBSTR(A.CR_SAYU,0,55)               CR_TITLE    \n");
            strQuery.append("     , B.CM_CODENAME REQUEST                \n");
            strQuery.append("     , C.CM_USERNAME USERNAME               \n");
            strQuery.append("     , D.CR_CONFNAME CONFNAME               \n");
            strQuery.append("  FROM CMR9900 D,CMM0040 C,CMM0020 B,CMR1000 A \n");
            strQuery.append(" WHERE A.CR_STATUS NOT IN ('3','9')         \n");
            strQuery.append("   AND A.CR_ACPTNO=D.CR_ACPTNO              \n");
            strQuery.append("   AND D.CR_LOCAT='00' AND D.CR_STATUS='0'  \n");
            strQuery.append("   AND D.CR_TEAMCD IN ('2','3','5','6','7') \n");
            strQuery.append("   AND D.CR_TEAM=?                          \n");
            strQuery.append("   AND B.CM_MACODE='REQUEST'                \n");
            strQuery.append("   AND B.CM_MICODE=A.CR_QRYCD               \n");
            strQuery.append("   AND A.CR_EDITOR=C.CM_USERID              \n");
            strQuery.append("UNION             \n");
			strQuery.append("SELECT '결재대상' GBN,A.CR_ACPTNO,a.cr_qrycd  \n");
            strQuery.append("     , TO_CHAR(A.CR_ACPTDATE, 'YYYY-MM-DD') CR_ACPTDATE \n");
            strQuery.append("     , SUBSTR(A.CR_SAYU,0,55)               CR_TITLE    \n");
            strQuery.append("     , B.CM_CODENAME REQUEST                \n");
            strQuery.append("     , C.CM_USERNAME USERNAME               \n");
            strQuery.append("     , D.CR_CONFNAME CONFNAME               \n");
            strQuery.append("  FROM CMD0304 E,CMR9900 D,CMM0040 C,CMM0020 B,CMR1000 A \n");
            strQuery.append(" WHERE A.CR_STATUS NOT IN ('3','9')         \n");
            strQuery.append("   AND A.CR_ACPTNO=D.CR_ACPTNO              \n");
            strQuery.append("   AND D.CR_LOCAT='00' AND D.CR_STATUS='0'  \n");
            strQuery.append("   AND D.CR_TEAMCD = 'P'                    \n");
            strQuery.append("   AND D.CR_TEAM=E.CD_PRJJIK                \n");
            strQuery.append("   AND A.CR_isrid=E.CD_isrid                \n");
            strQuery.append("   AND E.CD_PRJUSER=? AND E.CD_CLOSEDT IS NULL\n");
            strQuery.append("   AND B.CM_MACODE='REQUEST'                \n");
            strQuery.append("   AND B.CM_MICODE=A.CR_QRYCD               \n");
            strQuery.append("   AND A.CR_EDITOR=C.CM_USERID              \n");
            strQuery.append("UNION             \n");
			strQuery.append("SELECT '결재대상' GBN,A.CR_ACPTNO,a.cr_qrycd  \n");
            strQuery.append("     , TO_CHAR(A.CR_ACPTDATE, 'YYYY-MM-DD') CR_ACPTDATE \n");
            strQuery.append("     , SUBSTR(A.CR_SAYU,0,55)               CR_TITLE    \n");
            strQuery.append("     , B.CM_CODENAME REQUEST                \n");
            strQuery.append("     , C.CM_USERNAME USERNAME               \n");
            strQuery.append("     , D.CR_CONFNAME CONFNAME               \n");
            strQuery.append("  FROM CMM0043 E,CMR9900 D,CMM0040 C,CMM0020 B,CMR1000 A \n");
            strQuery.append(" WHERE A.CR_STATUS NOT IN ('3','9')         \n");
            strQuery.append("   AND A.CR_ACPTNO=D.CR_ACPTNO              \n");
            strQuery.append("   AND D.CR_LOCAT='00' AND D.CR_STATUS='0'  \n");
            strQuery.append("   AND E.CM_USERID=?                        \n");
            strQuery.append("   AND D.CR_TEAMCD NOT IN ('2','3','5','6','7','P') \n");
            strQuery.append("   AND D.CR_TEAM=E.CM_RGTCD                 \n");
            strQuery.append("   AND B.CM_MACODE='REQUEST'                \n");
            strQuery.append("   AND B.CM_MICODE=A.CR_QRYCD               \n");
            strQuery.append("   AND A.CR_EDITOR=C.CM_USERID              \n");
            strQuery.append("   AND ROWNUM     <  ?                      \n");
            strQuery.append("   order by  GBN,CR_ACPTDATE desc             \n");
			
            pstmt = conn.prepareStatement(strQuery.toString());
            pstmt.setString(1, UserId);
            pstmt.setString(2, UserId);
            pstmt.setString(3, UserId);
            pstmt.setString(4, UserId);
            pstmt.setInt(5, Integer.parseInt(Cnt));
            
            rs = pstmt.executeQuery();
            //ecamsLogger.debug(rs.getRow()+"");
			while (rs.next()){
			//ecamsLogger.debug(rs.getString("CM_TITLE"));				
				rst = new HashMap<String, String>();
				rst.put("gbncd", rs.getString("GBN"));
				rst.put("cr_acptno", rs.getString("cr_acptno"));
				rst.put("cr_qrycd", rs.getString("cr_qrycd"));
				rst.put("acptdate", rs.getString("CR_ACPTDATE"));
				rst.put("cr_sayu", rs.getString("CR_TITLE"));
				rst.put("qrycd", rs.getString("REQUEST"));
				rst.put("editor", rs.getString("USERNAME"));
				rst.put("confname", rs.getString("CONFNAME"));
				rtList.add(rst);
				
				rst = null;
				if (rtList.size()>=Integer.parseInt(Cnt)) break;
			}//end of while-loop statement
			

			rs.close();
			pstmt.close();
			conn.close();

			rs = null;
			pstmt = null;
			conn = null;

			return rtList.toArray();

		} catch (SQLException sqlexception) {
			sqlexception.printStackTrace();
			ecamsLogger.error("## Cmr3200.get_SelectDashBoard() SQLException START ##");
			ecamsLogger.error("## Error DESC : ", sqlexception);
			ecamsLogger.error("## Cmr3200.get_SelectDashBoard() SQLException END ##");
			throw sqlexception;
		} catch (Exception exception) {
			exception.printStackTrace();
			ecamsLogger.error("## Cmr3200.get_SelectDashBoard() Exception START ##");
			ecamsLogger.error("## Error DESC : ", exception);
			ecamsLogger.error("## Cmr3200.get_SelectDashBoard() Exception END ##");
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
					ecamsLogger.error("## Cmr3200.get_SelectDashBoard() connection release exception ##");
					ex3.printStackTrace();
				}
			}
		}
	}//end of get_SelectDashBoard() method statement
    public String reqCncl(String AcptNo,String UserId,String conMsg,String ConfUsr) throws SQLException, Exception {
    	System.out.println(ConfUsr);
    	System.out.println(ConfUsr);
    	System.out.println(ConfUsr);
    	System.out.println(ConfUsr);
    	System.out.println(ConfUsr);
    	System.out.println(ConfUsr);
		Connection        conn        = null;
		Connection        connD        = null;
		PreparedStatement pstmt       = null;
		PreparedStatement pstmt2      = null;
		PreparedStatement pstmt3      = null;
		
		
		ResultSet         rs          = null;
		ResultSet         rs2          = null;
		StringBuffer      strQuery    = new StringBuffer();
		Runtime  run = null;
		//Process p = null;
		SystemPath		  systemPath = new SystemPath();
		String  binpath;
		String[] chkAry;
		//int		cmdrtn;

		ConnectionContext connectionContext = new ConnectionResource();
		ConnectionContext connectionContextD = new ConnectionResource(false,"D");
		

		try {
			//ecamsLogger.debug("+++++++++prcCd========"+prcCd);
			//binpath = systemPath.getTmpDir("14");
			conn = connectionContext.getConnection();
			connD = connectionContextD.getConnection();

			binpath = systemPath.getTmpDir_conn("14",conn);

			run = Runtime.getRuntime();

			chkAry = new String[4];
			chkAry[0] = "/bin/sh";
			chkAry[1] = binpath+"/procck2";
			chkAry[2] = "ecams_acct";
			chkAry[3] = AcptNo;

/*  로컬 테스트 위해 임시 주석처리 (20100719 호윤)
			p = run.exec(chkAry);
			p.waitFor();

			cmdrtn = p.exitValue();
			if (cmdrtn > 1) {
	        	pstmt = null;
	        	conn = null;

				//ecamsLogger.error(chkAry[0]+" " + chkAry[1] + " " + chkAry[2]);
				//ecamsLogger.error(cmdrtn);
				return "2";
			}

*/
			//2022.10.14 전체회수 할 때 process kill
			Process p = null;
			run = Runtime.getRuntime();

			chkAry = new String[4];
			chkAry[0] = "/bin/sh";
			chkAry[1] = binpath + "/prockill2";
			chkAry[2] = "ecams_acct";
			chkAry[3] = AcptNo;

			p = run.exec(chkAry);
			p.waitFor();
			
			ecamsLogger.error("========== [AllCancel] skip and process kill [UserId] " + UserId + " [AcptNo] " + AcptNo + " [exitValue] " + p.exitValue());
			
			conn.setAutoCommit(false);
			connD.setAutoCommit(false);
        	strQuery.setLength(0);
        	strQuery.append("Begin CMR9900_STR ( ");
        	strQuery.append("?, ?, ?, '9', ?, '9' ); End;");
        	pstmt = conn.prepareStatement(strQuery.toString());
        	pstmt.setString(1, AcptNo);
        	pstmt.setString(2,UserId);
        	pstmt.setString(3,conMsg);
        	pstmt.setString(4,ConfUsr);

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
        	strQuery.setLength(0);
    		strQuery.append("select a.cr_acptno,a.cr_confusr      \n");
    		strQuery.append("  from (Select cr_acptno             \n");        
    		strQuery.append("          from cmr1030               \n");
    		strQuery.append("          start with cr_befact=?     \n");
    		strQuery.append("        connect by prior             \n");
    		strQuery.append("             cr_acptno=cr_befact) b, \n");
    		strQuery.append("  cmr9900 a                          \n");
    		strQuery.append(" where b.cr_acptno=a.cr_acptno       \n");
    		strQuery.append("   and a.cr_locat='00'               \n");
    		strQuery.append("   and a.cr_status='0'               \n");
    		pstmt2 = conn.prepareStatement(strQuery.toString());
    		pstmt2 = new LoggableStatement(conn,strQuery.toString());
    		pstmt2.setString(1, AcptNo);
    		rs = pstmt2.executeQuery();
    		ecamsLogger.error(((LoggableStatement)pstmt2).getQueryString());
    		while (rs.next()) {
    			strQuery.setLength(0);
            	strQuery.append("Begin CMR9900_STR ( ");
            	strQuery.append("?, ?, ?, '9', ?, '9' ); End;");
            	pstmt = conn.prepareStatement(strQuery.toString());
            	pstmt.setString(1, rs.getString("cr_acptno"));
            	pstmt.setString(2,UserId);
            	pstmt.setString(3,"선행작업 회수로 인한 자동반려처리");
            	pstmt.setString(4,rs.getString("cr_confusr"));
            	
            	pstmt.executeUpdate();
            	pstmt.close();
            	
    		}
    		
    		rs.close();
    		pstmt2.close();
    		
    		connD.commit();
        	conn.close();
        	connD.close();
			conn = null;
			connD = null;
			pstmt = null;
			pstmt2 = null;
			pstmt3 = null;
			rs = null;
			rs2 = null;
        	return "0";

		} catch (SQLException sqlexception) {
			sqlexception.printStackTrace();
			if (rs != null)     try{rs.close();}catch (Exception ex){ex.printStackTrace();}
			if (rs2 != null)     try{rs2.close();}catch (Exception ex){ex.printStackTrace();}
			if (pstmt != null)  try{pstmt.close();}catch (Exception ex2){ex2.printStackTrace();}
			if (pstmt2 != null)  try{pstmt2.close();}catch (Exception ex2){ex2.printStackTrace();}
			if (pstmt3 != null)  try{pstmt3.close();}catch (Exception ex2){ex2.printStackTrace();}
			if (conn != null){
				try{
					conn.rollback();
					ConnectionResource.release(conn);
					conn = null;
				}catch(Exception ex3){
					ecamsLogger.error("## Cmr3200.reqCncl() connection release exception ##");
					ex3.printStackTrace();
				}				
			}
			if (connD != null){
				try{
					connD.rollback();
					ConnectionResource.release(connD);
					connD = null;
				}catch(Exception ex3){
					ecamsLogger.error("## Cmr3200.reqCncl() connection release exception ##");
					ex3.printStackTrace();
				}				
			}
			ecamsLogger.error("## Cmr3200.reqCncl() SQLException START ##");
			ecamsLogger.error("## Error DESC : ", sqlexception);	
			ecamsLogger.error("## Cmr3200.reqCncl() SQLException END ##");			
			throw sqlexception;
		} catch (Exception exception) {
			exception.printStackTrace();
			if (rs != null)     try{rs.close();}catch (Exception ex){ex.printStackTrace();}
			if (pstmt != null)  try{pstmt.close();}catch (Exception ex2){ex2.printStackTrace();}
			if (pstmt2 != null)  try{pstmt2.close();}catch (Exception ex2){ex2.printStackTrace();}
			if (pstmt3 != null)  try{pstmt3.close();}catch (Exception ex2){ex2.printStackTrace();}
			if (conn != null){
				try{
					conn.rollback();
					ConnectionResource.release(conn);
					conn = null;
				}catch(Exception ex3){
					ecamsLogger.error("## Cmr3200.reqCncl() connection release exception ##");
					ex3.printStackTrace();
				}				
			}
			if (connD != null){
				try{
					connD.rollback();
					ConnectionResource.release(connD);
					connD = null;
				}catch(Exception ex3){
					ecamsLogger.error("## Cmr3200.reqCncl() connection release exception ##");
					ex3.printStackTrace();
				}				
			}
			ecamsLogger.error("## Cmr0200.Cmr3200.reqCncl() SQLException START ##");
			ecamsLogger.error("## Error DESC : ", exception);	
			ecamsLogger.error("## Cmr0200.Cmr3200.reqCncl() SQLException END ##");			
			throw exception;
		}finally{
			if (strQuery != null) 	strQuery = null;
			if (pstmt != null)  try{pstmt.close();}catch (Exception ex2){ex2.printStackTrace();}
			if (pstmt2 != null)  try{pstmt2.close();}catch (Exception ex2){ex2.printStackTrace();}
			if (rs != null)  try{rs.close();}catch (Exception ex2){ex2.printStackTrace();}
			if (conn != null){
				try{
					conn.close();
				}catch(Exception ex3){
					ecamsLogger.error("## Cmr3200.reqCncl() connection release exception ##");
					ex3.printStackTrace();
				}
			}
			if (connD != null){
				try{
					connD.rollback();
					ConnectionResource.release(connD);
					connD = null;
				}catch(Exception ex3){
					ecamsLogger.error("## Cmr3200.reqCncl() connection release exception ##");
					ex3.printStackTrace();
				}
			}
		}
	}//end of reqCncl() method statement


    public String cnclYn(String AcptNo) throws SQLException, Exception {
		Connection        conn        = null;
		PreparedStatement pstmt       = null;
		PreparedStatement pstmt2      = null;
		ResultSet         rs          = null;
		ResultSet         rs2         = null;
		StringBuffer      strQuery    = new StringBuffer();
		String            retUser     = "";

		ConnectionContext connectionContext = new ConnectionResource();

		try {

			conn = connectionContext.getConnection();

        	strQuery.setLength(0);
        	strQuery.append("select count(*) as cnt from cmr9900 \n");
        	strQuery.append("where cr_acptno=? and cr_locat<>'00' \n");
        	strQuery.append("  and cr_status='9' and cr_teamcd<>'2' and cr_teamcd <> '3' \n");
        	strQuery.append("  and cr_team not in ('SYSFT','SYSPF','SYSUP','SYSCB') \n");
        	//pstmt =  new LoggableStatement(conn, strQuery.toString());
        	pstmt = conn.prepareStatement(strQuery.toString());
        	pstmt.setString(1, AcptNo);
        	//ecamsLogger.debug(((LoggableStatement)pstmt).getQueryString());
        	rs = pstmt.executeQuery();

        	if (rs.next()){
        		if (rs.getInt("cnt") == 0) {

        			strQuery.setLength(0);
        			strQuery.append("select cr_confusr from cmr9900           \n");
        			strQuery.append(" where cr_acptno=? and cr_locat='00'     \n");
        			strQuery.append("   and cr_status='0'                     \n");
        			//pstmt =  new LoggableStatement(conn, strQuery.toString());
        			pstmt2 = conn.prepareStatement(strQuery.toString());
                	pstmt2.setString(1, AcptNo);
                	//ecamsLogger.debug(((LoggableStatement)pstmt).getQueryString());
                	rs2 = pstmt2.executeQuery();
                	if (rs2.next()) {
                		retUser = rs2.getString("cr_confusr");
                	}
                	rs2.close();
                	pstmt2.close();
                	rs2 = null;
                	pstmt2 = null;
        		}
        	}
        	rs.close();
        	pstmt.close();
        	conn.close();
        	
        	rs = null;
        	pstmt = null;
        	conn = null;

        	return retUser;


		} catch (SQLException sqlexception) {
			sqlexception.printStackTrace();
			ecamsLogger.error("## Cmr3200.cnclYn() SQLException START ##");
			ecamsLogger.error("## Error DESC : ", sqlexception);
			ecamsLogger.error("## Cmr3200.cnclYn() SQLException END ##");
			throw sqlexception;
		} catch (Exception exception) {
			exception.printStackTrace();
			ecamsLogger.error("## Cmr3200.cnclYn() Exception START ##");
			ecamsLogger.error("## Error DESC : ", exception);
			ecamsLogger.error("## Cmr3200.cnclYn() Exception END ##");
			throw exception;
		}finally{
			if (strQuery != null) 	strQuery = null;
			if (rs != null)     try{rs.close();}catch (Exception ex){ex.printStackTrace();}
			if (rs2 != null)    try{rs2.close();}catch (Exception ex){ex.printStackTrace();}
			if (pstmt != null)  try{pstmt.close();}catch (Exception ex2){ex2.printStackTrace();}
			if (conn != null){
				try{
					conn.close();
				}catch(Exception ex3){
					ecamsLogger.error("## Cmr3200.cnclYn() connection release exception ##");
					ex3.printStackTrace();
				}
			}
		}
	}//end of cnclYn() method statement

    public String reqDelete(String AcptNo,String UserId,String QryCd,String ConfUsr) throws SQLException, Exception {
		Connection        conn        = null;
		PreparedStatement pstmt       = null;
		StringBuffer      strQuery    = new StringBuffer();

		ConnectionContext connectionContext = new ConnectionResource();

		try {

			conn = connectionContext.getConnection();
			conn.setAutoCommit(false);
        	strQuery.setLength(0);
        	strQuery.append("Begin CMR9900_STR ( ");
        	strQuery.append("?, ?, '삭제처리를 위한 사전작업', '9', ?, '9' ); End;");
        	pstmt = conn.prepareStatement(strQuery.toString());
        	pstmt.setString(1, AcptNo);
        	pstmt.setString(2,UserId);
        	pstmt.setString(3,ConfUsr);
        	pstmt.executeUpdate();
        	pstmt.close();

        	strQuery.setLength(0);
        	strQuery.append("delete cmr9920 where cr_acptno=?              \n");
        	pstmt = conn.prepareStatement(strQuery.toString());
        	pstmt.setString(1, AcptNo);
        	pstmt.executeUpdate();
        	pstmt.close();

        	strQuery.setLength(0);
        	strQuery.append("delete cmr9910 where cr_acptno=?              \n");
        	pstmt = conn.prepareStatement(strQuery.toString());
        	pstmt.setString(1, AcptNo);
        	pstmt.executeUpdate();
        	pstmt.close();

        	strQuery.setLength(0);
        	strQuery.append("delete cmr9900 where cr_acptno=?              \n");
        	pstmt = conn.prepareStatement(strQuery.toString());
        	pstmt.setString(1, AcptNo);
        	pstmt.executeUpdate();
        	pstmt.close();

        	strQuery.setLength(0);
        	strQuery.append("delete cmr1001 where cr_acptno=?              \n");
        	pstmt = conn.prepareStatement(strQuery.toString());
        	pstmt.setString(1, AcptNo);
        	pstmt.executeUpdate();
        	pstmt.close();

        	if (QryCd.substring(0,1).equals("3")) {
        		strQuery.setLength(0);
            	strQuery.append("delete cmr1100 where cr_acptno=?              \n");
            	pstmt = conn.prepareStatement(strQuery.toString());
            	pstmt.setString(1, AcptNo);
            	pstmt.executeUpdate();
            	pstmt.close();

        		strQuery.setLength(0);
            	strQuery.append("delete cmr1002 where cr_acptno=?              \n");
            	pstmt = conn.prepareStatement(strQuery.toString());
            	pstmt.setString(1, AcptNo);
            	pstmt.executeUpdate();
            	pstmt.close();
        	} else {
        		strQuery.setLength(0);
	        	strQuery.append("delete cmr1011 where cr_acptno=?              \n");
	        	pstmt = conn.prepareStatement(strQuery.toString());
	        	pstmt.setString(1, AcptNo);
	        	pstmt.executeUpdate();
	        	pstmt.close();

        		strQuery.setLength(0);
	        	strQuery.append("delete cmr1030 where cr_acptno=?              \n");
	        	pstmt = conn.prepareStatement(strQuery.toString());
	        	pstmt.setString(1, AcptNo);
	        	pstmt.executeUpdate();
	        	pstmt.close();

        		strQuery.setLength(0);
	        	strQuery.append("delete cmr1030 where cr_befact=?              \n");
	        	pstmt = conn.prepareStatement(strQuery.toString());
	        	pstmt.setString(1, AcptNo);
	        	pstmt.executeUpdate();
	        	pstmt.close();

        		strQuery.setLength(0);
	        	strQuery.append("delete cmr0027 where cr_acptno=?              \n");
	        	pstmt = conn.prepareStatement(strQuery.toString());
	        	pstmt.setString(1, AcptNo);
	        	pstmt.executeUpdate();
	        	pstmt.close();

        		strQuery.setLength(0);
	        	strQuery.append("delete cmr1010 where cr_acptno=?              \n");
	        	pstmt = conn.prepareStatement(strQuery.toString());
	        	pstmt.setString(1, AcptNo);
	        	pstmt.executeUpdate();
	        	pstmt.close();
        	}
        	strQuery.setLength(0);
        	strQuery.append("delete cmr1000 where cr_acptno=?              \n");
        	pstmt = conn.prepareStatement(strQuery.toString());
        	pstmt.setString(1, AcptNo);
        	pstmt.executeUpdate();
        	pstmt.close();
        	conn.commit();
        	conn.close();
			conn = null;
			pstmt = null;

        	return "0";

		} catch (SQLException sqlexception) {
			sqlexception.printStackTrace();
			ecamsLogger.error("## Cmr3200.reqDelete() SQLException START ##");
			ecamsLogger.error("## Error DESC : ", sqlexception);
			ecamsLogger.error("## Cmr3200.reqDelete() SQLException END ##");
			if (conn != null) conn.rollback();
			throw sqlexception;
		} catch (Exception exception) {
			exception.printStackTrace();
			ecamsLogger.error("## Cmr3200.reqDelete() Exception START ##");
			ecamsLogger.error("## Error DESC : ", exception);
			ecamsLogger.error("## Cmr3200.reqDelete() Exception END ##");
			if (conn != null) conn.rollback();
			throw exception;
		}finally{
			if (strQuery != null) 	strQuery = null;
			if (pstmt != null)  try{pstmt.close();}catch (Exception ex2){ex2.printStackTrace();}
			if (conn != null){
				try{
					conn.close();
				}catch(Exception ex3){
					ecamsLogger.error("## Cmr3200.reqDelete() connection release exception ##");
					ex3.printStackTrace();
				}
			}
		}
	}//end of reqDelete() method statement
    
    public Object[] get_SelectList2(String pSysCd,String pReqCd,String pTeamCd,String pReqUser,String pUserId, String pChk, String pStD, String pEdD) throws SQLException, Exception {
		Connection        conn        = null;
		PreparedStatement pstmt       = null;
		PreparedStatement pstmt2      = null;
		ResultSet         rs          = null;
		ResultSet         rs2         = null;
		StringBuffer      strQuery    = new StringBuffer();
		Object[] 		  returnObject= null;
		ArrayList<HashMap<String, String>>  rtList	= new ArrayList<HashMap<String, String>>();
		HashMap<String, String>			  	rst	 	= null;
		allTrim(pReqUser);
		 
		ConnectionContext connectionContext = new ConnectionResource();

		try {
	
			rtList.clear();
			
			String			  ConfName    	= "";
			String			  PgmSayu     	= "";
			String			  ColorSw     	= "";
	        Integer           Cnt         	= 0;
	        String            StrTeam    	= "";
	        String            Sunhang	  	= "";
	        String            strOrderid    = "";
	        String			  strOrderTitle = "";
	        boolean           sysSw       	= false;
	        String            strSysGbn 	= "";
	        
			conn = connectionContext.getConnection();

			strQuery.setLength(0);
			strQuery.append("select cm_project from cmm0040 where cm_userid = ?    \n");
			pstmt = conn.prepareStatement(strQuery.toString());
			pstmt = new LoggableStatement(conn,strQuery.toString());
            Cnt = 0;
			pstmt.setString(++Cnt, pUserId);
        	ecamsLogger.error(((LoggableStatement)pstmt).getQueryString());
            rs = pstmt.executeQuery();
            if (rs.next()){
            	StrTeam = rs.getString("cm_project"); 
			}
			rs.close();
			pstmt.close();

			
			strQuery.setLength(0);
			strQuery.append("SELECT /*+ USE_CONCAT */ A.CR_ACPTNO,A.CR_ACPTDATE,A.CR_EMGCD,A.CR_ORDERID,		\n");
			strQuery.append("       A.CR_EDITOR,A.CR_QRYCD,A.CR_SYSCD,A.CR_STATUS,A.CR_SAYU,       				\n");
			strQuery.append("       A.CR_GYULJAE,A.CR_PRCDATE,A.CR_SYSGB,a.cr_prcreq,               			\n");
			strQuery.append("       B.CM_SYSMSG,C.CM_DEPTNAME,D.CM_USERNAME,E.CM_CODENAME SIN,      			\n");
			strQuery.append("       to_char(a.cr_acptdate,'yyyy/mm/dd hh24:mi') as acptdate,        			\n");
			strQuery.append("       to_char(a.cr_prcdate,'yyyy/mm/dd hh24:mi') as prcdate,          			\n");
			strQuery.append("       substr(a.cr_acptno,1,4) || '-' || substr(a.cr_acptno,5,2) ||    			\n");
			strQuery.append("       '-' || substr(a.cr_acptno,7,6) as acptno,g.CC_REQSUB          				\n");
			strQuery.append("   , (SELECT COUNT(*)FROM CMR1015 WHERE CR_ACPTNO = A.CR_ACPTNO AND CR_STATUS != '3') AS CHKTOT  \n");
			strQuery.append("   , (SELECT COUNT(*)FROM CMR1015 WHERE CR_ACPTNO = A.CR_ACPTNO AND CR_STATUS  = '9') AS NOTCHK  \n");
			strQuery.append("  FROM	CMC0420 G, CMM0020 E,CMM0040 D,CMM0100 C,CMM0030 B,CMR1000 A    			\n");
			strQuery.append(" WHERE (a.cr_qrycd in('01','02','04','06','11','16','60'))                         \n");
		
			if (!"".equals(pSysCd) && pSysCd != null) strQuery.append("and a.cr_syscd = ?               		\n");
			if (!"".equals(pReqCd) && pReqCd != null){
				if("04".equals(pReqCd))
					strQuery.append("and a.cr_qrycd in ('04', '60')												\n");
				else
					strQuery.append("and a.cr_qrycd = ?               											\n");
			}
			if (!"".equals(pTeamCd) && pTeamCd != null)  {
				strQuery.append("and a.cr_teamcd in (select cm_deptcd from (select * from cmm0100 where cm_useyn='Y')  \n");
				strQuery.append("                      start with cm_updeptcd=?                   \n");
				strQuery.append("                      connect by prior cm_deptcd=cm_updeptcd     \n");
				strQuery.append("                     union    									  \n");
				strQuery.append("                     select ? from dual)     					  \n");
			}
			if (pReqUser != null && pReqUser.length() > 0) {
					strQuery.append("and a.cr_editor in (select cm_userid from cmm0040            \n");
					strQuery.append("                     where (cm_username=?                    \n");
					strQuery.append("                        or cm_userid=?))                     \n");
			} 
			strQuery.append("and a.cr_syscd=b.cm_syscd                                            \n");
			strQuery.append("and d.cm_project = c.cm_deptcd                                       \n");
			strQuery.append("and a.cr_editor = d.cm_userid                                        \n");
			strQuery.append("and e.cm_macode='REQUEST' and e.cm_micode=a.cr_qrycd                 \n");
			strQuery.append("and a.cr_orderid=g.cc_orderid(+)                                     \n");
		
			strQuery.append("AND ( exists (select 'x'                                             \n");
			strQuery.append("				from cmr1015                                          \n");
			strQuery.append("				 where cr_acptno = a.cr_acptno                        \n");
			strQuery.append("				 and cr_refuser = DECODE(?, A.CR_EDITOR, cr_refuser, ?)  \n");
			if (!"1".equals(pChk)) {
					strQuery.append("				 and cr_status = '0'                          \n");	
			}else {
				strQuery.append("and (     to_char(a.cr_acptdate, 'yyyymmdd') between ? and ?     \n");
				strQuery.append("		or cr_status = '0'                            			  \n");	
				strQuery.append("    )                         									  \n");	
			}
			strQuery.append("		)				                							  \n");		
			if ("1".equals(pChk)) {
				strQuery.append(" or  exists (select 'x'                                          \n");
				strQuery.append("				from cmr1015, cmm0040                             \n");
				strQuery.append("				 where cr_acptno = a.cr_acptno                    \n");
				strQuery.append("				 and cr_refuser = cm_userid						  \n");
				strQuery.append("				 and cm_project = ? 							  \n");
				strQuery.append("                and (     to_char(a.cr_acptdate, 'yyyymmdd') between ? and ?     \n");
				strQuery.append("	                	or cr_status = '0'                        \n");	
				strQuery.append("                    )                         					  \n");	
				strQuery.append("		     ) 				                					  \n");				
			}
			strQuery.append(" )				                									  \n");
			strQuery.append("order by a.cr_acptdate desc                                          \n");

			//pstmt = conn.prepareStatement(strQuery.toString());
			pstmt = new LoggableStatement(conn,strQuery.toString());
            Cnt = 0;
            
            if (!"".equals(pSysCd) && pSysCd != null) pstmt.setString(++Cnt, pSysCd);
           
            if (!"".equals(pReqCd) && pReqCd != null){
				if(!"04".equals(pReqCd))
					pstmt.setString(++Cnt, pReqCd);
			}
			if (!"".equals(pTeamCd) && pTeamCd != null){
				pstmt.setString(++Cnt, pTeamCd);
				pstmt.setString(++Cnt, pTeamCd);
			}
			if (pReqUser != null && pReqUser.length() > 0){
				pstmt.setString(++Cnt, pReqUser);
				pstmt.setString(++Cnt, pReqUser);
			}
			
			pstmt.setString(++Cnt, pUserId);
			pstmt.setString(++Cnt, pUserId);
			if ("1".equals(pChk)) {
				pstmt.setString(++Cnt, pStD);
				pstmt.setString(++Cnt, pEdD);
			}
			
			if ("1".equals(pChk)) {
				pstmt.setString(++Cnt, StrTeam);
				pstmt.setString(++Cnt, pStD);
				pstmt.setString(++Cnt, pEdD);
			}

			ecamsLogger.error(((LoggableStatement)pstmt).getQueryString());
            rs = pstmt.executeQuery();

			while (rs.next()){
				
				ConfName = "";
				PgmSayu = "";
			    ColorSw = "0";
			    Sunhang = "";
			    strOrderid = "";
			    strOrderTitle = "";
			    strSysGbn = "";
			    sysSw = false;
			    
	            if (rs.getString("cr_orderid") != null ) {
	            	strOrderid = rs.getString("cr_orderid");
	            	strOrderTitle = rs.getString("CC_REQSUB");
	            }

	            if (rs.getString("cr_status").equals("3")) {
	            	ConfName = "반려";
	            	ColorSw = "3";
	            } else if (rs.getString("cr_status").equals("9")) {	//2009_0706 책임자 ->결재완료
	            	ConfName = "처리완료";
	            	ColorSw = "9";
	            } else {
	            	strQuery.setLength(0);
					strQuery.append("select cr_teamcd,cr_team,cr_confname,cr_congbn from cmr9900   \n");
				    strQuery.append(" where cr_acptno=? and cr_locat='00'        \n");
					pstmt2 = conn.prepareStatement(strQuery.toString());
					//pstmt2 = new LoggableStatement(conn,strQuery.toString());
		            pstmt2.setString(1, rs.getString("cr_acptno"));
		            ////ecamsLogger.error(((LoggableStatement)pstmt2).getQueryString());
		            rs2 = pstmt2.executeQuery();
		            if (rs2.next()){
		            	if (rs2.getString("cr_teamcd").equals("1")) sysSw = true;
		            	strSysGbn = rs2.getString("cr_team");
		            	ConfName = rs2.getString("cr_confname");
		            	if (rs2.getString("cr_congbn").equals("4")){
		            		ConfName = ConfName + "(후결)";
		            	}
		            }
		            rs2.close();
		            pstmt2.close();
	            }
	            if (rs.getString("cr_prcdate") == null && sysSw == true) {
	            	if (Integer.parseInt(rs.getString("cr_qrycd")) < 30) {
	            		strQuery.setLength(0);
						strQuery.append("select count(*) as cnt from cmr1010   \n");
					    strQuery.append(" where cr_acptno=?                    \n");
					    strQuery.append("   and cr_putcode is not null         \n");
					    strQuery.append("   and cr_prcdate is null             \n");
					    strQuery.append("   and nvl(cr_putcode,'RTRY')!='RTRY' \n");
					    strQuery.append("   and nvl(cr_putcode,'0000')!='0000' \n");
					    pstmt2 = conn.prepareStatement(strQuery.toString());
						//pstmt2 = new LoggableStatement(conn,strQuery.toString());
					    pstmt2.setString(1, rs.getString("cr_acptno"));
			            ////ecamsLogger.error(((LoggableStatement)pstmt2).getQueryString());
			            rs2 = pstmt2.executeQuery();
			            if (rs2.next()) {
			            	if (!rs.getString("cr_qrycd").equals("46")){
			            		if (rs2.getInt("cnt") > 0) ColorSw = "5";
			            	}
			            }

			            rs2.close();
			            pstmt2.close();

			            if (!ColorSw.equals("5")) {
			            	strQuery.setLength(0);
			            	/*
			            	select count(*) as cnt from cmr1011 a,
			                (select cr_serno from cmr1010 
			                  where cr_acptno='201116000362'
			                    and cr_status='0'
			                    and nvl(cr_putcode,'0000')<>'0000') b 
			           where a.cr_acptno='201116000362'
			             and a.cr_prcsys='SYSUP'
			             and a.cr_serno=b.cr_serno
			             and (nvl(a.cr_prcrst,'0000')!='0000'
			              or nvl(a.cr_wait,'0')='W')*/
							strQuery.append("select count(*) as cnt from cmr1011 a,  \n");
							strQuery.append("     (select cr_serno from cmr1010      \n");
							strQuery.append("       where cr_acptno=?                \n");
							strQuery.append("         and cr_status='0'              \n");
							strQuery.append("         and nvl(cr_putcode,'0000')<>'0000') b \n");
						    strQuery.append(" where a.cr_acptno=?                    \n");
						    strQuery.append("   and a.cr_prcsys=?                    \n");
						    strQuery.append("   and a.cr_serno=b.cr_serno            \n");
						    strQuery.append("   and (nvl(a.cr_prcrst,'0000')!='0000' \n");
						    strQuery.append("    or nvl(a.cr_wait,'0')='W')          \n");
							pstmt2 = conn.prepareStatement(strQuery.toString());
							//pstmt2 = new LoggableStatement(conn,strQuery.toString());
				            pstmt2.setString(1, rs.getString("cr_acptno"));
				            pstmt2.setString(2, rs.getString("cr_acptno"));
				            pstmt2.setString(3, strSysGbn);
				            ////ecamsLogger.error(((LoggableStatement)pstmt2).getQueryString());
				            rs2 = pstmt2.executeQuery();
				            if (rs2.next()){
				            	if (rs2.getInt("cnt") > 0) ColorSw = "5";
				            }
				            rs2.close();
				            pstmt2.close();
		            	}
	            	}
	            }
	            else if (ColorSw.equals("0") && rs.getString("cr_prcdate") != null){
	            	ColorSw = "9";
	            }

	            if (Integer.parseInt(rs.getString("cr_qrycd")) < 30) {
	            	strQuery.setLength(0);
					strQuery.append("select a.cr_rsrcname from cmr1010 a            \n");
				    strQuery.append(" where a.cr_acptno=?                           \n");
				    strQuery.append("   and ((a.cr_itemid is null and a.cr_baseitem is null) \n");
				    strQuery.append("     or  a.cr_itemid=a.cr_baseitem)            \n");
					pstmt2 = conn.prepareStatement(strQuery.toString());
					//pstmt2 = new LoggableStatement(conn,strQuery.toString());
		            pstmt2.setString(1, rs.getString("cr_acptno"));
		            ////ecamsLogger.error(((LoggableStatement)pstmt2).getQueryString());
		            rs2 = pstmt2.executeQuery();
		            if (rs2.next()){
		            	PgmSayu = rs2.getString("cr_rsrcname");
		            }
		            rs2.close();
		            pstmt2.close();

	            	strQuery.setLength(0);
					strQuery.append("select count(*) as cnt from cmr1010 a          \n");
				    strQuery.append(" where a.cr_acptno=?                           \n");
				    strQuery.append("   and ((a.cr_itemid is null and a.cr_baseitem is null) \n");
				    strQuery.append("     or  a.cr_itemid=a.cr_baseitem)            \n");
					pstmt2 = conn.prepareStatement(strQuery.toString());
					//pstmt2 = new LoggableStatement(conn,strQuery.toString());
		            pstmt2.setString(1, rs.getString("cr_acptno"));
		            ////ecamsLogger.error(((LoggableStatement)pstmt2).getQueryString());
		            rs2 = pstmt2.executeQuery();
		            if (rs2.next()){
		            	if (rs2.getInt("cnt") > 1)
		            	   PgmSayu = PgmSayu + "외 " + Integer.toString(rs2.getInt("cnt") - 1) + "건";
		            }
		            rs2.close();
		            pstmt2.close();

	            } 
	            strQuery.setLength(0);
				strQuery.append("select count(*) as cnt from cmr1030 					 \n");
			    strQuery.append(" where cr_acptno=?                                      \n");;
				pstmt2 = conn.prepareStatement(strQuery.toString());
				//pstmt2 = new LoggableStatement(conn,strQuery.toString());
	            pstmt2.setString(1, rs.getString("cr_acptno"));
	            ////ecamsLogger.error(((LoggableStatement)pstmt2).getQueryString());
	            rs2 = pstmt2.executeQuery();
	            if (rs2.next()){
	            	if (rs2.getInt("cnt") > 0) Sunhang = "유";
	            	else Sunhang = "무";
	            }
	            rs2.close();
	            pstmt2.close();
	            
	            if (rs.getString("prcdate") != null) {
	            	ConfName = ConfName + "["+rs.getString("prcdate")+"]";
	            }
	            
	            rst = new HashMap<String, String>();
	            rst.put("rows",    Integer.toString(rs.getRow()));  //NO
        		rst.put("syscd",   rs.getString("cm_sysmsg"));      //시스템
        		rst.put("acptno",  rs.getString("acptno"));         //요청번호
        		rst.put("deptname",rs.getString("cm_deptname"));    //팀명
        		rst.put("editor",  rs.getString("cm_username"));    //요청자
        		rst.put("qrycd",   rs.getString("sin"));	        //요청구분
        		rst.put("sayu",    rs.getString("cr_sayu"));	    //요청구분
        		rst.put("acptdate",rs.getString("acptdate"));       //요청일시
        		rst.put("sta",     ConfName);                       //상태
        		rst.put("pgmid",   PgmSayu);  	                    //프로그램명
        		rst.put("orderid",   strOrderid);                          //프로젝트
        		rst.put("isrtitle",   strOrderTitle);                     //프로젝트
        		rst.put("qrycd2",  rs.getString("cr_qrycd"));       //Qrycd
        		rst.put("editor2",  rs.getString("cr_editor"));     //Editor
        		rst.put("sysgb",  rs.getString("cr_sysgb"));        //SysGb
        		rst.put("endyn",  rs.getString("cr_status"));       //처리완료여부
        		rst.put("syscd2",  rs.getString("cr_syscd"));       //SysCd
        		rst.put("acptno2",  rs.getString("cr_acptno"));     //AcptNo
        		rst.put("Sunhang",  Sunhang);     //선행작업 유무

				if (rs.getString("prcdate") != null) rst.put("prcdate", rs.getString("prcdate"));
				if (rs.getString("cr_prcreq") != null)
					rst.put("prcreq", rs.getString("cr_prcreq").substring(0, 4) + "/" +
							rs.getString("cr_prcreq").substring(4, 6) + "/" +
							rs.getString("cr_prcreq").substring(6, 8) + " " +
							rs.getString("cr_prcreq").substring(8, 10) + ":" +
							rs.getString("cr_prcreq").substring(10, 12));
				/*else {
					if (rs.getString("cr_passok").equals("0")) {

					} else {

					}
				}*/
				if (rs.getInt("NOTCHK") >= rs.getInt("CHKTOT")) {
					rst.put("chksta", "확인완료");
				}
				else if (rs.getInt("NOTCHK") == 0) {
					rst.put("chksta", "미진행");
				}
				else {
					rst.put("chksta", "확인중(" + Integer.toString(rs.getInt("NOTCHK")) + "/" + Integer.toString(rs.getInt("CHKTOT")) + ")");
				}
				
				rst.put("Sunhang",  Sunhang);     //선행작업 유무
        		
        		rst.put("colorsw",  ColorSw);
        		rtList.add(rst);
        		rst = null;
			}//end of while-loop statement

			rs.close();
			pstmt.close();
			conn.close();
			//ecamsLogger.error("+++++ query end +++++");
			rs = null;
			pstmt = null;
			rs2 = null;
			pstmt2 = null;
			conn = null;

			returnObject = rtList.toArray();
			rtList.clear();
			rtList = null;

			return returnObject;

		} catch (SQLException sqlexception) {
			sqlexception.printStackTrace();
			ecamsLogger.error("## Cmr3200.SelectList2() SQLException START ##");
			ecamsLogger.error("## Error DESC : ", sqlexception);
			ecamsLogger.error("## Cmr3200.SelectList2() SQLException END ##");
			throw sqlexception;
		} catch (Exception exception) {
			exception.printStackTrace();
			ecamsLogger.error("## Cmr3200.SelectList2() Exception START ##");
			ecamsLogger.error("## Error DESC : ", exception);
			ecamsLogger.error("## Cmr3200.SelectList2() Exception END ##");
			throw exception;
		}finally{
			if (strQuery != null) 	strQuery = null;
			if (returnObject != null)	returnObject = null;
			if (rs != null)     try{rs.close();}catch (Exception ex){ex.printStackTrace();}
			if (rs2 != null)    try{rs2.close();}catch (Exception ex){ex.printStackTrace();}
			if (pstmt != null)  try{pstmt.close();}catch (Exception ex2){ex2.printStackTrace();}
			if (pstmt2 != null)  try{pstmt2.close();}catch (Exception ex2){ex2.printStackTrace();}
			if (conn != null){
				try{
					conn.close();
				}catch(Exception ex3){
					ecamsLogger.error("## Cmr3200.SelectList2() connection release exception ##");
					ex3.printStackTrace();
				}
			}
		}
	}//end of SelectList2() method statement
    
    public Object[] getAsta(String Acptno,String userid,String gubun) throws SQLException, Exception {
		Connection        conn        = null;
		PreparedStatement pstmt       = null;
		PreparedStatement pstmt2      = null;
		ResultSet         rs          = null;
		ResultSet         rs2         = null;
		StringBuffer      strQuery    = new StringBuffer();
		Object[] 		  returnObject= null;
		ArrayList<HashMap<String, String>>  rtList	= new ArrayList<HashMap<String, String>>();
		HashMap<String, String>			  	rst	 	= null;
		 
		ConnectionContext connectionContext = new ConnectionResource();

		try {
	
			rtList.clear();
			
			String			  ConfName    = "";
			String			  PgmSayu     = "";
			String			  ColorSw     = "";
	        Integer           Cnt         = 0;
	        //String            StrAdmin    = "N";
	        String            StrTeam     = "";
	        //String            StrTeam2    = "";
	        String            Sunhang	  = "";
	        String            strOrderid    = "";
	        String			  strOrderTitle = "";
	        boolean           sysSw       = false;
	        String            strSysGbn  = "";
	        
			conn = connectionContext.getConnection();

			strQuery.setLength(0);
			strQuery.append("select cm_project from cmm0040 where cm_userid = ?    \n");
			pstmt = conn.prepareStatement(strQuery.toString());
			pstmt = new LoggableStatement(conn,strQuery.toString());
            Cnt = 0;
			pstmt.setString(++Cnt, userid);
        	ecamsLogger.error(((LoggableStatement)pstmt).getQueryString());
            rs = pstmt.executeQuery();
            if (rs.next()){
            	StrTeam = rs.getString("cm_project"); 
			}
			rs.close();
			pstmt.close();

			
			
			strQuery.setLength(0);
			
			if (Acptno.substring(4,6).equals("04")){
				strQuery.append("select f.cm_username,h.cr_rsrcname as cr_rsrcname2,h.cr_story as cr_story2,b.cr_rsrcname,b.cr_story,d.cm_username as cr_refusernm, d.cm_project as refproject,  \n");
				strQuery.append("root_rsrcname2(a.cr_itemid) as modelnm, root_rsrcname2(a.cr_refitemid) as refmodelnm,        \n");
				strQuery.append("a.cr_chkflag,e.cm_username as cr_chkusser,to_char(a.cr_chkdate,'yyyy/mm/dd hh24:mi') as cr_chkdate,        \n");
				strQuery.append("a.cr_acptno,g.cm_sysmsg, a.cr_refuser,a.cr_status,a.cr_itemid,a.cr_refitemid, i.cr_editor,         		\n");
				strQuery.append("(select cm_codename from cmm0020 where cm_macode = 'CMR0020' AND CM_MICODE = h.CR_STATUS) rsrcstat1,       \n");
				strQuery.append("(select cm_codename from cmm0020 where cm_macode = 'CMR0020' AND CM_MICODE = b.CR_STATUS) rsrcstat2,       \n");
				strQuery.append("d.cm_project,       \n");
				strQuery.append("nvl((select cm_codename from cmm0020 where cm_macode = 'ASTA' and cm_micode = a.cr_chkflag), '미확인') as chkflag,       \n");
				strQuery.append("(select cm_deptname from cmm0100 where cm_deptcd = d.cm_project and a.cr_refuser = d.cm_userid) as cm_deptname					\n");
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
				if(gubun.equals("1")){
					strQuery.append("and a.cr_status !='0'           		\n");
				}else if(gubun.equals("2")){
					strQuery.append("and a.cr_status ='0'           		\n");
				}
			} else { 
				strQuery.append("select f.cm_username,h.cr_rsrcname as cr_rsrcname2,h.cr_story as cr_story2,b.cr_rsrcname,b.cr_story,d.cm_username as cr_refusernm, d.cm_project as refproject,    \n");
				strQuery.append("root_rsrcname2(a.cr_itemid) as modelnm, root_rsrcname2(a.cr_refitemid) as refmodelnm,        \n");
				strQuery.append("a.cr_chkflag,e.cm_username as cr_chkusser,to_char(a.cr_chkdate,'yyyy/mm/dd hh24:mi') as cr_chkdate,        \n");
				strQuery.append("a.cr_acptno,g.cm_sysmsg, a.cr_refuser,a.cr_status,a.cr_itemid,a.cr_refitemid, i.cr_editor,         \n");
				strQuery.append("(select cm_codename from cmm0020 where cm_macode = 'CMR0020' AND CM_MICODE = h.CR_STATUS) rsrcstat1,       \n");
				strQuery.append("(select cm_codename from cmm0020 where cm_macode = 'CMR0020' AND CM_MICODE = b.CR_STATUS) rsrcstat2,       \n");
				strQuery.append("d.cm_project,       \n");
				strQuery.append("nvl((select cm_codename from cmm0020 where cm_macode = 'ASTA' and cm_micode = a.cr_chkflag), '미확인') as chkflag,       \n");
				strQuery.append("(select cm_deptname from cmm0100 where cm_deptcd = d.cm_project) as cm_deptname					\n");
				strQuery.append("from cmr1015 a, cmr0020 b,cmm0040 d,cmm0040 e,cmm0040 f,cmm0030 g,cmr0020 h,cmr1000 i       \n");
				strQuery.append("where a.cr_acptno = ?        \n");
				strQuery.append("and f.cm_userid = i.cr_editor        \n");
				strQuery.append("and i.cr_acptno = a.cr_acptno        \n");
				strQuery.append("and b.cr_itemid = a.cr_refitemid        \n");
				strQuery.append("and h.cr_itemid = a.cr_itemid        \n");
				strQuery.append("and a.cr_refuser = d.cm_userid          	\n");
				strQuery.append("and a.cr_chkusser = e.cm_userid(+)           		\n");
				strQuery.append("and b.cr_syscd = g.cm_syscd           		\n");
				if(gubun.equals("1")){
					strQuery.append("and a.cr_status !='0'           		\n");
				}else if(gubun.equals("2")){
					strQuery.append("and a.cr_status ='0'           		\n");
				}
				
			}
			pstmt = conn.prepareStatement(strQuery.toString());
			pstmt = new LoggableStatement(conn,strQuery.toString());
            Cnt = 0;
			if (Acptno.substring(4,6).equals("04")){
				pstmt.setString(++Cnt, Acptno);
			}else {
				pstmt.setString(++Cnt, Acptno);			
			}
        	ecamsLogger.error(((LoggableStatement)pstmt).getQueryString());
            rs = pstmt.executeQuery();

			while (rs.next()){
				ColorSw = "0";
			    rst = new HashMap<String, String>();
	            rst.put("ID",Integer.toString(rs.getRow()));//NO
	            rst.put("cr_rsrcname",    rs.getString("cr_rsrcname"));  //참조파일명
        		rst.put("cr_story",   rs.getString("cr_story"));      //참조사유
        		rst.put("cr_rsrcname2",    rs.getString("cr_rsrcname2"));  //파일명
        		rst.put("cr_story2",   rs.getString("cr_story2"));      //사유
        		rst.put("cr_refuser",  rs.getString("cr_refusernm") + "(" + rs.getString("cr_refuser") +")");    //확인대상자
        		rst.put("cr_chkflag",rs.getString("chkflag"));    //확인구분
        		rst.put("cr_chkusser",  rs.getString("cr_chkusser"));    //확인자
        		rst.put("cr_chkdate",  rs.getString("cr_chkdate"));    //확인일시
        		rst.put("cr_acptno",  rs.getString("cr_acptno"));    //신청번호
        		rst.put("cm_username",  rs.getString("cm_username") + "(" + rs.getString("cr_editor") +")");    //신청자
        		rst.put("cm_sysmsg",  rs.getString("cm_sysmsg"));    //시스템명
        		rst.put("cr_itemid",  rs.getString("cr_itemid"));    //시스템명
        		rst.put("cr_refitemid",  rs.getString("cr_refitemid"));    //시스템명
        		rst.put("modelnm",  rs.getString("modelnm"));    //모델명
        		rst.put("refmodelnm",  rs.getString("refmodelnm"));    //c모델명
        		rst.put("rsrcstat1",  rs.getString("rsrcstat1"));    //프로그램상태
        		rst.put("rsrcstat2",  rs.getString("rsrcstat2"));    //참조프로그램상태
        		
        		rst.put("cr_editor", rs.getString("cr_editor"));    //신청자
        		rst.put("cr_editor2", rs.getString("cr_refuser"));    //확인자
        		rst.put("refproject", rs.getString("refproject"));    //확인자 부서코드
        		rst.put("cm_deptname", rs.getString("cm_deptname"));    //확인자 부서명
        		
        		String tmp = rs.getString("cr_chkflag");
        		if((userid.equals(rs.getString("cr_refuser")) || userid.equals(rs.getString("cr_editor")) || StrTeam.equals(rs.getString("cm_project")))  && rs.getString("cr_status").equals("0")){
        			rst.put("enabled", "1");
        		}else{
        			rst.put("enabled", "0");
        		}
        		rst.put("selected", "0");
        	
        		rtList.add(rst);
        		rst = null;
			}//end of while-loop statement

			rs.close();
			pstmt.close();
			conn.close();
			//ecamsLogger.error("+++++ query end +++++");
			rs = null;
			pstmt = null;
			rs2 = null;
			pstmt2 = null;
			conn = null;

			returnObject = rtList.toArray();
			rtList.clear();
			rtList = null;

			return returnObject;

		} catch (SQLException sqlexception) {
			sqlexception.printStackTrace();
			ecamsLogger.error("## Cmr3200.SelectList() SQLException START ##");
			ecamsLogger.error("## Error DESC : ", sqlexception);
			ecamsLogger.error("## Cmr3200.SelectList() SQLException END ##");
			throw sqlexception;
		} catch (Exception exception) {
			exception.printStackTrace();
			ecamsLogger.error("## Cmr3200.SelectList() Exception START ##");
			ecamsLogger.error("## Error DESC : ", exception);
			ecamsLogger.error("## Cmr3200.SelectList() Exception END ##");
			throw exception;
		}finally{
			if (strQuery != null) 	strQuery = null;
			if (returnObject != null)	returnObject = null;
			if (rs != null)     try{rs.close();}catch (Exception ex){ex.printStackTrace();}
			if (rs2 != null)    try{rs2.close();}catch (Exception ex){ex.printStackTrace();}
			if (pstmt != null)  try{pstmt.close();}catch (Exception ex2){ex2.printStackTrace();}
			if (pstmt2 != null)  try{pstmt2.close();}catch (Exception ex2){ex2.printStackTrace();}
			if (conn != null){
				try{
					conn.close();
				}catch(Exception ex3){
					ecamsLogger.error("## Cmr3200.SelectList() connection release exception ##");
					ex3.printStackTrace();
				}
			}
		}
	}//end of SelectList() method statement
    
    public Object[] getAsta2(String Acptno,String userid) throws SQLException, Exception {
  		Connection        conn        = null;
  		PreparedStatement pstmt       = null;
  		PreparedStatement pstmt2      = null;
  		ResultSet         rs          = null;
  		ResultSet         rs2         = null;
  		StringBuffer      strQuery    = new StringBuffer();
  		Object[] 		  returnObject= null;
  		ArrayList<HashMap<String, String>>  rtList	= new ArrayList<HashMap<String, String>>();
  		HashMap<String, String>			  	rst	 	= null;
  		 
  		ConnectionContext connectionContext = new ConnectionResource();

  		try {
  	
  			rtList.clear();
  			
  			String			  ConfName    = "";
  			String			  PgmSayu     = "";
  			String			  ColorSw     = "";
  	        Integer           Cnt         = 0;
  	        //String            StrAdmin    = "N";
  	        String            StrTeam     = "";
  	        //String            StrTeam2    = "";
  	        String            Sunhang	  = "";
  	        String            strOrderid    = "";
  	        String			  strOrderTitle = "";
  	        boolean           sysSw       = false;
  	        String            strSysGbn  = "";
  	        
  			conn = connectionContext.getConnection();

  			strQuery.setLength(0);
  			strQuery.append("select cm_project from cmm0040 where cm_userid = ?    \n");
  			pstmt = conn.prepareStatement(strQuery.toString());
  			pstmt = new LoggableStatement(conn,strQuery.toString());
              Cnt = 0;
  			pstmt.setString(++Cnt, userid);
          	ecamsLogger.error(((LoggableStatement)pstmt).getQueryString());
              rs = pstmt.executeQuery();
              if (rs.next()){
              	StrTeam = rs.getString("cm_project"); 
  			}
  			rs.close();
  			pstmt.close();

  			
  			
  			strQuery.setLength(0);
  			
  			if (Acptno.substring(4,6).equals("04")){
  				strQuery.append("select f.cm_username,h.cr_rsrcname as cr_rsrcname2,h.cr_story as cr_story2,b.cr_rsrcname,b.cr_story,d.cm_username as cr_refusernm,   \n");
  				strQuery.append("root_rsrcname2(a.cr_itemid) as modelnm, root_rsrcname2(a.cr_refitemid) as refmodelnm,        \n");
  				strQuery.append("a.cr_chkflag,e.cm_username as cr_chkusser,to_char(a.cr_chkdate,'yyyy/mm/dd hh24:mi') as cr_chkdate,        \n");
  				strQuery.append("a.cr_acptno,g.cm_sysmsg, a.cr_refuser,a.cr_status,a.cr_itemid,a.cr_refitemid, i.cr_editor,         		\n");
  				strQuery.append("(select cm_codename from cmm0020 where cm_macode = 'CMR0020' AND CM_MICODE = h.CR_STATUS) rsrcstat1,       \n");
  				strQuery.append("(select cm_codename from cmm0020 where cm_macode = 'CMR0020' AND CM_MICODE = b.CR_STATUS) rsrcstat2,       \n");
  				strQuery.append("d.cm_project,       \n");
  				strQuery.append("nvl((select cm_codename from cmm0020 where cm_macode = 'ASTA' and cm_micode = a.cr_chkflag), '미확인') as chkflag       \n");
  				strQuery.append("from cmr1015 a, cmr0020 b,cmm0040 d,cmm0040 e,cmm0040 f,cmm0030 g,cmr0020 h,cmr1000 i, cmr1010 j             \n");
  				strQuery.append(" WHERE  j.cr_acptno = ?	\n");
  				strQuery.append("and j.cr_acptno = i.cr_acptno 										\n");
  				strQuery.append("and j.cr_confno = a.cr_acptno                                       \n");				
  				strQuery.append("and f.cm_userid = i.cr_editor        \n");
  				strQuery.append("and b.cr_itemid = a.cr_refitemid        \n");
  				strQuery.append("and h.cr_itemid = a.cr_itemid        \n");
  				strQuery.append("and a.cr_refuser = d.cm_userid          	\n");
  				strQuery.append("and a.cr_chkusser = e.cm_userid(+)           		\n");
  				strQuery.append("and b.cr_syscd = g.cm_syscd           		\n");
  				// 20230222 쿼리 추가
  				strQuery.append("AND EXISTS (SELECT  'X'							\n");
				strQuery.append("              FROM  CMR1010						\n");
				strQuery.append("	             WHERE  CR_ACPTNO = J.CR_ACPTNO \n");
				strQuery.append("	               AND  CR_ITEMID = J.CR_ITEMID \n");
				strQuery.append("	               AND  ROWNUM = 1				\n");
				strQuery.append("	           )								\n");
  			} else { 
  				strQuery.append("select f.cm_username,h.cr_rsrcname as cr_rsrcname2,h.cr_story as cr_story2,b.cr_rsrcname,b.cr_story,d.cm_username as cr_refusernm,   \n");
  				strQuery.append("root_rsrcname2(a.cr_itemid) as modelnm, root_rsrcname2(a.cr_refitemid) as refmodelnm,        \n");
  				strQuery.append("a.cr_chkflag,e.cm_username as cr_chkusser,to_char(a.cr_chkdate,'yyyy/mm/dd hh24:mi') as cr_chkdate,        \n");
  				strQuery.append("a.cr_acptno,g.cm_sysmsg, a.cr_refuser,a.cr_status,a.cr_itemid,a.cr_refitemid, i.cr_editor,         \n");
  				strQuery.append("(select cm_codename from cmm0020 where cm_macode = 'CMR0020' AND CM_MICODE = h.CR_STATUS) rsrcstat1,       \n");
  				strQuery.append("(select cm_codename from cmm0020 where cm_macode = 'CMR0020' AND CM_MICODE = b.CR_STATUS) rsrcstat2,       \n");
  				strQuery.append("d.cm_project,       \n");
  				strQuery.append("nvl((select cm_codename from cmm0020 where cm_macode = 'ASTA' and cm_micode = a.cr_chkflag), '미확인') as chkflag       \n");
  				strQuery.append("from cmr1015 a, cmr0020 b,cmm0040 d,cmm0040 e,cmm0040 f,cmm0030 g,cmr0020 h,cmr1000 i       \n");
  				strQuery.append("where a.cr_acptno = ?        \n");
  				strQuery.append("and f.cm_userid = i.cr_editor        \n");
  				strQuery.append("and b.cr_itemid = a.cr_refitemid        \n");
  				strQuery.append("and h.cr_itemid = a.cr_itemid        \n");
  				strQuery.append("and a.cr_refuser = d.cm_userid          	\n");
  				strQuery.append("and a.cr_chkusser = e.cm_userid(+)           		\n");
  				strQuery.append("and b.cr_syscd = g.cm_syscd           		\n");
  				
  			}
  			pstmt = conn.prepareStatement(strQuery.toString());
  			pstmt = new LoggableStatement(conn,strQuery.toString());
              Cnt = 0;
  			if (Acptno.substring(4,6).equals("04")){
  				pstmt.setString(++Cnt, Acptno);
  			}else {
  				pstmt.setString(++Cnt, Acptno);			
  			}
          	ecamsLogger.error(((LoggableStatement)pstmt).getQueryString());
              rs = pstmt.executeQuery();

  			while (rs.next()){
  				ColorSw = "0";
  			    rst = new HashMap<String, String>();
  	            rst.put("ID",Integer.toString(rs.getRow()));//NO 
  	            rst.put("cr_rsrcname",    rs.getString("cr_rsrcname"));  //참조파일명
          		rst.put("cr_story",   rs.getString("cr_story"));      //참조사유
          		rst.put("cr_rsrcname2",    rs.getString("cr_rsrcname2"));  //파일명
          		rst.put("cr_story2",   rs.getString("cr_story2"));      //사유
          		rst.put("cr_refuser",  rs.getString("cr_refusernm") + "(" + rs.getString("cr_refuser") +")");    //확인대상자
//          		rst.put("cr_refuser",  rs.getString("cr_refusernm"));    //확인대상자
          		rst.put("cr_chkflag",rs.getString("chkflag"));    //확인구분
          		rst.put("cr_chkusser",  rs.getString("cr_chkusser"));    //확인자
          		rst.put("cr_chkdate",  rs.getString("cr_chkdate"));    //확인일시
          		rst.put("cr_acptno",  rs.getString("cr_acptno"));    //신청번호
          		rst.put("cm_username",  rs.getString("cm_username") + "(" + rs.getString("cr_editor") +")");    //신청자
//          		rst.put("cm_username",  rs.getString("cm_username"));    //신청자
          		rst.put("cm_sysmsg",  rs.getString("cm_sysmsg"));    //시스템명
          		rst.put("cr_itemid",  rs.getString("cr_itemid"));    //시스템명
          		rst.put("cr_refitemid",  rs.getString("cr_refitemid"));    //시스템명
          		rst.put("modelnm",  rs.getString("modelnm"));    //모델명
          		rst.put("refmodelnm",  rs.getString("refmodelnm"));    //c모델명
          		rst.put("rsrcstat1",  rs.getString("rsrcstat1"));    //프로그램상태
          		rst.put("rsrcstat2",  rs.getString("rsrcstat2"));    //참조프로그램상태
          		String tmp = rs.getString("cr_chkflag");
          		if((userid.equals(rs.getString("cr_refuser")) || 
          			userid.equals(rs.getString("cr_editor")) ||
          			StrTeam.equals(rs.getString("cm_project")))  && rs.getString("cr_status").equals("0")){
          			rst.put("enabled", "1");
          		}else{
          			rst.put("enabled", "0");
          		}
          		rst.put("selected", "0");
          	
          		rtList.add(rst);
          		rst = null;
  			}//end of while-loop statement

  			rs.close();
  			pstmt.close();
  			conn.close();
  			//ecamsLogger.error("+++++ query end +++++");
  			rs = null;
  			pstmt = null;
  			rs2 = null;
  			pstmt2 = null;
  			conn = null;

  			returnObject = rtList.toArray();
  			rtList.clear();
  			rtList = null;

  			return returnObject;

  		} catch (SQLException sqlexception) {
  			sqlexception.printStackTrace();
  			ecamsLogger.error("## Cmr3200.SelectList() SQLException START ##");
  			ecamsLogger.error("## Error DESC : ", sqlexception);
  			ecamsLogger.error("## Cmr3200.SelectList() SQLException END ##");
  			throw sqlexception;
  		} catch (Exception exception) {
  			exception.printStackTrace();
  			ecamsLogger.error("## Cmr3200.SelectList() Exception START ##");
  			ecamsLogger.error("## Error DESC : ", exception);
  			ecamsLogger.error("## Cmr3200.SelectList() Exception END ##");
  			throw exception;
  		}finally{
  			if (strQuery != null) 	strQuery = null;
  			if (returnObject != null)	returnObject = null;
  			if (rs != null)     try{rs.close();}catch (Exception ex){ex.printStackTrace();}
  			if (rs2 != null)    try{rs2.close();}catch (Exception ex){ex.printStackTrace();}
  			if (pstmt != null)  try{pstmt.close();}catch (Exception ex2){ex2.printStackTrace();}
  			if (pstmt2 != null)  try{pstmt2.close();}catch (Exception ex2){ex2.printStackTrace();}
  			if (conn != null){
  				try{
  					conn.close();
  				}catch(Exception ex3){
  					ecamsLogger.error("## Cmr3200.SelectList() connection release exception ##");
  					ex3.printStackTrace();
  				}
  			}
  		}
  	}//end of SelectList() method statement
    
    public String Cmr1015Insert(ArrayList<HashMap<String,String>> etcData,String chkflag,String userid) throws SQLException, Exception {
		Connection        conn        = null;
		PreparedStatement pstmt       = null;
		ResultSet         rs          = null;
		StringBuffer      strQuery    = new StringBuffer();
		String            returnMSG   = "N";
		String            sameDocID        = "";
		String            CONN        = "";
		ConnectionContext connectionContext = new ConnectionResource();
		
		try {
			conn = connectionContext.getConnection();
			
			for (int i=0; i < etcData.size(); i++){
				
					strQuery.setLength(0);
					strQuery.append("update cmr1015 set cr_chkdate=sysdate, cr_chkusser=?, cr_chkflag=?, cr_status = '9'     \n");
					strQuery.append("where cr_acptno = ? and cr_itemid = ? and cr_refitemid = ?                                 \n");		
					pstmt = conn.prepareStatement(strQuery.toString());
			        pstmt = new LoggableStatement(conn,strQuery.toString());
					pstmt.setString(1, userid);
					pstmt.setString(2, chkflag);
					pstmt.setString(3, etcData.get(i).get("cr_acptno"));
					pstmt.setString(4, etcData.get(i).get("cr_itemid"));
					pstmt.setString(5, etcData.get(i).get("cr_refitemid"));
					ecamsLogger.error(((LoggableStatement)pstmt).getQueryString());
					pstmt.executeUpdate();
					pstmt.close();
					
			

			}

			conn.close();
			pstmt = null;
			conn = null;
			return "OK";		


		}catch (SQLException sqlexception) {
			sqlexception.printStackTrace();
			ecamsLogger.error("## eCmr3200.Cmr1015Insert() SQLException START ##");
			ecamsLogger.error("## Error DESC : ", sqlexception);	
			ecamsLogger.error("## eCmr3200.Cmr1015Insert() SQLException END ##");			
			throw sqlexception;
		} catch (Exception exception) {
			exception.printStackTrace();
			ecamsLogger.error("## eCmr3200.Cmr1015Insert() Exception START ##");				
			ecamsLogger.error("## Error DESC : ", exception);	
			ecamsLogger.error("## eCmr3200.Cmr1015Insert() Exception END ##");				
			throw exception;
		}finally{
			if (strQuery != null) 	strQuery = null;
			if (pstmt != null)  try{pstmt.close();}catch (Exception ex2){ex2.printStackTrace();}
			if (conn != null){
				try{
					ConnectionResource.release(conn);
				}catch(Exception ex3){
					ecamsLogger.error("## eCmr3200.Cmr1015Insert() connection release exception ##");
					ex3.printStackTrace();
				}
			}
		}
	}//end of Cmr1015Insert() method statemen

}//end of Cmr3200 class statement
