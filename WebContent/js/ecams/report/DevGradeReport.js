var mainGrid = [new ax5.ui.grid(), new ax5.ui.grid(), new ax5.ui.grid()];

$(document).ready(function() {
	
	for(var i = 0; i <= 2; i++) {
		
		mainGrid[i].setConfig({
			target : $('[data-ax5grid="mainGrid' + (i+1) + '"]'),
			showLineNumber : false,
			showRowSelector : false,
			multipleSelect : false,
			lineNumberColumnWidth : 40,
			rowSelectorColumnWidth : 27,
			body : {
				columnHeight: 24,
		        onClick: function () {
		        	this.self.clearSelect();
		            this.self.select(this.dindex);
		        },
			},
		}); 
	}
	
	//조회
	$("#btnSearch").bind('click', function() {
		getColHeader();
		console.log("조회클릭")
		if($("#btnExcel").length < 1) {
			$("#btnBox").append('<button class="btn_basic_s dib" data-grid-control="excel-export" style="width: 70px; margin-left:5px;" id="btnExcel">엑셀저장</button>');
			console.log("조건문 발동");
		}
	});
	
	//엑셀저장
	$("#searchBar").on('click', '#btnExcel', function() {
		var st_date = new Date().toLocaleString();
		var today = st_date.substr(0, st_date.indexOf("오"));
		
		mainGrid[0].exportExcel("부서별 의뢰 현황 " + today + ".xls");
		mainGrid[1].exportExcel("업무 등급별 처리현황 " + today + ".xls");
		mainGrid[2].exportExcel("연구소실 별 처리현황 " + today + ".xls");
	})
	
});


//그리드 헤더 세팅
function getColHeader() {
	
	var columns = [[],[],[]];
	var ajaxData = new Object();
	var ajaxResult = [];
	
	ajaxData.requestType = "getColHeader";
	
	ajaxData.dv = "reqDept";
	ajaxResult.push(ajaxCallWithJson('/webPage/ecmp/Cmp6000Servlet', ajaxData, 'json'));
	ajaxData.dv = "rate";
	ajaxResult.push(ajaxCallWithJson('/webPage/ecmp/Cmp6000Servlet', ajaxData, 'json'));
	ajaxData.dv = "devDept";
	ajaxResult.push(ajaxCallWithJson('/webPage/ecmp/Cmp6000Servlet', ajaxData, 'json'));
	
	$.each(ajaxResult, function(i, value) {
		columns[i].push({key : "deptname", label : "구분", align : "center", width: "10%"});
		columns[i].push({key : "rowhap", label : "합계", align : "center", width: "5%", styleClass: "color-red"});
		$.each(value, function(j, value2) {
			columns[i].push({key : j < 9 ? "col0" + (j+1) : "col" + (j+1) , label : value2.codename, align: "center", width : 85 / value.length + "%"});
		})
		
		mainGrid[i].setConfig({
			target : $('[data-ax5grid="mainGrid' + (i+1) + '"]'),
			columns : columns[i]
		})
	});
	getRowList(columns);
}

//그리드 데이터 세팅
function getRowList(columns) {
	
	var ajaxData = new Object();
	var ajaxResult = [];
	var gridData = [[],[],[]];
	var month = $("#month").text().slice(0, -1);
	var year = $("#year").text().slice(0, -1);
	if(month.length <= 1) month = '0' + month;
		
	ajaxData.requestType = "getRowList";
	ajaxData.selMonth = year + month;
	
	ajaxData.dv = "reqDept";
	ajaxResult.push(ajaxCallWithJson('/webPage/ecmp/Cmp6000Servlet', ajaxData, 'json'));
	ajaxData.dv = "rate";
	ajaxResult.push(ajaxCallWithJson('/webPage/ecmp/Cmp6000Servlet', ajaxData, 'json'));
	ajaxData.dv = "devDept";
	ajaxResult.push(ajaxCallWithJson('/webPage/ecmp/Cmp6000Servlet', ajaxData, 'json'));
	
	//빈 데이터 0으로 채우기위한 그리드 데이터 세팅
	$.each(columns, function(i, col) {
		var rowData1 = { rowhap : ajaxResult[i][0].rowhap, deptname : ajaxResult[i][0].deptname };
		var rowData2 = { rowhap : ajaxResult[i][1].rowhap, deptname : ajaxResult[i][1].deptname };
		$.each(col, function(j, value) {
			if(j > 1) {			
				rowData1[value.key] = ajaxResult[i][0][value.key] == undefined ? 0 : ajaxResult[i][0][value.key];
				rowData2[value.key] = ajaxResult[i][1][value.key] == undefined ? 0 : ajaxResult[i][1][value.key];
			}
		});
		gridData[i].push(rowData1);
		gridData[i].push(rowData2);
	});
		
	for(var i = 0; i <= 2; i++) {		
		mainGrid[i].setData(gridData[i]);
	}
}

//날짜버튼 클릭 시
$(".dateBtn").bind('click', function() {
	var clicked = $(this).attr('id');
	dateSet(clicked);
});

//picker에 오늘 날짜 디폴트로 세팅
$(function() {
	var today = new Date().toISOString().substring(0,7);
	$("#year").text(today.split('-')[0] + "년");	
	$("#month").text(parseInt(today.split('-')[1]) + "월");	
})

function dateSet(value) {
	
	var month = parseInt($("#month").text().slice(0, -1));
	var year = parseInt($("#year").text().slice(0, -1));
	
	switch(value) {
	case 'year-prev' :
		year--;
		break;
	case 'year-next' :
		year++;
		break;
	case 'month-prev' :
		if(month == 1) {
			month = 12;
			year--;
		} else {
			month--; 			
		}
		break;
	case 'month-next' :
		if(month == 12) {
			month = 1;
			year++;
		} else {
			month++; 			
		}
		break;
	}
	$("#month").text(month + "월");
	$("#year").text(year + "년");
}