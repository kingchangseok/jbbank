/**
 * 프로그램종류정보 팝업 화면 기능 정의
 * 
 * <pre>
 * 	작성자	: 이용문
 * 	버전 		: 1.0
 *  수정일 	: 2019-06-12
 * 
 */

var userName 	= window.top.userName;		// 접속자 Name
var userId 		= window.top.userId;		// 접속자 ID
var adminYN 	= window.top.adminYN;		// 관리자여부
var userDeptName= window.top.userDeptName;	// 부서명
var userDeptCd 	= window.top.userDeptCd;	// 부서코드
var selectedSystem  = window.parent.selectedSystem;
var sysCd			= selectedSystem.cm_syscd;

var prgSeqModal = new ax5.ui.modal();

var sameGrid 	= new ax5.ui.grid();
var prgGrid 	= new ax5.ui.grid();

var sameGridData 	= null;
var fSameGridData	= null;
var prgGridData 	= null;

var cboJawonData	= null;
var cboSameData		= null;
var cboOptions		= null;

var treeObj			= null;
var treeObjData		= null;

var strInfo			= null;

var setting = {
	check: {
		enable: true
	},
	data: {
		simpleData: {
			enable: true
		}
	},
	callback: {
		onClick: clickTree
	}
	
};

sameGrid.setConfig({
    target: $('[data-ax5grid="sameGrid"]'),
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
            clickSameGrid(this.dindex);
        },
        onDBLClick: function () {
        	dbClickSameGrid(this.dindex);
        },
    	trStyleClass: function () {},
    	onDataChanged: function(){
    		this.self.repaint();
    	}
    },
    columns: [
        {key: "cm_codename", 	label: "동시적용종류", 	width: '19%', align: "center"},
        {key: "chgname", 		label: "파일적용룰",  	width: '19%', align: "center"},
        {key: "cm_basedir", 	label: "기준Dir",  	width: '35%', align: "center"},
        {key: "cm_samedir", 	label: "변경Dir",  	width: '19%', align: "center"},
        {key: "cm_cmdyn", 		label: "커맨드",  	width: '8%', align: "center"},
    ]
});


prgGrid.setConfig({
    target: $('[data-ax5grid="prgGrid"]'),
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
            clickPrgGrid(this.dindex);
        },
        onDBLClick: function () {},
    	trStyleClass: function () {},
    	onDataChanged: function(){
    		this.self.repaint();
    	}
    },
    columns: [
        {key: "cm_codename", 	label: "프로그램종류", 	width: '20%', align: "center"},
        {key: "cm_vercnt", 		label: "버전수",  	width: '20%', align: "center"},
        {key: "cm_time", 		label: "정기배포",  	width: '30%', align: "center"},
        {key: "cm_exename", 	label: "대상확장자",  	width: '30%', align: "center"}
    ]
});

$('[data-ax5select="cboJawon"]').ax5select({
    options: []
});

$('[data-ax5select="cboSame"]').ax5select({
    options: []
});

$('input:radio[name=radioPrg]').wRadio({theme: 'circle-classic blue', selector: 'checkmark'});
$('input.checkbox-prg').wCheck({theme: 'square-inset blue', selector: 'checkmark', highlightLabel: false});

