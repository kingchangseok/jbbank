<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:import url="/webPage/common/common.jsp" />

<div class="contentFrame">	
	<!-- history S-->
	<div id="history_wrap">관리자<strong>&gt; Agent모니터링</strong></div>
       
	<div class="az_search_wrap">
		<div class="az_in_wrap">
			<div class="l_wrap width-70">
				<label class="tit_40">시스템</label>
				<div class="width-30 dib">
					<div id="cboSysCd" data-ax5select="cboSysCd" data-ax5select-config="{size:'sm',theme:'primary',selecteditable: true}" style="width: 100%"></div> 
				</div>
				<div class="dib vat margin-5-left">
					<input id="optAll" type="radio" name="radio" value="1"/>
					<label for="optAll">전체</label>
					<input id="optNormal" type="radio" name="radio" value="2"/>
					<label for="optNormal">정상</label>
					<input id="optErr" type="radio"  name="radio" value="3"/>
					<label for="optErr">장애</label>
				</div>
			</div>	
			<div class="r_wrap">
				<div class="vat dib">
					<!-- <button id="btnCheck" class="btn_basic_s" style="cursor: pointer;">Agent체크</button> -->
					<button id="btnQry" class="btn_basic_s margin-5-left" style="cursor: pointer;">조회</button>
					<button id="btnExcel" class="btn_basic_s margin-5-left" style="cursor: pointer;">엑셀저장</button>
				</div>
			</div>
		</div>
	</div>
	<div class="az_board_basic" style="height: 85%;">
		<div data-ax5grid="agentGrid" data-ax5grid-config="{showLineNumber: true, lineNumberColumnWidth: 40}" style="height: 100%" class="resize"></div>
	</div>
</div>

<c:import url="/js/ecams/common/commonscript.jsp" />
<script type="text/javascript" src="<c:url value="/js/ecams/administrator/AgentMornitoring.js"/>"></script>
	