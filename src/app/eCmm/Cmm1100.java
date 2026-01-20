/*****************************************************************************************
	1. program ID	: eCmm1100.java
	2. create date	: 
	3. auth		    : 
	4. update date	: 090605
	5. auth		    : no name
	6. description	: 기본관리 -> 부재등록 eCmm1100 
*****************************************************************************************/

package app.eCmm;

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
import app.common.UserInfo;

import java.util.*;
/**
 * @author bigeyes
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */

public class Cmm1100{
	 
    /**
     * Logger Class Instance Creation
     * logger
     */
    Logger ecamsLogger  = EcamsLogger.getLoggerInstance();
/*    {
    	Connection        conn        = null;
		PreparedStatement pstmt       = null;
		ResultSet         rs          = null;
		StringBuffer      strQuery    = new StringBuffer();
		//ArrayList<HashMap<String, String>>	rtList  = new ArrayList<HashMap<String, String>>();
		//HashMap<String, String>			  	rst	 	= null;
        boolean date = false;
        
		ConnectionContext connectionContext = new ConnectionResource();
		try{
			conn = connectionContext.getConnection();
			
			strQuery.setLength(0);
			//cm_blkstdate = to_char(to_date( ? , 'yyyy/mm/dd'),'yyyymmdd')
			strQuery.append("select to_char(cm_blkstdate , 'yyyy/mm/dd')as cm_blkstdate, to_char(cm_blkeddate , 'yyyy/mm/dd')as cm_blkeddate from cmm0042 where cm_userid=?");
			pstmt = conn.prepareStatement(strQuery.toString());
			pstmt = new LoggableStatement(conn, strQuery.toString());
			pstmt.setString(1,objDate.get("Frm_user"));
			
			
			rs = pstmt.executeQuery();
			
			while (rs.next()) {
				//rst = new HashMap<String,String>();
				if (objDate.get("sdate") != rs.getString("cm_blkstdate")&& objDate.get("edate") != rs.getString("cm_blkeddate")){
					date = true;
				}else{
					date =false;
				}
				
				//rst = null;
////////////////////////////////////////////////////////////
				System.out.println("############# getDate ##################");
				System.out.println(objDate.get("sdate"));
				System.out.println(rs.getString("cm_blkstdate"));
				System.out.println("############# getDate ##################");
			}
			
			ecamsLogger.error(((LoggableStatement)pstmt).getQueryString());
            rs.close();
            pstmt.close();
            conn.close();
            rs = null;
            pstmt = null;
            conn = null;
            
    		
    		
    		return date;
		}catch (SQLException sqlexception) {
			sqlexception.printStackTrace();
			ecamsLogger.error("## Cmm1100.getDate() SQLException START ##");
			ecamsLogger.error("## Error DESC : ", sqlexception);	
			ecamsLogger.error("## Cmm1100.getDate() SQLException END ##");			
			throw sqlexception;
		} catch (Exception exception) {
			exception.printStackTrace();
			ecamsLogger.error("## Cmm1100.getDate() Exception START ##");				
			ecamsLogger.error("## Error DESC : ", exception);	
			ecamsLogger.error("## Cmm1100.getDate() Exception END ##");				
			throw exception;
		}finally{
			if (strQuery != null) 	strQuery = null;

			if (rs != null)     try{rs.close();}catch (Exception ex){ex.printStackTrace();}
			if (pstmt != null)  try{pstmt.close();}catch (Exception ex2){ex2.printStackTrace();}
			if (conn != null){
				try{
					connectionContext.release();
				}catch(Exception ex3){
					ecamsLogger.error("## Cmm1100.getDate() connection release exception ##");
					ex3.printStackTrace();
				}
			}
		}
	}
 */   
	public Object[] get_grid_select(String user_id) throws SQLException, Exception
	{
		Connection        conn        = null;
		PreparedStatement pstmt       = null;
		ResultSet         rs          = null;
		StringBuffer      strQuery    = new StringBuffer();
		ArrayList<HashMap<String, String>>  rsList	= new ArrayList<HashMap<String, String>>();
		HashMap<String, String>			  	rst	 	= null;
        Object[]		 rtnObj		  = null;
		ConnectionContext connectionContext = new ConnectionResource();
		
		try {
			
			conn = connectionContext.getConnection();
			strQuery.append(" select cm_userid,cm_username,cm_blankdts,cm_blankdte	\n");
			//strQuery.append(" from cmm0040 where cm_status='9'						\n");
			strQuery.append(" from cmm0040 where  cm_daegyul= ?						\n");//대결지정자사번
			strQuery.append(" and cm_blankdts is not null							\n");
//			strQuery.append(" and cm_blankdts>=to_date(SYSDATE, 'yyyy/mm/dd')		\n");
			strQuery.append(" and cm_blankdts<=to_char(SYSDATE, 'yyyymmdd')		\n");
			strQuery.append(" and cm_blankdte>=to_char(SYSDATE, 'yyyymmdd')		\n");
			
			pstmt = conn.prepareStatement(strQuery.toString());
			//pstmt =  new LoggableStatement(conn, strQuery.toString());
            pstmt.setString(1, user_id);
            rs = pstmt.executeQuery();
            strQuery.setLength(0);
            
			if (rs.next()) {
				rst = new HashMap<String,String>();
				rst.put("cm_userid", rs.getString("cm_userid"));
				rst.put("cm_username", rs.getString("cm_username"));
				rst.put("cm_blankdts", rs.getString("cm_blankdts"));
				rst.put("cm_blankdte", rs.getString("cm_blankdte"));
				rst.put("sedate", rs.getString("cm_blankdts")+ " - " +rs.getString("cm_blankdte"));
				rsList.add(rst);
				rst = null;
			}
			rs.close();
			pstmt.close();
			conn.close();
			rs = null;
			pstmt = null;
			conn = null;
			
			rtnObj = rsList.toArray();
			rsList.clear();
			rsList = null;
			
			return rtnObj;
			
		} catch (SQLException sqlexception) {
			sqlexception.printStackTrace();
			ecamsLogger.error("## Cmm1100.get_grid_select() SQLException START ##");
			ecamsLogger.error("## Error DESC : ", sqlexception);	
			ecamsLogger.error("## Cmm1100.get_grid_select() SQLException END ##");			
			throw sqlexception;
		} catch (Exception exception) {
			exception.printStackTrace();
			ecamsLogger.error("## Cmm1100.get_grid_select() Exception START ##");				
			ecamsLogger.error("## Error DESC : ", exception);	
			ecamsLogger.error("## Cmm1100.get_grid_select() Exception END ##");				
			throw exception;
		}finally{
			if (strQuery != null) 	strQuery = null;
			if (rtnObj != null)	rtnObj = null;
			if (rs != null)     try{rs.close();}catch (Exception ex){ex.printStackTrace();}
			if (pstmt != null)  try{pstmt.close();}catch (Exception ex2){ex2.printStackTrace();}
			if (conn != null){
				try{
					connectionContext.release();
				}catch(Exception ex3){
					ecamsLogger.error("## Cmm1100.get_grid_select() connection release exception ##");
					ex3.printStackTrace();
				}
			}
		}
	}
	
	
//	public int get_Update(String Frm_User, String DaeSign,String Cbo_Sayu,String Txt_Sayu, String sdate,String edate, String Opt_cd0) throws SQLException, Exception
	public Object[] get_Update(HashMap<String,String> dataObj) throws SQLException, Exception {
		Connection        conn        = null;
		PreparedStatement pstmt       = null;
		PreparedStatement pstmt2 		= null;
		PreparedStatement pstmt3 		= null;
		ResultSet rs = null;
		ResultSet rs2 = null;
		StringBuffer      strQuery    = new StringBuffer();
		Object[]		  rtObj		  = null;
		HashMap<String, String>			  	rst	 	= null;
		ArrayList<HashMap<String, String>>	rtList  = new ArrayList<HashMap<String, String>>();
		String               rtn_cnt     = "0";
		String				 rtn_overlap = "1"; //등록하려는 날짜가 DB에 이미 등록되어있는지 판단. 0:중복 , 1:중복아님, 4:rs에 값 없을시(테스트) , 5:테스트
		ConnectionContext connectionContext = new ConnectionResource();
		
		try {	
			conn = connectionContext.getConnection();
			
			//################ cmm0042 의 cm_blkseq (=일련번호)  값 증가 시키기 위해 #################
			int cmm0042_count = 1;
			strQuery.setLength(0);
			strQuery.append("select cm_blkseq from cmm0042 \n");
			strQuery.append("where cm_userid=?");
			strQuery.append("order by cm_blkseq desc");

			pstmt = conn.prepareStatement(strQuery.toString());
			pstmt.setString(1, dataObj.get("Frm_User"));
			rs = pstmt.executeQuery();
			
			
			if(rs.next()){
				cmm0042_count = rs.getInt("cm_blkseq") + 1;
			}
			rs.close();
			pstmt.close();
			


			//#######################################################################
			strQuery.setLength(0);
			
			if (dataObj.get("Opt_Cd0").equals("true")) {
					String sdate = dataObj.get("sdate").replaceAll("/", "");
					String edate = dataObj.get("edate").replaceAll("/", "");
					
					strQuery.setLength(0);
					strQuery.append("select to_char(cm_blkstdate , 'yyyy/mm/dd')as blkstdate , to_char(cm_blkeddate , 'yyyy/mm/dd')as blkeddate \n" );
					strQuery.append("from cmm0042 where cm_userid=? \n");
					strQuery.append("and (to_char(cm_blkstdate,'yyyymmdd') >= ? and to_char(cm_blkstdate,'yyyymmdd') <= ? \n");
					strQuery.append("or to_char(cm_blkstdate,'yyyymmdd') <= ? and to_char(cm_blkeddate,'yyyymmdd') >= ?) \n");
					pstmt3 = conn.prepareStatement(strQuery.toString());
					pstmt3.setString(1, dataObj.get("Frm_User"));
					pstmt3.setString(2, sdate);
					pstmt3.setString(3, edate);
					pstmt3.setString(4, sdate);
					pstmt3.setString(5, sdate);
					rs2 = pstmt3.executeQuery();
					if(rs2.next()){
						rtn_overlap = "0";
					}else{
						rtn_overlap = "1";
					}
					
					if(rtn_overlap.equals("1")){
						strQuery.setLength(0);
						
						strQuery.append("insert into cmm0042 (cm_userid , cm_blkseq , cm_daeusr , cm_blankcd , cm_blkcont , cm_blkstdate , cm_blkeddate ) \n");
						strQuery.append(" values ( ");
						strQuery.append(" ? , "); // cm_userid
						strQuery.append(" ? , "); // cm_blkseq
						strQuery.append(" ? , "); //DaeSign
			           	strQuery.append(" ? , "); //Cbo_Sayu
			           	strQuery.append(" ? , ");  //Txt_Sayu
			           	strQuery.append("to_date( ? , 'yyyy/mm/dd'), ");
			           	strQuery.append("to_date( ? , 'yyyy/mm/dd')	");
			           	strQuery.append(" )"); //values end
			           	
			           	//int CNT = 0;
						
						
						pstmt2 = conn.prepareStatement(strQuery.toString());
			           	pstmt2.setString(1, dataObj.get("Frm_User"));
						pstmt2.setInt(2, cmm0042_count);
						pstmt2.setString(3, dataObj.get("DaeSign"));
						pstmt2.setString(4, dataObj.get("Cbo_Sayu"));
						pstmt2.setString(5, dataObj.get("Txt_Sayu"));
						pstmt2.setString(6, dataObj.get("sdate"));
						pstmt2.setString(7, dataObj.get("edate")); 
			           	
						pstmt2.executeUpdate();
						pstmt2.close();
					}				
			}
			else{ //Opt_Cd0 이 false 일 경우
				rtn_overlap = "rtn_overlap=delete (test)";
				strQuery.append("delete from cmm0042 where cm_userid = ? and cm_blkseq = ?");
				pstmt = conn.prepareStatement(strQuery.toString());
				pstmt.setString(1, dataObj.get("Frm_User"));
				pstmt.setString(2, dataObj.get("blkseq"));
				pstmt.executeUpdate();
				pstmt.close();
	        }
			//ecamsLogger.error(((LoggableStatement)pstmt).getQueryString()); 
			
			
			
			 //pstmt = conn.prepareStatement(strQuery.toString());
			 //pstmt =  new LoggableStatement(conn, strQuery.toString());
			if (dataObj.get("Opt_Cd0").equals("true")) {
				strQuery.setLength(0);
				strQuery.append("update cmr9900       \n");
				strQuery.append("   set cr_team=?     \n");
				strQuery.append(" where cr_status='0' \n");
				strQuery.append("   and cr_team=?     \n");
				pstmt = conn.prepareStatement(strQuery.toString());
				pstmt.setString(1, dataObj.get("DaeSign"));
				pstmt.setString(2,dataObj.get("Frm_User"));
				pstmt.executeUpdate();
			} else {
				strQuery.setLength(0);
				strQuery.append("update cmr9900              \n");
				strQuery.append("   set cr_team=?            \n");
				strQuery.append(" where cr_status='0'        \n");
				strQuery.append("   and cr_baseusr=?         \n");
				strQuery.append("   and cr_baseusr<>cr_team  \n");
				pstmt = conn.prepareStatement(strQuery.toString());
				pstmt.setString(1,dataObj.get("Frm_User"));
				pstmt.setString(2,dataObj.get("Frm_User"));
				pstmt.executeUpdate();
			}
			conn.commit();
			rs.close();
			pstmt.close();
			conn.close();
			rs = null;
			pstmt2 = null;
			pstmt = null;
			conn = null;
			
			
			rtn_cnt = "2";
			if (dataObj.get("Opt_Cd0").equals("true")) {
				rtn_cnt = "1";
			}
			//dataObj.clear();
			//dataObj = null;
			
			rst = new HashMap<String,String>();
			rst.put("rtn_cnt", rtn_cnt);
			rst.put("rtn_overlap", rtn_overlap);
			rtList.add(rst);
			rtObj = rtList.toArray();
			rtList.clear();
    		rtList = null;
			
			return rtObj;
			
			
		} catch (SQLException sqlexception) {
			sqlexception.printStackTrace();
			ecamsLogger.error("## Cmm1100.get_Update() SQLException START ##");
			ecamsLogger.error("## Error DESC : ", sqlexception);	
			ecamsLogger.error("## Cmm1100.get_Update() SQLException END ##");			
			throw sqlexception;
		} catch (Exception exception) {
			exception.printStackTrace();
			ecamsLogger.error("## Cmm1100.get_Update() Exception START ##");
			ecamsLogger.error("## Error DESC : ", exception);	
			ecamsLogger.error("## Cmm1100.get_Update() Exception END ##");				
			throw exception;
		}finally{
			if (strQuery != null) 	strQuery = null;
			if (rs != null)  try{rs.close();}catch (Exception ex2){ex2.printStackTrace();}
			if (pstmt != null)  try{pstmt.close();}catch (Exception ex2){ex2.printStackTrace();}
			if (pstmt2 != null)  try{pstmt2.close();}catch (Exception ex2){ex2.printStackTrace();}
			if (conn != null){
				try{
					connectionContext.release();
				}catch(Exception ex3){
					ecamsLogger.error("## Cmm1100.get_Update() connection release exception ##");
					ex3.printStackTrace();
				}
			}
		}
	}
	
