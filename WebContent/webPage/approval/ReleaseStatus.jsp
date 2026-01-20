<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:import url="/webPage/common/common.jsp" />

<style>
.width-75 {
	width: calc(97% - 100px);
}

.wrap_width {
	width: calc(98% - 280px);
}
</style>



<div id="wrapper">
    <div class="contentFrame">
        <!-- history S-->
        <div id="history_wrap">릴리즈관리 <strong>&gt; 릴리즈대상현황</strong></div>
        <!-- history E-->         
	    <!-- 검색 S-->    
		<div class="az_search_wrap">
			<div class="az_in_wrap">
			
				<div class="l_wrap wrap_width dib">
	                <div class="por">
	                	<div class="width-33 dib">
		                	<label class="dib" style="width:49px;">시스템</label>
		                    <div id="CboSysCd" data-ax5select="CboSysCd" data-ax5select-config="{size:'sm',theme:'primary'}" class="width-75 dib tal">
						    </div>
						</div>
						<div class="width-33 dib">
		                	<label class="tit_60">적용구분</label>
							<div id="cboDeploy" data-ax5select="cboDeploy" data-ax5select-config="{size:'sm',theme:'primary'}" class="width-75 dib tal">
						    </div>
						</div>
						<div class="width-33 dib vat">
		                	<label class="tit_80">신청인</label>
							<input id="TxtUserId" type="text" class="width-75" />
						</div>
					</div>

					<div class="row">
						<div class="width-33 dib">
							<label class="dib" style="width:49px;">업무</label>
							<div id="CboJobCd" data-ax5select="CboJobCd" data-ax5select-config="{size:'sm',theme:'primary'}" class="width-75 dib tal">
								
						    </div>
						</div>	
						<div class="width-33 dib vat">
		                	<label class="tit_60">진행상태</label>
							<div id="CboGbnCd" data-ax5select="CboGbnCd" data-ax5select-config="{size:'sm',theme:'primary'}" class="width-75 dib tal">
							</div>
						</div>
						<div class="width-33 dib vat">
		                	<label class="tit_80">프로그램ID</label>
							<input id="TxtPgmID" type="text" class="width-75 " />
						</div>
					</div>
					
					<label id="lblMsg" style="color: blue; margin-top: 5px;">** 형상관리담당자 이외에는 동일한 부서에서 신청한 내역만 조회됩니다.</label>
					
				</div>
				
				<div class="row tar">
					<div id="divPicker" data-ax5picker="basic" class="az_input_group dib">
						<input id="dateSt" name="start_date" type="text" placeholder="yyyy/mm/dd" style="width:92px;">
						<span class="btn_calendar"><i class="fa fa-calendar-o"></i></span>
						<span class="sim">&sim;</span>
						<input id="dateEd" name="end_date" type="text" placeholder="yyyy/mm/dd" style="width:92px;">
						<span class="btn_calendar"><i class="fa fa-calendar-o"></i></span>
					</div>
				</div>
				<div class="r_wrap poa_r vat" style="top:5px; width: 299px;">
					<div class="height-30 tar margin-right-50">
						<div class="vat dib margin-5-left">
							<button class="btn_basic_s" id="btnExcel">엑셀저장</button>
						</div>
						<div class="vat dib margin-5-left">
							<button class="btn_basic_s" id="btnQry">조회</button>
						</div>
					</div>
				</div>
			</div>
		</div>

	    <div class="az_board_basic" style="height:100%">
	    	<div id="mainGrid" data-ax5grid="mainGrid" data-ax5grid-config="{}" style="width:100%; height: 100%;" class="resize">
		
			</div>
		</div>	
	</div>
</div>
<form name="popPam">
	<input type="hidden" id="user" name="user"/>
	<input type="hidden" id="qyrcd" name="qrycd"/>
	<input type="hidden" id="acptno" name="acptno"/>
</form>
<c:import url="/js/ecams/common/commonscript.jsp" />
<script type="text/javascript" src="<c:url value="/js/ecams/approval/ReleaseStatus.js"/>"></script>