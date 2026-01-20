var projectGrid	= new ax5.ui.grid();

$('[data-ax5select="cboProject"]').ax5select({
	options: []
});

projectGrid.setConfig({
	target : $('[data-ax5grid="projectGrid"]'),
	showLineNumber : false,
	showRowSelector : false,
	multipleSelect : false,
	header : {
		align: "center"
	},
	body : {
		columnHeight: 24,
		onClick : function() {
			/*this.self.clearSelect();*/
			this.self.select(this.dindex);
			selectedItem = this.item;
		},
		onDBLClick: function () {
         	if (this.dindex < 0) return;
 			openWindow(1, this.item.qrycd2, this.item.acptno2,'');
         },
         trStyleClass: function () {
        	 if(this.item.colorsw === '5'){
      			return "fontStyle-error";
      		} else if (this.item.colorsw === '3'){
      			return "fontStyle-cncl";
      		} else if (this.item.colorsw === '0'){
      			return "fontStyle-ing";
      		}
      	},
 		onDataChanged : function() {
 			this.self.repaint();
 		}
	},
    contextMenu: {
        iconWidth: 20,
        acceleratorWidth: 100,
        itemClickAndClose: false,
        icons: {
            'arrow': '<i class="fa fa-caret-right"></i>'
        },
        items: [
            {type: 1, label: "상세조회"}
        ],
        popupFilter: function (item, param) {
        	projectGrid.clearSelect();
        	projectGrid.select(Number(param.dindex));
        	
        	var selIn = projectGrid.selectedDataIndexs;
         	if(selIn.length === 0) return;
         	 
          	if (param.item == undefined) return false;
          	if (param.dindex < 0) return false;
          	
 			strConfUser = "";
          	if (param.item.endyn != "0") {
          		//return item.type == 1;
     		} else if (param.item.cr_qrycd == "04" && (adminYN || param.item.editor2 == userId)) {
          		var ajaxData = {
      				AcptNo : param.item.acptno2,
      				requestType : "cnclYn"
          		}
          		strConfUser = ajaxCallWithJson('/webPage/ecmr/Cmr3200Servlet', ajaxData, 'json');
     	    } else {
     	    	return item.type == 1;
     	    }
        },
        onClick: function (item, param) {
   			openWindow(item.type, param.item.qrycd2, param.item.acptno2,'');
   			projectGrid.contextMenu.close();
        }
    },
	columns : [
		{key : "CR_CONN",	label : "등록",		width: "5%"}, 
		{key : "STA",		label : "상태",		width: "15%",	align: "center"	}, 
		{key : "DocName",	label : "산출물명",	width: "80%",	align: "left"	},
	]
});