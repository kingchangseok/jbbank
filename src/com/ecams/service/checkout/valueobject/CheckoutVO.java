package com.ecams.service.checkout.valueobject;

import java.io.Serializable;
import java.util.HashMap;

public class CheckoutVO implements Serializable{
	
	private static final long serialVersionUID = 1L;
		
		/**
		 * 
		 *//**
	rst = new HashMap<String, String>();
    rst.put("rows",    Integer.toString(rs.getRow()));  //NO
	rst.put("syscd",   rs.getString("cm_sysmsg"));      //시스템
	rst.put("acptno",  rs.getString("acptno"));         //요청번호
	rst.put("deptname",rs.getString("cm_deptname"));    //팀명
	rst.put("editor",  rs.getString("cm_username"));    //요청자
	rst.put("qrycd",   rs.getString("sin"));	        //요청구분
	rst.put("sayu",    rs.getString("cr_sayu"));	    //요청구분
	rst.put("acptdate",rs.getString("acptdate"));       //요청일시
	rst.put("sta",     ConfName);                       //상태
	rst.put("pgmid",   PgmSayu);  	                    //프로그램명
	rst.put("isrid",   strIsrid);                          //프로젝트
	rst.put("isrtitle",   strIsrTitle);                     //프로젝트
	rst.put("qrycd2",  rs.getString("cr_qrycd"));       //Qrycd
	rst.put("editor2",  rs.getString("cr_editor"));     //Editor
	rst.put("sysgb",  rs.getString("cr_sysgb"));        //SysGb
	rst.put("endyn",  rs.getString("cr_status"));       //처리완료여부
	rst.put("syscd2",  rs.getString("cr_syscd"));       //SysCd
	rst.put("acptno2",  rs.getString("cr_acptno"));     //AcptNo
	rst.put("Sunhang",  Sunhang);     //선행작업 유무 */
	
		
		
		private String acptno = null;
		private String cm_deptname = null;
		private String sin = null;
		private String cr_sayu = null;
		private String acptdate = null;
		
		
		private String ConfName = null;
		private String PgmSayu = null;
		private String strIsrid = null;
		private String strIsrTitle = null;
		private String cr_qrycd = null;
		private String cr_editor = null;
		private String cr_sysgb = null;
		private String cr_status = null;
		private String cr_syscd = null;
		private String Sunhang = null;
		private String cm_username = null;
		
		/*
		private String cr_sayu = null;
		private String cm_deptname = null;
		private String cm_username = null;
		private String sin = null;
		private String cr_sayu = null;
		private String acptdate = null;
		private String ConfName = null;
		private String PgmSayu = null;
		private String strIsrid = null;
		private String strIsrTitle = null;
		private String cr_qrycd = null;
		private String cr_editor = null;
		private String cr_sysgb = null;
		private String cr_status = null;
		private String cr_syscd = null;
		private String Sunhang = null;
		*/
		
		private String cr_isrid = null;
		private String cr_passcd = null;
		private String cr_prcdate = null;
		private String cr_rsrcname = null;
		
		public String getCr_rsrcname() {
			return cr_rsrcname;
		}
		public void setCr_rsrcname(String cr_rsrcname) {
			this.cr_rsrcname = cr_rsrcname;
		}
		public String getCr_passcd() {
			return cr_passcd;
		}
		public void setCr_passcd(String cr_passcd) {
			this.cr_passcd = cr_passcd;
		}
		public String getCr_isrid() {
			return cr_isrid;
		}
		public void setCr_isrid(String cr_isrid) {
			this.cr_isrid = cr_isrid;
		}
		public String getCm_username() {
			return cm_username;
		}
		public void setCm_username(String cm_username) {
			this.cm_username = cm_username;
		}
		public String getCr_prcdate() {
			return cr_prcdate;
		}
		public void setCr_prcdate(String cr_prcdate) {
			this.cr_prcdate = cr_prcdate;
		}
		public String getAcptdate() {
			return acptdate;
		}
		public void setAcptdate(String acptdate) {
			this.acptdate = acptdate;
		}
		public String getCr_editor() {
			return cr_editor;
		}
		public void setCr_editor(String cr_editor) {
			this.cr_editor = cr_editor;
		}
		public String getCr_sysgb() {
			return cr_sysgb;
		}
		public void setCr_sysgb(String cr_sysgb) {
			this.cr_sysgb = cr_sysgb;
		}
		public String getCr_status() {
			return cr_status;
		}
		public void setCr_status(String cr_status) {
			this.cr_status = cr_status;
		}
		public String getCr_syscd() {
			return cr_syscd;
		}
		public void setCr_syscd(String cr_syscd) {
			this.cr_syscd = cr_syscd;
		}

		
		public String getCr_sayu() {
			return cr_sayu;
		}
		public void setCr_sayu(String cr_sayu) {
			this.cr_sayu = cr_sayu;
		}
		public String getCr_acptno() {
			return acptno;
		}
		public void setCr_acptno(String acptno) {
			this.acptno = acptno;
		}
		//###########################################################
		//###########################################################
		//###########################################################
		//###########################################################
		//###########################################################
		//###########################################################
		
		
		
/**		
		
		//TABLE CMM0200 CM_GBNCD
		private String CM_STDATE   = null;
		private String CM_GBNCD    = null;
		
		
		private String CM_userid = null;
		
		public String getCM_STDATE() {
			return CM_STDATE;
		}
		public void setCM_STDATE(String cm_stdate) {
			CM_STDATE = cm_stdate;
		}
		
		/**
			 * toString method: creates a String representation of the object
			 * @return the String representation
			 * @author e.sale
			 */
		/**
			public String toString() {
				StringBuffer buffer = new StringBuffer();
				buffer.append("BbsVO[");
				buffer.append("CM_ACPTDATE = ").append(CM_ACPTDATE);
				buffer.append(", CM_ACPTNO = ").append(CM_ACPTNO);
				buffer.append(", CM_ANSCONT = ").append(CM_ANSCONT);
				buffer.append(", CM_ANSTIT = ").append(CM_ANSTIT);
				buffer.append(", CM_ATTFILE = ").append(CM_ATTFILE);
				buffer.append(", CM_CONTENTS = ").append(CM_CONTENTS);
				buffer.append(", CM_EDITOR = ").append(CM_EDITOR);
				buffer.append(", CM_GBNCD = ").append(CM_GBNCD);
				buffer.append(", CM_NOTIYN = ").append(CM_NOTIYN);
				buffer.append(", CM_LASTDATE = ").append(CM_LASTDATE);
				buffer.append(", CM_SEQNO = ").append(CM_SEQNO);
				buffer.append(", CM_SVFILE = ").append(CM_SVFILE);
				buffer.append(", CM_TITLE = ").append(CM_TITLE);
				buffer.append(", CM_STDATE = ").append(CM_STDATE);
				buffer.append(", CM_EDDATE = ").append(CM_EDDATE);
				buffer.append("]");
				return buffer.toString();
			}
**/		
}
