var firstGrid	= new ax5.ui.grid();
var secondGrid	= new ax5.ui.grid();
var thirdGrid	= new ax5.ui.grid();

var commonData	= [];
var srTestData  = [];
var srTestCommon = null;

var isAdmin		= false;
var isDevEnable = false;
var isEnable	= false;

var devTimeSw	= false;
var qaSw		= false;
var gradeSw		= false;
var totalSw		= false;
var detailSw	= false;
var sayuSw		= false;
var pSrId	 	= null;

var chgCodeData		= [];
var detailCodeData 	= [];
var cboDp = [
	{grade: '선택하세요'},
	{grade: 'A'},
	{grade: 'B'},
	{grade: 'C'},
	{grade: 'D'},
	{grade: 'E'}
]

var cboDp2 = [
	{yn: '선택하세요', cd:'X'},
	{yn: 'Y', cd:'1'},
	{yn: 'N', cd:'0'},
]

var completeReadyFunc = false;

$(document).ready(function(){
	getCodeInfo();
	createViewGrid();
	createViewGrid2();
	createViewGrid3();
	
	$("#btnRewrite").bind("click", function(){
		btnRewrite_Click();
		//btnRewriteQA_Click();
	});
	
	$("#btnRewriteQA").bind("click", function(){
		btnRewriteQA_Click();
	});

	$("#btnTestSkip").bind("click", function(){
		btnTestSkip_Click();
	});
	
	completeReadyFunc = true;
	$('#btnTestSkip').hide();
});

function upFocus() {
	firstGrid.focus('HOME');
}

function QaApp(value1, value2) {
	isEnable = value1;
	isDevEnable = value2;
	
	if(isDevEnable){
		$('#btnRewrite').show();
		//$('#btnTestSkip').show();
	}
	else $('#btnRewrite').hide();
	
	screenInit();
}
function QaApp_cis(value1, value2, value3) {
	isEnable = value1;
	isDevEnable = value2;
	isQaEnable = value3;
	isDevEnable = true; //test임시  cishappy
	$('#btnTestSkip').css('display','none');
	if(isDevEnable) $('#btnRewrite').show();
	else $('#btnRewrite').hide();
	if (isEnable) $('#btnRewriteQA').show();
	else $('#btnRewriteQA').hide();
	
	screenInit();
}

