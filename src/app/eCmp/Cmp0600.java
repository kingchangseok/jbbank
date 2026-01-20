/*****************************************************************************************
	1. program ID	: eCmr3100.java
	2. create date	: 
	3. auth		    : [결재확인] -> [결재현황]
	4. update date	: 090825
	5. auth		    : no name
	6. description	: eCmr3100.java 결재현황
*****************************************************************************************/

package app.eCmp;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import org.apache.logging.log4j.Logger;
import app.common.LoggableStatement;
import com.ecams.common.dbconn.ConnectionContext;
import com.ecams.common.dbconn.ConnectionResource;
import com.ecams.common.logger.EcamsLogger;

/** 
 * @author bigeyes
 * 	
 * Window - Preferences - Java - Code Style - Code Templates
 */


public class Cmp0600{
    /**
     * Logger Class Instance Creation
     * logger
     */
    Logger ecamsLogger  = EcamsLogger.getLoggerInstance();
    
	/**
	 * 결재리스트 조회합니다.(구분값을 주어)
	 * @param  gbn
	 * @return ArrayList
	 * @throws SQLException
	 * @throws Exception
	 */
    public Object[] get_SelectList(String pSysCd,String pReqCd,String pTeamCd,String pEmgCd,
    								String pReqUser,String pAcptN,String pProgN,String pReal,String pTest,
    								String pStartDt,String pEndDt,String pUserId) 
    																		throws SQLException, Exception {
		Connection        conn        = null;
		PreparedStatement pstmt       = null;
		PreparedStatement pstmt2      = null;
		ResultSet         rs          = null;
		ResultSet         rs2         = null;
		StringBuffer      strQuery    = new StringBuffer();
		ArrayList<HashMap<String, String>>  rtList	= new ArrayList<HashMap<String, String>>();
		HashMap<String, String>			  	rst	 	= null;
        Object[]		 rtObj		  = null;	
        int              Cnt = 0;
		ConnectionContext connectionContext = new ConnectionResource();
		try {            
			conn = connectionContext.getConnection();
			strQuery.setLength(0);
			
			strQuery.append(" select a.cr_syscd,a.cr_acptno,c.cm_sysmsg, a.cr_orderid, e.cm_deptname,f.cm_username,           \n");
			strQuery.append("  a.cr_passcd,a.cr_acptdate,b.cr_status,a.cr_sayu,                                                          \n");
			strQuery.append("  a.cr_prcdate, a.cr_qrycd, g.cm_codename as requestgb ,d.cm_codename as jawon,h.cm_dirpath, b.cr_rsrcname, \n"); 
			strQuery.append("  to_char(a.cr_acptdate,'yyyy/mm/dd') acptdate,                                                             \n");  
			strQuery.append("  to_char(a.cr_prcdate,'yyyy/mm/dd') prcdate                                                                \n");  
			strQuery.append("  from cmm0070 h,cmm0020 g,cmm0040 f,cmm0100 e, cmm0030 c,cmr1010 b, cmr1000 a , cmm0020 d where            \n");
			if(pReal.equals("Y") && pTest.equals("Y")){
				strQuery.append("     a.cr_qrycd in('03','04') \n");
			}else if(pReal.equals("Y")||pTest.equals("N")){
				strQuery.append("     a.cr_qrycd in('04') \n");
			}else if(pReal.equals("N")||pTest.equals("Y")){  
				strQuery.append("     a.cr_qrycd in('03') \n");
			}
			strQuery.append("  and to_char(a.cr_acptdate,'yyyymmdd')>=? \n");                         
			strQuery.append("  and to_char(a.cr_acptdate,'yyyymmdd')<=? \n");                        
			strQuery.append("  and a.cr_acptno = b.cr_acptno \n");
			strQuery.append("  and a.cr_status <> '3' \n");
			if (pSysCd != "" && pSysCd != null)   strQuery.append("   and a.cr_syscd = ? \n");
			if (pReqCd != "" && pReqCd != null)   strQuery.append("   and b.cr_qrycd = ? \n");
			if (pTeamCd != "" && pTeamCd != null)  {
				strQuery.append("   and a.cr_teamcd in (select cm_deptcd from cmm0100 \n");
				strQuery.append("                        where cm_updeptcd=?) \n");
			}
			if(pReqUser != "" && pReqUser != null && !pReqUser.equals("")){
				strQuery.append("   and upper(f.cm_username) like upper(?) \n");
			}
			if(pAcptN != "" && pAcptN != null && !pAcptN.equals("")){
				strQuery.append("   and a.cr_orderid is not null and upper(a.cr_orderid) like upper(?) \n");
			}
			if(pProgN != "" && pProgN != null && !pProgN.equals("")){
				strQuery.append("   and upper(b.cr_rsrcname) like upper(?) \n");
			}
			strQuery.append("   and a.cr_prcdate is not null \n"); 
			strQuery.append("   and a.cr_syscd = c.cm_syscd \n");
			strQuery.append("   and a.cr_editor = f.cm_userid  \n");
			strQuery.append("   and  a.cr_teamcd = e.cm_deptcd \n");
			strQuery.append("   and g.cm_macode = 'CHECKIN'  \n");
			strQuery.append("   and g.cm_micode = b.cr_qrycd \n");
			strQuery.append("   and d.cm_macode = 'JAWON'  \n");
			strQuery.append("   and d.cm_micode = b.cr_rsrccd \n");
			strQuery.append("   and a.cr_syscd = h.cm_syscd \n");
			strQuery.append("   and b.cr_dsncd = h.cm_dsncd \n");
			strQuery.append("   and b.cr_itemid = b.cr_baseitem \n");
		//	strQuery.append("   and b.cr_dsncd=b.cr_basedsn \n");
	    //	strQuery.append("   and b.cr_rsrcname=b.cr_basepgm \n");
			strQuery.append("order by a.cr_syscd \n");
			
			pstmt = conn.prepareStatement(strQuery.toString());
	//		pstmt = new LoggableStatement(conn,strQuery.toString());
            Cnt = 0;
	            pstmt.setString(++Cnt, pStartDt);
	            pstmt.setString(++Cnt, pEndDt);
	        if (pSysCd != "" && pSysCd != null) {
            	pstmt.setString(++Cnt, pSysCd);
            }
			if (pReqCd != "" && pReqCd != null)  {
            	pstmt.setString(++Cnt, pReqCd);
            }
			if (pTeamCd != "" && pTeamCd != null) {
            	pstmt.setString(++Cnt, pTeamCd);
            }
			if (pReqUser != null && pReqUser != "" && !pReqUser.equals("")){
				pstmt.setString(++Cnt, pReqUser);
			}
			if (pAcptN != null && pAcptN != "" && !pAcptN.equals("")){
				pstmt.setString(++Cnt, "%"+pAcptN+"%");
			}
			if (pProgN != "" && pProgN != null && !pProgN.equals("")){
				pstmt.setString(++Cnt, "%"+pProgN+"%");
			}
	//		//ecamsLogger.error(((LoggableStatement)pstmt).getQueryString());
            rs = pstmt.executeQuery();
            
			while (rs.next()){	
				rst = new HashMap<String, String>();
				rst.put("rows", Integer.toString(rs.getRow()));					
				rst.put("cm_sysmsg", rs.getString("cm_sysmsg"));	//시스템 
//        		rst.put("reqcd", rs.getString("reqcd"));            //변경구분
				
        		rst.put("acptno",  rs.getString("cr_acptno").substring(0,4)+"-"+
        				           rs.getString("cr_acptno").substring(4,6)+"-"+
        				           rs.getString("cr_acptno").substring(6));         //요청번호
        		
        		if (rs.getString("cm_deptname").indexOf(" ") > 0) {
        			rst.put("deptname",rs.getString("cm_deptname").substring(rs.getString("cm_deptname").indexOf(" ")+1));    //팀명
        		} else  {
        			rst.put("deptname",rs.getString("cm_deptname"));    //팀명
        		}
        		rst.put("editor",  rs.getString("cm_username")); 	//요청자
        		rst.put("acptdate",rs.getString("acptdate"));		//요청일시
        		rst.put("prcdate",rs.getString("prcdate"));		    //완료일시
        		rst.put("qrycd",  rs.getString("requestgb"));	    //신청구분
        		rst.put("jawon",  rs.getString("jawon"));	        //자원구분
        		rst.put("cm_dirpath",rs.getString("cm_dirpath"));   //디렉토리
        		rst.put("cr_rsrcname",rs.getString("cr_rsrcname")); //프로그램
        		rst.put("cr_sayu",rs.getString("cr_sayu"));         //신청사유
        		if (rs.getString("cr_orderid") != null) {
        			//rst.put("cr_isrid",rs.getString("cr_isrid")+"-"+rs.getString("cr_isrsub"));
        			
        			strQuery.setLength(0);
        			strQuery.append("select CC_REQSUB , CC_SUBID from cmc0420  \n");
        			strQuery.append(" where CC_ORDERID=? \n");
        			pstmt2 = conn.prepareStatement(strQuery.toString());
        //			pstmt2 = new LoggableStatement(conn,strQuery.toString());
        			pstmt2.setString(1, rs.getString("cr_orderid"));
        //			//ecamsLogger.error(((LoggableStatement)pstmt2).getQueryString());
        			rs2 = pstmt2.executeQuery();
        			if (rs2.next()) {
        				//rst.put("chgcd", rs2.getString("cm_codename"));
        				rst.put("cr_orderid",rs.getString("cr_orderid")+"-"+rs.getString("cc_isrsub")+"  "+rs2.getString("CC_CC_SUBID"));
        			}
        			rs2.close();
        			pstmt2.close();
        		} else {
        			rst.put("cr_orderid","");
        		    //rst.put("sayu", rs.getString("cr_passcd"));
        		}
        		rst.put("cr_acptno", rs.getString("cr_acptno"));
        		rtList.add(rst);
        		rst = null;
			}//end of while-loop statement
			
			rs.close();
			pstmt.close();
			conn.close();

			rs = null;
			pstmt = null;
            rs2 = null;
            pstmt2 = null;
			conn = null;
			
			rtObj =  rtList.toArray();
			rtList.clear(); 
			rtList = null;
			
			return rtObj;
			
		} catch (SQLException sqlexception) {
			sqlexception.printStackTrace();
			ecamsLogger.error("## Cmr3100.SelectList() SQLException START ##");
			ecamsLogger.error("## Error DESC : ", sqlexception);	
			ecamsLogger.error("## Cmr3100.SelectList() SQLException END ##");			
			throw sqlexception;
		} catch (Exception exception) {
			exception.printStackTrace();
			ecamsLogger.error("## Cmr3100.SelectList() Exception START ##");				
			ecamsLogger.error("## Error DESC : ", exception);	
			ecamsLogger.error("## Cmr3100.SelectList() Exception END ##");				
			throw exception;
		}finally{
			if (strQuery != null) 	strQuery = null;
			if (rtObj != null)	rtObj = null;
			if (rs != null)     try{rs.close();}catch (Exception ex){ex.printStackTrace();}
			if (rs2 != null)    try{rs2.close();}catch (Exception ex){ex.printStackTrace();}
			if (pstmt != null)  try{pstmt.close();}catch (Exception ex2){ex2.printStackTrace();}
			if (pstmt2 != null)  try{pstmt2.close();}catch (Exception ex2){ex2.printStackTrace();}
			if (conn != null){
				try{
					ConnectionResource.release(conn);
				}catch(Exception ex3){
					ecamsLogger.error("## Cmr3100.SelectList() connection release exception ##");
					ex3.printStackTrace();
				}
			}
		}
	}//end of SelectList() method statement
}//end of Cmr3100 class statement