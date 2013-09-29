#!/usr/bin/env python

import zmq

context = zmq.Context()

# Socket to receive messages on
receiver = context.socket(zmq.PULL)
receiver.bind("tcp://*:2120")
n = 0

while True:
    s = receiver.recv()
    n = n + 1
    print(n, s)