function createViewGrid() {
	firstGrid	= new ax5.ui.grid();
	firstGrid.setConfig({
	    target: $('[data-ax5grid="firstGrid"]'),
	    sortable: false, 
	    multiSort: true,
	    showLineNumber: false,
	    paging : false,
	    page: false,
	    header: {
	        align: 'center'
	    },
	    body: {
	        onClick: function () {
	        	this.self.clearSelect();
	            this.self.select(this.dindex);
	        },
	    	onDataChanged: function(){
	    	    this.self.repaint();
	    	}
	    },
	    columns: [{label: "성과제어",  width: '100%', align: 'left', columns: [
		            {key: 'cr_totalcheck1', label: "난이도",		width: "25%", align: "center",
		            	formatter: function(){
		            		var disabled = isEnable ? '' : 'disabled';
		        			var selStr = '<div class="select width-100">';
		        			selStr += "<select class='selBox width-100'onChange='totalcheckChange(this,0)' " + disabled +">"
		        			for(var i=0; i<cboDp.length; i++) {
		        				var selected = "";
		        				// 그리드 값이랑 같으면 selected 처리
		        				if (this.item.cr_totalcheck != null && this.item.cr_totalcheck != '') {
		        					if(this.item.cr_totalcheck.substr(0,1) == cboDp[i].grade){
		        						selected = "selected='selected'";
		        					}
		        				}
		        				selStr += "<option value='"+cboDp[i].grade+"' "+ selected +">"+cboDp[i].grade+"</option>";
		        			}
		        			selStr += "</select>";
		        			selStr +='</div>';
		        			return selStr;
		        		}
		            },
		            {key: 'cr_totalcheck2', label: "작업량",		width: "25%", align: "center",
		            	formatter: function(){
		            		var disabled = isEnable ? '' : 'disabled';
		        			var selStr = '<div class="select width-100">';
		        			selStr += "<select class='selBox width-100' onChange='totalcheckChange(this,1)' " + disabled +">"
		        			for(var i=0; i<cboDp.length; i++) {
		        				var selected = "";
		        				// 그리드 값이랑 같으면 selected 처리
		        				if (this.item.cr_totalcheck != null && this.item.cr_totalcheck != '') {
		        					if(this.item.cr_totalcheck.substr(1,1) == cboDp[i].grade){
		        						selected = "selected='selected'";
		        					}
		        				}
		        				selStr += "<option value='"+cboDp[i].grade+"' "+ selected +">"+cboDp[i].grade+"</option>";
		        			}
		        			selStr += "</select>";
		        			selStr +='</div>';
		        			return selStr;
		        		}
		            },
		            {key: 'cr_totalcheck3', label: "개선도",		width: "25%", align: "center",
		            	formatter: function(){
		            		var disabled = isEnable ? '' : 'disabled';
		        			var selStr = '<div class="select width-100">';
		        			selStr += "<select class='selBox width-100' onChange='totalcheckChange(this,2)' " + disabled +">"
		        			for(var i=0; i<cboDp.length; i++) {
		        				var selected = "";
		        				// 그리드 값이랑 같으면 selected 처리
		        				if (this.item.cr_totalcheck != null && this.item.cr_totalcheck != '') {
		        					if(this.item.cr_totalcheck.substr(2,1) == cboDp[i].grade){
		        						selected = "selected='selected'";
		        					}
		        				}
		        				selStr += "<option value='"+cboDp[i].grade+"' "+ selected +">"+cboDp[i].grade+"</option>";
		        			}
		        			selStr += "</select>";
		        			selStr +='</div>';
		        			return selStr;
		        		}
		            },
		            {key: 'cr_totalcheck4', label: "종합평가",	width: "25%", align: "center",
		            	formatter: function(){
		            		var disabled = isEnable ? '' : 'disabled';
		        			var selStr = '<div class="select width-100">';
		        			selStr += "<select class='selBox width-100' onChange='totalcheckChange(this,3)' " + disabled +">"
		        			for(var i=0; i<cboDp.length; i++) {
		        				var selected = "";
		        				// 그리드 값이랑 같으면 selected 처리
		        				if (this.item.cr_totalcheck != null && this.item.cr_totalcheck != '') {
		        					if(this.item.cr_totalcheck.substr(3,1) == cboDp[i].grade){
		        						selected = "selected='selected'";
		        					}
		        				}
		        				selStr += "<option value='"+cboDp[i].grade+"' "+ selected +">"+cboDp[i].grade+"</option>";
		        			}
		        			selStr += "</select>";
		        			selStr +='</div>';
		        			return selStr;
		        		}
		            },
	           ]
	    	}]
	});
	
	if (commonData != null && commonData.length > 0) {
		firstGrid.setData(commonData);
		firstGrid.repaint();
	}
}

