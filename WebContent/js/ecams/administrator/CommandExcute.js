/**
 * 커맨드수행 기능정의 화면
 *
 * <pre>
 * 	작성자	: 김정우
 * 	버전 		: 1.0
 *  수정일 	: 2019-08-19
 *
 */

var userName 			= window.top.userName;
var userid 				= window.top.userId;
var adminYN 			= window.top.adminYN;
var userDeptName		= window.top.userDeptName;
var userDeptCd 			= window.top.userDeptCd;
var strReqCD 			= window.top.reqCd;
var cmdGridData 		= null;
var mask = new ax5.ui.mask();

var cmdGrid				= new ax5.ui.grid();
var fileUploadModal		= new ax5.ui.modal();
var uploadCk = true;
var cboDbData			= [];
var cboGbnData          = [];
var tmpPath = '';

cmdGrid.setConfig({
    target: $('[data-ax5grid="cmdGrid"]'),
    sortable: true,
    multiSort: true,
    showRowSelector : false,
	multipleSelect : false,
    header: {
        align: "center",
        columnHeight: 30
    },
    body: {
        columnHeight: 25,
        onClick: function () {
        	this.self.clearSelect();
            this.self.select(this.dindex);
        },
        onDBLClick: function () {},
        trStyleClass: function () {
    	},
    	onDataChanged: function(){
    		this.self.repaint();
    	}
    },
    columns: [

    ]
});

$('input:radio[name=cmdRadioUsr]').wRadio({theme: 'circle-radial blue', selector: 'checkmark'});
$('input.checkbox-pie').wCheck({theme: 'square-inset blue', selector: 'checkmark', highlightLabel: true});

$(document).ready(function(){
	if (adminYN != true) {
		dialog.alert('블법적인 접근입니다. 정상적으로 로그인하시기 바랍니다.');
		return;
	} 
	
	getTmpPath();
	// 조회 버튼 클릭
	$('#btnQry').bind('click', function() {
		getRequest();
	});
	//구분 라디오 클릭
	// 프로그램 종류 콤보 체인지 이벤트
	$('#cboGbnCd').bind('change',function() {
		gbnSet();
	});
	//파일송수신 라디오 클릭
	$('[name="cmdRadioUsr"]').bind('click', function() {
		btnSet();
	});
	//엑셀저장
	$("#btnExcel").on('click', function() {
		if (cmdGridData != null && cmdGridData.length > 0) {
			cmdGrid.exportExcel("쿼리.xls");
		}
	})
	getCodeInfo();
});

function getTmpPath() {
	var tmpData = {
		pCode:	'99',
		requestType: 	'getTmpDir'
	}
	var ajaxReturnData = null;
	ajaxReturnData = ajaxCallWithJson('/webPage/common/SystemPathServlet', tmpData, 'json');
	if (ajaxReturnData.indexOf('ERROR')>-1 || ajaxReturnData == 'ERR' || ajaxReturnData.substr(0,5) == 'ERROR') {
		dialog.alert(ajaxReturnData.substr(5));
		return;
	}
	tmpPath = ajaxReturnData;
	if(typeof tmpPath === 'string') {
		tmpPath = tmpPath;
	} else {
		tmpPath = tmpPath[0].cm_path;
	}
}

