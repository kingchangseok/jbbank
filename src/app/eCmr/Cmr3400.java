package app.eCmr;

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

public class Cmr3400 {
	
	Logger ecamsLogger  = EcamsLogger.getLoggerInstance();
	// 대여현황 , 적용현황
	public Object[] getReqList(String strQryCd ,String UserId, String sDate, String eDate, String UserNo, String strDept, String ProgName) throws SQLException, Exception{
		
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		StringBuffer strQuery = new StringBuffer();
		
		ArrayList<HashMap<String, String>>  rsval = new ArrayList<HashMap<String, String>>();
		HashMap<String, String>			    rst		  = null;
		Object[] rtObj = null;
		int              Cnt = 0;
		ConnectionContext connectionContext = new ConnectionResource();	 

		try{
			conn = connectionContext.getConnection();
			strQuery.setLength(0);
			if(strQryCd.equals("01")){
				strQuery.append("select /*+ RULE */																												\n");
				strQuery.append("decode(a.cr_qrycd,'01','대여','대여취소') gubun,																					\n");
				strQuery.append("		(select cm_deptname from cmm0100 where a.cr_teamcd = cm_deptcd) as cm_deptname , d.cr_rsrcname,							\n");
				strQuery.append("		(select cm_codename from cmm0020 where cm_macode = 'JAWON' and b.cr_rsrccd = cm_micode) as cm_codename,					\n");
				strQuery.append("		(select cm_username from cmm0040 where cm_userid = a.cr_editor) as cm_username, 										\n");
				strQuery.append("		NVL((select distinct cr_qrycd from cmr1010 where b.cr_confno = cr_acptno and b.cr_itemid = cr_itemid),'') as qrycd,		\n");
				strQuery.append("		NVL((select distinct cr_status from cmr1010 where b.cr_confno = cr_acptno and b.cr_itemid = cr_itemid),'') as status,	\n");
				strQuery.append("		to_char(b.cr_prcdate,'yyyy-mm-dd') as cr_prcdate,b.cr_rsrcname,a.cr_sayu,e.cm_sysmsg, a.cr_qrycd, a.cr_acptno			\n");
				strQuery.append("  from cmr1000 a, cmr1010 b, cmm0102 c, cmr0020 d, cmm0030 e																	\n");
				strQuery.append(" where a.cr_qrycd in ('01','11')																								\n");
				strQuery.append("     and a.cr_syscd = e.cm_syscd																								\n");
				if(!"".equals(UserNo) && UserNo != null){
					strQuery.append("   and a.cr_editor = ?																										\n");
				}	
				if (!"".equals(strDept) && strDept != null)  {
					strQuery.append("   and a.cr_teamcd = ?																										\n");
				}
				if(!"".equals(ProgName) && ProgName != null){
					strQuery.append("and upper(d.cr_rsrcname) like upper(?)                                                                                             		\n");
				}
				//strQuery.append("and a.cr_status in ('9','8')																									\n");
				strQuery.append("and b.cr_status in ('9','8')																									\n");
				if(!"".equals(sDate) && sDate != null){
					strQuery.append("and to_char(b.cr_prcdate,'yyyy/mm/dd') >= ? ");
					}
				if(!"".equals(eDate) && eDate != null){
					strQuery.append("and to_char(b.cr_prcdate,'yyyy/mm/dd') <= ? ");
				}
				strQuery.append("and b.cr_jobcd = c.cm_jobcd   																		\n");
				strQuery.append("and a.cr_acptno = b.cr_acptno 																		\n");
				strQuery.append("and b.cr_itemid = d.cr_itemid																		\n");
//				[20251209] 당일 대여 취소건이 있더라도 대여목록에 노출시키기 
//				strQuery.append("   AND not exists (select 'X' 																	    \n");
//				strQuery.append("                     FROM cmr1010 																	\n");
//				strQuery.append("                    WHERE cr_baseno = b.cr_acptno													\n");						
//				strQuery.append("                      AND cr_itemid = b.cr_itemid													\n");						
//				strQuery.append("                      AND to_char(cr_prcdate,'yyyymmdd') = to_char(b.cr_prcdate,'yyyymmdd')		\n");
//				strQuery.append("                      AND cr_status IN ('9','8') 													\n");
//				strQuery.append("                      AND SUBSTR(cr_acptno,5,2) = '11' 											\n");
//				strQuery.append("                  )																				\n");
				strQuery.append("    order by b.cr_prcdate																			\n");
				pstmt = conn.prepareStatement(strQuery.toString());
				pstmt = new LoggableStatement(conn,strQuery.toString());
			
				if(!"".equals(UserNo) && UserNo != null){
					pstmt.setString(++Cnt, UserNo);
				}
				if (!"".equals(strDept) && strDept != null) {
	            	pstmt.setString(++Cnt, strDept);
	            }
				if(!"".equals(ProgName) && ProgName != null){
					pstmt.setString(++Cnt, "%"+ProgName+"%");
				}
		        if(!"".equals(sDate) && sDate != null && !"".equals(eDate) && eDate != null){
		        	pstmt.setString(++Cnt, sDate);
		        	pstmt.setString(++Cnt, eDate);
		        }
			}
			else {
				strQuery.append("select /*+ RULE */																									\n");
				strQuery.append("decode(a.cr_qrycd,'01','대여','대여취소') gubun,																		\n");				
				strQuery.append("		(select cm_deptname from cmm0100 where a.cr_teamcd = cm_deptcd) as cm_deptname , d.cr_rsrcname,				\n");
				strQuery.append("		(select cm_codename from cmm0020 where cm_macode = 'JAWON' and b.cr_rsrccd = cm_micode) as cm_codename,		\n");
				strQuery.append("		(select cm_username from cmm0040 where cm_userid = a.cr_editor) as cm_username, 							\n");
				strQuery.append("		to_char(b.cr_prcdate,'yyyy-mm-dd') as cr_prcdate,															\n");
				strQuery.append("		b.cr_rsrcname, a.cr_sayu, f.cm_sysmsg,a.cr_qrycd,a.cr_acptno,															\n");
				strQuery.append("		(select cm_codename from cmm0020 where cm_macode = 'CHECKIN' AND cm_micode = DECODE(b.CR_VERSION, 1, '03', b.cr_qrycd)) as checkin	\n");
				strQuery.append("  from cmr1000 a, cmr1010 b, cmm0102 c, cmr0020 d, cmm0036 e, cmm0030 f  											\n");
			    strQuery.append(" where a.cr_qrycd = '04'																							\n");
				strQuery.append("   and (b.cr_qrycd = '04' and b.cr_version > 1)																	\n");
				strQuery.append("	and a.cr_syscd = f.cm_syscd																						\n");
				if(!"".equals(UserNo) && UserNo != null) {
					strQuery.append("   and a.cr_editor = ?																							\n");
				}	
				if (!"".equals(strDept) && strDept != null)  {
					strQuery.append("   and a.cr_teamcd = ?																							\n");
				}
				if(!"".equals(ProgName) && ProgName != null) {
					strQuery.append("and upper(d.cr_rsrcname) like upper(?)                                                                                           \n");
				}
				strQuery.append("and b.cr_jobcd = c.cm_jobcd   																						\n");
				strQuery.append("and b.cr_status in ('9','8')																						\n");
				if(!"".equals(sDate) && sDate != null)
				
				//strQuery.append("and to_char(b.cr_prcdate,'yyyy/mm/dd') >= ?																		\n");
				//strQuery.append("and b.cr_prcdate >= trunc(to_date(?, 'yyyy/mm/dd'), 'dd')														\n");
				strQuery.append("and to_char(b.cr_prcdate,'yyyymmdd') >= replace(?, '/', '')														\n");
				if(!"".equals(eDate) && eDate != null)
				//strQuery.append("and to_char(b.cr_prcdate,'yyyy/mm/dd') <= ?																		\n");
				//strQuery.append("and b.cr_prcdate < trunc(to_date(?, 'yyyy/mm/dd')+1, 'dd')														\n");
				strQuery.append("and to_char(b.cr_prcdate,'yyyymmdd') <= replace(?, '/', '')														\n");
				strQuery.append("and a.cr_acptno = b.cr_acptno																						\n");
				strQuery.append("and b.cr_syscd  = e.cm_syscd																						\n");
				strQuery.append("and b.cr_rsrccd = e.cm_rsrccd																						\n");
				strQuery.append("and SUBSTR(e.CM_INFO, 26, 1) = '0'																					\n");
				strQuery.append("and SUBSTR(e.CM_INFO, 02, 1) = '1'																					\n");
				strQuery.append("and b.cr_itemid = d.cr_itemid																						\n");
				strQuery.append("and '20111019' < (select to_char(cr_acptdate,'yyyymmdd')from cmr1000 where cr_acptno = b.cr_baseno)				\n");				
		        strQuery.append("UNION ALL select 																									\n");
				strQuery.append("decode(a.cr_qrycd,'01','대여','대여취소','적용') gubun,																		\n");				
				strQuery.append("(select cm_deptname from cmm0100 where a.cr_teamcd = cm_deptcd) as cm_deptname , d.cr_rsrcname, 					\n");
				strQuery.append("(select cm_codename from cmm0020 where cm_macode = 'JAWON' and b.cr_rsrccd = cm_micode) as cm_codename,			\n");
				strQuery.append("(select cm_username from cmm0040 where cm_userid = a.cr_editor) as cm_username, 									\n");
				strQuery.append("to_char(b.cr_prcdate,'yyyy-mm-dd') as cr_prcdate,																	\n");
				strQuery.append("b.cr_rsrcname, 																									\n");
				strQuery.append("a.cr_sayu, f.cm_sysmsg,																							\n");
				strQuery.append("a.cr_qrycd,a.cr_acptno, 																										\n");
				strQuery.append("(select cm_codename from cmm0020 where cm_macode = 'CHECKIN' AND cm_micode = DECODE(b.CR_VERSION, 1, '03', b.cr_qrycd)) as checkin	\n");
				strQuery.append("from cmr1000 a, cmr1010 b, cmm0102 c, cmr0020 d, CMM0036 e, cmm0030 f  , cmr0020 g									\n");
			    strQuery.append("where a.cr_qrycd = '04'																							\n");
				strQuery.append("and b.CR_VERSION = 1																								\n");
				strQuery.append("and a.cr_syscd = f.cm_syscd																						\n");
				strQuery.append("and g.cr_itemid = b.cr_itemid 																						\n");
				if(!"".equals(UserNo) && UserNo != null){
					strQuery.append("   and a.cr_editor = ?																							\n");
				}	
				if (!"".equals(strDept) && strDept != null)  {
					strQuery.append("   and a.cr_teamcd = ?																							\n");
				}
				if(!"".equals(ProgName) && ProgName != null){
					strQuery.append("and upper(d.cr_rsrcname) like upper(?)                                                                                           \n");
				}
				strQuery.append("and b.cr_jobcd = c.cm_jobcd   																						\n");
				strQuery.append("and b.cr_status in ('9','8')																						\n");
				if(!"".equals(sDate) && sDate != null)
				strQuery.append("and to_char(b.cr_prcdate, 'yyyy/mm/dd') >= ?																		\n");
				if(!"".equals(eDate) && eDate != null)
				strQuery.append("and to_char(b.cr_prcdate, 'yyyy/mm/dd') <= ?																		\n");
				strQuery.append("and a.cr_acptno = b.cr_acptno																						\n");
				strQuery.append("and b.cr_syscd  = e.cm_syscd																						\n");
				strQuery.append("and b.cr_rsrccd = e.cm_rsrccd																						\n");
				strQuery.append("and SUBSTR(e.CM_INFO, 26, 1) = '0'																					\n");
				strQuery.append("and b.cr_itemid = d.cr_itemid																						\n");
				//strQuery.append("and    '20111019' < (select to_char(cr_acptdate,'yyyymmdd')from cmr1000 where cr_acptno = b.cr_baseno)			\n");
				//strQuery.append("order by b.cr_prcdate																							\n");
				pstmt = conn.prepareStatement(strQuery.toString());
				pstmt = new LoggableStatement(conn,strQuery.toString());
				
				if(!"".equals(UserNo) && UserNo != null){
					pstmt.setString(++Cnt, UserNo);
				}
				if (!"".equals(strDept) && strDept != null) {
	            	pstmt.setString(++Cnt, strDept);
	            }
				if(!"".equals(ProgName) && ProgName != null){
					pstmt.setString(++Cnt, "%"+ProgName+"%");
				}
		        if(!"".equals(sDate) && sDate != null && !"".equals(eDate) && eDate != null){
		        	pstmt.setString(++Cnt, sDate);
		        	pstmt.setString(++Cnt, eDate);
		        	
		        }
				if(!"".equals(UserNo) && UserNo != null){
					pstmt.setString(++Cnt, UserNo);
					
				}
				if (!"".equals(strDept) && strDept != null) {
	            	pstmt.setString(++Cnt, strDept);
	            }
				if(!"".equals(ProgName) && ProgName != null){
					pstmt.setString(++Cnt, ProgName);
				}
		        if(!"".equals(sDate) && sDate != null && !"".equals(eDate) && eDate != null){
		        	pstmt.setString(++Cnt, sDate);
		        	pstmt.setString(++Cnt, eDate);
		        	
		        }
			}
			ecamsLogger.error(((LoggableStatement)pstmt).getQueryString());
			rs = pstmt.executeQuery();
			
			while(rs.next()){ 
				rst = new HashMap<String, String>();

				rst.put("gubun", rs.getString("gubun"));
				rst.put("cm_deptname", rs.getString("cm_deptname"));
				rst.put("cm_codename", rs.getString("cm_codename"));
				rst.put("cr_prcdate", rs.getString("cr_prcdate"));
				rst.put("cr_rsrcname", rs.getString("cr_rsrcname"));
				rst.put("cr_sayu", rs.getString("cr_sayu"));				
				rst.put("cm_username", rs.getString("cm_username"));
				rst.put("cr_qrycd", rs.getString("cr_qrycd"));
				if(strQryCd.equals("01")){
					rst.put("qrycd", rs.getString("qrycd"));
					rst.put("status", rs.getString("status"));
				}else {	
					rst.put("checkin", rs.getString("checkin"));
				}				
				rst.put("cm_sysmsg", rs.getString("cm_sysmsg"));
				rst.put("cr_acptno", rs.getString("cr_acptno"));
				rsval.add(rst);
				rst = null;

			}
			rs.close();
			pstmt.close();
			conn.close();
			
			rs = null;
			pstmt = null;
			conn = null;
			
			rtObj = rsval.toArray();
			rsval.clear();
			rsval = null;
    		
    		return rtObj; 
		}
		catch (SQLException sqlexception) {
			sqlexception.printStackTrace();
			ecamsLogger.error("## Cmr3400.getReqList() SQLException START ##");
			ecamsLogger.error("## Error DESC : ", sqlexception);	
			ecamsLogger.error("## Cmr3400.getReqList() SQLException END ##");			
			throw sqlexception;
		} catch (Exception exception) {
			exception.printStackTrace();
			ecamsLogger.error("## Cmr3400.getReqList() Exception START ##");				
			ecamsLogger.error("## Error DESC : ", exception);	
			ecamsLogger.error("## Cmr3400.getReqList() Exception END ##");				
			throw exception; 
		}finally{
			if (strQuery != null)	strQuery = null;
			if (rtObj != null)	rtObj = null;			
			if (rs != null)     try{rs.close();}catch (Exception ex){ex.printStackTrace();}
			if (pstmt != null)  try{pstmt.close();}catch (Exception ex2){ex2.printStackTrace();}
			if (conn != null){
				try{
					ConnectionResource.release(conn);
				}catch(Exception ex3){
					ecamsLogger.error("## Cmr3400.getReqList() connection release exception ##");
					ex3.printStackTrace();
				}
			}
		}		
		
	} 
	
	
	//내건만 조회 
	public Object[] getReqList2(String strQryCd ,String UserId) throws SQLException, Exception{
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		StringBuffer strQuery = new StringBuffer();
		
		ArrayList<HashMap<String, String>>  rsval = new ArrayList<HashMap<String, String>>();
		HashMap<String, String>			    rst		  = null;
		Object[] rtObj = null;
		int              Cnt = 0;
		ConnectionContext connectionContext = new ConnectionResource();	

		
		try{
			conn = connectionContext.getConnection();
			
			
			strQuery.setLength(0);
			strQuery.append("select d.cm_deptname, e.cm_codename, a.cr_acptdate, b.cr_rsrcname, a.cr_sayu, c.cm_username, a.cr_qrycd  ");
			strQuery.append("from cmr1000 a, cmr1010 b, cmm0040 c, cmm0100 d, cmm0020 e ");
			strQuery.append("where a.cr_editor = ? ");
			strQuery.append("and a.cr_qrycd = ? ");
			strQuery.append("and a.cr_acptno = b.cr_acptno ");
			strQuery.append("and a.cr_editor = c.cm_userid ");
			strQuery.append("and a.cr_teamcd = d.cm_deptcd ");
			strQuery.append("and b.cr_rsrccd = e.cm_micode ");
			strQuery.append("and e.cm_macode = 'JAWON' ");
			
			
			
			pstmt = conn.prepareStatement(strQuery.toString());
			 
			pstmt.setString(1, UserId);
			pstmt.setString(2, strQryCd);
			
	
	
			//pstmt = new LoggableStatement(conn,strQuery.toString());
			//ecamsLogger.error(((LoggableStatement)pstmt).getQueryString());
			rs = pstmt.executeQuery();
			
			while(rs.next()){ 
				rst = new HashMap<String, String>();
				
				rst.put("cm_deptname", rs.getString("cm_deptname"));
				rst.put("cm_codename", rs.getString("cm_codename"));
				rst.put("cr_acptdate", rs.getString("cr_acptdate"));
				rst.put("cr_rsrcname", rs.getString("cr_rsrcname"));
				rst.put("cr_sayu", rs.getString("cr_sayu"));				
				rst.put("cm_username", rs.getString("cm_username"));
				rst.put("cr_qrycd", rs.getString("cr_qrycd"));
				
				
				rsval.add(rst);
				rst = null;

			}
			rs.close();
			pstmt.close();
			conn.close();
			
			rs = null;
			pstmt = null;
			conn = null;
			
			rtObj = rsval.toArray();
			rsval.clear();
			rsval = null;
    		
    		return rtObj; 
		}
		catch (SQLException sqlexception) {
			sqlexception.printStackTrace();
			ecamsLogger.error("## Cmr3400.getReqList2() SQLException START ##");
			ecamsLogger.error("## Error DESC : ", sqlexception);	
			ecamsLogger.error("## Cmr3400.getReqList2() SQLException END ##");			
			throw sqlexception;
		} catch (Exception exception) {
			exception.printStackTrace();
			ecamsLogger.error("## Cmr3400.getReqList2() Exception START ##");				
			ecamsLogger.error("## Error DESC : ", exception);	
			ecamsLogger.error("## Cmr3400.getReqList2() Exception END ##");				
			throw exception; 
		}finally{
			if (strQuery != null)	strQuery = null;
			if (rtObj != null)	rtObj = null;			
			if (rs != null)     try{rs.close();}catch (Exception ex){ex.printStackTrace();}
			if (pstmt != null)  try{pstmt.close();}catch (Exception ex2){ex2.printStackTrace();}
			if (conn != null){
				try{
					ConnectionResource.release(conn);
				}catch(Exception ex3){
					ecamsLogger.error("## Cmr3400.getReqList2() connection release exception ##");
					ex3.printStackTrace();
				}
			}
		}		
		
	} 

	
	// 해당팀정보
	public Object[] getTeamInfo(String SelMsg,String itYn) throws SQLException, Exception{
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		StringBuffer strQuery = new StringBuffer();
		Object[] rtObj = null;
		
		ArrayList<HashMap<String, String>>  rsval = new ArrayList<HashMap<String, String>>();
		HashMap<String, String>			    rst		  = null;
		String            strSelMsg   = "";
		int              Cnt = 0;
		int				 index = 0;
		ConnectionContext connectionContext = new ConnectionResource();	


		try{
			conn = connectionContext.getConnection();
			if (!"".equals(SelMsg) && SelMsg != null) {
				if (SelMsg.toUpperCase().equals("ALL")){
					strSelMsg = "전체";
				}
				else if(SelMsg.toUpperCase().equals("SEL")){
					strSelMsg = "선택하세요";
				}
				else{
					strSelMsg   = "";
				}	
			}
			
			strQuery.setLength(0);
			strQuery.append("select cm_deptcd,cm_deptname \n");
			if (itYn.equals("Y")) {
				strQuery.append(" from (Select cm_deptcd,cm_updeptcd,cm_deptname,cm_useyn \n");
				strQuery.append("         from (Select * from cmm0100 where cm_useyn='Y') \n");
				//strQuery.append("        start with cm_updeptcd='000010505'  				  \n");
				//strQuery.append("        start with cm_updeptcd='0091'  				  \n");
				strQuery.append("        start with cm_updeptcd in('0091','0093')  				  \n");				
				strQuery.append("       connect by prior cm_deptcd = cm_updeptcd) 		  \n");
				strQuery.append(" where cm_useyn='Y'                          		      \n");
			} else {
				strQuery.append("  from cmm0100 \n");
				strQuery.append(" where cm_useyn='Y' \n");
			}strQuery.append(" group by cm_deptcd,cm_deptname \n"); 	
			strQuery.append(" order by cm_deptcd,cm_deptname \n"); 
			
			pstmt = conn.prepareStatement(strQuery.toString());
           
			//pstmt = new LoggableStatement(conn,strQuery.toString());
			//ecamsLogger.error(((LoggableStatement)pstmt).getQueryString());
			rs = pstmt.executeQuery();
			
			while(rs.next()){ 
				if (rs.getRow() == 1 && !strSelMsg.equals("") && strSelMsg.length() > 0) {
					rst = new HashMap<String, String>();
					rst.put("ID", "0");
					rst.put("cm_deptname", strSelMsg);
					rst.put("cm_deptcd", "");
					rsval.add(rst);
					rst = null;
				}	
				index = 0;
				if(rs.getString("cm_deptname").indexOf(" ") != -1){
				index = rs.getString("cm_deptname").indexOf(" ")+1;
				}
				
				rst = new HashMap<String, String>();
				
				rst.put("cm_deptcd", rs.getString("cm_deptcd"));
				rst.put("cm_deptname", rs.getString("cm_deptname"));

				rsval.add(rst);
				rst = null;

			}
			rs.close();
			pstmt.close();
			conn.close();
			
			rs = null;
			pstmt = null;
			conn = null;
			
			rtObj = rsval.toArray();
			rsval.clear();
			rsval = null;
    		
    		return rtObj; 
		}
		catch (SQLException sqlexception) {
			sqlexception.printStackTrace();
			ecamsLogger.error("## Cmr3400.getTeamInfo() SQLException START ##");
			ecamsLogger.error("## Error DESC : ", sqlexception);	
			ecamsLogger.error("## Cmr3400.getTeamInfo() SQLException END ##");			
			throw sqlexception;
		} catch (Exception exception) {
			exception.printStackTrace();
			ecamsLogger.error("## Cmr3400.getTeamInfo() Exception START ##");				
			ecamsLogger.error("## Error DESC : ", exception);	
			ecamsLogger.error("## Cmr3400.getTeamInfo() Exception END ##");				
			throw exception; 
		}finally{
			if (strQuery != null)	strQuery = null;
			if (rtObj != null)	rtObj = null;			
			if (rs != null)     try{rs.close();}catch (Exception ex){ex.printStackTrace();}
			if (pstmt != null)  try{pstmt.close();}catch (Exception ex2){ex2.printStackTrace();}
			if (conn != null){
				try{
					ConnectionResource.release(conn);
				}catch(Exception ex3){
					ecamsLogger.error("## Cmr3400.getTeamInfo() connection release exception ##");
					ex3.printStackTrace();
				}
			}
		}		 
		
	}