function createViewGrid3() {
	thirdGrid.setConfig({
		target: $('[data-ax5grid="thirdGrid"]'),
		sortable: true, 
		multiSort: true,
		showLineNumber: true,
		header: {
			align: "center"
		},
		body: {
			onClick: function () {
				this.self.clearSelect();
				this.self.select(this.dindex);
				if (this.colIndex == 5) { //SR보기
					pSrId = this.item.cc_srreqid;
					//window.open("https://dcis.jbbank.co.kr/cis/sso/bizSvtLogin?SysDiv=DMO&RedirectUrl=/p/request/"+pSrId,'srOpenWeb',"scrollbars=1,toolbar=0,location=no,resizable=yes,status=0,menubar=0,width=1200,height=720,left=0,top=0");
					window.open("https://cis.jbbank.co.kr/cis/sso/bizSvtLogin?SysDiv=SR&RedirectUrl=/p/request/"+pSrId,'srOpenWeb',"scrollbars=1,toolbar=0,location=no,resizable=yes,status=0,menubar=0,width=1200,height=720,left=0,top=0");					
				}
				if (this.colIndex == 6) { //테스트 내용보기
					var yn = this.item.cc_testreq_yn; // 또는 this.item._effYN
                    if (yn !== 'Y') return; // N이면 클릭 무시

					pSrId = this.item.cc_srreqid;
					//window.open("https://dcis.jbbank.co.kr/cis/sso/bizSvtLogin?SysDiv=DMO&RedirectUrl=/p/TestComplateDetail/"+pSrId,'srTestWeb',"scrollbars=1,toolbar=0,location=no,resizable=yes,status=0,menubar=0,width=1200,height=720,left=0,top=0");
					window.open("https://cis.jbbank.co.kr/cis/sso/bizSvtLogin?SysDiv=SR&RedirectUrl=/p/TestComplateDetail/"+pSrId,'srTestWeb',"scrollbars=1,toolbar=0,location=no,resizable=yes,status=0,menubar=0,width=1200,height=720,left=0,top=0");					
				}
			},
			onDBLClick: function () {
				if (this.dindex < 0) return;
			},
			trStyleClass: function () {
				if (this.item.ColorSw == '3'){
					return "fontStyle-cncl";
				} else if(this.item.ColorSw == '0'){
					return "fontStyle-ing";
				} else if(this.item.ColorSw == '5'){
					return "fontStyle-error";
				} 
			},
			onDataChanged: function(){
				this.self.repaint();
			}
		},
		contextMenu: {
		 },
		columns: [		
			{key: "cc_srreqid", 	label: "SR요청서번호",		width: '20%', align: 'left'},
			{key: "cc_docsubj", 	label: "요청서명",     	width: '30%', align: 'left'},
			{key: "cc_testreq_yn", 	label: "테스트요청여부",	width: '10%'},
			{key: "testpass", 		label: "테스트생략사유",  	width: '10%'},
			{key: "jobdif", 		label: "업무난이도",  	    width: '10%'},
			{key: 'srpop', 			label: 'SR보기',  		width: '10%', align: 'center',
	        	formatter: function() {
	        		return '<button style="width: 98%; height: 98%;">이동</button>';
	        	}
	        },
			{key: 'testpop', 		label: '테스트내용보기',  		width: '10%', align: 'center',
	        	formatter: function() {
					var yn = this.item.cc_testreq_yn; // 또는 this.item._effYN
                    if (yn === 'Y') {
                        return '<button style="width:98%;height:98%;">이동</button>';
                    }
                    // N이면 버튼 감춤(칸 유지용 공백)
                    return '';
	        	}
	        },
		]
	});
};
function totalcheckChange(val,cd) {
	var selVal = $(val);
	var selIndex = Number($(val).parent().parent().parent().attr('data-ax5grid-data-index'));
	
	var selItem = commonData[selIndex];	// 그리드에서 가져옴
	var selVal 	= cboDp[Number(selVal[0].selectedIndex)]; // 선택된 콤보박스정보
	if(selItem == null || selItem.length == 0) {
		return;
	}
	
	console.log('val=',cd);
	console.log('selVal=',selVal);
	if (selVal.grade != '선택하세요') {
		if(cd == '0') {
			for(i = 0; i < firstGrid.list[selIndex].cr_totalcheck.length; i++) {
				firstGrid.list[selIndex].cr_totalcheck = firstGrid.list[selIndex].cr_totalcheck.replaceAt(i, selVal.grade);
			}
		} else {
			firstGrid.list[selIndex].cr_totalcheck = firstGrid.list[selIndex].cr_totalcheck.replaceAt(Number(cd),selVal.grade);
		}
	} else {
		firstGrid.list[selIndex].cr_totalcheck = firstGrid.list[selIndex].cr_totalcheck.replaceAt(Number(cd),'0');
	}
	console.log('totalCheck=',firstGrid.list[selIndex].cr_totalcheck);
	firstGrid.setData(commonData);
	firstGrid.repaint();
}