function getCodeInfo() {
/*
	var codeInfos = getCodeInfoCommon([new CodeInfo('CMDGBN', 'SEL','N')] );
	cboGbnData = codeInfos.CMDGBN;
	codeInfos = null;

*/

	/*
		01	커맨드수행
		02	쿼리수행
		03	파일송수신
		04	URL호출
		05	암/복호화(AES)
		06	암호화(DES)
		07	암호화(SHA)
		08	Unicode
	*/
	var tmpObj = {};
	tmpObj.cm_codename = "커맨드수행";
	tmpObj.cm_micode = "01";
	cboGbnData.push(tmpObj);
	
	tmpObj = {};
	tmpObj.cm_codename = "쿼리수행";
	tmpObj.cm_micode = "02";
	cboGbnData.push(tmpObj);
	
	tmpObj = {};
	tmpObj.cm_codename = "파일송수신";
	tmpObj.cm_micode = "03";
	cboGbnData.push(tmpObj);
	
	tmpObj = {};
	tmpObj.cm_codename = "URL호출";
	tmpObj.cm_micode = "04";
	cboGbnData.push(tmpObj);
	
	tmpObj = {};
	tmpObj.cm_codename = "암/복호화(AES)";
	tmpObj.cm_micode = "05";
	cboGbnData.push(tmpObj);
	
	tmpObj = {};
	tmpObj.cm_codename = "암호화(DES)";
	tmpObj.cm_micode = "06";
	cboGbnData.push(tmpObj);
	
	tmpObj = {};
	tmpObj.cm_codename = "암호화(SHA)";
	tmpObj.cm_micode = "07";
	cboGbnData.push(tmpObj);
	
	tmpObj = {};
	tmpObj.cm_codename = "Unicode";
	tmpObj.cm_micode = "08";
	cboGbnData.push(tmpObj);
	
	
	
	$('[data-ax5select="cboGbnCd"]').ax5select({
        options: injectCboDataToArr(cboGbnData, 'cm_micode' , 'cm_codename')
	});
	if (cboGbnData != null && cboGbnData.length>0) {
		$('[data-ax5select="cboGbnCd"]').ax5select("setValue", '02', true);
		gbnSet();
	
	}
	//DB계정
	$('[data-ax5select="cboDbUsrSel"]').ax5select({
		options: [
			{value: "0", text: "선택하세요"},
			{value: "O", text: "형상관리(운영)"},
			{value: "D", text: "형상관리(개발)"},
			{value: "F", text: "FORTIFY"}
		]
	});
	$('[data-ax5select="cboDbUsrSel"]').ax5select('setValue','O',true);
}

