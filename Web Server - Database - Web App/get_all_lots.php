<?php
                $mysqli = new mysqli();

                require 'db_login.php';


                // Build query to insert product
                $query = "select L.lot_id, lot_name, location, price, temp, count(S.stall_id) as capacity, count(CASE WHEN S.status THEN 1 END) as taken from ParkingLots L, ParkingStalls S where L.lot_id = S.lot_id group by S.lot_id;" ;
                
                //Run query
                $result = $mysqli->query($query,MYSQLI_STORE_RESULT);
                $errNum = $mysqli->errno;
                if ($errNum) {
                        printf("<div class='errorMsg'>Error in query: %s </div> <br />",$mysqli->error);
                }
                else {
                        echo "<div class='table-bordered'>";
		                echo "<table class='table table-hover'>";
                        echo "<thead>";
                        echo "<tr>";
                        echo "<th>Parking Lot</th>";
                        echo "<th>Location</th>";
                        echo "<th>Price</th>";
                        echo "<th>Temperature</th>";
                        echo "<th>Capacity</th>";
                        echo "<th>Available Stalls</th>";
                        echo "</tr>";
                        echo "<tbody>";
                        //output result
                        $num_rows = $result->num_rows;
                        for ($i = 0; $i < $num_rows; $i++) {
                            $row = $result->fetch_row();
                            $lot_id = $row[0];
                            $lot_name = $row[1];
                            $location = $row[2];
                            $price = $row[3];
                            $temp = $row[4];
                            $capacity = $row[5];
                            $available = $capacity - $row[6];
                            echo "<tr>";
                            
                            echo "<td><a href='./lot_view.php?lot_id=". $lot_id . "'>" . $lot_name . "</a></td>";
                            echo "<td><a href='#' class='lotLocations'>" .$location. "</a></td>";
                            echo "<td>" .$price. "</td>";
                            echo "<td>" .$temp. "</td>";
                            echo "<td>" .$capacity. "</td>";
                            echo "<td>" .$available. "</td>";
                            echo "</tr>";
                        }	
                        echo "</tbody>";
                        echo "</table>";
                        echo "</div>";
                }
                // Close the database connection
                $mysqli->close();
            ?>