/*****************************************************************************************
	1. program ID	: eCmr3100.java
	2. create date	: 
	3. auth		    : [결재확인] -> [결재현황]
	4. update date	: 090825
	5. auth		    : no name
	6. description	: eCmr3600.java 결재현황
*****************************************************************************************/
	
package app.eCmr;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import org.apache.logging.log4j.Logger;
import app.common.LoggableStatement;
import app.common.SystemPath;
import app.common.eCAMSInfo;

import com.ecams.common.dbconn.ConnectionContext;
import com.ecams.common.dbconn.ConnectionResource;
import com.ecams.common.logger.EcamsLogger;

/** 
 * @author bigeyes
 * 
 * Window - Preferences - Java - Code Style - Code Templates
 */


public class Cmr3600{
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
    										
    public Object[] get_SelectList(String pReqCd,String pTeamCd,String pStateCd,
    								String pReqUser,String pIsr,String pStartDt,String pEndDt,String pUserId) 
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

		String			  AcptNo      = "";
		String			  ConfName    = null;
		String			  ColorSw     = null;
        Integer           FindFg      = 0;
        Integer           Cnt         = 0;
        String            ConfTeamCd  = "";
        
		ConnectionContext connectionContext = new ConnectionResource();
		
		try {

			conn = connectionContext.getConnection();
		       //01	결재대기
		       //02	결재진행
		       //03	결재반려
		       //04	결재완료
			
			strQuery.setLength(0);
		       strQuery.append(" select g.CC_REQSUB, a.cr_acptno,a.cr_orderid orderid ,     \n");
		       strQuery.append(" a.cr_editor,a.cr_qrycd,a.cr_syscd,a.cr_status,a.cr_sayu,    \n");    
		       strQuery.append(" a.cr_prcdate,b.cr_locat,b.cr_team,b.cr_status sta,          \n");    
		       strQuery.append(" b.cr_confusr,b.cr_congbn,b.cr_confname,c.cm_deptname,          \n"); 
		       strQuery.append(" to_char(b.cr_confdate,'yyyy/mm/dd hh24:mi') confdate,          \n"); 
		       strQuery.append(" to_char(a.cr_acptdate,'yyyy/mm/dd hh24:mi') as acptdate,        \n");
		       strQuery.append(" to_char(a.cr_prcdate,'yyyy/mm/dd hh24:mi') as prcdate,          \n");
		       strQuery.append(" substr(a.cr_acptno,1,4) || '-' || substr(a.cr_acptno,5,2) ||    \n");
		       strQuery.append(" '-' || substr(a.cr_acptno,7,6) as acptno,a.cr_prcreq,           \n");
		       strQuery.append(" d.cm_codename qrycd,e.cm_sysmsg,f.cm_username                \n");   
		       strQuery.append(" from cmc0420 g,cmm0040 f,cmm0030 e,cmm0020 d,cmm0100 c,cmr9900 b,cmr1000 a where \n");
			if (pStateCd.equals("0")) {		//전체		
				strQuery.append("      b.cr_locat<>'00' and ((b.cr_status<>'0'	\n");
				strQuery.append("  and b.cr_confdate is not null and b.cr_confusr is not null       \n");
				strQuery.append("  and to_char(b.cr_confdate,'yyyymmdd')>=?                         \n");
				strQuery.append("  and to_char(b.cr_confdate,'yyyymmdd')<=?                         \n");
				strQuery.append("      and b.cr_team = 'D1'											\n");
				strQuery.append("      and b.cr_team in (select cm_rgtcd from cmm0043               \n");
				strQuery.append("                     where cm_userid=?)))                          \n");
			} else if (pStateCd.equals("01")){//결재대기 
				strQuery.append("      b.cr_status='0'                                              \n");
				strQuery.append("      and b.cr_team = 'D1'											\n");
				strQuery.append("and  (b.cr_teamcd in ('4','5','9') and b.cr_team in                \n");
				strQuery.append("            (select cm_rgtcd from cmm0043 where cm_userid=?))      \n");
				strQuery.append("and b.cr_locat>=(select substr(cr_confusr,1,2) from cmr9900        \n");
				strQuery.append("                  where cr_acptno=b.cr_acptno and cr_locat='00')   \n");
				strQuery.append("and b.cr_locat<=(select substr(cr_confusr,3,2) from cmr9900        \n");
				strQuery.append("                  where cr_acptno=b.cr_acptno and cr_locat='00')   \n");
			} else if (pStateCd.equals("02")){  
				strQuery.append("      b.cr_locat<>'00' and b.cr_status='0'                         \n");
				strQuery.append("      and b.cr_team = 'D1'											\n");
				strQuery.append("and     (b.cr_teamcd in ('4','5','9') and b.cr_team in             \n");
				strQuery.append("            (select cm_rgtcd from cmm0043 where cm_userid=?))      \n");
			} else if (pStateCd.equals("03")){
				strQuery.append("    b.cr_locat<>'00' and b.cr_status='3'                         \n");
				strQuery.append("      and b.cr_team = 'D1'											\n");
				strQuery.append("and b.cr_confusr is not null and b.cr_confusr=?                  \n");
				strQuery.append("and b.cr_confdate is not null                                    \n");
				strQuery.append("      and b.cr_team = 'D1'										  \n");
				strQuery.append("and to_char(b.cr_confdate,'yyyymmdd')>=?                         \n");
				strQuery.append("and to_char(b.cr_confdate,'yyyymmdd')<=?                         \n");
			} else if (pStateCd.equals("04")){
				strQuery.append("    b.cr_locat<>'00' and b.cr_status='9'                         \n");
				strQuery.append("and b.cr_confusr is not null and b.cr_confusr=?                  \n");
				strQuery.append("and b.cr_confdate is not null                                    \n");
				strQuery.append("      and b.cr_team = 'D1'										  \n");
				strQuery.append("and to_char(b.cr_confdate,'yyyymmdd')>=?                         \n");
				strQuery.append("and to_char(b.cr_confdate,'yyyymmdd')<=?                         \n");
			} 
			if (!pReqCd.equals("0"))   strQuery.append("and a.cr_qrycd = ?                        \n");
			if (!pTeamCd.equals("0"))  strQuery.append("and a.cr_teamcd = ?                       \n");
			if (pReqUser != null && pReqUser.length() > 0) { 
					strQuery.append("and a.cr_editor in (select cm_userid from cmm0040            \n"); 
					strQuery.append("                     where cm_username=?)                    \n");
			}
			if (pIsr != null && pIsr != "") {
				strQuery.append("and (a.cr_orderid like ? or g.cc_isrtitle like ?)                \n");
			}
			strQuery.append("and b.cr_acptno = a.cr_acptno                                        \n");	
			strQuery.append("and a.cr_teamcd = c.cm_deptcd                                        \n");
			strQuery.append("and a.CR_orderid = g.CC_orderid                                      \n");		
			strQuery.append("and a.cr_syscd=e.cm_syscd and a.cr_editor=f.cm_userid                \n");	
			strQuery.append("and d.cm_macode='REQUEST' and d.cm_micode=a.cr_qrycd                 \n");	

			pstmt = conn.prepareStatement(strQuery.toString());
			pstmt = new LoggableStatement(conn,strQuery.toString());
           
			Cnt = 0;
            if (pStateCd.equals("0")) {
	            pstmt.setString(++Cnt, pStartDt);
	            pstmt.setString(++Cnt, pEndDt);
	            pstmt.setString(++Cnt, pUserId);
            } else if (pStateCd.equals("01")){
	            pstmt.setString(++Cnt, pUserId);
            } else if (pStateCd.equals("02")){
	            pstmt.setString(++Cnt, pUserId);  
            }else if (pStateCd.equals("03") || pStateCd.equals("04")){
            	pstmt.setString(++Cnt, pUserId);
                pstmt.setString(++Cnt, pStartDt);
                pstmt.setString(++Cnt, pEndDt);
            }
			if (!pReqCd.equals("0"))  {
            	pstmt.setString(++Cnt, pReqCd);
            }
			if (!pTeamCd.equals("0")) {
            	pstmt.setString(++Cnt, pTeamCd);
            }
			if (pReqUser != null && pReqUser.length() > 0){
	           pstmt.setString(++Cnt, pReqUser);
			}
			if (pIsr != null && pIsr != "") {
				pstmt.setString(++Cnt, "%"+pIsr+"%");
				pstmt.setString(++Cnt, "%"+pIsr+"%");
			}
			ecamsLogger.error(((LoggableStatement)pstmt).getQueryString());
            rs = pstmt.executeQuery();
            
			while (rs.next()){
				//EmgName = "";
				//PrjName = "";
				//UserName = "";
				//QryName = "";
				//SysName = "";
				//PgmSayu = "";
				ConfName = "";
				ConfTeamCd = "";
			    ColorSw = rs.getString("cr_status");	
			    FindFg = 0;
				if (pStateCd.equals("02")) {   //결재진행
					FindFg = 1;
					strQuery.setLength(0);
					strQuery.append("select count(*) as cnt from cmr9900      \n");
					strQuery.append(" where cr_acptno=? and cr_locat='00'     \n");
					strQuery.append("   and substr(cr_confusr,1,2)>=?         \n");
					strQuery.append("   and substr(cr_confusr,3,2)<=?         \n");
					pstmt2 = conn.prepareStatement(strQuery.toString());	
		            pstmt2.setString(1, rs.getString("cr_acptno"));
		            pstmt2.setString(2, rs.getString("cr_locat"));
		            pstmt2.setString(3, rs.getString("cr_locat"));		            
		            rs2 = pstmt2.executeQuery();
		            
		            if (rs2.next()){
		            	if (rs2.getInt("cnt") == 0){
		            		FindFg = 0;
		            	}
		            }
		            rs2.close();
		            pstmt2.close();
		            
				} else{
					FindFg = 0;
				}
				
				if (FindFg == 0) {
					if (rs.getString("cr_acptno").equals(AcptNo)) FindFg = 1; 
				}
				if (FindFg == 0) {
					AcptNo = rs.getString("cr_acptno");	
					
		            if (rs.getString("sta").equals("3")){
		            	ConfName = "반려";
		            }
		            else if (rs.getString("sta").equals("9")){
		            	ConfName = "결재완료";
		            }
		            else {
		            	strQuery.setLength(0);
						strQuery.append("select cr_teamcd,cr_confname,cr_congbn from cmr9900   \n");
					    strQuery.append(" where cr_acptno=? and cr_locat='00'        \n");
						pstmt2 = conn.prepareStatement(strQuery.toString());	
			            //pstmt2.setString(1, rs.getString("cr_acptno"));
						pstmt2.setString(1, AcptNo);
			            rs2 = pstmt2.executeQuery();		            
			            if (rs2.next()){
			            	ConfTeamCd = rs2.getString("cr_teamcd");
			            	if (rs2.getString("cr_teamcd").equals("1")){
			            		ConfName = rs2.getString("cr_confname") + "중";
			            	}
			            	else{
			            		ConfName = rs2.getString("cr_confname") + "결재대기중";	
			            	}
			            	
			            	if (rs2.getString("cr_congbn").equals("4")) {
			            		ConfName = "[후열]"+ConfName;
			            	}
			            }
			            rs2.close();
			            pstmt2.close();
			            
		            }
		            if (Integer.parseInt(rs.getString("cr_qrycd")) < 30) {
		            	if (rs.getString("cr_prcdate") == null) {
		            		strQuery.setLength(0);
							strQuery.append("select count(*) as cnt from cmr1010   \n");
						    strQuery.append(" where cr_acptno=?                    \n");
						    strQuery.append("   and cr_putcode is not null         \n");
						    strQuery.append("   and cr_putcode<>'0000' and cr_prcdate is null \n");
							pstmt2 = conn.prepareStatement(strQuery.toString());	
				            //pstmt2.setString(1, rs.getString("cr_acptno"));
				            pstmt2.setString(1, AcptNo);
				            rs2 = pstmt2.executeQuery();		            
				            if (rs2.next() && !rs.getString("cr_qrycd").equals("46")){
				            	if (rs2.getInt("cnt") > 0) ColorSw = "5";
				            }
				            rs2.close();
				            pstmt2.close();
				            
				            if (!ColorSw.equals("5")) {
				            	strQuery.setLength(0);
								strQuery.append("select count(*) as cnt from cmr1011 a,cmr1010 b \n");
							    strQuery.append(" where b.cr_acptno=? and b.cr_prcdate is null   \n");
							    strQuery.append("   and b.cr_acptno=a.cr_acptno                  \n");
							    strQuery.append("   and b.cr_serno=a.cr_serno                    \n");
							    strQuery.append("   and nvl(cr_prcrst,'0000')<>'0000'            \n");
							    pstmt2 = conn.prepareStatement(strQuery.toString());
							    //pstmt2.setString(1, rs.getString("cr_acptno"));
					            pstmt2.setString(1, AcptNo);		            
					            rs2 = pstmt2.executeQuery();		            
					            if (rs2.next()){
					            	if (rs2.getInt("cnt") > 0) ColorSw = "5";
					            }
					            rs2.close();
					            pstmt2.close();
					            
			            	}
		            	}
		            }
		            if (rs.getString("cr_status").equals("3")){
		            	ColorSw = "3";
		            }
		            else if (rs.getString("cr_status").equals("9")){
		            	ColorSw = "9";
		            }
		            else if (rs.getString("cr_prcdate") != null){
		            	ColorSw = "9";
		            }
		            
					rst = new HashMap<String, String>();				
					rst.put("cm_sysmsg",   rs.getString("cm_sysmsg"));      //시스템 
	        		rst.put("acptno",  rs.getString("acptno"));         //요청번호
	        		rst.put("deptname",rs.getString("cm_deptname"));    //팀명
	        		rst.put("editor",  rs.getString("cm_username"));    //요청자
	        		rst.put("qrycd",   rs.getString("qrycd"));			//요청구분
	        		rst.put("sayu",    rs.getString("cr_sayu"));	    //요청내용
	        		rst.put("acptdate",rs.getString("acptdate"));       //요청일시
	        		rst.put("signteamcd", ConfTeamCd);
	        		rst.put("sta",     ConfName);                       //상태
	        		if (rs.getString("orderid") != null){
	        			rst.put("orderid", rs.getString("orderid"));
	        			rst.put("isrtitle", rs.getString("CC_REQSUB"));
	        		}else{
	        			rst.put("orderid", "");
	        			rst.put("isrtitle", "");
	        		}
	        		rst.put("cr_qrycd",  rs.getString("cr_qrycd"));       //Qrycd
	        		rst.put("cr_editor",  rs.getString("cr_editor"));     //Editor
	        		rst.put("endyn",  rs.getString("cr_status"));       //처리완료여부
	        		rst.put("cr_syscd",  rs.getString("cr_syscd"));       //SysCd
	        		rst.put("cr_congbn", rs.getString("cr_congbn"));
	        		//rst.put("acptno2",  rs.getString("cr_acptno"));     //AcptNo
	        		rst.put("cr_acptno",  rs.getString("cr_acptno"));     //AcptNo
	        		if (rs.getString("confdate") != null) {
	        			rst.put("confdate", rs.getString("confdate"));
	        		}
					if (rs.getString("prcdate") != null) rst.put("prcdate", rs.getString("prcdate"));
	        		rst.put("colorsw",  ColorSw);
	        		rtList.add(rst);
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
			
			rtObj =  rtList.toArray();
			rtList.clear();
			rtList = null;
			
			return rtObj;
			
		} catch (SQLException sqlexception) {
			sqlexception.printStackTrace();
			ecamsLogger.error("## Cmr3600.SelectList() SQLException START ##");
			ecamsLogger.error("## Error DESC : ", sqlexception);	
			ecamsLogger.error("## Cmr3600.SelectList() SQLException END ##");			
			throw sqlexception;
		} catch (Exception exception) {
			exception.printStackTrace();
			ecamsLogger.error("## Cmr3600.SelectList() Exception START ##");				
			ecamsLogger.error("## Error DESC : ", exception);	
			ecamsLogger.error("## Cmr3600.SelectList() Exception END ##");				
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
					ecamsLogger.error("## Cmr3600.SelectList() connection release exception ##");
					ex3.printStackTrace();
				}
			}
		}
	}//end of SelectList() method statement

}//end of Cmr3600 class statement