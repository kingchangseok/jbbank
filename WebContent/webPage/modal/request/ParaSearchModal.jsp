<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:import url="/webPage/common/common.jsp" />

<body style="width: 100% !important; min-width: 820px !important">
	<div class="pop-header">
		<div>
			<label id="lbSub">파라미터 연관조회</label>
		</div>
		<div>
			<button type="button" class="close" aria-label="닫기" onclick="popClose()">
			  <span aria-hidden="true">&times;</span>
			</button>
		</div> 
	</div>
	<div class="container-fluid pop_wrap">
		<div class="row half_wrap_cb">
			<div class="width-100">
				<div class="row">
					<div class="az_board_basic az_board_basic_in" style="height:85%">
						<div data-ax5grid="firstGrid" data-ax5grid-config="{showLineNumber: true, lineNumberColumnWidth: 40}" style="height:100%"></div>
					</div>
				</div>
			</div>
			<div class="row">
				<div class="width-100 dib vat" style="margin-top: 5px">
					<div class="vat dib float-right">
						<button id=btnSubmit class="btn_basic_s">대여요청</button>
						<button id="btnClose" class="btn_basic_s">닫기</button>
					</div>
				</div>
			</div>
		</div>
	</div>
</body>	

<c:import url="/js/ecams/common/commonscript.jsp" />	
<script type="text/javascript" src="<c:url value="/js/ecams/modal/request/ParaSearchModal.js"/>"></script>