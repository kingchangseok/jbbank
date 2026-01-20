<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:import url="/webPage/common/common.jsp" />



<div id="history_wrap">보고서 <strong>&gt; 프로그램 변경내역</strong></div>
<div class="az_search_wrap sm-row">
	<div class="az_in_wrap por">
		<div class="width-28 dib">
			<label class="tit_80 poa">시스템</label>
			<div class="ml_80">
				<div id="cboSysCd" data-ax5select="cboSysCd" data-ax5select-config="{size:'sm',theme:'primary'}" style="width:calc(100% - 10px);" ></div>
			</div>
		</div>
		<div class="width-28 dib vat">
			<label class="tit_80 poa margin-40-left">접수파트</label>
			<div class="ml_80">
				<div id="cboDept" data-ax5select="cboDept" data-ax5select-config="{size:'sm',theme:'primary'}" class="dib width-88 margin-20-left"></div>
			</div>	
		</div>
		<div class="width-44 dib vat">
			<label class="tit_80 poa margin-40-left">자체보완</label>
			<div class="ml_80">
				<input id="optSel" name="opt" type="radio" value="optSel" checked="checked" style="margin-left: 20px; margin-top: 5px;"/> 
				<label for="optSel" style="margin-right: 10px;">선택</label> 
				<input id="optDel" name="opt"  type="radio" value="optDel" style="margin-top: 5px;"/> 
				<label for="optDel">제외</label> 	
			</div>	
		</div>
		<div class="sm-row por">
			<div class="vat width-100 dib">
				<label class="tit_80 poa">ISR-ID/제목</label>
				<div class="ml_80">
					<input id="txtIsr" name="txtIsr" type="text" class="width-52">

				</div>
			</div>
			<div class="poa_r">
				<div id="divPicker" data-ax5picker="basic" class="az_input_group dib" style="margin-right: 20px;">
				   <input id="dateSt" name="start_date" type="text" placeholder="yyyy/mm/dd" style="width:100px;">
				   <button id="btnSt" class="btn_calendar"><i class="fa fa-calendar-o"></i></button>
				   <span class="sim">&sim;</span>
				   <input id="dateEd" name="end_date" type="text" placeholder="yyyy/mm/dd" style="width:100px;">
				   <button id="btnEd" class="btn_calendar"><i class="fa fa-calendar-o"></i></button>
				</div>
				<button id="btnQry" class="btn_basic_s" style="margin-right:5px;">조회</button>
				<button id="btnExcel" class="btn_basic_s" style="margin-right:8px;">엑셀저장</button>
			</div>
		</div>
	</div>
</div>

<div class="az_board_basic" style="height:89%">
	<div id="mainGrid" data-ax5grid="mainGrid" data-ax5grid-config="{}" style="height: 100%;" class="resize"></div>
</div>

<form name="popPam">
	<input type="hidden" id="acptNo" name="acptNo"/>
	<input type="hidden" id="reqCd" name="reqCd"/>
	<input type="hidden" id="isrId" name="isrId"/>
	<input type="hidden" id="user" name="user"/>
</form>

 <c:import url="/js/ecams/common/commonscript.jsp" />
<script type="text/javascript" src="<c:url value="/js/ecams/report/SrcRevisionReport.js"/>"></script>