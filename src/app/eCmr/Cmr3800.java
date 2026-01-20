/*****************************************************************************************
	1. program ID	: eCmr3800.java
	2. create date	: 
	3. auth		    : [배포관리]
	4. update date	: 11.09.07
	5. auth		    : WON
	6. description	: eCmr3800.java 
*****************************************************************************************/

package app.eCmr;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import org.apache.logging.log4j.Logger;
import app.common.LoggableStatement;
import app.common.SystemPath;
import app.eCmd.Cmd0400;

import com.ecams.common.dbconn.ConnectionContext;
import com.ecams.common.dbconn.ConnectionResource;
import com.ecams.common.logger.EcamsLogger;

public class Cmr3800{

    Logger ecamsLogger  = EcamsLogger.getLoggerInstance();
    // 첫번째 그리드 조회
    public Object[] getFileList(String strUserId, String strSys,String strDept, String cboGbn, String Admin) throws SQLException, Exception {
		
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
			 strQuery.append(" select a.cr_acptno , a.cr_syscd,a.cr_status , a.cr_qrycd ,  a.cr_editor , a.cr_sayu, a.cr_lastdate, a.cr_acptdate,    \n ");
			 
			 strQuery.append("        a.cr_prcreq, a.cr_eddate,c.cm_sysmsg , d.CM_DEPTNAME  ,e.cm_username                                           \n ");
			 strQuery.append(" from cmr1000 a , cmr9900 b , cmm0030 c ,  cmm0100 d , cmm0040 e                                                       \n ");
			 strQuery.append(" where a.cr_acptno = b.cr_acptno                                                                                \n ");
			 strQuery.append("  and  a.cr_status = '0'                                                                                        \n ");
			 strQuery.append("  and  c.cm_syscd = a.cr_syscd                                                                                  \n ");			 
			 if (strSys!= "" && strSys != null){
				 strQuery.append("  and  c.cm_syscd = ?                                                                                       \n ");	 
			 }else{
				 strQuery.append(" and c.cm_syscd in (select distinct cm_syscd from cmm0044 where cm_userid = ?)                              \n ");	 
			 }
			 if(cboGbn.equals("00")){
				 strQuery.append("  and  b.cr_team = 'SYSTS' \n ");
			 }else if(cboGbn.equals("01")){
				 strQuery.append("  and  b.cr_team = 'SYSED' \n ");
			 }
			 strQuery.append("  and  a.CR_TEAMCD = d.CM_DEPTCD                                                                                \n ");
			 if (strDept!= "" && strSys != null){
		     strQuery.append("  and  a.CR_TEAMCD = ?  	                                                                                      \n ");
			 }
			 if(Admin.equals("N")){
				 strQuery.append("  and  a.cr_editor = ?	\n");			 
			 }			 
			 strQuery.append("  and  a.cr_editor = e.cm_userid                                                                                \n ");
			 strQuery.append("  and  b.cr_locat = '00'                                                                                        \n ");
			 strQuery.append("  and  b.cr_status = '0'                                                                                        \n ");
			 strQuery.append("  and  exists (select 'X'                                                                                       \n ");
			 strQuery.append("                 FROM CMR1010 D                                                                                 \n ");
			 strQuery.append("                     ,CMM0038 E                                                                                 \n ");
			 strQuery.append("                     ,CMM0036 F                                                                                 \n ");
			 strQuery.append("                WHERE D.CR_ACPTNO = A.CR_ACPTNO                                                                 \n ");
			 strQuery.append("                  AND D.CR_SYSCD  = E.CM_SYSCD                                                                  \n ");
			 strQuery.append("                  AND D.CR_RSRCCD = E.CM_RSRCCD                                                                 \n ");
			 strQuery.append("                  AND E.CM_SVRCD  = DECODE(b.CR_TEAM, 'SYSTS', '35', '05')                                      \n ");
			 strQuery.append("                  AND D.CR_SYSCD  = F.CM_SYSCD                                                                  \n ");
			 strQuery.append("                  AND D.CR_RSRCCD = F.CM_RSRCCD 	                                                              \n ");
			 strQuery.append("                  AND SUBSTR(F.CM_INFO, 54, 1) != '1'                                                           \n ");
			 strQuery.append("               )                                                                                                \n ");
			 strQuery.append(" ORDER  BY  c.cm_sysmsg, e.cm_username, a.cr_acptno                                                             \n ");
			pstmt = conn.prepareStatement(strQuery.toString());
			pstmt = new LoggableStatement(conn,strQuery.toString());			
			Cnt = 0;
	    
	        if (strSys!= "" && strSys != null) {
            	pstmt.setString(++Cnt, strSys);
            }else{
            	pstmt.setString(++Cnt, strUserId);
            }
			if (strDept!= "" && strSys != null)  {
            	pstmt.setString(++Cnt, strDept);
            }
			if(Admin.equals("N")){
				pstmt.setString(++Cnt, strUserId);
			}
			ecamsLogger.error(((LoggableStatement)pstmt).getQueryString());
            rs = pstmt.executeQuery();
            
			while (rs.next()){	
				rst = new HashMap<String, String>();
				rst.put("rows", Integer.toString(rs.getRow()));					
				rst.put("cm_sysmsg", rs.getString("cm_sysmsg"));     //시스템 
				rst.put("cm_deptname", rs.getString("CM_DEPTNAME")); //업무
				rst.put("cr_acptno", rs.getString("cr_acptno"));     //신청번호
				rst.put("cr_acptdate", rs.getString("cr_acptdate")); //신청일시
				rst.put("cr_sayu", rs.getString("cr_sayu"));         //신청사유
				rst.put("cr_editor", rs.getString("cm_username"));     //신청인  
				rst.put("cr_prcreq", rs.getString("cr_prcreq"));     //적용예정일시
				rst.put("cr_eddate", rs.getString("cr_eddate"));     //적용예정일시 
        		
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
			ecamsLogger.error("## Cmr3800.getFileList() SQLException START ##");
			ecamsLogger.error("## Error DESC : ", sqlexception);	
			ecamsLogger.error("## Cmr3800.getFileList() SQLException END ##");			
			throw sqlexception;
		} catch (Exception exception) {
			exception.printStackTrace();
			ecamsLogger.error("## Cmr3800.getFileList() Exception START ##");				
			ecamsLogger.error("## Error DESC : ", exception);	
			ecamsLogger.error("## Cmr3800.getFileList() Exception END ##");				
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
					ecamsLogger.error("## Cmr3800.getFileList() connection release exception ##");
					ex3.printStackTrace();
				}
			}
		}
	}//end of SelectList() method statement
    
    //두번째 그리드 조회
 public Object[] getFileList2(String acptno, String cboGbn) throws SQLException, Exception {
		
    	Connection        conn        = null;
		PreparedStatement pstmt       = null;
		PreparedStatement pstmt2      = null;
		ResultSet         rs          = null;
		ResultSet         rs2         = null;
		boolean           errSw       = false;
		StringBuffer      strQuery    = new StringBuffer();
		ArrayList<HashMap<String, String>>  rtList	= new ArrayList<HashMap<String, String>>();
		HashMap<String, String>			  	rst	 	= null;
        Object[]		 rtObj		  = null;	
        int              Cnt = 0;
        String           rtJson = "" ;
		ConnectionContext connectionContext = new ConnectionResource();
		try {            
			conn = connectionContext.getConnection();
			strQuery.setLength(0);
			strQuery.append(" select ROOT_RSRCNAME(a.cr_acptno, a.cr_itemid) as root_rsrcname,a.cr_rsrcname, b.cm_codename , c.cm_dirpath ,a.cr_deploy, a.cr_deployt, a.cr_deploym,   \n ");
			strQuery.append("        a.cr_acptno,a.cr_aplydate,a.cr_editcon,a.cr_prcdate,d.cr_qrycd, 	\n ");
			strQuery.append("        a.cr_status,a.cr_putcode,nvl(x.cnt,0) rst,a.cr_baseitem,nvl(z.cnt,0) basecnt,a.cr_itemid,nvl(u.cnt,0) baserst	 \n ");
			strQuery.append(" from cmr1010 a , cmm0020 b , cmm0070 c, cmr1000 d                	 		\n ");
			strQuery.append(" ,(select a.cr_baseitem,count(*) cnt from cmr1010 a    		     		\n");
			strQuery.append("    where a.cr_acptno=? and a.cr_status='0'                         		\n");
			strQuery.append("    group by a.cr_baseitem) z		   		                         		\n");
			strQuery.append(" ,(select cr_serno,count(*) cnt from cmr1011						 		\n");
			strQuery.append("    where cr_acptno=? and cr_prcdate is not null	                 		\n");
			strQuery.append("    group by cr_serno) x						                     		\n");
			strQuery.append(" ,(select b.cr_baseitem,count(*) cnt from cmr1011 a,cmr1010 b		 		\n");
			strQuery.append("    where a.cr_acptno=? and a.cr_prcdate is not null                		\n");
			strQuery.append("      and a.cr_acptno=b.cr_acptno and a.cr_serno=b.cr_serno         		\n");
			strQuery.append("    group by b.cr_baseitem) u		   		                         		\n");
			strQuery.append("    where a.cr_acptno = ?                                           		\n");
			strQuery.append("    and b.cm_macode = 'JAWON'                                       		\n");
			strQuery.append("    and a.cr_rsrccd = b.cm_micode                                   		\n");
			strQuery.append("    and a.cr_status != '3'		                                   	 		\n");
			strQuery.append("    and c.cm_syscd  = a.cr_syscd                                    		\n");
			strQuery.append("   and a.cr_serno=x.cr_serno(+)                                     		\n");
			strQuery.append("   and a.cr_itemid = z.cr_baseitem(+)                               		\n");
			strQuery.append("   and a.cr_itemid = u.cr_baseitem(+)                               		\n");
			strQuery.append("    and c.cm_dsncd  = a.cr_dsncd                                    		\n");
			strQuery.append("    and a.cr_acptno  = d.cr_acptno                                  		\n");
			strQuery.append("    and   exists (select  'X'                                        		\n");
			strQuery.append("                    FROM  CMR1010 D                                    	\n");
			strQuery.append("                        , CMM0038 E                                    	\n");
			strQuery.append("                        , CMM0036 F                                    	\n");
			strQuery.append("                        , CMR9900 G                                    	\n");
			strQuery.append("                   WHERE  D.CR_ACPTNO = A.CR_ACPTNO                    	\n");
			strQuery.append("                     AND  D.CR_SERNO  = A.CR_SERNO                     	\n");
			strQuery.append("                     AND  D.CR_SYSCD  = E.CM_SYSCD                      	\n");
			strQuery.append("                     AND  D.CR_RSRCCD = E.CM_RSRCCD                    	\n");
			strQuery.append("                     AND  D.CR_ACPTNO = G.CR_ACPTNO                    	\n");
			strQuery.append("                     AND  G.CR_LOCAT  = '00'                           	\n");
			strQuery.append("                     AND  E.CM_SVRCD  = DECODE(G.CR_TEAM, 'SYSTS', '35'	\n");
			strQuery.append("                                                                 , '05'	\n");
			strQuery.append("                                              )  							\n");
			strQuery.append("                     AND  D.CR_SYSCD  = F.CM_SYSCD                     	\n");
			strQuery.append("                     AND  D.CR_RSRCCD = F.CM_RSRCCD                    	\n");
			strQuery.append("                     AND  SUBSTR(F.CM_INFO, 54, 1) != '1'              	\n");
			strQuery.append("                     AND  (    G.CR_TEAM != 'SYSMCI'						\n");
			strQuery.append("                           OR  SUBSTR(F.CM_INFO, 59, 1) = '1'				\n");
			strQuery.append("                          )												\n");			 
			strQuery.append("                 )                                                  		\n");             
			strQuery.append(" order by NVL(ROOT_RSRCNAME(a.cr_acptno, a.cr_itemid), ' '), c.cm_dirpath, a.cr_rsrcname  \n");
			 
			pstmt = conn.prepareStatement(strQuery.toString());
			pstmt = new LoggableStatement(conn,strQuery.toString());
			pstmt.setString(1, acptno);
			pstmt.setString(2, acptno);
			pstmt.setString(3, acptno);
			pstmt.setString(4, acptno);
			ecamsLogger.error(((LoggableStatement)pstmt).getQueryString());
            rs = pstmt.executeQuery();

            while (rs.next()){
				errSw = false;
				String aplydate="";
				rst = new HashMap<String, String>();
				
				if(cboGbn.equals("00")){
					if (rs.getString("cr_deployt").equals("N")) {
						rtJson = "미배포";						
					}else if (rs.getString("cr_deployt").equals("Y")){
						rtJson = "배포요청완료";
					}else if (rs.getString("cr_deployt").equals("C")){
						rtJson = "배포완료";
					}
				}else if(cboGbn.equals("01")){
					if (rs.getString("cr_deploy").equals("N")) {
						rtJson = "미배포";
						
					}else if (rs.getString("cr_deploy").equals("Y")){
						if(rs.getString("cr_prcdate") != null && !rs.getString("cr_prcdate").equals("")){
							rtJson = "배포완료";
						}else{
							rtJson = "배포요청완료";
						}						
					}
				} else if(cboGbn.equals("02")){
					if (rs.getString("cr_deploym").equals("N")) {
						rtJson = "미배포";
						
					}else if (rs.getString("cr_deploym").equals("Y")){
						if(rs.getString("cr_prcdate") != null && !rs.getString("cr_prcdate").equals("")){
							rtJson = "배포완료";
						}else{
							rtJson = "배포요청완료";
						}						
					}
				} 
				
				rst.put("rows", Integer.toString(rs.getRow()));					
				rst.put("cr_acptno", rs.getString("cr_acptno"));     //신청번호
				rst.put("acptno2", rs.getString("cr_acptno"));
				rst.put("cr_rsrcname", rs.getString("cr_rsrcname"));     //자원명
				rst.put("cm_codename", rs.getString("cm_codename"));     //프로그램 종류 
				rst.put("cm_dirpath", rs.getString("cm_dirpath"));       //디렉토리 
				rst.put("cr_rsrcname2", rs.getString("root_rsrcname"));
				
				if(cboGbn.equals("00")){
					rst.put("cr_deploycd", rs.getString("cr_deployt"));       //
				}else if(cboGbn.equals("01")){
					rst.put("cr_deploycd", rs.getString("cr_deploy"));
				}
				
				rst.put("cr_editcon", rs.getString("cr_editcon"));
				rst.put("qrycd2", rs.getString("cr_qrycd"));
				rst.put("cr_deploy", rtJson);         //적용
				
				if(rs.getString("cr_aplydate") != null && !rs.getString("cr_aplydate").equals("")){
					aplydate=rs.getString("cr_aplydate").substring(0,4)+"/"+rs.getString("cr_aplydate").substring(4,6)+"/"+
					rs.getString("cr_aplydate").substring(6,8)+" "+rs.getString("cr_aplydate").substring(8,10)+":"+rs.getString("cr_aplydate").substring(10);
				}
				rst.put("cr_aplydate", aplydate);
				
				if (!rs.getString("cr_status").equals("3") && rs.getString("cr_baseitem") != null) {
					if (rs.getString("basecnt") != null) {
						if (rs.getInt("basecnt")>0) errSw = true;
					}
				}
								
				if (!rs.getString("cr_status").equals("3")) {
					if (rs.getString("cr_putcode") != null && !rs.getString("cr_putcode").equals("0000") && !rs.getString("cr_putcode").equals("RTRY")){
						rst.put("ColorSw","5");
					} //else if (errSw == true) rst.put("ColorSw","5");
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
			ecamsLogger.error("## Cmr3800.getFileList2() SQLException START ##");
			ecamsLogger.error("## Error DESC : ", sqlexception);	
			ecamsLogger.error("## Cmr3800.getFileList2() SQLException END ##");			
			throw sqlexception;
		} catch (Exception exception) {
			exception.printStackTrace();
			ecamsLogger.error("## Cmr3800.getFileList2() Exception START ##");				
			ecamsLogger.error("## Error DESC : ", exception);	
			ecamsLogger.error("## Cmr3800.getFileList2() Exception END ##");				
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
					ecamsLogger.error("## Cmr3800.getFileList2() connection release exception ##");
					ex3.printStackTrace();
				}
			}
		}
	}//end of SelectList() method statement
 public String BePoplay(ArrayList<HashMap<String,String>> chkInList, String cboGbn) throws SQLException, Exception {
		Connection        conn        = null;
		PreparedStatement pstmt       = null;
		ResultSet         rs          = null;
		StringBuffer      strQuery    = new StringBuffer();
		String            retMsg      = "0";
		String            strItemId   = null;
		boolean           insFg       = true;
		
		ConnectionContext connectionContext = new ConnectionResource();		
		
		try {
			conn = connectionContext.getConnection();
			conn.setAutoCommit(false);			
			
			
			for(int i =0;i<chkInList.size();i++){
		        strQuery.setLength(0);                		
	     		strQuery.append("update cmr1010            						\n");
	     		if(cboGbn.equals("00")){
					strQuery.append("set cr_deployt = 'Y'             				\n");	
				}else if(cboGbn.equals("01")){
					strQuery.append("set cr_deploy = 'Y'             				\n");
				}     		
	     		strQuery.append("where cr_acptno = ? and  cr_rsrcname = ?       \n");	     		      		
	     		pstmt = conn.prepareStatement(strQuery.toString());	
	       	    pstmt.setString(1, chkInList.get(i).get("cr_acptno"));         
	       	    pstmt.setString(2, chkInList.get(i).get("cr_rsrcname"));         
	       	     
	       		pstmt.executeUpdate();   
		        
		        pstmt.close();
			}
	 
	        conn.commit();
	        conn.close();
	        conn = null;
	        
	        return retMsg;
	        
	        
		} catch (SQLException sqlexception) {
			sqlexception.printStackTrace();
			if (rs != null)     try{rs.close();}catch (Exception ex){ex.printStackTrace();}
			if (pstmt != null)  try{pstmt.close();}catch (Exception ex2){ex2.printStackTrace();}
			if (conn != null){
				try{
					conn.rollback();
					ConnectionResource.release(conn);
				}catch(Exception ex3){
					ecamsLogger.error("## Cmr0200.cmr0020_Insert() connection release exception ##");
					ex3.printStackTrace();
				}				
			}			
			ecamsLogger.error("## Cmr0200.cmr0020_Insert() SQLException START ##");
			ecamsLogger.error("## Error DESC : ", sqlexception);	
			ecamsLogger.error("## Cmr0200.cmr0020_Insert() SQLException END ##");			
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
					ecamsLogger.error("## Cmr0200.cmr0020_Insert() connection release exception ##");
					ex3.printStackTrace();
				}				
			}			
			ecamsLogger.error("## Cmr0200.cmr0020_Insert() Exception START ##");				
			ecamsLogger.error("## Error DESC : ", exception);	
			ecamsLogger.error("## Cmr0200.cmr0020_Insert() Exception END ##");				
			throw exception;
		}finally{
			if (strQuery != null) 	strQuery = null;
			if (rs != null)     try{rs.close();}catch (Exception ex){ex.printStackTrace();}
			if (pstmt != null)  try{pstmt.close();}catch (Exception ex2){ex2.printStackTrace();}
			if (conn != null){
				try{
					conn.commit();
					conn.close();
					conn = null;
				}catch(Exception ex3){
					ecamsLogger.error("## Cmr0200.cmr0020_Insert() connection release exception ##");
					ex3.printStackTrace();
				}
			}
		}
	}//end of cmr0020_Insert() method statement
 
 
 public String BePoplay1(ArrayList<HashMap<String,String>> chkInList, String cboGbn) throws SQLException, Exception {
		Connection        conn        = null;
		PreparedStatement pstmt       = null;
		ResultSet         rs          = null;
		StringBuffer      strQuery    = new StringBuffer();
		String            retMsg      = "0";
		String            strItemId   = null;
		boolean           insFg       = true;
		
		ConnectionContext connectionContext = new ConnectionResource();		
		
		try {
			conn = connectionContext.getConnection();
			conn.setAutoCommit(false);			
			
			
			for(int i =0;i<chkInList.size();i++){
		        strQuery.setLength(0);                		
	     		strQuery.append("update cmr1010            							\n");
	     		if(cboGbn.equals("00")){
					strQuery.append("set cr_deployt = 'Y'             				\n");	
				}else if(cboGbn.equals("01")){
					strQuery.append("set cr_deploy  = 'Y'             				\n");
				}else if(cboGbn.equals("02")){
					strQuery.append("set cr_deploym  = 'Y'             				\n");
				}      		
	     		strQuery.append("where cr_acptno = ?                                \n");	     		      		
	     		if(cboGbn.equals("00")){
					strQuery.append(" and cr_deployt = 'N'             				\n");	
				}else if(cboGbn.equals("01")){
					strQuery.append(" and cr_deploy  = 'N'             				\n");
				}else if(cboGbn.equals("02")){
					strQuery.append(" and cr_deploym  = 'N'             				\n");
				}       		
	     		pstmt = conn.prepareStatement(strQuery.toString());	
	       	    pstmt.setString(1, chkInList.get(i).get("cr_acptno"));  
	       		pstmt.executeUpdate();  		        
		        pstmt.close();
			}	 
	        conn.commit();
	        conn.close();
	        conn = null;
	        
	        return retMsg;
	        
	        
		} catch (SQLException sqlexception) {
			sqlexception.printStackTrace();
			if (rs != null)     try{rs.close();}catch (Exception ex){ex.printStackTrace();}
			if (pstmt != null)  try{pstmt.close();}catch (Exception ex2){ex2.printStackTrace();}
			if (conn != null){
				try{
					conn.rollback();
					ConnectionResource.release(conn);
				}catch(Exception ex3){
					ecamsLogger.error("## Cmr3800.BePoplay1() connection release exception ##");
					ex3.printStackTrace();
				}				
			}			
			ecamsLogger.error("## Cmr3800.BePoplay1() SQLException START ##");
			ecamsLogger.error("## Error DESC : ", sqlexception);	
			ecamsLogger.error("## Cmr3800.BePoplay1() SQLException END ##");			
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
					ecamsLogger.error("## Cmr3800.BePoplay1() connection release exception ##");
					ex3.printStackTrace();
				}				
			}			
			ecamsLogger.error("## Cmr3800.BePoplay1() Exception START ##");				
			ecamsLogger.error("## Error DESC : ", exception);	
			ecamsLogger.error("## Cmr3800.BePoplay1() Exception END ##");				
			throw exception;
		}finally{
			if (strQuery != null) 	strQuery = null;
			if (rs != null)     try{rs.close();}catch (Exception ex){ex.printStackTrace();}
			if (pstmt != null)  try{pstmt.close();}catch (Exception ex2){ex2.printStackTrace();}
			if (conn != null){
				try{
					conn.commit();
					conn.close();
					conn = null;
				}catch(Exception ex3){
					ecamsLogger.error("## Cmr3800.BePoplay1() connection release exception ##");
					ex3.printStackTrace();
				}
			}
		}
	}//end of BePoplay1() method statement

 
 
	 public Object[] get_node(String SysCd) throws SQLException, Exception{
		Connection        conn        = null;
		PreparedStatement pstmt       = null;
		ResultSet         rs          = null;
		StringBuffer      strQuery    = new StringBuffer();
		ArrayList<HashMap<String, String>>  rsval  = new ArrayList<HashMap<String, String>>();
		HashMap<String, String>			  rst		  = null;		
		ConnectionContext connectionContext = new ConnectionResource();
		
		
		
		try {
			conn = connectionContext.getConnection();
			rst = new HashMap<String, String>();
			strQuery.setLength(0);
			strQuery.append("select cm_svrname,cm_svrip,cm_portno, rpad(cm_svrip, 20, ' ') || rpad(cm_portno, 7, ' ') || cm_svrname as fullname    \n");
			strQuery.append("from cmm0031      	 	\n");
			strQuery.append("where cm_syscd = ?     \n");			
			strQuery.append("and cm_svrcd = '05'    \n");
			
			pstmt = conn.prepareStatement(strQuery.toString());					
			//pstmt = new LoggableStatement(conn,strQuery.toString());			
			pstmt.setString(1,SysCd);			
	        //ecamsLogger.error(((LoggableStatement)pstmt).getQueryString());           
	        rs = pstmt.executeQuery();
	        rsval.clear();
	        while (rs.next()){
					rst = new HashMap<String,String>();
					rst.put("cm_svrname", rs.getString("cm_svrname"));
					rst.put("cm_svrip", rs.getString("cm_svrip"));
					rst.put("cm_portno", rs.getString("cm_portno"));
					rst.put("fullname", rs.getString("fullname"));
					rsval.add(rst);
					rst = null;
				}
			rs.close();
			pstmt.close();			
			conn.close();
			
			conn = null;
			pstmt = null;
			rs = null;
			
			return rsval.toArray();			
			
		} catch (SQLException sqlexception) {
			sqlexception.printStackTrace();
			ecamsLogger.error("## Cmr3800.get_node() SQLException START ##");
			ecamsLogger.error("## Error DESC : ", sqlexception);	
			ecamsLogger.error("## Cmr3800.get_node() SQLException END ##");			
			throw sqlexception;
		} catch (Exception exception) {
			exception.printStackTrace();
			ecamsLogger.error("## Cmr3800.get_node() Exception START ##");				
			ecamsLogger.error("## Error DESC : ", exception);	
			ecamsLogger.error("## Cmr3800.get_node() Exception END ##");				
			throw exception;
		}finally{
			if (strQuery != null) 	strQuery = null;
			if (rs != null)     try{rs.close();}catch (Exception ex){ex.printStackTrace();}
			if (pstmt != null)  try{pstmt.close();}catch (Exception ex2){ex2.printStackTrace();}
			if (conn != null){
				try{
					ConnectionResource.release(conn);
				}catch(Exception ex3){
					ecamsLogger.error("## Cmr3800.get_node() connection release exception ##");
					ex3.printStackTrace();
				}
			}
		}
	}//end of get_node() method statement
	 
	 public Object[] get_Instance() throws SQLException, Exception{
		Connection        conn        = null;
		PreparedStatement pstmt       = null;
		ResultSet         rs          = null;
		StringBuffer      strQuery    = new StringBuffer();
		ArrayList<HashMap<String, String>>  rsval  = new ArrayList<HashMap<String, String>>();
		HashMap<String, String>			  rst		  = null;		
		ConnectionContext connectionContext = new ConnectionResource();		
		
		
		try {
			conn = connectionContext.getConnection();
			rst = new HashMap<String, String>();
			strQuery.setLength(0);
		
			strQuery.append("select cm_macode, cm_micode, cm_codename from cmm0020		\n");
			strQuery.append("where cm_macode = 'INSTANCE'      							\n");
			strQuery.append("and cm_micode >= '00'     									\n");			
			
			pstmt = conn.prepareStatement(strQuery.toString());					
	//		pstmt = new LoggableStatement(conn,strQuery.toString());						
	 //       //ecamsLogger.error(((LoggableStatement)pstmt).getQueryString());           
	        rs = pstmt.executeQuery();
	        rsval.clear();
	        
	        while (rs.next()){
					rst = new HashMap<String,String>();
					rst.put("cm_macode", rs.getString("cm_macode"));
					rst.put("cm_micode", rs.getString("cm_micode"));
					rst.put("cm_codename", rs.getString("cm_codename"));
					rsval.add(rst);
					rst = null;
			}
	        
			rs.close();
			pstmt.close();			
			conn.close();
			
			conn = null;
			pstmt = null;
			rs = null;
			
			return rsval.toArray();			
			
		} catch (SQLException sqlexception) {
			sqlexception.printStackTrace();
			ecamsLogger.error("## Cmr3800.get_Instance() SQLException START ##");
			ecamsLogger.error("## Error DESC : ", sqlexception);	
			ecamsLogger.error("## Cmr3800.get_Instance() SQLException END ##");			
			throw sqlexception;
		} catch (Exception exception) {
			exception.printStackTrace();
			ecamsLogger.error("## Cmr3800.get_Instance() Exception START ##");				
			ecamsLogger.error("## Error DESC : ", exception);	
			ecamsLogger.error("## Cmr3800.get_Instance() Exception END ##");				
			throw exception;
		}finally{
			if (strQuery != null) 	strQuery = null;
			if (rs != null)     try{rs.close();}catch (Exception ex){ex.printStackTrace();}
			if (pstmt != null)  try{pstmt.close();}catch (Exception ex2){ex2.printStackTrace();}
			if (conn != null){
				try{
					ConnectionResource.release(conn);
				}catch(Exception ex3){
					ecamsLogger.error("## Cmr3800.get_Instance() connection release exception ##");
					ex3.printStackTrace();
				}
			}
		}
	}//end of get_Instance() method statement
 
	 public void cmd7000_INSERT(String node, String svrip, String portno,String svrstart,String svrstop,
		      String stopck,String startck,String instancecd,String instanceid,String reqcd,String runcd,String userid,String strchk,String health) throws SQLException, Exception{
		Connection        conn        = null;
		PreparedStatement pstmt       = null;
		StringBuffer      strQuery    = new StringBuffer();
		ResultSet         rs          = null;
		ConnectionContext connectionContext = new ConnectionResource();
		int 			  temp     	  = 0;

		try {
			conn = connectionContext.getConnection();
			conn.setAutoCommit(false);
			strQuery.setLength(0);
			strQuery.append("select count(*)as cnt from cmd7000    \n");
			strQuery.append("where cd_node= ?			     \n");
			strQuery.append("and cd_reqcd= ?  	 		\n");
			strQuery.append("and cd_runcd= ? 				 \n");
			strQuery.append("and cd_instancecd= ?          \n");
			strQuery.append("and cd_instanceid= ?          \n");
			strQuery.append("and cd_svrip= ?          \n");
			
			pstmt = conn.prepareStatement(strQuery.toString());		
			//pstmt = new LoggableStatement(conn,strQuery.toString());	
			pstmt.setString(1, node);
			pstmt.setString(2, reqcd);
			pstmt.setString(3, runcd);
			pstmt.setString(4, instancecd);
			pstmt.setString(5, instanceid);
			pstmt.setString(6, svrip);
			//ecamsLogger.error(((LoggableStatement)pstmt).getQueryString());	
		    rs = pstmt.executeQuery();	
	    	while (rs.next()){	
	    		temp = rs.getInt("cnt");
	    	}
	    	if(temp == 0){
	    		strQuery.setLength(0);			
				strQuery.append("insert into cmd7000(cd_node,cd_svrip,cd_portno,cd_svrstart,cd_svrstop, 	\n");
				strQuery.append("CD_STARTCK,CD_STOPCK,CD_INSTANCECD,CD_INSTANCEID,CD_REQCD,CD_RUNCD,CD_CREATDT,CD_LASTUPDT,CD_EDITOR,cd_runtype,cd_healthck) 	\n");
				strQuery.append("values(?,?,?,?,?,?,?,?,?,?,?,sysdate,sysdate,?,?,?) 															\n"); 				
				pstmt = conn.prepareStatement(strQuery.toString());
				pstmt = new LoggableStatement(conn,strQuery.toString());
		    	pstmt.setString(1, node);
		    	pstmt.setString(2, svrip);
		    	pstmt.setString(3, portno);
		    	pstmt.setString(4, svrstart);
		    	pstmt.setString(5, svrstop);
		    	pstmt.setString(6, startck);
		    	pstmt.setString(7, stopck);
		    	pstmt.setString(8, instancecd);
		    	pstmt.setString(9, instanceid);
		    	pstmt.setString(10, reqcd);
		    	pstmt.setString(11, runcd);
		    	pstmt.setString(12, userid);
		    	pstmt.setString(13, strchk);
		    	pstmt.setString(14, health);
		    	ecamsLogger.error(((LoggableStatement)pstmt).getQueryString());
		    	pstmt.executeUpdate();
		    	
	    	}else if(temp == 1){
				strQuery.setLength(0);
				strQuery.append("update cmd7000 set cd_closedt = null,cd_lastupdt = sysdate,cd_portno=?,cd_svrstart=?,   \n");
				strQuery.append("cd_svrstop=?,CD_STARTCK=?,CD_STOPCK=?,CD_EDITOR=?,cd_runtype=?,cd_healthck=?		   \n");
				strQuery.append("where cd_node= ?			     \n");
				strQuery.append("and cd_reqcd= ?  	 		\n");
				strQuery.append("and cd_runcd= ? 				 \n");
				strQuery.append("and cd_instancecd= ?          \n");
				strQuery.append("and cd_instanceid= ?          \n");
				strQuery.append("and cd_svrip= ?          \n");
	     		pstmt = conn.prepareStatement(strQuery.toString());
	     		//pstmt = new LoggableStatement(conn,strQuery.toString());
				pstmt.setString(1, portno);
				pstmt.setString(2, svrstart);
				pstmt.setString(3, svrstop);
				pstmt.setString(4, startck);
				pstmt.setString(5, stopck);
				pstmt.setString(6, userid);
				pstmt.setString(7, strchk);
				pstmt.setString(8, health);
				pstmt.setString(9, node);
				pstmt.setString(10, reqcd);
				pstmt.setString(11, runcd);
				pstmt.setString(12, instancecd);
				pstmt.setString(13, instanceid);
				pstmt.setString(14, svrip);
				//ecamsLogger.error(((LoggableStatement)pstmt).getQueryString());
	       		pstmt.executeUpdate();  
	       		
	    	}
		    pstmt.close();
			conn.commit();
			conn.close();		
			pstmt = null;
			conn = null;	
		    
		} catch (SQLException sqlexception) {
			sqlexception.printStackTrace();
			if (pstmt != null)  try{pstmt.close();}catch (Exception ex2){ex2.printStackTrace();}
			if (conn != null){
				try{
					conn.rollback();
					ConnectionResource.release(conn);
				}catch(Exception ex3){
					ecamsLogger.error("## Cmr3800.cmd7000_INSERT() connection release exception ##");
					ex3.printStackTrace();
				}				
			}
			ecamsLogger.error("## Cmr3800.cmd7000_INSERT() SQLException START ##");
			ecamsLogger.error("## Error DESC : ", sqlexception);	
			ecamsLogger.error("## Cmr3800.cmd7000_INSERT() SQLException END ##");			
			throw sqlexception;
		} catch (Exception exception) {
			exception.printStackTrace();
			if (pstmt != null)  try{pstmt.close();}catch (Exception ex2){ex2.printStackTrace();}
			if (conn != null){
				try{
					conn.rollback();
					ConnectionResource.release(conn);
				}catch(Exception ex3){
					ecamsLogger.error("## Cmr3800.cmd7000_INSERT() connection release exception ##");
					ex3.printStackTrace();
				}				
			}			
			ecamsLogger.error("## Cmr3800.cmd7000_INSERT() Exception START ##");				
			ecamsLogger.error("## Error DESC : ", exception);	
			ecamsLogger.error("## Cmr3800.cmd7000_INSERT() Exception END ##");				
			throw exception;
		}finally{
			if (strQuery != null)	strQuery = null;
			if (pstmt != null)  try{pstmt.close();}catch (Exception ex2){ex2.printStackTrace();}
			if (conn != null){
				try{
					ConnectionResource.release(conn);
				}catch(Exception ex3){
					ecamsLogger.error("## Cmr3800.cmd7000_INSERT() connection release exception ##");
					ex3.printStackTrace();
				}
			}
		}
	}//end of cmd7000_INSERT() method statement		 
	public String cmr7000_INSERT(ArrayList<HashMap<String, String>>  getCm7000List) throws SQLException, Exception{
		Connection         conn        	= null;
		PreparedStatement  pstmt       	= null;
		StringBuffer       strQuery    	= new StringBuffer();
		ConnectionContext  connectionContext = new ConnectionResource();
		
		SystemPath		   cTempGet	    = new SystemPath();
		String			   tmpPath 		= "";
		String			   strBinPath 	= "";
		String  		   shFileName 	= "";
		File 			   shfile		= null;
		OutputStreamWriter writer 		= null;
		String[] 		   strAry 		= null;
		Runtime  		   run 			= null;
		Process 		   p 			= null;
		String rtString = "";
		ResultSet         rs          = null;
		try {
			conn = connectionContext.getConnection();
			conn.setAutoCommit(false);
			
			SimpleDateFormat formatter = new SimpleDateFormat ("yyyyMMddHHmmss");
			Calendar cal = Calendar.getInstance();
			String today = formatter.format(cal.getTime());
			
			int i=0;
			String retMsg = "";
			
			ecamsLogger.error("++++++cmr7000_INSERT LIST SIZE : "+getCm7000List.size());
			for(i=0;i<getCm7000List.size();i++){
				strQuery.setLength(0);	
				strQuery.append("insert into cmr7000																					\n");
				strQuery.append("(cr_acptdate, cr_node, cr_instancecd, cr_instanceid, cr_trcontrol, cr_refresh, cr_instance, cr_editor)	\n");
				strQuery.append("values(?, ?, ?, ?, ?, ?, ?, ?)												 							\n");
				pstmt = conn.prepareStatement(strQuery.toString());
		    	pstmt.setString(1, today);
		    	pstmt.setString(2, getCm7000List.get(i).get("node"));
		    	pstmt.setString(3, getCm7000List.get(i).get("instancecd"));
		    	pstmt.setString(4, getCm7000List.get(i).get("instanceid"));
		    	pstmt.setString(5, getCm7000List.get(i).get("trcontrol"));
		    	pstmt.setString(6, getCm7000List.get(i).get("refresh"));
		    	pstmt.setString(7, getCm7000List.get(i).get("instance"));
		    	pstmt.setString(8, getCm7000List.get(i).get("editor"));
		    	pstmt.executeUpdate();
			    pstmt.close();
			}
			
			conn.commit();
			conn.close();		
			pstmt = null;
			conn = null;
			

			ecamsLogger.error("++++++cmr7000_INSERT RETURN ACPTDATE : "+today);
			
			tmpPath = cTempGet.getTmpDir("99");
			strBinPath = cTempGet.getTmpDir("14");
			
			shFileName = tmpPath + "/" + today; 
									
			shfile = new File(shFileName);
			
			if( !(shfile.isFile()) ){
				shfile.createNewFile();
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
			//writer.write("./ecams_trcontrol.sh " + today + " &\n");
//			writer.write("./ecams_trcontrol " + today + " &\n");
			writer.write("./ecams_batexec " +rtString + " \"cd " + strBinPath+ ";" +"./ecams_trcontrol " + today + " &\" \n");
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
			
			if (p.exitValue() != 0) {
				ecamsLogger.error("++++++cmr7000_INSERT RETURN ERROR");
				retMsg = "ERR";
			} else {
				retMsg = today;
			}
			
			pstmt.close();
			conn.close();
			pstmt = null;
			conn = null;
			rs.close();// 20221219 ecams_batexec 추가 쿼리
			rs = null;	
			return retMsg;
			
		} catch (SQLException sqlexception) {
			sqlexception.printStackTrace();
			if (pstmt != null)  try{pstmt.close();}catch (Exception ex2){ex2.printStackTrace();}
			if (conn != null){
				try{
					conn.rollback();
					ConnectionResource.release(conn);
				}catch(Exception ex3){
					ecamsLogger.error("## Cmr3800.cmr7000_INSERT() connection release exception ##");
					ex3.printStackTrace();
				}				
			}
			ecamsLogger.error("## Cmr3800.cmr7000_INSERT() SQLException START ##");
			ecamsLogger.error("## Error DESC : ", sqlexception);	
			ecamsLogger.error("## Cmr3800.cmr7000_INSERT() SQLException END ##");			
			throw sqlexception;
		} catch (Exception exception) {
			exception.printStackTrace();
			if (pstmt != null)  try{pstmt.close();}catch (Exception ex2){ex2.printStackTrace();}
			if (conn != null){
				try{
					conn.rollback();
					ConnectionResource.release(conn);
				}catch(Exception ex3){
					ecamsLogger.error("## Cmr3800.cmr7000_INSERT() connection release exception ##");
					ex3.printStackTrace();
				}				
			}			
			ecamsLogger.error("## Cmr3800.cmr7000_INSERT() Exception START ##");				
			ecamsLogger.error("## Error DESC : ", exception);	
			ecamsLogger.error("## Cmr3800.cmr7000_INSERT() Exception END ##");				
			throw exception;
		}finally{
			if (strQuery != null)	strQuery = null;
			if (pstmt != null)  try{pstmt.close();}catch (Exception ex2){ex2.printStackTrace();}
			if (conn != null){
				try{
					ConnectionResource.release(conn);
				}catch(Exception ex3){
					ecamsLogger.error("## Cmr3800.cmr7000_INSERT() connection release exception ##");
					ex3.printStackTrace();
				}
			}
		}
	}//end of cmr7000_INSERT() method statement
	 
	 public Object[] cmd7000_SELECT(String node,String instancecd,String instanceid,String reqcd,String runcd) throws SQLException, Exception{
		Connection        conn        = null;
		PreparedStatement pstmt       = null;		
		ResultSet         rs          = null;		
		StringBuffer      strQuery    = new StringBuffer();
		ArrayList<HashMap<String, String>>  rsval  = new ArrayList<HashMap<String, String>>();
		HashMap<String, String>			  rst		  = null;		
		ConnectionContext connectionContext = new ConnectionResource();	
		int cnt = 1;
		try {
			conn = connectionContext.getConnection();   
			
	    	strQuery.setLength(0);
			strQuery.append("select b.cm_codename as runcd,c.cm_codename as instanceid,d.cm_codename as reqcd,e.cm_codename as instancecd,     \n");
			strQuery.append("f.cm_codename as node,a.cd_svrip,a.cd_portno,a.cd_svrstart,a.cd_svrstop,     \n");
			strQuery.append(" a.CD_STARTCK,a.CD_STOPCK,a.CD_CREATDT,a.CD_LASTUPDT,a.CD_EDITOR,a.cd_runtype,  	 \n");
			strQuery.append(" a.cd_node,a.cd_reqcd,a.cd_runcd,a.cd_instanceid,a.cd_instancecd,a.cd_healthck  	 \n");
			strQuery.append("from cmd7000 a,cmm0020 b,cmm0020 c,cmm0020 d,cmm0020 e,cmm0020 f \n");
			strQuery.append("where  f.cm_macode = 'CMR7000ND'                                  \n");
			strQuery.append("and f.cm_micode = a.cd_node                                 	   \n");
			strQuery.append("and  b.cm_macode = 'CMR7000GBN'                                  \n");
			strQuery.append("and b.cm_micode = a.cd_runcd                                     \n");
			strQuery.append("and b.cm_closedt is null                                   	  \n");
			strQuery.append("and c.cm_macode = 'CMR7000ID'                                    \n");
			strQuery.append("and c.cm_micode = a.cd_instanceid                                \n");
			strQuery.append("and c.cm_closedt is null                                  		   \n");
			strQuery.append("and d.cm_macode = 'CMR7000JOB'                                   \n");
			strQuery.append("and d.cm_micode = a.cd_reqcd                                     \n");
			strQuery.append("and d.cm_closedt is null                               	      \n");
			strQuery.append("and e.cm_macode = 'INSTANCE'                                     \n");
			strQuery.append("and e.cm_micode = a.cd_instancecd                                \n");
			strQuery.append("and e.cm_closedt is null                                	     \n");
			if(!node.equals("00")){
				strQuery.append("and a.cd_node = ?           								 \n");              
			}
			if(!instancecd.equals("00")){
				strQuery.append("and a.cd_instancecd = ?           								 \n");              
			}
			if(!instanceid.equals("00")){
				strQuery.append("and a.cd_instanceid = ?           								 \n");              
			}
			if(!reqcd.equals("00")){
				strQuery.append("and a.cd_reqcd = ?           								 \n");              
			}
			if(!runcd.equals("00")){
				strQuery.append("and a.cd_runcd = ?           								 \n");              
			}
			strQuery.append("and CD_CLOSEDT is null 											 \n");
			strQuery.append("order by a.cd_node,a.cd_runcd,a.cd_reqcd,e.cm_seqno				 \n");
			
			pstmt = conn.prepareStatement(strQuery.toString());
			if(!node.equals("00")){
				pstmt.setString(cnt++, node);              
			}
			if(!instancecd.equals("00")){
				pstmt.setString(cnt++, instancecd);              
			}
			if(!instanceid.equals("00")){
				pstmt.setString(cnt++, instanceid);              
			}
			if(!reqcd.equals("00")){
				pstmt.setString(cnt++, reqcd);              
			}
			if(!runcd.equals("00")){
				pstmt.setString(cnt++, runcd);              
			}
		    rs = pstmt.executeQuery();	
	    			    	
		    rsval.clear();	
	
	    	while (rs.next()){	
	    		rst = new HashMap<String,String>();			    	
				rst.put("SYSMSG", rs.getString("node"));
				rst.put("runcd", rs.getString("runcd"));		
				rst.put("instanceid", rs.getString("instanceid"));		
				rst.put("reqcd", rs.getString("reqcd"));
				rst.put("instancecd", rs.getString("instancecd"));
				rst.put("cd_svrip", rs.getString("cd_svrip"));
				rst.put("cd_portno", rs.getString("cd_portno"));
				rst.put("cd_svrstart", rs.getString("cd_svrstart"));
				rst.put("cd_svrstop", rs.getString("cd_svrstop"));
				rst.put("CD_STARTCK", rs.getString("CD_STARTCK"));
				rst.put("CD_STOPCK", rs.getString("CD_STOPCK"));
				rst.put("CD_CREATDT", rs.getString("CD_CREATDT"));
				rst.put("CD_LASTUPDT", rs.getString("CD_LASTUPDT"));
				rst.put("CD_EDITOR", rs.getString("CD_EDITOR"));
				rst.put("cd_node", rs.getString("cd_node"));
				rst.put("cd_instancecd", rs.getString("cd_instancecd"));
				rst.put("cd_instanceid", rs.getString("cd_instanceid"));
				rst.put("cd_reqcd", rs.getString("cd_reqcd"));
				rst.put("cd_runcd", rs.getString("cd_runcd"));
				rst.put("cd_runtype", rs.getString("cd_runtype"));
				rst.put("cd_healthck", rs.getString("cd_healthck"));
				rsval.add(rst);
				rst = null;						
			}		    	
			rs.close();				
			pstmt.close();
			conn.close();
			
			conn = null;
			pstmt = null;				
			rs = null;
			
			return rsval.toArray();			
			
		} catch (SQLException sqlexception) {
			sqlexception.printStackTrace();
			ecamsLogger.error("## Cmr3800.cmd7000_SELECT() SQLException START ##");
			ecamsLogger.error("## Error DESC : ", sqlexception);	
			ecamsLogger.error("## Cmr3800.cmd7000_SELECT() SQLException END ##");			
			throw sqlexception;
		} catch (Exception exception) {
			exception.printStackTrace();
			ecamsLogger.error("## Cmr3800.cmd7000_SELECT() Exception START ##");				
			ecamsLogger.error("## Error DESC : ", exception);	
			ecamsLogger.error("## Cmr3800.cmd7000_SELECT() Exception END ##");				
			throw exception;
		}finally{
			if (strQuery != null) 	strQuery = null;
			if (rs != null)     try{rs.close();}catch (Exception ex){ex.printStackTrace();}
			if (pstmt != null)  try{pstmt.close();}catch (Exception ex2){ex2.printStackTrace();}
			if (conn != null){
				try{
					ConnectionResource.release(conn);
				}catch(Exception ex3){
					ecamsLogger.error("## Cmr3800.cmd7000_SELECT() connection release exception ##");
					ex3.printStackTrace();
				}
			}
		}
	}//end of cmd7000_SELECT() method statement
	 
	 public Object[] cmd7000_SELECT1(String acptdate,String node , String instance) throws SQLException, Exception{
		Connection        conn        = null;
		PreparedStatement pstmt       = null;		
		ResultSet         rs          = null;				
		StringBuffer      strQuery    = new StringBuffer();
		ArrayList<HashMap<String, String>>  rsval  = new ArrayList<HashMap<String, String>>();
		HashMap<String, String>			  rst		  = null;	
		int				cnt           = 0;
		ConnectionContext connectionContext = new ConnectionResource();				
		try {
			conn = connectionContext.getConnection(); 				
			if(instance.equals("00")){					
				strQuery.append("SELECT  CD_NODE, d.cm_codename as instancecd, CD_INSTANCECD,																			\n");
				strQuery.append("SUM(T1), SUM(T2) , SUM(R1), SUM(R2), SUM(R3), SUM(I1),                                                                                   \n");
				strQuery.append("TRCONTROL_CK(?, CD_NODE ,CD_INSTANCECD,'01','TRCONTROL', 'ON' )  AS TC1ON,												 \n");
				strQuery.append("TRCONTROL_CK(?, CD_NODE ,CD_INSTANCECD,'01','TRCONTROL', 'OFF') AS TC1OFF,	                                                      \n");										
				strQuery.append("TRCONTROL_CK(?, CD_NODE ,CD_INSTANCECD,'02','TRCONTROL', 'ON') AS TC2ON,                                                    \n");
				strQuery.append("TRCONTROL_CK(?, CD_NODE ,CD_INSTANCECD,'02','TRCONTROL', 'OFF') AS TC2OFF,                                                      \n");
				strQuery.append("TRCONTROL_CK(?, CD_NODE ,CD_INSTANCECD,'01','INSTANCE', 'ON') AS IC1ON,                                                      \n");
				strQuery.append("TRCONTROL_CK(?, CD_NODE ,CD_INSTANCECD,'01','INSTANCE', 'OFF') AS IC1OFF,                                                      \n");
				strQuery.append("TRCONTROL_CK(?, CD_NODE ,CD_INSTANCECD,'01','REFRESH', 'ON') AS RC1ON,                                                      \n");
				strQuery.append("TRCONTROL_CK(?, CD_NODE ,CD_INSTANCECD,'02','REFRESH', 'ON') AS RC2ON,                                                      \n");
				strQuery.append("TRCONTROL_CK(?, CD_NODE ,CD_INSTANCECD,'03','REFRESH', 'ON') AS RC3ON                                                      \n");
				
				strQuery.append("FROM (                                                                                                                         \n");
				strQuery.append("        SELECT CD_NODE, CD_REQCD, CD_RUNCD, CD_INSTANCECD, 1 AS T1, 0 AS T2, 0 AS R1, 0 AS R2, 0 AS R3,0 AS I1 FROM CMD7000            \n");              
				strQuery.append("        WHERE CD_NODE = ?                                                                                                   \n");
				strQuery.append("        AND CD_CLOSEDT IS NULL                                                                                                 \n");
				strQuery.append("        AND CD_REQCD = '01'                                                                                                    \n");
				strQuery.append("        AND CD_INSTANCEID = '01'                                                                                               \n");
				strQuery.append("        UNION ALL                                                                                                              \n");
				strQuery.append("        SELECT CD_NODE,CD_REQCD, CD_RUNCD, CD_INSTANCECD, 0 AS T1, 1  AS T2, 0 AS R1, 0 AS R2, 0 AS R3,0 AS I1 FROM CMD7000            \n");              
				strQuery.append("        WHERE CD_NODE = ?                                                                                                   \n");
				strQuery.append("        AND CD_CLOSEDT IS NULL                                                                                                 \n");
				strQuery.append("        AND CD_REQCD = '01'                                                                                                    \n");
				strQuery.append("        AND CD_INSTANCEID = '02'                                                                                               \n");
				strQuery.append("        UNION ALL                                                                                                              \n");
				strQuery.append("        SELECT CD_NODE, CD_REQCD, CD_RUNCD, CD_INSTANCECD, 0 AS T1, 0 AS T2, 1 AS R1, 0 AS R2, 0 AS R3,0 AS I1 FROM CMD7000            \n");              
				strQuery.append("        WHERE CD_NODE = ?                                                                                                   \n");
				strQuery.append("        AND CD_CLOSEDT IS NULL                                                                                                 \n");
				strQuery.append("        AND CD_REQCD = '02'                                                                                                    \n");
				strQuery.append("        AND CD_INSTANCEID = '01'                                                                                               \n");
				strQuery.append("        UNION ALL                                                                                                              \n");
				strQuery.append("        SELECT CD_NODE, CD_REQCD, CD_RUNCD, CD_INSTANCECD, 0 AS T1, 0 AS T2, 0 AS R1, 0 AS R2, 0 AS R3,1 AS I1 FROM CMD7000            \n");              
				strQuery.append("        WHERE CD_NODE = ?                                                                                                   \n");
				strQuery.append("        AND CD_CLOSEDT IS NULL                                                                                                 \n");
				strQuery.append("        AND CD_REQCD = '03'                                                                                                    \n");
				strQuery.append("        AND CD_INSTANCEID = '01'                                                                                               \n");
				strQuery.append("        UNION ALL                                                                                                              \n");
				strQuery.append("        SELECT CD_NODE,CD_REQCD, CD_RUNCD, CD_INSTANCECD, 0 AS T1, 0  AS T2, 0 AS R1, 1 AS R2, 0 AS R3,0 AS I1 FROM CMD7000            \n");              
				strQuery.append("        WHERE CD_NODE = ?                                                                                                   \n");
				strQuery.append("        AND CD_CLOSEDT IS NULL                                                                                                 \n");
				strQuery.append("        AND CD_REQCD = '02'                                                                                                    \n");
				strQuery.append("        AND CD_INSTANCEID = '02'                                                                                               \n");
				
				strQuery.append("        UNION ALL                                                                                                              \n");
				strQuery.append("        SELECT CD_NODE,CD_REQCD, CD_RUNCD, CD_INSTANCECD, 0 AS T1, 0  AS T2, 0 AS R1, 0 AS R2, 1 AS R3,0 AS I1 FROM CMD7000            \n");              
				strQuery.append("        WHERE CD_NODE = ?                                                                                                   \n");
				strQuery.append("        AND CD_CLOSEDT IS NULL                                                                                                 \n");
				strQuery.append("        AND CD_REQCD = '02'                                                                                                    \n");
				strQuery.append("        AND CD_INSTANCEID = '03'                                                                                               \n");
				
				strQuery.append(")a, cmm0020 b, cmm0020 c, cmm0020 d, cmm0020 e                                                                                 \n");
				strQuery.append("where b.cm_macode = 'CMR7000GBN'                                  								                                \n");
				strQuery.append("and b.cm_micode = CD_RUNCD 	                                                                                                \n");
				strQuery.append("and b.cm_closedt is null	                                     								                                \n");
				strQuery.append("and c.cm_macode = 'CMR7000JOB'                                   								                                \n");
				strQuery.append("and c.cm_micode = CD_REQCD                                                                                                     \n");
				strQuery.append("and c.cm_closedt is null	                                     								                                \n");
				strQuery.append("and d.cm_macode = 'INSTANCE'                                     								                                \n");
				strQuery.append("and d.cm_closedt is null	                                     								                                \n");
				strQuery.append("and d.cm_micode = CD_INSTANCECD                                                                                                \n");
				strQuery.append("and e.cm_macode = 'CMR7000ND'                                     								                                \n");
				strQuery.append("and e.cm_micode = CD_NODE                                                                                                      \n");
				strQuery.append("and e.cm_closedt is null	                                     								                                \n");
				strQuery.append("GROUP BY CD_NODE, d.cm_codename , CD_INSTANCECD ,d.cm_seqno           	                                                                            \n");
				strQuery.append("order by d.cm_seqno 	                                     								                                \n");
				pstmt = conn.prepareStatement(strQuery.toString());	
				pstmt = new LoggableStatement(conn,strQuery.toString());
				pstmt.setString(++cnt, acptdate);
				pstmt.setString(++cnt, acptdate);
				pstmt.setString(++cnt, acptdate);
				pstmt.setString(++cnt, acptdate);
				pstmt.setString(++cnt, acptdate);
				pstmt.setString(++cnt, acptdate);
				pstmt.setString(++cnt, acptdate);
				pstmt.setString(++cnt, acptdate);
				pstmt.setString(++cnt, acptdate);
		    	pstmt.setString(++cnt, node);
		    	pstmt.setString(++cnt, node);
		    	pstmt.setString(++cnt, node);
		    	pstmt.setString(++cnt, node);
		    	pstmt.setString(++cnt, node);
		    	pstmt.setString(++cnt, node);
			    ecamsLogger.error(((LoggableStatement)pstmt).getQueryString());			    
			    rs = pstmt.executeQuery();
			}else{
			    strQuery.append("SELECT  CD_NODE, d.cm_codename as instancecd, CD_INSTANCECD, SUM(T1), SUM(T2) , SUM(R1), SUM(R2), SUM(R3), SUM(I1),							\n");
			    strQuery.append("TRCONTROL_CK(?, CD_NODE ,CD_INSTANCECD,'01','TRCONTROL', 'ON' )  AS TC1ON,												 \n");
			    strQuery.append("TRCONTROL_CK(?, CD_NODE ,CD_INSTANCECD,'01','TRCONTROL', 'OFF') AS TC1OFF,	                                                      \n");										
				strQuery.append("TRCONTROL_CK(?, CD_NODE ,CD_INSTANCECD,'02','TRCONTROL', 'ON') AS TC2ON,                                                    \n");
				strQuery.append("TRCONTROL_CK(?, CD_NODE ,CD_INSTANCECD,'02','TRCONTROL', 'OFF') AS TC2OFF,                                                      \n");
				strQuery.append("TRCONTROL_CK(?, CD_NODE ,CD_INSTANCECD,'01','INSTANCE', 'ON') AS IC1ON,                                                      \n");
				strQuery.append("TRCONTROL_CK(?, CD_NODE ,CD_INSTANCECD,'01','INSTANCE', 'OFF') AS IC1OFF,                                                      \n");
				strQuery.append("TRCONTROL_CK(?, CD_NODE ,CD_INSTANCECD,'01','REFRESH', 'ON') AS RC1ON,                                                      \n");
				strQuery.append("TRCONTROL_CK(?, CD_NODE ,CD_INSTANCECD,'02','REFRESH', 'ON') AS RC2ON,                                                      \n");
				strQuery.append("TRCONTROL_CK(?, CD_NODE ,CD_INSTANCECD,'03','REFRESH', 'ON') AS RC3ON                                                      \n");
				strQuery.append("FROM(                                                                                                                         \n");
				if(instance.equals("01")){
					strQuery.append("	SELECT CD_NODE, CD_REQCD, CD_RUNCD, CD_INSTANCECD, 1 AS T1, 0 AS T2, 0 AS R1, 0 AS R2, 0 AS R3,0 AS I1 FROM CMD7000            \n");              
				}else{
					strQuery.append("	SELECT CD_NODE, CD_REQCD, CD_RUNCD, CD_INSTANCECD, 0 AS T1, 1 AS T2, 0 AS R1, 0 AS R2, 0 AS R3,0 AS I1 FROM CMD7000            \n");
				}            
				strQuery.append("	WHERE CD_NODE = ?                                                                                                   \n");
				strQuery.append("	AND CD_CLOSEDT IS NULL                                                                                                 \n");
				strQuery.append("	AND CD_REQCD = '01'                                                                                                    \n");
				strQuery.append("	AND CD_INSTANCEID = ?                                                                                               \n");
				strQuery.append("	UNION ALL                                                                                                              \n");
				if(instance.equals("01")){
					strQuery.append("	SELECT CD_NODE, CD_REQCD, CD_RUNCD, CD_INSTANCECD, 0 AS T1, 0 AS T2, 1 AS R1, 0 AS R2, 0 AS R3,0 AS I1 FROM CMD7000            \n");              
				}else if(instance.equals("02")){
					strQuery.append("	SELECT CD_NODE, CD_REQCD, CD_RUNCD, CD_INSTANCECD, 0 AS T1, 0 AS T2, 0 AS R1, 1 AS R2, 0 AS R3,0 AS I1 FROM CMD7000            \n");
				}else{
					strQuery.append("	SELECT CD_NODE, CD_REQCD, CD_RUNCD, CD_INSTANCECD, 0 AS T1, 0 AS T2, 0 AS R1, 0 AS R2, 1 AS R3,0 AS I1 FROM CMD7000            \n");
				}
				strQuery.append("	WHERE CD_NODE = ?                                                                                                   \n");
				strQuery.append("	AND CD_CLOSEDT IS NULL                                                                                                 \n");
				strQuery.append("	AND CD_REQCD = '02'                                                                                                    \n");
				strQuery.append("	AND CD_INSTANCEID = ?                                                                                               \n");
				
				//if(instance.equals("01")){
					strQuery.append("	UNION ALL                                                                                                              \n");
					strQuery.append("	SELECT CD_NODE, CD_REQCD, CD_RUNCD, CD_INSTANCECD, 0 AS T1, 0 AS T2, 0 AS R1, 0 AS R2, 0 AS R3,1 AS I1 FROM CMD7000            \n");              
					strQuery.append("	WHERE CD_NODE = ?                                                                                                   \n");
					strQuery.append("	AND CD_CLOSEDT IS NULL                                                                                                 \n");
					strQuery.append("	AND CD_REQCD = '03'                                                                                                    \n");
					strQuery.append("	AND CD_INSTANCEID = ?                                                                                               \n");
				//}
				
				strQuery.append(")a, cmm0020 b, cmm0020 c, cmm0020 d, cmm0020 e                                                                                 \n");
				strQuery.append("where b.cm_macode = 'CMR7000GBN'                                  								                                \n");
				strQuery.append("and b.cm_micode = CD_RUNCD 	                                                                                                \n");
				strQuery.append("and b.cm_closedt is null	                                     								                                \n");
				strQuery.append("and c.cm_macode = 'CMR7000JOB'                                   								                                \n");
				strQuery.append("and c.cm_micode = CD_REQCD                                                                                                     \n");
				strQuery.append("and c.cm_closedt is null	                                     								                                \n");
				strQuery.append("and d.cm_macode = 'INSTANCE'                                     								                                \n");
				strQuery.append("and d.cm_micode = CD_INSTANCECD                                                                                                \n");
				strQuery.append("and d.cm_closedt is null	                                     								                                \n");
				strQuery.append("and e.cm_macode = 'CMR7000ND'                                     								                                \n");
				strQuery.append("and e.cm_micode = CD_NODE                                                                                                      \n");
				strQuery.append("and e.cm_closedt is null	                                     								                                \n");
				strQuery.append("GROUP BY CD_NODE, d.cm_codename , CD_INSTANCECD,d.cm_seqno                                                                                         \n");
				strQuery.append("order by d.cm_seqno 	                                     								                                \n");
				
				pstmt = conn.prepareStatement(strQuery.toString());	
				pstmt = new LoggableStatement(conn,strQuery.toString());	
				pstmt.setString(++cnt, acptdate);
				pstmt.setString(++cnt, acptdate);
				pstmt.setString(++cnt, acptdate);
				pstmt.setString(++cnt, acptdate);
				pstmt.setString(++cnt, acptdate);
				pstmt.setString(++cnt, acptdate);
				pstmt.setString(++cnt, acptdate);
				pstmt.setString(++cnt, acptdate);
				pstmt.setString(++cnt, acptdate);
			    	pstmt.setString(++cnt, node);
			    	pstmt.setString(++cnt, instance);
			    	pstmt.setString(++cnt, node);
			    	pstmt.setString(++cnt, instance);
			    	//	if(instance.equals("01")){
				    pstmt.setString(++cnt, node);
				    pstmt.setString(++cnt, instance);	
				    //}
			 
			    ecamsLogger.error(((LoggableStatement)pstmt).getQueryString());			    
			    rs = pstmt.executeQuery();
			}
		    
		    rsval.clear();
		    
		    while (rs.next()){
		    	rst = new HashMap<String,String>();			
		    	rst.put("CD_NODE", rs.getString("CD_NODE"));
				rst.put("CD_INSTANCECD", rs.getString("CD_INSTANCECD"));	    	
				rst.put("instancecd", rs.getString("instancecd"));						
				if(rs.getInt("SUM(T1)") > 0){
					rst.put("visible1", "1");
				}else{
					rst.put("visible1", "0");
				}
				if(rs.getInt("SUM(T2)") > 0){
					rst.put("visible2", "1");
				}else{
					rst.put("visible2", "0");
				}	
				if(rs.getInt("SUM(I1)") > 0){
					rst.put("visible3", "1");
				}else{
					rst.put("visible3", "0");						
				}				
				if(rs.getInt("SUM(R1)") > 0){
					rst.put("visible4", "1");
				}else{
					rst.put("visible4", "0");
				}					
				if(rs.getInt("SUM(R2)") > 0){
					rst.put("visible5", "1");
				}else{
					rst.put("visible5", "0");
				}
				if(rs.getInt("SUM(R3)") > 0){
					rst.put("visible6", "1");
				}else{
					rst.put("visible6", "0");
				}
				//String temptc1 = 
				//String temptc2 = rs.getString("TC1").substring(1);
				rst.put("checkbox1", rs.getString("TC1ON").substring(0,1));
				rst.put("colorswTC1ON", rs.getString("TC1ON").substring(1));
				//rst.put("colorswTC1ON", "1");
				rst.put("checkbox2", rs.getString("TC1OFF").substring(0,1));
				rst.put("checkbox3", rs.getString("TC2ON").substring(0,1));
				rst.put("colorswTC2ON", rs.getString("TC2ON").substring(1));
				//rst.put("colorswTC2ON", "3");
				rst.put("checkbox4", rs.getString("TC2OFF").substring(0,1));
				rst.put("checkbox5", rs.getString("IC1ON").substring(0,1));
				rst.put("colorswIC1ON", rs.getString("IC1ON").substring(1));
				//rst.put("colorswIC1ON", "5");
				rst.put("checkbox6", rs.getString("IC1OFF").substring(0,1));
				//rst.put("checkbox7", "0");
				//rst.put("checkbox8", "0");
				rst.put("selected1", rs.getString("RC1ON").substring(0,1));
				rst.put("colorswRC1", rs.getString("RC1ON").substring(1));
				rst.put("selected2", rs.getString("RC2ON").substring(0,1));
				rst.put("colorswRC2", rs.getString("RC2ON").substring(1));
				rst.put("selected3", rs.getString("RC3ON").substring(0,1));
				rst.put("colorswRC3", rs.getString("RC3ON").substring(1));
				
				rst.put("enabled1", "1");		
				rst.put("enabled2", "1");
				//rst.put("colorsw", "3");
				rsval.add(rst);
				rst = null;	
			}
		    pstmt.close();
		    rs.close();
			conn.close();
			
			conn = null;
			pstmt = null;				
			rs = null;
			
			return rsval.toArray();			
			
		} catch (SQLException sqlexception) {
			sqlexception.printStackTrace();
			ecamsLogger.error("## Cmr3800.cmd7000_SELECT1() SQLException START ##");
			ecamsLogger.error("## Error DESC : ", sqlexception);	
			ecamsLogger.error("## Cmr3800.cmd7000_SELECT1() SQLException END ##");			
			throw sqlexception;
		} catch (Exception exception) {
			exception.printStackTrace();
			ecamsLogger.error("## Cmr3800.cmd7000_SELECT1() Exception START ##");				
			ecamsLogger.error("## Error DESC : ", exception);	
			ecamsLogger.error("## Cmr3800.cmd7000_SELECT1() Exception END ##");				
			throw exception;
		}finally{
			if (strQuery != null) 	strQuery = null;
			if (rs != null)     try{rs.close();}catch (Exception ex){ex.printStackTrace();}
			if (pstmt != null)  try{pstmt.close();}catch (Exception ex2){ex2.printStackTrace();}
			if (conn != null){
				try{
					ConnectionResource.release(conn);
				}catch(Exception ex3){
					ecamsLogger.error("## Cmr3800.cmd7000_SELECT1() connection release exception ##");
					ex3.printStackTrace();
				}
			}
		}
	}//end of cmd7000_SELECT1() method statement
	 
	 public Object[] cmd7000_SELECT2(String acptdate) throws SQLException, Exception{
		Connection        conn        = null;
		PreparedStatement pstmt       = null;
		ResultSet         rs          = null;
		StringBuffer      strQuery    = new StringBuffer();
		ArrayList<HashMap<String, String>>  rsval  = new ArrayList<HashMap<String, String>>();
		HashMap<String, String>			  rst		  = null;		
		ConnectionContext connectionContext = new ConnectionResource();
		 
		
		try {
			conn = connectionContext.getConnection();
			 
			strQuery.setLength(0);
			strQuery.append("select a.cr_acptdate, to_date(a.cr_acptdate,'yyyy/mm/dd hh24:mi:ss') acptdate,   \n");
			strQuery.append("       a.cr_seqno, a.cr_svrip, a.cr_portno,                                      \n");
			strQuery.append("       a.cr_prcdate, a.cr_runtype, a.cr_result, a.cr_prcrst,a.cr_rstmsg,          \n");
			strQuery.append("       a.cr_node, b.cm_codename node,                                            \n");
			strQuery.append("       a.cr_runcd, c.cm_codename runcd,                                          \n");
			strQuery.append("       a.cr_reqcd, d.cm_codename reqcd,                                          \n");
			strQuery.append("       a.cr_instanceid, e.cm_codename instanceid,                                \n");
			strQuery.append("       a.cr_instancecd, decode(cr_reqcd, '02', '전체 인스턴스', f.cm_codename) instancecd,                            \n");
			strQuery.append("       a.CR_RESULT								                                 \n");
			strQuery.append("  from cmr7010 a, cmm0020 b, cmm0020 c, cmm0020 d,                               \n");
			strQuery.append("       cmm0020 e, cmm0020 f                                                      \n");
			strQuery.append(" where a.cr_acptdate = ?                                                         \n");
			strQuery.append("   and b.cm_macode = 'CMR7000ND'                                                 \n");
			strQuery.append("   and a.cr_node = b.cm_micode                                                   \n");
			strQuery.append("   and c.cm_macode = 'CMR7000GBN'                                                \n");
			strQuery.append("   and a.cr_runcd = c.cm_micode                                                  \n");
			strQuery.append("   and d.cm_macode = 'CMR7000JOB'                                                \n");
			strQuery.append("   and a.cr_reqcd = d.cm_micode                                                  \n");
			strQuery.append("   and e.cm_macode = 'CMR7000ID'                                                 \n");
			strQuery.append("   and a.cr_instanceid = e.cm_micode                                             \n");
			strQuery.append("   and f.cm_macode = 'INSTANCE'                                                  \n");
			strQuery.append("   and a.cr_instancecd = f.cm_micode                                             \n");
			strQuery.append("   order by node,instancecd,instanceid,runcd desc                             	  \n");
			pstmt = conn.prepareStatement(strQuery.toString());	
			pstmt = new LoggableStatement(conn,strQuery.toString());
			pstmt.setString(1, acptdate);
			ecamsLogger.error(((LoggableStatement)pstmt).getQueryString());
		    rs = pstmt.executeQuery();	
		    rsval.clear();
		    
		    while (rs.next()){
		    	rst = new HashMap<String,String>();				    	
		    	rst.put("cr_acptdate", rs.getString("cr_acptdate"));	
		    	rst.put("acptdate", rs.getString("acptdate"));	
		    	rst.put("cr_seqno", rs.getString("cr_seqno"));	
		    	rst.put("cr_svrip", rs.getString("cr_svrip"));	
		    	rst.put("cr_portno", rs.getString("cr_portno"));	
		    	rst.put("cr_prcdate", rs.getString("cr_prcdate"));	
		    	rst.put("cr_runtype", rs.getString("cr_runtype"));	
		    	rst.put("cr_result", rs.getString("cr_result"));	
		    	rst.put("cr_prcrst", rs.getString("cr_prcrst"));	
		    	rst.put("cr_node", rs.getString("cr_node"));	
		    	rst.put("node", rs.getString("node"));	
		    	rst.put("cr_reqcd", rs.getString("cr_reqcd"));	
		    	rst.put("reqcd", rs.getString("reqcd"));	
		    	rst.put("cr_runcd", rs.getString("cr_runcd"));	
		    	rst.put("runcd", rs.getString("runcd"));	
		    	rst.put("cr_instanceid", rs.getString("cr_instanceid"));	
		    	rst.put("instanceid", rs.getString("instanceid"));	
		    	rst.put("cr_instancecd", rs.getString("cr_instancecd"));	
		    	rst.put("instancecd", rs.getString("instancecd"));
		    	rst.put("cr_rstmsg", rs.getString("cr_rstmsg"));
		    	rst.put("CR_RESULT", rs.getString("CR_RESULT"));
		    	if(rs.getString("cr_prcrst") != null && rs.getString("cr_prcrst") !=""){
		    		if (!rs.getString("cr_prcrst").equals("0000") ){
		    			rst.put("colorsw", "3");
		    		}else{
		    			rst.put("colorsw", "9");
		    		}
		    	}else{
		    		rst.put("colorsw", "0");
		    	}

				rsval.add(rst);
				rst = null;	
			}				
			rs.close();				
			pstmt.close();
			conn.close();
			
			conn = null;
			pstmt = null;				
			rs = null;
			
			return rsval.toArray();			
			
		} catch (SQLException sqlexception) {
			sqlexception.printStackTrace();
			ecamsLogger.error("## Cmr3800.cmd7000_SELECT2() SQLException START ##");
			ecamsLogger.error("## Error DESC : ", sqlexception);	
			ecamsLogger.error("## Cmr3800.cmd7000_SELECT2() SQLException END ##");			
			throw sqlexception;
		} catch (Exception exception) {
			exception.printStackTrace();
			ecamsLogger.error("## Cmr3800.cmd7000_SELECT2() Exception START ##");				
			ecamsLogger.error("## Error DESC : ", exception);	
			ecamsLogger.error("## Cmr3800.cmd7000_SELECT2() Exception END ##");				
			throw exception;
		}finally{
			if (strQuery != null) 	strQuery = null;
			if (rs != null)     try{rs.close();}catch (Exception ex){ex.printStackTrace();}
			if (pstmt != null)  try{pstmt.close();}catch (Exception ex2){ex2.printStackTrace();}
			if (conn != null){
				try{
					ConnectionResource.release(conn);
				}catch(Exception ex3){
					ecamsLogger.error("## Cmr3800.cmd7000_SELECT2() connection release exception ##");
					ex3.printStackTrace();
				}
			}
		}
	}//end of cmd7000_SELECT2() method statement	

	 public void cmd7000_DELETE(String node, String reqcd, String runcd, String instancecd,String instanceid) throws SQLException, Exception{
		Connection        conn        = null;
		PreparedStatement pstmt       = null;	
		ResultSet         rs          = null;
		StringBuffer      strQuery    = new StringBuffer();
		
		ConnectionContext connectionContext = new ConnectionResource();		
		
		try {
			conn = connectionContext.getConnection();	
			strQuery.setLength(0);
			strQuery.append("update cmd7000 set cd_closedt = sysdate   \n");
			strQuery.append("where cd_node= ?			     \n");
			strQuery.append("and cd_reqcd= ?  	 		\n");
			strQuery.append("and cd_runcd= ? 				 \n");
			strQuery.append("and cd_instancecd= ?          \n");
			strQuery.append("and cd_instanceid= ?          \n");
     		pstmt = conn.prepareStatement(strQuery.toString());	
			pstmt.setString(1, node);
			pstmt.setString(2, reqcd);
			pstmt.setString(3, runcd);
			pstmt.setString(4, instancecd);
			pstmt.setString(5, instanceid);
       		pstmt.executeUpdate();   
			pstmt.close();				
			conn.close();
			
			conn = null;
			pstmt = null;

			
		} catch (SQLException sqlexception) {
			sqlexception.printStackTrace();
			ecamsLogger.error("## Cmr3800.cmd7000_DELETE() SQLException START ##");
			ecamsLogger.error("## Error DESC : ", sqlexception);	
			ecamsLogger.error("## Cmr3800.cmd7000_DELETE() SQLException END ##");			
			throw sqlexception;
		} catch (Exception exception) {
			exception.printStackTrace();
			ecamsLogger.error("## Cmr3800.cmd7000_DELETE() Exception START ##");				
			ecamsLogger.error("## Error DESC : ", exception);	
			ecamsLogger.error("## Cmr3800.cmd7000_DELETE() Exception END ##");				
			throw exception;
		}finally{
			if (strQuery != null) 	strQuery = null;
			if (rs != null)     try{rs.close();}catch (Exception ex){ex.printStackTrace();}
			if (pstmt != null)  try{pstmt.close();}catch (Exception ex2){ex2.printStackTrace();}
			if (conn != null){
				try{
					ConnectionResource.release(conn);
				}catch(Exception ex3){
					ecamsLogger.error("## Cmr3800.cmd7000_DELETE() connection release exception ##");
					ex3.printStackTrace();
				}
			}
		}
	}//end of cmd7000_DELETE() method statement 
	 
	 public Object[] cmr7100_search() throws SQLException, Exception{
		Connection        conn        = null;
		PreparedStatement pstmt       = null;
		ResultSet         rs          = null;
		StringBuffer      strQuery    = new StringBuffer();
		ArrayList<HashMap<String, String>>  rsval  = new ArrayList<HashMap<String, String>>();
		HashMap<String, String>			  rst		  = null;		
		ConnectionContext connectionContext = new ConnectionResource();		
		
		
		try {
			conn = connectionContext.getConnection();
			strQuery.setLength(0);
		
			strQuery.append("select distinct cr_acptdate from cmr7000		\n");
			strQuery.append("order by cr_acptdate desc		\n");

			pstmt = conn.prepareStatement(strQuery.toString());					
	//		pstmt = new LoggableStatement(conn,strQuery.toString());						
	 //       //ecamsLogger.error(((LoggableStatement)pstmt).getQueryString());           
	        rs = pstmt.executeQuery();
	        rsval.clear();
	        rst = new HashMap<String,String>();
			rst.put("acptdate", "선택하세요");

			rsval.add(rst);
			rst = null;
	        while (rs.next()){
					rst = new HashMap<String,String>();
					rst.put("cr_acptdate", rs.getString("cr_acptdate"));
					String tmp = rs.getString("cr_acptdate");
					String yyyy, MM, dd, hh, mm, ss= "";
					yyyy = tmp.substring(0,4).trim();
					MM = tmp.substring(4,6).trim();
					dd = tmp.substring(6,8).trim();
					hh = tmp.substring(8,10).trim();
					mm = tmp.substring(10,12).trim();
					ss = tmp.substring(12).trim();
					rst.put("acptdate", yyyy + "-" + MM + "-" + dd + "  " + hh + ":" + mm + ":" + ss);
					rsval.add(rst);
					rst = null;
			}
	         
			rs.close();
			pstmt.close();			
			conn.close();
			
			conn = null;
			pstmt = null;
			rs = null;
			
			return rsval.toArray();			
			
		} catch (SQLException sqlexception) {
			sqlexception.printStackTrace();
			ecamsLogger.error("## Cmr3800.cmr7100_search() SQLException START ##");
			ecamsLogger.error("## Error DESC : ", sqlexception);	
			ecamsLogger.error("## Cmr3800.cmr7100_search() SQLException END ##");			
			throw sqlexception;
		} catch (Exception exception) {
			exception.printStackTrace();
			ecamsLogger.error("## Cmr3800.cmr7100_search() Exception START ##");				
			ecamsLogger.error("## Error DESC : ", exception);	
			ecamsLogger.error("## Cmr3800.cmr7100_search() Exception END ##");				
			throw exception;
		}finally{
			if (strQuery != null) 	strQuery = null;
			if (rs != null)     try{rs.close();}catch (Exception ex){ex.printStackTrace();}
			if (pstmt != null)  try{pstmt.close();}catch (Exception ex2){ex2.printStackTrace();}
			if (conn != null){
				try{
					ConnectionResource.release(conn);
				}catch(Exception ex3){
					ecamsLogger.error("## Cmr3800.cmr7100_search() connection release exception ##");
					ex3.printStackTrace();
				}
			}
		}
	}//end of get_Instance() method statement
	 
	 public int cmd7000_CHK(String acptdate) throws SQLException, Exception{
		Connection        conn        = null;
		PreparedStatement pstmt       = null;
		ResultSet         rs          = null;
		StringBuffer      strQuery    = new StringBuffer();
		ArrayList<HashMap<String, String>>  rsval  = new ArrayList<HashMap<String, String>>();
		HashMap<String, String>			  rst		  = null;
		int selectchk = 0;
		ConnectionContext connectionContext = new ConnectionResource();	
		
		try {
			conn = connectionContext.getConnection();
			
			strQuery.setLength(0);
			strQuery.append("select count(*) as cnt from cmr7000   \n");
			strQuery.append("where cr_acptdate = ?                   \n");
			strQuery.append("and cr_prcdate is null          \n");
			pstmt = conn.prepareStatement(strQuery.toString());	
			pstmt = new LoggableStatement(conn,strQuery.toString());
			pstmt.setString(1, acptdate);
			ecamsLogger.error(((LoggableStatement)pstmt).getQueryString());
		    rs = pstmt.executeQuery();	
		    rsval.clear();
		    
		    while (rs.next()){
		    					    	
		    	selectchk=rs.getInt("cnt");	
		    	
					
			}				
			rs.close();				
			pstmt.close();
			conn.close();
			
			conn = null;
			pstmt = null;				
			rs = null;
			
			return selectchk;			
			
		} catch (SQLException sqlexception) {
			sqlexception.printStackTrace();
			ecamsLogger.error("## Cmr3800.cmd7000_CHK() SQLException START ##");
			ecamsLogger.error("## Error DESC : ", sqlexception);	
			ecamsLogger.error("## Cmr3800.cmd7000_CHK() SQLException END ##");			
			throw sqlexception;
		} catch (Exception exception) {
			exception.printStackTrace();
			ecamsLogger.error("## Cmr3800.cmd7000_CHK() Exception START ##");				
			ecamsLogger.error("## Error DESC : ", exception);	
			ecamsLogger.error("## Cmr3800.cmd7000_CHK() Exception END ##");				
			throw exception;
		}finally{
			if (strQuery != null) 	strQuery = null;
			if (rs != null)     try{rs.close();}catch (Exception ex){ex.printStackTrace();}
			if (pstmt != null)  try{pstmt.close();}catch (Exception ex2){ex2.printStackTrace();}
			if (conn != null){
				try{
					ConnectionResource.release(conn);
				}catch(Exception ex3){
					ecamsLogger.error("## Cmr3800.cmd7000_CHK() connection release exception ##");
					ex3.printStackTrace();
				}
			}
		}
	}//end of cmd7000_CHK() method statement	
	 
	 public String cmr7010_result(String rstfile) throws Exception {
		String			  fileStr = "";
		SystemPath		  csysPath    = new SystemPath();
		String			  resultPath  = "";
		String			  fileName = "";
		
		File readfile=null;
		ByteArrayOutputStream fileStream = null;
		byte[] byteTmpBuf = null;
		FileInputStream fis =null;
		int nCnt;
		
		try {
			
			resultPath = csysPath.getTmpDir("TR");
			csysPath = null;
			fileName = resultPath + "/" + rstfile;
			
			fileStream = new ByteArrayOutputStream();
			readfile = new File(fileName);
			
			byteTmpBuf = new byte[8192];
			fis = new FileInputStream(readfile);

			while( (nCnt=fis.read(byteTmpBuf)) > -1 )
			{ 
				fileStream.write(byteTmpBuf, 0, nCnt);
			} 
			fis.close();
			//fileStr = fileStream.toString("EUC-KR");
			fileStr = fileStream.toString("MS949");
			
			
			return fileStr;
			
			//end of while-loop statement
			
		} catch (Exception exception) {
			exception.printStackTrace();
			ecamsLogger.error("## Cmr5300.getFileText() Exception START ##");				
			ecamsLogger.error("## Error DESC : ", exception);	
			ecamsLogger.error("## Cmr5300.getFileText() Exception END ##");				
			throw exception;
		}finally{
			if (fileStream != null)	fileStream = null;
			if (fis != null) fis = null;			
		}
	}
		
	 public Object[] cmd7000_SELECTT(String acptdate,String node , String instance, String status,String Healthck) throws SQLException, Exception{
		Connection        conn        = null;
		PreparedStatement pstmt       = null;		
		ResultSet         rs          = null;				
		StringBuffer      strQuery    = new StringBuffer();
		ArrayList<HashMap<String, String>>  rsval  = new ArrayList<HashMap<String, String>>();
		HashMap<String, String>			  rst		  = null;	
		int				cnt           = 0;
		ConnectionContext connectionContext = new ConnectionResource();				
		try {
			conn = connectionContext.getConnection(); 		
			
			strQuery.setLength(0);
			strQuery.append(" SELECT  A.CD_NODE, d.cm_codename as instancecd, A.CD_INSTANCECD,A.CD_INSTANCEID,f.CM_CODENAME as codename,		\n");
			strQuery.append(" 			TRCONTROL_CK(?,Cd_NODE ,Cd_INSTANCECD,Cd_INSTANCEID,'TRCONTROL', 'ON' )  AS TC1ON1,                     \n");
			strQuery.append(" 			TRCONTROL_CK(?,Cd_NODE ,Cd_INSTANCECD,Cd_INSTANCEID,'TRCONTROL', 'OFF') AS TC1OFF1,                     \n");
			strQuery.append(" 			TRCONTROL_CK(?,CD_NODE ,CD_INSTANCECD,Cd_INSTANCEID,'REFRESH', 'ON') AS RC1ON1,                         \n");
			strQuery.append(" 			TRCONTROL_CK(?, CD_NODE ,CD_INSTANCECD,'01','INSTANCE', 'ON') AS IC1ON,                                 \n");
			strQuery.append(" 			TRCONTROL_CK(?, CD_NODE ,CD_INSTANCECD,'01','INSTANCE', 'OFF') AS IC1OFF,                               \n");
			if(!Healthck.equals("TRUE")){
				strQuery.append(" 			TRCONTROL_STATUS(CD_NODE ,CD_INSTANCECD, Cd_INSTANCEID, '01', ?) AS MCI,                                   \n");
				strQuery.append(" 			TRCONTROL_STATUS(CD_NODE ,CD_INSTANCECD, Cd_INSTANCEID, '02', ?) AS EAI,                                   \n");
				strQuery.append(" 			TRCONTROL_STATUS(CD_NODE ,CD_INSTANCECD, Cd_INSTANCEID, '03', ?) AS FEP,                                   \n");
			}else{
				strQuery.append(" 			HEALTHCK_STATUS(CD_NODE ,CD_INSTANCECD, Cd_INSTANCEID, '01', ?) AS MCI,                                   \n");
				strQuery.append(" 			HEALTHCK_STATUS(CD_NODE ,CD_INSTANCECD, Cd_INSTANCEID, '02', ?) AS EAI,                                   \n");
				strQuery.append(" 			HEALTHCK_STATUS(CD_NODE ,CD_INSTANCECD, Cd_INSTANCEID, '03', ?) AS FEP,                                   \n");
				
			}
			strQuery.append(" 			SUM(T1) AS T1, SUM(R1) AS R1, SUM(I1) AS I1                                                             \n");
			strQuery.append(" FROM (                                                                                                            \n");
			strQuery.append("       SELECT CD_NODE, CD_REQCD, CD_RUNCD, CD_INSTANCECD,CD_INSTANCEID, 1 AS T1, 0 AS R1, 0 AS I1 FROM CMD7000     \n");
			strQuery.append("        WHERE CD_NODE = ?                                                                                          \n");
			strQuery.append("          AND CD_CLOSEDT IS NULL                                                                                   \n");
			strQuery.append("          AND CD_REQCD = '01'                                                                                      \n");
			strQuery.append("       UNION ALL                                                                                                   \n");
			strQuery.append("       SELECT CD_NODE, CD_REQCD, CD_RUNCD, CD_INSTANCECD,CD_INSTANCEID, 0 AS T1, 1 AS R1, 0 AS I1 FROM CMD7000     \n");
			strQuery.append("        WHERE CD_NODE = ?                                                                                          \n");
			strQuery.append("          AND CD_CLOSEDT IS NULL                                                                                   \n");
			strQuery.append("          AND CD_REQCD = '02'                                                                                      \n");
			strQuery.append("       UNION ALL                                                                                                   \n");
			strQuery.append("       SELECT CD_NODE, CD_REQCD, CD_RUNCD, CD_INSTANCECD,CD_INSTANCEID, 0 AS T1, 0 AS R1, 1 AS I1 FROM CMD7000     \n");
			strQuery.append("        WHERE CD_NODE = ?                                                                                          \n");
			strQuery.append("          AND CD_CLOSEDT IS NULL                                                                                   \n");
			strQuery.append("          AND CD_INSTANCEID = '01'                                                                                 \n");
			strQuery.append("          AND CD_REQCD = '03'                                                                                      \n");
			strQuery.append("      ) a, cmm0020 b, cmm0020 c, cmm0020 d, cmm0020 e ,cmm0020 f                                                   \n");
			strQuery.append(" where b.cm_macode = 'CMR7000GBN'                                                                                  \n");
			if(!instance.equals("00")) strQuery.append(" and A.CD_INSTANCEID = ?                                                                \n");
			strQuery.append(" and b.cm_micode = CD_RUNCD                                                                                        \n");
			strQuery.append(" and b.cm_closedt is null                                                                                          \n");
			strQuery.append(" and c.cm_macode = 'CMR7000JOB'                                                                                    \n");
			strQuery.append(" and c.cm_micode = CD_REQCD                                                                                        \n");
			strQuery.append(" and c.cm_closedt is null                                                                                          \n");
			strQuery.append(" and d.cm_macode = 'INSTANCE'                                                                                      \n");
			strQuery.append(" and d.cm_closedt is null                                                                                          \n");
			strQuery.append(" and d.cm_micode = CD_INSTANCECD                                                                                   \n");
			strQuery.append(" and e.cm_macode = 'CMR7000ND'                                                                                     \n");
			strQuery.append(" and e.cm_micode = CD_NODE                                                                                         \n");
			strQuery.append(" and e.cm_closedt is null                                                                                          \n");
			strQuery.append(" and f.cm_micode = CD_INSTANCEID                                                                                   \n");
			strQuery.append(" and f.cm_macode = 'CMR7000ID'                                                                                     \n");
			strQuery.append(" GROUP BY CD_NODE, d.cm_codename , CD_INSTANCECD ,d.cm_seqno, CD_INSTANCEID,f.CM_CODENAME                          \n");
			strQuery.append(" order by d.cm_seqno                                                                                               \n");
			pstmt = conn.prepareStatement(strQuery.toString());	
			pstmt = new LoggableStatement(conn,strQuery.toString());
			
			cnt = 0;

			pstmt.setString(++cnt, acptdate);
			pstmt.setString(++cnt, acptdate);
			pstmt.setString(++cnt, acptdate);
			pstmt.setString(++cnt, acptdate);
			pstmt.setString(++cnt, acptdate);
			pstmt.setString(++cnt, status);
			pstmt.setString(++cnt, status);
			pstmt.setString(++cnt, status);
	    	pstmt.setString(++cnt, node);
	    	pstmt.setString(++cnt, node);
	    	pstmt.setString(++cnt, node);
	    	if(!instance.equals("00")) pstmt.setString(++cnt, instance);
		    ecamsLogger.error(((LoggableStatement)pstmt).getQueryString());			    
		    rs = pstmt.executeQuery();
			
		    rsval.clear();
		    
		    String in = "";
		    while (rs.next()){
		    	rst = new HashMap<String,String>();			
		    	rst.put("CD_NODE", rs.getString("CD_NODE"));
				rst.put("CD_INSTANCECD", rs.getString("CD_INSTANCECD"));
				rst.put("CD_INSTANCEID", rs.getString("CD_INSTANCEID"));
				
				if(!in.equals(rs.getString("instancecd"))){
					rst.put("instancecd", rs.getString("instancecd"));
					in = rs.getString("instancecd");
				}
				rst.put("INSTANCE_YN2",rs.getString("codename"));
					
				if(rs.getInt("T1") > 0){
					rst.put("visible1", "1");
				}else{
					rst.put("visible1", "0");
				}
				if(rs.getInt("R1") > 0){
					rst.put("visible4", "1");
				}else{
					rst.put("visible4", "0");
				}
				if(rs.getInt("I1") > 0){
					rst.put("visible3", "1");
				}else{
					rst.put("visible3", "0");						
				}
				
				rst.put("colormci", "0");
				if(rs.getString("MCI")!=null){
					if(rs.getString("MCI").equals("제어")){
						rst.put("colormci", "3");
					}else if(rs.getString("MCI").equals("체크실패")){
						rst.put("colormci", "5");
					}
				}
				rst.put("colorfep", "0");
				if(rs.getString("FEP")!=null){
					if(rs.getString("FEP").equals("제어")){
						rst.put("colorfep", "3");
					}else if(rs.getString("FEP").equals("체크실패")){
						rst.put("colorfep", "5");
					}
				}
				rst.put("coloreai", "0");
				if(rs.getString("EAI")!=null){
					if(rs.getString("EAI").equals("제어")){
						rst.put("coloreai", "3");
					}else if(rs.getString("EAI").equals("체크실패")){
						rst.put("coloreai", "5");
					}
				}
				rst.put("MCI", rs.getString("MCI"));
				rst.put("FEP", rs.getString("FEP"));
				rst.put("EAI", rs.getString("EAI"));
				
				rst.put("checkbox1", rs.getString("TC1ON1").substring(0,1));
				rst.put("checkbox2", rs.getString("TC1OFF1").substring(0,1));
				rst.put("colorswTC1ON", rs.getString("TC1ON1").substring(1));
				
				rst.put("checkbox5", rs.getString("IC1ON").substring(0,1));
				rst.put("checkbox6", rs.getString("IC1OFF").substring(0,1));
				rst.put("colorswIC1ON", rs.getString("IC1ON").substring(1));
				
				rst.put("selected1", rs.getString("RC1ON1").substring(0,1));
				rst.put("colorswRC1", rs.getString("RC1ON1").substring(1));
				
				rst.put("enabled1", "1");		
				rst.put("enabled2", "1");
				rsval.add(rst);
				rst = null;	
			}
		    pstmt.close();
		    rs.close();
			conn.close();
			
			conn = null;
			pstmt = null;				
			rs = null;
			
			return rsval.toArray();			
			
		} catch (SQLException sqlexception) {
			sqlexception.printStackTrace();
			ecamsLogger.error("## Cmr3800.cmd7000_SELECT1() SQLException START ##");
			ecamsLogger.error("## Error DESC : ", sqlexception);	
			ecamsLogger.error("## Cmr3800.cmd7000_SELECT1() SQLException END ##");			
			throw sqlexception;
		} catch (Exception exception) {
			exception.printStackTrace();
			ecamsLogger.error("## Cmr3800.cmd7000_SELECT1() Exception START ##");				
			ecamsLogger.error("## Error DESC : ", exception);	
			ecamsLogger.error("## Cmr3800.cmd7000_SELECT1() Exception END ##");				
			throw exception;
		}finally{
			if (strQuery != null) 	strQuery = null;
			if (rs != null)     try{rs.close();}catch (Exception ex){ex.printStackTrace();}
			if (pstmt != null)  try{pstmt.close();}catch (Exception ex2){ex2.printStackTrace();}
			if (conn != null){
				try{
					ConnectionResource.release(conn);
				}catch(Exception ex3){
					ecamsLogger.error("## Cmr3800.cmd7000_SELECT1() connection release exception ##");
					ex3.printStackTrace();
				}
			}
		}
	}//end of cmd7000_SELECTT() method statement
	 
	public String cmr7000_INSERTT(ArrayList<HashMap<String, String>>  getCm7000List) throws SQLException, Exception{
		Connection         conn        	= null;
		PreparedStatement  pstmt       	= null;
		StringBuffer       strQuery    	= new StringBuffer();
		ConnectionContext  connectionContext = new ConnectionResource();
		
		SystemPath		   cTempGet	    = new SystemPath();
		String			   tmpPath 		= "";
		String			   strBinPath 	= "";
		String  		   shFileName 	= "";
		File 			   shfile		= null;
		OutputStreamWriter writer 		= null;
		String[] 		   strAry 		= null;
		Runtime  		   run 			= null;
		Process 		   p 			= null;
		ResultSet         rs          = null;
		String rtString = "";
		try {
			conn = connectionContext.getConnection();
			conn.setAutoCommit(false);
			
			SimpleDateFormat formatter = new SimpleDateFormat ("yyyyMMddHHmmss");
			Calendar cal = Calendar.getInstance();
			String today = formatter.format(cal.getTime());
			
			int i=0;
			String retMsg = "";
			
			//ecamsLogger.error("++++++cmr7000_INSERTT LIST SIZE : "+getCm7000List.size());
			for(i=0;i<getCm7000List.size();i++){
				strQuery.setLength(0);	
				strQuery.append("insert into cmr7000																					\n");
				strQuery.append("(cr_acptdate, cr_node, cr_instancecd, cr_instanceid, cr_trcontrol, cr_refresh, cr_instance, cr_editor)	\n");
				strQuery.append("values(?, ?, ?, ?, ?, ?, ?, ?)												 							\n");
				pstmt = conn.prepareStatement(strQuery.toString());
		    	pstmt.setString(1, today);
		    	pstmt.setString(2, getCm7000List.get(i).get("node"));
		    	pstmt.setString(3, getCm7000List.get(i).get("instancecd"));
		    	pstmt.setString(4, getCm7000List.get(i).get("instanceid"));
		    	pstmt.setString(5, getCm7000List.get(i).get("trcontrol"));
		    	pstmt.setString(6, getCm7000List.get(i).get("refresh"));
		    	pstmt.setString(7, getCm7000List.get(i).get("instance"));
		    	pstmt.setString(8, getCm7000List.get(i).get("editor"));
		    	pstmt.executeUpdate();
			    pstmt.close();
			}
			
			conn.commit();
			conn.close();		
			pstmt = null;
			conn = null;
			
			tmpPath = cTempGet.getTmpDir("99");
			strBinPath = cTempGet.getTmpDir("14");
			
			shFileName = tmpPath + "/" + today; 
									
			shfile = new File(shFileName);
			
			if( !(shfile.isFile()) ){
				shfile.createNewFile();
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
			//writer.write("./ecams_trcontrol.sh " + today + " &\n");
//			writer.write("./ecams_trcontrol " + today + " &\n");
			writer.write("./ecams_batexec " +rtString + " \"cd " + strBinPath+ ";" +"./ecams_trcontrol " + today + " &\" \n");
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
			
			if (p.exitValue() != 0) {
				ecamsLogger.error("++++++cmr7000_INSERTT RETURN ERROR");
				retMsg = "ERR";
			} else {
				retMsg = today;
			}
			pstmt.close();
			conn.close();
			pstmt = null;
			conn = null;
			rs.close();// 20221219 ecams_batexec 추가 쿼리
			rs = null;	
			return retMsg;
			
		} catch (SQLException sqlexception) {
			sqlexception.printStackTrace();
			if (pstmt != null)  try{pstmt.close();}catch (Exception ex2){ex2.printStackTrace();}
			if (conn != null){
				try{
					conn.rollback();
					ConnectionResource.release(conn);
				}catch(Exception ex3){
					ecamsLogger.error("## Cmr3800.cmr7000_INSERTT() connection release exception ##");
					ex3.printStackTrace();
				}				
			}
			ecamsLogger.error("## Cmr3800.cmr7000_INSERTT() SQLException START ##");
			ecamsLogger.error("## Error DESC : ", sqlexception);	
			ecamsLogger.error("## Cmr3800.cmr7000_INSERTT() SQLException END ##");			
			throw sqlexception;
		} catch (Exception exception) {
			exception.printStackTrace();
			if (pstmt != null)  try{pstmt.close();}catch (Exception ex2){ex2.printStackTrace();}
			if (conn != null){
				try{
					conn.rollback();
					ConnectionResource.release(conn);
				}catch(Exception ex3){
					ecamsLogger.error("## Cmr3800.cmr7000_INSERTT() connection release exception ##");
					ex3.printStackTrace();
				}				
			}			
			ecamsLogger.error("## Cmr3800.cmr7000_INSERTT() Exception START ##");				
			ecamsLogger.error("## Error DESC : ", exception);	
			ecamsLogger.error("## Cmr3800.cmr7000_INSERTT() Exception END ##");				
			throw exception;
		}finally{
			if (strQuery != null)	strQuery = null;
			if (pstmt != null)  try{pstmt.close();}catch (Exception ex2){ex2.printStackTrace();}
			if (conn != null){
				try{
					ConnectionResource.release(conn);
				}catch(Exception ex3){
					ecamsLogger.error("## Cmr3800.cmr7000_INSERTT() connection release exception ##");
					ex3.printStackTrace();
				}
			}
		}
	}//end of cmr7000_INSERT() method statement
	 
	
	public String trcontrol_call(String node, String instance, String userid) throws SQLException, Exception{
		SystemPath		   cTempGet	    = new SystemPath();
		String			   tmpPath 		= "";
		String			   strBinPath 	= "";
		String  		   shFileName 	= "";
		String             retMsg       = "";
		File 			   shfile		= null;
		OutputStreamWriter writer 		= null;
		String[] 		   strAry 		= null;
		Runtime  		   run 			= null;
		Process 		   p 			= null;
		Connection        conn        = null;
		PreparedStatement pstmt       = null;
		ConnectionContext connectionContext = new ConnectionResource();
		StringBuffer      strQuery    = new StringBuffer();
		ResultSet         rs          = null;
		String rtString = "";
		
		try {
			tmpPath = cTempGet.getTmpDir("99");
			strBinPath = cTempGet.getTmpDir("14");
			
			shFileName = tmpPath + "/" + node+"_"+userid; 
									
			shfile = new File(shFileName);
			
			if( !(shfile.isFile()) ){
				shfile.createNewFile();
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
			//writer.write("./ecams_trcontrol_ck.sh " + node + " \"" + instance + "\" \n");
//			writer.write("./ecams_trcontrol_ck " + node + " \"" + instance + "\" \n");
			writer.write("./ecams_batexec " +rtString + " \"cd " + strBinPath+ ";" +"./ecams_trcontrol_ck " + node + " '" + instance + "'\" \n");
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
			
			if (p.exitValue() != 0) {
				retMsg = "FAIL";
			} else {
				retMsg = "OK";
			}
			
			pstmt.close();
			conn.close();
			pstmt = null;
			conn = null;
			rs.close();// 20221219 ecams_batexec 추가 쿼리
			rs = null;	
			return retMsg;
			
		} catch (SQLException sqlexception) {
			sqlexception.printStackTrace();
			ecamsLogger.error("## Cmr3800.trcontrol_call() SQLException START ##");
			ecamsLogger.error("## Error DESC : ", sqlexception);	
			ecamsLogger.error("## Cmr3800.trcontrol_call() SQLException END ##");			
			throw sqlexception;
		} catch (Exception exception) {
			exception.printStackTrace();
			ecamsLogger.error("## Cmr3800.trcontrol_call() Exception START ##");				
			ecamsLogger.error("## Error DESC : ", exception);	
			ecamsLogger.error("## Cmr3800.trcontrol_call() Exception END ##");				
			throw exception;
		}finally{
		}
	}//end of trcontrol_call() method statement
	
	public String HealthChk(String node, String instance, String userid) throws SQLException, Exception{
		SystemPath		   cTempGet	    = new SystemPath();
		String			   tmpPath 		= "";
		String			   strBinPath 	= "";
		String  		   shFileName 	= "";
		String             retMsg       = "";
		File 			   shfile		= null;
		OutputStreamWriter writer 		= null;
		String[] 		   strAry 		= null;
		Runtime  		   run 			= null;
		Process 		   p 			= null;
		Connection        conn        = null;
		PreparedStatement pstmt       = null;
		ConnectionContext connectionContext = new ConnectionResource();
		StringBuffer      strQuery    = new StringBuffer();
		ResultSet         rs          = null;
		String rtString = "";
		try {
			tmpPath = cTempGet.getTmpDir("99");
			strBinPath = cTempGet.getTmpDir("14");
			
			shFileName = tmpPath + "/" + node+"_"+userid; 
									
			shfile = new File(shFileName);
			
			if( !(shfile.isFile()) ){
				shfile.createNewFile();
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
//			writer.write("./ecams_health_ck.sh " + node + " \"" + instance + "\" \n");
			writer.write("./ecams_batexec " +rtString + " \"cd " + strBinPath+ ";" +"./ecams_health_ck.sh " + node + " '" + instance + "'\" \n");
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
			
			if (p.exitValue() != 0) {
				retMsg = "FAIL";
			} else {
				retMsg = "OK";
			}
			pstmt.close();
			conn.close();
			pstmt = null;
			conn = null;
			rs.close();// 20221219 ecams_batexec 추가 쿼리
			rs = null;	
			return retMsg;
			
		} catch (SQLException sqlexception) {
			sqlexception.printStackTrace();
			ecamsLogger.error("## Cmr3800.trcontrol_call() SQLException START ##");
			ecamsLogger.error("## Error DESC : ", sqlexception);	
			ecamsLogger.error("## Cmr3800.trcontrol_call() SQLException END ##");			
			throw sqlexception;
		} catch (Exception exception) {
			exception.printStackTrace();
			ecamsLogger.error("## Cmr3800.trcontrol_call() Exception START ##");				
			ecamsLogger.error("## Error DESC : ", exception);	
			ecamsLogger.error("## Cmr3800.trcontrol_call() Exception END ##");				
			throw exception;
		}finally{
		}
	}//end of HealthChk() method statement
   public Object[] getFileList_Emg(String strUserId, String strSys,String strDept, String cboGbn, String Admin, String emgSw) throws SQLException, Exception {
		
    	Connection        conn        = null;
		PreparedStatement pstmt       = null;
		PreparedStatement pstmt2      = null;
		ResultSet         rs          = null;
		ResultSet         rs2         = null;
		StringBuffer      strQuery    = new StringBuffer();
		ArrayList<HashMap<String, String>>  rtList	= new ArrayList<HashMap<String, String>>();
		HashMap<String, String>			  	rst	 	= null;
        Object[]		 					rtObj	= null;	
        int              					Cnt 	= 0;
		ConnectionContext connectionContext = new ConnectionResource();
		try {            
			 conn = connectionContext.getConnection();
			 strQuery.setLength(0);
			 strQuery.append(" select a.cr_acptno , a.cr_syscd,a.cr_status , a.cr_qrycd ,  a.cr_editor , a.cr_sayu, a.cr_lastdate, a.cr_acptdate,    \n");
			 strQuery.append("        a.cr_prcreq, a.cr_eddate,c.cm_sysmsg , d.CM_DEPTNAME  ,e.cm_username, a.cr_passok,                             \n");
			 strQuery.append("       (select cm_codename from cmm0020 where cm_macode ='REQPASS' and cm_micode = a.cr_passok) passok,                 \n");
			 strQuery.append("       (select count(*) cnt from cmr1010 aa, cmm0036 bb where aa.cr_acptno = a.cr_acptno and aa.cr_rsrccd = bb.cm_rsrccd and aa.cr_syscd = bb.cm_syscd and substr(bb.cm_info,59,1) = 1     ) cnt                 \n");
			 strQuery.append(" from cmr1000 a , cmr9900 b , cmm0030 c ,  cmm0100 d , cmm0040 e                                                       \n");
			 strQuery.append(" where a.cr_acptno = b.cr_acptno                                                                                \n");
			 if (emgSw.equals("2")) {
				strQuery.append("and a.cr_passok='2'                                             											  \n");
			 }
			 strQuery.append("  and  a.cr_status = '0'                                                                                        \n");
			 strQuery.append("  and  c.cm_syscd = a.cr_syscd                                                                                  \n");			 
			 if (!"".equals(strSys) && strSys != null){
				 strQuery.append("  and  c.cm_syscd = ?                                                                                       \n");	 
			 }else{
				 strQuery.append(" and c.cm_syscd in (select distinct cm_syscd from cmm0044 where cm_userid = ?)                              \n");	 
			 }
			 if(cboGbn.equals("00")){
				 strQuery.append("  and  b.cr_team = 'SYSTS' \n ");
			 }else if(cboGbn.equals("01")){
				 strQuery.append("  and  b.cr_team = 'SYSED' \n ");
			 }else if(cboGbn.equals("02")){ // 20230125 MCI 있는지 여부 체크 추가
				 strQuery.append("  and  b.cr_team = 'SYSMCI' \n "); 
			 }
			 strQuery.append("  and  a.CR_TEAMCD = d.CM_DEPTCD                                                                                \n");
			 if (!"".equals(strDept) && strDept != null){
		     strQuery.append("  and  a.CR_TEAMCD = ?  	                                                                                      \n");
			 }
			 if(Admin.equals("N")){
				 strQuery.append("  and  a.cr_editor = ?	\n");			 
			 }			 
			 strQuery.append("  and  a.cr_editor = e.cm_userid                                                                                \n");
			 strQuery.append("  and  b.cr_locat = '00'                                                                                        \n");
			 strQuery.append("  and  b.cr_status = '0'                                                                                        \n");
			 strQuery.append("  and  exists (select 'X'                                                                                       \n");
			 strQuery.append("                 FROM CMR1010 D                                                                                 \n");
			 strQuery.append("                     ,CMM0038 E                                                                                 \n");
			 strQuery.append("                     ,CMM0036 F                                                                                 \n");
			 strQuery.append("                WHERE D.CR_ACPTNO = A.CR_ACPTNO                                                                 \n");
			 strQuery.append("                  AND D.CR_SYSCD  = E.CM_SYSCD                                                                  \n");
			 strQuery.append("                  AND D.CR_RSRCCD = E.CM_RSRCCD                                                                 \n");
			 strQuery.append("                  AND E.CM_SVRCD  = DECODE(b.CR_TEAM, 'SYSTS', '35', '05')                                      \n");
			 strQuery.append("                  AND D.CR_SYSCD  = F.CM_SYSCD                                                                  \n");
			 strQuery.append("                  AND D.CR_RSRCCD = F.CM_RSRCCD 	                                                              \n");
			 strQuery.append("                  AND SUBSTR(F.CM_INFO, 54, 1) != '1'                                                           \n");
			 strQuery.append("               )                                                                                                \n");
			 strQuery.append(" ORDER  BY  c.cm_sysmsg, e.cm_username, a.cr_acptno                                                             \n");
			 //pstmt = conn.prepareStatement(strQuery.toString());
			 pstmt = new LoggableStatement(conn,strQuery.toString());			
			 Cnt = 0;
	    
			 if (!"".equals(strSys) && strSys != null){
            	pstmt.setString(++Cnt, strSys);
			 }else{
            	pstmt.setString(++Cnt, strUserId);
			 }
			 if (!"".equals(strDept) && strDept != null){
            	pstmt.setString(++Cnt, strDept);
			 }
			 if(Admin.equals("N")){
				pstmt.setString(++Cnt, strUserId);
			 }
			 ecamsLogger.error(((LoggableStatement)pstmt).getQueryString());
			 rs = pstmt.executeQuery();
            
			 while (rs.next()){	
				rst = new HashMap<String, String>();
				rst.put("rows", Integer.toString(rs.getRow()));					
				rst.put("cm_sysmsg",   rs.getString("cm_sysmsg"));     	//시스템 
				rst.put("cm_deptname", rs.getString("CM_DEPTNAME")); 	//업무
				rst.put("cr_acptno",   rs.getString("cr_acptno"));     	//신청번호
				rst.put("cr_acptdate", rs.getString("cr_acptdate")); 	//신청일시
				rst.put("cr_sayu",     rs.getString("cr_sayu"));        //신청사유
				rst.put("cr_editor",   rs.getString("cm_username"));    //신청인  
				rst.put("cr_prcreq",   rs.getString("cr_prcreq"));     	//적용예정일시
				rst.put("cr_eddate",   rs.getString("cr_eddate"));     	//적용예정일시 
        		rst.put("cr_passok",   rs.getString("cr_passok"));     
        		rst.put("passok",      rs.getString("passok"));  
        		if(rs.getInt("cnt") > 0){
        			rst.put("mciYN", "Y");
        		} else {
        			rst.put("mciYN", "N");
        		}
        		
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
			ecamsLogger.error("## Cmr3800.getFileList() SQLException START ##");
			ecamsLogger.error("## Error DESC : ", sqlexception);	
			ecamsLogger.error("## Cmr3800.getFileList() SQLException END ##");			
			throw sqlexception;
		} catch (Exception exception) {
			exception.printStackTrace();
			ecamsLogger.error("## Cmr3800.getFileList() Exception START ##");				
			ecamsLogger.error("## Error DESC : ", exception);	
			ecamsLogger.error("## Cmr3800.getFileList() Exception END ##");				
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
					ecamsLogger.error("## Cmr3800.getFileList() connection release exception ##");
					ex3.printStackTrace();
				}
			}
		}
	}//end of SelectList() method statement	
}//end of Cmr3800 class statement