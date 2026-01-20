<%@page import="com.ecams.common.base.StringHelper"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<title>개발요청상세</title>
<c:import url="/webPage/common/common.jsp"/>

<%
	request.setCharacterEncoding("UTF-8");
	String userId = StringHelper.evl(request.getParameter("user"),"");
	String orderId = StringHelper.evl(request.getParameter("orderId"),"");
%>

<style>
	.write_wrap dl dt{position: absolute; width: 100px;}
	.write_wrap_100 dl dt{position: absolute; width: 100px !important;}
	.write_wrap dl dd{position: relative; margin-left: 100px;}
	.write_wrap_100 dl dd{position: relative; margin-left: 100px !important;}
	.write_wrap .half dl dt{width: 100px;}
	.write_wrap .half dl dd{margin-left: 100px;}
</style>

<body style="width: 100% !important; padding: 10px;">
	<div class="content">
		<div id="history_wrap">개발요청서 조회<strong>&gt; 개발요청상세 상세보기</strong></div>
		<div class="az_search_wrap">
			<div class="az_in_wrap">
				<!--좌측-->
				<div class="l_wrap width-49 vat write_wrap">
					<div class="row cb">
						<dl>
							<dt><label>요청번호</label></dt>
							<dd><input id="cboReqNum1" type="text" readonly="readonly"></dd>
						</dl>
					</div>
					<div class="row half cb">
						<dl>
							<dt><label>문서유형/문서번호</label></dt>
							<dd><input id="txtDocType" type="text" readonly="readonly"></dd>
						</dl>
						<dl>
							<dd style="margin-left: 5px"><input id="txtDocNum" type="text" readonly="readonly"></dd>
						</dl>
					</div>
					<div class="row half cb">
						<dl>
							<dt><label>대내-부점/요청자</label></dt>
							<dd><input id="txtDomestic" type="text" readonly="readonly"></dd>
						</dl>
						<dl>
							<dd style="margin-left: 5px"><input id="txtCaller1" type="text" readonly="readonly"></dd>
						</dl>
					</div>
					<div class="row">
						<dl>
							<dt>
								<label>주관부서-담당자</label>
							</dt>						
							<dd>
								<div class="az_board_basic scroll_h az_board_basic_in" style="height: 120px;">
							    	<div data-ax5grid="userGrid" data-ax5grid-config="{showLineNumber: true, lineNumberColumnWidth: 40}" style="height: 100%;"></div>
							    </div>
							</dd>
						</dl>
					</div>
					<div class="row">
						<dl>
							<dt><label>업무상세명</label></dt>
							<dd><input id="txtTitleDetail" type="text" readonly="readonly"></dd>
						</dl>
					</div>
					<div class="row half cb">
						<dl>
							<dt><label>업무</label></dt>
							<dd><input id="txtJob" type="text" readonly="readonly"></dd>
						</dl>
						<dl>
							<dt style="margin-left: 10px; width: 60px"><label>조치유형</label></dt>
							<dd style="margin-left: 70px"><input id="txtContype" type="text" readonly="readonly"></dd>
						</dl>
					</div>
					<div class="row">
						<dl>
							<dt><label>업무난이도</label></dt>
							<dd><input id="txtJobDiff" type="text" readonly="readonly"></dd>
						</dl>
					</div>
					<div class="row">
						<dl>
							<dt><label>개발기간</label></dt>
							<dd>
								<div class="az_input_group dib vat">
									<input id="txtStDt" type="text" readonly="readonly" style="width: 100px">
									<span class="sim">∼</span>
									<input id="txtEndDt" type="text" readonly="readonly" style="width: 100px">
								</div>
								<div class="dib vat ml_10">
									<input id="txtDays" name="txtDays" type="text" readonly="readonly" style="width: 50px" value="0" class="tar">
									<label>일</label>
								</div>
							</dd>
						</dl>
					</div>
					<div class="row">
						<dl>
							<dt><label>추가업무내용</label></dt>
							<dd>
								<form>
									<textarea id="txtDetail" style="align-content:flex-start;width:100%;height:80px;resize: none; overflow-y:auto; padding:5px;" readonly="readonly"></textarea>
								</form>
							</dd>
						</dl>
					</div>
					<div class="row">
						<dl>
							<dd>
								<div class="az_board_basic scroll_h az_board_basic_in">
							    	<section>
										<div class="container-fluid">
											<div data-ax5grid="fileGrid" data-ax5grid-config="{showLineNumber: true, lineNumberColumnWidth: 40}" style="height: 115px;"></div>
										</div>
									</section>
							    </div>
							</dd>
						</dl>
					</div>
				</div>
				
				<!--우측-->
				<div class="r_wrap width-49 vat write_wrap">
					<div class="row half cb">
						<dl>
							<dt><label>발행자</label></dt>
							<dd><input id="txtUsr" type="text" autocomplete="off" readonly="readonly"></dd>
						</dl>
					</div>
					<div class="row">
						<dl>
							<dt><label>문서제목</label></dt>
							<dd><input id="txtTitle" type="text" autocomplete="off" readonly="readonly"></dd>
						</dl>
					</div>
					<div class="row half cb">
						<dl>
							<dt><label>대외-기관/요청자</label></dt>
							<dd><input id="txtForeign" type="text" readonly="readonly"></dd>
						</dl>
						<dl>
							<dd style="margin-left: 5px"><input id="txtCaller2" type="text" readonly="readonly"></dd>
						</dl>
					</div>
					<div class="row">
						<dl>
							<dt><label>주관부서-문서번호</label></dt>
							<dd><input id="txtHandlerDocNum" type="text" autocomplete="off" readonly="readonly"></dd>
						</dl>
					</div>
					<div class="row half cb">
						<dl>
							<dt><label>요청유형/처리기간</label></dt>
							<dd><input id="txtReqtype" type="text" readonly="readonly"></dd>
						</dl>
						<dl>
							<dd style="margin-left: 5px"><input id="txtPrcDt" type="text" readonly="readonly"></dd>
						</dl>
					</div>
					<div class="row">
						<dl>
							<dt><label>업무팀</label></dt>
							<dd>
								<div class="scrollBind" id="treeDeptDiv" style="height: 240px; font-size: 12px">
				    				<ul class="ztree" id="treeDept" style="height: 100%;"></ul>
				    			</div>
							</dd>
						</dl>
					</div>
					<div style="margin-top: 150px">
						<div class="float-right">
							<button id="btnClose" name="btnClose" class="btn_basic_s">닫기</button>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>
</body>

<form name="getReqData">
	<input type="hidden" id="user" name="user" value="<%=userId%>"/>
	<input type="hidden" id="orderId" name="orderId" value="<%=orderId%>"/>
</form>

<form name="setReqData" accept-charset="utf-8">
	<input type="hidden" name="user"/>
	<input type="hidden" name="orderId"/>
</form>

<c:import url="/js/ecams/common/commonscript.jsp" />
<script type="text/javascript" src="<c:url value="/js/ecams/winpop/PopDevRequestDetail.js"/>"></script>