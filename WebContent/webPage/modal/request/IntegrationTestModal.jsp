<!-- 
화면 명: 통합테스트 등록
화면호출:
1) 개발 -> 
 -->
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:import url="/webPage/common/common.jsp" />

<body style="width: 100% !important; min-width: 0px !important">
<div class="pop-header">
    <div>
        <label id="lbSub">통합테스트 등록</label>
    </div>
    <div>
        <button type="button" class="close" aria-label="닫기" onclick="popClose()">
            <span aria-hidden="true">&times;</span>
        </button>
    </div> 
</div>    
<div class="container-fluid pop_wrap">
    <div class="row">
        <div class="l_wrap dib vat" style="width:100%;">
            <div class="row">
                <div class="vat width-100">
                    <label class="dib" id="lbTest">&nbsp;&nbsp;테스트조건</label>
                </div>
            </div>
            <div class="row">
                <div class="vat width-100 write_wrap">
                    <textarea id="txtTest" name="txtTest" style="align-content:flex-start; width:100%; height:120px; resize: none; overflow-y:auto;"></textarea>                   
                </div>
            </div>

            <div class="row">
                <div class="vat width-100">
                    <label class="dib" id="lbCheckList">&nbsp;&nbsp;확인사항</label>
                </div>
            </div>
            <div class="row">
                <div class="vat width-100 write_wrap">
                    <textarea id="txtCheckList" name="txtCheckList" style="align-content:flex-start; width:100%; height:120px; resize: none; overflow-y:auto;"></textarea>                   
                </div>
            </div>            
            
            <div class="row">
                <div class="vat width-100">
                    <div class="width-100">
                        <button id="btnCancel" name="btnCancel" class="btn_basic_s r_wrap width-20" style="margin-left:5px;">취소</button>  
                        <button id="btnSave" name="btnSave" class="btn_basic_s r_wrap width-20">등록</button>
                    </div>
                </div>
            </div>			
            </div>              
        </div>
    </div>
</div>
</body>


<c:import url="/js/ecams/common/commonscript.jsp" />	
<script type="text/javascript" src="<c:url value="/js/ecams/modal/request/IntegrationTestModal.js"/>"></script>