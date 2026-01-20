<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:import url="/webPage/common/common.jsp" />

<div class="az_search_wrap">
	<div class="az_in_wrap">
		<div class="row vat">
			<div class="width-100 dib">
				<div class="ml_100">
					<label class="fontStyle-ing">[등록대상] 하나의 디렉토리에 파일이 너무 많아서 파일대사 시 하나의 디렉토리에 대해서 별도로 파일을 생성해야 하는 경우 등록</label>
				</div>
			</div>
		</div>
	
		<div class="row vat">
			<div class="width-95 dib">
				<div class="tit_100 poa">
					<label>디렉토리</label>
				</div>
				<div class="ml_100">
					<input id="txtEtcDir" name="txtEtcDir" type="text" class="width-100">
				</div>
			</div>
			
			<div class="vat dib" style="float: right;">
				<button id="btEtcReq" name="btEtcReq" class="btn_basic_s">등록</button>
			</div>
		</div>
		
		<div class="row vat">
			<div class="width-100 dib">
				<div class="ml_100">
					<div class="az_board_basic" style="height: 20%;">
						<div data-ax5grid="dirGrid" data-ax5grid-config="{showLineNumber: false, lineNumberColumnWidth: 40}" style="height: 100%" class="frameResize"></div>
					</div>
				</div>
			</div>
		</div>
		
		<div class="row vat">
			<div class="width-100 dib">
				<div class="tit_100 poa">
					<label>예외디렉토리</label>
				</div>
				<div class="ml_100">
					<div class="az_board_basic" style="height: 20%;">
						<div data-ax5grid="dirEtcGrid" data-ax5grid-config="{showLineNumber: false, lineNumberColumnWidth: 40}" style="height: 100%" class="frameResize"></div>
					</div>
				</div>
			</div>
		</div>
		
		<div class="row vat">
			<div class="width-100 dib">
				<div class="vat dib" style="float: right;">
					<button id="btEtcDel" name="btEtcDel" class="btn_basic_s">삭제</button>
				</div>
			</div>
		</div>
	</div>
</div>



<c:import url="/js/ecams/common/commonscript.jsp" />	
<script type="text/javascript" src="<c:url value="/js/ecams/tab/fileconfiguration/ExceptionDirInfoTab.js"/>"></script>