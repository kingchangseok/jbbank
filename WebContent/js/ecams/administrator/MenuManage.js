/**
 * MENU 관리화면
 *
 * <pre>
 * 	작성자	: 이성현
 * 	버전 		: 1.0
 *  수정일 	: 2019-07-09
 *
 */

/*
var userName 	= window.top.userName;
var userid 		= window.top.userId;
var adminYN 	= window.top.adminYN;
var userDeptName= window.top.userDeptName;
var userDeptCd 	= window.top.userDeptCd;
var strReqCD 	= window.top.reqCd;
*/

var userName 	= window.top.userName;
var userid 		= window.top.userId;
var adminYN 	= window.top.adminYN;
var strReqCD 	= window.top.reqCd;

//grid 생성
var firstGrid = new ax5.ui.grid();
var cboRequestData	= [];
var treeObj			= null;
var treeObjData		= null;

var setting = {
	data: {
		simpleData: {
			enable: true
		}
	}
};

$(document).ready(function(){
	if (adminYN != true) {
		dialog.alert('블법적인 접근입니다. 정상적으로 로그인하시기 바랍니다.');
		return;
	} 
	
	if(strReqCD != null && strReqCD != ""){
		if(strReqCD.length > 2) strReqCD.substring(0, 2);
		else strReqCD = "";
	}

    // activate Nestable for list 1
    $('#nestable').nestable({
        group: 1
    }).on('change', updateOutput);

    // activate Nestable for list 2
    $('#nestable2').nestable({
        group: 1
    }).on('change', updateOutput);

    // output initial serialised data
    updateOutput($('#nestable').data('output', $('#nestable-output')));
    updateOutput($('#nestable2').data('output', $('#nestable2-output')));


	Cbo_reqcd();
	setGrid();
	setCboMenu();
	CboMaCode();

	// 상위메뉴 이벤트
	$('#Cbo_selMenu').bind('change', function() {
		cbo_selMenu_click();
	});

	// 하위메뉴 이벤트
	$('#Cbo_Menu').bind('change', function() {
		Cbo_Menu_Click();
	});

	// 메뉴 수정 및 등록 이벤트
	$('#Cbo_MaCode').bind('change', function() {
		Cbo_MaCode_click();
	});

	// 메뉴 순서 등록 이벤트
	$('#btnAply').bind('click', function() {
		Cmd_Ip_Click(3);
	});

	// 메뉴 등록 및 수정, 삭제 이벤트
	$('#btnInsert').bind('click', function() {
		Cmd_Ip_Click(0);
	});

	$('#btnDelete').bind('click', function() {
		Cmd_Ip_Click(1);
	});

	// 조회버튼 클릭 이벤트
	$('#btnFact').bind('click', function(){
		Cmd_Ip_Click(2);
	});
});

var updateOutput = function (e) {
    var list = e.length ? e : $(e.target),
            output = list.data('output');
    if (window.JSON) {
        output.val(window.JSON.stringify(list.nestable('serialize')));//, null, 2));
    } else {
        output.val('JSON browser support required for this demo.');
    }
};

function Cbo_reqcd(){

	var codeInfos = getCodeInfoCommon( [new CodeInfo('REQUEST','SEL','Y')] );
	cboRequestData = codeInfos.REQUEST;
	codeInfos = null;

	// 메뉴 리스트 셀렉트박스에 넣기
	$('[data-ax5select="Cbo_reqcd"]').ax5select({
        options: injectCboDataToArr(cboRequestData, 'cm_micode' , 'cm_codename')
   	});
}

function CboMaCode(){
	var ajaxResultData = null;
	var tmpData = {
		requestType : 'getMenuList',
		sqlGB		: 'NEW'
	}
	ajaxResultData = ajaxCallWithJson('/webPage/ecmm/Cmm0500Servlet', tmpData, 'json');
	// 메뉴 리스트 셀렉트박스에 넣기
	$('[data-ax5select="Cbo_MaCode"]').ax5select({
        options: injectCboDataToArr(ajaxResultData, 'cm_menucd' , 'cm_maname')
   	});
}

