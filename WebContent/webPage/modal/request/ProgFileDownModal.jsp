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
			<label id="lbSub" class="margin-5-left">[프로그램다운로드]</label>
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
			<div class="row vat">
				<div class="width-100 dib">
					<div class="az_board_basic" style="height: calc(100% - 80px);">
						<div data-ax5grid="mainGrid" data-ax5grid-config="{lineNumberColumnWidth: 40}" style="height: 100%"></div>
					</div>
				</div>
			</div>
			<div class="row vat">
				<div class="width-100 dib vat">
					<!-- <div class="progressbar ml_10" style="display: inline-block; width:calc(100% - 175px); border: none;">
						<dl class="srdl" style="margin: 0;">
							<dd style="cursor: pointer; text-align: left; margin-left: 0px; height: 25px">
								<span class="blue" style="width: 0%; display: block;"id="percent"></span> 
								<span style="height: 25px; text-align: center; width: 100%; display: inline-block; position: absolute; top: 0px; font-size: 12px" id="percentText">0/0</span>
							</dd>
						</dl>
					</div> -->
					<!-- <label id="progBar" style="width:calc(100% - 175px)"></label> -->
					<div class="r_wrap">
						<button class="btn_basic_s margin-5-left" id="btnClose">닫기</button>
					</div>
				</div>
			</div>
		</div>
	</div>

</body>
<object id="eCAMSFile_Uc" classid="clsid:248F5217-DD94-4598-A3D1-387B9AE2B3D3" codebase="eCAMSFile.cab#version=1,0,0,92" width=0 height=0></object>

<c:import url="/js/ecams/common/commonscript.jsp" />	
<script type="text/javascript" src="<c:url value="/js/ecams/modal/request/ProgFileDownModal.js"/>"></script>
