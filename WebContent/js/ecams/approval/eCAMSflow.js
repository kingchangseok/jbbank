var userId = window.top.userId;
var secuList = null;
var contentHeight = null;
var errCnt = null;

$(document).ready(function() {
//	$(".components > button").click(function(e) {
//		console.log("클릭!");
//		console.log(e);
//		if(e.target.value != null && e.target.value != "") {			
//			if(e.target.innerHTML == "운영적용") {
//				e.target.innerHTML = "운영배포";
//			}
//			window.top.clickSideMenu(e);
//		}
//	})
	prcCnt();
	getSecuList();
	
	$(".components > button").bind('click', function() {
		var wkLabel = '1';
		
		if (this.getAttribute("class") == "grayBtn") {
			if ($('#'+this.id).text().indexOf('(')<0) {
				wkLabel = '0';
			}
		}
		/*
		var wkLabel = Number($("#" + this.id + "State").text()) + Number($("#" + this.id + "Err").text());
		if(this.getAttribute("class") != "yellowBtn" && this.getAttribute("class") != "grayBtn" && this.getAttribute("rel") != "/webPage/approval/RequestStatus.jsp" ){
			wkLabel = "1";
		}*/
		extInterface_call($(this).attr("rel"), this.value, wkLabel, $(this).text());
	})
	
	$(".condDiv > button").bind('click', function() {
		var wkLabel = '1';
		
		if (this.getAttribute("class") == "grayBtn") {
			if ($('#'+this.id).text().indexOf('(')<0) {
				wkLabel = '0';
			}
		}
		/*
		var wkLabel = Number($("#" + this.id + "State").text()) + Number($("#" + this.id + "Err").text());
		if(this.getAttribute("class") != "yellowBtn" && this.getAttribute("class") != "grayBtn" && this.getAttribute("rel") != "/webPage/approval/RequestStatus.jsp" ){
			wkLabel = "1";
		}*/
		extInterface_call($(this).attr("rel"), this.value, wkLabel, $(this).text());
	})
});

function mouseover(btnname) {
	$('#'+btnname).css('color','white');
}
function mouseout(btnname) {
	
	var tmpVal = $('#'+btnname).text();

	if (tmpVal.indexOf('[')>0) {
		$('#'+btnname).css('color','blue');
	} else {
		$('#'+btnname).css('color','black');
	}
}
//PrjInfo.getPrccnt(strUserId);
function prcCnt() {
	var ajaxData = {
		     UserId : userId,
		requestType : "getFlowCnts"
	}

	console.log('getFlowCnts',ajaxData);
	ajaxAsync('/webPage/common/PrjInfoServlet', ajaxData, 'json',successGetPrcCnt);
}

function successGetPrcCnt(data) {
	var list = []
	list = data;
	
	//Alert.show("prccnt_resultHandler");
	var jobCnt = 0;
	var er07Cnt = 0;
	var tot07Cnt = 0;
	var tot03Cnt = 0;
	var editCnt = 0;
	var gyulCnt = 0;
	var tmpCD = "";
	
//	screenInit();
	if (list.length != 0){
		for (var i=0;list.length>i;i++) {
			//건수있음
			if (list[i].count != "0") {
				tmpCD = list[i].qrycd;
				if (tmpCD == "ReqCnt") {
					$("#txtPum1").text('개발요청 담당['+ list[i].count +']건');
					$('#cmdPum1').css('color','blue');
				}  else if (tmpCD == "OrderCnt") {
					$("#txtPum2").text('업무지시서 담당['+ list[i].count +']건');
					$('#cmdPum2').css('color','blue');
				}  else if (tmpCD == "chkInReqCnt") {
					$("#txtTeam3").text('운영적용요청['+ list[i].count +']건');
					$('#cmdTeam3').css('color','blue');
				}  else if (tmpCD == "chkOutReqCnt") {
					$("#cmdTeam1").text('대여 요청['+ list[i].count +']건');
					$('#cmdTeam1').css('color','blue');
				} else if (tmpCD == "HandlerCnt") {
					$("#cmdGyul1").text('담당책임자 승인['+ list[i].count +']건');
					$('#cmdGyul1').css('color','blue');	
				} else if (tmpCD == "DochkoutCnt") {
					$("#txtProg1").text('대여 수행['+ list[i].count +']본 수');
					$('#cmdProg1').css('color','blue');
				} else if (tmpCD == "BubuJangCnt") {
					$("#cmdGyul4").text('부부장['+ list[i].count +']/부장 결재 건');
					$('#cmdGyul4').css('color','blue');
				} else if (tmpCD == "BuJangCnt") {
					$("#cmdGyul4").text('부부장/부장['+ list[i].count +']결재 건');
					$('#cmdGyul4').css('color','blue');
				} else if (tmpCD == "QACnt") {
					$("#cmdPum3").text('품질관리 사전검토['+ list[i].count +']건');
					$('#cmdPum3').css('color','blue');
				} else if (tmpCD == "ThirdCnt") {
					$("#cmdGyul2").text('제3자 확인['+ list[i].count +']건');
					$('#cmdGyul2').css('color','blue');
				} else if (tmpCD == "HandlerDeptCnt") {
					$("#cmdGyul3").text('주관부서 확인['+ list[i].count +']건');
					$('#cmdGyul3').css('color','blue');
				} else if (tmpCD == "CompileCnt") {
					$("#txtProg2").text('소스적용 및 컴파일['+ list[i].count +']건');
					$('#cmdProg2').css('color','blue');
				} else if (tmpCD == "RunConfCnt") {
					$("#txtProg4").text('배포(시스템적용) ['+ list[i].count +']건');
					$('#cmdProg4').css('color','blue');
				} else if (tmpCD == "CompleteCnt") {
					$("#cmdPum4").text('개발 요청 완료['+ list[i].count +']본 수');
					$('#cmdPum4').css('color','blue');
				}
			}
		}
	}
}


