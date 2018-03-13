<?php

$lot_id = $_GET['lot_id'];

$mysqli = new mysqli();

// Connect to database
require 'db_login.php';

$query = "select username,logtime,text from Comments where lot_id='" .$lot_id . "' order by logtime desc;"; 
$result = $mysqli->query($query,MYSQLI_STORE_RESULT);
$errNum = $mysqli->errno;


if ($errNum) {
	printf("<div class='errorMsg'>Error in query: %s </div> <br />",$mysqli->error);
	printf($query);
}
else {
   
	//output result
	$num_rows = $result->num_rows;
	if ($num_rows == 0) {
		  echo "<div class='row'><div align='center' class='col-m-12'>No comments have been submitted</div></div>";
	}
	else {
	  
	   
	    
		for ($i = 0; $i < $num_rows; $i++) {
			
			$row = $result->fetch_row();
			$username = $row[0];
			$logtime = $row[1];
			$text = $row[2];
			echo "<div class='row'>";
			echo "<div align='center'>";
			echo "<div class='panel panel-default'>";
			
			echo "<div class='panel-heading'>";
			echo "<strong>" . $username . "</strong> commented at " . $logtime;
			echo "</div>"; //end of panel heading
			
			echo "<div class='panel-body'>";
			echo $text;
			echo "</div>"; //end of panel body
			echo "</div></div>";
			
	
			echo  "</div>"; //end row div
		}	
		
		
	}
	
	
}


// Close the database connection
$mysqli->close();
?>