$(document).ready(function(){
	$('#txtSysMsg').val(selectedSystem.cm_syscd + ' ' + selectedSystem.cm_sysmsg);
	
	getProgInfoTree();
	
	_promise(10,getJawon())
		.then(function() {
			return _promise(10,getSameList());
		});
	
	getSameCbo();
	getProgList();
	getProcInfoInit();
	
	// 프로그램 종류 콤보 체인지 이벤트
	$('#cboJawon').bind('change',function() {
		var jawonVal 	= getSelectedVal('cboJawon').value;
		var prgSelItem 	= null;
		var findSw	= false;
		var tmpWork = null;
		var tmpCd	= null;
		var tmpCd2	= null;
		
		var prgList = prgGrid.getList(); 
		
		if(prgList.length > 0) {
			for(var k=0; k<prgList.length; k++) {
				if(jawonVal === prgList[k].cm_rsrccd) {
					findSw		= true;
					prgSelItem 	= prgList[k];
					prgGrid.clearSelect();
					prgGrid.select(k);
					
					var txtTime = prgSelItem.cm_time;
					txtTime = txtTime !== undefined ? txtTime.substr(0,2) + ':' + txtTime.substr(2,2) : '';
					$('#txtVer').val(prgSelItem.cm_vercnt);
					$('#txtTime').val(txtTime);
					$('#txtExename').val(prgSelItem.cm_exename);
					tmpWork = prgSelItem.cm_info;
					
					treeObj.checkAllNodes(false);
					
					for(var i=0; i<tmpWork.length; i++) {
						if(tmpWork.substr(i,1) === '1') {
							if (i == 3) 		$('#radioCkIn').wCheck('check',true);		// 실행모듈정보
				    		else if (i == 8) 	$('#radioCkOut').wCheck('check',true);		// 동시적용항목정보
				    		else if (i == 26) 	$('#radioDir').wCheck('check',true);		// 자동생성항목정보
				    		else if (i == 49) 	$('#radioRename').wCheck('check',true);		// 확장자변경
							
							tmpCd = i < 9 ? '0'+ (i+1) : String(i+1);
							
							for(var j=0; j<treeObjData.length; j++) {
								if(treeObjData[j].isParent == 'false') {
									var id = treeObjData[j].id;
									var pid = treeObjData[j].pId;
									var tmpCd2 = id.substr(pid.length,2);
									
									if(tmpCd === tmpCd2) {
										var node = treeObj.getNodeByParam('id', id);
										treeObj.selectNode(node);
										treeObj.checkNode(node,true,true);
									}
									
								}
							}
						}
	 				}
				}
			}
			
			if(!findSw) {
				$('#txtVer').val('');
				$('#txtTime').val('');
				$('#txtExename').val('');
				treeObj.checkAllNodes(false);
				$('#chkCmd').wCheck('check',false);
				$('#radioCkIn').wCheck('check',true);
				$('#txtBase').val('');
				$('#txtChgRule').val('');
				$('#txtBaseDir').val('');
				$('#txtChgDir').val('');
			}
		}
		
		sameGridFilter();
		sameGrid.setData(fSameGridData);
	});
	
	// 등록 버튼 클릭
	$('#btnReqItem').bind('click', function() {
		var tmpObj 		= new Object();
		var sameSw 		= false;
		var modSw 		= false;
		var dirSw 		= false;
		var renameSw 	= false;
		var findSw		= false;
		var exeSw		= false;
		
		var j			= 0;
		var tmpWork		= strInfo;

		var commaCnt 	= 0; // ,문자 갯수
		var dotCnt		= 0  // .문자 갯수	
		
		var txtBase 	= $('#txtBase').val().trim();
		var txtChgRule 	= $('#txtChgRule').val().trim();
		var txtBaseDir 	= $('#txtBaseDir').val().trim();
		var txtChgDir 	= $('#txtChgDir').val().trim();
		var txtExename	= $('#txtExename').val().trim();
		
		var checkedNodes= treeObj.getCheckedNodes(true);
		var sameGridArr = sameGrid.getList();
		
		console.log('tmpWork='+tmpWork);
		checkedNodes.forEach(function(node, index) {
			if(!node.isParent) {
				var id = node.id;
				var pid = node.pId;
				var code = id.substr(pid.length,2);
				if( code === '04') sameSw 	= true;
				if( code === '09') modSw 	= true;
				if( code === '27') dirSw 	= true;
				if( code === '47') dirSw 	= true;
				if( code === '44') exeSw 	= true;
				if( code === '50') renameSw = true;
				
				j = Number(code) -1 ;
				
				if(j>0 && j< (tmpWork.length -1)) {
					tmpWork = tmpWork.substr(0,j) + '1' + tmpWork.substr(j+1);
				} else if( j=== 0) {
					tmpWork = '1' + tmpWork.substr(1);
				} else {
					tmpWork = tmpWork.substr(0,j) + '1';
				}
			}
		});
		
		if(txtExename.length > 0 ) {
			if( txtExename.substr(0,1) == "," ) {
				txtExename = txtExename.substring(1, txtExename.text.length);
			}
			if( txtExename.substr(txtExename.length-1, 1) == "," ) {
				txtExename = txtExename.substring(0, txtExename.length-1);
			}
			
			/* 마지막 문자가 ,면 ,제거 */
			if( txtExename.substr(txtExename.length-1, 1) == "," ) {
				txtExename = txtExename.substring(0, txtExename.length-1);
			}
			
			/* 문자중에 ,의 갯수를 파악 */
			for(i=0; i<txtExename.length; i++ ) {
				if( txtExename.charAt(i) == "," ) {
					commaCnt++;
				}
				if( txtExename.charAt(i) == "." ) {
					dotCnt++;
				}				
			}
		}
		
		if (txtExename.length == 0) {
			dialog.alert('대상확장자를 입력하여 주시기 바랍니다.', function(){});
			return;
		}
		if (sameSw) {
			if(sameGridArr.length === 0 ){
				dialog.alert('동시적용처리속성이 체크되어 있습니다. 동시적용정보를 입력한 후 등록하시기 바랍니다.', function(){});
				return;
			} else {
				for( var i=0; i<sameGridArr.length; i++) {
					if(sameGridArr[i].cm_factcd === '04') {
						findSw = true;
						break;
					}
				}
				if(!findSw) {
					dialog.alert('동시적용처리속성이 체크되어 있습니다. 동시적용정보를 입력한 후 등록하시기 바랍니다.', function(){});
					return;
				}
			}
		}
		
		if (modSw) {
			if(sameGridArr.length === 0 ){
				dialog.alert('실행모듈체크 처리속성이 체크되어 있습니다. 동시적용정보를 입력한 후 등록하시기 바랍니다.', function(){});
				return;
			} else {
				findSw = false;
				for( var i=0; i<sameGridArr.length; i++) {
					if(sameGridArr[i].cm_factcd === '09') {
						findSw = true;
						break;
					}
				}
				if(!findSw) {
					dialog.alert('실행모듈체크 처리속성이 체크되어 있습니다. 동시적용정보를 입력한 후 등록하시기 바랍니다.', function(){});
					return;
				}
			}
		}
		
		if (dirSw) {
			if(sameGridArr.length === 0 ){
				dialog.alert('개발툴연계 또는 디렉토리기준 처리속성이 체크되어 있습니다. 자동생성항목정보를 입력한 후 등록하시기 바랍니다.', function(){});
				return;
			} else {
				findSw = false;
				for( var i=0; i<sameGridArr.length; i++) {
					if(sameGridArr[i].cm_factcd === '09') {
						findSw = true;
						break;
					}
				}
				if(!findSw) {
					dialog.alert('개발툴연계 또는 디렉토리기준 처리속성이 체크되어 있습니다. 자동생성항목정보를 입력한 후 등록하시기 바랍니다.', function(){});
					return;
				}
			}
		}
		
		if (renameSw) {
			if(sameGridArr.length === 0 ){
				dialog.alert('확장자변경 처리속성이 체크되어 있습니다. 확장자적용정보를 입력한 후 등록하시기 바랍니다.', function(){});
				return;
			} else {
				findSw = false;
				for( var i=0; i<sameGridArr.length; i++) {
					if(sameGridArr[i].cm_factcd === '50') {
						findSw = true;
						break;
					}
				}
				if(!findSw) {
					dialog.alert('확장자변경 처리속성이 체크되어 있습니다. 확장자변경정보를 입력한 후 등록하시기 바랍니다.', function(){});
					return;
				}
			}
		}
		
		// 확장자자동입력 대상
		if (exeSw) { 
			if (txtExename.length === 0) {
				dialog.alert('확장자자동입력 처리속성이 체크되어 있습니다. 자동입력 할 확장자를 입력한 후 등록하시기 바랍니다.', function(){});
				return;
			}
			//,문자가 1개 이상이면
			if( commaCnt > 0 ) { 
				dialog.alert('확장자자동입력 처리속성이 체크되어 있습니다. 자동입력 할 확장자는 1개만 입력해주세요.', function(){});
				return;
			}
			// .문자가 1개가 아니면
			if( dotCnt != 1 ) { 
				dialog.alert('확장자자동입력 처리속성이 체크되어 있습니다. 자동입력 할 확장자를 확인해주세요. [.확장자]', function(){});
				return;
			}				
		} else {
			//확장자가 입력이 되어있는데 .문자가 1개 이하일 때
			if( txtExename !== '' && (dotCnt < 1) ){ 
				dialog.alert('유효하지않은 확장자 형식입니다. 대상확장자를 확인하여 주십시오. [.확장자]', function(){});
				return;
			}
		}
		if($('#txtVer').val().trim() === ""){
			tmpObj.cm_vercnt 	= "9999";
		} else {
			tmpObj.cm_vercnt 	= $('#txtVer').val().trim();
		}
		tmpObj.cm_syscd 	= sysCd;
		tmpObj.cm_rsrccd 	= getSelectedVal('cboJawon').value;
		tmpObj.userid 		= userId;
		tmpObj.info 		= tmpWork;
		tmpObj.cm_time 		= replaceAllString($('#txtTime').val(), ':', '');
		if (txtExename.length > 0) tmpObj.cm_exename 	= txtExename;
		
		confirmDialog.confirm({
			msg: '프로그램정보를 등록하시겠습니까?',
		}, function(){ 
			if(this.key === 'ok') {
				var data = new Object();
				data = {
						etcData : tmpObj, 
						sameList : sameGridArr,
						requestType	: 'insertPrgInfo'
				}
				ajaxAsync('/webPage/modal/sysinfo/PrgKindsServlet', data, 'json',successInsertPrgInfo);
				return;
			} else{
				tmpObj = null;
				sameGridArr = null;
			}
		});
		
	});
	
	// 조회 버튼 클릭
	$('#btnQry').bind('click', function() {
		getSameCbo();
		getSameList();
		getProgList();
	});
	
	// 프로그램종류 폐기 버튼 클릭
	$('#btnCls').bind('click', function() {
		var selIndex = prgGrid.selectedDataIndexs;
		var selItem = null;
		if(selIndex.length === 0 ) {
			dialog.alert('폐기할 프로그램을 등록된 프로그램종류 그리드에서 선택하시기 바랍니다.', function() {});
			return;
		}
		
		
		
		confirmDialog.confirm({
			msg: '프로그램종류를 폐기처리하시겠습니까?',
		}, function(){
			if(this.key === 'ok') {
				selItem = prgGrid.list[selIndex];
				var data = new Object();
				data = {
					SysCd 		: sysCd,
					UserId 		: userId, 
					RsrcCd 		: selItem.cm_rsrccd,
					requestType	: 'closePrgInfo'
				}
				ajaxAsync('/webPage/modal/sysinfo/PrgKindsServlet', data, 'json',successClosePrgInfo);
			}
		});
	});
	
	$('#btnPrgSeq').bind('click', function() {
		setTimeout(function() {
			prgSeqModal.open({
				width: 800,
				height: 700,
				iframe: {
					method: "get",
					url: "./PrgSeqModal.jsp",
					param: "callBack=modalCallBack"
				},
				onStateChanged: function () {
					if (this.state === "open") {
						mask.open();
					}
					else if (this.state === "close") {
						mask.close();
						getProgInfoTree();
					}
				}
			}, function () {
			});
		}, 200);
	})
	
	// 동시등록 클릭
	$('#btnAdd').bind('click', function(){
		var tmpObj 		= new Object();
		var sameSw 		= false;
		var modSw 		= false;
		var dirSw 		= false;
		var renameSw 	= false;
		
		var txtBase 	= $('#txtBase').val().trim();
		var txtChgRule 	= $('#txtChgRule').val().trim();
		var txtBaseDir 	= $('#txtBaseDir').val().trim();
		var txtChgDir 	= $('#txtChgDir').val().trim();
		
		var checkedNodes= treeObj.getCheckedNodes(true);
		var sameGridArr = sameGrid.getList();
		
		checkedNodes.forEach(function(node, index) {
			
			if(!node.isParent) {
				var id = node.id;
				var pid = node.pId;
				var code = id.substr(pid.length,2);
				if( code === '04') sameSw 	= true;
				if( code === '09') modSw 	= true;
				if( code === '27') dirSw 	= true;
				if( code === '47') dirSw 	= true;
				if( code === '50') renameSw = true;
			}
		});
		
		if( $('#radioCkIn').is(':checked') && sameSw) {
			if(txtBase.length === 0 ) {
				dialog.alert('기준프로그램종류에 대한 동시적용룰을 입력하여 주시기 바랍니다.', function(){});
				return;
			}
			if(txtChgRule.length === 0 ) {
				dialog.alert('동시적용프로그램종류에 대한 동시적용룰을 입력하여 주시기 바랍니다.', function(){});
				return;
			}
			if(txtBaseDir.length === 0 ) {
				dialog.alert('기준프로그램종류에 대한 기준디렉토리를 입력하여 주시기 바랍니다.', function(){});
				return;
			}
			if(txtChgDir.length === 0 ) {
				dialog.alert('동시적용프로그램종류에 대한 기준디렉토리를 입력하여 주시기 바랍니다.', function(){});
				return;
			}
		}else if ($('#radioCkOut').is(':checked') && modSw == true) {
			
		} else if ($('#radioDir').is(':checked') && dirSw == true) {
			
		} else if ($('#radioRename').is(':checked') && renameSw == true) {

		} else {
			dialog.alert('처리팩터와 동시적용항목을 정확히 선택한 후 처리하시기 바랍니다.', function(){});
			return;
		}
		
		for(var i=0; i<sameGridArr.length; i++) {
			if (sameGridArr[i].cm_rsrccd == getSelectedVal('cboJawon').value  &&
				sameGridArr[i].cm_samersrc == getSelectedVal('cboSame').value &&
				sameGridArr[i].cm_basedir == txtBaseDir &&
				sameGridArr[i].cm_samedir == txtChgDir &&
				sameGridArr[i].cm_basename == txtBase &&
				sameGridArr[i].cm_samename == txtChgRule) {
				sameGrid.removeRow(i);
				break;
			}
		}
		
		tmpObj.cm_codename 	= getSelectedVal('cboSame').text;
		tmpObj.cm_rsrccd 	= getSelectedVal('cboJawon').value;
		tmpObj.cm_samersrc 	= getSelectedVal('cboSame').value;
		tmpObj.cm_basename 	= txtBase;
		tmpObj.cm_samename 	= txtChgRule;
		tmpObj.cm_basedir 	= txtBaseDir;
		tmpObj.cm_samedir 	= txtChgDir;
		tmpObj.chgname 		= txtBase + "->"+txtChgRule;
		
		if ($('#radioCkOut').is(':checked')) 		tmpObj.cm_factcd = "09";
		else if ($('#radioCkIn').is(':checked')) 	tmpObj.cm_factcd = "04";
		else if ($('#radioDir').is(':checked')) 	tmpObj.cm_factcd = "27";
		else if($('#radioRename').is(':checked')) 	tmpObj.cm_factcd = "50";
		
		if ($('#chkCmd').is(':checked')) tmpObj.cm_cmdyn = "Y";
		else tmpObj.cm_cmdyn = "N";
		
		sameGrid.addRow(tmpObj);
		sameGrid.clearSelect();
	});
	
	// 우선순위적용 위로
	$('#btnUp').bind('click', function() {
		var selIndex = prgGrid.selectedDataIndexs;
		var selIndex = selIndex[0];
		var selItem	 = prgGrid.list[selIndex];
		
		if(selIndex === 0 ) return;
		
		// 그리드 포커스 이동시 index 0이면 가장아래로 가는 오류? 발생 하므로 HOME으로 이동
		var focusIn = selIndex -1 === 0 ? 'HOME' : selIndex -1; 
		
		prgGrid.addRow(selItem, selIndex-1, {focus: focusIn});
		prgGrid.removeRow(selIndex+1);
	});
	
	// 우선순위적용 아래로
	$('#btnDown').bind('click', function() {
		var selIndex = prgGrid.selectedDataIndexs;
		var selIndex = selIndex[0];
		var selItem	 = prgGrid.list[selIndex];
		
		if(selIndex === prgGridData.length-1 ) return;
		
		prgGrid.addRow(selItem, selIndex+2, {focus: selIndex+1});
		prgGrid.removeRow(selIndex);
	});
	
	// 우선순위적용 세팅 버튼 클릭
	$('#btnReqSeq').bind('click',function() {
		if(prgGridData.length <= 0 ) return;
		var SecuCd = '';
		var prgList = prgGrid.getList(); 
		
		prgList.forEach(function(prgItem, index) {
			if(SecuCd.length > 0) SecuCd += ',';
			
			SecuCd += prgItem.cm_rsrccd;
		})
		
		if(SecuCd.length === 0) return;
		
		var data = new Object();
		data = {
			SecuCd 		: SecuCd,
			SysCd 		: sysCd,
			requestType	: 'setPrgSeq'
		}
		ajaxAsync('/webPage/modal/sysinfo/PrgKindsServlet', data, 'json',successSetPrgSeq);
		
	});
	
	// 닫기 버튼 클릭
	$('#btnExit').bind('click',function() {
		popClose();
	});
	
});