function createViewGrid2() {
	secondGrid	= new ax5.ui.grid();
	secondGrid.setConfig({
		target: $('[data-ax5grid="secondGrid"]'),
		sortable: false, 
		multiSort: true,
		showLineNumber: false,
		paging : false,
		page: false,
		header: {
			align: 'center'
		},
		body: {
			onClick: function () {
				this.self.clearSelect();
				this.self.select(this.dindex);
			},
			onDataChanged: function(){
				this.self.repaint();
			}
		},
		columns: [
			{key: 'cr_devptime',	label: "개발기간",	 width: "10%", align: "center", editor: {type: "number"}},
			{label: "책임자 확인",  width: '50%', align: 'left', columns: [
		            {key: 'cr_qacheck1', label: "규정준수여부",	 	 width: "17%", align: "center",
		            	formatter: function(){
		            		var disabled = isEnable ? '' : 'disabled';
		        			var selStr = '<div class="select width-100">';
		        			selStr += "<select class='selBox width-100' onChange='qacheckChange(this,0)' " + disabled +">"
		        			for(var i=0; i<cboDp2.length; i++) {
		        				var selected = "";
		        				// 그리드 값이랑 같으면 selected 처리
		        				if (this.item.cr_qacheck != null && this.item.cr_qacheck != '') {
		        					if(this.item.cr_qacheck.substr(0,1) == cboDp2[i].cd){
		        						selected = "selected='selected'";
		        					}
		        				}
		        				selStr += "<option value='"+cboDp2[i].yn+"' "+ selected +">"+cboDp2[i].yn+"</option>";
		        			}
		        			selStr += "</select>";
		        			selStr +='</div>';
		        			return selStr;
		        		}},
		            {key: 'cr_qacheck12', label: "테스트정확여부",	 width: "17%", align: "center",
		            	formatter: function(){
		            		var disabled = isEnable ? '' : 'disabled';
		        			var selStr = '<div class="select width-100">';
		        			selStr += "<select class='selBox width-100' onChange='qacheckChange(this,1)' " + disabled +">"
		        			for(var i=0; i<cboDp2.length; i++) {
		        				var selected = "";
		        				// 그리드 값이랑 같으면 selected 처리
		        				if (this.item.cr_qacheck != null && this.item.cr_qacheck != '') {
		        					if(this.item.cr_qacheck.substr(1,1) == cboDp2[i].cd){
		        						selected = "selected='selected'";
		        					}
		        				}
		        				selStr += "<option value='"+cboDp2[i].yn+"' "+ selected +">"+cboDp2[i].yn+"</option>";
		        			}
		        			selStr += "</select>";
		        			selStr +='</div>';
		        			return selStr;
		        		}
		            },
		            {key: 'cr_qacheck3', label: "타업무연계성여부",	 width: "17%", align: "center",
		            	formatter: function(){
		            		var disabled = isEnable ? '' : 'disabled';
		        			var selStr = '<div class="select width-100">';
		        			selStr += "<select class='selBox width-100' onChange='qacheckChange(this,2)' " + disabled +">"
		        			for(var i=0; i<cboDp2.length; i++) {
		        				var selected = "";
		        				// 그리드 값이랑 같으면 selected 처리
		        				if (this.item.cr_qacheck != null && this.item.cr_qacheck != '') {
		        					if(this.item.cr_qacheck.substr(2,1) == cboDp2[i].cd){
		        						selected = "selected='selected'";
		        					}
		        				}
		        				selStr += "<option value='"+cboDp2[i].yn+"' "+ selected +">"+cboDp2[i].yn+"</option>";
		        			}
		        			selStr += "</select>";
		        			selStr +='</div>';
		        			return selStr;
		        		}
		            }
	           ]
	        },
	        {key: 'cr_chgcode',		label: "변경사유코드",	 width: "20%", align: "center",
            	formatter: function(){
            		var disabled = isEnable ? '' : 'disabled';
            		var selStr = '<div class="select width-100">';
        			selStr += "<select class='selBox width-100' onChange='chgCodeChange(this)' " + disabled +">" 
        			for(var i=0; i<chgCodeData.length; i++) {
        				var selected = "";
        				// 그리드 값이랑 같으면 selected 처리
        				if (this.item.cr_chgcode != null && this.item.cr_chgcode != '') {
        					if(chgCodeData[i].cm_micode == this.item.cr_chgcode){
        						selected = "selected='selected'";
        					}
        				}
        				selStr += "<option value='"+chgCodeData[i].cm_micode+"' "+ selected +">"+chgCodeData[i].cm_codename+"</option>";
        			}
        			selStr += "</select>";
        			selStr +='</div>';
        			return selStr;
        		}
	        },
	        {key: 'cr_detailcode',	label: "변경상세코드",	 width: "20%", align: "center",
	        	formatter: function(){
	        		var disabled = isEnable ? '' : 'disabled';
            		var selStr = '<div class="select width-100">';
        			selStr += "<select class='selBox width-100' onChange='detailCodeChange(this)' " + disabled +">" 
        			for(var i=0; i<detailCodeData.length; i++) {
        				var selected = "";
        				// 그리드 값이랑 같으면 selected 처리
        				if (this.item.cr_detailcode != null && this.item.cr_detailcode != '') {
        					if(detailCodeData[i].cm_micode == this.item.cr_detailcode){
        						selected = "selected='selected'";
        					}
        				}
        				selStr += "<option value='"+detailCodeData[i].cm_micode+"' "+ selected +">"+detailCodeData[i].cm_codename+"</option>";
        			}
        			selStr += "</select>";
        			selStr +='</div>';
        			return selStr;
        		}
	        }
		]
	});
	
	if (commonData != null && commonData.length > 0) {
		secondGrid.setData(commonData);
		secondGrid.repaint();
	}
}

