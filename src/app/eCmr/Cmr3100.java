/*****************************************************************************************
	1. program ID	: eCmr3100.java
	2. create date	: 
	3. auth		    : 
	4. update date	: 11.09.07
	5. auth		    : Won
	6. description	: eCmr3100.java
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
import app.eCmm.Cmm1600;

import com.ecams.common.dbconn.ConnectionContext;
import com.ecams.common.dbconn.ConnectionResource;
import com.ecams.common.logger.EcamsLogger;
import com.ecams.service.srlink.SRRestApi;

/** 
 * @author bigeyes
 * 
 * Window - Preferences - Java - Code Style - Code Templates
 */


public class Cmr3100{
    /**
     * Logger Class Instance Creation
     * logger
     */
    Logger ecamsLogger  = EcamsLogger.getLoggerInstance();
    
	/**
	 * 
	 * @param  gbn
	 * @return ArrayList
	 * @throws SQLException
	 * @throws Exception
	 */
    										
    //Cmr3100.get_SelectList(strQry,strTeam,strSta,txtUser.text,mx.utils.StringUtil.trim(txtIsr.text),strStD,strEdD,strUserId);
    public Object[] get_SelectList(String pReqCd,String pTeamCd,String pStateCd,
    								String pReqUser,String pStartDt,String pEndDt,String pUserId, boolean pDeptManager) 
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
        String            ConfTeam  = "";
        
		ConnectionContext connectionContext = new ConnectionResource();
		