// 트리노드 클릭시 check box에 선택 되도록
function clickTree(event, treeId, treeNode) {
	var node = treeObj.getNodeByParam('id', treeNode.id);
	var checkStatus = node.getCheckStatus();
	var checkSw = checkStatus.checked;
	
	if(checkSw) {
		treeObj.selectNode(node);
		treeObj.checkNode(node, false, true);
	} else {
		treeObj.selectNode(node);
		treeObj.checkNode(node, true, true);
	}
	
	
}

function successInsertPrgInfo(data) {
	dialog.alert('프로그램정보 등록처리를 완료하였습니다.',function() {
		$('#btnQry').trigger('click');
	});
}

function successClosePrgInfo(data) {
	dialog.alert('프로그램정보 폐기처리를 완료하였습니다.',function() {
		$('#btnQry').trigger('click');
	});
}


// 우선순위 적용 세팅 완료
function successSetPrgSeq(data) {
	dialog.alert('프로그램처리순서 등록처리를 완료하였습니다.', function(){
		$('#btnQry').trigger('click');
	});
}

// 동시적용 리스트 클릭
function clickSameGrid(index) {
	var sameItem = sameGrid.list[index];
	$('[data-ax5select="cboSame"]').ax5select('setValue',sameItem.cm_samersrc,true);
	
	if(sameItem.cm_cmdyn === 'Y') $('#chkCmd').wCheck('check',true);
	else $('#chkCmd').wCheck('check',false);
	
	$('#txtBase').val(sameItem.cm_basename);
	$('#txtChgRule').val(sameItem.cm_samename);
	$('#txtBaseDir').val(sameItem.cm_basedir);
	$('#txtChgDir').val(sameItem.cm_samedir);
	
	if (sameItem.cm_factcd == "04") 	 $('#radioCkIn').wCheck('check',true);		// 실행모듈정보
	else if (sameItem.cm_factcd == "09") $('#radioCkOut').wCheck('check',true);		// 동시적용항목정보
	else if (sameItem.cm_factcd == "27") $('#radioDir').wCheck('check',true);		// 자동생성항목정보
	else $('#radioRename').wCheck('check',true);									// 확장자변경
	
}

