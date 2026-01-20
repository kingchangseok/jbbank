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
		     UserID : userId,
		requestType : "getPrccnt"
	}

	console.log('getPrccnt',ajaxData);
	ajaxAsync('/webPage/common/PrjInfoServlet', ajaxData, 'json',successGetPrcCnt);
}

function successGetPrcCnt(data) {
	var cmr0100_dp = data;
	var tmpCd = '';
	var tmpCnt = '';
	var tmpText = '';
	var cnt1  = 0;
	var cnt2  = 0;
	var cnt3  = 0;
	var cnt4  = 0;
	var cnt5  = 0;
	var cnt6  = 0;
	var cnt7  = 0;
	var cnt8  = 0;
	var ckOutCnt = 0;
	var ckCnclCnt = 0;
	var ckOutErrCnt = 0;
	var ckInTestCnt = 0;
	var ckInRealCnt = 0;
	var rbCnt = 0;
	var ckInErrCnt = 0;
	var editCnt = 0;
	var err01Cnt = 0;
	var err04Cnt = 0;
	var ingCnt = 0;

	console.log('successGetPrcCnt',data);
	
	if(cmr0100_dp.length != 0) {
		for(var i=0; i<cmr0100_dp.length; i++) {
			tmpCd = cmr0100_dp[i].CD;
			tmpCnt = Number(cmr0100_dp[i].cnt);
			if (tmpCnt>0) {
				if (tmpCd == '01') ckOutCnt = tmpCnt;
				else if (tmpCd == '11') ckCnclCnt = tmpCnt;
				else if (tmpCd == 'E01') ckOutErrCnt = tmpCnt;
				else if (tmpCd == '03') ckInTestCnt = tmpCnt;
				else if (tmpCd == '04') ckInRealCnt = tmpCnt;
				else if (tmpCd == '06') rbCnt = tmpCnt;
				else if (tmpCd == 'ER') ckInErrCnt = tmpCnt;
				else if (tmpCd == 'T03') cnt5 = tmpCnt;
				else if (tmpCd == 'T0') cnt1 = tmpCnt;
				else if (tmpCd == 'T1') cnt2 = tmpCnt;
				else if (tmpCd == 'R0') cnt3 = tmpCnt;
				else if (tmpCd == 'R1') cnt4 = tmpCnt;
				else if (tmpCd == 'T04') cnt6 = tmpCnt;
				else if (tmpCd == 'S1') cnt7 = tmpCnt;
				else if (tmpCd == 'S2') cnt8 = tmpCnt;
			}
		}
		
		editCnt = 0;		
		if (ckOutCnt>0) {
			++editCnt;
			$('#txtReq0'+editCnt).text('체크아웃 : '+ ckCnclCnt + '건');
		}
		if (ckCnclCnt>0) {
			++editCnt;
			$('#txtReq0'+editCnt).text('체크아웃취소 : '+ ckCnclCnt + '건');
		}
		if (ckOutErrCnt>0) {
			++editCnt;
			$('#txtReq0'+editCnt).text('오류대기 : '+ ckOutErrCnt + '건');
			$('#txtReq0'+editCnt).css('color','#FF00FF');
		}
		editCnt = 0;		
		if (ckInTestCnt>0) {
			++editCnt;
			$('#txtReq1'+editCnt).text('체크인(테스트)요청 : '+ ckInTestCnt + '건');
		}
		if (ckInRealCnt>0) {
			++editCnt;
			$('#txtReq1'+editCnt).text('체크인(운영)요청 : '+ ckInRealCnt + '건');
		}
		if (rbCnt>0) {
			++editCnt;
			$('#txtReq1'+editCnt).text('Roll-Back요청 : '+ rbCnt + '건');
			$('#txtReq1'+editCnt).css('color','#FF00FF');
		}
		if (ckInErrCnt>0) {
			++editCnt;
			$('#txtReq1'+editCnt).text('오류대기 : '+ ckInErrCnt + '건');
			$('#txtReq1'+editCnt).css('color','#FF00FF');
		}
		if ((cnt7+cnt8)>0) {
			$("#cmdConf").text('결재절차진행['+(cnt7+cnt8)+']');
			$('#cmdConf').css('color','blue');
		}
		editCnt = 0;
		if ((cnt1+cnt3)>0) {
			++editCnt;
			$('#txtReq2'+editCnt).text('신규 : '+ (cnt1+cnt3) + '건');
		}
		if ((cnt2+cnt5+cnt6)>0) {
			++editCnt;
			$('#txtReq2'+editCnt).text('수정 : '+ tmpCnt + '건');
		}
		if ((cnt2+cnt3)>0) {
			$("#cmdCkCncl").text('체크아웃취소['+(cnt2+cnt3)+']');
			$('#cmdCkCncl').css('color','blue');
		}
		if ((cnt1+cnt2+cnt4+cnt5+cnt6)>0) {
			$("#cmdTest").text('체크인(테스트)['+(cnt1+cnt2+cnt4+cnt5+cnt6)+']');
			$('#cmdTest').css('color','blue');
		}
		if ((cnt3+cnt4+cnt5+cnt6)>0) {
			$("#cmdReal").text('체크인(운영)['+(cnt3+cnt4+cnt5+cnt6)+']');
			$('#cmdReal').css('color','blue');
		}
	}
}

function extInterface_call(callJsp, reqCd, wkLabel, btnText) {
    var findSw = false;
    var chkJsp = callJsp;
    
    console.log('extInterface_call='+callJsp+','+reqCd+','+wkLabel+','+btnText);
	for (var i = 0; secuList.length > i; i++) {
		if (secuList[i].cm_htmlfile == chkJsp) {
			if (reqCd != "" && reqCd != null && reqCd != undefined) {
				if (secuList[i].cm_reqcd != null && secuList[i].cm_reqcd != '' && secuList[i].cm_reqcd != undefined) {
					if (secuList[i].cm_reqcd == reqCd) findSw = true;
				} else findSw = true;
			} else {
				findSw = true;
			}
			if (findSw) {
				window.top.changeScreen(secuList[i].topname +"<strong> &gt; "+secuList[i].cm_maname+"</strong>",callJsp,reqCd);
				return;
			}
		}
	}
	if (!findSw) dialog.alert("클릭하신 화면에 대한 권한이 없습니다. 권한이 필요한 경우 형상관리시스템 담당자에게 연락하시기 바랍니다.");	
}

function getSecuList() {
	var ajaxData = {
		  Sv_UserID : userId,
		requestType : "getSecuList"
	}
	console.log('getSecuList',ajaxData)
	secuList = ajaxCallWithJson('/webPage/common/MenuListServlet', ajaxData, 'json');
	console.log('getSecuList return',secuList)
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
$(document).on("mouseover",".errCnt",function(event){
	var selectTxt = "";
	var iconTop = 11;
	
	selectTxt = "오류 : " + $(event.target).attr("value") + "건";
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
	
}).on('mouseout',".errCnt",function(){
	$(this).attr("title", '');
	$("#tip").remove();	
});