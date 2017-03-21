#!/usr/bin/env python

from vicci.srv import *
import rospy
import os
import glob

def handle_add_two_ints(req):
	#return "newMap.yaml"
	os.chdir("/opt/map")
	return glob.glob("*.yaml")
		
    #return "Hallo Es geht juhuuu"
 
def add_two_ints_server():
    list = os.listdir('/opt/map')
    if(len(list)>2):
	rospy.logerr("The folder /opt/map contains more files then just the 2 map files!")
	exit()
    rospy.init_node('map_name_server')
    s = rospy.Service('get_map_name', StringResponse, handle_add_two_ints)
    print "Map Name Server started."
    rospy.spin()
 
if __name__ == "__main__":
    add_two_ints_server()
