<%@page import="com.ecams.common.base.StringHelper"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:import url="/webPage/common/common.jsp" />

<%
	String userId = StringHelper.evl(request.getParameter("userId"),"");
%>

<style>
	.fontStyle-43err {color: green;}	/*43 테이블 등록 실패 */
	.fontStyle-44err {color: magenta;}	/*44 테이블 등록 실패 */
	.fontStyle-4344err {color: cyan;}	/*43 ,44  테이블 등록 실패 */
	.fontStyle-async {color: blue;}		/*동기화 제외 대상 */
</style>

<body>
	<div class="pop-header">
		<div>
			<label> 관리자 ㅣ 사용자일괄등록</label>
		</div>
	</div>
	<div class="container-fluid pop_wrap">
		<!--line1-->					
		<div class="half_wrap_cb">
			<!--left wrap-->
			<div class="l_wrap width-50">
				<div class="margin-5-right">
					<input id="optAll"  type="radio" name="radio"  value="all"/>
					<label for="optAll" >전체</label>
					<input id="optNomal" type="radio"  name="radio"  value="normal"/>
					<label for="optNomal">정상</label>
					<input id="optError" type="radio"  name="radio"  value="error"/>
					<label for="optError">장애</label>
				</div>
		 	</div>
		 	<!--right wrap-->
		 	<div class="r_wrap width-50">
		 		<div class="margin-5-left">
					<!--button-->
					<div class="vat dib poa_r">
						<button id="btnExcelOpen" class="btn_basic_s">엑셀열기</button>
						<button id="btnCellAd" class="btn_basic_s margin-5-left" data-grid-control="row-add">셀추가</button>
						<button id="btnExcel" class="btn_basic_s margin-5-left">엑셀저장</button>
						<button id="btnDbSave" class="btn_basic_s margin-5-left">디비저장</button>
					</div>
				</div>
			</div>
		</div>
		<!--line2-->				
		<div class="row">
			<div class="az_board_basic" style="height: 88%">
		    	<div data-ax5grid="signUpGrid" data-ax5grid-config="{showLineNumber: false, lineNumberColumnWidth: 40}" style="height: 100%;"></div>
			</div>
		</div>
	</div>
</body>	

<input type="hidden" id="userId" name="userId" value="<%=userId%>"/>
<form id='ajaxform' method='post' enctype='multypart/form-data' style="display: none;">
	<input type="file" id="excelFile" name="excelFile" accept=".xls,.xlsx" accept-charset="UTF-8" />
</form>

<c:import url="/js/ecams/common/commonscript.jsp" />	
<script type="text/javascript" src="<c:url value="/js/ecams/winpop/PopAllSignUp.js"/>"></script>