function gbnSet(){

	var gbnVal 	= getSelectedVal('cboGbnCd').value;
	if(gbnVal == '01'){ //커맨드수행
		$('#lbtit').text('[ 수행 할 커맨드 입력 ]');
		$('#btnQry').text('커맨드 실행');
		$('[data-ax5grid="cmdGrid"]').parent(".az_board_basic").hide();
		$('#txtrst').show();
		$('#txtrst').val('');
		$("#lblap").text('AP계정');
		$("#lblweb").text('WEB계정');
		$('#rdoap').wRadio('disabled',true);
		$('#rdoweb').wRadio('check', true);
		$('#rdoweb').wRadio('disabled',false);
		$('#rdoweb').show();
		$('#rdo2').show();
		$('#cboDbUsrSel').hide();
		$('#DBINFO').hide();
		$('#btnExcel').hide();
		$('#chkViewDiv').css('display','inline-block');
		$('#txtChkView').text('결과파일보기');
	} else if(gbnVal == '02'){ //쿼리수행
		$('#lbtit').text('[ 조회 할 쿼리문 입력 ]');
		$('#btnQry').text('쿼리 실행');
		$('[data-ax5grid="cmdGrid"]').parent(".az_board_basic").show();
		$('#txtrst').hide();
		$('#rdo2').hide();
		$('#cboDbUsrSel').show();
		$('#DBINFO').show();
		$('#btnExcel').show();
		//$('#chkViewDiv').css('visibility','hidden');
		$('#chkViewDiv').css('display','none');
	} else if(gbnVal == '03'){ //파일송수신
		$('#lbtit').text('[ 송/수신 할 파일 입력 ]');
		$('#txtrst').show();
		$('#txtrst').val('');
		$('[data-ax5grid="cmdGrid"]').parent(".az_board_basic").hide();
		$('[name="cmdRadioUsr"]').show();
		$("#lblap").text('송신');
		$("#lblweb").text('수신');
		$('#rdoap').wRadio('disabled',false);
		$('#rdoweb').wRadio('check', true);
		$('#rdoweb').wRadio('disabled',false);
		$('#rdo2').show();
		$('#cboDbUsrSel').hide();
		$('#DBINFO').hide();
		$('#btnExcel').hide();
		//$('#chkViewDiv').css('visibility','hidden');
		$('#chkViewDiv').css('display','none');
		btnSet();
	} else if(gbnVal == '04'){ //URL호출
		$('#lbtit').text('[ 수행 할 URL 입력 ]');
		$('#btnQry').text('URL 실행');
		$('#txtrst').show();
		$('#txtrst').val('');
		$('[data-ax5grid="cmdGrid"]').parent(".az_board_basic").hide();
		$('[name="cmdRadioUsr"]').show();
		$("#lblap").text('AP계정');
		$("#lblweb").text('WEB계정');
		$('#rdoap').wRadio('disabled',false);
		$('#rdoweb').wRadio('check', true);
		$('#rdoweb').wRadio('disabled',false);
		$('#rdo2').show();
		$('#cboDbUsrSel').hide();
		$('#DBINFO').hide();
		$('#btnExcel').hide();
		//$('#chkViewDiv').css('visibility','visible');
		$('#chkViewDiv').css('display','inline-block');
		$('#txtChkView').text('URL Link');
	} else if(gbnVal == '05' || gbnVal == '06'){ //암/복호화(AES),암/복호화(DES)
		$('#lbtit').text('[ 암/복호화 할 문자 입력 ]');
		$('#btnQry').text('암/복호화');
		$('#txtrst').show();
		$('#txtrst').val('');
		$('[data-ax5grid="cmdGrid"]').parent(".az_board_basic").hide();
		$('[name="cmdRadioUsr"]').show();
		$("#lblap").text('암호화');
		$("#lblweb").text('복호화');
		$('#rdoap').wRadio('disabled',false);
		$('#rdoap').wRadio('check', true);
		$('#rdoweb').wRadio('disabled',false);
		$('#rdo2').show();
		$('#cboDbUsrSel').hide();
		$('#DBINFO').hide();
		$('#btnExcel').hide();
		//$('#chkViewDiv').css('visibility','visible');
		$('#chkViewDiv').css('display','inline-block');
		$('#txtChkView').text('결과확인');
	} else if(gbnVal == '07'){ //암호화(SHA)
		$('#lbtit').text('[ 암호화 할 문자 입력 ]');
		$('#btnQry').text('암호화');
		$('#txtrst').show();
		$('#txtrst').val('');
		$('[data-ax5grid="cmdGrid"]').parent(".az_board_basic").hide();
		$('[name="cmdRadioUsr"]').show();
		$('#lblap').text('암호화');
		$('#lblweb').text('복호화');
		$('#rdoap').wRadio('disabled',false);
		$('#rdoap').wRadio('check', true);
		$('#rdoweb').wRadio('disabled',true);
		$('#rdo2').show();
		$('#cboDbUsrSel').hide();
		$('#DBINFO').hide();
		$('#btnExcel').hide();
		//$('#chkViewDiv').css('visibility','visible');
		$('#chkViewDiv').css('display','inline-block');
		$('#txtChkView').text('결과확인');
	} else if(gbnVal == '08'){ //Unicode
		$('#lbtit').text('[ 변환 할 문자 입력 ]');
		$('#btnQry').text('변환실행');
		$('#txtrst').show();
		$('#txtrst').val('');
		$('[data-ax5grid="cmdGrid"]').parent(".az_board_basic").hide();
		$('[name="cmdRadioUsr"]').show();
		$('#lblap').text('한글->Unicode');
		$('#lblweb').text('Unicode->한글');
		$('#rdoap').wRadio('disabled',false);
		$('#rdoap').wRadio('check', true);
		$('#rdoweb').wRadio('disabled',false);
		$('#rdo2').show();
		$('#cboDbUsrSel').hide();
		$('#DBINFO').hide();
		$('#btnExcel').hide();
		//$('#chkViewDiv').css('visibility','visible');
		$('#chkViewDiv').css('display','inline-block');
		$('#txtChkView').text('결과확인');
	}
}
function btnSet(){

	var gbnCd = getSelectedVal('cboGbnCd').value;
	if (gbnCd == '03') {
		if($('#rdoap').is(':checked')) {
			$('#btnQry').text('파일선택');
			//$('#chkViewDiv').css('visibility','hidden');
			$('#chkViewDiv').css('display','none');
		} else{
			$('#btnQry').text('파일수신');
			//$('#chkViewDiv').css('visibility','visible');
			$('#chkViewDiv').css('display','inline-block');
			$('#txtChkView').text('수신파일 직접보기');
		}
	}
}

