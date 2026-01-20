<%@page import="com.ecams.common.base.StringHelper"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:import url="/webPage/common/common.jsp" />

<%
	request.setCharacterEncoding("UTF-8");
	String userId = StringHelper.evl(request.getParameter("user"),"");
	String acptNo = StringHelper.evl(request.getParameter("acptno"),"");
%>

<style>
.wrap_width {
	width: calc(98% - 280px);
}
</style>

<div id="wrapper">
	<div class="contentFrame">
		<div id="history_wrap">
			결재 <strong>&gt; 영향분석확인 상세조회</strong>
		</div>
		<div class="az_search_wrap">
			<div class="az_in_wrap">
				<div class="row por">
					<!--시스템S-->
					<div class="width-20 dib">
						<label class="tit_80 poa">진행상태</label>
						<div class="ml_80">
							<div id="cboSta" data-ax5select="cboSta" data-ax5select-config="{size:'sm',theme:'primary',selecteditable: true}" class="width-100"></div>
						</div>
					</div>
					<div class="width-20 dib vat">
						<label class="tit_80 poa margin-10-left">조치여부</label>
						<div class="ml_80">
							<div id="cboSta2" data-ax5select="cboSta2" data-ax5select-config="{size:'sm',theme:'primary'}" class="width-100"></div>
						</div>
					</div>
					<div class="width-25 dib vat">
						<label class="tit_110 poa margin-10-left">확인대상자부서</label>
						<div class="ml_110">
							<div id="cboDept" data-ax5select="cboDept" data-ax5select-config="{size:'sm',theme:'primary'}" class="width-100"></div>
						</div>
					</div>
					<div class="float-right dib">
						<button class="btn_basic_s" id="btnExcel" style="width: 70px; margin-right: 5px;">엑셀저장</button>
						<button class="btn_basic_s" id="btnQry" style="width: 70px;">조회</button>
					</div>
				</div>
				<div class="row">
					<div class="width-20 dib vat">
						<!-- <input type="checkbox" class="checkbox-file" id="chkAll" data-label="전체선택" /> -->
					</div>
					<div class="width-20 dib vat">
						<input id="txtSys" name="txtSys" type="text" style="display: none;">
						<input id="txtAcptNo" name="txtAcptNo" type="text" style="display: none;">
					</div>
					<div class="float-right dib">
						<button class="btn_basic_s" id="btnRegist" style="width: 70px; margin-right: 5px;">등록</button>
						<button class="btn_basic_s" id="btnClose" style="width: 70px;">닫기</button>
					</div>
				</div>
			</div>
		</div>
		<div class="az_board_basic" style="height: 100%">
			<div id="astaGrid" data-ax5grid="astaGrid" data-ax5grid-config="{}" style="width: 100%; height: 85%;" class="resize" ></div>
		</div>
	</div>
</div>

<form name="getReqData">
	<input type="hidden" id="userId" name="userId" value="<%=userId%>"/>
	<input type="hidden" id="acptno" name="acptno" value="<%=acptNo%>"/>
</form>

<form name="popPam">
	<input type="hidden" name="acptno" /> 
	<input type="hidden" name="user" />
	<input type="hidden" name="itemid" />
</form>

<c:import url="/js/ecams/common/commonscript.jsp" />
<script type="text/javascript"
	src="<c:url value="/js/ecams/winpop/PopAnalysisDetail.js"/>"></script>
