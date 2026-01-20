/**
 * [기본관리/사용자환경설정] My결재선설정
 * 
 */

var userId 		= window.top.userId;

//grid 생성
var firstGrid = new ax5.ui.grid();
var secondGrid = new ax5.ui.grid();

var option = [];
var grid1Data = [];
var grid2Data = [];
var grid2Index = '';
var selectedGrid1_Item = [];
var selectedGrid2_Item = [];
var signNm = '';

var tmpInfo = {};
var treeObj			= null;
var treeObjData		= [];

var setting = {
	data: {
		simpleData: {
			enable: true
		}
	},
	callback: {
		onDblClick: myOnDblClick
	}
};

firstGrid.setConfig({
target: $('[data-ax5grid="first-grid"]'),
header: {
	align: "center",
	columnHeight: 30
},
body: {
	columnHeight: 28,
	onClick: function () {
		this.self.clearSelect();
		this.self.select(this.dindex);
	},
	onDataChanged: function(){
		this.self.repaint();
	}
},
columns: [
	{key: "TEAMNAME",		label: "부서명",	width: '20%', align: 'left'},
	{key: "CM_USERNAME", 	label: "성명",	width: '20%', align: 'left'},
	{key: "POSITION",	 	label: "직급",	width: '60%', align: 'left'}	 
	]
});

secondGrid.setConfig({
	target: $('[data-ax5grid="second-grid"]'),
	header: {
		align: "center",
		columnHeight: 30
	},
	body: {
		columnHeight: 28,
		onClick: function () {
			this.self.clearSelect();
			this.self.select(this.dindex);
		},
		onDataChanged: function(){
			this.self.repaint();
		}
	},
	columns: [
		{key: "ITEM1",			label: "유형",	width: '20%', align: 'left'},
		{key: "TEAMNAME", 		label: "소속",	width: '20%', align: 'left'},
		{key: "CM_USERNAME", 	label: "사용자명",	width: '17%', align: 'left'},
		{key: "POSITION",	 	label: "직급",	width: '60%', align: 'left'}	 
		],
	contextMenu: {
        items: [
        	{type: 1, label: "결재(순차)"},
            {type: 2, label: "참조"},
            {type: 3, label: "결재(후열)"}
        ],
        popupFilter: function (item, param) {
        	return true;
        },
        onClick: function (item, param) {
          grid2Index = Number(secondGrid.selectedDataIndexs);
          selectedGrid2_Item = secondGrid.list[grid2Index];
          
          selectedGrid2_Item.ITEM1 = this.label;
          selectedGrid2_Item.ITEM7 = this.type;
          
          secondGrid.updateRow(selectedGrid2_Item, grid2Index);
      }
	}
	});


$(document).ready(function(){

	screenInit();
	
	getTeamInfoTree();
	
	getSignNMADD();

	//결재선구분 
	$('#cboSignNM').bind('change', function(){
		cboSignNM_Change();
	});
	
	//검색 
	$('#btnSearch').bind('click', function(){
		search();
	});
	
	//▼ 추가
	$('#dataAdd').bind('click', function(){
		dataAdd();
	});
	
	//▲ 삭제
	$('#dataDel').bind('click', function(){
		dataDel();
	});
	
	//등록버튼
	$('#btnReg').bind('click', function(){
		register();
	});
	
	//삭제버튼
	$('#btnDel').bind('click', function(){
		btnDelete();
	});
    
});


function screenInit(){
	
	firstGrid.setData([]);
	secondGrid.setData([]);

	$('[data-ax5select="cboSignNM"]').ax5select({
		options: []
	});
}

function getTeamInfoTree(){
	
	tmpInfo = {
		chkcd		: false,
		itsw		: false,
		requestType	: 'getTeamInfoZTree'
	}
	
	ajaxAsync('/webPage/common/CommonTeamInfo', tmpInfo, 'json', successGetTeamInfoTree);
}

function successGetTeamInfoTree(data) {

	treeObjData = data;

    $.fn.zTree.init($("#treeMenu"), setting, treeObjData);
    treeObj = $.fn.zTree.getZTreeObj("treeMenu");
    treeObj.expandAll(true);
}

function myOnDblClick(event, treeId, treeNode) {
	
    var dept = treeNode.name;
    search(dept);
};

function getSignNMADD(){
	
	tmpInfo = {
		UserID		: userId,
		requestType	: 'getSignNMADD'
	}
	
	ajaxAsync('/webPage/mypage/MyInfoServlet', tmpInfo, 'json', successGetSignNMADD);
}

function successGetSignNMADD(data){

	options = [];

	$.each(data, function(i, item) {
		options.push({value: item.CM_SIGNNM, text : item.CM_SIGNNM});
	});
	
	$('[data-ax5select="cboSignNM"]').ax5select({
		options: options
	});
}


function search(dept){
	
    var userName = '';
    var deptName = '';
    if(dept != undefined){
    	deptName = dept;
    } 

    if (dept == undefined && ($('#txtUser').val() == '' || $('#txtUser').val() == null)){
	    dialog.alert('검색할 내용을 입력해주십시오.');
	    $('#txtUser').focus();
	    return;
    } else if (dept == undefined && $('#Name').is(':checked')){
	    userName =  $('#txtUser').val().trim();
    } else if(dept == undefined && $('#Buse').is(':checked')){
	    deptName = $('#txtUser').val().trim();
    } 
    
    tmpInfo = {
    	UserName	: userName,
    	DeptCd		: '',
    	DeptName	: deptName,
    	requestType	: 'getTeamNodeInfo'
    }
    
	grid1Data = ajaxCallWithJson('/webPage/mypage/MyInfoServlet', tmpInfo, 'json');
	firstGrid.setData(grid1Data);    
}

