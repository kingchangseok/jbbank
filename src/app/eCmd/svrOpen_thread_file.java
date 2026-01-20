package app.eCmd;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.logging.log4j.Logger;

import com.ecams.common.dbconn.ConnectionContext;
import com.ecams.common.dbconn.ConnectionResource;
import com.ecams.common.logger.EcamsLogger;

import app.common.LoggableStatement;
import app.thread.*;

public class svrOpen_thread_file implements Runnable{

	Logger ecamsLogger  = EcamsLogger.getLoggerInstance();
	private HashMap<String,String> myfile = null;
	private HashMap<String,String> myetcData = null;
	private ArrayList<HashMap<String, String>> parrent_rtList = null;
	private Connection conn1 = null;


	svrOpen_thread_file(HashMap<String,String> etcData,ArrayList<HashMap<String, String>> rtList,Connection conn){
		myetcData = etcData;
		parrent_rtList = rtList;
		conn1 = conn;
	}

	public void run() {
		HashMap<String,String> tmpRtList = null;
		try {
			tmpRtList = Master_Check(myetcData,conn1);

			if (tmpRtList != null) {
					parrent_rtList.add(tmpRtList);
					
			} 
			//ecamsLogger.error("+++null+++"+tmpRtList.toString());
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			ecamsLogger.error(e);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			ecamsLogger.error(e);
		}

	}
	public HashMap<String,String> Master_Check(HashMap<String,String> etcData,Connection conn) throws SQLException, Exception {
		PreparedStatement pstmt       = null;
		ResultSet         rs          = null;
		StringBuffer      strQuery    = new StringBuffer();
		String            wkDsnCd     = "";
		String            wkB         = "";
		String            wkC         = "";
		boolean           findSw      = false;
		HashMap<String,String> rst = null;
		try {

			wkB = etcData.get("filename");
			strQuery.setLength(0);
			strQuery.append("select cm_dsncd from cmm0070            \n");
			strQuery.append(" where cm_syscd=? and cm_dirpath=?      \n");
			pstmt = conn.prepareStatement(strQuery.toString());
			//pstmt = new LoggableStatement(conn,strQuery.toString());
            pstmt.setString(1, etcData.get("syscd"));
            pstmt.setString(2, etcData.get("dirpath"));
            ////ecamsLogger.error(((LoggableStatement)pstmt).getQueryString());
            rs = pstmt.executeQuery();
            if (rs.next()) {
            	wkDsnCd = rs.getString("cm_dsncd");
            }
            rs.close();
            pstmt.close();
            
            if (wkB.indexOf(".")>=0) {
            	wkC = wkB.substring(0,wkB.lastIndexOf("."));
            }
            findSw = false;
            if (wkDsnCd != null && wkDsnCd != "") {
            	strQuery.setLength(0);
            	strQuery.append("select count(*) cnt from cmr0020 a,cmm0036 b  \n");
				strQuery.append(" where a.cr_syscd=? and a.cr_dsncd=? \n");
				strQuery.append("   and a.cr_syscd=b.cm_syscd         \n");
				strQuery.append("   and a.cr_rsrccd=b.cm_rsrccd       \n");
				strQuery.append("   and a.cr_rsrcname=decode(substr(b.cm_info,27,1),'1',?,?) \n");
				pstmt = conn.prepareStatement(strQuery.toString());
				//pstmt = new LoggableStatement(conn,strQuery.toString());
	            pstmt.setString(1, etcData.get("syscd"));
	            pstmt.setString(2, wkDsnCd);
	            pstmt.setString(3, wkC);
	            pstmt.setString(4, wkB);
	            ////ecamsLogger.error(((LoggableStatement)pstmt).getQueryString());
	            rs = pstmt.executeQuery();
	            if (rs.next()) {
	            	if (rs.getInt("cnt") > 0) findSw = true;
	            }
	            rs.close();
	            pstmt.close();
            }
            if (findSw == false) {
				rst = new HashMap<String, String>();
				rst.put("cm_dirpath", etcData.get("dirpath"));
				rst.put("filename", wkB);
				rst.put("cm_dsncd", wkDsnCd);
				
				rst.put("enable1", "1");
				rst.put("selected", "0");
				rst.put("error", "");
				//rsval.add(rst);
            } else rst = null;


			return rst;

		} catch (SQLException sqlexception) {
			sqlexception.printStackTrace();
			ecamsLogger.error("## svrOpen_thread.Master_Proc() SQLException START ##");
			ecamsLogger.error("## Error DESC : ", sqlexception);
			ecamsLogger.error("## svrOpen_thread.Master_Proc() SQLException END ##");
			throw sqlexception;
		} catch (Exception exception) {
			exception.printStackTrace();
			ecamsLogger.error("## svrOpen_thread.Master_Proc() Exception START ##");
			ecamsLogger.error("## Error DESC : ", exception);
			ecamsLogger.error("## svrOpen_thread.Master_Proc() Exception END ##");
			throw exception;
		}finally{
			if (strQuery != null) 	strQuery = null;
			if (rs != null)     try{rs.close();}catch (Exception ex){ex.printStackTrace();}
			if (pstmt != null)  try{pstmt.close();}catch (Exception ex2){ex2.printStackTrace();}
		}
	}
}