	public String Check_Confirm(String UserId,boolean daeSw) throws SQLException, Exception {
		Connection        conn        = null;
		ResultSet         rs          = null;
		PreparedStatement pstmt       = null;
		StringBuffer      strQuery    = new StringBuffer();
		String               rtnMsg   = "OK";
		ConnectionContext connectionContext = new ConnectionResource();
		
		try {
			conn = connectionContext.getConnection();
			
			if (daeSw == true) {
				strQuery.setLength(0);
				strQuery.append("select GYULCNT(?) msg from dual \n");
				pstmt = conn.prepareStatement(strQuery.toString());
				pstmt.setString(1, UserId);
				rs = pstmt.executeQuery();
				if (rs.next()) {
					rtnMsg = rs.getString("msg");
				}
				rs.close();
				pstmt.close();
			}
			conn.close();
			
			rs = null;
			pstmt = null;
			conn = null;
			return rtnMsg;
			
			
		} catch (SQLException sqlexception) {
			sqlexception.printStackTrace();
			ecamsLogger.error("## Cmm1100.Check_Confirm() SQLException START ##");
			ecamsLogger.error("## Error DESC : ", sqlexception);	
			ecamsLogger.error("## Cmm1100.Check_Confirm() SQLException END ##");			
			throw sqlexception;
		} catch (Exception exception) {
			exception.printStackTrace();
			ecamsLogger.error("## Cmm1100.Check_Confirm() Exception START ##");
			ecamsLogger.error("## Error DESC : ", exception);	
			ecamsLogger.error("## Cmm1100.Check_Confirm() Exception END ##");				
			throw exception;
		}finally{
			if (strQuery != null) 	strQuery = null;
			if (rs != null) 	try{rs.close();}catch (Exception ex2){ex2.printStackTrace();}
			if (pstmt != null)  try{pstmt.close();}catch (Exception ex2){ex2.printStackTrace();}
			if (conn != null){
				try{
					connectionContext.release();
				}catch(Exception ex3){
					ecamsLogger.error("## Cmm1100.Check_Confirm() connection release exception ##");
					ex3.printStackTrace();
				}
			}
		}
	}
	public Object[] getCbo_User(String UserId,String Sv_Admin) throws SQLException, Exception {
		Connection        conn        = null;
		PreparedStatement pstmt       = null;
		StringBuffer      strQuery    = new StringBuffer();
		ResultSet         rs          = null;
		Object[]		  rtObj		  = null;
		ArrayList<HashMap<String, String>>	rtList  = new ArrayList<HashMap<String, String>>();
		HashMap<String, String>			  	rst	 	= null;
        
		ConnectionContext connectionContext = new ConnectionResource();
		
		try {
			conn = connectionContext.getConnection();
			
			strQuery.setLength(0);
			strQuery.append("select cm_userid,cm_username   \n");
			strQuery.append("  from cmm0040                 \n");
		    if (!Sv_Admin.equals("Y")) {
		    	strQuery.append(" where cm_userid=?         \n");	//UserId
		    } else {
		    	strQuery.append(" where cm_active='1'       \n");	//UserId
		    }
		    strQuery.append(" order by cm_username \n");
			pstmt = conn.prepareStatement(strQuery.toString());
			if (!Sv_Admin.equals("Y")) pstmt.setString(1, UserId);
            rs = pstmt.executeQuery();            
            rtList.clear();
            
            
            while (rs.next()) {
				rst = new HashMap<String,String>();
				//rst.put("ID", "Cbo_User");
				rst.put("cm_userid", rs.getString("cm_userid"));
				rst.put("cm_username", rs.getString("cm_username"));
				rst.put("username", rs.getString("cm_username") + "  [" + rs.getString("cm_userid") + "]");
				rtList.add(rst);
				rst = null;
			}
            rs.close();
            pstmt.close();
            conn.close();
            rs = null;
            pstmt = null;
            conn = null;
            
    		rtObj =  rtList.toArray();
    		rtList.clear();
    		rtList = null;
    		
    		return rtObj;
			
		} catch (SQLException sqlexception) {
			sqlexception.printStackTrace();
			ecamsLogger.error("## Cmm1100.get_Update() SQLException START ##");
			ecamsLogger.error("## Error DESC : ", sqlexception);	
			ecamsLogger.error("## Cmm1100.get_Update() SQLException END ##");			
			throw sqlexception;
		} catch (Exception exception) {
			exception.printStackTrace();
			ecamsLogger.error("## Cmm1100.get_Update() Exception START ##");
			ecamsLogger.error("## Error DESC : ", exception);	
			ecamsLogger.error("## Cmm1100.get_Update() Exception END ##");				
			throw exception;
		}finally{
			if (strQuery != null) 	strQuery = null;
			if (rtObj != null)	rtObj = null;
			if (rs != null)     try{rs.close();}catch (Exception ex){ex.printStackTrace();}
			if (pstmt != null)  try{pstmt.close();}catch (Exception ex2){ex2.printStackTrace();}
			if (conn != null){
				try{
					connectionContext.release();
				}catch(Exception ex3){
					ecamsLogger.error("## Cmm1100.get_Update() connection release exception ##");
					ex3.printStackTrace();
				}
			}
		}
	}
	


