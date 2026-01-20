<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:import url="/webPage/common/common.jsp" />
<style>
/* .sel {
	min-width: 220px;
}
.width-23 {
	width: calc(26% - 38px);
} */
.borderBox{
	padding: 0 5px; 
	border: 1px solid #ddd;
}
</style>

<!-- contener S -->
<div id="wrapper">
    <div class="contentFrame">
        <!-- history S--> 
        <div id="history_wrap"></div>
        <!-- history E-->    
        <!-- 검색 S-->    
		<div class="az_search_wrap">
			<div class="az_in_wrap">
				<div class="por">					
					<!-- 시스템 -->
		     		<div style="width: calc(100% - 150px);" class="dib">	
					<div class="width-22 dib vat">
					    <div class="margin-5-top margin-5-bottom">
		                  	<label id="lbSys" class="text-left tit_80 poa">구분</label>
							<div class="ml_40">
								<div id="cboFind" data-ax5select="cboFind" data-ax5select-config="{size:'sm',theme:'primary'}" class="width-100 dib tal"></div>
								</div>
							</div>
						</div>
					<div class="width-22 dib margin-20-left">
						<div class="margin-5-top margin-5-bottom">
		                  	<label id="lbSys" class="text-left tit_80 poa">상세</label>
							<div class="ml_40">
								<div id="cboDetail" data-ax5select="cboDetail" data-ax5select-config="{size:'sm',theme:'primary'}" class="width-100 dib tal"></div>
								</div>
							</div>
						</div>
					</div>
				<!--버튼 -->
				<div class="dib tar vat float-right">
				<div class="margin-5-top margin-5-bottom">
						<button class="btn_basic_s" id="btnSearch" style="width:70px; ">조회</button>
						<button class="btn_basic_s" data-grid-control="excel-export" id="btnExcel" style="width:70px; margin-left: 5px;">엑셀저장</button>
	            		</div>
					</div>
				</div>
			</div>
		</div>
		
		<div class="az_board_basic" style="height:80%">
	    	<div id="mainGrid" data-ax5grid="mainGrid" data-ax5grid-config="{}" style="width:100%; height: 100%;" class="resize">
			</div>
		</div>
	</div>	
    </div>

<form name="popPam">
	<input type="hidden" name="acptno"/>
	<input type="hidden" name="user"/>
</form>
<c:import url="/js/ecams/common/commonscript.jsp" />
<script type="text/javascript" src="<c:url value="/js/ecams/report/UserConfigReport.js"/>"></script>