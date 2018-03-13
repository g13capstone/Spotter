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
		
		
		echo "<div style='height: 150px; overflow-y: auto;' class='table-condensed'>";
		echo "<table class='table table-hover'>";
		echo "<thead>";
		echo "<tr><th>Stall Number</th><th>status</th></tr>";
		echo "</thead>";
		echo "<tbody>";
	
		
		for ($i = 0; $i < $num_rows; $i++) {
			$row = $result->fetch_row();
			$stall_id = $row[0];
			$status = $row[1];
			
			echo "<tr>";
			echo "<td> . $stall_id . </td>";
			echo "<td>";
			if ($status == true) {
				echo "TAKEN";
				$taken++;
			}
			else {
				echo "AVAILABLE";
				$available++;
			}
			echo "</td></tr>";
			
		}
		
		echo "</table>";
		
}


// Close the database connection
$mysqli->close();

?>