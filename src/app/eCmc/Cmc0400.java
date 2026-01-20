/*****************************************************************************************
	1. program ID	: Cmc0300.java
	2. create date	: 2010.11.20
	3. auth		    : no name
	4. update date	: 2010.11.20
	5. auth		    : no name
	6. description	: RFC ?????????
*****************************************************************************************/

package app.eCmc;

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
import app.common.AutoSeq;
import app.common.LoggableStatement;
import app.common.UserInfo;
import app.eCmr.Cmr0200;

/**
 * @author bigeyes
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */


public class Cmc0400{
    /**
     * Logger Class Instance Creation
     * logger
     */	
    Logger ecamsLogger  = EcamsLogger.getLoggerInstance();
    // RFC발행회차
    
    public String setREQInfo(HashMap<String,String> etcData,ArrayList<HashMap<String,String>> TeamList,ArrayList<HashMap<String,String>> UserList, ArrayList<HashMap<String,String>>tmpRunnerList) throws SQLException, Exception {
		Connection        conn        = null;
		PreparedStatement pstmt       = null;
		ResultSet         rs          = null;
		StringBuffer      strQuery    = new StringBuffer();
		String            retMsg      = "0";
		String            strREQID      = "";
		int pstmtcount = 0;
		ConnectionContext connectionContext = new ConnectionResource();		
		
		try {
			
			conn = connectionContext.getConnection();
			conn.setAutoCommit(false);
			
			strQuery.setLength(0);                		
    		strQuery.append("select lpad(to_number(max(substr(cc_reqid,-4)))+ 1,4,'0') as max	 	\n");               		
    		strQuery.append("  from cmc0400 												\n"); 
    		strQuery.append(" where cc_reqid like ?									 		\n"); 
    		pstmt = conn.prepareStatement(strQuery.toString());	
    		pstmt.setString(1, etcData.get("CC_REQID")+"%");
	        rs = pstmt.executeQuery();
	        if (rs.next()) {
	        	if (rs.getString("max") == null) {
	        		strREQID = etcData.get("CC_REQID") + "-0001";
	        	} 
	        	else {
	        		strREQID = etcData.get("CC_REQID") + "-" + rs.getString("max");
	        	}
	        }
	        rs.close();
	        pstmt.close();
			
			strQuery.setLength(0);
        	strQuery.append("insert into cmc0400				                              				\n");
        	strQuery.append("    (CC_REQID,CC_DOCTYPE,CC_DOCNUM,CC_DOCSUBJ,CC_DEPT1,CC_REQUSER1, 			\n");
        	strQuery.append("     CC_DEPT2,CC_REQUSER2,CC_DOCNUM2,CC_DETAILJOBN, 							\n");
        	strQuery.append("     CC_REQTYPE,CC_ENDPLAN,CC_JOBCD,CC_ACTTYPE,CC_JOBDIF,CC_CHKTEAM, 			\n");
        	strQuery.append("     CC_CHKDATA,CC_DEVSTDT,CC_DEVEDDT,CC_DETAILSAYU,CC_STARTDT,				\n");
        	strQuery.append("     CC_STATUS,CC_EDITOR,CC_ETTEAM,CC_DEVPERIOD) values						\n");
        	strQuery.append("(?,?,?,?,?,?,    ?,?,?,?,    ?,?,?,?,?,?,    ?,?,?,?,sysdate,	   '0',?,?,?)	\n");
        	
        	pstmt = conn.prepareStatement(strQuery.toString());
			//pstmt = new LoggableStatement(conn,strQuery.toString());
			pstmtcount = 1;
        	pstmt.setString(pstmtcount++, strREQID);
        	pstmt.setString(pstmtcount++, etcData.get("CC_DOCTYPE"));
        	pstmt.setString(pstmtcount++, etcData.get("CC_DOCNUM"));
        	pstmt.setString(pstmtcount++, etcData.get("CC_DOCSUBJ"));
        	pstmt.setString(pstmtcount++, etcData.get("CC_DEPT1"));
        	pstmt.setString(pstmtcount++, etcData.get("CC_REQUSER1"));
        	pstmt.setString(pstmtcount++, etcData.get("CC_DEPT2"));
        	pstmt.setString(pstmtcount++, etcData.get("CC_REQUSER2"));
        	pstmt.setString(pstmtcount++, etcData.get("CC_DOCNUM2"));
        	pstmt.setString(pstmtcount++, etcData.get("CC_DETAILJOBN"));
        	pstmt.setString(pstmtcount++, etcData.get("CC_REQTYPE"));
        	pstmt.setString(pstmtcount++, etcData.get("CC_ENDPLAN"));
        	pstmt.setString(pstmtcount++, etcData.get("CC_JOBCD"));
        	pstmt.setString(pstmtcount++, etcData.get("CC_ACTTYPE"));
        	pstmt.setString(pstmtcount++, etcData.get("CC_JOBDIF"));
        	pstmt.setString(pstmtcount++, etcData.get("CC_CHKTEAM"));
        	pstmt.setString(pstmtcount++, etcData.get("CC_CHKDATA"));
        	pstmt.setString(pstmtcount++, etcData.get("CC_DEVSTDT"));
        	pstmt.setString(pstmtcount++, etcData.get("CC_DEVEDDT"));
        	pstmt.setString(pstmtcount++, etcData.get("CC_DETAILSAYU"));
        	pstmt.setString(pstmtcount++, etcData.get("CC_EDITOR"));
        	pstmt.setString(pstmtcount++, etcData.get("CC_ETTEAM"));
        	pstmt.setString(pstmtcount++, etcData.get("CC_DEVPERIOD"));
        	
        	//ecamsLogger.error(((LoggableStatement)pstmt).getQueryString());
        	pstmt.executeUpdate();
        	pstmt.close();
        	
        	
        	for(int i=0;i<TeamList.size();i++){
        		
        		String            strSUBID      = "";
        		String			  manID         = "";
        		strQuery.setLength(0);                		
        		strQuery.append("select lpad(to_number(max(CC_SUBID))+ 1,2,'0') as max			 	\n");               		
        		strQuery.append("  from cmc0410 												\n"); 
        		strQuery.append(" where CC_REQID = ?									 		\n"); 
        		pstmt = conn.prepareStatement(strQuery.toString());	
        		pstmt.setString(1, strREQID);
    	        rs = pstmt.executeQuery();
    	        if (rs.next()) {
    	        	if (rs.getString("max") == null) {
    	        		strSUBID = "01";
    	        	} 
    	        	else {
    	        		strSUBID = rs.getString("max");
    	        	}
    	        }
    	        rs.close();
    	        pstmt.close();
        		
    	        strQuery.setLength(0);
    	        strQuery.append(" select cm_userid from cmm0040 \n");
    	        strQuery.append(" where cm_userid = ?           \n");
    	        strQuery.append(" and cm_manid = 'N'            \n");
    	        pstmt = conn.prepareStatement(strQuery.toString());
    	        pstmt.setString(1, TeamList.get(i).get("cm_userid"));
    	        rs = pstmt.executeQuery();
    	        
    	        
    	        if (rs.next()){
    	        	// 외주직원일 경우에는 CMC0410 의 CC_TEAM 에 manID 삽입
    	        	manID = TeamList.get(i).get("cm_userid");
    	        }else{
    	        	// 일반직원일 경우에는 기존대로  TeamList.get(i).get("cm_deptcd")
    	        	manID = "N";
    	        }
    	        
    	        
	        	strQuery.setLength(0);
	        	strQuery.append("insert into cmc0410				                              				\n");
	        	strQuery.append("    (CC_SUBID,CC_REQID,CC_TEAM,CC_STATUS,CC_STARTDT) 							\n");
	        	strQuery.append("values(?,?,?,'0',sysdate)														\n");
	        	
	        	pstmt = conn.prepareStatement(strQuery.toString());
				//pstmt = new LoggableStatement(conn,strQuery.toString());
				pstmtcount = 1;
	        	pstmt.setString(pstmtcount++, strSUBID);
	        	pstmt.setString(pstmtcount++, strREQID);
// 2022.10.11 외주/직원구분 없앰. 외주사번넣으면 수신부서 체   	
//	        	if (manID != "N"){
//	        		pstmt.setString(pstmtcount++, manID);
//	        	}else{
	        		pstmt.setString(pstmtcount++, TeamList.get(i).get("cm_deptcd"));	
//	        	}
	        	
	        	//ecamsLogger.error(((LoggableStatement)pstmt).getQueryString());
	        	pstmt.executeUpdate();
	        	pstmt.close();
        	
        	}
        	
        	if (UserList.size() > 0){
            	for(int i=0;i<UserList.size();i++){
            		
    	        	strQuery.setLength(0);
    	        	strQuery.append("insert into cmc0401				                              				\n");
    	        	strQuery.append("    (CC_REQID,CC_DEPT3,CC_REQUSER3)				 							\n");
    	        	strQuery.append("values(?,?,?)																	\n");
    	        	
    	        	pstmt = conn.prepareStatement(strQuery.toString());
    				//pstmt = new LoggableStatement(conn,strQuery.toString());
    				pstmtcount = 1;
    				pstmt.setString(pstmtcount++, strREQID);
    				pstmt.setString(pstmtcount++, UserList.get(i).get("cm_project"));
    	        	pstmt.setString(pstmtcount++, UserList.get(i).get("cm_userid"));
    	        	//ecamsLogger.error(((LoggableStatement)pstmt).getQueryString());
    	        	pstmt.executeUpdate();
    	        	pstmt.close();
            	
            	}
        	}
        	
        	if(tmpRunnerList != null){
	        	for(int k=0;k<tmpRunnerList.size();k++){
	    			
	    			String UserIp = "";
	    			String UserNm = "";
	    			String url	  = "";
	    			
	    			strQuery.setLength(0);                		
	  	    		strQuery.append("select a.cm_ipaddress, a.cm_username, b.cm_url 	\n");               		
	  	    		strQuery.append("  from cmm0040 a, cmm0010 b						\n"); 
	  	    		strQuery.append(" where a.cm_userid = ?								\n");
	  	    		strQuery.append("   and b.cm_stno = 'ECAMS'							\n");
	  	    		pstmt = conn.prepareStatement(strQuery.toString());
	  	    		//pstmt = new LoggableStatement(conn,strQuery.toString());
	  	    		pstmtcount=1;
	  	    		pstmt.setString(pstmtcount++, tmpRunnerList.get(k).get("cm_userid"));
	  	    		//ecamsLogger.error(((LoggableStatement)pstmt).getQueryString());
	  		        rs = pstmt.executeQuery();
	  		        if (rs.next()) {
	  		        	UserIp = rs.getString("cm_ipaddress");
	  		        	UserNm = rs.getString("cm_username");
	  		        	url = rs.getString("cm_url");
	  		        }
	  		        rs.close();
	  		        pstmt.close();
	    			
	    			String Makegap="";
	    			Makegap="개발요청제목 : " + etcData.get("CC_REQSUB")+"\n"+ "에 대한개발요청서가 발행되었습니다. 형상관리시스템에 접속하여 확인하여 주시기 바랍니다.";
	    			
			        strQuery.setLength(0);        	
		        	strQuery.append("Begin CMR9920_STR ( ");
		        	//strQuery.append("?, ?, ?, '개발요청서발행통보', ?, 'http://scm/jbbank.co.kr:8080'); End;");
		        	strQuery.append("?, ?, ?, '개발요청서발행통보', ?, ?); End;");
		        	pstmt = conn.prepareStatement(strQuery.toString());
		        	pstmtcount = 1;
		        	pstmt.setString(pstmtcount++, UserNm);
		        	pstmt.setString(pstmtcount++, UserNm);
		        	pstmt.setString(pstmtcount++, UserIp);
		        	pstmt.setString(pstmtcount++, Makegap);
		        	pstmt.setString(pstmtcount++, url);
		        	
		        	pstmt.executeUpdate();
		        	pstmt.close();
		        	rs = null;
		        	pstmt = null;
			    }
        	}

	        conn.close();
	        
	        rs = null;
	        pstmt = null;
	        conn = null;
	        
	        
	        return strREQID;
	        
	        
		} catch (SQLException sqlexception) {
			sqlexception.printStackTrace();
			if (rs != null)     try{rs.close();}catch (Exception ex){ex.printStackTrace();}
			if (pstmt != null)  try{pstmt.close();}catch (Exception ex2){ex2.printStackTrace();}
			if (conn != null){
				try{
					conn.rollback();
					ConnectionResource.release(conn);
				}catch(Exception ex3){
					ecamsLogger.error("## Cmc0100.setISRInfo() connection release exception ##");
					ex3.printStackTrace();
				}
			}
			ecamsLogger.error("## Cmc0100.setISRInfo() SQLException START ##");
			ecamsLogger.error("## Error DESC : ", sqlexception);	
			ecamsLogger.error("## Cmc0100.setISRInfo() SQLException END ##");			
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
					ecamsLogger.error("## Cmc0100.setISRInfo() connection release exception ##");
					ex3.printStackTrace();
				}				
			}			
			ecamsLogger.error("## Cmc0100.setISRInfo() Exception START ##");				
			ecamsLogger.error("## Error DESC : ", exception);	
			ecamsLogger.error("## Cmc0100.setISRInfo() Exception END ##");				
			throw exception;
		}finally{
			if (strQuery != null) 	strQuery = null;
			if (rs != null)     try{rs.close();}catch (Exception ex){ex.printStackTrace();}
			if (pstmt != null)  try{pstmt.close();}catch (Exception ex2){ex2.printStackTrace();}
			if (conn != null){
				try{
					ConnectionResource.release(conn);
					conn = null;
				}catch(Exception ex3){
					ecamsLogger.error("## Cmc0100.setISRInfo() connection release exception ##");
					ex3.printStackTrace();
				}
			}
		}
	}//end of setISRInfo() method statement
    
    public String setREQupdt(HashMap<String,String> etcData,ArrayList<HashMap<String,String>> TeamList,ArrayList<HashMap<String,String>> UserList, ArrayList<HashMap<String,String>>tmpRunnerList) throws SQLException, Exception {
		Connection        conn        = null;
		PreparedStatement pstmt       = null;
		ResultSet         rs          = null;
		ResultSet         rs2         = null;
		StringBuffer      strQuery    = new StringBuffer();
		String            retMsg      = "0";
		String            strREQID    = "";
		int				  pstmtcount  = 0;
		String[]          arrYN       = new String[TeamList.size()]; //수정되어 들어온 Team 값과 기존에 있던 Team 값을 비교할 때 사용 
		
		String[] arrStatus;
		String[] arrTeam;
		ConnectionContext connectionContext = new ConnectionResource();		
		
		try {

			conn = connectionContext.getConnection();
			conn.setAutoCommit(false);
			strREQID=etcData.get("CC_REQID");
			
			strQuery.setLength(0);
        	strQuery.append("update cmc0400 set				                              					\n");
        	strQuery.append("     CC_DOCTYPE=?,CC_DOCNUM=?,CC_DOCSUBJ=?,CC_DEPT1=?,CC_REQUSER1=?, 			\n");
        	strQuery.append("     CC_DEPT2=?,CC_REQUSER2=?,CC_DOCNUM2=?,CC_DETAILJOBN=?,					\n");
        	strQuery.append("     CC_REQTYPE=?,CC_ENDPLAN=?,CC_JOBCD=?,CC_ACTTYPE=?,CC_JOBDIF=?,CC_CHKTEAM=?,\n");
        	strQuery.append("     CC_CHKDATA=?,CC_DEVSTDT=?,CC_DEVEDDT=?,CC_DETAILSAYU=?,					\n");
        	strQuery.append("     CC_EDITOR=?,CC_ETTEAM=?,CC_DEVPERIOD=?									\n");
        	strQuery.append("where CC_REQID = ?																\n");
        	pstmt = conn.prepareStatement(strQuery.toString());
			//pstmt = new LoggableStatement(conn,strQuery.toString());
			pstmtcount = 1;
        	pstmt.setString(pstmtcount++, etcData.get("CC_DOCTYPE"));
        	pstmt.setString(pstmtcount++, etcData.get("CC_DOCNUM"));
        	pstmt.setString(pstmtcount++, etcData.get("CC_DOCSUBJ"));
        	pstmt.setString(pstmtcount++, etcData.get("CC_DEPT1"));
        	pstmt.setString(pstmtcount++, etcData.get("CC_REQUSER1"));
        	pstmt.setString(pstmtcount++, etcData.get("CC_DEPT2"));
        	pstmt.setString(pstmtcount++, etcData.get("CC_REQUSER2"));
        	pstmt.setString(pstmtcount++, etcData.get("CC_DOCNUM2"));
        	pstmt.setString(pstmtcount++, etcData.get("CC_DETAILJOBN"));
        	pstmt.setString(pstmtcount++, etcData.get("CC_REQTYPE"));
        	pstmt.setString(pstmtcount++, etcData.get("CC_ENDPLAN"));
        	pstmt.setString(pstmtcount++, etcData.get("CC_JOBCD"));
        	pstmt.setString(pstmtcount++, etcData.get("CC_ACTTYPE"));
        	pstmt.setString(pstmtcount++, etcData.get("CC_JOBDIF"));
        	pstmt.setString(pstmtcount++, etcData.get("CC_CHKTEAM"));
        	pstmt.setString(pstmtcount++, etcData.get("CC_CHKDATA"));
        	pstmt.setString(pstmtcount++, etcData.get("CC_DEVSTDT"));
        	pstmt.setString(pstmtcount++, etcData.get("CC_DEVEDDT"));
        	pstmt.setString(pstmtcount++, etcData.get("CC_DETAILSAYU"));
        	pstmt.setString(pstmtcount++, etcData.get("CC_EDITOR"));
        	pstmt.setString(pstmtcount++, etcData.get("CC_ETTEAM"));
        	pstmt.setString(pstmtcount++, etcData.get("CC_DEVPERIOD"));
        	pstmt.setString(pstmtcount++, etcData.get("CC_REQID"));
        	
        	
        	
        	//ecamsLogger.error(((LoggableStatement)pstmt).getQueryString());
        	pstmt.executeUpdate();
        	pstmt.close();

	        strQuery.setLength(0);
	        strQuery.append("select cc_team, cc_status 		\n");
	        strQuery.append("from cmc0410					\n");
	        strQuery.append("where cc_reqid = ?	    		\n");
	        pstmt = conn.prepareStatement(strQuery.toString());
	        //pstmt = new LoggableStatement(conn,strQuery.toString());
	        pstmt.setString(1, strREQID);
	        //ecamsLogger.error(((LoggableStatement)pstmt).getQueryString());
	        rs2 = pstmt.executeQuery();
	        
	        int k = 0;
	        while (rs2.next()){
	        	k += k;
	        	k++;
	        }
	        
	        arrTeam = new String[k];
	        arrStatus = new String[k];
	        
	        int a=0;
	        while (rs2.next()){
	        	arrTeam[a] = rs2.getString("cc_team");
	        	arrStatus[a] = rs2.getString("cc_status");
	        	a++;
	        }
	        
	        rs2.close();
	        pstmt.close();

        	strQuery.setLength(0);
			strQuery.append(" delete cmc0410 where cc_reqid = ? ");
    		
    		pstmt = conn.prepareStatement(strQuery.toString());
    		pstmt.setString(1, etcData.get("CC_REQID"));
    		
    		pstmt.executeUpdate();
    		pstmt.close();
    		
    		strQuery.setLength(0);
			strQuery.append(" delete cmc0401 where cc_reqid = ? ");
    		
    		pstmt = conn.prepareStatement(strQuery.toString());
    		pstmt.setString(1, etcData.get("CC_REQID"));
    		pstmt.executeUpdate();
    		pstmt.close();

        	for(int i=0;i<TeamList.size();i++){

        		
        		String strSUBID      = "";
        		String manID         = "";
        		
        		strQuery.setLength(0);                		
        		strQuery.append("select lpad(to_number(max(CC_SUBID))+ 1,2,'0') as max			\n");               		
        		strQuery.append("  from cmc0410 												\n"); 
        		strQuery.append(" where CC_REQID = ?									 		\n"); 
        		pstmt = conn.prepareStatement(strQuery.toString());	
        		pstmt.setString(1, etcData.get("CC_REQID"));
    	        rs = pstmt.executeQuery();
    	        if (rs.next()) {
    	        	if (rs.getString("max") == null) {
    	        		strSUBID = "01";
    	        	} 
    	        	else {
    	        		strSUBID = rs.getString("max");
    	        	}
    	        }
    	        rs.close();
    	        pstmt.close();
    	        
    	        strQuery.setLength(0);
    	        strQuery.append(" select cm_userid from cmm0040 \n");
    	        strQuery.append(" where cm_userid = ?           \n");
    	        strQuery.append(" and cm_manid = 'N'            \n");
    	        pstmt = conn.prepareStatement(strQuery.toString());
    	        pstmt.setString(1, TeamList.get(i).get("cm_userid"));
    	        rs = pstmt.executeQuery();
    	        
    	        //System.out.println(TeamList.get(i).get("cm_userid"));
    	        if (rs.next()){
    	        	// 외주직원일 경우에는 CMC0410 의 CC_TEAM 에 manID 삽입
    	        	manID = TeamList.get(i).get("cm_userid");
    	        }else{
    	        	// 일반직원일 경우에는 기존대로  TeamList.get(i).get("cm_deptcd") 
    	        	manID = "N";
    	        }
	        	strQuery.setLength(0);
	        	strQuery.append("insert into cmc0410				                              				\n");
	        	strQuery.append("    (CC_SUBID,CC_REQID,CC_TEAM,CC_STATUS,CC_STARTDT) 							\n");
	        	strQuery.append("values(?,?,?,?,sysdate)														\n");
	        	
	        	pstmt = conn.prepareStatement(strQuery.toString());
				//pstmt = new LoggableStatement(conn,strQuery.toString());
				pstmtcount = 1;
				
	        	pstmt.setString(pstmtcount++, strSUBID);
	        	pstmt.setString(pstmtcount++, etcData.get("CC_REQID"));
// 2022.10.11 외주/직원구분 없앰. 외주사번넣으면 수신부서 체크 안됨.	        	
//	        	if (manID != "N"){
//	        		pstmt.setString(pstmtcount++, manID);
//	        		for (int b=0; b<arrTeam.length; b++){
//	        			// 기존에 있던 Team 의 값과 수정되어 들어온 Team 값을 비교해서 cc_status 값도 기존의 값과 맞게 넣어줌
//	        			// 기존의 Team 값에 없던 Team 값이 들어오면 status 는 "0"
//	        			if (TeamList.get(i).get("cm_userid") == arrTeam[b]){
//	        				arrYN[i] = "Y";
//	        				break;
//	        			}else{
//	        				arrYN[i] = "N";
//	        			}
//	        		}
//	        		
//	        		if (arrYN[i] == "Y"){
//	        			pstmt.setString(pstmtcount++, arrStatus[i]);
//	        		}else{
//	        			pstmt.setString(pstmtcount++, "0");
//	        		}
//	        		
//	        		
//	        	}else{
	        		for (int b=0; b<arrTeam.length; b++){
	        			// 기존에 있던 Team 의 값과 수정되어 들어온 Team 값을 비교해서 cc_status 값도 기존의 값과 맞게 넣어줌
	        			// 기존의 Team 값에 없던 Team 값이 들어오면 status 는 "0"
	        			if (TeamList.get(i).get("cm_deptcd") == arrTeam[b]){
	        				arrYN[i] = "Y";
	        				break;
	        			}else{
	        				arrYN[i] = "N";
	        			}
	        		}
	        		
	        		if (arrYN[i] == "Y"){
	        			pstmt.setString(pstmtcount++, TeamList.get(i).get("cm_deptcd"));
	        			pstmt.setString(pstmtcount++, arrStatus[i]);
	        		}else{
	        			pstmt.setString(pstmtcount++, TeamList.get(i).get("cm_deptcd"));
	        			pstmt.setString(pstmtcount++, "0");
	        		}

//	        	}

	        	//ecamsLogger.error(((LoggableStatement)pstmt).getQueryString());
	        	pstmt.executeUpdate();
	        	pstmt.close();
        	
        	}
        	
        	for(int i=0;i<UserList.size();i++){
        		
	        	strQuery.setLength(0);
	        	strQuery.append("insert into cmc0401				                              				\n");
	        	strQuery.append("    (CC_REQID,CC_DEPT3,CC_REQUSER3)				 							\n");
	        	strQuery.append("values(?,?,?)																	\n");
	        	
	        	pstmt = conn.prepareStatement(strQuery.toString());
				//pstmt = new LoggableStatement(conn,strQuery.toString());
				pstmtcount = 1;
				pstmt.setString(pstmtcount++, etcData.get("CC_REQID"));
				pstmt.setString(pstmtcount++, UserList.get(i).get("cm_project"));
	        	pstmt.setString(pstmtcount++, UserList.get(i).get("cm_userid"));
	        	//ecamsLogger.error(((LoggableStatement)pstmt).getQueryString());
	        	pstmt.executeUpdate();
	        	pstmt.close();
        	
        	}
        	if(tmpRunnerList != null){
        	for(int z=0;z<tmpRunnerList.size();z++){
    			
    			String UserIp = "";
    			String UserNm = "";
    			String url = "";
    			
    			strQuery.setLength(0);                		
  	    		strQuery.append("select a.cm_ipaddress, a.cm_username, b.cm_url	\n");               		
  	    		strQuery.append("  from cmm0040 a, cmm0010 b 					\n"); 
  	    		strQuery.append(" where a.cm_userid = ?							\n");
  	    		strQuery.append("   and b.cm_stno = 'ECAMS'						\n");
  	    		pstmt = conn.prepareStatement(strQuery.toString());
  	    		//pstmt = new LoggableStatement(conn,strQuery.toString());
  	    		pstmtcount=1;
  	    		pstmt.setString(pstmtcount++, tmpRunnerList.get(z).get("cm_userid"));
  	    		//ecamsLogger.error(((LoggableStatement)pstmt).getQueryString());
  		        rs = pstmt.executeQuery();
  		        if (rs.next()) {
  		        	UserIp = rs.getString("cm_ipaddress");
  		        	UserNm = rs.getString("cm_username");
  		        	url = rs.getString("cm_url");
  		        }
  		        rs.close();
  		        pstmt.close();
    			
    			String Makegap="";
    			Makegap="개발요청제목 : " + etcData.get("CC_REQSUB")+"\n"+ "에 대한개발요청서가 발행되었습니다. 형상관리시스템에 접속하여 확인하여 주시기 바랍니다.";
    			
		        strQuery.setLength(0);        	
	        	strQuery.append("Begin CMR9920_STR ( ");
	        	//strQuery.append("?, ?, ?, '개발요청서발행통보', ?, 'http://scm/jbbank.co.kr'); End;");
	        	strQuery.append("?, ?, ?, '개발요청서발행통보', ?, ?); End;");
	        	pstmt = conn.prepareStatement(strQuery.toString());
	        	pstmtcount = 1;
	        	pstmt.setString(pstmtcount++, UserNm);
	        	pstmt.setString(pstmtcount++, UserNm);
	        	pstmt.setString(pstmtcount++, UserIp);
	        	pstmt.setString(pstmtcount++, Makegap);
	        	pstmt.setString(pstmtcount++, url);
	        	
	        	pstmt.executeUpdate();
	        	pstmt.close();
	        	rs = null;
	        	pstmt = null;
		   	}
        }
	        conn.close();
	        
	        rs = null;
	        pstmt = null;
	        conn = null;
	        
	        
	        return strREQID;
	        
	        
		} catch (SQLException sqlexception) {
			sqlexception.printStackTrace();
			if (rs != null)     try{rs.close();}catch (Exception ex){ex.printStackTrace();}
			if (pstmt != null)  try{pstmt.close();}catch (Exception ex2){ex2.printStackTrace();}
			if (conn != null){
				try{
					conn.rollback();
					ConnectionResource.release(conn);
				}catch(Exception ex3){
					ecamsLogger.error("## Cmc0100.setISRInfo() connection release exception ##");
					ex3.printStackTrace();
				}
			}
			ecamsLogger.error("## Cmc0100.setISRInfo() SQLException START ##");
			ecamsLogger.error("## Error DESC : ", sqlexception);	
			ecamsLogger.error("## Cmc0100.setISRInfo() SQLException END ##");			
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
					ecamsLogger.error("## Cmc0100.setISRInfo() connection release exception ##");
					ex3.printStackTrace();
				}				
			}			
			ecamsLogger.error("## Cmc0100.setISRInfo() Exception START ##");				
			ecamsLogger.error("## Error DESC : ", exception);	
			ecamsLogger.error("## Cmc0100.setISRInfo() Exception END ##");				
			throw exception;
		}finally{
			if (strQuery != null) 	strQuery = null;
			if (rs != null)     try{rs.close();}catch (Exception ex){ex.printStackTrace();}
			if (pstmt != null)  try{pstmt.close();}catch (Exception ex2){ex2.printStackTrace();}
			if (conn != null){
				try{
					ConnectionResource.release(conn);
					conn = null;
				}catch(Exception ex3){
					ecamsLogger.error("## Cmc0100.setISRInfo() connection release exception ##");
					ex3.printStackTrace();
				}
			}
		}
	}//end of setISRInfo() method statement
    
    public Object[] getREQList(String status, String UserID, String editorName, String cboGbn, String datStD, String datEdD, String DocType) throws SQLException, Exception {
		Connection        conn        = null;
		PreparedStatement pstmt       = null;
		PreparedStatement pstmt2      = null;
		PreparedStatement pstmt3      = null;
		ResultSet         rs          = null;
		ResultSet         rs2         = null;
		ResultSet         rs3         = null;
		StringBuffer      strQuery    = new StringBuffer();
		Object[] returnObjectArray    = null;
		ArrayList<HashMap<String, String>>  rsval = new ArrayList<HashMap<String, String>>();
		HashMap<String, String> rst	  = null;
		int              parmCnt      = 0;
		ConnectionContext connectionContext = new ConnectionResource();		
		
		try {
			
			conn = connectionContext.getConnection();
			strQuery.setLength(0);//ISERID : R + YYYYMM + '-' + SEQ(4) : R201011-0001
			int ParmCnt = 0;
			strQuery.append("select distinct a.CC_REQID, a.CC_DOCTYPE, a.CC_DOCNUM, a.CC_DOCSUBJ, a.CC_DEPT1,		\n");
			strQuery.append("       a.CC_REQUSER1, a.CC_DEPT2, a.CC_REQUSER2,NVL(a.CC_SRREQID,'N') CC_SRREQID ,  	\n");
			strQuery.append("       a.CC_DOCNUM2, a.CC_DETAILJOBN, a.CC_REQTYPE, a.CC_ENDPLAN, a.CC_JOBCD,  		\n");
			strQuery.append("   	a.CC_ACTTYPE, a.CC_JOBDIF, a.CC_CHKTEAM, a.CC_CHKDATA, a.CC_DEVSTDT,			\n");
			strQuery.append("   	a.CC_DEVEDDT, a.CC_DETAILSAYU, a.CC_STARTDT, a.CC_ENDDT, a.CC_STATUS,			\n");
			strQuery.append("   	a.CC_EDITOR, a.CC_ETTEAM, ISR_RECVPART(a.CC_REQID) RECVPART,a.CC_DEVPERIOD,		\n");
			strQuery.append("   	nvl(a.CC_TESTREQ_YN,'N') testreqyn,												\n");
			strQuery.append("   	(select cm_codename from cmm0020 where cm_macode = 'PASSINGTYPE' and cm_micode=a.CC_TESTPASS_CD) TESTPASS_NM,												\n");
			strQuery.append("   	DECODE(a.cc_requser1,null,'',b.cm_userid,b.cm_username) as bname, 				\n");
			strQuery.append(" 		g.cm_username as editorName,													\n"); 
			strQuery.append(" 		DECODE(a.cc_dept1,null,'',f.cm_deptcd,f.cm_deptname) as CM_DEPTNAME				\n");
			strQuery.append(" from cmc0400 a, cmm0040 b,cmm0100 f, cmm0040 g										\n");
			if(status.equals("00")){
				strQuery.append(" where a.CC_STATUS in ('0' , '1' ) \n" );
			}else if(status.equals("01")){
				strQuery.append(" where a.CC_STATUS in ('9') 		\n" );
			}
			if(cboGbn.equals("01")){
				strQuery.append(" and a.CC_EDITOR = ? \n");
			}
			if(!"".equals(editorName) && editorName != null){
				strQuery.append(" and g.cm_username = ? \n");
			}
			if(!datStD.equals("") && datStD != null){
				strQuery.append("and to_char(a.CC_STARTDT,'yyyy/mm/dd') >= ? \n" );
				strQuery.append("and to_char(a.CC_STARTDT,'yyyy/mm/dd') <= ? \n" );
			}
			//strQuery.append("and b.cm_userid = a.CC_REQUSER1 \n");
			strQuery.append(" and b.cm_userid = nvl(replace(a.cc_requser1,'기타',''),a.cc_editor)  \n");
			strQuery.append(" and g.cm_userid = a.cc_editor   				    \n");
			//strQuery.append(" and a.cc_dept1 = f.cm_deptcd                     \n");
			strQuery.append(" and f.cm_deptcd = nvl(a.cc_dept1, a.cc_etteam)    \n");
			/*
			if(DocType.equals("03")){
				strQuery.append(" and a.cc_doctype = '03'						\n");
			}else{
				strQuery.append(" and a.cc_doctype <> '03'						\n");
			}
			*/
			
			
	        pstmt = conn.prepareStatement(strQuery.toString());
	        pstmt = new LoggableStatement(conn,strQuery.toString());
	        if(cboGbn.equals("01")){
	        	pstmt.setString(++ParmCnt, UserID);
	        }
	        if(!"".equals(editorName) && editorName != null){
	        	pstmt.setString(++ParmCnt, editorName);
	        }
			if(!datStD.equals("") && datStD != null){
		        pstmt.setString(++ParmCnt, datStD);
		        pstmt.setString(++ParmCnt, datEdD);
			}
	        ecamsLogger.error(((LoggableStatement)pstmt).getQueryString());
	        rs = pstmt.executeQuery();
	        
	        while(rs.next()) {
		  		rst = new HashMap<String, String>();
		  		rst.put("CC_REQID", rs.getString("CC_REQID"));
				rst.put("CC_DOCTYPE", rs.getString("CC_DOCTYPE"));
				rst.put("CC_DOCNUM", rs.getString("CC_DOCNUM"));
				rst.put("CC_DOCSUBJ", rs.getString("CC_DOCSUBJ"));
				rst.put("CC_DEPT1", rs.getString("CC_DEPT1"));
				rst.put("CC_REQUSER1", rs.getString("CC_REQUSER1"));
				rst.put("CC_REQUSER1FULL", rs.getString("bname"));
				rst.put("CC_DEPT2", rs.getString("CC_DEPT2"));
				rst.put("CC_REQUSER2", rs.getString("CC_REQUSER2"));
				rst.put("CC_DOCNUM2", rs.getString("CC_DOCNUM2"));
				rst.put("CC_DETAILJOBN", rs.getString("CC_DETAILJOBN"));
				rst.put("CC_REQTYPE", rs.getString("CC_REQTYPE"));
				rst.put("CC_ENDPLAN", rs.getString("CC_ENDPLAN"));
				rst.put("CC_JOBCD", rs.getString("CC_JOBCD"));
				rst.put("CC_ACTTYPE", rs.getString("CC_ACTTYPE"));
				rst.put("CC_JOBDIF", rs.getString("CC_JOBDIF"));
				rst.put("CC_CHKTEAM", rs.getString("CC_CHKTEAM"));
				rst.put("CC_CHKDATA", rs.getString("CC_CHKDATA"));
				rst.put("CC_DEVSTDT", rs.getString("CC_DEVSTDT"));
				rst.put("CC_DEVEDDT", rs.getString("CC_DEVEDDT"));
				rst.put("CC_DETAILSAYU", rs.getString("CC_DETAILSAYU"));
				rst.put("CC_STARTDT", rs.getString("CC_STARTDT"));
				rst.put("CC_ENDDT", rs.getString("CC_ENDDT"));
				rst.put("CC_STATUS", rs.getString("CC_STATUS"));
				rst.put("CC_EDITOR", rs.getString("CC_EDITOR"));
				rst.put("CC_ETTEAM", rs.getString("CC_ETTEAM"));
				rst.put("RECVPART", rs.getString("RECVPART"));
				rst.put("CM_DEPTNAME", rs.getString("CM_DEPTNAME"));
				rst.put("editorName" , rs.getString("editorName"));
				rst.put("CC_DEVPERIOD", rs.getString("CC_DEVPERIOD"));
				rst.put("CC_SRREQID", rs.getString("CC_SRREQID"));
				rst.put("testreqyn", rs.getString("testreqyn"));
				rst.put("TESTPASS_NM", rs.getString("TESTPASS_NM"));
				
				
				String deptreq = "";
				String requser = "";
				
				strQuery.setLength(0);//ISERID : R + YYYYMM + '-' + SEQ(4) : R201011-0001
				ParmCnt = 0;
				strQuery.append("select a.CC_REQID, a.CC_DEPT3, a.CC_REQUSER3, b.cm_username, c.cm_deptname, d.cm_posname	\n");
				strQuery.append("  from cmc0401 a, cmm0040 b, cmm0100 c, cmm0103 d							\n");
				strQuery.append(" where a.CC_REQID = ?									 					\n");
				strQuery.append("   and a.CC_REQUSER3 = b.cm_userid						 					\n");
				strQuery.append("   and b.cm_position = d.cm_poscd                                			\n");
				strQuery.append("   and a.CC_DEPT3 = c.cm_deptcd							 				\n");
		        pstmt2 = conn.prepareStatement(strQuery.toString());
		        //pstmt2 = new LoggableStatement(conn,strQuery.toString());
				pstmt2.setString(++ParmCnt, rs.getString("CC_REQID"));
				//ecamsLogger.error(((LoggableStatement)pstmt2).getQueryString());
		        rs2 = pstmt2.executeQuery();
	        
		        while(rs2.next()) {
		        	if(!requser.equals("")){
		        		requser=requser+",";
		        	}
			  		deptreq = rs2.getString("CC_DEPT3")+"^"+rs2.getString("cm_deptname")+"^"+rs2.getString("CC_REQUSER3")+"^"+rs2.getString("cm_username")+"^"+rs2.getString("cm_posname");
//			  		System.out.println(rs2.getString("cm_posname")+rs2.getString("cm_username"));
			  		requser = requser+deptreq;
			  		deptreq = "";
			  		
		        }
		        rst.put("CC_REQUSER3", requser);

				rs2.close();
		        pstmt2.close();
		        
		        String fileinfo = "";
				String fileinfos = "";
				
				strQuery.setLength(0);//ISERID : R + YYYYMM + '-' + SEQ(4) : R201011-0001
				ParmCnt = 0;
				strQuery.append("select a.CC_ID,a.CC_SUBID, a.CC_SUBREQ, a.CC_SEQNO, a.CC_SAVEFILE, a.CC_ATTFILE,	\n");
				strQuery.append(" 		a.CC_LASTDT, a.CC_EDITOR, a.CC_REQCD, B.CM_USERNAME		\n");
				strQuery.append("  from cmc1001 a, cmm0040 b									\n");
				strQuery.append(" where a.CC_ID = ?									 			\n");
				strQuery.append("   and a.CC_EDITOR = b.cm_userid						 		\n");
		        pstmt2 = conn.prepareStatement(strQuery.toString());
		        //pstmt2 = new LoggableStatement(conn,strQuery.toString());
				pstmt2.setString(++ParmCnt, rs.getString("CC_REQID"));
				//ecamsLogger.error(((LoggableStatement)pstmt2).getQueryString());
		        rs2 = pstmt2.executeQuery();
	        
		        while(rs2.next()) {
		        	if(!fileinfos.equals("")){
		        		fileinfos=fileinfos+"$";
		        	}
		        	fileinfo = rs2.getString("CC_ID")+"^"+rs2.getString("CC_SUBID")+"^"+rs2.getString("CC_SUBREQ")+"^"+rs2.getString("CC_SEQNO")+"^"
			  		+rs2.getString("CC_SAVEFILE")+"^"+rs2.getString("CC_ATTFILE")+"^"+rs2.getString("CC_LASTDT")+"^"
			  		+rs2.getString("CC_EDITOR")+"^"+rs2.getString("CC_REQCD")+"^"+rs2.getString("CM_USERNAME");
			  		fileinfos = fileinfos+fileinfo;
			  		fileinfo = "";
			  		
		        }

				rs2.close();
		        pstmt2.close();
		        
		        rst.put("fileinfos", fileinfos);
		        
		        strQuery.setLength(0);
		        strQuery.append(" select CC_REQID, CC_SUBID, CC_TEAM from cmc0410 \n");
		        strQuery.append(" where CC_REQID = ? 							  \n");
		        pstmt3 = conn.prepareStatement(strQuery.toString());
		        pstmt3.setString(1, rs.getString("CC_REQID"));
		        //pstmt3 = new LoggableStatement(conn,strQuery.toString());
		        //ecamsLogger.error(((LoggableStatement)pstmt3).getQueryString());
		        rs3 = pstmt3.executeQuery();
		         
		        while(rs3.next()){
		        	rst.put("C_REQID", rs3.getString("CC_REQID"));
		        	rst.put("C_SUBID", rs3.getString("CC_SUBID"));
		        	rst.put("C_TEAM", rs3.getString("CC_TEAM"));
		        }
		        
				rsval.add(rst);
				rst = null;
				
		        rs3.close();
		        pstmt3.close();
	        }
	        rs.close();
	        pstmt.close();
	        conn.close();
	        
	  		rs = null;
	  		pstmt = null;
	  		rs2 = null;
	  		pstmt2 = null;
	  		conn = null;
			
			returnObjectArray = rsval.toArray();
			rsval.clear();
			rsval = null;

			return returnObjectArray;
			
			
		} catch (SQLException sqlexception) {
			sqlexception.printStackTrace();
			ecamsLogger.error("## Cmc0400.getREQList() SQLException START ##");
			ecamsLogger.error("## Error DESC : ", sqlexception);	
			ecamsLogger.error("## Cmc0400.getREQList() SQLException END ##");			
			throw sqlexception;
		} catch (Exception exception) {
			exception.printStackTrace();
			ecamsLogger.error("## Cmc0400.getREQList() Exception START ##");				
			ecamsLogger.error("## Error DESC : ", exception);	
			ecamsLogger.error("## Cmc0400.getREQList() Exception END ##");				
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
					ecamsLogger.error("## Cmc0400.getREQList() connection release exception ##");
					ex3.printStackTrace();
				}
			}
		}
	}//end of getISRList() method statement
    
    public String delREQinfo(HashMap<String,String> delREQ) throws SQLException, Exception{
    	Connection conn = null;
    	PreparedStatement pstmt = null;
    	ResultSet rs = null;
    	StringBuffer strQuery = new StringBuffer();
    	String status = "";
    	String reqid = "";
    	String result = "";
    	
    	ConnectionContext connectionContext = new ConnectionResource();
    	
    	try{
    		conn = connectionContext.getConnection();
    		conn.setAutoCommit(false);
    		
    		strQuery.setLength(0);
    		strQuery.append("select count(*) as cnt 					\n");
			strQuery.append("from cmc0410								\n");
			strQuery.append("where cc_reqid = ?							\n");
			strQuery.append("and cc_status in ('1','9')					\n");
    		
			pstmt = conn.prepareStatement(strQuery.toString());
	        //pstmt = new LoggableStatement(conn,strQuery.toString());
	        pstmt.setString(1, delREQ.get("delREQinfo"));
	        //ecamsLogger.error(((LoggableStatement)pstmt).getQueryString());
	        rs = pstmt.executeQuery();
	        
	        if(rs.next()) {
	        	if(rs.getInt("cnt")>0){
	        		result="C";
	        	}
	        }
	        rs.close();
	        pstmt.close();
	        
	  		rs = null;
	  		pstmt = null;
    		
	  		if(!result.equals("C")){
        		result="Y";
	  			strQuery.setLength(0);
        		strQuery.append(" update cmc0400 set cc_status = '3', \n");
        		strQuery.append(" cc_enddt = sysdate 				 \n");
        		strQuery.append(" where cc_reqid = ? 				 \n");
        		strQuery.append(" and cc_editor = ?				 \n");
        		
        		pstmt = conn.prepareStatement(strQuery.toString());
        		//pstmt = new LoggableStatement(conn,strQuery.toString());
        		pstmt.setString(1, delREQ.get("delREQinfo"));
        		pstmt.setString(2, delREQ.get("delStrUserID"));
        		//ecamsLogger.error(((LoggableStatement)pstmt).getQueryString());
        		pstmt.executeUpdate();
        		pstmt.close();
	  		}
    		
    		//pstmt.close();
    		//pstmt = null;
    		
    		conn.commit();
    		conn.close();
    		conn = null;
    		
    		return result;
    	} catch (SQLException sqlexception) {
			sqlexception.printStackTrace();
			if (rs != null)     try{rs.close();}catch (Exception ex){ex.printStackTrace();}
			if (pstmt != null)  try{pstmt.close();}catch (Exception ex2){ex2.printStackTrace();}
			if (conn != null){
				try{
					conn.rollback();
					ConnectionResource.release(conn);
				}catch(Exception ex3){
					ecamsLogger.error("## Cmc0400.delREQinfo() connection release exception ##");
					ex3.printStackTrace();
				}				
			}			
			ecamsLogger.error("## delREQinfo.setISRInfo() SQLException START ##");
			ecamsLogger.error("## Error DESC : ", sqlexception);	
			ecamsLogger.error("## delREQinfo.setISRInfo() SQLException END ##");			
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
					ecamsLogger.error("## Cmc0400.delREQinfo() connection release exception ##");
					ex3.printStackTrace();
				}				
			}			
			ecamsLogger.error("## Cmc0400.delREQinfo() Exception START ##");				
			ecamsLogger.error("## Error DESC : ", exception);	
			ecamsLogger.error("## Cmc0400.delREQinfo() Exception END ##");				
			throw exception;
		}finally{
			if (strQuery != null) 	strQuery = null;
			if (rs != null)     try{rs.close();}catch (Exception ex){ex.printStackTrace();}
			if (pstmt != null)  try{pstmt.close();}catch (Exception ex2){ex2.printStackTrace();}
			if (conn != null){
				try{
					ConnectionResource.release(conn);
					conn = null;
				}catch(Exception ex3){
					ecamsLogger.error("## Cmc0400.delREQinfo() connection release exception ##");
					ex3.printStackTrace();
				}
			}
		}
    	
    	
    	
    }
    
    public Object[] getReqInfo(String UserId, String Reqid) throws SQLException, Exception {
    	Connection        conn        = null;
    	PreparedStatement pstmt       = null;
    	ResultSet         rs          = null;
    	StringBuffer      strQuery    = new StringBuffer();
    	Object[] returnObjectArray    = null;
    	ArrayList<HashMap<String, String>>  rsval = new ArrayList<HashMap<String, String>>();
    	HashMap<String, String> rst	  = null;
    	int              parmCnt      = 0;
    	ConnectionContext connectionContext = new ConnectionResource();		
    	
    	try {
    		
    		conn = connectionContext.getConnection();
    		strQuery.setLength(0);
    		strQuery.append("select a.CC_REQID,(select cm_codename from cmm0020 where cm_macode='DOCTYPE' and cm_micode=a.cc_doctype ) as doctype,	\n");    
        	strQuery.append("a.CC_DOCNUM, a.CC_DOCSUBJ, a.CC_DOCNUM2, a.CC_DETAILJOBN,																\n");                                                         
        	strQuery.append("select cm_deptname from   cmm0100 where  cm_deptcd = a.cc_dept1) as dept1,												\n");										
        	strQuery.append("select cm_username from   cmm0040 where  cm_userid = a.cc_requser1) as caller1,										\n"); 							
        	strQuery.append("select cm_deptname from   cmm0100 where  cm_deptcd = a.cc_dept2) as dept2,												\n");										
        	strQuery.append("select cm_username from   cmm0040 where  cm_userid = a.cc_requser2) as caller2,										\n");							
        	strQuery.append("select cm_deptname from   cmm0100 where  cm_deptcd = a.cc_dept3) as owndept,											\n");									
        	strQuery.append("select cm_username from   cmm0040 where  cm_userid = a.cc_requser3) as handler,										\n");								    
        	strQuery.append("select cm_codename from cmm0020 where cm_macode='REQTYPE' and cm_micode=a.cc_reqtype) as reqtype,						\n");	
        	strQuery.append("select cm_codename from cmm0020 where cm_macode='JOBGRADE' and cm_micode=a.cc_jobdif) as jobgrade,						\n");	
        	strQuery.append("select cm_jobname from cmm0102 where cm_jobcd=cc_jobcd) as jobname,													\n");												
        	strQuery.append("select cm_codename from cmm0020 where cm_macode='CONTYPE' and cm_micode=a.cc_acttype ) as contype,						\n");	
        	strQuery.append("a.CC_ENDPLAN, a.CC_CHKTEAM, a.CC_CHKDATA, a.CC_DEVSTDT, a.CC_DEVEDDT, a.CC_DETAILSAYU, a.CC_STARTDT, a.CC_ENDDT,		\n"); 
        	strQuery.append("a.CC_STATUS, a.CC_ETTEAM, a.CC_EDITOR, a.CC_DEPT3, ISR_RECVPART(a.CC_REQID) RECVPART									\n");							
        	strQuery.append("from cmc0400 a, cmc0420 b																								\n");
            strQuery.append("where b.cc_orderid = ?																									\n");
            strQuery.append("and b.cc_reqid = a.cc_reqid																							\n");
           
    	
    		pstmt = conn.prepareStatement(strQuery.toString());
    		pstmt.setString(1, Reqid);
    		rs = pstmt.executeQuery();
            while(rs.next()) {
    	  		rst = new HashMap<String, String>();
    	  		
    	  		rst.put("cc_reqid", rs.getString("CC_REQID"));
    			rst.put("cc_doctype", rs.getString("doctype"));
    			rst.put("cc_docnum", rs.getString("CC_DOCNUM"));
    			rst.put("cc_docsubj", rs.getString("CC_DOCSUBJ"));
    			rst.put("cc_dept1", rs.getString("dept1"));
    			rst.put("cc_requser1", rs.getString("caller1"));
    			rst.put("cc_dept2", rs.getString("dept2"));
    			rst.put("cc_requser2", rs.getString("caller2"));
    			rst.put("cc_dept3", rs.getString("owndept"));
    			rst.put("cc_owndeptcd", rs.getString("cc_dept3"));
    			rst.put("cc_requser3", rs.getString("handler"));
    			rst.put("cc_docnum2", rs.getString("CC_DOCNUM2"));
    			rst.put("cc_detailjobn", rs.getString("CC_DETAILJOBN"));
    			rst.put("cc_reqtype", rs.getString("reqtype"));
    			rst.put("cc_endplan", rs.getString("CC_ENDPLAN"));
    			rst.put("cc_jobname", rs.getString("jobname"));
    			rst.put("cc_acttype", rs.getString("contype"));
    			rst.put("cc_jobdif", rs.getString("jobgrade"));
    			rst.put("cc_chkteam", rs.getString("CC_CHKTEAM"));
    			rst.put("cc_chkdata", rs.getString("CC_CHKDATA"));
    			rst.put("cc_devstdt", rs.getString("CC_DEVSTDT"));
    			rst.put("cc_deveddt", rs.getString("CC_DEVEDDT"));
    			rst.put("cc_detailsayu", rs.getString("CC_DETAILSAYU"));
    			rst.put("cc_startdt", rs.getString("CC_STARTDT"));
    			rst.put("cc_enddt", rs.getString("CC_ENDDT"));
    			rst.put("cc_status", rs.getString("CC_STATUS"));
    			rst.put("cc_editor", rs.getString("CC_EDITOR"));
    			rst.put("cc_etteam", rs.getString("CC_ETTEAM"));
    			rst.put("RECVPART", rs.getString("RECVPART"));
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
    		ecamsLogger.error("## Cmm0400.getReqInfo() SQLException START ##");
    		ecamsLogger.error("## Error DESC : ", sqlexception);	
    		ecamsLogger.error("## Cmm0400.getReqInfo() SQLException END ##");			
    		throw sqlexception;
    	} catch (Exception exception) {
    		exception.printStackTrace();
    		ecamsLogger.error("## Cmm0400.getReqInfo() Exception START ##");				
    		ecamsLogger.error("## Error DESC : ", exception);	
    		ecamsLogger.error("## Cmm0400.getReqInfo() Exception END ##");				
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
    				ecamsLogger.error("## Cmm0400.getReqInfo() connection release exception ##");
    				ex3.printStackTrace();
    			}
    		}
    	}

    }
   
    public Object[] getRequest(String OrderId) throws SQLException, Exception {
    	Connection        conn        = null;
    	PreparedStatement pstmt       = null;
    	ResultSet         rs          = null;
    	StringBuffer      strQuery    = new StringBuffer();
    	Object[] returnObjectArray    = null;
    	ArrayList<HashMap<String, String>>  rsval = new ArrayList<HashMap<String, String>>();
    	HashMap<String, String> rst	  = null;
    	int              parmCnt      = 0;
    	ConnectionContext connectionContext = new ConnectionResource();		
    	
    	try {
    		
    		conn = connectionContext.getConnection();
    		strQuery.setLength(0);
			strQuery.append("select a.CC_REQID,(select cm_codename from cmm0020 where cm_macode='DOCTYPE' and cm_micode=a.cc_doctype ) as doctype,	    \n"); 
			strQuery.append("a.CC_DOCNUM, a.CC_DOCSUBJ, a.CC_DOCNUM2, a.CC_DETAILJOBN,	a.cc_devperiod,																\n");				                                
			strQuery.append("(select cm_deptname from cmm0100 where cm_deptcd = a.cc_dept1) as dept1,													\n");		
			strQuery.append("(select cm_username from cmm0040 where cm_userid = a.cc_requser1) as caller1,												\n");		
			strQuery.append("(select cm_deptname from cmm0100 where cm_deptcd = a.cc_dept2) as dept2,													\n");			
			strQuery.append("(select cm_username from cmm0040 where cm_userid = a.cc_requser2) as caller2,												\n");					    
			strQuery.append("(select cm_codename from cmm0020 where cm_macode='REQTYPE' and cm_micode=a.cc_reqtype) as reqtype,							\n");		
			strQuery.append("(select cm_codename from cmm0020 where cm_macode='JOBGRADE' and cm_micode=a.cc_jobdif) as jobgrade,						\n");		
			strQuery.append("(select cm_jobname  from cmm0102 where cm_jobcd=cc_jobcd) as jobname,														\n");											
			strQuery.append("(select cm_codename from cmm0020 where cm_macode='CONTYPE' and cm_micode=a.cc_acttype ) as contype,						\n");			
			strQuery.append("(select cm_username from cmm0040 where cm_userid = a.cc_editor) as editornm,												\n");					    
			strQuery.append("a.CC_ENDPLAN, a.CC_DEVSTDT, a.CC_DEVEDDT, a.CC_DETAILSAYU, a.CC_STARTDT, a.CC_ENDDT,										\n");
			strQuery.append("a.CC_STATUS, a.CC_ETTEAM, a.CC_EDITOR,ISR_RECVPART(a.CC_REQID) RECVPART													\n");						
			strQuery.append("from cmc0400 a, cmc0420 b																									\n");					
			strQuery.append("where b.cc_orderid = ?																										\n");
			strQuery.append("and b.cc_reqid = a.cc_reqid																								\n");															
    	
    		pstmt = conn.prepareStatement(strQuery.toString());
    		pstmt = new LoggableStatement(conn,strQuery.toString());
    		pstmt.setString(1, OrderId);
    		ecamsLogger.error(((LoggableStatement)pstmt).getQueryString());
    		rs = pstmt.executeQuery();
            while(rs.next()) {
    	  		rst = new HashMap<String, String>();
    	  		System.out.println(rs.getString("CC_REQID"));
    	  		System.out.println(rs.getString("CC_REQID"));
    	  		System.out.println(rs.getString("CC_REQID"));
    	  		System.out.println(rs.getString("CC_REQID"));
    	  		System.out.println(rs.getString("CC_REQID"));
    	  		System.out.println(rs.getString("CC_REQID"));
    	  		System.out.println(rs.getString("CC_REQID"));
    	  		rst.put("cc_reqid", rs.getString("CC_REQID"));
    			rst.put("cc_doctype", rs.getString("doctype"));
    			rst.put("cc_docnum", rs.getString("CC_DOCNUM"));
    			rst.put("cc_docsubj", rs.getString("CC_DOCSUBJ"));
    			rst.put("cc_docnum2", rs.getString("CC_DOCNUM2"));
    			rst.put("cc_detailjobn", rs.getString("CC_DETAILJOBN"));
    			rst.put("cc_dept1", rs.getString("dept1"));
    			rst.put("cc_requser1", rs.getString("caller1"));
    			rst.put("cc_dept2", rs.getString("dept2"));
    			rst.put("cc_requser2", rs.getString("caller2"));
    			rst.put("cc_reqtype", rs.getString("reqtype"));
    			rst.put("cc_endplan", rs.getString("CC_ENDPLAN"));
    			rst.put("cc_jobname", rs.getString("jobname"));
    			rst.put("cc_acttype", rs.getString("contype"));
    			rst.put("cc_jobdif", rs.getString("jobgrade"));
    			//rst.put("cc_chkteam", rs.getString("CC_CHKTEAM"));
    			//rst.put("cc_chkdata", rs.getString("CC_CHKDATA"));
    			rst.put("cc_devstdt", rs.getString("CC_DEVSTDT"));
    			rst.put("cc_deveddt", rs.getString("CC_DEVEDDT"));
    			rst.put("cc_detailsayu", rs.getString("CC_DETAILSAYU"));
    			rst.put("cc_startdt", rs.getString("CC_STARTDT"));
    			rst.put("cc_enddt", rs.getString("CC_ENDDT"));
    			rst.put("cc_status", rs.getString("CC_STATUS"));
    			rst.put("cc_editor", rs.getString("CC_EDITOR"));
    			rst.put("cc_etteam", rs.getString("CC_ETTEAM"));
    			rst.put("RECVPART", rs.getString("RECVPART"));
    			rst.put("cc_devperiod",  rs.getString("cc_devperiod"));
    			rst.put("editornm",  rs.getString("editornm"));
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
    		ecamsLogger.error("## Cmm0400.getRequest() SQLException START ##");
    		ecamsLogger.error("## Error DESC : ", sqlexception);	
    		ecamsLogger.error("## Cmm0400.getRequest() SQLException END ##");			
    		throw sqlexception;
    	} catch (Exception exception) {
    		exception.printStackTrace();
    		ecamsLogger.error("## Cmm0400.getRequest() Exception START ##");				
    		ecamsLogger.error("## Error DESC : ", exception);	
    		ecamsLogger.error("## Cmm0400.getRequest() Exception END ##");				
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
    				ecamsLogger.error("## Cmm0400.getRequest() connection release exception ##");
    				ex3.printStackTrace();
    			}
    		}
    	}

    }
    
    public Object[] getReqRunners(String ReqId) throws SQLException, Exception {
    	Connection        conn        = null;
    	PreparedStatement pstmt       = null;
    	ResultSet         rs          = null;
    	StringBuffer      strQuery    = new StringBuffer();
    	Object[] returnObjectArray    = null;
    	ArrayList<HashMap<String, String>>  rsval = new ArrayList<HashMap<String, String>>();
    	HashMap<String, String> rst	  = null;
    	int              parmCnt      = 0;
    	ConnectionContext connectionContext = new ConnectionResource();		
    	
    	try {
    		
    		conn = connectionContext.getConnection();
    		strQuery.setLength(0);
			strQuery.append("select a.cc_reqid, b.cm_username, c.cm_deptname, 	    \n");
			strQuery.append("		a.cc_requser3, a.cc_dept3 					    		\n");
			strQuery.append("	from cmc0401 a, cmm0040 b, cmm0100 c 				\n");
			strQuery.append("		where a.cc_reqid = ? 					    			\n");
			strQuery.append("		and	  a.cc_requser3 = b.cm_userid 				\n");
			strQuery.append("		and	  a.cc_dept3 = c.cm_deptcd 					\n");
    		pstmt = conn.prepareStatement(strQuery.toString());
    		pstmt = new LoggableStatement(conn,strQuery.toString());
    		pstmt.setString(1, ReqId);
    		//ecamsLogger.error(((LoggableStatement)pstmt).getQueryString());
    		rs = pstmt.executeQuery();
            while(rs.next()) {
    	  		rst = new HashMap<String, String>();
    	  		rst.put("cc_reqid", rs.getString("cc_reqid"));
    			rst.put("cm_username", rs.getString("cm_username"));
    			rst.put("cm_deptname", rs.getString("cm_deptname"));
    			rst.put("cc_requser3", rs.getString("cc_requser3"));
    			rst.put("cc_dept3", rs.getString("cc_dept3"));

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
    		ecamsLogger.error("## Cmm0400.getReqRunners() SQLException START ##");
    		ecamsLogger.error("## Error DESC : ", sqlexception);	
    		ecamsLogger.error("## Cmm0400.getReqRunners() SQLException END ##");			
    		throw sqlexception;
    	} catch (Exception exception) {
    		exception.printStackTrace();
    		ecamsLogger.error("## Cmm0400.getReqRunners() Exception START ##");				
    		ecamsLogger.error("## Error DESC : ", exception);	
    		ecamsLogger.error("## Cmm0400.getReqRunners() Exception END ##");				
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
    				ecamsLogger.error("## Cmm0400.getReqRunners() connection release exception ##");
    				ex3.printStackTrace();
    			}
    		}
    	}

    }
 
    public Object[] statusUpdt(String userID, String reqID) throws SQLException, Exception{
    	Connection conn = null;
    	PreparedStatement pstmt = null;
    	StringBuffer strQuery = new StringBuffer();
    	String result = "N";
    	ResultSet         rs          = null;
    	Object[] returnObjectArray    = null;
		ArrayList<HashMap<String, String>>  rsval = new ArrayList<HashMap<String, String>>();
		HashMap<String, String> rst	  = null;
    	int				yn			=0;
    	
    	ConnectionContext connectionContext = new ConnectionResource();	
    	
    	try{
    		conn = connectionContext.getConnection();
    		
    		
    		strQuery.setLength(0);		        
	        strQuery.append("select count(*) as cnt from cmc0410 \n");
        	strQuery.append(" where cc_reqid = ?                 \n");
        	strQuery.append(" and cc_status not in ('9','3','0') \n");
        	
        	pstmt = conn.prepareStatement(strQuery.toString());
        	pstmt.setString(1, reqID);
        	
        	rs = pstmt.executeQuery();
        	
        	if (rs.next()){
        		yn = rs.getInt("cnt");
        	}	        	
        	rs.close();
        	pstmt.close();
    		
        	
        	if(yn<1){
	    		strQuery.setLength(0);
	    		strQuery.append(" update cmc0400 set cc_status = '9', \n");
	    		strQuery.append(" cc_enddt = sysdate 				 \n");
	    		strQuery.append(" where cc_reqid = ?                  \n");
	    		strQuery.append(" and cc_editor = ?				 \n");
	    		
	    		pstmt = conn.prepareStatement(strQuery.toString());
	    		pstmt.setString(1, reqID);
	    		pstmt.setString(2, userID);
	    		pstmt.executeUpdate();
	    		pstmt.close();
	    		
	    		pstmt = null;
	    		//conn = null;
	    		result = "Y"; ///완료처리가 되었을 경우
        	}else{
        		strQuery.setLength(0);
        		strQuery.append(" select a.cc_team                                                  \n");
        		strQuery.append("    , DECODE(LENGTH(CC_TEAM), 9, (SELECT cm_deptname               \n");
        		strQuery.append("                                    FROM CMM0100                   \n");
        		strQuery.append("                                   WHERE CM_DEPTCD = A.CC_TEAM     \n");
        		strQuery.append("                                 ),                                \n");
        		strQuery.append("                               (SELECT cm_USERNAME                 \n");
        		strQuery.append("                                    FROM CMM0040                   \n");
        		strQuery.append("                                   WHERE CM_USERID = A.CC_TEAM     \n");
        		strQuery.append("                                 )                                 \n");
        		strQuery.append("   ) CM_DEPTNAME                                                   \n");
        		strQuery.append("  from cmc0410 a                                                   \n");
        		strQuery.append(" where a.cc_reqid = ?        										\n");
        		strQuery.append(" and a.cc_status not in ('9','3','0')			                    \n");
        		pstmt = conn.prepareStatement(strQuery.toString());
	  			pstmt.setString(1, reqID);
	  			rs = pstmt.executeQuery();
	  			
	  			if(rs.next()) {
	  				
	  				rst = new HashMap<String, String>();
			  		rst.put("cc_team", rs.getString("cc_team"));
			  		rst.put("cm_deptname", rs.getString("cm_deptname"));
			 		rsval.add(rst);
	  				
	  			}
	  			
	  			rs.close();
		        pstmt.close();
		        
		        rs = null;
		  		pstmt = null;
        	}
    		
        	returnObjectArray = rsval.toArray();
			rsval.clear();
			rsval = null;
				
			conn.close();
			conn = null;
			
			return returnObjectArray;
    		
    	} catch (SQLException sqlexception) {
			sqlexception.printStackTrace();
			ecamsLogger.error("## Cmc0500.statusUpdt() SQLException START ##");
			ecamsLogger.error("## Error DESC : ", sqlexception);	
			ecamsLogger.error("## Cmc0500.statusUpdt() SQLException END ##");			
			throw sqlexception;
		} catch (Exception exception) {
			exception.printStackTrace();
			ecamsLogger.error("## Cmc0500.statusUpdt() Exception START ##");				
			ecamsLogger.error("## Error DESC : ", exception);	
			ecamsLogger.error("## Cmc0500.statusUpdt() Exception END ##");				
			throw exception;
		}finally{
			if (strQuery != null) 	strQuery = null;
			if (pstmt != null)  try{pstmt.close();}catch (Exception ex2){ex2.printStackTrace();}
			if (conn != null){
				try{
					ConnectionResource.release(conn);
				}catch(Exception ex3){
					ecamsLogger.error("## Cmc0500.statusUpdt() connection release exception ##");
					ex3.printStackTrace();
				}
			}
		}
   
    }
}//end of Cmc0300 class statement
