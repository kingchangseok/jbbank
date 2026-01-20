<!--  
	* 화면명: 프로그램종류정보
	* 화면호출: 시스템정보 -> 프로그램종류정보 클릭
-->
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:import url="/webPage/common/common.jsp" />

<div>	
	<div class="sm-row" style="font-size:0px;">			
		<div class="dib width-30 vat">
			<label id="lbSub" class="poa tit_80">프로그램종류</label>
			<div class="ml_80 margin-5-right">
				<div id="cboJawon" data-ax5select="cboJawon" data-ax5select-config="{size:'sm',theme:'primary',selecteditable: true}" style="width:100%;" ></div>
			</div>
		</div>
		<div class="dib width-15 vat">
			<label id="lbSub" class="poa tit_80" style="margin-left:5px;">버전관리수</label>
			<div class="ml_80">
				<input id="txtVer" class="dib" type="text" style="margin-left:5px; height: 25px; width: calc(100% - 10px);">
			</div>
		</div>
		<div class="dib width-15 vat">
			<label id="lbSub" class="poa tit_130" style="margin-left:5px;">정기배포시간(HHMM)</label>
			<div class="ml_130">
				<input id="txtTime" class="dib" style="margin-left:10px; height: 25px;  width: calc(100% - 10px);">
			</div>
		</div>
	</div>
	
	<div class="sm-row">
		<div class="half_wrap_cb">
			<!--처리속성선택-->
			<div class="l_wrap width-30">
				<div class="margin-5-right" style="margin-top:5px;">
					<div class="width-100 dib vat">
						<label id="lbUser" class="title">[처리속성선택]</label>
					</div>
					<div class="scrollBind" style="margin-top:3px;height: calc(100% - 70px);">
						<ul id="tvInfo" class="ztree"></ul>
					</div>
				</div>
			</div>
		
			<!--대상확장자-->
			<div class="r_wrap width-70">
				<div class="margin-5-left">
					<div class="sm-row">
						<label id="lbSub" class="poa tit_80">대상확장자</label>
						<div class="ml_80">
							<input id="txtExename" class="dib width-100" type="text">
						</div>
					</div>
					<!--line1 S-->
					<div class="sm-row">
						<div class="half_wrap">
							<div>
								<ul>
									<li>
										<label class="tit_150 dib poa">동시적용프로그램종류</label>
										<div class="ml_150">
											<div id="cboSame" class="dib vat width-45" data-ax5select="cboSame" data-ax5select-config="{size:'sm',theme:'primary'}" ></div>
											<div class="dib vat">
												<input type="checkbox" class="checkbox-prg" id="chkCmd" data-label="커맨드사용"  />
											</div>
										</div>						
									</li>
									<li class="sm-row">
										<label class="tit_150 dib poa">동시적용룰(기준/변경)</label>
										<div class="ml_150">
											<input id="txtBase" type="text" class="width-45">
											<input id="txtChgRule"  type="text" style="width:calc(55% - 5px);">
										</div>
									</li>
									<li class="sm-row">
										<label class="tit_150 dib poa">디렉토리룰(기준/변경)</label>
										<div class="ml_150">
											<input id="txtBaseDir" type="text" class="width-45"> 
											<input id="txtChgDir" type="text"  style="width:calc(55% - 5px);">
										</div>
									</li>
									<li class="sm-row">
										<label class="tit_150 dib poa">동시적용유형</label>
										<div class="ml_150">
											<div id="cboFact" class="dib vat width-45 " data-ax5select="cboFact" data-ax5select-config="{size:'sm',theme:'primary'}" ></div>
											<button id="btnAdd" class="btn_basic_s">동시등록</button>
										</div>						
									</li>
								</ul>
							</div>
							<div class="sm-row">
								<div class="az_board_basic az_board_basic_in" style="height: 18%">
									<div data-ax5grid="sameGrid" data-ax5grid-config="{showLineNumber: true, lineNumberColumnWidth: 40}" style="height: 100%;"></div>
								</div>
							</div>
						</div> 
					</div>
					<!--버튼영억 S-->
					<div class="sm-row">
						<button id="btnReqItem" class="btn_basic_s">프로그램종류 정보등록</button>
						<button id="btnCls" class="btn_basic_s">프로그램종류폐기</button>
						<button id="btnQry" class="btn_basic_s">조회</button>
						<button id="btnPrgSeq" class="btn_basic_s">프로그램처리속성 트리구조작성</button>
					</div>
					<!--line3 S-->
					<div class="sm-row">
						<div class="half_wrap">							
							<div class="width-100 dib vat">
								<label class="title">[등록된 프로그램 종류 목록]</label>
							</div>
							<div class="sm-row">
								<div class="az_board_basic az_board_basic_in" style="height: calc(85% - 325px)">
									<div data-ax5grid="prgGrid" data-ax5grid-config="{showLineNumber: true, lineNumberColumnWidth: 40}" style="height: 100%;"></div>	
								</div>
							</div>
							<div class="sm-row por">
								<div class="dib">
									<label>우선순위적용</label>
									<button id="btnUp" class="btn_basic_s margin-5-left md-font">&#9652;</button>
									<button id="btnDown" class="btn_basic_s margin-5-left md-font">&#9662;</button>
								</div>
								<div class="poa_r dib">
									<button id="btnReqSeq" class="btn_basic_s">우선순위적용</button>
									<!-- <button id="btnExit" class="btn_basic_s">닫기</button> -->
								</div>
							</div>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>
</div>

		
<script type="text/javascript" src="<c:url value="/scripts/bluebird-3.5.0.min.js"/>"></script>
<c:import url="/js/ecams/common/commonscript.jsp" />	
<script type="text/javascript" src="<c:url value="/js/ecams/tab/sysinfo/SysDetailPrgTab.js"/>"></script>