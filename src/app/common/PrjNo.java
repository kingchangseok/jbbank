
/*****************************************************************************************
	1. program ID	: PrjNo.java
	2. create date	: 2008.04. 17
	3. auth		    : ms.kang
	4. update date	: 
	5. auth		    : 
	6. description	: 1. 프로젝트목록
*****************************************************************************************/

package app.common;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.logging.log4j.Logger;
import org.w3c.dom.Document;

import com.ecams.common.dbconn.ConnectionContext;
import com.ecams.common.dbconn.ConnectionResource;
import com.ecams.common.logger.EcamsLogger;
import java.util.ArrayList;
import java.util.HashMap;


/**
 * @author bigeyes
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class PrjNo{
    /**
     * Logger Class Instance Creation
     * logger
     */
    Logger ecamsLogger  = EcamsLogger.getLoggerInstance();
    
	/**
	 * @param  user_id,QryCd,AdminYn,EndYn
	 *         사용자,화면구분,관리자여부,종료건포함여부
	 * @return ArrayList
	 * @throws SQLException
	 * @throws Exception
	 */
    public ArrayList<HashMap<String, String>> selectPrjNo(String user_id,String QryCd,String EndYn) throws SQLException, Exception {
		Connection        conn        = null;
		PreparedStatement pstmt       = null;
		ResultSet         rs          = null;
		StringBuffer      strQuery    = new StringBuffer();
		ArrayList<HashMap<String, String>>  rsval = new ArrayList<HashMap<String, String>>();
		HashMap<String, String>			  rst		  = null;
		UserInfo		  userInfo	  = new UserInfo();
		Boolean			  AdminYn	  = false;
		HashMap<String, String>    userData = null;
		ConnectionContext connectionContext = new ConnectionResource();
		
		
		
		
		try {
			conn = connectionContext.getConnection();
			
			AdminYn = userInfo.isAdmin(user_id);
			userInfo = null;
			
			if(QryCd.equals("D03") || QryCd.equals("D04")){  
			 // 프로젝트신규,프로젝트정보	
				if (AdminYn == true){
				    strQuery.append("SELECT A.CD_PRJNO PRJNO,A.CD_PRJNAME PRJNAME FROM CMD0300 A ");
				    strQuery.append(" WHERE A.CD_CLOSEDT IS NULL ");
				    if (EndYn.toUpperCase().equals("Y")) strQuery.append("   AND A.CD_STATUS<>'3' ");
				    else strQuery.append("   AND A.CD_STATUS='0' ");	
					strQuery.append("GROUP BY A.CD_PRJNO,A.CD_PRJNAME  ");
					strQuery.append("ORDER BY A.CD_PRJNO ");
		            pstmt = conn.prepareStatement(strQuery.toString());	
				}else {
					strQuery.append("SELECT A.CD_PRJNO PRJNO,A.CD_PRJNAME PRJNAME ");
					strQuery.append("  FROM CMD0300 A,CMD0304 B ");
					strQuery.append(" WHERE B.CD_PRJUSER = ?     ");
					strQuery.append("   AND	B.CD_CLOSEDT IS NULL ");
					strQuery.append("   AND	B.CD_PRJNO=A.CD_PRJNO ");
					strQuery.append("   AND	A.CD_CLOSEDT IS NULL ");
				    if (EndYn.toUpperCase().equals("Y")) strQuery.append("   AND A.CD_STATUS<>'3' ");
				    else strQuery.append("   AND A.CD_STATUS='0' ");
					strQuery.append("GROUP BY A.CD_PRJNO,A.CD_PRJNAME  ");
					strQuery.append("ORDER BY A.CD_PRJNO ");
		            pstmt = conn.prepareStatement(strQuery.toString());	
		            pstmt.setString(1, user_id);	
				}
			}else if (QryCd.equals("R39")){  // 산출물체크아웃취소
				strQuery.append("SELECT A.CR_PRJNO PRJNO,A.CR_PRJNAME PRJNAME ");
				strQuery.append("  FROM CMR1000 A,CMR1100 B ");	
				strQuery.append(" WHERE B.CR_EDITOR = ? ");		
				strQuery.append("   AND B.CR_CONFNO IS NULL ");
				strQuery.append("   AND B.CR_STATUS NOT IN ('0','3') ");	
				strQuery.append("   AND B.CR_ACPTNO=A.CR_ACPTNO ");		
				strQuery.append("   AND A.CR_QRYCD='31' ");		
				strQuery.append("   AND A.CR_PRJNO IS NOT NULL ");
				strQuery.append("GROUP BY A.CR_PRJNO,PRJNAME ");
				strQuery.append("ORDER BY A.CR_PRJNO ");
	            pstmt = conn.prepareStatement(strQuery.toString());	
	            pstmt.setString(1, user_id);								
			} else if(QryCd.equals("R31") || QryCd.equals("D16")){  
			 // 산출물체크아웃,산출물신규
				strQuery.append("SELECT A.CD_PRJNO PRJNO,A.CD_PRJNAME PRJNAME ");
				strQuery.append("  FROM CMD0300 A,CMD0304 B ");
				strQuery.append(" WHERE B.CD_PRJUSER = ?     ");
				strQuery.append("   AND	B.CD_CLOSEDT IS NULL ");
				strQuery.append("   AND	B.CD_PRJNO=A.CD_PRJNO ");
				strQuery.append("   AND	A.CD_CLOSEDT IS NULL ");
			    if (EndYn.toUpperCase().equals("Y")) strQuery.append("   AND A.CD_STATUS<>'3' ");
			    else strQuery.append("   AND A.CD_STATUS='0' ");
				strQuery.append("GROUP BY A.CD_PRJNO,A.CD_PRJNAME  ");
				strQuery.append("ORDER BY A.CD_PRJNO ");
	            pstmt = conn.prepareStatement(strQuery.toString());	
	            pstmt.setString(1, user_id);	
			}
			else {
				if (AdminYn == true){
					strQuery.append("SELECT A.CD_PRJNO PRJNO,A.CD_PRJNAME PRJNAME ");
					strQuery.append("FROM CMD0300 A ");
					strQuery.append("WHERE A.CD_CLOSEDT IS NULL ");
					strQuery.append("AND A.CD_STATUS = '0' ");
				}
				else{
					strQuery.append("SELECT A.CD_PRJNO PRJNO,A.CD_PRJNAME PRJNAME ");
					strQuery.append("FROM CMD0300 A, CMD0304 B ");
					strQuery.append("WHERE B.CD_PRJUSER= ? ");							
					strQuery.append("AND B.CD_CLOSEDT IS NULL ");
					strQuery.append("AND A.CD_PRJNO = B.CD_PRJNO ");
					strQuery.append("AND a.CD_CLOSEDT IS NULL ");
					strQuery.append("AND A.CD_STATUS = '0' ");
				}
				strQuery.append("GROUP BY A.CD_PRJNO,a.CD_PRJNAME  ");
				strQuery.append("ORDER BY A.CD_PRJNO ");
				
				pstmt = conn.prepareStatement(strQuery.toString());	
				
				if (AdminYn == false){
					pstmt.setString(1, user_id);
				}
			}
		
			            
            rs = pstmt.executeQuery();
	        rsval.clear();
            while (rs.next()){
				rst = new HashMap<String, String>();
				rst.put("ID", Integer.toString(rs.getRow()));
				rst.put("prjno", rs.getString("prjno"));
				rst.put("prjname", rs.getString("prjname"));
				rst.put("prjnoname", rs.getString("prjno") + " " +rs.getString("prjname"));
				rsval.add(rst);
				rst = null;
			}//end of while-loop statement
            rs.close();
            pstmt.close();
            conn.close();
            
    		return rsval;
    		
	    } catch (SQLException sqlexception) {
			sqlexception.printStackTrace();
			ecamsLogger.error("## PrjNo.selectPrjNo() SQLException START ##");
			ecamsLogger.error("## Error DESC : ", sqlexception);	
			ecamsLogger.error("## PrjNo.selectPrjNo() SQLException END ##");			
			throw sqlexception;
		} catch (Exception exception) {
			exception.printStackTrace();
			ecamsLogger.error("## PrjNo.selectPrjNo() Exception START ##");				
			ecamsLogger.error("## Error DESC : ", exception);	
			ecamsLogger.error("## PrjNo.selectPrjNo() Exception END ##");				
			throw exception;
		}finally{
			if (strQuery != null) 	strQuery = null;
			if (rsval != null)	rsval = null;
			if (rs != null)     try{rs.close();}catch (Exception ex){ex.printStackTrace();}
			if (pstmt != null)  try{pstmt.close();}catch (Exception ex2){ex2.printStackTrace();}
			if (conn != null){
				try{
					ConnectionResource.release(conn);
				}catch(Exception ex3){
					ecamsLogger.error("## PrjNo.selectPrjNo() connection release exception ##");
					ex3.printStackTrace();
				}
			}
		}
		
	}//end of selectPrjNo() method statement
	
	public Document getDocStru_Tree(String prjNo) throws SQLException, Exception {
		Connection        conn        = null;
		PreparedStatement pstmt       = null;
		ResultSet         rs          = null;
		StringBuffer      strQuery    = new StringBuffer();
		CreateXml		  myXml		  = new CreateXml();
		
		ConnectionContext connectionContext = new ConnectionResource();
		
		try {
			conn = connectionContext.getConnection();
		    strQuery.append("SELECT cd_docseq, cd_dirname, cd_updocseq ");
		    strQuery.append("FROM (SELECT * FROM CMD0303 WHERE CD_PRJNO = ? ) ");
			strQuery.append("START WITH CD_UPDOCSEQ IS NULL ");
            strQuery.append("CONNECT BY PRIOR CD_DOCSEQ = CD_UPDOCSEQ ");
            
            pstmt = conn.prepareStatement(strQuery.toString());	
            pstmt.setString(1, prjNo);
            
            myXml.init_Xml("ID","cd_docseq","cd_dirname","cd_updocseq");
            
            rs = pstmt.executeQuery();
            
            while (rs.next()){
				myXml.addXML(rs.getString("cd_docseq"),
						rs.getString("cd_docseq"), 
						rs.getString("cd_dirname"),
						rs.getString("cd_updocseq"));
			}//end of while-loop statement
            
            rs.close();
            pstmt.close();
            conn.close();

    		return myXml.getDocument();
    		            
			
		} catch (SQLException sqlexception) {
			sqlexception.printStackTrace();
			ecamsLogger.error("## PrjNo.getDocStru_Tree() SQLException START ##");
			ecamsLogger.error("## Error DESC : ", sqlexception);	
			ecamsLogger.error("## PrjNo.getDocStru_Tree() SQLException END ##");			
			throw sqlexception;
		} catch (Exception exception) {
			exception.printStackTrace();
			ecamsLogger.error("## PrjNo.getDocStru_Tree() Exception START ##");				
			ecamsLogger.error("## Error DESC : ", exception);	
			ecamsLogger.error("## PrjNo.getDocStru_Tree() Exception END ##");				
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
					ecamsLogger.error("## PrjNo.getDocStru_Tree() connection release exception ##");
					ex3.printStackTrace();
				}
			}
		}
		
	}//end of getDocStru_Tree() method statement
	
}//end of PrjNo class statement
