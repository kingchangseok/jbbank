
$(document).ready(function() {
	$(".components > button").click(function(e) {
		console.log("클릭!");
		console.log(e);
		if(e.target.value != null && e.target.value != "") {			
			if(e.target.innerHTML == "운영적용") {
				e.target.innerHTML = "운영배포";
			}
			window.top.clickSideMenu(e);
		}
	})
});