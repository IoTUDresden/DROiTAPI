#!/bin/bash
echo -n 'Waiting till wlan4 gets a valid ip ..'

HOSTNAME=` ifconfig | grep "wlan4" -A1 | grep inet\ addr | awk -F ":" '{ print $2 }' | sed -e "s/  Bcast//"`
while [ -z $HOSTNAME ]
do
	sleep 2
	HOSTNAME=` ifconfig | grep "wlan4" -A1 | grep inet\ addr | awk -F ":" '{ print $2 }' | sed -e "s/  Bcast//"`
	echo -n "."
done
echo " done"
. /opt/ros/groovy/setup.bash
. /home/turtlebot/catkin_ws/devel/setup.bash
export ROS_PACKAGE_PATH=$ROS_PACKAGE_PATH:$HOME/groovy_workspace/
export ROS_HOSTNAME=` ifconfig | grep "wlan4" -A1 | grep inet\ addr | awk -F ":" '{ print $2 }' | sed -e "s/  Bcast//"`
export ROS_MASTER_URI=http://${ROS_HOSTNAME}:11311
echo 'Using "'$HOSTNAME'" as ROS_HOSTNAME and ROS_MASTER_URI'
roslaunch vicci start.launch map_file:=/opt/map/$(ls /opt/map/ | grep .yaml)

