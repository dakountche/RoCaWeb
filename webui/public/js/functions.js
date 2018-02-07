	var adresse = "";
	 	
	 	function moins(){
		
			$('#treeview1').treeview('collapseAll');
			}
		
		function expand(){
		
			$('#treeview1').treeview('expandAll');
			}
		
	 	function Traitement_Analyse(){      
		        	var T_Analyse = $.ajax({
			 			url: adresse+"learning/analyse/Traitement_Analyse.json",
			  			dataType: 'json',
			  			async: false,
			  			data: "",
			  			success: function(data) {}
						}).responseText;		
					var objT_analyse = jQuery.parseJSON( "["+T_Analyse+"]" );
				return objT_analyse;
				}
				
		function Traitement_Algorithme(){      
		        	var T_Algorithme = $.ajax({
			 			url: adresse+"learning/algorithme/Traitement_Algorithme.json",
			  			dataType: 'json',
			  			async: false,
			  			data: "",
			  			success: function(data) {}
						}).responseText;		
					var objT_algorithme = jQuery.parseJSON( "["+T_Algorithme+"]" );
				return objT_algorithme;
				}
		
		function Traitement_FormatRegle(){      
		        	var T_FormatRegle = $.ajax({
			 			url: adresse+"learning/formatRegle/Traitement_FormatRegle.json",
			  			dataType: 'json',
			  			async: false,
			  			data: "",
			  			success: function(data) {}
						}).responseText;		
					var objT_FormatRegle = jQuery.parseJSON( "["+T_FormatRegle+"]" );
				return objT_FormatRegle;
				}
				
		function getdata2(){      
		        	var test = $.ajax({
			 			url: adresse+"/profile/www.kereval.com", // "data/treeview/general/mock.json",
			  			dataType: 'json',
			  			async: false,
			  			data: "",
			  			success: function(data) {}
						}).responseText;		
					var obj = jQuery.parseJSON( "["+test+"]" );
				return obj;
				}
		
		
		// Fonction refresh treeview NON UTILISE
		function refresh() {
					var tmp = $('#treeview1').treeview({data: getdata2()});
	           		document.getElementById('#treeview1').innerHTML = tmp;
				};
		      			
		window.onload = function(){
		
			// Format the rules
			var myJsonregle = Traitement_FormatRegle();
			var optsregle= '';
			var objregle = myJsonregle[0];
			for(var keyregle in objregle){
			     optsregle+= '<option id="'+keyregle+'">'+objregle[keyregle]+'</option>';
			}
			$('#myRegle').html( optsregle);
			
			 
			// Textarea
			var myJsonalgo = Traitement_Algorithme();
			var optsalgo= '';
			var objalgo = myJsonalgo[0];
			for(var keyalgo in objalgo){
			     optsalgo+= '<option id="'+keyalgo+'">'+objalgo[keyalgo]+'</option>';
			}
			$('#myAlgo').html( optsalgo);
			
			
			// Checkbox
			var myJsonanalyse = Traitement_Analyse();
			var optsanalyse= '';
			var objanalyse = myJsonanalyse[0];
			for(var keyanalyse in objanalyse){
			     optsanalyse+= '<input type="checkbox" id="'+keyanalyse+'">'+" " + objanalyse[keyanalyse]+'</input></br>';
			}
			$('#myAnalyse').html( optsanalyse);
		}

		$('.BSswitch').bootstrapSwitch('state', true);
		$('#CheckBoxValue').text($("#TheCheckBox").bootstrapSwitch('state'));
		$('#TheCheckBox').on('switchChange.bootstrapSwitch', function () {
		    $("#CheckBoxValue").text($('#TheCheckBox').bootstrapSwitch('state'));
		});
		$('.probeProbe').bootstrapSwitch('state', true);
		$('.probeProbe').on('switchChange.bootstrapSwitch', function (event, state) {
			alert(this);
	    	alert(event);
	    	alert(state);
		});
		$('#toggleSwitches ').click(function () {
		    $('.BSswitch ').bootstrapSwitch('toggleDisabled');
		    if ($('.BSswitch ').attr('disabled')) {
		        $(this).text('Enable All Switches ');
		    } else {
		        $(this).text('Disable All Switches ');
		    }
		});
	  	
		
		$(function() { 
			var defaultData =  getdata2()    
	  		$('#treeview1').treeview({
	        	data: defaultData
	        });
		});
		
		
		function reload(){
			// Save the opened nodes
			var listeNoeudOuvert = $('#treeview1').treeview('getExpanded');
			
			// Reload the tree
			var tmp = $('#treeview1').treeview({data: getdata2()});
	       
			
			// Open the already opened node
			for (var objlist in listeNoeudOuvert){
			 
			 $('#treeview1').treeview('expandNode', listeNoeudOuvert[objlist].nodeId);
			 
			 }
			
		}
		
		
		<!-- Fonction Javascript  --> 	
		<script type="text/javascript">
		
		 var adresse = "";
		 
		 
		 
		 function moins(){		
				$('#treeview1').treeview('collapseAll');
			}
		 
		 
		
		function expand(){		
				$('#treeview1').treeview('expandAll');
			}
		
		
		function UploadData(){		
			var i = document.ListeInputData.inputData.selectedIndex;
			var file = document.ListeInputData.inputData.options[i].value;
		
			
			$.ajax({
				  url: "/data/input/clusterize",
				  type: "post", // send it through get method
				  data:{file},
				  success: function(response) {
				    // Do Something
				  },
				  error: function(xhr) {
				    // Do Something to handle error
				  }
				});
		}		
			
		
		function lien(){
		 	var i = document.Choix.Liste.selectedIndex;
			var url = document.Choix.Liste.options[i].value;
			
			if (url == "Tous" | url==""){
				treeviewDefaut();
			}
			else{
				treeview(url);
			}	
		}
		
		
		// Allow the upload of the data
		$(function () {
		    $('#fileupload').fileupload({
		        dataType: 'json',
		        url: '/data/input',
		        sequentialUploads: true,
		        add: function (e, data) {
		            data.context = $('<p/>').text('Uploading...').appendTo(document.body);
		            data.submit();
		            location.reload();
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
		            data.context.text('Upload finished.');
		        }
		    });
		    
		});
		
		
		
		function tableau(){   
			var tableau = $.ajax({
				url: adresse+"/data/clusterized",
				dataType: 'json',
				async: false,
				data: "",
				success: function(data) {}
				}).responseText;		
			var objtableau = jQuery.parseJSON( "["+tableau+"]"    );
			return objtableau;
		}
		
		
					
		function List_InputData(){      
			var T_Algorithme = $.ajax({
				url: adresse+"/data/input",
				dataType: 'json',
				async: false,
				data: "",
				success: function(data) {}
				}).responseText;		
			var objT_algorithme = jQuery.parseJSON( "["+T_Algorithme+"]" );
			return objT_algorithme;
		}
				
				
		 
		function test(website){      
			var myJsonSite = site(website);
			var optsSite= '';
			var objsite = myJsonSite[0];
						
			for(var keysite in objsite){
				optsSite+= objsite[keysite]+"\n"
			}
			
			$('#mySite').html(optsSite);
			return null;
		}
			
		function site(url){     
			var site = $.ajax({
				url: adresse +url,
				dataType: 'json',
				async: false,
				data: "",
				success: function(data) {}
				}).responseText;		
			var objsite = jQuery.parseJSON( "["+site+"]" );
			return objsite;
		}
			
		function occurence(){      
	    	var test = $.ajax({
	 			url: adresse +"/data/clusterized/idwww/rule",
	  			dataType: 'json',
	  			async: false,
	  			data: "",
	  			success: function(data) {}
				}).responseText;		
			var obj = jQuery.parseJSON( "["+test+"]" );
			return obj;
		}
			
				
					
		function getdata2(url){      
			var test = $.ajax({
					url: adresse+"/data/clusterized/"+url,
	 				dataType: 'json',
	 				async: false,
	 				data: "",
	 				success: function(data) {}
				}).responseText;					
			var obj = jQuery.parseJSON( "["+test+"]" );
			return obj;
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
				   	
		function List_ClusterizedData(){      
	       	var T_Algorithme = $.ajax({
	 			url: adresse+"/data/clusterized",
	  			dataType: 'json',
	  			async: false,
	  			data: "",
	  			success: function(data) {}
				}).responseText;		
			var objT_algorithme = jQuery.parseJSON( "["+T_Algorithme+"]" );
			return objT_algorithme;
		} 
			
		function data(){      
	       	var test = $.ajax({
	 			url: adresse+"/data/clusterized/idwww/rule",
	  			dataType: 'json',
	  			async: false,
	  			data: "",
	  			success: function(data) {}
				}).responseText;		
			var obj = jQuery.parseJSON( "["+test+"]" );
			return obj;
		}	
					
					
		function treeviewDefaut(){
			var defaultData =  data()    
	  		$('#treeview1').treeview({
	        	data: defaultData
	        });	
		}			 	
				
			
		window.onload = function(){
		
			treeviewDefaut();
		
			// Site Textarea
			var myJsonalgo = List_ClusterizedData();
			var optsalgo= '';
			var objalgo = myJsonalgo[0];
			for(var keyalgo in objalgo){
			     optsalgo+= "<OPTION VALUE='" + objalgo[keyalgo] + "'>" + objalgo[keyalgo] ;
			}
			$('#clusterizedData').html( optsalgo);
			
			
			// Occurrence
			var myJsonoccurence = occurence();
			var optsoccurence= '';
			
			var objoccurence = myJsonoccurence[0];
			for(var keyocurence in objoccurence){
			     optsoccurence+= objoccurence[keyocurence]+"\n";
			     
			}
			$('#myOccurence').html( optsoccurence);
		
			
			// Text area
			var myJsonalgo = List_InputData();
			var optsalgo= '';
			var objalgo = myJsonalgo[0];
			for(var keyalgo in objalgo){
			     optsalgo+= '<option id="'+keyalgo+'">'+objalgo[keyalgo]+'</option>';
			}
			$('#inputData').html( optsalgo);
			
			
						
			
			// Dynamic table
			
			var $records = $('#json-records'),	myRecords = tableau();
			for(var key in myRecords )
			{
				$('#my-final-table').dynatable({
					dataset: {
			    		records: myRecords 
			  		}
				});
			}
						
		
		
			// Site
			var myJsonSite = site();
			var optsSite= '';
			
			var objsite = myJsonSite[0];
			for(var keysite in objsite){
			     optsSite+= objsite[keysite]+"\n"
			     
			}
			$('#mySite').html(optsSite);
			
		}  	 	
			
			
		function charge(){
		
		i = document.Choix.Liste.selectedIndex;
		url = document.Choix.Liste.options[i].value;
			
			// Saved the opened node
			var listeNoeudOuvert = $('#treeview1').treeview('getExpanded');
			
			// Reload the tree
			var tmp = $('#treeview1').treeview({data:getdata2(url)});
	       		
			// Open the already opened nodes
			for (var objlist in listeNoeudOuvert){
				$('#treeview1').treeview('expandNode', listeNoeudOuvert[objlist].nodeId);
			}		
			return null;
		}
			
		function reload1(){
			// Save the opened nodes
			var listeNoeudOuvert = $('#treeview1').treeview('getExpanded');
			
			// Reload the tree
			var tmp = $('#treeview1').treeview({data: data()});
			
			// Reopen the nodes
			for (var objlist in listeNoeudOuvert){
				$('#treeview1').treeview('expandNode', listeNoeudOuvert[objlist].nodeId);
			}
		}
			
			 
		
	  	
		<!-- Fonction Javascript  --> 	
			<script type="text/javascript">   

				var adresse = "";
				
				function moins(){
				
					$('#treeview1').treeview('collapseAll');
				}
				
				function expand(){
				
					$('#treeview1').treeview('expandAll');
				}
				
				function essaie(){      
				        	var param1 = $.ajax({
					 			url: adresse +"/profile",
					  			dataType: 'json',
					  			async: false,
					  			data: "",
					  			success: function(data) {}
								}).responseText;		
							var objparam1 = jQuery.parseJSON( "["+param1+"]" );
						return objparam1;
						}
						
				function bis(){      
				        	var c = essaie()
				        	var tot='<tr><td><b> Profil</b></td><td><b>Website</b></td><td><b>Actif</b></td></tr> ';
				        	
				        	for (var obj in c){
				        		var profil = c[obj].profil;
				        		var website = c[obj].website;
				        		var actif = c[obj].actif;
				        		
				        		tot+= "<tr><td>"+profil+"</td>" + "<td><a href=javascript:test('"+website+"')>"+website+"</td>" + "<td>"+actif+"</td></tr>";
				        	}
				        	
				        $('#myTable').html(tot);	
				        	
						return tot;
						}
				
				function charge(){
				
					var listeNoeudOuvert = $('#treeview1').treeview('getExpanded');
					
					var site = listeNoeudOuvert[0].text
					
					
					test(site);
			       
					
					for (var objlist in listeNoeudOuvert){
					 
					 $('#treeview1').treeview('expandNode', listeNoeudOuvert[objlist].nodeId);
					 
					 }
					
					return site;
				}
				
				function test(url){      
				        	 
						      // Site
							var defaultData =  getdata2(url)    
				  			$('#treeview1').treeview({
				          		data: defaultData
				        	});
				        	
						}
				
				function site(url){     
							
				        	var site = $.ajax({
					 			url: adresse +url, // +".json",
					  			dataType: 'json',
					  			async: false,
					  			data: "",
					  			success: function(data) {}
								}).responseText;		
							var objsite = jQuery.parseJSON( "["+site+"]" );
							
						return objsite;
						}
								
				function EditionRegex(){      
				        	var regex = $.ajax({
					 			url: adresse+"/profile/idwww/Regex",
					  			dataType: 'json',
					  			async: false,
					  			data: "",
					  			success: function(data) {}
								}).responseText;		
							var objregex = jQuery.parseJSON( "["+regex+"]" );
						return objregex;
						}
						
				function EditionSecrules(){      
				        	var Secrules = $.ajax({
					 			url: adresse+"/profile/idwww/Secrules", // "/profile/edition/regles/Edition_Secrules.json",
					  			dataType: 'json',
					  			async: false,
					  			data: "",
					  			success: function(data) {}
								}).responseText;		
							var objsecrules = jQuery.parseJSON( "["+Secrules+"]" );
						return objsecrules;
						}					
					
				function param1(){      
				        	var param1 = $.ajax({
					 			url: adresse +"/profile/idwww/Param1", // "profile/edition/regles/Edition_Param1.json",
					  			dataType: 'json',
					  			async: false,
					  			data: "",
					  			success: function(data) {}
								}).responseText;		
							var objparam1 = jQuery.parseJSON( "["+param1+"]" );
						return objparam1;
						}
				
				function param2(){      
				        	var param2 = $.ajax({
					 			url: adresse + "/profile/idwww/Param2", // "profile/edition/regles/Edition_Param2.json",
					  			dataType: 'json',
					  			async: false,
					  			data: "",
					  			success: function(data) {}
								}).responseText;		
							var objparam2 = jQuery.parseJSON( "["+param2+"]" );
						return objparam2;
						}	
				
				
						
				function refresh() {
							var tmp = $('#treeview1').treeview({data: getdata2()});
		        			document.getElementById('#treeview1').innerHTML = tmp;
						};
		    
		    	function getdata2(url){      
		    				var test = $.ajax({
				 				url: adresse+"/data/clusterized/"+url,
				  				dataType: 'json',
				  				async: false,
				  				data: "",
				  				success: function(data) {}
								}).responseText;					
							var obj = jQuery.parseJSON( "["+test+"]" );
						return obj;
		   				}		
				function getdata3(){      
				        	var test = $.ajax({
					 			url: adresse+"/data/clusterized",
					  			dataType: 'json',
					  			async: false,
					  			data: "",
					  			success: function(data) {}
								}).responseText;		
							var obj = jQuery.parseJSON( "["+test+"]" );
						return obj;
						}			
				      			
				window.onload = function(){
				
					bis()	
				
					// Load the treeview
					var defaultData =  getdata3()    
			  		$('#treeview1').treeview({
			        	data: defaultData
			        });
					
					// Edition of the regex
					var myJsonregex = EditionRegex();
					var optsregex= '';
					var objregex = myJsonregex[0];
					for(var keyregex in objregex){
					     optsregex+= objregex[keyregex];
					}
					$('#Regex').html( optsregex);
					
					
					// Edition of the security rules
					var myJsonrules = EditionSecrules();
					var optsrules= '';
					var objrules = myJsonrules[0];
					for(var keyrules in objrules){
					     optsrules+= objrules[keyrules];
					}
					$('#Rules').html( optsrules);
					
					
						
				    // Edition of the rules
					var myJsonparam1 = param1();
					var optsparam1= '';
					var objparam1 = myJsonparam1[0];
					for(var keyparam1 in objparam1){
					     optsparam1+= '<option id="'+keyparam1+'">'+objparam1[keyparam1]+'</option>';
					}
					$('#myList').html( optsparam1);
				
				
					// Show the list of parameters
					var myJsonparam2 = param2();
					var optsparam2= '';
					var objparam2 = myJsonparam2[0];
					for(var keyparam2 in objparam2){
					     optsparam2+= '<option id="'+keyparam2+'">'+objparam2[keyparam2]+'</option>';
					}
					$('#myparam').html( optsparam2);
				
				}
				  	 	      
			
				$(function() { 
		  			var defaultData =  getdata3()
		  			$('#treeview1').treeview({
		          		data: defaultData
		       		 });
				});

			</script>
	
				
