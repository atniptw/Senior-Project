#!/usr/bin/env python

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

