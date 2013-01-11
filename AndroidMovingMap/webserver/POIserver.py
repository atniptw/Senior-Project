#!/usr/bin/env python

import json
import socket
import time
import datetime
import math
import random

class POI:
    def __init__(self, UID, name, latitude, longitude, POItype = 0):
        self.UID = UID
        self.name = name
        self.latitude = latitude
        self.longitude = longitude
        self.POItype = POItype

    def toDict(self):
        return {"UID":str(self.UID),
                "name":str(self.name), 
                "latitude":str(self.latitude(time.time())),
                "longitude":str(self.longitude(time.time())),
                "type":str(self.POItype)}

    def getUID(self):
        return self.UID

    def getName(self):
        return self.name

    def __eq__(self, other):
        if type(other) is type(self):
            return self.UID == other.UID
        return False

    def __ne__(self, other):
        return not self.__eq__(other)

    def __hash__(self):
        return self.UID

    def __repr__(self):
        return str(self.toDict())

rose = POI(0, "Rose",
    lambda t:40.0,
    lambda t:-88.0, 0)
fred = POI(1, "Fred",
    lambda t: 10*math.cos(6.28*t/10.0),
    lambda t:0, 0)
bob = POI(2, "Bob",
    lambda t:0,
    lambda t: 10*math.sin(6.28*t/15.0), 0)
drogo = POI(3, "Drogo",
    lambda t:-25+15*math.sin(6.28*t/15.0),
    lambda t:45+20*math.cos(6.28*t/15.0), 0)

POIelements = {rose.UID:rose, fred.UID:fred, bob.UID:bob, drogo.UID:drogo}

conn = 0
s = 0
def serverSocket():
    global conn, s
    HOST = ''                 # Symbolic name meaning all available interfaces
    PORT = 5047              # Arbitrary non-privileged port
    s = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
    s.bind((HOST, PORT))
    s.listen(1)
    conn, addr = s.accept()

    print 'Connected by', addr, datetime.datetime.now()

    dataBlock = 1
    while 1:
        data = conn.recv(1024)
        if not data:
            print "\nData empty - closing"
            break
        reply = data[:]

        # start of socket (???) FIXME
        if data in ('\xac\xed', '\x00\x05'):
            print "DEBUG: recieved start of connection"
            conn.sendall(reply)
            continue

        if data in ('t\x00\x06hello\n'):
            print "DEBUG: recieved 'hello'"
            conn.sendall('t\x00\x09ACKhello\n')
            break

    status = 1
    messageNum = 1
    try:
        while 1:
            jsonPOI = json.dumps({"POI":dict([(uid,value.toDict()) for uid,value in POIelements.items()])})
            length = len(jsonPOI)

            print "sending ({0}): {1}".format(messageNum, "ommitted")

            conn.sendall('t' + chr(length/256) + chr(length%256) + jsonPOI)

            messageNum += 1
            time.sleep(0.25 + (random.random() / 4.0))

    except KeyboardInterrupt:
        print "broke by KeyboardInterrupt"
        status = 0
    except:
        print "broke by some random exception or interrupt"
        status = 1

    conn.close()
    time.sleep(0.5)
    s.close()
    time.sleep(0.5)
    return status


def main():
    
    try:
        status = 1
        while status:
            status = serverSocket()
#        server = HTTPServer(('', 8080), HelloWorldWeb)
#        print "Hello World Wide Web..."
#        server.serve_forever()

    except KeyboardInterrupt:
        print "Kill received, shutting down"
        global conn, s
        conn.close()
        time.sleep(0.5)
        s.close()
        time.sleep(0.5)
        print "killed connections"
#        server.socket.close()

if __name__ == '__main__':
    main()

