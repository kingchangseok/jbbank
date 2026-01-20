/**
 * 영향분석대상확장자정보 화면 기능 정의
 * 
 * <pre>
 * 	작성자	: 이성현
 * 	버전 		: 1.0
 *  수정일 	: 2021-04-26
 * 
 */

var jobGrid 		= new ax5.ui.grid();
var jobInModal 		= new ax5.ui.modal();
var jobUpModal 		= new ax5.ui.modal();

var jobGridData = null;
var selCode 	= null;
var selVal 		= null;
var selPro		= null;

jobGrid.setConfig({
    target: $('[data-ax5grid="jobGrid"]'),
    sortable: true, 
    multiSort: false,
    showRowSelector: true,
    multipleSelect: false,
    header: {
        align: "center",
    },
    body: {
        onClick: function () {
            this.self.select(this.dindex);
        },
        onDBLClick: function () {},
    	trStyleClass: function () {},
    	onDataChanged: function(){
    		this.self.repaint();
    	}
    },
    columns: [
        {key: "cm_secuexe", 	label: "취약점대상확장자",  	width: '95%'}
    ]
});


function popClose(){
	window.parent.jobModal.close();
}

$(document).ready(function(){
	getSecuExeList();
	// 새로만들기
	$('#btnIn').bind('click',function() {
		setTimeout(function() {
			jobUpModal.open({
				width: 400,
				height: 140,
				iframe: {
					method: "get",
					url: "./secuUpModal.jsp",
					param: "callBack=jobUpModalCallBack"
				},
				onStateChanged: function () {
					if (this.state === "open") {
						mask.open();
					}
					else if (this.state === "close") {
						mask.close();
					}
				}
			}, function () {
			});
		}, 200);
	});
	
	// 편집
	$('#btnUp').bind('click',function() {
		var selectedIndex = jobGrid.selectedDataIndexs;
		var selectedItem = null;
		if(selectedIndex.length <= 0 ) {
			dialog.alert('수정할 취약점확장자를 선택 후 눌러주시기 바랍니다.',function() {});
			return;
		}
		if(selectedIndex.length > 1 ) {
			dialog.alert('수정할 취약점확장자를 하나만 선택 후 눌러주시기 바랍니다.',function() {});
			return;
		}
		selectedItem = jobGrid.list[selectedIndex];
		
		selCode = selectedItem.cm_analexe;
		
		setTimeout(function() {
			jobUpModal.open({
				width: 400,
				height: 140,
				iframe: {
					method: "get",
					url: "./secuUpModal.jsp",
					param: "callBack=jobUpModalCallBack"
				},
				onStateChanged: function () {
					if (this.state === "open") {
						mask.open();
					}
					else if (this.state === "close") {
						mask.close();
					}
				}
			}, function () {
			});
		}, 200);
	});
	
	// 삭제
	$('#btnDel').bind('click',function() {
		var selectedIndex = jobGrid.selectedDataIndexs;
		var selectedItems = [];
		if(selectedIndex.length <= 0 ) {
			dialog.alert('삭제할 확장자를 선택 후 눌러주시기 바랍니다.',function() {});
			return;
		}
		
		selectedIndex.forEach(function(item,index) {
			selectedItems.push(jobGrid.list[item]);
		});
		
		confirmDialog.confirm({
			msg: '선택한 확장자를 삭제하시겠습니까?',
		}, function(){
			if(this.key === 'ok') {
				delJobInfo(selectedItems);
			}
		});
		
	});
	
	// 닫기
	$('#btnClose').bind('click',function() {
		window.parent.jobModal2.close();
	});
	
});

function delJobInfo(selectedItems) {
	var delJobInfoData = new Object(); 
	
	var etcData = new Object();
	etcData.code = selectedItems[0].cm_secuexe;
	
	delJobInfoData = {
		etcData 	: etcData,
		requestType	: 'delSecuExe',
	}
	
	console.log(delJobInfoData);
	ajaxAsync('/webPage/modal/sysinfo/Job', delJobInfoData, 'json',successDelJobInfo);
}

function successDelJobInfo(data) {
	if(data) {
		getSecuExeList();
	} else {
		dialog.lart('취약점대상확장자 삭제중 오류가 발생했습니다. 관리자에게 문의하시기 바랍니다.',function(){});
	}
}

var jobUpModalCallBack = function() {
	jobUpModal.close();
}

function getSecuExeList() {
	var jobInfoData = new Object(); 
	jobInfoData = {
		requestType		: 'getSecuExeList',
	}
	ajaxAsync('/webPage/modal/sysinfo/Job', jobInfoData, 'json',successgetJobList);
}

function successgetJobList(data) {
	jobGridData = data;
	jobGrid.setData(jobGridData);
}


