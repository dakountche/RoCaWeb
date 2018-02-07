


var adresse = "";
var currentTree="";
	var i=0;

	

window.onload = function(){

	treeviewDefaut();	
	updateListeClusterized();
	ServeDataList();	
	RecuperationDonneesTableauJob()	
	StyleMenu();
	poll()
}  	 	

function poll() {
    setTimeout(function(){
    	$.ajax({
        url: "/learning/jobs",
        type: "GET",
        dataType: "json",
        success: function(data, status, jqXHR) {
        	AffichageDonneesTableauJob(data)
        },
        complete: function(){poll()}
         // temps en ms soit 1000 = 1 seconde
    });
    },5000)
}


// Permet de surligner (dans le menu) l'onglet où nous sommes actuellement
function StyleMenu(){				
	$("#learningmenu").addClass("active");
	
}
	
// permet de récuperer les données pour le tableau jobs du fichier JSON
function RecuperationDonneesTableauJob(){      
	var DonneesTableau = $.ajax({
		url: "/learning/jobs",
		dataType: 'json',
		async: true,
		data: "",
		success: function(data, status, jqXHR) {
			AffichageDonneesTableauJob(data)
		}
		})
}

// Permet d'afficher les données Jobs dans le tableau Jobs
function AffichageDonneesTableauJob(dataJobs){      
	var i=0;
	var tot='<tr><td align="center"><b>#</b></td><td align="center"><b>Configuration</b></td><td align="center"><b>Input</b></td><td align="center"><b>Node</b></td><td align="center"><b>Status</b></td><td align="center"><b>Save</b></td></tr> ';
	

	for (var objJobs in dataJobs){
		var id = objJobs;
		var node = dataJobs[objJobs].node;
		var configuration = dataJobs[objJobs].configuration;
		var input = dataJobs[objJobs].input;
		var status = dataJobs[objJobs].status;
		
		statusJobs=StatusJobs(status,id)  	    		
		
		tot+= "<tr><td align='center'>"+id+"</td>" + "<td align='center'>"+configuration+"</td>" + "<td align='center'>"+input+"</td>" + "<td align='center'>"+node+"</td>" + "<td align='center'>"+statusJobs.status+"</td><td align='center'>"+statusJobs.button+"</td></tr>";
		i++
	}
	
	$('#tab_logic').html(tot);	
}

// Permet d'affecter une couleur à un statut ainsi que de bloquer le bouton ou
// non
function StatusJobs(status,id){
	switch(status){
	case "finish":
		monStatus= "<span class='label label-success'>Finished</span>";
		monButton = "<i onclick='deleteJob("+id+")' class='pe-7s-close-circle btn'></i>";
		break;
	case "running":
		monStatus = "<span class='label label-default'>Running</span>";
		monButton = "<i class='pe-7s-refresh-2' disabled='disabled'></i>";
		break;
	case "failed":
		monStatus = "<span class='label label-danger'>Failed</span>";
		monButton = "<i class='pe-7s-refresh-2' disabled='disabled'></i>";
		break;
	case "waiting":
		monStatus = "<span class='label label-primary'>Waiting</span>";
		monButton = "<i class='pe-7s-refresh-2'></i>";
		break;
	default:
		monStatus = "<span class='label label-warning'>Warning</span>";
		monButton = "<i class='pe-7s-refresh-2' disabled='disabled'></i>";			
	}		
	return {status: monStatus, button: monButton}
}
	
// Ajoute une ligne au tableau des Jobs (ajout en dur) -> inutile lorsque
// addRowsJobs() sera traiter
function addRow(name,config,input,status){
		valeur=StatusJobs(status)
        $('#addr'+i).html(
                "<td>"+ i +"</td><td>"+name+"</td><td>"+config+"</td><td>"+input+"</td><td>"+valeur.status+"</td><td>"+valeur.button+"</td>");

        $('#tab_logic').append('<tr id="addr'+(i+1)+'"></tr>');
        i++; 
}

// Requete pour ajouter une ligne au tableau des Jobs -> se referer à la
// fonction addRow
function addRowJobs(name,config,input,status){		
		
	$.ajax({
        data: {name,config,input,status},
        url: "/data/input",
        Type:"POST",	        
        success: function (e, data) {
            alert("success");
        },
        error: function(xhr) {
        	alert("bad");
		  }
	})   
}
		
// Permet de supprimer une ligne dans le tableau Job
function deleteRow(){
 	 if(i>1){
	 $("#addr"+(i-1)).html('');
	 i--;
	 }
 }
	

// Replie tous les noeuds de la treeview
function collapseTreeview(){		
		$('#treeview1').treeview('collapseAll');
}
 
 
// Deplie tous les noeuds de la treeview
function expandTreeview(){		
		$('#treeview1').treeview('expandAll');
	}

	
