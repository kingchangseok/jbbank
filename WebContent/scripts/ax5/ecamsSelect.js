
var ecamsSelGroupId = [];
var ecamsSelGroupUi = [];
var ecamsSelUi = {
	addSel : function(id, sel){
		if(ecamsSelGroupId.indexOf(id) != -1){
			ecamsSelGroupUi.splice(ecamsSelGroupId.indexOf(id),1,sel);
		} else {
			ecamsSelGroupId.push(id);
			ecamsSelGroupUi.push(sel);
		}

		return sel;
	}
};
$.fn.ax5select = function(a,b,c) {
	var $self = (ecamsSelGroupId.indexOf(this.attr("id")) > -1? ecamsSelGroupUi[ecamsSelGroupId.indexOf(this.attr("id"))] : new Object());
	if(typeof a == 'object') {
		if(this[0] == undefined){
			return;
		}

		$self = new Object();
		$jQeuryThis = $(this);
		$self.allOptions = a.options,
		$self.selOption = null,
		$self.showOptions = $self.allOptions,
		$self.inputYN = false,
		$self.target = $jQeuryThis,
		$self.dataConfig = ax5.util.parseJson($jQeuryThis.attr('data-ax5select-config'), true),
		$self.selSize = $self.dataConfig.selSize == undefined ? 20:$self.dataConfig.selSize,
		$self.selName = $jQeuryThis.attr('id');
		var options = a.options;
		var selName = $jQeuryThis.attr('id');
		$jQeuryThis.attr("name", selName);

		var selStr = [];
		selStr.push('<span class="ecamsSel-icon" id="ecamsSelIcon'+selName+'"></span><input class="selSearch" readonly="readonly" id="ecamsSelText'+selName+'"  autocomplete="off" />');
		selStr.push('<ul id="ecamsSel'+selName+'" class="ax5select-default">');

		if(options == undefined || options.length == 0){
			selStr.push('<li class="item show">no options</li>');
		}

		if(options != undefined ){

			for(var i=0; i<options.length; i++) {
				selStr.push('<li class="item show'+(i == 0? " selected":"")+'" id="ecamsSel_'+selName+'_'+i+'" index="'+i+ '">'+options[i].text+'</li>');

				if(i==0){
					$self.selOption = options[i];
					selStr[0] = selStr[0].replace('selSearch"', 'selSearch" value="' + options[i].text + '"');
				}

			}
		}
		selStr.push('</ul>');
		this[0].innerHTML = selStr.join("");

		$jQeuryThis.unbind("mousedown");
		$jQeuryThis.bind("mousedown", function(e){
			// selectBox 열기

			var $icon = $(this).children("span.ecamsSel-icon");
			var $ul = $(this).children("ul.ax5select-default");

			var input = null;
			// input 과 span(화살표) 가 아니라면 동작 안함.;
			if(e.target.tagName != "INPUT" && e.target.tagName != "SPAN" ){
				return false;
			}

			if($(this).hasClass("disabled")){

				if(e.target.tagName == "INPUT"){
					input = e.target;
				}else if (e.target.tagName == "SPAN"){
					input = $(e.target).siblings("input");
				}
				setTimeout(function(){
					input.blur();
				},1);
				$icon.removeClass("close");
				$ul.hide();
				return;
			}
			var allOptions = $self.allOptions;

			if(e.target.tagName == "INPUT"){
				input = e.target;
				if($(input).prop("readonly") == true){
					if( $self.dataConfig.selecteditable == true ){
						$(input).removeAttr("readonly");
						setTimeout(function(){
							input.blur();
							input.focus();
						},1);
					} else {
						setTimeout(function(){
							input.blur();
							$icon.attr("tabIndex",0);
							$icon.focus();
						},1);
						if($ul.css("display") != "none"){
							$icon.removeClass("close");
							$ul.hide();
							return false;
						}
					}
				} else if($self.dataConfig.selecteditable == true){
					return;
				}
			} else if (e.target.tagName == "SPAN"){
				input = $(e.target).siblings("input");
				if(input.prop("readonly") == true){

					if( $self.dataConfig.selecteditable == true ){
						input.removeAttr("readonly");

						setTimeout(function(){
							input.blur();
							input.focus();
						},1);
					} else {
						setTimeout(function(){
							input.blur();
							$icon.attr("tabIndex",0);
							$icon.focus();
						},1);
						if($ul.css("display") != "none"){
							$icon.removeClass("close");
							$ul.hide();
							return false;
						}
					}
				} else {
					input.attr("readonly","readonly");
					$icon.removeClass("close");
					$ul.hide();
					return false;
				}
			}

			$icon.addClass("close");

			var ulHeight = "auto";
			var ulShowHeight = 0;
			var scrollHeight = 0;
			if(allOptions.length > $self.selSize) {
				ulHeight = ($self.selSize*20)+"px";
				ulShowHeight = $self.selSize*20;
				$ul.addClass("over");
				if(allOptions.indexOf($self.selOption) > $self.selSize){
					scrollHeight = ((allOptions.indexOf($self.selOption) - (Math.trunc($self.selSize/2) -1)) * 20);
				}
			} else {
				ulShowHeight = 20 * allOptions.length;
			}

			$ul.css("height",ulHeight);

			if( $(this).offset().top + $(this).height() + ulShowHeight > $(document).innerHeight()){
				$ul.css({top:"auto", bottom:"25px"});

				// 창 사이즈 보다 셀렉트박스 길이가 길경우
				if( ulShowHeight > $(this).offset().top - 5 ){
					ulShowHeight = $(this).offset().top - 5;
					ulHeight = ulShowHeight;
					scrollHeight = ((allOptions.indexOf($self.selOption)) * 20);
					$ul.addClass("over");
					$ul.css("height",ulHeight);
				}
			}

			$ul.css("display", "block");

			$ul.find("li:not(.show)").addClass("show");
			$ul.find("li.focus").removeClass("focus");
			if(scrollHeight >= 0){
				$ul.scrollTop(scrollHeight);
			}
		});

		$("#ecamsSelText"+selName + ", #ecamsSelIcon"+selName).unbind("keydown");
		$("#ecamsSelText"+selName + ", #ecamsSelIcon"+selName).bind("keydown", function(e){
			var $this = this;
			var $ul = $($this).siblings("ul");
			var thisId = "ecamsSel_"+$self.selName;

			// 아래방향키
			if(e.keyCode == 40){
				var focus = $ul.find("li.show.focus");
				var next = null;
				if(focus.length == 1){
					next = focus.nextAll(".show:first");
				} else {
					next = $ul.find("li.show.selected");
					if(next.length == 0){
						next = $ul.find(".show:first");
					} else {
						// selected 가 범위내에 있는지 체크 ( 화면에 보이는지 )
						if(next.offset().top - $ul.offset().top < $ul.height()){
							next = next.nextAll(".show:first");
						} else {
							next = next.prevAll(".show:last");
						}
					}
				}
				if(next.length  == 1){
					$ul.find("li.focus").removeClass("focus");
					next.addClass("focus");

					if(next.offset().top+20 - $ul.offset().top > $ul.height()){
						$ul.scrollTop($ul.scrollTop()+20);
					}
				}
				return false;
			}
			// 위 방향키
			if(e.keyCode == 38){

				var focus = $ul.find("li.show.focus");
				var prev = null;
				if(focus.length == 1){
					prev = focus.prevAll(".show:first");
				} else {
					prev = $ul.find("li.show.selected");
					if(prev.length == 0){
						prev = $ul.find(".show:first");
					} else {
						// selected 가 범위내에 있는지 체크 ( 화면에 보이는지 )
						if(prev.offset().top - $ul.offset().top < $ul.height()){
							prev = prev.prevAll(".show:first");
						} else {
							prev = prev.prevAll(".show:last");
						}
					}
				}
				if(prev.length  == 1){
					$ul.find("li.focus").removeClass("focus");
					prev.addClass("focus");

					if(prev.offset().top - $ul.offset().top < 0){
						$ul.scrollTop($ul.scrollTop()-20);
					}
				}
				return false;
			}
		});

		var keyupTime = null;
		$("#ecamsSelText"+selName + ", #ecamsSelIcon"+selName).unbind("keyup");
		$("#ecamsSelText"+selName + ", #ecamsSelIcon"+selName).bind("keyup", function(e){
			// 아래방향키
			if(e.keyCode == 40){
				return false;
			}
			// 위 방향키
			if(e.keyCode == 38){
				return false;
			}


			var thisval = $(this).val();
			var optionsFilter = [];
			var $this = this;
			var $ul = $($this).siblings("ul");
			var thisId = "ecamsSel_"+$self.selName;

			// 엔터키
			if(e.keyCode == 13){
				var focus = $ul.find("li.focus");
				if(focus.length == 1){
					focus.trigger("click");
					return false;
				}
			}

			if($self.dataConfig.selecteditable != true){
				return false;
			}

			clearTimeout(keyupTime);

			keyupTime = setTimeout(function(){
				$ul.find("li.focus").removeClass("focus");
				$ul.children().removeClass("show");
				var index = 0;

				$($self.allOptions).each(function(i){
					if((this.text).toUpperCase().indexOf(thisval.toUpperCase()) > -1){
						$("#"+thisId+"_"+i).addClass("show");
						optionsFilter.push(this);
					}
				});

				var size = $self.selSize;
				if(optionsFilter.length < $self.selSize){
					size = optionsFilter.length;
				}
				$ul.css("height",(optionsFilter.length == 0 ?"1px":(optionsFilter.length > $self.selSize ? 20*$self.selSize:"auto"))).removeClass("over");
				if(optionsFilter.length > $self.selSize){
					$ul.addClass("over");
				}
			}, 100);

		});

		$("#ecamsSel"+selName).unbind("click");
		$("#ecamsSel"+selName).bind("click", function(e){
			if(e.target.tagName == "LI"){
				var index = e.target.getAttribute("index");
				if(index == undefined ){
					return;
				}

				var input = $(this).siblings("input");
				$(this).find("li.selected").removeClass("selected");
				$self.selOption = $self.showOptions[index];
				e.target.className += " selected";
				input.val(e.target.innerText).prop("readonly", "readonly");
				input.blur();
				$(this).siblings("span").removeClass("close");
				$(this).hide();
				$("#"+$self.selName).trigger("change");
			}

		});

		$(document).off('mousedown.ecamsSelect');
		$(document).on('mousedown.ecamsSelect', function(e) {
			var selBoxs = $(".ax5select-default");
			for(var i=0; i<selBoxs.length; i++) {
				var selBoxParent = $(selBoxs[i]).parent();
				var selID = selBoxParent.attr('id');
				if($(selBoxs[i]).css("display") != "none" && selID != $(e.target).parent().attr('id')) {
					var selBoxChildren = selBoxParent.children();
					var iconEl = selBoxChildren[0];
					$(iconEl).removeClass("close");

					var thisSelGroup = ecamsSelGroupUi[ecamsSelGroupId.indexOf(selID)];
					if(thisSelGroup.dataConfig.selecteditable == true && thisSelGroup.selOption != null){
						var inEl = selBoxChildren[1];
						$(inEl).prop("readonly", "readonly").val(thisSelGroup.selOption.text);
					}
					$(selBoxs[i]).hide();
				}
			}
		});
		return ecamsSelUi.addSel($self.selName, $self);
	}

	if(typeof a == 'string' && a == 'setValue') {
		if($self.allOptions == undefined){
			return false;
		}

		var selOption = null;
		$self.allOptions.forEach(function(item){
			if(item.value == b){
				selOption = item;
				return false;
			}
		});

		if(selOption != null){
			$self.selOption = selOption;
			$(this).find("li.selected").removeClass("selected");
			$(this).find("li:eq("+$self.allOptions.indexOf($self.selOption)+")").addClass("selected");
			$(this).children("input").val($self.selOption.text);
			return ecamsSelUi.addSel($self.selName, $self);
		}

		return this;
	}

	if(typeof a == 'string' && a == 'enable') {
		this.removeClass("disabled").removeAttr("disabled");
		return this;
	}

	if(typeof a == 'string' && a == 'disable') {
		if(!this.hasClass("disabled")){
			this.addClass("disabled").attr("disabled","disabled");
		}
	}

	if(typeof a == 'string' && a == 'getValue') {
		return $self.selOption;
	}

	if(typeof a == 'string' && a == 'getIndex') {
		if($self.allOptions == undefined){
			return -1;
		}
		return $self.allOptions.indexOf($self.selOption);
	}

	if(typeof a == 'string' && a == 'open') {
		var inEl = $($self.target).children()[1];
		var iconEl = $($self.target).children()[0];
		var ulEl = $($self.target).children()[2];
		$(iconEl).addClass("close");
		$(inEl).removeAttr("readonly");
		$(ulEl).show();
	}

	if(typeof a == 'string' && a == 'close') {
		var inEl = $($self.target).children()[1];
		var iconEl = $($self.target).children()[0];
		var ulEl = $($self.target).children()[2];
		$(iconEl).removeClass("close");
		$(inEl).prop("readonly", "readonly");
		$(ulEl).hide();
	}

	return this;
};
/*
// common.js 에 수정
function getSelectedIndex(id) {
	return $('[data-ax5select="'+id+'"]').ax5select("getIndex");
};

function getSelectedVal(id) {
	var value = $('[data-ax5select="'+id+'"]').ax5select("getValue");
	if (value == undefined) return null;
	else return value;
};

*/
