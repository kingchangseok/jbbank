
var reqCd 		= window.parent.reqCd;			// 메뉴코드
var sysCd		= window.parent.SelSysCd; 			// 시스템코드
var firstGridData = window.parent.realFileData;

var firstGrid  	= new ax5.ui.grid();

var data          = null;							//json parameter
var tmpWord = "";
var selIn = null;

firstGrid.setConfig({
    target: $('[data-ax5grid="firstGrid"]'),
    sortable: true, 
    multiSort: true,
    multipleSelect : false,
    page : {
    	display:false
    },
    showLineNumber: true,
    header: {
        align: "center"
    },
    body: {
    	trStyleClass: function () {
    		if (this.item.colorsw === '3'){
    			return "fontStyle-cncl";
    		}
    	}
    },
    columns: [
        {key: "moddir", label: "바이너리디렉토리",  width: '20%', align: 'left'},
        {key: "modname", label: "바이너리프로그램",  width: '15%', align: 'left'},
        {key: "srid", label: "SR-ID",  width: '10%', align: 'left'},
        {key: "srtitle", label: "SR제목",  width: '20%', align: 'left'},
        {key: "acptdate", label: "체크인일시",  width: '5%', align: 'left'},
        {key: "editor", label: "개발자",  width: '5%', align: 'left'},
        {key: "status", label: "상태",  width: '5%', align: 'left'},
        {key: "srcdir", label: "소스디렉토리",  width: '10%', align: 'left'},
        {key: "rsrcname", label: "프로그램명",  width: '10%', align: 'left'}
    ]
});


$(document).ready(function() {
	$("#txt_tit").html("운영에 적용 할 대상 중 다른사용자 또는 다른 SR로 개발 후 적용하지 않은 기록이 있습니다. <br/>"
			+"목록을 확인 후 계속 진행하려면 확인버튼을 클릭하고 운영적용을 중단하려면 취소버튼을 클릭하여 주시기 바랍니다.");
	
	firstGrid.setData(firstGridData);
	
	$('#btnCnl').bind('click',function(){
		popClose();
	});

	$('#btnReg').bind('click',function(){
		window.parent.realFileCk = true;
		popClose();
	});
	
});

function popClose(){
	window.parent.realFileModal.close();
}




