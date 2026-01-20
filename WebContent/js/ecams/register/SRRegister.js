/**
 * SR등록 화면 기능 정의
 * <pre>
 * <b>History</b>
 * 	작성자:
 * 	버전 : 1.0
 *  수정일 : 2019-00-00
 */

var userName 	 	= window.top.userName;
var userId 		 	= window.top.userId;
var adminYN 		= window.top.adminYN;
var userDeptName 	= window.top.userDeptName;
var userDeptCd 	 	= window.top.userDeptCd;
var strReqCd	 	= window.top.reqCd;
var selectedSr		= null;
var testFlag = false;
var cboQryGbnData = [];

var txtUserId = "";
var txtUserName = "";
var deptCd = "";
var deptName = ""; 
var subSw = false;
var selDeptSw = false;
var selDeptCd = "";
var txtOrg = "";

var loadSrReg = false;
var picker 		= new ax5.ui.picker();
var focusCk = false;

//이 부분 지우면 영어명칭으로 바뀜
//ex) 월 -> MON
ax5.info.weekNames = [
{label: "일"},
{label: "월"},
{label: "화"},
{label: "수"},
{label: "목"},
{label: "금"},
{label: "토"}
];


var organizationModal = new ax5.ui.modal(); // 조직도 팝업
/******************** eCmc0100.mxml ********************/

$(document).ready(function(){
	var timeOut = null;
	var timeOut2 = null;
	
	document.getElementById('frmSRRegister').onload = function() {
		loadSrReg = true;
	}

//	$('#datStD').val(getDate('DATE',-1));
//	$('#datEdD').val(getDate('DATE',0));
//
//	//picker.bind(defaultPickerInfo('basic', 'top'));
//	
//	$("#datStD, #datEdD").bind('change', function(){
//		var dataId = $(this).prop("id");
//		var dataStd = $('#frmPrjList').contents().find("#"+dataId).val($(this).val());
//	});
//	
//	$(document).on("focusout",function(){
//		setTimeout(function(){
//			if(document.activeElement instanceof HTMLIFrameElement){
//				$('[data-picker-btn="ok"]').click();
//			}
//		},0);
//	});
//	
//	$('.btn_calendar').bind('click', function(e) {
//		e.preventDefault();
//	    e.stopPropagation();
//		if($(this).css('background-color') === 'rgb(255, 255, 255)') {
//			var inputs = $(this).siblings().prevAll('input');
//			$(inputs.prevObject[0]).trigger('click');
//		}
//	});
//
//	picker.bind({
//		target: $('[data-ax5picker="basic"]'),
//		direction: "top",
//		content: {
//			width: 220,
//			margin: 10,
//			type: 'date',
//			config: {
//				control: {
//					left: '<i class="fa fa-chevron-left"></i>',
//					yearTmpl: '%s',
//					monthTmpl: '%s',
//					right: '<i class="fa fa-chevron-right"></i>'
//				},
//				dateFormat: 'yyyy/MM/dd',
//				lang: {
//					yearTmpl: "%s년",
//					months: ['01', '02', '03', '04', '05', '06', '07', '08', '09', '10', '11', '12'],
//					dayTmpl: "%s"
//				},dimensions: {
//					height: 140,
//					width : 75,
//					colHeadHeight: 11,
//					controlHeight: 25,
//				}
//			},
//			formatter: {
//				pattern: 'date'
//			}
//		},
//		onStateChanged: function () {
//	        if (this.state == "open") {
//	            var selectedValue = this.self.getContentValue(this.item["$target"]);
//	            if (!selectedValue) {
//	                this.item.pickerCalendar[0].ax5uiInstance.setSelection([ax5.util.date(new Date(), {'add': {d: 0}})]);
//	            }
//	        }
//	        if(this.state == "changeValue"){
//	    		$("#btnStD").focus();
//	        }
//	    },
//		btns: {
//			today: {
//				label: "Today", onClick: function () {
//					var today = new Date();
//					this.self
//					.setContentValue(this.item.id, 0, ax5.util.date(today, {"return": "yyyy/MM/dd"}))
//					.setContentValue(this.item.id, 1, ax5.util.date(today, {"return": "yyyy/MM/dd"}))
//					.close();
//				}
//			},
//			thisMonth: {
//				label: "This Month", onClick: function () {
//					var today = new Date();
//					this.self
//					.setContentValue(this.item.id, 0, ax5.util.date(today, {"return": "yyyy/MM/01"}))
//					.setContentValue(this.item.id, 1, ax5.util.date(today, {"return": "yyyy/MM"})
//							+ '/'
//							+ ax5.util.daysOfMonth(today.getFullYear(), today.getMonth()))
//							.close();
//				}
//			},
//			ok: {label: "Close", theme: "default"}
//		}
//	});
//	$(document).on("click",".ax5-ui-picker",function(){
//		$("#btnStD").focus();
//	});

	var oldVal = "";
	// 기간 선택 달력의 경우 두번째 달력 값 선택 또는 변경시 자동으로 달력 닫히게 추가
	$('#datEdD').bind('propertychange change keyup paste input', function() {
		var currentVal =  $("#datEdD").val();
		if(currentVal != oldVal){
			picker.close();
		}
		oldVal = currentVal;
	});
});
	
// 페이지 로딩 완료시 다음 진행 
var inter = null;

function callSRRegister(data) {
   inter = setInterval(function(){
      if(loadSrReg) {
         iSRID_Click(data);
         clearInterval(inter);
      }
   },100);
}

//소소조직 선택 창 오픈
function openOranizationModal() {
	setTimeout(function() {
		organizationModal.open({
			width : 400,
			height : 700,
			iframe : {
				method : "get",
				url : "../modal/OrganizationModal.jsp",
				param : "callBack=modalCallBack"
			},
			onStateChanged : function() {
				if (this.state === "open") {
					mask.open();
				} else if (this.state === "close") {
					mask.close();
					if(subSw === true && selDeptSw === false){
						var tmpTab = $('#frmSRRegister').get(0).contentWindow;
						
						tmpTab.txtUserId = txtUserId;
						tmpTab.txtUserName = txtUserName;
						tmpTab.deptName = deptName;
						tmpTab.deptCd = deptCd;
						tmpTab.selDeptSw = selDeptSw; 
						tmpTab.subSw = subSw;
						tmpTab.closeModal();
					} else {
						if(selDeptSw) {
							var tmpTab = $('#frmSRRegister').get(0).contentWindow;
							tmpTab.selDeptCd = selDeptCd;
							tmpTab.txtOrg = txtOrg;
							tmpTab.selDeptSw = selDeptSw;
							tmpTab.subSw = subSw;
							tmpTab.closeModal()
						}
					}
				}
			}
		}, function() {
		});
	}, 200);
}

function subScreenInit() {
	$('#frmSRRegister').get(0).contentWindow.elementInit("NEW"); //tab1.screenInit("NEW");
}

function subCmdQry_Click() {
	$('#frmPrjList').get(0).contentWindow.getPrjList(); //tab0.cmdQry_click();
}

//PrjListTab 에서 그리드 클릭 이벤트
function iSRID_Click(data) {
	var tmpTab = $('#frmSRRegister').get(0).contentWindow;
	if(tmpTab != null) {
		tmpTab.firstGridClick(data); 
	}
}