function qacheckChange(val,cd) {
	var selVal = $(val);
	var selIndex = Number($(val).parent().parent().parent().attr('data-ax5grid-data-index'));
	
	var selItem = commonData[selIndex];	// 그리드에서 가져옴
	var selVal 	= cboDp2[Number(selVal[0].selectedIndex)]; // 선택된 콤보박스정보
	if(selItem == null || selItem.length == 0) {
		return;
	}
	
	console.log('val=',cd);
	console.log('selVal=',selVal);
	secondGrid.list[selIndex].cr_qacheck = secondGrid.list[selIndex].cr_qacheck.replaceAt(Number(cd),selVal.cd);
}

function chgCodeChange(val) {
	var selVal = $(val);
	var selIndex = Number($(val).parent().parent().parent().attr('data-ax5grid-data-index'));
	
	var selItem = commonData[selIndex];	// 그리드에서 가져옴
	var selVal 	= chgCodeData[Number(selVal[0].selectedIndex)]; // 선택된 콤보박스정보
	if(selItem == null || selItem.length == 0) {
		return;
	}
	
	selItem.cr_chgcode = selVal.cm_micode;
	secondGrid.list[selIndex].cr_chgcode = selVal.cm_micode;
}

function detailCodeChange(val) {
	var selVal = $(val);
	var selIndex = Number($(val).parent().parent().parent().attr('data-ax5grid-data-index'));
	
	var selItem = commonData[selIndex];	// 그리드에서 가져옴
	var selVal 	= detailCodeData[Number(selVal[0].selectedIndex)]; // 선택된 콤보박스정보
	if(selItem == null || selItem.length == 0) {
		return;
	}
	
	selItem.cr_detailcode = selVal.cm_micode;
	secondGrid.list[selIndex].cr_detailcode = selVal.cm_micode;
} 

function screenInit() {
	isAdmin = window.parent.isAdmin;
	/*if(isAdmin) $('#btnRewriteQA').show();
	else $('#btnRewriteQA').hide();*/
}

function refresh(data) {
	commonData = [];
 	
 	if(data == null || data == undefined) setDefaultValue();
 	else commonData = clone(data);

 	//$('#btnTestSkip').css('display','none'); //테스트통과처리 안보이도록
 	
 	commonData = [commonData];
 	//console.log('commonData ==>',commonData);
    firstGrid.setData(commonData);
    secondGrid.setData(commonData);
}


