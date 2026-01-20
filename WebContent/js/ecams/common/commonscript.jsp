<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!-- Toast UI Chart -->
<!-- 사용안함 주석
<script src="<c:url value="/scripts/tui-code-snippet.min.js"/>" type="text/javascript"></script>
<script src="<c:url value="/scripts/tui-chart-all.min.js"/>" type="text/javascript"></script>
--!>

<!-- Vendor scripts -->
<script src="<c:url value="/scripts/jquery-3.3.1.min.js"/>" type="text/javascript"></script>

<!-- full calendar -->
<!-- 사용안함 주석
<script src="<c:url value="/scripts/fullcalendar/core/main.js"/>"></script>
<script src="<c:url value="/scripts/fullcalendar/moment/main.js"/>"></script>
<script src="<c:url value="/scripts/fullcalendar/interaction/main.js"/>"></script>
<script src="<c:url value="/scripts/fullcalendar/daygrid/main.js"/>"></script>
<script src="<c:url value="/scripts/fullcalendar/timegrid/main.js"/>"></script>
<script src="<c:url value="/scripts/fullcalendar/list/main.js"/>"></script>
<script src="<c:url value="/scripts/fullcalendar/rrule/main.js"/>"></script>
<script src="<c:url value="/scripts/fullcalendar/rrule.js"/>"></script>
<script src="<c:url value="/scripts/fullcalendar/locales-all.js"/>"></script>
 --!>
<script src="<c:url value="/vendor/jquery-flot/jquery.flot.js"/>"></script>
<script src="<c:url value="/vendor/nestable/jquery.nestable.js"/>"></script>
<script src="<c:url value="/vendor/wCheck-master/wCheck.js"/>"></script>

<!-- 사용안함 주석
<script src="<c:url value="/vendor/metisMenu/dist/metisMenu.min.js"/>"></script>
<script src="<c:url value="/scripts/sweetalert.min.js"/>"></script>
 --!>

<!-- ax5ui script -->
<script src="<c:url value="/scripts/ax5/ax5core.min.js"/>" type="text/javascript"></script>
<script src="<c:url value="/scripts/ax5/ax5calendar.min.js"/>" type="text/javascript"></script>
<script src="<c:url value="/scripts/ax5/ax5picker.min.js"/>" type="text/javascript"></script>
<script src="<c:url value="/scripts/ax5/ax5formatter.min.js"/>" type="text/javascript"></script>
<%-- <script src="<c:url value="/scripts/ax5/ax5select.min.js"/>" type="text/javascript"></script> --%>
<script src="<c:url value="/scripts/ax5/ax5menu.min.js"/>" type="text/javascript"></script>
<script src="<c:url value="/scripts/ax5/ax5grid.min.js"/>" type="text/javascript"></script>
<script src="<c:url value="/scripts/ax5/ax5toast.min.js"/>" type="text/javascript"></script>
<script src="<c:url value="/scripts/ax5/ax5mask.min.js"/>" type="text/javascript"></script>
<script src="<c:url value="/scripts/ax5/ax5modal.js"/>" type="text/javascript"></script>
<script src="<c:url value="/scripts/ax5/ax5dialog.js"/>" type="text/javascript"></script>
<script src="<c:url value="/scripts/ax5/ax5uploader.js"/>" type="text/javascript"></script>
<script src="<c:url value="/scripts/ax5/ax5binder.min.js"/>" type="text/javascript"></script>

<!-- fileupload script -->
<!-- 파일 업로드, 공지사항에서만 사용해 해당 페이지(FileDownloadModalNew.jsp, FileUpModal.jsp) 직접 추가
<script src="<c:url value="/scripts/fileupload/jquery.dm-uploader.min.js"/>"></script>
<script src="<c:url value="/scripts/fileupload/demo-ui.js"/>"></script>
--!>

<!-- FILE TREE -->
<script src="<c:url value="/scripts/filetree/jquery.ztree.all.js"/>"></script>
<script src="<c:url value="/scripts/filetree/jquery.ztree.exhide.js"/>"></script>
<!-- jquery.ztree.all.js 에 있음 주석처리
<script src="<c:url value="/scripts/filetree/jquery.ztree.core.js"/>"></script>
<script src="<c:url value="/scripts/filetree/jquery.ztree.excheck.js"/>"></script>
<script src="<c:url value="/scripts/filetree/jquery.ztree.exedit.js"/>"></script>
 --!>

<!-- time picker -->
<script src="<c:url value="/scripts/jquery.timepicker.js"/>"></script>

<!-- ResizeSensor  -->
<!--
사용 하는 페이지가 1개(PopSourceDiffView.js)라 페이지 직접 추가
<script src="<c:url value="/scripts/ResizeSensor/ResizeSensor.js"/>"></script>
--!>

<!-- ie에서 Promise함수 구현 -->
<script src="<c:url value="/scripts/bluebird/bluebird.core.min.js"/>" type="text/javascript"></script>
<script src="<c:url value="/scripts/bluebird/bluebird.min.js"/>" type="text/javascript"></script> 


<!-- <script src="<c:url value="/scripts/ecams.All.min.js"/>"></script> -->
<script src="<c:url value="/scripts/ax5/ecamsSelect.js"/>" type="text/javascript"></script>
<script src="<c:url value="/scripts/ax5/ax5grid.js"/>" type="text/javascript"></script>

 <script type="text/javascript">

 	/* dev temp 2020.08.28 jkheo mouse right click, F12 Usable
 	$(document).ready(function(){
 		$(document).bind("keydown", function(e){
 			if(e.keyCode === 123){
 				e.preventDefault();
 				e.returnValue = false;
 			}
 		});
 	});

 	document.oncontextmenu = function(e){
 		return false;
 	}
	*/

	//Popup Loading Image
	if(document.URL.indexOf("winpop") > 0){
		var Page_loading_div = "<div class='loding-Frame' style='width: 100%; height: 100%;'><div class='loding-Frame-img'><img alt='' src='/img/loading.gif' style='width: 64px;'></div></div>";
		$("html").append(Page_loading_div);

		window.onload = function(){

			setTimeout(function(){
				$(".loding-Frame").remove();
			}, 1000);

		}
	}
</script>
<script src="<c:url value="/js/ecams/common/common.js" />" type="text/javascript"></script>
