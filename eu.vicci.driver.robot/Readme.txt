- coordinates in double format (e.g., Position.x and Position.y are given in meters)
- functionality has to be implemented that obstacle avoidance in ROS is activated when the bump sensor of the TurtleBot is triggered
- libraries, such as ROS-Bridge, should either be placed in a separate project or, in the case of jar files, in the "libs" folder of the most general dependent project (export libs for other projects!) 

- TODOs:
  - Have the location manager connect to a central server instead of loading/saving data locally once the server is in place.
  - Implement locking of a robot once the central server is in place so that a robot can only be controlled by a single client at a time.
  - Have another interface for obstacle detection using camera, bump sensor etc. and make the Robot implement that in the distant future.
  - Christoph: What about a method "moveAndGrab(GrabbingTarget grabbingTarget)"? Would tie the grabber to the movement of the robot! Should be in robot!

  - LocationManager might have to be extended to some kind of WorldKnowledge that serves as storage and information source for locations, items, maps, other robots etc. (Facade for possible SeMiWa connection).
  - TurtleBotDockingStation still needs to be moved down to the TurtleBot project but serialization mechanism of lcoation manager is still too clumsy to operate for that.
  

