package app.eCmr;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.logging.log4j.Logger;
import org.w3c.dom.Document;

import app.common.AutoSeq;
import app.common.CodeInfo;
import app.common.DeepCopy;
import app.common.LoggableStatement;
import app.common.UserInfo;
import app.common.TimeSch;

import com.ecams.common.dbconn.ConnectionContext;
import com.ecams.common.dbconn.ConnectionResource;
import com.ecams.common.logger.EcamsLogger;

public class Confirm_select {

    Logger ecamsLogger  = EcamsLogger.getLoggerInstance();
    
	/**
	 * 게시판을 조회합니다.(구분값을 주어)
	 * @param  gbn
	 * @return ArrayList
	 * @throws SQLException
	 * @throws Exception
	 */

	public String confSelect_doc(HashMap<String,String> dataObj) throws SQLException, Exception {
		Connection        conn        = null;
		PreparedStatement pstmt       = null;
		ResultSet         rs          = null;
		StringBuffer      strQuery    = new StringBuffer();
		int               rsCnt       = 0;
		ConnectionContext connectionContext = new ConnectionResource();

		try {
			conn = connectionContext.getConnection();
			strQuery.setLength(0);
			strQuery.append("select a.cm_gubun,a.cm_rsrccd,a.cm_position        \n");
			strQuery.append("  from cmm0060 a,cmm0040 b                         \n");
			strQuery.append(" where a.cm_syscd=? 							    \n");
			strQuery.append("   and a.cm_reqcd=?                    			\n");
			strQuery.append("   and decode(a.cm_manid,'1','Y','N')=b.cm_manid   \n");
			strQuery.append("   and b.cm_userid=?                               \n");
		/*	if (dataObj.get("CCB_YN").equals("Y")){
	        	strQuery.append(" and (a.cm_rsrccd is null or (a.cm_rsrccd is not null and a.cm_rsrccd='Y')) \n");
			}
	        else{*/
	        	strQuery.append(" and a.cm_rsrccd is null \n");
	       // }
			strQuery.append(" and a.cm_gubun not in ('1','2','P') \n");
			pstmt = conn.prepareStatement(strQuery.toString());
            pstmt.setString(1, dataObj.get("SysCd"));
            pstmt.setString(2, dataObj.get("SinCd"));
            pstmt.setString(3, dataObj.get("UserId"));
            rs = pstmt.executeQuery();

            while (rs.next()) { 
            	++rsCnt;
            }
            conn.close();
			
			rs = null;
			pstmt = null;
			conn = null;
            //ecamsLogger.error("++++++++confirm_doc end+++++++");
            return Integer.toString(rsCnt);
            
		} catch (SQLException sqlexception) {
			sqlexception.printStackTrace();
			ecamsLogger.error("## Confirm_select.confSelect_doc() SQLException START ##");
			ecamsLogger.error("## Error DESC : ", sqlexception);	
			ecamsLogger.error("## Confirm_select.confSelect_doc() SQLException END ##");
			throw sqlexception;
		} catch (Exception exception) {
			exception.printStackTrace();
			ecamsLogger.error("## Confirm_select.confSelect_doc() Exception START ##");				
			ecamsLogger.error("## Error DESC : ", exception);	
			ecamsLogger.error("## Confirm_select.confSelect_doc() Exception END ##");
			throw exception;
		}finally{
			if (rs != null)     try{rs.close();}catch (Exception ex){ex.printStackTrace();}
			if (pstmt != null)  try{pstmt.close();}catch (Exception ex2){ex2.printStackTrace();}
			if (conn != null){
				try{
					ConnectionResource.release(conn);
				}catch(Exception ex3){
					ecamsLogger.error("## Confirm_select.confSelect_doc() connection release exception ##");
					ex3.printStackTrace();
				}
			}
		}
	}//end of confSelect() method statement
	

	public String confirmYN(String ReqCd,String SysCd,String UserId) throws SQLException, Exception {
		Connection        conn        = null;
		PreparedStatement pstmt       = null;
		ResultSet         rs          = null;
		StringBuffer      strQuery    = new StringBuffer();
		int               rsCnt       = 0;
		ConnectionContext connectionContext = new ConnectionResource();

		try {
			conn = connectionContext.getConnection();
			strQuery.setLength(0);
			strQuery.append("select count(*) cnt        \n");
			strQuery.append("  from cmm0060 a,cmm0040 b \n");
			strQuery.append(" where a.cm_syscd=? 	    \n");
			strQuery.append("   and a.cm_reqcd=?      	\n");
			strQuery.append("   and decode(a.cm_manid,'1','Y','N')=b.cm_manid   \n");
			strQuery.append("   and b.cm_userid=?       \n");
			pstmt = conn.prepareStatement(strQuery.toString());
            pstmt.setString(1, SysCd);
            pstmt.setString(2, ReqCd);
            pstmt.setString(3, UserId);
            rs = pstmt.executeQuery();

            if (rs.next()) { 
            	rsCnt = rs.getInt("cnt");
            }
            rs.close();
            pstmt.close();
            conn.close();
			
			rs = null;
			pstmt = null;
			conn = null;
            //ecamsLogger.error("++++++++confirm_doc end+++++++");
            return Integer.toString(rsCnt);
            
		} catch (SQLException sqlexception) {
			sqlexception.printStackTrace();
			ecamsLogger.error("## Confirm_select.confirmYN() SQLException START ##");
			ecamsLogger.error("## Error DESC : ", sqlexception);	
			ecamsLogger.error("## Confirm_select.confirmYN() SQLException END ##");
			throw sqlexception;
		} catch (Exception exception) {
			exception.printStackTrace();
			ecamsLogger.error("## Confirm_select.confirmYN() Exception START ##");				
			ecamsLogger.error("## Error DESC : ", exception);	
			ecamsLogger.error("## Confirm_select.confirmYN() Exception END ##");
			throw exception;
		}finally{
			if (rs != null)     try{rs.close();}catch (Exception ex){ex.printStackTrace();}
			if (pstmt != null)  try{pstmt.close();}catch (Exception ex2){ex2.printStackTrace();}
			if (conn != null){
				try{
					ConnectionResource.release(conn);
				}catch(Exception ex3){
					ecamsLogger.error("## Confirm_select.confirmYN() connection release exception ##");
					ex3.printStackTrace();
				}
			}
		}
	}//end of confSelect() method statement
	
