/*****************************************************************************************
	1. program ID	: eCmd0600.java
	2. create date	: 2011.01. 12
	3. auth		    : No name
	4. update date	: 
	5. auth		    : 
	6. description	: eCmd0600 프로그램정보 일괄 수정
*****************************************************************************************/

package app.eCmd;

import java.sql.Connection;
import java.sql.PreparedStatement;
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
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */


public class Cmd0600{
    /**
     * Logger Class Instance Creation
     * logger
     */
    Logger ecamsLogger  = EcamsLogger.getLoggerInstance();


    /**
     * <PRE>
     * 1. MethodName	: getRsrcInfo
     * 2. ClassName		: Cmd0600
     * 3. Commnet			: 시스템 선택시 그 시스템에 포함된 프로그램종류 조회
     * 4. 작성자				: no name
     * 5. 작성일				: 2011. 1. 12. 오전 11:20:36
     * </PRE>
     * 		@return Object[]
     * 		@param Syscd
     * 		@param SelMsg
     * 		@return
     * 		@throws SQLException
     * 		@throws Exception
     */
    public Object[] getRsrcInfo(String Syscd,String SelMsg) throws SQLException, Exception {
		Connection        conn        = null;
		PreparedStatement pstmt       = null;
		ResultSet         rs          = null;
		StringBuffer      strQuery    = new StringBuffer();
		ArrayList<HashMap<String, String>>  rsval = new ArrayList<HashMap<String, String>>();
		HashMap<String, String>			  rst		  = null;
		Object[]		  rtObj		  = null;

		ConnectionContext connectionContext = new ConnectionResource();
		
		try {
			
			conn = connectionContext.getConnection();
			
			String        strSelMsg   = "";
			if (!"".equals(SelMsg)) {
				if (SelMsg.toUpperCase().equals("ALL")){
					strSelMsg = "전체";
				}
				else if (SelMsg.toUpperCase().equals("SEL")){
					strSelMsg = "선택하세요";
				}
				else{
					strSelMsg = "";
				}
			}
			strQuery.setLength(0);
			strQuery.append("select a.cm_micode,a.cm_codename,b.cm_info,d.cm_volpath,b.cm_exename \n");
			strQuery.append("  from cmm0020 a,cmm0036 b,cmm0031 c,cmm0038 d          \n");
			strQuery.append(" where b.cm_syscd=?                                     \n");
			strQuery.append("   and b.cm_closedt is null                             \n");
			strQuery.append("   and substr(b.cm_info,26,1)='0'                       \n");
			strQuery.append("   and b.cm_rsrccd not in (select cm_samersrc           \n");
			strQuery.append("                             from cmm0037               \n");
			strQuery.append("                            where cm_syscd=?            \n");
			strQuery.append("                              and cm_factcd='04')       \n");
			strQuery.append("   and a.cm_macode='JAWON' and a.cm_micode=b.cm_rsrccd  \n");
			strQuery.append("   and b.cm_syscd=d.cm_syscd and b.cm_rsrccd=d.cm_rsrccd\n");
			strQuery.append("   and d.cm_svrcd='01'                                  \n");
			strQuery.append("   and d.cm_syscd=c.cm_syscd and d.cm_svrcd=c.cm_svrcd  \n");
			strQuery.append("   and d.cm_seqno=c.cm_seqno and c.cm_closedt is null   \n");
			strQuery.append(" order by a.cm_codename								 \n");
			
            pstmt = conn.prepareStatement(strQuery.toString());
            //pstmt = new LoggableStatement(conn,strQuery.toString());
            pstmt.setString(1, Syscd);
            pstmt.setString(2, Syscd);
			////ecamsLogger.error(((LoggableStatement)pstmt).getQueryString());
            rs = pstmt.executeQuery();
            
            rsval.clear();
            
			while (rs.next()){
				if (rs.getRow() == 1 && strSelMsg.length() > 0 && !"".equals(strSelMsg)) {
					rst = new HashMap<String,String>();
					rst.put("cm_micode", "00");
					rst.put("cm_codename", strSelMsg);
					rsval.add(rst);
					rst = null;
				}
				rst = new HashMap<String, String>();
				rst.put("cm_micode", rs.getString("cm_micode"));
				rst.put("cm_codename", rs.getString("cm_codename"));
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

		} catch (SQLException sqlexception) {
			sqlexception.printStackTrace();
			ecamsLogger.error("## Cmd0600.getRsrcInfo() SQLException START ##");
			ecamsLogger.error("## Error DESC : ", sqlexception);
			ecamsLogger.error("## Cmd0600.getRsrcInfo() SQLException END ##");
			throw sqlexception;
		} catch (Exception exception) {
			exception.printStackTrace();
			ecamsLogger.error("## Cmd0600.getRsrcInfo() Exception START ##");
			ecamsLogger.error("## Error DESC : ", exception);
			ecamsLogger.error("## Cmd0600.getRsrcInfo() Exception END ##");
			throw exception;
		}finally{
			if (strQuery != null) 	strQuery = null;
			if (rtObj != null)	rtObj = null;
			if (rs != null)     try{rs.close();}catch (Exception ex){ex.printStackTrace();}
			if (pstmt != null)  try{pstmt.close();}catch (Exception ex2){ex2.printStackTrace();}
			if (conn != null){
				try{
					ConnectionResource.release(conn);
				}catch(Exception ex3){
					ecamsLogger.error("## Cmd0600.getRsrcInfo() connection release exception ##");
					ex3.printStackTrace();
				}
			}
		}
	}//end of getRsrcInfo() method statement


   	/**
   	 * <PRE>
   	 * 1. MethodName	: getSqlQry
   	 * 2. ClassName		: Cmd0600
   	 * 3. Commnet			: 프로그램리스트 조회
   	 * 4. 작성자				: no name
   	 * 5. 작성일				: 2011. 1. 12. 오전 11:20:08
   	 * </PRE>
   	 * 		@return Object[]
   	 * 		@param etcData
   	 * 		@return
   	 * 		@throws SQLException
   	 * 		@throws Exception
   	 */
   	public Object[] getSqlQry(HashMap<String,String> etcData) throws SQLException, Exception {
		Connection        conn        = null;
		PreparedStatement pstmt       = null;
		ResultSet         rs          = null;
		StringBuffer      strQuery    = new StringBuffer();
		ArrayList<HashMap<String, String>>  rtList = new ArrayList<HashMap<String, String>>();
		HashMap<String, String>			  rst		  = null;
		Object[]		  rtObj		  = null;
		int 			  CNT 		  = 0;
		int 			  i 		  = 0;
		String			  strRsrcCd   = "";
		String            strRsrc[]   = null;
		
		ConnectionContext connectionContext = new ConnectionResource();

		try {
			
			conn = connectionContext.getConnection();

	    	strQuery.setLength(0);
			strQuery.append("select cm_rsrccd from cmm0036                     \n");
			strQuery.append(" where cm_syscd=? and cm_closedt is null          \n");
			strQuery.append("   and substr(cm_info, 2, 1)='1'                  \n");
			strQuery.append("   and substr(cm_info, 26, 1)='0'                 \n");
			strQuery.append("   and cm_rsrccd not in (select cm_samersrc       \n");
			strQuery.append("                           from cmm0037           \n");
			strQuery.append("                          where cm_syscd=?)       \n");
			
			pstmt = conn.prepareStatement(strQuery.toString());
		//	pstmt =  new LoggableStatement(conn, strQuery.toString());
            pstmt.setString(1, etcData.get("cboSys"));
            pstmt.setString(2, etcData.get("cboSys"));
         //   //ecamsLogger.error(((LoggableStatement)pstmt).getQueryString());
            rs = pstmt.executeQuery();
            while (rs.next()) {
            	if (!"".equals(strRsrcCd)) strRsrcCd = strRsrcCd + ",";
            	strRsrcCd = strRsrcCd + rs.getString("cm_rsrccd");
            }
			rs.close();
			pstmt.close();
			if (strRsrcCd.length()>0) strRsrc = strRsrcCd.split(",");
			
		    strQuery.setLength(0);
		    strQuery.append("select a.cr_itemid,a.cr_syscd,a.cr_dsncd,a.cr_rsrcname, \n");
		    strQuery.append("      a.cr_rsrccd,a.cr_lstver,a.cr_status,a.cr_jobcd,    \n");
		    strQuery.append("      a.cr_story,a.cr_editor, a.cr_teamcd, a.cr_compile, \n");
		    strQuery.append("      to_char(a.cr_lastdate,'yyyy/mm/dd') cr_lastdate,   \n");
    		strQuery.append("      a.cr_langcd,h.cm_sysmsg,d.cm_jobname job,          \n");
    		strQuery.append("      e.cm_codename rsrccd,g.cm_dirpath,a.cr_master,     \n");
			strQuery.append("      h.cm_sysgb,i.cm_codename sta,j.cm_Info,l.cm_username as masname,     \n");
			strQuery.append("      b.cm_username,f.cm_codename as teamN, k.cm_codename as compN \n");
		    strQuery.append(" from cmm0036 j,cmm0030 h,cmm0070 g,cmm0020 i,cmm0102 d, cmm0040 l, \n");
		    strQuery.append("      cmm0020 e,cmm0044 c,cmm0040 b,cmr0020 a, cmm0020 f, cmm0020 k \n");
	    	strQuery.append("where a.cr_syscd = ? \n");
	    	strQuery.append("  and a.cr_syscd=c.cm_syscd \n");
	    	strQuery.append("  and a.cr_syscd=h.cm_syscd \n");
	        strQuery.append("  and a.cr_syscd=j.cm_syscd \n");
	        strQuery.append("  and a.cr_syscd=g.cm_syscd \n");
	    	if (!etcData.get("SecuYn").equals("Y"))	strQuery.append("  and b.cm_userid=? \n");
	    	strQuery.append("  and a.cr_editor=c.cm_userid \n");
	    	strQuery.append("  and a.cr_editor=b.cm_userid \n");
	    	strQuery.append("  and a.cr_jobcd=d.cm_jobcd \n");
	    	strQuery.append("  and a.cr_jobcd=c.cm_jobcd \n");
	    	strQuery.append("  and c.cm_jobcd=d.cm_jobcd \n");
	    	strQuery.append("  and a.cr_dsncd=g.cm_dsncd \n");
	    	strQuery.append("  and f.cm_macode='ETCTEAM' \n");
	    	strQuery.append("  and f.cm_micode=a.cr_teamcd \n");
	    	strQuery.append("  and k.cm_macode='COMPILE' \n");
	    	strQuery.append("  and k.cm_micode=a.cr_compile \n");
	    	strQuery.append("  and a.cr_master = l.cm_userid \n");
	    	if (!"".equals(etcData.get("txtRsrcName"))) strQuery.append("  and upper(a.cr_rsrcname) like upper(?) \n");
	    	strQuery.append("  and a.cr_rsrccd=j.cm_rsrccd \n");
	    	if (!etcData.get("cboRsrccd").equals("00")) strQuery.append("  and a.cr_rsrccd = ? \n");
	    	else if (strRsrc != null && strRsrc.length>0){
	        	strQuery.append("  and a.cr_rsrccd in (");
				for (i=0 ; i<strRsrc.length ; i++) {
					if (strRsrc.length-1>i) strQuery.append("? ,");
					else strQuery.append("? ");
				}
				strQuery.append(") \n");
	        }
	        strQuery.append("  and e.cm_macode='JAWON' and a.cr_rsrccd=e.cm_micode \n");
	        strQuery.append("  and i.cm_macode='CMR0020' and a.cr_status=i.cm_micode \n");
	        
	        pstmt = conn.prepareStatement(strQuery.toString());
	        pstmt =  new LoggableStatement(conn, strQuery.toString());
	        
	        CNT = 0;
            pstmt.setString(++CNT, etcData.get("cboSys"));
            if (!etcData.get("SecuYn").equals("Y")) pstmt.setString(++CNT, etcData.get("UserId"));
            if (!"".equals(etcData.get("txtRsrcName"))) pstmt.setString(++CNT, "%"+etcData.get("txtRsrcName")+"%");
            if (!etcData.get("cboRsrccd").equals("00")){
            	pstmt.setString(++CNT, etcData.get("cboRsrccd"));
		    }else if (strRsrc != null && strRsrc.length>0){
				for (i=0 ; i<strRsrc.length ; i++) {
	        		pstmt.setString(++CNT, strRsrc[i]);
	        	}
			}
            ecamsLogger.error(((LoggableStatement)pstmt).getQueryString());
            rs = pstmt.executeQuery();
            rtList.clear();
			while (rs.next()){
				rst = new HashMap<String,String>();
				rst.put("rsrccd",rs.getString("rsrccd"));
				rst.put("cr_rsrcname",rs.getString("cr_rsrcname"));
				rst.put("cr_story",rs.getString("cr_story"));
				rst.put("cm_dirpath",rs.getString("cm_dirpath"));
				rst.put("sta",rs.getString("sta"));
				rst.put("job",rs.getString("job"));
				rst.put("cr_editor",rs.getString("cr_editor"));
				rst.put("cm_username",rs.getString("cm_username"));
				rst.put("cr_lastdate",rs.getString("cr_lastdate"));
				rst.put("cm_sysgb",rs.getString("cm_sysgb"));
				rst.put("cr_syscd",rs.getString("cr_syscd"));
				rst.put("cr_rsrccd",rs.getString("cr_rsrccd"));
				rst.put("cr_itemid",rs.getString("cr_itemid"));
				rst.put("cr_dsncd",rs.getString("cr_dsncd"));
				rst.put("cr_jobcd",rs.getString("cr_jobcd"));
				rst.put("cm_info",rs.getString("cm_info"));	
				rst.put("cr_status",rs.getString("cr_status"));
				rst.put("cr_lstver",Integer.toString(rs.getInt("cr_lstver")));
				rst.put("cr_teamcd",rs.getString("cr_teamcd"));
				rst.put("cr_compile",rs.getString("cr_compile"));
				rst.put("teamN",rs.getString("teamN"));
				rst.put("compN",rs.getString("compN"));
				rst.put("masname",rs.getString("masname"));
				rst.put("cr_master",rs.getString("cr_master"));
				rtList.add(rst);
				rst = null;
			}
			rs.close();
			pstmt.close();
			conn.close();

			rs = null;
			pstmt = null;
			conn = null;
			rtObj = rtList.toArray();
			rtList.clear();
			rtList = null;

			return rtObj;

		} catch (SQLException sqlexception) {
			sqlexception.printStackTrace();
			throw sqlexception;
		} catch (Exception exception) {
			exception.printStackTrace();
			throw exception;
		}finally{
			if (strQuery != null) 	strQuery = null;
			if (rtObj != null)	rtObj = null;
			if (rs != null)     try{rs.close();}catch (Exception ex){ex.printStackTrace();}
			if (pstmt != null)  try{pstmt.close();}catch (Exception ex2){ex2.printStackTrace();}
			if (conn != null){
				try{
					ConnectionResource.release(conn);
				}catch(Exception ex3){
					ex3.printStackTrace();
				}
			}
		}
	}

	/**
	 * <PRE>
	 * 1. MethodName	: getDir
	 * 2. ClassName		: Cmd0600
	 * 3. Commnet			: 시스템에 등록된 경로 조회
	 * 4. 작성자				: no name
	 * 5. 작성일				: 2011. 1. 13. 오전 9:00:29
	 * </PRE>
	 * 		@return Object[]
	 * 		@param UserID
	 * 		@param SysCd
	 * 		@param SecuYn : 관리자 여부(Y:관리자)
	 * 		@param RsrcCd
	 * 		@param JobCd
	 * 		@param SelMsg
	 * 		@return
	 * 		@throws SQLException
	 * 		@throws Exception
	 */
	public Object[] getDir(String UserID,String SysCd,String SecuYn,String RsrcCd,String JobCd,String SelMsg) throws SQLException, Exception {
		Connection        conn        = null;
		PreparedStatement pstmt       = null;
		ResultSet         rs          = null;
		StringBuffer      strQuery    = new StringBuffer();

		ArrayList<HashMap<String, String>>  rsval = new ArrayList<HashMap<String, String>>();
		HashMap<String, String>			  rst		  = null;	
		Object[] returnObjectArray = null;
		
		String   strSelMsg   = "";
	    String   strJob[]    = {};
	    if (JobCd.length()>0) strJob = JobCd.split(",");
	    int      i = 0;
	    int      parmCnt = 0;
	    
		ConnectionContext connectionContext = new ConnectionResource();		
		
		try {
			conn = connectionContext.getConnection();
			
			if (!"".equals(SelMsg)) {
				if (SelMsg.equals("ALL")) strSelMsg = "전체";
				else if (SelMsg.equals("SEL")) strSelMsg = "선택하세요";
			}
			//JobCd =JobCd.replace(",", "','");
			strQuery.append("select a.cm_dsncd,a.cm_dirpath                         \n");
			strQuery.append(" from cmm0070 a,cmm0072 c,cmm0073 b                    \n");
			strQuery.append("where b.cm_syscd=?                                     \n");
			if (!SecuYn.equals("Y")) {
				strQuery.append("  and b.cm_jobcd in (select cm_jobcd from cmm0044      \n");
				strQuery.append("                      where cm_userid=? and cm_syscd=? \n");
				strQuery.append("                        and cm_closedt is null         \n");
				if (strJob.length>0){
					strQuery.append("                        and cm_jobcd in ( \n");
					for (i=0 ; strJob.length>i ; i++) {
						if (i>0) strQuery.append(", ? ");
						else strQuery.append("? ");
					}
					strQuery.append("))                                                \n");
				}
			} else {
				if (strJob.length>0){
					strQuery.append("and b.cm_jobcd in ( \n");
					for (i=0 ; strJob.length>i ; i++) {
						if (i>0) strQuery.append(", ? ");
						else strQuery.append("? ");
					}
					strQuery.append(")                                                  \n");
				}
			}
			strQuery.append("  and b.cm_syscd=a.cm_syscd and b.cm_dsncd=a.cm_dsncd  \n");
			strQuery.append("  and c.cm_syscd=? \n");
			if (!"".equals(RsrcCd)) strQuery.append("   and c.cm_rsrccd=? \n");
			strQuery.append("  and c.cm_syscd=a.cm_syscd and c.cm_dsncd=a.cm_dsncd  \n");
			strQuery.append("  and a.cm_clsdt is null                               \n");
			strQuery.append("group by a.cm_dirpath,a.cm_dsncd                       \n"); 	
			strQuery.append("order by a.cm_dirpath                                  \n"); 			
			
            pstmt = conn.prepareStatement(strQuery.toString());
            //pstmt =  new LoggableStatement(conn, strQuery.toString());
            pstmt.setString(++parmCnt, SysCd);
            if (!SecuYn.equals("Y")) {
            	pstmt.setString(++parmCnt, UserID);
            	pstmt.setString(++parmCnt, SysCd);
            }
            if (strJob.length>0){
	            for (i=0;strJob.length>i;i++) {
	            	pstmt.setString(++parmCnt, strJob[i]);
	            }
            }
            pstmt.setString(++parmCnt, SysCd);
            if (!"".equals(RsrcCd)) pstmt.setString(++parmCnt, RsrcCd);
            
            ////ecamsLogger.error(((LoggableStatement)pstmt).getQueryString());           
            rs = pstmt.executeQuery();
            
            rsval.clear();
            
			while (rs.next()){
				if (rs.getRow() == 1 && !"".equals(strSelMsg)) {
				   rst = new HashMap<String, String>();
				   rst.put("cm_dsncd", "0000");
				   rst.put("cm_dirpath", strSelMsg);
				   rsval.add(rst);
				   rst = null;
				}
				rst = new HashMap<String, String>();
				rst.put("cm_dsncd", rs.getString("cm_dsncd"));
				rst.put("cm_dirpath", rs.getString("cm_dirpath"));
				rsval.add(rst);
				rst = null;
			}
			rs.close();
			pstmt.close();
			conn.close();
			rs = null;
			pstmt = null;
			conn = null;
			returnObjectArray = rsval.toArray();
			rsval.clear();
			rsval = null;

			return returnObjectArray;
			
		} catch (SQLException sqlexception) {
			sqlexception.printStackTrace();
			ecamsLogger.error("## Cmd0600.getDir() SQLException START ##");
			ecamsLogger.error("## Error DESC : ", sqlexception);	
			ecamsLogger.error("## Cmd0600.getDir() SQLException END ##");			
			throw sqlexception;
		} catch (Exception exception) {
			exception.printStackTrace();
			ecamsLogger.error("## Cmd0600.getDir() Exception START ##");				
			ecamsLogger.error("## Error DESC : ", exception);	
			ecamsLogger.error("## Cmd0600.getDir() Exception END ##");				
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
					ecamsLogger.error("## Cmd0600.getDir() connection release exception ##");
					ex3.printStackTrace();
				}
			}
		}		
	}//end of getDir() method statement

	
    /**
     * <PRE>
     * 1. MethodName	: setRsrcInfo
     * 2. ClassName		: Cmd0600
     * 3. Commnet			: 프로그램의 종류, 경로, 업무, ISR정보를 수정
     * 4. 작성자				: no name
     * 5. 작성일				: 2011. 1. 13. 오전 8:58:26
     * </PRE>
     * 		@return int : 0(정상)
     * 		@param UserId
     * 		@param dataList : 수정 될 프로그램 리스트
     * 		@return
     * 		@throws SQLException
     * 		@throws Exception
     */
    public int setRsrcInfo(String UserId,ArrayList<HashMap<String,String>> dataList) throws SQLException, Exception {	
		Connection        conn        = null;
		PreparedStatement pstmt       = null;
		PreparedStatement pstmt2      = null;
		ResultSet         rs          = null;
		StringBuffer      strQuery    = new StringBuffer();
		int               intResult   = 0;
		String            strIsrId    = "";
		String            strIsrSub   = "";
		boolean           findSw      = false;
		
		ConnectionContext connectionContext = new ConnectionResource();
		
		try {
			
			conn = connectionContext.getConnection();
			conn.setAutoCommit(false);
			
			int i = 0;
			int paramIndex = 0;
        	intResult = 1;
			for (i=0 ; i<dataList.size() ; i++){
	            	
        	findSw = false;
    		strQuery.setLength(0);
        	strQuery.append("update cmr0020 set \n");
        	
        	if (!"".equals(dataList.get(i).get("cr_rsrccd"))){
        		strQuery.append("   cr_rsrccd = ? \n");
        		findSw = true;
        	}
        	if (!"".equals(dataList.get(i).get("cr_story"))){
        		if (findSw == true) strQuery.append(", ");
        		else findSw = true;
        		strQuery.append("   cr_story = ? \n");
        	}
        	if (!"".equals(dataList.get(i).get("cr_jobcd"))){
        		if (findSw == true) strQuery.append(", ");
        		else findSw = true;
        		strQuery.append("   cr_jobcd = ? \n");
        	}
        	if (!"".equals(dataList.get(i).get("cr_compile"))){
        		if (findSw == true) strQuery.append(", ");
        		else findSw = true;
        		strQuery.append("   cr_compile = ? \n");
        	}
        	if (!"".equals(dataList.get(i).get("cr_team"))){
        		if (findSw == true) strQuery.append(", ");
        		else findSw = true;
        		strQuery.append("   cr_teamcd = ? \n");
        	}
        	if (!"".equals(dataList.get(i).get("cr_master"))){
        		if (findSw == true) strQuery.append(", ");
        		else findSw = true;
        		strQuery.append("   cr_master = ? \n");
        	}
        	strQuery.append("   where cr_itemid=? \n");
        	strQuery.append("   and cr_syscd=? \n");
        	
        	pstmt2 = conn.prepareStatement(strQuery.toString());
   //     	pstmt2 = new LoggableStatement(conn,strQuery.toString());
        	paramIndex = 1;
        	if (!"".equals(dataList.get(i).get("cr_rsrccd"))){
        		pstmt2.setString(paramIndex++, dataList.get(i).get("cr_rsrccd"));
        	}
        	if (!"".equals(dataList.get(i).get("cr_story"))){
        		pstmt2.setString(paramIndex++, dataList.get(i).get("cr_story"));
        	}
        	if (!"".equals(dataList.get(i).get("cr_jobcd"))){
        		pstmt2.setString(paramIndex++, dataList.get(i).get("cr_jobcd"));
        	}
        	if (!"".equals(dataList.get(i).get("cr_compile"))){
        		pstmt2.setString(paramIndex++, dataList.get(i).get("cr_compile"));
        	}
        	if (!"".equals(dataList.get(i).get("cr_team"))){
        		pstmt2.setString(paramIndex++, dataList.get(i).get("cr_team"));
        	}
        	if (!"".equals(dataList.get(i).get("cr_master"))){
        		pstmt2.setString(paramIndex++, dataList.get(i).get("cr_master"));
        	}
        	pstmt2.setString(paramIndex++, dataList.get(i).get("cr_itemid"));
        	pstmt2.setString(paramIndex++, dataList.get(i).get("cr_syscd"));
        //	//ecamsLogger.error(((LoggableStatement)pstmt2).getQueryString());
      		pstmt2.executeUpdate();
      		pstmt2.close();
        	intResult = 0;
        	
        	findSw = false;
			}
			
            conn.commit();
            conn.close();
            
            rs = null;
		    pstmt = null;
		    conn = null;
		    
            return intResult;
            
		}  catch (SQLException sqlexception) {
			sqlexception.printStackTrace();
			if (rs != null)     try{rs.close();}catch (Exception ex){ex.printStackTrace();}
			if (pstmt != null)  try{pstmt.close();}catch (Exception ex2){ex2.printStackTrace();}
			if (conn != null){
				try{
					conn.rollback();
					ConnectionResource.release(conn);
				}catch(Exception ex3){
					ecamsLogger.error("## Cmd0600.setRsrcInfo() connection release exception ##");
					ex3.printStackTrace();
				}				
			}			
			ecamsLogger.error("## Cmd0600.setRsrcInfo() SQLException START ##");
			ecamsLogger.error("## Error DESC : ", sqlexception);	
			ecamsLogger.error("## Cmd0600.setRsrcInfo() SQLException END ##");			
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
					ecamsLogger.error("## Cmd0600.setRsrcInfo() connection release exception ##");
					ex3.printStackTrace();
				}				
			}			
			ecamsLogger.error("## Cmd0600.setRsrcInfo() Exception START ##");				
			ecamsLogger.error("## Error DESC : ", exception);	
			ecamsLogger.error("## Cmd0600.setRsrcInfo() Exception END ##");				
			throw exception;
		}finally{
			if (strQuery != null) 	strQuery = null;
			if (pstmt != null)  try{pstmt.close();}catch (Exception ex2){ex2.printStackTrace();}
			if (conn != null){
				try{
					ConnectionResource.release(conn);
				}catch(Exception ex3){
					ecamsLogger.error("## Cmd0600.setRsrcInfo() connection release exception ##");
					ex3.printStackTrace();
				}
			}
		}
    }//end of setRsrcInfo() method statement
    
}//end of Cmd0600 class statement
