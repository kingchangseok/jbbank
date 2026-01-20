/**
 * 개발계획/완료 탭 기능 정의
 * <pre>
 * <b>History</b>
 * 	작성자: 허정규
 * 	버전 : 1.0
 *  수정일 : 2020-04-23
 */

var userName 	 	= window.parent.userName;
var userId 		 	= window.top.userId;
var adminYN 		= window.parent.adminYN;
var userDeptName 	= window.parent.userDeptName;
var userDeptCd 	 	= window.parent.userDeptCd;
var strReqCd	 	= window.parent.strReqCd; 
var codeList        = window.top.codeList;      //전체코드리스트

var grdDoc 			= new ax5.ui.grid();

var picker = new ax5.ui.picker();

var strReqCD = "";
var SrID = "";
var SrTitle = "";
var SRStatus = "";
var GradeCd = "";
var AcptNo = "";
var Endmsg = "";
var notiPath = "";
var Cnchk = window.top.Cnchk;
var permitStr = "";

var upFiles = [];
var gridDoc1ContextMenu  = null;

ax5.info.weekNames = [
    {label: "일"},
    {label: "월"},
    {label: "화"},
    {label: "수"},
    {label: "목"},
    {label: "금"},
    {label: "토"}
];

$(document).ready(function(){

	$("#cmdResultTest").bind("click",function(){
		$("#doc").click();
	});
	
	$("#doc").bind("change",function(){
		if($("#doc").get(0).files && $("#doc").get(0).files[0]){
			var fileObj = new Object();
			fileObj = {
				srid : SrID,
				filegb : "21",
				editor : userId,
				testergbn : ""
			}
			window.parent.docList = grdDoc.getList();
			window.parent.upFiles = $("#doc").get(0); 
			window.parent.fileObj = fileObj; 
			window.parent.fileupload();
		}
	});
	
	$("cmdReq").bind("click",function(){
		cmdReq_click();
	});
	
	$("cmdCncl").bind("click",function(){
		cmdCncl_click();
	});
	
	$("#cmdDoc").bind("click",function(){
		fileOpen();
	});
});

function createViewGrid() {
//	if(!createGrid) {
		grdDoc.setConfig({
		    target: $('[data-ax5grid="grdDoc"]'),
		    sortable: true, 
		    multiSort: true,
		    showRowSelector: false,
		    multipleSelect: false,
		    paging: false,
		    page : {
		    	display:false
		    },
		    header: {
		        align: "center"
		    },
		    body: {
		        onClick: function () {
		        	this.self.clearSelect();
		            this.self.select(this.dindex);
		        },
		        onDBLClick: function () {
		        	this.self.clearSelect();
		        	this.self.select(this.dindex);
		        	downSelectDoc(this.dindex, this.item);
		        },
		    	onDataChanged: function(){
		    		this.self.repaint();
		    	}
		    },
		    contextMenu: {
		        iconWidth: 20,
		        acceleratorWidth: 100,
		        itemClickAndClose: false,
		        icons: {
		            'arrow': '<i class="fa fa-caret-right"></i>'
		        },
		        items: [
		            {type: 1, label: "제거"}
		        ],
		        popupFilter: function (item, param) {
		        	if(isNaN(param.dindex)){
			        	grdDoc.contextMenu.close();
		        		return false;
		        	}
		         	return true;
		       	 
		        },
		        onClick: function (item, param) {
		        	if(param.item.newFile == true){
		        		grdDoc.removeRow(param.item.dindex);
		        		dialog.alert("제거되었습니다.");
		        		
		        	} else {
		        		delSelectDoc(SrID, "03", param.item.cc_seqno);
		        	}
		        	grdDoc.contextMenu.close();//또는 return true;
		        }
		    },
		    columns: [
		        {key: "name", label: "파일명",	width: '100%', 	align: "center"}
		    ]
		});

	createGrid = true;
}

