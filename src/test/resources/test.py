#!/usr/bin/env python

import sys
import zmq
import argparse

parser = argparse.ArgumentParser(description='Script to recieve messages over ZeroMQ')
parser.add_argument('-v', '--verbose', help='print out messages recieved in full', action="store_true")
args = parser.parse_args()

context = zmq.Context()

# Socket to receive messages on
receiver = context.socket(zmq.PULL)
receiver.bind("tcp://*:2120")
n = 0

try:
    while True:
        s = receiver.recv()
        n = n + 1
        if args.verbose:
            print s
        else:
            sys.stdout.write('.')
        sys.stdout.flush()
        
except KeyboardInterrupt:
    print "Exiting..."
    print "Received",n,"messages"
    receiver.close()
    context.term()
    print "Bye!"
