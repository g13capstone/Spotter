<?php

/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
$mysqli = new mysqli();

// Connect to database
require 'db_login.php';

class lotInfo{
    
    public $id = "";
    public $name = "";
    public $location = "";
    public $price = "";
    public $latitude = "";
    public $longitude = "";
    public $temp = "";
    public $capacity = "";
    public $available = "";
}

// Build query to insert product
$query = "select L.lot_id, lot_name, location, price, latitude, longitude, temp, count(S.stall_id) as capacity, count(CASE WHEN S.status THEN 1 END) as taken from ParkingLots L, ParkingStalls S where L.lot_id = S.lot_id group by S.lot_id;" ;
//printf("%s",$query);
//Run query
$result = $mysqli->query($query,MYSQLI_STORE_RESULT);
$errNum = $mysqli->errno;
if ($errNum) {
	printf("<div class='errorMsg'>Error in query: %s </div> <br />",$mysqli->error);

}
else {

	$num_rows = $result->num_rows;
	    
	$lot = array();
		
        for ($i = 0; $i < $num_rows; $i++) {
            $lot_info = new lotInfo();
            $row = $result->fetch_row();
            $lot_info->id = $row[0];
            $lot_info->name = $row[1];
            $lot_info->location = $row[2];
            $lot_info->price = $row[3];
            $lot_info->latitude = $row[4];
            $lot_info->longitude = $row[5];
            $lot_info->temp = $row[6];
            $lot_info->capacity = $row[7];
            $lot_info->availability = $row[7] - $row[8];
			
            array_push($lot, $lot_info);
	        
        }
        echo json_encode($lot);
        
}
// Close the database connection
$mysqli->close();

?>
