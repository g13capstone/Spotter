<?php
$lot_id = $_POST['lot_id'];
$username = $_POST['username'];
$comment = $_POST['comment'];

print_r(json_encode($_POST));

$mysqli = new mysqli();

require 'db_login.php';

date_default_timezone_set('America/Winnipeg');
$logtime = date('Y-m-d H:i:s');
$query = "insert into Comments values ('" . $lot_id . "', '". $username . "', '". $logtime . "', '" . $comment ."');";

$result = $mysqli->query($query,MYSQLI_STORE_RESULT);
$errNum = $mysqli->errno;


if ($errNum) {
	printf("<div class='errorMsg'>Error in query: %s </div> <br />",$mysqli->error);
	printf($query);
}
else {
	echo("Comment successfully added");
}


// Close the database connection
$mysqli->close();

?>
