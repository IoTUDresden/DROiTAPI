
Folders
-------
launch: Contains launchfiles for ROS
nodes: Contrains ROS-Nodes that are necessary for our Robot API
script: Script to launch ROS
maps: Contains maps


Steps to setup the youbot for our robot API:
-----------------------------------------------
<1> Switch to your ROS-Workspace and create a ROS-Package called vicci_turtle:

	cd ~/ros_workspace
	roscreate-pkg vicci_youbot

<2> If not already happened add the workspace to the ROS_PACKAGE_PATH. In ~/.bashrc add the following line:

	export ROS_PACKAGE_PATH=$ROS_PACKAGE_PATH:/home/youbot/ros_workspace/

<3> 	source ~/.bashrc

<4> Add all nodes and the launch folder to the new Package.

<5> Use the script for downloading the map from a server and for starting ros
