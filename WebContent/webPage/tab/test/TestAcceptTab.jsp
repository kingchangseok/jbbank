<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:import url="/webPage/common/common.jsp" />


<div class="contentFrame">
	<div id="history_wrap"></div>
	
	<div class="az_search_wrap">
		<div class="az_in_wrap sr_status">
			<div class="row">
				<div class="width-30 dib vat">
					<label class="tit-150 dib poa">&nbsp;&nbsp;&nbsp;요청자테스트참여</label>
					<div class="ml_100">
						<input id="txtTestYn" type="text" class="width-70 margin-15-left" />
					</div>
				</div>
				<div class="width-30 dib vat">
					<label class="tit-150 dib poa">테스트회차</label>
					<div class="ml_100">
						<div id="cboTest" data-ax5select="cboTest" data-ax5select-config="{size:'sm',theme:'primary'}" style="width: 70%;"></div>
					</div>
				</div>
			</div>
		</div>
	</div>

	<div class="az_board_basic" style="height: 65%;">
		<div data-ax5grid="testTabGrid" data-ax5grid-config="{showLineNumber: true, lineNumberColumnWidth: 40}" style="height: 100%" class="resize"></div>
	</div>

	<div class="half_wrap" style="margin-top: 10px;">
		<div class="row">
			<div class="dib vat margin-10-right">
				<label>&nbsp;&nbsp;&nbsp;테스트기한</label>
			</div>
			<div id="picker" data-ax5picker="picker" class="az_input_group dib">
				<div class="dib margin-5-right">
					<input id="date" name="date" type="text" placeholder="yyyy/mm/dd" style="width: 150px;" autocomplete="off">
					<button id="btnDate" class="btn_calendar"><i class="fa fa-calendar-o"></i></button>
				</div>
			</div>
			<span><input type="time" id="txtTime" name="txtTime" value=""></span>
			<button id="btnReg" name="btnReg" class="btn_basic_s" style="width: 54px; margin-left: 5px">등록</button>
		</div>
	</div>
	
</div>

<form name="popPam">
	<input type="hidden" id="isr" name="isr"/>
	<input type="hidden" id="user" name="user"/>
</form>


<c:import url="/js/ecams/common/commonscript.jsp" />
<script type="text/javascript" src="<c:url value="/js/ecams/tab/test/TestAcceptTab.js"/>"></script>