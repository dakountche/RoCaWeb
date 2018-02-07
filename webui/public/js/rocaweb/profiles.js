var adresse = "";
var currentTree="";


	window.onload = function(){
	
		StyleMenuActiv();
		updateListeData("/profiles","#profiles")
	 }
	 
	 function StyleMenuActiv(){
			
			$("#profilesmenu").addClass("active");
		}
	 
	 
	 
	 function Update_List(json,idselect){
			output = ''
			for(var key in json){
				output+= '<option id="'+json[key]+'">Result from job: '+json[key]+'</option>';
			}
			$(idselect).html(output);
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
	 
	 
	 function collapseTreeview(){		
			$('#treeview1').treeview('collapseAll');
		}
			
		function expandTreeview(){		
			$('#treeview1').treeview('expandAll');
		}
		
		
	    // Display the treeview
		// TODO Refactor this method
		function AffichageTreeview(i){   		
			
			// Display the treeview
			if (i == 0){
				var j = document.getElementById("profiles").selectedIndex;
				var id = document.getElementById("profiles").options[j].id;
				currentTree=id
			}
			// Refresh the treeview
			else{
				// Save the openned node
				var listeNoeudOuvert = $('#treeview1').treeview('getExpanded');
			}
			
			var req = $.ajax({
				url: adresse+"/profiles/"+id,
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
				error: function(data, status, jqXHR) {
					alert("Error")
				},
			})
		}
		
		
		function downloadRule(){
			
		var j = document.getElementById("profiles").selectedIndex;
		var id = document.getElementById("profiles").options[j].id;	
		
		
		$.fileDownload("/profiles/download/"+id+"/rules.conf");

	
	}
		
		
		function VisualizeRuleNode(){
			var j = document.getElementById("profiles").selectedIndex;
			var id = document.getElementById("profiles").options[j].id;	
			var listnode = $('#treeview1').treeview('getSelected')
			// alert(listnode)
			if(currentTree.length>0){
				if (listnode.length>0){
					node = listnode[0].text
					
					url = "/profiles/"+id+"/node"
		 			$.ajax({
		 				url: url,
						type: "post", // send it through get method
						data: {node},
						success: function(response) {
							$('#myOccurence').html(response)
						},
						error: function(xhr) {
							alert("Node problem")
						}
					});
				}
				else{
					alert("No node selected")
				}
			}
			else{
				alert("No tree selected")
			}
		}	
	 