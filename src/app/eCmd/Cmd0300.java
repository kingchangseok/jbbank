
/*****************************************************************************************
	1. program ID	: eCmd0300.java
	2. create date	: 2006.08. 08
	3. auth		    : teok.kang
	4. update date	: 
	5. auth		    : 
	6. description	: eCmd0300 [문서관리]->[프로젝트등록]
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
import app.common.UserInfo;


/**
 * @author bigeyes
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */


public class Cmd0300{
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


	public Object[] getMKDIR(String PrjNo) throws SQLException, Exception{
		Connection        conn        = null;
		PreparedStatement pstmt       = null;
		ResultSet         rs          = null;
		StringBuffer      strQuery    = new StringBuffer();
		ArrayList<HashMap<String, String>>  rsval = new ArrayList<HashMap<String, String>>();
		HashMap<String, String>			  	rst	  = null;		
		
		ConnectionContext connectionContext = new ConnectionResource();
		try {
			conn = connectionContext.getConnection();
			
			strQuery.append("select a.cd_prjname,c.cm_codename from cmd0300 a, cmd0301 b, cmm0020 c ");
			strQuery.append("where a.cd_prjno  = ? ");//PrjNo
			strQuery.append("  and a.cd_prjno = b.cd_prjno ");
			strQuery.append("  and c.cm_macode='DEVSTEP' ");
			strQuery.append("  and b.cd_devstep = c.cm_micode ");
			strQuery.append("order by b.cd_seqno ");
            pstmt = conn.prepareStatement(strQuery.toString());
           	pstmt.setString(1, PrjNo);
            rs = pstmt.executeQuery();

			while (rs.next()){
				if (rs.getRow() == 1){
					rst = new HashMap<String, String>();
					rst.put("cd_prjname", rs.getString("cd_prjname"));
					rst.put("cm_codename", "");
					rst.put("Sv_LocalF", rs.getString("cd_prjname"));
					rsval.add(rst);
				}
				
				rst = new HashMap<String, String>();
				rst.put("cd_prjname", rs.getString("cd_prjname"));
				rst.put("cm_codename", rs.getString("cm_codename"));
				rst.put("Sv_LocalF", rs.getString("cd_prjname")+"\\\\"+rs.getString("cm_codename"));
	           	rsval.add(rst);
	           	rst = null;
			}//end of while-loop statement
			rs.close();
			pstmt.close();
			conn.close();
			
			rs = null;
			pstmt = null;
			conn = null;
			
			return rsval.toArray();
			
		} catch (SQLException sqlexception) {
			sqlexception.printStackTrace();
			ecamsLogger.error("## Cmd0300.getMKDIR() SQLException START ##");
			ecamsLogger.error("## Error DESC : ", sqlexception);
			ecamsLogger.error("## Cmd0300.getMKDIR() SQLException END ##");
			throw sqlexception;
		} catch (Exception exception) {
			exception.printStackTrace();
			ecamsLogger.error("## Cmd0300.getMKDIR() Exception START ##");
			ecamsLogger.error("## Error DESC : ", exception);
			ecamsLogger.error("## Cmd0300.getMKDIR() Exception END ##");
			throw exception;
		}finally{
			if (strQuery != null)	strQuery = null;
			if (rsval != null)  	rsval = null;
			if (rs != null)     try{rs.close();}catch (Exception ex){ex.printStackTrace();}
			if (pstmt != null)  try{pstmt.close();}catch (Exception ex2){ex2.printStackTrace();}
			if (conn != null){
				try{
					ConnectionResource.release(conn);
				}catch(Exception ex3){
					ecamsLogger.error("## Cmd0300.getMKDIR() connection release exception ##");
					ex3.printStackTrace();
				}
			}
		}
	}

