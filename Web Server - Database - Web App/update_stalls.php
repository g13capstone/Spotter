<?php
//update stalls script with hash parameter

$lot_id = $_POST['lot_id'];
$stall_id = $_POST['stall_id'];
$new_status = $_POST['new_status'];
$hash = $_POST['hash'];


$salt = 'c75aa354fcf34894b997bcf367caae32';


$input = $lot_id . $stall_id . $new_status . $salt;

$calc_hash = hash('sha256', $input);

if ($calc_hash == $hash) {
    // Connect to database
    $mysqli = new mysqli();
    
    require 'db_login.php';
    
    // Build query to insert product
    $query = "UPDATE ParkingStalls SET status=". $new_status ." WHERE lot_id='". $lot_id . "' AND stall_id='" . $stall_id . "';";
    //printf("%s",$query);
    //Run query
    $result = $mysqli->query($query,MYSQLI_STORE_RESULT);
    $errNum = $mysqli->errno;
    
    
    if ($errNum) {
    	printf("Error in query: %s",$mysqli->error);
    
    }
    else {
    
    	printf("Stall status has been updated");
    
    }
    
    
    // Close the database connection
    $mysqli->close();
}
else {
    printf("ERROR: Authentication Failed");
}



?>