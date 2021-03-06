# DROiTAPI
[![Build Status](https://travis-ci.org/IoTUDresden/DROiTAPI.svg?branch=master)](https://travis-ci.org/IoTUDresden/DROiTAPI)


See the following publication for more detail:

Seiger, R., Seidl, C., Assmann, U., & Schlegel, T. (2015, July). A Capability-based Framework for Programming Small Domestic Service Robots. In Proceedings of the 2015 Joint MORSE/VAO Workshop on Model-Driven Robot Software Engineering and View-based Software-Engineering (pp. 49-54). ACM.

Hints:

- This project is not maintained anymore.
- The API requires ROSBridge running on the respective robot.
- It was developed and tested for ROS Groovy.

Deployment:

- run mvn deploy
- copy repo/update-directory-index.sh to target/repo and execute it
  - directory index.html will be created for all folders
- copy the content of target/repo to the gh-pages branch

# add the repo for this api to you project

```
<repositories>
	<repository>
		<id>DROiTAPI-Repo</id>
		<url>https://IoTUDresden.github.io/DROiTAPI/repo</url>
	</repository>
</repositories>

<dependencies>
	<dependency>
		<groupId>eu.vicci.driver</groupId>
		<artifactId>eu.vicci.driver.turtlebot</artifactId>
		<version>0.0.1-SNAPSHOT</version>
	</dependency>
</dependencies>
```


