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
								<div id="cboBaseRsrc" data-ax5select="cboBaseRsrc" data-ax5select-config="{size:'sm',theme:'primary'}" style="width:100%;"></div>
							</div>
						</div>	
						<div class="width-33 dib vat">
		                	<label class="tit-150 dib poa">&nbsp;&nbsp;&nbsp;프로그램명</label>
		                	<div class="ml_100">
								<input id="txtProg" type="text" placeholder="프로그램명을 입력하세요." class="width-100" />
		                	</div>
						</div>
					</div>
				</div>
				<div class="r_wrap">
					<div class="vat dib">
						<div class="vat dib margin-5-left">
							<button id="btnQry" name="btnQry" class="btn_basic_s">조회</button>
						</div>
					</div>
				</div>
			</div>
		</div>
	
	    <div class="az_board_basic" style="height: 65%;">
		    <div data-ax5grid="buildGrid" data-ax5grid-config="{showLineNumber: true, lineNumberColumnWidth: 40}" style="height: 100%" class="resize"></div>
	    </div>

		<div class="half_wrap" style="margin-top:10px;">
			<div class="row">
				<div class="l_wrap width-30" >
	               	<label class="tit-150 dib poa">실행구분</label>
	               	<div class="ml_100">
	                    <div id="cboGbnCd" data-ax5select="cboGbnCd" data-ax5select-config="{size:'sm',theme:'primary'}" style="width:100%;"></div>
	               	</div>
				</div>
				<div class="r_wrap width-70">
					<label id="lblCmd" class="tit-150 dib poa">&nbsp;&nbsp;&nbsp;빌드커맨드</label>
					<div class="ml_100">
						<input id="txtCmd" type="text" placeholder="" class="width-100" />
					</div>
				</div>
			</div>
			<div class="row">
				<div class="l_wrap width-50" style="width:calc(100% - 130px);margin-top:3px;">
	               	<label class="tit-150 dib poa">프로그램종류</label>
	               	<div class="ml_100">
	                    <div id="cboRsrcCd" data-ax5select="cboRsrcCd" data-ax5select-config="{size:'sm',theme:'primary'}" class="width-71 dib tal"></div>
						<input id="txtExeName" type="text" style="width:calc(29% - 5px);" />
	               	</div>
				</div>
				<div class="r_wrap width-50" style="width:calc(100% - 130px);margin-top:3px;">
					<label class="tit-150 dib poa">&nbsp;&nbsp;&nbsp;빌드후모듈명</label>
					<div class="ml_100">
						<input id="txtMod" type="text" placeholder="" class="width-100" />
					</div>
				</div>
			</div>
			<div class="row">
				<div class="l_wrap" style="width:calc(100% - 130px);margin-top:3px;">
	               	<label class="tit-150 dib poa">빌드모듈경로</label>
	               	<div class="ml_100">
	                    <div id="cboDir" data-ax5select="cboDir" data-ax5select-config="{size:'sm',theme:'primary'}" style="width:100%;"></div>
	               	</div>
				</div>
				<div class="r_wrap" style="margin-top:3px;">
					<div class="vat dib">
						<div class="vat dib">
							<button id="btnReg" name="btnReg" class="btn_basic_s" style="width:54px;">등록</button>
						</div>
					</div>
				</div>
				<div class="r_wrap" style="margin-top:3px;">
					<div class="vat dib">
						<div class="vat dib">
							<button id="btnDel" name="btnDel" class="btn_basic_s" style="width:54px;">삭제</button>
						</div>
					</div>
				</div>
			</div>
		</div>
</div>

<c:import url="/js/ecams/common/commonscript.jsp" />
<script type="text/javascript" src="<c:url value="/js/ecams/program/CustomBuildInfo.js"/>"></script>