/**
 * [일괄등록 > 맵핑] 화면 기능정의
 * 
 * <pre>
 * 	작성자	: 이용문
 * 	버전 		: 1.0
 *  수정일 	: 2019-06-28
 * 
 */
var userName 	= $('#userName').val();
var userId 		= $('#userId').val();
var adminYN 	= $('#adminYN').val();
var strReqCD    = $('#strReqCD').val();

var mapGrid	= new ax5.ui.grid();

var mapGridData		= [];
var fMapGridData 	= [];
var cboSysCdData	= [];

var templetPath		= '';
var tmpPath			= '';
var uploadJspFile 	= '';
var errSw			= false;

mapGrid.setConfig({
    target: $('[data-ax5grid="mapGrid"]'),
    sortable: true, 
    multiSort: true,
    header: {
        align: "center"
    },
    body: {
        onClick: function () {
            this.self.select(this.dindex);
        },
        onDBLClick: function () {},
        trStyleClass: function () {
    		if(this.item.errsw === '1'){
    			return "fontStyle-cncl";
    		} else if(this.item.errsw === '0'){
    			return "fontStyle-ing";
    		}
    	},
    	onDataChanged: function(){
    		this.self.repaint();
    	}
    },
    columns: [
        {key: "srcdir", 	label: "소스디렉토리",  	width: '30%', align: "left"},
        {key: "srcfile",	label: "소스파일",  		width: '15%', align: "left"},
        {key: "moddir", 	label: "실행모듈디렉토리",  	width: '30%', align: "left"},
        {key: "modfile", 	label: "실행모듈",  		width: '15%', align: "left"},
        {key: "errmsg", 	label: "체크결과",  		width: '10%', align: "left"},
    ]
});

$('[data-ax5select="cboSysCd"]').ax5select({
    options: []
});

$('input:radio[name^="radio"]').wRadio({theme: 'circle-radial blue', selector: 'checkmark'});
$('input.checkbox-batch').wCheck({theme: 'square-inset blue', selector: 'checkmark', highlightLabel: true});

$(document).ready(function() {
	getSysInfoMap();
	getTmpDir();
	getDocPath();
	
	$('#btnReq').prop( "disabled", true);
	
	// 정산건, 오류건, 전체 라이도 버튼 클릭
	$('input:radio[name^="radio"]').bind('click', function() {
		gridDataFilter();
	});
	// 정상건만 등록 클릭
	$('#chkOk').bind('click', function() {
		var checkSw = $('#chkOk').is(':checked');
		
		if(fMapGridData.length > 0 && checkSw) {
			$('#btnReq').prop( "disabled", 	false);
		} else {
			if(errSw) {
				$('#btnReq').prop( "disabled", 	true);
			}
		}
	});
	// 엑셀파일
	$('#btnLoadExl').bind('click', function() {
		if(getSelectedIndex('cboSysCd') < 1) {
			dialog.alert('시스템 선택 후 사용해 주시기 바랍니다.', function() {});
			return;
		}
		$('#excelFile').trigger('click');
	});
	// 엑셀 파일 선택
	$('#excelFile').bind('change', function() {
		fileTypeCheck(this);
	})
	// 일괄등록
	$('#btnReq').bind('click', function() {
		requestCheckIn();
	});
	// 엑셀저장
	$('#btnSaveExl').bind('click', function() {
		mapGrid.exportExcel('소스모듈맵핑리스트.xls');
	});
	// 엑셀템플릿
	// fileUploadServlet 사용.
	$('#btnExlTmp').bind('click', function() {
//		location.href = homePath + '/webPage/fileupload/upload?fileName=excel_templet_cmm1800.xls&fullPath='+templetPath + '/excel_templet_cmm1800.xls';
		mapGrid.config.columns[4].excel = false;
		mapGrid.setData([]);
		mapGrid.exportExcel('excel_templet_'+userId+'.xlsx');
		mapGrid.config.columns[4].excel = true;
	});
});

