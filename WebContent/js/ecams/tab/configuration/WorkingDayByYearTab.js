
var userName 	= window.parent.userName;		// 접속자 Name
var userId 		= window.parent.userId;			// 접속자 ID
var adminYN 	= window.parent.adminYN;		// 관리자여부
var userDeptName= window.parent.userDeptName;	// 부서명
var userDeptCd 	= window.parent.userDeptCd;		// 부서코드
var codeList    = window.top.codeList;      	//전체 코드 리스트

var firstGrid		= new ax5.ui.grid();

var firstGridData 	= null;
var cboYearData		= [];


firstGrid.setConfig({
    target: $('[data-ax5grid="firstGrid"]'),
    sortable: true, 
    multiSort: true,
    showRowSelector: false,
    showLineNumber: true,
    header: {
        align: "center"
    },
    body: {
        onClick: function () {
        	this.self.clearSelect();
            this.self.select(this.dindex);
            gridCick(this.item);
        },
        onDBLClick: function () {},
    	trStyleClass: function () {},
    	onDataChanged: function(){
    		this.self.repaint();
    	}
    },
    columns: [
        {key: "year", 		label: "연도",  		width: '46%' },
        {key: "monthday", 	label: "근무일수",  	width: '46%' }
    ]
});


$('[data-ax5select="cboYear"]').ax5select({
    options: []
});


$(document).ready(function() {
	setYearCbo();
	getWorkingDays();
	// 등록 
	$('#btnReq').bind('click', function() {
		insertOperTime(1);
	});
	
	// 수정 
	$('#btnChange').bind('click', function() {
		insertOperTime(2);
	});
});

// 등록
function insertOperTime(gubun) {
	
	var i;
	var tmpWork1 = "";
	
	if(gubun==1){
		if (getSelectedIndex('cboYear') < 1){
			dialog.alert("해당연도를 확인하여 주십시오.");
			return;
		}else{
			for(i=0;i<firstGridData.length;i++){
				if(firstGridData[i].year == getSelectedVal('cboYear').cm_codename){
					dialog.alert("해당연도를 확인하여 주십시오.");
					return;
				}
			}
		}
	}else if(gubun==2){
		var gu = "";
		for(i=0;i<firstGridData.length;i++){
			if(firstGridData[i].year == getSelectedVal('cboYear').cm_codename){
				gu="ok";
				break;
			}
		}
		if(gu != "ok"){
			dialog.alert("수정하실 해당연도를 확인하여 주십시오.");
			return;
		}
	}
	
	if ($("#txtWorkDays").val().indexOf(".") > 0) {
	 	tmpWork1 = $("#txtWorkDays").val().substr($("#txtWorkDays").val().indexOf(".")+1);
	 	if (tmpWork1.length > 2) {
	 		dialog.alert("근무일수는 소숫점 2자리까지 입력이 가능합니다.");
//	 		txtWorkday.setFocus();
	 		return;
	 	}	
	}
	
	if ($("#txtWorkDays").val().length == 0){
		dialog.alert("근무일수를 확인하여 주십시오.");
		return;
	}
	
	var data = new Object();
	data = {
		year : getSelectedVal('cboYear').cm_codename,
		day : $("#txtWorkDays").val(),
		requestType	: 'addTab7Info'
	}
	ajaxAsync('/webPage/administrator/Configuration', data, 'json',successInsertOperTime);
}

// 등록 완료
function successInsertOperTime(data) {
	
}


function getWorkingDays() {
	var data = new Object();
	data = {
		requestType	: 'getTab7Info'
	}
	ajaxAsync('/webPage/administrator/Configuration', data, 'json',successGetWorkingDays);
}

function successGetWorkingDays(data) {
	firstGridData = data;
	firstGrid.setData(firstGridData);
}

// 시간구분가져오기
function setYearCbo() {
	
	var nowYear = parseInt(getDate('DATE', 0).substr(0,4));
	cboYearData.push({cm_micode: '00', cm_codename: "선택하세요"});
	cboYearData.push({cm_micode: '01', cm_codename: nowYear});
	cboYearData.push({cm_micode: '02', cm_codename: nowYear+1});
	cboYearData.push({cm_micode: '03', cm_codename: nowYear+2});
	
	$('[data-ax5select="cboYear"]').ax5select({
        options: injectCboDataToArr(cboYearData, 'cm_micode' , 'cm_codename')
	});
}

function gridCick(item) {
	$.each(cboYearData, function(i, val) {
		if(val.cm_codename == item.year) {
			$("#cboYear").ax5select("setValue", i);
		}
	});
	$("#txtWorkDays").val(item.monthday);
}