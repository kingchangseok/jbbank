<%@page import="com.ecams.common.base.StringHelper"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<title>외주개발요청상세</title>
<c:import url="/webPage/common/common.jsp"/>

<%
	request.setCharacterEncoding("UTF-8");
	String user = StringHelper.evl(request.getParameter("user"),"");
	String orderId = StringHelper.evl(request.getParameter("orderId"),"");
%>

<body style="width: 100% !important; padding: 10px;">
	<div class="content">
		<div id="history_wrap">외주개발요청서 조회<strong>&gt; 외주개발요청서 상세보기</strong></div>
		<div class="az_search_wrap">
			<div class="az_in_wrap">
				<!--좌측-->
				<div class="l_wrap width-49 vat write_wrap">
					<div class="row cb">
						<dl>
							<dt><label>발행번호</label></dt>
							<dd><input id="txtReqNum0" type="text" autocomplete="off" readonly="readonly"></dd>
						</dl>
					</div>
					<div class="row">
						<dl>
							<dt><label>요청제목</label></dt>
							<dd><input id="txtTitle" type="text" style="width: 100%;" readonly="readonly"></dd>
						</dl>
					</div>
					<div class="row">
						<dl>
							<dt><label>상세요청내용</label></dt>
							<dd>
								<form>
									<textarea id="txtDetail" style="align-content:flex-start;width:100%;height:380px;resize: none; overflow-y:auto; padding:5px;" readonly="readonly"></textarea>
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
							<dd style="margin-left: 100px"><input id="txtUsr" type="text" class="tac" autocomplete="off" readonly="readonly"></dd>
						</dl>
					</div>
					<div class="row half cb">
						<dl>
							<dt><label>발행근거</label></dt>
							<dd style="margin-left: 100px">
								<div class="vat">
									<input id="txtCause" type="text" style="width: 100%;" readonly="readonly">
								</div>
							</dd>
						</dl>
						<dl>
							<dt style="margin-left: 10px; width: 60px"><label>처리기한</label></dt>
							<dd>
								<div class="vat">
					            	<input id="txtTime" type="text" style="width: 100%;" readonly="readonly">
					         	</div>
					        </dd>
						</dl>
					</div>
					<div class="row">
						<dl>
						</dl>
					</div>
					<div class="row">
						<dl>
							<dt style="width: 100px"><label>외주PM</label></dt>
							<dd style="margin-left: 100px">
								<div class="scrollBind" id="lstPMDiv" style="height: 200px">
				    				<ul class="list-group" id="lstPM" style="height: 100%;">
					    			</ul>
				    			</div>
							</dd>
						</dl>
					</div>
					<div class="row">
						<dl>
							<dt style="width: 100px">
								<label>주관부서-담당자</label>
								<!-- <div class="sm-row">
									<button id="btnAddDevUser" name="btnAddDevUser" class="btn_basic_s" style="margin: 0 0;">추가</button>
								</div> -->
							</dt>
							<dd style="margin-left: 100px">
								<div class="az_board_basic scroll_h az_board_basic_in" style="height: 120px;">
							    	<div data-ax5grid="userGrid" data-ax5grid-config="{showLineNumber: true, lineNumberColumnWidth: 40}" style="height: 100%;"></div>
							    </div>
							</dd>
						</dl>
					</div>
					
					<div class="row">
						<div class="float-right" style="margin-top: 145px">
							<button id="btnClose" name="btnClose" class="btn_basic_s">닫기</button>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>
</body>

<form name="getReqData">
	<input type="hidden" id="user" name="user" value="<%=user%>"/>
	<input type="hidden" id="orderId" name="orderId" value="<%=orderId%>"/>
</form>

<form name="setReqData" accept-charset="utf-8">
	<input type="hidden" name="user"/>
	<input type="hidden" name="orderId"/>
</form>

<c:import url="/js/ecams/common/commonscript.jsp" />
<script type="text/javascript" src="<c:url value="/js/ecams/winpop/PopOutSourcingDetail.js"/>"></script>