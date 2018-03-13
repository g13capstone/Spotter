<?php
session_start();

$lot_id = $_GET['lot_id'];
?>

<!DOCTYPE html>
<html lang='en'>
<head>
<link rel='stylesheet' href = "app-style.css">
<link href="https://fonts.googleapis.com/css?family=Lato|Open+Sans" rel="stylesheet">
<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css">
<meta charset="utf-8">
<title>Spotter Lot Information</title>
<style>
hr { 
    display: block;
    margin-top: 0.5em;
    margin-bottom: 0.5em;
    margin-left: auto;
    margin-right: auto;
    border-style: inset;
    border-width: 1px;
} 
</style>

</head>
<body>
    
  <div class="container-fluid">
        <?php include 'header.php' ?>
        
        <?php
    $mysqli = new mysqli();
    
    require 'db_login.php';
    
    
    // Build query to insert product
    $query = "SELECT lot_name, location, price, temp, image FROM ParkingLots WHERE lot_id='" . $lot_id . "';"; 
    
    //Run query
    $result = $mysqli->query($query,MYSQLI_STORE_RESULT);
    $errNum = $mysqli->errno;
    
    
    if ($errNum) {
    	printf("<div class='errorMsg'>Error in query: %s </div> <br />",$mysqli->error);
    
    }
    else {
        $row = $result->fetch_row();
        $lot_name = $row[0];
        $location = $row[1];
        $price = $row[2];
        $temp = $row[3];
        $image = $row[4];
        $image_path = "./lot_images/";
        if ($image == null) {
            $image_path = $image_path . "not available.jpg";
        }
        else {
            $image_path = $image_path . $image;
        }
        
      
    }
    
    
    // Close the database connection
    $mysqli->close();