	public Object[] getReqList_Emg(String strQryCd ,String UserId, String sDate, String eDate, String UserNo, String strDept, String ProgName,String emgSw) throws SQLException, Exception{
		
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		StringBuffer strQuery = new StringBuffer();
		
		ArrayList<HashMap<String, String>>  rsval = new ArrayList<HashMap<String, String>>();
		HashMap<String, String>			    rst		  = null;
		Object[] rtObj = null;
		int              Cnt = 0;
		ConnectionContext connectionContext = new ConnectionResource();	 

		try{
			conn = connectionContext.getConnection();
			strQuery.setLength(0);
			if(strQryCd.equals("01")){
					strQuery.append("select /*+ RULE */																												\n");
					strQuery.append("(select cm_deptname from cmm0100 where a.cr_teamcd = cm_deptcd) as cm_deptname , d.cr_rsrcname,									\n");
					strQuery.append("(select cm_codename from cmm0020 where cm_macode = 'JAWON' and b.cr_rsrccd = cm_micode) as cm_codename,						\n");
					strQuery.append("(select cm_username from cmm0040 where cm_userid = a.cr_editor) as cm_username, 												\n");
					strQuery.append("NVL((select distinct cr_qrycd from cmr1010 where b.cr_confno = cr_acptno and b.cr_itemid = cr_itemid),'') as qrycd,			\n");
					strQuery.append("NVL((select distinct cr_status from cmr1010 where b.cr_confno = cr_acptno and b.cr_itemid = cr_itemid),'') as status,			\n");
					strQuery.append("to_char(b.cr_prcdate,'yyyy-mm-dd') as cr_prcdate, 																				\n");
					strQuery.append("b.cr_rsrcname, 																												\n");
					strQuery.append("a.cr_sayu,e.cm_sysmsg,																											\n");
					strQuery.append("	a.cr_qrycd, a.cr_passok,a.cr_acptno,																									\n");
					strQuery.append("(select cm_codename from cmm0020 where cm_macode ='REQPASS' and cm_micode = a.cr_passok) passok,          \n");					
					strQuery.append("from cmr1000 a, cmr1010 b, cmm0102 c,  cmr0020 d, cmm0030 e																	\n");
					strQuery.append("	where substr(a.cr_acptno,5,2) in('01','11')																					\n");
					strQuery.append("     and a.cr_syscd = e.cm_syscd																								\n");
					if(!"".equals(UserNo) && UserNo != null){
						strQuery.append("   and a.cr_editor = ?																										\n");
					}	
					if (!"".equals(strDept) && strDept != null)  {
						strQuery.append("   and a.cr_teamcd = ?																										\n");
					}
					if(!"".equals(ProgName) && ProgName != null){
						strQuery.append("and upper(d.cr_rsrcname) like upper(?)                                                                                            		\n");
					}
					//strQuery.append("and a.cr_status in ('9','8')																									\n");
					strQuery.append("and b.cr_status in ('9','8')																									\n");
					if (emgSw.equals("2")){
						strQuery.append("and a.cr_passok='2'                                                                                                        \n");
					}
					if(!"".equals(sDate) && sDate != null){
						strQuery.append("and to_char(b.cr_prcdate,'yyyy/mm/dd') >= ? ");
						}
					if(!"".equals(eDate) && eDate != null){
						strQuery.append("and to_char(b.cr_prcdate,'yyyy/mm/dd') <= ? ");
					}
					strQuery.append("and b.cr_jobcd = c.cm_jobcd   																									\n");
					strQuery.append("and a.cr_acptno = b.cr_acptno 																									\n");
					strQuery.append("and b.cr_itemid = d.cr_itemid																									\n");

					
					strQuery.append("   AND not exists (select 'X' 																	    \n");
					strQuery.append("                     FROM cmr1010 																	\n");
					strQuery.append("                    WHERE cr_baseno = b.cr_acptno													\n");						
					strQuery.append("                      AND cr_itemid = b.cr_itemid													\n");						
					strQuery.append("                      AND to_char(cr_prcdate,'yyyymmdd') = to_char(b.cr_prcdate,'yyyymmdd')		\n");
					strQuery.append("                      AND cr_status IN ('9','8') 													\n");
					strQuery.append("                      AND SUBSTR(cr_acptno,5,2) = '11' 											\n");
					strQuery.append("                  )																				\n");
					strQuery.append("    order by b.cr_prcdate																																		\n");
					pstmt = conn.prepareStatement(strQuery.toString());
					pstmt = new LoggableStatement(conn,strQuery.toString());
				
					if(!"".equals(UserNo) && UserNo != null){
						pstmt.setString(++Cnt, UserNo);
					}
					if (!"".equals(strDept) && strDept != null) {
		            	pstmt.setString(++Cnt, strDept);
		            }
					if(!"".equals(ProgName) && ProgName != null){
						pstmt.setString(++Cnt, "%"+ProgName+"%");
					}
			        if(!"".equals(sDate) && sDate != null && !"".equals(eDate) && eDate != null){
			        	pstmt.setString(++Cnt, sDate);
			        	pstmt.setString(++Cnt, eDate);
			        }
			}
			else {
				strQuery.append("select /*+ RULE */																																	\n");
				strQuery.append("(select cm_deptname from cmm0100 where a.cr_teamcd = cm_deptcd) as cm_deptname , d.cr_rsrcname, 													\n");
				strQuery.append("(select cm_codename from cmm0020 where cm_macode = 'JAWON' and b.cr_rsrccd = cm_micode) as cm_codename,											\n");
				strQuery.append("(select cm_username from cmm0040 where cm_userid = a.cr_editor) as cm_username, 																	\n");
				strQuery.append("to_char(b.cr_prcdate,'yyyy-mm-dd') as cr_prcdate,																									\n");
				strQuery.append("b.cr_rsrcname, 																																	\n");
				strQuery.append("a.cr_sayu, f.cm_sysmsg,																															\n");
				strQuery.append("a.cr_qrycd,a.cr_passok,a.cr_acptno,																																		\n");
				strQuery.append("(select cm_codename from cmm0020 where cm_macode ='REQPASS' and cm_micode = a.cr_passok) passok,          \n");					
				strQuery.append("(select cm_codename from cmm0020 where cm_macode = 'CHECKIN' AND cm_micode = DECODE(b.CR_VERSION, 1, '03', b.cr_qrycd)) as checkin												\n");
				strQuery.append("from cmr1000 a, cmr1010 b	,cmm0102 c	, cmr0020 d, cmm0036 e, cmm0030 f  																			\n");
			    strQuery.append("where a.cr_qrycd IN ('04')																														    \n");
				strQuery.append("and (b.cr_qrycd = '04' and b.cr_version > 1)																													    	\n");
				strQuery.append("and a.cr_syscd = f.cm_syscd																													   	\n");
				if(!"".equals(UserNo) && UserNo != null){
					strQuery.append("   and a.cr_editor = ?																																	\n");
				}	
				if (!"".equals(strDept) && strDept != null)  {
					strQuery.append("   and a.cr_teamcd = ?																																\n");
				}
				if(!"".equals(ProgName) && ProgName != null){
					strQuery.append("and upper(d.cr_rsrcname) like upper(?)                                                                                                 \n");
				}
				strQuery.append("and b.cr_jobcd = c.cm_jobcd   																															\n");
				strQuery.append("and b.cr_status in ('9','8')																																	\n");
				if (emgSw.equals("2")){
					strQuery.append("and a.cr_passok='2'                                                                                                        \n");
				}				
				if(!"".equals(sDate) && sDate != null)
				
				//strQuery.append("and to_char(b.cr_prcdate,'yyyy/mm/dd') >= ?																											\n");
				strQuery.append("and b.cr_prcdate >= trunc(to_date(?, 'yyyy/mm/dd'), 'dd')																											\n");
				if(!"".equals(eDate) && eDate != null)
				//strQuery.append("and to_char(b.cr_prcdate,'yyyy/mm/dd') <= ?																											\n");
				strQuery.append("and b.cr_prcdate < trunc(to_date(?, 'yyyy/mm/dd')+1, 'dd')																											\n");

				strQuery.append("and a.cr_acptno = b.cr_acptno																															\n");
				strQuery.append("and b.cr_syscd  = e.cm_syscd																					\n");
				strQuery.append("and b.cr_rsrccd = e.cm_rsrccd																					\n");
				strQuery.append("and SUBSTR(e.CM_INFO, 26, 1) = '0'																				\n");
				strQuery.append("and SUBSTR(e.CM_INFO, 02, 1) = '1'																				\n");
				strQuery.append("and b.cr_itemid = d.cr_itemid																					\n");
				strQuery.append("and    '20111019' < (select to_char(cr_acptdate,'yyyymmdd')from cmr1000 where cr_acptno = b.cr_baseno)			\n");				
		        strQuery.append("UNION ALL select 																																						\n");
				strQuery.append("(select cm_deptname from cmm0100 where a.cr_teamcd = cm_deptcd) as cm_deptname , d.cr_rsrcname, 													\n");
				strQuery.append("(select cm_codename from cmm0020 where cm_macode = 'JAWON' and b.cr_rsrccd = cm_micode) as cm_codename,					\n");
				strQuery.append("(select cm_username from cmm0040 where cm_userid = a.cr_editor) as cm_username, 														\n");
				strQuery.append("to_char(b.cr_prcdate,'yyyy-mm-dd') as cr_prcdate,																																				\n");
				strQuery.append("b.cr_rsrcname, 																																			\n");
				strQuery.append("a.cr_sayu, f.cm_sysmsg,																																				\n");
				strQuery.append("a.cr_qrycd, a.cr_passok,a.cr_acptno,																																					\n");
				strQuery.append("(select cm_codename from cmm0020 where cm_macode ='REQPASS' and cm_micode = a.cr_passok) passok,          \n");				
				strQuery.append("(select cm_codename from cmm0020 where cm_macode = 'CHECKIN' AND cm_micode = DECODE(b.CR_VERSION, 1, '03', b.cr_qrycd)) as checkin												\n");
				strQuery.append("from cmr1000 a, cmr1010 b	,cmm0102 c	, cmr0020 d, CMM0036 e, cmm0030 f  , cmr0020 g																			\n");
			    strQuery.append("where a.cr_qrycd IN ('04')																														    \n");
				strQuery.append("and b.CR_VERSION = 1																													    		\n");
				strQuery.append("and substr(a.cr_acptno,5,2) ='04'																													    		\n");
				strQuery.append("and a.cr_syscd = f.cm_syscd																													   	\n");
				strQuery.append("and g.cr_itemid = b.cr_itemid 																											   	\n");
				if(!"".equals(UserNo) && UserNo != null){
					strQuery.append("   and a.cr_editor = ?																																	\n");
				}	
				if (!"".equals(strDept) && strDept != null)  {
					strQuery.append("   and a.cr_teamcd = ?																																\n");
				}
				if(!"".equals(ProgName) && ProgName != null){
					strQuery.append("and upper(d.cr_rsrcname) like upper(?)                                                                                           \n");
				}
				strQuery.append("and b.cr_jobcd = c.cm_jobcd   																															\n");
				strQuery.append("and b.cr_status in ('9','8')																																	\n");
				if (emgSw.equals("2")){
					strQuery.append("and a.cr_passok='2'                                                                                                        \n");
				}				
				if(!"".equals(sDate) && sDate != null)
				strQuery.append("and to_char(b.cr_prcdate, 'yyyy/mm/dd') >= ?																									\n");
				if(!"".equals(eDate) && eDate != null)
				strQuery.append("and to_char(b.cr_prcdate, 'yyyy/mm/dd') <= ?																											\n");
				strQuery.append("and a.cr_acptno = b.cr_acptno																															\n");
				strQuery.append("and b.cr_syscd  = e.cm_syscd																					\n");
				strQuery.append("and b.cr_rsrccd = e.cm_rsrccd																					\n");
				strQuery.append("and SUBSTR(e.CM_INFO, 26, 1) = '0'																				\n");
				strQuery.append("and b.cr_itemid = d.cr_itemid																					\n");
				//strQuery.append("and    '20111019' < (select to_char(cr_acptdate,'yyyymmdd')from cmr1000 where cr_acptno = b.cr_baseno)			\n");
				//strQuery.append("order by b.cr_prcdate																																		\n");
				pstmt = conn.prepareStatement(strQuery.toString());
				pstmt = new LoggableStatement(conn,strQuery.toString());
				
				if(!"".equals(UserNo) && UserNo != null){
					pstmt.setString(++Cnt, UserNo);
				}
				if (!"".equals(strDept) && strDept != null) {
	            	pstmt.setString(++Cnt, strDept);
	            }
				if(!"".equals(ProgName) && ProgName != null){
					pstmt.setString(++Cnt, "%"+ProgName+"%");
				}
		        if(!"".equals(sDate) && sDate != null && !"".equals(eDate) && eDate != null){
		        	pstmt.setString(++Cnt, sDate);
		        	pstmt.setString(++Cnt, eDate);
		        	
		        }
				if(!"".equals(UserNo) && UserNo != null){
					pstmt.setString(++Cnt, UserNo);
					
				}
				if (!"".equals(strDept) && strDept != null) {
	            	pstmt.setString(++Cnt, strDept);
	            }
				if(!"".equals(ProgName) && ProgName != null){
					pstmt.setString(++Cnt, "%"+ProgName+"%");
				}
		        if(!"".equals(sDate) && sDate != null && !"".equals(eDate) && eDate != null){
		        	pstmt.setString(++Cnt, sDate);
		        	pstmt.setString(++Cnt, eDate);
		        	
		        }
			}
			ecamsLogger.error(((LoggableStatement)pstmt).getQueryString());
			rs = pstmt.executeQuery();
			
			while(rs.next()){ 
				rst = new HashMap<String, String>();
				
				rst.put("cm_deptname", rs.getString("cm_deptname"));
				rst.put("cm_codename", rs.getString("cm_codename"));
				rst.put("cr_prcdate", rs.getString("cr_prcdate"));
				rst.put("cr_rsrcname", rs.getString("cr_rsrcname"));
				rst.put("cr_sayu", rs.getString("cr_sayu"));				
				rst.put("cm_username", rs.getString("cm_username"));
				rst.put("cr_qrycd", rs.getString("cr_qrycd"));
				if(strQryCd.equals("01")){
					rst.put("qrycd", rs.getString("qrycd"));
					rst.put("status", rs.getString("status"));
				}else {	
					rst.put("checkin", rs.getString("checkin"));
				}				
				rst.put("cm_sysmsg", rs.getString("cm_sysmsg"));
        		rst.put("cr_passok",  rs.getString("cr_passok"));     
        		rst.put("passok",  rs.getString("passok"));  				
				rsval.add(rst);
				rst = null;

			}
			rs.close();
			pstmt.close();
			conn.close();
			
			rs = null;
			pstmt = null;
			conn = null;
			
			rtObj = rsval.toArray();
			rsval.clear();
			rsval = null;
    		
    		return rtObj; 
		}
		catch (SQLException sqlexception) {
			sqlexception.printStackTrace();
			ecamsLogger.error("## Cmr3400.getReqList() SQLException START ##");
			ecamsLogger.error("## Error DESC : ", sqlexception);	
			ecamsLogger.error("## Cmr3400.getReqList() SQLException END ##");			
			throw sqlexception;
		} catch (Exception exception) {
			exception.printStackTrace();
			ecamsLogger.error("## Cmr3400.getReqList() Exception START ##");				
			ecamsLogger.error("## Error DESC : ", exception);	
			ecamsLogger.error("## Cmr3400.getReqList() Exception END ##");				
			throw exception; 
		}finally{
			if (strQuery != null)	strQuery = null;
			if (rtObj != null)	rtObj = null;			
			if (rs != null)     try{rs.close();}catch (Exception ex){ex.printStackTrace();}
			if (pstmt != null)  try{pstmt.close();}catch (Exception ex2){ex2.printStackTrace();}
			if (conn != null){
				try{
					ConnectionResource.release(conn);
				}catch(Exception ex3){
					ecamsLogger.error("## Cmr3400.getReqList() connection release exception ##");
					ex3.printStackTrace();
				}
			}
		}		
		
	} 
}
