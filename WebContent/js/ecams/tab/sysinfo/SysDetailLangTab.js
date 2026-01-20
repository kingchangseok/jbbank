/**
 * 시스템상세정보 팝업 [언어정보] 화면 기능 정의
 * 
 * <pre>
 * 작성자: 방지연
 * 버전: 1.0
 * 수정일: 2022-08-00
 * 
 */

var userId 			= window.parent.userId;
var selectedSystem 	= window.parent.selectedSystem;

var firstGrid 	= new ax5.ui.grid();
var secondGrid 	= new ax5.ui.grid();
var langModal 	= new ax5.ui.modal();

var firstGridData 	= [];
var secondGridData 	= [];
var chkLangData 	= [];

$(document).ready(function() {
	if (window.parent.frmLoad7) {
		createViewGrid();
	}
});

function createViewGrid() {
	firstGrid.setConfig({
		target : $('[data-ax5grid="firstGrid"]'),
		sortable : true,
		multiSort : true,
		showRowSelector : true,
		multipleSelect : false,
		header : {
			align : "center",
		},
		body : {
			onClick : function() {
				this.self.clearSelect();
				this.self.select(this.dindex);
				clickFirstGrid(this.dindex);
			},
			onDBLClick : function() {
			},
			trStyleClass : function() {
			},
			onDataChanged : function() {
				this.self.repaint();
			}
		},
		columns : [
			{key : "cm_codename", label : "프로그램종류", width : '10%', align : "left"} 
		]
	});

	secondGrid.setConfig({
		target : $('[data-ax5grid="secondGrid"]'),
		sortable : true,
		multiSort : true,
		showRowSelector : true,
		multipleSelect : true,
		header : {
			align : "center",
		},
		body : {
			onClick : function() {
				//this.self.clearSelect();
				//this.self.select(this.dindex);
			},
			onDBLClick : function() {
			},
			trStyleClass : function() {
			},
			onDataChanged : function() {
				this.self.repaint();
			}
		},
		columns : [
			{key : "cm_micode",		label : "언어코드",	width : '40%',	align : "left"},
			{key : "cm_codename",	label : "언어명",		width : '60%',	align : "left"}
		]
	});
	screenLoad();
}

function screenLoad() {
	getCboRsrc();

	if (selectedSystem != null) {
		getProgList(selectedSystem.cm_syscd);
		getChkLangInfo(selectedSystem.cm_syscd);

		// 우선순위 up
		$('#btnUp').bind('click', function() {
			var selIndex = secondGrid.selectedDataIndexs;
			var selIndex = selIndex[0];
			var selItem = secondGrid.list[selIndex];
			if (selIndex === 0)
				return;

			// 그리드 포커스 이동시 index 0이면 가장아래로 가는 오류? 발생 하므로 HOME으로 이동
			var focusIn = selIndex - 1 === 0 ? 'HOME' : selIndex - 1;

			secondGrid.addRow(selItem, selIndex - 1, {
				focus : focusIn
			});
			secondGrid.removeRow(selIndex + 1);
		});

		// 우선순위 down
		$('#btnDown').bind('click', function() {
			var selIndex = secondGrid.selectedDataIndexs;
			var selIndex = selIndex[0];
			var selItem = secondGrid.list[selIndex];

			if (selIndex === secondGridData.length - 1)
				return;

			secondGrid.addRow(selItem, selIndex + 2, {
				focus : selIndex + 1
			});
			secondGrid.removeRow(selIndex);
		});

		// 적용
		$('#btnAdd').bind('click',function() {
			firstGridData = firstGrid.getList();
			secondGridData = secondGrid.getList();
			
			var jawonArray = new Array();
			var jawonInfo = new Object();
			for (i=0; i<firstGridData.length; i++) {
				jawonInfo = new Object();
				jawonInfo.CM_RSRCCD = firstGridData[i].cm_rsrccd;
				if (firstGridData[i].__selected__ == true) {
					jawonInfo.checkRsrc = 'Y';
				}else {
					jawonInfo.checkRsrc = 'N';
				}
				jawonArray.push(jawonInfo);
			}
			
			var langArray = new Array();
			var langInfo = new Object();
			for (i=0; i<secondGridData.length; i++) {
				langInfo = new Object();
				langInfo.CM_LANGCD = secondGridData[i].cm_micode;
				if (secondGridData[i].__selected__ == true) {
					langInfo.checkLang = 'Y';
				}else {
					langInfo.checkLang = 'N';
				}
				langArray.push(langInfo);
			}

			var data = new Object();
			data = {
				syscd : selectedSystem.cm_syscd,
				jawonInfo : jawonArray,
				langInfo : langArray,
				requestType : 'rangInfo_ins'
			}
			console.log('[rangInfo_ins] ==>', data);
			ajaxAsync('/webPage/ecmm/Cmm0200_ProgServlet', data, 'json', successInsRangInfo);
		});
		
		// 언어등록
		$('#btnLang').bind('click', function() {
			setTimeout(function() {
				langModal.open({
					width: 800,
					height: 650,
					iframe: {
						method: "get",
						url: "../../modal/sysinfo/LangModal.jsp"
					},
					onStateChanged: function () {
						if (this.state === "open") {
							mask.open();
						}
						else if (this.state === "close") {
							mask.close();
							getCboRsrc();
						}
					}
				}, function () {
				});
			}, 200);
		});
	}
}

