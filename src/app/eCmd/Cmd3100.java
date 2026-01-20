/*****************************************************************************************
	1. program ID	: eCmd3100.java
	2. create date	: 2008.07. 10
	3. auth		    : No name
	4. update date	: 09 06 16
	5. auth		    : No name
	6. description	: eCmd3100(프로그램목록)
*****************************************************************************************/

package app.eCmd;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.logging.log4j.Logger;

import app.common.LoggableStatement;
import app.common.UserInfo;
import app.common.eCAMSInfo;
import app.eCmm.Cmm1600;

import com.ecams.common.dbconn.ConnectionContext;
import com.ecams.common.dbconn.ConnectionResource;
import com.ecams.common.logger.EcamsLogger;


/**
 * @author bigeyes
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */


public class Cmd3100{
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

    public Object[] getJogun(int Index) throws Exception{
		ArrayList<HashMap<String, String>>  rsval = null;
		HashMap<String, String>			  rst		  = null;
		Object[]		  rtObj		  = null;

		try {
			rsval = new ArrayList<HashMap<String, String>>();
            rsval.clear();
        	rst = new HashMap<String, String>();
			rst.put("cm_codename", "선택하세요");
			rst.put("cm_micode", "0");
			rst.put("Index", Integer.toString(Index));
			rsval.add(rst);
			rst = null;

            if (Index == 1){
            	rst = new HashMap<String, String>();
				rst.put("cm_codename", "프로그램종류");
				rst.put("cm_micode", "1");
				rst.put("Index", Integer.toString(Index));
				rsval.add(rst);
				rst = null;
/*
            	rst = new HashMap<String, String>();
				rst.put("cm_codename", "프로그램언어");
				rst.put("cm_micode", "2");
				rst.put("Index", Integer.toString(Index));
				rsval.add(rst);
				rst = null;
*/
            	rst = new HashMap<String, String>();
				rst.put("cm_codename", "프로그램상태");
				rst.put("cm_micode", "3");
				rst.put("Index", Integer.toString(Index));
				rsval.add(rst);
				rst = null;
			}else{
            	rst = new HashMap<String, String>();
				rst.put("cm_codename", "프로그램명");
				rst.put("cm_micode", "1");
				rst.put("Index", Integer.toString(Index));
				rsval.add(rst);
				rst = null;

            	rst = new HashMap<String, String>();
				rst.put("cm_codename", "프로그램설명");
				rst.put("cm_micode", "2");
				rst.put("Index", Integer.toString(Index));
				rsval.add(rst);
				rst = null;

            	rst = new HashMap<String, String>();
				rst.put("cm_codename", "최종변경자");
				rst.put("cm_micode", "3");
				rst.put("Index", Integer.toString(Index));
				rsval.add(rst);
				rst = null;

            	rst = new HashMap<String, String>();
				rst.put("cm_codename", "프로그램경로");
				rst.put("cm_micode", "4");
				rst.put("Index", Integer.toString(Index));
				rsval.add(rst);
				rst = null;
			}

            rtObj = rsval.toArray();
            rsval.clear();
            rsval = null;

    		return rtObj;

		} catch (Exception exception) {
			exception.printStackTrace();
			ecamsLogger.error("## Cmd3100.getJogun() Exception START ##");
			ecamsLogger.error("## Error DESC : ", exception);
			ecamsLogger.error("## Cmd3100.getJogun() Exception END ##");
			throw exception;
		}
		finally{
			if (rtObj != null)	rtObj = null;
		}
	}//end of getJogun() method statement