function initFrame(){
	//SystemPath.getTmpDir("30");
	getTmpDir();

	getUserRGTCD();
}

function setSRInfo_detail_func() {
	if(SrID == "" || SrID == null) return;
	 initFrame();

	if(SrID != "" && SrID != null){
		//getSRInfo();
	}
		
	$("#txtsrid").val(SrID);
	$("#txttitle").val(SrTitle);
	$("#txtcomment").val(Endmsg);
	
	if(strReqCD == "31"){
		$("#txtcomment").prop("disabled",false);
		$("#cmdCncl").prop("disabled",false);
		$("#cmdReq").prop("disabled",false);
		$("#cmdDoc").prop("disabled",false);
	}else{
		if(GradeCd != "" && GradeCd != null && GradeCd == "04"){
			$("#txtcomment").prop("disabled",false);
			$("#cmdCncl").prop("disabled",false);
			$("#cmdReq").prop("disabled",false);
			$("#cmdDoc").prop("disabled",false);
		}
		$("#txtcomment").prop("disabled",true);
		$("#cmdCncl").prop("disabled",true);
		$("#cmdReq").prop("disabled",true);
		return;
	}

	if(Endmsg == "" || Endmsg == null){
		$("#cmdReq").text("등록");
	}else{
		$("#cmdReq").text("수정");
	}

	grdDoc.setData([]);
	getDocList("03");
	gridDoc1ContextMenu = grdDoc1.config.contextMenu;
	if(strReqCd != "31"){
		grdDoc.config.contextMenu = null;
	}
	
}

//엑셀 파일 업로드시 파일 올릴 경로 가져오기
function getTmpDir() {
	var data = new Object();
	data = {
		pcode 		: '30',
		requestType	: 'getSystemPath'
	}
	ajaxAsync('/webPage/common/CommonSystemPath', data, 'json',successGetTmpDir);
}

function successGetTmpDir(data) {
	notiPath = data;
	if(typeof(tmpPath) == "object"){
		notiPath = data[0].cm_path;
	}
}

function getUserRGTCD() {
	var data = new Object();
	data = {
		userId 		: userId,
		rgtCd			: "13",
		closeYn		: "N",
		requestType	: 'getUserRGTCD'
	}
	ajaxAsync('/webPage/sr/SRStatus', data, 'json',successGetUserRGTCD);
}

function successGetUserRGTCD(data) {
	permitStr = data;
	if(strReqCD == "31"){
		if(permitStr.length==0) dialog.alert("SR일반문의에 대한 답변권한이 없습니다.");
	}

	if(strReqCD == "31" && SRStatus == "01" && permitStr.length>0){
		$("#txtcomment").prop("disabled",false);
		$("#cmdDoc").prop("disabled",false);
		$("#cmdReq").prop("disabled",false);
		if(Cnchk == "Y"){
			$("#cmdCncl").prop("disabled",false);
		}else {
			$("#cmdCncl").prop("disabled",true);
		}
	}
}
function getDocList(gbnCd){

	ajaxReturnData = null;
	var docSr = {
		srid : SrID,
		gbn : gbnCd,
		requestType : 'getDocList'
	}

	ajaxReturnData = ajaxCallWithJson('/webPage/srcommon/SRRegister',
			docSr, 'json');
	if (ajaxReturnData !== 'ERR') {
		grdDoc.setData([]);
	
		grdDoc.setData(ajaxReturnData);
	}
	
}

function getSRInfo(){
	var ajaxReturnData = null;
	var prjData = new Object();
	prjData.deptCd = "ALL";
	prjData.gbnCd = "ALL";
	prjData.strDate = "";
	prjData.endDate = "";
	prjData.selCd = "SEL";
	prjData.selTxt = "";
	prjData.srId = SrID;
	
	prjData.userId 	= userId;
	prjData.reqCd 	= strReqCD;
	
	var prjInfo = new Object();
	prjInfo.prjInfoData =	prjData;
	prjInfo.requestType = 'getSRInfo';
	
	ajaxReturnData = ajaxCallWithJson('/webPage/common/ComPrjInfo', prjInfo, 'json');

	if(ajaxReturnData.length>0){
		SrTitle = ajaxReturnData[0].cc_title;
		SRStatus = ajaxReturnData[0].cc_status;
		Endmsg = ajaxReturnData[0].cc_procmsg;
		setSRInfo_detail_func();
	}
}