function getRequest(){
	var gbnCd = getSelectedVal('cboGbnCd').value;
	var cmdData = new Object();
	if (gbnCd == '01') {			//커맨드
		if($('#rdoap').is(':checked')){			//AP계정(agent 띄어져 있는 계정으로)
			execCmd('A');
		} else { 								//web계정 (web이 띄어져 있는 계정으로)
			execCmd('W');
		}
	} else if (gbnCd == '02') {	//쿼리
		execQry();
	} else if (gbnCd == '03') {	//파일 송수신
		if($('#rdoap').is(':checked')){
			execFile('S');	//송신
		} else {
			execFile('R');	//수신
		}
	} else if (gbnCd == '04') {	//URL 호출
		if($('#chkView').is(':checked')) {
			window.open($('#txtcmd').val(),"LINK","menubar=no,status=no,resizable=yes,width=1000,height=700,left=10,top=30");
		}else {
			if($('#rdoap').is(':checked')){			//AP계정(agent 띄어져 있는 계정으로)
				execCmd('UA');
			} else { 								//web계정 (web이 띄어져 있는 계정으로)
				execCmd('UW');
			}
		}
	} else if (gbnCd == '05') {	//암/복호화(AES)
		if($('#rdoap').is(':checked')){			//암호화
			execCmd('EA');
		} else { 								//복호화
			execCmd('DA');
		}
	} else if (gbnCd == '06') {	//암/복호화(DES)
		if($('#rdoap').is(':checked')){			//암호화
			execCmd('ED');
		} else { 								//복호화
			execCmd('DD');
		}
	} else if (gbnCd == '07') {	//암호화(SHA)
		if($('#rdoap').is(':checked')){			//암호화
			execCmd('ES');
		}
	} else if (gbnCd == '08') {	//Unicode
		if($('#rdoap').is(':checked')){			//한글-->Unicode
			execCmd('KU');
		} else { 								//Unicode-->한글
			execCmd('UK');
		}
	}
}


function execCmd(gbnCd){
	if(document.getElementById("txtcmd").value.trim()==""){
		dialog.alert('내용을 입력하여 주시기 바랍니다.');
		return;
	}
	var cmdData = new Object();
	var requestType = '';
	var gbnCdVal = getSelectedVal('cboGbnCd').value;
	if (gbnCdVal == '01') requestType = 'getExecCmd';           //커맨드수행
	else if (gbnCdVal == '04') requestType = 'getRemoteUrl';   	//URL
	else if (gbnCdVal == '05') requestType = 'getEncrypt';   	//AES
	else if (gbnCdVal == '06') requestType = 'getEncrypt';     	//DES
	else if (gbnCdVal == '07') requestType = 'getEncrypt';     	//SHA
	else if (gbnCdVal == '08') requestType = 'getUnicode';     	//Unicode
	else return;

	cmdData.txtcmd 	= document.getElementById("txtcmd").value.trim();
	cmdData.userid  = userid;
	cmdData.gbnCd  	= gbnCd;
	cmdData.savePath= tmpPath;
	if($('#chkView').is(':checked')) {
		cmdData.view	= 'ok';
	} else {
		cmdData.view	= 'no';
	}
	var data =  new Object();
	data = {
		requestType : requestType,
		cmdData: cmdData
	}
	ajaxAsync('/webPage/ecmm/Cmm1600Servlet', data, 'json',successGetCmdRst);
}
function successGetCmdRst(data) {
	var gbnCd = getSelectedVal('cboGbnCd').value;
	if (gbnCd == '01') {
		if(data === '0'){	//성공
			var filePath = '';
			if($('#chkView').is(':checked')) {
				if($('#rdoap').is(':checked')){
					filePath = tmpPath + '/' + userid + 'apcmd.out';
				} else {
					filePath = tmpPath + '/' + userid + 'webcmd.out';
				}
				fileView(filePath);
			}
			dialog.alert('커맨드 수행이 정상적으로 처리되었습니다.');
		} else {	//실패
			dialog.alert(data);
		}
	} else if (gbnCd == '04'){  //URL
		if(data.substring(0,2) == 'ER'){
			dialog.alert("실패 하였습니다."+data);
		} else if(!$('#chkView').is(':checked')) {	//URL Link가 아니면
			fileView(data.trim());
		}
	} else {
		if(data.substring(0,2) == 'ER'){
			dialog.alert("실패 하였습니다."+data);
		} else {	//직접보기
			$('#txtrst').val(data.trim());
		}
	}
}

