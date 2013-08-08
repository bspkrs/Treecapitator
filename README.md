Treecapitator
=================
DaftPVF's Treecapitator mod for Minecraft.  Takes trees down in one blow.  Most popular mods supported by default.
This repo contains source files for ModLoader and Forge (dependant on bspkrsCore).

### Links of Interest
 - [Official Minecraft Forum Thread](http://www.minecraftforum.net/topic/1009577-)
 - [ModLoader Downloads](http://bspk.rs/MC/TreeCapitator_ML/index.html)
 - [Forge Downloads](http://bspk.rs/MC/TreeCapitator_Forge/index.html)
 - [Issue Tracking System](https://github.com/bspkrs/Treecapitator/issues)
 
* * *

#### How to install and use the source code ####

1. Download the latest recommended MinecraftForge src distribution.
2. Extract the Forge src zip file and run install.bat/.sh in the forge folder.
3. Clone this git repo to whatever location you like.
4. Use the eclipse folder in your Forge/MCP setup as your Eclipse workspace.
5. Under the Minecraft project, add the "src" folder in the git repo as a linked folder (rename if necessary) and add it as a source folder.
6. Copy TreeCapitator_dummy.jar from resources/mods to the mcp/jars/mods folder.

#### How to build from the source code ####

1. Download and install Apache Ant on your system. Make sure it is available on the path environment variable.
2. In the Treecapitator repo folder, make a copy of build.properties_example and name it build.properties.
3. Edit the values in build.properties to contain valid paths on your system for the 3 properties. Details can be found in build.properties_example.
4. From a console window run "ant" from the Treecapitator repo folder. The build will create its output in the bin folder.
5. Install the resulting mod archive by copying it to the mods folder on the client or server. The Forge version of the mod is universal and works on both the client and the server.

* * *

<a rel="license" href="http://creativecommons.org/licenses/by-nc-sa/3.0/"><img alt="Creative Commons License" style="border-width:0" src="http://i.creativecommons.org/l/by-nc-sa/3.0/88x31.png" /></a><br />This work is licensed under a <a rel="license" href="http://creativecommons.org/licenses/by-nc-sa/3.0/">Creative Commons Attribution-NonCommercial-ShareAlike 3.0 Unported License</a>.
