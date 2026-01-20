/**
 * 프로그램종류정보 팝업 화면 기능 정의
 * 
 * <pre>
 * 	작성자	: 이용문
 * 	버전 		: 1.0
 *  수정일 	: 2019-06-12
 * 
 */

var userName 	= window.parent.userName;
var userId 		= window.parent.userId;
var adminYN 	= window.parent.adminYN;
var userDeptName= window.parent.userDeptName;
var userDeptCd 	= window.parent.userDeptCd;

var selectedSystem  = window.parent.selectedSystem;
var sysCd			= selectedSystem != undefined ? selectedSystem.cm_syscd : null;
var prgSeqModal 	= new ax5.ui.modal();

var sameGrid 		= new ax5.ui.grid();
var prgGrid 		= new ax5.ui.grid();

var sameGridData 	= null;
var fSameGridData	= null;
var prgGridData 	= null;

var cboJawonData	= null;
var cboSameData		= null;
var cboFactData     = null;
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

$('[data-ax5select="cboJawon"]').ax5select({
    options: []
});

$('[data-ax5select="cboSame"]').ax5select({
    options: []
});

$('[data-ax5select="cboFact"]').ax5select({
    options: []
});

$('input.checkbox-prg').wCheck({theme: 'square-inset blue', selector: 'checkmark', highlightLabel: false});

