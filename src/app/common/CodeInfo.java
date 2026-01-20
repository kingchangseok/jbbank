
/*****************************************************************************************
	1. program ID	: CodeInfo.java
	2. create date	: 2006.08. 08
	3. auth		    : teok.kang
	4. update date	: 
	5. auth		    : 
	6. description	: CodeInfo
*****************************************************************************************/

package app.common;

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


/**
 * @author bigeyes
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */


public class CodeInfo{
	
    /**
     * Logger Class Instance Creation
     * logger
     */
    Logger ecamsLogger  = EcamsLogger.getLoggerInstance();
    
	/**
	 * 게시판을 조회합니다.(구분값을 주어)
	 * @param  gbn
	 * @return ArrayList
	 * @throws SQLException
	 * @throws Exception
	 */

	public Object[] getCodeInfo(String MACODE,String SelMsg,String closeYn) throws SQLException, Exception {
		Connection        conn        = null;
		PreparedStatement pstmt       = null;
		ResultSet         rs          = null;
		StringBuffer      strQuery    = new StringBuffer();
		Object[]		  rtObj		  = null;
		ArrayList<HashMap<String, String>>  rtList = new ArrayList<HashMap<String, String>>();
		HashMap<String, String>			    rst	   = null;
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
			
			String[] macode = MACODE.split(",");
			strQuery.append("select decode(cm_micode,'****',1,2) flag, cm_macode,cm_micode,cm_codename from cmm0020 where \n");
			strQuery.append("cm_macode in ( \n");
			if (macode.length == 1)
				strQuery.append(" ? ");
			else{
				for (int i=0;i<macode.length;i++){
					if (i == macode.length-1)
						strQuery.append(" ? ");
					else
						strQuery.append(" ? ,");
				}
			}
			strQuery.append(" ) \n");
			if (SelMsg == "" || SelMsg == null) {
	           strQuery.append("and cm_micode<>'****' \n");
			}
	        if (closeYn.toUpperCase().equals("N") || closeYn.toUpperCase().equals("0")){
	        	strQuery.append("and cm_closedt is null \n");
	        }
	        
	        strQuery.append("order by cm_macode,flag, cm_seqno, cm_micode \n");			
			
            pstmt = conn.prepareStatement(strQuery.toString());
	        //pstmt =  new LoggableStatement(conn, strQuery.toString());
           
			for (int i=0;i<macode.length;i++){
				pstmt.setString(i+1, macode[i]);
			}

            //ecamsLogger.error(((LoggableStatement)pstmt).getQueryString());
            
            rs = pstmt.executeQuery();
            
            rtList.clear();
            
			while (rs.next()){
				if (rs.getString("cm_micode").equals("****")) {
					if (strSelMsg != "" && !strSelMsg.equals("") && strSelMsg.length() > 0) {
						rst = new HashMap<String, String>();
						rst.put("ID", "0");
						rst.put("cm_codename", strSelMsg);
						rst.put("cm_micode", "00");
						rst.put("cm_macode", rs.getString("cm_macode"));
						rtList.add(rst);
						rst = null;
					}
				} else {				
					rst = new HashMap<String, String>();
					rst.put("ID", Integer.toString(rs.getRow()));
					rst.put("cm_codename", rs.getString("cm_codename"));
					rst.put("cm_micode", rs.getString("cm_micode"));
					rst.put("cm_macode", rs.getString("cm_macode"));
					rtList.add(rst);
					rst = null;
				}
			}//end of while-loop statement
			rs.close();
			pstmt.close();
			conn.close();
			
			rs = null; //수정
			pstmt= null; //수정
			conn= null; //수정
			
			rtObj =  rtList.toArray();
			
			rtList = null;
			
			return rtObj;			

		} catch (SQLException sqlexception) {
			sqlexception.printStackTrace();
			ecamsLogger.error("## CodeInfo.getCodeInfo() SQLException START ##");
			ecamsLogger.error("## Error DESC : ", sqlexception);	
			ecamsLogger.error("## CodeInfo.getCodeInfo() SQLException END ##");			
			throw sqlexception;
		} catch (Exception exception) {
			exception.printStackTrace();
			ecamsLogger.error("## CodeInfo.getCodeInfo() Exception START ##");				
			ecamsLogger.error("## Error DESC : ", exception);	
			ecamsLogger.error("## CodeInfo.getCodeInfo() Exception END ##");				
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
					ecamsLogger.error("## CodeInfo.getCodeInfo() connection release exception ##");
					ex3.printStackTrace();
				}
			}
		}

		
	}//end of getCodeInfo() method statement

	public Object[] getCodeInfo2(String MACODE,String SelMsg,String closeYn) throws SQLException, Exception {
		Connection        conn        = null;
		PreparedStatement pstmt       = null;
		ResultSet         rs          = null;
		StringBuffer      strQuery    = new StringBuffer();
		Object[]		  rtObj		  = null;
		ArrayList<HashMap<String, String>>  rtList = new ArrayList<HashMap<String, String>>();
		HashMap<String, String>			    rst	   = null; 
		String            strSelMsg   = "";
		
		ConnectionContext connectionContext = new ConnectionResource();
		
		try {
			conn = connectionContext.getConnection();

			if (SelMsg != "" && SelMsg != null) {
				if (SelMsg.toUpperCase().equals("ALL")){
					strSelMsg = "전체";
				}
			
				else{
					strSelMsg   = "";
				}
			}			
			
			String[] macode = MACODE.split(",");
			strQuery.append("select decode(cm_micode,'****',1,2) flag, cm_macode,cm_micode,cm_codename from cmm0020 where \n");
			strQuery.append("cm_macode in ( \n");
			if (macode.length == 1)
				strQuery.append(" ? ");
			else{
				for (int i=0;i<macode.length;i++){
					if (i == macode.length-1)
						strQuery.append(" ? ");
					else
						strQuery.append(" ? ,");
				}
			}
			strQuery.append(" ) \n");
			if (SelMsg == "" || SelMsg == null) {
	           strQuery.append("and cm_micode<>'****' \n");
			}
	        if (closeYn.toUpperCase().equals("N") || closeYn.toUpperCase().equals("0")){
	        	strQuery.append("and cm_closedt is null \n");
	        }
	        strQuery.append("order by cm_macode,flag, cm_micode \n");			
			
            pstmt = conn.prepareStatement(strQuery.toString());
	        //pstmt =  new LoggableStatement(conn, strQuery.toString());
           
			for (int i=0;i<macode.length;i++){
				pstmt.setString(i+1, macode[i]);
			}

            //ecamsLogger.error(((LoggableStatement)pstmt).getQueryString());
            
            rs = pstmt.executeQuery();
            
            rtList.clear();
            
			while (rs.next()){
				if (rs.getString("cm_micode").equals("****")) {
					if (strSelMsg != "" && !strSelMsg.equals("") && strSelMsg.length() > 0) {
						rst = new HashMap<String, String>();
						rst.put("ID", "0");
						rst.put("cm_codename", strSelMsg);
						rst.put("cm_micode", "00");
						rst.put("cm_macode", rs.getString("cm_macode"));
						rtList.add(rst);
						rst = null;
					}
				} else {				
					rst = new HashMap<String, String>();
					rst.put("ID", Integer.toString(rs.getRow()));
					rst.put("cm_codename", rs.getString("cm_codename"));
					rst.put("cm_micode", rs.getString("cm_micode"));
					rst.put("cm_macode", rs.getString("cm_macode"));
					rtList.add(rst);
					rst = null;
				}
			}//end of while-loop statement
			rs.close();
			pstmt.close();
			conn.close();
			
			rs = null; //수정
			pstmt= null; //수정
			conn= null; //수정
			
			rtObj =  rtList.toArray();
			
			rtList = null;
			
			return rtObj;			

		} catch (SQLException sqlexception) {
			sqlexception.printStackTrace();
			ecamsLogger.error("## CodeInfo.getCodeInfo2() SQLException START ##");
			ecamsLogger.error("## Error DESC : ", sqlexception);	
			ecamsLogger.error("## CodeInfo.getCodeInfo2() SQLException END ##");			
			throw sqlexception;
		} catch (Exception exception) {
			exception.printStackTrace();
			ecamsLogger.error("## CodeInfo.getCodeInfo2() Exception START ##");				
			ecamsLogger.error("## Error DESC : ", exception);	
			ecamsLogger.error("## CodeInfo.getCodeInfo2() Exception END ##");				
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
					ecamsLogger.error("## CodeInfo.getCodeInfo2() connection release exception ##");
					ex3.printStackTrace();
				} 
			}
		}

		
	}//end of getCodeInfo() method statement

	public Object[] getCodeInfo_Sort(String MACODE,String SelMsg,String closeYn,int sortCd) throws SQLException, Exception {
		Connection        conn        = null;
		PreparedStatement pstmt       = null;
		ResultSet         rs          = null;
		StringBuffer      strQuery    = new StringBuffer();
		Object[]		  rtObj		  = null;
		ArrayList<HashMap<String, String>>  rtList = new ArrayList<HashMap<String, String>>();
		HashMap<String, String>			    rst	   = null;
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
			
			String[] macode = MACODE.split(",");
			strQuery.append("select decode(cm_micode,'****',1,2) flag, cm_macode,cm_micode,cm_codename from cmm0020 where \n");
			strQuery.append("cm_macode in ( \n");
			if (macode.length == 1)
				strQuery.append(" ? ");
			else{
				for (int i=0;i<macode.length;i++){
					if (i == macode.length-1)
						strQuery.append(" ? ");
					else
						strQuery.append(" ? ,");
				}
			}
			strQuery.append(" ) \n");
			if (SelMsg == "" || SelMsg == null) {
	           strQuery.append("and cm_micode<>'****' \n");
			}
	        if (closeYn.toUpperCase().equals("N") || closeYn.toUpperCase().equals("0")){
	        	strQuery.append("and cm_closedt is null \n");
	        }
	        if (sortCd == 0) {
	        	strQuery.append("order by cm_macode, flag, cm_micode \n");	
	        } else {
	        	strQuery.append("order by cm_macode, flag, cm_codename \n");	
	        }
			
            pstmt = conn.prepareStatement(strQuery.toString());
	        //pstmt =  new LoggableStatement(conn, strQuery.toString());
           
			for (int i=0;i<macode.length;i++){
				pstmt.setString(i+1, macode[i]);
			}

            //ecamsLogger.error(((LoggableStatement)pstmt).getQueryString());
            
            rs = pstmt.executeQuery();
            
            rtList.clear();
            
			while (rs.next()){
				if (rs.getString("cm_micode").equals("****")) {
					if (strSelMsg != "" && !strSelMsg.equals("") && strSelMsg.length() > 0) {
						rst = new HashMap<String, String>();
						rst.put("ID", "0");
						rst.put("cm_codename", strSelMsg);
						rst.put("cm_micode", "00");
						rst.put("cm_macode", rs.getString("cm_macode"));
						rtList.add(rst);
						rst = null;
					}
				} else {				
					rst = new HashMap<String, String>();
					rst.put("ID", Integer.toString(rs.getRow()));
					rst.put("cm_codename", rs.getString("cm_codename"));
					rst.put("cm_micode", rs.getString("cm_micode"));
					rst.put("cm_macode", rs.getString("cm_macode"));
					rtList.add(rst);
					rst = null;
				}
			}//end of while-loop statement
			rs.close();
			pstmt.close();
			conn.close();
			
			rs = null; //수정
			pstmt= null; //수정
			conn= null; //수정
			
			rtObj =  rtList.toArray();
			
			rtList = null;
			
			return rtObj;			

		} catch (SQLException sqlexception) {
			sqlexception.printStackTrace();
			ecamsLogger.error("## CodeInfo.getCodeInfo() SQLException START ##");
			ecamsLogger.error("## Error DESC : ", sqlexception);	
			ecamsLogger.error("## CodeInfo.getCodeInfo() SQLException END ##");			
			throw sqlexception;
		} catch (Exception exception) {
			exception.printStackTrace();
			ecamsLogger.error("## CodeInfo.getCodeInfo() Exception START ##");				
			ecamsLogger.error("## Error DESC : ", exception);	
			ecamsLogger.error("## CodeInfo.getCodeInfo() Exception END ##");				
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
					ecamsLogger.error("## CodeInfo.getCodeInfo() connection release exception ##");
					ex3.printStackTrace();
				}
			}
		}

		
	}//end of getCodeInfo() method statement
	

	public String getSrOpen(String SelMsg,String closeYn) throws SQLException, Exception {
		Connection        conn        = null;
		PreparedStatement pstmt       = null;
		ResultSet         rs          = null;
		StringBuffer      strQuery    = new StringBuffer();
		String			  rtJson	  = "";
		ConnectionContext connectionContext = new ConnectionResource();
		
		try {
			conn = connectionContext.getConnection();
			strQuery.append("select cm_micode from cmm0020 where cm_macode ='SROPEN'  and cm_micode <> '****'  and cm_closedt is null \n");			
			pstmt = conn.prepareStatement(strQuery.toString());		
	    	pstmt = new LoggableStatement(conn,strQuery.toString());	
	    	ecamsLogger.error(((LoggableStatement)pstmt).getQueryString());
            rs = pstmt.executeQuery();
			if (rs.next()){
				rtJson = rs.getString("cm_micode");
			}//end of while-loop statement
			rs.close();
			pstmt.close();
			conn.close();

			rs = null;
			pstmt = null;
			conn = null;
			
			return rtJson;
			
		} catch (SQLException sqlexception) {
			sqlexception.printStackTrace();
			ecamsLogger.error("## CodeInfo.getSrOpen() SQLException START ##");
			ecamsLogger.error("## Error DESC : ", sqlexception);	
			ecamsLogger.error("## CodeInfo.getSrOpen() SQLException END ##");			
			throw sqlexception;
		} catch (Exception exception) {
			exception.printStackTrace();
			ecamsLogger.error("## CodeInfo.getSrOpen() Exception START ##");				
			ecamsLogger.error("## Error DESC : ", exception);	
			ecamsLogger.error("## CodeInfo.getSrOpen() Exception END ##");				
			throw exception;
		}finally{
			if (strQuery != null) 	strQuery = null;
			if (rs != null)     try{rs.close();}catch (Exception ex){ex.printStackTrace();}
			if (pstmt != null)  try{pstmt.close();}catch (Exception ex2){ex2.printStackTrace();}
			if (conn != null){
				try{
					ConnectionResource.release(conn);
				}catch(Exception ex3){
					ecamsLogger.error("## CodeInfo.getSrOpen() connection release exception ##");
					ex3.printStackTrace();
				}
			}
		} 
	}//end of getSrOpen() method statement
	
	public Object[] getJobCd(String SelMsg,String closeYn) throws SQLException, Exception {
		Connection        conn        = null;
		PreparedStatement pstmt       = null;
		ResultSet         rs          = null;
		StringBuffer      strQuery    = new StringBuffer();
		Object[]		  rtObj		  = null;
		ArrayList<HashMap<String, String>>  rtList = new ArrayList<HashMap<String, String>>();
		HashMap<String, String>			    rst	   = null;
		String            strSelMsg   = "";
		
		ConnectionContext connectionContext = new ConnectionResource();
		
		try {
			conn = connectionContext.getConnection();
			
			if (SelMsg != null && !"".equals(SelMsg)) {
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
			
			strQuery.append("select cm_jobcd,cm_jobname from cmm0102 \n");
	        if (closeYn.toUpperCase().equals("N") || closeYn.toUpperCase().equals("0")){
	        	strQuery.append("where cm_useyn='Y'                  \n");
	        }
	        strQuery.append("order by cm_jobcd                       \n");			
			
            pstmt = conn.prepareStatement(strQuery.toString());
	        //pstmt =  new LoggableStatement(conn, strQuery.toString());
           
            //ecamsLogger.error(((LoggableStatement)pstmt).getQueryString());
            
            rs = pstmt.executeQuery();
            
            rtList.clear();
            
			while (rs.next()){
				if (rs.getRow() == 1) {
					if (strSelMsg != null && !"".equals(strSelMsg) && strSelMsg.length() > 0) {
						rst = new HashMap<String, String>();
						rst.put("ID", "0");
						rst.put("cm_jobname", strSelMsg);
						rst.put("cm_jobcd", "00");
						rtList.add(rst);
						rst = null;
					}
				} 			
				rst = new HashMap<String, String>();
				rst.put("ID", Integer.toString(rs.getRow()));
				rst.put("cm_jobname", rs.getString("cm_jobname"));
				rst.put("cm_jobcd", rs.getString("cm_jobcd"));
				rtList.add(rst);
				rst = null;
			}//end of while-loop statement
			rs.close();
			pstmt.close();
			conn.close();
			
			rs = null; //수정
			pstmt= null; //수정
			conn= null; //수정
			
			rtObj =  rtList.toArray();
			
			rtList = null;
			
			return rtObj;			

		} catch (SQLException sqlexception) {
			sqlexception.printStackTrace();
			ecamsLogger.error("## CodeInfo.getJobCd() SQLException START ##");
			ecamsLogger.error("## Error DESC : ", sqlexception);	
			ecamsLogger.error("## CodeInfo.getJobCd() SQLException END ##");			
			throw sqlexception;
		} catch (Exception exception) {
			exception.printStackTrace();
			ecamsLogger.error("## CodeInfo.getJobCd() Exception START ##");				
			ecamsLogger.error("## Error DESC : ", exception);	
			ecamsLogger.error("## CodeInfo.getJobCd() Exception END ##");				
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
					ecamsLogger.error("## CodeInfo.getJobCd() connection release exception ##");
					ex3.printStackTrace();
				}
			}
		}
	}//end of getJobCd() method statement
	
	public HashMap<String, ArrayList<HashMap<String, String>>> getCodeInfoWithArray(ArrayList<HashMap<String, String>> codeInfoArr) throws SQLException, Exception {
		Connection        conn        = null;
		PreparedStatement pstmt       = null;
		ResultSet         rs          = null;
		StringBuffer      strQuery    = new StringBuffer();
		Object[]		  rtObj		  = null;
		ArrayList<HashMap<String, String>>  rtList = new ArrayList<HashMap<String, String>>();
		HashMap<String, String>			    rst	   = null;
		String            strSelMsg   = "";
		HashMap<String, ArrayList<HashMap<String, String>>> returnCodeInfoArrayMap = new HashMap<String, ArrayList<HashMap<String, String>>>();
		ConnectionContext connectionContext = new ConnectionResource();

		try {
			conn = connectionContext.getConnection();
			
			
			for(HashMap<String, String> codeInfoMap: codeInfoArr) {
				//if(codeInfoMap.get("SelMsg").equals(anObject))
				if(codeInfoMap.containsKey("SelMsg")) {
					if (codeInfoMap.get("SelMsg").toUpperCase().equals("ALL")){
						strSelMsg = "전체";
					}
					else if(codeInfoMap.get("SelMsg").toUpperCase().equals("SEL")){
						strSelMsg = "선택하세요";
					}
					else{
						strSelMsg   = "";
					}
				}
				strQuery.setLength(0);
				strQuery.append("select decode(cm_micode,'****',1,2) flag, cm_macode,cm_micode,cm_codename from cmm0020 where 	\n");
				strQuery.append("cm_macode = ? 																					\n");
				
				if (!codeInfoMap.containsKey("SelMsg")) {
		           strQuery.append("and cm_micode<>'****' \n");
				}
				
				if(codeInfoMap.containsKey("closeYn")) {
					if (codeInfoMap.get("closeYn").toUpperCase().equals("N") || codeInfoMap.get("closeYn").toUpperCase().equals("0")){
			        	strQuery.append("and cm_closedt is null \n");
			        }
				}else {
					strQuery.append("and cm_closedt is null \n");
				}
		        if (codeInfoMap.containsKey("ordercd")) {
		        	if ("2".equals(codeInfoMap.get("ordercd"))) strQuery.append("order by cm_macode,flag, cm_codename \n");
		        	else if ("1".equals(codeInfoMap.get("ordercd"))) strQuery.append("order by cm_macode,flag, cm_seqno \n");
		        	else strQuery.append("order by cm_macode,flag, cm_micode \n");
		        } else strQuery.append("order by cm_macode,flag, cm_micode \n");

		        pstmt =  conn.prepareStatement(strQuery.toString());
		        pstmt =  new LoggableStatement(conn, strQuery.toString());

				pstmt.setString(1, codeInfoMap.get("MACODE"));	
				ecamsLogger.error(((LoggableStatement)pstmt).getQueryString());
	            rs = pstmt.executeQuery();
	            rtList = new ArrayList<>();
				while (rs.next()){
					if ("****".equals(rs.getString("cm_micode"))) {
						if (!strSelMsg.equals("") && !strSelMsg.equals("") && strSelMsg.length() > 0) {
							rst = new HashMap<String, String>();
							rst.put("ID", "0");
							rst.put("cm_codename", strSelMsg);
							rst.put("cm_micode", "00");
							rst.put("cm_macode", rs.getString("cm_macode"));
							
							rst.put("id", "00");
							rst.put("value", "00");
							rst.put("pid", "0");
							rst.put("text", strSelMsg);
							rst.put("order", Integer.toString(rs.getRow()));
							rst.put("link", "");
							
							rtList.add(rst);
							rst = null;
						}
					} else {

						rst = new HashMap<String, String>();
						rst.put("ID", Integer.toString(rs.getRow()));
						rst.put("cm_codename", rs.getString("cm_codename"));
						rst.put("cm_micode", rs.getString("cm_micode"));
						rst.put("cm_macode", rs.getString("cm_macode"));
						
						
						rst.put("id", rs.getString("cm_micode"));
						rst.put("value", rs.getString("cm_micode"));
						rst.put("pid", "0");
						rst.put("text", rs.getString("cm_codename"));
						rst.put("order", Integer.toString(rs.getRow()));
						rst.put("link", "");
						
						rtList.add(rst);
						rst = null;
					}
				}//end of while-loop statement
				
				returnCodeInfoArrayMap.put(codeInfoMap.get("MACODE"), rtList);
			}
			
			if(rs != null) rs.close();
			if(pstmt != null) pstmt.close();
			conn.close();

			rs = null;
			pstmt = null;
			conn = null;

			rtList = null;

			return returnCodeInfoArrayMap;

		} catch (SQLException sqlexception) {
			sqlexception.getStackTrace();
			ecamsLogger.error("## CodeInfo.getCodeInfoWithArray() SQLException START ##");
			ecamsLogger.error("## Error DESC : ", sqlexception);
			ecamsLogger.error("## CodeInfo.getCodeInfoWithArray() SQLException END ##");
			throw sqlexception;
		} catch (Exception exception) {
			exception.getStackTrace();
			ecamsLogger.error("## CodeInfo.getCodeInfoWithArray() Exception START ##");
			ecamsLogger.error("## Error DESC : ", exception);
			ecamsLogger.error("## CodeInfo.getCodeInfoWithArray() Exception END ##");
			throw exception;
		}finally { if(connectionContext != null) {connectionContext.release();connectionContext = null;}
			if (strQuery != null) 	strQuery = null;
			if (rtObj != null)	rtObj = null;
			if (rs != null)     try{rs.close();}catch (Exception ex){ex.getStackTrace();}
			if (pstmt != null)  try{pstmt.close();}catch (Exception ex2){ex2.getStackTrace();}
			if (conn != null){
				try{
					ConnectionResource.release(conn);
				}catch(Exception ex3){
					ecamsLogger.error("## CodeInfo.getCodeInfoWithArray() connection release exception ##");
					ex3.getStackTrace();
				}
			}
		}
	}//end of getCodeInfoWithArray() method statement
	
	public ArrayList<HashMap<String, String>> getJobCd_Array(String SelMsg,String closeYn) throws SQLException, Exception {
		Connection        conn        = null;
		PreparedStatement pstmt       = null;
		ResultSet         rs          = null;
		StringBuffer      strQuery    = new StringBuffer();
		ArrayList<HashMap<String, String>>  rtList = new ArrayList<HashMap<String, String>>();
		HashMap<String, String>			    rst	   = null;
		String            strSelMsg   = "";
		
		ConnectionContext connectionContext = new ConnectionResource();
		
		try {
			conn = connectionContext.getConnection();
			
			if (SelMsg != null && !"".equals(SelMsg)) {
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
			
			strQuery.append("select cm_jobcd,cm_jobname,cm_useyn from cmm0102 \n");
	        if (closeYn.toUpperCase().equals("N") || closeYn.toUpperCase().equals("0")){
	        	strQuery.append("where cm_useyn='Y'                  \n");
	        }
	        strQuery.append("order by cm_jobcd                       \n");			
			
            pstmt = conn.prepareStatement(strQuery.toString());
	        pstmt =  new LoggableStatement(conn, strQuery.toString());
           
            ecamsLogger.error(((LoggableStatement)pstmt).getQueryString());
            
            rs = pstmt.executeQuery();
            
            rtList.clear();
            
			while (rs.next()){
				if (rs.getRow() == 1) {
					if (strSelMsg != null && !"".equals(strSelMsg) && strSelMsg.length() > 0) {
						rst = new HashMap<String, String>();
						rst.put("cm_jobname", strSelMsg);
						rst.put("cm_jobcd", "00");
						rtList.add(rst);
						rst = null;
					}
				} 			
				rst = new HashMap<String, String>();
				rst.put("cm_jobname", rs.getString("cm_jobname"));
				rst.put("cm_jobcd", rs.getString("cm_jobcd"));
				rst.put("cm_useyn", rs.getString("cm_useyn"));
				rtList.add(rst);
				rst = null;
			}//end of while-loop statement
			rs.close();
			pstmt.close();
			conn.close();
			
			rs = null; //수정
			pstmt= null; //수정
			conn= null; //수정
			
			return rtList;			

		} catch (SQLException sqlexception) {
			sqlexception.printStackTrace();
			ecamsLogger.error("## CodeInfo.getJobCd_Array() SQLException START ##");
			ecamsLogger.error("## Error DESC : ", sqlexception);	
			ecamsLogger.error("## CodeInfo.getJobCd_Array() SQLException END ##");			
			throw sqlexception;
		} catch (Exception exception) {
			exception.printStackTrace();
			ecamsLogger.error("## CodeInfo.getJobCd_Array() Exception START ##");				
			ecamsLogger.error("## Error DESC : ", exception);	
			ecamsLogger.error("## CodeInfo.getJobCd_Array() Exception END ##");				
			throw exception;
		}finally{
			if (strQuery != null) 	strQuery = null;
			if (rtList != null)	rtList = null;
			if (rs != null)     try{rs.close();}catch (Exception ex){ex.printStackTrace();}
			if (pstmt != null)  try{pstmt.close();}catch (Exception ex2){ex2.printStackTrace();}
			if (conn != null){
				try{
					ConnectionResource.release(conn);
				}catch(Exception ex3){
					ecamsLogger.error("## CodeInfo.getJobCd_Array() connection release exception ##");
					ex3.printStackTrace();
				}
			}
		}
	}//end of getJobCd_Array() method statement
	public ArrayList<HashMap<String, String>> getCodeInfo_Array(String MACODE,String SelMsg,String closeYn,String ordercd) throws SQLException, Exception {
		Connection        conn        = null;
		PreparedStatement pstmt       = null;
		ResultSet         rs          = null;
		StringBuffer      strQuery    = new StringBuffer();
		ArrayList<HashMap<String, String>>  rtList = new ArrayList<HashMap<String, String>>();
		HashMap<String, String>			    rst	   = null;
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
			
			String[] macode = MACODE.split(",");
			strQuery.append("select decode(cm_micode,'****',1,2) flag, cm_macode,cm_micode,cm_codename,cm_seqno,cm_closedt \n");
			strQuery.append("  from cmm0020        \n");
			strQuery.append(" where cm_macode in ( \n");
			if (macode.length == 1)
				strQuery.append(" ? ");
			else{
				for (int i=0;i<macode.length;i++){
					if (i == macode.length-1)
						strQuery.append(" ? ");
					else
						strQuery.append(" ? ,");
				}
			}
			strQuery.append(" ) \n");
			if (SelMsg == "" || SelMsg == null) {
	           strQuery.append("and cm_micode<>'****' \n");
			}
	        if (closeYn.toUpperCase().equals("N") || closeYn.toUpperCase().equals("0")){
	        	strQuery.append("and cm_closedt is null \n");
	        }
	        if (ordercd != null && !"".equals(ordercd)) {
	        	if ("2".equals(ordercd)) strQuery.append("order by cm_macode,flag, cm_codename \n");
	        	else if ("1".equals(ordercd)) strQuery.append("order by cm_macode,flag,cm_seqno \n");
	        	else strQuery.append("order by cm_macode,flag, cm_micode \n");
	        } else strQuery.append("order by cm_macode,flag, cm_micode   \n");		
			
            pstmt = conn.prepareStatement(strQuery.toString());
	        pstmt =  new LoggableStatement(conn, strQuery.toString());
           
			for (int i=0;i<macode.length;i++){
				pstmt.setString(i+1, macode[i]);
			}

            ecamsLogger.error(((LoggableStatement)pstmt).getQueryString());
            
            rs = pstmt.executeQuery();
            
            rtList.clear();
            
			while (rs.next()){
				if (rs.getString("cm_micode").equals("****")) {
					if (strSelMsg != "" && !strSelMsg.equals("") && strSelMsg.length() > 0) {
						rst = new HashMap<String, String>();
						rst.put("ID", "0");
						rst.put("cm_codename", strSelMsg);
						rst.put("cm_micode", "00");
						rst.put("cm_macode", rs.getString("cm_macode"));
						rtList.add(rst);
						rst = null;
					}
				} else {				
					rst = new HashMap<String, String>();
					rst.put("ID", Integer.toString(rs.getRow()));
					rst.put("cm_codename", rs.getString("cm_codename"));
					rst.put("cm_micode", rs.getString("cm_micode"));
					rst.put("cm_macode", rs.getString("cm_macode"));
					rst.put("cm_seqno", rs.getString("cm_seqno"));
					if (rs.getString("cm_closedt") != null) rst.put("cm_useyn", "N");
					else rst.put("cm_useyn", "Y");
					rtList.add(rst);
					rst = null;
				}
			}//end of while-loop statement
			rs.close();
			pstmt.close();
			conn.close();
			
			rs = null; //수정
			pstmt= null; //수정
			conn= null; //수정
			
			
			return rtList;			

		} catch (SQLException sqlexception) {
			sqlexception.printStackTrace();
			ecamsLogger.error("## CodeInfo.getCodeInfo_Array() SQLException START ##");
			ecamsLogger.error("## Error DESC : ", sqlexception);	
			ecamsLogger.error("## CodeInfo.getCodeInfo_Array() SQLException END ##");			
			throw sqlexception;
		} catch (Exception exception) {
			exception.printStackTrace();
			ecamsLogger.error("## CodeInfo.getCodeInfo_Array() Exception START ##");				
			ecamsLogger.error("## Error DESC : ", exception);	
			ecamsLogger.error("## CodeInfo.getCodeInfo_Array() Exception END ##");				
			throw exception;
		}finally{
			if (strQuery != null) 	strQuery = null;
			if (rtList != null)	rtList = null;
			if (rs != null)     try{rs.close();}catch (Exception ex){ex.printStackTrace();}
			if (pstmt != null)  try{pstmt.close();}catch (Exception ex2){ex2.printStackTrace();}
			if (conn != null){
				try{
					ConnectionResource.release(conn);
				}catch(Exception ex3){
					ecamsLogger.error("## CodeInfo.getCodeInfo_Array() connection release exception ##");
					ex3.printStackTrace();
				}
			}
		}
	}//end of getCodeInfo_Array() method statement
}//end of CodeInfo class statement
