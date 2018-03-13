<?php

$lot_id = $_GET['lot_id'];


$mysqli = new mysqli();

require 'db_login.php';


// Build query to insert product
$query = "select stall_ID,status from ParkingStalls where lot_id='" . $lot_id . "';" ;
//printf("%s",$query);
//Run query
$result = $mysqli->query($query,MYSQLI_STORE_RESULT);
$errNum = $mysqli->errno;
if ($errNum) {
	printf("<div class='errorMsg'>Error in query: %s </div> <br />",$mysqli->error);

}
else {

		//output result into html table
		$num_rows = $result->num_rows;
		$taken = 0;
		$available = 0;
		$array = [];
		
		
	
		
		for ($i = 0; $i < $num_rows; $i++) {
			$row = $result->fetch_row();
			$stall_id = $row[0];
			$status = $row[1];
			
			if ($status == true) {
				$taken++;
				$array[$i] = true;
			}
			else {
				$available++;
				$array[$i] = false;
			}
			
		}
		echo "<style type='text/css'>canvas {  border: 1px solid black; background: #161616;} </style>";
		echo "<canvas id='stall_graphic'> </canvas>";
		
		

}


// Close the database connection
$mysqli->close();

?>

<script>
    var canvas =document.getElementById("stall_graphic");
    var ctx=canvas.getContext("2d");
    ctx.canvas.width  = window.innerWidth / 2.2;
    ctx.canvas.height = window.innerHeight / 2;
    var width = canvas.width;
    var height = canvas.height;
    
    var taken = <?php echo $taken ?>;
    var available = <?php echo $available?>;
    var total = taken + available;
    var rectWidth = width / total / 2;
    var rectHeight = height / 2;
    var stalls = <?php echo json_encode($array); ?>;
   
   //draw parking stall divisions
    for (i = 1; i < total; i++) {
        ctx.beginPath();
        ctx.moveTo(i * width / total,0);
        ctx.lineTo(i * width / total, height * 2 /3);
        ctx.strokeStyle="#ffd633";
        ctx.lineWidth=10;
        ctx.stroke();
        
    }
    
    ctx.font="bold 24px Consolas";
    ctx.fillStyle = "#ffd633";

    for (i = 0; i < total; i++) {
        
         ctx.fillText("00" +(i+1), (i*2 + 1) * (width/total/2) - (rectWidth/4),height / 8);
        if (stalls[i]) {
           	
            ctx.fillRect( (i*2 + 1) * (width/total/2) - (rectWidth/2), height/6, rectWidth, rectHeight);
          
        }
    }
        
    
    
   
</script>