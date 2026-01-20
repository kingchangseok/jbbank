<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:import url="/webPage/common/common.jsp" />
<style>
.sel {
	min-width: 220px;
}
.width-23 {
	width: calc(26% - 38px);
}
</style>

<!-- contener S -->
<div id="wrapper">
    <div class="contentFrame">
        <!-- history S--> 
        <div id="history_wrap">관리자 <strong>&gt; ISR강제종료</strong></div>
        <!-- history E-->    
        <!-- 검색 S-->    
		<div class="az_search_wrap">
			<div class="az_in_wrap">
				<div class="width-20 dib vat">
					<label class="tit-80 dib poa">&nbsp;요청부서</label>
					<div class="ml_80">
						<div id="cboTeam" data-ax5select="cboTeam" data-ax5select-config="{size:'sm',theme:'primary'}" style="width:100%;"></div>
                	</div>
				</div>
				<div class="width-20 dib vat margin-5-left">
                	<label class="tit-80 dib poa">&nbsp;ISR-ID/제목</label>
                	<div class="ml_80 vat">
						<input id="txtSr" type="text" placeholder="개발담당자명을 입력하세요." class="width-100" />
                	</div>
				</div>
				<div class="r_wrap margin-5-left">
					<button class="btn_basic_s" id="btnSession">강제종료</button>
					<button class="btn_basic_s margin-5-left" id="btnSearch" style="width:70px;">조회</button>
				</div>
				<div class="dib r_wrap">
					<div id="divPicker" data-ax5picker="basic" class="az_input_group dib">
						<input id="datStD" name="datStD" type="text" placeholder="yyyy/mm/dd" style="width:100px;" class="f-cal" autocomplete="off">
						<button id="btnStD" class="btn_calendar"><i class="fa fa-calendar-o"></i></button>
						<span class="sim">∼</span>
						<input id="datEdD" name="datEdD" type="text" placeholder="yyyy/mm/dd" style="width:100px;" class="f-cal" autocomplete="off">
						<button id="btnEdD" class="btn_calendar"><i class="fa fa-calendar-o"></i></button>
					</div>
				</div>	
				
			</div>
		</div>
		
		<div class="az_board_basic" style="height:85%">
	    	<div id="mainGrid" data-ax5grid="mainGrid" data-ax5grid-config="{}" style="width:100%; height: 100%;" class="resize">
		
			</div>
		</div>
    </div>
</div>

<c:import url="/js/ecams/common/commonscript.jsp" />
<script type="text/javascript" src="<c:url value="/js/ecams/administrator/SRCompleteByForce.js"/>"></script>