    public Object[] getCode(String L_Syscd,int Index) throws SQLException, Exception {
		Connection        conn        = null;
		PreparedStatement pstmt       = null;
		ResultSet         rs          = null;
		StringBuffer      strQuery    = new StringBuffer();
		ArrayList<HashMap<String, String>>  rsval = new ArrayList<HashMap<String, String>>();
		HashMap<String, String>			  rst		  = null;
		Object[]		  rtObj		  = null;

		ConnectionContext connectionContext = new ConnectionResource();
		try {
			conn = connectionContext.getConnection();

			if (!L_Syscd.equals("00000")){
				switch(Index){
	            case 1:
	              	strQuery.append("select distinct a.cm_micode,a.cm_codename from cmm0020 a,cmm0036 b \n");
	            	strQuery.append("where b.cm_syscd=? and \n");//L_Syscd
	                strQuery.append("a.cm_macode='JAWON' and \n");
	                strQuery.append("a.cm_micode=b.cm_rsrccd and \n");
	                break;
                case 2:
	            	strQuery.append("select distinct a.cm_micode,a.cm_codename from cmm0020 a,cmm0032 b \n");
	                strQuery.append("where b.cm_syscd=? and \n");//L_Syscd
	                strQuery.append("cm_macode='LANGUAGE' and \n");
	                strQuery.append("a.cm_micode=b.cm_langcd and \n");
	                break;
                case 3:
	            	strQuery.append("select distinct a.cm_micode,a.cm_codename from cmm0020 a \n");
	                strQuery.append("where a.cm_macode='CMR0020' and \n");
	                break;
				}
				strQuery.append("a.cm_micode<>'****' and \n");
	            strQuery.append("a.cm_closedt is null \n");
	            strQuery.append("order by a.cm_micode \n");
			}else{
				strQuery.append("select cm_micode,cm_codename from cmm0020 where \n");
	            switch(Index){
				case 1:
					strQuery.append("cm_macode='JAWON' and \n");
					break;
				case 2:
					strQuery.append("cm_macode='LANGUAGE' and \n");
					break;
				case 3:
					strQuery.append("cm_macode='CMR0020' and \n");
					break;
	            }
	            strQuery.append("cm_micode<>'****' and \n");
	            strQuery.append("cm_closedt is null \n");
	            strQuery.append("order by cm_micode \n");
			}
			//pstmt = new LoggableStatement(conn,strQuery.toString());
            pstmt = conn.prepareStatement(strQuery.toString());
			if (!L_Syscd.equals("00000")){
				switch(Index){
	            	case 1:	pstmt.setString(1, L_Syscd);break;
	            	case 2:	pstmt.setString(1, L_Syscd);break;
				}
			}
			////ecamsLogger.error(((LoggableStatement)pstmt).getQueryString());
            rs = pstmt.executeQuery();
            rsval.clear();
			rst = new HashMap<String, String>();
			rst.put("cm_micode", "-1");
			rst.put("cm_codename", "선택하세요");
           	rsval.add(rst);
           	rst = null;
			while (rs.next()){
				rst = new HashMap<String, String>();
				rst.put("cm_micode", rs.getString("cm_micode"));
				rst.put("cm_codename", rs.getString("cm_codename"));
	           	rsval.add(rst);
	           	rst = null;
			}
			rs.close();
			pstmt.close();
			conn.close();
			rs = null;
			pstmt = null;
			conn = null;


			rtObj = rsval.toArray();
			rsval.clear();
			rsval = null;

			return rtObj;

		} catch (SQLException sqlexception) {
			sqlexception.printStackTrace();
			ecamsLogger.error("## Cmd3100.getCode() SQLException START ##");
			ecamsLogger.error("## Error DESC : ", sqlexception);
			ecamsLogger.error("## Cmd3100.getCode() SQLException END ##");
			throw sqlexception;
		} catch (Exception exception) {
			exception.printStackTrace();
			ecamsLogger.error("## Cmd3100.getCode() Exception START ##");
			ecamsLogger.error("## Error DESC : ", exception);
			ecamsLogger.error("## Cmd3100.getCode() Exception END ##");
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
					ecamsLogger.error("## Cmd3100.getCode() connection release exception ##");
					ex3.printStackTrace();
				}
			}
		}

	}//end of getCode() method statement

    public Object[] Cmd3100_Cbo_SysCd(String UserId) throws SQLException, Exception {
		Connection        conn        = null;
		PreparedStatement pstmt       = null;
		ResultSet         rs          = null;
		StringBuffer      strQuery    = new StringBuffer();
		ArrayList<HashMap<String, String>>  rsval = new ArrayList<HashMap<String, String>>();
		HashMap<String, String>			  rst		  = null;
		Object[]		  rtObj		  = null;

		ConnectionContext connectionContext = new ConnectionResource();
		try {
			conn = connectionContext.getConnection();

			UserInfo userinfo = new UserInfo();
			Boolean tmp = userinfo.isAdmin(UserId);

			strQuery.setLength(0);
	        strQuery.append("select cm_syscd,cm_sysmsg \n");
	        strQuery.append("from cmm0030 where \n");
	        strQuery.append("cm_closedt is null and substr(cm_sysinfo,1,1)='0' \n");
            if (!tmp){
            	strQuery.append("and cm_syscd in(\n");
            	strQuery.append("select distinct cm_syscd from cmm0044 where \n");
            	strQuery.append("cm_userid=? and cm_closedt is null) \n");//UserId
			}
            strQuery.append("order by cm_sysmsg \n");

            pstmt = conn.prepareStatement(strQuery.toString());
	        if (!tmp){
	        	pstmt.setString(1, UserId);
	        }
		    rs = pstmt.executeQuery();

			rst = new HashMap<String, String>();
			rst.put("cm_sysmsg", "전체");
			rst.put("cm_syscd", "00000");
           	rsval.add(rst);
           	rst = null;

			while (rs.next()){
				rst = new HashMap<String, String>();
				rst.put("cm_sysmsg", rs.getString("cm_sysmsg"));
				rst.put("cm_syscd", rs.getString("cm_syscd"));
	           	rsval.add(rst);
	           	rst = null;
			}
			rs.close();
			pstmt.close();
			conn.close();
			rs = null;
			pstmt = null;
			conn = null;

			rtObj = rsval.toArray();
			rsval.clear();
			rsval = null;

			return rtObj;

		} catch (SQLException sqlexception) {
			sqlexception.printStackTrace();
			throw sqlexception;
		} catch (Exception exception) {
			exception.printStackTrace();
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
					ex3.printStackTrace();
				}
			}
		}
	}//end of getCbo_SysCd() method statement


   	public Object[] getSql_Qry(HashMap<String,String> etcData) throws SQLException, Exception {
		Connection        conn        = null;
		PreparedStatement pstmt       = null;
		ResultSet         rs          = null;
		StringBuffer      strQuery    = new StringBuffer();
		ArrayList<HashMap<String, String>>  rtList = new ArrayList<HashMap<String, String>>();
		HashMap<String, String>			  rst		  = null;
		Object[]		  rtObj		  = null;
		String            WkCond2     = "";
		int 			  CNT 		  = 0;
		String            Txt_Cond    = null;
		String            Cbo_Cond2_code    = null;
		String			  strRsrcCd = "";
		String            strRsrc[] = null;
		int               i = 0;
		ConnectionContext connectionContext = new ConnectionResource();

		try {
			conn = connectionContext.getConnection();

			if (etcData.get("Txt_Cond").length() > 0) Txt_Cond = etcData.get("Txt_Cond");
			else Txt_Cond = "";

			if (etcData.get("Cbo_Cond2_code").length() >0 ) Cbo_Cond2_code=etcData.get("Cbo_Cond2_code");
			else Cbo_Cond2_code="";

			WkCond2 = "";
		    if (etcData.get("Cbo_Cond11_code").equals("3") && !"".equals(Txt_Cond)){
		    	strQuery.append("select cm_userid from cmm0040 where \n");
		    	strQuery.append(" cm_username = ? \n"); //Txt5
	            pstmt = conn.prepareStatement(strQuery.toString());
	            pstmt.setString(1, Txt_Cond);
	            rs = pstmt.executeQuery();
				while (rs.next()){
		             if (!"".equals(WkCond2)) {
		            	 WkCond2 = WkCond2 + ",";
		             }
		             WkCond2 = WkCond2 + rs.getString("cm_userid");
				}
				rs.close();
				pstmt.close();

				WkCond2 = WkCond2.replace(",", "','");
			}

		    if (Integer.parseInt(etcData.get("Cbo_Cond10_code")) > 0 && !Cbo_Cond2_code.equals("0")) {
				if (Integer.parseInt(etcData.get("Cbo_Cond10_code")) == 1) {
					strRsrcCd = "Y";
				}
		    }

		    if (!strRsrcCd.equals("Y")) {
		    	strQuery.setLength(0);
				strQuery.append("select cm_rsrccd from cmm0036                     \n");
				strQuery.append(" where cm_syscd=? and cm_closedt is null          \n");
				strQuery.append("   and substr(cm_info, 2, 1)='1'                  \n");
				strQuery.append("   and substr(cm_info, 26, 1)='0'                 \n");
				strQuery.append("   and cm_rsrccd not in (select cm_samersrc       \n");
				strQuery.append("                           from cmm0037           \n");
				strQuery.append("                          where cm_syscd=?)       \n");
				
				pstmt = conn.prepareStatement(strQuery.toString());
				//pstmt =  new LoggableStatement(conn, strQuery.toString());
	            pstmt.setString(1, etcData.get("L_SysCd"));
	            pstmt.setString(2, etcData.get("L_SysCd"));

	            ////ecamsLogger.error(((LoggableStatement)pstmt).getQueryString());
	            rs = pstmt.executeQuery();
	            while (rs.next()) {
	            	if (!"".equals(strRsrcCd)) strRsrcCd = strRsrcCd + ",";
	            	strRsrcCd = strRsrcCd + rs.getString("cm_rsrccd");
	            }
				rs.close();
				pstmt.close();

				strRsrc = strRsrcCd.split(",");
		    }
		    strQuery.setLength(0);
		    strQuery.append("select /*+ ALL ROWS */                                   \n");
		    strQuery.append("      a.cr_itemid,a.cr_syscd,a.cr_dsncd,a.cr_rsrcname,   \n");
		    strQuery.append("      a.cr_rsrccd,a.cr_lstver,a.cr_status,a.cr_jobcd,    \n");
		    strQuery.append("      a.cr_story,a.cr_editor,a.cr_creator,               \n");
		    strQuery.append("      to_char(a.cr_opendate,'yyyy/mm/dd') cr_opendate,   \n");
		    strQuery.append("      to_char(a.cr_lastdate,'yyyy/mm/dd') cr_lastdate,   \n");
    		strQuery.append("      a.cr_langcd,h.cm_sysmsg,d.cm_jobname job,          \n");
    		strQuery.append("      e.cm_codename rsrccd,g.cm_dirpath,                 \n");
			strQuery.append("      h.cm_sysgb,i.cm_codename sta,j.cm_Info,            \n");
			strQuery.append("      b.cm_username,k.cm_codename deptname,              \n");
			strQuery.append("      l.cm_codename compile,m.cm_username creatorname,   \n");
			strQuery.append("      n.cm_codename language                			  \n");
		    strQuery.append(" from cmm0020 n,cmm0040 m, \n");
		    strQuery.append("      cmm0020 l,cmm0020 k,cmm0036 j,cmm0030 h,cmm0070 g,cmm0020 i,cmm0102 d, \n");
		    if (etcData.get("L_JobCd").equals("0000") && !etcData.get("SecuYn").equals("Y")) {
	    	   strQuery.append(" cmm0044 y, \n");
	        }
		    strQuery.append("      cmm0020 e,cmm0040 b,cmr0020 a where      \n");
		    if (!etcData.get("L_SysCd").equals("00000")){
		    	strQuery.append(" a.cr_syscd=? and \n"); //L_Syscd
		    }

	        if (etcData.get("L_JobCd").equals("0000") && !etcData.get("SecuYn").equals("Y")) {
 	    	   	strQuery.append("    y.cm_syscd=a.cr_syscd and y.cm_userid=? and  \n");
 	    	   	strQuery.append("    y.cm_closedt is null and                     \n");
 	    	   	strQuery.append("    y.cm_jobcd=a.cr_jobcd and                    \n");
		    }else if (!etcData.get("L_JobCd").equals("0000")){
	        	strQuery.append(" a.cr_jobcd=? and \n"); //L_Jobcd
	        }

	        if (Integer.parseInt(etcData.get("Cbo_Cond10_code")) > 0 && !Cbo_Cond2_code.equals("-1")) {
				switch(Integer.parseInt(etcData.get("Cbo_Cond10_code"))){
	            case 1:
	               strQuery.append(" a.cr_rsrccd=? and                               \n");
	               break;
	            case 2:
	               //strQuery.append(" a.cr_langcd=? and                               \n");
	               break;
	            case 3:
	               strQuery.append(" a.cr_status=? and                               \n");
	               break;
				}
	        }

	        if (Integer.parseInt(etcData.get("Cbo_Cond11_code")) > 0 && !"".equals(Txt_Cond)){
				switch(Integer.parseInt(etcData.get("Cbo_Cond11_code"))){
	            case 1:
	            	strQuery.append(" upper(a.cr_rsrcname) like replace(upper(?),'_','\\_') escape '\\' and \n");
	            	break;
	            case 2:
	            	strQuery.append(" a.cr_story is not null and a.cr_story like replace(?,'_','\\_') escape '\\' and \n");
	            	break;
	            case 3:
	            	strQuery.append(" a.cr_editor is not null and rtrim(a.cr_editor) in (?) and \n");//WkCond2
	            	break;
	            case 4:
	            	strQuery.append(" g.cm_dirpath like replace(?,'_','\\_') escape '\\' and \n");
	            	break;
				}
	        }

	        if (etcData.get("Opt_Qry1").equals("true")){
	        	strQuery.append(" a.cr_clsdate is null and a.cr_status<>'9' and \n");
	        } else if (etcData.get("Opt_Qry2").equals("true")){
	        	strQuery.append(" (a.cr_clsdate is not null or a.cr_status='9') and \n");
	        }

	        if (etcData.get("Opt_Qry4").equals("true")){
	        	strQuery.append(" a.cr_status<>'3' and \n");
	        } else if (etcData.get("Opt_Qry5").equals("true")){
	        	strQuery.append(" a.cr_status='3' and \n");
	        }

	        strQuery.append(" a.cr_syscd=h.cm_syscd and \n");
	        strQuery.append(" a.cr_syscd=g.cm_syscd and a.cr_dsncd=g.cm_dsncd and \n");
	        strQuery.append(" a.cr_jobcd=d.cm_jobcd and \n");
	        strQuery.append(" a.cr_syscd=j.cm_syscd and \n");
	        strQuery.append(" j.cm_syscd=a.cr_syscd and \n");
	        strQuery.append(" a.cr_rsrccd=j.cm_rsrccd and \n");

	        if (!etcData.get("Chk_Aply").equals("true") && !"Y".equals(strRsrcCd) && strRsrc.length > 0){
	        	strQuery.append("a.cr_rsrccd in (");
				for (i=0 ; strRsrc.length>i ; i++) {
					if (strRsrc.length-1>i) strQuery.append("? ,");
					else strQuery.append("? ");
				}
				strQuery.append(") and                                 \n");
		    }
	        strQuery.append(" e.cm_macode='JAWON' and a.cr_rsrccd=e.cm_micode and 		\n");
	        strQuery.append(" i.cm_macode='CMR0020' and a.cr_status=i.cm_micode and 	\n");
	        strQuery.append(" a.cr_editor=b.cm_userid and								\n");
	        strQuery.append(" a.cr_teamcd=k.cm_micode and k.cm_macode='ETCTEAM' and k.cm_closedt is null and	\n");
	        strQuery.append(" a.cr_compile=l.cm_micode and l.cm_macode='COMPILE' and l.cm_closedt is null and	\n");
	        strQuery.append(" a.cr_creator=m.cm_userid and														\n");
	        strQuery.append(" a.cr_langcd=n.cm_micode and n.cm_macode='LANGUAGE' and n.cm_closedt is null	\n");
	        strQuery.append(" order by 1,2,4,7	\n");
	        

	        pstmt = conn.prepareStatement(strQuery.toString());
	  //      pstmt =  new LoggableStatement(conn, strQuery.toString());

            if (!etcData.get("L_SysCd").equals("00000")){
            	pstmt.setString(++CNT, etcData.get("L_SysCd"));
            }
	        if (etcData.get("L_JobCd").equals("0000") && !"Y".equals(etcData.get("SecuYn"))) {
	        	pstmt.setString(++CNT, etcData.get("UserId"));
		    }else if (!etcData.get("L_JobCd").equals("0000")){
		    	pstmt.setString(++CNT, etcData.get("L_JobCd"));
		    }
	        if (Integer.parseInt(etcData.get("Cbo_Cond10_code")) > 0 && !"-1".equals(Cbo_Cond2_code)) {
				pstmt.setString(++CNT, Cbo_Cond2_code);
	        }

	        if (Integer.parseInt(etcData.get("Cbo_Cond11_code")) > 0 && !"".equals(Txt_Cond)){
				switch(Integer.parseInt(etcData.get("Cbo_Cond11_code"))){
	            case 1:
	            	pstmt.setString(++CNT, "%"+Txt_Cond+"%");
	            	break;
	            case 2:
	            	pstmt.setString(++CNT, "%"+Txt_Cond+"%");
	            	break;
	            case 3:
	            	pstmt.setString(++CNT, WkCond2);
	            	break;
	            case 4:
	            	pstmt.setString(++CNT, "%"+Txt_Cond+"%");
	            	break;
				}
	        }
	        if (!etcData.get("Chk_Aply").equals("true") && !"Y".equals(strRsrcCd) && strRsrc.length > 0){
				for (i=0;strRsrc.length>i;i++) {
            		pstmt.setString(++CNT, strRsrc[i]);
            	}
	        }
	  //      //ecamsLogger.error(((LoggableStatement)pstmt).getQueryString());
            rs = pstmt.executeQuery();
            rtList.clear();
			while (rs.next()){
				rst = new HashMap<String,String>();
				//rst.put("NO",Integer.toString(rs.getRow()));
				rst.put("rsrccd",rs.getString("rsrccd"));
				rst.put("cr_rsrcname",rs.getString("cr_rsrcname"));
				rst.put("cr_story",rs.getString("cr_story"));
				rst.put("cm_dirpath",rs.getString("cm_dirpath"));
				rst.put("sta",rs.getString("sta"));
				rst.put("job",rs.getString("job"));
				rst.put("cr_editor",rs.getString("cr_editor"));
				rst.put("cm_username",rs.getString("cm_username"));
				rst.put("opendate",rs.getString("cr_opendate"));
				rst.put("cr_lastdate",rs.getString("cr_lastdate"));
				rst.put("cm_sysgb",rs.getString("cm_sysgb"));
				rst.put("cr_syscd",rs.getString("cr_syscd"));
				rst.put("cr_rsrccd",rs.getString("cr_rsrccd"));
				rst.put("cr_itemid",rs.getString("cr_itemid"));
				rst.put("cr_dsncd",rs.getString("cr_dsncd"));
				rst.put("cr_jobcd",rs.getString("cr_jobcd"));
				rst.put("cm_info",rs.getString("cm_info"));
				rst.put("cr_status",rs.getString("cr_status"));
				rst.put("cr_lstver",Integer.toString(rs.getInt("cr_lstver")));
				rst.put("deptname",rs.getString("deptname"));
				rst.put("compile",rs.getString("compile"));
				rst.put("creator",rs.getString("cr_creator"));
				rst.put("creatorname",rs.getString("creatorname"));
				rst.put("language",rs.getString("language"));
				
				rtList.add(rst);
				rst = null;
			}//end of while-loop statement
			rs.close();
			pstmt.close();
			conn.close();
			rs = null;
			pstmt = null;
			conn = null;


			rtObj = rtList.toArray();
			rtList.clear();
			rtList = null;

			return rtObj;

		} catch (SQLException sqlexception) {
			sqlexception.printStackTrace();
			throw sqlexception;
		} catch (Exception exception) {
			exception.printStackTrace();
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
					ex3.printStackTrace();
				}
			}
		}
	}
   	public Object[] gitProgList(HashMap<String,String> etcData) throws SQLException, Exception {
		Connection        conn        = null;
		PreparedStatement pstmt       = null;
		ResultSet         rs          = null;
		StringBuffer      strQuery    = new StringBuffer();
		ArrayList<HashMap<String, String>>  rtList = new ArrayList<HashMap<String, String>>();
		ArrayList<HashMap<String, String>>  rsrcList = new ArrayList<HashMap<String, String>>();
		ArrayList<HashMap<String, String>>  langList = new ArrayList<HashMap<String, String>>();
		HashMap<String, String>			  rst		  = null;
		String            strTmpPath = "";
		ConnectionContext connectionContext = new ConnectionResource();

		try {
			conn = connectionContext.getConnection();

			eCAMSInfo         ecamsinfo   = new eCAMSInfo();
			strTmpPath = ecamsinfo.getFileInfo_conn("99", conn);
			ecamsinfo = null;
			if ("".equals(strTmpPath) || strTmpPath == null) {
				if(conn != null) {
					conn.close();
					conn=null;
				}
				throw new Exception("관리자에게 연락하여 주시기 바랍니다. (형상관리환경설정 - 실행디렉토리)");
			}	
			rsrcList.clear();
			strQuery.setLength(0);
			strQuery.append("select b.cm_micode,b.cm_codename  \n");
			strQuery.append("  from cmm0020 b,cmm0036 a        \n");
			strQuery.append(" where a.cm_syscd=?               \n");
			strQuery.append("   and a.cm_closedt is null       \n");
			strQuery.append("   and b.cm_macode='JAWON'        \n");
			strQuery.append("   and b.cm_micode=a.cm_rsrccd    \n");
			pstmt = conn.prepareStatement(strQuery.toString());
            pstmt.setString(1, etcData.get("syscd"));
		    rs = pstmt.executeQuery();
		    while (rs.next()) {
		    	rst = new HashMap<String,String>();
		    	rst.put("cm_micode", rs.getString("cm_micode"));
		    	rst.put("cm_codename", rs.getString("cm_codename"));
		    	rsrcList.add(rst);
		    	rst = null;		    	
		    }
		    rs.close();
		    pstmt.close();
		    
			langList.clear();
			strQuery.setLength(0);
			strQuery.append("select b.cm_micode,b.cm_codename  \n");
			strQuery.append("  from cmm0020 b                  \n");
			strQuery.append(" where b.cm_macode='LANGUAGE'     \n");
			strQuery.append("   and b.cm_closedt is null       \n");
			pstmt = conn.prepareStatement(strQuery.toString());
		    rs = pstmt.executeQuery();
		    while (rs.next()) {
		    	rst = new HashMap<String,String>();
		    	rst.put("cm_micode", rs.getString("cm_micode"));
		    	rst.put("cm_codename", rs.getString("cm_codename"));
		    	langList.add(rst);
		    	rst = null;		    	
		    }
		    rs.close();
		    pstmt.close();
		    
		    strQuery.setLength(0);
		    strQuery.append("select /*+ ALL ROWS */                                   \n");
		    strQuery.append("      a.cr_itemid,a.cr_syscd,a.cr_dsncd,a.cr_rsrcname,   \n");
		    strQuery.append("      a.cr_rsrccd,a.cr_lstver,a.cr_status,a.cr_jobcd,    \n");
		    strQuery.append("      a.cr_story,a.cr_editor,a.cr_creator,a.cr_langcd,   \n");
		    strQuery.append("      to_char(a.cr_opendate,'yyyy/mm/dd') cr_opendate,   \n");
		    strQuery.append("      to_char(a.cr_lastdate,'yyyy/mm/dd') cr_lastdate,   \n");
    		strQuery.append("      (select cm_sysmsg from cmm0030 where cm_syscd=a.cr_syscd) cm_sysmsg,    \n");
    		strQuery.append("      (select cm_jobname from cmm0102 where cm_jobcd=a.cr_jobcd) cm_jobname,  \n");
    		strQuery.append("      (select cm_codename from cmm0020 where cm_macode='JAWON' and cm_micode=a.cr_rsrccd) rsrccd,     \n");
    		strQuery.append("      (select cm_dirpath from cmm0070 where cm_syscd=a.cr_syscd and cm_dsncd=a.cr_dsncd) cm_dirpath,  \n");
    		strQuery.append("      (select cm_codename from cmm0020 where cm_macode='JAWON' and cm_micode=a.cr_rsrccd) rsrccd,     \n");
    		strQuery.append("      (select cm_codename from cmm0020 where cm_macode='CMR0020' and cm_micode=a.cr_status) sta,      \n");
    		strQuery.append("      (select cm_codename from cmm0020 where cm_macode='LANGUAGE' and cm_micode=a.cr_langcd) language,\n");
    		strQuery.append("      (select cm_info from cmm0036 where cm_syscd=a.cr_syscd and cm_rsrccd=a.cr_rsrccd) cm_info,      \n");
    		strQuery.append("      (select cm_username from cmm0040 where cm_userid=a.cr_editor) cm_username,                      \n");
    		strQuery.append("      (select cm_username from cmm0040 where cm_userid=a.cr_creator) creatorname,                     \n");
    		strQuery.append("      (select cr_md5sum from cmr0021 where cr_itemid=a.cr_itemid and cr_acptno=a.cr_acptno) cr_md5sum \n");
		    strQuery.append("  from cmr0020 a        \n");
		    strQuery.append(" where a.cr_syscd=?     \n");
		    strQuery.append("   and a.cr_jobcd=?     \n");
		    strQuery.append(" order by a.cr_rsrcname \n");
	        

	        pstmt = conn.prepareStatement(strQuery.toString());
	  //      pstmt =  new LoggableStatement(conn, strQuery.toString());

            pstmt.setString(1, etcData.get("syscd"));
            pstmt.setString(2, etcData.get("jobcd"));
	  //      //ecamsLogger.error(((LoggableStatement)pstmt).getQueryString());
            rs = pstmt.executeQuery();
            rtList.clear();
			while (rs.next()){
				rst = new HashMap<String,String>();
				//rst.put("NO",Integer.toString(rs.getRow()));
				rst.put("rsrccd",rs.getString("rsrccd"));
				rst.put("cr_rsrcname",rs.getString("cr_rsrcname"));
				rst.put("cr_story",rs.getString("cr_story"));
				rst.put("cm_dirpath",rs.getString("cm_dirpath"));
				rst.put("sta",rs.getString("sta"));
				rst.put("cm_jobname",rs.getString("cm_jobname"));
				rst.put("cr_editor",rs.getString("cr_editor"));
				rst.put("cm_username",rs.getString("cm_username"));
				rst.put("opendate",rs.getString("cr_opendate"));
				rst.put("cr_lastdate",rs.getString("cr_lastdate"));
				rst.put("cr_syscd",rs.getString("cr_syscd"));
				rst.put("cr_rsrccd",rs.getString("cr_rsrccd"));
				rst.put("cr_itemid",rs.getString("cr_itemid"));
				rst.put("cr_dsncd",rs.getString("cr_dsncd"));
				rst.put("cr_jobcd",rs.getString("cr_jobcd"));
				rst.put("cm_info",rs.getString("cm_info"));
				rst.put("cr_status",rs.getString("cr_status"));
				rst.put("cr_lstver",Integer.toString(rs.getInt("cr_lstver")));
				rst.put("creator",rs.getString("cr_creator"));
				rst.put("creatorname",rs.getString("creatorname"));
				rst.put("language",rs.getString("language"));		
				rst.put("cr_md5sum",rs.getString("cr_md5sum"));				
				rtList.add(rst);
				rst = null;
			}//end of while-loop statement
			rs.close();
			pstmt.close();
			conn.close();
			rs = null;
			pstmt = null;
			conn = null;
			
			boolean ErrSw = false;
			String shlName = etcData.get("syscd") + "_" + etcData.get("jobcd") + "_" + etcData.get("userid") + "_gitprog.sh";
			String gitCommand = "./ecams_gitlab_export "+ etcData.get("syscd") + " " + etcData.get("jobcd") + " " + etcData.get("userid") + " XX";
			Cmm1600 cmm1600 = new Cmm1600();
			int retCd = cmm1600.execShell(shlName, gitCommand, true);
			String retMsg = "";
			if (retCd != 0) {
				retMsg = "GITLAB Command 수행 중 오류가 발생하였습니다.[command="+gitCommand+"] result=["+retCd+"]";
			} else {
				//szSysCD,szJobCD,szUserID,szQryCD
				String strFile = strTmpPath + etcData.get("syscd")  + "_"+ etcData.get("jobcd") + "_" + etcData.get("userid") + "_XX.GIT";
				File mFile = new File(strFile);	//파일을 생성한다.
		        if (!mFile.isFile() || !mFile.exists()) {	//파일을 생성하지 못하면
		        	ErrSw = true;
					retMsg ="신규대상목록 추출을 위한 작업에 실패하였습니다 [작성된 파일 없음]";
		        } else {				        
			        BufferedReader in = null;
			        
			        try {
			            in = new BufferedReader(new FileReader(mFile));	//파일을 읽어온다
			        	String str = null;
			            String strProg = "";
			            String strDirPath = "";
			            String strRsrcCD = "";
			            String strLangCD = "";
			            String strMd5Val = "";
			            
			            String wkF = "";
			            String wkItem = "";
			            int idx = 0;
			            int x = 0;
			            int i = 0;
			            boolean findSw = false;
			            //fprintf(RstPtr,"%s\t%s\t%s\t%s\n",szRsrcName,szDirPath,szRsrcCD,szLangCD);
			            while ((str = in.readLine()) != null) {			//str 변수에 읽어온 파일을 넣고 없지 않을 때까지 반복한다.
			            	if (str.trim().length()==0) continue;
			            	wkF = str;
			            	idx = 0;
                			while (wkF.length() > 0) {
                				findSw = false;
                				x = wkF.indexOf("\t");
                				++idx;
                				if (x>=0) {
                					wkItem = wkF.substring(0,x).trim();
                					wkF = wkF.substring(x).trim();
                				} else {
                					wkItem = wkF.trim();
                					wkF = "";
                				}
                				if (idx == 1) strProg = wkItem;
                				else if (idx == 2) strDirPath = wkItem;
                				else if (idx == 3) strRsrcCD = wkItem;
                				else if (idx == 4) strLangCD = wkItem;
                				else if (idx == 5) {
                					strMd5Val = wkItem;
                					findSw = true;
                					break;
                				}
                				if (idx>=5 || wkF.length()==0) break;
                			}
                			if (findSw) {
                				findSw = false;
	                			for (i=0;rtList.size()>i;i++) {
	                				if (rtList.get(i).get("cm_dirpath").equals(strDirPath) && rtList.get(i).get("cr_rsrcname").equals(strProg)) {
	                					rst = new HashMap<String,String>();
	                					rst = rtList.get(i);
	                					rst.put("progsta", "0");
	                					rst.put("progstaname", rst.get("sta"));
	                					if ("0".equals(rst.get("cr_lstver"))) {
	                						if ("3".equals(rst.get("cr_status"))) {
	                							rst.put("progsta", "3");
		                						rst.put("progstaname", "적용요청대상[신규]");
	                						} else {
	                							rst.put("progsta", "5");
		                						rst.put("progstaname", "적용요청중[신규]");
	                						}	
	                					}else {
	                						if (rst.get("cr_md5sum") != null && !"".equals(rst.get("cr_md5sum")) && strMd5Val.length()>0) {
	                							if (!rst.get("cr_md5sum").equals(strMd5Val)) {
	                								if ("0".equals(rst.get("cr_status"))) {
	                									rst.put("progsta", "2");	
	                									rst.put("progstaname", "프로그램변경[대여대상]");	
	                								} else if ("7".equals(rst.get("cr_status"))) {
	                									rst.put("progsta", "6");	
	                									rst.put("progstaname", "적용요청중[수정]");	
	                								} else{
	                									rst.put("progsta", "4");	
	                									rst.put("progstaname", "적용요청대상[수정]");	
	                								}
	                							} else {
                									rst.put("progstaname", "해당없음");	
	                							}
	                						} else {
            									rst.put("progstaname", "해당없음");
	                						}
	                					}
	                					findSw = true;
	                					rtList.set(i, rst);
	                					rst = null;
	                					break;
	                				}
	                			}
	                			if (!findSw) {
	                				rst = new HashMap<String, String>();
	                				rst.put("cr_rsrcname", strProg);
	                				rst.put("cm_dirpath", strDirPath);
	                				rst.put("cr_rsrcname", strProg);
	                				if (strRsrcCD.length()>0) {
		                				for (i=0;rsrcList.size()>i;i++) {
		                					if (rsrcList.get(i).get("cm_micode").equals(strRsrcCD)) {
		                						rst.put("cr_rsrccd", strRsrcCD);
		                						rst.put("rsrccd", rsrcList.get(i).get("cm_codename"));
		                						break;
		                					}
		                				}
	                				}
	                				if (strLangCD.length()>0) {
		                				for (i=0;langList.size()>i;i++) {
		                					if (langList.get(i).get("cm_micode").equals(strLangCD)) {
		                						rst.put("cr_langcd", strLangCD);
		                						rst.put("language", langList.get(i).get("cm_codename"));
		                						break;
		                					}
		                				}
	                				}
	                				rst.put("progsta", "1");
	                				rst.put("cr_lstver","0");
	                				rst.put("progstaname", "미등록프로그램");
	                				rtList.add(rst);	                				
	                			}
                			}
			            }
			        }finally {
			            if (in != null)
			                in.close();
			        }
		        }
		        if (mFile.isFile() && mFile.exists()) mFile.delete();
			}
			if (ErrSw && rtList.size()>0) {
				rst = new HashMap<String, String>();
				rst = rtList.get(0);
				rst.put("errmsg", retMsg);
				rtList.set(0, rst);
				rst = null;
			}
			return rtList.toArray();

		} catch (SQLException sqlexception) {
			sqlexception.printStackTrace();
			throw sqlexception;
		} catch (Exception exception) {
			exception.printStackTrace();
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
					ex3.printStackTrace();
				}
			}
		}
	}
}//end of Cmd3100 class statement
