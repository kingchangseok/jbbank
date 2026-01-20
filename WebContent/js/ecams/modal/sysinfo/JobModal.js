/**
 * 업무등록 팝업 화면 기능 정의
 * 
 * <pre>
 * 	작성자	: 이용문
 * 	버전 		: 1.0
 *  수정일 	: 2019-05-29
 * 
 */
var jobGrid 		= new ax5.ui.grid();
var jobInModal 		= new ax5.ui.modal();
var jobUpModal 		= new ax5.ui.modal();

var jobGridData = [];
var selCode 	= [];
var selVal 		= [];
var selPro		= [];

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
        onDBLClick: function () {
        	if (this.dindex < 0) return;
        	
	       	var selIn = jobGrid.selectedDataIndexs;
	       	if(selIn.length === 0) return;
       	 
	       	btnUp_click(this.item);
        },
    	trStyleClass: function () {},
    	onDataChanged: function(){
    		this.self.repaint();
    	}
    },
    columns: [
        {key: "cm_jobcd", 	label: "업무코드",  	width: '30%'},
        {key: "cm_jobname", label: "업무명",  	width: '70%', align: 'left'},
    ]
});


function popClose(){
	window.parent.jobModal.close();
}

$(document).ready(function(){
	getJobList();
	// 새로만들기
	$('#btnIn').bind('click',function() {
		setTimeout(function() {
			jobUpModal.open({
				width: 400,
				height: 140,
				iframe: {
					method: "get",
					url: "./JobUpModal.jsp"
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
			dialog.alert('수정할 업무를 선택 후 눌러주시기 바랍니다.',function() {});
			return;
		}
		if(selectedIndex.length > 1 ) {
			dialog.alert('수정할 업무를 하나만 선택 후 눌러주시기 바랍니다.',function() {});
			return;
		}
		selectedItem = jobGrid.list[selectedIndex];
		
		btnUp_click(selectedItem);
	});
	
	// 삭제
	$('#btnDel').bind('click',function() {
		var selectedIndex = jobGrid.selectedDataIndexs;
		var selectedItems = [];
		if(selectedIndex.length <= 0 ) {
			dialog.alert('삭제할 업무를 선택 후 눌러주시기 바랍니다.',function() {});
			return;
		}
		
		selectedIndex.forEach(function(item,index) {
			selectedItems.push(jobGrid.list[item]);
		});
		
		confirmDialog.confirm({
			msg: '선택한 업무를 삭제하시겠습니까?',
		}, function(){
			if(this.key === 'ok') {
				delJobInfo(selectedItems);
			}
		});
		
	});
	
	// 닫기
	$('#btnClose').bind('click',function() {
		popClose('jobModal');
	});
	
	// 엑셀저장
	/*$('#btnExl').bind('click',function() {
		excelExport(jobGrid,"JobList.xls");
	})*/
});

function btnUp_click(selectedItems) {
	selCode = selectedItems.cm_jobcd;
	selVal = selectedItems.cm_jobname;
	selPro = selectedItems.cm_deptcd;
	
	setTimeout(function() {
		jobUpModal.open({
			width: 400,
			height: 140,
			iframe: {
				method: "get",
				url: "./JobUpModal.jsp"
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
}

function delJobInfo(selectedItems) {
	var delJobInfoData = new Object(); 
	delJobInfoData = {
		jobList 	: selectedItems,
		requestType	: 'delJobInfo',
	}
	ajaxAsync('/webPage/ecmm/Cmm0100Servlet', delJobInfoData, 'json',successDelJobInfo);
}

function successDelJobInfo(data) {
	if(data) {
		getJobList();
	} else {
		dialog.lart('업무 삭제중 오류가 발생했습니다. 관리자에게 문의하시기 바랍니다.',function(){});
	}
}

function getJobList() {
	var jobInfoData = new Object(); 
	jobInfoData = {
		requestType		: 'getJobList',
	}
	ajaxAsync('/webPage/ecmm/Cmm0100Servlet', jobInfoData, 'json',successgetJobList);
}

function successgetJobList(data) {
	jobGridData = data;
	jobGrid.setData(jobGridData);
}