		try {  

			conn = connectionContext.getConnection();
			
			strQuery.setLength(0);
		    strQuery.append(" select a.cr_acptno,a.cr_orderid orderid ,     					\n");
		    strQuery.append(" a.cr_editor,a.cr_qrycd,a.cr_syscd,a.cr_status,a.cr_sayu,    		\n");    
		    strQuery.append(" a.cr_prcdate,b.cr_locat,b.cr_team,b.cr_status sta,          		\n");    
		    strQuery.append(" b.cr_confusr,b.cr_congbn,b.cr_confname,c.cm_deptname,          	\n"); 
		    strQuery.append(" to_char(b.cr_confdate,'yyyy/mm/dd hh24:mi') confdate,          	\n"); 
		    strQuery.append(" to_char(a.cr_acptdate,'yyyy/mm/dd hh24:mi') as acptdate,        	\n");
		    strQuery.append(" to_char(a.cr_prcdate,'yyyy/mm/dd hh24:mi') as prcdate,          	\n");
		    strQuery.append(" substr(a.cr_acptno,1,4) || '-' || substr(a.cr_acptno,5,2) ||    	\n");
		    strQuery.append(" '-' || substr(a.cr_acptno,7,6) as acptno,a.cr_prcreq,           	\n");
		    strQuery.append(" d.cm_codename qrycd,e.cm_sysmsg,f.cm_username                		\n");   
		    strQuery.append(" from cmm0040 f,cmm0030 e,cmm0020 d,cmm0100 c,cmr9900 b,cmr1000 a where	\n");
		 
		    if (pDeptManager == false) {
				if (pStateCd.equals("0")) {				
					strQuery.append("      b.cr_locat<>'00' and ((b.cr_status<>'0'	\n");
					strQuery.append("  and b.cr_confdate is not null and b.cr_confusr is not null       \n");
					strQuery.append("  and to_char(b.cr_confdate,'yyyymmdd')>=?                         \n");
					strQuery.append("  and to_char(b.cr_confdate,'yyyymmdd')<=?                         \n");
					strQuery.append("  and b.cr_confusr=?) or                                           \n");	
					strQuery.append("     (b.cr_status='0' and b.cr_teamcd not in ('1','4','5','9')     \n");
					strQuery.append("      and b.cr_team=?) or                                          \n");
					strQuery.append("     (b.cr_status='0' and b.cr_teamcd in ('4','5','9')             \n");
					strQuery.append("      and b.cr_team in (select cm_rgtcd from cmm0043               \n");
					strQuery.append("                     where cm_userid=?)))                          \n");
				} else if (pStateCd.equals("01")){//
					strQuery.append("      b.cr_status='0'                                              \n");
					strQuery.append("and ((b.cr_teamcd not in ('1','4','5','9') and b.cr_team=?) or     \n");
					strQuery.append("     (b.cr_teamcd in ('4','5','9') and b.cr_team in                \n");
					strQuery.append("            (select cm_rgtcd from cmm0043 where cm_userid=?)))     \n");
					strQuery.append("and b.cr_locat>=(select substr(cr_confusr,1,2) from cmr9900        \n");
					strQuery.append("                  where cr_acptno=b.cr_acptno and cr_locat='00')   \n");
					strQuery.append("and b.cr_locat<=(select substr(cr_confusr,3,2) from cmr9900        \n");
					strQuery.append("                  where cr_acptno=b.cr_acptno and cr_locat='00')   \n");
				} else if (pStateCd.equals("02")){  
					strQuery.append("      b.cr_locat<>'00' and b.cr_status='0'                         \n");
					strQuery.append("and ((b.cr_teamcd not in ('1','4','5','9') and b.cr_team=?) or     \n");
					strQuery.append("     (b.cr_teamcd in ('4','5','9') and b.cr_team in                \n");
					strQuery.append("            (select cm_rgtcd from cmm0043 where cm_userid=?)))     \n");
				} else if (pStateCd.equals("03")){
					strQuery.append("    b.cr_locat<>'00' and b.cr_status='3'                         \n");
					strQuery.append("and b.cr_confusr is not null and b.cr_confusr=?                  \n");
					strQuery.append("and b.cr_confdate is not null                                    \n");
					strQuery.append("and to_char(b.cr_confdate,'yyyymmdd')>=?                         \n");
					strQuery.append("and to_char(b.cr_confdate,'yyyymmdd')<=?                         \n");
				} else if (pStateCd.equals("04")){
					strQuery.append("    b.cr_locat<>'00' and b.cr_status='9'                         \n");
					strQuery.append("and b.cr_confusr is not null and b.cr_confusr=?                  \n");
					strQuery.append("and b.cr_confdate is not null                                    \n");
					strQuery.append("and to_char(b.cr_confdate,'yyyymmdd')>=?                         \n");
					strQuery.append("and to_char(b.cr_confdate,'yyyymmdd')<=?                         \n");
				} 
		    }
		    else {
				if (pStateCd.equals("0")) {				
					strQuery.append("      b.cr_locat<>'00' and ((b.cr_status<>'0'	\n");
					strQuery.append("  and b.cr_confdate is not null and b.cr_confusr is not null       \n");
					strQuery.append("  and to_char(b.cr_confdate,'yyyymmdd')>=?                         \n");
					strQuery.append("  and to_char(b.cr_confdate,'yyyymmdd')<=?                         \n");
					strQuery.append("  and b.cr_confusr=?) or                                           \n");	
					strQuery.append("     (b.cr_status='0' and b.cr_teamcd not in ('1','4','5','9')     \n");
					strQuery.append("      and b.cr_team=?) or                                          \n");
					strQuery.append("     (b.cr_status='0' and b.cr_teamcd in ('4','5','9')             \n");
					strQuery.append("      and b.cr_team in (select cm_rgtcd from cmm0043               \n");
					strQuery.append("                     where cm_userid=?)))                          \n");
				} else if (pStateCd.equals("01") || pStateCd.equals("05")){//
					strQuery.append("      b.cr_status='0' and                                             \n");
					strQuery.append("b.cr_locat='00'  and b.cr_teamcd='3' and b.cr_sgngbn ='31' and a.cr_status in ('0','8') \n");
					strQuery.append(" AND EXISTS (SELECT 'X' FROM CMR9900 WHERE CR_ACPTNO = A.CR_ACPTNO \n");
					strQuery.append("              AND CR_LOCAT > '00' AND CR_TEAM = ? AND cr_sgngbn ='41' AND CR_STATUS = '9') \n");
				} else if (pStateCd.equals("02")){  
					strQuery.append("      b.cr_locat<>'00' and b.cr_status='0'                         \n");
					strQuery.append("and ((b.cr_teamcd not in ('1','4','5','9') and b.cr_team=?) or     \n");
					strQuery.append("     (b.cr_teamcd in ('4','5','9') and b.cr_team in                \n");
					strQuery.append("            (select cm_rgtcd from cmm0043 where cm_userid=?)))     \n");
				} else if (pStateCd.equals("03")){
					strQuery.append("    b.cr_locat<>'00' and b.cr_status='3'                         \n");
					strQuery.append("and b.cr_confusr is not null and b.cr_confusr=?                  \n");
					strQuery.append("and b.cr_confdate is not null                                    \n");
					strQuery.append("and to_char(b.cr_confdate,'yyyymmdd')>=?                         \n");
					strQuery.append("and to_char(b.cr_confdate,'yyyymmdd')<=?                         \n");
				} else if (pStateCd.equals("04")){
					strQuery.append("    b.cr_locat<>'00' and b.cr_status='9'                         \n");
					strQuery.append("and b.cr_confusr is not null and b.cr_confusr=?                  \n");
					strQuery.append("and b.cr_confdate is not null                                    \n");
					strQuery.append("and to_char(b.cr_confdate,'yyyymmdd')>=?                         \n");
					strQuery.append("and to_char(b.cr_confdate,'yyyymmdd')<=?                         \n");
				} 
		    }
			if (!pReqCd.equals("0"))   strQuery.append("and a.cr_qrycd = ?                        \n");
			if (!pTeamCd.equals("0"))  strQuery.append("and a.cr_teamcd = ?                       \n");
			if (pReqUser != null && pReqUser.length() > 0) { 
					strQuery.append("and a.cr_editor in (select cm_userid from cmm0040            \n"); 
					strQuery.append("                     where cm_username=?)                    \n");
			}
			strQuery.append("and b.cr_acptno = a.cr_acptno                                        \n");	
			strQuery.append("and a.cr_teamcd = c.cm_deptcd                                        \n");
			strQuery.append("and a.cr_syscd=e.cm_syscd and a.cr_editor=f.cm_userid                \n");	
			strQuery.append("and d.cm_macode='REQUEST' and d.cm_micode=a.cr_qrycd                 \n");	
			
	
			pstmt = conn.prepareStatement(strQuery.toString());
			pstmt = new LoggableStatement(conn,strQuery.toString());
           
			Cnt = 0;
			if (pDeptManager == false) {
	            if (pStateCd.equals("0")) {
		            pstmt.setString(++Cnt, pStartDt);
		            pstmt.setString(++Cnt, pEndDt);
		            pstmt.setString(++Cnt, pUserId);
		            pstmt.setString(++Cnt, pUserId);
		            pstmt.setString(++Cnt, pUserId);
	            } else if (pStateCd.equals("01")){
	            	pstmt.setString(++Cnt, pUserId);
		            pstmt.setString(++Cnt, pUserId);
	            } else if (pStateCd.equals("02")){
	            	pstmt.setString(++Cnt, pUserId);
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
			}
			else {
	            if (pStateCd.equals("0")) {
		            pstmt.setString(++Cnt, pStartDt);
		            pstmt.setString(++Cnt, pEndDt);
		            pstmt.setString(++Cnt, pUserId);
		            pstmt.setString(++Cnt, pUserId);
		            pstmt.setString(++Cnt, pUserId);
	            } else if (pStateCd.equals("01") || pStateCd.equals("05")){
	            	pstmt.setString(++Cnt, pUserId);
	            } else if (pStateCd.equals("02")){
	            	pstmt.setString(++Cnt, pUserId);
		            pstmt.setString(++Cnt, pUserId);  
	            } else if (pStateCd.equals("03") || pStateCd.equals("04")){
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
			}
		
			ecamsLogger.error(((LoggableStatement)pstmt).getQueryString());
            rs = pstmt.executeQuery();
            
			while (rs.next()){
		
				ConfName = "";
				ConfTeamCd = "";
				ConfTeam = "";
			    ColorSw = rs.getString("cr_status");	
			    FindFg = 0;
				if (pStateCd.equals("02")) {   //
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
						strQuery.append("select cr_team, cr_teamcd,cr_confname,cr_congbn from cmr9900   \n");
					    strQuery.append(" where cr_acptno=? and cr_locat='00'        \n");
						pstmt2 = conn.prepareStatement(strQuery.toString());	
						pstmt2.setString(1, AcptNo);
			            rs2 = pstmt2.executeQuery();		            
			            if (rs2.next()){
			            	ConfTeamCd = rs2.getString("cr_teamcd");
			            	ConfTeam = rs2.getString("cr_team");
			            	if (rs2.getString("cr_teamcd").equals("1")){
			            		ConfName = rs2.getString("cr_confname") + "중";
			            	}
			            	else{
			            		ConfName = rs2.getString("cr_confname") + "결재대기중";	
			            	}
			            	
			            	if (rs2.getString("cr_congbn").equals("4")) {
			            		ConfName = "[후결]"+ConfName;
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
					rst.put("cm_sysmsg",   rs.getString("cm_sysmsg"));  //
	        		rst.put("acptno",  rs.getString("acptno"));         //
	        		rst.put("deptname",rs.getString("cm_deptname"));    //
	        		rst.put("editor",  rs.getString("cm_username"));    //
	        		rst.put("qrycd",   rs.getString("qrycd"));			//
	        		rst.put("sayu",    rs.getString("cr_sayu"));	    //
	        		rst.put("acptdate",rs.getString("acptdate"));       //
	        		rst.put("signteamcd", ConfTeamCd);

	        		//2025.11.24 QA일괄결재 관련
	        		/*
	        		if(ConfTeam.equals("Q1")){
	        			rst.put("enabledyn", "false");
	        		}else{
	        			rst.put("enabledyn", "true");
	        		}
	        		*/
	        		
	        		rst.put("sta",     ConfName);                       //
	        		if (rs.getString("orderid") != null){
	        			rst.put("orderid", rs.getString("orderid"));
	        			rst.put("isrtitle", rs.getString("CC_REQSUB"));
	        		}else{
	        			rst.put("orderid", "");
	        			rst.put("isrtitle", "");
	        		}
	        		rst.put("cr_qrycd",  rs.getString("cr_qrycd"));       //Qrycd
	        		rst.put("cr_editor",  rs.getString("cr_editor"));     //Editor
	        		rst.put("endyn",  rs.getString("cr_status"));         //처
	        		rst.put("cr_syscd",  rs.getString("cr_syscd"));       //SysCd
	        		rst.put("cr_congbn", rs.getString("cr_congbn"));
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
			ecamsLogger.error("## Cmr3100.get_SelectList() SQLException START ##");
			ecamsLogger.error("## Error DESC : ", sqlexception);	
			ecamsLogger.error("## Cmr3100.get_SelectList() SQLException END ##");			
			throw sqlexception;
		} catch (Exception exception) {
			exception.printStackTrace();
			ecamsLogger.error("## Cmr3100.get_SelectList() Exception START ##");				
			ecamsLogger.error("## Error DESC : ", exception);	
			ecamsLogger.error("## Cmr3100.get_SelectList() Exception END ##");				
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
					ecamsLogger.error("## Cmr3100.get_SelectList() connection release exception ##");
					ex3.printStackTrace();
				}
			}
		}
	}//end of SelectList() method statement
    
    public String gyulChk(String AcptNo,String UserId) throws SQLException, Exception {
		Connection        conn        = null;
		ConnectionContext connectionContext = new ConnectionResource();
		String            retMsg      = "";
		
		try {
			conn = connectionContext.getConnection();
			
			CallableStatement cs = null;
			cs = conn.prepareCall("{call GYUL_CHECK(?,?,?)}");
            cs.setString(1, AcptNo);
            cs.setString(2, UserId);
            cs.registerOutParameter(3,java.sql.Types.INTEGER);
            cs.execute();
            
            retMsg = Integer.toString(cs.getInt(3));
            
            cs.close();
            conn.close();
            cs = null;           
            conn = null;
            
    		return retMsg;
    		
		} catch (SQLException sqlexception) {
			sqlexception.printStackTrace();
			ecamsLogger.error("## Cmr3200.gyulChk() SQLException START ##");
			ecamsLogger.error("## Error DESC : ", sqlexception);	
			ecamsLogger.error("## Cmr3200.gyulChk() SQLException END ##");			
			throw sqlexception;
		} catch (Exception exception) {
			exception.printStackTrace();
			ecamsLogger.error("## Cmr3200.gyulChk() Exception START ##");				
			ecamsLogger.error("## Error DESC : ", exception);	
			ecamsLogger.error("## Cmr3200.gyulChk() Exception END ##");				
			throw exception;
		}finally{
			if (conn != null){
				try{
					conn.close();
				}catch(Exception ex3){
					ecamsLogger.error("## Cmr3200.gyulChk() connection release exception ##");
					ex3.printStackTrace();
				}
			}
		}
	}//end of gyulChk() method statement
    
    public String gyulChk_bujang(String AcptNo,String UserId) throws SQLException, Exception {
		Connection        conn        = null;
		PreparedStatement pstmt       = null;
		PreparedStatement pstmt2       = null;
		ResultSet         rs          = null;
		ResultSet         rs2          = null;
		ConnectionContext connectionContext = new ConnectionResource();
		StringBuffer      strQuery    = new StringBuffer();
		
		try {
			conn = connectionContext.getConnection();
			
			String retMsg = "N";
			
			conn = connectionContext.getConnection();
			
        	strQuery.setLength(0);
        	strQuery.append("select count(*) cnt from cmr9900 where cr_acptno = ? and cr_locat = '00' and cr_status = '0' and cr_sgngbn='31' ");
        	//strQuery.append("select count(*) cnt from cmr9900 where cr_acptno = ? and cr_locat = '00' and cr_status = '0' and (cr_sgngbn='31' or cr_team = 'Q1' ");        	
        	pstmt = conn.prepareStatement(strQuery.toString());
        	pstmt.setString(1, AcptNo);
        	rs = pstmt.executeQuery();
        	if(rs.next()){
        		if(rs.getInt("cnt")>0){
        			strQuery.setLength(0);
                	strQuery.append("select cr_team from cmr9900 where cr_acptno = ? and cr_locat <> '00' and cr_status = '9' and cr_sgngbn='41' ");
                	pstmt2 = conn.prepareStatement(strQuery.toString());
                	pstmt2.setString(1, AcptNo);
                	rs2 = pstmt2.executeQuery();
                	if(rs2.next()){
                		if(rs2.getString("cr_team").equals(UserId)){
                			retMsg = "Y";
                		}
            		}
                	rs2.close();
                	pstmt2.close();
        		}
        	}
        	rs.close();
        	pstmt.close();
            conn.close();       
            
            rs = null;
            pstmt = null;
            conn = null;
            
    		return retMsg;
    		
		} catch (SQLException sqlexception) {
			sqlexception.printStackTrace();
			ecamsLogger.error("## Cmr3100.gyulChk_bujang() SQLException START ##");
			ecamsLogger.error("## Error DESC : ", sqlexception);	
			ecamsLogger.error("## Cmr3100.gyulChk_bujang() SQLException END ##");			
			throw sqlexception;
		} catch (Exception exception) {
			exception.printStackTrace();
			ecamsLogger.error("## Cmr3100.gyulChk_bujang() Exception START ##");				
			ecamsLogger.error("## Error DESC : ", exception);	
			ecamsLogger.error("## Cmr3100.gyulChk_bujang() Exception END ##");				
			throw exception;
		}finally{
			if (conn != null){
				try{
					conn.close();
				}catch(Exception ex3){
					ecamsLogger.error("## Cmr3100.gyulChk_bujang() connection release exception ##");
					ex3.printStackTrace();
				}
			}
		}
	}//end of gyulChk_bujang() method statement
    
    public String nextConf2(String AcptNo,String UserId,String conMsg,String Cd,String ReqCd) throws SQLException, Exception {
		Connection        conn        = null;
		PreparedStatement pstmt       = null;
		PreparedStatement pstmt2      = null;
		ResultSet         rs          = null;
		StringBuffer      strQuery    = new StringBuffer();
		String  		  binpath	  = null;
		SystemPath		  systemPath = new SystemPath();
		String[] strAry = null;
		String            makeFile    = "";
		String            strTmpPath  = "";
		Runtime  run = null;
		Process p = null;
		File shfile=null;
		ConnectionContext connectionContext = new ConnectionResource();
		SystemPath		  cTempGet	  = new SystemPath();
		String			  tmpPath = "";
		String			  docPath = "";
		String			  strBinPath = "";
		String rtString = "";
		try {
			
			conn = connectionContext.getConnection();
			
        	strQuery.setLength(0);
        	strQuery.append("Begin CMR9900_STR ( ");
        	strQuery.append("?, ?, ?, '9', ?, ? ); End;");
        	pstmt = conn.prepareStatement(strQuery.toString());
        	pstmt.setString(1, AcptNo);
        	pstmt.setString(2,UserId);
        	pstmt.setString(3,conMsg);
        	pstmt.setString(4,ReqCd);
        	pstmt.setString(5,Cd);
        	
        	//pstmt1 = new LoggableStatement(conn,strQuery.toString());
			//ecamsLogger.debug(((LoggableStatement)pstmt1).getQueryString());

        	pstmt.executeUpdate();
        	
        	pstmt.close();
        	
        	binpath = systemPath.getTmpDir("14");
        	strTmpPath = systemPath.getTmpDir("99");
        	
        	makeFile = "file" + AcptNo + UserId;
			
			shfile=null;
			shfile = new File(strTmpPath+"/"+ makeFile+".sh");              //File 
		
			if( !(shfile.isFile()) )              //File
			{
				shfile.createNewFile();          //File 
			}
			// 20221219 ecams_batexec 추가 쿼리
			strBinPath = cTempGet.getTmpDir("14");
			strQuery.setLength(0);
			strQuery.append("select cm_ipaddr, cm_port 	\n");
			strQuery.append("  from cmm0010 			\n");
			strQuery.append(" where cm_stno = 'ECAMS'	\n");
			
			pstmt = conn.prepareStatement(strQuery.toString());	
			rs = pstmt.executeQuery();
			if(rs.next()){
				rtString = rs.getString("cm_ipaddr") + " " + rs.getString("cm_port") + " 0";
			}
//			BaseDir = HomeDir.substring(0,HomeDir.indexOf("\\")) + dirList.get(i).get("cm_fullpath").replace("\\","/");
//			BaseDir = BaseDir.replace("//", "/");
			//if (SysOs.equals("03")) BaseDir = AgentDir + BaseDir;
			BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(strTmpPath + makeFile+".sh"),"MS949"));
			writer.write("cd "+binpath +"\n");
//			writer.write("ecams_verfile " + AcptNo+"\n");
			writer.write("./ecams_batexec " +rtString + " \"cd " + strBinPath+ ";" +"./ecams_verfile " + AcptNo + "\" \n");
			writer.write("exit $?\n");
			writer.close();
			
			strAry = new String[3];
			
			strAry[0] = "chmod";
			strAry[1] = "777";
			strAry[2] = strTmpPath+"/"+ makeFile+".sh";
			
			run = Runtime.getRuntime();

			p = run.exec(strAry);
			p.waitFor();
							
			run = Runtime.getRuntime();
			
			strAry = new String[2];
			
			strAry[0] = "/bin/sh";
			strAry[1] = strTmpPath+"/" + makeFile+".sh";
			
			p = run.exec(strAry);
			p.waitFor();
			
			//ecamsLogger.debug("====return===="+Integer.toString(p.exitValue())+" \n");
			if (p.exitValue() != 0) {
				throw new Exception("신규대상목록 추출을 위한 작업에 실패하였습니다 ");
			}

			conn.close();
        	pstmt = null;
        	conn = null;
        	
    		return "0";        	
			
		} catch (SQLException sqlexception) {
			sqlexception.printStackTrace();
			ecamsLogger.error("## Cmr3100.nextConf2() SQLException START ##");
			ecamsLogger.error("## Error DESC : ", sqlexception);	
			ecamsLogger.error("## Cmr3100.nextConf2() SQLException END ##");			
			throw sqlexception;
		} catch (Exception exception) {
			exception.printStackTrace();
			ecamsLogger.error("## Cmr3100.nextConf2() Exception START ##");				
			ecamsLogger.error("## Error DESC : ", exception);	
			ecamsLogger.error("## Cmr3100.nextConf2() Exception END ##");				
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
					ecamsLogger.error("## Cmr3100.nextConf2() connection release exception ##");
					ex3.printStackTrace();
				}
			}
		}
	}//end of nextConf() method statement
    
    public String packageConf(ArrayList<HashMap<String, String>> value, String UserId,String conMsg,String Cd) throws SQLException, Exception {
		Connection        conn        = null;
		Connection        connD        = null;
		PreparedStatement pstmt       = null;
		PreparedStatement pstmt2      = null;
		PreparedStatement pstmt3      = null;
		ResultSet         rs          = null;
		ResultSet         rs2          = null;
		StringBuffer      strQuery    = new StringBuffer();
		ArrayList<HashMap<String, String>>  rsval = new ArrayList<HashMap<String, String>>();
		HashMap<String, String>			  rst		  = null;
		boolean           testSw = false;
		
		int cntQaTeam = 0;     // QA 결재단계 존재여부
		int cntLeadStep = 0;   // 팀장/파트장 결재단계(32/94) 여부
		int cnt0900Add = 0;


		ConnectionContext connectionContext = new ConnectionResource();
		ConnectionContext connectionContextD = new ConnectionResource(false,"D");
		
		try {
			
			conn = connectionContext.getConnection();
			connD = connectionContextD.getConnection();			
			
			/*  cmr0900 validation  Check logic 추가!  
			 *  QA 일괄결재인 경우 Cmr0900의 책임자/QA확인 탭의 데이터 있는지 확인 후 결재  반려가 아닌경우
			 *  [추가] 예외시스템제외 : QA 결재단계 존재 여부 + (팀장/파트장 단계 32/94)인 경우 cmr0900 체크
			 *   1) QA결재단계가 있는 신청건 : acptno만으로 cr_team='Q1' 존재여부 확인
			 *   2) QA가 있으면, 현재 결재단계(cr_locat='00')의 sgngbn이 32/94이면 cmr0900 체크
			 *   파트장 or 책임자 결재시 권한은 파트장으로 되어 있음(94만 체크하도록 재 변경)
			*/
			if(!"3".equals(Cd)){
				for(int i=0;i<value.size();i++){
					/* 추가로직 예외시스템 제외 후 책임자/QA탭 테이블 확인후 에러처리 */
					cntQaTeam = 0;     
					cntLeadStep = 0;   
					cnt0900Add = 0;

					strQuery.setLength(0);
					strQuery.append(" select count(*) as count            \n");
					strQuery.append("   from cmr9900 a                    \n");
					strQuery.append("  where a.cr_acptno = ?              \n");
					strQuery.append("    and a.cr_team  = 'Q1'            \n");
					pstmt3 = conn.prepareStatement(strQuery.toString());
					pstmt3.setString(1, value.get(i).get("cr_acptno"));
					rs2 = pstmt3.executeQuery();
					if(rs2.next()){
						cntQaTeam = rs2.getInt("count");
					}
					rs2.close();
					pstmt3.close();

					if(cntQaTeam > 0){
						/*  현재 결재단계(cr_locat='00')의 sgngbn이 32/94(팀장/파트장)인지  */
						strQuery.setLength(0);
						strQuery.append(" select count(*) as count            \n");
						strQuery.append("   from cmr9900 a                    \n");
						strQuery.append("  where a.cr_acptno = ?              \n");
						strQuery.append("    and a.cr_locat = '00'            \n");
						strQuery.append("    and (a.cr_sgngbn = '94'          \n");	
						strQuery.append("          or a.cr_sgngbn = '32'      \n");											
						strQuery.append("          or a.cr_sgngbn like'%94%'  \n");					
						strQuery.append("          or a.cr_sgngbn like'%32%'  \n");											
						strQuery.append("        )                            \n");												
						pstmt3 = conn.prepareStatement(strQuery.toString());
						pstmt3.setString(1, value.get(i).get("cr_acptno"));
						rs2 = pstmt3.executeQuery();
						if(rs2.next()){
							cntLeadStep = rs2.getInt("count");
						}
						rs2.close();
						pstmt3.close();

						// 팀장/파트장 결재단계면 cmr0900 확인 (없으면 에러)
						if(cntLeadStep > 0){
							strQuery.setLength(0);
							strQuery.append(" select count(*) as count            \n");
							strQuery.append("   from cmr0900 a                    \n");
							strQuery.append("  where a.cr_acptno = ?              \n");
							pstmt2 = conn.prepareStatement(strQuery.toString());
							pstmt2.setString(1, value.get(i).get("cr_acptno"));
							rs2 = pstmt2.executeQuery();
							if(rs2.next()){
								cnt0900Add = rs2.getInt("count");
							}
							rs2.close();
							pstmt2.close();

							if(cnt0900Add == 0){
								throw new Exception("신청상세화면에서 책임자/QA확인 탭의\n필수 데이터를 입력하시기 바랍니다.\n\n 신청번호:" + value.get(i).get("cr_acptno"));
							}
						}
					}
					
					/* 기존로직 */
					strQuery.setLength(0);
					strQuery.append(" select count(*) as count  					  \n");
					strQuery.append(" from cmr9900 a	  							  \n");
					strQuery.append(" where a.cr_acptno= ?   				       	  \n");
					strQuery.append(" and a.cr_locat = '00'						  \n");
					strQuery.append(" and a.cr_team = 'Q1'		    				  \n");
					pstmt = conn.prepareStatement(strQuery.toString());
					pstmt.setString(1, value.get(i).get("cr_acptno"));
					rs = pstmt.executeQuery();
					int cnt9900 = 0;
					int cnt0900 = 0;
					
					if(rs.next()){
						cnt9900 = rs.getInt("count");
						if(cnt9900 > 0){
							strQuery.setLength(0);
							strQuery.append(" select count(*) as count  					  \n");
							strQuery.append(" from cmr0900 a     							  \n");
							strQuery.append(" where a.cr_acptno= ?   				       	  \n");
							pstmt2 = conn.prepareStatement(strQuery.toString());
							pstmt2.setString(1, value.get(i).get("cr_acptno"));
							rs2 = pstmt2.executeQuery();
							if(rs2.next()){
								cnt0900 = rs2.getInt("count");
							}
							rs2.close();
							pstmt2.close();
							
							if(cnt0900 == 0){
								throw new Exception("책임자/QA확인 탭의 데이터가 존재하지 않습니다.\n 신청번호:" + value.get(i).get("cr_acptno"));
							}
							
							// 테스트완료여부 검증 API		
							testSw = false;	
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
							strQuery.append("   and a.cc_testreq_yn='Y'   	                \n");
							strQuery.append(" order by a.cc_srreqid        					\n");
							//pstmt = conn.prepareStatement(strQuery.toString());	
							pstmt2 = new LoggableStatement(conn,strQuery.toString());	
							pstmt2.setString(1,value.get(i).get("cr_acptno"));		
					        ecamsLogger.error(((LoggableStatement)pstmt2).getQueryString());
					        rs2 = pstmt2.executeQuery();
							while (rs2.next()){
								rst = new HashMap<String,String>();
								rst.put("cc_srreqid", rs2.getString("cc_srreqid"));
								rst.put("cc_docsubj", rs2.getString("cc_docsubj"));
								rst.put("cc_testreq_yn", rs2.getString("cc_testreq_yn"));				
								rst.put("cc_testpass_cd", rs2.getString("cc_testpass_cd"));
								if (rs2.getString("cc_testreq_yn") != null && "Y".equals(rs2.getString("cc_testreq_yn"))) testSw = true;									
								rsval.add(rst);					
								rst = null;
							}//end of while-loop statement
							rs2.close();
							pstmt2.close();

							if (testSw) {
								SRRestApi  srrestapi = new SRRestApi();
								String retMsg = srrestapi.callSRTestYn(value.get(i).get("cr_acptno"),rsval,conn);
								if (retMsg != null && retMsg.startsWith("OK:")){					
									retMsg = retMsg.substring(3).trim();
									if (!"Y".equals(retMsg)){
										//결재가능Flag
										throw new Exception("테스트전담반 테스트 미완료(결재불가)\n 신청번호:" + value.get(i).get("cr_acptno"));
									}
								}
							}
						}
					}
				}
				rs.close();
			}
			pstmt.close();
			
			for(int i=0;i<value.size();i++){
				strQuery.setLength(0);
				strQuery.append("Begin CMR9900_STR ( ");
				strQuery.append("?, ?, ?, '9', ?, ? ); End;");
				pstmt = conn.prepareStatement(strQuery.toString());
				pstmt = new LoggableStatement(conn,strQuery.toString());
				pstmt.setString(1, value.get(i).get("cr_acptno"));
				pstmt.setString(2,UserId);
				pstmt.setString(3,conMsg);
				pstmt.setString(4, value.get(i).get("cr_qrycd"));
				pstmt.setString(5,Cd);
				ecamsLogger.error(((LoggableStatement)pstmt).getQueryString());
				pstmt.executeUpdate();
        	
				pstmt.close();
			}
        	//====================================================
            /////////////////////////////////////차세대DB 컨프넘버 삭제///////////
	       	if(Cd.equals("3")){
	            strQuery.setLength(0);
	            strQuery.append("select a.cr_rsrcname,a.cr_jobcd,a.cr_syscd from cmr1010 a, cmm0036 d     \n");
		        strQuery.append("where a.cr_acptno = ? 		  \n");
		        strQuery.append("and a.cr_baseitem =  a.cr_itemid            \n");
		        strQuery.append("and a.cr_syscd = d.cm_syscd \n");
		        strQuery.append("and a.cr_rsrccd = d.cm_rsrccd \n");
		        strQuery.append("and substr(d.cm_info,58,1) = '1' \n");
		        pstmt = conn.prepareStatement(strQuery.toString());
		        pstmt = new LoggableStatement(conn,strQuery.toString());
		        pstmt.setString(1, value.get(0).get("cr_acptno"));
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
			            pstmt2.setString(1, value.get(0).get("cr_acptno"));
			            pstmt2.setString(2, rs2.getString("cr_itemid"));
			            pstmt2.executeUpdate();
			            pstmt2.close();
		            }
		            pstmt3.close();
		            rs2.close();
		        }
		        pstmt.close();
		        rs.close();
        	}
        	//====================================================
        	conn.close();
        	connD.close();
        	pstmt = null;
        	pstmt2 = null;
        	pstmt3 = null;
        	conn = null;
        	
    		return "0";        	
			
		} catch (SQLException sqlexception) {
			sqlexception.printStackTrace();
			ecamsLogger.error("## Cmr3100.packageConf() SQLException START ##");
			ecamsLogger.error("## Error DESC : ", sqlexception);	
			ecamsLogger.error("## Cmr3100.packageConf() SQLException END ##");			
			throw sqlexception;
		} catch (Exception exception) {
			exception.printStackTrace();
			ecamsLogger.error("## Cmr3100.packageConf() Exception START ##");				
			ecamsLogger.error("## Error DESC : ", exception);	
			ecamsLogger.error("## Cmr3100.packageConf() Exception END ##");				
			throw exception;
		}finally{
			if (strQuery != null) 	strQuery = null;
			if (pstmt != null)  try{pstmt.close();}catch (Exception ex2){ex2.printStackTrace();}
			if (pstmt2 != null)  try{pstmt2.close();}catch (Exception ex2){ex2.printStackTrace();}
			if (pstmt3 != null)  try{pstmt3.close();}catch (Exception ex2){ex2.printStackTrace();}
			if (rs != null)  try{rs.close();}catch (Exception ex2){ex2.printStackTrace();}
			if (rs2 != null)  try{rs2.close();}catch (Exception ex2){ex2.printStackTrace();}
			if (conn != null){
				try{
					conn.close();
				}catch(Exception ex3){
					ecamsLogger.error("## Cmr3100.packageConf() connection release exception ##");
					ex3.printStackTrace();
				}
			}
			if (connD != null){
				try{
					connD.close();
				}catch(Exception ex3){
					ecamsLogger.error("## Cmr3100.packageConf() connection release exception ##");
					ex3.printStackTrace();
				}
			}
		}
	}//end of packageConf() method statement

    public String bujangConf(ArrayList<HashMap<String, String>> value, String UserId,String conMsg,String Cd) throws SQLException, Exception {
		Connection        conn        = null;
		PreparedStatement pstmt       = null;
		PreparedStatement pstmt2      = null;
		PreparedStatement pstmt3      = null;
		ResultSet         rs          = null;
		ResultSet         rs2          = null;
		StringBuffer      strQuery    = new StringBuffer();
		
		ConnectionContext connectionContext = new ConnectionResource();
		
		try {
			
			conn = connectionContext.getConnection();
			for(int i=0;i<value.size();i++){
				strQuery.setLength(0);
				strQuery.append("select cr_team from cmr9900 \n");
				strQuery.append("where cr_acptno = ? \n");
				strQuery.append("	and cr_locat = '00' \n");
				strQuery.append("	and cr_status = '0' \n");
				strQuery.append("	and cr_sgngbn = '31' \n"); //부장
				pstmt = conn.prepareStatement(strQuery.toString());
				pstmt = new LoggableStatement(conn,strQuery.toString());
				pstmt.setString(1,value.get(i).get("cr_acptno"));
				ecamsLogger.error(((LoggableStatement)pstmt).getQueryString());
				rs = pstmt.executeQuery();
				if(rs.next()){
					strQuery.setLength(0);
					strQuery.append("select cr_team from cmr9900 \n");
					strQuery.append("where cr_acptno = ? \n");
					strQuery.append("and cr_locat <> '00' \n");
					strQuery.append("and cr_status = '9' \n");
					strQuery.append("and cr_sgngbn = '41' \n"); //부부장
					pstmt2 = conn.prepareStatement(strQuery.toString());
					pstmt2 = new LoggableStatement(conn,strQuery.toString());
					pstmt2.setString(1,value.get(i).get("cr_acptno"));
					ecamsLogger.error(((LoggableStatement)pstmt2).getQueryString());
					rs2 = pstmt2.executeQuery();
					if(rs2.next()){
						if(rs2.getString("cr_team").equals(UserId)){
							strQuery.setLength(0);
							strQuery.append("Begin CMR9900_STR ( ");
							strQuery.append("?, ?, ?, '9', ?, ? ); End;");
							pstmt3 = conn.prepareStatement(strQuery.toString());
							pstmt3 = new LoggableStatement(conn,strQuery.toString());
							pstmt3.setString(1,value.get(i).get("cr_acptno"));
							pstmt3.setString(2,rs.getString("cr_team"));
							pstmt3.setString(3,conMsg);
							pstmt3.setString(4,value.get(i).get("cr_qrycd"));
							pstmt3.setString(5,Cd);
							ecamsLogger.error(((LoggableStatement)pstmt3).getQueryString());
							pstmt3.executeUpdate();
							pstmt3.close();
						}
					}
					rs2.close();
					pstmt2.close();
				}
				
				rs.close();
				pstmt.close();			
			}
        	conn.close();
        	pstmt = null;
        	conn = null;
        	
    		return "0";        	
			
		} catch (SQLException sqlexception) {
			sqlexception.printStackTrace();
			ecamsLogger.error("## Cmr3100.bujangConf() SQLException START ##");
			ecamsLogger.error("## Error DESC : ", sqlexception);	
			ecamsLogger.error("## Cmr3100.bujangConf() SQLException END ##");			
			throw sqlexception;
		} catch (Exception exception) {
			exception.printStackTrace();
			ecamsLogger.error("## Cmr3100.bujangConf() Exception START ##");				
			ecamsLogger.error("## Error DESC : ", exception);	
			ecamsLogger.error("## Cmr3100.bujangConf() Exception END ##");				
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
					ecamsLogger.error("## Cmr3100.bujangConf() connection release exception ##");
					ex3.printStackTrace();
				}
			}
		}
	}//end of bujangConf() method statement
    
    
    public String nextConf(String AcptNo,String UserId,String conMsg,String Cd,String ReqCd) throws SQLException, Exception {
		Connection        conn        = null;
		Connection        connD        = null;
		PreparedStatement pstmt       = null;
		PreparedStatement pstmt2      = null;
		PreparedStatement pstmt3      = null;
		ResultSet         rs          = null;
		ResultSet         rs2          = null;
		StringBuffer      strQuery    = new StringBuffer();

		int cntQaTeam = 0;     // QA 결재단계 존재여부
		int cntLeadStep = 0;   // 팀장/파트장 결재단계(32/94) 여부
		int cnt0900Add = 0;

		ConnectionContext connectionContext = new ConnectionResource();
		ConnectionContext connectionContextD = new ConnectionResource(false,"D");
		
		try {
			
			conn = connectionContext.getConnection();
			connD = connectionContextD.getConnection();
			
			/*  cmr0900 validation  Check logic 추가!  
			 *  QA 결재인 경우 Cmr0900의 책임자/QA확인 탭의 데이터 있는지 확인 후 결재  반려가 아닌경우
			 *  [추가] 예외시스템제외 : QA 결재단계 존재 여부 + (팀장/파트장 단계 32/94)인 경우 cmr0900 체크
			 *   1) QA결재단계가 있는 신청건 : acptno만으로 cr_team='Q1' 존재여부 확인
			 *   2) QA가 있으면, 현재 결재단계(cr_locat='00')의 sgngbn이 32/94이면 cmr0900 체크
			 *   파트장 or 책임자 결재시 권한은 파트장으로 되어 있음(94만 체크하도록 재 변경)
			*/

			cntQaTeam = 0;     
			cntLeadStep = 0;   
			cnt0900Add = 0;

			strQuery.setLength(0);
			strQuery.append(" select count(*) as count            \n");
			strQuery.append("   from cmr9900 a                    \n");
			strQuery.append("  where a.cr_acptno = ?              \n");
			strQuery.append("    and a.cr_team  = 'Q1'            \n");
			pstmt = conn.prepareStatement(strQuery.toString());
        	pstmt.setString(1,AcptNo);
			rs = pstmt.executeQuery();
			if(rs.next()){
				cntQaTeam = rs.getInt("count");
			}
			rs.close();
			pstmt.close();
			
			if(cntQaTeam > 0){
				/*  현재 결재단계(cr_locat='00')의 sgngbn이 32/94(팀장/파트장)인지  */
				strQuery.setLength(0);
				strQuery.append(" select count(*) as count            \n");
				strQuery.append("   from cmr9900 a                    \n");
				strQuery.append("  where a.cr_acptno = ?              \n");
				strQuery.append("    and a.cr_locat = '00'            \n");
				strQuery.append("    and (a.cr_sgngbn = '94'          \n");	
				strQuery.append("          or a.cr_sgngbn = '32'      \n");											
				strQuery.append("          or a.cr_sgngbn like'%94%'  \n");					
				strQuery.append("          or a.cr_sgngbn like'%32%'  \n");											
				strQuery.append("        )                            \n");												
				pstmt = conn.prepareStatement(strQuery.toString());
        		pstmt.setString(1,AcptNo);
				rs = pstmt.executeQuery();
				if(rs.next()){
					cntLeadStep = rs.getInt("count");
				}
				rs.close();
				pstmt.close();

				// 파트장/팀장 결재단계면 cmr0900 확인 (없으면 에러)
				if(cntLeadStep > 0){
					strQuery.setLength(0);
					strQuery.append(" select count(*) as count            \n");
					strQuery.append("   from cmr0900 a                    \n");
					strQuery.append("  where a.cr_acptno = ?              \n");
					pstmt = conn.prepareStatement(strQuery.toString());
        			pstmt.setString(1,AcptNo);
					rs = pstmt.executeQuery();
					if(rs.next()){
						cnt0900Add = rs.getInt("count");
					}
					rs.close();
					pstmt.close();

					if(cnt0900Add == 0){
						throw new Exception("신청상세화면에서 책임자/QA확인 탭의\n필수 데이터를 입력하시기 바랍니다.\n\n 신청번호:" + value.get(i).get("cr_acptno"));
					}
				}
				
				/* QA단계도 체크 */
				strQuery.setLength(0);
				strQuery.append(" select count(*) as count  					  \n");
				strQuery.append(" from cmr9900 a	  							  \n");
				strQuery.append(" where a.cr_acptno= ?   				       	  \n");
				strQuery.append(" and a.cr_locat = '00'						  \n");
				strQuery.append(" and a.cr_team = 'Q1'		    				  \n");
				pstmt = conn.prepareStatement(strQuery.toString());
        		pstmt.setString(1,AcptNo);
				rs = pstmt.executeQuery();
				int cnt9900 = 0;
				int cnt0900 = 0;
				if(rs.next()){
					cnt9900 = rs.getInt("count");
					if(cnt9900 > 0){
						strQuery.setLength(0);
						strQuery.append(" select count(*) as count  					  \n");
						strQuery.append(" from cmr0900 a     							  \n");
						strQuery.append(" where a.cr_acptno= ?   				       	  \n");
						pstmt2 = conn.prepareStatement(strQuery.toString());
						pstmt2.setString(1, value.get(i).get("cr_acptno"));
						rs2 = pstmt2.executeQuery();
						if(rs2.next()){
							cnt0900 = rs2.getInt("count");
						}
						rs2.close();
						pstmt2.close();
						
						if(cnt0900 == 0){
							throw new Exception("책임자/QA확인 탭의 데이터가 존재하지 않습니다.\n 신청번호:" + value.get(i).get("cr_acptno"));
						}
					}
				}
			}

        	strQuery.setLength(0);
        	strQuery.append("Begin CMR9900_STR ( ");
        	strQuery.append("?, ?, ?, '9', ?, ? ); End;");
        	pstmt = conn.prepareStatement(strQuery.toString());
        	pstmt.setString(1,AcptNo);
        	pstmt.setString(2,UserId);
        	pstmt.setString(3,conMsg);
        	pstmt.setString(4,ReqCd);
        	pstmt.setString(5,Cd);
        	pstmt.executeUpdate();        	
        	pstmt.close();
        	
        	//2022.10.12 FORTIFY SKIP 할 때 process kill
			if("SYSFT".equals(UserId)) {
				Cmm1600 cmm1600 = new Cmm1600();
				String parmName = "prockill2 ecams_acct " + AcptNo;
				int diffRst = cmm1600.execShell(UserId+"_"+AcptNo + "_apcmd.sh",parmName,false);
				
				/*
				String binPath = "";
				String[] chkAry;
				Process p = null;
				Runtime  run = null;
				
				SystemPath systemPath = new SystemPath();
				binPath = systemPath.getTmpDir_conn("14",conn);
				systemPath = null;

				run = Runtime.getRuntime();

				chkAry = new String[4];
				chkAry[0] = "/bin/sh";
				chkAry[1] = binPath + "/prockill2";
				chkAry[2] = "ecams_acct";
				chkAry[3] = AcptNo;

				p = run.exec(chkAry);
				p.waitFor();
				*/
				ecamsLogger.error("========== [SYSFT] skip and process kill [AcptNo] " + AcptNo + " [exitValue] " + diffRst);
			}
        	
        	//====================================================
            /////////////////////////////////////차세대DB 컨프넘버 삭제///////////
        	//컴프넘버 acptno 베이스아이템이 개발쪽아이템아이디
        	if(Cd.equals("3")){
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
		            pstmt3.close();
		            rs2.close();
		        }
		        pstmt.close();
		        rs.close();
        	}
        	//====================================================
        	
        	conn.close();
        	connD.close();
        	pstmt = null;
        	pstmt2 = null;
        	pstmt3 = null;
        	conn = null;
        	connD = null;
        	
    		return Cd;        	
			
		} catch (SQLException sqlexception) {
			sqlexception.printStackTrace();
			ecamsLogger.error("## Cmr3100.nextConf() SQLException START ##");
			ecamsLogger.error("## Error DESC : ", sqlexception);	
			ecamsLogger.error("## Cmr3100.nextConf() SQLException END ##");			
			throw sqlexception;
		} catch (Exception exception) {
			exception.printStackTrace();
			ecamsLogger.error("## Cmr3100.nextConf() Exception START ##");				
			ecamsLogger.error("## Error DESC : ", exception);	
			ecamsLogger.error("## Cmr3100.nextConf() Exception END ##");				
			throw exception;
		}finally{
			if (strQuery != null) 	strQuery = null;
			if (pstmt != null)  try{pstmt.close();}catch (Exception ex2){ex2.printStackTrace();}
			if (pstmt2 != null)  try{pstmt2.close();}catch (Exception ex2){ex2.printStackTrace();}
			if (pstmt3 != null)  try{pstmt3.close();}catch (Exception ex2){ex2.printStackTrace();}
			if (rs != null)  try{rs.close();}catch (Exception ex2){ex2.printStackTrace();}
			if (rs2 != null)  try{rs2.close();}catch (Exception ex2){ex2.printStackTrace();}
			if (conn != null){
				try{
					conn.close();
				}catch(Exception ex3){
					ecamsLogger.error("## Cmr3100.nextConf() connection release exception ##");
					ex3.printStackTrace();
				}
			}
			if (connD != null){
				try{
					connD.close();
				}catch(Exception ex3){
					ecamsLogger.error("## Cmr3100.nextConf() connection release exception ##");
					ex3.printStackTrace();
				}
			}
		}
	}//end of nextConf() method statement

    public String nextConf_ISR(String AcptNo,String UserId,String conMsg,String Cd,String ReqCd) throws SQLException, Exception {
		Connection        conn        = null;
		PreparedStatement pstmt       = null;
		StringBuffer      strQuery    = new StringBuffer();
		
		ConnectionContext connectionContext = new ConnectionResource();
		try {
			
			conn = connectionContext.getConnection();
        	strQuery.setLength(0);
        	strQuery.append("Begin CMR9900_STR ( ");
        	strQuery.append("?, ?, ?, '9', ?, ? ); End;");
        	pstmt = conn.prepareStatement(strQuery.toString());
        	pstmt.setString(1, AcptNo);
        	pstmt.setString(2,UserId);
        	pstmt.setString(3,conMsg);
        	pstmt.setString(4,ReqCd);
        	pstmt.setString(5,Cd);    
        	pstmt.executeUpdate();        	
        	pstmt.close();
        	
        	conn.close();
        	pstmt = null;
        	conn = null;
        	
    		return "0";        	
			
		} catch (SQLException sqlexception) {
			sqlexception.printStackTrace();
			ecamsLogger.error("## Cmr3100.nextConf_ISR() SQLException START ##");
			ecamsLogger.error("## Error DESC : ", sqlexception);	
			ecamsLogger.error("## Cmr3100.nextConf_ISR() SQLException END ##");			
			throw sqlexception;
		} catch (Exception exception) {
			exception.printStackTrace();
			ecamsLogger.error("## Cmr3100.nextConf_ISR() Exception START ##");				
			ecamsLogger.error("## Error DESC : ", exception);	
			ecamsLogger.error("## Cmr3100.nextConf_ISR() Exception END ##");				
			throw exception;
		}finally{
			if (strQuery != null) 	strQuery = null;
			if (pstmt != null)  try{pstmt.close();}catch (Exception ex2){ex2.printStackTrace();}
			if (conn != null){
				try{
					conn.close();
				}catch(Exception ex3){
					ecamsLogger.error("## Cmr3100.nextConf_ISR() connection release exception ##");
					ex3.printStackTrace();
				}
			}
		}
	}//end of nextConf() method statement
    
    public String nextConf3(String AcptNo,String UserId,String conMsg,String Cd,String ReqCd) throws SQLException, Exception {
		Connection        conn        = null;
		Connection        connD        = null;
		PreparedStatement pstmt       = null;
		PreparedStatement pstmt2       = null;
		PreparedStatement pstmt3       = null;
		ResultSet         rs          = null;
		ResultSet         rs2          = null;
		StringBuffer      strQuery    = new StringBuffer();

		ConnectionContext connectionContext = new ConnectionResource();
		ConnectionContext connectionContextD = new ConnectionResource(false,"D");
		
		try {
			
			conn = connectionContext.getConnection();
			connD = connectionContextD.getConnection();
			String confId = "";
			
			//cr_sgngbn='31'
			strQuery.setLength(0);
			strQuery.append("select cr_team from cmr9900 \n");
			strQuery.append("where cr_acptno = ? \n");
			strQuery.append("and cr_locat <> '00' \n");
			strQuery.append("and cr_status = '0' \n");
			strQuery.append("and cr_sgngbn = '31' \n"); //부장
			pstmt = conn.prepareStatement(strQuery.toString());
			pstmt.setString(1,AcptNo);
			rs = pstmt.executeQuery();
			if(rs.next()){
				confId = rs.getString("cr_team");
			}
			rs.close();
			pstmt.close();
			
        	strQuery.setLength(0);
        	strQuery.append("Begin CMR9900_STR ( ");
        	strQuery.append("?, ?, ?, '9', ?, ? ); End;");
        	pstmt = conn.prepareStatement(strQuery.toString());
        	pstmt.setString(1,AcptNo);
        	pstmt.setString(2,confId);
        	pstmt.setString(3,conMsg);
        	pstmt.setString(4,ReqCd);
        	pstmt.setString(5,Cd);
        	
        	pstmt.executeUpdate();        	
        	pstmt.close();
        	
        	//====================================================
            /////////////////////////////////////차세대DB 컨프넘버 삭제///////////
        	if(Cd.equals("3")){
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
		            pstmt3.close();
		            rs2.close();
		        }
		        pstmt.close();
		        rs.close();
        	}
        	//====================================================
        	
        	conn.close();
        	connD.close();
        	pstmt = null;
        	pstmt2 = null;
        	pstmt3 = null;
        	conn = null;
        	connD = null;
        	
    		return Cd;        	
			
		} catch (SQLException sqlexception) {
			sqlexception.printStackTrace();
			ecamsLogger.error("## Cmr3100.nextConf3() SQLException START ##");
			ecamsLogger.error("## Error DESC : ", sqlexception);	
			ecamsLogger.error("## Cmr3100.nextConf3() SQLException END ##");			
			throw sqlexception;
		} catch (Exception exception) {
			exception.printStackTrace();
			ecamsLogger.error("## Cmr3100.nextConf3() Exception START ##");				
			ecamsLogger.error("## Error DESC : ", exception);	
			ecamsLogger.error("## Cmr3100.nextConf3() Exception END ##");				
			throw exception;
		}finally{
			if (strQuery != null) 	strQuery = null;
			if (pstmt != null)  try{pstmt.close();}catch (Exception ex2){ex2.printStackTrace();}
			if (pstmt2 != null)  try{pstmt2.close();}catch (Exception ex2){ex2.printStackTrace();}
			if (pstmt3 != null)  try{pstmt3.close();}catch (Exception ex2){ex2.printStackTrace();}
			if (rs != null)  try{rs.close();}catch (Exception ex2){ex2.printStackTrace();}
			if (rs2 != null)  try{rs2.close();}catch (Exception ex2){ex2.printStackTrace();}
			if (conn != null){
				try{
					conn.close();
				}catch(Exception ex3){
					ecamsLogger.error("## Cmr3100.nextConf3() connection release exception ##");
					ex3.printStackTrace();
				}
			}
			if (connD != null){
				try{
					connD.close();
				}catch(Exception ex3){
					ecamsLogger.error("## Cmr3100.nextConf3() connection release exception ##");
					ex3.printStackTrace();
				}
			}
		}
	}//end of nextConf3() method statement
    
    public String fortifyUpdate(String AcptNo, String resultMSG, String txtConMsg) throws SQLException, Exception {
		Connection        conn        = null;
		PreparedStatement pstmt       = null;
		ResultSet         rs          = null;
		StringBuffer      strQuery    = new StringBuffer();

    	ConnectionContext connectionContextF = new ConnectionResource(false,"F");
		
		try {
			
			conn = connectionContextF.getConnection();			

			
            strQuery.setLength(0);
            strQuery.append("update Fortify_Sca_Name set job_status = ?  \n");
            strQuery.append(", job_status_reason  = ?  \n");
            strQuery.append("where pro_acptno =?          \n");
            pstmt = conn.prepareStatement(strQuery.toString());
	        pstmt = new LoggableStatement(conn,strQuery.toString());
            pstmt.setString(1, resultMSG);
            pstmt.setString(2, txtConMsg);
            pstmt.setString(3, AcptNo);
			ecamsLogger.error(((LoggableStatement)pstmt).getQueryString());
            pstmt.executeUpdate();
            
            pstmt.close();
		
            conn.close();
            pstmt = null;
            conn = null;
        	
            return "0";        	
			
		} catch (SQLException sqlexception) {
			sqlexception.printStackTrace();
			ecamsLogger.error("## Cmr3100.fortifyUpdate() SQLException START ##");
			ecamsLogger.error("## Error DESC : ", sqlexception);	
			ecamsLogger.error("## Cmr3100.fortifyUpdate() SQLException END ##");			
			throw sqlexception;
		} catch (Exception exception) {
			exception.printStackTrace();
			ecamsLogger.error("## Cmr3100.fortifyUpdate() Exception START ##");				
			ecamsLogger.error("## Error DESC : ", exception);	
			ecamsLogger.error("## Cmr3100.fortifyUpdate() Exception END ##");				
			throw exception;
		}finally{
			if (strQuery != null) 	strQuery = null;
			if (pstmt != null)  try{pstmt.close();}catch (Exception ex2){ex2.printStackTrace();}
			if (rs != null)  try{rs.close();}catch (Exception ex2){ex2.printStackTrace();}
			if (conn != null){
				try{
					conn.close();
				}catch(Exception ex3){
					ecamsLogger.error("## Cmr3100.fortifyUpdate() connection release exception ##");
					ex3.printStackTrace();
				}
			}
		}
	}//end of fortifyUpdate() method statement    
    
    // 20230223 ecams_verfile - 적용 신청상세 관리자가 단계완료버튼 누르면 처리 로직
    public String ecams_verfile(String AcptNo) throws SQLException, Exception {
    	SystemPath		  cTempGet	  = new SystemPath();
		String			  tmpPath = "";
		String			  docPath = "";
		String			  strBinPath = "";
		Connection        conn        = null;
		ResultSet         rs          = null;
		PreparedStatement pstmt       = null;
		StringBuffer      strQuery    = new StringBuffer();
		
		//String           strDevHome   = null;
		ConnectionContext connectionContext = new ConnectionResource();
		
		File shfile=null;
		String  shFileName = "";
	
		OutputStreamWriter writer = null;
		String[] strAry = null;
		Runtime  run = null;
		Process p = null;
		int rtn = 0;
		String rtString = "";
		try {
	
			tmpPath = cTempGet.getTmpDir("99");
			strBinPath = cTempGet.getTmpDir("14");
			docPath = cTempGet.getTmpDir("21");
			shFileName = tmpPath + "/" + AcptNo + "_ADMIN.sh"; 
			shfile = new File(shFileName);
			
			if( !(shfile.isFile()) )              //File이 없으면 
			{
				shfile.createNewFile();          //File 생성
			}
			
			// 20221219 ecams_batexec 추가 쿼리
			conn = connectionContext.getConnection();
			conn.setAutoCommit(false);
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
//			writer.write("rtval=`./ecams_fdsextract " + FileName + "`\n");
			writer.write("./ecams_batexec " +rtString + " \"cd " + strBinPath+ ";" +"./ecams_verfile " + AcptNo + " & \" \n");
//			writer.write("exit $rtval\n");
			writer.write("exit $?\n");
			writer.close();
			strAry = new String[3];
			strAry[0] = "chmod";
			strAry[1] = "777";
			strAry[2] = shFileName;			
			
			run = Runtime.getRuntime();
			
			p = run.exec(strAry);
			p.waitFor();
			
			run = Runtime.getRuntime();
			
			strAry = new String[2];
			
			strAry[0] = "/bin/sh";
			
			strAry[1] = shFileName;
			
			p = run.exec(strAry);
			p.waitFor();
			
			pstmt.close();
			conn.close();
			pstmt = null;
			conn = null;
			rs.close();// 20221219 ecams_batexec 추가 쿼리
			rs = null;
			return "0";
			
		} catch (SQLException sqlexception) {
			sqlexception.printStackTrace();
			ecamsLogger.error("## CMR3100.ecams_verfile() SQLException START ##");
			ecamsLogger.error("## Error DESC : ", sqlexception);	
			ecamsLogger.error("## CMR3100.ecams_verfile() SQLException END ##");			
			throw sqlexception;
		} catch (Exception exception) {
			exception.printStackTrace();
			ecamsLogger.error("## CMR3100.ecams_verfile() Exception START ##");				
			ecamsLogger.error("## Error DESC : ", exception);	
			ecamsLogger.error("## CMR3100.ecams_verfile() Exception END ##");				
			throw exception;
		}finally{
			if (strQuery != null) 	strQuery = null;
			if (pstmt != null)  try{pstmt.close();}catch (Exception ex2){ex2.printStackTrace();}
			if (conn != null){
				try{
					ConnectionResource.release(conn);
				}catch(Exception ex3){
					ecamsLogger.error("## CMR3100.ecams_verfile() connection release exception ##");
					ex3.printStackTrace();
				}
			}
		}
    }
    
}//end of Cmr3100 class statement