#!/usr/bin/env python

import socket
import threading

import time
import datetime
import random

import json
import poi
from POIData import POIelements, nextUIDgenerator

BUFFERSIZE = 1024

HOST = ''   # what to allow connections from
PORT = 5047
ADDR = (HOST, PORT)

class ServerSocket(threading.Thread):
    def __init__(self, connectionNumber, clientsocket, addr):
        threading.Thread.__init__(self)
        self.connectionNumber = connectionNumber
        self.clientsocket = clientsocket
        self.addr = addr

        self.sendGPS = True
        self.GPSCords = {"latitude":39.4826527, "longitude":-87.3304517}

        self.overlays = set(["type 1", "type 2"])
        self.lastSync = -1.0

        self.sendingData = False
        self.close = False


    def run(self):
        dataBlock = 1
        try:
            while 1:
                data = self.clientsocket.recv(BUFFERSIZE)
                if not data:
                    print "\n\tSS Data empty - closing %d" %(self.connectionNumber)
                    break

                if data == "hello":
                    print "\tSS DEBUG(%d): received 'hello', time to ACK" %(self.connectionNumber)
                    self.clientsocket.sendall("ACKhello\n")

                    if self.sendGPS:
                        print "\tSS DEBUG(%d): sendGPS = true, sending GPS" %(self.connectionNumber)
                        jsonGPS = json.dumps({"GPS":self.GPSCords})
                        self.clientsocket.sendall(jsonGPS + "\n")
                    break

                print "\tSS DEBUG(%d): got '%s' <> 'hello'" %(self.connectionNumber, dataCombined)

            t1 = threading.Thread(target = self.POISocketListener)
            t1.start()

            t2 = threading.Thread(target = self.POISocketSender)
            t2.start()

            t1.join()
            t2.join(0.1)    # After listen thread has [ended, died]
                            # [kill, collect] send thread within 0.1 seconds

            print "\tSS DEBUG(%d): threads joined time to close" %(self.connectionNumber)
            self.clientsocket.close()
        except KeyboardInterrupt:
            print "\tserverSocket broke by KeyboardInterrupt (%d)" %(self.connectionNumber)
        except Exception as e:
            print "\tserverSocket broke by some random exception or interrupt (%d)" %(self.connectionNumber)
            print e

    def POISocketListener(self):
        try:
            print "\tin POISocketListener"
            while True:
                data = self.clientsocket.recv(BUFFERSIZE)
                if not data:
                    print "\n\tPSL Data empty - closing %d" %(self.connectionNumber)
                    break

                print "PSL %d received data: \"%s\"" %(self.connectionNumber, data)
                for tempData in data.split("\n"):
                    if tempData == "":
                        continue

                    if tempData == "close":
                        self.close = True
                        print "\tPSL exiting from stopPOI (%d)" %(self.connectionNumber)
                        return

                    if tempData == "sendPOI":
                        self.sendingData = True
                        continue

                    if tempData == "stopPOI":
                        self.sendingData = False
                        continue

                    if tempData.startswith("addOverlay:") or tempData.startswith("removeOverlay:"):
                        # partition returns 3-tuple (before, seperator, after)
                        action, sep, overlayName = tempData.partition(":")

                        if action.startswith("add"):
                            self.overlays.add(overlayName)
                        else:
                            # remove throws error if not present, discard does not
                            self.overlays.discard(overlayName)    

                        # resync all points (to force new overlay points sync)
                        self.lastSync = -1.0
                        continue

                    if tempData.startswith("addPoint:") or tempData.startswith("removePoint:"):
                        # partition returns 3-tuple (before, seperator, after)
                        action, sep, point = tempData.partition(":")
                        try:
                            tempPOI = poi.POI(point)

                            if action.startswith("add"):
                                tempPOI.setUID(nextUIDgenerator.next())
                                POIelements[tempPOI.getUID()] = tempPOI
                                print "\tadded:", tempPOI, "as UID:", tempPOI.getUID()
                            else:
                                removed = POIelements.pop(tempPOI.getUID(), "not present")
                                if removed != "not present":
                                    print "\tremoved POI with UID:", tempPOI.getUID()
                                else:
                                    print "\tfailed to remove POI:", tempPOI, POIelements.keys()

                        except Exception as e:
                            print "\tPSL failed to parse data: ", e
                        continue

        except KeyboardInterrupt:
            print "\tPSL broke by KeyboardInterrupt (%d)" %(self.connectionNumber)
        except Exception as e:
            print "\tPSL broke by some random exception or interrupt (%d)" %(self.connectionNumber)
            print e

        
    def POISocketSender(self):
        try:
            print "\tin POISocketSender"
            messageNum = 1

            while self.close == False:
                while self.sendingData != True and self.close == False:
                    time.sleep(0.1)

                while self.sendingData == True and self.close == False:
                    # TODO only sent requested types
                    syncTime = time.time()
                    POIToSend = [(uid, point.toDict()) for uid, point in POIelements.items() if point.getTimestamp() >= self.lastSync] # and point.type in self.overlays]
                    jsonPOI = json.dumps({"POI":dict(POIToSend)})

                    print "\tSS message #{0} : {1} ({2} points, connection {3}, lastSync {4})".format(
                        messageNum, "not shown", len(POIToSend), self.connectionNumber, self.lastSync)

                    self.lastSync = syncTime
                    self.clientsocket.sendall(jsonPOI + "\n")

                    messageNum += 1
                    time.sleep(3.875 + (random.random() / 4.0)) # 4 second average pause


        except KeyboardInterrupt:
            print "\tPSS broke by KeyboardInterrupt (%d)" %(self.connectionNumber)
        except Exception as e:
            print "\tPSS broke by some random exception or interrupt (%d)" %(self.connectionNumber)
            print e


def main():
    HOST = ''   # what to allow connections from
    PORT = 5047
    ADDR = (HOST, PORT)

    try:
        serversock = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
        serversock.bind(ADDR)
        serversock.listen(1)

        connectionNumber = 1

        ss = []
        while 1:
            print "waiting for connection (number %d)" %(connectionNumber)
            clientsocket, addr = serversock.accept()
            print "\t%s at %s (connection %d)" %(addr, datetime.datetime.now(), connectionNumber)

            SS = ServerSocket(connectionNumber, clientsocket, addr)
            SS.start()  #split and run
            ss.append(SS)


            connectionNumber += 1

    except KeyboardInterrupt:
        print "Kill received, shutting down"
        serversock.close()

if __name__ == '__main__':
    main()

