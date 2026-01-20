/**
 * [사용자정보 > 사용자일괄등록] 화면 기능정의
 * 
 * <pre>
 * 	작성자	: 이용문
 * 	버전 		: 1.0
 *  수정일 	: 2019-07-31
 * 
 */


var userId 		= $('#userId').val();			// 접속자 ID

var signUpGrid	= new ax5.ui.grid();
var signUpGridData 	= null;
var fSignUpGridData 	= null;

var tmpPath			= '';
var uploadJspFile 	= '';
var errSw			= false;

signUpGrid.setConfig({
    target: $('[data-ax5grid="signUpGrid"]'),
    sortable: true, 
    multiSort: true,
    showRowSelector: false,
    header: {
        align: "center",
    },
    body: {
        onClick: function () {
        	this.self.clearSelect();
            this.self.select(this.dindex);
        },
        onDBLClick: function () {},
    	trStyleClass: function () {
    		if(this.item.colorsw === '3'){//엑셀값이 오류가 있는경우
    			return "fontStyle-cncl";
    		}
    		if(this.item.colorsw === '0'){//정상 등록 완료
    			return "";
    		}
    		if(this.item.colorsw === '4'){//43 테이블 등록 실패
    			return "fontStyle-43err";
    		}
    		if(this.item.colorsw === '5'){//44 테이블 등록 실패
    			return "fontStyle-44err";
    		}
    		if(this.item.colorsw === '6'){//43 44 테이블 등록 실패
    			return "fontStyle-4344err";
    		}
    		if(this.item.colorsw === '7'){//동기화 제외 대상
    			return "fontStyle-async";
    		}
    	},
    	onDataChanged: function(){
    		this.self.repaint();
    	}
    },
    columns: [
        {key: "CM_USERID", 		label: "*사용자ID",  	width: '8%',  editor: {type: "text"}},
        {key: "CM_USERNAME",	label: "*이름",  		width: '7%',  editor: {type: "text"}},
//        {key: "CM_UPPERPROJECT", label: "*상위조직명",  	width: '9%',  editor: {type: "text"}},
        {key: "CM_PROJECT", 	label: "*조직명",  	width: '9%',  editor: {type: "text"}},
        {key: "CM_POSITION", 	label: "*직급명",  	width: '7%',  editor: {type: "text"}},
        {key: "CM_CODENAME",     	label: "*직무명",  	width: '9%',  editor: {type: "text"}},
        {key: "CM_SYSCD", 		label: "운영서버",  	width: '15%',  editor: {type: "text"}},
        {key: "CM_JOBNAME", 	label: "업무코드",  	width: '10%',  editor: {type: "text"}},
        {key: "CM_EMAIL", 		label: "이메일주소",  	width: '10%',  editor: {type: "text"}},
        {key: "CM_TELNO1", 		label: "전화번호",  	width: '8%',  editor: {type: "text"}},
        {key: "CM_TELNO2", 		label: "핸드폰번호",  	width: '8%',  editor: {type: "text"}},
    ]
});

$('[data-grid-control]').click(function () {
	switch (this.getAttribute("data-grid-control")) {
    case "row-add":
    	signUpGrid.addRow($.extend({}, {newRow : true}, {__index: undefined}) ,"last",{focus: "END"});
    	signUpGrid.clearSelect();
    	signUpGrid.select(signUpGrid.getList().length-1, {selected: true});
    	$('#btnExcel').prop('disabled', false);
    	$('#btnDbSave').prop('disabled', false);
        break;
	}
});

$('input:radio[name^="radio"]').wRadio({theme: 'circle-radial blue', selector: 'checkmark'});
$('input:radio[name^="radio"]').wRadio("disabled", true);

$(document).ready(function() {
	getTmpDir();
	
	$('#btnExcel').prop('disabled', true);
	$('#btnDbSave').prop('disabled', true);
	
	$('input[name="radio"]').bind('click', function() {
		clickRadio();
	});
	
	// 엑셀열기 클릭
	$('#btnExcelOpen').bind('click', function() {
		$('#excelFile').trigger('click');
	});
	// 엑셀 파일 선택
	$('#excelFile').bind('change', function() {
		fileTypeCheck(this);
	})
	// 셀추가 클릭
	//$('#btnCellAd').bind('click', function() {
		
	//});
	// 엑셀저장 클릭
	$('#btnExcel').bind('click', function() {
		signUpGrid.exportExcel('사용자일괄등록.xls');
	});
	// 디비저장 클릭
	$('#btnDbSave').bind('click', function() {
		saveDb();
	});
});

// 전체/정상/장애 라디오 클릭
function clickRadio() {
	var selVal = $(":input:radio[name=radio]:checked").val();
	
	fSignUpGridData = [];
	if(selVal === 'all') {
		fSignUpGridData = signUpGridData;
	}
	if(selVal === 'normal') {
		signUpGridData.forEach(function(item, inex) {
			if(item.colorsw === '0') {
				fSignUpGridData.push(item);
			}
		});
	}
	if(selVal === 'error') {
		signUpGridData.forEach(function(item, inex) {
			if(item.colorsw !== '0') {
				fSignUpGridData.push(item);
			}
		});
	}
	signUpGrid.setData(fSignUpGridData);
}