	public Object[] METHCD_Set(String PrjNo) throws SQLException, Exception {
		Connection        conn        = null;
		PreparedStatement pstmt       = null;
		ResultSet         rs          = null;
		PreparedStatement pstmt2      = null;
		ResultSet         rs2         = null;
		StringBuffer      strQuery    = new StringBuffer();
		ArrayList<HashMap<String, String>>  rsval = new ArrayList<HashMap<String, String>>();
		HashMap<String, String>			  rst		  = null;
		
		ConnectionContext connectionContext = new ConnectionResource();
		try {
			conn = connectionContext.getConnection();
			strQuery.setLength(0);
           	strQuery.append("select a.CD_PRJNO,a.CD_PRJNAME,a.CD_STATUS,b.CD_METHCD, ");
           	strQuery.append("B.CD_DEVSTEP,B.CD_SEQNO,c.cm_codename ");
           	strQuery.append("FROM cmd0300 a,cmd0301 b, cmm0020 c ");
           	strQuery.append(" WHERE a.CD_PRJNO = ? AND a.CD_PRJNO = b.CD_PRJNO ");		//PrjNo
           	strQuery.append("   AND c.CM_MACODE = 'DEVSTEP' AND b.CD_DEVSTEP = c.CM_MICODE ");
           	strQuery.append(" order by B.CD_SEQNO ");
            pstmt = conn.prepareStatement(strQuery.toString());
           	pstmt.setString(1, PrjNo);
            rs = pstmt.executeQuery();
            
            String MICODE = null;
			while(rs.next()){
				if (rs.getRow()==1){
					rst = new HashMap<String, String>();
					rst.put("ID", "Check_MethCd");
					rst.put("CD_METHCD", rs.getString("CD_METHCD"));
					rsval.add(rst);
					rst = null;
				}
				
				rst = new HashMap<String, String>();
				rst.put("cm_macode", "DEVSTEP");
				rst.put("cm_micode", rs.getString("CD_DEVSTEP"));
				rst.put("cm_codename", rs.getString("cm_codename"));
				rst.put("checkbox", "true");				
				strQuery.setLength(0);
				strQuery.append("SELECT count(*) as cnt FROM CMR0030 "); 
				strQuery.append("WHERE cr_prjno=? ");
				strQuery.append("AND cr_devstep=? ");
	            pstmt2 = conn.prepareStatement(strQuery.toString());	            
	           	pstmt2.setString(1, PrjNo);
	           	pstmt2.setString(2, rs.getString("CD_DEVSTEP"));
	            rs2 = pstmt2.executeQuery();
	            if (rs2.next()){
	            	if (rs2.getString("CNT").equals("0")) 
	            		rst.put("docFileYN", "N");
	            	else 
	            		rst.put("docFileYN", "Y");
	            }
				if (MICODE == null)
					MICODE = rs.getString("CD_DEVSTEP");
				else MICODE = MICODE + "," + rs.getString("CD_DEVSTEP");
				
	            rs2.close();
	            pstmt2.close();
				rsval.add(rst);
				rst = null;
			}
			rs.close();
			pstmt.close();
			
			if (MICODE != "" && MICODE != null){
				String[] micode = MICODE.split(",");
				strQuery.setLength(0);
				strQuery.append("select cm_macode,cm_micode,cm_codename from cmm0020 ");
				strQuery.append("where cm_macode='DEVSTEP' and cm_micode <> '****' ");
				strQuery.append("  and cm_closedt is null ");
				strQuery.append("  and cm_micode not in ( ");
				if (micode.length == 1)
					strQuery.append(" ? ");
				else{
					for (int i=0;i<micode.length;i++){
						if (i == micode.length-1)
							strQuery.append(" ? ");
						else
							strQuery.append(" ? ,");
					}
				}
				strQuery.append(" ) ");
				strQuery.append("order by cm_micode ");
	            pstmt = conn.prepareStatement(strQuery.toString());
				for (int i=0;i<micode.length;i++){
					pstmt.setString(i+1, micode[i]);
				}
			}else{
				strQuery.setLength(0);
				strQuery.append("select cm_macode,cm_micode,cm_codename from cmm0020 ");
				strQuery.append("where cm_macode='DEVSTEP' and cm_micode <> '****' ");
				strQuery.append("  and cm_closedt is null ");
				strQuery.append("order by cm_micode ");
				pstmt = conn.prepareStatement(strQuery.toString());
			}
			rs = pstmt.executeQuery();
			while (rs.next()){
				rst = new HashMap<String, String>();
				rst.put("cm_codename", rs.getString("cm_codename"));
				rst.put("cm_micode", rs.getString("cm_micode"));
				rst.put("cm_macode", rs.getString("cm_macode"));
				rst.put("docFileYN", "N");
				rst.put("checkbox", "");
				rsval.add(rst);
				rst = null;
			}
			rs.close();
			pstmt.close();
			conn.close();
			
			rs = null;
			pstmt = null;
			rs2 = null;
			pstmt2 = null;
			conn = null;
			return rsval.toArray();
			
		} catch (SQLException sqlexception) {
			sqlexception.printStackTrace();
			ecamsLogger.error("## Cmd0300.METHCD_Set() SQLException START ##");
			ecamsLogger.error("## Error DESC : ", sqlexception);	
			ecamsLogger.error("## Cmd0300.METHCD_Set() SQLException END ##");			
			throw sqlexception;
		} catch (Exception exception) {
			exception.printStackTrace();
			ecamsLogger.error("## Cmd0300.METHCD_Set() Exception START ##");				
			ecamsLogger.error("## Error DESC : ", exception);	
			ecamsLogger.error("## Cmd0300.METHCD_Set() Exception END ##");				
			throw exception;
		}finally{
			if (strQuery != null)	strQuery = null;
			if (rsval != null)  	rsval = null;
			if (rs != null)     try{rs.close();}catch (Exception ex){ex.printStackTrace();}
			if (rs2 != null)     try{rs2.close();}catch (Exception ex1){ex1.printStackTrace();}
			if (pstmt != null)  try{pstmt.close();}catch (Exception ex2){ex2.printStackTrace();}
			if (pstmt2 != null)  try{pstmt2.close();}catch (Exception ex3){ex3.printStackTrace();}
			if (conn != null){
				try{
					ConnectionResource.release(conn);
				}catch(Exception ex3){
					ecamsLogger.error("## Cmd0300.METHCD_Set() connection release exception ##");
					ex3.printStackTrace();
				}
			}
		}
	}//end of METHCD_Set() method statement
		