function setSRTest_old(data) {
	thirdGrid.setData([]);
	srTestData = data;
	$('#txtTestReqYn').val('');
	$('#txtTestEndYn').val('');
	
	//if (srTestData.length>1) {
	for (var i=0;i<srTestData.length;i++) {		
		if (srTestData[i].baseyn != null && srTestData[i].baseyn != '' && srTestData[i].baseyn != undefined && srTestData[i].baseyn == 'Y') {
			srTestCommon = new Object();
			srTestCommon = srTestData[i];
			
			if (srTestCommon.testyn != null && srTestCommon.testyn != '' && srTestCommon.testyn != undefined) {
				$('#txtTestReqYn').val(srTestCommon.testyn);
				if (srTestCommon.testyn == 'Y') {
					window.parent.isSRTest = true;
					if (srTestCommon.testrst != null && srTestCommon.testrst != '' && srTestCommon.testrst != undefined && srTestCommon.testrst != 'F') window.parent.isSRTestEnd = true;
				}
			}
			
			if (srTestCommon.errmsg != null && srTestCommon.errmsg != '' && srTestCommon.errmsg != undefined) {
				if (srTestCommon.testyn == 'N') {  //테스트전담반요쳥여부 N인경우 clear
					$('#txtTestEndYn').val('');
				}else{
					$('#txtTestEndYn').val(srTestCommon.errmsg);
				}
			}  else {
				if (srTestCommon.cr_testskip != null && srTestCommon.cr_testskip != '' && srTestCommon.cr_testskip != undefined && srTestCommon.cr_testskip == 'Y') {
					window.parent.isSRTestSkip = true;
					if (srTestCommon.testrst != null && srTestCommon.testrst != '' && srTestCommon.testrst != undefined) skipData = srTestCommon.testrst;
					if (srTestCommon.skipinfo != null && srTestCommon.skipinfo != '' && srTestCommon.skipinfo != undefined) {
						if (skipData.length>0) skipData = skipData + ' : ';
						skipData = skipData + srTestCommon.skipinfo;			
					}
				} else if (srTestCommon.testrst != null && srTestCommon.testrst != '' && srTestCommon.testrst != undefined) {
					if (srTestCommon.testyn == 'N') {  //테스트전담반요쳥여부 N인경우 clear
						$('#txtTestEndYn').val('');
					}else{
						$('#txtTestEndYn').val(srTestCommon.testrst);
					}
				}
			}
		}
	}
	srTestData = srTestData.filter(function(data) {
		if(data.baseyn == 'Y') return false;
		else return true;
	});
	//secondGrid.setData(srTestData);
	thirdGrid.setData(srTestData);

	thirdGrid.setColumnSort({cc_testreq_yn:{seq:0, orderBy:'desc'}});
}

function setSRTest(data) {
    var i, r, r2;
    var baseRows    = [];
    var detailRows  = [];
    var hasAnyY     = false;
    var effectiveYN = 'N';   // 대표 testyn
    var endText     = '';
    var skipData    = '';

    // 0) 초기화
    thirdGrid.setData([]);

    // data → srTestData (배열이 아닐 수도 있으므로 방어코드)
    if (data == null) {
        srTestData = [];
    } else if ($.isArray && $.isArray(data)) {
        srTestData = data;
    } else if (typeof data.length === 'number') {
        // 유사 배열(ex. arguments 같은 것)
        srTestData = [];
        for (i = 0; i < data.length; i++) {
            srTestData.push(data[i]);
        }
    } else {
        // 단일 Object인 경우
        srTestData = [data];
    }

    $('#txtTestReqYn').val('');
    $('#txtTestEndYn').val('');

    try { window.parent.isSRTest     = false; } catch (e) {}
    try { window.parent.isSRTestEnd  = false; } catch (e) {}
    try { window.parent.isSRTestSkip = false; } catch (e) {}

    // 1) 기준행/상세행 분리 (filter 사용 안 함 → IE/환경 상관없이 동작)
    for (i = 0; i < srTestData.length; i++) {
        r = srTestData[i];
        if (r && r.baseyn === 'Y') baseRows.push(r);
        else                       detailRows.push(r);
    }

    // 2) 기준행 선정 (여러 개면 첫 번째)
    // 전역 srTestCommon(상단 var srTestCommon = null;)에 세팅해야
    // 부모창에서 tmpTab4.srTestCommon 으로 접근 가능함
    srTestCommon = null;
    if (baseRows.length > 0) {
        srTestCommon = baseRows[0];
    }

    // 3) 전체 데이터 중 하나라도 testyn == 'Y' 있는지 체크
    for (i = 0; i < srTestData.length; i++) {
        r2 = srTestData[i];
        if (r2 && r2.testyn === 'Y') {
            hasAnyY = true;
            break;
        }
    }

    if (srTestCommon && srTestCommon.testyn) {
        effectiveYN = srTestCommon.testyn;
    } else {
        effectiveYN = hasAnyY ? 'Y' : 'N';
    }

    // 4) 대표 testyn 화면/플래그 반영
    $('#txtTestReqYn').val(effectiveYN);

    if (effectiveYN === 'Y') {
        try { window.parent.isSRTest = true; } catch (e) {}

        if (srTestCommon &&
            srTestCommon.testrst &&
            srTestCommon.testrst !== 'F') {
            try { window.parent.isSRTestEnd = true; } catch (e) {}
        }
    }

    // 5) 종료값(txtTestEndYn) 결정
    // 우선순위: N → errmsg → (cr_testskip: testrst[: ]skipinfo) → testrst → ''
    if (effectiveYN === 'N') {
        endText = '';
    } else if (srTestCommon && srTestCommon.errmsg) {
        endText = srTestCommon.errmsg;
    } else if (srTestCommon && srTestCommon.cr_testskip === 'Y') {
        try { window.parent.isSRTestSkip = true; } catch (e) {}

        if (srTestCommon.testrst)   skipData = srTestCommon.testrst;
        if (srTestCommon.skipinfo) {
            if (skipData.length > 0) skipData += ' : ';
            skipData += srTestCommon.skipinfo;
        }
        endText = skipData;
    } else if (srTestCommon && srTestCommon.testrst) {
        endText = srTestCommon.testrst;
    } else {
        endText = '';
    }
    $('#txtTestEndYn').val(endText);

    // 6) 그리드 바인딩 + 정렬
    thirdGrid.setData(detailRows);

    if (typeof thirdGrid.setColumnSort === 'function') {
        thirdGrid.setColumnSort({ cc_testreq_yn: { seq: 0, orderBy: 'desc' } });
    }
}

