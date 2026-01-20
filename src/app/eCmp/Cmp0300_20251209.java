/*****************************************************************************************
	1. program ID	: Cmp3300.java
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
//import java.io.BufferedReader;

import org.apache.commons.lang.StringUtils;

//import oracle.net.aso.r;

import org.apache.logging.log4j.Logger;
//import org.w3c.dom.Document;

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
public class Cmp0300_20251209{    
	
    /**
     * Logger Class Instance Creation
     * logger
     */
	   
    Logger ecamsLogger  = EcamsLogger.getLoggerInstance();
     
	/**         
	 * getProgList  프로그램갯수
	 * @param String UserId,String SysCd,String JobCd,String StDate,boolean jobSw
	 * @return List Object[]
	 * @throws SQLException
	 * @throws Exception
	 * ,boolean jobSw
	 */
	
	public Object[] getProgList(String UserId,String SysCd,String JobCd,String StDate) throws SQLException, Exception {
		Connection        conn        = null;
		PreparedStatement pstmt       = null;
		ResultSet         rs          = null;
	
		StringBuffer      strQuery    = new StringBuffer();
	 
		boolean			  isFirst	  = true;
		ArrayList<HashMap<String, String>>  rsval = new ArrayList<HashMap<String, String>>();
		HashMap<String, String>			    rst		  		  = null;
		HashMap<String,				 String>			    rst2		  		  = null;
		Object[] returnObjectArray = null;
		ConnectionContext connectionContext = new ConnectionResource();		
	
		int 			  totalbeforeCnt	 = 0;
		int 			  totalopenCnt		 = 0;
		int 			  totalcloseCnt		 = 0;
		int 			  totalchkInCnt		 = 0;
		int 			  totalchkOutCnt     = 0;
		int 			  totalnewCnt		 = 0;
		int 			  totalcurrOutCnt    = 0;
		int				  totalrentCnclCnt	 = 0; 
		int 			  totalih			 = 0;
		int 			  paramCnt = 0;
	 
		
		try {
			conn = connectionContext.getConnection();
			strQuery.setLength(0);
			strQuery.append("SELECT a.cm_syscd,a.cm_sysmsg,b.cm_jobcd,b.cm_jobname,NVL(SUM(cnt1), 0)  before,NVL(SUM(cnt2),0) new,								\n");
			strQuery.append("       NVL(SUM(cnt3),0) rent,NVL(SUM(cnt4),0) del,NVL(SUM(cnt5),0) chg, NVL(SUM(cnt6),0) today,                                  	\n");
			strQuery.append("       NVL(SUM(cnt7),0) currOut ,NVL(SUM(cnt8),0) rentCncl,	NVL(SUM(cnt9),0) ih	                                                \n");
			strQuery.append("      FROM   cmm0030 a, cmm0102 b, cmm0034 c, 																                    	\n");
			strQuery.append("       (SELECT  x.cr_syscd, x.cr_jobcd, COUNT(*) cnt1, 0 cnt2, 0 cnt3, 0 cnt4 , 0 cnt5, 0 cnt6, 0 cnt7	, 0 cnt8, 0 cnt9	    	\n");
			strQuery.append("               FROM																						                        \n");
			strQuery.append("               (SELECT  d.cr_syscd, d.cr_jobcd, d.cr_itemid												                        \n");
			strQuery.append("                        FROM   cmr0020 d, cmm0036 e														                        \n");
			strQuery.append("                                WHERE  d.cr_opendate1< ?			 					                                            \n");
			strQuery.append("                                AND    ( d.cr_clsdate IS NULL OR TO_CHAR(d.cr_clsdate, 'yyyymmdd') >= ?  )				        	\n");
			strQuery.append("                                and   d.cr_syscd = e.cm_syscd and d.cr_rsrccd = e.cm_rsrccd and substr(e.cm_info, 26,1) = '0'		\n");
			strQuery.append("                                AND   d.CR_LSTVER > 0																				\n");
			strQuery.append("                                GROUP  BY d.cr_syscd, d.cr_jobcd, d.cr_itemid			 								        	\n");
			strQuery.append("               ) x																								 	            	\n");
			strQuery.append("               GROUP  BY x.cr_syscd, x.cr_jobcd																	                \n");
			paramCnt++;
			paramCnt++;
			strQuery.append("        UNION ALL																									            	\n");
			strQuery.append("       SELECT  e.cr_syscd, e.cr_jobcd, 0 cnt1, COUNT(*) cnt2, 0 cnt3, 0 cnt4 , 0 cnt5, 0 cnt6, 0 cnt7, 0 cnt8, 0 cnt9              \n");                      
			strQuery.append("         FROM  cmr0020 e, cmm0036 d                                                                                               	\n");                                                                                           
			strQuery.append("        WHERE  cr_opendate1 = ?                                                                                 					\n");                                                                 			   
			strQuery.append("          and  e.cr_syscd = d.cm_syscd and e.cr_rsrccd = d.cm_rsrccd and substr(d.cm_info, 26,1) = '0'                            	\n");                                  
			strQuery.append(" 		   and  substr(e.cr_acptno,5,2) != '16'         																			\n");                                                                                                      
			strQuery.append(" 		   and  not exists(select 'x' from cmr0021 where cr_itemid = e.cr_itemid and cr_qrycd ='16')    							\n");			
			strQuery.append(" 		 GROUP  BY e.cr_syscd, e.cr_jobcd      																						\n");  
			strQuery.append("        UNION  ALL																									            	\n");
			paramCnt++;
			strQuery.append("        SELECT  e.cr_syscd, e.cr_jobcd, 0 cnt1, 0 cnt2, COUNT(*) cnt3, 0 cnt4 , 0 cnt5, 0 cnt6, 0 cnt7, 0 cnt8	, 0 cnt9			\n");	
			strQuery.append("                FROM       cmr1000 d, cmr1010 e																	                \n");
			strQuery.append("                WHERE      d.cr_acptno = e.cr_acptno																                \n");
			strQuery.append("                AND        e.cr_status IN('9', '8')																                \n");
			strQuery.append("                AND        TO_CHAR(e.cr_prcdate, 'yyyymmdd') = ? 													            	\n");
			strQuery.append("                AND        d.cr_qrycd = '01' 															            				\n");
			strQuery.append("                AND        e.cr_acptno || e.cr_itemid  In (														                \n");
			strQuery.append("                                    SELECT b.cr_acptno || b.cr_itemid	 											            	\n");
			strQuery.append("                                    FROM cmr1000 a, cmr1010 b WHERE a.cr_acptno = b.cr_acptno						            	\n");
			strQuery.append("                                    AND to_char(b.cr_prcdate,'yyyymmdd') = ?		 								                \n");
			strQuery.append("                                    AND b.cr_status IN ('9','8') AND a.cr_qrycd ='01'									            \n");
			strQuery.append("                                    MINUS																				            \n");
			strQuery.append("                                    SELECT b.cr_baseno || b.cr_itemid 													            \n");
			strQuery.append("                                    FROM cmr1000 a, cmr1010 b WHERE a.cr_acptno = b.cr_acptno							            \n");
			strQuery.append("                                    AND to_char(b.cr_prcdate,'yyyymmdd') = ? 											            \n");
			strQuery.append("                                    AND b.cr_status IN ('9','8') AND a.cr_qrycd ='11'									            \n");
			strQuery.append("                                  	)																					            \n");
			strQuery.append("                GROUP  BY e.cr_syscd, e.cr_jobcd																	                \n");
			strQuery.append("                UNION ALL																	                        	            \n");
			paramCnt++;
			paramCnt++;
			paramCnt++;
			strQuery.append("       SELECT  cr_syscd, cr_jobcd, 0 Cnt1, 0 Cnt2, 0 cnt3, COUNT(*) cnt4 , 0 cnt5, 0 cnt6, 0 cnt7, 0 cnt8	, 0 cnt9				\n");	
			strQuery.append("         FROM  cmr0020 D, CMM0036 E                                                                       							\n");	                                            
			strQuery.append("   	 WHERE  TO_CHAR(D.cr_clsdate, 'yyyymmdd') = ?                                                                               \n");	
			strQuery.append("		   and  D.cr_opendate1 is NOT NULL                                                                                          \n");	
			strQuery.append("		   and  D.cr_opendate1 < '20221004'                                                                                         \n");	
			strQuery.append("		   and  d.cr_syscd = e.cm_syscd and d.cr_rsrccd = e.cm_rsrccd and substr(e.cm_info, 26,1) = '0'								\n");
			strQuery.append("        GROUP  BY  cr_syscd, cr_jobcd																		            			\n");
			paramCnt++;
			strQuery.append("       UNION  ALL																									     	       	\n");
			strQuery.append("       SELECT  e.cr_syscd, e.cr_jobcd, 0 cnt1, 0 cnt2, 0 cnt3, 0 cnt4 , COUNT(*) cnt5, 0 cnt6, 0 cnt7, 0 cnt8	, 0 cnt9			\n");	
			strQuery.append("               FROM   cmr1000 d, cmr1010 e, cmm0036 f																				\n");
			strQuery.append("               WHERE  d.cr_acptno = e.cr_acptno																	       	     	\n");
			strQuery.append("               AND    e.cr_status IN('9', '8')																		        	    \n");
			strQuery.append("               AND    TO_CHAR(e.cr_prcdate, 'yyyymmdd') = ?		 													            \n");
			strQuery.append("               AND    d.cr_qrycd = '04'																			            	\n");
			strQuery.append("               AND    (e.cr_qrycd = '04' and e.cr_version > 1)																		\n");
			strQuery.append("				and    e.cr_syscd  = f.cm_syscd																						\n");
			strQuery.append("				and    e.cr_rsrccd = f.cm_rsrccd																					\n");
			strQuery.append("				and    SUBSTR(f.CM_INFO, 26, 1) = '0'																				\n");
			strQuery.append("				and    SUBSTR(f.CM_INFO, 02, 1) = '1'																				\n");
			strQuery.append("            	and   '20111019' < (select to_char(cr_acptdate,'yyyymmdd') from cmr1000 where e.cr_baseno = cr_acptno)	   	     	\n");
			strQuery.append("               GROUP  BY e.cr_syscd, e.cr_jobcd																	       	     	\n");
			paramCnt++;
			
			strQuery.append("      UNION  ALL																									     	       	\n");
			strQuery.append("     SELECT  d.cr_syscd, d.cr_jobcd, 0 cnt1, 0 cnt2, 0 cnt3, 0 cnt4, 0 cnt5, COUNT(*) cnt6, 0 cnt7, 0 cnt8, 0 cnt9					\n");
			strQuery.append("	    FROM   cmr0020 d, cmm0036 e                                                                                                 \n");                 
			strQuery.append("	   WHERE  d.cr_opendate1 <= ?                                                                                                   \n");           
			strQuery.append("		 AND  ( d.cr_clsdate IS NULL OR TO_CHAR(d.cr_clsdate, 'yyyymmdd') > ?  )													\n");
			strQuery.append("		 AND  d.cr_syscd = e.cm_syscd and d.cr_rsrccd = e.cm_rsrccd and substr(e.cm_info, 26,1) = '0'								\n");
			strQuery.append("		 AND  d.CR_LSTVER > 0                                                                    									\n");                                                        
			strQuery.append("	   GROUP  BY d.cr_syscd, d.cr_jobcd 																							\n");
			paramCnt++;
			paramCnt++;
			strQuery.append("       UNION  ALL																									            	\n");
			strQuery.append("SELECT distinct  e.cr_syscd, e.cr_jobcd, 0 cnt1, 0 cnt2, 0 cnt3, 0 cnt4 , 0 cnt5, 0 cnt6, count(*) cnt7, 0 cnt8, 0 cnt9			\n");
			strQuery.append(" FROM       cmr1000 d, cmr1010 e, cmm0036 f, cmr0020 dd															            	\n");
			strQuery.append(" WHERE      d.cr_acptno = e.cr_acptno																	                        	\n");
			strQuery.append(" AND        e.cr_status IN('9', '8')																	                       	 	\n");
			strQuery.append(" AND        to_char(e.cr_prcdate,'yyyymmdd') <= ?														                       		\n");
			strQuery.append(" AND        d.cr_qrycd = '01' 															                        					\n");
			strQuery.append(" AND        e.cr_itemid || e.cr_acptno  In (																			        	\n");
			strQuery.append("                    SELECT b.cr_itemid	|| b.cr_acptno 																            	\n");
			strQuery.append("                     FROM cmr1000 a, cmr1010 b WHERE a.cr_acptno = b.cr_acptno							                        	\n");
			strQuery.append("                      AND        to_char(b.cr_prcdate,'yyyymmdd') <= ?									                        	\n");
			strQuery.append("                     AND b.cr_status IN ('9','8') AND a.cr_qrycd ='01'									                        	\n");
			strQuery.append("                     MINUS																				                        	\n");
			strQuery.append("                     SELECT b.cr_itemid || b.cr_baseno																            	\n");
			strQuery.append("                     FROM cmr1000 a, cmr1010 b WHERE a.cr_acptno = b.cr_acptno							                        	\n");
			strQuery.append("                     AND to_char(b.cr_prcdate,'yyyymmdd') <= ?											                        	\n");
			strQuery.append("                     AND b.cr_status IN ('9','8') AND SUBSTR(a.cr_acptno,5,2) in ('04','11')			                        	\n");
			strQuery.append("					  AND b.cr_qrycd not in ('03','16')										                            			\n");
			strQuery.append("                  	)																					                        	\n");
			strQuery.append("  and    e.cr_syscd  = f.cm_syscd																									\n");
			strQuery.append("  and    e.cr_rsrccd = f.cm_rsrccd																									\n");
			strQuery.append("  and    e.cr_itemid = dd.cr_itemid																								\n");
			strQuery.append("  and    (to_char(dd.cr_clsdate,'yyyymmdd') >= ? or dd.cr_clsdate is null)    														\n");
			strQuery.append("  and    SUBSTR(f.CM_INFO, 26, 1) = '0'																							\n");
			strQuery.append(" GROUP  BY e.cr_syscd, e.cr_jobcd																		                        	\n");
			paramCnt++;
			paramCnt++;
			paramCnt++; 
			paramCnt++;
			strQuery.append(" UNION  ALL																							                            \n");
			strQuery.append("SELECT  e.cr_syscd, e.cr_jobcd, 0 cnt1, 0 cnt2, 0 cnt3, 0 cnt4 , 0 cnt5, 0 cnt6, 0 cnt7, COUNT(*) cnt8	, 0 cnt9             		\n");
			strQuery.append("  FROM  cmr1000 d, cmr1010 e																	               						\n");
			strQuery.append(" WHERE  d.cr_acptno = e.cr_acptno																                					\n");
			strQuery.append("   AND  e.cr_status IN ('9', '8')														                    						\n");
			strQuery.append("   AND  TO_CHAR(e.cr_prcdate, 'yyyymmdd') = ?													               						\n");
			strQuery.append("   AND  d.cr_qrycd = '11' 													                        								\n");
			/*
			strQuery.append("	AND  e.cr_baseno || e.cr_itemid in (                                         													\n");
			strQuery.append("										select f.cr_acptno || f.cr_itemid                                              				\n");
			strQuery.append("										  from cmr1010 f, cmr1000 g                                                   				\n");
			strQuery.append("										 where f.cr_acptno = g.cr_acptno                                          					\n");
			strQuery.append("										   and   f.cr_status in ('9','8')                                        					\n");
			strQuery.append("										   and   g.cr_status in ('9','8')															\n");
			strQuery.append("										   and   to_char(f.cr_prcdate,'yyyymmdd') < ?												\n");
			strQuery.append("										   and   substr(g.cr_acptno,5,2) = '01'														\n");
			strQuery.append("									 	   and   '20111019' < (select to_char(cr_acptdate,'yyyymmdd')								\n");
			strQuery.append("															     from cmr1000 where f.cr_baseno = cr_acptno)						\n");
			strQuery.append("										)																							\n");
			*/
			strQuery.append("   AND not exists (select 'X' 																	    \n");
			strQuery.append("                     FROM cmr1010 																	\n");
			strQuery.append("                    WHERE cr_baseno = e.cr_acptno													\n");						
			strQuery.append("                      AND cr_itemid = e.cr_itemid													\n");						
			strQuery.append("                      AND to_char(cr_prcdate,'yyyymmdd') = to_char(e.cr_prcdate,'yyyymmdd')		\n");
			strQuery.append("                      AND cr_status IN ('9','8') 													\n");
			strQuery.append("                      AND SUBSTR(cr_acptno,5,2) = '11' 											\n");
			strQuery.append("                  )																				\n");
 			strQuery.append("GROUP  BY e.cr_syscd, e.cr_jobcd                                                                                                 	\n");
			strQuery.append("UNION  ALL		 																							                    	\n");
			paramCnt++;
			//paramCnt++;
			strQuery.append("SELECT distinct  e.cr_syscd, e.cr_jobcd, 0 cnt1, 0 cnt2, 0 cnt3, 0 cnt4 , 0 cnt5, 0 cnt6, 0 cnt7, 0 cnt8, count(*) cnt9	    	\n");
			strQuery.append(" FROM       (SELECT distinct cr_syscd, cr_jobcd, cr_itemid from cmr1010 a, cmm0036 b                                           	\n");
			strQuery.append("                         where SUBSTR(cr_acptno,5,2) = '16'                                                                    	\n");
			strQuery.append("                           and cr_version > 0																        				\n");
			strQuery.append("                           and cr_status IN('9', '8')																	        	\n");
			strQuery.append("                           AND to_char(cr_prcdate,'yyyymmdd') = ?	                                                            	\n");
			strQuery.append("							and a.cr_syscd  = b.cm_syscd																			\n");
			strQuery.append("							and a.cr_rsrccd = b.cm_rsrccd																			\n");
			strQuery.append("							and SUBSTR(b.CM_INFO, 26, 1) = '0'																		\n");
			strQuery.append("							and cr_itemid is not null																				\n");
			strQuery.append("                       ) e																		                            	    \n");
			paramCnt++;
			strQuery.append("			GROUP  BY e.cr_syscd, e.cr_jobcd                                                                             			\n");                    
			strQuery.append("			) 	 d                                                                                                  				\n");        
			strQuery.append("       WHERE  c.cm_syscd = a.cm_syscd																								\n");																									
			if(!"".equals(SysCd) && SysCd != null)
				strQuery.append("   AND    c.cm_syscd = ? 																										\n");																																	
			if(!"".equals(JobCd) && JobCd != null) 
				strQuery.append("		 AND    c.cm_jobcd = ?																									\n");
			strQuery.append("       AND    c.cm_jobcd = b.cm_jobcd																								\n");																											
			strQuery.append("       AND    c.cm_syscd = d.cr_syscd(+)																							\n");																									
			strQuery.append("       AND    c.cm_jobcd = d.cr_jobcd(+)																							\n");																																
			//산출물 = "99999", 홈페이지 배포 _ 웹 = "00510", 홈페이지 배포 _ 와스 = "00500"
			strQuery.append("       AND    c.cm_syscd not in('99999','00510','00500')																			\n");																					
			strQuery.append("       GROUP  BY ROLLUP(a.cm_syscd, a.cm_sysmsg, b.cm_jobcd, b.cm_jobname)															\n");														
			strQuery.append("       ORDER BY a.cm_syscd, b.cm_jobcd																								\n");													
																													
			//pstmt = conn.prepareStatement(strQuery.toString());
			pstmt = new LoggableStatement(conn,strQuery.toString());
			
			for(int i=1;i<=paramCnt;i++){
				pstmt.setString(i, StDate);
			}
			
			if (!"".equals(SysCd) && SysCd != null) pstmt.setString(++paramCnt, SysCd);
			if (!"".equals(JobCd) && JobCd != null) pstmt.setString(++paramCnt, JobCd); 
			ecamsLogger.error(((LoggableStatement)pstmt).getQueryString());	
			rs = pstmt.executeQuery();
			
			int openCnt = 0;
		    while(rs.next()){
		    	int chkOutCnt = 0;	
		    	rst = new HashMap<String,String>(); 
		    	if(rs.getString("cm_jobname") == null && rs.getString("cm_jobcd") == null 
		    			&& rs.getString("cm_sysmsg")!=null && rs.getString("cm_syscd")!= null){
		    		rst.put("cm_sysmsg", rs.getString("cm_sysmsg"));
		    		rst.put("cm_syscd", rs.getString("cm_syscd"));
		    		rst.put("cm_jobname","소계");
		    		rst.put("cm_jobcd", rs.getString("cm_jobcd"));
		    		rst.put("beforeCnt", Integer.toString(rs.getInt("before")));
		    		rst.put("newCnt", Integer.toString(rs.getInt("new")));
		    		//rst.put("openCnt", Integer.toString(rs.getInt("today")));
		    		openCnt = rs.getInt("before")+rs.getInt("new")+rs.getInt("ih")-rs.getInt("del");
		    		rst.put("openCnt", Integer.toString(openCnt));
		    		rst.put("closeCnt", Integer.toString(rs.getInt("del")));
		    		rst.put("chkInCnt", Integer.toString(rs.getInt("chg")));
		    		rst.put("chkOutCnt", Integer.toString(rs.getInt("rent")));
		    		rst.put("currOutCnt", Integer.toString(rs.getInt("currOut")));
		    		rst.put("chkOutCnclCnt", Integer.toString(rs.getInt("rentCncl")));
		    		rst.put("ihCnt", Integer.toString(rs.getInt("ih")));
		    	
					isFirst = true;
		    	
		    	}
		    	else if (rs.getString("cm_jobname") != null && rs.getString("cm_jobcd") != null 
		    			&& rs.getString("cm_sysmsg") != null && rs.getString("cm_syscd") != null){
		    		if(isFirst){
		    			rst.put("cm_sysmsg", rs.getString("cm_sysmsg"));
		    			isFirst = false;
		    		}
		    		rst.put("cm_syscd", rs.getString("cm_syscd"));
		    		rst.put("cm_jobname",rs.getString("cm_jobname"));
		    		rst.put("cm_jobcd",rs.getString("cm_jobcd"));
		    		rst.put("beforeCnt", Integer.toString(rs.getInt("before")));
		    		rst.put("newCnt", Integer.toString(rs.getInt("new")));
		    		//rst.put("openCnt", Integer.toString(rs.getInt("today")));
		    		openCnt = rs.getInt("before")+rs.getInt("new")+rs.getInt("ih")-rs.getInt("del");
		    		totalopenCnt = totalopenCnt+openCnt;
		    		rst.put("openCnt", Integer.toString(openCnt));
		    		rst.put("closeCnt", Integer.toString(rs.getInt("del")));
		    		rst.put("chkInCnt", Integer.toString(rs.getInt("chg")));
		    		rst.put("chkOutCnt", Integer.toString(rs.getInt("rent")));
		    		rst.put("currOutCnt", Integer.toString(rs.getInt("currOut")));
		    		rst.put("chkOutCnclCnt", Integer.toString(rs.getInt("rentCncl")));
		    		rst.put("ihCnt", Integer.toString(rs.getInt("ih")));
		    		
		    	}
		    	else if (rs.getString("cm_jobname") == null && rs.getString("cm_jobcd") == null 
		    			&& rs.getString("cm_sysmsg") == null && rs.getString("cm_syscd") == null){
		    		 totalbeforeCnt	 =  rs.getInt("before");
		    		 //totalopenCnt		 =  rs.getInt("today");
		    		 totalcloseCnt		 = rs.getInt("del");
		    		 totalchkInCnt		 = rs.getInt("chg");
		    		 totalchkOutCnt     = rs.getInt("rent");
		    		 totalnewCnt		 = rs.getInt("new");
		    		 totalcurrOutCnt  = rs.getInt("currout");
		    		 totalrentCnclCnt = rs.getInt("rentCncl");
		    		 totalih = rs.getInt("ih");

		    		 continue;
		    	}
		    	else 
		    		continue;
				
				rsval.add(rst);
				rst = null;
		    
			
				
			}//end of while-loop statement
		    
		    rst = new HashMap<String,String>();
		    rst.put("cm_sysmsg","전체");
    	    rst.put("cm_jobname","합계");
			rst.put("beforeCnt", Integer.toString(totalbeforeCnt));
			rst.put("newCnt", Integer.toString(totalnewCnt));
			rst.put("openCnt", Integer.toString(totalopenCnt));
			rst.put("closeCnt", Integer.toString(totalcloseCnt));
			rst.put("chkInCnt", Integer.toString(totalchkInCnt));
			rst.put("chkOutCnt", Integer.toString(totalchkOutCnt));
			rst.put("currOutCnt", Integer.toString(totalcurrOutCnt));
			rst.put("chkOutCnclCnt", Integer.toString(totalrentCnclCnt));
			rst.put("ihCnt", Integer.toString(totalih));
			rsval.add(rst);
			
			rst = null;
	        
	        rs.close();
			pstmt.close();
			conn.close();
			conn = null;
			pstmt = null;
			rs = null;
		
			returnObjectArray = rsval.toArray();
			rsval = null;
			return returnObjectArray;
			
		} catch (SQLException sqlexception) {
			sqlexception.printStackTrace();
			ecamsLogger.error("## Cmp0300.getProgList() SQLException START ##");
			ecamsLogger.error("## Error DESC : ", sqlexception);	
			ecamsLogger.error("## Cmp0300.getProgList() SQLException END ##");			
			throw sqlexception;
		} catch (Exception exception) {
			exception.printStackTrace();
			ecamsLogger.error("## Cmp0300.getProgList() Exception START ##");				
			ecamsLogger.error("## Error DESC : ", exception);	
			ecamsLogger.error("## Cmp0300.getProgList() Exception END ##");				
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
					ecamsLogger.error("## Cmp0300.getProgList() connection release exception ##");
					ex3.printStackTrace();
				}
			}
		}
	}//end of getProgList() method statement
		
		
	public String excelDataMake(ArrayList<HashMap<String,String>> fileList,ArrayList<HashMap<String,String>> prjStep,String UserId,String exlName) throws SQLException, Exception {
		
		ArrayList<HashMap<String, String>>		  rtList	  = new ArrayList<HashMap<String, String>>();
		HashMap<String, String>			  rst		  = null;
		ArrayList<String> headerDef = new ArrayList<String>();
		excelUtil excelutil = new excelUtil();
		SystemPath systempath = new SystemPath();
		int j     = 0;
		String retMsg         = "";
		
		try {	
			headerDef.add("cd_prjno");
			headerDef.add("cd_prjname");
			headerDef.add("rowhap");
			for (j=0;prjStep.size()>j;j++) {
				headerDef.add("col"+prjStep.get(j).get("cm_micode"));
			}

			rtList.clear();	
			rst = new HashMap<String,String>();
			rst.put("cd_prjno", "프로젝트번호");
			rst.put("cd_prjname", "프로젝트명");
			rst.put("rowhap", "합계");
			
			for (j=0;prjStep.size()>j;j++) {
				rst.put("col"+prjStep.get(j).get("cm_micode"), prjStep.get(j).get("cm_codename"));
			}
			rtList.add(rst);
			rst = null;

			for (int i=0;i<fileList.size();i++){			
				rst = new HashMap<String,String>();
				rst.put("cd_prjno", fileList.get(i).get("cd_prjno"));
				rst.put("cd_prjname", fileList.get(i).get("cd_prjname"));
				rst.put("rowhap", fileList.get(i).get("rowhap"));
				for (j=0;prjStep.size()>j;j++) {
					rst.put("col"+prjStep.get(j).get("cm_micode"), fileList.get(i).get("col"+prjStep.get(j).get("cm_micode")));
				}
				rtList.add(rst);
				rst = null;
			}//end of while-loop statement

			String strPath = systempath.getTmpDir("99");
            //ecamsLogger.debug("++++++excel++"+rtList.toString());
			retMsg = excelutil.setExcel(strPath+"/"+exlName, headerDef, rtList);
			excelutil = null;
			systempath = null;
			rtList = null;
			return retMsg;
			
		} catch (Exception exception) {
			exception.printStackTrace();
			ecamsLogger.error("## Cmp3300.excelDataMake() Exception START ##");				
			ecamsLogger.error("## Error DESC : ", exception);	
			ecamsLogger.error("## Cmp3300.excelDataMake() Exception END ##");				
			throw exception;
		}finally{

		}
	}
	
	public String excelDataMakeDetail(ArrayList<HashMap<String,String>> fileList,ArrayList<HashMap<String,String>> prjStep,String UserId,String exlName,boolean jobSw) throws SQLException, Exception {
		
		ArrayList<HashMap<String, String>>		  rtList	  = new ArrayList<HashMap<String, String>>();
		HashMap<String, String>			  rst		  = null;
		ArrayList<String> headerDef = new ArrayList<String>();
		excelUtil excelutil = new excelUtil();
		SystemPath systempath = new SystemPath();
		int j     = 0;
		String retMsg         = "";
		
		try {	
			headerDef.add("cm_sysmsg");
			if (jobSw == true) headerDef.add("cm_jobname");
			headerDef.add("rowhap");
			for (j=0;prjStep.size()>j;j++) {
				headerDef.add("col"+prjStep.get(j).get("cm_micode"));
			}

			rtList.clear();
			rst = new HashMap<String,String>();
			rst.put("cm_sysmsg", "시스템");
			if (jobSw == true) rst.put("cm_jobname", "업무명");
			rst.put("rowhap", "합계");
			
			for (j=0;prjStep.size()>j;j++) {
				rst.put("col"+prjStep.get(j).get("cm_micode"), prjStep.get(j).get("cm_codename"));
			}
			rtList.add(rst);
			rst = null;

			for (int i=0;i<fileList.size();i++){			
				rst = new HashMap<String,String>();
				rst.put("cm_sysmsg", fileList.get(i).get("cm_sysmsg"));
				if (jobSw == true) rst.put("cm_jobname", fileList.get(i).get("cm_jobname"));
				rst.put("rowhap", fileList.get(i).get("rowhap"));
				for (j=0;prjStep.size()>j;j++) {
					rst.put("col"+prjStep.get(j).get("cm_micode"), fileList.get(i).get("col"+prjStep.get(j).get("cm_micode")));
				}
				rtList.add(rst);
				rst = null;
			}//end of while-loop statement

			String strPath = systempath.getTmpDir("99");
            //ecamsLogger.debug("++++++excel++"+rtList.toString());
			retMsg = excelutil.setExcel(strPath+"/"+exlName, headerDef, rtList);
			excelutil = null;
			systempath = null;
			rtList = null;
			return retMsg;
			
		} catch (Exception exception) {
			exception.printStackTrace();
			ecamsLogger.error("## Cmp3300.excelDataMake() Exception START ##");				
			ecamsLogger.error("## Error DESC : ", exception);	
			ecamsLogger.error("## Cmp3300.excelDataMake() Exception END ##");				
			throw exception;
		}finally{

		}
	}

	public Object[] getRsrcCd(String UserId,String SysCd) throws SQLException, Exception {
		Connection        conn        = null;
		PreparedStatement pstmt       = null;
		ResultSet         rs          = null;
		//ResultSet         rs2         = null;
		StringBuffer      strQuery    = new StringBuffer();
		//String            strReqGbn   = "";
		int               parmCnt     = 0;
		String            secuYn      = "";
		UserInfo          userinfo    = new UserInfo();
		Object[] returnObjectArray = null;
	
		ArrayList<HashMap<String, String>>  rsval = new ArrayList<HashMap<String, String>>();
		HashMap<String, String>			  rst		  = null;
		ConnectionContext connectionContext = new ConnectionResource();		
		
		try {
			conn = connectionContext.getConnection();
			
			secuYn = userinfo.getSecuInfo(UserId);
			
			strQuery.append("select b.cm_micode,b.cm_codename from cmm0036 a,cmm0020 b    \n"); 
			strQuery.append(" where a.cm_closedt is null                                  \n");
			if (SysCd != "" && SysCd != null) strQuery.append("and a.cm_syscd=?           \n");
			else if (secuYn.equals("0")) {
			   strQuery.append("and a.cm_syscd in (select distinct cm_syscd from cmm0044  \n");
			   strQuery.append("                    where cm_userid=? and cm_closedt is null) \n");
			}
			strQuery.append("   and a.cm_rsrccd not in (select cm_samersrc from cmm0037   \n");			
			strQuery.append("                            where cm_syscd=a.cm_syscd)       \n");					
			strQuery.append("   and substr(a.cm_info,26,1)='0'                            \n");	
			strQuery.append("   and b.cm_macode='JAWON'                                   \n");				
			strQuery.append("   and b.cm_micode=decode(a.cm_rsrccd,'46','12','47','12','48','12','49','12','50','12',a.cm_rsrccd) \n");				
			strQuery.append(" group by b.cm_micode,b.cm_codename                          \n");
			strQuery.append(" order by b.cm_micode,b.cm_codename                          \n");
	        
			pstmt = conn.prepareStatement(strQuery.toString());	
	//		pstmt = new LoggableStatement(conn,strQuery.toString());
	        if (SysCd != "" && SysCd != null) pstmt.setString(++parmCnt, SysCd);
	        else if (secuYn.equals("0")) pstmt.setString(++parmCnt, UserId);
	 //       //ecamsLogger.error(((LoggableStatement)pstmt).getQueryString());           
	        rs = pstmt.executeQuery();	
	        
			while (rs.next()){	
				rst = new HashMap<String, String>();
				rst.put("ID", Integer.toString(rs.getRow()));
				rst.put("cm_micode",rs.getString("cm_micode"));
				rst.put("cm_codename",rs.getString("cm_codename"));
				rsval.add(rst);
				rst = null;
			}//end of while-loop statement
			
			rs.close();
			pstmt.close();
			conn.close();
			
			rs = null;
			pstmt = null;
			conn = null;
			
			returnObjectArray = rsval.toArray();
			
			rsval = null;
			//ecamsLogger.debug(rsval.toString());		
			return returnObjectArray;
			
		} catch (SQLException sqlexception) {
			sqlexception.printStackTrace();
			ecamsLogger.error("## Cmp3500.getRsrcCd() SQLException START ##");
			ecamsLogger.error("## Error DESC : ", sqlexception);	
			ecamsLogger.error("## Cmp3500.getRsrcCd() SQLException END ##");			
			throw sqlexception;
		} catch (Exception exception) {
			exception.printStackTrace();
			ecamsLogger.error("## Cmp3500.getRsrcCd() Exception START ##");				
			ecamsLogger.error("## Error DESC : ", exception);	
			ecamsLogger.error("## Cmp3500.getRsrcCd() Exception END ##");				
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
					ecamsLogger.error("## Cmp3500.getRsrcCd() connection release exception ##");
					ex3.printStackTrace();
				}
			}
		}
	}//end of getRsrcCd() method statement
	
	public Object[] getCountList(String UserId,String SysCd,String DecaCd,String StDate) throws SQLException, Exception {
		Connection        conn        = null;
		PreparedStatement pstmt       = null;
		ResultSet         rs          = null;
		StringBuffer      strQuery    = new StringBuffer();
		PreparedStatement pstmt2       = null;
		ResultSet         rs2          = null;
		StringBuffer      strQuery2    = new StringBuffer();
		
		ArrayList<HashMap<String, String>>  rsval = new ArrayList<HashMap<String, String>>();
		HashMap<String, String>			    rst		  = null;
	
		Object[] returnObjectArray = null;
		ConnectionContext connectionContext = new ConnectionResource();		
	
		int n_Code = 0;
		
		try {
			conn = connectionContext.getConnection();
	        
			strQuery2.setLength(0);
			strQuery2.append("SELECT /*+ RULE */ A.CM_MICODE, A.CM_codename, NVL(SUM(B.CNT1), 0) as cnta, NVL(SUM(B.CNT2), 0) as cntb 							   	\n"); 
			strQuery2.append("  FROM CMM0020 A,                                                                                                                    	\n"); 
			strQuery2.append("		 ( SELECT NVL(C.CR_CHGCODE, '07') CR_CHGCODE, SUM(1) CNT1, 0 CNT2                                                              	\n"); 
			strQuery2.append("			 FROM CMR1000 A, CMR1010 B, (SELECT CR_ACPTNO, MIN(CR_CHGCODE) CR_CHGCODE FROM CMR0900 GROUP BY CR_ACPTNO) C, CMM0036 D    	\n"); 
			strQuery2.append("			WHERE A.CR_ACPTNO = B.CR_ACPTNO                                                                                            	\n"); 
			strQuery2.append("			  AND TO_CHAR(B.CR_PRCDATE,'yyyymmdd') = ?                                                                             		\n"); 
			strQuery2.append("		  	  AND B.CR_SYSCD  = D.CM_SYSCD                                                                                            	\n"); 
			strQuery2.append("		  	  AND B.CR_RSRCCD = D.CM_RSRCCD                                                                                             \n"); 
			strQuery2.append("		  	  AND SUBSTR(D.CM_INFO, 26, 1) = '0'                                                                                        \n"); 
			strQuery2.append("		  	  AND B.CR_JOBCD is not null                                                                                                \n"); 
			strQuery2.append("		  	  AND A.CR_QRYCD = '04'                                                                                                     \n"); 
			strQuery2.append("		  	  AND B.CR_VERSION = 1                                                                                                      \n"); 
			strQuery2.append("			  AND A.CR_ACPTNO = C.CR_ACPTNO(+) AND B.CR_STATUS IN ('9','8')                                                             \n"); 
			strQuery2.append("			  AND A.CR_PRCDATE IS NOT NULL AND B.CR_PRCDATE IS NOT NULL                                                                 \n"); 
			strQuery2.append("		 GROUP BY C.CR_CHGCODE                                                                                                          \n"); 
			strQuery2.append("		  UNION ALL                                                                                                                     \n"); 
			strQuery2.append("		   SELECT NVL(C.CR_CHGCODE, '07') CR_CHGCODE,  0 CNT1, SUM(1) CNT2                                                              \n"); 
			strQuery2.append("			 FROM CMR1000 A, CMR1010 B, (SELECT CR_ACPTNO, MIN(CR_CHGCODE) CR_CHGCODE FROM CMR0900 GROUP BY CR_ACPTNO) C, CMM0036 D     \n"); 
			strQuery2.append("			WHERE A.CR_ACPTNO = B.CR_ACPTNO                                                                                             \n"); 
			strQuery2.append("			  AND TO_CHAR(B.CR_PRCDATE,'yyyymmdd') = ?                                                                              	\n"); 
			strQuery2.append("		  	  AND B.CR_SYSCD  = D.CM_SYSCD                                                                                              \n"); 
			strQuery2.append("		  	  AND B.CR_RSRCCD = D.CM_RSRCCD                                                                                             \n"); 
			strQuery2.append("		  	  AND SUBSTR(D.CM_INFO, 26, 1) = '0'                                                                                        \n"); 
			strQuery2.append("		  	  AND SUBSTR(D.CM_INFO, 02, 1) = '1'                                                                                        \n"); 
			strQuery2.append("		  	  AND B.CR_JOBCD is not null                                                                                                \n"); 
			strQuery2.append("		  	  AND A.CR_QRYCD = '04'                                                                                                     \n"); 
			strQuery2.append("		 	  AND (B.CR_QRYCD = '04' AND B.CR_VERSION > 1)                                                                              \n"); 
			strQuery2.append("			  AND A.CR_ACPTNO = C.CR_ACPTNO(+) AND B.CR_STATUS IN ('9','8')                                                             \n"); 
			strQuery2.append("		 	  AND '20111019' < (select to_char(cr_acptdate,'yyyymmdd') from cmr1000 where cr_acptno = B.cr_baseno)                      \n"); 
			strQuery2.append("			  AND A.CR_PRCDATE IS NOT NULL AND B.CR_PRCDATE IS NOT NULL                                                                 \n"); 
			strQuery2.append("			GROUP BY C.CR_CHGCODE                                                                                                       \n"); 
			strQuery2.append("		) B                                                                                                                             \n"); 
			strQuery2.append(" WHERE A.CM_MACODE = 'SAYUCODE'                                                                                                       \n"); 
			strQuery2.append("   AND A.CM_MICODE != '****'                                                                                                          \n"); 
			strQuery2.append("   AND A.CM_CLOSEDT IS NULL                                                                                                           \n"); 
			strQuery2.append("   AND A.CM_MICODE = B.CR_CHGCODE(+)                                                                                                  \n"); 
			strQuery2.append(" GROUP BY ROLLUP(CM_MICODE, CM_CODENAME)                                                                                              \n"); 
			strQuery2.append(" ORDER BY CM_MICODE                                                                                                                   \n"); 
			
			//pstmt2 = conn.prepareStatement(strQuery2.toString());
			pstmt2 = new LoggableStatement(conn,strQuery2.toString());
			
			pstmt2.setString(1, StDate);
			pstmt2.setString(2, StDate);		
			ecamsLogger.error(((LoggableStatement)pstmt2).getQueryString());
			rs2 = pstmt2.executeQuery();
			
			while(rs2.next()){
				if(rs2.getString("CM_codename") != null){
					int sum = 0;
					sum = rs2.getInt("cnta")+rs2.getInt("cntb");
					rst = new HashMap<String, String>();
					
					rst.put("cm_micode",rs2.getString("CM_MICODE"));
					rst.put("cm_codename",rs2.getString("CM_codename"));
					rst.put("cnta",rs2.getString("cnta"));
					rst.put("cntb",rs2.getString("cntb"));
					rst.put("sum",Integer.toString(sum));
					n_Code = Integer.parseInt(rs2.getString("CM_MICODE"));
					rsval.add(rst);
				}
				else if(rs2.getString("CM_MICODE") == null &&  rs2.getString("CM_codename") == null ){
					int sum = 0;
					sum = rs2.getInt("cnta")+rs2.getInt("cntb");
					rst = new HashMap<String, String>();
					String strMiCd = "";
					if(n_Code < 10)
						strMiCd = "0" + Integer.toString(++n_Code);
					else
						strMiCd = Integer.toString(++n_Code);
					rst.put("cm_micode",strMiCd);
					rst.put("cm_codename", "합 계");
					rst.put("cnta",rs2.getString("cnta"));
					rst.put("cntb",rs2.getString("cntb"));
					rst.put("sum",Integer.toString(sum));
					rsval.add(rst);
				}
				
				rst = null;
			}

			pstmt2.close();
			rs2.close();	
			conn.close();
			
			pstmt2 = null;
			rs2 = null;
			conn = null;
			
			returnObjectArray = rsval.toArray();
			rsval = null;
			
			return returnObjectArray;
			
		} catch (SQLException sqlexception) {
			sqlexception.printStackTrace();
			ecamsLogger.error("## Cmp0300.getCountList() SQLException START ##");
			ecamsLogger.error("## Error DESC : ", sqlexception);	
			ecamsLogger.error("## Cmp0300.getCountList() SQLException END ##");			
			throw sqlexception;
		} catch (Exception exception) {
			exception.printStackTrace();
			ecamsLogger.error("## Cmp0300.getCountList() Exception START ##");				
			ecamsLogger.error("## Error DESC : ", exception);	
			ecamsLogger.error("## Cmp0300.getCountList() Exception END ##");				
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
					ecamsLogger.error("## Cmp0300.getCountList() connection release exception ##");
					ex3.printStackTrace();
				}
			}
		}
	}//end of getProgList() method statement

	public Object[] getCountList_vertical(HashMap<String,String> etcData) throws SQLException, Exception {
    Connection        conn  = null;
    PreparedStatement pstmt = null;
    ResultSet         rs    = null;
    StringBuffer      strQuery = new StringBuffer();

    ArrayList<HashMap<String, String>> rsval = new ArrayList<HashMap<String, String>>();
    HashMap<String, String> rst = null;
    HashMap<String, String> totalRow = null; // 최종 합계 행

    ConnectionContext connectionContext = new ConnectionResource();

    try {
        conn = connectionContext.getConnection();

        // 1) 카테고리별 신규/변경 + ROLLUP(합계) 조회
        strQuery.setLength(0);
        strQuery.append("SELECT /*+ RULE */ A.CM_MICODE, A.CM_codename, NVL(SUM(B.CNT1), 0) as cnta, NVL(SUM(B.CNT2), 0) as cntb \n");
        strQuery.append("  FROM CMM0020 A, \n");
        strQuery.append("       ( SELECT NVL(C.CR_CHGCODE, '07') CR_CHGCODE, SUM(1) CNT1, 0 CNT2 \n");
        strQuery.append("           FROM CMR1000 A, CMR1010 B, (SELECT CR_ACPTNO, MIN(CR_CHGCODE) CR_CHGCODE FROM CMR0900 GROUP BY CR_ACPTNO) C, CMM0036 D \n");
        strQuery.append("          WHERE A.CR_ACPTNO = B.CR_ACPTNO \n");
        strQuery.append("            AND TO_CHAR(B.CR_PRCDATE,'yyyymmdd') = ? \n");
        strQuery.append("            AND B.CR_SYSCD  = D.CM_SYSCD \n");
        strQuery.append("            AND B.CR_RSRCCD = D.CM_RSRCCD \n");
        strQuery.append("            AND SUBSTR(D.CM_INFO, 26, 1) = '0' and cr_itemid is not null	 \n");
        strQuery.append("            AND B.CR_JOBCD is not null \n");
        strQuery.append("            AND A.CR_QRYCD = '04' \n");
        strQuery.append("            AND B.CR_VERSION = 1 \n");
        strQuery.append("            AND A.CR_ACPTNO = C.CR_ACPTNO(+) AND B.CR_STATUS IN ('9','8') \n");
        strQuery.append("            AND A.CR_PRCDATE IS NOT NULL AND B.CR_PRCDATE IS NOT NULL \n");
        strQuery.append("         GROUP BY C.CR_CHGCODE \n");
        strQuery.append("         UNION ALL \n");
        strQuery.append("         SELECT NVL(C.CR_CHGCODE, '07') CR_CHGCODE, 0 CNT1, SUM(1) CNT2 \n");
        strQuery.append("           FROM CMR1000 A, CMR1010 B, (SELECT CR_ACPTNO, MIN(CR_CHGCODE) CR_CHGCODE FROM CMR0900 GROUP BY CR_ACPTNO) C, CMM0036 D \n");
        strQuery.append("          WHERE A.CR_ACPTNO = B.CR_ACPTNO \n");
        strQuery.append("            AND TO_CHAR(B.CR_PRCDATE,'yyyymmdd') = ? \n");
        strQuery.append("            AND B.CR_SYSCD  = D.CM_SYSCD \n");
        strQuery.append("            AND B.CR_RSRCCD = D.CM_RSRCCD \n");
        strQuery.append("            AND SUBSTR(D.CM_INFO, 26, 1) = '0' and cr_itemid is not null	 \n");
        strQuery.append("            AND SUBSTR(D.CM_INFO, 02, 1) = '1' \n");
        strQuery.append("            AND B.CR_JOBCD is not null \n");
        strQuery.append("            AND A.CR_QRYCD = '04' \n");
        strQuery.append("            AND (B.CR_QRYCD = '04' AND B.CR_VERSION > 1) \n");
        strQuery.append("            AND A.CR_ACPTNO = C.CR_ACPTNO(+) AND B.CR_STATUS IN ('9','8') \n");
        strQuery.append("            AND '20111019' < (select to_char(cr_acptdate,'yyyymmdd') from cmr1000 where cr_acptno = B.cr_baseno) \n");
        strQuery.append("            AND A.CR_PRCDATE IS NOT NULL AND B.CR_PRCDATE IS NOT NULL \n");
        strQuery.append("         GROUP BY C.CR_CHGCODE \n");
        strQuery.append("       ) B \n");
        strQuery.append(" WHERE A.CM_MACODE = 'SAYUCODE' \n");
        strQuery.append("   AND A.CM_MICODE != '****' \n");
        strQuery.append("   AND A.CM_CLOSEDT IS NULL \n");
        strQuery.append("   AND A.CM_MICODE = B.CR_CHGCODE(+) \n");
        strQuery.append(" GROUP BY ROLLUP(CM_MICODE, CM_CODENAME) \n");
        strQuery.append(" ORDER BY CM_MICODE \n");

        pstmt = new LoggableStatement(conn,strQuery.toString());
        pstmt.setString(1, etcData.get("baseday"));
        pstmt.setString(2, etcData.get("baseday"));
        ecamsLogger.error(((LoggableStatement)pstmt).getQueryString());
        rs = pstmt.executeQuery();

        while (rs.next()) {
            String name = rs.getString("CM_codename");
            String micode = rs.getString("CM_MICODE");

            // 일반 행
            if (name != null) {
                rst = new HashMap<String, String>();
                rst.put("cm_micode", micode);
                rst.put("cm_codename", name);
                rst.put("opencnt", rs.getString("cnta"));
                rst.put("updtcnt", rs.getString("cntb"));
                // 일반 행의 네 컬럼은 공백으로
                rst.put("allreq", "");
                rst.put("delreq", "");
                rst.put("ckout", "");
                rst.put("ckoutcncl", "");
                rsval.add(rst);
            } 
            // 합계 행 (ROLLUP null)
            else if (micode == null && name == null) {
                totalRow = new HashMap<String, String>();
                totalRow.put("cm_micode", "");
                totalRow.put("cm_codename", "합계");
                totalRow.put("opencnt", rs.getString("cnta"));
                totalRow.put("updtcnt", rs.getString("cntb"));
                // 일괄/삭제/대여/대여취소는 두 번째 쿼리에서 채움(없으면 0)
            }
        }
        rs.close(); pstmt.close();

        if (totalRow == null) {
            totalRow = new HashMap<String, String>();
            totalRow.put("cm_micode", "");
            totalRow.put("cm_codename", "합계");
            totalRow.put("opencnt", "0");
            totalRow.put("updtcnt", "0");
        }

        // 2) 일괄이행/삭제/대여/대여취소 집계 후, 합계 행에 병합
        strQuery.setLength(0);
        strQuery.append("SELECT 'allreq' gbn, count(*) cnt         \n");
        strQuery.append("  FROM cmr1010 a,cmr1000 c                \n");
        strQuery.append(" where c.cr_qrycd='16'                    \n");
        strQuery.append("   and c.cr_status in ('8','9')           \n");
        strQuery.append("   and c.cr_prcdate is not null 		   \n");
        strQuery.append("   and c.cr_acptno=a.cr_acptno 		   \n");
        strQuery.append("   and a.cr_status in ('8','9') 		   \n");
        strQuery.append("   and to_char(a.cr_prcdate,'yyyymmdd')=? \n");
        strQuery.append("   and a.cr_version>0 					   \n");
        strQuery.append("   and a.cr_itemid is not null			   \n");
        strQuery.append("   and exists (select 1 from cmm0036 where cm_syscd=a.cr_syscd and cm_rsrccd=a.cr_rsrccd and substr(cm_info,26,1)='0') \n");
        strQuery.append(" UNION ALL \n");
        strQuery.append("SELECT 'delreq' gbn, count(*) cnt \n");
        strQuery.append("  FROM cmr0020 a \n");
        strQuery.append(" where to_char(a.cr_clsdate,'yyyymmdd')=? \n");
        strQuery.append("   and a.cr_opendate1 is not null \n");
        strQuery.append("   and a.cr_opendate1<'20221004' \n");
        strQuery.append("   and exists (select 1 from cmm0036 where cm_syscd=a.cr_syscd and cm_rsrccd=a.cr_rsrccd and substr(cm_info,26,1)='0') \n");
        strQuery.append(" UNION ALL \n");
        strQuery.append("SELECT 'ckout' gbn, count(*) cnt \n");
        strQuery.append("  FROM cmr1010 a,cmr1000 c \n");
        strQuery.append(" where c.cr_qrycd='01' \n");
        strQuery.append("   and c.cr_status in ('8','9') \n");
        strQuery.append("   and c.cr_prcdate is not null \n");
        strQuery.append("   and c.cr_acptno=a.cr_acptno \n");
        strQuery.append("   and a.cr_status in ('8','9') \n");
        strQuery.append("   and to_char(a.cr_prcdate,'yyyymmdd')=? \n");
        strQuery.append("   and a.cr_itemid is not null			   \n");        
        strQuery.append("   and exists (select 1 from cmm0036 where cm_syscd=a.cr_syscd and cm_rsrccd=a.cr_rsrccd and substr(cm_info,26,1)='0') \n");
        strQuery.append("   and not exists (select 1 from cmr1010 y,cmr1000 x \n");
        strQuery.append("                     where x.cr_qrycd='11' and x.cr_status in ('8','9') and x.cr_prcdate is not null \n");
        strQuery.append("                       and to_char(x.cr_prcdate,'yyyymmdd')=? \n");
        strQuery.append("                       and x.cr_acptno=y.cr_acptno and y.cr_status in ('8','9') and y.cr_prcdate is not null \n");
        strQuery.append("                       and y.cr_baseno=a.cr_acptno and y.cr_itemid=a.cr_itemid) \n");
        strQuery.append(" UNION ALL \n");
        strQuery.append("SELECT 'ckoutcncl' gbn, count(*) cnt \n");
        strQuery.append("  FROM cmr1010 a,cmr1000 c \n");
        strQuery.append(" where c.cr_qrycd='11' \n");
        strQuery.append("   and c.cr_status in ('8','9') \n");
        strQuery.append("   and c.cr_prcdate is not null \n");
        strQuery.append("   and c.cr_acptno=a.cr_acptno \n");
        strQuery.append("   and a.cr_status in ('8','9') \n");
        strQuery.append("   and to_char(a.cr_prcdate,'yyyymmdd')=? \n");
/*
        strQuery.append("	AND  a.cr_baseno || a.cr_itemid in (                                         													\n");
		strQuery.append("										select f.cr_acptno || f.cr_itemid                                              				\n");
		strQuery.append("										  from cmr1010 f, cmr1000 g                                                   				\n");
		strQuery.append("										 where f.cr_acptno = g.cr_acptno                                          					\n");
		strQuery.append("										   and   f.cr_status in ('9','8')                                        					\n");
		strQuery.append("										   and   g.cr_status in ('9','8')															\n");
		strQuery.append("										   and   to_char(f.cr_prcdate,'yyyymmdd') < ?												\n");
		strQuery.append("										   and   substr(g.cr_acptno,5,2) = '01'														\n");
		strQuery.append("									 	   and   '20111019' < (select to_char(cr_acptdate,'yyyymmdd')								\n");
		strQuery.append("															     from cmr1000 where f.cr_baseno = cr_acptno)						\n");
		strQuery.append("										)																							\n");
  */
		strQuery.append("   AND not exists (select 'X' 																	    \n");
		strQuery.append("                     FROM cmr1010 																	\n");
		strQuery.append("                    WHERE cr_baseno = a.cr_acptno													\n");						
		strQuery.append("                      AND cr_itemid = a.cr_itemid													\n");						
		strQuery.append("                      AND to_char(cr_prcdate,'yyyymmdd') = to_char(a.cr_prcdate,'yyyymmdd')		\n");
		strQuery.append("                      AND cr_status IN ('9','8') 													\n");
		strQuery.append("                      AND SUBSTR(cr_acptno,5,2) = '11' 											\n");
		strQuery.append("                  )																				\n");


        int qCount = org.apache.commons.lang.StringUtils.countMatches(strQuery.toString(), "?");
        pstmt = new LoggableStatement(conn, strQuery.toString());
        for (int i = 1; i <= qCount; i++) {
            pstmt.setString(i, etcData.get("baseday"));
        }
        ecamsLogger.error(((LoggableStatement)pstmt).getQueryString());
        rs = pstmt.executeQuery();

        // 기본 0으로
        totalRow.put("allreq",    totalRow.get("allreq")    == null ? "0" : totalRow.get("allreq"));
        totalRow.put("delreq",    totalRow.get("delreq")    == null ? "0" : totalRow.get("delreq"));
        totalRow.put("ckout",     totalRow.get("ckout")     == null ? "0" : totalRow.get("ckout"));
        totalRow.put("ckoutcncl", totalRow.get("ckoutcncl") == null ? "0" : totalRow.get("ckoutcncl"));

        while (rs.next()) {
            String gbn = rs.getString("gbn");
            String cnt = rs.getString("cnt");
            if (gbn != null) {
                totalRow.put(gbn, (cnt == null ? "0" : cnt));
            }
        }
        rs.close(); pstmt.close();

        // 3) 합계 행을 리스트 마지막에만 위치
        // (일반 행들은 이미 allreq/delreq/ckout/ckoutcncl가 "" 로 세팅됨)
        if (totalRow.get("cm_codename") == null) {
            totalRow.put("cm_codename", "합계");
        }
        if (totalRow.get("cm_micode") == null) {
            totalRow.put("cm_micode", "");
        }
        // 합계 행이 반드시 마지막
        rsval.add(totalRow);

        if (conn != null) conn.close();
        conn = null;
        return rsval.toArray();

    } catch (SQLException sqlexception) {
        sqlexception.printStackTrace();
        ecamsLogger.error("## Cmp0300.getCountList_vertical() SQLException START ##");
        ecamsLogger.error("## Error DESC : ", sqlexception);
        ecamsLogger.error("## Cmp0300.getCountList_vertical() SQLException END ##");
        throw sqlexception;
    } catch (Exception exception) {
        exception.printStackTrace();
        ecamsLogger.error("## Cmp0300.getCountList_vertical() Exception START ##");
        ecamsLogger.error("## Error DESC : ", exception);
        ecamsLogger.error("## Cmp0300.getCountList_vertical() Exception END ##");
        throw exception;
    } finally {
        if (strQuery != null) strQuery = null;
        if (rs != null) try{ rs.close(); } catch (Exception ex){ ex.printStackTrace(); }
        if (pstmt != null) try{ pstmt.close(); } catch (Exception ex2){ ex2.printStackTrace(); }
        if (conn != null) {
            try { ConnectionResource.release(conn); }
            catch (Exception ex3) {
                ecamsLogger.error("## Cmp0300.getCountList_vertical() connection release exception ##");
                ex3.printStackTrace();
            }
        }
    }
}


	public Object[] getCountList_vertical_old(HashMap<String,String> etcData) throws SQLException, Exception {
		Connection        conn        = null;
		PreparedStatement pstmt       = null;
		ResultSet         rs          = null;
		StringBuffer      strQuery    = new StringBuffer();
		
		ArrayList<HashMap<String, String>>  rsval = new ArrayList<HashMap<String, String>>();
		HashMap<String, String>			    rst		  = null;
	
		ConnectionContext connectionContext = new ConnectionResource();		
			
		try {
			conn = connectionContext.getConnection();
	        
			strQuery.setLength(0);
			strQuery.append("SELECT /*+ RULE */ A.CM_MICODE, A.CM_codename, NVL(SUM(B.CNT1), 0) as cnta, NVL(SUM(B.CNT2), 0) as cntb 							   	\n"); 
			strQuery.append("  FROM CMM0020 A,                                                                                                                    	\n"); 
			strQuery.append("		 ( SELECT NVL(C.CR_CHGCODE, '07') CR_CHGCODE, SUM(1) CNT1, 0 CNT2                                                              	\n"); 
			strQuery.append("			 FROM CMR1000 A, CMR1010 B, (SELECT CR_ACPTNO, MIN(CR_CHGCODE) CR_CHGCODE FROM CMR0900 GROUP BY CR_ACPTNO) C, CMM0036 D    	\n"); 
			strQuery.append("			WHERE A.CR_ACPTNO = B.CR_ACPTNO                                                                                            	\n"); 
			strQuery.append("			  AND TO_CHAR(B.CR_PRCDATE,'yyyymmdd') = ?                                                                             		\n"); 
			strQuery.append("		  	  AND B.CR_SYSCD  = D.CM_SYSCD                                                                                            	\n"); 
			strQuery.append("		  	  AND B.CR_RSRCCD = D.CM_RSRCCD                                                                                             \n"); 
			strQuery.append("		  	  AND SUBSTR(D.CM_INFO, 26, 1) = '0'                                                                                        \n"); 
			strQuery.append("		  	  AND B.CR_JOBCD is not null                                                                                                \n"); 
			strQuery.append("		  	  AND A.CR_QRYCD = '04'                                                                                                     \n"); 
			strQuery.append("		  	  AND B.CR_VERSION = 1                                                                                                      \n"); 
			strQuery.append("			  AND A.CR_ACPTNO = C.CR_ACPTNO(+) AND B.CR_STATUS IN ('9','8')                                                             \n"); 
			strQuery.append("			  AND A.CR_PRCDATE IS NOT NULL AND B.CR_PRCDATE IS NOT NULL                                                                 \n"); 
			strQuery.append("		 GROUP BY C.CR_CHGCODE                                                                                                          \n"); 
			strQuery.append("		  UNION ALL                                                                                                                     \n"); 
			strQuery.append("		   SELECT NVL(C.CR_CHGCODE, '07') CR_CHGCODE,  0 CNT1, SUM(1) CNT2                                                              \n"); 
			strQuery.append("			 FROM CMR1000 A, CMR1010 B, (SELECT CR_ACPTNO, MIN(CR_CHGCODE) CR_CHGCODE FROM CMR0900 GROUP BY CR_ACPTNO) C, CMM0036 D     \n"); 
			strQuery.append("			WHERE A.CR_ACPTNO = B.CR_ACPTNO                                                                                             \n"); 
			strQuery.append("			  AND TO_CHAR(B.CR_PRCDATE,'yyyymmdd') = ?                                                                              	\n"); 
			strQuery.append("		  	  AND B.CR_SYSCD  = D.CM_SYSCD                                                                                              \n"); 
			strQuery.append("		  	  AND B.CR_RSRCCD = D.CM_RSRCCD                                                                                             \n"); 
			strQuery.append("		  	  AND SUBSTR(D.CM_INFO, 26, 1) = '0'                                                                                        \n"); 
			strQuery.append("		  	  AND SUBSTR(D.CM_INFO, 02, 1) = '1'                                                                                        \n"); 
			strQuery.append("		  	  AND B.CR_JOBCD is not null                                                                                                \n"); 
			strQuery.append("		  	  AND A.CR_QRYCD = '04'                                                                                                     \n"); 
			strQuery.append("		 	  AND (B.CR_QRYCD = '04' AND B.CR_VERSION > 1)                                                                              \n"); 
			strQuery.append("			  AND A.CR_ACPTNO = C.CR_ACPTNO(+) AND B.CR_STATUS IN ('9','8')                                                             \n"); 
			strQuery.append("		 	  AND '20111019' < (select to_char(cr_acptdate,'yyyymmdd') from cmr1000 where cr_acptno = B.cr_baseno)                      \n"); 
			strQuery.append("			  AND A.CR_PRCDATE IS NOT NULL AND B.CR_PRCDATE IS NOT NULL                                                                 \n"); 
			strQuery.append("			GROUP BY C.CR_CHGCODE                                                                                                       \n"); 
			strQuery.append("		) B                                                                                                                             \n"); 
			strQuery.append(" WHERE A.CM_MACODE = 'SAYUCODE'                                                                                                       \n"); 
			strQuery.append("   AND A.CM_MICODE != '****'                                                                                                          \n"); 
			strQuery.append("   AND A.CM_CLOSEDT IS NULL                                                                                                           \n"); 
			strQuery.append("   AND A.CM_MICODE = B.CR_CHGCODE(+)                                                                                                  \n"); 
			strQuery.append(" GROUP BY ROLLUP(CM_MICODE, CM_CODENAME)                                                                                              \n"); 
			strQuery.append(" ORDER BY CM_MICODE                                                                                                                   \n"); 
			
			//pstmt = conn.prepareStatement(strQuery.toString());
			pstmt = new LoggableStatement(conn,strQuery.toString());
			
			pstmt.setString(1, etcData.get("baseday"));
			pstmt.setString(2, etcData.get("baseday"));		
			ecamsLogger.error(((LoggableStatement)pstmt).getQueryString());
			rs = pstmt.executeQuery();
			
			while(rs.next()){
				if(rs.getString("CM_codename") != null){
					rst = new HashMap<String, String>();					
					rst.put("cm_micode",rs.getString("CM_MICODE"));
					rst.put("cm_codename",rs.getString("CM_codename"));
					rst.put("opencnt",rs.getString("cnta"));
					rst.put("updtcnt",rs.getString("cntb"));
					rsval.add(rst);
				}
				else if(rs.getString("CM_MICODE") == null &&  rs.getString("CM_codename") == null ){
					rst = new HashMap<String, String>();	
					rst.put("cm_micode","");
					rst.put("cm_codename", "합 계");
					rst.put("opencnt",rs.getString("cnta"));
					rst.put("updtcnt",rs.getString("cntb"));
					rsval.add(rst);
				}
				
				rst = null;
			}
			pstmt.close();
			rs.close();	
			
			if (rsval.size()==0) {
				rst = new HashMap<String,String>();
			} else {
				rst = new HashMap<String,String>(rsval.get(rsval.size()-1));
			}
			int paramCnt = 0;
			strQuery.setLength(0);
			//일괄이행
			strQuery.append("SELECT 'allreq' gbn, count(*) cnt	              \n");
			strQuery.append("  FROM cmr1010 a,cmr1000 c  	                  \n");
			strQuery.append(" where c.cr_qrycd='16'                	          \n");
			strQuery.append("   and c.cr_status in ('8','9')      	          \n");
			strQuery.append("   and c.cr_prcdate is not null      	          \n");
			strQuery.append("   and c.cr_acptno=a.cr_acptno         	      \n");
			strQuery.append("   and a.cr_status in ('8','9')      	          \n");
			strQuery.append("   and to_char(a.cr_prcdate,'yyyymmdd')=? 	      \n");
			strQuery.append("   and a.cr_version>0   	                      \n");
			strQuery.append("   and exists (select 1 from cmm0036 	          \n");
			strQuery.append("                where cm_syscd=a.cr_syscd        \n");
			strQuery.append("                  and cm_rsrccd=a.cr_rsrccd      \n");
			strQuery.append("                  and substr(cm_info,26,1)='0')  \n");
			paramCnt++;
			//삭제
			strQuery.append(" UNION  ALL			     	       	          \n");
			strQuery.append("SELECT 'delreq' gbn, count(*) cnt	   	          \n");
			strQuery.append("  FROM cmr0020 a 	                              \n");
			strQuery.append(" where to_char(a.cr_clsdate,'yyyymmdd')=?        \n");
			strQuery.append("   and a.cr_opendate1 is not null    	          \n");
			strQuery.append("   and a.cr_opendate1<'20221004'      	          \n");
			strQuery.append("   and exists (select 1 from cmm0036 	          \n");
			strQuery.append("                where cm_syscd=a.cr_syscd        \n");
			strQuery.append("                  and cm_rsrccd=a.cr_rsrccd      \n");
			strQuery.append("                  and substr(cm_info,26,1)='0')  \n");
			paramCnt++;
			//대여
			strQuery.append(" UNION  ALL			     	       	          \n");
			strQuery.append("SELECT 'ckout' gbn, count(*) cnt	   	          \n");
			strQuery.append("  FROM cmr1010 a,cmr1000 c  	                  \n");
			strQuery.append(" where c.cr_qrycd='01'                	          \n");
			strQuery.append("   and c.cr_status in ('8','9')      	          \n");
			strQuery.append("   and c.cr_prcdate is not null      	          \n");
			strQuery.append("   and c.cr_acptno=a.cr_acptno         	      \n");
			strQuery.append("   and a.cr_status in ('8','9')      	          \n");
			strQuery.append("   and to_char(a.cr_prcdate,'yyyymmdd')=? 	      \n");
			strQuery.append("   and exists (select 1 from cmm0036 	          \n");
			strQuery.append("                where cm_syscd=a.cr_syscd        \n");
			strQuery.append("                  and cm_rsrccd=a.cr_rsrccd      \n");
			strQuery.append("                  and substr(cm_info,26,1)='0')  \n");
			strQuery.append("   and not exists (select 1 from cmr1010 y,cmr1000 x  \n");  //대여 후 당일 대여취소건 제외
			strQuery.append("                    where x.cr_qrycd='11'             \n");
			strQuery.append("                      and x.cr_status in ('8','9')    \n");
			strQuery.append("                      and x.cr_prcdate is not null    \n");
			strQuery.append("                      and to_char(x.cr_prcdate,'yyyymmdd')=?  \n");
			strQuery.append("                      and x.cr_acptno=y.cr_acptno     \n");
			strQuery.append("                      and y.cr_status in ('8','9')    \n");
			strQuery.append("                      and y.cr_prcdate is not null    \n");
			strQuery.append("                      and y.cr_baseno=a.cr_acptno     \n");
			strQuery.append("                      and y.cr_itemid=a.cr_itemid)    \n");
			paramCnt++;
			//대여취소
			strQuery.append(" UNION  ALL			     	       	          \n");
			strQuery.append("SELECT 'ckoutcncl' gbn, count(*) cnt   	      \n");
			strQuery.append("  FROM cmr1010 a,cmr1000 c  	                  \n");
			strQuery.append(" where c.cr_qrycd='11'                	          \n");
			strQuery.append("   and c.cr_status in ('8','9')      	          \n");
			strQuery.append("   and c.cr_prcdate is not null      	          \n");
			strQuery.append("   and c.cr_acptno=a.cr_acptno         	      \n");
			strQuery.append("   and a.cr_status in ('8','9')      	          \n");
			strQuery.append("   and to_char(a.cr_prcdate,'yyyymmdd')=? 	      \n");
			strQuery.append("   and exists (select 1 from cmm0036 	          \n");
			strQuery.append("                where cm_syscd=a.cr_syscd        \n");
			strQuery.append("                  and cm_rsrccd=a.cr_rsrccd      \n");
			strQuery.append("                  and substr(cm_info,26,1)='0')  \n");
			strQuery.append("   and not exists (select 1 from cmr1010 y,cmr1000 x  \n");  //당일대여에 대한 대여취소가 아닌 경우만
			strQuery.append("                    where x.cr_qrycd='01'             \n");
			strQuery.append("                      and x.cr_status in ('8','9')    \n");
			strQuery.append("                      and x.cr_prcdate is not null    \n");
			strQuery.append("                      and to_char(x.cr_acptdate,'yyyymmdd')>'20111019' \n");
			strQuery.append("                      and to_char(x.cr_prcdate,'yyyymmdd')<?  \n");
			strQuery.append("                      and x.cr_acptno=y.cr_acptno     \n");
			strQuery.append("                      and y.cr_status in ('8','9')    \n");
			strQuery.append("                      and y.cr_prcdate is not null    \n");
			strQuery.append("                      and y.cr_acptno=a.cr_baseno     \n");
			strQuery.append("                      and y.cr_itemid=a.cr_itemid)    \n");
			paramCnt++;												
																													
			//pstmt = conn.prepareStatement(strQuery.toString());
			pstmt = new LoggableStatement(conn,strQuery.toString());
			/* 
			for(int i=1;i<=paramCnt;i++){
				pstmt.setString(i, etcData.get("baseday"));
			}
			*/
			int qCount = StringUtils.countMatches(strQuery.toString(), "?");
			pstmt = new LoggableStatement(conn, strQuery.toString());
			for (int i = 1; i <= qCount; i++) {
				pstmt.setString(i, etcData.get("baseday"));
			}

			ecamsLogger.error(((LoggableStatement)pstmt).getQueryString());	
			rs = pstmt.executeQuery();
			
		    while(rs.next()){
		    	rst.put(rs.getString("gbn"), rs.getString("cnt"));	
		    	rsval.set(0, rst);
			}//end of while-loop statement	        
	        rs.close();
			pstmt.close();
			conn.close();
			
			pstmt = null;
			rs = null;
			conn = null;
						
			return rsval.toArray();
			
		} catch (SQLException sqlexception) {
			sqlexception.printStackTrace();
			ecamsLogger.error("## Cmp0300.getCountList_vertical() SQLException START ##");
			ecamsLogger.error("## Error DESC : ", sqlexception);	
			ecamsLogger.error("## Cmp0300.getCountList_vertical() SQLException END ##");			
			throw sqlexception;
		} catch (Exception exception) {
			exception.printStackTrace();
			ecamsLogger.error("## Cmp0300.getCountList_vertical() Exception START ##");				
			ecamsLogger.error("## Error DESC : ", exception);	
			ecamsLogger.error("## Cmp0300.getCountList_vertical() Exception END ##");				
			throw exception;
		}finally{
			if (strQuery != null)	strQuery = null;			
			if (rs != null)     try{rs.close();}catch (Exception ex){ex.printStackTrace();}
			if (pstmt != null)  try{pstmt.close();}catch (Exception ex2){ex2.printStackTrace();}
			if (conn != null){
				try{
					ConnectionResource.release(conn);
				}catch(Exception ex3){
					ecamsLogger.error("## Cmp0300.getCountList_vertical() connection release exception ##");
					ex3.printStackTrace();
				}
			}
		}
	}//end of getCountList_vertical() method statement
	
	public Object[] getBeforeToConvertList(String StDate, String EdDate, String HandlerId, String DeptCd, String ProgName, String QryCd) throws SQLException, Exception {
		Connection        conn        = null;
		PreparedStatement pstmt       = null;
		ResultSet         rs          = null;
		StringBuffer      strQuery    = new StringBuffer();
		ArrayList<HashMap<String, String>>  rsval = new ArrayList<HashMap<String, String>>();
		HashMap<String, String>			    rst		  = null;
	
		Object[] returnObjectArray = null;
		ConnectionContext connectionContext = new ConnectionResource();		
		
		int paramCnt = 0;
		
		try {
			conn = connectionContext.getConnection();
			strQuery.setLength(0);
	    
			if(QryCd.equals("01")){
				strQuery.append("select a.TEAM, a.PROG_TYPE, a.NAME, a.COPY_USER, a.CHNG_CODE,a.APRV_STA, 			\n");
				strQuery.append("		rtrim(a.CHNG_RMKS) CHNG_RMKS, b.IMPORTANT_LVL, a.NEWCOMM_FLAG, a.COPY_DATE, c.cm_username		\n");
				strQuery.append("		from  프로그램대여원장 a, 프로그램원장 b , cmm0040 c										\n");														
				
				strQuery.append("		where a.copy_date <> a.move_date				 								\n");
				
				if(!StDate.equals("") && StDate != null)
				strQuery.append("       and a.COPY_DATE >= ?															\n");
				if(!EdDate.equals("") && EdDate != null)
				strQuery.append("		and a.COPY_DATE <= ?		 													\n");
				strQuery.append("		and rtrim(a.COPY_USER) = c.cm_userid 										\n");
				strQuery.append("		and   a.STA not in ('09','99') 													\n");
				strQuery.append("		and   a.NAME = b.NAME 															\n");
				//strQuery.append("		and a.JOB_TYPE = b.JOB_TYPE 													\n");
				strQuery.append("		and a.PROG_TYPE = b.PROG_TYPE 													\n");
				//strQuery.append("		and a.TEAM =  b.TEAM   															\n");
				
				if(!HandlerId.equals("") && HandlerId != null)
					strQuery.append("		and rtrim(a.COPY_USER) = ?  												\n");
				if(!DeptCd.equals("") && DeptCd != null)
					strQuery.append("		and a.TEAM = ?   															\n");
				if(!ProgName.equals("") && ProgName != null)	
					strQuery.append("		and a.NAME = ?   															\n");
				
				strQuery.append("		order by a.job_type, b.prog_type, a.copy_date, a.copy_time  					\n");
			}
			else
			{
				strQuery.append("select a.TEAM, a.PROG_TYPE, a.NAME, a.MOVE_DATE, a.MOVE_USER, a.CHNG_CODE,a.APRV_STA , \n");
				strQuery.append("		rtrim(a.CHNG_RMKS) CHNG_RMKS, b.IMPORTANT_LVL, a.NEWCOMM_FLAG,  c.cm_username					\n");
				strQuery.append("		from  프로그램대여원장 a, 프로그램원장 b , cmm0040 c										\n");														
				strQuery.append("		where 	a.NAME = b.NAME		 													\n");
				
				if(!StDate.equals("") && StDate != null)
					strQuery.append("		and    a.MOVE_DATE >= ?															\n");
				if(!EdDate.equals("") && EdDate != null)
					strQuery.append("		and a.MOVE_DATE <= ?		 													\n");
			
				strQuery.append("		and rtrim(a.MOVE_USER) = c.cm_userid 											\n");					
				//strQuery.append("		and a.JOB_TYPE = b.JOB_TYPE 													\n");
				strQuery.append("		and a.PROG_TYPE = b.PROG_TYPE 													\n");
				//strQuery.append("		and a.TEAM =  b.TEAM   															\n");
				
				if(!HandlerId.equals("") && HandlerId != null)
					strQuery.append("		and rtrim(a.MOVE_USER) = ?  												\n");
				if(!DeptCd.equals("") && DeptCd != null)
					strQuery.append("		and a.TEAM = ?   															\n");
				if(!ProgName.equals("") && ProgName != null)	
					strQuery.append("		and a.NAME = ?   															\n");
				
				strQuery.append("		order by a.job_type, b.prog_type, a.move_date, a.move_time  					\n");
			}
			pstmt = conn.prepareStatement(strQuery.toString());
			pstmt = new LoggableStatement(conn,strQuery.toString());
			if(!StDate.equals("") && StDate != null)
				pstmt.setString(++paramCnt, StDate);
			if(!EdDate.equals("") && EdDate != null)
				pstmt.setString(++paramCnt, EdDate);
			if(!HandlerId.equals("") && HandlerId != null)
				pstmt.setString(++paramCnt, HandlerId);
			if(!DeptCd.equals("") && DeptCd != null)
				pstmt.setString(++paramCnt, DeptCd);
			if(!ProgName.equals("") && ProgName != null)
				pstmt.setString(++paramCnt, ProgName);
			ecamsLogger.error(((LoggableStatement)pstmt).getQueryString());	
			rs = pstmt.executeQuery();
			while(rs.next()){
				rst = new HashMap<String, String>();
				
				rst.put("cdTEAM",rs.getString("TEAM"));
				rst.put("cdPROG_TYPE",rs.getString("PROG_TYPE"));
				rst.put("NAME",rs.getString("NAME"));
			
				
				rst.put("cdCHNG_CODE",rs.getString("CHNG_CODE"));
				rst.put("CHNG_RMKS",rs.getString("CHNG_RMKS"));
				rst.put("cdIMPORTANT_LVL",rs.getString("IMPORTANT_LVL"));
				rst.put("cdNEWCOMM_FLAG",rs.getString("NEWCOMM_FLAG"));
				rst.put("APRV_STA",rs.getString("APRV_STA"));
				if(QryCd.equals("01")){
					rst.put("COPY_DATE", rs.getString("COPY_DATE"));
					rst.put("COPY_USER",rs.getString("COPY_USER"));
				}
				else{
					rst.put("MOVE_DATE",rs.getString("MOVE_DATE"));
					rst.put("MOVE_USER",rs.getString("MOVE_USER"));
				}
				rst.put("cm_username", rs.getString("cm_username"));
				
				rsval.add(rst);
				rst = null;
			}
			pstmt.close();
			rs.close();	
			conn.close();
			
			pstmt = null;
			rs = null;
			conn = null;
			
			returnObjectArray = rsval.toArray();
			
			rsval = null;
			
			return returnObjectArray;
			
		} catch (SQLException sqlexception) {
			sqlexception.printStackTrace();
			ecamsLogger.error("## Cmp0300.getBeforeToConvertList() SQLException START ##");
			ecamsLogger.error("## Error DESC : ", sqlexception);	
			ecamsLogger.error("## Cmp0300.getBeforeToConvertList() SQLException END ##");			
			throw sqlexception;
		} catch (Exception exception) {
			exception.printStackTrace();
			ecamsLogger.error("## Cmp0300.getBeforeToConvertList() Exception START ##");				
			ecamsLogger.error("## Error DESC : ", exception);	
			ecamsLogger.error("## Cmp0300.getBeforeToConvertList() Exception END ##");				
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
					ecamsLogger.error("## Cmp0300.getBeforeToConvertList() connection release exception ##");
					ex3.printStackTrace();
				}
			}
		}
	}//end of getBeforeToConvertList() method statement
	
	
	
}//end of Cmp3300 class statement
