
/*****************************************************************************************
	1. program ID	: Cmd1300 Table
	2. create date	: 2008.05. 26
	3. auth		    : 
	4. update date	: 
	5. auth		    : 
	6. description	: Cmd1300
*****************************************************************************************/

package app.eCmd;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
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
import app.common.LoggableStatement;
import app.common.SystemPath;
import app.common.CreateXml;

/**
 * @author bigeyes
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */


public class Cmd1300{
	
    /**
     * Logger Class Instance Creation
     * logger
     */
    Logger ecamsLogger  = EcamsLogger.getLoggerInstance();
    
	/**
	 * 조직도 트리
	 * @param  
	 * @return ArrayList
	 * @throws SQLException
	 * @throws Exception
	 */
	
    public Object[] get_Cbo_SignNMADD(String UserID) throws SQLException, Exception {
		Connection        conn        = null;
		PreparedStatement pstmt       = null;
		ResultSet         rs          = null;
		StringBuffer      strQuery    = new StringBuffer();
		ArrayList<HashMap<String, String>>  rtList	= new ArrayList<HashMap<String, String>>();
		HashMap<String, String>			  	rst	 	= null;
        Object[]		 rtObj		  = null;	
		
		ConnectionContext connectionContext = new ConnectionResource();
		
		try {
			conn = connectionContext.getConnection();
			strQuery.append("select distinct CM_SIGNNM from CMM0045 \n");
			strQuery.append("Where CM_USERID= ? 			\n");  //UserID
			strQuery.append("AND CM_SIGNUSER is not null 	\n");
			strQuery.append("Order by CM_SIGNNM 			\n"); 			

			pstmt = conn.prepareStatement(strQuery.toString());
			pstmt.setString(1, UserID);
            
            rs = pstmt.executeQuery();            
            rtList.clear();
            
            rst = new HashMap<String, String>();
            rst.put("CM_SIGNNM", "결재추가");
            rtList.add(rst);
            rst = null;
            
            while (rs.next()){
            	rst = new HashMap<String, String>();
    			rst.put("CM_SIGNNM", rs.getString("CM_SIGNNM"));
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
    		rtList = null;
    		return rtObj;
    		
    		
		} catch (SQLException sqlexception) {
			sqlexception.printStackTrace();			
			throw sqlexception;
		} catch (Exception exception) {
			exception.printStackTrace();				
			throw exception;
		}finally{
			if (strQuery != null)	strQuery = null;
			if (rtObj != null)	rtObj = null;
			if (rs != null)     try{rs.close();}catch (Exception ex){ex.printStackTrace();}
			if (pstmt != null)  try{pstmt.close();}catch (Exception ex2){ex2.printStackTrace();}
			if (conn != null){
				try{
					ConnectionResource.release(conn);
				}catch(Exception ex3){
					ex3.printStackTrace();
				}
			}
		}

		
	}//end of Cbo_SignNMADD() method statement	
	
    public int get_SignNM_cnt(String UserId,String SignNM) throws SQLException, Exception {
		Connection        conn        = null;
		PreparedStatement pstmt       = null;
		ResultSet         rs          = null;
		StringBuffer      strQuery    = new StringBuffer();
        int               rtInt       = 0;
		
		ConnectionContext connectionContext = new ConnectionResource();
		
		try {
			conn = connectionContext.getConnection();
			strQuery.append("select Count(*) as CNT from cmm0045 \n");
			strQuery.append("where CM_SIGNNM= ? 				\n");
			strQuery.append("and CM_USERID= ?					\n");
			pstmt = conn.prepareStatement(strQuery.toString());
			pstmt.setString(1, SignNM);
			pstmt.setString(2, UserId);
            rs = pstmt.executeQuery();
            if (rs.next()){
            	rtInt = Integer.parseInt(rs.getString("CNT"));
            }
            rs.close();
            pstmt.close();
            conn.close();
			rs = null;
			pstmt = null;
			conn = null;
            
            return rtInt;
            
		} catch (SQLException sqlexception) {
			sqlexception.printStackTrace();			
			throw sqlexception;
		} catch (Exception exception) {
			exception.printStackTrace();
			throw exception;
		}finally{
			if (strQuery != null)	strQuery = null;
			if (rs != null)     try{rs.close();}catch (Exception ex){ex.printStackTrace();}
			if (pstmt != null)  try{pstmt.close();}catch (Exception ex2){ex2.printStackTrace();}
			if (conn != null){
				try{
					ConnectionResource.release(conn);
				}catch(Exception ex3){
					ex3.printStackTrace();
				}
			}
		}
		
	}//end of SignNM_cnt() method statement	
    
    
	
    public Object[] get_Cbo_Select(String UserID,String SignNM) throws SQLException, Exception {
		Connection        conn        = null;
		PreparedStatement pstmt       = null;
		ResultSet         rs          = null;
		StringBuffer      strQuery    = new StringBuffer();
		ArrayList<HashMap<String, String>>  rtList	= new ArrayList<HashMap<String, String>>();
		HashMap<String, String>			  	rst	 	= null;
        Object[]		 rtObj		  = null;	
		
		ConnectionContext connectionContext = new ConnectionResource();
		
		try {
			conn = connectionContext.getConnection();
			strQuery.append("select distinct a.cm_userid,a.cm_username,b.cm_deptname,c.cm_CODEname as pos,c.cm_seqno,e.CM_SIGNSTEP,e.CM_SIGNCD,d.cm_codename as SIGNCD\n");
			strQuery.append("from cmm0040 a,cmm0100 b,cmm0020 c,cmm0020 d,cmm0045 e				\n");
			strQuery.append("where a.cm_active='1'												\n");
			strQuery.append("and e.cm_userid=	?												\n");
			strQuery.append("and e.CM_SIGNNM=	?												\n");
			strQuery.append("and a.cm_userid=e.CM_SIGNUSER										\n");
			strQuery.append("and a.cm_project=b.cm_deptcd										\n");
			strQuery.append("and C.CM_MACODE='POSITION' AND C.CM_MICODE=a.cm_position			\n");
			strQuery.append("and d.cm_macode='SIGNCD' and d.cm_micode=e.CM_SIGNCD				\n");
			strQuery.append("order by e.CM_SIGNSTEP												\n");

			pstmt = conn.prepareStatement(strQuery.toString());
			pstmt.setString(1, UserID);
			pstmt.setString(2, SignNM);			
            rs = pstmt.executeQuery();            
            rtList.clear();
           
            while (rs.next()){
            	rst = new HashMap<String, String>();
				rst.put("CM_USERID", rs.getString("cm_userid"));
				rst.put("CM_USERNAME", rs.getString("cm_username"));
				rst.put("TEAMNAME", rs.getString("cm_deptname"));
				rst.put("POSITION", rs.getString("pos"));
				rst.put("CM_SEQNO", rs.getString("cm_seqno"));
				rst.put("CM_SIGNSTEP", rs.getString("CM_SIGNSTEP"));
				rst.put("ITEM7", rs.getString("CM_SIGNCD"));				
				rst.put("ITEM1", rs.getString("SIGNCD")); //codename
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
    		rtList = null;
    		return rtObj;
            
		} catch (SQLException sqlexception) {
			sqlexception.printStackTrace();			
			throw sqlexception;
		} catch (Exception exception) {
			exception.printStackTrace();
			throw exception;
		}finally{
			if (strQuery != null)	strQuery = null;
			if (rtObj != null)	rtObj = null;			
			if (rs != null)     try{rs.close();}catch (Exception ex){ex.printStackTrace();}
			if (pstmt != null)  try{pstmt.close();}catch (Exception ex2){ex2.printStackTrace();}
			if (conn != null){
				try{
					ConnectionResource.release(conn);
				}catch(Exception ex3){
					ex3.printStackTrace();
				}
			}
		}
		
	}//end of Cbo_Select() method statement
    
    public int getTblUpdate(String UserId,String SysCd,String Lbl_Dir) throws SQLException, Exception{
    	Connection			conn				= null;
    	PreparedStatement	pstmt				= null;
    	StringBuffer		strQuery			= new StringBuffer();
    	ConnectionContext   connectionContext	= new ConnectionResource();
    	int					rtn_cnt				= 0;
    	
    	try{
    		conn = connectionContext.getConnection();
    		
    		strQuery.setLength(0);
    		strQuery.append("delete cmd0200 where cd_syscd=? \n");//SysCd
    		strQuery.append(" and cd_userid=?  \n");//UserId
    		pstmt = conn.prepareStatement(strQuery.toString());
    		pstmt.setString(1, SysCd);
    		pstmt.setString(2, UserId);
    		rtn_cnt = pstmt.executeUpdate();
    		pstmt.close();
    		
    		strQuery.setLength(0);
    		strQuery.append("insert into cmd0200 (CD_USERID,CD_SYSCD,CD_DEVHOME) values (\n");
    		strQuery.append("?, \n");//UserId
			strQuery.append("?, \n");//SysCd
			strQuery.append("?) \n");//Lbl_Dir
			pstmt = conn.prepareStatement(strQuery.toString());
			pstmt.setString(1, UserId);
			pstmt.setString(2, SysCd);
			pstmt.setString(3, Lbl_Dir);
			rtn_cnt = pstmt.executeUpdate();
			pstmt.close();
			
			conn.close();
			pstmt = null;
			conn = null;
			return rtn_cnt;
			
			
    	} catch (SQLException sqlexception){
    		sqlexception.printStackTrace();
    		throw sqlexception;
 		} catch (Exception exception) {
 			exception.printStackTrace();
 			throw exception;
 		}finally{
			if (strQuery != null)	strQuery = null;
 			if (pstmt != null)  try{pstmt.close();}catch (Exception ex2){ex2.printStackTrace();}
 			if (conn != null){
 				try{ 
 					ConnectionResource.release(conn);
 				}catch(Exception ex3){
 					ex3.printStackTrace();
 				}
 			}
 		}
    }
    
    public int getTblDelete(String UserId,ArrayList<HashMap<String, String>> rvList) throws SQLException, Exception{
    	Connection			conn				= null;
    	PreparedStatement	pstmt				= null;
    	StringBuffer		strQuery			= new StringBuffer();
    	ConnectionContext   connectionContext	= new ConnectionResource();
    	int					rtn_cnt				= 0;
    	
    	try{
    		conn = connectionContext.getConnection();

    		strQuery.setLength(0);
    		strQuery.append("delete cmd0200 where \n");//SysCd
    		strQuery.append(" cd_userid=?  \n");//UserId
    		pstmt = conn.prepareStatement(strQuery.toString());
    		pstmt.setString(1, UserId);
    		rtn_cnt = pstmt.executeUpdate();
    		pstmt.close();
    					
    		for (int i=0 ; i<rvList.size() ; i++){
	    		strQuery.setLength(0);
	    		strQuery.append("insert into cmd0200 (CD_USERID,CD_SYSCD,CD_DEVHOME) values (\n");
	    		strQuery.append("?, \n");//UserId
				strQuery.append("?, \n");//SysCd
				strQuery.append("?) \n");//Lbl_Dir
				pstmt = conn.prepareStatement(strQuery.toString());
				pstmt.setString(1, UserId);
				pstmt.setString(2, rvList.get(i).get("cd_syscd"));
				pstmt.setString(3, rvList.get(i).get("cd_devhome"));
				rtn_cnt = pstmt.executeUpdate();
				pstmt.close();
    		}
    		
    		conn.commit();
    		conn.close();
			
			pstmt = null;
			conn = null;
    		return rtn_cnt;
    		
    	} catch (SQLException sqlexception){
    		sqlexception.printStackTrace();
    		throw sqlexception;
 		} catch (Exception exception) {
 			exception.printStackTrace();
 			throw exception;
 		}finally{
			if (strQuery != null)	strQuery = null;
 			if (pstmt != null)  try{pstmt.close();}catch (Exception ex2){ex2.printStackTrace();}
 			if (conn != null){
 				try{
 					ConnectionResource.release(conn);
 				}catch(Exception ex3){
 					ex3.printStackTrace();
 				}
 			}
 		}
    }
    
    public int get_myGrid2_insert(String UserId,ArrayList<HashMap<String, String>> rtList,int Index,String SignName) throws SQLException, Exception {
		Connection        conn        			  = null;
		PreparedStatement pstmt       			  = null;
		StringBuffer      strQuery   			  = new StringBuffer();
		int               rtn_cnt				  = 0;
		ConnectionContext connectionContext 	  = new ConnectionResource();
		
		try {
			conn = connectionContext.getConnection();
			
		    if (Index ==1){
				strQuery.setLength(0);
				strQuery.append(" delete cmm0045 where cm_userid= ? 		\n");
				strQuery.append(" and CM_SIGNNM= ?							\n");
				pstmt = conn.prepareStatement(strQuery.toString());
				pstmt.setString(1, UserId);
	            pstmt.setString(2, SignName);
	            rtn_cnt = pstmt.executeUpdate();
	            pstmt.close();
		    }
	    
			for (int i=0 ; i<rtList.size() ; i++){           
				strQuery.setLength(0);
				strQuery.append("insert into cmm0045 (CM_USERID,CM_SIGNUSER,CM_LASTDT,CM_SIGNNM,CM_SIGNSTEP,CM_SIGNCD) values (\n");
				strQuery.append("?, \n");  //UserId
				strQuery.append("?, \n");//rtList.get(i).get("CM_USERID")
				strQuery.append("SYSDATE, \n");
				strQuery.append("?, \n");//Txt_SignNM or Cbo_SignNM.Text
				strQuery.append("?, \n");//리스트 순서(NO)
				strQuery.append("?) \n");//rtList.get(i).get("ITEM7")	                
				pstmt = conn.prepareStatement(strQuery.toString());
				pstmt.setString(1, UserId);
				pstmt.setString(2, rtList.get(i).get("CM_USERID"));
				pstmt.setString(3, SignName);
				pstmt.setString(4, Integer.toString(i+1));
				pstmt.setString(5, rtList.get(i).get("ITEM7"));				
				rtn_cnt = pstmt.executeUpdate();
				pstmt.close();
			}
		    
			//conn.commit();
			conn.close();
			
			pstmt = null;
			conn = null;
			return rtn_cnt;
			
		} catch (SQLException sqlexception) {
			
			sqlexception.printStackTrace();
			throw sqlexception;
		} catch (Exception exception) {
			exception.printStackTrace();
			throw exception;
		}finally{
			if (strQuery != null)	strQuery = null;
			if (pstmt != null)  try{pstmt.close();}catch (Exception ex2){ex2.printStackTrace();}
			if (conn != null){
				try{ 
					ConnectionResource.release(conn);
				}catch(Exception ex3){
					ex3.printStackTrace();
				}
			}
		}
		
	}//end of Txt_SignNM_insert() method statement
    
        
    public Object[] get_Txt_SignNM_delete(String USERID,String SIGNNM) throws SQLException, Exception {
		Connection        conn        	= null;
		PreparedStatement pstmt       	= null;
		StringBuffer      strQuery   	= new StringBuffer();
		ArrayList<HashMap<String, String>>  rtList	= new ArrayList<HashMap<String, String>>();
		HashMap<String, String>			  	rst	 	= null;
        Object[]		 rtObj		  = null;	
		int               rtn_cnt       = 0;
		ConnectionContext connectionContext = new ConnectionResource();
		
		conn = connectionContext.getConnection();		
		try {

			conn = connectionContext.getConnection();

			strQuery.setLength(0);
			strQuery.append(" delete cmm0045 where cm_userid= ? 		\n");
			strQuery.append(" and CM_SIGNNM= ?							\n");
		
			//pstmt = new LoggableStatement(conn,strQuery.toString());
			pstmt = conn.prepareStatement(strQuery.toString());
			pstmt.setString(1, USERID);
            pstmt.setString(2, SIGNNM); 
            
            rtn_cnt = pstmt.executeUpdate();
            pstmt.close();
            
            rtList.clear();
			rst = new HashMap<String, String>();
			rst.put("ID","Txt_SignNM_delete");
			rst.put("result",Integer.toString(rtn_cnt));
			rst.put("TASK","DELETE CMM0045");
			rtList.add(rst);
			rst = null;
			
			rtObj =  rtList.toArray();
			rtList = null;
			conn.close();
			pstmt = null;
			conn = null;
			
			return rtObj;			
		    
		} catch (SQLException sqlexception) {
			
			sqlexception.printStackTrace();
			throw sqlexception;
		} catch (Exception exception) {
			exception.printStackTrace();
			throw exception;
		}finally{
			if (strQuery != null)	strQuery = null;
			if (rtObj != null)	rtObj = null;				
			if (pstmt != null)  try{pstmt.close();}catch (Exception ex2){ex2.printStackTrace();}
			if (conn != null){
				try{ 
					ConnectionResource.release(conn);
				}catch(Exception ex3){
					ex3.printStackTrace();
				}
			}
		}
		
	}//end of Txt_SignNM_delete() method statement	
    
    public Object[] getSql_Qry(String UserId) throws SQLException, Exception {
		Connection        conn        	= null;
		PreparedStatement pstmt       	= null;
		ResultSet         rs          	= null;
		StringBuffer      strQuery   	= new StringBuffer();
		ArrayList<HashMap<String, String>>  rtList	= new ArrayList<HashMap<String, String>>();
		HashMap<String, String>			  	rst	 	= null;
        Object[]		  rtObj		    = null;
		ConnectionContext connectionContext = new ConnectionResource();
		
		conn = connectionContext.getConnection();		
		try {

			conn = connectionContext.getConnection();

			strQuery.setLength(0);
			strQuery.append("select b.cd_syscd,a.cm_sysmsg,b.cd_devhome \n");
			strQuery.append(" from cmd0200 b,cmm0030 a \n");
			strQuery.append(" where b.cd_userid=? \n");//UserId
			strQuery.append("   and b.cd_syscd=a.cm_syscd \n");
			pstmt = conn.prepareStatement(strQuery.toString());
			pstmt.setString(1, UserId);
			rs = pstmt.executeQuery();
			rtList.clear();
	        while(rs.next()){
				rst = new HashMap<String, String>();
				rst.put("NO", Integer.toString(rs.getRow()));
				if (rs.getString("cm_sysmsg") != null){
					rst.put("cm_sysmsg", rs.getString("cm_sysmsg"));
				}else{
					continue;
					//rst.put("cm_sysmsg", "시스템명이 존재하지않습니다.");
				}
				rst.put("cd_syscd", rs.getString("cd_syscd"));
				rst.put("cd_devhome", rs.getString("cd_devhome"));
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
			rtList = null;
			return rtObj;	        
		    
		} catch (SQLException sqlexception) {			
			sqlexception.printStackTrace();
			throw sqlexception;
		} catch (Exception exception) {
			exception.printStackTrace();				
			throw exception;
		}finally{
			if (strQuery != null)	strQuery = null;
			if (rtObj != null)	rtObj = null;				
			if (pstmt != null)  try{pstmt.close();}catch (Exception ex2){ex2.printStackTrace();}
			if (conn != null){
				try{ 
					ConnectionResource.release(conn);
				}catch(Exception ex3){
					ex3.printStackTrace();
				}
			}
		}
		
	}//end of getSql_Qry() method statement	
    
    //20090722_결재정보 설정_sjm
    public int setNoti(String noti,String UserId) throws SQLException, Exception{
    	Connection			conn				= null;
    	PreparedStatement	pstmt				= null;
    	StringBuffer		strQuery			= new StringBuffer();
    	ConnectionContext   connectionContext	= new ConnectionResource();
    	int					rtn_cnt				= 0;
    	
    	try{
    		conn = connectionContext.getConnection();
    		
    		strQuery.setLength(0);
    		strQuery.append("update cmm0040 set CM_NOTICD=?	\n");
    		strQuery.append(" where cm_userid=? 			\n");//UserId
    		pstmt = conn.prepareStatement(strQuery.toString());
    		pstmt.setString(1, noti);
    		pstmt.setString(2, UserId);
    		rtn_cnt = pstmt.executeUpdate();
    		
    		pstmt.close();
            conn.close();

			pstmt = null;
			conn = null;
			
			return rtn_cnt;
			
    	} catch (SQLException sqlexception){
    		sqlexception.printStackTrace();
    		throw sqlexception;
 		} catch (Exception exception) {
 			exception.printStackTrace();
 			throw exception;
 		}finally{
			if (strQuery != null)	strQuery = null;
 			if (pstmt != null)  try{pstmt.close();}catch (Exception ex2){ex2.printStackTrace();}
 			if (conn != null){
 				try{ 
 					ConnectionResource.release(conn);
 				}catch(Exception ex3){
 					ex3.printStackTrace();
 				}
 			}
 		}
    }
    
    public String getNoti(String UserId) throws SQLException, Exception {
		Connection        conn        = null;
		PreparedStatement pstmt       = null;
		ResultSet         rs          = null;
		StringBuffer      strQuery    = new StringBuffer();
        String            rtStr       = "";
        
		ConnectionContext connectionContext = new ConnectionResource();
		
		try {
			conn = connectionContext.getConnection();
			strQuery.append("select cm_noticd from cmm0040 \n");
			strQuery.append("where CM_USERID= ? 				\n");
			pstmt = conn.prepareStatement(strQuery.toString());
			pstmt.setString(1, UserId);
            rs = pstmt.executeQuery();
            if (rs.next()){
            	rtStr = rs.getString("cm_noticd");
            }
            rs.close();
            pstmt.close();
            conn.close();
			rs = null;
			pstmt = null;
			conn = null;
            
            return rtStr;
            
		} catch (SQLException sqlexception) {
			sqlexception.printStackTrace();			
			throw sqlexception;
		} catch (Exception exception) {
			exception.printStackTrace();
			throw exception;
		}finally{
			if (strQuery != null)	strQuery = null;
			if (rs != null)     try{rs.close();}catch (Exception ex){ex.printStackTrace();}
			if (pstmt != null)  try{pstmt.close();}catch (Exception ex2){ex2.printStackTrace();}
			if (conn != null){
				try{
					ConnectionResource.release(conn);
				}catch(Exception ex3){
					ex3.printStackTrace();
				}
			}
		}
		
	}
    public Object[] dirOpenChk(ArrayList<String> fileList,String DevHome)	throws SQLException, Exception {
		CreateXml         ecmmtb      = new CreateXml();
		ArrayList<Document> list = new ArrayList<Document>();		
		ArrayList<HashMap<String, String>>  rsval = new ArrayList<HashMap<String, String>>();
		HashMap<String, String>			  rst		  = null;
		
		Object[] returnObjectArray = null;
		
		try {
			int               i = 0;
			String            strPath1 = "";
			int               upSeq       = 0; 
			int               maxSeq      = 100;
			rsval.clear();
			upSeq = 1;
			maxSeq = 1;
			upSeq = maxSeq;
			ecmmtb.init_Xml("ID","cm_seqno","cm_dirpath","cm_upseq","cm_fullpath","isBranch");

			rst = new HashMap<String,String>();
			rst.put("cm_dirpath",DevHome);							
			rst.put("cm_fullpath",DevHome);
			rst.put("cm_upseq",Integer.toString(upSeq));
			rst.put("cm_seqno",Integer.toString(maxSeq));
			rsval.add(maxSeq - 1, rst); 
			
			rst = null;
			upSeq = maxSeq;
			//ecamsLogger.error("++++fileList++++"+fileList.toString());
			for (i=0;fileList.size()>i;i++) {
				strPath1 = fileList.get(i).toString();
				maxSeq = maxSeq + 1;
                
				//ecamsLogger.debug("dirpath,full,upseq,seq====>" + pathDepth[k] + "  , " + strPath1  + "  , " + Integer.toString(upSeq)  + "  , " + Integer.toString(maxSeq));
				rst = new HashMap<String,String>();
				rst.put("cm_dirpath",strPath1);							
				rst.put("cm_fullpath",DevHome+"/"+strPath1);
				rst.put("cm_upseq",Integer.toString(upSeq));
				rst.put("cm_seqno",Integer.toString(maxSeq));
				rsval.add(maxSeq - 1, rst); 
				rst = null;
			}
			//ecamsLogger.error("++++++++rsval++++++++"+rsval.toString());
			if (rsval.size() > 0) {
				
			    String strBran = "";
				for (i = 0;rsval.size() > i;i++) {
					strBran = "true";
				//ecamsLogger.error("ecmmtb.addXML");
					ecmmtb.addXML(rsval.get(i).get("cm_seqno"),rsval.get(i).get("cm_seqno"),
							rsval.get(i).get("cm_dirpath"),rsval.get(i).get("cm_upseq"),
							rsval.get(i).get("cm_fullpath").replace("/","\\\\"),
							strBran,rsval.get(i).get("cm_upseq"));
				}
			}
			list.add(ecmmtb.getDocument());
			//ecmmtb.xml_toFile("/pgm/aitasd2/aaa.xml");
			returnObjectArray = list.toArray();
			list = null;
			ecmmtb = null;
			
			return returnObjectArray;	
			
		}  catch (Exception exception) {
			exception.printStackTrace();
			ecamsLogger.error("## Cmd1300.dirOpenChk() Exception START ##");				
			ecamsLogger.error("## Error DESC : ", exception);	
			ecamsLogger.error("## Cmd1300.dirOpenChk() Exception END ##");				
			throw exception;
		}finally{
			if (rsval != null)  	rsval = null;
		}
	}//end of dirOpenChk() method statement
    
    
    public Object[] getMyPage(String UserId) throws SQLException, Exception {
		Connection        conn        	= null;
		PreparedStatement pstmt       	= null;
		ResultSet         rs          	= null;
		StringBuffer      strQuery   	= new StringBuffer();
		ArrayList<HashMap<String, String>>  rtList	= new ArrayList<HashMap<String, String>>();
		HashMap<String, String>			  	rst	 	= null;
		ConnectionContext connectionContext = new ConnectionResource();
		
		conn = connectionContext.getConnection();		
		try {

			conn = connectionContext.getConnection();

			strQuery.setLength(0);
			strQuery.append("select a.cd_pjid,a.cd_devno,a.cd_chgtit,a.cd_CHGDET,      \n");
		    strQuery.append("       a.CD_REQDEPT,a.CD_CHGDEPT,a.cd_chgcd,              \n");
		    strQuery.append("       c.cm_sysmsg,b.cd_syscd,d.cm_codename qrycd,        \n");
		    strQuery.append("       e.cm_codename sayucd,b.cd_sayucd,b.cd_qrycd        \n");
	        strQuery.append("  from CMD0210 b,cmd0110 a,cmm0030 c,cmm0020 d,cmm0020 e  \n");
	        strQuery.append("  where B.CD_USERID=?            \n ");
	        strQuery.append("    and b.cd_prjno=a.cd_pjid     \n ");
	        strQuery.append("    and b.cd_devno=a.cd_devno    \n ");
	        strQuery.append("    and decode(b.cd_syscd,'00000','99999',b.cd_syscd)=c.cm_syscd \n ");
	        strQuery.append("    and d.cm_macode='REQUEST'    \n ");
	        strQuery.append("    and d.cm_micode=decode(b.cd_qrycd,'00','****',b.cd_qrycd) \n");
	        strQuery.append("    and e.cm_macode='REQCD'      \n ");
	        strQuery.append("    and e.cm_micode=b.cd_sayucd  \n ");
    		strQuery.append("  order by a.cd_pjid desc,a.cd_devno \n");
			pstmt = conn.prepareStatement(strQuery.toString());
			//pstmt = new LoggableStatement(conn, strQuery.toString());
			pstmt.setString(1, UserId);
        	////ecamsLogger.error(((LoggableStatement)pstmt).getQueryString());
			rs = pstmt.executeQuery();
			rtList.clear();
	        while(rs.next()){
				rst = new HashMap<String, String>();
				rst.put("cd_syscd", rs.getString("cd_syscd"));
				rst.put("cd_qrycd", rs.getString("cd_qrycd"));
				rst.put("cd_sayucd", rs.getString("cd_sayucd"));
				rst.put("cd_pjid", rs.getString("cd_pjid"));
				rst.put("cd_devno", rs.getString("cd_devno"));
				rst.put("prjno", rs.getString("cd_pjid") + "-" + rs.getString("cd_devno"));
				rst.put("cm_sysmsg", rs.getString("cm_sysmsg"));
				rst.put("qryname", rs.getString("qrycd"));
				rst.put("sayuname", rs.getString("sayucd"));
				if (rs.getString("cd_chgcd").equals("1")){
	        		rst.put("chg_tyMsg", "프로젝트");
	        	} else if(rs.getString("cd_chgcd").equals("2")){
	        		rst.put("chg_tyMsg", "정상변경");
	        	} else if(rs.getString("cd_chgcd").equals("3")){
	        		rst.put("chg_tyMsg", "단순변경");
	        	} else if(rs.getString("cd_chgcd").equals("4")){
	        		rst.put("chg_tyMsg", "비상변경");
	        	}
				if (rs.getString("cd_syscd").equals("00000")) rst.put("cm_sysmsg", "전체");
				if (rs.getString("cd_qrycd").equals("00")) rst.put("qryname","전체");
				
				rst.put("reqdept", rs.getString("CD_REQDEPT"));
				rst.put("CHGdept", rs.getString("CD_CHGDEPT"));
				rst.put("Title", rs.getString("cd_chgtit"));
				rst.put("chg_cnt", rs.getString("cd_CHGDET"));
				rst.put("selected", "0");
				rtList.add(rst);
				rst = null;
	        }
	        rs.close();
	        pstmt.close();
	        
	        conn.close();
			rs = null;
			pstmt = null;
			conn = null;
			
			return rtList.toArray();	        
		    
		} catch (SQLException sqlexception) {
			sqlexception.printStackTrace();
			ecamsLogger.error("## Cmd1300.getMyPage() SQLException START ##");
			ecamsLogger.error("## Error DESC : ", sqlexception);	
			ecamsLogger.error("## Cmd1300.getMyPage() SQLException END ##");			
			throw sqlexception;
		} catch (Exception exception) {
			exception.printStackTrace();
			ecamsLogger.error("## Cmd1300.getMyPage() Exception START ##");				
			ecamsLogger.error("## Error DESC : ", exception);	
			ecamsLogger.error("## Cmd1300.getMyPage() Exception END ##");				
			throw exception;
		}finally{
			if (strQuery != null) 	strQuery = null;
			if (rs != null)     try{rs.close();}catch (Exception ex){ex.printStackTrace();}
			if (pstmt != null)  try{pstmt.close();}catch (Exception ex2){ex2.printStackTrace();}
			if (conn != null){
				try{
					ConnectionResource.release(conn);
				}catch(Exception ex3){
					ecamsLogger.error("## Cmd1300.getMyPage() connection release exception ##");
					ex3.printStackTrace();
				}
			}
		}		
	}//end of getMyPage() method statement	
    public String setMyPage(HashMap<String,String> etcData,ArrayList<HashMap<String,String>> prjList) throws SQLException, Exception {
		Connection        conn        	= null;
		PreparedStatement pstmt       	= null;
		ResultSet         rs          	= null;
		StringBuffer      strQuery   	= new StringBuffer();
		ConnectionContext connectionContext = new ConnectionResource();
		
		conn = connectionContext.getConnection();		
		try {
			
			conn = connectionContext.getConnection();
			
			int parmCnt = 0;
			boolean insSw = false;
			for (int i=0;prjList.size()>i;i++) {
				parmCnt = 0;
				strQuery.setLength(0);
				strQuery.append("delete cmd0210                     \n");
		        strQuery.append("  where CD_USERID=?                \n ");
		        strQuery.append("    and cd_syscd=? and cd_qrycd=?  \n ");
		        strQuery.append("    and cd_sayucd=?                \n ");
		        strQuery.append("    and cd_prjno=? and cd_devno=?  \n ");
		        
				pstmt = conn.prepareStatement(strQuery.toString());
				//pstmt = new LoggableStatement(conn, strQuery.toString());
				pstmt.setString(++parmCnt, etcData.get("userid"));
				pstmt.setString(++parmCnt, etcData.get("syscd"));
				pstmt.setString(++parmCnt, etcData.get("qrycd"));
				pstmt.setString(++parmCnt, etcData.get("sayucd"));
				pstmt.setString(++parmCnt, prjList.get(i).get("prjno"));
				pstmt.setString(++parmCnt, prjList.get(i).get("devno"));
				////ecamsLogger.error(((LoggableStatement)pstmt).getQueryString());
				pstmt.executeUpdate();
				pstmt.close();
				
				parmCnt = 0;
				strQuery.setLength(0);
				strQuery.append("insert into cmd0210               \n");
				strQuery.append("  (CD_USERID,CD_SYSCD,CD_QRYCD,CD_SAYUCD,CD_PRJNO,CD_DEVNO,CD_LASTDT) \n");
				strQuery.append("values (?, ?, ?, ?, ?, ?, SYSDATE) \n");
				
				pstmt = conn.prepareStatement(strQuery.toString());
				//pstmt = new LoggableStatement(conn, strQuery.toString());
				pstmt.setString(++parmCnt, etcData.get("userid"));
				pstmt.setString(++parmCnt, etcData.get("syscd"));
				pstmt.setString(++parmCnt, etcData.get("qrycd"));
				pstmt.setString(++parmCnt, etcData.get("sayucd"));
				pstmt.setString(++parmCnt, prjList.get(i).get("prjno"));
				pstmt.setString(++parmCnt, prjList.get(i).get("devno"));
				////ecamsLogger.error(((LoggableStatement)pstmt).getQueryString());
				pstmt.executeUpdate();
				pstmt.close();
				
				insSw = false;
				parmCnt = 0;
				strQuery.setLength(0);
				strQuery.append("select count(*) cnt from cmd0110 \n");
				strQuery.append(" where cd_pjid=? and cd_devno=?  \n");
				
				pstmt = conn.prepareStatement(strQuery.toString());
				//pstmt = new LoggableStatement(conn, strQuery.toString());
				pstmt.setString(++parmCnt, prjList.get(i).get("prjno"));
				pstmt.setString(++parmCnt, prjList.get(i).get("devno"));
				////ecamsLogger.error(((LoggableStatement)pstmt).getQueryString());
				rs = pstmt.executeQuery();
				if (rs.next()) {
					if (rs.getInt("cnt") == 0) {
						insSw = true;
					}
				}
				rs.close();
				pstmt.close();
				
				if (insSw == true) {
					parmCnt = 0;
					strQuery.setLength(0);
	            	strQuery.append("insert into CMD0110 (CD_PJID,CD_DEVNO,CD_REQDEPT,CD_CHGDEPT,CD_CHGUPDPT,CD_CREATOR, \n");
	            	strQuery.append("                      CD_CHGCD,CD_CHGSAYU,CD_CHGTIT,CD_CHGDET,CD_CHGSTA) \n");
	            	strQuery.append("(select pj_no,dev_chg_no,wso_dmd_orgn_name,chg_dmd_orgn_name,chg_dmd_up_orgn_name, \n");
	            	strQuery.append("        mpr_no,chg_ty,chg_rsn,chg_name,chg_cnt,chg_stt from vi_pms_chg_pfmc \n");
	            	strQuery.append("  where pj_no=? and  dev_chg_no=?) \n");
	            	
	            	pstmt = conn.prepareStatement(strQuery.toString());
	            	//pstmt = new LoggableStatement(conn, strQuery.toString());
					pstmt.setString(++parmCnt, prjList.get(i).get("prjno"));
					pstmt.setString(++parmCnt, prjList.get(i).get("devno"));
	            	////ecamsLogger.error(((LoggableStatement)pstmt).getQueryString());
	            	pstmt.executeUpdate();	            	
	            	pstmt.close();
				}
			}
	        conn.close();
			rs = null;
			pstmt = null;
			conn = null;
			return "OK";	        
		    
		} catch (SQLException sqlexception) {
			sqlexception.printStackTrace();
			ecamsLogger.error("## Cmd1300.setMyPage() SQLException START ##");
			ecamsLogger.error("## Error DESC : ", sqlexception);	
			ecamsLogger.error("## Cmd1300.setMyPage() SQLException END ##");			
			throw sqlexception;
		} catch (Exception exception) {
			exception.printStackTrace();
			ecamsLogger.error("## Cmd1300.setMyPage() Exception START ##");				
			ecamsLogger.error("## Error DESC : ", exception);	
			ecamsLogger.error("## Cmd1300.setMyPage() Exception END ##");				
			throw exception;
		}finally{
			if (strQuery != null) 	strQuery = null;
			if (rs != null)     try{rs.close();}catch (Exception ex){ex.printStackTrace();}
			if (pstmt != null)  try{pstmt.close();}catch (Exception ex2){ex2.printStackTrace();}
			if (conn != null){
				try{
					ConnectionResource.release(conn);
				}catch(Exception ex3){
					ecamsLogger.error("## Cmd1300.setMyPage() connection release exception ##");
					ex3.printStackTrace();
				}
			}
		}		
	}//end of setMyPage() method statement	
    public String delMyPage(String UserId,ArrayList<HashMap<String,String>> delPrjList) throws SQLException, Exception {
		Connection        conn        	= null;
		PreparedStatement pstmt       	= null;
		ResultSet         rs          	= null;
		StringBuffer      strQuery   	= new StringBuffer();
		ConnectionContext connectionContext = new ConnectionResource();
		
		conn = connectionContext.getConnection();		
		try {

			conn = connectionContext.getConnection();
			
			int parmCnt = 0;
			int retInt = 0;
			for (int i=0 ; delPrjList.size()>i ; i++) {
				strQuery.setLength(0);
				strQuery.append("delete cmd0210                     \n");
		        strQuery.append("  where CD_USERID=?                \n ");
		        strQuery.append("    and cd_syscd=? and cd_qrycd=?  \n ");
		        strQuery.append("    and cd_sayucd=?                \n ");
		        strQuery.append("    and cd_prjno=? and cd_devno=?  \n ");
		        
				pstmt = conn.prepareStatement(strQuery.toString());
				//pstmt = new LoggableStatement(conn, strQuery.toString());
				parmCnt = 0;
				pstmt.setString(++parmCnt, UserId);
				pstmt.setString(++parmCnt, delPrjList.get(i).get("syscd"));
				pstmt.setString(++parmCnt, delPrjList.get(i).get("qrycd"));
				pstmt.setString(++parmCnt, delPrjList.get(i).get("sayucd"));
				pstmt.setString(++parmCnt, delPrjList.get(i).get("prjno"));
				pstmt.setString(++parmCnt, delPrjList.get(i).get("devno"));
				////ecamsLogger.error(((LoggableStatement)pstmt).getQueryString());
				retInt = pstmt.executeUpdate();
				pstmt.close();
			}
	        conn.close();
			rs = null;
			pstmt = null;
			conn = null;
	        if (retInt>0){
	        	return "OK";
	        }else{
	        	return "denied";
	        }
		    
		} catch (SQLException sqlexception) {
			sqlexception.printStackTrace();
			ecamsLogger.error("## Cmd1300.delMyPage() SQLException START ##");
			ecamsLogger.error("## Error DESC : ", sqlexception);	
			ecamsLogger.error("## Cmd1300.delMyPage() SQLException END ##");			
			throw sqlexception;
		} catch (Exception exception) {
			exception.printStackTrace();
			ecamsLogger.error("## Cmd1300.delMyPage() Exception START ##");				
			ecamsLogger.error("## Error DESC : ", exception);	
			ecamsLogger.error("## Cmd1300.delMyPage() Exception END ##");				
			throw exception;
		}finally{
			if (strQuery != null) 	strQuery = null;
			if (rs != null)     try{rs.close();}catch (Exception ex){ex.printStackTrace();}
			if (pstmt != null)  try{pstmt.close();}catch (Exception ex2){ex2.printStackTrace();}
			if (conn != null){
				try{
					ConnectionResource.release(conn);
				}catch(Exception ex3){
					ecamsLogger.error("## Cmd1300.delMyPage() connection release exception ##");
					ex3.printStackTrace();
				}
			}
		}		
	}//end of delMyPage() method statement	
public ArrayList<HashMap<String, String>> cmdControl(String userId, String targetGB, String Sv_GbnCd, String Sv_Dir, String agentIp, String agentDir, String paramPid) throws Exception {
		
    	ArrayList<HashMap<String, String>>	rtList 	= new ArrayList<HashMap<String, String>>();
    	Object[]		  					rtObj	= null;
    	
		String			  tmpPath     		= "";
		String			  strBinPath  		= "";
		int               nRet              = 0;
		
		File shfile=null;
		String  shFileName = "";
		String  fileName = "";
		
		boolean errSw = false;
		
		OutputStreamWriter writer = null;
		String[] strAry = null;
		Runtime  run = null;
		Process p = null;
		Connection        conn        = null;
		ResultSet         rs          = null;
		PreparedStatement pstmt       = null;
		StringBuffer      strQuery    = new StringBuffer();
		ConnectionContext connectionContext = new ConnectionResource();
		String rtString = "";
		
		try {			
			SystemPath cTempGet = new SystemPath();
			tmpPath = cTempGet.getTmpDir("99");
			strBinPath = cTempGet.getTmpDir("14");
			cTempGet = null;
			
			shFileName = tmpPath + "/" + userId + "_cmdcontrol.sh";
			fileName = userId + "_cmdcontrol.file";			
			shfile = new File(shFileName);
			
			if( !(shfile.isFile()) )			//File이 없으면 
			{
				shfile.createNewFile();			//File 생성
			}
			
			// 20221219 ecams_batexec 추가 쿼리
			conn = connectionContext.getConnection();
			conn.setAutoCommit(false);
			strQuery.setLength(0);
			strQuery.append("select cm_ipaddr, cm_port 	\n");
			strQuery.append("  from cmm0010 			\n");
			strQuery.append(" where cm_stno = 'ECAMS'	\n");
			
			pstmt = conn.prepareStatement(strQuery.toString());	
			rs = pstmt.executeQuery();
			if(rs.next()){
				rtString = rs.getString("cm_ipaddr") + " " + rs.getString("cm_port") + " 0";
			}

			writer = new OutputStreamWriter( new FileOutputStream(shFileName), "MS949");
			writer.write("cd " + strBinPath + "\n");
			if("FILELIST".equals(targetGB) && !"9".equals(Sv_GbnCd)) {
//				writer.write("zen " + agentIp + " 29895 0 S 'dir \"" + Sv_Dir + "\" /b/a-d-h-s/on > " + agentDir + "\\" + fileName + "' \n");
				writer.write("./ecams_batexec " +rtString + " \"cd " + strBinPath+ ";" +"./zen " + agentIp + " 29895 0 S 'dir \"" + Sv_Dir + "\" /b/a-d-h-s/on > " + agentDir + "\\" + fileName + "'\" \n");
			}else if("FILELIST".equals(targetGB) && "9".equals(Sv_GbnCd)) {
//				writer.write("zen " + agentIp + " 29895 0 S 'dir \"" + Sv_Dir + "\" /b/s/a-d-h-s/on > " + agentDir + "\\" + fileName + "' \n");
				writer.write("./ecams_batexec " +rtString + " \"cd " + strBinPath+ ";" +"./zen " + agentIp + " 29895 0 S 'dir \"" + Sv_Dir + "\" /b/s/a-d-h-s/on > " + agentDir + "\\" + fileName + "'\" \n");
			}else if("DIRLIST".equals(targetGB) || "DIRLIST2".equals(targetGB)) {
//				writer.write("zen " + agentIp + " 29895 0 S 'dir \"" + Sv_Dir + "\" /b/ad-h/on > " + agentDir + "\\" + fileName + "' \n");
				writer.write("./ecams_batexec " +rtString + " \"cd " + strBinPath+ ";" +"./zen " + agentIp + " 29895 0 S 'dir \"" + Sv_Dir + "\" /b/ad-h/on > " + agentDir + "\\" + fileName + "'\" \n");
			}else if("DIRALL".equals(targetGB)) {
//				writer.write("zen " + agentIp + " 29895 0 S 'dir \"" + Sv_Dir + "\" /b/s/ad-h/on > " + agentDir + "\\" + fileName + "' \n");
				writer.write("./ecams_batexec " +rtString + " \"cd " + strBinPath+ ";" +"./zen " + agentIp + " 29895 0 S 'dir \"" + Sv_Dir + "\" /b/s/ad-h/on > " + agentDir + "\\" + fileName + "'\" \n");
			}else if("DRIVE".equals(targetGB)) {
//				writer.write("zen " + agentIp + " 29895 0 S 'wmic logicaldisk get name > " + agentDir + "\\" + fileName + "' \n");
				writer.write("./ecams_batexec " +rtString + " \"cd " + strBinPath+ ";" +"./zen " + agentIp + " 29895 0 S 'wmic logicaldisk get name > " + agentDir + "\\" + fileName + "'\" \n");
			}
			writer.write("rtval=$?		\n");
			writer.write("exit $rtval	\n");
			writer.close();
						
			strAry = new String[3];
			strAry[0] = "chmod";
			strAry[1] = "777";
			strAry[2] = shFileName;			
			
			run = Runtime.getRuntime();

			ecamsLogger.error("=== chmod 777 > ["+shFileName+"] \n");
			
			p = run.exec(strAry);
			p.waitFor();
			
			run = Runtime.getRuntime();
			
			strAry = new String[2];
			
			strAry[0] = "/bin/sh";
			strAry[1] = shFileName;
			
			p = run.exec(strAry);
			p.waitFor();
			
			run = null;
			writer = null;
			if (p.exitValue() != 0) {
				//shfile.delete();
				shfile = null;
				throw new Exception("스크립트 실행실패. shfile=["+shFileName +"] return=[" + p.exitValue() + "]" );
			}else{ //스크립트 실행 결과파일 수신
//				shfile.delete();
				shfile = null;
				
				shFileName = tmpPath + "/" + userId + "_cmdcontrol2.sh";
				fileName = userId + "_cmdcontrol.file";
				shfile = new File(shFileName);
				
				if( !(shfile.isFile()) )			//File이 없으면 
				{
					shfile.createNewFile();			//File 생성
				}
				
				writer = new OutputStreamWriter( new FileOutputStream(shFileName));
				writer.write("cd " + strBinPath + "\n");
//				writer.write("zen " + agentIp + " 29895 0 G " + tmpPath + "/" + fileName + " " + agentDir.replace("\\","\\\\") + "\\\\" + fileName + "\n");
				writer.write("./ecams_batexec " +rtString + " \"cd " + strBinPath+ ";" +"./zen " + agentIp + " 29895 0 G " + tmpPath + "/" + fileName + " " + agentDir.replace("\\","\\\\") + "\\\\" + fileName + "\" \n");
				writer.write("rtval=$?		\n");
				writer.write("exit $rtval	\n");
				writer.close();
							
				strAry = new String[3];
				strAry[0] = "chmod";
				strAry[1] = "777";
				strAry[2] = shFileName;			
				
				run = Runtime.getRuntime();

				ecamsLogger.error("=== chmod 777 > ["+shFileName+"] \n");
				
				p = run.exec(strAry);
				p.waitFor();
				
				run = Runtime.getRuntime();
				
				strAry = new String[2];
				
				strAry[0] = "/bin/sh";
				strAry[1] = shFileName;
				
				p = run.exec(strAry);
				p.waitFor();
				
				run = null;
				writer = null;
				if (p.exitValue() != 0) {
					//shfile.delete();
					shfile = null;
					throw new Exception("결과파일 수신실패. shfile=["+shFileName +"] return=[" + p.exitValue() + "]" );
				}else{ //결과파일 read
//					shfile.delete();
					shfile = null;		
					
					File rstfile = null;
					BufferedReader in = null;
					String str = null;
					int maxSeq      = 0;
					
					HashMap<String, String>	rst = null;
					
					rstfile = new File(tmpPath + "/" + fileName); // 파일을 불러온다
					
					if (!rstfile.isFile() || !rstfile.exists()) {
						throw new Exception("작업에 실패하였습니다 [작성된 파일 없음 : " + tmpPath + "/" + fileName + "]");
			        }
					
					try {
		        		in = new BufferedReader(new InputStreamReader(new FileInputStream(rstfile),"MS949"));
			            if ("DRIVE".equals(targetGB)) {
			            	in.readLine(); // 빈 첫줄 제거
			            }
			            while ((str = in.readLine()) != null) {
			            	if (str.length() > 0) {
			            		if("FILELIST".equals(targetGB) && !"9".equals(Sv_GbnCd)) {
			        				
			        			}else if("FILELIST".equals(targetGB) && "9".equals(Sv_GbnCd)) {
			        				
			        			}else if("DIRLIST".equals(targetGB)) {
			        				++maxSeq;
			        				rst = new HashMap<String, String>();
			        				rst.put("id", Integer.toString(maxSeq));
			        				rst.put("pId", Integer.toString(maxSeq));
			        				rst.put("name", str);
			        				rst.put("isParent", "true");
			        				rtList.add(rst);
	        						rst = null;
	        						
			        			}else if("DIRLIST2".equals(targetGB)) {
			        				++maxSeq;
			        				
			        				rst = new HashMap<String, String>();
			        				rst.put("id", paramPid + "_" + maxSeq);
			        				rst.put("pId", paramPid);
			        				rst.put("name", str);
			        				rst.put("isParent", "true");
			        				rtList.add(rst);
	        						rst = null;
	        						
			        			}else if("DIRALL".equals(targetGB)) {
			        				
			        			}else if("DRIVE".equals(targetGB)) {
			        				str = str.trim();
			        				str = str.replace("\u0000", ""); // 공백제거
			        				if(str == null || "".equals(str)) continue;
			        				
			        				rst = new HashMap<String, String>();
	        						rst.put("text", str);
	        						rst.put("value", str);
	        						rtList.add(rst);
	        						rst = null;
			        			}
			            	} 		            	
			            } 
		        	}finally {
			            if (in != null) in.close();
		        	}
				}	
			}	
			shfile = null;

			pstmt.close();
			conn.close();
			pstmt = null;
			conn = null;
			rs.close();// 20221219 ecams_batexec 추가 쿼리
			rs = null;
			return rtList;
			
		} catch (SQLException sqlexception) {
			sqlexception.printStackTrace();
			ecamsLogger.error("## Cmd1300.cmdControl() SQLException START ##");
			ecamsLogger.error("## Error DESC : ", sqlexception);	
			ecamsLogger.error("## Cmd1300.cmdControl() SQLException END ##");			
			throw sqlexception;
		} catch (IOException exception) {
	        exception.printStackTrace();
	        ecamsLogger.error("## Error IOException : ", exception);
	   		throw exception;
	    } catch (Exception exception) {
			exception.printStackTrace();
			ecamsLogger.error("## Cmd1300.cmdControl() Exception START ##");
			ecamsLogger.error("## Error DESC : ", exception);
			ecamsLogger.error("## Cmd1300.cmdControl() Exception END ##");
			throw exception;
		}finally{
			p = null;
		}
	}//end of while-loop statement
}//end of Cmd1300 class statement
