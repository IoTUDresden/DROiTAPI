#!/usr/bin/env python 
import roslib
import rospy
from std_msgs.msg import String
from geometry_msgs.msg import PoseStamped, Pose, Point, Quaternion 
import math
import tf
import turtlesim.msg
import turtlesim.srv

if __name__ == '__main__':
    pub = rospy.Publisher('tf_odom_to_map_transform', Pose)
    rospy.init_node('tf_odom_to_map_publisher')
    listener = tf.TransformListener()
    rate = rospy.Rate(10.0)

    while not rospy.is_shutdown():
        try:
		(trans,rot) = listener.lookupTransform('/map', '/odom', rospy.Time(0))
		my_pose = Pose()
		my_pose.position.x = trans[0]
		my_pose.position.y = trans[1]
		my_pose.position.z = trans[2]		
		my_pose.orientation.x = rot[0]
		my_pose.orientation.y = rot[1]
		my_pose.orientation.z = rot[2]
		my_pose.orientation.w = rot[3]
		pub.publish(my_pose)
    		rospy.sleep(1.0)
        except (tf.LookupException, tf.ConnectivityException, tf.ExtrapolationException):
            continue

