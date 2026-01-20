
/*****************************************************************************************
	1. program ID	: TeamInfo
	2. create date	: 2008.05. 26
	3. auth		    : 
	4. update date	: 
	5. auth		    : 
	6. description	: TeamInfo
*****************************************************************************************/

package app.common;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.logging.log4j.Logger;
import org.w3c.dom.Document;

import com.ecams.common.dbconn.ConnectionContext;
import com.ecams.common.dbconn.ConnectionResource;
import com.ecams.common.logger.EcamsLogger;



/**
 * @author bigeyes
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */


public class TeamInfo{
	
    /**
     * Logger Class Instance Creation
     * logger
     */
    Logger ecamsLogger  = EcamsLogger.getLoggerInstance();
    
	/**
	 * 조직도 트리
	 * @param  
	 * @return Object[]
	 * @throws SQLException
	 * @throws Exception
	 */
 
    public Object[] getTeamInfoGrid2(String SelMsg,String cm_useyn,String gubun,String itYn) throws SQLException, Exception {
		Connection        conn        = null;
		PreparedStatement pstmt       = null;
		ResultSet         rs          = null;
		StringBuffer      strQuery    = new StringBuffer();
		Object[]		  rtObj		  = null;
		ArrayList<HashMap<String, String>>	rtList  = new ArrayList<HashMap<String, String>>();
		HashMap<String, String>			  	rst	 	= null;
		String            strSelMsg   = "";
		int				  index = 0;
		
		ConnectionContext connectionContext = new ConnectionResource();
		
		try {
			conn = connectionContext.getConnection();
			
			if (SelMsg != "" && SelMsg != null) {
				if (SelMsg.toUpperCase().equals("ALL")){
					strSelMsg = "전체";
				}
				else if(SelMsg.toUpperCase().equals("SEL")){
					strSelMsg = "선택하세요";
				}
				else{
					strSelMsg   = "";
				}	
			}
			
			strQuery.setLength(0);
			strQuery.append("select cm_deptcd,cm_deptname \n");
			
			
			if (itYn.equals("Y")) {
				strQuery.append(" from (Select cm_deptcd,cm_updeptcd,cm_deptname,cm_useyn \n");
				strQuery.append("         from (Select * from cmm0100 where cm_useyn='Y') \n");
				//strQuery.append("        start with cm_updeptcd='000010505'  				  \n");
				strQuery.append("        start with cm_dept_code in ('0091','0093','0960') 	  \n");
				strQuery.append("       connect by prior cm_deptcd = cm_updeptcd) 		  \n");
				strQuery.append(" where cm_useyn='Y'                          		      \n");
			} else if (!"".equals(gubun) && gubun != null) {
				if (gubun.equals("DEPT")) {
					strQuery.append(" from (Select cm_deptcd,cm_updeptcd,cm_deptname,cm_useyn \n");
					strQuery.append("         from (Select * from cmm0100  \n");
					strQuery.append("                where cm_useyn='Y'    \n");
					strQuery.append("                  and cm_updeptcd is not null) \n");
					strQuery.append("        start with cm_deptcd in (select cm_project from cmm0040  \n");
					strQuery.append("                                  where cm_active='1'    \n");
					strQuery.append("                                  group by cm_project)   \n");
					strQuery.append("       connect by prior cm_updeptcd = cm_deptcd) 		  \n");
					strQuery.append(" where cm_useyn='Y'                          		      \n");
				} else if (gubun.equals("main")){
					strQuery.append("  from cmm0100 \n");
					strQuery.append(" where cm_useyn='Y' \n");
					strQuery.append(" and CM_UPDEPTCD is null \n");
					//if (itYn.equals("Y")) strQuery.append("and cm_deptcd like '10%'    \n");
				} else if (gubun.equals("sub")) {
					strQuery.append("  from cmm0100 \n");
					strQuery.append(" where cm_useyn='Y' \n");
					strQuery.append("   and cm_deptcd in (select cm_project from cmm0040  \n");
					strQuery.append("                      where cm_active='1'            \n");
					strQuery.append("                      group by cm_project)           \n");
				} else {
					strQuery.append("  from cmm0100 \n");
					strQuery.append(" where cm_useyn='Y' \n");
					strQuery.append(" and CM_UPDEPTCD = ?                               \n");
				}
			} else {
				strQuery.append("  from cmm0100 \n");
				strQuery.append(" where cm_useyn='Y' \n");
			}
			strQuery.append(" group by cm_deptcd,cm_deptname \n"); 	
			strQuery.append(" order by cm_deptcd,cm_deptname \n"); 			

            pstmt = conn.prepareStatement(strQuery.toString());
            pstmt = new LoggableStatement(conn,strQuery.toString());
            if(!"".equals(gubun) && gubun != null) {
            	if (!gubun.equals("main") && !gubun.equals("sub") && !gubun.equals("DEPT")){
            		pstmt.setString(1, gubun);
            	}
            }
           ecamsLogger.error(((LoggableStatement)pstmt).getQueryString());  
            rs = pstmt.executeQuery();
            
            rtList.clear();
            
			while (rs.next()){
				if (rs.getRow() ==1 && !strSelMsg.equals("") && strSelMsg.length() > 0) {
					rst = new HashMap<String, String>();
					rst.put("ID", "0");
					rst.put("cm_deptname", strSelMsg);
					rst.put("cm_deptcd", "");
					rtList.add(rst);
					rst = null;
				}	
				index = 0;
				if(rs.getString("cm_deptname").indexOf(" ") != -1){
					index = rs.getString("cm_deptname").indexOf(" ")+1;
				}
				rst = new HashMap<String,String>();
				rst.put("ID", Integer.toString(rs.getRow()));
				rst.put("cm_deptcd", rs.getString("cm_deptcd"));
				rst.put("cm_deptname", rs.getString("cm_deptname").substring(index));
				rtList.add(rst);
				rst = null;
			}//end of while-loop statement
			rs.close();
			pstmt.close();
			conn.close();
			
			rs = null;
			pstmt = null;
			conn = null;
			
			rtObj =  rtList.toArray();
			rtList = null;
			return rtObj;
			
		} catch (SQLException sqlexception) {
			sqlexception.printStackTrace();
			ecamsLogger.error("## TeamInfo.getTeamInfoGrid() SQLException START ##");
			ecamsLogger.error("## Error DESC : ", sqlexception);	
			ecamsLogger.error("## TeamInfo.getTeamInfoGrid() SQLException END ##");
			throw sqlexception;
		} catch (Exception exception) {
			exception.printStackTrace();
			ecamsLogger.error("## TeamInfo.getTeamInfoGrid() Exception START ##");				
			ecamsLogger.error("## Error DESC : ", exception);	
			ecamsLogger.error("## TeamInfo.getTeamInfoGrid() Exception END ##");
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
					ecamsLogger.error("## TeamInfo.getTeamInfoGrid() connection release exception ##");
					ex3.printStackTrace();
				}
			}
		}
		

		
	}//end of getTeamInfoGrid() method statement	
    
	
	public Object[] getTeamInfoGrid(String SelMsg,String cm_useyn) throws SQLException, Exception {
		Connection        conn        = null;
		PreparedStatement pstmt       = null;
		ResultSet         rs          = null;
		StringBuffer      strQuery    = new StringBuffer();
		Object[]		  rtObj		  = null;
		ArrayList<HashMap<String, String>>	rtList  = new ArrayList<HashMap<String, String>>();
		HashMap<String, String>			  	rst	 	= null;
		String            strSelMsg   = "";
		
		ConnectionContext connectionContext = new ConnectionResource();
		
		try {
			conn = connectionContext.getConnection();
			
			if (SelMsg != "" && SelMsg != null) {
				if (SelMsg.toUpperCase().equals("ALL")){
					strSelMsg = "전체";
				}
				else if(SelMsg.toUpperCase().equals("SEL")){
					strSelMsg = "선택하세요";
				}
				else{
					strSelMsg   = "";
				}
			}			
			
			strQuery.append("select cm_deptcd,cm_deptname \n");
			strQuery.append("  from cmm0100               \n");
			strQuery.append(" where cm_useyn='Y'          \n");
			strQuery.append("   and cm_deptcd in (select cm_project from cmm0040  \n");
			strQuery.append("                      where cm_active='1'            \n");
			strQuery.append("                      group by cm_project)           \n");
			strQuery.append("order by cm_deptname         \n"); 			

            pstmt = conn.prepareStatement(strQuery.toString());	
            
            rs = pstmt.executeQuery();
            
            rtList.clear();
            
			while (rs.next()){
				if (rs.getRow() ==1 && strSelMsg != "" && !strSelMsg.equals("") && strSelMsg.length() > 0) {
					rst = new HashMap<String, String>();
					rst.put("ID", "0");
					rst.put("cm_deptname", strSelMsg);
					rst.put("cm_deptcd", "");
					rtList.add(rst);
					rst = null;
				}	
				rst = new HashMap<String,String>();
				rst.put("ID", Integer.toString(rs.getRow()));
				rst.put("cm_deptcd", rs.getString("cm_deptcd"));
				rst.put("cm_deptname", rs.getString("cm_deptname"));
				rtList.add(rst);
				rst = null;
			}//end of while-loop statement
			rs.close();
			pstmt.close();
			conn.close();
			
			rs = null;
			pstmt = null;
			conn = null;
			
			rtObj =  rtList.toArray();
			rtList = null;
			return rtObj;
			
		} catch (SQLException sqlexception) {
			sqlexception.printStackTrace();
			ecamsLogger.error("## TeamInfo.getTeamInfoGrid() SQLException START ##");
			ecamsLogger.error("## Error DESC : ", sqlexception);	
			ecamsLogger.error("## TeamInfo.getTeamInfoGrid() SQLException END ##");
			throw sqlexception;
		} catch (Exception exception) {
			exception.printStackTrace();
			ecamsLogger.error("## TeamInfo.getTeamInfoGrid() Exception START ##");				
			ecamsLogger.error("## Error DESC : ", exception);	
			ecamsLogger.error("## TeamInfo.getTeamInfoGrid() Exception END ##");
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
					ecamsLogger.error("## TeamInfo.getTeamInfoGrid() connection release exception ##");
					ex3.printStackTrace();
				}
			}
		}
		

		
	}//end of getTeamInfoGrid() method statement	
	
	public Object[] getTeam(String SelMsg,String cm_useyn) throws SQLException, Exception {
		Connection        conn        = null;
		PreparedStatement pstmt       = null;
		ResultSet         rs          = null;
		StringBuffer      strQuery    = new StringBuffer();
		Object[]		  rtObj		  = null;
		ArrayList<HashMap<String, String>>	rtList  = new ArrayList<HashMap<String, String>>();
		HashMap<String, String>			  	rst	 	= null;
		String            strSelMsg   = "";
		
		ConnectionContext connectionContext = new ConnectionResource();
		
		try {
			conn = connectionContext.getConnection();
			
			if (SelMsg != "" && SelMsg != null) {
				if (SelMsg.toUpperCase().equals("ALL")){
					strSelMsg = "전체";
				}
				else if(SelMsg.toUpperCase().equals("SEL")){
					strSelMsg = "선택하세요";
				}
				else{
					strSelMsg   = "";
				}
			}			
			
			strQuery.append("select cm_deptcd,cm_deptname 	\n");
			strQuery.append("  from cmm0100               	\n");
			strQuery.append(" where cm_useyn='Y'          	\n");
			strQuery.append("and cm_dept_code is not null  	\n");
			strQuery.append("and cm_dept_code in ('0000','0010','0091','0093','0960') \n");
			strQuery.append("order by cm_deptcd \n");

            pstmt = conn.prepareStatement(strQuery.toString());	
            
            rs = pstmt.executeQuery();
            
            rtList.clear();
            
			while (rs.next()){
				if (rs.getRow() ==1 && strSelMsg != "" && !strSelMsg.equals("") && strSelMsg.length() > 0) {
					rst = new HashMap<String, String>();
					rst.put("ID", "0");
					rst.put("cm_deptname", strSelMsg);
					rst.put("cm_deptcd", "");
					rtList.add(rst);
					rst = null;
				}	
				rst = new HashMap<String,String>();
				rst.put("ID", Integer.toString(rs.getRow()));
				rst.put("cm_deptcd", rs.getString("cm_deptcd"));
				rst.put("cm_deptname", rs.getString("cm_deptname"));
				rtList.add(rst);
				rst = null;
			}//end of while-loop statement
			rs.close();
			pstmt.close();
			conn.close();
			
			rs = null;
			pstmt = null;
			conn = null;
			
			rtObj =  rtList.toArray();
			rtList = null;
			return rtObj;
			
		} catch (SQLException sqlexception) {
			sqlexception.printStackTrace();
			ecamsLogger.error("## TeamInfo.getTeamInfoGrid() SQLException START ##");
			ecamsLogger.error("## Error DESC : ", sqlexception);	
			ecamsLogger.error("## TeamInfo.getTeamInfoGrid() SQLException END ##");
			throw sqlexception;
		} catch (Exception exception) {
			exception.printStackTrace();
			ecamsLogger.error("## TeamInfo.getTeamInfoGrid() Exception START ##");				
			ecamsLogger.error("## Error DESC : ", exception);	
			ecamsLogger.error("## TeamInfo.getTeamInfoGrid() Exception END ##");
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
					ecamsLogger.error("## TeamInfo.getTeamInfoGrid() connection release exception ##");
					ex3.printStackTrace();
				}
			}
		}
		

		
	}//end of getTeamInfoGrid() method statement	

	public Document getTeamInfoTree_old() throws SQLException, Exception {
		Connection        conn        = null;
		PreparedStatement pstmt       = null;
		ResultSet         rs          = null;
		StringBuffer      strQuery    = new StringBuffer();
		CreateXml		  myXml		  = new CreateXml();
		
		ConnectionContext connectionContext = new ConnectionResource();
		
		try {
			conn = connectionContext.getConnection();
			strQuery.append("Select cm_deptcd, cm_updeptcd, cm_deptname        \n");
			strQuery.append("from (Select * from cmm0100 where cm_useyn='Y')   \n");
			strQuery.append("start with cm_updeptcd is null  				   \n");
			strQuery.append("connect by prior cm_deptcd = cm_updeptcd 		   \n");
			
            pstmt = conn.prepareStatement(strQuery.toString());	
            
            myXml.init_Xml("ID","cm_deptcd","cm_deptname","cm_updeptcd");
            
            rs = pstmt.executeQuery();
            
            while (rs.next()){
				myXml.addXML(rs.getString("cm_deptcd"),
						rs.getString("cm_deptcd"), 
						rs.getString("cm_deptname"),
						rs.getString("cm_updeptcd"));
			}//end of while-loop statement
            rs.close();
            pstmt.close();
            conn.close();

			rs = null;
			pstmt = null;
			conn = null;
			
            return myXml.getDocument();
			
		} catch (SQLException sqlexception) {
			sqlexception.printStackTrace();
			ecamsLogger.error("## TeamInfo.getTeamInfoTree() SQLException START ##");
			ecamsLogger.error("## Error DESC : ", sqlexception);	
			ecamsLogger.error("## TeamInfo.getTeamInfoTree() SQLException END ##");
			throw sqlexception;
		} catch (Exception exception) {
			exception.printStackTrace();
			ecamsLogger.error("## TeamInfo.getTeamInfoTree() Exception START ##");				
			ecamsLogger.error("## Error DESC : ", exception);	
			ecamsLogger.error("## TeamInfo.getTeamInfoTree() Exception END ##");
			throw exception;
		}finally{
			if (myXml != null)	myXml = null;
			if (strQuery != null) 	strQuery = null;
			if (rs != null)     try{rs.close();}catch (Exception ex){ex.printStackTrace();}
			if (pstmt != null)  try{pstmt.close();}catch (Exception ex2){ex2.printStackTrace();}
			if (conn != null){
				try{
					ConnectionResource.release(conn);
				}catch(Exception ex3){
					ecamsLogger.error("## TeamInfo.getTeamInfoTree() connection release exception ##");
					ex3.printStackTrace();
				}
			}
		}
		
		
	}//end of getTeamInfoTree() method statement

	
	public Document getTeamInfoTree(boolean chkcd,boolean itsw) throws SQLException, Exception {
		Connection        conn        = null;
		PreparedStatement pstmt       = null;
		ResultSet         rs          = null;
		StringBuffer      strQuery    = new StringBuffer();
		CreateXml		  myXml		  = new CreateXml();
		int 								paramCnt = 1;
		int               j = 0;
		
		ConnectionContext connectionContext = new ConnectionResource();
		
		try {
			
			conn = connectionContext.getConnection();

			/*   20160425 수정
			strQuery.append("Select cm_deptcd, cm_updeptcd, cm_deptname        \n");
			strQuery.append("from (Select * from cmm0100 where cm_useyn='Y'    \n");
			//strQuery.append("		and (cm_deptcd='000010535' or cm_deptcd = '000010505' or cm_deptcd ='000018305' or cm_updeptcd='000010505' or cm_updeptcd ='000019817' or cm_updeptcd ='000019818' or cm_updeptcd is null))    \n");
			strQuery.append("		and (cm_dept_code in ('0000','0010','0091','0092')))    \n");			
			strQuery.append("start with cm_updeptcd is null  				   \n");
			strQuery.append("connect by prior cm_deptcd = cm_updeptcd 		   \n");
			*/


			String            strDept     = "";
			String            strDept2[] = null;

			strQuery.setLength(0);
			strQuery.append("select cm_micode from cmm0020 where cm_macode ='INSADEPT' and cm_closedt is null \n");
			pstmt = conn.prepareStatement(strQuery.toString());				
  		    rs = pstmt.executeQuery();
	        while (rs.next()) {
	          	if (strDept != "") strDept = strDept + ",";
	          	strDept = strDept + rs.getString("cm_micode");          
	        }
	        pstmt.close();
	        rs.close();

			ecamsLogger.error("strDept >>>>> " + strDept);
			strDept2 = strDept.split(",");		
			
/*			
			strQuery.append("Select cm_deptcd, cm_updeptcd, cm_deptname        \n");
			strQuery.append("from cmm0100 where cm_dept_code in ('0000','0010','0091','0093','0960','1015001') and cm_deptcd not in('00002')  and cm_useyn ='Y'  \n");			
			strQuery.append("start with cm_updeptcd is null  				   \n");
			strQuery.append("connect by prior cm_deptcd = cm_updeptcd 		   \n");
*/
			strQuery.setLength(0);
			strQuery.append("Select cm_deptcd, cm_updeptcd, cm_deptname from cmm0100 where        \n");
			
			if (strDept2.length > 0){
				strQuery.append(" cm_dept_code in ( \n");
				for (j=0;strDept2.length>j;j++) {
					if (j>0) strQuery.append(", ? ");
					else strQuery.append("? ");
				}
				strQuery.append(" ) and cm_deptcd not in('00002')  and cm_useyn ='Y'  \n");
			}
			strQuery.append("start with cm_updeptcd is null  				   \n");
			strQuery.append("connect by prior cm_deptcd = cm_updeptcd 		   \n");
            pstmt = conn.prepareStatement(strQuery.toString());
            pstmt = new LoggableStatement(conn,strQuery.toString());
        	
            if (chkcd == true) {
            	myXml.init_Xml("ID","cm_deptcd","cm_deptname","checked","enabled","cm_updeptcd"); 
            } else {
            	myXml.init_Xml("ID","cm_deptcd","cm_deptname","cm_updeptcd"); 
            }
			for (int i=0;i<strDept2.length;i++){
				pstmt.setString(paramCnt++, strDept2[i]);
			}
			ecamsLogger.error(((LoggableStatement)pstmt).getQueryString());
            rs = pstmt.executeQuery();
			
            while (rs.next()){
            	if (chkcd == true) {
					myXml.addXML(rs.getString("cm_deptcd"),
							rs.getString("cm_deptcd"), 
							rs.getString("cm_deptname"),
							"false",
							"true",
							rs.getString("cm_updeptcd"));
            	} else {
					myXml.addXML(rs.getString("cm_deptcd"),
							rs.getString("cm_deptcd"), 
							rs.getString("cm_deptname"),
							rs.getString("cm_updeptcd"));
            	}
			}//end of while-loop statement
            rs.close();
            pstmt.close();
            conn.close();

			rs = null;
			pstmt = null;
			conn = null;
			
            return myXml.getDocument();
			
		} catch (SQLException sqlexception) {
			sqlexception.printStackTrace();
			ecamsLogger.error("## TeamInfo.getTeamInfoTreeCheckBox() SQLException START ##");
			ecamsLogger.error("## Error DESC : ", sqlexception);	
			ecamsLogger.error("## TeamInfo.getTeamInfoTreeCheckBox() SQLException END ##");
			throw sqlexception;
		} catch (Exception exception) {
			exception.printStackTrace();
			ecamsLogger.error("## TeamInfo.getTeamInfoTreeCheckBox() Exception START ##");				
			ecamsLogger.error("## Error DESC : ", exception);	
			ecamsLogger.error("## TeamInfo.getTeamInfoTreeCheckBox() Exception END ##");
			throw exception;
		}finally{
			if (myXml != null)	myXml = null;
			if (strQuery != null) 	strQuery = null;
			if (rs != null)     try{rs.close();}catch (Exception ex){ex.printStackTrace();}
			if (pstmt != null)  try{pstmt.close();}catch (Exception ex2){ex2.printStackTrace();}
			if (conn != null){
				try{
					ConnectionResource.release(conn);
				}catch(Exception ex3){
					ecamsLogger.error("## TeamInfo.getTeamInfoTreeCheckBox() connection release exception ##");
					ex3.printStackTrace();
				}
			}
		}
	}//end of getTeamInfoTreeCheckBox() method statement
	
	public Document getTeamUsrInfoTree(boolean chkcd,boolean itsw,ArrayList<HashMap<String,String>> TeamList) throws SQLException, Exception {
		Connection        conn        = null;
		PreparedStatement pstmt       = null;
		ResultSet         rs          = null;
		StringBuffer      strQuery    = new StringBuffer();
		CreateXml		  myXml		  = new CreateXml();
		int				  pstmtcount  = 0;
		
		ConnectionContext connectionContext = new ConnectionResource();
		
		try {
			
			conn = connectionContext.getConnection();
			
			strQuery.append("Select cm_deptcd, cm_updeptcd, cm_deptname        \n");
			strQuery.append("from (Select cm_deptcd, cm_updeptcd, cm_deptname from cmm0100 where cm_useyn='Y' and cm_updeptcd is null	\n");
			strQuery.append("	union																		    						\n");
			strQuery.append("	Select a.cm_deptcd, a.cm_updeptcd, a.cm_deptname from cmm0100 a, cmm0040 b where a.cm_useyn='Y'   		\n");
			strQuery.append("	and b.cm_userid in (																					\n");
			if(TeamList.size()==1){
				strQuery.append("?																										\n");
			}else{
				strQuery.append("?																										\n");
				for(int i=1;i<TeamList.size();i++){
					strQuery.append(",?																									\n");
				}
			}
			strQuery.append(")																											\n");
			strQuery.append("	and b.cm_project = a.cm_deptcd																		    					\n");
			strQuery.append("	union																		    						\n");
			strQuery.append("	Select a.cm_userid, a.cm_project, a.cm_username from cmm0040 a, cmm0043 b 								\n");
			strQuery.append("	where a.cm_active = '1' and a.cm_userid = b.cm_userid and b.cm_rgtcd in (								\n");
			if(TeamList.size()==1){
				strQuery.append("?																										\n");
			}else{
				strQuery.append("?																										\n");
				for(int i=1;i<TeamList.size();i++){
					strQuery.append(",?																									\n");
				}
			}
			strQuery.append(")																											\n");
			strQuery.append("	and a.cm_userid in (																					\n");
			if(TeamList.size()==1){
				strQuery.append("?																										\n");
			}else{
				strQuery.append("?																										\n");
				for(int i=1;i<TeamList.size();i++){
					strQuery.append(",?																									\n");
				}
			}
			strQuery.append("))																											\n");
			strQuery.append("start with cm_updeptcd is null  				   															\n");
			strQuery.append("connect by prior cm_deptcd = cm_updeptcd 		   															\n");
			
            pstmt = conn.prepareStatement(strQuery.toString());	
            //pstmt =  new LoggableStatement(conn, strQuery.toString());
            if(TeamList.size()==1){
            	pstmt.setString(++pstmtcount,TeamList.get(0).get("cm_baseuser"));
            }else{
            	for(int i=0;i<TeamList.size();i++){
                	pstmt.setString(++pstmtcount,TeamList.get(i).get("cm_baseuser"));
                }
            }
            if(TeamList.size()==1){
            	pstmt.setString(++pstmtcount,TeamList.get(0).get("cm_duty"));
            }else{
            	for(int i=0;i<TeamList.size();i++){
                	pstmt.setString(++pstmtcount,TeamList.get(i).get("cm_duty"));
                }
            }
            if(TeamList.size()==1){
            	pstmt.setString(++pstmtcount,TeamList.get(0).get("cm_baseuser"));
            }else{
            	for(int i=0;i<TeamList.size();i++){
                	pstmt.setString(++pstmtcount,TeamList.get(i).get("cm_baseuser"));
                }
            }
            
            //ecamsLogger.error(((LoggableStatement)pstmt).getQueryString());       
            
            if (chkcd == true) {
            	myXml.init_Xml("ID","cm_deptcd","cm_deptname","checked","enabled","cm_updeptcd"); 
            } else {
            	myXml.init_Xml("ID","cm_deptcd","cm_deptname","cm_updeptcd"); 
            }
            rs = pstmt.executeQuery();
            
            while (rs.next()){
            	if (chkcd == true) {
					myXml.addXML(rs.getString("cm_deptcd"),
							rs.getString("cm_deptcd"), 
							rs.getString("cm_deptname"),
							"false",
							"true",
							rs.getString("cm_updeptcd"));
            	} else {
					myXml.addXML(rs.getString("cm_deptcd"),
							rs.getString("cm_deptcd"), 
							rs.getString("cm_deptname"),
							rs.getString("cm_updeptcd"));
            	}
			}//end of while-loop statement
            rs.close();
            pstmt.close();
            conn.close();

			rs = null;
			pstmt = null;
			conn = null;
			
            return myXml.getDocument();
			
		} catch (SQLException sqlexception) {
			sqlexception.printStackTrace();
			ecamsLogger.error("## TeamInfo.getTeamInfoTreeCheckBox() SQLException START ##");
			ecamsLogger.error("## Error DESC : ", sqlexception);	
			ecamsLogger.error("## TeamInfo.getTeamInfoTreeCheckBox() SQLException END ##");
			throw sqlexception;
		} catch (Exception exception) {
			exception.printStackTrace();
			ecamsLogger.error("## TeamInfo.getTeamInfoTreeCheckBox() Exception START ##");				
			ecamsLogger.error("## Error DESC : ", exception);	
			ecamsLogger.error("## TeamInfo.getTeamInfoTreeCheckBox() Exception END ##");
			throw exception;
		}finally{
			if (myXml != null)	myXml = null;
			if (strQuery != null) 	strQuery = null;
			if (rs != null)     try{rs.close();}catch (Exception ex){ex.printStackTrace();}
			if (pstmt != null)  try{pstmt.close();}catch (Exception ex2){ex2.printStackTrace();}
			if (conn != null){
				try{
					ConnectionResource.release(conn);
				}catch(Exception ex3){
					ecamsLogger.error("## TeamInfo.getTeamInfoTreeCheckBox() connection release exception ##");
					ex3.printStackTrace();
				}
			}
		}
	}//end of getTeamInfoTreeCheckBox() method statement

	///////////////////////////////////////////////////////////////////////
	// 조직도 트리의 노드 클릭시 부서에 포함된 사원조회 쿼리 
	// UserName,DeptCd,DeptName
	// 1) UserName,"",""  => 성명으로 검색 
	// 2) "",DeptCd,""    => 조직도 tree's node(부서) click 시 부서의 구성원 검색 
	// 3) "","",DeptName  => 부서명으로 검색
	///////////////////////////////////////////////////////////////////////
	public Object[] getTeamNodeInfo(String UserName,String DeptCd,String DeptName) throws SQLException, Exception {
		Connection        conn        = null;
		PreparedStatement pstmt       = null;
		ResultSet         rs          = null;
		StringBuffer      strQuery    = new StringBuffer();
		Object[]		  rtObj		  = null;
		ArrayList<HashMap<String, String>>	rtList = new ArrayList<HashMap<String, String>>();
		HashMap<String, String>			  rst	   = null;
		int				  Cnt         = 0;
		
		ConnectionContext connectionContext = new ConnectionResource();
		try {
			conn = connectionContext.getConnection();

			strQuery.append("Select A.CM_USERID,A.CM_PROJECT,A.CM_USERNAME,A.CM_DUTY, \n");
			strQuery.append("B.CM_codeNAME POSITION,D.cm_seqno,C.CM_DEPTNAME,D.CM_CODENAME DUTY \n");
			strQuery.append("FROM cmm0040 A,CMM0020 B,CMM0100 C,CMM0020 D \n");
			strQuery.append("WHERE A.CM_ACTIVE = '1' \n");
		    if (UserName != null && !"".equals(UserName))	strQuery.append("AND A.CM_USERNAME LIKE ? \n");  //UserName
		    else if (DeptCd != null && !"".equals(DeptCd))	strQuery.append("and A.CM_PROJECT = ? \n");  //DeptCd
		    else if (DeptName != null && !"".equals(DeptName)) {
		    	strQuery.append("and A.CM_PROJECT in (select CM_DEPTCD from cmm0100 \n");
		    	strQuery.append("                      where  CM_USEYN='Y' \n");
		    	strQuery.append("                        AND  CM_DEPTNAME LIKE ?) \n");  //DeptName
		    }
		    else	strQuery.append("and A.CM_PROJECT in (select CM_DEPTCD from cmm0100 where  CM_USEYN='Y') \n");
		    strQuery.append("AND b.cm_macode='POSITION' and b.cm_micode=A.CM_POSITION  \n");
		    strQuery.append("AND D.CM_MACODE='DUTY' AND D.CM_MICODE=A.CM_DUTY \n");
		    strQuery.append("AND A.CM_PROJECT=C.CM_DEPTCD \n");
		    strQuery.append("ORDER BY B.CM_SEQNO,a.cm_userid \n");
		    //pstmt = new LoggableStatement(conn,strQuery.toString());
            pstmt = conn.prepareStatement(strQuery.toString());
		    
            Cnt = 0;
            if (UserName != null && !"".equals(UserName))	pstmt.setString(++Cnt, "%"+UserName+"%");   	
            else if (DeptCd != null && !"".equals(DeptCd))	pstmt.setString(++Cnt, DeptCd);
            else if (DeptName != null && !"".equals(DeptName))	pstmt.setString(++Cnt, "%"+DeptName+"%");
            //ecamsLogger.error(((LoggableStatement)pstmt).getQueryString());  
            rs = pstmt.executeQuery();
            rtList.clear();
			while (rs.next()){
				rst = new HashMap<String, String>();
				rst.put("rows", Integer.toString(rs.getRow()));
				rst.put("POSITION",rs.getString("POSITION"));		//직급명
				rst.put("CM_USERNAME",rs.getString("CM_USERNAME"));	//사용자명
				rst.put("DUTY",rs.getString("DUTY"));				//호칭
				rst.put("TEAMNAME",rs.getString("CM_DEPTNAME"));	//팀이름
				rst.put("CM_USERID",rs.getString("CM_USERID"));		//사용자ID
				rst.put("TEAMCD",rs.getString("CM_PROJECT"));		//팀코드
				rst.put("CM_SEQNO",rs.getString("CM_SEQNO"));		//직급명코드
				rst.put("JIKMOO","");								//참여직무
				rtList.add(rst);
				rst = null;
			}//end of while-loop statement
			rs.close();
			pstmt.close();
			conn.close();

			rs = null;
			pstmt = null;
			conn = null;
			
			rtObj =  rtList.toArray();
			rtList = null;
			return rtObj;			
			
		} catch (SQLException sqlexception) {
			sqlexception.printStackTrace();
			ecamsLogger.error("## TeamInfo.getTeamNodeInfo() SQLException START ##");
			ecamsLogger.error("## Error DESC : ", sqlexception);
			ecamsLogger.error("## TeamInfo.getTeamNodeInfo() SQLException END ##");
			throw sqlexception;
		} catch (Exception exception) {
			exception.printStackTrace();
			ecamsLogger.error("## TeamInfo.getTeamNodeInfo() Exception START ##");
			ecamsLogger.error("## Error DESC : ", exception);
			ecamsLogger.error("## TeamInfo.getTeamNodeInfo() Exception END ##");
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
					ecamsLogger.error("## TeamInfo.getTeamNodeInfo() connection release exception ##");
					ex3.printStackTrace();
				}
			}
		}		

		
	}//end of getTeamNodeInfo() method statement
	
	public ArrayList<HashMap<String, String>> getTeamInfoTree_zTree(boolean chkcd,boolean itsw) throws SQLException, Exception {
		Connection        conn        = null;
		PreparedStatement pstmt       = null;
		ResultSet         rs          = null;
		StringBuffer      strQuery    = new StringBuffer();
		CreateXml		  myXml		  = new CreateXml();
		int 			  paramCnt 	  = 1;
		int               j 		  = 0;
		
		HashMap<String, String> teamInfoMap 			= null;
		ArrayList<HashMap<String, String>> teamInfoArr 	= new ArrayList<HashMap<String, String>>();
		
		ConnectionContext connectionContext = new ConnectionResource();
		
		try {
			
			conn = connectionContext.getConnection();

			String strDept     	= "";
			String strDept2[]	= null;

			strQuery.setLength(0);
			strQuery.append("select cm_micode from cmm0020 where cm_macode ='INSADEPT' and cm_closedt is null \n");
			pstmt = conn.prepareStatement(strQuery.toString());				
  		    rs = pstmt.executeQuery();
	        while (rs.next()) {
	          	if (strDept != "") strDept = strDept + ",";
	          	strDept = strDept + rs.getString("cm_micode");          
	        }
	        pstmt.close();
	        rs.close();

			ecamsLogger.error("strDept >>>>> " + strDept);
			strDept2 = strDept.split(",");		
			
			strQuery.setLength(0);
			strQuery.append("Select cm_deptcd, cm_updeptcd, cm_deptname from cmm0100 where        \n");
			
			if (strDept2.length > 0){
				strQuery.append(" cm_dept_code in ( \n");
				for (j=0;strDept2.length>j;j++) {
					if (j>0) strQuery.append(", ? ");
					else strQuery.append("? ");
				}
				strQuery.append(" ) and cm_deptcd not in('00002')  and cm_useyn ='Y'  \n");
			}
			strQuery.append("start with cm_updeptcd is null  				   \n");
			strQuery.append("connect by prior cm_deptcd = cm_updeptcd 		   \n");
			strQuery.append("  order by cm_deptname						   	   \n");
            pstmt = conn.prepareStatement(strQuery.toString());
            pstmt = new LoggableStatement(conn,strQuery.toString());

            for (int i=0;i<strDept2.length;i++){
				pstmt.setString(paramCnt++, strDept2[i]);
			}
			ecamsLogger.error(((LoggableStatement)pstmt).getQueryString());
            rs = pstmt.executeQuery();
			
            while (rs.next()){
            	if (chkcd == true) {
					teamInfoMap = new HashMap<String, String>();
            		teamInfoMap.put("id", rs.getString("cm_deptcd"));
            		teamInfoMap.put("cm_deptcd", rs.getString("cm_deptcd"));
            		teamInfoMap.put("pId", rs.getString("cm_updeptcd"));
            		teamInfoMap.put("cm_updeptcd", rs.getString("cm_updeptcd"));
            		teamInfoMap.put("name", rs.getString("cm_deptname"));
            		teamInfoMap.put("cm_deptname", rs.getString("cm_deptname"));
            		teamInfoMap.put("checked", "false");
            		teamInfoMap.put("enabled", "true");
            		teamInfoMap.put("isParent", "true");
            		teamInfoArr.add(teamInfoMap);
            	} else {
            		teamInfoMap = new HashMap<String, String>();
            		teamInfoMap.put("id", rs.getString("cm_deptcd"));
            		teamInfoMap.put("cm_deptcd", rs.getString("cm_deptcd"));
            		teamInfoMap.put("pId", rs.getString("cm_updeptcd"));
            		teamInfoMap.put("cm_updeptcd", rs.getString("cm_updeptcd"));
            		teamInfoMap.put("name", rs.getString("cm_deptname"));
            		teamInfoMap.put("cm_deptname", rs.getString("cm_deptname"));
            		teamInfoMap.put("isParent", "true");
            		teamInfoArr.add(teamInfoMap);
            	}
			}//end of while-loop statement
            
            rs.close();
            pstmt.close();
            conn.close();

			rs = null;
			pstmt = null;
			conn = null;
			
            return teamInfoArr;
			
		} catch (SQLException sqlexception) {
			sqlexception.printStackTrace();
			ecamsLogger.error("## TeamInfo.getTeamInfoTree_zTree() SQLException START ##");
			ecamsLogger.error("## Error DESC : ", sqlexception);	
			ecamsLogger.error("## TeamInfo.getTeamInfoTree_zTree() SQLException END ##");
			throw sqlexception;
		} catch (Exception exception) {
			exception.printStackTrace();
			ecamsLogger.error("## TeamInfo.getTeamInfoTree_zTree() Exception START ##");				
			ecamsLogger.error("## Error DESC : ", exception);	
			ecamsLogger.error("## TeamInfo.getTeamInfoTree_zTree() Exception END ##");
			throw exception;
		}finally{
			if (myXml != null)	myXml = null;
			if (strQuery != null) 	strQuery = null;
			if (rs != null)     try{rs.close();}catch (Exception ex){ex.printStackTrace();}
			if (pstmt != null)  try{pstmt.close();}catch (Exception ex2){ex2.printStackTrace();}
			if (conn != null){
				try{
					ConnectionResource.release(conn);
				}catch(Exception ex3){
					ecamsLogger.error("## TeamInfo.getTeamInfoTree_zTree() connection release exception ##");
					ex3.printStackTrace();
				}
			}
		}
	}//end of getTeamInfoTree_zTree() method statement
	
}//end of TeamInfo class statement
