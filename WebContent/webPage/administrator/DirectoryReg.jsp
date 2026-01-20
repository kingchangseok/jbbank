<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:import url="/webPage/common/common.jsp" />

<div class="contentFrame">	
	<div id="history_wrap">관리자<strong>&gt; 디렉토리등록</strong></div>
             
	<div class="half_wrap">
		<div class="l_wrap dib width-38 vat">
			<label class="tit_60 dib">시스템</label>
			<div class="dib vat width-90">
				<div id="cboSysCd" data-ax5select="cboSysCd" data-ax5select-config="{size:'sm',theme:'primary',selecteditable: true}"></div> 
			</div>
			
			<div class="margin-10-top">
				<label class="tit_60 dib">Directory</label>
				<div class="dib vat width-90">
					<input id="txtPath" name="txtPath" type="text" class="width-100">
				</div>
			</div>
		</div>
			
		<div class="r_wrap dib width-60 vat">
			<label class="tit_80 dib">프로그램종류</label>
			<div class="dib margin-3-top" style="float: right;">
				<input type="checkbox" class="checkbox-dir" id="chkAllPrg" data-label="전체선택"/>
			</div>
	
			<div class="width-100 dib" style="height: 203px; border: 1px dotted gray;; background-color: white; overflow-y: auto;">
				<ul class="list-group" id="ulPrgInfo"></ul>
			</div>
			
			<div class="row dib vat">
				<p class="txt_r font_12">[등록순서]등록/수정할 내용을 입력 후 추가버튼을 목록에 추가한 후 등록버튼을 클릭하여 주십시오.</p>
				<p class="txt_r font_12">[참고사항]업무영역이나 프로그램종류만 수정하는 경우 Directory란은 Clear한 후 추가하여 주십시오.</p>
			</div>
		</div>
				
		<div class="l_wrap dib width-38 vat margin-10-top">
			<label class="tit_80 dib">업무</label>
			<div class="dib" style="width: calc(100% - 90px)">
				<div class="margin-3-top" style="float: right;">
					<input type="checkbox" class="checkbox-dir float-right" id="chkAllJob" data-label="전체선택"/>
				</div>
			</div>
			<div class="dib" style="height: 200px; border: 1px dotted gray; background-color: white; overflow-y: auto; width: 100%">
				<ul class="list-group" id="ulJobInfo"></ul>
			</div>
		</div>
			
		<div class="r_wrap dib width-60 vat margin-10-top">
			<button id="btnExcel" class="btn_basic_s float-right">엑셀저장</button>
			<button id="btnAllReg" class="btn_basic_s float-right margin-5-right">일괄등록</button>
			<button id="btnReq" class="btn_basic_s float-right margin-5-right">등록</button>
			<button id="btnAdd" class="btn_basic_s float-right margin-5-right">추가</button>
			<button id="btnQry" class="btn_basic_s float-right margin-5-right">조회</button>
			<button id="btnDel" class="btn_basic_s float-right margin-5-right hide">삭제</button>
		</div>
			
	</div>
	<div class="az_board_basic margin-10-top" style="height: 50%;">
		<div data-ax5grid="dirGrid" data-ax5grid-config="{showLineNumber: false, lineNumberColumnWidth: 40}" style="height: 100%" class="resize"></div>
	</div>
</div>
<c:import url="/js/ecams/common/commonscript.jsp" />
<script type="text/javascript" src="<c:url value="/js/ecams/administrator/DirectoryReg.js"/>"></script>
	