function execQry(){
	if (getSelectedIndex('cboDbUsrSel') < 1) {
		dialog.alert('DB정보를 선택하여 주십시오.',function(){});
		return;
	}
	
	if(document.getElementById("txtcmd").value.trim()==""){
		dialog.alert('쿼리문을 입력하여 주시기 바랍니다.');
		return;
	}

	var cmdData = new Object();
	var txtqry = document.getElementById("txtcmd").value.trim();

	if(txtqry.charAt(txtqry.length-1) == ';'){
		txtqry = txtqry.substr(0, txtqry.length-1);
	}

	txtqry.substr(0,txtqry)
	cmdData.txtcmd 	= txtqry;
	cmdData.dbGbnCd = getSelectedVal('cboDbUsrSel').value;
	var data =  new Object();
	data = {
			requestType : 'getExecQry',
			cmdData: cmdData
	}
	ajaxAsync('/webPage/ecmm/Cmm1600Servlet', data, 'json',successGetQryRst);
}

function successGetQryRst(data) {
	// 그리드 초기화
	gridClear();
	cmdGridData = data;
	var i = 0;
	var coladdcnt = 0;
	var colremovecnt = 0;

	if (cmdGridData.length<2){
		if (cmdGridData.length === 1){
			if(cmdGridData[0].ERROR == 'Y'){
				dialog.alert(cmdGridData[0].ERRMSG);
				return;
			}
			if(cmdGridData[0].readsw == 'N'){
				dialog.alert(cmdGridData[0].rowcnt + "건이 정상적으로 처리되었습니다. 조회쿼리로 확인하여 주시기 바랍니다.");
				return;
			}
		}
		dialog.alert("쿼리결과가 없습니다.");
		return;
	}
	//컬럼 추가
	coladdcnt = cmdGridData[0].colcount;
	for(i=0;coladdcnt>i;i++) {
		cmdGrid.addColumn({key: "col"+i,	label: eval("cmdGridData[0].col"+i),	width: '10%', editor:{type:"text"}});
	}
	//첫번째 로우 삭제 - 컬렴명
	cmdGridData.splice(0,1);

	//그리드 columnsort 초기화 이 라인이 없을시 order by 안먹힘
	cmdGrid.setColumnSort({});
	cmdGrid.setData([]);

	cmdGrid.setData(cmdGridData);
}

function execFile(gbnCd){
	if (gbnCd=='S'){	//송신
		setTimeout(function() {
			fileUploadModal.open({
		        width: 685,
		        height: 420,
		        iframe: {
		            method: "get",
		            url: "../modal/fileupload/CmdFileUpload.jsp"
			    },
		        onStateChanged: function () {
		            if (this.state === "open") {
		                mask.open();
		            }
		            else if (this.state === "close") {
		                mask.close();
		            }
		        }
			});
		}, 200);
	} else {	//수신
		if($('#chkView').is(':checked')) {	//직접보기
			fileView(document.getElementById("txtcmd").value.trim());
		} else {	// 로컬다운
			//로컬로 파일 다운
			if(document.getElementById("txtcmd").value.trim()==""){
				dialog.alert('수신 할 파일에 대한 디렉토리와 파일명(FullPath)을 입력하여 주시기 바랍니다.');
				return;
			}
			var fullpath=document.getElementById("txtcmd").value.trim();
			fileDown(fullpath, fullpath.substr(fullpath.lastIndexOf('/')+1));
//			location.href = homePath + '/ecamsap/webPage/fileupload/upload?&fullPath='+fullpath+'&fileName='+encodeURIComponent(fullpath.substr(fullpath.lastIndexOf('/')+1));
		}
	}
}
function fileView(txtcmd){
	var data =  new Object();
	data = {
		saveFile: txtcmd,
		requestType : 'getFileView'
	}
	ajaxAsync('/webPage/ecmm/Cmm1600Servlet', data, 'json',successgetFileView);
}
function successgetFileView(data) {
	if (data.length < 2){
		dialog.alert('파일 수신 중 오류가 발생하였습니다.');
		return;
	}
	if (data.substr(0,2)==='ER'){
		dialog.alert(data.substr(2));
		return;
	} else {
		$('#txtrst').val(data.substr(2));
	}
}

function gridClear(){
	var gridConfig = cmdGrid.config;
	cmdGrid.destroy();
	gridConfig.columns=[];
	cmdGrid.setConfig(gridConfig);
}
