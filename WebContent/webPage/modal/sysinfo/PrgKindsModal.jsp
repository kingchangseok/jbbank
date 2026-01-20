<!--  
	* 화면명: 프로그램종류정보
	* 화면호출: 시스템정보 -> 프로그램종류정보 클릭
-->
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:import url="/webPage/common/common.jsp" />

<div class="pop-header">
	<div>
		<label id="lbSub">[프로그램종류정보]</label>
	</div>
	<div>
		<button type="button" class="close" aria-label="닫기" onclick="popClose()">
			<span aria-hidden="true">&times;</span>
		</button>
	</div>
</div>

<div class="container-fluid pop_wrap">
	<div class="sm-row">
		<div>
			<label id="lbSub" class="poa tit_80">시스템</label>
			<div class="ml_80">
				<input id="txtSysMsg" class="form-control dib" type="text" readonly="readonly">
			</div>
		</div>
	</div>
	
	<div class="sm-row" style="font-size:0px;">			
		<div class="dib width-50 vat">
			<label id="lbSub" class="poa tit_80">프로그램종류</label>
			<div class="ml_80">
				<div id="cboJawon" data-ax5select="cboJawon" data-ax5select-config="{size:'sm',theme:'primary'}" style="width:100%;" ></div>
			</div>
		</div>
		<div class="dib width-25 vat">
			<label id="lbSub" class="poa tit_80" style="margin-left:5px; width:75px;">버전관리수</label>
			<div class="ml_80">
				<input id="txtVer" class="form-control dib" type="text">
			</div>
		</div>
		<div class="dib width-25 vat">
			<label id="lbSub" class="poa tit_80" style="margin-left:5px; width:75px;">정기배포시간(HHMM)</label>
			<div class="ml_80">
				<input id="txtTime" class="form-control dib" type="text">
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
					<div class="scrollBind" style="height: 619px;">
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
							<input id="txtExename" class="form-control dib" type="text">
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
											<div id="cboSame" class="dib vat" data-ax5select="cboSame" data-ax5select-config="{size:'sm',theme:'primary'}" style="width:calc(100% - 91px);"></div>
											<div class="dib vat">
												<input type="checkbox" class="checkbox-prg" id="chkCmd" data-label="커맨드사용"  />
											</div>
										</div>						
									</li>
									<li class="sm-row">
										<label class="tit_150 dib poa">동시적용룰(기준/변경)</label>
										<div class="ml_150">
											<input id="txtBase" type="text" class="width-45">
											<input id="txtChgRule"  type="text" class="width-54">
										</div>
									</li>
									<li class="sm-row">
										<label class="tit_150 dib poa">디렉토리룰(기준/변경)</label>
										<div class="ml_150">
											<input id="txtBaseDir" type="text" class="width-45"> 
											<input id="txtChgDir" type="text" class="width-54">
										</div>
									</li>
								</ul>
							</div>
							<!--버튼-->
							<div class="sm-row por">
								<input id="radioCkOut" tabindex="8" type="radio" name="radioPrg" value="optCkOut" />
								<label for="radioCkOut">실행모듈정보</label>
								<input id="radioCkIn" tabindex="8" type="radio" name="radioPrg" value="optCkIn" checked="checked"/>
								<label for="radioCkIn">동시적용항목정보</label>
								<input id="radioDir" tabindex="8" type="radio" name="radioPrg" value="optDir" />
								<label for="radioDir">자동생성항목정보</label>
								<input id="radioRename" tabindex="8" type="radio" name="radioPrg" value="optRename" />
								<label for="radioRename">확장자변경</label>
								<button id="btnAdd" class="btn_basic_s margin-5-left poa_r">동시등록</button>
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
						<button id="btnReqItem" class="btn_basic_s margin-5-left">프로그램종류 정보등록</button>
						<button id="btnCls" class="btn_basic_s margin-5-left">프로그램종류폐기</button>
						<button id="btnQry" class="btn_basic_s margin-5-left">조회</button>
						<button id="btnPrgSeq" class="btn_basic_s margin-5-left">프로그램처리속성 트리구조작성</button>
					</div>
					<!--line3 S-->
					<div class="sm-row">
						<div class="half_wrap">							
							<div class="width-100 dib vat">
								<label class="title">[등록된 프로그램 종류 목록]</label>
							</div>
							<div class="sm-row">
								<div class="az_board_basic az_board_basic_in" style="height: 28%">
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
									<button id="btnReqSeq" class="btn_basic_s margin-5-left">우선순위적용</button>
									<button id="btnExit" class="btn_basic_s margin-5-left">닫기</button>
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
<script type="text/javascript" src="<c:url value="/js/ecams/modal/sysinfo/PrgKindsModal.js"/>"></script>