<?php

$lot_id = $_GET['lot_id'];

$mysqli = new mysqli();

// Connect to database
require 'db_login.php';

class commentInfo{
    
    public $username = "";
    public $logtime = "";
    public $comment = "";
    
}

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
		echo "No comments have been submitted";
	}
	else {
	    
	    $comment = array();
	    
		for ($i = 0; $i < $num_rows; $i++) {
			$comment_info = new commentInfo();
			$row = $result->fetch_row();
			$comment_info->username = $row[0];
			$comment_info->logtime = $row[1];
			$comment_info->comment = $row[2];
			
			array_push($comment, $comment_info);
		}	
		
		echo json_encode($comment);
	}
	
	
}


// Close the database connection
$mysqli->close();
?>