<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:import url="/webPage/common/common.jsp"/>

<div class="contentFrame">
	<div id="history_wrap"></div>
	
	<div class="az_search_wrap" style="padding-left: 10px; padding-right: 10px;">
		<div class="row">
			<label class="poa title tit_110">개발요청목록</label>
			
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
	
	<div class="az_board_basic" style="height:33%;">
    	<div data-ax5grid="firstGrid" data-ax5grid-config="{showLineNumber: true, lineNumberColumnWidth: 40}" style="height: 100%;" class="resize"></div>
	</div>
	
	<div class="az_search_wrap" style="padding-left: 10px; padding-right: 10px; margin-top: 10px">
		<div class="row">
			<label class="poa title tit_110">외주개발요청목록</label>
			
			<div class="width-25 dib vat" style="min-width: 285px">
				<label class="tit_60 poa">진행상태</label>
				<div class="ml_60 vat">
					<div id="cboGbn2" data-ax5select="cboGbn2" data-ax5select-config="{size:'sm',theme:'primary',selecteditable: true}" class="dib width-49"></div>
					<div id="cboSta2" data-ax5select="cboSta2" data-ax5select-config="{size:'sm',theme:'primary',selecteditable: true}" class="dib width-49"></div>
				</div>
			</div>
	
			<div class="dib vat" style="min-width: 400px; width: 21%;">
				<input type="checkbox" class="checkbox-pie" id="chkReqDate2" data-label="요청등록일"/>
				<div class="dib vat">
					<div id="divPicker" data-ax5picker="basic2" class="az_input_group dib vat">
						<input id="datStD2" name="datStD2" type="text" placeholder="yyyy/mm/dd" style="width:100px;" class="f-cal" autocomplete="off">
						<button id="btnStD2" class="btn_calendar"><i class="fa fa-calendar-o"></i></button>
						<span class="sim">∼</span>
						<input id="datEdD2" name="datEdD2" type="text" placeholder="yyyy/mm/dd" style="width:100px;" class="f-cal" autocomplete="off">
						<button id="btnEdD2" class="btn_calendar"><i class="fa fa-calendar-o"></i></button>
					</div>
				</div>
			</div>
			
			<div class="width-10 dib vat">
				<label class="tit_60 poa">발행자</label>
				<input class="ml_60" id="txtEditor" name="txtEditor" type="text" style="width: 120px;">
			</div>
			
			<div class="float-right">
				<button id="btnQry2" class="btn_basic_s margin-5-left" style="width:70px;">조회</button>
			</div>
		</div>
	</div>
	
	<div class="az_board_basic" style="height:33%;">
    	<div data-ax5grid="secondGrid" data-ax5grid-config="{showLineNumber: true, lineNumberColumnWidth: 40}" style="height: 100%;" class="resize"></div>
	</div>
	
	<div class="az_search_wrap margin-10-top" style="padding-left: 10px; padding-right: 10px;">
		<!--좌측-->
		<div class="l_wrap width-49 vat write_wrap">
			<div class="row">
				<dl>
					<dt><label>발행번호</label></dt>
					<dd>
						<div class="dib vat width-60" id="divOrder" style="display: none;"><input id="txtOrderHide" type="text" class="tac" style="width: 100%;" disabled="disabled"></div>
						<div class="dib vat width-15" id="divReqNum0"><input id="txtReqNum0" type="text" class="tac" style="width: 100%;" disabled="disabled" value="요청"></div>
						<div class="dib vat width-30" id="divJob">
							<div id="cboJob" data-ax5select="cboJob" data-ax5select-config="{size:'sm',theme:'primary',selecteditable: true}" class="width-100 dib" style="min-width: 0;"></div>
						</div>
						<div class="dib vat width-15" id="divReqNum1"><input id="txtReqNum1" type="text" class="tac" style="width: 100%;" disabled="disabled"></div>
						<div class="dib vat">
							<input type="checkbox" class="checkbox-pie" id="chkNew" data-label="신규등록" checked></input>
						</div>
					</dd>
				</dl>
			</div>
			<div class="row">
				<dl>
					<dt><label>요청제목</label></dt>
					<dd><input id="txtTitle" name="txtTitle" type="text" style="width:100%;"></dd>
				</dl>
			</div>
			<div class="row">
				<dl>
					<dt><label>상세요청내용</label><label id="lblDetail" style="font-weight: normal;">총 (0자)</label></dt>
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
			<div class="row half cb">
				<dl>
					<dt style="width: 100px"><label>발행근거</label></dt>
					<dd style="margin-left: 100px">
						<div class="vat">
							<input id="txtCause" type="text" style="width: 100%;">
						</div>
					</dd>
				</dl>
				<dl>
					<dt style="margin-left: 10px; width: 60px"><label>처리기한</label></dt>
					<dd>
						<div class="az_input-group" data-ax5picker="basic3">
			            	<input id="datePrcDate" type="text" class="f-cal" placeholder="yyyy/mm/dd" autocomplete="off"><span class="btn_calendar poa_r"><i class="fa fa-calendar-o"></i></span>
			         	</div>
			        </dd>
				</dl>
			</div>
			<div class="row">
				<dl>
					<dt style="width: 100px"><label>외주PM</label></dt>
					<dd style="margin-left: 100px">
						<div class="scrollBind" id="lstPMDiv" style="height: 100px">
		    				<ul class="list-group" id="lstPM" style="height: 100%;">
			    			</ul>
		    			</div>
					</dd>
				</dl>
			</div>
			<div class="row">
				<dl>
					<dt style="width: 100px">
						<label>주관부서-담당자</label>
						<div class="sm-row">
							<button id="btnAddDevUser" name="btnAddDevUser" class="btn_basic_s" style="margin: 0 0;">추가</button>
						</div>
					</dt>
					<dd style="margin-left: 100px">
						<div class="az_board_basic scroll_h az_board_basic_in" style="height: 120px;">
					    	<div data-ax5grid="userGrid" data-ax5grid-config="{showLineNumber: true, lineNumberColumnWidth: 40}" style="height: 100%;"></div>
					    </div>
					</dd>
				</dl>
			</div>
			
			<div class="row">
				<div class="float-right">
					<button id="btnComplete" name="btnComplete" class="btn_basic_s">개발종료</button>
					<button id="btnDel" name="btnDel" class="btn_basic_s">삭제</button>
					<button id="btnAdd" name="btnAdd" class="btn_basic_s">등록</button>
				</div>
			</div>
		</div>
	</div>
</div>

<c:import url="/js/ecams/common/commonscript.jsp" />
<script type="text/javascript" src="<c:url value="/js/ecams/dev/RegistOutSourcingReq.js"/>"></script>