// 소스모듈맵핑 일괄등록 신청
function requestCheckIn() {
	var chkInList = [];
	if(getSelectedIndex('cboSysCd') < 1) {
		dialog.alert('시스템을 선택하여 주세요.' , function() {});
		return;
	}
	if(fMapGridData.length === 0 ) {
		dialog.alert('엑셀 DATA가 없습니다.' , function() {});
		return;
	}
	
	mapGridData.forEach(function(item, index) {
		if(item.errsw === '0') {
			chkInList.push(item);
		}
	});
	
	if(chkInList.length === 0) {
		dialog.alert('등록할 DATA가 없습니다.' , function() {});
		return;
	}
	
	var data = new Object();
	data = {
		chkInList 	: chkInList,
		UserId 		: userId,
		requestType	: 'relatUpdt'
	}
	ajaxAsync('/webPage/ecmm/Cmm1600Servlet', data, 'json',successRelatUpdt);
}

// 소스모듈맵핑 일괄등록 신청 완료
function successRelatUpdt(data) {
	if(data.substr(0,5) === 'ERROR') {
		dialog.alert(data.substr(5), function() {});
	} else {
		dialog.alert('실행모듈 일괄등록 완료', function() {});
	}
	mapGridData = [];
	gridDataFilter();
}

// 라디오 버튼에 따른 그리드 필터
function gridDataFilter() {
	mapGrid.clearSelect();
	fMapGridData = [];
	var checkVal = $(':input:radio[name=radio]:checked').val();
	if(checkVal === undefined) {
		fMapGridData = mapGridData;
	} else {
		mapGridData.forEach(function(item, index) {
			if(checkVal === 'normal' && item.errsw === '0') {
				fMapGridData.push(item);
			} else if (checkVal === 'err' && item.errsw === '1') {
				fMapGridData.push(item);
			}else if(checkVal === 'all'){
				fMapGridData.push(item);
			}
		});
	}
	mapGrid.setData(fMapGridData);
}

// 엑셀 템플릿이 있는 경로 가져오기
function getDocPath() {
	var data = new Object();
	data = {
		pCode 		: '21',
		requestType	: 'getTmpDir'
	}
	ajaxAsync('/webPage/common/SystemPathServlet', data, 'json',successGetDocPath);
}

// 엑셀 템플릿이 있는 경로 가져오기 완료
function successGetDocPath(data) {
	templetPath = data;
	if(typeof(templetPath) == "object"){
		templetPath = data[0].cm_path;
	}
}

// 엑셀 파일 업로드시 파일 올릴 경로 가져오기
function getTmpDir() {
	var data = new Object();
	data = {
		pCode 		: '99',
		requestType	: 'geteCAMSDir'
	}
	ajaxAsync('/webPage/common/SystemPathServlet', data, 'json',successGetTmpDir);
}
// 엑셀 파일 업로드시 파일 올릴 경로 가져오기 완료 AND 업로드할때 사용할 JSP 파일명 가져오기
function successGetTmpDir(data) {
	tmpPath = data;
	if(typeof(tmpPath) == "object"){
		tmpPath = data[0].cm_path;
	}
	var data = new Object();
	data = {
		pCode 		: 'F2',
		requestType	: 'getTmpDir'
	}
	ajaxAsync('/webPage/common/SystemPathServlet', data, 'json',successGetUploadJspName);
}
// 업로드할때 사용할 JSP 파일명 가져오기 완료
function successGetUploadJspName(data) {
	uploadJspFile = data;
	if(typeof(uploadJspFile) == "object"){
		uploadJspFile = data[0].cm_path;
	}
}