function delSelectDoc(srId, fileGb, seq) {
	var ajaxReturnData = null;
	if(seq == null || seq == undefined)  return;
	
	var tmpObj = new Object();
	tmpObj = {
			srid : SrID,
			editor : userId,
			filegb : fileGb,
			seq : seq
	}
	var data = new Object();
	data = {
		tmpObj : tmpObj,
		requestType	: 'delSelectDoc'
	}
	ajaxReturnData = ajaxCallWithJson('/webPage/sr/SRStatus', data, 'json');
	
	if(ajaxReturnData == "ok") {
		dialog.alert("제거되었습니다.");
		var grdDocList = grdDoc.getList();
		$(grdDocList).each(function(i){
			if(this.cc_seqno == seq){
				grdDocList.splice(i,1);
				grdDoc.setData(grdDocList);
				return false;
			}
		});
	}else {
		dialog.alert("제거에 실패하였습니다.");
	}
}

function downSelectDoc(index,item){
	var fullPath = notiPath+'/'+item.cc_srid+'/'+item.cc_reldoc;
	var fileName = item.name;
	fileDown(fullPath, fileName);
}

function cmdReq_click(){
	if(SrID == "" || SrID == null || window.parent.getPrjSelectIndex() < 0){
		dialog.alert($("#cmdReq").text()+"할 SR을 선택하십시오.");
		return;
	}else if($("#txtcomment").val().length == 0){
		dialog.alert("작업내용을 입력하십시오.");
		return;
	}else if($("#txtcomment").val().length >1999){
		dialog.alert("작업내용을 2000자 이내로 입력하십시오.");
		return;
	}
	var tmpObj = {};
	tmpObj.UserId = userId;
	tmpObj.Srid = SrID;
	tmpObj.txtcomment = $("#txtcomment").val();
	
	var docSr = {
			tmpObj : tmpObj,
			requestType : 'updateEndMsg'
		}

		ajaxReturnData = ajaxCallWithJson('/webPage/sr/SRStatus',
				docSr, 'json');
		if(ajaxReturnData == "OK"){
			if (upFiles.length > 0){
				var newFileList = [];
				var oldFileList = [];
				$(grdDoc.getList()).each(function(){
					if(this.newFile == true){
						newFileList.push(this);
					} else {
						oldFileList.push(this);
					}
				});
				
				if(newFileList.length > 0){
					
					var fileObj = new Object();
					fileObj = {
						srid : SrID,
						filegb : "03",
						editor : userId,
						testergbn : ""
					}
					window.parent.docList = oldFileList;
					window.parent.upFiles = newFileList; 
					window.parent.fileObj = fileObj; 
					window.parent.fileupload();
				}
			}else{
				dialog.alert("작업등록을 완료했습니다.");
				this.parentDocument.refreshGrd();
			}
		}else{
			dialog.alert(ajaxReturnData);
		}
}

function fileClear(){
	$("#cmdCncl").prop("disabled",true);
	$("#cmdReq").prop("disabled",true);
	
	grdDoc.config.contextMenu = null;
	
	dialog.alert("작업등록을 완료했습니다.");
	window.parent.getPrjList();
}

//파일첨부
function fileOpen() {
	if($("#file"+fileIndex).val() != "" && $("#file"+fileIndex).val() != "null"){
		fileIndex++;
		$('#fileSave').append('<input type="file" id="file'+fileIndex+'" name="file'+fileIndex+'" onchange="fileChange(\'file'+fileIndex+'\')" accept-charset="UTF-8" multiple="multiple" />');
	}
	$("#file"+fileIndex).click();
	
}

