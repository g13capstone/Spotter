<?php
$lot_id = $_GET['lot_id'];


$mysqli = new mysqli();

require 'db_login.php';


// Build query to insert product
$query = "select temp from ParkingLots L where L.lot_id = " . $lot_id;

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
        $temp = $row[0];
           
    }
    
    
    if ($temp < -25) {
        printf("<div class='row'>");
        printf("<div align='center' class='col-md-12'>");
        printf("<div class='panel-group'>");
        printf("<div class='panel panel-warning'>");
        printf("<div class='panel-heading'>WARNING</div>");
        printf("<div class='panel-body'>");
        printf("Temperature has fallen below -25 Degrees C. Information presented may be inaccurate");
        printf("</div>"); //end of panel body
        printf("</div></div>"); //end of panel
        printf("</div>"); //end of col
        printf("</div>"); //end of row
    }
    
    
    
}

// Close the database connection
$mysqli->close();

?>

