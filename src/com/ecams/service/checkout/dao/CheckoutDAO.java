/*****************************************************************************************
	1. program ID	: Cmr3200.java
	2. create date	: 2008.04.08
	3. auth		    : m.s.kang
	4. update date	: 2009.02.21
	5. auth		    : no name
	6. description	: Request List
*****************************************************************************************/

package com.ecams.service.checkout.dao;

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

import app.common.LoggableStatement;
import app.common.SystemPath;
import com.ecams.service.checkout.valueobject.CheckoutVO;


public class CheckoutDAO{

    Logger ecamsLogger  = EcamsLogger.getLoggerInstance();
    public ArrayList<CheckoutVO> getCheckoutCnt(String pSysCd,String pQrycd,String pTeamCd,String pStateCd,String pReqUser,String pIsrId,String pStartDt,String pEndDt,String pUserId) throws SQLException, Exception {
    	//public ArrayList<CheckoutVO> getCheckoutCnt(String pUserId, String pSysCd,String pReqCd,String pTeamCd,String pStateCd,String pReqUser,String pIsrId,String pStartDt,String pEndDt ) throws SQLException, Exception {
    //public ArrayList<CheckoutVO> getCheckoutCnt(String pReqCd, String pUserId) throws SQLException, Exception {
		Connection        conn        = null;
		PreparedStatement pstmt       = null;
		PreparedStatement pstmt2      = null;
		ResultSet         rs          = null;
		ResultSet         rs2          = null;
		StringBuffer      strQuery    = new StringBuffer();

		
		ArrayList<CheckoutVO> arrCheckout = new ArrayList<CheckoutVO>();
		CheckoutVO vo = null;
		 
		ConnectionContext connectionContext = new ConnectionResource();

		try {
			System.out.println("try");
			conn = connectionContext.getConnection();

			strQuery.setLength(0);
			strQuery.append("select  distinct NVL(NVL(NVL(a.cr_sayu, (SELECT CR_REQSUB FROM CMR1012 WHERE CR_ACPTNO = A.CR_ACPTNO AND ROWNUM=1)), \n");
			strQuery.append("                 (SELECT CR_EDITCON FROM CMR1010 WHERE CR_ACPTNO = A.CR_ACPTNO AND ROWNUM=1)), '力格绝娇') as cr_sayu  \n");
			strQuery.append("      ,a.cr_acptno 													\n");// a.cr_orderid, a.cr_prcdate, a.cr_passcd, h.cr_rsrcname ,a.cr_sayu,a.cr_acptno  \n");
			// a.cr_orderid, a.cr_prcdate, a.cr_passcd, h.cr_rsrcname ,a.cr_sayu,a.cr_acptno  \n");
			strQuery.append("  FROM	CMR1010 H, CMM0020 E,CMM0040 D,CMM0100 C,CMM0030 B,CMR1000 A,cmr0020 f    \n");
			strQuery.append(" where a.cr_qrycd<'30' \n");
			strQuery.append(" and a.cr_qrycd = '01' \n");
			strQuery.append(" and f.cr_status = '5' and h.cr_acptno = a.cr_acptno \n");
			strQuery.append(" and f.CR_ITEMID = h.CR_ITEMID \n");
			strQuery.append(" and a.cr_editor in (select cm_userid from cmm0040 where (cm_username=? or cm_userid=?)) \n");
			strQuery.append(" and a.cr_syscd=b.cm_syscd \n");
			strQuery.append(" and d.cm_project = c.cm_deptcd \n");
			strQuery.append(" and a.cr_editor = d.cm_userid \n");
			strQuery.append(" and e.cm_macode='REQUEST' \n");
			strQuery.append(" and e.cm_micode=a.cr_qrycd \n");
			strQuery.append(" and h.CR_CONFNO is null \n");
			
			
			System.out.println("this is before in the pstmt");
			pstmt = conn.prepareStatement(strQuery.toString());
			pstmt = new LoggableStatement(conn,strQuery.toString());
			
			pstmt.setString(1, pUserId); 
			pstmt.setString(2, pUserId);
			//pstmt.setString(3, pQrycd);
			
			ecamsLogger.error(((LoggableStatement)pstmt).getQueryString());
			
			
            rs = pstmt.executeQuery();
            
			while (rs.next()){
				
	            vo = new CheckoutVO();
	            //vo.setAcptdate(rs.getString("cr_acptdate"));
	            //vo.setCm_username(rs.getString("cm_username"));
	            //vo.setCr_editor(rs.getString("cr_editor"));
	            //vo.setCr_status(rs.getString("cr_status"));
	            vo.setCr_acptno(rs.getString("cr_acptno"));
	            
	            //vo.setStrIsrid(strIsrid);
	            //vo.setStrIsrTitle(strIsrTitle);
//	            vo.setCr_isrid(rs.getString("cr_orderid"));
//	            vo.setCr_prcdate(rs.getString("cr_prcdate"));
//	            vo.setCr_passcd(rs.getString("cr_passcd"));
//	            vo.setCr_rsrcname(rs.getString("cr_rsrcname"));
	            vo.setCr_sayu(rs.getString("cr_sayu"));
	            arrCheckout.add(vo);

	            vo = null;
	           
	            
			}//end of while-loop statement
			
			 
			
			rs.close();
			pstmt.close();
			conn.close();
			//ecamsLogger.error("+++++ query end +++++");
			rs = null;
			pstmt = null;
			rs2 = null;
			pstmt2 = null;
			conn = null;
			strQuery = null;
			
			return arrCheckout;

		} catch (SQLException sqlexception) {
			sqlexception.printStackTrace();
			ecamsLogger.error("## CheckoutDAO.ArrayList() SQLException START ##");
			ecamsLogger.error("## Error DESC : ", sqlexception);
			ecamsLogger.error("## CheckoutDAO.ArrayList() SQLException END ##");
			throw sqlexception;
		} catch (Exception exception) {
			exception.printStackTrace();
			ecamsLogger.error("## CheckoutDAO.ArrayList()) Exception START ##");
			ecamsLogger.error("## Error DESC : ", exception);
			ecamsLogger.error("## CheckoutDAO.ArrayList() Exception END ##");
			throw exception;
		}finally{
			if (strQuery != null) 	strQuery = null;
			//if (returnObject != null)	returnObject = null;
			if (rs != null)     try{rs.close();}catch (Exception ex){ex.printStackTrace();}
			if (rs2 != null)    try{rs2.close();}catch (Exception ex){ex.printStackTrace();}
			if (pstmt != null)  try{pstmt.close();}catch (Exception ex2){ex2.printStackTrace();}
			if (pstmt2 != null)  try{pstmt2.close();}catch (Exception ex2){ex2.printStackTrace();}
			if (conn != null){
				try{
					conn.close();
				}catch(Exception ex3){
					ecamsLogger.error("## CheckoutDAO.ArrayList() connection release exception ##");
					ex3.printStackTrace();
				}
			}
		}
	}//end of SelectList() method statement

