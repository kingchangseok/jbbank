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
import java.io.BufferedReader;

import org.apache.logging.log4j.Logger;
import org.w3c.dom.Document;

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
public class Cmp3200{    
	

    /**
     * Logger Class Instance Creation
     * logger
     */
	
    Logger ecamsLogger  = EcamsLogger.getLoggerInstance();
    
	/**
	 * USER NAME
	 * @param user_id
	 * @return String
	 * @throws SQLException
	 * @throws Exception
	 */							//strUserId,strStD,datEnD,cboReq
	public Object[] getReqList(String UserId) throws SQLException, Exception {
		Connection        conn        = null;
		PreparedStatement pstmt       = null;
		PreparedStatement pstmt2       = null;
		ResultSet         rs          = null;
		ResultSet         rs2         = null;
		StringBuffer      strQuery    = new StringBuffer();
		int               parmCnt     = 0;
		String            svPrjNo     = null;
		String            svStep      = null;
		String 			AcptN 		= null;		
		long              docCnt      = 0;
		long              ccbCnt      = 0;
		long              ccbHap      = 0;
		long              docHap      = 0;
		long              ccbTot      = 0;
		long              docTot      = 0;
		int               stepCnt     = 0;
		boolean           rowSw       = false;
		int               i           = 0;
		
		ArrayList<HashMap<String, String>>  rsval = new ArrayList<HashMap<String, String>>();
		HashMap<String, String>			    rst		  = null;
		Object[] returnObjectArray = null;
		ArrayList<String>  rstot1 = new ArrayList<String>();
		ArrayList<Long>  rstot2 = new ArrayList<Long>();
		ArrayList<Long>  rstot3 = new ArrayList<Long>();
		UserInfo         secuinfo     = new UserInfo();
		
		ConnectionContext connectionContext = new ConnectionResource();		
		
		try {
			conn = connectionContext.getConnection();
			strQuery.setLength(0);
			strQuery.append("select b.cm_codename,c.cm_username,m.cr_acptdate,m.cr_prcdate,m.cr_acptno,m.cr_qrycd, \n");
			strQuery.append("a.cr_team,a.cr_sgngbn,a.cr_teamcd,a.cr_confname,d.cm_deptname, \n");
			strQuery.append("substr(m.cr_acptno,1,4) || '-' || substr(m.cr_acptno,5,2) || '-' || substr(m.cr_acptno,7,6) acptno \n");
			strQuery.append("from cmm0100 d,cmm0040 c,cmm0020 b,cmr1000 m,cmr9900 a \n");
			strQuery.append("where m.cr_status not in('3','9') \n");
			strQuery.append("and m.cr_prcdate is not null and a.cr_status='0' and a.cr_locat<>'00' \n");
			strQuery.append("and a.cr_acptno=m.cr_acptno \n");
			strQuery.append("and a.cr_teamcd<>'1' \n");
			strQuery.append("and b.cm_macode='REQUEST' and b.cm_micode=m.cr_qrycd \n");
			strQuery.append("and M.cr_editor = c.cm_userid \n");
			strQuery.append("and m.cr_teamcd=d.cm_deptcd \n");
			strQuery.append("order by a.cr_acptno,a.cr_locat \n");

			pstmt = conn.prepareStatement(strQuery.toString());	
		//	pstmt = new LoggableStatement(conn,strQuery.toString());
			
	    //    //ecamsLogger.error(((LoggableStatement)pstmt).getQueryString());           
	        rs = pstmt.executeQuery();
	        
			while (rs.next()){
				
				if(rs.getString("acptno").equals(AcptN)){
					stepCnt=stepCnt+1;
				
					String ConfN = "";
					String ConfNb = "";
					ConfNb ="["+ rs.getString("cr_confname") +"]";
					ConfN ="["+ rs.getString("cr_confname") +"]";
					
					if(rs.getString("cr_teamcd").equals("2") || rs.getString("cr_teamcd").equals("3") || 
					   rs.getString("cr_teamcd").equals("6") || rs.getString("cr_teamcd").equals("8")){
						
						strQuery.setLength(0);
						strQuery.append("select cm_username from cmm0040 \n");
						strQuery.append("where cm_userid=? \n");//DbSet!cr_team
						
						pstmt2 = conn.prepareStatement(strQuery.toString());
						pstmt2.setString(1, rs.getString("cr_team"));
						rs2 = pstmt2.executeQuery();
						if (rs2.next()){
							if(ConfN.equals(ConfNb) || ConfN==null || ConfN==""){
								ConfN=ConfN + rs2.getString("cm_username");
							}else{
								ConfN=ConfN +", "+ rs2.getString("cm_username");
							}
						}else{
							if(ConfN.equals(ConfNb) || ConfN==null || ConfN==""){
								ConfN=ConfN + rs.getString("cr_team");
							}else{
								ConfN=ConfN +", " + rs.getString("cr_team");
							}
						}
						rs2.close();
						pstmt2.close();
						
					}
					if(rs.getString("cr_teamcd").equals("4") || rs.getString("cr_teamcd").equals("5")){
						
						strQuery.setLength(0);
						strQuery.append("select cm_codename from cmm0020 \n");
		                strQuery.append("where cm_macode='RGTCD' and instr(?,cm_micode)>0 \n"); //DbSet!cr_team
		                
		                pstmt2 = conn.prepareStatement(strQuery.toString());
		                pstmt2.setString(1, rs.getString("cr_team"));
						rs2 = pstmt2.executeQuery();
						if(rs2.next()){
							while (rs2.next()){
								if(ConfN.equals(ConfNb) || ConfN==null || ConfN==""){
									ConfN = ConfN + rs2.getString("cm_codename");
								}else{
									ConfN = ConfN+ ", " + rs2.getString("cm_codename");
								}
							}
						}else{
							if(ConfN.equals(ConfNb) || ConfN==null || ConfN==""){
								ConfN= ConfN + rs.getString("cr_team");
							}else{
								ConfN= ConfN + ", " + rs.getString("cr_team");
							}
						}
						
						rs2.close();
						pstmt2.close();
					}
					if(rs.getString("cr_teamcd").equals("9")){
						
						strQuery.setLength(0);
						strQuery.append("select cm_deptname from cmm0100 \n");
	                  	strQuery.append("where cm_deptcd=? \n");	//DbSet!cr_team
	                  	
	                  	pstmt2 = conn.prepareStatement(strQuery.toString());
	                  	pstmt2.setString(1, rs.getString("cr_team"));
	                  	rs2 = pstmt2.executeQuery();
						if (rs2.next()){
							if(ConfN.equals(ConfNb) || ConfN==null || ConfN==""){
								ConfN = ConfN + rs2.getString("cm_deptname");
							}else{
								ConfN = ConfN +", " + rs2.getString("cm_deptname");
							}
						}else{
							if(ConfN.equals(ConfNb) || ConfN==null || ConfN==""){
								ConfN = ConfN + rs.getString("cr_team");
							}else{
								ConfN = ConfN + ", " + rs.getString("cr_team");
							}
						}
						rs2.close();
						pstmt2.close();
						
						strQuery.setLength(0);
						strQuery.append("select cm_codename from cmm0020 \n");
		                strQuery.append("where cm_macode='RGTCD' and instr(?,cm_micode)>0 \n");
		                     
		                pstmt2 = conn.prepareStatement(strQuery.toString());
		                pstmt2.setString(1, rs.getString("cr_team"));
		                rs2 = pstmt2.executeQuery();
		                if(rs2.next()){
			                while (rs2.next()){
			                	if(ConfN.equals(ConfNb) || ConfN==null || ConfN==""){
			                		ConfN = ConfN + rs2.getString("cm_codename");
			                	}else{
			                		ConfN = ConfN + ", " + rs2.getString("cm_codename");
			                	}
							}
		                }else{
		                	if(ConfN.equals(ConfNb) || ConfN==null || ConfN==""){
		                		ConfN = ConfN + rs.getString("cr_team");
		                	}else{
		                		ConfN = ConfN + ", " + rs.getString("cr_team");
		                	}
						}
						rs2.close();
						pstmt2.close();
					}
					rst.put("cr_confname"+stepCnt,ConfN);
					if (rst.get("cr_confname") == ""){
						
					}else{
						
					}
				}else{
					rsval.add(rst);
					rst = null;
					
					stepCnt=0;
					AcptN = "";
					AcptN = rs.getString("acptno");
					String ConfN = "";
					String ConfNb = "";
					ConfNb ="["+ rs.getString("cr_confname") +"]";
					ConfN ="["+ rs.getString("cr_confname") +"]";
					
					if(rs.getString("cr_teamcd").equals("2") || rs.getString("cr_teamcd").equals("3") || 
					   rs.getString("cr_teamcd").equals("6") || rs.getString("cr_teamcd").equals("8")){
						
						strQuery.setLength(0);
						strQuery.append("select cm_username from cmm0040 \n");
						strQuery.append("where cm_userid=? \n");//DbSet!cr_team
						
						pstmt2 = conn.prepareStatement(strQuery.toString());
						pstmt2.setString(1, rs.getString("cr_team"));
						rs2 = pstmt2.executeQuery();
						if (rs2.next()){
							if(ConfN.equals(ConfNb) || ConfN==null || ConfN==""){
								ConfN=ConfN + rs2.getString("cm_username");
							}else{
								ConfN=ConfN +", "+ rs2.getString("cm_username");
							}
						}else{
							if(ConfN.equals(ConfNb) || ConfN==null || ConfN==""){
								ConfN=ConfN + rs.getString("cr_team");
							}else{
								ConfN=ConfN +", " + rs.getString("cr_team");
							}
						}
						rs2.close();
						pstmt2.close();
						
					}
					if(rs.getString("cr_teamcd").equals("4") || rs.getString("cr_teamcd").equals("5")){
						
						strQuery.setLength(0);
						strQuery.append("select cm_codename from cmm0020 \n");
		                strQuery.append("where cm_macode='RGTCD' and instr(?,cm_micode)>0 \n"); //DbSet!cr_team
		                
		                pstmt2 = conn.prepareStatement(strQuery.toString());
		                pstmt2.setString(1, rs.getString("cr_team"));
						rs2 = pstmt2.executeQuery();
						if(rs2.next()){
							while (rs2.next()){
								if(ConfN.equals(ConfNb) || ConfN==null || ConfN==""){
									ConfN = ConfN + rs2.getString("cm_codename");
								}else{
									ConfN = ConfN+ ", " + rs2.getString("cm_codename");
								}
							}
						}else{
							if(ConfN.equals(ConfNb) || ConfN==null || ConfN==""){
								ConfN= ConfN + rs.getString("cr_team");
							}else{
								ConfN= ConfN + ", " + rs.getString("cr_team");
							}
						}
						
						rs2.close();
						pstmt2.close();
					}
					if(rs.getString("cr_teamcd").equals("9")){
						
						strQuery.setLength(0);
						strQuery.append("select cm_deptname from cmm0100 \n");
	                  	strQuery.append("where cm_deptcd=? \n");	//DbSet!cr_team
	                  	
	                  	pstmt2 = conn.prepareStatement(strQuery.toString());
	                  	pstmt2.setString(1, rs.getString("cr_team"));
	                  	rs2 = pstmt2.executeQuery();
						if (rs2.next()){
							if(ConfN.equals(ConfNb) || ConfN==null || ConfN==""){
								ConfN = ConfN + rs2.getString("cm_deptname");
							}else{
								ConfN = ConfN +", " + rs2.getString("cm_deptname");
							}
						}else{
							if(ConfN.equals(ConfNb) || ConfN==null || ConfN==""){
								ConfN = ConfN + rs.getString("cr_team");
							}else{
								ConfN = ConfN + ", " + rs.getString("cr_team");
							}
						}
						rs2.close();
						pstmt2.close();
						
						strQuery.setLength(0);
						strQuery.append("select cm_codename from cmm0020 \n");
		                strQuery.append("where cm_macode='RGTCD' and instr(?,cm_micode)>0 \n");
		                     
		                pstmt2 = conn.prepareStatement(strQuery.toString());
		                pstmt2.setString(1, rs.getString("cr_team"));
		                rs2 = pstmt2.executeQuery();
		                if(rs2.next()){
			                while (rs2.next()){
			                	if(ConfN.equals(ConfNb) || ConfN==null || ConfN==""){
			                		ConfN = ConfN + rs2.getString("cm_codename");
			                	}else{
			                		ConfN = ConfN + ", " + rs2.getString("cm_codename");
			                	}
							}
		                }else{
		                	if(ConfN.equals(ConfNb) || ConfN==null || ConfN==""){
		                		ConfN = ConfN + rs.getString("cr_team");
		                	}else{
		                		ConfN = ConfN + ", " + rs.getString("cr_team");
		                	}
						}
						rs2.close();
						pstmt2.close();
					}
					
					rst = new HashMap<String, String>();
					rst.put("acptno",rs.getString("acptno"));
					rst.put("cr_team",rs.getString("cr_team"));
					rst.put("cm_codename",rs.getString("cm_codename"));
					rst.put("cm_username",rs.getString("cm_username"));
					rst.put("cm_deptname",rs.getString("cm_deptname"));
					rst.put("cr_acptdate",rs.getString("cr_acptdate"));
					rst.put("cr_prcdate",rs.getString("cr_prcdate"));
					rst.put("cr_qrycd",rs.getString("cr_qrycd"));
					rst.put("cr_confname",ConfN);
				}	
			}//end of while-loop statement
			
			rs.close();
			pstmt.close();
			conn.close();
			conn = null;
			pstmt = null;
			rs = null;
			returnObjectArray = rsval.toArray();
			
			rsval = null;
			//ecamsLogger.debug(rsval.toString());		
			return returnObjectArray;			
			
		} catch (SQLException sqlexception) {
			sqlexception.printStackTrace();
			ecamsLogger.error("## Cmp3300.getReqList() SQLException START ##");
			ecamsLogger.error("## Error DESC : ", sqlexception);	
			ecamsLogger.error("## Cmp3300.getReqList() SQLException END ##");			
			throw sqlexception;
		} catch (Exception exception) {
			exception.printStackTrace();
			ecamsLogger.error("## Cmp3300.getReqList() Exception START ##");				
			ecamsLogger.error("## Error DESC : ", exception);	
			ecamsLogger.error("## Cmp3300.getReqList() Exception END ##");				
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
					ecamsLogger.error("## Cmp3300.getReqList() connection release exception ##");
					ex3.printStackTrace();
				}
			}
		}		


	}//end of getReqList() method statement		
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
}//end of Cmp3300 class statement