function getCodeInfo(){
	var codeInfos = getCodeInfoCommon([
		new CodeInfoOrdercd('SAYUCODE','SEL','N'),
		new CodeInfoOrdercd('SAUDETAIL','SEL','N'),
	]);
	
	chgCodeData = codeInfos.SAYUCODE;
	detailCodeData = codeInfos.SAUDETAIL;
}

function setDefaultValue() {
	var defaultObj = new Object();
	defaultObj.cr_totalcheck = "0000";
    defaultObj.cr_qacheck = "XXX";
    defaultObj.cr_devptime = "0";
    defaultObj.cr_chgcode = "00";
    defaultObj.cr_detailcode = "00";
    commonData.push(defaultObj);
    defaultObj = null;
}

function getData() {
	if(chkSwValue() && commonData.length == 1) return commonData[0];
	return null;
}

function chkSwValue() {
    // 스위치 리셋
    devTimeSw = qaSw = gradeSw = sayuSw = detailSw = false;

    var missing = [];
    var data = commonData[0]; // 단일행 기준

    // 1) 상태 계산
    if (Number(data.cr_devptime) > 0) devTimeSw = true;            else missing.push("책임자확인-개발기간");
    if (data.cr_totalcheck.indexOf('0') < 0) qaSw = true;          else missing.push("성과 및 종합판정");
    if (data.cr_qacheck.indexOf('X') < 0) gradeSw = true;          else missing.push("책임자확인");
    if (data.cr_chgcode != '00') sayuSw = true;                    else missing.push("책임자확인-변경사유코드");
    if (data.cr_detailcode != '00') detailSw = true;               else missing.push("책임자확인-변경상세코드");

    // 2) 모두 OK → true 반환
    if (devTimeSw && qaSw && gradeSw && sayuSw && detailSw) {
        return true;
    }

    // 3) 누락 항목 안내 메시지
    var msg;

    if (missing.length === 1) {
        //dialog.alert("아래 항목을 먼저 설정해 주세요.\n\n- " + missing[0]);
    	msg = "책임자/Q.A확인 탭 \n아래 항목을 먼저 설정해 주세요.\n\n- " + missing[0];
    } else {
        //dialog.alert("아래 항목이 미설정 상태입니다.\n\n- " + missing.join("\n- "));
    	msg = "책임자/Q.A확인 탭 \n아래 항목이 미설정 상태입니다.\n\n- " + missing.join("\n- ");
    }
    
    var pwd = window.parent || window.top;
    
    if(pwd && pwd.dialog && typeof pwd.dialog.alert === 'function'){
    	pwd.dialog.alert(msg);
    }else{
    	alert(msg);
    }
    return false;
}

