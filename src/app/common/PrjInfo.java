
/*****************************************************************************************
	1. program ID	: PrjInfo.java
	2. create date	: 2008.08. 29
	3. auth		    : NO Name
	4. update date	: 
	5. auth		    : 
	6. description	: PrjInfo 프로젝트정보
*****************************************************************************************/

package app.common;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import org.apache.logging.log4j.Logger;
import org.w3c.dom.Document;

import com.ecams.common.dbconn.ConnectionContext;
import com.ecams.common.dbconn.ConnectionResource;
import com.ecams.common.logger.EcamsLogger;


/**
 * @author bigeyes
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */

public class PrjInfo{
	
    /**
     * Logger Class Instance Creation
     * logger
     */
    Logger ecamsLogger  = EcamsLogger.getLoggerInstance();
    
	/**
	 * 프로젝트정보를 조회합니다.
	 * @param  String UserId,String SysCd, String ReqCd
	 * @return Object[]
	 * @throws SQLException
	 * @throws Exception
	 */    
    public Object[] getPrjInfo(String UserId,String SelMsg,String CloseYn) throws SQLException, Exception {
		Connection        conn        = null;
		PreparedStatement pstmt       = null;
		ResultSet         rs          = null;
		PreparedStatement pstmt2      = null;
		ResultSet         rs2         = null;
		StringBuffer      strQuery    = new StringBuffer();
		String            strSelMsg   = "";
		String            svPrjNo     = "";
		boolean           findSw      = false;
		ArrayList<HashMap<String, String>> rtList = new ArrayList<HashMap<String, String>>();
		HashMap<String, String>			   rst	  = null;
		
		ConnectionContext connectionContext = new ConnectionResource();
		
		try {
			conn = connectionContext.getConnection();
			
			UserInfo userinfo = new UserInfo();
			String Sv_Pmo = userinfo.getPMOInfo_conn(UserId,conn);
			Boolean Sv_Admin = userinfo.isAdmin_conn(UserId,conn);
			userinfo = null;
			
			String PrjNo = "";
			strQuery.setLength(0);
		    strQuery.append("select cr_prjno from cmr1000 ");
	        strQuery.append("where cr_editor=? ");//UserId
	        strQuery.append("  and cr_prjno is not null ");
	        strQuery.append("order by cr_acptdate desc ");
	        pstmt = conn.prepareStatement(strQuery.toString());
	        //pstmt =  new LoggableStatement(conn, strQuery.toString());
            pstmt.setString(1, UserId);            
            //ecamsLogger.error(((LoggableStatement)pstmt).getQueryString());
            rs = pstmt.executeQuery();
			if(rs.next()) PrjNo = rs.getString("cr_prjno");
			rs.close();
			pstmt.close();			
			
			if (SelMsg != "") {
				if (SelMsg.toUpperCase().equals("ALL")){
					strSelMsg = "전체";
				}
				else if (SelMsg.toUpperCase().equals("SEL")){
					strSelMsg = "선택하세요";
				}
				else if (SelMsg.toUpperCase().equals("NEW")){
					strSelMsg = "신규";
				}
				else{
					strSelMsg = "";
				}
			}
			
			strQuery.setLength(0);
			if (Sv_Admin || Sv_Pmo.equals("1")) {
				strQuery.append("Select a.cd_prjno,a.cd_prjname,'N' cd_edityn      \n");
				strQuery.append("  from cmd0300 a                                  \n");
				strQuery.append("Where A.cd_status = '0'                           \n");
			}else{
				strQuery.append("Select a.CD_PRJNO,a.CD_PRJNAME,b.cd_edityn        \n");
				strQuery.append("  from cmd0300 a,cmd0304 b                        \n");
				strQuery.append("Where B.CD_prjuser = ? AND b.cd_closedt is null   \n");
				strQuery.append("  AND A.CD_PRJNO=B.CD_PRJNO AND A.cd_status = '0' \n");				
			}
			if (CloseYn.toUpperCase().equals("N"))
				strQuery.append("  AND A.CD_CLOSEDT is null                        \n");
			
			if (Sv_Admin || Sv_Pmo.equals("1")) {
				strQuery.append("group by a.CD_PRJNO,a.cd_prjname                  \n");	
				strQuery.append("order by a.CD_PRJNO                               \n");	
			}
	        else {
	        	strQuery.append("group by a.CD_PRJNO,a.CD_PRJNAME,b.cd_edityn       \n");
	        	strQuery.append("order by a.CD_PRJNO,a.CD_PRJNAME,b.cd_edityn desc  \n");	 	 
	        }
			
	        pstmt = conn.prepareStatement(strQuery.toString());
	        pstmt =  new LoggableStatement(conn, strQuery.toString());
	        if (Sv_Admin || Sv_Pmo.equals("1")){
	        }else pstmt.setString(1, UserId);
	        ecamsLogger.error(((LoggableStatement)pstmt).getQueryString());
            rs = pstmt.executeQuery();
            
            rtList.clear();
            
			if (strSelMsg.equals("신규") && strSelMsg.length() > 0 && strSelMsg != "" && !strSelMsg.equals("")) {
				rst = new HashMap<String,String>();
				rst.put("ID", "0");
				rst.put("cd_prjno", "00000");
				rst.put("cd_prjname", "");
				rst.put("prjnoname", strSelMsg);					
				rtList.add(rst);
				rst = null;
			}
			
			while (rs.next()){
				if (rs.getRow()==1 && !strSelMsg.equals("신규") && strSelMsg.length() > 0 && strSelMsg != "" && !strSelMsg.equals("")) {
					rst = new HashMap<String,String>();
					rst.put("ID", "0");
					rst.put("cd_prjno", "00000");
					rst.put("cd_prjname", "");
					rst.put("prjnoname", strSelMsg);					
					rtList.add(rst);
					rst = null;
				}
				
				findSw = false;
				if (svPrjNo != null && svPrjNo != "") {
					if (svPrjNo.equals("cd_prjNo")) {
						findSw = true;
						break;
					}
				}
				if (findSw == false) {
					svPrjNo = rs.getString("cd_prjno");
					
					rst = new HashMap<String,String>();
					rst.put("ID", Integer.toString(rs.getRow()));
					rst.put("cd_prjno",rs.getString("cd_prjno"));
					rst.put("cd_prjname",rs.getString("cd_prjname"));
					rst.put("prjnoname",rs.getString("cd_prjno") + "   " + 
							rs.getString("cd_prjname"));
					
					if (rs.getString("cd_prjno").equals(PrjNo)) rst.put("lastPrj", "Y");
					else rst.put("lastPrj", "N");
					
					//if (Sv_Admin || Sv_Pmo.equals("1")) {
					strQuery.setLength(0);
					strQuery.append("select count(*) as cnt from cmd0304          \n");
					strQuery.append(" where cd_prjno=? and cd_edityn='Y'          \n");
					strQuery.append("   and cd_prjuser=? and cd_closedt is null   \n");
					pstmt2 = conn.prepareStatement(strQuery.toString());
					pstmt2.setString(1, rs.getString("cd_prjno"));
					pstmt2.setString(2, UserId);
					rs2 = pstmt2.executeQuery();
					if(rs2.next()) {
					   if (rs2.getInt("cnt") > 0) rst.put("cd_edityn", "Y"); 	
					   else rst.put("cd_edityn", "N");
					}
					rs2.close();
					pstmt2.close();
					//} else rst.put("cd_edityn", rs.getString("cd_edityn"));
					
					strQuery.setLength(0);
					strQuery.append("Select count(cr_docid) as cnt from cmr0030 where cr_prjno=? ");//prjno
					pstmt2 = conn.prepareStatement(strQuery.toString());
					pstmt2.setString(1, rs.getString("cd_prjno"));
					rs2 = pstmt2.executeQuery();
					if(rs2.next()) {
						if (rs2.getString("cnt").equals("0")) rst.put("docFileYN", "N");
						else rst.put("docFileYN", "Y");
					}
					pstmt2.close();
					rs2.close();
					rtList.add(rst);
					rst = null;
				}
				
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
			ecamsLogger.error("## PrjInfo.getPrjInfo() SQLException START ##");
			ecamsLogger.error("## Error DESC : ", sqlexception);	
			ecamsLogger.error("## PrjInfo.getPrjInfo() SQLException END ##");			
			throw sqlexception;
		} catch (Exception exception) {
			exception.printStackTrace();
			ecamsLogger.error("## PrjInfo.getPrjInfo() Exception START ##");				
			ecamsLogger.error("## Error DESC : ", exception);	
			ecamsLogger.error("## PrjInfo.getPrjInfo() Exception END ##");				
			throw exception;
		}finally{
			if (strQuery != null) 	strQuery = null;
			if (rtList != null)		rtList = null;
			if (rs != null)     try{rs.close();}catch (Exception ex){ex.printStackTrace();}
			if (rs2 != null)     try{rs2.close();}catch (Exception ex1){ex1.printStackTrace();}
			if (pstmt != null)  try{pstmt.close();}catch (Exception ex2){ex2.printStackTrace();}
			if (pstmt2 != null)  try{pstmt2.close();}catch (Exception ex3){ex3.printStackTrace();}
			if (conn != null){
				try{
					ConnectionResource.release(conn);
				}catch(Exception ex3){
					ecamsLogger.error("## PrjInfo.getPrjInfo() connection release exception ##");
					ex3.printStackTrace();
				}
			}
		}
	}//end of getPrjInfo() method statement
	
	
	/**	 
	 *  프로젝트리스트 조회
	 *  etcData 검색조건 Data
	   (단, 필요한 부분만 셋팅 하면 됨)
	   1. secuyn
	      Y  : 본인에게 권한있는 것만
	      N  : 모두
	   2. qrygbn
	      00 : 전체
	      01 : 현재화면에 대한 등록대상만			      
	   3. reqcd (화면구분)
	      41 : 개발계획서
	   4. stday,edday : 조회기간
	      단,secuyn='N'이거나 qrygbn='00'일 경우만 사용
	   5. reqdept : 등록부서
	   6. recvdept : 접수부서
	   7. requser : 요청자 (like)
	   8. rettit : 요청제목 (like)
	 * @param  HashMap<String,String> etcData
	 * @return Object[]
	 * @throws SQLException
	 * @throws Exception
	 */
	public Object[] getPrjList_Recv(HashMap<String,String> etcData) throws SQLException, Exception {
		Connection        conn        = null;
		PreparedStatement pstmt       = null;
		ResultSet         rs          = null;
		PreparedStatement pstmt2      = null;
		ResultSet         rs2         = null;
		StringBuffer      strQuery    = new StringBuffer();
		ArrayList<HashMap<String, String>> rtList = new ArrayList<HashMap<String, String>>();
		HashMap<String, String>			   rst	  = null;
		
		ConnectionContext connectionContext = new ConnectionResource();
		
		try {
			
			conn = connectionContext.getConnection();
			int cnt = 0;
			String strSCMUSER = "";
			strQuery.setLength(0);
			if (!etcData.get("reqcd").equals("41")){
				conn.close();
				conn = null;
				
				throw new Exception("getPrjList_Recv() : RFC접수 일때 사용 가능합니다. 관리자에게 문의해주세요.");
			}
			if (etcData.get("reqcd").equals("31") || etcData.get("reqcd").equals("39")) {
				strQuery.append("select distinct a.cc_isrid,b.cc_isrtitle \n");
				strQuery.append("  from cmm0040 c,cmc0100 b,cmc0110 a \n");
			} else {
				strQuery.append("select a.CC_ISRID || '-' || a.CC_ISRSUB ISRID,a.CC_ISRID,a.CC_ISRSUB, \n");
				strQuery.append("       b.CC_ISRTITLE,b.CC_TESTERYN,b.CC_DOCNO,b.CC_REQENDDT, \n");
				strQuery.append("       c.cm_username REQUSER,f.cm_deptname REQDEPT, \n");
				strQuery.append("       g.cm_codename REQSTA1,                       \n");
				strQuery.append("       decode(a.cc_substatus,'23',decode(TESTREQ_TOTAL(a.cc_isrid,a.cc_isrsub),'OK','단위테스트종료',h.cm_codename),h.cm_codename) REQSTA2,\n");
				strQuery.append("       a.CC_RECVPART,i.cm_deptname RECVPART,        \n");
				strQuery.append("       a.CC_DETCATE,k.cm_codename DETCATE,          \n");
				strQuery.append("       a.CC_HANDLER,L.CM_USERNAME CONTUSER,         \n");
				strQuery.append("       to_char(b.cc_creatdt,'yyyy/mm/dd') REQDAY,   \n");
				strQuery.append("       to_char(b.cc_creatdt,'yyyy/mm/dd hh24:mi') REQDATE,\n");
				strQuery.append("       a.CC_MAINSTATUS,a.CC_SUBSTATUS,a.CC_CHGUSER,j.CM_USERNAME CHGUSER,a.CC_CHGTYPE,a.CC_RECVMSG \n");
				strQuery.append("  from cmc0110 a,cmc0100 b,cmm0040 c,cmm0100 f,cmm0020 g, \n");
				strQuery.append("       cmm0020 h,cmm0100 i,cmm0040 j,cmm0020 k,cmm0040 L  \n");
				if (etcData.get("reqcd").equals("41") && etcData.get("qrygbn").equals("01")) {
					strQuery.append("       ,cmm0040 d,cmm0043 e \n");
				}
			}
			strQuery.append(" where a.CC_ISRID=b.CC_ISRID \n");
			strQuery.append("   and b.CC_EDITOR=c.CM_USERID \n");
			strQuery.append("   and b.CC_REQDEPT=f.CM_DEPTCD \n");
			strQuery.append("   and g.CM_MACODE='ISRSTAMAIN' \n");
			strQuery.append("   and a.CC_MAINSTATUS=g.CM_MICODE \n");
			strQuery.append("   and h.CM_MACODE='ISRSTASUB' \n");
			strQuery.append("   and a.CC_SUBSTATUS=h.CM_MICODE \n");
			strQuery.append("   and a.CC_RECVPART=i.CM_DEPTCD \n");
			strQuery.append("   and k.CM_MACODE='DETCATE' \n");
			strQuery.append("   and a.CC_DETCATE=k.CM_MICODE \n");
			strQuery.append("   and a.CC_HANDLER=L.CM_USERID \n");
			strQuery.append("   and a.CC_CHGUSER=j.CM_USERID(+) \n");
			if (etcData.get("reqdept") != null && etcData.get("reqdept") != "") {
				strQuery.append("and b.cc_reqdept=?                          \n");
			}
			if (etcData.get("recvdept") != null && etcData.get("recvdept") != "") {
				strQuery.append("and a.cc_recvpart=?                          \n");
			}
			if (etcData.get("requser") != null && etcData.get("requser") != "") {
				strQuery.append("and c.cm_username like ?                    \n");
			}
			if (etcData.get("reqtit") != null && etcData.get("reqtit") != "") {
				strQuery.append("and b.cc_isrtitle like ?                    \n");
			}
			if (etcData.get("secuyn").equals("Y")) {
				if (etcData.get("reqcd").equals("31") || etcData.get("reqcd").equals("39")) {
					//ISR접수 및 ISR종료
					if (etcData.get("reqcd").equals("31")) {
						if (etcData.get("qrygbn").equals("01")) {
							strQuery.append("and b.cc_editor=?                       \n");
							strQuery.append("and a.cc_substatus='11'         \n");
						} else {
							strQuery.append("and to_char(b.cc_creatdt,'yyyymmdd')>=? \n");
							strQuery.append("and to_char(b.cc_creatdt,'yyyymmdd')<=? \n");
						}
					} else  {
						if (etcData.get("qrygbn") == null && etcData.get("qrygbn") == "") {	
							strQuery.append("and to_char(b.cc_creatdt,'yyyymmdd')>=? \n");
							strQuery.append("and to_char(b.cc_creatdt,'yyyymmdd')<=? \n");
						} else if (etcData.get("qrygbn").equals("99")) {						
							strQuery.append("and a.cc_substatus in ('11','19') \n");
						} else {
							strQuery.append("and a.cc_substatus=?            \n");
						}
					}
				} else if (etcData.get("reqcd").equals("32") || etcData.get("reqcd").equals("38")) {
					//ISR접수 및 ISR종료
					if (etcData.get("reqcd").equals("32")) {
						if (etcData.get("qrygbn").equals("01")) {
							strQuery.append("and a.cc_recvuser=?                     \n");
							strQuery.append("and a.cc_substatus='11'         \n");
						} else {
							strQuery.append("and to_char(b.cc_creatdt,'yyyymmdd')>=? \n");
							strQuery.append("and to_char(b.cc_creatdt,'yyyymmdd')<=? \n");
						}
					} else  {
						if (etcData.get("qrygbn") == null && etcData.get("qrygbn") == "") {	
							strQuery.append("and to_char(b.cc_creatdt,'yyyymmdd')>=? \n");
							strQuery.append("and to_char(b.cc_creatdt,'yyyymmdd')<=? \n");
						} else if (etcData.get("qrygbn").equals("99")) {						
							strQuery.append("and a.cc_substatus in ('12','13','22','29') \n");
						} else {
							strQuery.append("and a.cc_substatus=?            \n");
						}
					}
				} else if (etcData.get("reqcd").equals("33")) {
					//RFC 발생요청
					if (etcData.get("qrygbn").equals("00")) {
						strQuery.append("and to_char(b.cc_creatdt,'yyyymmdd')>=? \n");
						strQuery.append("and to_char(b.cc_creatdt,'yyyymmdd')<=? \n");
					} else {
						strQuery.append("and a.cc_handler=?                      \n");
						strQuery.append("and a.cc_substatus='15'         \n");
					} 
				} else if (etcData.get("reqcd").equals("41")) {
					//RFC 접수
					if (etcData.get("qrygbn").equals("01")) {
						//RFC 본인 접수 대상만 조회
						strQuery.append("  and a.CC_SUBSTATUS = '15' \n");
						strQuery.append("  and d.CM_USERID = ? \n");
						strQuery.append("  and d.CM_USERID = e.CM_USERID \n");
						strQuery.append("  and e.CM_RGTCD = 'C1' \n");
						strQuery.append("  and a.CC_RECVPART = d.CM_PROJECT \n");
					} else if (etcData.get("qrygbn").equals("02")) {
						//RFC 파트 접수 대상만 조회
						strQuery.append("and a.cc_substatus='15'         \n");
					}
				}
			}else{
				//secuyn==N 또는 전체일때 일정
				strQuery.append("   and to_char(b.cc_creatdt,'yyyymmdd')>=? \n");
				strQuery.append("   and to_char(b.cc_creatdt,'yyyymmdd')<=? \n");
			}
			strQuery.append(" order by b.cc_creatdt desc,a.cc_isrid,a.cc_isrsub \n");
			
	        pstmt = conn.prepareStatement(strQuery.toString());
	        //pstmt =  new LoggableStatement(conn, strQuery.toString());
	        cnt = 0;
	        if (etcData.get("reqdept") != null && etcData.get("reqdept") != "") {
				pstmt.setString(++cnt, etcData.get("reqdept"));
			}
			if (etcData.get("recvdept") != null && etcData.get("recvdept") != "") {
				pstmt.setString(++cnt, etcData.get("recvdept"));
			}
			if (etcData.get("requser") != null && etcData.get("requser") != "") {
				pstmt.setString(++cnt, "%"+etcData.get("requser")+"%");
			}
			if (etcData.get("reqtit") != null && etcData.get("reqtit") != "") {
				pstmt.setString(++cnt, "%"+etcData.get("reqtit")+"%");
			}
			if (etcData.get("secuyn").equals("Y")) {
				if (etcData.get("reqcd").equals("31") || etcData.get("reqcd").equals("39")) {
					if (etcData.get("reqcd").equals("31")) {
						if (!etcData.get("qrygbn").equals("01")) {
							pstmt.setString(++cnt, etcData.get("stday"));					
							pstmt.setString(++cnt, etcData.get("edday"));
						}else{
							pstmt.setString(++cnt, etcData.get("userid"));
						}
					} else  {
						if (etcData.get("qrygbn") == null && etcData.get("qrygbn") == "") {	
							pstmt.setString(++cnt, etcData.get("stday"));					
							pstmt.setString(++cnt, etcData.get("edday"));
						} else if (!etcData.get("qrygbn").equals("99")) {
							pstmt.setString(++cnt, etcData.get("qrygbn"));	
						}
					}
				} else if (etcData.get("reqcd").equals("32") || etcData.get("reqcd").equals("38")) {
					if (etcData.get("reqcd").equals("32")) {
						if (!etcData.get("qrygbn").equals("01")) {	
							pstmt.setString(++cnt, etcData.get("stday"));					
							pstmt.setString(++cnt, etcData.get("edday"));
						}else{
							pstmt.setString(++cnt, etcData.get("userid"));
						}
					} else  {
						if (etcData.get("qrygbn") == "00" && etcData.get("qrygbn") == "00") {	
							pstmt.setString(++cnt, etcData.get("stday"));					
							pstmt.setString(++cnt, etcData.get("edday"));
						} else if (!etcData.get("qrygbn").equals("99")) {
							pstmt.setString(++cnt, etcData.get("qrygbn"));
						}
					}
				} else if (etcData.get("reqcd").equals("33")) {
					if (etcData.get("qrygbn").equals("00")) {	
						pstmt.setString(++cnt, etcData.get("stday"));					
						pstmt.setString(++cnt, etcData.get("edday"));
					}else{
						pstmt.setString(++cnt, etcData.get("userid"));
					}
				} else if (etcData.get("reqcd").equals("41")) {
					if (etcData.get("qrygbn").equals("01")) {
						//RFC 본인 접수 대상만 조회
						pstmt.setString(++cnt, etcData.get("userid"));
					}
				}
			} else {
				pstmt.setString(++cnt, etcData.get("stday"));					
				pstmt.setString(++cnt, etcData.get("edday"));
			}
            //ecamsLogger.error(((LoggableStatement)pstmt).getQueryString());
            rs = pstmt.executeQuery();
            
			while(rs.next()){
				rst = new HashMap<String, String>();
				rst.put("ISRID", rs.getString("ISRID"));              //ISR-ID+SUBNO : cmc0100
				rst.put("CC_ISRID", rs.getString("CC_ISRID"));        //ISR-ID : cmc0100
				rst.put("CC_ISRSUB", rs.getString("CC_ISRSUB"));      //ISR SUB NO : cmc0100
				rst.put("CC_ISRTITLE", rs.getString("CC_ISRTITLE"));     //요청제목 : cmc0100
				rst.put("REQDEPT", rs.getString("REQDEPT"));          //등록부서 : cmc0100
				rst.put("REQUSER", rs.getString("REQUSER"));          //등록인 : cmc0100
				rst.put("REQDAY", rs.getString("REQDAY"));            //등록일 : cmc0100
				rst.put("REQDATE", rs.getString("REQDATE"));          //등록일시 : cmc0100
				rst.put("CC_TESTERYN", rs.getString("CC_TESTERYN"));  //테스트참여여부 : cmc0100
				rst.put("CC_DOCNO", rs.getString("CC_DOCNO"));        //문서번호 : cmc0100
				rst.put("CC_REQENDDT", rs.getString("CC_REQENDDT").substring(0,4)+"/"+
						               rs.getString("CC_REQENDDT").substring(4,6)+"/"+
						               rs.getString("CC_REQENDDT").substring(6));   //완료예정일 : cmc0100
				rst.put("STATUS", "["+rs.getString("REQSTA1")+"]"+rs.getString("REQSTA2"));   //상태 : cmc0110
				rst.put("CC_RECVPART", rs.getString("CC_RECVPART"));
				rst.put("RECVPART", rs.getString("RECVPART"));       //접수파트 : cmc0110
				rst.put("CC_CHGUSER", rs.getString("CC_CHGUSER"));   //변경관리관리자 : cmc0110
				rst.put("CHGUSER", rs.getString("CHGUSER"));         //변경관리관리자 : cmc0110
				rst.put("CC_DETCATE", rs.getString("CC_DETCATE"));   //상세분류코드 : cmc0110
				rst.put("DETCATE", rs.getString("DETCATE"));         //상세분류명 : cmc0110
				rst.put("REQSTA1", rs.getString("REQSTA1"));         //진행구분 : cmc0110
				rst.put("REQSTA2", rs.getString("REQSTA2"));         //상세진행상황 : cmc0110
				rst.put("CC_HANDLER", rs.getString("CC_HANDLER"));
				rst.put("CONTUSER", rs.getString("CONTUSER"));
				rst.put("CC_CHGTYPE", rs.getString("CC_CHGTYPE"));
				rst.put("CC_MAINSTATUS", rs.getString("CC_MAINSTATUS"));
				rst.put("CC_SUBSTATUS", rs.getString("CC_SUBSTATUS"));
				rst.put("CC_RECVMSG", rs.getString("CC_RECVMSG"));
				
				strSCMUSER = "";
				if(Integer.parseInt(rs.getString("CC_SUBSTATUS")) > 15){
	    	        strQuery.setLength(0);
	            	strQuery.append("select a.CC_SCMUSER,b.CM_USERNAME \n");
	            	strQuery.append("  from cmc0210 a, cmm0040 b \n");
	            	strQuery.append(" where a.CC_ISRID = ? \n");
	            	strQuery.append("   and a.CC_ISRSUB = ? \n");
	            	strQuery.append("   and a.CC_SCMUSER=b.CM_USERID \n");
	            	
	            	pstmt2 = conn.prepareStatement(strQuery.toString());
	            	cnt = 0;
		            pstmt2.setString(++cnt, rs.getString("CC_ISRID"));
		            pstmt2.setString(++cnt, rs.getString("CC_ISRSUB"));
		            rs2 = pstmt2.executeQuery();
		            
		            while (rs2.next()){
		            	if (strSCMUSER.length() == 0){
		            		strSCMUSER = rs2.getString("CC_SCMUSER")+"."+rs2.getString("CM_USERNAME");
		            	}else{
		            		strSCMUSER = strSCMUSER + "," + rs2.getString("CC_SCMUSER")+"."+rs2.getString("CM_USERNAME");
		            	}
		            }
		            pstmt2.close();
		            rs2.close();
		            rs2 = null;
		            pstmt2 = null;
				}
				rst.put("SCMUSER", strSCMUSER);
				rtList.add(rst);
				rst = null;
			}
			rs.close();
			pstmt.close();
			conn.close();
			
			rs = null;
			pstmt = null;
			conn = null;
			
			return rtList.toArray();
			
			
		} catch (SQLException sqlexception) {
			sqlexception.printStackTrace();
			ecamsLogger.error("## PrjInfo.getPrjList_Recv() SQLException START ##");
			ecamsLogger.error("## Error DESC : ", sqlexception);	
			ecamsLogger.error("## PrjInfo.getPrjList_Recv() SQLException END ##");			
			throw sqlexception;
		} catch (Exception exception) {
			exception.printStackTrace();
			ecamsLogger.error("## PrjInfo.getPrjList_Recv() Exception START ##");				
			ecamsLogger.error("## Error DESC : ", exception);	
			ecamsLogger.error("## PrjInfo.v() Exception END ##");				
			throw exception;
		}finally{
			if (strQuery != null) 	strQuery = null;
			if (rtList != null)		rtList = null;
			if (rs != null)     try{rs.close();}catch (Exception ex){ex.printStackTrace();}
			if (pstmt != null)  try{pstmt.close();}catch (Exception ex2){ex2.printStackTrace();}
			if (rs2 != null)     try{rs2.close();}catch (Exception ex){ex.printStackTrace();}
			if (pstmt2 != null)  try{pstmt2.close();}catch (Exception ex2){ex2.printStackTrace();}
			if (conn != null){
				try{
					ConnectionResource.release(conn);
				}catch(Exception ex3){
					ecamsLogger.error("## PrjInfo.getPrjList_Recv() connection release exception ##");
					ex3.printStackTrace();
				}
			}
		}
	}//end of getPrjList_Recv() method statement

	/**
	 *  프로젝트리스트 조회
	 *  etcData 검색조건 Data
	   (단, 필요한 부분만 셋팅 하면 됨)
	   1. secuyn
	      Y  : 본인에게 권한있는 것만,N:모두
	   2. qrygbn
	      00 : 전체
	      01 : 현재화면에 대한 등록대상만			      
	   3. reqcd (화면구분)
	      03 : 테스트적용 요청
	      04 : 운영적용 요청
	      42 : 개발계획서   
	      43 : 단위테스트작성
	      44 : 통합테스트작성
	      49 : 변경관리종료
	   4. stday,edday : 조회기간
	               단,secuyn='N'이거나 qrygbn='00'일 경우만 사용
	   5. reqdept : 등록부서
	   6. recvdept : 접수부서
	   7. requser : 요청자 (like)
	   8. rettit : 요청제목 (like)
	 * @param  HashMap<String,String> etcData
	 * @return Object[]
	 * @throws SQLException
	 * @throws Exception
	 */
	public Object[] getPrjList_Isr(HashMap<String,String> etcData) throws SQLException, Exception {
		Connection        conn        = null;
		PreparedStatement pstmt       = null;
		ResultSet         rs          = null;
		StringBuffer      strQuery    = new StringBuffer();
		ArrayList<HashMap<String, String>> rtList = new ArrayList<HashMap<String, String>>();
		HashMap<String, String>			   rst	  = null;
		
		ConnectionContext connectionContext = new ConnectionResource();
		
		try {

			conn = connectionContext.getConnection();
			int cnt = 0;
			String svIsrId = "";
			String svScmUser = "";
			boolean findSw = false;
			
			strQuery.setLength(0);
			strQuery.append("select a.cc_isrid || '-' || a.cc_isrsub isrid,a.cc_isrid,a.cc_isrsub subid, \n");
			strQuery.append("       b.cc_isrtitle,b.cc_testeryn,nvl(b.cc_docno,'') cc_docno,b.cc_reqenddt, \n");
			strQuery.append("       c.cm_username requser,f.cm_deptname reqdept,              \n");
			strQuery.append("       g.cm_codename reqsta1,                                    \n");
			strQuery.append("       decode(a.cc_substatus,'23',decode(TESTREQ_TOTAL(a.cc_isrid,a.cc_isrsub),'OK','단위테스트종료',h.cm_codename),h.cm_codename) reqsta2,\n");
			strQuery.append("       i.cm_deptname recvdept,a.cc_detcate,a.CC_HANDLER,         \n");
			strQuery.append("       to_char(b.cc_creatdt,'yyyy/mm/dd') reqday,                \n");
			strQuery.append("       to_char(b.cc_creatdt,'yyyy/mm/dd hh24:mi') reqdate,       \n");
			strQuery.append("       a.cc_mainstatus,a.cc_substatus,                           \n");
			strQuery.append("       ISRSTA_MAIN(a.cc_isrid,a.cc_isrsub,?) maintab             \n"); 
			//strQuery.append("       ISRSTA_SUB(a.cc_isrid,a.cc_isrsub,'REQ',?) subtab         \n");  
			strQuery.append("  from cmm0100 i,cmm0020 h,cmm0020 g,                            \n");			
			strQuery.append("       cmm0100 f,cmm0040 c,cmc0100 b,cmc0110 a                   \n");			
			if (etcData.get("secuyn").equals("Y")) {
				strQuery.append("where SYSISR_SECUCHK(?,a.cc_isrid,a.cc_isrsub,?)='OK'        \n");
				if (etcData.get("qrygbn").equals("00")) {
					strQuery.append("and to_char(b.cc_creatdt,'yyyymmdd')>=? \n");
					strQuery.append("and to_char(b.cc_creatdt,'yyyymmdd')<=? \n");
				} else if (etcData.get("qrygbn").equals("01")){
					if (etcData.get("reqcd").equals("32")) {        //ISR접수
						strQuery.append("and a.cc_substatus='11'             \n");
					} else if (etcData.get("reqcd").equals("33")) { //RFC발행
						strQuery.append("and a.cc_substatus in ('12','16','22') \n");
					} else if (etcData.get("reqcd").equals("39")) { //ISR종료
						strQuery.append("and a.cc_substatus in ('16','22','29','1A') \n");
					} else if (etcData.get("reqcd").equals("38")) { //요청자종료
						strQuery.append("and a.cc_substatus in ('19')        \n");
					} 
				} else {
					if (etcData.get("reqcd").equals("33") && etcData.get("qrygbn").equals("02")) {
						strQuery.append("and a.cc_substatus in ('14','16')   \n");
					} else if (etcData.get("reqcd").equals("32") && etcData.get("qrygbn").equals("02")) {
					   strQuery.append("  and to_char(b.cc_creatdt,'yyyymmdd')>=? \n");
					   strQuery.append("  and to_char(b.cc_creatdt,'yyyymmdd')<=? \n");
					} 
				}
			} else {
				strQuery.append("where to_char(b.cc_creatdt,'yyyymmdd')>=? \n");
				strQuery.append("  and to_char(b.cc_creatdt,'yyyymmdd')<=? \n");
			}
			if (etcData.get("reqdept") != null && etcData.get("reqdept") != "") {
				strQuery.append("and b.cc_reqdept=?                        \n");
			}
			if (etcData.get("recvdept") != null && etcData.get("recvdept") != "") {
				strQuery.append("and a.cc_recvpart=?                       \n");
			}
			if (etcData.get("requser") != null && etcData.get("requser") != "") {
				strQuery.append("and c.cm_username like ?                  \n");
			}
			if (etcData.get("reqtit") != null && etcData.get("reqtit") != "") {
				strQuery.append("and b.cc_isrtitle like ?                  \n");
			}
			strQuery.append("  and a.cc_isrid=b.cc_isrid                 \n");
			strQuery.append("  and b.cc_editor=c.cm_userid               \n");
			strQuery.append("  and b.cc_reqdept=f.cm_deptcd              \n");
			strQuery.append("  and g.cm_macode='ISRSTAMAIN'              \n");
			strQuery.append("  and a.cc_mainstatus=g.cm_micode           \n");
			strQuery.append("  and h.cm_macode='ISRSTASUB'               \n");
			strQuery.append("  and a.cc_substatus=h.cm_micode            \n");
			strQuery.append("  and a.cc_recvpart=i.cm_deptcd             \n");
			strQuery.append("order by b.cc_creatdt desc,a.cc_isrid,a.cc_isrsub \n");

			pstmt = conn.prepareStatement(strQuery.toString());
	        //pstmt =  new LoggableStatement(conn, strQuery.toString());
	        //pstmt.setString(++cnt, etcData.get("userid"));	
	        pstmt.setString(++cnt, etcData.get("userid"));	
	        if (etcData.get("secuyn").equals("Y")) {
				pstmt.setString(++cnt, etcData.get("userid"));	
				pstmt.setString(++cnt, etcData.get("reqcd"));				
				if (etcData.get("qrygbn").equals("00")) {
					pstmt.setString(++cnt, etcData.get("stday"));					
					pstmt.setString(++cnt, etcData.get("edday"));
				} else if (etcData.get("reqcd").equals("32") && etcData.get("qrygbn").equals("02")) {
					pstmt.setString(++cnt, etcData.get("stday"));					
					pstmt.setString(++cnt, etcData.get("edday"));
				} 
			} else {
				pstmt.setString(++cnt, etcData.get("stday"));					
				pstmt.setString(++cnt, etcData.get("edday"));
			}
	        if (etcData.get("reqdept") != null && etcData.get("reqdept") != "") {
				pstmt.setString(++cnt, etcData.get("reqdept"));
			}
			if (etcData.get("recvdept") != null && etcData.get("recvdept") != "") {
				pstmt.setString(++cnt, etcData.get("recvdept"));
			}
			if (etcData.get("requser") != null && etcData.get("requser") != "") {
				pstmt.setString(++cnt, "%"+etcData.get("requser")+"%");
			}
			if (etcData.get("reqtit") != null && etcData.get("reqtit") != "") {
				pstmt.setString(++cnt, "%"+etcData.get("reqtit")+"%");
			}
			
            //ecamsLogger.error(((LoggableStatement)pstmt).getQueryString());
            rs = pstmt.executeQuery();
            
			while(rs.next()){
				rst = new HashMap<String, String>();
				rst.put("isrid", rs.getString("isrid"));              //ISR-ID+SUBNO : cmc0100
				rst.put("isridtitle", "["+rs.getString("isrid")+"] "+rs.getString("cc_isrtitle"));//ISR-ID+SUBNO+TITLE
				rst.put("cc_isrid", rs.getString("cc_isrid"));        //ISR-ID : cmc0100
				rst.put("cc_isrsub", rs.getString("subid"));      //ISR SUB NO : cmc0100
				rst.put("isrtitle", rs.getString("cc_isrtitle"));     //요청제목 : cmc0100
				rst.put("reqdept", rs.getString("reqdept"));          //등록부서 : cmc0100
				rst.put("requser", rs.getString("requser"));          //등록인 : cmc0100
				rst.put("reqday", rs.getString("reqday"));            //등록일 : cmc0100
				rst.put("reqdate", rs.getString("reqdate"));          //등록일시 : cmc0100
				rst.put("cc_testeryn", rs.getString("cc_testeryn"));  //테스트참여여부 : cmc0100
				rst.put("cc_docno", rs.getString("cc_docno"));        //문서번호 : cmc0100
				rst.put("reqenddt", rs.getString("cc_reqenddt").substring(0,4)+"/"+
						               rs.getString("cc_reqenddt").substring(4,6)+"/"+
						               rs.getString("cc_reqenddt").substring(6));   //완료예정일 : cmc0100
				rst.put("status", "["+rs.getString("reqsta1")+"]"+rs.getString("reqsta2"));   //상태 : cmc0110
				rst.put("recvdept", rs.getString("recvdept"));       //접수파트 : cmc0110
				rst.put("cc_detcate", rs.getString("cc_detcate"));   //상세분류코드 : cmc0110
				rst.put("reqsta1", rs.getString("reqsta1"));         //진행구분 : cmc0110
				rst.put("reqsta2", rs.getString("reqsta2"));         //상세진행상황 : cmc0110
				rst.put("cc_substatus", rs.getString("cc_substatus"));         //상세진행상황 : cmc0110
				rst.put("CC_HANDLER", rs.getString("CC_HANDLER"));
				rst.put("cc_mainstatus", rs.getString("cc_mainstatus"));
				rst.put("maintab", rs.getString("maintab"));
				//rst.put("subtab", rs.getString("subtab"));
				rtList.add(rst);
			}
			rs.close();
			pstmt.close();
			
			conn.close();
			
			rs = null;
			pstmt = null;
			conn = null;
			
			return rtList.toArray();
			
			
		} catch (SQLException sqlexception) {
			sqlexception.printStackTrace();
			ecamsLogger.error("## PrjInfo.getPrjList_Isr() SQLException START ##");
			ecamsLogger.error("## Error DESC : ", sqlexception);	
			ecamsLogger.error("## PrjInfo.getPrjList_Isr() SQLException END ##");			
			throw sqlexception;
		} catch (Exception exception) {
			exception.printStackTrace();
			ecamsLogger.error("## PrjInfo.getPrjList_Isr() Exception START ##");				
			ecamsLogger.error("## Error DESC : ", exception);	
			ecamsLogger.error("## PrjInfo.getPrjList_Isr() Exception END ##");				
			throw exception;
		}finally{
			if (strQuery != null) 	strQuery = null;
			if (rtList != null)		rtList = null;
			if (rs != null)     try{rs.close();}catch (Exception ex){ex.printStackTrace();}
			if (pstmt != null)  try{pstmt.close();}catch (Exception ex2){ex2.printStackTrace();}
			if (conn != null){
				try{
					ConnectionResource.release(conn);
				}catch(Exception ex3){
					ecamsLogger.error("## PrjInfo.getPrjList_Isr() connection release exception ##");
					ex3.printStackTrace();
				}
			}
		}
	}//end of getPrjList_Isr() method statement
	
	/**
	 *  프로젝트리스트 조회
	 *  etcData 검색조건 Data
	   (단, 필요한 부분만 셋팅 하면 됨)
	   1. secuyn
	      Y  : 본인에게 권한있는 것만,N:모두
	   2. qrygbn
	      00 : 전체
	      01 : 현재화면에 대한 등록대상만			      
	   3. reqcd (화면구분)
	      03 : 테스트적용 요청
	      04 : 운영적용 요청
	      42 : 개발계획서   
	      43 : 단위테스트작성
	      44 : 통합테스트작성
	      49 : 변경관리종료
	   4. stday,edday : 조회기간
	               단,secuyn='N'이거나 qrygbn='00'일 경우만 사용
	   5. reqdept : 등록부서
	   6. recvdept : 접수부서
	   7. requser : 요청자 (like)
	   8. rettit : 요청제목 (like)
	 * @param  HashMap<String,String> etcData
	 * @return Object[]
	 * @throws SQLException
	 * @throws Exception
	 */
	public Object[] getPrjList_Chg(HashMap<String,String> etcData) throws SQLException, Exception {
		Connection        conn        = null;
		PreparedStatement pstmt       = null;
		ResultSet         rs          = null;
		StringBuffer      strQuery    = new StringBuffer();
		ArrayList<HashMap<String, String>> rtList = new ArrayList<HashMap<String, String>>();
		HashMap<String, String>			   rst	  = null;
		
		ConnectionContext connectionContext = new ConnectionResource();
		
		try {

			conn = connectionContext.getConnection();
			int cnt = 0;
			boolean findSw = false;
			
			strQuery.setLength(0);
			strQuery.append("select a.cc_isrid || '-' || a.cc_isrsub isrid,a.cc_isrid,a.cc_isrsub subid, \n");
			strQuery.append("       b.cc_isrtitle,b.cc_testeryn,nvl(b.cc_docno,'') cc_docno,b.cc_reqenddt, \n");
			strQuery.append("       c.cm_username requser,f.cm_deptname reqdept,              \n");
			strQuery.append("       g.cm_codename reqsta1,                                    \n");
			strQuery.append("       decode(a.cc_substatus,'23',decode(TESTREQ_TOTAL(a.cc_isrid,a.cc_isrsub),'OK','단위테스트종료',h.cm_codename),h.cm_codename) reqsta2,\n");
			strQuery.append("       i.cm_deptname recvdept,j.cm_username chguser,             \n");
			strQuery.append("       a.cc_detcate,a.CC_HANDLER,a.cc_mainstatus,a.cc_substatus, \n");
			strQuery.append("       to_char(b.cc_creatdt,'yyyy/mm/dd') reqday,                \n");
			strQuery.append("       to_char(b.cc_creatdt,'yyyy/mm/dd hh24:mi') reqdate,       \n");
			strQuery.append("       ISRSTA_MAIN(a.cc_isrid,a.cc_isrsub,?) maintab,            \n"); 
			//strQuery.append("       ISRSTA_SUB(a.cc_isrid,a.cc_isrsub,'CHG',?) subtab,        \n"); 
			if (etcData.get("secuyn").equals("Y") && etcData.get("qrygbn").equals("01") && etcData.get("reqcd").equals("04")) {
				strQuery.append("   REALREQ(?,a.cc_isrid,a.cc_isrsub,a.CC_DETCATE,a.CC_SUBSTATUS) stasub \n");
			} else {
				strQuery.append(" '' stasub                                                   \n");
			}
			strQuery.append("  from cmm0040 j,cmm0100 i,cmm0020 h,cmm0020 g,                  \n");			
			strQuery.append("       cmm0100 f,cmm0040 c,cmc0100 b,cmc0110 a                   \n");			
			if (etcData.get("secuyn").equals("Y")) {
				strQuery.append("where SYSCHG_SECUCHK(?,a.cc_isrid,a.cc_isrsub,?)='OK'        \n");
				if (etcData.get("qrygbn").equals("00")) {
					strQuery.append("and to_char(b.cc_creatdt,'yyyymmdd')>=? \n");
					strQuery.append("and to_char(b.cc_creatdt,'yyyymmdd')<=? \n");
				} else if (etcData.get("qrygbn").equals("01")){
					if (etcData.get("reqcd").equals("03")) {
						//테스트적용 요청
						strQuery.append("and a.cc_substatus in ('23','24','25','36')     \n");
						strQuery.append("and a.cc_detcate <= '90'                        \n");
						strQuery.append("and TESTREQ(?,a.cc_isrid,a.cc_isrsub)='OK'      \n");
						strQuery.append("and TESTREQ_YN(?,a.cc_isrid,a.cc_isrsub)='OK'   \n");
						strQuery.append("and TESTREQ_TOTAL(a.cc_isrid,a.cc_isrsub)='OK'  \n");
					} else if (etcData.get("reqcd").equals("04")) {
						//운영적용 요청(체크인) 2010.11.25 수정 진행중
						strQuery.append("and a.cc_substatus in ('23','39','26','27','41') \n");
						strQuery.append("and REALREQ(?,a.cc_isrid,a.cc_isrsub,a.CC_DETCATE,a.CC_SUBSTATUS) in ('OK','NK') \n");
					} else if (etcData.get("reqcd").equals("47")) {//작업계획서만 작성
						strQuery.append("and a.cc_substatus in ('23','39','26','27','41') \n");
						strQuery.append("and REALREQ_JOBMAKE(?,a.cc_isrid,a.cc_isrsub,a.CC_DETCATE,a.CC_SUBSTATUS)='OK' \n");
					} else if (etcData.get("reqcd").equals("01") || etcData.get("reqcd").equals("02") 
							  || etcData.get("reqcd").equals("11") || etcData.get("reqcd").equals("12")) {   
						//체크아웃 요청(체크아웃_)
						if (etcData.get("reqcd").equals("12")) {
							strQuery.append("and a.cc_substatus in ('21','23','24','25','36','39')  \n");
						} else if (etcData.get("reqcd").equals("11")) {
							strQuery.append("and a.cc_substatus in ('21','23','24','25','36')  \n");
						} else {
							strQuery.append("and a.cc_substatus in ('21','23','24','36')  \n");
						}
						strQuery.append("and CHGREQ(?,a.cc_isrid,a.cc_isrsub,?)='OK'  \n");
					} else if (etcData.get("reqcd").equals("41")) { //RFC접수
						strQuery.append("and a.cc_substatus='15'           \n");
					} else if (etcData.get("reqcd").equals("42")) { //개발계획서
						strQuery.append("and a.cc_substatus in ('21','23') \n");
					} else if (etcData.get("reqcd").equals("43") ) {
						//단위테스트,통합테스트
						strQuery.append("and a.cc_substatus in ('21','23','36') \n");
					//	strQuery.append("and a.cc_detcate<='90' \n");
					} else if (etcData.get("reqcd").equals("44") ) {
						strQuery.append("and a.cc_substatus in ('21','23','36') \n");
						strQuery.append("and a.cc_detcate<='90' \n");					
					}else if (etcData.get("reqcd").equals("49")) {
						//변경관리종료
						strQuery.append("and a.cc_substatus in ('2A','49','28') \n");
						strQuery.append("and CHGENDYN(?,a.cc_isrid,a.cc_isrsub)='OK' \n");
					} else {
						strQuery.append("and a.cc_substatus in ('21','23') \n");
					}
				} else {
					if (etcData.get("qrygbn").equals("00")) {
						strQuery.append("  and to_char(b.cc_creatdt,'yyyymmdd')>=? \n");
						strQuery.append("  and to_char(b.cc_creatdt,'yyyymmdd')<=? \n");
					} else {
						if (etcData.get("reqcd").equals("41")) {
							if (etcData.get("qrygbn").equals("02")) {
								strQuery.append("and a.cc_substatus not in ('11','12','13','14','16','22','28','2A','29','18','1A','19','1B') \n");
							} else {
								strQuery.append("and a.cc_substatus not in ('11','12','13','14','15','16','22','29','18','1A','19','1B') \n");
							}
						} else if (etcData.get("reqcd").equals("42")) {
							if (etcData.get("qrygbn").equals("02")) {
								strQuery.append("and a.cc_substatus not in ('11','12','13','14','15','16','22','29','18','1A','19','1B') \n");
							}
						} else if (etcData.get("reqcd").equals("49")) {
							if (etcData.get("qrygbn").equals("02")) {
								strQuery.append("and PRJPROG_USER(a.cc_isrid,a.cc_isrsub,?)=0   \n");
								strQuery.append("and a.cc_substatus in ('23','24','25','31','32','34','35','36','39') \n");
							}
						} 
					}
				}
			} else {
				if (etcData.get("reqcd").equals("RP")) {
					strQuery.append("where a.cc_substatus in ('23','24','35')  \n");
					strQuery.append("  and PRJPROG(a.cc_isrid,a.cc_isrsub)>0   \n");
					if (etcData.get("baseisr") != null && etcData.get("baseisr") != "") {
						strQuery.append("  and a.cc_isrid=? and a.cc_isrsub=?  \n");
					}
				} else {
					strQuery.append("where to_char(b.cc_creatdt,'yyyymmdd')>=? \n");
					strQuery.append("  and to_char(b.cc_creatdt,'yyyymmdd')<=? \n");
				}
			}
			if (etcData.get("reqdept") != null && etcData.get("reqdept") != "") {
				strQuery.append("and b.cc_reqdept=?                        \n");
			}
			if (etcData.get("recvdept") != null && etcData.get("recvdept") != "") {
				strQuery.append("and a.cc_recvpart=?                       \n");
			}
			if (etcData.get("requser") != null && etcData.get("requser") != "") {
				strQuery.append("and c.cm_username like ?                  \n");
			}
			if (etcData.get("reqtit") != null && etcData.get("reqtit") != "") {
				strQuery.append("and b.cc_isrtitle like ?                  \n");
			}
			strQuery.append("  and a.cc_isrid=b.cc_isrid                 \n");
			strQuery.append("  and b.cc_editor=c.cm_userid               \n");
			strQuery.append("  and b.cc_reqdept=f.cm_deptcd              \n");
			strQuery.append("  and g.cm_macode='ISRSTAMAIN'              \n");
			strQuery.append("  and a.cc_mainstatus=g.cm_micode           \n");
			strQuery.append("  and h.cm_macode='ISRSTASUB'               \n");
			strQuery.append("  and a.cc_substatus=h.cm_micode            \n");
			strQuery.append("  and a.cc_recvpart=i.cm_deptcd             \n");
			strQuery.append("  and a.cc_chguser=j.cm_userid(+)           \n");
			strQuery.append("order by b.cc_creatdt desc,a.cc_isrid,a.cc_isrsub \n");

			pstmt = conn.prepareStatement(strQuery.toString());
	        //pstmt =  new LoggableStatement(conn, strQuery.toString());
	        //pstmt.setString(++cnt, etcData.get("userid"));
	        pstmt.setString(++cnt, etcData.get("userid"));
	        if (etcData.get("secuyn").equals("Y") && etcData.get("qrygbn").equals("01") && etcData.get("reqcd").equals("04")) {
	        	pstmt.setString(++cnt, etcData.get("userid"));
	        }
	        if (etcData.get("secuyn").equals("Y")) {
				pstmt.setString(++cnt, etcData.get("userid"));	
				pstmt.setString(++cnt, etcData.get("reqcd"));				
				if (etcData.get("qrygbn").equals("00")) {
					pstmt.setString(++cnt, etcData.get("stday"));					
					pstmt.setString(++cnt, etcData.get("edday"));
				} else if (etcData.get("qrygbn").equals("01")) {
					if (etcData.get("reqcd").equals("03") || etcData.get("reqcd").equals("04") || etcData.get("reqcd").equals("47")) {
						pstmt.setString(++cnt, etcData.get("userid"));	
						if (etcData.get("reqcd").equals("03")) pstmt.setString(++cnt, etcData.get("userid"));	
					} else if (etcData.get("reqcd").equals("01") || etcData.get("reqcd").equals("02") 
							  || etcData.get("reqcd").equals("11") || etcData.get("reqcd").equals("12")) {
						pstmt.setString(++cnt, etcData.get("userid"));	
						pstmt.setString(++cnt, etcData.get("reqcd"));	
					} else if (etcData.get("reqcd").equals("49")) {
						pstmt.setString(++cnt, etcData.get("userid"));	
					}
				} else if (etcData.get("qrygbn").equals("00")) {
					pstmt.setString(++cnt, etcData.get("stday"));					
					pstmt.setString(++cnt, etcData.get("edday"));
				} else if (etcData.get("reqcd").equals("49") && etcData.get("qrygbn").equals("02")) {
					pstmt.setString(++cnt, etcData.get("userid"));
				}
			} else {
				if (!etcData.get("reqcd").equals("RP")) {
					pstmt.setString(++cnt, etcData.get("stday"));					
					pstmt.setString(++cnt, etcData.get("edday"));
				} else {
					if (etcData.get("baseisr") != null && etcData.get("baseisr") != "") {
						pstmt.setString(++cnt, etcData.get("baseisr").substring(0,12));
						pstmt.setString(++cnt, etcData.get("baseisr").substring(12));
					}
				}
			}
	        if (etcData.get("reqdept") != null && etcData.get("reqdept") != "") {
				pstmt.setString(++cnt, etcData.get("reqdept"));
			}
			if (etcData.get("recvdept") != null && etcData.get("recvdept") != "") {
				pstmt.setString(++cnt, etcData.get("recvdept"));
			}
			if (etcData.get("requser") != null && etcData.get("requser") != "") {
				pstmt.setString(++cnt, "%"+etcData.get("requser")+"%");
			}
			if (etcData.get("reqtit") != null && etcData.get("reqtit") != "") {
				pstmt.setString(++cnt, "%"+etcData.get("reqtit")+"%");
			}
			
            //ecamsLogger.error(((LoggableStatement)pstmt).getQueryString());
            rs = pstmt.executeQuery();
            
			while(rs.next()){
				findSw = true;
				if (rs.getRow() ==1 && (etcData.get("reqcd").equals("01") || etcData.get("reqcd").equals("02")
						|| etcData.get("reqcd").equals("03") || etcData.get("reqcd").equals("04") 
						|| etcData.get("reqcd").equals("11") || etcData.get("reqcd").equals("12")
						|| etcData.get("reqcd").equals("47"))) {
					   rst = new HashMap<String, String>();
					   rst.put("cc_detcate","");
					   rst.put("cc_devtime","");
					   rst.put("cc_docno","");
					   rst.put("CC_HANDLER","");
					   rst.put("cc_isrid","0000");
					   rst.put("cc_isrsub","00");
					   rst.put("cc_scmuser","");
					   rst.put("cc_testeryn","");
					   rst.put("chguser","");
					   rst.put("isrid","0000");
					   rst.put("isridtitle","선택하세요");
					   rst.put("isrtitle","선택하세요");
					   rst.put("recvdept","");
					   rst.put("reqdate","");
					   rst.put("reqday","");
					   rst.put("reqdept","");
					   rst.put("reqenddt","");
					   rst.put("reqsta1","");
					   rst.put("reqsta2","");
					   rst.put("requser","");
					   rst.put("scmuser","");
					   rst.put("status","");
					   rtList.add(rst);   
					   rst = null;
					}
				
				if (findSw == true) {
					rst = new HashMap<String, String>();
					rst.put("isrid", rs.getString("isrid"));              //ISR-ID+SUBNO : cmc0100
					rst.put("isridtitle", "["+rs.getString("isrid")+"] "+rs.getString("cc_isrtitle"));//ISR-ID+SUBNO+TITLE
					rst.put("cc_isrid", rs.getString("cc_isrid"));        //ISR-ID : cmc0100
					rst.put("cc_isrsub", rs.getString("subid"));      //ISR SUB NO : cmc0100
					rst.put("isrtitle", rs.getString("cc_isrtitle"));     //요청제목 : cmc0100
					rst.put("reqdept", rs.getString("reqdept"));          //등록부서 : cmc0100
					rst.put("requser", rs.getString("requser"));          //등록인 : cmc0100
					rst.put("reqday", rs.getString("reqday"));            //등록일 : cmc0100
					rst.put("reqdate", rs.getString("reqdate"));          //등록일시 : cmc0100
					rst.put("cc_testeryn", rs.getString("cc_testeryn"));  //테스트참여여부 : cmc0100
					rst.put("cc_docno", rs.getString("cc_docno"));        //문서번호 : cmc0100
					rst.put("reqenddt", rs.getString("cc_reqenddt").substring(0,4)+"/"+
							               rs.getString("cc_reqenddt").substring(4,6)+"/"+
							               rs.getString("cc_reqenddt").substring(6));   //완료예정일 : cmc0100
					rst.put("status", "["+rs.getString("reqsta1")+"]"+rs.getString("reqsta2"));   //상태 : cmc0110
					rst.put("recvdept", rs.getString("recvdept"));       //접수파트 : cmc0110
					rst.put("chguser", rs.getString("chguser"));         //변경관리관리자 : cmc0110
					rst.put("cc_detcate", rs.getString("cc_detcate"));   //상세분류코드 : cmc0110
					rst.put("reqsta1", rs.getString("reqsta1"));         //진행구분 : cmc0110
					rst.put("reqsta2", rs.getString("reqsta2"));         //상세진행상황 : cmc0110
					rst.put("cc_substatus", rs.getString("cc_substatus"));         //상세진행상황 : cmc0110
					rst.put("CC_HANDLER", rs.getString("CC_HANDLER"));
					rst.put("cc_mainstatus", rs.getString("cc_mainstatus"));

					rst.put("maintab", rs.getString("maintab"));
					/*
					if (etcData.get("reqcd").equals("49") && etcData.get("qrygbn").equals("02")) {
						rst.put("subtab", rs.getString("subtab")+",49");
					} else {
						rst.put("subtab", rs.getString("subtab"));
					}*/
					rst.put("stasub", rs.getString("stasub"));
					rtList.add(rst);
				}
			}
			rs.close();
			pstmt.close();
			
			conn.close();
			
			rs = null;
			pstmt = null;
			conn = null;
			
			return rtList.toArray();
			
			
		} catch (SQLException sqlexception) {
			sqlexception.printStackTrace();
			ecamsLogger.error("## PrjInfo.getPrjList_Chg() SQLException START ##");
			ecamsLogger.error("## Error DESC : ", sqlexception);	
			ecamsLogger.error("## PrjInfo.getPrjList_Chg() SQLException END ##");			
			throw sqlexception;
		} catch (Exception exception) {
			exception.printStackTrace();
			ecamsLogger.error("## PrjInfo.getPrjList_Chg() Exception START ##");				
			ecamsLogger.error("## Error DESC : ", exception);	
			ecamsLogger.error("## PrjInfo.getPrjList_Chg() Exception END ##");				
			throw exception;
		}finally{
			if (strQuery != null) 	strQuery = null;
			if (rtList != null)		rtList = null;
			if (rs != null)     try{rs.close();}catch (Exception ex){ex.printStackTrace();}
			if (pstmt != null)  try{pstmt.close();}catch (Exception ex2){ex2.printStackTrace();}
			if (conn != null){
				try{
					ConnectionResource.release(conn);
				}catch(Exception ex3){
					ecamsLogger.error("## PrjInfo.getPrjList_Chg() connection release exception ##");
					ex3.printStackTrace();
				}
			}
		}
	}//end of getPrjList_Chg() method statement

	
	/**
	 * 프로젝트리스트 조회
	 * etcData 검색조건 Data
	   (단, 필요한 부분만 셋팅 하면 됨)
	   1. secuyn
	      Y  : 본인에게 권한있는 것만
	      N  :모두
	   2. qrygbn
	      00 : 전체
	      01 : 테스트접수대상
	      02 : 테스트
	   3. reqcd (화면구분)
	      42 : 개발계획서   
	      43 : 단위테스트작성
	      44 : 통합테스트작성
	      49 : 변경관리종료
	   4. stday,edday : 조회기간
	               단,secuyn='N'이거나 qrygbn='00'일 경우만 사용
	   5. reqdept : 등록부서
	   6. recvdept : 접수부서
	   7. requser : 요청자 (like)
	   8. rettit : 요청제목 (like)
	 * @param  HashMap<String,String> etcData
	 * @return Object[]
	 * @throws SQLException
	 * @throws Exception
	 */
	public Object[] getPrjList_Test(HashMap<String,String> etcData) throws SQLException, Exception {
		Connection        conn        = null;
		PreparedStatement pstmt       = null;
		ResultSet         rs          = null;
		StringBuffer      strQuery    = new StringBuffer();
		ArrayList<HashMap<String, String>> rtList = new ArrayList<HashMap<String, String>>();
		HashMap<String, String>			   rst	  = null;
		
		ConnectionContext connectionContext = new ConnectionResource();
		
		try {
			
			conn = connectionContext.getConnection();
			int cnt = 0;
			
			strQuery.setLength(0);
			strQuery.append("select a.cc_isrid || '-' || a.cc_isrsub isrid,a.cc_isrid,a.cc_isrsub subid,\n");
			strQuery.append("       b.cc_isrtitle,b.cc_testeryn,b.cc_reqenddt,                \n");
			strQuery.append("       c.cm_username requser,f.cm_deptname reqdept,              \n");
			strQuery.append("       g.cm_codename reqsta1,                                    \n");
			strQuery.append("       decode(a.cc_substatus,'23',decode(TESTREQ_TOTAL(a.cc_isrid,a.cc_isrsub),'OK','단위테스트종료',h.cm_codename),h.cm_codename) reqsta2,\n");
			strQuery.append("       to_char(b.cc_creatdt,'yyyy/mm/dd') reqday,                \n");
			strQuery.append("       to_char(b.cc_creatdt,'yyyy/mm/dd hh24:mi') reqdate,       \n");
			strQuery.append("       a.cc_mainstatus,a.cc_substatus,d.cm_deptname recvdept,    \n");
			strQuery.append("       ISRSTA_MAIN(a.cc_isrid,a.cc_isrsub,?) maintab             \n"); 
			//strQuery.append("       ISRSTA_SUB(a.cc_isrid,a.cc_isrsub,'TST',?) subtab         \n"); 
			strQuery.append("  from cmm0020 h,cmm0020 g,cmm0100 d,          \n");			
			strQuery.append("       cmm0100 f,cmm0040 c,cmc0100 b,cmc0110 a \n");
			if (etcData.get("secuyn").equals("Y")) {
				strQuery.append("where SYSTEST_SECUCHK(?,a.cc_isrid,a.cc_isrsub,?)='OK'   \n");
				if (etcData.get("qrygbn").equals("00")) {
					strQuery.append("and to_char(b.cc_creatdt,'yyyymmdd')>=? \n");
					strQuery.append("and to_char(b.cc_creatdt,'yyyymmdd')<=? \n");
				} else if (etcData.get("qrygbn").equals("01")) {         //대상
					if (etcData.get("reqcd").equals("51")) {             //테스트접수					
						strQuery.append("and SYSTEST_RECV(a.cc_isrid,a.cc_isrsub)='OK' \n");
					} else if (etcData.get("reqcd").equals("52")) {      //테스트결과등록				
						strQuery.append("and a.cc_substatus in ('31','32','35') \n");
					} else if (etcData.get("reqcd").equals("59")) {      //테스트종료승인대상				
						strQuery.append("and a.cc_substatus in ('34') \n");
						
					}
				} else if (etcData.get("qrygbn").equals("02")) {         //테스트종료대상				
					strQuery.append("and a.cc_substatus in ('32')       \n");				
					strQuery.append("and SYSTEST_END(?,a.cc_isrid,a.cc_isrsub)='OK' \n");
				} else if (etcData.get("qrygbn").equals("03")) {         //테스트종료요청				
					strQuery.append("and a.cc_substatus in ('34')       \n");	
				} else if (etcData.get("qrygbn").equals("04")) {         //테스트종료승인				
					strQuery.append("and a.cc_substatus in ('39')       \n");	
				} else if (etcData.get("qrygbn").equals("05")) {         //테스트종료승인(변경관리)				
					strQuery.append("and a.cc_substatus in ('36')       \n");	
				} else if (etcData.get("qrygbn").equals("06")) {         //테스트종료반려				
					strQuery.append("and a.cc_substatus in ('35')       \n");	
				} 
			} else {					
				if (etcData.get("qrygbn").equals("02")) {         //테스트종료대상				
					strQuery.append("where a.cc_substatus in ('32')       \n");				
					strQuery.append("  and SYSTEST_END(?,a.cc_isrid,a.cc_isrsub)='OK' \n");
				} else if (etcData.get("qrygbn").equals("03")) {         //테스트종료요청				
					strQuery.append("where a.cc_substatus in ('34')       \n");	
				} else if (etcData.get("qrygbn").equals("04")) {         //테스트종료승인				
					strQuery.append("where a.cc_substatus in ('39')       \n");	
				} else if (etcData.get("qrygbn").equals("05")) {         //테스트종료승인				
					strQuery.append("where a.cc_substatus in ('36')       \n");	
				} else if (etcData.get("qrygbn").equals("06")) {         //테스트종료승인				
					strQuery.append("where a.cc_substatus in ('35')       \n");	
				} else {
					strQuery.append("where to_char(b.cc_creatdt,'yyyymmdd')>=? \n");
					strQuery.append("  and to_char(b.cc_creatdt,'yyyymmdd')<=? \n");
				}
			}
			if (etcData.get("reqdept") != null && etcData.get("reqdept") != "") {
				strQuery.append("and b.cc_reqdept=?                        \n");
			}
			if (etcData.get("recvdept") != null && etcData.get("recvdept") != "") {
				strQuery.append("and a.cc_recvpart=?                       \n");
			}
			if (etcData.get("requser") != null && etcData.get("requser") != "") {
				strQuery.append("and c.cm_username like ?                  \n");
			}
			if (etcData.get("reqtit") != null && etcData.get("reqtit") != "") {
				strQuery.append("and b.cc_isrtitle like ?                  \n");
			}
			strQuery.append("  and a.cc_isrid=b.cc_isrid                 \n");
			strQuery.append("  and b.cc_editor=c.cm_userid               \n");
			strQuery.append("  and b.cc_reqdept=f.cm_deptcd              \n");
			strQuery.append("  and a.cc_recvpart=d.cm_deptcd              \n");
			strQuery.append("  and g.cm_macode='ISRSTAMAIN'              \n");
			strQuery.append("  and a.cc_mainstatus=g.cm_micode           \n");
			strQuery.append("  and h.cm_macode='ISRSTASUB'               \n");
			strQuery.append("  and a.cc_substatus=h.cm_micode            \n");
			strQuery.append("order by b.cc_creatdt desc,a.cc_isrid,a.cc_isrsub \n");
	        pstmt = conn.prepareStatement(strQuery.toString());
	        //pstmt =  new LoggableStatement(conn, strQuery.toString());
	        //pstmt.setString(++cnt, etcData.get("userid"));
	        pstmt.setString(++cnt, etcData.get("userid"));
	        if (etcData.get("secuyn").equals("Y")) {
	        	pstmt.setString(++cnt, etcData.get("userid"));
	        	pstmt.setString(++cnt, etcData.get("reqcd"));
				if (etcData.get("qrygbn").equals("00")) {
					pstmt.setString(++cnt, etcData.get("stday"));					
					pstmt.setString(++cnt, etcData.get("edday"));
				} else if (etcData.get("qrygbn").equals("02")) {
					pstmt.setString(++cnt, etcData.get("userid"));
				}
			} else {
				if (etcData.get("qrygbn").equals("02")) {
					pstmt.setString(++cnt, etcData.get("userid"));
				} else {
					pstmt.setString(++cnt, etcData.get("stday"));					
					pstmt.setString(++cnt, etcData.get("edday"));
				}
			}
	        if (etcData.get("reqdept") != null && etcData.get("reqdept") != "") {
				pstmt.setString(++cnt, etcData.get("reqdept"));
			}
			if (etcData.get("recvdept") != null && etcData.get("recvdept") != "") {
				pstmt.setString(++cnt, etcData.get("recvdept"));
			}
			if (etcData.get("requser") != null && etcData.get("requser") != "") {
				pstmt.setString(++cnt, "%"+etcData.get("requser")+"%");
			}
			if (etcData.get("reqtit") != null && etcData.get("reqtit") != "") {
				pstmt.setString(++cnt, "%"+etcData.get("reqtit")+"%");
			}
            //ecamsLogger.error(((LoggableStatement)pstmt).getQueryString());
            rs = pstmt.executeQuery();
            
			while(rs.next()){
				rst = new HashMap<String, String>();
				rst.put("isrid", rs.getString("isrid"));              //ISR-ID+SUBNO : cmc0100
				rst.put("cc_isrid", rs.getString("cc_isrid"));        //ISR-ID : cmc0100
				rst.put("cc_isrsub", rs.getString("subid"));          //ISR SUB NO : cmc0100
				rst.put("isrtitle", rs.getString("cc_isrtitle"));     //요청제목 : cmc0100
				rst.put("reqdept", rs.getString("reqdept"));          //등록부서 : cmc0100
				rst.put("requser", rs.getString("requser"));          //등록인 : cmc0100
				rst.put("reqday", rs.getString("reqday"));            //등록일 : cmc0100
				rst.put("reqdate", rs.getString("reqdate"));          //등록일시 : cmc0100
				rst.put("cc_testeryn", rs.getString("cc_testeryn"));  //테스트참여여부 : cmc0100
				rst.put("reqenddt", rs.getString("cc_reqenddt").substring(0,4)+"/"+
						               rs.getString("cc_reqenddt").substring(4,6)+"/"+
						               rs.getString("cc_reqenddt").substring(6));   //완료예정일 : cmc0100
				rst.put("status", "["+rs.getString("reqsta1")+"]"+rs.getString("reqsta2"));   //상태 : cmc0110
				rst.put("recvdept", rs.getString("recvdept"));       //접수파트 : cmc0110
				rst.put("reqsta1", rs.getString("reqsta1"));         //진행구분 : cmc0110
				rst.put("reqsta2", rs.getString("reqsta2"));         //상세진행상황 : cmc0110
				rst.put("cc_mainstatus", rs.getString("cc_mainstatus"));         //상세진행상황 : cmc0110
				rst.put("cc_substatus", rs.getString("cc_substatus"));         //상세진행상황 : cmc0110

				rst.put("maintab", rs.getString("maintab"));
				//rst.put("subtab", rs.getString("subtab"));
				rtList.add(rst);
				rst = null;
			}
			rs.close();
			pstmt.close();
			conn.close();
			rs = null;
			pstmt = null;
			conn = null;
			
			return rtList.toArray();
			
			
		} catch (SQLException sqlexception) {
			sqlexception.printStackTrace();
			ecamsLogger.error("## PrjInfo.getPrjList_Test() SQLException START ##");
			ecamsLogger.error("## Error DESC : ", sqlexception);	
			ecamsLogger.error("## PrjInfo.getPrjList_Test() SQLException END ##");			
			throw sqlexception;
		} catch (Exception exception) {
			exception.printStackTrace();
			ecamsLogger.error("## PrjInfo.getPrjList_Test() Exception START ##");				
			ecamsLogger.error("## Error DESC : ", exception);	
			ecamsLogger.error("## PrjInfo.getPrjList_Test() Exception END ##");				
			throw exception;
		}finally{
			if (strQuery != null) 	strQuery = null;
			if (rtList != null)		rtList = null;
			if (rs != null)     try{rs.close();}catch (Exception ex){ex.printStackTrace();}
			if (pstmt != null)  try{pstmt.close();}catch (Exception ex2){ex2.printStackTrace();}
			if (conn != null){
				try{
					ConnectionResource.release(conn);
				}catch(Exception ex3){
					ecamsLogger.error("## PrjInfo.getPrjList_Test() connection release exception ##");
					ex3.printStackTrace();
				}
			}
		}
	}//end of getPrjList_Chg() method statement
	
	public Object[] getPrjSub(String IsrId,String UserId) throws SQLException, Exception {
		Connection        conn        = null;
		PreparedStatement pstmt       = null;
		ResultSet         rs          = null;
		StringBuffer      strQuery    = new StringBuffer();
		ArrayList<HashMap<String, String>> rtList = new ArrayList<HashMap<String, String>>();
		HashMap<String, String>			   rst	  = null;
		
		ConnectionContext connectionContext = new ConnectionResource();
		
		try {

			conn = connectionContext.getConnection();
			int parmCnt = 0;
			
			strQuery.setLength(0);
			strQuery.append("select b.cm_deptname,a.cc_isrsub,                        \n");
			strQuery.append("       c.cm_codename mainsta,d.cm_codename substa,       \n");
			strQuery.append("       ISRSTA_MAIN(a.cc_isrid,a.cc_isrsub,?) maintab  \n"); 
			//strQuery.append("       ISRSTA_SUB(a.cc_isrid,a.cc_isrsub,'REQ',?) subtab \n");  
			strQuery.append("  from cmm0020 c,cmm0020 d,cmm0100 b,cmc0110 a           \n");	
			strQuery.append(" where a.cc_isrid=?                  \n");
			strQuery.append("   and a.cc_recvpart=b.cm_deptcd     \n");
			strQuery.append("   and c.cm_macode='ISRSTAMAIN'      \n");
			strQuery.append("   and a.cc_mainstatus=c.cm_micode   \n");
			strQuery.append("   and d.cm_macode='ISRSTASUB'       \n");
			strQuery.append("   and a.cc_substatus=d.cm_micode    \n");
			strQuery.append(" order by a.cc_isrsub                \n");

			pstmt = conn.prepareStatement(strQuery.toString());
	        //pstmt =  new LoggableStatement(conn, strQuery.toString());
	        
	        pstmt.setString(++parmCnt, UserId);
	        pstmt.setString(++parmCnt, IsrId);
            //ecamsLogger.error(((LoggableStatement)pstmt).getQueryString());
            rs = pstmt.executeQuery();
            
			while(rs.next()){
				if (rs.getRow() == 1) {
					rst = new HashMap<String, String>();
					rst.put("cc_isrid", IsrId);                           //ISR-ID : cmc0100
					rst.put("cc_isrsub", "");                             //ISR SUB NO : cmc0100
					rst.put("recvdept", "전체");                          //접수파트 : cmc0110
					rtList.add(rst);
				}
				rst = new HashMap<String, String>();
				rst.put("cc_isrid", IsrId);                           //ISR-ID : cmc0100
				rst.put("cc_isrsub", rs.getString("cc_isrsub"));      //ISR SUB NO : cmc0100
				rst.put("recvdept", rs.getString("cm_deptname"));     //접수파트 : cmc0110
				rst.put("maintab", rs.getString("maintab"));
				//rst.put("subtab", rs.getString("subtab"));
				rst.put("mainsta", rs.getString("mainsta"));
				rst.put("substa", rs.getString("substa"));
				rtList.add(rst);
			}
			rs.close();
			pstmt.close();
			
			conn.close();
			
			rs = null;
			pstmt = null;
			conn = null;
			
			return rtList.toArray();
			
			
		} catch (SQLException sqlexception) {
			sqlexception.printStackTrace();
			ecamsLogger.error("## PrjInfo.getPrjSub() SQLException START ##");
			ecamsLogger.error("## Error DESC : ", sqlexception);	
			ecamsLogger.error("## PrjInfo.getPrjSub() SQLException END ##");			
			throw sqlexception;
		} catch (Exception exception) {
			exception.printStackTrace();
			ecamsLogger.error("## PrjInfo.getPrjSub() Exception START ##");				
			ecamsLogger.error("## Error DESC : ", exception);	
			ecamsLogger.error("## PrjInfo.getPrjSub() Exception END ##");				
			throw exception;
		}finally{
			if (strQuery != null) 	strQuery = null;
			if (rtList != null)		rtList = null;
			if (rs != null)     try{rs.close();}catch (Exception ex){ex.printStackTrace();}
			if (pstmt != null)  try{pstmt.close();}catch (Exception ex2){ex2.printStackTrace();}
			if (conn != null){
				try{
					ConnectionResource.release(conn);
				}catch(Exception ex3){
					ecamsLogger.error("## PrjInfo.getPrjSub() connection release exception ##");
					ex3.printStackTrace();
				}
			}
		}
	}//end of getPrjSub() method statement
	/**
	 * <PRE>
	 * 1. MethodName	: getPrjInfoDetail
	 * 2. ClassName		: PrjInfo
	 * 3. Commnet			: 프로젝트정보를 상세조회합니다.
	 * 4. 작성자				: no name
	 * 5. 작성일				: 2010. 12. 10. 오후 3:28:07
	 * </PRE>
	 * 		@return Object[]
	 * 		@param IsrId
	 * 		@param IsrSub
	 * 		@return
	 * 		@throws SQLException
	 * 		@throws Exception
	 */
	public Object[] getPrjInfoDetail(String IsrId,String IsrSub,String ReqCd,String UserId) throws SQLException, Exception {
		Connection        conn        = null;
		PreparedStatement pstmt       = null;
		ResultSet         rs          = null;
		PreparedStatement pstmt2      = null;
		ResultSet         rs2         = null;
		StringBuffer      strQuery    = new StringBuffer();
		ArrayList<HashMap<String, String>> rtList = new ArrayList<HashMap<String, String>>();
		HashMap<String, String>	rst	  = null;
		
		ConnectionContext connectionContext = new ConnectionResource();
		
		try {
			
			conn = connectionContext.getConnection();
			int parmCnt = 0;
			strQuery.setLength(0);
			strQuery.append("select a.cc_isrid || '-' || a.cc_isrsub isrid,a.cc_isrid,a.cc_isrsub subid,\n");
			strQuery.append("       b.cc_isrtitle,b.cc_testeryn,b.cc_reqenddt,b.cc_docno,     \n");
			strQuery.append("       c.cm_username requser,f.cm_deptname reqdept,              \n");
			strQuery.append("       g.cm_codename reqsta1,                                    \n");
			strQuery.append("       decode(a.cc_substatus,'23',decode(TESTREQ_TOTAL(a.cc_isrid,a.cc_isrsub),'OK','단위테스트종료',h.cm_codename),h.cm_codename) reqsta2,\n");
			strQuery.append("       to_char(b.cc_creatdt,'yyyy/mm/dd') reqday,                \n");
			strQuery.append("       to_char(b.cc_creatdt,'yyyy/mm/dd hh24:mi') reqdate,       \n");
			strQuery.append("       a.cc_mainstatus,a.cc_substatus,d.cm_deptname recvdept,    \n");
			strQuery.append("       to_char(a.cc_eddate,'yyyy/mm/dd hh24:mi') cc_eddate,     \n");
			strQuery.append("       a.cc_endacpt,a.cc_realmm,nvl(a.cc_realedday,'') cc_realedday, \n");
			strQuery.append("       a.cc_endgbn,a.cc_recvuser,a.cc_realtime,                  \n");
			strQuery.append("       ISRSTA_MAIN(a.cc_isrid,a.cc_isrsub,?) maintab,            \n"); 
			strQuery.append("       ISRSTA_SUB(a.cc_isrid,a.cc_isrsub,?,?) subtab,            \n"); 
			strQuery.append("       nvl(a.cc_handler,'') cc_handler,nvl(e.cm_username,'') handlernm \n");
			strQuery.append("  from cmm0020 h,cmm0020 g,cmm0100 d,cmm0040 e, \n");			
			strQuery.append("       cmm0100 f,cmm0040 c,cmc0100 b,cmc0110 a \n");			
			strQuery.append(" where a.cc_isrid=? 					        \n");
			if(IsrSub != null && IsrSub != ""){
				strQuery.append(" and a.cc_isrsub=?          					\n");
			}
			strQuery.append("   and a.cc_isrid=b.cc_isrid                   \n");
			strQuery.append("   and b.cc_editor=c.cm_userid                 \n");
			strQuery.append("   and b.cc_reqdept=f.cm_deptcd                \n");
			strQuery.append("   and a.cc_recvpart=d.cm_deptcd               \n");
			strQuery.append("   and g.cm_macode='ISRSTAMAIN'                \n");
			strQuery.append("   and a.cc_mainstatus=g.cm_micode             \n");
			strQuery.append("   and h.cm_macode='ISRSTASUB'                 \n");
			strQuery.append("   and a.cc_substatus=h.cm_micode              \n");
			strQuery.append("   and a.cc_handler=e.cm_userid(+)             \n");
			
			pstmt = conn.prepareStatement(strQuery.toString());
	        //pstmt =  new LoggableStatement(conn, strQuery.toString());
	        pstmt.setString(++parmCnt, UserId);
	        pstmt.setString(++parmCnt, ReqCd);
	        pstmt.setString(++parmCnt, UserId);
	        pstmt.setString(++parmCnt, IsrId);
	        if(IsrSub != null && IsrSub != ""){
	        	pstmt.setString(++parmCnt, IsrSub);
	        }
            //ecamsLogger.error(((LoggableStatement)pstmt).getQueryString());
            rs = pstmt.executeQuery();
            
			if (rs.next()){
				rst = new HashMap<String, String>();
				rst.put("isrid", rs.getString("isrid"));              //ISR-ID+SUBNO : cmc0100
				rst.put("cc_isrid", rs.getString("cc_isrid"));        //ISR-ID : cmc0100
				rst.put("cc_isrsub", rs.getString("subid"));          //ISR SUB NO : cmc0100
				rst.put("isrtitle", rs.getString("cc_isrtitle"));     //요청제목 : cmc0100
				rst.put("reqdept", rs.getString("reqdept"));          //등록부서 : cmc0100
				rst.put("requser", rs.getString("requser"));          //등록인 : cmc0100
				rst.put("reqday", rs.getString("reqday"));            //등록일 : cmc0100
				rst.put("reqdate", rs.getString("reqdate"));          //등록일시 : cmc0100
				rst.put("cc_docno", rs.getString("cc_docno"));          //문서번호 : cmc0100
				rst.put("cc_testeryn", rs.getString("cc_testeryn"));  //테스트참여여부 : cmc0100
				rst.put("reqenddt", rs.getString("cc_reqenddt").substring(0,4)+"/"+
						               rs.getString("cc_reqenddt").substring(4,6)+"/"+
						               rs.getString("cc_reqenddt").substring(6));   //완료예정일 : cmc0100
				rst.put("status", "["+rs.getString("reqsta1")+"]"+rs.getString("reqsta2"));   //상태 : cmc0110
				rst.put("recvdept", rs.getString("recvdept"));       //접수파트 : cmc0110
				rst.put("reqsta1", rs.getString("reqsta1"));         //진행구분 : cmc0110
				rst.put("reqsta2", rs.getString("reqsta2"));         //상세진행상황 : cmc0110
				rst.put("cc_mainstatus", rs.getString("cc_mainstatus"));         //상세진행상황 : cmc0110
				rst.put("cc_substatus", rs.getString("cc_substatus"));         //상세진행상황 : cmc0110
				
				rst.put("cc_recvuser", rs.getString("cc_recvuser"));
				rst.put("cc_eddate", rs.getString("cc_eddate"));           //ISR SUB종료일 : cmc0110
				rst.put("cc_endacpt", rs.getString("cc_endacpt"));         //ISR종료번호 : cmc0110
				if (rs.getBigDecimal("cc_realmm") != null){
					rst.put("cc_realmm", rs.getBigDecimal("cc_realmm").toString());         //실투입공수 : cmc0110
				} else{
					rst.put("cc_realmm", "");
				}
				if (rs.getBigDecimal("cc_realtime") != null){
					rst.put("cc_realtime", rs.getBigDecimal("cc_realtime").toString());         //실투입공수 : cmc0110
				} else{
					rst.put("cc_realtime", "");
				}
				rst.put("cc_realedday", "");//실완료일 : cmc0110
				if (rs.getString("cc_realedday") != "" && rs.getString("cc_realedday") != null){
					rst.put("cc_realedday", rs.getString("cc_realedday").substring(0,4)+"/"
							+rs.getString("cc_realedday").substring(4,6)+"/"
							+rs.getString("cc_realedday").substring(6));
				}
				rst.put("cc_endgbn", rs.getString("cc_endgbn"));         //ISR완료구분 : cmc0110
				rst.put("cc_handler", rs.getString("cc_handler"));         //요구담당자 : cmc0110
				rst.put("handlernm", rs.getString("handlernm"));         //요구담당자명 : cmc0110

				rst.put("maintab", rs.getString("maintab"));
				rst.put("subtab", rs.getString("subtab"));
				if (ReqCd != null && ReqCd != "") {
					if (ReqCd.equals("39") && rs.getString("cc_endacpt") != null) {
						strQuery.setLength(0);
						strQuery.append("select cc_acptno,cc_reqetc,cc_seqno,cc_status, \n");
						strQuery.append("       to_char(cc_lastdt,'yyyy/mm/dd hh24:mi') endreqdt, \n");
						strQuery.append("       to_char(cc_eddate,'yyyy/mm/dd hh24:mi') enddate   \n");
						strQuery.append("  from cmc0190                    \n");			
						strQuery.append(" where cc_isrid=? and cc_isrsub=? \n");			
						strQuery.append("   and cc_acptno=?               \n");
						pstmt2 = conn.prepareStatement(strQuery.toString());
				        pstmt2.setString(1, IsrId);
				        pstmt2.setString(2, IsrSub);
				        pstmt2.setString(3, rs.getString("cc_endacpt"));
			            rs2 = pstmt2.executeQuery();
						if (rs2.next()){
							rst.put("cc_acptno", rs2.getString("cc_acptno"));//ISR종료 신청번호
							rst.put("cc_reqetc", rs2.getString("cc_reqetc"));
							rst.put("cc_seqno", rs2.getString("cc_seqno"));
							rst.put("enddate", rs2.getString("enddate"));
							rst.put("endreqdt", rs2.getString("endreqdt"));
							if (rs2.getString("cc_status").equals("0")) rst.put("stasub", "ISR종료승인요청가능");
							else if (rs2.getString("cc_status").equals("1")) rst.put("stasub", "ISR종료요청승인대기");
							else if (rs2.getString("cc_status").equals("3")) rst.put("stasub", "ISR종료요청반려");
							else if (rs2.getString("cc_status").equals("9")) rst.put("stasub", "ISR종료요청완료");
						}else{
							rst.put("cc_acptno", "");
							rst.put("cc_reqetc", "");
						}
						rs2.close();
						pstmt2.close();
					}	
				}
					
				
				rtList.add(rst);
				rst = null;
			}
			rs.close();
			pstmt.close();
			conn.close();
			
			rs = null;
			pstmt = null;
			rs2 = null;
			pstmt2 = null;
			conn = null;
			
			return rtList.toArray();
			
			
		} catch (SQLException sqlexception) {
			sqlexception.printStackTrace();
			ecamsLogger.error("## PrjInfo.getPrjInfoDetail() SQLException START ##");
			ecamsLogger.error("## Error DESC : ", sqlexception);	
			ecamsLogger.error("## PrjInfo.getPrjInfoDetail() SQLException END ##");			
			throw sqlexception;
		} catch (Exception exception) {
			exception.printStackTrace();
			ecamsLogger.error("## PrjInfo.getPrjInfoDetail() Exception START ##");				
			ecamsLogger.error("## Error DESC : ", exception);	
			ecamsLogger.error("## PrjInfo.getPrjInfoDetail() Exception END ##");				
			throw exception;
		}finally{
			if (strQuery != null) 	strQuery = null;
			if (rtList != null)		rtList = null;
			if (rs != null)     try{rs.close();}catch (Exception ex){ex.printStackTrace();}
			if (pstmt != null)  try{pstmt.close();}catch (Exception ex2){ex2.printStackTrace();}
			if (rs2 != null)     try{rs2.close();}catch (Exception ex){ex.printStackTrace();}
			if (pstmt2 != null)  try{pstmt2.close();}catch (Exception ex2){ex2.printStackTrace();}
			if (conn != null){
				try{
					ConnectionResource.release(conn);
				}catch(Exception ex3){
					ecamsLogger.error("## PrjInfo.getPrjInfoDetail() connection release exception ##");
					ex3.printStackTrace();
				}
			}
		}
	}//end of getPrjInfoDetail() method statement
	
	


	public boolean getPrjInfoJIKMOO(String UserID, String PrjNo) throws SQLException, Exception {
		Connection        conn        = null;
		PreparedStatement pstmt       = null;
		ResultSet         rs          = null;
		StringBuffer      strQuery    = new StringBuffer();
		boolean rtnValue = false;
	
		Object[] returnObjectArray = null;
		ConnectionContext connectionContext = new ConnectionResource();		
		
	
		
		try {
			conn = connectionContext.getConnection();
	        
			strQuery.setLength(0);
			
			strQuery.append("select cd_prjjik from cmd0304 where cd_prjno = ? and cd_prjuser = ? and cd_closedt is null				\n");
			pstmt = conn.prepareStatement(strQuery.toString());
			pstmt.setString(1, PrjNo);
			pstmt.setString(2, UserID);
			rs = pstmt.executeQuery();
			while(rs.next()){
				if(rs.getString("cd_prjjik").equals("PM"))
					rtnValue = true;
			}
			
			rs.close();
			pstmt.close();
			conn.close();
			rs = null;
			pstmt = null;
			conn = null;
			return rtnValue;
			
		} catch (SQLException sqlexception) {
			sqlexception.printStackTrace();
			ecamsLogger.error("## PrjInfo.getPrjInfoJIKMOO() SQLException START ##");
			ecamsLogger.error("## Error DESC : ", sqlexception);	
			ecamsLogger.error("## PrjInfo.getPrjInfoJIKMOO() SQLException END ##");			
			throw sqlexception;
		} catch (Exception exception) {
			exception.printStackTrace();
			ecamsLogger.error("## PrjInfo.getPrjInfoJIKMOO() Exception START ##");				
			ecamsLogger.error("## Error DESC : ", exception);	
			ecamsLogger.error("## PrjInfo.getPrjInfoJIKMOO() Exception END ##");				
			throw exception;
		}finally{
			if (strQuery != null)	strQuery = null;
			if (returnObjectArray != null)	returnObjectArray = null;				
			if (rs != null)     try{rs.close();}catch (Exception ex){ex.printStackTrace();}
			if (pstmt != null)  try{pstmt.close();}catch (Exception ex2){ex2.printStackTrace();}
			if (conn != null){
				try{
					ConnectionResource.release(conn);
				}catch(Exception ex3){
					ecamsLogger.error("## PrjInfo.getPrjInfoJIKMOO() connection release exception ##");
					ex3.printStackTrace();
				}
			}
		}
	}//end of getProgList() method statement
	
	
	public Object[] getFlowCnts(String UserId) throws SQLException, Exception {
		Connection        conn        = null;
		PreparedStatement pstmt       = null;
		ResultSet         rs          = null;
		StringBuffer      strQuery    = new StringBuffer();
		
		ArrayList<HashMap<String, String>> rsval = new ArrayList<HashMap<String, String>>();
		HashMap<String, String>			   rst	  = null;
		
		ConnectionContext connectionContext = new ConnectionResource();
		
		
		
		try {
			conn = connectionContext.getConnection();
			int cnt = 0;
			strQuery.setLength(0);
			strQuery.append("select 'ReqCnt' cd, count(a.cc_requser3) as cnt from cmc0401 a																\n");												
			strQuery.append("where a.cc_requser3 = ? and a.cc_reqid in (select cc_reqid from cmc0410 where cc_status in('0','1')) 						\n");
			cnt++;
			strQuery.append("union																										       			\n");          
			strQuery.append("select 'OrderCnt' cd, count(*) as cnt from cmc0450 a						                                     			\n");            
			strQuery.append("where a.cc_runner = ? and a.cc_orderid in (select cc_orderid from cmc0420 where cc_status in ('0','1'))					\n");								                                                                
			cnt++;
			strQuery.append("union 																																																\n");
			strQuery.append("select 'DochkoutCnt' cd, count(A.cr_rsrcname) as cnt 																								\n");
			strQuery.append("from (select c.cr_rsrcname, c.cr_status from cmr1000 a, cmr1010 b, cmr0020 c												\n");
			strQuery.append("where a.cr_acptno = b.cr_acptno and a.cr_editor = ? and substr(a.cr_acptno,5,2) = '01' and							\n");
			strQuery.append("a.cr_qrycd = '01' and b.cr_itemid = c.cr_itemid and c.cr_status = '5'														\n");
			strQuery.append("minus																																																\n");
			strQuery.append("select c.cr_rsrcname, c.cr_status from cmr1000 a, cmr1010 b, cmr0020 c														\n");
			strQuery.append("where a.cr_acptno = b.cr_acptno and a.cr_editor = ? and substr(a.cr_acptno,5,2) = '04' and							\n");
			strQuery.append("b.cr_qrycd = '04' and a.cr_status != '3' and b.cr_itemid = c.cr_itemid) A  												\n");
			cnt++;
			cnt++;
			
			strQuery.append("union																							             				\n");                     
			strQuery.append("select 'chkOutReqCnt' cd, count(*) as cnt from cmr1000 a, cmr9900 b														\n");	                                                     
			strQuery.append("where a.cr_editor = ? and a.cr_status = '0' and a.cr_qrycd ='01' and a.cr_acptno = b.cr_acptno\n");
			strQuery.append("and b.cr_locat = '00' and b.cr_team in ('SYSDN','SYSFMK','SYSPDN') and b.cr_status not in ('3', '9')						\n");
			cnt++;
			strQuery.append("union																														\n");                                 
			strQuery.append("select 'chkInReqCnt' cd, count(*) as cnt from cmr1000 a, cmr9900 b															\n");	                                                     
			strQuery.append("where a.cr_editor = ? and a.cr_status = '0' and a.cr_qrycd ='04' and a.cr_acptno = b.cr_acptno\n");
			strQuery.append("and b.cr_locat = '00' and b.cr_team in('SYSPUP','SYSPF','SYSMV') and b.cr_status not in ('3', '9')	 						\n");                      			                                        
			cnt++;
			strQuery.append("union																														\n");											                                 
			strQuery.append("select 'HandlerCnt' cd, count(*) as cnt from cmr9900 a 												     				\n");                                                  
			strQuery.append("where a.cr_status = '0' and a.cr_team  = ? and a.cr_locat = '00' and a.cr_sgngbn  = '91'									\n");		                                                                
			cnt++;
			
			strQuery.append("union																								        				\n");                            
			strQuery.append("select 'BubuJangCnt' cd, count(*) as cnt from cmr9900 a 												    				\n");                                                   
			strQuery.append("where a.cr_status = '0' and a.cr_team= ? and a.cr_locat = '00' and a.cr_sgngbn ='41'										\n");	                                                                
			cnt++;
			strQuery.append("union																								       					\n");                            
			strQuery.append("select 'BuJangCnt' cd,count(*) as cnt from cmr9900 a												         				\n");                                              
			strQuery.append("where a.cr_status = '0' and a.cr_team = ? and a.cr_locat = '00' and a.cr_sgngbn ='31'										\n");		                                                                
			cnt++;
			strQuery.append("union																									    				\n");                               
			strQuery.append("select 'QACnt' cd,count(*) as cnt from cmr9900 a 													         				\n");                                               
			strQuery.append("where a.cr_status = '0' and a.cr_team = 'Q1' and a.cr_locat = '00' and a.cr_confusr = ?									\n");					                                                                
			cnt++;
			strQuery.append("union																														\n");		                                        
			strQuery.append("select 'ThirdCnt' cd,count(*) as cnt from cmr9900 a 																		\n");		                                                         
			strQuery.append("where a.cr_status = '0' and a.cr_team = ? and a.cr_locat = '00' and a.cr_confname='3자확인'									\n");							                                                                
			cnt++;
			strQuery.append("union																														\n");			                                        
			strQuery.append("select 'HandlerDeptCnt' cd, count(*) as cnt from cmr9900 a 																\n");		                                                          
			strQuery.append("where a.cr_status = '0' and a.cr_team = ? and a.cr_locat = '00' and a.cr_confname='주관부서확인'								\n");					                                                              
			cnt++;
		    strQuery.append("union																														\n");												                                                                
			strQuery.append(" select 'RunConfCnt' cd, count(*) as cnt                                                                  				 	\n");
			strQuery.append("    from cmr1000 a , cmr9900 b , cmm0030 c ,  cmm0100 d , cmm0040 e														\n");
			strQuery.append("    where a.cr_acptno = b.cr_acptno  																						\n");
			strQuery.append("and ( a.cr_editor = ? or exists (select 'X' from cmm0043 f where a.cr_editor = f.cm_userid and f.cm_rgtcd = 'LIB')) 		\n");
			strQuery.append("and  a.cr_status = '0' and  c.cm_syscd = a.cr_syscd  																		\n");                                                                               			 
			strQuery.append(" and  b.cr_team = 'SYSED'                 																					\n");                                                                    
			strQuery.append("and  a.CR_TEAMCD = d.CM_DEPTCD               																				\n");                                                                
			strQuery.append("and  a.cr_editor = e.cm_userid          																					\n");                                                           
			strQuery.append("and  b.cr_locat = '00'            																							\n");                                                                 
			strQuery.append("and  b.cr_status = '0'     																								\n");                                                                            
			strQuery.append("and  exists 																												\n");
			strQuery.append("   (select 'X' FROM CMR1010 D ,CMM0038 E WHERE D.CR_ACPTNO = A.CR_ACPTNO AND D.CR_SYSCD = E.CM_SYSCD   					\n");                                                         
			strQuery.append("        AND D.CR_RSRCCD = E.CM_RSRCCD AND E.CM_SVRCD = '05')                  												\n");                                                                       		
			cnt++;
			strQuery.append("union																														\n");
			strQuery.append("select 'CompileCnt' cd, count(*) as cnt from cmr1000 a, cmr9900 b															\n");					                                                     
			strQuery.append("where a.cr_editor = ? and a.cr_status = '0' and a.cr_qrycd ='04' and a.cr_acptno = b.cr_acptno								\n");
			strQuery.append("and b.cr_locat = '00' and b.cr_team in ('SYSCB','SYSGB') and b.cr_status not in ('3', '9')									\n");
			cnt++;
			strQuery.append("union																														\n");																	
			strQuery.append("select 'CompleteCnt' cd, count(a.cc_reqid) as cnt from   cmc0400 a 														\n");													
			strQuery.append("where a.cc_requser1 = ? and a.cc_status in ('0','1')																		\n");
			strQuery.append("and 0 = 																													\n");											
			strQuery.append("(select nvl(sum(decode(cc_status, '9', 0, 1)),0) from   cmc0420 where  a.cc_reqid = cc_reqid ) 							\n");	
			cnt++;


            pstmt = conn.prepareStatement(strQuery.toString());
            //pstmt = new LoggableStatement(conn,strQuery.toString());
            for (int i=0;i<cnt;i++) {
            	pstmt.setString(i+1, UserId);
            }            
            //ecamsLogger.error(((LoggableStatement)pstmt).getQueryString());
            rs = pstmt.executeQuery();
            while (rs.next()){
            	if (rs.getInt("cnt") > 0) {
	            	rst = new HashMap<String, String>();
					rst.put("qrycd", rs.getString("cd"));
					rst.put("count", rs.getString("cnt"));
					rsval.add(rst);
					rst = null;
            	}
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
			ecamsLogger.error("## PrjInfo.getFlowCnts() SQLException START ##");
			ecamsLogger.error("## Error DESC : ", sqlexception);	
			ecamsLogger.error("## PrjInfo.getFlowCnts() SQLException END ##");			
			throw sqlexception;
		} catch (Exception exception) {
			exception.printStackTrace();
			ecamsLogger.error("## PrjInfo.getFlowCnts() Exception START ##");				
			ecamsLogger.error("## Error DESC : ", exception);	
			ecamsLogger.error("## PrjInfo.getFlowCnts() Exception END ##");				
			throw exception;
		}finally{
			if (strQuery != null) 	strQuery = null;
			if (rs != null)     try{rs.close();}catch (Exception ex){ex.printStackTrace();}
			if (pstmt != null)  try{pstmt.close();}catch (Exception ex2){ex2.printStackTrace();}
			if (conn != null){
				try{
					ConnectionResource.release(conn);
				}catch(Exception ex3){
					ecamsLogger.error("## PrjInfo.getFlowCnts() connection release exception ##");
					ex3.printStackTrace();
				}
			}
		}
	}//end of getFlowCnt() method statement
   
	
	public Object[] getPrjJoiner(String PrjNo, String Gubun) throws SQLException, Exception {
		Connection        conn        = null;
		PreparedStatement pstmt       = null;
		ResultSet         rs          = null;
		StringBuffer      strQuery    = new StringBuffer();
		ArrayList<HashMap<String, String>>  rsval = new ArrayList<HashMap<String, String>>();
		HashMap<String, String>			  	rst	  = null;
		
		ConnectionContext connectionContext = new ConnectionResource();
		try {
			conn = connectionContext.getConnection();
			
			strQuery.setLength(0);
			strQuery.append("select a.CD_USERTEAM,a.CD_PRJJIK,b.CM_USERID,b.CM_USERNAME,\n");
			strQuery.append("B.CM_POSITION,c.CM_CODENAME as JIKMOO,c.CM_SEQNO,d.CM_DEPTNAME \n");
			strQuery.append("from cmd0304 a,cmm0040 b,cmm0020 c,cmm0100 d \n");
			strQuery.append("where a.cd_prjno=? AND A.CD_CLOSEDT IS NULL \n");//PrjNo
			strQuery.append("  and a.cd_gubun = ? \n");
			strQuery.append("  and a.cd_prjuser=b.cm_userid \n");
			strQuery.append("  and A.cD_USERTEAM=D.cm_DEPTCD \n");
			strQuery.append("  and c.cm_macode='PRJJIK' and c.cm_micode=a.cd_prjjik \n");
			strQuery.append("ORDER BY c.CM_SEQNO \n");
            pstmt = conn.prepareStatement(strQuery.toString());
           	pstmt.setString(1, PrjNo);
           	pstmt.setString(2, Gubun);
            rs = pstmt.executeQuery();
            rsval.clear();
            while (rs.next()){
            	rst = new HashMap<String, String>();
				rst.put("JIKMOO", rs.getString("JIKMOO"));
				rst.put("TEAMNAME", rs.getString("CM_DEPTNAME"));
				rst.put("CM_USERNAME", rs.getString("CM_USERNAME"));
				rst.put("CM_USERID", rs.getString("CM_USERID"));
				rst.put("TEAMCD", rs.getString("CD_USERTEAM"));
				rst.put("CD_PRJJIK", rs.getString("CD_PRJJIK"));
				rst.put("CM_SEQNO", rs.getString("CM_SEQNO"));
				rsval.add(rst);
				rst = null;
			}
            rs.close();
            pstmt.close();
            conn.close();
            
            rs = null;
            pstmt = null;
            conn = null;
            
    		return rsval.toArray();            
			
		} catch (SQLException sqlexception) {
			sqlexception.printStackTrace();
			ecamsLogger.error("## PrjInfo.getPrjJoiner() SQLException START ##");
			ecamsLogger.error("## Error DESC : ", sqlexception);	
			ecamsLogger.error("## PrjInfo.getPrjJoiner() SQLException END ##");			
			throw sqlexception;
		} catch (Exception exception) {
			exception.printStackTrace();
			ecamsLogger.error("## PrjInfo.getPrjJoiner() Exception START ##");				
			ecamsLogger.error("## Error DESC : ", exception);	
			ecamsLogger.error("## PrjInfo.getPrjJoiner() Exception END ##");				
			throw exception;
		}finally{
			if (strQuery != null) 	strQuery = null;
			if (rsval != null)	rsval = null;
			if (rs != null)     try{rs.close();}catch (Exception ex){ex.printStackTrace();}
			if (pstmt != null)  try{pstmt.close();}catch (Exception ex2){ex2.printStackTrace();}
			if (conn != null){
				try{
					ConnectionResource.release(conn);
				}catch(Exception ex3){
					ecamsLogger.error("## PrjInfo.getPrjJoiner() connection release exception ##");
					ex3.printStackTrace();
				}
			}
		}

	}//end of getPrjJoiner() method statement
    
    public Object[] getPrjJoinerInfo(String UserId, String PrjNo) throws SQLException, Exception {
		Connection        conn        = null;
		PreparedStatement pstmt       = null;
		ResultSet         rs          = null;
		StringBuffer      strQuery    = new StringBuffer();
		ArrayList<HashMap<String, String>>  rsval = new ArrayList<HashMap<String, String>>();
		HashMap<String, String>			  	rst	  = null;
		
		ConnectionContext connectionContext = new ConnectionResource();
		try {
			conn = connectionContext.getConnection();
			
			strQuery.setLength(0);
			strQuery.append("select a.cd_prjjik,a.cd_edityn,a.cd_docyn,b.cm_admin \n");
			strQuery.append("  from cmd0304 a , cmm0040 b  \n");
			strQuery.append(" where a.cd_prjno=? \n");//PrjNo
			strQuery.append("   and a.cd_prjuser=? and a.cd_prjuser = b.cm_userid \n");//UserId
			strQuery.append("   and a.cd_closedt IS NULL  \n");
            pstmt = conn.prepareStatement(strQuery.toString());
            pstmt.setString(1, PrjNo);
           	pstmt.setString(2, UserId);
            rs = pstmt.executeQuery();
            rsval.clear();
            
            UserInfo userinfo = new UserInfo();
            rst = new HashMap<String, String>();
            if (rs.next()){
				rst.put("cd_prjno", PrjNo);
				rst.put("cd_prjjik", rs.getString("cd_prjjik"));
				rst.put("cd_edityn", rs.getString("cd_edityn"));
				rst.put("cd_docyn", rs.getString("cd_docyn"));
				rst.put("cm_admin", rs.getString("cm_admin"));
	            rst.put("PMO", userinfo.getPMOInfo_conn(UserId,conn));	            
			}else{
				rst.put("cd_prjno", PrjNo);
				rst.put("cd_prjjik", "");
				rst.put("PMO", userinfo.getPMOInfo(UserId));
				if (userinfo.isAdmin(UserId)){
					rst.put("cd_edityn", "Y");
					rst.put("cd_docyn", "Y");
					rst.put("cm_admin", "1");
				}else{
					rst.put("cd_edityn", "N");
					rst.put("cd_docyn", "N");
					rst.put("cm_admin", "0");					
				}
			}
            rsval.add(rst);
            rst = null;
            userinfo = null;
            rs.close();
            pstmt.close();
            conn.close();
            
            rs = null;
            pstmt = null;
            conn = null;
    		return rsval.toArray();            
			
		} catch (SQLException sqlexception) {
			sqlexception.printStackTrace();
			ecamsLogger.error("## PrjInfo.getPrjJoinerInfo() SQLException START ##");
			ecamsLogger.error("## Error DESC : ", sqlexception);	
			ecamsLogger.error("## PrjInfo.getPrjJoinerInfo() SQLException END ##");			
			throw sqlexception;
		} catch (Exception exception) {
			exception.printStackTrace();
			ecamsLogger.error("## PrjInfo.getPrjJoinerInfo() Exception START ##");				
			ecamsLogger.error("## Error DESC : ", exception);	
			ecamsLogger.error("## PrjInfo.getPrjJoinerInfo() Exception END ##");				
			throw exception;
		}finally{
			if (rsval != null)	rsval = null;
			if (strQuery != null) 	strQuery = null;
			if (rs != null)     try{rs.close();}catch (Exception ex){ex.printStackTrace();}
			if (pstmt != null)  try{pstmt.close();}catch (Exception ex2){ex2.printStackTrace();}
			if (conn != null){
				try{
					ConnectionResource.release(conn);
				}catch(Exception ex3){
					ecamsLogger.error("## PrjInfo.getPrjJoinerInfo() connection release exception ##");
					ex3.printStackTrace();
				}
			}
		}

	}//end of getPrjJoinerInfo() method statement

    public String getHomeDir(String UserId) throws SQLException, Exception {
		Connection        conn        = null;
		PreparedStatement pstmt       = null;
		ResultSet         rs          = null;
		StringBuffer      strQuery    = new StringBuffer();
		ConnectionContext connectionContext = new ConnectionResource();
		
		try {
			conn = connectionContext.getConnection();
			
			strQuery.setLength(0);
		    strQuery.append("SELECT replace(cd_devhome,'\','\\') as cd_devhome FROM CMD0200 ");
		    strQuery.append(" WHERE cd_syscd = '99999' and cd_userid=? \n");
            pstmt = conn.prepareStatement(strQuery.toString());	
            pstmt.setString(1, UserId);
            rs = pstmt.executeQuery();
            if (rs.next()){
            	if (rs.getString("cd_devhome") != null && rs.getString("cd_devhome") != "") {
            		return rs.getString("cd_devhome").replace("\\","/");
            	}	
			}//end of while-loop statement
            rs.close();
            pstmt.close();
            conn.close();
            
            rs = null;
            pstmt = null;
            conn = null;
            
            return "";
            
	    } catch (SQLException sqlexception) {
			sqlexception.printStackTrace();
			ecamsLogger.error("## PrjInfo.getHomeDir() SQLException START ##");
			ecamsLogger.error("## Error DESC : ", sqlexception);	
			ecamsLogger.error("## PrjInfo.getHomeDir() SQLException END ##");			
			throw sqlexception;
		} catch (Exception exception) {
			exception.printStackTrace();
			ecamsLogger.error("## PrjInfo.getHomeDir() Exception START ##");				
			ecamsLogger.error("## Error DESC : ", exception);	
			ecamsLogger.error("## PrjInfo.getHomeDir() Exception END ##");				
			throw exception;
		}finally{
			if (strQuery != null) 	strQuery = null;
			if (rs != null)     try{rs.close();}catch (Exception ex){ex.printStackTrace();}
			if (pstmt != null)  try{pstmt.close();}catch (Exception ex2){ex2.printStackTrace();}
			if (conn != null){
				try{
					ConnectionResource.release(conn);
				}catch(Exception ex3){
					ecamsLogger.error("## PrjInfo.getHomeDir() connection release exception ##");
					ex3.printStackTrace();
				}
			}
		}
	}//end of getHomeDir() method statement
    
    
    /**
     * <PRE>
     * 1. MethodName	: getSubTab
     * 2. ClassName		: PrjInfo
     * 3. Commnet			: 
     * 4. 작성자				: no name
     * 5. 작성일				: 2011. 3. 31. 오후 2:54:11
     * </PRE>
     * 		@return String
     * 		@param IsrId
     * 		@param IsrSub
     * 		@param ReqCd
     * 		@param UserId
     * 		@return
     * 		@throws SQLException
     * 		@throws Exception
     */
    public String getSubTab(String IsrId,String IsrSub,String ReqCd,String UserId) throws SQLException, Exception {
		Connection        conn        = null;
		PreparedStatement pstmt       = null;
		ResultSet         rs          = null;
		StringBuffer      strQuery    = new StringBuffer();
		ConnectionContext connectionContext = new ConnectionResource();
		
		try {
			conn = connectionContext.getConnection();
			String retMsg = "";
			strQuery.setLength(0);
		    strQuery.append("SELECT ISRSTA_SUB(?,?,?,?) subtab FROM dual ");
            pstmt = conn.prepareStatement(strQuery.toString());	
            pstmt.setString(1, IsrId);
            pstmt.setString(2, IsrSub);
            pstmt.setString(3, ReqCd);
            pstmt.setString(4, UserId);
            rs = pstmt.executeQuery();
            if (rs.next()){
            	retMsg = rs.getString("subtab");
			}//end of while-loop statement
            rs.close();
            pstmt.close();
            conn.close();
            
            rs = null;
            pstmt = null;
            conn = null;
            
            return retMsg;
            
	    } catch (SQLException sqlexception) {
			sqlexception.printStackTrace();
			ecamsLogger.error("## PrjInfo.getSubTab() SQLException START ##");
			ecamsLogger.error("## Error DESC : ", sqlexception);	
			ecamsLogger.error("## PrjInfo.getSubTab() SQLException END ##");			
			throw sqlexception;
		} catch (Exception exception) {
			exception.printStackTrace();
			ecamsLogger.error("## PrjInfo.getSubTab() Exception START ##");				
			ecamsLogger.error("## Error DESC : ", exception);	
			ecamsLogger.error("## PrjInfo.getSubTab() Exception END ##");				
			throw exception;
		}finally{
			if (strQuery != null) 	strQuery = null;
			if (rs != null)     try{rs.close();}catch (Exception ex){ex.printStackTrace();}
			if (pstmt != null)  try{pstmt.close();}catch (Exception ex2){ex2.printStackTrace();}
			if (conn != null){
				try{
					ConnectionResource.release(conn);
				}catch(Exception ex3){
					ecamsLogger.error("## PrjInfo.getSubTab() connection release exception ##");
					ex3.printStackTrace();
				}
			}
		}
	}//end of getSubTab() method statement
	public String chkDocseq(String PrjNo,String devHome,String dirPath,boolean insSw,Connection conn)	throws SQLException, Exception {
		PreparedStatement 	pstmt       = null;
		ResultSet         	rs          = null;
		PreparedStatement 	pstmt2       = null;
		ResultSet         	rs2          = null;
		StringBuffer      	strQuery    = new StringBuffer();
		
		try {
			int                 i = 0;
			String              strPath1 = "";
			String              strSeq   = "";
			String              strUpSeq = "";
			boolean             findSw   = false;
			String              wkDepth[] = null;
			String              fullPath = "";
			String              prjName = "";
			
			devHome = devHome.replace("\\", "/");
			prjName = devHome.substring(devHome.lastIndexOf("/")+1);
			if (prjName.indexOf(".")>0) prjName = prjName.substring(prjName.indexOf(".")+1);
			dirPath = dirPath.replace("\\", "/");
			
			if (dirPath.indexOf("/")>0) strPath1 = dirPath.substring(0,dirPath.indexOf("/"));
			else strPath1 = dirPath;
			
			if (strPath1.indexOf(".")>0) strPath1 = strPath1.substring(strPath1.indexOf(".")+1);
			if (dirPath.indexOf("/")>0) dirPath = strPath1 + dirPath.substring(dirPath.indexOf("/"));
			else dirPath = strPath1;
			
			strPath1 = prjName + "/"+dirPath;
			wkDepth = strPath1.split("/");
			//ecamsLogger.error("+++++strPath1+++++"+new String(strPath1.getBytes("MS949")));
			for (i=0;wkDepth.length>i;i++) {
				if (i==0) fullPath = wkDepth[i];
				else fullPath = fullPath + "\\" + wkDepth[i];
				
				findSw = false;
				strQuery.setLength(0);
				strQuery.append("select cd_docseq from cmd0303      \n");
				strQuery.append(" where cd_prjno=? and cd_dirpath=? \n");
				pstmt = conn.prepareStatement(strQuery.toString());
				//pstmt = new LoggableStatement(conn,strQuery.toString());
				pstmt.setString(1, PrjNo);
				pstmt.setString(2, fullPath);
				//new String((((LoggableStatement)pstmt).getQueryString()).getBytes("MS949")
				//ecamsLogger.error(new String((((LoggableStatement)pstmt).getQueryString()).getBytes("MS949")));
				rs = pstmt.executeQuery();
				if (rs.next()) {
					strSeq = rs.getString("cd_docseq");
					strUpSeq = rs.getString("cd_docseq");
					//ecamsLogger.error("++++dircode 1++++"+PrjNo + " " + strSeq+" "+strUpSeq);
					findSw = true;
				} else {
					if (insSw == true) {
						strQuery.setLength(0);
		            	strQuery.append("select lpad(to_number(nvl(max(cd_docseq),0))+1,5,'0') seq \n");
		            	strQuery.append("  from cmd0303 where cd_prjno=?         \n");
		            	pstmt2 = conn.prepareStatement(strQuery.toString());
			            pstmt2.setString(1, PrjNo);
			            rs2 = pstmt2.executeQuery();
			            if (rs2.next()) strSeq = rs2.getString("seq");
			            pstmt2.close();
			            rs2.close();
			            
			           //ecamsLogger.error("++++dircode 2++++"+PrjNo + " " + strSeq+" "+strUpSeq);
		            	strQuery.setLength(0);
		            	strQuery.append("insert into cmd0303 (CD_PRJNO,CD_DOCSEQ, \n");
		            	strQuery.append("   CD_DIRNAME,CD_UPDOCSEQ,CD_DIRPATH) values        \n");
		            	strQuery.append("   (?, ?, ?, ?, ?)         \n");
		            	pstmt2 = conn.prepareStatement(strQuery.toString());
		            	pstmt2.setString(1, PrjNo);
		            	pstmt2.setString(2, strSeq);
		            	pstmt2.setString(3, wkDepth[i]);
		            	pstmt2.setString(4, strUpSeq);
		            	pstmt2.setString(5, fullPath);
		            	pstmt2.executeUpdate();
		            	pstmt2.close();
		            	pstmt2 = null;
		            	
		            	strUpSeq = strSeq;
		            	findSw = true;
					} else 	{
						findSw = false;
						break;
					}
				}
				pstmt.close();
				rs.close();
				pstmt = null;
				rs = null;
			}
			if (findSw == false) strSeq = "";
			//ecamsLogger.error("+++++PrjNo,strPath1,strSeq++++++++"+PrjNo + ", " + new String(strPath1.getBytes("MS949"))+", "+strSeq);
			return strSeq;
			
		} catch (SQLException sqlexception) {
			sqlexception.printStackTrace();
			ecamsLogger.error("## Cmd1600.dirOpenChk() SQLException START ##");
			ecamsLogger.error("## Error DESC : ", sqlexception);	
			ecamsLogger.error("## Cmd1600.dirOpenChk() SQLException END ##");			
			throw sqlexception;
		} catch (Exception exception) {
			exception.printStackTrace();
			ecamsLogger.error("## Cmd1600.dirOpenChk() Exception START ##");				
			ecamsLogger.error("## Error DESC : ", exception);	
			ecamsLogger.error("## Cmd1600.dirOpenChk() Exception END ##");				
			throw exception;
		}finally{
			if (strQuery != null)	strQuery = null;
			if (rs != null)     try{rs.close();}catch (Exception ex){ex.printStackTrace();}
			if (pstmt != null)  try{pstmt.close();}catch (Exception ex2){ex2.printStackTrace();}
			if (rs2 != null)     try{rs2.close();}catch (Exception ex){ex.printStackTrace();}
			if (pstmt2 != null)  try{pstmt2.close();}catch (Exception ex2){ex2.printStackTrace();}
		}
	}//end of dirOpenChk() method statement
	
	
	
	/**
	 * ISR 리스트 조회
	 * @param UserId
	 * @return Object[]
	 * @throws SQLException
	 * @throws Exception
	 */
	public Object[] getISRList(String UserId,String ReqCD) throws SQLException, Exception {
		Connection        conn        = null;
		PreparedStatement pstmt       = null;
		PreparedStatement pstmt2      = null;
		ResultSet         rs          = null;
		ResultSet         rs2         = null;
		StringBuffer      strQuery    = new StringBuffer();
		Object[] returnObjectArray    = null;
		ArrayList<HashMap<String, String>> rtList = new ArrayList<HashMap<String, String>>();
		HashMap<String, String> rst	  = null;
		
		ConnectionContext connectionContext = new ConnectionResource();		
		
		try {
			
			conn = connectionContext.getConnection();
			
			strQuery.setLength(0);
			strQuery.append("select a.CC_ISRID || '-' || a.CC_ISRSUB ISRID,a.CC_ISRID,a.CC_ISRSUB, \n");
			strQuery.append("       b.CC_ISRTITLE,b.CC_TESTERYN,b.CC_DOCNO,b.CC_REQENDDT, \n");
			strQuery.append("       c.cm_username REQUSER,f.cm_deptname REQDEPT, \n");
			strQuery.append("       g.cm_codename REQSTA1,                       \n");
			strQuery.append("       decode(a.cc_substatus,'23',decode(TESTREQ_TOTAL(a.cc_isrid,a.cc_isrsub),'OK','단위테스트종료',h.cm_codename),h.cm_codename) REQSTA2,\n");
			strQuery.append("       a.CC_RECVPART,i.cm_deptname RECVPART, \n");
			strQuery.append("       a.CC_DETCATE,k.cm_codename DETCATE, \n");
			strQuery.append("       a.CC_HANDLER,L.CM_USERNAME CONTUSER, \n");
			strQuery.append("       to_char(b.cc_creatdt,'yyyy/mm/dd') REQDAY, \n");
			strQuery.append("       to_char(b.cc_creatdt,'yyyy/mm/dd hh24:mi') REQDATE, \n");
			strQuery.append("       a.CC_MAINSTATUS,a.CC_SUBSTATUS,a.CC_CHGUSER,j.CM_USERNAME CHGUSER,a.CC_CHGTYPE,a.CC_RECVMSG \n");
			strQuery.append("  from cmc0110 a,cmc0100 b,cmm0040 c,cmm0100 f,cmm0020 g, \n");
			strQuery.append("       cmm0020 h,cmm0100 i,cmm0040 j,cmm0020 k,cmm0040 L, \n");
			strQuery.append("       cmc0210 d,cmc0230 e,cmc0240 m \n");
			strQuery.append(" where a.CC_ISRID=b.CC_ISRID \n");
			strQuery.append("   and a.CC_ISRID=d.CC_ISRID \n");
			strQuery.append("   and a.CC_ISRID=d.CC_ISRID \n");
			strQuery.append("   and a.CC_ISRID=m.CC_ISRID \n");
			strQuery.append("   and a.CC_ISRSUB=d.CC_ISRSUB \n");
			strQuery.append("   and a.CC_ISRSUB=e.CC_ISRSUB \n");
			strQuery.append("   and a.CC_ISRSUB=m.CC_ISRSUB \n");
			strQuery.append("   and b.CC_EDITOR=c.CM_USERID \n");
			strQuery.append("   and b.CC_REQDEPT=f.CM_DEPTCD \n");
			strQuery.append("   and g.CM_MACODE='ISRSTAMAIN' \n");
			strQuery.append("   and a.CC_MAINSTATUS=g.CM_MICODE \n");
			strQuery.append("   and h.CM_MACODE='ISRSTASUB' \n");
			strQuery.append("   and a.CC_SUBSTATUS=h.CM_MICODE \n");
			strQuery.append("   and a.CC_RECVPART=i.CM_DEPTCD \n");
			strQuery.append("   and k.CM_MACODE='DETCATE' \n");
			strQuery.append("   and a.CC_DETCATE=k.CM_MICODE \n");
			strQuery.append("   and a.CC_HANDLER=L.CM_USERID \n");
			strQuery.append("   and a.CC_CHGUSER=j.CM_USERID \n");
			strQuery.append("   and d.CC_SCMUSER=? \n");
			strQuery.append("   and d.CC_SCMUSER=e.CC_SCMUSER \n");
			strQuery.append("   and d.CC_SCMUSER=m.CC_SCMUSER \n");
			strQuery.append("   and d.CC_EDDATE is null \n");
			
	        pstmt = conn.prepareStatement(strQuery.toString());
	        //pstmt = new LoggableStatement(conn,strQuery.toString());
	        pstmt.setString(1, UserId);
	        //ecamsLogger.error(((LoggableStatement)pstmt).getQueryString());
	        rs = pstmt.executeQuery();
	        
	        String strSCMUSER = "";
	        int cnt = 0;
	        while(rs.next()) {
				rst = new HashMap<String, String>();
				rst.put("ISRID", rs.getString("ISRID"));              //ISR-ID+SUBNO : cmc0100
				rst.put("CC_ISRID", rs.getString("CC_ISRID"));        //ISR-ID : cmc0100
				rst.put("CC_ISRSUB", rs.getString("CC_ISRSUB"));      //ISR SUB NO : cmc0100
				rst.put("CC_ISRTITLE", rs.getString("CC_ISRTITLE"));     //요청제목 : cmc0100
				rst.put("REQDEPT", rs.getString("REQDEPT"));          //등록부서 : cmc0100
				rst.put("REQUSER", rs.getString("REQUSER"));          //등록인 : cmc0100
				rst.put("REQDAY", rs.getString("REQDAY"));            //등록일 : cmc0100
				rst.put("REQDATE", rs.getString("REQDATE"));          //등록일시 : cmc0100
				rst.put("CC_TESTERYN", rs.getString("CC_TESTERYN"));  //테스트참여여부 : cmc0100
				rst.put("CC_DOCNO", rs.getString("CC_DOCNO"));        //문서번호 : cmc0100
				rst.put("CC_REQENDDT", rs.getString("CC_REQENDDT").substring(0,4)+"/"+
						               rs.getString("CC_REQENDDT").substring(4,6)+"/"+
						               rs.getString("CC_REQENDDT").substring(6));   //완료예정일 : cmc0100
				rst.put("STATUS", "["+rs.getString("REQSTA1")+"]"+rs.getString("REQSTA2"));   //상태 : cmc0110
				rst.put("CC_RECVPART", rs.getString("CC_RECVPART"));
				rst.put("RECVPART", rs.getString("RECVPART"));       //접수파트 : cmc0110
				rst.put("CC_CHGUSER", rs.getString("CC_CHGUSER"));   //변경관리관리자 : cmc0110
				rst.put("CHGUSER", rs.getString("CHGUSER"));         //변경관리관리자 : cmc0110
				rst.put("CC_DETCATE", rs.getString("CC_DETCATE"));   //상세분류코드 : cmc0110
				rst.put("DETCATE", rs.getString("DETCATE"));         //상세분류명 : cmc0110
				rst.put("REQSTA1", rs.getString("REQSTA1"));         //진행구분 : cmc0110
				rst.put("REQSTA2", rs.getString("REQSTA2"));         //상세진행상황 : cmc0110
				rst.put("CC_HANDLER", rs.getString("CC_HANDLER"));
				rst.put("CONTUSER", rs.getString("CONTUSER"));
				rst.put("CC_CHGTYPE", rs.getString("CC_CHGTYPE"));
				rst.put("CC_MAINSTATUS", rs.getString("CC_MAINSTATUS"));
				rst.put("CC_SUBSTATUS", rs.getString("CC_SUBSTATUS"));
				rst.put("CC_RECVMSG", rs.getString("CC_RECVMSG"));
				
				strSCMUSER = "";
				if(Integer.parseInt(rs.getString("CC_SUBSTATUS")) > 15){
	    	        strQuery.setLength(0);
	            	strQuery.append("select a.CC_SCMUSER,b.CM_USERNAME \n");
	            	strQuery.append("  from cmc0210 a, cmm0040 b \n");
	            	strQuery.append(" where a.CC_ISRID = ? \n");
	            	strQuery.append("   and a.CC_ISRSUB = ? \n");
	            	strQuery.append("   and a.CC_SCMUSER=b.CM_USERID \n");
	            	
	            	pstmt2 = conn.prepareStatement(strQuery.toString());
	            	cnt = 0;
		            pstmt2.setString(++cnt, rs.getString("CC_ISRID"));
		            pstmt2.setString(++cnt, rs.getString("CC_ISRSUB"));
		            rs2 = pstmt2.executeQuery();
		            
		            while (rs2.next()){
		            	if (strSCMUSER.length() == 0){
		            		strSCMUSER = rs2.getString("CC_SCMUSER")+"."+rs2.getString("CM_USERNAME");
		            	}else{
		            		strSCMUSER = strSCMUSER + "," + rs2.getString("CC_SCMUSER")+"."+rs2.getString("CM_USERNAME");
		            	}
		            }
		            pstmt2.close();
		            rs2.close();
		            
		            rs2 = null;
		            pstmt2 = null;
				}
				rst.put("SCMUSER", strSCMUSER);
				rtList.add(rst);
				rst = null;
	        }
	        rs.close();
	        pstmt.close();
	        conn.close();
	        
	  		rs = null;
	  		pstmt = null;
	  		conn = null;
			
			returnObjectArray = rtList.toArray();
			rtList.clear();
			rtList = null;

			return returnObjectArray;
			
			
		} catch (SQLException sqlexception) {
			sqlexception.printStackTrace();
			ecamsLogger.error("## PrjInfo.getISRList() SQLException START ##");
			ecamsLogger.error("## Error DESC : ", sqlexception);	
			ecamsLogger.error("## PrjInfo.getISRList() SQLException END ##");			
			throw sqlexception;
		} catch (Exception exception) {
			exception.printStackTrace();
			ecamsLogger.error("## PrjInfo.getISRList() Exception START ##");				
			ecamsLogger.error("## Error DESC : ", exception);	
			ecamsLogger.error("## PrjInfo.getISRList() Exception END ##");				
			throw exception;
		}finally{
			if (strQuery != null) 	strQuery = null;
			if (returnObjectArray != null)	returnObjectArray = null;
			if (rs != null)     try{rs.close();}catch (Exception ex){ex.printStackTrace();}
			if (pstmt != null)  try{pstmt.close();}catch (Exception ex2){ex2.printStackTrace();}
			if (rs2 != null)     try{rs2.close();}catch (Exception ex){ex.printStackTrace();}
			if (pstmt2 != null)  try{pstmt2.close();}catch (Exception ex2){ex2.printStackTrace();}
			if (conn != null){
				try{
					ConnectionResource.release(conn);
				}catch(Exception ex3){
					ecamsLogger.error("## PrjInfo.getISRList() connection release exception ##");
					ex3.printStackTrace();
				}
			}
		}
	}//end of getISRList() method statement
	
	public Document getPrjInfoTree(String PrjNo) throws SQLException, Exception {
		Connection        conn        = null;
		PreparedStatement pstmt       = null;
		ResultSet         rs          = null;
		PreparedStatement pstmt2       = null;
		ResultSet         rs2          = null;
		StringBuffer      strQuery    = new StringBuffer();
		CreateXml		  myXml		  = new CreateXml();
		
		
		ConnectionContext connectionContext = new ConnectionResource();
		
		try {
			conn = connectionContext.getConnection();
			
			strQuery.setLength(0);
			strQuery.append("Select cd_docseq,cd_updocseq,cd_dirname, cd_dirpath		\n");
			strQuery.append("from (Select * from cmd0303 where cd_prjno=? order by CD_DIRNAME )	\n");//PrjNo
			strQuery.append("start with CD_UPDOCSEQ is null  				\n");
			strQuery.append("connect by prior CD_DOCSEQ = CD_UPDOCSEQ order by CD_UPDOCSEQ desc,CD_DIRNAME 		\n");
			
            pstmt = conn.prepareStatement(strQuery.toString());
            pstmt = new LoggableStatement(conn,strQuery.toString());
            pstmt.setString(1, PrjNo);
            
            myXml.init_Xml("ID","cd_docseq","cd_dirname","cd_fullpath","existYN","cd_updocseq");
           
            ecamsLogger.error(((LoggableStatement)pstmt).getQueryString());
            rs = pstmt.executeQuery();
            while (rs.next()){
            	
				/*if (rs.getString("cd_updocseq") != null) {
					strQuery.setLength(0);
					strQuery.append("Select cd_dirname	                       	    \n");
					strQuery.append("from (Select * from cmd0303 where cd_prjno=? )	\n");//PrjNo
					strQuery.append("start with CD_DOCSEQ=?        				    \n");
					strQuery.append("connect by prior CD_UPDOCSEQ=CD_DOCSEQ 		\n");			
		            pstmt2 = conn.prepareStatement(strQuery.toString());
		            pstmt2.setString(1, PrjNo);
		            pstmt2.setString(2, rs.getString("cd_docseq"));
		            rs2 = pstmt2.executeQuery();
		            while (rs2.next()) {
		            	if (strFullPath != "" && strFullPath != null) strFullPath = "/"+strFullPath;
		            	strWork1 = strFullPath;
		            	strFullPath = rs2.getString("cd_dirname")+strWork1;
		            }
		            rs2.close();
		            pstmt2.close();
				} else {
					strFullPath = rs.getString("cd_dirname");
				}*/
            	
            	String existYN = "";
            	/*
            	strQuery.setLength(0);
				strQuery.append("select count(*) as cnt from cmr0031    			\n");
				strQuery.append("where cr_prjno=?									\n");//PrjNo
				strQuery.append("  and cr_docseq in         						\n");
				strQuery.append("		(select cd_docseq from cmd0303				\n");
				strQuery.append("			where cd_prjno=?						\n");//PrjNo
				strQuery.append("			start with cd_updocseq=?				\n");//cr_docseq
				strQuery.append("			connect by prior cd_docseq=cd_updocseq 	\n");
				strQuery.append("			union select ? from dual)				\n");//cr_docseq
	            pstmt2 = conn.prepareStatement(strQuery.toString());
	            pstmt2.setString(1, PrjNo);
	            pstmt2.setString(2, PrjNo);
	            pstmt2.setString(3, rs.getString("cd_docseq"));
	            pstmt2.setString(4, rs.getString("cd_docseq"));
	            rs2 = pstmt2.executeQuery();
	            */
            	
            	strQuery.setLength(0);
				strQuery.append("select count(*) as cnt from cmr0030    			\n");
				strQuery.append("where cr_prjno= ?									\n");//PrjNo
				strQuery.append("  and cr_docseq = ?         						\n");
	            pstmt2 = conn.prepareStatement(strQuery.toString());
	            pstmt2.setString(1, PrjNo);
	            pstmt2.setString(2, rs.getString("cd_docseq"));
	            rs2 = pstmt2.executeQuery();
            	if (rs2.next()) {
	            	if (rs2.getInt("cnt") >0) existYN = "Y";
	            	else existYN = "N";
	            }
	            rs2.close();
	            pstmt2.close();
	            
	        	strQuery.setLength(0);
				strQuery.append("select count(*) as cnt from cmd0303    			\n");
				strQuery.append("where cd_prjno= ?									\n");//PrjNo
				strQuery.append("  and cd_docseq = ? and cd_updocseq is null       	\n");
	            pstmt2 = conn.prepareStatement(strQuery.toString());
	            pstmt2.setString(1, PrjNo);
	            pstmt2.setString(2, rs.getString("cd_docseq"));
	            rs2 = pstmt2.executeQuery();
            	if (rs2.next()) {
	            	if (rs2.getInt("cnt") >0) existYN = "Y";
	            	else existYN = "N";
	            }
	            rs2.close();
	            pstmt2.close();
	           
	            //existYN = "Y";
	            
				myXml.addXML(rs.getString("cd_docseq"),
						rs.getString("cd_docseq"), 
						rs.getString("cd_dirname"),
						rs.getString("cd_dirpath"),
						existYN,
						rs.getString("cd_updocseq")
						);
				existYN = null;
			}//end of while-loop statement
            rs.close();
            pstmt.close();
            conn.close();
            
            rs = null;
            pstmt = null;
            conn = null;
            
    		return myXml.getDocument();
            
		} catch (SQLException sqlexception) {
			sqlexception.printStackTrace();
			ecamsLogger.error("## PrjInfo.getPrjInfoTree() SQLException START ##");
			ecamsLogger.error("## Error DESC : ", sqlexception);	
			ecamsLogger.error("## PrjInfo.getPrjInfoTree() SQLException END ##");			
			throw sqlexception;
		} catch (Exception exception) {
			exception.printStackTrace();
			ecamsLogger.error("## PrjInfo.getPrjInfoTree() Exception START ##");				
			ecamsLogger.error("## Error DESC : ", exception);	
			ecamsLogger.error("## PrjInfo.getPrjInfoTree() Exception END ##");				
			throw exception;
		}finally{
			if (myXml != null)	myXml = null;
			if (strQuery != null) 	strQuery = null;
			if (rs != null)     try{rs.close();}catch (Exception ex){ex.printStackTrace();}
			if (pstmt != null)  try{pstmt.close();}catch (Exception ex2){ex2.printStackTrace();}
			if (conn != null){
				try{
					ConnectionResource.release(conn);
				}catch(Exception ex3){
					ecamsLogger.error("## PrjInfo.getPrjInfoTree() connection release exception ##");
					ex3.printStackTrace();
				}
			}
		}
		
	}//end of getPrjInfoTree() method statement
	
	
}//end of PrjInfo class statement
