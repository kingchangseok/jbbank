<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:import url="/webPage/common/common.jsp" />

<div class="contentFrame">
    <!-- history S-->
	<div id="history_wrap">
		관리자 <strong>&gt; 프로그램유형정보</strong>
	</div>
	
	<div class="az_search_wrap">
		<div class="az_in_wrap">
			<div class="l_wrap width-60">
				<label>시스템</label>
				<div class="width-30 vat dib">
					<div id="cboSys" data-ax5select="cboSys" data-ax5select-config="{size:'sm',theme:'primary',selecteditable: true}" style="width: 100%"></div> 
				</div>
				<div class="dib vat margin-5-left">
					<input id="optQry"  type="radio" name="radio" value="1"/>
					<label for="optQry">조회</label>
					<input id="optChk" type="radio" name="radio" value="2"/>
					<label for="optChk">점검</label>
				</div>
			</div>	
		</div>
	</div>
	
	<div class="az_board_basic" style="height: 48%">
		<div data-ax5grid="prgGrid" data-ax5grid-config="{showLineNumber: false, lineNumberColumnWidth: 40}" style="height: 100%"></div>
	</div>
	<div class="az_board_basic margin-10-top" style="height: 40%">
		<div data-ax5grid="cmdGrid" data-ax5grid-config="{showLineNumber: false, lineNumberColumnWidth: 40}" style="height: 100%"></div>
	</div>
</div>


<c:import url="/js/ecams/common/commonscript.jsp" />
<script type="text/javascript" src="<c:url value="/js/ecams/administrator/ProgramPatternInfo.js"/>"></script>