function getCboRsrc() {
	var data = new Object();
	data = {
		SelMsg : "",
		requestType : 'getCboRsrc'
	}
	ajaxAsync('/webPage/ecmm/Cmm0023Servlet', data, 'json', successGetCboRsrc);
	data = null;
}

function successGetCboRsrc(data) {
	secondGridData = data;
	secondGrid.setData(secondGridData);
}

function getProgList(syscd) {
	var data = new Object();
	data = {
		SysCd : syscd,
		requestType : 'getProgList'
	}
	ajaxAsync('/webPage/ecmm/Cmm0200_ProgServlet', data, 'json', successGetProgList);
	data = null;
}

function successGetProgList(data) {
	firstGridData = data;
	firstGrid.setData(firstGridData);
}

function getChkLangInfo(syscd) {
	var data = new Object();
	data = {
		syscd : syscd,
		requestType : 'getChkLangInfo'
	}
	ajaxAsync('/webPage/ecmm/Cmm0200_ProgServlet', data, 'json',
			successGetChkLangInfo);
	data = null;
}

function successGetChkLangInfo(data) {
	chkLangData = data;
}

function clickFirstGrid(index) {
	var selItem = firstGrid.list[index];

	if (selItem == null || selItem == undefined)
		return;

	secondGrid.clearSelect();
	
	var focus = -1;
	// if(selItem.checkRsrc == 'Y') {
	for (var i=0; i<chkLangData.length; i++) {
		if (chkLangData[i].CM_RSRCCD == selItem.cm_rsrccd) {
			firstGrid.list[index].checkLang = 'Y';
			for (var j=0; j<secondGridData.length; j++) {
				if (chkLangData[i].CM_LANGCD == secondGridData[j].cm_micode) {
					focus = j;
					secondGridData[j].__selected__ = true;
					secondGridData[j].checkLang = 'Y';
				}
			}
		}
	}
	// }else {
	// getCboRsrc();
	// }
	
	console.log('focus='+focus);
	firstGrid.repaint();
	secondGrid.repaint();
	if(focus > -1) secondGrid.focus(focus);
}

function successInsRangInfo(data) {
	if(data != null && data != undefined) {
		if(typeof data == 'string' && data.indexOf('ERROR') > -1) {
			dialog.alert(data.substr(5));
			return;
		}
	}
	
	if(data == 'OK') {
		dialog.alert('등록되었습니다.');
	}else {
		dialog.alert('등록이 실패되었습니다.');
	}
}