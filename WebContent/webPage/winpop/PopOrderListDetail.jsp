<%@page import="com.ecams.common.base.StringHelper"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<title>업무지시서 상세</title>
<c:import url="/webPage/common/common.jsp"/>

<%
	request.setCharacterEncoding("UTF-8");
	String userId = StringHelper.evl(request.getParameter("user"),"");
	String orderId = StringHelper.evl(request.getParameter("orderId"),"");
%>

<body style="width: 100% !important; padding: 10px;">
	<div class="content">
		<div id="history_wrap">업무지시서 조회<strong>&gt; 업무지시서 상세보기</strong></div>
		<div class="az_search_wrap" style="height: 96%">
			<div class="az_in_wrap">
				<!--좌측-->
				<div class="l_wrap width-54 vat write_wrap">
					<div class="row cb">
						<dl>
							<dt><label>요청번호</label></dt>
							<dd><input id="txtTitl0" type="text" autocomplete="off" readonly="readonly"></dd>
						</dl>
					</div>
					<div class="row">
						<dl>
							<dt><label>발행번호</label></dt>
							<dd><input id="txtOrderNum0" type="text" style="width: 100%;" readonly="readonly"></dd>
						</dl>
					</div>
					<div class="row">
						<dl>
							<dt><label>요청제목</label></dt>
							<dd><input id="txtTitle" name="txtTitle" type="text" style="width:100%;" readonly="readonly"></dd>
						</dl>
					</div>
					<div class="row">
						<dl>
							<dt><label>상세요청내용</label></dt>
							<dd>
								<form>
									<textarea id="txtDetail" style="align-content:flex-start;width:100%;height:350px;resize: none; overflow-y:auto; padding:5px;" readonly="readonly"></textarea>
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
				<div class="r_wrap width-45 vat write_wrap">
					<div class="row half cb">
						<dl>
							<dt><label>발행자</label></dt>
							<dd><input id="txtUsr" type="text" autocomplete="off" readonly="readonly"></dd>
						</dl>
					</div>
					<div class="row half cb">
						<dl>
							<dt><label>발행근거</label></dt>
							<dd>
								<div class="vat">
									<input id="txtCause" type="text" style="width: 100%;" readonly="readonly">
								</div>
							</dd>
						</dl>
						<dl>
							<dt style="margin-left: 10px; width: 60px"><label>처리기한</label></dt>
							<dd>
								<div class="vat">
					            	<input id="txtPrcdate" type="text" style="width: 100%;" readonly="readonly">
					         	</div>
					        </dd>
						</dl>
					</div>
					<div class="row">
						<dl>
							<dt>
								<label>담당자</label>
							</dt>						
							<dd>
								<div class="az_board_basic scroll_h az_board_basic_in" style="height: 160px;">
							    	<div data-ax5grid="userGrid" data-ax5grid-config="{showLineNumber: true, lineNumberColumnWidth: 40}" style="height: 100%;"></div>
							    </div>
							</dd>
						</dl>
					</div>
					<div class="row">
						<dl>
							<dt>
								<label>3자지정</label>
							</dt>						
							<dd>
								<div class="az_board_basic scroll_h az_board_basic_in" style="height: 160px;">
							    	<div data-ax5grid="thirdUserGrid" data-ax5grid-config="{showLineNumber: true, lineNumberColumnWidth: 40}" style="height: 100%;"></div>
							    </div>
							</dd>
						</dl>
					</div>
					
					<div class="row">
						<div class="float-right" style="margin-top: 145px">
							<button id="btnReqView" name="btnReqView" class="btn_basic_s" disabled="disabled">개발요청서보기</button>
							<button id="btnClose" name="btnClose" class="btn_basic_s margin-5-left">닫기</button>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>
</body>

<form name="getReqData">
	<input type="hidden" id="user" name="userId" value="<%=userId%>"/>
	<input type="hidden" id="orderId" name="orderId" value="<%=orderId%>"/>
</form>

<form name="setReqData" accept-charset="utf-8">
	<input type="hidden" name="user"/>
	<input type="hidden" name="orderId"/>
</form>

<c:import url="/js/ecams/common/commonscript.jsp" />
<script type="text/javascript" src="<c:url value="/js/ecams/winpop/PopOrderListDetail.js"/>"></script>