/*****************************************************************************************
	1. program ID	: Cmp4000.java
	2. create date	: 2008. 08. 10
	3. auth		    : is.choi
	4. update date	: 
	5. auth		    : 
	6. description	: 1. User Deploy
*****************************************************************************************/

package app.eCmp;

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

/**
 * @author bigeyes
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class Cmp4000{    
	

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
public Object[] getReqList(String QryDt) throws SQLException, Exception {
		Connection        conn        = null;
		PreparedStatement pstmt       = null;
		ResultSet         rs          = null;
		StringBuffer      strQuery    = new StringBuffer();
		int               parmCnt     = 0;
		TimeSch			  timesch	  = new TimeSch();
		String 			  Nowdate     = null;
		ArrayList<HashMap<String, String>>  rsval = new ArrayList<HashMap<String, String>>();
		HashMap<String, String>			  rst		  = null;
		ConnectionContext connectionContext = new ConnectionResource();		
		
		try {	

			conn = connectionContext.getConnection();	

			strQuery.setLength(0);
			strQuery.append("select  cc_isrid, cc_reqdept_deptname deptname , cc_docno1 , cc_isrtitle ,																  \n");
			strQuery.append("       substr(cc_createdt,5,2)||'월 '||substr(cc_createdt,7,2)||'일'                             as cc_createdt,  \n");
			strQuery.append("       substr(cc_reqenddt,5,2)||'월 '||substr(cc_reqenddt,7,2)||'일'                             as cc_reqenddt,  \n");
			strQuery.append("       decode(cc_expday,NULL,NULL,substr(cc_expday,5,2)  ||'월 '||substr(cc_expday,7,2)  ||'일') as cc_expday,    \n");
			strQuery.append("       decode(end_date, NULL,NULL,substr(end_date,5,2)   ||'월 '||substr(end_date,7,2)   ||'일') as end_date,     \n");
			strQuery.append("       decode(F1B1A_SUM,0,NULL,F1B1A_SUM) as AA,                                                                  \n");
			strQuery.append("       decode(F1B1B_SUM,0,NULL,F1B1B_SUM) as BB,                                                                  \n");
			strQuery.append("       decode(F1B1D_SUM,0,NULL,F1B1D_SUM) as DD,                                                                  \n");
			strQuery.append("       decode(F1B1C_SUM,0,NULL,F1B1C_SUM) as CC,                                                                  \n");
			strQuery.append("       decode(ETIME    ,0,NULL,ETIME)     as cc_expruntime,                                                               \n");
			strQuery.append("       decode(ETIME,NULL,'업무요건 확인중',NULL) as BIGO                                                             \n");
			strQuery.append("from                                                                                                              \n");
			strQuery.append("        (                                                                                                         \n");
			strQuery.append("        select cc_isrid , cc_reqdept_deptname, cc_docno1, cc_isrtitle,                                          \n");
			strQuery.append("               max(cc_createdt) cc_createdt, max(cc_reqenddt) cc_reqenddt,                                        \n");
			strQuery.append("               max(cc_expday) cc_expday, min(end_date) end_date,                                                  \n");
			strQuery.append("               sum(decode(cc_recvpart, 'F1B1A', cc_expruntime, 0)) as F1B1A_SUM,                                  \n");
			strQuery.append("               sum(decode(cc_recvpart, 'F1B1B', cc_expruntime, 0)) as F1B1B_SUM,                                  \n");
			strQuery.append("               sum(decode(cc_recvpart, 'F1B1D', cc_expruntime, 0)) as F1B1D_SUM,                                  \n");
			strQuery.append("               sum(decode(cc_recvpart, 'F1B1C', cc_expruntime, 0)) as F1B1C_SUM,                                  \n");
			strQuery.append("               sum(cc_expruntime) as ETIME,                                                                       \n");
			strQuery.append("               sum(cc_realtime)   as RTIME                                                                        \n");
			strQuery.append("        from                                                                                                      \n");
			strQuery.append("                (                                                                                                 \n");
			strQuery.append("                select Q.cc_isrid, S.cc_isrsub, Q.cc_reqdept,                                                     \n");
			strQuery.append("                			(select cm_deptname from cmm0100 where cm_deptcd = Q.cc_reqdept) as cc_reqdept_deptname,     \n");
			strQuery.append("                			 Q.cc_docno1,Q.cc_isrtitle, to_char(Q.cc_creatdt,'YYYYMMDD') cc_createdt, Q.cc_reqenddt,     \n");
			strQuery.append("                       S.cc_expday, J.cc_jobdate, S.cc_realedday,                                                 \n");
			strQuery.append("                       decode(J.cc_jobdate,NULL,S.cc_realedday,J.cc_jobdate) as end_date,                         \n");
			strQuery.append("                       substr(S.cc_recvpart,1,5) as cc_recvpart,                                                  \n");
			strQuery.append("                       (select cm_deptname from cmm0100 where cm_deptcd = (select decode(CM_HANDYN,'Y',CM_UPDEPTCD,CM_DEPTCD) \n"); 
			strQuery.append("											from cmm0100 where cm_deptcd=S.cc_recvpart)) as cc_recvpart_deptname, \n"); 
			strQuery.append("                       S.cc_recvgbn, S.cc_expruntime, S.cc_realtime                                               \n");
			strQuery.append("                  from cmc0100 Q, cmc0110 S, cmc0250 J                                                            \n");
			strQuery.append("                 where Q.cc_isrid  = S.cc_isrid (+)                                                               \n");
			strQuery.append("                   and S.cc_isrid  = J.cc_isrid (+)                                                               \n");
			strQuery.append("                   and S.cc_isrsub = J.cc_isrsub(+)                                                               \n");
			strQuery.append("                   and Q.cc_reqdept not like 'F1%'                                                                \n");
			strQuery.append("                   and    S.cc_recvpart not like 'F1A1%'                                                          \n");
			strQuery.append("                   and S.cc_recvgbn not in ('0','3')                                                              \n");
			//strQuery.append("                   and (Q.cc_creatdt > sysdate - 7                                                                \n");
			strQuery.append("                   and (q.cc_creatdt > to_date(?, 'yyyymmddhh24miss') - 7                                         \n");
			strQuery.append("                     or decode(J.cc_jobdate,NULL,S.cc_realedday,J.cc_jobdate) is null                             \n");
			strQuery.append("                       )                                                                                          \n");
			strQuery.append("                )                                                                                                 \n");
			strQuery.append("        group by cc_isrid, cc_reqdept_deptname, cc_docno1, cc_isrtitle                                            \n");
			strQuery.append("        order by cc_reqdept_deptname, cc_isrid                                                                    \n");
			strQuery.append("        )                                                                                                         \n");
			strQuery.append("order by cc_reqdept_deptname, cc_isrid                                                                                \n");
			pstmt = conn.prepareStatement(strQuery.toString());	
			//pstmt = new LoggableStatement(conn,strQuery.toString());
			pstmt.setString(++parmCnt, QryDt+"000000");
			//ecamsLogger.error(((LoggableStatement)pstmt).getQueryString());
	        rs = pstmt.executeQuery();
	        rsval.clear();
	        
			while (rs.next()){	
				rst = new HashMap<String, String>();
				rst.put("ID", Integer.toString(rs.getRow()));
				rst.put("cc_isrid",rs.getString("cc_isrid"));
				rst.put("deptname",rs.getString("deptname"));
				rst.put("cc_docno1",rs.getString("cc_docno1"));
				rst.put("cc_isrtitle",rs.getString("cc_isrtitle"));
				rst.put("cc_createdt",rs.getString("cc_createdt"));
				rst.put("cc_reqenddt",rs.getString("cc_reqenddt"));
				rst.put("cc_expday",rs.getString("cc_expday"));
				rst.put("end_date",rs.getString("end_date"));
				rst.put("AA",rs.getString("AA"));
				rst.put("BB",rs.getString("BB"));
				rst.put("DD",rs.getString("DD"));
				rst.put("CC",rs.getString("CC"));
				rst.put("cc_expruntime",rs.getString("cc_expruntime"));
				rst.put("bigo",rs.getString("bigo"));
				rsval.add(rst);
				rst = null;
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
			ecamsLogger.error("## Cmp4000.getSelect_List() SQLException START ##");
			ecamsLogger.error("## Error DESC : ", sqlexception);	
			ecamsLogger.error("## Cmp4000.getSelect_List() SQLException END ##");			
			throw sqlexception;
		} catch (Exception exception) {
			exception.printStackTrace();
			ecamsLogger.error("## Cmp4000.getSelect_List() Exception START ##");				
			ecamsLogger.error("## Error DESC : ", exception);	
			ecamsLogger.error("## Cmp4000.getSelect_List() Exception END ##");				
			throw exception;
		}finally{
			if (strQuery != null)	strQuery = null;		
			if (rs != null)     try{rs.close();}catch (Exception ex){ex.printStackTrace();}
			if (pstmt != null)  try{pstmt.close();}catch (Exception ex2){ex2.printStackTrace();}
			if (conn != null){
				try{
					ConnectionResource.release(conn);
				}catch(Exception ex3){
					ecamsLogger.error("## Cmp4000.getSelect_List() connection release exception ##");
					ex3.printStackTrace();
				}
			}
		}		
	}//end of getSelect_List() method statement
}//end of Cmp4000 class statement
