import io
import picamera
import cv2
import numpy
import urllib.request
import urllib.parse
import hashlib
import requests

import socket
from threading import Thread
from socketserver import ThreadingMixIn

class ClientThread(Thread):
 

	def cam_run(self, message):
		loopNum = 3
		overlap = 0

		#Stall_A Parameters
		stallA_ystart = 0
		stallA_yheight = 480
		stallA_xstart = 0
		stallA_xwidth = 640 + overlap

		#Stall_B Parameters
		stallB_ystart = 0
		stallB_yheight = 480
		stallB_xstart = 140 - overlap
		stallB_xwidth = 360 + overlap * 2
		
		lotID = '20499'
		status = 'true'
		salt = "c75aa354fcf34894b997bcf367caae32"
		url = 'http://g13capstone.000webhostapp.com/update_stalls2.php?'
		
		if (message == '1 on'):
			currStall_ystart = stallA_ystart
			currStall_yheight = stallA_yheight
			currStall_xstart = stallA_xstart
			currStall_xwidth = stallA_xwidth
			stallNum = 1
			stallID = "001"
			
		elif (message == '2 on'):
			currStall_ystart = stallB_ystart
			currStall_yheight = stallB_yheight
			currStall_xstart = stallB_xstart
			currStall_xwidth = stallB_xwidth
			stallNum = 2
			stallID = "002"		
			
		else:
			currStall_ystart = 0
			currStall_yheight = 0
			currStall_xstart = 0
			currStall_xwidth = 0
			stallNum = 0
			stallID = "001"
			
		strSent = lotID+stallID+status+salt
		input = bytearray(strSent, encoding = "ascii")
		hash_object = hashlib.sha256(input)
		hex_dig = hash_object.hexdigest()
		payload = {'lot_id':lotID, 'stall_id':stallID, 'new_status': status, 'hash': hex_dig}
		positives = 0
		
		for i in range(0, loopNum):

			#Create buffer stream to contain image
			stream = io.BytesIO()
			#Capture image 
			#Specify other parameters here 
			with picamera.PiCamera() as camera:
				#camera.vflip = True
				#camera.hflip = True
				camera.resolution = (640, 480)
				camera.capture(stream, format = 'jpeg')
				

			#Convert the picture to numpy array
			buffer = numpy.fromstring(stream.getvalue(), dtype=numpy.uint8)

			#Create OpenCV image (blur image for noise reduction)
			image = cv2.imdecode(buffer,1)
			image = cv2.blur(image,(5,5))
			
			#Convert to Grayscale for analysis
			grayImage = cv2.cvtColor(image, cv2.COLOR_BGR2GRAY)
			
			#Load object classifier cascade for detecting Cars
			#carCascade = cv2.CascadeClassifier('/home/pi/opencv-3.1.0/data/haarcascades/myhaar(newbatch pos2900 neg1500).xml') #Put path in the parantheses
			carCascade = cv2.CascadeClassifier('/home/pi/opencv-3.1.0/data/lbpcascades/lbpClassifierC5S.xml') #Put path in the parantheses


			#Set-up Region of interests (for example 2 lots)
			
			stall_roi = grayImage[currStall_ystart:currStall_ystart+currStall_yheight, currStall_xstart:currStall_xstart+currStall_xwidth]
			cv2.rectangle(image, (currStall_xstart,currStall_ystart),(currStall_xstart+currStall_xwidth, currStall_ystart+currStall_yheight), (255,0,0), 2)
			

			#Look for cars in the image using the loaded cascade file
			detectedCars = carCascade.detectMultiScale(stall_roi, 1.05, 5)
			
			if(len(detectedCars) > 0):
				positives = positives +1
				print( 'Found car in stall '+str(stallNum)+': ' + str(len(detectedCars)))
			else:
				print( 'No car found in stall '+str(stallNum))
			
			
			
			#Draw a rectangle around every cars
			for (x,y,w,h) in detectedCars:
				cv2.rectangle(image, (currStall_xstart+x,currStall_ystart+y),(currStall_xstart+x+w, currStall_ystart+y+h), (255,0,0), 2)
			
			#Save image for viewing
			cv2.imwrite('stall'+str(stallNum)+'_pic'+str(i)+'.jpg', image)			
			
		if(positives >= 2):
		#update server here
			print('positives: ' + str(positives))
			r = requests.post(url, data = payload)
			#f = urllib.request.urlopen(url)
		#else:
			#ignore

	def __init__(self, ip, port):
		Thread.__init__(self)
		self.ip = ip
		self.port = port
		print ('[+] New server socket thread started for ' + ip + ':' +str(port))
	
	def run(self):
                #data = conn.recv(2048)
                #msg = data.decode('UTF-8')
                
                #while (msg != 'quit'):
                #    print ('Server received data:' + msg)
                #    if (msg == ('1 on') or msg== ('2 on') or msg == ('3 on') or msg == ('4 on')):
                #                self.cam_run(msg)
                #    data = conn.recv(2048)
                #    msg = data.decode('UTF-8')
                #conn.close()
		
		while True:
			data = conn.recv(2048)
			if (not(data is None)): 
                            msg = data.decode('UTF-8')
                            print ('Server received data:' + msg)
                            if msg == 'quit':
                                conn.close()
                                print('closing connection')
                                break
			
                            if (msg == ('1 on') or msg== ('2 on')):
                                self.cam_run(msg)
                        
			
                                

			


#TCP_IP = '192.168.0.20'
TCP_IP = '192.168.43.210'
TCP_PORT = 4602
BUFFER_SIZE = 20 #usually 1024, but we need quick responses

tcpServer = socket.socket (socket.AF_INET, socket.SOCK_STREAM)
tcpServer.setsockopt(socket.SOL_SOCKET, socket.SO_REUSEADDR, 1)
tcpServer.bind((TCP_IP, TCP_PORT))
threads = []

while True:
	tcpServer.listen(2)
	print ('Multithreaded Python server : Waitng for connections from TCP clients...')
	(conn, (ip, port)) = tcpServer.accept()
	newthread = ClientThread(ip, port)
	newthread.start()
	threads.append(newthread)

for t in threads:
	t.join()
