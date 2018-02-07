var adresse = "";
var currentTree="";
	 
window.onload = function(){
			
	treeviewDefaut();
	StyleMenuActiv();
	updateListeData("/data/input/file",'#inputData');
	updateListeData("/data/clusterized",'#clusterizedData');
}


$( "#treeview1" ).click(function(ev){
	ev.target.ondblclick= function(event){
		VisualizeClusterizeDataNode(event.target.textContent);
	};	
})

function SaveEditor(){
	var file = document.getElementById("myOccurence").value;
	alert(file)
	return file;
}
	 
function StyleMenuActiv(){			
	$("#datamenu").addClass("active");
}
	 
function collapseTreeview(){		
	$('#treeview1').treeview('collapseAll');
}
	
function expandTreeview(){		
	$('#treeview1').treeview('expandAll');
}
	
// affiche un objet json dans un element select
function Update_List(json,idselect){
	output = ''
	for(var key in json){
		output+= '<option id="'+key+'">'+json[key]+'</option>';
	}
	$(idselect).html(output);
}

function ClusterizeInputData(){		
	var i = document.getElementById("inputData").selectedIndex;
	var file = document.getElementById("inputData").options[i].value;	
	var url = "/data/input/clusterize/es"
		
	if($('#toggle-event').prop('checked')){url = "/data/input/clusterize/file"} 
	
	$.ajax({
		url: url,
		type: "post", // send it through get method
		data:{file},
		success: function(response) {
			updateListeData("/data/clusterized",'#clusterizedData');
			alert("clusterizing finished");
		},	
		error: function(xhr) {
		    // Do Something to handle error
		}
	});
}		
	
function RemoveInputData(){		
	var i = document.getElementById("inputData").selectedIndex;
	var file = document.getElementById("inputData").options[i].value;	
		
	$.ajax({
		url: "/data/input/remove",
		type: "post", // send it through get method
		data:{file},
		success: function(response) {
			updateListeData("/data/input",'#inputData');
		},
		error: function(xhr) {
		    // Do Something to handle error
		}
	});
}		

function RemovePreprocessedData(){		
	var i = document.getElementById("clusterizedData").selectedIndex;
	var file = document.getElementById("clusterizedData").options[i].value;	
		
	$.ajax({
		url: "/data/clusterized/remove",
		type: "post", // send it through get method
		data:{file},
		success: function(response) {
			updateListeData("/data/clusterized",'#clusterizedData');
		},
		error: function(xhr) {
		    // Do Something to handle error
		}
	});
}		


function VisualizeClusterizeDataNode(node){
	i = document.getElementById("inputData").selectedIndex;

	if(currentTree.length>0){
			// url = "/data/clusterized/"+currentTree+nurl
			// alert(url)
			url = "data/clusterized/node"
 			$.ajax({
 				url: url,
				type: "post", // send it through get method
				data: {node},
				success: function(response) {
					$('#myOccurence').html(response)
					UpdateOccurences(jQuery.parseJSON(response))
					// UpdateOccurences();
				},
				error: function(xhr) {
					alert("Node problem")
				}
			});
	}
	else{
		alert("no tree selected")
	}
}		

function VisualizeClusterizeDataNode2(){
	i = document.getElementById("inputData").selectedIndex;
	var listnode = $('#treeview1').treeview('getSelected')
	// alert(listnode)
	if(currentTree.length>0){
		if (listnode.length>0){
			node = listnode[0].text
			// url = "/data/clusterized/"+currentTree+nurl
			// alert(url)
			url = "data/clusterized/node"
 			$.ajax({
 				url: url,
				type: "post", // send it through get method
				data: {node},
				success: function(response) {
					$('#myOccurence').html(response)
					UpdateOccurences(jQuery.parseJSON(response))
					// UpdateOccurences();
				},
				error: function(xhr) {
					alert("Node problem")
				}
			});
		}
		else{
			alert("no node selected")
		}
	}
	else{
		alert("no tree selected")
	}
}		



