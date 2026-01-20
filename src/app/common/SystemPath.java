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
import java.util.List;


public class SystemPath {
	
	Logger ecamsLogger  = EcamsLogger.getLoggerInstance();

	public Object[] geteCAMSDir(String pCode) throws SQLException, Exception {
		Connection        conn        = null;
		PreparedStatement pstmt       = null;
		ResultSet         rs          = null;
		StringBuffer      strQuery    = new StringBuffer();
		ConnectionContext connectionContext = new ConnectionResource();
		ArrayList<HashMap<String, String>>  rsval = new ArrayList<HashMap<String, String>>();
		HashMap<String, String>			  rst		  = null;
		try {
			conn = connectionContext.getConnection();
		    rsval.clear();
			String[] dirCd = pCode.split(",");
			
			strQuery.append("SELECT cm_pathcd,cm_path            \n");
			strQuery.append("  FROM cmm0012                      \n");
			strQuery.append(" WHERE cm_stno = 'ECAMS'            \n");
			strQuery.append("   AND cm_pathcd in (               \n");
			for (int i=0;dirCd.length>i;i++) {
				if (i == 0) strQuery.append("? ");
				else strQuery.append(", ?");
			}
			strQuery.append(")                                   \n");
			
			pstmt = conn.prepareStatement(strQuery.toString());
			for (int i=0;dirCd.length>i;i++) {
				pstmt.setString(i + 1, dirCd[i]);
			}
            rs = pstmt.executeQuery();
            
			while (rs.next()){
				rst = new HashMap<String,String>();
				rst.put("cm_pathcd", rs.getString("cm_pathcd"));
				if (rs.getString("cm_path").indexOf(".")>0) {
					rst.put("cm_path", rs.getString("cm_path"));
				} else {
					if (rs.getString("cm_path").substring(rs.getString("cm_path").length()-1).equals("/"))
						rst.put("cm_path", rs.getString("cm_path"));
					else
						rst.put("cm_path", rs.getString("cm_path")+"/");
				}
				rsval.add(rst);
			}//end of while-loop statement
			rs.close();
			pstmt.close();
			conn.close();

			rs = null;
			pstmt = null;
			return rsval.toArray();
			
		} catch (SQLException sqlexception) {
			sqlexception.printStackTrace();
			ecamsLogger.error("## SystemPath.geteCAMSDir() SQLException START ##");
			ecamsLogger.error("## Error DESC : ", sqlexception);	
			ecamsLogger.error("## SystemPath.geteCAMSDir() SQLException END ##");			
			throw sqlexception;
		} catch (Exception exception) {
			exception.printStackTrace();
			ecamsLogger.error("## SystemPath.geteCAMSDir() Exception START ##");				
			ecamsLogger.error("## Error DESC : ", exception);	
			ecamsLogger.error("## SystemPath.geteCAMSDir() Exception END ##");				
			throw exception;
		}finally{
			if (strQuery != null) 	strQuery = null;
			if (rs != null)     try{rs.close();}catch (Exception ex){ex.printStackTrace();}
			if (pstmt != null)  try{pstmt.close();}catch (Exception ex2){ex2.printStackTrace();}
			if (conn != null){
				try{
					ConnectionResource.release(conn);
				}catch(Exception ex3){
					ecamsLogger.error("## SystemPath.geteCAMSDir() connection release exception ##");
					ex3.printStackTrace();
				}
			}
		}
		

	}//end of geteCAMSDir() method statement
	
	public String getDevHome(String UserId,String SysCd) throws SQLException, Exception {
		Connection        conn        = null;
		PreparedStatement pstmt       = null;
		ResultSet         rs          = null;
		StringBuffer      strQuery    = new StringBuffer();
		ConnectionContext connectionContext = new ConnectionResource();
		
		
		try {
			conn = connectionContext.getConnection();
			String rtString = "";
			
			strQuery.append("SELECT cd_devhome FROM cmd0200    \n");
			strQuery.append(" WHERE cd_syscd=? and cd_userid=? \n");	
			//pstmt =  new LoggableStatement(conn, strQuery.toString());
			pstmt = conn.prepareStatement(strQuery.toString());
			pstmt.setString(1, SysCd);
			pstmt.setString(2, UserId);

			
			//ecamsLogger.debug(((LoggableStatement)pstmt).getQueryString());
            rs = pstmt.executeQuery();
            
			if (rs.next()){
				rtString = rs.getString("cd_devhome").replace("\\", "\\\\"); //로컬톰켓  막음
				//rtString = rs.getString("cd_devhome").replace("\\", "\\");
			}//end of while-loop statement
			rs.close();
			pstmt.close();
			conn.close();
			
			rs = null;
			pstmt = null;
			conn = null;
			
			return rtString;
			
		} catch (SQLException sqlexception) {
			sqlexception.printStackTrace();
			ecamsLogger.error("## SystemPath.getDevHome() SQLException START ##");
			ecamsLogger.error("## Error DESC : ", sqlexception);	
			ecamsLogger.error("## SystemPath.getDevHome() SQLException END ##");			
			throw sqlexception;
		} catch (Exception exception) {
			exception.printStackTrace();
			ecamsLogger.error("## SystemPath.getDevHome() Exception START ##");				
			ecamsLogger.error("## Error DESC : ", exception);	
			ecamsLogger.error("## SystemPath.getDevHome() Exception END ##");				
			throw exception;
		}finally{
			if (strQuery != null) 	strQuery = null;
			if (rs != null)     try{rs.close();}catch (Exception ex){ex.printStackTrace();}
			if (pstmt != null)  try{pstmt.close();}catch (Exception ex2){ex2.printStackTrace();}
			if (conn != null){
				try{
					ConnectionResource.release(conn);
				}catch(Exception ex3){
					ecamsLogger.error("## SystemPath.getDevHome() connection release exception ##");
					ex3.printStackTrace();
				}
			}
		}
	}//end of getDevHome() method statement
	public String getTmpDir(String pCode) throws SQLException, Exception {
		Connection        conn        = null;
		PreparedStatement pstmt       = null;
		ResultSet         rs          = null;
		StringBuffer      strQuery    = new StringBuffer();
		ConnectionContext connectionContext = new ConnectionResource();
		
		
		try {
			conn = connectionContext.getConnection();
			String rtString = "";
			
			strQuery.append("SELECT cm_path \n");
			strQuery.append("FROM cmm0012 \n");
			strQuery.append("WHERE cm_stno = 'ECAMS' \n");
			strQuery.append("AND cm_pathcd = ? \n");
			
			pstmt = conn.prepareStatement(strQuery.toString());
			pstmt.setString(1, pCode);

            rs = pstmt.executeQuery();
            
			while (rs.next()){
				rtString = rs.getString("cm_path");
			}//end of while-loop statement
			rs.close();
			pstmt.close();
			conn.close();
			
			rs = null;
			pstmt = null;
			conn = null;
			
			return rtString;
			
		} catch (SQLException sqlexception) {
			sqlexception.printStackTrace();
			ecamsLogger.error("## SystemPath.getSysPath() SQLException START ##");
			ecamsLogger.error("## Error DESC : ", sqlexception);	
			ecamsLogger.error("## SystemPath.getSysPath() SQLException END ##");			
			throw sqlexception;
		} catch (Exception exception) {
			exception.printStackTrace();
			ecamsLogger.error("## SystemPath.getSysPath() Exception START ##");				
			ecamsLogger.error("## Error DESC : ", exception);	
			ecamsLogger.error("## SystemPath.getSysPath() Exception END ##");				
			throw exception;
		}finally{
			if (strQuery != null) 	strQuery = null;
			if (rs != null)     try{rs.close();}catch (Exception ex){ex.printStackTrace();}
			if (pstmt != null)  try{pstmt.close();}catch (Exception ex2){ex2.printStackTrace();}
			if (conn != null){
				try{
					ConnectionResource.release(conn);
				}catch(Exception ex3){
					ecamsLogger.error("## SystemPath.getSysPath() connection release exception ##");
					ex3.printStackTrace();
				}
			}
		}
	}//end of SelectUserInfo() method statement
	
