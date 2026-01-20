<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:import url="/webPage/common/common.jsp" />

<div class="contentFrame">
	<div id="history_wrap"></div>
	<div class="az_search_wrap">
		<div class="az_in_wrap">
			<div class="row">	
                <div class="dib vat width-30" style="margin-right:5px;">
                    <label class="tit_50 poa">시스템</label>
                    <div class="ml_50 vat">
						<div id="cboSys" data-ax5select="cboSys" data-ax5select-config="{size:'sm',theme:'primary',selecteditable: true}" class="dib width-100"></div>
					</div>
				</div>
				<div class="dib vat width-30" style="margin-right:5px;">
                    <label class="tit_60 poa">신청부서</label>
                    <div class="ml_60 vat">
						<div id="cboDept" data-ax5select="cboDept" data-ax5select-config="{size:'sm',theme:'primary',selecteditable: true}" class="dib width-100"></div>
					</div>
				</div>
				<div class="dib vat width-30" style="margin-right:5px;">
                    <label class="tit_60 poa">배포구분</label>
                    <div class="ml_60 vat">
						<div id="cboGbn" data-ax5select="cboGbn" data-ax5select-config="{size:'sm',theme:'primary',selecteditable: true}" class="dib margin-5-right" style="width: calc(100% - 90px)"></div>
						<input type="checkbox" class='checkbox-pie' id="chkEmg" style="width:80px;" data-label="긴급건만"></input>
					</div>
				</div>
				<div class="vat float-right">
					<button id="btnReq" class="btn_basic_s">조회</button>
				</div>
			</div>
		</div>
	</div>
	
	<div class="margin-5-bottom">
		<input id="txtSearch" name="txtSearch" type="text" style="width:200px;" placeholder="신청번호 또는 신청인을 입력하세요.">
		<button id="btnSearch" name="btnSearch" class="btn_basic_s margin-5-left">찾기</button>
	</div>
	
	<div class="az_board_basic az_board_basic_in" style="height:39%">
    	<div data-ax5grid="firstGrid" data-ax5grid-config="{showLineNumber: true, lineNumberColumnWidth: 40}" style="height:100%;" class="resize"></div>
	</div>	
	
	<div class="row">
		<div class="az_search_wrap">
			<div class="az_in_wrap">
				<div class="dib vat">
					<button id="btnAcptApply" class="btn_basic_s">신청건 선택배포</button>
				</div>
				<div class="dib float-right">
					<div class="dib vat">
						<button id="btnApply" class="btn_basic_s">선택배포</button>
					</div>
				</div>
			</div>
		</div>
	</div>
	
	<div class="az_board_basic az_board_basic_in" style="height:39%">
    	<div data-ax5grid="secondGrid" data-ax5grid-config="{showLineNumber: true, lineNumberColumnWidth: 40}" style="height:100%" class="resize"></div>
	</div>
</div>

<form name="popPam">
	<input type="hidden" name="acptno"/>
	<input type="hidden" name="user"/>
	<input type="hidden" name="rgtList"/>
	<input type="hidden" name="adminYN"/>
</form>

<c:import url="/js/ecams/common/commonscript.jsp" />
<script type="text/javascript"	src="<c:url value="/js/ecams/apply/ApplyConfig.js"/>"></script>