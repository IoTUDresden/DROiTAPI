#!/usr/bin/env python
import rospy
from geometry_msgs.msg import Twist

from threading import Timer

pub = rospy.Publisher('/cmd_vel', Twist)

def stop():
        twist = Twist()
	twist.linear.x = 0
	twist.linear.y = 0
	twist.linear.z = 0
	twist.angular.x = 0
	twist.angular.y = 0
	twist.angular.z = 0
	pub.publish(twist)
	rospy.loginfo('Sended Stop Command')

t = Timer(1,stop)

def callback(data):
	global t
	t.cancel()
	pub.publish(data)
	t = Timer(2,stop)
	t.start()

rospy.init_node('youbot_stopper',anonymous=True)
rospy.Subscriber("/mobile_base/commands/velocity",Twist,callback)
rospy.loginfo('Youbot Stopper Node started')
rospy.spin()
