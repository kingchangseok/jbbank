<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.ecams.common.base.StringHelper"%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:import url="/webPage/common/common.jsp" />

<%
	request.setCharacterEncoding("UTF-8");
	String acptNo = StringHelper.evl(request.getParameter("acptno"),"");
	String userId = StringHelper.evl(request.getParameter("user"),"");
	String winSw = StringHelper.evl(request.getParameter("winSw"),"");
%>

<div class="contentFrame" id="divFrame">
	<div id="history_wrap">대여 <strong>&gt; 대여신청</strong></div>
    <!-- 검색 S-->    
	<div class="az_search_wrap">
		<div class="az_in_wrap checkout_tit">
			<div class="row por">
               	<div class="dib" style="width: calc(100% - 90px)">
                	<!-- 시스템 -->		
	                <div class="width-25 dib vat">
	                    <label id="lbSystem" class="tit_50 poa">시스템</label>
	                    <div class="ml_50 vat">
							<div id="cboSys" data-ax5select="cboSys" data-ax5select-config="{size:'sm',theme:'primary',selecteditable: true}" style="width:calc(100% - 10px);" class="dib width-100"></div>
						</div>
					</div>
					<div class="width-10 dib vat" style="min-width: 160px">
						<input type="checkbox" class='checkbox-pie' name='chkEmg' id='chkEmg' data-label="긴급">
						<input type="checkbox" class='checkbox-pie' name='chkTool' id='chkTool' data-label="영향도툴 안함">
	               	</div>
		            <div class="width-25 dib vat" id="divcommon">
		               	<label id="lblProg" class="tit_100 poa">프로그램명</label>
	                    <div class="ml_80 vat">
							<input id="txtRsrcName" name="txtRsrcName" type="text" class="width-100"></input>
						</div>	
					</div>
					<div class="width-10 dib vat ml_10" style="min-width: 170px">
						<input type="checkbox" class='checkbox-pie' name='chkUpLow' id='chkUpLow' data-label="대소문자구분">
						<input type="checkbox" class='checkbox-pie' name='chkLike' id='chkLike' data-label="Like검색" checked>
	               	</div>
		            <div class="width-20 dib vat" id="divgit" style="display:none">
		               	<label id="lblJob" class="tit_50 poa text-right">업무명</label>
	                    <div class="ml_60 vat">
							<div id="cboJob" data-ax5select="cboJob" data-ax5select-config="{size:'sm',theme:'primary',selecteditable: true}" style="width:calc(100% - 10px);" class="dib width-100"></div>
						</div>	
					</div>
					<div class="vat poa_r" id="searchBox">
						 <button id="btnGit" class="btn_basic_s" style="display:none">GITLAB검색</button>
						 <button id="btnSearch" class="btn_basic_s margin-3-left" style="width:80px;">검색</button>
					</div>
               	</div>
			</div>
		</div>
	</div>
	
	<div class="l_wrap width-20" style="height:37%">
		<div style="overflow-y: auto; height:94%; background-color: white; border: 1px solid #DDD; margin-right: 5px;">
			<ul id="treeDemo" class="ztree"></ul>
		</div>
		<div class="">
			<input type="checkbox" class='checkbox-pie' name = 'chkbox_subnode' id ='chkbox_subnode' data-label="하위폴더 포함하여 조회">
		</div>				
		<div class="dib vat margin-5-top"><label class="tit_80 poa" id="lbPath" style="display: none;"></label></div>
	</div>
	<div class="r_wrap width-80">
		<!-- 게시판 S-->
	    <div class="az_board_basic az_board_basic_in" style="height:35%">
	    	<div data-ax5grid="first-grid" data-ax5grid-config="{showLineNumber: true, lineNumberColumnWidth: 40}" id="fileGrid" style="height:100%" class="resize"></div>
		</div>	
	</div>
	
	<div style="height:20%; min-height: 183px" class="margin-5-top margin-5-bottom az_search_wrap">
		<div style="height: 100%; padding: 0 10px 0 10px">
			<div class="l_wrap width-50">
				<label id="lblNum" class="tit_100 poa">발행번호</label>
				<div class="sm-row" style="height:calc(100% - 20px);">
					<div class="az_board_basic az_board_basic_in ml_60" style="height:100%; min-height: 131px;">
				   		<div data-ax5grid="third-grid" data-ax5grid-config="{showLineNumber: true, lineNumberColumnWidth: 40, showRowSelector:true}" id="requestGrid" style="height:100%"></div>
				   	</div>
				</div>
			</div>
			<div class="r_wrap width-45">
				<div class="row">
					<label id="lblSayu" class="tit_100 poa">신청사유</label>
					<textarea id="txtSayu" name="txtSayu" class="ml_60" rows="6" cols="75" style="width: calc(100% - 180px); margin-top: 5px; padding:5px; box-sizing: border-box; border: 1px solid #ddd; background-color: #fff; font-size: 12px; resize:none; overflow-y:auto;"></textarea>
					<div class="dib vat" style="width: 105px">
						<input type="checkbox" class='checkbox-pie ml_10' name='chkOverWrite' id='chkOverWrite' data-label="덮어쓰지않기">
					</div>
				</div>
				<div class="row">
					<label id="lblDatStD" class="tit_100 poa">반환예정일</label>
					<div class="ml_60" >
						<div id="divPicker" data-ax5picker="basic" class="az_input_group dib">
							<input id="datStD" name="datStD" type="text" placeholder="yyyy/mm/dd" style="width: 80px;">
							<button id="datStD" class="btn_calendar"><i class="fa fa-calendar-o"></i></button>
						</div>
						<div class="float-right">
							<button id="btnLoadExl" class="btn_basic_s dib vat">엑셀 업로드</button>
							<button id="btnExlTmp" class="btn_basic_s dib vat margin-2-left">엑셀템플릿</button>
							<button id="btnParaSearch" class="btn_basic_s dib vat margin-2-left" style="width: 130px;">파라미터 연관조회</button>
							<button id="btnRegist" name="btnRegist" class="btn_basic_s dib vat margin-2-left" style="width:40px;">▼</button>
							<button id="btnDelete" name="btnDelete" class="btn_basic_s dib vat margin-2-left" style="width:40px;">▲</button>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>
	
    <div class="az_board_basic az_board_basic_in" style="height:calc(42% - 130px)">
    	<div data-ax5grid="second-grid" data-ax5grid-config="{showLineNumber: true, lineNumberColumnWidth: 40, showRowSelector:true}" id="requestGrid" style="height:100%" class="resize"></div>
	</div>
	
	<div class="row">
		<div class="ml_60">
			<div class='tal dib float-right'>
				<button id="btnReq" class="btn_basic_s margin-5-left">대여요청</button>
			</div>
		</div>
	</div>
</div>

<input type="hidden" id="userId" name="userId" value="<%=userId%>"/>
<input type="hidden" id="acptNo" name="acptNo" value="<%=acptNo%>"/>
<input type="hidden" id="winSw" name="winSw" value="<%=winSw%>"/>

<form name="setReqData" accept-charset="utf-8">
	<input type="hidden" name="acptno"/>
	<input type="hidden" name="user"/>
	<input type="hidden" name="syscd"/>
	<input type="hidden" name="jobcd"/>
	<input type="hidden" name="etcinfo"/>
	<input type="hidden" name="orderId"/>
</form>

<form id='ajaxform' method='post' enctype='multypart/form-data' style="display: none;">
	<input type="file" id="excelFile" name="excelFile" accept=".xls,.xlsx" accept-charset="UTF-8" />
</form>

<c:import url="/js/ecams/common/commonscript.jsp" />
<script type="text/javascript" src="<c:url value="/js/ecams/dev/CheckOut.js"/>"></script>