	//Make PrjNo
	/*
	public String PrjNo_make(Connection pconn) throws SQLException, Exception{
		PreparedStatement pstmt       = null;
		StringBuffer      strQuery    = new StringBuffer();
		ResultSet         rs          = null;
		String 		      PrjNo		  = null;
		int               i = 0;
		String            tmpSeq      = "";
		try {
			//프로젝트번호
			//P + YYYYMMDD + '-' + SEQ3자리
			strQuery.setLength(0);
	        strQuery.append("select nvl(max(cd_prjno),'P' || to_char(SYSDATE,'yyyymmdd') || '-000') prjno \n");
	        strQuery.append("  from cmd0300 \n");
	        strQuery.append("where substr(cd_prjno,2,8)=to_char(sysdate,'yyyymmdd') \n");
	        
			pstmt = pconn.prepareStatement(strQuery.toString());	
            rs = pstmt.executeQuery();
			if (rs.next()){
				i = Integer.parseInt(rs.getString("prjno").substring(10,13))+1;
				PrjNo = rs.getString("prjno").substring(0, 10) + String.format("%03d", i);;
			}
			
			rs.close();
			pstmt.close();
			return PrjNo;
			
		} catch (SQLException sqlexception) {
			sqlexception.printStackTrace();
			ecamsLogger.error("## Cmd0300.PrjNo_make() SQLException START ##");
			ecamsLogger.error("## Error DESC : ", sqlexception);	
			ecamsLogger.error("## Cmd0300.PrjNo_make() SQLException END ##");			
			throw sqlexception;
		} catch (Exception exception) {
			exception.printStackTrace();
			ecamsLogger.error("## Cmd0300.PrjNo_make() Exception START ##");				
			ecamsLogger.error("## Error DESC : ", exception);	
			ecamsLogger.error("## Cmd0300.PrjNo_make() Exception END ##");				
			throw exception;
		}
	}//end of PrjNo_make(String) method statement
	*/
	
	//Make PrjNo
	public String PrjNo_make(String PrjNo) throws SQLException, Exception{
		Connection        conn        = null;
		PreparedStatement pstmt       = null;
		StringBuffer      strQuery    = new StringBuffer();
		ResultSet         rs          = null;
		ConnectionContext connectionContext = new ConnectionResource();
		String 		      rPrjNo	  = null;

		try {
			conn = connectionContext.getConnection();
			//프로젝트번호
			strQuery.setLength(0);
	        strQuery.append("with tmp as                                                            \n");                                                 
	        strQuery.append("(select case when nvl(max(substr(cd_prjno,16,2)), 'x') = 'x'           \n");                                                 
	        strQuery.append("        then 1                                                         \n");                                                 
	        strQuery.append("        else to_number(max(substr(cd_prjno,16,2))) + 1 end as maxprjno \n");                                                 
	        strQuery.append(" from  cmd0300                                                         \n");                                                 
	        strQuery.append(" where substr(cd_prjno,1,15) = substr(?,1,15))                         \n");                                              
	        strQuery.append(" select lpad(maxprjno, 2, '0') seqno  from tmp                         \n"); 
	        
	        pstmt = conn.prepareStatement(strQuery.toString());
           	
	        pstmt.setString(1, PrjNo);
            rs = pstmt.executeQuery();
			
            if (rs.next()){
				rPrjNo = PrjNo + rs.getString("seqno") ;
			}
			
			rs.close();
			pstmt.close();
			conn.close();
			
			rs = null;
			pstmt = null;
			conn = null;
			
			return rPrjNo;
			
		} catch (SQLException sqlexception) {
			sqlexception.printStackTrace();
			ecamsLogger.error("## Cmd0300.PrjNo_make() SQLException START ##");
			ecamsLogger.error("## Error DESC : ", sqlexception);	
			ecamsLogger.error("## Cmd0300.PrjNo_make() SQLException END ##");			
			throw sqlexception;
		} catch (Exception exception) {
			exception.printStackTrace();
			ecamsLogger.error("## Cmd0300.PrjNo_make() Exception START ##");				
			ecamsLogger.error("## Error DESC : ", exception);	
			ecamsLogger.error("## Cmd0300.PrjNo_make() Exception END ##");				
			throw exception;
		} finally{
			if (strQuery != null)	strQuery = null;
			if (rs != null)     try{rs.close();}catch (Exception ex){ex.printStackTrace();}
			if (pstmt != null)  try{pstmt.close();}catch (Exception ex2){ex2.printStackTrace();}
			if (conn != null){
				try{
					ConnectionResource.release(conn);
				}catch(Exception ex3){
					ecamsLogger.error("## Cmd0300.PrjNo_make() connection release exception ##");
					ex3.printStackTrace();
				}
			}
		}
	}//end of PrjNo_make(String) method statement
	

	
	