    public ArrayList<CheckoutVO> getCheckInCnt(String pSysCd,String pQrycd,String pTeamCd,String pStateCd,String pReqUser,String pIsrId,String pStartDt,String pEndDt,String pUserId) throws SQLException, Exception {
    	//public ArrayList<CheckoutVO> getCheckoutCnt(String pUserId, String pSysCd,String pReqCd,String pTeamCd,String pStateCd,String pReqUser,String pIsrId,String pStartDt,String pEndDt ) throws SQLException, Exception {
    //public ArrayList<CheckoutVO> getCheckoutCnt(String pReqCd, String pUserId) throws SQLException, Exception {
		Connection        conn        = null;
		PreparedStatement pstmt       = null;
		PreparedStatement pstmt2      = null;
		ResultSet         rs          = null;
		ResultSet         rs2          = null;
		StringBuffer      strQuery    = new StringBuffer();

		
		ArrayList<CheckoutVO> arrCheckout = new ArrayList<CheckoutVO>();
		CheckoutVO vo = null;
		 
		ConnectionContext connectionContext = new ConnectionResource();

		try {
			System.out.println("try");
			conn = connectionContext.getConnection();

			strQuery.setLength(0);
			strQuery.append("select  distinct NVL(NVL(NVL(a.cr_sayu, (SELECT CR_REQSUB FROM CMR1012 WHERE CR_ACPTNO = A.CR_ACPTNO AND ROWNUM=1)), \n");
			strQuery.append("                 (SELECT CR_EDITCON FROM CMR1010 WHERE CR_ACPTNO = A.CR_ACPTNO AND ROWNUM=1)), '力格绝娇') as cr_sayu  \n");
			strQuery.append("      ,a.cr_acptno 													\n");// a.cr_orderid, a.cr_prcdate, a.cr_passcd, h.cr_rsrcname ,a.cr_sayu,a.cr_acptno  \n");
			strQuery.append("  FROM	CMR1010 H, CMM0020 E,CMM0040 D,CMM0100 C,CMM0030 B,CMR1000 A    \n");
			strQuery.append(" where a.cr_qrycd<'30' \n");
			strQuery.append(" and a.cr_qrycd = '04' and h.cr_acptno = a.cr_acptno \n");
			strQuery.append(" and a.cr_editor in (select cm_userid from cmm0040 where (cm_username=? or cm_userid=?)) \n");
			strQuery.append(" and a.cr_syscd=b.cm_syscd \n");
			strQuery.append(" and d.cm_project = c.cm_deptcd \n");
			strQuery.append(" and a.cr_editor = d.cm_userid \n");
			strQuery.append(" and e.cm_macode='REQUEST' \n");
			strQuery.append(" and e.cm_micode=a.cr_qrycd \n");
			strQuery.append(" and h.CR_PRCDATE is null \n");
			
			
			System.out.println("this is before in the pstmt");
			pstmt = conn.prepareStatement(strQuery.toString());
			pstmt = new LoggableStatement(conn,strQuery.toString());
			
			pstmt.setString(1, pUserId); 
			pstmt.setString(2, pUserId);
			//pstmt.setString(3, pQrycd);
			
			ecamsLogger.error(((LoggableStatement)pstmt).getQueryString());
			
			
            rs = pstmt.executeQuery();
            
			while (rs.next()){
				
	            vo = new CheckoutVO();
	            //vo.setAcptdate(rs.getString("cr_acptdate")); 
	            //vo.setCm_username(rs.getString("cm_username")); 
	            //vo.setCr_editor(rs.getString("cr_editor"));
	            //vo.setCr_status(rs.getString("cr_status"));
	            //vo.setCr_syscd(rs.getString("cr_syscd"));
	            vo.setCr_acptno(rs.getString("cr_acptno"));
	            //vo.setStrIsrid(strIsrid);
	            //vo.setStrIsrTitle(strIsrTitle);
//	            vo.setCr_isrid(rs.getString("cr_orderid"));
//	            vo.setCr_prcdate(rs.getString("cr_prcdate"));
//	            vo.setCr_passcd(rs.getString("cr_passcd"));
//	            vo.setCr_rsrcname(rs.getString("cr_rsrcname"));
	            vo.setCr_sayu(rs.getString("cr_sayu"));
	            arrCheckout.add(vo);

	            vo = null;
	           
	            
			}//end of while-loop statement
			
			 
			
			rs.close();
			pstmt.close();
			conn.close();
			//ecamsLogger.error("+++++ query end +++++");
			rs = null;
			pstmt = null;
			rs2 = null;
			pstmt2 = null;
			conn = null;
			strQuery = null;
			
			return arrCheckout;

		} catch (SQLException sqlexception) {
			sqlexception.printStackTrace();
			ecamsLogger.error("## CheckoutDAO.ArrayList() SQLException START ##");
			ecamsLogger.error("## Error DESC : ", sqlexception);
			ecamsLogger.error("## CheckoutDAO.ArrayList() SQLException END ##");
			throw sqlexception;
		} catch (Exception exception) {
			exception.printStackTrace();
			ecamsLogger.error("## CheckoutDAO.ArrayList()) Exception START ##");
			ecamsLogger.error("## Error DESC : ", exception);
			ecamsLogger.error("## CheckoutDAO.ArrayList() Exception END ##");
			throw exception;
		}finally{
			if (strQuery != null) 	strQuery = null;
			//if (returnObject != null)	returnObject = null;
			if (rs != null)     try{rs.close();}catch (Exception ex){ex.printStackTrace();}
			if (rs2 != null)    try{rs2.close();}catch (Exception ex){ex.printStackTrace();}
			if (pstmt != null)  try{pstmt.close();}catch (Exception ex2){ex2.printStackTrace();}
			if (pstmt2 != null)  try{pstmt2.close();}catch (Exception ex2){ex2.printStackTrace();}
			if (conn != null){
				try{
					conn.close();
				}catch(Exception ex3){
					ecamsLogger.error("## CheckoutDAO.ArrayList() connection release exception ##");
					ex3.printStackTrace();
				}
			}
		}
	}//end of SelectList() method statement
    