// 유저목록 디비 저장
function saveDb() {
	signUpGridData = signUpGrid.getList();
	
	for(var i = 0; i < signUpGridData.length; i++) {
		var data = signUpGridData[i];
			
		if(data.CM_USERID === null || data.CM_USERID === ''){
			dialog.alert('ID를 입력하세요.', function() {});
			return;
		}
		if(data.CM_USERNAME === null || data.CM_USERNAME === ''){
			dialog.alert('이름을 입력하세요.', function() {});
			return;
		}
		/*if(data.CM_UPPERPROJECT === null || data.CM_UPPERPROJECT === ''){
			dialog.alert('상위조직명을 입력하세요.', function() {});
			return;
		}*/
		if(data.CM_PROJECT === null || data.CM_PROJECT === ''){
			dialog.alert('조직명을 입력하세요.', function() {});
			return;
		}
		if(data.CM_POSITION === null || data.CM_POSITION === ''){
			dialog.alert('직급명을 입력하세요.', function() {});
			return;
		}
		if(data.CM_CODENAME === null || data.CM_CODENAME === ''){
			dialog.alert('직무를 입력하세요.', function() {});
			return;
		}
		/*
		if(data.CM_TELNO2 === null || data.CM_TELNO2 === ''){
			dialog.alert('전화번호를 입력하세요.', function() {});
			return;
		}
		*/
	}
	
	var data = new Object();
	data = {
		rtList 		: signUpGrid.getList(),
		requestType	: 'all_sign_up'
	}
	ajaxAsync('/webPage/ecmm/Cmm0403Servlet', data, 'json',successSaveDb);
}

// 유저목록 디비 저장 완료
function successSaveDb(data) {
	console.log(data);
	
	dialog.alert('처리되었습니다.', function() {
		signUpGridData = data;
		signUpGrid.setData(signUpGridData);
		$('#optAll').wRadio('check', true);
	});
	
}

//엑셀 파일 업로드시 파일타입 체크
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

//엑셀 파일을 임시 경로에 업로드
function startUpload() {
	var excelFileSub = $('#excelFile')[0].files[0];
	var excelFile = null;
	
	// 파일 업로드 jsp 를 호출해야함
	var formData = new FormData();
	console.log(uploadJspFile);
	// 테스트 임시경로
//	tmpPath = 'D:\\fileupload\\tmp\\';
	formData.append('fullName', tmpPath  + userId + "_signUpExcel.tmp");
	formData.append('fullpath', tmpPath);
	formData.append('file',excelFileSub);
    $.ajax({
        url:'/webPage/fileupload/'+uploadJspFile,
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
	console.log('test');
	var headerDef = new  Array();
	filePath = replaceAllString(filePath,"\n","");
	
	headerDef.push("CM_USERID");
	headerDef.push("CM_USERNAME");
//	headerDef.push("CM_UPPERPROJECT");
	headerDef.push("CM_PROJECT");
	headerDef.push("CM_POSITION");
	headerDef.push("CM_CODENAME");
	headerDef.push("CM_SYSCD");
	headerDef.push("CM_JOBNAME");
	headerDef.push("CM_EMAIL");
	headerDef.push("CM_TELNO1");
	headerDef.push("CM_TELNO2");
	
	var tmpData = {
		filePath : filePath,
		headerDef: headerDef,
		requestType: 'getArrayCollection'
	}
	ajaxAsync('/webPage/common/CommonCodeInfo', tmpData, 'json',successGetArrayCollection);
}

// 읽어온 엑셀 정보 그리드에 세팅
function successGetArrayCollection(data) {
	signUpGridData = data;
	signUpGridData.splice(0,1);
	signUpGrid.setData(signUpGridData);
	
	$('#optAll').wRadio('check', false);
	$('#optNomal').wRadio('check', false);
	$('#optError').wRadio('check', false);
	
	
	if( signUpGridData.length > 0 ) {
		$('#btnExcel').prop('disabled', false);
		$('#btnDbSave').prop('disabled', false);
	}
}

//엑셀 파일 업로드시 파일 올릴 경로 가져오기
function getTmpDir() {
	var data = new Object();
	data = {
		pCode 		: '99',
		requestType	: 'getTmpDir'
	}
	ajaxAsync('/webPage/common/SystemPathServlet', data, 'json',successGetTmpDir);
}
//엑셀 파일 업로드시 파일 올릴 경로 가져오기 완료 AND 업로드할때 사용할 JSP 파일명 가져오기
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
//업로드할때 사용할 JSP 파일명 가져오기 완료
function successGetUploadJspName(data) {
	uploadJspFile = data;
	if(typeof(uploadJspFile) == "object"){
		uploadJspFile = data[0].cm_path;
	}
	
}
