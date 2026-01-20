<!-- 파일대사사유등록 -->
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:import url="/webPage/common/common.jsp" />

<div class="contentFrame">
	<div id="history_wrap">보고서 <strong>&gt; 파일대사사유등록</strong></div>
	<div class="az_search_wrap sm-row">
		<div class="az_in_wrap por">
		
			<div class="por">
				<div class="width-20 dib">
					<label class="tit_80">적용구분</label>
					<div id="Cbo_gubun" data-ax5select="Cbo_gubun" data-ax5select-config="{size:'sm',theme:'primary'}" class="width-60 dib tal"></div>
				</div>
				<div class="width-30 dib vat">
					<label class="tit_80">시스템</label> 
					<div id="Cbo_SysCd" data-ax5select="Cbo_SysCd" data-ax5select-config="{size:'sm',theme:'primary'}" class="width-67 dib tal"></div>
				</div>
				<div class="width-25 dib vat">
					<label class="tit_80">업무</label> 
					<div id="Cbo_JobCd" data-ax5select="Cbo_JobCd" data-ax5select-config="{size:'sm',theme:'primary'}" class="width-60 dib tal"></div>
				</div>
				<div class="width-25 dib vat">
					<label class="tit_80">프로그램종류</label> 
					<div id="Cbo_rsrc" data-ax5select="Cbo_rsrc" data-ax5select-config="{size:'sm',theme:'primary'}" class="width-60 dib tal"></div>
				</div>
			</div>

			<div class="row">
				<div class="width-50 dib">
					<label class="tit_80">프로그램명</label>
					<input id="Txt_pgmname" type="text" class="width-80" />
				</div>
				<div class="width-50 dib vat">
					<label class="tit_80 poa">프로그램경로</label> 
			        <div class="ml_90" id="divDirTxt">
						<input id="Txt_dir" name="Txt_dir" type="text" class="width-100" readonly>
					</div>
			        <div class="ml_90" id="divDirCbo">
					    <div id="Cbo_dir" data-ax5select="Cbo_dir" data-ax5select-config="{size:'sm',theme:'primary'}" class="dib" style="width:100%;" ></div>
					</div>
				</div>
			</div>

			<div class="row">
				<div class="width-50 dib">
					<label class="tit_80">사유구분</label>
					<div id="Cbo_Sayu" data-ax5select="Cbo_Sayu" data-ax5select-config="{size:'sm',theme:'primary'}" class="width-80 dib tal"></div>
				</div>
				<div class="width-50 dib vat">
					<label style="color: red; margin-right: 13px;">(※개발서버 디렉토리 기준으로 입력하여 주십시오.)</label> 
				</div>
			</div>

			<div class="row">
				<div class="width-50 dib">
					<label class="tit_80">등록사유</label>
					<input id="Txt_Sayu" type="text" class="width-80" />
				</div>
				<div class="width-50 dib vat">
					<div class="r_wrap">
					<input id="delCheck" type="checkbox" style="margin-top: 5px;"/> 
					<label for="delCheck" style="margin-right: 20px;">삭제 제외</label>
					<button id="btnExcel" class="btn_basic_s" style="margin-right: 2px;">엑셀저장</button>
					<button id="btnInsert" class="btn_basic_s" style="margin-right: 2px;">등록</button>
					<button id="btnDel" class="btn_basic_s" style="margin-right: 2px;">삭제</button>
					<button id="btnQry" class="btn_basic_s" style="margin-right: 20px;">조회</button>
					</div>
				</div>
			</div>

		</div>
	</div>

	<div class="az_board_basic" style="height: 75%; margin-top: 5px;">
		<div id="mainGrid" data-ax5grid="mainGrid" data-ax5grid-config="{}" style="height: 100%;" class="frameResize"></div>
	</div>
	
</div>

<c:import url="/js/ecams/common/commonscript.jsp" />
<script type="text/javascript" src="<c:url value="/js/ecams/report/FileReasonRegist.js"/>"></script>