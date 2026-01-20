/**
 * 정기배포일괄등록 팝업 화면 기능 정의
 * 
 * <pre>
 * 	작성자	: 이용문
 * 	버전 		: 1.0
 *  수정일 	: 2019-05-29
 * 
 */
var userName 	= window.parent.userName;		// 접속자 Name
var userId 		= window.parent.userId;			// 접속자 ID
var adminYN 	= window.parent.adminYN;		// 관리자여부
var userDeptName= window.parent.userDeptName;	// 부서명
var userDeptCd 	= window.parent.userDeptCd;		// 부서코드

var releaseGrid = new ax5.ui.grid();
var releaseGridData = null;

releaseGrid.setConfig({
    target: $('[data-ax5grid="releaseGrid"]'),
    sortable: false, 
    multiSort: false,
//    showRowSelector: true,
    header: {
        align: "center",
    },
    body: {
        onClick: function () {
            this.self.select(this.dindex);
            this.self.clearSelect();
        },
        onDBLClick: function () {},
    	trStyleClass: function () {},
    	onDataChanged: function(){
    		this.self.repaint();
    	}
    },
    columns: [
    	{key: "checked", 	label: "정기배포사용",  	width: '25%', align: 'left', formatter: function() {
    		
    		var checked = this.item.checked;
    		return '<input id="optCheck'+this.item.cm_syscd+'" type="radio" name="'+this.item.cm_syscd+'" class="gridCkT" '+((checked == "true")?"checked":"")+' /> <label for="optCheck'+this.item.cm_syscd+'">설정</label>'
    		+'<input id="optUnCheck'+this.item.cm_syscd+'" type="radio" name="'+this.item.cm_syscd+'" class="gridCkF" '+((checked == "false")?"checked":"")+'/> <label for="optUnCheck'+this.item.cm_syscd+'">해제</label>';
    		
    	}},
        {key: "cm_sysmsg", 	label: "시스템명",  		width: '45%', align: 'left'},
        {key: "cm_systime", label: "배포시간", 		width: '30%'}
    ]
});

$('input:radio[name=releaseChkS]').wRadio({theme: 'circle-radial blue', selector: 'checkmark'});
$('input:radio[name=releaseChk]').wRadio({theme: 'circle-radial blue', selector: 'checkmark'});

$('input.checkbox-rel').wCheck({theme: 'square-inset blue', selector: 'checkmark', highlightLabel: true});

$('#txtBuildTime').timepicker({
	direction:'top',
	timeFormat: 'HH:mm',
    interval: 30,
    dynamic: false,
    dropdown: false,
    scrollbar: true
});

//설정, 해제 라디오 버튼 클릭 할때마다 유지되도록
$(document).on('click','.gridCkT, .gridCkF', function(){
	var syscd = this.name;
	$(releaseGridData).each(function(){
		if(this.cm_syscd == syscd){
			if($("#optCheck"+this.cm_syscd).is(":checked")){
				this.checked = "true";
			} else {
				this.checked = "false";
			}
		}
	});
});

$('#txtDeployTime').timepicker({
	direction:'top',
	timeFormat: 'HH:mm',
    interval: 30,
    dynamic: false,
    dropdown: false,
    scrollbar: true
});

