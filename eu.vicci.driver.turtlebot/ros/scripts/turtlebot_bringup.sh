#!/bin/bash
read -p "Please enter server ip (Default:192.168.1.4):" eingabe
if [ "$eingabe" == "" ] 
then
	server_ip=192.168.1.4
else
	server_ip=$eingabe
fi
read -p "Please enter map location on server (Default:/opt/map/):" eingabe
if [ "$eingabe" == "" ] 
then
	map_location=/opt/map/
else
	map_location=$eingabe
fi
echo -ne 'Downloading map from '$server_ip':'$map_location' into '$HOME'/map/ .. '
rm ~/map/* &> /dev/null
scp $server_ip:$map_location\* ~/map &> /dev/null

if (($? > 0)); 
then
	echo "failed!"
	echo ""
	read -p "Specify local map location (Default:"$HOME"/vicci_labor_new.yaml):" eingabe
	if [ "$eingabe" == "" ] 
	then
		map_location_local=/opt/map/
	else
		map_location_local=$eingabe
	fi

else
	# SCP was successfull
	echo "done!"
	map_location_local=$HOME/map/$(ls ~/map | grep .yaml)
fi
echo 'Starting ROS with mapfile: '$map_location_local':'
roslaunch vicci_turtle start.launch map_file:=$map_location_local

