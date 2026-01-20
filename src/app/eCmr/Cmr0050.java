/*****************************************************************************************
	1. program ID	: Cmr0050.java
	2. create date	: 2006.08. 08
	3. auth		    : is.choi
	4. update date	: 
	5. auth		    : 
	6. description	: 1. USER INFO.
*****************************************************************************************/

package app.eCmr;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
//import java.io.BufferedReader;
//import java.io.File;
//import java.io.FileInputStream;
//import java.io.FileOutputStream;
//import java.io.FileReader;
//import java.io.InputStreamReader;

import app.common.LoggableStatement;

//import java.io.OutputStreamWriter;

import org.apache.logging.log4j.Logger;
//import org.apache.poi.hssf.record.formula.functions.Sin;


import com.ecams.common.dbconn.ConnectionContext;
import com.ecams.common.dbconn.ConnectionResource;
import com.ecams.common.logger.EcamsLogger;
import java.util.ArrayList;
import java.util.HashMap;
import app.common.*;

import app.thread.*;

/**
 * @author bigeyes
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style -  Code Templates
 */
public class Cmr0050{    
	

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
	 */
    
    
    public Object[] getTestCase(String IsrId, String IsrSub, String ReqCd) throws SQLException, Exception{
		Connection        conn        = null;
		PreparedStatement pstmt       = null;
		ResultSet         rs          = null;
		StringBuffer      strQuery    = new StringBuffer();
		ArrayList<HashMap<String, String>>  rsval  = new ArrayList<HashMap<String, String>>();
		HashMap<String, String>			  rst		  = null;
		ConnectionContext connectionContext = new ConnectionResource();	
		int				  pstmtcount  = 1;
		Object[] returnObjectArray		 = null;
		
		try {
			conn = connectionContext.getConnection();
			strQuery.setLength(0);
			if (ReqCd.equals("02")) {   //통합테스트
				strQuery.append("select a.CC_CASESEQ,a.CC_CASENAME,b.cm_username    \n");
				strQuery.append("  from cmc0240 a,cmm0040 b						    \n");
				strQuery.append(" where a.CC_ISRID = ?                		        \n");
				strQuery.append("   and a.CC_ISRSUB = ?   				            \n");
				strQuery.append("   and nvl(a.CC_STATUS,'0')<> '3'		            \n");
				strQuery.append("   and nvl(a.CC_NOTHING,'N')='N'		            \n");
				strQuery.append("   and a.cc_scmuser=b.cm_userid	                \n");
				pstmt = conn.prepareStatement(strQuery.toString());	
				//pstmt = new LoggableStatement(conn,strQuery.toString());
				pstmtcount = 1;
				pstmt.setString(pstmtcount++,IsrId);
				pstmt.setString(pstmtcount++,IsrSub);
				////ecamsLogger.error(((LoggableStatement)pstmt).getQueryString());
		        rs = pstmt.executeQuery();
		        rsval.clear();
				while(rs.next()){			
					rst = new HashMap<String, String>();
					rst.put("CC_CASESEQ",rs.getString("CC_CASESEQ"));
					rst.put("CC_CASENAME",rs.getString("CC_CASENAME"));
					rst.put("cm_username",rs.getString("cm_username"));
					rsval.add(rst);
					rst = null;
				}//end of while-loop statement
				rs.close();
				pstmt.close();
			} else {
				strQuery.append("select a.CC_CASESEQ,a.CC_SCMUSER,a.CC_TESTGBN,  	\n");			
				strQuery.append("        a.CC_CASENAME,a.CC_EXPRST,a.CC_ETC,  		\n");			
				strQuery.append("        b.cm_username  		                    \n");
				strQuery.append("  from cmc0230 a,cmm0040 b						    \n");
				strQuery.append(" where a.CC_ISRID = ?                		        \n");
				strQuery.append("   and a.CC_ISRSUB = ?   				            \n");
				strQuery.append("   and a.CC_SCMUSER=b.cm_userid					\n");
				pstmt = conn.prepareStatement(strQuery.toString());	
				//pstmt = new LoggableStatement(conn,strQuery.toString());
				pstmtcount = 1;
				pstmt.setString(pstmtcount++,IsrId);
				pstmt.setString(pstmtcount++,IsrSub);
				////ecamsLogger.error(((LoggableStatement)pstmt).getQueryString());
		        rs = pstmt.executeQuery();
		        rsval.clear();
				while(rs.next()){			
					rst = new HashMap<String, String>();
					rst.put("CC_CASESEQ",rs.getString("CC_CASESEQ"));
					rst.put("CC_SCMUSER",rs.getString("CC_SCMUSER"));
					rst.put("CC_TESTGBN",rs.getString("CC_TESTGBN"));
					rst.put("CC_CASENAME",rs.getString("CC_CASENAME"));
					rst.put("CC_EXPRST",rs.getString("CC_EXPRST"));
					rst.put("CC_ETC",rs.getString("CC_ETC"));
					rst.put("cm_username",rs.getString("cm_username"));
					rsval.add(rst);
					rst = null;
				}//end of while-loop statement
				rs.close();
				pstmt.close();
			}
						
			conn.close();
			
			rs = null;
			pstmt = null;
			conn = null;
			
			returnObjectArray = rsval.toArray();
			rsval.clear();
			rsval = null;
			return returnObjectArray;			
			
		} catch (SQLException sqlexception) {
			sqlexception.printStackTrace();
			ecamsLogger.error("## Cmr0050.getTestCase() SQLException START ##");
			ecamsLogger.error("## Error DESC : ", sqlexception);	
			ecamsLogger.error("## Cmr0050.getTestCase() SQLException END ##");			
			throw sqlexception;
		} catch (Exception exception) {
			exception.printStackTrace();
			ecamsLogger.error("## Cmr0050.getTestCase() Exception START ##");				
			ecamsLogger.error("## Error DESC : ", exception);	
			ecamsLogger.error("## Cmr0050.getTestCase() Exception END ##");				
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
					ecamsLogger.error("## Cmr0050.getTestCase() connection release exception ##");
					ex3.printStackTrace();
				}
			}
		}
	}//end of getTestCase() method statement
    
    public String setCaseCopy(HashMap<String,String> etcData,ArrayList<HashMap<String,String>> CopyList) throws SQLException, Exception {
		Connection        conn        = null;
		PreparedStatement pstmt       = null;
		ResultSet         rs          = null;
		StringBuffer      strQuery    = new StringBuffer();
		String            retMsg      = "0";
		int				  seq		  = 1;
		int				  i			  = 0;
		int				  pstmtcount  = 1;
		ConnectionContext connectionContext = new ConnectionResource();		
		
		try {
			conn = connectionContext.getConnection();
			
			if (etcData.get("reqcd").equals("02")) {  //통합테스트
				strQuery.setLength(0);
				strQuery.append("select nvl(max(cc_caseseq),0) maxseq from cmc0240 \n");	
				strQuery.append(" where CC_ISRID = ? and CC_ISRSUB = ?			   \n");
				pstmt = conn.prepareStatement(strQuery.toString());	
				//pstmt = new LoggableStatement(conn,strQuery.toString());
				pstmtcount = 1;
				pstmt.setString(pstmtcount++,etcData.get("toisrid"));
				pstmt.setString(pstmtcount++,etcData.get("toisrsub"));
		        ////ecamsLogger.error(((LoggableStatement)pstmt).getQueryString());           
		        rs = pstmt.executeQuery();
				if (rs.next()){
					
					seq = rs.getInt("maxseq");
					++seq;
					
				}//end of while-loop statement
				rs.close();
				pstmt.close();
				for(i=0;i<CopyList.size();i++){
		        	strQuery.setLength(0);
		    		strQuery.append("insert into cmc0240 (CC_ISRID,CC_ISRSUB,CC_CASESEQ,CC_SCMUSER,	\n");
		    		strQuery.append("   CC_STATUS,CC_CREATDT,CC_LASTDT,CC_CASENAME,CC_NOTHING)		\n");
		    		strQuery.append(" (select ?,?,?,?,'0',SYSDATE,SYSDATE,CC_CASENAME,'N'  	        \n");
		    		strQuery.append("    from cmc0240	    \n");
		    		strQuery.append("   where cc_isrid=?	\n");
		    		strQuery.append("     and cc_isrsub=?	\n");
		    		strQuery.append("     and cc_caseseq=?)	\n");
		    		
		    		pstmt = conn.prepareStatement(strQuery.toString());
		    		//pstmt = new LoggableStatement(conn,strQuery.toString());
		    		pstmtcount = 1;
		          	pstmt.setString(pstmtcount++, etcData.get("toisrid"));
		          	pstmt.setString(pstmtcount++, etcData.get("toisrsub"));
		      	    pstmt.setInt(pstmtcount++, seq);
		          	pstmt.setString(pstmtcount++, etcData.get("UserId"));
		          	pstmt.setString(pstmtcount++, etcData.get("fromisrid"));
		          	pstmt.setString(pstmtcount++, etcData.get("fromisrsub"));
		      	    pstmt.setString(pstmtcount++, CopyList.get(i).get("CC_CASESEQ"));
		      	    ////ecamsLogger.error(((LoggableStatement)pstmt).getQueryString());
		      		pstmt.executeUpdate();
		      		pstmt.close();
		      		
		        	strQuery.setLength(0);
		    		strQuery.append("insert into cmc0241 (CC_ISRID,CC_ISRSUB,CC_CASESEQ,	\n");
		    		strQuery.append("   CC_GBNCD,CC_SEQNO,CC_ITEMMSG,CC_LASTDT,CC_EDITOR)	\n");
		    		strQuery.append(" (select ?,?,?,CC_GBNCD,CC_SEQNO,CC_ITEMMSG,SYSDATE,?  \n");
		    		strQuery.append("    from cmc0241	    \n");
		    		strQuery.append("   where cc_isrid=?	\n");
		    		strQuery.append("     and cc_isrsub=?	\n");
		    		strQuery.append("     and cc_caseseq=?)	\n");
		    		
		    		pstmt = conn.prepareStatement(strQuery.toString());
		    		//pstmt = new LoggableStatement(conn,strQuery.toString());
		    		pstmtcount = 1;
		          	pstmt.setString(pstmtcount++, etcData.get("toisrid"));
		          	pstmt.setString(pstmtcount++, etcData.get("toisrsub"));
		      	    pstmt.setInt(pstmtcount++, seq);
		          	pstmt.setString(pstmtcount++, etcData.get("UserId"));
		          	pstmt.setString(pstmtcount++, etcData.get("fromisrid"));
		          	pstmt.setString(pstmtcount++, etcData.get("fromisrsub"));
		      	    pstmt.setString(pstmtcount++, CopyList.get(i).get("CC_CASESEQ"));
		      	    ////ecamsLogger.error(((LoggableStatement)pstmt).getQueryString());
		      		pstmt.executeUpdate();
		      		pstmt.close();
		      		seq++;
				}
				
			} else {
				strQuery.setLength(0);
				strQuery.append("select nvl(max(cc_caseseq),0) maxseq from cmc0230 \n");	
				strQuery.append(" where CC_ISRID = ? and CC_ISRSUB = ?			   \n");
				pstmt = conn.prepareStatement(strQuery.toString());	
				//pstmt = new LoggableStatement(conn,strQuery.toString());
				pstmtcount = 1;
				pstmt.setString(pstmtcount++,etcData.get("toisrid"));
				pstmt.setString(pstmtcount++,etcData.get("toisrsub"));
		        ////ecamsLogger.error(((LoggableStatement)pstmt).getQueryString());           
		        rs = pstmt.executeQuery();
				if (rs.next()){
					
					seq = rs.getInt("maxseq");
					++seq;
					
				}//end of while-loop statement
				rs.close();
				pstmt.close();
				for(i=0;i<CopyList.size();i++){
		        	strQuery.setLength(0);
		    		strQuery.append("insert into cmc0230 (CC_ISRID,CC_ISRSUB,CC_CASESEQ,CC_SCMUSER,				\n");
		    		strQuery.append("   CC_CREATDT,CC_LASTDT,CC_TESTGBN,CC_CASENAME,CC_EXPRST,CC_ETC)			\n");
		    		strQuery.append(" (select ?,?,?,?,SYSDATE,SYSDATE,CC_TESTGBN,CC_CASENAME,CC_EXPRST,CC_ETC	\n");
		    		strQuery.append("    from cmc0230	    \n");
		    		strQuery.append("   where cc_isrid=?	\n");
		    		strQuery.append("     and cc_isrsub=?	\n");
		    		strQuery.append("     and cc_caseseq=?)	\n");
		    		
		    		pstmt = conn.prepareStatement(strQuery.toString());
		    		//pstmt = new LoggableStatement(conn,strQuery.toString());
		    		pstmtcount = 1;
		          	pstmt.setString(pstmtcount++, etcData.get("toisrid"));
		          	pstmt.setString(pstmtcount++, etcData.get("toisrsub"));
		      	    pstmt.setInt(pstmtcount++, seq++);
		          	pstmt.setString(pstmtcount++, etcData.get("UserId"));
		          	pstmt.setString(pstmtcount++, etcData.get("fromisrid"));
		          	pstmt.setString(pstmtcount++, etcData.get("fromisrsub"));
		      	    pstmt.setString(pstmtcount++, CopyList.get(i).get("CC_CASESEQ"));
		      	    ////ecamsLogger.error(((LoggableStatement)pstmt).getQueryString());
		      		pstmt.executeUpdate();
		      		pstmt.close();
				}
			}

			strQuery.setLength(0);                		
    		strQuery.append("update cmc0110 set 	  \n");             		
    		strQuery.append("    cc_substatus='23',	  \n");            		
    		strQuery.append("    cc_mainstatus='02'	  \n");
    		strQuery.append(" where CC_ISRID=?		  \n");
    		strQuery.append("   and CC_ISRSUB=?		  \n");
    		strQuery.append("   and CC_substatus in ('21','36') \n");
    		pstmt = conn.prepareStatement(strQuery.toString());	
	//		pstmt = new LoggableStatement(conn,strQuery.toString());
			pstmtcount = 1;
			pstmt.setString(pstmtcount++,etcData.get("toisrid"));
			pstmt.setString(pstmtcount++,etcData.get("toisrsub"));
	//        //ecamsLogger.error(((LoggableStatement)pstmt).getQueryString());           
			pstmt.executeUpdate();           		
			rs.close();
			pstmt.close();
			conn.close();
	        
			conn = null;
			rs = null;
			pstmt = null;
			
			
		    retMsg = "0";
	        
	        
	        return retMsg;
	        
	        
		} catch (SQLException sqlexception) {
			sqlexception.printStackTrace();
			ecamsLogger.error("## Cmr0050.setCaseCopy() SQLException START ##");
			ecamsLogger.error("## Error DESC : ", sqlexception);	
			ecamsLogger.error("## Cmr0050.setCaseCopy() SQLException END ##");			
			throw sqlexception;
		} catch (Exception exception) {
			exception.printStackTrace();
			ecamsLogger.error("## Cmr0050.setCaseCopy() Exception START ##");				
			ecamsLogger.error("## Error DESC : ", exception);	
			ecamsLogger.error("## Cmr0050.setCaseCopy() Exception END ##");				
			throw exception;
		}finally{
			if (strQuery != null) 	strQuery = null;
			if (rs != null)     try{rs.close();}catch (Exception ex){ex.printStackTrace();}
			if (pstmt != null)  try{pstmt.close();}catch (Exception ex2){ex2.printStackTrace();}
			if (conn != null){
				try{
					conn.commit();
					conn.close();
					conn = null;
				}catch(Exception ex3){
					ecamsLogger.error("## Cmr0050.setCaseCopy() connection release exception ##");
					ex3.printStackTrace();
				}
			}
		}
	}//end of Cmr0050_Insert() method statement
}//end of Cmr0050 class statement