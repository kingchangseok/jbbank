<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!-- 2.My결재선설정 -->
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:import url="/webPage/common/common.jsp" />

<body style="height: 100% !important">

	<div class="half_wrap_cb">
		<div class="l_wrap width-40">
			<div class="margin-5-right">
				<div class="scrollBind row" style="height:90%; border: 1px dotted gray;">
					<ul id="treeMenu" class="ztree"></ul>
    			</div>
			</div>
		</div>
		<div class="r_wrap width-60">
			<div class="margin-5-left">
				<div>
					<div class="l_wrap" style="width: 130px;">
		                <div class="dib vat">
							<input id="rdoName" type="radio" name="rdoFind" value="0" checked="checked" style="margin-top:3px;"/>
							<label for="rdoName" data-lang="REQDAY">성명</label>
							<input id="rdoDept" type="radio" name="rdoFind" value="1" style="margin-top:3px;"/>
							<label for="rdoDept" data-lang="ENDDAY">부서명</label>
						</div>
					</div>
					<div class="r_wrap" style="width: calc(100% - 135px);">
						<div class="width-100 dib vat">
		                    <div class="tal">
								<input id="txtFind" name="txtFind" class="han-input" type="text" style="margin-left:10px;width:calc(100% - 90px);" placeholder="검색단어를 입력하세요."></input>
							</div>
							<div class="vat poa_r">
								 <button id="btnSearch" class="btn_basic_s" style="width:70px;">검색</button>
							</div>
						</div>
					</div>
				</div>
				<div class="row az_board_basic" style="height: calc(45% - 50px);">
					<div data-ax5grid="findGrid" data-ax5grid-config="{showLineNumber: true, lineNumberColumnWidth: 40}" style="height: 100%"></div>
				</div>
				<div class="row" style="margin-top:10px;">
					<div class="dib vat" style="width: calc(100% - 160px);">
						<div class="l_wrap width-60">
			               	<label class="tit-150 dib poa">결재선구분</label>
			               	<div class="ml_80">
								<div id="Cbo_SignNM" data-ax5select="Cbo_SignNM" data-ax5select-config="{size:'sm',theme:'primary'}" style="width:90%;"></div>
			               	</div>
						</div>
						<div class="r_wrap width-40" id="signNmBox">
			               	<label class="tit-150 dib poa">결재선명칭</label>
			               	<div class="ml_80">
								<input id="txtApprovalName" type="text" class="width-100"/>
			               	</div>
						</div>
					</div>
					<div class="dib vat float-right">
						<button id="upBtn" class="btn_basic_s" style="width:70px;">추가</button>
						<button id="downBtn" class="btn_basic_s" style="width:70px;">제거</button>
					</div>
				</div>
				<div class="row az_board_basic" style="height:  calc(45% - 50px);">
					<div data-ax5grid="approvalGrid" data-ax5grid-config="{showLineNumber: true, lineNumberColumnWidth: 40}" style="height: 100%"></div>
				</div>
				<div class="row" style="margin-top:10px;">
					<div class="l_wrap width-50">
		               	<label class="tit-150 dib poa" style="margin-top: -1px;">결재선 순서조정</label>
		               	<div class="ml_100">
							<button id="upBtn2" name="btnRegist" class="btn_basic_s" style="width:40px;">▲</button>
							<button id="downBtn2" name="btnDelete" class="btn_basic_s" style="width:40px;">▼</button>
						</div>
					</div>
					<div class="r_wrap width-50">
						<div class="width-100 dib vat">
							<div class="vat poa_r">
								<button id="btnReg" name="btnRegist" class="btn_basic_s" style="width:70px;">등록</button>
								<button id="btnDel" name="btnDelete" class="btn_basic_s" style="width:70px;">삭제</button>
							</div>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>

</body>
<c:import url="/js/ecams/common/commonscript.jsp" />
<script type="text/javascript" src="<c:url value="/js/ecams/tab/userconfig/MyApprovalTab.js"/>"></script>