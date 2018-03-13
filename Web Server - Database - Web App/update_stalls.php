<?php

$lot_id = $_POST['lot_id'];
$stall_id = $_POST['stall_id'];
$new_status = $_POST['new_status'];

$mysqli = new mysqli();

// Connect to database
require 'db_login.php';


// Build query to insert product
$query = "UPDATE ParkingStalls SET status=". $new_status ." WHERE lot_id='". $lot_id . "' AND stall_id='" . $stall_id . "';";
//printf("%s",$query);
//Run query
$result = $mysqli->query($query,MYSQLI_STORE_RESULT);
$errNum = $mysqli->errno;


if ($errNum) {
    printf("ERROR: Problem with update");

}
else {

	printf("Stall has been updated");

}


// Close the database connection
$mysqli->close();

?>