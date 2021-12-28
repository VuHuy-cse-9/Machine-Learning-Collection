from imutils.video import VideoStream
import imagezmq
import argparse
import socket
import time

#Construct argument parser and parse the agruments
ap = argparse.ArgumentParser()
ap.add_argument("-s", "--server-ip", required=True,
                help="ip address of the server to which the client will connect")
args = vars(ap.parse_args())

#initialize the ImageSender object with the socket address 
# of the server
sender = imagezmq.ImageSender(connect_to="tcp://{}:5555".format(
    args["server_ip"]))

#Get the host name, initialize the video stream, and allow the
#Camera sensor to warmup
rpiName = socket.gethostname()
vs = VideoStream(usePiCamera=True, resolution=(512,512)).start()

time.sleep(2.0)

while True:
    #read the frame.from the camera and send it to the server
    frame = vs.read()
    sender.send_image(rpiName, frame)