	public Object[] getDaegyulList(String UserId)  throws SQLException, Exception {
		Connection        conn        = null;
		PreparedStatement pstmt       = null;
		StringBuffer      strQuery    = new StringBuffer();
		ResultSet         rs          = null;
		Object[]		  rtObj		  = null;
		ArrayList<HashMap<String, String>>	rtList  = new ArrayList<HashMap<String, String>>();
		HashMap<String, String>			  	rst	 	= null;
		ConnectionContext connectionContext = new ConnectionResource();	
		
		
		
		try {
			conn = connectionContext.getConnection();
			
            strQuery.setLength(0);
            //to_char(to_date( ? , 'yyyy/mm/dd'),'yyyymmdd')
		    //strQuery.append("select b.cm_userid,b.cm_username, a.cm_blkseq, a.cm_blkstdate,a.cm_blkeddate,a.cm_blankcd,a.cm_daeusr,a.cm_blkcont \n");
            strQuery.append("select b.cm_userid,b.cm_username, a.cm_blkseq, to_char(a.cm_blkstdate,'yyyy/mm/dd') as cm_blkstdate, to_char(a.cm_blkeddate,'yyyy/mm/dd') as cm_blkeddate, a.cm_blankcd,a.cm_daeusr,a.cm_blkcont, \n");
            strQuery.append("to_date(a.cm_blkstdate, 'yyyy/mm/dd')as sssdate, to_date(a.cm_blkeddate, 'yyyy/mm/dd')as eeedate \n");
		    strQuery.append("  from cmm0042 a, cmm0040 b		 \n");
		    //strQuery.append(" where a.cm_status='9' \n");
		    strQuery.append(" where a.cm_userid = ? \n");
		    strQuery.append("  and  b.cm_userid = a.cm_daeusr(+) \n");
		    strQuery.append("  and  a.cm_blkstdate(+) is not null \n");
		    strQuery.append("  and  a.cm_blkeddate(+) is not null \n");
//			strQuery.append(" and a.cm_blankdts>=to_date(SYSDATE, 'yyyy/mm/dd')		\n");
		    //strQuery.append("  and a.cm_blankdts<=to_char(SYSDATE, 'yyyymmdd') \n");
		    strQuery.append("  and a.cm_blkeddate(+) >= to_date(SYSDATE, 'yyyy/mm/dd') \n");

            /*
            strQuery.append("select cm_userid, cm_username, cm_blkseq, cm_blankcd, cm_blkstdate, cm_blkeddate, cm_blkcont, cm_daeusr \n");
            strQuery.append(" from cmm0042 \n");
            strQuery.append("where cm_userid=? \n");
            strQuery.append(" and cm_blkstdate is not null \n");
            strQuery.append(" and cm_blkeddate is not null \n");
            //strQuery.append(" and cm_blkstdate >= to_date(SYSDATE, 'yyyy/mm/dd') \n");
            strQuery.append(" and cm_blkstdate >= to_date(SYSDATE, 'yyyy./mm/dd') \n");
		    */
		   
			
			
			
		    pstmt = conn.prepareStatement(strQuery.toString());
		    //pstmt = new LoggableStatement(conn, strQuery.toString());
			pstmt.setString(1, UserId);
            //ecamsLogger.error(((LoggableStatement)pstmt).getQueryString());
            rs = pstmt.executeQuery();
            
            while (rs.next()) {
				rst = new HashMap<String,String>();
				rst.put("cm_userid", rs.getString("cm_userid"));
				rst.put("cm_username", rs.getString("cm_username"));
				rst.put("sedate", rs.getString("cm_blkstdate")+ " - " +rs.getString("cm_blkeddate"));
				
				rst.put("ssdate", rs.getString("cm_blkstdate"));
				rst.put("eedate", rs.getString("cm_blkeddate"));
				
				rst.put("cm_daegmsg", rs.getString("cm_blankcd"));
				if (rs.getString("cm_daeusr") != null && rs.getString("cm_daeusr") != "")
				    rst.put("cm_daeusr", rs.getString("cm_daeusr"));
				rst.put("cm_daesayu", rs.getString("cm_blkcont"));	
				rst.put("blkseq", rs.getString("cm_blkseq"));
				rtList.add(rst);
				rst = null;
			}
            rs.close();
            pstmt.close();
            conn.close();
            rs = null;
            pstmt = null;
            conn = null;
            
    		rtObj =  rtList.toArray();
    		rtList.clear();
    		rtList = null;
    		
    		return rtObj;            
			
		} catch (SQLException sqlexception) {
			sqlexception.printStackTrace();
			ecamsLogger.error("## Cmm1100.getDaegyulList() SQLException START ##");
			ecamsLogger.error("## Error DESC : ", sqlexception);	
			ecamsLogger.error("## Cmm1100.getDaegyulList() SQLException END ##");			
			throw sqlexception;
		} catch (Exception exception) {
			exception.printStackTrace();
			ecamsLogger.error("## Cmm1100.getDaegyulList() Exception START ##");
			ecamsLogger.error("## Error DESC : ", exception);	
			ecamsLogger.error("## Cmm1100.getDaegyulList() Exception END ##");				
			throw exception;
		}finally{
			if (strQuery != null) 	strQuery = null;
			if (rtObj != null)	rtObj = null;
			if (rs != null)     try{rs.close();}catch (Exception ex){ex.printStackTrace();}
			if (pstmt != null)  try{pstmt.close();}catch (Exception ex2){ex2.printStackTrace();}
			if (conn != null){
				try{
					connectionContext.release();
				}catch(Exception ex3){
					ecamsLogger.error("## Cmm1100.getDaegyulList() connection release exception ##");
					ex3.printStackTrace();
				}
			}
		}
	}
	
