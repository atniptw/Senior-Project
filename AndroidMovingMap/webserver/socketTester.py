#!/usr/bin/env python

'''
import socket

host = 'localhost'
port = 5047
size = 1024

s = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
s.connect((host,port))

message = "Hello darkness my old friend"
while ("quit" not in message) and ("exit" not in message):
    print "sending '%s'" %(message)
    s.send(message)
    data = s.recv(size)
    print "recieved '%s'" %(data)
    message = raw_input("next:")

s.close()
'''


import socket 
import threading
import time

HOST = ''   # what to allow connections from
PORT = 5047
ADDR = (HOST, PORT)

class Client(threading.Thread):

    def __init__(self):
        self.socket = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
        self.socket.bind(ADDR)
        self.socket.listen(1)

        self.clientsocket, self.addr = self.socket.accept()

        print 'Client connected to server'
        print 'from', self.addr

        t = threading.Thread(target = self.read)
        t.start()

        t2 = threading.Thread(target = self.write)
        t2.start()

    def read(self):
        while True:
            data = self.clientsocket.recv(1024)
            if data:
                print('Received:', data)


    def write(self):
        while True:
            time.sleep(10.0)
            print "sending -hello-"
            self.clientsocket.sendall('hello\n')


try:
    client = Client()
except KeyboardInterrupt:
    client.socket.close();
    print "\tclient broke by KeyboardInterrupt (%d)" %(connectionNumber)


