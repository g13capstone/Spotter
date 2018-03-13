<html>
<head>
    <title>Spotter: IoT Parking Lot Monitor</title>
    <link rel='stylesheet' type='text/css' href='./app-style.css'/>
        
    <link href="https://fonts.googleapis.com/css?family=Lato|Open+Sans" rel="stylesheet">
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css">
    <meta charset="utf-8">
    <meta name="viewport" content="initial-scale=1.0, user-scalable=no">
   
    <style>

      #map {
        height:50%;
        left: 0px;
        right:0px;
        padding: 0;
        margin:0;
                       
      }
     
    </style>
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.2.0/jquery.min.js"></script>
</head>
<body>
    <div class='container-fluid'>
    
   <?php include 'header.php' ?>
	
	
    
    <div class='row'>
        <div class='col-md-12'>    
        	<div id='lotList'>
                   
        	</div>
        </div>
    </div>
	 
	<div class='row'>
	    <div class='col-md-2'>
	        <h4>Nearby Parking Lots</h4>
	        <div id="currList"> 
            </div>
        </div>
        <div class='col-md-10'>
            <div id="map">
            </div>
        </div>
    </div>
    
       <script src="jquery-3.2.0.min.js"></script>
        <script>
        var map, infoWindow, gMarkers = [];
        var markers = {};

        function initMap() {
          map = new google.maps.Map(document.getElementById('map'), {
            //center: {lat: 49.8951, lng: -97.16384},
            center: {lat: 49.808501, lng: -97.135398},
            zoom: 14 // 16
          });

       
         
          infoWindow = new google.maps.InfoWindow;
          
          var lotList;
          
          
          
          lotList = {
            <?php
            $mysqli = new mysqli();

            require 'db_login.php';

                // Build query to insert product
                $query = "select lot_name, latitude,longitude,lot_id from  ParkingLots;";
                //Run query
                $result = $mysqli->query($query,MYSQLI_STORE_RESULT);
                $errNum = $mysqli->errno;
                if ($errNum) {
                        printf("<div class='errorMsg'>Error in query: %s </div> <br />",$mysqli->error);

                }
                else {
                    //send latitude and longitude for location
                    $num_rows = $result->num_rows;
                        for ($i = 0; $i < $num_rows; $i++) {
                            $row = $result->fetch_row();
                            $lot_name = $row[0];
                            $latitude = $row[1];
                            $longitude = $row[2];
                            $lot_id = $row[3];
                           
                            echo "lot" . ($i+1) . ": {";
                            echo "lat: " .$latitude . ",";
                            echo "lng: " .$longitude . ",";
                            echo "id: " . $lot_id . ",";
                            echo "title: '" .$lot_name . "'}";
                            
                            if ($i != ($num_rows - 1)) {
                                echo ",";
                            }
                        }
                    

                }

                // Close the database connection
                $mysqli->close();

                ?>
              
             
          };

          var count = 0, str = '';
          for (markerId in lotList) {
              markers[markerId] = createMarker(lotList[markerId]);
              gMarkers[count] = markers[markerId];
              
              str += '<li id=L' + count + '><a href="./lot_view.php?lot_id='+ lotList[markerId].id +'">' + lotList[markerId].title +'</a></li>'; 
             
              
              
              count++;
          }
          $('#currList').append(str);
          //$('#currList span li').hide();
                              
          function createMarker(data) {
              var lotWindow = new google.maps.InfoWindow;
              var myLatLng = new google.maps.LatLng(data.lat, data.lng);
              
              var marker = new google.maps.Marker({
                  position: myLatLng,
                  map: map,
                  title: data.title
              });
              
              marker.addListener('click', function() {
                  lotWindow.setContent(marker.title);
                  lotWindow.open(map, marker);
              });
              
              return marker;
        }

          // Try HTML5 geolocation.
          if (navigator.geolocation) {
            navigator.geolocation.getCurrentPosition(function(position) {
              var pos = {
                lat: position.coords.latitude,
                lng: position.coords.longitude
              };
			  
			/*
			var userMarker = new google.maps.Marker({
				position: new google.maps.LatLng(position.coords.latitude,position.coords.longitude);
				map: map,
				icon: {
					path: google.maps.SymbolPath.CIRCLE,
					scale: 10
          }

			});
			*/
          infoWindow.setPosition(pos);
              infoWindow.setContent('Location found.');
			  icon: blue_dot;
              infoWindow.open(map);
              map.setCenter(pos);
            }, function() {
              handleLocationError(true, infoWindow, map.getCenter());
            });
          } else {
            // Browser doesn't support Geolocation
            handleLocationError(false, infoWindow, map.getCenter());
          }
          
         
           google.maps.event.addListener(map, 'idle', function() {
            
              for (var i = 0; i<gMarkers.length; i++) {
                  var infoPanel = $('#L'+i);
                  if (map.getBounds().contains(gMarkers[i].getPosition())) {
                    infoPanel.show();
                  }
                  else {
                     infoPanel.hide();
                    }
                }
            
           });
        }

        function handleLocationError(browserHasGeolocation, infoWindow, pos) {
          infoWindow.setPosition(pos);
          infoWindow.setContent(browserHasGeolocation ?
                                'Error: The Geolocation service failed.' :
                                'Error: Your browser doesn\'t support geolocation.');
          infoWindow.open(map);
        }
        
        //AJAX call for lot table
         var interval;
        function get_lots() {
            clearInterval(interval);
                $.ajax({
                        type: 'GET',
                        url: 'get_all_lots.php',			
                        success: function(msg) {
                            $('#lotList').html(msg);
                        },
                        error: function(msg) {
                            $('#lotList').html("Error: lot information could not be retrieved");
                        }
                });
                interval = setInterval(get_lots, 3000);
        }
        
        function move_map() {
            map.setCenter({lat: 0, lng: 0});
        }


        $( document ).ready(function() {
               
                get_lots();	
        });
        
        $(document).on('click', ".lotLocations", function() {
                var location = $(this).text();
               
                $.ajax({
                        type: 'GET',
                        url: 'get_locations_coords.php',
                        data: 'location=' + location,
                        success: function(msg) {
                            //$('#coords').html(msg);
                            var obj = jQuery.parseJSON( msg );
                            
                            map.setCenter({lat: parseFloat(obj.lat), lng: parseFloat(obj.lng)});
                        }
                        
                });
                
               
        });
      </script>   
      <script src="https://maps.googleapis.com/maps/api/js?key=AIzaSyD7utKhOq6TyOvpSHJPclUzAPK8Axlfh9Q&callback=initMap"
          async defer>
      </script>  
     
    </div> <!-- end class=container-fluid -->  
</body>
</html>