function VisualizeClusterizeData(){		
	var i = document.getElementById("clusterizedData").selectedIndex;		
	var file = document.getElementById("clusterizedData").options[i].value;
	 
	currentTree = file
	// alert(file)
	$.ajax({
		  url: "/data/clusterized/visualize",
		  type: "post", // send it through get method
		  data:{file},
		  success: function(response) {
			  AffichageTreeview(0)
			 // expand()
		    
		  },
		  error: function(xhr) {
		    // Do Something to handle error
		  }
		});
	
}		


function VisualizeClusterizeDataNode(){
	i = document.getElementById("inputData").selectedIndex;
	var listnode = $('#treeview1').treeview('getSelected')
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
				  alert( "["+response+"]" )
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
		
			

			
function refresh() {
	var tmp = $('#treeview1').treeview({data: getdata2()});
    document.getElementById('#treeview1').innerHTML = tmp;
}
		
		
function treeview(url) { 
	var defaultData =  getdata2(url)    
	$('#treeview1').treeview({
    	data: defaultData
   	});
};	
		   	


function ListeData(url){
	var data = $.ajax({
		url: adresse+url,
		dataType: 'json',
		async: false,
		data: "",
		success: function(data) {}
		}).responseText;		
	var ObjData = jQuery.parseJSON( "["+data+"]" );
	return ObjData;
}
	
function ServeDataList(){
	var url = "/res/json/algorithms.json"
	$.ajax({
		url: adresse+url,
		dataType: 'json',
		async: true,
		data: "",
		success: function(data) {
			updateListAlgorithms(data)
			
		},
		error : function(data){alert()}
		});		
}
				
			
function treeviewDefaut(){
	var defaultData = "{}"     
	$('#treeview1').treeview({
    	data: defaultData
    });	
}			 	
		
	function updateOccurence(){
		// Occurence
		var myJsonoccurence = occurence();
		var optsoccurence= '';
		
		var objoccurence = myJsonoccurence[0];
		for(var keyocurence in objoccurence){
		     optsoccurence+= objoccurence[keyocurence]+"\n";
		     
		}
		$('#myOccurence').html( optsoccurence);
		
	}
	
	
	function updateListeClusterized(){
		// Zone de texte deroulante Site
		var myJsonalgo = ListeData("/data/clusterized");
		var optsalgo= '';
		var objalgo = myJsonalgo[0];
		for(var keyalgo in objalgo){
		     optsalgo+= "<OPTION VALUE='" + objalgo[keyalgo] + "'>" + objalgo[keyalgo] ;
		}
		$('#clusterizedData').html( optsalgo);
	}
			
	
	
	function updateListAlgorithms(objalgo){
		// Zone de texte deroulante
		// var objalgo = ServeDataList("/res/json/algorithms.json");
		var optsalgo= '';
		for(var keyalgo in objalgo){
		     optsalgo+= '<option id="'+objalgo[keyalgo].id+'" value="'+objalgo[keyalgo].file+'"+>'+objalgo[keyalgo].name+'</option>';
		}
		$('#algorithms').html(optsalgo);
		
	}
	
	
	function launchNewLearningBatch(){
	
		
		var i = document.getElementById("clusterizedData").selectedIndex;
		var j = document.getElementById("algorithms").selectedIndex;
		var listnode = $('#treeview1').treeview('getSelected')
		
		if (i == -1 || j == -1){
			alert("bad selection")
		}
		else{
	
			var input = document.getElementById("clusterizedData").options[i].value;
			var algorithm = document.getElementById("algorithms").options[j].value;
			var node = "";
			if (listnode.length>0){
				node = listnode[0].text
			}
			
			
			$.ajax({
				  url: "/learning/job/new",
				  type: "POST", // send it through get method
				  data:{input,node,algorithm},
				  success: function(response) {
				
					 // alert("launch")
				    
				  },
				  error: function(xhr) {
					  
					  alert("Error while lauchning learning job")
					 
				    // Do Something to handle error
				  }
				});
		
		}
		
	}
	
	
	function deleteJob(i){
		
		$.ajax({
			  url: "/learning/job/delete/"+i,
			  type: "get", // send it through get method
			  success: function(response) {
			
				 // alert("launch")
			    
			  },
			  error: function(xhr) {
				  
				  alert("error when lauchning learning job")
			    // Do Something to handle error
			  }
			});
		
	}

	


function AffichageTreeview(i){   		
	
	// affichage de le treeview
	if (i == 0){
		var j = document.getElementById("clusterizedData").selectedIndex;
		var url = document.getElementById("clusterizedData").options[j].value;
	}
	// refresh de la treeview
	else{
		// Sauvegarde des noeud ouvert
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
				// deplie tous les noeuds lors d'un affichage
				expandTreeview()
			}
			else{
				// redeplier les noeuds ouvert lors d'un refresh
				for (var objlist in listeNoeudOuvert){
					$('#treeview1').treeview('expandNode', listeNoeudOuvert[objlist].nodeId);
				}
				alert("MàJ OK")
			}
		},
	})
}