?>
		
		<div class='row'>
			<div align='center' class='col-md-12' id='temp_warning'>
				
			</div>
		</div>
		<div class='row'>
			<div class='col-md-6'> <!-- left card -->
				 <div class='panel-group'>
				 	<div class='panel panel-default'>
				 		<div class='panel-heading'>
				 			<h4> <?php echo $lot_name  ?> <small> <?php echo $location ?> </small></h4>
				 		</div>
    				 	<div class='panel-body'>
    				 		<div class='row'> <!-- image section -->
    				 			 <div class='col-md-6'>
    				 			 	<img src='<?php echo $image_path?>' class='img-thumbnail' alt='Cinque Terre' style='max-height: 300px; max-width: 500 px;'>
    				 			 </div>
    				 			 <div class='col-md-6' id='lot_details'>
    				 			 	
    				 			 </div>
    				 		</div>
    				 		<hr>
    				 		<div class='row'> <!-- comment title -->
    				 			<div align='center' class='col-m-12'><h3>Submitted Comments</h3></div>
    				 		</div>
    				 		<div class='row'> <!-- comments table -->
    				 			<div class='col-md-12'>
    				 				<div class='vscroll' id='comment_table' >
        
    								</div>
    				 			
    				 			</div>
    				 			
    				 		</div>
    				 		<hr>
    				 		<div class='row'>
    				 			<div align='center' class='col-m-12'><h3>Add Comment</h3></div>
    				 		</div>
    				 		<div class='row' id='input_comment'>
    				 			<div class='col-md-3'>
    				 				<label for='username'>Username:</label>
    				 				<br>
                        			<input type='text'  id='username'>
                        			<br>
                        			<label for='user_comment'>Issue:</label>
                        			<br>
                        			<select id='user_comment'>
                        				<option value='None'> (NONE) </option>
                        				<option value='Obstruction'>Obstruction</option>
                        				<option value='Knocked Over Sensor'>Knocked Over Sensor</option>
                        				<option value='Other'>Other</option>
                        			</select>
                        		</div>
                        		<div class='col-md-5'>
                        		    <label for='description'>Optional Description:</label>
                        		    <br>
                        			<textarea rows=3 cols=50 id='description'></textarea>
                        		</div>
                        		<div class='col-md-4'>
                        		    <div align='left'>
                        
                            		    <br>
                            		    <br>
                            		    <br>
                            			<button id='submit_comment' class="btn btn-primary">Submit</button>
                            			<button id='clear_comment' class="btn btn-danger">Clear</button>
                        			</div>
    				 			</div>
    				 		</div>
    				 		<div class='row'>
    				 			<div id='comment_result'>
									
								</div>
    				 		</div>
    				 	</div>
				 	</div>
				 </div>
			</div>
			<div class='col-md-6'> <!--  right card  -->
				<div class='panel-group'>
					<div class='panel panel-default'>
				 		<div class='panel-heading'>
				 			<strong>STALL INFO</strong>
				 		</div>
    					<div class='panel-body'>
    					
    						<div class='row'>		<!-- graphical view -->
    							<div class='col-md-12' id='graphic_view'>
    							
    							</div>
    						</div>
    						<div class='row'> <!-- list view -->
    							<div  class='col-md-12' id='list_view'>
    							
    							
    							</div>
    						</div>
    					</div>
    				</div>
				</div>
			</div>
		</div>		
    
   	</div> <!-- /div container-fluid -->
	
	<script src="jquery-3.2.0.min.js"></script>
	<script>
        var lotID = <?php echo "'" . $lot_id . "'"  ?>;
        var list_interval;
        var stall_interval;
		var com_interval;
		var lot_interval;
		var temp_interval;
		
		function lot_details() {
		    clearInterval(lot_interval);
			$.ajax({
				type: 'GET',
				url: 'get_lot_details.php',
				data: 'lot_id=' + lotID,				
				success: function(msg) {
					$('#lot_details').html(msg);
				}
			});
			lot_interval = setInterval(lot_details, 3000);
			
		}

		function temp_warning() {
		    clearInterval(temp_interval);
			$.ajax({
				type: 'GET',
				url: 'temp_warning.php',
				data: 'lot_id=' + lotID,				
				success: function(msg) {
					$('#temp_warning').html(msg);
				}
			});
			temp_interval = setInterval(temp_warning, 3000);
			
		}

		
		
		function list_stalls() {
		    clearInterval(list_interval);
			$.ajax({
				type: 'GET',
				url: 'get_stalls_list.php',
				data: 'lot_id=' + lotID,				
				success: function(msg) {
					$('#list_view').html(msg);
				}
			});
			list_interval = setInterval(list_stalls, 3000);
			
		}
		
		
		function draw_stalls() {
		    clearInterval(stall_interval);
		    $.ajax({
				type: 'GET',
				url: 'get_stalls_graphic.php',
				data: 'lot_id=' + lotID,				
				success: function(msg) {
					$('#graphic_view').html(msg);
				}
			});
			stall_interval = setInterval(draw_stalls, 3000);
		}
		
		
		function submit_comment() {
			if ($("#username").val() == '') {
				alert('username cannot be empty');
			}
			else if ($("#user_comment").val() == "None") {
				alert('Please select an issue you would like to report');
			}
			else {
			    var user_description = "";
			    if ($("#description").val() != "" ) {
			        user_description = " - " + $("#description").val();
			    }
				
				$.post("add_comment.php",
                {
                    lot_id: lotID,
                    username: $("#username").val(),
                    comment: $("#user_comment").val() + user_description 
                   
                },
                function(msg){
                    $('#comment_result').html("<p style='color:green'>Comment successfully added</p>");
                });
				clearcomment();
			}
		
		}
		
		function get_comments() {
			clearInterval(com_interval);
			$.ajax({
					type: 'GET',
					url: 'show_comments.php',
					data: 'lot_id=' + lotID,				
					success: function(msg) {
						$('#comment_table').html(msg);
					}
				});
			com_interval = setInterval(get_comments, 3000);
		}
		
		function clearcomment() {
			$('#user_comment').val('None');
			$('#username').val('');
			$('#description').val('');
			$('#comment_result').html('');
			
		}
	
		$( document ).ready(function() {
			$('#clear_comment').click( clearcomment );
			$('#submit_comment').click( submit_comment );
			
			get_comments();
	        list_stalls();
	        draw_stalls();
	        lot_details();
	        temp_warning();
		  		
		});
	</script>
</body>
</html>