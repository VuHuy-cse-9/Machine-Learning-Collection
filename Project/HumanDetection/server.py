from imutils import build_montages
from datetime import datetime
import numpy as np
import imagezmq
import argparse
import imutils
import cv2

#Initialize the ImageHub object
imagehub = image.zmq.ImageHub()

#Start looping all the frame:
while True:
    (rpiName, frame) = imageHub.recv_image()
    imagehub.send_reply(b"OK")
    
    if rpiName not in lastActive.keys():
        print("[INFO] receiving data from {}...".format(rpiName))
    lastActive[rpiName] = datetime.now()
    cv2.imshow("Video from Pi", frame)
    key = cv2.waitKey(1) & 0xFF
    if key == ord("q"):
    	break
    if ((datetime.now() - lastActiveCheck).seconds > ACTIVE_CHECK_SECONDS):
        for (rpiName, ts) in list(lastActive.items()):
            if (datetime.now() - ts).seconds > ACTIVE_CHECK_SECONDS:
                print("[INFO] lost connection to {}".format(rpiName))
                lastActive.pop(rpiName)
                frameDict.pop(rpiName)
        lastActiveCheck = datetime.now()
cv2.destroyAllWindows()