    public ArrayList<CheckoutVO> getOrderCnt(String pSysCd,String pQrycd,String pTeamCd,String pStateCd,String pReqUser,String pIsrId,String pStartDt,String pEndDt,String pUserId) throws SQLException, Exception {
    	//public ArrayList<CheckoutVO> getCheckoutCnt(String pUserId, String pSysCd,String pReqCd,String pTeamCd,String pStateCd,String pReqUser,String pIsrId,String pStartDt,String pEndDt ) throws SQLException, Exception {
    //public ArrayList<CheckoutVO> getCheckoutCnt(String pReqCd, String pUserId) throws SQLException, Exception {
		Connection        conn        = null;
		PreparedStatement pstmt       = null;
		PreparedStatement pstmt2      = null;
		ResultSet         rs          = null;
		ResultSet         rs2          = null;
		StringBuffer      strQuery    = new StringBuffer();

		
		ArrayList<CheckoutVO> arrCheckout = new ArrayList<CheckoutVO>();
		CheckoutVO vo = null;
		 
		ConnectionContext connectionContext = new ConnectionResource();

		try {
			conn = connectionContext.getConnection();

			strQuery.setLength(0);
			strQuery.append("select a.CC_ORDERID, a.CC_REQSUB		\n");
			strQuery.append("from cmc0420 a, cmc0450 c				\n");
			strQuery.append("where c.cc_runner = ?					\n");
			strQuery.append("and a.cc_status != '9'					\n");
			strQuery.append("and a.CC_ORDERID = c.CC_ORDERID		\n");
	        
			pstmt = conn.prepareStatement(strQuery.toString());
			pstmt = new LoggableStatement(conn,strQuery.toString());			
			pstmt.setString(1, pUserId); 			
			ecamsLogger.error(((LoggableStatement)pstmt).getQueryString());
            rs = pstmt.executeQuery();            
			while (rs.next()){
	            vo = new CheckoutVO();
	            vo.setCr_isrid(rs.getString("cc_orderid")+" "+rs.getString("CC_REQSUB"));	            
	            arrCheckout.add(vo);
	            vo = null; 
			}//end of while-loop statement
			
			rs.close();
			pstmt.close();
			conn.close();
			rs = null;
			pstmt = null;
			rs2 = null;
			pstmt2 = null;
			conn = null;
			strQuery = null;
			
			return arrCheckout;

		} catch (SQLException sqlexception) {
			sqlexception.printStackTrace();
			ecamsLogger.error("## CheckoutDAO.ArrayList() SQLException START ##");
			ecamsLogger.error("## Error DESC : ", sqlexception);
			ecamsLogger.error("## CheckoutDAO.ArrayList() SQLException END ##");
			throw sqlexception;
		} catch (Exception exception) {
			exception.printStackTrace();
			ecamsLogger.error("## CheckoutDAO.ArrayList()) Exception START ##");
			ecamsLogger.error("## Error DESC : ", exception);
			ecamsLogger.error("## CheckoutDAO.ArrayList() Exception END ##");
			throw exception;
		}finally{
			if (strQuery != null) 	strQuery = null;
			//if (returnObject != null)	returnObject = null;
			if (rs != null)     try{rs.close();}catch (Exception ex){ex.printStackTrace();}
			if (rs2 != null)    try{rs2.close();}catch (Exception ex){ex.printStackTrace();}
			if (pstmt != null)  try{pstmt.close();}catch (Exception ex2){ex2.printStackTrace();}
			if (pstmt2 != null)  try{pstmt2.close();}catch (Exception ex2){ex2.printStackTrace();}
			if (conn != null){
				try{
					conn.close();
				}catch(Exception ex3){
					ecamsLogger.error("## CheckoutDAO.ArrayList() connection release exception ##");
					ex3.printStackTrace();
				}
			}
		}
	}//end of SelectList() method statement
    
    
}//end of Cmr3200 class statement
