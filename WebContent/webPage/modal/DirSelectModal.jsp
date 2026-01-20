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
			<label id="lbSub" class="margin-5-left">[로컬디렉토리 선택]</label>
		</div>
		<div>
			<button type="button" class="close" aria-label="닫기" onclick="popClose();">
			  <span aria-hidden="true">&times;</span>
			</button>
		</div> 
	</div>
	<div style="padding: 5px;">
		<div class="container-fluid">
			<!--CM_TITLE -->
			<div class="row">
				<div class="tit_80 poa">
					<label>드라이브</label>
				</div>
				<div class="ml_80">
					<div id="cboDrive" data-ax5select="cboDrive" data-ax5select-config="{size:'sm',theme:'primary'}"></div>
				</div>
			</div>
			<div class="row">
				<div>
					<div class="scrollBind" style="height: calc(100% - 130px);">
						<ul class="list-group ztree" id="treeDir">
						</ul>
					</div>
				</div>
			</div>
		</div>
		<div class="margin-5-top">
			<label class="poa">디렉토리선택 후 더블클릭하면 선택이 완료됩니다.</label>
			<div class="margin-5-left">
				<div class="tar">
					<button class="btn_basic_s" id="cmdExit">닫기</button>
				</div>
			</div>
		</div>
	</div>
</body>
<c:import url="/js/ecams/common/commonscript.jsp" />	
<script type="text/javascript" src="<c:url value="/js/ecams/modal/DirSelectModal.js"/>"></script>