// 동시적용 리스트 더블 클릭
function dbClickSameGrid(index) {
	sameGrid.removeRow(index);
}

// 프로그램 리스트 클릭
function clickPrgGrid(index) {
	var prgItem = prgGrid.list[index];
	$('[data-ax5select="cboJawon"]').ax5select('setValue',prgItem.cm_rsrccd,true);
	$('#cboJawon').trigger('change');
}

// 처리속성수 가져오기
function getProcInfoInit() {
	var data = new Object();
	data = {
		requestType	: 'getProcInfoInit'
	}
	ajaxAsync('/webPage/modal/sysinfo/PrgKindsServlet', data, 'json',successGetProcInfoInit);
}

function successGetProcInfoInit(data) {
	strInfo = data;
}

// 동시적용 리스트 가져오기
function getSameList() {
	var data = new Object();
	data = {
		SysCd : sysCd,
		requestType	: 'getSameList'
	}
	ajaxAsync('/webPage/modal/sysinfo/PrgKindsServlet', data, 'json',successGetSameList);
}

function successGetSameList(data) {
	sameGridData = data;
	_promise(10,sameGridFilter())
	.then(function() {
		return _promise(10,sameGrid.setData(fSameGridData));
	});
	
}

// 동시적용 그리드 필터
function sameGridFilter() {
	fSameGridData = [];
	var jawonVal = getSelectedVal('cboJawon').value;
	
	sameGridData.forEach(function(item, index) {
		if(item.cm_rsrccd === jawonVal) {
			fSameGridData.push(item);
		}
	});
}