	//INSERT
	public String Cmd0300_INSERT(HashMap<String,String> dataObj,
			//ArrayList<HashMap<String,String>> DEVSTEP,
			//ArrayList<HashMap<String,String>> UnDEVSTEP,			
			ArrayList<HashMap<String,String>> PRJUSER) throws SQLException, Exception{
		Connection        conn        = null;
		PreparedStatement pstmt       = null;
		PreparedStatement pstmt2      = null;
		ResultSet         rs          = null;
		ResultSet         rs2          = null;
		StringBuffer      strQuery    = new StringBuffer();
		String            TKey        = "";
		String            UpKey       = "";
		String			  PrjNo       = "";
		String			  UserId      = "";
		String			  PrjName     = "";
		String			  MethCd      = "";
		
		ConnectionContext connectionContext = new ConnectionResource();

		try {
			conn = connectionContext.getConnection();
			conn.setAutoCommit(false);
			UserId      = dataObj.get("UserId");
			PrjNo       = dataObj.get("PrjNo");
			PrjName     = dataObj.get("PrjName");
			MethCd      = dataObj.get("MethCd");
			
			strQuery.setLength(0);
			//if (dataObj.get("PrjNo").equals("00000")){
			if (dataObj.get("NewFlag").equals("Y")){				

				strQuery.append("select cd_prjname from cmd0300 ");
				strQuery.append("Where cd_prjname = ? ");//PrjName
				strQuery.append("  and substr(cd_prjno,1,15)=? ");//PrjName
	            pstmt = conn.prepareStatement(strQuery.toString());
	            pstmt.setString(1, PrjName);
	            pstmt.setString(2, PrjNo);
	            rs = pstmt.executeQuery();
				if (rs.next()){
					//throw new Exception("["+PrjName+"] 프로젝트가 이미 존재합니다.");
					return "0000";
				}
				rs.close();
				pstmt.close();
				// 번호 자동 생성이 아니라 입력 받는 것으로 변경 (PMS와 맞추기 위함)
				PrjNo = PrjNo_make(PrjNo);
				
				strQuery.setLength(0);
				strQuery.append("INSERT INTO CMD0300 (CD_PRJNO,CD_PRJNAME,CD_CREATDT, \n");
				strQuery.append("CD_LASTDT,CD_STATUS,CD_EDITOR,CD_CREATOR,CD_JOBUSEYN,\n");
				strQuery.append("CD_METHCD) values ( \n");
				strQuery.append("?, \n");				//CD_PRJNO
				strQuery.append("?, \n"); 				//CD_PRJNAME
				strQuery.append("SYSDATE,   \n");       
				strQuery.append("SYSDATE,   \n");       
				strQuery.append("'0', \n");             
				strQuery.append("?, ?, \n");      		//CD_EDITOR, CD_CREATOR
				strQuery.append("'N', ?) \n");
				//pstmt = new LoggableStatement(conn,strQuery.toString());
				pstmt = conn.prepareStatement(strQuery.toString());				
            	pstmt.setString(1, PrjNo);
                pstmt.setString(2, PrjName);
                pstmt.setString(3, UserId);
                pstmt.setString(4, UserId);
                pstmt.setString(5, MethCd);
                //ecamsLogger.error("1 => " + new String((((LoggableStatement)pstmt).getQueryString()).getBytes("MS949")));
                pstmt.executeUpdate();
                pstmt.close();
			}else{
				PrjNo = dataObj.get("PrjNo");
				strQuery.setLength(0);
				strQuery.append("UPDATE CMD0300 SET \n");
				strQuery.append("CD_PRJNAME = ?, \n"); 			//CD_PRJNAME
				strQuery.append("CD_METHCD = ?,  \n"); 			//CD_PRJNAME
				strQuery.append("CD_LASTDT = SYSDATE,   \n");   
				strQuery.append("CD_EDITOR = ? \n");        	//CD_EDITOR
				strQuery.append("Where CD_PRJNO = ? \n");     	//CD_PRJNO
				
				//pstmt = new LoggableStatement(conn,strQuery.toString());
				pstmt = conn.prepareStatement(strQuery.toString());
				
                pstmt.setString(1, PrjName);
                pstmt.setString(2, MethCd);
                pstmt.setString(3, UserId);
                pstmt.setString(4, PrjNo);
                //ecamsLogger.error("2 => " + new String((((LoggableStatement)pstmt).getQueryString()).getBytes("MS949")));
                pstmt.executeUpdate();
                pstmt.close();
			}
			
		    //CMD0301_INSERT start
			strQuery.setLength(0);
			strQuery.append("update cmd0301 set cd_closedt=sysdate \n");
			strQuery.append("where cd_prjno=? \n");  //PrjNo
			pstmt = conn.prepareStatement(strQuery.toString());
		    pstmt.setString(1, PrjNo);
		    pstmt.executeUpdate();
		    pstmt.close();
		    
		    int Cnt = 0;
		    /*
			for (int i=0 ; i<DEVSTEP.size() ; i++){					
				strQuery.setLength(0);
				strQuery.append("select count(*) as cnt from CMD0301 \n");
				strQuery.append("Where CD_PRJNO = ? \n");		//PrjNo
				strQuery.append("  AND CD_DEVSTEP = ? \n");		//DEVSTEP
	        	pstmt = conn.prepareStatement(strQuery.toString());
			    pstmt.setString(1, PrjNo);
			    pstmt.setString(2, DEVSTEP.get(i).get("cm_micode"));
	        	rs = pstmt.executeQuery();
	        	strQuery.setLength(0);
	        	if (rs.next()){
	        		if (rs.getString("cnt").equals("0")){
	            		strQuery.append("INSERT INTO CMD0301 (CD_METHCD,CD_EDITOR,CD_SEQNO, ");
	            		strQuery.append("CD_PRJNO,CD_DEVSTEP,CD_CREATDT,CD_LASTDT) values ( ");
	            		strQuery.append("?, ");      					//METHCD
	            		strQuery.append("?, ");							//UserID
	            		strQuery.append("?, ");							//SEQNO
	            		strQuery.append("?, ");     					//PrjNo
	            		strQuery.append("?, ");         				//DEVSTEP
	            		strQuery.append("SYSDATE, ");
	            		strQuery.append("SYSDATE) ");
	        		}else{
	            		strQuery.append("UPDATE CMD0301 SET ");
	            		strQuery.append("CD_LASTDT=SYSDATE,CD_CLOSEDT='',");
	            		strQuery.append("CD_METHCD = ?, "); 			//METHCD
	            		strQuery.append("CD_EDITOR=?, ");               //UserID
	            		strQuery.append("CD_SEQNO=? ");					//SEQNO
	            		strQuery.append("Where CD_PRJNO=? ");           //PrjNo
	            		strQuery.append("  AND CD_DEVSTEP=? ");     	//DEVSTEP    
	        		}
	        		pstmt2 = conn.prepareStatement(strQuery.toString());	    			
	        		pstmt2.setString(1, MethCd);
	        		pstmt2.setString(2, UserId);
	        		pstmt2.setInt(3, ++Cnt);
	        		pstmt2.setString(4, PrjNo);
	        		pstmt2.setString(5, DEVSTEP.get(i).get("cm_micode"));		    		    
	        		pstmt2.executeUpdate();
	        		pstmt2.close();
	        	}
	        	rs.close();
	        	pstmt.close();
			}
			*/
			strQuery.setLength(0);			
			strQuery.append("DELETE CMD0301 \n");
			strQuery.append("Where CD_PRJNO = ? \n"); //CD_PRJNO
			strQuery.append("  AND CD_CLOSEDT IS NOT NULL \n");
			pstmt = conn.prepareStatement(strQuery.toString());
		    pstmt.setString(1, PrjNo);
		    pstmt.executeUpdate();
		    pstmt.close();
		    //////////////////////////
		    //  CMD0301_INSERT end  //
		    //////////////////////////
		    
		    
		    ///////////////////////////
		    //  CMD0303_INSERT Start //
		    ///////////////////////////
		    strQuery.setLength(0);
        	strQuery.append("select count(*) as cnt from cmd0303 \n");
        	strQuery.append("where cd_prjno=? \n");  	//CD_PRJNO
        	pstmt = conn.prepareStatement(strQuery.toString());
		    pstmt.setString(1, PrjNo);
        	rs = pstmt.executeQuery();
        	strQuery.setLength(0);
        	if (rs.next()){
        		if (rs.getString("cnt").equals("0")) {
            		strQuery.append("insert into cmd0303 (cd_dirname,cd_prjno,cd_docseq,cd_dirpath) \n");
            		strQuery.append("values (?,?,'00001',?) \n");			//PRJNAME,PRJNO
            		pstmt2 = conn.prepareStatement(strQuery.toString());
            		pstmt2.setString(1, PrjName);
            		pstmt2.setString(2, PrjNo);
            		pstmt2.setString(3, PrjName);
            		pstmt2.executeUpdate();
            		pstmt2.close();
        		}else{
	        		strQuery.append("update cmd0303 set cd_dirname=?,cd_dirpath=? \n");	//PRJNAME
	        		strQuery.append("where cd_prjno=? \n");					//PRJNO
	        		strQuery.append("  and CD_DOCSEQ='00001' \n");
	        		pstmt2 = conn.prepareStatement(strQuery.toString());
	        		pstmt2.setString(1, PrjName);
	        		pstmt2.setString(2, PrjName);
	        		pstmt2.setString(3, PrjNo);
	        		pstmt2.executeUpdate();
	        		pstmt2.close();
        		}
        	}
        	rs.close();
        	pstmt.close();
        	
        	strQuery.setLength(0);
        	strQuery.append("select count(*) as cnt from cmd0305 \n");
        	strQuery.append("where cd_prjno=? 		\n");
        	strQuery.append("  and cd_dsncd='00001' \n");
        	pstmt = conn.prepareStatement(strQuery.toString());
		    pstmt.setString(1, PrjNo);
        	rs = pstmt.executeQuery();
        	strQuery.setLength(0);
        	if (rs.next()){
        		if (rs.getInt("cnt") > 0) {
	                strQuery.append("update cmd0305 set cd_dirpath=?, \n");//Txt_PrjName
					strQuery.append("cd_editor=?, 		\n"); //SV_UserID
					strQuery.append("cd_lastupdt=SYSDATE,cd_clsdt='' \n");
					strQuery.append("where cd_prjno=? 	\n");//Txt_PrjNo
					strQuery.append("  AND CD_Dsncd='00001' \n");
        		}else{
	                strQuery.append("insert into cmd0305 (CD_DIRPATH,CD_EDITOR,CD_PRJNO,CD_DSNCD,CD_OPENDT,CD_LASTUPDT) values (\n");
	                strQuery.append("?,?,?, \n");   //Txt_PrjName SV_UserID Txt_PrjNo
	                strQuery.append("'00001', \n");
	                strQuery.append("SYSDATE,SYSDATE) \n");
        		}
        		pstmt2 = conn.prepareStatement(strQuery.toString());
        		pstmt2.setString(1, PrjName);
        		pstmt2.setString(2, UserId);
        		pstmt2.setString(3, PrjNo);
        		pstmt2.executeUpdate();
        		pstmt2.close();
        	}
        	rs.close();
        	pstmt.close();
        	 /* 
			for (int i=0 ; i<DEVSTEP.size() ; i++){
				strQuery.setLength(0);
	        	strQuery.append("select cd_docseq from cmd0303 \n");
	        	strQuery.append("where cd_prjno=? \n");//PrjNo
	        	strQuery.append("  and CD_UPDOCSEQ='00001' \n");
	        	strQuery.append("  and CD_DEVSTEP=? \n");//DevStep
				pstmt = conn.prepareStatement(strQuery.toString());
			    pstmt.setString(1, PrjNo);
			    pstmt.setString(2, DEVSTEP.get(i).get("cm_micode"));
			    rs = pstmt.executeQuery();
			    strQuery.setLength(0);
			    if (rs.next()){
			    	TKey = rs.getString("cd_docseq");			    	
		    		strQuery.append("UPDATE CMD0303 SET CD_DIRNAME=?,CD_DIRPATH=? \n");	//DevStepName
            		strQuery.append("where CD_PRJNO=? \n");					//PrjNo
            		strQuery.append("  and CD_DOCSEQ=? \n");				//TKey
            		pstmt2 = conn.prepareStatement(strQuery.toString());
            		pstmt2.setString(1, DEVSTEP.get(i).get("cm_codename"));
            		pstmt2.setString(2, PrjName + "\\" + DEVSTEP.get(i).get("cm_codename"));
            		pstmt2.setString(3, PrjNo);
            		pstmt2.setString(4, TKey);
            		pstmt2.executeUpdate();
            		pstmt2.close();
			    }else{
		    		TKey = "00001";
		    		UpKey = "00001";
		    		strQuery.append("select max(cd_docseq) max from cmd0303 \n");
	                strQuery.append("where cd_prjno=? \n");//PrjNo
	                pstmt2 = conn.prepareStatement(strQuery.toString());
	                pstmt2.setString(1, PrjNo);
				    rs2 = pstmt2.executeQuery();
				    if (rs2.next()){
						int j = Integer.parseInt(rs2.getString("max"))+1;
						
				    	TKey = "00000".substring(0,5-Integer.toString(j).length()) + 
				    		Integer.toString(j);
				    }
				    rs2.close();
				    pstmt2.close();
				    
				    strQuery.setLength(0);
				    strQuery.append("INSERT into cmd0303 (CD_PRJNO,CD_DOCSEQ,CD_DIRNAME,\n");
				    strQuery.append("CD_DEVSTEP,CD_UPDOCSEQ,CD_DIRPATH) values (\n");
                	strQuery.append("?, \n");						//PrjNo
                	strQuery.append("?, \n");						//TKey
                	strQuery.append("?, \n");						//DevStepName
                	strQuery.append("?, \n");						//DevStep
                	strQuery.append("?, \n");						//UpKey
                	strQuery.append("?) \n");						//FULLPATH
                	pstmt2 = conn.prepareStatement(strQuery.toString());
                	pstmt2.setString(1, PrjNo);
                	pstmt2.setString(2, TKey);
                	pstmt2.setString(3, DEVSTEP.get(i).get("cm_codename"));                	
                	pstmt2.setString(4, DEVSTEP.get(i).get("cm_micode"));
                	pstmt2.setString(5, UpKey);
                	pstmt2.setString(6, PrjName + "\\" + DEVSTEP.get(i).get("cm_codename"));
                	pstmt2.executeUpdate();
                	pstmt2.close();
			    }
			    rs.close();
			    pstmt.close();
			    
			    strQuery.setLength(0);
			    strQuery.append("select COUNT(*) AS cnt from cmd0305 \n");
			    strQuery.append("where cd_prjno=? 	\n"); //PrjNo
			    strQuery.append("  and CD_dsncd=?	\n"); //TKey
				pstmt = conn.prepareStatement(strQuery.toString());
			    pstmt.setString(1, PrjNo);
			    pstmt.setString(2, TKey);
			    rs = pstmt.executeQuery();
			    strQuery.setLength(0);
			    if (rs.next()){
			    	if (rs.getInt("cnt") == 0){
				    	strQuery.append("insert into cmd0305 (CD_DIRPATH,CD_EDITOR,CD_PRJNO,");
				    	strQuery.append("CD_DSNCD,CD_OPENDT,CD_LASTUPDT) values ( \n");
				    	strQuery.append("?, \n");	//PrjName "/" Lst_DevStep
				    	strQuery.append("?, \n");  	//UserId				    	
				    	strQuery.append("?, \n");	//PrjNo
				    	strQuery.append("?, \n"); 	//TKey				    	
				    	strQuery.append("SYSDATE,SYSDATE) \n");	                 
			    	}else{
			    		strQuery.append("UPDATE CMD0305 SET \n");
			    		strQuery.append(" CD_DIRPATH=?, \n");  	//PrjName "/" Lst_DevStep
			    		strQuery.append("  CD_EDITOR=?, \n");  	//UserId
			    		strQuery.append("CD_LASTUPDT=SYSDATE,cd_clsdt='' \n");
			    		strQuery.append("where CD_PRJNO=? \n");	//PrjNo
			    		strQuery.append("  and CD_DSNCD=? \n");	//TKey
			    	}
			    	pstmt2 = conn.prepareStatement(strQuery.toString());
			    	pstmt2.setString(1, PrjName + "/" + DEVSTEP.get(i).get("cm_codename"));
			    	pstmt2.setString(2, UserId);
			    	pstmt2.setString(3, PrjNo);
			    	pstmt2.setString(4, TKey);
			    	pstmt2.executeUpdate();
			        pstmt2.close();
			    }
			    rs.close();
			    pstmt.close();

			}*/
			
			
			//단계선택 안한 경우
			/*for (int i=0 ; i<UnDEVSTEP.size() ; i++){
				strQuery.setLength(0);
				strQuery.append("DELETE CMD0305 \n");
				strQuery.append("where cd_prjno=? \n");  //PrjNo
				strQuery.append("  and CD_DSNCD=(select cd_docseq from cmd0303 \n");
				strQuery.append("       where cd_prjno=? \n");  //PrjNo
				strQuery.append("         and cd_updocseq='00001' \n");
				strQuery.append("         and CD_DIRNAME=?) \n");//DevStepName
				pstmt = conn.prepareStatement(strQuery.toString());
		        pstmt.setString(1, PrjNo);
		        pstmt.setString(2, PrjNo);
		        pstmt.setString(3, UnDEVSTEP.get(i).get("cm_codename"));
		        pstmt.executeUpdate();
		        pstmt.close();
		        
		    	strQuery.setLength(0);
		    	strQuery.append("DELETE CMD0303 \n");
		    	strQuery.append("where cd_prjno=? \n");   			//PrjNo
		    	strQuery.append("  and CD_UPDOCSEQ='00001' \n");
				strQuery.append("  and CD_DIRNAME=? \n");			//DevStepName
				pstmt = conn.prepareStatement(strQuery.toString());
		        pstmt.setString(1, PrjNo);
		        pstmt.setString(2, UnDEVSTEP.get(i).get("cm_codename"));
		        pstmt.executeUpdate();
		        pstmt.close();
			}*/
			//CMD0303_INSERT  END
			conn.commit();
			conn.close();
			
			rs = null;
			pstmt = null;
			rs2 = null;
			pstmt2 = null;
			conn = null;
			
			
			Cmd0400 cmd0400 = new Cmd0400();
			cmd0400.Cmd0304_Update(PrjNo, UserId, PRJUSER,"0");
		    
		    //CMD0304_INSERT end
		    return PrjNo;
		    
		} catch (SQLException sqlexception) {
			sqlexception.printStackTrace();
			if (rs != null)     try{rs.close();}catch (Exception ex){ex.printStackTrace();}
			if (pstmt != null)  try{pstmt.close();}catch (Exception ex2){ex2.printStackTrace();}
			if (conn != null){
				try{
					conn.rollback();
					ConnectionResource.release(conn);
				}catch(Exception ex3){
					ecamsLogger.error("## Cmd0300.Cmd0300_INSERT() connection release exception ##");
					ex3.printStackTrace();
				}				
			}
			ecamsLogger.error("## Cmd0300.Cmd0300_INSERT() SQLException START ##");
			ecamsLogger.error("## Error DESC : ", sqlexception);	
			ecamsLogger.error("## Cmd0300.Cmd0300_INSERT() SQLException END ##");			
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
					ecamsLogger.error("## Cmd0300.Cmd0300_INSERT() connection release exception ##");
					ex3.printStackTrace();
				}				
			}			
			ecamsLogger.error("## Cmd0300.Cmd0300_INSERT() Exception START ##");				
			ecamsLogger.error("## Error DESC : ", exception);	
			ecamsLogger.error("## Cmd0300.Cmd0300_INSERT() Exception END ##");				
			throw exception;
		}finally{
			if (strQuery != null)	strQuery = null;
			if (rs != null)     try{rs.close();}catch (Exception ex){ex.printStackTrace();}
			if (pstmt != null)  try{pstmt.close();}catch (Exception ex2){ex2.printStackTrace();}
			if (conn != null){
				try{
					ConnectionResource.release(conn);
				}catch(Exception ex3){
					ecamsLogger.error("## Cmd0300.Cmd0300_INSERT() connection release exception ##");
					ex3.printStackTrace();
				}
			}
		}
	}//end of Cmd0300_INSERT() method statement
	
	public int Cmd0300_UPDATE(String UserId,String PrjNo,int index) throws SQLException, Exception{
		Connection        conn        = null;
		PreparedStatement pstmt       = null;
		ResultSet         rs          = null;
		StringBuffer      strQuery    = new StringBuffer();
		
		ConnectionContext connectionContext = new ConnectionResource();

		try {
			conn = connectionContext.getConnection();
			conn.setAutoCommit(false);
			
			switch(index){
			case 1://플젝 삭제처리
				strQuery.setLength(0);
				strQuery.append("Select count(*) as cnt from cmr0031 ");
				strQuery.append("where cr_prjno=? ");//PrjNo
				pstmt = conn.prepareStatement(strQuery.toString());
	            pstmt.setString(1, PrjNo);
	            rs = pstmt.executeQuery();
	            if (rs.next()){
	            	if (rs.getInt("cnt") >0){
	            		throw new Exception("등록된 산출물이 존재합니다. 프로젝트 삭제가 취소 되었습니다.");
	            	}
	            }
	            rs.close();
	            pstmt.close();
	            
	            strQuery.setLength(0);
	            strQuery.append("UPDATE CMD0300 SET ");
	            strQuery.append("CD_CLOSEDT=SYSDATE, ");
	            strQuery.append("CD_STATUS='3', ");
	            strQuery.append("CD_EDITOR =? ");
	            strQuery.append("Where CD_PRJNO =? ");
				pstmt = conn.prepareStatement(strQuery.toString());
	            pstmt.setString(1, UserId);
	            pstmt.setString(2, PrjNo);
				pstmt.executeUpdate();
				pstmt.close();
				
	            strQuery.setLength(0);
	            strQuery.append("UPDATE CMD0301 SET ");
	            strQuery.append("CD_CLOSEDT=SYSDATE, ");
	            strQuery.append("CD_EDITOR =? ");
	            strQuery.append("Where CD_PRJNO =? ");
				pstmt = conn.prepareStatement(strQuery.toString());
	            pstmt.setString(1, UserId);
	            pstmt.setString(2, PrjNo);
				pstmt.executeUpdate();
				pstmt.close();
	             
	            strQuery.setLength(0);
	            strQuery.append("UPDATE CMD0304 SET ");
	            strQuery.append("CD_CLOSEDT=SYSDATE, ");
	            strQuery.append("CD_EDITOR =? ");
	            strQuery.append("Where CD_PRJNO =? ");
				pstmt = conn.prepareStatement(strQuery.toString());
	            pstmt.setString(1, UserId);
	            pstmt.setString(2, PrjNo);
				pstmt.executeUpdate();
				pstmt.close();
	             
	            strQuery.setLength(0);
	            strQuery.append("UPDATE CMD0305 SET ");
	            strQuery.append("CD_CLSDT=SYSDATE, ");
	            strQuery.append("CD_EDITOR =? ");
	            strQuery.append("Where CD_PRJNO =? ");
				pstmt = conn.prepareStatement(strQuery.toString());
	            pstmt.setString(1, UserId);
	            pstmt.setString(2, PrjNo);
				pstmt.executeUpdate();
				pstmt.close();	            
				break;
			case 2://플젝 완료처리
				strQuery.setLength(0);
				strQuery.append("UPDATE CMD0300 SET ");
				strQuery.append("CD_EDDATE=SYSDATE, ");
				strQuery.append("CD_STATUS='9', ");
				strQuery.append("CD_EDITOR = ? ");//UserId
				strQuery.append("Where CD_PRJNO = ? ");//PrjNo
				pstmt = conn.prepareStatement(strQuery.toString());
	            pstmt.setString(1, UserId);
	            pstmt.setString(2, PrjNo);
				pstmt.executeUpdate();
				pstmt.close();
				break;
			}
			conn.commit();
			conn.close();
			rs = null;
			pstmt = null;
			conn = null;
			return index;
			
  		} catch (SQLException sqlexception) {
			sqlexception.printStackTrace();
			if (rs != null)     try{rs.close();}catch (Exception ex){ex.printStackTrace();}
			if (pstmt != null)  try{pstmt.close();}catch (Exception ex2){ex2.printStackTrace();}
			if (conn != null){
				try{
					conn.rollback();
					ConnectionResource.release(conn);
				}catch(Exception ex3){
					ecamsLogger.error("## Cmd0300.Cmd0300_UPDATE() connection release exception ##");
					ex3.printStackTrace();
				}				
			}
			ecamsLogger.error("## Cmd0300.Cmd0300_UPDATE() SQLException START ##");
			ecamsLogger.error("## Error DESC : ", sqlexception);	
			ecamsLogger.error("## Cmd0300.Cmd0300_UPDATE() SQLException END ##");			
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
					ecamsLogger.error("## Cmd0300.Cmd0300_UPDATE() connection release exception ##");
					ex3.printStackTrace();
				}				
			}			
			ecamsLogger.error("## Cmd0300.Cmd0300_UPDATE() Exception START ##");				
			ecamsLogger.error("## Error DESC : ", exception);	
			ecamsLogger.error("## Cmd0300.Cmd0300_UPDATE() Exception END ##");				
			throw exception;
		}finally{
			if (rs != null)     try{rs.close();}catch (Exception ex){ex.printStackTrace();}
			if (pstmt != null)  try{pstmt.close();}catch (Exception ex2){ex2.printStackTrace();}
			if (conn != null){
				try{
					ConnectionResource.release(conn);
				}catch(Exception ex3){
					ecamsLogger.error("## Cmd0300.Cmd0300_UPDATE() connection release exception ##");
					ex3.printStackTrace();
				}
			}
		}
	}//end of Cmd0300_UPDATE() method statement
		
}//end of Cmd0300 class statement