    //public Object[] Confirm_Info(String UserId,String SysCd,String ReqCd,ArrayList<String> RsrcCd,ArrayList<String> PgmType,String EmgSw,String PrjNo,String deployCd,ArrayList<String> QryCd,String passok,String gyulcheck) throws SQLException, Exception {
	public Object[] Confirm_Info(HashMap<String, String> paramMap) throws SQLException, Exception {
		Connection        conn        = null;
		PreparedStatement pstmt       = null;
		PreparedStatement pstmt2       = null;
		PreparedStatement pstmt3       = null;
		PreparedStatement pstmt4       = null;
		ResultSet         rs          = null;
		ResultSet         rs2         = null;
		ResultSet         rs3         = null;
		ResultSet         rs4         = null;
		StringBuffer      strQuery    = new StringBuffer();
		ArrayList<HashMap<Object, Object>> rsval = new ArrayList<HashMap<Object, Object>>();
		ArrayList<HashMap<String, String>>  rsval2 = new ArrayList<HashMap<String, String>>();
		ArrayList<HashMap<String, String>>  thirdlist = new ArrayList<HashMap<String, String>>();
		ArrayList<HashMap<String, String>>  requserlist = new ArrayList<HashMap<String, String>>();
		HashMap<Object, Object>			   rst   = null;
		ArrayList<HashMap<String, String>> ArySv = null;
		HashMap<String, String>				rst2 =null;
		HashMap<String, String>			   HashSv= null;
		Object[] returnObjectArray	 = null;
		boolean isSysChk = false;
		
		String UserId = paramMap.get("UserId");
		String SysCd = paramMap.get("SysCd");
		String ReqCd = paramMap.get("ReqCd");
		String RsrcCd = paramMap.get("RsrcCd");
		String PgmType = paramMap.get("PgmType");
		String EmgSw = paramMap.get("EmgSw");
		String PrjNo = paramMap.get("PrjNo");
		String deployCd = paramMap.get("deployCd");
		String QryCd = paramMap.get("QryCd");
		String passok = paramMap.get("passok");
		String gyulcheck = paramMap.get("gyulcheck");
		
		
		if(SysCd.equals("00510"))
			isSysChk = true;
		ConnectionContext connectionContext = new ConnectionResource();
		
		try {
			conn = connectionContext.getConnection();
			
			int				pstmtcount  = 1;
			String          SvLine      = "";
			String          SvTag       = "";
			String          SvSum       = "";
			String          SvUser      = "";
			String          SvSgnName   = "";
			String          SvBaseUser  = "";
			String          svDeptCd    = "";
			String 			Reqid	  	= "";
			int				reqcnt		= 0;
			String			YnCheck     = "No";
			String			Strgubun 	= "";
			String			Manid	 	= "";
			String[]		orderlist;
			String[]		reqlist;
			boolean         FindSw      = true;
			boolean         QrySw       = true;
			int i = 0;
			int z = 0;
			int c = 0;
			
			ecamsLogger.error("============ [Confirm_Info] RsrcCd : " + RsrcCd);
			
			if (RsrcCd != null && RsrcCd.length() > 0) {
				strQuery.setLength(0);
				strQuery.append("select distinct cm_samersrc from cmm0037     \n");
				strQuery.append(" where cm_syscd=?                            \n");
				strQuery.append("   and instr(?,cm_rsrccd)>0                  \n");
				//pstmt = conn.prepareStatement(strQuery.toString());
				pstmt = new LoggableStatement(conn,strQuery.toString());
				pstmt.setString(1, SysCd);
				pstmt.setString(2, RsrcCd);
				ecamsLogger.error(((LoggableStatement)pstmt).getQueryString());
				rs = pstmt.executeQuery();
				while (rs.next()) {
					ecamsLogger.error("============ [Confirm_Info] cm_samersrc : " + rs.getString("cm_samersrc"));
					if (RsrcCd.indexOf(rs.getString("cm_samersrc"))<0) {
						RsrcCd = RsrcCd + "," + rs.getString("cm_samersrc");
					}
				}
				rs.close();
				pstmt.close();
			} else {
				RsrcCd = "";
			}
			String[] aRsrcCd = RsrcCd.split(",");
			
			orderlist = PrjNo.split(",");
			if("01".equals(gyulcheck)){
				for(c=0;c<orderlist.length;c++){
					strQuery.setLength(0);
					strQuery.append("select cc_reqid						     \n");
					strQuery.append(" from cmc0400	                         	 \n");
					strQuery.append(" where cc_reqid = (select cc_reqid       \n");
					strQuery.append(" 						from cmc0420 	     \n");
					strQuery.append(" 						where cc_orderid = ?) \n");
					pstmt = conn.prepareStatement(strQuery.toString());
					pstmt.setString(1, orderlist[c]);
					rs = pstmt.executeQuery();
					while (rs.next()) {
						YnCheck  = "Yes";
						rst2 = new HashMap<String, String>();
						rst2.put("CC_REQID", rs.getString("cc_reqid"));
						
						rsval2.add(rst2);
						rst2 = null;
					}
					rs.close();
					pstmt.close();
				}
			}
			
			for(c=0;c<orderlist.length;c++){
				strQuery.setLength(0);
				strQuery.append("select cc_thirduser					     \n");
				strQuery.append(" from cmc0421	                         	 \n");
				strQuery.append(" where cc_orderid = ? 					\n");
				pstmt = conn.prepareStatement(strQuery.toString());
				pstmt.setString(1, orderlist[c]);
				rs = pstmt.executeQuery();
				while (rs.next()) {
					rst2 = new HashMap<String, String>();
					rst2.put("cc_thirduser", rs.getString("cc_thirduser"));
					
					thirdlist.add(rst2);
					rst2 = null;
				}
				rs.close();
				pstmt.close();
			}
			if("01".equals(gyulcheck)){
				for(c=0;c<rsval2.size();c++){
					strQuery.setLength(0);
					strQuery.append("select cc_requser3					     \n");
					strQuery.append(" from cmc0401	                         	 \n");
					strQuery.append(" where cc_reqid = ? 					\n");
					pstmt = conn.prepareStatement(strQuery.toString());
					pstmt.setString(1, rsval2.get(c).get("CC_REQID"));
					rs = pstmt.executeQuery();
					while (rs.next()) {
						rst2 = new HashMap<String, String>();
						rst2.put("cc_requser3", rs.getString("cc_requser3"));
						
						requserlist.add(rst2);
						rst2 = null;
					}
					rs.close();
					pstmt.close();
				}
			}
			
			
			FindSw = true;
			TimeSch           timesch    = new TimeSch();
			String strHoli = timesch.reqTimeGb(conn,EmgSw);
			if(passok.equals("pass")){
				strHoli = "1";
			}
			rsval.clear();			
			
			
			strQuery.setLength(0);
			strQuery.append("select cm_manid						     \n");
			strQuery.append(" from cmm0040	                         	 \n");
			strQuery.append(" where cm_userid = ?				       \n");
			pstmt = conn.prepareStatement(strQuery.toString());
			pstmt.setString(1, UserId);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				Manid=rs.getString("cm_manid");
			}
			rs.close();
			pstmt.close();
			
			
			
			pstmtcount = 1;
			strQuery.setLength(0);
			strQuery.append("select b.cm_username,b.cm_project,a.cm_seqno,a.cm_name,\n");
			strQuery.append("      a.cm_gubun,a.cm_common,a.cm_blank,a.cm_holiday,  \n");
			strQuery.append("      a.cm_emg,a.cm_emg2,a.cm_manid,d.cm_codename,     \n");
			strQuery.append("      a.cm_position,a.cm_jobcd,a.cm_rsrccd,a.cm_prcsw, \n");
			strQuery.append("      b.cm_project,nvl(a.cm_orgstep,'N') cm_orgstep    \n");
			strQuery.append(" from cmm0060 a,cmm0040 b, cmm0020 d                   \n");
			strQuery.append("where a.cm_reqcd=?                                     \n");                
			strQuery.append("  and a.cm_syscd=?                                     \n"); 
			strQuery.append("  and a.cm_manid=decode(b.cm_manid,'Y','1','2')        \n");                
			strQuery.append("  and b.cm_userid=?                                    \n");
			strQuery.append("  and d.cm_macode = 'POSITION'		                    \n");
			strQuery.append("  and d.cm_micode = b.cm_position	                    \n");
			strQuery.append("  and a.cm_gubun not in ('X','Z')                      \n");
			if(YnCheck.equals("Yes")){
				strQuery.append("union all							                    \n");
				strQuery.append("select b.cm_username,b.cm_project,a.cm_seqno,a.cm_name,\n");
				strQuery.append("      a.cm_gubun,a.cm_common,a.cm_blank,a.cm_holiday,  \n");
				strQuery.append("      a.cm_emg,a.cm_emg2,a.cm_manid,d.cm_codename,     \n");
				strQuery.append("      a.cm_position,a.cm_jobcd,a.cm_rsrccd,a.cm_prcsw, \n");
				strQuery.append("      b.cm_project,nvl(a.cm_orgstep,'N') cm_orgstep    \n");
				strQuery.append(" from cmm0060 a,cmm0040 b, cmc0401 c, cmm0020 d        \n");
				strQuery.append("where a.cm_reqcd=?                                     \n");                
				strQuery.append("  and a.cm_syscd=?                                     \n"); 
				strQuery.append("  and a.cm_manid=decode(b.cm_manid,'Y','1','2')        \n");                
				strQuery.append("  and b.cm_userid=?                                    \n");
				strQuery.append("  and a.cm_gubun = 'X'				                    \n");
				strQuery.append("  and d.cm_macode = 'POSITION'		                    \n");
				strQuery.append("  and d.cm_micode = b.cm_position	                    \n");
				strQuery.append("  and c.cc_reqid in (				                    \n");
				for(int v=0;v<rsval2.size();v++){
					if(v==0){
						strQuery.append("  ?				                    		\n");
					}else{
						strQuery.append("  ,?				                    		\n");
					}
				}
				strQuery.append("  )								                    \n");
			}
			strQuery.append("union all							                    \n");
			strQuery.append("select b.cm_username,b.cm_project,a.cm_seqno,a.cm_name,\n");
			strQuery.append("      a.cm_gubun,a.cm_common,a.cm_blank,a.cm_holiday,  \n");
			strQuery.append("      a.cm_emg,a.cm_emg2,a.cm_manid,d.cm_codename,     \n");
			strQuery.append("      a.cm_position,a.cm_jobcd,a.cm_rsrccd,a.cm_prcsw, \n");
			strQuery.append("      b.cm_project,nvl(a.cm_orgstep,'N') cm_orgstep    \n");
			strQuery.append(" from cmm0060 a,cmm0040 b, cmc0421 c, cmm0020 d        \n");
			strQuery.append("where a.cm_reqcd=?                                     \n");                
			strQuery.append("  and a.cm_syscd=?                                     \n"); 
			strQuery.append("  and a.cm_manid=decode(b.cm_manid,'Y','1','2')        \n");                
			strQuery.append("  and b.cm_userid=?                                    \n");
			strQuery.append("  and a.cm_gubun = 'Z'				                    \n");
			strQuery.append("  and d.cm_macode = 'POSITION'		                    \n");
			strQuery.append("  and d.cm_micode = b.cm_position	                    \n");
			strQuery.append("  and c.cc_orderid in (				                \n");
			for(int v=0;v<orderlist.length;v++){
				if(v==0){
					strQuery.append("  ?				                    		\n");
				}else{
					strQuery.append("  ,?				                    		\n");
				}
			}
			strQuery.append("  )								                    \n");
            strQuery.append("order by cm_seqno, cm_position                     \n");
			
            pstmt = conn.prepareStatement(strQuery.toString());
            pstmt = new LoggableStatement(conn,strQuery.toString());
            pstmt.setString(pstmtcount++, ReqCd);
            pstmt.setString(pstmtcount++, SysCd);            
            pstmt.setString(pstmtcount++, UserId);
            if(YnCheck.equals("Yes")){
            	 pstmt.setString(pstmtcount++, ReqCd);
                 pstmt.setString(pstmtcount++, SysCd);            
                 pstmt.setString(pstmtcount++, UserId);
                 for(int v=0;v<rsval2.size();v++){
                	 pstmt.setString(pstmtcount++, rsval2.get(v).get("CC_REQID"));
                 }
            }
            pstmt.setString(pstmtcount++, ReqCd);
            pstmt.setString(pstmtcount++, SysCd);            
            pstmt.setString(pstmtcount++, UserId);
            for(int v=0;v<orderlist.length;v++){
           	 pstmt.setString(pstmtcount++, orderlist[v]);
            }
            	
            ecamsLogger.error(((LoggableStatement)pstmt).getQueryString());
            rs = pstmt.executeQuery(); 
			while (rs.next()){
				
				svDeptCd = rs.getString("cm_project");
				FindSw = true;
				QrySw = true;
				//svUserName = rs.getString("cm_username");
								
				if (QryCd != null && QryCd.length()>0) {
		            if ((ReqCd.equals("03") || ReqCd.equals("04") || ReqCd.equals("60")) ) {
//		            	if (QryCd.get(0).equals("09")) {
//		            		if ((rs.getString("cm_gubun").equals("1") && !rs.getString("cm_jobcd").equals("SYSFT")) || 
//		            			rs.getString("cm_gubun").equals("2")) {
//		            			QrySw = true;
//		            		} else QrySw = false;
//		            	} else
		            		if ("05".equals(QryCd)) {
		            		if (rs.getString("cm_gubun").equals("1") && rs.getString("cm_jobcd").equals("SYSFT")) {
			            		QrySw = false;
			            	}
			            }
		            } 
				}
	            /*ecamsLogger.debug("+++++++++QryCd.size++++++"+Integer.toString(QryCd.size()));
	            if (QrySw == true) {
	               ecamsLogger.debug("+++++++++cm_gubun++++++"+rs.getString("cm_gubun")+",true");
	            } else {
		           ecamsLogger.debug("+++++++++cm_gubun++++++"+rs.getString("cm_gubun")+",false");
	            }*/
	            if (aRsrcCd.length > 0 && rs.getString("cm_rsrccd") != null && QrySw == true) {
	            	FindSw = false;
	            	for (i=0;i<aRsrcCd.length;i++){
	            		if (rs.getString("cm_rsrccd").indexOf(aRsrcCd[i]) > -1) {
	            			FindSw = true;
	            			break;
	            		}
	    			}
	            }
	            /*
	            if (FindSw  == false && QrySw == true && PgmType.size() > 0 && rs.getString("cm_pgmtype") != null) {
	            	FindSw = false;
	            	for (int i=0;i<PgmType.size();i++){
	            		if (rs.getString("cm_pgmtype").indexOf(PgmType.get(i)) > -1) {
	            			FindSw = true;
	            			break;
	            		}
	    			}
	            }
	            */
	            if (FindSw == true && QrySw == true) {
	            	
					SvLine = "";
					SvTag = "";
					SvSum = "";
					SvUser = "";
					SvSgnName = "";
					SvBaseUser = "";
					
					ArySv = new ArrayList<HashMap<String, String>>();
					FindSw = false;
					if (strHoli.equals("0")) SvLine = rs.getString("cm_common");
					else if (strHoli.equals("1")) SvLine = rs.getString("cm_emg");
					else {					
						SvLine = rs.getString("cm_holiday");
					}
					
					/*
					if (EmgSw.equals("Y")){
						SvLine = rs.getString("cm_emg2");
					} else if (deployCd != null && deployCd != "") {
						if (deployCd.equals("2")) SvLine = rs.getString("cm_emg");
						else SvLine = rs.getString("cm_common");
					} else {
						SvLine = rs.getString("cm_common");
					}
					*/
					
					if (EmgSw.equals("Y")){
						SvLine = rs.getString("cm_emg");						
					}else{
						SvLine = rs.getString("cm_common");
					}

					FindSw = true;
					if (!"".equals(SvLine)) {
						if (rs.getString("cm_gubun").equals("3") && !ReqCd.equals("01") && !Manid.equals("N")) {
							//ecamsLogger.error("Delete!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
							System.out.println("Delete!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
							pstmtcount = 1;
							if (rs.getString("cm_position") != null) {
								strQuery.setLength(0);
								strQuery.append("select count(*) as cnt from cmm0043                          \n");
								strQuery.append(" where cm_userid=? and instr(?,cm_rgtcd)>0                   \n");
								pstmt2 = conn.prepareStatement(strQuery.toString());
								pstmt2 = new LoggableStatement(conn,strQuery.toString());
								pstmt2.setString(pstmtcount++, UserId);
								pstmt2.setString(pstmtcount++, rs.getString("cm_position"));
					            ecamsLogger.error(((LoggableStatement)pstmt2).getQueryString());
					            rs2 = pstmt2.executeQuery();
					            if (rs2.next()) {
					            	if (rs2.getInt("cnt") > 0){
					            		SvLine = ""; 
					            	}
					            }
					            pstmt2.close();
					            rs2.close();
								
							}
						} else if (rs.getString("cm_gubun").equals("P")||rs.getString("cm_gubun").equals("Z")||rs.getString("cm_gubun").equals("X")) {									
							if (PrjNo.equals("") || PrjNo == null){
								SvLine = ""; 								
							} 
						} else if (rs.getString("cm_gubun").equals("5") ||
						    rs.getString("cm_gubun").equals("6") || rs.getString("cm_gubun").equals("7")) {
							strQuery.setLength(0);
							pstmtcount = 1;
							strQuery.append("select count(*) as cnt from cmm0043          \n");
							strQuery.append(" where instr(?,cm_rgtcd)>0                   \n");
							pstmt2 = conn.prepareStatement(strQuery.toString());
							pstmt2 = new LoggableStatement(conn,strQuery.toString());
							pstmt2.setString(pstmtcount++, rs.getString("cm_position"));
				            ecamsLogger.error(((LoggableStatement)pstmt2).getQueryString());
				            rs2 = pstmt2.executeQuery();
				            if (rs2.next()) {
				            	if (rs2.getInt("cnt") == 0){
				            		SvLine = ""; 
				            	}
				            }
				            pstmt2.close();
				            rs2.close();
						}
						else if (rs.getString("cm_gubun").equals("2") && (ReqCd.equals("03") || ReqCd.equals("04") || ReqCd.equals("60"))) {
							strQuery.setLength(0);
							strQuery.append("select count(*) as cnt from cmm0036                          \n");
							strQuery.append(" where cm_syscd=? and cm_closedt is null                     \n");
							strQuery.append("   and instr(?,cm_rsrccd)>0                                  \n");
							strQuery.append("   and (substr(cm_info,1,1)='1' or substr(cm_info,13,1)='1') \n");
							pstmt2 = conn.prepareStatement(strQuery.toString());
							pstmt2 = new LoggableStatement(conn,strQuery.toString());
							pstmt2.setString(1, SysCd);
							pstmt2.setString(2, RsrcCd);
				            ecamsLogger.error(((LoggableStatement)pstmt2).getQueryString());
				            rs2 = pstmt2.executeQuery();
				            if (rs2.next()) {
				            	if (rs2.getInt("cnt") == 0){
				            		SvLine = ""; 
				            	}
				            }
				            pstmt2.close();
				            rs2.close();
						}
						else if (rs.getString("cm_gubun").equals("1")) {
							if (rs.getString("cm_jobcd").equals("SYSPF")) {
								strQuery.setLength(0);
//   SYSPF 가 살아진다.								
//								strQuery.append("select count(*) as cnt from cmm0036                          \n");
//								strQuery.append(" where cm_syscd=? and cm_closedt is null                     \n");
//								strQuery.append("   and instr(?,cm_rsrccd)>0                                  \n");
//								strQuery.append("   and (substr(cm_info,27,1)='1' or                          \n");
//								strQuery.append("        substr(cm_info,4,1)='1' or                           \n");
//								strQuery.append("        substr(cm_info,47,1)='1' or                          \n");
//								strQuery.append("        substr(cm_info,9,1)='1')                             \n");
//								pstmt2 = conn.prepareStatement(strQuery.toString());
//								pstmt2 = new LoggableStatement(conn,strQuery.toString());
//								pstmt2.setString(1, SysCd);
//								pstmt2.setString(2, aRsrcCd);
//					            ecamsLogger.error(((LoggableStatement)pstmt2).getQueryString());
//					            rs2 = pstmt2.executeQuery();
//					            if (rs2.next()) {
//					            	if (rs2.getInt("cnt") == 0){
//					            		SvLine = ""; 
//					            	}
//					            }
//					            pstmt2.close();
//					            rs2.close();
							} else if (rs.getString("cm_jobcd").equals("SYSCB")) {
								strQuery.setLength(0);
								strQuery.append("select count(*) as cnt from cmm0036                          \n");
								strQuery.append(" where cm_syscd=? and cm_closedt is null                     \n");
								strQuery.append("   and instr(?,cm_rsrccd)>0                                  \n");
								//컴파일,빌드서버파일Copy,빌드서버에서 체크인
								strQuery.append("   and (substr(cm_info,1,1)='1' or substr(cm_info,13,1)='1'  \n");
								strQuery.append("      or substr(cm_info,25,1)='1')                           \n");
								//pstmt2 = new LoggableStatement(conn,strQuery.toString());
								pstmt2 = conn.prepareStatement(strQuery.toString());
								pstmt2.setString(1, SysCd);
								pstmt2.setString(2, RsrcCd);
					            ////ecamsLogger.error(((LoggableStatement)pstmt2).getQueryString());
					            rs2 = pstmt2.executeQuery();
					            if (rs2.next()) {
					            	if (rs2.getInt("cnt") == 0){
					            		SvLine = ""; 
					            	}
					            }
					            pstmt2.close();
					            rs2.close();
							} else if (rs.getString("cm_jobcd").equals("SYSFMK") || rs.getString("cm_jobcd").equals("SYSPDN") || rs.getString("cm_jobcd").equals("SYSPUP")) {
								strQuery.setLength(0);
								strQuery.append("select count(*) as cnt from cmm0036      \n");
								strQuery.append(" where cm_syscd=? and cm_closedt is null \n");
								strQuery.append("   and instr(?,cm_rsrccd)>0              \n");
								//로컬에서 개발
								strQuery.append("   and (substr(cm_info,45,1)='1'           \n");
								if (rs.getString("cm_jobcd").equals("SYSFMK") || rs.getString("cm_jobcd").equals("SYSPDN")) {
									strQuery.append(" or substr(cm_info,38,1)='1'           \n");
								} 
								strQuery.append(") \n");
								
								pstmt2 = conn.prepareStatement(strQuery.toString());
								//pstmt2 = new LoggableStatement(conn,strQuery.toString());
								pstmt2.setString(1, SysCd);
								pstmt2.setString(2, RsrcCd);
					            ////ecamsLogger.error(((LoggableStatement)pstmt2).getQueryString());
					            rs2 = pstmt2.executeQuery();
					            if (rs2.next()) {
					            	if (rs2.getInt("cnt") == 0){
					            		SvLine = ""; 
					            	}
					            }
					            pstmt2.close();
					            rs2.close();
							} else if (rs.getString("cm_jobcd").equals("SYSDN")) {
								strQuery.setLength(0);
								strQuery.append("select count(*) as cnt from cmm0036      \n");
								strQuery.append(" where cm_syscd=? and cm_closedt is null \n");
								strQuery.append("   and instr(?,cm_rsrccd)>0              \n");
								//로컬에서 개발
								strQuery.append("   and (substr(cm_info,45,1)='0' or       \n");
								strQuery.append("        substr(cm_info,438,1)='1')        \n");
								//pstmt2 = new LoggableStatement(conn,strQuery.toString());
								pstmt2 = conn.prepareStatement(strQuery.toString());
								pstmt2.setString(1, SysCd);
								pstmt2.setString(2, RsrcCd);
					            ////ecamsLogger.error(((LoggableStatement)pstmt2).getQueryString());
					            rs2 = pstmt2.executeQuery();
					            if (rs2.next()) {
					            	if (rs2.getInt("cnt") == 0){
					            		SvLine = ""; 
					            	}
					            }
					            pstmt2.close();
					            rs2.close();
							} else if (rs.getString("cm_jobcd").equals("SYSED")) {
								int k = 0;
						//		if (deployCd.equals("Y")) {
									strQuery.setLength(0);
									pstmtcount = 1;
									strQuery.append("select count(*) as cnt from cmm0036                          \n");
									strQuery.append(" where cm_syscd=? and cm_closedt is null                     \n");
									strQuery.append("   and instr(?,cm_rsrccd)>0                                  \n");
									strQuery.append("   and (substr(cm_info,11,1)='1' or substr(cm_info,21,1)='1') \n");
									pstmt2 = conn.prepareStatement(strQuery.toString());
									//pstmt2 = new LoggableStatement(conn,strQuery.toString());								
									pstmt2.setString(pstmtcount++, SysCd);
									pstmt2.setString(pstmtcount++, RsrcCd);
						            //ecamsLogger.error(((LoggableStatement)pstmt2).getQueryString());
						            rs2 = pstmt2.executeQuery();						            
						            while (rs2.next()) {
						            	if (rs2.getInt("cnt") > 0) ++k; 
						            }
						            pstmt2.close();
						            rs2.close();
						//		} 
					            if (k == 0) SvLine = "";
							} else if (rs.getString("cm_jobcd").equals("SYSRC")) {
								strQuery.setLength(0);
								pstmtcount = 1;
								strQuery.append("select count(*) as cnt from cmm0036                          \n");
								strQuery.append(" where cm_syscd=? and cm_closedt is null                     \n");
								strQuery.append("   and instr(?,cm_rsrccd)>0                                  \n");
								strQuery.append("   and substr(cm_info,35,1)='1'                              \n");
								//pstmt2 = new LoggableStatement(conn,strQuery.toString());
								pstmt2 = conn.prepareStatement(strQuery.toString());
								pstmt2.setString(pstmtcount++, SysCd);
								pstmt2.setString(pstmtcount++, RsrcCd);
								pstmt2.setString(pstmtcount++, SysCd);
								pstmt2.setString(pstmtcount++, RsrcCd);
					            ////ecamsLogger.error(((LoggableStatement)pstmt2).getQueryString());
					            rs2 = pstmt2.executeQuery();
					            int k = 0;
					            while (rs2.next()) {
					            	if (rs2.getInt("cnt") > 0) ++k; 
					            }
					            pstmt2.close();
					            rs2.close();
					            if (k == 0) SvLine = "";
							}  else if (rs.getString("cm_jobcd").equals("SYSFT")) {
								strQuery.setLength(0);
								pstmtcount = 1;
								strQuery.append("select count(*) as cnt from cmm0036                          \n");
								strQuery.append(" where cm_syscd=? and cm_closedt is null                     \n");
								strQuery.append("   and instr(?,cm_rsrccd)>0                                  \n");
								strQuery.append("   and substr(cm_info,28,1)='1'                              \n");
								//pstmt = new LoggableStatement(conn,strQuery.toString());
								pstmt2 = conn.prepareStatement(strQuery.toString());
								pstmt2.setString(pstmtcount++, SysCd);
								pstmt2.setString(pstmtcount++, RsrcCd);
					            ////ecamsLogger.error(((LoggableStatement)pstmt2).getQueryString());
					            rs2 = pstmt2.executeQuery();
					            if (rs2.next()) {
					            	if (rs2.getInt("cnt") == 0) SvLine = ""; 
					            }
					            pstmt2.close();
					            rs2.close();
							} else if (rs.getString("cm_jobcd").equals("SYSRK")) {
								strQuery.setLength(0);
								strQuery.append("select count(*) as cnt from cmm0036                          \n");
								strQuery.append(" where cm_syscd=? and cm_closedt is null                     \n");
								strQuery.append("   and instr(?,cm_rsrccd)>0                                  \n");
								//컴파일,빌드서버파일Copy,빌드서버에서 체크인
								strQuery.append("   and substr(cm_info,6,1)='1'                               \n");
								//pstmt2 = new LoggableStatement(conn,strQuery.toString());
								pstmt2 = conn.prepareStatement(strQuery.toString());
								pstmt2.setString(1, SysCd);
								pstmt2.setString(2, RsrcCd);
					            ////ecamsLogger.error(((LoggableStatement)pstmt2).getQueryString());
					            rs2 = pstmt2.executeQuery();
					            if (rs2.next()) {
					            	if (rs2.getInt("cnt") == 0){
					            		SvLine = ""; 
					            	}
					            }
					            pstmt2.close();
					            rs2.close();
							} 
						} 
				    }
					//ecamsLogger.debug("++++++++++++gubun,SvLine+++++++++"+rs.getString("cm_gubun")+","+SvLine);
					if (!SvLine.equals("")) {
						if (rs.getString("cm_gubun").equals("1")) {
							SvUser = rs.getString("cm_jobcd");
							SvBaseUser =  rs.getString("cm_jobcd");
							SvSgnName = "자동처리";
						//	if (rs.getString("cm_jobcd").equals("SYSDDN") || 
							//	rs.getString("cm_jobcd").equals("SYSDUP") ||
							//    rs.getString("cm_jobcd").equals("SYSDNC")) {
//								if (rs.getString("cm_manid").equals("N")){
//									SvTag = "파트너사 " + rs.getString("cm_username");
//								}
//							    else{
//							    	SvTag = "한국정보" + rs.getString("cm_username");
//							    }
							//}
							//else {
								strQuery.setLength(0);
								pstmtcount = 1; 
								strQuery.append("select cm_codename from cmm0020                         \n");
								strQuery.append(" where cm_macode='SYSGBN' and cm_micode=?               \n");
								//pstmt = new LoggableStatement(conn,strQuery.toString());
								pstmt2 = conn.prepareStatement(strQuery.toString());
								pstmt2.setString(pstmtcount++, rs.getString("cm_jobcd"));
								////ecamsLogger.error(((LoggableStatement)pstmt2).getQueryString());
					            rs2 = pstmt2.executeQuery();
					            if (rs2.next()) {
					            	SvTag = rs2.getString("cm_codename");
					            }
					            pstmt2.close();
					            rs2.close();
							//}
							HashSv = new HashMap<String, String>();
							HashSv.put("SvUser", SvUser);
							HashSv.put("SvTag", SvTag);
							HashSv.put("SvSum", SvTag);
							ArySv.add(HashSv);
							HashSv = null;
							FindSw = true;
						}
						else if (rs.getString("cm_gubun").equals("2")) {
							SvTag = rs.getString("cm_username");
							SvUser = UserId;
							SvSum = rs.getString("cm_codename") + " " + rs.getString("cm_username");
							SvBaseUser = UserId;
							
							
							SvSgnName = "확인";
							HashSv = new HashMap<String, String>();
							HashSv.put("SvUser", SvUser);
							HashSv.put("SvTag", SvTag);
							HashSv.put("SvSum", SvSum);
							ArySv.add(HashSv);
							HashSv = null;
							FindSw = true;
						}
						else if (rs.getString("cm_gubun").equals("C")) {							
							HashSv = new HashMap<String, String>();
							HashSv.put("SvUser", SvUser);
							HashSv.put("SvTag", SvTag);
							HashSv.put("SvSum", SvTag);
							ArySv.add(HashSv);			
							HashSv = null;
							SvSgnName = "결재(순차)";
							FindSw = true;
						}
						else if (rs.getString("cm_gubun").equals("8")) {
							HashSv = new HashMap<String, String>();
							HashSv.put("SvUser", SvUser);
							HashSv.put("SvTag", SvTag);
							HashSv.put("SvSum", SvTag);
							ArySv.add(HashSv);	
							HashSv = null;
							SvSgnName = "참조";
							FindSw = true;
						}
						/*
						else if (rs.getString("cm_gubun").equals("9")){
							SvUser = rs.getString("cm_jobcd");
							SvBaseUser = rs.getString("cm_jobcd");
							strQuery.setLength(0);
							pstmtcount = 1;
							strQuery.append("select cm_deptname from cmm0100              \n");
							strQuery.append(" where cm_deptcd=?                           \n");
							//pstmt = new LoggableStatement(conn,strQuery.toString());
							pstmt2 = conn.prepareStatement(strQuery.toString());
							pstmt2.setString(pstmtcount++, rs.getString("cm_jobcd"));
							////ecamsLogger.error(((LoggableStatement)pstmt2).getQueryString());
				            rs2 = pstmt2.executeQuery();	
				            if (rs2.next()) {
				            	SvTag = rs2.getString("cm_deptname");
				            }
				            rs2.close();
				            pstmt2.close();
				            
							HashSv = new HashMap<String, String>();
							HashSv.put("SvUser", SvUser);
							HashSv.put("SvTag", SvTag);
							ArySv.add(HashSv);	
							HashSv = null;
				            FindSw = true;

						}*/
						else if (rs.getString("cm_gubun").equals("3")){
							SvUser = "";
							SvBaseUser = "";
							SvTag = "";
							SvSgnName = "결재(순차)";
							
							FindSw = false;
							
							//20240307 추가 시작 직전신청건 해당단계 결재자 추출
							//20250623 syscd가 걸려 있지 않아 수정(운영-개발에만 적용)
							String befConfUser = "";
							pstmtcount = 0;
							strQuery.setLength(0);
							strQuery.append("select a.cr_team from cmr9900 a,        \n");
							strQuery.append("       (select max(cr_acptno) cr_acptno \n");
							strQuery.append("          from cmr1000                  \n");
							strQuery.append("         where cr_qrycd=?               \n");
							strQuery.append("           and cr_editor=?              \n");
							strQuery.append("           and cr_syscd=?               \n");
							strQuery.append("           and cr_status<>'3') b        \n");
							strQuery.append("  where b.cr_acptno=a.cr_acptno         \n");
							strQuery.append("    and a.cr_teamcd='3'                 \n");
							strQuery.append("    and a.cr_sgngbn=?                   \n");
							strQuery.append("    and a.cr_locat<>'00'                \n");
							pstmt2 = conn.prepareStatement(strQuery.toString());
							pstmt2 = new LoggableStatement(conn,strQuery.toString());
							pstmt2.setString(++pstmtcount, ReqCd);
							pstmt2.setString(++pstmtcount, UserId);
							pstmt2.setString(++pstmtcount, SysCd);
				            pstmt2.setString(++pstmtcount, rs.getString("cm_position"));
				            ecamsLogger.error(((LoggableStatement)pstmt2).getQueryString());
				            rs2 = pstmt2.executeQuery();
				            if (rs2.next()) {
				            	if (rs2.getString("cr_team")!=null) befConfUser = rs2.getString("cr_team");
				            }
				            rs2.close();
				            pstmt2.close();
							//20240307 추가 종료
				            
							strQuery.setLength(0);
							pstmtcount = 1;							
							strQuery.append("select distinct a.cm_userid,a.cm_username,a.cm_position,          \n");
							strQuery.append("       a.cm_status,a.cm_duty,a.cm_deptseq, d.cm_codename           \n");
							strQuery.append("  from cmm0040 a,cmm0043 b, cmm0020 d     \n");
							strQuery.append(" where a.cm_userid=? 								 \n");
							strQuery.append("   and a.cm_active='1'                              \n");
							strQuery.append("   and a.cm_manid='Y'                              \n");
							strQuery.append("   and (a.cm_project in (select cm_deptcd           \n");
							strQuery.append("                          from (select * from cmm0100 where cm_useyn='Y') \n");
							strQuery.append("                         start with cm_deptcd=?     \n");
							strQuery.append("                         connect by prior cm_updeptcd=cm_deptcd) \n");
							strQuery.append("   or a.cm_project2 in (select cm_deptcd            \n");
							strQuery.append("                          from (select * from cmm0100 where cm_useyn='Y') \n");
							strQuery.append("                         start with cm_deptcd=?     \n");
							strQuery.append("                         connect by prior cm_updeptcd=cm_deptcd)) \n");
							strQuery.append("   and a.cm_userid=b.cm_userid                      \n");
							strQuery.append("   and instr(?,rtrim(b.cm_rgtcd))>0                 \n");
							strQuery.append("   and d.cm_macode='POSITION'		                 \n");
							strQuery.append("   and d.cm_micode=a.cm_position	                 \n");
							strQuery.append("order by a.cm_userid   \n");
							pstmt2 = conn.prepareStatement(strQuery.toString());
							pstmt2 = new LoggableStatement(conn,strQuery.toString());
							pstmt2.setString(pstmtcount++, UserId);
							pstmt2.setString(pstmtcount++, svDeptCd);
							pstmt2.setString(pstmtcount++, svDeptCd);
				            pstmt2.setString(pstmtcount++, rs.getString("cm_position"));
				            ecamsLogger.error(((LoggableStatement)pstmt2).getQueryString());
				            rs2 = pstmt2.executeQuery();
				            if (rs2.next()) {
				            	if(!UserId.equals(rs2.getString("cm_userid"))){
					            	SvUser = rs2.getString("cm_userid");
					            	SvTag = rs2.getString("cm_username");
					            	SvSum = rs2.getString("cm_codename") + " " + rs2.getString("cm_username");
					            	SvBaseUser = rs2.getString("cm_userid");
					            	FindSw = true;
				            	}else{
				            		FindSw = false;
				            	}
				            }
				            rs2.close();
				            pstmt2.close();
				            
				            if (FindSw == false) {
				            	SvUser = "";
								strQuery.setLength(0);
								pstmtcount = 1;
								strQuery.append("select distinct a.cm_userid,a.cm_username,          \n");
								strQuery.append("       a.cm_status,a.cm_duty,a.cm_deptseq,d.cm_codename           \n");
								strQuery.append("  from cmm0040 a,cmm0043 b, cmm0020 d                \n");
								strQuery.append(" where a.cm_active='1'                              \n");
								strQuery.append("   and a.cm_manid='Y'                              \n");
								strQuery.append("   and (a.cm_project in (select cm_deptcd           \n");
								strQuery.append("                          from (select * from cmm0100 where cm_useyn='Y') \n");
								strQuery.append("                         start with cm_deptcd=?     \n");
								strQuery.append("                         connect by prior cm_updeptcd=cm_deptcd) \n");
								strQuery.append("   or a.cm_project2 in (select cm_deptcd            \n");
								strQuery.append("                          from (select * from cmm0100 where cm_useyn='Y') \n");
								strQuery.append("                         start with cm_deptcd=?     \n");
								strQuery.append("                         connect by prior cm_updeptcd=cm_deptcd)) \n");
								strQuery.append("   and a.cm_userid=b.cm_userid                      \n");
								strQuery.append("   and instr(?,rtrim(b.cm_rgtcd))>0                 \n");
								strQuery.append("   and a.cm_userid != ?	                 	\n");
								strQuery.append("   and d.cm_macode='POSITION'		                 \n");
								strQuery.append("   and d.cm_micode=a.cm_position			                 \n");
								strQuery.append("order by a.cm_userid   \n");
								pstmt2 = conn.prepareStatement(strQuery.toString());
								pstmt2 = new LoggableStatement(conn,strQuery.toString());
								pstmt2.setString(pstmtcount++, svDeptCd);
								pstmt2.setString(pstmtcount++, svDeptCd);
					            pstmt2.setString(pstmtcount++, rs.getString("cm_position"));
					            pstmt2.setString(pstmtcount++, UserId);
					            ecamsLogger.error(((LoggableStatement)pstmt2).getQueryString());
					            rs2 = pstmt2.executeQuery();
					            while (rs2.next()) {
					            	if (befConfUser.length()>0 && befConfUser.equals(rs2.getString("cm_userid"))) FindSw = true;
					            	
					            	if (FindSw || rs2.getRow()==1) {
						            	SvUser = rs2.getString("cm_userid");
						            	SvTag = rs2.getString("cm_username");
						            	SvSum = rs2.getString("cm_codename") + " " + rs2.getString("cm_username");
						            	SvBaseUser = rs2.getString("cm_userid");
					            	}
					            	if (FindSw) break;
					            }
					            pstmt2.close();
					            rs2.close();
					            
					            if (!FindSw && SvUser.length()>0) FindSw = true;
				            }
				            
				            if (FindSw == false) {
				            	strQuery.setLength(0);
								pstmtcount = 1;
								strQuery.append("select distinct a.cm_userid,a.cm_username,          \n");
								strQuery.append("       a.cm_status,a.cm_duty,a.cm_deptseq,d.cm_codename           \n");
								strQuery.append("  from cmm0040 a,cmm0043 b, cmm0020 d                \n");
								strQuery.append(" where a.cm_active='1'                              \n");
								strQuery.append("   and a.cm_manid='Y'                              \n");
								strQuery.append("   and (a.cm_project in (select cm_deptcd           \n");
								strQuery.append("                          from (select * from cmm0100 where cm_useyn='Y') \n");
								strQuery.append("                         where cm_updeptcd = (select cm_updeptcd from cmm0100 where cm_deptcd=?))     \n");
								strQuery.append("   or a.cm_project2 in (select cm_deptcd            \n");
								strQuery.append("                          from (select * from cmm0100 where cm_useyn='Y') \n");
								strQuery.append("                         where cm_updeptcd = (select cm_updeptcd from cmm0100 where cm_deptcd=?)))     \n");
								strQuery.append("   and a.cm_userid=b.cm_userid                      \n");
								strQuery.append("   and instr(?,rtrim(b.cm_rgtcd))>0                 \n");
								strQuery.append("   and a.cm_userid != ?	                 	\n");
								strQuery.append("   and d.cm_macode='POSITION'		                 \n");
								strQuery.append("   and d.cm_micode=a.cm_position			                 \n");
								strQuery.append("order by a.cm_userid   \n");
								pstmt2 = conn.prepareStatement(strQuery.toString());
								pstmt2 = new LoggableStatement(conn,strQuery.toString());
								pstmt2.setString(pstmtcount++, svDeptCd);
								pstmt2.setString(pstmtcount++, svDeptCd);
					            pstmt2.setString(pstmtcount++, rs.getString("cm_position"));
					            pstmt2.setString(pstmtcount++, UserId);
					            ecamsLogger.error(((LoggableStatement)pstmt2).getQueryString());
					            rs2 = pstmt2.executeQuery();
					            if (rs2.next()) {
					            	FindSw = true;
					            	SvUser = rs2.getString("cm_userid");
					            	SvTag = rs2.getString("cm_username");
					            	SvSum = rs2.getString("cm_codename") + " " + rs2.getString("cm_username");
					            	SvBaseUser = rs2.getString("cm_userid");
					            }
					            pstmt2.close();
					            rs2.close();
				            }
							
				            if (FindSw == true) {
					            strQuery.setLength(0);
				            	strQuery.append("select b.cm_username,b.cm_userid,b.cm_duty,d.cm_codename       \n");
				            	strQuery.append("  from cmm0042 a,cmm0040 b, cmm0020 d            \n");
				            	strQuery.append(" where b.cm_userid=?                             \n");
				            	strQuery.append("   and a.cm_userid(+)=b.cm_userid                \n");
				            	strQuery.append("   and to_char(a.cm_blkstdate(+),'yyyymmdd')<=to_char(SYSDATE,'yyyymmdd')\n");
				            	strQuery.append("   and to_char(a.cm_blkeddate(+),'yyyymmdd')>=to_char(SYSDATE,'yyyymmdd')\n");
				            	strQuery.append("   and nvl(a.cm_daeusr(+),'0000000')=b.cm_userid      \n");
				            	strQuery.append("   and d.cm_macode='POSITION'		                 \n");
								strQuery.append("   and d.cm_micode=b.cm_position		                 \n");
				            	pstmt3 = conn.prepareStatement(strQuery.toString());
				            	pstmt3 = new LoggableStatement(conn,strQuery.toString());
				            	pstmt3.setString(1, SvUser);
				            	ecamsLogger.error(((LoggableStatement)pstmt3).getQueryString());
				            	rs3 = pstmt3.executeQuery();
				            	if (rs3.next()) {
				            		SvTag = rs3.getString("cm_username");
					            	SvUser = rs3.getString("cm_userid");
					            	SvSum = rs3.getString("cm_codename") + " " + rs3.getString("cm_username");
				            	}
				            	rs3.close();
				            	pstmt3.close();
				            }
				            
				            strQuery.setLength(0);
				            strQuery.append("select cm_codename from cmm0020                      \n");
				            strQuery.append(" where cm_macode='SGNCD' and cm_micode=?             \n");
				            
				            pstmt2 = conn.prepareStatement(strQuery.toString());
				            pstmt2 = new LoggableStatement(conn,strQuery.toString());
				            pstmt2.setString(1, SvLine);
				            ecamsLogger.error(((LoggableStatement)pstmt2).getQueryString());			            
				            rs2 = pstmt2.executeQuery();	
				            if (rs2.next()) {
				            	if(isSysChk && SvLine.equals("4"))
				            		SvSgnName = "참조";
				            	else
				            		SvSgnName = rs2.getString("cm_codename");
				            }
				            rs2.close();
				            pstmt2.close();
				            
				            HashSv = new HashMap<String, String>();
							HashSv.put("SvUser", SvUser);
							HashSv.put("SvTag", SvTag);
							HashSv.put("SvSum", SvSum);
							ArySv.add(HashSv);
							HashSv = null;
							
						}
						// 20221223 내부직원결재시 해당 업무 가진 사람들 다 나오게 추가
						else if(rs.getString("cm_gubun").equals("B")){
							SvUser = "";
							SvBaseUser = "";
							SvTag = "";
							SvSgnName = "결재(순차)";
							
							FindSw = false;
							strQuery.setLength(0);
							pstmtcount = 1;							
							strQuery.append("SELECT DISTINCT a.cm_userid, a.cm_username,a.cm_position,  \n");        
							strQuery.append("			     a.cm_status,a.cm_duty, c.cm_codename    	\n");            
							strQuery.append("  FROM CMM0040 a, cmm0044 b,  cmm0020 c, cmm0043 d			\n");     
							strQuery.append(" WHERE b.cm_syscd = ? 										\n");     
							strQuery.append("   AND a.cm_userid = b.cm_userid							\n");
							strQuery.append("   AND a.cm_userid = d.cm_userid							\n");
							strQuery.append("   AND instr(?,rtrim(d.cm_rgtcd))>0                 \n");
							strQuery.append("   AND c.cm_macode='POSITION'								\n");                      
							strQuery.append("   AND c.cm_micode=a.cm_position							\n");     
							strQuery.append("   AND a.cm_manid = 'Y'		    						\n");              
							strQuery.append("   order by a.cm_userid   									\n");
							pstmt2 = conn.prepareStatement(strQuery.toString());
							pstmt2 = new LoggableStatement(conn,strQuery.toString());
							pstmt2.setString(pstmtcount++, SysCd);
							pstmt2.setString(pstmtcount++, rs.getString("cm_position"));
							
				            ecamsLogger.error(((LoggableStatement)pstmt2).getQueryString());
				            rs2 = pstmt2.executeQuery();
				            if (rs2.next()) {
				            	if(!UserId.equals(rs2.getString("cm_userid"))){
				            	SvUser = rs2.getString("cm_userid");
				            	SvTag = rs2.getString("cm_username");
				            	SvSum = rs2.getString("cm_codename") + " " + rs2.getString("cm_username");
				            	SvBaseUser = rs2.getString("cm_userid");
				            	}
				            }
				            rs2.close();
				            pstmt2.close();
				            HashSv = new HashMap<String, String>();
							HashSv.put("SvUser", SvUser);
							HashSv.put("SvTag", SvTag);
							HashSv.put("SvSum", SvSum);
							ArySv.add(HashSv);
							HashSv = null;
							FindSw = true;
						}
						else if (rs.getString("cm_gubun").equals("6") || rs.getString("cm_gubun").equals("7")) {
			            	strQuery.setLength(0);
			            	pstmtcount = 1;
							strQuery.append("select a.cm_userid,a.cm_username,a.cm_status,a.cm_position,b.cm_codename,a.cm_duty \n");
							strQuery.append("  from cmm0043 c, cmm0020 b, cmm0040 a where               \n");
							if (!rs.getString("cm_gubun").equals("5")){ 
								strQuery.append("a.cm_project=? and                                     \n");
							}
							strQuery.append("    a.cm_active='1'                                        \n");
							strQuery.append("and b.cm_macode='POSITION' and a.cm_position=b.cm_micode   \n");
							strQuery.append("and a.cm_userid = c.cm_userid                              \n");
							strQuery.append("and instr(?,c.cm_rgtcd)>0                                  \n");
							strQuery.append("order by a.cm_position desc,a.cm_userid desc               \n");
							//pstmt = new LoggableStatement(conn,strQuery.toString());
							pstmt2 = conn.prepareStatement(strQuery.toString());
				            if (!rs.getString("cm_gubun").equals("5")){ 
				            	pstmt2.setString(pstmtcount++, rs.getString("cm_project"));
				            }
				            pstmt2.setString(pstmtcount++, rs.getString("cm_position"));
							////ecamsLogger.error(((LoggableStatement)pstmt).getQueryString());
							rs2 = pstmt2.executeQuery();
			                while (rs2.next()){
			                	SvBaseUser = rs2.getString("cm_userid");
			                	strQuery.setLength(0);
			                	pstmtcount = 1;
								strQuery.append("select cm_daeusr from cmm0042                             \n");
								strQuery.append(" where cm_userid=?                                         \n");
								strQuery.append("   and cm_blkstdate is not null                             \n");
								strQuery.append("   and cm_blkeddate is not null                             \n");
								strQuery.append("   and cm_blkstdate<=to_char(sysdate,'yyyymmdd')            \n");
								strQuery.append("   and cm_blkeddate>=to_char(sysdate,'yyyymmdd')            \n");
								//pstmt = new LoggableStatement(conn,strQuery.toString());
								pstmt3 = conn.prepareStatement(strQuery.toString());
								pstmt3.setString(pstmtcount++, rs2.getString("cm_userid"));
					            ////ecamsLogger.error(((LoggableStatement)pstmt).getQueryString());	
					            rs3 = pstmt3.executeQuery();	
					            if (rs3.next()) {
					            	if (!rs2.getString("cm_userid").equals(rs3.getString("cm_daeusr"))) {
					            		strQuery.setLength(0);
										strQuery.append("select a.cm_username,a.cm_duty,b.cm_codename               \n");
										strQuery.append("  from cmm0040 a,cmm0020 b                                 \n");
										strQuery.append(" where a.cm_userid=?                                       \n");
										strQuery.append("   and b.cm_macode='POSITION'                              \n");
										strQuery.append("   and b.cm_micode=a.cm_position                           \n");
										//pstmt = new LoggableStatement(conn,strQuery.toString());
							            pstmt4 = conn.prepareStatement(strQuery.toString());
							            pstmt4.setString(1, rs3.getString("cm_daeusr"));

							            rs4 = pstmt4.executeQuery();	
							            if (rs4.next()) {
							            	SvTag = rs4.getString("cm_username");
							            	SvUser = rs3.getString("cm_daeusr");
							            	SvSum = rs4.getString("cm_codename")+" "+rs4.getString("cm_username");
							            }
							            
							            rs4.close();
							            pstmt4.close();
							            
					            	}		
					            } else {
					            	SvTag = rs2.getString("cm_username");
					            	SvUser = rs2.getString("cm_userid");
					            	SvSum = rs2.getString("cm_codename")+" "+rs2.getString("cm_username");
					            }
					            rs3.close();
					            pstmt3.close();
					            
					            strQuery.setLength(0);
					            strQuery.append("select cm_codename from cmm0020                      \n");
					            strQuery.append(" where cm_macode='SGNCD' and cm_micode=?             \n");
					            
					            pstmt3 = conn.prepareStatement(strQuery.toString());
					            //pstmt3 = new LoggableStatement(conn,strQuery.toString());
					            pstmt3.setString(1, SvLine);
					            ////ecamsLogger.error(((LoggableStatement)pstmt3).getQueryString());
					            rs3 = pstmt3.executeQuery();	
					            if (rs3.next()) {
					            	if(isSysChk && SvLine.equals("4"))
					            		SvSgnName = "참조";
					            	else
					            		SvSgnName = rs3.getString("cm_codename");
					            }
					            rs3.close();
					            pstmt3.close();
					            
								HashSv = new HashMap<String, String>();
								HashSv.put("SvUser", SvUser);
								HashSv.put("SvTag", SvTag);
								HashSv.put("SvSum", SvSum);
								ArySv.add(HashSv);
								HashSv = null;
					            FindSw = true;
					            
				            }
			                
			                rs2.close();
			                pstmt2.close();
			                
			                if (ArySv.size() == 0){
			                	throw new Exception("결재가능한 사용자가 없습니다. ["+ rs.getString("cm_name") + "]");
				            }
						}
						else if (rs.getString("cm_gubun").equals("Z")) {
			            	strQuery.setLength(0);
		            		strQuery.append("select  a.cc_thirduser, b.cm_userid, b.cm_username, c.cm_codename	\n");
		            		strQuery.append("from cmc0421 a, cmm0040 b, cmm0020 c				\n");
		            		strQuery.append("where a.cc_thirduser=?								\n");
		            		strQuery.append("and b.cm_userid = a.cc_thirduser					\n");
		            		strQuery.append("and c.cm_macode='POSITION' and b.cm_position=c.cm_micode   \n");
				            pstmt2 = conn.prepareStatement(strQuery.toString());
				            //pstmt2 = new LoggableStatement(conn,strQuery.toString());
				            pstmtcount = 1;
						    pstmt2.setString(pstmtcount++, thirdlist.get(0).get("cc_thirduser"));
							//ecamsLogger.error(((LoggableStatement)pstmt2).getQueryString());
							rs2 = pstmt2.executeQuery();
			                if (rs2.next()){
			                	SvBaseUser = rs2.getString("cc_thirduser");
			                	strQuery.setLength(0);
								strQuery.append("select cm_daeusr from cmm0042                             \n");
								strQuery.append(" where cm_userid=?                                         \n");
								strQuery.append("   and cm_blkstdate is not null                             \n");
								strQuery.append("   and cm_blkeddate is not null                             \n");
								strQuery.append("   and cm_blkstdate<=to_char(sysdate,'yyyymmdd')            \n");
								strQuery.append("   and cm_blkeddate>=to_char(sysdate,'yyyymmdd')            \n");
								pstmt3 = conn.prepareStatement(strQuery.toString());
								//pstmt3 = new LoggableStatement(conn,strQuery.toString());
								pstmtcount = 1;
								pstmt3.setString(pstmtcount++, rs2.getString("cc_thirduser"));
					            //ecamsLogger.error(((LoggableStatement)pstmt3).getQueryString());
					            rs3 = pstmt3.executeQuery();	
					            if (rs3.next()) {
					            	if (!rs2.getString("cc_thirduser").equals(rs3.getString("cm_daeusr"))) {
					            		strQuery.setLength(0);
										strQuery.append("select a.cm_username,a.cm_duty,b.cm_codename               \n");
										strQuery.append("  from cmm0040 a,cmm0020 b                                 \n");
										strQuery.append(" where a.cm_userid=?                                       \n");
										strQuery.append("   and b.cm_macode='POSITION'                              \n");
										strQuery.append("   and b.cm_micode=a.cm_position                           \n");
							            pstmt4 = conn.prepareStatement(strQuery.toString());
										//pstmt4 = new LoggableStatement(conn,strQuery.toString());
							            pstmt4.setString(1, rs3.getString("cm_daeusr"));
							            //ecamsLogger.error(((LoggableStatement)pstmt4).getQueryString());
							            rs4 = pstmt4.executeQuery();	
							            if (rs4.next()) {
							            	SvTag = rs4.getString("cm_username");
							            	SvUser = rs3.getString("cm_daeusr");
							            	SvSum = rs4.getString("cm_codename")+" "+rs4.getString("cm_username");
							            }
							            
							            rs4.close();
							            pstmt4.close();
							            
					            	}		
					            } else {
					            	SvTag = rs2.getString("cm_username");
					            	SvUser = rs2.getString("cm_userid");
					            	SvSum = rs2.getString("cm_codename")+" "+rs2.getString("cm_username");
					            }
					            rs3.close();
					            pstmt3.close();
					            
					            strQuery.setLength(0);
					            strQuery.append("select cm_codename from cmm0020                      \n");
					            strQuery.append(" where cm_macode='SGNCD' and cm_micode=?             \n");

					            pstmt3 = conn.prepareStatement(strQuery.toString());
					            //pstmt3 = new LoggableStatement(conn,strQuery.toString());
					            pstmt3.setString(1, SvLine);
					            //ecamsLogger.error(((LoggableStatement)pstmt3).getQueryString());			            
					            rs3 = pstmt3.executeQuery();	
					            if (rs3.next()) {
					            	if(isSysChk && SvLine.equals("4"))
					            		SvSgnName = "참조";
					            	else
					            		SvSgnName = rs3.getString("cm_codename");
					            }
					            rs3.close();
					            pstmt3.close();
					            
								HashSv = new HashMap<String, String>();
								HashSv.put("SvUser", SvUser);
								HashSv.put("SvTag", SvTag);
								HashSv.put("SvSum", SvSum);
								ArySv.add(HashSv);
								HashSv = null;
					            FindSw = true;
					            
				            }
			                
			                thirdlist.remove(0);
			                
			                rs2.close();
			                pstmt2.close();
			                if (ArySv.size() == 0){
			                	throw new Exception("결재가능한 사용자가 없습니다. ["+ rs.getString("cm_name") + "]");
				            }
						}
						else if (rs.getString("cm_gubun").equals("X") && YnCheck.equals("Yes")) {
			            	strQuery.setLength(0);
			            	strQuery.append("select A.CC_REQID, A.CC_DEPT3, A.CC_REQUSER3,C.CM_CODENAME,	\n");
			            	strQuery.append(" B.CM_USERID, B.CM_USERNAME			      	\n");
							strQuery.append(" from cmc0401 A, cmm0040 B, cmm0020 C         	\n");
							strQuery.append(" where a.CC_REQUSER3 =  ?						\n");
							strQuery.append("and A.CC_REQUSER3 = B.CM_USERID				\n");
							strQuery.append("and c.cm_macode='POSITION' and b.cm_position=c.cm_micode   \n");
							pstmt = conn.prepareStatement(strQuery.toString());
				            pstmt2 = conn.prepareStatement(strQuery.toString());
				            //pstmt2 = new LoggableStatement(conn,strQuery.toString());
				            pstmtcount = 1;
						    pstmt2.setString(pstmtcount++, requserlist.get(0).get("cc_requser3"));
							//ecamsLogger.error(((LoggableStatement)pstmt2).getQueryString());
							rs2 = pstmt2.executeQuery();
			                if (rs2.next()){
			                	SvBaseUser = rs2.getString("CC_REQUSER3");
			                	strQuery.setLength(0);
								strQuery.append("select cm_daeusr from cmm0042                             \n");
								strQuery.append(" where cm_userid=?                                         \n");
								strQuery.append("   and cm_blkstdate is not null                             \n");
								strQuery.append("   and cm_blkeddate is not null                             \n");
								strQuery.append("   and cm_blkstdate<=to_char(sysdate,'yyyymmdd')            \n");
								strQuery.append("   and cm_blkeddate>=to_char(sysdate,'yyyymmdd')            \n");
								pstmt3 = conn.prepareStatement(strQuery.toString());
								//pstmt3 = new LoggableStatement(conn,strQuery.toString());
								pstmtcount = 1;
								pstmt3.setString(pstmtcount++, rs2.getString("CC_REQUSER3"));
					            //ecamsLogger.error(((LoggableStatement)pstmt3).getQueryString());
					            rs3 = pstmt3.executeQuery();	
					            if (rs3.next()) {
					            	if (!rs2.getString("CC_REQUSER3").equals(rs3.getString("cm_daeusr"))) {
					            		strQuery.setLength(0);
										strQuery.append("select a.cm_username,a.cm_duty,b.cm_codename               \n");
										strQuery.append("  from cmm0040 a,cmm0020 b                                 \n");
										strQuery.append(" where a.cm_userid=?                                       \n");
										strQuery.append("   and b.cm_macode='POSITION'                              \n");
										strQuery.append("   and b.cm_micode=a.cm_position                           \n");
							            pstmt4 = conn.prepareStatement(strQuery.toString());
										//pstmt4 = new LoggableStatement(conn,strQuery.toString());
							            pstmt4.setString(1, rs3.getString("cm_daeusr"));
							            //ecamsLogger.error(((LoggableStatement)pstmt4).getQueryString());
							            rs4 = pstmt4.executeQuery();	
							            if (rs4.next()) {
							            	SvTag = rs4.getString("cm_username");
							            	SvUser = rs3.getString("cm_daeusr");
							            	SvSum = rs4.getString("cm_codename")+" "+rs4.getString("cm_username");
							            }
							            
							            rs4.close();
							            pstmt4.close();
							            
					            	}		
					            } else {
					            	SvTag = rs2.getString("cm_username");
					            	SvUser = rs2.getString("cm_userid");
					            	SvSum = rs2.getString("cm_codename")+" "+rs2.getString("cm_username");
					            }
					            rs3.close();
					            pstmt3.close();
					            
					            strQuery.setLength(0);
					            strQuery.append("select cm_codename from cmm0020                      \n");
					            strQuery.append(" where cm_macode='SGNCD' and cm_micode=?             \n");

					            pstmt3 = conn.prepareStatement(strQuery.toString());
					            //pstmt3 = new LoggableStatement(conn,strQuery.toString());
					            pstmt3.setString(1, SvLine);
					            //ecamsLogger.error(((LoggableStatement)pstmt3).getQueryString());			            
					            rs3 = pstmt3.executeQuery();	
					            if (rs3.next()) {
					            	if(isSysChk && SvLine.equals("4"))
					            		SvSgnName = "참조";
					            	else
					            	SvSgnName = rs3.getString("cm_codename");
					            }
					            rs3.close();
					            pstmt3.close();
					            
								HashSv = new HashMap<String, String>();
								HashSv.put("SvUser", SvUser);
								HashSv.put("SvTag", SvTag);
								HashSv.put("SvSum", SvSum);
								ArySv.add(HashSv);
								HashSv = null;
					            FindSw = true;
					            
				            }
			                
			                requserlist.remove(0);
			                
			                rs2.close();
			                pstmt2.close();
			                if (ArySv.size() == 0){
			                	throw new Exception("결재가능한 사용자가 없습니다. ["+ rs.getString("cm_name") + "]");
				            }
						}
						else {
							strQuery.setLength(0);
							SvUser = rs.getString("cm_position");
							SvBaseUser = rs.getString("cm_position");
							strQuery.append("select cm_codename from cmm0020                 \n");
							strQuery.append(" where cm_macode = ? and instr(?,cm_micode)>0   \n");
							
							pstmt2 = conn.prepareStatement(strQuery.toString());
							//pstmt2 = new LoggableStatement(conn,strQuery.toString());
							if (rs.getString("cm_gubun").equals("P")) pstmt2.setString(1, "PGMJIK");
							else pstmt2.setString(1, "RGTCD");
							pstmt2.setString(2, rs.getString("cm_position"));
							//ecamsLogger.error(((LoggableStatement)pstmt2).getQueryString());
				            rs2 = pstmt2.executeQuery();	
				            if (rs2.next()) {
				            	SvTag = rs2.getString("cm_codename");
				            	SvSum = SvTag;
				            }	
				            rs2.close();
				            pstmt2.close();
	
				            strQuery.setLength(0);
				            strQuery.append("select cm_codename from cmm0020                      \n");
				            strQuery.append(" where cm_macode='SGNCD' and cm_micode=?             \n");
				            
				            pstmt2 = conn.prepareStatement(strQuery.toString());
				            //pstmt2 = new LoggableStatement(conn,strQuery.toString());
				            pstmt2.setString(1, SvLine);
				            ////ecamsLogger.error(((LoggableStatement)pstmt2).getQueryString());	
				            rs2 = pstmt2.executeQuery();	
				            if (rs2.next()) {
				            	if(isSysChk && SvLine.equals("4"))
				            		SvSgnName = "참조";
				            	else
				            		SvSgnName = rs2.getString("cm_codename");
				            }
				            rs2.close();
				            pstmt2.close();
				            
							HashSv = new HashMap<String, String>();
							HashSv.put("SvUser", SvUser);
							HashSv.put("SvTag", SvTag);
							HashSv.put("SvSum", SvSum);
							ArySv.add(HashSv);
							HashSv = null;
							
				            FindSw = true;
						}
					}
					Strgubun ="";
					if (FindSw == true && !"".equals(SvLine) && SvLine != null) {
						
						if (rs.getString("cm_gubun").equals("C") || 
							rs.getString("cm_gubun").equals("3")) {
							strQuery.setLength(0);
				            strQuery.append("select cm_codename from cmm0020                      \n");
				            strQuery.append(" where cm_macode='SGNCD' and cm_micode=?             \n");
				            
				            pstmt3 = conn.prepareStatement(strQuery.toString());
				            //pstmt3 = new LoggableStatement(conn,strQuery.toString());
				            pstmt3.setString(1, SvLine);
				            ////ecamsLogger.error(((LoggableStatement)pstmt3).getQueryString());
				            rs3 = pstmt3.executeQuery();	
				            if (rs3.next()) {
				            	SvSgnName = rs3.getString("cm_codename");
				            }
				            rs3.close();
				            pstmt3.close();
						}
						rst = new HashMap<Object, Object>();
						rst.put("cm_name", rs.getString("cm_name"));
						rst.put("arysv", ArySv);
						rst.put("cm_sgnname", SvSgnName);
						rst.put("cm_baseuser", SvBaseUser);
						rst.put("cm_congbn", SvLine);
						rst.put("cm_emg2", rs.getString("cm_emg2"));
						rst.put("cm_orgstep", rs.getString("cm_orgstep"));
						rst.put("cm_prcsw", rs.getString("cm_prcsw"));
						rst.put("cm_position", rs.getString("cm_position"));
						rst.put("cm_gubun", rs.getString("cm_gubun"));
						Strgubun =  rs.getString("cm_gubun");
						rst.put("cm_common", rs.getString("cm_common"));
						rst.put("cm_blank", rs.getString("cm_blank"));
						rst.put("cm_holi", rs.getString("cm_holiday"));
						rst.put("cm_emg", rs.getString("cm_emg"));
						if (rs.getString("cm_gubun").equals("3") || rs.getString("cm_gubun").equals("C")
						    || rs.getString("cm_gubun").equals("5") || rs.getString("cm_gubun").equals("6")
						    || rs.getString("cm_gubun").equals("7") || rs.getString("cm_gubun").equals("8")) {
							rst.put("cm_duty", rs.getString("cm_position"));
						} else rst.put("cm_duty", rs.getString("cm_jobcd"));
						rst.put("cm_seqno", "0");
						if (rs.getString("cm_gubun").equals("3") || rs.getString("cm_gubun").equals("C")){
							rst.put("userSetable", true);
						}
						else{
							rst.put("userSetable", false);
						}
						if (rs.getString("cm_gubun").equals("8")) rst.put("visible", "1");
						else if (rs.getString("cm_gubun").equals("3") && rs.getString("cm_orgstep").equals("Y")) rst.put("visible", "1");
						else rst.put("visible", "0");
						rsval.add(rst);
						rst = null;
					}	            	
	            }
			}//end of while-loop statement
			rs.close();
			pstmt.close();
			
			boolean findSw = false;
			if (ReqCd.equals("03") || ReqCd.equals("04") || ReqCd.equals("60")) {
				
				for (i=0;rsval.size()>i;i++) {
					if (rsval.get(i).get("cm_gubun").equals("1") && rsval.get(i).get("cm_duty").equals("SYSED")) {
						findSw = true;
						break;
					}
				}
				if (findSw == false) {
					for (i=0;rsval.size()>i;i++) {
						if (rsval.get(i).get("cm_gubun").equals("4") && rsval.get(i).get("cm_position").equals("D1")) {
							//rsval.remove(i);
							break;
						}
					}
				}
			}
			
			findSw = false;
			
			for (i=0;rsval.size()>i;i++) {
				if (rsval.get(i).get("cm_gubun").toString().equals("8")) {
					rst = new HashMap<Object, Object>();
					rst = rsval.get(i);
					SvSgnName = rst.get("cm_name").toString();
					findSw = true;
					break;
				}
			}
			int j = 0;
			if (findSw == true) {				
				strQuery.setLength(0);
				strQuery.append("select a.cm_deptname,b.cm_username,b.cm_userid, d.cm_codename        \n");
	        	strQuery.append("  from cmm0043 c,cmm0040 b,cmm0100 a, cmm0020 d       \n");
	        	strQuery.append(" where c.cm_rgtcd=? and c.cm_userid=b.cm_userid       \n");
	        	strQuery.append("   and b.cm_active='1' and b.cm_project<>?            \n");
	        	if(rst.get("cm_gubun").toString().equals("8")){
		        	strQuery.append("   and b.cm_project like 'F1%'                       \n");	        		
	        	}
	        	strQuery.append("   and b.cm_project=a.cm_deptcd                       \n");
	        	strQuery.append("   and d.cm_macode='POSITION' and d.cm_micode = b.cm_position  \n");
	        	pstmt = conn.prepareStatement(strQuery.toString());
	            //pstmt = new LoggableStatement(conn,strQuery.toString());
	        	pstmt.setString(1, rst.get("cm_duty").toString());
	        	pstmt.setString(2, svDeptCd);
	            //ecamsLogger.error(((LoggableStatement)pstmt).getQueryString());
	            rs = pstmt.executeQuery();	
	            while (rs.next()) {
	            	SvTag = rs.getString("cm_username");
	            	SvUser = rs.getString("cm_userid");
	            	SvSum = rs.getString("cm_codename")+" "+rs.getString("cm_username");
	            	
	            	strQuery.setLength(0);
	            	strQuery.append("select b.cm_username,b.cm_userid,b.cm_duty, c.cm_codename       \n");
	            	strQuery.append("  from cmm0042 a,cmm0040 b, cmm0020 c            \n");
	            	strQuery.append(" where b.cm_userid=?                             \n");
	            	strQuery.append("   and a.cm_userid(+)=b.cm_userid                   \n");
	            	strQuery.append("   and a.cm_blkstdate(+)<=to_char(SYSDATE,'yyyymmdd')\n");
	            	strQuery.append("   and a.cm_blkeddate(+)>=to_char(SYSDATE,'yyyymmdd')\n");
	            	strQuery.append("   and nvl(a.cm_daeusr(+),'0000')=b.cm_userid      \n");
	            	strQuery.append("   and c.cm_macode='POSITION' and c.cm_micode = b.cm_position  \n");
	            	pstmt3 = conn.prepareStatement(strQuery.toString());
	            	//pstmt3 = new LoggableStatement(conn,strQuery.toString());
	            	pstmt3.setString(1, rs.getString("cm_userid"));
	            	//ecamsLogger.error(((LoggableStatement)pstmt3).getQueryString());
	            	rs3 = pstmt3.executeQuery();
	            	if (rs3.next()) {
	            		SvTag = rs3.getString("cm_username");
		            	SvUser = rs3.getString("cm_userid");
		            	SvSum = rs3.getString("cm_codename")+" "+rs3.getString("cm_username");
	            	}
	            	rs3.close();
	            	pstmt3.close();
	            	
	            	rst = (HashMap<Object, Object>) DeepCopy.deepCopy(rsval.get(i));
	            	ArySv = new ArrayList<HashMap<String, String>>();
	            	HashSv = new HashMap<String, String>();
					HashSv.put("SvTag", SvTag);
					HashSv.put("SvUser", SvUser);
					HashSv.put("SvSum", SvSum);
					ArySv.add(HashSv);
					rst.put("cm_name", SvSgnName+"["+rs.getString("cm_deptname")+"]");
					HashSv = null;
					rst.put("arysv", ArySv);
					rst.put("cm_baseuser", rs.getString("cm_userid"));
					if (j==0) {
						rsval.set(i, rst);
					} else {
						rsval.add(++i,rst);
					}
					j++;
					//ecamsLogger.error("+++rst+++"+rst.toString()+", "+Integer.toString(i));
	            }
	            rs.close();
	            pstmt.close();
	            
	            if (j==0) {
	            	rsval.remove(i);
	            }
			}
			conn.close();
			
			rs = null;
			pstmt = null;
			rs2 = null;
			pstmt2 = null;
			rs3 = null;
			pstmt3 = null;
			conn = null;
			
			returnObjectArray = rsval.toArray();
			rsval.clear();
			rsval = null;
			
			return returnObjectArray;			
			
		} catch (SQLException sqlexception) {
			sqlexception.printStackTrace();
			ecamsLogger.error("## Confirm_select.Confirm_Info() SQLException START ##");
			ecamsLogger.error("## Error DESC : ", sqlexception);	
			ecamsLogger.error("## Confirm_select.Confirm_Info() SQLException END ##");			
			throw sqlexception;
		} catch (Exception exception) {
			exception.printStackTrace();
			ecamsLogger.error("## Confirm_select.Confirm_Info() Exception START ##");				
			ecamsLogger.error("## Error DESC : ", exception);	
			ecamsLogger.error("## Confirm_select.Confirm_Info() Exception END ##");				
			throw exception;
		}finally{
			if (strQuery != null) 	strQuery = null;
			if (returnObjectArray != null)	returnObjectArray = null;
			if (rs != null)     try{rs.close();}catch (Exception ex){ex.printStackTrace();}
			if (pstmt != null)  try{pstmt.close();}catch (Exception ex2){ex2.printStackTrace();}
			if (rs2 != null)     try{rs2.close();}catch (Exception ex3){ex3.printStackTrace();}
			if (pstmt2 != null)  try{pstmt2.close();}catch (Exception ex4){ex4.printStackTrace();}
			if (rs3 != null)     try{rs3.close();}catch (Exception ex5){ex5.printStackTrace();}
			if (pstmt3 != null)  try{pstmt3.close();}catch (Exception ex6){ex6.printStackTrace();}
			if (rs4 != null)     try{rs4.close();}catch (Exception ex7){ex7.printStackTrace();}
			if (pstmt4 != null)  try{pstmt4.close();}catch (Exception ex8){ex8.printStackTrace();}
			if (conn != null){
				try{
					ConnectionResource.release(conn);
				}catch(Exception ex9){
					ecamsLogger.error("## Confirm_select.Confirm_Info() connection release exception ##");
					ex9.printStackTrace();
				}
			}
		}
	}//end of Confirm_Info() method statement
	
}//end of Confirm_select class statement