function setCboMenu(){
	var options = [];
	var Cbo_selMenu = [
		{cm_codename : "선택하세요", cm_micode : "0"},
		{cm_codename : "상위메뉴", cm_micode : "1"},
		{cm_codename : "하위메뉴", cm_micode : "2"}
	]

	$.each(Cbo_selMenu,function(key,value) {
	    options.push({value: value.cm_micode, text: value.cm_codename});
	});

	$('[data-ax5select="Cbo_selMenu"]').ax5select({
        options: options
	});

	$('[data-ax5select="Cbo_Menu"]').ax5select({
	    options: []
	});

	$("#Cbo_Menu").hide();
}

// getMenuList
function getMenuList(temp){
	var ajaxResultData = null;
	var tmpData = {
		requestType : 'getMenuList',
		sqlGB		: temp
	}
	ajaxResultData = ajaxCallWithJson('/webPage/ecmm/Cmm0500Servlet', tmpData, 'json');

	return ajaxResultData;
}

function cbo_selMenu_click(){
	var resultData;

	if(getSelectedVal('Cbo_selMenu').text === "선택하세요"){
		$("#Cbo_Menu").hide();
		return;
	}

	if(getSelectedVal('Cbo_selMenu').text === "상위메뉴"){

		$('[data-ax5select="Cbo_Menu"]').ax5select({
	        options: []
		});

		$("#Cbo_Menu").hide();

		resultData = getMenuList("999");

		if(resultData.length == 0 || resultData == null){
			dialog.alert("모든메뉴가 사용중입니다.\n 신규등록 후 선택하세요.");
			return;
		}

		$('#tmpTest *').remove();
		if(resultData[0].ID === "999"){
			$.each(resultData,function(key,value) {
				var option = $("<li class='dd-item' data-id="+value.cm_menucd+"><div class='dd-handle'>"+value.cm_maname+"</div></li>");
				$('#tmpTest').append(option);
			});

			resultData = null;

			if(getSelectedVal('Cbo_selMenu').text === "상위메뉴"){
				resultData = getMenuList("LOW");
				$('#tmpTest2 *').remove();
				$.each(resultData,function(key,value) {
					var option = $("<li class='dd-item' data-id="+value.cm_menucd+"><div class='dd-handle'>"+value.cm_maname+"</div></li>");
					$('#tmpTest2').append(option);
				});
			}
		}
	} else {
		$("#Cbo_Menu").show();
		var options = [];
		resultData = null;

		resultData = getMenuList("999");
		$('#tmpTest *').remove();
		$.each(resultData,function(key,value) {
			var option = $("<li class='dd-item' data-id="+value.cm_menucd+"><div class='dd-handle'>"+value.cm_maname+"</div></li>");
			$('#tmpTest').append(option);
		});

		resultData = null;

		resultData = getMenuList("LOW");
		$.each(resultData,function(key,value) {
		    options.push({value: value.cm_menucd, text: value.cm_maname});
		});

		$('[data-ax5select="Cbo_Menu"]').ax5select({
	        options: options
		});

		Cbo_Menu_Click();
	}
}

function Cbo_MaCode_click(){
	if(getSelectedVal('Cbo_MaCode').cm_maname === "신규"){
		$('#Txt_MaCode').val('');
		$('#Txt_MaFile').val('');
		$('#btnInsert').text("등록");
		$('#btnDelete').attr('disabled', true);
		$('[data-ax5select="Cbo_reqcd"]').ax5select("setValue", '00' , true);
	} else {
		$('#Txt_MaCode').val(getSelectedVal('Cbo_MaCode').cm_maname);
		$('#Txt_MaFile').val(getSelectedVal('Cbo_MaCode').cm_filename);
		$('#btnInsert').text("수정");
		$('#btnDelete').attr('disabled', false);

		for(var m=0; m<cboRequestData.length; m++){
			if(cboRequestData[m].cm_micode == getSelectedVal('Cbo_MaCode').cm_reqcd){
				$('[data-ax5select="Cbo_reqcd"]').ax5select("setValue", cboRequestData[m].cm_micode , true);
				break;
			} else {
				$('[data-ax5select="Cbo_reqcd"]').ax5select("setValue", '00' , true);
			}
		}
	}
}

