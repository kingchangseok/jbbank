<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:import url="/webPage/common/common.jsp" />

<body style="width: 100% !important; min-width: 0px !important">
	<div class="pop-header">
		<div>
			<label>[산출물 등록]</label>
		</div>
	</div>
	<div>
		<button type="button" class="close" aria-label="닫기" onclick="popClose(false)">
		  <span aria-hidden="true">&times;</span>
		</button>
	</div> 
	<div class="container-fluid pop_wrap">
		<div class="az_search_wrap">
			<div class="az_in_wrap">
				<label class="poa">프로젝트</label>
				<div class="ml_70 vat">
					<div id="cboProject" data-ax5select="cboProject" data-ax5select-config="{size:'sm',theme:'primary',selecteditable: true}" style="width:calc(100% - 10px);" class="dib width-100"></div>
				</div>
			</div>
		</div>
		<div class="r_wrap width-70">
			<div class="width-40 dib vat">
				<label class="poa">프로그램명</label>
				<div class="ml_70 vat">
					<input id="txtSearch" style="width:calc(100% - 50px);" type="text"/>
				</div>
			</div>
			<div class="dib vat" style="width: calc(60% - 4px);">
				<label class="poa">프로그램설명</label>
				<div class="ml_80 vat">
					<input id="txtSearch" style="width:100%;" type="text"/>
				</div>
			</div>
			<div class="row az_board_basic" style="height:74%">
				<div data-ax5grid="projectGrid" data-ax5grid-config="{showLineNumber: true, lineNumberColumnWidth: 40}" style="height: 100%;"></div>
			</div>
			<div class="float-left dib margin-5-top">
				<button id="btnConfirm" class="btn_basic_s">전체 문서등록</button>
			</div>
			<div class="float-right dib margin-5-top">
				<button id="btnConfirm" class="btn_basic_s">산출물신규등록</button>
				<button id="btnCncl" class="btn_basic_s">닫기</button>
			</div>
		</div>
		<div class="l_wrap width-30">
			<div style="overflow-y: auto; height:84%; background-color: white; border: 1px solid #DDD; margin-right: 5px;">
				<ul id="treeDemo" class="ztree"></ul>
			</div>
		</div>
	</div>
</body>	

<c:import url="/js/ecams/common/commonscript.jsp" />
<script type="text/javascript" src="<c:url value="/js/ecams/modal/AddProjectModal.js"/>"></script>