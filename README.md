## Minecraft Manhunt

A copy of features which are found in Dream's manhunt videos. 
The plugin is just for fun and mostly for practice/getting used to 
spigot api features!

Disclaimer: No outside assistance was used in the making of this plugin, 
including but not limited to source code, jars, or other developers.

#### Compilation

I'm using IntelliJ to compile my classes into the jar artifact located in the out folder. 
One day, I will include the actual javac command used to compile everything.

#### Usage

Drop the MinecraftManhunt.jar file into your plugins folder before you start up a spigot server.

###### Commands

1. `/hunters {add|remove|clear|see} [usernames ...]`
2. `/runners {add|remove|clear|see} [usernames ...]`

#### File Structure

 - `src`: location of the source files in their respective packages
   - `us.wendell.MinecraftManhunt` is home to the plugin's main functionality like startup,
     shutdown, and command handling
 - `out`: location of the compiled jar which you can drop into your plugins folder
 - `build.xml`: includes a target for copying plugin jar to my local test server

#### License

Feel free to use or distribute as you wish! No restrictions.