// 프로그램종류 리스트 가져오기
function getProgList() {
	var data = new Object();
	data = {
		SysCd : sysCd,
		requestType	: 'getProgList'
	}
	ajaxAsync('/webPage/modal/sysinfo/PrgKindsServlet', data, 'json',successGetProgList);
}

function successGetProgList(data) {
	prgGridData = data;
	prgGrid.setData(data);
}

// 자원 콤보 가져오기
function getJawon() {
	console.log('getjawon');
	var codeInfos = getCodeInfoCommon([new CodeInfoOrdercd('JAWON','','N','1','')]);
	cboJawonData 	= codeInfos.JAWON;
	cboOptions 		= [];
	$.each(cboJawonData,function(key,value) {
		cboOptions.push({value: value.cm_micode, text: value.cm_codename});
	});
	
	$('[data-ax5select="cboJawon"]').ax5select({
        options: cboOptions
	});
	
	cboOptions = [];
}

// 동시적용 자원 콤보 가져오기
function getSameCbo() {
	var data = new Object();
	data = {
		SysCd : sysCd,
		requestType	: 'getSameCbo'
	}
	ajaxAsync('/webPage/modal/sysinfo/PrgKindsServlet', data, 'json',successGetSameCbo);
}

function successGetSameCbo(data) {
	cboSameData = data;
	$.each(cboSameData,function(key,value) {
		cboOptions.push({value: value.cm_micode, text: value.cm_codename});
	});
	
	$('[data-ax5select="cboSame"]').ax5select({
        options: cboOptions
	});
	
	cboOptions = [];
}

// 처리속성 트리정보 가져오기
function getProgInfoTree() {
	
	var data = new Object();
	data = {
		requestType	: 'getProgInfoTree'
	}
	ajaxAsync('/webPage/modal/sysinfo/PrgKindsServlet', data, 'json',successGetProgInfoTree);
	
}

function successGetProgInfoTree(data) {
	treeObjData = data;
	$.fn.zTree.init($("#tvInfo"), setting, data);
	treeObj = $.fn.zTree.getZTreeObj("tvInfo");
}

function popClose() {
	window.parent.prgKindsModal.close();
}

