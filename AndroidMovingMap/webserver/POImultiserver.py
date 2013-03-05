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
                    break

                print "\tSS DEBUG(%d): got '%s' <> 'hello'" %(self.connectionNumber, dataCombined)

            t1 = threading.Thread(target = self.POISocketListener)
            t1.start()


            t2 = threading.Thread(target = self.POISocketSender)
            t2.start()

            t1.join()
            t2.join(0.1)    # After listen thread has died kill send thread after 0.1 seconds

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

                    if tempData == "sendPOI":
                        self.sendingData = True
                        continue

                    if tempData == "stopPOI":
                        self.close = True
                        self.sendingData = False
                        print "\tPSL exiting from stopPOI (%d)" %(self.connectionNumber)
                        return

                    try:
                        tempPOI = poi.POI(tempData)
                        tempPOI.setUID(nextUIDgenerator.next())
                        POIelements[tempPOI.getUID()] = tempPOI
                        print "\tparsed to:", tempPOI
                    except Exception as e:
                        print "\tPSL failed to parse data: ", e

        except KeyboardInterrupt:
            print "\tPSL broke by KeyboardInterrupt (%d)" %(self.connectionNumber)
        except Exception as e:
            print "\tPSL broke by some random exception or interrupt (%d)" %(self.connectionNumber)
            print e

        
    def POISocketSender(self):
        try:
            print "\tin POISocketSender"
            messageNum = 1

            while self.sendingData != True and self.close == False:
                time.sleep(.1)

            while self.sendingData == True:
                jsonPOI = json.dumps({"POI":dict([(uid,value.toDict()) for uid,value in POIelements.items()])})

                print "\tSS sending ({0}): {1} ({2} points) for connection {3} ".format(messageNum, "omitted", len(POIelements), self.connectionNumber)
                self.clientsocket.sendall(jsonPOI + "\n")

                messageNum += 1
                time.sleep(1.375 + (random.random() / 4.0))
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

