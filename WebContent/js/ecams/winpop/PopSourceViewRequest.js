var pReqNo  = null;
var pItemId = null;
var pUserId = null;
var codeList = null;

var isAdmin 	   = false;

var tmpInfo = new Object();
var tmpInfoData = new Object();

var f = document.getSrcData;
var pReqNo = f.acptno.value;
var pItemId = f.itemid.value;
var cr_ver = f.cr_ver.value;
var rsrcname = f.rsrcname.value;
var pUserId = f.user.value;
var rsrccd = f.rsrccd.value;
var codeList = JSON.parse(replaceAllString(replaceAllString(f.codeList.value,'%','"'),'$',' '));

//$('[data-ax5select="cboCharacter"]').ax5select({
//    options: []
//});

$('input:radio[name^="optradio"]').wRadio({theme: 'circle-radial blue', selector: 'checkmark'});

$(document).ready(function(){

	if (pUserId == null || pUserId.length == 0) {
		dialog.alert('로그인 후 사용하시기 바랍니다.',function(){});
		return;		
	}
	if (pReqNo == null || pReqNo.length == 0) {
		dialog.alert('신청번호가 정확하지 않습니다. 확인 후 진행하시기 바랍니다.');
		return;
	}
	
//	getCodeInfo();
	
	//닫기클릭
	$('#btnClose').bind('click', function() {
		close();
	});
	
	$("#txtRsrcName").val(rsrcname);
	
	if(cr_ver != undefined && cr_ver != null && cr_ver.indexOf(".") > 0){
		version = cr_ver.split(".");
		getFileText(pUserId,pItemId,pReqNo,version[0],version[1],status);
	}
	
	$("#sourceCode").keydown(function(event){
		if(event.ctrlKey) {
			if(event.keyCode == 65 || event.keyCode==97) {
				$("#sourceCode").select();
			} else if (event.keyCode == 67){
				$("#sourceCode").select();
				document.execCommand("copy");
				dialog.alert("복사되었습니다.");
				setTimeout(function(){
					dialog.close();
				}, 500);
			}
		}
	});
	
});

function getFileText( userid, itemid, acptno, cr_befver,cr_devver, status){
	
//	console.log("SourceViewTab  param userid:",userid,",itemid:",itemid,",acptno:",acptno,",realver:",realver,",cr_devver:",cr_devver,",status:",status,",rsrcname:",rsrcname);
	
	tmpInfo = new Object();
	tmpInfo.userid = userid;
	tmpInfo.cr_itemid  = itemid;
	tmpInfo.cr_rsrcname  = rsrcname;
	tmpInfo.cr_rsrccd  = rsrccd;
	/*outName = userid + '_' + ver + '_' + rsrcname;*/
	outName = itemid + '.' + cr_befver + '.' + cr_devver;
	tmpInfo.outname = outName; 
//	tmpInfo.tmpdir = tmpDir;
	if ( acptno.substr(4,2) == "05" || acptno.substr(4,2) == "09" || acptno.substr(4,2) == "10" ) {
		tmpInfo.cr_acptno  = cr_befver;
		tmpInfo.gbncd = "CMR0025";
	} else if ( status == "9" ){
		tmpInfo.cr_acptno  = acptno;
		tmpInfo.gbncd = "CMR0025";
	} else {
		tmpInfo.cr_acptno  = acptno;
		tmpInfo.gbncd = "CMR0027";
	}
//	tmpInfo.encoding = encoding;
	tmpInfo.realver  = cr_befver;
	tmpInfo.cr_devver  = cr_devver;
	
//	console.log("SourceViewTab  getFileText  tmpInfo:",tmpInfo);
	var tmpInfoData = {
		tmpInfo		: tmpInfo,
		requestType: 	'GETFILETEXT'
	}
	
	ajaxAsync('/webPage/winpop/RequestSourceViewServlet', tmpInfoData, 'json', successFileText);
	
}


function successFileText(data) {
	var src = "";
	$(data).each(function(){
		src += this.src;
	});
	
	$("#sourceCode").val(src);
}