	public String getTmpDir_conn(String pCode, Connection conn) throws SQLException, Exception {
		PreparedStatement pstmt       = null;
		ResultSet         rs          = null;
		StringBuffer      strQuery    = new StringBuffer();
		
		try {
			String rtString = "";
			
			strQuery.setLength(0);
			strQuery.append("SELECT cm_path FROM cmm0012 \n");
			strQuery.append(" WHERE cm_stno = 'ECAMS' AND cm_pathcd = ? \n");
			pstmt = conn.prepareStatement(strQuery.toString());
			pstmt.setString(1, pCode);
            rs = pstmt.executeQuery();
            
			if (rs.next()){
				rtString = rs.getString("cm_path");
			}
			rs.close();
			pstmt.close();
			
			rs = null;
			pstmt = null;
			
			return rtString;
			
		} catch (SQLException sqlexception) {
			sqlexception.printStackTrace();
			ecamsLogger.error("## SystemPath.getSysPath() SQLException START ##");
			ecamsLogger.error("## Error DESC : ", sqlexception);
			ecamsLogger.error("## SystemPath.getSysPath() SQLException END ##");
			throw sqlexception;
		} catch (Exception exception) {
			exception.printStackTrace();
			ecamsLogger.error("## SystemPath.getSysPath() Exception START ##");
			ecamsLogger.error("## Error DESC : ", exception);
			ecamsLogger.error("## SystemPath.getSysPath() Exception END ##");
			throw exception;
		}finally{
			if (strQuery != null) 	strQuery = null;
			if (rs != null)     try{rs.close();}catch (Exception ex){ex.printStackTrace();}
			if (pstmt != null)  try{pstmt.close();}catch (Exception ex2){ex2.printStackTrace();}
		}
	}//end of SelectUserInfo() method statement
	
	
	public Object[] getSysPath(String UserID,String syscd,String jobcd,String reqcd,String adminCk) throws SQLException, Exception {
		Connection        conn        = null;
		PreparedStatement pstmt       = null;
		ResultSet         rs          = null;
		StringBuffer      strQuery    = new StringBuffer();
		UserInfo		  userInfo	  = new UserInfo();
		CreateXml      ecmmtb      = new CreateXml();
		ArrayList<Document> list = new ArrayList<Document>();
		Object[] returnObjectArray	 = null;		
		
		ConnectionContext connectionContext = new ConnectionResource();
		
		try {
			conn = connectionContext.getConnection();
			Boolean AdminYn= false;
			
			if (adminCk.toUpperCase().equals("Y") || adminCk.equals("1")){
				//AdminYn = userInfo.isAdmin(UserID);
				AdminYn = userInfo.isAdmin_conn(UserID,conn);
			}
			
			userInfo = null;
			
			conn = connectionContext.getConnection();
			
			if (AdminYn){
				strQuery.append("select a.CM_SEQNO,a.CM_DIRPATH,a.CM_UPSEQ,a.CM_FULLPATH,a.CM_DSNCD \n");
				strQuery.append("from cmm0074 a where \n");
				strQuery.append("a.cm_syscd = ? \n");
				strQuery.append("START WITH a.cm_upseq is null \n");
				strQuery.append("CONNECT BY PRIOR a.cm_seqno = a.cm_upseq \n");
				pstmt = conn.prepareStatement(strQuery.toString());
				pstmt.setString(1, syscd);
			} else{
/*				
				strQuery.append("select a.CM_SEQNO,a.CM_DIRPATH,a.CM_UPSEQ,a.CM_FULLPATH,a.CM_DSNCD from cmm0074 a \n");
				strQuery.append("where a.cm_syscd = ? \n");
				strQuery.append("and   a.cm_seqno in (select distinct b.cm_seqno from cmm0074 b \n");
				strQuery.append("					  where b.cm_syscd = ? \n");
				strQuery.append("					  START WITH b.cm_dsncd in (  select distinct d.cm_dsncd \n");
				strQuery.append("					  							  from cmm0044 c,cmm0073 d, cmm0072 e, cmm0036 f \n");
				strQuery.append("					  							  where   c.cm_syscd    = ? \n");
				strQuery.append("					 							  and     c.cm_userid   = ? \n");
				if (!(jobcd.equals("") || jobcd.length() == 0 || jobcd == null)){
					strQuery.append("												  and     rtrim(c.cm_jobcd)= ? \n"); 
				}				
				strQuery.append("					  							  and     c.cm_syscd    = d.cm_syscd \n");
				strQuery.append("					  							  and     c.cm_jobcd    = d.cm_jobcd \n");
				strQuery.append("					  							  and     d.cm_syscd    = e.cm_syscd \n");
				strQuery.append("					  							  and     d.cm_dsncd    = e.cm_dsncd \n");
				strQuery.append("					  							  and     d.cm_syscd    = f.cm_syscd \n");
				if (reqcd.equals("98") || reqcd.equals("99")){
					strQuery.append("					  							  and     substr(f.cm_info,18,1)='1' 	 \n");
				}
				else if (reqcd.equals("99") == false && reqcd.equals("97") == false){
					strQuery.append("					  							  and     substr(f.cm_info,2,1)='1' 	 \n");
				}
				strQuery.append("					  							  and     f.cm_closedt  is null    ) \n");
				strQuery.append("					  CONNECT BY PRIOR b.cm_upseq = b.cm_seqno) \n"); 
				strQuery.append("START WITH a.cm_upseq is null \n"); 
				strQuery.append("CONNECT BY PRIOR a.cm_seqno = a.cm_upseq \n");
*/
				
				strQuery.append("select distinct a.CM_SEQNO,a.CM_DIRPATH,a.CM_UPSEQ,a.CM_FULLPATH,a.CM_DSNCD from cmm0074 a \n");
				strQuery.append("where a.cm_syscd = ? \n");
				strQuery.append("START WITH a.cm_dsncd in (  select distinct d.cm_dsncd \n");
				strQuery.append("							 from cmm0044 c,cmm0073 d, cmm0072 e, cmm0036 f \n");
				strQuery.append("					  		 where   c.cm_syscd    = ? \n");
				strQuery.append("					 		 and     c.cm_userid   = ? \n");
				if (!(jobcd.equals("") || jobcd.length() == 0 || jobcd == null)){
					strQuery.append("						 and     rtrim(c.cm_jobcd)= ? \n"); 
				}				
				strQuery.append("					  		 and     c.cm_syscd    = d.cm_syscd \n");
				strQuery.append("					  		 and     c.cm_jobcd    = d.cm_jobcd \n");
				strQuery.append("					  		 and     d.cm_syscd    = e.cm_syscd \n");
				strQuery.append("					  		 and     d.cm_dsncd    = e.cm_dsncd \n");
				strQuery.append("					  		 and     d.cm_syscd    = f.cm_syscd \n");
				if (reqcd.equals("98") || reqcd.equals("99")){
					strQuery.append("					    and     substr(f.cm_info,18,1)='1' 	 \n");
				}
				else if (reqcd.equals("99") == false && reqcd.equals("97") == false){
					strQuery.append("					    and     substr(f.cm_info,2,1)='1' 	 \n");
				}
				strQuery.append("					  		and     f.cm_closedt  is null  ) \n");
				strQuery.append("CONNECT BY PRIOR a.cm_upseq = a.cm_seqno \n");
				strQuery.append("order by a.cm_seqno\n");
				//pstmt = new LoggableStatement(conn,strQuery.toString());
				pstmt = conn.prepareStatement(strQuery.toString());
				
				pstmt.setString(1, syscd);
				pstmt.setString(2, syscd);
				pstmt.setString(3, UserID);
				if (!(jobcd.equals("") || jobcd.length() == 0 || jobcd == null)){
					pstmt.setString(4, jobcd);
				}
			}
			
            rs = pstmt.executeQuery();
            
            ecmmtb.init_Xml("ID","cm_seqno","cm_dirpath","cm_upseq","cm_fullpath","cm_dsncd","isBranch");
            
			while (rs.next()){
				ecmmtb.addXML(rs.getString("cm_seqno"),rs.getString("cm_seqno"),
						rs.getString("cm_dirpath"),rs.getString("cm_upseq"),
						rs.getString("cm_fullpath"),rs.getString("cm_dsncd"),
						"true",rs.getString("cm_upseq"));
			}//end of while-loop statement
			
			rs.close();
			pstmt.close();
			conn.close();
			
			rs = null;
			pstmt = null;
			conn = null;
			
			list.add(ecmmtb.getDocument());
			
			returnObjectArray = list.toArray();

			list = null;
			ecmmtb = null;
			//ecamsLogger.error(ecmmtb.xml_toStr());
			
			return returnObjectArray;			
			
	
		} catch (SQLException sqlexception) {
			sqlexception.printStackTrace();
			ecamsLogger.error("## SystemPath.getSysPath() SQLException START ##");
			ecamsLogger.error("## Error DESC : ", sqlexception);	
			ecamsLogger.error("## SystemPath.getSysPath() SQLException END ##");			
			throw sqlexception;
		} catch (Exception exception) {
			exception.printStackTrace();
			ecamsLogger.error("## SystemPath.getSysPath() Exception START ##");				
			ecamsLogger.error("## Error DESC : ", exception);	
			ecamsLogger.error("## SystemPath.getSysPath() Exception END ##");				
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
					ecamsLogger.error("## SystemPath.getSysPath() connection release exception ##");
					ex3.printStackTrace();
				}
			}
		}
	}//end of SelectUserInfo() method statement

	
	//public Object[] getSysPath(String UserID,String syscd,String sysgb,String sysfc1,String dirbase ,String jobcd,String reqcd,String adminCk) throws SQLException, Exception {
	public Object[] getDirPath(String UserID,String syscd,String jobcd,String reqcd,String adminCk) throws SQLException, Exception {
		Connection        conn        = null;
		PreparedStatement pstmt       = null;
		ResultSet         rs          = null;
		StringBuffer      strQuery    = new StringBuffer();
		UserInfo		  userInfo	  = new UserInfo();
		CreateXml         ecmmtb      = new CreateXml();
		ArrayList<HashMap<String, String>>  rsval = new ArrayList<HashMap<String, String>>();
		HashMap<String, String>			  rst		  = null;
		String[]          pathDepth   = null;
		String            strDir      = null;
		int               upSeq       = 0; 
		int               maxSeq      = 0;
		boolean           findSw      = false;
		int               parmCnt     = 0;
		ArrayList<Document> list = new ArrayList<Document>();
		Object[] returnObjectArray = null;
		ConnectionContext connectionContext = new ConnectionResource();
		
		try {
			conn = connectionContext.getConnection();
			Boolean AdminYn= false;
			
			//if (adminCk.toUpperCase().equals("Y") || adminCk.equals("1")){
				//AdminYn = userInfo.isAdmin(UserID);
				AdminYn = userInfo.isAdmin_conn(UserID,conn);
			//}
			userInfo = null;
	
			strQuery.append("select /*+ leading(a) use_nl(a c) use_nl(c d) */ '/' || b.cm_codename || a.cm_dirpath cm_dirpath,a.cm_dsncd, c.cr_rsrccd \n");      
			strQuery.append("  from cmm0020 b,cmm0070 a,                                       \n");
			if (!AdminYn){//관리자가 아닐경우
				strQuery.append("   (select cr_syscd,cr_dsncd,cr_rsrccd,cr_itemid from cmr0020 \n"); 
			    strQuery.append("        where cr_syscd=?) c,                                  \n");
				strQuery.append("      (select j.cr_itemid from cmm0044 k,CMR0023 j            \n"); 
			    strQuery.append("        where k.cm_syscd=? and k.cm_userid=?                  \n"); 
			    strQuery.append("         and k.cm_closedt is null                             \n"); 
			    strQuery.append("         and k.cm_syscd=j.cr_syscd                            \n");
			    strQuery.append("         and k.cm_jobcd=j.cr_jobcd) d,                        \n");                                     
			} else {
				strQuery.append("   (select distinct cr_syscd,cr_dsncd,cr_rsrccd from cmr0020  \n"); 
			    strQuery.append("        where cr_syscd=?) c,                                  \n");
			}
			strQuery.append("      (select cm_syscd,cm_rsrccd from cmm0036                     \n");                                      
			strQuery.append("        where cm_syscd=? and substr(cm_info,2,1)='1'              \n");                                     
			strQuery.append("          and substr(cm_info,26,1)='0' and cm_closedt is null     \n");                                     
			strQuery.append("        minus                                                     \n");                                      
			strQuery.append("       select cm_syscd,cm_samersrc from cmm0037                   \n");                                      
			strQuery.append("        where cm_syscd=?) e                                       \n");  
			strQuery.append(" where a.cm_syscd=?                                               \n");
			strQuery.append("   and a.cm_syscd=c.cr_syscd and a.cm_dsncd=c.cr_dsncd            \n");
			if (!AdminYn){//관리자가 아닐경우
			   strQuery.append("and c.cr_itemid=d.cr_itemid                                    \n");               
			}
			strQuery.append("   and b.cm_macode='JAWON' and b.cm_micode=c.cr_rsrccd            \n"); 
			strQuery.append("   and c.cr_syscd=e.cm_syscd and c.cr_rsrccd=e.cm_rsrccd          \n");
			strQuery.append(" group by b.cm_micode,b.cm_codename,a.cm_dirpath,a.cm_dsncd,c.cr_rsrccd      \n");                                      
			strQuery.append(" order by b.cm_micode,b.cm_codename,a.cm_dirpath,a.cm_dsncd,c.cr_rsrccd       \n"); 
			
			pstmt = conn.prepareStatement(strQuery.toString());
			//pstmt = new LoggableStatement(conn,strQuery.toString());
			pstmt.setString(++parmCnt, syscd);
			if (!AdminYn){//관리자가 아닐경우
				pstmt.setString(++parmCnt, syscd);
			    pstmt.setString(++parmCnt, UserID);
			}
			pstmt.setString(++parmCnt, syscd);
			pstmt.setString(++parmCnt, syscd); 
			pstmt.setString(++parmCnt, syscd); 
			//ecamsLogger.error(((LoggableStatement)pstmt).getQueryString());  
            rs = pstmt.executeQuery();
            
            ecmmtb.init_Xml("ID","cm_seqno","cm_dirpath","cm_upseq","cm_fullpath","cm_dsncd","cr_rsrccd","isBranch");
            rsval.clear();
			while (rs.next()){
				pathDepth = rs.getString("cm_dirpath").substring(1).split("/");
				
				strDir = "/";
				upSeq = 0;
				findSw = false;
				for (int i = 0;pathDepth.length > i;i++) {
					if (strDir.length() > 1 ) {
						strDir = strDir + "/";
					}
					strDir = strDir + pathDepth[i];
					//ecamsLogger.debug("strDir====>" + strDir);
					findSw = false;
					if (rsval.size() > 0) {
						for (int j = 0;rsval.size() > j;j++) {
							if (rsval.get(j).get("cm_fullpath").equals(strDir)) {
								upSeq = Integer.parseInt(rsval.get(j).get("cm_seqno"));
								findSw = true;
							}
						}
					} else {
						findSw = false;
					}
					if (!findSw) {
						maxSeq = maxSeq + 1;
                        
						//ecamsLogger.debug("dirpath,full,upseq,seq====>" + pathDepth[i] + "  , " + strDir  + "  , " + Integer.toString(upSeq)  + "  , " + Integer.toString(maxSeq));
						rst = new HashMap<String,String>();
						rst.put("cm_dirpath",pathDepth[i]);
						rst.put("cm_fullpath",strDir);
						rst.put("cm_upseq",Integer.toString(upSeq));
						rst.put("cm_seqno",Integer.toString(maxSeq));
						if (i == (pathDepth.length - 1)) {
						   rst.put("cm_dsncd",rs.getString("cm_dsncd"));
						}
						rst.put("cr_rsrccd",rs.getString("cr_rsrccd"));
						rsval.add(maxSeq - 1, rst); 
						rst = null;
						upSeq = maxSeq;
					}
				}
			}//end of while-loop statement
			rs.close();
			pstmt.close();
			conn.close();
			
			rs = null;
			pstmt = null;
			conn = null;

			//ecamsLogger.debug("rsval.size====>"+ Integer.toString(rsval.size()));
			if (rsval.size() > 0) {
				for (int i = 0;rsval.size() > i;i++) {
					ecmmtb.addXML(rsval.get(i).get("cm_seqno"),rsval.get(i).get("cm_seqno"),
							rsval.get(i).get("cm_dirpath"),rsval.get(i).get("cm_upseq"),
							rsval.get(i).get("cm_fullpath"),rsval.get(i).get("cm_dsncd"),
							rsval.get(i).get("cr_rsrccd"),"true",rsval.get(i).get("cm_upseq"));
				}
			}
			
			
			list.add(ecmmtb.getDocument());
			
			returnObjectArray = list.toArray();
			
			list = null;
			ecmmtb = null;
			//ecamsLogger.error(ecmmtb.xml_toStr());
			
			return returnObjectArray;			
	
		} catch (SQLException sqlexception) {
			sqlexception.printStackTrace();
			ecamsLogger.error("## SystemPath.getDirPath() SQLException START ##");
			ecamsLogger.error("## Error DESC : ", sqlexception);	
			ecamsLogger.error("## SystemPath.getDirPath() SQLException END ##");			
			throw sqlexception;
		} catch (Exception exception) {
			exception.printStackTrace();
			ecamsLogger.error("## SystemPath.getDirPath() Exception START ##");				
			ecamsLogger.error("## Error DESC : ", exception);	
			ecamsLogger.error("## SystemPath.getDirPath() Exception END ##");				
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
					ecamsLogger.error("## SystemPath.getDirPath() connection release exception ##");
					ex3.printStackTrace();
				}
			}
		}
	}//end of getDirPath() method statement
	
	
	public Object[] getDirPath2(String UserID,String syscd,String SvrCd,String SeqNo) throws SQLException, Exception {
		Connection        conn        = null;
		PreparedStatement pstmt       = null;
		ResultSet         rs          = null;
		StringBuffer      strQuery    = new StringBuffer();
		UserInfo		  userInfo	  = new UserInfo();
		CreateXml         ecmmtb      = new CreateXml();
		ArrayList<HashMap<String, String>>  rsval = new ArrayList<HashMap<String, String>>();
		HashMap<String, String>			  rst		  = null;
		String[]          pathDepth   = null;
		String            strDir      = null;
		int               upSeq       = 0; 
		int               maxSeq      = 0;
		int               parmCnt     = 0;
		boolean           findSw      = false;
		ArrayList<Document> list = new ArrayList<Document>();
		Object[] returnObjectArray = null;
		ConnectionContext connectionContext = new ConnectionResource();
		
		try {
			conn = connectionContext.getConnection();
			Boolean AdminYn= false;
			
			//AdminYn = userInfo.isAdmin(UserID);
			AdminYn = userInfo.isAdmin_conn(UserID,conn);
			userInfo = null;
	
			strQuery.append("select a.cm_dirpath cm_dirpath,a.cm_dsncd                         \n");      
			strQuery.append("  from cmm0070 a,cmr0020 c                                        \n");
			strQuery.append(" where a.cm_syscd=?                                               \n");
			strQuery.append("   and a.cm_syscd=c.cr_syscd and a.cm_dsncd=c.cr_dsncd            \n");                                  
			if (!AdminYn){//관리자가 아닐경우
			   strQuery.append("   and c.cr_jobcd in (select cm_jobcd from cmm0044             \n"); 
			   strQuery.append("                       where cm_syscd=? and cm_userid=?        \n"); 
			   strQuery.append("                         and cm_closedt is null)               \n");               
			}
			if (SvrCd != null && !"".equals(SvrCd)) {
				strQuery.append("and c.cr_rsrccd in (select cm_rsrccd from cmm0038             \n");
				strQuery.append("                     where cm_syscd=? and cm_svrcd=?          \n");
				strQuery.append("                       and cm_seqno=?)                        \n");
			}
			strQuery.append(" group by a.cm_dirpath,a.cm_dsncd                                 \n");                                      
			strQuery.append(" order by a.cm_dirpath,a.cm_dsncd                                 \n"); 
			
			//pstmt = new LoggableStatement(conn,strQuery.toString());
			pstmt = conn.prepareStatement(strQuery.toString());
			
			pstmt.setString(++parmCnt, syscd);
			if (!AdminYn){//관리자가 아닐경우
				pstmt.setString(++parmCnt, syscd);
			    pstmt.setString(++parmCnt, UserID);
			}
			if (SvrCd != null && !"".equals(SvrCd)) { 
				pstmt.setString(++parmCnt, syscd);
				pstmt.setString(++parmCnt, SvrCd);
				pstmt.setInt(++parmCnt, Integer.parseInt(SeqNo));
			}
			//ecamsLogger.error(((LoggableStatement)pstmt).getQueryString()); 
	        rs = pstmt.executeQuery();
	        
	        ecmmtb.init_Xml("ID","cm_seqno","cm_dirpath","cm_upseq","cm_fullpath","cm_dsncd","isBranch");
	        rsval.clear();
			while (rs.next()){
				pathDepth = rs.getString("cm_dirpath").substring(1).split("/");
				
				strDir = "/";
				upSeq = 0;
				findSw = false;
				for (int i = 0;pathDepth.length > i;i++) {
					if (strDir.length() > 1 ) {
						strDir = strDir + "/";
					}
					strDir = strDir + pathDepth[i];
					//ecamsLogger.debug("strDir====>" + strDir);
					findSw = false;
					if (rsval.size() > 0) {
						for (int j = 0;rsval.size() > j;j++) {
							if (rsval.get(j).get("cm_fullpath").equals(strDir)) {
								upSeq = Integer.parseInt(rsval.get(j).get("cm_seqno"));
								findSw = true;
							}
						}
					} else {
						findSw = false;
					}
					if (!findSw) {
						maxSeq = maxSeq + 1;
	                    
						//ecamsLogger.debug("dirpath,full,upseq,seq====>" + pathDepth[i] + "  , " + strDir  + "  , " + Integer.toString(upSeq)  + "  , " + Integer.toString(maxSeq));
						rst = new HashMap<String,String>();
						rst.put("cm_dirpath",pathDepth[i]);
						rst.put("cm_fullpath",strDir);
						rst.put("cm_upseq",Integer.toString(upSeq));
						rst.put("cm_seqno",Integer.toString(maxSeq));
						if (i == (pathDepth.length - 1)) {
						   rst.put("cm_dsncd",rs.getString("cm_dsncd"));
						}
						rsval.add(maxSeq - 1, rst); 
						rst = null;
						upSeq = maxSeq;
					}
				}
			}//end of while-loop statement
			rs.close();
			pstmt.close();
			conn.close();
			
			rs = null;
			pstmt = null;
			conn = null;
			
			//ecamsLogger.debug("rsval.size====>"+ Integer.toString(rsval.size()));
			if (rsval.size() > 0) {
				for (int i = 0;rsval.size() > i;i++) {
					ecmmtb.addXML(rsval.get(i).get("cm_seqno"),rsval.get(i).get("cm_seqno"),
							rsval.get(i).get("cm_dirpath"),rsval.get(i).get("cm_upseq"),
							rsval.get(i).get("cm_fullpath"),rsval.get(i).get("cm_dsncd"),
							"true",rsval.get(i).get("cm_upseq"));
				}
			}
			
			
			list.add(ecmmtb.getDocument());
			
			returnObjectArray = list.toArray();
			
			list = null;
			ecmmtb = null;
			//ecamsLogger.error(ecmmtb.xml_toStr());
			
			return returnObjectArray;			
	
		} catch (SQLException sqlexception) {
			sqlexception.printStackTrace();
			ecamsLogger.error("## SystemPath.getDirPath2() SQLException START ##");
			ecamsLogger.error("## Error DESC : ", sqlexception);	
			ecamsLogger.error("## SystemPath.getDirPath2() SQLException END ##");			
			throw sqlexception;
		} catch (Exception exception) {
			exception.printStackTrace();
			ecamsLogger.error("## SystemPath.getDirPath2() Exception START ##");				
			ecamsLogger.error("## Error DESC : ", exception);	
			ecamsLogger.error("## SystemPath.getDirPath2() Exception END ##");				
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
					ecamsLogger.error("## SystemPath.getDirPath2() connection release exception ##");
					ex3.printStackTrace();
				}
			}
		}
	}//end of getDirPath2() method statement
	
	
	public Object[] getDirPath3(String UserId,String SysCd,String RsrcCd,String Info,String seqNo) throws SQLException, Exception {
		Connection        conn        = null;
		PreparedStatement pstmt       = null;
		ResultSet         rs          = null;
		CreateXml         ecmmtb      = new CreateXml();
		StringBuffer      strQuery    = new StringBuffer();
		UserInfo		  userInfo	  = new UserInfo();
		//String            strDsn      = null; 
		String            strHome     = "";
		String[]          pathDepth   = null;
		String            strDir      = "";
		int               upSeq       = 0; 
		int               maxSeq      = 100;
		boolean           findSw      = false;
		int               parmCnt     = 0;
		ArrayList<Document> list = new ArrayList<Document>();		
		ArrayList<HashMap<String, String>>  rsval = new ArrayList<HashMap<String, String>>();
		HashMap<String, String>			  rst		  = null;
		
		Object[] returnObjectArray = null;
		ConnectionContext connectionContext = new ConnectionResource();
		
		Boolean			  AdminYn= false;
		
		try {
			conn = connectionContext.getConnection();
			
			AdminYn = userInfo.isAdmin_conn(UserId,conn);
			userInfo = null;

			if (SysCd.equals("00089") && RsrcCd.equals("86")) {
				strQuery.append("select substr(b.cm_dirpath,1,instr(b.cm_dirpath,'/',-1,1)-1) volpath,e.cm_volpath   \n");
				strQuery.append("  from cmm0070 b,cmm0031 a,cmm0030 c,cmr0020 d,cmm0038 e ");
				if (!AdminYn) {
					strQuery.append(",cmm0044 f \n");
				}
				strQuery.append(" where c.cm_syscd=?                                         \n");          
				strQuery.append("   and c.cm_syscd=a.cm_syscd and c.cm_dirbase=a.cm_svrcd    \n");
				strQuery.append("   and a.cm_syscd=b.cm_syscd and a.cm_closedt is null       \n");
				strQuery.append("   and a.cm_syscd=e.cm_syscd and a.cm_svrcd=e.cm_svrcd      \n");
				strQuery.append("   and a.cm_seqno=e.cm_seqno and e.cm_rsrccd=?              \n");
				if (!AdminYn){
					strQuery.append("and f.cm_userid=?           \n");
					strQuery.append("and f.cm_syscd=?           \n");
					strQuery.append("and f.cm_syscd=d.cr_syscd           \n");
					strQuery.append("and f.cm_jobcd=d.cr_jobcd           \n");
				}
				strQuery.append("   and d.cr_rsrccd=? and b.cm_dirpath is not null           \n");
				strQuery.append("   and b.cm_syscd=d.cr_syscd and b.cm_dsncd=d.cr_dsncd      \n");
				strQuery.append("   and b.cm_dirpath like '%/src/serviceModule/%'            \n");
				strQuery.append(" group by substr(b.cm_dirpath,1,instr(b.cm_dirpath,'/',-1,1)-1),e.cm_volpath \n");       
				strQuery.append(" order by substr(b.cm_dirpath,1,instr(b.cm_dirpath,'/',-1,1)-1) \n"); 
			} else {
				strQuery.append("select b.cm_dirpath volpath,b.cm_dsncd,e.cm_volpath         \n");
				strQuery.append("  from cmm0070 b,cmm0031 a,cmm0030 c,cmr0020 d,cmm0038 e ");
				if (!AdminYn) {
					strQuery.append(",cmm0044 f \n");
				}
				strQuery.append(" where c.cm_syscd=?                                         \n");          
				strQuery.append("   and c.cm_syscd=a.cm_syscd and c.cm_dirbase=a.cm_svrcd    \n");
				strQuery.append("   and a.cm_syscd=b.cm_syscd and a.cm_closedt is null       \n");
				strQuery.append("   and a.cm_syscd=e.cm_syscd and a.cm_svrcd=e.cm_svrcd      \n");
				strQuery.append("   and a.cm_seqno=e.cm_seqno and e.cm_rsrccd=?              \n");
				if (!AdminYn){
					strQuery.append("and f.cm_userid=?           \n");
					strQuery.append("and f.cm_syscd=?           \n");
					strQuery.append("and f.cm_syscd=d.cr_syscd           \n");
					strQuery.append("and f.cm_jobcd=d.cr_jobcd           \n");
				}
				strQuery.append("   and d.cr_rsrccd=?                                        \n");
				strQuery.append("   and b.cm_syscd=d.cr_syscd and b.cm_dsncd=d.cr_dsncd      \n");
				strQuery.append(" group by b.cm_dirpath,b.cm_dsncd,e.cm_volpath              \n");       
				strQuery.append(" order by b.cm_dirpath                                      \n");     
			}
			
			pstmt = conn.prepareStatement(strQuery.toString());
			//pstmt = new LoggableStatement(conn,strQuery.toString());
			pstmt.setString(++parmCnt, SysCd);
			pstmt.setString(++parmCnt, RsrcCd);
			if (!AdminYn) {
				pstmt.setString(++parmCnt, UserId);
				pstmt.setString(++parmCnt, SysCd);
			}
			pstmt.setString(++parmCnt, RsrcCd);
			
			ecmmtb.init_Xml("ID","cm_seqno","cm_dirpath","cm_upseq","cm_fullpath","cm_dsncd","cr_rsrccd","cm_info","isBranch");
            //ecamsLogger.error(((LoggableStatement)pstmt).getQueryString()); 
	        rs = pstmt.executeQuery();
	        rsval.clear();
	        while (rs.next()){
	        	strDir = "";
				if (rs.getString("cm_volpath") != null) {
					strDir = rs.getString("cm_volpath");
					if (strDir.length()>0){
						if (strDir.substring(strDir.length()-1).equals("/")){
							strDir = strDir.substring(0,strDir.length()-1);
						}
					}
					strHome = strDir;
					if (rs.getString("volpath").length() >= strHome.length()){
						if (!rs.getString("volpath").substring(0,strHome.length()).equals(strHome)) {
							strHome = "";
						}
					}
				} else strHome = "";
				if(rs.getString("volpath").length() >= strDir.length()){
					if (rs.getString("volpath").substring(0,strDir.length()).equals(strDir)) {
						strDir = rs.getString("volpath").replace(strDir, "");
					} else strDir = "";
				}
				if (strDir.length() > 0){
					pathDepth = strDir.substring(1).split("/");
				
					strDir = "/";
					upSeq = Integer.parseInt(seqNo);
					findSw = false;
					
					for (int i = 0;pathDepth.length > i;i++) {
						if (strDir.length() > 1 ) {
							strDir = strDir + "/";
						}
						strDir = strDir + pathDepth[i];
						//ecamsLogger.error("strDir====>" + strDir);
						findSw = false;
						if (rsval.size() > 0) {
							for (int j = 0;rsval.size() > j;j++) {
								//ecamsLogger.error("cm_fullpath====>" + rsval.get(j).get("cm_fullpath"));
								if (rsval.get(j).get("cm_fullpath").equals(strHome+strDir)) {
									upSeq = Integer.parseInt(rsval.get(j).get("cm_seqno"));
									
									//rsval.set(j, element)
									//rst.put("branch","true");
									findSw = true;
								}
							}
						} else {
							findSw = false;
						}
						if (findSw == false) {
							maxSeq = maxSeq + 1;
	                        
							//ecamsLogger.error("dirpath,full,upseq,seq====>" + pathDepth[i] + "  , " + strDir  + "  , " + Integer.toString(upSeq)  + "  , " + Integer.toString(maxSeq));
							rst = new HashMap<String,String>();
							rst.put("cm_dirpath",pathDepth[i]);							
							rst.put("cm_fullpath",strHome+strDir);
							rst.put("cm_upseq",Integer.toString(upSeq));
							rst.put("cm_seqno",Integer.toString(maxSeq));
							rst.put("cr_rsrccd",RsrcCd);
							if (Info.substring(26,27).equals("0") && rs.getString("volpath").equals(strHome+strDir)){
								rst.put("cr_dsncd", rs.getString("cm_dsncd")); 
							}else{
								rst.put("cr_dsncd", "");
							}
							if (rs.getString("volpath").equals(strHome+strDir)){
								rst.put("branch", "false");
							}
							rsval.add(maxSeq - 101, rst); 
							rst = null;
							upSeq = maxSeq;
						}
					}
				}
				
			}//end of while-loop statement
			rs.close();
			pstmt.close();
			conn.close();
			
			rs = null;
			pstmt = null;
			conn = null;
			
			if (rsval.size() > 0) {
				//ecamsLogger.debug(rsval.toString());
				
			    String strBran = "";
				for (int i = 0;rsval.size() > i;i++) {
					strBran = "false";
					for (int j=0;rsval.size()>j;j++) {
						if (i != j) {
							if (rsval.get(i).get("cm_seqno").equals(rsval.get(j).get("cm_upseq"))) {
								strBran = "true";
								break;
							}
						}
					}
					ecmmtb.addXML(rsval.get(i).get("cm_seqno"),rsval.get(i).get("cm_seqno"),
							rsval.get(i).get("cm_dirpath"),rsval.get(i).get("cm_upseq"),
							rsval.get(i).get("cm_fullpath"),rsval.get(i).get("cr_dsncd"),
							rsval.get(i).get("cr_rsrccd"),Info,strBran,rsval.get(i).get("cm_upseq"));
				}
			}
			
			
			list.add(ecmmtb.getDocument());
			
			returnObjectArray = list.toArray();
			
			list = null;
			ecmmtb = null;
			//ecamsLogger.error(ecmmtb.xml_toStr());
			
			return returnObjectArray;				

		} catch (SQLException sqlexception) {
			sqlexception.printStackTrace();
			ecamsLogger.error("## SystemPath.getDirPath3() SQLException START ##");
			ecamsLogger.error("## Error DESC : ", sqlexception);	
			ecamsLogger.error("## SystemPath.getDirPath3() SQLException END ##");			
			throw sqlexception;
		} catch (Exception exception) {
			exception.printStackTrace();
			ecamsLogger.error("## SystemPath.getDirPath3() Exception START ##");				
			ecamsLogger.error("## Error DESC : ", exception);	
			ecamsLogger.error("## SystemPath.getDirPath3() Exception END ##");				
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
					ecamsLogger.error("## SystemPath.getDirPath3() connection release exception ##");
					ex3.printStackTrace();
				}
			}
		}
	}//end of getDirPath3() method statement
	
	public List<HashMap<String, String>> getDirPath3_ztree(String UserId,String SysCd,String RsrcCd,String Info,String seqNo) throws SQLException, Exception {
		Connection        conn        = null;
		PreparedStatement pstmt       = null;
		ResultSet         rs          = null;
		StringBuffer      strQuery    = new StringBuffer();
		UserInfo		  userInfo	  = new UserInfo();
		//String            strDsn      = null;
		String            strHome     = "";
		String[]          pathDepth   = null;
		String            strDir      = "";
		int               upSeq       = 0;
		int               maxSeq      = 100;
		boolean           findSw      = false;
		int               parmCnt     = 0;
		ArrayList<HashMap<String, String>>  rsval = new ArrayList<HashMap<String, String>>();
		HashMap<String, String>			  rst		  = null;

		Object[] returnObjectArray = null;
		ConnectionContext connectionContext = new ConnectionResource();

		Boolean			  AdminYn= false;

		try {
			conn = connectionContext.getConnection();
			
			AdminYn = userInfo.isAdmin_conn(UserId,conn);
			userInfo = null;

			if (SysCd.equals("00089") && RsrcCd.equals("86")) {
				strQuery.append("select substr(b.cm_dirpath,1,instr(b.cm_dirpath,'/',-1,1)-1) volpath,e.cm_volpath   \n");
				strQuery.append("  from cmm0070 b,cmm0031 a,cmm0030 c,cmr0020 d,cmm0038 e ");
				if (!AdminYn) {
					strQuery.append(",cmm0044 f \n");
				}
				strQuery.append(" where c.cm_syscd=?                                         \n");          
				strQuery.append("   and c.cm_syscd=a.cm_syscd and c.cm_dirbase=a.cm_svrcd    \n");
				strQuery.append("   and a.cm_syscd=b.cm_syscd and a.cm_closedt is null       \n");
				strQuery.append("   and a.cm_syscd=e.cm_syscd and a.cm_svrcd=e.cm_svrcd      \n");
				strQuery.append("   and a.cm_seqno=e.cm_seqno and e.cm_rsrccd=?              \n");
				if (!AdminYn){
					strQuery.append("and f.cm_userid=?           \n");
					strQuery.append("and f.cm_syscd=?           \n");
					strQuery.append("and f.cm_syscd=d.cr_syscd           \n");
					strQuery.append("and f.cm_jobcd=d.cr_jobcd           \n");
				}
				strQuery.append("   and d.cr_rsrccd=? and b.cm_dirpath is not null           \n");
				strQuery.append("   and b.cm_syscd=d.cr_syscd and b.cm_dsncd=d.cr_dsncd      \n");
				strQuery.append("   and b.cm_dirpath like '%/src/serviceModule/%'            \n");
				strQuery.append(" group by substr(b.cm_dirpath,1,instr(b.cm_dirpath,'/',-1,1)-1),e.cm_volpath \n");       
				strQuery.append(" order by substr(b.cm_dirpath,1,instr(b.cm_dirpath,'/',-1,1)-1) \n"); 
			} else {
				strQuery.append("select b.cm_dirpath volpath,b.cm_dsncd,e.cm_volpath         \n");
				strQuery.append("  from cmm0070 b,cmm0031 a,cmm0030 c,cmr0020 d,cmm0038 e ");
				if (!AdminYn) {
					strQuery.append(",cmm0044 f \n");
				}
				strQuery.append(" where c.cm_syscd=?                                         \n");          
				strQuery.append("   and c.cm_syscd=a.cm_syscd and c.cm_dirbase=a.cm_svrcd    \n");
				strQuery.append("   and a.cm_syscd=b.cm_syscd and a.cm_closedt is null       \n");
				strQuery.append("   and a.cm_syscd=e.cm_syscd and a.cm_svrcd=e.cm_svrcd      \n");
				strQuery.append("   and a.cm_seqno=e.cm_seqno and e.cm_rsrccd=?              \n");
				if (!AdminYn){
					strQuery.append("and f.cm_userid=?           \n");
					strQuery.append("and f.cm_syscd=?           \n");
					strQuery.append("and f.cm_syscd=d.cr_syscd           \n");
					strQuery.append("and f.cm_jobcd=d.cr_jobcd           \n");
				}
				strQuery.append("   and d.cr_rsrccd=?                                        \n");
				strQuery.append("   and b.cm_syscd=d.cr_syscd and b.cm_dsncd=d.cr_dsncd      \n");
				strQuery.append(" group by b.cm_dirpath,b.cm_dsncd,e.cm_volpath              \n");       
				strQuery.append(" order by b.cm_dirpath                                      \n");     
			}
			
			pstmt = conn.prepareStatement(strQuery.toString());
			//pstmt = new LoggableStatement(conn,strQuery.toString());
			pstmt.setString(++parmCnt, SysCd);
			pstmt.setString(++parmCnt, RsrcCd);
			if (!AdminYn) {
				pstmt.setString(++parmCnt, UserId);
				pstmt.setString(++parmCnt, SysCd);
			}
			pstmt.setString(++parmCnt, RsrcCd);

            //ecamsLogger.error(((LoggableStatement)pstmt).getQueryString());
	        rs = pstmt.executeQuery();
	        rsval.clear();
	        while (rs.next()){
	        	strDir = "";
				if (rs.getString("cm_volpath") != null) {
					strDir = rs.getString("cm_volpath");
					if (strDir.length()>0){
						if (strDir.substring(strDir.length()-1).equals("/")){
							strDir = strDir.substring(0,strDir.length()-1);
						}
					}
					strHome = strDir;
					if (rs.getString("volpath").length() >= strHome.length()){
						if (!rs.getString("volpath").substring(0,strHome.length()).equals(strHome)) {
							strHome = "";
						}
					}
				} else strHome = "";
				if(rs.getString("volpath").length() >= strDir.length()){
					if (rs.getString("volpath").substring(0,strDir.length()).equals(strDir)) {
						strDir = rs.getString("volpath").replace(strDir, "");
					} else strDir = "";
				}
				if (strDir.length() > 0){
					pathDepth = strDir.substring(1).split("/");

					strDir = "/";
					upSeq = Integer.parseInt(seqNo);
					findSw = false;

					for (int i = 0;pathDepth.length > i;i++) {
						if (strDir.length() > 1 ) {
							strDir = strDir + "/";
						}
						strDir = strDir + pathDepth[i];
						//ecamsLogger.error("strDir====>" + strDir);
						findSw = false;
						if (rsval.size() > 0) {
							for (int j = 0;rsval.size() > j;j++) {
								//ecamsLogger.error("cm_fullpath====>" + rsval.get(j).get("cm_fullpath"));
								if (rsval.get(j).get("cm_fullpath").equals(strHome+strDir)) {
									upSeq = Integer.parseInt(rsval.get(j).get("cm_seqno"));

									//rsval.set(j, element)
									//rst.put("branch","true");
									findSw = true;
								}
							}
						} else {
							findSw = false;
						}
						if (!findSw) {
							maxSeq = maxSeq + 1;

							//ecamsLogger.error("dirpath,full,upseq,seq====>" + pathDepth[i] + "  , " + strDir  + "  , " + Integer.toString(upSeq)  + "  , " + Integer.toString(maxSeq));
							rst = new HashMap<String,String>();
							rst.put("cm_dirpath",pathDepth[i]);
							rst.put("cm_fullpath",strHome+strDir);
							rst.put("cm_upseq",Integer.toString(upSeq));
							rst.put("cm_seqno",Integer.toString(maxSeq));
							rst.put("cr_rsrccd",RsrcCd);
							if (Info.substring(26,27).equals("0") && rs.getString("volpath").equals(strHome+strDir)){
								rst.put("cr_dsncd", rs.getString("cm_dsncd"));
							}else{
								rst.put("cr_dsncd", "");
							}
							rst.put("isParent", "true");
							if (rs.getString("volpath").equals(strHome+strDir)){
								rst.put("branch", "false");
								rst.put("isParent", "false");
							}
							rst.put("pId",Integer.toString(upSeq));	//cm_upseq
							rst.put("id",Integer.toString(maxSeq));	//cm_seqno
							rst.put("name",pathDepth[i]);
							rst.put("imagesrc", "/img/folderDefaultClosed.gif");
							rsval.add(maxSeq - 101, rst);
							rst = null;
							upSeq = maxSeq;
						}
					}
				}
			}//end of while-loop statement
			rs.close();
			pstmt.close();
			conn.close();

			rs = null;
			pstmt = null;
			conn = null;
			
			return rsval;

		} catch (SQLException sqlexception) {
			sqlexception.getStackTrace();
			ecamsLogger.error("## SystemPath.getDirPath3() SQLException START ##");
			ecamsLogger.error("## Error DESC : ", sqlexception);
			ecamsLogger.error("## SystemPath.getDirPath3() SQLException END ##");
			throw sqlexception;
		} catch (Exception exception) {
			exception.getStackTrace();
			ecamsLogger.error("## SystemPath.getDirPath3() Exception START ##");
			ecamsLogger.error("## Error DESC : ", exception);
			ecamsLogger.error("## SystemPath.getDirPath3() Exception END ##");
			throw exception;
		}finally {
			if (strQuery != null) 	strQuery = null;
			if (returnObjectArray != null)	returnObjectArray = null;
			if (pathDepth != null)	pathDepth = null;
			if (userInfo != null)	userInfo = null;
			if (rsval != null)	rsval = null;
			if (rst != null)	rst = null;

			if (rs != null)     try{rs.close();}catch (Exception ex){ex.getStackTrace();}
			if (pstmt != null)  try{pstmt.close();}catch (Exception ex2){ex2.getStackTrace();}
			if (conn != null){
				ConnectionResource.release(conn);
			}
			if(connectionContext != null) {
				connectionContext.release();
				connectionContext = null;
			}
		}
	}//end of getDirPath3() method statement
	
	public Object[] getDirPath_Job(String UserId,String ReqCd,String SysCd,String JobCd,String HomePath,String seqNo) throws SQLException, Exception {
		Connection        conn        = null;
		PreparedStatement pstmt       = null;
		ResultSet         rs          = null;
		CreateXml         ecmmtb      = new CreateXml();
		StringBuffer      strQuery    = new StringBuffer();
		UserInfo		  userInfo	  = new UserInfo();
		//String            strDsn      = null;
		String            strHome     = "";
		String[]          pathDepth   = null;
		String            strDir      = "";
		int               upSeq       = 0;
		int               maxSeq      = 100;
		boolean           findSw      = false;
		int               parmCnt     = 0;
		ArrayList<Document> list = new ArrayList<Document>();
		ArrayList<HashMap<String, String>>  rsval = new ArrayList<HashMap<String, String>>();
		HashMap<String, String>			  rst		  = null;

		Object[] returnObjectArray = null;
		ConnectionContext connectionContext = new ConnectionResource();

		Boolean			  AdminYn= false;

		try {
			conn = connectionContext.getConnection();

			AdminYn = userInfo.isAdmin_conn(UserId,conn);
			userInfo = null;
			strDir = HomePath;
			conn = connectionContext.getConnection();
			strQuery.append("select b.cm_dirpath,b.cm_dsncd                              \n");
			strQuery.append("  from cmm0070 b,cmr0020 d                                  \n");
			strQuery.append(" where d.cr_syscd=? and d.cr_jobcd=?                        \n");
			strQuery.append("   and d.cr_status<>'9'                                     \n");
			if (ReqCd.equals("93")) {
				strQuery.append("   and d.cr_devsta='9' and d.cr_teststa<>'9'            \n");
			} else if (ReqCd.equals("94")) {
				strQuery.append("   and d.cr_devsta='9' and d.cr_realsta<>'9'            \n");
			} else {
				strQuery.append("   and d.cr_devsta<>'9'                                 \n");
				strQuery.append("   and d.cr_teststa<>'9'                                \n");
				strQuery.append("   and d.cr_realsta<>'9'                                \n");
			}			
			strQuery.append("   and b.cm_syscd=d.cr_syscd and b.cm_dsncd=d.cr_dsncd      \n");
			strQuery.append(" group by b.cm_dirpath,b.cm_dsncd                           \n");
			strQuery.append(" order by b.cm_dirpath                                      \n");

			pstmt = conn.prepareStatement(strQuery.toString());
			//pstmt = new LoggableStatement(conn,strQuery.toString());
			pstmt.setString(++parmCnt, SysCd);
			pstmt.setString(++parmCnt, JobCd);

			ecmmtb.init_Xml("ID","cm_seqno","cm_dirpath","cm_upseq","cm_fullpath","cm_dsncd","cr_jobcd","isBranch");
            //ecamsLogger.error(((LoggableStatement)pstmt).getQueryString());
	        rs = pstmt.executeQuery();
	        rsval.clear();
	        while (rs.next()){
				if(rs.getString("cm_dirpath").length() >= HomePath.length()){
					if (rs.getString("cm_dirpath").substring(0,HomePath.length()).equals(HomePath)) {
						strDir = rs.getString("cm_dirpath").replace(HomePath, "");
					} else strDir = "";
				}
				if (strDir.length() > 0){
					pathDepth = strDir.substring(1).split("/");

					strDir = "/";
					upSeq = Integer.parseInt(seqNo);
					findSw = false;

					for (int i = 0;pathDepth.length > i;i++) {
						if (strDir.length() > 1 ) {
							strDir = strDir + "/";
						}
						strDir = strDir + pathDepth[i];
						//ecamsLogger.error("strDir====>" + strDir);
						findSw = false;
						if (rsval.size() > 0) {
							for (int j = 0;rsval.size() > j;j++) {
								//ecamsLogger.error("cm_fullpath====>" + rsval.get(j).get("cm_fullpath"));
								if (rsval.get(j).get("cm_fullpath").equals(HomePath+strDir)) {
									upSeq = Integer.parseInt(rsval.get(j).get("cm_seqno"));

									//rsval.set(j, element)
									//rst.put("branch","true");
									findSw = true;
								}
							}
						} else {
							findSw = false;
						}
						if (!findSw) {
							maxSeq = maxSeq + 1;

							//ecamsLogger.error("dirpath,full,upseq,seq====>" + pathDepth[i] + "  , " + strDir  + "  , " + Integer.toString(upSeq)  + "  , " + Integer.toString(maxSeq));
							rst = new HashMap<String,String>();
							rst.put("cm_dirpath",pathDepth[i]);
							rst.put("cm_fullpath",HomePath+strDir);
							rst.put("cm_upseq",Integer.toString(upSeq));
							rst.put("cm_seqno",Integer.toString(maxSeq));
							rst.put("cm_jobcd",JobCd);
							if (rs.getString("cm_dirpath").equals(HomePath+strDir)){
								rst.put("cr_dsncd", rs.getString("cm_dsncd"));
							}else{
								rst.put("cr_dsncd", "");
							}
							if (rs.getString("cm_dirpath").equals(HomePath+strDir)){
								rst.put("branch", "false");
							}
							rsval.add(maxSeq - 101, rst);
							rst = null;
							upSeq = maxSeq;
						}
					}
				}
			}//end of while-loop statement
			rs.close();
			pstmt.close();
			conn.close();

			rs = null;
			pstmt = null;
			conn = null;

			if (rsval.size() > 0) {
				//ecamsLogger.debug(rsval.toString());

			    String strBran = "";
				for (int i = 0;rsval.size() > i;i++) {
					strBran = "false";
					for (int j=0;rsval.size()>j;j++) {
						if (i != j) {
							if (rsval.get(i).get("cm_seqno").equals(rsval.get(j).get("cm_upseq"))) {
								strBran = "true";
								break;
							}
						}
					}
					ecmmtb.addXML(rsval.get(i).get("cm_seqno"),rsval.get(i).get("cm_seqno"),
							rsval.get(i).get("cm_dirpath"),rsval.get(i).get("cm_upseq"),
							rsval.get(i).get("cm_fullpath"),rsval.get(i).get("cr_dsncd"),
							rsval.get(i).get("cr_rsrccd"),strBran,rsval.get(i).get("cm_upseq"));
				}
			}


			list.add(ecmmtb.getDocument());

			returnObjectArray = list.toArray();

			list = null;
			ecmmtb = null;
			//ecamsLogger.error(ecmmtb.xml_toStr());

			return returnObjectArray;

		} catch (SQLException sqlexception) {
			sqlexception.printStackTrace();
			ecamsLogger.error("## SystemPath.getDirPath3() SQLException START ##");
			ecamsLogger.error("## Error DESC : ", sqlexception);
			ecamsLogger.error("## SystemPath.getDirPath3() SQLException END ##");
			throw sqlexception;
		} catch (Exception exception) {
			exception.printStackTrace();
			ecamsLogger.error("## SystemPath.getDirPath3() Exception START ##");
			ecamsLogger.error("## Error DESC : ", exception);
			ecamsLogger.error("## SystemPath.getDirPath3() Exception END ##");
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
					ecamsLogger.error("## SystemPath.getDirPath3() connection release exception ##");
					ex3.printStackTrace();
				}
			}
		}
	}//end of getDirPath3() method statement


