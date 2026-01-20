<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!-- 프로그램일괄등록  -->
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:import url="/webPage/common/common.jsp" />

<div class="contentFrame">
	<div id="history_wrap">관리자<strong>&gt; 프로그램일괄등록</strong></div>

	<div class="az_search_wrap">
		<div class="az_in_wrap">
			<div class="sm-row vat">
				<div class="dib" style="width:calc(50% - 135px);">
					<div class="tit_80 poa">
						<label>시스템</label>
					</div>
					<div class="ml_80">
						<div id="cboSysCd" data-ax5select="cboSysCd" data-ax5select-config="{size:'sm',theme:'primary', selecteditable: true}"></div>
					</div>
				</div>
				<div class="dib tar" style="width:calc(50% - 135px);">
					<div class="tit_80 poa">
						<label>대상서버</label>
					</div>
					<div class="ml_80 tal">
						<div id="cboSvrCd" data-ax5select="cboSvrCd" data-ax5select-config="{size:'sm',theme:'primary'}"></div>
					</div>
				</div>

				<div class="dib" style="vertical-align: bottom;width: 270px;">
					<div>
						<div class="vat dib float-right">
							<button id="btnLoadExl" class="btn_basic_s mw-85">엑셀파일</button>
							<button id="btnReq"  class="btn_basic_s mw-85 margin-5-left">일괄등록</button>
							<button id="btnMapping"  class="btn_basic_s mw-85 margin-5-left">맵핑</button>
						</div>
					</div>
				</div>
			</div>

			<div class="sm-row vat">
				<div class="dib margin-5-top" style="width:calc(50% - 135px);">
					<div class="ml_80" id="hbxBase">
						<input id="optBase1"  type="radio" name="optradio"  value="1"/>
						<label for="optBase1">최초등록(버전:1)</label>
						<input id="optBase2" type="radio"  name="optradio"  value="2"/>
						<label for="optBase2">목록등록(버전:0)</label>
						<!-- 
						<input id="optBase3" type="radio"  name="optradio"  value="3"/>
						<label for="optBase3">추가등록(버전:+1)</label>
						 -->
					</div>
				</div>
				<div class="dib vat" style="width:calc(50% - 135px);">
					<div class="ml_80">
						<input id="optNomal"  type="radio" name="radio"  value="normal"/>
						<label for="optNomal">정상건</label>
						<input id="optErr" type="radio"  name="radio"  value="err"/>
						<label for="optErr">오류건</label>
						<input id="optAll" type="radio"  name="radio"  value="all"/>
						<label for="optAll">전체</label>
						<input type="checkbox" class="checkbox-batch" id="chkOk" data-label="정상건만등록"/>
					</div>
				</div>

				<div class="dib vat" style="width: 270px;">
					<div class="vat dib" style="float: right;">
						<button id="btnSaveExl" class="btn_basic_s vat mw-85">엑셀저장</button>
						<button id="btnExlTmp"  class="btn_basic_s vat mw-85 margin-5-left">엑셀템플릿</button>
						<button id="btnDel"  class="btn_basic_s vat mw-85 margin-5-left">삭제</button>
					</div>
				</div>
			</div>
		</div>
	</div>

	<div class="sm-row vat">
		<div class="width-100 dib">
			<div class="az_board_basic" style="height: calc(100% - 140px);">
				<div data-ax5grid="batchGrid" data-ax5grid-config="{showLineNumber: true, lineNumberColumnWidth: 40}" style="height: 100%"></div>
			</div>
		</div>
	</div>
</div>

<form name="popPam">
	<input type="hidden" name="userId"/>
	<input type="hidden" name="userName"/>
	<input type="hidden" name="adminYN"/>
	<input type="hidden" name="strReqCD"/>
</form>
<form id='ajaxform' method='post' enctype='multypart/form-data' style="display: none;">
	<input type="file" id="excelFile" name="excelFile" accept=".xls,.xlsx" accept-charset="UTF-8" />
</form>

<c:import url="/js/ecams/common/commonscript.jsp" />
<script type="text/javascript" src="<c:url value="/js/ecams/administrator/ProgramAllRegister.js"/>"></script>