function Cbo_Menu_Click(){
	var resultData = getMenuList("999");

	$('#tmpTest *').remove();
	$.each(resultData,function(key,value) {
		var option = $("<li class='dd-item' data-id="+value.cm_menucd+"><div class='dd-handle'>"+value.cm_maname+"</div></li>");
		$('#tmpTest').append(option);
	});
	resultData = null;
	var ajaxResultData = [];
	var tmpData = {
			requestType : 'getLowMenuList',
			Cbo_Menu		: getSelectedVal('Cbo_Menu').value
	}
	ajaxResultData = ajaxCallWithJson('/webPage/ecmm/Cmm0500Servlet', tmpData, 'json');


	$('#nestable *').remove();
	if(ajaxResultData.length === 0 ) {
		$('#nestable .dd-empty').remove();
		var option = $("<div class='dd-empty'></div>");
		$('#nestable').append(option);
	} else {
		var option = $("<ol  id='tmpTest2' class='dd-list'></ol>");
		$('#nestable').append(option);
		$.each(ajaxResultData,function(key,value) {
			var option = $("<li class='dd-item' data-id="+value.cm_menucd+"><div class='dd-handle'>"+value.cm_maname+"</div></li>");
			$('#nestable').children('.dd-list').append(option);
		});
	}
}

function Cmd_Ip_Click(num){
	switch (num) {
		case 0:
			if($('#Txt_MaCode').val() === "" || $('#Txt_MaCode').val() == null){
				dialog.alert("메뉴명을 입력하세요.");
				return;
			}

			if($('#Txt_MaFile').val() === "" || $('#Txt_MaFile').val() == null){
				dialog.alert("링크파일명을 입력하세요.");
				return;
			}

			var ajaxResultData = null;
	    	var data = {
    			Cbo_MaCode : getSelectedVal('Cbo_MaCode').cm_menucd,
    			Txt_MaCode : $('#Txt_MaCode').val(),
    			Txt_MaFile : $('#Txt_MaFile').val(),
    			reqcd	   : getSelectedVal('Cbo_reqcd').cm_micode,
    			requestType: 'setMenuInfo'
	    	}
	    	ajaxResultData = ajaxCallWithJson('/webPage/ecmm/Cmm0500Servlet', data, 'json');

	    	if(ajaxResultData == ""){
	    		dialog.alert("저장완료");
	    		CboMaCode();
	    		Cbo_MaCode_click();
	    	}else{
	    		dialog.alert("저장실패");
	    	}
			break;
	    case 1:
	    	if(getSelectedVal('Cbo_MaCode').cm_menucd === "000"){
	    		dialog.alert("삭제할 메뉴명을 선택하여주세요.");
	    		return;
	    	}

			var ajaxResultData = null;
	    	var tmpData = {
    			requestType: 'delMenuInfo',
    			Cbo_MaCode : getSelectedVal('Cbo_MaCode').cm_menucd
	    	}
	    	ajaxResultData = ajaxCallWithJson('/webPage/ecmm/Cmm0500Servlet', tmpData, 'json');

	    	if(ajaxResultData == ""){
	    		dialog.alert("삭제완료");
	    		CboMaCode();
	    		Cbo_MaCode_click();
	    	}else{
	    		dialog.alert("삭제실패");
	    	}
	    	break;
	    case 2:
	    	getMenuAllList();
	    	getMenuTree();
	    	break;
	    case 3:
	    	// 오른쪽 메뉴 없어도 등록 되도록 수정
	    	// if($('#nestable').children('.dd-list').children().length === 0) return;

	    	if(getSelectedVal('Cbo_selMenu').text === "선택하세요"){
	    		dialog.alert("메뉴리스트 데이터가 없습니다.");
	    		return;
	    	}

	    	var menucd = "";
	    	if(getSelectedVal('Cbo_Menu') != undefined){
	    		menucd = getSelectedVal('Cbo_Menu').value;
	    	}

	    	var tmpList = new Array();
	    	$('#nestable').children('.dd-list').children().each(function(){
	    		var data = new Object();
	    		data.cm_menucd = $(this).attr("data-id");
	    		tmpList.push(data);
	    	})

	    	var ajaxResultData = null;
	    	var tmpData = {
    			requestType : 'setMenuList',
    			Cbo_selMenu : getSelectedVal('Cbo_selMenu').text,
    			Cbo_Menu	: menucd,
    			Lst_DevStep	: tmpList
	    	}
	    	ajaxResultData = ajaxCallWithJson('/webPage/ecmm/Cmm0500Servlet', tmpData, 'json');

	    	if(ajaxResultData == ""){
	    		dialog.alert("적용완료");
	    		Cmd_Ip_Click(2);
	    	}else{
	    		dialog.alert("적용실패");
	    	}
	    	break;
	}
}

