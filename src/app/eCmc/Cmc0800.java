/*****************************************************************************************
	1. program ID	: Cmc0100.java
	2. create date	: 2010.11.15
	3. auth		    : no Name
	4. update date	: 
	5. auth		    : 
	6. description	: 夸备包府 ISR 殿废
*****************************************************************************************/

package app.eCmc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import app.common.LoggableStatement;
import org.apache.logging.log4j.Logger;
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
public class Cmc0800{

    /**
     * Logger Class Instance Creation
     * logger
     */
    Logger ecamsLogger  = EcamsLogger.getLoggerInstance();
    
	/**
	 * 夸备包府 ISRSUBID 炼雀
	 * @param UserId
	 * @return List
	 * @throws SQLException
	 * @throws Exception
	 */
	public Object[] getBaseInfo(String UserId,String strAcptNo) throws SQLException, Exception {
		Connection        conn        = null;
		PreparedStatement pstmt       = null;
		ResultSet         rs          = null;
		StringBuffer      strQuery    = new StringBuffer();
		Object[] returnObjectArray    = null;
		ArrayList<HashMap<String, String>>  rsval = new ArrayList<HashMap<String, String>>();
		HashMap<String, String> rst	  = null;
		
		ConnectionContext connectionContext = new ConnectionResource();		
		
		try {
			
			conn = connectionContext.getConnection();
			
			strQuery.setLength(0);//ISERID : R + YYYYMM + '-' + SEQ(4) : R201011-0001
			strQuery.append("select distinct a.CC_ISRID,a.CC_ISRTITLE,a.CC_EDITOR,a.CC_REQENDDT,a.CC_DETAILSAYU,b.CM_USERNAME,d.CM_DEPTNAME, \n");
			strQuery.append("       c.cm_codename ISRREQGBN,e.CC_ISRSUB,e.CC_RECVPART,e.CC_CREATDT,e.CC_LASTDT,\n");
			strQuery.append("       e.CC_RECVGBN,e.CC_MAINSTATUS,e.CC_SUBSTATUS,e.CC_CATETYPE,e.CC_DETCATE,e.CC_RECVUSER, \n");
			strQuery.append("       e.CC_JOBRANK,e.CC_HANDLER,nvl(e.CC_BASESUB,'') as CC_BASESUB,f.cm_codename STATUS,g.cm_codename SUBSTATUS \n");
			strQuery.append("  from cmc0100 a,cmm0040 b,cmm0020 c,cmm0100 d,cmc0110 e,cmm0020 f,cmm0020 g,cmm0040 h,cmc0300 i   \n");
			strQuery.append(" where i.cc_acptno = ? \n");
			strQuery.append("   and a.cc_isrid = i.cc_isrid \n");
			strQuery.append("   and a.cc_isrid = e.cc_isrid \n");
			strQuery.append("   and e.cc_isrsub = i.cc_isrsub \n");
			strQuery.append("   and h.cm_project = e.CC_RECVPART \n");
			strQuery.append("   and a.cc_editor = b.cm_userid \n");
			strQuery.append("   and b.cm_project = d.cm_deptcd \n");
			strQuery.append("   and f.cm_macode='ISRSTAMAIN' \n");
			strQuery.append("   and f.cm_micode = e.cc_mainstatus \n");
			strQuery.append("   and g.cm_macode = 'ISRSTASUB' \n");
			strQuery.append("   and g.cm_micode = e.cc_substatus \n");
			strQuery.append("   and c.cm_macode='RECVGBN' \n");
			strQuery.append("   and c.cm_micode=e.CC_RECVGBN \n");
			strQuery.append(" order by a.CC_ISRID,e.CC_ISRSUB \n");
	        pstmt = conn.prepareStatement(strQuery.toString());
	        // pstmt = new LoggableStatement(conn,strQuery.toString());
	        pstmt.setString(1, strAcptNo);
	    //    ecamsLogger.error(((LoggableStatement)pstmt).getQueryString());
	        rs = pstmt.executeQuery();

	        while(rs.next()) {
		  		rst = new HashMap<String, String>();
				rst.put("CC_ISRID", rs.getString("CC_ISRID"));
				rst.put("CC_ISRTITLE", rs.getString("CC_ISRTITLE"));
				rst.put("CC_REQENDDT", "");
				if (rs.getString("CC_REQENDDT") != "" && rs.getString("CC_REQENDDT") != null){
					rst.put("CC_REQENDDT", rs.getString("CC_REQENDDT").substring(0,4)+"/"+rs.getString("CC_REQENDDT").substring(4,6)+"/"+rs.getString("CC_REQENDDT").substring(6));
				}
				rst.put("CC_DETAILSAYU", rs.getString("CC_DETAILSAYU"));
				rst.put("CM_USERNAME", rs.getString("CM_USERNAME"));
				rst.put("CM_DEPTNAME", rs.getString("CM_DEPTNAME"));
				rst.put("CC_ISRSUB", rs.getString("CC_ISRSUB"));
				rst.put("CC_RECVPART", rs.getString("CC_RECVPART"));
				rst.put("CC_CREATDT", rs.getString("CC_CREATDT"));
				rst.put("CC_LASTDT", rs.getString("CC_LASTDT"));
				rst.put("ISRREQGBN", rs.getString("ISRREQGBN"));
				rst.put("CC_RECVGBN", rs.getString("CC_RECVGBN"));
				rst.put("CC_EDITOR", rs.getString("CC_EDITOR"));
				rst.put("CC_RECVUSER", rs.getString("CC_RECVUSER"));
				rst.put("CC_CATETYPE", "");
				if (rs.getString("CC_CATETYPE") != null && rs.getString("CC_CATETYPE") != ""){
					rst.put("CC_CATETYPE", rs.getString("CC_CATETYPE"));
				}
				rst.put("CC_DETCATE", "");
				if (rs.getString("CC_DETCATE") != null && rs.getString("CC_DETCATE") != ""){
					rst.put("CC_DETCATE", rs.getString("CC_DETCATE"));
				}
				rst.put("CC_JOBRANK", "");
				if (rs.getString("CC_JOBRANK") != null && rs.getString("CC_JOBRANK") != ""){
					rst.put("CC_JOBRANK", rs.getString("CC_JOBRANK"));
				}
				rst.put("CC_RECVUSER", UserId);
				rst.put("CC_HANDLER", "");
				if (rs.getString("CC_HANDLER") != null && rs.getString("CC_HANDLER") != ""){
					rst.put("CC_HANDLER", rs.getString("CC_HANDLER"));
				}
				rst.put("CC_BASESUB", rs.getString("CC_BASESUB"));
				rst.put("STATUS", rs.getString("STATUS"));
				rst.put("SUBSTATUS", rs.getString("SUBSTATUS"));
				rst.put("CC_MAINSTATUS", rs.getString("CC_MAINSTATUS"));
				rst.put("CC_SUBSTATUS", rs.getString("CC_SUBSTATUS"));
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
			ecamsLogger.error("## Cmc0800.getBaseInfo() SQLException START ##");
			ecamsLogger.error("## Error DESC : ", sqlexception);	
			ecamsLogger.error("## Cmc0800.getBaseInfo() SQLException END ##");			
			throw sqlexception;
		} catch (Exception exception) {
			exception.printStackTrace();
			ecamsLogger.error("## Cmc0800.getBaseInfo() Exception START ##");				
			ecamsLogger.error("## Error DESC : ", exception);	
			ecamsLogger.error("## Cmc0800.getBaseInfo() Exception END ##");				
			throw exception;
		}finally{
			if (strQuery != null) 	strQuery = null;
			if (returnObjectArray != null)	returnObjectArray = null;
			if (rs != null)     try{rs.close();}catch (Exception ex){ex.printStackTrace();}
			if (pstmt != null)  try{pstmt.close();}catch (Exception ex2){ex2.printStackTrace();}
			if (conn != null){
				try{
					ConnectionResource.release(conn);
				}catch(Exception ex3){
					ecamsLogger.error("## Cmc0800.getBaseInfo() connection release exception ##");
					ex3.printStackTrace();
				}
			}
		}
	}//end of getBaseInfo() method statement
	   
}//end of Cmc0100 class statement