function fileChange(file){
	var fileData = grdDoc.getList();
	var jqueryFiles = $("#"+file).get(0);
	var fileSizeArr = [' KB', ' MB', ' GB'];
	var spcChar = "{}<>?|~`!@#$%^&*+\"'\\/";
	
	if (jqueryFiles.files && jqueryFiles.files[0]) { 
		var fileCk = true;
		
		for(var i=0; i<jqueryFiles.files.length; i++){
			var sizeCount = 0;
			var size = jqueryFiles.files[i].size/1024; // Byte, KB, MB, GB
			while(size > 1024 && sizeCount < 2){
				size = size/1024;
				sizeCount ++;
			}
			size = Math.round(size*10)/10.0 + fileSizeArr[sizeCount];
			var sizeReal = jqueryFiles.files[i].size;
			var name = jqueryFiles.files[i].name
			if(jqueryFiles.files[i].size > 20*1024*1024){ // 20MB 제한
				dialog.alert('<div>파일명 : '+name+'</div> <div>파일은 20MB를 넘어 업로드 할 수 없습니다.</div>',function(){});
				fileCk = false;
				continue;
			}
			TotalFileSize = TotalFileSize + sizeReal;
			if(TotalFileSize > 1 *1024*1024*1024){ // 총파일 사이즈 1GB 제한
				dialog.alert('첨부파일의 총 용량은 1GB 를 초과할 수 없습니다.',function(){});
				TotalFileSize = TotalFileSize - sizeReal;
				fileCk = false;
				break;
			}
			
			for(var j=0; j<fileData.length; j++){

				for (k=0;spcChar.length>k;k++) {
					if (name.indexOf(spcChar.substr(k,1))>=0) {
						dialog.alert("첨부파일명에 특수문자를 입력하실 수 없습니다. 특수문자를 제외하고 첨부하여 주시기 바랍니다.");
						$("#"+file).remove();
						fileCk = false;
						break;
					}
				}
				if(!fileCk){
					break;
				}
				
				if(fileData[j].name == name){
					dialog.alert("파일명 : " + name +"\n 은 이미 등록된 파일입니다.\n확인 후 다시 등록해 주세요");
					$("#"+file).remove();
					fileCk = false;
					break;
				}
				fileCk = true;
			}

			if(fileCk){
				var tmpObj = new Object(); // 그리드에 추가할 파일 속성
				tmpObj.name = name;
				tmpObj.size = size;
				tmpObj.sizeReal = sizeReal;
				tmpObj.newFile = true;
				tmpObj.realName = name;
				tmpObj.file = jqueryFiles.files[i];
				
				fileData.push(tmpObj);
			}
			else{
				break;
			}
		}
		grdDoc.setData(fileData);

		if(fileCk){
			//dialog.alert("파일첨부 시 등록/수정 버튼을 클릭해야 파일이 저장됩니다.");
		}

	} 
	
}

function cmdCncl_click(){
	if(SrID == "" || SrID == null || window.parent.getPrjSelectIndex() < 0){
		dialog.alert($("#cmdCncl").text()+"할 SR을 선택하십시오.");
		return;
	}
	
	if(!$("#cmdCncl").prop("disabled")){

		var data = {
				srId	: SrID,
				UserId	: userId,
				Status	: "03",
				requestType	: 'setSrCncl'
		}
		ajaxReturnData = ajaxCallWithJson('/webPage/sr/SRStatus', data, 'json');
		
		if(ajaxReturnData == "OK"){
			dialog.alert("SR-ID("+SrID+") 취소했습니다.");
		}else{
			dialog.alert("SR-ID("+SrID+") 취소에 실패하였습니다.");
		}
		window.parent.getPrjList();
	}
}