function getMenuAllList(){
	var ajaxResultData = null;
	var tmpData = {
		requestType : 'getMenuAllList',
	}
	ajaxResultData = ajaxCallWithJson('/webPage/ecmm/Cmm0500Servlet', tmpData, 'json');

	//첫번째 tree만 닫기
	//ajaxResultData[0].collapse = true;

	$(ajaxResultData).each(function(i){
		//grid tree 전체닫기
		ajaxResultData[i].collapse = true;
	});
	firstGrid.setData(ajaxResultData);
}

//트리정보 가져오기
function getMenuTree() {
	var data = new Object();
	data = {
		requestType	: 'getMenuZTree'
	}
	ajaxAsync('/webPage/ecmm/Cmm0500Servlet', data, 'json',successGetTreeInfo);
}

// 트리정보 가져오기 완료
function successGetTreeInfo(data) {
	$.fn.zTree.init($("#treeMenu"), setting, data);
	treeObj = $.fn.zTree.getZTreeObj("treeMenu");
}

function setGrid(){
	firstGrid.setConfig({
        target: $('[data-ax5grid="first-grid"]'),
        header: {
            align: "center",
            columnHeight: 30
        },
        body: {
            columnHeight: 28,
            onClick: function () {
                //this.self.select(this.dindex);
            },
        	onDataChanged: function(){
        		//그리드 새로고침 (스타일 유지)
        	    this.self.repaint();
        	}
        },
        tree: {
            use: true,
            indentWidth: 10,
            arrowWidth: 15,
            iconWidth: 18,
            icons: {
                openedArrow: '<i class="fa fa-caret-down" aria-hidden="true"></i>',
                collapsedArrow: '<i class="fa fa-caret-right" aria-hidden="true"></i>',
                groupIcon: '<i class="fa fa-folder-open" aria-hidden="true"></i>',
                collapsedGroupIcon: '<i class="fa fa-folder" aria-hidden="true"></i>',
                itemIcon: '<i class="fa fa-circle" aria-hidden="true"></i>'
            },
            columnKeys: {
                parentKey: "parentMenucd",
                selfKey: "cm_menucd"
            }
        },
        columns: [
            {key: "cm_maname", 		label: "메뉴명",  	width: '30%', treeControl: true},
            {key: "cm_filename", 	label: "링크파일명",  	width: '70%'} //formatter: function(){	return "<button>" + this.value + "</button>"},
        ]
    });
}