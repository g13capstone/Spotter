<?php

/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
$lot_id = $_GET['lot_id'];

class stallInfo{
    
    public $stall_id = "";
    public $status = "";
    
}


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

		//output result
		$num_rows = $result->num_rows;
	
                $stall = array();
		
            for ($i = 0; $i < $num_rows; $i++) {
                $stall_info = new stallInfo();
                $row = $result->fetch_row();
                $stall_info->stall_id = $row[0];
                $stat = $row[1];
                
                if ($stat == true) {
                    $stall_info->status = "TAKEN";
                    
                }
                else {
                    $stall_info->status = "AVAILABLE";
                    
                }
                array_push($stall, $stall_info);

            }
            echo json_encode($stall);
                	
		
}
// Close the database connection
$mysqli->close();

?>