$(document).ready(function(){
	
	$('#optAll').wRadio('check', true);
	
	getReleaseTime();
	
	// 숫자만 입력
	$("#txtBuildTime").on("keyup", function(event) {
		if(event.keyCode === 13) {
			$("#txtBuildTime").focusout();
		} else {
			$(this).val($(this).val().replace(/[^0-9]/g,""));
		}
	    
	});
	
	// 숫자만 입력
	$("#txtDeployTime").on("keyup", function(event) {
		if(event.keyCode === 13) {
			$("#txtBuildTime").focusout();
		} else {
			$(this).val($(this).val().replace(/[^0-9]/g,""));
		}
	});

	// 전체선택 , 해제
	$('input:radio[name=releaseChk]').bind('click', function() {
		if($("input:radio[name=releaseChk]:checked").val() == "optCheck"){
			$(".gridCkT").prop("checked", true);

			$(releaseGridData).each(function(){
				this.checked = "true";
			});
		} else {
			$(".gridCkF").prop("checked", true);

			$(releaseGridData).each(function(){
				this.checked = "false";
			});
		}
	});
	
	// 전체/정기배포대상/정기비배포대상 라디오 클릭
	$('input:radio[name=releaseChkS]').bind('click', function() {
		releaseGridFilter();
	});
	
	// 해제 클릭
	$('input:radio[id=optUnCheck]').bind('click', function() {
		$('#chkSun').wCheck('check', false);
		$('#chkMon').wCheck('check', false);
		$('#chkTue').wCheck('check', false);
		$('#chkWed').wCheck('check', false);
		$('#chkThu').wCheck('check', false);
		$('#chkFri').wCheck('check', false);
		$('#chkSat').wCheck('check', false);
		
		$('#txtBuildTime').val('');
		$('#txtDeployTime').val('');
	});
	
	// 시스템명 엔터
	$('#txtSysMsg').bind('keydown', function(event) {
		if(event.keyCode === 13) {
			releaseGridFilter();
		}
	});
	
	// 조회
	$('#btnSearch').bind('click',function() {
		getReleaseTime();
	});
	
	// 등록
	$('#btnReleaseTimeSet').bind('click',function() {
		setReleaseTime();
	});
	
	// 닫기
	$('#btnClose').bind('click',function() {
		window.parent.relModal.close();
	});
});

// 그리드 데이터 세팅 전 화면 값에 따라서 필터
function releaseGridFilter() {
	$(".loding-div").remove();
	releaseGrid.setData(releaseGridData);
}

function popClose(){
	window.parent.relModal.close();
}

// 정기배포 설정값 등록
function setReleaseTime(txtTime) {
	var etcData = new Object();
	var syslist = '';
	var selIn 	= releaseGrid.selectedDataIndexs;
	var txtDeployTime 	= $('#txtDeployTime').val().trim();
	txtDeployTime 	= replaceAllString(txtDeployTime,':','');
	var releaseGridData = releaseGrid.getList();
	var chkYN = false;
	$(releaseGridData).each(function(){
		
		if($("#optCheck"+this.cm_syscd).is(":checked")){
			this.checked = "true";
			chkYN = true;
		} else {
			this.checked = "false";
		}
	});
	
	if(chkYN && txtDeployTime.length < 4){
		dialog.alert('정기배포시간를 확인하여 주세요.['+txtDeployTime+']',function(){});
		return;
	}
	
	console.log(releaseGridData);
	
	var systemInfoDta = new Object(); 
	systemInfoDta = {
		requestType		: 'setReleaseTime',
		sysList 		: releaseGridData,
		releaseTime 		: txtDeployTime,
	}
	ajaxAsync('/webPage/ecmm/Cmm0200Servlet', systemInfoDta, 'json',successSetReleaseTime);
}

function successSetReleaseTime(data) {
	var msg = '';
	if(data === 0) {
		msg = "등록완료~!";
		dialog.alert(msg, function(){
						getReleaseTime();
					});
	} else {
		dialog.alert('정기배포설정중 오류가 발생했습니다. 관리자에게 문의하시기 바랍니다.',function(){});
	}
}

// 정기배포 설정값 조회
function getReleaseTime() {

	$('[data-ax5grid="releaseGrid"] [data-ax5grid-container="root"] [data-ax5grid-container="body"]').append(loading_div);
	$(".loding-div").show();
	
	var systemInfoDta = new Object(); 
	systemInfoDta = {
		requestType	: 	'getReleaseTime'
	}
	ajaxAsync('/webPage/ecmm/Cmm0200Servlet', systemInfoDta, 'json',successgetReleaseTime);
}

// 정기배포 설정값 조회 결과
function successgetReleaseTime(data) {
	var rowSelector = $('[data-ax5grid-column-attr="rowSelector"]');
	$(rowSelector).attr('data-ax5grid-selected',"false");
	
	releaseGridData = data;
	releaseGridFilter();
	
	$('input:radio[name^="release"]').wRadio({theme: 'circle-radial red', selector: 'checkmark'});
}