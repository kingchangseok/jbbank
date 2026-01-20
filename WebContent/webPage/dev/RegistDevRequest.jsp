<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:import url="/webPage/common/common.jsp"/>

<style>
	.write_wrap dl dt{position: absolute; width: 100px;}
	.write_wrap_100 dl dt{position: absolute; width: 100px !important;}
	.write_wrap dl dd{position: relative; margin-left: 100px;}
	.write_wrap_100 dl dd{position: relative; margin-left: 100px !important;}
	.write_wrap .half dl dt{width: 100px;}
	.write_wrap .half dl dd{margin-left: 100px;}
</style>

<div class="contentFrame">
	<div id="history_wrap"></div>
	
	<div class="az_search_wrap" style="padding-left: 10px; padding-right: 10px;">
		<div class="row">
			<label class="poa title">개발요청목록</label>
			
			<div class="width-25 dib vat" style="min-width: 285px">
				<label class="tit_60 poa">진행상태</label>
				<div class="ml_60 vat">
					<div id="cboGbn" data-ax5select="cboGbn" data-ax5select-config="{size:'sm',theme:'primary',selecteditable: true}" class="dib width-49"></div>
					<div id="cboSta" data-ax5select="cboSta" data-ax5select-config="{size:'sm',theme:'primary',selecteditable: true}" class="dib width-49"></div>
				</div>
			</div>
	
			<div class="width-25 dib vat" style="min-width: 400px">
				<input type="checkbox" class="checkbox-pie" id="chkReqDate" data-label="요청등록일"/>
				<div class="dib vat">
					<div id="divPicker" data-ax5picker="basic" class="az_input_group dib vat">
						<input id="datStD" name="datStD" type="text" placeholder="yyyy/mm/dd" style="width:100px;" class="f-cal" autocomplete="off">
						<button id="btnStD" class="btn_calendar"><i class="fa fa-calendar-o"></i></button>
						<span class="sim">∼</span>
						<input id="datEdD" name="datEdD" type="text" placeholder="yyyy/mm/dd" style="width:100px;" class="f-cal" autocomplete="off">
						<button id="btnEdD" class="btn_calendar"><i class="fa fa-calendar-o"></i></button>
					</div>
				</div>
			</div>	
			
			<div class="float-right">
				<button id="btnQry" class="btn_basic_s margin-5-left" style="width:70px;">조회</button>
			</div>
		</div>
	</div>
	
	<div class="az_board_basic" style="height:30%;">
    	<div data-ax5grid="firstGrid" data-ax5grid-config="{showLineNumber: true, lineNumberColumnWidth: 40}" style="height: 100%;" class="resize"></div>
	</div>
	
	<div class="az_search_wrap margin-10-top" style="padding-left: 10px; padding-right: 10px;">
		<!--좌측-->
		<div class="l_wrap width-49 vat write_wrap">
			<div class="row">
				<dl>
					<dt><label>요청번호</label></dt>
					<dd>
						<div class="dib vat width-25" id="divReqNum">
							<input id="txtReqNum0" type="text" class="tac" style="width: 100%;" disabled="disabled" value="요청">
							<input id="txtReqNum2" type="text" style="width: 100%; display: none;" disabled="disabled"> <!-- 신규등록 체크했을 때 -->
						</div>
						<span class="sim" id="labReq01">-</span>
						<div class="dib vat width-25" id="divCboReqNum1"><input id="cboReqNum1" type="text" class="tac" style="width: 100%;" disabled="disabled"></div>
						<span class="sim" id="labReq02">-</span>
						<div class="dib vat width-15" id="divTxtReqNum1"><input id="txtReqNum1" type="text" class="tac" style="width: 100%;" disabled="disabled"></div>
						<span class="sim" id="labReq03">-</span>
						<div class="dib vat width-15" id="divTxtReqNum3"><input id="txtReqNum3" type="text" class="tac" style="width: 100%;" disabled="disabled" value="일련번호"></div>
						<div class="dib vat" id="divChkNew">
							<input type="checkbox" class="checkbox-pie" id="chkNew" data-label="신규등록"></input>
						</div>
					</dd>
				</dl>
			</div>
			<div class="row half cb">
				<dl>
					<dt><label>문서유형/문서번호</label></dt>
					<dd>
						<div class="vat">
							<div id="cboDocType" data-ax5select="cboDocType" data-ax5select-config="{size:'sm',theme:'primary'}" class="width-100 dib" style="min-width: 0;"></div>
						</div>
					</dd>
				</dl>
				<dl>
					<dd style="margin-left: 5px"><input id="txtDoc" type="text" autocomplete="off"></dd>
				</dl>
			</div>
			<div class="row half cb">
				<dl>
					<dt><label>대내-부점/요청자</label></dt>
					<dd id="txtCaller0ToolTip"><input id="txtCaller0" type="text" autocomplete="off" disabled="disabled"></dd>
				</dl>
				<dl>
					<dd id="txtCaller1ToolTip" style="margin-left: 5px"><input id="txtCaller1" type="text" autocomplete="off"></dd>
				</dl>
			</div>
			<div class="row">
				<dl>
					<dt>
						<label>주관부서-담당자</label>
						<div class="sm-row">
							<button id="btnAddDevUser" name="btnAddDevUser" class="btn_basic_s" style="margin: 0 0;">추가</button>
						</div>
					</dt>						
					<dd>
						<div class="az_board_basic scroll_h az_board_basic_in" style="height: 120px;">
					    	<div data-ax5grid="userGrid" data-ax5grid-config="{showLineNumber: true, lineNumberColumnWidth: 40}" style="height: 100%;"></div>
					    </div>
					</dd>
				</dl>
			</div>
			<div class="row">
				<dl>
					<dt><label>업무상세명</label></dt>
					<dd><input id="txtTitleDetail" type="text" autocomplete="off"></dd>
				</dl>
			</div>
			<div class="row half cb">
				<dl>
					<dt><label>업무</label></dt>
					<dd>
						<div id="cboJob" data-ax5select="cboJob" data-ax5select-config="{size:'sm',theme:'primary',selecteditable: true,selSize:5}" class="width-100 dib" style="min-width: 0;"></div>
					</dd>
				</dl>
				<dl>
					<dt style="margin-left: 10px; width: 60px"><label>조치유형</label></dt>
					<dd style="margin-left: 70px">
						<div id="cboConType" data-ax5select="cboConType" data-ax5select-config="{size:'sm',theme:'primary'}" class="width-100 dib" style="min-width: 0;"></div>
					</dd>
				</dl>
			</div>
			<div class="row half cb">
				<dl>
					<dt><label>업무난이도</label></dt>
					<dd>
						<div id="cboGrade" data-ax5select="cboGrade" data-ax5select-config="{size:'sm',theme:'primary'}" class="width-100 dib" style="min-width: 0;"></div>
					</dd>
				</dl>
				<dl>
					<dt style="margin-left: 10px; width: 60px"><label>개발기간</label></dt>
					<dd style="margin-left: 70px">
						<div id="divPicker" data-ax5picker="basic2" class="az_input_group dib vat">
							<input id="datStD2" name="datStD2" type="text" placeholder="yyyy/mm/dd" style="width:100px;" class="f-cal" autocomplete="off">
							<button id="btnStD2" class="btn_calendar"><i class="fa fa-calendar-o"></i></button>
						</div>
						<span class="sim">∼</span>
						<div id="divPicker" data-ax5picker="basic21" class="az_input_group dib vat">
							<input id="datEdD2" name="datEdD2" type="text" placeholder="yyyy/mm/dd" style="width:100px;" class="f-cal" autocomplete="off">
							<button id="btnEdD2" class="btn_calendar"><i class="fa fa-calendar-o"></i></button>
						</div>
						<div class="dib vat">
							<input id="txtDevDate" name="txtDevDate" type="text" disabled="disabled" style="width: 50px">
							<label>일</label>
						</div>
					</dd>
				</dl>
			</div>
			<div class="row">
				<dl>
					<dt><label>추가업무내용</label><label id="lblDetail" style="font-weight: normal;">총 (0자)</label></dt>
					<dd>
						<form>
							<textarea id="txtDetail" style="align-content:flex-start;width:100%;height:100px;resize: none; overflow-y:auto; padding:5px;"></textarea>
						</form>
					</dd>
				</dl>
			</div>
			<div class="row">
				<dl>
					<dt>
						<div>
							<div style="display:none;" id="fileSave"></div>
							<button id="btnFileAdd" name="btnFileAdd" class="btn_basic_s vat mt3m" style="margin: 0 0;">파일첨부</button>
							<div class="sm-row">
								<button id="btnFileDel" name="btnFileDel" class="btn_basic_s vat mt3m" style="margin: 0 0;">파일삭제</button>
							</div>
						</div>
					</dt>
					<dd>
						<div class="az_board_basic scroll_h az_board_basic_in">
					    	<section>
								<div class="container-fluid">
									<div data-ax5grid="fileGrid" data-ax5grid-config="{showLineNumber: true, lineNumberColumnWidth: 40}" style="height: 115px;"></div>
								</div>
							</section>
					    </div>
					</dd>
				</dl>
			</div>
		</div>
		
		<!--우측-->
		<div class="r_wrap width-49 vat write_wrap">
			<div style="margin-top: 30px">
				<dl>
					<dt><label>문서제목</label></dt>
					<dd><input id="txtTitle" name="txtTitle" type="text" style="width:100%;"></dd>
				</dl>
			</div>

	        <div class="row">
				<dl>
					<dd>
						<input type="checkbox" id="chkTestReqYN" class="checkbox-pie" data-label="테스트전담반 사용여부" disabled>
					</dd>
				</dl>
				<dl>
					<dd style="margin-left: 5px;"><input id="txtTestPass" type="text" autocomplete="off" style="display: none;"></dd>
				</dl>				
	         </div>

			<div class="row half cb">
				<dl>
					<dt><label>대외-기관/요청자</label></dt>
					<dd>
						<div class="vat">
							<div id="cboForeign" data-ax5select="cboForeign" data-ax5select-config="{size:'sm',theme:'primary'}" class="width-100 dib" style="min-width: 0;"></div>
						</div>
					</dd>
				</dl>
				<dl>
					<dd style="margin-left: 5px;"><input id="txtCaller2" type="text" autocomplete="off" style="display: none;"></dd>
				</dl>
			</div>
			<div class="row">
				<dl>
					<dt><label>주관부서-문서번호</label></dt>
					<dd><input id="txtHandlerDocNum" name="txtHandlerDocNum" type="text" style="width:100%;"></dd>
				</dl>
			</div>
			<div class="row half cb">
				<dl>
					<dt><label class="fontStyle-cncl">요청유형/처리기간</label></dt>
					<dd>
						<div class="vat">
							<div id="cboReqType" data-ax5select="cboReqType" data-ax5select-config="{size:'sm',theme:'primary',selecteditable: true,selSize:8}" class="width-100 dib" style="min-width: 0;"></div>
						</div>
					</dd>
				</dl>
				<dl>
					<dd style="margin-left: 5px">
						<div class="az_input-group" data-ax5picker="basic3">
			            	<input id="datePrcDate" type="text" class="f-cal" placeholder="yyyy/mm/dd" autocomplete="off"><span class="btn_calendar poa_r"><i class="fa fa-calendar-o"></i></span>
			         	</div>
			        </dd>
				</dl>
			</div>
			<div class="row">
				<dl>
					<dt><label>수신부서</label></dt>
					<dd>
						<div class="scrollBind" id="treeDeptDiv" style="height: 240px">
		    				<ul class="ztree" id="treeDept" style="height: 100%;">
			    			</ul>
		    			</div>
					</dd>
				</dl>
			</div>
			
			<div class="row">
				<div class="float-right">
					<button id="btnComplete" name="btnComplete" class="btn_basic_s" disabled="disabled">개발종료</button>
					<button id="btnDel" name="btnDel" class="btn_basic_s" disabled="disabled">삭제</button>
					<button id="btnAdd" name="btnAdd" class="btn_basic_s">등록</button>
				</div>
			</div>
			
			<div class="row width-100 dib vat">
				<label class="txt_r fontStyle-bold float-right">※ 해당 개발 요청으로 발행된 업무 지시서가 모두 종료되었을 시 요청자가 개발 요청 종료</label>
			</div>
		</div>
	</div>
</div>

<c:import url="/js/ecams/common/commonscript.jsp" />
<script type="text/javascript" src="<c:url value="/js/ecams/dev/RegistDevRequest.js"/>"></script>