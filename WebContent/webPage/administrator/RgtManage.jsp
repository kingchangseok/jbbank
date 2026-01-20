<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:import url="/webPage/common/common.jsp" />

<div class="contentFrame">
	<div id="history_wrap">관리자 <strong>&gt; 권한관리</strong></div>
	
	<div class="half_wrap_cb">
		<div class="l_wrap width-50">
			<div class="margin-5-right">				   
				<div class="az_search_wrap">
					<div class="az_in_wrap">
						<div class="por">	
							<div class="vat">
			                    <label id="lbUser" class="tit_80 poa">직무구분</label>
			                    <div class="ml_80">
			                    	<div class="width-50">
										<div id="cboDuty" data-ax5select="cboDuty" data-ax5select-config="{size:'sm',theme:'primary',selecteditable: true}" style="width:100%;"></div>
			                    	</div>
								    <div class="dib poa_r margin-3-top">
										<input type="checkbox" class="checkbox-rgt" id="chkAll" data-label="전체선택"/>
									</div>
								</div>
							</div>
						</div>	
					</div>
				</div>
				<!--검색E-->
				<div class="scrollBind row" style="height:85%;">
    				<ul id="dutyUlInfo" class="list-group"></ul>
    			</div>
    		</div>
		</div>
		<div class="r_wrap width-50">
			<div class="margin-5-left">		   
		        <!-- 검색 S-->    
				<div class="az_search_wrap">
					<div class="az_in_wrap">
						<div class="por">	
							<div class="vat">
			                    <label class="tit_80 poa">메뉴체계</label>
							</div>
							<div class="tar margin-10-right">
								<div class="vat dib margin-5-left">
									<span class="fa_wrap" id="btnPlus"><i class="fas fa-plus"></i></span> 
									<span class="fa_wrap margin-5-left" id="btnMinus"><i class="fas fa-minus"></i></span>
								</div>
								<div class="vat dib margin-5-left">
									<button class="btn_basic_s" id="btnReq">적용</button>
								</div>
							</div>
						</div>	
					</div>
				</div>
				<!--검색E-->
				<div class="scrollBind row" style="height:85%;">
    				<ul id="tvMenu" class="ztree"></ul>
    			</div>
			</div>
		</div>
	</div>
</div>
<c:import url="/js/ecams/common/commonscript.jsp" />	
<script type="text/javascript" src="<c:url value="/js/ecams/administrator/RgtManage.js"/>"></script>