/*
function chkSwValue() {
	if(Number(commonData[0].cr_devptime) > 0) devTimeSw = true;
	if(commonData[0].cr_totalcheck.indexOf('0') < 0) qaSw = true;
	if(commonData[0].cr_qacheck.indexOf('X') < 0) gradeSw = true;
	if(commonData[0].cr_chgcode != '00') sayuSw = true;
	if(commonData[0].cr_detailcode != '00') detailSw = true;
	
	if(devTimeSw && qaSw && gradeSw && sayuSw && detailSw) {
		return true;
	}else {
		//dialog.alert("Q.A 결재 항목을 설정해 주십시오.");
		dialog.alert("수정항목을 설정하여 주시기 바랍니다.");
		return false;
	}
}
*/
function btnRewrite_Click() {
	var data = new Object();
	data = {
		Acptno : window.parent.pReqNo,
		time   : commonData[0].cr_devptime,
		requestType : 'setDevTime'
	}
	console.log('[btnRewrite_Click] ==>',data);
	ajaxAsync('/webPage/ecmr/Cmr0250Servlet', data, 'json', successSetDevTime);
}

function successSetDevTime(data) {
	if(data == "No") dialog.alert("개발기간 수정 실패");
	else dialog.alert("개발기간 수정 완료");
}

function btnRewriteQA_Click() {
	var OAObj = new Object();
	QAObj = getData();
	
	console.log('[QAObj] ==>', QAObj);
	if(QAObj == null) return;
	
	var data = new Object();
	data = {
		Acptno : window.parent.pReqNo,
		etcData : QAObj,
		requestType : 'setQACheck'
	}
	console.log('[setQACheck] ==>', data);
	ajaxAsync('/webPage/ecmr/Cmr0250Servlet', data, 'json', successSetQACheck);
	
	QAObj = null;
}

function successSetQACheck(data) {
	if (data != undefined && data != null) {
		if (typeof data == 'string' && data.indexOf('ERROR')>-1) {
			dialog.alert(data);
			return;
		}
	}
	
	if(data == 'Yes') dialog.alert("수정을 완료하였습니다.");
	else dialog.alert("수정 중 오류가 발생하였습니다.["+ data + "]");
}


function btnTestSkip_Click() {
	var tmpObj = new Object();
	tmpObj.userid = window.parent.pUserId;
	tmpObj.acptno = window.parent.pReqNo
	
	var data = new Object();
	data = {
		 srData : tmpObj,
		requestType : 'setTestSkip'
	}
	console.log('[setTestSkip] ==>', data);
	ajaxAsync('/webPage/ecmr/Cmr0250Servlet', data, 'json', successSetTestSkip);
	
	tmpObj = null;
}

function successSetTestSkip(data) {
	if (data != undefined && data != null) {
		if (typeof data == 'string' && data == 'OK') {
			dialog.alert("테스트통과처리를 완료하였습니다.");
			
			window.parent.getSRList('N');
			return;
		}
	}
}

// [QATab.js] 파일 하단(전역 스코프)에 추가
function getQaPassYn() {
    try {
        var $el = $('#chkTestResultPass');
        if ($el.length === 0) return 'N';
        return $el.is(':checked') ? 'Y' : 'N';
    } catch (e) {
        return 'N';
    }
}

function setQaPassVisible(show) {
  try {
    var $el = $('#chkTestResultPass');
    var $wrap = $el.closest('.dib');
    ($wrap.length ? $wrap : $el)[show ? 'show' : 'hide']();

    // 1) 표준 DOM 상태
    $el.prop('disabled', !show);
    if (show === true) $el.removeAttr('disabled'); // 일부 브라우저/스킨 대비

    // 2) wCheck 스킨 동기화(지원 시)
    if (typeof $el.wCheck === 'function') {
      $el.wCheck('disabled', !show);
    }
  } catch (e) {}
}

function initQaCheckbox() {
  var $el = $('#chkTestResultPass');
  if ($el.length === 0) return;

  // data-label 보이게: wCheck는 input 자신에 data-label이 있어야 렌더함
  if (!$el.attr('data-label')) {
    $el.attr('data-label', '테스트전담반결과PASS'); // 화면 문구
  }

  // wCheck 스킨 적용
  if (typeof $el.wCheck === 'function') {
    $el.addClass('checkbox-detail').wCheck({
      theme: 'square-inset blue',
      selector: 'checkmark',
      highlightLabel: true
    });
  }
}
