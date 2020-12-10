## MyFirstPlugin

A location for me to practice developing spigot plugins. Just for fun!

#### Compilation

I'm using IntelliJ to compile my classes into the jar located in the out folder.

#### Usage

Drop the MyFirstPlugin.jar file into your plugins folder before you start up a spigot server.

#### File Structure
 - `src`: location of the source files in their respective packages
   - `us.wendell.MyFirstPlugin` is home to the plugin's main functionality like startup 
     and shutdown and command handling
 - `out`: location of the compiled jar which you can drop into your plugins folder
 - `build.xml`: includes a target for copying plugin jar to my local test server