
/*****************************************************************************************
	1. program ID	: eCmd0400.java
	2. create date	: 2006.08. 08
	3. auth		    : NoName
	4. update date	: 
	5. auth		    : 
	6. description	: eCmd1500 [문서관리]->[상세정보]
*****************************************************************************************/

package app.eCmd;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.logging.log4j.Logger;
import app.common.LoggableStatement;
import com.ecams.common.dbconn.ConnectionContext;
import com.ecams.common.dbconn.ConnectionResource;
import com.ecams.common.logger.EcamsLogger;

/**
 * @author bigeyes
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */

public class Cmd1500{
    /**
     * Logger Class Instance Creation
     * logger
     */
    Logger ecamsLogger  = EcamsLogger.getLoggerInstance();
    
	/**
	 * project 등록
	 * @param  pPrjno,pDocname,pDocPath
	 * @return String
	 * @throws SQLException
	 * @throws Exception
	 */


	public Object[] getPrjNo(String DocId,String UserId) throws SQLException, Exception {
		Connection        conn        = null;
		PreparedStatement pstmt       = null;
		ResultSet         rs          = null;
		PreparedStatement pstmt2      = null;
		ResultSet         rs2         = null;
		StringBuffer      strQuery    = new StringBuffer();
		//String            strSelMsg   = "";
		//String            svPrjNo     = "";
		//boolean           findSw      = false;
		ArrayList<HashMap<String, String>> rtList = new ArrayList<HashMap<String, String>>();
		HashMap<String, String>			   rst	  = null;

		ConnectionContext connectionContext = new ConnectionResource();
		
		try {
			conn = connectionContext.getConnection();
			//strSelMsg = "선택하세요";	
			
			strQuery.setLength(0);
			strQuery.append("Select a.cd_prjno,a.cd_prjname,'N' cd_edityn      \n");
			strQuery.append("  from cmd0300 a,cmr0031 b                        \n");
			strQuery.append(" Where b.cr_docid=? and b.cr_prjno=a.cd_prjno     \n");
	                
	        pstmt = conn.prepareStatement(strQuery.toString());
	        //pstmt =  new LoggableStatement(conn, strQuery.toString());
	        pstmt.setString(1, DocId);
	        ////ecamsLogger.error(((LoggableStatement)pstmt).getQueryString());
            rs = pstmt.executeQuery();
            rtList.clear();
			while (rs.next()){
				/*
				if (rs.getRow() == 1) {
					rst = new HashMap<String,String>();
					rst.put("ID", "0");
					rst.put("cd_prjno", "00000");
					rst.put("cd_prjname", "");
					rst.put("prjnoname", strSelMsg);					
					rtList.add(rst);
					rst = null;
				}*/
					
				rst = new HashMap<String,String>();
				rst.put("ID", Integer.toString(rs.getRow()));
				rst.put("cd_prjno",rs.getString("cd_prjno"));
				rst.put("cd_prjname",rs.getString("cd_prjname"));
				rst.put("prjnoname",rs.getString("cd_prjno") + "   " + 
						rs.getString("cd_prjname"));
				rst.put("lastPrj", "N");
					
				strQuery.setLength(0);
				strQuery.append("select count(*) as cnt from cmd0304          \n");
				strQuery.append(" where cd_prjno=? and cd_edityn='Y'          \n");
				strQuery.append("   and cd_prjuser=? and cd_closedt is null   \n");
				pstmt2 = conn.prepareStatement(strQuery.toString());
				pstmt2.setString(1, rs.getString("cd_prjno"));
				pstmt2.setString(2, UserId);
				rs2 = pstmt2.executeQuery();
				if(rs2.next()) {
				   if (rs2.getInt("cnt") > 0) rst.put("cd_edityn", "Y"); 	
				   else rst.put("cd_edityn", "N");
				}
				rs2.close();
				pstmt2.close();
				
				strQuery.setLength(0);
				strQuery.append("Select count(cr_docid) as cnt from cmr0030 where cr_prjno=? ");//prjno
				pstmt2 = conn.prepareStatement(strQuery.toString());
				pstmt2.setString(1, rs.getString("cd_prjno"));
				rs2 = pstmt2.executeQuery();
				if(rs2.next()) {
					if (rs2.getInt("cnt") > 0) rst.put("docFileYN", "Y");
					else rst.put("docFileYN", "N");
				}
				rs2.close();
				pstmt2.close();
				rtList.add(rst);
				rst = null;
				
			}//end of while-loop statement
			rs.close();
			pstmt.close();
			conn.close();
			rs = null;
			pstmt = null;
			conn = null;
			return rtList.toArray();
						
			
		} catch (SQLException sqlexception) {
			sqlexception.printStackTrace();
			ecamsLogger.error("## Cmd1500.getPrjNo() SQLException START ##");
			ecamsLogger.error("## Error DESC : ", sqlexception);	
			ecamsLogger.error("## Cmd1500.getPrjNo() SQLException END ##");			
			throw sqlexception;
		} catch (Exception exception) {
			exception.printStackTrace();
			ecamsLogger.error("## Cmd1500.getPrjNo() Exception START ##");				
			ecamsLogger.error("## Error DESC : ", exception);	
			ecamsLogger.error("## Cmd1500.getPrjNo() Exception END ##");				
			throw exception;
		}finally{
			if (strQuery != null)	strQuery = null;
			if (rtList != null)	rtList = null;
			if (rs != null)     try{rs.close();}catch (Exception ex){ex.printStackTrace();}
			if (rs2 != null)     try{rs2.close();}catch (Exception ex1){ex1.printStackTrace();}
			if (pstmt != null)  try{pstmt.close();}catch (Exception ex2){ex2.printStackTrace();}
			if (pstmt2 != null)  try{pstmt2.close();}catch (Exception ex3){ex3.printStackTrace();}
			if (conn != null){
				try{
					ConnectionResource.release(conn);
				}catch(Exception ex3){
					ex3.printStackTrace();
				}
			}
		}

	}//end of getPrjNo() method statement
    public Object[] getHistList(HashMap<String,String> dataObj) throws SQLException, Exception {
		Connection        	conn        = null;
		PreparedStatement 	pstmt       = null;
		ResultSet         	rs          = null;
		PreparedStatement 	pstmt2      = null;
		ResultSet         	rs2         = null;
		StringBuffer      	strQuery    = new StringBuffer();
		ArrayList<HashMap<String, String>>  rsval = new ArrayList<HashMap<String, String>>();
		HashMap<String, String>			  	rst	  = null;
		
		ConnectionContext connectionContext = new ConnectionResource();
		try {
			conn = connectionContext.getConnection(); 
			
			String DocId = dataObj.get("DocId");
			String ReqCd = dataObj.get("ReqCd");
			String CondCd = dataObj.get("CondCd");
			String CondWord = dataObj.get("CondWord");
			
			strQuery.setLength(0);
		    strQuery.append("select a.cr_acptno,a.cr_version,a.cr_qrycd,a.cr_unittit,a.cr_status,a.cr_errcd, \n");
		    strQuery.append("       to_char(a.cr_prcdate,'yyyy/mm/dd hh24:mi') cr_prcdate,   \n");
		    strQuery.append("       to_char(c.cr_acptdate,'yyyy/mm/dd hh24:mi') cr_acptdate, \n");
    	    strQuery.append("       b.cm_username,c.cr_sayu,c.cr_emgcd,c.cr_docno,           \n");
    	    strQuery.append("       c.cr_qrycd qrycd,d.cm_codename,f.cr_docfile              \n");
    	    strQuery.append("  from cmr0030 f,cmm0020 d,cmr1100 a,cmm0040 b,cmr1000 c        \n");
    	    strQuery.append(" where a.cr_docid=? and a.cr_acptno=c.cr_acptno                 \n"); //PrjNo
    	    if (ReqCd != "" && ReqCd != null)//Cbo_ReqCd 전체가 아니면
    	       strQuery.append("and c.cr_qrycd=?                                             \n");//Cbo_ReqCd    	    
    	    
    	    if (CondCd != "" && CondCd != null){
    	    	if (CondCd.equals("02"))                      //요청번호
    	    		strQuery.append("and c.cr_acptno like ?                                  \n");// % Replace(Txt_Cond, "-", "") %
    	    	else if (CondCd.equals("03")) strQuery.append("and c.cr_sayu like ?          \n");//%Txt_Cond%
    	    	else if (CondCd.equals("04")) {               //요청자 
    	    		strQuery.append("and (c.cr_editor like ? or b.cm_username like ?)        \n");//%Txt_Cond%
    	    	}else if (CondCd.equals("06")) {              //변경근거
    	    		strQuery.append("and c.cr_docno is not null                              \n");
    	        	strQuery.append("and c.cr_docno like ?                                   \n");//%Txt_Cond%
    	    	}
    	    }
    	    strQuery.append("  and a.cr_editor=b.cm_userid                                   \n");
    	    strQuery.append("  and a.cr_docid=f.cr_docid                                     \n");
    	    strQuery.append("  and d.cm_macode='REQUEST' and d.cm_micode=c.cr_qrycd          \n");
    	    strQuery.append("order by c.cr_acptdate desc                                     \n");    	    
            pstmt = conn.prepareStatement(strQuery.toString());
            //pstmt =  new LoggableStatement(conn, strQuery.toString());
            int Cnt = 0;
            pstmt.setString(++Cnt, DocId);
            if (ReqCd != "" && ReqCd != null)//Cbo_ReqCd 전체가 아니면
     	       pstmt.setString(++Cnt, ReqCd);     	    
     	    if (CondCd != "" && CondCd != null){
     	    	if (CondCd.equals("02"))                      //요청번호
     	    		pstmt.setString(++Cnt,CondWord);
     	    	else if (CondCd.equals("03")) pstmt.setString(++Cnt,CondWord);
     	    	else if (CondCd.equals("04")) {               //요청자
     	    		pstmt.setString(++Cnt,CondWord);
     	    		pstmt.setString(++Cnt,CondWord);
     	    	}else if (CondCd.equals("06")) {              //변경근거
     	    		pstmt.setString(++Cnt,CondWord);
     	    		pstmt.setString(++Cnt,CondWord);       
     	    	}
     	    }          	
           	////ecamsLogger.error(((LoggableStatement)pstmt).getQueryString());
            rs = pstmt.executeQuery();
            rsval.clear();
            String Tmp = "";            
            String SubItems1 = "";
            String CodeName_Tmp1 = "";
            String CodeName_Tmp2 = "";
            while (rs.next()){
            	rst = new HashMap<String, String>();
            	rst.put("NO", Integer.toString(rs.getRow()));
            	
            	SubItems1 = rs.getString("cm_codename");            	
                if (!rs.getString("qrycd").equals("31")){
                    if (!Tmp.equals(rs.getString("cr_qrycd"))){
                    	strQuery.setLength(0);
                    	strQuery.append("select cm_codename from cmm0020 \n");
                    	strQuery.append("where cm_macode='CHECKIN' and cm_micode=? \n");//rs.getString("cr_qrycd")
                        pstmt2 = conn.prepareStatement(strQuery.toString());
                        pstmt2.setString(1, rs.getString("cr_qrycd"));
                        rs2 = pstmt2.executeQuery();
                        if (rs2.next()){
                            CodeName_Tmp1 = rs2.getString("cm_codename");
                            if (CodeName_Tmp1 != null && CodeName_Tmp1 != "")
                            	SubItems1 = SubItems1 + "[" + CodeName_Tmp1 + "]";
                        }
                        rs2.close();
                        pstmt2.close();
                    }else{
                    	if (CodeName_Tmp1 != null)
                    		SubItems1 = SubItems1 + "[" + CodeName_Tmp1 + "]";
                    }
                }
                rst.put("SubItems1", SubItems1);
                rst.put("cm_username", rs.getString("cm_username"));
                //ecamsLogger.debug("+++++++++++cr_status++++++"+rs.getString("cr_status"));
                
                if (rs.getString("cr_prcdate") == null) rst.put("cr_prcdate", "진행중");
                else {
                	if (rs.getString("cr_status").equals("3")) rst.put("cr_prcdate", "[반려]"+rs.getString("cr_prcdate"));                
                	else  rst.put("cr_prcdate", rs.getString("cr_prcdate"));                
                }
                rst.put("cr_docfile", rs.getString("cr_docfile"));                
                rst.put("cr_acptno", rs.getString("cr_acptno"));                 
                rst.put("acptno", rs.getString("cr_acptno").substring(0,4)+"-"+
                		             rs.getString("cr_acptno").substring(4,6)+"-"+
                		             rs.getString("cr_acptno").substring(6,12));                
                rst.put("cr_version", rs.getString("cr_version"));
                if (rs.getString("cr_errcd") == null){
                	rst.put("cr_errcd", "");
                }else{
                	rst.put("cr_errcd", rs.getString("cr_errcd"));
                }
                
                if (rs.getString("cr_emgcd") != null){
                	strQuery.setLength(0);
                	strQuery.append("select cm_codename from cmm0020 \n");
                	strQuery.append("where cm_macode='REQGBN' and cm_micode=? \n");//cr_emgcd
                	pstmt2 = conn.prepareStatement(strQuery.toString());
                	pstmt2.setString(1,rs.getString("cr_emgcd"));
                	rs2 = pstmt2.executeQuery();
                	if (rs2.next()){
                		CodeName_Tmp2 = "[" + rs2.getString("cm_codename") + "] ";
                	}
                	rs2.close();
                	pstmt2.close();
                    if (rs.getString("cr_docno") != null){
                    	CodeName_Tmp2 = CodeName_Tmp2 + rs.getString("cr_docno");
                    }
                    rst.put("SubItems7", CodeName_Tmp2);
                }else rst.put("SubItems7", "");
                
                if (rs.getString("cr_unittit") != null)
                   rst.put("SubItems8", rs.getString("cr_unittit"));
                else
                   rst.put("SubItems8", rs.getString("cr_sayu"));
                   
                rst.put("cr_acptno", rs.getString("cr_acptno"));
                rst.put("cr_qrycd", rs.getString("cr_qrycd"));
                rst.put("qrycd", rs.getString("qrycd"));
                rst.put("cr_status", rs.getString("cr_status"));
                Tmp = rs.getString("cr_qrycd");
				rsval.add(rst);
				rst = null;
			}
            rs.close();
            pstmt.close();
            conn.close();
			rs = null;
			pstmt = null;
			conn = null;
    		return rsval.toArray();
            
		} catch (SQLException sqlexception) {
			sqlexception.printStackTrace();
			ecamsLogger.error("## Cmd1500.getHistList() SQLException START ##");
			ecamsLogger.error("## Error DESC : ", sqlexception);	
			ecamsLogger.error("## Cmd1500.getHistList() SQLException END ##");			
			throw sqlexception;
		} catch (Exception exception) {
			exception.printStackTrace();
			ecamsLogger.error("## Cmd1500.getHistList() Exception START ##");				
			ecamsLogger.error("## Error DESC : ", exception);	
			ecamsLogger.error("## Cmd1500.getHistList() Exception END ##");				
			throw exception;
		}finally{
			if (strQuery != null)	strQuery = null;
			if (rsval != null)	rsval = null;
			if (rs != null)     try{rs.close();}catch (Exception ex){ex.printStackTrace();}
			if (rs2 != null)     try{rs.close();}catch (Exception ex1){ex1.printStackTrace();}
			if (pstmt != null)  try{pstmt.close();}catch (Exception ex2){ex2.printStackTrace();}
			if (pstmt2 != null)  try{pstmt2.close();}catch (Exception ex3){ex3.printStackTrace();}
			if (conn != null){
				try{
					ConnectionResource.release(conn);
				}catch(Exception ex3){
					ex3.printStackTrace();
				}
			}
		}
	}//end of getHistList() method statement
		
//    public Object[] getDocList(String UserId,String PrjNo,String CondCd,String CondWord,String dirYn,String DocSeq,String DocId) throws SQLException, Exception {
    public Object[] getDocList(HashMap<String,String> dataObj) throws SQLException, Exception {
		Connection        	conn        = null;
		PreparedStatement 	pstmt       = null;
		ResultSet         	rs          = null;
		PreparedStatement 	pstmt2       = null;
		ResultSet         	rs2          = null;
		StringBuffer      	strQuery    = new StringBuffer();
		ArrayList<HashMap<String, String>>  rsval = new ArrayList<HashMap<String, String>>();
		HashMap<String, String>			  	rst	  = null;
		int                 Cnt         = 0;
		
		ConnectionContext connectionContext = new ConnectionResource();
		try {
			conn = connectionContext.getConnection();
			
			String UserId = dataObj.get("UserId");
			String PrjNo = dataObj.get("PrjNo");
			String CondCd = dataObj.get("CondCd");
			String CondWord = dataObj.get("CondWord");
			String dirYn = dataObj.get("dirYn");
			String strDocSeq = dataObj.get("strDocSeq");
			String strDocId = dataObj.get("strDocId");

			strQuery.setLength(0);
	        strQuery.append("select a.cr_docid,a.cr_docfile,a.cr_lstver,a.cr_status,         \n");
	        strQuery.append("       c.cr_prjno,d.cm_codename sta,f.cm_username creator,      \n");
	        strQuery.append("       to_char(a.cr_creatdt,'yyyy/mm/dd hh24:mi') cr_creatdt,   \n");
	        strQuery.append("       to_char(a.cr_lastdt,'yyyy/mm/dd hh24:mi') cr_lastdt,     \n");
	        strQuery.append("       a.cr_ccbyn,g.cm_username editor,a.cr_devstep,a.cr_docsta \n");
	        strQuery.append("  from cmr0030 a,cmr0031 c,cmm0020 d,cmm0040 f,cmm0040 g        \n");
	        strQuery.append(" where c.cr_prjno=? and c.cr_docid=a.cr_docid                   \n");//PrjNo
	        if (strDocId != null && strDocId != "") strQuery.append("and c.cr_docid=?        \n");//docid
	        else {
	        	if (!CondCd.equals("00")) {
		        	if (CondCd.equals("04")) {
		        		strQuery.append("and a.cr_devstep in (select cm_micode from cmm0020  \n");
		        		strQuery.append("                  where cm_macode='DEVSTEP'         \n");
		        		strQuery.append("                  and cm_codename like '%' || ? || '%') \n");//devstepname
		        	} else if (CondCd.equals("05")) {
		        		strQuery.append("and a.cr_docfile like '%' || ? || '%'               \n");//docfile
		        	}	        		
		        }
	        	
		        if (strDocSeq != "" && strDocSeq != null){
	                if (dirYn.equals("true")){//하위폴더포함
	                	strQuery.append("and c.cr_docseq in (SELECT CD_DOCSEQ                 \n");
	                	strQuery.append("   FROM (select CD_DOCSEQ,CD_UPDOCSEQ from CMD0303   \n");
	                	strQuery.append("   WHERE CD_PRJNO = ?)                               \n");//PrjNo
	                	strQuery.append("   START WITH CD_DOCSEQ=?                            \n");//DocSeq
	                	strQuery.append("   CONNECT BY PRIOR CD_DOCSEQ = CD_UPDOCSEQ          \n");
	                	strQuery.append("   union select ? from dual)         \n");//DocSeq
	                }else
	                	strQuery.append("and c.cr_docseq=? \n");//DocSeq
	            }
	        }
	        strQuery.append("  and a.cr_creator=f.cm_userid and a.cr_editor=g.cm_userid      \n");
	        strQuery.append("  and d.cm_macode='CMR0020' and d.cm_micode=a.cr_status         \n");
	        if (dataObj.get("Chk_Abandon").equals("false")){
	        	strQuery.append("  and a.cr_closedt is null \n");
	        }
	        strQuery.append("  order by cr_docfile \n");
	        
	        pstmt = conn.prepareStatement(strQuery.toString());
            //pstmt =  new LoggableStatement(conn, strQuery.toString());
	        
            pstmt.setString(++Cnt, PrjNo);
            if (strDocId != null && strDocId != "") pstmt.setString(++Cnt,strDocId);
            else {
            	if (!CondCd.equals("00")) {
    	        	if (CondCd.equals("04")) {
    	        		pstmt.setString(++Cnt, CondWord);
    	        	} else if (CondCd.equals("05")) {
    	        		pstmt.setString(++Cnt, CondWord);
    	        	}
    	        }
            	
                if (strDocSeq != "" && strDocSeq != null){
                    if (dirYn.equals("true")){//하위폴더포함
                    	pstmt.setString(++Cnt, PrjNo);
                    	pstmt.setString(++Cnt, strDocSeq);
                    	pstmt.setString(++Cnt, strDocSeq);
                    }else
                    	pstmt.setString(++Cnt, strDocSeq);
                }
            }
            
           	////ecamsLogger.error(((LoggableStatement)pstmt).getQueryString());
            rs = pstmt.executeQuery();
            rsval.clear();            
            while (rs.next()){
            	rst = new HashMap<String, String>();
            	rst.put("cr_creatdt", rs.getString("cr_creatdt"));
            	rst.put("creator", rs.getString("creator"));
            	rst.put("cr_lastdt", rs.getString("cr_lastdt"));
            	rst.put("editor", rs.getString("editor"));
            	rst.put("sta", rs.getString("sta"));
            	rst.put("cr_docfile", rs.getString("cr_docfile"));
            	rst.put("cr_docid", rs.getString("cr_docid"));
            	rst.put("cr_ccbyn", rs.getString("cr_ccbyn"));
            	rst.put("cr_status", rs.getString("cr_status"));
            	rst.put("cr_lstver", rs.getString("cr_lstver"));
            	rst.put("cr_devstep", rs.getString("cr_devstep"));
            	rst.put("cr_docsta", rs.getString("cr_docsta"));
            	
            	strQuery.setLength(0);
            	strQuery.append("select count(*) cnt from cmr0031 a,cmd0304 b               \n");
            	strQuery.append(" where a.cr_docid=?                                        \n");
            	strQuery.append("   and a.cr_prjno=b.cd_prjno                               \n");
            	strQuery.append("   and b.cd_edityn='Y' and b.cd_prjuser=?                  \n");
            	pstmt2 = conn.prepareStatement(strQuery.toString());
    	        
                //pstmt =  new LoggableStatement(conn, strQuery.toString());
                pstmt2.setString(1, rs.getString("cr_docid"));
                pstmt2.setString(2, UserId);
               	////ecamsLogger.error(((LoggableStatement)pstmt).getQueryString());
                rs2 = pstmt2.executeQuery();
                if (rs2.next()) {
                	if (rs2.getInt("cnt") > 0) rst.put("edityn", "Y");
                	else  rst.put("edityn", "N");
                }
                rs2.close();
                pstmt2.close();
                
            	rsval.add(rst);
            	rst = null;
			}
            rs.close();
            pstmt.close();
            conn.close();
            
			rs = null;
			pstmt = null;
			conn = null;
            
    		return rsval.toArray();
            
		} catch (SQLException sqlexception) {
			sqlexception.printStackTrace();
			ecamsLogger.error("## Cmd1500.getDocList() SQLException START ##");
			ecamsLogger.error("## Error DESC : ", sqlexception);	
			ecamsLogger.error("## Cmd1500.getDocList() SQLException END ##");			
			throw sqlexception;
		} catch (Exception exception) {
			exception.printStackTrace();
			ecamsLogger.error("## Cmd1500.getDocList() Exception START ##");				
			ecamsLogger.error("## Error DESC : ", exception);	
			ecamsLogger.error("## Cmd1500.getDocList() Exception END ##");				
			throw exception;
		}finally{
			if (strQuery != null)	strQuery = null;
			if (rsval != null)	rsval = null;
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

	}//end of getDocList() method statement
    public Object[] getEditor(String PrjNo) throws SQLException, Exception {
		Connection        	conn        = null;
		PreparedStatement 	pstmt       = null;
		ResultSet         	rs          = null;
		StringBuffer      	strQuery    = new StringBuffer();
		ArrayList<HashMap<String, String>>  rsval = new ArrayList<HashMap<String, String>>();
		HashMap<String, String>			  	rst	  = null;
		
		ConnectionContext connectionContext = new ConnectionResource();
		try {
			conn = connectionContext.getConnection(); 
			strQuery.setLength(0);
	        strQuery.append("select b.cm_userid,b.cm_username                           \n");
	        strQuery.append("  from cmd0304 a,cmm0040 b                                 \n");
	        strQuery.append(" where a.cd_prjno=? and a.cd_closedt is null               \n");
	        strQuery.append("  and a.cd_prjuser=b.cm_userid and b.cm_active='1'         \n");
	        pstmt = conn.prepareStatement(strQuery.toString());
	        
            //pstmt =  new LoggableStatement(conn, strQuery.toString());
            pstmt.setString(1, PrjNo);
           	////ecamsLogger.error(((LoggableStatement)pstmt).getQueryString());
            rs = pstmt.executeQuery();
            rsval.clear();            
            while (rs.next()){
            	if (rs.getRow() == 1) {
            		rst = new HashMap<String, String>();
                	rst.put("cm_userid", "00000000");
                	rst.put("cm_username", "선택하세요.");
                	rsval.add(rst);
                	rst = null;
            	}
            	rst = new HashMap<String, String>();
            	rst.put("cm_userid", rs.getString("cm_userid"));
            	rst.put("cm_username", rs.getString("cm_username"));
            	rsval.add(rst);
            	rst = null;
			}
            rs.close();
            pstmt.close();
            conn.close();
			rs = null;
			pstmt = null;
			conn = null;
    		return rsval.toArray();
            
		} catch (SQLException sqlexception) {
			sqlexception.printStackTrace();
			ecamsLogger.error("## Cmd1500.getEditor() SQLException START ##");
			ecamsLogger.error("## Error DESC : ", sqlexception);	
			ecamsLogger.error("## Cmd1500.getEditor() SQLException END ##");			
			throw sqlexception;
		} catch (Exception exception) {
			exception.printStackTrace();
			ecamsLogger.error("## Cmd1500.getEditor() Exception START ##");				
			ecamsLogger.error("## Error DESC : ", exception);	
			ecamsLogger.error("## Cmd1500.getEditor() Exception END ##");				
			throw exception;
		}finally{
			if (strQuery != null)	strQuery = null;
			if (rsval != null)	rsval = null;
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
	}//end of getEditor() method statement       
    
    public int cmr0030Updt(String DocId,String Editor,String ccbYn,String docSta,String UserId) throws SQLException, Exception{
		Connection        conn        = null;
		PreparedStatement pstmt       = null;
		StringBuffer      strQuery    = new StringBuffer();		
		ConnectionContext connectionContext = new ConnectionResource();
		int parmCnt = 0;
		try {
			conn = connectionContext.getConnection();
			
			if (ccbYn.equals("true")) ccbYn = "Y";
			else ccbYn = "N";
			
            strQuery.setLength(0);
            strQuery.append("UPDATE CMR0030 SET                 \n");
           	strQuery.append("         CR_CCBYN=?,               \n");
            strQuery.append("         CR_LASTDT=SYSDATE         \n");
            if (Editor != null && Editor != "" && Editor.length()>0)
            	strQuery.append("         ,CR_EDITOR=?          \n");//UserId
            if (docSta != null && docSta != "" && docSta.length()>0) {
            	strQuery.append(",CR_DOCSTA=?,CR_STAUSER=?,CR_STADATE=SYSDATE \n");
            }
            strQuery.append(" WHERE   CR_DOCID=?                \n");//docid
        	pstmt = conn.prepareStatement(strQuery.toString());
        	//pstmt =  new LoggableStatement(conn, strQuery.toString());
           	
		    pstmt.setString(++parmCnt, ccbYn);
		    if (Editor != null && Editor != "" && Editor.length()>0)
		    	pstmt.setString(++parmCnt, Editor);
		    if (docSta != null && docSta != "" && docSta.length()>0) {
		    	pstmt.setString(++parmCnt, docSta.replace("0", ""));
		    	pstmt.setString(++parmCnt, UserId);
		    }
		    pstmt.setString(++parmCnt, DocId);
		    ////ecamsLogger.error(((LoggableStatement)pstmt).getQueryString());
        	pstmt.executeUpdate();
        	pstmt.close();
        	
        	if (Editor != null && Editor != "" && Editor.length()>0) {
	            strQuery.setLength(0);
	            strQuery.append("UPDATE CMR1100 SET                \n");
	            strQuery.append("       CR_EDITOR=?                \n");//UserId
	            strQuery.append(" WHERE CR_DOCID=?                 \n");//docid
	            strQuery.append("   AND substr(CR_acptno,5,2)='31' \n");//docid
	            strQuery.append("   AND cr_confno is null          \n");//docid
	        	pstmt = conn.prepareStatement(strQuery.toString());
			    pstmt.setString(1, Editor);
			    pstmt.setString(2, DocId);
	        	pstmt.executeUpdate();
	        	pstmt.close();
        	}
        	
        	conn.commit();
        	conn.close();

			pstmt = null;
			conn = null;
        	return 0;
        	
		} catch (SQLException sqlexception) {
			sqlexception.printStackTrace();
			ecamsLogger.error("## Cmd1500.cmr0030Updt() SQLException START ##");
			ecamsLogger.error("## Error DESC : ", sqlexception);	
			ecamsLogger.error("## Cmd1500.cmr0030Updt() SQLException END ##");			
			throw sqlexception;
		} catch (Exception exception) {
			exception.printStackTrace();
			ecamsLogger.error("## Cmd1500.cmr0030Updt() Exception START ##");				
			ecamsLogger.error("## Error DESC : ", exception);	
			ecamsLogger.error("## Cmd1500.cmr0030Updt() Exception END ##");				
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
	}//end of cmr0030Updt() method statement
    public int cmr0030Dlet(String DocId) throws SQLException, Exception{
		Connection        conn        = null;
		PreparedStatement pstmt       = null;
		StringBuffer      strQuery    = new StringBuffer();		
		ConnectionContext connectionContext = new ConnectionResource();
		try {
			conn = connectionContext.getConnection();

            strQuery.setLength(0);
            strQuery.append("delete CMR1100 where cr_docid=?    \n");
        	pstmt = conn.prepareStatement(strQuery.toString());
		    pstmt.setString(1, DocId);
        	pstmt.executeUpdate();
        	pstmt.close();
        	
            strQuery.setLength(0);
            strQuery.append("delete CMR0034 where cr_docid=?    \n");
        	pstmt = conn.prepareStatement(strQuery.toString());
		    pstmt.setString(1, DocId);
        	pstmt.executeUpdate();
        	pstmt.close();
        	
            strQuery.setLength(0);
            strQuery.append("delete CMR0033 where cr_docid=?    \n");
        	pstmt = conn.prepareStatement(strQuery.toString());
		    pstmt.setString(1, DocId);
        	pstmt.executeUpdate();
        	pstmt.close();
        	
            strQuery.setLength(0);
            strQuery.append("delete CMR0032 where cr_docid=?    \n");
        	pstmt = conn.prepareStatement(strQuery.toString());
		    pstmt.setString(1, DocId);
        	pstmt.executeUpdate();
        	pstmt.close();
        	
            strQuery.setLength(0);
            strQuery.append("delete CMR0031 where cr_docid=?    \n");
        	pstmt = conn.prepareStatement(strQuery.toString());
		    pstmt.setString(1, DocId);
        	pstmt.executeUpdate();
        	pstmt.close();
        	
            strQuery.setLength(0);
            strQuery.append("delete CMR0030 where cr_docid=?    \n");
        	pstmt = conn.prepareStatement(strQuery.toString());
		    pstmt.setString(1, DocId);
        	pstmt.executeUpdate();
        	pstmt.close();
        	conn.commit();
        	conn.close();

			pstmt = null;
			conn = null;
        	return 0;

		} catch (SQLException sqlexception) {
			sqlexception.printStackTrace();
			ecamsLogger.error("## Cmd1500.cmr0030Dlet() SQLException START ##");
			ecamsLogger.error("## Error DESC : ", sqlexception);	
			ecamsLogger.error("## Cmd1500.cmr0030Dlet() SQLException END ##");			
			throw sqlexception;
		} catch (Exception exception) {
			exception.printStackTrace();
			ecamsLogger.error("## Cmd1500.cmr0030Dlet() Exception START ##");				
			ecamsLogger.error("## Error DESC : ", exception);	
			ecamsLogger.error("## Cmd1500.cmr0030Dlet() Exception END ##");				
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
	}//end of cmr0030Dlet() method statement
    
}//end of Cmd1500 class statement
