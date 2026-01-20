/**
 * 실행모듈정보 화면 기능정의
 * 
 * <pre>
 * 	작성자	: 이용문
 * 	버전 		: 1.0
 *  수정일 	: 2019-06-28
 * 
 */
var modalObj = window.parent.modalObj;

var strUserId 	= "";
var strSysCd 	= "";
var strItemId	= "";

var prgGrid		= new ax5.ui.grid();
var modGrid		= new ax5.ui.grid();

var prgGridData 	= [];
var fPrgGridData 	= [];

prgGrid.setConfig({
    target: $('[data-ax5grid="prgGrid"]'),
    sortable: true, 
    multiSort: true,
    multipleSelect: true,
    showRowSelector: true,
    header: {
        align: "center",
    },
    body: {
        onClick: function () {
            this.self.select(this.dindex);
        },
        onDBLClick: function () {
        	dblClickDirGrid(this.dindex);
        },
        trStyleClass: function () {
    		if(this.item.status === 'ER'){
    			return "text-danger";
    		}
    	},
    	onDataChanged: function(){
    		this.self.repaint();
    	}
    },
    columns: [
        {key: "cr_rsrcname", 	label: "모듈명",  		width: '35%', align: "left"},
        {key: "jawon",			label: "프로그램종류",  	width: '20%', align: "left"},
        {key: "cm_dirpath", 	label: "디렉토리",  		width: '35%', align: "left"},
    ]
});

$(document).ready(function() {
	strUserId 	= modalObj.userid;
	strSysCd 	= modalObj.syscd;
	strItemId	= modalObj.itemid;
	
	$("#txtSysCd").val(modalObj.sysmsg);
	$("#txtPrg").val(modalObj.rsrcname);
	$("#txtDir").val(modalObj.path);
	
	getModList();
	
	// 등록
	$('#btnReq').bind('click', function() {
		updtRelat();
	});
	// 닫기
	$('#btnClose').bind('click', function() {
		popClose();
	});
	
});

// 등록
function updtRelat() {
	var selInPrg = prgGrid.selectedDataIndexs;
	var progList = [];
	
	if(selInPrg.length === 0 ){
		dialog.alert('연결할 모듈을 선택하여 주시기 바랍니다.', function() {});
		return;
	}
	
	selInPrg.forEach(function(selInP, index) {
		var prgItem = prgGrid.list[selInP];
		var tmpItem = new Object();
		tmpItem.cr_itemid = strItemId;
		tmpItem.cr_prcitem = prgItem.cr_itemid;
		tmpItem.check = 'true';
		progList.push(tmpItem);
		tmpItem = null;
		prgItem = null;
	});
	
	var data = {
		UserId : strUserId,
		SysCd : strSysCd,
		progList : progList,			
		requestType	: 'updtRelat'
	}
	
	ajaxAsync('/webPage/ecmd/Cmd0900Servlet', data, 'json',successUpdtRelat);
}

// 등록 완료
function successUpdtRelat(data) {
	if(data === '0' ) {
		dialog.alert('프로그램과 실행모듈연결정보 등록처리를 완료하였습니다.', function(){
			popClose();
		});
	} else {
		dialog.alert('프로그램과 실행모듈연결정보 등록처리를 실패하였습니다.', function(){
			popClose();
		});
	}
}

// 프로그램 목록 가져오기
function getModList() {
	prgGridData	= [];
	prgGrid.setData([]);	
	fPrgGridData = [];
	
	var data = {
		UserId : strUserId,
		SysCd : strSysCd,
		ProgId : "",
		requestType	: 'getModList'
	}
	
	ajaxAsync('/webPage/ecmd/Cmd0900Servlet', data, 'json',successGetProgList);
}

// 프로그램 목록 가져오기 완료
function successGetProgList(data) {
	prgGridData = data;
	getRelatList();
}

//프로그램 목록 가져오기
function getRelatList() {
	fPrgGridData 	= [];
	var data = {
		UserId : strUserId,
		SysCd : strSysCd,
		GbnCd : "I",
		ProgId : strItemId,
		requestType	: 'getRelatList'
	}
	
	ajaxAsync('/webPage/ecmd/Cmd0900Servlet', data, 'json',successGetRelatList);
}

function successGetRelatList(data){
	fPrgGridData = data;
	var proGridList = prgGridData;
	var count =0;
	
	for(var i=proGridList.length-1; i>0; i--){
		var selectedData = null;
		for(var j=0; j<fPrgGridData.length; j++) {
			if(proGridList[i].cr_itemid == fPrgGridData[j].cd_prcitem){
				selectedData = proGridList[i];
				selectedData.__selected__ = true;
				proGridList.splice(i,1);
				proGridList.splice(0,0,selectedData);
				if(i != proGridList.length-1){
					i++;
				}
				continue;
			}
		}
		count++;
		if(count >= proGridList.length){
			break;
		}
	}
	prgGrid.setData(proGridList);
}

function popClose(){
	window.parent.progModal.close();
}