$(document).ready(function(){
	selectedSystem  = window.parent.selectedSystem;
	if (window.parent.frmLoad2) createViewGrid();
	
	// 프로그램 종류 콤보 체인지 이벤트
	$('#cboJawon').bind('change',function() {
		var jawonVal 	= getSelectedVal('cboJawon').value;
		var prgSelItem 	= null;
		var findSw	= false;
		var tmpWork = null;
		var tmpCd	= null;
		var tmpCd2	= null;
		var factCd  = null;
		var firstNode = null;
		var prgList = prgGrid.getList(); 
		var prgList_len = prgList.length;
		
		if(prgList_len > 0) {
			
			var treeObjData_len = treeObjData.length;
			var k=0;
			var i=0;
			var j=0;
			
			for(k=0; k<prgList_len; k++) {
				if(jawonVal === prgList[k].cm_rsrccd) {
					findSw		= true;
					prgSelItem 	= prgList[k];
					prgGrid.clearSelect();
					prgGrid.select(k);
					
					$('#txtVer').val(prgSelItem.cm_vercnt);
					$('#txtTime').val(prgSelItem.cm_time);
					$('#txtExename').val(prgSelItem.cm_exename);
					
					tmpWork = prgSelItem.cm_info;
					var tmpWork_len = tmpWork.length;
					treeObj.checkAllNodes(false);
					
					for(i=0; i<tmpWork_len; i++) {
						if(tmpWork.substr(i,1) === '1') {
							factCd = null;
							if (i == 3) 		factCd = '04';		// 동시적용항목정보(04)
				    		else if (i == 8) 	factCd = '09';		// 실행모듈정보(09)
				    		else if (i == 26) 	factCd = '27';		// 개발툴연계(27)
				    		else if (i == 46) 	factCd = '47';		// 디렉토리기준관리(47)
							
							if (factCd != null) {
								$('[data-ax5select="cboFact"]').ax5select('setValue',factCd,true);
							} else {
								$('[data-ax5select="cboFact"]').ax5select('setValue','0000',true);
							}
							tmpCd = i < 9 ? '0'+ (i+1) : String(i+1);
							
							for(j=0; j<treeObjData_len; j++) {
								if(treeObjData[j].isParent == 'false') {
									var id = treeObjData[j].id;
									var pid = treeObjData[j].pId;
									var tmpCd2 = id.substr(pid.length,2);
									if(tmpCd === tmpCd2) {
										var node = treeObj.getNodeByParam('id', id);
										if(firstNode == null){
											firstNode = node;
										} else if (firstNode.pId > node.pId || firstNode.cm_order > node.cm_order){
											firstNode = node;
										}
										treeObj.selectNode(node);
										treeObj.checkNode(node,true,true);
										node = null;
									}
									
								}
							}
						}
	 				}
	 				treeObj.selectNode(firstNode);
					treeObj.cancelSelectedNode(firstNode);
				}
			}
			
			if(!findSw) {
				$('#txtVer').val('');
				$('#txtTime').val('');
				$('#txtExename').val('');
				treeObj.checkAllNodes(false);
				$('#chkCmd').wCheck('check',false);
				$('#txtBase').val('');
				$('#txtChgRule').val('');
				$('#txtBaseDir').val('');
				$('#txtChgDir').val('');
				$('[data-ax5select="cboFact"]').ax5select('setValue','0000',true);
			}
		}
		jawonVal 	= null;
		prgSelItem 	= null;
		tmpWork = null;
		tmpCd	= null;
		tmpCd2	= null;
		factCd  = null;
		firstNode = null;
		prgList = null;
		
		sameGridFilter();
		sameGrid.setData(fSameGridData);
	});
	
	// 등록 버튼 클릭
	$('#btnReqItem').bind('click', function() {
		var tmpObj 		= new Object();
		var sameSw 		= false;
		var modSw 		= false;
		var dirSw		= false;
		var renameSw 	= false;
		var exeSw		= false;
		var findSw		= false;
		
		var j			= 0;
		var i           = 0;
		var tmpWork		= strInfo;

		var commaCnt 	= 0; // ,문자 갯수
		var dotCnt		= 0; // .문자 갯수	
		
		var txtBase 	= $('#txtBase').val().trim();
		var txtChgRule 	= $('#txtChgRule').val().trim();
		var txtBaseDir 	= $('#txtBaseDir').val().trim();
		var txtChgDir 	= $('#txtChgDir').val().trim();
		var txtExename	= $('#txtExename').val().trim();
		
		var checkedNodes= treeObj.getCheckedNodes(true);
		var sameGridArr = sameGrid.getList();
		
		//console.log('tmpWork 1='+tmpWork);
		checkedNodes.forEach(function(node, index) {
			if(!node.isParent) {
				var id = node.id;
				var pid = node.pId;
				var code = id.substr(pid.length,2);
				
				if( code === '04') sameSw 	= true;
				if( code === '09') modSw 	= true;
				if( code === '27') dirSw	= true;
				if( code === '47') dirSw	= true;
				if( code === '44') exeSw    = true;
				
				j = Number(code) -1 ;
				
				if(j>0 && j < (tmpWork.length -1)) {
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
			
			commaCnt = (txtExename.match(/,/g) || []).length;
			dotCnt = (txtExename.match(/\./g) || []).length;
		}
		
		var same_len = sameGridArr.length;
		if (sameSw) {
			if(same_len === 0 ){
				dialog.alert('동시적용처리속성이 체크되어 있습니다. 동시적용정보를 입력한 후 등록하시기 바랍니다.', function(){});
				return;
			} else {
				findSw = false;
				for( i=0; i<same_len; i++) {
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
			if(same_len === 0 ){
				dialog.alert('실행모듈체크 처리속성이 체크되어 있습니다. 실행모듈정보를 입력한 후 등록하시기 바랍니다.', function(){});
				return;
			} else {
				findSw = false;
				for( i=0; i<same_len; i++) {
					if(sameGridArr[i].cm_factcd === '09') {
						findSw = true;
						break;
					}
				}
				if(!findSw) {
					dialog.alert('실행모듈체크 처리속성이 체크되어 있습니다. 실행모듈정보를 입력한 후 등록하시기 바랍니다.', function(){});
					return;
				}
			}
		}
		
		if (dirSw) {
			if(same_len === 0 ){
				dialog.alert('개발툴연계 또는 디렉토리기준 처리속성이 체크되어 있습니다. 동시적용정보를 입력한 후 등록하시기 바랍니다.', function(){});
				return;
			} else {
				findSw = false;
				for( i=0; i<same_len; i++) {
					if(sameGridArr[i].cm_factcd === '27') {
						findSw = true;
						break;
					}
				}
				if(!findSw) {
					dialog.alert('개발툴연계 또는 디렉토리기준 처리속성이 체크되어 있습니다. 동시적용정보를 입력한 후 등록하시기 바랍니다.', function(){});
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
		tmpObj.cm_time 		= $('#txtTime').val().trim();
		if (txtExename.length > 0) tmpObj.cm_exename = txtExename;
		
		confirmDialog.confirm({
			msg: '프로그램정보를 등록하시겠습니까?',
		}, function(){ 
			if(this.key === 'ok') {
				var data = new Object();
				data = {
					etcData : tmpObj, 
					sameList : sameGridArr,
					requestType	: 'rsrcInfo_Ins'
				}
				ajaxAsync('/webPage/ecmm/Cmm0200_ProgServlet', data, 'json',successInsertPrgInfo);
				tmpObj = null;
				sameGridArr = null;
				data = null;
				return;
			} else{
				tmpObj = null;
				sameGridArr = null;
			}
		});

		tmpWork		= null;
		txtBase 	= null;
		txtChgRule 	= null;
		txtBaseDir 	= null;
		txtChgDir 	= null;
		txtExename	= null;
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
					SysCd : sysCd,
					UserId : userId,
					RsrcCd : selItem.cm_rsrccd,
					requestType	: 'rsrcInfo_Close'
				}
				ajaxAsync('/webPage/ecmm/Cmm0200_ProgServlet', data, 'json',successClosePrgInfo);
			}
		});
	});
	
	$('#btnPrgSeq').bind('click', function() {
		setTimeout(function() {
			prgSeqModal.open({
				width: 800,
				height: 600,
				iframe: {
					method: "get",
					url: "../../modal/sysinfo/PrgSeqModal.jsp"
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
				if( code === '47') dirSw = true;
			}
		});
		
		if( getSelectedVal('cboFact').value == '04' && sameSw) {
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
		}else if (getSelectedVal('cboFact').value == '09' && modSw) {
			
		} else if (getSelectedVal('cboFact').value == '27' && dirSw) {
			
		} else if (getSelectedVal('cboFact').value == '47' && dirSw) {

		} else {
			dialog.alert('처리팩터와 동시적용항목을 정확히 선택한 후 처리하시기 바랍니다.', function(){});
			return;
		}
		
		for(var i=0; i<sameGridArr.length; i++) {
			if (sameGridArr[i].cm_rsrccd == getSelectedVal('cboJawon').value  &&
				sameGridArr[i].cm_samersrc == getSelectedVal('cboSame').value &&
				sameGridArr[i].cm_factcd == getSelectedVal('cboFact').value &&
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
		tmpObj.cm_factcd 	= getSelectedVal('cboFact').value;
		tmpObj.cm_basename 	= txtBase;
		tmpObj.cm_samename 	= txtChgRule;
		tmpObj.cm_basedir 	= txtBaseDir;
		tmpObj.cm_samedir 	= txtChgDir;
		tmpObj.factname 	= getSelectedVal('cboFact').text;
		
		if($("#chkCmd").is(":checked")) {
			tmpObj.cm_cmdyn = "Y";
		} else {
			tmpObj.cm_cmdyn = "N";
		}
		
		sameGrid.addRow(tmpObj);
		sameGrid.clearSelect();
		
		tmpObj 		= null;
		txtBase 	= null;
		txtChgRule 	= null;
		txtBaseDir 	= null;
		txtChgDir 	= null;
		
		checkedNodes= null;
		sameGridArr = null;
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
			SecuCd : SecuCd,
			SysCd : sysCd,
			requestType	: 'rsrcInfo_seq'
		}
		ajaxAsync('/webPage/ecmm/Cmm0200_ProgServlet', data, 'json',successSetPrgSeq);
	});
});

function createViewGrid() {
	sameGrid 	= new ax5.ui.grid();
	prgGrid 	= new ax5.ui.grid();
	
	sameGrid.setConfig({
	    target: $('[data-ax5grid="sameGrid"]'),
	    sortable: true, 
	    multiSort: true,
	    showRowSelector: false,
	    page : false,
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
	        {key: "cm_codename", 	label: "구분", 			width: '15%', align: "left"},
	        {key: "factname", 		label: "동시적용종류", 		width: '15%', align: "left"},
	        {key: "cm_basename", 	label: "파일적용[기준]",  	width: '15%', align: "center"},
	        {key: "cm_samename", 	label: "파일적용[변경]",  	width: '15%', align: "center"},
	        {key: "cm_basedir", 	label: "기준Dir",  		width: '20%', align: "left"},
	        {key: "cm_samedir", 	label: "변경Dir",  		width: '20%', align: "left"}
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
	        {key: "cm_codename", 	label: "프로그램종류", 	width: '25%', align: "left"},
	        {key: "cm_vercnt", 		label: "버전수",  	width: '25%', align: "center"},
	        {key: "cm_time", 		label: "정기배포",  	width: '15%', align: "center"},
	        {key: "cm_exename", 	label: "대상확장자",  	width: '35%', align: "left"}
	    ]
	});
	
	screenLoad();
	
	if (prgGridData != null && prgGridData.length > 0) {
		prgGrid.setData(prgGridData);
	}
	if (sameGridData != null && sameGridData.length > 0) {
		sameGrid.setData(sameGridData);
	}
}
function screenLoad() {
	if (selectedSystem != null) {		
		getProgInfoTree();
		
		_promise(10,getJawon()).then(function() {
			return _promise(10,getSameList());
		});
		
		getSameCbo();
		getProgList();
		getProcInfoInit();
	}
}

// 트리노드 클릭시 check box에 선택 되도록
function clickTree(event, treeId, treeNode) {
	var node = treeObj.getNodeByParam('id', treeNode.id);
	var checkStatus = node.getCheckStatus();
	
	if(checkStatus.checked) {
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
		
	$('#txtBase').val(sameItem.cm_basename);
	$('#txtChgRule').val(sameItem.cm_samename);
	$('#txtBaseDir').val(sameItem.cm_basedir);
	$('#txtChgDir').val(sameItem.cm_samedir);
	
	if(sameItem.cm_cmdyn == "Y") $("#chkCmd").wCheck("check", true);
	else $("#chkCmd").wCheck("check", false);
	
	$('[data-ax5select="cboFact"]').ax5select('setValue','0000',true);
	
	if (sameItem.cm_factcd != null && sameItem.cm_factcd != '') {
		$('[data-ax5select="cboFact"]').ax5select('setValue',sameItem.cm_factcd,true);
	}
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
		requestType	: 'getProcInfo_Init'
	}
	ajaxAsync('/webPage/ecmm/Cmm0100Servlet', data, 'json',successGetProcInfoInit);
	data = null;
}

function successGetProcInfoInit(data) {
	strInfo = data;
}

// 동시적용 리스트 가져오기
function getSameList() {
	var data = new Object();
	data = {
		SysCd : selectedSystem.cm_syscd,
		requestType	: 'getSameList'
	}
	ajaxAsync('/webPage/ecmm/Cmm0200_ProgServlet', data, 'json',successGetSameList);
	data = null;
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
	jawonVal = null;
}

// 프로그램종류 리스트 가져오기
function getProgList() {
	var data = new Object();
	data = {
		SysCd : selectedSystem.cm_syscd,
		requestType	: 'getProgList'
	}
	ajaxAsync('/webPage/ecmm/Cmm0200_ProgServlet', data, 'json',successGetProgList);
	data = null;
}

function successGetProgList(data) {
	prgGridData = data;
	prgGrid.setData(data);
}

// 자원 콤보 가져오기
function getJawon() {
	var codeInfos = getCodeInfoCommon([
	    new CodeInfoOrdercd('JAWON','','N','2',''),
	    new CodeInfoOrdercd('RSCHKITEM','','N','1',''),
    ]);
	cboJawonData 	= codeInfos.JAWON;
	cboFactData 	= codeInfos.RSCHKITEM;
	
	cboOptions 		= [];
	$.each(cboJawonData,function(key,value) {
		cboOptions.push({value: value.cm_micode, text: value.cm_codename + " [ " +value.cm_micode+ " ]" });
	});
	
	$('[data-ax5select="cboJawon"]').ax5select({
        options: cboOptions
	});
	
	cboOptions = [];
	
	cboOptions.push({value: '0000', text: '선택하세요'});
	$.each(cboFactData,function(key,value) {
		if (value.cm_micode == '04' || value.cm_micode == '09') {
			cboOptions.push({value: value.cm_micode, text: value.cm_codename});
		} else if (value.cm_micode == '27') {
			cboOptions.push({value: value.cm_micode, text: '자동생성항목'});
		}
	});
	
	$('[data-ax5select="cboFact"]').ax5select({
        options: cboOptions
	});
	
	cboOptions = [];
}

// 동시적용 자원 콤보 가져오기
function getSameCbo() {
	var data = new Object();
	data = {
		SysCd : selectedSystem.cm_syscd,
		requestType	: 'getProgInfo'
	}
	ajaxAsync('/webPage/ecmm/Cmm0200_CopyServlet', data, 'json',successGetSameCbo);
	data = null;
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
		requestType	: 'getProgInfoZTree'
	}
	ajaxAsync('/webPage/ecmm/Cmm0100Servlet', data, 'json',successGetProgInfoTree);
	data = null;
}

function successGetProgInfoTree(data) {
	treeObjData = data;
	$.fn.zTree.init($("#tvInfo"), setting, data);
	treeObj = $.fn.zTree.getZTreeObj("tvInfo");
}

function popClose() {
	window.parent.prgKindsModal.close();
}