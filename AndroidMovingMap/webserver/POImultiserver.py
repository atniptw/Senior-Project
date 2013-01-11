#!/usr/bin/env python

import json
import socket
import thread

import datetime
import time
import math
import random

class POI:
    def __init__(self, UID, name, latitude, longitude, attributes = {}, POItype = 0):
        self.attributes = attributes
        self.attributes["UID"] = UID
        self.attributes["name"] = name
        self.latitudeF = latitude
        self.longitudeF = longitude
        self.attributes["POItype"] = POItype

    def toDict(self):
        newAttr = self.attributes
        newAttr["latitude"] = self.latitudeF(time.time())
        newAttr["longitude"] = self.longitudeF(time.time())
        return newAttr

    def getUID(self):
        return self.attributes["UID"]

    def getName(self):
        return self.attributes["name"]

    def __eq__(self, other):
        if type(other) is type(self):
            return self.getUID() == other.getUID()
        return False

    def __ne__(self, other):
        return not self.__eq__(other)

    def __hash__(self):
        return self.getUID()

    def __repr__(self):
        return str(self.toDict())

rose = POI(0, "Rose",
    lambda t:40.0,
    lambda t:-88.0, 
    {"desc":"rose"},
    0)
fred = POI(1, "Fred",
    lambda t: 10*math.cos(6.28*t/10.0),
    lambda t:0,
    {},
    0)
bob = POI(2, "Bob",
    lambda t:0,
    lambda t: 10*math.sin(6.28*t/15.0),
    {"describtion":"Bob"},
    0)
drogo = POI(3, "Drogo",
    lambda t:-25+15*math.sin(6.28*t/15.0),
    lambda t:45+20*math.cos(6.28*t/15.0),
    {"manlevel":"15"},
    0)

POIelements = {rose.getUID():rose, 
                fred.getUID():fred,
                bob.getUID():bob,
                drogo.getUID():drogo}


def serverSocket(connectionNumber, clientsocket, addr):
    BUFFERSIZE = 1024
    print "\tConnected by %s (connection %d)" %(addr, connectionNumber)

    dataBlock = 1
    try:
        while 1:
            data = clientsocket.recv(BUFFERSIZE)
            if not data:
                print "\n\tData empty - closing %d" %(connectionNumber)
                break

            # start of socket (???) FIXME
            if data in ('\xac\xed', '\x00\x05'):
                print "\tDEBUG: recieved start of connection for #%d" %(connectionNumber)
                clientsocket.sendall(data)
                continue

            if data == 't\x00\x06hello\n':
                print "\tDEBUG: recieved 'hello' for connection %d" %(connectionNumber)
                clientsocket.sendall('t\x00\x09ACKhello\n')
                break

        messageNum = 1
        while 1:
            jsonPOI = json.dumps({"POI":dict([(uid,value.toDict()) for uid,value in POIelements.items()])})
            length = len(jsonPOI)

            print "\tsending ({0}): {1} for connection {2}".format(messageNum, "ommitted", connectionNumber)

            clientsocket.sendall('t' + chr(length/256) + chr(length%256) + jsonPOI)

            messageNum += 1
            time.sleep(0.25 + (random.random() / 4.0))

    except KeyboardInterrupt:
        print "\tbroke by KeyboardInterrupt (%d)" %(connectionNumber)
    except Exception as e:
        print "\tbroke by some random exception or interrupt (%d)" %(connectionNumber)
        print e

    clientsocket.close()


def main():
    HOST = ''   # what to allow connections from
    PORT = 5047
    ADDR = (HOST, PORT)

    try:
        serversock = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
        serversock.bind(ADDR)
        serversock.listen(1)

        connectionNumber = 1
        while 1:
            print "waiting for connection (number %d)" %(connectionNumber)
            clientsock, addr = serversock.accept()
            print "\t%s at %s (connection %d)" %(addr, datetime.datetime.now(), connectionNumber)

            thread.start_new_thread(serverSocket, (connectionNumber, clientsock, addr))

            connectionNumber += 1

    except KeyboardInterrupt:
        print "Kill received, shutting down"
        serversock.close()

if __name__ == '__main__':
    main()