// Update the files in the upper right frame
// Code Alex
$(function () {
	$('#fileupload').fileupload({
		dataType: 'json',
	    url: '/data/input/file',
	    sequentialUploads: true,
	    add: function (e, data) {
	        data.submit();
	        // location.reload();
	    },
	    progress: function (e, data) {
	    	var progress = parseInt(data.loaded / data.total * 100, 10);
	        $('#progress .bar').css(
	        	'width',
	            progress + '%'
	        );
	    },

        progressall: function (e, data) {
        	var progress = parseInt(data.loaded / data.total * 100, 10);
            $('#progresstotal .bar').css(
            	'width',
                progress + '%'
            );
        },
        done: function (e, data) {
        	alert('Upload finished.')
        	updateListeData("/data/input/file",'#inputData');
        }
	});
});
				
		
function UpdateOccurences(myJsonoccurence){      
	
	var optsoccurence= '';
	var objoccurence = myJsonoccurence;
	for(var objoccurence in myJsonoccurence){
		optsoccurence+= objoccurence[keyocurence]+"\n";
	}
	$('#myOccurence').html( optsoccurence);
}				
				
function treeviewDefaut(){
	var defaultData =  "{}"   
	$('#treeview1').treeview({
		data: defaultData
    });	
}			 	
			
function updateOccurence(){
	// Occurrence
	var myJsonoccurence = occurence();
	var optsoccurence= '';
	var objoccurence = myJsonoccurence[0];
	
	for(var keyocurence in objoccurence){
	     optsoccurence+= objoccurence[keyocurence]+"\n";
	}
	$('#myOccurence').html( optsoccurence);
}
	
function updateListeData(url,idlist){

	var T_Algorithme = $.ajax({
		url: adresse+url,
		dataType: 'json',
		async: true,
		data: "",
		success: function(data, status, jqXHR) {
			Update_List(data,idlist)		
		}
	});
	return T_Algorithme
}
		
// Treeview
function VisualizeClusterizeData(i){		
	var j = document.getElementById("clusterizedData").selectedIndex;		
	var file = document.getElementById("clusterizedData").options[j].value;
	 
	currentTree = file
	// alert(file)
	$.ajax({
		url: "/data/clusterized/visualize",
		type: "post", // send it through get method
		async: true,
		data:{file},
		success: function(response, status, jqXHR) {				  
			AffichageTreeview(i)
		},
		error: function(xhr) {
		    // Do Something to handle error
		}
	});
}
		

// Show and refresh the the treeview
function AffichageTreeview(i){   		
	
	// Show the treeview
	if (i == 0){
		var j = document.getElementById("clusterizedData").selectedIndex;
		var url = document.getElementById("clusterizedData").options[j].value;
	}
	// Refresh the treeview
	else{
		// save the open node
		var listeNoeudOuvert = $('#treeview1').treeview('getExpanded');
	}
	
	var req = $.ajax({
		url: adresse+"/data/clusterized/"+url,
		dataType: 'json',
		async: true,
		data: "",
		success: function(data, status, jqXHR) {
			var test  = "[" + jqXHR.responseText + "]";
			datatreeview = jQuery.parseJSON(test)
			$('#treeview1').treeview({data:datatreeview}) 
			if (i == 0){
				
				expandTreeview()
			}
			else{
				
				for (var objlist in listeNoeudOuvert){
					$('#treeview1').treeview('expandNode', listeNoeudOuvert[objlist].nodeId);
				}
			}
		},
	})
}



$(function() {
    $('#toggle-event').change(function() {
    	if( $(this).prop('checked')){
    	updateListeData("/data/input/file",'#inputData');
    	$('.pe-7s-trash.btn')[0].classList.remove('hidden')
    	$('.myFile')[0].classList.remove('hidden')
    	}else{
    	updateListeData("/data/input/es",'#inputData');	
    	$('.pe-7s-trash.btn')[0].classList.add('hidden')
    	$('.myFile')[0].classList.add('hidden')
    	
    	}
      
    })
  })
		
	