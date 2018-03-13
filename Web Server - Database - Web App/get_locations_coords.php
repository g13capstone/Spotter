<?php

/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

class coordinate{
    
    public $lat = "";
    public $lng = "";
}

$location = $_GET['location'];
$mysqli = new mysqli();

// Connect to database
require 'db_login.php';

// Build query to insert product
$query = "select latitude,longitude from Locations where location='". $location . "';" ;
//Run query
$result = $mysqli->query($query,MYSQLI_STORE_RESULT);
$errNum = $mysqli->errno;
if ($errNum) {
	printf("<div class='errorMsg'>Error in query: %s </div> <br />",$mysqli->error);

}
else {
    $row = $result->fetch_row();
    $coord = new coordinate();
    $coord->lat = $row[0];
    $coord->lng = $row[1];
    
    //send latitude and longitude for location
     echo json_encode($coord);
      
}

// Close the database connection
$mysqli->close();

?>