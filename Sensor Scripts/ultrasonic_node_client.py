#Managing errors in python socket programming
 
import socket   #for sockets
import sys  #for exit
import RPi.GPIO as GPIO 
import time 
import urllib.request 
import urllib.parse 
import hashlib
import requests

 
try:
    #create an AF_INET, STREAM socket (TCP)
	sock_obj = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
except socket.error as err_msg:
    print ('Unable to instantiate socket. Error code: ' + str(err_msg[0]) + ' , Error message : ' + err_msg[1])
    sys.exit()
 
print ('Socket Initialized')

#host = '192.168.0.20'  #'192.168.0.20' '140.193.4.33'
host = '192.168.43.210'
port = 4602

try:
	#attempt to connect to server
	sock_obj.connect((host,port))
except socket.error as err_msg:
	print ('Unable to connect to server. Error code: ' + str(err_msg[0]) + ', Error message : ' + err_msg[1])
	sys.exit()


GPIO.setmode(GPIO.BCM) 
TRIG = 23 
ECHO = 24
#TRIG1 = 17
#ECHO1 = 27
#TRIG2 = 5
#ECHO2 = 6
#TRIG3 = 13
#ECHO3 = 26
STALL_NUM = 2
state = [False] * STALL_NUM

def calcDistance(TRIG, ECHO, stall):
	GPIO.setup(TRIG, GPIO.OUT)
	GPIO.setup(ECHO, GPIO.IN)
	global state 
	
	GPIO.output(TRIG, False)
	#print( "Waiting for sensor to settle")
	
	time.sleep(2)
  
	GPIO.output(TRIG, True)
	time.sleep(0.00001)
	GPIO.output(TRIG, False)
  
	while GPIO.input(ECHO)==0:
		pulse_start = time.time()
  
	while GPIO.input(ECHO)==1:
		pulse_end = time.time()
  
	pulse_duration = pulse_end - pulse_start
  
	distance = pulse_duration * 17150
  
	distance = round(distance, 2)
	
	salt = 'c75aa354fcf34894b997bcf367caae32'
	lotID = '20499'
	stallID = '00' + str(stall)
	status = 'true'
	
	if distance < 130 and state[stall] == False:
		message = str(stall) + ' on'
		sock_obj.send((message).encode('UTF-8'))
		time.sleep(2)
		state[stall] = True
	
	elif distance >= 130 and state[stall] == True:
		strSent = lotID+stallID+'false'+salt
		state[stall] = False
		input = bytearray(strSent, encoding ="ascii")
		hash_object = hashlib.sha256(input)
		hex_dig = hash_object.hexdigest()

		#url = 'http://g13capstone.000webhostapp.com/update_stalls2.php?lot_id=' + lotID+ '&stall_id='+stallID+'&new_status=false&hash='+hex_dig

		#f = urllib.request.urlopen(url)
		payload = {'lot_id': '20499', 'stall_id': stallID, 'new_status':'false', 'hash': hex_dig}
		r = requests.post("http://g13capstone.000webhostapp.com/update_stalls2.php", data=payload)
		#print ("Distance:", distance, "cm")
	
	return distance

print( "Distance Measurement in Progress") 

#stall = str(sys.argv[1])
var = 1

while var == 1:

	
	distance1 = calcDistance(TRIG, ECHO, 1)
	#distance2 = calcDistance(TRIG1, ECHO1, 2)
	#distance3 = calcDistance(TRIG2, ECHO2, 3)
	#distance4 = calcDistance(TRIG3, ECHO3, 4)
	print('Stall 1: ' + str(distance1))
	#print('Stall 2: ' + str(distance2))
	#print('Stall 3: ' + str(distance3))
	#print('Stall 4: ' + str(distance4))


GPIO.cleanup()

print ('Client has closed the connection')
sock_obj.close()