function cboSignNM_Change(index){
	
	$('#txtSignNM').val('');
	$('#txtSignNM').prop('disabled', true);
	
	if(index == undefined) index = getSelectedIndex('cboSignNM');
    
    if (index == 0){
    	$('#txtSignNM').prop('disabled', false);
    	secondGrid.setData([]);
    } else{
    	tmpInfo = {
    		UserID		: userId,
    		SignNM		: getSelectedVal('cboSignNM').text,
    		requestType	: 'getCboSelect'
    	}
    	
    	grid2Data = ajaxCallWithJson('/webPage/mypage/MyInfoServlet', tmpInfo, 'json');
    	secondGrid.setData(grid2Data);
    }
}

function dataAdd(){
	
	selectedGrid1_Item = firstGrid.list[firstGrid.selectedDataIndexs];
	
	var grid2List = secondGrid.getList();
	
   	var i = 0;
    for (i; grid2List.length > i; i++) {
   			if (grid2List[i].CM_USERNAME == selectedGrid1_Item.CM_USERNAME) return; //플렉스ver -> USERID
   		}
   			
   		selectedGrid1_Item.ITEM1 = '결재(순차)';
   		selectedGrid1_Item.ITEM7 = '1';
		if (i >= grid2List.length) {
			secondGrid.addRow(selectedGrid1_Item);
		}
}

function dataDel(){
	
//	var grid2Index = secondGrid.selectedDataIndexs + '';
//	secondGrid.deleteRow(grid2Index); //delete 사용 시 데이터에는 남아 있어 add가 작동하지 않음
	
	secondGrid.removeRow("selected"); 
}

function register(){

	grid2Index = secondGrid.selectedDataIndexs;
	selectedGrid2_Item = secondGrid.list[grid2Index];
	
	signNm = $('#txtSignNM').val().trim();
	
    if (getSelectedIndex('cboSignNM') == 0 && signNm == ''){
        dialog.alert("등록할 결재선 명칭을 입력하십시오.");
        return;
    } else if (selectedGrid2_Item == undefined){
    	dialog.alert("등록할 결재자를 선택/추가한 후 등록하십시오.");
		return;
    } else if (getSelectedIndex('cboSignNM') == 0){
    	tmpInfo = {
    		UserId		: userId,
    		SignNM		: signNm,
    		requestType	: 'getSignNMcnt'
    	}
    	
    	var result = ajaxCallWithJson('/webPage/mypage/MyInfoServlet', tmpInfo, 'json');
    	
    	if(result > 0){
    		dialog.alert("결재선 명칭이 이미 존재합니다.");
    	} else{
    		getInsert(2);
    	}
    } else{
    	getInsert(1);
    }
}

function getInsert(index){
	
	tmpInfo = {
		UserId		: userId,
		rtList		: secondGrid.getList(),
		Index		: index,
		SignName	: signNm,
		requestType	: 'getGridInsert'
	}
	
	ajaxAsync('/webPage/mypage/MyInfoServlet', tmpInfo, 'json',successGetGridInsert);
}

function successGetGridInsert(data){
	
	if (data == 0) {
		dialog.alert("결재선 등록이 실패 되었습니다.");
	} else{
		if (getSelectedIndex('cboSignNM') == 0){
			
			options.push({value: signNm, text : signNm});
			
			$('[data-ax5select="cboSignNM"]').ax5select({
				options: options
			});
			
//			getSignNMADD();
			
			$('[data-ax5select="cboSignNM"]').ax5select('setValue',signNm,true);
			$('#txtSignNM').val(signNm);
		}
		dialog.alert("결재선 등록이 완료 되었습니다.");
	}
}

function btnDelete(){
	
	if(getSelectedIndex('cboSignNM') < 1){
		dialog.alert("삭제할 결재선구분을 선택한 후 처리하십시오.")
	} else{
        tmpInfo = {
        	USERID		: userId,
        	SIGNNM		: getSelectedVal('cboSignNM').text,
        	requestType	: 'getSignNMdelete'
        }
        
    	ajaxAsync('/webPage/mypage/MyInfoServlet', tmpInfo, 'json',successDelete);
	}
}

function successDelete(data){
	
	if (data.length == '0'){
		dialog.alert(data.TASK + "\n삭제처리 실패!");
	} else{
		
//		options.splice(getSelectedIndex('cboSignNM'),1);
//		
//		$('[data-ax5select="cboSignNM"]').ax5select({
//			options: options
//		});
		
		getSignNMADD();
		
//		var selectVal = $('#cboSignNM').eq(0).val();
//		$('[data-ax5select="cboSignNM"]').ax5select('setValue',selectVal,true);
//		secondGrid.removeRow("selected"); 
		cboSignNM_Change(0);
		
		dialog.alert("삭제처리가 완료 되었습니다.");
	}
}










