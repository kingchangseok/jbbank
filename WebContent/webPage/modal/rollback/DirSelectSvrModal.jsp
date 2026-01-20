<!--  
	* 화면명: 디렉토리 선택 모달
	* 화면호출: 롤백 이전버전 체크 후 신청
-->
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:import url="/webPage/common/common.jsp" />

<body style="width: 100% !important; min-width: 0px !important">
	
	<div class="pop-header">
		<div>
			<label id="lbSub" class="margin-5-left">[디렉토리 선택]</label>
		</div>
		<div>
			<button type="button" class="close" aria-label="닫기" onclick="popClose();">
			  <span aria-hidden="true">&times;</span>
			</button>
		</div> 
	</div>
	<div style="padding: 5px;">
		<div class="container-fluid">
			<div class="row">
				<div class="dib vat por width-48">
					<label class="poa tit_40">시스템</label>
					<div class="ml_50">
						<input type="text" id="txtSys" style="width: calc(100% - 10px)" readonly/>
					</div>
				</div>
				<div class="dib vat width-50">
					<label class="poa tit_60">프로그램명</label>
					<div class="ml_70">
						<input type="text" id="txtProg" class="width-100" />
					</div>
				</div>
			</div>
			<div class="row">
				<input type="checkbox" id="chksubdir" class="checkbox" data-label="하위폴더포함하여조회"/>
			</div>
			<div class="row">
				<div>
					<div class="scrollBind" style="height: calc(100% - 150px);">
						<ul class="list-group ztree" id="treeDir">
						</ul>
					</div>
				</div>
			</div>
			<div class="row dib vat">
				<label id="lblDir" style="display: none;"></label>
			</div>
		</div>
		<div class="">
			<label class="poa">디렉토리선택 후 더블클릭하면 선택이 완료됩니다.</label>
			<div class="margin-5-left">
				<div class="tar">
					<button class="btn_basic_s" id="cmdExit" >닫기</button>
				</div>
			</div>
		</div>
	</div>
</body>
<c:import url="/js/ecams/common/commonscript.jsp" />	
<script type="text/javascript" src="<c:url value="/js/ecams/modal/rollback/DirSelectSvrModal.js"/>"></script>