//	public Object[] getRsrcPath(String UserID,String SysCd,String SecuYn,String SinCd,String ReqCd) throws SQLException, Exception {
//		Connection        conn        = null;
//		PreparedStatement pstmt       = null;
//		ResultSet         rs          = null;
//		StringBuffer      strQuery    = new StringBuffer();
//		Object[] returnObjectArray	  = null;
//		CreateXml         ecmmtb      = new CreateXml();	
//		int               maxSeq      = 0;	
//		int               parmCnt     = 0;
//		
//		ArrayList<Document> list = new ArrayList<Document>();
//		
//	    		
//		ConnectionContext connectionContext = new ConnectionResource();		
//		
//		try {
//			conn = connectionContext.getConnection();
//			ecmmtb.init_Xml("ID","cm_seqno","cm_dirpath","cm_upseq","cm_fullpath","cm_dsncd","cr_rsrccd","cm_info","isBranch");
//			
//			strQuery.append("select a.cm_rsrccd,a.cm_info,d.cm_codename,c.cm_volpath    \n");
//			strQuery.append("  from cmm0036 a,cmm0038 c,cmm0031 b,                      \n");
//			strQuery.append("       cmm0020 d                                           \n");
//			strQuery.append(" where a.cm_syscd=? and a.cm_closedt is null               \n");
//			strQuery.append("   and a.cm_rsrccd not in (select cm_samersrc from cmm0037 \n");
//			strQuery.append("                            where cm_syscd=?)              \n");
//			if (ReqCd.equals("09")) 
//				strQuery.append("and substr(a.cm_info,15,1)='1'                         \n");
//			else {
//				strQuery.append("and substr(a.cm_info,2,1)='1'                          \n");
//				strQuery.append("and substr(a.cm_info,26,1)='0'                         \n");
//			}
//			//strQuery.append("   and a.cm_rsrccd=b.cr_rsrccd                             \n");
//			/*
//			if (!adminYn) {
//			    strQuery.append("   and b.cr_itemid=c.cr_itemid                         \n");
//			}
//			*/
//			strQuery.append("   and d.cm_macode='JAWON' and d.cm_micode=a.cm_rsrccd     \n");
//			strQuery.append("   and a.cm_syscd=b.cm_syscd and b.cm_closedt is null      \n");
//			strQuery.append("   and b.cm_svrcd='01' and b.cm_syscd=c.cm_syscd           \n");
//			strQuery.append("   and b.cm_svrcd=c.cm_svrcd and b.cm_seqno=c.cm_seqno     \n");
//			strQuery.append("   and a.cm_rsrccd=c.cm_rsrccd                             \n");
//			strQuery.append(" group by a.cm_rsrccd,a.cm_info,d.cm_codename,c.cm_volpath \n");			
//			strQuery.append(" order by a.cm_rsrccd                                      \n");
//			
//			pstmt = conn.prepareStatement(strQuery.toString());
//			//pstmt = new LoggableStatement(conn,strQuery.toString());
//            /*if (!adminYn) {
//            	pstmt.setString(++parmCnt,UserID);
//            	pstmt.setString(++parmCnt, SysCd);
//            }
//            */
//            pstmt.setString(++parmCnt, SysCd);
//            pstmt.setString(++parmCnt, SysCd);
////            if (SinCd.equals("03"))
////            	if (ReqCd.equals("05")) pstmt.setString(++parmCnt, SysCd);	
//            //ecamsLogger.error(((LoggableStatement)pstmt).getQueryString());            
//            rs = pstmt.executeQuery();
//            //ecamsLogger.error("+++++++==query end+++++++++++");
//            
//            while (rs.next()){
// 	        	++maxSeq; 	        	
//				ecmmtb.addXML(Integer.toString(maxSeq),Integer.toString(maxSeq),
//						rs.getString("cm_codename"),"0",rs.getString("cm_volpath"),"",
//						rs.getString("cm_rsrccd"),rs.getString("cm_info"),"true","");
//			}//end of while-loop statement
//			rs.close();
//			pstmt.close();
//			conn.close();
//			//ecamsLogger.error("+++++++==xml end+++++++++++");
//			rs = null;
//			pstmt = null;
//			conn = null;
//			
//			list.add(ecmmtb.getDocument());
//			//ecamsLogger.error("+++++++==return+++++++++++");
//			returnObjectArray = list.toArray();
//			
//			list = null;
//			ecmmtb = null;
//			//ecamsLogger.error(ecmmtb.xml_toStr());
//			
//			return returnObjectArray;			
//			
//			
//		} catch (SQLException sqlexception) {
//			sqlexception.printStackTrace();
//			ecamsLogger.error("## SystemPath.getRsrcPath() SQLException START ##");
//			ecamsLogger.error("## Error DESC : ", sqlexception);	
//			ecamsLogger.error("## SystemPath.getRsrcPath() SQLException END ##");			
//			throw sqlexception;
//		} catch (Exception exception) {
//			exception.printStackTrace();
//			ecamsLogger.error("## SystemPath.getRsrcPath() Exception START ##");				
//			ecamsLogger.error("## Error DESC : ", exception);	
//			ecamsLogger.error("## SystemPath.getRsrcPath() Exception END ##");				
//			throw exception;
//		}finally{
//			if (strQuery != null) 	strQuery = null;
//			if (returnObjectArray != null)	returnObjectArray = null;
//			if (rs != null)     try{rs.close();}catch (Exception ex){ex.printStackTrace();}
//			if (pstmt != null)  try{pstmt.close();}catch (Exception ex2){ex2.printStackTrace();}
//			if (conn != null){
//				try{
//					ConnectionResource.release(conn);
//				}catch(Exception ex3){
//					ecamsLogger.error("## SystemPath.getRsrcPath() connection release exception ##");
//					ex3.printStackTrace();
//				}
//			}
//		}
//	}//end of getRsrcPath() method statement
	
	public ArrayList<HashMap<String, String>> getRsrcPath_ztree(String UserID,String SysCd,String SecuYn,String SinCd,String ReqCd) throws SQLException, Exception {
		Connection        conn        = null;
		PreparedStatement pstmt       = null;
		ResultSet         rs          = null;
		StringBuffer      strQuery    = new StringBuffer();
		int               parmCnt     = 0;
		int				  maxSeq	  = 0;

		ArrayList<HashMap<String, String>> rsrcPathList 	= new ArrayList<HashMap<String, String>>();
		HashMap<String, String> rsrcPathMap 		= null;
		
		ConnectionContext connectionContext = new ConnectionResource();		

		try {
			conn = connectionContext.getConnection();
			
			strQuery.append("select a.cm_rsrccd,a.cm_info,d.cm_codename,c.cm_volpath    \n");
			strQuery.append("  from cmm0036 a,cmm0038 c,cmm0031 b,                      \n");
			strQuery.append("       cmm0020 d                                           \n");
			strQuery.append(" where a.cm_syscd=? and a.cm_closedt is null               \n");
			strQuery.append("   and a.cm_rsrccd not in (select cm_samersrc from cmm0037 \n");
			strQuery.append("                            where cm_syscd=?)              \n");
			if (ReqCd.equals("09")) 
				strQuery.append("and substr(a.cm_info,15,1)='1'                         \n");
			else {
				strQuery.append("and substr(a.cm_info,2,1)='1'                          \n");
				strQuery.append("and substr(a.cm_info,26,1)='0'                         \n");
			}
			//strQuery.append("   and a.cm_rsrccd=b.cr_rsrccd                             \n");
			/*
			if (!adminYn) {
			    strQuery.append("   and b.cr_itemid=c.cr_itemid                         \n");
			}
			*/
			strQuery.append("   and d.cm_macode='JAWON' and d.cm_micode=a.cm_rsrccd     \n");
			strQuery.append("   and a.cm_syscd=b.cm_syscd and b.cm_closedt is null      \n");
			strQuery.append("   and b.cm_svrcd='01' and b.cm_syscd=c.cm_syscd           \n");
			strQuery.append("   and b.cm_svrcd=c.cm_svrcd and b.cm_seqno=c.cm_seqno     \n");
			strQuery.append("   and a.cm_rsrccd=c.cm_rsrccd                             \n");
			strQuery.append(" group by a.cm_rsrccd,a.cm_info,d.cm_codename,c.cm_volpath \n");			
			strQuery.append(" order by a.cm_rsrccd                                      \n");
			
			pstmt = conn.prepareStatement(strQuery.toString());
			//pstmt = new LoggableStatement(conn,strQuery.toString());

            pstmt.setString(++parmCnt, SysCd);
            pstmt.setString(++parmCnt, SysCd);
//            if (SinCd.equals("03"))
//            	if (ReqCd.equals("05")) pstmt.setString(++parmCnt, SysCd);
            //ecamsLogger.error(((LoggableStatement)pstmt).getQueryString());
            rs = pstmt.executeQuery();

            while (rs.next()){
            	++maxSeq;
            	
            	rsrcPathMap = new HashMap<String, String>();
            	rsrcPathMap.put("cm_seqno"		, Integer.toString(maxSeq));
            	String cm_dirpath = rs.getString("cm_codename");
            	rsrcPathMap.put("cm_dirpath"	, cm_dirpath);
            	rsrcPathMap.put("cm_upseq"		, "0");
            	rsrcPathMap.put("cm_fullpath"	, rs.getString("cm_volpath"));
            	rsrcPathMap.put("cm_dsncd"		, "");
            	rsrcPathMap.put("cr_rsrccd"		, rs.getString("cm_rsrccd"));
            	rsrcPathMap.put("cm_info"		, rs.getString("cm_info"));
            	rsrcPathMap.put("isParent"		, "true");
            	rsrcPathMap.put("id"			, Integer.toString(maxSeq));
            	rsrcPathMap.put("pid"			, "0");
            	rsrcPathMap.put("name"			, cm_dirpath);
            	//rsrcPathMap.put("imagesrc"		, "/img/folderDefaultClosed.gif");
            	rsrcPathList.add(rsrcPathMap);
            	rsrcPathMap = null;
			}//end of while-loop statement
            
			rs.close();
			pstmt.close();
			conn.close();
			
			rs = null;
			pstmt = null;
			conn = null;
			
			return rsrcPathList;			
			
			
		} catch (SQLException sqlexception) {
			sqlexception.getStackTrace();
			ecamsLogger.error("## SystemPath.getRsrcPath() SQLException START ##");
			ecamsLogger.error("## Error DESC : ", sqlexception);
			ecamsLogger.error("## SystemPath.getRsrcPath() SQLException END ##");
			throw sqlexception;
		} catch (Exception exception) {
			exception.getStackTrace();
			ecamsLogger.error("## SystemPath.getRsrcPath() Exception START ##");
			ecamsLogger.error("## Error DESC : ", exception);
			ecamsLogger.error("## SystemPath.getRsrcPath() Exception END ##");
			throw exception;
		}finally {
			if (strQuery != null) 	strQuery = null;

			if (rsrcPathList != null) 	rsrcPathList = null;
			if (rsrcPathMap != null) 	rsrcPathMap = null;
			if (rs != null)     try{rs.close();}catch (Exception ex){ex.getStackTrace();}
			if (pstmt != null)  try{pstmt.close();}catch (Exception ex2){ex2.getStackTrace();}
			if (conn != null){
				ConnectionResource.release(conn);
			}
			if(connectionContext != null) {
				connectionContext.release();
				connectionContext = null;
			}
		}
	}//end of getRsrcPath() method statement
	
	public Object[] getTopSysPath(String UserID,String progName,boolean upSw) throws SQLException, Exception {
		Connection        conn        = null;
		PreparedStatement pstmt       = null;
		ResultSet         rs          = null;
		PreparedStatement pstmt2      = null;
		ResultSet         rs2         = null;
		StringBuffer      strQuery    = new StringBuffer();
		Object[] returnObjectArray	  = null;
		CreateXml         ecmmtb      = new CreateXml();	
		int               maxSeq      = 0;	
		int               sysSeq      = 0;		
		int               svrSeq      = 0;		
		int               rsrcSeq      = 0;	
		boolean           findSw      = false;
		String            strSysName  = null;
		String            strSvrName  = null;
		String            svSvrName   = null;
		ArrayList<HashMap<String, String>>  rsval = new ArrayList<HashMap<String, String>>();
		HashMap<String, String>			  rst		  = null;
		
		ArrayList<Document> list = new ArrayList<Document>();
		
	    		
		ConnectionContext connectionContext = new ConnectionResource();		
		
		try {
			
			conn = connectionContext.getConnection();
			
			ecmmtb.init_Xml("ID","cm_seqno","cm_dirpath","cm_upseq","cm_fullpath","cm_syscd","cr_itemid","cr_rsrccd","cr_lstver","gbncd","isBranch");
			
			int cnt = 0;
			strQuery.setLength(0);
			strQuery.append("select f.cm_sysmsg,e.cm_svrname,d.cm_seqno,       \n");
			strQuery.append("       a.cm_rsrccd,a.cm_info,d.cm_codename,       \n");
			strQuery.append("       c.cm_volpath,f.cm_syscd                    \n");
			strQuery.append("  from cmm0036 a,cmm0030 f,cmm0044 h,             \n");
			strQuery.append("       (select cr_syscd,cr_rsrccd from cmr0020    \n");
			strQuery.append("         group by cr_syscd,cr_rsrccd) b           \n");
			strQuery.append("       ,cmm0020 d,cmm0038 c,cmm0031 e             \n");
			if (progName != null && !"".equals(progName)) { 
    			strQuery.append(",(select y.cm_syscd,y.cm_dirpath              \n");
    			strQuery.append("    from cmm0070 y,cmr0020 x                  \n");
    			if (upSw == false) {
    				strQuery.append("where upper(x.cr_rsrcname) like upper(?)  \n");
        		} else {
        			strQuery.append("where x.cr_rsrcname like ?                \n");
        		}
    			strQuery.append("  and x.cr_syscd=y.cm_syscd                   \n");	
    			strQuery.append("  and x.cr_dsncd=y.cm_dsncd                   \n");	
    			strQuery.append("group by y.cm_syscd,y.cm_dirpath) g           \n");	        		
        		
        	}
			strQuery.append(" where f.cm_closedt is null                       \n");
			strQuery.append("   and h.cm_userid=? and h.cm_closedt is null     \n");
			strQuery.append("   and h.cm_syscd=f.cm_syscd                      \n");
			strQuery.append("   and f.cm_syscd=c.cm_syscd                      \n");
			strQuery.append("   and a.cm_rsrccd not in                         \n");
			strQuery.append("      (select cm_samersrc from cmm0037            \n");
			strQuery.append("        where cm_syscd=a.cm_syscd)                \n");
			strQuery.append("   and a.cm_syscd=b.cr_syscd                      \n");
			strQuery.append("   and a.cm_rsrccd=b.cr_rsrccd                    \n");
			strQuery.append("   and d.cm_macode='JAWON'                        \n");
			strQuery.append("   and d.cm_micode=a.cm_rsrccd                    \n");
			strQuery.append("   and a.cm_syscd=c.cm_syscd                      \n");
			strQuery.append("   and a.cm_rsrccd=c.cm_rsrccd and c.cm_svrcd=f.cm_dirbase\n");
			strQuery.append("   and c.cm_syscd=e.cm_syscd                      \n");
			strQuery.append("   and c.cm_svrcd=e.cm_svrcd                      \n");
			strQuery.append("   and c.cm_seqno=e.cm_seqno                      \n");
			if (progName != null && !"".equals(progName)) { 
				strQuery.append("and c.cm_syscd=g.cm_syscd                     \n");
				strQuery.append("and g.cm_dirpath like c.cm_volpath || '%'     \n");
			}
			strQuery.append(" group by f.cm_syscd,e.cm_svrname,d.cm_seqno,a.cm_rsrccd,a.cm_info,d.cm_codename,c.cm_volpath,f.cm_sysmsg \n");
			strQuery.append(" order by f.cm_syscd,e.cm_svrname,d.cm_seqno,a.cm_rsrccd     \n");
			
            pstmt = conn.prepareStatement(strQuery.toString());	
            //pstmt = new LoggableStatement(conn,strQuery.toString());
            if (progName != null && !"".equals(progName)) { 
            	pstmt.setString(++cnt, "%"+progName+"%");
        	}
            pstmt.setString(++cnt, UserID);
            //ecamsLogger.error(((LoggableStatement)pstmt).getQueryString());            
            rs = pstmt.executeQuery();
            while (rs.next()){
 	        	
 	        	//ecmmtb.init_Xml("ID","cm_seqno","cm_dirpath","cm_upseq","cm_fullpath","cm_dsncd","cr_rsrccd","cm_info","isBranch");
 	        	findSw = false;
 	        	if (strSysName == null) findSw = true;
 	        	else if (!strSysName.equals(rs.getString("cm_sysmsg"))) findSw = true;
 	        	
 	        	if (findSw == true) {
 	        		++maxSeq;
 	        		strSysName = rs.getString("cm_sysmsg");
 	        		sysSeq = maxSeq;
 	        		strSvrName = null;
 	        		//ecmmtb.addXML(Integer.toString(maxSeq),Integer.toString(maxSeq),
 	        		//		strSysName,"0","",rs.getString("cm_syscd"),"",
					//		"","","SYS","true",Integer.toString(sysSeq));
 	        		rst = new HashMap<String,String>();
					rst.put("cm_dirpath",strSysName);							
					rst.put("cm_fullpath","");
					rst.put("cm_upseq",Integer.toString(maxSeq));
					rst.put("cm_seqno",Integer.toString(maxSeq));
					rst.put("cm_syscd",rs.getString("cm_syscd"));
					rst.put("cr_rsrccd","");
					rst.put("gbncd", "SYS");
					rst.put("cr_itemid", ""); 
					rst.put("cr_lstver", "");
					rst.put("bransw", "true");
					rsval.add(rst); 
 	        	}
 	        	
 	        	findSw = false;
 	        	if (strSvrName == null) findSw = true;
 	        	else if (!strSvrName.equals(rs.getString("cm_svrname"))) findSw = true;
 	        	
 	        	if (findSw == true) {
 	        		++maxSeq;
 	        		strSvrName = rs.getString("cm_svrname");
 	        		svSvrName = strSvrName.replace("_개발", "");
 	        		svSvrName = svSvrName.replace("개발", "");
 	        		svrSeq = maxSeq;
 	        		//ecmmtb.addXML(Integer.toString(maxSeq),Integer.toString(maxSeq),
 	        		//strSvrName.replace("개발", ""),Integer.toString(sysSeq),rs.getString("cm_syscd"),"","",
 	        		//"","","SVR","true",Integer.toString(sysSeq));
 	        		rst = new HashMap<String,String>();
 	        		rst.put("cm_dirpath",svSvrName);
					rst.put("cm_fullpath","");
					rst.put("cm_upseq",Integer.toString(sysSeq));
					rst.put("cm_seqno",Integer.toString(maxSeq));
					rst.put("cm_syscd",rs.getString("cm_syscd"));
					rst.put("cr_rsrccd","");
					rst.put("gbncd", "SVR");
					rst.put("cr_itemid", ""); 
					rst.put("cr_lstver", "");
					rst.put("bransw", "true");
					rsval.add(rst); 
 	        	}
 	        	++maxSeq;
 	        	rsrcSeq = maxSeq;
				//ecmmtb.addXML(Integer.toString(maxSeq),Integer.toString(maxSeq),
				//		rs.getString("cm_codename"),"0",rs.getString("cm_volpath"),rs.getString("cm_syscd"),"",
				//		rs.getString("cm_rsrccd"),rs.getString("cm_info"),"RSC","true",Integer.toString(svrSeq));
				rst = new HashMap<String,String>();
				rst.put("cm_dirpath",rs.getString("cm_codename"));							
				rst.put("cm_fullpath",rs.getString("cm_volpath"));
				rst.put("cm_upseq",Integer.toString(svrSeq));
				rst.put("cm_seqno",Integer.toString(maxSeq));
				rst.put("cm_syscd",rs.getString("cm_syscd"));
				rst.put("cr_rsrccd",rs.getString("cm_rsrccd"));
				rst.put("gbncd", "RSC");
				rst.put("cr_itemid", "");
				rst.put("cr_lstver", "");
				rst.put("bransw", "true");
				rsval.add(rst); 
				
				cnt = 0;
 	        	strQuery.setLength(0);
 	        	strQuery.append("select a.cr_rsrcname,a.cr_itemid,a.cr_lstver  \n");
 	        	strQuery.append("  from cmm0044 d,cmm0070 b,cmr0020 a,cmm0036 c\n");
 	        	strQuery.append(" where b.cm_syscd=? and b.cm_dirpath=?        \n");
 	        	strQuery.append("   and b.cm_syscd=a.cr_syscd                  \n");
 	        	strQuery.append("   and b.cm_dsncd=a.cr_dsncd                  \n");
 	        	strQuery.append("   and a.cr_lstver>0 and a.cr_status<>'9'     \n");
 	        	strQuery.append("   and d.cm_userid=? and d.cm_closedt is null \n");
 	        	strQuery.append("   and d.cm_syscd=?                           \n");
 	        	strQuery.append("   and d.cm_jobcd=a.cr_jobcd                  \n");
 	        	if (progName != null && !"".equals(progName)) { 	        		
 	        		if (upSw == false) {
 	        			strQuery.append("and upper(a.cr_rsrcname) like upper(?)\n");
 	        		} else {
 	        			strQuery.append("and a.cr_rsrcname like ?              \n");
 	        		}
 	        	}
 	        	strQuery.append("   and (a.cr_rsrccd=? or                      \n");
 				strQuery.append("        a.cr_rsrccd in                        \n");
 				strQuery.append("       (select cm_samersrc from cmm0037       \n");
 				strQuery.append("        where cm_syscd=? and cm_rsrccd=?))    \n");
 				strQuery.append("   and a.cr_syscd=c.cm_syscd                  \n");
 				strQuery.append("   and a.cr_rsrccd=c.cm_rsrccd                \n");
 				strQuery.append("   and substr(c.cm_info,10,1)='0'             \n");
 				strQuery.append("   and substr(c.cm_info,12,1)='1'             \n");
 	        	strQuery.append("order by a.cr_rsrcname                        \n");
 	        	
 	        	pstmt2 = conn.prepareStatement(strQuery.toString());
 	        	//pstmt2 = new LoggableStatement(conn,strQuery.toString());
 	        	pstmt2.setString(++cnt, rs.getString("cm_syscd"));
 	        	pstmt2.setString(++cnt, rs.getString("cm_volpath"));
 	        	pstmt2.setString(++cnt, UserID);
 	        	pstmt2.setString(++cnt, rs.getString("cm_syscd"));
 	        	if (progName != null && !"".equals(progName)) { 
 	        		pstmt2.setString(++cnt, "%"+progName+"%");
 	        	}
 	        	pstmt2.setString(++cnt, rs.getString("cm_rsrccd"));
 	        	pstmt2.setString(++cnt, rs.getString("cm_syscd"));
 	        	pstmt2.setString(++cnt, rs.getString("cm_rsrccd"));
 	        	//ecamsLogger.error(((LoggableStatement)pstmt2).getQueryString()); 
 	        	rs2 = pstmt2.executeQuery();
 	        	while (rs2.next()) {
 	        		++maxSeq;
 	        		rst = new HashMap<String,String>();
					rst.put("cm_dirpath",rs2.getString("cr_rsrcname"));							
					rst.put("cm_fullpath",rs.getString("cm_volpath"));
					rst.put("cm_upseq",Integer.toString(rsrcSeq));
					rst.put("cm_seqno",Integer.toString(maxSeq));
					rst.put("cm_syscd",rs.getString("cm_syscd"));
					rst.put("cr_rsrccd",rs.getString("cm_rsrccd"));
					rst.put("cr_lstver",rs2.getString("cr_lstver"));
					rst.put("gbncd", "ITM");
					rst.put("cr_itemid", rs2.getString("cr_itemid")); 
					rst.put("bransw", "false");
					rsval.add(rst); 
 					//ecmmtb.addXML(Integer.toString(maxSeq),Integer.toString(maxSeq),
 					//		rs2.getString("cr_rsrcname"),"0",rs.getString("cm_volpath"),rs.getString("cm_syscd"),rs2.getString("cr_itemid"),
 					//		rs.getString("cm_rsrccd"),rs2.getString("cr_lstver"),"ITM","true",Integer.toString(rsrcSeq));
 	        	}
 	        	rs2.close();
 	        	pstmt2.close();
			}//end of while-loop statement
			rs.close();
			pstmt.close();
			conn.close();
			
			rs = null;
			pstmt = null;
			conn = null;

			if (rsval.size() > 0) {
				//ecamsLogger.debug(rsval.toString());
				String strLstVer = "";
				
				for (int i = 0;rsval.size() > i;i++) {
					if (!rsval.get(i).get("gbncd").equals("ITM")) strLstVer = Integer.toString(maxSeq);
					else strLstVer = rsval.get(i).get("cr_lstver");
					ecmmtb.addXML(rsval.get(i).get("cm_seqno"),rsval.get(i).get("cm_seqno"),
							rsval.get(i).get("cm_dirpath"),rsval.get(i).get("cm_upseq"),
							rsval.get(i).get("cm_fullpath"),rsval.get(i).get("cm_syscd"),
							rsval.get(i).get("cr_itemid"),rsval.get(i).get("cr_rsrccd"),
							strLstVer,rsval.get(i).get("gbncd"),
							rsval.get(i).get("bransw"),rsval.get(i).get("cm_upseq"));
				}
			}
			
			
			list.add(ecmmtb.getDocument());
			
			returnObjectArray = list.toArray();
			
			list = null;
			ecmmtb = null;
			rsval = null;
			//ecamsLogger.error(ecmmtb.xml_toStr());
			
			return returnObjectArray;			
			
			
		} catch (SQLException sqlexception) {
			sqlexception.printStackTrace();
			ecamsLogger.error("## SystemPath.getTopSysPath() SQLException START ##");
			ecamsLogger.error("## Error DESC : ", sqlexception);	
			ecamsLogger.error("## SystemPath.getTopSysPath() SQLException END ##");			
			throw sqlexception;
		} catch (Exception exception) {
			exception.printStackTrace();
			ecamsLogger.error("## SystemPath.getTopSysPath() Exception START ##");				
			ecamsLogger.error("## Error DESC : ", exception);	
			ecamsLogger.error("## SystemPath.getTopSysPath() Exception END ##");				
			throw exception;
		}finally{
			if (strQuery != null) 	strQuery = null;
			if (returnObjectArray != null)	returnObjectArray = null;
			if (rs != null)     try{rs.close();}catch (Exception ex){ex.printStackTrace();}
			if (pstmt != null)  try{pstmt.close();}catch (Exception ex2){ex2.printStackTrace();}
			if (rs2 != null)    try{rs2.close();}catch (Exception ex){ex.printStackTrace();}
			if (pstmt2 != null) try{pstmt2.close();}catch (Exception ex2){ex2.printStackTrace();}
			if (conn != null){
				try{
					ConnectionResource.release(conn);
				}catch(Exception ex3){
					ecamsLogger.error("## SystemPath.getTopSysPath() connection release exception ##");
					ex3.printStackTrace();
				}
			}
		}
	}//end of getTopSysPath() method statement
	
	
	public Object[] getDynamicPath(String UserID,String syscd,String rsrcCD,String rootPath,int pathlevel,String adminCk) throws SQLException, Exception {
		Connection        conn        = null;
		PreparedStatement pstmt       = null;
		ResultSet         rs          = null;
		StringBuffer      strQuery    = new StringBuffer();
		UserInfo		  userInfo	  = new UserInfo();
		CreateXml         ecmmtb      = new CreateXml();
		String[]          pathDepth   = null;
		int               parmCnt     = 0;
		//HashMap<String, String>			  rst		  = null;
		//Object[]		  rtObj		  = null;
		//ArrayList<HashMap<String, String>>		  rtList	  = new ArrayList<HashMap<String, String>>();
		
		ArrayList<Document> list = new ArrayList<Document>();
		Object[] returnObjectArray = null;
		ConnectionContext connectionContext = new ConnectionResource();
		
		try {
			conn = connectionContext.getConnection();
			Boolean AdminYn= false;
			
			//if (adminCk.toUpperCase().equals("Y") || adminCk.equals("1")){
				//AdminYn = userInfo.isAdmin(UserID);
				AdminYn = userInfo.isAdmin_conn(UserID,conn);
			//}
			userInfo = null;
			
			strQuery.setLength(0);
			strQuery.append("select /*+ leading(a) use_nl(a c) use_nl(c d) */ \n");
			strQuery.append("dirpath, rsrccd, max(maxlevel) as maxlevel, nowlevel from ( \n");
			strQuery.append("select distinct '/' || b.cm_codename || decode(?,length(a.cm_dirpath) \n");
			strQuery.append("- length(replace(a.cm_dirpath,'/',''))+1,a.cm_dirpath, \n");
			strQuery.append("substr(a.cm_dirpath,1,instr(a.cm_dirpath,'/',1,? )-1)) as dirpath, c.cr_rsrccd as rsrccd, \n");
			strQuery.append("length(a.cm_dirpath) - length(replace(a.cm_dirpath,'/',''))+1 as maxlevel, ? as nowlevel \n");
			strQuery.append("  from cmm0020 b,cmm0070 a,                                       \n");
			if (!AdminYn){
				strQuery.append("   (select cr_syscd,cr_dsncd,cr_rsrccd,cr_itemid from cmr0020 \n"); 
			    strQuery.append("        where cr_syscd=?) c,                                  \n");
				strQuery.append("      (select j.cr_itemid from cmm0044 k,CMR0023 j            \n"); 
			    strQuery.append("        where k.cm_syscd=? and k.cm_userid=?                  \n"); 
			    strQuery.append("         and k.cm_closedt is null                             \n"); 
			    strQuery.append("         and k.cm_syscd=j.cr_syscd                            \n");
			    strQuery.append("         and k.cm_jobcd=j.cr_jobcd) d,                        \n");                                     
			} else {
				strQuery.append("   (select distinct cr_syscd,cr_dsncd,cr_rsrccd from cmr0020  \n"); 
			    strQuery.append("        where cr_syscd=?) c,                                  \n");
			}
			strQuery.append("      (select cm_syscd,cm_rsrccd from cmm0036                     \n");                                      
			strQuery.append("        where cm_syscd=? and substr(cm_info,2,1)='1'              \n");                                     
			strQuery.append("          and substr(cm_info,26,1)='0' and cm_closedt is null     \n");                                     
			strQuery.append("        minus                                                     \n");                                      
			strQuery.append("       select cm_syscd,cm_samersrc from cmm0037                   \n");                                      
			strQuery.append("        where cm_syscd=?) e                                       \n");  
			strQuery.append(" where a.cm_syscd=?                                               \n");
			strQuery.append("   and a.cm_syscd=c.cr_syscd and a.cm_dsncd=c.cr_dsncd            \n");
			if (!AdminYn){
			   strQuery.append("and c.cr_itemid=d.cr_itemid                                    \n");               
			}
			strQuery.append("   and b.cm_macode='JAWON' and b.cm_micode=c.cr_rsrccd            \n"); 
			strQuery.append("   and c.cr_syscd=e.cm_syscd and c.cr_rsrccd=e.cm_rsrccd          \n");
			if (!rsrcCD.equals("")){
				strQuery.append("   and c.cr_rsrccd = ? \n");
			}
			strQuery.append(" and length(a.cm_dirpath) - length(replace(a.cm_dirpath,'/',''))+1 >= ? \n");
			strQuery.append(" ) \n");
			strQuery.append(" where dirpath like ?											\n");
			strQuery.append("group by dirpath, rsrccd, nowlevel \n");
			strQuery.append(" order by rsrccd,length(dirpath) \n");
			//pstmt = new LoggableStatement(conn,strQuery.toString());
			pstmt = conn.prepareStatement(strQuery.toString());
			pstmt.setInt(++parmCnt, pathlevel);
			pstmt.setInt(++parmCnt, pathlevel);
			pstmt.setInt(++parmCnt, pathlevel);
			
			if (!AdminYn){
				pstmt.setString(++parmCnt, syscd);
			} else{
				pstmt.setString(++parmCnt, syscd);
				pstmt.setString(++parmCnt, syscd);
			    pstmt.setString(++parmCnt, UserID);
			}
			pstmt.setString(++parmCnt, syscd);
			pstmt.setString(++parmCnt, syscd); 
			pstmt.setString(++parmCnt, syscd);
			
			if (!rsrcCD.equals("")){
				pstmt.setString(++parmCnt, rsrcCD);
			}
			
			pstmt.setInt(++parmCnt, pathlevel);
			pstmt.setString(++parmCnt, rootPath+"%");
			
			//ecamsLogger.error(((LoggableStatement)pstmt).getQueryString());  
			//ecamsLogger.error("Start Query");
            rs = pstmt.executeQuery();
            //ecamsLogger.error("End Query");
            
            ecmmtb.init_Xml("ID","cm_dirpath","cm_fullpath","cr_rsrccd","nowlevel","maxlevel","isBranch");
            //rtList.clear();
            
            //ecamsLogger.error("Make Xml Start");
			while (rs.next()){
				pathDepth = rs.getString("dirpath").substring(1).split("/");
				
				//ecamsLogger.error(rs.getRow());
				
				ecmmtb.addXML(rs.getString("rsrccd")+ String.format("%04d",rs.getInt("nowlevel"))+pathDepth[pathDepth.length-1],
						pathDepth[pathDepth.length-1],rs.getString("dirpath"),
						rs.getString("rsrccd"),rs.getString("nowlevel"),rs.getString("maxlevel"),
						"true","");
				
				//ecamsLogger.error(rs.getRow());
				/*
				rst = new HashMap<String,String>();
				rst.put("ID",rs.getString("rsrccd")+ String.format("%04d",rs.getInt("nowlevel"))+pathDepth[pathDepth.length-1]);
				rst.put("cm_dirpath",pathDepth[pathDepth.length-1]);
				rst.put("cm_fullpath",rs.getString("dirpath"));
				rst.put("cm_dsncd",rs.getString("dsncd"));
				rst.put("cr_rsrccd",rs.getString("rsrccd"));
				rst.put("nowlevel",rs.getString("nowlevel"));
				rst.put("maxlevel",rs.getString("maxlevel"));
				rst.put("isBranch","true");
				rtList.add(rst);
				rst = null;
				*/
			
			}//end of while-loop statement
			rs.close();
			pstmt.close();
			conn.close();
			
			rs = null;
			pstmt = null;
			conn = null;
			
			list.add(ecmmtb.getDocument());
			
			//ecamsLogger.error("Make Xml End");
			returnObjectArray = list.toArray();
			
			list = null;
			ecmmtb = null;
			//ecamsLogger.error(ecmmtb.xml_toStr());
			
			return returnObjectArray;
	
		} catch (SQLException sqlexception) {
			sqlexception.printStackTrace();
			ecamsLogger.error("## SystemPath.getDynamicPath() SQLException START ##");
			ecamsLogger.error("## Error DESC : ", sqlexception);	
			ecamsLogger.error("## SystemPath.getDynamicPath() SQLException END ##");			
			throw sqlexception;
		} catch (Exception exception) {
			exception.printStackTrace();
			ecamsLogger.error("## SystemPath.getDynamicPath() Exception START ##");				
			ecamsLogger.error("## Error DESC : ", exception);	
			ecamsLogger.error("## SystemPath.getDynamicPath() Exception END ##");				
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
					ecamsLogger.error("## SystemPath.getDynamicPath() connection release exception ##");
					ex3.printStackTrace();
				}
			}
		}
	}//end of getDirPath() method statement
	
	
	public Object[] getDirPath_File(String UserId,String SysCd,String RsrcCd,String seqNo,String maxCnt,String progName,boolean upSw) throws SQLException, Exception {
		Connection        conn        = null;
		PreparedStatement pstmt       = null;
		ResultSet         rs          = null;
		CreateXml         ecmmtb      = new CreateXml();
		StringBuffer      strQuery    = new StringBuffer();
		UserInfo		  userInfo	  = new UserInfo();
		//String            strDsn      = null; 
		String            strHome     = null;
		String[]          pathDepth   = null;
		String            strDir      = null;
		int               upSeq       = 0; 
		int               maxSeq      = Integer.parseInt(maxCnt);
		boolean           findSw      = false;
		int               parmCnt     = 0;
		String            strPath     = null;
		ArrayList<Document> list = new ArrayList<Document>();		
		ArrayList<HashMap<String, String>>  rsval = new ArrayList<HashMap<String, String>>();
		HashMap<String, String>			  rst		  = null;
		
		Object[] returnObjectArray = null;
		ConnectionContext connectionContext = new ConnectionResource();
		
		Boolean			  AdminYn= false;
		
		try {
			conn = connectionContext.getConnection();
			
			AdminYn = userInfo.isAdmin_conn(UserId, conn);
			userInfo = null;
			
			strQuery.setLength(0);
			strQuery.append("select b.cm_dirpath volpath,b.cm_dsncd,c.cm_volpath,        \n");
			strQuery.append("       d.cr_itemid,d.cr_lstver ,d.cr_rsrcname,f.cm_info     \n");
			strQuery.append("  from cmm0070 b,cmm0031 a,cmr0020 d,cmm0038 c,cmm0036 f,cmm0044 e,cmm0030 g \n");
			strQuery.append(" where e.cm_userid=? and e.cm_syscd=?                       \n");
			strQuery.append("   and e.cm_closedt is null and e.cm_syscd=a.cm_syscd       \n"); 
			strQuery.append("   and a.cm_svrcd=g.cm_dirbase and a.cm_closedt is null             \n"); 
			strQuery.append("   and a.cm_syscd=c.cm_syscd and a.cm_svrcd=c.cm_svrcd      \n");
			strQuery.append("   and a.cm_seqno=c.cm_seqno and g.cm_syscd=e.cm_syscd       \n");
        	strQuery.append("   and (c.cm_rsrccd=? or                                    \n");
			strQuery.append("        c.cm_rsrccd in                                      \n");
			strQuery.append("          (select cm_samersrc from cmm0037                  \n");
			strQuery.append("            where cm_syscd=? and cm_rsrccd=?))              \n");
			strQuery.append("   and c.cm_syscd=f.cm_syscd and c.cm_rsrccd=f.cm_rsrccd    \n");
			strQuery.append("   and substr(f.cm_info,12,1)='1'                           \n");
			strQuery.append("   and c.cm_syscd=d.cr_syscd and c.cm_rsrccd=d.cr_rsrccd    \n");
			strQuery.append("   and d.cr_status<>'9'                                     \n");
			strQuery.append("   and d.cr_jobcd=e.cm_jobcd                                \n");
			if (progName != null && !"".equals(progName)) { 	        		
        		if (upSw == false) {
        			strQuery.append("and upper(d.cr_rsrcname) like upper(?)\n");
        		} else {
        			strQuery.append("and d.cr_rsrcname like ?              \n");
        		}
        	}
			if (!AdminYn) strQuery.append("and d.cr_jobcd=e.cm_jobcd                     \n");
			strQuery.append("   and b.cm_syscd=d.cr_syscd and b.cm_dsncd=d.cr_dsncd      \n");     
			strQuery.append(" order by b.cm_dirpath,d.cr_rsrcname                        \n");  
			
			pstmt = conn.prepareStatement(strQuery.toString());
			//pstmt = new LoggableStatement(conn,strQuery.toString());
			pstmt.setString(++parmCnt, UserId);
			pstmt.setString(++parmCnt, SysCd);
			pstmt.setString(++parmCnt, RsrcCd);
			pstmt.setString(++parmCnt, SysCd);
			pstmt.setString(++parmCnt, RsrcCd);
			if (progName != null && !"".equals(progName)) pstmt.setString(++parmCnt, "%"+progName+"%");
			ecmmtb.init_Xml("ID","cm_seqno","cm_dirpath","cm_upseq","cm_fullpath","cm_syscd","cr_itemid","cr_rsrccd","cr_lstver","cm_info","gbncd","isBranch","cm_upseq");
            rsval.clear(); 
            //ecamsLogger.error(((LoggableStatement)pstmt).getQueryString()); 
	        rs = pstmt.executeQuery();
	        rsval.clear();
	        while (rs.next()){
	        	findSw = false;
	        	if (rs.getString("cr_lstver").equals("0") && rs.getString("cm_info").substring(44,45).equals("1")) {
	        		strDir = "";
	        	} else if (rs.getString("cm_info").substring(9,10).equals("1") && !rs.getString("cm_info").substring(45,46).equals("1")) { 
	        		strDir = "";
	        	} else {
		        	if (strPath == null) findSw = true;
		        	else if (!strPath.equals(rs.getString("volpath"))) findSw = true;
		        	//ecamsLogger.error("cr_rsrcname 2====>" + rs.getString("volpath") + "/" + rs.getString("cr_rsrcname"));
		        	if (findSw == true) {
						if (rs.getString("cm_volpath") != null) {
							strDir = rs.getString("cm_volpath");
							if (strDir.length()>0){
								if (strDir.substring(strDir.length()-1).equals("/")){
									strDir = strDir.substring(0,strDir.length()-1);
								}
							}
							strHome = strDir;
							if (rs.getString("volpath").length() >= strHome.length()){
								if (!rs.getString("volpath").substring(0,strHome.length()).equals(strHome)) {
									strHome = "";
								}
							}
						} else strHome = "";
						
						strDir = rs.getString("volpath").replace(strDir, "");
						
						
						if (strDir.length() > 0){
							pathDepth = strDir.substring(1).split("/");
						
							strDir = "/";
							upSeq = Integer.parseInt(seqNo);
							findSw = false;
							
							for (int i = 0;pathDepth.length > i;i++) {
								if (strDir.length() > 1 ) {
									strDir = strDir + "/";
								}
								strDir = strDir + pathDepth[i];
								//ecamsLogger.debug("strDir====>" + strDir);
								findSw = false;
								if (rsval.size() > 0) {
									for (int j = 0;rsval.size() > j;j++) {
										if (rsval.get(j).get("cm_fullpath").equals(strHome+strDir)) {
											upSeq = Integer.parseInt(rsval.get(j).get("cm_seqno"));
											
											//rsval.set(j, element)
											//rst.put("branch","true");
											findSw = true;
											break;
										}
									}
								} else {
									findSw = false;
								}
								if (!findSw) {
									maxSeq = maxSeq + 1;
									//System.out.println("1111"+strHome+strDir+"/"+pathDepth[i]);
									//ecamsLogger.error("dirpath,full,upseq,seq====>" + pathDepth[i] + "  , " + strDir  + "  , " + Integer.toString(upSeq)  + "  , " + maxCnt  + "  , " + Integer.toString(maxSeq));
									rst = new HashMap<String,String>();
									rst.put("cm_dirpath",pathDepth[i]);							
									rst.put("cm_fullpath",strHome+strDir);
									rst.put("cm_upseq",Integer.toString(upSeq));
									rst.put("cm_seqno",Integer.toString(maxSeq));
									rst.put("cr_rsrccd",RsrcCd);
									rst.put("gbncd", "DIR");
									rst.put("bransw", "true");
									rst.put("cr_itemid", rs.getString("cm_dsncd")); 
									if (rs.getString("volpath").equals(strHome+strDir)) rst.put("branch", "false");
									rsval.add(maxSeq - Integer.parseInt(maxCnt) - 1, rst); 
									rst = null;
									upSeq = maxSeq;
								}
							}
						}
		        	}
	        	}
	        	
	        	if (strDir.length() > 0){
	        		//ecamsLogger.error("cr_rsrcname 3====>" + Integer.toString(upSeq)  + "  , " + Integer.toString(maxSeq) + "," + rs.getString("volpath") + "/" + rs.getString("cr_rsrcname"));
	        		maxSeq = maxSeq + 1;
	        		//System.out.println("2222"+rs.getString("volpath")+"/"+rs.getString("cr_rsrcname"));
					//ecamsLogger.debug("dirpath,full,upseq,seq====>" + pathDepth[i] + "  , " + strDir  + "  , " + Integer.toString(upSeq)  + "  , " + Integer.toString(maxSeq));
					rst = new HashMap<String,String>();
					rst.put("cm_dirpath",rs.getString("cr_rsrcname"));							
					rst.put("cm_fullpath",rs.getString("volpath"));
					rst.put("cm_upseq",Integer.toString(upSeq));
					rst.put("cm_seqno",Integer.toString(maxSeq));
					rst.put("cr_rsrccd",RsrcCd);
					rst.put("cr_itemid", rs.getString("cr_itemid")); 
					rst.put("cr_lstver", rs.getString("cr_lstver")); 
					rst.put("cm_info",rs.getString("cm_info"));
					rst.put("gbncd", "ITM");
					rst.put("bransw", "false");
					rsval.add(maxSeq - Integer.parseInt(maxCnt) - 1, rst); 
					rst = null;
					//upSeq = maxSeq;
					//ecmmtb.addXML(Integer.toString(maxSeq),Integer.toString(maxSeq),
					//	rs2.getString("cr_rsrcname"),"0",rs.getString("cm_volpath"),rs2.getString("cr_itemid"),
					//		rs.getString("cm_rsrccd"),rs.getString("cr_lstver"),"true",Integer.toString(rsrcSeq));
	        	}
	        	strPath = rs.getString("volpath");
				
			}//end of while-loop statement
			rs.close();
			pstmt.close();
			conn.close();
			
			rs = null;
			pstmt = null;
			conn = null;
			
			if (rsval.size() > 0) {
				System.out.println(rsval.size());
				//ecamsLogger.debug(rsval.toString());
				
			    //String strBran = "";
				for (int i = 0;rsval.size() > i;i++) {
					ecmmtb.addXML(rsval.get(i).get("cm_seqno"),rsval.get(i).get("cm_seqno"),
							rsval.get(i).get("cm_dirpath"),rsval.get(i).get("cm_upseq"),
							rsval.get(i).get("cm_fullpath"),SysCd,rsval.get(i).get("cr_itemid"),
							rsval.get(i).get("cr_rsrccd"),rsval.get(i).get("cr_lstver"),rsval.get(i).get("cm_info"),
							rsval.get(i).get("gbncd"),rsval.get(i).get("bransw"),rsval.get(i).get("cm_upseq"));
				}
			}//"ID","cm_seqno","cm_dirpath","cm_upseq","cm_fullpath","cm_syscd","cr_itemid","cr_rsrccd","cr_lstver","cm_info","gbncd","isBranch"
			
			
			list.add(ecmmtb.getDocument());
			
			returnObjectArray = list.toArray();
			
			list = null;
			ecmmtb = null;
			rsval = null;
			//ecamsLogger.error(ecmmtb.xml_toStr());
			
			return returnObjectArray;				

		} catch (SQLException sqlexception) {
			sqlexception.printStackTrace();
			ecamsLogger.error("## SystemPath.getDirPath_File() SQLException START ##");
			ecamsLogger.error("## Error DESC : ", sqlexception);	
			ecamsLogger.error("## SystemPath.getDirPath_File() SQLException END ##");			
			throw sqlexception;
		} catch (Exception exception) {
			exception.printStackTrace();
			ecamsLogger.error("## SystemPath.getDirPath_File() Exception START ##");				
			ecamsLogger.error("## Error DESC : ", exception);	
			ecamsLogger.error("## SystemPath.getDirPath_File() Exception END ##");				
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
					ecamsLogger.error("## SystemPath.getDirPath_File() connection release exception ##");
					ex3.printStackTrace();
				}
			}
		}
	}//end of getDirPath_File() method statement
	
	
	public ArrayList<HashMap<String, String>> getUserPath_conn(String SysCd, Connection conn) throws SQLException, Exception {
		PreparedStatement pstmt       = null;
		ResultSet         rs          = null;
		StringBuffer      strQuery    = new StringBuffer();
		ArrayList<HashMap<String, String>>  rsval  = new ArrayList<HashMap<String, String>>();
		HashMap<String, String>			  rst		  = null;
		
		try { 
			rsval.clear();
			strQuery.setLength(0);
			strQuery.append("SELECT a.cm_volpath,b.cm_rsrccd from cmm0038 b,cmm0031 a,cmm0036 c \n");
			strQuery.append(" WHERE c.cm_syscd=? and c.cm_closedt is null                       \n");
			strQuery.append("   and c.cm_syscd=b.cm_syscd and c.cm_rsrccd=b.cm_rsrccd           \n");
			strQuery.append("   and b.cm_syscd=a.cm_syscd and a.cm_svrcd='01'                   \n");
			strQuery.append("   and b.cm_svrcd=a.cm_svrcd and b.cm_seqno=a.cm_seqno             \n");
			strQuery.append("   and a.cm_closedt is null                                        \n");
			strQuery.append(" order by b.cm_rsrccd,b.cm_volpath                                 \n");
			pstmt = conn.prepareStatement(strQuery.toString());
			pstmt.setString(1, SysCd);
            rs = pstmt.executeQuery();
            
			while (rs.next()){
				rst = new HashMap<String, String>();
				rst.put("cm_rsrccd", rs.getString("cm_rsrccd"));
				if (rs.getString("cm_volpath").substring(rs.getString("cm_volpath").length() - 1).equals("/"))
					rst.put("cm_volpath", rs.getString("cm_volpath"));
				else
					rst.put("cm_volpath", rs.getString("cm_volpath") + "/");
				rsval.add(rst);
				rst = null;
			}
			rs.close();
			pstmt.close();
			
			rs = null;
			pstmt = null;
			
			return rsval;
			
		} catch (SQLException sqlexception) {
			sqlexception.printStackTrace();
			ecamsLogger.error("## SystemPath.getSysPath() SQLException START ##");
			ecamsLogger.error("## Error DESC : ", sqlexception);
			ecamsLogger.error("## SystemPath.getSysPath() SQLException END ##");
			throw sqlexception;
		} catch (Exception exception) {
			exception.printStackTrace();
			ecamsLogger.error("## SystemPath.getSysPath() Exception START ##");
			ecamsLogger.error("## Error DESC : ", exception);
			ecamsLogger.error("## SystemPath.getSysPath() Exception END ##");
			throw exception;
		}finally{
			if (strQuery != null) 	strQuery = null;
			if (rs != null)     try{rs.close();}catch (Exception ex){ex.printStackTrace();}
			if (pstmt != null)  try{pstmt.close();}catch (Exception ex2){ex2.printStackTrace();}
		}
	}//end of getUserPath_conn() method statement

	public ArrayList<HashMap<String, String>> getTopSysPath_ztree(String UserID,String progName,boolean upSw) throws SQLException, Exception {
		Connection        conn        = null;
		PreparedStatement pstmt       = null;
		ResultSet         rs          = null;
		PreparedStatement pstmt2      = null;
		ResultSet         rs2         = null;
		StringBuffer      strQuery    = new StringBuffer();
		Object[] returnObjectArray	  = null;
		int               maxSeq      = 0;	
		int               sysSeq      = 0;		
		int               svrSeq      = 0;		
		int               rsrcSeq     = 0;	
		boolean           findSw      = false;
		String            strSysName  = null;
		String            strSvrName  = null;
		String            svSvrName   = null;
		ArrayList<HashMap<String, String>>  rsval =	 new ArrayList<HashMap<String, String>>();
		HashMap<String, String>			  	rst	  	= null;
		HashMap<String, String> 			pathMap	= null;
		ArrayList<HashMap<String, String>> 	pathArr	= new ArrayList<HashMap<String, String>>();
		
		ConnectionContext connectionContext = new ConnectionResource();		
		
		try {
			
			conn = connectionContext.getConnection();
			
			int cnt = 0;
			strQuery.setLength(0);
			strQuery.append("select f.cm_sysmsg,e.cm_svrname,d.cm_seqno,       \n");
			strQuery.append("       a.cm_rsrccd,a.cm_info,d.cm_codename,       \n");
			strQuery.append("       c.cm_volpath,f.cm_syscd                    \n");
			strQuery.append("  from cmm0036 a,cmm0030 f,cmm0044 h,             \n");
			strQuery.append("       (select cr_syscd,cr_rsrccd from cmr0020    \n");
			strQuery.append("         group by cr_syscd,cr_rsrccd) b           \n");
			strQuery.append("       ,cmm0020 d,cmm0038 c,cmm0031 e             \n");
			if (progName != null && !"".equals(progName)) { 
    			strQuery.append(",(select y.cm_syscd,y.cm_dirpath              \n");
    			strQuery.append("    from cmm0070 y,cmr0020 x                  \n");
    			if (upSw == false) {
    				strQuery.append("where upper(x.cr_rsrcname) like upper(?)  \n");
        		} else {
        			strQuery.append("where x.cr_rsrcname like ?                \n");
        		}
    			strQuery.append("  and x.cr_syscd=y.cm_syscd                   \n");	
    			strQuery.append("  and x.cr_dsncd=y.cm_dsncd                   \n");	
    			strQuery.append("group by y.cm_syscd,y.cm_dirpath) g           \n");	        		
        		
        	}
			strQuery.append(" where f.cm_closedt is null                       \n");
			strQuery.append("   and h.cm_userid=? and h.cm_closedt is null     \n");
			strQuery.append("   and h.cm_syscd=f.cm_syscd                      \n");
			strQuery.append("   and f.cm_syscd=c.cm_syscd                      \n");
			strQuery.append("   and a.cm_rsrccd not in                         \n");
			strQuery.append("      (select cm_samersrc from cmm0037            \n");
			strQuery.append("        where cm_syscd=a.cm_syscd)                \n");
			strQuery.append("   and a.cm_syscd=b.cr_syscd                      \n");
			strQuery.append("   and a.cm_rsrccd=b.cr_rsrccd                    \n");
			strQuery.append("   and d.cm_macode='JAWON'                        \n");
			strQuery.append("   and d.cm_micode=a.cm_rsrccd                    \n");
			strQuery.append("   and a.cm_syscd=c.cm_syscd                      \n");
			strQuery.append("   and a.cm_rsrccd=c.cm_rsrccd and c.cm_svrcd=f.cm_dirbase\n");
			strQuery.append("   and c.cm_syscd=e.cm_syscd                      \n");
			strQuery.append("   and c.cm_svrcd=e.cm_svrcd                      \n");
			strQuery.append("   and c.cm_seqno=e.cm_seqno                      \n");
			if (progName != null && !"".equals(progName)) { 
				strQuery.append("and c.cm_syscd=g.cm_syscd                     \n");
				strQuery.append("and g.cm_dirpath like c.cm_volpath || '%'     \n");
			}
			strQuery.append("   and f.cm_syscd = '01200'                       \n");	//차세대시스템
			strQuery.append(" group by f.cm_syscd,e.cm_svrname,d.cm_seqno,a.cm_rsrccd,a.cm_info,d.cm_codename,c.cm_volpath,f.cm_sysmsg \n");
			strQuery.append(" order by f.cm_syscd,e.cm_svrname,d.cm_seqno,a.cm_rsrccd     \n");
			
            pstmt = conn.prepareStatement(strQuery.toString());	
            //pstmt = new LoggableStatement(conn,strQuery.toString());
            if (progName != null && !"".equals(progName)) { 
            	pstmt.setString(++cnt, "%"+progName+"%");
        	}
            pstmt.setString(++cnt, UserID);
            //ecamsLogger.error(((LoggableStatement)pstmt).getQueryString());            
            rs = pstmt.executeQuery();
            while (rs.next()){
 	        	
 	        	findSw = false;
 	        	if (strSysName == null) findSw = true;
 	        	else if (!strSysName.equals(rs.getString("cm_sysmsg"))) findSw = true;
 	        	
 	        	if (findSw == true) {
 	        		++maxSeq;
 	        		strSysName = rs.getString("cm_sysmsg");
 	        		sysSeq = maxSeq;
 	        		strSvrName = null;
 	        		rst = new HashMap<String,String>();
					rst.put("cm_dirpath",strSysName);							
					rst.put("cm_fullpath","");
					rst.put("cm_upseq",Integer.toString(maxSeq));
					rst.put("cm_seqno",Integer.toString(maxSeq));
					rst.put("cm_syscd",rs.getString("cm_syscd"));
					rst.put("cr_rsrccd","");
					rst.put("gbncd", "SYS");
					rst.put("cr_itemid", ""); 
					rst.put("cr_lstver", "");
					rst.put("bransw", "true");
					rsval.add(rst); 
 	        	}
 	        	
 	        	findSw = false;
 	        	if (strSvrName == null) findSw = true;
 	        	else if (!strSvrName.equals(rs.getString("cm_svrname"))) findSw = true;
 	        	
 	        	if (findSw == true) {
 	        		++maxSeq;
 	        		strSvrName = rs.getString("cm_svrname");
 	        		svSvrName = strSvrName.replace("_개발", "");
 	        		svSvrName = svSvrName.replace("개발", "");
 	        		svrSeq = maxSeq;
 	        		rst = new HashMap<String,String>();
 	        		rst.put("cm_dirpath",svSvrName);
					rst.put("cm_fullpath","");
					rst.put("cm_upseq",Integer.toString(sysSeq));
					rst.put("cm_seqno",Integer.toString(maxSeq));
					rst.put("cm_syscd",rs.getString("cm_syscd"));
					rst.put("cr_rsrccd","");
					rst.put("gbncd", "SVR");
					rst.put("cr_itemid", ""); 
					rst.put("cr_lstver", "");
					rst.put("bransw", "true");
					rsval.add(rst); 
 	        	}
 	        	++maxSeq;
 	        	rsrcSeq = maxSeq;
				rst = new HashMap<String,String>();
				rst.put("cm_dirpath",rs.getString("cm_codename"));							
				rst.put("cm_fullpath",rs.getString("cm_volpath"));
				rst.put("cm_upseq",Integer.toString(svrSeq));
				rst.put("cm_seqno",Integer.toString(maxSeq));
				rst.put("cm_syscd",rs.getString("cm_syscd"));
				rst.put("cr_rsrccd",rs.getString("cm_rsrccd"));
				rst.put("gbncd", "RSC");
				rst.put("cr_itemid", "");
				rst.put("cr_lstver", "");
				rst.put("bransw", "true");
				rsval.add(rst); 
				
				cnt = 0;
 	        	strQuery.setLength(0);
 	        	strQuery.append("select a.cr_rsrcname,a.cr_itemid,a.cr_lstver  \n");
 	        	strQuery.append("  from cmm0044 d,cmm0070 b,cmr0020 a,cmm0036 c\n");
 	        	strQuery.append(" where b.cm_syscd=? and b.cm_dirpath=?        \n");
 	        	strQuery.append("   and b.cm_syscd=a.cr_syscd                  \n");
 	        	strQuery.append("   and b.cm_dsncd=a.cr_dsncd                  \n");
 	        	strQuery.append("   and a.cr_lstver>0 and a.cr_status<>'9'     \n");
 	        	strQuery.append("   and d.cm_userid=? and d.cm_closedt is null \n");
 	        	strQuery.append("   and d.cm_syscd=?                           \n");
 	        	strQuery.append("   and d.cm_jobcd=a.cr_jobcd                  \n");
 	        	if (progName != null && !"".equals(progName)) { 	        		
 	        		if (upSw == false) {
 	        			strQuery.append("and upper(a.cr_rsrcname) like upper(?)\n");
 	        		} else {
 	        			strQuery.append("and a.cr_rsrcname like ?              \n");
 	        		}
 	        	}
 	        	strQuery.append("   and (a.cr_rsrccd=? or                      \n");
 				strQuery.append("        a.cr_rsrccd in                        \n");
 				strQuery.append("       (select cm_samersrc from cmm0037       \n");
 				strQuery.append("        where cm_syscd=? and cm_rsrccd=?))    \n");
 				strQuery.append("   and a.cr_syscd=c.cm_syscd                  \n");
 				strQuery.append("   and a.cr_rsrccd=c.cm_rsrccd                \n");
 				strQuery.append("   and substr(c.cm_info,10,1)='0'             \n");
 				strQuery.append("   and substr(c.cm_info,12,1)='1'             \n");
 	        	strQuery.append("order by a.cr_rsrcname                        \n");
 	        	
 	        	pstmt2 = conn.prepareStatement(strQuery.toString());
 	        	//pstmt2 = new LoggableStatement(conn,strQuery.toString());
 	        	pstmt2.setString(++cnt, rs.getString("cm_syscd"));
 	        	pstmt2.setString(++cnt, rs.getString("cm_volpath"));
 	        	pstmt2.setString(++cnt, UserID);
 	        	pstmt2.setString(++cnt, rs.getString("cm_syscd"));
 	        	if (progName != null && !"".equals(progName)) { 
 	        		pstmt2.setString(++cnt, "%"+progName+"%");
 	        	}
 	        	pstmt2.setString(++cnt, rs.getString("cm_rsrccd"));
 	        	pstmt2.setString(++cnt, rs.getString("cm_syscd"));
 	        	pstmt2.setString(++cnt, rs.getString("cm_rsrccd"));
 	        	//ecamsLogger.error(((LoggableStatement)pstmt2).getQueryString()); 
 	        	rs2 = pstmt2.executeQuery();
 	        	while (rs2.next()) {
 	        		++maxSeq;
 	        		rst = new HashMap<String,String>();
					rst.put("cm_dirpath",rs2.getString("cr_rsrcname"));							
					rst.put("cm_fullpath",rs.getString("cm_volpath"));
					rst.put("cm_upseq",Integer.toString(rsrcSeq));
					rst.put("cm_seqno",Integer.toString(maxSeq));
					rst.put("cm_syscd",rs.getString("cm_syscd"));
					rst.put("cr_rsrccd",rs.getString("cm_rsrccd"));
					rst.put("cr_lstver",rs2.getString("cr_lstver"));
					rst.put("gbncd", "ITM");
					rst.put("cr_itemid", rs2.getString("cr_itemid")); 
					rst.put("bransw", "false");
					rsval.add(rst); 
 	        	}
 	        	rs2.close();
 	        	pstmt2.close();
			}//end of while-loop statement
            
			rs.close();
			pstmt.close();
			conn.close();
			
			rs = null;
			pstmt = null;
			conn = null;

			if (rsval.size() > 0) {
				String strLstVer = "";
				for (int i = 0;rsval.size() > i;i++) {
					if (!rsval.get(i).get("gbncd").equals("ITM")) strLstVer = Integer.toString(maxSeq);
					else strLstVer = rsval.get(i).get("cr_lstver");
					
					pathMap = new HashMap<String, String>();
					pathMap.put("id", rsval.get(i).get("cm_seqno"));
					pathMap.put("pId", rsval.get(i).get("cm_upseq"));
					pathMap.put("name", rsval.get(i).get("cm_dirpath"));
					pathMap.put("cm_seqno", rsval.get(i).get("cm_seqno"));
					pathMap.put("cm_dirpath", rsval.get(i).get("cm_dirpath"));
					pathMap.put("cm_upseq", rsval.get(i).get("cm_upseq"));
					pathMap.put("cm_fullpath", rsval.get(i).get("cm_fullpath"));
					pathMap.put("cm_syscd", rsval.get(i).get("cm_syscd"));
					pathMap.put("cr_itemid", rsval.get(i).get("cr_itemid"));
					pathMap.put("cr_rsrccd", rsval.get(i).get("cr_rsrccd"));
					pathMap.put("cr_lstver", strLstVer);
					pathMap.put("gbncd", rsval.get(i).get("gbncd"));
					pathMap.put("isParent", rsval.get(i).get("bransw"));
					pathArr.add(pathMap);
				}
			}
			
			return pathArr;			
			
		} catch (SQLException sqlexception) {
			sqlexception.printStackTrace();
			ecamsLogger.error("## SystemPath.getTopSysPath_ztree() SQLException START ##");
			ecamsLogger.error("## Error DESC : ", sqlexception);	
			ecamsLogger.error("## SystemPath.getTopSysPath_ztree() SQLException END ##");			
			throw sqlexception;
		} catch (Exception exception) {
			exception.printStackTrace();
			ecamsLogger.error("## SystemPath.getTopSysPath_ztree() Exception START ##");				
			ecamsLogger.error("## Error DESC : ", exception);	
			ecamsLogger.error("## SystemPath.getTopSysPath_ztree() Exception END ##");				
			throw exception;
		}finally{
			if (strQuery != null) 	strQuery = null;
			if (returnObjectArray != null)	returnObjectArray = null;
			if (rs != null)     try{rs.close();}catch (Exception ex){ex.printStackTrace();}
			if (pstmt != null)  try{pstmt.close();}catch (Exception ex2){ex2.printStackTrace();}
			if (rs2 != null)    try{rs2.close();}catch (Exception ex){ex.printStackTrace();}
			if (pstmt2 != null) try{pstmt2.close();}catch (Exception ex2){ex2.printStackTrace();}
			if (conn != null){
				try{
					ConnectionResource.release(conn);
				}catch(Exception ex3){
					ecamsLogger.error("## SystemPath.getTopSysPath_ztree() connection release exception ##");
					ex3.printStackTrace();
				}
			}
		}
	}//end of getTopSysPath_ztree() method statement
	
	public ArrayList<HashMap<String, String>> getDirPathFile_ztree(String UserId,String SysCd,String RsrcCd,String seqNo,String maxCnt,String progName,boolean upSw) throws SQLException, Exception {
		Connection        conn        = null;
		PreparedStatement pstmt       = null;
		ResultSet         rs          = null;
		StringBuffer      strQuery    = new StringBuffer();
		UserInfo		  userInfo	  = new UserInfo();
		String            strHome     = null;
		String[]          pathDepth   = null;
		String            strDir      = null;
		int               upSeq       = 0; 
		int               maxSeq      = Integer.parseInt(maxCnt);
		boolean           findSw      = false;
		int               parmCnt     = 0;
		String            strPath     = null;
		ArrayList<HashMap<String, String>>  rsval 		= new ArrayList<HashMap<String, String>>();
		HashMap<String, String>			  	rst	  		= null;
		HashMap<String, String>				pathMap		= null;
		ArrayList<HashMap<String, String>> 	pathArr 	= new ArrayList<HashMap<String, String>>();
		
		Object[] returnObjectArray = null;
		ConnectionContext connectionContext = new ConnectionResource();
		
		Boolean			  AdminYn= false;
		
		try {
			conn = connectionContext.getConnection();
			
			AdminYn = userInfo.isAdmin_conn(UserId, conn);
			userInfo = null;
			
			strQuery.setLength(0);
			strQuery.append("select b.cm_dirpath volpath,b.cm_dsncd,c.cm_volpath,        \n");
			strQuery.append("       d.cr_itemid,d.cr_lstver ,d.cr_rsrcname,f.cm_info     \n");
			strQuery.append("  from cmm0070 b,cmm0031 a,cmr0020 d,cmm0038 c,cmm0036 f,cmm0044 e,cmm0030 g \n");
			strQuery.append(" where e.cm_userid=? and e.cm_syscd=?                       \n");
			strQuery.append("   and e.cm_closedt is null and e.cm_syscd=a.cm_syscd       \n"); 
			strQuery.append("   and a.cm_svrcd=g.cm_dirbase and a.cm_closedt is null             \n"); 
			strQuery.append("   and a.cm_syscd=c.cm_syscd and a.cm_svrcd=c.cm_svrcd      \n");
			strQuery.append("   and a.cm_seqno=c.cm_seqno and g.cm_syscd=e.cm_syscd       \n");
        	strQuery.append("   and (c.cm_rsrccd=? or                                    \n");
			strQuery.append("        c.cm_rsrccd in                                      \n");
			strQuery.append("          (select cm_samersrc from cmm0037                  \n");
			strQuery.append("            where cm_syscd=? and cm_rsrccd=?))              \n");
			strQuery.append("   and c.cm_syscd=f.cm_syscd and c.cm_rsrccd=f.cm_rsrccd    \n");
			strQuery.append("   and substr(f.cm_info,12,1)='1'                           \n");
			strQuery.append("   and c.cm_syscd=d.cr_syscd and c.cm_rsrccd=d.cr_rsrccd    \n");
			strQuery.append("   and d.cr_status<>'9'                                     \n");
			strQuery.append("   and d.cr_jobcd=e.cm_jobcd                                \n");
			if (progName != null && !"".equals(progName)) { 	        		
        		if (upSw == false) {
        			strQuery.append("and upper(d.cr_rsrcname) like upper(?)\n");
        		} else {
        			strQuery.append("and d.cr_rsrcname like ?              \n");
        		}
        	}
			if (!AdminYn) strQuery.append("and d.cr_jobcd=e.cm_jobcd                     \n");
			strQuery.append("   and b.cm_syscd=d.cr_syscd and b.cm_dsncd=d.cr_dsncd      \n");     
			strQuery.append(" order by b.cm_dirpath,d.cr_rsrcname                        \n");  
			
			pstmt = conn.prepareStatement(strQuery.toString());
			//pstmt = new LoggableStatement(conn,strQuery.toString());
			pstmt.setString(++parmCnt, UserId);
			pstmt.setString(++parmCnt, SysCd);
			pstmt.setString(++parmCnt, RsrcCd);
			pstmt.setString(++parmCnt, SysCd);
			pstmt.setString(++parmCnt, RsrcCd);
			if (progName != null && !"".equals(progName)) pstmt.setString(++parmCnt, "%"+progName+"%");
            rsval.clear(); 
            //ecamsLogger.error(((LoggableStatement)pstmt).getQueryString()); 
	        rs = pstmt.executeQuery();
	        rsval.clear();
	        while (rs.next()){
	        	findSw = false;
	        	if (rs.getString("cr_lstver").equals("0") && rs.getString("cm_info").substring(44,45).equals("1")) {
	        		strDir = "";
	        	} else if (rs.getString("cm_info").substring(9,10).equals("1") && !rs.getString("cm_info").substring(45,46).equals("1")) { 
	        		strDir = "";
	        	} else {
		        	if (strPath == null) findSw = true;
		        	else if (!strPath.equals(rs.getString("volpath"))) findSw = true;
		        	//ecamsLogger.error("cr_rsrcname 2====>" + rs.getString("volpath") + "/" + rs.getString("cr_rsrcname"));
		        	if (findSw == true) {
						if (rs.getString("cm_volpath") != null) {
							strDir = rs.getString("cm_volpath");
							if (strDir.length()>0){
								if (strDir.substring(strDir.length()-1).equals("/")){
									strDir = strDir.substring(0,strDir.length()-1);
								}
							}
							strHome = strDir;
							if (rs.getString("volpath").length() >= strHome.length()){
								if (!rs.getString("volpath").substring(0,strHome.length()).equals(strHome)) {
									strHome = "";
								}
							}
						} else strHome = "";
						
						strDir = rs.getString("volpath").replace(strDir, "");
						
						
						if (strDir.length() > 0){
							pathDepth = strDir.substring(1).split("/");
						
							strDir = "/";
							upSeq = Integer.parseInt(seqNo);
							findSw = false;
							
							for (int i = 0;pathDepth.length > i;i++) {
								if (strDir.length() > 1 ) {
									strDir = strDir + "/";
								}
								strDir = strDir + pathDepth[i];
								//ecamsLogger.debug("strDir====>" + strDir);
								findSw = false;
								if (rsval.size() > 0) {
									for (int j = 0;rsval.size() > j;j++) {
										if (rsval.get(j).get("cm_fullpath").equals(strHome+strDir)) {
											upSeq = Integer.parseInt(rsval.get(j).get("cm_seqno"));
											
											//rsval.set(j, element)
											//rst.put("branch","true");
											findSw = true;
											break;
										}
									}
								} else {
									findSw = false;
								}
								if (!findSw) {
									maxSeq = maxSeq + 1;
									//System.out.println("1111"+strHome+strDir+"/"+pathDepth[i]);
									//ecamsLogger.error("dirpath,full,upseq,seq====>" + pathDepth[i] + "  , " + strDir  + "  , " + Integer.toString(upSeq)  + "  , " + maxCnt  + "  , " + Integer.toString(maxSeq));
									rst = new HashMap<String,String>();
									rst.put("cm_dirpath",pathDepth[i]);							
									rst.put("cm_fullpath",strHome+strDir);
									rst.put("cm_upseq",Integer.toString(upSeq));
									rst.put("cm_seqno",Integer.toString(maxSeq));
									rst.put("cr_rsrccd",RsrcCd);
									rst.put("gbncd", "DIR");
									rst.put("bransw", "true");
									rst.put("cr_itemid", rs.getString("cm_dsncd")); 
									if (rs.getString("volpath").equals(strHome+strDir)) rst.put("branch", "false");
									rsval.add(maxSeq - Integer.parseInt(maxCnt) - 1, rst); 
									rst = null;
									upSeq = maxSeq;
								}
							}
						}
		        	}
	        	}
	        	
	        	if (strDir.length() > 0){
	        		//ecamsLogger.error("cr_rsrcname 3====>" + Integer.toString(upSeq)  + "  , " + Integer.toString(maxSeq) + "," + rs.getString("volpath") + "/" + rs.getString("cr_rsrcname"));
	        		maxSeq = maxSeq + 1;
	        		//System.out.println("2222"+rs.getString("volpath")+"/"+rs.getString("cr_rsrcname"));
					//ecamsLogger.debug("dirpath,full,upseq,seq====>" + pathDepth[i] + "  , " + strDir  + "  , " + Integer.toString(upSeq)  + "  , " + Integer.toString(maxSeq));
					rst = new HashMap<String,String>();
					rst.put("cm_dirpath",rs.getString("cr_rsrcname"));							
					rst.put("cm_fullpath",rs.getString("volpath"));
					rst.put("cm_upseq",Integer.toString(upSeq));
					rst.put("cm_seqno",Integer.toString(maxSeq));
					rst.put("cr_rsrccd",RsrcCd);
					rst.put("cr_itemid", rs.getString("cr_itemid")); 
					rst.put("cr_lstver", rs.getString("cr_lstver")); 
					rst.put("cm_info",rs.getString("cm_info"));
					rst.put("gbncd", "ITM");
					rst.put("bransw", "false");
					rsval.add(maxSeq - Integer.parseInt(maxCnt) - 1, rst); 
					rst = null;
	        	}
	        	strPath = rs.getString("volpath");
				
			}//end of while-loop statement
	        
			rs.close();
			pstmt.close();
			conn.close();
			
			rs = null;
			pstmt = null;
			conn = null;

			if (rsval.size() > 0) {
				for (int i=0; rsval.size()>i; i++) {
					pathMap = new HashMap<String, String>();
					pathMap.put("id", rsval.get(i).get("cm_seqno"));
					pathMap.put("pId", rsval.get(i).get("cm_upseq"));
					pathMap.put("name", rsval.get(i).get("cm_dirpath"));
					pathMap.put("isParent", rsval.get(i).get("bransw"));
					pathMap.put("cm_seqno", rsval.get(i).get("cm_seqno"));
					pathMap.put("cm_dirpath", rsval.get(i).get("cm_dirpath"));
					pathMap.put("cm_upseq", rsval.get(i).get("cm_upseq"));
					pathMap.put("cm_fullpath", rsval.get(i).get("cm_fullpath"));
					pathMap.put("cm_syscd", SysCd);
					pathMap.put("cr_itemid", rsval.get(i).get("cr_itemid"));
					pathMap.put("cr_rsrccd", rsval.get(i).get("cr_rsrccd"));
					pathMap.put("cr_lstver", rsval.get(i).get("cr_lstver"));
					pathMap.put("cm_info", rsval.get(i).get("cm_info"));
					pathMap.put("gbncd", rsval.get(i).get("gbncd"));
					pathArr.add(pathMap);
				}
			}
			
			return pathArr;

		} catch (SQLException sqlexception) {
			sqlexception.printStackTrace();
			ecamsLogger.error("## SystemPath.getDirPathFile_ztree() SQLException START ##");
			ecamsLogger.error("## Error DESC : ", sqlexception);	
			ecamsLogger.error("## SystemPath.getDirPathFile_ztree() SQLException END ##");			
			throw sqlexception;
		} catch (Exception exception) {
			exception.printStackTrace();
			ecamsLogger.error("## SystemPath.getDirPathFile_ztree() Exception START ##");				
			ecamsLogger.error("## Error DESC : ", exception);	
			ecamsLogger.error("## SystemPath.getDirPathFile_ztree() Exception END ##");				
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
					ecamsLogger.error("## SystemPath.getDirPathFile_ztree() connection release exception ##");
					ex3.printStackTrace();
				}
			}
		}
	}//end of getDirPathFile_ztree() method statement
}