// 엑셀 파일 업로드시 파일타입 체크
function fileTypeCheck(obj) {
	var pathpoint = obj.value.lastIndexOf('.');
	var filepoint = obj.value.substring(pathpoint+1,obj.length);
	filetype = filepoint.toLowerCase();
	if(filetype=='xls' || filetype=='xlsx') {
		startUpload();
	} else {
		dialog.alert('엑셀 파일만 업로드 가능합니다.', function() {});
		parentObj  = obj.parentNode
		node = parentObj.replaceChild(obj.cloneNode(true),obj);
		return false;
	}
}
// 엑셀 파일을 임시 경로에 업로드
function startUpload() {
	var excelFileSub = $('#excelFile')[0].files[0];
	var excelFile = null;
	
	// 파일 업로드 jsp 를 호출해야함
	var formData = new FormData();
	
	// 테스트 임시경로
	//tmpPath = 'C:\\fileupload\\tmp\\';
	formData.append('fullName',tmpPath+"/"+userId+"_excel_eCmm1800.tmp");
	formData.append('fullpath',tmpPath);
	formData.append('file',excelFileSub);
	
    $.ajax({
        url: '/webPage/fileupload/'+uploadJspFile,
        type:'POST',
        data:formData,
        async:false,
        cache:false,
        contentType:false,
        processData:false
    }).done(function(response){
    	onUploadCompleteData(response);
    }).fail(function(xhr,status,errorThrown){
    	alert('오류가 발생했습니다. \r 오류명 : '+errorThrown + '\r상태 : '+status);
    }).always(function(){
    	// file 초기화
    	var agent = navigator.userAgent.toLowerCase();
    	if ( (navigator.appName == 'Netscape' && navigator.userAgent.search('Trident') != -1) || (agent.indexOf("msie") != -1) ){
    	    $("#excelFile").replaceWith( $("#excelFile").clone(true) );
    	} else {
    	    $("#excelFile").val("");
    	}
    });
}
// 엑셀파일 완료 후 
function onUploadCompleteData(filePath){
	var headerDef = new  Array();
	filePath = replaceAllString(filePath,"\n","");
	
	headerDef.push("srcdir");
	headerDef.push("srcfile");
	headerDef.push("moddir");
	headerDef.push("modfile");
	
	var tmpData = {
		filePath : filePath,
		headerDef: headerDef,
		requestType: 'getArrayCollection'
	}
	
	$('[data-ax5grid="mapGrid"] [data-ax5grid-container="root"] [data-ax5grid-container="body"]').append(loading_div);
	$(".loding-div").show();
	ajaxAsync('/webPage/common/CommonCodeInfo', tmpData, 'json',successGetArrayCollection);
}

// 읽어온 엑셀 정보 그리드에 세팅
function successGetArrayCollection(data) {
	if (data != undefined && data != null) {
		if (data.indexOf('ERROR')>-1) {
			$(".loding-div").remove();
			dialog.alert(data);
			return;
		}
	}
	mapGridData = data;
	//mapGridData.splice(0,1);
	mapGrid.setData(mapGridData);
	
	getModListExcel();
}

// 세팅한 정보 유효성 검사
function getModListExcel() {
	var dataObj = {
		userid : userId,
		cm_syscd : getSelectedVal('cboSysCd').cm_syscd
	}
	var tmpData = {
		fileList 	: mapGridData,
		dataObj		: dataObj,
		requestType	: 'getModList_excel'
	}
	ajaxAsync('/webPage/ecmm/Cmm1600Servlet', tmpData, 'json',successGetModListExcel);
}

// 세팅한 정보 유효성 검사 완료
function successGetModListExcel(data) {
	$(".loding-div").remove();
	
	mapGridData = data;
	
	errSw = false;
	for(var i=0; i<mapGridData.length; i++) {
		if(mapGridData[i].errsw === '1') {
			dialog.alert('입력체크 중 오류가 발생한 항목이 있습니다. 확인하여 조치 후 다시 처리하시기 바랍니다.',  function() {});
			errSw = true;
			break;
		}
	}
	$('#chkOk').wCheck('check', false);
	$('#btnReq').prop('disabled', errSw);
	gridDataFilter();
}

//시스템 콤보 가져오기
function getSysInfoMap() {
	var data = new Object();
	data = {
		UserId 		: userId,
		SecuYn 		: 'Y',
		SelMsg 		: 'SEL',
		requestType	: 'getSysInfo'
	}
	ajaxAsync('/webPage/ecmd/Cmd0900Servlet', data, 'json',successGetSysInfoMap);
}

// 시스템 콤보 가져오기 완료
function successGetSysInfoMap(data) {
	cboSysCdData = data;
	
	cboSysCdData = cboSysCdData.filter(function(data) {
		if(data.cm_sysinfo.substr(0,1) === '1') return false;
		else return true;
	});
	$('[data-ax5select="cboSysCd"]').ax5select({
      options: injectCboDataToArr(cboSysCdData, 'cm_syscd' , 'cm_sysmsg')
	});
	
	if(cboSysCdData.length === 0 ) {
		$('#btnLoadExl').prop( "disabled", 	true);
	}
}