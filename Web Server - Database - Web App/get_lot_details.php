
<?php

$lot_id = $_GET['lot_id'];


$mysqli = new mysqli();

require 'db_login.php';


// Build query to insert product
$query = "select location, price, temp, count(S.stall_id) as capacity, count(CASE WHEN S.status THEN 1 END) as taken from ParkingLots L, ParkingStalls S where L.lot_id = " . $lot_id ." AND L.lot_id = S.lot_id group by S.lot_id;" ;

//Run query
$result = $mysqli->query($query,MYSQLI_STORE_RESULT);
$errNum = $mysqli->errno;
if ($errNum) {
    printf("<div class='errorMsg'>Error in query: %s </div> <br />",$mysqli->error);
    
}
else {
    
    //output result into html table
    $num_rows = $result->num_rows;
     
    for ($i = 0; $i < $num_rows; $i++) {
        $row = $result->fetch_row();
       
        
        $location = $row[0];
        $price = $row[1];
        $temp = $row[2];   
        $capacity = $row[3];
        $taken = $row[4];
        $availability = $capacity - $taken;
     
    }
   
    
    
    
}

// Close the database connection
$mysqli->close();

?>

<div class='panel-group'>
<div class='panel panel-info'>
	<div class='panel-heading'>
		LOT DETAILS
	</div>
	<div class='panel-body'>
		<strong> PRICE:</strong> <?php echo $price?> <br>
		<strong> TEMPERATURE:</strong> <?php  echo $temp ?> Degrees Celsius<br>
		<strong> LOCATION: </strong> <?php  echo $location ?>
		<strong> CAPACITY: </strong> <?php echo $capacity ?> <br>
		<strong> AVAILABILITY: </strong> <?php echo $availability ?> <br>
		<strong> TAKEN: </strong> <?php echo $taken ?> <br>
	</div>
</div>