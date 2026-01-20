<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:import url="/webPage/common/common.jsp" />

<style>
.width-75 {
	width: calc(97% - 100px);
}

.wrap_width {
	width: calc(100% - 85px);
}
</style>

<!-- contener S -->
<div id="wrapper">
    <div class="contentFrame">
        <!-- history S-->
        <div id="history_wrap">보고서 <strong>&gt; 변경신청현황</strong></div>
        <!-- history E-->         
	    <!-- 검색 S-->    
		<div class="az_search_wrap">
			<div class="az_in_wrap">
				<div class="l_wrap wrap_width dib">
	                <div class="por">
	                	<!--시스템S-->
	                	<div class="width-23 dib">
		                	<label class="tit_80 poa">시스템</label>
		                	<div class="ml_80">
			                    <div id="cboSys" data-ax5select="cboSys" data-ax5select-config="{size:'sm',theme:'primary',selecteditable: true}" class="width-100"></div>
						    </div>
						</div>
						<div class="width-24 dib">
							<label class="tit_80 poa margin-10-left">업무</label>
							<div class="ml_80">							
								<div id="cboJob" data-ax5select="cboJob" data-ax5select-config="{size:'sm',theme:'primary',selecteditable: true}" class="width-100"></div>
							</div>
						</div>	
						<div class="width-51 dib vat margin-10-left">
		                	<label class="tit_80 poa">신청일</label>
							<div id="divPicker" class="az_input_group dib ml_60"data-ax5picker="basic">
								<input id="dateSt" name="dateSt" type="text"placeholder="yyyy/mm/dd" style="width: 100px;"> <span class="btn_calendar"><i class="fa fa-calendar-o"></i></span> 
								<span class="sim">∼</span>
								<input id="dateEd" name="dateEd" type="text"placeholder="yyyy/mm/dd"style="width: 100px;"> <span class="btn_calendar"><i class="fa fa-calendar-o"></i></span>
							</div>
						</div>
						<!--line2-->
						<div class="row">
							<div class="width-23 dib">
			                	<label class="tit_80 poa">신청구분</label>
			                	<div class="ml_80">
									<div id="cboSin" data-ax5select="cboSin" data-ax5select-config="{size:'sm',theme:'primary'}" class="width-100"></div>
								</div>
							</div>
							<div class="width-24 dib">
								<label class="tit_80 poa margin-10-left">신청부서</label>
			                	<div class="ml_80">
									<div id="cboDept" data-ax5select="cboDept" data-ax5select-config="{size:'sm',theme:'primary'}" class="width-100"></div>
								</div>
							</div>
						</div>
						<!--line3-->
						<div class="row">
							<div class="width-23 dib">
								<label class="tit_80 poa">프로그램명</label>
								<div class="ml_80">
									<input class="width-100 dib" id="txtPrg" type="text"></input>
								</div>
							</div>
							<div class="width-24 dib">
								<label id="lbSys" class="tit_80 poa margin-10-left">신청인</label>
								<div class="ml_80">
									<input class="width-100 dib" id="txtUser" type="text"></input>
								</div>
							</div>
							<div class="width-15 dib vat margin-10-left">
								<input id="rdoOpt1" type="radio" name="rdoOpt" value="0" checked="checked" /> <label for="rdoOpt1">전체</label> 
								<input id="rdoOpt2" type="radio" name="rdoOpt" value="1" /> <label for="rdoOpt2">미완료</label>
								<input id="rdoOpt3" type="radio" name="rdoOpt" value="2" /> <label for="rdoOpt3">반려/회수</label>
							</div>
						</div>		
					</div>
				</div>
				<div class="r_wrap poa_r vat" style="top:5px;">
	            	<div class="dib tar vat float-right">
	                    	<button class="btn_basic_s" id="btnSearch" style="width: 70px;">조회</button>
	                    <div class="row">
							<button id="btnSaveExl" class="btn_basic_s">엑셀저장</button>
	                    </div>
					</div>
				</div>
			</div>
		</div>
	    <!-- 게시판 S-->
	    <div class="az_board_basic" style="height:80%">
	    	<div id="mainGrid" data-ax5grid="mainGrid" data-ax5grid-config="{}" style="width:100%; height: 100%;" class="resize"></div>
		</div>	
	</div>
</div>
<form name="popPam">
	<input type="hidden" name="acptno"/>
	<input type="hidden" name="user"/>
</form>
<c:import url="/js/ecams/common/commonscript.jsp" />
<script type="text/javascript"src="<c:url value="/js/ecams/report/NotRealApplyList.js"/>"></script>