<!--  
	* 화면명: 체크리스트항목등록 모달
	* 화면호출: 체크리스트 메뉴트리 우클릭 -> 항목등록/수정
-->
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:import url="/webPage/common/common.jsp" />

<body style="width: 100% !important; min-width: 0px !important">
	
	<div class="pop-header">
		<div>
			<label id="lbSub" class="margin-5-left">체크리스트 항목 추가/변경</label>
		</div>
		<div>
			<button type="button" class="close" aria-label="닫기" onclick="btnClose_Click()">
			  <span aria-hidden="true">&times;</span>
			</button>
		</div> 
	</div>
	<div style="padding: 5px;">
		<div class="container-fluid">
			<!--CM_TITLE -->
			<div class="row">
				<div>
					<textarea id="content" name="content" class="form-control" rows="12" style="resize:none; overflow-y:auto;"></textarea>
				</div>
			</div>
		</div>
		<div class="">
			<div class="margin-5-top">
				<div>
					<input id="optbefYN"  type="radio" name="radio" value="0" style="height: 25px;"/>
					<label for="optbefYN">사전점검</label>
					<input id="optaftYN" type="radio"  name="radio" value="1"/>
					<label for="optaftYN">사후점검</label>
				</div>
				<div class="margin-5-left">
					<label class="tit_80">기본값</label>
					<div class="width-30 dib">
						<div id="cboDefault" data-ax5select="cboDefault" data-ax5select-config="{size:'sm',theme:'primary'}" style="width: 100%"></div>
					</div>
					
					<div class="tar">
						<button class="btn_basic_s" id="btnQry">등록</button>
						<button class="btn_basic_s" id="btnCancel">취소</button>
					</div>
				</div>
			</div>
		</div>
	</div>
</body>
<c:import url="/js/ecams/common/commonscript.jsp" />	
<script type="text/javascript" src="<c:url value="/js/ecams/modal/checklist/checklistModal.js"/>"></script>
