/*****************************************************************************************
	1. program ID	: Cmr3200.java
	2. create date	: 2008.04.08
	3. auth		    : m.s.kang
	4. update date	: 2009.02.21
	5. auth		    : no name
	6. description	: Request List
*****************************************************************************************/

package app.eCmc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.io.BufferedReader;

import org.apache.logging.log4j.Logger;
import org.w3c.dom.Document;
import com.ecams.common.dbconn.ConnectionContext;
import com.ecams.common.dbconn.ConnectionResource;
import com.ecams.common.logger.EcamsLogger;
import java.util.ArrayList;
import java.util.HashMap;

import app.common.*;
import app.eCmr.*;

/**
 * @author bigeyes
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */


public class Cmc1100{
    /**
     * Logger Class Instance Creation
     * logger
     */
    Logger ecamsLogger  = EcamsLogger.getLoggerInstance();
    

    /**
     * <PRE>
     * 1. MethodName	: get_SelectList
     * 2. ClassName		: Cmc1100
     * 3. Commnet			: 이행진행현황을 조회합니다.
     * 4. 작성자				: no name
     * 5. 작성일				: 2010. 12. 15. 오후 2:13:17
     * </PRE>
     * 		@return Object[]
     * 		@param etcData
     * 		@return
     * 		@throws SQLException
     * 		@throws Exception
     */
    public Object[] get_SelectList(HashMap<String,String> etcData) throws SQLException, Exception {
		Connection        conn        = null;
		PreparedStatement pstmt       = null;
		ResultSet         rs          = null;
		PreparedStatement pstmt2      = null;
		ResultSet         rs2         = null;
		StringBuffer      strQuery    = new StringBuffer();
		
		Object[] 		  returnObject= null;
		ArrayList<HashMap<String, String>>  rtList	= new ArrayList<HashMap<String, String>>();
		HashMap<String, String>			  	rst	 	= null;	
		
		ConnectionContext connectionContext = new ConnectionResource();
		
		try {
			
			rtList.clear();
			Integer           cnt         = 0;
			//String			  ColorSw     = "";
			String            svPer       = "";
			//String            strAcpt     = "";
			
			conn = connectionContext.getConnection();
			
			/*
			 // 요청사항기준
			<mx:AdvancedDataGridColumn headerText="ISR ID" dataField="isrid" width="100" textAlign="center"/>
			<mx:AdvancedDataGridColumn headerText="SUB" dataField="isrsub" width="3" textAlign="center" id="gisrsub"/>
			<mx:AdvancedDataGridColumn headerText="등록일시" dataField="reqdate" width="80" textAlign="center"/>
			<mx:AdvancedDataGridColumn headerText="요청부서" dataField="reqdept" width="100"/>
			<mx:AdvancedDataGridColumn headerText="요청인" dataField="requser" width="60" textAlign="center"/>
			<mx:AdvancedDataGridColumn headerText="요청제목" dataField="reqtitle" width="150"/>
			<mx:AdvancedDataGridColumn headerText="완료요청일" dataField="reqedday" width="110"/>
			
			
			// 접수기준
			<mx:AdvancedDataGridColumn headerText="접수파트" dataField="recvdept" width="100"  id="grecvdept"/>
			<mx:AdvancedDataGridColumn headerText="ISR접수일" dataField="recvdate" width="80"  id="grecvdate"/>
			<mx:AdvancedDataGridColumn headerText="접수관리자" dataField="recvuser" width="100"  id="grecvuser"/>
			<mx:AdvancedDataGridColumn headerText="접수구분" dataField="recvgbn" width="100"  id="grecvgbn"/>
			<mx:AdvancedDataGridColumn headerText="접수담당자" dataField="recvsubuser" width="100"  id="grecvsubuser"/>
			
			// 변경기준
			<mx:AdvancedDataGridColumn headerText="RFC접수일" dataField="chgdate" width="80"  id="gchgdate"/>
			<mx:AdvancedDataGridColumn headerText="변경관리자" dataField="chguser" width="100"  id="gchguser"/>
			<mx:AdvancedDataGridColumn headerText="변경담당자" dataField="chgsubuser" width="100" id="gchgsubuser" />
			
			//요청사항기준
			<mx:AdvancedDataGridColumn headerText="진행단계" dataField="reqsta1" width="80" id="greqsta1"/>
			<mx:AdvancedDataGridColumn headerText="세부진행상황" dataField="reqsta2" width="120" id="greqsta2"/>
			
			// 변경기준
			<mx:AdvancedDataGridColumn headerText="완료예정일" dataField="chgedday" width="90" id="gchgedday" />
			<mx:AdvancedDataGridColumn headerText="진행율" dataField="chgpercent" width="80"  id="gchgpercent"/>
			<mx:AdvancedDataGridColumn headerText="변경종료구분" dataField="chgedgbn" width="120" id="gchgedgbn"/>
			<mx:AdvancedDataGridColumn headerText="변경종료일시" dataField="chgeddate" width="110" id="gchgeddate" />
			
			// 접수기준
			<mx:AdvancedDataGridColumn headerText="ISR완료구분" dataField="isredgbn" width="100" id="gisredgbn"/>
			<mx:AdvancedDataGridColumn headerText="ISR완료일" dataField="isreddate" width="110" textAlign="center" id="gisreddate"/>
			
			// 요청사항기준
			<mx:AdvancedDataGridColumn headerText="만족도" dataField="isrsatis" width="80" id="gisrsatis"/>
			<mx:AdvancedDataGridColumn headerText="완료일시" dataField="reqeddate" width="110" id="greqeddate"/>
			 */
				
			if (etcData.get("reqstep").equals("1")) {        //등록기준
				strQuery.append("SELECT distinct A.CC_ISRID,a.cc_status,TO_CHAR(A.CC_CREATDT,'YYYY/MM/DD') REQDATE, \n");
				strQuery.append("       C.CM_DEPTNAME reqdept,D.CM_USERNAME requser,A.CC_ISRTITLE,      \n");
				strQuery.append("       decode(ISRSTA_TOTAL_SUB(a.cc_isrid),'23',decode(TESTREQ_TOTAL_MAIN(a.cc_isrid),'OK','단위테스트종료',f.cm_codename),f.cm_codename) reqsta2,\n");
				strQuery.append("       e.cm_codename reqsta1,a.cc_reqenddt,                            \n");
				strQuery.append("       a.cc_edgbncd,g.cm_codename isrsatis,                            \n");
				strQuery.append("       to_char(a.cc_edcomdt,'yyyy/mm/dd') reqeddate,                   \n");
				strQuery.append("       ISRSTA_MAIN(a.cc_isrid,'XX',?) maintab,                         \n");
				strQuery.append("       ISRSTA_TOTAL_MAIN(a.cc_isrid) mainstatus,                       \n"); 
				strQuery.append("       ISRSTA_TOTAL_SUB(a.cc_isrid) substatus                          \n"); 
				strQuery.append("  from (select cm_micode,cm_codename from cmm0020 where cm_macode='SATISCD') g, \n");
				strQuery.append("       cmm0020 f,cmm0020 e,cmm0040 d,cmm0100 c,cmc0100 a,              \n");
				strQuery.append("(select distinct a.cc_isrid,b.cc_mainstatus,b.cc_substatus             \n");
			} else if (etcData.get("reqstep").equals("2")) {  //접수기준
				strQuery.append("SELECT A.CC_ISRID,b.cc_isrsub,a.cc_status,TO_CHAR(A.CC_CREATDT,'YYYY/MM/DD') REQDATE, \n");
				strQuery.append("       C.CM_DEPTNAME reqdept,D.CM_USERNAME requser,A.CC_ISRTITLE,     \n");
				strQuery.append("       decode(b.cc_substatus,'23',decode(TESTREQ_TOTAL(b.cc_isrid,b.cc_isrsub),'OK','단위테스트종료',f.cm_codename),f.cm_codename) reqsta2,\n");
				strQuery.append("       e.cm_codename reqsta1,a.cc_reqenddt,                           \n");
				strQuery.append("       a.cc_edgbncd,g.cm_codename isrsatis,b.cc_substatus,            \n");
				strQuery.append("       to_char(a.cc_edcomdt,'yyyy/mm/dd') reqeddate,                  \n");
				strQuery.append("       to_char(b.cc_creatdt,'yyyy/mm/dd') recvdate,                   \n");
				strQuery.append("       h.cm_deptname recvpart,i.cm_username recvuser,b.cc_expday,     \n");
				strQuery.append("       j.cm_username recvsubuser,b.cc_recvgbn,b.cc_expruntime,        \n");
				strQuery.append("       b.cc_endgbn,to_char(b.cc_eddate,'yyyy/mm/dd') isreddate,       \n");
				strQuery.append("       ISRSTA_MAIN(b.cc_isrid,b.cc_isrsub,?) maintab,                 \n"); 
				strQuery.append("       b.cc_mainstatus mainstatus,b.cc_substatus substatus            \n"); 
				strQuery.append("  from (select cm_micode,cm_codename from cmm0020 where cm_macode='SATISCD') g, \n");
				strQuery.append("       cmm0040 j,cmm0040 i,cmm0100 h,cmm0020 f,                       \n");
				strQuery.append("       cmm0020 e,cmm0040 d,cmm0100 c,cmc0110 b,cmc0100 a,             \n");
				strQuery.append("(select a.cc_isrid,b.cc_isrsub                                        \n");				
			} else if (etcData.get("reqstep").equals("3")) {  //변경기준
				strQuery.append("SELECT A.CC_ISRID,b.cc_isrsub,a.cc_status,TO_CHAR(A.CC_CREATDT,'YYYY/MM/DD') REQDATE, \n");
				strQuery.append("       C.CM_DEPTNAME reqdept,D.CM_USERNAME requser,A.CC_ISRTITLE,     \n");
				strQuery.append("       decode(b.cc_substatus,'23',decode(TESTREQ_TOTAL(b.cc_isrid,b.cc_isrsub),'OK','단위테스트종료',f.cm_codename),f.cm_codename) reqsta2,\n");
				strQuery.append("       e.cm_codename reqsta1,a.cc_reqenddt,     \n");
				strQuery.append("       a.cc_edgbncd ,g.cm_codename isrsatis,b.cc_substatus,           \n");
				strQuery.append("       to_char(a.cc_edcomdt,'yyyy/mm/dd') reqeddate,                  \n");
				strQuery.append("       to_char(b.cc_creatdt,'yyyy/mm/dd') recvdate,                   \n");
				strQuery.append("       h.cm_deptname recvpart,i.cm_username recvuser,b.cc_expday,     \n");
				strQuery.append("       j.cm_username recvsubuser,b.cc_recvgbn,b.cc_expruntime,        \n");
				strQuery.append("       b.cc_endgbn,to_char(b.cc_eddate,'yyyy/mm/dd') isreddate,       \n");
				strQuery.append("       to_char(n.cc_creatdt,'yyyy/mm/dd') chgdate,                    \n");
				strQuery.append("       l.cm_username chguser,m.cm_username chgsubuser,                \n");
				strQuery.append("       n.cc_edgbncd chgedgbn,n.cc_scmuser,                            \n");
				strQuery.append("       to_char(n.cc_eddate,'yyyy/mm/dd hh24:mi') chgeddate,           \n");
				strQuery.append("       ISRSTA_MAIN(b.cc_isrid,b.cc_isrsub,?) maintab,                 \n"); 
				strQuery.append("       b.cc_mainstatus mainstatus,b.cc_substatus substatus            \n"); 
				strQuery.append("  from (select cm_micode,cm_codename from cmm0020 where cm_macode='SATISCD') g, \n");
				strQuery.append("       cmm0040 l,cmm0040 m,cmc0210 n,                                 \n");
				strQuery.append("       cmm0040 j,cmm0040 i,cmm0100 h,cmm0020 f,                       \n");
				strQuery.append("       cmm0020 e,cmm0040 d,cmm0100 c,cmc0110 b,cmc0100 a,             \n");
				strQuery.append("(select a.cc_isrid,b.cc_isrsub                                        \n");				
			}
			strQuery.append("  from cmc0110 b,cmc0100 a                                            \n");
			
			if (etcData.get("reqsta1") == null || etcData.get("reqsta1") == "") {
				if (etcData.get("daygbn").equals("0")) {
					strQuery.append("where a.cc_creatdt is not null                                \n");					
				} else if (etcData.get("daygbn").equals("1")) {
					strQuery.append("where a.cc_creatdt is not null                                \n");					
				} else {
					strQuery.append("where b.cc_rfcdate is not null                                \n");
				}
			} else if (etcData.get("daygbn").equals("0") && etcData.get("stday") != null && etcData.get("stday") != "" 
				&& etcData.get("edday") != null && etcData.get("edday") != "") {
				strQuery.append("where to_char(a.cc_creatdt,'yyyymmdd')>=?                         \n");
				strQuery.append("  and to_char(a.cc_creatdt,'yyyymmdd')<=?                         \n");
			} else if (etcData.get("daygbn").equals("1") && etcData.get("stday") != null && etcData.get("stday") != "" 
				&& etcData.get("edday") != null && etcData.get("edday") != "") {
				strQuery.append("where to_char(b.cc_creatdt,'yyyymmdd')>=?                         \n");
				strQuery.append("  and to_char(b.cc_creatdt,'yyyymmdd')<=?                         \n");
			} else if (etcData.get("stday") != null && etcData.get("stday") != "" 
				&& etcData.get("edday") != null && etcData.get("edday") != "") {
				strQuery.append("where b.cc_rfcdate is not null                                    \n");
				strQuery.append("  and to_char(b.cc_rfcdate,'yyyymmdd')>=?                         \n");
				strQuery.append("  and to_char(b.cc_rfcdate,'yyyymmdd')<=?                         \n");
			} else  {
				if (etcData.get("daygbn").equals("0")) {
					strQuery.append("where a.cc_creatdt is not null                                \n");					
				} else if (etcData.get("daygbn").equals("1")) {
					strQuery.append("where a.cc_creatdt is not null                                \n");					
				} else {
					strQuery.append("where b.cc_rfcdate is not null                                \n");
				}
			}
			if (etcData.get("reqdept") != null && etcData.get("reqdept") != "") {
				strQuery.append("and a.cc_reqdept in (select cm_deptcd from (select * from cmm0100 where cm_useyn='Y')  \n");
				strQuery.append("                      start with cm_updeptcd=?                   \n");
				strQuery.append("                      connect by prior cm_deptcd=cm_updeptcd     \n");
				strQuery.append("                      union     \n");
				strQuery.append("                      select ? from dual)     \n");
			} else if (etcData.get("recvdept") != null && etcData.get("recvdept") != "") {
				strQuery.append("and b.cc_recvpart in (select cm_deptcd from (select * from cmm0100 where cm_useyn='Y')  \n");
				strQuery.append("                       start with cm_updeptcd=?                  \n");
				strQuery.append("                       connect by prior cm_deptcd=cm_updeptcd   \n");
				strQuery.append("                      union     \n");
				strQuery.append("                      select ? from dual)     \n");
			} 
			if (etcData.get("reqsta1") != null && etcData.get("reqsta1") != "") {
				if (etcData.get("reqsta1").equals("XX")) {
					strQuery.append("and b.cc_substatus not in ('13','1B')                         \n");
				} else if (!etcData.get("reqsta1").equals("00")) {
					strQuery.append("and b.cc_mainstatus=?                                         \n");
				}
			} else {
				strQuery.append("and (b.cc_substatus not in ('13','1B') or                          \n");
				strQuery.append("     (b.cc_substatus in ('13','1B')                                \n");
				if (etcData.get("daygbn").equals("0") && etcData.get("stday") != null && etcData.get("stday") != "" 
					&& etcData.get("edday") != null && etcData.get("edday") != "") {
					strQuery.append("  and to_char(a.cc_creatdt,'yyyymmdd')>=?                      \n");
					strQuery.append("  and to_char(a.cc_creatdt,'yyyymmdd')<=?))                    \n");
				} else if (etcData.get("daygbn").equals("1") && etcData.get("stday") != null && etcData.get("stday") != "" 
					&& etcData.get("edday") != null && etcData.get("edday") != "") {
					strQuery.append("  and to_char(b.cc_creatdt,'yyyymmdd')>=?                      \n");
					strQuery.append("  and to_char(b.cc_creatdt,'yyyymmdd')<=?))                    \n");
				} else if (etcData.get("stday") != null && etcData.get("stday") != "" 
					&& etcData.get("edday") != null && etcData.get("edday") != "") {
					strQuery.append("  and b.cc_rfcdate is not null                                    \n");
					strQuery.append("  and to_char(b.cc_rfcdate,'yyyymmdd')>=?                         \n");
					strQuery.append("  and to_char(b.cc_rfcdate,'yyyymmdd')<=?))                       \n");
				} else strQuery.append(") \n");
			}
			if (etcData.get("reqsta2") != null && etcData.get("reqsta2") != "") {
				strQuery.append("and b.cc_substatus=?                                              \n");
			}
			if (etcData.get("reqtit") != null && etcData.get("reqtit") != "") {
				strQuery.append("and a.cc_isrtitle like ?                                          \n");
			}
			if (etcData.get("selfsw").equals("Y")) {
				strQuery.append("and ISRSELF_CHK(b.cc_isrid,b.cc_isrsub,?)='OK'                    \n");
			}
			strQuery.append("and b.cc_isrid=a.cc_isrid) xx  \n");
			strQuery.append(" where xx.cc_isrid=a.cc_isrid                                         \n");
			strQuery.append("   and a.cc_reqdept=c.cm_deptcd                                       \n");
			strQuery.append("   and a.cc_editor=d.cm_userid                                        \n");
			if (etcData.get("reqstep").equals("1")) {
				strQuery.append("   and e.cm_macode='ISRSTAMAIN'                                   \n");
				strQuery.append("   and e.cm_micode=ISRSTA_TOTAL_MAIN(a.cc_isrid)                  \n");
				strQuery.append("   and f.cm_macode='ISRSTASUB'                                    \n");
				strQuery.append("   and f.cm_micode=ISRSTA_TOTAL_SUB(a.cc_isrid)                   \n");
			} else {
				strQuery.append("   and a.cc_isrid=b.cc_isrid and xx.cc_isrsub=b.cc_isrsub         \n");
				strQuery.append("   and e.cm_macode='ISRSTAMAIN' and e.cm_micode=b.cc_mainstatus   \n");
				strQuery.append("   and f.cm_macode='ISRSTASUB' and f.cm_micode=b.cc_substatus     \n");
			}
			strQuery.append("   and a.cc_satiscd=g.cm_micode(+)                                    \n");
			if (!etcData.get("reqstep").equals("1")) {  //요청기준이 아니면
				strQuery.append("and b.cc_recvpart=h.cm_deptcd                                  \n");
				strQuery.append("and b.cc_recvuser=i.cm_userid(+)                               \n");
				strQuery.append("and b.cc_handler=j.cm_userid(+)                                \n");
			} 
			if (etcData.get("reqstep").equals("3")) {  //변경기준
				strQuery.append("and b.cc_chguser=l.cm_userid                                   \n");
				strQuery.append("and b.cc_isrid=n.cc_isrid and b.cc_isrsub=n.cc_isrsub          \n");
				strQuery.append("and n.cc_scmuser=m.cm_userid                                   \n");
			} 
			
			pstmt = conn.prepareStatement(strQuery.toString());
			pstmt = new LoggableStatement(conn,strQuery.toString());
			cnt = 0;
			pstmt.setString(++cnt, etcData.get("userid"));
			if (etcData.get("reqsta1") != null && etcData.get("reqsta1") != "") {			
				if (etcData.get("stday") != null && etcData.get("stday") != "" 
					&& etcData.get("edday") != null && etcData.get("edday") != "") {
					pstmt.setString(++cnt, etcData.get("stday"));
					pstmt.setString(++cnt, etcData.get("edday"));
				}
			}
			if (etcData.get("reqdept") != null && etcData.get("reqdept") != "") {
				pstmt.setString(++cnt,etcData.get("reqdept"));         
				pstmt.setString(++cnt,etcData.get("reqdept"));                                             
			} else if (etcData.get("recvdept") != null && etcData.get("recvdept") != "") {
				pstmt.setString(++cnt,etcData.get("recvdept")); 
				pstmt.setString(++cnt,etcData.get("recvdept")); 
			} 
			if (etcData.get("reqsta1") != null && etcData.get("reqsta1") != "" && !etcData.get("reqsta1").equals("XX") && !etcData.get("reqsta1").equals("00")) {
				pstmt.setString(++cnt,etcData.get("reqsta1")); 
			}
			if (etcData.get("reqsta1") == null || etcData.get("reqsta1") == "") {
				if (etcData.get("stday") != null && etcData.get("stday") != "" 
					&& etcData.get("edday") != null && etcData.get("edday") != "") {
					pstmt.setString(++cnt, etcData.get("stday"));
					pstmt.setString(++cnt, etcData.get("edday"));
				}
			}
			if (etcData.get("reqsta2") != null && etcData.get("reqsta2") != "") {
				pstmt.setString(++cnt,etcData.get("reqsta2")); 
			}
			if (etcData.get("reqtit") != null && etcData.get("reqtit") != "") {
				pstmt.setString(++cnt,"%"+etcData.get("reqtit")+"%"); 
			}		
			if (etcData.get("selfsw").equals("Y")) {
				pstmt.setString(++cnt,etcData.get("userid")); 
			}
			ecamsLogger.error(((LoggableStatement)pstmt).getQueryString());        
			rs = pstmt.executeQuery();
			
			while (rs.next()){
				rst = new HashMap<String, String>();
	        	rst.put("isrid",   rs.getString("cc_isrid"));      
	        	//rst.put("cc_mainstatus",   rs.getString("cc_mainstatus"));      
	        	//rst.put("cc_substatus",   rs.getString("cc_substatus"));
	        	rst.put("mainstatus",   rs.getString("mainstatus"));
	        	rst.put("substatus",   rs.getString("substatus"));
	        	rst.put("reqdate",   rs.getString("reqdate"));      
	        	rst.put("reqdept",   rs.getString("reqdept"));      
	        	rst.put("requser",   rs.getString("requser"));      
	        	rst.put("reqtitle",   rs.getString("cc_isrtitle"));       
	        	rst.put("maintab",   rs.getString("maintab"));  
	        	rst.put("reqedday",   rs.getString("cc_reqenddt").substring(0,4)+"/"+rs.getString("cc_reqenddt").substring(4,6)+"/"+rs.getString("cc_reqenddt").substring(6));
	        	if (rs.getString("substatus").equals("1B")) {   //요청자종료
	        		rst.put("color", "9");
	        	} else if (rs.getString("substatus").equals("13") ||   //이관
	        		rs.getString("substatus").equals("16") ||   //RFC발행반려
	        		rs.getString("substatus").equals("22") ||   //RFC접수반려
	        		rs.getString("substatus").equals("35") ||   //테스트종료반려
	        		rs.getString("substatus").equals("36") ||   //테스트종료비정상
	        		rs.getString("substatus").equals("2A") ||   //변경종료반려
	        		rs.getString("substatus").equals("1A")) {   //ISR종료반려
	        		rst.put("color", "3");
	        	} else {
	        		if (rs.getString("substatus").substring(0,1).equals("3")) {
	        			rst.put("color", "T");
	        		} else if (rs.getString("substatus").substring(0,1).equals("2") ||
	        				   rs.getString("substatus").substring(0,1).equals("4")) {
	        			rst.put("color", "C");	        			
	        		} else rst.put("color", "R");
	        	}
	        	
	        	if (rs.getString("reqeddate") != null) {
	        		if (rs.getString("cc_edgbncd").equals("1")) {
	        			rst.put("endgbn", "접수불가");
	        		} else if (rs.getString("cc_edgbncd").equals("8")) {
	        			rst.put("endgbn", "진행중단");
	        		} else {
	        			rst.put("endgbn", "정상종료");
	        		}
	        		rst.put("reqeddate", rs.getString("reqeddate"));
	        		rst.put("isrsatis", rs.getString("isrsatis"));
	        	}
	        	if (etcData.get("reqstep").equals("1")) {
	        		rst.put("reqsta2", rs.getString("reqsta2"));
	        	} else  {
	        		rst.put("isrsub",   rs.getString("cc_isrsub"));      
	        		rst.put("reqsta1", rs.getString("reqsta1"));
	        		rst.put("reqsta2", rs.getString("reqsta2"));
	        		rst.put("recvdept", rs.getString("recvpart"));
	        		rst.put("recvdate", rs.getString("recvdate"));
	        		rst.put("recvuser", rs.getString("recvuser"));
	        		rst.put("recvsubuser", rs.getString("recvsubuser"));
	        		if (rs.getString("cc_expday") != null && rs.getString("cc_expday") != "") {
	        			rst.put("chgedday", rs.getString("cc_expday").substring(0,4)+"/"+
	        					            rs.getString("cc_expday").substring(4,6)+"/"+
	        					            rs.getString("cc_expday").substring(6));
	        		}
	        		if (rs.getString("cc_recvgbn").equals("0")) {
	        			rst.put("recvgbn", "미접수");
	        		} else if (rs.getString("cc_recvgbn").equals("3"))  {
	        			rst.put("recvgbn", "이관");
	        		} else if (rs.getString("cc_recvgbn").equals("9"))  {
	        			rst.put("recvgbn", "접수");
	        		} 
	        		rst.put("recvuser", rs.getString("recvuser"));
	        		rst.put("recvsubuser", rs.getString("recvsubuser"));
	        		
	        		if (rs.getString("isreddate") != null && rs.getString("isreddate") != "") {
	        			if (rs.getString("cc_endgbn") != null && rs.getString("cc_endgbn") != "") {
			        		if (rs.getString("cc_endgbn").equals("3")) {
			        			rst.put("isredgbn", "접수불가");
			        		} else if (rs.getString("cc_endgbn").equals("5"))  {
			        			rst.put("isredgbn", "개발중단");
			        		} else if (rs.getString("cc_endgbn").equals("9"))  {
			        			rst.put("isredgbn", "정상종료");
			        		} 
	        			}
		        		rst.put("isreddate", rs.getString("isreddate"));
	        		}
	        		
	        		rst.put("chgpercent", "0%");
	        		if (rs.getInt("cc_expruntime") > 0) {
		        		strQuery.setLength(0);
		        		strQuery.append("select sum(cc_jobtime) sumtime from cmc0211  \n");
		        		strQuery.append(" where cc_isrid=? and cc_isrsub=?            \n");
		        		if (etcData.get("reqstep").equals("3")) {
			        		strQuery.append("and cc_scmuser=?            \n");
		        		}
		        		pstmt2 = conn.prepareStatement(strQuery.toString());
		        		//pstmt2 = new LoggableStatement(conn,strQuery.toString());
		        		pstmt2.setString(1, rs.getString("cc_isrid"));
		        		pstmt2.setString(2, rs.getString("cc_isrsub"));
		        		if (etcData.get("reqstep").equals("3")) pstmt2.setString(3, rs.getString("cc_scmuser"));
		        		//ecamsLogger.error(((LoggableStatement)pstmt2).getQueryString());        
		        		rs2 = pstmt2.executeQuery();
		        		if (rs2.next()) {
		        			if (rs2.getInt("sumtime") > 0) {
		        				//ratio=ratio.format("%.3f%%", (double)totalCnt / (double)totalMemberCnt * 100);
		        				svPer = svPer.format("%.2f%%", rs2.getDouble("sumtime") / rs.getDouble("cc_expruntime") * 100);
		        				rst.put("chgpercent", svPer);
		        			}
		        		}
		        		rs2.close();
		        		pstmt2.close();
	        		}
	        		if (etcData.get("reqstep").equals("3")) {   //변경기준
	        			rst.put("chgdate", rs.getString("chgdate"));
		        		rst.put("chguser", rs.getString("chguser"));
		        		rst.put("chgsubuser", rs.getString("chgsubuser"));
		        		
		        		if (rs.getString("chgedgbn") != null && rs.getString("chgedgbn") != "") {
		        			if (rs.getString("chgedgbn").equals("3")) {
			        			rst.put("chgedgbn", "개발중단");
			        		} else if (rs.getString("chgedgbn").equals("5"))  {
			        			rst.put("chgedgbn", "정상종료");
			        		} 
			        		rst.put("chgeddate", rs.getString("chgeddate"));
		        		}
	        		}
	        		if (etcData.get("reqstep").equals("2") || etcData.get("reqstep").equals("3")) {
	        			if (rs.getString("cc_substatus").equals("14") || rs.getString("cc_substatus").equals("18")) {
		        			if (rs.getString("cc_substatus").equals("14")) {  //RFC요청
		        				strQuery.setLength(0);
		        				strQuery.append("select a.cr_confname from cmr9900 a,cmc0110 b \n");
		        				strQuery.append(" where b.cc_isrid=? and b.cc_isrsub=?         \n");
		        				strQuery.append("   and b.cc_rfcacpt=a.cr_acptno               \n");
		        				strQuery.append("   and a.cr_locat='00'                        \n");	        				
		        			} else if (rs.getString("cc_substatus").equals("18")) {  //ISR종료
		        				strQuery.setLength(0);
		        				strQuery.append("select a.cr_confname from cmr9900 a,cmc0110 b \n");
		        				strQuery.append(" where b.cc_isrid=? and b.cc_isrsub=?         \n");
		        				strQuery.append("   and b.cc_endacpt=a.cr_acptno               \n");
		        				strQuery.append("   and a.cr_locat='00'                        \n");	        			
		        			} 
		        			pstmt2 = conn.prepareStatement(strQuery.toString());
		        			//pstmt2 = new LoggableStatement(conn,strQuery.toString());
		        			pstmt2.setString(1, rs.getString("cc_isrid"));
		        			pstmt2.setString(2, rs.getString("cc_isrsub"));
		        			//ecamsLogger.error(((LoggableStatement)pstmt2).getQueryString());      
		        			rs2 = pstmt2.executeQuery();
		        			if (rs2.next()) {
		        				rst.put("reqsta2",rs.getString("reqsta2")+"["+rs2.getString("cr_confname")+"]");
		        			}
		        			rs2.close();
		        			pstmt2.close();
	        			}
	        			
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
			conn = null;
			
			returnObject = rtList.toArray();
			rtList.clear();
			rtList = null;
			
			return returnObject;			
			
		} catch (SQLException sqlexception) {
			sqlexception.printStackTrace();
			ecamsLogger.error("## Cmc1100.SelectList() SQLException START ##");
			ecamsLogger.error("## Error DESC : ", sqlexception);	
			ecamsLogger.error("## Cmc1100.SelectList() SQLException END ##");			
			throw sqlexception;
		} catch (Exception exception) {
			exception.printStackTrace();
			ecamsLogger.error("## Cmc1100.SelectList() Exception START ##");				
			ecamsLogger.error("## Error DESC : ", exception);	
			ecamsLogger.error("## Cmc1100.SelectList() Exception END ##");				
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
					ecamsLogger.error("## Cmc1100.SelectList() connection release exception ##");
					ex3.printStackTrace();
				}
			}
		}
	}//end of SelectList() method statement

    public Object[] get_SelectList_scm(HashMap<String,String> etcData) throws SQLException, Exception {
		Connection        conn        = null;
		PreparedStatement pstmt       = null;
		ResultSet         rs          = null;
		PreparedStatement pstmt2      = null;
		ResultSet         rs2         = null;
		StringBuffer      strQuery    = new StringBuffer();
		
		Object[] 		  returnObject= null;
		ArrayList<HashMap<String, String>>  rtList	= new ArrayList<HashMap<String, String>>();
		HashMap<String, String>			  	rst	 	= null;	
		
		ConnectionContext connectionContext = new ConnectionResource();
		
		try {
			
			rtList.clear();
			Integer           cnt         = 0;
			String			  ColorSw     = "";
			String            svPer       = "";
			String[]          strStep     = null;
			int               i           = 0;
			
			conn = connectionContext.getConnection();
			
			/*
			<mx:AdvancedDataGridColumn headerText="ISR ID" dataField="isrid" minWidth="100" width="100" textAlign="center"/>
			<mx:AdvancedDataGridColumn headerText="SUB" dataField="isrsub" minWidth="40" width="40" textAlign="center" id="gisrsub"/>
			<mx:AdvancedDataGridColumn headerText="등록일" dataField="reqdate" minWidth="80" width="80" textAlign="center"/>
			<mx:AdvancedDataGridColumn headerText="요청제목" dataField="reqtitle" minWidth="150" width="{gridLst.width*0.15}"/>
			<mx:AdvancedDataGridColumn headerText="접수파트" dataField="recvdept" minWidth="100" width="80" id="grecvdept"/>
			<mx:AdvancedDataGridColumn headerText="변경담당자" dataField="chgsubuser" minWidth="100" width="90" id="gchgsubuser" textAlign="center" />
			<mx:AdvancedDataGridColumn headerText="진행단계" dataField="reqsta1" minWidth="80" width="80" id="greqsta1" textAlign="center"/>
			<mx:AdvancedDataGridColumn headerText="진행상황" dataField="reqsta2" minWidth="120" width="{gridLst.width*0.1}" id="greqsta2"/>
			<mx:AdvancedDataGridColumn headerText="개발계획서작성" dataField="devyn" minWidth="80" width="80" id="gchgedday" textAlign="center" />
			<mx:AdvancedDataGridColumn headerText="진행율" dataField="chgpercent" minWidth="80" width="80" id="gchgpercent" textAlign="center"/>
			<mx:AdvancedDataGridColumn headerText="단위테스트" dataField="unityn" minWidth="120" width="{gridLst.width*0.1}" id="gchgedgbn" textAlign="center"/>
			<mx:AdvancedDataGridColumn headerText="통합테스트" dataField="sysyn" minWidth="80" width="80" id="gchgeddate" textAlign="center" />
			<mx:AdvancedDataGridColumn headerText="프로그램개발" dataField="devprog" minWidth="100" width="{gridLst.width*0.1}" id="gisredgbn" textAlign="center"/>
			<mx:AdvancedDataGridColumn headerText="테스트적용" dataField="testyn" minWidth="80" width="80" textAlign="center" id="gisreddate"/>
			<mx:AdvancedDataGridColumn headerText="운영적용" dataField="realyn" minWidth="80" width="80" id="gendgbn" textAlign="center"/>
			<mx:AdvancedDataGridColumn headerText="변경종료" dataField="endyn" minWidth="80" width="80" id="gendgbn" textAlign="center"/>
			 */
				
			
			strQuery.append("SELECT A.CC_ISRID,b.cc_isrsub,TO_CHAR(A.CC_CREATDT,'YYYY/MM/DD') REQDATE, \n");
			strQuery.append("       A.CC_ISRTITLE,e.cm_codename reqsta1,b.cc_substatus substatus,  \n");
			strQuery.append("       decode(b.cc_substatus,'23',decode(TESTREQ_TOTAL(b.cc_isrid,b.cc_isrsub),'OK','단위테스트종료',f.cm_codename),f.cm_codename) reqsta2,\n");
			strQuery.append("       h.cm_deptname recvpart,m.cm_username chgsubuser,               \n");
			strQuery.append("       n.cc_scmuser,n.cc_status scmsta,n.cc_devtime,                  \n");
			strQuery.append("       to_char(n.cc_eddate,'yyyy/mm/dd hh24:mi') chgeddate,           \n");
			strQuery.append("       ISRSTA_MAIN(b.cc_isrid,b.cc_isrsub,?) maintab,                 \n"); 
			strQuery.append("       b.cc_mainstatus mainstatus,n.cc_devstday,n.cc_edgbncd chgedgbn,\n"); 
			strQuery.append("       ISRSCMUSER_STEP_CHK(n.cc_scmuser,n.cc_isrid,n.cc_isrsub,nvl(n.cc_status,'0'),b.cc_substatus,b.cc_detcate) scmstep \n");
			strQuery.append("  from cmm0040 m,cmc0210 n,cmm0100 h,cmm0020 f,                       \n");
			strQuery.append("       cmm0020 e,cmc0110 b,cmc0100 a                                  \n");
			strQuery.append(" where b.cc_substatus not in ('11','12','13','14','15','16')          \n");
			strQuery.append("   and  b.cc_rfcdate is not null and n.cc_status<>'C'                 \n");
			if (etcData.get("isrid") != null && etcData.get("stday") != "") {
				if (etcData.get("isrid").length()>12) {
					strQuery.append("  and b.cc_isrid=? and b.cc_isrsub=?                         \n");
				} else if (etcData.get("isrid").length()==12) {
					strQuery.append("  and b.cc_isrid=?                                           \n");
				} else {
					strQuery.append("  and b.cc_isrid like ?                                      \n");
				}
			} else if (etcData.get("stday") != null && etcData.get("stday") != "" 
				&& etcData.get("edday") != null && etcData.get("edday") != "") {
				strQuery.append("  and to_char(a.cc_creatdt,'yyyymmdd')>=?                         \n");
				strQuery.append("  and to_char(a.cc_creatdt,'yyyymmdd')<=?                         \n");
			} 
			if (etcData.get("reqdept") != null && etcData.get("reqdept") != "") {
				strQuery.append("and a.cc_reqdept=?                                                \n");
			} else if (etcData.get("recvdept") != null && etcData.get("recvdept") != "") {
				strQuery.append("and b.cc_recvpart=?                                               \n");
			} 
			if (etcData.get("reqsta1") != null && etcData.get("reqsta1") != "") {
				if (etcData.get("reqsta1").equals("XX")) {
					strQuery.append("and b.cc_substatus not in ('13','1B')                         \n");
				} else if (!etcData.get("reqsta1").equals("00")) {
					strQuery.append("and b.cc_mainstatus=?                                         \n");
				}
			} else {
				strQuery.append("and (b.cc_substatus not in ('13','1B') or                          \n");
				strQuery.append("     (b.cc_substatus in ('13','1B')                                \n");
				if (etcData.get("stday") != null && etcData.get("stday") != "" 
					&& etcData.get("edday") != null && etcData.get("edday") != "") {
					strQuery.append("  and to_char(a.cc_creatdt,'yyyymmdd')>=?                      \n");
					strQuery.append("  and to_char(a.cc_creatdt,'yyyymmdd')<=?))                    \n");
				} else if (etcData.get("stday") != null && etcData.get("stday") != "" 
					&& etcData.get("edday") != null && etcData.get("edday") != "") {
					strQuery.append("  and to_char(b.cc_creatdt,'yyyymmdd')>=?                      \n");
					strQuery.append("  and to_char(b.cc_creatdt,'yyyymmdd')<=?))                    \n");
				} else if (etcData.get("stday") != null && etcData.get("stday") != "" 
					&& etcData.get("edday") != null && etcData.get("edday") != "") {
					strQuery.append("  and b.cc_rfcdate is not null                                 \n");
					strQuery.append("  and to_char(b.cc_rfcdate,'yyyymmdd')>=?                      \n");
					strQuery.append("  and to_char(b.cc_rfcdate,'yyyymmdd')<=?))                    \n");
				} else strQuery.append(") \n");
			}
			if (etcData.get("reqsta2") != null && etcData.get("reqsta2") != "") {
				strQuery.append("and b.cc_substatus=?                                               \n");
			}
			if (etcData.get("reqtit") != null && etcData.get("reqtit") != "") {
				strQuery.append("and a.cc_isrtitle like ?                                           \n");
			}
			if (etcData.get("selfsw").equals("Y")) {
				strQuery.append("and ISRSELF_CHK(b.cc_isrid,b.cc_isrsub,?)='OK'                     \n");
			}
			if (etcData.get("scmuser") != null && etcData.get("scmuser") != "") {
				strQuery.append("and m.cm_username=?                                                \n");
			}
			strQuery.append("   and b.cc_isrid=a.cc_isrid and b.cc_recvpart=h.cm_deptcd             \n");
			strQuery.append("   and b.cc_isrid=n.cc_isrid and b.cc_isrsub=n.cc_isrsub               \n");
			strQuery.append("   and e.cm_macode='ISRSTAMAIN' and e.cm_micode=b.cc_mainstatus        \n");
			strQuery.append("   and f.cm_macode='ISRSTASUB' and f.cm_micode=b.cc_substatus          \n");
			strQuery.append("   and b.cc_isrid=n.cc_isrid and b.cc_isrsub=n.cc_isrsub               \n");
			strQuery.append("   and n.cc_scmuser=m.cm_userid                                        \n");
			
			pstmt = conn.prepareStatement(strQuery.toString());
			pstmt = new LoggableStatement(conn,strQuery.toString());
			cnt = 0;
			pstmt.setString(++cnt, etcData.get("userid"));
			if (etcData.get("isrid") != null && etcData.get("stday") != "") {
				if (etcData.get("isrid").length()>12) {
					pstmt.setString(++cnt, etcData.get("isrid").substring(0,12));
					pstmt.setString(++cnt, etcData.get("isrid").substring(13));
				} else if (etcData.get("isrid").length()==12) {
					pstmt.setString(++cnt, etcData.get("isrid"));
				} else {
					pstmt.setString(++cnt, etcData.get("isrid")+"%");
				}
			} else if (etcData.get("stday") != null && etcData.get("stday") != "" 
				&& etcData.get("edday") != null && etcData.get("edday") != "") {
				pstmt.setString(++cnt, etcData.get("stday"));
				pstmt.setString(++cnt, etcData.get("edday"));
			}
			if (etcData.get("reqdept") != null && etcData.get("reqdept") != "") {
				pstmt.setString(++cnt,etcData.get("reqdept"));                                   
			} else if (etcData.get("recvdept") != null && etcData.get("recvdept") != "") {
				pstmt.setString(++cnt,etcData.get("recvdept")); 
			} 
			if (etcData.get("reqsta1") != null && etcData.get("reqsta1") != "" && !etcData.get("reqsta1").equals("XX") && !etcData.get("reqsta1").equals("00")) {
				pstmt.setString(++cnt,etcData.get("reqsta1")); 
			}
			if (etcData.get("reqsta1") == null || etcData.get("reqsta1") == "") {
				if (etcData.get("stday") != null && etcData.get("stday") != "" 
					&& etcData.get("edday") != null && etcData.get("edday") != "") {
					pstmt.setString(++cnt, etcData.get("stday"));
					pstmt.setString(++cnt, etcData.get("edday"));
				}
			}
			if (etcData.get("reqsta2") != null && etcData.get("reqsta2") != "") {
				pstmt.setString(++cnt,etcData.get("reqsta2")); 
			}
			if (etcData.get("reqtit") != null && etcData.get("reqtit") != "") {
				pstmt.setString(++cnt,"%"+etcData.get("reqtit")+"%"); 
			}		
			if (etcData.get("selfsw").equals("Y")) {
				pstmt.setString(++cnt,etcData.get("userid")); 
			}
			if (etcData.get("scmuser") != null && etcData.get("scmuser") != "") {
				pstmt.setString(++cnt,etcData.get("scmuser")); 
			}
            ecamsLogger.error(((LoggableStatement)pstmt).getQueryString());        
			rs = pstmt.executeQuery();
			while (rs.next()){
				rst = new HashMap<String, String>();
	        	rst.put("isrid",   rs.getString("cc_isrid"));      
	        	//rst.put("cc_mainstatus",   rs.getString("cc_mainstatus"));      
	        	//rst.put("cc_substatus",   rs.getString("cc_substatus"));
	        	rst.put("mainstatus",   rs.getString("mainstatus"));
	        	rst.put("substatus",   rs.getString("substatus"));
	        	rst.put("reqdate",   rs.getString("reqdate"));     
	        	rst.put("reqtitle",   rs.getString("cc_isrtitle"));       
	        	rst.put("maintab",   rs.getString("maintab"));  
	        	if (rs.getString("substatus").equals("1B")) {   //요청자종료
	        		rst.put("color", "9");
	        	} else if (rs.getString("substatus").equals("13") ||   //이관
	        		rs.getString("substatus").equals("16") ||   //RFC발행반려
	        		rs.getString("substatus").equals("22") ||   //RFC접수반려
	        		rs.getString("substatus").equals("35") ||   //테스트종료반려
	        		rs.getString("substatus").equals("36") ||   //테스트종료비정상
	        		rs.getString("substatus").equals("2A") ||   //변경종료반려
	        		rs.getString("substatus").equals("1A")) {   //ISR종료반려
	        		rst.put("color", "3");
	        	} else {
	        		if (rs.getString("substatus").substring(0,1).equals("3")) {
	        			rst.put("color", "T");
	        		} else if (rs.getString("substatus").substring(0,1).equals("2") ||
	        				   rs.getString("substatus").substring(0,1).equals("4")) {
	        			rst.put("color", "C");	        			
	        		} else rst.put("color", "R");
	        	}
        		rst.put("isrsub",   rs.getString("cc_isrsub"));      
        		rst.put("reqsta1", rs.getString("reqsta1"));
        		rst.put("reqsta2", rs.getString("reqsta2"));
        		rst.put("recvdept", rs.getString("recvpart"));
        		rst.put("chgpercent", "0%");
        		if (rs.getInt("cc_devtime") > 0) {
	        		strQuery.setLength(0);
	        		strQuery.append("select sum(cc_jobtime) sumtime from cmc0211  \n");
	        		strQuery.append(" where cc_isrid=? and cc_isrsub=?            \n");
		        	strQuery.append("   and cc_scmuser=?                          \n");
	        		pstmt2 = conn.prepareStatement(strQuery.toString());
	        		//pstmt2 = new LoggableStatement(conn,strQuery.toString());
	        		pstmt2.setString(1, rs.getString("cc_isrid"));
	        		pstmt2.setString(2, rs.getString("cc_isrsub"));
	        		pstmt2.setString(3, rs.getString("cc_scmuser"));
	        		//ecamsLogger.error(((LoggableStatement)pstmt2).getQueryString());        
	        		rs2 = pstmt2.executeQuery();
	        		if (rs2.next()) {
	        			if (rs2.getInt("sumtime") > 0) {
	        				//ratio=ratio.format("%.3f%%", (double)totalCnt / (double)totalMemberCnt * 100);
	        				svPer = svPer.format("%.1f%%", rs2.getDouble("sumtime") / rs.getDouble("cc_devtime") * 100);
	        				rst.put("chgpercent", svPer);
	        			}
	        		}
	        		rs2.close();
	        		pstmt2.close();
	        		rst.put("devyn", rs.getString("cc_devtime"));
        		} else {
        			rst.put("devyn", "미작성");
        		}
	        	rst.put("chgsubuser", rs.getString("chgsubuser"));
	        		        	
	        	if (rs.getString("scmstep") != null) {
	        		strStep = rs.getString("scmstep").split(",");
	        		for (i=0;strStep.length>i;i++) {
	        			if (strStep[i].substring(0,1).equals("1")) {
	        				rst.put("unityn", strStep[i].substring(1));
	        			} else if (strStep[i].substring(0,1).equals("2")) {
	        				rst.put("sysyn", strStep[i].substring(1));
	        			} else if (strStep[i].substring(0,1).equals("3")) {
	        				rst.put("devprog", strStep[i].substring(1));
	        			} else if (strStep[i].substring(0,1).equals("4")) {
	        				rst.put("testyn", strStep[i].substring(1));
	        			} else if (strStep[i].substring(0,1).equals("5")) {
	        				rst.put("realyn", strStep[i].substring(1));
	        			} else if (strStep[i].substring(0,1).equals("6")) {
	        				rst.put("endyn", strStep[i].substring(1));
	        			} 
	        		}
	        	}
        		if (rs.getString("chgedgbn") != null && rs.getString("chgedgbn") != "") {
        			if (rs.getString("chgedgbn").equals("3")) {
	        			rst.put("endyn", rst.get("endyn") + "개발중단");
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
			
			returnObject = rtList.toArray();
			rtList.clear();
			rtList = null;
			
			return returnObject;			
			
		} catch (SQLException sqlexception) {
			sqlexception.printStackTrace();
			ecamsLogger.error("## Cmc1100.get_SelectList_scm() SQLException START ##");
			ecamsLogger.error("## Error DESC : ", sqlexception);	
			ecamsLogger.error("## Cmc1100.get_SelectList_scm() SQLException END ##");			
			throw sqlexception;
		} catch (Exception exception) {
			exception.printStackTrace();
			ecamsLogger.error("## Cmc1100.get_SelectList_scm() Exception START ##");				
			ecamsLogger.error("## Error DESC : ", exception);	
			ecamsLogger.error("## Cmc1100.get_SelectList_scm() Exception END ##");				
			throw exception;
		}finally{
			if (strQuery != null) 	strQuery = null;
			if (returnObject != null)	returnObject = null;
			if (rs != null)     try{rs.close();}catch (Exception ex){ex.printStackTrace();}
			if (pstmt != null)  try{pstmt.close();}catch (Exception ex2){ex2.printStackTrace();}
			if (rs2 != null)     try{rs2.close();}catch (Exception ex){ex.printStackTrace();}
			if (pstmt2 != null)  try{pstmt2.close();}catch (Exception ex2){ex2.printStackTrace();}
			if (conn != null){
				try{
					conn.close();
				}catch(Exception ex3){
					ecamsLogger.error("## Cmc1100.get_SelectList_scm() connection release exception ##");
					ex3.printStackTrace();
				}
			}
		}
	}//end of get_SelectList_scm() method statement
    
}//end of Cmr3200 class statement