function extInterface_call(callJsp, reqCd, wkLabel, btnText) {
    var findSw = false;
    var chkJsp = callJsp;
    
    console.log('extInterface_call='+callJsp+','+reqCd+','+wkLabel+','+btnText);
	for (var i = 0; secuList.length > i; i++) {
		if (secuList[i].link == chkJsp) {
			if (reqCd != "" && reqCd != null && reqCd != undefined) {
				if (secuList[i].reqcd != null && secuList[i].reqcd != '' && secuList[i].reqcd != undefined) {
					if (secuList[i].reqcd == reqCd) findSw = true;
				} else findSw = true;
			} else {
				findSw = true;
			}
			if (findSw) {
				window.top.changeScreen("<strong>"+secuList[i].text+"</strong>",callJsp,reqCd);
				return;
			}
		}
	}
	if (!findSw) dialog.alert("클릭하신 화면에 대한 권한이 없습니다. 권한이 필요한 경우 형상관리시스템 담당자에게 연락하시기 바랍니다.");	
}

function getSecuList() {
	var ajaxData = {
		userId : userId,
		requestType : "MenuList"
	}
	secuList = ajaxCallWithJson('/webPage/main/eCAMSBaseServlet', ajaxData, 'json');
	console.log('secuList', secuList);
}

function changeScreen() {
	contentHeight = window.innerHeight - $('#topMenu').height() - 25;
	
	$('#eCAMSFrame').empty();
	$iFrm = $('<IFRAME id="iFrm" frameBorder="0" name="iFrm" scrolling="yes" src="'+pathName+'" style="width:100%; height:'+contentHeight+'px; min-width:1004px;" marginwidth="0" marginheight="0"  onload="frameLoad()"></IFRAME>');
	$iFrm.appendTo('#eCAMSFrame');
}

var title_ = null;
var class_ = null;
//오류건 툴팁
$(document).on("mouseover",".err",function(event){
	var selectTxt = "";
	var iconTop = 11;
	
//	selectTxt = "오류 : " + $(event.target).attr("value") + "건";
	selectTxt = "첫번째 괄호숫자는 진행 중 총 건수\n두번째 괄호숫자는 오류건수";
	$(this).attr("title",selectTxt);
	
	// title을 변수에 저장
	title_ = $(this).attr("title");
	// class를 변수에 저장
	class_ = $(this).attr("class");
	// title 속성 삭제 ( 기본 툴팁 기능 방지 )
	$(this).attr("title","");
	
	$("body").append("<div id='tip'></div>");
	if (class_ == "img") {
		$("#tip").html(imgTag);
		$("#tip").css("width","100px");
	} else {
		$("#tip").css("display","inline-block");
		$("#tip").html('<pre>'+htmlFilter(title_)+'</pre>');
	}
	
	var pageX = event.pageX;
	var pageY = event.pageY;
	
	$("#tip").css({left : (pageX + 15) + "px", top : pageY + "px", "z-index" : "10"}).fadeIn(100);
	return;
	
}).on('mouseout',".err",function(){
	$(this).attr("title", '');
	$("#tip").remove();	
});

