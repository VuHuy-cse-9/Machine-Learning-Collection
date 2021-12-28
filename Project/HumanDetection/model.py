#--------------------------------------------------------------------
# Imports
import pyrebase
import firebase_admin
from firebase_admin import credentials
from firebase_admin import firestore
from datetime import datetime
import time
#--------------------------------------------------------------------
print("Libary")
# Variables & setup

config = {
  "apiKey": "AIzaSyAGgTYZR0Jd6ZS58onJQ_SMdDaM6KOMl2k",
  "authDomain": "human-detection-66dce.firebaseapp.com",
  "projectId": "human-detection-66dce",
  "storageBucket": "human-detection-66dce.appspot.com",
  "serviceAccount": "serviceAccountKey.json",
  "databaseURL": ""
}

start = time.time()
#firebase_storage = pyrebase.initialize_app(config)
start1 = time.time()
cred = credentials.Certificate("serviceAccountKey.json")
start2 = time.time()
firebase_admin.initialize_app(cred)
start3 = time.time()
#storage = firebase_storage.storage()
start4 = time.time()
firestore_db = firestore.client()
start5 = time.time()

collection_id = u"image-path"
document_id = u"pfHU0uZkrCShAoN2PzKg"

#---------------------------------------------------------------------
print("Variable")
#print("firebase_storage: {}".format(start1 - start))
print("cred: {}".format(start2 - start1))
#Get reference:
doc_ref = firestore_db.collection(collection_id).document(document_id)
#Get date:
date_time = datetime.now().strftime(u"%d/%m/%Y %H:%M:%S")
#Name for firestore's data:
name = firestore_db.field_path(u"linux " + date_time)
print(name)
#Update image path to firestore
doc_ref.update({name: "huy-family/linux.png"})
# Unloading And Downloading Images
#storage.child("linux.png").put("huy-family/linux.png")