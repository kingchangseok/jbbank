<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:import url="/webPage/common/common.jsp" />

<style>
.width-19 {
    width: calc(70% - 54%);
}
.pic-wid {
   width: 120px;
}

</style>

<!-- contener S -->
<div id="wrapper">
    <div class="contentFrame">
        <!-- history S-->
        <div id="history_wrap">결재확인 <strong>&gt; 적용대상현황</strong></div>
     	<div class="az_search_wrap">
			<div class="az_in_wrap">
				<div class="por">
					<!--시스템S-->
					<div class="width-25 dib">
						<label class="tit_60 poa">시스템</label>
						<div class="ml_60">
							<div id="systemSel" data-ax5select="systemSel" data-ax5select-config="{size:'sm',theme:'primary',selecteditable: true}" class="width-100"></div>
						</div>
					</div>
					<!-- <div class="width-19 dib vat">
						<label class="tit_80 poa margin-10-left">업무</label>
						<div class="ml_50">
							<div id="cboJobSel" data-ax5select="cboJobSel" data-ax5select-config="{size:'sm',theme:'primary',selecteditable: true}" class="width-100"></div>
						</div>
					</div>
					<div class="dib vat width-19"style="margin-left: 6px;">
						<label class="tit_80 poa">진행상태</label>
						<div class="ml_60">
							<div id="cboSta" data-ax5select="cboSta" data-ax5select-config="{size:'sm',theme:'primary'}" class="width-100"></div>
						</div>
					</div> -->
					<div class="width-25 dib vat">
						<label class="tit_60 poa margin-10-left">신청인</label>
						<div class="ml_60">
							<input id="reqUserId" data-ax-path="reqUserId" type="text" class="width-100" />
						</div>
					</div>
					<div class="dib vat" style="width: 180px">
						<label class="tit_60 poa margin-10-left">배포구분</label>
						<div class="ml_60 dib vat" style="border: 1px solid #ddd; padding-left: 5px;">
							<input id="radioCkOut" name="radioGroup" tabindex="8" type="radio" value="optCkOut" checked="checked"/>
			            	<label for="radioCkOut" style="margin-right: 10px;">Real</label>
			            	<input id="radioCkIn" name="radioGroup" tabindex="8" type="radio" value="optCkIn"/>
                 		 	<label for="radioCkIn">Test</label>
						</div>
					</div>
					<div class="dib vat" style="width: calc(50% - 190px); text-align: right; min-width: 490px">
						<label class="tit_50" style="margin-left: 0px;">신청일</label>
						<div id="divPicker" class="az_input_group dib" data-ax5picker="basic">
							<input id="dateSt" name="dateSt" type="text"placeholder="yyyy/mm/dd" style="width: 100px;"> 
							<span class="btn_calendar"><i class="fa fa-calendar-o"></i></span> 
							<span class="sim">∼</span>
							<input id="dateEd" name="dateEd"type="text" placeholder="yyyy/mm/dd" style="width: 100px;">
							<span class="btn_calendar"><i class="fa fa-calendar-o"></i></span>
						</div>	
						<button class="btn_basic_s margin-5-right margin-5-left" id="btnSearch">조회</button>
						<button class="btn_basic_s" id="btnExcel">엑셀저장</button>
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
   <input type="hidden" name="acptno"/>
   <input type="hidden" name="user"/>
</form>
<c:import url="/js/ecams/common/commonscript.jsp" />
<script type="text/javascript" src="<c:url value="/js/ecams/approval/ApplyListStatus.js"/>"></script>