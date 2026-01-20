package app.eCmc;


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
import app.eCmr.*;

public class Userselect {   
	
	 Logger ecamsLogger  = EcamsLogger.getLoggerInstance();
	 
	public Object[] getUserList(String srcWord, String deptCd) throws SQLException, Exception {
		Connection        conn        = null;
		PreparedStatement pstmt       = null;
		ResultSet         rs          = null;
		StringBuffer      strQuery    = new StringBuffer();
		String		      setWord 	  = "";
		Object[] 		  returnObject= null;
		ArrayList<HashMap<String, String>>  rtList	= new ArrayList<HashMap<String, String>>();
		HashMap<String, String>			  	rst	 	= null;	
		int 								paramCnt = 1;
		ConnectionContext connectionContext = new ConnectionResource();
		
		try {
			
			rtList.clear();
			String            strDept     = "";
			String            strDept2[] = null;
			
			conn = connectionContext.getConnection();
			if(!deptCd.equals("")){
				strQuery.setLength(0);
				strQuery.append("   select cm_micode from cmm0020 where cm_macode ='INSADEPT' and cm_closedt is null \n");
				pstmt = conn.prepareStatement(strQuery.toString());				
	  		    rs = pstmt.executeQuery();
		        while (rs.next()) {
		        	if (strDept.equals("")){
		        		strDept = rs.getString("cm_micode") + ",";
	            	} else{
	            		strDept = strDept + rs.getString("cm_micode") + ",";
	            	}
		        }
		        pstmt.close();
		        rs.close();
		        
				if(strDept.substring(strDept.length()-1).indexOf(",")>-1){
					strDept = strDept.substring(0,strDept.length()-1);
				}
				strDept2 = strDept.split(",");				
				
				
				setWord = "%"+srcWord+"%";

				strQuery.setLength(0);
				strQuery.append("   select a.cm_userid, a.cm_username, a.cm_project,										\n");
				strQuery.append("   (select cm_posname from   cmm0103 b where  a.cm_position = b.cm_poscd)as cm_posname,	\n");
				strQuery.append("   (select cm_deptname from cmm0100 c where c.cm_deptcd = a.cm_project) as cm_deptname		\n");
				strQuery.append("   from   cmm0040 a 																		\n");
				strQuery.append("    where  a.cm_project in 																\n");
				strQuery.append("   (select cm_deptcd from  cmm0100 														\n");
				if(deptCd.equals("0091")){
					if (strDept2.length > 0){
						strQuery.append(" start with cm_deptcd in( \n");
						if (strDept2.length == 1)
							strQuery.append(" ? ");
						else{
							for (int i=0;i<strDept2.length;i++){
								if (i == strDept2.length-1)
									strQuery.append(" ? ");
								else
									strQuery.append(" ? ,");
							}
						}
						strQuery.append(" ) connect by prior cm_deptcd = cm_updeptcd) \n");
					}
				}else if(deptCd.equals("0093")){
					if (strDept2.length > 0){
						strQuery.append(" start with cm_deptcd in( \n");
						if (strDept2.length == 1)
							strQuery.append(" ? ");
						else{
							for (int i=0;i<strDept2.length;i++){
								if (i == strDept2.length-1)
									strQuery.append(" ? ");
								else
									strQuery.append(" ? ,");
							}
						}
						strQuery.append(" ) connect by prior cm_deptcd = cm_updeptcd) \n");
					}
				}else{
					strQuery.append("   start with cm_deptcd = ? connect by prior cm_deptcd = cm_updeptcd)  					\n");					
				}
				strQuery.append("   and    a.cm_active = '1'                                                                \n");				
				if(!srcWord.equals(""))
					strQuery.append("   and a.cm_username like ? 															\n");
				
/*
				strQuery.setLength(0);
				strQuery.append("   select a.cm_userid, a.cm_username, a.cm_project,										\n");
				strQuery.append("   (select cm_posname from   cmm0103 b where  a.cm_position = b.cm_poscd)as cm_posname,	\n");
				strQuery.append("   (select cm_deptname from cmm0100 c where c.cm_deptcd = a.cm_project) as cm_deptname		\n");
				strQuery.append("   from   cmm0040 a 																		\n");
				strQuery.append("    where  a.cm_project in 																\n");
				strQuery.append("   (select cm_deptcd from  cmm0100 														\n");
				if(deptCd.equals("0091")){
					strQuery.append("   start with cm_deptcd in('0091','0093','0960','1015001') connect by prior cm_deptcd = cm_updeptcd)  					\n");
				}else if(deptCd.equals("0093")){
					strQuery.append("   start with cm_deptcd in('0091','0093','0960','1015001') connect by prior cm_deptcd = cm_updeptcd)  					\n");
				}else{
					strQuery.append("   start with cm_deptcd = ? connect by prior cm_deptcd = cm_updeptcd)  					\n");					
				}
//				strQuery.append("   and    a.cm_manid = 'Y'                                                                 \n");
				strQuery.append("   and    a.cm_active = '1'                                                                \n");				
				if(!srcWord.equals(""))
					strQuery.append("   and a.cm_username like ? 															\n");
*/
				
				pstmt = conn.prepareStatement(strQuery.toString());
				pstmt = new LoggableStatement(conn,strQuery.toString());
			
				for (int i=0;i<strDept2.length;i++){
					pstmt.setString(paramCnt++, strDept2[i]);
				}
				if(!srcWord.equals(""))
					pstmt.setString(paramCnt++, setWord);
				ecamsLogger.error(((LoggableStatement)pstmt).getQueryString());
				
			}
			
			else {
				setWord = "%"+srcWord+"%";
				
				strQuery.append("select a.cm_username, a.cm_userid, a.cm_project, b.cm_posname, c.cm_deptname   \n"); 
				strQuery.append("  from cmm0100 c, cmm0103 b,cmm0040 a 											\n");
				strQuery.append("  where a.cm_project=c.cm_deptcd  												\n");
				strQuery.append("   and a.cm_position = b.cm_poscd												\n");
				strQuery.append("   and a.cm_active = '1'                                                       \n");				
				if(!srcWord.equals(""))
					strQuery.append("   and a.cm_username like ?	      								  	    \n");


	
				
				pstmt = conn.prepareStatement(strQuery.toString());
				pstmt = new LoggableStatement(conn,strQuery.toString());
				if(!srcWord.equals(""))
					pstmt.setString(paramCnt++, setWord);
				
				ecamsLogger.error(((LoggableStatement)pstmt).getQueryString());

				
			}	
			rs = pstmt.executeQuery();
			while (rs.next()){
				rst = new HashMap<String, String>();
	        	rst.put("cm_username", rs.getString("cm_username"));      
	        	rst.put("cm_posname", rs.getString("cm_posname"));
	        	rst.put("cm_deptname", rs.getString("cm_deptname"));
	        	rst.put("cm_project", rs.getString("cm_project"));
	        	rst.put("cm_userid", rs.getString("cm_userid"));
	        	rtList.add(rst);
        		rst = null;
        		
			}
			
			rs.close();
			pstmt.close();
			conn.close();

			rs = null;
			pstmt = null;
			conn = null;
			
			returnObject = rtList.toArray();
			rtList.clear();
			rtList = null;
			
			return returnObject;			
			
		} catch (SQLException sqlexception) {
			sqlexception.printStackTrace();
			ecamsLogger.error("## User_select SQLException START ##");
			ecamsLogger.error("## Error DESC : ", sqlexception);	
			ecamsLogger.error("## User_select SQLException END ##");			
			throw sqlexception;
		} catch (Exception exception) {
			exception.printStackTrace();
			ecamsLogger.error("## User_select Exception START ##");				
			ecamsLogger.error("## Error DESC : ", exception);	
			ecamsLogger.error("## User_select Exception END ##");				
			throw exception;
		}finally{
			if (strQuery != null) 	strQuery = null;
			if (returnObject != null)	returnObject = null;
			if (rs != null)     try{rs.close();}catch (Exception ex){ex.printStackTrace();}
			if (pstmt != null)  try{pstmt.close();}catch (Exception ex2){ex2.printStackTrace();}
			if (conn != null){
				try{
					conn.close();
				}catch(Exception ex3){
					ecamsLogger.error("## User_select connection release exception ##");
					ex3.printStackTrace();
				}
			}
		}
	}//end of SelectList() method statement

	
	public Object[] OtherUserList(String srcWord, String deptCd) throws SQLException, Exception {
		Connection        conn        = null;
		PreparedStatement pstmt       = null;
		ResultSet         rs          = null;
		StringBuffer      strQuery    = new StringBuffer();
		String		      setWord 	  = "";
		Object[] 		  returnObject= null;
		ArrayList<HashMap<String, String>>  rtList	= new ArrayList<HashMap<String, String>>();
		HashMap<String, String>			  	rst	 	= null;	
		int 								paramCnt = 1;
		ConnectionContext connectionContext = new ConnectionResource();
		
		try {
			
			rtList.clear();
			
			conn = connectionContext.getConnection();
			System.out.println("OtherUserList");
			if(!deptCd.equals("")){
				setWord = "%"+srcWord+"%";
				
				strQuery.setLength(0);
				strQuery.append("   select a.cm_userid, a.cm_username, a.cm_project,										\n");
				strQuery.append("   (select cm_posname from   cmm0103 b where  a.cm_position = b.cm_poscd)as cm_posname,	\n");
				strQuery.append("   (select cm_deptname from cmm0100 c where c.cm_deptcd = a.cm_project) as cm_deptname		\n");
				strQuery.append("   from   cmm0040 a 																		\n");
				strQuery.append("    where  a.cm_project in 																\n");
				strQuery.append("   (select cm_deptcd from  cmm0100 														\n");
				strQuery.append("   start with cm_deptcd = ? connect by prior cm_deptcd = cm_updeptcd)  					\n");
				strQuery.append("	and a.cm_manid = 'N'																	\n");
				strQuery.append("   and a.cm_active = '1'                                                                   \n");				
				if(!srcWord.equals(""))
					strQuery.append("   and a.cm_username like ? 															\n");					

				pstmt = conn.prepareStatement(strQuery.toString());
				//pstmt = new LoggableStatement(conn,strQuery.toString());
				pstmt.setString(paramCnt++, deptCd);
				if(!srcWord.equals(""))
					pstmt.setString(paramCnt++, setWord);
				//ecamsLogger.error(((LoggableStatement)pstmt).getQueryString());
				
			}
			
			else {
				setWord = "%"+srcWord+"%";
				
				strQuery.append("select a.cm_username, a.cm_userid, a.cm_project, b.cm_posname, c.cm_deptname   \n"); 
				strQuery.append("  from cmm0100 c, cmm0103 b,cmm0040 a 											\n");
				strQuery.append("  where a.cm_project=c.cm_deptcd  												\n");
				strQuery.append("   and a.cm_position = b.cm_poscd												\n");
				strQuery.append("	and a.cm_manid = 'N'												   	    \n");
				strQuery.append("   and a.cm_active = '1'                                                       \n");				
				if(!srcWord.equals(""))
					strQuery.append("   and a.cm_username like ?												\n");					
				
				pstmt = conn.prepareStatement(strQuery.toString());
				//pstmt = new LoggableStatement(conn,strQuery.toString());
				if(!srcWord.equals(""))
					pstmt.setString(paramCnt++, setWord);
				//ecamsLogger.error(((LoggableStatement)pstmt).getQueryString());

				
			}	
			rs = pstmt.executeQuery();
			while (rs.next()){
				rst = new HashMap<String, String>();
	        	rst.put("cm_username", rs.getString("cm_username"));      
	        	rst.put("cm_posname", rs.getString("cm_posname"));
	        	rst.put("cm_deptname", rs.getString("cm_deptname"));
	        	rst.put("cm_project", rs.getString("cm_project"));
	        	rst.put("cm_userid", rs.getString("cm_userid"));
	        	rtList.add(rst);
        		rst = null;
        		
			}
			
			rs.close();
			pstmt.close();
			conn.close();

			rs = null;
			pstmt = null;
			conn = null;
			
			returnObject = rtList.toArray();
			rtList.clear();
			rtList = null;
			
			return returnObject;			
			
		} catch (SQLException sqlexception) {
			sqlexception.printStackTrace();
			ecamsLogger.error("## OtherUserList SQLException START ##");
			ecamsLogger.error("## Error DESC : ", sqlexception);	
			ecamsLogger.error("## OtherUserList SQLException END ##");			
			throw sqlexception;
		} catch (Exception exception) {
			exception.printStackTrace();
			ecamsLogger.error("## OtherUserList Exception START ##");				
			ecamsLogger.error("## Error DESC : ", exception);	
			ecamsLogger.error("## OtherUserList Exception END ##");				
			throw exception;
		}finally{
			if (strQuery != null) 	strQuery = null;
			if (returnObject != null)	returnObject = null;
			if (rs != null)     try{rs.close();}catch (Exception ex){ex.printStackTrace();}
			if (pstmt != null)  try{pstmt.close();}catch (Exception ex2){ex2.printStackTrace();}
			if (conn != null){
				try{
					conn.close();
				}catch(Exception ex3){
					ecamsLogger.error("## OtherUserList connection release exception ##");
					ex3.printStackTrace();
				}
			}
		}
	}//end of OtherUserList() method statement

}
