import cv2 as cv

#Get camera's video
capture = cv.VideoCapture(0)

#Initialize model

while True:
    isTrue,frame = capture.read()
    cv.imshow("Detect",frame)
    #Frame properties
    width = frame.shape[1]
    height = frame.shape[0]
    
    #Detecting human
    
    #Draw rectangle if detect human:
    # for (x, y, w, h) in human_rect:
    #     cv.rectangle(
    #         img=frame, 
    #         pt1=(x,y),
    #         pt2=(x+w, y+h),
    #         color=(0,255,0),
    #         thickness=2
    #     )
    
    
    if cv.waitKey(20) & 0xFF==ord('d'):
        break
    
capture.release()
cv.destroyAllWindows()
cv.waitKey(0)