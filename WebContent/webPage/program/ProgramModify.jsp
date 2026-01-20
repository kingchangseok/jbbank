<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:import url="/webPage/common/common.jsp" />

<div class="contentFrame">
	<div id="history_wrap"></div>
		<div class="az_search_wrap">
			<div class="az_in_wrap sr_status">
				<div class="l_wrap dib" style="width:calc(100% - 160px);">
	                <div class="por">
	                	<div class="width-33 dib vat">
		                	<label class="tit-60 dib poa">시스템</label>
		                	<div class="ml_60">
			                    <div id="cboSys" data-ax5select="cboSys" data-ax5select-config="{size:'sm',theme:'primary'}" style="width:100%;"></div>
		                	</div>
						</div>
						<div class="width-33 dib vat">
							<label class="tit-150 dib poa">&nbsp;&nbsp;&nbsp;프로그램종류</label>
							<div class="ml_100">
								<div id="cboRsrc" data-ax5select="cboRsrc" data-ax5select-config="{size:'sm',theme:'primary'}" style="width:100%;"></div>
							</div>
						</div>	
						<div class="width-33 dib vat">
		                	<label class="tit-150 dib poa">&nbsp;&nbsp;&nbsp;프로그램명</label>
		                	<div class="ml_80">
								<input id="txtRsrcName" type="text" placeholder="프로그램명을 입력하세요." class="width-100" />
		                	</div>
						</div>
					</div>
				</div>
				<div class="r_wrap">
					<div class="vat dib">
						<div class="vat dib margin-5-left">
							<button id="btnQry" name="btnQry" class="btn_basic_s">조회</button>
						</div>
						<div class="vat dib margin-5-left">
							<button id="btnExcel" name="btnExcel" class="btn_basic_s" data-grid-control="excel-export" style="margin-left: 0px; margin-right: 0px;">엑셀저장</button>
						</div>
					</div>
				</div>
			</div>
		</div>
	
	    <div class="az_board_basic" style="height: 65%;">
		    <div data-ax5grid="modifyGrid" data-ax5grid-config="{showLineNumber: true, lineNumberColumnWidth: 40}" style="height: 100%" class="resize"></div>
	    </div>

		<div class="half_wrap" style="margin-top:10px;">
			<div class="row">
				<div class="l_wrap width-50" >
	               	<label class="tit-150 dib poa">프로그램종류</label>
	               	<div class="ml_100">
	                    <div id="cboRsrcType" data-ax5select="cboRsrcType" data-ax5select-config="{size:'sm',theme:'primary'}" style="width:100%;"></div>
	               	</div>
				</div>
				<div class="r_wrap width-50">
					<label class="tit-150 dib poa">&nbsp;&nbsp;&nbsp;업무</label>
					<div class="ml_100">
						<div id="cboJob" data-ax5select="cboJob" data-ax5select-config="{size:'sm',theme:'primary'}" style="width:100%;"></div>
					</div>
				</div>
			</div>
			<div class="row">
				<div class="l_wrap width-100" style="margin-top:3px;">
	               	<label class="tit-150 dib poa">프로그램경로</label>
	               	<div class="ml_100">
	                    <div id="cboDir" data-ax5select="cboDir" data-ax5select-config="{size:'sm',theme:'primary'}" style="width:100%;"></div>
	               	</div>
				</div>
			</div>
			<div class="row">
				<div class="l_wrap" style="width:calc(100% - 130px);margin-top:3px;">
	               	<label class="tit-150 dib poa">ISR정보</label>
	               	<div class="ml_100">
	                    <div id="cboISRID" data-ax5select="cboISRID" data-ax5select-config="{size:'sm',theme:'primary'}" style="width:100%;"></div>
	               	</div>
				</div>
				<div class="r_wrap" style="margin-top:3px;">
					<div class="vat dib">
						<div class="vat dib">
							<button id="btnModify" name="btnModify" class="btn_basic_s" style="width:54px;">수정</button>
						</div>
					</div>
				</div>
			</div>
			<div class="row">
				<div class="l_wrap" style="width:calc(100% - 130px);margin-top:7px;">
					<p class="txt_r font_12">**신규 또는 운영중인 프로그램에 대해서만 수정가능</p>
				</div>
			</div>
		</div>
</div>

<c:import url="/js/ecams/common/commonscript.jsp" />
<script type="text/javascript" src="<c:url value="/js/ecams/program/ProgramModify.js"/>"></script>