	public Object[] getDaegyulState(String UserId)  throws SQLException, Exception {
		Connection        conn        = null;
		PreparedStatement pstmt       = null;
		StringBuffer      strQuery    = new StringBuffer();
		ResultSet         rs          = null;
		Object[]		  rtObj		  = null;
		String 			  SysDt		  = null;
		ArrayList<HashMap<String, String>>	rtList  = new ArrayList<HashMap<String, String>>();
		HashMap<String, String>			  	rst	 	= null;
		UserInfo		  userInfo	  = new UserInfo();
		HashMap<String,String>	rData = null;
		Object[] uInfo = null;
		ConnectionContext connectionContext = new ConnectionResource();	
		
		try {
			conn = connectionContext.getConnection();
			strQuery.setLength(0);
            strQuery.append("select to_char(SYSDATE, 'yyyymmdd') as sysdt from dual 	\n");
            pstmt = conn.prepareStatement(strQuery.toString());
			rs = pstmt.executeQuery();
            if (rs.next()) {
            	SysDt = rs.getString("sysdt");
            }
            rs.close();
            pstmt.close();
            //SysDt = "20110316";
            strQuery.setLength(0);
            strQuery.append("select NVL(a.cm_status,'0') as ustate,b.cm_daeusr,to_char(b.cm_blkstdate,'yyyymmdd') as cm_blkstdate," +
            		"to_char(b.cm_blkeddate,'yyyymmdd') as cm_blkeddate from cmm0040 a, cmm0042 b				\n");
            strQuery.append(" where a.cm_userid=? 												\n");
            strQuery.append("   and a.cm_userid=b.cm_userid(+) 									\n");//Frm_User
            strQuery.append("   and a.cm_active='1' 												\n");
			pstmt = conn.prepareStatement(strQuery.toString());
			pstmt.setString(1, UserId);
            rs = pstmt.executeQuery();
            if (rs.next()) {
            	rst = new HashMap<String,String>();
            	rst.put("cm_status", rs.getString("ustate"));
            	rst.put("status", "0");
            	//if (rs.getString("ustate").equals("9")){
            	//ecamsLogger.error("@@@@ : " +rs.getString("cm_blankdts").compareTo(SysDt));
            	//ecamsLogger.error("#### : " +rs.getString("cm_blankdte").compareTo(SysDt));
            	if (rs.getString("cm_blkstdate") != null){
	            	if (rs.getString("cm_blkstdate").compareTo(SysDt) <=0 && rs.getString("cm_blkeddate").compareTo(SysDt) >=0){
	            		rst.put("status", "9");
	            		//ecamsLogger.error("2222 : " +rs.getString("cm_blankdts").compareTo(SysDt));
	            		if (rs.getString("cm_daeusr") != null && rs.getString("cm_daeusr") != "" && !rs.getString("cm_daeusr").equals("")){
				        	uInfo = userInfo.getUserInfo(rs.getString("cm_daeusr"));
				        	rData = (HashMap<String, String>) uInfo[0];
							rst.put("Lbl_Tit", "부재등록 상태입니다. (대결인 : " + rData.get("cm_username") + ")");
							rData = null;
							uInfo = null;
						}
						else{
							rst.put("Lbl_Tit", "부재등록 상태입니다.");
						}            		
	            	}
            	}
               	rst.put("cm_blkstdate", rs.getString("cm_blkstdate"));
                rst.put("cm_blkeddate", rs.getString("cm_blkeddate"));
                
				rtList.add(rst);
				rst = null;
            }
            
            rs.close();
            pstmt.close();
            conn.close();
            userInfo = null;
            rs = null;
            pstmt = null;
            conn = null;
            
    		rtObj =  rtList.toArray();
    		rtList.clear();
    		rtList = null;
    		
    		return rtObj;
    		
    		
		} catch (SQLException sqlexception) {
			sqlexception.printStackTrace();
			ecamsLogger.error("## Cmm1100.getDaegyulState() SQLException START ##");
			ecamsLogger.error("## Error DESC : ", sqlexception);	
			ecamsLogger.error("## Cmm1100.getDaegyulState() SQLException END ##");			
			throw sqlexception;
		} catch (Exception exception) {
			exception.printStackTrace();
			ecamsLogger.error("## Cmm1100.getDaegyulState() Exception START ##");
			ecamsLogger.error("## Error DESC : ", exception);	
			ecamsLogger.error("## Cmm1100.getDaegyulState() Exception END ##");				
			throw exception;
		}finally{
			if (strQuery != null) 	strQuery = null;
			if (rtObj != null)	rtObj = null;
			if (rs != null)     try{rs.close();}catch (Exception ex){ex.printStackTrace();}
			if (pstmt != null)  try{pstmt.close();}catch (Exception ex2){ex2.printStackTrace();}
			if (conn != null){
				try{
					connectionContext.release();
				}catch(Exception ex3){
					ecamsLogger.error("## Cmm1100.getDaegyulState() connection release exception ##");
					ex3.printStackTrace();
				}
			}
		}
	}
	
	
	public Object[] getCbo_User_Click(String UserId, String cm_manid) throws SQLException, Exception {
		Connection        conn        = null;
		PreparedStatement pstmt       = null;
		StringBuffer      strQuery    = new StringBuffer();
		ResultSet         rs          = null;
		Object[]		  rtObj		  = null;
		ArrayList<HashMap<String, String>>	rtList  = new ArrayList<HashMap<String, String>>();
		HashMap<String, String>			  	rst	 	= null;
        
		ConnectionContext connectionContext = new ConnectionResource();		
		try {
			conn = connectionContext.getConnection();
			
			String DeptCd = "";
			strQuery.setLength(0);
			strQuery.append("select cm_userid,cm_username from cmm0040  \n");
		    strQuery.append(" where cm_project in (select cm_deptcd     \n");	
		    strQuery.append("                        from (select * from cmm0100 where cm_useyn='Y') \n");	
		    strQuery.append("                       start with cm_deptcd=(select cm_project from cmm0040  \n");	
		    strQuery.append("                                              where cm_userid=?) \n");		
		    strQuery.append("                       connect by prior cm_deptcd = cm_updeptcd) \n");	
		    strQuery.append("   and cm_active='1' \n");	
		    strQuery.append("   and cm_userid<>?  \n");
		    strQuery.append(" order by cm_username \n");
			pstmt = conn.prepareStatement(strQuery.toString());
			//pstmt =  new LoggableStatement(conn, strQuery.toString());
			
			pstmt.setString(1, UserId);
			pstmt.setString(2, UserId);
			////ecamsLogger.error(((LoggableStatement)pstmt).getQueryString()); 
            rs = pstmt.executeQuery();
            //
            
            rst = new HashMap<String,String>();
			rst.put("cm_userid", "0");
			rst.put("cm_username", "선택하세요");
			rst.put("username", "선택하세요");
			rtList.add(rst);
			
			while(rs.next()) {
				rst = new HashMap<String,String>();
				rst.put("cm_userid", rs.getString("cm_userid"));
				rst.put("cm_username", rs.getString("cm_username"));
				rst.put("username", rs.getString("cm_username") +"  ["+ rs.getString("cm_userid") +"]");
				rtList.add(rst);
				rst = null;
			}
			rs.close();
			pstmt.close();
			conn.close();
			rs = null;
			pstmt = null;
			conn = null;
			
			rtObj =  rtList.toArray();
			rtList.clear();
			rtList = null;
			
			return rtObj;			
			
            
		} catch (SQLException sqlexception) {
			sqlexception.printStackTrace();
			ecamsLogger.error("## Cmm1100.getCbo_User_Click() SQLException START ##");
			ecamsLogger.error("## Error DESC : ", sqlexception);	
			ecamsLogger.error("## Cmm1100.getCbo_User_Click() SQLException END ##");			
			throw sqlexception;
		} catch (Exception exception) {
			exception.printStackTrace();
			ecamsLogger.error("## Cmm1100.getCbo_User_Click() Exception START ##");
			ecamsLogger.error("## Error DESC : ", exception);	
			ecamsLogger.error("## Cmm1100.getCbo_User_Click() Exception END ##");				
			throw exception;
		}finally{
			if (strQuery != null) 	strQuery = null;
			if (rtObj != null)	rtObj = null;
			if (rs != null)     try{rs.close();}catch (Exception ex){ex.printStackTrace();}
			if (pstmt != null)  try{pstmt.close();}catch (Exception ex2){ex2.printStackTrace();}
			if (conn != null){
				try{
					connectionContext.release();
				}catch(Exception ex3){
					ecamsLogger.error("## Cmm1100.getCbo_User_Click() connection release exception ##");
					ex3.printStackTrace();
				}
			}
		}
	}
	
}//end of Cmm1100 class statement
