<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:import url="/webPage/common/common.jsp" />

<style id="style">
.listLi {
	height: 25px;
}

.listLi h4 {
	width: max-content;
}
</style>

<div class="contentFrame">
	<div id="history_wrap">관리자 <strong>&gt; 권한관리</strong></div>
	
	<div class="half_wrap_cb">
		<div class="az_search_wrap">
			<div class="az_in_wrap">
				<div class="por">	
					<div class="vat">
	                    <label id="lbUser" class="tit_80 poa">대구분</label>
	                    <div class="ml_80">
	                    	<div class="width-50">
								<div id="cboGbn" data-ax5select="cboGbn" data-ax5select-config="{size:'sm',theme:'primary'}" class="width-25 dib"></div>
								<div class="width-20 dib vat margin-5-left">
									<button class="btn_basic_s" id="btnSearch">조회</button>
								</div>
	                    	</div>
						</div>
					</div>
				</div>	
			</div>
		</div>
		<div class="l_wrap width-50">
			<div class="margin-5-right">	
				<div class="margin-10-right">
					<label id="lbUser" class="tit_120">체크리스트 항목 등록</label>
					<div class="float-right vat dib margin-5-left">
						<span class="fa_wrap" id="btnPlus"><i class="fas fa-plus"></i></span>
						<span class="fa_wrap margin-5-left" id="btnMinus"><i class="fas fa-minus"></i></span>
					</div>
				</div>			   
				<!--검색E-->
				<div class="row" style="height: calc(100% - 140px); border: 1px solid #ddd; overflow: auto;">
    				<ul id="cboTree" class="ztree" style="white-space: pre-line; "></ul>
    			</div>
    		</div>
		</div>
		<div class="dib width-45 vat">
			<div class="margin-5-left">		   
		        <!-- 검색 S-->    
				<div class="margin-10-right">
					<label id="lbUser" class="tit_120">체크리스트 순서 편집</label>
				</div>
				<!--검색E-->
				<div class="row" style="height: calc(100% - 140px); border: 1px solid #ddd; overflow: hidden;">
    				<ul id="stepList">
    					
    				</ul>
    			</div>
			</div>
		</div>
		<div class="dib width-4 tac" style="height: calc(100% - 110px);">
			<div class="poa" style="top: 50%; width: 4%;">
				<div class="width-70 tac" style="margin: 0 auto;">
					<button class="btn_basic_s vat" id="upBtn">▲</button>				
					<button class="btn_basic_s vab" id="downBtn">▼</button>				
				</div>
				<div class="width-100 dib margin-20-top" style="white-space: nowrap;">
					<button class="btn_basic_s" id="btnReq">적용</button>
				</div>
			</div>
		</div>
	</div>
	
	<div id="rMenu">
	<ul>
		<li id="m_add1">항목추가(선택한 항목과 동일한 레벨)</li>
		<li id="m_add2">항목추가(선택한 항목의 하위레벨)</li>
		<li id="m_change">항목명바꾸기</li>
		<li id="m_del">항목삭제</li>
	</ul>
	</div>
</div>
<c:import url="/js/ecams/common/commonscript.jsp" />	
<script type="text/javascript" src="<c:url value="/js/ecams/administrator/ChecklistReg.js"/>"></script>