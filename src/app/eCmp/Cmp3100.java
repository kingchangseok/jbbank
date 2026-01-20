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
public class Cmp3100{    
	

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
	public Object[] getReqList(String UserId,String StDate,String EdDate) throws SQLException, Exception {
		Connection        conn        = null;
		PreparedStatement pstmt       = null;
		ResultSet         rs          = null;
		ResultSet         rs2         = null;
		StringBuffer      strQuery    = new StringBuffer();
		int               parmCnt     = 0;
		String            svPrjNo     = null;
		String            svStep      = null;
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
			strQuery.append("select a.*,b.cm_codename,c.cm_username,d.cm_deptname as team,e.cm_username as baseusr, \n");
			strQuery.append("f.cm_username as confusr,m.cr_acptdate,m.cr_prcdate,m.cr_qrycd,                         \n");
			strQuery.append("substr(a.cr_acptno,1,4) || '-' || substr(a.cr_acptno,5,2) || '-' || substr(a.cr_acptno,7,6) acptno \n");
			strQuery.append("from (select cm_micode, cm_codename from cmm0020 where cm_macode='REQUEST') b,         \n");
			strQuery.append("(select cm_deptcd,cm_deptname from cmm0100 ) d,                                        \n");
			strQuery.append("cmm0040 e,cmm0040 f,cmm0040 c,cmr1000 m,cmr9900 a where                                \n");
			strQuery.append("to_char(a.cr_confdate,'yyyymmdd')>=? and     											\n");
			strQuery.append("to_char(a.cr_confdate,'yyyymmdd')<=? and    											\n");
			strQuery.append("a.cr_status<>'0' and                                                                   \n");
			strQuery.append("a.cr_baseusr<>a.cr_confusr and                                                         \n");
			strQuery.append("a.cr_teamcd in('2','3','6','8') and                                                    \n");
			strQuery.append("a.cr_locat<>'00' and                                                                   \n");
			strQuery.append("a.cr_acptno=m.cr_acptno and                                                            \n");
			strQuery.append("substr(a.cr_acptno,5,2)=b.cm_micode and                                                \n");
			strQuery.append("m.cr_editor=c.cm_userid and                                                            \n");
			strQuery.append("m.cr_teamcd=d.cm_deptcd and                                                            \n");
			strQuery.append("a.cr_baseusr=e.cm_userid and                                                           \n");
			strQuery.append("a.cr_confusr=f.cm_userid                                                               \n");
			strQuery.append("order by a.cr_acptno,a.cr_locat                                                        \n");

			pstmt = conn.prepareStatement(strQuery.toString());	
		//	pstmt = new LoggableStatement(conn,strQuery.toString());
			
			pstmt.setString(++parmCnt, StDate);
			pstmt.setString(++parmCnt, EdDate);
			
	    //    //ecamsLogger.error(((LoggableStatement)pstmt).getQueryString());           
	        rs = pstmt.executeQuery();
	        
			while (rs.next()){
				rst = new HashMap<String, String>();
				rst.put("acptno",rs.getString("acptno"));
				rst.put("cm_codename",rs.getString("cm_codename"));
				rst.put("cm_username",rs.getString("cm_username"));
				rst.put("cr_acptdate",rs.getString("cr_acptdate"));
				rst.put("team",rs.getString("team"));
				rst.put("baseusr",rs.getString("baseusr"));
				rst.put("confusr",rs.getString("confusr"));
				rst.put("cr_confdate",rs.getString("cr_confdate"));				
				rsval.add(rst);
				rst = null;
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
