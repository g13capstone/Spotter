<?php
// Connect to database
$mysqli->connect("localhost","id3240775_spotteradmin","ece4600capstone","id3240775_spotterdb");
if ($mysqli->errno) {
    printf(" <div class='errorMsg'>Error connecting to database: %s</div> <br />",$mysqli->error);
    exit();
}
?>