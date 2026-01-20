/*****************************************************************************************
	1. program ID	: Cmr3200.java
	2. create date	: 2008.04.08
	3. auth		    : m.s.kang
	4. update date	: 2009.02.21
	5. auth		    : no name
	6. description	: Request List
*****************************************************************************************/

package app.eCmr;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.logging.log4j.Logger;
import com.ecams.common.dbconn.ConnectionContext;
import com.ecams.common.dbconn.ConnectionResource;
import com.ecams.common.logger.EcamsLogger;

import java.util.ArrayList;
import java.util.HashMap;

import app.common.LoggableStatement;
import app.common.SystemPath;


/**
 * @author bigeyes
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
 

public class Cmr2400{
    /**
     * Logger Class Instance Creation
     * logger
     */
    Logger ecamsLogger  = EcamsLogger.getLoggerInstance();

	/**
	 * 요청현황을 조회합니다.
	 * @param  syscd,reqcd,teamcd,sta,requser,startdt,enddt,userid
	 * @return json
	 * @throws SQLException	
	 * @throws Exception 
	 */
    public Object[] get_SelectList(String pSysCd,String pJobCd,String pStateCd,String pStartDt,String pEndDt,String pReqUser,String ResultCk,String pUserId) throws SQLException, Exception {
		Connection        conn        = null;
		PreparedStatement pstmt       = null;
		PreparedStatement pstmt2      = null;
		//PreparedStatement pstmt3      = null;
		ResultSet         rs          = null;
		ResultSet         rs2         = null;
		//ResultSet         rs3         = null;
		StringBuffer      strQuery    = new StringBuffer();
		Object[] 		  returnObject= null;
		ArrayList<HashMap<String, String>>  rtList	= new ArrayList<HashMap<String, String>>();
		HashMap<String, String>			  	rst	 	= null;

		ConnectionContext connectionContext = new ConnectionResource();

		try {

			rtList.clear();
	        Integer           Cnt         = 0;
	        
			conn = connectionContext.getConnection();
			
			strQuery.setLength(0);
			strQuery.append("Select a.*,c.cm_sysmsg,e.cm_username,f.cm_codename sta, g.cm_codename sin, \n");
			strQuery.append("       substr(a.cr_acptno,1,4) || '-' || substr(a.cr_acptno,5,2) || '-' || substr(a.cr_acptno,7,6) as acptno	\n");
			strQuery.append("  from cmm0020 g,cmm0030 c,cmm0020 f,cmm0040 e,cmr9900 d,cmr1000 a    		\n");
			strQuery.append(" where a.cr_status='0' and d.cr_locat='00'									\n");
			strQuery.append("   and to_char(a.cr_acptdate,'yyyymmdd')>=?                     			\n");
			strQuery.append("   and to_char(a.cr_acptdate,'yyyymmdd')<=?                    			\n");
			if("0".equals(ResultCk)){
				strQuery.append("and rtrim(d.cr_team) = 'SYSED' 										\n");	//배포처리
			}else if("1".equals(ResultCk)){
				strQuery.append("and rtrim(d.cr_team) = 'SYSTS' 										\n");	//검증적용
			}
			strQuery.append("   and a.cr_acptno=d.cr_acptno 											\n");
			if(!"".equals(pSysCd) && pSysCd != null){
				strQuery.append("and a.cr_syscd=?  														\n");
			}
			if(!"".equals(pReqUser) && pReqUser!=null){
				strQuery.append("and a.cr_editor in(select cm_userid from cmm0040 where cm_username= ?) \n");
			} 
			strQuery.append("and a.cr_syscd=c.cm_syscd  												\n");
			strQuery.append("and f.cm_macode='CMR1000' and a.cr_status = f.cm_micode  					\n");
			strQuery.append("and g.cm_macode='REQUEST' and a.cr_qrycd = g.cm_micode  					\n");
			strQuery.append("and a.cr_editor=e.cm_userid 												\n");
			strQuery.append("order by a.cr_acptdate desc 												\n");

//			pstmt = conn.prepareStatement(strQuery.toString());
			pstmt = new LoggableStatement(conn,strQuery.toString());
            Cnt = 0;
            
            pstmt.setString(++Cnt, pStartDt);
            pstmt.setString(++Cnt, pEndDt);
            if (!"".equals(pSysCd) && pSysCd != null) {
            	pstmt.setString(++Cnt, pSysCd);
            }
			if (!"".equals(pReqUser) && pReqUser != null){
	           pstmt.setString(++Cnt, pReqUser);
			}

			ecamsLogger.error(((LoggableStatement)pstmt).getQueryString());
            rs = pstmt.executeQuery();

			while (rs.next()){
				rst = new HashMap<String, String>();
				rst.put("Sysmsg",  	rs.getString("cm_sysmsg"));
				rst.put("cr_acptno",rs.getString("cr_acptno"));
				rst.put("acptno",  	rs.getString("acptno"));
				rst.put("acptdate", rs.getString("cr_acptdate"));
				rst.put("username", rs.getString("cm_username"));
				rst.put("sta",  	rs.getString("sta"));
				rst.put("prcdate",  rs.getString("cr_prcdate"));
				rst.put("sayu",  	rs.getString("cr_sayu"));
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

			returnObject = rtList.toArray();
			rtList.clear();
			rtList = null;

			return returnObject;

		} catch (SQLException sqlexception) {
			sqlexception.printStackTrace();
			ecamsLogger.error("## Cmr3200.SelectList() SQLException START ##");
			ecamsLogger.error("## Error DESC : ", sqlexception);
			ecamsLogger.error("## Cmr3200.SelectList() SQLException END ##");
			throw sqlexception;
		} catch (Exception exception) {
			exception.printStackTrace();
			ecamsLogger.error("## Cmr3200.SelectList() Exception START ##");
			ecamsLogger.error("## Error DESC : ", exception);
			ecamsLogger.error("## Cmr3200.SelectList() Exception END ##");
			throw exception;
		}finally{
			if (strQuery != null) 	strQuery = null;
			if (returnObject != null)	returnObject = null;
			if (rs != null)     try{rs.close();}catch (Exception ex){ex.printStackTrace();}
			if (rs2 != null)    try{rs2.close();}catch (Exception ex){ex.printStackTrace();}
			if (pstmt != null)  try{pstmt.close();}catch (Exception ex2){ex2.printStackTrace();}
			if (pstmt2 != null)  try{pstmt2.close();}catch (Exception ex2){ex2.printStackTrace();}
			if (conn != null){
				try{
					conn.close();
				}catch(Exception ex3){
					ecamsLogger.error("## Cmr3200.SelectList() connection release exception ##");
					ex3.printStackTrace();
				}
			}
		}
	}//end of SelectList() method statement


    public String reqCncl(String AcptNo,String UserId,String conMsg,String ConfUsr) throws SQLException, Exception {
		Connection        conn        = null;
		PreparedStatement pstmt       = null;
		PreparedStatement pstmt2      = null;
		ResultSet         rs          = null;
		StringBuffer      strQuery    = new StringBuffer();
		Runtime  run = null;
		Process p = null;
		SystemPath		  systemPath = new SystemPath();
		String  binpath;
		String[] chkAry;
		int		cmdrtn;

		ConnectionContext connectionContext = new ConnectionResource();

		try {
			//ecamsLogger.debug("+++++++++prcCd========"+prcCd);
			//binpath = systemPath.getTmpDir("14");
			conn = connectionContext.getConnection();

			binpath = systemPath.getTmpDir_conn("14",conn);

			run = Runtime.getRuntime();

			chkAry = new String[4];
			chkAry[0] = "/bin/sh";
			chkAry[1] = binpath+"/procck2";
			chkAry[2] = "ecams_acct";
			chkAry[3] = AcptNo;


			p = run.exec(chkAry);
			p.waitFor();

			cmdrtn = p.exitValue();
			if (cmdrtn > 1) {
	        	pstmt = null;
	        	conn = null;

				//ecamsLogger.error(chkAry[0]+" " + chkAry[1] + " " + chkAry[2]);
				//ecamsLogger.error(cmdrtn);
				return "2";
			}


        	strQuery.setLength(0);
        	strQuery.append("Begin CMR9900_STR ( ");
        	strQuery.append("?, ?, ?, '9', ?, '9' ); End;");
        	pstmt = conn.prepareStatement(strQuery.toString());
        	pstmt.setString(1, AcptNo);
        	pstmt.setString(2,UserId);
        	pstmt.setString(3,conMsg);
        	pstmt.setString(4,ConfUsr);

        	pstmt.executeUpdate();
        	pstmt.close();
        	
        	strQuery.setLength(0);
    		strQuery.append("select a.cr_acptno,a.cr_confusr      \n");
    		strQuery.append("  from (Select cr_acptno             \n");        
    		strQuery.append("          from cmr1030               \n");
    		strQuery.append("          start with cr_befact=?     \n");
    		strQuery.append("        connect by prior             \n");
    		strQuery.append("             cr_acptno=cr_befact) b, \n");
    		strQuery.append("  cmr9900 a                          \n");
    		strQuery.append(" where b.cr_acptno=a.cr_acptno       \n");
    		strQuery.append("   and a.cr_locat='00'               \n");
    		strQuery.append("   and a.cr_status='0'               \n");
    		pstmt2 = conn.prepareStatement(strQuery.toString());
    		pstmt2.setString(1, AcptNo);
    		rs = pstmt2.executeQuery();
    		while (rs.next()) {
    			strQuery.setLength(0);
            	strQuery.append("Begin CMR9900_STR ( ");
            	strQuery.append("?, ?, ?, '9', ?, ? ); End;");
            	pstmt = conn.prepareStatement(strQuery.toString());
            	pstmt.setString(1, rs.getString("cr_acptno"));
            	pstmt.setString(2,UserId);
            	pstmt.setString(3,"선행작업 회수로 인한 자동반려처리");
            	pstmt.setString(4,rs.getString("cr_confusr"));               			
            	pstmt.setString(5,"9");
            	
            	pstmt.executeUpdate();
            	pstmt.close();
    		}
    		rs.close();
    		pstmt2.close();
    		
        	conn.close();
        	pstmt = null;
        	conn = null;

        	return "0";

		} catch (SQLException sqlexception) {
			sqlexception.printStackTrace();
			ecamsLogger.error("## Cmr3200.reqCncl() SQLException START ##");
			ecamsLogger.error("## Error DESC : ", sqlexception);
			ecamsLogger.error("## Cmr3200.reqCncl() SQLException END ##");
			throw sqlexception;
		} catch (Exception exception) {
			exception.printStackTrace();
			ecamsLogger.error("## Cmr3200.reqCncl() Exception START ##");
			ecamsLogger.error("## Error DESC : ", exception);
			ecamsLogger.error("## Cmr3200.reqCncl() Exception END ##");
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
					ecamsLogger.error("## Cmr3200.reqCncl() connection release exception ##");
					ex3.printStackTrace();
				}
			}
		}
	}//end of reqCncl() method statement


    public String cnclYn(String AcptNo) throws SQLException, Exception {
		Connection        conn        = null;
		PreparedStatement pstmt       = null;
		PreparedStatement pstmt2      = null;
		ResultSet         rs          = null;
		ResultSet         rs2         = null;
		StringBuffer      strQuery    = new StringBuffer();
		String            retUser     = "";

		ConnectionContext connectionContext = new ConnectionResource();

		try {

			conn = connectionContext.getConnection();

        	strQuery.setLength(0);
        	strQuery.append("select count(*) as cnt from cmr9900          \n");
        	strQuery.append("where cr_acptno=? and cr_locat<>'00'         \n");
        	strQuery.append("  and cr_status='9' and cr_teamcd<>'2'       \n");
        	strQuery.append("  and cr_team not in ('SYSFT','SYSPF','SYSUP','SYSCB') \n");
        	//pstmt =  new LoggableStatement(conn, strQuery.toString());
        	pstmt = conn.prepareStatement(strQuery.toString());
        	pstmt.setString(1, AcptNo);
        	//ecamsLogger.debug(((LoggableStatement)pstmt).getQueryString());
        	rs = pstmt.executeQuery();

        	if (rs.next()){
        		if (rs.getInt("cnt") == 0) {

        			strQuery.setLength(0);
        			strQuery.append("select cr_confusr from cmr9900           \n");
        			strQuery.append(" where cr_acptno=? and cr_locat='00'     \n");
        			strQuery.append("   and cr_status='0'                     \n");
        			//pstmt =  new LoggableStatement(conn, strQuery.toString());
        			pstmt2 = conn.prepareStatement(strQuery.toString());
                	pstmt2.setString(1, AcptNo);
                	//ecamsLogger.debug(((LoggableStatement)pstmt).getQueryString());
                	rs2 = pstmt2.executeQuery();
                	if (rs2.next()) {
                		retUser = rs2.getString("cr_confusr");
                	}
                	rs2.close();
                	pstmt2.close();
                	rs2 = null;
                	pstmt2 = null;
        		}
        	}
        	rs.close();
        	pstmt.close();
        	conn.close();
        	
        	rs = null;
        	pstmt = null;
        	conn = null;

        	return retUser;


		} catch (SQLException sqlexception) {
			sqlexception.printStackTrace();
			ecamsLogger.error("## Cmr3200.cnclYn() SQLException START ##");
			ecamsLogger.error("## Error DESC : ", sqlexception);
			ecamsLogger.error("## Cmr3200.cnclYn() SQLException END ##");
			throw sqlexception;
		} catch (Exception exception) {
			exception.printStackTrace();
			ecamsLogger.error("## Cmr3200.cnclYn() Exception START ##");
			ecamsLogger.error("## Error DESC : ", exception);
			ecamsLogger.error("## Cmr3200.cnclYn() Exception END ##");
			throw exception;
		}finally{
			if (strQuery != null) 	strQuery = null;
			if (rs != null)     try{rs.close();}catch (Exception ex){ex.printStackTrace();}
			if (rs2 != null)    try{rs2.close();}catch (Exception ex){ex.printStackTrace();}
			if (pstmt != null)  try{pstmt.close();}catch (Exception ex2){ex2.printStackTrace();}
			if (conn != null){
				try{
					conn.close();
				}catch(Exception ex3){
					ecamsLogger.error("## Cmr3200.cnclYn() connection release exception ##");
					ex3.printStackTrace();
				}
			}
		}
	}//end of cnclYn() method statement

    public String reqDelete(String AcptNo,String UserId,String QryCd,String ConfUsr) throws SQLException, Exception {
		Connection        conn        = null;
		PreparedStatement pstmt       = null;
		StringBuffer      strQuery    = new StringBuffer();

		ConnectionContext connectionContext = new ConnectionResource();

		try {

			conn = connectionContext.getConnection();
			conn.setAutoCommit(false);
        	strQuery.setLength(0);
        	strQuery.append("Begin CMR9900_STR ( ");
        	strQuery.append("?, ?, '삭제처리를 위한 사전작업', '9', ?, '9' ); End;");
        	pstmt = conn.prepareStatement(strQuery.toString());
        	pstmt.setString(1, AcptNo);
        	pstmt.setString(2,UserId);
        	pstmt.setString(3,ConfUsr);
        	pstmt.executeUpdate();
        	pstmt.close();

        	strQuery.setLength(0);
        	strQuery.append("delete cmr9920 where cr_acptno=?              \n");
        	pstmt = conn.prepareStatement(strQuery.toString());
        	pstmt.setString(1, AcptNo);
        	pstmt.executeUpdate();
        	pstmt.close();

        	strQuery.setLength(0);
        	strQuery.append("delete cmr9910 where cr_acptno=?              \n");
        	pstmt = conn.prepareStatement(strQuery.toString());
        	pstmt.setString(1, AcptNo);
        	pstmt.executeUpdate();
        	pstmt.close();

        	strQuery.setLength(0);
        	strQuery.append("delete cmr9900 where cr_acptno=?              \n");
        	pstmt = conn.prepareStatement(strQuery.toString());
        	pstmt.setString(1, AcptNo);
        	pstmt.executeUpdate();
        	pstmt.close();

        	strQuery.setLength(0);
        	strQuery.append("delete cmr1001 where cr_acptno=?              \n");
        	pstmt = conn.prepareStatement(strQuery.toString());
        	pstmt.setString(1, AcptNo);
        	pstmt.executeUpdate();
        	pstmt.close();

        	if (QryCd.substring(0,1).equals("3")) {
        		strQuery.setLength(0);
            	strQuery.append("delete cmr1100 where cr_acptno=?              \n");
            	pstmt = conn.prepareStatement(strQuery.toString());
            	pstmt.setString(1, AcptNo);
            	pstmt.executeUpdate();
            	pstmt.close();

        		strQuery.setLength(0);
            	strQuery.append("delete cmr1002 where cr_acptno=?              \n");
            	pstmt = conn.prepareStatement(strQuery.toString());
            	pstmt.setString(1, AcptNo);
            	pstmt.executeUpdate();
            	pstmt.close();
        	} else {
        		strQuery.setLength(0);
	        	strQuery.append("delete cmr1011 where cr_acptno=?              \n");
	        	pstmt = conn.prepareStatement(strQuery.toString());
	        	pstmt.setString(1, AcptNo);
	        	pstmt.executeUpdate();
	        	pstmt.close();

        		strQuery.setLength(0);
	        	strQuery.append("delete cmr1030 where cr_acptno=?              \n");
	        	pstmt = conn.prepareStatement(strQuery.toString());
	        	pstmt.setString(1, AcptNo);
	        	pstmt.executeUpdate();
	        	pstmt.close();

        		strQuery.setLength(0);
	        	strQuery.append("delete cmr1030 where cr_befact=?              \n");
	        	pstmt = conn.prepareStatement(strQuery.toString());
	        	pstmt.setString(1, AcptNo);
	        	pstmt.executeUpdate();
	        	pstmt.close();

        		strQuery.setLength(0);
	        	strQuery.append("delete cmr0027 where cr_acptno=?              \n");
	        	pstmt = conn.prepareStatement(strQuery.toString());
	        	pstmt.setString(1, AcptNo);
	        	pstmt.executeUpdate();
	        	pstmt.close();

        		strQuery.setLength(0);
	        	strQuery.append("delete cmr1010 where cr_acptno=?              \n");
	        	pstmt = conn.prepareStatement(strQuery.toString());
	        	pstmt.setString(1, AcptNo);
	        	pstmt.executeUpdate();
	        	pstmt.close();
        	}
        	strQuery.setLength(0);
        	strQuery.append("delete cmr1000 where cr_acptno=?              \n");
        	pstmt = conn.prepareStatement(strQuery.toString());
        	pstmt.setString(1, AcptNo);
        	pstmt.executeUpdate();
        	pstmt.close();
        	conn.commit();
        	conn.close();

        	pstmt = null;
        	conn = null;

        	return "0";

		} catch (SQLException sqlexception) {
			sqlexception.printStackTrace();
			ecamsLogger.error("## Cmr3200.reqDelete() SQLException START ##");
			ecamsLogger.error("## Error DESC : ", sqlexception);
			ecamsLogger.error("## Cmr3200.reqDelete() SQLException END ##");
			if (conn != null) conn.rollback();
			throw sqlexception;
		} catch (Exception exception) {
			exception.printStackTrace();
			ecamsLogger.error("## Cmr3200.reqDelete() Exception START ##");
			ecamsLogger.error("## Error DESC : ", exception);
			ecamsLogger.error("## Cmr3200.reqDelete() Exception END ##");
			if (conn != null) conn.rollback();
			throw exception;
		}finally{
			if (strQuery != null) 	strQuery = null;
			if (pstmt != null)  try{pstmt.close();}catch (Exception ex2){ex2.printStackTrace();}
			if (conn != null){
				try{
					conn.close();
				}catch(Exception ex3){
					ecamsLogger.error("## Cmr3200.reqDelete() connection release exception ##");
					ex3.printStackTrace();
				}
			}
		}
	}//end of reqDelete() method statement

}//end of Cmr3200 class statement
